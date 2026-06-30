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

import com.bornfire.brrs.entities.M_TBS_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_TBS_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_TBS_Detail_Entity;
import com.bornfire.brrs.entities.M_TBS_Resub_Detail_Entity;
import com.bornfire.brrs.entities.M_TBS_Resub_Summary_Entity;
import com.bornfire.brrs.entities.M_TBS_Summary_Entity;
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
