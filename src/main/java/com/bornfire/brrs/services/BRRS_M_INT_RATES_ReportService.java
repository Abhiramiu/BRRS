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

import com.bornfire.brrs.entities.M_INT_RATES_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_Detail_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_RESUB_Detail_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_RESUB_Summary_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_Summary_Entity;
import com.bornfire.brrs.entities.UserProfileRep;

@Service

public class BRRS_M_INT_RATES_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_INT_RATES_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	AuditService auditService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	UserProfileRep userProfileRep;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// =========================================================
	// JDBC QUERY METHODS
	// =========================================================

	public List<M_INT_RATES_Summary_Entity> getSummaryByDate(Date reportDate) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_INT_RATES_SUMMARYTABLE WHERE REPORT_DATE = ?",
			new Object[]{reportDate}, new M_INT_RATESSummaryRowMapper());
	}

	public List<M_INT_RATES_Detail_Entity> getDetailByDate(Date reportDate) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_INT_RATES_DETAILTABLE WHERE REPORT_DATE = ?",
			new Object[]{reportDate}, new M_INT_RATESDetailRowMapper());
	}

	public List<M_INT_RATES_Archival_Summary_Entity> getArchivalSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_INT_RATES_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
			new Object[]{reportDate, version}, new M_INT_RATESArchivalSummaryRowMapper());
	}

	public List<M_INT_RATES_Archival_Summary_Entity> getArchivalSummaryWithVersionAll() {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_INT_RATES_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC",
			new M_INT_RATESArchivalSummaryRowMapper());
	}

	public List<M_INT_RATES_Archival_Detail_Entity> getArchivalDetailByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_INT_RATES_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
			new Object[]{reportDate, version}, new M_INT_RATESArchivalDetailRowMapper());
	}

	public List<M_INT_RATES_RESUB_Summary_Entity> getResubSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_INT_RATES_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
			new Object[]{reportDate, version}, new M_INT_RATESResubSummaryRowMapper());
	}

	public List<M_INT_RATES_RESUB_Detail_Entity> getResubDetailByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_INT_RATES_RESUB_DETAILTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
			new Object[]{reportDate, version}, new M_INT_RATESResubDetailRowMapper());
	}

	public BigDecimal findMaxResubVersion(Date reportDate) {
		return jdbcTemplate.queryForObject(
			"SELECT MAX(REPORT_VERSION) FROM BRRS_M_INT_RATES_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ?",
			new Object[]{reportDate}, BigDecimal.class);
	}

	// =========================================================
	// JDBC WRITE METHODS
	// =========================================================

	private static final String R_COLS =
		"R11_LENDING,R11_NOMINAL_INTEREST_RATE,R11_AVG_EFFECTIVE_RATE,R11_VOLUME," +
		"R12_LENDING,R12_NOMINAL_INTEREST_RATE,R12_AVG_EFFECTIVE_RATE,R12_VOLUME," +
		"R13_LENDING,R13_NOMINAL_INTEREST_RATE,R13_AVG_EFFECTIVE_RATE,R13_VOLUME," +
		"R14_LENDING,R14_NOMINAL_INTEREST_RATE,R14_AVG_EFFECTIVE_RATE,R14_VOLUME," +
		"R15_LENDING,R15_NOMINAL_INTEREST_RATE,R15_AVG_EFFECTIVE_RATE,R15_VOLUME," +
		"R16_LENDING,R16_NOMINAL_INTEREST_RATE,R16_AVG_EFFECTIVE_RATE,R16_VOLUME," +
		"R17_LENDING,R17_NOMINAL_INTEREST_RATE,R17_AVG_EFFECTIVE_RATE,R17_VOLUME," +
		"R18_LENDING,R18_NOMINAL_INTEREST_RATE,R18_AVG_EFFECTIVE_RATE,R18_VOLUME," +
		"R19_LENDING,R19_NOMINAL_INTEREST_RATE,R19_AVG_EFFECTIVE_RATE,R19_VOLUME," +
		"R20_LENDING,R20_NOMINAL_INTEREST_RATE,R20_AVG_EFFECTIVE_RATE,R20_VOLUME," +
		"R21_LENDING,R21_NOMINAL_INTEREST_RATE,R21_AVG_EFFECTIVE_RATE,R21_VOLUME," +
		"R22_LENDING,R22_NOMINAL_INTEREST_RATE,R22_AVG_EFFECTIVE_RATE,R22_VOLUME," +
		"R23_LENDING,R23_NOMINAL_INTEREST_RATE,R23_AVG_EFFECTIVE_RATE,R23_VOLUME," +
		"R24_LENDING,R24_NOMINAL_INTEREST_RATE,R24_AVG_EFFECTIVE_RATE,R24_VOLUME," +
		"R25_LENDING,R25_NOMINAL_INTEREST_RATE,R25_AVG_EFFECTIVE_RATE,R25_VOLUME," +
		"R26_LENDING,R26_NOMINAL_INTEREST_RATE,R26_AVG_EFFECTIVE_RATE,R26_VOLUME," +
		"R27_LENDING,R27_NOMINAL_INTEREST_RATE,R27_AVG_EFFECTIVE_RATE,R27_VOLUME," +
		"R28_LENDING,R28_NOMINAL_INTEREST_RATE,R28_AVG_EFFECTIVE_RATE,R28_VOLUME," +
		"R29_LENDING,R29_NOMINAL_INTEREST_RATE,R29_AVG_EFFECTIVE_RATE,R29_VOLUME," +
		"R30_LENDING,R30_NOMINAL_INTEREST_RATE,R30_AVG_EFFECTIVE_RATE,R30_VOLUME," +
		"R31_LENDING,R31_NOMINAL_INTEREST_RATE,R31_AVG_EFFECTIVE_RATE,R31_VOLUME," +
		"R32_LENDING,R32_NOMINAL_INTEREST_RATE,R32_AVG_EFFECTIVE_RATE,R32_VOLUME," +
		"R33_LENDING,R33_NOMINAL_INTEREST_RATE,R33_AVG_EFFECTIVE_RATE,R33_VOLUME," +
		"R34_LENDING,R34_NOMINAL_INTEREST_RATE,R34_AVG_EFFECTIVE_RATE,R34_VOLUME," +
		"R35_LENDING,R35_NOMINAL_INTEREST_RATE,R35_AVG_EFFECTIVE_RATE,R35_VOLUME," +
		"R36_LENDING,R36_NOMINAL_INTEREST_RATE,R36_AVG_EFFECTIVE_RATE,R36_VOLUME," +
		"R37_LENDING,R37_NOMINAL_INTEREST_RATE,R37_AVG_EFFECTIVE_RATE,R37_VOLUME," +
		"R38_LENDING,R38_NOMINAL_INTEREST_RATE,R38_AVG_EFFECTIVE_RATE,R38_VOLUME," +
		"R39_LENDING,R39_NOMINAL_INTEREST_RATE,R39_AVG_EFFECTIVE_RATE,R39_VOLUME," +
		"R40_LENDING,R40_NOMINAL_INTEREST_RATE,R40_AVG_EFFECTIVE_RATE,R40_VOLUME," +
		"R41_LENDING,R41_NOMINAL_INTEREST_RATE,R41_AVG_EFFECTIVE_RATE,R41_VOLUME," +
		"R42_LENDING,R42_NOMINAL_INTEREST_RATE,R42_AVG_EFFECTIVE_RATE,R42_VOLUME";

	private static final String R_FIELDS_SET =
		"R11_LENDING=?,R11_NOMINAL_INTEREST_RATE=?,R11_AVG_EFFECTIVE_RATE=?,R11_VOLUME=?," +
		"R12_LENDING=?,R12_NOMINAL_INTEREST_RATE=?,R12_AVG_EFFECTIVE_RATE=?,R12_VOLUME=?," +
		"R13_LENDING=?,R13_NOMINAL_INTEREST_RATE=?,R13_AVG_EFFECTIVE_RATE=?,R13_VOLUME=?," +
		"R14_LENDING=?,R14_NOMINAL_INTEREST_RATE=?,R14_AVG_EFFECTIVE_RATE=?,R14_VOLUME=?," +
		"R15_LENDING=?,R15_NOMINAL_INTEREST_RATE=?,R15_AVG_EFFECTIVE_RATE=?,R15_VOLUME=?," +
		"R16_LENDING=?,R16_NOMINAL_INTEREST_RATE=?,R16_AVG_EFFECTIVE_RATE=?,R16_VOLUME=?," +
		"R17_LENDING=?,R17_NOMINAL_INTEREST_RATE=?,R17_AVG_EFFECTIVE_RATE=?,R17_VOLUME=?," +
		"R18_LENDING=?,R18_NOMINAL_INTEREST_RATE=?,R18_AVG_EFFECTIVE_RATE=?,R18_VOLUME=?," +
		"R19_LENDING=?,R19_NOMINAL_INTEREST_RATE=?,R19_AVG_EFFECTIVE_RATE=?,R19_VOLUME=?," +
		"R20_LENDING=?,R20_NOMINAL_INTEREST_RATE=?,R20_AVG_EFFECTIVE_RATE=?,R20_VOLUME=?," +
		"R21_LENDING=?,R21_NOMINAL_INTEREST_RATE=?,R21_AVG_EFFECTIVE_RATE=?,R21_VOLUME=?," +
		"R22_LENDING=?,R22_NOMINAL_INTEREST_RATE=?,R22_AVG_EFFECTIVE_RATE=?,R22_VOLUME=?," +
		"R23_LENDING=?,R23_NOMINAL_INTEREST_RATE=?,R23_AVG_EFFECTIVE_RATE=?,R23_VOLUME=?," +
		"R24_LENDING=?,R24_NOMINAL_INTEREST_RATE=?,R24_AVG_EFFECTIVE_RATE=?,R24_VOLUME=?," +
		"R25_LENDING=?,R25_NOMINAL_INTEREST_RATE=?,R25_AVG_EFFECTIVE_RATE=?,R25_VOLUME=?," +
		"R26_LENDING=?,R26_NOMINAL_INTEREST_RATE=?,R26_AVG_EFFECTIVE_RATE=?,R26_VOLUME=?," +
		"R27_LENDING=?,R27_NOMINAL_INTEREST_RATE=?,R27_AVG_EFFECTIVE_RATE=?,R27_VOLUME=?," +
		"R28_LENDING=?,R28_NOMINAL_INTEREST_RATE=?,R28_AVG_EFFECTIVE_RATE=?,R28_VOLUME=?," +
		"R29_LENDING=?,R29_NOMINAL_INTEREST_RATE=?,R29_AVG_EFFECTIVE_RATE=?,R29_VOLUME=?," +
		"R30_LENDING=?,R30_NOMINAL_INTEREST_RATE=?,R30_AVG_EFFECTIVE_RATE=?,R30_VOLUME=?," +
		"R31_LENDING=?,R31_NOMINAL_INTEREST_RATE=?,R31_AVG_EFFECTIVE_RATE=?,R31_VOLUME=?," +
		"R32_LENDING=?,R32_NOMINAL_INTEREST_RATE=?,R32_AVG_EFFECTIVE_RATE=?,R32_VOLUME=?," +
		"R33_LENDING=?,R33_NOMINAL_INTEREST_RATE=?,R33_AVG_EFFECTIVE_RATE=?,R33_VOLUME=?," +
		"R34_LENDING=?,R34_NOMINAL_INTEREST_RATE=?,R34_AVG_EFFECTIVE_RATE=?,R34_VOLUME=?," +
		"R35_LENDING=?,R35_NOMINAL_INTEREST_RATE=?,R35_AVG_EFFECTIVE_RATE=?,R35_VOLUME=?," +
		"R36_LENDING=?,R36_NOMINAL_INTEREST_RATE=?,R36_AVG_EFFECTIVE_RATE=?,R36_VOLUME=?," +
		"R37_LENDING=?,R37_NOMINAL_INTEREST_RATE=?,R37_AVG_EFFECTIVE_RATE=?,R37_VOLUME=?," +
		"R38_LENDING=?,R38_NOMINAL_INTEREST_RATE=?,R38_AVG_EFFECTIVE_RATE=?,R38_VOLUME=?," +
		"R39_LENDING=?,R39_NOMINAL_INTEREST_RATE=?,R39_AVG_EFFECTIVE_RATE=?,R39_VOLUME=?," +
		"R40_LENDING=?,R40_NOMINAL_INTEREST_RATE=?,R40_AVG_EFFECTIVE_RATE=?,R40_VOLUME=?," +
		"R41_LENDING=?,R41_NOMINAL_INTEREST_RATE=?,R41_AVG_EFFECTIVE_RATE=?,R41_VOLUME=?," +
		"R42_LENDING=?,R42_NOMINAL_INTEREST_RATE=?,R42_AVG_EFFECTIVE_RATE=?,R42_VOLUME=?";

	private static final String R_PLACEHOLDERS =
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?," +
		"?,?,?,?";

	private Object[] rFieldValues(M_INT_RATES_Summary_Entity e) {
		return new Object[]{
			e.getR11_LENDING(), e.getR11_NOMINAL_INTEREST_RATE(), e.getR11_AVG_EFFECTIVE_RATE(), e.getR11_VOLUME(),
			e.getR12_LENDING(), e.getR12_NOMINAL_INTEREST_RATE(), e.getR12_AVG_EFFECTIVE_RATE(), e.getR12_VOLUME(),
			e.getR13_LENDING(), e.getR13_NOMINAL_INTEREST_RATE(), e.getR13_AVG_EFFECTIVE_RATE(), e.getR13_VOLUME(),
			e.getR14_LENDING(), e.getR14_NOMINAL_INTEREST_RATE(), e.getR14_AVG_EFFECTIVE_RATE(), e.getR14_VOLUME(),

			e.getR15_LENDING(), e.getR15_NOMINAL_INTEREST_RATE(), e.getR15_AVG_EFFECTIVE_RATE(), e.getR15_VOLUME(),
			e.getR16_LENDING(), e.getR16_NOMINAL_INTEREST_RATE(), e.getR16_AVG_EFFECTIVE_RATE(), e.getR16_VOLUME(),
			e.getR17_LENDING(), e.getR17_NOMINAL_INTEREST_RATE(), e.getR17_AVG_EFFECTIVE_RATE(), e.getR17_VOLUME(),
			e.getR18_LENDING(), e.getR18_NOMINAL_INTEREST_RATE(), e.getR18_AVG_EFFECTIVE_RATE(), e.getR18_VOLUME(),

			e.getR19_LENDING(), e.getR19_NOMINAL_INTEREST_RATE(), e.getR19_AVG_EFFECTIVE_RATE(), e.getR19_VOLUME(),
			e.getR20_LENDING(), e.getR20_NOMINAL_INTEREST_RATE(), e.getR20_AVG_EFFECTIVE_RATE(), e.getR20_VOLUME(),
			e.getR21_LENDING(), e.getR21_NOMINAL_INTEREST_RATE(), e.getR21_AVG_EFFECTIVE_RATE(), e.getR21_VOLUME(),
			e.getR22_LENDING(), e.getR22_NOMINAL_INTEREST_RATE(), e.getR22_AVG_EFFECTIVE_RATE(), e.getR22_VOLUME(),

			e.getR23_LENDING(), e.getR23_NOMINAL_INTEREST_RATE(), e.getR23_AVG_EFFECTIVE_RATE(), e.getR23_VOLUME(),
			e.getR24_LENDING(), e.getR24_NOMINAL_INTEREST_RATE(), e.getR24_AVG_EFFECTIVE_RATE(), e.getR24_VOLUME(),
			e.getR25_LENDING(), e.getR25_NOMINAL_INTEREST_RATE(), e.getR25_AVG_EFFECTIVE_RATE(), e.getR25_VOLUME(),
			e.getR26_LENDING(), e.getR26_NOMINAL_INTEREST_RATE(), e.getR26_AVG_EFFECTIVE_RATE(), e.getR26_VOLUME(),

			e.getR27_LENDING(), e.getR27_NOMINAL_INTEREST_RATE(), e.getR27_AVG_EFFECTIVE_RATE(), e.getR27_VOLUME(),
			e.getR28_LENDING(), e.getR28_NOMINAL_INTEREST_RATE(), e.getR28_AVG_EFFECTIVE_RATE(), e.getR28_VOLUME(),
			e.getR29_LENDING(), e.getR29_NOMINAL_INTEREST_RATE(), e.getR29_AVG_EFFECTIVE_RATE(), e.getR29_VOLUME(),
			e.getR30_LENDING(), e.getR30_NOMINAL_INTEREST_RATE(), e.getR30_AVG_EFFECTIVE_RATE(), e.getR30_VOLUME(),

			e.getR31_LENDING(), e.getR31_NOMINAL_INTEREST_RATE(), e.getR31_AVG_EFFECTIVE_RATE(), e.getR31_VOLUME(),
			e.getR32_LENDING(), e.getR32_NOMINAL_INTEREST_RATE(), e.getR32_AVG_EFFECTIVE_RATE(), e.getR32_VOLUME(),
			e.getR33_LENDING(), e.getR33_NOMINAL_INTEREST_RATE(), e.getR33_AVG_EFFECTIVE_RATE(), e.getR33_VOLUME(),
			e.getR34_LENDING(), e.getR34_NOMINAL_INTEREST_RATE(), e.getR34_AVG_EFFECTIVE_RATE(), e.getR34_VOLUME(),

			e.getR35_LENDING(), e.getR35_NOMINAL_INTEREST_RATE(), e.getR35_AVG_EFFECTIVE_RATE(), e.getR35_VOLUME(),
			e.getR36_LENDING(), e.getR36_NOMINAL_INTEREST_RATE(), e.getR36_AVG_EFFECTIVE_RATE(), e.getR36_VOLUME(),
			e.getR37_LENDING(), e.getR37_NOMINAL_INTEREST_RATE(), e.getR37_AVG_EFFECTIVE_RATE(), e.getR37_VOLUME(),
			e.getR38_LENDING(), e.getR38_NOMINAL_INTEREST_RATE(), e.getR38_AVG_EFFECTIVE_RATE(), e.getR38_VOLUME(),

			e.getR39_LENDING(), e.getR39_NOMINAL_INTEREST_RATE(), e.getR39_AVG_EFFECTIVE_RATE(), e.getR39_VOLUME(),
			e.getR40_LENDING(), e.getR40_NOMINAL_INTEREST_RATE(), e.getR40_AVG_EFFECTIVE_RATE(), e.getR40_VOLUME(),
			e.getR41_LENDING(), e.getR41_NOMINAL_INTEREST_RATE(), e.getR41_AVG_EFFECTIVE_RATE(), e.getR41_VOLUME(),
			e.getR42_LENDING(), e.getR42_NOMINAL_INTEREST_RATE(), e.getR42_AVG_EFFECTIVE_RATE(), e.getR42_VOLUME()
		};
	}

	private Object[] rFieldValues(M_INT_RATES_Detail_Entity e) {
		return new Object[]{
			e.getR11_LENDING(), e.getR11_NOMINAL_INTEREST_RATE(), e.getR11_AVG_EFFECTIVE_RATE(), e.getR11_VOLUME(),
			e.getR12_LENDING(), e.getR12_NOMINAL_INTEREST_RATE(), e.getR12_AVG_EFFECTIVE_RATE(), e.getR12_VOLUME(),
			e.getR13_LENDING(), e.getR13_NOMINAL_INTEREST_RATE(), e.getR13_AVG_EFFECTIVE_RATE(), e.getR13_VOLUME(),
			e.getR14_LENDING(), e.getR14_NOMINAL_INTEREST_RATE(), e.getR14_AVG_EFFECTIVE_RATE(), e.getR14_VOLUME(),

			e.getR15_LENDING(), e.getR15_NOMINAL_INTEREST_RATE(), e.getR15_AVG_EFFECTIVE_RATE(), e.getR15_VOLUME(),
			e.getR16_LENDING(), e.getR16_NOMINAL_INTEREST_RATE(), e.getR16_AVG_EFFECTIVE_RATE(), e.getR16_VOLUME(),
			e.getR17_LENDING(), e.getR17_NOMINAL_INTEREST_RATE(), e.getR17_AVG_EFFECTIVE_RATE(), e.getR17_VOLUME(),
			e.getR18_LENDING(), e.getR18_NOMINAL_INTEREST_RATE(), e.getR18_AVG_EFFECTIVE_RATE(), e.getR18_VOLUME(),

			e.getR19_LENDING(), e.getR19_NOMINAL_INTEREST_RATE(), e.getR19_AVG_EFFECTIVE_RATE(), e.getR19_VOLUME(),
			e.getR20_LENDING(), e.getR20_NOMINAL_INTEREST_RATE(), e.getR20_AVG_EFFECTIVE_RATE(), e.getR20_VOLUME(),
			e.getR21_LENDING(), e.getR21_NOMINAL_INTEREST_RATE(), e.getR21_AVG_EFFECTIVE_RATE(), e.getR21_VOLUME(),
			e.getR22_LENDING(), e.getR22_NOMINAL_INTEREST_RATE(), e.getR22_AVG_EFFECTIVE_RATE(), e.getR22_VOLUME(),

			e.getR23_LENDING(), e.getR23_NOMINAL_INTEREST_RATE(), e.getR23_AVG_EFFECTIVE_RATE(), e.getR23_VOLUME(),
			e.getR24_LENDING(), e.getR24_NOMINAL_INTEREST_RATE(), e.getR24_AVG_EFFECTIVE_RATE(), e.getR24_VOLUME(),
			e.getR25_LENDING(), e.getR25_NOMINAL_INTEREST_RATE(), e.getR25_AVG_EFFECTIVE_RATE(), e.getR25_VOLUME(),
			e.getR26_LENDING(), e.getR26_NOMINAL_INTEREST_RATE(), e.getR26_AVG_EFFECTIVE_RATE(), e.getR26_VOLUME(),

			e.getR27_LENDING(), e.getR27_NOMINAL_INTEREST_RATE(), e.getR27_AVG_EFFECTIVE_RATE(), e.getR27_VOLUME(),
			e.getR28_LENDING(), e.getR28_NOMINAL_INTEREST_RATE(), e.getR28_AVG_EFFECTIVE_RATE(), e.getR28_VOLUME(),
			e.getR29_LENDING(), e.getR29_NOMINAL_INTEREST_RATE(), e.getR29_AVG_EFFECTIVE_RATE(), e.getR29_VOLUME(),
			e.getR30_LENDING(), e.getR30_NOMINAL_INTEREST_RATE(), e.getR30_AVG_EFFECTIVE_RATE(), e.getR30_VOLUME(),

			e.getR31_LENDING(), e.getR31_NOMINAL_INTEREST_RATE(), e.getR31_AVG_EFFECTIVE_RATE(), e.getR31_VOLUME(),
			e.getR32_LENDING(), e.getR32_NOMINAL_INTEREST_RATE(), e.getR32_AVG_EFFECTIVE_RATE(), e.getR32_VOLUME(),
			e.getR33_LENDING(), e.getR33_NOMINAL_INTEREST_RATE(), e.getR33_AVG_EFFECTIVE_RATE(), e.getR33_VOLUME(),
			e.getR34_LENDING(), e.getR34_NOMINAL_INTEREST_RATE(), e.getR34_AVG_EFFECTIVE_RATE(), e.getR34_VOLUME(),

			e.getR35_LENDING(), e.getR35_NOMINAL_INTEREST_RATE(), e.getR35_AVG_EFFECTIVE_RATE(), e.getR35_VOLUME(),
			e.getR36_LENDING(), e.getR36_NOMINAL_INTEREST_RATE(), e.getR36_AVG_EFFECTIVE_RATE(), e.getR36_VOLUME(),
			e.getR37_LENDING(), e.getR37_NOMINAL_INTEREST_RATE(), e.getR37_AVG_EFFECTIVE_RATE(), e.getR37_VOLUME(),
			e.getR38_LENDING(), e.getR38_NOMINAL_INTEREST_RATE(), e.getR38_AVG_EFFECTIVE_RATE(), e.getR38_VOLUME(),

			e.getR39_LENDING(), e.getR39_NOMINAL_INTEREST_RATE(), e.getR39_AVG_EFFECTIVE_RATE(), e.getR39_VOLUME(),
			e.getR40_LENDING(), e.getR40_NOMINAL_INTEREST_RATE(), e.getR40_AVG_EFFECTIVE_RATE(), e.getR40_VOLUME(),
			e.getR41_LENDING(), e.getR41_NOMINAL_INTEREST_RATE(), e.getR41_AVG_EFFECTIVE_RATE(), e.getR41_VOLUME(),
			e.getR42_LENDING(), e.getR42_NOMINAL_INTEREST_RATE(), e.getR42_AVG_EFFECTIVE_RATE(), e.getR42_VOLUME()
		};
	}

	private Object[] rFieldValues(M_INT_RATES_Archival_Summary_Entity e) {
		return new Object[]{
			e.getR11_LENDING(), e.getR11_NOMINAL_INTEREST_RATE(), e.getR11_AVG_EFFECTIVE_RATE(), e.getR11_VOLUME(),
			e.getR12_LENDING(), e.getR12_NOMINAL_INTEREST_RATE(), e.getR12_AVG_EFFECTIVE_RATE(), e.getR12_VOLUME(),
			e.getR13_LENDING(), e.getR13_NOMINAL_INTEREST_RATE(), e.getR13_AVG_EFFECTIVE_RATE(), e.getR13_VOLUME(),
			e.getR14_LENDING(), e.getR14_NOMINAL_INTEREST_RATE(), e.getR14_AVG_EFFECTIVE_RATE(), e.getR14_VOLUME(),

			e.getR15_LENDING(), e.getR15_NOMINAL_INTEREST_RATE(), e.getR15_AVG_EFFECTIVE_RATE(), e.getR15_VOLUME(),
			e.getR16_LENDING(), e.getR16_NOMINAL_INTEREST_RATE(), e.getR16_AVG_EFFECTIVE_RATE(), e.getR16_VOLUME(),
			e.getR17_LENDING(), e.getR17_NOMINAL_INTEREST_RATE(), e.getR17_AVG_EFFECTIVE_RATE(), e.getR17_VOLUME(),
			e.getR18_LENDING(), e.getR18_NOMINAL_INTEREST_RATE(), e.getR18_AVG_EFFECTIVE_RATE(), e.getR18_VOLUME(),

			e.getR19_LENDING(), e.getR19_NOMINAL_INTEREST_RATE(), e.getR19_AVG_EFFECTIVE_RATE(), e.getR19_VOLUME(),
			e.getR20_LENDING(), e.getR20_NOMINAL_INTEREST_RATE(), e.getR20_AVG_EFFECTIVE_RATE(), e.getR20_VOLUME(),
			e.getR21_LENDING(), e.getR21_NOMINAL_INTEREST_RATE(), e.getR21_AVG_EFFECTIVE_RATE(), e.getR21_VOLUME(),
			e.getR22_LENDING(), e.getR22_NOMINAL_INTEREST_RATE(), e.getR22_AVG_EFFECTIVE_RATE(), e.getR22_VOLUME(),

			e.getR23_LENDING(), e.getR23_NOMINAL_INTEREST_RATE(), e.getR23_AVG_EFFECTIVE_RATE(), e.getR23_VOLUME(),
			e.getR24_LENDING(), e.getR24_NOMINAL_INTEREST_RATE(), e.getR24_AVG_EFFECTIVE_RATE(), e.getR24_VOLUME(),
			e.getR25_LENDING(), e.getR25_NOMINAL_INTEREST_RATE(), e.getR25_AVG_EFFECTIVE_RATE(), e.getR25_VOLUME(),
			e.getR26_LENDING(), e.getR26_NOMINAL_INTEREST_RATE(), e.getR26_AVG_EFFECTIVE_RATE(), e.getR26_VOLUME(),

			e.getR27_LENDING(), e.getR27_NOMINAL_INTEREST_RATE(), e.getR27_AVG_EFFECTIVE_RATE(), e.getR27_VOLUME(),
			e.getR28_LENDING(), e.getR28_NOMINAL_INTEREST_RATE(), e.getR28_AVG_EFFECTIVE_RATE(), e.getR28_VOLUME(),
			e.getR29_LENDING(), e.getR29_NOMINAL_INTEREST_RATE(), e.getR29_AVG_EFFECTIVE_RATE(), e.getR29_VOLUME(),
			e.getR30_LENDING(), e.getR30_NOMINAL_INTEREST_RATE(), e.getR30_AVG_EFFECTIVE_RATE(), e.getR30_VOLUME(),

			e.getR31_LENDING(), e.getR31_NOMINAL_INTEREST_RATE(), e.getR31_AVG_EFFECTIVE_RATE(), e.getR31_VOLUME(),
			e.getR32_LENDING(), e.getR32_NOMINAL_INTEREST_RATE(), e.getR32_AVG_EFFECTIVE_RATE(), e.getR32_VOLUME(),
			e.getR33_LENDING(), e.getR33_NOMINAL_INTEREST_RATE(), e.getR33_AVG_EFFECTIVE_RATE(), e.getR33_VOLUME(),
			e.getR34_LENDING(), e.getR34_NOMINAL_INTEREST_RATE(), e.getR34_AVG_EFFECTIVE_RATE(), e.getR34_VOLUME(),

			e.getR35_LENDING(), e.getR35_NOMINAL_INTEREST_RATE(), e.getR35_AVG_EFFECTIVE_RATE(), e.getR35_VOLUME(),
			e.getR36_LENDING(), e.getR36_NOMINAL_INTEREST_RATE(), e.getR36_AVG_EFFECTIVE_RATE(), e.getR36_VOLUME(),
			e.getR37_LENDING(), e.getR37_NOMINAL_INTEREST_RATE(), e.getR37_AVG_EFFECTIVE_RATE(), e.getR37_VOLUME(),
			e.getR38_LENDING(), e.getR38_NOMINAL_INTEREST_RATE(), e.getR38_AVG_EFFECTIVE_RATE(), e.getR38_VOLUME(),

			e.getR39_LENDING(), e.getR39_NOMINAL_INTEREST_RATE(), e.getR39_AVG_EFFECTIVE_RATE(), e.getR39_VOLUME(),
			e.getR40_LENDING(), e.getR40_NOMINAL_INTEREST_RATE(), e.getR40_AVG_EFFECTIVE_RATE(), e.getR40_VOLUME(),
			e.getR41_LENDING(), e.getR41_NOMINAL_INTEREST_RATE(), e.getR41_AVG_EFFECTIVE_RATE(), e.getR41_VOLUME(),
			e.getR42_LENDING(), e.getR42_NOMINAL_INTEREST_RATE(), e.getR42_AVG_EFFECTIVE_RATE(), e.getR42_VOLUME()
		};
	}

	private Object[] rFieldValues(M_INT_RATES_Archival_Detail_Entity e) {
		return new Object[]{
			e.getR11_LENDING(), e.getR11_NOMINAL_INTEREST_RATE(), e.getR11_AVG_EFFECTIVE_RATE(), e.getR11_VOLUME(),
			e.getR12_LENDING(), e.getR12_NOMINAL_INTEREST_RATE(), e.getR12_AVG_EFFECTIVE_RATE(), e.getR12_VOLUME(),
			e.getR13_LENDING(), e.getR13_NOMINAL_INTEREST_RATE(), e.getR13_AVG_EFFECTIVE_RATE(), e.getR13_VOLUME(),
			e.getR14_LENDING(), e.getR14_NOMINAL_INTEREST_RATE(), e.getR14_AVG_EFFECTIVE_RATE(), e.getR14_VOLUME(),

			e.getR15_LENDING(), e.getR15_NOMINAL_INTEREST_RATE(), e.getR15_AVG_EFFECTIVE_RATE(), e.getR15_VOLUME(),
			e.getR16_LENDING(), e.getR16_NOMINAL_INTEREST_RATE(), e.getR16_AVG_EFFECTIVE_RATE(), e.getR16_VOLUME(),
			e.getR17_LENDING(), e.getR17_NOMINAL_INTEREST_RATE(), e.getR17_AVG_EFFECTIVE_RATE(), e.getR17_VOLUME(),
			e.getR18_LENDING(), e.getR18_NOMINAL_INTEREST_RATE(), e.getR18_AVG_EFFECTIVE_RATE(), e.getR18_VOLUME(),

			e.getR19_LENDING(), e.getR19_NOMINAL_INTEREST_RATE(), e.getR19_AVG_EFFECTIVE_RATE(), e.getR19_VOLUME(),
			e.getR20_LENDING(), e.getR20_NOMINAL_INTEREST_RATE(), e.getR20_AVG_EFFECTIVE_RATE(), e.getR20_VOLUME(),
			e.getR21_LENDING(), e.getR21_NOMINAL_INTEREST_RATE(), e.getR21_AVG_EFFECTIVE_RATE(), e.getR21_VOLUME(),
			e.getR22_LENDING(), e.getR22_NOMINAL_INTEREST_RATE(), e.getR22_AVG_EFFECTIVE_RATE(), e.getR22_VOLUME(),

			e.getR23_LENDING(), e.getR23_NOMINAL_INTEREST_RATE(), e.getR23_AVG_EFFECTIVE_RATE(), e.getR23_VOLUME(),
			e.getR24_LENDING(), e.getR24_NOMINAL_INTEREST_RATE(), e.getR24_AVG_EFFECTIVE_RATE(), e.getR24_VOLUME(),
			e.getR25_LENDING(), e.getR25_NOMINAL_INTEREST_RATE(), e.getR25_AVG_EFFECTIVE_RATE(), e.getR25_VOLUME(),
			e.getR26_LENDING(), e.getR26_NOMINAL_INTEREST_RATE(), e.getR26_AVG_EFFECTIVE_RATE(), e.getR26_VOLUME(),

			e.getR27_LENDING(), e.getR27_NOMINAL_INTEREST_RATE(), e.getR27_AVG_EFFECTIVE_RATE(), e.getR27_VOLUME(),
			e.getR28_LENDING(), e.getR28_NOMINAL_INTEREST_RATE(), e.getR28_AVG_EFFECTIVE_RATE(), e.getR28_VOLUME(),
			e.getR29_LENDING(), e.getR29_NOMINAL_INTEREST_RATE(), e.getR29_AVG_EFFECTIVE_RATE(), e.getR29_VOLUME(),
			e.getR30_LENDING(), e.getR30_NOMINAL_INTEREST_RATE(), e.getR30_AVG_EFFECTIVE_RATE(), e.getR30_VOLUME(),

			e.getR31_LENDING(), e.getR31_NOMINAL_INTEREST_RATE(), e.getR31_AVG_EFFECTIVE_RATE(), e.getR31_VOLUME(),
			e.getR32_LENDING(), e.getR32_NOMINAL_INTEREST_RATE(), e.getR32_AVG_EFFECTIVE_RATE(), e.getR32_VOLUME(),
			e.getR33_LENDING(), e.getR33_NOMINAL_INTEREST_RATE(), e.getR33_AVG_EFFECTIVE_RATE(), e.getR33_VOLUME(),
			e.getR34_LENDING(), e.getR34_NOMINAL_INTEREST_RATE(), e.getR34_AVG_EFFECTIVE_RATE(), e.getR34_VOLUME(),

			e.getR35_LENDING(), e.getR35_NOMINAL_INTEREST_RATE(), e.getR35_AVG_EFFECTIVE_RATE(), e.getR35_VOLUME(),
			e.getR36_LENDING(), e.getR36_NOMINAL_INTEREST_RATE(), e.getR36_AVG_EFFECTIVE_RATE(), e.getR36_VOLUME(),
			e.getR37_LENDING(), e.getR37_NOMINAL_INTEREST_RATE(), e.getR37_AVG_EFFECTIVE_RATE(), e.getR37_VOLUME(),
			e.getR38_LENDING(), e.getR38_NOMINAL_INTEREST_RATE(), e.getR38_AVG_EFFECTIVE_RATE(), e.getR38_VOLUME(),

			e.getR39_LENDING(), e.getR39_NOMINAL_INTEREST_RATE(), e.getR39_AVG_EFFECTIVE_RATE(), e.getR39_VOLUME(),
			e.getR40_LENDING(), e.getR40_NOMINAL_INTEREST_RATE(), e.getR40_AVG_EFFECTIVE_RATE(), e.getR40_VOLUME(),
			e.getR41_LENDING(), e.getR41_NOMINAL_INTEREST_RATE(), e.getR41_AVG_EFFECTIVE_RATE(), e.getR41_VOLUME(),
			e.getR42_LENDING(), e.getR42_NOMINAL_INTEREST_RATE(), e.getR42_AVG_EFFECTIVE_RATE(), e.getR42_VOLUME()
		};
	}

	private Object[] rFieldValues(M_INT_RATES_RESUB_Summary_Entity e) {
		return new Object[]{
			e.getR11_LENDING(), e.getR11_NOMINAL_INTEREST_RATE(), e.getR11_AVG_EFFECTIVE_RATE(), e.getR11_VOLUME(),
			e.getR12_LENDING(), e.getR12_NOMINAL_INTEREST_RATE(), e.getR12_AVG_EFFECTIVE_RATE(), e.getR12_VOLUME(),
			e.getR13_LENDING(), e.getR13_NOMINAL_INTEREST_RATE(), e.getR13_AVG_EFFECTIVE_RATE(), e.getR13_VOLUME(),
			e.getR14_LENDING(), e.getR14_NOMINAL_INTEREST_RATE(), e.getR14_AVG_EFFECTIVE_RATE(), e.getR14_VOLUME(),

			e.getR15_LENDING(), e.getR15_NOMINAL_INTEREST_RATE(), e.getR15_AVG_EFFECTIVE_RATE(), e.getR15_VOLUME(),
			e.getR16_LENDING(), e.getR16_NOMINAL_INTEREST_RATE(), e.getR16_AVG_EFFECTIVE_RATE(), e.getR16_VOLUME(),
			e.getR17_LENDING(), e.getR17_NOMINAL_INTEREST_RATE(), e.getR17_AVG_EFFECTIVE_RATE(), e.getR17_VOLUME(),
			e.getR18_LENDING(), e.getR18_NOMINAL_INTEREST_RATE(), e.getR18_AVG_EFFECTIVE_RATE(), e.getR18_VOLUME(),

			e.getR19_LENDING(), e.getR19_NOMINAL_INTEREST_RATE(), e.getR19_AVG_EFFECTIVE_RATE(), e.getR19_VOLUME(),
			e.getR20_LENDING(), e.getR20_NOMINAL_INTEREST_RATE(), e.getR20_AVG_EFFECTIVE_RATE(), e.getR20_VOLUME(),
			e.getR21_LENDING(), e.getR21_NOMINAL_INTEREST_RATE(), e.getR21_AVG_EFFECTIVE_RATE(), e.getR21_VOLUME(),
			e.getR22_LENDING(), e.getR22_NOMINAL_INTEREST_RATE(), e.getR22_AVG_EFFECTIVE_RATE(), e.getR22_VOLUME(),

			e.getR23_LENDING(), e.getR23_NOMINAL_INTEREST_RATE(), e.getR23_AVG_EFFECTIVE_RATE(), e.getR23_VOLUME(),
			e.getR24_LENDING(), e.getR24_NOMINAL_INTEREST_RATE(), e.getR24_AVG_EFFECTIVE_RATE(), e.getR24_VOLUME(),
			e.getR25_LENDING(), e.getR25_NOMINAL_INTEREST_RATE(), e.getR25_AVG_EFFECTIVE_RATE(), e.getR25_VOLUME(),
			e.getR26_LENDING(), e.getR26_NOMINAL_INTEREST_RATE(), e.getR26_AVG_EFFECTIVE_RATE(), e.getR26_VOLUME(),

			e.getR27_LENDING(), e.getR27_NOMINAL_INTEREST_RATE(), e.getR27_AVG_EFFECTIVE_RATE(), e.getR27_VOLUME(),
			e.getR28_LENDING(), e.getR28_NOMINAL_INTEREST_RATE(), e.getR28_AVG_EFFECTIVE_RATE(), e.getR28_VOLUME(),
			e.getR29_LENDING(), e.getR29_NOMINAL_INTEREST_RATE(), e.getR29_AVG_EFFECTIVE_RATE(), e.getR29_VOLUME(),
			e.getR30_LENDING(), e.getR30_NOMINAL_INTEREST_RATE(), e.getR30_AVG_EFFECTIVE_RATE(), e.getR30_VOLUME(),

			e.getR31_LENDING(), e.getR31_NOMINAL_INTEREST_RATE(), e.getR31_AVG_EFFECTIVE_RATE(), e.getR31_VOLUME(),
			e.getR32_LENDING(), e.getR32_NOMINAL_INTEREST_RATE(), e.getR32_AVG_EFFECTIVE_RATE(), e.getR32_VOLUME(),
			e.getR33_LENDING(), e.getR33_NOMINAL_INTEREST_RATE(), e.getR33_AVG_EFFECTIVE_RATE(), e.getR33_VOLUME(),
			e.getR34_LENDING(), e.getR34_NOMINAL_INTEREST_RATE(), e.getR34_AVG_EFFECTIVE_RATE(), e.getR34_VOLUME(),

			e.getR35_LENDING(), e.getR35_NOMINAL_INTEREST_RATE(), e.getR35_AVG_EFFECTIVE_RATE(), e.getR35_VOLUME(),
			e.getR36_LENDING(), e.getR36_NOMINAL_INTEREST_RATE(), e.getR36_AVG_EFFECTIVE_RATE(), e.getR36_VOLUME(),
			e.getR37_LENDING(), e.getR37_NOMINAL_INTEREST_RATE(), e.getR37_AVG_EFFECTIVE_RATE(), e.getR37_VOLUME(),
			e.getR38_LENDING(), e.getR38_NOMINAL_INTEREST_RATE(), e.getR38_AVG_EFFECTIVE_RATE(), e.getR38_VOLUME(),

			e.getR39_LENDING(), e.getR39_NOMINAL_INTEREST_RATE(), e.getR39_AVG_EFFECTIVE_RATE(), e.getR39_VOLUME(),
			e.getR40_LENDING(), e.getR40_NOMINAL_INTEREST_RATE(), e.getR40_AVG_EFFECTIVE_RATE(), e.getR40_VOLUME(),
			e.getR41_LENDING(), e.getR41_NOMINAL_INTEREST_RATE(), e.getR41_AVG_EFFECTIVE_RATE(), e.getR41_VOLUME(),
			e.getR42_LENDING(), e.getR42_NOMINAL_INTEREST_RATE(), e.getR42_AVG_EFFECTIVE_RATE(), e.getR42_VOLUME()
		};
	}

	private Object[] rFieldValues(M_INT_RATES_RESUB_Detail_Entity e) {
		return new Object[]{
			e.getR11_LENDING(), e.getR11_NOMINAL_INTEREST_RATE(), e.getR11_AVG_EFFECTIVE_RATE(), e.getR11_VOLUME(),
			e.getR12_LENDING(), e.getR12_NOMINAL_INTEREST_RATE(), e.getR12_AVG_EFFECTIVE_RATE(), e.getR12_VOLUME(),
			e.getR13_LENDING(), e.getR13_NOMINAL_INTEREST_RATE(), e.getR13_AVG_EFFECTIVE_RATE(), e.getR13_VOLUME(),
			e.getR14_LENDING(), e.getR14_NOMINAL_INTEREST_RATE(), e.getR14_AVG_EFFECTIVE_RATE(), e.getR14_VOLUME(),

			e.getR15_LENDING(), e.getR15_NOMINAL_INTEREST_RATE(), e.getR15_AVG_EFFECTIVE_RATE(), e.getR15_VOLUME(),
			e.getR16_LENDING(), e.getR16_NOMINAL_INTEREST_RATE(), e.getR16_AVG_EFFECTIVE_RATE(), e.getR16_VOLUME(),
			e.getR17_LENDING(), e.getR17_NOMINAL_INTEREST_RATE(), e.getR17_AVG_EFFECTIVE_RATE(), e.getR17_VOLUME(),
			e.getR18_LENDING(), e.getR18_NOMINAL_INTEREST_RATE(), e.getR18_AVG_EFFECTIVE_RATE(), e.getR18_VOLUME(),

			e.getR19_LENDING(), e.getR19_NOMINAL_INTEREST_RATE(), e.getR19_AVG_EFFECTIVE_RATE(), e.getR19_VOLUME(),
			e.getR20_LENDING(), e.getR20_NOMINAL_INTEREST_RATE(), e.getR20_AVG_EFFECTIVE_RATE(), e.getR20_VOLUME(),
			e.getR21_LENDING(), e.getR21_NOMINAL_INTEREST_RATE(), e.getR21_AVG_EFFECTIVE_RATE(), e.getR21_VOLUME(),
			e.getR22_LENDING(), e.getR22_NOMINAL_INTEREST_RATE(), e.getR22_AVG_EFFECTIVE_RATE(), e.getR22_VOLUME(),

			e.getR23_LENDING(), e.getR23_NOMINAL_INTEREST_RATE(), e.getR23_AVG_EFFECTIVE_RATE(), e.getR23_VOLUME(),
			e.getR24_LENDING(), e.getR24_NOMINAL_INTEREST_RATE(), e.getR24_AVG_EFFECTIVE_RATE(), e.getR24_VOLUME(),
			e.getR25_LENDING(), e.getR25_NOMINAL_INTEREST_RATE(), e.getR25_AVG_EFFECTIVE_RATE(), e.getR25_VOLUME(),
			e.getR26_LENDING(), e.getR26_NOMINAL_INTEREST_RATE(), e.getR26_AVG_EFFECTIVE_RATE(), e.getR26_VOLUME(),

			e.getR27_LENDING(), e.getR27_NOMINAL_INTEREST_RATE(), e.getR27_AVG_EFFECTIVE_RATE(), e.getR27_VOLUME(),
			e.getR28_LENDING(), e.getR28_NOMINAL_INTEREST_RATE(), e.getR28_AVG_EFFECTIVE_RATE(), e.getR28_VOLUME(),
			e.getR29_LENDING(), e.getR29_NOMINAL_INTEREST_RATE(), e.getR29_AVG_EFFECTIVE_RATE(), e.getR29_VOLUME(),
			e.getR30_LENDING(), e.getR30_NOMINAL_INTEREST_RATE(), e.getR30_AVG_EFFECTIVE_RATE(), e.getR30_VOLUME(),

			e.getR31_LENDING(), e.getR31_NOMINAL_INTEREST_RATE(), e.getR31_AVG_EFFECTIVE_RATE(), e.getR31_VOLUME(),
			e.getR32_LENDING(), e.getR32_NOMINAL_INTEREST_RATE(), e.getR32_AVG_EFFECTIVE_RATE(), e.getR32_VOLUME(),
			e.getR33_LENDING(), e.getR33_NOMINAL_INTEREST_RATE(), e.getR33_AVG_EFFECTIVE_RATE(), e.getR33_VOLUME(),
			e.getR34_LENDING(), e.getR34_NOMINAL_INTEREST_RATE(), e.getR34_AVG_EFFECTIVE_RATE(), e.getR34_VOLUME(),

			e.getR35_LENDING(), e.getR35_NOMINAL_INTEREST_RATE(), e.getR35_AVG_EFFECTIVE_RATE(), e.getR35_VOLUME(),
			e.getR36_LENDING(), e.getR36_NOMINAL_INTEREST_RATE(), e.getR36_AVG_EFFECTIVE_RATE(), e.getR36_VOLUME(),
			e.getR37_LENDING(), e.getR37_NOMINAL_INTEREST_RATE(), e.getR37_AVG_EFFECTIVE_RATE(), e.getR37_VOLUME(),
			e.getR38_LENDING(), e.getR38_NOMINAL_INTEREST_RATE(), e.getR38_AVG_EFFECTIVE_RATE(), e.getR38_VOLUME(),

			e.getR39_LENDING(), e.getR39_NOMINAL_INTEREST_RATE(), e.getR39_AVG_EFFECTIVE_RATE(), e.getR39_VOLUME(),
			e.getR40_LENDING(), e.getR40_NOMINAL_INTEREST_RATE(), e.getR40_AVG_EFFECTIVE_RATE(), e.getR40_VOLUME(),
			e.getR41_LENDING(), e.getR41_NOMINAL_INTEREST_RATE(), e.getR41_AVG_EFFECTIVE_RATE(), e.getR41_VOLUME(),
			e.getR42_LENDING(), e.getR42_NOMINAL_INTEREST_RATE(), e.getR42_AVG_EFFECTIVE_RATE(), e.getR42_VOLUME()
		};
	}

	private void saveSummary(M_INT_RATES_Summary_Entity e) {
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
			"UPDATE BRRS_M_INT_RATES_SUMMARYTABLE SET " + R_FIELDS_SET +
			",REPORT_VERSION=?,REPORT_FREQUENCY=?,REPORT_CODE=?,REPORT_DESC=?,ENTITY_FLG=?,MODIFY_FLG=?,DEL_FLG=?" +
			" WHERE REPORT_DATE=?", params);
	}

	private void saveDetail(M_INT_RATES_Detail_Entity e) {
		int cnt = jdbcTemplate.queryForObject(
			"SELECT COUNT(*) FROM BRRS_M_INT_RATES_DETAILTABLE WHERE REPORT_DATE=?",
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
				"UPDATE BRRS_M_INT_RATES_DETAILTABLE SET " + R_FIELDS_SET +
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
				"INSERT INTO BRRS_M_INT_RATES_DETAILTABLE (REPORT_DATE," + R_COLS +
				",REPORT_VERSION,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC,ENTITY_FLG,MODIFY_FLG,DEL_FLG) VALUES (?," +
				R_PLACEHOLDERS + ",?,?,?,?,?,?,?)", params);
		}
	}

	private void insertResubSummary(M_INT_RATES_RESUB_Summary_Entity e) {
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
			"INSERT INTO BRRS_M_INT_RATES_RESUB_SUMMARYTABLE (" + R_COLS +
			",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC,ENTITY_FLG,MODIFY_FLG,DEL_FLG) VALUES (" +
			R_PLACEHOLDERS + ",?,?,?,?,?,?,?,?,?)", params);
	}

	private void insertResubDetail(M_INT_RATES_RESUB_Detail_Entity e) {
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
			"INSERT INTO BRRS_M_INT_RATES_RESUB_DETAILTABLE (" + R_COLS +
			",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC,ENTITY_FLG,MODIFY_FLG,DEL_FLG) VALUES (" +
			R_PLACEHOLDERS + ",?,?,?,?,?,?,?,?,?)", params);
	}

	private void insertArchivalSummary(M_INT_RATES_Archival_Summary_Entity e) {
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
			"INSERT INTO BRRS_M_INT_RATES_ARCHIVALTABLE_SUMMARY (" + R_COLS +
			",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC,ENTITY_FLG,MODIFY_FLG,DEL_FLG) VALUES (" +
			R_PLACEHOLDERS + ",?,?,?,?,?,?,?,?,?)", params);
	}

	private void insertArchivalDetail(M_INT_RATES_Archival_Detail_Entity e) {
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
			"INSERT INTO BRRS_M_INT_RATES_ARCHIVALTABLE_DETAIL (" + R_COLS +
			",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC,ENTITY_FLG,MODIFY_FLG,DEL_FLG) VALUES (" +
			R_PLACEHOLDERS + ",?,?,?,?,?,?,?,?,?)", params);
	}

	public ModelAndView getM_INTRATESView(
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

	            List<M_INT_RATES_Archival_Summary_Entity> T1Master =
	                    getArchivalSummaryByDateAndVersion(d1, version);

	            System.out.println("Archival Summary Size : " + T1Master.size());

	            mv.addObject("displaymode", "summary");
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ---------- RESUB SUMMARY ----------
	        else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	            List<M_INT_RATES_RESUB_Summary_Entity> T1Master =
	                    getResubSummaryByDateAndVersion(d1, version);

	            System.out.println("Resub Summary Size : " + T1Master.size());

	            mv.addObject("displaymode", "resub");
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ---------- NORMAL SUMMARY ----------
	        else {

	            List<M_INT_RATES_Summary_Entity> T1Master =
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

	                List<M_INT_RATES_Archival_Detail_Entity> T1Master =
	                        getArchivalDetailByDateAndVersion(d1, version);

	                System.out.println("Archival Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "detail");
	                mv.addObject("reportsummary", T1Master);
	            }

	            // ---------- RESUB DETAIL ---------------------------------------------------------------------------------------------------------------------------------
	            else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	                List<M_INT_RATES_RESUB_Detail_Entity> T1Master =
	                        getResubDetailByDateAndVersion(d1, version);

	                System.out.println("Resub Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "resub");
	                mv.addObject("reportsummary", T1Master);
	            }

	            // ---------- NORMAL DETAIL ----------
	            else {

	            	 List<M_INT_RATES_Detail_Entity> T1Master =
		                        getDetailByDate(d1);

	                System.out.println("Normal Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "detail");
	                mv.addObject("reportsummary", T1Master);
	            }
	        }

	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	    mv.setViewName("BRRS/M_INT_RATES");

	    System.out.println("View set to: " + mv.getViewName());

	    return mv;
	}

	
	
	
//	@Transactional
//	public void updateReport(M_INT_RATES_Summary_Entity updatedEntity) {
//
//	    System.out.println("Came to services");
//	    System.out.println("Report Date: " + updatedEntity.getReportDate());
//
//	    // 1️⃣ Fetch existing SUMMARY
//	     M_INT_RATES_Summary_Entity existingSummary =
//	            M_INT_RATES_Summary_Repo.findById(updatedEntity.getReportDate())
//	                    .orElseThrow(() -> new RuntimeException(
//	                            "Summary record not found for REPORT_DATE: " + updatedEntity.getReportDate()));
//
//	    // 2️⃣ Fetch or create DETAIL
//	      M_INT_RATES_Detail_Entity existingDetail =
//	            M_INT_RATES_Detail_Repo.findById(updatedEntity.getReportDate())
//	                    .orElseGet(() -> {
//	                          M_INT_RATES_Detail_Entity d = new   M_INT_RATES_Detail_Entity();
//	                        d.setReport_date(updatedEntity.getReportDate());
//	                        return d;
//	                    });
//
//	  try {
//			// 1️⃣ Loop through R14 to R100
//			for (int i = 11; i <= 42; i++) {
//				String prefix = "R" + i + "_";
//
//				String[] fields = { "LENDING", "NOMINAL_INTEREST_RATE", "AVG_EFFECTIVE_RATE", "VOLUME"};
//
//	            for (String field : fields) {
//
//	                String getterName = "get" + prefix + field;
//	                String setterName = "set" + prefix + field;
//
//	                try {
//	                    Method getter =
//	                              M_INT_RATES_Summary_Entity.class.getMethod(getterName);
//
//	                    Method summarySetter =
//	                              M_INT_RATES_Summary_Entity.class.getMethod(
//	                                    setterName, getter.getReturnType());
//
//	                    Method detailSetter =
//	                              M_INT_RATES_Detail_Entity.class.getMethod(
//	                                    setterName, getter.getReturnType());
//
//	                    Object newValue = getter.invoke(updatedEntity);
//
//	                    // ✅ set into SUMMARY
//	                    summarySetter.invoke(existingSummary, newValue);
//
//	                    // ✅ set into DETAIL
//	                    detailSetter.invoke(existingDetail, newValue);
//
//	                } catch (NoSuchMethodException e) {
//	                    // skip missing fields safely
//	                    continue;
//	                }
//	            }
//	        }
//
//	    } catch (Exception e) {
//	        throw new RuntimeException("Error while updating report fields", e);
//	    }
//
//	    // 3️⃣ Save BOTH (same transaction)
//	    M_INT_RATES_Summary_Repo.save(existingSummary);
//	    M_INT_RATES_Detail_Repo.save(existingDetail);
//	}

	@Transactional
	public void updateReport(M_INT_RATES_Summary_Entity updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReportDate());

	    // 1️⃣ Fetch existing SUMMARY
	    List<M_INT_RATES_Summary_Entity> summaryList =
	            getSummaryByDate(updatedEntity.getReportDate());
	    if (summaryList.isEmpty()) {
	        throw new RuntimeException(
	                "Summary record not found for REPORT_DATE: "
	                        + updatedEntity.getReportDate());
	    }
	    M_INT_RATES_Summary_Entity existingSummary = summaryList.get(0);

	    // 2️⃣ Audit old copy
	    M_INT_RATES_Summary_Entity oldcopy =
	            new M_INT_RATES_Summary_Entity();

	    BeanUtils.copyProperties(
	            existingSummary,
	            oldcopy);

	    // 3️⃣ Fetch or create DETAIL
	    List<M_INT_RATES_Detail_Entity> detailList =
	            getDetailByDate(updatedEntity.getReportDate());
	    M_INT_RATES_Detail_Entity existingDetail;
	    if (detailList.isEmpty()) {
	        existingDetail = new M_INT_RATES_Detail_Entity();
	        existingDetail.setReport_date(updatedEntity.getReportDate());
	    } else {
	        existingDetail = detailList.get(0);
	    }

	    try {

	        // Loop R11 → R42
	        for (int i = 11; i <= 42; i++) {

	            String prefix = "R" + i + "_";

	            String[] fields = {
	                    "LENDING",
	                    "NOMINAL_INTEREST_RATE",
	                    "AVG_EFFECTIVE_RATE",
	                    "VOLUME"
	            };

	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {

	                    Method getter =
	                            M_INT_RATES_Summary_Entity.class
	                                    .getMethod(getterName);

	                    Method summarySetter =
	                            M_INT_RATES_Summary_Entity.class
	                                    .getMethod(
	                                            setterName,
	                                            getter.getReturnType());

	                    Method detailSetter =
	                            M_INT_RATES_Detail_Entity.class
	                                    .getMethod(
	                                            setterName,
	                                            getter.getReturnType());

	                    Object newValue =
	                            getter.invoke(updatedEntity);

	                    // Update Summary
	                    summarySetter.invoke(
	                            existingSummary,
	                            newValue);

	                    // Update Detail
	                    detailSetter.invoke(
	                            existingDetail,
	                            newValue);

	                } catch (NoSuchMethodException e) {
	                    continue;
	                }
	            }
	        }

	    } catch (Exception e) {

	        throw new RuntimeException(
	                "Error while updating report fields",
	                e);
	    }

	    // 4️⃣ Check Changes
	    String changes =
	            auditService.getChanges(
	                    oldcopy,
	                    existingSummary);

	    // 5️⃣ Save Summary & Detail
	    saveSummary(existingSummary);
	    saveDetail(existingDetail);

	    // 6️⃣ Audit Only If Changes Found
	    if (!changes.isEmpty()) {

	        auditService.compareEntitiesmanual(
	                oldcopy,
	                existingSummary,
	                updatedEntity.getReportDate().toString(),
	                "M INT RATES Summary Screen",
	                "BRRS_M_INT_RATES_SUMMARY"
	        );
	    }

	    System.out.println(
	            "M_INT_RATES Summary & Detail Update Completed");
	}
	
	
	
