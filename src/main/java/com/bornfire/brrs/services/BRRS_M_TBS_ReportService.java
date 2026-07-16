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
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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

import com.bornfire.brrs.entities.UserProfileRep;

@Service
@Transactional

public class BRRS_M_TBS_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_TBS_ReportService.class);

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

	public List<M_TBS_Summary_Entity> getSummaryByDate(Date reportDate) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_TBS_SUMMARYTABLE WHERE REPORT_DATE = ?",
			new Object[]{reportDate}, new M_TBSSummaryRowMapper());
	}

	public List<M_TBS_Detail_Entity> getDetailByDate(Date reportDate) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_TBS_DETAILTABLE WHERE REPORT_DATE = ?",
			new Object[]{reportDate}, new M_TBSDetailRowMapper());
	}

	public List<M_TBS_Archival_Summary_Entity> getArchivalSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_TBS_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
			new Object[]{reportDate, version}, new M_TBSArchivalSummaryRowMapper());
	}

	public List<M_TBS_Archival_Summary_Entity> getArchivalSummaryWithVersionAll() {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_TBS_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC",
			new M_TBSArchivalSummaryRowMapper());
	}

	public List<M_TBS_Archival_Detail_Entity> getArchivalDetailByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_TBS_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
			new Object[]{reportDate, version}, new M_TBSArchivalDetailRowMapper());
	}

	public List<M_TBS_Resub_Summary_Entity> getResubSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_TBS_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
			new Object[]{reportDate, version}, new M_TBSResubSummaryRowMapper());
	}

	public List<M_TBS_Resub_Detail_Entity> getResubDetailByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_TBS_RESUB_DETAILTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
			new Object[]{reportDate, version}, new M_TBSResubDetailRowMapper());
	}

	public BigDecimal findMaxResubVersion(Date reportDate) {
		return jdbcTemplate.queryForObject(
			"SELECT MAX(REPORT_VERSION) FROM BRRS_M_TBS_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ?",
			new Object[]{reportDate}, BigDecimal.class);
	}

	// =========================================================
	// JDBC WRITE METHODS
	// =========================================================

	private static final String R_FIELDS_SET =
		"R11_PRODUCT=?,R11_NV_LONG=?,R11_NV_SHORT=?,R11_FV_LONG=?,R11_FV_SHORT=?,R11_QFHA=?," +
		"R12_PRODUCT=?,R12_NV_LONG=?,R12_NV_SHORT=?,R12_FV_LONG=?,R12_FV_SHORT=?,R12_QFHA=?," +
		"R13_PRODUCT=?,R13_NV_LONG=?,R13_NV_SHORT=?,R13_FV_LONG=?,R13_FV_SHORT=?,R13_QFHA=?," +
		"R14_PRODUCT=?,R14_NV_LONG=?,R14_NV_SHORT=?,R14_FV_LONG=?,R14_FV_SHORT=?,R14_QFHA=?," +
		"R15_PRODUCT=?,R15_NV_LONG=?,R15_NV_SHORT=?,R15_FV_LONG=?,R15_FV_SHORT=?,R15_QFHA=?," +
		"R16_PRODUCT=?,R16_NV_LONG=?,R16_NV_SHORT=?,R16_FV_LONG=?,R16_FV_SHORT=?,R16_QFHA=?," +
		"R17_PRODUCT=?,R17_NV_LONG=?,R17_NV_SHORT=?,R17_FV_LONG=?,R17_FV_SHORT=?,R17_QFHA=?," +
		"R18_PRODUCT=?,R18_NV_LONG=?,R18_NV_SHORT=?,R18_FV_LONG=?,R18_FV_SHORT=?,R18_QFHA=?," +
		"R19_PRODUCT=?,R19_NV_LONG=?,R19_NV_SHORT=?,R19_FV_LONG=?,R19_FV_SHORT=?,R19_QFHA=?," +
		"R20_PRODUCT=?,R20_NV_LONG=?,R20_NV_SHORT=?,R20_FV_LONG=?,R20_FV_SHORT=?,R20_QFHA=?," +
		"R21_PRODUCT=?,R21_NV_LONG=?,R21_NV_SHORT=?,R21_FV_LONG=?,R21_FV_SHORT=?,R21_QFHA=?," +
		"R22_PRODUCT=?,R22_NV_LONG=?,R22_NV_SHORT=?,R22_FV_LONG=?,R22_FV_SHORT=?,R22_QFHA=?," +
		"R23_PRODUCT=?,R23_NV_LONG=?,R23_NV_SHORT=?,R23_FV_LONG=?,R23_FV_SHORT=?,R23_QFHA=?," +
		"R24_PRODUCT=?,R24_NV_LONG=?,R24_NV_SHORT=?,R24_FV_LONG=?,R24_FV_SHORT=?,R24_QFHA=?," +
		"R25_PRODUCT=?,R25_NV_LONG=?,R25_NV_SHORT=?,R25_FV_LONG=?,R25_FV_SHORT=?,R25_QFHA=?," +
		"R26_PRODUCT=?,R26_NV_LONG=?,R26_NV_SHORT=?,R26_FV_LONG=?,R26_FV_SHORT=?,R26_QFHA=?," +
		"R27_PRODUCT=?,R27_NV_LONG=?,R27_NV_SHORT=?,R27_FV_LONG=?,R27_FV_SHORT=?,R27_QFHA=?," +
		"R28_PRODUCT=?,R28_NV_LONG=?,R28_NV_SHORT=?,R28_FV_LONG=?,R28_FV_SHORT=?,R28_QFHA=?," +
		"R29_PRODUCT=?,R29_NV_LONG=?,R29_NV_SHORT=?,R29_FV_LONG=?,R29_FV_SHORT=?,R29_QFHA=?," +
		"R30_PRODUCT=?,R30_NV_LONG=?,R30_NV_SHORT=?,R30_FV_LONG=?,R30_FV_SHORT=?,R30_QFHA=?," +
		"R31_PRODUCT=?,R31_NV_LONG=?,R31_NV_SHORT=?,R31_FV_LONG=?,R31_FV_SHORT=?,R31_QFHA=?," +
		"R32_PRODUCT=?,R32_NV_LONG=?,R32_NV_SHORT=?,R32_FV_LONG=?,R32_FV_SHORT=?,R32_QFHA=?," +
		"R33_PRODUCT=?,R33_NV_LONG=?,R33_NV_SHORT=?,R33_FV_LONG=?,R33_FV_SHORT=?,R33_QFHA=?," +
		"R34_PRODUCT=?,R34_NV_LONG=?,R34_NV_SHORT=?,R34_FV_LONG=?,R34_FV_SHORT=?,R34_QFHA=?," +
		"R35_PRODUCT=?,R35_NV_LONG=?,R35_NV_SHORT=?,R35_FV_LONG=?,R35_FV_SHORT=?,R35_QFHA=?," +
		"R36_PRODUCT=?,R36_NV_LONG=?,R36_NV_SHORT=?,R36_FV_LONG=?,R36_FV_SHORT=?,R36_QFHA=?," +
		"R37_PRODUCT=?,R37_NV_LONG=?,R37_NV_SHORT=?,R37_FV_LONG=?,R37_FV_SHORT=?,R37_QFHA=?," +
		"R38_PRODUCT=?,R38_NV_LONG=?,R38_NV_SHORT=?,R38_FV_LONG=?,R38_FV_SHORT=?,R38_QFHA=?," +
		"R39_PRODUCT=?,R39_NV_LONG=?,R39_NV_SHORT=?,R39_FV_LONG=?,R39_FV_SHORT=?,R39_QFHA=?," +
		"R40_PRODUCT=?,R40_NV_LONG=?,R40_NV_SHORT=?,R40_FV_LONG=?,R40_FV_SHORT=?,R40_QFHA=?," +
		"R41_PRODUCT=?,R41_NV_LONG=?,R41_NV_SHORT=?,R41_FV_LONG=?,R41_FV_SHORT=?,R41_QFHA=?," +
		"R42_PRODUCT=?,R42_NV_LONG=?,R42_NV_SHORT=?,R42_FV_LONG=?,R42_FV_SHORT=?,R42_QFHA=?," +
		"R43_PRODUCT=?,R43_NV_LONG=?,R43_NV_SHORT=?,R43_FV_LONG=?,R43_FV_SHORT=?,R43_QFHA=?," +
		"R44_PRODUCT=?,R44_NV_LONG=?,R44_NV_SHORT=?,R44_FV_LONG=?,R44_FV_SHORT=?,R44_QFHA=?," +
		"R45_PRODUCT=?,R45_NV_LONG=?,R45_NV_SHORT=?,R45_FV_LONG=?,R45_FV_SHORT=?,R45_QFHA=?," +
		"R46_PRODUCT=?,R46_NV_LONG=?,R46_NV_SHORT=?,R46_FV_LONG=?,R46_FV_SHORT=?,R46_QFHA=?," +
		"R47_PRODUCT=?,R47_NV_LONG=?,R47_NV_SHORT=?,R47_FV_LONG=?,R47_FV_SHORT=?,R47_QFHA=?," +
		"R48_PRODUCT=?,R48_NV_LONG=?,R48_NV_SHORT=?,R48_FV_LONG=?,R48_FV_SHORT=?,R48_QFHA=?," +
		"R49_PRODUCT=?,R49_NV_LONG=?,R49_NV_SHORT=?,R49_FV_LONG=?,R49_FV_SHORT=?,R49_QFHA=?," +
		"R50_PRODUCT=?,R50_NV_LONG=?,R50_NV_SHORT=?,R50_FV_LONG=?,R50_FV_SHORT=?,R50_QFHA=?," +
		"R51_PRODUCT=?,R51_NV_LONG=?,R51_NV_SHORT=?,R51_FV_LONG=?,R51_FV_SHORT=?,R51_QFHA=?," +
		"R52_PRODUCT=?,R52_NV_LONG=?,R52_NV_SHORT=?,R52_FV_LONG=?,R52_FV_SHORT=?,R52_QFHA=?," +
		"R53_PRODUCT=?,R53_NV_LONG=?,R53_NV_SHORT=?,R53_FV_LONG=?,R53_FV_SHORT=?,R53_QFHA=?," +
		"R54_PRODUCT=?,R54_NV_LONG=?,R54_NV_SHORT=?,R54_FV_LONG=?,R54_FV_SHORT=?,R54_QFHA=?," +
		"R55_PRODUCT=?,R55_NV_LONG=?,R55_NV_SHORT=?,R55_FV_LONG=?,R55_FV_SHORT=?,R55_QFHA=?";

	private static final String R_COLS =
		"R11_PRODUCT,R11_NV_LONG,R11_NV_SHORT,R11_FV_LONG,R11_FV_SHORT,R11_QFHA," +
		"R12_PRODUCT,R12_NV_LONG,R12_NV_SHORT,R12_FV_LONG,R12_FV_SHORT,R12_QFHA," +
		"R13_PRODUCT,R13_NV_LONG,R13_NV_SHORT,R13_FV_LONG,R13_FV_SHORT,R13_QFHA," +
		"R14_PRODUCT,R14_NV_LONG,R14_NV_SHORT,R14_FV_LONG,R14_FV_SHORT,R14_QFHA," +
		"R15_PRODUCT,R15_NV_LONG,R15_NV_SHORT,R15_FV_LONG,R15_FV_SHORT,R15_QFHA," +
		"R16_PRODUCT,R16_NV_LONG,R16_NV_SHORT,R16_FV_LONG,R16_FV_SHORT,R16_QFHA," +
		"R17_PRODUCT,R17_NV_LONG,R17_NV_SHORT,R17_FV_LONG,R17_FV_SHORT,R17_QFHA," +
		"R18_PRODUCT,R18_NV_LONG,R18_NV_SHORT,R18_FV_LONG,R18_FV_SHORT,R18_QFHA," +
		"R19_PRODUCT,R19_NV_LONG,R19_NV_SHORT,R19_FV_LONG,R19_FV_SHORT,R19_QFHA," +
		"R20_PRODUCT,R20_NV_LONG,R20_NV_SHORT,R20_FV_LONG,R20_FV_SHORT,R20_QFHA," +
		"R21_PRODUCT,R21_NV_LONG,R21_NV_SHORT,R21_FV_LONG,R21_FV_SHORT,R21_QFHA," +
		"R22_PRODUCT,R22_NV_LONG,R22_NV_SHORT,R22_FV_LONG,R22_FV_SHORT,R22_QFHA," +
		"R23_PRODUCT,R23_NV_LONG,R23_NV_SHORT,R23_FV_LONG,R23_FV_SHORT,R23_QFHA," +
		"R24_PRODUCT,R24_NV_LONG,R24_NV_SHORT,R24_FV_LONG,R24_FV_SHORT,R24_QFHA," +
		"R25_PRODUCT,R25_NV_LONG,R25_NV_SHORT,R25_FV_LONG,R25_FV_SHORT,R25_QFHA," +
		"R26_PRODUCT,R26_NV_LONG,R26_NV_SHORT,R26_FV_LONG,R26_FV_SHORT,R26_QFHA," +
		"R27_PRODUCT,R27_NV_LONG,R27_NV_SHORT,R27_FV_LONG,R27_FV_SHORT,R27_QFHA," +
		"R28_PRODUCT,R28_NV_LONG,R28_NV_SHORT,R28_FV_LONG,R28_FV_SHORT,R28_QFHA," +
		"R29_PRODUCT,R29_NV_LONG,R29_NV_SHORT,R29_FV_LONG,R29_FV_SHORT,R29_QFHA," +
		"R30_PRODUCT,R30_NV_LONG,R30_NV_SHORT,R30_FV_LONG,R30_FV_SHORT,R30_QFHA," +
		"R31_PRODUCT,R31_NV_LONG,R31_NV_SHORT,R31_FV_LONG,R31_FV_SHORT,R31_QFHA," +
		"R32_PRODUCT,R32_NV_LONG,R32_NV_SHORT,R32_FV_LONG,R32_FV_SHORT,R32_QFHA," +
		"R33_PRODUCT,R33_NV_LONG,R33_NV_SHORT,R33_FV_LONG,R33_FV_SHORT,R33_QFHA," +
		"R34_PRODUCT,R34_NV_LONG,R34_NV_SHORT,R34_FV_LONG,R34_FV_SHORT,R34_QFHA," +
		"R35_PRODUCT,R35_NV_LONG,R35_NV_SHORT,R35_FV_LONG,R35_FV_SHORT,R35_QFHA," +
		"R36_PRODUCT,R36_NV_LONG,R36_NV_SHORT,R36_FV_LONG,R36_FV_SHORT,R36_QFHA," +
		"R37_PRODUCT,R37_NV_LONG,R37_NV_SHORT,R37_FV_LONG,R37_FV_SHORT,R37_QFHA," +
		"R38_PRODUCT,R38_NV_LONG,R38_NV_SHORT,R38_FV_LONG,R38_FV_SHORT,R38_QFHA," +
		"R39_PRODUCT,R39_NV_LONG,R39_NV_SHORT,R39_FV_LONG,R39_FV_SHORT,R39_QFHA," +
		"R40_PRODUCT,R40_NV_LONG,R40_NV_SHORT,R40_FV_LONG,R40_FV_SHORT,R40_QFHA," +
		"R41_PRODUCT,R41_NV_LONG,R41_NV_SHORT,R41_FV_LONG,R41_FV_SHORT,R41_QFHA," +
		"R42_PRODUCT,R42_NV_LONG,R42_NV_SHORT,R42_FV_LONG,R42_FV_SHORT,R42_QFHA," +
		"R43_PRODUCT,R43_NV_LONG,R43_NV_SHORT,R43_FV_LONG,R43_FV_SHORT,R43_QFHA," +
		"R44_PRODUCT,R44_NV_LONG,R44_NV_SHORT,R44_FV_LONG,R44_FV_SHORT,R44_QFHA," +
		"R45_PRODUCT,R45_NV_LONG,R45_NV_SHORT,R45_FV_LONG,R45_FV_SHORT,R45_QFHA," +
		"R46_PRODUCT,R46_NV_LONG,R46_NV_SHORT,R46_FV_LONG,R46_FV_SHORT,R46_QFHA," +
		"R47_PRODUCT,R47_NV_LONG,R47_NV_SHORT,R47_FV_LONG,R47_FV_SHORT,R47_QFHA," +
		"R48_PRODUCT,R48_NV_LONG,R48_NV_SHORT,R48_FV_LONG,R48_FV_SHORT,R48_QFHA," +
		"R49_PRODUCT,R49_NV_LONG,R49_NV_SHORT,R49_FV_LONG,R49_FV_SHORT,R49_QFHA," +
		"R50_PRODUCT,R50_NV_LONG,R50_NV_SHORT,R50_FV_LONG,R50_FV_SHORT,R50_QFHA," +
		"R51_PRODUCT,R51_NV_LONG,R51_NV_SHORT,R51_FV_LONG,R51_FV_SHORT,R51_QFHA," +
		"R52_PRODUCT,R52_NV_LONG,R52_NV_SHORT,R52_FV_LONG,R52_FV_SHORT,R52_QFHA," +
		"R53_PRODUCT,R53_NV_LONG,R53_NV_SHORT,R53_FV_LONG,R53_FV_SHORT,R53_QFHA," +
		"R54_PRODUCT,R54_NV_LONG,R54_NV_SHORT,R54_FV_LONG,R54_FV_SHORT,R54_QFHA," +
		"R55_PRODUCT,R55_NV_LONG,R55_NV_SHORT,R55_FV_LONG,R55_FV_SHORT,R55_QFHA";

	private static final String R_PLACEHOLDERS =
		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +  // R11-R16 (36)
		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +  // R17-R22 (36)
		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +  // R23-R28 (36)
		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +  // R29-R34 (36)
		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +  // R35-R40 (36)
		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +  // R41-R46 (36)
		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +  // R47-R52 (36)
		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";                                          // R47-R52 (18 items = 3 rows × 6)

	private Object[] rFieldValues(M_TBS_Summary_Entity e) {
		return new Object[]{
			e.getR11_PRODUCT(),e.getR11_NV_LONG(),e.getR11_NV_SHORT(),e.getR11_FV_LONG(),e.getR11_FV_SHORT(),e.getR11_QFHA(),
			e.getR12_PRODUCT(),e.getR12_NV_LONG(),e.getR12_NV_SHORT(),e.getR12_FV_LONG(),e.getR12_FV_SHORT(),e.getR12_QFHA(),
			e.getR13_PRODUCT(),e.getR13_NV_LONG(),e.getR13_NV_SHORT(),e.getR13_FV_LONG(),e.getR13_FV_SHORT(),e.getR13_QFHA(),
			e.getR14_PRODUCT(),e.getR14_NV_LONG(),e.getR14_NV_SHORT(),e.getR14_FV_LONG(),e.getR14_FV_SHORT(),e.getR14_QFHA(),
			e.getR15_PRODUCT(),e.getR15_NV_LONG(),e.getR15_NV_SHORT(),e.getR15_FV_LONG(),e.getR15_FV_SHORT(),e.getR15_QFHA(),
			e.getR16_PRODUCT(),e.getR16_NV_LONG(),e.getR16_NV_SHORT(),e.getR16_FV_LONG(),e.getR16_FV_SHORT(),e.getR16_QFHA(),
			e.getR17_PRODUCT(),e.getR17_NV_LONG(),e.getR17_NV_SHORT(),e.getR17_FV_LONG(),e.getR17_FV_SHORT(),e.getR17_QFHA(),
			e.getR18_PRODUCT(),e.getR18_NV_LONG(),e.getR18_NV_SHORT(),e.getR18_FV_LONG(),e.getR18_FV_SHORT(),e.getR18_QFHA(),
			e.getR19_PRODUCT(),e.getR19_NV_LONG(),e.getR19_NV_SHORT(),e.getR19_FV_LONG(),e.getR19_FV_SHORT(),e.getR19_QFHA(),
			e.getR20_PRODUCT(),e.getR20_NV_LONG(),e.getR20_NV_SHORT(),e.getR20_FV_LONG(),e.getR20_FV_SHORT(),e.getR20_QFHA(),
			e.getR21_PRODUCT(),e.getR21_NV_LONG(),e.getR21_NV_SHORT(),e.getR21_FV_LONG(),e.getR21_FV_SHORT(),e.getR21_QFHA(),
			e.getR22_PRODUCT(),e.getR22_NV_LONG(),e.getR22_NV_SHORT(),e.getR22_FV_LONG(),e.getR22_FV_SHORT(),e.getR22_QFHA(),
			e.getR23_PRODUCT(),e.getR23_NV_LONG(),e.getR23_NV_SHORT(),e.getR23_FV_LONG(),e.getR23_FV_SHORT(),e.getR23_QFHA(),
			e.getR24_PRODUCT(),e.getR24_NV_LONG(),e.getR24_NV_SHORT(),e.getR24_FV_LONG(),e.getR24_FV_SHORT(),e.getR24_QFHA(),
			e.getR25_PRODUCT(),e.getR25_NV_LONG(),e.getR25_NV_SHORT(),e.getR25_FV_LONG(),e.getR25_FV_SHORT(),e.getR25_QFHA(),
			e.getR26_PRODUCT(),e.getR26_NV_LONG(),e.getR26_NV_SHORT(),e.getR26_FV_LONG(),e.getR26_FV_SHORT(),e.getR26_QFHA(),
			e.getR27_PRODUCT(),e.getR27_NV_LONG(),e.getR27_NV_SHORT(),e.getR27_FV_LONG(),e.getR27_FV_SHORT(),e.getR27_QFHA(),
			e.getR28_PRODUCT(),e.getR28_NV_LONG(),e.getR28_NV_SHORT(),e.getR28_FV_LONG(),e.getR28_FV_SHORT(),e.getR28_QFHA(),
			e.getR29_PRODUCT(),e.getR29_NV_LONG(),e.getR29_NV_SHORT(),e.getR29_FV_LONG(),e.getR29_FV_SHORT(),e.getR29_QFHA(),
			e.getR30_PRODUCT(),e.getR30_NV_LONG(),e.getR30_NV_SHORT(),e.getR30_FV_LONG(),e.getR30_FV_SHORT(),e.getR30_QFHA(),
			e.getR31_PRODUCT(),e.getR31_NV_LONG(),e.getR31_NV_SHORT(),e.getR31_FV_LONG(),e.getR31_FV_SHORT(),e.getR31_QFHA(),
			e.getR32_PRODUCT(),e.getR32_NV_LONG(),e.getR32_NV_SHORT(),e.getR32_FV_LONG(),e.getR32_FV_SHORT(),e.getR32_QFHA(),
			e.getR33_PRODUCT(),e.getR33_NV_LONG(),e.getR33_NV_SHORT(),e.getR33_FV_LONG(),e.getR33_FV_SHORT(),e.getR33_QFHA(),
			e.getR34_PRODUCT(),e.getR34_NV_LONG(),e.getR34_NV_SHORT(),e.getR34_FV_LONG(),e.getR34_FV_SHORT(),e.getR34_QFHA(),
			e.getR35_PRODUCT(),e.getR35_NV_LONG(),e.getR35_NV_SHORT(),e.getR35_FV_LONG(),e.getR35_FV_SHORT(),e.getR35_QFHA(),
			e.getR36_PRODUCT(),e.getR36_NV_LONG(),e.getR36_NV_SHORT(),e.getR36_FV_LONG(),e.getR36_FV_SHORT(),e.getR36_QFHA(),
			e.getR37_PRODUCT(),e.getR37_NV_LONG(),e.getR37_NV_SHORT(),e.getR37_FV_LONG(),e.getR37_FV_SHORT(),e.getR37_QFHA(),
			e.getR38_PRODUCT(),e.getR38_NV_LONG(),e.getR38_NV_SHORT(),e.getR38_FV_LONG(),e.getR38_FV_SHORT(),e.getR38_QFHA(),
			e.getR39_PRODUCT(),e.getR39_NV_LONG(),e.getR39_NV_SHORT(),e.getR39_FV_LONG(),e.getR39_FV_SHORT(),e.getR39_QFHA(),
			e.getR40_PRODUCT(),e.getR40_NV_LONG(),e.getR40_NV_SHORT(),e.getR40_FV_LONG(),e.getR40_FV_SHORT(),e.getR40_QFHA(),
			e.getR41_PRODUCT(),e.getR41_NV_LONG(),e.getR41_NV_SHORT(),e.getR41_FV_LONG(),e.getR41_FV_SHORT(),e.getR41_QFHA(),
			e.getR42_PRODUCT(),e.getR42_NV_LONG(),e.getR42_NV_SHORT(),e.getR42_FV_LONG(),e.getR42_FV_SHORT(),e.getR42_QFHA(),
			e.getR43_PRODUCT(),e.getR43_NV_LONG(),e.getR43_NV_SHORT(),e.getR43_FV_LONG(),e.getR43_FV_SHORT(),e.getR43_QFHA(),
			e.getR44_PRODUCT(),e.getR44_NV_LONG(),e.getR44_NV_SHORT(),e.getR44_FV_LONG(),e.getR44_FV_SHORT(),e.getR44_QFHA(),
			e.getR45_PRODUCT(),e.getR45_NV_LONG(),e.getR45_NV_SHORT(),e.getR45_FV_LONG(),e.getR45_FV_SHORT(),e.getR45_QFHA(),
			e.getR46_PRODUCT(),e.getR46_NV_LONG(),e.getR46_NV_SHORT(),e.getR46_FV_LONG(),e.getR46_FV_SHORT(),e.getR46_QFHA(),
			e.getR47_PRODUCT(),e.getR47_NV_LONG(),e.getR47_NV_SHORT(),e.getR47_FV_LONG(),e.getR47_FV_SHORT(),e.getR47_QFHA(),
			e.getR48_PRODUCT(),e.getR48_NV_LONG(),e.getR48_NV_SHORT(),e.getR48_FV_LONG(),e.getR48_FV_SHORT(),e.getR48_QFHA(),
			e.getR49_PRODUCT(),e.getR49_NV_LONG(),e.getR49_NV_SHORT(),e.getR49_FV_LONG(),e.getR49_FV_SHORT(),e.getR49_QFHA(),
			e.getR50_PRODUCT(),e.getR50_NV_LONG(),e.getR50_NV_SHORT(),e.getR50_FV_LONG(),e.getR50_FV_SHORT(),e.getR50_QFHA(),
			e.getR51_PRODUCT(),e.getR51_NV_LONG(),e.getR51_NV_SHORT(),e.getR51_FV_LONG(),e.getR51_FV_SHORT(),e.getR51_QFHA(),
			e.getR52_PRODUCT(),e.getR52_NV_LONG(),e.getR52_NV_SHORT(),e.getR52_FV_LONG(),e.getR52_FV_SHORT(),e.getR52_QFHA(),
			e.getR53_PRODUCT(),e.getR53_NV_LONG(),e.getR53_NV_SHORT(),e.getR53_FV_LONG(),e.getR53_FV_SHORT(),e.getR53_QFHA(),
			e.getR54_PRODUCT(),e.getR54_NV_LONG(),e.getR54_NV_SHORT(),e.getR54_FV_LONG(),e.getR54_FV_SHORT(),e.getR54_QFHA(),
			e.getR55_PRODUCT(),e.getR55_NV_LONG(),e.getR55_NV_SHORT(),e.getR55_FV_LONG(),e.getR55_FV_SHORT(),e.getR55_QFHA()
		};
	}

	private Object[] rFieldValues(M_TBS_Detail_Entity e) {
		return new Object[]{
			e.getR11_PRODUCT(),e.getR11_NV_LONG(),e.getR11_NV_SHORT(),e.getR11_FV_LONG(),e.getR11_FV_SHORT(),e.getR11_QFHA(),
			e.getR12_PRODUCT(),e.getR12_NV_LONG(),e.getR12_NV_SHORT(),e.getR12_FV_LONG(),e.getR12_FV_SHORT(),e.getR12_QFHA(),
			e.getR13_PRODUCT(),e.getR13_NV_LONG(),e.getR13_NV_SHORT(),e.getR13_FV_LONG(),e.getR13_FV_SHORT(),e.getR13_QFHA(),
			e.getR14_PRODUCT(),e.getR14_NV_LONG(),e.getR14_NV_SHORT(),e.getR14_FV_LONG(),e.getR14_FV_SHORT(),e.getR14_QFHA(),
			e.getR15_PRODUCT(),e.getR15_NV_LONG(),e.getR15_NV_SHORT(),e.getR15_FV_LONG(),e.getR15_FV_SHORT(),e.getR15_QFHA(),
			e.getR16_PRODUCT(),e.getR16_NV_LONG(),e.getR16_NV_SHORT(),e.getR16_FV_LONG(),e.getR16_FV_SHORT(),e.getR16_QFHA(),
			e.getR17_PRODUCT(),e.getR17_NV_LONG(),e.getR17_NV_SHORT(),e.getR17_FV_LONG(),e.getR17_FV_SHORT(),e.getR17_QFHA(),
			e.getR18_PRODUCT(),e.getR18_NV_LONG(),e.getR18_NV_SHORT(),e.getR18_FV_LONG(),e.getR18_FV_SHORT(),e.getR18_QFHA(),
			e.getR19_PRODUCT(),e.getR19_NV_LONG(),e.getR19_NV_SHORT(),e.getR19_FV_LONG(),e.getR19_FV_SHORT(),e.getR19_QFHA(),
			e.getR20_PRODUCT(),e.getR20_NV_LONG(),e.getR20_NV_SHORT(),e.getR20_FV_LONG(),e.getR20_FV_SHORT(),e.getR20_QFHA(),
			e.getR21_PRODUCT(),e.getR21_NV_LONG(),e.getR21_NV_SHORT(),e.getR21_FV_LONG(),e.getR21_FV_SHORT(),e.getR21_QFHA(),
			e.getR22_PRODUCT(),e.getR22_NV_LONG(),e.getR22_NV_SHORT(),e.getR22_FV_LONG(),e.getR22_FV_SHORT(),e.getR22_QFHA(),
			e.getR23_PRODUCT(),e.getR23_NV_LONG(),e.getR23_NV_SHORT(),e.getR23_FV_LONG(),e.getR23_FV_SHORT(),e.getR23_QFHA(),
			e.getR24_PRODUCT(),e.getR24_NV_LONG(),e.getR24_NV_SHORT(),e.getR24_FV_LONG(),e.getR24_FV_SHORT(),e.getR24_QFHA(),
			e.getR25_PRODUCT(),e.getR25_NV_LONG(),e.getR25_NV_SHORT(),e.getR25_FV_LONG(),e.getR25_FV_SHORT(),e.getR25_QFHA(),
			e.getR26_PRODUCT(),e.getR26_NV_LONG(),e.getR26_NV_SHORT(),e.getR26_FV_LONG(),e.getR26_FV_SHORT(),e.getR26_QFHA(),
			e.getR27_PRODUCT(),e.getR27_NV_LONG(),e.getR27_NV_SHORT(),e.getR27_FV_LONG(),e.getR27_FV_SHORT(),e.getR27_QFHA(),
			e.getR28_PRODUCT(),e.getR28_NV_LONG(),e.getR28_NV_SHORT(),e.getR28_FV_LONG(),e.getR28_FV_SHORT(),e.getR28_QFHA(),
			e.getR29_PRODUCT(),e.getR29_NV_LONG(),e.getR29_NV_SHORT(),e.getR29_FV_LONG(),e.getR29_FV_SHORT(),e.getR29_QFHA(),
			e.getR30_PRODUCT(),e.getR30_NV_LONG(),e.getR30_NV_SHORT(),e.getR30_FV_LONG(),e.getR30_FV_SHORT(),e.getR30_QFHA(),
			e.getR31_PRODUCT(),e.getR31_NV_LONG(),e.getR31_NV_SHORT(),e.getR31_FV_LONG(),e.getR31_FV_SHORT(),e.getR31_QFHA(),
			e.getR32_PRODUCT(),e.getR32_NV_LONG(),e.getR32_NV_SHORT(),e.getR32_FV_LONG(),e.getR32_FV_SHORT(),e.getR32_QFHA(),
			e.getR33_PRODUCT(),e.getR33_NV_LONG(),e.getR33_NV_SHORT(),e.getR33_FV_LONG(),e.getR33_FV_SHORT(),e.getR33_QFHA(),
			e.getR34_PRODUCT(),e.getR34_NV_LONG(),e.getR34_NV_SHORT(),e.getR34_FV_LONG(),e.getR34_FV_SHORT(),e.getR34_QFHA(),
			e.getR35_PRODUCT(),e.getR35_NV_LONG(),e.getR35_NV_SHORT(),e.getR35_FV_LONG(),e.getR35_FV_SHORT(),e.getR35_QFHA(),
			e.getR36_PRODUCT(),e.getR36_NV_LONG(),e.getR36_NV_SHORT(),e.getR36_FV_LONG(),e.getR36_FV_SHORT(),e.getR36_QFHA(),
			e.getR37_PRODUCT(),e.getR37_NV_LONG(),e.getR37_NV_SHORT(),e.getR37_FV_LONG(),e.getR37_FV_SHORT(),e.getR37_QFHA(),
			e.getR38_PRODUCT(),e.getR38_NV_LONG(),e.getR38_NV_SHORT(),e.getR38_FV_LONG(),e.getR38_FV_SHORT(),e.getR38_QFHA(),
			e.getR39_PRODUCT(),e.getR39_NV_LONG(),e.getR39_NV_SHORT(),e.getR39_FV_LONG(),e.getR39_FV_SHORT(),e.getR39_QFHA(),
			e.getR40_PRODUCT(),e.getR40_NV_LONG(),e.getR40_NV_SHORT(),e.getR40_FV_LONG(),e.getR40_FV_SHORT(),e.getR40_QFHA(),
			e.getR41_PRODUCT(),e.getR41_NV_LONG(),e.getR41_NV_SHORT(),e.getR41_FV_LONG(),e.getR41_FV_SHORT(),e.getR41_QFHA(),
			e.getR42_PRODUCT(),e.getR42_NV_LONG(),e.getR42_NV_SHORT(),e.getR42_FV_LONG(),e.getR42_FV_SHORT(),e.getR42_QFHA(),
			e.getR43_PRODUCT(),e.getR43_NV_LONG(),e.getR43_NV_SHORT(),e.getR43_FV_LONG(),e.getR43_FV_SHORT(),e.getR43_QFHA(),
			e.getR44_PRODUCT(),e.getR44_NV_LONG(),e.getR44_NV_SHORT(),e.getR44_FV_LONG(),e.getR44_FV_SHORT(),e.getR44_QFHA(),
			e.getR45_PRODUCT(),e.getR45_NV_LONG(),e.getR45_NV_SHORT(),e.getR45_FV_LONG(),e.getR45_FV_SHORT(),e.getR45_QFHA(),
			e.getR46_PRODUCT(),e.getR46_NV_LONG(),e.getR46_NV_SHORT(),e.getR46_FV_LONG(),e.getR46_FV_SHORT(),e.getR46_QFHA(),
			e.getR47_PRODUCT(),e.getR47_NV_LONG(),e.getR47_NV_SHORT(),e.getR47_FV_LONG(),e.getR47_FV_SHORT(),e.getR47_QFHA(),
			e.getR48_PRODUCT(),e.getR48_NV_LONG(),e.getR48_NV_SHORT(),e.getR48_FV_LONG(),e.getR48_FV_SHORT(),e.getR48_QFHA(),
			e.getR49_PRODUCT(),e.getR49_NV_LONG(),e.getR49_NV_SHORT(),e.getR49_FV_LONG(),e.getR49_FV_SHORT(),e.getR49_QFHA(),
			e.getR50_PRODUCT(),e.getR50_NV_LONG(),e.getR50_NV_SHORT(),e.getR50_FV_LONG(),e.getR50_FV_SHORT(),e.getR50_QFHA(),
			e.getR51_PRODUCT(),e.getR51_NV_LONG(),e.getR51_NV_SHORT(),e.getR51_FV_LONG(),e.getR51_FV_SHORT(),e.getR51_QFHA(),
			e.getR52_PRODUCT(),e.getR52_NV_LONG(),e.getR52_NV_SHORT(),e.getR52_FV_LONG(),e.getR52_FV_SHORT(),e.getR52_QFHA(),
			e.getR53_PRODUCT(),e.getR53_NV_LONG(),e.getR53_NV_SHORT(),e.getR53_FV_LONG(),e.getR53_FV_SHORT(),e.getR53_QFHA(),
			e.getR54_PRODUCT(),e.getR54_NV_LONG(),e.getR54_NV_SHORT(),e.getR54_FV_LONG(),e.getR54_FV_SHORT(),e.getR54_QFHA(),
			e.getR55_PRODUCT(),e.getR55_NV_LONG(),e.getR55_NV_SHORT(),e.getR55_FV_LONG(),e.getR55_FV_SHORT(),e.getR55_QFHA()
		};
	}

	private Object[] rFieldValues(M_TBS_Archival_Summary_Entity e) {
		return new Object[]{
			e.getR11_PRODUCT(),e.getR11_NV_LONG(),e.getR11_NV_SHORT(),e.getR11_FV_LONG(),e.getR11_FV_SHORT(),e.getR11_QFHA(),
			e.getR12_PRODUCT(),e.getR12_NV_LONG(),e.getR12_NV_SHORT(),e.getR12_FV_LONG(),e.getR12_FV_SHORT(),e.getR12_QFHA(),
			e.getR13_PRODUCT(),e.getR13_NV_LONG(),e.getR13_NV_SHORT(),e.getR13_FV_LONG(),e.getR13_FV_SHORT(),e.getR13_QFHA(),
			e.getR14_PRODUCT(),e.getR14_NV_LONG(),e.getR14_NV_SHORT(),e.getR14_FV_LONG(),e.getR14_FV_SHORT(),e.getR14_QFHA(),
			e.getR15_PRODUCT(),e.getR15_NV_LONG(),e.getR15_NV_SHORT(),e.getR15_FV_LONG(),e.getR15_FV_SHORT(),e.getR15_QFHA(),
			e.getR16_PRODUCT(),e.getR16_NV_LONG(),e.getR16_NV_SHORT(),e.getR16_FV_LONG(),e.getR16_FV_SHORT(),e.getR16_QFHA(),
			e.getR17_PRODUCT(),e.getR17_NV_LONG(),e.getR17_NV_SHORT(),e.getR17_FV_LONG(),e.getR17_FV_SHORT(),e.getR17_QFHA(),
			e.getR18_PRODUCT(),e.getR18_NV_LONG(),e.getR18_NV_SHORT(),e.getR18_FV_LONG(),e.getR18_FV_SHORT(),e.getR18_QFHA(),
			e.getR19_PRODUCT(),e.getR19_NV_LONG(),e.getR19_NV_SHORT(),e.getR19_FV_LONG(),e.getR19_FV_SHORT(),e.getR19_QFHA(),
			e.getR20_PRODUCT(),e.getR20_NV_LONG(),e.getR20_NV_SHORT(),e.getR20_FV_LONG(),e.getR20_FV_SHORT(),e.getR20_QFHA(),
			e.getR21_PRODUCT(),e.getR21_NV_LONG(),e.getR21_NV_SHORT(),e.getR21_FV_LONG(),e.getR21_FV_SHORT(),e.getR21_QFHA(),
			e.getR22_PRODUCT(),e.getR22_NV_LONG(),e.getR22_NV_SHORT(),e.getR22_FV_LONG(),e.getR22_FV_SHORT(),e.getR22_QFHA(),
			e.getR23_PRODUCT(),e.getR23_NV_LONG(),e.getR23_NV_SHORT(),e.getR23_FV_LONG(),e.getR23_FV_SHORT(),e.getR23_QFHA(),
			e.getR24_PRODUCT(),e.getR24_NV_LONG(),e.getR24_NV_SHORT(),e.getR24_FV_LONG(),e.getR24_FV_SHORT(),e.getR24_QFHA(),
			e.getR25_PRODUCT(),e.getR25_NV_LONG(),e.getR25_NV_SHORT(),e.getR25_FV_LONG(),e.getR25_FV_SHORT(),e.getR25_QFHA(),
			e.getR26_PRODUCT(),e.getR26_NV_LONG(),e.getR26_NV_SHORT(),e.getR26_FV_LONG(),e.getR26_FV_SHORT(),e.getR26_QFHA(),
			e.getR27_PRODUCT(),e.getR27_NV_LONG(),e.getR27_NV_SHORT(),e.getR27_FV_LONG(),e.getR27_FV_SHORT(),e.getR27_QFHA(),
			e.getR28_PRODUCT(),e.getR28_NV_LONG(),e.getR28_NV_SHORT(),e.getR28_FV_LONG(),e.getR28_FV_SHORT(),e.getR28_QFHA(),
			e.getR29_PRODUCT(),e.getR29_NV_LONG(),e.getR29_NV_SHORT(),e.getR29_FV_LONG(),e.getR29_FV_SHORT(),e.getR29_QFHA(),
			e.getR30_PRODUCT(),e.getR30_NV_LONG(),e.getR30_NV_SHORT(),e.getR30_FV_LONG(),e.getR30_FV_SHORT(),e.getR30_QFHA(),
			e.getR31_PRODUCT(),e.getR31_NV_LONG(),e.getR31_NV_SHORT(),e.getR31_FV_LONG(),e.getR31_FV_SHORT(),e.getR31_QFHA(),
			e.getR32_PRODUCT(),e.getR32_NV_LONG(),e.getR32_NV_SHORT(),e.getR32_FV_LONG(),e.getR32_FV_SHORT(),e.getR32_QFHA(),
			e.getR33_PRODUCT(),e.getR33_NV_LONG(),e.getR33_NV_SHORT(),e.getR33_FV_LONG(),e.getR33_FV_SHORT(),e.getR33_QFHA(),
			e.getR34_PRODUCT(),e.getR34_NV_LONG(),e.getR34_NV_SHORT(),e.getR34_FV_LONG(),e.getR34_FV_SHORT(),e.getR34_QFHA(),
			e.getR35_PRODUCT(),e.getR35_NV_LONG(),e.getR35_NV_SHORT(),e.getR35_FV_LONG(),e.getR35_FV_SHORT(),e.getR35_QFHA(),
			e.getR36_PRODUCT(),e.getR36_NV_LONG(),e.getR36_NV_SHORT(),e.getR36_FV_LONG(),e.getR36_FV_SHORT(),e.getR36_QFHA(),
			e.getR37_PRODUCT(),e.getR37_NV_LONG(),e.getR37_NV_SHORT(),e.getR37_FV_LONG(),e.getR37_FV_SHORT(),e.getR37_QFHA(),
			e.getR38_PRODUCT(),e.getR38_NV_LONG(),e.getR38_NV_SHORT(),e.getR38_FV_LONG(),e.getR38_FV_SHORT(),e.getR38_QFHA(),
			e.getR39_PRODUCT(),e.getR39_NV_LONG(),e.getR39_NV_SHORT(),e.getR39_FV_LONG(),e.getR39_FV_SHORT(),e.getR39_QFHA(),
			e.getR40_PRODUCT(),e.getR40_NV_LONG(),e.getR40_NV_SHORT(),e.getR40_FV_LONG(),e.getR40_FV_SHORT(),e.getR40_QFHA(),
			e.getR41_PRODUCT(),e.getR41_NV_LONG(),e.getR41_NV_SHORT(),e.getR41_FV_LONG(),e.getR41_FV_SHORT(),e.getR41_QFHA(),
			e.getR42_PRODUCT(),e.getR42_NV_LONG(),e.getR42_NV_SHORT(),e.getR42_FV_LONG(),e.getR42_FV_SHORT(),e.getR42_QFHA(),
			e.getR43_PRODUCT(),e.getR43_NV_LONG(),e.getR43_NV_SHORT(),e.getR43_FV_LONG(),e.getR43_FV_SHORT(),e.getR43_QFHA(),
			e.getR44_PRODUCT(),e.getR44_NV_LONG(),e.getR44_NV_SHORT(),e.getR44_FV_LONG(),e.getR44_FV_SHORT(),e.getR44_QFHA(),
			e.getR45_PRODUCT(),e.getR45_NV_LONG(),e.getR45_NV_SHORT(),e.getR45_FV_LONG(),e.getR45_FV_SHORT(),e.getR45_QFHA(),
			e.getR46_PRODUCT(),e.getR46_NV_LONG(),e.getR46_NV_SHORT(),e.getR46_FV_LONG(),e.getR46_FV_SHORT(),e.getR46_QFHA(),
			e.getR47_PRODUCT(),e.getR47_NV_LONG(),e.getR47_NV_SHORT(),e.getR47_FV_LONG(),e.getR47_FV_SHORT(),e.getR47_QFHA(),
			e.getR48_PRODUCT(),e.getR48_NV_LONG(),e.getR48_NV_SHORT(),e.getR48_FV_LONG(),e.getR48_FV_SHORT(),e.getR48_QFHA(),
			e.getR49_PRODUCT(),e.getR49_NV_LONG(),e.getR49_NV_SHORT(),e.getR49_FV_LONG(),e.getR49_FV_SHORT(),e.getR49_QFHA(),
			e.getR50_PRODUCT(),e.getR50_NV_LONG(),e.getR50_NV_SHORT(),e.getR50_FV_LONG(),e.getR50_FV_SHORT(),e.getR50_QFHA(),
			e.getR51_PRODUCT(),e.getR51_NV_LONG(),e.getR51_NV_SHORT(),e.getR51_FV_LONG(),e.getR51_FV_SHORT(),e.getR51_QFHA(),
			e.getR52_PRODUCT(),e.getR52_NV_LONG(),e.getR52_NV_SHORT(),e.getR52_FV_LONG(),e.getR52_FV_SHORT(),e.getR52_QFHA(),
			e.getR53_PRODUCT(),e.getR53_NV_LONG(),e.getR53_NV_SHORT(),e.getR53_FV_LONG(),e.getR53_FV_SHORT(),e.getR53_QFHA(),
			e.getR54_PRODUCT(),e.getR54_NV_LONG(),e.getR54_NV_SHORT(),e.getR54_FV_LONG(),e.getR54_FV_SHORT(),e.getR54_QFHA(),
			e.getR55_PRODUCT(),e.getR55_NV_LONG(),e.getR55_NV_SHORT(),e.getR55_FV_LONG(),e.getR55_FV_SHORT(),e.getR55_QFHA()
		};
	}

	private Object[] rFieldValues(M_TBS_Archival_Detail_Entity e) {
		return new Object[]{
			e.getR11_PRODUCT(),e.getR11_NV_LONG(),e.getR11_NV_SHORT(),e.getR11_FV_LONG(),e.getR11_FV_SHORT(),e.getR11_QFHA(),
			e.getR12_PRODUCT(),e.getR12_NV_LONG(),e.getR12_NV_SHORT(),e.getR12_FV_LONG(),e.getR12_FV_SHORT(),e.getR12_QFHA(),
			e.getR13_PRODUCT(),e.getR13_NV_LONG(),e.getR13_NV_SHORT(),e.getR13_FV_LONG(),e.getR13_FV_SHORT(),e.getR13_QFHA(),
			e.getR14_PRODUCT(),e.getR14_NV_LONG(),e.getR14_NV_SHORT(),e.getR14_FV_LONG(),e.getR14_FV_SHORT(),e.getR14_QFHA(),
			e.getR15_PRODUCT(),e.getR15_NV_LONG(),e.getR15_NV_SHORT(),e.getR15_FV_LONG(),e.getR15_FV_SHORT(),e.getR15_QFHA(),
			e.getR16_PRODUCT(),e.getR16_NV_LONG(),e.getR16_NV_SHORT(),e.getR16_FV_LONG(),e.getR16_FV_SHORT(),e.getR16_QFHA(),
			e.getR17_PRODUCT(),e.getR17_NV_LONG(),e.getR17_NV_SHORT(),e.getR17_FV_LONG(),e.getR17_FV_SHORT(),e.getR17_QFHA(),
			e.getR18_PRODUCT(),e.getR18_NV_LONG(),e.getR18_NV_SHORT(),e.getR18_FV_LONG(),e.getR18_FV_SHORT(),e.getR18_QFHA(),
			e.getR19_PRODUCT(),e.getR19_NV_LONG(),e.getR19_NV_SHORT(),e.getR19_FV_LONG(),e.getR19_FV_SHORT(),e.getR19_QFHA(),
			e.getR20_PRODUCT(),e.getR20_NV_LONG(),e.getR20_NV_SHORT(),e.getR20_FV_LONG(),e.getR20_FV_SHORT(),e.getR20_QFHA(),
			e.getR21_PRODUCT(),e.getR21_NV_LONG(),e.getR21_NV_SHORT(),e.getR21_FV_LONG(),e.getR21_FV_SHORT(),e.getR21_QFHA(),
			e.getR22_PRODUCT(),e.getR22_NV_LONG(),e.getR22_NV_SHORT(),e.getR22_FV_LONG(),e.getR22_FV_SHORT(),e.getR22_QFHA(),
			e.getR23_PRODUCT(),e.getR23_NV_LONG(),e.getR23_NV_SHORT(),e.getR23_FV_LONG(),e.getR23_FV_SHORT(),e.getR23_QFHA(),
			e.getR24_PRODUCT(),e.getR24_NV_LONG(),e.getR24_NV_SHORT(),e.getR24_FV_LONG(),e.getR24_FV_SHORT(),e.getR24_QFHA(),
			e.getR25_PRODUCT(),e.getR25_NV_LONG(),e.getR25_NV_SHORT(),e.getR25_FV_LONG(),e.getR25_FV_SHORT(),e.getR25_QFHA(),
			e.getR26_PRODUCT(),e.getR26_NV_LONG(),e.getR26_NV_SHORT(),e.getR26_FV_LONG(),e.getR26_FV_SHORT(),e.getR26_QFHA(),
			e.getR27_PRODUCT(),e.getR27_NV_LONG(),e.getR27_NV_SHORT(),e.getR27_FV_LONG(),e.getR27_FV_SHORT(),e.getR27_QFHA(),
			e.getR28_PRODUCT(),e.getR28_NV_LONG(),e.getR28_NV_SHORT(),e.getR28_FV_LONG(),e.getR28_FV_SHORT(),e.getR28_QFHA(),
			e.getR29_PRODUCT(),e.getR29_NV_LONG(),e.getR29_NV_SHORT(),e.getR29_FV_LONG(),e.getR29_FV_SHORT(),e.getR29_QFHA(),
			e.getR30_PRODUCT(),e.getR30_NV_LONG(),e.getR30_NV_SHORT(),e.getR30_FV_LONG(),e.getR30_FV_SHORT(),e.getR30_QFHA(),
			e.getR31_PRODUCT(),e.getR31_NV_LONG(),e.getR31_NV_SHORT(),e.getR31_FV_LONG(),e.getR31_FV_SHORT(),e.getR31_QFHA(),
			e.getR32_PRODUCT(),e.getR32_NV_LONG(),e.getR32_NV_SHORT(),e.getR32_FV_LONG(),e.getR32_FV_SHORT(),e.getR32_QFHA(),
			e.getR33_PRODUCT(),e.getR33_NV_LONG(),e.getR33_NV_SHORT(),e.getR33_FV_LONG(),e.getR33_FV_SHORT(),e.getR33_QFHA(),
			e.getR34_PRODUCT(),e.getR34_NV_LONG(),e.getR34_NV_SHORT(),e.getR34_FV_LONG(),e.getR34_FV_SHORT(),e.getR34_QFHA(),
			e.getR35_PRODUCT(),e.getR35_NV_LONG(),e.getR35_NV_SHORT(),e.getR35_FV_LONG(),e.getR35_FV_SHORT(),e.getR35_QFHA(),
			e.getR36_PRODUCT(),e.getR36_NV_LONG(),e.getR36_NV_SHORT(),e.getR36_FV_LONG(),e.getR36_FV_SHORT(),e.getR36_QFHA(),
			e.getR37_PRODUCT(),e.getR37_NV_LONG(),e.getR37_NV_SHORT(),e.getR37_FV_LONG(),e.getR37_FV_SHORT(),e.getR37_QFHA(),
			e.getR38_PRODUCT(),e.getR38_NV_LONG(),e.getR38_NV_SHORT(),e.getR38_FV_LONG(),e.getR38_FV_SHORT(),e.getR38_QFHA(),
			e.getR39_PRODUCT(),e.getR39_NV_LONG(),e.getR39_NV_SHORT(),e.getR39_FV_LONG(),e.getR39_FV_SHORT(),e.getR39_QFHA(),
			e.getR40_PRODUCT(),e.getR40_NV_LONG(),e.getR40_NV_SHORT(),e.getR40_FV_LONG(),e.getR40_FV_SHORT(),e.getR40_QFHA(),
			e.getR41_PRODUCT(),e.getR41_NV_LONG(),e.getR41_NV_SHORT(),e.getR41_FV_LONG(),e.getR41_FV_SHORT(),e.getR41_QFHA(),
			e.getR42_PRODUCT(),e.getR42_NV_LONG(),e.getR42_NV_SHORT(),e.getR42_FV_LONG(),e.getR42_FV_SHORT(),e.getR42_QFHA(),
			e.getR43_PRODUCT(),e.getR43_NV_LONG(),e.getR43_NV_SHORT(),e.getR43_FV_LONG(),e.getR43_FV_SHORT(),e.getR43_QFHA(),
			e.getR44_PRODUCT(),e.getR44_NV_LONG(),e.getR44_NV_SHORT(),e.getR44_FV_LONG(),e.getR44_FV_SHORT(),e.getR44_QFHA(),
			e.getR45_PRODUCT(),e.getR45_NV_LONG(),e.getR45_NV_SHORT(),e.getR45_FV_LONG(),e.getR45_FV_SHORT(),e.getR45_QFHA(),
			e.getR46_PRODUCT(),e.getR46_NV_LONG(),e.getR46_NV_SHORT(),e.getR46_FV_LONG(),e.getR46_FV_SHORT(),e.getR46_QFHA(),
			e.getR47_PRODUCT(),e.getR47_NV_LONG(),e.getR47_NV_SHORT(),e.getR47_FV_LONG(),e.getR47_FV_SHORT(),e.getR47_QFHA(),
			e.getR48_PRODUCT(),e.getR48_NV_LONG(),e.getR48_NV_SHORT(),e.getR48_FV_LONG(),e.getR48_FV_SHORT(),e.getR48_QFHA(),
			e.getR49_PRODUCT(),e.getR49_NV_LONG(),e.getR49_NV_SHORT(),e.getR49_FV_LONG(),e.getR49_FV_SHORT(),e.getR49_QFHA(),
			e.getR50_PRODUCT(),e.getR50_NV_LONG(),e.getR50_NV_SHORT(),e.getR50_FV_LONG(),e.getR50_FV_SHORT(),e.getR50_QFHA(),
			e.getR51_PRODUCT(),e.getR51_NV_LONG(),e.getR51_NV_SHORT(),e.getR51_FV_LONG(),e.getR51_FV_SHORT(),e.getR51_QFHA(),
			e.getR52_PRODUCT(),e.getR52_NV_LONG(),e.getR52_NV_SHORT(),e.getR52_FV_LONG(),e.getR52_FV_SHORT(),e.getR52_QFHA(),
			e.getR53_PRODUCT(),e.getR53_NV_LONG(),e.getR53_NV_SHORT(),e.getR53_FV_LONG(),e.getR53_FV_SHORT(),e.getR53_QFHA(),
			e.getR54_PRODUCT(),e.getR54_NV_LONG(),e.getR54_NV_SHORT(),e.getR54_FV_LONG(),e.getR54_FV_SHORT(),e.getR54_QFHA(),
			e.getR55_PRODUCT(),e.getR55_NV_LONG(),e.getR55_NV_SHORT(),e.getR55_FV_LONG(),e.getR55_FV_SHORT(),e.getR55_QFHA()
		};
	}

	private Object[] rFieldValues(M_TBS_Resub_Summary_Entity e) {
		return new Object[]{
			e.getR11_PRODUCT(),e.getR11_NV_LONG(),e.getR11_NV_SHORT(),e.getR11_FV_LONG(),e.getR11_FV_SHORT(),e.getR11_QFHA(),
			e.getR12_PRODUCT(),e.getR12_NV_LONG(),e.getR12_NV_SHORT(),e.getR12_FV_LONG(),e.getR12_FV_SHORT(),e.getR12_QFHA(),
			e.getR13_PRODUCT(),e.getR13_NV_LONG(),e.getR13_NV_SHORT(),e.getR13_FV_LONG(),e.getR13_FV_SHORT(),e.getR13_QFHA(),
			e.getR14_PRODUCT(),e.getR14_NV_LONG(),e.getR14_NV_SHORT(),e.getR14_FV_LONG(),e.getR14_FV_SHORT(),e.getR14_QFHA(),
			e.getR15_PRODUCT(),e.getR15_NV_LONG(),e.getR15_NV_SHORT(),e.getR15_FV_LONG(),e.getR15_FV_SHORT(),e.getR15_QFHA(),
			e.getR16_PRODUCT(),e.getR16_NV_LONG(),e.getR16_NV_SHORT(),e.getR16_FV_LONG(),e.getR16_FV_SHORT(),e.getR16_QFHA(),
			e.getR17_PRODUCT(),e.getR17_NV_LONG(),e.getR17_NV_SHORT(),e.getR17_FV_LONG(),e.getR17_FV_SHORT(),e.getR17_QFHA(),
			e.getR18_PRODUCT(),e.getR18_NV_LONG(),e.getR18_NV_SHORT(),e.getR18_FV_LONG(),e.getR18_FV_SHORT(),e.getR18_QFHA(),
			e.getR19_PRODUCT(),e.getR19_NV_LONG(),e.getR19_NV_SHORT(),e.getR19_FV_LONG(),e.getR19_FV_SHORT(),e.getR19_QFHA(),
			e.getR20_PRODUCT(),e.getR20_NV_LONG(),e.getR20_NV_SHORT(),e.getR20_FV_LONG(),e.getR20_FV_SHORT(),e.getR20_QFHA(),
			e.getR21_PRODUCT(),e.getR21_NV_LONG(),e.getR21_NV_SHORT(),e.getR21_FV_LONG(),e.getR21_FV_SHORT(),e.getR21_QFHA(),
			e.getR22_PRODUCT(),e.getR22_NV_LONG(),e.getR22_NV_SHORT(),e.getR22_FV_LONG(),e.getR22_FV_SHORT(),e.getR22_QFHA(),
			e.getR23_PRODUCT(),e.getR23_NV_LONG(),e.getR23_NV_SHORT(),e.getR23_FV_LONG(),e.getR23_FV_SHORT(),e.getR23_QFHA(),
			e.getR24_PRODUCT(),e.getR24_NV_LONG(),e.getR24_NV_SHORT(),e.getR24_FV_LONG(),e.getR24_FV_SHORT(),e.getR24_QFHA(),
			e.getR25_PRODUCT(),e.getR25_NV_LONG(),e.getR25_NV_SHORT(),e.getR25_FV_LONG(),e.getR25_FV_SHORT(),e.getR25_QFHA(),
			e.getR26_PRODUCT(),e.getR26_NV_LONG(),e.getR26_NV_SHORT(),e.getR26_FV_LONG(),e.getR26_FV_SHORT(),e.getR26_QFHA(),
			e.getR27_PRODUCT(),e.getR27_NV_LONG(),e.getR27_NV_SHORT(),e.getR27_FV_LONG(),e.getR27_FV_SHORT(),e.getR27_QFHA(),
			e.getR28_PRODUCT(),e.getR28_NV_LONG(),e.getR28_NV_SHORT(),e.getR28_FV_LONG(),e.getR28_FV_SHORT(),e.getR28_QFHA(),
			e.getR29_PRODUCT(),e.getR29_NV_LONG(),e.getR29_NV_SHORT(),e.getR29_FV_LONG(),e.getR29_FV_SHORT(),e.getR29_QFHA(),
			e.getR30_PRODUCT(),e.getR30_NV_LONG(),e.getR30_NV_SHORT(),e.getR30_FV_LONG(),e.getR30_FV_SHORT(),e.getR30_QFHA(),
			e.getR31_PRODUCT(),e.getR31_NV_LONG(),e.getR31_NV_SHORT(),e.getR31_FV_LONG(),e.getR31_FV_SHORT(),e.getR31_QFHA(),
			e.getR32_PRODUCT(),e.getR32_NV_LONG(),e.getR32_NV_SHORT(),e.getR32_FV_LONG(),e.getR32_FV_SHORT(),e.getR32_QFHA(),
			e.getR33_PRODUCT(),e.getR33_NV_LONG(),e.getR33_NV_SHORT(),e.getR33_FV_LONG(),e.getR33_FV_SHORT(),e.getR33_QFHA(),
			e.getR34_PRODUCT(),e.getR34_NV_LONG(),e.getR34_NV_SHORT(),e.getR34_FV_LONG(),e.getR34_FV_SHORT(),e.getR34_QFHA(),
			e.getR35_PRODUCT(),e.getR35_NV_LONG(),e.getR35_NV_SHORT(),e.getR35_FV_LONG(),e.getR35_FV_SHORT(),e.getR35_QFHA(),
			e.getR36_PRODUCT(),e.getR36_NV_LONG(),e.getR36_NV_SHORT(),e.getR36_FV_LONG(),e.getR36_FV_SHORT(),e.getR36_QFHA(),
			e.getR37_PRODUCT(),e.getR37_NV_LONG(),e.getR37_NV_SHORT(),e.getR37_FV_LONG(),e.getR37_FV_SHORT(),e.getR37_QFHA(),
			e.getR38_PRODUCT(),e.getR38_NV_LONG(),e.getR38_NV_SHORT(),e.getR38_FV_LONG(),e.getR38_FV_SHORT(),e.getR38_QFHA(),
			e.getR39_PRODUCT(),e.getR39_NV_LONG(),e.getR39_NV_SHORT(),e.getR39_FV_LONG(),e.getR39_FV_SHORT(),e.getR39_QFHA(),
			e.getR40_PRODUCT(),e.getR40_NV_LONG(),e.getR40_NV_SHORT(),e.getR40_FV_LONG(),e.getR40_FV_SHORT(),e.getR40_QFHA(),
			e.getR41_PRODUCT(),e.getR41_NV_LONG(),e.getR41_NV_SHORT(),e.getR41_FV_LONG(),e.getR41_FV_SHORT(),e.getR41_QFHA(),
			e.getR42_PRODUCT(),e.getR42_NV_LONG(),e.getR42_NV_SHORT(),e.getR42_FV_LONG(),e.getR42_FV_SHORT(),e.getR42_QFHA(),
			e.getR43_PRODUCT(),e.getR43_NV_LONG(),e.getR43_NV_SHORT(),e.getR43_FV_LONG(),e.getR43_FV_SHORT(),e.getR43_QFHA(),
			e.getR44_PRODUCT(),e.getR44_NV_LONG(),e.getR44_NV_SHORT(),e.getR44_FV_LONG(),e.getR44_FV_SHORT(),e.getR44_QFHA(),
			e.getR45_PRODUCT(),e.getR45_NV_LONG(),e.getR45_NV_SHORT(),e.getR45_FV_LONG(),e.getR45_FV_SHORT(),e.getR45_QFHA(),
			e.getR46_PRODUCT(),e.getR46_NV_LONG(),e.getR46_NV_SHORT(),e.getR46_FV_LONG(),e.getR46_FV_SHORT(),e.getR46_QFHA(),
			e.getR47_PRODUCT(),e.getR47_NV_LONG(),e.getR47_NV_SHORT(),e.getR47_FV_LONG(),e.getR47_FV_SHORT(),e.getR47_QFHA(),
			e.getR48_PRODUCT(),e.getR48_NV_LONG(),e.getR48_NV_SHORT(),e.getR48_FV_LONG(),e.getR48_FV_SHORT(),e.getR48_QFHA(),
			e.getR49_PRODUCT(),e.getR49_NV_LONG(),e.getR49_NV_SHORT(),e.getR49_FV_LONG(),e.getR49_FV_SHORT(),e.getR49_QFHA(),
			e.getR50_PRODUCT(),e.getR50_NV_LONG(),e.getR50_NV_SHORT(),e.getR50_FV_LONG(),e.getR50_FV_SHORT(),e.getR50_QFHA(),
			e.getR51_PRODUCT(),e.getR51_NV_LONG(),e.getR51_NV_SHORT(),e.getR51_FV_LONG(),e.getR51_FV_SHORT(),e.getR51_QFHA(),
			e.getR52_PRODUCT(),e.getR52_NV_LONG(),e.getR52_NV_SHORT(),e.getR52_FV_LONG(),e.getR52_FV_SHORT(),e.getR52_QFHA(),
			e.getR53_PRODUCT(),e.getR53_NV_LONG(),e.getR53_NV_SHORT(),e.getR53_FV_LONG(),e.getR53_FV_SHORT(),e.getR53_QFHA(),
			e.getR54_PRODUCT(),e.getR54_NV_LONG(),e.getR54_NV_SHORT(),e.getR54_FV_LONG(),e.getR54_FV_SHORT(),e.getR54_QFHA(),
			e.getR55_PRODUCT(),e.getR55_NV_LONG(),e.getR55_NV_SHORT(),e.getR55_FV_LONG(),e.getR55_FV_SHORT(),e.getR55_QFHA()
		};
	}

	private Object[] rFieldValues(M_TBS_Resub_Detail_Entity e) {
		return new Object[]{
			e.getR11_PRODUCT(),e.getR11_NV_LONG(),e.getR11_NV_SHORT(),e.getR11_FV_LONG(),e.getR11_FV_SHORT(),e.getR11_QFHA(),
			e.getR12_PRODUCT(),e.getR12_NV_LONG(),e.getR12_NV_SHORT(),e.getR12_FV_LONG(),e.getR12_FV_SHORT(),e.getR12_QFHA(),
			e.getR13_PRODUCT(),e.getR13_NV_LONG(),e.getR13_NV_SHORT(),e.getR13_FV_LONG(),e.getR13_FV_SHORT(),e.getR13_QFHA(),
			e.getR14_PRODUCT(),e.getR14_NV_LONG(),e.getR14_NV_SHORT(),e.getR14_FV_LONG(),e.getR14_FV_SHORT(),e.getR14_QFHA(),
			e.getR15_PRODUCT(),e.getR15_NV_LONG(),e.getR15_NV_SHORT(),e.getR15_FV_LONG(),e.getR15_FV_SHORT(),e.getR15_QFHA(),
			e.getR16_PRODUCT(),e.getR16_NV_LONG(),e.getR16_NV_SHORT(),e.getR16_FV_LONG(),e.getR16_FV_SHORT(),e.getR16_QFHA(),
			e.getR17_PRODUCT(),e.getR17_NV_LONG(),e.getR17_NV_SHORT(),e.getR17_FV_LONG(),e.getR17_FV_SHORT(),e.getR17_QFHA(),
			e.getR18_PRODUCT(),e.getR18_NV_LONG(),e.getR18_NV_SHORT(),e.getR18_FV_LONG(),e.getR18_FV_SHORT(),e.getR18_QFHA(),
			e.getR19_PRODUCT(),e.getR19_NV_LONG(),e.getR19_NV_SHORT(),e.getR19_FV_LONG(),e.getR19_FV_SHORT(),e.getR19_QFHA(),
			e.getR20_PRODUCT(),e.getR20_NV_LONG(),e.getR20_NV_SHORT(),e.getR20_FV_LONG(),e.getR20_FV_SHORT(),e.getR20_QFHA(),
			e.getR21_PRODUCT(),e.getR21_NV_LONG(),e.getR21_NV_SHORT(),e.getR21_FV_LONG(),e.getR21_FV_SHORT(),e.getR21_QFHA(),
			e.getR22_PRODUCT(),e.getR22_NV_LONG(),e.getR22_NV_SHORT(),e.getR22_FV_LONG(),e.getR22_FV_SHORT(),e.getR22_QFHA(),
			e.getR23_PRODUCT(),e.getR23_NV_LONG(),e.getR23_NV_SHORT(),e.getR23_FV_LONG(),e.getR23_FV_SHORT(),e.getR23_QFHA(),
			e.getR24_PRODUCT(),e.getR24_NV_LONG(),e.getR24_NV_SHORT(),e.getR24_FV_LONG(),e.getR24_FV_SHORT(),e.getR24_QFHA(),
			e.getR25_PRODUCT(),e.getR25_NV_LONG(),e.getR25_NV_SHORT(),e.getR25_FV_LONG(),e.getR25_FV_SHORT(),e.getR25_QFHA(),
			e.getR26_PRODUCT(),e.getR26_NV_LONG(),e.getR26_NV_SHORT(),e.getR26_FV_LONG(),e.getR26_FV_SHORT(),e.getR26_QFHA(),
			e.getR27_PRODUCT(),e.getR27_NV_LONG(),e.getR27_NV_SHORT(),e.getR27_FV_LONG(),e.getR27_FV_SHORT(),e.getR27_QFHA(),
			e.getR28_PRODUCT(),e.getR28_NV_LONG(),e.getR28_NV_SHORT(),e.getR28_FV_LONG(),e.getR28_FV_SHORT(),e.getR28_QFHA(),
			e.getR29_PRODUCT(),e.getR29_NV_LONG(),e.getR29_NV_SHORT(),e.getR29_FV_LONG(),e.getR29_FV_SHORT(),e.getR29_QFHA(),
			e.getR30_PRODUCT(),e.getR30_NV_LONG(),e.getR30_NV_SHORT(),e.getR30_FV_LONG(),e.getR30_FV_SHORT(),e.getR30_QFHA(),
			e.getR31_PRODUCT(),e.getR31_NV_LONG(),e.getR31_NV_SHORT(),e.getR31_FV_LONG(),e.getR31_FV_SHORT(),e.getR31_QFHA(),
			e.getR32_PRODUCT(),e.getR32_NV_LONG(),e.getR32_NV_SHORT(),e.getR32_FV_LONG(),e.getR32_FV_SHORT(),e.getR32_QFHA(),
			e.getR33_PRODUCT(),e.getR33_NV_LONG(),e.getR33_NV_SHORT(),e.getR33_FV_LONG(),e.getR33_FV_SHORT(),e.getR33_QFHA(),
			e.getR34_PRODUCT(),e.getR34_NV_LONG(),e.getR34_NV_SHORT(),e.getR34_FV_LONG(),e.getR34_FV_SHORT(),e.getR34_QFHA(),
			e.getR35_PRODUCT(),e.getR35_NV_LONG(),e.getR35_NV_SHORT(),e.getR35_FV_LONG(),e.getR35_FV_SHORT(),e.getR35_QFHA(),
			e.getR36_PRODUCT(),e.getR36_NV_LONG(),e.getR36_NV_SHORT(),e.getR36_FV_LONG(),e.getR36_FV_SHORT(),e.getR36_QFHA(),
			e.getR37_PRODUCT(),e.getR37_NV_LONG(),e.getR37_NV_SHORT(),e.getR37_FV_LONG(),e.getR37_FV_SHORT(),e.getR37_QFHA(),
			e.getR38_PRODUCT(),e.getR38_NV_LONG(),e.getR38_NV_SHORT(),e.getR38_FV_LONG(),e.getR38_FV_SHORT(),e.getR38_QFHA(),
			e.getR39_PRODUCT(),e.getR39_NV_LONG(),e.getR39_NV_SHORT(),e.getR39_FV_LONG(),e.getR39_FV_SHORT(),e.getR39_QFHA(),
			e.getR40_PRODUCT(),e.getR40_NV_LONG(),e.getR40_NV_SHORT(),e.getR40_FV_LONG(),e.getR40_FV_SHORT(),e.getR40_QFHA(),
			e.getR41_PRODUCT(),e.getR41_NV_LONG(),e.getR41_NV_SHORT(),e.getR41_FV_LONG(),e.getR41_FV_SHORT(),e.getR41_QFHA(),
			e.getR42_PRODUCT(),e.getR42_NV_LONG(),e.getR42_NV_SHORT(),e.getR42_FV_LONG(),e.getR42_FV_SHORT(),e.getR42_QFHA(),
			e.getR43_PRODUCT(),e.getR43_NV_LONG(),e.getR43_NV_SHORT(),e.getR43_FV_LONG(),e.getR43_FV_SHORT(),e.getR43_QFHA(),
			e.getR44_PRODUCT(),e.getR44_NV_LONG(),e.getR44_NV_SHORT(),e.getR44_FV_LONG(),e.getR44_FV_SHORT(),e.getR44_QFHA(),
			e.getR45_PRODUCT(),e.getR45_NV_LONG(),e.getR45_NV_SHORT(),e.getR45_FV_LONG(),e.getR45_FV_SHORT(),e.getR45_QFHA(),
			e.getR46_PRODUCT(),e.getR46_NV_LONG(),e.getR46_NV_SHORT(),e.getR46_FV_LONG(),e.getR46_FV_SHORT(),e.getR46_QFHA(),
			e.getR47_PRODUCT(),e.getR47_NV_LONG(),e.getR47_NV_SHORT(),e.getR47_FV_LONG(),e.getR47_FV_SHORT(),e.getR47_QFHA(),
			e.getR48_PRODUCT(),e.getR48_NV_LONG(),e.getR48_NV_SHORT(),e.getR48_FV_LONG(),e.getR48_FV_SHORT(),e.getR48_QFHA(),
			e.getR49_PRODUCT(),e.getR49_NV_LONG(),e.getR49_NV_SHORT(),e.getR49_FV_LONG(),e.getR49_FV_SHORT(),e.getR49_QFHA(),
			e.getR50_PRODUCT(),e.getR50_NV_LONG(),e.getR50_NV_SHORT(),e.getR50_FV_LONG(),e.getR50_FV_SHORT(),e.getR50_QFHA(),
			e.getR51_PRODUCT(),e.getR51_NV_LONG(),e.getR51_NV_SHORT(),e.getR51_FV_LONG(),e.getR51_FV_SHORT(),e.getR51_QFHA(),
			e.getR52_PRODUCT(),e.getR52_NV_LONG(),e.getR52_NV_SHORT(),e.getR52_FV_LONG(),e.getR52_FV_SHORT(),e.getR52_QFHA(),
			e.getR53_PRODUCT(),e.getR53_NV_LONG(),e.getR53_NV_SHORT(),e.getR53_FV_LONG(),e.getR53_FV_SHORT(),e.getR53_QFHA(),
			e.getR54_PRODUCT(),e.getR54_NV_LONG(),e.getR54_NV_SHORT(),e.getR54_FV_LONG(),e.getR54_FV_SHORT(),e.getR54_QFHA(),
			e.getR55_PRODUCT(),e.getR55_NV_LONG(),e.getR55_NV_SHORT(),e.getR55_FV_LONG(),e.getR55_FV_SHORT(),e.getR55_QFHA()
		};
	}

	private void saveSummary(M_TBS_Summary_Entity e) {
		Object[] rVals = rFieldValues(e);
		Object[] params = new Object[rVals.length + 8];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length]     = e.getReportVersion();
		params[rVals.length + 1] = e.getREPORT_FREQUENCY();
		params[rVals.length + 2] = e.getREPORT_CODE();
		params[rVals.length + 3] = e.getREPORT_DESC();
		params[rVals.length + 4] = e.getENTITY_FLG();
		params[rVals.length + 5] = e.getMODIFY_FLG();
		params[rVals.length + 6] = e.getDEL_FLG();
		params[rVals.length + 7] = e.getReportDate();
		jdbcTemplate.update(
			"UPDATE BRRS_M_TBS_SUMMARYTABLE SET " + R_FIELDS_SET +
			",REPORT_VERSION=?,REPORT_FREQUENCY=?,REPORT_CODE=?,REPORT_DESC=?,ENTITY_FLG=?,MODIFY_FLG=?,DEL_FLG=?" +
			" WHERE REPORT_DATE=?", params);
	}

	private void saveDetail(M_TBS_Detail_Entity e) {
		int cnt = jdbcTemplate.queryForObject(
			"SELECT COUNT(*) FROM BRRS_M_TBS_DETAILTABLE WHERE REPORT_DATE=?",
			new Object[]{e.getReportDate()}, Integer.class);
		Object[] rVals = rFieldValues(e);
		if (cnt > 0) {
			Object[] params = new Object[rVals.length + 8];
			System.arraycopy(rVals, 0, params, 0, rVals.length);
			params[rVals.length]     = e.getReportVersion();
			params[rVals.length + 1] = e.getREPORT_FREQUENCY();
			params[rVals.length + 2] = e.getREPORT_CODE();
			params[rVals.length + 3] = e.getREPORT_DESC();
			params[rVals.length + 4] = e.getENTITY_FLG();
			params[rVals.length + 5] = e.getMODIFY_FLG();
			params[rVals.length + 6] = e.getDEL_FLG();
			params[rVals.length + 7] = e.getReportDate();
			jdbcTemplate.update(
				"UPDATE BRRS_M_TBS_DETAILTABLE SET " + R_FIELDS_SET +
				",REPORT_VERSION=?,REPORT_FREQUENCY=?,REPORT_CODE=?,REPORT_DESC=?,ENTITY_FLG=?,MODIFY_FLG=?,DEL_FLG=?" +
				" WHERE REPORT_DATE=?", params);
		} else {
			Object[] params = new Object[rVals.length + 8];
			System.arraycopy(rVals, 0, params, 0, rVals.length);
			params[rVals.length]     = e.getReportDate();
			params[rVals.length + 1] = e.getReportVersion();
			params[rVals.length + 2] = e.getREPORT_FREQUENCY();
			params[rVals.length + 3] = e.getREPORT_CODE();
			params[rVals.length + 4] = e.getREPORT_DESC();
			params[rVals.length + 5] = e.getENTITY_FLG();
			params[rVals.length + 6] = e.getMODIFY_FLG();
			params[rVals.length + 7] = e.getDEL_FLG();
			jdbcTemplate.update(
				"INSERT INTO BRRS_M_TBS_DETAILTABLE (REPORT_DATE," + R_COLS +
				",REPORT_VERSION,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC,ENTITY_FLG,MODIFY_FLG,DEL_FLG) VALUES (?," +
				R_PLACEHOLDERS + ",?,?,?,?,?,?,?)", params);
		}
	}

	private void insertResubSummary(M_TBS_Resub_Summary_Entity e) {
		Object[] rVals = rFieldValues(e);
		Object[] params = new Object[rVals.length + 6];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length]     = e.getReportDate();
		params[rVals.length + 1] = e.getReportVersion();
		params[rVals.length + 2] = e.getReportResubDate();
		params[rVals.length + 3] = e.getREPORT_FREQUENCY();
		params[rVals.length + 4] = e.getREPORT_CODE();
		params[rVals.length + 5] = e.getREPORT_DESC();
		jdbcTemplate.update(
			"INSERT INTO BRRS_M_TBS_RESUB_SUMMARYTABLE (" + R_COLS +
			",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC) VALUES (" +
			R_PLACEHOLDERS + ",?,?,?,?,?,?)", params);
	}

	private void insertResubDetail(M_TBS_Resub_Detail_Entity e) {
		Object[] rVals = rFieldValues(e);
		Object[] params = new Object[rVals.length + 6];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length]     = e.getReportDate();
		params[rVals.length + 1] = e.getReportVersion();
		params[rVals.length + 2] = e.getReportResubDate();
		params[rVals.length + 3] = e.getREPORT_FREQUENCY();
		params[rVals.length + 4] = e.getREPORT_CODE();
		params[rVals.length + 5] = e.getREPORT_DESC();
		jdbcTemplate.update(
			"INSERT INTO BRRS_M_TBS_RESUB_DETAILTABLE (" + R_COLS +
			",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC) VALUES (" +
			R_PLACEHOLDERS + ",?,?,?,?,?,?)", params);
	}

	private void insertArchivalSummary(M_TBS_Archival_Summary_Entity e) {
		Object[] rVals = rFieldValues(e);
		Object[] params = new Object[rVals.length + 6];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length]     = e.getReportDate();
		params[rVals.length + 1] = e.getReportVersion();
		params[rVals.length + 2] = e.getReportResubDate();
		params[rVals.length + 3] = e.getREPORT_FREQUENCY();
		params[rVals.length + 4] = e.getREPORT_CODE();
		params[rVals.length + 5] = e.getREPORT_DESC();
		jdbcTemplate.update(
			"INSERT INTO BRRS_M_TBS_ARCHIVALTABLE_SUMMARY (" + R_COLS +
			",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC) VALUES (" +
			R_PLACEHOLDERS + ",?,?,?,?,?,?)", params);
	}

	private void insertArchivalDetail(M_TBS_Archival_Detail_Entity e) {
		Object[] rVals = rFieldValues(e);
		Object[] params = new Object[rVals.length + 6];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length]     = e.getReportDate();
		params[rVals.length + 1] = e.getReportVersion();
		params[rVals.length + 2] = e.getReportResubDate();
		params[rVals.length + 3] = e.getREPORT_FREQUENCY();
		params[rVals.length + 4] = e.getREPORT_CODE();
		params[rVals.length + 5] = e.getREPORT_DESC();
		jdbcTemplate.update(
			"INSERT INTO BRRS_M_TBS_ARCHIVALTABLE_DETAIL (" + R_COLS +
			",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC) VALUES (" +
			R_PLACEHOLDERS + ",?,?,?,?,?,?)", params);
	}

	public ModelAndView getBRRS_M_TBSView(String reportId, String fromdate, String todate, String currency,
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
				List<M_TBS_Archival_Summary_Entity> T1Master = getArchivalSummaryByDateAndVersion(d1, version);
				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_TBS_Resub_Summary_Entity> T1Master = getResubSummaryByDateAndVersion(d1, version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<M_TBS_Summary_Entity> T1Master = getSummaryByDate(dateformat.parse(todate));
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_TBS_Archival_Detail_Entity> T1Master = getArchivalDetailByDateAndVersion(d1, version);
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_TBS_Resub_Detail_Entity> T1Master = getResubDetailByDateAndVersion(d1, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_TBS_Detail_Entity> T1Master = getDetailByDate(dateformat.parse(todate));
					System.out.println("Details......T1Master Size " + T1Master.size());
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_TBS");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	
	public void updateReport(M_TBS_Summary_Entity updatedEntity) {

	    System.out.println("Came to TBS Summary services");
	    System.out.println("Report Date: " + updatedEntity.getReportDate());

	    // Fetch existing SUMMARY
	    List<M_TBS_Summary_Entity> summaryList = getSummaryByDate(updatedEntity.getReportDate());
	    if (summaryList.isEmpty()) throw new RuntimeException(
	            "Summary record not found for REPORT_DATE: " + updatedEntity.getReportDate());
	    M_TBS_Summary_Entity existingSummary = summaryList.get(0);

	    // Audit old copy
	    M_TBS_Summary_Entity oldcopy = new M_TBS_Summary_Entity();
	    BeanUtils.copyProperties(existingSummary, oldcopy);

	    // Fetch existing DETAIL
	    List<M_TBS_Detail_Entity> detailList = getDetailByDate(updatedEntity.getReportDate());
	    M_TBS_Detail_Entity existingDetail;
	    if (detailList.isEmpty()) {
	        existingDetail = new M_TBS_Detail_Entity();
	        existingDetail.setReportDate(updatedEntity.getReportDate());
	    } else {
	        existingDetail = detailList.get(0);
	    }

	    try {

	        String[] fields = {
	                "NV_LONG",
	                "NV_SHORT",
	                "FV_LONG",
	                "FV_SHORT",
	                "QFHA"
	        };

	        // Helper: copy rows into Summary + Detail
	        java.util.function.Consumer<int[]> copyRows = (rows) -> {

	            for (int row : rows) {

	                String prefix = "R" + row + "_";

	                for (String field : fields) {

	                    String getterName = "get" + prefix + field;
	                    String setterName = "set" + prefix + field;

	                    try {

	                        Method getter = M_TBS_Summary_Entity.class
	                                .getMethod(getterName);

	                        Method summarySetter = M_TBS_Summary_Entity.class
	                                .getMethod(setterName, getter.getReturnType());

	                        Method detailSetter = M_TBS_Detail_Entity.class
	                                .getMethod(setterName, getter.getReturnType());

	                        Object newValue = getter.invoke(updatedEntity);

	                        summarySetter.invoke(existingSummary, newValue);
	                        detailSetter.invoke(existingDetail, newValue);

	                    } catch (NoSuchMethodException e) {
	                        continue;
	                    } catch (Exception e) {
	                        throw new RuntimeException(e);
	                    }
	                }
	            }
	        };

	        // Helper: copy total rows
	        java.util.function.Consumer<Integer> copyTotal = (row) -> {

	            String prefix = "R" + row + "_";

	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {

	                    Method getter = M_TBS_Summary_Entity.class
	                            .getMethod(getterName);

	                    Method summarySetter = M_TBS_Summary_Entity.class
	                            .getMethod(setterName, getter.getReturnType());

	                    Method detailSetter = M_TBS_Detail_Entity.class
	                            .getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);

	                    summarySetter.invoke(existingSummary, newValue);
	                    detailSetter.invoke(existingDetail, newValue);

	                } catch (NoSuchMethodException e) {
	                    continue;
	                } catch (Exception e) {
	                    throw new RuntimeException(e);
	                }
	            }
	        };

	        // R11 = SUM(C12:C16) + C21
	        copyRows.accept(new int[] {12, 13, 14, 15, 16, 21});
	        copyTotal.accept(11);

	        // R16 = SUM(C17:C20)
	        copyRows.accept(new int[] {17, 18, 19, 20});
	        copyTotal.accept(16);

	        // R22 = SUM(C23:C27) + C33
	        copyRows.accept(new int[] {23, 24, 25, 26, 27, 33});
	        copyTotal.accept(22);

	        // R27 = SUM(C28:C32)
	        copyRows.accept(new int[] {28, 29, 30, 31, 32});
	        copyTotal.accept(27);

	        // R34 = C35 + C36 + C40
	        copyRows.accept(new int[] {35, 36, 40});
	        copyTotal.accept(34);

	        // R36 = SUM(C37:C39)
	        copyRows.accept(new int[] {37, 38, 39});
	        copyTotal.accept(36);

	        // R41 = C42 + C43 + C44 + C49
	        copyRows.accept(new int[] {42, 43, 44, 49});
	        copyTotal.accept(41);

	        // R44 = SUM(C45:C48)
	        copyRows.accept(new int[] {45, 46, 47, 48});
	        copyTotal.accept(44);

	        // R50 = SUM(C51:C54)
	        copyRows.accept(new int[] {51, 52, 53, 54});
	        copyTotal.accept(50);

	        // R55 = C50 + C41 + C34 + C22 + C11
	        copyRows.accept(new int[] {50, 41, 34, 22, 11});
	        copyTotal.accept(55);

	    } catch (Exception e) {
	        throw new RuntimeException(
	                "Error while updating M_TBS Summary & Detail report fields", e);
	    }

	    // Audit check
	    String changes = auditService.getChanges(oldcopy, existingSummary);

	    if (!changes.isEmpty()) {

	        saveSummary(existingSummary);
	        saveDetail(existingDetail);

	        auditService.compareEntitiesmanual(
	                oldcopy,
	                existingSummary,
	                updatedEntity.getReportDate().toString(),
	                "M TBS Summary Screen",
	                "BRRS_M_TBS_SUMMARY"
	        );
	    }
	}


	public void updateResubReport(M_TBS_Resub_Summary_Entity updatedEntity) {

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

		M_TBS_Resub_Summary_Entity resubSummary = new M_TBS_Resub_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "reportResubDate");

		resubSummary.setReportDate(reportDate);
		resubSummary.setReportVersion(newVersion);
		resubSummary.setReportResubDate(now);

		// ====================================================
		// 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
		// ====================================================

		M_TBS_Resub_Detail_Entity resubDetail = new M_TBS_Resub_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "reportResubDate");

		resubDetail.setReportDate(reportDate);
		resubDetail.setReportVersion(newVersion);
		resubDetail.setReportResubDate(now);

		// ====================================================
		// 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
		// ====================================================

		M_TBS_Archival_Summary_Entity archSummary = new M_TBS_Archival_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "reportResubDate");

		archSummary.setReportDate(reportDate);
		archSummary.setReportVersion(newVersion); // SAME VERSION
		archSummary.setReportResubDate(now);

		// ====================================================
		// 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
		// ====================================================

		M_TBS_Archival_Detail_Entity archDetail = new M_TBS_Archival_Detail_Entity();

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

	public List<Object[]> getM_TBSResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_TBS_Archival_Summary_Entity> latestArchivalList = getArchivalSummaryWithVersionAll();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_TBS_Archival_Summary_Entity entity : latestArchivalList) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					resubList.add(row);
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}
		} catch (Exception e) {
			System.err.println("Error fetching M_TBS Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	public List<Object[]> getM_TBSArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_TBS_Archival_Summary_Entity> repoData = getArchivalSummaryWithVersionAll();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_TBS_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(), entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_TBS_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_TBS Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Normal format Excel

	public byte[] getBRRS_M_TBSExcel(String filename, String reportId, String fromdate, String todate, String currency,
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
				return getExcelM_TBSARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_TBSResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_TBSEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				List<M_TBS_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_TBS report. Returning empty result.");
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
							M_TBS_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

							//REPORT_DATE
							row = sheet.getRow(6);
							Cell cell1 = row.getCell(1);
							if (cell1 == null) {
							    cell1 = row.createCell(1);
							}

							if (record.getReportDate() != null) {
							    cell1.setCellValue(record.getReportDate()); // java.util.Date
							    cell1.setCellStyle(dateStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							
							// row12
							// Column B
							row = sheet.getRow(10);

							 cell1 = row.getCell(2);
							if (record.getR11_NV_LONG() != null) {
								cell1.setCellValue(record.getR11_NV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1=row.getCell(3);
							if (record.getR11_NV_SHORT() != null) {
								cell1.setCellValue(record.getR11_NV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

						    cell1 = row.getCell(4);
							if (record.getR11_FV_LONG() != null) {
								cell1.setCellValue(record.getR11_FV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}		
								
								
							cell1 = row.getCell(5);
							if (record.getR11_FV_SHORT() != null) {
								cell1.setCellValue(record.getR11_FV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1 = row.getCell(6);
							if (record.getR11_QFHA() != null) {
								cell1.setCellValue(record.getR11_QFHA().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
		//------ROW11---------						
							row=sheet.getRow(11);
						    cell1 = row.getCell(2);
							if (record.getR12_NV_LONG() != null) {
								cell1.setCellValue(record.getR12_NV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1=row.getCell(3);
							if (record.getR12_NV_SHORT() != null) {
								cell1.setCellValue(record.getR12_NV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR12_FV_LONG() != null) {
								cell1.setCellValue(record.getR12_FV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}		
								
								
							cell1 = row.getCell(5);
							if (record.getR12_FV_SHORT() != null) {
								cell1.setCellValue(record.getR12_FV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1 = row.getCell(6);
							if (record.getR12_QFHA() != null) {
								cell1.setCellValue(record.getR12_QFHA().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							//------ROW12---------						
							row=sheet.getRow(12);
						    cell1 = row.getCell(2);
							if (record.getR13_NV_LONG() != null) {
								cell1.setCellValue(record.getR13_NV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1=row.getCell(3);
							if (record.getR13_NV_SHORT() != null) {
								cell1.setCellValue(record.getR13_NV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR13_FV_LONG() != null) {
								cell1.setCellValue(record.getR13_FV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}		
								
								
							cell1 = row.getCell(5);
							if (record.getR13_FV_SHORT() != null) {
								cell1.setCellValue(record.getR13_FV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1 = row.getCell(6);
							if (record.getR13_QFHA() != null) {
								cell1.setCellValue(record.getR13_QFHA().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	

							//------ROW13---------
							row = sheet.getRow(13);

							// NV_LONG
							cell1 = row.getCell(2);
							if (record.getR14_NV_LONG() != null) {
							    cell1.setCellValue(record.getR14_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							// NV_SHORT
							cell1 = row.getCell(3);
							if (record.getR14_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR14_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							// FV_LONG
							cell1 = row.getCell(4);
							if (record.getR14_FV_LONG() != null) {
							    cell1.setCellValue(record.getR14_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							// FV_SHORT
							cell1 = row.getCell(5);
							if (record.getR14_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR14_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							// QFHA
							cell1 = row.getCell(6);
							if (record.getR14_QFHA() != null) {
							    cell1.setCellValue(record.getR14_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW14---------
							row = sheet.getRow(14);

							cell1 = row.getCell(2);
							if (record.getR15_NV_LONG() != null) {
							    cell1.setCellValue(record.getR15_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR15_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR15_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR15_FV_LONG() != null) {
							    cell1.setCellValue(record.getR15_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR15_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR15_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR15_QFHA() != null) {
							    cell1.setCellValue(record.getR15_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW15---------
							row = sheet.getRow(15);

							cell1 = row.getCell(2);
							if (record.getR16_NV_LONG() != null) {
							    cell1.setCellValue(record.getR16_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR16_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR16_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR16_FV_LONG() != null) {
							    cell1.setCellValue(record.getR16_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR16_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR16_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR16_QFHA() != null) {
							    cell1.setCellValue(record.getR16_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW16---------
							row = sheet.getRow(16);

							cell1 = row.getCell(2);
							if (record.getR17_NV_LONG() != null) {
							    cell1.setCellValue(record.getR17_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR17_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR17_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR17_FV_LONG() != null) {
							    cell1.setCellValue(record.getR17_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR17_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR17_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR17_QFHA() != null) {
							    cell1.setCellValue(record.getR17_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW17---------
							row = sheet.getRow(17);

							cell1 = row.getCell(2);
							if (record.getR18_NV_LONG() != null) {
							    cell1.setCellValue(record.getR18_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR18_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR18_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR18_FV_LONG() != null) {
							    cell1.setCellValue(record.getR18_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR18_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR18_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR18_QFHA() != null) {
							    cell1.setCellValue(record.getR18_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW18---------
							row = sheet.getRow(18);

							cell1 = row.getCell(2);
							if (record.getR19_NV_LONG() != null) {
							    cell1.setCellValue(record.getR19_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR19_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR19_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR19_FV_LONG() != null) {
							    cell1.setCellValue(record.getR19_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR19_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR19_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR19_QFHA() != null) {
							    cell1.setCellValue(record.getR19_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW19---------
							row = sheet.getRow(19);

							cell1 = row.getCell(2);
							if (record.getR20_NV_LONG() != null) {
							    cell1.setCellValue(record.getR20_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR20_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR20_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR20_FV_LONG() != null) {
							    cell1.setCellValue(record.getR20_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR20_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR20_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR20_QFHA() != null) {
							    cell1.setCellValue(record.getR20_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW20---------
							row = sheet.getRow(20);

							cell1 = row.getCell(2);
							if (record.getR21_NV_LONG() != null) {
							    cell1.setCellValue(record.getR21_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR21_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR21_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR21_FV_LONG() != null) {
							    cell1.setCellValue(record.getR21_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR21_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR21_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR21_QFHA() != null) {
							    cell1.setCellValue(record.getR21_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW21---------
							row = sheet.getRow(21);

							cell1 = row.getCell(2);
							if (record.getR22_NV_LONG() != null) {
							    cell1.setCellValue(record.getR22_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR22_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR22_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR22_FV_LONG() != null) {
							    cell1.setCellValue(record.getR22_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR22_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR22_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR22_QFHA() != null) {
							    cell1.setCellValue(record.getR22_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW22---------
							row = sheet.getRow(22);

							cell1 = row.getCell(2);
							if (record.getR23_NV_LONG() != null) {
							    cell1.setCellValue(record.getR23_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR23_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR23_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR23_FV_LONG() != null) {
							    cell1.setCellValue(record.getR23_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR23_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR23_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR23_QFHA() != null) {
							    cell1.setCellValue(record.getR23_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW23---------
							row = sheet.getRow(23);

							cell1 = row.getCell(2);
							if (record.getR24_NV_LONG() != null) {
							    cell1.setCellValue(record.getR24_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR24_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR24_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR24_FV_LONG() != null) {
							    cell1.setCellValue(record.getR24_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR24_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR24_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR24_QFHA() != null) {
							    cell1.setCellValue(record.getR24_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW24---------
							row = sheet.getRow(24);

							cell1 = row.getCell(2);
							if (record.getR25_NV_LONG() != null) {
							    cell1.setCellValue(record.getR25_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR25_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR25_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR25_FV_LONG() != null) {
							    cell1.setCellValue(record.getR25_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR25_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR25_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR25_QFHA() != null) {
							    cell1.setCellValue(record.getR25_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW25---------
							row = sheet.getRow(25);

							cell1 = row.getCell(2);
							if (record.getR26_NV_LONG() != null) {
							    cell1.setCellValue(record.getR26_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR26_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR26_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR26_FV_LONG() != null) {
							    cell1.setCellValue(record.getR26_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR26_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR26_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR26_QFHA() != null) {
							    cell1.setCellValue(record.getR26_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW26---------
							row = sheet.getRow(26);

							cell1 = row.getCell(2);
							if (record.getR27_NV_LONG() != null) {
							    cell1.setCellValue(record.getR27_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR27_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR27_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR27_FV_LONG() != null) {
							    cell1.setCellValue(record.getR27_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR27_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR27_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR27_QFHA() != null) {
							    cell1.setCellValue(record.getR27_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW27---------
							row = sheet.getRow(27);

							cell1 = row.getCell(2);
							if (record.getR28_NV_LONG() != null) {
							    cell1.setCellValue(record.getR28_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR28_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR28_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR28_FV_LONG() != null) {
							    cell1.setCellValue(record.getR28_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR28_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR28_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR28_QFHA() != null) {
							    cell1.setCellValue(record.getR28_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW28---------
							row = sheet.getRow(28);

							cell1 = row.getCell(2);
							if (record.getR29_NV_LONG() != null) {
							    cell1.setCellValue(record.getR29_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR29_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR29_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR29_FV_LONG() != null) {
							    cell1.setCellValue(record.getR29_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR29_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR29_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR29_QFHA() != null) {
							    cell1.setCellValue(record.getR29_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW29---------
							row = sheet.getRow(29);

							cell1 = row.getCell(2);
							if (record.getR30_NV_LONG() != null) {
							    cell1.setCellValue(record.getR30_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR30_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR30_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR30_FV_LONG() != null) {
							    cell1.setCellValue(record.getR30_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR30_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR30_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR30_QFHA() != null) {
							    cell1.setCellValue(record.getR30_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW30--------- R31
							row = sheet.getRow(30);

							// NV_LONG
							cell1 = row.getCell(2);
							if (record.getR31_NV_LONG() != null) {
							    cell1.setCellValue(record.getR31_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							// NV_SHORT
							cell1 = row.getCell(3);
							if (record.getR31_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR31_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							// FV_LONG
							cell1 = row.getCell(4);
							if (record.getR31_FV_LONG() != null) {
							    cell1.setCellValue(record.getR31_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							// FV_SHORT
							cell1 = row.getCell(5);
							if (record.getR31_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR31_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							// QFHA
							cell1 = row.getCell(6);
							if (record.getR31_QFHA() != null) {
							    cell1.setCellValue(record.getR31_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW31--------- R32
							row = sheet.getRow(31);

							cell1 = row.getCell(2);
							if (record.getR32_NV_LONG() != null) {
							    cell1.setCellValue(record.getR32_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR32_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR32_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR32_FV_LONG() != null) {
							    cell1.setCellValue(record.getR32_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR32_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR32_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR32_QFHA() != null) {
							    cell1.setCellValue(record.getR32_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW32--------- R33
							row = sheet.getRow(32);

							cell1 = row.getCell(2);
							if (record.getR33_NV_LONG() != null) {
							    cell1.setCellValue(record.getR33_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR33_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR33_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR33_FV_LONG() != null) {
							    cell1.setCellValue(record.getR33_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR33_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR33_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR33_QFHA() != null) {
							    cell1.setCellValue(record.getR33_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW33--------- R34
							row = sheet.getRow(33);

							cell1 = row.getCell(2);
							if (record.getR34_NV_LONG() != null) {
							    cell1.setCellValue(record.getR34_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR34_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR34_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR34_FV_LONG() != null) {
							    cell1.setCellValue(record.getR34_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR34_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR34_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR34_QFHA() != null) {
							    cell1.setCellValue(record.getR34_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW34--------- R35
							row = sheet.getRow(34);

							cell1 = row.getCell(2);
							if (record.getR35_NV_LONG() != null) {
							    cell1.setCellValue(record.getR35_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR35_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR35_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR35_FV_LONG() != null) {
							    cell1.setCellValue(record.getR35_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR35_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR35_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR35_QFHA() != null) {
							    cell1.setCellValue(record.getR35_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW35--------- R36
							row = sheet.getRow(35);

							cell1 = row.getCell(2);
							if (record.getR36_NV_LONG() != null) {
							    cell1.setCellValue(record.getR36_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR36_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR36_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR36_FV_LONG() != null) {
							    cell1.setCellValue(record.getR36_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR36_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR36_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR36_QFHA() != null) {
							    cell1.setCellValue(record.getR36_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW36--------- R37
							row = sheet.getRow(36);

							cell1 = row.getCell(2);
							if (record.getR37_NV_LONG() != null) {
							    cell1.setCellValue(record.getR37_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR37_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR37_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR37_FV_LONG() != null) {
							    cell1.setCellValue(record.getR37_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR37_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR37_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR37_QFHA() != null) {
							    cell1.setCellValue(record.getR37_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW37--------- R38
							row = sheet.getRow(37);

							cell1 = row.getCell(2);
							if (record.getR38_NV_LONG() != null) {
							    cell1.setCellValue(record.getR38_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR38_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR38_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR38_FV_LONG() != null) {
							    cell1.setCellValue(record.getR38_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR38_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR38_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR38_QFHA() != null) {
							    cell1.setCellValue(record.getR38_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW38--------- R39
							row = sheet.getRow(38);

							cell1 = row.getCell(2);
							if (record.getR39_NV_LONG() != null) {
							    cell1.setCellValue(record.getR39_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR39_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR39_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR39_FV_LONG() != null) {
							    cell1.setCellValue(record.getR39_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR39_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR39_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR39_QFHA() != null) {
							    cell1.setCellValue(record.getR39_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW39--------- R40
							row = sheet.getRow(39);

							cell1 = row.getCell(2);
							if (record.getR40_NV_LONG() != null) {
							    cell1.setCellValue(record.getR40_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR40_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR40_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR40_FV_LONG() != null) {
							    cell1.setCellValue(record.getR40_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR40_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR40_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR40_QFHA() != null) {
							    cell1.setCellValue(record.getR40_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							//------ROW40--------- R41
							row = sheet.getRow(40);

							cell1 = row.getCell(2);
							if (record.getR41_NV_LONG() != null) {
							    cell1.setCellValue(record.getR41_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR41_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR41_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR41_FV_LONG() != null) {
							    cell1.setCellValue(record.getR41_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR41_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR41_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR41_QFHA() != null) {
							    cell1.setCellValue(record.getR41_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW41--------- R42
							row = sheet.getRow(41);

							cell1 = row.getCell(2);
							if (record.getR42_NV_LONG() != null) {
							    cell1.setCellValue(record.getR42_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR42_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR42_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR42_FV_LONG() != null) {
							    cell1.setCellValue(record.getR42_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR42_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR42_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR42_QFHA() != null) {
							    cell1.setCellValue(record.getR42_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW42--------- R43
							row = sheet.getRow(42);

							cell1 = row.getCell(2);
							if (record.getR43_NV_LONG() != null) {
							    cell1.setCellValue(record.getR43_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR43_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR43_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR43_FV_LONG() != null) {
							    cell1.setCellValue(record.getR43_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR43_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR43_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR43_QFHA() != null) {
							    cell1.setCellValue(record.getR43_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW43--------- R44
							row = sheet.getRow(43);

							cell1 = row.getCell(2);
							if (record.getR44_NV_LONG() != null) {
							    cell1.setCellValue(record.getR44_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR44_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR44_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR44_FV_LONG() != null) {
							    cell1.setCellValue(record.getR44_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR44_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR44_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR44_QFHA() != null) {
							    cell1.setCellValue(record.getR44_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW44--------- R45
							row = sheet.getRow(44);

							cell1 = row.getCell(2);
							if (record.getR45_NV_LONG() != null) {
							    cell1.setCellValue(record.getR45_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR45_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR45_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR45_FV_LONG() != null) {
							    cell1.setCellValue(record.getR45_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR45_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR45_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR45_QFHA() != null) {
							    cell1.setCellValue(record.getR45_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW45--------- R46
							row = sheet.getRow(45);

							cell1 = row.getCell(2);
							if (record.getR46_NV_LONG() != null) {
							    cell1.setCellValue(record.getR46_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR46_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR46_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR46_FV_LONG() != null) {
							    cell1.setCellValue(record.getR46_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR46_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR46_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR46_QFHA() != null) {
							    cell1.setCellValue(record.getR46_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW46--------- R47
							row = sheet.getRow(46);

							cell1 = row.getCell(2);
							if (record.getR47_NV_LONG() != null) {
							    cell1.setCellValue(record.getR47_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR47_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR47_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR47_FV_LONG() != null) {
							    cell1.setCellValue(record.getR47_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR47_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR47_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR47_QFHA() != null) {
							    cell1.setCellValue(record.getR47_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW47--------- R48
							row = sheet.getRow(47);

							cell1 = row.getCell(2);
							if (record.getR48_NV_LONG() != null) {
							    cell1.setCellValue(record.getR48_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR48_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR48_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR48_FV_LONG() != null) {
							    cell1.setCellValue(record.getR48_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR48_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR48_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR48_QFHA() != null) {
							    cell1.setCellValue(record.getR48_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW48--------- R49
							row = sheet.getRow(48);

							cell1 = row.getCell(2);
							if (record.getR49_NV_LONG() != null) {
							    cell1.setCellValue(record.getR49_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR49_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR49_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR49_FV_LONG() != null) {
							    cell1.setCellValue(record.getR49_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR49_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR49_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR49_QFHA() != null) {
							    cell1.setCellValue(record.getR49_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW49--------- R50
							row = sheet.getRow(49);

							cell1 = row.getCell(2);
							if (record.getR50_NV_LONG() != null) {
							    cell1.setCellValue(record.getR50_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR50_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR50_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR50_FV_LONG() != null) {
							    cell1.setCellValue(record.getR50_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR50_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR50_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR50_QFHA() != null) {
							    cell1.setCellValue(record.getR50_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
							//------ROW50--------- R51
							row = sheet.getRow(50);

							cell1 = row.getCell(2);
							if (record.getR51_NV_LONG() != null) {
							    cell1.setCellValue(record.getR51_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR51_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR51_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR51_FV_LONG() != null) {
							    cell1.setCellValue(record.getR51_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR51_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR51_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR51_QFHA() != null) {
							    cell1.setCellValue(record.getR51_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
							
							
							//------ROW51--------- R52
							row = sheet.getRow(51);

							cell1 = row.getCell(2);
							if (record.getR52_NV_LONG() != null) {
							    cell1.setCellValue(record.getR52_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR52_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR52_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR52_FV_LONG() != null) {
							    cell1.setCellValue(record.getR52_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR52_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR52_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR52_QFHA() != null) {
							    cell1.setCellValue(record.getR52_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							
							//------ROW52--------- R53
							row = sheet.getRow(52);

							cell1 = row.getCell(2);
							if (record.getR53_NV_LONG() != null) {
							    cell1.setCellValue(record.getR53_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR53_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR53_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR53_FV_LONG() != null) {
							    cell1.setCellValue(record.getR53_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR53_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR53_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR53_QFHA() != null) {
							    cell1.setCellValue(record.getR53_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							
							//------ROW53--------- R54
							row = sheet.getRow(53);

							cell1 = row.getCell(2);
							if (record.getR54_NV_LONG() != null) {
							    cell1.setCellValue(record.getR54_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR54_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR54_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR54_FV_LONG() != null) {
							    cell1.setCellValue(record.getR54_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR54_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR54_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR54_QFHA() != null) {
							    cell1.setCellValue(record.getR54_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							
							//------ROW54--------- R55
							row = sheet.getRow(54);

							cell1 = row.getCell(2);
							if (record.getR55_NV_LONG() != null) {
							    cell1.setCellValue(record.getR55_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR55_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR55_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR55_FV_LONG() != null) {
							    cell1.setCellValue(record.getR55_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR55_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR55_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR55_QFHA() != null) {
							    cell1.setCellValue(record.getR55_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_TBS SUMMARY", null, "BRRS_M_TBS_SUMMARYTABLE");
					}
					return out.toByteArray();
				}
			}
		}
	}

	// Normal Email Excel
	public byte[] BRRS_M_TBSEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");
		
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_TBSArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_TBSResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} 
		else {
		List<M_TBS_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_TBS report. Returning empty result.");
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
					M_TBS_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					//REPORT_DATE
					row = sheet.getRow(6);
					Cell cell1 = row.getCell(1);
					if (cell1 == null) {
					    cell1 = row.createCell(1);
					}

					if (record.getReportDate() != null) {
					    cell1.setCellValue(record.getReportDate()); // java.util.Date
					    cell1.setCellStyle(dateStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					
					// row12
					// Column B
					row = sheet.getRow(10);
					 cell1 = row.getCell(2);
					if (record.getR11_NV_LONG() != null) {
						cell1.setCellValue(record.getR11_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR11_NV_SHORT() != null) {
						cell1.setCellValue(record.getR11_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

				    cell1 = row.getCell(4);
					if (record.getR11_FV_LONG() != null) {
						cell1.setCellValue(record.getR11_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR11_FV_SHORT() != null) {
						cell1.setCellValue(record.getR11_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR11_QFHA() != null) {
						cell1.setCellValue(record.getR11_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
//------ROW11---------						
					row=sheet.getRow(11);
				    cell1 = row.getCell(2);
					if (record.getR12_NV_LONG() != null) {
						cell1.setCellValue(record.getR12_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR12_NV_SHORT() != null) {
						cell1.setCellValue(record.getR12_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR12_FV_LONG() != null) {
						cell1.setCellValue(record.getR12_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR12_FV_SHORT() != null) {
						cell1.setCellValue(record.getR12_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR12_QFHA() != null) {
						cell1.setCellValue(record.getR12_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					//------ROW12---------						
					row=sheet.getRow(12);
				    cell1 = row.getCell(2);
					if (record.getR13_NV_LONG() != null) {
						cell1.setCellValue(record.getR13_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR13_NV_SHORT() != null) {
						cell1.setCellValue(record.getR13_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR13_FV_LONG() != null) {
						cell1.setCellValue(record.getR13_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR13_FV_SHORT() != null) {
						cell1.setCellValue(record.getR13_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR13_QFHA() != null) {
						cell1.setCellValue(record.getR13_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	

					//------ROW13---------
					row = sheet.getRow(13);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR14_NV_LONG() != null) {
					    cell1.setCellValue(record.getR14_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR14_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR14_FV_LONG() != null) {
					    cell1.setCellValue(record.getR14_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR14_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR14_QFHA() != null) {
					    cell1.setCellValue(record.getR14_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW14---------
					row = sheet.getRow(14);

					cell1 = row.getCell(2);
					if (record.getR15_NV_LONG() != null) {
					    cell1.setCellValue(record.getR15_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR15_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR15_FV_LONG() != null) {
					    cell1.setCellValue(record.getR15_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR15_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR15_QFHA() != null) {
					    cell1.setCellValue(record.getR15_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW15---------
					row = sheet.getRow(15);

					cell1 = row.getCell(2);
					if (record.getR16_NV_LONG() != null) {
					    cell1.setCellValue(record.getR16_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR16_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR16_FV_LONG() != null) {
					    cell1.setCellValue(record.getR16_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR16_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR16_QFHA() != null) {
					    cell1.setCellValue(record.getR16_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW16---------
					row = sheet.getRow(16);

					cell1 = row.getCell(2);
					if (record.getR17_NV_LONG() != null) {
					    cell1.setCellValue(record.getR17_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR17_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR17_FV_LONG() != null) {
					    cell1.setCellValue(record.getR17_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR17_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR17_QFHA() != null) {
					    cell1.setCellValue(record.getR17_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW17---------
					row = sheet.getRow(17);

					cell1 = row.getCell(2);
					if (record.getR18_NV_LONG() != null) {
					    cell1.setCellValue(record.getR18_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR18_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR18_FV_LONG() != null) {
					    cell1.setCellValue(record.getR18_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR18_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR18_QFHA() != null) {
					    cell1.setCellValue(record.getR18_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW18---------
					row = sheet.getRow(18);

					cell1 = row.getCell(2);
					if (record.getR19_NV_LONG() != null) {
					    cell1.setCellValue(record.getR19_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR19_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR19_FV_LONG() != null) {
					    cell1.setCellValue(record.getR19_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR19_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR19_QFHA() != null) {
					    cell1.setCellValue(record.getR19_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW19---------
					row = sheet.getRow(19);

					cell1 = row.getCell(2);
					if (record.getR20_NV_LONG() != null) {
					    cell1.setCellValue(record.getR20_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR20_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR20_FV_LONG() != null) {
					    cell1.setCellValue(record.getR20_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR20_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR20_QFHA() != null) {
					    cell1.setCellValue(record.getR20_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW20---------
					row = sheet.getRow(20);

					cell1 = row.getCell(2);
					if (record.getR21_NV_LONG() != null) {
					    cell1.setCellValue(record.getR21_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR21_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR21_FV_LONG() != null) {
					    cell1.setCellValue(record.getR21_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR21_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR21_QFHA() != null) {
					    cell1.setCellValue(record.getR21_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW21---------
					row = sheet.getRow(21);

					cell1 = row.getCell(2);
					if (record.getR22_NV_LONG() != null) {
					    cell1.setCellValue(record.getR22_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR22_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR22_FV_LONG() != null) {
					    cell1.setCellValue(record.getR22_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR22_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR22_QFHA() != null) {
					    cell1.setCellValue(record.getR22_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW22---------
					row = sheet.getRow(22);

					cell1 = row.getCell(2);
					if (record.getR23_NV_LONG() != null) {
					    cell1.setCellValue(record.getR23_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR23_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR23_FV_LONG() != null) {
					    cell1.setCellValue(record.getR23_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR23_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR23_QFHA() != null) {
					    cell1.setCellValue(record.getR23_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW23---------
					row = sheet.getRow(23);

					cell1 = row.getCell(2);
					if (record.getR24_NV_LONG() != null) {
					    cell1.setCellValue(record.getR24_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR24_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR24_FV_LONG() != null) {
					    cell1.setCellValue(record.getR24_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR24_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR24_QFHA() != null) {
					    cell1.setCellValue(record.getR24_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW24---------
					row = sheet.getRow(24);

					cell1 = row.getCell(2);
					if (record.getR25_NV_LONG() != null) {
					    cell1.setCellValue(record.getR25_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR25_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR25_FV_LONG() != null) {
					    cell1.setCellValue(record.getR25_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR25_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR25_QFHA() != null) {
					    cell1.setCellValue(record.getR25_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW25---------
					row = sheet.getRow(25);

					cell1 = row.getCell(2);
					if (record.getR26_NV_LONG() != null) {
					    cell1.setCellValue(record.getR26_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR26_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR26_FV_LONG() != null) {
					    cell1.setCellValue(record.getR26_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR26_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR26_QFHA() != null) {
					    cell1.setCellValue(record.getR26_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW26---------
					row = sheet.getRow(26);

					cell1 = row.getCell(2);
					if (record.getR27_NV_LONG() != null) {
					    cell1.setCellValue(record.getR27_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR27_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR27_FV_LONG() != null) {
					    cell1.setCellValue(record.getR27_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR27_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR27_QFHA() != null) {
					    cell1.setCellValue(record.getR27_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW27---------
					row = sheet.getRow(27);

					cell1 = row.getCell(2);
					if (record.getR28_NV_LONG() != null) {
					    cell1.setCellValue(record.getR28_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR28_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR28_FV_LONG() != null) {
					    cell1.setCellValue(record.getR28_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR28_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR28_QFHA() != null) {
					    cell1.setCellValue(record.getR28_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW28---------
					row = sheet.getRow(28);

					cell1 = row.getCell(2);
					if (record.getR29_NV_LONG() != null) {
					    cell1.setCellValue(record.getR29_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR29_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR29_FV_LONG() != null) {
					    cell1.setCellValue(record.getR29_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR29_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR29_QFHA() != null) {
					    cell1.setCellValue(record.getR29_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW29---------
					row = sheet.getRow(29);

					cell1 = row.getCell(2);
					if (record.getR30_NV_LONG() != null) {
					    cell1.setCellValue(record.getR30_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR30_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR30_FV_LONG() != null) {
					    cell1.setCellValue(record.getR30_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR30_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR30_QFHA() != null) {
					    cell1.setCellValue(record.getR30_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW30--------- R31
					row = sheet.getRow(30);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR31_NV_LONG() != null) {
					    cell1.setCellValue(record.getR31_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR31_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR31_FV_LONG() != null) {
					    cell1.setCellValue(record.getR31_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR31_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR31_QFHA() != null) {
					    cell1.setCellValue(record.getR31_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW31--------- R32
					row = sheet.getRow(31);

					cell1 = row.getCell(2);
					if (record.getR32_NV_LONG() != null) {
					    cell1.setCellValue(record.getR32_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR32_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR32_FV_LONG() != null) {
					    cell1.setCellValue(record.getR32_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR32_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR32_QFHA() != null) {
					    cell1.setCellValue(record.getR32_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW32--------- R33
					row = sheet.getRow(32);

					cell1 = row.getCell(2);
					if (record.getR33_NV_LONG() != null) {
					    cell1.setCellValue(record.getR33_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR33_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR33_FV_LONG() != null) {
					    cell1.setCellValue(record.getR33_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR33_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR33_QFHA() != null) {
					    cell1.setCellValue(record.getR33_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW33--------- R34
					row = sheet.getRow(33);

					cell1 = row.getCell(2);
					if (record.getR34_NV_LONG() != null) {
					    cell1.setCellValue(record.getR34_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR34_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR34_FV_LONG() != null) {
					    cell1.setCellValue(record.getR34_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR34_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR34_QFHA() != null) {
					    cell1.setCellValue(record.getR34_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW34--------- R35
					row = sheet.getRow(34);

					cell1 = row.getCell(2);
					if (record.getR35_NV_LONG() != null) {
					    cell1.setCellValue(record.getR35_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR35_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR35_FV_LONG() != null) {
					    cell1.setCellValue(record.getR35_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR35_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR35_QFHA() != null) {
					    cell1.setCellValue(record.getR35_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW35--------- R36
					row = sheet.getRow(35);

					cell1 = row.getCell(2);
					if (record.getR36_NV_LONG() != null) {
					    cell1.setCellValue(record.getR36_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR36_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR36_FV_LONG() != null) {
					    cell1.setCellValue(record.getR36_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR36_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR36_QFHA() != null) {
					    cell1.setCellValue(record.getR36_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW36--------- R37
					row = sheet.getRow(36);

					cell1 = row.getCell(2);
					if (record.getR37_NV_LONG() != null) {
					    cell1.setCellValue(record.getR37_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR37_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR37_FV_LONG() != null) {
					    cell1.setCellValue(record.getR37_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR37_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR37_QFHA() != null) {
					    cell1.setCellValue(record.getR37_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW37--------- R38
					row = sheet.getRow(37);

					cell1 = row.getCell(2);
					if (record.getR38_NV_LONG() != null) {
					    cell1.setCellValue(record.getR38_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR38_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR38_FV_LONG() != null) {
					    cell1.setCellValue(record.getR38_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR38_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR38_QFHA() != null) {
					    cell1.setCellValue(record.getR38_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW38--------- R39
					row = sheet.getRow(38);

					cell1 = row.getCell(2);
					if (record.getR39_NV_LONG() != null) {
					    cell1.setCellValue(record.getR39_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR39_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR39_FV_LONG() != null) {
					    cell1.setCellValue(record.getR39_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR39_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR39_QFHA() != null) {
					    cell1.setCellValue(record.getR39_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW39--------- R40
					row = sheet.getRow(39);

					cell1 = row.getCell(2);
					if (record.getR40_NV_LONG() != null) {
					    cell1.setCellValue(record.getR40_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR40_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR40_FV_LONG() != null) {
					    cell1.setCellValue(record.getR40_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR40_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR40_QFHA() != null) {
					    cell1.setCellValue(record.getR40_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					//------ROW40--------- R41
					row = sheet.getRow(40);

					cell1 = row.getCell(2);
					if (record.getR41_NV_LONG() != null) {
					    cell1.setCellValue(record.getR41_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR41_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR41_FV_LONG() != null) {
					    cell1.setCellValue(record.getR41_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR41_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR41_QFHA() != null) {
					    cell1.setCellValue(record.getR41_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW41--------- R42
					row = sheet.getRow(41);

					cell1 = row.getCell(2);
					if (record.getR42_NV_LONG() != null) {
					    cell1.setCellValue(record.getR42_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR42_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR42_FV_LONG() != null) {
					    cell1.setCellValue(record.getR42_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR42_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR42_QFHA() != null) {
					    cell1.setCellValue(record.getR42_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW42--------- R43
					row = sheet.getRow(42);

					cell1 = row.getCell(2);
					if (record.getR43_NV_LONG() != null) {
					    cell1.setCellValue(record.getR43_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR43_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR43_FV_LONG() != null) {
					    cell1.setCellValue(record.getR43_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR43_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR43_QFHA() != null) {
					    cell1.setCellValue(record.getR43_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW43--------- R44
					row = sheet.getRow(43);

					cell1 = row.getCell(2);
					if (record.getR44_NV_LONG() != null) {
					    cell1.setCellValue(record.getR44_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR44_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR44_FV_LONG() != null) {
					    cell1.setCellValue(record.getR44_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR44_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR44_QFHA() != null) {
					    cell1.setCellValue(record.getR44_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW44--------- R45
					row = sheet.getRow(44);

					cell1 = row.getCell(2);
					if (record.getR45_NV_LONG() != null) {
					    cell1.setCellValue(record.getR45_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR45_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR45_FV_LONG() != null) {
					    cell1.setCellValue(record.getR45_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR45_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR45_QFHA() != null) {
					    cell1.setCellValue(record.getR45_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW45--------- R46
					row = sheet.getRow(45);

					cell1 = row.getCell(2);
					if (record.getR46_NV_LONG() != null) {
					    cell1.setCellValue(record.getR46_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR46_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR46_FV_LONG() != null) {
					    cell1.setCellValue(record.getR46_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR46_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR46_QFHA() != null) {
					    cell1.setCellValue(record.getR46_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW46--------- R47
					row = sheet.getRow(46);

					cell1 = row.getCell(2);
					if (record.getR47_NV_LONG() != null) {
					    cell1.setCellValue(record.getR47_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR47_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR47_FV_LONG() != null) {
					    cell1.setCellValue(record.getR47_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR47_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR47_QFHA() != null) {
					    cell1.setCellValue(record.getR47_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW47--------- R48
					row = sheet.getRow(47);

					cell1 = row.getCell(2);
					if (record.getR48_NV_LONG() != null) {
					    cell1.setCellValue(record.getR48_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR48_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR48_FV_LONG() != null) {
					    cell1.setCellValue(record.getR48_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR48_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR48_QFHA() != null) {
					    cell1.setCellValue(record.getR48_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW48--------- R49
					row = sheet.getRow(48);

					cell1 = row.getCell(2);
					if (record.getR49_NV_LONG() != null) {
					    cell1.setCellValue(record.getR49_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR49_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR49_FV_LONG() != null) {
					    cell1.setCellValue(record.getR49_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR49_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR49_QFHA() != null) {
					    cell1.setCellValue(record.getR49_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW49--------- R50
					row = sheet.getRow(49);

					cell1 = row.getCell(2);
					if (record.getR50_NV_LONG() != null) {
					    cell1.setCellValue(record.getR50_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR50_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR50_FV_LONG() != null) {
					    cell1.setCellValue(record.getR50_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR50_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR50_QFHA() != null) {
					    cell1.setCellValue(record.getR50_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					//------ROW50--------- R51
					row = sheet.getRow(50);

					cell1 = row.getCell(2);
					if (record.getR51_NV_LONG() != null) {
					    cell1.setCellValue(record.getR51_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR51_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR51_FV_LONG() != null) {
					    cell1.setCellValue(record.getR51_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR51_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR51_QFHA() != null) {
					    cell1.setCellValue(record.getR51_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					
					
					//------ROW51--------- R52
					row = sheet.getRow(51);

					cell1 = row.getCell(2);
					if (record.getR52_NV_LONG() != null) {
					    cell1.setCellValue(record.getR52_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR52_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR52_FV_LONG() != null) {
					    cell1.setCellValue(record.getR52_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR52_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR52_QFHA() != null) {
					    cell1.setCellValue(record.getR52_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW52--------- R53
					row = sheet.getRow(52);

					cell1 = row.getCell(2);
					if (record.getR53_NV_LONG() != null) {
					    cell1.setCellValue(record.getR53_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR53_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR53_FV_LONG() != null) {
					    cell1.setCellValue(record.getR53_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR53_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR53_QFHA() != null) {
					    cell1.setCellValue(record.getR53_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW53--------- R54
					row = sheet.getRow(53);

					cell1 = row.getCell(2);
					if (record.getR54_NV_LONG() != null) {
					    cell1.setCellValue(record.getR54_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR54_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR54_FV_LONG() != null) {
					    cell1.setCellValue(record.getR54_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR54_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR54_QFHA() != null) {
					    cell1.setCellValue(record.getR54_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW54--------- R55
					row = sheet.getRow(54);

					cell1 = row.getCell(2);
					if (record.getR55_NV_LONG() != null) {
					    cell1.setCellValue(record.getR55_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR55_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR55_FV_LONG() != null) {
					    cell1.setCellValue(record.getR55_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR55_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR55_QFHA() != null) {
					    cell1.setCellValue(record.getR55_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_TBS EMAIL SUMMARY", null, "BRRS_M_TBS_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
		}
	}

	// Archival format excel
	public byte[] getExcelM_TBSARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_TBSArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} 

		List<M_TBS_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_TBS report. Returning empty result.");
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
					M_TBS_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					//REPORT_DATE
					row = sheet.getRow(6);
					Cell cell1 = row.getCell(1);
					if (cell1 == null) {
					    cell1 = row.createCell(1);
					}

					if (record.getReportDate() != null) {
					    cell1.setCellValue(record.getReportDate()); // java.util.Date
					    cell1.setCellStyle(dateStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					
					// row12
					// Column B
					row = sheet.getRow(10);

					 cell1 = row.getCell(2);
					if (record.getR11_NV_LONG() != null) {
						cell1.setCellValue(record.getR11_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR11_NV_SHORT() != null) {
						cell1.setCellValue(record.getR11_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

				    cell1 = row.getCell(4);
					if (record.getR11_FV_LONG() != null) {
						cell1.setCellValue(record.getR11_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR11_FV_SHORT() != null) {
						cell1.setCellValue(record.getR11_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR11_QFHA() != null) {
						cell1.setCellValue(record.getR11_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
//------ROW11---------						
					row=sheet.getRow(11);
				    cell1 = row.getCell(2);
					if (record.getR12_NV_LONG() != null) {
						cell1.setCellValue(record.getR12_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR12_NV_SHORT() != null) {
						cell1.setCellValue(record.getR12_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR12_FV_LONG() != null) {
						cell1.setCellValue(record.getR12_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR12_FV_SHORT() != null) {
						cell1.setCellValue(record.getR12_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR12_QFHA() != null) {
						cell1.setCellValue(record.getR12_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					//------ROW12---------						
					row=sheet.getRow(12);
				    cell1 = row.getCell(2);
					if (record.getR13_NV_LONG() != null) {
						cell1.setCellValue(record.getR13_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR13_NV_SHORT() != null) {
						cell1.setCellValue(record.getR13_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR13_FV_LONG() != null) {
						cell1.setCellValue(record.getR13_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR13_FV_SHORT() != null) {
						cell1.setCellValue(record.getR13_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR13_QFHA() != null) {
						cell1.setCellValue(record.getR13_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	

					//------ROW13---------
					row = sheet.getRow(13);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR14_NV_LONG() != null) {
					    cell1.setCellValue(record.getR14_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR14_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR14_FV_LONG() != null) {
					    cell1.setCellValue(record.getR14_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR14_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR14_QFHA() != null) {
					    cell1.setCellValue(record.getR14_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW14---------
					row = sheet.getRow(14);

					cell1 = row.getCell(2);
					if (record.getR15_NV_LONG() != null) {
					    cell1.setCellValue(record.getR15_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR15_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR15_FV_LONG() != null) {
					    cell1.setCellValue(record.getR15_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR15_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR15_QFHA() != null) {
					    cell1.setCellValue(record.getR15_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW15---------
					row = sheet.getRow(15);

					cell1 = row.getCell(2);
					if (record.getR16_NV_LONG() != null) {
					    cell1.setCellValue(record.getR16_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR16_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR16_FV_LONG() != null) {
					    cell1.setCellValue(record.getR16_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR16_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR16_QFHA() != null) {
					    cell1.setCellValue(record.getR16_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW16---------
					row = sheet.getRow(16);

					cell1 = row.getCell(2);
					if (record.getR17_NV_LONG() != null) {
					    cell1.setCellValue(record.getR17_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR17_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR17_FV_LONG() != null) {
					    cell1.setCellValue(record.getR17_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR17_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR17_QFHA() != null) {
					    cell1.setCellValue(record.getR17_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW17---------
					row = sheet.getRow(17);

					cell1 = row.getCell(2);
					if (record.getR18_NV_LONG() != null) {
					    cell1.setCellValue(record.getR18_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR18_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR18_FV_LONG() != null) {
					    cell1.setCellValue(record.getR18_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR18_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR18_QFHA() != null) {
					    cell1.setCellValue(record.getR18_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW18---------
					row = sheet.getRow(18);

					cell1 = row.getCell(2);
					if (record.getR19_NV_LONG() != null) {
					    cell1.setCellValue(record.getR19_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR19_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR19_FV_LONG() != null) {
					    cell1.setCellValue(record.getR19_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR19_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR19_QFHA() != null) {
					    cell1.setCellValue(record.getR19_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW19---------
					row = sheet.getRow(19);

					cell1 = row.getCell(2);
					if (record.getR20_NV_LONG() != null) {
					    cell1.setCellValue(record.getR20_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR20_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR20_FV_LONG() != null) {
					    cell1.setCellValue(record.getR20_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR20_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR20_QFHA() != null) {
					    cell1.setCellValue(record.getR20_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW20---------
					row = sheet.getRow(20);

					cell1 = row.getCell(2);
					if (record.getR21_NV_LONG() != null) {
					    cell1.setCellValue(record.getR21_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR21_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR21_FV_LONG() != null) {
					    cell1.setCellValue(record.getR21_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR21_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR21_QFHA() != null) {
					    cell1.setCellValue(record.getR21_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW21---------
					row = sheet.getRow(21);

					cell1 = row.getCell(2);
					if (record.getR22_NV_LONG() != null) {
					    cell1.setCellValue(record.getR22_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR22_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR22_FV_LONG() != null) {
					    cell1.setCellValue(record.getR22_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR22_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR22_QFHA() != null) {
					    cell1.setCellValue(record.getR22_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW22---------
					row = sheet.getRow(22);

					cell1 = row.getCell(2);
					if (record.getR23_NV_LONG() != null) {
					    cell1.setCellValue(record.getR23_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR23_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR23_FV_LONG() != null) {
					    cell1.setCellValue(record.getR23_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR23_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR23_QFHA() != null) {
					    cell1.setCellValue(record.getR23_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW23---------
					row = sheet.getRow(23);

					cell1 = row.getCell(2);
					if (record.getR24_NV_LONG() != null) {
					    cell1.setCellValue(record.getR24_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR24_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR24_FV_LONG() != null) {
					    cell1.setCellValue(record.getR24_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR24_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR24_QFHA() != null) {
					    cell1.setCellValue(record.getR24_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW24---------
					row = sheet.getRow(24);

					cell1 = row.getCell(2);
					if (record.getR25_NV_LONG() != null) {
					    cell1.setCellValue(record.getR25_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR25_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR25_FV_LONG() != null) {
					    cell1.setCellValue(record.getR25_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR25_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR25_QFHA() != null) {
					    cell1.setCellValue(record.getR25_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW25---------
					row = sheet.getRow(25);

					cell1 = row.getCell(2);
					if (record.getR26_NV_LONG() != null) {
					    cell1.setCellValue(record.getR26_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR26_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR26_FV_LONG() != null) {
					    cell1.setCellValue(record.getR26_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR26_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR26_QFHA() != null) {
					    cell1.setCellValue(record.getR26_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW26---------
					row = sheet.getRow(26);

					cell1 = row.getCell(2);
					if (record.getR27_NV_LONG() != null) {
					    cell1.setCellValue(record.getR27_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR27_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR27_FV_LONG() != null) {
					    cell1.setCellValue(record.getR27_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR27_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR27_QFHA() != null) {
					    cell1.setCellValue(record.getR27_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW27---------
					row = sheet.getRow(27);

					cell1 = row.getCell(2);
					if (record.getR28_NV_LONG() != null) {
					    cell1.setCellValue(record.getR28_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR28_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR28_FV_LONG() != null) {
					    cell1.setCellValue(record.getR28_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR28_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR28_QFHA() != null) {
					    cell1.setCellValue(record.getR28_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW28---------
					row = sheet.getRow(28);

					cell1 = row.getCell(2);
					if (record.getR29_NV_LONG() != null) {
					    cell1.setCellValue(record.getR29_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR29_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR29_FV_LONG() != null) {
					    cell1.setCellValue(record.getR29_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR29_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR29_QFHA() != null) {
					    cell1.setCellValue(record.getR29_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW29---------
					row = sheet.getRow(29);

					cell1 = row.getCell(2);
					if (record.getR30_NV_LONG() != null) {
					    cell1.setCellValue(record.getR30_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR30_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR30_FV_LONG() != null) {
					    cell1.setCellValue(record.getR30_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR30_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR30_QFHA() != null) {
					    cell1.setCellValue(record.getR30_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW30--------- R31
					row = sheet.getRow(30);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR31_NV_LONG() != null) {
					    cell1.setCellValue(record.getR31_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR31_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR31_FV_LONG() != null) {
					    cell1.setCellValue(record.getR31_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR31_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR31_QFHA() != null) {
					    cell1.setCellValue(record.getR31_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW31--------- R32
					row = sheet.getRow(31);

					cell1 = row.getCell(2);
					if (record.getR32_NV_LONG() != null) {
					    cell1.setCellValue(record.getR32_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR32_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR32_FV_LONG() != null) {
					    cell1.setCellValue(record.getR32_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR32_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR32_QFHA() != null) {
					    cell1.setCellValue(record.getR32_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW32--------- R33
					row = sheet.getRow(32);

					cell1 = row.getCell(2);
					if (record.getR33_NV_LONG() != null) {
					    cell1.setCellValue(record.getR33_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR33_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR33_FV_LONG() != null) {
					    cell1.setCellValue(record.getR33_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR33_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR33_QFHA() != null) {
					    cell1.setCellValue(record.getR33_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW33--------- R34
					row = sheet.getRow(33);

					cell1 = row.getCell(2);
					if (record.getR34_NV_LONG() != null) {
					    cell1.setCellValue(record.getR34_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR34_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR34_FV_LONG() != null) {
					    cell1.setCellValue(record.getR34_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR34_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR34_QFHA() != null) {
					    cell1.setCellValue(record.getR34_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW34--------- R35
					row = sheet.getRow(34);

					cell1 = row.getCell(2);
					if (record.getR35_NV_LONG() != null) {
					    cell1.setCellValue(record.getR35_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR35_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR35_FV_LONG() != null) {
					    cell1.setCellValue(record.getR35_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR35_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR35_QFHA() != null) {
					    cell1.setCellValue(record.getR35_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW35--------- R36
					row = sheet.getRow(35);

					cell1 = row.getCell(2);
					if (record.getR36_NV_LONG() != null) {
					    cell1.setCellValue(record.getR36_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR36_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR36_FV_LONG() != null) {
					    cell1.setCellValue(record.getR36_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR36_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR36_QFHA() != null) {
					    cell1.setCellValue(record.getR36_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW36--------- R37
					row = sheet.getRow(36);

					cell1 = row.getCell(2);
					if (record.getR37_NV_LONG() != null) {
					    cell1.setCellValue(record.getR37_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR37_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR37_FV_LONG() != null) {
					    cell1.setCellValue(record.getR37_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR37_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR37_QFHA() != null) {
					    cell1.setCellValue(record.getR37_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW37--------- R38
					row = sheet.getRow(37);

					cell1 = row.getCell(2);
					if (record.getR38_NV_LONG() != null) {
					    cell1.setCellValue(record.getR38_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR38_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR38_FV_LONG() != null) {
					    cell1.setCellValue(record.getR38_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR38_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR38_QFHA() != null) {
					    cell1.setCellValue(record.getR38_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW38--------- R39
					row = sheet.getRow(38);

					cell1 = row.getCell(2);
					if (record.getR39_NV_LONG() != null) {
					    cell1.setCellValue(record.getR39_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR39_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR39_FV_LONG() != null) {
					    cell1.setCellValue(record.getR39_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR39_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR39_QFHA() != null) {
					    cell1.setCellValue(record.getR39_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW39--------- R40
					row = sheet.getRow(39);

					cell1 = row.getCell(2);
					if (record.getR40_NV_LONG() != null) {
					    cell1.setCellValue(record.getR40_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR40_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR40_FV_LONG() != null) {
					    cell1.setCellValue(record.getR40_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR40_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR40_QFHA() != null) {
					    cell1.setCellValue(record.getR40_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					//------ROW40--------- R41
					row = sheet.getRow(40);

					cell1 = row.getCell(2);
					if (record.getR41_NV_LONG() != null) {
					    cell1.setCellValue(record.getR41_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR41_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR41_FV_LONG() != null) {
					    cell1.setCellValue(record.getR41_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR41_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR41_QFHA() != null) {
					    cell1.setCellValue(record.getR41_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW41--------- R42
					row = sheet.getRow(41);

					cell1 = row.getCell(2);
					if (record.getR42_NV_LONG() != null) {
					    cell1.setCellValue(record.getR42_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR42_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR42_FV_LONG() != null) {
					    cell1.setCellValue(record.getR42_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR42_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR42_QFHA() != null) {
					    cell1.setCellValue(record.getR42_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW42--------- R43
					row = sheet.getRow(42);

					cell1 = row.getCell(2);
					if (record.getR43_NV_LONG() != null) {
					    cell1.setCellValue(record.getR43_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR43_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR43_FV_LONG() != null) {
					    cell1.setCellValue(record.getR43_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR43_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR43_QFHA() != null) {
					    cell1.setCellValue(record.getR43_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW43--------- R44
					row = sheet.getRow(43);

					cell1 = row.getCell(2);
					if (record.getR44_NV_LONG() != null) {
					    cell1.setCellValue(record.getR44_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR44_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR44_FV_LONG() != null) {
					    cell1.setCellValue(record.getR44_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR44_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR44_QFHA() != null) {
					    cell1.setCellValue(record.getR44_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW44--------- R45
					row = sheet.getRow(44);

					cell1 = row.getCell(2);
					if (record.getR45_NV_LONG() != null) {
					    cell1.setCellValue(record.getR45_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR45_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR45_FV_LONG() != null) {
					    cell1.setCellValue(record.getR45_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR45_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR45_QFHA() != null) {
					    cell1.setCellValue(record.getR45_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW45--------- R46
					row = sheet.getRow(45);

					cell1 = row.getCell(2);
					if (record.getR46_NV_LONG() != null) {
					    cell1.setCellValue(record.getR46_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR46_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR46_FV_LONG() != null) {
					    cell1.setCellValue(record.getR46_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR46_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR46_QFHA() != null) {
					    cell1.setCellValue(record.getR46_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW46--------- R47
					row = sheet.getRow(46);

					cell1 = row.getCell(2);
					if (record.getR47_NV_LONG() != null) {
					    cell1.setCellValue(record.getR47_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR47_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR47_FV_LONG() != null) {
					    cell1.setCellValue(record.getR47_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR47_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR47_QFHA() != null) {
					    cell1.setCellValue(record.getR47_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW47--------- R48
					row = sheet.getRow(47);

					cell1 = row.getCell(2);
					if (record.getR48_NV_LONG() != null) {
					    cell1.setCellValue(record.getR48_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR48_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR48_FV_LONG() != null) {
					    cell1.setCellValue(record.getR48_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR48_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR48_QFHA() != null) {
					    cell1.setCellValue(record.getR48_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW48--------- R49
					row = sheet.getRow(48);

					cell1 = row.getCell(2);
					if (record.getR49_NV_LONG() != null) {
					    cell1.setCellValue(record.getR49_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR49_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR49_FV_LONG() != null) {
					    cell1.setCellValue(record.getR49_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR49_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR49_QFHA() != null) {
					    cell1.setCellValue(record.getR49_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW49--------- R50
					row = sheet.getRow(49);

					cell1 = row.getCell(2);
					if (record.getR50_NV_LONG() != null) {
					    cell1.setCellValue(record.getR50_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR50_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR50_FV_LONG() != null) {
					    cell1.setCellValue(record.getR50_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR50_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR50_QFHA() != null) {
					    cell1.setCellValue(record.getR50_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					//------ROW50--------- R51
					row = sheet.getRow(50);

					cell1 = row.getCell(2);
					if (record.getR51_NV_LONG() != null) {
					    cell1.setCellValue(record.getR51_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR51_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR51_FV_LONG() != null) {
					    cell1.setCellValue(record.getR51_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR51_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR51_QFHA() != null) {
					    cell1.setCellValue(record.getR51_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					
					
					//------ROW51--------- R52
					row = sheet.getRow(51);

					cell1 = row.getCell(2);
					if (record.getR52_NV_LONG() != null) {
					    cell1.setCellValue(record.getR52_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR52_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR52_FV_LONG() != null) {
					    cell1.setCellValue(record.getR52_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR52_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR52_QFHA() != null) {
					    cell1.setCellValue(record.getR52_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW52--------- R53
					row = sheet.getRow(52);

					cell1 = row.getCell(2);
					if (record.getR53_NV_LONG() != null) {
					    cell1.setCellValue(record.getR53_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR53_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR53_FV_LONG() != null) {
					    cell1.setCellValue(record.getR53_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR53_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR53_QFHA() != null) {
					    cell1.setCellValue(record.getR53_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW53--------- R54
					row = sheet.getRow(53);

					cell1 = row.getCell(2);
					if (record.getR54_NV_LONG() != null) {
					    cell1.setCellValue(record.getR54_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR54_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR54_FV_LONG() != null) {
					    cell1.setCellValue(record.getR54_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR54_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR54_QFHA() != null) {
					    cell1.setCellValue(record.getR54_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW54--------- R55
					row = sheet.getRow(54);

					cell1 = row.getCell(2);
					if (record.getR55_NV_LONG() != null) {
					    cell1.setCellValue(record.getR55_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR55_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR55_FV_LONG() != null) {
					    cell1.setCellValue(record.getR55_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR55_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR55_QFHA() != null) {
					    cell1.setCellValue(record.getR55_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_TBS ARCHIVAL SUMMARY", null, "BRRS_M_TBS_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}

	}

	// Archival Email Excel
	public byte[] BRRS_M_TBSArchivalEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_TBS_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_TBS report. Returning empty result.");
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
					M_TBS_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					//REPORT_DATE
					row = sheet.getRow(6);
					Cell cell1 = row.getCell(1);
					if (cell1 == null) {
					    cell1 = row.createCell(1);
					}

					if (record.getReportDate() != null) {
					    cell1.setCellValue(record.getReportDate()); // java.util.Date
					    cell1.setCellStyle(dateStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					
					// row12
					// Column B
					row = sheet.getRow(10);

					 cell1 = row.getCell(2);
					if (record.getR11_NV_LONG() != null) {
						cell1.setCellValue(record.getR11_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR11_NV_SHORT() != null) {
						cell1.setCellValue(record.getR11_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

				    cell1 = row.getCell(4);
					if (record.getR11_FV_LONG() != null) {
						cell1.setCellValue(record.getR11_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR11_FV_SHORT() != null) {
						cell1.setCellValue(record.getR11_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR11_QFHA() != null) {
						cell1.setCellValue(record.getR11_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
//------ROW11---------						
					row=sheet.getRow(11);
				    cell1 = row.getCell(2);
					if (record.getR12_NV_LONG() != null) {
						cell1.setCellValue(record.getR12_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR12_NV_SHORT() != null) {
						cell1.setCellValue(record.getR12_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR12_FV_LONG() != null) {
						cell1.setCellValue(record.getR12_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR12_FV_SHORT() != null) {
						cell1.setCellValue(record.getR12_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR12_QFHA() != null) {
						cell1.setCellValue(record.getR12_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					//------ROW12---------						
					row=sheet.getRow(12);
				    cell1 = row.getCell(2);
					if (record.getR13_NV_LONG() != null) {
						cell1.setCellValue(record.getR13_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR13_NV_SHORT() != null) {
						cell1.setCellValue(record.getR13_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR13_FV_LONG() != null) {
						cell1.setCellValue(record.getR13_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR13_FV_SHORT() != null) {
						cell1.setCellValue(record.getR13_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR13_QFHA() != null) {
						cell1.setCellValue(record.getR13_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	

					//------ROW13---------
					row = sheet.getRow(13);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR14_NV_LONG() != null) {
					    cell1.setCellValue(record.getR14_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR14_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR14_FV_LONG() != null) {
					    cell1.setCellValue(record.getR14_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR14_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR14_QFHA() != null) {
					    cell1.setCellValue(record.getR14_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW14---------
					row = sheet.getRow(14);

					cell1 = row.getCell(2);
					if (record.getR15_NV_LONG() != null) {
					    cell1.setCellValue(record.getR15_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR15_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR15_FV_LONG() != null) {
					    cell1.setCellValue(record.getR15_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR15_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR15_QFHA() != null) {
					    cell1.setCellValue(record.getR15_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW15---------
					row = sheet.getRow(15);

					cell1 = row.getCell(2);
					if (record.getR16_NV_LONG() != null) {
					    cell1.setCellValue(record.getR16_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR16_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR16_FV_LONG() != null) {
					    cell1.setCellValue(record.getR16_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR16_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR16_QFHA() != null) {
					    cell1.setCellValue(record.getR16_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW16---------
					row = sheet.getRow(16);

					cell1 = row.getCell(2);
					if (record.getR17_NV_LONG() != null) {
					    cell1.setCellValue(record.getR17_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR17_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR17_FV_LONG() != null) {
					    cell1.setCellValue(record.getR17_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR17_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR17_QFHA() != null) {
					    cell1.setCellValue(record.getR17_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW17---------
					row = sheet.getRow(17);

					cell1 = row.getCell(2);
					if (record.getR18_NV_LONG() != null) {
					    cell1.setCellValue(record.getR18_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR18_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR18_FV_LONG() != null) {
					    cell1.setCellValue(record.getR18_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR18_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR18_QFHA() != null) {
					    cell1.setCellValue(record.getR18_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW18---------
					row = sheet.getRow(18);

					cell1 = row.getCell(2);
					if (record.getR19_NV_LONG() != null) {
					    cell1.setCellValue(record.getR19_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR19_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR19_FV_LONG() != null) {
					    cell1.setCellValue(record.getR19_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR19_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR19_QFHA() != null) {
					    cell1.setCellValue(record.getR19_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW19---------
					row = sheet.getRow(19);

					cell1 = row.getCell(2);
					if (record.getR20_NV_LONG() != null) {
					    cell1.setCellValue(record.getR20_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR20_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR20_FV_LONG() != null) {
					    cell1.setCellValue(record.getR20_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR20_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR20_QFHA() != null) {
					    cell1.setCellValue(record.getR20_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW20---------
					row = sheet.getRow(20);

					cell1 = row.getCell(2);
					if (record.getR21_NV_LONG() != null) {
					    cell1.setCellValue(record.getR21_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR21_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR21_FV_LONG() != null) {
					    cell1.setCellValue(record.getR21_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR21_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR21_QFHA() != null) {
					    cell1.setCellValue(record.getR21_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW21---------
					row = sheet.getRow(21);

					cell1 = row.getCell(2);
					if (record.getR22_NV_LONG() != null) {
					    cell1.setCellValue(record.getR22_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR22_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR22_FV_LONG() != null) {
					    cell1.setCellValue(record.getR22_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR22_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR22_QFHA() != null) {
					    cell1.setCellValue(record.getR22_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW22---------
					row = sheet.getRow(22);

					cell1 = row.getCell(2);
					if (record.getR23_NV_LONG() != null) {
					    cell1.setCellValue(record.getR23_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR23_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR23_FV_LONG() != null) {
					    cell1.setCellValue(record.getR23_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR23_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR23_QFHA() != null) {
					    cell1.setCellValue(record.getR23_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW23---------
					row = sheet.getRow(23);

					cell1 = row.getCell(2);
					if (record.getR24_NV_LONG() != null) {
					    cell1.setCellValue(record.getR24_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR24_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR24_FV_LONG() != null) {
					    cell1.setCellValue(record.getR24_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR24_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR24_QFHA() != null) {
					    cell1.setCellValue(record.getR24_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW24---------
					row = sheet.getRow(24);

					cell1 = row.getCell(2);
					if (record.getR25_NV_LONG() != null) {
					    cell1.setCellValue(record.getR25_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR25_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR25_FV_LONG() != null) {
					    cell1.setCellValue(record.getR25_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR25_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR25_QFHA() != null) {
					    cell1.setCellValue(record.getR25_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW25---------
					row = sheet.getRow(25);

					cell1 = row.getCell(2);
					if (record.getR26_NV_LONG() != null) {
					    cell1.setCellValue(record.getR26_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR26_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR26_FV_LONG() != null) {
					    cell1.setCellValue(record.getR26_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR26_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR26_QFHA() != null) {
					    cell1.setCellValue(record.getR26_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW26---------
					row = sheet.getRow(26);

					cell1 = row.getCell(2);
					if (record.getR27_NV_LONG() != null) {
					    cell1.setCellValue(record.getR27_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR27_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR27_FV_LONG() != null) {
					    cell1.setCellValue(record.getR27_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR27_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR27_QFHA() != null) {
					    cell1.setCellValue(record.getR27_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW27---------
					row = sheet.getRow(27);

					cell1 = row.getCell(2);
					if (record.getR28_NV_LONG() != null) {
					    cell1.setCellValue(record.getR28_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR28_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR28_FV_LONG() != null) {
					    cell1.setCellValue(record.getR28_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR28_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR28_QFHA() != null) {
					    cell1.setCellValue(record.getR28_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW28---------
					row = sheet.getRow(28);

					cell1 = row.getCell(2);
					if (record.getR29_NV_LONG() != null) {
					    cell1.setCellValue(record.getR29_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR29_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR29_FV_LONG() != null) {
					    cell1.setCellValue(record.getR29_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR29_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR29_QFHA() != null) {
					    cell1.setCellValue(record.getR29_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW29---------
					row = sheet.getRow(29);

					cell1 = row.getCell(2);
					if (record.getR30_NV_LONG() != null) {
					    cell1.setCellValue(record.getR30_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR30_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR30_FV_LONG() != null) {
					    cell1.setCellValue(record.getR30_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR30_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR30_QFHA() != null) {
					    cell1.setCellValue(record.getR30_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW30--------- R31
					row = sheet.getRow(30);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR31_NV_LONG() != null) {
					    cell1.setCellValue(record.getR31_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR31_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR31_FV_LONG() != null) {
					    cell1.setCellValue(record.getR31_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR31_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR31_QFHA() != null) {
					    cell1.setCellValue(record.getR31_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW31--------- R32
					row = sheet.getRow(31);

					cell1 = row.getCell(2);
					if (record.getR32_NV_LONG() != null) {
					    cell1.setCellValue(record.getR32_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR32_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR32_FV_LONG() != null) {
					    cell1.setCellValue(record.getR32_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR32_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR32_QFHA() != null) {
					    cell1.setCellValue(record.getR32_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW32--------- R33
					row = sheet.getRow(32);

					cell1 = row.getCell(2);
					if (record.getR33_NV_LONG() != null) {
					    cell1.setCellValue(record.getR33_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR33_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR33_FV_LONG() != null) {
					    cell1.setCellValue(record.getR33_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR33_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR33_QFHA() != null) {
					    cell1.setCellValue(record.getR33_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW33--------- R34
					row = sheet.getRow(33);

					cell1 = row.getCell(2);
					if (record.getR34_NV_LONG() != null) {
					    cell1.setCellValue(record.getR34_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR34_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR34_FV_LONG() != null) {
					    cell1.setCellValue(record.getR34_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR34_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR34_QFHA() != null) {
					    cell1.setCellValue(record.getR34_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW34--------- R35
					row = sheet.getRow(34);

					cell1 = row.getCell(2);
					if (record.getR35_NV_LONG() != null) {
					    cell1.setCellValue(record.getR35_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR35_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR35_FV_LONG() != null) {
					    cell1.setCellValue(record.getR35_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR35_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR35_QFHA() != null) {
					    cell1.setCellValue(record.getR35_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW35--------- R36
					row = sheet.getRow(35);

					cell1 = row.getCell(2);
					if (record.getR36_NV_LONG() != null) {
					    cell1.setCellValue(record.getR36_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR36_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR36_FV_LONG() != null) {
					    cell1.setCellValue(record.getR36_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR36_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR36_QFHA() != null) {
					    cell1.setCellValue(record.getR36_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW36--------- R37
					row = sheet.getRow(36);

					cell1 = row.getCell(2);
					if (record.getR37_NV_LONG() != null) {
					    cell1.setCellValue(record.getR37_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR37_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR37_FV_LONG() != null) {
					    cell1.setCellValue(record.getR37_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR37_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR37_QFHA() != null) {
					    cell1.setCellValue(record.getR37_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW37--------- R38
					row = sheet.getRow(37);

					cell1 = row.getCell(2);
					if (record.getR38_NV_LONG() != null) {
					    cell1.setCellValue(record.getR38_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR38_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR38_FV_LONG() != null) {
					    cell1.setCellValue(record.getR38_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR38_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR38_QFHA() != null) {
					    cell1.setCellValue(record.getR38_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW38--------- R39
					row = sheet.getRow(38);

					cell1 = row.getCell(2);
					if (record.getR39_NV_LONG() != null) {
					    cell1.setCellValue(record.getR39_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR39_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR39_FV_LONG() != null) {
					    cell1.setCellValue(record.getR39_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR39_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR39_QFHA() != null) {
					    cell1.setCellValue(record.getR39_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW39--------- R40
					row = sheet.getRow(39);

					cell1 = row.getCell(2);
					if (record.getR40_NV_LONG() != null) {
					    cell1.setCellValue(record.getR40_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR40_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR40_FV_LONG() != null) {
					    cell1.setCellValue(record.getR40_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR40_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR40_QFHA() != null) {
					    cell1.setCellValue(record.getR40_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					//------ROW40--------- R41
					row = sheet.getRow(40);

					cell1 = row.getCell(2);
					if (record.getR41_NV_LONG() != null) {
					    cell1.setCellValue(record.getR41_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR41_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR41_FV_LONG() != null) {
					    cell1.setCellValue(record.getR41_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR41_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR41_QFHA() != null) {
					    cell1.setCellValue(record.getR41_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW41--------- R42
					row = sheet.getRow(41);

					cell1 = row.getCell(2);
					if (record.getR42_NV_LONG() != null) {
					    cell1.setCellValue(record.getR42_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR42_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR42_FV_LONG() != null) {
					    cell1.setCellValue(record.getR42_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR42_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR42_QFHA() != null) {
					    cell1.setCellValue(record.getR42_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW42--------- R43
					row = sheet.getRow(42);

					cell1 = row.getCell(2);
					if (record.getR43_NV_LONG() != null) {
					    cell1.setCellValue(record.getR43_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR43_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR43_FV_LONG() != null) {
					    cell1.setCellValue(record.getR43_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR43_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR43_QFHA() != null) {
					    cell1.setCellValue(record.getR43_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW43--------- R44
					row = sheet.getRow(43);

					cell1 = row.getCell(2);
					if (record.getR44_NV_LONG() != null) {
					    cell1.setCellValue(record.getR44_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR44_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR44_FV_LONG() != null) {
					    cell1.setCellValue(record.getR44_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR44_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR44_QFHA() != null) {
					    cell1.setCellValue(record.getR44_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW44--------- R45
					row = sheet.getRow(44);

					cell1 = row.getCell(2);
					if (record.getR45_NV_LONG() != null) {
					    cell1.setCellValue(record.getR45_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR45_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR45_FV_LONG() != null) {
					    cell1.setCellValue(record.getR45_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR45_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR45_QFHA() != null) {
					    cell1.setCellValue(record.getR45_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW45--------- R46
					row = sheet.getRow(45);

					cell1 = row.getCell(2);
					if (record.getR46_NV_LONG() != null) {
					    cell1.setCellValue(record.getR46_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR46_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR46_FV_LONG() != null) {
					    cell1.setCellValue(record.getR46_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR46_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR46_QFHA() != null) {
					    cell1.setCellValue(record.getR46_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW46--------- R47
					row = sheet.getRow(46);

					cell1 = row.getCell(2);
					if (record.getR47_NV_LONG() != null) {
					    cell1.setCellValue(record.getR47_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR47_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR47_FV_LONG() != null) {
					    cell1.setCellValue(record.getR47_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR47_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR47_QFHA() != null) {
					    cell1.setCellValue(record.getR47_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW47--------- R48
					row = sheet.getRow(47);

					cell1 = row.getCell(2);
					if (record.getR48_NV_LONG() != null) {
					    cell1.setCellValue(record.getR48_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR48_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR48_FV_LONG() != null) {
					    cell1.setCellValue(record.getR48_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR48_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR48_QFHA() != null) {
					    cell1.setCellValue(record.getR48_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW48--------- R49
					row = sheet.getRow(48);

					cell1 = row.getCell(2);
					if (record.getR49_NV_LONG() != null) {
					    cell1.setCellValue(record.getR49_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR49_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR49_FV_LONG() != null) {
					    cell1.setCellValue(record.getR49_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR49_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR49_QFHA() != null) {
					    cell1.setCellValue(record.getR49_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW49--------- R50
					row = sheet.getRow(49);

					cell1 = row.getCell(2);
					if (record.getR50_NV_LONG() != null) {
					    cell1.setCellValue(record.getR50_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR50_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR50_FV_LONG() != null) {
					    cell1.setCellValue(record.getR50_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR50_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR50_QFHA() != null) {
					    cell1.setCellValue(record.getR50_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					//------ROW50--------- R51
					row = sheet.getRow(50);

					cell1 = row.getCell(2);
					if (record.getR51_NV_LONG() != null) {
					    cell1.setCellValue(record.getR51_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR51_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR51_FV_LONG() != null) {
					    cell1.setCellValue(record.getR51_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR51_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR51_QFHA() != null) {
					    cell1.setCellValue(record.getR51_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					
					
					//------ROW51--------- R52
					row = sheet.getRow(51);

					cell1 = row.getCell(2);
					if (record.getR52_NV_LONG() != null) {
					    cell1.setCellValue(record.getR52_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR52_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR52_FV_LONG() != null) {
					    cell1.setCellValue(record.getR52_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR52_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR52_QFHA() != null) {
					    cell1.setCellValue(record.getR52_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW52--------- R53
					row = sheet.getRow(52);

					cell1 = row.getCell(2);
					if (record.getR53_NV_LONG() != null) {
					    cell1.setCellValue(record.getR53_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR53_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR53_FV_LONG() != null) {
					    cell1.setCellValue(record.getR53_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR53_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR53_QFHA() != null) {
					    cell1.setCellValue(record.getR53_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW53--------- R54
					row = sheet.getRow(53);

					cell1 = row.getCell(2);
					if (record.getR54_NV_LONG() != null) {
					    cell1.setCellValue(record.getR54_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR54_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR54_FV_LONG() != null) {
					    cell1.setCellValue(record.getR54_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR54_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR54_QFHA() != null) {
					    cell1.setCellValue(record.getR54_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW54--------- R55
					row = sheet.getRow(54);

					cell1 = row.getCell(2);
					if (record.getR55_NV_LONG() != null) {
					    cell1.setCellValue(record.getR55_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR55_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR55_FV_LONG() != null) {
					    cell1.setCellValue(record.getR55_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR55_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR55_QFHA() != null) {
					    cell1.setCellValue(record.getR55_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_TBS EMAIL ARCHIVAL SUMMARY", null, "BRRS_M_TBS_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	// Resub Format excel
	public byte[] BRRS_M_TBSResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_TBSResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
		}
	}

		List<M_TBS_Resub_Summary_Entity> dataList = getResubSummaryByDateAndVersion(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_TBS report. Returning empty result.");
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

					M_TBS_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}


					//REPORT_DATE
					row = sheet.getRow(6);
					Cell cell1 = row.getCell(1);
					if (cell1 == null) {
					    cell1 = row.createCell(1);
					}

					if (record.getReportDate() != null) {
					    cell1.setCellValue(record.getReportDate()); // java.util.Date
					    cell1.setCellStyle(dateStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					
					// row12
					// Column B
					row = sheet.getRow(10);
					 cell1 = row.getCell(2);
					if (record.getR11_NV_LONG() != null) {
						cell1.setCellValue(record.getR11_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR11_NV_SHORT() != null) {
						cell1.setCellValue(record.getR11_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

				    cell1 = row.getCell(4);
					if (record.getR11_FV_LONG() != null) {
						cell1.setCellValue(record.getR11_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR11_FV_SHORT() != null) {
						cell1.setCellValue(record.getR11_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR11_QFHA() != null) {
						cell1.setCellValue(record.getR11_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
//------ROW11---------						
					row=sheet.getRow(11);
				    cell1 = row.getCell(2);
					if (record.getR12_NV_LONG() != null) {
						cell1.setCellValue(record.getR12_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR12_NV_SHORT() != null) {
						cell1.setCellValue(record.getR12_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR12_FV_LONG() != null) {
						cell1.setCellValue(record.getR12_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR12_FV_SHORT() != null) {
						cell1.setCellValue(record.getR12_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR12_QFHA() != null) {
						cell1.setCellValue(record.getR12_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					//------ROW12---------						
					row=sheet.getRow(12);
				    cell1 = row.getCell(2);
					if (record.getR13_NV_LONG() != null) {
						cell1.setCellValue(record.getR13_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR13_NV_SHORT() != null) {
						cell1.setCellValue(record.getR13_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR13_FV_LONG() != null) {
						cell1.setCellValue(record.getR13_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR13_FV_SHORT() != null) {
						cell1.setCellValue(record.getR13_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR13_QFHA() != null) {
						cell1.setCellValue(record.getR13_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	

					//------ROW13---------
					row = sheet.getRow(13);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR14_NV_LONG() != null) {
					    cell1.setCellValue(record.getR14_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR14_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR14_FV_LONG() != null) {
					    cell1.setCellValue(record.getR14_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR14_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR14_QFHA() != null) {
					    cell1.setCellValue(record.getR14_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW14---------
					row = sheet.getRow(14);

					cell1 = row.getCell(2);
					if (record.getR15_NV_LONG() != null) {
					    cell1.setCellValue(record.getR15_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR15_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR15_FV_LONG() != null) {
					    cell1.setCellValue(record.getR15_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR15_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR15_QFHA() != null) {
					    cell1.setCellValue(record.getR15_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW15---------
					row = sheet.getRow(15);

					cell1 = row.getCell(2);
					if (record.getR16_NV_LONG() != null) {
					    cell1.setCellValue(record.getR16_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR16_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR16_FV_LONG() != null) {
					    cell1.setCellValue(record.getR16_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR16_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR16_QFHA() != null) {
					    cell1.setCellValue(record.getR16_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW16---------
					row = sheet.getRow(16);

					cell1 = row.getCell(2);
					if (record.getR17_NV_LONG() != null) {
					    cell1.setCellValue(record.getR17_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR17_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR17_FV_LONG() != null) {
					    cell1.setCellValue(record.getR17_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR17_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR17_QFHA() != null) {
					    cell1.setCellValue(record.getR17_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW17---------
					row = sheet.getRow(17);

					cell1 = row.getCell(2);
					if (record.getR18_NV_LONG() != null) {
					    cell1.setCellValue(record.getR18_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR18_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR18_FV_LONG() != null) {
					    cell1.setCellValue(record.getR18_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR18_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR18_QFHA() != null) {
					    cell1.setCellValue(record.getR18_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW18---------
					row = sheet.getRow(18);

					cell1 = row.getCell(2);
					if (record.getR19_NV_LONG() != null) {
					    cell1.setCellValue(record.getR19_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR19_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR19_FV_LONG() != null) {
					    cell1.setCellValue(record.getR19_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR19_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR19_QFHA() != null) {
					    cell1.setCellValue(record.getR19_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW19---------
					row = sheet.getRow(19);

					cell1 = row.getCell(2);
					if (record.getR20_NV_LONG() != null) {
					    cell1.setCellValue(record.getR20_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR20_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR20_FV_LONG() != null) {
					    cell1.setCellValue(record.getR20_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR20_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR20_QFHA() != null) {
					    cell1.setCellValue(record.getR20_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW20---------
					row = sheet.getRow(20);

					cell1 = row.getCell(2);
					if (record.getR21_NV_LONG() != null) {
					    cell1.setCellValue(record.getR21_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR21_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR21_FV_LONG() != null) {
					    cell1.setCellValue(record.getR21_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR21_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR21_QFHA() != null) {
					    cell1.setCellValue(record.getR21_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW21---------
					row = sheet.getRow(21);

					cell1 = row.getCell(2);
					if (record.getR22_NV_LONG() != null) {
					    cell1.setCellValue(record.getR22_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR22_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR22_FV_LONG() != null) {
					    cell1.setCellValue(record.getR22_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR22_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR22_QFHA() != null) {
					    cell1.setCellValue(record.getR22_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW22---------
					row = sheet.getRow(22);

					cell1 = row.getCell(2);
					if (record.getR23_NV_LONG() != null) {
					    cell1.setCellValue(record.getR23_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR23_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR23_FV_LONG() != null) {
					    cell1.setCellValue(record.getR23_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR23_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR23_QFHA() != null) {
					    cell1.setCellValue(record.getR23_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW23---------
					row = sheet.getRow(23);

					cell1 = row.getCell(2);
					if (record.getR24_NV_LONG() != null) {
					    cell1.setCellValue(record.getR24_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR24_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR24_FV_LONG() != null) {
					    cell1.setCellValue(record.getR24_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR24_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR24_QFHA() != null) {
					    cell1.setCellValue(record.getR24_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW24---------
					row = sheet.getRow(24);

					cell1 = row.getCell(2);
					if (record.getR25_NV_LONG() != null) {
					    cell1.setCellValue(record.getR25_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR25_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR25_FV_LONG() != null) {
					    cell1.setCellValue(record.getR25_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR25_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR25_QFHA() != null) {
					    cell1.setCellValue(record.getR25_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW25---------
					row = sheet.getRow(25);

					cell1 = row.getCell(2);
					if (record.getR26_NV_LONG() != null) {
					    cell1.setCellValue(record.getR26_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR26_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR26_FV_LONG() != null) {
					    cell1.setCellValue(record.getR26_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR26_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR26_QFHA() != null) {
					    cell1.setCellValue(record.getR26_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW26---------
					row = sheet.getRow(26);

					cell1 = row.getCell(2);
					if (record.getR27_NV_LONG() != null) {
					    cell1.setCellValue(record.getR27_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR27_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR27_FV_LONG() != null) {
					    cell1.setCellValue(record.getR27_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR27_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR27_QFHA() != null) {
					    cell1.setCellValue(record.getR27_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW27---------
					row = sheet.getRow(27);

					cell1 = row.getCell(2);
					if (record.getR28_NV_LONG() != null) {
					    cell1.setCellValue(record.getR28_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR28_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR28_FV_LONG() != null) {
					    cell1.setCellValue(record.getR28_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR28_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR28_QFHA() != null) {
					    cell1.setCellValue(record.getR28_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW28---------
					row = sheet.getRow(28);

					cell1 = row.getCell(2);
					if (record.getR29_NV_LONG() != null) {
					    cell1.setCellValue(record.getR29_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR29_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR29_FV_LONG() != null) {
					    cell1.setCellValue(record.getR29_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR29_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR29_QFHA() != null) {
					    cell1.setCellValue(record.getR29_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW29---------
					row = sheet.getRow(29);

					cell1 = row.getCell(2);
					if (record.getR30_NV_LONG() != null) {
					    cell1.setCellValue(record.getR30_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR30_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR30_FV_LONG() != null) {
					    cell1.setCellValue(record.getR30_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR30_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR30_QFHA() != null) {
					    cell1.setCellValue(record.getR30_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW30--------- R31
					row = sheet.getRow(30);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR31_NV_LONG() != null) {
					    cell1.setCellValue(record.getR31_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR31_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR31_FV_LONG() != null) {
					    cell1.setCellValue(record.getR31_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR31_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR31_QFHA() != null) {
					    cell1.setCellValue(record.getR31_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW31--------- R32
					row = sheet.getRow(31);

					cell1 = row.getCell(2);
					if (record.getR32_NV_LONG() != null) {
					    cell1.setCellValue(record.getR32_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR32_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR32_FV_LONG() != null) {
					    cell1.setCellValue(record.getR32_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR32_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR32_QFHA() != null) {
					    cell1.setCellValue(record.getR32_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW32--------- R33
					row = sheet.getRow(32);

					cell1 = row.getCell(2);
					if (record.getR33_NV_LONG() != null) {
					    cell1.setCellValue(record.getR33_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR33_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR33_FV_LONG() != null) {
					    cell1.setCellValue(record.getR33_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR33_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR33_QFHA() != null) {
					    cell1.setCellValue(record.getR33_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW33--------- R34
					row = sheet.getRow(33);

					cell1 = row.getCell(2);
					if (record.getR34_NV_LONG() != null) {
					    cell1.setCellValue(record.getR34_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR34_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR34_FV_LONG() != null) {
					    cell1.setCellValue(record.getR34_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR34_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR34_QFHA() != null) {
					    cell1.setCellValue(record.getR34_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW34--------- R35
					row = sheet.getRow(34);

					cell1 = row.getCell(2);
					if (record.getR35_NV_LONG() != null) {
					    cell1.setCellValue(record.getR35_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR35_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR35_FV_LONG() != null) {
					    cell1.setCellValue(record.getR35_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR35_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR35_QFHA() != null) {
					    cell1.setCellValue(record.getR35_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW35--------- R36
					row = sheet.getRow(35);

					cell1 = row.getCell(2);
					if (record.getR36_NV_LONG() != null) {
					    cell1.setCellValue(record.getR36_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR36_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR36_FV_LONG() != null) {
					    cell1.setCellValue(record.getR36_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR36_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR36_QFHA() != null) {
					    cell1.setCellValue(record.getR36_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW36--------- R37
					row = sheet.getRow(36);

					cell1 = row.getCell(2);
					if (record.getR37_NV_LONG() != null) {
					    cell1.setCellValue(record.getR37_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR37_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR37_FV_LONG() != null) {
					    cell1.setCellValue(record.getR37_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR37_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR37_QFHA() != null) {
					    cell1.setCellValue(record.getR37_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW37--------- R38
					row = sheet.getRow(37);

					cell1 = row.getCell(2);
					if (record.getR38_NV_LONG() != null) {
					    cell1.setCellValue(record.getR38_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR38_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR38_FV_LONG() != null) {
					    cell1.setCellValue(record.getR38_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR38_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR38_QFHA() != null) {
					    cell1.setCellValue(record.getR38_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW38--------- R39
					row = sheet.getRow(38);

					cell1 = row.getCell(2);
					if (record.getR39_NV_LONG() != null) {
					    cell1.setCellValue(record.getR39_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR39_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR39_FV_LONG() != null) {
					    cell1.setCellValue(record.getR39_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR39_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR39_QFHA() != null) {
					    cell1.setCellValue(record.getR39_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW39--------- R40
					row = sheet.getRow(39);

					cell1 = row.getCell(2);
					if (record.getR40_NV_LONG() != null) {
					    cell1.setCellValue(record.getR40_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR40_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR40_FV_LONG() != null) {
					    cell1.setCellValue(record.getR40_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR40_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR40_QFHA() != null) {
					    cell1.setCellValue(record.getR40_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					//------ROW40--------- R41
					row = sheet.getRow(40);

					cell1 = row.getCell(2);
					if (record.getR41_NV_LONG() != null) {
					    cell1.setCellValue(record.getR41_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR41_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR41_FV_LONG() != null) {
					    cell1.setCellValue(record.getR41_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR41_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR41_QFHA() != null) {
					    cell1.setCellValue(record.getR41_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW41--------- R42
					row = sheet.getRow(41);

					cell1 = row.getCell(2);
					if (record.getR42_NV_LONG() != null) {
					    cell1.setCellValue(record.getR42_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR42_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR42_FV_LONG() != null) {
					    cell1.setCellValue(record.getR42_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR42_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR42_QFHA() != null) {
					    cell1.setCellValue(record.getR42_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW42--------- R43
					row = sheet.getRow(42);

					cell1 = row.getCell(2);
					if (record.getR43_NV_LONG() != null) {
					    cell1.setCellValue(record.getR43_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR43_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR43_FV_LONG() != null) {
					    cell1.setCellValue(record.getR43_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR43_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR43_QFHA() != null) {
					    cell1.setCellValue(record.getR43_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW43--------- R44
					row = sheet.getRow(43);

					cell1 = row.getCell(2);
					if (record.getR44_NV_LONG() != null) {
					    cell1.setCellValue(record.getR44_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR44_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR44_FV_LONG() != null) {
					    cell1.setCellValue(record.getR44_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR44_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR44_QFHA() != null) {
					    cell1.setCellValue(record.getR44_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW44--------- R45
					row = sheet.getRow(44);

					cell1 = row.getCell(2);
					if (record.getR45_NV_LONG() != null) {
					    cell1.setCellValue(record.getR45_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR45_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR45_FV_LONG() != null) {
					    cell1.setCellValue(record.getR45_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR45_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR45_QFHA() != null) {
					    cell1.setCellValue(record.getR45_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW45--------- R46
					row = sheet.getRow(45);

					cell1 = row.getCell(2);
					if (record.getR46_NV_LONG() != null) {
					    cell1.setCellValue(record.getR46_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR46_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR46_FV_LONG() != null) {
					    cell1.setCellValue(record.getR46_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR46_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR46_QFHA() != null) {
					    cell1.setCellValue(record.getR46_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW46--------- R47
					row = sheet.getRow(46);

					cell1 = row.getCell(2);
					if (record.getR47_NV_LONG() != null) {
					    cell1.setCellValue(record.getR47_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR47_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR47_FV_LONG() != null) {
					    cell1.setCellValue(record.getR47_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR47_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR47_QFHA() != null) {
					    cell1.setCellValue(record.getR47_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW47--------- R48
					row = sheet.getRow(47);

					cell1 = row.getCell(2);
					if (record.getR48_NV_LONG() != null) {
					    cell1.setCellValue(record.getR48_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR48_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR48_FV_LONG() != null) {
					    cell1.setCellValue(record.getR48_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR48_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR48_QFHA() != null) {
					    cell1.setCellValue(record.getR48_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW48--------- R49
					row = sheet.getRow(48);

					cell1 = row.getCell(2);
					if (record.getR49_NV_LONG() != null) {
					    cell1.setCellValue(record.getR49_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR49_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR49_FV_LONG() != null) {
					    cell1.setCellValue(record.getR49_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR49_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR49_QFHA() != null) {
					    cell1.setCellValue(record.getR49_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW49--------- R50
					row = sheet.getRow(49);

					cell1 = row.getCell(2);
					if (record.getR50_NV_LONG() != null) {
					    cell1.setCellValue(record.getR50_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR50_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR50_FV_LONG() != null) {
					    cell1.setCellValue(record.getR50_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR50_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR50_QFHA() != null) {
					    cell1.setCellValue(record.getR50_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					//------ROW50--------- R51
					row = sheet.getRow(50);

					cell1 = row.getCell(2);
					if (record.getR51_NV_LONG() != null) {
					    cell1.setCellValue(record.getR51_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR51_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR51_FV_LONG() != null) {
					    cell1.setCellValue(record.getR51_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR51_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR51_QFHA() != null) {
					    cell1.setCellValue(record.getR51_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					
					
					//------ROW51--------- R52
					row = sheet.getRow(51);

					cell1 = row.getCell(2);
					if (record.getR52_NV_LONG() != null) {
					    cell1.setCellValue(record.getR52_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR52_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR52_FV_LONG() != null) {
					    cell1.setCellValue(record.getR52_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR52_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR52_QFHA() != null) {
					    cell1.setCellValue(record.getR52_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW52--------- R53
					row = sheet.getRow(52);

					cell1 = row.getCell(2);
					if (record.getR53_NV_LONG() != null) {
					    cell1.setCellValue(record.getR53_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR53_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR53_FV_LONG() != null) {
					    cell1.setCellValue(record.getR53_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR53_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR53_QFHA() != null) {
					    cell1.setCellValue(record.getR53_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW53--------- R54
					row = sheet.getRow(53);

					cell1 = row.getCell(2);
					if (record.getR54_NV_LONG() != null) {
					    cell1.setCellValue(record.getR54_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR54_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR54_FV_LONG() != null) {
					    cell1.setCellValue(record.getR54_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR54_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR54_QFHA() != null) {
					    cell1.setCellValue(record.getR54_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW54--------- R55
					row = sheet.getRow(54);

					cell1 = row.getCell(2);
					if (record.getR55_NV_LONG() != null) {
					    cell1.setCellValue(record.getR55_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR55_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR55_FV_LONG() != null) {
					    cell1.setCellValue(record.getR55_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR55_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR55_QFHA() != null) {
					    cell1.setCellValue(record.getR55_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_TBS RESUB SUMMARY", null, "BRRS_M_TBS_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}

	}

	// Resub Email Excel
	public byte[] BRRS_M_TBSResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_TBS_Resub_Summary_Entity> dataList = getResubSummaryByDateAndVersion(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_TBS report. Returning empty result.");
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
					M_TBS_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					//REPORT_DATE
					row = sheet.getRow(6);
					Cell cell1 = row.getCell(1);
					if (cell1 == null) {
					    cell1 = row.createCell(1);
					}

					if (record.getReportDate() != null) {
					    cell1.setCellValue(record.getReportDate()); // java.util.Date
					    cell1.setCellStyle(dateStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					
					// row12
					// Column B
					row = sheet.getRow(10);
					 cell1 = row.getCell(2);
					if (record.getR11_NV_LONG() != null) {
						cell1.setCellValue(record.getR11_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR11_NV_SHORT() != null) {
						cell1.setCellValue(record.getR11_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

				    cell1 = row.getCell(4);
					if (record.getR11_FV_LONG() != null) {
						cell1.setCellValue(record.getR11_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR11_FV_SHORT() != null) {
						cell1.setCellValue(record.getR11_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR11_QFHA() != null) {
						cell1.setCellValue(record.getR11_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
//------ROW11---------						
					row=sheet.getRow(11);
				    cell1 = row.getCell(2);
					if (record.getR12_NV_LONG() != null) {
						cell1.setCellValue(record.getR12_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR12_NV_SHORT() != null) {
						cell1.setCellValue(record.getR12_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR12_FV_LONG() != null) {
						cell1.setCellValue(record.getR12_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR12_FV_SHORT() != null) {
						cell1.setCellValue(record.getR12_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR12_QFHA() != null) {
						cell1.setCellValue(record.getR12_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					//------ROW12---------						
					row=sheet.getRow(12);
				    cell1 = row.getCell(2);
					if (record.getR13_NV_LONG() != null) {
						cell1.setCellValue(record.getR13_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR13_NV_SHORT() != null) {
						cell1.setCellValue(record.getR13_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR13_FV_LONG() != null) {
						cell1.setCellValue(record.getR13_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR13_FV_SHORT() != null) {
						cell1.setCellValue(record.getR13_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR13_QFHA() != null) {
						cell1.setCellValue(record.getR13_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	

					//------ROW13---------
					row = sheet.getRow(13);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR14_NV_LONG() != null) {
					    cell1.setCellValue(record.getR14_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR14_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR14_FV_LONG() != null) {
					    cell1.setCellValue(record.getR14_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR14_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR14_QFHA() != null) {
					    cell1.setCellValue(record.getR14_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW14---------
					row = sheet.getRow(14);

					cell1 = row.getCell(2);
					if (record.getR15_NV_LONG() != null) {
					    cell1.setCellValue(record.getR15_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR15_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR15_FV_LONG() != null) {
					    cell1.setCellValue(record.getR15_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR15_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR15_QFHA() != null) {
					    cell1.setCellValue(record.getR15_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW15---------
					row = sheet.getRow(15);

					cell1 = row.getCell(2);
					if (record.getR16_NV_LONG() != null) {
					    cell1.setCellValue(record.getR16_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR16_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR16_FV_LONG() != null) {
					    cell1.setCellValue(record.getR16_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR16_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR16_QFHA() != null) {
					    cell1.setCellValue(record.getR16_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW16---------
					row = sheet.getRow(16);

					cell1 = row.getCell(2);
					if (record.getR17_NV_LONG() != null) {
					    cell1.setCellValue(record.getR17_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR17_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR17_FV_LONG() != null) {
					    cell1.setCellValue(record.getR17_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR17_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR17_QFHA() != null) {
					    cell1.setCellValue(record.getR17_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW17---------
					row = sheet.getRow(17);

					cell1 = row.getCell(2);
					if (record.getR18_NV_LONG() != null) {
					    cell1.setCellValue(record.getR18_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR18_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR18_FV_LONG() != null) {
					    cell1.setCellValue(record.getR18_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR18_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR18_QFHA() != null) {
					    cell1.setCellValue(record.getR18_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW18---------
					row = sheet.getRow(18);

					cell1 = row.getCell(2);
					if (record.getR19_NV_LONG() != null) {
					    cell1.setCellValue(record.getR19_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR19_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR19_FV_LONG() != null) {
					    cell1.setCellValue(record.getR19_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR19_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR19_QFHA() != null) {
					    cell1.setCellValue(record.getR19_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW19---------
					row = sheet.getRow(19);

					cell1 = row.getCell(2);
					if (record.getR20_NV_LONG() != null) {
					    cell1.setCellValue(record.getR20_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR20_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR20_FV_LONG() != null) {
					    cell1.setCellValue(record.getR20_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR20_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR20_QFHA() != null) {
					    cell1.setCellValue(record.getR20_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW20---------
					row = sheet.getRow(20);

					cell1 = row.getCell(2);
					if (record.getR21_NV_LONG() != null) {
					    cell1.setCellValue(record.getR21_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR21_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR21_FV_LONG() != null) {
					    cell1.setCellValue(record.getR21_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR21_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR21_QFHA() != null) {
					    cell1.setCellValue(record.getR21_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW21---------
					row = sheet.getRow(21);

					cell1 = row.getCell(2);
					if (record.getR22_NV_LONG() != null) {
					    cell1.setCellValue(record.getR22_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR22_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR22_FV_LONG() != null) {
					    cell1.setCellValue(record.getR22_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR22_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR22_QFHA() != null) {
					    cell1.setCellValue(record.getR22_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW22---------
					row = sheet.getRow(22);

					cell1 = row.getCell(2);
					if (record.getR23_NV_LONG() != null) {
					    cell1.setCellValue(record.getR23_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR23_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR23_FV_LONG() != null) {
					    cell1.setCellValue(record.getR23_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR23_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR23_QFHA() != null) {
					    cell1.setCellValue(record.getR23_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW23---------
					row = sheet.getRow(23);

					cell1 = row.getCell(2);
					if (record.getR24_NV_LONG() != null) {
					    cell1.setCellValue(record.getR24_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR24_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR24_FV_LONG() != null) {
					    cell1.setCellValue(record.getR24_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR24_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR24_QFHA() != null) {
					    cell1.setCellValue(record.getR24_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW24---------
					row = sheet.getRow(24);

					cell1 = row.getCell(2);
					if (record.getR25_NV_LONG() != null) {
					    cell1.setCellValue(record.getR25_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR25_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR25_FV_LONG() != null) {
					    cell1.setCellValue(record.getR25_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR25_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR25_QFHA() != null) {
					    cell1.setCellValue(record.getR25_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW25---------
					row = sheet.getRow(25);

					cell1 = row.getCell(2);
					if (record.getR26_NV_LONG() != null) {
					    cell1.setCellValue(record.getR26_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR26_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR26_FV_LONG() != null) {
					    cell1.setCellValue(record.getR26_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR26_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR26_QFHA() != null) {
					    cell1.setCellValue(record.getR26_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW26---------
					row = sheet.getRow(26);

					cell1 = row.getCell(2);
					if (record.getR27_NV_LONG() != null) {
					    cell1.setCellValue(record.getR27_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR27_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR27_FV_LONG() != null) {
					    cell1.setCellValue(record.getR27_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR27_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR27_QFHA() != null) {
					    cell1.setCellValue(record.getR27_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW27---------
					row = sheet.getRow(27);

					cell1 = row.getCell(2);
					if (record.getR28_NV_LONG() != null) {
					    cell1.setCellValue(record.getR28_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR28_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR28_FV_LONG() != null) {
					    cell1.setCellValue(record.getR28_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR28_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR28_QFHA() != null) {
					    cell1.setCellValue(record.getR28_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW28---------
					row = sheet.getRow(28);

					cell1 = row.getCell(2);
					if (record.getR29_NV_LONG() != null) {
					    cell1.setCellValue(record.getR29_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR29_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR29_FV_LONG() != null) {
					    cell1.setCellValue(record.getR29_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR29_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR29_QFHA() != null) {
					    cell1.setCellValue(record.getR29_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW29---------
					row = sheet.getRow(29);

					cell1 = row.getCell(2);
					if (record.getR30_NV_LONG() != null) {
					    cell1.setCellValue(record.getR30_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR30_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR30_FV_LONG() != null) {
					    cell1.setCellValue(record.getR30_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR30_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR30_QFHA() != null) {
					    cell1.setCellValue(record.getR30_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW30--------- R31
					row = sheet.getRow(30);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR31_NV_LONG() != null) {
					    cell1.setCellValue(record.getR31_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR31_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR31_FV_LONG() != null) {
					    cell1.setCellValue(record.getR31_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR31_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR31_QFHA() != null) {
					    cell1.setCellValue(record.getR31_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW31--------- R32
					row = sheet.getRow(31);

					cell1 = row.getCell(2);
					if (record.getR32_NV_LONG() != null) {
					    cell1.setCellValue(record.getR32_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR32_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR32_FV_LONG() != null) {
					    cell1.setCellValue(record.getR32_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR32_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR32_QFHA() != null) {
					    cell1.setCellValue(record.getR32_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW32--------- R33
					row = sheet.getRow(32);

					cell1 = row.getCell(2);
					if (record.getR33_NV_LONG() != null) {
					    cell1.setCellValue(record.getR33_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR33_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR33_FV_LONG() != null) {
					    cell1.setCellValue(record.getR33_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR33_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR33_QFHA() != null) {
					    cell1.setCellValue(record.getR33_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW33--------- R34
					row = sheet.getRow(33);

					cell1 = row.getCell(2);
					if (record.getR34_NV_LONG() != null) {
					    cell1.setCellValue(record.getR34_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR34_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR34_FV_LONG() != null) {
					    cell1.setCellValue(record.getR34_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR34_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR34_QFHA() != null) {
					    cell1.setCellValue(record.getR34_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW34--------- R35
					row = sheet.getRow(34);

					cell1 = row.getCell(2);
					if (record.getR35_NV_LONG() != null) {
					    cell1.setCellValue(record.getR35_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR35_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR35_FV_LONG() != null) {
					    cell1.setCellValue(record.getR35_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR35_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR35_QFHA() != null) {
					    cell1.setCellValue(record.getR35_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW35--------- R36
					row = sheet.getRow(35);

					cell1 = row.getCell(2);
					if (record.getR36_NV_LONG() != null) {
					    cell1.setCellValue(record.getR36_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR36_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR36_FV_LONG() != null) {
					    cell1.setCellValue(record.getR36_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR36_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR36_QFHA() != null) {
					    cell1.setCellValue(record.getR36_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW36--------- R37
					row = sheet.getRow(36);

					cell1 = row.getCell(2);
					if (record.getR37_NV_LONG() != null) {
					    cell1.setCellValue(record.getR37_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR37_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR37_FV_LONG() != null) {
					    cell1.setCellValue(record.getR37_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR37_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR37_QFHA() != null) {
					    cell1.setCellValue(record.getR37_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW37--------- R38
					row = sheet.getRow(37);

					cell1 = row.getCell(2);
					if (record.getR38_NV_LONG() != null) {
					    cell1.setCellValue(record.getR38_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR38_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR38_FV_LONG() != null) {
					    cell1.setCellValue(record.getR38_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR38_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR38_QFHA() != null) {
					    cell1.setCellValue(record.getR38_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW38--------- R39
					row = sheet.getRow(38);

					cell1 = row.getCell(2);
					if (record.getR39_NV_LONG() != null) {
					    cell1.setCellValue(record.getR39_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR39_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR39_FV_LONG() != null) {
					    cell1.setCellValue(record.getR39_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR39_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR39_QFHA() != null) {
					    cell1.setCellValue(record.getR39_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW39--------- R40
					row = sheet.getRow(39);

					cell1 = row.getCell(2);
					if (record.getR40_NV_LONG() != null) {
					    cell1.setCellValue(record.getR40_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR40_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR40_FV_LONG() != null) {
					    cell1.setCellValue(record.getR40_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR40_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR40_QFHA() != null) {
					    cell1.setCellValue(record.getR40_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					//------ROW40--------- R41
					row = sheet.getRow(40);

					cell1 = row.getCell(2);
					if (record.getR41_NV_LONG() != null) {
					    cell1.setCellValue(record.getR41_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR41_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR41_FV_LONG() != null) {
					    cell1.setCellValue(record.getR41_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR41_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR41_QFHA() != null) {
					    cell1.setCellValue(record.getR41_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW41--------- R42
					row = sheet.getRow(41);

					cell1 = row.getCell(2);
					if (record.getR42_NV_LONG() != null) {
					    cell1.setCellValue(record.getR42_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR42_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR42_FV_LONG() != null) {
					    cell1.setCellValue(record.getR42_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR42_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR42_QFHA() != null) {
					    cell1.setCellValue(record.getR42_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW42--------- R43
					row = sheet.getRow(42);

					cell1 = row.getCell(2);
					if (record.getR43_NV_LONG() != null) {
					    cell1.setCellValue(record.getR43_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR43_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR43_FV_LONG() != null) {
					    cell1.setCellValue(record.getR43_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR43_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR43_QFHA() != null) {
					    cell1.setCellValue(record.getR43_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW43--------- R44
					row = sheet.getRow(43);

					cell1 = row.getCell(2);
					if (record.getR44_NV_LONG() != null) {
					    cell1.setCellValue(record.getR44_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR44_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR44_FV_LONG() != null) {
					    cell1.setCellValue(record.getR44_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR44_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR44_QFHA() != null) {
					    cell1.setCellValue(record.getR44_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW44--------- R45
					row = sheet.getRow(44);

					cell1 = row.getCell(2);
					if (record.getR45_NV_LONG() != null) {
					    cell1.setCellValue(record.getR45_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR45_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR45_FV_LONG() != null) {
					    cell1.setCellValue(record.getR45_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR45_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR45_QFHA() != null) {
					    cell1.setCellValue(record.getR45_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW45--------- R46
					row = sheet.getRow(45);

					cell1 = row.getCell(2);
					if (record.getR46_NV_LONG() != null) {
					    cell1.setCellValue(record.getR46_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR46_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR46_FV_LONG() != null) {
					    cell1.setCellValue(record.getR46_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR46_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR46_QFHA() != null) {
					    cell1.setCellValue(record.getR46_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW46--------- R47
					row = sheet.getRow(46);

					cell1 = row.getCell(2);
					if (record.getR47_NV_LONG() != null) {
					    cell1.setCellValue(record.getR47_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR47_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR47_FV_LONG() != null) {
					    cell1.setCellValue(record.getR47_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR47_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR47_QFHA() != null) {
					    cell1.setCellValue(record.getR47_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW47--------- R48
					row = sheet.getRow(47);

					cell1 = row.getCell(2);
					if (record.getR48_NV_LONG() != null) {
					    cell1.setCellValue(record.getR48_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR48_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR48_FV_LONG() != null) {
					    cell1.setCellValue(record.getR48_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR48_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR48_QFHA() != null) {
					    cell1.setCellValue(record.getR48_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW48--------- R49
					row = sheet.getRow(48);

					cell1 = row.getCell(2);
					if (record.getR49_NV_LONG() != null) {
					    cell1.setCellValue(record.getR49_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR49_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR49_FV_LONG() != null) {
					    cell1.setCellValue(record.getR49_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR49_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR49_QFHA() != null) {
					    cell1.setCellValue(record.getR49_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW49--------- R50
					row = sheet.getRow(49);

					cell1 = row.getCell(2);
					if (record.getR50_NV_LONG() != null) {
					    cell1.setCellValue(record.getR50_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR50_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR50_FV_LONG() != null) {
					    cell1.setCellValue(record.getR50_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR50_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR50_QFHA() != null) {
					    cell1.setCellValue(record.getR50_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					//------ROW50--------- R51
					row = sheet.getRow(50);

					cell1 = row.getCell(2);
					if (record.getR51_NV_LONG() != null) {
					    cell1.setCellValue(record.getR51_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR51_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR51_FV_LONG() != null) {
					    cell1.setCellValue(record.getR51_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR51_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR51_QFHA() != null) {
					    cell1.setCellValue(record.getR51_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					
					
					//------ROW51--------- R52
					row = sheet.getRow(51);

					cell1 = row.getCell(2);
					if (record.getR52_NV_LONG() != null) {
					    cell1.setCellValue(record.getR52_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR52_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR52_FV_LONG() != null) {
					    cell1.setCellValue(record.getR52_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR52_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR52_QFHA() != null) {
					    cell1.setCellValue(record.getR52_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW52--------- R53
					row = sheet.getRow(52);

					cell1 = row.getCell(2);
					if (record.getR53_NV_LONG() != null) {
					    cell1.setCellValue(record.getR53_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR53_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR53_FV_LONG() != null) {
					    cell1.setCellValue(record.getR53_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR53_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR53_QFHA() != null) {
					    cell1.setCellValue(record.getR53_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW53--------- R54
					row = sheet.getRow(53);

					cell1 = row.getCell(2);
					if (record.getR54_NV_LONG() != null) {
					    cell1.setCellValue(record.getR54_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR54_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR54_FV_LONG() != null) {
					    cell1.setCellValue(record.getR54_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR54_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR54_QFHA() != null) {
					    cell1.setCellValue(record.getR54_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW54--------- R55
					row = sheet.getRow(54);

					cell1 = row.getCell(2);
					if (record.getR55_NV_LONG() != null) {
					    cell1.setCellValue(record.getR55_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR55_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR55_FV_LONG() != null) {
					    cell1.setCellValue(record.getR55_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR55_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR55_QFHA() != null) {
					    cell1.setCellValue(record.getR55_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

}				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_TBS EMAIL REUBSUMMARY", null, "BRRS_M_TBS_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}


	// =========================================================
	// Inner entity: M_TBS_Summary_Entity
	// =========================================================
	public static class M_TBS_Summary_Entity {
		private String R11_PRODUCT;
		private BigDecimal R11_NV_LONG;
		private BigDecimal R11_NV_SHORT;
		private BigDecimal R11_FV_LONG;
		private BigDecimal R11_FV_SHORT;
		private BigDecimal R11_QFHA;
		private String R12_PRODUCT;
		private BigDecimal R12_NV_LONG;
		private BigDecimal R12_NV_SHORT;
		private BigDecimal R12_FV_LONG;
		private BigDecimal R12_FV_SHORT;
		private BigDecimal R12_QFHA;
		private String R13_PRODUCT;
		private BigDecimal R13_NV_LONG;
		private BigDecimal R13_NV_SHORT;
		private BigDecimal R13_FV_LONG;
		private BigDecimal R13_FV_SHORT;
		private BigDecimal R13_QFHA;
		private String R14_PRODUCT;
		private BigDecimal R14_NV_LONG;
		private BigDecimal R14_NV_SHORT;
		private BigDecimal R14_FV_LONG;
		private BigDecimal R14_FV_SHORT;
		private BigDecimal R14_QFHA;
		private String R15_PRODUCT;
		private BigDecimal R15_NV_LONG;
		private BigDecimal R15_NV_SHORT;
		private BigDecimal R15_FV_LONG;
		private BigDecimal R15_FV_SHORT;
		private BigDecimal R15_QFHA;
		private String R16_PRODUCT;
		private BigDecimal R16_NV_LONG;
		private BigDecimal R16_NV_SHORT;
		private BigDecimal R16_FV_LONG;
		private BigDecimal R16_FV_SHORT;
		private BigDecimal R16_QFHA;
		private String R17_PRODUCT;
		private BigDecimal R17_NV_LONG;
		private BigDecimal R17_NV_SHORT;
		private BigDecimal R17_FV_LONG;
		private BigDecimal R17_FV_SHORT;
		private BigDecimal R17_QFHA;
		private String R18_PRODUCT;
		private BigDecimal R18_NV_LONG;
		private BigDecimal R18_NV_SHORT;
		private BigDecimal R18_FV_LONG;
		private BigDecimal R18_FV_SHORT;
		private BigDecimal R18_QFHA;
		private String R19_PRODUCT;
		private BigDecimal R19_NV_LONG;
		private BigDecimal R19_NV_SHORT;
		private BigDecimal R19_FV_LONG;
		private BigDecimal R19_FV_SHORT;
		private BigDecimal R19_QFHA;
		private String R20_PRODUCT;
		private BigDecimal R20_NV_LONG;
		private BigDecimal R20_NV_SHORT;
		private BigDecimal R20_FV_LONG;
		private BigDecimal R20_FV_SHORT;
		private BigDecimal R20_QFHA;
		private String R21_PRODUCT;
		private BigDecimal R21_NV_LONG;
		private BigDecimal R21_NV_SHORT;
		private BigDecimal R21_FV_LONG;
		private BigDecimal R21_FV_SHORT;
		private BigDecimal R21_QFHA;
		private String R22_PRODUCT;
		private BigDecimal R22_NV_LONG;
		private BigDecimal R22_NV_SHORT;
		private BigDecimal R22_FV_LONG;
		private BigDecimal R22_FV_SHORT;
		private BigDecimal R22_QFHA;
		private String R23_PRODUCT;
		private BigDecimal R23_NV_LONG;
		private BigDecimal R23_NV_SHORT;
		private BigDecimal R23_FV_LONG;
		private BigDecimal R23_FV_SHORT;
		private BigDecimal R23_QFHA;
		private String R24_PRODUCT;
		private BigDecimal R24_NV_LONG;
		private BigDecimal R24_NV_SHORT;
		private BigDecimal R24_FV_LONG;
		private BigDecimal R24_FV_SHORT;
		private BigDecimal R24_QFHA;
		private String R25_PRODUCT;
		private BigDecimal R25_NV_LONG;
		private BigDecimal R25_NV_SHORT;
		private BigDecimal R25_FV_LONG;
		private BigDecimal R25_FV_SHORT;
		private BigDecimal R25_QFHA;
		private String R26_PRODUCT;
		private BigDecimal R26_NV_LONG;
		private BigDecimal R26_NV_SHORT;
		private BigDecimal R26_FV_LONG;
		private BigDecimal R26_FV_SHORT;
		private BigDecimal R26_QFHA;
		private String R27_PRODUCT;
		private BigDecimal R27_NV_LONG;
		private BigDecimal R27_NV_SHORT;
		private BigDecimal R27_FV_LONG;
		private BigDecimal R27_FV_SHORT;
		private BigDecimal R27_QFHA;
		private String R28_PRODUCT;
		private BigDecimal R28_NV_LONG;
		private BigDecimal R28_NV_SHORT;
		private BigDecimal R28_FV_LONG;
		private BigDecimal R28_FV_SHORT;
		private BigDecimal R28_QFHA;
		private String R29_PRODUCT;
		private BigDecimal R29_NV_LONG;
		private BigDecimal R29_NV_SHORT;
		private BigDecimal R29_FV_LONG;
		private BigDecimal R29_FV_SHORT;
		private BigDecimal R29_QFHA;
		private String R30_PRODUCT;
		private BigDecimal R30_NV_LONG;
		private BigDecimal R30_NV_SHORT;
		private BigDecimal R30_FV_LONG;
		private BigDecimal R30_FV_SHORT;
		private BigDecimal R30_QFHA;
		private String R31_PRODUCT;
		private BigDecimal R31_NV_LONG;
		private BigDecimal R31_NV_SHORT;
		private BigDecimal R31_FV_LONG;
		private BigDecimal R31_FV_SHORT;
		private BigDecimal R31_QFHA;
		private String R32_PRODUCT;
		private BigDecimal R32_NV_LONG;
		private BigDecimal R32_NV_SHORT;
		private BigDecimal R32_FV_LONG;
		private BigDecimal R32_FV_SHORT;
		private BigDecimal R32_QFHA;
		private String R33_PRODUCT;
		private BigDecimal R33_NV_LONG;
		private BigDecimal R33_NV_SHORT;
		private BigDecimal R33_FV_LONG;
		private BigDecimal R33_FV_SHORT;
		private BigDecimal R33_QFHA;
		private String R34_PRODUCT;
		private BigDecimal R34_NV_LONG;
		private BigDecimal R34_NV_SHORT;
		private BigDecimal R34_FV_LONG;
		private BigDecimal R34_FV_SHORT;
		private BigDecimal R34_QFHA;
		private String R35_PRODUCT;
		private BigDecimal R35_NV_LONG;
		private BigDecimal R35_NV_SHORT;
		private BigDecimal R35_FV_LONG;
		private BigDecimal R35_FV_SHORT;
		private BigDecimal R35_QFHA;
		private String R36_PRODUCT;
		private BigDecimal R36_NV_LONG;
		private BigDecimal R36_NV_SHORT;
		private BigDecimal R36_FV_LONG;
		private BigDecimal R36_FV_SHORT;
		private BigDecimal R36_QFHA;
		private String R37_PRODUCT;
		private BigDecimal R37_NV_LONG;
		private BigDecimal R37_NV_SHORT;
		private BigDecimal R37_FV_LONG;
		private BigDecimal R37_FV_SHORT;
		private BigDecimal R37_QFHA;
		private String R38_PRODUCT;
		private BigDecimal R38_NV_LONG;
		private BigDecimal R38_NV_SHORT;
		private BigDecimal R38_FV_LONG;
		private BigDecimal R38_FV_SHORT;
		private BigDecimal R38_QFHA;
		private String R39_PRODUCT;
		private BigDecimal R39_NV_LONG;
		private BigDecimal R39_NV_SHORT;
		private BigDecimal R39_FV_LONG;
		private BigDecimal R39_FV_SHORT;
		private BigDecimal R39_QFHA;
		private String R40_PRODUCT;
		private BigDecimal R40_NV_LONG;
		private BigDecimal R40_NV_SHORT;
		private BigDecimal R40_FV_LONG;
		private BigDecimal R40_FV_SHORT;
		private BigDecimal R40_QFHA;
		private String R41_PRODUCT;
		private BigDecimal R41_NV_LONG;
		private BigDecimal R41_NV_SHORT;
		private BigDecimal R41_FV_LONG;
		private BigDecimal R41_FV_SHORT;
		private BigDecimal R41_QFHA;
		private String R42_PRODUCT;
		private BigDecimal R42_NV_LONG;
		private BigDecimal R42_NV_SHORT;
		private BigDecimal R42_FV_LONG;
		private BigDecimal R42_FV_SHORT;
		private BigDecimal R42_QFHA;
		private String R43_PRODUCT;
		private BigDecimal R43_NV_LONG;
		private BigDecimal R43_NV_SHORT;
		private BigDecimal R43_FV_LONG;
		private BigDecimal R43_FV_SHORT;
		private BigDecimal R43_QFHA;
		private String R44_PRODUCT;
		private BigDecimal R44_NV_LONG;
		private BigDecimal R44_NV_SHORT;
		private BigDecimal R44_FV_LONG;
		private BigDecimal R44_FV_SHORT;
		private BigDecimal R44_QFHA;
		private String R45_PRODUCT;
		private BigDecimal R45_NV_LONG;
		private BigDecimal R45_NV_SHORT;
		private BigDecimal R45_FV_LONG;
		private BigDecimal R45_FV_SHORT;
		private BigDecimal R45_QFHA;
		private String R46_PRODUCT;
		private BigDecimal R46_NV_LONG;
		private BigDecimal R46_NV_SHORT;
		private BigDecimal R46_FV_LONG;
		private BigDecimal R46_FV_SHORT;
		private BigDecimal R46_QFHA;
		private String R47_PRODUCT;
		private BigDecimal R47_NV_LONG;
		private BigDecimal R47_NV_SHORT;
		private BigDecimal R47_FV_LONG;
		private BigDecimal R47_FV_SHORT;
		private BigDecimal R47_QFHA;
		private String R48_PRODUCT;
		private BigDecimal R48_NV_LONG;
		private BigDecimal R48_NV_SHORT;
		private BigDecimal R48_FV_LONG;
		private BigDecimal R48_FV_SHORT;
		private BigDecimal R48_QFHA;
		private String R49_PRODUCT;
		private BigDecimal R49_NV_LONG;
		private BigDecimal R49_NV_SHORT;
		private BigDecimal R49_FV_LONG;
		private BigDecimal R49_FV_SHORT;
		private BigDecimal R49_QFHA;
		private String R50_PRODUCT;
		private BigDecimal R50_NV_LONG;
		private BigDecimal R50_NV_SHORT;
		private BigDecimal R50_FV_LONG;
		private BigDecimal R50_FV_SHORT;
		private BigDecimal R50_QFHA;
		private String R51_PRODUCT;
		private BigDecimal R51_NV_LONG;
		private BigDecimal R51_NV_SHORT;
		private BigDecimal R51_FV_LONG;
		private BigDecimal R51_FV_SHORT;
		private BigDecimal R51_QFHA;
		private String R52_PRODUCT;
		private BigDecimal R52_NV_LONG;
		private BigDecimal R52_NV_SHORT;
		private BigDecimal R52_FV_LONG;
		private BigDecimal R52_FV_SHORT;
		private BigDecimal R52_QFHA;
		private String R53_PRODUCT;
		private BigDecimal R53_NV_LONG;
		private BigDecimal R53_NV_SHORT;
		private BigDecimal R53_FV_LONG;
		private BigDecimal R53_FV_SHORT;
		private BigDecimal R53_QFHA;
		private String R54_PRODUCT;
		private BigDecimal R54_NV_LONG;
		private BigDecimal R54_NV_SHORT;
		private BigDecimal R54_FV_LONG;
		private BigDecimal R54_FV_SHORT;
		private BigDecimal R54_QFHA;
		private String R55_PRODUCT;
		private BigDecimal R55_NV_LONG;
		private BigDecimal R55_NV_SHORT;
		private BigDecimal R55_FV_LONG;
		private BigDecimal R55_FV_SHORT;
		private BigDecimal R55_QFHA;

		private Date reportDate;
		private BigDecimal reportVersion;
		public String REPORT_FREQUENCY;
		public String REPORT_CODE;
		public String REPORT_DESC;
		public String ENTITY_FLG;
		public String MODIFY_FLG;
		public String DEL_FLG;

		public String getR11_PRODUCT() { return R11_PRODUCT; }
		public void setR11_PRODUCT(String r11_PRODUCT) { R11_PRODUCT = r11_PRODUCT; }
		public BigDecimal getR11_NV_LONG() { return R11_NV_LONG; }
		public void setR11_NV_LONG(BigDecimal r11_NV_LONG) { R11_NV_LONG = r11_NV_LONG; }
		public BigDecimal getR11_NV_SHORT() { return R11_NV_SHORT; }
		public void setR11_NV_SHORT(BigDecimal r11_NV_SHORT) { R11_NV_SHORT = r11_NV_SHORT; }
		public BigDecimal getR11_FV_LONG() { return R11_FV_LONG; }
		public void setR11_FV_LONG(BigDecimal r11_FV_LONG) { R11_FV_LONG = r11_FV_LONG; }
		public BigDecimal getR11_FV_SHORT() { return R11_FV_SHORT; }
		public void setR11_FV_SHORT(BigDecimal r11_FV_SHORT) { R11_FV_SHORT = r11_FV_SHORT; }
		public BigDecimal getR11_QFHA() { return R11_QFHA; }
		public void setR11_QFHA(BigDecimal r11_QFHA) { R11_QFHA = r11_QFHA; }
		public String getR12_PRODUCT() { return R12_PRODUCT; }
		public void setR12_PRODUCT(String r12_PRODUCT) { R12_PRODUCT = r12_PRODUCT; }
		public BigDecimal getR12_NV_LONG() { return R12_NV_LONG; }
		public void setR12_NV_LONG(BigDecimal r12_NV_LONG) { R12_NV_LONG = r12_NV_LONG; }
		public BigDecimal getR12_NV_SHORT() { return R12_NV_SHORT; }
		public void setR12_NV_SHORT(BigDecimal r12_NV_SHORT) { R12_NV_SHORT = r12_NV_SHORT; }
		public BigDecimal getR12_FV_LONG() { return R12_FV_LONG; }
		public void setR12_FV_LONG(BigDecimal r12_FV_LONG) { R12_FV_LONG = r12_FV_LONG; }
		public BigDecimal getR12_FV_SHORT() { return R12_FV_SHORT; }
		public void setR12_FV_SHORT(BigDecimal r12_FV_SHORT) { R12_FV_SHORT = r12_FV_SHORT; }
		public BigDecimal getR12_QFHA() { return R12_QFHA; }
		public void setR12_QFHA(BigDecimal r12_QFHA) { R12_QFHA = r12_QFHA; }
		public String getR13_PRODUCT() { return R13_PRODUCT; }
		public void setR13_PRODUCT(String r13_PRODUCT) { R13_PRODUCT = r13_PRODUCT; }
		public BigDecimal getR13_NV_LONG() { return R13_NV_LONG; }
		public void setR13_NV_LONG(BigDecimal r13_NV_LONG) { R13_NV_LONG = r13_NV_LONG; }
		public BigDecimal getR13_NV_SHORT() { return R13_NV_SHORT; }
		public void setR13_NV_SHORT(BigDecimal r13_NV_SHORT) { R13_NV_SHORT = r13_NV_SHORT; }
		public BigDecimal getR13_FV_LONG() { return R13_FV_LONG; }
		public void setR13_FV_LONG(BigDecimal r13_FV_LONG) { R13_FV_LONG = r13_FV_LONG; }
		public BigDecimal getR13_FV_SHORT() { return R13_FV_SHORT; }
		public void setR13_FV_SHORT(BigDecimal r13_FV_SHORT) { R13_FV_SHORT = r13_FV_SHORT; }
		public BigDecimal getR13_QFHA() { return R13_QFHA; }
		public void setR13_QFHA(BigDecimal r13_QFHA) { R13_QFHA = r13_QFHA; }
		public String getR14_PRODUCT() { return R14_PRODUCT; }
		public void setR14_PRODUCT(String r14_PRODUCT) { R14_PRODUCT = r14_PRODUCT; }
		public BigDecimal getR14_NV_LONG() { return R14_NV_LONG; }
		public void setR14_NV_LONG(BigDecimal r14_NV_LONG) { R14_NV_LONG = r14_NV_LONG; }
		public BigDecimal getR14_NV_SHORT() { return R14_NV_SHORT; }
		public void setR14_NV_SHORT(BigDecimal r14_NV_SHORT) { R14_NV_SHORT = r14_NV_SHORT; }
		public BigDecimal getR14_FV_LONG() { return R14_FV_LONG; }
		public void setR14_FV_LONG(BigDecimal r14_FV_LONG) { R14_FV_LONG = r14_FV_LONG; }
		public BigDecimal getR14_FV_SHORT() { return R14_FV_SHORT; }
		public void setR14_FV_SHORT(BigDecimal r14_FV_SHORT) { R14_FV_SHORT = r14_FV_SHORT; }
		public BigDecimal getR14_QFHA() { return R14_QFHA; }
		public void setR14_QFHA(BigDecimal r14_QFHA) { R14_QFHA = r14_QFHA; }
		public String getR15_PRODUCT() { return R15_PRODUCT; }
		public void setR15_PRODUCT(String r15_PRODUCT) { R15_PRODUCT = r15_PRODUCT; }
		public BigDecimal getR15_NV_LONG() { return R15_NV_LONG; }
		public void setR15_NV_LONG(BigDecimal r15_NV_LONG) { R15_NV_LONG = r15_NV_LONG; }
		public BigDecimal getR15_NV_SHORT() { return R15_NV_SHORT; }
		public void setR15_NV_SHORT(BigDecimal r15_NV_SHORT) { R15_NV_SHORT = r15_NV_SHORT; }
		public BigDecimal getR15_FV_LONG() { return R15_FV_LONG; }
		public void setR15_FV_LONG(BigDecimal r15_FV_LONG) { R15_FV_LONG = r15_FV_LONG; }
		public BigDecimal getR15_FV_SHORT() { return R15_FV_SHORT; }
		public void setR15_FV_SHORT(BigDecimal r15_FV_SHORT) { R15_FV_SHORT = r15_FV_SHORT; }
		public BigDecimal getR15_QFHA() { return R15_QFHA; }
		public void setR15_QFHA(BigDecimal r15_QFHA) { R15_QFHA = r15_QFHA; }
		public String getR16_PRODUCT() { return R16_PRODUCT; }
		public void setR16_PRODUCT(String r16_PRODUCT) { R16_PRODUCT = r16_PRODUCT; }
		public BigDecimal getR16_NV_LONG() { return R16_NV_LONG; }
		public void setR16_NV_LONG(BigDecimal r16_NV_LONG) { R16_NV_LONG = r16_NV_LONG; }
		public BigDecimal getR16_NV_SHORT() { return R16_NV_SHORT; }
		public void setR16_NV_SHORT(BigDecimal r16_NV_SHORT) { R16_NV_SHORT = r16_NV_SHORT; }
		public BigDecimal getR16_FV_LONG() { return R16_FV_LONG; }
		public void setR16_FV_LONG(BigDecimal r16_FV_LONG) { R16_FV_LONG = r16_FV_LONG; }
		public BigDecimal getR16_FV_SHORT() { return R16_FV_SHORT; }
		public void setR16_FV_SHORT(BigDecimal r16_FV_SHORT) { R16_FV_SHORT = r16_FV_SHORT; }
		public BigDecimal getR16_QFHA() { return R16_QFHA; }
		public void setR16_QFHA(BigDecimal r16_QFHA) { R16_QFHA = r16_QFHA; }
		public String getR17_PRODUCT() { return R17_PRODUCT; }
		public void setR17_PRODUCT(String r17_PRODUCT) { R17_PRODUCT = r17_PRODUCT; }
		public BigDecimal getR17_NV_LONG() { return R17_NV_LONG; }
		public void setR17_NV_LONG(BigDecimal r17_NV_LONG) { R17_NV_LONG = r17_NV_LONG; }
		public BigDecimal getR17_NV_SHORT() { return R17_NV_SHORT; }
		public void setR17_NV_SHORT(BigDecimal r17_NV_SHORT) { R17_NV_SHORT = r17_NV_SHORT; }
		public BigDecimal getR17_FV_LONG() { return R17_FV_LONG; }
		public void setR17_FV_LONG(BigDecimal r17_FV_LONG) { R17_FV_LONG = r17_FV_LONG; }
		public BigDecimal getR17_FV_SHORT() { return R17_FV_SHORT; }
		public void setR17_FV_SHORT(BigDecimal r17_FV_SHORT) { R17_FV_SHORT = r17_FV_SHORT; }
		public BigDecimal getR17_QFHA() { return R17_QFHA; }
		public void setR17_QFHA(BigDecimal r17_QFHA) { R17_QFHA = r17_QFHA; }
		public String getR18_PRODUCT() { return R18_PRODUCT; }
		public void setR18_PRODUCT(String r18_PRODUCT) { R18_PRODUCT = r18_PRODUCT; }
		public BigDecimal getR18_NV_LONG() { return R18_NV_LONG; }
		public void setR18_NV_LONG(BigDecimal r18_NV_LONG) { R18_NV_LONG = r18_NV_LONG; }
		public BigDecimal getR18_NV_SHORT() { return R18_NV_SHORT; }
		public void setR18_NV_SHORT(BigDecimal r18_NV_SHORT) { R18_NV_SHORT = r18_NV_SHORT; }
		public BigDecimal getR18_FV_LONG() { return R18_FV_LONG; }
		public void setR18_FV_LONG(BigDecimal r18_FV_LONG) { R18_FV_LONG = r18_FV_LONG; }
		public BigDecimal getR18_FV_SHORT() { return R18_FV_SHORT; }
		public void setR18_FV_SHORT(BigDecimal r18_FV_SHORT) { R18_FV_SHORT = r18_FV_SHORT; }
		public BigDecimal getR18_QFHA() { return R18_QFHA; }
		public void setR18_QFHA(BigDecimal r18_QFHA) { R18_QFHA = r18_QFHA; }
		public String getR19_PRODUCT() { return R19_PRODUCT; }
		public void setR19_PRODUCT(String r19_PRODUCT) { R19_PRODUCT = r19_PRODUCT; }
		public BigDecimal getR19_NV_LONG() { return R19_NV_LONG; }
		public void setR19_NV_LONG(BigDecimal r19_NV_LONG) { R19_NV_LONG = r19_NV_LONG; }
		public BigDecimal getR19_NV_SHORT() { return R19_NV_SHORT; }
		public void setR19_NV_SHORT(BigDecimal r19_NV_SHORT) { R19_NV_SHORT = r19_NV_SHORT; }
		public BigDecimal getR19_FV_LONG() { return R19_FV_LONG; }
		public void setR19_FV_LONG(BigDecimal r19_FV_LONG) { R19_FV_LONG = r19_FV_LONG; }
		public BigDecimal getR19_FV_SHORT() { return R19_FV_SHORT; }
		public void setR19_FV_SHORT(BigDecimal r19_FV_SHORT) { R19_FV_SHORT = r19_FV_SHORT; }
		public BigDecimal getR19_QFHA() { return R19_QFHA; }
		public void setR19_QFHA(BigDecimal r19_QFHA) { R19_QFHA = r19_QFHA; }
		public String getR20_PRODUCT() { return R20_PRODUCT; }
		public void setR20_PRODUCT(String r20_PRODUCT) { R20_PRODUCT = r20_PRODUCT; }
		public BigDecimal getR20_NV_LONG() { return R20_NV_LONG; }
		public void setR20_NV_LONG(BigDecimal r20_NV_LONG) { R20_NV_LONG = r20_NV_LONG; }
		public BigDecimal getR20_NV_SHORT() { return R20_NV_SHORT; }
		public void setR20_NV_SHORT(BigDecimal r20_NV_SHORT) { R20_NV_SHORT = r20_NV_SHORT; }
		public BigDecimal getR20_FV_LONG() { return R20_FV_LONG; }
		public void setR20_FV_LONG(BigDecimal r20_FV_LONG) { R20_FV_LONG = r20_FV_LONG; }
		public BigDecimal getR20_FV_SHORT() { return R20_FV_SHORT; }
		public void setR20_FV_SHORT(BigDecimal r20_FV_SHORT) { R20_FV_SHORT = r20_FV_SHORT; }
		public BigDecimal getR20_QFHA() { return R20_QFHA; }
		public void setR20_QFHA(BigDecimal r20_QFHA) { R20_QFHA = r20_QFHA; }
		public String getR21_PRODUCT() { return R21_PRODUCT; }
		public void setR21_PRODUCT(String r21_PRODUCT) { R21_PRODUCT = r21_PRODUCT; }
		public BigDecimal getR21_NV_LONG() { return R21_NV_LONG; }
		public void setR21_NV_LONG(BigDecimal r21_NV_LONG) { R21_NV_LONG = r21_NV_LONG; }
		public BigDecimal getR21_NV_SHORT() { return R21_NV_SHORT; }
		public void setR21_NV_SHORT(BigDecimal r21_NV_SHORT) { R21_NV_SHORT = r21_NV_SHORT; }
		public BigDecimal getR21_FV_LONG() { return R21_FV_LONG; }
		public void setR21_FV_LONG(BigDecimal r21_FV_LONG) { R21_FV_LONG = r21_FV_LONG; }
		public BigDecimal getR21_FV_SHORT() { return R21_FV_SHORT; }
		public void setR21_FV_SHORT(BigDecimal r21_FV_SHORT) { R21_FV_SHORT = r21_FV_SHORT; }
		public BigDecimal getR21_QFHA() { return R21_QFHA; }
		public void setR21_QFHA(BigDecimal r21_QFHA) { R21_QFHA = r21_QFHA; }
		public String getR22_PRODUCT() { return R22_PRODUCT; }
		public void setR22_PRODUCT(String r22_PRODUCT) { R22_PRODUCT = r22_PRODUCT; }
		public BigDecimal getR22_NV_LONG() { return R22_NV_LONG; }
		public void setR22_NV_LONG(BigDecimal r22_NV_LONG) { R22_NV_LONG = r22_NV_LONG; }
		public BigDecimal getR22_NV_SHORT() { return R22_NV_SHORT; }
		public void setR22_NV_SHORT(BigDecimal r22_NV_SHORT) { R22_NV_SHORT = r22_NV_SHORT; }
		public BigDecimal getR22_FV_LONG() { return R22_FV_LONG; }
		public void setR22_FV_LONG(BigDecimal r22_FV_LONG) { R22_FV_LONG = r22_FV_LONG; }
		public BigDecimal getR22_FV_SHORT() { return R22_FV_SHORT; }
		public void setR22_FV_SHORT(BigDecimal r22_FV_SHORT) { R22_FV_SHORT = r22_FV_SHORT; }
		public BigDecimal getR22_QFHA() { return R22_QFHA; }
		public void setR22_QFHA(BigDecimal r22_QFHA) { R22_QFHA = r22_QFHA; }
		public String getR23_PRODUCT() { return R23_PRODUCT; }
		public void setR23_PRODUCT(String r23_PRODUCT) { R23_PRODUCT = r23_PRODUCT; }
		public BigDecimal getR23_NV_LONG() { return R23_NV_LONG; }
		public void setR23_NV_LONG(BigDecimal r23_NV_LONG) { R23_NV_LONG = r23_NV_LONG; }
		public BigDecimal getR23_NV_SHORT() { return R23_NV_SHORT; }
		public void setR23_NV_SHORT(BigDecimal r23_NV_SHORT) { R23_NV_SHORT = r23_NV_SHORT; }
		public BigDecimal getR23_FV_LONG() { return R23_FV_LONG; }
		public void setR23_FV_LONG(BigDecimal r23_FV_LONG) { R23_FV_LONG = r23_FV_LONG; }
		public BigDecimal getR23_FV_SHORT() { return R23_FV_SHORT; }
		public void setR23_FV_SHORT(BigDecimal r23_FV_SHORT) { R23_FV_SHORT = r23_FV_SHORT; }
		public BigDecimal getR23_QFHA() { return R23_QFHA; }
		public void setR23_QFHA(BigDecimal r23_QFHA) { R23_QFHA = r23_QFHA; }
		public String getR24_PRODUCT() { return R24_PRODUCT; }
		public void setR24_PRODUCT(String r24_PRODUCT) { R24_PRODUCT = r24_PRODUCT; }
		public BigDecimal getR24_NV_LONG() { return R24_NV_LONG; }
		public void setR24_NV_LONG(BigDecimal r24_NV_LONG) { R24_NV_LONG = r24_NV_LONG; }
		public BigDecimal getR24_NV_SHORT() { return R24_NV_SHORT; }
		public void setR24_NV_SHORT(BigDecimal r24_NV_SHORT) { R24_NV_SHORT = r24_NV_SHORT; }
		public BigDecimal getR24_FV_LONG() { return R24_FV_LONG; }
		public void setR24_FV_LONG(BigDecimal r24_FV_LONG) { R24_FV_LONG = r24_FV_LONG; }
		public BigDecimal getR24_FV_SHORT() { return R24_FV_SHORT; }
		public void setR24_FV_SHORT(BigDecimal r24_FV_SHORT) { R24_FV_SHORT = r24_FV_SHORT; }
		public BigDecimal getR24_QFHA() { return R24_QFHA; }
		public void setR24_QFHA(BigDecimal r24_QFHA) { R24_QFHA = r24_QFHA; }
		public String getR25_PRODUCT() { return R25_PRODUCT; }
		public void setR25_PRODUCT(String r25_PRODUCT) { R25_PRODUCT = r25_PRODUCT; }
		public BigDecimal getR25_NV_LONG() { return R25_NV_LONG; }
		public void setR25_NV_LONG(BigDecimal r25_NV_LONG) { R25_NV_LONG = r25_NV_LONG; }
		public BigDecimal getR25_NV_SHORT() { return R25_NV_SHORT; }
		public void setR25_NV_SHORT(BigDecimal r25_NV_SHORT) { R25_NV_SHORT = r25_NV_SHORT; }
		public BigDecimal getR25_FV_LONG() { return R25_FV_LONG; }
		public void setR25_FV_LONG(BigDecimal r25_FV_LONG) { R25_FV_LONG = r25_FV_LONG; }
		public BigDecimal getR25_FV_SHORT() { return R25_FV_SHORT; }
		public void setR25_FV_SHORT(BigDecimal r25_FV_SHORT) { R25_FV_SHORT = r25_FV_SHORT; }
		public BigDecimal getR25_QFHA() { return R25_QFHA; }
		public void setR25_QFHA(BigDecimal r25_QFHA) { R25_QFHA = r25_QFHA; }
		public String getR26_PRODUCT() { return R26_PRODUCT; }
		public void setR26_PRODUCT(String r26_PRODUCT) { R26_PRODUCT = r26_PRODUCT; }
		public BigDecimal getR26_NV_LONG() { return R26_NV_LONG; }
		public void setR26_NV_LONG(BigDecimal r26_NV_LONG) { R26_NV_LONG = r26_NV_LONG; }
		public BigDecimal getR26_NV_SHORT() { return R26_NV_SHORT; }
		public void setR26_NV_SHORT(BigDecimal r26_NV_SHORT) { R26_NV_SHORT = r26_NV_SHORT; }
		public BigDecimal getR26_FV_LONG() { return R26_FV_LONG; }
		public void setR26_FV_LONG(BigDecimal r26_FV_LONG) { R26_FV_LONG = r26_FV_LONG; }
		public BigDecimal getR26_FV_SHORT() { return R26_FV_SHORT; }
		public void setR26_FV_SHORT(BigDecimal r26_FV_SHORT) { R26_FV_SHORT = r26_FV_SHORT; }
		public BigDecimal getR26_QFHA() { return R26_QFHA; }
		public void setR26_QFHA(BigDecimal r26_QFHA) { R26_QFHA = r26_QFHA; }
		public String getR27_PRODUCT() { return R27_PRODUCT; }
		public void setR27_PRODUCT(String r27_PRODUCT) { R27_PRODUCT = r27_PRODUCT; }
		public BigDecimal getR27_NV_LONG() { return R27_NV_LONG; }
		public void setR27_NV_LONG(BigDecimal r27_NV_LONG) { R27_NV_LONG = r27_NV_LONG; }
		public BigDecimal getR27_NV_SHORT() { return R27_NV_SHORT; }
		public void setR27_NV_SHORT(BigDecimal r27_NV_SHORT) { R27_NV_SHORT = r27_NV_SHORT; }
		public BigDecimal getR27_FV_LONG() { return R27_FV_LONG; }
		public void setR27_FV_LONG(BigDecimal r27_FV_LONG) { R27_FV_LONG = r27_FV_LONG; }
		public BigDecimal getR27_FV_SHORT() { return R27_FV_SHORT; }
		public void setR27_FV_SHORT(BigDecimal r27_FV_SHORT) { R27_FV_SHORT = r27_FV_SHORT; }
		public BigDecimal getR27_QFHA() { return R27_QFHA; }
		public void setR27_QFHA(BigDecimal r27_QFHA) { R27_QFHA = r27_QFHA; }
		public String getR28_PRODUCT() { return R28_PRODUCT; }
		public void setR28_PRODUCT(String r28_PRODUCT) { R28_PRODUCT = r28_PRODUCT; }
		public BigDecimal getR28_NV_LONG() { return R28_NV_LONG; }
		public void setR28_NV_LONG(BigDecimal r28_NV_LONG) { R28_NV_LONG = r28_NV_LONG; }
		public BigDecimal getR28_NV_SHORT() { return R28_NV_SHORT; }
		public void setR28_NV_SHORT(BigDecimal r28_NV_SHORT) { R28_NV_SHORT = r28_NV_SHORT; }
		public BigDecimal getR28_FV_LONG() { return R28_FV_LONG; }
		public void setR28_FV_LONG(BigDecimal r28_FV_LONG) { R28_FV_LONG = r28_FV_LONG; }
		public BigDecimal getR28_FV_SHORT() { return R28_FV_SHORT; }
		public void setR28_FV_SHORT(BigDecimal r28_FV_SHORT) { R28_FV_SHORT = r28_FV_SHORT; }
		public BigDecimal getR28_QFHA() { return R28_QFHA; }
		public void setR28_QFHA(BigDecimal r28_QFHA) { R28_QFHA = r28_QFHA; }
		public String getR29_PRODUCT() { return R29_PRODUCT; }
		public void setR29_PRODUCT(String r29_PRODUCT) { R29_PRODUCT = r29_PRODUCT; }
		public BigDecimal getR29_NV_LONG() { return R29_NV_LONG; }
		public void setR29_NV_LONG(BigDecimal r29_NV_LONG) { R29_NV_LONG = r29_NV_LONG; }
		public BigDecimal getR29_NV_SHORT() { return R29_NV_SHORT; }
		public void setR29_NV_SHORT(BigDecimal r29_NV_SHORT) { R29_NV_SHORT = r29_NV_SHORT; }
		public BigDecimal getR29_FV_LONG() { return R29_FV_LONG; }
		public void setR29_FV_LONG(BigDecimal r29_FV_LONG) { R29_FV_LONG = r29_FV_LONG; }
		public BigDecimal getR29_FV_SHORT() { return R29_FV_SHORT; }
		public void setR29_FV_SHORT(BigDecimal r29_FV_SHORT) { R29_FV_SHORT = r29_FV_SHORT; }
		public BigDecimal getR29_QFHA() { return R29_QFHA; }
		public void setR29_QFHA(BigDecimal r29_QFHA) { R29_QFHA = r29_QFHA; }
		public String getR30_PRODUCT() { return R30_PRODUCT; }
		public void setR30_PRODUCT(String r30_PRODUCT) { R30_PRODUCT = r30_PRODUCT; }
		public BigDecimal getR30_NV_LONG() { return R30_NV_LONG; }
		public void setR30_NV_LONG(BigDecimal r30_NV_LONG) { R30_NV_LONG = r30_NV_LONG; }
		public BigDecimal getR30_NV_SHORT() { return R30_NV_SHORT; }
		public void setR30_NV_SHORT(BigDecimal r30_NV_SHORT) { R30_NV_SHORT = r30_NV_SHORT; }
		public BigDecimal getR30_FV_LONG() { return R30_FV_LONG; }
		public void setR30_FV_LONG(BigDecimal r30_FV_LONG) { R30_FV_LONG = r30_FV_LONG; }
		public BigDecimal getR30_FV_SHORT() { return R30_FV_SHORT; }
		public void setR30_FV_SHORT(BigDecimal r30_FV_SHORT) { R30_FV_SHORT = r30_FV_SHORT; }
		public BigDecimal getR30_QFHA() { return R30_QFHA; }
		public void setR30_QFHA(BigDecimal r30_QFHA) { R30_QFHA = r30_QFHA; }
		public String getR31_PRODUCT() { return R31_PRODUCT; }
		public void setR31_PRODUCT(String r31_PRODUCT) { R31_PRODUCT = r31_PRODUCT; }
		public BigDecimal getR31_NV_LONG() { return R31_NV_LONG; }
		public void setR31_NV_LONG(BigDecimal r31_NV_LONG) { R31_NV_LONG = r31_NV_LONG; }
		public BigDecimal getR31_NV_SHORT() { return R31_NV_SHORT; }
		public void setR31_NV_SHORT(BigDecimal r31_NV_SHORT) { R31_NV_SHORT = r31_NV_SHORT; }
		public BigDecimal getR31_FV_LONG() { return R31_FV_LONG; }
		public void setR31_FV_LONG(BigDecimal r31_FV_LONG) { R31_FV_LONG = r31_FV_LONG; }
		public BigDecimal getR31_FV_SHORT() { return R31_FV_SHORT; }
		public void setR31_FV_SHORT(BigDecimal r31_FV_SHORT) { R31_FV_SHORT = r31_FV_SHORT; }
		public BigDecimal getR31_QFHA() { return R31_QFHA; }
		public void setR31_QFHA(BigDecimal r31_QFHA) { R31_QFHA = r31_QFHA; }
		public String getR32_PRODUCT() { return R32_PRODUCT; }
		public void setR32_PRODUCT(String r32_PRODUCT) { R32_PRODUCT = r32_PRODUCT; }
		public BigDecimal getR32_NV_LONG() { return R32_NV_LONG; }
		public void setR32_NV_LONG(BigDecimal r32_NV_LONG) { R32_NV_LONG = r32_NV_LONG; }
		public BigDecimal getR32_NV_SHORT() { return R32_NV_SHORT; }
		public void setR32_NV_SHORT(BigDecimal r32_NV_SHORT) { R32_NV_SHORT = r32_NV_SHORT; }
		public BigDecimal getR32_FV_LONG() { return R32_FV_LONG; }
		public void setR32_FV_LONG(BigDecimal r32_FV_LONG) { R32_FV_LONG = r32_FV_LONG; }
		public BigDecimal getR32_FV_SHORT() { return R32_FV_SHORT; }
		public void setR32_FV_SHORT(BigDecimal r32_FV_SHORT) { R32_FV_SHORT = r32_FV_SHORT; }
		public BigDecimal getR32_QFHA() { return R32_QFHA; }
		public void setR32_QFHA(BigDecimal r32_QFHA) { R32_QFHA = r32_QFHA; }
		public String getR33_PRODUCT() { return R33_PRODUCT; }
		public void setR33_PRODUCT(String r33_PRODUCT) { R33_PRODUCT = r33_PRODUCT; }
		public BigDecimal getR33_NV_LONG() { return R33_NV_LONG; }
		public void setR33_NV_LONG(BigDecimal r33_NV_LONG) { R33_NV_LONG = r33_NV_LONG; }
		public BigDecimal getR33_NV_SHORT() { return R33_NV_SHORT; }
		public void setR33_NV_SHORT(BigDecimal r33_NV_SHORT) { R33_NV_SHORT = r33_NV_SHORT; }
		public BigDecimal getR33_FV_LONG() { return R33_FV_LONG; }
		public void setR33_FV_LONG(BigDecimal r33_FV_LONG) { R33_FV_LONG = r33_FV_LONG; }
		public BigDecimal getR33_FV_SHORT() { return R33_FV_SHORT; }
		public void setR33_FV_SHORT(BigDecimal r33_FV_SHORT) { R33_FV_SHORT = r33_FV_SHORT; }
		public BigDecimal getR33_QFHA() { return R33_QFHA; }
		public void setR33_QFHA(BigDecimal r33_QFHA) { R33_QFHA = r33_QFHA; }
		public String getR34_PRODUCT() { return R34_PRODUCT; }
		public void setR34_PRODUCT(String r34_PRODUCT) { R34_PRODUCT = r34_PRODUCT; }
		public BigDecimal getR34_NV_LONG() { return R34_NV_LONG; }
		public void setR34_NV_LONG(BigDecimal r34_NV_LONG) { R34_NV_LONG = r34_NV_LONG; }
		public BigDecimal getR34_NV_SHORT() { return R34_NV_SHORT; }
		public void setR34_NV_SHORT(BigDecimal r34_NV_SHORT) { R34_NV_SHORT = r34_NV_SHORT; }
		public BigDecimal getR34_FV_LONG() { return R34_FV_LONG; }
		public void setR34_FV_LONG(BigDecimal r34_FV_LONG) { R34_FV_LONG = r34_FV_LONG; }
		public BigDecimal getR34_FV_SHORT() { return R34_FV_SHORT; }
		public void setR34_FV_SHORT(BigDecimal r34_FV_SHORT) { R34_FV_SHORT = r34_FV_SHORT; }
		public BigDecimal getR34_QFHA() { return R34_QFHA; }
		public void setR34_QFHA(BigDecimal r34_QFHA) { R34_QFHA = r34_QFHA; }
		public String getR35_PRODUCT() { return R35_PRODUCT; }
		public void setR35_PRODUCT(String r35_PRODUCT) { R35_PRODUCT = r35_PRODUCT; }
		public BigDecimal getR35_NV_LONG() { return R35_NV_LONG; }
		public void setR35_NV_LONG(BigDecimal r35_NV_LONG) { R35_NV_LONG = r35_NV_LONG; }
		public BigDecimal getR35_NV_SHORT() { return R35_NV_SHORT; }
		public void setR35_NV_SHORT(BigDecimal r35_NV_SHORT) { R35_NV_SHORT = r35_NV_SHORT; }
		public BigDecimal getR35_FV_LONG() { return R35_FV_LONG; }
		public void setR35_FV_LONG(BigDecimal r35_FV_LONG) { R35_FV_LONG = r35_FV_LONG; }
		public BigDecimal getR35_FV_SHORT() { return R35_FV_SHORT; }
		public void setR35_FV_SHORT(BigDecimal r35_FV_SHORT) { R35_FV_SHORT = r35_FV_SHORT; }
		public BigDecimal getR35_QFHA() { return R35_QFHA; }
		public void setR35_QFHA(BigDecimal r35_QFHA) { R35_QFHA = r35_QFHA; }
		public String getR36_PRODUCT() { return R36_PRODUCT; }
		public void setR36_PRODUCT(String r36_PRODUCT) { R36_PRODUCT = r36_PRODUCT; }
		public BigDecimal getR36_NV_LONG() { return R36_NV_LONG; }
		public void setR36_NV_LONG(BigDecimal r36_NV_LONG) { R36_NV_LONG = r36_NV_LONG; }
		public BigDecimal getR36_NV_SHORT() { return R36_NV_SHORT; }
		public void setR36_NV_SHORT(BigDecimal r36_NV_SHORT) { R36_NV_SHORT = r36_NV_SHORT; }
		public BigDecimal getR36_FV_LONG() { return R36_FV_LONG; }
		public void setR36_FV_LONG(BigDecimal r36_FV_LONG) { R36_FV_LONG = r36_FV_LONG; }
		public BigDecimal getR36_FV_SHORT() { return R36_FV_SHORT; }
		public void setR36_FV_SHORT(BigDecimal r36_FV_SHORT) { R36_FV_SHORT = r36_FV_SHORT; }
		public BigDecimal getR36_QFHA() { return R36_QFHA; }
		public void setR36_QFHA(BigDecimal r36_QFHA) { R36_QFHA = r36_QFHA; }
		public String getR37_PRODUCT() { return R37_PRODUCT; }
		public void setR37_PRODUCT(String r37_PRODUCT) { R37_PRODUCT = r37_PRODUCT; }
		public BigDecimal getR37_NV_LONG() { return R37_NV_LONG; }
		public void setR37_NV_LONG(BigDecimal r37_NV_LONG) { R37_NV_LONG = r37_NV_LONG; }
		public BigDecimal getR37_NV_SHORT() { return R37_NV_SHORT; }
		public void setR37_NV_SHORT(BigDecimal r37_NV_SHORT) { R37_NV_SHORT = r37_NV_SHORT; }
		public BigDecimal getR37_FV_LONG() { return R37_FV_LONG; }
		public void setR37_FV_LONG(BigDecimal r37_FV_LONG) { R37_FV_LONG = r37_FV_LONG; }
		public BigDecimal getR37_FV_SHORT() { return R37_FV_SHORT; }
		public void setR37_FV_SHORT(BigDecimal r37_FV_SHORT) { R37_FV_SHORT = r37_FV_SHORT; }
		public BigDecimal getR37_QFHA() { return R37_QFHA; }
		public void setR37_QFHA(BigDecimal r37_QFHA) { R37_QFHA = r37_QFHA; }
		public String getR38_PRODUCT() { return R38_PRODUCT; }
		public void setR38_PRODUCT(String r38_PRODUCT) { R38_PRODUCT = r38_PRODUCT; }
		public BigDecimal getR38_NV_LONG() { return R38_NV_LONG; }
		public void setR38_NV_LONG(BigDecimal r38_NV_LONG) { R38_NV_LONG = r38_NV_LONG; }
		public BigDecimal getR38_NV_SHORT() { return R38_NV_SHORT; }
		public void setR38_NV_SHORT(BigDecimal r38_NV_SHORT) { R38_NV_SHORT = r38_NV_SHORT; }
		public BigDecimal getR38_FV_LONG() { return R38_FV_LONG; }
		public void setR38_FV_LONG(BigDecimal r38_FV_LONG) { R38_FV_LONG = r38_FV_LONG; }
		public BigDecimal getR38_FV_SHORT() { return R38_FV_SHORT; }
		public void setR38_FV_SHORT(BigDecimal r38_FV_SHORT) { R38_FV_SHORT = r38_FV_SHORT; }
		public BigDecimal getR38_QFHA() { return R38_QFHA; }
		public void setR38_QFHA(BigDecimal r38_QFHA) { R38_QFHA = r38_QFHA; }
		public String getR39_PRODUCT() { return R39_PRODUCT; }
		public void setR39_PRODUCT(String r39_PRODUCT) { R39_PRODUCT = r39_PRODUCT; }
		public BigDecimal getR39_NV_LONG() { return R39_NV_LONG; }
		public void setR39_NV_LONG(BigDecimal r39_NV_LONG) { R39_NV_LONG = r39_NV_LONG; }
		public BigDecimal getR39_NV_SHORT() { return R39_NV_SHORT; }
		public void setR39_NV_SHORT(BigDecimal r39_NV_SHORT) { R39_NV_SHORT = r39_NV_SHORT; }
		public BigDecimal getR39_FV_LONG() { return R39_FV_LONG; }
		public void setR39_FV_LONG(BigDecimal r39_FV_LONG) { R39_FV_LONG = r39_FV_LONG; }
		public BigDecimal getR39_FV_SHORT() { return R39_FV_SHORT; }
		public void setR39_FV_SHORT(BigDecimal r39_FV_SHORT) { R39_FV_SHORT = r39_FV_SHORT; }
		public BigDecimal getR39_QFHA() { return R39_QFHA; }
		public void setR39_QFHA(BigDecimal r39_QFHA) { R39_QFHA = r39_QFHA; }
		public String getR40_PRODUCT() { return R40_PRODUCT; }
		public void setR40_PRODUCT(String r40_PRODUCT) { R40_PRODUCT = r40_PRODUCT; }
		public BigDecimal getR40_NV_LONG() { return R40_NV_LONG; }
		public void setR40_NV_LONG(BigDecimal r40_NV_LONG) { R40_NV_LONG = r40_NV_LONG; }
		public BigDecimal getR40_NV_SHORT() { return R40_NV_SHORT; }
		public void setR40_NV_SHORT(BigDecimal r40_NV_SHORT) { R40_NV_SHORT = r40_NV_SHORT; }
		public BigDecimal getR40_FV_LONG() { return R40_FV_LONG; }
		public void setR40_FV_LONG(BigDecimal r40_FV_LONG) { R40_FV_LONG = r40_FV_LONG; }
		public BigDecimal getR40_FV_SHORT() { return R40_FV_SHORT; }
		public void setR40_FV_SHORT(BigDecimal r40_FV_SHORT) { R40_FV_SHORT = r40_FV_SHORT; }
		public BigDecimal getR40_QFHA() { return R40_QFHA; }
		public void setR40_QFHA(BigDecimal r40_QFHA) { R40_QFHA = r40_QFHA; }
		public String getR41_PRODUCT() { return R41_PRODUCT; }
		public void setR41_PRODUCT(String r41_PRODUCT) { R41_PRODUCT = r41_PRODUCT; }
		public BigDecimal getR41_NV_LONG() { return R41_NV_LONG; }
		public void setR41_NV_LONG(BigDecimal r41_NV_LONG) { R41_NV_LONG = r41_NV_LONG; }
		public BigDecimal getR41_NV_SHORT() { return R41_NV_SHORT; }
		public void setR41_NV_SHORT(BigDecimal r41_NV_SHORT) { R41_NV_SHORT = r41_NV_SHORT; }
		public BigDecimal getR41_FV_LONG() { return R41_FV_LONG; }
		public void setR41_FV_LONG(BigDecimal r41_FV_LONG) { R41_FV_LONG = r41_FV_LONG; }
		public BigDecimal getR41_FV_SHORT() { return R41_FV_SHORT; }
		public void setR41_FV_SHORT(BigDecimal r41_FV_SHORT) { R41_FV_SHORT = r41_FV_SHORT; }
		public BigDecimal getR41_QFHA() { return R41_QFHA; }
		public void setR41_QFHA(BigDecimal r41_QFHA) { R41_QFHA = r41_QFHA; }
		public String getR42_PRODUCT() { return R42_PRODUCT; }
		public void setR42_PRODUCT(String r42_PRODUCT) { R42_PRODUCT = r42_PRODUCT; }
		public BigDecimal getR42_NV_LONG() { return R42_NV_LONG; }
		public void setR42_NV_LONG(BigDecimal r42_NV_LONG) { R42_NV_LONG = r42_NV_LONG; }
		public BigDecimal getR42_NV_SHORT() { return R42_NV_SHORT; }
		public void setR42_NV_SHORT(BigDecimal r42_NV_SHORT) { R42_NV_SHORT = r42_NV_SHORT; }
		public BigDecimal getR42_FV_LONG() { return R42_FV_LONG; }
		public void setR42_FV_LONG(BigDecimal r42_FV_LONG) { R42_FV_LONG = r42_FV_LONG; }
		public BigDecimal getR42_FV_SHORT() { return R42_FV_SHORT; }
		public void setR42_FV_SHORT(BigDecimal r42_FV_SHORT) { R42_FV_SHORT = r42_FV_SHORT; }
		public BigDecimal getR42_QFHA() { return R42_QFHA; }
		public void setR42_QFHA(BigDecimal r42_QFHA) { R42_QFHA = r42_QFHA; }
		public String getR43_PRODUCT() { return R43_PRODUCT; }
		public void setR43_PRODUCT(String r43_PRODUCT) { R43_PRODUCT = r43_PRODUCT; }
		public BigDecimal getR43_NV_LONG() { return R43_NV_LONG; }
		public void setR43_NV_LONG(BigDecimal r43_NV_LONG) { R43_NV_LONG = r43_NV_LONG; }
		public BigDecimal getR43_NV_SHORT() { return R43_NV_SHORT; }
		public void setR43_NV_SHORT(BigDecimal r43_NV_SHORT) { R43_NV_SHORT = r43_NV_SHORT; }
		public BigDecimal getR43_FV_LONG() { return R43_FV_LONG; }
		public void setR43_FV_LONG(BigDecimal r43_FV_LONG) { R43_FV_LONG = r43_FV_LONG; }
		public BigDecimal getR43_FV_SHORT() { return R43_FV_SHORT; }
		public void setR43_FV_SHORT(BigDecimal r43_FV_SHORT) { R43_FV_SHORT = r43_FV_SHORT; }
		public BigDecimal getR43_QFHA() { return R43_QFHA; }
		public void setR43_QFHA(BigDecimal r43_QFHA) { R43_QFHA = r43_QFHA; }
		public String getR44_PRODUCT() { return R44_PRODUCT; }
		public void setR44_PRODUCT(String r44_PRODUCT) { R44_PRODUCT = r44_PRODUCT; }
		public BigDecimal getR44_NV_LONG() { return R44_NV_LONG; }
		public void setR44_NV_LONG(BigDecimal r44_NV_LONG) { R44_NV_LONG = r44_NV_LONG; }
		public BigDecimal getR44_NV_SHORT() { return R44_NV_SHORT; }
		public void setR44_NV_SHORT(BigDecimal r44_NV_SHORT) { R44_NV_SHORT = r44_NV_SHORT; }
		public BigDecimal getR44_FV_LONG() { return R44_FV_LONG; }
		public void setR44_FV_LONG(BigDecimal r44_FV_LONG) { R44_FV_LONG = r44_FV_LONG; }
		public BigDecimal getR44_FV_SHORT() { return R44_FV_SHORT; }
		public void setR44_FV_SHORT(BigDecimal r44_FV_SHORT) { R44_FV_SHORT = r44_FV_SHORT; }
		public BigDecimal getR44_QFHA() { return R44_QFHA; }
		public void setR44_QFHA(BigDecimal r44_QFHA) { R44_QFHA = r44_QFHA; }
		public String getR45_PRODUCT() { return R45_PRODUCT; }
		public void setR45_PRODUCT(String r45_PRODUCT) { R45_PRODUCT = r45_PRODUCT; }
		public BigDecimal getR45_NV_LONG() { return R45_NV_LONG; }
		public void setR45_NV_LONG(BigDecimal r45_NV_LONG) { R45_NV_LONG = r45_NV_LONG; }
		public BigDecimal getR45_NV_SHORT() { return R45_NV_SHORT; }
		public void setR45_NV_SHORT(BigDecimal r45_NV_SHORT) { R45_NV_SHORT = r45_NV_SHORT; }
		public BigDecimal getR45_FV_LONG() { return R45_FV_LONG; }
		public void setR45_FV_LONG(BigDecimal r45_FV_LONG) { R45_FV_LONG = r45_FV_LONG; }
		public BigDecimal getR45_FV_SHORT() { return R45_FV_SHORT; }
		public void setR45_FV_SHORT(BigDecimal r45_FV_SHORT) { R45_FV_SHORT = r45_FV_SHORT; }
		public BigDecimal getR45_QFHA() { return R45_QFHA; }
		public void setR45_QFHA(BigDecimal r45_QFHA) { R45_QFHA = r45_QFHA; }
		public String getR46_PRODUCT() { return R46_PRODUCT; }
		public void setR46_PRODUCT(String r46_PRODUCT) { R46_PRODUCT = r46_PRODUCT; }
		public BigDecimal getR46_NV_LONG() { return R46_NV_LONG; }
		public void setR46_NV_LONG(BigDecimal r46_NV_LONG) { R46_NV_LONG = r46_NV_LONG; }
		public BigDecimal getR46_NV_SHORT() { return R46_NV_SHORT; }
		public void setR46_NV_SHORT(BigDecimal r46_NV_SHORT) { R46_NV_SHORT = r46_NV_SHORT; }
		public BigDecimal getR46_FV_LONG() { return R46_FV_LONG; }
		public void setR46_FV_LONG(BigDecimal r46_FV_LONG) { R46_FV_LONG = r46_FV_LONG; }
		public BigDecimal getR46_FV_SHORT() { return R46_FV_SHORT; }
		public void setR46_FV_SHORT(BigDecimal r46_FV_SHORT) { R46_FV_SHORT = r46_FV_SHORT; }
		public BigDecimal getR46_QFHA() { return R46_QFHA; }
		public void setR46_QFHA(BigDecimal r46_QFHA) { R46_QFHA = r46_QFHA; }
		public String getR47_PRODUCT() { return R47_PRODUCT; }
		public void setR47_PRODUCT(String r47_PRODUCT) { R47_PRODUCT = r47_PRODUCT; }
		public BigDecimal getR47_NV_LONG() { return R47_NV_LONG; }
		public void setR47_NV_LONG(BigDecimal r47_NV_LONG) { R47_NV_LONG = r47_NV_LONG; }
		public BigDecimal getR47_NV_SHORT() { return R47_NV_SHORT; }
		public void setR47_NV_SHORT(BigDecimal r47_NV_SHORT) { R47_NV_SHORT = r47_NV_SHORT; }
		public BigDecimal getR47_FV_LONG() { return R47_FV_LONG; }
		public void setR47_FV_LONG(BigDecimal r47_FV_LONG) { R47_FV_LONG = r47_FV_LONG; }
		public BigDecimal getR47_FV_SHORT() { return R47_FV_SHORT; }
		public void setR47_FV_SHORT(BigDecimal r47_FV_SHORT) { R47_FV_SHORT = r47_FV_SHORT; }
		public BigDecimal getR47_QFHA() { return R47_QFHA; }
		public void setR47_QFHA(BigDecimal r47_QFHA) { R47_QFHA = r47_QFHA; }
		public String getR48_PRODUCT() { return R48_PRODUCT; }
		public void setR48_PRODUCT(String r48_PRODUCT) { R48_PRODUCT = r48_PRODUCT; }
		public BigDecimal getR48_NV_LONG() { return R48_NV_LONG; }
		public void setR48_NV_LONG(BigDecimal r48_NV_LONG) { R48_NV_LONG = r48_NV_LONG; }
		public BigDecimal getR48_NV_SHORT() { return R48_NV_SHORT; }
		public void setR48_NV_SHORT(BigDecimal r48_NV_SHORT) { R48_NV_SHORT = r48_NV_SHORT; }
		public BigDecimal getR48_FV_LONG() { return R48_FV_LONG; }
		public void setR48_FV_LONG(BigDecimal r48_FV_LONG) { R48_FV_LONG = r48_FV_LONG; }
		public BigDecimal getR48_FV_SHORT() { return R48_FV_SHORT; }
		public void setR48_FV_SHORT(BigDecimal r48_FV_SHORT) { R48_FV_SHORT = r48_FV_SHORT; }
		public BigDecimal getR48_QFHA() { return R48_QFHA; }
		public void setR48_QFHA(BigDecimal r48_QFHA) { R48_QFHA = r48_QFHA; }
		public String getR49_PRODUCT() { return R49_PRODUCT; }
		public void setR49_PRODUCT(String r49_PRODUCT) { R49_PRODUCT = r49_PRODUCT; }
		public BigDecimal getR49_NV_LONG() { return R49_NV_LONG; }
		public void setR49_NV_LONG(BigDecimal r49_NV_LONG) { R49_NV_LONG = r49_NV_LONG; }
		public BigDecimal getR49_NV_SHORT() { return R49_NV_SHORT; }
		public void setR49_NV_SHORT(BigDecimal r49_NV_SHORT) { R49_NV_SHORT = r49_NV_SHORT; }
		public BigDecimal getR49_FV_LONG() { return R49_FV_LONG; }
		public void setR49_FV_LONG(BigDecimal r49_FV_LONG) { R49_FV_LONG = r49_FV_LONG; }
		public BigDecimal getR49_FV_SHORT() { return R49_FV_SHORT; }
		public void setR49_FV_SHORT(BigDecimal r49_FV_SHORT) { R49_FV_SHORT = r49_FV_SHORT; }
		public BigDecimal getR49_QFHA() { return R49_QFHA; }
		public void setR49_QFHA(BigDecimal r49_QFHA) { R49_QFHA = r49_QFHA; }
		public String getR50_PRODUCT() { return R50_PRODUCT; }
		public void setR50_PRODUCT(String r50_PRODUCT) { R50_PRODUCT = r50_PRODUCT; }
		public BigDecimal getR50_NV_LONG() { return R50_NV_LONG; }
		public void setR50_NV_LONG(BigDecimal r50_NV_LONG) { R50_NV_LONG = r50_NV_LONG; }
		public BigDecimal getR50_NV_SHORT() { return R50_NV_SHORT; }
		public void setR50_NV_SHORT(BigDecimal r50_NV_SHORT) { R50_NV_SHORT = r50_NV_SHORT; }
		public BigDecimal getR50_FV_LONG() { return R50_FV_LONG; }
		public void setR50_FV_LONG(BigDecimal r50_FV_LONG) { R50_FV_LONG = r50_FV_LONG; }
		public BigDecimal getR50_FV_SHORT() { return R50_FV_SHORT; }
		public void setR50_FV_SHORT(BigDecimal r50_FV_SHORT) { R50_FV_SHORT = r50_FV_SHORT; }
		public BigDecimal getR50_QFHA() { return R50_QFHA; }
		public void setR50_QFHA(BigDecimal r50_QFHA) { R50_QFHA = r50_QFHA; }
		public String getR51_PRODUCT() { return R51_PRODUCT; }
		public void setR51_PRODUCT(String r51_PRODUCT) { R51_PRODUCT = r51_PRODUCT; }
		public BigDecimal getR51_NV_LONG() { return R51_NV_LONG; }
		public void setR51_NV_LONG(BigDecimal r51_NV_LONG) { R51_NV_LONG = r51_NV_LONG; }
		public BigDecimal getR51_NV_SHORT() { return R51_NV_SHORT; }
		public void setR51_NV_SHORT(BigDecimal r51_NV_SHORT) { R51_NV_SHORT = r51_NV_SHORT; }
		public BigDecimal getR51_FV_LONG() { return R51_FV_LONG; }
		public void setR51_FV_LONG(BigDecimal r51_FV_LONG) { R51_FV_LONG = r51_FV_LONG; }
		public BigDecimal getR51_FV_SHORT() { return R51_FV_SHORT; }
		public void setR51_FV_SHORT(BigDecimal r51_FV_SHORT) { R51_FV_SHORT = r51_FV_SHORT; }
		public BigDecimal getR51_QFHA() { return R51_QFHA; }
		public void setR51_QFHA(BigDecimal r51_QFHA) { R51_QFHA = r51_QFHA; }
		public String getR52_PRODUCT() { return R52_PRODUCT; }
		public void setR52_PRODUCT(String r52_PRODUCT) { R52_PRODUCT = r52_PRODUCT; }
		public BigDecimal getR52_NV_LONG() { return R52_NV_LONG; }
		public void setR52_NV_LONG(BigDecimal r52_NV_LONG) { R52_NV_LONG = r52_NV_LONG; }
		public BigDecimal getR52_NV_SHORT() { return R52_NV_SHORT; }
		public void setR52_NV_SHORT(BigDecimal r52_NV_SHORT) { R52_NV_SHORT = r52_NV_SHORT; }
		public BigDecimal getR52_FV_LONG() { return R52_FV_LONG; }
		public void setR52_FV_LONG(BigDecimal r52_FV_LONG) { R52_FV_LONG = r52_FV_LONG; }
		public BigDecimal getR52_FV_SHORT() { return R52_FV_SHORT; }
		public void setR52_FV_SHORT(BigDecimal r52_FV_SHORT) { R52_FV_SHORT = r52_FV_SHORT; }
		public BigDecimal getR52_QFHA() { return R52_QFHA; }
		public void setR52_QFHA(BigDecimal r52_QFHA) { R52_QFHA = r52_QFHA; }
		public String getR53_PRODUCT() { return R53_PRODUCT; }
		public void setR53_PRODUCT(String r53_PRODUCT) { R53_PRODUCT = r53_PRODUCT; }
		public BigDecimal getR53_NV_LONG() { return R53_NV_LONG; }
		public void setR53_NV_LONG(BigDecimal r53_NV_LONG) { R53_NV_LONG = r53_NV_LONG; }
		public BigDecimal getR53_NV_SHORT() { return R53_NV_SHORT; }
		public void setR53_NV_SHORT(BigDecimal r53_NV_SHORT) { R53_NV_SHORT = r53_NV_SHORT; }
		public BigDecimal getR53_FV_LONG() { return R53_FV_LONG; }
		public void setR53_FV_LONG(BigDecimal r53_FV_LONG) { R53_FV_LONG = r53_FV_LONG; }
		public BigDecimal getR53_FV_SHORT() { return R53_FV_SHORT; }
		public void setR53_FV_SHORT(BigDecimal r53_FV_SHORT) { R53_FV_SHORT = r53_FV_SHORT; }
		public BigDecimal getR53_QFHA() { return R53_QFHA; }
		public void setR53_QFHA(BigDecimal r53_QFHA) { R53_QFHA = r53_QFHA; }
		public String getR54_PRODUCT() { return R54_PRODUCT; }
		public void setR54_PRODUCT(String r54_PRODUCT) { R54_PRODUCT = r54_PRODUCT; }
		public BigDecimal getR54_NV_LONG() { return R54_NV_LONG; }
		public void setR54_NV_LONG(BigDecimal r54_NV_LONG) { R54_NV_LONG = r54_NV_LONG; }
		public BigDecimal getR54_NV_SHORT() { return R54_NV_SHORT; }
		public void setR54_NV_SHORT(BigDecimal r54_NV_SHORT) { R54_NV_SHORT = r54_NV_SHORT; }
		public BigDecimal getR54_FV_LONG() { return R54_FV_LONG; }
		public void setR54_FV_LONG(BigDecimal r54_FV_LONG) { R54_FV_LONG = r54_FV_LONG; }
		public BigDecimal getR54_FV_SHORT() { return R54_FV_SHORT; }
		public void setR54_FV_SHORT(BigDecimal r54_FV_SHORT) { R54_FV_SHORT = r54_FV_SHORT; }
		public BigDecimal getR54_QFHA() { return R54_QFHA; }
		public void setR54_QFHA(BigDecimal r54_QFHA) { R54_QFHA = r54_QFHA; }
		public String getR55_PRODUCT() { return R55_PRODUCT; }
		public void setR55_PRODUCT(String r55_PRODUCT) { R55_PRODUCT = r55_PRODUCT; }
		public BigDecimal getR55_NV_LONG() { return R55_NV_LONG; }
		public void setR55_NV_LONG(BigDecimal r55_NV_LONG) { R55_NV_LONG = r55_NV_LONG; }
		public BigDecimal getR55_NV_SHORT() { return R55_NV_SHORT; }
		public void setR55_NV_SHORT(BigDecimal r55_NV_SHORT) { R55_NV_SHORT = r55_NV_SHORT; }
		public BigDecimal getR55_FV_LONG() { return R55_FV_LONG; }
		public void setR55_FV_LONG(BigDecimal r55_FV_LONG) { R55_FV_LONG = r55_FV_LONG; }
		public BigDecimal getR55_FV_SHORT() { return R55_FV_SHORT; }
		public void setR55_FV_SHORT(BigDecimal r55_FV_SHORT) { R55_FV_SHORT = r55_FV_SHORT; }
		public BigDecimal getR55_QFHA() { return R55_QFHA; }
		public void setR55_QFHA(BigDecimal r55_QFHA) { R55_QFHA = r55_QFHA; }
		public Date getReportDate() { return reportDate; }
		public void setReportDate(Date reportDate) { this.reportDate = reportDate; }
		public BigDecimal getReportVersion() { return reportVersion; }
		public void setReportVersion(BigDecimal reportVersion) { this.reportVersion = reportVersion; }
		public String getREPORT_FREQUENCY() { return REPORT_FREQUENCY; }
		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) { REPORT_FREQUENCY = rEPORT_FREQUENCY; }
		public String getREPORT_CODE() { return REPORT_CODE; }
		public void setREPORT_CODE(String rEPORT_CODE) { REPORT_CODE = rEPORT_CODE; }
		public String getREPORT_DESC() { return REPORT_DESC; }
		public void setREPORT_DESC(String rEPORT_DESC) { REPORT_DESC = rEPORT_DESC; }
		public String getENTITY_FLG() { return ENTITY_FLG; }
		public void setENTITY_FLG(String eNTITY_FLG) { ENTITY_FLG = eNTITY_FLG; }
		public String getMODIFY_FLG() { return MODIFY_FLG; }
		public void setMODIFY_FLG(String mODIFY_FLG) { MODIFY_FLG = mODIFY_FLG; }
		public String getDEL_FLG() { return DEL_FLG; }
		public void setDEL_FLG(String dEL_FLG) { DEL_FLG = dEL_FLG; }
	}

	// =========================================================
	// Inner entity: M_TBS_Detail_Entity
	// =========================================================
	public static class M_TBS_Detail_Entity {
		private String R11_PRODUCT;
		private BigDecimal R11_NV_LONG;
		private BigDecimal R11_NV_SHORT;
		private BigDecimal R11_FV_LONG;
		private BigDecimal R11_FV_SHORT;
		private BigDecimal R11_QFHA;
		private String R12_PRODUCT;
		private BigDecimal R12_NV_LONG;
		private BigDecimal R12_NV_SHORT;
		private BigDecimal R12_FV_LONG;
		private BigDecimal R12_FV_SHORT;
		private BigDecimal R12_QFHA;
		private String R13_PRODUCT;
		private BigDecimal R13_NV_LONG;
		private BigDecimal R13_NV_SHORT;
		private BigDecimal R13_FV_LONG;
		private BigDecimal R13_FV_SHORT;
		private BigDecimal R13_QFHA;
		private String R14_PRODUCT;
		private BigDecimal R14_NV_LONG;
		private BigDecimal R14_NV_SHORT;
		private BigDecimal R14_FV_LONG;
		private BigDecimal R14_FV_SHORT;
		private BigDecimal R14_QFHA;
		private String R15_PRODUCT;
		private BigDecimal R15_NV_LONG;
		private BigDecimal R15_NV_SHORT;
		private BigDecimal R15_FV_LONG;
		private BigDecimal R15_FV_SHORT;
		private BigDecimal R15_QFHA;
		private String R16_PRODUCT;
		private BigDecimal R16_NV_LONG;
		private BigDecimal R16_NV_SHORT;
		private BigDecimal R16_FV_LONG;
		private BigDecimal R16_FV_SHORT;
		private BigDecimal R16_QFHA;
		private String R17_PRODUCT;
		private BigDecimal R17_NV_LONG;
		private BigDecimal R17_NV_SHORT;
		private BigDecimal R17_FV_LONG;
		private BigDecimal R17_FV_SHORT;
		private BigDecimal R17_QFHA;
		private String R18_PRODUCT;
		private BigDecimal R18_NV_LONG;
		private BigDecimal R18_NV_SHORT;
		private BigDecimal R18_FV_LONG;
		private BigDecimal R18_FV_SHORT;
		private BigDecimal R18_QFHA;
		private String R19_PRODUCT;
		private BigDecimal R19_NV_LONG;
		private BigDecimal R19_NV_SHORT;
		private BigDecimal R19_FV_LONG;
		private BigDecimal R19_FV_SHORT;
		private BigDecimal R19_QFHA;
		private String R20_PRODUCT;
		private BigDecimal R20_NV_LONG;
		private BigDecimal R20_NV_SHORT;
		private BigDecimal R20_FV_LONG;
		private BigDecimal R20_FV_SHORT;
		private BigDecimal R20_QFHA;
		private String R21_PRODUCT;
		private BigDecimal R21_NV_LONG;
		private BigDecimal R21_NV_SHORT;
		private BigDecimal R21_FV_LONG;
		private BigDecimal R21_FV_SHORT;
		private BigDecimal R21_QFHA;
		private String R22_PRODUCT;
		private BigDecimal R22_NV_LONG;
		private BigDecimal R22_NV_SHORT;
		private BigDecimal R22_FV_LONG;
		private BigDecimal R22_FV_SHORT;
		private BigDecimal R22_QFHA;
		private String R23_PRODUCT;
		private BigDecimal R23_NV_LONG;
		private BigDecimal R23_NV_SHORT;
		private BigDecimal R23_FV_LONG;
		private BigDecimal R23_FV_SHORT;
		private BigDecimal R23_QFHA;
		private String R24_PRODUCT;
		private BigDecimal R24_NV_LONG;
		private BigDecimal R24_NV_SHORT;
		private BigDecimal R24_FV_LONG;
		private BigDecimal R24_FV_SHORT;
		private BigDecimal R24_QFHA;
		private String R25_PRODUCT;
		private BigDecimal R25_NV_LONG;
		private BigDecimal R25_NV_SHORT;
		private BigDecimal R25_FV_LONG;
		private BigDecimal R25_FV_SHORT;
		private BigDecimal R25_QFHA;
		private String R26_PRODUCT;
		private BigDecimal R26_NV_LONG;
		private BigDecimal R26_NV_SHORT;
		private BigDecimal R26_FV_LONG;
		private BigDecimal R26_FV_SHORT;
		private BigDecimal R26_QFHA;
		private String R27_PRODUCT;
		private BigDecimal R27_NV_LONG;
		private BigDecimal R27_NV_SHORT;
		private BigDecimal R27_FV_LONG;
		private BigDecimal R27_FV_SHORT;
		private BigDecimal R27_QFHA;
		private String R28_PRODUCT;
		private BigDecimal R28_NV_LONG;
		private BigDecimal R28_NV_SHORT;
		private BigDecimal R28_FV_LONG;
		private BigDecimal R28_FV_SHORT;
		private BigDecimal R28_QFHA;
		private String R29_PRODUCT;
		private BigDecimal R29_NV_LONG;
		private BigDecimal R29_NV_SHORT;
		private BigDecimal R29_FV_LONG;
		private BigDecimal R29_FV_SHORT;
		private BigDecimal R29_QFHA;
		private String R30_PRODUCT;
		private BigDecimal R30_NV_LONG;
		private BigDecimal R30_NV_SHORT;
		private BigDecimal R30_FV_LONG;
		private BigDecimal R30_FV_SHORT;
		private BigDecimal R30_QFHA;
		private String R31_PRODUCT;
		private BigDecimal R31_NV_LONG;
		private BigDecimal R31_NV_SHORT;
		private BigDecimal R31_FV_LONG;
		private BigDecimal R31_FV_SHORT;
		private BigDecimal R31_QFHA;
		private String R32_PRODUCT;
		private BigDecimal R32_NV_LONG;
		private BigDecimal R32_NV_SHORT;
		private BigDecimal R32_FV_LONG;
		private BigDecimal R32_FV_SHORT;
		private BigDecimal R32_QFHA;
		private String R33_PRODUCT;
		private BigDecimal R33_NV_LONG;
		private BigDecimal R33_NV_SHORT;
		private BigDecimal R33_FV_LONG;
		private BigDecimal R33_FV_SHORT;
		private BigDecimal R33_QFHA;
		private String R34_PRODUCT;
		private BigDecimal R34_NV_LONG;
		private BigDecimal R34_NV_SHORT;
		private BigDecimal R34_FV_LONG;
		private BigDecimal R34_FV_SHORT;
		private BigDecimal R34_QFHA;
		private String R35_PRODUCT;
		private BigDecimal R35_NV_LONG;
		private BigDecimal R35_NV_SHORT;
		private BigDecimal R35_FV_LONG;
		private BigDecimal R35_FV_SHORT;
		private BigDecimal R35_QFHA;
		private String R36_PRODUCT;
		private BigDecimal R36_NV_LONG;
		private BigDecimal R36_NV_SHORT;
		private BigDecimal R36_FV_LONG;
		private BigDecimal R36_FV_SHORT;
		private BigDecimal R36_QFHA;
		private String R37_PRODUCT;
		private BigDecimal R37_NV_LONG;
		private BigDecimal R37_NV_SHORT;
		private BigDecimal R37_FV_LONG;
		private BigDecimal R37_FV_SHORT;
		private BigDecimal R37_QFHA;
		private String R38_PRODUCT;
		private BigDecimal R38_NV_LONG;
		private BigDecimal R38_NV_SHORT;
		private BigDecimal R38_FV_LONG;
		private BigDecimal R38_FV_SHORT;
		private BigDecimal R38_QFHA;
		private String R39_PRODUCT;
		private BigDecimal R39_NV_LONG;
		private BigDecimal R39_NV_SHORT;
		private BigDecimal R39_FV_LONG;
		private BigDecimal R39_FV_SHORT;
		private BigDecimal R39_QFHA;
		private String R40_PRODUCT;
		private BigDecimal R40_NV_LONG;
		private BigDecimal R40_NV_SHORT;
		private BigDecimal R40_FV_LONG;
		private BigDecimal R40_FV_SHORT;
		private BigDecimal R40_QFHA;
		private String R41_PRODUCT;
		private BigDecimal R41_NV_LONG;
		private BigDecimal R41_NV_SHORT;
		private BigDecimal R41_FV_LONG;
		private BigDecimal R41_FV_SHORT;
		private BigDecimal R41_QFHA;
		private String R42_PRODUCT;
		private BigDecimal R42_NV_LONG;
		private BigDecimal R42_NV_SHORT;
		private BigDecimal R42_FV_LONG;
		private BigDecimal R42_FV_SHORT;
		private BigDecimal R42_QFHA;
		private String R43_PRODUCT;
		private BigDecimal R43_NV_LONG;
		private BigDecimal R43_NV_SHORT;
		private BigDecimal R43_FV_LONG;
		private BigDecimal R43_FV_SHORT;
		private BigDecimal R43_QFHA;
		private String R44_PRODUCT;
		private BigDecimal R44_NV_LONG;
		private BigDecimal R44_NV_SHORT;
		private BigDecimal R44_FV_LONG;
		private BigDecimal R44_FV_SHORT;
		private BigDecimal R44_QFHA;
		private String R45_PRODUCT;
		private BigDecimal R45_NV_LONG;
		private BigDecimal R45_NV_SHORT;
		private BigDecimal R45_FV_LONG;
		private BigDecimal R45_FV_SHORT;
		private BigDecimal R45_QFHA;
		private String R46_PRODUCT;
		private BigDecimal R46_NV_LONG;
		private BigDecimal R46_NV_SHORT;
		private BigDecimal R46_FV_LONG;
		private BigDecimal R46_FV_SHORT;
		private BigDecimal R46_QFHA;
		private String R47_PRODUCT;
		private BigDecimal R47_NV_LONG;
		private BigDecimal R47_NV_SHORT;
		private BigDecimal R47_FV_LONG;
		private BigDecimal R47_FV_SHORT;
		private BigDecimal R47_QFHA;
		private String R48_PRODUCT;
		private BigDecimal R48_NV_LONG;
		private BigDecimal R48_NV_SHORT;
		private BigDecimal R48_FV_LONG;
		private BigDecimal R48_FV_SHORT;
		private BigDecimal R48_QFHA;
		private String R49_PRODUCT;
		private BigDecimal R49_NV_LONG;
		private BigDecimal R49_NV_SHORT;
		private BigDecimal R49_FV_LONG;
		private BigDecimal R49_FV_SHORT;
		private BigDecimal R49_QFHA;
		private String R50_PRODUCT;
		private BigDecimal R50_NV_LONG;
		private BigDecimal R50_NV_SHORT;
		private BigDecimal R50_FV_LONG;
		private BigDecimal R50_FV_SHORT;
		private BigDecimal R50_QFHA;
		private String R51_PRODUCT;
		private BigDecimal R51_NV_LONG;
		private BigDecimal R51_NV_SHORT;
		private BigDecimal R51_FV_LONG;
		private BigDecimal R51_FV_SHORT;
		private BigDecimal R51_QFHA;
		private String R52_PRODUCT;
		private BigDecimal R52_NV_LONG;
		private BigDecimal R52_NV_SHORT;
		private BigDecimal R52_FV_LONG;
		private BigDecimal R52_FV_SHORT;
		private BigDecimal R52_QFHA;
		private String R53_PRODUCT;
		private BigDecimal R53_NV_LONG;
		private BigDecimal R53_NV_SHORT;
		private BigDecimal R53_FV_LONG;
		private BigDecimal R53_FV_SHORT;
		private BigDecimal R53_QFHA;
		private String R54_PRODUCT;
		private BigDecimal R54_NV_LONG;
		private BigDecimal R54_NV_SHORT;
		private BigDecimal R54_FV_LONG;
		private BigDecimal R54_FV_SHORT;
		private BigDecimal R54_QFHA;
		private String R55_PRODUCT;
		private BigDecimal R55_NV_LONG;
		private BigDecimal R55_NV_SHORT;
		private BigDecimal R55_FV_LONG;
		private BigDecimal R55_FV_SHORT;
		private BigDecimal R55_QFHA;

		private Date reportDate;
		private BigDecimal reportVersion;
		public String REPORT_FREQUENCY;
		public String REPORT_CODE;
		public String REPORT_DESC;
		public String ENTITY_FLG;
		public String MODIFY_FLG;
		public String DEL_FLG;

		public String getR11_PRODUCT() { return R11_PRODUCT; }
		public void setR11_PRODUCT(String r11_PRODUCT) { R11_PRODUCT = r11_PRODUCT; }
		public BigDecimal getR11_NV_LONG() { return R11_NV_LONG; }
		public void setR11_NV_LONG(BigDecimal r11_NV_LONG) { R11_NV_LONG = r11_NV_LONG; }
		public BigDecimal getR11_NV_SHORT() { return R11_NV_SHORT; }
		public void setR11_NV_SHORT(BigDecimal r11_NV_SHORT) { R11_NV_SHORT = r11_NV_SHORT; }
		public BigDecimal getR11_FV_LONG() { return R11_FV_LONG; }
		public void setR11_FV_LONG(BigDecimal r11_FV_LONG) { R11_FV_LONG = r11_FV_LONG; }
		public BigDecimal getR11_FV_SHORT() { return R11_FV_SHORT; }
		public void setR11_FV_SHORT(BigDecimal r11_FV_SHORT) { R11_FV_SHORT = r11_FV_SHORT; }
		public BigDecimal getR11_QFHA() { return R11_QFHA; }
		public void setR11_QFHA(BigDecimal r11_QFHA) { R11_QFHA = r11_QFHA; }
		public String getR12_PRODUCT() { return R12_PRODUCT; }
		public void setR12_PRODUCT(String r12_PRODUCT) { R12_PRODUCT = r12_PRODUCT; }
		public BigDecimal getR12_NV_LONG() { return R12_NV_LONG; }
		public void setR12_NV_LONG(BigDecimal r12_NV_LONG) { R12_NV_LONG = r12_NV_LONG; }
		public BigDecimal getR12_NV_SHORT() { return R12_NV_SHORT; }
		public void setR12_NV_SHORT(BigDecimal r12_NV_SHORT) { R12_NV_SHORT = r12_NV_SHORT; }
		public BigDecimal getR12_FV_LONG() { return R12_FV_LONG; }
		public void setR12_FV_LONG(BigDecimal r12_FV_LONG) { R12_FV_LONG = r12_FV_LONG; }
		public BigDecimal getR12_FV_SHORT() { return R12_FV_SHORT; }
		public void setR12_FV_SHORT(BigDecimal r12_FV_SHORT) { R12_FV_SHORT = r12_FV_SHORT; }
		public BigDecimal getR12_QFHA() { return R12_QFHA; }
		public void setR12_QFHA(BigDecimal r12_QFHA) { R12_QFHA = r12_QFHA; }
		public String getR13_PRODUCT() { return R13_PRODUCT; }
		public void setR13_PRODUCT(String r13_PRODUCT) { R13_PRODUCT = r13_PRODUCT; }
		public BigDecimal getR13_NV_LONG() { return R13_NV_LONG; }
		public void setR13_NV_LONG(BigDecimal r13_NV_LONG) { R13_NV_LONG = r13_NV_LONG; }
		public BigDecimal getR13_NV_SHORT() { return R13_NV_SHORT; }
		public void setR13_NV_SHORT(BigDecimal r13_NV_SHORT) { R13_NV_SHORT = r13_NV_SHORT; }
		public BigDecimal getR13_FV_LONG() { return R13_FV_LONG; }
		public void setR13_FV_LONG(BigDecimal r13_FV_LONG) { R13_FV_LONG = r13_FV_LONG; }
		public BigDecimal getR13_FV_SHORT() { return R13_FV_SHORT; }
		public void setR13_FV_SHORT(BigDecimal r13_FV_SHORT) { R13_FV_SHORT = r13_FV_SHORT; }
		public BigDecimal getR13_QFHA() { return R13_QFHA; }
		public void setR13_QFHA(BigDecimal r13_QFHA) { R13_QFHA = r13_QFHA; }
		public String getR14_PRODUCT() { return R14_PRODUCT; }
		public void setR14_PRODUCT(String r14_PRODUCT) { R14_PRODUCT = r14_PRODUCT; }
		public BigDecimal getR14_NV_LONG() { return R14_NV_LONG; }
		public void setR14_NV_LONG(BigDecimal r14_NV_LONG) { R14_NV_LONG = r14_NV_LONG; }
		public BigDecimal getR14_NV_SHORT() { return R14_NV_SHORT; }
		public void setR14_NV_SHORT(BigDecimal r14_NV_SHORT) { R14_NV_SHORT = r14_NV_SHORT; }
		public BigDecimal getR14_FV_LONG() { return R14_FV_LONG; }
		public void setR14_FV_LONG(BigDecimal r14_FV_LONG) { R14_FV_LONG = r14_FV_LONG; }
		public BigDecimal getR14_FV_SHORT() { return R14_FV_SHORT; }
		public void setR14_FV_SHORT(BigDecimal r14_FV_SHORT) { R14_FV_SHORT = r14_FV_SHORT; }
		public BigDecimal getR14_QFHA() { return R14_QFHA; }
		public void setR14_QFHA(BigDecimal r14_QFHA) { R14_QFHA = r14_QFHA; }
		public String getR15_PRODUCT() { return R15_PRODUCT; }
		public void setR15_PRODUCT(String r15_PRODUCT) { R15_PRODUCT = r15_PRODUCT; }
		public BigDecimal getR15_NV_LONG() { return R15_NV_LONG; }
		public void setR15_NV_LONG(BigDecimal r15_NV_LONG) { R15_NV_LONG = r15_NV_LONG; }
		public BigDecimal getR15_NV_SHORT() { return R15_NV_SHORT; }
		public void setR15_NV_SHORT(BigDecimal r15_NV_SHORT) { R15_NV_SHORT = r15_NV_SHORT; }
		public BigDecimal getR15_FV_LONG() { return R15_FV_LONG; }
		public void setR15_FV_LONG(BigDecimal r15_FV_LONG) { R15_FV_LONG = r15_FV_LONG; }
		public BigDecimal getR15_FV_SHORT() { return R15_FV_SHORT; }
		public void setR15_FV_SHORT(BigDecimal r15_FV_SHORT) { R15_FV_SHORT = r15_FV_SHORT; }
		public BigDecimal getR15_QFHA() { return R15_QFHA; }
		public void setR15_QFHA(BigDecimal r15_QFHA) { R15_QFHA = r15_QFHA; }
		public String getR16_PRODUCT() { return R16_PRODUCT; }
		public void setR16_PRODUCT(String r16_PRODUCT) { R16_PRODUCT = r16_PRODUCT; }
		public BigDecimal getR16_NV_LONG() { return R16_NV_LONG; }
		public void setR16_NV_LONG(BigDecimal r16_NV_LONG) { R16_NV_LONG = r16_NV_LONG; }
		public BigDecimal getR16_NV_SHORT() { return R16_NV_SHORT; }
		public void setR16_NV_SHORT(BigDecimal r16_NV_SHORT) { R16_NV_SHORT = r16_NV_SHORT; }
		public BigDecimal getR16_FV_LONG() { return R16_FV_LONG; }
		public void setR16_FV_LONG(BigDecimal r16_FV_LONG) { R16_FV_LONG = r16_FV_LONG; }
		public BigDecimal getR16_FV_SHORT() { return R16_FV_SHORT; }
		public void setR16_FV_SHORT(BigDecimal r16_FV_SHORT) { R16_FV_SHORT = r16_FV_SHORT; }
		public BigDecimal getR16_QFHA() { return R16_QFHA; }
		public void setR16_QFHA(BigDecimal r16_QFHA) { R16_QFHA = r16_QFHA; }
		public String getR17_PRODUCT() { return R17_PRODUCT; }
		public void setR17_PRODUCT(String r17_PRODUCT) { R17_PRODUCT = r17_PRODUCT; }
		public BigDecimal getR17_NV_LONG() { return R17_NV_LONG; }
		public void setR17_NV_LONG(BigDecimal r17_NV_LONG) { R17_NV_LONG = r17_NV_LONG; }
		public BigDecimal getR17_NV_SHORT() { return R17_NV_SHORT; }
		public void setR17_NV_SHORT(BigDecimal r17_NV_SHORT) { R17_NV_SHORT = r17_NV_SHORT; }
		public BigDecimal getR17_FV_LONG() { return R17_FV_LONG; }
		public void setR17_FV_LONG(BigDecimal r17_FV_LONG) { R17_FV_LONG = r17_FV_LONG; }
		public BigDecimal getR17_FV_SHORT() { return R17_FV_SHORT; }
		public void setR17_FV_SHORT(BigDecimal r17_FV_SHORT) { R17_FV_SHORT = r17_FV_SHORT; }
		public BigDecimal getR17_QFHA() { return R17_QFHA; }
		public void setR17_QFHA(BigDecimal r17_QFHA) { R17_QFHA = r17_QFHA; }
		public String getR18_PRODUCT() { return R18_PRODUCT; }
		public void setR18_PRODUCT(String r18_PRODUCT) { R18_PRODUCT = r18_PRODUCT; }
		public BigDecimal getR18_NV_LONG() { return R18_NV_LONG; }
		public void setR18_NV_LONG(BigDecimal r18_NV_LONG) { R18_NV_LONG = r18_NV_LONG; }
		public BigDecimal getR18_NV_SHORT() { return R18_NV_SHORT; }
		public void setR18_NV_SHORT(BigDecimal r18_NV_SHORT) { R18_NV_SHORT = r18_NV_SHORT; }
		public BigDecimal getR18_FV_LONG() { return R18_FV_LONG; }
		public void setR18_FV_LONG(BigDecimal r18_FV_LONG) { R18_FV_LONG = r18_FV_LONG; }
		public BigDecimal getR18_FV_SHORT() { return R18_FV_SHORT; }
		public void setR18_FV_SHORT(BigDecimal r18_FV_SHORT) { R18_FV_SHORT = r18_FV_SHORT; }
		public BigDecimal getR18_QFHA() { return R18_QFHA; }
		public void setR18_QFHA(BigDecimal r18_QFHA) { R18_QFHA = r18_QFHA; }
		public String getR19_PRODUCT() { return R19_PRODUCT; }
		public void setR19_PRODUCT(String r19_PRODUCT) { R19_PRODUCT = r19_PRODUCT; }
		public BigDecimal getR19_NV_LONG() { return R19_NV_LONG; }
		public void setR19_NV_LONG(BigDecimal r19_NV_LONG) { R19_NV_LONG = r19_NV_LONG; }
		public BigDecimal getR19_NV_SHORT() { return R19_NV_SHORT; }
		public void setR19_NV_SHORT(BigDecimal r19_NV_SHORT) { R19_NV_SHORT = r19_NV_SHORT; }
		public BigDecimal getR19_FV_LONG() { return R19_FV_LONG; }
		public void setR19_FV_LONG(BigDecimal r19_FV_LONG) { R19_FV_LONG = r19_FV_LONG; }
		public BigDecimal getR19_FV_SHORT() { return R19_FV_SHORT; }
		public void setR19_FV_SHORT(BigDecimal r19_FV_SHORT) { R19_FV_SHORT = r19_FV_SHORT; }
		public BigDecimal getR19_QFHA() { return R19_QFHA; }
		public void setR19_QFHA(BigDecimal r19_QFHA) { R19_QFHA = r19_QFHA; }
		public String getR20_PRODUCT() { return R20_PRODUCT; }
		public void setR20_PRODUCT(String r20_PRODUCT) { R20_PRODUCT = r20_PRODUCT; }
		public BigDecimal getR20_NV_LONG() { return R20_NV_LONG; }
		public void setR20_NV_LONG(BigDecimal r20_NV_LONG) { R20_NV_LONG = r20_NV_LONG; }
		public BigDecimal getR20_NV_SHORT() { return R20_NV_SHORT; }
		public void setR20_NV_SHORT(BigDecimal r20_NV_SHORT) { R20_NV_SHORT = r20_NV_SHORT; }
		public BigDecimal getR20_FV_LONG() { return R20_FV_LONG; }
		public void setR20_FV_LONG(BigDecimal r20_FV_LONG) { R20_FV_LONG = r20_FV_LONG; }
		public BigDecimal getR20_FV_SHORT() { return R20_FV_SHORT; }
		public void setR20_FV_SHORT(BigDecimal r20_FV_SHORT) { R20_FV_SHORT = r20_FV_SHORT; }
		public BigDecimal getR20_QFHA() { return R20_QFHA; }
		public void setR20_QFHA(BigDecimal r20_QFHA) { R20_QFHA = r20_QFHA; }
		public String getR21_PRODUCT() { return R21_PRODUCT; }
		public void setR21_PRODUCT(String r21_PRODUCT) { R21_PRODUCT = r21_PRODUCT; }
		public BigDecimal getR21_NV_LONG() { return R21_NV_LONG; }
		public void setR21_NV_LONG(BigDecimal r21_NV_LONG) { R21_NV_LONG = r21_NV_LONG; }
		public BigDecimal getR21_NV_SHORT() { return R21_NV_SHORT; }
		public void setR21_NV_SHORT(BigDecimal r21_NV_SHORT) { R21_NV_SHORT = r21_NV_SHORT; }
		public BigDecimal getR21_FV_LONG() { return R21_FV_LONG; }
		public void setR21_FV_LONG(BigDecimal r21_FV_LONG) { R21_FV_LONG = r21_FV_LONG; }
		public BigDecimal getR21_FV_SHORT() { return R21_FV_SHORT; }
		public void setR21_FV_SHORT(BigDecimal r21_FV_SHORT) { R21_FV_SHORT = r21_FV_SHORT; }
		public BigDecimal getR21_QFHA() { return R21_QFHA; }
		public void setR21_QFHA(BigDecimal r21_QFHA) { R21_QFHA = r21_QFHA; }
		public String getR22_PRODUCT() { return R22_PRODUCT; }
		public void setR22_PRODUCT(String r22_PRODUCT) { R22_PRODUCT = r22_PRODUCT; }
		public BigDecimal getR22_NV_LONG() { return R22_NV_LONG; }
		public void setR22_NV_LONG(BigDecimal r22_NV_LONG) { R22_NV_LONG = r22_NV_LONG; }
		public BigDecimal getR22_NV_SHORT() { return R22_NV_SHORT; }
		public void setR22_NV_SHORT(BigDecimal r22_NV_SHORT) { R22_NV_SHORT = r22_NV_SHORT; }
		public BigDecimal getR22_FV_LONG() { return R22_FV_LONG; }
		public void setR22_FV_LONG(BigDecimal r22_FV_LONG) { R22_FV_LONG = r22_FV_LONG; }
		public BigDecimal getR22_FV_SHORT() { return R22_FV_SHORT; }
		public void setR22_FV_SHORT(BigDecimal r22_FV_SHORT) { R22_FV_SHORT = r22_FV_SHORT; }
		public BigDecimal getR22_QFHA() { return R22_QFHA; }
		public void setR22_QFHA(BigDecimal r22_QFHA) { R22_QFHA = r22_QFHA; }
		public String getR23_PRODUCT() { return R23_PRODUCT; }
		public void setR23_PRODUCT(String r23_PRODUCT) { R23_PRODUCT = r23_PRODUCT; }
		public BigDecimal getR23_NV_LONG() { return R23_NV_LONG; }
		public void setR23_NV_LONG(BigDecimal r23_NV_LONG) { R23_NV_LONG = r23_NV_LONG; }
		public BigDecimal getR23_NV_SHORT() { return R23_NV_SHORT; }
		public void setR23_NV_SHORT(BigDecimal r23_NV_SHORT) { R23_NV_SHORT = r23_NV_SHORT; }
		public BigDecimal getR23_FV_LONG() { return R23_FV_LONG; }
		public void setR23_FV_LONG(BigDecimal r23_FV_LONG) { R23_FV_LONG = r23_FV_LONG; }
		public BigDecimal getR23_FV_SHORT() { return R23_FV_SHORT; }
		public void setR23_FV_SHORT(BigDecimal r23_FV_SHORT) { R23_FV_SHORT = r23_FV_SHORT; }
		public BigDecimal getR23_QFHA() { return R23_QFHA; }
		public void setR23_QFHA(BigDecimal r23_QFHA) { R23_QFHA = r23_QFHA; }
		public String getR24_PRODUCT() { return R24_PRODUCT; }
		public void setR24_PRODUCT(String r24_PRODUCT) { R24_PRODUCT = r24_PRODUCT; }
		public BigDecimal getR24_NV_LONG() { return R24_NV_LONG; }
		public void setR24_NV_LONG(BigDecimal r24_NV_LONG) { R24_NV_LONG = r24_NV_LONG; }
		public BigDecimal getR24_NV_SHORT() { return R24_NV_SHORT; }
		public void setR24_NV_SHORT(BigDecimal r24_NV_SHORT) { R24_NV_SHORT = r24_NV_SHORT; }
		public BigDecimal getR24_FV_LONG() { return R24_FV_LONG; }
		public void setR24_FV_LONG(BigDecimal r24_FV_LONG) { R24_FV_LONG = r24_FV_LONG; }
		public BigDecimal getR24_FV_SHORT() { return R24_FV_SHORT; }
		public void setR24_FV_SHORT(BigDecimal r24_FV_SHORT) { R24_FV_SHORT = r24_FV_SHORT; }
		public BigDecimal getR24_QFHA() { return R24_QFHA; }
		public void setR24_QFHA(BigDecimal r24_QFHA) { R24_QFHA = r24_QFHA; }
		public String getR25_PRODUCT() { return R25_PRODUCT; }
		public void setR25_PRODUCT(String r25_PRODUCT) { R25_PRODUCT = r25_PRODUCT; }
		public BigDecimal getR25_NV_LONG() { return R25_NV_LONG; }
		public void setR25_NV_LONG(BigDecimal r25_NV_LONG) { R25_NV_LONG = r25_NV_LONG; }
		public BigDecimal getR25_NV_SHORT() { return R25_NV_SHORT; }
		public void setR25_NV_SHORT(BigDecimal r25_NV_SHORT) { R25_NV_SHORT = r25_NV_SHORT; }
		public BigDecimal getR25_FV_LONG() { return R25_FV_LONG; }
		public void setR25_FV_LONG(BigDecimal r25_FV_LONG) { R25_FV_LONG = r25_FV_LONG; }
		public BigDecimal getR25_FV_SHORT() { return R25_FV_SHORT; }
		public void setR25_FV_SHORT(BigDecimal r25_FV_SHORT) { R25_FV_SHORT = r25_FV_SHORT; }
		public BigDecimal getR25_QFHA() { return R25_QFHA; }
		public void setR25_QFHA(BigDecimal r25_QFHA) { R25_QFHA = r25_QFHA; }
		public String getR26_PRODUCT() { return R26_PRODUCT; }
		public void setR26_PRODUCT(String r26_PRODUCT) { R26_PRODUCT = r26_PRODUCT; }
		public BigDecimal getR26_NV_LONG() { return R26_NV_LONG; }
		public void setR26_NV_LONG(BigDecimal r26_NV_LONG) { R26_NV_LONG = r26_NV_LONG; }
		public BigDecimal getR26_NV_SHORT() { return R26_NV_SHORT; }
		public void setR26_NV_SHORT(BigDecimal r26_NV_SHORT) { R26_NV_SHORT = r26_NV_SHORT; }
		public BigDecimal getR26_FV_LONG() { return R26_FV_LONG; }
		public void setR26_FV_LONG(BigDecimal r26_FV_LONG) { R26_FV_LONG = r26_FV_LONG; }
		public BigDecimal getR26_FV_SHORT() { return R26_FV_SHORT; }
		public void setR26_FV_SHORT(BigDecimal r26_FV_SHORT) { R26_FV_SHORT = r26_FV_SHORT; }
		public BigDecimal getR26_QFHA() { return R26_QFHA; }
		public void setR26_QFHA(BigDecimal r26_QFHA) { R26_QFHA = r26_QFHA; }
		public String getR27_PRODUCT() { return R27_PRODUCT; }
		public void setR27_PRODUCT(String r27_PRODUCT) { R27_PRODUCT = r27_PRODUCT; }
		public BigDecimal getR27_NV_LONG() { return R27_NV_LONG; }
		public void setR27_NV_LONG(BigDecimal r27_NV_LONG) { R27_NV_LONG = r27_NV_LONG; }
		public BigDecimal getR27_NV_SHORT() { return R27_NV_SHORT; }
		public void setR27_NV_SHORT(BigDecimal r27_NV_SHORT) { R27_NV_SHORT = r27_NV_SHORT; }
		public BigDecimal getR27_FV_LONG() { return R27_FV_LONG; }
		public void setR27_FV_LONG(BigDecimal r27_FV_LONG) { R27_FV_LONG = r27_FV_LONG; }
		public BigDecimal getR27_FV_SHORT() { return R27_FV_SHORT; }
		public void setR27_FV_SHORT(BigDecimal r27_FV_SHORT) { R27_FV_SHORT = r27_FV_SHORT; }
		public BigDecimal getR27_QFHA() { return R27_QFHA; }
		public void setR27_QFHA(BigDecimal r27_QFHA) { R27_QFHA = r27_QFHA; }
		public String getR28_PRODUCT() { return R28_PRODUCT; }
		public void setR28_PRODUCT(String r28_PRODUCT) { R28_PRODUCT = r28_PRODUCT; }
		public BigDecimal getR28_NV_LONG() { return R28_NV_LONG; }
		public void setR28_NV_LONG(BigDecimal r28_NV_LONG) { R28_NV_LONG = r28_NV_LONG; }
		public BigDecimal getR28_NV_SHORT() { return R28_NV_SHORT; }
		public void setR28_NV_SHORT(BigDecimal r28_NV_SHORT) { R28_NV_SHORT = r28_NV_SHORT; }
		public BigDecimal getR28_FV_LONG() { return R28_FV_LONG; }
		public void setR28_FV_LONG(BigDecimal r28_FV_LONG) { R28_FV_LONG = r28_FV_LONG; }
		public BigDecimal getR28_FV_SHORT() { return R28_FV_SHORT; }
		public void setR28_FV_SHORT(BigDecimal r28_FV_SHORT) { R28_FV_SHORT = r28_FV_SHORT; }
		public BigDecimal getR28_QFHA() { return R28_QFHA; }
		public void setR28_QFHA(BigDecimal r28_QFHA) { R28_QFHA = r28_QFHA; }
		public String getR29_PRODUCT() { return R29_PRODUCT; }
		public void setR29_PRODUCT(String r29_PRODUCT) { R29_PRODUCT = r29_PRODUCT; }
		public BigDecimal getR29_NV_LONG() { return R29_NV_LONG; }
		public void setR29_NV_LONG(BigDecimal r29_NV_LONG) { R29_NV_LONG = r29_NV_LONG; }
		public BigDecimal getR29_NV_SHORT() { return R29_NV_SHORT; }
		public void setR29_NV_SHORT(BigDecimal r29_NV_SHORT) { R29_NV_SHORT = r29_NV_SHORT; }
		public BigDecimal getR29_FV_LONG() { return R29_FV_LONG; }
		public void setR29_FV_LONG(BigDecimal r29_FV_LONG) { R29_FV_LONG = r29_FV_LONG; }
		public BigDecimal getR29_FV_SHORT() { return R29_FV_SHORT; }
		public void setR29_FV_SHORT(BigDecimal r29_FV_SHORT) { R29_FV_SHORT = r29_FV_SHORT; }
		public BigDecimal getR29_QFHA() { return R29_QFHA; }
		public void setR29_QFHA(BigDecimal r29_QFHA) { R29_QFHA = r29_QFHA; }
		public String getR30_PRODUCT() { return R30_PRODUCT; }
		public void setR30_PRODUCT(String r30_PRODUCT) { R30_PRODUCT = r30_PRODUCT; }
		public BigDecimal getR30_NV_LONG() { return R30_NV_LONG; }
		public void setR30_NV_LONG(BigDecimal r30_NV_LONG) { R30_NV_LONG = r30_NV_LONG; }
		public BigDecimal getR30_NV_SHORT() { return R30_NV_SHORT; }
		public void setR30_NV_SHORT(BigDecimal r30_NV_SHORT) { R30_NV_SHORT = r30_NV_SHORT; }
		public BigDecimal getR30_FV_LONG() { return R30_FV_LONG; }
		public void setR30_FV_LONG(BigDecimal r30_FV_LONG) { R30_FV_LONG = r30_FV_LONG; }
		public BigDecimal getR30_FV_SHORT() { return R30_FV_SHORT; }
		public void setR30_FV_SHORT(BigDecimal r30_FV_SHORT) { R30_FV_SHORT = r30_FV_SHORT; }
		public BigDecimal getR30_QFHA() { return R30_QFHA; }
		public void setR30_QFHA(BigDecimal r30_QFHA) { R30_QFHA = r30_QFHA; }
		public String getR31_PRODUCT() { return R31_PRODUCT; }
		public void setR31_PRODUCT(String r31_PRODUCT) { R31_PRODUCT = r31_PRODUCT; }
		public BigDecimal getR31_NV_LONG() { return R31_NV_LONG; }
		public void setR31_NV_LONG(BigDecimal r31_NV_LONG) { R31_NV_LONG = r31_NV_LONG; }
		public BigDecimal getR31_NV_SHORT() { return R31_NV_SHORT; }
		public void setR31_NV_SHORT(BigDecimal r31_NV_SHORT) { R31_NV_SHORT = r31_NV_SHORT; }
		public BigDecimal getR31_FV_LONG() { return R31_FV_LONG; }
		public void setR31_FV_LONG(BigDecimal r31_FV_LONG) { R31_FV_LONG = r31_FV_LONG; }
		public BigDecimal getR31_FV_SHORT() { return R31_FV_SHORT; }
		public void setR31_FV_SHORT(BigDecimal r31_FV_SHORT) { R31_FV_SHORT = r31_FV_SHORT; }
		public BigDecimal getR31_QFHA() { return R31_QFHA; }
		public void setR31_QFHA(BigDecimal r31_QFHA) { R31_QFHA = r31_QFHA; }
		public String getR32_PRODUCT() { return R32_PRODUCT; }
		public void setR32_PRODUCT(String r32_PRODUCT) { R32_PRODUCT = r32_PRODUCT; }
		public BigDecimal getR32_NV_LONG() { return R32_NV_LONG; }
		public void setR32_NV_LONG(BigDecimal r32_NV_LONG) { R32_NV_LONG = r32_NV_LONG; }
		public BigDecimal getR32_NV_SHORT() { return R32_NV_SHORT; }
		public void setR32_NV_SHORT(BigDecimal r32_NV_SHORT) { R32_NV_SHORT = r32_NV_SHORT; }
		public BigDecimal getR32_FV_LONG() { return R32_FV_LONG; }
		public void setR32_FV_LONG(BigDecimal r32_FV_LONG) { R32_FV_LONG = r32_FV_LONG; }
		public BigDecimal getR32_FV_SHORT() { return R32_FV_SHORT; }
		public void setR32_FV_SHORT(BigDecimal r32_FV_SHORT) { R32_FV_SHORT = r32_FV_SHORT; }
		public BigDecimal getR32_QFHA() { return R32_QFHA; }
		public void setR32_QFHA(BigDecimal r32_QFHA) { R32_QFHA = r32_QFHA; }
		public String getR33_PRODUCT() { return R33_PRODUCT; }
		public void setR33_PRODUCT(String r33_PRODUCT) { R33_PRODUCT = r33_PRODUCT; }
		public BigDecimal getR33_NV_LONG() { return R33_NV_LONG; }
		public void setR33_NV_LONG(BigDecimal r33_NV_LONG) { R33_NV_LONG = r33_NV_LONG; }
		public BigDecimal getR33_NV_SHORT() { return R33_NV_SHORT; }
		public void setR33_NV_SHORT(BigDecimal r33_NV_SHORT) { R33_NV_SHORT = r33_NV_SHORT; }
		public BigDecimal getR33_FV_LONG() { return R33_FV_LONG; }
		public void setR33_FV_LONG(BigDecimal r33_FV_LONG) { R33_FV_LONG = r33_FV_LONG; }
		public BigDecimal getR33_FV_SHORT() { return R33_FV_SHORT; }
		public void setR33_FV_SHORT(BigDecimal r33_FV_SHORT) { R33_FV_SHORT = r33_FV_SHORT; }
		public BigDecimal getR33_QFHA() { return R33_QFHA; }
		public void setR33_QFHA(BigDecimal r33_QFHA) { R33_QFHA = r33_QFHA; }
		public String getR34_PRODUCT() { return R34_PRODUCT; }
		public void setR34_PRODUCT(String r34_PRODUCT) { R34_PRODUCT = r34_PRODUCT; }
		public BigDecimal getR34_NV_LONG() { return R34_NV_LONG; }
		public void setR34_NV_LONG(BigDecimal r34_NV_LONG) { R34_NV_LONG = r34_NV_LONG; }
		public BigDecimal getR34_NV_SHORT() { return R34_NV_SHORT; }
		public void setR34_NV_SHORT(BigDecimal r34_NV_SHORT) { R34_NV_SHORT = r34_NV_SHORT; }
		public BigDecimal getR34_FV_LONG() { return R34_FV_LONG; }
		public void setR34_FV_LONG(BigDecimal r34_FV_LONG) { R34_FV_LONG = r34_FV_LONG; }
		public BigDecimal getR34_FV_SHORT() { return R34_FV_SHORT; }
		public void setR34_FV_SHORT(BigDecimal r34_FV_SHORT) { R34_FV_SHORT = r34_FV_SHORT; }
		public BigDecimal getR34_QFHA() { return R34_QFHA; }
		public void setR34_QFHA(BigDecimal r34_QFHA) { R34_QFHA = r34_QFHA; }
		public String getR35_PRODUCT() { return R35_PRODUCT; }
		public void setR35_PRODUCT(String r35_PRODUCT) { R35_PRODUCT = r35_PRODUCT; }
		public BigDecimal getR35_NV_LONG() { return R35_NV_LONG; }
		public void setR35_NV_LONG(BigDecimal r35_NV_LONG) { R35_NV_LONG = r35_NV_LONG; }
		public BigDecimal getR35_NV_SHORT() { return R35_NV_SHORT; }
		public void setR35_NV_SHORT(BigDecimal r35_NV_SHORT) { R35_NV_SHORT = r35_NV_SHORT; }
		public BigDecimal getR35_FV_LONG() { return R35_FV_LONG; }
		public void setR35_FV_LONG(BigDecimal r35_FV_LONG) { R35_FV_LONG = r35_FV_LONG; }
		public BigDecimal getR35_FV_SHORT() { return R35_FV_SHORT; }
		public void setR35_FV_SHORT(BigDecimal r35_FV_SHORT) { R35_FV_SHORT = r35_FV_SHORT; }
		public BigDecimal getR35_QFHA() { return R35_QFHA; }
		public void setR35_QFHA(BigDecimal r35_QFHA) { R35_QFHA = r35_QFHA; }
		public String getR36_PRODUCT() { return R36_PRODUCT; }
		public void setR36_PRODUCT(String r36_PRODUCT) { R36_PRODUCT = r36_PRODUCT; }
		public BigDecimal getR36_NV_LONG() { return R36_NV_LONG; }
		public void setR36_NV_LONG(BigDecimal r36_NV_LONG) { R36_NV_LONG = r36_NV_LONG; }
		public BigDecimal getR36_NV_SHORT() { return R36_NV_SHORT; }
		public void setR36_NV_SHORT(BigDecimal r36_NV_SHORT) { R36_NV_SHORT = r36_NV_SHORT; }
		public BigDecimal getR36_FV_LONG() { return R36_FV_LONG; }
		public void setR36_FV_LONG(BigDecimal r36_FV_LONG) { R36_FV_LONG = r36_FV_LONG; }
		public BigDecimal getR36_FV_SHORT() { return R36_FV_SHORT; }
		public void setR36_FV_SHORT(BigDecimal r36_FV_SHORT) { R36_FV_SHORT = r36_FV_SHORT; }
		public BigDecimal getR36_QFHA() { return R36_QFHA; }
		public void setR36_QFHA(BigDecimal r36_QFHA) { R36_QFHA = r36_QFHA; }
		public String getR37_PRODUCT() { return R37_PRODUCT; }
		public void setR37_PRODUCT(String r37_PRODUCT) { R37_PRODUCT = r37_PRODUCT; }
		public BigDecimal getR37_NV_LONG() { return R37_NV_LONG; }
		public void setR37_NV_LONG(BigDecimal r37_NV_LONG) { R37_NV_LONG = r37_NV_LONG; }
		public BigDecimal getR37_NV_SHORT() { return R37_NV_SHORT; }
		public void setR37_NV_SHORT(BigDecimal r37_NV_SHORT) { R37_NV_SHORT = r37_NV_SHORT; }
		public BigDecimal getR37_FV_LONG() { return R37_FV_LONG; }
		public void setR37_FV_LONG(BigDecimal r37_FV_LONG) { R37_FV_LONG = r37_FV_LONG; }
		public BigDecimal getR37_FV_SHORT() { return R37_FV_SHORT; }
		public void setR37_FV_SHORT(BigDecimal r37_FV_SHORT) { R37_FV_SHORT = r37_FV_SHORT; }
		public BigDecimal getR37_QFHA() { return R37_QFHA; }
		public void setR37_QFHA(BigDecimal r37_QFHA) { R37_QFHA = r37_QFHA; }
		public String getR38_PRODUCT() { return R38_PRODUCT; }
		public void setR38_PRODUCT(String r38_PRODUCT) { R38_PRODUCT = r38_PRODUCT; }
		public BigDecimal getR38_NV_LONG() { return R38_NV_LONG; }
		public void setR38_NV_LONG(BigDecimal r38_NV_LONG) { R38_NV_LONG = r38_NV_LONG; }
		public BigDecimal getR38_NV_SHORT() { return R38_NV_SHORT; }
		public void setR38_NV_SHORT(BigDecimal r38_NV_SHORT) { R38_NV_SHORT = r38_NV_SHORT; }
		public BigDecimal getR38_FV_LONG() { return R38_FV_LONG; }
		public void setR38_FV_LONG(BigDecimal r38_FV_LONG) { R38_FV_LONG = r38_FV_LONG; }
		public BigDecimal getR38_FV_SHORT() { return R38_FV_SHORT; }
		public void setR38_FV_SHORT(BigDecimal r38_FV_SHORT) { R38_FV_SHORT = r38_FV_SHORT; }
		public BigDecimal getR38_QFHA() { return R38_QFHA; }
		public void setR38_QFHA(BigDecimal r38_QFHA) { R38_QFHA = r38_QFHA; }
		public String getR39_PRODUCT() { return R39_PRODUCT; }
		public void setR39_PRODUCT(String r39_PRODUCT) { R39_PRODUCT = r39_PRODUCT; }
		public BigDecimal getR39_NV_LONG() { return R39_NV_LONG; }
		public void setR39_NV_LONG(BigDecimal r39_NV_LONG) { R39_NV_LONG = r39_NV_LONG; }
		public BigDecimal getR39_NV_SHORT() { return R39_NV_SHORT; }
		public void setR39_NV_SHORT(BigDecimal r39_NV_SHORT) { R39_NV_SHORT = r39_NV_SHORT; }
		public BigDecimal getR39_FV_LONG() { return R39_FV_LONG; }
		public void setR39_FV_LONG(BigDecimal r39_FV_LONG) { R39_FV_LONG = r39_FV_LONG; }
		public BigDecimal getR39_FV_SHORT() { return R39_FV_SHORT; }
		public void setR39_FV_SHORT(BigDecimal r39_FV_SHORT) { R39_FV_SHORT = r39_FV_SHORT; }
		public BigDecimal getR39_QFHA() { return R39_QFHA; }
		public void setR39_QFHA(BigDecimal r39_QFHA) { R39_QFHA = r39_QFHA; }
		public String getR40_PRODUCT() { return R40_PRODUCT; }
		public void setR40_PRODUCT(String r40_PRODUCT) { R40_PRODUCT = r40_PRODUCT; }
		public BigDecimal getR40_NV_LONG() { return R40_NV_LONG; }
		public void setR40_NV_LONG(BigDecimal r40_NV_LONG) { R40_NV_LONG = r40_NV_LONG; }
		public BigDecimal getR40_NV_SHORT() { return R40_NV_SHORT; }
		public void setR40_NV_SHORT(BigDecimal r40_NV_SHORT) { R40_NV_SHORT = r40_NV_SHORT; }
		public BigDecimal getR40_FV_LONG() { return R40_FV_LONG; }
		public void setR40_FV_LONG(BigDecimal r40_FV_LONG) { R40_FV_LONG = r40_FV_LONG; }
		public BigDecimal getR40_FV_SHORT() { return R40_FV_SHORT; }
		public void setR40_FV_SHORT(BigDecimal r40_FV_SHORT) { R40_FV_SHORT = r40_FV_SHORT; }
		public BigDecimal getR40_QFHA() { return R40_QFHA; }
		public void setR40_QFHA(BigDecimal r40_QFHA) { R40_QFHA = r40_QFHA; }
		public String getR41_PRODUCT() { return R41_PRODUCT; }
		public void setR41_PRODUCT(String r41_PRODUCT) { R41_PRODUCT = r41_PRODUCT; }
		public BigDecimal getR41_NV_LONG() { return R41_NV_LONG; }
		public void setR41_NV_LONG(BigDecimal r41_NV_LONG) { R41_NV_LONG = r41_NV_LONG; }
		public BigDecimal getR41_NV_SHORT() { return R41_NV_SHORT; }
		public void setR41_NV_SHORT(BigDecimal r41_NV_SHORT) { R41_NV_SHORT = r41_NV_SHORT; }
		public BigDecimal getR41_FV_LONG() { return R41_FV_LONG; }
		public void setR41_FV_LONG(BigDecimal r41_FV_LONG) { R41_FV_LONG = r41_FV_LONG; }
		public BigDecimal getR41_FV_SHORT() { return R41_FV_SHORT; }
		public void setR41_FV_SHORT(BigDecimal r41_FV_SHORT) { R41_FV_SHORT = r41_FV_SHORT; }
		public BigDecimal getR41_QFHA() { return R41_QFHA; }
		public void setR41_QFHA(BigDecimal r41_QFHA) { R41_QFHA = r41_QFHA; }
		public String getR42_PRODUCT() { return R42_PRODUCT; }
		public void setR42_PRODUCT(String r42_PRODUCT) { R42_PRODUCT = r42_PRODUCT; }
		public BigDecimal getR42_NV_LONG() { return R42_NV_LONG; }
		public void setR42_NV_LONG(BigDecimal r42_NV_LONG) { R42_NV_LONG = r42_NV_LONG; }
		public BigDecimal getR42_NV_SHORT() { return R42_NV_SHORT; }
		public void setR42_NV_SHORT(BigDecimal r42_NV_SHORT) { R42_NV_SHORT = r42_NV_SHORT; }
		public BigDecimal getR42_FV_LONG() { return R42_FV_LONG; }
		public void setR42_FV_LONG(BigDecimal r42_FV_LONG) { R42_FV_LONG = r42_FV_LONG; }
		public BigDecimal getR42_FV_SHORT() { return R42_FV_SHORT; }
		public void setR42_FV_SHORT(BigDecimal r42_FV_SHORT) { R42_FV_SHORT = r42_FV_SHORT; }
		public BigDecimal getR42_QFHA() { return R42_QFHA; }
		public void setR42_QFHA(BigDecimal r42_QFHA) { R42_QFHA = r42_QFHA; }
		public String getR43_PRODUCT() { return R43_PRODUCT; }
		public void setR43_PRODUCT(String r43_PRODUCT) { R43_PRODUCT = r43_PRODUCT; }
		public BigDecimal getR43_NV_LONG() { return R43_NV_LONG; }
		public void setR43_NV_LONG(BigDecimal r43_NV_LONG) { R43_NV_LONG = r43_NV_LONG; }
		public BigDecimal getR43_NV_SHORT() { return R43_NV_SHORT; }
		public void setR43_NV_SHORT(BigDecimal r43_NV_SHORT) { R43_NV_SHORT = r43_NV_SHORT; }
		public BigDecimal getR43_FV_LONG() { return R43_FV_LONG; }
		public void setR43_FV_LONG(BigDecimal r43_FV_LONG) { R43_FV_LONG = r43_FV_LONG; }
		public BigDecimal getR43_FV_SHORT() { return R43_FV_SHORT; }
		public void setR43_FV_SHORT(BigDecimal r43_FV_SHORT) { R43_FV_SHORT = r43_FV_SHORT; }
		public BigDecimal getR43_QFHA() { return R43_QFHA; }
		public void setR43_QFHA(BigDecimal r43_QFHA) { R43_QFHA = r43_QFHA; }
		public String getR44_PRODUCT() { return R44_PRODUCT; }
		public void setR44_PRODUCT(String r44_PRODUCT) { R44_PRODUCT = r44_PRODUCT; }
		public BigDecimal getR44_NV_LONG() { return R44_NV_LONG; }
		public void setR44_NV_LONG(BigDecimal r44_NV_LONG) { R44_NV_LONG = r44_NV_LONG; }
		public BigDecimal getR44_NV_SHORT() { return R44_NV_SHORT; }
		public void setR44_NV_SHORT(BigDecimal r44_NV_SHORT) { R44_NV_SHORT = r44_NV_SHORT; }
		public BigDecimal getR44_FV_LONG() { return R44_FV_LONG; }
		public void setR44_FV_LONG(BigDecimal r44_FV_LONG) { R44_FV_LONG = r44_FV_LONG; }
		public BigDecimal getR44_FV_SHORT() { return R44_FV_SHORT; }
		public void setR44_FV_SHORT(BigDecimal r44_FV_SHORT) { R44_FV_SHORT = r44_FV_SHORT; }
		public BigDecimal getR44_QFHA() { return R44_QFHA; }
		public void setR44_QFHA(BigDecimal r44_QFHA) { R44_QFHA = r44_QFHA; }
		public String getR45_PRODUCT() { return R45_PRODUCT; }
		public void setR45_PRODUCT(String r45_PRODUCT) { R45_PRODUCT = r45_PRODUCT; }
		public BigDecimal getR45_NV_LONG() { return R45_NV_LONG; }
		public void setR45_NV_LONG(BigDecimal r45_NV_LONG) { R45_NV_LONG = r45_NV_LONG; }
		public BigDecimal getR45_NV_SHORT() { return R45_NV_SHORT; }
		public void setR45_NV_SHORT(BigDecimal r45_NV_SHORT) { R45_NV_SHORT = r45_NV_SHORT; }
		public BigDecimal getR45_FV_LONG() { return R45_FV_LONG; }
		public void setR45_FV_LONG(BigDecimal r45_FV_LONG) { R45_FV_LONG = r45_FV_LONG; }
		public BigDecimal getR45_FV_SHORT() { return R45_FV_SHORT; }
		public void setR45_FV_SHORT(BigDecimal r45_FV_SHORT) { R45_FV_SHORT = r45_FV_SHORT; }
		public BigDecimal getR45_QFHA() { return R45_QFHA; }
		public void setR45_QFHA(BigDecimal r45_QFHA) { R45_QFHA = r45_QFHA; }
		public String getR46_PRODUCT() { return R46_PRODUCT; }
		public void setR46_PRODUCT(String r46_PRODUCT) { R46_PRODUCT = r46_PRODUCT; }
		public BigDecimal getR46_NV_LONG() { return R46_NV_LONG; }
		public void setR46_NV_LONG(BigDecimal r46_NV_LONG) { R46_NV_LONG = r46_NV_LONG; }
		public BigDecimal getR46_NV_SHORT() { return R46_NV_SHORT; }
		public void setR46_NV_SHORT(BigDecimal r46_NV_SHORT) { R46_NV_SHORT = r46_NV_SHORT; }
		public BigDecimal getR46_FV_LONG() { return R46_FV_LONG; }
		public void setR46_FV_LONG(BigDecimal r46_FV_LONG) { R46_FV_LONG = r46_FV_LONG; }
		public BigDecimal getR46_FV_SHORT() { return R46_FV_SHORT; }
		public void setR46_FV_SHORT(BigDecimal r46_FV_SHORT) { R46_FV_SHORT = r46_FV_SHORT; }
		public BigDecimal getR46_QFHA() { return R46_QFHA; }
		public void setR46_QFHA(BigDecimal r46_QFHA) { R46_QFHA = r46_QFHA; }
		public String getR47_PRODUCT() { return R47_PRODUCT; }
		public void setR47_PRODUCT(String r47_PRODUCT) { R47_PRODUCT = r47_PRODUCT; }
		public BigDecimal getR47_NV_LONG() { return R47_NV_LONG; }
		public void setR47_NV_LONG(BigDecimal r47_NV_LONG) { R47_NV_LONG = r47_NV_LONG; }
		public BigDecimal getR47_NV_SHORT() { return R47_NV_SHORT; }
		public void setR47_NV_SHORT(BigDecimal r47_NV_SHORT) { R47_NV_SHORT = r47_NV_SHORT; }
		public BigDecimal getR47_FV_LONG() { return R47_FV_LONG; }
		public void setR47_FV_LONG(BigDecimal r47_FV_LONG) { R47_FV_LONG = r47_FV_LONG; }
		public BigDecimal getR47_FV_SHORT() { return R47_FV_SHORT; }
		public void setR47_FV_SHORT(BigDecimal r47_FV_SHORT) { R47_FV_SHORT = r47_FV_SHORT; }
		public BigDecimal getR47_QFHA() { return R47_QFHA; }
		public void setR47_QFHA(BigDecimal r47_QFHA) { R47_QFHA = r47_QFHA; }
		public String getR48_PRODUCT() { return R48_PRODUCT; }
		public void setR48_PRODUCT(String r48_PRODUCT) { R48_PRODUCT = r48_PRODUCT; }
		public BigDecimal getR48_NV_LONG() { return R48_NV_LONG; }
		public void setR48_NV_LONG(BigDecimal r48_NV_LONG) { R48_NV_LONG = r48_NV_LONG; }
		public BigDecimal getR48_NV_SHORT() { return R48_NV_SHORT; }
		public void setR48_NV_SHORT(BigDecimal r48_NV_SHORT) { R48_NV_SHORT = r48_NV_SHORT; }
		public BigDecimal getR48_FV_LONG() { return R48_FV_LONG; }
		public void setR48_FV_LONG(BigDecimal r48_FV_LONG) { R48_FV_LONG = r48_FV_LONG; }
		public BigDecimal getR48_FV_SHORT() { return R48_FV_SHORT; }
		public void setR48_FV_SHORT(BigDecimal r48_FV_SHORT) { R48_FV_SHORT = r48_FV_SHORT; }
		public BigDecimal getR48_QFHA() { return R48_QFHA; }
		public void setR48_QFHA(BigDecimal r48_QFHA) { R48_QFHA = r48_QFHA; }
		public String getR49_PRODUCT() { return R49_PRODUCT; }
		public void setR49_PRODUCT(String r49_PRODUCT) { R49_PRODUCT = r49_PRODUCT; }
		public BigDecimal getR49_NV_LONG() { return R49_NV_LONG; }
		public void setR49_NV_LONG(BigDecimal r49_NV_LONG) { R49_NV_LONG = r49_NV_LONG; }
		public BigDecimal getR49_NV_SHORT() { return R49_NV_SHORT; }
		public void setR49_NV_SHORT(BigDecimal r49_NV_SHORT) { R49_NV_SHORT = r49_NV_SHORT; }
		public BigDecimal getR49_FV_LONG() { return R49_FV_LONG; }
		public void setR49_FV_LONG(BigDecimal r49_FV_LONG) { R49_FV_LONG = r49_FV_LONG; }
		public BigDecimal getR49_FV_SHORT() { return R49_FV_SHORT; }
		public void setR49_FV_SHORT(BigDecimal r49_FV_SHORT) { R49_FV_SHORT = r49_FV_SHORT; }
		public BigDecimal getR49_QFHA() { return R49_QFHA; }
		public void setR49_QFHA(BigDecimal r49_QFHA) { R49_QFHA = r49_QFHA; }
		public String getR50_PRODUCT() { return R50_PRODUCT; }
		public void setR50_PRODUCT(String r50_PRODUCT) { R50_PRODUCT = r50_PRODUCT; }
		public BigDecimal getR50_NV_LONG() { return R50_NV_LONG; }
		public void setR50_NV_LONG(BigDecimal r50_NV_LONG) { R50_NV_LONG = r50_NV_LONG; }
		public BigDecimal getR50_NV_SHORT() { return R50_NV_SHORT; }
		public void setR50_NV_SHORT(BigDecimal r50_NV_SHORT) { R50_NV_SHORT = r50_NV_SHORT; }
		public BigDecimal getR50_FV_LONG() { return R50_FV_LONG; }
		public void setR50_FV_LONG(BigDecimal r50_FV_LONG) { R50_FV_LONG = r50_FV_LONG; }
		public BigDecimal getR50_FV_SHORT() { return R50_FV_SHORT; }
		public void setR50_FV_SHORT(BigDecimal r50_FV_SHORT) { R50_FV_SHORT = r50_FV_SHORT; }
		public BigDecimal getR50_QFHA() { return R50_QFHA; }
		public void setR50_QFHA(BigDecimal r50_QFHA) { R50_QFHA = r50_QFHA; }
		public String getR51_PRODUCT() { return R51_PRODUCT; }
		public void setR51_PRODUCT(String r51_PRODUCT) { R51_PRODUCT = r51_PRODUCT; }
		public BigDecimal getR51_NV_LONG() { return R51_NV_LONG; }
		public void setR51_NV_LONG(BigDecimal r51_NV_LONG) { R51_NV_LONG = r51_NV_LONG; }
		public BigDecimal getR51_NV_SHORT() { return R51_NV_SHORT; }
		public void setR51_NV_SHORT(BigDecimal r51_NV_SHORT) { R51_NV_SHORT = r51_NV_SHORT; }
		public BigDecimal getR51_FV_LONG() { return R51_FV_LONG; }
		public void setR51_FV_LONG(BigDecimal r51_FV_LONG) { R51_FV_LONG = r51_FV_LONG; }
		public BigDecimal getR51_FV_SHORT() { return R51_FV_SHORT; }
		public void setR51_FV_SHORT(BigDecimal r51_FV_SHORT) { R51_FV_SHORT = r51_FV_SHORT; }
		public BigDecimal getR51_QFHA() { return R51_QFHA; }
		public void setR51_QFHA(BigDecimal r51_QFHA) { R51_QFHA = r51_QFHA; }
		public String getR52_PRODUCT() { return R52_PRODUCT; }
		public void setR52_PRODUCT(String r52_PRODUCT) { R52_PRODUCT = r52_PRODUCT; }
		public BigDecimal getR52_NV_LONG() { return R52_NV_LONG; }
		public void setR52_NV_LONG(BigDecimal r52_NV_LONG) { R52_NV_LONG = r52_NV_LONG; }
		public BigDecimal getR52_NV_SHORT() { return R52_NV_SHORT; }
		public void setR52_NV_SHORT(BigDecimal r52_NV_SHORT) { R52_NV_SHORT = r52_NV_SHORT; }
		public BigDecimal getR52_FV_LONG() { return R52_FV_LONG; }
		public void setR52_FV_LONG(BigDecimal r52_FV_LONG) { R52_FV_LONG = r52_FV_LONG; }
		public BigDecimal getR52_FV_SHORT() { return R52_FV_SHORT; }
		public void setR52_FV_SHORT(BigDecimal r52_FV_SHORT) { R52_FV_SHORT = r52_FV_SHORT; }
		public BigDecimal getR52_QFHA() { return R52_QFHA; }
		public void setR52_QFHA(BigDecimal r52_QFHA) { R52_QFHA = r52_QFHA; }
		public String getR53_PRODUCT() { return R53_PRODUCT; }
		public void setR53_PRODUCT(String r53_PRODUCT) { R53_PRODUCT = r53_PRODUCT; }
		public BigDecimal getR53_NV_LONG() { return R53_NV_LONG; }
		public void setR53_NV_LONG(BigDecimal r53_NV_LONG) { R53_NV_LONG = r53_NV_LONG; }
		public BigDecimal getR53_NV_SHORT() { return R53_NV_SHORT; }
		public void setR53_NV_SHORT(BigDecimal r53_NV_SHORT) { R53_NV_SHORT = r53_NV_SHORT; }
		public BigDecimal getR53_FV_LONG() { return R53_FV_LONG; }
		public void setR53_FV_LONG(BigDecimal r53_FV_LONG) { R53_FV_LONG = r53_FV_LONG; }
		public BigDecimal getR53_FV_SHORT() { return R53_FV_SHORT; }
		public void setR53_FV_SHORT(BigDecimal r53_FV_SHORT) { R53_FV_SHORT = r53_FV_SHORT; }
		public BigDecimal getR53_QFHA() { return R53_QFHA; }
		public void setR53_QFHA(BigDecimal r53_QFHA) { R53_QFHA = r53_QFHA; }
		public String getR54_PRODUCT() { return R54_PRODUCT; }
		public void setR54_PRODUCT(String r54_PRODUCT) { R54_PRODUCT = r54_PRODUCT; }
		public BigDecimal getR54_NV_LONG() { return R54_NV_LONG; }
		public void setR54_NV_LONG(BigDecimal r54_NV_LONG) { R54_NV_LONG = r54_NV_LONG; }
		public BigDecimal getR54_NV_SHORT() { return R54_NV_SHORT; }
		public void setR54_NV_SHORT(BigDecimal r54_NV_SHORT) { R54_NV_SHORT = r54_NV_SHORT; }
		public BigDecimal getR54_FV_LONG() { return R54_FV_LONG; }
		public void setR54_FV_LONG(BigDecimal r54_FV_LONG) { R54_FV_LONG = r54_FV_LONG; }
		public BigDecimal getR54_FV_SHORT() { return R54_FV_SHORT; }
		public void setR54_FV_SHORT(BigDecimal r54_FV_SHORT) { R54_FV_SHORT = r54_FV_SHORT; }
		public BigDecimal getR54_QFHA() { return R54_QFHA; }
		public void setR54_QFHA(BigDecimal r54_QFHA) { R54_QFHA = r54_QFHA; }
		public String getR55_PRODUCT() { return R55_PRODUCT; }
		public void setR55_PRODUCT(String r55_PRODUCT) { R55_PRODUCT = r55_PRODUCT; }
		public BigDecimal getR55_NV_LONG() { return R55_NV_LONG; }
		public void setR55_NV_LONG(BigDecimal r55_NV_LONG) { R55_NV_LONG = r55_NV_LONG; }
		public BigDecimal getR55_NV_SHORT() { return R55_NV_SHORT; }
		public void setR55_NV_SHORT(BigDecimal r55_NV_SHORT) { R55_NV_SHORT = r55_NV_SHORT; }
		public BigDecimal getR55_FV_LONG() { return R55_FV_LONG; }
		public void setR55_FV_LONG(BigDecimal r55_FV_LONG) { R55_FV_LONG = r55_FV_LONG; }
		public BigDecimal getR55_FV_SHORT() { return R55_FV_SHORT; }
		public void setR55_FV_SHORT(BigDecimal r55_FV_SHORT) { R55_FV_SHORT = r55_FV_SHORT; }
		public BigDecimal getR55_QFHA() { return R55_QFHA; }
		public void setR55_QFHA(BigDecimal r55_QFHA) { R55_QFHA = r55_QFHA; }
		public Date getReportDate() { return reportDate; }
		public void setReportDate(Date reportDate) { this.reportDate = reportDate; }
		public BigDecimal getReportVersion() { return reportVersion; }
		public void setReportVersion(BigDecimal reportVersion) { this.reportVersion = reportVersion; }
		public String getREPORT_FREQUENCY() { return REPORT_FREQUENCY; }
		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) { REPORT_FREQUENCY = rEPORT_FREQUENCY; }
		public String getREPORT_CODE() { return REPORT_CODE; }
		public void setREPORT_CODE(String rEPORT_CODE) { REPORT_CODE = rEPORT_CODE; }
		public String getREPORT_DESC() { return REPORT_DESC; }
		public void setREPORT_DESC(String rEPORT_DESC) { REPORT_DESC = rEPORT_DESC; }
		public String getENTITY_FLG() { return ENTITY_FLG; }
		public void setENTITY_FLG(String eNTITY_FLG) { ENTITY_FLG = eNTITY_FLG; }
		public String getMODIFY_FLG() { return MODIFY_FLG; }
		public void setMODIFY_FLG(String mODIFY_FLG) { MODIFY_FLG = mODIFY_FLG; }
		public String getDEL_FLG() { return DEL_FLG; }
		public void setDEL_FLG(String dEL_FLG) { DEL_FLG = dEL_FLG; }
	}

	// =========================================================
	// Inner entity: M_TBS_Archival_Summary_Entity
	// =========================================================
	public static class M_TBS_Archival_Summary_Entity {
		private String R11_PRODUCT;
		private BigDecimal R11_NV_LONG;
		private BigDecimal R11_NV_SHORT;
		private BigDecimal R11_FV_LONG;
		private BigDecimal R11_FV_SHORT;
		private BigDecimal R11_QFHA;
		private String R12_PRODUCT;
		private BigDecimal R12_NV_LONG;
		private BigDecimal R12_NV_SHORT;
		private BigDecimal R12_FV_LONG;
		private BigDecimal R12_FV_SHORT;
		private BigDecimal R12_QFHA;
		private String R13_PRODUCT;
		private BigDecimal R13_NV_LONG;
		private BigDecimal R13_NV_SHORT;
		private BigDecimal R13_FV_LONG;
		private BigDecimal R13_FV_SHORT;
		private BigDecimal R13_QFHA;
		private String R14_PRODUCT;
		private BigDecimal R14_NV_LONG;
		private BigDecimal R14_NV_SHORT;
		private BigDecimal R14_FV_LONG;
		private BigDecimal R14_FV_SHORT;
		private BigDecimal R14_QFHA;
		private String R15_PRODUCT;
		private BigDecimal R15_NV_LONG;
		private BigDecimal R15_NV_SHORT;
		private BigDecimal R15_FV_LONG;
		private BigDecimal R15_FV_SHORT;
		private BigDecimal R15_QFHA;
		private String R16_PRODUCT;
		private BigDecimal R16_NV_LONG;
		private BigDecimal R16_NV_SHORT;
		private BigDecimal R16_FV_LONG;
		private BigDecimal R16_FV_SHORT;
		private BigDecimal R16_QFHA;
		private String R17_PRODUCT;
		private BigDecimal R17_NV_LONG;
		private BigDecimal R17_NV_SHORT;
		private BigDecimal R17_FV_LONG;
		private BigDecimal R17_FV_SHORT;
		private BigDecimal R17_QFHA;
		private String R18_PRODUCT;
		private BigDecimal R18_NV_LONG;
		private BigDecimal R18_NV_SHORT;
		private BigDecimal R18_FV_LONG;
		private BigDecimal R18_FV_SHORT;
		private BigDecimal R18_QFHA;
		private String R19_PRODUCT;
		private BigDecimal R19_NV_LONG;
		private BigDecimal R19_NV_SHORT;
		private BigDecimal R19_FV_LONG;
		private BigDecimal R19_FV_SHORT;
		private BigDecimal R19_QFHA;
		private String R20_PRODUCT;
		private BigDecimal R20_NV_LONG;
		private BigDecimal R20_NV_SHORT;
		private BigDecimal R20_FV_LONG;
		private BigDecimal R20_FV_SHORT;
		private BigDecimal R20_QFHA;
		private String R21_PRODUCT;
		private BigDecimal R21_NV_LONG;
		private BigDecimal R21_NV_SHORT;
		private BigDecimal R21_FV_LONG;
		private BigDecimal R21_FV_SHORT;
		private BigDecimal R21_QFHA;
		private String R22_PRODUCT;
		private BigDecimal R22_NV_LONG;
		private BigDecimal R22_NV_SHORT;
		private BigDecimal R22_FV_LONG;
		private BigDecimal R22_FV_SHORT;
		private BigDecimal R22_QFHA;
		private String R23_PRODUCT;
		private BigDecimal R23_NV_LONG;
		private BigDecimal R23_NV_SHORT;
		private BigDecimal R23_FV_LONG;
		private BigDecimal R23_FV_SHORT;
		private BigDecimal R23_QFHA;
		private String R24_PRODUCT;
		private BigDecimal R24_NV_LONG;
		private BigDecimal R24_NV_SHORT;
		private BigDecimal R24_FV_LONG;
		private BigDecimal R24_FV_SHORT;
		private BigDecimal R24_QFHA;
		private String R25_PRODUCT;
		private BigDecimal R25_NV_LONG;
		private BigDecimal R25_NV_SHORT;
		private BigDecimal R25_FV_LONG;
		private BigDecimal R25_FV_SHORT;
		private BigDecimal R25_QFHA;
		private String R26_PRODUCT;
		private BigDecimal R26_NV_LONG;
		private BigDecimal R26_NV_SHORT;
		private BigDecimal R26_FV_LONG;
		private BigDecimal R26_FV_SHORT;
		private BigDecimal R26_QFHA;
		private String R27_PRODUCT;
		private BigDecimal R27_NV_LONG;
		private BigDecimal R27_NV_SHORT;
		private BigDecimal R27_FV_LONG;
		private BigDecimal R27_FV_SHORT;
		private BigDecimal R27_QFHA;
		private String R28_PRODUCT;
		private BigDecimal R28_NV_LONG;
		private BigDecimal R28_NV_SHORT;
		private BigDecimal R28_FV_LONG;
		private BigDecimal R28_FV_SHORT;
		private BigDecimal R28_QFHA;
		private String R29_PRODUCT;
		private BigDecimal R29_NV_LONG;
		private BigDecimal R29_NV_SHORT;
		private BigDecimal R29_FV_LONG;
		private BigDecimal R29_FV_SHORT;
		private BigDecimal R29_QFHA;
		private String R30_PRODUCT;
		private BigDecimal R30_NV_LONG;
		private BigDecimal R30_NV_SHORT;
		private BigDecimal R30_FV_LONG;
		private BigDecimal R30_FV_SHORT;
		private BigDecimal R30_QFHA;
		private String R31_PRODUCT;
		private BigDecimal R31_NV_LONG;
		private BigDecimal R31_NV_SHORT;
		private BigDecimal R31_FV_LONG;
		private BigDecimal R31_FV_SHORT;
		private BigDecimal R31_QFHA;
		private String R32_PRODUCT;
		private BigDecimal R32_NV_LONG;
		private BigDecimal R32_NV_SHORT;
		private BigDecimal R32_FV_LONG;
		private BigDecimal R32_FV_SHORT;
		private BigDecimal R32_QFHA;
		private String R33_PRODUCT;
		private BigDecimal R33_NV_LONG;
		private BigDecimal R33_NV_SHORT;
		private BigDecimal R33_FV_LONG;
		private BigDecimal R33_FV_SHORT;
		private BigDecimal R33_QFHA;
		private String R34_PRODUCT;
		private BigDecimal R34_NV_LONG;
		private BigDecimal R34_NV_SHORT;
		private BigDecimal R34_FV_LONG;
		private BigDecimal R34_FV_SHORT;
		private BigDecimal R34_QFHA;
		private String R35_PRODUCT;
		private BigDecimal R35_NV_LONG;
		private BigDecimal R35_NV_SHORT;
		private BigDecimal R35_FV_LONG;
		private BigDecimal R35_FV_SHORT;
		private BigDecimal R35_QFHA;
		private String R36_PRODUCT;
		private BigDecimal R36_NV_LONG;
		private BigDecimal R36_NV_SHORT;
		private BigDecimal R36_FV_LONG;
		private BigDecimal R36_FV_SHORT;
		private BigDecimal R36_QFHA;
		private String R37_PRODUCT;
		private BigDecimal R37_NV_LONG;
		private BigDecimal R37_NV_SHORT;
		private BigDecimal R37_FV_LONG;
		private BigDecimal R37_FV_SHORT;
		private BigDecimal R37_QFHA;
		private String R38_PRODUCT;
		private BigDecimal R38_NV_LONG;
		private BigDecimal R38_NV_SHORT;
		private BigDecimal R38_FV_LONG;
		private BigDecimal R38_FV_SHORT;
		private BigDecimal R38_QFHA;
		private String R39_PRODUCT;
		private BigDecimal R39_NV_LONG;
		private BigDecimal R39_NV_SHORT;
		private BigDecimal R39_FV_LONG;
		private BigDecimal R39_FV_SHORT;
		private BigDecimal R39_QFHA;
		private String R40_PRODUCT;
		private BigDecimal R40_NV_LONG;
		private BigDecimal R40_NV_SHORT;
		private BigDecimal R40_FV_LONG;
		private BigDecimal R40_FV_SHORT;
		private BigDecimal R40_QFHA;
		private String R41_PRODUCT;
		private BigDecimal R41_NV_LONG;
		private BigDecimal R41_NV_SHORT;
		private BigDecimal R41_FV_LONG;
		private BigDecimal R41_FV_SHORT;
		private BigDecimal R41_QFHA;
		private String R42_PRODUCT;
		private BigDecimal R42_NV_LONG;
		private BigDecimal R42_NV_SHORT;
		private BigDecimal R42_FV_LONG;
		private BigDecimal R42_FV_SHORT;
		private BigDecimal R42_QFHA;
		private String R43_PRODUCT;
		private BigDecimal R43_NV_LONG;
		private BigDecimal R43_NV_SHORT;
		private BigDecimal R43_FV_LONG;
		private BigDecimal R43_FV_SHORT;
		private BigDecimal R43_QFHA;
		private String R44_PRODUCT;
		private BigDecimal R44_NV_LONG;
		private BigDecimal R44_NV_SHORT;
		private BigDecimal R44_FV_LONG;
		private BigDecimal R44_FV_SHORT;
		private BigDecimal R44_QFHA;
		private String R45_PRODUCT;
		private BigDecimal R45_NV_LONG;
		private BigDecimal R45_NV_SHORT;
		private BigDecimal R45_FV_LONG;
		private BigDecimal R45_FV_SHORT;
		private BigDecimal R45_QFHA;
		private String R46_PRODUCT;
		private BigDecimal R46_NV_LONG;
		private BigDecimal R46_NV_SHORT;
		private BigDecimal R46_FV_LONG;
		private BigDecimal R46_FV_SHORT;
		private BigDecimal R46_QFHA;
		private String R47_PRODUCT;
		private BigDecimal R47_NV_LONG;
		private BigDecimal R47_NV_SHORT;
		private BigDecimal R47_FV_LONG;
		private BigDecimal R47_FV_SHORT;
		private BigDecimal R47_QFHA;
		private String R48_PRODUCT;
		private BigDecimal R48_NV_LONG;
		private BigDecimal R48_NV_SHORT;
		private BigDecimal R48_FV_LONG;
		private BigDecimal R48_FV_SHORT;
		private BigDecimal R48_QFHA;
		private String R49_PRODUCT;
		private BigDecimal R49_NV_LONG;
		private BigDecimal R49_NV_SHORT;
		private BigDecimal R49_FV_LONG;
		private BigDecimal R49_FV_SHORT;
		private BigDecimal R49_QFHA;
		private String R50_PRODUCT;
		private BigDecimal R50_NV_LONG;
		private BigDecimal R50_NV_SHORT;
		private BigDecimal R50_FV_LONG;
		private BigDecimal R50_FV_SHORT;
		private BigDecimal R50_QFHA;
		private String R51_PRODUCT;
		private BigDecimal R51_NV_LONG;
		private BigDecimal R51_NV_SHORT;
		private BigDecimal R51_FV_LONG;
		private BigDecimal R51_FV_SHORT;
		private BigDecimal R51_QFHA;
		private String R52_PRODUCT;
		private BigDecimal R52_NV_LONG;
		private BigDecimal R52_NV_SHORT;
		private BigDecimal R52_FV_LONG;
		private BigDecimal R52_FV_SHORT;
		private BigDecimal R52_QFHA;
		private String R53_PRODUCT;
		private BigDecimal R53_NV_LONG;
		private BigDecimal R53_NV_SHORT;
		private BigDecimal R53_FV_LONG;
		private BigDecimal R53_FV_SHORT;
		private BigDecimal R53_QFHA;
		private String R54_PRODUCT;
		private BigDecimal R54_NV_LONG;
		private BigDecimal R54_NV_SHORT;
		private BigDecimal R54_FV_LONG;
		private BigDecimal R54_FV_SHORT;
		private BigDecimal R54_QFHA;
		private String R55_PRODUCT;
		private BigDecimal R55_NV_LONG;
		private BigDecimal R55_NV_SHORT;
		private BigDecimal R55_FV_LONG;
		private BigDecimal R55_FV_SHORT;
		private BigDecimal R55_QFHA;

		private Date reportDate;
		private BigDecimal reportVersion;
		public String REPORT_FREQUENCY;
		public String REPORT_CODE;
		public String REPORT_DESC;
		public String ENTITY_FLG;
		public String MODIFY_FLG;
		public String DEL_FLG;
		private Date reportResubDate;

		public String getR11_PRODUCT() { return R11_PRODUCT; }
		public void setR11_PRODUCT(String r11_PRODUCT) { R11_PRODUCT = r11_PRODUCT; }
		public BigDecimal getR11_NV_LONG() { return R11_NV_LONG; }
		public void setR11_NV_LONG(BigDecimal r11_NV_LONG) { R11_NV_LONG = r11_NV_LONG; }
		public BigDecimal getR11_NV_SHORT() { return R11_NV_SHORT; }
		public void setR11_NV_SHORT(BigDecimal r11_NV_SHORT) { R11_NV_SHORT = r11_NV_SHORT; }
		public BigDecimal getR11_FV_LONG() { return R11_FV_LONG; }
		public void setR11_FV_LONG(BigDecimal r11_FV_LONG) { R11_FV_LONG = r11_FV_LONG; }
		public BigDecimal getR11_FV_SHORT() { return R11_FV_SHORT; }
		public void setR11_FV_SHORT(BigDecimal r11_FV_SHORT) { R11_FV_SHORT = r11_FV_SHORT; }
		public BigDecimal getR11_QFHA() { return R11_QFHA; }
		public void setR11_QFHA(BigDecimal r11_QFHA) { R11_QFHA = r11_QFHA; }
		public String getR12_PRODUCT() { return R12_PRODUCT; }
		public void setR12_PRODUCT(String r12_PRODUCT) { R12_PRODUCT = r12_PRODUCT; }
		public BigDecimal getR12_NV_LONG() { return R12_NV_LONG; }
		public void setR12_NV_LONG(BigDecimal r12_NV_LONG) { R12_NV_LONG = r12_NV_LONG; }
		public BigDecimal getR12_NV_SHORT() { return R12_NV_SHORT; }
		public void setR12_NV_SHORT(BigDecimal r12_NV_SHORT) { R12_NV_SHORT = r12_NV_SHORT; }
		public BigDecimal getR12_FV_LONG() { return R12_FV_LONG; }
		public void setR12_FV_LONG(BigDecimal r12_FV_LONG) { R12_FV_LONG = r12_FV_LONG; }
		public BigDecimal getR12_FV_SHORT() { return R12_FV_SHORT; }
		public void setR12_FV_SHORT(BigDecimal r12_FV_SHORT) { R12_FV_SHORT = r12_FV_SHORT; }
		public BigDecimal getR12_QFHA() { return R12_QFHA; }
		public void setR12_QFHA(BigDecimal r12_QFHA) { R12_QFHA = r12_QFHA; }
		public String getR13_PRODUCT() { return R13_PRODUCT; }
		public void setR13_PRODUCT(String r13_PRODUCT) { R13_PRODUCT = r13_PRODUCT; }
		public BigDecimal getR13_NV_LONG() { return R13_NV_LONG; }
		public void setR13_NV_LONG(BigDecimal r13_NV_LONG) { R13_NV_LONG = r13_NV_LONG; }
		public BigDecimal getR13_NV_SHORT() { return R13_NV_SHORT; }
		public void setR13_NV_SHORT(BigDecimal r13_NV_SHORT) { R13_NV_SHORT = r13_NV_SHORT; }
		public BigDecimal getR13_FV_LONG() { return R13_FV_LONG; }
		public void setR13_FV_LONG(BigDecimal r13_FV_LONG) { R13_FV_LONG = r13_FV_LONG; }
		public BigDecimal getR13_FV_SHORT() { return R13_FV_SHORT; }
		public void setR13_FV_SHORT(BigDecimal r13_FV_SHORT) { R13_FV_SHORT = r13_FV_SHORT; }
		public BigDecimal getR13_QFHA() { return R13_QFHA; }
		public void setR13_QFHA(BigDecimal r13_QFHA) { R13_QFHA = r13_QFHA; }
		public String getR14_PRODUCT() { return R14_PRODUCT; }
		public void setR14_PRODUCT(String r14_PRODUCT) { R14_PRODUCT = r14_PRODUCT; }
		public BigDecimal getR14_NV_LONG() { return R14_NV_LONG; }
		public void setR14_NV_LONG(BigDecimal r14_NV_LONG) { R14_NV_LONG = r14_NV_LONG; }
		public BigDecimal getR14_NV_SHORT() { return R14_NV_SHORT; }
		public void setR14_NV_SHORT(BigDecimal r14_NV_SHORT) { R14_NV_SHORT = r14_NV_SHORT; }
		public BigDecimal getR14_FV_LONG() { return R14_FV_LONG; }
		public void setR14_FV_LONG(BigDecimal r14_FV_LONG) { R14_FV_LONG = r14_FV_LONG; }
		public BigDecimal getR14_FV_SHORT() { return R14_FV_SHORT; }
		public void setR14_FV_SHORT(BigDecimal r14_FV_SHORT) { R14_FV_SHORT = r14_FV_SHORT; }
		public BigDecimal getR14_QFHA() { return R14_QFHA; }
		public void setR14_QFHA(BigDecimal r14_QFHA) { R14_QFHA = r14_QFHA; }
		public String getR15_PRODUCT() { return R15_PRODUCT; }
		public void setR15_PRODUCT(String r15_PRODUCT) { R15_PRODUCT = r15_PRODUCT; }
		public BigDecimal getR15_NV_LONG() { return R15_NV_LONG; }
		public void setR15_NV_LONG(BigDecimal r15_NV_LONG) { R15_NV_LONG = r15_NV_LONG; }
		public BigDecimal getR15_NV_SHORT() { return R15_NV_SHORT; }
		public void setR15_NV_SHORT(BigDecimal r15_NV_SHORT) { R15_NV_SHORT = r15_NV_SHORT; }
		public BigDecimal getR15_FV_LONG() { return R15_FV_LONG; }
		public void setR15_FV_LONG(BigDecimal r15_FV_LONG) { R15_FV_LONG = r15_FV_LONG; }
		public BigDecimal getR15_FV_SHORT() { return R15_FV_SHORT; }
		public void setR15_FV_SHORT(BigDecimal r15_FV_SHORT) { R15_FV_SHORT = r15_FV_SHORT; }
		public BigDecimal getR15_QFHA() { return R15_QFHA; }
		public void setR15_QFHA(BigDecimal r15_QFHA) { R15_QFHA = r15_QFHA; }
		public String getR16_PRODUCT() { return R16_PRODUCT; }
		public void setR16_PRODUCT(String r16_PRODUCT) { R16_PRODUCT = r16_PRODUCT; }
		public BigDecimal getR16_NV_LONG() { return R16_NV_LONG; }
		public void setR16_NV_LONG(BigDecimal r16_NV_LONG) { R16_NV_LONG = r16_NV_LONG; }
		public BigDecimal getR16_NV_SHORT() { return R16_NV_SHORT; }
		public void setR16_NV_SHORT(BigDecimal r16_NV_SHORT) { R16_NV_SHORT = r16_NV_SHORT; }
		public BigDecimal getR16_FV_LONG() { return R16_FV_LONG; }
		public void setR16_FV_LONG(BigDecimal r16_FV_LONG) { R16_FV_LONG = r16_FV_LONG; }
		public BigDecimal getR16_FV_SHORT() { return R16_FV_SHORT; }
		public void setR16_FV_SHORT(BigDecimal r16_FV_SHORT) { R16_FV_SHORT = r16_FV_SHORT; }
		public BigDecimal getR16_QFHA() { return R16_QFHA; }
		public void setR16_QFHA(BigDecimal r16_QFHA) { R16_QFHA = r16_QFHA; }
		public String getR17_PRODUCT() { return R17_PRODUCT; }
		public void setR17_PRODUCT(String r17_PRODUCT) { R17_PRODUCT = r17_PRODUCT; }
		public BigDecimal getR17_NV_LONG() { return R17_NV_LONG; }
		public void setR17_NV_LONG(BigDecimal r17_NV_LONG) { R17_NV_LONG = r17_NV_LONG; }
		public BigDecimal getR17_NV_SHORT() { return R17_NV_SHORT; }
		public void setR17_NV_SHORT(BigDecimal r17_NV_SHORT) { R17_NV_SHORT = r17_NV_SHORT; }
		public BigDecimal getR17_FV_LONG() { return R17_FV_LONG; }
		public void setR17_FV_LONG(BigDecimal r17_FV_LONG) { R17_FV_LONG = r17_FV_LONG; }
		public BigDecimal getR17_FV_SHORT() { return R17_FV_SHORT; }
		public void setR17_FV_SHORT(BigDecimal r17_FV_SHORT) { R17_FV_SHORT = r17_FV_SHORT; }
		public BigDecimal getR17_QFHA() { return R17_QFHA; }
		public void setR17_QFHA(BigDecimal r17_QFHA) { R17_QFHA = r17_QFHA; }
		public String getR18_PRODUCT() { return R18_PRODUCT; }
		public void setR18_PRODUCT(String r18_PRODUCT) { R18_PRODUCT = r18_PRODUCT; }
		public BigDecimal getR18_NV_LONG() { return R18_NV_LONG; }
		public void setR18_NV_LONG(BigDecimal r18_NV_LONG) { R18_NV_LONG = r18_NV_LONG; }
		public BigDecimal getR18_NV_SHORT() { return R18_NV_SHORT; }
		public void setR18_NV_SHORT(BigDecimal r18_NV_SHORT) { R18_NV_SHORT = r18_NV_SHORT; }
		public BigDecimal getR18_FV_LONG() { return R18_FV_LONG; }
		public void setR18_FV_LONG(BigDecimal r18_FV_LONG) { R18_FV_LONG = r18_FV_LONG; }
		public BigDecimal getR18_FV_SHORT() { return R18_FV_SHORT; }
		public void setR18_FV_SHORT(BigDecimal r18_FV_SHORT) { R18_FV_SHORT = r18_FV_SHORT; }
		public BigDecimal getR18_QFHA() { return R18_QFHA; }
		public void setR18_QFHA(BigDecimal r18_QFHA) { R18_QFHA = r18_QFHA; }
		public String getR19_PRODUCT() { return R19_PRODUCT; }
		public void setR19_PRODUCT(String r19_PRODUCT) { R19_PRODUCT = r19_PRODUCT; }
		public BigDecimal getR19_NV_LONG() { return R19_NV_LONG; }
		public void setR19_NV_LONG(BigDecimal r19_NV_LONG) { R19_NV_LONG = r19_NV_LONG; }
		public BigDecimal getR19_NV_SHORT() { return R19_NV_SHORT; }
		public void setR19_NV_SHORT(BigDecimal r19_NV_SHORT) { R19_NV_SHORT = r19_NV_SHORT; }
		public BigDecimal getR19_FV_LONG() { return R19_FV_LONG; }
		public void setR19_FV_LONG(BigDecimal r19_FV_LONG) { R19_FV_LONG = r19_FV_LONG; }
		public BigDecimal getR19_FV_SHORT() { return R19_FV_SHORT; }
		public void setR19_FV_SHORT(BigDecimal r19_FV_SHORT) { R19_FV_SHORT = r19_FV_SHORT; }
		public BigDecimal getR19_QFHA() { return R19_QFHA; }
		public void setR19_QFHA(BigDecimal r19_QFHA) { R19_QFHA = r19_QFHA; }
		public String getR20_PRODUCT() { return R20_PRODUCT; }
		public void setR20_PRODUCT(String r20_PRODUCT) { R20_PRODUCT = r20_PRODUCT; }
		public BigDecimal getR20_NV_LONG() { return R20_NV_LONG; }
		public void setR20_NV_LONG(BigDecimal r20_NV_LONG) { R20_NV_LONG = r20_NV_LONG; }
		public BigDecimal getR20_NV_SHORT() { return R20_NV_SHORT; }
		public void setR20_NV_SHORT(BigDecimal r20_NV_SHORT) { R20_NV_SHORT = r20_NV_SHORT; }
		public BigDecimal getR20_FV_LONG() { return R20_FV_LONG; }
		public void setR20_FV_LONG(BigDecimal r20_FV_LONG) { R20_FV_LONG = r20_FV_LONG; }
		public BigDecimal getR20_FV_SHORT() { return R20_FV_SHORT; }
		public void setR20_FV_SHORT(BigDecimal r20_FV_SHORT) { R20_FV_SHORT = r20_FV_SHORT; }
		public BigDecimal getR20_QFHA() { return R20_QFHA; }
		public void setR20_QFHA(BigDecimal r20_QFHA) { R20_QFHA = r20_QFHA; }
		public String getR21_PRODUCT() { return R21_PRODUCT; }
		public void setR21_PRODUCT(String r21_PRODUCT) { R21_PRODUCT = r21_PRODUCT; }
		public BigDecimal getR21_NV_LONG() { return R21_NV_LONG; }
		public void setR21_NV_LONG(BigDecimal r21_NV_LONG) { R21_NV_LONG = r21_NV_LONG; }
		public BigDecimal getR21_NV_SHORT() { return R21_NV_SHORT; }
		public void setR21_NV_SHORT(BigDecimal r21_NV_SHORT) { R21_NV_SHORT = r21_NV_SHORT; }
		public BigDecimal getR21_FV_LONG() { return R21_FV_LONG; }
		public void setR21_FV_LONG(BigDecimal r21_FV_LONG) { R21_FV_LONG = r21_FV_LONG; }
		public BigDecimal getR21_FV_SHORT() { return R21_FV_SHORT; }
		public void setR21_FV_SHORT(BigDecimal r21_FV_SHORT) { R21_FV_SHORT = r21_FV_SHORT; }
		public BigDecimal getR21_QFHA() { return R21_QFHA; }
		public void setR21_QFHA(BigDecimal r21_QFHA) { R21_QFHA = r21_QFHA; }
		public String getR22_PRODUCT() { return R22_PRODUCT; }
		public void setR22_PRODUCT(String r22_PRODUCT) { R22_PRODUCT = r22_PRODUCT; }
		public BigDecimal getR22_NV_LONG() { return R22_NV_LONG; }
		public void setR22_NV_LONG(BigDecimal r22_NV_LONG) { R22_NV_LONG = r22_NV_LONG; }
		public BigDecimal getR22_NV_SHORT() { return R22_NV_SHORT; }
		public void setR22_NV_SHORT(BigDecimal r22_NV_SHORT) { R22_NV_SHORT = r22_NV_SHORT; }
		public BigDecimal getR22_FV_LONG() { return R22_FV_LONG; }
		public void setR22_FV_LONG(BigDecimal r22_FV_LONG) { R22_FV_LONG = r22_FV_LONG; }
		public BigDecimal getR22_FV_SHORT() { return R22_FV_SHORT; }
		public void setR22_FV_SHORT(BigDecimal r22_FV_SHORT) { R22_FV_SHORT = r22_FV_SHORT; }
		public BigDecimal getR22_QFHA() { return R22_QFHA; }
		public void setR22_QFHA(BigDecimal r22_QFHA) { R22_QFHA = r22_QFHA; }
		public String getR23_PRODUCT() { return R23_PRODUCT; }
		public void setR23_PRODUCT(String r23_PRODUCT) { R23_PRODUCT = r23_PRODUCT; }
		public BigDecimal getR23_NV_LONG() { return R23_NV_LONG; }
		public void setR23_NV_LONG(BigDecimal r23_NV_LONG) { R23_NV_LONG = r23_NV_LONG; }
		public BigDecimal getR23_NV_SHORT() { return R23_NV_SHORT; }
		public void setR23_NV_SHORT(BigDecimal r23_NV_SHORT) { R23_NV_SHORT = r23_NV_SHORT; }
		public BigDecimal getR23_FV_LONG() { return R23_FV_LONG; }
		public void setR23_FV_LONG(BigDecimal r23_FV_LONG) { R23_FV_LONG = r23_FV_LONG; }
		public BigDecimal getR23_FV_SHORT() { return R23_FV_SHORT; }
		public void setR23_FV_SHORT(BigDecimal r23_FV_SHORT) { R23_FV_SHORT = r23_FV_SHORT; }
		public BigDecimal getR23_QFHA() { return R23_QFHA; }
		public void setR23_QFHA(BigDecimal r23_QFHA) { R23_QFHA = r23_QFHA; }
		public String getR24_PRODUCT() { return R24_PRODUCT; }
		public void setR24_PRODUCT(String r24_PRODUCT) { R24_PRODUCT = r24_PRODUCT; }
		public BigDecimal getR24_NV_LONG() { return R24_NV_LONG; }
		public void setR24_NV_LONG(BigDecimal r24_NV_LONG) { R24_NV_LONG = r24_NV_LONG; }
		public BigDecimal getR24_NV_SHORT() { return R24_NV_SHORT; }
		public void setR24_NV_SHORT(BigDecimal r24_NV_SHORT) { R24_NV_SHORT = r24_NV_SHORT; }
		public BigDecimal getR24_FV_LONG() { return R24_FV_LONG; }
		public void setR24_FV_LONG(BigDecimal r24_FV_LONG) { R24_FV_LONG = r24_FV_LONG; }
		public BigDecimal getR24_FV_SHORT() { return R24_FV_SHORT; }
		public void setR24_FV_SHORT(BigDecimal r24_FV_SHORT) { R24_FV_SHORT = r24_FV_SHORT; }
		public BigDecimal getR24_QFHA() { return R24_QFHA; }
		public void setR24_QFHA(BigDecimal r24_QFHA) { R24_QFHA = r24_QFHA; }
		public String getR25_PRODUCT() { return R25_PRODUCT; }
		public void setR25_PRODUCT(String r25_PRODUCT) { R25_PRODUCT = r25_PRODUCT; }
		public BigDecimal getR25_NV_LONG() { return R25_NV_LONG; }
		public void setR25_NV_LONG(BigDecimal r25_NV_LONG) { R25_NV_LONG = r25_NV_LONG; }
		public BigDecimal getR25_NV_SHORT() { return R25_NV_SHORT; }
		public void setR25_NV_SHORT(BigDecimal r25_NV_SHORT) { R25_NV_SHORT = r25_NV_SHORT; }
		public BigDecimal getR25_FV_LONG() { return R25_FV_LONG; }
		public void setR25_FV_LONG(BigDecimal r25_FV_LONG) { R25_FV_LONG = r25_FV_LONG; }
		public BigDecimal getR25_FV_SHORT() { return R25_FV_SHORT; }
		public void setR25_FV_SHORT(BigDecimal r25_FV_SHORT) { R25_FV_SHORT = r25_FV_SHORT; }
		public BigDecimal getR25_QFHA() { return R25_QFHA; }
		public void setR25_QFHA(BigDecimal r25_QFHA) { R25_QFHA = r25_QFHA; }
		public String getR26_PRODUCT() { return R26_PRODUCT; }
		public void setR26_PRODUCT(String r26_PRODUCT) { R26_PRODUCT = r26_PRODUCT; }
		public BigDecimal getR26_NV_LONG() { return R26_NV_LONG; }
		public void setR26_NV_LONG(BigDecimal r26_NV_LONG) { R26_NV_LONG = r26_NV_LONG; }
		public BigDecimal getR26_NV_SHORT() { return R26_NV_SHORT; }
		public void setR26_NV_SHORT(BigDecimal r26_NV_SHORT) { R26_NV_SHORT = r26_NV_SHORT; }
		public BigDecimal getR26_FV_LONG() { return R26_FV_LONG; }
		public void setR26_FV_LONG(BigDecimal r26_FV_LONG) { R26_FV_LONG = r26_FV_LONG; }
		public BigDecimal getR26_FV_SHORT() { return R26_FV_SHORT; }
		public void setR26_FV_SHORT(BigDecimal r26_FV_SHORT) { R26_FV_SHORT = r26_FV_SHORT; }
		public BigDecimal getR26_QFHA() { return R26_QFHA; }
		public void setR26_QFHA(BigDecimal r26_QFHA) { R26_QFHA = r26_QFHA; }
		public String getR27_PRODUCT() { return R27_PRODUCT; }
		public void setR27_PRODUCT(String r27_PRODUCT) { R27_PRODUCT = r27_PRODUCT; }
		public BigDecimal getR27_NV_LONG() { return R27_NV_LONG; }
		public void setR27_NV_LONG(BigDecimal r27_NV_LONG) { R27_NV_LONG = r27_NV_LONG; }
		public BigDecimal getR27_NV_SHORT() { return R27_NV_SHORT; }
		public void setR27_NV_SHORT(BigDecimal r27_NV_SHORT) { R27_NV_SHORT = r27_NV_SHORT; }
		public BigDecimal getR27_FV_LONG() { return R27_FV_LONG; }
		public void setR27_FV_LONG(BigDecimal r27_FV_LONG) { R27_FV_LONG = r27_FV_LONG; }
		public BigDecimal getR27_FV_SHORT() { return R27_FV_SHORT; }
		public void setR27_FV_SHORT(BigDecimal r27_FV_SHORT) { R27_FV_SHORT = r27_FV_SHORT; }
		public BigDecimal getR27_QFHA() { return R27_QFHA; }
		public void setR27_QFHA(BigDecimal r27_QFHA) { R27_QFHA = r27_QFHA; }
		public String getR28_PRODUCT() { return R28_PRODUCT; }
		public void setR28_PRODUCT(String r28_PRODUCT) { R28_PRODUCT = r28_PRODUCT; }
		public BigDecimal getR28_NV_LONG() { return R28_NV_LONG; }
		public void setR28_NV_LONG(BigDecimal r28_NV_LONG) { R28_NV_LONG = r28_NV_LONG; }
		public BigDecimal getR28_NV_SHORT() { return R28_NV_SHORT; }
		public void setR28_NV_SHORT(BigDecimal r28_NV_SHORT) { R28_NV_SHORT = r28_NV_SHORT; }
		public BigDecimal getR28_FV_LONG() { return R28_FV_LONG; }
		public void setR28_FV_LONG(BigDecimal r28_FV_LONG) { R28_FV_LONG = r28_FV_LONG; }
		public BigDecimal getR28_FV_SHORT() { return R28_FV_SHORT; }
		public void setR28_FV_SHORT(BigDecimal r28_FV_SHORT) { R28_FV_SHORT = r28_FV_SHORT; }
		public BigDecimal getR28_QFHA() { return R28_QFHA; }
		public void setR28_QFHA(BigDecimal r28_QFHA) { R28_QFHA = r28_QFHA; }
		public String getR29_PRODUCT() { return R29_PRODUCT; }
		public void setR29_PRODUCT(String r29_PRODUCT) { R29_PRODUCT = r29_PRODUCT; }
		public BigDecimal getR29_NV_LONG() { return R29_NV_LONG; }
		public void setR29_NV_LONG(BigDecimal r29_NV_LONG) { R29_NV_LONG = r29_NV_LONG; }
		public BigDecimal getR29_NV_SHORT() { return R29_NV_SHORT; }
		public void setR29_NV_SHORT(BigDecimal r29_NV_SHORT) { R29_NV_SHORT = r29_NV_SHORT; }
		public BigDecimal getR29_FV_LONG() { return R29_FV_LONG; }
		public void setR29_FV_LONG(BigDecimal r29_FV_LONG) { R29_FV_LONG = r29_FV_LONG; }
		public BigDecimal getR29_FV_SHORT() { return R29_FV_SHORT; }
		public void setR29_FV_SHORT(BigDecimal r29_FV_SHORT) { R29_FV_SHORT = r29_FV_SHORT; }
		public BigDecimal getR29_QFHA() { return R29_QFHA; }
		public void setR29_QFHA(BigDecimal r29_QFHA) { R29_QFHA = r29_QFHA; }
		public String getR30_PRODUCT() { return R30_PRODUCT; }
		public void setR30_PRODUCT(String r30_PRODUCT) { R30_PRODUCT = r30_PRODUCT; }
		public BigDecimal getR30_NV_LONG() { return R30_NV_LONG; }
		public void setR30_NV_LONG(BigDecimal r30_NV_LONG) { R30_NV_LONG = r30_NV_LONG; }
		public BigDecimal getR30_NV_SHORT() { return R30_NV_SHORT; }
		public void setR30_NV_SHORT(BigDecimal r30_NV_SHORT) { R30_NV_SHORT = r30_NV_SHORT; }
		public BigDecimal getR30_FV_LONG() { return R30_FV_LONG; }
		public void setR30_FV_LONG(BigDecimal r30_FV_LONG) { R30_FV_LONG = r30_FV_LONG; }
		public BigDecimal getR30_FV_SHORT() { return R30_FV_SHORT; }
		public void setR30_FV_SHORT(BigDecimal r30_FV_SHORT) { R30_FV_SHORT = r30_FV_SHORT; }
		public BigDecimal getR30_QFHA() { return R30_QFHA; }
		public void setR30_QFHA(BigDecimal r30_QFHA) { R30_QFHA = r30_QFHA; }
		public String getR31_PRODUCT() { return R31_PRODUCT; }
		public void setR31_PRODUCT(String r31_PRODUCT) { R31_PRODUCT = r31_PRODUCT; }
		public BigDecimal getR31_NV_LONG() { return R31_NV_LONG; }
		public void setR31_NV_LONG(BigDecimal r31_NV_LONG) { R31_NV_LONG = r31_NV_LONG; }
		public BigDecimal getR31_NV_SHORT() { return R31_NV_SHORT; }
		public void setR31_NV_SHORT(BigDecimal r31_NV_SHORT) { R31_NV_SHORT = r31_NV_SHORT; }
		public BigDecimal getR31_FV_LONG() { return R31_FV_LONG; }
		public void setR31_FV_LONG(BigDecimal r31_FV_LONG) { R31_FV_LONG = r31_FV_LONG; }
		public BigDecimal getR31_FV_SHORT() { return R31_FV_SHORT; }
		public void setR31_FV_SHORT(BigDecimal r31_FV_SHORT) { R31_FV_SHORT = r31_FV_SHORT; }
		public BigDecimal getR31_QFHA() { return R31_QFHA; }
		public void setR31_QFHA(BigDecimal r31_QFHA) { R31_QFHA = r31_QFHA; }
		public String getR32_PRODUCT() { return R32_PRODUCT; }
		public void setR32_PRODUCT(String r32_PRODUCT) { R32_PRODUCT = r32_PRODUCT; }
		public BigDecimal getR32_NV_LONG() { return R32_NV_LONG; }
		public void setR32_NV_LONG(BigDecimal r32_NV_LONG) { R32_NV_LONG = r32_NV_LONG; }
		public BigDecimal getR32_NV_SHORT() { return R32_NV_SHORT; }
		public void setR32_NV_SHORT(BigDecimal r32_NV_SHORT) { R32_NV_SHORT = r32_NV_SHORT; }
		public BigDecimal getR32_FV_LONG() { return R32_FV_LONG; }
		public void setR32_FV_LONG(BigDecimal r32_FV_LONG) { R32_FV_LONG = r32_FV_LONG; }
		public BigDecimal getR32_FV_SHORT() { return R32_FV_SHORT; }
		public void setR32_FV_SHORT(BigDecimal r32_FV_SHORT) { R32_FV_SHORT = r32_FV_SHORT; }
		public BigDecimal getR32_QFHA() { return R32_QFHA; }
		public void setR32_QFHA(BigDecimal r32_QFHA) { R32_QFHA = r32_QFHA; }
		public String getR33_PRODUCT() { return R33_PRODUCT; }
		public void setR33_PRODUCT(String r33_PRODUCT) { R33_PRODUCT = r33_PRODUCT; }
		public BigDecimal getR33_NV_LONG() { return R33_NV_LONG; }
		public void setR33_NV_LONG(BigDecimal r33_NV_LONG) { R33_NV_LONG = r33_NV_LONG; }
		public BigDecimal getR33_NV_SHORT() { return R33_NV_SHORT; }
		public void setR33_NV_SHORT(BigDecimal r33_NV_SHORT) { R33_NV_SHORT = r33_NV_SHORT; }
		public BigDecimal getR33_FV_LONG() { return R33_FV_LONG; }
		public void setR33_FV_LONG(BigDecimal r33_FV_LONG) { R33_FV_LONG = r33_FV_LONG; }
		public BigDecimal getR33_FV_SHORT() { return R33_FV_SHORT; }
		public void setR33_FV_SHORT(BigDecimal r33_FV_SHORT) { R33_FV_SHORT = r33_FV_SHORT; }
		public BigDecimal getR33_QFHA() { return R33_QFHA; }
		public void setR33_QFHA(BigDecimal r33_QFHA) { R33_QFHA = r33_QFHA; }
		public String getR34_PRODUCT() { return R34_PRODUCT; }
		public void setR34_PRODUCT(String r34_PRODUCT) { R34_PRODUCT = r34_PRODUCT; }
		public BigDecimal getR34_NV_LONG() { return R34_NV_LONG; }
		public void setR34_NV_LONG(BigDecimal r34_NV_LONG) { R34_NV_LONG = r34_NV_LONG; }
		public BigDecimal getR34_NV_SHORT() { return R34_NV_SHORT; }
		public void setR34_NV_SHORT(BigDecimal r34_NV_SHORT) { R34_NV_SHORT = r34_NV_SHORT; }
		public BigDecimal getR34_FV_LONG() { return R34_FV_LONG; }
		public void setR34_FV_LONG(BigDecimal r34_FV_LONG) { R34_FV_LONG = r34_FV_LONG; }
		public BigDecimal getR34_FV_SHORT() { return R34_FV_SHORT; }
		public void setR34_FV_SHORT(BigDecimal r34_FV_SHORT) { R34_FV_SHORT = r34_FV_SHORT; }
		public BigDecimal getR34_QFHA() { return R34_QFHA; }
		public void setR34_QFHA(BigDecimal r34_QFHA) { R34_QFHA = r34_QFHA; }
		public String getR35_PRODUCT() { return R35_PRODUCT; }
		public void setR35_PRODUCT(String r35_PRODUCT) { R35_PRODUCT = r35_PRODUCT; }
		public BigDecimal getR35_NV_LONG() { return R35_NV_LONG; }
		public void setR35_NV_LONG(BigDecimal r35_NV_LONG) { R35_NV_LONG = r35_NV_LONG; }
		public BigDecimal getR35_NV_SHORT() { return R35_NV_SHORT; }
		public void setR35_NV_SHORT(BigDecimal r35_NV_SHORT) { R35_NV_SHORT = r35_NV_SHORT; }
		public BigDecimal getR35_FV_LONG() { return R35_FV_LONG; }
		public void setR35_FV_LONG(BigDecimal r35_FV_LONG) { R35_FV_LONG = r35_FV_LONG; }
		public BigDecimal getR35_FV_SHORT() { return R35_FV_SHORT; }
		public void setR35_FV_SHORT(BigDecimal r35_FV_SHORT) { R35_FV_SHORT = r35_FV_SHORT; }
		public BigDecimal getR35_QFHA() { return R35_QFHA; }
		public void setR35_QFHA(BigDecimal r35_QFHA) { R35_QFHA = r35_QFHA; }
		public String getR36_PRODUCT() { return R36_PRODUCT; }
		public void setR36_PRODUCT(String r36_PRODUCT) { R36_PRODUCT = r36_PRODUCT; }
		public BigDecimal getR36_NV_LONG() { return R36_NV_LONG; }
		public void setR36_NV_LONG(BigDecimal r36_NV_LONG) { R36_NV_LONG = r36_NV_LONG; }
		public BigDecimal getR36_NV_SHORT() { return R36_NV_SHORT; }
		public void setR36_NV_SHORT(BigDecimal r36_NV_SHORT) { R36_NV_SHORT = r36_NV_SHORT; }
		public BigDecimal getR36_FV_LONG() { return R36_FV_LONG; }
		public void setR36_FV_LONG(BigDecimal r36_FV_LONG) { R36_FV_LONG = r36_FV_LONG; }
		public BigDecimal getR36_FV_SHORT() { return R36_FV_SHORT; }
		public void setR36_FV_SHORT(BigDecimal r36_FV_SHORT) { R36_FV_SHORT = r36_FV_SHORT; }
		public BigDecimal getR36_QFHA() { return R36_QFHA; }
		public void setR36_QFHA(BigDecimal r36_QFHA) { R36_QFHA = r36_QFHA; }
		public String getR37_PRODUCT() { return R37_PRODUCT; }
		public void setR37_PRODUCT(String r37_PRODUCT) { R37_PRODUCT = r37_PRODUCT; }
		public BigDecimal getR37_NV_LONG() { return R37_NV_LONG; }
		public void setR37_NV_LONG(BigDecimal r37_NV_LONG) { R37_NV_LONG = r37_NV_LONG; }
		public BigDecimal getR37_NV_SHORT() { return R37_NV_SHORT; }
		public void setR37_NV_SHORT(BigDecimal r37_NV_SHORT) { R37_NV_SHORT = r37_NV_SHORT; }
		public BigDecimal getR37_FV_LONG() { return R37_FV_LONG; }
		public void setR37_FV_LONG(BigDecimal r37_FV_LONG) { R37_FV_LONG = r37_FV_LONG; }
		public BigDecimal getR37_FV_SHORT() { return R37_FV_SHORT; }
		public void setR37_FV_SHORT(BigDecimal r37_FV_SHORT) { R37_FV_SHORT = r37_FV_SHORT; }
		public BigDecimal getR37_QFHA() { return R37_QFHA; }
		public void setR37_QFHA(BigDecimal r37_QFHA) { R37_QFHA = r37_QFHA; }
		public String getR38_PRODUCT() { return R38_PRODUCT; }
		public void setR38_PRODUCT(String r38_PRODUCT) { R38_PRODUCT = r38_PRODUCT; }
		public BigDecimal getR38_NV_LONG() { return R38_NV_LONG; }
		public void setR38_NV_LONG(BigDecimal r38_NV_LONG) { R38_NV_LONG = r38_NV_LONG; }
		public BigDecimal getR38_NV_SHORT() { return R38_NV_SHORT; }
		public void setR38_NV_SHORT(BigDecimal r38_NV_SHORT) { R38_NV_SHORT = r38_NV_SHORT; }
		public BigDecimal getR38_FV_LONG() { return R38_FV_LONG; }
		public void setR38_FV_LONG(BigDecimal r38_FV_LONG) { R38_FV_LONG = r38_FV_LONG; }
		public BigDecimal getR38_FV_SHORT() { return R38_FV_SHORT; }
		public void setR38_FV_SHORT(BigDecimal r38_FV_SHORT) { R38_FV_SHORT = r38_FV_SHORT; }
		public BigDecimal getR38_QFHA() { return R38_QFHA; }
		public void setR38_QFHA(BigDecimal r38_QFHA) { R38_QFHA = r38_QFHA; }
		public String getR39_PRODUCT() { return R39_PRODUCT; }
		public void setR39_PRODUCT(String r39_PRODUCT) { R39_PRODUCT = r39_PRODUCT; }
		public BigDecimal getR39_NV_LONG() { return R39_NV_LONG; }
		public void setR39_NV_LONG(BigDecimal r39_NV_LONG) { R39_NV_LONG = r39_NV_LONG; }
		public BigDecimal getR39_NV_SHORT() { return R39_NV_SHORT; }
		public void setR39_NV_SHORT(BigDecimal r39_NV_SHORT) { R39_NV_SHORT = r39_NV_SHORT; }
		public BigDecimal getR39_FV_LONG() { return R39_FV_LONG; }
		public void setR39_FV_LONG(BigDecimal r39_FV_LONG) { R39_FV_LONG = r39_FV_LONG; }
		public BigDecimal getR39_FV_SHORT() { return R39_FV_SHORT; }
		public void setR39_FV_SHORT(BigDecimal r39_FV_SHORT) { R39_FV_SHORT = r39_FV_SHORT; }
		public BigDecimal getR39_QFHA() { return R39_QFHA; }
		public void setR39_QFHA(BigDecimal r39_QFHA) { R39_QFHA = r39_QFHA; }
		public String getR40_PRODUCT() { return R40_PRODUCT; }
		public void setR40_PRODUCT(String r40_PRODUCT) { R40_PRODUCT = r40_PRODUCT; }
		public BigDecimal getR40_NV_LONG() { return R40_NV_LONG; }
		public void setR40_NV_LONG(BigDecimal r40_NV_LONG) { R40_NV_LONG = r40_NV_LONG; }
		public BigDecimal getR40_NV_SHORT() { return R40_NV_SHORT; }
		public void setR40_NV_SHORT(BigDecimal r40_NV_SHORT) { R40_NV_SHORT = r40_NV_SHORT; }
		public BigDecimal getR40_FV_LONG() { return R40_FV_LONG; }
		public void setR40_FV_LONG(BigDecimal r40_FV_LONG) { R40_FV_LONG = r40_FV_LONG; }
		public BigDecimal getR40_FV_SHORT() { return R40_FV_SHORT; }
		public void setR40_FV_SHORT(BigDecimal r40_FV_SHORT) { R40_FV_SHORT = r40_FV_SHORT; }
		public BigDecimal getR40_QFHA() { return R40_QFHA; }
		public void setR40_QFHA(BigDecimal r40_QFHA) { R40_QFHA = r40_QFHA; }
		public String getR41_PRODUCT() { return R41_PRODUCT; }
		public void setR41_PRODUCT(String r41_PRODUCT) { R41_PRODUCT = r41_PRODUCT; }
		public BigDecimal getR41_NV_LONG() { return R41_NV_LONG; }
		public void setR41_NV_LONG(BigDecimal r41_NV_LONG) { R41_NV_LONG = r41_NV_LONG; }
		public BigDecimal getR41_NV_SHORT() { return R41_NV_SHORT; }
		public void setR41_NV_SHORT(BigDecimal r41_NV_SHORT) { R41_NV_SHORT = r41_NV_SHORT; }
		public BigDecimal getR41_FV_LONG() { return R41_FV_LONG; }
		public void setR41_FV_LONG(BigDecimal r41_FV_LONG) { R41_FV_LONG = r41_FV_LONG; }
		public BigDecimal getR41_FV_SHORT() { return R41_FV_SHORT; }
		public void setR41_FV_SHORT(BigDecimal r41_FV_SHORT) { R41_FV_SHORT = r41_FV_SHORT; }
		public BigDecimal getR41_QFHA() { return R41_QFHA; }
		public void setR41_QFHA(BigDecimal r41_QFHA) { R41_QFHA = r41_QFHA; }
		public String getR42_PRODUCT() { return R42_PRODUCT; }
		public void setR42_PRODUCT(String r42_PRODUCT) { R42_PRODUCT = r42_PRODUCT; }
		public BigDecimal getR42_NV_LONG() { return R42_NV_LONG; }
		public void setR42_NV_LONG(BigDecimal r42_NV_LONG) { R42_NV_LONG = r42_NV_LONG; }
		public BigDecimal getR42_NV_SHORT() { return R42_NV_SHORT; }
		public void setR42_NV_SHORT(BigDecimal r42_NV_SHORT) { R42_NV_SHORT = r42_NV_SHORT; }
		public BigDecimal getR42_FV_LONG() { return R42_FV_LONG; }
		public void setR42_FV_LONG(BigDecimal r42_FV_LONG) { R42_FV_LONG = r42_FV_LONG; }
		public BigDecimal getR42_FV_SHORT() { return R42_FV_SHORT; }
		public void setR42_FV_SHORT(BigDecimal r42_FV_SHORT) { R42_FV_SHORT = r42_FV_SHORT; }
		public BigDecimal getR42_QFHA() { return R42_QFHA; }
		public void setR42_QFHA(BigDecimal r42_QFHA) { R42_QFHA = r42_QFHA; }
		public String getR43_PRODUCT() { return R43_PRODUCT; }
		public void setR43_PRODUCT(String r43_PRODUCT) { R43_PRODUCT = r43_PRODUCT; }
		public BigDecimal getR43_NV_LONG() { return R43_NV_LONG; }
		public void setR43_NV_LONG(BigDecimal r43_NV_LONG) { R43_NV_LONG = r43_NV_LONG; }
		public BigDecimal getR43_NV_SHORT() { return R43_NV_SHORT; }
		public void setR43_NV_SHORT(BigDecimal r43_NV_SHORT) { R43_NV_SHORT = r43_NV_SHORT; }
		public BigDecimal getR43_FV_LONG() { return R43_FV_LONG; }
		public void setR43_FV_LONG(BigDecimal r43_FV_LONG) { R43_FV_LONG = r43_FV_LONG; }
		public BigDecimal getR43_FV_SHORT() { return R43_FV_SHORT; }
		public void setR43_FV_SHORT(BigDecimal r43_FV_SHORT) { R43_FV_SHORT = r43_FV_SHORT; }
		public BigDecimal getR43_QFHA() { return R43_QFHA; }
		public void setR43_QFHA(BigDecimal r43_QFHA) { R43_QFHA = r43_QFHA; }
		public String getR44_PRODUCT() { return R44_PRODUCT; }
		public void setR44_PRODUCT(String r44_PRODUCT) { R44_PRODUCT = r44_PRODUCT; }
		public BigDecimal getR44_NV_LONG() { return R44_NV_LONG; }
		public void setR44_NV_LONG(BigDecimal r44_NV_LONG) { R44_NV_LONG = r44_NV_LONG; }
		public BigDecimal getR44_NV_SHORT() { return R44_NV_SHORT; }
		public void setR44_NV_SHORT(BigDecimal r44_NV_SHORT) { R44_NV_SHORT = r44_NV_SHORT; }
		public BigDecimal getR44_FV_LONG() { return R44_FV_LONG; }
		public void setR44_FV_LONG(BigDecimal r44_FV_LONG) { R44_FV_LONG = r44_FV_LONG; }
		public BigDecimal getR44_FV_SHORT() { return R44_FV_SHORT; }
		public void setR44_FV_SHORT(BigDecimal r44_FV_SHORT) { R44_FV_SHORT = r44_FV_SHORT; }
		public BigDecimal getR44_QFHA() { return R44_QFHA; }
		public void setR44_QFHA(BigDecimal r44_QFHA) { R44_QFHA = r44_QFHA; }
		public String getR45_PRODUCT() { return R45_PRODUCT; }
		public void setR45_PRODUCT(String r45_PRODUCT) { R45_PRODUCT = r45_PRODUCT; }
		public BigDecimal getR45_NV_LONG() { return R45_NV_LONG; }
		public void setR45_NV_LONG(BigDecimal r45_NV_LONG) { R45_NV_LONG = r45_NV_LONG; }
		public BigDecimal getR45_NV_SHORT() { return R45_NV_SHORT; }
		public void setR45_NV_SHORT(BigDecimal r45_NV_SHORT) { R45_NV_SHORT = r45_NV_SHORT; }
		public BigDecimal getR45_FV_LONG() { return R45_FV_LONG; }
		public void setR45_FV_LONG(BigDecimal r45_FV_LONG) { R45_FV_LONG = r45_FV_LONG; }
		public BigDecimal getR45_FV_SHORT() { return R45_FV_SHORT; }
		public void setR45_FV_SHORT(BigDecimal r45_FV_SHORT) { R45_FV_SHORT = r45_FV_SHORT; }
		public BigDecimal getR45_QFHA() { return R45_QFHA; }
		public void setR45_QFHA(BigDecimal r45_QFHA) { R45_QFHA = r45_QFHA; }
		public String getR46_PRODUCT() { return R46_PRODUCT; }
		public void setR46_PRODUCT(String r46_PRODUCT) { R46_PRODUCT = r46_PRODUCT; }
		public BigDecimal getR46_NV_LONG() { return R46_NV_LONG; }
		public void setR46_NV_LONG(BigDecimal r46_NV_LONG) { R46_NV_LONG = r46_NV_LONG; }
		public BigDecimal getR46_NV_SHORT() { return R46_NV_SHORT; }
		public void setR46_NV_SHORT(BigDecimal r46_NV_SHORT) { R46_NV_SHORT = r46_NV_SHORT; }
		public BigDecimal getR46_FV_LONG() { return R46_FV_LONG; }
		public void setR46_FV_LONG(BigDecimal r46_FV_LONG) { R46_FV_LONG = r46_FV_LONG; }
		public BigDecimal getR46_FV_SHORT() { return R46_FV_SHORT; }
		public void setR46_FV_SHORT(BigDecimal r46_FV_SHORT) { R46_FV_SHORT = r46_FV_SHORT; }
		public BigDecimal getR46_QFHA() { return R46_QFHA; }
		public void setR46_QFHA(BigDecimal r46_QFHA) { R46_QFHA = r46_QFHA; }
		public String getR47_PRODUCT() { return R47_PRODUCT; }
		public void setR47_PRODUCT(String r47_PRODUCT) { R47_PRODUCT = r47_PRODUCT; }
		public BigDecimal getR47_NV_LONG() { return R47_NV_LONG; }
		public void setR47_NV_LONG(BigDecimal r47_NV_LONG) { R47_NV_LONG = r47_NV_LONG; }
		public BigDecimal getR47_NV_SHORT() { return R47_NV_SHORT; }
		public void setR47_NV_SHORT(BigDecimal r47_NV_SHORT) { R47_NV_SHORT = r47_NV_SHORT; }
		public BigDecimal getR47_FV_LONG() { return R47_FV_LONG; }
		public void setR47_FV_LONG(BigDecimal r47_FV_LONG) { R47_FV_LONG = r47_FV_LONG; }
		public BigDecimal getR47_FV_SHORT() { return R47_FV_SHORT; }
		public void setR47_FV_SHORT(BigDecimal r47_FV_SHORT) { R47_FV_SHORT = r47_FV_SHORT; }
		public BigDecimal getR47_QFHA() { return R47_QFHA; }
		public void setR47_QFHA(BigDecimal r47_QFHA) { R47_QFHA = r47_QFHA; }
		public String getR48_PRODUCT() { return R48_PRODUCT; }
		public void setR48_PRODUCT(String r48_PRODUCT) { R48_PRODUCT = r48_PRODUCT; }
		public BigDecimal getR48_NV_LONG() { return R48_NV_LONG; }
		public void setR48_NV_LONG(BigDecimal r48_NV_LONG) { R48_NV_LONG = r48_NV_LONG; }
		public BigDecimal getR48_NV_SHORT() { return R48_NV_SHORT; }
		public void setR48_NV_SHORT(BigDecimal r48_NV_SHORT) { R48_NV_SHORT = r48_NV_SHORT; }
		public BigDecimal getR48_FV_LONG() { return R48_FV_LONG; }
		public void setR48_FV_LONG(BigDecimal r48_FV_LONG) { R48_FV_LONG = r48_FV_LONG; }
		public BigDecimal getR48_FV_SHORT() { return R48_FV_SHORT; }
		public void setR48_FV_SHORT(BigDecimal r48_FV_SHORT) { R48_FV_SHORT = r48_FV_SHORT; }
		public BigDecimal getR48_QFHA() { return R48_QFHA; }
		public void setR48_QFHA(BigDecimal r48_QFHA) { R48_QFHA = r48_QFHA; }
		public String getR49_PRODUCT() { return R49_PRODUCT; }
		public void setR49_PRODUCT(String r49_PRODUCT) { R49_PRODUCT = r49_PRODUCT; }
		public BigDecimal getR49_NV_LONG() { return R49_NV_LONG; }
		public void setR49_NV_LONG(BigDecimal r49_NV_LONG) { R49_NV_LONG = r49_NV_LONG; }
		public BigDecimal getR49_NV_SHORT() { return R49_NV_SHORT; }
		public void setR49_NV_SHORT(BigDecimal r49_NV_SHORT) { R49_NV_SHORT = r49_NV_SHORT; }
		public BigDecimal getR49_FV_LONG() { return R49_FV_LONG; }
		public void setR49_FV_LONG(BigDecimal r49_FV_LONG) { R49_FV_LONG = r49_FV_LONG; }
		public BigDecimal getR49_FV_SHORT() { return R49_FV_SHORT; }
		public void setR49_FV_SHORT(BigDecimal r49_FV_SHORT) { R49_FV_SHORT = r49_FV_SHORT; }
		public BigDecimal getR49_QFHA() { return R49_QFHA; }
		public void setR49_QFHA(BigDecimal r49_QFHA) { R49_QFHA = r49_QFHA; }
		public String getR50_PRODUCT() { return R50_PRODUCT; }
		public void setR50_PRODUCT(String r50_PRODUCT) { R50_PRODUCT = r50_PRODUCT; }
		public BigDecimal getR50_NV_LONG() { return R50_NV_LONG; }
		public void setR50_NV_LONG(BigDecimal r50_NV_LONG) { R50_NV_LONG = r50_NV_LONG; }
		public BigDecimal getR50_NV_SHORT() { return R50_NV_SHORT; }
		public void setR50_NV_SHORT(BigDecimal r50_NV_SHORT) { R50_NV_SHORT = r50_NV_SHORT; }
		public BigDecimal getR50_FV_LONG() { return R50_FV_LONG; }
		public void setR50_FV_LONG(BigDecimal r50_FV_LONG) { R50_FV_LONG = r50_FV_LONG; }
		public BigDecimal getR50_FV_SHORT() { return R50_FV_SHORT; }
		public void setR50_FV_SHORT(BigDecimal r50_FV_SHORT) { R50_FV_SHORT = r50_FV_SHORT; }
		public BigDecimal getR50_QFHA() { return R50_QFHA; }
		public void setR50_QFHA(BigDecimal r50_QFHA) { R50_QFHA = r50_QFHA; }
		public String getR51_PRODUCT() { return R51_PRODUCT; }
		public void setR51_PRODUCT(String r51_PRODUCT) { R51_PRODUCT = r51_PRODUCT; }
		public BigDecimal getR51_NV_LONG() { return R51_NV_LONG; }
		public void setR51_NV_LONG(BigDecimal r51_NV_LONG) { R51_NV_LONG = r51_NV_LONG; }
		public BigDecimal getR51_NV_SHORT() { return R51_NV_SHORT; }
		public void setR51_NV_SHORT(BigDecimal r51_NV_SHORT) { R51_NV_SHORT = r51_NV_SHORT; }
		public BigDecimal getR51_FV_LONG() { return R51_FV_LONG; }
		public void setR51_FV_LONG(BigDecimal r51_FV_LONG) { R51_FV_LONG = r51_FV_LONG; }
		public BigDecimal getR51_FV_SHORT() { return R51_FV_SHORT; }
		public void setR51_FV_SHORT(BigDecimal r51_FV_SHORT) { R51_FV_SHORT = r51_FV_SHORT; }
		public BigDecimal getR51_QFHA() { return R51_QFHA; }
		public void setR51_QFHA(BigDecimal r51_QFHA) { R51_QFHA = r51_QFHA; }
		public String getR52_PRODUCT() { return R52_PRODUCT; }
		public void setR52_PRODUCT(String r52_PRODUCT) { R52_PRODUCT = r52_PRODUCT; }
		public BigDecimal getR52_NV_LONG() { return R52_NV_LONG; }
		public void setR52_NV_LONG(BigDecimal r52_NV_LONG) { R52_NV_LONG = r52_NV_LONG; }
		public BigDecimal getR52_NV_SHORT() { return R52_NV_SHORT; }
		public void setR52_NV_SHORT(BigDecimal r52_NV_SHORT) { R52_NV_SHORT = r52_NV_SHORT; }
		public BigDecimal getR52_FV_LONG() { return R52_FV_LONG; }
		public void setR52_FV_LONG(BigDecimal r52_FV_LONG) { R52_FV_LONG = r52_FV_LONG; }
		public BigDecimal getR52_FV_SHORT() { return R52_FV_SHORT; }
		public void setR52_FV_SHORT(BigDecimal r52_FV_SHORT) { R52_FV_SHORT = r52_FV_SHORT; }
		public BigDecimal getR52_QFHA() { return R52_QFHA; }
		public void setR52_QFHA(BigDecimal r52_QFHA) { R52_QFHA = r52_QFHA; }
		public String getR53_PRODUCT() { return R53_PRODUCT; }
		public void setR53_PRODUCT(String r53_PRODUCT) { R53_PRODUCT = r53_PRODUCT; }
		public BigDecimal getR53_NV_LONG() { return R53_NV_LONG; }
		public void setR53_NV_LONG(BigDecimal r53_NV_LONG) { R53_NV_LONG = r53_NV_LONG; }
		public BigDecimal getR53_NV_SHORT() { return R53_NV_SHORT; }
		public void setR53_NV_SHORT(BigDecimal r53_NV_SHORT) { R53_NV_SHORT = r53_NV_SHORT; }
		public BigDecimal getR53_FV_LONG() { return R53_FV_LONG; }
		public void setR53_FV_LONG(BigDecimal r53_FV_LONG) { R53_FV_LONG = r53_FV_LONG; }
		public BigDecimal getR53_FV_SHORT() { return R53_FV_SHORT; }
		public void setR53_FV_SHORT(BigDecimal r53_FV_SHORT) { R53_FV_SHORT = r53_FV_SHORT; }
		public BigDecimal getR53_QFHA() { return R53_QFHA; }
		public void setR53_QFHA(BigDecimal r53_QFHA) { R53_QFHA = r53_QFHA; }
		public String getR54_PRODUCT() { return R54_PRODUCT; }
		public void setR54_PRODUCT(String r54_PRODUCT) { R54_PRODUCT = r54_PRODUCT; }
		public BigDecimal getR54_NV_LONG() { return R54_NV_LONG; }
		public void setR54_NV_LONG(BigDecimal r54_NV_LONG) { R54_NV_LONG = r54_NV_LONG; }
		public BigDecimal getR54_NV_SHORT() { return R54_NV_SHORT; }
		public void setR54_NV_SHORT(BigDecimal r54_NV_SHORT) { R54_NV_SHORT = r54_NV_SHORT; }
		public BigDecimal getR54_FV_LONG() { return R54_FV_LONG; }
		public void setR54_FV_LONG(BigDecimal r54_FV_LONG) { R54_FV_LONG = r54_FV_LONG; }
		public BigDecimal getR54_FV_SHORT() { return R54_FV_SHORT; }
		public void setR54_FV_SHORT(BigDecimal r54_FV_SHORT) { R54_FV_SHORT = r54_FV_SHORT; }
		public BigDecimal getR54_QFHA() { return R54_QFHA; }
		public void setR54_QFHA(BigDecimal r54_QFHA) { R54_QFHA = r54_QFHA; }
		public String getR55_PRODUCT() { return R55_PRODUCT; }
		public void setR55_PRODUCT(String r55_PRODUCT) { R55_PRODUCT = r55_PRODUCT; }
		public BigDecimal getR55_NV_LONG() { return R55_NV_LONG; }
		public void setR55_NV_LONG(BigDecimal r55_NV_LONG) { R55_NV_LONG = r55_NV_LONG; }
		public BigDecimal getR55_NV_SHORT() { return R55_NV_SHORT; }
		public void setR55_NV_SHORT(BigDecimal r55_NV_SHORT) { R55_NV_SHORT = r55_NV_SHORT; }
		public BigDecimal getR55_FV_LONG() { return R55_FV_LONG; }
		public void setR55_FV_LONG(BigDecimal r55_FV_LONG) { R55_FV_LONG = r55_FV_LONG; }
		public BigDecimal getR55_FV_SHORT() { return R55_FV_SHORT; }
		public void setR55_FV_SHORT(BigDecimal r55_FV_SHORT) { R55_FV_SHORT = r55_FV_SHORT; }
		public BigDecimal getR55_QFHA() { return R55_QFHA; }
		public void setR55_QFHA(BigDecimal r55_QFHA) { R55_QFHA = r55_QFHA; }
		public Date getReportDate() { return reportDate; }
		public void setReportDate(Date reportDate) { this.reportDate = reportDate; }
		public BigDecimal getReportVersion() { return reportVersion; }
		public void setReportVersion(BigDecimal reportVersion) { this.reportVersion = reportVersion; }
		public String getREPORT_FREQUENCY() { return REPORT_FREQUENCY; }
		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) { REPORT_FREQUENCY = rEPORT_FREQUENCY; }
		public String getREPORT_CODE() { return REPORT_CODE; }
		public void setREPORT_CODE(String rEPORT_CODE) { REPORT_CODE = rEPORT_CODE; }
		public String getREPORT_DESC() { return REPORT_DESC; }
		public void setREPORT_DESC(String rEPORT_DESC) { REPORT_DESC = rEPORT_DESC; }
		public String getENTITY_FLG() { return ENTITY_FLG; }
		public void setENTITY_FLG(String eNTITY_FLG) { ENTITY_FLG = eNTITY_FLG; }
		public String getMODIFY_FLG() { return MODIFY_FLG; }
		public void setMODIFY_FLG(String mODIFY_FLG) { MODIFY_FLG = mODIFY_FLG; }
		public String getDEL_FLG() { return DEL_FLG; }
		public void setDEL_FLG(String dEL_FLG) { DEL_FLG = dEL_FLG; }
		public Date getReportResubDate() { return reportResubDate; }
		public void setReportResubDate(Date reportResubDate) { this.reportResubDate = reportResubDate; }
	}

	// =========================================================
	// Inner entity: M_TBS_Archival_Detail_Entity
	// =========================================================
	public static class M_TBS_Archival_Detail_Entity {
		private String R11_PRODUCT;
		private BigDecimal R11_NV_LONG;
		private BigDecimal R11_NV_SHORT;
		private BigDecimal R11_FV_LONG;
		private BigDecimal R11_FV_SHORT;
		private BigDecimal R11_QFHA;
		private String R12_PRODUCT;
		private BigDecimal R12_NV_LONG;
		private BigDecimal R12_NV_SHORT;
		private BigDecimal R12_FV_LONG;
		private BigDecimal R12_FV_SHORT;
		private BigDecimal R12_QFHA;
		private String R13_PRODUCT;
		private BigDecimal R13_NV_LONG;
		private BigDecimal R13_NV_SHORT;
		private BigDecimal R13_FV_LONG;
		private BigDecimal R13_FV_SHORT;
		private BigDecimal R13_QFHA;
		private String R14_PRODUCT;
		private BigDecimal R14_NV_LONG;
		private BigDecimal R14_NV_SHORT;
		private BigDecimal R14_FV_LONG;
		private BigDecimal R14_FV_SHORT;
		private BigDecimal R14_QFHA;
		private String R15_PRODUCT;
		private BigDecimal R15_NV_LONG;
		private BigDecimal R15_NV_SHORT;
		private BigDecimal R15_FV_LONG;
		private BigDecimal R15_FV_SHORT;
		private BigDecimal R15_QFHA;
		private String R16_PRODUCT;
		private BigDecimal R16_NV_LONG;
		private BigDecimal R16_NV_SHORT;
		private BigDecimal R16_FV_LONG;
		private BigDecimal R16_FV_SHORT;
		private BigDecimal R16_QFHA;
		private String R17_PRODUCT;
		private BigDecimal R17_NV_LONG;
		private BigDecimal R17_NV_SHORT;
		private BigDecimal R17_FV_LONG;
		private BigDecimal R17_FV_SHORT;
		private BigDecimal R17_QFHA;
		private String R18_PRODUCT;
		private BigDecimal R18_NV_LONG;
		private BigDecimal R18_NV_SHORT;
		private BigDecimal R18_FV_LONG;
		private BigDecimal R18_FV_SHORT;
		private BigDecimal R18_QFHA;
		private String R19_PRODUCT;
		private BigDecimal R19_NV_LONG;
		private BigDecimal R19_NV_SHORT;
		private BigDecimal R19_FV_LONG;
		private BigDecimal R19_FV_SHORT;
		private BigDecimal R19_QFHA;
		private String R20_PRODUCT;
		private BigDecimal R20_NV_LONG;
		private BigDecimal R20_NV_SHORT;
		private BigDecimal R20_FV_LONG;
		private BigDecimal R20_FV_SHORT;
		private BigDecimal R20_QFHA;
		private String R21_PRODUCT;
		private BigDecimal R21_NV_LONG;
		private BigDecimal R21_NV_SHORT;
		private BigDecimal R21_FV_LONG;
		private BigDecimal R21_FV_SHORT;
		private BigDecimal R21_QFHA;
		private String R22_PRODUCT;
		private BigDecimal R22_NV_LONG;
		private BigDecimal R22_NV_SHORT;
		private BigDecimal R22_FV_LONG;
		private BigDecimal R22_FV_SHORT;
		private BigDecimal R22_QFHA;
		private String R23_PRODUCT;
		private BigDecimal R23_NV_LONG;
		private BigDecimal R23_NV_SHORT;
		private BigDecimal R23_FV_LONG;
		private BigDecimal R23_FV_SHORT;
		private BigDecimal R23_QFHA;
		private String R24_PRODUCT;
		private BigDecimal R24_NV_LONG;
		private BigDecimal R24_NV_SHORT;
		private BigDecimal R24_FV_LONG;
		private BigDecimal R24_FV_SHORT;
		private BigDecimal R24_QFHA;
		private String R25_PRODUCT;
		private BigDecimal R25_NV_LONG;
		private BigDecimal R25_NV_SHORT;
		private BigDecimal R25_FV_LONG;
		private BigDecimal R25_FV_SHORT;
		private BigDecimal R25_QFHA;
		private String R26_PRODUCT;
		private BigDecimal R26_NV_LONG;
		private BigDecimal R26_NV_SHORT;
		private BigDecimal R26_FV_LONG;
		private BigDecimal R26_FV_SHORT;
		private BigDecimal R26_QFHA;
		private String R27_PRODUCT;
		private BigDecimal R27_NV_LONG;
		private BigDecimal R27_NV_SHORT;
		private BigDecimal R27_FV_LONG;
		private BigDecimal R27_FV_SHORT;
		private BigDecimal R27_QFHA;
		private String R28_PRODUCT;
		private BigDecimal R28_NV_LONG;
		private BigDecimal R28_NV_SHORT;
		private BigDecimal R28_FV_LONG;
		private BigDecimal R28_FV_SHORT;
		private BigDecimal R28_QFHA;
		private String R29_PRODUCT;
		private BigDecimal R29_NV_LONG;
		private BigDecimal R29_NV_SHORT;
		private BigDecimal R29_FV_LONG;
		private BigDecimal R29_FV_SHORT;
		private BigDecimal R29_QFHA;
		private String R30_PRODUCT;
		private BigDecimal R30_NV_LONG;
		private BigDecimal R30_NV_SHORT;
		private BigDecimal R30_FV_LONG;
		private BigDecimal R30_FV_SHORT;
		private BigDecimal R30_QFHA;
		private String R31_PRODUCT;
		private BigDecimal R31_NV_LONG;
		private BigDecimal R31_NV_SHORT;
		private BigDecimal R31_FV_LONG;
		private BigDecimal R31_FV_SHORT;
		private BigDecimal R31_QFHA;
		private String R32_PRODUCT;
		private BigDecimal R32_NV_LONG;
		private BigDecimal R32_NV_SHORT;
		private BigDecimal R32_FV_LONG;
		private BigDecimal R32_FV_SHORT;
		private BigDecimal R32_QFHA;
		private String R33_PRODUCT;
		private BigDecimal R33_NV_LONG;
		private BigDecimal R33_NV_SHORT;
		private BigDecimal R33_FV_LONG;
		private BigDecimal R33_FV_SHORT;
		private BigDecimal R33_QFHA;
		private String R34_PRODUCT;
		private BigDecimal R34_NV_LONG;
		private BigDecimal R34_NV_SHORT;
		private BigDecimal R34_FV_LONG;
		private BigDecimal R34_FV_SHORT;
		private BigDecimal R34_QFHA;
		private String R35_PRODUCT;
		private BigDecimal R35_NV_LONG;
		private BigDecimal R35_NV_SHORT;
		private BigDecimal R35_FV_LONG;
		private BigDecimal R35_FV_SHORT;
		private BigDecimal R35_QFHA;
		private String R36_PRODUCT;
		private BigDecimal R36_NV_LONG;
		private BigDecimal R36_NV_SHORT;
		private BigDecimal R36_FV_LONG;
		private BigDecimal R36_FV_SHORT;
		private BigDecimal R36_QFHA;
		private String R37_PRODUCT;
		private BigDecimal R37_NV_LONG;
		private BigDecimal R37_NV_SHORT;
		private BigDecimal R37_FV_LONG;
		private BigDecimal R37_FV_SHORT;
		private BigDecimal R37_QFHA;
		private String R38_PRODUCT;
		private BigDecimal R38_NV_LONG;
		private BigDecimal R38_NV_SHORT;
		private BigDecimal R38_FV_LONG;
		private BigDecimal R38_FV_SHORT;
		private BigDecimal R38_QFHA;
		private String R39_PRODUCT;
		private BigDecimal R39_NV_LONG;
		private BigDecimal R39_NV_SHORT;
		private BigDecimal R39_FV_LONG;
		private BigDecimal R39_FV_SHORT;
		private BigDecimal R39_QFHA;
		private String R40_PRODUCT;
		private BigDecimal R40_NV_LONG;
		private BigDecimal R40_NV_SHORT;
		private BigDecimal R40_FV_LONG;
		private BigDecimal R40_FV_SHORT;
		private BigDecimal R40_QFHA;
		private String R41_PRODUCT;
		private BigDecimal R41_NV_LONG;
		private BigDecimal R41_NV_SHORT;
		private BigDecimal R41_FV_LONG;
		private BigDecimal R41_FV_SHORT;
		private BigDecimal R41_QFHA;
		private String R42_PRODUCT;
		private BigDecimal R42_NV_LONG;
		private BigDecimal R42_NV_SHORT;
		private BigDecimal R42_FV_LONG;
		private BigDecimal R42_FV_SHORT;
		private BigDecimal R42_QFHA;
		private String R43_PRODUCT;
		private BigDecimal R43_NV_LONG;
		private BigDecimal R43_NV_SHORT;
		private BigDecimal R43_FV_LONG;
		private BigDecimal R43_FV_SHORT;
		private BigDecimal R43_QFHA;
		private String R44_PRODUCT;
		private BigDecimal R44_NV_LONG;
		private BigDecimal R44_NV_SHORT;
		private BigDecimal R44_FV_LONG;
		private BigDecimal R44_FV_SHORT;
		private BigDecimal R44_QFHA;
		private String R45_PRODUCT;
		private BigDecimal R45_NV_LONG;
		private BigDecimal R45_NV_SHORT;
		private BigDecimal R45_FV_LONG;
		private BigDecimal R45_FV_SHORT;
		private BigDecimal R45_QFHA;
		private String R46_PRODUCT;
		private BigDecimal R46_NV_LONG;
		private BigDecimal R46_NV_SHORT;
		private BigDecimal R46_FV_LONG;
		private BigDecimal R46_FV_SHORT;
		private BigDecimal R46_QFHA;
		private String R47_PRODUCT;
		private BigDecimal R47_NV_LONG;
		private BigDecimal R47_NV_SHORT;
		private BigDecimal R47_FV_LONG;
		private BigDecimal R47_FV_SHORT;
		private BigDecimal R47_QFHA;
		private String R48_PRODUCT;
		private BigDecimal R48_NV_LONG;
		private BigDecimal R48_NV_SHORT;
		private BigDecimal R48_FV_LONG;
		private BigDecimal R48_FV_SHORT;
		private BigDecimal R48_QFHA;
		private String R49_PRODUCT;
		private BigDecimal R49_NV_LONG;
		private BigDecimal R49_NV_SHORT;
		private BigDecimal R49_FV_LONG;
		private BigDecimal R49_FV_SHORT;
		private BigDecimal R49_QFHA;
		private String R50_PRODUCT;
		private BigDecimal R50_NV_LONG;
		private BigDecimal R50_NV_SHORT;
		private BigDecimal R50_FV_LONG;
		private BigDecimal R50_FV_SHORT;
		private BigDecimal R50_QFHA;
		private String R51_PRODUCT;
		private BigDecimal R51_NV_LONG;
		private BigDecimal R51_NV_SHORT;
		private BigDecimal R51_FV_LONG;
		private BigDecimal R51_FV_SHORT;
		private BigDecimal R51_QFHA;
		private String R52_PRODUCT;
		private BigDecimal R52_NV_LONG;
		private BigDecimal R52_NV_SHORT;
		private BigDecimal R52_FV_LONG;
		private BigDecimal R52_FV_SHORT;
		private BigDecimal R52_QFHA;
		private String R53_PRODUCT;
		private BigDecimal R53_NV_LONG;
		private BigDecimal R53_NV_SHORT;
		private BigDecimal R53_FV_LONG;
		private BigDecimal R53_FV_SHORT;
		private BigDecimal R53_QFHA;
		private String R54_PRODUCT;
		private BigDecimal R54_NV_LONG;
		private BigDecimal R54_NV_SHORT;
		private BigDecimal R54_FV_LONG;
		private BigDecimal R54_FV_SHORT;
		private BigDecimal R54_QFHA;
		private String R55_PRODUCT;
		private BigDecimal R55_NV_LONG;
		private BigDecimal R55_NV_SHORT;
		private BigDecimal R55_FV_LONG;
		private BigDecimal R55_FV_SHORT;
		private BigDecimal R55_QFHA;

		private Date reportDate;
		private BigDecimal reportVersion;
		public String REPORT_FREQUENCY;
		public String REPORT_CODE;
		public String REPORT_DESC;
		public String ENTITY_FLG;
		public String MODIFY_FLG;
		public String DEL_FLG;
		private Date reportResubDate;

		public String getR11_PRODUCT() { return R11_PRODUCT; }
		public void setR11_PRODUCT(String r11_PRODUCT) { R11_PRODUCT = r11_PRODUCT; }
		public BigDecimal getR11_NV_LONG() { return R11_NV_LONG; }
		public void setR11_NV_LONG(BigDecimal r11_NV_LONG) { R11_NV_LONG = r11_NV_LONG; }
		public BigDecimal getR11_NV_SHORT() { return R11_NV_SHORT; }
		public void setR11_NV_SHORT(BigDecimal r11_NV_SHORT) { R11_NV_SHORT = r11_NV_SHORT; }
		public BigDecimal getR11_FV_LONG() { return R11_FV_LONG; }
		public void setR11_FV_LONG(BigDecimal r11_FV_LONG) { R11_FV_LONG = r11_FV_LONG; }
		public BigDecimal getR11_FV_SHORT() { return R11_FV_SHORT; }
		public void setR11_FV_SHORT(BigDecimal r11_FV_SHORT) { R11_FV_SHORT = r11_FV_SHORT; }
		public BigDecimal getR11_QFHA() { return R11_QFHA; }
		public void setR11_QFHA(BigDecimal r11_QFHA) { R11_QFHA = r11_QFHA; }
		public String getR12_PRODUCT() { return R12_PRODUCT; }
		public void setR12_PRODUCT(String r12_PRODUCT) { R12_PRODUCT = r12_PRODUCT; }
		public BigDecimal getR12_NV_LONG() { return R12_NV_LONG; }
		public void setR12_NV_LONG(BigDecimal r12_NV_LONG) { R12_NV_LONG = r12_NV_LONG; }
		public BigDecimal getR12_NV_SHORT() { return R12_NV_SHORT; }
		public void setR12_NV_SHORT(BigDecimal r12_NV_SHORT) { R12_NV_SHORT = r12_NV_SHORT; }
		public BigDecimal getR12_FV_LONG() { return R12_FV_LONG; }
		public void setR12_FV_LONG(BigDecimal r12_FV_LONG) { R12_FV_LONG = r12_FV_LONG; }
		public BigDecimal getR12_FV_SHORT() { return R12_FV_SHORT; }
		public void setR12_FV_SHORT(BigDecimal r12_FV_SHORT) { R12_FV_SHORT = r12_FV_SHORT; }
		public BigDecimal getR12_QFHA() { return R12_QFHA; }
		public void setR12_QFHA(BigDecimal r12_QFHA) { R12_QFHA = r12_QFHA; }
		public String getR13_PRODUCT() { return R13_PRODUCT; }
		public void setR13_PRODUCT(String r13_PRODUCT) { R13_PRODUCT = r13_PRODUCT; }
		public BigDecimal getR13_NV_LONG() { return R13_NV_LONG; }
		public void setR13_NV_LONG(BigDecimal r13_NV_LONG) { R13_NV_LONG = r13_NV_LONG; }
		public BigDecimal getR13_NV_SHORT() { return R13_NV_SHORT; }
		public void setR13_NV_SHORT(BigDecimal r13_NV_SHORT) { R13_NV_SHORT = r13_NV_SHORT; }
		public BigDecimal getR13_FV_LONG() { return R13_FV_LONG; }
		public void setR13_FV_LONG(BigDecimal r13_FV_LONG) { R13_FV_LONG = r13_FV_LONG; }
		public BigDecimal getR13_FV_SHORT() { return R13_FV_SHORT; }
		public void setR13_FV_SHORT(BigDecimal r13_FV_SHORT) { R13_FV_SHORT = r13_FV_SHORT; }
		public BigDecimal getR13_QFHA() { return R13_QFHA; }
		public void setR13_QFHA(BigDecimal r13_QFHA) { R13_QFHA = r13_QFHA; }
		public String getR14_PRODUCT() { return R14_PRODUCT; }
		public void setR14_PRODUCT(String r14_PRODUCT) { R14_PRODUCT = r14_PRODUCT; }
		public BigDecimal getR14_NV_LONG() { return R14_NV_LONG; }
		public void setR14_NV_LONG(BigDecimal r14_NV_LONG) { R14_NV_LONG = r14_NV_LONG; }
		public BigDecimal getR14_NV_SHORT() { return R14_NV_SHORT; }
		public void setR14_NV_SHORT(BigDecimal r14_NV_SHORT) { R14_NV_SHORT = r14_NV_SHORT; }
		public BigDecimal getR14_FV_LONG() { return R14_FV_LONG; }
		public void setR14_FV_LONG(BigDecimal r14_FV_LONG) { R14_FV_LONG = r14_FV_LONG; }
		public BigDecimal getR14_FV_SHORT() { return R14_FV_SHORT; }
		public void setR14_FV_SHORT(BigDecimal r14_FV_SHORT) { R14_FV_SHORT = r14_FV_SHORT; }
		public BigDecimal getR14_QFHA() { return R14_QFHA; }
		public void setR14_QFHA(BigDecimal r14_QFHA) { R14_QFHA = r14_QFHA; }
		public String getR15_PRODUCT() { return R15_PRODUCT; }
		public void setR15_PRODUCT(String r15_PRODUCT) { R15_PRODUCT = r15_PRODUCT; }
		public BigDecimal getR15_NV_LONG() { return R15_NV_LONG; }
		public void setR15_NV_LONG(BigDecimal r15_NV_LONG) { R15_NV_LONG = r15_NV_LONG; }
		public BigDecimal getR15_NV_SHORT() { return R15_NV_SHORT; }
		public void setR15_NV_SHORT(BigDecimal r15_NV_SHORT) { R15_NV_SHORT = r15_NV_SHORT; }
		public BigDecimal getR15_FV_LONG() { return R15_FV_LONG; }
		public void setR15_FV_LONG(BigDecimal r15_FV_LONG) { R15_FV_LONG = r15_FV_LONG; }
		public BigDecimal getR15_FV_SHORT() { return R15_FV_SHORT; }
		public void setR15_FV_SHORT(BigDecimal r15_FV_SHORT) { R15_FV_SHORT = r15_FV_SHORT; }
		public BigDecimal getR15_QFHA() { return R15_QFHA; }
		public void setR15_QFHA(BigDecimal r15_QFHA) { R15_QFHA = r15_QFHA; }
		public String getR16_PRODUCT() { return R16_PRODUCT; }
		public void setR16_PRODUCT(String r16_PRODUCT) { R16_PRODUCT = r16_PRODUCT; }
		public BigDecimal getR16_NV_LONG() { return R16_NV_LONG; }
		public void setR16_NV_LONG(BigDecimal r16_NV_LONG) { R16_NV_LONG = r16_NV_LONG; }
		public BigDecimal getR16_NV_SHORT() { return R16_NV_SHORT; }
		public void setR16_NV_SHORT(BigDecimal r16_NV_SHORT) { R16_NV_SHORT = r16_NV_SHORT; }
		public BigDecimal getR16_FV_LONG() { return R16_FV_LONG; }
		public void setR16_FV_LONG(BigDecimal r16_FV_LONG) { R16_FV_LONG = r16_FV_LONG; }
		public BigDecimal getR16_FV_SHORT() { return R16_FV_SHORT; }
		public void setR16_FV_SHORT(BigDecimal r16_FV_SHORT) { R16_FV_SHORT = r16_FV_SHORT; }
		public BigDecimal getR16_QFHA() { return R16_QFHA; }
		public void setR16_QFHA(BigDecimal r16_QFHA) { R16_QFHA = r16_QFHA; }
		public String getR17_PRODUCT() { return R17_PRODUCT; }
		public void setR17_PRODUCT(String r17_PRODUCT) { R17_PRODUCT = r17_PRODUCT; }
		public BigDecimal getR17_NV_LONG() { return R17_NV_LONG; }
		public void setR17_NV_LONG(BigDecimal r17_NV_LONG) { R17_NV_LONG = r17_NV_LONG; }
		public BigDecimal getR17_NV_SHORT() { return R17_NV_SHORT; }
		public void setR17_NV_SHORT(BigDecimal r17_NV_SHORT) { R17_NV_SHORT = r17_NV_SHORT; }
		public BigDecimal getR17_FV_LONG() { return R17_FV_LONG; }
		public void setR17_FV_LONG(BigDecimal r17_FV_LONG) { R17_FV_LONG = r17_FV_LONG; }
		public BigDecimal getR17_FV_SHORT() { return R17_FV_SHORT; }
		public void setR17_FV_SHORT(BigDecimal r17_FV_SHORT) { R17_FV_SHORT = r17_FV_SHORT; }
		public BigDecimal getR17_QFHA() { return R17_QFHA; }
		public void setR17_QFHA(BigDecimal r17_QFHA) { R17_QFHA = r17_QFHA; }
		public String getR18_PRODUCT() { return R18_PRODUCT; }
		public void setR18_PRODUCT(String r18_PRODUCT) { R18_PRODUCT = r18_PRODUCT; }
		public BigDecimal getR18_NV_LONG() { return R18_NV_LONG; }
		public void setR18_NV_LONG(BigDecimal r18_NV_LONG) { R18_NV_LONG = r18_NV_LONG; }
		public BigDecimal getR18_NV_SHORT() { return R18_NV_SHORT; }
		public void setR18_NV_SHORT(BigDecimal r18_NV_SHORT) { R18_NV_SHORT = r18_NV_SHORT; }
		public BigDecimal getR18_FV_LONG() { return R18_FV_LONG; }
		public void setR18_FV_LONG(BigDecimal r18_FV_LONG) { R18_FV_LONG = r18_FV_LONG; }
		public BigDecimal getR18_FV_SHORT() { return R18_FV_SHORT; }
		public void setR18_FV_SHORT(BigDecimal r18_FV_SHORT) { R18_FV_SHORT = r18_FV_SHORT; }
		public BigDecimal getR18_QFHA() { return R18_QFHA; }
		public void setR18_QFHA(BigDecimal r18_QFHA) { R18_QFHA = r18_QFHA; }
		public String getR19_PRODUCT() { return R19_PRODUCT; }
		public void setR19_PRODUCT(String r19_PRODUCT) { R19_PRODUCT = r19_PRODUCT; }
		public BigDecimal getR19_NV_LONG() { return R19_NV_LONG; }
		public void setR19_NV_LONG(BigDecimal r19_NV_LONG) { R19_NV_LONG = r19_NV_LONG; }
		public BigDecimal getR19_NV_SHORT() { return R19_NV_SHORT; }
		public void setR19_NV_SHORT(BigDecimal r19_NV_SHORT) { R19_NV_SHORT = r19_NV_SHORT; }
		public BigDecimal getR19_FV_LONG() { return R19_FV_LONG; }
		public void setR19_FV_LONG(BigDecimal r19_FV_LONG) { R19_FV_LONG = r19_FV_LONG; }
		public BigDecimal getR19_FV_SHORT() { return R19_FV_SHORT; }
		public void setR19_FV_SHORT(BigDecimal r19_FV_SHORT) { R19_FV_SHORT = r19_FV_SHORT; }
		public BigDecimal getR19_QFHA() { return R19_QFHA; }
		public void setR19_QFHA(BigDecimal r19_QFHA) { R19_QFHA = r19_QFHA; }
		public String getR20_PRODUCT() { return R20_PRODUCT; }
		public void setR20_PRODUCT(String r20_PRODUCT) { R20_PRODUCT = r20_PRODUCT; }
		public BigDecimal getR20_NV_LONG() { return R20_NV_LONG; }
		public void setR20_NV_LONG(BigDecimal r20_NV_LONG) { R20_NV_LONG = r20_NV_LONG; }
		public BigDecimal getR20_NV_SHORT() { return R20_NV_SHORT; }
		public void setR20_NV_SHORT(BigDecimal r20_NV_SHORT) { R20_NV_SHORT = r20_NV_SHORT; }
		public BigDecimal getR20_FV_LONG() { return R20_FV_LONG; }
		public void setR20_FV_LONG(BigDecimal r20_FV_LONG) { R20_FV_LONG = r20_FV_LONG; }
		public BigDecimal getR20_FV_SHORT() { return R20_FV_SHORT; }
		public void setR20_FV_SHORT(BigDecimal r20_FV_SHORT) { R20_FV_SHORT = r20_FV_SHORT; }
		public BigDecimal getR20_QFHA() { return R20_QFHA; }
		public void setR20_QFHA(BigDecimal r20_QFHA) { R20_QFHA = r20_QFHA; }
		public String getR21_PRODUCT() { return R21_PRODUCT; }
		public void setR21_PRODUCT(String r21_PRODUCT) { R21_PRODUCT = r21_PRODUCT; }
		public BigDecimal getR21_NV_LONG() { return R21_NV_LONG; }
		public void setR21_NV_LONG(BigDecimal r21_NV_LONG) { R21_NV_LONG = r21_NV_LONG; }
		public BigDecimal getR21_NV_SHORT() { return R21_NV_SHORT; }
		public void setR21_NV_SHORT(BigDecimal r21_NV_SHORT) { R21_NV_SHORT = r21_NV_SHORT; }
		public BigDecimal getR21_FV_LONG() { return R21_FV_LONG; }
		public void setR21_FV_LONG(BigDecimal r21_FV_LONG) { R21_FV_LONG = r21_FV_LONG; }
		public BigDecimal getR21_FV_SHORT() { return R21_FV_SHORT; }
		public void setR21_FV_SHORT(BigDecimal r21_FV_SHORT) { R21_FV_SHORT = r21_FV_SHORT; }
		public BigDecimal getR21_QFHA() { return R21_QFHA; }
		public void setR21_QFHA(BigDecimal r21_QFHA) { R21_QFHA = r21_QFHA; }
		public String getR22_PRODUCT() { return R22_PRODUCT; }
		public void setR22_PRODUCT(String r22_PRODUCT) { R22_PRODUCT = r22_PRODUCT; }
		public BigDecimal getR22_NV_LONG() { return R22_NV_LONG; }
		public void setR22_NV_LONG(BigDecimal r22_NV_LONG) { R22_NV_LONG = r22_NV_LONG; }
		public BigDecimal getR22_NV_SHORT() { return R22_NV_SHORT; }
		public void setR22_NV_SHORT(BigDecimal r22_NV_SHORT) { R22_NV_SHORT = r22_NV_SHORT; }
		public BigDecimal getR22_FV_LONG() { return R22_FV_LONG; }
		public void setR22_FV_LONG(BigDecimal r22_FV_LONG) { R22_FV_LONG = r22_FV_LONG; }
		public BigDecimal getR22_FV_SHORT() { return R22_FV_SHORT; }
		public void setR22_FV_SHORT(BigDecimal r22_FV_SHORT) { R22_FV_SHORT = r22_FV_SHORT; }
		public BigDecimal getR22_QFHA() { return R22_QFHA; }
		public void setR22_QFHA(BigDecimal r22_QFHA) { R22_QFHA = r22_QFHA; }
		public String getR23_PRODUCT() { return R23_PRODUCT; }
		public void setR23_PRODUCT(String r23_PRODUCT) { R23_PRODUCT = r23_PRODUCT; }
		public BigDecimal getR23_NV_LONG() { return R23_NV_LONG; }
		public void setR23_NV_LONG(BigDecimal r23_NV_LONG) { R23_NV_LONG = r23_NV_LONG; }
		public BigDecimal getR23_NV_SHORT() { return R23_NV_SHORT; }
		public void setR23_NV_SHORT(BigDecimal r23_NV_SHORT) { R23_NV_SHORT = r23_NV_SHORT; }
		public BigDecimal getR23_FV_LONG() { return R23_FV_LONG; }
		public void setR23_FV_LONG(BigDecimal r23_FV_LONG) { R23_FV_LONG = r23_FV_LONG; }
		public BigDecimal getR23_FV_SHORT() { return R23_FV_SHORT; }
		public void setR23_FV_SHORT(BigDecimal r23_FV_SHORT) { R23_FV_SHORT = r23_FV_SHORT; }
		public BigDecimal getR23_QFHA() { return R23_QFHA; }
		public void setR23_QFHA(BigDecimal r23_QFHA) { R23_QFHA = r23_QFHA; }
		public String getR24_PRODUCT() { return R24_PRODUCT; }
		public void setR24_PRODUCT(String r24_PRODUCT) { R24_PRODUCT = r24_PRODUCT; }
		public BigDecimal getR24_NV_LONG() { return R24_NV_LONG; }
		public void setR24_NV_LONG(BigDecimal r24_NV_LONG) { R24_NV_LONG = r24_NV_LONG; }
		public BigDecimal getR24_NV_SHORT() { return R24_NV_SHORT; }
		public void setR24_NV_SHORT(BigDecimal r24_NV_SHORT) { R24_NV_SHORT = r24_NV_SHORT; }
		public BigDecimal getR24_FV_LONG() { return R24_FV_LONG; }
		public void setR24_FV_LONG(BigDecimal r24_FV_LONG) { R24_FV_LONG = r24_FV_LONG; }
		public BigDecimal getR24_FV_SHORT() { return R24_FV_SHORT; }
		public void setR24_FV_SHORT(BigDecimal r24_FV_SHORT) { R24_FV_SHORT = r24_FV_SHORT; }
		public BigDecimal getR24_QFHA() { return R24_QFHA; }
		public void setR24_QFHA(BigDecimal r24_QFHA) { R24_QFHA = r24_QFHA; }
		public String getR25_PRODUCT() { return R25_PRODUCT; }
		public void setR25_PRODUCT(String r25_PRODUCT) { R25_PRODUCT = r25_PRODUCT; }
		public BigDecimal getR25_NV_LONG() { return R25_NV_LONG; }
		public void setR25_NV_LONG(BigDecimal r25_NV_LONG) { R25_NV_LONG = r25_NV_LONG; }
		public BigDecimal getR25_NV_SHORT() { return R25_NV_SHORT; }
		public void setR25_NV_SHORT(BigDecimal r25_NV_SHORT) { R25_NV_SHORT = r25_NV_SHORT; }
		public BigDecimal getR25_FV_LONG() { return R25_FV_LONG; }
		public void setR25_FV_LONG(BigDecimal r25_FV_LONG) { R25_FV_LONG = r25_FV_LONG; }
		public BigDecimal getR25_FV_SHORT() { return R25_FV_SHORT; }
		public void setR25_FV_SHORT(BigDecimal r25_FV_SHORT) { R25_FV_SHORT = r25_FV_SHORT; }
		public BigDecimal getR25_QFHA() { return R25_QFHA; }
		public void setR25_QFHA(BigDecimal r25_QFHA) { R25_QFHA = r25_QFHA; }
		public String getR26_PRODUCT() { return R26_PRODUCT; }
		public void setR26_PRODUCT(String r26_PRODUCT) { R26_PRODUCT = r26_PRODUCT; }
		public BigDecimal getR26_NV_LONG() { return R26_NV_LONG; }
		public void setR26_NV_LONG(BigDecimal r26_NV_LONG) { R26_NV_LONG = r26_NV_LONG; }
		public BigDecimal getR26_NV_SHORT() { return R26_NV_SHORT; }
		public void setR26_NV_SHORT(BigDecimal r26_NV_SHORT) { R26_NV_SHORT = r26_NV_SHORT; }
		public BigDecimal getR26_FV_LONG() { return R26_FV_LONG; }
		public void setR26_FV_LONG(BigDecimal r26_FV_LONG) { R26_FV_LONG = r26_FV_LONG; }
		public BigDecimal getR26_FV_SHORT() { return R26_FV_SHORT; }
		public void setR26_FV_SHORT(BigDecimal r26_FV_SHORT) { R26_FV_SHORT = r26_FV_SHORT; }
		public BigDecimal getR26_QFHA() { return R26_QFHA; }
		public void setR26_QFHA(BigDecimal r26_QFHA) { R26_QFHA = r26_QFHA; }
		public String getR27_PRODUCT() { return R27_PRODUCT; }
		public void setR27_PRODUCT(String r27_PRODUCT) { R27_PRODUCT = r27_PRODUCT; }
		public BigDecimal getR27_NV_LONG() { return R27_NV_LONG; }
		public void setR27_NV_LONG(BigDecimal r27_NV_LONG) { R27_NV_LONG = r27_NV_LONG; }
		public BigDecimal getR27_NV_SHORT() { return R27_NV_SHORT; }
		public void setR27_NV_SHORT(BigDecimal r27_NV_SHORT) { R27_NV_SHORT = r27_NV_SHORT; }
		public BigDecimal getR27_FV_LONG() { return R27_FV_LONG; }
		public void setR27_FV_LONG(BigDecimal r27_FV_LONG) { R27_FV_LONG = r27_FV_LONG; }
		public BigDecimal getR27_FV_SHORT() { return R27_FV_SHORT; }
		public void setR27_FV_SHORT(BigDecimal r27_FV_SHORT) { R27_FV_SHORT = r27_FV_SHORT; }
		public BigDecimal getR27_QFHA() { return R27_QFHA; }
		public void setR27_QFHA(BigDecimal r27_QFHA) { R27_QFHA = r27_QFHA; }
		public String getR28_PRODUCT() { return R28_PRODUCT; }
		public void setR28_PRODUCT(String r28_PRODUCT) { R28_PRODUCT = r28_PRODUCT; }
		public BigDecimal getR28_NV_LONG() { return R28_NV_LONG; }
		public void setR28_NV_LONG(BigDecimal r28_NV_LONG) { R28_NV_LONG = r28_NV_LONG; }
		public BigDecimal getR28_NV_SHORT() { return R28_NV_SHORT; }
		public void setR28_NV_SHORT(BigDecimal r28_NV_SHORT) { R28_NV_SHORT = r28_NV_SHORT; }
		public BigDecimal getR28_FV_LONG() { return R28_FV_LONG; }
		public void setR28_FV_LONG(BigDecimal r28_FV_LONG) { R28_FV_LONG = r28_FV_LONG; }
		public BigDecimal getR28_FV_SHORT() { return R28_FV_SHORT; }
		public void setR28_FV_SHORT(BigDecimal r28_FV_SHORT) { R28_FV_SHORT = r28_FV_SHORT; }
		public BigDecimal getR28_QFHA() { return R28_QFHA; }
		public void setR28_QFHA(BigDecimal r28_QFHA) { R28_QFHA = r28_QFHA; }
		public String getR29_PRODUCT() { return R29_PRODUCT; }
		public void setR29_PRODUCT(String r29_PRODUCT) { R29_PRODUCT = r29_PRODUCT; }
		public BigDecimal getR29_NV_LONG() { return R29_NV_LONG; }
		public void setR29_NV_LONG(BigDecimal r29_NV_LONG) { R29_NV_LONG = r29_NV_LONG; }
		public BigDecimal getR29_NV_SHORT() { return R29_NV_SHORT; }
		public void setR29_NV_SHORT(BigDecimal r29_NV_SHORT) { R29_NV_SHORT = r29_NV_SHORT; }
		public BigDecimal getR29_FV_LONG() { return R29_FV_LONG; }
		public void setR29_FV_LONG(BigDecimal r29_FV_LONG) { R29_FV_LONG = r29_FV_LONG; }
		public BigDecimal getR29_FV_SHORT() { return R29_FV_SHORT; }
		public void setR29_FV_SHORT(BigDecimal r29_FV_SHORT) { R29_FV_SHORT = r29_FV_SHORT; }
		public BigDecimal getR29_QFHA() { return R29_QFHA; }
		public void setR29_QFHA(BigDecimal r29_QFHA) { R29_QFHA = r29_QFHA; }
		public String getR30_PRODUCT() { return R30_PRODUCT; }
		public void setR30_PRODUCT(String r30_PRODUCT) { R30_PRODUCT = r30_PRODUCT; }
		public BigDecimal getR30_NV_LONG() { return R30_NV_LONG; }
		public void setR30_NV_LONG(BigDecimal r30_NV_LONG) { R30_NV_LONG = r30_NV_LONG; }
		public BigDecimal getR30_NV_SHORT() { return R30_NV_SHORT; }
		public void setR30_NV_SHORT(BigDecimal r30_NV_SHORT) { R30_NV_SHORT = r30_NV_SHORT; }
		public BigDecimal getR30_FV_LONG() { return R30_FV_LONG; }
		public void setR30_FV_LONG(BigDecimal r30_FV_LONG) { R30_FV_LONG = r30_FV_LONG; }
		public BigDecimal getR30_FV_SHORT() { return R30_FV_SHORT; }
		public void setR30_FV_SHORT(BigDecimal r30_FV_SHORT) { R30_FV_SHORT = r30_FV_SHORT; }
		public BigDecimal getR30_QFHA() { return R30_QFHA; }
		public void setR30_QFHA(BigDecimal r30_QFHA) { R30_QFHA = r30_QFHA; }
		public String getR31_PRODUCT() { return R31_PRODUCT; }
		public void setR31_PRODUCT(String r31_PRODUCT) { R31_PRODUCT = r31_PRODUCT; }
		public BigDecimal getR31_NV_LONG() { return R31_NV_LONG; }
		public void setR31_NV_LONG(BigDecimal r31_NV_LONG) { R31_NV_LONG = r31_NV_LONG; }
		public BigDecimal getR31_NV_SHORT() { return R31_NV_SHORT; }
		public void setR31_NV_SHORT(BigDecimal r31_NV_SHORT) { R31_NV_SHORT = r31_NV_SHORT; }
		public BigDecimal getR31_FV_LONG() { return R31_FV_LONG; }
		public void setR31_FV_LONG(BigDecimal r31_FV_LONG) { R31_FV_LONG = r31_FV_LONG; }
		public BigDecimal getR31_FV_SHORT() { return R31_FV_SHORT; }
		public void setR31_FV_SHORT(BigDecimal r31_FV_SHORT) { R31_FV_SHORT = r31_FV_SHORT; }
		public BigDecimal getR31_QFHA() { return R31_QFHA; }
		public void setR31_QFHA(BigDecimal r31_QFHA) { R31_QFHA = r31_QFHA; }
		public String getR32_PRODUCT() { return R32_PRODUCT; }
		public void setR32_PRODUCT(String r32_PRODUCT) { R32_PRODUCT = r32_PRODUCT; }
		public BigDecimal getR32_NV_LONG() { return R32_NV_LONG; }
		public void setR32_NV_LONG(BigDecimal r32_NV_LONG) { R32_NV_LONG = r32_NV_LONG; }
		public BigDecimal getR32_NV_SHORT() { return R32_NV_SHORT; }
		public void setR32_NV_SHORT(BigDecimal r32_NV_SHORT) { R32_NV_SHORT = r32_NV_SHORT; }
		public BigDecimal getR32_FV_LONG() { return R32_FV_LONG; }
		public void setR32_FV_LONG(BigDecimal r32_FV_LONG) { R32_FV_LONG = r32_FV_LONG; }
		public BigDecimal getR32_FV_SHORT() { return R32_FV_SHORT; }
		public void setR32_FV_SHORT(BigDecimal r32_FV_SHORT) { R32_FV_SHORT = r32_FV_SHORT; }
		public BigDecimal getR32_QFHA() { return R32_QFHA; }
		public void setR32_QFHA(BigDecimal r32_QFHA) { R32_QFHA = r32_QFHA; }
		public String getR33_PRODUCT() { return R33_PRODUCT; }
		public void setR33_PRODUCT(String r33_PRODUCT) { R33_PRODUCT = r33_PRODUCT; }
		public BigDecimal getR33_NV_LONG() { return R33_NV_LONG; }
		public void setR33_NV_LONG(BigDecimal r33_NV_LONG) { R33_NV_LONG = r33_NV_LONG; }
		public BigDecimal getR33_NV_SHORT() { return R33_NV_SHORT; }
		public void setR33_NV_SHORT(BigDecimal r33_NV_SHORT) { R33_NV_SHORT = r33_NV_SHORT; }
		public BigDecimal getR33_FV_LONG() { return R33_FV_LONG; }
		public void setR33_FV_LONG(BigDecimal r33_FV_LONG) { R33_FV_LONG = r33_FV_LONG; }
		public BigDecimal getR33_FV_SHORT() { return R33_FV_SHORT; }
		public void setR33_FV_SHORT(BigDecimal r33_FV_SHORT) { R33_FV_SHORT = r33_FV_SHORT; }
		public BigDecimal getR33_QFHA() { return R33_QFHA; }
		public void setR33_QFHA(BigDecimal r33_QFHA) { R33_QFHA = r33_QFHA; }
		public String getR34_PRODUCT() { return R34_PRODUCT; }
		public void setR34_PRODUCT(String r34_PRODUCT) { R34_PRODUCT = r34_PRODUCT; }
		public BigDecimal getR34_NV_LONG() { return R34_NV_LONG; }
		public void setR34_NV_LONG(BigDecimal r34_NV_LONG) { R34_NV_LONG = r34_NV_LONG; }
		public BigDecimal getR34_NV_SHORT() { return R34_NV_SHORT; }
		public void setR34_NV_SHORT(BigDecimal r34_NV_SHORT) { R34_NV_SHORT = r34_NV_SHORT; }
		public BigDecimal getR34_FV_LONG() { return R34_FV_LONG; }
		public void setR34_FV_LONG(BigDecimal r34_FV_LONG) { R34_FV_LONG = r34_FV_LONG; }
		public BigDecimal getR34_FV_SHORT() { return R34_FV_SHORT; }
		public void setR34_FV_SHORT(BigDecimal r34_FV_SHORT) { R34_FV_SHORT = r34_FV_SHORT; }
		public BigDecimal getR34_QFHA() { return R34_QFHA; }
		public void setR34_QFHA(BigDecimal r34_QFHA) { R34_QFHA = r34_QFHA; }
		public String getR35_PRODUCT() { return R35_PRODUCT; }
		public void setR35_PRODUCT(String r35_PRODUCT) { R35_PRODUCT = r35_PRODUCT; }
		public BigDecimal getR35_NV_LONG() { return R35_NV_LONG; }
		public void setR35_NV_LONG(BigDecimal r35_NV_LONG) { R35_NV_LONG = r35_NV_LONG; }
		public BigDecimal getR35_NV_SHORT() { return R35_NV_SHORT; }
		public void setR35_NV_SHORT(BigDecimal r35_NV_SHORT) { R35_NV_SHORT = r35_NV_SHORT; }
		public BigDecimal getR35_FV_LONG() { return R35_FV_LONG; }
		public void setR35_FV_LONG(BigDecimal r35_FV_LONG) { R35_FV_LONG = r35_FV_LONG; }
		public BigDecimal getR35_FV_SHORT() { return R35_FV_SHORT; }
		public void setR35_FV_SHORT(BigDecimal r35_FV_SHORT) { R35_FV_SHORT = r35_FV_SHORT; }
		public BigDecimal getR35_QFHA() { return R35_QFHA; }
		public void setR35_QFHA(BigDecimal r35_QFHA) { R35_QFHA = r35_QFHA; }
		public String getR36_PRODUCT() { return R36_PRODUCT; }
		public void setR36_PRODUCT(String r36_PRODUCT) { R36_PRODUCT = r36_PRODUCT; }
		public BigDecimal getR36_NV_LONG() { return R36_NV_LONG; }
		public void setR36_NV_LONG(BigDecimal r36_NV_LONG) { R36_NV_LONG = r36_NV_LONG; }
		public BigDecimal getR36_NV_SHORT() { return R36_NV_SHORT; }
		public void setR36_NV_SHORT(BigDecimal r36_NV_SHORT) { R36_NV_SHORT = r36_NV_SHORT; }
		public BigDecimal getR36_FV_LONG() { return R36_FV_LONG; }
		public void setR36_FV_LONG(BigDecimal r36_FV_LONG) { R36_FV_LONG = r36_FV_LONG; }
		public BigDecimal getR36_FV_SHORT() { return R36_FV_SHORT; }
		public void setR36_FV_SHORT(BigDecimal r36_FV_SHORT) { R36_FV_SHORT = r36_FV_SHORT; }
		public BigDecimal getR36_QFHA() { return R36_QFHA; }
		public void setR36_QFHA(BigDecimal r36_QFHA) { R36_QFHA = r36_QFHA; }
		public String getR37_PRODUCT() { return R37_PRODUCT; }
		public void setR37_PRODUCT(String r37_PRODUCT) { R37_PRODUCT = r37_PRODUCT; }
		public BigDecimal getR37_NV_LONG() { return R37_NV_LONG; }
		public void setR37_NV_LONG(BigDecimal r37_NV_LONG) { R37_NV_LONG = r37_NV_LONG; }
		public BigDecimal getR37_NV_SHORT() { return R37_NV_SHORT; }
		public void setR37_NV_SHORT(BigDecimal r37_NV_SHORT) { R37_NV_SHORT = r37_NV_SHORT; }
		public BigDecimal getR37_FV_LONG() { return R37_FV_LONG; }
		public void setR37_FV_LONG(BigDecimal r37_FV_LONG) { R37_FV_LONG = r37_FV_LONG; }
		public BigDecimal getR37_FV_SHORT() { return R37_FV_SHORT; }
		public void setR37_FV_SHORT(BigDecimal r37_FV_SHORT) { R37_FV_SHORT = r37_FV_SHORT; }
		public BigDecimal getR37_QFHA() { return R37_QFHA; }
		public void setR37_QFHA(BigDecimal r37_QFHA) { R37_QFHA = r37_QFHA; }
		public String getR38_PRODUCT() { return R38_PRODUCT; }
		public void setR38_PRODUCT(String r38_PRODUCT) { R38_PRODUCT = r38_PRODUCT; }
		public BigDecimal getR38_NV_LONG() { return R38_NV_LONG; }
		public void setR38_NV_LONG(BigDecimal r38_NV_LONG) { R38_NV_LONG = r38_NV_LONG; }
		public BigDecimal getR38_NV_SHORT() { return R38_NV_SHORT; }
		public void setR38_NV_SHORT(BigDecimal r38_NV_SHORT) { R38_NV_SHORT = r38_NV_SHORT; }
		public BigDecimal getR38_FV_LONG() { return R38_FV_LONG; }
		public void setR38_FV_LONG(BigDecimal r38_FV_LONG) { R38_FV_LONG = r38_FV_LONG; }
		public BigDecimal getR38_FV_SHORT() { return R38_FV_SHORT; }
		public void setR38_FV_SHORT(BigDecimal r38_FV_SHORT) { R38_FV_SHORT = r38_FV_SHORT; }
		public BigDecimal getR38_QFHA() { return R38_QFHA; }
		public void setR38_QFHA(BigDecimal r38_QFHA) { R38_QFHA = r38_QFHA; }
		public String getR39_PRODUCT() { return R39_PRODUCT; }
		public void setR39_PRODUCT(String r39_PRODUCT) { R39_PRODUCT = r39_PRODUCT; }
		public BigDecimal getR39_NV_LONG() { return R39_NV_LONG; }
		public void setR39_NV_LONG(BigDecimal r39_NV_LONG) { R39_NV_LONG = r39_NV_LONG; }
		public BigDecimal getR39_NV_SHORT() { return R39_NV_SHORT; }
		public void setR39_NV_SHORT(BigDecimal r39_NV_SHORT) { R39_NV_SHORT = r39_NV_SHORT; }
		public BigDecimal getR39_FV_LONG() { return R39_FV_LONG; }
		public void setR39_FV_LONG(BigDecimal r39_FV_LONG) { R39_FV_LONG = r39_FV_LONG; }
		public BigDecimal getR39_FV_SHORT() { return R39_FV_SHORT; }
		public void setR39_FV_SHORT(BigDecimal r39_FV_SHORT) { R39_FV_SHORT = r39_FV_SHORT; }
		public BigDecimal getR39_QFHA() { return R39_QFHA; }
		public void setR39_QFHA(BigDecimal r39_QFHA) { R39_QFHA = r39_QFHA; }
		public String getR40_PRODUCT() { return R40_PRODUCT; }
		public void setR40_PRODUCT(String r40_PRODUCT) { R40_PRODUCT = r40_PRODUCT; }
		public BigDecimal getR40_NV_LONG() { return R40_NV_LONG; }
		public void setR40_NV_LONG(BigDecimal r40_NV_LONG) { R40_NV_LONG = r40_NV_LONG; }
		public BigDecimal getR40_NV_SHORT() { return R40_NV_SHORT; }
		public void setR40_NV_SHORT(BigDecimal r40_NV_SHORT) { R40_NV_SHORT = r40_NV_SHORT; }
		public BigDecimal getR40_FV_LONG() { return R40_FV_LONG; }
		public void setR40_FV_LONG(BigDecimal r40_FV_LONG) { R40_FV_LONG = r40_FV_LONG; }
		public BigDecimal getR40_FV_SHORT() { return R40_FV_SHORT; }
		public void setR40_FV_SHORT(BigDecimal r40_FV_SHORT) { R40_FV_SHORT = r40_FV_SHORT; }
		public BigDecimal getR40_QFHA() { return R40_QFHA; }
		public void setR40_QFHA(BigDecimal r40_QFHA) { R40_QFHA = r40_QFHA; }
		public String getR41_PRODUCT() { return R41_PRODUCT; }
		public void setR41_PRODUCT(String r41_PRODUCT) { R41_PRODUCT = r41_PRODUCT; }
		public BigDecimal getR41_NV_LONG() { return R41_NV_LONG; }
		public void setR41_NV_LONG(BigDecimal r41_NV_LONG) { R41_NV_LONG = r41_NV_LONG; }
		public BigDecimal getR41_NV_SHORT() { return R41_NV_SHORT; }
		public void setR41_NV_SHORT(BigDecimal r41_NV_SHORT) { R41_NV_SHORT = r41_NV_SHORT; }
		public BigDecimal getR41_FV_LONG() { return R41_FV_LONG; }
		public void setR41_FV_LONG(BigDecimal r41_FV_LONG) { R41_FV_LONG = r41_FV_LONG; }
		public BigDecimal getR41_FV_SHORT() { return R41_FV_SHORT; }
		public void setR41_FV_SHORT(BigDecimal r41_FV_SHORT) { R41_FV_SHORT = r41_FV_SHORT; }
		public BigDecimal getR41_QFHA() { return R41_QFHA; }
		public void setR41_QFHA(BigDecimal r41_QFHA) { R41_QFHA = r41_QFHA; }
		public String getR42_PRODUCT() { return R42_PRODUCT; }
		public void setR42_PRODUCT(String r42_PRODUCT) { R42_PRODUCT = r42_PRODUCT; }
		public BigDecimal getR42_NV_LONG() { return R42_NV_LONG; }
		public void setR42_NV_LONG(BigDecimal r42_NV_LONG) { R42_NV_LONG = r42_NV_LONG; }
		public BigDecimal getR42_NV_SHORT() { return R42_NV_SHORT; }
		public void setR42_NV_SHORT(BigDecimal r42_NV_SHORT) { R42_NV_SHORT = r42_NV_SHORT; }
		public BigDecimal getR42_FV_LONG() { return R42_FV_LONG; }
		public void setR42_FV_LONG(BigDecimal r42_FV_LONG) { R42_FV_LONG = r42_FV_LONG; }
		public BigDecimal getR42_FV_SHORT() { return R42_FV_SHORT; }
		public void setR42_FV_SHORT(BigDecimal r42_FV_SHORT) { R42_FV_SHORT = r42_FV_SHORT; }
		public BigDecimal getR42_QFHA() { return R42_QFHA; }
		public void setR42_QFHA(BigDecimal r42_QFHA) { R42_QFHA = r42_QFHA; }
		public String getR43_PRODUCT() { return R43_PRODUCT; }
		public void setR43_PRODUCT(String r43_PRODUCT) { R43_PRODUCT = r43_PRODUCT; }
		public BigDecimal getR43_NV_LONG() { return R43_NV_LONG; }
		public void setR43_NV_LONG(BigDecimal r43_NV_LONG) { R43_NV_LONG = r43_NV_LONG; }
		public BigDecimal getR43_NV_SHORT() { return R43_NV_SHORT; }
		public void setR43_NV_SHORT(BigDecimal r43_NV_SHORT) { R43_NV_SHORT = r43_NV_SHORT; }
		public BigDecimal getR43_FV_LONG() { return R43_FV_LONG; }
		public void setR43_FV_LONG(BigDecimal r43_FV_LONG) { R43_FV_LONG = r43_FV_LONG; }
		public BigDecimal getR43_FV_SHORT() { return R43_FV_SHORT; }
		public void setR43_FV_SHORT(BigDecimal r43_FV_SHORT) { R43_FV_SHORT = r43_FV_SHORT; }
		public BigDecimal getR43_QFHA() { return R43_QFHA; }
		public void setR43_QFHA(BigDecimal r43_QFHA) { R43_QFHA = r43_QFHA; }
		public String getR44_PRODUCT() { return R44_PRODUCT; }
		public void setR44_PRODUCT(String r44_PRODUCT) { R44_PRODUCT = r44_PRODUCT; }
		public BigDecimal getR44_NV_LONG() { return R44_NV_LONG; }
		public void setR44_NV_LONG(BigDecimal r44_NV_LONG) { R44_NV_LONG = r44_NV_LONG; }
		public BigDecimal getR44_NV_SHORT() { return R44_NV_SHORT; }
		public void setR44_NV_SHORT(BigDecimal r44_NV_SHORT) { R44_NV_SHORT = r44_NV_SHORT; }
		public BigDecimal getR44_FV_LONG() { return R44_FV_LONG; }
		public void setR44_FV_LONG(BigDecimal r44_FV_LONG) { R44_FV_LONG = r44_FV_LONG; }
		public BigDecimal getR44_FV_SHORT() { return R44_FV_SHORT; }
		public void setR44_FV_SHORT(BigDecimal r44_FV_SHORT) { R44_FV_SHORT = r44_FV_SHORT; }
		public BigDecimal getR44_QFHA() { return R44_QFHA; }
		public void setR44_QFHA(BigDecimal r44_QFHA) { R44_QFHA = r44_QFHA; }
		public String getR45_PRODUCT() { return R45_PRODUCT; }
		public void setR45_PRODUCT(String r45_PRODUCT) { R45_PRODUCT = r45_PRODUCT; }
		public BigDecimal getR45_NV_LONG() { return R45_NV_LONG; }
		public void setR45_NV_LONG(BigDecimal r45_NV_LONG) { R45_NV_LONG = r45_NV_LONG; }
		public BigDecimal getR45_NV_SHORT() { return R45_NV_SHORT; }
		public void setR45_NV_SHORT(BigDecimal r45_NV_SHORT) { R45_NV_SHORT = r45_NV_SHORT; }
		public BigDecimal getR45_FV_LONG() { return R45_FV_LONG; }
		public void setR45_FV_LONG(BigDecimal r45_FV_LONG) { R45_FV_LONG = r45_FV_LONG; }
		public BigDecimal getR45_FV_SHORT() { return R45_FV_SHORT; }
		public void setR45_FV_SHORT(BigDecimal r45_FV_SHORT) { R45_FV_SHORT = r45_FV_SHORT; }
		public BigDecimal getR45_QFHA() { return R45_QFHA; }
		public void setR45_QFHA(BigDecimal r45_QFHA) { R45_QFHA = r45_QFHA; }
		public String getR46_PRODUCT() { return R46_PRODUCT; }
		public void setR46_PRODUCT(String r46_PRODUCT) { R46_PRODUCT = r46_PRODUCT; }
		public BigDecimal getR46_NV_LONG() { return R46_NV_LONG; }
		public void setR46_NV_LONG(BigDecimal r46_NV_LONG) { R46_NV_LONG = r46_NV_LONG; }
		public BigDecimal getR46_NV_SHORT() { return R46_NV_SHORT; }
		public void setR46_NV_SHORT(BigDecimal r46_NV_SHORT) { R46_NV_SHORT = r46_NV_SHORT; }
		public BigDecimal getR46_FV_LONG() { return R46_FV_LONG; }
		public void setR46_FV_LONG(BigDecimal r46_FV_LONG) { R46_FV_LONG = r46_FV_LONG; }
		public BigDecimal getR46_FV_SHORT() { return R46_FV_SHORT; }
		public void setR46_FV_SHORT(BigDecimal r46_FV_SHORT) { R46_FV_SHORT = r46_FV_SHORT; }
		public BigDecimal getR46_QFHA() { return R46_QFHA; }
		public void setR46_QFHA(BigDecimal r46_QFHA) { R46_QFHA = r46_QFHA; }
		public String getR47_PRODUCT() { return R47_PRODUCT; }
		public void setR47_PRODUCT(String r47_PRODUCT) { R47_PRODUCT = r47_PRODUCT; }
		public BigDecimal getR47_NV_LONG() { return R47_NV_LONG; }
		public void setR47_NV_LONG(BigDecimal r47_NV_LONG) { R47_NV_LONG = r47_NV_LONG; }
		public BigDecimal getR47_NV_SHORT() { return R47_NV_SHORT; }
		public void setR47_NV_SHORT(BigDecimal r47_NV_SHORT) { R47_NV_SHORT = r47_NV_SHORT; }
		public BigDecimal getR47_FV_LONG() { return R47_FV_LONG; }
		public void setR47_FV_LONG(BigDecimal r47_FV_LONG) { R47_FV_LONG = r47_FV_LONG; }
		public BigDecimal getR47_FV_SHORT() { return R47_FV_SHORT; }
		public void setR47_FV_SHORT(BigDecimal r47_FV_SHORT) { R47_FV_SHORT = r47_FV_SHORT; }
		public BigDecimal getR47_QFHA() { return R47_QFHA; }
		public void setR47_QFHA(BigDecimal r47_QFHA) { R47_QFHA = r47_QFHA; }
		public String getR48_PRODUCT() { return R48_PRODUCT; }
		public void setR48_PRODUCT(String r48_PRODUCT) { R48_PRODUCT = r48_PRODUCT; }
		public BigDecimal getR48_NV_LONG() { return R48_NV_LONG; }
		public void setR48_NV_LONG(BigDecimal r48_NV_LONG) { R48_NV_LONG = r48_NV_LONG; }
		public BigDecimal getR48_NV_SHORT() { return R48_NV_SHORT; }
		public void setR48_NV_SHORT(BigDecimal r48_NV_SHORT) { R48_NV_SHORT = r48_NV_SHORT; }
		public BigDecimal getR48_FV_LONG() { return R48_FV_LONG; }
		public void setR48_FV_LONG(BigDecimal r48_FV_LONG) { R48_FV_LONG = r48_FV_LONG; }
		public BigDecimal getR48_FV_SHORT() { return R48_FV_SHORT; }
		public void setR48_FV_SHORT(BigDecimal r48_FV_SHORT) { R48_FV_SHORT = r48_FV_SHORT; }
		public BigDecimal getR48_QFHA() { return R48_QFHA; }
		public void setR48_QFHA(BigDecimal r48_QFHA) { R48_QFHA = r48_QFHA; }
		public String getR49_PRODUCT() { return R49_PRODUCT; }
		public void setR49_PRODUCT(String r49_PRODUCT) { R49_PRODUCT = r49_PRODUCT; }
		public BigDecimal getR49_NV_LONG() { return R49_NV_LONG; }
		public void setR49_NV_LONG(BigDecimal r49_NV_LONG) { R49_NV_LONG = r49_NV_LONG; }
		public BigDecimal getR49_NV_SHORT() { return R49_NV_SHORT; }
		public void setR49_NV_SHORT(BigDecimal r49_NV_SHORT) { R49_NV_SHORT = r49_NV_SHORT; }
		public BigDecimal getR49_FV_LONG() { return R49_FV_LONG; }
		public void setR49_FV_LONG(BigDecimal r49_FV_LONG) { R49_FV_LONG = r49_FV_LONG; }
		public BigDecimal getR49_FV_SHORT() { return R49_FV_SHORT; }
		public void setR49_FV_SHORT(BigDecimal r49_FV_SHORT) { R49_FV_SHORT = r49_FV_SHORT; }
		public BigDecimal getR49_QFHA() { return R49_QFHA; }
		public void setR49_QFHA(BigDecimal r49_QFHA) { R49_QFHA = r49_QFHA; }
		public String getR50_PRODUCT() { return R50_PRODUCT; }
		public void setR50_PRODUCT(String r50_PRODUCT) { R50_PRODUCT = r50_PRODUCT; }
		public BigDecimal getR50_NV_LONG() { return R50_NV_LONG; }
		public void setR50_NV_LONG(BigDecimal r50_NV_LONG) { R50_NV_LONG = r50_NV_LONG; }
		public BigDecimal getR50_NV_SHORT() { return R50_NV_SHORT; }
		public void setR50_NV_SHORT(BigDecimal r50_NV_SHORT) { R50_NV_SHORT = r50_NV_SHORT; }
		public BigDecimal getR50_FV_LONG() { return R50_FV_LONG; }
		public void setR50_FV_LONG(BigDecimal r50_FV_LONG) { R50_FV_LONG = r50_FV_LONG; }
		public BigDecimal getR50_FV_SHORT() { return R50_FV_SHORT; }
		public void setR50_FV_SHORT(BigDecimal r50_FV_SHORT) { R50_FV_SHORT = r50_FV_SHORT; }
		public BigDecimal getR50_QFHA() { return R50_QFHA; }
		public void setR50_QFHA(BigDecimal r50_QFHA) { R50_QFHA = r50_QFHA; }
		public String getR51_PRODUCT() { return R51_PRODUCT; }
		public void setR51_PRODUCT(String r51_PRODUCT) { R51_PRODUCT = r51_PRODUCT; }
		public BigDecimal getR51_NV_LONG() { return R51_NV_LONG; }
		public void setR51_NV_LONG(BigDecimal r51_NV_LONG) { R51_NV_LONG = r51_NV_LONG; }
		public BigDecimal getR51_NV_SHORT() { return R51_NV_SHORT; }
		public void setR51_NV_SHORT(BigDecimal r51_NV_SHORT) { R51_NV_SHORT = r51_NV_SHORT; }
		public BigDecimal getR51_FV_LONG() { return R51_FV_LONG; }
		public void setR51_FV_LONG(BigDecimal r51_FV_LONG) { R51_FV_LONG = r51_FV_LONG; }
		public BigDecimal getR51_FV_SHORT() { return R51_FV_SHORT; }
		public void setR51_FV_SHORT(BigDecimal r51_FV_SHORT) { R51_FV_SHORT = r51_FV_SHORT; }
		public BigDecimal getR51_QFHA() { return R51_QFHA; }
		public void setR51_QFHA(BigDecimal r51_QFHA) { R51_QFHA = r51_QFHA; }
		public String getR52_PRODUCT() { return R52_PRODUCT; }
		public void setR52_PRODUCT(String r52_PRODUCT) { R52_PRODUCT = r52_PRODUCT; }
		public BigDecimal getR52_NV_LONG() { return R52_NV_LONG; }
		public void setR52_NV_LONG(BigDecimal r52_NV_LONG) { R52_NV_LONG = r52_NV_LONG; }
		public BigDecimal getR52_NV_SHORT() { return R52_NV_SHORT; }
		public void setR52_NV_SHORT(BigDecimal r52_NV_SHORT) { R52_NV_SHORT = r52_NV_SHORT; }
		public BigDecimal getR52_FV_LONG() { return R52_FV_LONG; }
		public void setR52_FV_LONG(BigDecimal r52_FV_LONG) { R52_FV_LONG = r52_FV_LONG; }
		public BigDecimal getR52_FV_SHORT() { return R52_FV_SHORT; }
		public void setR52_FV_SHORT(BigDecimal r52_FV_SHORT) { R52_FV_SHORT = r52_FV_SHORT; }
		public BigDecimal getR52_QFHA() { return R52_QFHA; }
		public void setR52_QFHA(BigDecimal r52_QFHA) { R52_QFHA = r52_QFHA; }
		public String getR53_PRODUCT() { return R53_PRODUCT; }
		public void setR53_PRODUCT(String r53_PRODUCT) { R53_PRODUCT = r53_PRODUCT; }
		public BigDecimal getR53_NV_LONG() { return R53_NV_LONG; }
		public void setR53_NV_LONG(BigDecimal r53_NV_LONG) { R53_NV_LONG = r53_NV_LONG; }
		public BigDecimal getR53_NV_SHORT() { return R53_NV_SHORT; }
		public void setR53_NV_SHORT(BigDecimal r53_NV_SHORT) { R53_NV_SHORT = r53_NV_SHORT; }
		public BigDecimal getR53_FV_LONG() { return R53_FV_LONG; }
		public void setR53_FV_LONG(BigDecimal r53_FV_LONG) { R53_FV_LONG = r53_FV_LONG; }
		public BigDecimal getR53_FV_SHORT() { return R53_FV_SHORT; }
		public void setR53_FV_SHORT(BigDecimal r53_FV_SHORT) { R53_FV_SHORT = r53_FV_SHORT; }
		public BigDecimal getR53_QFHA() { return R53_QFHA; }
		public void setR53_QFHA(BigDecimal r53_QFHA) { R53_QFHA = r53_QFHA; }
		public String getR54_PRODUCT() { return R54_PRODUCT; }
		public void setR54_PRODUCT(String r54_PRODUCT) { R54_PRODUCT = r54_PRODUCT; }
		public BigDecimal getR54_NV_LONG() { return R54_NV_LONG; }
		public void setR54_NV_LONG(BigDecimal r54_NV_LONG) { R54_NV_LONG = r54_NV_LONG; }
		public BigDecimal getR54_NV_SHORT() { return R54_NV_SHORT; }
		public void setR54_NV_SHORT(BigDecimal r54_NV_SHORT) { R54_NV_SHORT = r54_NV_SHORT; }
		public BigDecimal getR54_FV_LONG() { return R54_FV_LONG; }
		public void setR54_FV_LONG(BigDecimal r54_FV_LONG) { R54_FV_LONG = r54_FV_LONG; }
		public BigDecimal getR54_FV_SHORT() { return R54_FV_SHORT; }
		public void setR54_FV_SHORT(BigDecimal r54_FV_SHORT) { R54_FV_SHORT = r54_FV_SHORT; }
		public BigDecimal getR54_QFHA() { return R54_QFHA; }
		public void setR54_QFHA(BigDecimal r54_QFHA) { R54_QFHA = r54_QFHA; }
		public String getR55_PRODUCT() { return R55_PRODUCT; }
		public void setR55_PRODUCT(String r55_PRODUCT) { R55_PRODUCT = r55_PRODUCT; }
		public BigDecimal getR55_NV_LONG() { return R55_NV_LONG; }
		public void setR55_NV_LONG(BigDecimal r55_NV_LONG) { R55_NV_LONG = r55_NV_LONG; }
		public BigDecimal getR55_NV_SHORT() { return R55_NV_SHORT; }
		public void setR55_NV_SHORT(BigDecimal r55_NV_SHORT) { R55_NV_SHORT = r55_NV_SHORT; }
		public BigDecimal getR55_FV_LONG() { return R55_FV_LONG; }
		public void setR55_FV_LONG(BigDecimal r55_FV_LONG) { R55_FV_LONG = r55_FV_LONG; }
		public BigDecimal getR55_FV_SHORT() { return R55_FV_SHORT; }
		public void setR55_FV_SHORT(BigDecimal r55_FV_SHORT) { R55_FV_SHORT = r55_FV_SHORT; }
		public BigDecimal getR55_QFHA() { return R55_QFHA; }
		public void setR55_QFHA(BigDecimal r55_QFHA) { R55_QFHA = r55_QFHA; }
		public Date getReportDate() { return reportDate; }
		public void setReportDate(Date reportDate) { this.reportDate = reportDate; }
		public BigDecimal getReportVersion() { return reportVersion; }
		public void setReportVersion(BigDecimal reportVersion) { this.reportVersion = reportVersion; }
		public String getREPORT_FREQUENCY() { return REPORT_FREQUENCY; }
		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) { REPORT_FREQUENCY = rEPORT_FREQUENCY; }
		public String getREPORT_CODE() { return REPORT_CODE; }
		public void setREPORT_CODE(String rEPORT_CODE) { REPORT_CODE = rEPORT_CODE; }
		public String getREPORT_DESC() { return REPORT_DESC; }
		public void setREPORT_DESC(String rEPORT_DESC) { REPORT_DESC = rEPORT_DESC; }
		public String getENTITY_FLG() { return ENTITY_FLG; }
		public void setENTITY_FLG(String eNTITY_FLG) { ENTITY_FLG = eNTITY_FLG; }
		public String getMODIFY_FLG() { return MODIFY_FLG; }
		public void setMODIFY_FLG(String mODIFY_FLG) { MODIFY_FLG = mODIFY_FLG; }
		public String getDEL_FLG() { return DEL_FLG; }
		public void setDEL_FLG(String dEL_FLG) { DEL_FLG = dEL_FLG; }
		public Date getReportResubDate() { return reportResubDate; }
		public void setReportResubDate(Date reportResubDate) { this.reportResubDate = reportResubDate; }
	}

	// =========================================================
	// Inner entity: M_TBS_Resub_Summary_Entity
	// =========================================================
	public static class M_TBS_Resub_Summary_Entity {
		private String R11_PRODUCT;
		private BigDecimal R11_NV_LONG;
		private BigDecimal R11_NV_SHORT;
		private BigDecimal R11_FV_LONG;
		private BigDecimal R11_FV_SHORT;
		private BigDecimal R11_QFHA;
		private String R12_PRODUCT;
		private BigDecimal R12_NV_LONG;
		private BigDecimal R12_NV_SHORT;
		private BigDecimal R12_FV_LONG;
		private BigDecimal R12_FV_SHORT;
		private BigDecimal R12_QFHA;
		private String R13_PRODUCT;
		private BigDecimal R13_NV_LONG;
		private BigDecimal R13_NV_SHORT;
		private BigDecimal R13_FV_LONG;
		private BigDecimal R13_FV_SHORT;
		private BigDecimal R13_QFHA;
		private String R14_PRODUCT;
		private BigDecimal R14_NV_LONG;
		private BigDecimal R14_NV_SHORT;
		private BigDecimal R14_FV_LONG;
		private BigDecimal R14_FV_SHORT;
		private BigDecimal R14_QFHA;
		private String R15_PRODUCT;
		private BigDecimal R15_NV_LONG;
		private BigDecimal R15_NV_SHORT;
		private BigDecimal R15_FV_LONG;
		private BigDecimal R15_FV_SHORT;
		private BigDecimal R15_QFHA;
		private String R16_PRODUCT;
		private BigDecimal R16_NV_LONG;
		private BigDecimal R16_NV_SHORT;
		private BigDecimal R16_FV_LONG;
		private BigDecimal R16_FV_SHORT;
		private BigDecimal R16_QFHA;
		private String R17_PRODUCT;
		private BigDecimal R17_NV_LONG;
		private BigDecimal R17_NV_SHORT;
		private BigDecimal R17_FV_LONG;
		private BigDecimal R17_FV_SHORT;
		private BigDecimal R17_QFHA;
		private String R18_PRODUCT;
		private BigDecimal R18_NV_LONG;
		private BigDecimal R18_NV_SHORT;
		private BigDecimal R18_FV_LONG;
		private BigDecimal R18_FV_SHORT;
		private BigDecimal R18_QFHA;
		private String R19_PRODUCT;
		private BigDecimal R19_NV_LONG;
		private BigDecimal R19_NV_SHORT;
		private BigDecimal R19_FV_LONG;
		private BigDecimal R19_FV_SHORT;
		private BigDecimal R19_QFHA;
		private String R20_PRODUCT;
		private BigDecimal R20_NV_LONG;
		private BigDecimal R20_NV_SHORT;
		private BigDecimal R20_FV_LONG;
		private BigDecimal R20_FV_SHORT;
		private BigDecimal R20_QFHA;
		private String R21_PRODUCT;
		private BigDecimal R21_NV_LONG;
		private BigDecimal R21_NV_SHORT;
		private BigDecimal R21_FV_LONG;
		private BigDecimal R21_FV_SHORT;
		private BigDecimal R21_QFHA;
		private String R22_PRODUCT;
		private BigDecimal R22_NV_LONG;
		private BigDecimal R22_NV_SHORT;
		private BigDecimal R22_FV_LONG;
		private BigDecimal R22_FV_SHORT;
		private BigDecimal R22_QFHA;
		private String R23_PRODUCT;
		private BigDecimal R23_NV_LONG;
		private BigDecimal R23_NV_SHORT;
		private BigDecimal R23_FV_LONG;
		private BigDecimal R23_FV_SHORT;
		private BigDecimal R23_QFHA;
		private String R24_PRODUCT;
		private BigDecimal R24_NV_LONG;
		private BigDecimal R24_NV_SHORT;
		private BigDecimal R24_FV_LONG;
		private BigDecimal R24_FV_SHORT;
		private BigDecimal R24_QFHA;
		private String R25_PRODUCT;
		private BigDecimal R25_NV_LONG;
		private BigDecimal R25_NV_SHORT;
		private BigDecimal R25_FV_LONG;
		private BigDecimal R25_FV_SHORT;
		private BigDecimal R25_QFHA;
		private String R26_PRODUCT;
		private BigDecimal R26_NV_LONG;
		private BigDecimal R26_NV_SHORT;
		private BigDecimal R26_FV_LONG;
		private BigDecimal R26_FV_SHORT;
		private BigDecimal R26_QFHA;
		private String R27_PRODUCT;
		private BigDecimal R27_NV_LONG;
		private BigDecimal R27_NV_SHORT;
		private BigDecimal R27_FV_LONG;
		private BigDecimal R27_FV_SHORT;
		private BigDecimal R27_QFHA;
		private String R28_PRODUCT;
		private BigDecimal R28_NV_LONG;
		private BigDecimal R28_NV_SHORT;
		private BigDecimal R28_FV_LONG;
		private BigDecimal R28_FV_SHORT;
		private BigDecimal R28_QFHA;
		private String R29_PRODUCT;
		private BigDecimal R29_NV_LONG;
		private BigDecimal R29_NV_SHORT;
		private BigDecimal R29_FV_LONG;
		private BigDecimal R29_FV_SHORT;
		private BigDecimal R29_QFHA;
		private String R30_PRODUCT;
		private BigDecimal R30_NV_LONG;
		private BigDecimal R30_NV_SHORT;
		private BigDecimal R30_FV_LONG;
		private BigDecimal R30_FV_SHORT;
		private BigDecimal R30_QFHA;
		private String R31_PRODUCT;
		private BigDecimal R31_NV_LONG;
		private BigDecimal R31_NV_SHORT;
		private BigDecimal R31_FV_LONG;
		private BigDecimal R31_FV_SHORT;
		private BigDecimal R31_QFHA;
		private String R32_PRODUCT;
		private BigDecimal R32_NV_LONG;
		private BigDecimal R32_NV_SHORT;
		private BigDecimal R32_FV_LONG;
		private BigDecimal R32_FV_SHORT;
		private BigDecimal R32_QFHA;
		private String R33_PRODUCT;
		private BigDecimal R33_NV_LONG;
		private BigDecimal R33_NV_SHORT;
		private BigDecimal R33_FV_LONG;
		private BigDecimal R33_FV_SHORT;
		private BigDecimal R33_QFHA;
		private String R34_PRODUCT;
		private BigDecimal R34_NV_LONG;
		private BigDecimal R34_NV_SHORT;
		private BigDecimal R34_FV_LONG;
		private BigDecimal R34_FV_SHORT;
		private BigDecimal R34_QFHA;
		private String R35_PRODUCT;
		private BigDecimal R35_NV_LONG;
		private BigDecimal R35_NV_SHORT;
		private BigDecimal R35_FV_LONG;
		private BigDecimal R35_FV_SHORT;
		private BigDecimal R35_QFHA;
		private String R36_PRODUCT;
		private BigDecimal R36_NV_LONG;
		private BigDecimal R36_NV_SHORT;
		private BigDecimal R36_FV_LONG;
		private BigDecimal R36_FV_SHORT;
		private BigDecimal R36_QFHA;
		private String R37_PRODUCT;
		private BigDecimal R37_NV_LONG;
		private BigDecimal R37_NV_SHORT;
		private BigDecimal R37_FV_LONG;
		private BigDecimal R37_FV_SHORT;
		private BigDecimal R37_QFHA;
		private String R38_PRODUCT;
		private BigDecimal R38_NV_LONG;
		private BigDecimal R38_NV_SHORT;
		private BigDecimal R38_FV_LONG;
		private BigDecimal R38_FV_SHORT;
		private BigDecimal R38_QFHA;
		private String R39_PRODUCT;
		private BigDecimal R39_NV_LONG;
		private BigDecimal R39_NV_SHORT;
		private BigDecimal R39_FV_LONG;
		private BigDecimal R39_FV_SHORT;
		private BigDecimal R39_QFHA;
		private String R40_PRODUCT;
		private BigDecimal R40_NV_LONG;
		private BigDecimal R40_NV_SHORT;
		private BigDecimal R40_FV_LONG;
		private BigDecimal R40_FV_SHORT;
		private BigDecimal R40_QFHA;
		private String R41_PRODUCT;
		private BigDecimal R41_NV_LONG;
		private BigDecimal R41_NV_SHORT;
		private BigDecimal R41_FV_LONG;
		private BigDecimal R41_FV_SHORT;
		private BigDecimal R41_QFHA;
		private String R42_PRODUCT;
		private BigDecimal R42_NV_LONG;
		private BigDecimal R42_NV_SHORT;
		private BigDecimal R42_FV_LONG;
		private BigDecimal R42_FV_SHORT;
		private BigDecimal R42_QFHA;
		private String R43_PRODUCT;
		private BigDecimal R43_NV_LONG;
		private BigDecimal R43_NV_SHORT;
		private BigDecimal R43_FV_LONG;
		private BigDecimal R43_FV_SHORT;
		private BigDecimal R43_QFHA;
		private String R44_PRODUCT;
		private BigDecimal R44_NV_LONG;
		private BigDecimal R44_NV_SHORT;
		private BigDecimal R44_FV_LONG;
		private BigDecimal R44_FV_SHORT;
		private BigDecimal R44_QFHA;
		private String R45_PRODUCT;
		private BigDecimal R45_NV_LONG;
		private BigDecimal R45_NV_SHORT;
		private BigDecimal R45_FV_LONG;
		private BigDecimal R45_FV_SHORT;
		private BigDecimal R45_QFHA;
		private String R46_PRODUCT;
		private BigDecimal R46_NV_LONG;
		private BigDecimal R46_NV_SHORT;
		private BigDecimal R46_FV_LONG;
		private BigDecimal R46_FV_SHORT;
		private BigDecimal R46_QFHA;
		private String R47_PRODUCT;
		private BigDecimal R47_NV_LONG;
		private BigDecimal R47_NV_SHORT;
		private BigDecimal R47_FV_LONG;
		private BigDecimal R47_FV_SHORT;
		private BigDecimal R47_QFHA;
		private String R48_PRODUCT;
		private BigDecimal R48_NV_LONG;
		private BigDecimal R48_NV_SHORT;
		private BigDecimal R48_FV_LONG;
		private BigDecimal R48_FV_SHORT;
		private BigDecimal R48_QFHA;
		private String R49_PRODUCT;
		private BigDecimal R49_NV_LONG;
		private BigDecimal R49_NV_SHORT;
		private BigDecimal R49_FV_LONG;
		private BigDecimal R49_FV_SHORT;
		private BigDecimal R49_QFHA;
		private String R50_PRODUCT;
		private BigDecimal R50_NV_LONG;
		private BigDecimal R50_NV_SHORT;
		private BigDecimal R50_FV_LONG;
		private BigDecimal R50_FV_SHORT;
		private BigDecimal R50_QFHA;
		private String R51_PRODUCT;
		private BigDecimal R51_NV_LONG;
		private BigDecimal R51_NV_SHORT;
		private BigDecimal R51_FV_LONG;
		private BigDecimal R51_FV_SHORT;
		private BigDecimal R51_QFHA;
		private String R52_PRODUCT;
		private BigDecimal R52_NV_LONG;
		private BigDecimal R52_NV_SHORT;
		private BigDecimal R52_FV_LONG;
		private BigDecimal R52_FV_SHORT;
		private BigDecimal R52_QFHA;
		private String R53_PRODUCT;
		private BigDecimal R53_NV_LONG;
		private BigDecimal R53_NV_SHORT;
		private BigDecimal R53_FV_LONG;
		private BigDecimal R53_FV_SHORT;
		private BigDecimal R53_QFHA;
		private String R54_PRODUCT;
		private BigDecimal R54_NV_LONG;
		private BigDecimal R54_NV_SHORT;
		private BigDecimal R54_FV_LONG;
		private BigDecimal R54_FV_SHORT;
		private BigDecimal R54_QFHA;
		private String R55_PRODUCT;
		private BigDecimal R55_NV_LONG;
		private BigDecimal R55_NV_SHORT;
		private BigDecimal R55_FV_LONG;
		private BigDecimal R55_FV_SHORT;
		private BigDecimal R55_QFHA;

		private Date reportDate;
		private BigDecimal reportVersion;
		public String REPORT_FREQUENCY;
		public String REPORT_CODE;
		public String REPORT_DESC;
		public String ENTITY_FLG;
		public String MODIFY_FLG;
		public String DEL_FLG;
		private Date reportResubDate;

		public String getR11_PRODUCT() { return R11_PRODUCT; }
		public void setR11_PRODUCT(String r11_PRODUCT) { R11_PRODUCT = r11_PRODUCT; }
		public BigDecimal getR11_NV_LONG() { return R11_NV_LONG; }
		public void setR11_NV_LONG(BigDecimal r11_NV_LONG) { R11_NV_LONG = r11_NV_LONG; }
		public BigDecimal getR11_NV_SHORT() { return R11_NV_SHORT; }
		public void setR11_NV_SHORT(BigDecimal r11_NV_SHORT) { R11_NV_SHORT = r11_NV_SHORT; }
		public BigDecimal getR11_FV_LONG() { return R11_FV_LONG; }
		public void setR11_FV_LONG(BigDecimal r11_FV_LONG) { R11_FV_LONG = r11_FV_LONG; }
		public BigDecimal getR11_FV_SHORT() { return R11_FV_SHORT; }
		public void setR11_FV_SHORT(BigDecimal r11_FV_SHORT) { R11_FV_SHORT = r11_FV_SHORT; }
		public BigDecimal getR11_QFHA() { return R11_QFHA; }
		public void setR11_QFHA(BigDecimal r11_QFHA) { R11_QFHA = r11_QFHA; }
		public String getR12_PRODUCT() { return R12_PRODUCT; }
		public void setR12_PRODUCT(String r12_PRODUCT) { R12_PRODUCT = r12_PRODUCT; }
		public BigDecimal getR12_NV_LONG() { return R12_NV_LONG; }
		public void setR12_NV_LONG(BigDecimal r12_NV_LONG) { R12_NV_LONG = r12_NV_LONG; }
		public BigDecimal getR12_NV_SHORT() { return R12_NV_SHORT; }
		public void setR12_NV_SHORT(BigDecimal r12_NV_SHORT) { R12_NV_SHORT = r12_NV_SHORT; }
		public BigDecimal getR12_FV_LONG() { return R12_FV_LONG; }
		public void setR12_FV_LONG(BigDecimal r12_FV_LONG) { R12_FV_LONG = r12_FV_LONG; }
		public BigDecimal getR12_FV_SHORT() { return R12_FV_SHORT; }
		public void setR12_FV_SHORT(BigDecimal r12_FV_SHORT) { R12_FV_SHORT = r12_FV_SHORT; }
		public BigDecimal getR12_QFHA() { return R12_QFHA; }
		public void setR12_QFHA(BigDecimal r12_QFHA) { R12_QFHA = r12_QFHA; }
		public String getR13_PRODUCT() { return R13_PRODUCT; }
		public void setR13_PRODUCT(String r13_PRODUCT) { R13_PRODUCT = r13_PRODUCT; }
		public BigDecimal getR13_NV_LONG() { return R13_NV_LONG; }
		public void setR13_NV_LONG(BigDecimal r13_NV_LONG) { R13_NV_LONG = r13_NV_LONG; }
		public BigDecimal getR13_NV_SHORT() { return R13_NV_SHORT; }
		public void setR13_NV_SHORT(BigDecimal r13_NV_SHORT) { R13_NV_SHORT = r13_NV_SHORT; }
		public BigDecimal getR13_FV_LONG() { return R13_FV_LONG; }
		public void setR13_FV_LONG(BigDecimal r13_FV_LONG) { R13_FV_LONG = r13_FV_LONG; }
		public BigDecimal getR13_FV_SHORT() { return R13_FV_SHORT; }
		public void setR13_FV_SHORT(BigDecimal r13_FV_SHORT) { R13_FV_SHORT = r13_FV_SHORT; }
		public BigDecimal getR13_QFHA() { return R13_QFHA; }
		public void setR13_QFHA(BigDecimal r13_QFHA) { R13_QFHA = r13_QFHA; }
		public String getR14_PRODUCT() { return R14_PRODUCT; }
		public void setR14_PRODUCT(String r14_PRODUCT) { R14_PRODUCT = r14_PRODUCT; }
		public BigDecimal getR14_NV_LONG() { return R14_NV_LONG; }
		public void setR14_NV_LONG(BigDecimal r14_NV_LONG) { R14_NV_LONG = r14_NV_LONG; }
		public BigDecimal getR14_NV_SHORT() { return R14_NV_SHORT; }
		public void setR14_NV_SHORT(BigDecimal r14_NV_SHORT) { R14_NV_SHORT = r14_NV_SHORT; }
		public BigDecimal getR14_FV_LONG() { return R14_FV_LONG; }
		public void setR14_FV_LONG(BigDecimal r14_FV_LONG) { R14_FV_LONG = r14_FV_LONG; }
		public BigDecimal getR14_FV_SHORT() { return R14_FV_SHORT; }
		public void setR14_FV_SHORT(BigDecimal r14_FV_SHORT) { R14_FV_SHORT = r14_FV_SHORT; }
		public BigDecimal getR14_QFHA() { return R14_QFHA; }
		public void setR14_QFHA(BigDecimal r14_QFHA) { R14_QFHA = r14_QFHA; }
		public String getR15_PRODUCT() { return R15_PRODUCT; }
		public void setR15_PRODUCT(String r15_PRODUCT) { R15_PRODUCT = r15_PRODUCT; }
		public BigDecimal getR15_NV_LONG() { return R15_NV_LONG; }
		public void setR15_NV_LONG(BigDecimal r15_NV_LONG) { R15_NV_LONG = r15_NV_LONG; }
		public BigDecimal getR15_NV_SHORT() { return R15_NV_SHORT; }
		public void setR15_NV_SHORT(BigDecimal r15_NV_SHORT) { R15_NV_SHORT = r15_NV_SHORT; }
		public BigDecimal getR15_FV_LONG() { return R15_FV_LONG; }
		public void setR15_FV_LONG(BigDecimal r15_FV_LONG) { R15_FV_LONG = r15_FV_LONG; }
		public BigDecimal getR15_FV_SHORT() { return R15_FV_SHORT; }
		public void setR15_FV_SHORT(BigDecimal r15_FV_SHORT) { R15_FV_SHORT = r15_FV_SHORT; }
		public BigDecimal getR15_QFHA() { return R15_QFHA; }
		public void setR15_QFHA(BigDecimal r15_QFHA) { R15_QFHA = r15_QFHA; }
		public String getR16_PRODUCT() { return R16_PRODUCT; }
		public void setR16_PRODUCT(String r16_PRODUCT) { R16_PRODUCT = r16_PRODUCT; }
		public BigDecimal getR16_NV_LONG() { return R16_NV_LONG; }
		public void setR16_NV_LONG(BigDecimal r16_NV_LONG) { R16_NV_LONG = r16_NV_LONG; }
		public BigDecimal getR16_NV_SHORT() { return R16_NV_SHORT; }
		public void setR16_NV_SHORT(BigDecimal r16_NV_SHORT) { R16_NV_SHORT = r16_NV_SHORT; }
		public BigDecimal getR16_FV_LONG() { return R16_FV_LONG; }
		public void setR16_FV_LONG(BigDecimal r16_FV_LONG) { R16_FV_LONG = r16_FV_LONG; }
		public BigDecimal getR16_FV_SHORT() { return R16_FV_SHORT; }
		public void setR16_FV_SHORT(BigDecimal r16_FV_SHORT) { R16_FV_SHORT = r16_FV_SHORT; }
		public BigDecimal getR16_QFHA() { return R16_QFHA; }
		public void setR16_QFHA(BigDecimal r16_QFHA) { R16_QFHA = r16_QFHA; }
		public String getR17_PRODUCT() { return R17_PRODUCT; }
		public void setR17_PRODUCT(String r17_PRODUCT) { R17_PRODUCT = r17_PRODUCT; }
		public BigDecimal getR17_NV_LONG() { return R17_NV_LONG; }
		public void setR17_NV_LONG(BigDecimal r17_NV_LONG) { R17_NV_LONG = r17_NV_LONG; }
		public BigDecimal getR17_NV_SHORT() { return R17_NV_SHORT; }
		public void setR17_NV_SHORT(BigDecimal r17_NV_SHORT) { R17_NV_SHORT = r17_NV_SHORT; }
		public BigDecimal getR17_FV_LONG() { return R17_FV_LONG; }
		public void setR17_FV_LONG(BigDecimal r17_FV_LONG) { R17_FV_LONG = r17_FV_LONG; }
		public BigDecimal getR17_FV_SHORT() { return R17_FV_SHORT; }
		public void setR17_FV_SHORT(BigDecimal r17_FV_SHORT) { R17_FV_SHORT = r17_FV_SHORT; }
		public BigDecimal getR17_QFHA() { return R17_QFHA; }
		public void setR17_QFHA(BigDecimal r17_QFHA) { R17_QFHA = r17_QFHA; }
		public String getR18_PRODUCT() { return R18_PRODUCT; }
		public void setR18_PRODUCT(String r18_PRODUCT) { R18_PRODUCT = r18_PRODUCT; }
		public BigDecimal getR18_NV_LONG() { return R18_NV_LONG; }
		public void setR18_NV_LONG(BigDecimal r18_NV_LONG) { R18_NV_LONG = r18_NV_LONG; }
		public BigDecimal getR18_NV_SHORT() { return R18_NV_SHORT; }
		public void setR18_NV_SHORT(BigDecimal r18_NV_SHORT) { R18_NV_SHORT = r18_NV_SHORT; }
		public BigDecimal getR18_FV_LONG() { return R18_FV_LONG; }
		public void setR18_FV_LONG(BigDecimal r18_FV_LONG) { R18_FV_LONG = r18_FV_LONG; }
		public BigDecimal getR18_FV_SHORT() { return R18_FV_SHORT; }
		public void setR18_FV_SHORT(BigDecimal r18_FV_SHORT) { R18_FV_SHORT = r18_FV_SHORT; }
		public BigDecimal getR18_QFHA() { return R18_QFHA; }
		public void setR18_QFHA(BigDecimal r18_QFHA) { R18_QFHA = r18_QFHA; }
		public String getR19_PRODUCT() { return R19_PRODUCT; }
		public void setR19_PRODUCT(String r19_PRODUCT) { R19_PRODUCT = r19_PRODUCT; }
		public BigDecimal getR19_NV_LONG() { return R19_NV_LONG; }
		public void setR19_NV_LONG(BigDecimal r19_NV_LONG) { R19_NV_LONG = r19_NV_LONG; }
		public BigDecimal getR19_NV_SHORT() { return R19_NV_SHORT; }
		public void setR19_NV_SHORT(BigDecimal r19_NV_SHORT) { R19_NV_SHORT = r19_NV_SHORT; }
		public BigDecimal getR19_FV_LONG() { return R19_FV_LONG; }
		public void setR19_FV_LONG(BigDecimal r19_FV_LONG) { R19_FV_LONG = r19_FV_LONG; }
		public BigDecimal getR19_FV_SHORT() { return R19_FV_SHORT; }
		public void setR19_FV_SHORT(BigDecimal r19_FV_SHORT) { R19_FV_SHORT = r19_FV_SHORT; }
		public BigDecimal getR19_QFHA() { return R19_QFHA; }
		public void setR19_QFHA(BigDecimal r19_QFHA) { R19_QFHA = r19_QFHA; }
		public String getR20_PRODUCT() { return R20_PRODUCT; }
		public void setR20_PRODUCT(String r20_PRODUCT) { R20_PRODUCT = r20_PRODUCT; }
		public BigDecimal getR20_NV_LONG() { return R20_NV_LONG; }
		public void setR20_NV_LONG(BigDecimal r20_NV_LONG) { R20_NV_LONG = r20_NV_LONG; }
		public BigDecimal getR20_NV_SHORT() { return R20_NV_SHORT; }
		public void setR20_NV_SHORT(BigDecimal r20_NV_SHORT) { R20_NV_SHORT = r20_NV_SHORT; }
		public BigDecimal getR20_FV_LONG() { return R20_FV_LONG; }
		public void setR20_FV_LONG(BigDecimal r20_FV_LONG) { R20_FV_LONG = r20_FV_LONG; }
		public BigDecimal getR20_FV_SHORT() { return R20_FV_SHORT; }
		public void setR20_FV_SHORT(BigDecimal r20_FV_SHORT) { R20_FV_SHORT = r20_FV_SHORT; }
		public BigDecimal getR20_QFHA() { return R20_QFHA; }
		public void setR20_QFHA(BigDecimal r20_QFHA) { R20_QFHA = r20_QFHA; }
		public String getR21_PRODUCT() { return R21_PRODUCT; }
		public void setR21_PRODUCT(String r21_PRODUCT) { R21_PRODUCT = r21_PRODUCT; }
		public BigDecimal getR21_NV_LONG() { return R21_NV_LONG; }
		public void setR21_NV_LONG(BigDecimal r21_NV_LONG) { R21_NV_LONG = r21_NV_LONG; }
		public BigDecimal getR21_NV_SHORT() { return R21_NV_SHORT; }
		public void setR21_NV_SHORT(BigDecimal r21_NV_SHORT) { R21_NV_SHORT = r21_NV_SHORT; }
		public BigDecimal getR21_FV_LONG() { return R21_FV_LONG; }
		public void setR21_FV_LONG(BigDecimal r21_FV_LONG) { R21_FV_LONG = r21_FV_LONG; }
		public BigDecimal getR21_FV_SHORT() { return R21_FV_SHORT; }
		public void setR21_FV_SHORT(BigDecimal r21_FV_SHORT) { R21_FV_SHORT = r21_FV_SHORT; }
		public BigDecimal getR21_QFHA() { return R21_QFHA; }
		public void setR21_QFHA(BigDecimal r21_QFHA) { R21_QFHA = r21_QFHA; }
		public String getR22_PRODUCT() { return R22_PRODUCT; }
		public void setR22_PRODUCT(String r22_PRODUCT) { R22_PRODUCT = r22_PRODUCT; }
		public BigDecimal getR22_NV_LONG() { return R22_NV_LONG; }
		public void setR22_NV_LONG(BigDecimal r22_NV_LONG) { R22_NV_LONG = r22_NV_LONG; }
		public BigDecimal getR22_NV_SHORT() { return R22_NV_SHORT; }
		public void setR22_NV_SHORT(BigDecimal r22_NV_SHORT) { R22_NV_SHORT = r22_NV_SHORT; }
		public BigDecimal getR22_FV_LONG() { return R22_FV_LONG; }
		public void setR22_FV_LONG(BigDecimal r22_FV_LONG) { R22_FV_LONG = r22_FV_LONG; }
		public BigDecimal getR22_FV_SHORT() { return R22_FV_SHORT; }
		public void setR22_FV_SHORT(BigDecimal r22_FV_SHORT) { R22_FV_SHORT = r22_FV_SHORT; }
		public BigDecimal getR22_QFHA() { return R22_QFHA; }
		public void setR22_QFHA(BigDecimal r22_QFHA) { R22_QFHA = r22_QFHA; }
		public String getR23_PRODUCT() { return R23_PRODUCT; }
		public void setR23_PRODUCT(String r23_PRODUCT) { R23_PRODUCT = r23_PRODUCT; }
		public BigDecimal getR23_NV_LONG() { return R23_NV_LONG; }
		public void setR23_NV_LONG(BigDecimal r23_NV_LONG) { R23_NV_LONG = r23_NV_LONG; }
		public BigDecimal getR23_NV_SHORT() { return R23_NV_SHORT; }
		public void setR23_NV_SHORT(BigDecimal r23_NV_SHORT) { R23_NV_SHORT = r23_NV_SHORT; }
		public BigDecimal getR23_FV_LONG() { return R23_FV_LONG; }
		public void setR23_FV_LONG(BigDecimal r23_FV_LONG) { R23_FV_LONG = r23_FV_LONG; }
		public BigDecimal getR23_FV_SHORT() { return R23_FV_SHORT; }
		public void setR23_FV_SHORT(BigDecimal r23_FV_SHORT) { R23_FV_SHORT = r23_FV_SHORT; }
		public BigDecimal getR23_QFHA() { return R23_QFHA; }
		public void setR23_QFHA(BigDecimal r23_QFHA) { R23_QFHA = r23_QFHA; }
		public String getR24_PRODUCT() { return R24_PRODUCT; }
		public void setR24_PRODUCT(String r24_PRODUCT) { R24_PRODUCT = r24_PRODUCT; }
		public BigDecimal getR24_NV_LONG() { return R24_NV_LONG; }
		public void setR24_NV_LONG(BigDecimal r24_NV_LONG) { R24_NV_LONG = r24_NV_LONG; }
		public BigDecimal getR24_NV_SHORT() { return R24_NV_SHORT; }
		public void setR24_NV_SHORT(BigDecimal r24_NV_SHORT) { R24_NV_SHORT = r24_NV_SHORT; }
		public BigDecimal getR24_FV_LONG() { return R24_FV_LONG; }
		public void setR24_FV_LONG(BigDecimal r24_FV_LONG) { R24_FV_LONG = r24_FV_LONG; }
		public BigDecimal getR24_FV_SHORT() { return R24_FV_SHORT; }
		public void setR24_FV_SHORT(BigDecimal r24_FV_SHORT) { R24_FV_SHORT = r24_FV_SHORT; }
		public BigDecimal getR24_QFHA() { return R24_QFHA; }
		public void setR24_QFHA(BigDecimal r24_QFHA) { R24_QFHA = r24_QFHA; }
		public String getR25_PRODUCT() { return R25_PRODUCT; }
		public void setR25_PRODUCT(String r25_PRODUCT) { R25_PRODUCT = r25_PRODUCT; }
		public BigDecimal getR25_NV_LONG() { return R25_NV_LONG; }
		public void setR25_NV_LONG(BigDecimal r25_NV_LONG) { R25_NV_LONG = r25_NV_LONG; }
		public BigDecimal getR25_NV_SHORT() { return R25_NV_SHORT; }
		public void setR25_NV_SHORT(BigDecimal r25_NV_SHORT) { R25_NV_SHORT = r25_NV_SHORT; }
		public BigDecimal getR25_FV_LONG() { return R25_FV_LONG; }
		public void setR25_FV_LONG(BigDecimal r25_FV_LONG) { R25_FV_LONG = r25_FV_LONG; }
		public BigDecimal getR25_FV_SHORT() { return R25_FV_SHORT; }
		public void setR25_FV_SHORT(BigDecimal r25_FV_SHORT) { R25_FV_SHORT = r25_FV_SHORT; }
		public BigDecimal getR25_QFHA() { return R25_QFHA; }
		public void setR25_QFHA(BigDecimal r25_QFHA) { R25_QFHA = r25_QFHA; }
		public String getR26_PRODUCT() { return R26_PRODUCT; }
		public void setR26_PRODUCT(String r26_PRODUCT) { R26_PRODUCT = r26_PRODUCT; }
		public BigDecimal getR26_NV_LONG() { return R26_NV_LONG; }
		public void setR26_NV_LONG(BigDecimal r26_NV_LONG) { R26_NV_LONG = r26_NV_LONG; }
		public BigDecimal getR26_NV_SHORT() { return R26_NV_SHORT; }
		public void setR26_NV_SHORT(BigDecimal r26_NV_SHORT) { R26_NV_SHORT = r26_NV_SHORT; }
		public BigDecimal getR26_FV_LONG() { return R26_FV_LONG; }
		public void setR26_FV_LONG(BigDecimal r26_FV_LONG) { R26_FV_LONG = r26_FV_LONG; }
		public BigDecimal getR26_FV_SHORT() { return R26_FV_SHORT; }
		public void setR26_FV_SHORT(BigDecimal r26_FV_SHORT) { R26_FV_SHORT = r26_FV_SHORT; }
		public BigDecimal getR26_QFHA() { return R26_QFHA; }
		public void setR26_QFHA(BigDecimal r26_QFHA) { R26_QFHA = r26_QFHA; }
		public String getR27_PRODUCT() { return R27_PRODUCT; }
		public void setR27_PRODUCT(String r27_PRODUCT) { R27_PRODUCT = r27_PRODUCT; }
		public BigDecimal getR27_NV_LONG() { return R27_NV_LONG; }
		public void setR27_NV_LONG(BigDecimal r27_NV_LONG) { R27_NV_LONG = r27_NV_LONG; }
		public BigDecimal getR27_NV_SHORT() { return R27_NV_SHORT; }
		public void setR27_NV_SHORT(BigDecimal r27_NV_SHORT) { R27_NV_SHORT = r27_NV_SHORT; }
		public BigDecimal getR27_FV_LONG() { return R27_FV_LONG; }
		public void setR27_FV_LONG(BigDecimal r27_FV_LONG) { R27_FV_LONG = r27_FV_LONG; }
		public BigDecimal getR27_FV_SHORT() { return R27_FV_SHORT; }
		public void setR27_FV_SHORT(BigDecimal r27_FV_SHORT) { R27_FV_SHORT = r27_FV_SHORT; }
		public BigDecimal getR27_QFHA() { return R27_QFHA; }
		public void setR27_QFHA(BigDecimal r27_QFHA) { R27_QFHA = r27_QFHA; }
		public String getR28_PRODUCT() { return R28_PRODUCT; }
		public void setR28_PRODUCT(String r28_PRODUCT) { R28_PRODUCT = r28_PRODUCT; }
		public BigDecimal getR28_NV_LONG() { return R28_NV_LONG; }
		public void setR28_NV_LONG(BigDecimal r28_NV_LONG) { R28_NV_LONG = r28_NV_LONG; }
		public BigDecimal getR28_NV_SHORT() { return R28_NV_SHORT; }
		public void setR28_NV_SHORT(BigDecimal r28_NV_SHORT) { R28_NV_SHORT = r28_NV_SHORT; }
		public BigDecimal getR28_FV_LONG() { return R28_FV_LONG; }
		public void setR28_FV_LONG(BigDecimal r28_FV_LONG) { R28_FV_LONG = r28_FV_LONG; }
		public BigDecimal getR28_FV_SHORT() { return R28_FV_SHORT; }
		public void setR28_FV_SHORT(BigDecimal r28_FV_SHORT) { R28_FV_SHORT = r28_FV_SHORT; }
		public BigDecimal getR28_QFHA() { return R28_QFHA; }
		public void setR28_QFHA(BigDecimal r28_QFHA) { R28_QFHA = r28_QFHA; }
		public String getR29_PRODUCT() { return R29_PRODUCT; }
		public void setR29_PRODUCT(String r29_PRODUCT) { R29_PRODUCT = r29_PRODUCT; }
		public BigDecimal getR29_NV_LONG() { return R29_NV_LONG; }
		public void setR29_NV_LONG(BigDecimal r29_NV_LONG) { R29_NV_LONG = r29_NV_LONG; }
		public BigDecimal getR29_NV_SHORT() { return R29_NV_SHORT; }
		public void setR29_NV_SHORT(BigDecimal r29_NV_SHORT) { R29_NV_SHORT = r29_NV_SHORT; }
		public BigDecimal getR29_FV_LONG() { return R29_FV_LONG; }
		public void setR29_FV_LONG(BigDecimal r29_FV_LONG) { R29_FV_LONG = r29_FV_LONG; }
		public BigDecimal getR29_FV_SHORT() { return R29_FV_SHORT; }
		public void setR29_FV_SHORT(BigDecimal r29_FV_SHORT) { R29_FV_SHORT = r29_FV_SHORT; }
		public BigDecimal getR29_QFHA() { return R29_QFHA; }
		public void setR29_QFHA(BigDecimal r29_QFHA) { R29_QFHA = r29_QFHA; }
		public String getR30_PRODUCT() { return R30_PRODUCT; }
		public void setR30_PRODUCT(String r30_PRODUCT) { R30_PRODUCT = r30_PRODUCT; }
		public BigDecimal getR30_NV_LONG() { return R30_NV_LONG; }
		public void setR30_NV_LONG(BigDecimal r30_NV_LONG) { R30_NV_LONG = r30_NV_LONG; }
		public BigDecimal getR30_NV_SHORT() { return R30_NV_SHORT; }
		public void setR30_NV_SHORT(BigDecimal r30_NV_SHORT) { R30_NV_SHORT = r30_NV_SHORT; }
		public BigDecimal getR30_FV_LONG() { return R30_FV_LONG; }
		public void setR30_FV_LONG(BigDecimal r30_FV_LONG) { R30_FV_LONG = r30_FV_LONG; }
		public BigDecimal getR30_FV_SHORT() { return R30_FV_SHORT; }
		public void setR30_FV_SHORT(BigDecimal r30_FV_SHORT) { R30_FV_SHORT = r30_FV_SHORT; }
		public BigDecimal getR30_QFHA() { return R30_QFHA; }
		public void setR30_QFHA(BigDecimal r30_QFHA) { R30_QFHA = r30_QFHA; }
		public String getR31_PRODUCT() { return R31_PRODUCT; }
		public void setR31_PRODUCT(String r31_PRODUCT) { R31_PRODUCT = r31_PRODUCT; }
		public BigDecimal getR31_NV_LONG() { return R31_NV_LONG; }
		public void setR31_NV_LONG(BigDecimal r31_NV_LONG) { R31_NV_LONG = r31_NV_LONG; }
		public BigDecimal getR31_NV_SHORT() { return R31_NV_SHORT; }
		public void setR31_NV_SHORT(BigDecimal r31_NV_SHORT) { R31_NV_SHORT = r31_NV_SHORT; }
		public BigDecimal getR31_FV_LONG() { return R31_FV_LONG; }
		public void setR31_FV_LONG(BigDecimal r31_FV_LONG) { R31_FV_LONG = r31_FV_LONG; }
		public BigDecimal getR31_FV_SHORT() { return R31_FV_SHORT; }
		public void setR31_FV_SHORT(BigDecimal r31_FV_SHORT) { R31_FV_SHORT = r31_FV_SHORT; }
		public BigDecimal getR31_QFHA() { return R31_QFHA; }
		public void setR31_QFHA(BigDecimal r31_QFHA) { R31_QFHA = r31_QFHA; }
		public String getR32_PRODUCT() { return R32_PRODUCT; }
		public void setR32_PRODUCT(String r32_PRODUCT) { R32_PRODUCT = r32_PRODUCT; }
		public BigDecimal getR32_NV_LONG() { return R32_NV_LONG; }
		public void setR32_NV_LONG(BigDecimal r32_NV_LONG) { R32_NV_LONG = r32_NV_LONG; }
		public BigDecimal getR32_NV_SHORT() { return R32_NV_SHORT; }
		public void setR32_NV_SHORT(BigDecimal r32_NV_SHORT) { R32_NV_SHORT = r32_NV_SHORT; }
		public BigDecimal getR32_FV_LONG() { return R32_FV_LONG; }
		public void setR32_FV_LONG(BigDecimal r32_FV_LONG) { R32_FV_LONG = r32_FV_LONG; }
		public BigDecimal getR32_FV_SHORT() { return R32_FV_SHORT; }
		public void setR32_FV_SHORT(BigDecimal r32_FV_SHORT) { R32_FV_SHORT = r32_FV_SHORT; }
		public BigDecimal getR32_QFHA() { return R32_QFHA; }
		public void setR32_QFHA(BigDecimal r32_QFHA) { R32_QFHA = r32_QFHA; }
		public String getR33_PRODUCT() { return R33_PRODUCT; }
		public void setR33_PRODUCT(String r33_PRODUCT) { R33_PRODUCT = r33_PRODUCT; }
		public BigDecimal getR33_NV_LONG() { return R33_NV_LONG; }
		public void setR33_NV_LONG(BigDecimal r33_NV_LONG) { R33_NV_LONG = r33_NV_LONG; }
		public BigDecimal getR33_NV_SHORT() { return R33_NV_SHORT; }
		public void setR33_NV_SHORT(BigDecimal r33_NV_SHORT) { R33_NV_SHORT = r33_NV_SHORT; }
		public BigDecimal getR33_FV_LONG() { return R33_FV_LONG; }
		public void setR33_FV_LONG(BigDecimal r33_FV_LONG) { R33_FV_LONG = r33_FV_LONG; }
		public BigDecimal getR33_FV_SHORT() { return R33_FV_SHORT; }
		public void setR33_FV_SHORT(BigDecimal r33_FV_SHORT) { R33_FV_SHORT = r33_FV_SHORT; }
		public BigDecimal getR33_QFHA() { return R33_QFHA; }
		public void setR33_QFHA(BigDecimal r33_QFHA) { R33_QFHA = r33_QFHA; }
		public String getR34_PRODUCT() { return R34_PRODUCT; }
		public void setR34_PRODUCT(String r34_PRODUCT) { R34_PRODUCT = r34_PRODUCT; }
		public BigDecimal getR34_NV_LONG() { return R34_NV_LONG; }
		public void setR34_NV_LONG(BigDecimal r34_NV_LONG) { R34_NV_LONG = r34_NV_LONG; }
		public BigDecimal getR34_NV_SHORT() { return R34_NV_SHORT; }
		public void setR34_NV_SHORT(BigDecimal r34_NV_SHORT) { R34_NV_SHORT = r34_NV_SHORT; }
		public BigDecimal getR34_FV_LONG() { return R34_FV_LONG; }
		public void setR34_FV_LONG(BigDecimal r34_FV_LONG) { R34_FV_LONG = r34_FV_LONG; }
		public BigDecimal getR34_FV_SHORT() { return R34_FV_SHORT; }
		public void setR34_FV_SHORT(BigDecimal r34_FV_SHORT) { R34_FV_SHORT = r34_FV_SHORT; }
		public BigDecimal getR34_QFHA() { return R34_QFHA; }
		public void setR34_QFHA(BigDecimal r34_QFHA) { R34_QFHA = r34_QFHA; }
		public String getR35_PRODUCT() { return R35_PRODUCT; }
		public void setR35_PRODUCT(String r35_PRODUCT) { R35_PRODUCT = r35_PRODUCT; }
		public BigDecimal getR35_NV_LONG() { return R35_NV_LONG; }
		public void setR35_NV_LONG(BigDecimal r35_NV_LONG) { R35_NV_LONG = r35_NV_LONG; }
		public BigDecimal getR35_NV_SHORT() { return R35_NV_SHORT; }
		public void setR35_NV_SHORT(BigDecimal r35_NV_SHORT) { R35_NV_SHORT = r35_NV_SHORT; }
		public BigDecimal getR35_FV_LONG() { return R35_FV_LONG; }
		public void setR35_FV_LONG(BigDecimal r35_FV_LONG) { R35_FV_LONG = r35_FV_LONG; }
		public BigDecimal getR35_FV_SHORT() { return R35_FV_SHORT; }
		public void setR35_FV_SHORT(BigDecimal r35_FV_SHORT) { R35_FV_SHORT = r35_FV_SHORT; }
		public BigDecimal getR35_QFHA() { return R35_QFHA; }
		public void setR35_QFHA(BigDecimal r35_QFHA) { R35_QFHA = r35_QFHA; }
		public String getR36_PRODUCT() { return R36_PRODUCT; }
		public void setR36_PRODUCT(String r36_PRODUCT) { R36_PRODUCT = r36_PRODUCT; }
		public BigDecimal getR36_NV_LONG() { return R36_NV_LONG; }
		public void setR36_NV_LONG(BigDecimal r36_NV_LONG) { R36_NV_LONG = r36_NV_LONG; }
		public BigDecimal getR36_NV_SHORT() { return R36_NV_SHORT; }
		public void setR36_NV_SHORT(BigDecimal r36_NV_SHORT) { R36_NV_SHORT = r36_NV_SHORT; }
		public BigDecimal getR36_FV_LONG() { return R36_FV_LONG; }
		public void setR36_FV_LONG(BigDecimal r36_FV_LONG) { R36_FV_LONG = r36_FV_LONG; }
		public BigDecimal getR36_FV_SHORT() { return R36_FV_SHORT; }
		public void setR36_FV_SHORT(BigDecimal r36_FV_SHORT) { R36_FV_SHORT = r36_FV_SHORT; }
		public BigDecimal getR36_QFHA() { return R36_QFHA; }
		public void setR36_QFHA(BigDecimal r36_QFHA) { R36_QFHA = r36_QFHA; }
		public String getR37_PRODUCT() { return R37_PRODUCT; }
		public void setR37_PRODUCT(String r37_PRODUCT) { R37_PRODUCT = r37_PRODUCT; }
		public BigDecimal getR37_NV_LONG() { return R37_NV_LONG; }
		public void setR37_NV_LONG(BigDecimal r37_NV_LONG) { R37_NV_LONG = r37_NV_LONG; }
		public BigDecimal getR37_NV_SHORT() { return R37_NV_SHORT; }
		public void setR37_NV_SHORT(BigDecimal r37_NV_SHORT) { R37_NV_SHORT = r37_NV_SHORT; }
		public BigDecimal getR37_FV_LONG() { return R37_FV_LONG; }
		public void setR37_FV_LONG(BigDecimal r37_FV_LONG) { R37_FV_LONG = r37_FV_LONG; }
		public BigDecimal getR37_FV_SHORT() { return R37_FV_SHORT; }
		public void setR37_FV_SHORT(BigDecimal r37_FV_SHORT) { R37_FV_SHORT = r37_FV_SHORT; }
		public BigDecimal getR37_QFHA() { return R37_QFHA; }
		public void setR37_QFHA(BigDecimal r37_QFHA) { R37_QFHA = r37_QFHA; }
		public String getR38_PRODUCT() { return R38_PRODUCT; }
		public void setR38_PRODUCT(String r38_PRODUCT) { R38_PRODUCT = r38_PRODUCT; }
		public BigDecimal getR38_NV_LONG() { return R38_NV_LONG; }
		public void setR38_NV_LONG(BigDecimal r38_NV_LONG) { R38_NV_LONG = r38_NV_LONG; }
		public BigDecimal getR38_NV_SHORT() { return R38_NV_SHORT; }
		public void setR38_NV_SHORT(BigDecimal r38_NV_SHORT) { R38_NV_SHORT = r38_NV_SHORT; }
		public BigDecimal getR38_FV_LONG() { return R38_FV_LONG; }
		public void setR38_FV_LONG(BigDecimal r38_FV_LONG) { R38_FV_LONG = r38_FV_LONG; }
		public BigDecimal getR38_FV_SHORT() { return R38_FV_SHORT; }
		public void setR38_FV_SHORT(BigDecimal r38_FV_SHORT) { R38_FV_SHORT = r38_FV_SHORT; }
		public BigDecimal getR38_QFHA() { return R38_QFHA; }
		public void setR38_QFHA(BigDecimal r38_QFHA) { R38_QFHA = r38_QFHA; }
		public String getR39_PRODUCT() { return R39_PRODUCT; }
		public void setR39_PRODUCT(String r39_PRODUCT) { R39_PRODUCT = r39_PRODUCT; }
		public BigDecimal getR39_NV_LONG() { return R39_NV_LONG; }
		public void setR39_NV_LONG(BigDecimal r39_NV_LONG) { R39_NV_LONG = r39_NV_LONG; }
		public BigDecimal getR39_NV_SHORT() { return R39_NV_SHORT; }
		public void setR39_NV_SHORT(BigDecimal r39_NV_SHORT) { R39_NV_SHORT = r39_NV_SHORT; }
		public BigDecimal getR39_FV_LONG() { return R39_FV_LONG; }
		public void setR39_FV_LONG(BigDecimal r39_FV_LONG) { R39_FV_LONG = r39_FV_LONG; }
		public BigDecimal getR39_FV_SHORT() { return R39_FV_SHORT; }
		public void setR39_FV_SHORT(BigDecimal r39_FV_SHORT) { R39_FV_SHORT = r39_FV_SHORT; }
		public BigDecimal getR39_QFHA() { return R39_QFHA; }
		public void setR39_QFHA(BigDecimal r39_QFHA) { R39_QFHA = r39_QFHA; }
		public String getR40_PRODUCT() { return R40_PRODUCT; }
		public void setR40_PRODUCT(String r40_PRODUCT) { R40_PRODUCT = r40_PRODUCT; }
		public BigDecimal getR40_NV_LONG() { return R40_NV_LONG; }
		public void setR40_NV_LONG(BigDecimal r40_NV_LONG) { R40_NV_LONG = r40_NV_LONG; }
		public BigDecimal getR40_NV_SHORT() { return R40_NV_SHORT; }
		public void setR40_NV_SHORT(BigDecimal r40_NV_SHORT) { R40_NV_SHORT = r40_NV_SHORT; }
		public BigDecimal getR40_FV_LONG() { return R40_FV_LONG; }
		public void setR40_FV_LONG(BigDecimal r40_FV_LONG) { R40_FV_LONG = r40_FV_LONG; }
		public BigDecimal getR40_FV_SHORT() { return R40_FV_SHORT; }
		public void setR40_FV_SHORT(BigDecimal r40_FV_SHORT) { R40_FV_SHORT = r40_FV_SHORT; }
		public BigDecimal getR40_QFHA() { return R40_QFHA; }
		public void setR40_QFHA(BigDecimal r40_QFHA) { R40_QFHA = r40_QFHA; }
		public String getR41_PRODUCT() { return R41_PRODUCT; }
		public void setR41_PRODUCT(String r41_PRODUCT) { R41_PRODUCT = r41_PRODUCT; }
		public BigDecimal getR41_NV_LONG() { return R41_NV_LONG; }
		public void setR41_NV_LONG(BigDecimal r41_NV_LONG) { R41_NV_LONG = r41_NV_LONG; }
		public BigDecimal getR41_NV_SHORT() { return R41_NV_SHORT; }
		public void setR41_NV_SHORT(BigDecimal r41_NV_SHORT) { R41_NV_SHORT = r41_NV_SHORT; }
		public BigDecimal getR41_FV_LONG() { return R41_FV_LONG; }
		public void setR41_FV_LONG(BigDecimal r41_FV_LONG) { R41_FV_LONG = r41_FV_LONG; }
		public BigDecimal getR41_FV_SHORT() { return R41_FV_SHORT; }
		public void setR41_FV_SHORT(BigDecimal r41_FV_SHORT) { R41_FV_SHORT = r41_FV_SHORT; }
		public BigDecimal getR41_QFHA() { return R41_QFHA; }
		public void setR41_QFHA(BigDecimal r41_QFHA) { R41_QFHA = r41_QFHA; }
		public String getR42_PRODUCT() { return R42_PRODUCT; }
		public void setR42_PRODUCT(String r42_PRODUCT) { R42_PRODUCT = r42_PRODUCT; }
		public BigDecimal getR42_NV_LONG() { return R42_NV_LONG; }
		public void setR42_NV_LONG(BigDecimal r42_NV_LONG) { R42_NV_LONG = r42_NV_LONG; }
		public BigDecimal getR42_NV_SHORT() { return R42_NV_SHORT; }
		public void setR42_NV_SHORT(BigDecimal r42_NV_SHORT) { R42_NV_SHORT = r42_NV_SHORT; }
		public BigDecimal getR42_FV_LONG() { return R42_FV_LONG; }
		public void setR42_FV_LONG(BigDecimal r42_FV_LONG) { R42_FV_LONG = r42_FV_LONG; }
		public BigDecimal getR42_FV_SHORT() { return R42_FV_SHORT; }
		public void setR42_FV_SHORT(BigDecimal r42_FV_SHORT) { R42_FV_SHORT = r42_FV_SHORT; }
		public BigDecimal getR42_QFHA() { return R42_QFHA; }
		public void setR42_QFHA(BigDecimal r42_QFHA) { R42_QFHA = r42_QFHA; }
		public String getR43_PRODUCT() { return R43_PRODUCT; }
		public void setR43_PRODUCT(String r43_PRODUCT) { R43_PRODUCT = r43_PRODUCT; }
		public BigDecimal getR43_NV_LONG() { return R43_NV_LONG; }
		public void setR43_NV_LONG(BigDecimal r43_NV_LONG) { R43_NV_LONG = r43_NV_LONG; }
		public BigDecimal getR43_NV_SHORT() { return R43_NV_SHORT; }
		public void setR43_NV_SHORT(BigDecimal r43_NV_SHORT) { R43_NV_SHORT = r43_NV_SHORT; }
		public BigDecimal getR43_FV_LONG() { return R43_FV_LONG; }
		public void setR43_FV_LONG(BigDecimal r43_FV_LONG) { R43_FV_LONG = r43_FV_LONG; }
		public BigDecimal getR43_FV_SHORT() { return R43_FV_SHORT; }
		public void setR43_FV_SHORT(BigDecimal r43_FV_SHORT) { R43_FV_SHORT = r43_FV_SHORT; }
		public BigDecimal getR43_QFHA() { return R43_QFHA; }
		public void setR43_QFHA(BigDecimal r43_QFHA) { R43_QFHA = r43_QFHA; }
		public String getR44_PRODUCT() { return R44_PRODUCT; }
		public void setR44_PRODUCT(String r44_PRODUCT) { R44_PRODUCT = r44_PRODUCT; }
		public BigDecimal getR44_NV_LONG() { return R44_NV_LONG; }
		public void setR44_NV_LONG(BigDecimal r44_NV_LONG) { R44_NV_LONG = r44_NV_LONG; }
		public BigDecimal getR44_NV_SHORT() { return R44_NV_SHORT; }
		public void setR44_NV_SHORT(BigDecimal r44_NV_SHORT) { R44_NV_SHORT = r44_NV_SHORT; }
		public BigDecimal getR44_FV_LONG() { return R44_FV_LONG; }
		public void setR44_FV_LONG(BigDecimal r44_FV_LONG) { R44_FV_LONG = r44_FV_LONG; }
		public BigDecimal getR44_FV_SHORT() { return R44_FV_SHORT; }
		public void setR44_FV_SHORT(BigDecimal r44_FV_SHORT) { R44_FV_SHORT = r44_FV_SHORT; }
		public BigDecimal getR44_QFHA() { return R44_QFHA; }
		public void setR44_QFHA(BigDecimal r44_QFHA) { R44_QFHA = r44_QFHA; }
		public String getR45_PRODUCT() { return R45_PRODUCT; }
		public void setR45_PRODUCT(String r45_PRODUCT) { R45_PRODUCT = r45_PRODUCT; }
		public BigDecimal getR45_NV_LONG() { return R45_NV_LONG; }
		public void setR45_NV_LONG(BigDecimal r45_NV_LONG) { R45_NV_LONG = r45_NV_LONG; }
		public BigDecimal getR45_NV_SHORT() { return R45_NV_SHORT; }
		public void setR45_NV_SHORT(BigDecimal r45_NV_SHORT) { R45_NV_SHORT = r45_NV_SHORT; }
		public BigDecimal getR45_FV_LONG() { return R45_FV_LONG; }
		public void setR45_FV_LONG(BigDecimal r45_FV_LONG) { R45_FV_LONG = r45_FV_LONG; }
		public BigDecimal getR45_FV_SHORT() { return R45_FV_SHORT; }
		public void setR45_FV_SHORT(BigDecimal r45_FV_SHORT) { R45_FV_SHORT = r45_FV_SHORT; }
		public BigDecimal getR45_QFHA() { return R45_QFHA; }
		public void setR45_QFHA(BigDecimal r45_QFHA) { R45_QFHA = r45_QFHA; }
		public String getR46_PRODUCT() { return R46_PRODUCT; }
		public void setR46_PRODUCT(String r46_PRODUCT) { R46_PRODUCT = r46_PRODUCT; }
		public BigDecimal getR46_NV_LONG() { return R46_NV_LONG; }
		public void setR46_NV_LONG(BigDecimal r46_NV_LONG) { R46_NV_LONG = r46_NV_LONG; }
		public BigDecimal getR46_NV_SHORT() { return R46_NV_SHORT; }
		public void setR46_NV_SHORT(BigDecimal r46_NV_SHORT) { R46_NV_SHORT = r46_NV_SHORT; }
		public BigDecimal getR46_FV_LONG() { return R46_FV_LONG; }
		public void setR46_FV_LONG(BigDecimal r46_FV_LONG) { R46_FV_LONG = r46_FV_LONG; }
		public BigDecimal getR46_FV_SHORT() { return R46_FV_SHORT; }
		public void setR46_FV_SHORT(BigDecimal r46_FV_SHORT) { R46_FV_SHORT = r46_FV_SHORT; }
		public BigDecimal getR46_QFHA() { return R46_QFHA; }
		public void setR46_QFHA(BigDecimal r46_QFHA) { R46_QFHA = r46_QFHA; }
		public String getR47_PRODUCT() { return R47_PRODUCT; }
		public void setR47_PRODUCT(String r47_PRODUCT) { R47_PRODUCT = r47_PRODUCT; }
		public BigDecimal getR47_NV_LONG() { return R47_NV_LONG; }
		public void setR47_NV_LONG(BigDecimal r47_NV_LONG) { R47_NV_LONG = r47_NV_LONG; }
		public BigDecimal getR47_NV_SHORT() { return R47_NV_SHORT; }
		public void setR47_NV_SHORT(BigDecimal r47_NV_SHORT) { R47_NV_SHORT = r47_NV_SHORT; }
		public BigDecimal getR47_FV_LONG() { return R47_FV_LONG; }
		public void setR47_FV_LONG(BigDecimal r47_FV_LONG) { R47_FV_LONG = r47_FV_LONG; }
		public BigDecimal getR47_FV_SHORT() { return R47_FV_SHORT; }
		public void setR47_FV_SHORT(BigDecimal r47_FV_SHORT) { R47_FV_SHORT = r47_FV_SHORT; }
		public BigDecimal getR47_QFHA() { return R47_QFHA; }
		public void setR47_QFHA(BigDecimal r47_QFHA) { R47_QFHA = r47_QFHA; }
		public String getR48_PRODUCT() { return R48_PRODUCT; }
		public void setR48_PRODUCT(String r48_PRODUCT) { R48_PRODUCT = r48_PRODUCT; }
		public BigDecimal getR48_NV_LONG() { return R48_NV_LONG; }
		public void setR48_NV_LONG(BigDecimal r48_NV_LONG) { R48_NV_LONG = r48_NV_LONG; }
		public BigDecimal getR48_NV_SHORT() { return R48_NV_SHORT; }
		public void setR48_NV_SHORT(BigDecimal r48_NV_SHORT) { R48_NV_SHORT = r48_NV_SHORT; }
		public BigDecimal getR48_FV_LONG() { return R48_FV_LONG; }
		public void setR48_FV_LONG(BigDecimal r48_FV_LONG) { R48_FV_LONG = r48_FV_LONG; }
		public BigDecimal getR48_FV_SHORT() { return R48_FV_SHORT; }
		public void setR48_FV_SHORT(BigDecimal r48_FV_SHORT) { R48_FV_SHORT = r48_FV_SHORT; }
		public BigDecimal getR48_QFHA() { return R48_QFHA; }
		public void setR48_QFHA(BigDecimal r48_QFHA) { R48_QFHA = r48_QFHA; }
		public String getR49_PRODUCT() { return R49_PRODUCT; }
		public void setR49_PRODUCT(String r49_PRODUCT) { R49_PRODUCT = r49_PRODUCT; }
		public BigDecimal getR49_NV_LONG() { return R49_NV_LONG; }
		public void setR49_NV_LONG(BigDecimal r49_NV_LONG) { R49_NV_LONG = r49_NV_LONG; }
		public BigDecimal getR49_NV_SHORT() { return R49_NV_SHORT; }
		public void setR49_NV_SHORT(BigDecimal r49_NV_SHORT) { R49_NV_SHORT = r49_NV_SHORT; }
		public BigDecimal getR49_FV_LONG() { return R49_FV_LONG; }
		public void setR49_FV_LONG(BigDecimal r49_FV_LONG) { R49_FV_LONG = r49_FV_LONG; }
		public BigDecimal getR49_FV_SHORT() { return R49_FV_SHORT; }
		public void setR49_FV_SHORT(BigDecimal r49_FV_SHORT) { R49_FV_SHORT = r49_FV_SHORT; }
		public BigDecimal getR49_QFHA() { return R49_QFHA; }
		public void setR49_QFHA(BigDecimal r49_QFHA) { R49_QFHA = r49_QFHA; }
		public String getR50_PRODUCT() { return R50_PRODUCT; }
		public void setR50_PRODUCT(String r50_PRODUCT) { R50_PRODUCT = r50_PRODUCT; }
		public BigDecimal getR50_NV_LONG() { return R50_NV_LONG; }
		public void setR50_NV_LONG(BigDecimal r50_NV_LONG) { R50_NV_LONG = r50_NV_LONG; }
		public BigDecimal getR50_NV_SHORT() { return R50_NV_SHORT; }
		public void setR50_NV_SHORT(BigDecimal r50_NV_SHORT) { R50_NV_SHORT = r50_NV_SHORT; }
		public BigDecimal getR50_FV_LONG() { return R50_FV_LONG; }
		public void setR50_FV_LONG(BigDecimal r50_FV_LONG) { R50_FV_LONG = r50_FV_LONG; }
		public BigDecimal getR50_FV_SHORT() { return R50_FV_SHORT; }
		public void setR50_FV_SHORT(BigDecimal r50_FV_SHORT) { R50_FV_SHORT = r50_FV_SHORT; }
		public BigDecimal getR50_QFHA() { return R50_QFHA; }
		public void setR50_QFHA(BigDecimal r50_QFHA) { R50_QFHA = r50_QFHA; }
		public String getR51_PRODUCT() { return R51_PRODUCT; }
		public void setR51_PRODUCT(String r51_PRODUCT) { R51_PRODUCT = r51_PRODUCT; }
		public BigDecimal getR51_NV_LONG() { return R51_NV_LONG; }
		public void setR51_NV_LONG(BigDecimal r51_NV_LONG) { R51_NV_LONG = r51_NV_LONG; }
		public BigDecimal getR51_NV_SHORT() { return R51_NV_SHORT; }
		public void setR51_NV_SHORT(BigDecimal r51_NV_SHORT) { R51_NV_SHORT = r51_NV_SHORT; }
		public BigDecimal getR51_FV_LONG() { return R51_FV_LONG; }
		public void setR51_FV_LONG(BigDecimal r51_FV_LONG) { R51_FV_LONG = r51_FV_LONG; }
		public BigDecimal getR51_FV_SHORT() { return R51_FV_SHORT; }
		public void setR51_FV_SHORT(BigDecimal r51_FV_SHORT) { R51_FV_SHORT = r51_FV_SHORT; }
		public BigDecimal getR51_QFHA() { return R51_QFHA; }
		public void setR51_QFHA(BigDecimal r51_QFHA) { R51_QFHA = r51_QFHA; }
		public String getR52_PRODUCT() { return R52_PRODUCT; }
		public void setR52_PRODUCT(String r52_PRODUCT) { R52_PRODUCT = r52_PRODUCT; }
		public BigDecimal getR52_NV_LONG() { return R52_NV_LONG; }
		public void setR52_NV_LONG(BigDecimal r52_NV_LONG) { R52_NV_LONG = r52_NV_LONG; }
		public BigDecimal getR52_NV_SHORT() { return R52_NV_SHORT; }
		public void setR52_NV_SHORT(BigDecimal r52_NV_SHORT) { R52_NV_SHORT = r52_NV_SHORT; }
		public BigDecimal getR52_FV_LONG() { return R52_FV_LONG; }
		public void setR52_FV_LONG(BigDecimal r52_FV_LONG) { R52_FV_LONG = r52_FV_LONG; }
		public BigDecimal getR52_FV_SHORT() { return R52_FV_SHORT; }
		public void setR52_FV_SHORT(BigDecimal r52_FV_SHORT) { R52_FV_SHORT = r52_FV_SHORT; }
		public BigDecimal getR52_QFHA() { return R52_QFHA; }
		public void setR52_QFHA(BigDecimal r52_QFHA) { R52_QFHA = r52_QFHA; }
		public String getR53_PRODUCT() { return R53_PRODUCT; }
		public void setR53_PRODUCT(String r53_PRODUCT) { R53_PRODUCT = r53_PRODUCT; }
		public BigDecimal getR53_NV_LONG() { return R53_NV_LONG; }
		public void setR53_NV_LONG(BigDecimal r53_NV_LONG) { R53_NV_LONG = r53_NV_LONG; }
		public BigDecimal getR53_NV_SHORT() { return R53_NV_SHORT; }
		public void setR53_NV_SHORT(BigDecimal r53_NV_SHORT) { R53_NV_SHORT = r53_NV_SHORT; }
		public BigDecimal getR53_FV_LONG() { return R53_FV_LONG; }
		public void setR53_FV_LONG(BigDecimal r53_FV_LONG) { R53_FV_LONG = r53_FV_LONG; }
		public BigDecimal getR53_FV_SHORT() { return R53_FV_SHORT; }
		public void setR53_FV_SHORT(BigDecimal r53_FV_SHORT) { R53_FV_SHORT = r53_FV_SHORT; }
		public BigDecimal getR53_QFHA() { return R53_QFHA; }
		public void setR53_QFHA(BigDecimal r53_QFHA) { R53_QFHA = r53_QFHA; }
		public String getR54_PRODUCT() { return R54_PRODUCT; }
		public void setR54_PRODUCT(String r54_PRODUCT) { R54_PRODUCT = r54_PRODUCT; }
		public BigDecimal getR54_NV_LONG() { return R54_NV_LONG; }
		public void setR54_NV_LONG(BigDecimal r54_NV_LONG) { R54_NV_LONG = r54_NV_LONG; }
		public BigDecimal getR54_NV_SHORT() { return R54_NV_SHORT; }
		public void setR54_NV_SHORT(BigDecimal r54_NV_SHORT) { R54_NV_SHORT = r54_NV_SHORT; }
		public BigDecimal getR54_FV_LONG() { return R54_FV_LONG; }
		public void setR54_FV_LONG(BigDecimal r54_FV_LONG) { R54_FV_LONG = r54_FV_LONG; }
		public BigDecimal getR54_FV_SHORT() { return R54_FV_SHORT; }
		public void setR54_FV_SHORT(BigDecimal r54_FV_SHORT) { R54_FV_SHORT = r54_FV_SHORT; }
		public BigDecimal getR54_QFHA() { return R54_QFHA; }
		public void setR54_QFHA(BigDecimal r54_QFHA) { R54_QFHA = r54_QFHA; }
		public String getR55_PRODUCT() { return R55_PRODUCT; }
		public void setR55_PRODUCT(String r55_PRODUCT) { R55_PRODUCT = r55_PRODUCT; }
		public BigDecimal getR55_NV_LONG() { return R55_NV_LONG; }
		public void setR55_NV_LONG(BigDecimal r55_NV_LONG) { R55_NV_LONG = r55_NV_LONG; }
		public BigDecimal getR55_NV_SHORT() { return R55_NV_SHORT; }
		public void setR55_NV_SHORT(BigDecimal r55_NV_SHORT) { R55_NV_SHORT = r55_NV_SHORT; }
		public BigDecimal getR55_FV_LONG() { return R55_FV_LONG; }
		public void setR55_FV_LONG(BigDecimal r55_FV_LONG) { R55_FV_LONG = r55_FV_LONG; }
		public BigDecimal getR55_FV_SHORT() { return R55_FV_SHORT; }
		public void setR55_FV_SHORT(BigDecimal r55_FV_SHORT) { R55_FV_SHORT = r55_FV_SHORT; }
		public BigDecimal getR55_QFHA() { return R55_QFHA; }
		public void setR55_QFHA(BigDecimal r55_QFHA) { R55_QFHA = r55_QFHA; }
		public Date getReportDate() { return reportDate; }
		public void setReportDate(Date reportDate) { this.reportDate = reportDate; }
		public BigDecimal getReportVersion() { return reportVersion; }
		public void setReportVersion(BigDecimal reportVersion) { this.reportVersion = reportVersion; }
		public String getREPORT_FREQUENCY() { return REPORT_FREQUENCY; }
		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) { REPORT_FREQUENCY = rEPORT_FREQUENCY; }
		public String getREPORT_CODE() { return REPORT_CODE; }
		public void setREPORT_CODE(String rEPORT_CODE) { REPORT_CODE = rEPORT_CODE; }
		public String getREPORT_DESC() { return REPORT_DESC; }
		public void setREPORT_DESC(String rEPORT_DESC) { REPORT_DESC = rEPORT_DESC; }
		public String getENTITY_FLG() { return ENTITY_FLG; }
		public void setENTITY_FLG(String eNTITY_FLG) { ENTITY_FLG = eNTITY_FLG; }
		public String getMODIFY_FLG() { return MODIFY_FLG; }
		public void setMODIFY_FLG(String mODIFY_FLG) { MODIFY_FLG = mODIFY_FLG; }
		public String getDEL_FLG() { return DEL_FLG; }
		public void setDEL_FLG(String dEL_FLG) { DEL_FLG = dEL_FLG; }
		public Date getReportResubDate() { return reportResubDate; }
		public void setReportResubDate(Date reportResubDate) { this.reportResubDate = reportResubDate; }
	}

	// =========================================================
	// Inner entity: M_TBS_Resub_Detail_Entity
	// =========================================================
	public static class M_TBS_Resub_Detail_Entity {
		private String R11_PRODUCT;
		private BigDecimal R11_NV_LONG;
		private BigDecimal R11_NV_SHORT;
		private BigDecimal R11_FV_LONG;
		private BigDecimal R11_FV_SHORT;
		private BigDecimal R11_QFHA;
		private String R12_PRODUCT;
		private BigDecimal R12_NV_LONG;
		private BigDecimal R12_NV_SHORT;
		private BigDecimal R12_FV_LONG;
		private BigDecimal R12_FV_SHORT;
		private BigDecimal R12_QFHA;
		private String R13_PRODUCT;
		private BigDecimal R13_NV_LONG;
		private BigDecimal R13_NV_SHORT;
		private BigDecimal R13_FV_LONG;
		private BigDecimal R13_FV_SHORT;
		private BigDecimal R13_QFHA;
		private String R14_PRODUCT;
		private BigDecimal R14_NV_LONG;
		private BigDecimal R14_NV_SHORT;
		private BigDecimal R14_FV_LONG;
		private BigDecimal R14_FV_SHORT;
		private BigDecimal R14_QFHA;
		private String R15_PRODUCT;
		private BigDecimal R15_NV_LONG;
		private BigDecimal R15_NV_SHORT;
		private BigDecimal R15_FV_LONG;
		private BigDecimal R15_FV_SHORT;
		private BigDecimal R15_QFHA;
		private String R16_PRODUCT;
		private BigDecimal R16_NV_LONG;
		private BigDecimal R16_NV_SHORT;
		private BigDecimal R16_FV_LONG;
		private BigDecimal R16_FV_SHORT;
		private BigDecimal R16_QFHA;
		private String R17_PRODUCT;
		private BigDecimal R17_NV_LONG;
		private BigDecimal R17_NV_SHORT;
		private BigDecimal R17_FV_LONG;
		private BigDecimal R17_FV_SHORT;
		private BigDecimal R17_QFHA;
		private String R18_PRODUCT;
		private BigDecimal R18_NV_LONG;
		private BigDecimal R18_NV_SHORT;
		private BigDecimal R18_FV_LONG;
		private BigDecimal R18_FV_SHORT;
		private BigDecimal R18_QFHA;
		private String R19_PRODUCT;
		private BigDecimal R19_NV_LONG;
		private BigDecimal R19_NV_SHORT;
		private BigDecimal R19_FV_LONG;
		private BigDecimal R19_FV_SHORT;
		private BigDecimal R19_QFHA;
		private String R20_PRODUCT;
		private BigDecimal R20_NV_LONG;
		private BigDecimal R20_NV_SHORT;
		private BigDecimal R20_FV_LONG;
		private BigDecimal R20_FV_SHORT;
		private BigDecimal R20_QFHA;
		private String R21_PRODUCT;
		private BigDecimal R21_NV_LONG;
		private BigDecimal R21_NV_SHORT;
		private BigDecimal R21_FV_LONG;
		private BigDecimal R21_FV_SHORT;
		private BigDecimal R21_QFHA;
		private String R22_PRODUCT;
		private BigDecimal R22_NV_LONG;
		private BigDecimal R22_NV_SHORT;
		private BigDecimal R22_FV_LONG;
		private BigDecimal R22_FV_SHORT;
		private BigDecimal R22_QFHA;
		private String R23_PRODUCT;
		private BigDecimal R23_NV_LONG;
		private BigDecimal R23_NV_SHORT;
		private BigDecimal R23_FV_LONG;
		private BigDecimal R23_FV_SHORT;
		private BigDecimal R23_QFHA;
		private String R24_PRODUCT;
		private BigDecimal R24_NV_LONG;
		private BigDecimal R24_NV_SHORT;
		private BigDecimal R24_FV_LONG;
		private BigDecimal R24_FV_SHORT;
		private BigDecimal R24_QFHA;
		private String R25_PRODUCT;
		private BigDecimal R25_NV_LONG;
		private BigDecimal R25_NV_SHORT;
		private BigDecimal R25_FV_LONG;
		private BigDecimal R25_FV_SHORT;
		private BigDecimal R25_QFHA;
		private String R26_PRODUCT;
		private BigDecimal R26_NV_LONG;
		private BigDecimal R26_NV_SHORT;
		private BigDecimal R26_FV_LONG;
		private BigDecimal R26_FV_SHORT;
		private BigDecimal R26_QFHA;
		private String R27_PRODUCT;
		private BigDecimal R27_NV_LONG;
		private BigDecimal R27_NV_SHORT;
		private BigDecimal R27_FV_LONG;
		private BigDecimal R27_FV_SHORT;
		private BigDecimal R27_QFHA;
		private String R28_PRODUCT;
		private BigDecimal R28_NV_LONG;
		private BigDecimal R28_NV_SHORT;
		private BigDecimal R28_FV_LONG;
		private BigDecimal R28_FV_SHORT;
		private BigDecimal R28_QFHA;
		private String R29_PRODUCT;
		private BigDecimal R29_NV_LONG;
		private BigDecimal R29_NV_SHORT;
		private BigDecimal R29_FV_LONG;
		private BigDecimal R29_FV_SHORT;
		private BigDecimal R29_QFHA;
		private String R30_PRODUCT;
		private BigDecimal R30_NV_LONG;
		private BigDecimal R30_NV_SHORT;
		private BigDecimal R30_FV_LONG;
		private BigDecimal R30_FV_SHORT;
		private BigDecimal R30_QFHA;
		private String R31_PRODUCT;
		private BigDecimal R31_NV_LONG;
		private BigDecimal R31_NV_SHORT;
		private BigDecimal R31_FV_LONG;
		private BigDecimal R31_FV_SHORT;
		private BigDecimal R31_QFHA;
		private String R32_PRODUCT;
		private BigDecimal R32_NV_LONG;
		private BigDecimal R32_NV_SHORT;
		private BigDecimal R32_FV_LONG;
		private BigDecimal R32_FV_SHORT;
		private BigDecimal R32_QFHA;
		private String R33_PRODUCT;
		private BigDecimal R33_NV_LONG;
		private BigDecimal R33_NV_SHORT;
		private BigDecimal R33_FV_LONG;
		private BigDecimal R33_FV_SHORT;
		private BigDecimal R33_QFHA;
		private String R34_PRODUCT;
		private BigDecimal R34_NV_LONG;
		private BigDecimal R34_NV_SHORT;
		private BigDecimal R34_FV_LONG;
		private BigDecimal R34_FV_SHORT;
		private BigDecimal R34_QFHA;
		private String R35_PRODUCT;
		private BigDecimal R35_NV_LONG;
		private BigDecimal R35_NV_SHORT;
		private BigDecimal R35_FV_LONG;
		private BigDecimal R35_FV_SHORT;
		private BigDecimal R35_QFHA;
		private String R36_PRODUCT;
		private BigDecimal R36_NV_LONG;
		private BigDecimal R36_NV_SHORT;
		private BigDecimal R36_FV_LONG;
		private BigDecimal R36_FV_SHORT;
		private BigDecimal R36_QFHA;
		private String R37_PRODUCT;
		private BigDecimal R37_NV_LONG;
		private BigDecimal R37_NV_SHORT;
		private BigDecimal R37_FV_LONG;
		private BigDecimal R37_FV_SHORT;
		private BigDecimal R37_QFHA;
		private String R38_PRODUCT;
		private BigDecimal R38_NV_LONG;
		private BigDecimal R38_NV_SHORT;
		private BigDecimal R38_FV_LONG;
		private BigDecimal R38_FV_SHORT;
		private BigDecimal R38_QFHA;
		private String R39_PRODUCT;
		private BigDecimal R39_NV_LONG;
		private BigDecimal R39_NV_SHORT;
		private BigDecimal R39_FV_LONG;
		private BigDecimal R39_FV_SHORT;
		private BigDecimal R39_QFHA;
		private String R40_PRODUCT;
		private BigDecimal R40_NV_LONG;
		private BigDecimal R40_NV_SHORT;
		private BigDecimal R40_FV_LONG;
		private BigDecimal R40_FV_SHORT;
		private BigDecimal R40_QFHA;
		private String R41_PRODUCT;
		private BigDecimal R41_NV_LONG;
		private BigDecimal R41_NV_SHORT;
		private BigDecimal R41_FV_LONG;
		private BigDecimal R41_FV_SHORT;
		private BigDecimal R41_QFHA;
		private String R42_PRODUCT;
		private BigDecimal R42_NV_LONG;
		private BigDecimal R42_NV_SHORT;
		private BigDecimal R42_FV_LONG;
		private BigDecimal R42_FV_SHORT;
		private BigDecimal R42_QFHA;
		private String R43_PRODUCT;
		private BigDecimal R43_NV_LONG;
		private BigDecimal R43_NV_SHORT;
		private BigDecimal R43_FV_LONG;
		private BigDecimal R43_FV_SHORT;
		private BigDecimal R43_QFHA;
		private String R44_PRODUCT;
		private BigDecimal R44_NV_LONG;
		private BigDecimal R44_NV_SHORT;
		private BigDecimal R44_FV_LONG;
		private BigDecimal R44_FV_SHORT;
		private BigDecimal R44_QFHA;
		private String R45_PRODUCT;
		private BigDecimal R45_NV_LONG;
		private BigDecimal R45_NV_SHORT;
		private BigDecimal R45_FV_LONG;
		private BigDecimal R45_FV_SHORT;
		private BigDecimal R45_QFHA;
		private String R46_PRODUCT;
		private BigDecimal R46_NV_LONG;
		private BigDecimal R46_NV_SHORT;
		private BigDecimal R46_FV_LONG;
		private BigDecimal R46_FV_SHORT;
		private BigDecimal R46_QFHA;
		private String R47_PRODUCT;
		private BigDecimal R47_NV_LONG;
		private BigDecimal R47_NV_SHORT;
		private BigDecimal R47_FV_LONG;
		private BigDecimal R47_FV_SHORT;
		private BigDecimal R47_QFHA;
		private String R48_PRODUCT;
		private BigDecimal R48_NV_LONG;
		private BigDecimal R48_NV_SHORT;
		private BigDecimal R48_FV_LONG;
		private BigDecimal R48_FV_SHORT;
		private BigDecimal R48_QFHA;
		private String R49_PRODUCT;
		private BigDecimal R49_NV_LONG;
		private BigDecimal R49_NV_SHORT;
		private BigDecimal R49_FV_LONG;
		private BigDecimal R49_FV_SHORT;
		private BigDecimal R49_QFHA;
		private String R50_PRODUCT;
		private BigDecimal R50_NV_LONG;
		private BigDecimal R50_NV_SHORT;
		private BigDecimal R50_FV_LONG;
		private BigDecimal R50_FV_SHORT;
		private BigDecimal R50_QFHA;
		private String R51_PRODUCT;
		private BigDecimal R51_NV_LONG;
		private BigDecimal R51_NV_SHORT;
		private BigDecimal R51_FV_LONG;
		private BigDecimal R51_FV_SHORT;
		private BigDecimal R51_QFHA;
		private String R52_PRODUCT;
		private BigDecimal R52_NV_LONG;
		private BigDecimal R52_NV_SHORT;
		private BigDecimal R52_FV_LONG;
		private BigDecimal R52_FV_SHORT;
		private BigDecimal R52_QFHA;
		private String R53_PRODUCT;
		private BigDecimal R53_NV_LONG;
		private BigDecimal R53_NV_SHORT;
		private BigDecimal R53_FV_LONG;
		private BigDecimal R53_FV_SHORT;
		private BigDecimal R53_QFHA;
		private String R54_PRODUCT;
		private BigDecimal R54_NV_LONG;
		private BigDecimal R54_NV_SHORT;
		private BigDecimal R54_FV_LONG;
		private BigDecimal R54_FV_SHORT;
		private BigDecimal R54_QFHA;
		private String R55_PRODUCT;
		private BigDecimal R55_NV_LONG;
		private BigDecimal R55_NV_SHORT;
		private BigDecimal R55_FV_LONG;
		private BigDecimal R55_FV_SHORT;
		private BigDecimal R55_QFHA;

		private Date reportDate;
		private BigDecimal reportVersion;
		public String REPORT_FREQUENCY;
		public String REPORT_CODE;
		public String REPORT_DESC;
		public String ENTITY_FLG;
		public String MODIFY_FLG;
		public String DEL_FLG;
		private Date reportResubDate;

		public String getR11_PRODUCT() { return R11_PRODUCT; }
		public void setR11_PRODUCT(String r11_PRODUCT) { R11_PRODUCT = r11_PRODUCT; }
		public BigDecimal getR11_NV_LONG() { return R11_NV_LONG; }
		public void setR11_NV_LONG(BigDecimal r11_NV_LONG) { R11_NV_LONG = r11_NV_LONG; }
		public BigDecimal getR11_NV_SHORT() { return R11_NV_SHORT; }
		public void setR11_NV_SHORT(BigDecimal r11_NV_SHORT) { R11_NV_SHORT = r11_NV_SHORT; }
		public BigDecimal getR11_FV_LONG() { return R11_FV_LONG; }
		public void setR11_FV_LONG(BigDecimal r11_FV_LONG) { R11_FV_LONG = r11_FV_LONG; }
		public BigDecimal getR11_FV_SHORT() { return R11_FV_SHORT; }
		public void setR11_FV_SHORT(BigDecimal r11_FV_SHORT) { R11_FV_SHORT = r11_FV_SHORT; }
		public BigDecimal getR11_QFHA() { return R11_QFHA; }
		public void setR11_QFHA(BigDecimal r11_QFHA) { R11_QFHA = r11_QFHA; }
		public String getR12_PRODUCT() { return R12_PRODUCT; }
		public void setR12_PRODUCT(String r12_PRODUCT) { R12_PRODUCT = r12_PRODUCT; }
		public BigDecimal getR12_NV_LONG() { return R12_NV_LONG; }
		public void setR12_NV_LONG(BigDecimal r12_NV_LONG) { R12_NV_LONG = r12_NV_LONG; }
		public BigDecimal getR12_NV_SHORT() { return R12_NV_SHORT; }
		public void setR12_NV_SHORT(BigDecimal r12_NV_SHORT) { R12_NV_SHORT = r12_NV_SHORT; }
		public BigDecimal getR12_FV_LONG() { return R12_FV_LONG; }
		public void setR12_FV_LONG(BigDecimal r12_FV_LONG) { R12_FV_LONG = r12_FV_LONG; }
		public BigDecimal getR12_FV_SHORT() { return R12_FV_SHORT; }
		public void setR12_FV_SHORT(BigDecimal r12_FV_SHORT) { R12_FV_SHORT = r12_FV_SHORT; }
		public BigDecimal getR12_QFHA() { return R12_QFHA; }
		public void setR12_QFHA(BigDecimal r12_QFHA) { R12_QFHA = r12_QFHA; }
		public String getR13_PRODUCT() { return R13_PRODUCT; }
		public void setR13_PRODUCT(String r13_PRODUCT) { R13_PRODUCT = r13_PRODUCT; }
		public BigDecimal getR13_NV_LONG() { return R13_NV_LONG; }
		public void setR13_NV_LONG(BigDecimal r13_NV_LONG) { R13_NV_LONG = r13_NV_LONG; }
		public BigDecimal getR13_NV_SHORT() { return R13_NV_SHORT; }
		public void setR13_NV_SHORT(BigDecimal r13_NV_SHORT) { R13_NV_SHORT = r13_NV_SHORT; }
		public BigDecimal getR13_FV_LONG() { return R13_FV_LONG; }
		public void setR13_FV_LONG(BigDecimal r13_FV_LONG) { R13_FV_LONG = r13_FV_LONG; }
		public BigDecimal getR13_FV_SHORT() { return R13_FV_SHORT; }
		public void setR13_FV_SHORT(BigDecimal r13_FV_SHORT) { R13_FV_SHORT = r13_FV_SHORT; }
		public BigDecimal getR13_QFHA() { return R13_QFHA; }
		public void setR13_QFHA(BigDecimal r13_QFHA) { R13_QFHA = r13_QFHA; }
		public String getR14_PRODUCT() { return R14_PRODUCT; }
		public void setR14_PRODUCT(String r14_PRODUCT) { R14_PRODUCT = r14_PRODUCT; }
		public BigDecimal getR14_NV_LONG() { return R14_NV_LONG; }
		public void setR14_NV_LONG(BigDecimal r14_NV_LONG) { R14_NV_LONG = r14_NV_LONG; }
		public BigDecimal getR14_NV_SHORT() { return R14_NV_SHORT; }
		public void setR14_NV_SHORT(BigDecimal r14_NV_SHORT) { R14_NV_SHORT = r14_NV_SHORT; }
		public BigDecimal getR14_FV_LONG() { return R14_FV_LONG; }
		public void setR14_FV_LONG(BigDecimal r14_FV_LONG) { R14_FV_LONG = r14_FV_LONG; }
		public BigDecimal getR14_FV_SHORT() { return R14_FV_SHORT; }
		public void setR14_FV_SHORT(BigDecimal r14_FV_SHORT) { R14_FV_SHORT = r14_FV_SHORT; }
		public BigDecimal getR14_QFHA() { return R14_QFHA; }
		public void setR14_QFHA(BigDecimal r14_QFHA) { R14_QFHA = r14_QFHA; }
		public String getR15_PRODUCT() { return R15_PRODUCT; }
		public void setR15_PRODUCT(String r15_PRODUCT) { R15_PRODUCT = r15_PRODUCT; }
		public BigDecimal getR15_NV_LONG() { return R15_NV_LONG; }
		public void setR15_NV_LONG(BigDecimal r15_NV_LONG) { R15_NV_LONG = r15_NV_LONG; }
		public BigDecimal getR15_NV_SHORT() { return R15_NV_SHORT; }
		public void setR15_NV_SHORT(BigDecimal r15_NV_SHORT) { R15_NV_SHORT = r15_NV_SHORT; }
		public BigDecimal getR15_FV_LONG() { return R15_FV_LONG; }
		public void setR15_FV_LONG(BigDecimal r15_FV_LONG) { R15_FV_LONG = r15_FV_LONG; }
		public BigDecimal getR15_FV_SHORT() { return R15_FV_SHORT; }
		public void setR15_FV_SHORT(BigDecimal r15_FV_SHORT) { R15_FV_SHORT = r15_FV_SHORT; }
		public BigDecimal getR15_QFHA() { return R15_QFHA; }
		public void setR15_QFHA(BigDecimal r15_QFHA) { R15_QFHA = r15_QFHA; }
		public String getR16_PRODUCT() { return R16_PRODUCT; }
		public void setR16_PRODUCT(String r16_PRODUCT) { R16_PRODUCT = r16_PRODUCT; }
		public BigDecimal getR16_NV_LONG() { return R16_NV_LONG; }
		public void setR16_NV_LONG(BigDecimal r16_NV_LONG) { R16_NV_LONG = r16_NV_LONG; }
		public BigDecimal getR16_NV_SHORT() { return R16_NV_SHORT; }
		public void setR16_NV_SHORT(BigDecimal r16_NV_SHORT) { R16_NV_SHORT = r16_NV_SHORT; }
		public BigDecimal getR16_FV_LONG() { return R16_FV_LONG; }
		public void setR16_FV_LONG(BigDecimal r16_FV_LONG) { R16_FV_LONG = r16_FV_LONG; }
		public BigDecimal getR16_FV_SHORT() { return R16_FV_SHORT; }
		public void setR16_FV_SHORT(BigDecimal r16_FV_SHORT) { R16_FV_SHORT = r16_FV_SHORT; }
		public BigDecimal getR16_QFHA() { return R16_QFHA; }
		public void setR16_QFHA(BigDecimal r16_QFHA) { R16_QFHA = r16_QFHA; }
		public String getR17_PRODUCT() { return R17_PRODUCT; }
		public void setR17_PRODUCT(String r17_PRODUCT) { R17_PRODUCT = r17_PRODUCT; }
		public BigDecimal getR17_NV_LONG() { return R17_NV_LONG; }
		public void setR17_NV_LONG(BigDecimal r17_NV_LONG) { R17_NV_LONG = r17_NV_LONG; }
		public BigDecimal getR17_NV_SHORT() { return R17_NV_SHORT; }
		public void setR17_NV_SHORT(BigDecimal r17_NV_SHORT) { R17_NV_SHORT = r17_NV_SHORT; }
		public BigDecimal getR17_FV_LONG() { return R17_FV_LONG; }
		public void setR17_FV_LONG(BigDecimal r17_FV_LONG) { R17_FV_LONG = r17_FV_LONG; }
		public BigDecimal getR17_FV_SHORT() { return R17_FV_SHORT; }
		public void setR17_FV_SHORT(BigDecimal r17_FV_SHORT) { R17_FV_SHORT = r17_FV_SHORT; }
		public BigDecimal getR17_QFHA() { return R17_QFHA; }
		public void setR17_QFHA(BigDecimal r17_QFHA) { R17_QFHA = r17_QFHA; }
		public String getR18_PRODUCT() { return R18_PRODUCT; }
		public void setR18_PRODUCT(String r18_PRODUCT) { R18_PRODUCT = r18_PRODUCT; }
		public BigDecimal getR18_NV_LONG() { return R18_NV_LONG; }
		public void setR18_NV_LONG(BigDecimal r18_NV_LONG) { R18_NV_LONG = r18_NV_LONG; }
		public BigDecimal getR18_NV_SHORT() { return R18_NV_SHORT; }
		public void setR18_NV_SHORT(BigDecimal r18_NV_SHORT) { R18_NV_SHORT = r18_NV_SHORT; }
		public BigDecimal getR18_FV_LONG() { return R18_FV_LONG; }
		public void setR18_FV_LONG(BigDecimal r18_FV_LONG) { R18_FV_LONG = r18_FV_LONG; }
		public BigDecimal getR18_FV_SHORT() { return R18_FV_SHORT; }
		public void setR18_FV_SHORT(BigDecimal r18_FV_SHORT) { R18_FV_SHORT = r18_FV_SHORT; }
		public BigDecimal getR18_QFHA() { return R18_QFHA; }
		public void setR18_QFHA(BigDecimal r18_QFHA) { R18_QFHA = r18_QFHA; }
		public String getR19_PRODUCT() { return R19_PRODUCT; }
		public void setR19_PRODUCT(String r19_PRODUCT) { R19_PRODUCT = r19_PRODUCT; }
		public BigDecimal getR19_NV_LONG() { return R19_NV_LONG; }
		public void setR19_NV_LONG(BigDecimal r19_NV_LONG) { R19_NV_LONG = r19_NV_LONG; }
		public BigDecimal getR19_NV_SHORT() { return R19_NV_SHORT; }
		public void setR19_NV_SHORT(BigDecimal r19_NV_SHORT) { R19_NV_SHORT = r19_NV_SHORT; }
		public BigDecimal getR19_FV_LONG() { return R19_FV_LONG; }
		public void setR19_FV_LONG(BigDecimal r19_FV_LONG) { R19_FV_LONG = r19_FV_LONG; }
		public BigDecimal getR19_FV_SHORT() { return R19_FV_SHORT; }
		public void setR19_FV_SHORT(BigDecimal r19_FV_SHORT) { R19_FV_SHORT = r19_FV_SHORT; }
		public BigDecimal getR19_QFHA() { return R19_QFHA; }
		public void setR19_QFHA(BigDecimal r19_QFHA) { R19_QFHA = r19_QFHA; }
		public String getR20_PRODUCT() { return R20_PRODUCT; }
		public void setR20_PRODUCT(String r20_PRODUCT) { R20_PRODUCT = r20_PRODUCT; }
		public BigDecimal getR20_NV_LONG() { return R20_NV_LONG; }
		public void setR20_NV_LONG(BigDecimal r20_NV_LONG) { R20_NV_LONG = r20_NV_LONG; }
		public BigDecimal getR20_NV_SHORT() { return R20_NV_SHORT; }
		public void setR20_NV_SHORT(BigDecimal r20_NV_SHORT) { R20_NV_SHORT = r20_NV_SHORT; }
		public BigDecimal getR20_FV_LONG() { return R20_FV_LONG; }
		public void setR20_FV_LONG(BigDecimal r20_FV_LONG) { R20_FV_LONG = r20_FV_LONG; }
		public BigDecimal getR20_FV_SHORT() { return R20_FV_SHORT; }
		public void setR20_FV_SHORT(BigDecimal r20_FV_SHORT) { R20_FV_SHORT = r20_FV_SHORT; }
		public BigDecimal getR20_QFHA() { return R20_QFHA; }
		public void setR20_QFHA(BigDecimal r20_QFHA) { R20_QFHA = r20_QFHA; }
		public String getR21_PRODUCT() { return R21_PRODUCT; }
		public void setR21_PRODUCT(String r21_PRODUCT) { R21_PRODUCT = r21_PRODUCT; }
		public BigDecimal getR21_NV_LONG() { return R21_NV_LONG; }
		public void setR21_NV_LONG(BigDecimal r21_NV_LONG) { R21_NV_LONG = r21_NV_LONG; }
		public BigDecimal getR21_NV_SHORT() { return R21_NV_SHORT; }
		public void setR21_NV_SHORT(BigDecimal r21_NV_SHORT) { R21_NV_SHORT = r21_NV_SHORT; }
		public BigDecimal getR21_FV_LONG() { return R21_FV_LONG; }
		public void setR21_FV_LONG(BigDecimal r21_FV_LONG) { R21_FV_LONG = r21_FV_LONG; }
		public BigDecimal getR21_FV_SHORT() { return R21_FV_SHORT; }
		public void setR21_FV_SHORT(BigDecimal r21_FV_SHORT) { R21_FV_SHORT = r21_FV_SHORT; }
		public BigDecimal getR21_QFHA() { return R21_QFHA; }
		public void setR21_QFHA(BigDecimal r21_QFHA) { R21_QFHA = r21_QFHA; }
		public String getR22_PRODUCT() { return R22_PRODUCT; }
		public void setR22_PRODUCT(String r22_PRODUCT) { R22_PRODUCT = r22_PRODUCT; }
		public BigDecimal getR22_NV_LONG() { return R22_NV_LONG; }
		public void setR22_NV_LONG(BigDecimal r22_NV_LONG) { R22_NV_LONG = r22_NV_LONG; }
		public BigDecimal getR22_NV_SHORT() { return R22_NV_SHORT; }
		public void setR22_NV_SHORT(BigDecimal r22_NV_SHORT) { R22_NV_SHORT = r22_NV_SHORT; }
		public BigDecimal getR22_FV_LONG() { return R22_FV_LONG; }
		public void setR22_FV_LONG(BigDecimal r22_FV_LONG) { R22_FV_LONG = r22_FV_LONG; }
		public BigDecimal getR22_FV_SHORT() { return R22_FV_SHORT; }
		public void setR22_FV_SHORT(BigDecimal r22_FV_SHORT) { R22_FV_SHORT = r22_FV_SHORT; }
		public BigDecimal getR22_QFHA() { return R22_QFHA; }
		public void setR22_QFHA(BigDecimal r22_QFHA) { R22_QFHA = r22_QFHA; }
		public String getR23_PRODUCT() { return R23_PRODUCT; }
		public void setR23_PRODUCT(String r23_PRODUCT) { R23_PRODUCT = r23_PRODUCT; }
		public BigDecimal getR23_NV_LONG() { return R23_NV_LONG; }
		public void setR23_NV_LONG(BigDecimal r23_NV_LONG) { R23_NV_LONG = r23_NV_LONG; }
		public BigDecimal getR23_NV_SHORT() { return R23_NV_SHORT; }
		public void setR23_NV_SHORT(BigDecimal r23_NV_SHORT) { R23_NV_SHORT = r23_NV_SHORT; }
		public BigDecimal getR23_FV_LONG() { return R23_FV_LONG; }
		public void setR23_FV_LONG(BigDecimal r23_FV_LONG) { R23_FV_LONG = r23_FV_LONG; }
		public BigDecimal getR23_FV_SHORT() { return R23_FV_SHORT; }
		public void setR23_FV_SHORT(BigDecimal r23_FV_SHORT) { R23_FV_SHORT = r23_FV_SHORT; }
		public BigDecimal getR23_QFHA() { return R23_QFHA; }
		public void setR23_QFHA(BigDecimal r23_QFHA) { R23_QFHA = r23_QFHA; }
		public String getR24_PRODUCT() { return R24_PRODUCT; }
		public void setR24_PRODUCT(String r24_PRODUCT) { R24_PRODUCT = r24_PRODUCT; }
		public BigDecimal getR24_NV_LONG() { return R24_NV_LONG; }
		public void setR24_NV_LONG(BigDecimal r24_NV_LONG) { R24_NV_LONG = r24_NV_LONG; }
		public BigDecimal getR24_NV_SHORT() { return R24_NV_SHORT; }
		public void setR24_NV_SHORT(BigDecimal r24_NV_SHORT) { R24_NV_SHORT = r24_NV_SHORT; }
		public BigDecimal getR24_FV_LONG() { return R24_FV_LONG; }
		public void setR24_FV_LONG(BigDecimal r24_FV_LONG) { R24_FV_LONG = r24_FV_LONG; }
		public BigDecimal getR24_FV_SHORT() { return R24_FV_SHORT; }
		public void setR24_FV_SHORT(BigDecimal r24_FV_SHORT) { R24_FV_SHORT = r24_FV_SHORT; }
		public BigDecimal getR24_QFHA() { return R24_QFHA; }
		public void setR24_QFHA(BigDecimal r24_QFHA) { R24_QFHA = r24_QFHA; }
		public String getR25_PRODUCT() { return R25_PRODUCT; }
		public void setR25_PRODUCT(String r25_PRODUCT) { R25_PRODUCT = r25_PRODUCT; }
		public BigDecimal getR25_NV_LONG() { return R25_NV_LONG; }
		public void setR25_NV_LONG(BigDecimal r25_NV_LONG) { R25_NV_LONG = r25_NV_LONG; }
		public BigDecimal getR25_NV_SHORT() { return R25_NV_SHORT; }
		public void setR25_NV_SHORT(BigDecimal r25_NV_SHORT) { R25_NV_SHORT = r25_NV_SHORT; }
		public BigDecimal getR25_FV_LONG() { return R25_FV_LONG; }
		public void setR25_FV_LONG(BigDecimal r25_FV_LONG) { R25_FV_LONG = r25_FV_LONG; }
		public BigDecimal getR25_FV_SHORT() { return R25_FV_SHORT; }
		public void setR25_FV_SHORT(BigDecimal r25_FV_SHORT) { R25_FV_SHORT = r25_FV_SHORT; }
		public BigDecimal getR25_QFHA() { return R25_QFHA; }
		public void setR25_QFHA(BigDecimal r25_QFHA) { R25_QFHA = r25_QFHA; }
		public String getR26_PRODUCT() { return R26_PRODUCT; }
		public void setR26_PRODUCT(String r26_PRODUCT) { R26_PRODUCT = r26_PRODUCT; }
		public BigDecimal getR26_NV_LONG() { return R26_NV_LONG; }
		public void setR26_NV_LONG(BigDecimal r26_NV_LONG) { R26_NV_LONG = r26_NV_LONG; }
		public BigDecimal getR26_NV_SHORT() { return R26_NV_SHORT; }
		public void setR26_NV_SHORT(BigDecimal r26_NV_SHORT) { R26_NV_SHORT = r26_NV_SHORT; }
		public BigDecimal getR26_FV_LONG() { return R26_FV_LONG; }
		public void setR26_FV_LONG(BigDecimal r26_FV_LONG) { R26_FV_LONG = r26_FV_LONG; }
		public BigDecimal getR26_FV_SHORT() { return R26_FV_SHORT; }
		public void setR26_FV_SHORT(BigDecimal r26_FV_SHORT) { R26_FV_SHORT = r26_FV_SHORT; }
		public BigDecimal getR26_QFHA() { return R26_QFHA; }
		public void setR26_QFHA(BigDecimal r26_QFHA) { R26_QFHA = r26_QFHA; }
		public String getR27_PRODUCT() { return R27_PRODUCT; }
		public void setR27_PRODUCT(String r27_PRODUCT) { R27_PRODUCT = r27_PRODUCT; }
		public BigDecimal getR27_NV_LONG() { return R27_NV_LONG; }
		public void setR27_NV_LONG(BigDecimal r27_NV_LONG) { R27_NV_LONG = r27_NV_LONG; }
		public BigDecimal getR27_NV_SHORT() { return R27_NV_SHORT; }
		public void setR27_NV_SHORT(BigDecimal r27_NV_SHORT) { R27_NV_SHORT = r27_NV_SHORT; }
		public BigDecimal getR27_FV_LONG() { return R27_FV_LONG; }
		public void setR27_FV_LONG(BigDecimal r27_FV_LONG) { R27_FV_LONG = r27_FV_LONG; }
		public BigDecimal getR27_FV_SHORT() { return R27_FV_SHORT; }
		public void setR27_FV_SHORT(BigDecimal r27_FV_SHORT) { R27_FV_SHORT = r27_FV_SHORT; }
		public BigDecimal getR27_QFHA() { return R27_QFHA; }
		public void setR27_QFHA(BigDecimal r27_QFHA) { R27_QFHA = r27_QFHA; }
		public String getR28_PRODUCT() { return R28_PRODUCT; }
		public void setR28_PRODUCT(String r28_PRODUCT) { R28_PRODUCT = r28_PRODUCT; }
		public BigDecimal getR28_NV_LONG() { return R28_NV_LONG; }
		public void setR28_NV_LONG(BigDecimal r28_NV_LONG) { R28_NV_LONG = r28_NV_LONG; }
		public BigDecimal getR28_NV_SHORT() { return R28_NV_SHORT; }
		public void setR28_NV_SHORT(BigDecimal r28_NV_SHORT) { R28_NV_SHORT = r28_NV_SHORT; }
		public BigDecimal getR28_FV_LONG() { return R28_FV_LONG; }
		public void setR28_FV_LONG(BigDecimal r28_FV_LONG) { R28_FV_LONG = r28_FV_LONG; }
		public BigDecimal getR28_FV_SHORT() { return R28_FV_SHORT; }
		public void setR28_FV_SHORT(BigDecimal r28_FV_SHORT) { R28_FV_SHORT = r28_FV_SHORT; }
		public BigDecimal getR28_QFHA() { return R28_QFHA; }
		public void setR28_QFHA(BigDecimal r28_QFHA) { R28_QFHA = r28_QFHA; }
		public String getR29_PRODUCT() { return R29_PRODUCT; }
		public void setR29_PRODUCT(String r29_PRODUCT) { R29_PRODUCT = r29_PRODUCT; }
		public BigDecimal getR29_NV_LONG() { return R29_NV_LONG; }
		public void setR29_NV_LONG(BigDecimal r29_NV_LONG) { R29_NV_LONG = r29_NV_LONG; }
		public BigDecimal getR29_NV_SHORT() { return R29_NV_SHORT; }
		public void setR29_NV_SHORT(BigDecimal r29_NV_SHORT) { R29_NV_SHORT = r29_NV_SHORT; }
		public BigDecimal getR29_FV_LONG() { return R29_FV_LONG; }
		public void setR29_FV_LONG(BigDecimal r29_FV_LONG) { R29_FV_LONG = r29_FV_LONG; }
		public BigDecimal getR29_FV_SHORT() { return R29_FV_SHORT; }
		public void setR29_FV_SHORT(BigDecimal r29_FV_SHORT) { R29_FV_SHORT = r29_FV_SHORT; }
		public BigDecimal getR29_QFHA() { return R29_QFHA; }
		public void setR29_QFHA(BigDecimal r29_QFHA) { R29_QFHA = r29_QFHA; }
		public String getR30_PRODUCT() { return R30_PRODUCT; }
		public void setR30_PRODUCT(String r30_PRODUCT) { R30_PRODUCT = r30_PRODUCT; }
		public BigDecimal getR30_NV_LONG() { return R30_NV_LONG; }
		public void setR30_NV_LONG(BigDecimal r30_NV_LONG) { R30_NV_LONG = r30_NV_LONG; }
		public BigDecimal getR30_NV_SHORT() { return R30_NV_SHORT; }
		public void setR30_NV_SHORT(BigDecimal r30_NV_SHORT) { R30_NV_SHORT = r30_NV_SHORT; }
		public BigDecimal getR30_FV_LONG() { return R30_FV_LONG; }
		public void setR30_FV_LONG(BigDecimal r30_FV_LONG) { R30_FV_LONG = r30_FV_LONG; }
		public BigDecimal getR30_FV_SHORT() { return R30_FV_SHORT; }
		public void setR30_FV_SHORT(BigDecimal r30_FV_SHORT) { R30_FV_SHORT = r30_FV_SHORT; }
		public BigDecimal getR30_QFHA() { return R30_QFHA; }
		public void setR30_QFHA(BigDecimal r30_QFHA) { R30_QFHA = r30_QFHA; }
		public String getR31_PRODUCT() { return R31_PRODUCT; }
		public void setR31_PRODUCT(String r31_PRODUCT) { R31_PRODUCT = r31_PRODUCT; }
		public BigDecimal getR31_NV_LONG() { return R31_NV_LONG; }
		public void setR31_NV_LONG(BigDecimal r31_NV_LONG) { R31_NV_LONG = r31_NV_LONG; }
		public BigDecimal getR31_NV_SHORT() { return R31_NV_SHORT; }
		public void setR31_NV_SHORT(BigDecimal r31_NV_SHORT) { R31_NV_SHORT = r31_NV_SHORT; }
		public BigDecimal getR31_FV_LONG() { return R31_FV_LONG; }
		public void setR31_FV_LONG(BigDecimal r31_FV_LONG) { R31_FV_LONG = r31_FV_LONG; }
		public BigDecimal getR31_FV_SHORT() { return R31_FV_SHORT; }
		public void setR31_FV_SHORT(BigDecimal r31_FV_SHORT) { R31_FV_SHORT = r31_FV_SHORT; }
		public BigDecimal getR31_QFHA() { return R31_QFHA; }
		public void setR31_QFHA(BigDecimal r31_QFHA) { R31_QFHA = r31_QFHA; }
		public String getR32_PRODUCT() { return R32_PRODUCT; }
		public void setR32_PRODUCT(String r32_PRODUCT) { R32_PRODUCT = r32_PRODUCT; }
		public BigDecimal getR32_NV_LONG() { return R32_NV_LONG; }
		public void setR32_NV_LONG(BigDecimal r32_NV_LONG) { R32_NV_LONG = r32_NV_LONG; }
		public BigDecimal getR32_NV_SHORT() { return R32_NV_SHORT; }
		public void setR32_NV_SHORT(BigDecimal r32_NV_SHORT) { R32_NV_SHORT = r32_NV_SHORT; }
		public BigDecimal getR32_FV_LONG() { return R32_FV_LONG; }
		public void setR32_FV_LONG(BigDecimal r32_FV_LONG) { R32_FV_LONG = r32_FV_LONG; }
		public BigDecimal getR32_FV_SHORT() { return R32_FV_SHORT; }
		public void setR32_FV_SHORT(BigDecimal r32_FV_SHORT) { R32_FV_SHORT = r32_FV_SHORT; }
		public BigDecimal getR32_QFHA() { return R32_QFHA; }
		public void setR32_QFHA(BigDecimal r32_QFHA) { R32_QFHA = r32_QFHA; }
		public String getR33_PRODUCT() { return R33_PRODUCT; }
		public void setR33_PRODUCT(String r33_PRODUCT) { R33_PRODUCT = r33_PRODUCT; }
		public BigDecimal getR33_NV_LONG() { return R33_NV_LONG; }
		public void setR33_NV_LONG(BigDecimal r33_NV_LONG) { R33_NV_LONG = r33_NV_LONG; }
		public BigDecimal getR33_NV_SHORT() { return R33_NV_SHORT; }
		public void setR33_NV_SHORT(BigDecimal r33_NV_SHORT) { R33_NV_SHORT = r33_NV_SHORT; }
		public BigDecimal getR33_FV_LONG() { return R33_FV_LONG; }
		public void setR33_FV_LONG(BigDecimal r33_FV_LONG) { R33_FV_LONG = r33_FV_LONG; }
		public BigDecimal getR33_FV_SHORT() { return R33_FV_SHORT; }
		public void setR33_FV_SHORT(BigDecimal r33_FV_SHORT) { R33_FV_SHORT = r33_FV_SHORT; }
		public BigDecimal getR33_QFHA() { return R33_QFHA; }
		public void setR33_QFHA(BigDecimal r33_QFHA) { R33_QFHA = r33_QFHA; }
		public String getR34_PRODUCT() { return R34_PRODUCT; }
		public void setR34_PRODUCT(String r34_PRODUCT) { R34_PRODUCT = r34_PRODUCT; }
		public BigDecimal getR34_NV_LONG() { return R34_NV_LONG; }
		public void setR34_NV_LONG(BigDecimal r34_NV_LONG) { R34_NV_LONG = r34_NV_LONG; }
		public BigDecimal getR34_NV_SHORT() { return R34_NV_SHORT; }
		public void setR34_NV_SHORT(BigDecimal r34_NV_SHORT) { R34_NV_SHORT = r34_NV_SHORT; }
		public BigDecimal getR34_FV_LONG() { return R34_FV_LONG; }
		public void setR34_FV_LONG(BigDecimal r34_FV_LONG) { R34_FV_LONG = r34_FV_LONG; }
		public BigDecimal getR34_FV_SHORT() { return R34_FV_SHORT; }
		public void setR34_FV_SHORT(BigDecimal r34_FV_SHORT) { R34_FV_SHORT = r34_FV_SHORT; }
		public BigDecimal getR34_QFHA() { return R34_QFHA; }
		public void setR34_QFHA(BigDecimal r34_QFHA) { R34_QFHA = r34_QFHA; }
		public String getR35_PRODUCT() { return R35_PRODUCT; }
		public void setR35_PRODUCT(String r35_PRODUCT) { R35_PRODUCT = r35_PRODUCT; }
		public BigDecimal getR35_NV_LONG() { return R35_NV_LONG; }
		public void setR35_NV_LONG(BigDecimal r35_NV_LONG) { R35_NV_LONG = r35_NV_LONG; }
		public BigDecimal getR35_NV_SHORT() { return R35_NV_SHORT; }
		public void setR35_NV_SHORT(BigDecimal r35_NV_SHORT) { R35_NV_SHORT = r35_NV_SHORT; }
		public BigDecimal getR35_FV_LONG() { return R35_FV_LONG; }
		public void setR35_FV_LONG(BigDecimal r35_FV_LONG) { R35_FV_LONG = r35_FV_LONG; }
		public BigDecimal getR35_FV_SHORT() { return R35_FV_SHORT; }
		public void setR35_FV_SHORT(BigDecimal r35_FV_SHORT) { R35_FV_SHORT = r35_FV_SHORT; }
		public BigDecimal getR35_QFHA() { return R35_QFHA; }
		public void setR35_QFHA(BigDecimal r35_QFHA) { R35_QFHA = r35_QFHA; }
		public String getR36_PRODUCT() { return R36_PRODUCT; }
		public void setR36_PRODUCT(String r36_PRODUCT) { R36_PRODUCT = r36_PRODUCT; }
		public BigDecimal getR36_NV_LONG() { return R36_NV_LONG; }
		public void setR36_NV_LONG(BigDecimal r36_NV_LONG) { R36_NV_LONG = r36_NV_LONG; }
		public BigDecimal getR36_NV_SHORT() { return R36_NV_SHORT; }
		public void setR36_NV_SHORT(BigDecimal r36_NV_SHORT) { R36_NV_SHORT = r36_NV_SHORT; }
		public BigDecimal getR36_FV_LONG() { return R36_FV_LONG; }
		public void setR36_FV_LONG(BigDecimal r36_FV_LONG) { R36_FV_LONG = r36_FV_LONG; }
		public BigDecimal getR36_FV_SHORT() { return R36_FV_SHORT; }
		public void setR36_FV_SHORT(BigDecimal r36_FV_SHORT) { R36_FV_SHORT = r36_FV_SHORT; }
		public BigDecimal getR36_QFHA() { return R36_QFHA; }
		public void setR36_QFHA(BigDecimal r36_QFHA) { R36_QFHA = r36_QFHA; }
		public String getR37_PRODUCT() { return R37_PRODUCT; }
		public void setR37_PRODUCT(String r37_PRODUCT) { R37_PRODUCT = r37_PRODUCT; }
		public BigDecimal getR37_NV_LONG() { return R37_NV_LONG; }
		public void setR37_NV_LONG(BigDecimal r37_NV_LONG) { R37_NV_LONG = r37_NV_LONG; }
		public BigDecimal getR37_NV_SHORT() { return R37_NV_SHORT; }
		public void setR37_NV_SHORT(BigDecimal r37_NV_SHORT) { R37_NV_SHORT = r37_NV_SHORT; }
		public BigDecimal getR37_FV_LONG() { return R37_FV_LONG; }
		public void setR37_FV_LONG(BigDecimal r37_FV_LONG) { R37_FV_LONG = r37_FV_LONG; }
		public BigDecimal getR37_FV_SHORT() { return R37_FV_SHORT; }
		public void setR37_FV_SHORT(BigDecimal r37_FV_SHORT) { R37_FV_SHORT = r37_FV_SHORT; }
		public BigDecimal getR37_QFHA() { return R37_QFHA; }
		public void setR37_QFHA(BigDecimal r37_QFHA) { R37_QFHA = r37_QFHA; }
		public String getR38_PRODUCT() { return R38_PRODUCT; }
		public void setR38_PRODUCT(String r38_PRODUCT) { R38_PRODUCT = r38_PRODUCT; }
		public BigDecimal getR38_NV_LONG() { return R38_NV_LONG; }
		public void setR38_NV_LONG(BigDecimal r38_NV_LONG) { R38_NV_LONG = r38_NV_LONG; }
		public BigDecimal getR38_NV_SHORT() { return R38_NV_SHORT; }
		public void setR38_NV_SHORT(BigDecimal r38_NV_SHORT) { R38_NV_SHORT = r38_NV_SHORT; }
		public BigDecimal getR38_FV_LONG() { return R38_FV_LONG; }
		public void setR38_FV_LONG(BigDecimal r38_FV_LONG) { R38_FV_LONG = r38_FV_LONG; }
		public BigDecimal getR38_FV_SHORT() { return R38_FV_SHORT; }
		public void setR38_FV_SHORT(BigDecimal r38_FV_SHORT) { R38_FV_SHORT = r38_FV_SHORT; }
		public BigDecimal getR38_QFHA() { return R38_QFHA; }
		public void setR38_QFHA(BigDecimal r38_QFHA) { R38_QFHA = r38_QFHA; }
		public String getR39_PRODUCT() { return R39_PRODUCT; }
		public void setR39_PRODUCT(String r39_PRODUCT) { R39_PRODUCT = r39_PRODUCT; }
		public BigDecimal getR39_NV_LONG() { return R39_NV_LONG; }
		public void setR39_NV_LONG(BigDecimal r39_NV_LONG) { R39_NV_LONG = r39_NV_LONG; }
		public BigDecimal getR39_NV_SHORT() { return R39_NV_SHORT; }
		public void setR39_NV_SHORT(BigDecimal r39_NV_SHORT) { R39_NV_SHORT = r39_NV_SHORT; }
		public BigDecimal getR39_FV_LONG() { return R39_FV_LONG; }
		public void setR39_FV_LONG(BigDecimal r39_FV_LONG) { R39_FV_LONG = r39_FV_LONG; }
		public BigDecimal getR39_FV_SHORT() { return R39_FV_SHORT; }
		public void setR39_FV_SHORT(BigDecimal r39_FV_SHORT) { R39_FV_SHORT = r39_FV_SHORT; }
		public BigDecimal getR39_QFHA() { return R39_QFHA; }
		public void setR39_QFHA(BigDecimal r39_QFHA) { R39_QFHA = r39_QFHA; }
		public String getR40_PRODUCT() { return R40_PRODUCT; }
		public void setR40_PRODUCT(String r40_PRODUCT) { R40_PRODUCT = r40_PRODUCT; }
		public BigDecimal getR40_NV_LONG() { return R40_NV_LONG; }
		public void setR40_NV_LONG(BigDecimal r40_NV_LONG) { R40_NV_LONG = r40_NV_LONG; }
		public BigDecimal getR40_NV_SHORT() { return R40_NV_SHORT; }
		public void setR40_NV_SHORT(BigDecimal r40_NV_SHORT) { R40_NV_SHORT = r40_NV_SHORT; }
		public BigDecimal getR40_FV_LONG() { return R40_FV_LONG; }
		public void setR40_FV_LONG(BigDecimal r40_FV_LONG) { R40_FV_LONG = r40_FV_LONG; }
		public BigDecimal getR40_FV_SHORT() { return R40_FV_SHORT; }
		public void setR40_FV_SHORT(BigDecimal r40_FV_SHORT) { R40_FV_SHORT = r40_FV_SHORT; }
		public BigDecimal getR40_QFHA() { return R40_QFHA; }
		public void setR40_QFHA(BigDecimal r40_QFHA) { R40_QFHA = r40_QFHA; }
		public String getR41_PRODUCT() { return R41_PRODUCT; }
		public void setR41_PRODUCT(String r41_PRODUCT) { R41_PRODUCT = r41_PRODUCT; }
		public BigDecimal getR41_NV_LONG() { return R41_NV_LONG; }
		public void setR41_NV_LONG(BigDecimal r41_NV_LONG) { R41_NV_LONG = r41_NV_LONG; }
		public BigDecimal getR41_NV_SHORT() { return R41_NV_SHORT; }
		public void setR41_NV_SHORT(BigDecimal r41_NV_SHORT) { R41_NV_SHORT = r41_NV_SHORT; }
		public BigDecimal getR41_FV_LONG() { return R41_FV_LONG; }
		public void setR41_FV_LONG(BigDecimal r41_FV_LONG) { R41_FV_LONG = r41_FV_LONG; }
		public BigDecimal getR41_FV_SHORT() { return R41_FV_SHORT; }
		public void setR41_FV_SHORT(BigDecimal r41_FV_SHORT) { R41_FV_SHORT = r41_FV_SHORT; }
		public BigDecimal getR41_QFHA() { return R41_QFHA; }
		public void setR41_QFHA(BigDecimal r41_QFHA) { R41_QFHA = r41_QFHA; }
		public String getR42_PRODUCT() { return R42_PRODUCT; }
		public void setR42_PRODUCT(String r42_PRODUCT) { R42_PRODUCT = r42_PRODUCT; }
		public BigDecimal getR42_NV_LONG() { return R42_NV_LONG; }
		public void setR42_NV_LONG(BigDecimal r42_NV_LONG) { R42_NV_LONG = r42_NV_LONG; }
		public BigDecimal getR42_NV_SHORT() { return R42_NV_SHORT; }
		public void setR42_NV_SHORT(BigDecimal r42_NV_SHORT) { R42_NV_SHORT = r42_NV_SHORT; }
		public BigDecimal getR42_FV_LONG() { return R42_FV_LONG; }
		public void setR42_FV_LONG(BigDecimal r42_FV_LONG) { R42_FV_LONG = r42_FV_LONG; }
		public BigDecimal getR42_FV_SHORT() { return R42_FV_SHORT; }
		public void setR42_FV_SHORT(BigDecimal r42_FV_SHORT) { R42_FV_SHORT = r42_FV_SHORT; }
		public BigDecimal getR42_QFHA() { return R42_QFHA; }
		public void setR42_QFHA(BigDecimal r42_QFHA) { R42_QFHA = r42_QFHA; }
		public String getR43_PRODUCT() { return R43_PRODUCT; }
		public void setR43_PRODUCT(String r43_PRODUCT) { R43_PRODUCT = r43_PRODUCT; }
		public BigDecimal getR43_NV_LONG() { return R43_NV_LONG; }
		public void setR43_NV_LONG(BigDecimal r43_NV_LONG) { R43_NV_LONG = r43_NV_LONG; }
		public BigDecimal getR43_NV_SHORT() { return R43_NV_SHORT; }
		public void setR43_NV_SHORT(BigDecimal r43_NV_SHORT) { R43_NV_SHORT = r43_NV_SHORT; }
		public BigDecimal getR43_FV_LONG() { return R43_FV_LONG; }
		public void setR43_FV_LONG(BigDecimal r43_FV_LONG) { R43_FV_LONG = r43_FV_LONG; }
		public BigDecimal getR43_FV_SHORT() { return R43_FV_SHORT; }
		public void setR43_FV_SHORT(BigDecimal r43_FV_SHORT) { R43_FV_SHORT = r43_FV_SHORT; }
		public BigDecimal getR43_QFHA() { return R43_QFHA; }
		public void setR43_QFHA(BigDecimal r43_QFHA) { R43_QFHA = r43_QFHA; }
		public String getR44_PRODUCT() { return R44_PRODUCT; }
		public void setR44_PRODUCT(String r44_PRODUCT) { R44_PRODUCT = r44_PRODUCT; }
		public BigDecimal getR44_NV_LONG() { return R44_NV_LONG; }
		public void setR44_NV_LONG(BigDecimal r44_NV_LONG) { R44_NV_LONG = r44_NV_LONG; }
		public BigDecimal getR44_NV_SHORT() { return R44_NV_SHORT; }
		public void setR44_NV_SHORT(BigDecimal r44_NV_SHORT) { R44_NV_SHORT = r44_NV_SHORT; }
		public BigDecimal getR44_FV_LONG() { return R44_FV_LONG; }
		public void setR44_FV_LONG(BigDecimal r44_FV_LONG) { R44_FV_LONG = r44_FV_LONG; }
		public BigDecimal getR44_FV_SHORT() { return R44_FV_SHORT; }
		public void setR44_FV_SHORT(BigDecimal r44_FV_SHORT) { R44_FV_SHORT = r44_FV_SHORT; }
		public BigDecimal getR44_QFHA() { return R44_QFHA; }
		public void setR44_QFHA(BigDecimal r44_QFHA) { R44_QFHA = r44_QFHA; }
		public String getR45_PRODUCT() { return R45_PRODUCT; }
		public void setR45_PRODUCT(String r45_PRODUCT) { R45_PRODUCT = r45_PRODUCT; }
		public BigDecimal getR45_NV_LONG() { return R45_NV_LONG; }
		public void setR45_NV_LONG(BigDecimal r45_NV_LONG) { R45_NV_LONG = r45_NV_LONG; }
		public BigDecimal getR45_NV_SHORT() { return R45_NV_SHORT; }
		public void setR45_NV_SHORT(BigDecimal r45_NV_SHORT) { R45_NV_SHORT = r45_NV_SHORT; }
		public BigDecimal getR45_FV_LONG() { return R45_FV_LONG; }
		public void setR45_FV_LONG(BigDecimal r45_FV_LONG) { R45_FV_LONG = r45_FV_LONG; }
		public BigDecimal getR45_FV_SHORT() { return R45_FV_SHORT; }
		public void setR45_FV_SHORT(BigDecimal r45_FV_SHORT) { R45_FV_SHORT = r45_FV_SHORT; }
		public BigDecimal getR45_QFHA() { return R45_QFHA; }
		public void setR45_QFHA(BigDecimal r45_QFHA) { R45_QFHA = r45_QFHA; }
		public String getR46_PRODUCT() { return R46_PRODUCT; }
		public void setR46_PRODUCT(String r46_PRODUCT) { R46_PRODUCT = r46_PRODUCT; }
		public BigDecimal getR46_NV_LONG() { return R46_NV_LONG; }
		public void setR46_NV_LONG(BigDecimal r46_NV_LONG) { R46_NV_LONG = r46_NV_LONG; }
		public BigDecimal getR46_NV_SHORT() { return R46_NV_SHORT; }
		public void setR46_NV_SHORT(BigDecimal r46_NV_SHORT) { R46_NV_SHORT = r46_NV_SHORT; }
		public BigDecimal getR46_FV_LONG() { return R46_FV_LONG; }
		public void setR46_FV_LONG(BigDecimal r46_FV_LONG) { R46_FV_LONG = r46_FV_LONG; }
		public BigDecimal getR46_FV_SHORT() { return R46_FV_SHORT; }
		public void setR46_FV_SHORT(BigDecimal r46_FV_SHORT) { R46_FV_SHORT = r46_FV_SHORT; }
		public BigDecimal getR46_QFHA() { return R46_QFHA; }
		public void setR46_QFHA(BigDecimal r46_QFHA) { R46_QFHA = r46_QFHA; }
		public String getR47_PRODUCT() { return R47_PRODUCT; }
		public void setR47_PRODUCT(String r47_PRODUCT) { R47_PRODUCT = r47_PRODUCT; }
		public BigDecimal getR47_NV_LONG() { return R47_NV_LONG; }
		public void setR47_NV_LONG(BigDecimal r47_NV_LONG) { R47_NV_LONG = r47_NV_LONG; }
		public BigDecimal getR47_NV_SHORT() { return R47_NV_SHORT; }
		public void setR47_NV_SHORT(BigDecimal r47_NV_SHORT) { R47_NV_SHORT = r47_NV_SHORT; }
		public BigDecimal getR47_FV_LONG() { return R47_FV_LONG; }
		public void setR47_FV_LONG(BigDecimal r47_FV_LONG) { R47_FV_LONG = r47_FV_LONG; }
		public BigDecimal getR47_FV_SHORT() { return R47_FV_SHORT; }
		public void setR47_FV_SHORT(BigDecimal r47_FV_SHORT) { R47_FV_SHORT = r47_FV_SHORT; }
		public BigDecimal getR47_QFHA() { return R47_QFHA; }
		public void setR47_QFHA(BigDecimal r47_QFHA) { R47_QFHA = r47_QFHA; }
		public String getR48_PRODUCT() { return R48_PRODUCT; }
		public void setR48_PRODUCT(String r48_PRODUCT) { R48_PRODUCT = r48_PRODUCT; }
		public BigDecimal getR48_NV_LONG() { return R48_NV_LONG; }
		public void setR48_NV_LONG(BigDecimal r48_NV_LONG) { R48_NV_LONG = r48_NV_LONG; }
		public BigDecimal getR48_NV_SHORT() { return R48_NV_SHORT; }
		public void setR48_NV_SHORT(BigDecimal r48_NV_SHORT) { R48_NV_SHORT = r48_NV_SHORT; }
		public BigDecimal getR48_FV_LONG() { return R48_FV_LONG; }
		public void setR48_FV_LONG(BigDecimal r48_FV_LONG) { R48_FV_LONG = r48_FV_LONG; }
		public BigDecimal getR48_FV_SHORT() { return R48_FV_SHORT; }
		public void setR48_FV_SHORT(BigDecimal r48_FV_SHORT) { R48_FV_SHORT = r48_FV_SHORT; }
		public BigDecimal getR48_QFHA() { return R48_QFHA; }
		public void setR48_QFHA(BigDecimal r48_QFHA) { R48_QFHA = r48_QFHA; }
		public String getR49_PRODUCT() { return R49_PRODUCT; }
		public void setR49_PRODUCT(String r49_PRODUCT) { R49_PRODUCT = r49_PRODUCT; }
		public BigDecimal getR49_NV_LONG() { return R49_NV_LONG; }
		public void setR49_NV_LONG(BigDecimal r49_NV_LONG) { R49_NV_LONG = r49_NV_LONG; }
		public BigDecimal getR49_NV_SHORT() { return R49_NV_SHORT; }
		public void setR49_NV_SHORT(BigDecimal r49_NV_SHORT) { R49_NV_SHORT = r49_NV_SHORT; }
		public BigDecimal getR49_FV_LONG() { return R49_FV_LONG; }
		public void setR49_FV_LONG(BigDecimal r49_FV_LONG) { R49_FV_LONG = r49_FV_LONG; }
		public BigDecimal getR49_FV_SHORT() { return R49_FV_SHORT; }
		public void setR49_FV_SHORT(BigDecimal r49_FV_SHORT) { R49_FV_SHORT = r49_FV_SHORT; }
		public BigDecimal getR49_QFHA() { return R49_QFHA; }
		public void setR49_QFHA(BigDecimal r49_QFHA) { R49_QFHA = r49_QFHA; }
		public String getR50_PRODUCT() { return R50_PRODUCT; }
		public void setR50_PRODUCT(String r50_PRODUCT) { R50_PRODUCT = r50_PRODUCT; }
		public BigDecimal getR50_NV_LONG() { return R50_NV_LONG; }
		public void setR50_NV_LONG(BigDecimal r50_NV_LONG) { R50_NV_LONG = r50_NV_LONG; }
		public BigDecimal getR50_NV_SHORT() { return R50_NV_SHORT; }
		public void setR50_NV_SHORT(BigDecimal r50_NV_SHORT) { R50_NV_SHORT = r50_NV_SHORT; }
		public BigDecimal getR50_FV_LONG() { return R50_FV_LONG; }
		public void setR50_FV_LONG(BigDecimal r50_FV_LONG) { R50_FV_LONG = r50_FV_LONG; }
		public BigDecimal getR50_FV_SHORT() { return R50_FV_SHORT; }
		public void setR50_FV_SHORT(BigDecimal r50_FV_SHORT) { R50_FV_SHORT = r50_FV_SHORT; }
		public BigDecimal getR50_QFHA() { return R50_QFHA; }
		public void setR50_QFHA(BigDecimal r50_QFHA) { R50_QFHA = r50_QFHA; }
		public String getR51_PRODUCT() { return R51_PRODUCT; }
		public void setR51_PRODUCT(String r51_PRODUCT) { R51_PRODUCT = r51_PRODUCT; }
		public BigDecimal getR51_NV_LONG() { return R51_NV_LONG; }
		public void setR51_NV_LONG(BigDecimal r51_NV_LONG) { R51_NV_LONG = r51_NV_LONG; }
		public BigDecimal getR51_NV_SHORT() { return R51_NV_SHORT; }
		public void setR51_NV_SHORT(BigDecimal r51_NV_SHORT) { R51_NV_SHORT = r51_NV_SHORT; }
		public BigDecimal getR51_FV_LONG() { return R51_FV_LONG; }
		public void setR51_FV_LONG(BigDecimal r51_FV_LONG) { R51_FV_LONG = r51_FV_LONG; }
		public BigDecimal getR51_FV_SHORT() { return R51_FV_SHORT; }
		public void setR51_FV_SHORT(BigDecimal r51_FV_SHORT) { R51_FV_SHORT = r51_FV_SHORT; }
		public BigDecimal getR51_QFHA() { return R51_QFHA; }
		public void setR51_QFHA(BigDecimal r51_QFHA) { R51_QFHA = r51_QFHA; }
		public String getR52_PRODUCT() { return R52_PRODUCT; }
		public void setR52_PRODUCT(String r52_PRODUCT) { R52_PRODUCT = r52_PRODUCT; }
		public BigDecimal getR52_NV_LONG() { return R52_NV_LONG; }
		public void setR52_NV_LONG(BigDecimal r52_NV_LONG) { R52_NV_LONG = r52_NV_LONG; }
		public BigDecimal getR52_NV_SHORT() { return R52_NV_SHORT; }
		public void setR52_NV_SHORT(BigDecimal r52_NV_SHORT) { R52_NV_SHORT = r52_NV_SHORT; }
		public BigDecimal getR52_FV_LONG() { return R52_FV_LONG; }
		public void setR52_FV_LONG(BigDecimal r52_FV_LONG) { R52_FV_LONG = r52_FV_LONG; }
		public BigDecimal getR52_FV_SHORT() { return R52_FV_SHORT; }
		public void setR52_FV_SHORT(BigDecimal r52_FV_SHORT) { R52_FV_SHORT = r52_FV_SHORT; }
		public BigDecimal getR52_QFHA() { return R52_QFHA; }
		public void setR52_QFHA(BigDecimal r52_QFHA) { R52_QFHA = r52_QFHA; }
		public String getR53_PRODUCT() { return R53_PRODUCT; }
		public void setR53_PRODUCT(String r53_PRODUCT) { R53_PRODUCT = r53_PRODUCT; }
		public BigDecimal getR53_NV_LONG() { return R53_NV_LONG; }
		public void setR53_NV_LONG(BigDecimal r53_NV_LONG) { R53_NV_LONG = r53_NV_LONG; }
		public BigDecimal getR53_NV_SHORT() { return R53_NV_SHORT; }
		public void setR53_NV_SHORT(BigDecimal r53_NV_SHORT) { R53_NV_SHORT = r53_NV_SHORT; }
		public BigDecimal getR53_FV_LONG() { return R53_FV_LONG; }
		public void setR53_FV_LONG(BigDecimal r53_FV_LONG) { R53_FV_LONG = r53_FV_LONG; }
		public BigDecimal getR53_FV_SHORT() { return R53_FV_SHORT; }
		public void setR53_FV_SHORT(BigDecimal r53_FV_SHORT) { R53_FV_SHORT = r53_FV_SHORT; }
		public BigDecimal getR53_QFHA() { return R53_QFHA; }
		public void setR53_QFHA(BigDecimal r53_QFHA) { R53_QFHA = r53_QFHA; }
		public String getR54_PRODUCT() { return R54_PRODUCT; }
		public void setR54_PRODUCT(String r54_PRODUCT) { R54_PRODUCT = r54_PRODUCT; }
		public BigDecimal getR54_NV_LONG() { return R54_NV_LONG; }
		public void setR54_NV_LONG(BigDecimal r54_NV_LONG) { R54_NV_LONG = r54_NV_LONG; }
		public BigDecimal getR54_NV_SHORT() { return R54_NV_SHORT; }
		public void setR54_NV_SHORT(BigDecimal r54_NV_SHORT) { R54_NV_SHORT = r54_NV_SHORT; }
		public BigDecimal getR54_FV_LONG() { return R54_FV_LONG; }
		public void setR54_FV_LONG(BigDecimal r54_FV_LONG) { R54_FV_LONG = r54_FV_LONG; }
		public BigDecimal getR54_FV_SHORT() { return R54_FV_SHORT; }
		public void setR54_FV_SHORT(BigDecimal r54_FV_SHORT) { R54_FV_SHORT = r54_FV_SHORT; }
		public BigDecimal getR54_QFHA() { return R54_QFHA; }
		public void setR54_QFHA(BigDecimal r54_QFHA) { R54_QFHA = r54_QFHA; }
		public String getR55_PRODUCT() { return R55_PRODUCT; }
		public void setR55_PRODUCT(String r55_PRODUCT) { R55_PRODUCT = r55_PRODUCT; }
		public BigDecimal getR55_NV_LONG() { return R55_NV_LONG; }
		public void setR55_NV_LONG(BigDecimal r55_NV_LONG) { R55_NV_LONG = r55_NV_LONG; }
		public BigDecimal getR55_NV_SHORT() { return R55_NV_SHORT; }
		public void setR55_NV_SHORT(BigDecimal r55_NV_SHORT) { R55_NV_SHORT = r55_NV_SHORT; }
		public BigDecimal getR55_FV_LONG() { return R55_FV_LONG; }
		public void setR55_FV_LONG(BigDecimal r55_FV_LONG) { R55_FV_LONG = r55_FV_LONG; }
		public BigDecimal getR55_FV_SHORT() { return R55_FV_SHORT; }
		public void setR55_FV_SHORT(BigDecimal r55_FV_SHORT) { R55_FV_SHORT = r55_FV_SHORT; }
		public BigDecimal getR55_QFHA() { return R55_QFHA; }
		public void setR55_QFHA(BigDecimal r55_QFHA) { R55_QFHA = r55_QFHA; }
		public Date getReportDate() { return reportDate; }
		public void setReportDate(Date reportDate) { this.reportDate = reportDate; }
		public BigDecimal getReportVersion() { return reportVersion; }
		public void setReportVersion(BigDecimal reportVersion) { this.reportVersion = reportVersion; }
		public String getREPORT_FREQUENCY() { return REPORT_FREQUENCY; }
		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) { REPORT_FREQUENCY = rEPORT_FREQUENCY; }
		public String getREPORT_CODE() { return REPORT_CODE; }
		public void setREPORT_CODE(String rEPORT_CODE) { REPORT_CODE = rEPORT_CODE; }
		public String getREPORT_DESC() { return REPORT_DESC; }
		public void setREPORT_DESC(String rEPORT_DESC) { REPORT_DESC = rEPORT_DESC; }
		public String getENTITY_FLG() { return ENTITY_FLG; }
		public void setENTITY_FLG(String eNTITY_FLG) { ENTITY_FLG = eNTITY_FLG; }
		public String getMODIFY_FLG() { return MODIFY_FLG; }
		public void setMODIFY_FLG(String mODIFY_FLG) { MODIFY_FLG = mODIFY_FLG; }
		public String getDEL_FLG() { return DEL_FLG; }
		public void setDEL_FLG(String dEL_FLG) { DEL_FLG = dEL_FLG; }
		public Date getReportResubDate() { return reportResubDate; }
		public void setReportResubDate(Date reportResubDate) { this.reportResubDate = reportResubDate; }
	}


	class M_TBSSummaryRowMapper implements RowMapper<M_TBS_Summary_Entity> {
		@Override
		public M_TBS_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_TBS_Summary_Entity obj = new M_TBS_Summary_Entity();
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));
		obj.setR11_PRODUCT(rs.getString("R11_PRODUCT")); obj.setR11_NV_LONG(rs.getBigDecimal("R11_NV_LONG")); obj.setR11_NV_SHORT(rs.getBigDecimal("R11_NV_SHORT"));
		obj.setR11_FV_LONG(rs.getBigDecimal("R11_FV_LONG")); obj.setR11_FV_SHORT(rs.getBigDecimal("R11_FV_SHORT")); obj.setR11_QFHA(rs.getBigDecimal("R11_QFHA"));
		obj.setR12_PRODUCT(rs.getString("R12_PRODUCT")); obj.setR12_NV_LONG(rs.getBigDecimal("R12_NV_LONG")); obj.setR12_NV_SHORT(rs.getBigDecimal("R12_NV_SHORT"));
		obj.setR12_FV_LONG(rs.getBigDecimal("R12_FV_LONG")); obj.setR12_FV_SHORT(rs.getBigDecimal("R12_FV_SHORT")); obj.setR12_QFHA(rs.getBigDecimal("R12_QFHA"));
		obj.setR13_PRODUCT(rs.getString("R13_PRODUCT")); obj.setR13_NV_LONG(rs.getBigDecimal("R13_NV_LONG")); obj.setR13_NV_SHORT(rs.getBigDecimal("R13_NV_SHORT"));
		obj.setR13_FV_LONG(rs.getBigDecimal("R13_FV_LONG")); obj.setR13_FV_SHORT(rs.getBigDecimal("R13_FV_SHORT")); obj.setR13_QFHA(rs.getBigDecimal("R13_QFHA"));
		obj.setR14_PRODUCT(rs.getString("R14_PRODUCT")); obj.setR14_NV_LONG(rs.getBigDecimal("R14_NV_LONG")); obj.setR14_NV_SHORT(rs.getBigDecimal("R14_NV_SHORT"));
		obj.setR14_FV_LONG(rs.getBigDecimal("R14_FV_LONG")); obj.setR14_FV_SHORT(rs.getBigDecimal("R14_FV_SHORT")); obj.setR14_QFHA(rs.getBigDecimal("R14_QFHA"));
		obj.setR15_PRODUCT(rs.getString("R15_PRODUCT")); obj.setR15_NV_LONG(rs.getBigDecimal("R15_NV_LONG")); obj.setR15_NV_SHORT(rs.getBigDecimal("R15_NV_SHORT"));
		obj.setR15_FV_LONG(rs.getBigDecimal("R15_FV_LONG")); obj.setR15_FV_SHORT(rs.getBigDecimal("R15_FV_SHORT")); obj.setR15_QFHA(rs.getBigDecimal("R15_QFHA"));
		obj.setR16_PRODUCT(rs.getString("R16_PRODUCT")); obj.setR16_NV_LONG(rs.getBigDecimal("R16_NV_LONG")); obj.setR16_NV_SHORT(rs.getBigDecimal("R16_NV_SHORT"));
		obj.setR16_FV_LONG(rs.getBigDecimal("R16_FV_LONG")); obj.setR16_FV_SHORT(rs.getBigDecimal("R16_FV_SHORT")); obj.setR16_QFHA(rs.getBigDecimal("R16_QFHA"));
		obj.setR17_PRODUCT(rs.getString("R17_PRODUCT")); obj.setR17_NV_LONG(rs.getBigDecimal("R17_NV_LONG")); obj.setR17_NV_SHORT(rs.getBigDecimal("R17_NV_SHORT"));
		obj.setR17_FV_LONG(rs.getBigDecimal("R17_FV_LONG")); obj.setR17_FV_SHORT(rs.getBigDecimal("R17_FV_SHORT")); obj.setR17_QFHA(rs.getBigDecimal("R17_QFHA"));
		obj.setR18_PRODUCT(rs.getString("R18_PRODUCT")); obj.setR18_NV_LONG(rs.getBigDecimal("R18_NV_LONG")); obj.setR18_NV_SHORT(rs.getBigDecimal("R18_NV_SHORT"));
		obj.setR18_FV_LONG(rs.getBigDecimal("R18_FV_LONG")); obj.setR18_FV_SHORT(rs.getBigDecimal("R18_FV_SHORT")); obj.setR18_QFHA(rs.getBigDecimal("R18_QFHA"));
		obj.setR19_PRODUCT(rs.getString("R19_PRODUCT")); obj.setR19_NV_LONG(rs.getBigDecimal("R19_NV_LONG")); obj.setR19_NV_SHORT(rs.getBigDecimal("R19_NV_SHORT"));
		obj.setR19_FV_LONG(rs.getBigDecimal("R19_FV_LONG")); obj.setR19_FV_SHORT(rs.getBigDecimal("R19_FV_SHORT")); obj.setR19_QFHA(rs.getBigDecimal("R19_QFHA"));
		obj.setR20_PRODUCT(rs.getString("R20_PRODUCT")); obj.setR20_NV_LONG(rs.getBigDecimal("R20_NV_LONG")); obj.setR20_NV_SHORT(rs.getBigDecimal("R20_NV_SHORT"));
		obj.setR20_FV_LONG(rs.getBigDecimal("R20_FV_LONG")); obj.setR20_FV_SHORT(rs.getBigDecimal("R20_FV_SHORT")); obj.setR20_QFHA(rs.getBigDecimal("R20_QFHA"));
		obj.setR21_PRODUCT(rs.getString("R21_PRODUCT")); obj.setR21_NV_LONG(rs.getBigDecimal("R21_NV_LONG")); obj.setR21_NV_SHORT(rs.getBigDecimal("R21_NV_SHORT"));
		obj.setR21_FV_LONG(rs.getBigDecimal("R21_FV_LONG")); obj.setR21_FV_SHORT(rs.getBigDecimal("R21_FV_SHORT")); obj.setR21_QFHA(rs.getBigDecimal("R21_QFHA"));
		obj.setR22_PRODUCT(rs.getString("R22_PRODUCT")); obj.setR22_NV_LONG(rs.getBigDecimal("R22_NV_LONG")); obj.setR22_NV_SHORT(rs.getBigDecimal("R22_NV_SHORT"));
		obj.setR22_FV_LONG(rs.getBigDecimal("R22_FV_LONG")); obj.setR22_FV_SHORT(rs.getBigDecimal("R22_FV_SHORT")); obj.setR22_QFHA(rs.getBigDecimal("R22_QFHA"));
		obj.setR23_PRODUCT(rs.getString("R23_PRODUCT")); obj.setR23_NV_LONG(rs.getBigDecimal("R23_NV_LONG")); obj.setR23_NV_SHORT(rs.getBigDecimal("R23_NV_SHORT"));
		obj.setR23_FV_LONG(rs.getBigDecimal("R23_FV_LONG")); obj.setR23_FV_SHORT(rs.getBigDecimal("R23_FV_SHORT")); obj.setR23_QFHA(rs.getBigDecimal("R23_QFHA"));
		obj.setR24_PRODUCT(rs.getString("R24_PRODUCT")); obj.setR24_NV_LONG(rs.getBigDecimal("R24_NV_LONG")); obj.setR24_NV_SHORT(rs.getBigDecimal("R24_NV_SHORT"));
		obj.setR24_FV_LONG(rs.getBigDecimal("R24_FV_LONG")); obj.setR24_FV_SHORT(rs.getBigDecimal("R24_FV_SHORT")); obj.setR24_QFHA(rs.getBigDecimal("R24_QFHA"));
		obj.setR25_PRODUCT(rs.getString("R25_PRODUCT")); obj.setR25_NV_LONG(rs.getBigDecimal("R25_NV_LONG")); obj.setR25_NV_SHORT(rs.getBigDecimal("R25_NV_SHORT"));
		obj.setR25_FV_LONG(rs.getBigDecimal("R25_FV_LONG")); obj.setR25_FV_SHORT(rs.getBigDecimal("R25_FV_SHORT")); obj.setR25_QFHA(rs.getBigDecimal("R25_QFHA"));
		obj.setR26_PRODUCT(rs.getString("R26_PRODUCT")); obj.setR26_NV_LONG(rs.getBigDecimal("R26_NV_LONG")); obj.setR26_NV_SHORT(rs.getBigDecimal("R26_NV_SHORT"));
		obj.setR26_FV_LONG(rs.getBigDecimal("R26_FV_LONG")); obj.setR26_FV_SHORT(rs.getBigDecimal("R26_FV_SHORT")); obj.setR26_QFHA(rs.getBigDecimal("R26_QFHA"));
		obj.setR27_PRODUCT(rs.getString("R27_PRODUCT")); obj.setR27_NV_LONG(rs.getBigDecimal("R27_NV_LONG")); obj.setR27_NV_SHORT(rs.getBigDecimal("R27_NV_SHORT"));
		obj.setR27_FV_LONG(rs.getBigDecimal("R27_FV_LONG")); obj.setR27_FV_SHORT(rs.getBigDecimal("R27_FV_SHORT")); obj.setR27_QFHA(rs.getBigDecimal("R27_QFHA"));
		obj.setR28_PRODUCT(rs.getString("R28_PRODUCT")); obj.setR28_NV_LONG(rs.getBigDecimal("R28_NV_LONG")); obj.setR28_NV_SHORT(rs.getBigDecimal("R28_NV_SHORT"));
		obj.setR28_FV_LONG(rs.getBigDecimal("R28_FV_LONG")); obj.setR28_FV_SHORT(rs.getBigDecimal("R28_FV_SHORT")); obj.setR28_QFHA(rs.getBigDecimal("R28_QFHA"));
		obj.setR29_PRODUCT(rs.getString("R29_PRODUCT")); obj.setR29_NV_LONG(rs.getBigDecimal("R29_NV_LONG")); obj.setR29_NV_SHORT(rs.getBigDecimal("R29_NV_SHORT"));
		obj.setR29_FV_LONG(rs.getBigDecimal("R29_FV_LONG")); obj.setR29_FV_SHORT(rs.getBigDecimal("R29_FV_SHORT")); obj.setR29_QFHA(rs.getBigDecimal("R29_QFHA"));
		obj.setR30_PRODUCT(rs.getString("R30_PRODUCT")); obj.setR30_NV_LONG(rs.getBigDecimal("R30_NV_LONG")); obj.setR30_NV_SHORT(rs.getBigDecimal("R30_NV_SHORT"));
		obj.setR30_FV_LONG(rs.getBigDecimal("R30_FV_LONG")); obj.setR30_FV_SHORT(rs.getBigDecimal("R30_FV_SHORT")); obj.setR30_QFHA(rs.getBigDecimal("R30_QFHA"));
		obj.setR31_PRODUCT(rs.getString("R31_PRODUCT")); obj.setR31_NV_LONG(rs.getBigDecimal("R31_NV_LONG")); obj.setR31_NV_SHORT(rs.getBigDecimal("R31_NV_SHORT"));
		obj.setR31_FV_LONG(rs.getBigDecimal("R31_FV_LONG")); obj.setR31_FV_SHORT(rs.getBigDecimal("R31_FV_SHORT")); obj.setR31_QFHA(rs.getBigDecimal("R31_QFHA"));
		obj.setR32_PRODUCT(rs.getString("R32_PRODUCT")); obj.setR32_NV_LONG(rs.getBigDecimal("R32_NV_LONG")); obj.setR32_NV_SHORT(rs.getBigDecimal("R32_NV_SHORT"));
		obj.setR32_FV_LONG(rs.getBigDecimal("R32_FV_LONG")); obj.setR32_FV_SHORT(rs.getBigDecimal("R32_FV_SHORT")); obj.setR32_QFHA(rs.getBigDecimal("R32_QFHA"));
		obj.setR33_PRODUCT(rs.getString("R33_PRODUCT")); obj.setR33_NV_LONG(rs.getBigDecimal("R33_NV_LONG")); obj.setR33_NV_SHORT(rs.getBigDecimal("R33_NV_SHORT"));
		obj.setR33_FV_LONG(rs.getBigDecimal("R33_FV_LONG")); obj.setR33_FV_SHORT(rs.getBigDecimal("R33_FV_SHORT")); obj.setR33_QFHA(rs.getBigDecimal("R33_QFHA"));
		obj.setR34_PRODUCT(rs.getString("R34_PRODUCT")); obj.setR34_NV_LONG(rs.getBigDecimal("R34_NV_LONG")); obj.setR34_NV_SHORT(rs.getBigDecimal("R34_NV_SHORT"));
		obj.setR34_FV_LONG(rs.getBigDecimal("R34_FV_LONG")); obj.setR34_FV_SHORT(rs.getBigDecimal("R34_FV_SHORT")); obj.setR34_QFHA(rs.getBigDecimal("R34_QFHA"));
		obj.setR35_PRODUCT(rs.getString("R35_PRODUCT")); obj.setR35_NV_LONG(rs.getBigDecimal("R35_NV_LONG")); obj.setR35_NV_SHORT(rs.getBigDecimal("R35_NV_SHORT"));
		obj.setR35_FV_LONG(rs.getBigDecimal("R35_FV_LONG")); obj.setR35_FV_SHORT(rs.getBigDecimal("R35_FV_SHORT")); obj.setR35_QFHA(rs.getBigDecimal("R35_QFHA"));
		obj.setR36_PRODUCT(rs.getString("R36_PRODUCT")); obj.setR36_NV_LONG(rs.getBigDecimal("R36_NV_LONG")); obj.setR36_NV_SHORT(rs.getBigDecimal("R36_NV_SHORT"));
		obj.setR36_FV_LONG(rs.getBigDecimal("R36_FV_LONG")); obj.setR36_FV_SHORT(rs.getBigDecimal("R36_FV_SHORT")); obj.setR36_QFHA(rs.getBigDecimal("R36_QFHA"));
		obj.setR37_PRODUCT(rs.getString("R37_PRODUCT")); obj.setR37_NV_LONG(rs.getBigDecimal("R37_NV_LONG")); obj.setR37_NV_SHORT(rs.getBigDecimal("R37_NV_SHORT"));
		obj.setR37_FV_LONG(rs.getBigDecimal("R37_FV_LONG")); obj.setR37_FV_SHORT(rs.getBigDecimal("R37_FV_SHORT")); obj.setR37_QFHA(rs.getBigDecimal("R37_QFHA"));
		obj.setR38_PRODUCT(rs.getString("R38_PRODUCT")); obj.setR38_NV_LONG(rs.getBigDecimal("R38_NV_LONG")); obj.setR38_NV_SHORT(rs.getBigDecimal("R38_NV_SHORT"));
		obj.setR38_FV_LONG(rs.getBigDecimal("R38_FV_LONG")); obj.setR38_FV_SHORT(rs.getBigDecimal("R38_FV_SHORT")); obj.setR38_QFHA(rs.getBigDecimal("R38_QFHA"));
		obj.setR39_PRODUCT(rs.getString("R39_PRODUCT")); obj.setR39_NV_LONG(rs.getBigDecimal("R39_NV_LONG")); obj.setR39_NV_SHORT(rs.getBigDecimal("R39_NV_SHORT"));
		obj.setR39_FV_LONG(rs.getBigDecimal("R39_FV_LONG")); obj.setR39_FV_SHORT(rs.getBigDecimal("R39_FV_SHORT")); obj.setR39_QFHA(rs.getBigDecimal("R39_QFHA"));
		obj.setR40_PRODUCT(rs.getString("R40_PRODUCT")); obj.setR40_NV_LONG(rs.getBigDecimal("R40_NV_LONG")); obj.setR40_NV_SHORT(rs.getBigDecimal("R40_NV_SHORT"));
		obj.setR40_FV_LONG(rs.getBigDecimal("R40_FV_LONG")); obj.setR40_FV_SHORT(rs.getBigDecimal("R40_FV_SHORT")); obj.setR40_QFHA(rs.getBigDecimal("R40_QFHA"));
		obj.setR41_PRODUCT(rs.getString("R41_PRODUCT")); obj.setR41_NV_LONG(rs.getBigDecimal("R41_NV_LONG")); obj.setR41_NV_SHORT(rs.getBigDecimal("R41_NV_SHORT"));
		obj.setR41_FV_LONG(rs.getBigDecimal("R41_FV_LONG")); obj.setR41_FV_SHORT(rs.getBigDecimal("R41_FV_SHORT")); obj.setR41_QFHA(rs.getBigDecimal("R41_QFHA"));
		obj.setR42_PRODUCT(rs.getString("R42_PRODUCT")); obj.setR42_NV_LONG(rs.getBigDecimal("R42_NV_LONG")); obj.setR42_NV_SHORT(rs.getBigDecimal("R42_NV_SHORT"));
		obj.setR42_FV_LONG(rs.getBigDecimal("R42_FV_LONG")); obj.setR42_FV_SHORT(rs.getBigDecimal("R42_FV_SHORT")); obj.setR42_QFHA(rs.getBigDecimal("R42_QFHA"));
		obj.setR43_PRODUCT(rs.getString("R43_PRODUCT")); obj.setR43_NV_LONG(rs.getBigDecimal("R43_NV_LONG")); obj.setR43_NV_SHORT(rs.getBigDecimal("R43_NV_SHORT"));
		obj.setR43_FV_LONG(rs.getBigDecimal("R43_FV_LONG")); obj.setR43_FV_SHORT(rs.getBigDecimal("R43_FV_SHORT")); obj.setR43_QFHA(rs.getBigDecimal("R43_QFHA"));
		obj.setR44_PRODUCT(rs.getString("R44_PRODUCT")); obj.setR44_NV_LONG(rs.getBigDecimal("R44_NV_LONG")); obj.setR44_NV_SHORT(rs.getBigDecimal("R44_NV_SHORT"));
		obj.setR44_FV_LONG(rs.getBigDecimal("R44_FV_LONG")); obj.setR44_FV_SHORT(rs.getBigDecimal("R44_FV_SHORT")); obj.setR44_QFHA(rs.getBigDecimal("R44_QFHA"));
		obj.setR45_PRODUCT(rs.getString("R45_PRODUCT")); obj.setR45_NV_LONG(rs.getBigDecimal("R45_NV_LONG")); obj.setR45_NV_SHORT(rs.getBigDecimal("R45_NV_SHORT"));
		obj.setR45_FV_LONG(rs.getBigDecimal("R45_FV_LONG")); obj.setR45_FV_SHORT(rs.getBigDecimal("R45_FV_SHORT")); obj.setR45_QFHA(rs.getBigDecimal("R45_QFHA"));
		obj.setR46_PRODUCT(rs.getString("R46_PRODUCT")); obj.setR46_NV_LONG(rs.getBigDecimal("R46_NV_LONG")); obj.setR46_NV_SHORT(rs.getBigDecimal("R46_NV_SHORT"));
		obj.setR46_FV_LONG(rs.getBigDecimal("R46_FV_LONG")); obj.setR46_FV_SHORT(rs.getBigDecimal("R46_FV_SHORT")); obj.setR46_QFHA(rs.getBigDecimal("R46_QFHA"));
		obj.setR47_PRODUCT(rs.getString("R47_PRODUCT")); obj.setR47_NV_LONG(rs.getBigDecimal("R47_NV_LONG")); obj.setR47_NV_SHORT(rs.getBigDecimal("R47_NV_SHORT"));
		obj.setR47_FV_LONG(rs.getBigDecimal("R47_FV_LONG")); obj.setR47_FV_SHORT(rs.getBigDecimal("R47_FV_SHORT")); obj.setR47_QFHA(rs.getBigDecimal("R47_QFHA"));
		obj.setR48_PRODUCT(rs.getString("R48_PRODUCT")); obj.setR48_NV_LONG(rs.getBigDecimal("R48_NV_LONG")); obj.setR48_NV_SHORT(rs.getBigDecimal("R48_NV_SHORT"));
		obj.setR48_FV_LONG(rs.getBigDecimal("R48_FV_LONG")); obj.setR48_FV_SHORT(rs.getBigDecimal("R48_FV_SHORT")); obj.setR48_QFHA(rs.getBigDecimal("R48_QFHA"));
		obj.setR49_PRODUCT(rs.getString("R49_PRODUCT")); obj.setR49_NV_LONG(rs.getBigDecimal("R49_NV_LONG")); obj.setR49_NV_SHORT(rs.getBigDecimal("R49_NV_SHORT"));
		obj.setR49_FV_LONG(rs.getBigDecimal("R49_FV_LONG")); obj.setR49_FV_SHORT(rs.getBigDecimal("R49_FV_SHORT")); obj.setR49_QFHA(rs.getBigDecimal("R49_QFHA"));
		obj.setR50_PRODUCT(rs.getString("R50_PRODUCT")); obj.setR50_NV_LONG(rs.getBigDecimal("R50_NV_LONG")); obj.setR50_NV_SHORT(rs.getBigDecimal("R50_NV_SHORT"));
		obj.setR50_FV_LONG(rs.getBigDecimal("R50_FV_LONG")); obj.setR50_FV_SHORT(rs.getBigDecimal("R50_FV_SHORT")); obj.setR50_QFHA(rs.getBigDecimal("R50_QFHA"));
		obj.setR51_PRODUCT(rs.getString("R51_PRODUCT")); obj.setR51_NV_LONG(rs.getBigDecimal("R51_NV_LONG")); obj.setR51_NV_SHORT(rs.getBigDecimal("R51_NV_SHORT"));
		obj.setR51_FV_LONG(rs.getBigDecimal("R51_FV_LONG")); obj.setR51_FV_SHORT(rs.getBigDecimal("R51_FV_SHORT")); obj.setR51_QFHA(rs.getBigDecimal("R51_QFHA"));
		obj.setR52_PRODUCT(rs.getString("R52_PRODUCT")); obj.setR52_NV_LONG(rs.getBigDecimal("R52_NV_LONG")); obj.setR52_NV_SHORT(rs.getBigDecimal("R52_NV_SHORT"));
		obj.setR52_FV_LONG(rs.getBigDecimal("R52_FV_LONG")); obj.setR52_FV_SHORT(rs.getBigDecimal("R52_FV_SHORT")); obj.setR52_QFHA(rs.getBigDecimal("R52_QFHA"));
		obj.setR53_PRODUCT(rs.getString("R53_PRODUCT")); obj.setR53_NV_LONG(rs.getBigDecimal("R53_NV_LONG")); obj.setR53_NV_SHORT(rs.getBigDecimal("R53_NV_SHORT"));
		obj.setR53_FV_LONG(rs.getBigDecimal("R53_FV_LONG")); obj.setR53_FV_SHORT(rs.getBigDecimal("R53_FV_SHORT")); obj.setR53_QFHA(rs.getBigDecimal("R53_QFHA"));
		obj.setR54_PRODUCT(rs.getString("R54_PRODUCT")); obj.setR54_NV_LONG(rs.getBigDecimal("R54_NV_LONG")); obj.setR54_NV_SHORT(rs.getBigDecimal("R54_NV_SHORT"));
		obj.setR54_FV_LONG(rs.getBigDecimal("R54_FV_LONG")); obj.setR54_FV_SHORT(rs.getBigDecimal("R54_FV_SHORT")); obj.setR54_QFHA(rs.getBigDecimal("R54_QFHA"));
		obj.setR55_PRODUCT(rs.getString("R55_PRODUCT")); obj.setR55_NV_LONG(rs.getBigDecimal("R55_NV_LONG")); obj.setR55_NV_SHORT(rs.getBigDecimal("R55_NV_SHORT"));
		obj.setR55_FV_LONG(rs.getBigDecimal("R55_FV_LONG")); obj.setR55_FV_SHORT(rs.getBigDecimal("R55_FV_SHORT")); obj.setR55_QFHA(rs.getBigDecimal("R55_QFHA"));
			return obj;
		}
	}
	class M_TBSDetailRowMapper implements RowMapper<M_TBS_Detail_Entity> {
		@Override
		public M_TBS_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_TBS_Detail_Entity obj = new M_TBS_Detail_Entity();
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));
		obj.setR11_PRODUCT(rs.getString("R11_PRODUCT")); obj.setR11_NV_LONG(rs.getBigDecimal("R11_NV_LONG")); obj.setR11_NV_SHORT(rs.getBigDecimal("R11_NV_SHORT"));
		obj.setR11_FV_LONG(rs.getBigDecimal("R11_FV_LONG")); obj.setR11_FV_SHORT(rs.getBigDecimal("R11_FV_SHORT")); obj.setR11_QFHA(rs.getBigDecimal("R11_QFHA"));
		obj.setR12_PRODUCT(rs.getString("R12_PRODUCT")); obj.setR12_NV_LONG(rs.getBigDecimal("R12_NV_LONG")); obj.setR12_NV_SHORT(rs.getBigDecimal("R12_NV_SHORT"));
		obj.setR12_FV_LONG(rs.getBigDecimal("R12_FV_LONG")); obj.setR12_FV_SHORT(rs.getBigDecimal("R12_FV_SHORT")); obj.setR12_QFHA(rs.getBigDecimal("R12_QFHA"));
		obj.setR13_PRODUCT(rs.getString("R13_PRODUCT")); obj.setR13_NV_LONG(rs.getBigDecimal("R13_NV_LONG")); obj.setR13_NV_SHORT(rs.getBigDecimal("R13_NV_SHORT"));
		obj.setR13_FV_LONG(rs.getBigDecimal("R13_FV_LONG")); obj.setR13_FV_SHORT(rs.getBigDecimal("R13_FV_SHORT")); obj.setR13_QFHA(rs.getBigDecimal("R13_QFHA"));
		obj.setR14_PRODUCT(rs.getString("R14_PRODUCT")); obj.setR14_NV_LONG(rs.getBigDecimal("R14_NV_LONG")); obj.setR14_NV_SHORT(rs.getBigDecimal("R14_NV_SHORT"));
		obj.setR14_FV_LONG(rs.getBigDecimal("R14_FV_LONG")); obj.setR14_FV_SHORT(rs.getBigDecimal("R14_FV_SHORT")); obj.setR14_QFHA(rs.getBigDecimal("R14_QFHA"));
		obj.setR15_PRODUCT(rs.getString("R15_PRODUCT")); obj.setR15_NV_LONG(rs.getBigDecimal("R15_NV_LONG")); obj.setR15_NV_SHORT(rs.getBigDecimal("R15_NV_SHORT"));
		obj.setR15_FV_LONG(rs.getBigDecimal("R15_FV_LONG")); obj.setR15_FV_SHORT(rs.getBigDecimal("R15_FV_SHORT")); obj.setR15_QFHA(rs.getBigDecimal("R15_QFHA"));
		obj.setR16_PRODUCT(rs.getString("R16_PRODUCT")); obj.setR16_NV_LONG(rs.getBigDecimal("R16_NV_LONG")); obj.setR16_NV_SHORT(rs.getBigDecimal("R16_NV_SHORT"));
		obj.setR16_FV_LONG(rs.getBigDecimal("R16_FV_LONG")); obj.setR16_FV_SHORT(rs.getBigDecimal("R16_FV_SHORT")); obj.setR16_QFHA(rs.getBigDecimal("R16_QFHA"));
		obj.setR17_PRODUCT(rs.getString("R17_PRODUCT")); obj.setR17_NV_LONG(rs.getBigDecimal("R17_NV_LONG")); obj.setR17_NV_SHORT(rs.getBigDecimal("R17_NV_SHORT"));
		obj.setR17_FV_LONG(rs.getBigDecimal("R17_FV_LONG")); obj.setR17_FV_SHORT(rs.getBigDecimal("R17_FV_SHORT")); obj.setR17_QFHA(rs.getBigDecimal("R17_QFHA"));
		obj.setR18_PRODUCT(rs.getString("R18_PRODUCT")); obj.setR18_NV_LONG(rs.getBigDecimal("R18_NV_LONG")); obj.setR18_NV_SHORT(rs.getBigDecimal("R18_NV_SHORT"));
		obj.setR18_FV_LONG(rs.getBigDecimal("R18_FV_LONG")); obj.setR18_FV_SHORT(rs.getBigDecimal("R18_FV_SHORT")); obj.setR18_QFHA(rs.getBigDecimal("R18_QFHA"));
		obj.setR19_PRODUCT(rs.getString("R19_PRODUCT")); obj.setR19_NV_LONG(rs.getBigDecimal("R19_NV_LONG")); obj.setR19_NV_SHORT(rs.getBigDecimal("R19_NV_SHORT"));
		obj.setR19_FV_LONG(rs.getBigDecimal("R19_FV_LONG")); obj.setR19_FV_SHORT(rs.getBigDecimal("R19_FV_SHORT")); obj.setR19_QFHA(rs.getBigDecimal("R19_QFHA"));
		obj.setR20_PRODUCT(rs.getString("R20_PRODUCT")); obj.setR20_NV_LONG(rs.getBigDecimal("R20_NV_LONG")); obj.setR20_NV_SHORT(rs.getBigDecimal("R20_NV_SHORT"));
		obj.setR20_FV_LONG(rs.getBigDecimal("R20_FV_LONG")); obj.setR20_FV_SHORT(rs.getBigDecimal("R20_FV_SHORT")); obj.setR20_QFHA(rs.getBigDecimal("R20_QFHA"));
		obj.setR21_PRODUCT(rs.getString("R21_PRODUCT")); obj.setR21_NV_LONG(rs.getBigDecimal("R21_NV_LONG")); obj.setR21_NV_SHORT(rs.getBigDecimal("R21_NV_SHORT"));
		obj.setR21_FV_LONG(rs.getBigDecimal("R21_FV_LONG")); obj.setR21_FV_SHORT(rs.getBigDecimal("R21_FV_SHORT")); obj.setR21_QFHA(rs.getBigDecimal("R21_QFHA"));
		obj.setR22_PRODUCT(rs.getString("R22_PRODUCT")); obj.setR22_NV_LONG(rs.getBigDecimal("R22_NV_LONG")); obj.setR22_NV_SHORT(rs.getBigDecimal("R22_NV_SHORT"));
		obj.setR22_FV_LONG(rs.getBigDecimal("R22_FV_LONG")); obj.setR22_FV_SHORT(rs.getBigDecimal("R22_FV_SHORT")); obj.setR22_QFHA(rs.getBigDecimal("R22_QFHA"));
		obj.setR23_PRODUCT(rs.getString("R23_PRODUCT")); obj.setR23_NV_LONG(rs.getBigDecimal("R23_NV_LONG")); obj.setR23_NV_SHORT(rs.getBigDecimal("R23_NV_SHORT"));
		obj.setR23_FV_LONG(rs.getBigDecimal("R23_FV_LONG")); obj.setR23_FV_SHORT(rs.getBigDecimal("R23_FV_SHORT")); obj.setR23_QFHA(rs.getBigDecimal("R23_QFHA"));
		obj.setR24_PRODUCT(rs.getString("R24_PRODUCT")); obj.setR24_NV_LONG(rs.getBigDecimal("R24_NV_LONG")); obj.setR24_NV_SHORT(rs.getBigDecimal("R24_NV_SHORT"));
		obj.setR24_FV_LONG(rs.getBigDecimal("R24_FV_LONG")); obj.setR24_FV_SHORT(rs.getBigDecimal("R24_FV_SHORT")); obj.setR24_QFHA(rs.getBigDecimal("R24_QFHA"));
		obj.setR25_PRODUCT(rs.getString("R25_PRODUCT")); obj.setR25_NV_LONG(rs.getBigDecimal("R25_NV_LONG")); obj.setR25_NV_SHORT(rs.getBigDecimal("R25_NV_SHORT"));
		obj.setR25_FV_LONG(rs.getBigDecimal("R25_FV_LONG")); obj.setR25_FV_SHORT(rs.getBigDecimal("R25_FV_SHORT")); obj.setR25_QFHA(rs.getBigDecimal("R25_QFHA"));
		obj.setR26_PRODUCT(rs.getString("R26_PRODUCT")); obj.setR26_NV_LONG(rs.getBigDecimal("R26_NV_LONG")); obj.setR26_NV_SHORT(rs.getBigDecimal("R26_NV_SHORT"));
		obj.setR26_FV_LONG(rs.getBigDecimal("R26_FV_LONG")); obj.setR26_FV_SHORT(rs.getBigDecimal("R26_FV_SHORT")); obj.setR26_QFHA(rs.getBigDecimal("R26_QFHA"));
		obj.setR27_PRODUCT(rs.getString("R27_PRODUCT")); obj.setR27_NV_LONG(rs.getBigDecimal("R27_NV_LONG")); obj.setR27_NV_SHORT(rs.getBigDecimal("R27_NV_SHORT"));
		obj.setR27_FV_LONG(rs.getBigDecimal("R27_FV_LONG")); obj.setR27_FV_SHORT(rs.getBigDecimal("R27_FV_SHORT")); obj.setR27_QFHA(rs.getBigDecimal("R27_QFHA"));
		obj.setR28_PRODUCT(rs.getString("R28_PRODUCT")); obj.setR28_NV_LONG(rs.getBigDecimal("R28_NV_LONG")); obj.setR28_NV_SHORT(rs.getBigDecimal("R28_NV_SHORT"));
		obj.setR28_FV_LONG(rs.getBigDecimal("R28_FV_LONG")); obj.setR28_FV_SHORT(rs.getBigDecimal("R28_FV_SHORT")); obj.setR28_QFHA(rs.getBigDecimal("R28_QFHA"));
		obj.setR29_PRODUCT(rs.getString("R29_PRODUCT")); obj.setR29_NV_LONG(rs.getBigDecimal("R29_NV_LONG")); obj.setR29_NV_SHORT(rs.getBigDecimal("R29_NV_SHORT"));
		obj.setR29_FV_LONG(rs.getBigDecimal("R29_FV_LONG")); obj.setR29_FV_SHORT(rs.getBigDecimal("R29_FV_SHORT")); obj.setR29_QFHA(rs.getBigDecimal("R29_QFHA"));
		obj.setR30_PRODUCT(rs.getString("R30_PRODUCT")); obj.setR30_NV_LONG(rs.getBigDecimal("R30_NV_LONG")); obj.setR30_NV_SHORT(rs.getBigDecimal("R30_NV_SHORT"));
		obj.setR30_FV_LONG(rs.getBigDecimal("R30_FV_LONG")); obj.setR30_FV_SHORT(rs.getBigDecimal("R30_FV_SHORT")); obj.setR30_QFHA(rs.getBigDecimal("R30_QFHA"));
		obj.setR31_PRODUCT(rs.getString("R31_PRODUCT")); obj.setR31_NV_LONG(rs.getBigDecimal("R31_NV_LONG")); obj.setR31_NV_SHORT(rs.getBigDecimal("R31_NV_SHORT"));
		obj.setR31_FV_LONG(rs.getBigDecimal("R31_FV_LONG")); obj.setR31_FV_SHORT(rs.getBigDecimal("R31_FV_SHORT")); obj.setR31_QFHA(rs.getBigDecimal("R31_QFHA"));
		obj.setR32_PRODUCT(rs.getString("R32_PRODUCT")); obj.setR32_NV_LONG(rs.getBigDecimal("R32_NV_LONG")); obj.setR32_NV_SHORT(rs.getBigDecimal("R32_NV_SHORT"));
		obj.setR32_FV_LONG(rs.getBigDecimal("R32_FV_LONG")); obj.setR32_FV_SHORT(rs.getBigDecimal("R32_FV_SHORT")); obj.setR32_QFHA(rs.getBigDecimal("R32_QFHA"));
		obj.setR33_PRODUCT(rs.getString("R33_PRODUCT")); obj.setR33_NV_LONG(rs.getBigDecimal("R33_NV_LONG")); obj.setR33_NV_SHORT(rs.getBigDecimal("R33_NV_SHORT"));
		obj.setR33_FV_LONG(rs.getBigDecimal("R33_FV_LONG")); obj.setR33_FV_SHORT(rs.getBigDecimal("R33_FV_SHORT")); obj.setR33_QFHA(rs.getBigDecimal("R33_QFHA"));
		obj.setR34_PRODUCT(rs.getString("R34_PRODUCT")); obj.setR34_NV_LONG(rs.getBigDecimal("R34_NV_LONG")); obj.setR34_NV_SHORT(rs.getBigDecimal("R34_NV_SHORT"));
		obj.setR34_FV_LONG(rs.getBigDecimal("R34_FV_LONG")); obj.setR34_FV_SHORT(rs.getBigDecimal("R34_FV_SHORT")); obj.setR34_QFHA(rs.getBigDecimal("R34_QFHA"));
		obj.setR35_PRODUCT(rs.getString("R35_PRODUCT")); obj.setR35_NV_LONG(rs.getBigDecimal("R35_NV_LONG")); obj.setR35_NV_SHORT(rs.getBigDecimal("R35_NV_SHORT"));
		obj.setR35_FV_LONG(rs.getBigDecimal("R35_FV_LONG")); obj.setR35_FV_SHORT(rs.getBigDecimal("R35_FV_SHORT")); obj.setR35_QFHA(rs.getBigDecimal("R35_QFHA"));
		obj.setR36_PRODUCT(rs.getString("R36_PRODUCT")); obj.setR36_NV_LONG(rs.getBigDecimal("R36_NV_LONG")); obj.setR36_NV_SHORT(rs.getBigDecimal("R36_NV_SHORT"));
		obj.setR36_FV_LONG(rs.getBigDecimal("R36_FV_LONG")); obj.setR36_FV_SHORT(rs.getBigDecimal("R36_FV_SHORT")); obj.setR36_QFHA(rs.getBigDecimal("R36_QFHA"));
		obj.setR37_PRODUCT(rs.getString("R37_PRODUCT")); obj.setR37_NV_LONG(rs.getBigDecimal("R37_NV_LONG")); obj.setR37_NV_SHORT(rs.getBigDecimal("R37_NV_SHORT"));
		obj.setR37_FV_LONG(rs.getBigDecimal("R37_FV_LONG")); obj.setR37_FV_SHORT(rs.getBigDecimal("R37_FV_SHORT")); obj.setR37_QFHA(rs.getBigDecimal("R37_QFHA"));
		obj.setR38_PRODUCT(rs.getString("R38_PRODUCT")); obj.setR38_NV_LONG(rs.getBigDecimal("R38_NV_LONG")); obj.setR38_NV_SHORT(rs.getBigDecimal("R38_NV_SHORT"));
		obj.setR38_FV_LONG(rs.getBigDecimal("R38_FV_LONG")); obj.setR38_FV_SHORT(rs.getBigDecimal("R38_FV_SHORT")); obj.setR38_QFHA(rs.getBigDecimal("R38_QFHA"));
		obj.setR39_PRODUCT(rs.getString("R39_PRODUCT")); obj.setR39_NV_LONG(rs.getBigDecimal("R39_NV_LONG")); obj.setR39_NV_SHORT(rs.getBigDecimal("R39_NV_SHORT"));
		obj.setR39_FV_LONG(rs.getBigDecimal("R39_FV_LONG")); obj.setR39_FV_SHORT(rs.getBigDecimal("R39_FV_SHORT")); obj.setR39_QFHA(rs.getBigDecimal("R39_QFHA"));
		obj.setR40_PRODUCT(rs.getString("R40_PRODUCT")); obj.setR40_NV_LONG(rs.getBigDecimal("R40_NV_LONG")); obj.setR40_NV_SHORT(rs.getBigDecimal("R40_NV_SHORT"));
		obj.setR40_FV_LONG(rs.getBigDecimal("R40_FV_LONG")); obj.setR40_FV_SHORT(rs.getBigDecimal("R40_FV_SHORT")); obj.setR40_QFHA(rs.getBigDecimal("R40_QFHA"));
		obj.setR41_PRODUCT(rs.getString("R41_PRODUCT")); obj.setR41_NV_LONG(rs.getBigDecimal("R41_NV_LONG")); obj.setR41_NV_SHORT(rs.getBigDecimal("R41_NV_SHORT"));
		obj.setR41_FV_LONG(rs.getBigDecimal("R41_FV_LONG")); obj.setR41_FV_SHORT(rs.getBigDecimal("R41_FV_SHORT")); obj.setR41_QFHA(rs.getBigDecimal("R41_QFHA"));
		obj.setR42_PRODUCT(rs.getString("R42_PRODUCT")); obj.setR42_NV_LONG(rs.getBigDecimal("R42_NV_LONG")); obj.setR42_NV_SHORT(rs.getBigDecimal("R42_NV_SHORT"));
		obj.setR42_FV_LONG(rs.getBigDecimal("R42_FV_LONG")); obj.setR42_FV_SHORT(rs.getBigDecimal("R42_FV_SHORT")); obj.setR42_QFHA(rs.getBigDecimal("R42_QFHA"));
		obj.setR43_PRODUCT(rs.getString("R43_PRODUCT")); obj.setR43_NV_LONG(rs.getBigDecimal("R43_NV_LONG")); obj.setR43_NV_SHORT(rs.getBigDecimal("R43_NV_SHORT"));
		obj.setR43_FV_LONG(rs.getBigDecimal("R43_FV_LONG")); obj.setR43_FV_SHORT(rs.getBigDecimal("R43_FV_SHORT")); obj.setR43_QFHA(rs.getBigDecimal("R43_QFHA"));
		obj.setR44_PRODUCT(rs.getString("R44_PRODUCT")); obj.setR44_NV_LONG(rs.getBigDecimal("R44_NV_LONG")); obj.setR44_NV_SHORT(rs.getBigDecimal("R44_NV_SHORT"));
		obj.setR44_FV_LONG(rs.getBigDecimal("R44_FV_LONG")); obj.setR44_FV_SHORT(rs.getBigDecimal("R44_FV_SHORT")); obj.setR44_QFHA(rs.getBigDecimal("R44_QFHA"));
		obj.setR45_PRODUCT(rs.getString("R45_PRODUCT")); obj.setR45_NV_LONG(rs.getBigDecimal("R45_NV_LONG")); obj.setR45_NV_SHORT(rs.getBigDecimal("R45_NV_SHORT"));
		obj.setR45_FV_LONG(rs.getBigDecimal("R45_FV_LONG")); obj.setR45_FV_SHORT(rs.getBigDecimal("R45_FV_SHORT")); obj.setR45_QFHA(rs.getBigDecimal("R45_QFHA"));
		obj.setR46_PRODUCT(rs.getString("R46_PRODUCT")); obj.setR46_NV_LONG(rs.getBigDecimal("R46_NV_LONG")); obj.setR46_NV_SHORT(rs.getBigDecimal("R46_NV_SHORT"));
		obj.setR46_FV_LONG(rs.getBigDecimal("R46_FV_LONG")); obj.setR46_FV_SHORT(rs.getBigDecimal("R46_FV_SHORT")); obj.setR46_QFHA(rs.getBigDecimal("R46_QFHA"));
		obj.setR47_PRODUCT(rs.getString("R47_PRODUCT")); obj.setR47_NV_LONG(rs.getBigDecimal("R47_NV_LONG")); obj.setR47_NV_SHORT(rs.getBigDecimal("R47_NV_SHORT"));
		obj.setR47_FV_LONG(rs.getBigDecimal("R47_FV_LONG")); obj.setR47_FV_SHORT(rs.getBigDecimal("R47_FV_SHORT")); obj.setR47_QFHA(rs.getBigDecimal("R47_QFHA"));
		obj.setR48_PRODUCT(rs.getString("R48_PRODUCT")); obj.setR48_NV_LONG(rs.getBigDecimal("R48_NV_LONG")); obj.setR48_NV_SHORT(rs.getBigDecimal("R48_NV_SHORT"));
		obj.setR48_FV_LONG(rs.getBigDecimal("R48_FV_LONG")); obj.setR48_FV_SHORT(rs.getBigDecimal("R48_FV_SHORT")); obj.setR48_QFHA(rs.getBigDecimal("R48_QFHA"));
		obj.setR49_PRODUCT(rs.getString("R49_PRODUCT")); obj.setR49_NV_LONG(rs.getBigDecimal("R49_NV_LONG")); obj.setR49_NV_SHORT(rs.getBigDecimal("R49_NV_SHORT"));
		obj.setR49_FV_LONG(rs.getBigDecimal("R49_FV_LONG")); obj.setR49_FV_SHORT(rs.getBigDecimal("R49_FV_SHORT")); obj.setR49_QFHA(rs.getBigDecimal("R49_QFHA"));
		obj.setR50_PRODUCT(rs.getString("R50_PRODUCT")); obj.setR50_NV_LONG(rs.getBigDecimal("R50_NV_LONG")); obj.setR50_NV_SHORT(rs.getBigDecimal("R50_NV_SHORT"));
		obj.setR50_FV_LONG(rs.getBigDecimal("R50_FV_LONG")); obj.setR50_FV_SHORT(rs.getBigDecimal("R50_FV_SHORT")); obj.setR50_QFHA(rs.getBigDecimal("R50_QFHA"));
		obj.setR51_PRODUCT(rs.getString("R51_PRODUCT")); obj.setR51_NV_LONG(rs.getBigDecimal("R51_NV_LONG")); obj.setR51_NV_SHORT(rs.getBigDecimal("R51_NV_SHORT"));
		obj.setR51_FV_LONG(rs.getBigDecimal("R51_FV_LONG")); obj.setR51_FV_SHORT(rs.getBigDecimal("R51_FV_SHORT")); obj.setR51_QFHA(rs.getBigDecimal("R51_QFHA"));
		obj.setR52_PRODUCT(rs.getString("R52_PRODUCT")); obj.setR52_NV_LONG(rs.getBigDecimal("R52_NV_LONG")); obj.setR52_NV_SHORT(rs.getBigDecimal("R52_NV_SHORT"));
		obj.setR52_FV_LONG(rs.getBigDecimal("R52_FV_LONG")); obj.setR52_FV_SHORT(rs.getBigDecimal("R52_FV_SHORT")); obj.setR52_QFHA(rs.getBigDecimal("R52_QFHA"));
		obj.setR53_PRODUCT(rs.getString("R53_PRODUCT")); obj.setR53_NV_LONG(rs.getBigDecimal("R53_NV_LONG")); obj.setR53_NV_SHORT(rs.getBigDecimal("R53_NV_SHORT"));
		obj.setR53_FV_LONG(rs.getBigDecimal("R53_FV_LONG")); obj.setR53_FV_SHORT(rs.getBigDecimal("R53_FV_SHORT")); obj.setR53_QFHA(rs.getBigDecimal("R53_QFHA"));
		obj.setR54_PRODUCT(rs.getString("R54_PRODUCT")); obj.setR54_NV_LONG(rs.getBigDecimal("R54_NV_LONG")); obj.setR54_NV_SHORT(rs.getBigDecimal("R54_NV_SHORT"));
		obj.setR54_FV_LONG(rs.getBigDecimal("R54_FV_LONG")); obj.setR54_FV_SHORT(rs.getBigDecimal("R54_FV_SHORT")); obj.setR54_QFHA(rs.getBigDecimal("R54_QFHA"));
		obj.setR55_PRODUCT(rs.getString("R55_PRODUCT")); obj.setR55_NV_LONG(rs.getBigDecimal("R55_NV_LONG")); obj.setR55_NV_SHORT(rs.getBigDecimal("R55_NV_SHORT"));
		obj.setR55_FV_LONG(rs.getBigDecimal("R55_FV_LONG")); obj.setR55_FV_SHORT(rs.getBigDecimal("R55_FV_SHORT")); obj.setR55_QFHA(rs.getBigDecimal("R55_QFHA"));
			return obj;
		}
	}
	class M_TBSArchivalSummaryRowMapper implements RowMapper<M_TBS_Archival_Summary_Entity> {
		@Override
		public M_TBS_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_TBS_Archival_Summary_Entity obj = new M_TBS_Archival_Summary_Entity();
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
		obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));
		obj.setR11_PRODUCT(rs.getString("R11_PRODUCT")); obj.setR11_NV_LONG(rs.getBigDecimal("R11_NV_LONG")); obj.setR11_NV_SHORT(rs.getBigDecimal("R11_NV_SHORT"));
		obj.setR11_FV_LONG(rs.getBigDecimal("R11_FV_LONG")); obj.setR11_FV_SHORT(rs.getBigDecimal("R11_FV_SHORT")); obj.setR11_QFHA(rs.getBigDecimal("R11_QFHA"));
		obj.setR12_PRODUCT(rs.getString("R12_PRODUCT")); obj.setR12_NV_LONG(rs.getBigDecimal("R12_NV_LONG")); obj.setR12_NV_SHORT(rs.getBigDecimal("R12_NV_SHORT"));
		obj.setR12_FV_LONG(rs.getBigDecimal("R12_FV_LONG")); obj.setR12_FV_SHORT(rs.getBigDecimal("R12_FV_SHORT")); obj.setR12_QFHA(rs.getBigDecimal("R12_QFHA"));
		obj.setR13_PRODUCT(rs.getString("R13_PRODUCT")); obj.setR13_NV_LONG(rs.getBigDecimal("R13_NV_LONG")); obj.setR13_NV_SHORT(rs.getBigDecimal("R13_NV_SHORT"));
		obj.setR13_FV_LONG(rs.getBigDecimal("R13_FV_LONG")); obj.setR13_FV_SHORT(rs.getBigDecimal("R13_FV_SHORT")); obj.setR13_QFHA(rs.getBigDecimal("R13_QFHA"));
		obj.setR14_PRODUCT(rs.getString("R14_PRODUCT")); obj.setR14_NV_LONG(rs.getBigDecimal("R14_NV_LONG")); obj.setR14_NV_SHORT(rs.getBigDecimal("R14_NV_SHORT"));
		obj.setR14_FV_LONG(rs.getBigDecimal("R14_FV_LONG")); obj.setR14_FV_SHORT(rs.getBigDecimal("R14_FV_SHORT")); obj.setR14_QFHA(rs.getBigDecimal("R14_QFHA"));
		obj.setR15_PRODUCT(rs.getString("R15_PRODUCT")); obj.setR15_NV_LONG(rs.getBigDecimal("R15_NV_LONG")); obj.setR15_NV_SHORT(rs.getBigDecimal("R15_NV_SHORT"));
		obj.setR15_FV_LONG(rs.getBigDecimal("R15_FV_LONG")); obj.setR15_FV_SHORT(rs.getBigDecimal("R15_FV_SHORT")); obj.setR15_QFHA(rs.getBigDecimal("R15_QFHA"));
		obj.setR16_PRODUCT(rs.getString("R16_PRODUCT")); obj.setR16_NV_LONG(rs.getBigDecimal("R16_NV_LONG")); obj.setR16_NV_SHORT(rs.getBigDecimal("R16_NV_SHORT"));
		obj.setR16_FV_LONG(rs.getBigDecimal("R16_FV_LONG")); obj.setR16_FV_SHORT(rs.getBigDecimal("R16_FV_SHORT")); obj.setR16_QFHA(rs.getBigDecimal("R16_QFHA"));
		obj.setR17_PRODUCT(rs.getString("R17_PRODUCT")); obj.setR17_NV_LONG(rs.getBigDecimal("R17_NV_LONG")); obj.setR17_NV_SHORT(rs.getBigDecimal("R17_NV_SHORT"));
		obj.setR17_FV_LONG(rs.getBigDecimal("R17_FV_LONG")); obj.setR17_FV_SHORT(rs.getBigDecimal("R17_FV_SHORT")); obj.setR17_QFHA(rs.getBigDecimal("R17_QFHA"));
		obj.setR18_PRODUCT(rs.getString("R18_PRODUCT")); obj.setR18_NV_LONG(rs.getBigDecimal("R18_NV_LONG")); obj.setR18_NV_SHORT(rs.getBigDecimal("R18_NV_SHORT"));
		obj.setR18_FV_LONG(rs.getBigDecimal("R18_FV_LONG")); obj.setR18_FV_SHORT(rs.getBigDecimal("R18_FV_SHORT")); obj.setR18_QFHA(rs.getBigDecimal("R18_QFHA"));
		obj.setR19_PRODUCT(rs.getString("R19_PRODUCT")); obj.setR19_NV_LONG(rs.getBigDecimal("R19_NV_LONG")); obj.setR19_NV_SHORT(rs.getBigDecimal("R19_NV_SHORT"));
		obj.setR19_FV_LONG(rs.getBigDecimal("R19_FV_LONG")); obj.setR19_FV_SHORT(rs.getBigDecimal("R19_FV_SHORT")); obj.setR19_QFHA(rs.getBigDecimal("R19_QFHA"));
		obj.setR20_PRODUCT(rs.getString("R20_PRODUCT")); obj.setR20_NV_LONG(rs.getBigDecimal("R20_NV_LONG")); obj.setR20_NV_SHORT(rs.getBigDecimal("R20_NV_SHORT"));
		obj.setR20_FV_LONG(rs.getBigDecimal("R20_FV_LONG")); obj.setR20_FV_SHORT(rs.getBigDecimal("R20_FV_SHORT")); obj.setR20_QFHA(rs.getBigDecimal("R20_QFHA"));
		obj.setR21_PRODUCT(rs.getString("R21_PRODUCT")); obj.setR21_NV_LONG(rs.getBigDecimal("R21_NV_LONG")); obj.setR21_NV_SHORT(rs.getBigDecimal("R21_NV_SHORT"));
		obj.setR21_FV_LONG(rs.getBigDecimal("R21_FV_LONG")); obj.setR21_FV_SHORT(rs.getBigDecimal("R21_FV_SHORT")); obj.setR21_QFHA(rs.getBigDecimal("R21_QFHA"));
		obj.setR22_PRODUCT(rs.getString("R22_PRODUCT")); obj.setR22_NV_LONG(rs.getBigDecimal("R22_NV_LONG")); obj.setR22_NV_SHORT(rs.getBigDecimal("R22_NV_SHORT"));
		obj.setR22_FV_LONG(rs.getBigDecimal("R22_FV_LONG")); obj.setR22_FV_SHORT(rs.getBigDecimal("R22_FV_SHORT")); obj.setR22_QFHA(rs.getBigDecimal("R22_QFHA"));
		obj.setR23_PRODUCT(rs.getString("R23_PRODUCT")); obj.setR23_NV_LONG(rs.getBigDecimal("R23_NV_LONG")); obj.setR23_NV_SHORT(rs.getBigDecimal("R23_NV_SHORT"));
		obj.setR23_FV_LONG(rs.getBigDecimal("R23_FV_LONG")); obj.setR23_FV_SHORT(rs.getBigDecimal("R23_FV_SHORT")); obj.setR23_QFHA(rs.getBigDecimal("R23_QFHA"));
		obj.setR24_PRODUCT(rs.getString("R24_PRODUCT")); obj.setR24_NV_LONG(rs.getBigDecimal("R24_NV_LONG")); obj.setR24_NV_SHORT(rs.getBigDecimal("R24_NV_SHORT"));
		obj.setR24_FV_LONG(rs.getBigDecimal("R24_FV_LONG")); obj.setR24_FV_SHORT(rs.getBigDecimal("R24_FV_SHORT")); obj.setR24_QFHA(rs.getBigDecimal("R24_QFHA"));
		obj.setR25_PRODUCT(rs.getString("R25_PRODUCT")); obj.setR25_NV_LONG(rs.getBigDecimal("R25_NV_LONG")); obj.setR25_NV_SHORT(rs.getBigDecimal("R25_NV_SHORT"));
		obj.setR25_FV_LONG(rs.getBigDecimal("R25_FV_LONG")); obj.setR25_FV_SHORT(rs.getBigDecimal("R25_FV_SHORT")); obj.setR25_QFHA(rs.getBigDecimal("R25_QFHA"));
		obj.setR26_PRODUCT(rs.getString("R26_PRODUCT")); obj.setR26_NV_LONG(rs.getBigDecimal("R26_NV_LONG")); obj.setR26_NV_SHORT(rs.getBigDecimal("R26_NV_SHORT"));
		obj.setR26_FV_LONG(rs.getBigDecimal("R26_FV_LONG")); obj.setR26_FV_SHORT(rs.getBigDecimal("R26_FV_SHORT")); obj.setR26_QFHA(rs.getBigDecimal("R26_QFHA"));
		obj.setR27_PRODUCT(rs.getString("R27_PRODUCT")); obj.setR27_NV_LONG(rs.getBigDecimal("R27_NV_LONG")); obj.setR27_NV_SHORT(rs.getBigDecimal("R27_NV_SHORT"));
		obj.setR27_FV_LONG(rs.getBigDecimal("R27_FV_LONG")); obj.setR27_FV_SHORT(rs.getBigDecimal("R27_FV_SHORT")); obj.setR27_QFHA(rs.getBigDecimal("R27_QFHA"));
		obj.setR28_PRODUCT(rs.getString("R28_PRODUCT")); obj.setR28_NV_LONG(rs.getBigDecimal("R28_NV_LONG")); obj.setR28_NV_SHORT(rs.getBigDecimal("R28_NV_SHORT"));
		obj.setR28_FV_LONG(rs.getBigDecimal("R28_FV_LONG")); obj.setR28_FV_SHORT(rs.getBigDecimal("R28_FV_SHORT")); obj.setR28_QFHA(rs.getBigDecimal("R28_QFHA"));
		obj.setR29_PRODUCT(rs.getString("R29_PRODUCT")); obj.setR29_NV_LONG(rs.getBigDecimal("R29_NV_LONG")); obj.setR29_NV_SHORT(rs.getBigDecimal("R29_NV_SHORT"));
		obj.setR29_FV_LONG(rs.getBigDecimal("R29_FV_LONG")); obj.setR29_FV_SHORT(rs.getBigDecimal("R29_FV_SHORT")); obj.setR29_QFHA(rs.getBigDecimal("R29_QFHA"));
		obj.setR30_PRODUCT(rs.getString("R30_PRODUCT")); obj.setR30_NV_LONG(rs.getBigDecimal("R30_NV_LONG")); obj.setR30_NV_SHORT(rs.getBigDecimal("R30_NV_SHORT"));
		obj.setR30_FV_LONG(rs.getBigDecimal("R30_FV_LONG")); obj.setR30_FV_SHORT(rs.getBigDecimal("R30_FV_SHORT")); obj.setR30_QFHA(rs.getBigDecimal("R30_QFHA"));
		obj.setR31_PRODUCT(rs.getString("R31_PRODUCT")); obj.setR31_NV_LONG(rs.getBigDecimal("R31_NV_LONG")); obj.setR31_NV_SHORT(rs.getBigDecimal("R31_NV_SHORT"));
		obj.setR31_FV_LONG(rs.getBigDecimal("R31_FV_LONG")); obj.setR31_FV_SHORT(rs.getBigDecimal("R31_FV_SHORT")); obj.setR31_QFHA(rs.getBigDecimal("R31_QFHA"));
		obj.setR32_PRODUCT(rs.getString("R32_PRODUCT")); obj.setR32_NV_LONG(rs.getBigDecimal("R32_NV_LONG")); obj.setR32_NV_SHORT(rs.getBigDecimal("R32_NV_SHORT"));
		obj.setR32_FV_LONG(rs.getBigDecimal("R32_FV_LONG")); obj.setR32_FV_SHORT(rs.getBigDecimal("R32_FV_SHORT")); obj.setR32_QFHA(rs.getBigDecimal("R32_QFHA"));
		obj.setR33_PRODUCT(rs.getString("R33_PRODUCT")); obj.setR33_NV_LONG(rs.getBigDecimal("R33_NV_LONG")); obj.setR33_NV_SHORT(rs.getBigDecimal("R33_NV_SHORT"));
		obj.setR33_FV_LONG(rs.getBigDecimal("R33_FV_LONG")); obj.setR33_FV_SHORT(rs.getBigDecimal("R33_FV_SHORT")); obj.setR33_QFHA(rs.getBigDecimal("R33_QFHA"));
		obj.setR34_PRODUCT(rs.getString("R34_PRODUCT")); obj.setR34_NV_LONG(rs.getBigDecimal("R34_NV_LONG")); obj.setR34_NV_SHORT(rs.getBigDecimal("R34_NV_SHORT"));
		obj.setR34_FV_LONG(rs.getBigDecimal("R34_FV_LONG")); obj.setR34_FV_SHORT(rs.getBigDecimal("R34_FV_SHORT")); obj.setR34_QFHA(rs.getBigDecimal("R34_QFHA"));
		obj.setR35_PRODUCT(rs.getString("R35_PRODUCT")); obj.setR35_NV_LONG(rs.getBigDecimal("R35_NV_LONG")); obj.setR35_NV_SHORT(rs.getBigDecimal("R35_NV_SHORT"));
		obj.setR35_FV_LONG(rs.getBigDecimal("R35_FV_LONG")); obj.setR35_FV_SHORT(rs.getBigDecimal("R35_FV_SHORT")); obj.setR35_QFHA(rs.getBigDecimal("R35_QFHA"));
		obj.setR36_PRODUCT(rs.getString("R36_PRODUCT")); obj.setR36_NV_LONG(rs.getBigDecimal("R36_NV_LONG")); obj.setR36_NV_SHORT(rs.getBigDecimal("R36_NV_SHORT"));
		obj.setR36_FV_LONG(rs.getBigDecimal("R36_FV_LONG")); obj.setR36_FV_SHORT(rs.getBigDecimal("R36_FV_SHORT")); obj.setR36_QFHA(rs.getBigDecimal("R36_QFHA"));
		obj.setR37_PRODUCT(rs.getString("R37_PRODUCT")); obj.setR37_NV_LONG(rs.getBigDecimal("R37_NV_LONG")); obj.setR37_NV_SHORT(rs.getBigDecimal("R37_NV_SHORT"));
		obj.setR37_FV_LONG(rs.getBigDecimal("R37_FV_LONG")); obj.setR37_FV_SHORT(rs.getBigDecimal("R37_FV_SHORT")); obj.setR37_QFHA(rs.getBigDecimal("R37_QFHA"));
		obj.setR38_PRODUCT(rs.getString("R38_PRODUCT")); obj.setR38_NV_LONG(rs.getBigDecimal("R38_NV_LONG")); obj.setR38_NV_SHORT(rs.getBigDecimal("R38_NV_SHORT"));
		obj.setR38_FV_LONG(rs.getBigDecimal("R38_FV_LONG")); obj.setR38_FV_SHORT(rs.getBigDecimal("R38_FV_SHORT")); obj.setR38_QFHA(rs.getBigDecimal("R38_QFHA"));
		obj.setR39_PRODUCT(rs.getString("R39_PRODUCT")); obj.setR39_NV_LONG(rs.getBigDecimal("R39_NV_LONG")); obj.setR39_NV_SHORT(rs.getBigDecimal("R39_NV_SHORT"));
		obj.setR39_FV_LONG(rs.getBigDecimal("R39_FV_LONG")); obj.setR39_FV_SHORT(rs.getBigDecimal("R39_FV_SHORT")); obj.setR39_QFHA(rs.getBigDecimal("R39_QFHA"));
		obj.setR40_PRODUCT(rs.getString("R40_PRODUCT")); obj.setR40_NV_LONG(rs.getBigDecimal("R40_NV_LONG")); obj.setR40_NV_SHORT(rs.getBigDecimal("R40_NV_SHORT"));
		obj.setR40_FV_LONG(rs.getBigDecimal("R40_FV_LONG")); obj.setR40_FV_SHORT(rs.getBigDecimal("R40_FV_SHORT")); obj.setR40_QFHA(rs.getBigDecimal("R40_QFHA"));
		obj.setR41_PRODUCT(rs.getString("R41_PRODUCT")); obj.setR41_NV_LONG(rs.getBigDecimal("R41_NV_LONG")); obj.setR41_NV_SHORT(rs.getBigDecimal("R41_NV_SHORT"));
		obj.setR41_FV_LONG(rs.getBigDecimal("R41_FV_LONG")); obj.setR41_FV_SHORT(rs.getBigDecimal("R41_FV_SHORT")); obj.setR41_QFHA(rs.getBigDecimal("R41_QFHA"));
		obj.setR42_PRODUCT(rs.getString("R42_PRODUCT")); obj.setR42_NV_LONG(rs.getBigDecimal("R42_NV_LONG")); obj.setR42_NV_SHORT(rs.getBigDecimal("R42_NV_SHORT"));
		obj.setR42_FV_LONG(rs.getBigDecimal("R42_FV_LONG")); obj.setR42_FV_SHORT(rs.getBigDecimal("R42_FV_SHORT")); obj.setR42_QFHA(rs.getBigDecimal("R42_QFHA"));
		obj.setR43_PRODUCT(rs.getString("R43_PRODUCT")); obj.setR43_NV_LONG(rs.getBigDecimal("R43_NV_LONG")); obj.setR43_NV_SHORT(rs.getBigDecimal("R43_NV_SHORT"));
		obj.setR43_FV_LONG(rs.getBigDecimal("R43_FV_LONG")); obj.setR43_FV_SHORT(rs.getBigDecimal("R43_FV_SHORT")); obj.setR43_QFHA(rs.getBigDecimal("R43_QFHA"));
		obj.setR44_PRODUCT(rs.getString("R44_PRODUCT")); obj.setR44_NV_LONG(rs.getBigDecimal("R44_NV_LONG")); obj.setR44_NV_SHORT(rs.getBigDecimal("R44_NV_SHORT"));
		obj.setR44_FV_LONG(rs.getBigDecimal("R44_FV_LONG")); obj.setR44_FV_SHORT(rs.getBigDecimal("R44_FV_SHORT")); obj.setR44_QFHA(rs.getBigDecimal("R44_QFHA"));
		obj.setR45_PRODUCT(rs.getString("R45_PRODUCT")); obj.setR45_NV_LONG(rs.getBigDecimal("R45_NV_LONG")); obj.setR45_NV_SHORT(rs.getBigDecimal("R45_NV_SHORT"));
		obj.setR45_FV_LONG(rs.getBigDecimal("R45_FV_LONG")); obj.setR45_FV_SHORT(rs.getBigDecimal("R45_FV_SHORT")); obj.setR45_QFHA(rs.getBigDecimal("R45_QFHA"));
		obj.setR46_PRODUCT(rs.getString("R46_PRODUCT")); obj.setR46_NV_LONG(rs.getBigDecimal("R46_NV_LONG")); obj.setR46_NV_SHORT(rs.getBigDecimal("R46_NV_SHORT"));
		obj.setR46_FV_LONG(rs.getBigDecimal("R46_FV_LONG")); obj.setR46_FV_SHORT(rs.getBigDecimal("R46_FV_SHORT")); obj.setR46_QFHA(rs.getBigDecimal("R46_QFHA"));
		obj.setR47_PRODUCT(rs.getString("R47_PRODUCT")); obj.setR47_NV_LONG(rs.getBigDecimal("R47_NV_LONG")); obj.setR47_NV_SHORT(rs.getBigDecimal("R47_NV_SHORT"));
		obj.setR47_FV_LONG(rs.getBigDecimal("R47_FV_LONG")); obj.setR47_FV_SHORT(rs.getBigDecimal("R47_FV_SHORT")); obj.setR47_QFHA(rs.getBigDecimal("R47_QFHA"));
		obj.setR48_PRODUCT(rs.getString("R48_PRODUCT")); obj.setR48_NV_LONG(rs.getBigDecimal("R48_NV_LONG")); obj.setR48_NV_SHORT(rs.getBigDecimal("R48_NV_SHORT"));
		obj.setR48_FV_LONG(rs.getBigDecimal("R48_FV_LONG")); obj.setR48_FV_SHORT(rs.getBigDecimal("R48_FV_SHORT")); obj.setR48_QFHA(rs.getBigDecimal("R48_QFHA"));
		obj.setR49_PRODUCT(rs.getString("R49_PRODUCT")); obj.setR49_NV_LONG(rs.getBigDecimal("R49_NV_LONG")); obj.setR49_NV_SHORT(rs.getBigDecimal("R49_NV_SHORT"));
		obj.setR49_FV_LONG(rs.getBigDecimal("R49_FV_LONG")); obj.setR49_FV_SHORT(rs.getBigDecimal("R49_FV_SHORT")); obj.setR49_QFHA(rs.getBigDecimal("R49_QFHA"));
		obj.setR50_PRODUCT(rs.getString("R50_PRODUCT")); obj.setR50_NV_LONG(rs.getBigDecimal("R50_NV_LONG")); obj.setR50_NV_SHORT(rs.getBigDecimal("R50_NV_SHORT"));
		obj.setR50_FV_LONG(rs.getBigDecimal("R50_FV_LONG")); obj.setR50_FV_SHORT(rs.getBigDecimal("R50_FV_SHORT")); obj.setR50_QFHA(rs.getBigDecimal("R50_QFHA"));
		obj.setR51_PRODUCT(rs.getString("R51_PRODUCT")); obj.setR51_NV_LONG(rs.getBigDecimal("R51_NV_LONG")); obj.setR51_NV_SHORT(rs.getBigDecimal("R51_NV_SHORT"));
		obj.setR51_FV_LONG(rs.getBigDecimal("R51_FV_LONG")); obj.setR51_FV_SHORT(rs.getBigDecimal("R51_FV_SHORT")); obj.setR51_QFHA(rs.getBigDecimal("R51_QFHA"));
		obj.setR52_PRODUCT(rs.getString("R52_PRODUCT")); obj.setR52_NV_LONG(rs.getBigDecimal("R52_NV_LONG")); obj.setR52_NV_SHORT(rs.getBigDecimal("R52_NV_SHORT"));
		obj.setR52_FV_LONG(rs.getBigDecimal("R52_FV_LONG")); obj.setR52_FV_SHORT(rs.getBigDecimal("R52_FV_SHORT")); obj.setR52_QFHA(rs.getBigDecimal("R52_QFHA"));
		obj.setR53_PRODUCT(rs.getString("R53_PRODUCT")); obj.setR53_NV_LONG(rs.getBigDecimal("R53_NV_LONG")); obj.setR53_NV_SHORT(rs.getBigDecimal("R53_NV_SHORT"));
		obj.setR53_FV_LONG(rs.getBigDecimal("R53_FV_LONG")); obj.setR53_FV_SHORT(rs.getBigDecimal("R53_FV_SHORT")); obj.setR53_QFHA(rs.getBigDecimal("R53_QFHA"));
		obj.setR54_PRODUCT(rs.getString("R54_PRODUCT")); obj.setR54_NV_LONG(rs.getBigDecimal("R54_NV_LONG")); obj.setR54_NV_SHORT(rs.getBigDecimal("R54_NV_SHORT"));
		obj.setR54_FV_LONG(rs.getBigDecimal("R54_FV_LONG")); obj.setR54_FV_SHORT(rs.getBigDecimal("R54_FV_SHORT")); obj.setR54_QFHA(rs.getBigDecimal("R54_QFHA"));
		obj.setR55_PRODUCT(rs.getString("R55_PRODUCT")); obj.setR55_NV_LONG(rs.getBigDecimal("R55_NV_LONG")); obj.setR55_NV_SHORT(rs.getBigDecimal("R55_NV_SHORT"));
		obj.setR55_FV_LONG(rs.getBigDecimal("R55_FV_LONG")); obj.setR55_FV_SHORT(rs.getBigDecimal("R55_FV_SHORT")); obj.setR55_QFHA(rs.getBigDecimal("R55_QFHA"));
			return obj;
		}
	}
	class M_TBSArchivalDetailRowMapper implements RowMapper<M_TBS_Archival_Detail_Entity> {
		@Override
		public M_TBS_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_TBS_Archival_Detail_Entity obj = new M_TBS_Archival_Detail_Entity();
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
		obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));
		obj.setR11_PRODUCT(rs.getString("R11_PRODUCT")); obj.setR11_NV_LONG(rs.getBigDecimal("R11_NV_LONG")); obj.setR11_NV_SHORT(rs.getBigDecimal("R11_NV_SHORT"));
		obj.setR11_FV_LONG(rs.getBigDecimal("R11_FV_LONG")); obj.setR11_FV_SHORT(rs.getBigDecimal("R11_FV_SHORT")); obj.setR11_QFHA(rs.getBigDecimal("R11_QFHA"));
		obj.setR12_PRODUCT(rs.getString("R12_PRODUCT")); obj.setR12_NV_LONG(rs.getBigDecimal("R12_NV_LONG")); obj.setR12_NV_SHORT(rs.getBigDecimal("R12_NV_SHORT"));
		obj.setR12_FV_LONG(rs.getBigDecimal("R12_FV_LONG")); obj.setR12_FV_SHORT(rs.getBigDecimal("R12_FV_SHORT")); obj.setR12_QFHA(rs.getBigDecimal("R12_QFHA"));
		obj.setR13_PRODUCT(rs.getString("R13_PRODUCT")); obj.setR13_NV_LONG(rs.getBigDecimal("R13_NV_LONG")); obj.setR13_NV_SHORT(rs.getBigDecimal("R13_NV_SHORT"));
		obj.setR13_FV_LONG(rs.getBigDecimal("R13_FV_LONG")); obj.setR13_FV_SHORT(rs.getBigDecimal("R13_FV_SHORT")); obj.setR13_QFHA(rs.getBigDecimal("R13_QFHA"));
		obj.setR14_PRODUCT(rs.getString("R14_PRODUCT")); obj.setR14_NV_LONG(rs.getBigDecimal("R14_NV_LONG")); obj.setR14_NV_SHORT(rs.getBigDecimal("R14_NV_SHORT"));
		obj.setR14_FV_LONG(rs.getBigDecimal("R14_FV_LONG")); obj.setR14_FV_SHORT(rs.getBigDecimal("R14_FV_SHORT")); obj.setR14_QFHA(rs.getBigDecimal("R14_QFHA"));
		obj.setR15_PRODUCT(rs.getString("R15_PRODUCT")); obj.setR15_NV_LONG(rs.getBigDecimal("R15_NV_LONG")); obj.setR15_NV_SHORT(rs.getBigDecimal("R15_NV_SHORT"));
		obj.setR15_FV_LONG(rs.getBigDecimal("R15_FV_LONG")); obj.setR15_FV_SHORT(rs.getBigDecimal("R15_FV_SHORT")); obj.setR15_QFHA(rs.getBigDecimal("R15_QFHA"));
		obj.setR16_PRODUCT(rs.getString("R16_PRODUCT")); obj.setR16_NV_LONG(rs.getBigDecimal("R16_NV_LONG")); obj.setR16_NV_SHORT(rs.getBigDecimal("R16_NV_SHORT"));
		obj.setR16_FV_LONG(rs.getBigDecimal("R16_FV_LONG")); obj.setR16_FV_SHORT(rs.getBigDecimal("R16_FV_SHORT")); obj.setR16_QFHA(rs.getBigDecimal("R16_QFHA"));
		obj.setR17_PRODUCT(rs.getString("R17_PRODUCT")); obj.setR17_NV_LONG(rs.getBigDecimal("R17_NV_LONG")); obj.setR17_NV_SHORT(rs.getBigDecimal("R17_NV_SHORT"));
		obj.setR17_FV_LONG(rs.getBigDecimal("R17_FV_LONG")); obj.setR17_FV_SHORT(rs.getBigDecimal("R17_FV_SHORT")); obj.setR17_QFHA(rs.getBigDecimal("R17_QFHA"));
		obj.setR18_PRODUCT(rs.getString("R18_PRODUCT")); obj.setR18_NV_LONG(rs.getBigDecimal("R18_NV_LONG")); obj.setR18_NV_SHORT(rs.getBigDecimal("R18_NV_SHORT"));
		obj.setR18_FV_LONG(rs.getBigDecimal("R18_FV_LONG")); obj.setR18_FV_SHORT(rs.getBigDecimal("R18_FV_SHORT")); obj.setR18_QFHA(rs.getBigDecimal("R18_QFHA"));
		obj.setR19_PRODUCT(rs.getString("R19_PRODUCT")); obj.setR19_NV_LONG(rs.getBigDecimal("R19_NV_LONG")); obj.setR19_NV_SHORT(rs.getBigDecimal("R19_NV_SHORT"));
		obj.setR19_FV_LONG(rs.getBigDecimal("R19_FV_LONG")); obj.setR19_FV_SHORT(rs.getBigDecimal("R19_FV_SHORT")); obj.setR19_QFHA(rs.getBigDecimal("R19_QFHA"));
		obj.setR20_PRODUCT(rs.getString("R20_PRODUCT")); obj.setR20_NV_LONG(rs.getBigDecimal("R20_NV_LONG")); obj.setR20_NV_SHORT(rs.getBigDecimal("R20_NV_SHORT"));
		obj.setR20_FV_LONG(rs.getBigDecimal("R20_FV_LONG")); obj.setR20_FV_SHORT(rs.getBigDecimal("R20_FV_SHORT")); obj.setR20_QFHA(rs.getBigDecimal("R20_QFHA"));
		obj.setR21_PRODUCT(rs.getString("R21_PRODUCT")); obj.setR21_NV_LONG(rs.getBigDecimal("R21_NV_LONG")); obj.setR21_NV_SHORT(rs.getBigDecimal("R21_NV_SHORT"));
		obj.setR21_FV_LONG(rs.getBigDecimal("R21_FV_LONG")); obj.setR21_FV_SHORT(rs.getBigDecimal("R21_FV_SHORT")); obj.setR21_QFHA(rs.getBigDecimal("R21_QFHA"));
		obj.setR22_PRODUCT(rs.getString("R22_PRODUCT")); obj.setR22_NV_LONG(rs.getBigDecimal("R22_NV_LONG")); obj.setR22_NV_SHORT(rs.getBigDecimal("R22_NV_SHORT"));
		obj.setR22_FV_LONG(rs.getBigDecimal("R22_FV_LONG")); obj.setR22_FV_SHORT(rs.getBigDecimal("R22_FV_SHORT")); obj.setR22_QFHA(rs.getBigDecimal("R22_QFHA"));
		obj.setR23_PRODUCT(rs.getString("R23_PRODUCT")); obj.setR23_NV_LONG(rs.getBigDecimal("R23_NV_LONG")); obj.setR23_NV_SHORT(rs.getBigDecimal("R23_NV_SHORT"));
		obj.setR23_FV_LONG(rs.getBigDecimal("R23_FV_LONG")); obj.setR23_FV_SHORT(rs.getBigDecimal("R23_FV_SHORT")); obj.setR23_QFHA(rs.getBigDecimal("R23_QFHA"));
		obj.setR24_PRODUCT(rs.getString("R24_PRODUCT")); obj.setR24_NV_LONG(rs.getBigDecimal("R24_NV_LONG")); obj.setR24_NV_SHORT(rs.getBigDecimal("R24_NV_SHORT"));
		obj.setR24_FV_LONG(rs.getBigDecimal("R24_FV_LONG")); obj.setR24_FV_SHORT(rs.getBigDecimal("R24_FV_SHORT")); obj.setR24_QFHA(rs.getBigDecimal("R24_QFHA"));
		obj.setR25_PRODUCT(rs.getString("R25_PRODUCT")); obj.setR25_NV_LONG(rs.getBigDecimal("R25_NV_LONG")); obj.setR25_NV_SHORT(rs.getBigDecimal("R25_NV_SHORT"));
		obj.setR25_FV_LONG(rs.getBigDecimal("R25_FV_LONG")); obj.setR25_FV_SHORT(rs.getBigDecimal("R25_FV_SHORT")); obj.setR25_QFHA(rs.getBigDecimal("R25_QFHA"));
		obj.setR26_PRODUCT(rs.getString("R26_PRODUCT")); obj.setR26_NV_LONG(rs.getBigDecimal("R26_NV_LONG")); obj.setR26_NV_SHORT(rs.getBigDecimal("R26_NV_SHORT"));
		obj.setR26_FV_LONG(rs.getBigDecimal("R26_FV_LONG")); obj.setR26_FV_SHORT(rs.getBigDecimal("R26_FV_SHORT")); obj.setR26_QFHA(rs.getBigDecimal("R26_QFHA"));
		obj.setR27_PRODUCT(rs.getString("R27_PRODUCT")); obj.setR27_NV_LONG(rs.getBigDecimal("R27_NV_LONG")); obj.setR27_NV_SHORT(rs.getBigDecimal("R27_NV_SHORT"));
		obj.setR27_FV_LONG(rs.getBigDecimal("R27_FV_LONG")); obj.setR27_FV_SHORT(rs.getBigDecimal("R27_FV_SHORT")); obj.setR27_QFHA(rs.getBigDecimal("R27_QFHA"));
		obj.setR28_PRODUCT(rs.getString("R28_PRODUCT")); obj.setR28_NV_LONG(rs.getBigDecimal("R28_NV_LONG")); obj.setR28_NV_SHORT(rs.getBigDecimal("R28_NV_SHORT"));
		obj.setR28_FV_LONG(rs.getBigDecimal("R28_FV_LONG")); obj.setR28_FV_SHORT(rs.getBigDecimal("R28_FV_SHORT")); obj.setR28_QFHA(rs.getBigDecimal("R28_QFHA"));
		obj.setR29_PRODUCT(rs.getString("R29_PRODUCT")); obj.setR29_NV_LONG(rs.getBigDecimal("R29_NV_LONG")); obj.setR29_NV_SHORT(rs.getBigDecimal("R29_NV_SHORT"));
		obj.setR29_FV_LONG(rs.getBigDecimal("R29_FV_LONG")); obj.setR29_FV_SHORT(rs.getBigDecimal("R29_FV_SHORT")); obj.setR29_QFHA(rs.getBigDecimal("R29_QFHA"));
		obj.setR30_PRODUCT(rs.getString("R30_PRODUCT")); obj.setR30_NV_LONG(rs.getBigDecimal("R30_NV_LONG")); obj.setR30_NV_SHORT(rs.getBigDecimal("R30_NV_SHORT"));
		obj.setR30_FV_LONG(rs.getBigDecimal("R30_FV_LONG")); obj.setR30_FV_SHORT(rs.getBigDecimal("R30_FV_SHORT")); obj.setR30_QFHA(rs.getBigDecimal("R30_QFHA"));
		obj.setR31_PRODUCT(rs.getString("R31_PRODUCT")); obj.setR31_NV_LONG(rs.getBigDecimal("R31_NV_LONG")); obj.setR31_NV_SHORT(rs.getBigDecimal("R31_NV_SHORT"));
		obj.setR31_FV_LONG(rs.getBigDecimal("R31_FV_LONG")); obj.setR31_FV_SHORT(rs.getBigDecimal("R31_FV_SHORT")); obj.setR31_QFHA(rs.getBigDecimal("R31_QFHA"));
		obj.setR32_PRODUCT(rs.getString("R32_PRODUCT")); obj.setR32_NV_LONG(rs.getBigDecimal("R32_NV_LONG")); obj.setR32_NV_SHORT(rs.getBigDecimal("R32_NV_SHORT"));
		obj.setR32_FV_LONG(rs.getBigDecimal("R32_FV_LONG")); obj.setR32_FV_SHORT(rs.getBigDecimal("R32_FV_SHORT")); obj.setR32_QFHA(rs.getBigDecimal("R32_QFHA"));
		obj.setR33_PRODUCT(rs.getString("R33_PRODUCT")); obj.setR33_NV_LONG(rs.getBigDecimal("R33_NV_LONG")); obj.setR33_NV_SHORT(rs.getBigDecimal("R33_NV_SHORT"));
		obj.setR33_FV_LONG(rs.getBigDecimal("R33_FV_LONG")); obj.setR33_FV_SHORT(rs.getBigDecimal("R33_FV_SHORT")); obj.setR33_QFHA(rs.getBigDecimal("R33_QFHA"));
		obj.setR34_PRODUCT(rs.getString("R34_PRODUCT")); obj.setR34_NV_LONG(rs.getBigDecimal("R34_NV_LONG")); obj.setR34_NV_SHORT(rs.getBigDecimal("R34_NV_SHORT"));
		obj.setR34_FV_LONG(rs.getBigDecimal("R34_FV_LONG")); obj.setR34_FV_SHORT(rs.getBigDecimal("R34_FV_SHORT")); obj.setR34_QFHA(rs.getBigDecimal("R34_QFHA"));
		obj.setR35_PRODUCT(rs.getString("R35_PRODUCT")); obj.setR35_NV_LONG(rs.getBigDecimal("R35_NV_LONG")); obj.setR35_NV_SHORT(rs.getBigDecimal("R35_NV_SHORT"));
		obj.setR35_FV_LONG(rs.getBigDecimal("R35_FV_LONG")); obj.setR35_FV_SHORT(rs.getBigDecimal("R35_FV_SHORT")); obj.setR35_QFHA(rs.getBigDecimal("R35_QFHA"));
		obj.setR36_PRODUCT(rs.getString("R36_PRODUCT")); obj.setR36_NV_LONG(rs.getBigDecimal("R36_NV_LONG")); obj.setR36_NV_SHORT(rs.getBigDecimal("R36_NV_SHORT"));
		obj.setR36_FV_LONG(rs.getBigDecimal("R36_FV_LONG")); obj.setR36_FV_SHORT(rs.getBigDecimal("R36_FV_SHORT")); obj.setR36_QFHA(rs.getBigDecimal("R36_QFHA"));
		obj.setR37_PRODUCT(rs.getString("R37_PRODUCT")); obj.setR37_NV_LONG(rs.getBigDecimal("R37_NV_LONG")); obj.setR37_NV_SHORT(rs.getBigDecimal("R37_NV_SHORT"));
		obj.setR37_FV_LONG(rs.getBigDecimal("R37_FV_LONG")); obj.setR37_FV_SHORT(rs.getBigDecimal("R37_FV_SHORT")); obj.setR37_QFHA(rs.getBigDecimal("R37_QFHA"));
		obj.setR38_PRODUCT(rs.getString("R38_PRODUCT")); obj.setR38_NV_LONG(rs.getBigDecimal("R38_NV_LONG")); obj.setR38_NV_SHORT(rs.getBigDecimal("R38_NV_SHORT"));
		obj.setR38_FV_LONG(rs.getBigDecimal("R38_FV_LONG")); obj.setR38_FV_SHORT(rs.getBigDecimal("R38_FV_SHORT")); obj.setR38_QFHA(rs.getBigDecimal("R38_QFHA"));
		obj.setR39_PRODUCT(rs.getString("R39_PRODUCT")); obj.setR39_NV_LONG(rs.getBigDecimal("R39_NV_LONG")); obj.setR39_NV_SHORT(rs.getBigDecimal("R39_NV_SHORT"));
		obj.setR39_FV_LONG(rs.getBigDecimal("R39_FV_LONG")); obj.setR39_FV_SHORT(rs.getBigDecimal("R39_FV_SHORT")); obj.setR39_QFHA(rs.getBigDecimal("R39_QFHA"));
		obj.setR40_PRODUCT(rs.getString("R40_PRODUCT")); obj.setR40_NV_LONG(rs.getBigDecimal("R40_NV_LONG")); obj.setR40_NV_SHORT(rs.getBigDecimal("R40_NV_SHORT"));
		obj.setR40_FV_LONG(rs.getBigDecimal("R40_FV_LONG")); obj.setR40_FV_SHORT(rs.getBigDecimal("R40_FV_SHORT")); obj.setR40_QFHA(rs.getBigDecimal("R40_QFHA"));
		obj.setR41_PRODUCT(rs.getString("R41_PRODUCT")); obj.setR41_NV_LONG(rs.getBigDecimal("R41_NV_LONG")); obj.setR41_NV_SHORT(rs.getBigDecimal("R41_NV_SHORT"));
		obj.setR41_FV_LONG(rs.getBigDecimal("R41_FV_LONG")); obj.setR41_FV_SHORT(rs.getBigDecimal("R41_FV_SHORT")); obj.setR41_QFHA(rs.getBigDecimal("R41_QFHA"));
		obj.setR42_PRODUCT(rs.getString("R42_PRODUCT")); obj.setR42_NV_LONG(rs.getBigDecimal("R42_NV_LONG")); obj.setR42_NV_SHORT(rs.getBigDecimal("R42_NV_SHORT"));
		obj.setR42_FV_LONG(rs.getBigDecimal("R42_FV_LONG")); obj.setR42_FV_SHORT(rs.getBigDecimal("R42_FV_SHORT")); obj.setR42_QFHA(rs.getBigDecimal("R42_QFHA"));
		obj.setR43_PRODUCT(rs.getString("R43_PRODUCT")); obj.setR43_NV_LONG(rs.getBigDecimal("R43_NV_LONG")); obj.setR43_NV_SHORT(rs.getBigDecimal("R43_NV_SHORT"));
		obj.setR43_FV_LONG(rs.getBigDecimal("R43_FV_LONG")); obj.setR43_FV_SHORT(rs.getBigDecimal("R43_FV_SHORT")); obj.setR43_QFHA(rs.getBigDecimal("R43_QFHA"));
		obj.setR44_PRODUCT(rs.getString("R44_PRODUCT")); obj.setR44_NV_LONG(rs.getBigDecimal("R44_NV_LONG")); obj.setR44_NV_SHORT(rs.getBigDecimal("R44_NV_SHORT"));
		obj.setR44_FV_LONG(rs.getBigDecimal("R44_FV_LONG")); obj.setR44_FV_SHORT(rs.getBigDecimal("R44_FV_SHORT")); obj.setR44_QFHA(rs.getBigDecimal("R44_QFHA"));
		obj.setR45_PRODUCT(rs.getString("R45_PRODUCT")); obj.setR45_NV_LONG(rs.getBigDecimal("R45_NV_LONG")); obj.setR45_NV_SHORT(rs.getBigDecimal("R45_NV_SHORT"));
		obj.setR45_FV_LONG(rs.getBigDecimal("R45_FV_LONG")); obj.setR45_FV_SHORT(rs.getBigDecimal("R45_FV_SHORT")); obj.setR45_QFHA(rs.getBigDecimal("R45_QFHA"));
		obj.setR46_PRODUCT(rs.getString("R46_PRODUCT")); obj.setR46_NV_LONG(rs.getBigDecimal("R46_NV_LONG")); obj.setR46_NV_SHORT(rs.getBigDecimal("R46_NV_SHORT"));
		obj.setR46_FV_LONG(rs.getBigDecimal("R46_FV_LONG")); obj.setR46_FV_SHORT(rs.getBigDecimal("R46_FV_SHORT")); obj.setR46_QFHA(rs.getBigDecimal("R46_QFHA"));
		obj.setR47_PRODUCT(rs.getString("R47_PRODUCT")); obj.setR47_NV_LONG(rs.getBigDecimal("R47_NV_LONG")); obj.setR47_NV_SHORT(rs.getBigDecimal("R47_NV_SHORT"));
		obj.setR47_FV_LONG(rs.getBigDecimal("R47_FV_LONG")); obj.setR47_FV_SHORT(rs.getBigDecimal("R47_FV_SHORT")); obj.setR47_QFHA(rs.getBigDecimal("R47_QFHA"));
		obj.setR48_PRODUCT(rs.getString("R48_PRODUCT")); obj.setR48_NV_LONG(rs.getBigDecimal("R48_NV_LONG")); obj.setR48_NV_SHORT(rs.getBigDecimal("R48_NV_SHORT"));
		obj.setR48_FV_LONG(rs.getBigDecimal("R48_FV_LONG")); obj.setR48_FV_SHORT(rs.getBigDecimal("R48_FV_SHORT")); obj.setR48_QFHA(rs.getBigDecimal("R48_QFHA"));
		obj.setR49_PRODUCT(rs.getString("R49_PRODUCT")); obj.setR49_NV_LONG(rs.getBigDecimal("R49_NV_LONG")); obj.setR49_NV_SHORT(rs.getBigDecimal("R49_NV_SHORT"));
		obj.setR49_FV_LONG(rs.getBigDecimal("R49_FV_LONG")); obj.setR49_FV_SHORT(rs.getBigDecimal("R49_FV_SHORT")); obj.setR49_QFHA(rs.getBigDecimal("R49_QFHA"));
		obj.setR50_PRODUCT(rs.getString("R50_PRODUCT")); obj.setR50_NV_LONG(rs.getBigDecimal("R50_NV_LONG")); obj.setR50_NV_SHORT(rs.getBigDecimal("R50_NV_SHORT"));
		obj.setR50_FV_LONG(rs.getBigDecimal("R50_FV_LONG")); obj.setR50_FV_SHORT(rs.getBigDecimal("R50_FV_SHORT")); obj.setR50_QFHA(rs.getBigDecimal("R50_QFHA"));
		obj.setR51_PRODUCT(rs.getString("R51_PRODUCT")); obj.setR51_NV_LONG(rs.getBigDecimal("R51_NV_LONG")); obj.setR51_NV_SHORT(rs.getBigDecimal("R51_NV_SHORT"));
		obj.setR51_FV_LONG(rs.getBigDecimal("R51_FV_LONG")); obj.setR51_FV_SHORT(rs.getBigDecimal("R51_FV_SHORT")); obj.setR51_QFHA(rs.getBigDecimal("R51_QFHA"));
		obj.setR52_PRODUCT(rs.getString("R52_PRODUCT")); obj.setR52_NV_LONG(rs.getBigDecimal("R52_NV_LONG")); obj.setR52_NV_SHORT(rs.getBigDecimal("R52_NV_SHORT"));
		obj.setR52_FV_LONG(rs.getBigDecimal("R52_FV_LONG")); obj.setR52_FV_SHORT(rs.getBigDecimal("R52_FV_SHORT")); obj.setR52_QFHA(rs.getBigDecimal("R52_QFHA"));
		obj.setR53_PRODUCT(rs.getString("R53_PRODUCT")); obj.setR53_NV_LONG(rs.getBigDecimal("R53_NV_LONG")); obj.setR53_NV_SHORT(rs.getBigDecimal("R53_NV_SHORT"));
		obj.setR53_FV_LONG(rs.getBigDecimal("R53_FV_LONG")); obj.setR53_FV_SHORT(rs.getBigDecimal("R53_FV_SHORT")); obj.setR53_QFHA(rs.getBigDecimal("R53_QFHA"));
		obj.setR54_PRODUCT(rs.getString("R54_PRODUCT")); obj.setR54_NV_LONG(rs.getBigDecimal("R54_NV_LONG")); obj.setR54_NV_SHORT(rs.getBigDecimal("R54_NV_SHORT"));
		obj.setR54_FV_LONG(rs.getBigDecimal("R54_FV_LONG")); obj.setR54_FV_SHORT(rs.getBigDecimal("R54_FV_SHORT")); obj.setR54_QFHA(rs.getBigDecimal("R54_QFHA"));
		obj.setR55_PRODUCT(rs.getString("R55_PRODUCT")); obj.setR55_NV_LONG(rs.getBigDecimal("R55_NV_LONG")); obj.setR55_NV_SHORT(rs.getBigDecimal("R55_NV_SHORT"));
		obj.setR55_FV_LONG(rs.getBigDecimal("R55_FV_LONG")); obj.setR55_FV_SHORT(rs.getBigDecimal("R55_FV_SHORT")); obj.setR55_QFHA(rs.getBigDecimal("R55_QFHA"));
			return obj;
		}
	}
	class M_TBSResubSummaryRowMapper implements RowMapper<M_TBS_Resub_Summary_Entity> {
		@Override
		public M_TBS_Resub_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_TBS_Resub_Summary_Entity obj = new M_TBS_Resub_Summary_Entity();
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
		obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));
		obj.setR11_PRODUCT(rs.getString("R11_PRODUCT")); obj.setR11_NV_LONG(rs.getBigDecimal("R11_NV_LONG")); obj.setR11_NV_SHORT(rs.getBigDecimal("R11_NV_SHORT"));
		obj.setR11_FV_LONG(rs.getBigDecimal("R11_FV_LONG")); obj.setR11_FV_SHORT(rs.getBigDecimal("R11_FV_SHORT")); obj.setR11_QFHA(rs.getBigDecimal("R11_QFHA"));
		obj.setR12_PRODUCT(rs.getString("R12_PRODUCT")); obj.setR12_NV_LONG(rs.getBigDecimal("R12_NV_LONG")); obj.setR12_NV_SHORT(rs.getBigDecimal("R12_NV_SHORT"));
		obj.setR12_FV_LONG(rs.getBigDecimal("R12_FV_LONG")); obj.setR12_FV_SHORT(rs.getBigDecimal("R12_FV_SHORT")); obj.setR12_QFHA(rs.getBigDecimal("R12_QFHA"));
		obj.setR13_PRODUCT(rs.getString("R13_PRODUCT")); obj.setR13_NV_LONG(rs.getBigDecimal("R13_NV_LONG")); obj.setR13_NV_SHORT(rs.getBigDecimal("R13_NV_SHORT"));
		obj.setR13_FV_LONG(rs.getBigDecimal("R13_FV_LONG")); obj.setR13_FV_SHORT(rs.getBigDecimal("R13_FV_SHORT")); obj.setR13_QFHA(rs.getBigDecimal("R13_QFHA"));
		obj.setR14_PRODUCT(rs.getString("R14_PRODUCT")); obj.setR14_NV_LONG(rs.getBigDecimal("R14_NV_LONG")); obj.setR14_NV_SHORT(rs.getBigDecimal("R14_NV_SHORT"));
		obj.setR14_FV_LONG(rs.getBigDecimal("R14_FV_LONG")); obj.setR14_FV_SHORT(rs.getBigDecimal("R14_FV_SHORT")); obj.setR14_QFHA(rs.getBigDecimal("R14_QFHA"));
		obj.setR15_PRODUCT(rs.getString("R15_PRODUCT")); obj.setR15_NV_LONG(rs.getBigDecimal("R15_NV_LONG")); obj.setR15_NV_SHORT(rs.getBigDecimal("R15_NV_SHORT"));
		obj.setR15_FV_LONG(rs.getBigDecimal("R15_FV_LONG")); obj.setR15_FV_SHORT(rs.getBigDecimal("R15_FV_SHORT")); obj.setR15_QFHA(rs.getBigDecimal("R15_QFHA"));
		obj.setR16_PRODUCT(rs.getString("R16_PRODUCT")); obj.setR16_NV_LONG(rs.getBigDecimal("R16_NV_LONG")); obj.setR16_NV_SHORT(rs.getBigDecimal("R16_NV_SHORT"));
		obj.setR16_FV_LONG(rs.getBigDecimal("R16_FV_LONG")); obj.setR16_FV_SHORT(rs.getBigDecimal("R16_FV_SHORT")); obj.setR16_QFHA(rs.getBigDecimal("R16_QFHA"));
		obj.setR17_PRODUCT(rs.getString("R17_PRODUCT")); obj.setR17_NV_LONG(rs.getBigDecimal("R17_NV_LONG")); obj.setR17_NV_SHORT(rs.getBigDecimal("R17_NV_SHORT"));
		obj.setR17_FV_LONG(rs.getBigDecimal("R17_FV_LONG")); obj.setR17_FV_SHORT(rs.getBigDecimal("R17_FV_SHORT")); obj.setR17_QFHA(rs.getBigDecimal("R17_QFHA"));
		obj.setR18_PRODUCT(rs.getString("R18_PRODUCT")); obj.setR18_NV_LONG(rs.getBigDecimal("R18_NV_LONG")); obj.setR18_NV_SHORT(rs.getBigDecimal("R18_NV_SHORT"));
		obj.setR18_FV_LONG(rs.getBigDecimal("R18_FV_LONG")); obj.setR18_FV_SHORT(rs.getBigDecimal("R18_FV_SHORT")); obj.setR18_QFHA(rs.getBigDecimal("R18_QFHA"));
		obj.setR19_PRODUCT(rs.getString("R19_PRODUCT")); obj.setR19_NV_LONG(rs.getBigDecimal("R19_NV_LONG")); obj.setR19_NV_SHORT(rs.getBigDecimal("R19_NV_SHORT"));
		obj.setR19_FV_LONG(rs.getBigDecimal("R19_FV_LONG")); obj.setR19_FV_SHORT(rs.getBigDecimal("R19_FV_SHORT")); obj.setR19_QFHA(rs.getBigDecimal("R19_QFHA"));
		obj.setR20_PRODUCT(rs.getString("R20_PRODUCT")); obj.setR20_NV_LONG(rs.getBigDecimal("R20_NV_LONG")); obj.setR20_NV_SHORT(rs.getBigDecimal("R20_NV_SHORT"));
		obj.setR20_FV_LONG(rs.getBigDecimal("R20_FV_LONG")); obj.setR20_FV_SHORT(rs.getBigDecimal("R20_FV_SHORT")); obj.setR20_QFHA(rs.getBigDecimal("R20_QFHA"));
		obj.setR21_PRODUCT(rs.getString("R21_PRODUCT")); obj.setR21_NV_LONG(rs.getBigDecimal("R21_NV_LONG")); obj.setR21_NV_SHORT(rs.getBigDecimal("R21_NV_SHORT"));
		obj.setR21_FV_LONG(rs.getBigDecimal("R21_FV_LONG")); obj.setR21_FV_SHORT(rs.getBigDecimal("R21_FV_SHORT")); obj.setR21_QFHA(rs.getBigDecimal("R21_QFHA"));
		obj.setR22_PRODUCT(rs.getString("R22_PRODUCT")); obj.setR22_NV_LONG(rs.getBigDecimal("R22_NV_LONG")); obj.setR22_NV_SHORT(rs.getBigDecimal("R22_NV_SHORT"));
		obj.setR22_FV_LONG(rs.getBigDecimal("R22_FV_LONG")); obj.setR22_FV_SHORT(rs.getBigDecimal("R22_FV_SHORT")); obj.setR22_QFHA(rs.getBigDecimal("R22_QFHA"));
		obj.setR23_PRODUCT(rs.getString("R23_PRODUCT")); obj.setR23_NV_LONG(rs.getBigDecimal("R23_NV_LONG")); obj.setR23_NV_SHORT(rs.getBigDecimal("R23_NV_SHORT"));
		obj.setR23_FV_LONG(rs.getBigDecimal("R23_FV_LONG")); obj.setR23_FV_SHORT(rs.getBigDecimal("R23_FV_SHORT")); obj.setR23_QFHA(rs.getBigDecimal("R23_QFHA"));
		obj.setR24_PRODUCT(rs.getString("R24_PRODUCT")); obj.setR24_NV_LONG(rs.getBigDecimal("R24_NV_LONG")); obj.setR24_NV_SHORT(rs.getBigDecimal("R24_NV_SHORT"));
		obj.setR24_FV_LONG(rs.getBigDecimal("R24_FV_LONG")); obj.setR24_FV_SHORT(rs.getBigDecimal("R24_FV_SHORT")); obj.setR24_QFHA(rs.getBigDecimal("R24_QFHA"));
		obj.setR25_PRODUCT(rs.getString("R25_PRODUCT")); obj.setR25_NV_LONG(rs.getBigDecimal("R25_NV_LONG")); obj.setR25_NV_SHORT(rs.getBigDecimal("R25_NV_SHORT"));
		obj.setR25_FV_LONG(rs.getBigDecimal("R25_FV_LONG")); obj.setR25_FV_SHORT(rs.getBigDecimal("R25_FV_SHORT")); obj.setR25_QFHA(rs.getBigDecimal("R25_QFHA"));
		obj.setR26_PRODUCT(rs.getString("R26_PRODUCT")); obj.setR26_NV_LONG(rs.getBigDecimal("R26_NV_LONG")); obj.setR26_NV_SHORT(rs.getBigDecimal("R26_NV_SHORT"));
		obj.setR26_FV_LONG(rs.getBigDecimal("R26_FV_LONG")); obj.setR26_FV_SHORT(rs.getBigDecimal("R26_FV_SHORT")); obj.setR26_QFHA(rs.getBigDecimal("R26_QFHA"));
		obj.setR27_PRODUCT(rs.getString("R27_PRODUCT")); obj.setR27_NV_LONG(rs.getBigDecimal("R27_NV_LONG")); obj.setR27_NV_SHORT(rs.getBigDecimal("R27_NV_SHORT"));
		obj.setR27_FV_LONG(rs.getBigDecimal("R27_FV_LONG")); obj.setR27_FV_SHORT(rs.getBigDecimal("R27_FV_SHORT")); obj.setR27_QFHA(rs.getBigDecimal("R27_QFHA"));
		obj.setR28_PRODUCT(rs.getString("R28_PRODUCT")); obj.setR28_NV_LONG(rs.getBigDecimal("R28_NV_LONG")); obj.setR28_NV_SHORT(rs.getBigDecimal("R28_NV_SHORT"));
		obj.setR28_FV_LONG(rs.getBigDecimal("R28_FV_LONG")); obj.setR28_FV_SHORT(rs.getBigDecimal("R28_FV_SHORT")); obj.setR28_QFHA(rs.getBigDecimal("R28_QFHA"));
		obj.setR29_PRODUCT(rs.getString("R29_PRODUCT")); obj.setR29_NV_LONG(rs.getBigDecimal("R29_NV_LONG")); obj.setR29_NV_SHORT(rs.getBigDecimal("R29_NV_SHORT"));
		obj.setR29_FV_LONG(rs.getBigDecimal("R29_FV_LONG")); obj.setR29_FV_SHORT(rs.getBigDecimal("R29_FV_SHORT")); obj.setR29_QFHA(rs.getBigDecimal("R29_QFHA"));
		obj.setR30_PRODUCT(rs.getString("R30_PRODUCT")); obj.setR30_NV_LONG(rs.getBigDecimal("R30_NV_LONG")); obj.setR30_NV_SHORT(rs.getBigDecimal("R30_NV_SHORT"));
		obj.setR30_FV_LONG(rs.getBigDecimal("R30_FV_LONG")); obj.setR30_FV_SHORT(rs.getBigDecimal("R30_FV_SHORT")); obj.setR30_QFHA(rs.getBigDecimal("R30_QFHA"));
		obj.setR31_PRODUCT(rs.getString("R31_PRODUCT")); obj.setR31_NV_LONG(rs.getBigDecimal("R31_NV_LONG")); obj.setR31_NV_SHORT(rs.getBigDecimal("R31_NV_SHORT"));
		obj.setR31_FV_LONG(rs.getBigDecimal("R31_FV_LONG")); obj.setR31_FV_SHORT(rs.getBigDecimal("R31_FV_SHORT")); obj.setR31_QFHA(rs.getBigDecimal("R31_QFHA"));
		obj.setR32_PRODUCT(rs.getString("R32_PRODUCT")); obj.setR32_NV_LONG(rs.getBigDecimal("R32_NV_LONG")); obj.setR32_NV_SHORT(rs.getBigDecimal("R32_NV_SHORT"));
		obj.setR32_FV_LONG(rs.getBigDecimal("R32_FV_LONG")); obj.setR32_FV_SHORT(rs.getBigDecimal("R32_FV_SHORT")); obj.setR32_QFHA(rs.getBigDecimal("R32_QFHA"));
		obj.setR33_PRODUCT(rs.getString("R33_PRODUCT")); obj.setR33_NV_LONG(rs.getBigDecimal("R33_NV_LONG")); obj.setR33_NV_SHORT(rs.getBigDecimal("R33_NV_SHORT"));
		obj.setR33_FV_LONG(rs.getBigDecimal("R33_FV_LONG")); obj.setR33_FV_SHORT(rs.getBigDecimal("R33_FV_SHORT")); obj.setR33_QFHA(rs.getBigDecimal("R33_QFHA"));
		obj.setR34_PRODUCT(rs.getString("R34_PRODUCT")); obj.setR34_NV_LONG(rs.getBigDecimal("R34_NV_LONG")); obj.setR34_NV_SHORT(rs.getBigDecimal("R34_NV_SHORT"));
		obj.setR34_FV_LONG(rs.getBigDecimal("R34_FV_LONG")); obj.setR34_FV_SHORT(rs.getBigDecimal("R34_FV_SHORT")); obj.setR34_QFHA(rs.getBigDecimal("R34_QFHA"));
		obj.setR35_PRODUCT(rs.getString("R35_PRODUCT")); obj.setR35_NV_LONG(rs.getBigDecimal("R35_NV_LONG")); obj.setR35_NV_SHORT(rs.getBigDecimal("R35_NV_SHORT"));
		obj.setR35_FV_LONG(rs.getBigDecimal("R35_FV_LONG")); obj.setR35_FV_SHORT(rs.getBigDecimal("R35_FV_SHORT")); obj.setR35_QFHA(rs.getBigDecimal("R35_QFHA"));
		obj.setR36_PRODUCT(rs.getString("R36_PRODUCT")); obj.setR36_NV_LONG(rs.getBigDecimal("R36_NV_LONG")); obj.setR36_NV_SHORT(rs.getBigDecimal("R36_NV_SHORT"));
		obj.setR36_FV_LONG(rs.getBigDecimal("R36_FV_LONG")); obj.setR36_FV_SHORT(rs.getBigDecimal("R36_FV_SHORT")); obj.setR36_QFHA(rs.getBigDecimal("R36_QFHA"));
		obj.setR37_PRODUCT(rs.getString("R37_PRODUCT")); obj.setR37_NV_LONG(rs.getBigDecimal("R37_NV_LONG")); obj.setR37_NV_SHORT(rs.getBigDecimal("R37_NV_SHORT"));
		obj.setR37_FV_LONG(rs.getBigDecimal("R37_FV_LONG")); obj.setR37_FV_SHORT(rs.getBigDecimal("R37_FV_SHORT")); obj.setR37_QFHA(rs.getBigDecimal("R37_QFHA"));
		obj.setR38_PRODUCT(rs.getString("R38_PRODUCT")); obj.setR38_NV_LONG(rs.getBigDecimal("R38_NV_LONG")); obj.setR38_NV_SHORT(rs.getBigDecimal("R38_NV_SHORT"));
		obj.setR38_FV_LONG(rs.getBigDecimal("R38_FV_LONG")); obj.setR38_FV_SHORT(rs.getBigDecimal("R38_FV_SHORT")); obj.setR38_QFHA(rs.getBigDecimal("R38_QFHA"));
		obj.setR39_PRODUCT(rs.getString("R39_PRODUCT")); obj.setR39_NV_LONG(rs.getBigDecimal("R39_NV_LONG")); obj.setR39_NV_SHORT(rs.getBigDecimal("R39_NV_SHORT"));
		obj.setR39_FV_LONG(rs.getBigDecimal("R39_FV_LONG")); obj.setR39_FV_SHORT(rs.getBigDecimal("R39_FV_SHORT")); obj.setR39_QFHA(rs.getBigDecimal("R39_QFHA"));
		obj.setR40_PRODUCT(rs.getString("R40_PRODUCT")); obj.setR40_NV_LONG(rs.getBigDecimal("R40_NV_LONG")); obj.setR40_NV_SHORT(rs.getBigDecimal("R40_NV_SHORT"));
		obj.setR40_FV_LONG(rs.getBigDecimal("R40_FV_LONG")); obj.setR40_FV_SHORT(rs.getBigDecimal("R40_FV_SHORT")); obj.setR40_QFHA(rs.getBigDecimal("R40_QFHA"));
		obj.setR41_PRODUCT(rs.getString("R41_PRODUCT")); obj.setR41_NV_LONG(rs.getBigDecimal("R41_NV_LONG")); obj.setR41_NV_SHORT(rs.getBigDecimal("R41_NV_SHORT"));
		obj.setR41_FV_LONG(rs.getBigDecimal("R41_FV_LONG")); obj.setR41_FV_SHORT(rs.getBigDecimal("R41_FV_SHORT")); obj.setR41_QFHA(rs.getBigDecimal("R41_QFHA"));
		obj.setR42_PRODUCT(rs.getString("R42_PRODUCT")); obj.setR42_NV_LONG(rs.getBigDecimal("R42_NV_LONG")); obj.setR42_NV_SHORT(rs.getBigDecimal("R42_NV_SHORT"));
		obj.setR42_FV_LONG(rs.getBigDecimal("R42_FV_LONG")); obj.setR42_FV_SHORT(rs.getBigDecimal("R42_FV_SHORT")); obj.setR42_QFHA(rs.getBigDecimal("R42_QFHA"));
		obj.setR43_PRODUCT(rs.getString("R43_PRODUCT")); obj.setR43_NV_LONG(rs.getBigDecimal("R43_NV_LONG")); obj.setR43_NV_SHORT(rs.getBigDecimal("R43_NV_SHORT"));
		obj.setR43_FV_LONG(rs.getBigDecimal("R43_FV_LONG")); obj.setR43_FV_SHORT(rs.getBigDecimal("R43_FV_SHORT")); obj.setR43_QFHA(rs.getBigDecimal("R43_QFHA"));
		obj.setR44_PRODUCT(rs.getString("R44_PRODUCT")); obj.setR44_NV_LONG(rs.getBigDecimal("R44_NV_LONG")); obj.setR44_NV_SHORT(rs.getBigDecimal("R44_NV_SHORT"));
		obj.setR44_FV_LONG(rs.getBigDecimal("R44_FV_LONG")); obj.setR44_FV_SHORT(rs.getBigDecimal("R44_FV_SHORT")); obj.setR44_QFHA(rs.getBigDecimal("R44_QFHA"));
		obj.setR45_PRODUCT(rs.getString("R45_PRODUCT")); obj.setR45_NV_LONG(rs.getBigDecimal("R45_NV_LONG")); obj.setR45_NV_SHORT(rs.getBigDecimal("R45_NV_SHORT"));
		obj.setR45_FV_LONG(rs.getBigDecimal("R45_FV_LONG")); obj.setR45_FV_SHORT(rs.getBigDecimal("R45_FV_SHORT")); obj.setR45_QFHA(rs.getBigDecimal("R45_QFHA"));
		obj.setR46_PRODUCT(rs.getString("R46_PRODUCT")); obj.setR46_NV_LONG(rs.getBigDecimal("R46_NV_LONG")); obj.setR46_NV_SHORT(rs.getBigDecimal("R46_NV_SHORT"));
		obj.setR46_FV_LONG(rs.getBigDecimal("R46_FV_LONG")); obj.setR46_FV_SHORT(rs.getBigDecimal("R46_FV_SHORT")); obj.setR46_QFHA(rs.getBigDecimal("R46_QFHA"));
		obj.setR47_PRODUCT(rs.getString("R47_PRODUCT")); obj.setR47_NV_LONG(rs.getBigDecimal("R47_NV_LONG")); obj.setR47_NV_SHORT(rs.getBigDecimal("R47_NV_SHORT"));
		obj.setR47_FV_LONG(rs.getBigDecimal("R47_FV_LONG")); obj.setR47_FV_SHORT(rs.getBigDecimal("R47_FV_SHORT")); obj.setR47_QFHA(rs.getBigDecimal("R47_QFHA"));
		obj.setR48_PRODUCT(rs.getString("R48_PRODUCT")); obj.setR48_NV_LONG(rs.getBigDecimal("R48_NV_LONG")); obj.setR48_NV_SHORT(rs.getBigDecimal("R48_NV_SHORT"));
		obj.setR48_FV_LONG(rs.getBigDecimal("R48_FV_LONG")); obj.setR48_FV_SHORT(rs.getBigDecimal("R48_FV_SHORT")); obj.setR48_QFHA(rs.getBigDecimal("R48_QFHA"));
		obj.setR49_PRODUCT(rs.getString("R49_PRODUCT")); obj.setR49_NV_LONG(rs.getBigDecimal("R49_NV_LONG")); obj.setR49_NV_SHORT(rs.getBigDecimal("R49_NV_SHORT"));
		obj.setR49_FV_LONG(rs.getBigDecimal("R49_FV_LONG")); obj.setR49_FV_SHORT(rs.getBigDecimal("R49_FV_SHORT")); obj.setR49_QFHA(rs.getBigDecimal("R49_QFHA"));
		obj.setR50_PRODUCT(rs.getString("R50_PRODUCT")); obj.setR50_NV_LONG(rs.getBigDecimal("R50_NV_LONG")); obj.setR50_NV_SHORT(rs.getBigDecimal("R50_NV_SHORT"));
		obj.setR50_FV_LONG(rs.getBigDecimal("R50_FV_LONG")); obj.setR50_FV_SHORT(rs.getBigDecimal("R50_FV_SHORT")); obj.setR50_QFHA(rs.getBigDecimal("R50_QFHA"));
		obj.setR51_PRODUCT(rs.getString("R51_PRODUCT")); obj.setR51_NV_LONG(rs.getBigDecimal("R51_NV_LONG")); obj.setR51_NV_SHORT(rs.getBigDecimal("R51_NV_SHORT"));
		obj.setR51_FV_LONG(rs.getBigDecimal("R51_FV_LONG")); obj.setR51_FV_SHORT(rs.getBigDecimal("R51_FV_SHORT")); obj.setR51_QFHA(rs.getBigDecimal("R51_QFHA"));
		obj.setR52_PRODUCT(rs.getString("R52_PRODUCT")); obj.setR52_NV_LONG(rs.getBigDecimal("R52_NV_LONG")); obj.setR52_NV_SHORT(rs.getBigDecimal("R52_NV_SHORT"));
		obj.setR52_FV_LONG(rs.getBigDecimal("R52_FV_LONG")); obj.setR52_FV_SHORT(rs.getBigDecimal("R52_FV_SHORT")); obj.setR52_QFHA(rs.getBigDecimal("R52_QFHA"));
		obj.setR53_PRODUCT(rs.getString("R53_PRODUCT")); obj.setR53_NV_LONG(rs.getBigDecimal("R53_NV_LONG")); obj.setR53_NV_SHORT(rs.getBigDecimal("R53_NV_SHORT"));
		obj.setR53_FV_LONG(rs.getBigDecimal("R53_FV_LONG")); obj.setR53_FV_SHORT(rs.getBigDecimal("R53_FV_SHORT")); obj.setR53_QFHA(rs.getBigDecimal("R53_QFHA"));
		obj.setR54_PRODUCT(rs.getString("R54_PRODUCT")); obj.setR54_NV_LONG(rs.getBigDecimal("R54_NV_LONG")); obj.setR54_NV_SHORT(rs.getBigDecimal("R54_NV_SHORT"));
		obj.setR54_FV_LONG(rs.getBigDecimal("R54_FV_LONG")); obj.setR54_FV_SHORT(rs.getBigDecimal("R54_FV_SHORT")); obj.setR54_QFHA(rs.getBigDecimal("R54_QFHA"));
		obj.setR55_PRODUCT(rs.getString("R55_PRODUCT")); obj.setR55_NV_LONG(rs.getBigDecimal("R55_NV_LONG")); obj.setR55_NV_SHORT(rs.getBigDecimal("R55_NV_SHORT"));
		obj.setR55_FV_LONG(rs.getBigDecimal("R55_FV_LONG")); obj.setR55_FV_SHORT(rs.getBigDecimal("R55_FV_SHORT")); obj.setR55_QFHA(rs.getBigDecimal("R55_QFHA"));
			return obj;
		}
	}
	class M_TBSResubDetailRowMapper implements RowMapper<M_TBS_Resub_Detail_Entity> {
		@Override
		public M_TBS_Resub_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_TBS_Resub_Detail_Entity obj = new M_TBS_Resub_Detail_Entity();
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
		obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));
		obj.setR11_PRODUCT(rs.getString("R11_PRODUCT")); obj.setR11_NV_LONG(rs.getBigDecimal("R11_NV_LONG")); obj.setR11_NV_SHORT(rs.getBigDecimal("R11_NV_SHORT"));
		obj.setR11_FV_LONG(rs.getBigDecimal("R11_FV_LONG")); obj.setR11_FV_SHORT(rs.getBigDecimal("R11_FV_SHORT")); obj.setR11_QFHA(rs.getBigDecimal("R11_QFHA"));
		obj.setR12_PRODUCT(rs.getString("R12_PRODUCT")); obj.setR12_NV_LONG(rs.getBigDecimal("R12_NV_LONG")); obj.setR12_NV_SHORT(rs.getBigDecimal("R12_NV_SHORT"));
		obj.setR12_FV_LONG(rs.getBigDecimal("R12_FV_LONG")); obj.setR12_FV_SHORT(rs.getBigDecimal("R12_FV_SHORT")); obj.setR12_QFHA(rs.getBigDecimal("R12_QFHA"));
		obj.setR13_PRODUCT(rs.getString("R13_PRODUCT")); obj.setR13_NV_LONG(rs.getBigDecimal("R13_NV_LONG")); obj.setR13_NV_SHORT(rs.getBigDecimal("R13_NV_SHORT"));
		obj.setR13_FV_LONG(rs.getBigDecimal("R13_FV_LONG")); obj.setR13_FV_SHORT(rs.getBigDecimal("R13_FV_SHORT")); obj.setR13_QFHA(rs.getBigDecimal("R13_QFHA"));
		obj.setR14_PRODUCT(rs.getString("R14_PRODUCT")); obj.setR14_NV_LONG(rs.getBigDecimal("R14_NV_LONG")); obj.setR14_NV_SHORT(rs.getBigDecimal("R14_NV_SHORT"));
		obj.setR14_FV_LONG(rs.getBigDecimal("R14_FV_LONG")); obj.setR14_FV_SHORT(rs.getBigDecimal("R14_FV_SHORT")); obj.setR14_QFHA(rs.getBigDecimal("R14_QFHA"));
		obj.setR15_PRODUCT(rs.getString("R15_PRODUCT")); obj.setR15_NV_LONG(rs.getBigDecimal("R15_NV_LONG")); obj.setR15_NV_SHORT(rs.getBigDecimal("R15_NV_SHORT"));
		obj.setR15_FV_LONG(rs.getBigDecimal("R15_FV_LONG")); obj.setR15_FV_SHORT(rs.getBigDecimal("R15_FV_SHORT")); obj.setR15_QFHA(rs.getBigDecimal("R15_QFHA"));
		obj.setR16_PRODUCT(rs.getString("R16_PRODUCT")); obj.setR16_NV_LONG(rs.getBigDecimal("R16_NV_LONG")); obj.setR16_NV_SHORT(rs.getBigDecimal("R16_NV_SHORT"));
		obj.setR16_FV_LONG(rs.getBigDecimal("R16_FV_LONG")); obj.setR16_FV_SHORT(rs.getBigDecimal("R16_FV_SHORT")); obj.setR16_QFHA(rs.getBigDecimal("R16_QFHA"));
		obj.setR17_PRODUCT(rs.getString("R17_PRODUCT")); obj.setR17_NV_LONG(rs.getBigDecimal("R17_NV_LONG")); obj.setR17_NV_SHORT(rs.getBigDecimal("R17_NV_SHORT"));
		obj.setR17_FV_LONG(rs.getBigDecimal("R17_FV_LONG")); obj.setR17_FV_SHORT(rs.getBigDecimal("R17_FV_SHORT")); obj.setR17_QFHA(rs.getBigDecimal("R17_QFHA"));
		obj.setR18_PRODUCT(rs.getString("R18_PRODUCT")); obj.setR18_NV_LONG(rs.getBigDecimal("R18_NV_LONG")); obj.setR18_NV_SHORT(rs.getBigDecimal("R18_NV_SHORT"));
		obj.setR18_FV_LONG(rs.getBigDecimal("R18_FV_LONG")); obj.setR18_FV_SHORT(rs.getBigDecimal("R18_FV_SHORT")); obj.setR18_QFHA(rs.getBigDecimal("R18_QFHA"));
		obj.setR19_PRODUCT(rs.getString("R19_PRODUCT")); obj.setR19_NV_LONG(rs.getBigDecimal("R19_NV_LONG")); obj.setR19_NV_SHORT(rs.getBigDecimal("R19_NV_SHORT"));
		obj.setR19_FV_LONG(rs.getBigDecimal("R19_FV_LONG")); obj.setR19_FV_SHORT(rs.getBigDecimal("R19_FV_SHORT")); obj.setR19_QFHA(rs.getBigDecimal("R19_QFHA"));
		obj.setR20_PRODUCT(rs.getString("R20_PRODUCT")); obj.setR20_NV_LONG(rs.getBigDecimal("R20_NV_LONG")); obj.setR20_NV_SHORT(rs.getBigDecimal("R20_NV_SHORT"));
		obj.setR20_FV_LONG(rs.getBigDecimal("R20_FV_LONG")); obj.setR20_FV_SHORT(rs.getBigDecimal("R20_FV_SHORT")); obj.setR20_QFHA(rs.getBigDecimal("R20_QFHA"));
		obj.setR21_PRODUCT(rs.getString("R21_PRODUCT")); obj.setR21_NV_LONG(rs.getBigDecimal("R21_NV_LONG")); obj.setR21_NV_SHORT(rs.getBigDecimal("R21_NV_SHORT"));
		obj.setR21_FV_LONG(rs.getBigDecimal("R21_FV_LONG")); obj.setR21_FV_SHORT(rs.getBigDecimal("R21_FV_SHORT")); obj.setR21_QFHA(rs.getBigDecimal("R21_QFHA"));
		obj.setR22_PRODUCT(rs.getString("R22_PRODUCT")); obj.setR22_NV_LONG(rs.getBigDecimal("R22_NV_LONG")); obj.setR22_NV_SHORT(rs.getBigDecimal("R22_NV_SHORT"));
		obj.setR22_FV_LONG(rs.getBigDecimal("R22_FV_LONG")); obj.setR22_FV_SHORT(rs.getBigDecimal("R22_FV_SHORT")); obj.setR22_QFHA(rs.getBigDecimal("R22_QFHA"));
		obj.setR23_PRODUCT(rs.getString("R23_PRODUCT")); obj.setR23_NV_LONG(rs.getBigDecimal("R23_NV_LONG")); obj.setR23_NV_SHORT(rs.getBigDecimal("R23_NV_SHORT"));
		obj.setR23_FV_LONG(rs.getBigDecimal("R23_FV_LONG")); obj.setR23_FV_SHORT(rs.getBigDecimal("R23_FV_SHORT")); obj.setR23_QFHA(rs.getBigDecimal("R23_QFHA"));
		obj.setR24_PRODUCT(rs.getString("R24_PRODUCT")); obj.setR24_NV_LONG(rs.getBigDecimal("R24_NV_LONG")); obj.setR24_NV_SHORT(rs.getBigDecimal("R24_NV_SHORT"));
		obj.setR24_FV_LONG(rs.getBigDecimal("R24_FV_LONG")); obj.setR24_FV_SHORT(rs.getBigDecimal("R24_FV_SHORT")); obj.setR24_QFHA(rs.getBigDecimal("R24_QFHA"));
		obj.setR25_PRODUCT(rs.getString("R25_PRODUCT")); obj.setR25_NV_LONG(rs.getBigDecimal("R25_NV_LONG")); obj.setR25_NV_SHORT(rs.getBigDecimal("R25_NV_SHORT"));
		obj.setR25_FV_LONG(rs.getBigDecimal("R25_FV_LONG")); obj.setR25_FV_SHORT(rs.getBigDecimal("R25_FV_SHORT")); obj.setR25_QFHA(rs.getBigDecimal("R25_QFHA"));
		obj.setR26_PRODUCT(rs.getString("R26_PRODUCT")); obj.setR26_NV_LONG(rs.getBigDecimal("R26_NV_LONG")); obj.setR26_NV_SHORT(rs.getBigDecimal("R26_NV_SHORT"));
		obj.setR26_FV_LONG(rs.getBigDecimal("R26_FV_LONG")); obj.setR26_FV_SHORT(rs.getBigDecimal("R26_FV_SHORT")); obj.setR26_QFHA(rs.getBigDecimal("R26_QFHA"));
		obj.setR27_PRODUCT(rs.getString("R27_PRODUCT")); obj.setR27_NV_LONG(rs.getBigDecimal("R27_NV_LONG")); obj.setR27_NV_SHORT(rs.getBigDecimal("R27_NV_SHORT"));
		obj.setR27_FV_LONG(rs.getBigDecimal("R27_FV_LONG")); obj.setR27_FV_SHORT(rs.getBigDecimal("R27_FV_SHORT")); obj.setR27_QFHA(rs.getBigDecimal("R27_QFHA"));
		obj.setR28_PRODUCT(rs.getString("R28_PRODUCT")); obj.setR28_NV_LONG(rs.getBigDecimal("R28_NV_LONG")); obj.setR28_NV_SHORT(rs.getBigDecimal("R28_NV_SHORT"));
		obj.setR28_FV_LONG(rs.getBigDecimal("R28_FV_LONG")); obj.setR28_FV_SHORT(rs.getBigDecimal("R28_FV_SHORT")); obj.setR28_QFHA(rs.getBigDecimal("R28_QFHA"));
		obj.setR29_PRODUCT(rs.getString("R29_PRODUCT")); obj.setR29_NV_LONG(rs.getBigDecimal("R29_NV_LONG")); obj.setR29_NV_SHORT(rs.getBigDecimal("R29_NV_SHORT"));
		obj.setR29_FV_LONG(rs.getBigDecimal("R29_FV_LONG")); obj.setR29_FV_SHORT(rs.getBigDecimal("R29_FV_SHORT")); obj.setR29_QFHA(rs.getBigDecimal("R29_QFHA"));
		obj.setR30_PRODUCT(rs.getString("R30_PRODUCT")); obj.setR30_NV_LONG(rs.getBigDecimal("R30_NV_LONG")); obj.setR30_NV_SHORT(rs.getBigDecimal("R30_NV_SHORT"));
		obj.setR30_FV_LONG(rs.getBigDecimal("R30_FV_LONG")); obj.setR30_FV_SHORT(rs.getBigDecimal("R30_FV_SHORT")); obj.setR30_QFHA(rs.getBigDecimal("R30_QFHA"));
		obj.setR31_PRODUCT(rs.getString("R31_PRODUCT")); obj.setR31_NV_LONG(rs.getBigDecimal("R31_NV_LONG")); obj.setR31_NV_SHORT(rs.getBigDecimal("R31_NV_SHORT"));
		obj.setR31_FV_LONG(rs.getBigDecimal("R31_FV_LONG")); obj.setR31_FV_SHORT(rs.getBigDecimal("R31_FV_SHORT")); obj.setR31_QFHA(rs.getBigDecimal("R31_QFHA"));
		obj.setR32_PRODUCT(rs.getString("R32_PRODUCT")); obj.setR32_NV_LONG(rs.getBigDecimal("R32_NV_LONG")); obj.setR32_NV_SHORT(rs.getBigDecimal("R32_NV_SHORT"));
		obj.setR32_FV_LONG(rs.getBigDecimal("R32_FV_LONG")); obj.setR32_FV_SHORT(rs.getBigDecimal("R32_FV_SHORT")); obj.setR32_QFHA(rs.getBigDecimal("R32_QFHA"));
		obj.setR33_PRODUCT(rs.getString("R33_PRODUCT")); obj.setR33_NV_LONG(rs.getBigDecimal("R33_NV_LONG")); obj.setR33_NV_SHORT(rs.getBigDecimal("R33_NV_SHORT"));
		obj.setR33_FV_LONG(rs.getBigDecimal("R33_FV_LONG")); obj.setR33_FV_SHORT(rs.getBigDecimal("R33_FV_SHORT")); obj.setR33_QFHA(rs.getBigDecimal("R33_QFHA"));
		obj.setR34_PRODUCT(rs.getString("R34_PRODUCT")); obj.setR34_NV_LONG(rs.getBigDecimal("R34_NV_LONG")); obj.setR34_NV_SHORT(rs.getBigDecimal("R34_NV_SHORT"));
		obj.setR34_FV_LONG(rs.getBigDecimal("R34_FV_LONG")); obj.setR34_FV_SHORT(rs.getBigDecimal("R34_FV_SHORT")); obj.setR34_QFHA(rs.getBigDecimal("R34_QFHA"));
		obj.setR35_PRODUCT(rs.getString("R35_PRODUCT")); obj.setR35_NV_LONG(rs.getBigDecimal("R35_NV_LONG")); obj.setR35_NV_SHORT(rs.getBigDecimal("R35_NV_SHORT"));
		obj.setR35_FV_LONG(rs.getBigDecimal("R35_FV_LONG")); obj.setR35_FV_SHORT(rs.getBigDecimal("R35_FV_SHORT")); obj.setR35_QFHA(rs.getBigDecimal("R35_QFHA"));
		obj.setR36_PRODUCT(rs.getString("R36_PRODUCT")); obj.setR36_NV_LONG(rs.getBigDecimal("R36_NV_LONG")); obj.setR36_NV_SHORT(rs.getBigDecimal("R36_NV_SHORT"));
		obj.setR36_FV_LONG(rs.getBigDecimal("R36_FV_LONG")); obj.setR36_FV_SHORT(rs.getBigDecimal("R36_FV_SHORT")); obj.setR36_QFHA(rs.getBigDecimal("R36_QFHA"));
		obj.setR37_PRODUCT(rs.getString("R37_PRODUCT")); obj.setR37_NV_LONG(rs.getBigDecimal("R37_NV_LONG")); obj.setR37_NV_SHORT(rs.getBigDecimal("R37_NV_SHORT"));
		obj.setR37_FV_LONG(rs.getBigDecimal("R37_FV_LONG")); obj.setR37_FV_SHORT(rs.getBigDecimal("R37_FV_SHORT")); obj.setR37_QFHA(rs.getBigDecimal("R37_QFHA"));
		obj.setR38_PRODUCT(rs.getString("R38_PRODUCT")); obj.setR38_NV_LONG(rs.getBigDecimal("R38_NV_LONG")); obj.setR38_NV_SHORT(rs.getBigDecimal("R38_NV_SHORT"));
		obj.setR38_FV_LONG(rs.getBigDecimal("R38_FV_LONG")); obj.setR38_FV_SHORT(rs.getBigDecimal("R38_FV_SHORT")); obj.setR38_QFHA(rs.getBigDecimal("R38_QFHA"));
		obj.setR39_PRODUCT(rs.getString("R39_PRODUCT")); obj.setR39_NV_LONG(rs.getBigDecimal("R39_NV_LONG")); obj.setR39_NV_SHORT(rs.getBigDecimal("R39_NV_SHORT"));
		obj.setR39_FV_LONG(rs.getBigDecimal("R39_FV_LONG")); obj.setR39_FV_SHORT(rs.getBigDecimal("R39_FV_SHORT")); obj.setR39_QFHA(rs.getBigDecimal("R39_QFHA"));
		obj.setR40_PRODUCT(rs.getString("R40_PRODUCT")); obj.setR40_NV_LONG(rs.getBigDecimal("R40_NV_LONG")); obj.setR40_NV_SHORT(rs.getBigDecimal("R40_NV_SHORT"));
		obj.setR40_FV_LONG(rs.getBigDecimal("R40_FV_LONG")); obj.setR40_FV_SHORT(rs.getBigDecimal("R40_FV_SHORT")); obj.setR40_QFHA(rs.getBigDecimal("R40_QFHA"));
		obj.setR41_PRODUCT(rs.getString("R41_PRODUCT")); obj.setR41_NV_LONG(rs.getBigDecimal("R41_NV_LONG")); obj.setR41_NV_SHORT(rs.getBigDecimal("R41_NV_SHORT"));
		obj.setR41_FV_LONG(rs.getBigDecimal("R41_FV_LONG")); obj.setR41_FV_SHORT(rs.getBigDecimal("R41_FV_SHORT")); obj.setR41_QFHA(rs.getBigDecimal("R41_QFHA"));
		obj.setR42_PRODUCT(rs.getString("R42_PRODUCT")); obj.setR42_NV_LONG(rs.getBigDecimal("R42_NV_LONG")); obj.setR42_NV_SHORT(rs.getBigDecimal("R42_NV_SHORT"));
		obj.setR42_FV_LONG(rs.getBigDecimal("R42_FV_LONG")); obj.setR42_FV_SHORT(rs.getBigDecimal("R42_FV_SHORT")); obj.setR42_QFHA(rs.getBigDecimal("R42_QFHA"));
		obj.setR43_PRODUCT(rs.getString("R43_PRODUCT")); obj.setR43_NV_LONG(rs.getBigDecimal("R43_NV_LONG")); obj.setR43_NV_SHORT(rs.getBigDecimal("R43_NV_SHORT"));
		obj.setR43_FV_LONG(rs.getBigDecimal("R43_FV_LONG")); obj.setR43_FV_SHORT(rs.getBigDecimal("R43_FV_SHORT")); obj.setR43_QFHA(rs.getBigDecimal("R43_QFHA"));
		obj.setR44_PRODUCT(rs.getString("R44_PRODUCT")); obj.setR44_NV_LONG(rs.getBigDecimal("R44_NV_LONG")); obj.setR44_NV_SHORT(rs.getBigDecimal("R44_NV_SHORT"));
		obj.setR44_FV_LONG(rs.getBigDecimal("R44_FV_LONG")); obj.setR44_FV_SHORT(rs.getBigDecimal("R44_FV_SHORT")); obj.setR44_QFHA(rs.getBigDecimal("R44_QFHA"));
		obj.setR45_PRODUCT(rs.getString("R45_PRODUCT")); obj.setR45_NV_LONG(rs.getBigDecimal("R45_NV_LONG")); obj.setR45_NV_SHORT(rs.getBigDecimal("R45_NV_SHORT"));
		obj.setR45_FV_LONG(rs.getBigDecimal("R45_FV_LONG")); obj.setR45_FV_SHORT(rs.getBigDecimal("R45_FV_SHORT")); obj.setR45_QFHA(rs.getBigDecimal("R45_QFHA"));
		obj.setR46_PRODUCT(rs.getString("R46_PRODUCT")); obj.setR46_NV_LONG(rs.getBigDecimal("R46_NV_LONG")); obj.setR46_NV_SHORT(rs.getBigDecimal("R46_NV_SHORT"));
		obj.setR46_FV_LONG(rs.getBigDecimal("R46_FV_LONG")); obj.setR46_FV_SHORT(rs.getBigDecimal("R46_FV_SHORT")); obj.setR46_QFHA(rs.getBigDecimal("R46_QFHA"));
		obj.setR47_PRODUCT(rs.getString("R47_PRODUCT")); obj.setR47_NV_LONG(rs.getBigDecimal("R47_NV_LONG")); obj.setR47_NV_SHORT(rs.getBigDecimal("R47_NV_SHORT"));
		obj.setR47_FV_LONG(rs.getBigDecimal("R47_FV_LONG")); obj.setR47_FV_SHORT(rs.getBigDecimal("R47_FV_SHORT")); obj.setR47_QFHA(rs.getBigDecimal("R47_QFHA"));
		obj.setR48_PRODUCT(rs.getString("R48_PRODUCT")); obj.setR48_NV_LONG(rs.getBigDecimal("R48_NV_LONG")); obj.setR48_NV_SHORT(rs.getBigDecimal("R48_NV_SHORT"));
		obj.setR48_FV_LONG(rs.getBigDecimal("R48_FV_LONG")); obj.setR48_FV_SHORT(rs.getBigDecimal("R48_FV_SHORT")); obj.setR48_QFHA(rs.getBigDecimal("R48_QFHA"));
		obj.setR49_PRODUCT(rs.getString("R49_PRODUCT")); obj.setR49_NV_LONG(rs.getBigDecimal("R49_NV_LONG")); obj.setR49_NV_SHORT(rs.getBigDecimal("R49_NV_SHORT"));
		obj.setR49_FV_LONG(rs.getBigDecimal("R49_FV_LONG")); obj.setR49_FV_SHORT(rs.getBigDecimal("R49_FV_SHORT")); obj.setR49_QFHA(rs.getBigDecimal("R49_QFHA"));
		obj.setR50_PRODUCT(rs.getString("R50_PRODUCT")); obj.setR50_NV_LONG(rs.getBigDecimal("R50_NV_LONG")); obj.setR50_NV_SHORT(rs.getBigDecimal("R50_NV_SHORT"));
		obj.setR50_FV_LONG(rs.getBigDecimal("R50_FV_LONG")); obj.setR50_FV_SHORT(rs.getBigDecimal("R50_FV_SHORT")); obj.setR50_QFHA(rs.getBigDecimal("R50_QFHA"));
		obj.setR51_PRODUCT(rs.getString("R51_PRODUCT")); obj.setR51_NV_LONG(rs.getBigDecimal("R51_NV_LONG")); obj.setR51_NV_SHORT(rs.getBigDecimal("R51_NV_SHORT"));
		obj.setR51_FV_LONG(rs.getBigDecimal("R51_FV_LONG")); obj.setR51_FV_SHORT(rs.getBigDecimal("R51_FV_SHORT")); obj.setR51_QFHA(rs.getBigDecimal("R51_QFHA"));
		obj.setR52_PRODUCT(rs.getString("R52_PRODUCT")); obj.setR52_NV_LONG(rs.getBigDecimal("R52_NV_LONG")); obj.setR52_NV_SHORT(rs.getBigDecimal("R52_NV_SHORT"));
		obj.setR52_FV_LONG(rs.getBigDecimal("R52_FV_LONG")); obj.setR52_FV_SHORT(rs.getBigDecimal("R52_FV_SHORT")); obj.setR52_QFHA(rs.getBigDecimal("R52_QFHA"));
		obj.setR53_PRODUCT(rs.getString("R53_PRODUCT")); obj.setR53_NV_LONG(rs.getBigDecimal("R53_NV_LONG")); obj.setR53_NV_SHORT(rs.getBigDecimal("R53_NV_SHORT"));
		obj.setR53_FV_LONG(rs.getBigDecimal("R53_FV_LONG")); obj.setR53_FV_SHORT(rs.getBigDecimal("R53_FV_SHORT")); obj.setR53_QFHA(rs.getBigDecimal("R53_QFHA"));
		obj.setR54_PRODUCT(rs.getString("R54_PRODUCT")); obj.setR54_NV_LONG(rs.getBigDecimal("R54_NV_LONG")); obj.setR54_NV_SHORT(rs.getBigDecimal("R54_NV_SHORT"));
		obj.setR54_FV_LONG(rs.getBigDecimal("R54_FV_LONG")); obj.setR54_FV_SHORT(rs.getBigDecimal("R54_FV_SHORT")); obj.setR54_QFHA(rs.getBigDecimal("R54_QFHA"));
		obj.setR55_PRODUCT(rs.getString("R55_PRODUCT")); obj.setR55_NV_LONG(rs.getBigDecimal("R55_NV_LONG")); obj.setR55_NV_SHORT(rs.getBigDecimal("R55_NV_SHORT"));
		obj.setR55_FV_LONG(rs.getBigDecimal("R55_FV_LONG")); obj.setR55_FV_SHORT(rs.getBigDecimal("R55_FV_SHORT")); obj.setR55_QFHA(rs.getBigDecimal("R55_QFHA"));
			return obj;
		}
	}
}