//	@Transactional
//	public void updateResubReport(M_INT_RATES_RESUB_Summary_Entity updatedEntity) {
//
//	    Date reportDate = updatedEntity.getReport_date();
//
//	    // ----------------------------------------------------
//	    // GET CURRENT VERSION FROM RESUB TABLE
//	    // ----------------------------------------------------
//
//	    BigDecimal maxResubVer =
//	        M_INT_RATES_resub_summary_repo.findMaxVersion(reportDate);
//
//	    if (maxResubVer == null)
//	        throw new RuntimeException("No record for: " + reportDate);
//
//	    BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);
//
//	    Date now = new Date();
//
//	    // ====================================================
//	    // 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
//	    // ====================================================
//
//	    M_INT_RATES_RESUB_Summary_Entity resubSummary =
//	        new M_INT_RATES_RESUB_Summary_Entity();
//
//	    BeanUtils.copyProperties(updatedEntity, resubSummary,
//	        "reportDate", "reportVersion", "reportResubDate");
//
//	    resubSummary.setReport_date(reportDate);
//	    resubSummary.setReport_version(newVersion);
//	    resubSummary.setReportResubDate(now);
//
//	    // ====================================================
//	    // 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
//	    // ====================================================
//
//	    M_INT_RATES_RESUB_Detail_Entity resubDetail =
//	        new M_INT_RATES_RESUB_Detail_Entity();
//
//	    BeanUtils.copyProperties(updatedEntity, resubDetail,
//	        "reportDate", "reportVersion", "reportResubDate");
//
//	    resubDetail.setReport_date(reportDate);
//	    resubDetail.setReport_version(newVersion);
//	    resubDetail.setReportResubDate(now);
//
//	    // ====================================================
//	    // 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
//	    // ====================================================
//
//	    M_INT_RATES_Archival_Summary_Entity archSummary =
//	        new M_INT_RATES_Archival_Summary_Entity();
//
//	    BeanUtils.copyProperties(updatedEntity, archSummary,
//	        "reportDate", "reportVersion", "reportResubDate");
//
//	    archSummary.setReport_date(reportDate);
//	    archSummary.setReport_version(newVersion);   // SAME VERSION
//	    archSummary.setReportResubDate(now);
//
//	    // ====================================================
//	    // 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
//	    // ====================================================
//
//	    M_INT_RATES_Archival_Detail_Entity archDetail =
//	        new M_INT_RATES_Archival_Detail_Entity();
//
//	    BeanUtils.copyProperties(updatedEntity, archDetail,
//	        "reportDate", "reportVersion", "reportResubDate");
//
//	    archDetail.setReport_date(reportDate);
//	    archDetail.setReport_version(newVersion);    // SAME VERSION
//	    archDetail.setReportResubDate(now);
//
//	    // ====================================================
//	    // 6️⃣ SAVE ALL WITH SAME DATA
//	    // ====================================================
//
//	    M_INT_RATES_resub_summary_repo.save(resubSummary);
//	    M_INT_RATES_resub_detail_repo.save(resubDetail);
//
//	    M_INT_RATES_Archival_Summary_Repo.save(archSummary);
//	    M_INT_RATES_Archival_Detail_Repo.save(archDetail);
//	}
	
	@Transactional
	public void updateResubReport(
	        M_INT_RATES_RESUB_Summary_Entity updatedEntity) {

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

	    M_INT_RATES_RESUB_Summary_Entity resubSummary =
	            new M_INT_RATES_RESUB_Summary_Entity();

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

	    M_INT_RATES_RESUB_Detail_Entity resubDetail =
	            new M_INT_RATES_RESUB_Detail_Entity();

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

	    M_INT_RATES_Archival_Summary_Entity archSummary =
	            new M_INT_RATES_Archival_Summary_Entity();

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

	    M_INT_RATES_Archival_Detail_Entity archDetail =
	            new M_INT_RATES_Archival_Detail_Entity();

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
	                "M INT RATES Resub Summary",
	                null,
	                "BRRS_M_INT_RATES_RESUB_SUMMARYTABLE"
	        );
	    }

	    System.out.println(
	            "M_INT_RATES Resub Version Created Successfully : "
	                    + newVersion);
	}

		
	//Archival View
	public List<Object[]> getM_INT_RATESArchival() {
	List<Object[]> archivalList = new ArrayList<>();

	try {
		List<M_INT_RATES_Archival_Summary_Entity> repoData = getArchivalSummaryWithVersionAll();

		if (repoData != null && !repoData.isEmpty()) {
			for (M_INT_RATES_Archival_Summary_Entity entity : repoData) {
				Object[] row = new Object[] {
						entity.getReport_date(), 
						entity.getReport_version(), 
						 entity.getReportResubDate()
				};
				archivalList.add(row);
			}

			System.out.println("Fetched " + archivalList.size() + " archival records");
			M_INT_RATES_Archival_Summary_Entity first = repoData.get(0);
			System.out.println("Latest archival version: " + first.getReport_version());
		} else {
			System.out.println("No archival data found.");
		}

	} catch (Exception e) {
		System.err.println("Error fetching M_INT_RATES  Archival data: " + e.getMessage());
		e.printStackTrace();
	}

	return archivalList;
	}

	/// RESUB VIEW



	public List<Object[]> getM_INTRATESResub() {
	    List<Object[]> resubList = new ArrayList<>();
	    try {
	        List<M_INT_RATES_Archival_Summary_Entity> latestArchivalList =
	        		getArchivalSummaryWithVersionAll();

	        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
	            for (M_INT_RATES_Archival_Summary_Entity entity : latestArchivalList) {
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
	        System.err.println("Error fetching M_INT_RATES Resub data: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return resubList;
	}
	
	
	// Normal format Excel

		public byte[] getM_INTRATESExcel(String filename, String reportId, String fromdate, String todate, String currency,
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
					return getExcelM_INTRATESARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);
				} catch (ParseException e) {
					logger.error("Invalid report date format: {}", fromdate, e);
					throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				logger.info("Service: Generating RESUB report for version {}", version);

				try {
					// ✅ Redirecting to Resub Excel
					return BRRS_M_INT_RATESResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);

				} catch (ParseException e) {
					logger.error("Invalid report date format: {}", fromdate, e);
					throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			} else {

				if ("email".equalsIgnoreCase(format) && version == null) {
					logger.info("Got format as Email");
					logger.info("Service: Generating Email report for version {}", version);
					return BRRS_M_INT_RATESEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
				} else {

					// Fetch data

					List<M_INT_RATES_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

					if (dataList.isEmpty()) {
						logger.warn("Service: No data found for BRRS_M_INT_RATES report. Returning empty result.");
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
								M_INT_RATES_Summary_Entity record = dataList.get(i);
								System.out.println("rownumber=" + startRow + i);
								Row row = sheet.getRow(startRow + i);
								if (row == null) {
									row = sheet.createRow(startRow + i);
								}
								
								//row6
								// Column B
								Cell cellBdate = row.createCell(2);
								if (record.getReportDate() != null) {
									cellBdate.setCellValue(record.getReportDate());
									cellBdate.setCellStyle(dateStyle);
								} else {
									cellBdate.setCellValue("");
									cellBdate.setCellStyle(textStyle);
								}
								
								
								
								//ROW 11
								row = sheet.getRow(10);

		Cell cell1 = row.createCell(1);
						if (record.getR11_NOMINAL_INTEREST_RATE() != null) {
							cell1.setCellValue(record.getR11_NOMINAL_INTEREST_RATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						
						Cell cell2 = row.createCell(2);
						if (record.getR11_AVG_EFFECTIVE_RATE() != null) {
							cell2.setCellValue(record.getR11_AVG_EFFECTIVE_RATE());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}
						
						Cell cell3 = row.createCell(3);
						if (record.getR11_VOLUME() != null) {
							cell3.setCellValue(record.getR11_VOLUME().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}
						
						
						row = sheet.getRow(11);
						if (row == null) {
						    row = sheet.createRow(11);
						}

						cell1 = row.createCell(1);
						if (record.getR12_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR12_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR12_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR12_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR12_VOLUME() != null) {
						    cell3.setCellValue(record.getR12_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(12);
						if (row == null) {
						    row = sheet.createRow(12);
						}

						cell1 = row.createCell(1);
						if (record.getR13_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR13_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR13_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR13_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR13_VOLUME() != null) {
						    cell3.setCellValue(record.getR13_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(13);
						if (row == null) {
						    row = sheet.createRow(13);
						}

						cell1 = row.createCell(1);
						if (record.getR14_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR14_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR14_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR14_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR14_VOLUME() != null) {
						    cell3.setCellValue(record.getR14_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(14);
						if (row == null) {
						    row = sheet.createRow(14);
						}

						cell1 = row.createCell(1);
						if (record.getR15_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR15_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR15_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR15_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR15_VOLUME() != null) {
						    cell3.setCellValue(record.getR15_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(15);
						if (row == null) {
						    row = sheet.createRow(15);
						}

						cell1 = row.createCell(1);
						if (record.getR16_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR16_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR16_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR16_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR16_VOLUME() != null) {
						    cell3.setCellValue(record.getR16_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(16);
						if (row == null) {
						    row = sheet.createRow(16);
						}

						cell1 = row.createCell(1);
						if (record.getR17_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR17_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR17_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR17_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR17_VOLUME() != null) {
						    cell3.setCellValue(record.getR17_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(17);
						if (row == null) {
						    row = sheet.createRow(17);
						}

						cell1 = row.createCell(1);
						if (record.getR18_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR18_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR18_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR18_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR18_VOLUME() != null) {
						    cell3.setCellValue(record.getR18_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(18);
						if (row == null) {
						    row = sheet.createRow(18);
						}

						cell1 = row.createCell(1);
						if (record.getR19_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR19_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR19_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR19_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR19_VOLUME() != null) {
						    cell3.setCellValue(record.getR19_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(19);
						if (row == null) {
						    row = sheet.createRow(19);
						}

						cell1 = row.createCell(1);
						if (record.getR20_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR20_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR20_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR20_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR20_VOLUME() != null) {
						    cell3.setCellValue(record.getR20_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(20);
						if (row == null) {
						    row = sheet.createRow(20);
						}

						cell1 = row.createCell(1);
						if (record.getR21_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR21_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR21_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR21_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR21_VOLUME() != null) {
						    cell3.setCellValue(record.getR21_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(21);
						if (row == null) {
						    row = sheet.createRow(21);
						}

						cell1 = row.createCell(1);
						if (record.getR22_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR22_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR22_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR22_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR22_VOLUME() != null) {
						    cell3.setCellValue(record.getR22_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(22);
						if (row == null) {
						    row = sheet.createRow(22);
						}

						cell1 = row.createCell(1);
						if (record.getR23_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR23_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR23_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR23_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR23_VOLUME() != null) {
						    cell3.setCellValue(record.getR23_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}


						row = sheet.getRow(24);
						if (row == null) {
						    row = sheet.createRow(24);
						}

						cell1 = row.createCell(1);
						if (record.getR25_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR25_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR25_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR25_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR25_VOLUME() != null) {
						    cell3.setCellValue(record.getR25_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(25);
						if (row == null) {
						    row = sheet.createRow(25);
						}

						cell1 = row.createCell(1);
						if (record.getR26_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR26_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR26_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR26_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR26_VOLUME() != null) {
						    cell3.setCellValue(record.getR26_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(26);
						if (row == null) {
						    row = sheet.createRow(26);
						}

						cell1 = row.createCell(1);
						if (record.getR27_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR27_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR27_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR27_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR27_VOLUME() != null) {
						    cell3.setCellValue(record.getR27_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(27);
						if (row == null) {
						    row = sheet.createRow(27);
						}

						cell1 = row.createCell(1);
						if (record.getR28_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR28_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR28_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR28_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR28_VOLUME() != null) {
						    cell3.setCellValue(record.getR28_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(28);
						if (row == null) {
						    row = sheet.createRow(28);
						}

						cell1 = row.createCell(1);
						if (record.getR29_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR29_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR29_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR29_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR29_VOLUME() != null) {
						    cell3.setCellValue(record.getR29_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(29);
						if (row == null) {
						    row = sheet.createRow(29);
						}

						cell1 = row.createCell(1);
						if (record.getR30_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR30_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR30_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR30_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR30_VOLUME() != null) {
						    cell3.setCellValue(record.getR30_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(30);
						if (row == null) {
						    row = sheet.createRow(30);
						}

						cell1 = row.createCell(1);
						if (record.getR31_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR31_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR31_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR31_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR31_VOLUME() != null) {
						    cell3.setCellValue(record.getR31_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(31);
						if (row == null) {
						    row = sheet.createRow(31);
						}

						cell1 = row.createCell(1);
						if (record.getR32_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR32_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR32_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR32_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR32_VOLUME() != null) {
						    cell3.setCellValue(record.getR32_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(32);
						if (row == null) {
						    row = sheet.createRow(32);
						}

						cell1 = row.createCell(1);
						if (record.getR33_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR33_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR33_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR33_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR33_VOLUME() != null) {
						    cell3.setCellValue(record.getR33_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(33);
						if (row == null) {
						    row = sheet.createRow(33);
						}

						cell1 = row.createCell(1);
						if (record.getR34_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR34_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR34_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR34_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR34_VOLUME() != null) {
						    cell3.setCellValue(record.getR34_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(34);
						if (row == null) {
						    row = sheet.createRow(34);
						}

						cell1 = row.createCell(1);
						if (record.getR35_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR35_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR35_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR35_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR35_VOLUME() != null) {
						    cell3.setCellValue(record.getR35_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(35);
						if (row == null) {
						    row = sheet.createRow(35);
						}

						cell1 = row.createCell(1);
						if (record.getR36_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR36_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR36_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR36_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR36_VOLUME() != null) {
						    cell3.setCellValue(record.getR36_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(36);
						if (row == null) {
						    row = sheet.createRow(36);
						}

						cell1 = row.createCell(1);
						if (record.getR37_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR37_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR37_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR37_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR37_VOLUME() != null) {
						    cell3.setCellValue(record.getR37_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(37);
						if (row == null) {
						    row = sheet.createRow(37);
						}

						cell1 = row.createCell(1);
						if (record.getR38_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR38_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR38_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR38_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR38_VOLUME() != null) {
						    cell3.setCellValue(record.getR38_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(38);
						if (row == null) {
						    row = sheet.createRow(38);
						}

						cell1 = row.createCell(1);
						if (record.getR39_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR39_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR39_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR39_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR39_VOLUME() != null) {
						    cell3.setCellValue(record.getR39_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(39);
						if (row == null) {
						    row = sheet.createRow(39);
						}

						cell1 = row.createCell(1);
						if (record.getR40_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR40_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR40_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR40_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR40_VOLUME() != null) {
						    cell3.setCellValue(record.getR40_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(40);
						if (row == null) {
						    row = sheet.createRow(40);
						}

						cell1 = row.createCell(1);
						if (record.getR41_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR41_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR41_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR41_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR41_VOLUME() != null) {
						    cell3.setCellValue(record.getR41_VOLUME().doubleValue());
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
						
						
						// audit service summary format

						ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
													if (attrs != null) {
														HttpServletRequest request = attrs.getRequest();
														String userid = (String) request.getSession().getAttribute("USERID");
														auditService.createBusinessAudit(userid, "DOWNLOAD", "M_INT_RATES SUMMARY", null, "BRRS_M_INT_RATES_SUMMARYTABLE");
													}

						return out.toByteArray();
					}	
				}
			}
		}

		// Normal Email Excel
		public byte[] BRRS_M_INT_RATESEmailExcel(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Email Excel generation process in memory.");
			
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				try {
					// Redirecting to Archival
					return BRRS_M_INT_RATESARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
				} catch (ParseException e) {
					logger.error("Invalid report date format: {}", fromdate, e);
					throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				logger.info("Service: Generating RESUB report for version {}", version);

				try {
					// ✅ Redirecting to Resub Excel
					return BRRS_M_INT_RATESEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

				} catch (ParseException e) {
					logger.error("Invalid report date format: {}", fromdate, e);
					throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			} 
			else {
			List<M_INT_RATES_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_INT_RATES report. Returning empty result.");
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
						M_INT_RATES_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						
						//row6
						// Column B
						Cell cellBdate = row.createCell(2);
						if (record.getReportDate() != null) {
							cellBdate.setCellValue(record.getReportDate());
							cellBdate.setCellStyle(dateStyle);
						} else {
							cellBdate.setCellValue("");
							cellBdate.setCellStyle(textStyle);
						}
						
						
						
						//ROW 11
						row = sheet.getRow(10);


							Cell cell1 = row.createCell(1);
			if (record.getR11_NOMINAL_INTEREST_RATE() != null) {
				cell1.setCellValue(record.getR11_NOMINAL_INTEREST_RATE());
				cell1.setCellStyle(textStyle);
			} else {
				cell1.setCellValue("");
				cell1.setCellStyle(textStyle);
			}
			
			Cell cell2 = row.createCell(2);
			if (record.getR11_AVG_EFFECTIVE_RATE() != null) {
				cell2.setCellValue(record.getR11_AVG_EFFECTIVE_RATE());
				cell2.setCellStyle(textStyle);
			} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
			}
			
			
			
			row = sheet.getRow(11);
			if (row == null) {
			    row = sheet.createRow(11);
			}

			cell1 = row.createCell(1);
			if (record.getR12_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR12_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR12_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR12_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(12);
			if (row == null) {
			    row = sheet.createRow(12);
			}

			cell1 = row.createCell(1);
			if (record.getR13_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR13_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR13_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR13_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		


			row = sheet.getRow(13);
			if (row == null) {
			    row = sheet.createRow(13);
			}

			cell1 = row.createCell(1);
			if (record.getR14_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR14_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR14_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR14_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			

			row = sheet.getRow(14);
			if (row == null) {
			    row = sheet.createRow(14);
			}

			cell1 = row.createCell(1);
			if (record.getR15_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR15_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR15_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR15_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		


			row = sheet.getRow(15);
			if (row == null) {
			    row = sheet.createRow(15);
			}

			cell1 = row.createCell(1);
			if (record.getR16_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR16_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR16_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR16_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(16);
			if (row == null) {
			    row = sheet.createRow(16);
			}

			cell1 = row.createCell(1);
			if (record.getR17_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR17_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR17_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR17_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(17);
			if (row == null) {
			    row = sheet.createRow(17);
			}

			cell1 = row.createCell(1);
			if (record.getR18_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR18_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR18_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR18_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		



			row = sheet.getRow(18);
			if (row == null) {
			    row = sheet.createRow(18);
			}

			cell1 = row.createCell(1);
			if (record.getR19_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR19_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR19_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR19_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(19);
			if (row == null) {
			    row = sheet.createRow(19);
			}

			cell1 = row.createCell(1);
			if (record.getR20_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR20_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR20_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR20_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}



			row = sheet.getRow(20);
			if (row == null) {
			    row = sheet.createRow(20);
			}

			cell1 = row.createCell(1);
			if (record.getR21_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR21_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR21_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR21_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(21);
			if (row == null) {
			    row = sheet.createRow(21);
			}

			cell1 = row.createCell(1);
			if (record.getR22_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR22_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR22_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR22_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		


			row = sheet.getRow(22);
			if (row == null) {
			    row = sheet.createRow(22);
			}

			cell1 = row.createCell(1);
			if (record.getR23_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR23_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR23_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR23_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		

			row = sheet.getRow(23);
			if (row == null) {
			    row = sheet.createRow(23);
			}

			cell1 = row.createCell(1);
			if (record.getR31_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR31_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR31_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR31_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(24);
			if (row == null) {
			    row = sheet.createRow(24);
			}

			cell1 = row.createCell(1);
			if (record.getR32_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR32_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR32_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR32_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(25);
			if (row == null) {
			    row = sheet.createRow(25);
			}

			cell1 = row.createCell(1);
			if (record.getR33_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR33_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR33_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR33_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		



			row = sheet.getRow(26);
			if (row == null) {
			    row = sheet.createRow(33);
			}

			cell1 = row.createCell(1);
			if (record.getR34_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR34_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR34_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR34_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(27);
			if (row == null) {
			    row = sheet.createRow(27);
			}

			cell1 = row.createCell(1);
			if (record.getR35_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR35_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR35_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR35_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(28);
			if (row == null) {
			    row = sheet.createRow(28);
			}

			cell1 = row.createCell(1);
			if (record.getR36_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR36_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR36_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR36_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		



			row = sheet.getRow(29);
			if (row == null) {
			    row = sheet.createRow(29);
			}

			cell1 = row.createCell(1);
			if (record.getR37_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR37_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR37_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR37_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(30);
			if (row == null) {
			    row = sheet.createRow(30);
			}

			cell1 = row.createCell(1);
			if (record.getR38_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR38_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR38_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR38_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(31);
			if (row == null) {
			    row = sheet.createRow(31);
			}

			cell1 = row.createCell(1);
			if (record.getR39_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR39_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR39_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR39_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		



			row = sheet.getRow(32);
			if (row == null) {
			    row = sheet.createRow(32);
			}

			cell1 = row.createCell(1);
			if (record.getR40_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR40_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR40_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR40_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(33);
			if (row == null) {
			    row = sheet.createRow(33);
			}

			cell1 = row.createCell(1);
			if (record.getR41_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR41_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR41_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR41_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
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
										auditService.createBusinessAudit(userid, "DOWNLOAD", "M_INT_RATES EMAIL SUMMARY", null, "BRRS_M_INT_RATES_SUMMARYTABLE");
									}

				return out.toByteArray();
			}
			}
		}
		
		
		
		// Archival format excel
		public byte[] getExcelM_INTRATESARCHIVAL(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

			logger.info("Service: Starting Excel generation process in memory in Archival.");

			if ("email".equalsIgnoreCase(format) && version != null) {
				try {
					// Redirecting to Archival
					return BRRS_M_INT_RATESARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
				} catch (ParseException e) {
					logger.error("Invalid report date format: {}", fromdate, e);
					throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			} 

			List<M_INT_RATES_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for M_INT_RATES report. Returning empty result.");
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
						M_INT_RATES_Archival_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						
						//row6
						// Column B
						Cell cellBdate = row.createCell(2);
						if (record.getReport_date() != null) {
							cellBdate.setCellValue(record.getReport_date());
							cellBdate.setCellStyle(dateStyle);
						} else {
							cellBdate.setCellValue("");
							cellBdate.setCellStyle(textStyle);
						}
						
						
						
						//ROW 11
						row = sheet.getRow(10);

		Cell cell1 = row.createCell(1);
							if (record.getR11_NOMINAL_INTEREST_RATE() != null) {
								cell1.setCellValue(record.getR11_NOMINAL_INTEREST_RATE());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							
							Cell cell2 = row.createCell(2);
							if (record.getR11_AVG_EFFECTIVE_RATE() != null) {
								cell2.setCellValue(record.getR11_AVG_EFFECTIVE_RATE());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}
							
							Cell cell3 = row.createCell(3);
							if (record.getR11_VOLUME() != null) {
								cell3.setCellValue(record.getR11_VOLUME().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							
							
							row = sheet.getRow(11);
							if (row == null) {
							    row = sheet.createRow(11);
							}

							cell1 = row.createCell(1);
							if (record.getR12_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR12_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR12_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR12_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR12_VOLUME() != null) {
							    cell3.setCellValue(record.getR12_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(12);
							if (row == null) {
							    row = sheet.createRow(12);
							}

							cell1 = row.createCell(1);
							if (record.getR13_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR13_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR13_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR13_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR13_VOLUME() != null) {
							    cell3.setCellValue(record.getR13_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(13);
							if (row == null) {
							    row = sheet.createRow(13);
							}

							cell1 = row.createCell(1);
							if (record.getR14_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR14_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR14_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR14_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR14_VOLUME() != null) {
							    cell3.setCellValue(record.getR14_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(14);
							if (row == null) {
							    row = sheet.createRow(14);
							}

							cell1 = row.createCell(1);
							if (record.getR15_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR15_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR15_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR15_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR15_VOLUME() != null) {
							    cell3.setCellValue(record.getR15_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(15);
							if (row == null) {
							    row = sheet.createRow(15);
							}

							cell1 = row.createCell(1);
							if (record.getR16_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR16_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR16_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR16_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR16_VOLUME() != null) {
							    cell3.setCellValue(record.getR16_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(16);
							if (row == null) {
							    row = sheet.createRow(16);
							}

							cell1 = row.createCell(1);
							if (record.getR17_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR17_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR17_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR17_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR17_VOLUME() != null) {
							    cell3.setCellValue(record.getR17_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(17);
							if (row == null) {
							    row = sheet.createRow(17);
							}

							cell1 = row.createCell(1);
							if (record.getR18_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR18_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR18_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR18_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR18_VOLUME() != null) {
							    cell3.setCellValue(record.getR18_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(18);
							if (row == null) {
							    row = sheet.createRow(18);
							}

							cell1 = row.createCell(1);
							if (record.getR19_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR19_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR19_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR19_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR19_VOLUME() != null) {
							    cell3.setCellValue(record.getR19_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(19);
							if (row == null) {
							    row = sheet.createRow(19);
							}

							cell1 = row.createCell(1);
							if (record.getR20_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR20_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR20_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR20_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR20_VOLUME() != null) {
							    cell3.setCellValue(record.getR20_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(20);
							if (row == null) {
							    row = sheet.createRow(20);
							}

							cell1 = row.createCell(1);
							if (record.getR21_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR21_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR21_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR21_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR21_VOLUME() != null) {
							    cell3.setCellValue(record.getR21_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(21);
							if (row == null) {
							    row = sheet.createRow(21);
							}

							cell1 = row.createCell(1);
							if (record.getR22_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR22_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR22_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR22_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR22_VOLUME() != null) {
							    cell3.setCellValue(record.getR22_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(22);
							if (row == null) {
							    row = sheet.createRow(22);
							}

							cell1 = row.createCell(1);
							if (record.getR23_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR23_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR23_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR23_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR23_VOLUME() != null) {
							    cell3.setCellValue(record.getR23_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}
							
							
							/*
							 * // ----------------- R24 ----------------- row = sheet.getRow(23); // R24 =
							 * Excel row 24 (index 23) if (row == null) { row = sheet.createRow(23); }
							 * 
							 * cell1 = row.createCell(1); if (record.getR24_NOMINAL_INTEREST_RATE() != null)
							 * { cell1.setCellValue(record.getR24_NOMINAL_INTEREST_RATE()); // <-- use text
							 * if 200-200 cell1.setCellStyle(textStyle); } else { cell1.setCellValue("");
							 * cell1.setCellStyle(textStyle); }
							 * 
							 * cell2 = row.createCell(2); if (record.getR24_AVG_EFFECTIVE_RATE() != null) {
							 * cell2.setCellValue(record.getR24_AVG_EFFECTIVE_RATE());
							 * cell2.setCellStyle(textStyle); } else { cell2.setCellValue("");
							 * cell2.setCellStyle(textStyle); }
							 */

							row = sheet.getRow(24);
							if (row == null) {
							    row = sheet.createRow(24);
							}

							cell1 = row.createCell(1);
							if (record.getR25_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR25_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR25_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR25_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR25_VOLUME() != null) {
							    cell3.setCellValue(record.getR25_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(25);
							if (row == null) {
							    row = sheet.createRow(25);
							}

							cell1 = row.createCell(1);
							if (record.getR26_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR26_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR26_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR26_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR26_VOLUME() != null) {
							    cell3.setCellValue(record.getR26_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(26);
							if (row == null) {
							    row = sheet.createRow(26);
							}

							cell1 = row.createCell(1);
							if (record.getR27_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR27_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR27_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR27_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR27_VOLUME() != null) {
							    cell3.setCellValue(record.getR27_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(27);
							if (row == null) {
							    row = sheet.createRow(27);
							}

							cell1 = row.createCell(1);
							if (record.getR28_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR28_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR28_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR28_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR28_VOLUME() != null) {
							    cell3.setCellValue(record.getR28_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(28);
							if (row == null) {
							    row = sheet.createRow(28);
							}

							cell1 = row.createCell(1);
							if (record.getR29_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR29_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR29_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR29_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR29_VOLUME() != null) {
							    cell3.setCellValue(record.getR29_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(29);
							if (row == null) {
							    row = sheet.createRow(29);
							}

							cell1 = row.createCell(1);
							if (record.getR30_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR30_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR30_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR30_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR30_VOLUME() != null) {
							    cell3.setCellValue(record.getR30_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(30);
							if (row == null) {
							    row = sheet.createRow(30);
							}

							cell1 = row.createCell(1);
							if (record.getR31_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR31_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR31_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR31_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR31_VOLUME() != null) {
							    cell3.setCellValue(record.getR31_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(31);
							if (row == null) {
							    row = sheet.createRow(31);
							}

							cell1 = row.createCell(1);
							if (record.getR32_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR32_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR32_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR32_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR32_VOLUME() != null) {
							    cell3.setCellValue(record.getR32_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(32);
							if (row == null) {
							    row = sheet.createRow(32);
							}

							cell1 = row.createCell(1);
							if (record.getR33_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR33_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR33_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR33_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR33_VOLUME() != null) {
							    cell3.setCellValue(record.getR33_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(33);
							if (row == null) {
							    row = sheet.createRow(33);
							}

							cell1 = row.createCell(1);
							if (record.getR34_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR34_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR34_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR34_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR34_VOLUME() != null) {
							    cell3.setCellValue(record.getR34_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(34);
							if (row == null) {
							    row = sheet.createRow(34);
							}

							cell1 = row.createCell(1);
							if (record.getR35_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR35_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR35_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR35_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR35_VOLUME() != null) {
							    cell3.setCellValue(record.getR35_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(35);
							if (row == null) {
							    row = sheet.createRow(35);
							}

							cell1 = row.createCell(1);
							if (record.getR36_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR36_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR36_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR36_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR36_VOLUME() != null) {
							    cell3.setCellValue(record.getR36_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(36);
							if (row == null) {
							    row = sheet.createRow(36);
							}

							cell1 = row.createCell(1);
							if (record.getR37_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR37_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR37_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR37_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR37_VOLUME() != null) {
							    cell3.setCellValue(record.getR37_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(37);
							if (row == null) {
							    row = sheet.createRow(37);
							}

							cell1 = row.createCell(1);
							if (record.getR38_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR38_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR38_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR38_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR38_VOLUME() != null) {
							    cell3.setCellValue(record.getR38_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(38);
							if (row == null) {
							    row = sheet.createRow(38);
							}

							cell1 = row.createCell(1);
							if (record.getR39_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR39_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR39_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR39_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR39_VOLUME() != null) {
							    cell3.setCellValue(record.getR39_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(39);
							if (row == null) {
							    row = sheet.createRow(39);
							}

							cell1 = row.createCell(1);
							if (record.getR40_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR40_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR40_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR40_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR40_VOLUME() != null) {
							    cell3.setCellValue(record.getR40_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(40);
							if (row == null) {
							    row = sheet.createRow(40);
							}

							cell1 = row.createCell(1);
							if (record.getR41_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR41_NOMINAL_INTEREST_RATE());
							    cell1.setCellStyle(textStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR41_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR41_AVG_EFFECTIVE_RATE());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR41_VOLUME() != null) {
							    cell3.setCellValue(record.getR41_VOLUME().doubleValue());
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
				
				
				// audit service archival summary format

				ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
									if (attrs != null) {
										HttpServletRequest request = attrs.getRequest();
										String userid = (String) request.getSession().getAttribute("USERID");
										auditService.createBusinessAudit(userid, "DOWNLOAD", "M_INT_RATES ARCHIVAL SUMMARY", null, "BRRS_M_INT_RATES_ARCHIVALTABLE_SUMMARY");
									}

				return out.toByteArray();
			}

		}

		// Archival Email Excel
		public byte[] BRRS_M_INT_RATESARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Archival Email Excel generation process in memory.");

			List<M_INT_RATES_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_INT_RATES report. Returning empty result.");
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
						M_INT_RATES_Archival_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						
						//row6
						// Column B
						Cell cellBdate = row.createCell(2);
						if (record.getReport_date() != null) {
							cellBdate.setCellValue(record.getReport_date());
							cellBdate.setCellStyle(dateStyle);
						} else {
							cellBdate.setCellValue("");
							cellBdate.setCellStyle(textStyle);
						}
						
						
						
						//ROW 11
						row = sheet.getRow(10);

	Cell cell1 = row.createCell(1);
			if (record.getR11_NOMINAL_INTEREST_RATE() != null) {
				cell1.setCellValue(record.getR11_NOMINAL_INTEREST_RATE());
				cell1.setCellStyle(textStyle);
			} else {
				cell1.setCellValue("");
				cell1.setCellStyle(textStyle);
			}
			
			Cell cell2 = row.createCell(2);
			if (record.getR11_AVG_EFFECTIVE_RATE() != null) {
				cell2.setCellValue(record.getR11_AVG_EFFECTIVE_RATE());
				cell2.setCellStyle(textStyle);
			} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
			}
			
			
			
			row = sheet.getRow(11);
			if (row == null) {
			    row = sheet.createRow(11);
			}

			cell1 = row.createCell(1);
			if (record.getR12_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR12_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR12_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR12_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(12);
			if (row == null) {
			    row = sheet.createRow(12);
			}

			cell1 = row.createCell(1);
			if (record.getR13_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR13_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR13_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR13_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		


			row = sheet.getRow(13);
			if (row == null) {
			    row = sheet.createRow(13);
			}

			cell1 = row.createCell(1);
			if (record.getR14_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR14_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR14_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR14_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			

			row = sheet.getRow(14);
			if (row == null) {
			    row = sheet.createRow(14);
			}

			cell1 = row.createCell(1);
			if (record.getR15_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR15_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR15_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR15_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		


			row = sheet.getRow(15);
			if (row == null) {
			    row = sheet.createRow(15);
			}

			cell1 = row.createCell(1);
			if (record.getR16_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR16_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR16_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR16_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(16);
			if (row == null) {
			    row = sheet.createRow(16);
			}

			cell1 = row.createCell(1);
			if (record.getR17_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR17_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR17_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR17_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(17);
			if (row == null) {
			    row = sheet.createRow(17);
			}

			cell1 = row.createCell(1);
			if (record.getR18_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR18_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR18_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR18_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		



			row = sheet.getRow(18);
			if (row == null) {
			    row = sheet.createRow(18);
			}

			cell1 = row.createCell(1);
			if (record.getR19_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR19_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR19_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR19_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(19);
			if (row == null) {
			    row = sheet.createRow(19);
			}

			cell1 = row.createCell(1);
			if (record.getR20_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR20_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR20_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR20_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}



			row = sheet.getRow(20);
			if (row == null) {
			    row = sheet.createRow(20);
			}

			cell1 = row.createCell(1);
			if (record.getR21_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR21_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR21_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR21_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(21);
			if (row == null) {
			    row = sheet.createRow(21);
			}

			cell1 = row.createCell(1);
			if (record.getR22_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR22_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR22_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR22_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		


			row = sheet.getRow(22);
			if (row == null) {
			    row = sheet.createRow(22);
			}

			cell1 = row.createCell(1);
			if (record.getR23_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR23_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR23_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR23_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		

			row = sheet.getRow(23);
			if (row == null) {
			    row = sheet.createRow(23);
			}

			cell1 = row.createCell(1);
			if (record.getR31_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR31_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR31_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR31_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(24);
			if (row == null) {
			    row = sheet.createRow(24);
			}

			cell1 = row.createCell(1);
			if (record.getR32_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR32_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR32_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR32_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(25);
			if (row == null) {
			    row = sheet.createRow(25);
			}

			cell1 = row.createCell(1);
			if (record.getR33_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR33_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR33_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR33_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		



			row = sheet.getRow(26);
			if (row == null) {
			    row = sheet.createRow(33);
			}

			cell1 = row.createCell(1);
			if (record.getR34_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR34_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR34_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR34_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(27);
			if (row == null) {
			    row = sheet.createRow(27);
			}

			cell1 = row.createCell(1);
			if (record.getR35_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR35_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR35_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR35_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(28);
			if (row == null) {
			    row = sheet.createRow(28);
			}

			cell1 = row.createCell(1);
			if (record.getR36_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR36_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR36_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR36_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		



			row = sheet.getRow(29);
			if (row == null) {
			    row = sheet.createRow(29);
			}

			cell1 = row.createCell(1);
			if (record.getR37_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR37_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR37_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR37_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(30);
			if (row == null) {
			    row = sheet.createRow(30);
			}

			cell1 = row.createCell(1);
			if (record.getR38_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR38_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR38_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR38_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(31);
			if (row == null) {
			    row = sheet.createRow(31);
			}

			cell1 = row.createCell(1);
			if (record.getR39_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR39_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR39_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR39_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		



			row = sheet.getRow(32);
			if (row == null) {
			    row = sheet.createRow(32);
			}

			cell1 = row.createCell(1);
			if (record.getR40_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR40_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR40_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR40_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(33);
			if (row == null) {
			    row = sheet.createRow(33);
			}

			cell1 = row.createCell(1);
			if (record.getR41_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR41_NOMINAL_INTEREST_RATE());
			    cell1.setCellStyle(textStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR41_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR41_AVG_EFFECTIVE_RATE());
			    cell2.setCellStyle(textStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
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
									auditService.createBusinessAudit(userid, "DOWNLOAD", "M_INT_RATES EMAIL ARCHIVAL SUMMARY", null, "BRRS_M_INT_RATES_ARCHIVALTABLE_SUMMARY");
								}

				return out.toByteArray();
			}
		}

		// Resub Format excel
		public byte[] BRRS_M_INT_RATESResubExcel(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

			logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

			if ("email".equalsIgnoreCase(format) && version != null) {
				logger.info("Service: Generating RESUB report for version {}", version);

				try {
					// ✅ Redirecting to Resub Excel
					return BRRS_M_INT_RATESEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

				} catch (ParseException e) {
					logger.error("Invalid report date format: {}", fromdate, e);
					throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

			List<M_INT_RATES_RESUB_Summary_Entity> dataList = getResubSummaryByDateAndVersion(dateformat.parse(todate), version);

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for M_INT_RATES report. Returning empty result.");
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

						M_INT_RATES_RESUB_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						//row6
						// Column B
						Cell cellBdate = row.createCell(2);
						if (record.getReport_date() != null) {
							cellBdate.setCellValue(record.getReport_date());
							cellBdate.setCellStyle(dateStyle);
						} else {
							cellBdate.setCellValue("");
							cellBdate.setCellStyle(textStyle);
						}
						
						
						
						//ROW 11
						row = sheet.getRow(10);

						Cell cell1 = row.createCell(1);
						if (record.getR11_NOMINAL_INTEREST_RATE() != null) {
							cell1.setCellValue(record.getR11_NOMINAL_INTEREST_RATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						
						Cell cell2 = row.createCell(2);
						if (record.getR11_AVG_EFFECTIVE_RATE() != null) {
							cell2.setCellValue(record.getR11_AVG_EFFECTIVE_RATE());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}
						
						Cell cell3 = row.createCell(3);
						if (record.getR11_VOLUME() != null) {
							cell3.setCellValue(record.getR11_VOLUME().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}
						
						
						row = sheet.getRow(11);
						if (row == null) {
						    row = sheet.createRow(11);
						}

						cell1 = row.createCell(1);
						if (record.getR12_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR12_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR12_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR12_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR12_VOLUME() != null) {
						    cell3.setCellValue(record.getR12_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(12);
						if (row == null) {
						    row = sheet.createRow(12);
						}

						cell1 = row.createCell(1);
						if (record.getR13_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR13_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR13_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR13_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR13_VOLUME() != null) {
						    cell3.setCellValue(record.getR13_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(13);
						if (row == null) {
						    row = sheet.createRow(13);
						}

						cell1 = row.createCell(1);
						if (record.getR14_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR14_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR14_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR14_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR14_VOLUME() != null) {
						    cell3.setCellValue(record.getR14_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(14);
						if (row == null) {
						    row = sheet.createRow(14);
						}

						cell1 = row.createCell(1);
						if (record.getR15_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR15_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR15_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR15_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR15_VOLUME() != null) {
						    cell3.setCellValue(record.getR15_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(15);
						if (row == null) {
						    row = sheet.createRow(15);
						}

						cell1 = row.createCell(1);
						if (record.getR16_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR16_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR16_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR16_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR16_VOLUME() != null) {
						    cell3.setCellValue(record.getR16_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(16);
						if (row == null) {
						    row = sheet.createRow(16);
						}

						cell1 = row.createCell(1);
						if (record.getR17_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR17_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR17_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR17_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR17_VOLUME() != null) {
						    cell3.setCellValue(record.getR17_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(17);
						if (row == null) {
						    row = sheet.createRow(17);
						}

						cell1 = row.createCell(1);
						if (record.getR18_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR18_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR18_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR18_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR18_VOLUME() != null) {
						    cell3.setCellValue(record.getR18_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(18);
						if (row == null) {
						    row = sheet.createRow(18);
						}

						cell1 = row.createCell(1);
						if (record.getR19_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR19_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR19_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR19_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR19_VOLUME() != null) {
						    cell3.setCellValue(record.getR19_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(19);
						if (row == null) {
						    row = sheet.createRow(19);
						}

						cell1 = row.createCell(1);
						if (record.getR20_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR20_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR20_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR20_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR20_VOLUME() != null) {
						    cell3.setCellValue(record.getR20_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(20);
						if (row == null) {
						    row = sheet.createRow(20);
						}

						cell1 = row.createCell(1);
						if (record.getR21_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR21_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR21_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR21_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR21_VOLUME() != null) {
						    cell3.setCellValue(record.getR21_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(21);
						if (row == null) {
						    row = sheet.createRow(21);
						}

						cell1 = row.createCell(1);
						if (record.getR22_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR22_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR22_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR22_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR22_VOLUME() != null) {
						    cell3.setCellValue(record.getR22_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(22);
						if (row == null) {
						    row = sheet.createRow(22);
						}

						cell1 = row.createCell(1);
						if (record.getR23_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR23_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR23_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR23_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR23_VOLUME() != null) {
						    cell3.setCellValue(record.getR23_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}
						
						
						/*
						 * // ----------------- R24 ----------------- row = sheet.getRow(23); // R24 =
						 * Excel row 24 (index 23) if (row == null) { row = sheet.createRow(23); }
						 * 
						 * cell1 = row.createCell(1); if (record.getR24_NOMINAL_INTEREST_RATE() != null)
						 * { cell1.setCellValue(record.getR24_NOMINAL_INTEREST_RATE()); // <-- use text
						 * if 200-200 cell1.setCellStyle(textStyle); } else { cell1.setCellValue("");
						 * cell1.setCellStyle(textStyle); }
						 * 
						 * cell2 = row.createCell(2); if (record.getR24_AVG_EFFECTIVE_RATE() != null) {
						 * cell2.setCellValue(record.getR24_AVG_EFFECTIVE_RATE());
						 * cell2.setCellStyle(textStyle); } else { cell2.setCellValue("");
						 * cell2.setCellStyle(textStyle); }
						 */

						row = sheet.getRow(24);
						if (row == null) {
						    row = sheet.createRow(24);
						}

						cell1 = row.createCell(1);
						if (record.getR25_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR25_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR25_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR25_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR25_VOLUME() != null) {
						    cell3.setCellValue(record.getR25_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(25);
						if (row == null) {
						    row = sheet.createRow(25);
						}

						cell1 = row.createCell(1);
						if (record.getR26_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR26_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR26_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR26_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR26_VOLUME() != null) {
						    cell3.setCellValue(record.getR26_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(26);
						if (row == null) {
						    row = sheet.createRow(26);
						}

						cell1 = row.createCell(1);
						if (record.getR27_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR27_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR27_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR27_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR27_VOLUME() != null) {
						    cell3.setCellValue(record.getR27_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(27);
						if (row == null) {
						    row = sheet.createRow(27);
						}

						cell1 = row.createCell(1);
						if (record.getR28_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR28_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR28_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR28_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR28_VOLUME() != null) {
						    cell3.setCellValue(record.getR28_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(28);
						if (row == null) {
						    row = sheet.createRow(28);
						}

						cell1 = row.createCell(1);
						if (record.getR29_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR29_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR29_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR29_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR29_VOLUME() != null) {
						    cell3.setCellValue(record.getR29_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(29);
						if (row == null) {
						    row = sheet.createRow(29);
						}

						cell1 = row.createCell(1);
						if (record.getR30_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR30_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR30_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR30_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR30_VOLUME() != null) {
						    cell3.setCellValue(record.getR30_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(30);
						if (row == null) {
						    row = sheet.createRow(30);
						}

						cell1 = row.createCell(1);
						if (record.getR31_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR31_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR31_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR31_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR31_VOLUME() != null) {
						    cell3.setCellValue(record.getR31_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(31);
						if (row == null) {
						    row = sheet.createRow(31);
						}

						cell1 = row.createCell(1);
						if (record.getR32_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR32_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR32_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR32_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR32_VOLUME() != null) {
						    cell3.setCellValue(record.getR32_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(32);
						if (row == null) {
						    row = sheet.createRow(32);
						}

						cell1 = row.createCell(1);
						if (record.getR33_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR33_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR33_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR33_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR33_VOLUME() != null) {
						    cell3.setCellValue(record.getR33_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(33);
						if (row == null) {
						    row = sheet.createRow(33);
						}

						cell1 = row.createCell(1);
						if (record.getR34_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR34_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR34_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR34_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR34_VOLUME() != null) {
						    cell3.setCellValue(record.getR34_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(34);
						if (row == null) {
						    row = sheet.createRow(34);
						}

						cell1 = row.createCell(1);
						if (record.getR35_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR35_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR35_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR35_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR35_VOLUME() != null) {
						    cell3.setCellValue(record.getR35_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(35);
						if (row == null) {
						    row = sheet.createRow(35);
						}

						cell1 = row.createCell(1);
						if (record.getR36_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR36_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR36_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR36_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR36_VOLUME() != null) {
						    cell3.setCellValue(record.getR36_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(36);
						if (row == null) {
						    row = sheet.createRow(36);
						}

						cell1 = row.createCell(1);
						if (record.getR37_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR37_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR37_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR37_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR37_VOLUME() != null) {
						    cell3.setCellValue(record.getR37_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(37);
						if (row == null) {
						    row = sheet.createRow(37);
						}

						cell1 = row.createCell(1);
						if (record.getR38_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR38_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR38_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR38_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR38_VOLUME() != null) {
						    cell3.setCellValue(record.getR38_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(38);
						if (row == null) {
						    row = sheet.createRow(38);
						}

						cell1 = row.createCell(1);
						if (record.getR39_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR39_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR39_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR39_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR39_VOLUME() != null) {
						    cell3.setCellValue(record.getR39_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(39);
						if (row == null) {
						    row = sheet.createRow(39);
						}

						cell1 = row.createCell(1);
						if (record.getR40_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR40_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR40_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR40_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR40_VOLUME() != null) {
						    cell3.setCellValue(record.getR40_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(40);
						if (row == null) {
						    row = sheet.createRow(40);
						}

						cell1 = row.createCell(1);
						if (record.getR41_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR41_NOMINAL_INTEREST_RATE());
						    cell1.setCellStyle(textStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR41_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR41_AVG_EFFECTIVE_RATE());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR41_VOLUME() != null) {
						    cell3.setCellValue(record.getR41_VOLUME().doubleValue());
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
				
				// audit service summary resub format


				ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
									if (attrs != null) {
										HttpServletRequest request = attrs.getRequest();
										String userid = (String) request.getSession().getAttribute("USERID");
										auditService.createBusinessAudit(userid, "DOWNLOAD", "M_INT_RATES RESUB SUMMARY", null, "BRRS_M_INT_RATES_RESUB_SUMMARYTABLE");
									}

				return out.toByteArray();
			}

		}

		// Resub Email Excel
		public byte[] BRRS_M_INT_RATESEmailResubExcel(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Archival Email Excel generation process in memory.");

			List<M_INT_RATES_RESUB_Summary_Entity> dataList = getResubSummaryByDateAndVersion(dateformat.parse(todate), version);

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_INT_RATES report. Returning empty result.");
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
						M_INT_RATES_RESUB_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						
						//row6
						// Column B
						Cell cellBdate = row.createCell(2);
						if (record.getReport_date() != null) {
							cellBdate.setCellValue(record.getReport_date());
							cellBdate.setCellStyle(dateStyle);
						} else {
							cellBdate.setCellValue("");
							cellBdate.setCellStyle(textStyle);
						}
						
						
						
						//ROW 11
						row = sheet.getRow(10);

		Cell cell1 = row.createCell(1);
		if (record.getR11_NOMINAL_INTEREST_RATE() != null) {
			cell1.setCellValue(record.getR11_NOMINAL_INTEREST_RATE());
			cell1.setCellStyle(textStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}
		
		Cell cell2 = row.createCell(2);
		if (record.getR11_AVG_EFFECTIVE_RATE() != null) {
			cell2.setCellValue(record.getR11_AVG_EFFECTIVE_RATE());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}
		
		
		
		row = sheet.getRow(11);
		if (row == null) {
		    row = sheet.createRow(11);
		}

		cell1 = row.createCell(1);
		if (record.getR12_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR12_NOMINAL_INTEREST_RATE());
		    cell1.setCellStyle(textStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR12_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR12_AVG_EFFECTIVE_RATE());
		    cell2.setCellStyle(textStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		



		row = sheet.getRow(12);
		if (row == null) {
		    row = sheet.createRow(12);
		}

		cell1 = row.createCell(1);
		if (record.getR13_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR13_NOMINAL_INTEREST_RATE());
		    cell1.setCellStyle(textStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR13_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR13_AVG_EFFECTIVE_RATE());
		    cell2.setCellStyle(textStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}




		row = sheet.getRow(13);
		if (row == null) {
		    row = sheet.createRow(13);
		}

		cell1 = row.createCell(1);
		if (record.getR14_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR14_NOMINAL_INTEREST_RATE());
		    cell1.setCellStyle(textStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR14_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR14_AVG_EFFECTIVE_RATE());
		    cell2.setCellStyle(textStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		

		row = sheet.getRow(14);
		if (row == null) {
		    row = sheet.createRow(14);
		}

		cell1 = row.createCell(1);
		if (record.getR15_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR15_NOMINAL_INTEREST_RATE());
		    cell1.setCellStyle(textStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR15_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR15_AVG_EFFECTIVE_RATE());
		    cell2.setCellStyle(textStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}




		row = sheet.getRow(15);
		if (row == null) {
		    row = sheet.createRow(15);
		}

		cell1 = row.createCell(1);
		if (record.getR16_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR16_NOMINAL_INTEREST_RATE());
		    cell1.setCellStyle(textStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR16_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR16_AVG_EFFECTIVE_RATE());
		    cell2.setCellStyle(textStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		


		row = sheet.getRow(16);
		if (row == null) {
		    row = sheet.createRow(16);
		}

		cell1 = row.createCell(1);
		if (record.getR17_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR17_NOMINAL_INTEREST_RATE());
		    cell1.setCellStyle(textStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR17_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR17_AVG_EFFECTIVE_RATE());
		    cell2.setCellStyle(textStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		



		row = sheet.getRow(17);
		if (row == null) {
		    row = sheet.createRow(17);
		}

		cell1 = row.createCell(1);
		if (record.getR18_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR18_NOMINAL_INTEREST_RATE());
		    cell1.setCellStyle(textStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR18_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR18_AVG_EFFECTIVE_RATE());
		    cell2.setCellStyle(textStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}





		row = sheet.getRow(18);
		if (row == null) {
		    row = sheet.createRow(18);
		}

		cell1 = row.createCell(1);
		if (record.getR19_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR19_NOMINAL_INTEREST_RATE());
		    cell1.setCellStyle(textStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR19_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR19_AVG_EFFECTIVE_RATE());
		    cell2.setCellStyle(textStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		



		row = sheet.getRow(19);
		if (row == null) {
		    row = sheet.createRow(19);
		}

		cell1 = row.createCell(1);
		if (record.getR20_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR20_NOMINAL_INTEREST_RATE());
		    cell1.setCellStyle(textStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR20_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR20_AVG_EFFECTIVE_RATE());
		    cell2.setCellStyle(textStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}



		row = sheet.getRow(20);
		if (row == null) {
		    row = sheet.createRow(20);
		}

		cell1 = row.createCell(1);
		if (record.getR21_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR21_NOMINAL_INTEREST_RATE());
		    cell1.setCellStyle(textStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR21_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR21_AVG_EFFECTIVE_RATE());
		    cell2.setCellStyle(textStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		



		row = sheet.getRow(21);
		if (row == null) {
		    row = sheet.createRow(21);
		}

		cell1 = row.createCell(1);
		if (record.getR22_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR22_NOMINAL_INTEREST_RATE());
		    cell1.setCellStyle(textStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR22_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR22_AVG_EFFECTIVE_RATE());
		    cell2.setCellStyle(textStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}




		row = sheet.getRow(22);
		if (row == null) {
		    row = sheet.createRow(22);
		}

		cell1 = row.createCell(1);
		if (record.getR23_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR23_NOMINAL_INTEREST_RATE());
		    cell1.setCellStyle(textStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR23_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR23_AVG_EFFECTIVE_RATE());
		    cell2.setCellStyle(textStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}



		row = sheet.getRow(23);
		if (row == null) {
		    row = sheet.createRow(23);
		}

		cell1 = row.createCell(1);
		if (record.getR31_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR31_NOMINAL_INTEREST_RATE());
		    cell1.setCellStyle(textStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR31_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR31_AVG_EFFECTIVE_RATE());
		    cell2.setCellStyle(textStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		



		row = sheet.getRow(24);
		if (row == null) {
		    row = sheet.createRow(24);
		}

		cell1 = row.createCell(1);
		if (record.getR32_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR32_NOMINAL_INTEREST_RATE());
		    cell1.setCellStyle(textStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR32_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR32_AVG_EFFECTIVE_RATE());
		    cell2.setCellStyle(textStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		



		row = sheet.getRow(25);
		if (row == null) {
		    row = sheet.createRow(25);
		}

		cell1 = row.createCell(1);
		if (record.getR33_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR33_NOMINAL_INTEREST_RATE());
		    cell1.setCellStyle(textStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR33_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR33_AVG_EFFECTIVE_RATE());
		    cell2.setCellStyle(textStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}





		row = sheet.getRow(26);
		if (row == null) {
		    row = sheet.createRow(33);
		}

		cell1 = row.createCell(1);
		if (record.getR34_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR34_NOMINAL_INTEREST_RATE());
		    cell1.setCellStyle(textStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR34_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR34_AVG_EFFECTIVE_RATE());
		    cell2.setCellStyle(textStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		


		row = sheet.getRow(27);
		if (row == null) {
		    row = sheet.createRow(27);
		}

		cell1 = row.createCell(1);
		if (record.getR35_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR35_NOMINAL_INTEREST_RATE());
		    cell1.setCellStyle(textStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR35_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR35_AVG_EFFECTIVE_RATE());
		    cell2.setCellStyle(textStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		


		row = sheet.getRow(28);
		if (row == null) {
		    row = sheet.createRow(28);
		}

		cell1 = row.createCell(1);
		if (record.getR36_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR36_NOMINAL_INTEREST_RATE());
		    cell1.setCellStyle(textStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR36_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR36_AVG_EFFECTIVE_RATE());
		    cell2.setCellStyle(textStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

	row = sheet.getRow(29);
		if (row == null) {
		    row = sheet.createRow(29);
		}

		cell1 = row.createCell(1);
		if (record.getR37_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR37_NOMINAL_INTEREST_RATE());
		    cell1.setCellStyle(textStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR37_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR37_AVG_EFFECTIVE_RATE());
		    cell2.setCellStyle(textStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		


		row = sheet.getRow(30);
		if (row == null) {
		    row = sheet.createRow(30);
		}

		cell1 = row.createCell(1);
		if (record.getR38_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR38_NOMINAL_INTEREST_RATE());
		    cell1.setCellStyle(textStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR38_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR38_AVG_EFFECTIVE_RATE());
		    cell2.setCellStyle(textStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		



		row = sheet.getRow(31);
		if (row == null) {
		    row = sheet.createRow(31);
		}

		cell1 = row.createCell(1);
		if (record.getR39_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR39_NOMINAL_INTEREST_RATE());
		    cell1.setCellStyle(textStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR39_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR39_AVG_EFFECTIVE_RATE());
		    cell2.setCellStyle(textStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}





		row = sheet.getRow(32);
		if (row == null) {
		    row = sheet.createRow(32);
		}

		cell1 = row.createCell(1);
		if (record.getR40_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR40_NOMINAL_INTEREST_RATE());
		    cell1.setCellStyle(textStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR40_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR40_AVG_EFFECTIVE_RATE());
		    cell2.setCellStyle(textStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		


		row = sheet.getRow(33);
		if (row == null) {
		    row = sheet.createRow(33);
		}

		cell1 = row.createCell(1);
		if (record.getR41_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR41_NOMINAL_INTEREST_RATE());
		    cell1.setCellStyle(textStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR41_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR41_AVG_EFFECTIVE_RATE());
		    cell2.setCellStyle(textStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
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
									auditService.createBusinessAudit(userid, "DOWNLOAD", "M_INT_RATES EMAIL RESUB SUMMARY", null, "BRRS_M_INT_RATES_RESUB_SUMMARYTABLE");
								}

				return out.toByteArray();
			}
		}



class M_INT_RATESSummaryRowMapper implements RowMapper<M_INT_RATES_Summary_Entity> {
	@Override
	public M_INT_RATES_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
		M_INT_RATES_Summary_Entity obj = new M_INT_RATES_Summary_Entity();
		obj.setReportDate(rs.getDate("REPORT_DATE"));
		obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
		obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
		obj.setReport_code(rs.getString("REPORT_CODE"));
		obj.setReport_desc(rs.getString("REPORT_DESC"));
		obj.setEntity_flg(rs.getString("ENTITY_FLG"));
		obj.setModify_flg(rs.getString("MODIFY_FLG"));
		obj.setDel_flg(rs.getString("DEL_FLG"));
		obj.setR11_LENDING(rs.getString("R11_LENDING")); obj.setR11_NOMINAL_INTEREST_RATE(rs.getString("R11_NOMINAL_INTEREST_RATE"));
		obj.setR11_AVG_EFFECTIVE_RATE(rs.getString("R11_AVG_EFFECTIVE_RATE")); obj.setR11_VOLUME(rs.getBigDecimal("R11_VOLUME"));
		obj.setR12_LENDING(rs.getString("R12_LENDING")); obj.setR12_NOMINAL_INTEREST_RATE(rs.getString("R12_NOMINAL_INTEREST_RATE"));
		obj.setR12_AVG_EFFECTIVE_RATE(rs.getString("R12_AVG_EFFECTIVE_RATE")); obj.setR12_VOLUME(rs.getBigDecimal("R12_VOLUME"));
		obj.setR13_LENDING(rs.getString("R13_LENDING")); obj.setR13_NOMINAL_INTEREST_RATE(rs.getString("R13_NOMINAL_INTEREST_RATE"));
		obj.setR13_AVG_EFFECTIVE_RATE(rs.getString("R13_AVG_EFFECTIVE_RATE")); obj.setR13_VOLUME(rs.getBigDecimal("R13_VOLUME"));
		obj.setR14_LENDING(rs.getString("R14_LENDING")); obj.setR14_NOMINAL_INTEREST_RATE(rs.getString("R14_NOMINAL_INTEREST_RATE"));
		obj.setR14_AVG_EFFECTIVE_RATE(rs.getString("R14_AVG_EFFECTIVE_RATE")); obj.setR14_VOLUME(rs.getBigDecimal("R14_VOLUME"));
		obj.setR15_LENDING(rs.getString("R15_LENDING")); obj.setR15_NOMINAL_INTEREST_RATE(rs.getString("R15_NOMINAL_INTEREST_RATE"));
		obj.setR15_AVG_EFFECTIVE_RATE(rs.getString("R15_AVG_EFFECTIVE_RATE")); obj.setR15_VOLUME(rs.getBigDecimal("R15_VOLUME"));
		obj.setR16_LENDING(rs.getString("R16_LENDING")); obj.setR16_NOMINAL_INTEREST_RATE(rs.getString("R16_NOMINAL_INTEREST_RATE"));
		obj.setR16_AVG_EFFECTIVE_RATE(rs.getString("R16_AVG_EFFECTIVE_RATE")); obj.setR16_VOLUME(rs.getBigDecimal("R16_VOLUME"));
		obj.setR17_LENDING(rs.getString("R17_LENDING")); obj.setR17_NOMINAL_INTEREST_RATE(rs.getString("R17_NOMINAL_INTEREST_RATE"));
		obj.setR17_AVG_EFFECTIVE_RATE(rs.getString("R17_AVG_EFFECTIVE_RATE")); obj.setR17_VOLUME(rs.getBigDecimal("R17_VOLUME"));
		obj.setR18_LENDING(rs.getString("R18_LENDING")); obj.setR18_NOMINAL_INTEREST_RATE(rs.getString("R18_NOMINAL_INTEREST_RATE"));
		obj.setR18_AVG_EFFECTIVE_RATE(rs.getString("R18_AVG_EFFECTIVE_RATE")); obj.setR18_VOLUME(rs.getBigDecimal("R18_VOLUME"));
		obj.setR19_LENDING(rs.getString("R19_LENDING")); obj.setR19_NOMINAL_INTEREST_RATE(rs.getString("R19_NOMINAL_INTEREST_RATE"));
		obj.setR19_AVG_EFFECTIVE_RATE(rs.getString("R19_AVG_EFFECTIVE_RATE")); obj.setR19_VOLUME(rs.getBigDecimal("R19_VOLUME"));
		obj.setR20_LENDING(rs.getString("R20_LENDING")); obj.setR20_NOMINAL_INTEREST_RATE(rs.getString("R20_NOMINAL_INTEREST_RATE"));
		obj.setR20_AVG_EFFECTIVE_RATE(rs.getString("R20_AVG_EFFECTIVE_RATE")); obj.setR20_VOLUME(rs.getBigDecimal("R20_VOLUME"));
		obj.setR21_LENDING(rs.getString("R21_LENDING")); obj.setR21_NOMINAL_INTEREST_RATE(rs.getString("R21_NOMINAL_INTEREST_RATE"));
		obj.setR21_AVG_EFFECTIVE_RATE(rs.getString("R21_AVG_EFFECTIVE_RATE")); obj.setR21_VOLUME(rs.getBigDecimal("R21_VOLUME"));
		obj.setR22_LENDING(rs.getString("R22_LENDING")); obj.setR22_NOMINAL_INTEREST_RATE(rs.getString("R22_NOMINAL_INTEREST_RATE"));
		obj.setR22_AVG_EFFECTIVE_RATE(rs.getString("R22_AVG_EFFECTIVE_RATE")); obj.setR22_VOLUME(rs.getBigDecimal("R22_VOLUME"));
		obj.setR23_LENDING(rs.getString("R23_LENDING")); obj.setR23_NOMINAL_INTEREST_RATE(rs.getString("R23_NOMINAL_INTEREST_RATE"));
		obj.setR23_AVG_EFFECTIVE_RATE(rs.getString("R23_AVG_EFFECTIVE_RATE")); obj.setR23_VOLUME(rs.getBigDecimal("R23_VOLUME"));
		obj.setR24_LENDING(rs.getString("R24_LENDING")); obj.setR24_NOMINAL_INTEREST_RATE(rs.getString("R24_NOMINAL_INTEREST_RATE"));
		obj.setR24_AVG_EFFECTIVE_RATE(rs.getString("R24_AVG_EFFECTIVE_RATE")); obj.setR24_VOLUME(rs.getBigDecimal("R24_VOLUME"));
		obj.setR25_LENDING(rs.getString("R25_LENDING")); obj.setR25_NOMINAL_INTEREST_RATE(rs.getString("R25_NOMINAL_INTEREST_RATE"));
		obj.setR25_AVG_EFFECTIVE_RATE(rs.getString("R25_AVG_EFFECTIVE_RATE")); obj.setR25_VOLUME(rs.getBigDecimal("R25_VOLUME"));
		obj.setR26_LENDING(rs.getString("R26_LENDING")); obj.setR26_NOMINAL_INTEREST_RATE(rs.getString("R26_NOMINAL_INTEREST_RATE"));
		obj.setR26_AVG_EFFECTIVE_RATE(rs.getString("R26_AVG_EFFECTIVE_RATE")); obj.setR26_VOLUME(rs.getBigDecimal("R26_VOLUME"));
		obj.setR27_LENDING(rs.getString("R27_LENDING")); obj.setR27_NOMINAL_INTEREST_RATE(rs.getString("R27_NOMINAL_INTEREST_RATE"));
		obj.setR27_AVG_EFFECTIVE_RATE(rs.getString("R27_AVG_EFFECTIVE_RATE")); obj.setR27_VOLUME(rs.getBigDecimal("R27_VOLUME"));
		obj.setR28_LENDING(rs.getString("R28_LENDING")); obj.setR28_NOMINAL_INTEREST_RATE(rs.getString("R28_NOMINAL_INTEREST_RATE"));
		obj.setR28_AVG_EFFECTIVE_RATE(rs.getString("R28_AVG_EFFECTIVE_RATE")); obj.setR28_VOLUME(rs.getBigDecimal("R28_VOLUME"));
		obj.setR29_LENDING(rs.getString("R29_LENDING")); obj.setR29_NOMINAL_INTEREST_RATE(rs.getString("R29_NOMINAL_INTEREST_RATE"));
		obj.setR29_AVG_EFFECTIVE_RATE(rs.getString("R29_AVG_EFFECTIVE_RATE")); obj.setR29_VOLUME(rs.getBigDecimal("R29_VOLUME"));
		obj.setR30_LENDING(rs.getString("R30_LENDING")); obj.setR30_NOMINAL_INTEREST_RATE(rs.getString("R30_NOMINAL_INTEREST_RATE"));
		obj.setR30_AVG_EFFECTIVE_RATE(rs.getString("R30_AVG_EFFECTIVE_RATE")); obj.setR30_VOLUME(rs.getBigDecimal("R30_VOLUME"));
		obj.setR31_LENDING(rs.getString("R31_LENDING")); obj.setR31_NOMINAL_INTEREST_RATE(rs.getString("R31_NOMINAL_INTEREST_RATE"));
		obj.setR31_AVG_EFFECTIVE_RATE(rs.getString("R31_AVG_EFFECTIVE_RATE")); obj.setR31_VOLUME(rs.getBigDecimal("R31_VOLUME"));
		obj.setR32_LENDING(rs.getString("R32_LENDING")); obj.setR32_NOMINAL_INTEREST_RATE(rs.getString("R32_NOMINAL_INTEREST_RATE"));
		obj.setR32_AVG_EFFECTIVE_RATE(rs.getString("R32_AVG_EFFECTIVE_RATE")); obj.setR32_VOLUME(rs.getBigDecimal("R32_VOLUME"));
		obj.setR33_LENDING(rs.getString("R33_LENDING")); obj.setR33_NOMINAL_INTEREST_RATE(rs.getString("R33_NOMINAL_INTEREST_RATE"));
		obj.setR33_AVG_EFFECTIVE_RATE(rs.getString("R33_AVG_EFFECTIVE_RATE")); obj.setR33_VOLUME(rs.getBigDecimal("R33_VOLUME"));
		obj.setR34_LENDING(rs.getString("R34_LENDING")); obj.setR34_NOMINAL_INTEREST_RATE(rs.getString("R34_NOMINAL_INTEREST_RATE"));
		obj.setR34_AVG_EFFECTIVE_RATE(rs.getString("R34_AVG_EFFECTIVE_RATE")); obj.setR34_VOLUME(rs.getBigDecimal("R34_VOLUME"));
		obj.setR35_LENDING(rs.getString("R35_LENDING")); obj.setR35_NOMINAL_INTEREST_RATE(rs.getString("R35_NOMINAL_INTEREST_RATE"));
		obj.setR35_AVG_EFFECTIVE_RATE(rs.getString("R35_AVG_EFFECTIVE_RATE")); obj.setR35_VOLUME(rs.getBigDecimal("R35_VOLUME"));
		obj.setR36_LENDING(rs.getString("R36_LENDING")); obj.setR36_NOMINAL_INTEREST_RATE(rs.getString("R36_NOMINAL_INTEREST_RATE"));
		obj.setR36_AVG_EFFECTIVE_RATE(rs.getString("R36_AVG_EFFECTIVE_RATE")); obj.setR36_VOLUME(rs.getBigDecimal("R36_VOLUME"));
		obj.setR37_LENDING(rs.getString("R37_LENDING")); obj.setR37_NOMINAL_INTEREST_RATE(rs.getString("R37_NOMINAL_INTEREST_RATE"));
		obj.setR37_AVG_EFFECTIVE_RATE(rs.getString("R37_AVG_EFFECTIVE_RATE")); obj.setR37_VOLUME(rs.getBigDecimal("R37_VOLUME"));
		obj.setR38_LENDING(rs.getString("R38_LENDING")); obj.setR38_NOMINAL_INTEREST_RATE(rs.getString("R38_NOMINAL_INTEREST_RATE"));
		obj.setR38_AVG_EFFECTIVE_RATE(rs.getString("R38_AVG_EFFECTIVE_RATE")); obj.setR38_VOLUME(rs.getBigDecimal("R38_VOLUME"));
		obj.setR39_LENDING(rs.getString("R39_LENDING")); obj.setR39_NOMINAL_INTEREST_RATE(rs.getString("R39_NOMINAL_INTEREST_RATE"));
		obj.setR39_AVG_EFFECTIVE_RATE(rs.getString("R39_AVG_EFFECTIVE_RATE")); obj.setR39_VOLUME(rs.getBigDecimal("R39_VOLUME"));
		obj.setR40_LENDING(rs.getString("R40_LENDING")); obj.setR40_NOMINAL_INTEREST_RATE(rs.getString("R40_NOMINAL_INTEREST_RATE"));
		obj.setR40_AVG_EFFECTIVE_RATE(rs.getString("R40_AVG_EFFECTIVE_RATE")); obj.setR40_VOLUME(rs.getBigDecimal("R40_VOLUME"));
		obj.setR41_LENDING(rs.getString("R41_LENDING")); obj.setR41_NOMINAL_INTEREST_RATE(rs.getString("R41_NOMINAL_INTEREST_RATE"));
		obj.setR41_AVG_EFFECTIVE_RATE(rs.getString("R41_AVG_EFFECTIVE_RATE")); obj.setR41_VOLUME(rs.getBigDecimal("R41_VOLUME"));
		obj.setR42_LENDING(rs.getString("R42_LENDING")); obj.setR42_NOMINAL_INTEREST_RATE(rs.getString("R42_NOMINAL_INTEREST_RATE"));
		obj.setR42_AVG_EFFECTIVE_RATE(rs.getString("R42_AVG_EFFECTIVE_RATE")); obj.setR42_VOLUME(rs.getBigDecimal("R42_VOLUME"));
		return obj;
	}
}

class M_INT_RATESDetailRowMapper implements RowMapper<M_INT_RATES_Detail_Entity> {
	@Override
	public M_INT_RATES_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
		M_INT_RATES_Detail_Entity obj = new M_INT_RATES_Detail_Entity();
		obj.setReport_date(rs.getDate("REPORT_DATE"));
		obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
		obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
		obj.setReport_code(rs.getString("REPORT_CODE"));
		obj.setReport_desc(rs.getString("REPORT_DESC"));
		obj.setEntity_flg(rs.getString("ENTITY_FLG"));
		obj.setModify_flg(rs.getString("MODIFY_FLG"));
		obj.setDel_flg(rs.getString("DEL_FLG"));
		obj.setR11_LENDING(rs.getString("R11_LENDING")); obj.setR11_NOMINAL_INTEREST_RATE(rs.getString("R11_NOMINAL_INTEREST_RATE"));
		obj.setR11_AVG_EFFECTIVE_RATE(rs.getString("R11_AVG_EFFECTIVE_RATE")); obj.setR11_VOLUME(rs.getBigDecimal("R11_VOLUME"));
		obj.setR12_LENDING(rs.getString("R12_LENDING")); obj.setR12_NOMINAL_INTEREST_RATE(rs.getString("R12_NOMINAL_INTEREST_RATE"));
		obj.setR12_AVG_EFFECTIVE_RATE(rs.getString("R12_AVG_EFFECTIVE_RATE")); obj.setR12_VOLUME(rs.getBigDecimal("R12_VOLUME"));
		obj.setR13_LENDING(rs.getString("R13_LENDING")); obj.setR13_NOMINAL_INTEREST_RATE(rs.getString("R13_NOMINAL_INTEREST_RATE"));
		obj.setR13_AVG_EFFECTIVE_RATE(rs.getString("R13_AVG_EFFECTIVE_RATE")); obj.setR13_VOLUME(rs.getBigDecimal("R13_VOLUME"));
		obj.setR14_LENDING(rs.getString("R14_LENDING")); obj.setR14_NOMINAL_INTEREST_RATE(rs.getString("R14_NOMINAL_INTEREST_RATE"));
		obj.setR14_AVG_EFFECTIVE_RATE(rs.getString("R14_AVG_EFFECTIVE_RATE")); obj.setR14_VOLUME(rs.getBigDecimal("R14_VOLUME"));
		obj.setR15_LENDING(rs.getString("R15_LENDING")); obj.setR15_NOMINAL_INTEREST_RATE(rs.getString("R15_NOMINAL_INTEREST_RATE"));
		obj.setR15_AVG_EFFECTIVE_RATE(rs.getString("R15_AVG_EFFECTIVE_RATE")); obj.setR15_VOLUME(rs.getBigDecimal("R15_VOLUME"));
		obj.setR16_LENDING(rs.getString("R16_LENDING")); obj.setR16_NOMINAL_INTEREST_RATE(rs.getString("R16_NOMINAL_INTEREST_RATE"));
		obj.setR16_AVG_EFFECTIVE_RATE(rs.getString("R16_AVG_EFFECTIVE_RATE")); obj.setR16_VOLUME(rs.getBigDecimal("R16_VOLUME"));
		obj.setR17_LENDING(rs.getString("R17_LENDING")); obj.setR17_NOMINAL_INTEREST_RATE(rs.getString("R17_NOMINAL_INTEREST_RATE"));
		obj.setR17_AVG_EFFECTIVE_RATE(rs.getString("R17_AVG_EFFECTIVE_RATE")); obj.setR17_VOLUME(rs.getBigDecimal("R17_VOLUME"));
		obj.setR18_LENDING(rs.getString("R18_LENDING")); obj.setR18_NOMINAL_INTEREST_RATE(rs.getString("R18_NOMINAL_INTEREST_RATE"));
		obj.setR18_AVG_EFFECTIVE_RATE(rs.getString("R18_AVG_EFFECTIVE_RATE")); obj.setR18_VOLUME(rs.getBigDecimal("R18_VOLUME"));
		obj.setR19_LENDING(rs.getString("R19_LENDING")); obj.setR19_NOMINAL_INTEREST_RATE(rs.getString("R19_NOMINAL_INTEREST_RATE"));
		obj.setR19_AVG_EFFECTIVE_RATE(rs.getString("R19_AVG_EFFECTIVE_RATE")); obj.setR19_VOLUME(rs.getBigDecimal("R19_VOLUME"));
		obj.setR20_LENDING(rs.getString("R20_LENDING")); obj.setR20_NOMINAL_INTEREST_RATE(rs.getString("R20_NOMINAL_INTEREST_RATE"));
		obj.setR20_AVG_EFFECTIVE_RATE(rs.getString("R20_AVG_EFFECTIVE_RATE")); obj.setR20_VOLUME(rs.getBigDecimal("R20_VOLUME"));
		obj.setR21_LENDING(rs.getString("R21_LENDING")); obj.setR21_NOMINAL_INTEREST_RATE(rs.getString("R21_NOMINAL_INTEREST_RATE"));
		obj.setR21_AVG_EFFECTIVE_RATE(rs.getString("R21_AVG_EFFECTIVE_RATE")); obj.setR21_VOLUME(rs.getBigDecimal("R21_VOLUME"));
		obj.setR22_LENDING(rs.getString("R22_LENDING")); obj.setR22_NOMINAL_INTEREST_RATE(rs.getString("R22_NOMINAL_INTEREST_RATE"));
		obj.setR22_AVG_EFFECTIVE_RATE(rs.getString("R22_AVG_EFFECTIVE_RATE")); obj.setR22_VOLUME(rs.getBigDecimal("R22_VOLUME"));
		obj.setR23_LENDING(rs.getString("R23_LENDING")); obj.setR23_NOMINAL_INTEREST_RATE(rs.getString("R23_NOMINAL_INTEREST_RATE"));
		obj.setR23_AVG_EFFECTIVE_RATE(rs.getString("R23_AVG_EFFECTIVE_RATE")); obj.setR23_VOLUME(rs.getBigDecimal("R23_VOLUME"));
		obj.setR24_LENDING(rs.getString("R24_LENDING")); obj.setR24_NOMINAL_INTEREST_RATE(rs.getString("R24_NOMINAL_INTEREST_RATE"));
		obj.setR24_AVG_EFFECTIVE_RATE(rs.getString("R24_AVG_EFFECTIVE_RATE")); obj.setR24_VOLUME(rs.getBigDecimal("R24_VOLUME"));
		obj.setR25_LENDING(rs.getString("R25_LENDING")); obj.setR25_NOMINAL_INTEREST_RATE(rs.getString("R25_NOMINAL_INTEREST_RATE"));
		obj.setR25_AVG_EFFECTIVE_RATE(rs.getString("R25_AVG_EFFECTIVE_RATE")); obj.setR25_VOLUME(rs.getBigDecimal("R25_VOLUME"));
		obj.setR26_LENDING(rs.getString("R26_LENDING")); obj.setR26_NOMINAL_INTEREST_RATE(rs.getString("R26_NOMINAL_INTEREST_RATE"));
		obj.setR26_AVG_EFFECTIVE_RATE(rs.getString("R26_AVG_EFFECTIVE_RATE")); obj.setR26_VOLUME(rs.getBigDecimal("R26_VOLUME"));
		obj.setR27_LENDING(rs.getString("R27_LENDING")); obj.setR27_NOMINAL_INTEREST_RATE(rs.getString("R27_NOMINAL_INTEREST_RATE"));
		obj.setR27_AVG_EFFECTIVE_RATE(rs.getString("R27_AVG_EFFECTIVE_RATE")); obj.setR27_VOLUME(rs.getBigDecimal("R27_VOLUME"));
		obj.setR28_LENDING(rs.getString("R28_LENDING")); obj.setR28_NOMINAL_INTEREST_RATE(rs.getString("R28_NOMINAL_INTEREST_RATE"));
		obj.setR28_AVG_EFFECTIVE_RATE(rs.getString("R28_AVG_EFFECTIVE_RATE")); obj.setR28_VOLUME(rs.getBigDecimal("R28_VOLUME"));
		obj.setR29_LENDING(rs.getString("R29_LENDING")); obj.setR29_NOMINAL_INTEREST_RATE(rs.getString("R29_NOMINAL_INTEREST_RATE"));
		obj.setR29_AVG_EFFECTIVE_RATE(rs.getString("R29_AVG_EFFECTIVE_RATE")); obj.setR29_VOLUME(rs.getBigDecimal("R29_VOLUME"));
		obj.setR30_LENDING(rs.getString("R30_LENDING")); obj.setR30_NOMINAL_INTEREST_RATE(rs.getString("R30_NOMINAL_INTEREST_RATE"));
		obj.setR30_AVG_EFFECTIVE_RATE(rs.getString("R30_AVG_EFFECTIVE_RATE")); obj.setR30_VOLUME(rs.getBigDecimal("R30_VOLUME"));
		obj.setR31_LENDING(rs.getString("R31_LENDING")); obj.setR31_NOMINAL_INTEREST_RATE(rs.getString("R31_NOMINAL_INTEREST_RATE"));
		obj.setR31_AVG_EFFECTIVE_RATE(rs.getString("R31_AVG_EFFECTIVE_RATE")); obj.setR31_VOLUME(rs.getBigDecimal("R31_VOLUME"));
		obj.setR32_LENDING(rs.getString("R32_LENDING")); obj.setR32_NOMINAL_INTEREST_RATE(rs.getString("R32_NOMINAL_INTEREST_RATE"));
		obj.setR32_AVG_EFFECTIVE_RATE(rs.getString("R32_AVG_EFFECTIVE_RATE")); obj.setR32_VOLUME(rs.getBigDecimal("R32_VOLUME"));
		obj.setR33_LENDING(rs.getString("R33_LENDING")); obj.setR33_NOMINAL_INTEREST_RATE(rs.getString("R33_NOMINAL_INTEREST_RATE"));
		obj.setR33_AVG_EFFECTIVE_RATE(rs.getString("R33_AVG_EFFECTIVE_RATE")); obj.setR33_VOLUME(rs.getBigDecimal("R33_VOLUME"));
		obj.setR34_LENDING(rs.getString("R34_LENDING")); obj.setR34_NOMINAL_INTEREST_RATE(rs.getString("R34_NOMINAL_INTEREST_RATE"));
		obj.setR34_AVG_EFFECTIVE_RATE(rs.getString("R34_AVG_EFFECTIVE_RATE")); obj.setR34_VOLUME(rs.getBigDecimal("R34_VOLUME"));
		obj.setR35_LENDING(rs.getString("R35_LENDING")); obj.setR35_NOMINAL_INTEREST_RATE(rs.getString("R35_NOMINAL_INTEREST_RATE"));
		obj.setR35_AVG_EFFECTIVE_RATE(rs.getString("R35_AVG_EFFECTIVE_RATE")); obj.setR35_VOLUME(rs.getBigDecimal("R35_VOLUME"));
		obj.setR36_LENDING(rs.getString("R36_LENDING")); obj.setR36_NOMINAL_INTEREST_RATE(rs.getString("R36_NOMINAL_INTEREST_RATE"));
		obj.setR36_AVG_EFFECTIVE_RATE(rs.getString("R36_AVG_EFFECTIVE_RATE")); obj.setR36_VOLUME(rs.getBigDecimal("R36_VOLUME"));
		obj.setR37_LENDING(rs.getString("R37_LENDING")); obj.setR37_NOMINAL_INTEREST_RATE(rs.getString("R37_NOMINAL_INTEREST_RATE"));
		obj.setR37_AVG_EFFECTIVE_RATE(rs.getString("R37_AVG_EFFECTIVE_RATE")); obj.setR37_VOLUME(rs.getBigDecimal("R37_VOLUME"));
		obj.setR38_LENDING(rs.getString("R38_LENDING")); obj.setR38_NOMINAL_INTEREST_RATE(rs.getString("R38_NOMINAL_INTEREST_RATE"));
		obj.setR38_AVG_EFFECTIVE_RATE(rs.getString("R38_AVG_EFFECTIVE_RATE")); obj.setR38_VOLUME(rs.getBigDecimal("R38_VOLUME"));
		obj.setR39_LENDING(rs.getString("R39_LENDING")); obj.setR39_NOMINAL_INTEREST_RATE(rs.getString("R39_NOMINAL_INTEREST_RATE"));
		obj.setR39_AVG_EFFECTIVE_RATE(rs.getString("R39_AVG_EFFECTIVE_RATE")); obj.setR39_VOLUME(rs.getBigDecimal("R39_VOLUME"));
		obj.setR40_LENDING(rs.getString("R40_LENDING")); obj.setR40_NOMINAL_INTEREST_RATE(rs.getString("R40_NOMINAL_INTEREST_RATE"));
		obj.setR40_AVG_EFFECTIVE_RATE(rs.getString("R40_AVG_EFFECTIVE_RATE")); obj.setR40_VOLUME(rs.getBigDecimal("R40_VOLUME"));
		obj.setR41_LENDING(rs.getString("R41_LENDING")); obj.setR41_NOMINAL_INTEREST_RATE(rs.getString("R41_NOMINAL_INTEREST_RATE"));
		obj.setR41_AVG_EFFECTIVE_RATE(rs.getString("R41_AVG_EFFECTIVE_RATE")); obj.setR41_VOLUME(rs.getBigDecimal("R41_VOLUME"));
		obj.setR42_LENDING(rs.getString("R42_LENDING")); obj.setR42_NOMINAL_INTEREST_RATE(rs.getString("R42_NOMINAL_INTEREST_RATE"));
		obj.setR42_AVG_EFFECTIVE_RATE(rs.getString("R42_AVG_EFFECTIVE_RATE")); obj.setR42_VOLUME(rs.getBigDecimal("R42_VOLUME"));
		return obj;
	}
}

class M_INT_RATESArchivalSummaryRowMapper implements RowMapper<M_INT_RATES_Archival_Summary_Entity> {
	@Override
	public M_INT_RATES_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
		M_INT_RATES_Archival_Summary_Entity obj = new M_INT_RATES_Archival_Summary_Entity();
		obj.setReport_date(rs.getDate("REPORT_DATE"));
		obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
		obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
		obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
		obj.setReport_code(rs.getString("REPORT_CODE"));
		obj.setReport_desc(rs.getString("REPORT_DESC"));
		obj.setEntity_flg(rs.getString("ENTITY_FLG"));
		obj.setModify_flg(rs.getString("MODIFY_FLG"));
		obj.setDel_flg(rs.getString("DEL_FLG"));
		obj.setR11_LENDING(rs.getString("R11_LENDING")); obj.setR11_NOMINAL_INTEREST_RATE(rs.getString("R11_NOMINAL_INTEREST_RATE"));
		obj.setR11_AVG_EFFECTIVE_RATE(rs.getString("R11_AVG_EFFECTIVE_RATE")); obj.setR11_VOLUME(rs.getBigDecimal("R11_VOLUME"));
		obj.setR12_LENDING(rs.getString("R12_LENDING")); obj.setR12_NOMINAL_INTEREST_RATE(rs.getString("R12_NOMINAL_INTEREST_RATE"));
		obj.setR12_AVG_EFFECTIVE_RATE(rs.getString("R12_AVG_EFFECTIVE_RATE")); obj.setR12_VOLUME(rs.getBigDecimal("R12_VOLUME"));
		obj.setR13_LENDING(rs.getString("R13_LENDING")); obj.setR13_NOMINAL_INTEREST_RATE(rs.getString("R13_NOMINAL_INTEREST_RATE"));
		obj.setR13_AVG_EFFECTIVE_RATE(rs.getString("R13_AVG_EFFECTIVE_RATE")); obj.setR13_VOLUME(rs.getBigDecimal("R13_VOLUME"));
		obj.setR14_LENDING(rs.getString("R14_LENDING")); obj.setR14_NOMINAL_INTEREST_RATE(rs.getString("R14_NOMINAL_INTEREST_RATE"));
		obj.setR14_AVG_EFFECTIVE_RATE(rs.getString("R14_AVG_EFFECTIVE_RATE")); obj.setR14_VOLUME(rs.getBigDecimal("R14_VOLUME"));
		obj.setR15_LENDING(rs.getString("R15_LENDING")); obj.setR15_NOMINAL_INTEREST_RATE(rs.getString("R15_NOMINAL_INTEREST_RATE"));
		obj.setR15_AVG_EFFECTIVE_RATE(rs.getString("R15_AVG_EFFECTIVE_RATE")); obj.setR15_VOLUME(rs.getBigDecimal("R15_VOLUME"));
		obj.setR16_LENDING(rs.getString("R16_LENDING")); obj.setR16_NOMINAL_INTEREST_RATE(rs.getString("R16_NOMINAL_INTEREST_RATE"));
		obj.setR16_AVG_EFFECTIVE_RATE(rs.getString("R16_AVG_EFFECTIVE_RATE")); obj.setR16_VOLUME(rs.getBigDecimal("R16_VOLUME"));
		obj.setR17_LENDING(rs.getString("R17_LENDING")); obj.setR17_NOMINAL_INTEREST_RATE(rs.getString("R17_NOMINAL_INTEREST_RATE"));
		obj.setR17_AVG_EFFECTIVE_RATE(rs.getString("R17_AVG_EFFECTIVE_RATE")); obj.setR17_VOLUME(rs.getBigDecimal("R17_VOLUME"));
		obj.setR18_LENDING(rs.getString("R18_LENDING")); obj.setR18_NOMINAL_INTEREST_RATE(rs.getString("R18_NOMINAL_INTEREST_RATE"));
		obj.setR18_AVG_EFFECTIVE_RATE(rs.getString("R18_AVG_EFFECTIVE_RATE")); obj.setR18_VOLUME(rs.getBigDecimal("R18_VOLUME"));
		obj.setR19_LENDING(rs.getString("R19_LENDING")); obj.setR19_NOMINAL_INTEREST_RATE(rs.getString("R19_NOMINAL_INTEREST_RATE"));
		obj.setR19_AVG_EFFECTIVE_RATE(rs.getString("R19_AVG_EFFECTIVE_RATE")); obj.setR19_VOLUME(rs.getBigDecimal("R19_VOLUME"));
		obj.setR20_LENDING(rs.getString("R20_LENDING")); obj.setR20_NOMINAL_INTEREST_RATE(rs.getString("R20_NOMINAL_INTEREST_RATE"));
		obj.setR20_AVG_EFFECTIVE_RATE(rs.getString("R20_AVG_EFFECTIVE_RATE")); obj.setR20_VOLUME(rs.getBigDecimal("R20_VOLUME"));
		obj.setR21_LENDING(rs.getString("R21_LENDING")); obj.setR21_NOMINAL_INTEREST_RATE(rs.getString("R21_NOMINAL_INTEREST_RATE"));
		obj.setR21_AVG_EFFECTIVE_RATE(rs.getString("R21_AVG_EFFECTIVE_RATE")); obj.setR21_VOLUME(rs.getBigDecimal("R21_VOLUME"));
		obj.setR22_LENDING(rs.getString("R22_LENDING")); obj.setR22_NOMINAL_INTEREST_RATE(rs.getString("R22_NOMINAL_INTEREST_RATE"));
		obj.setR22_AVG_EFFECTIVE_RATE(rs.getString("R22_AVG_EFFECTIVE_RATE")); obj.setR22_VOLUME(rs.getBigDecimal("R22_VOLUME"));
		obj.setR23_LENDING(rs.getString("R23_LENDING")); obj.setR23_NOMINAL_INTEREST_RATE(rs.getString("R23_NOMINAL_INTEREST_RATE"));
		obj.setR23_AVG_EFFECTIVE_RATE(rs.getString("R23_AVG_EFFECTIVE_RATE")); obj.setR23_VOLUME(rs.getBigDecimal("R23_VOLUME"));
		obj.setR24_LENDING(rs.getString("R24_LENDING")); obj.setR24_NOMINAL_INTEREST_RATE(rs.getString("R24_NOMINAL_INTEREST_RATE"));
		obj.setR24_AVG_EFFECTIVE_RATE(rs.getString("R24_AVG_EFFECTIVE_RATE")); obj.setR24_VOLUME(rs.getBigDecimal("R24_VOLUME"));
		obj.setR25_LENDING(rs.getString("R25_LENDING")); obj.setR25_NOMINAL_INTEREST_RATE(rs.getString("R25_NOMINAL_INTEREST_RATE"));
		obj.setR25_AVG_EFFECTIVE_RATE(rs.getString("R25_AVG_EFFECTIVE_RATE")); obj.setR25_VOLUME(rs.getBigDecimal("R25_VOLUME"));
		obj.setR26_LENDING(rs.getString("R26_LENDING")); obj.setR26_NOMINAL_INTEREST_RATE(rs.getString("R26_NOMINAL_INTEREST_RATE"));
		obj.setR26_AVG_EFFECTIVE_RATE(rs.getString("R26_AVG_EFFECTIVE_RATE")); obj.setR26_VOLUME(rs.getBigDecimal("R26_VOLUME"));
		obj.setR27_LENDING(rs.getString("R27_LENDING")); obj.setR27_NOMINAL_INTEREST_RATE(rs.getString("R27_NOMINAL_INTEREST_RATE"));
		obj.setR27_AVG_EFFECTIVE_RATE(rs.getString("R27_AVG_EFFECTIVE_RATE")); obj.setR27_VOLUME(rs.getBigDecimal("R27_VOLUME"));
		obj.setR28_LENDING(rs.getString("R28_LENDING")); obj.setR28_NOMINAL_INTEREST_RATE(rs.getString("R28_NOMINAL_INTEREST_RATE"));
		obj.setR28_AVG_EFFECTIVE_RATE(rs.getString("R28_AVG_EFFECTIVE_RATE")); obj.setR28_VOLUME(rs.getBigDecimal("R28_VOLUME"));
		obj.setR29_LENDING(rs.getString("R29_LENDING")); obj.setR29_NOMINAL_INTEREST_RATE(rs.getString("R29_NOMINAL_INTEREST_RATE"));
		obj.setR29_AVG_EFFECTIVE_RATE(rs.getString("R29_AVG_EFFECTIVE_RATE")); obj.setR29_VOLUME(rs.getBigDecimal("R29_VOLUME"));
		obj.setR30_LENDING(rs.getString("R30_LENDING")); obj.setR30_NOMINAL_INTEREST_RATE(rs.getString("R30_NOMINAL_INTEREST_RATE"));
		obj.setR30_AVG_EFFECTIVE_RATE(rs.getString("R30_AVG_EFFECTIVE_RATE")); obj.setR30_VOLUME(rs.getBigDecimal("R30_VOLUME"));
		obj.setR31_LENDING(rs.getString("R31_LENDING")); obj.setR31_NOMINAL_INTEREST_RATE(rs.getString("R31_NOMINAL_INTEREST_RATE"));
		obj.setR31_AVG_EFFECTIVE_RATE(rs.getString("R31_AVG_EFFECTIVE_RATE")); obj.setR31_VOLUME(rs.getBigDecimal("R31_VOLUME"));
		obj.setR32_LENDING(rs.getString("R32_LENDING")); obj.setR32_NOMINAL_INTEREST_RATE(rs.getString("R32_NOMINAL_INTEREST_RATE"));
		obj.setR32_AVG_EFFECTIVE_RATE(rs.getString("R32_AVG_EFFECTIVE_RATE")); obj.setR32_VOLUME(rs.getBigDecimal("R32_VOLUME"));
		obj.setR33_LENDING(rs.getString("R33_LENDING")); obj.setR33_NOMINAL_INTEREST_RATE(rs.getString("R33_NOMINAL_INTEREST_RATE"));
		obj.setR33_AVG_EFFECTIVE_RATE(rs.getString("R33_AVG_EFFECTIVE_RATE")); obj.setR33_VOLUME(rs.getBigDecimal("R33_VOLUME"));
		obj.setR34_LENDING(rs.getString("R34_LENDING")); obj.setR34_NOMINAL_INTEREST_RATE(rs.getString("R34_NOMINAL_INTEREST_RATE"));
		obj.setR34_AVG_EFFECTIVE_RATE(rs.getString("R34_AVG_EFFECTIVE_RATE")); obj.setR34_VOLUME(rs.getBigDecimal("R34_VOLUME"));
		obj.setR35_LENDING(rs.getString("R35_LENDING")); obj.setR35_NOMINAL_INTEREST_RATE(rs.getString("R35_NOMINAL_INTEREST_RATE"));
		obj.setR35_AVG_EFFECTIVE_RATE(rs.getString("R35_AVG_EFFECTIVE_RATE")); obj.setR35_VOLUME(rs.getBigDecimal("R35_VOLUME"));
		obj.setR36_LENDING(rs.getString("R36_LENDING")); obj.setR36_NOMINAL_INTEREST_RATE(rs.getString("R36_NOMINAL_INTEREST_RATE"));
		obj.setR36_AVG_EFFECTIVE_RATE(rs.getString("R36_AVG_EFFECTIVE_RATE")); obj.setR36_VOLUME(rs.getBigDecimal("R36_VOLUME"));
		obj.setR37_LENDING(rs.getString("R37_LENDING")); obj.setR37_NOMINAL_INTEREST_RATE(rs.getString("R37_NOMINAL_INTEREST_RATE"));
		obj.setR37_AVG_EFFECTIVE_RATE(rs.getString("R37_AVG_EFFECTIVE_RATE")); obj.setR37_VOLUME(rs.getBigDecimal("R37_VOLUME"));
		obj.setR38_LENDING(rs.getString("R38_LENDING")); obj.setR38_NOMINAL_INTEREST_RATE(rs.getString("R38_NOMINAL_INTEREST_RATE"));
		obj.setR38_AVG_EFFECTIVE_RATE(rs.getString("R38_AVG_EFFECTIVE_RATE")); obj.setR38_VOLUME(rs.getBigDecimal("R38_VOLUME"));
		obj.setR39_LENDING(rs.getString("R39_LENDING")); obj.setR39_NOMINAL_INTEREST_RATE(rs.getString("R39_NOMINAL_INTEREST_RATE"));
		obj.setR39_AVG_EFFECTIVE_RATE(rs.getString("R39_AVG_EFFECTIVE_RATE")); obj.setR39_VOLUME(rs.getBigDecimal("R39_VOLUME"));
		obj.setR40_LENDING(rs.getString("R40_LENDING")); obj.setR40_NOMINAL_INTEREST_RATE(rs.getString("R40_NOMINAL_INTEREST_RATE"));
		obj.setR40_AVG_EFFECTIVE_RATE(rs.getString("R40_AVG_EFFECTIVE_RATE")); obj.setR40_VOLUME(rs.getBigDecimal("R40_VOLUME"));
		obj.setR41_LENDING(rs.getString("R41_LENDING")); obj.setR41_NOMINAL_INTEREST_RATE(rs.getString("R41_NOMINAL_INTEREST_RATE"));
		obj.setR41_AVG_EFFECTIVE_RATE(rs.getString("R41_AVG_EFFECTIVE_RATE")); obj.setR41_VOLUME(rs.getBigDecimal("R41_VOLUME"));
		obj.setR42_LENDING(rs.getString("R42_LENDING")); obj.setR42_NOMINAL_INTEREST_RATE(rs.getString("R42_NOMINAL_INTEREST_RATE"));
		obj.setR42_AVG_EFFECTIVE_RATE(rs.getString("R42_AVG_EFFECTIVE_RATE")); obj.setR42_VOLUME(rs.getBigDecimal("R42_VOLUME"));
		return obj;
	}
}

class M_INT_RATESArchivalDetailRowMapper implements RowMapper<M_INT_RATES_Archival_Detail_Entity> {
	@Override
	public M_INT_RATES_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
		M_INT_RATES_Archival_Detail_Entity obj = new M_INT_RATES_Archival_Detail_Entity();
		obj.setReport_date(rs.getDate("REPORT_DATE"));
		obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
		obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
		obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
		obj.setReport_code(rs.getString("REPORT_CODE"));
		obj.setReport_desc(rs.getString("REPORT_DESC"));
		obj.setEntity_flg(rs.getString("ENTITY_FLG"));
		obj.setModify_flg(rs.getString("MODIFY_FLG"));
		obj.setDel_flg(rs.getString("DEL_FLG"));
		obj.setR11_LENDING(rs.getString("R11_LENDING")); obj.setR11_NOMINAL_INTEREST_RATE(rs.getString("R11_NOMINAL_INTEREST_RATE"));
		obj.setR11_AVG_EFFECTIVE_RATE(rs.getString("R11_AVG_EFFECTIVE_RATE")); obj.setR11_VOLUME(rs.getBigDecimal("R11_VOLUME"));
		obj.setR12_LENDING(rs.getString("R12_LENDING")); obj.setR12_NOMINAL_INTEREST_RATE(rs.getString("R12_NOMINAL_INTEREST_RATE"));
		obj.setR12_AVG_EFFECTIVE_RATE(rs.getString("R12_AVG_EFFECTIVE_RATE")); obj.setR12_VOLUME(rs.getBigDecimal("R12_VOLUME"));
		obj.setR13_LENDING(rs.getString("R13_LENDING")); obj.setR13_NOMINAL_INTEREST_RATE(rs.getString("R13_NOMINAL_INTEREST_RATE"));
		obj.setR13_AVG_EFFECTIVE_RATE(rs.getString("R13_AVG_EFFECTIVE_RATE")); obj.setR13_VOLUME(rs.getBigDecimal("R13_VOLUME"));
		obj.setR14_LENDING(rs.getString("R14_LENDING")); obj.setR14_NOMINAL_INTEREST_RATE(rs.getString("R14_NOMINAL_INTEREST_RATE"));
		obj.setR14_AVG_EFFECTIVE_RATE(rs.getString("R14_AVG_EFFECTIVE_RATE")); obj.setR14_VOLUME(rs.getBigDecimal("R14_VOLUME"));
		obj.setR15_LENDING(rs.getString("R15_LENDING")); obj.setR15_NOMINAL_INTEREST_RATE(rs.getString("R15_NOMINAL_INTEREST_RATE"));
		obj.setR15_AVG_EFFECTIVE_RATE(rs.getString("R15_AVG_EFFECTIVE_RATE")); obj.setR15_VOLUME(rs.getBigDecimal("R15_VOLUME"));
		obj.setR16_LENDING(rs.getString("R16_LENDING")); obj.setR16_NOMINAL_INTEREST_RATE(rs.getString("R16_NOMINAL_INTEREST_RATE"));
		obj.setR16_AVG_EFFECTIVE_RATE(rs.getString("R16_AVG_EFFECTIVE_RATE")); obj.setR16_VOLUME(rs.getBigDecimal("R16_VOLUME"));
		obj.setR17_LENDING(rs.getString("R17_LENDING")); obj.setR17_NOMINAL_INTEREST_RATE(rs.getString("R17_NOMINAL_INTEREST_RATE"));
		obj.setR17_AVG_EFFECTIVE_RATE(rs.getString("R17_AVG_EFFECTIVE_RATE")); obj.setR17_VOLUME(rs.getBigDecimal("R17_VOLUME"));
		obj.setR18_LENDING(rs.getString("R18_LENDING")); obj.setR18_NOMINAL_INTEREST_RATE(rs.getString("R18_NOMINAL_INTEREST_RATE"));
		obj.setR18_AVG_EFFECTIVE_RATE(rs.getString("R18_AVG_EFFECTIVE_RATE")); obj.setR18_VOLUME(rs.getBigDecimal("R18_VOLUME"));
		obj.setR19_LENDING(rs.getString("R19_LENDING")); obj.setR19_NOMINAL_INTEREST_RATE(rs.getString("R19_NOMINAL_INTEREST_RATE"));
		obj.setR19_AVG_EFFECTIVE_RATE(rs.getString("R19_AVG_EFFECTIVE_RATE")); obj.setR19_VOLUME(rs.getBigDecimal("R19_VOLUME"));
		obj.setR20_LENDING(rs.getString("R20_LENDING")); obj.setR20_NOMINAL_INTEREST_RATE(rs.getString("R20_NOMINAL_INTEREST_RATE"));
		obj.setR20_AVG_EFFECTIVE_RATE(rs.getString("R20_AVG_EFFECTIVE_RATE")); obj.setR20_VOLUME(rs.getBigDecimal("R20_VOLUME"));
		obj.setR21_LENDING(rs.getString("R21_LENDING")); obj.setR21_NOMINAL_INTEREST_RATE(rs.getString("R21_NOMINAL_INTEREST_RATE"));
		obj.setR21_AVG_EFFECTIVE_RATE(rs.getString("R21_AVG_EFFECTIVE_RATE")); obj.setR21_VOLUME(rs.getBigDecimal("R21_VOLUME"));
		obj.setR22_LENDING(rs.getString("R22_LENDING")); obj.setR22_NOMINAL_INTEREST_RATE(rs.getString("R22_NOMINAL_INTEREST_RATE"));
		obj.setR22_AVG_EFFECTIVE_RATE(rs.getString("R22_AVG_EFFECTIVE_RATE")); obj.setR22_VOLUME(rs.getBigDecimal("R22_VOLUME"));
		obj.setR23_LENDING(rs.getString("R23_LENDING")); obj.setR23_NOMINAL_INTEREST_RATE(rs.getString("R23_NOMINAL_INTEREST_RATE"));
		obj.setR23_AVG_EFFECTIVE_RATE(rs.getString("R23_AVG_EFFECTIVE_RATE")); obj.setR23_VOLUME(rs.getBigDecimal("R23_VOLUME"));
		obj.setR24_LENDING(rs.getString("R24_LENDING")); obj.setR24_NOMINAL_INTEREST_RATE(rs.getString("R24_NOMINAL_INTEREST_RATE"));
		obj.setR24_AVG_EFFECTIVE_RATE(rs.getString("R24_AVG_EFFECTIVE_RATE")); obj.setR24_VOLUME(rs.getBigDecimal("R24_VOLUME"));
		obj.setR25_LENDING(rs.getString("R25_LENDING")); obj.setR25_NOMINAL_INTEREST_RATE(rs.getString("R25_NOMINAL_INTEREST_RATE"));
		obj.setR25_AVG_EFFECTIVE_RATE(rs.getString("R25_AVG_EFFECTIVE_RATE")); obj.setR25_VOLUME(rs.getBigDecimal("R25_VOLUME"));
		obj.setR26_LENDING(rs.getString("R26_LENDING")); obj.setR26_NOMINAL_INTEREST_RATE(rs.getString("R26_NOMINAL_INTEREST_RATE"));
		obj.setR26_AVG_EFFECTIVE_RATE(rs.getString("R26_AVG_EFFECTIVE_RATE")); obj.setR26_VOLUME(rs.getBigDecimal("R26_VOLUME"));
		obj.setR27_LENDING(rs.getString("R27_LENDING")); obj.setR27_NOMINAL_INTEREST_RATE(rs.getString("R27_NOMINAL_INTEREST_RATE"));
		obj.setR27_AVG_EFFECTIVE_RATE(rs.getString("R27_AVG_EFFECTIVE_RATE")); obj.setR27_VOLUME(rs.getBigDecimal("R27_VOLUME"));
		obj.setR28_LENDING(rs.getString("R28_LENDING")); obj.setR28_NOMINAL_INTEREST_RATE(rs.getString("R28_NOMINAL_INTEREST_RATE"));
		obj.setR28_AVG_EFFECTIVE_RATE(rs.getString("R28_AVG_EFFECTIVE_RATE")); obj.setR28_VOLUME(rs.getBigDecimal("R28_VOLUME"));
		obj.setR29_LENDING(rs.getString("R29_LENDING")); obj.setR29_NOMINAL_INTEREST_RATE(rs.getString("R29_NOMINAL_INTEREST_RATE"));
		obj.setR29_AVG_EFFECTIVE_RATE(rs.getString("R29_AVG_EFFECTIVE_RATE")); obj.setR29_VOLUME(rs.getBigDecimal("R29_VOLUME"));
		obj.setR30_LENDING(rs.getString("R30_LENDING")); obj.setR30_NOMINAL_INTEREST_RATE(rs.getString("R30_NOMINAL_INTEREST_RATE"));
		obj.setR30_AVG_EFFECTIVE_RATE(rs.getString("R30_AVG_EFFECTIVE_RATE")); obj.setR30_VOLUME(rs.getBigDecimal("R30_VOLUME"));
		obj.setR31_LENDING(rs.getString("R31_LENDING")); obj.setR31_NOMINAL_INTEREST_RATE(rs.getString("R31_NOMINAL_INTEREST_RATE"));
		obj.setR31_AVG_EFFECTIVE_RATE(rs.getString("R31_AVG_EFFECTIVE_RATE")); obj.setR31_VOLUME(rs.getBigDecimal("R31_VOLUME"));
		obj.setR32_LENDING(rs.getString("R32_LENDING")); obj.setR32_NOMINAL_INTEREST_RATE(rs.getString("R32_NOMINAL_INTEREST_RATE"));
		obj.setR32_AVG_EFFECTIVE_RATE(rs.getString("R32_AVG_EFFECTIVE_RATE")); obj.setR32_VOLUME(rs.getBigDecimal("R32_VOLUME"));
		obj.setR33_LENDING(rs.getString("R33_LENDING")); obj.setR33_NOMINAL_INTEREST_RATE(rs.getString("R33_NOMINAL_INTEREST_RATE"));
		obj.setR33_AVG_EFFECTIVE_RATE(rs.getString("R33_AVG_EFFECTIVE_RATE")); obj.setR33_VOLUME(rs.getBigDecimal("R33_VOLUME"));
		obj.setR34_LENDING(rs.getString("R34_LENDING")); obj.setR34_NOMINAL_INTEREST_RATE(rs.getString("R34_NOMINAL_INTEREST_RATE"));
		obj.setR34_AVG_EFFECTIVE_RATE(rs.getString("R34_AVG_EFFECTIVE_RATE")); obj.setR34_VOLUME(rs.getBigDecimal("R34_VOLUME"));
		obj.setR35_LENDING(rs.getString("R35_LENDING")); obj.setR35_NOMINAL_INTEREST_RATE(rs.getString("R35_NOMINAL_INTEREST_RATE"));
		obj.setR35_AVG_EFFECTIVE_RATE(rs.getString("R35_AVG_EFFECTIVE_RATE")); obj.setR35_VOLUME(rs.getBigDecimal("R35_VOLUME"));
		obj.setR36_LENDING(rs.getString("R36_LENDING")); obj.setR36_NOMINAL_INTEREST_RATE(rs.getString("R36_NOMINAL_INTEREST_RATE"));
		obj.setR36_AVG_EFFECTIVE_RATE(rs.getString("R36_AVG_EFFECTIVE_RATE")); obj.setR36_VOLUME(rs.getBigDecimal("R36_VOLUME"));
		obj.setR37_LENDING(rs.getString("R37_LENDING")); obj.setR37_NOMINAL_INTEREST_RATE(rs.getString("R37_NOMINAL_INTEREST_RATE"));
		obj.setR37_AVG_EFFECTIVE_RATE(rs.getString("R37_AVG_EFFECTIVE_RATE")); obj.setR37_VOLUME(rs.getBigDecimal("R37_VOLUME"));
		obj.setR38_LENDING(rs.getString("R38_LENDING")); obj.setR38_NOMINAL_INTEREST_RATE(rs.getString("R38_NOMINAL_INTEREST_RATE"));
		obj.setR38_AVG_EFFECTIVE_RATE(rs.getString("R38_AVG_EFFECTIVE_RATE")); obj.setR38_VOLUME(rs.getBigDecimal("R38_VOLUME"));
		obj.setR39_LENDING(rs.getString("R39_LENDING")); obj.setR39_NOMINAL_INTEREST_RATE(rs.getString("R39_NOMINAL_INTEREST_RATE"));
		obj.setR39_AVG_EFFECTIVE_RATE(rs.getString("R39_AVG_EFFECTIVE_RATE")); obj.setR39_VOLUME(rs.getBigDecimal("R39_VOLUME"));
		obj.setR40_LENDING(rs.getString("R40_LENDING")); obj.setR40_NOMINAL_INTEREST_RATE(rs.getString("R40_NOMINAL_INTEREST_RATE"));
		obj.setR40_AVG_EFFECTIVE_RATE(rs.getString("R40_AVG_EFFECTIVE_RATE")); obj.setR40_VOLUME(rs.getBigDecimal("R40_VOLUME"));
		obj.setR41_LENDING(rs.getString("R41_LENDING")); obj.setR41_NOMINAL_INTEREST_RATE(rs.getString("R41_NOMINAL_INTEREST_RATE"));
		obj.setR41_AVG_EFFECTIVE_RATE(rs.getString("R41_AVG_EFFECTIVE_RATE")); obj.setR41_VOLUME(rs.getBigDecimal("R41_VOLUME"));
		obj.setR42_LENDING(rs.getString("R42_LENDING")); obj.setR42_NOMINAL_INTEREST_RATE(rs.getString("R42_NOMINAL_INTEREST_RATE"));
		obj.setR42_AVG_EFFECTIVE_RATE(rs.getString("R42_AVG_EFFECTIVE_RATE")); obj.setR42_VOLUME(rs.getBigDecimal("R42_VOLUME"));
		return obj;
	}
}

class M_INT_RATESResubSummaryRowMapper implements RowMapper<M_INT_RATES_RESUB_Summary_Entity> {
	@Override
	public M_INT_RATES_RESUB_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
		M_INT_RATES_RESUB_Summary_Entity obj = new M_INT_RATES_RESUB_Summary_Entity();
		obj.setReport_date(rs.getDate("REPORT_DATE"));
		obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
		obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
		obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
		obj.setReport_code(rs.getString("REPORT_CODE"));
		obj.setReport_desc(rs.getString("REPORT_DESC"));
		obj.setEntity_flg(rs.getString("ENTITY_FLG"));
		obj.setModify_flg(rs.getString("MODIFY_FLG"));
		obj.setDel_flg(rs.getString("DEL_FLG"));
		obj.setR11_LENDING(rs.getString("R11_LENDING")); obj.setR11_NOMINAL_INTEREST_RATE(rs.getString("R11_NOMINAL_INTEREST_RATE"));
		obj.setR11_AVG_EFFECTIVE_RATE(rs.getString("R11_AVG_EFFECTIVE_RATE")); obj.setR11_VOLUME(rs.getBigDecimal("R11_VOLUME"));
		obj.setR12_LENDING(rs.getString("R12_LENDING")); obj.setR12_NOMINAL_INTEREST_RATE(rs.getString("R12_NOMINAL_INTEREST_RATE"));
		obj.setR12_AVG_EFFECTIVE_RATE(rs.getString("R12_AVG_EFFECTIVE_RATE")); obj.setR12_VOLUME(rs.getBigDecimal("R12_VOLUME"));
		obj.setR13_LENDING(rs.getString("R13_LENDING")); obj.setR13_NOMINAL_INTEREST_RATE(rs.getString("R13_NOMINAL_INTEREST_RATE"));
		obj.setR13_AVG_EFFECTIVE_RATE(rs.getString("R13_AVG_EFFECTIVE_RATE")); obj.setR13_VOLUME(rs.getBigDecimal("R13_VOLUME"));
		obj.setR14_LENDING(rs.getString("R14_LENDING")); obj.setR14_NOMINAL_INTEREST_RATE(rs.getString("R14_NOMINAL_INTEREST_RATE"));
		obj.setR14_AVG_EFFECTIVE_RATE(rs.getString("R14_AVG_EFFECTIVE_RATE")); obj.setR14_VOLUME(rs.getBigDecimal("R14_VOLUME"));
		obj.setR15_LENDING(rs.getString("R15_LENDING")); obj.setR15_NOMINAL_INTEREST_RATE(rs.getString("R15_NOMINAL_INTEREST_RATE"));
		obj.setR15_AVG_EFFECTIVE_RATE(rs.getString("R15_AVG_EFFECTIVE_RATE")); obj.setR15_VOLUME(rs.getBigDecimal("R15_VOLUME"));
		obj.setR16_LENDING(rs.getString("R16_LENDING")); obj.setR16_NOMINAL_INTEREST_RATE(rs.getString("R16_NOMINAL_INTEREST_RATE"));
		obj.setR16_AVG_EFFECTIVE_RATE(rs.getString("R16_AVG_EFFECTIVE_RATE")); obj.setR16_VOLUME(rs.getBigDecimal("R16_VOLUME"));
		obj.setR17_LENDING(rs.getString("R17_LENDING")); obj.setR17_NOMINAL_INTEREST_RATE(rs.getString("R17_NOMINAL_INTEREST_RATE"));
		obj.setR17_AVG_EFFECTIVE_RATE(rs.getString("R17_AVG_EFFECTIVE_RATE")); obj.setR17_VOLUME(rs.getBigDecimal("R17_VOLUME"));
		obj.setR18_LENDING(rs.getString("R18_LENDING")); obj.setR18_NOMINAL_INTEREST_RATE(rs.getString("R18_NOMINAL_INTEREST_RATE"));
		obj.setR18_AVG_EFFECTIVE_RATE(rs.getString("R18_AVG_EFFECTIVE_RATE")); obj.setR18_VOLUME(rs.getBigDecimal("R18_VOLUME"));
		obj.setR19_LENDING(rs.getString("R19_LENDING")); obj.setR19_NOMINAL_INTEREST_RATE(rs.getString("R19_NOMINAL_INTEREST_RATE"));
		obj.setR19_AVG_EFFECTIVE_RATE(rs.getString("R19_AVG_EFFECTIVE_RATE")); obj.setR19_VOLUME(rs.getBigDecimal("R19_VOLUME"));
		obj.setR20_LENDING(rs.getString("R20_LENDING")); obj.setR20_NOMINAL_INTEREST_RATE(rs.getString("R20_NOMINAL_INTEREST_RATE"));
		obj.setR20_AVG_EFFECTIVE_RATE(rs.getString("R20_AVG_EFFECTIVE_RATE")); obj.setR20_VOLUME(rs.getBigDecimal("R20_VOLUME"));
		obj.setR21_LENDING(rs.getString("R21_LENDING")); obj.setR21_NOMINAL_INTEREST_RATE(rs.getString("R21_NOMINAL_INTEREST_RATE"));
		obj.setR21_AVG_EFFECTIVE_RATE(rs.getString("R21_AVG_EFFECTIVE_RATE")); obj.setR21_VOLUME(rs.getBigDecimal("R21_VOLUME"));
		obj.setR22_LENDING(rs.getString("R22_LENDING")); obj.setR22_NOMINAL_INTEREST_RATE(rs.getString("R22_NOMINAL_INTEREST_RATE"));
		obj.setR22_AVG_EFFECTIVE_RATE(rs.getString("R22_AVG_EFFECTIVE_RATE")); obj.setR22_VOLUME(rs.getBigDecimal("R22_VOLUME"));
		obj.setR23_LENDING(rs.getString("R23_LENDING")); obj.setR23_NOMINAL_INTEREST_RATE(rs.getString("R23_NOMINAL_INTEREST_RATE"));
		obj.setR23_AVG_EFFECTIVE_RATE(rs.getString("R23_AVG_EFFECTIVE_RATE")); obj.setR23_VOLUME(rs.getBigDecimal("R23_VOLUME"));
		obj.setR24_LENDING(rs.getString("R24_LENDING")); obj.setR24_NOMINAL_INTEREST_RATE(rs.getString("R24_NOMINAL_INTEREST_RATE"));
		obj.setR24_AVG_EFFECTIVE_RATE(rs.getString("R24_AVG_EFFECTIVE_RATE")); obj.setR24_VOLUME(rs.getBigDecimal("R24_VOLUME"));
		obj.setR25_LENDING(rs.getString("R25_LENDING")); obj.setR25_NOMINAL_INTEREST_RATE(rs.getString("R25_NOMINAL_INTEREST_RATE"));
		obj.setR25_AVG_EFFECTIVE_RATE(rs.getString("R25_AVG_EFFECTIVE_RATE")); obj.setR25_VOLUME(rs.getBigDecimal("R25_VOLUME"));
		obj.setR26_LENDING(rs.getString("R26_LENDING")); obj.setR26_NOMINAL_INTEREST_RATE(rs.getString("R26_NOMINAL_INTEREST_RATE"));
		obj.setR26_AVG_EFFECTIVE_RATE(rs.getString("R26_AVG_EFFECTIVE_RATE")); obj.setR26_VOLUME(rs.getBigDecimal("R26_VOLUME"));
		obj.setR27_LENDING(rs.getString("R27_LENDING")); obj.setR27_NOMINAL_INTEREST_RATE(rs.getString("R27_NOMINAL_INTEREST_RATE"));
		obj.setR27_AVG_EFFECTIVE_RATE(rs.getString("R27_AVG_EFFECTIVE_RATE")); obj.setR27_VOLUME(rs.getBigDecimal("R27_VOLUME"));
		obj.setR28_LENDING(rs.getString("R28_LENDING")); obj.setR28_NOMINAL_INTEREST_RATE(rs.getString("R28_NOMINAL_INTEREST_RATE"));
		obj.setR28_AVG_EFFECTIVE_RATE(rs.getString("R28_AVG_EFFECTIVE_RATE")); obj.setR28_VOLUME(rs.getBigDecimal("R28_VOLUME"));
		obj.setR29_LENDING(rs.getString("R29_LENDING")); obj.setR29_NOMINAL_INTEREST_RATE(rs.getString("R29_NOMINAL_INTEREST_RATE"));
		obj.setR29_AVG_EFFECTIVE_RATE(rs.getString("R29_AVG_EFFECTIVE_RATE")); obj.setR29_VOLUME(rs.getBigDecimal("R29_VOLUME"));
		obj.setR30_LENDING(rs.getString("R30_LENDING")); obj.setR30_NOMINAL_INTEREST_RATE(rs.getString("R30_NOMINAL_INTEREST_RATE"));
		obj.setR30_AVG_EFFECTIVE_RATE(rs.getString("R30_AVG_EFFECTIVE_RATE")); obj.setR30_VOLUME(rs.getBigDecimal("R30_VOLUME"));
		obj.setR31_LENDING(rs.getString("R31_LENDING")); obj.setR31_NOMINAL_INTEREST_RATE(rs.getString("R31_NOMINAL_INTEREST_RATE"));
		obj.setR31_AVG_EFFECTIVE_RATE(rs.getString("R31_AVG_EFFECTIVE_RATE")); obj.setR31_VOLUME(rs.getBigDecimal("R31_VOLUME"));
		obj.setR32_LENDING(rs.getString("R32_LENDING")); obj.setR32_NOMINAL_INTEREST_RATE(rs.getString("R32_NOMINAL_INTEREST_RATE"));
		obj.setR32_AVG_EFFECTIVE_RATE(rs.getString("R32_AVG_EFFECTIVE_RATE")); obj.setR32_VOLUME(rs.getBigDecimal("R32_VOLUME"));
		obj.setR33_LENDING(rs.getString("R33_LENDING")); obj.setR33_NOMINAL_INTEREST_RATE(rs.getString("R33_NOMINAL_INTEREST_RATE"));
		obj.setR33_AVG_EFFECTIVE_RATE(rs.getString("R33_AVG_EFFECTIVE_RATE")); obj.setR33_VOLUME(rs.getBigDecimal("R33_VOLUME"));
		obj.setR34_LENDING(rs.getString("R34_LENDING")); obj.setR34_NOMINAL_INTEREST_RATE(rs.getString("R34_NOMINAL_INTEREST_RATE"));
		obj.setR34_AVG_EFFECTIVE_RATE(rs.getString("R34_AVG_EFFECTIVE_RATE")); obj.setR34_VOLUME(rs.getBigDecimal("R34_VOLUME"));
		obj.setR35_LENDING(rs.getString("R35_LENDING")); obj.setR35_NOMINAL_INTEREST_RATE(rs.getString("R35_NOMINAL_INTEREST_RATE"));
		obj.setR35_AVG_EFFECTIVE_RATE(rs.getString("R35_AVG_EFFECTIVE_RATE")); obj.setR35_VOLUME(rs.getBigDecimal("R35_VOLUME"));
		obj.setR36_LENDING(rs.getString("R36_LENDING")); obj.setR36_NOMINAL_INTEREST_RATE(rs.getString("R36_NOMINAL_INTEREST_RATE"));
		obj.setR36_AVG_EFFECTIVE_RATE(rs.getString("R36_AVG_EFFECTIVE_RATE")); obj.setR36_VOLUME(rs.getBigDecimal("R36_VOLUME"));
		obj.setR37_LENDING(rs.getString("R37_LENDING")); obj.setR37_NOMINAL_INTEREST_RATE(rs.getString("R37_NOMINAL_INTEREST_RATE"));
		obj.setR37_AVG_EFFECTIVE_RATE(rs.getString("R37_AVG_EFFECTIVE_RATE")); obj.setR37_VOLUME(rs.getBigDecimal("R37_VOLUME"));
		obj.setR38_LENDING(rs.getString("R38_LENDING")); obj.setR38_NOMINAL_INTEREST_RATE(rs.getString("R38_NOMINAL_INTEREST_RATE"));
		obj.setR38_AVG_EFFECTIVE_RATE(rs.getString("R38_AVG_EFFECTIVE_RATE")); obj.setR38_VOLUME(rs.getBigDecimal("R38_VOLUME"));
		obj.setR39_LENDING(rs.getString("R39_LENDING")); obj.setR39_NOMINAL_INTEREST_RATE(rs.getString("R39_NOMINAL_INTEREST_RATE"));
		obj.setR39_AVG_EFFECTIVE_RATE(rs.getString("R39_AVG_EFFECTIVE_RATE")); obj.setR39_VOLUME(rs.getBigDecimal("R39_VOLUME"));
		obj.setR40_LENDING(rs.getString("R40_LENDING")); obj.setR40_NOMINAL_INTEREST_RATE(rs.getString("R40_NOMINAL_INTEREST_RATE"));
		obj.setR40_AVG_EFFECTIVE_RATE(rs.getString("R40_AVG_EFFECTIVE_RATE")); obj.setR40_VOLUME(rs.getBigDecimal("R40_VOLUME"));
		obj.setR41_LENDING(rs.getString("R41_LENDING")); obj.setR41_NOMINAL_INTEREST_RATE(rs.getString("R41_NOMINAL_INTEREST_RATE"));
		obj.setR41_AVG_EFFECTIVE_RATE(rs.getString("R41_AVG_EFFECTIVE_RATE")); obj.setR41_VOLUME(rs.getBigDecimal("R41_VOLUME"));
		obj.setR42_LENDING(rs.getString("R42_LENDING")); obj.setR42_NOMINAL_INTEREST_RATE(rs.getString("R42_NOMINAL_INTEREST_RATE"));
		obj.setR42_AVG_EFFECTIVE_RATE(rs.getString("R42_AVG_EFFECTIVE_RATE")); obj.setR42_VOLUME(rs.getBigDecimal("R42_VOLUME"));
		return obj;
	}
}

class M_INT_RATESResubDetailRowMapper implements RowMapper<M_INT_RATES_RESUB_Detail_Entity> {
	@Override
	public M_INT_RATES_RESUB_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
		M_INT_RATES_RESUB_Detail_Entity obj = new M_INT_RATES_RESUB_Detail_Entity();
		obj.setReport_date(rs.getDate("REPORT_DATE"));
		obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
		obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
		obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
		obj.setReport_code(rs.getString("REPORT_CODE"));
		obj.setReport_desc(rs.getString("REPORT_DESC"));
		obj.setEntity_flg(rs.getString("ENTITY_FLG"));
		obj.setModify_flg(rs.getString("MODIFY_FLG"));
		obj.setDel_flg(rs.getString("DEL_FLG"));
		obj.setR11_LENDING(rs.getString("R11_LENDING")); obj.setR11_NOMINAL_INTEREST_RATE(rs.getString("R11_NOMINAL_INTEREST_RATE"));
		obj.setR11_AVG_EFFECTIVE_RATE(rs.getString("R11_AVG_EFFECTIVE_RATE")); obj.setR11_VOLUME(rs.getBigDecimal("R11_VOLUME"));
		obj.setR12_LENDING(rs.getString("R12_LENDING")); obj.setR12_NOMINAL_INTEREST_RATE(rs.getString("R12_NOMINAL_INTEREST_RATE"));
		obj.setR12_AVG_EFFECTIVE_RATE(rs.getString("R12_AVG_EFFECTIVE_RATE")); obj.setR12_VOLUME(rs.getBigDecimal("R12_VOLUME"));
		obj.setR13_LENDING(rs.getString("R13_LENDING")); obj.setR13_NOMINAL_INTEREST_RATE(rs.getString("R13_NOMINAL_INTEREST_RATE"));
		obj.setR13_AVG_EFFECTIVE_RATE(rs.getString("R13_AVG_EFFECTIVE_RATE")); obj.setR13_VOLUME(rs.getBigDecimal("R13_VOLUME"));
		obj.setR14_LENDING(rs.getString("R14_LENDING")); obj.setR14_NOMINAL_INTEREST_RATE(rs.getString("R14_NOMINAL_INTEREST_RATE"));
		obj.setR14_AVG_EFFECTIVE_RATE(rs.getString("R14_AVG_EFFECTIVE_RATE")); obj.setR14_VOLUME(rs.getBigDecimal("R14_VOLUME"));
		obj.setR15_LENDING(rs.getString("R15_LENDING")); obj.setR15_NOMINAL_INTEREST_RATE(rs.getString("R15_NOMINAL_INTEREST_RATE"));
		obj.setR15_AVG_EFFECTIVE_RATE(rs.getString("R15_AVG_EFFECTIVE_RATE")); obj.setR15_VOLUME(rs.getBigDecimal("R15_VOLUME"));
		obj.setR16_LENDING(rs.getString("R16_LENDING")); obj.setR16_NOMINAL_INTEREST_RATE(rs.getString("R16_NOMINAL_INTEREST_RATE"));
		obj.setR16_AVG_EFFECTIVE_RATE(rs.getString("R16_AVG_EFFECTIVE_RATE")); obj.setR16_VOLUME(rs.getBigDecimal("R16_VOLUME"));
		obj.setR17_LENDING(rs.getString("R17_LENDING")); obj.setR17_NOMINAL_INTEREST_RATE(rs.getString("R17_NOMINAL_INTEREST_RATE"));
		obj.setR17_AVG_EFFECTIVE_RATE(rs.getString("R17_AVG_EFFECTIVE_RATE")); obj.setR17_VOLUME(rs.getBigDecimal("R17_VOLUME"));
		obj.setR18_LENDING(rs.getString("R18_LENDING")); obj.setR18_NOMINAL_INTEREST_RATE(rs.getString("R18_NOMINAL_INTEREST_RATE"));
		obj.setR18_AVG_EFFECTIVE_RATE(rs.getString("R18_AVG_EFFECTIVE_RATE")); obj.setR18_VOLUME(rs.getBigDecimal("R18_VOLUME"));
		obj.setR19_LENDING(rs.getString("R19_LENDING")); obj.setR19_NOMINAL_INTEREST_RATE(rs.getString("R19_NOMINAL_INTEREST_RATE"));
		obj.setR19_AVG_EFFECTIVE_RATE(rs.getString("R19_AVG_EFFECTIVE_RATE")); obj.setR19_VOLUME(rs.getBigDecimal("R19_VOLUME"));
		obj.setR20_LENDING(rs.getString("R20_LENDING")); obj.setR20_NOMINAL_INTEREST_RATE(rs.getString("R20_NOMINAL_INTEREST_RATE"));
		obj.setR20_AVG_EFFECTIVE_RATE(rs.getString("R20_AVG_EFFECTIVE_RATE")); obj.setR20_VOLUME(rs.getBigDecimal("R20_VOLUME"));
		obj.setR21_LENDING(rs.getString("R21_LENDING")); obj.setR21_NOMINAL_INTEREST_RATE(rs.getString("R21_NOMINAL_INTEREST_RATE"));
		obj.setR21_AVG_EFFECTIVE_RATE(rs.getString("R21_AVG_EFFECTIVE_RATE")); obj.setR21_VOLUME(rs.getBigDecimal("R21_VOLUME"));
		obj.setR22_LENDING(rs.getString("R22_LENDING")); obj.setR22_NOMINAL_INTEREST_RATE(rs.getString("R22_NOMINAL_INTEREST_RATE"));
		obj.setR22_AVG_EFFECTIVE_RATE(rs.getString("R22_AVG_EFFECTIVE_RATE")); obj.setR22_VOLUME(rs.getBigDecimal("R22_VOLUME"));
		obj.setR23_LENDING(rs.getString("R23_LENDING")); obj.setR23_NOMINAL_INTEREST_RATE(rs.getString("R23_NOMINAL_INTEREST_RATE"));
		obj.setR23_AVG_EFFECTIVE_RATE(rs.getString("R23_AVG_EFFECTIVE_RATE")); obj.setR23_VOLUME(rs.getBigDecimal("R23_VOLUME"));
		obj.setR24_LENDING(rs.getString("R24_LENDING")); obj.setR24_NOMINAL_INTEREST_RATE(rs.getString("R24_NOMINAL_INTEREST_RATE"));
		obj.setR24_AVG_EFFECTIVE_RATE(rs.getString("R24_AVG_EFFECTIVE_RATE")); obj.setR24_VOLUME(rs.getBigDecimal("R24_VOLUME"));
		obj.setR25_LENDING(rs.getString("R25_LENDING")); obj.setR25_NOMINAL_INTEREST_RATE(rs.getString("R25_NOMINAL_INTEREST_RATE"));
		obj.setR25_AVG_EFFECTIVE_RATE(rs.getString("R25_AVG_EFFECTIVE_RATE")); obj.setR25_VOLUME(rs.getBigDecimal("R25_VOLUME"));
		obj.setR26_LENDING(rs.getString("R26_LENDING")); obj.setR26_NOMINAL_INTEREST_RATE(rs.getString("R26_NOMINAL_INTEREST_RATE"));
		obj.setR26_AVG_EFFECTIVE_RATE(rs.getString("R26_AVG_EFFECTIVE_RATE")); obj.setR26_VOLUME(rs.getBigDecimal("R26_VOLUME"));
		obj.setR27_LENDING(rs.getString("R27_LENDING")); obj.setR27_NOMINAL_INTEREST_RATE(rs.getString("R27_NOMINAL_INTEREST_RATE"));
		obj.setR27_AVG_EFFECTIVE_RATE(rs.getString("R27_AVG_EFFECTIVE_RATE")); obj.setR27_VOLUME(rs.getBigDecimal("R27_VOLUME"));
		obj.setR28_LENDING(rs.getString("R28_LENDING")); obj.setR28_NOMINAL_INTEREST_RATE(rs.getString("R28_NOMINAL_INTEREST_RATE"));
		obj.setR28_AVG_EFFECTIVE_RATE(rs.getString("R28_AVG_EFFECTIVE_RATE")); obj.setR28_VOLUME(rs.getBigDecimal("R28_VOLUME"));
		obj.setR29_LENDING(rs.getString("R29_LENDING")); obj.setR29_NOMINAL_INTEREST_RATE(rs.getString("R29_NOMINAL_INTEREST_RATE"));
		obj.setR29_AVG_EFFECTIVE_RATE(rs.getString("R29_AVG_EFFECTIVE_RATE")); obj.setR29_VOLUME(rs.getBigDecimal("R29_VOLUME"));
		obj.setR30_LENDING(rs.getString("R30_LENDING")); obj.setR30_NOMINAL_INTEREST_RATE(rs.getString("R30_NOMINAL_INTEREST_RATE"));
		obj.setR30_AVG_EFFECTIVE_RATE(rs.getString("R30_AVG_EFFECTIVE_RATE")); obj.setR30_VOLUME(rs.getBigDecimal("R30_VOLUME"));
		obj.setR31_LENDING(rs.getString("R31_LENDING")); obj.setR31_NOMINAL_INTEREST_RATE(rs.getString("R31_NOMINAL_INTEREST_RATE"));
		obj.setR31_AVG_EFFECTIVE_RATE(rs.getString("R31_AVG_EFFECTIVE_RATE")); obj.setR31_VOLUME(rs.getBigDecimal("R31_VOLUME"));
		obj.setR32_LENDING(rs.getString("R32_LENDING")); obj.setR32_NOMINAL_INTEREST_RATE(rs.getString("R32_NOMINAL_INTEREST_RATE"));
		obj.setR32_AVG_EFFECTIVE_RATE(rs.getString("R32_AVG_EFFECTIVE_RATE")); obj.setR32_VOLUME(rs.getBigDecimal("R32_VOLUME"));
		obj.setR33_LENDING(rs.getString("R33_LENDING")); obj.setR33_NOMINAL_INTEREST_RATE(rs.getString("R33_NOMINAL_INTEREST_RATE"));
		obj.setR33_AVG_EFFECTIVE_RATE(rs.getString("R33_AVG_EFFECTIVE_RATE")); obj.setR33_VOLUME(rs.getBigDecimal("R33_VOLUME"));
		obj.setR34_LENDING(rs.getString("R34_LENDING")); obj.setR34_NOMINAL_INTEREST_RATE(rs.getString("R34_NOMINAL_INTEREST_RATE"));
		obj.setR34_AVG_EFFECTIVE_RATE(rs.getString("R34_AVG_EFFECTIVE_RATE")); obj.setR34_VOLUME(rs.getBigDecimal("R34_VOLUME"));
		obj.setR35_LENDING(rs.getString("R35_LENDING")); obj.setR35_NOMINAL_INTEREST_RATE(rs.getString("R35_NOMINAL_INTEREST_RATE"));
		obj.setR35_AVG_EFFECTIVE_RATE(rs.getString("R35_AVG_EFFECTIVE_RATE")); obj.setR35_VOLUME(rs.getBigDecimal("R35_VOLUME"));
		obj.setR36_LENDING(rs.getString("R36_LENDING")); obj.setR36_NOMINAL_INTEREST_RATE(rs.getString("R36_NOMINAL_INTEREST_RATE"));
		obj.setR36_AVG_EFFECTIVE_RATE(rs.getString("R36_AVG_EFFECTIVE_RATE")); obj.setR36_VOLUME(rs.getBigDecimal("R36_VOLUME"));
		obj.setR37_LENDING(rs.getString("R37_LENDING")); obj.setR37_NOMINAL_INTEREST_RATE(rs.getString("R37_NOMINAL_INTEREST_RATE"));
		obj.setR37_AVG_EFFECTIVE_RATE(rs.getString("R37_AVG_EFFECTIVE_RATE")); obj.setR37_VOLUME(rs.getBigDecimal("R37_VOLUME"));
		obj.setR38_LENDING(rs.getString("R38_LENDING")); obj.setR38_NOMINAL_INTEREST_RATE(rs.getString("R38_NOMINAL_INTEREST_RATE"));
		obj.setR38_AVG_EFFECTIVE_RATE(rs.getString("R38_AVG_EFFECTIVE_RATE")); obj.setR38_VOLUME(rs.getBigDecimal("R38_VOLUME"));
		obj.setR39_LENDING(rs.getString("R39_LENDING")); obj.setR39_NOMINAL_INTEREST_RATE(rs.getString("R39_NOMINAL_INTEREST_RATE"));
		obj.setR39_AVG_EFFECTIVE_RATE(rs.getString("R39_AVG_EFFECTIVE_RATE")); obj.setR39_VOLUME(rs.getBigDecimal("R39_VOLUME"));
		obj.setR40_LENDING(rs.getString("R40_LENDING")); obj.setR40_NOMINAL_INTEREST_RATE(rs.getString("R40_NOMINAL_INTEREST_RATE"));
		obj.setR40_AVG_EFFECTIVE_RATE(rs.getString("R40_AVG_EFFECTIVE_RATE")); obj.setR40_VOLUME(rs.getBigDecimal("R40_VOLUME"));
		obj.setR41_LENDING(rs.getString("R41_LENDING")); obj.setR41_NOMINAL_INTEREST_RATE(rs.getString("R41_NOMINAL_INTEREST_RATE"));
		obj.setR41_AVG_EFFECTIVE_RATE(rs.getString("R41_AVG_EFFECTIVE_RATE")); obj.setR41_VOLUME(rs.getBigDecimal("R41_VOLUME"));
		obj.setR42_LENDING(rs.getString("R42_LENDING")); obj.setR42_NOMINAL_INTEREST_RATE(rs.getString("R42_NOMINAL_INTEREST_RATE"));
		obj.setR42_AVG_EFFECTIVE_RATE(rs.getString("R42_AVG_EFFECTIVE_RATE")); obj.setR42_VOLUME(rs.getBigDecimal("R42_VOLUME"));
		return obj;
	}
}

}
