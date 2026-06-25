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

import com.bornfire.brrs.entities.M_CA3_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_CA3_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_CA3_Detail_Entity;
import com.bornfire.brrs.entities.M_CA3_Resub_Detail_Entity;
import com.bornfire.brrs.entities.M_CA3_Resub_Summary_Entity;
import com.bornfire.brrs.entities.M_CA3_Summary_Entity;
import com.bornfire.brrs.entities.UserProfileRep;

@Service
@Transactional

public class BRRS_M_CA3_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_CA3_ReportService.class);

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

	public List<M_CA3_Summary_Entity> getSummaryByDate(Date reportDate) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_CA3_SUMMARYTABLE WHERE REPORT_DATE = ?",
			new Object[]{reportDate}, new M_CA3SummaryRowMapper());
	}

	public List<M_CA3_Detail_Entity> getDetailByDate(Date reportDate) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_CA3_DETAILTABLE WHERE REPORT_DATE = ?",
			new Object[]{reportDate}, new M_CA3DetailRowMapper());
	}

	public List<M_CA3_Archival_Summary_Entity> getArchivalSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_CA3_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
			new Object[]{reportDate, version}, new M_CA3ArchivalSummaryRowMapper());
	}

	public List<M_CA3_Archival_Summary_Entity> getArchivalSummaryWithVersionAll() {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_CA3_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC",
			new M_CA3ArchivalSummaryRowMapper());
	}

	public List<M_CA3_Archival_Detail_Entity> getArchivalDetailByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_CA3_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
			new Object[]{reportDate, version}, new M_CA3ArchivalDetailRowMapper());
	}

	public List<M_CA3_Resub_Summary_Entity> getResubSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_CA3_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
			new Object[]{reportDate, version}, new M_CA3ResubSummaryRowMapper());
	}

	public List<M_CA3_Resub_Detail_Entity> getResubDetailByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_M_CA3_RESUB_DETAILTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
			new Object[]{reportDate, version}, new M_CA3ResubDetailRowMapper());
	}

	public BigDecimal findMaxResubVersion(Date reportDate) {
		return jdbcTemplate.queryForObject(
			"SELECT MAX(REPORT_VERSION) FROM BRRS_M_CA3_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ?",
			new Object[]{reportDate}, BigDecimal.class);
	}

	// =========================================================
	// JDBC WRITE METHODS
	// =========================================================

	private static final String R_FIELDS_SET =
		"R10_PRODUCT=?, R10_AMOUNT=?, R11_PRODUCT=?, R11_AMOUNT=?, R12_PRODUCT=?, R12_AMOUNT=?," +
		"R13_PRODUCT=?, R13_AMOUNT=?, R14_PRODUCT=?, R14_AMOUNT=?, R15_PRODUCT=?, R15_AMOUNT=?," +
		"R16_PRODUCT=?, R16_AMOUNT=?, R17_PRODUCT=?, R17_AMOUNT=?, R18_PRODUCT=?, R18_AMOUNT=?," +
		"R19_PRODUCT=?, R19_AMOUNT=?, R20_PRODUCT=?, R20_AMOUNT=?," +
		"R24_PRODUCT=?, R24_AMOUNT=?, R25_PRODUCT=?, R25_AMOUNT=?, R26_PRODUCT=?, R26_AMOUNT=?," +
		"R27_PRODUCT=?, R27_AMOUNT=?, R28_PRODUCT=?, R28_AMOUNT=?, R29_PRODUCT=?, R29_AMOUNT=?," +
		"R36_PRODUCT=?, R36_AMOUNT=?, R37_PRODUCT=?, R37_AMOUNT=?, R38_PRODUCT=?, R38_AMOUNT=?," +
		"R39_PRODUCT=?, R39_AMOUNT=?, R40_PRODUCT=?, R40_AMOUNT=?, R41_PRODUCT=?, R41_AMOUNT=?," +
		"R44_PRODUCT=?, R44_AMOUNT=?, R45_PRODUCT=?, R45_AMOUNT=?, R46_PRODUCT=?, R46_AMOUNT=?," +
		"R50_PRODUCT=?, R50_AMOUNT=?, R51_PRODUCT=?, R51_AMOUNT=?, R52_PRODUCT=?, R52_AMOUNT=?," +
		"R53_PRODUCT=?, R53_AMOUNT=?, R54_PRODUCT=?, R54_AMOUNT=?, R55_PRODUCT=?, R55_AMOUNT=?," +
		"R58_PRODUCT=?, R58_AMOUNT=?, R59_PRODUCT=?, R59_AMOUNT=?, R60_PRODUCT=?, R60_AMOUNT=?";

	private static final String R_COLS =
		"R10_PRODUCT,R10_AMOUNT,R11_PRODUCT,R11_AMOUNT,R12_PRODUCT,R12_AMOUNT," +
		"R13_PRODUCT,R13_AMOUNT,R14_PRODUCT,R14_AMOUNT,R15_PRODUCT,R15_AMOUNT," +
		"R16_PRODUCT,R16_AMOUNT,R17_PRODUCT,R17_AMOUNT,R18_PRODUCT,R18_AMOUNT," +
		"R19_PRODUCT,R19_AMOUNT,R20_PRODUCT,R20_AMOUNT," +
		"R24_PRODUCT,R24_AMOUNT,R25_PRODUCT,R25_AMOUNT,R26_PRODUCT,R26_AMOUNT," +
		"R27_PRODUCT,R27_AMOUNT,R28_PRODUCT,R28_AMOUNT,R29_PRODUCT,R29_AMOUNT," +
		"R36_PRODUCT,R36_AMOUNT,R37_PRODUCT,R37_AMOUNT,R38_PRODUCT,R38_AMOUNT," +
		"R39_PRODUCT,R39_AMOUNT,R40_PRODUCT,R40_AMOUNT,R41_PRODUCT,R41_AMOUNT," +
		"R44_PRODUCT,R44_AMOUNT,R45_PRODUCT,R45_AMOUNT,R46_PRODUCT,R46_AMOUNT," +
		"R50_PRODUCT,R50_AMOUNT,R51_PRODUCT,R51_AMOUNT,R52_PRODUCT,R52_AMOUNT," +
		"R53_PRODUCT,R53_AMOUNT,R54_PRODUCT,R54_AMOUNT,R55_PRODUCT,R55_AMOUNT," +
		"R58_PRODUCT,R58_AMOUNT,R59_PRODUCT,R59_AMOUNT,R60_PRODUCT,R60_AMOUNT";

	private static final String R_PLACEHOLDERS =
		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
		"?,?,?,?,?,?,?,?,?,?,?,?," +
		"?,?,?,?,?,?,?,?,?,?,?,?," +
		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";

	private Object[] rFieldValues(M_CA3_Summary_Entity e) {
		return new Object[]{
			e.getR10_PRODUCT(), e.getR10_AMOUNT(), e.getR11_PRODUCT(), e.getR11_AMOUNT(),
			e.getR12_PRODUCT(), e.getR12_AMOUNT(), e.getR13_PRODUCT(), e.getR13_AMOUNT(),
			e.getR14_PRODUCT(), e.getR14_AMOUNT(), e.getR15_PRODUCT(), e.getR15_AMOUNT(),
			e.getR16_PRODUCT(), e.getR16_AMOUNT(), e.getR17_PRODUCT(), e.getR17_AMOUNT(),
			e.getR18_PRODUCT(), e.getR18_AMOUNT(), e.getR19_PRODUCT(), e.getR19_AMOUNT(),
			e.getR20_PRODUCT(), e.getR20_AMOUNT(),
			e.getR24_PRODUCT(), e.getR24_AMOUNT(), e.getR25_PRODUCT(), e.getR25_AMOUNT(),
			e.getR26_PRODUCT(), e.getR26_AMOUNT(), e.getR27_PRODUCT(), e.getR27_AMOUNT(),
			e.getR28_PRODUCT(), e.getR28_AMOUNT(), e.getR29_PRODUCT(), e.getR29_AMOUNT(),
			e.getR36_PRODUCT(), e.getR36_AMOUNT(), e.getR37_PRODUCT(), e.getR37_AMOUNT(),
			e.getR38_PRODUCT(), e.getR38_AMOUNT(), e.getR39_PRODUCT(), e.getR39_AMOUNT(),
			e.getR40_PRODUCT(), e.getR40_AMOUNT(), e.getR41_PRODUCT(), e.getR41_AMOUNT(),
			e.getR44_PRODUCT(), e.getR44_AMOUNT(), e.getR45_PRODUCT(), e.getR45_AMOUNT(),
			e.getR46_PRODUCT(), e.getR46_AMOUNT(),
			e.getR50_PRODUCT(), e.getR50_AMOUNT(), e.getR51_PRODUCT(), e.getR51_AMOUNT(),
			e.getR52_PRODUCT(), e.getR52_AMOUNT(), e.getR53_PRODUCT(), e.getR53_AMOUNT(),
			e.getR54_PRODUCT(), e.getR54_AMOUNT(), e.getR55_PRODUCT(), e.getR55_AMOUNT(),
			e.getR58_PRODUCT(), e.getR58_AMOUNT(), e.getR59_PRODUCT(), e.getR59_AMOUNT(),
			e.getR60_PRODUCT(), e.getR60_AMOUNT()
		};
	}

	private Object[] rFieldValues(M_CA3_Detail_Entity e) {
		return new Object[]{
			e.getR10_PRODUCT(), e.getR10_AMOUNT(), e.getR11_PRODUCT(), e.getR11_AMOUNT(),
			e.getR12_PRODUCT(), e.getR12_AMOUNT(), e.getR13_PRODUCT(), e.getR13_AMOUNT(),
			e.getR14_PRODUCT(), e.getR14_AMOUNT(), e.getR15_PRODUCT(), e.getR15_AMOUNT(),
			e.getR16_PRODUCT(), e.getR16_AMOUNT(), e.getR17_PRODUCT(), e.getR17_AMOUNT(),
			e.getR18_PRODUCT(), e.getR18_AMOUNT(), e.getR19_PRODUCT(), e.getR19_AMOUNT(),
			e.getR20_PRODUCT(), e.getR20_AMOUNT(),
			e.getR24_PRODUCT(), e.getR24_AMOUNT(), e.getR25_PRODUCT(), e.getR25_AMOUNT(),
			e.getR26_PRODUCT(), e.getR26_AMOUNT(), e.getR27_PRODUCT(), e.getR27_AMOUNT(),
			e.getR28_PRODUCT(), e.getR28_AMOUNT(), e.getR29_PRODUCT(), e.getR29_AMOUNT(),
			e.getR36_PRODUCT(), e.getR36_AMOUNT(), e.getR37_PRODUCT(), e.getR37_AMOUNT(),
			e.getR38_PRODUCT(), e.getR38_AMOUNT(), e.getR39_PRODUCT(), e.getR39_AMOUNT(),
			e.getR40_PRODUCT(), e.getR40_AMOUNT(), e.getR41_PRODUCT(), e.getR41_AMOUNT(),
			e.getR44_PRODUCT(), e.getR44_AMOUNT(), e.getR45_PRODUCT(), e.getR45_AMOUNT(),
			e.getR46_PRODUCT(), e.getR46_AMOUNT(),
			e.getR50_PRODUCT(), e.getR50_AMOUNT(), e.getR51_PRODUCT(), e.getR51_AMOUNT(),
			e.getR52_PRODUCT(), e.getR52_AMOUNT(), e.getR53_PRODUCT(), e.getR53_AMOUNT(),
			e.getR54_PRODUCT(), e.getR54_AMOUNT(), e.getR55_PRODUCT(), e.getR55_AMOUNT(),
			e.getR58_PRODUCT(), e.getR58_AMOUNT(), e.getR59_PRODUCT(), e.getR59_AMOUNT(),
			e.getR60_PRODUCT(), e.getR60_AMOUNT()
		};
	}

	private Object[] rFieldValues(M_CA3_Archival_Summary_Entity e) {
		return new Object[]{
			e.getR10_PRODUCT(), e.getR10_AMOUNT(), e.getR11_PRODUCT(), e.getR11_AMOUNT(),
			e.getR12_PRODUCT(), e.getR12_AMOUNT(), e.getR13_PRODUCT(), e.getR13_AMOUNT(),
			e.getR14_PRODUCT(), e.getR14_AMOUNT(), e.getR15_PRODUCT(), e.getR15_AMOUNT(),
			e.getR16_PRODUCT(), e.getR16_AMOUNT(), e.getR17_PRODUCT(), e.getR17_AMOUNT(),
			e.getR18_PRODUCT(), e.getR18_AMOUNT(), e.getR19_PRODUCT(), e.getR19_AMOUNT(),
			e.getR20_PRODUCT(), e.getR20_AMOUNT(),
			e.getR24_PRODUCT(), e.getR24_AMOUNT(), e.getR25_PRODUCT(), e.getR25_AMOUNT(),
			e.getR26_PRODUCT(), e.getR26_AMOUNT(), e.getR27_PRODUCT(), e.getR27_AMOUNT(),
			e.getR28_PRODUCT(), e.getR28_AMOUNT(), e.getR29_PRODUCT(), e.getR29_AMOUNT(),
			e.getR36_PRODUCT(), e.getR36_AMOUNT(), e.getR37_PRODUCT(), e.getR37_AMOUNT(),
			e.getR38_PRODUCT(), e.getR38_AMOUNT(), e.getR39_PRODUCT(), e.getR39_AMOUNT(),
			e.getR40_PRODUCT(), e.getR40_AMOUNT(), e.getR41_PRODUCT(), e.getR41_AMOUNT(),
			e.getR44_PRODUCT(), e.getR44_AMOUNT(), e.getR45_PRODUCT(), e.getR45_AMOUNT(),
			e.getR46_PRODUCT(), e.getR46_AMOUNT(),
			e.getR50_PRODUCT(), e.getR50_AMOUNT(), e.getR51_PRODUCT(), e.getR51_AMOUNT(),
			e.getR52_PRODUCT(), e.getR52_AMOUNT(), e.getR53_PRODUCT(), e.getR53_AMOUNT(),
			e.getR54_PRODUCT(), e.getR54_AMOUNT(), e.getR55_PRODUCT(), e.getR55_AMOUNT(),
			e.getR58_PRODUCT(), e.getR58_AMOUNT(), e.getR59_PRODUCT(), e.getR59_AMOUNT(),
			e.getR60_PRODUCT(), e.getR60_AMOUNT()
		};
	}

	private Object[] rFieldValues(M_CA3_Archival_Detail_Entity e) {
		return new Object[]{
			e.getR10_PRODUCT(), e.getR10_AMOUNT(), e.getR11_PRODUCT(), e.getR11_AMOUNT(),
			e.getR12_PRODUCT(), e.getR12_AMOUNT(), e.getR13_PRODUCT(), e.getR13_AMOUNT(),
			e.getR14_PRODUCT(), e.getR14_AMOUNT(), e.getR15_PRODUCT(), e.getR15_AMOUNT(),
			e.getR16_PRODUCT(), e.getR16_AMOUNT(), e.getR17_PRODUCT(), e.getR17_AMOUNT(),
			e.getR18_PRODUCT(), e.getR18_AMOUNT(), e.getR19_PRODUCT(), e.getR19_AMOUNT(),
			e.getR20_PRODUCT(), e.getR20_AMOUNT(),
			e.getR24_PRODUCT(), e.getR24_AMOUNT(), e.getR25_PRODUCT(), e.getR25_AMOUNT(),
			e.getR26_PRODUCT(), e.getR26_AMOUNT(), e.getR27_PRODUCT(), e.getR27_AMOUNT(),
			e.getR28_PRODUCT(), e.getR28_AMOUNT(), e.getR29_PRODUCT(), e.getR29_AMOUNT(),
			e.getR36_PRODUCT(), e.getR36_AMOUNT(), e.getR37_PRODUCT(), e.getR37_AMOUNT(),
			e.getR38_PRODUCT(), e.getR38_AMOUNT(), e.getR39_PRODUCT(), e.getR39_AMOUNT(),
			e.getR40_PRODUCT(), e.getR40_AMOUNT(), e.getR41_PRODUCT(), e.getR41_AMOUNT(),
			e.getR44_PRODUCT(), e.getR44_AMOUNT(), e.getR45_PRODUCT(), e.getR45_AMOUNT(),
			e.getR46_PRODUCT(), e.getR46_AMOUNT(),
			e.getR50_PRODUCT(), e.getR50_AMOUNT(), e.getR51_PRODUCT(), e.getR51_AMOUNT(),
			e.getR52_PRODUCT(), e.getR52_AMOUNT(), e.getR53_PRODUCT(), e.getR53_AMOUNT(),
			e.getR54_PRODUCT(), e.getR54_AMOUNT(), e.getR55_PRODUCT(), e.getR55_AMOUNT(),
			e.getR58_PRODUCT(), e.getR58_AMOUNT(), e.getR59_PRODUCT(), e.getR59_AMOUNT(),
			e.getR60_PRODUCT(), e.getR60_AMOUNT()
		};
	}

	private Object[] rFieldValues(M_CA3_Resub_Summary_Entity e) {
		return new Object[]{
			e.getR10_PRODUCT(), e.getR10_AMOUNT(), e.getR11_PRODUCT(), e.getR11_AMOUNT(),
			e.getR12_PRODUCT(), e.getR12_AMOUNT(), e.getR13_PRODUCT(), e.getR13_AMOUNT(),
			e.getR14_PRODUCT(), e.getR14_AMOUNT(), e.getR15_PRODUCT(), e.getR15_AMOUNT(),
			e.getR16_PRODUCT(), e.getR16_AMOUNT(), e.getR17_PRODUCT(), e.getR17_AMOUNT(),
			e.getR18_PRODUCT(), e.getR18_AMOUNT(), e.getR19_PRODUCT(), e.getR19_AMOUNT(),
			e.getR20_PRODUCT(), e.getR20_AMOUNT(),
			e.getR24_PRODUCT(), e.getR24_AMOUNT(), e.getR25_PRODUCT(), e.getR25_AMOUNT(),
			e.getR26_PRODUCT(), e.getR26_AMOUNT(), e.getR27_PRODUCT(), e.getR27_AMOUNT(),
			e.getR28_PRODUCT(), e.getR28_AMOUNT(), e.getR29_PRODUCT(), e.getR29_AMOUNT(),
			e.getR36_PRODUCT(), e.getR36_AMOUNT(), e.getR37_PRODUCT(), e.getR37_AMOUNT(),
			e.getR38_PRODUCT(), e.getR38_AMOUNT(), e.getR39_PRODUCT(), e.getR39_AMOUNT(),
			e.getR40_PRODUCT(), e.getR40_AMOUNT(), e.getR41_PRODUCT(), e.getR41_AMOUNT(),
			e.getR44_PRODUCT(), e.getR44_AMOUNT(), e.getR45_PRODUCT(), e.getR45_AMOUNT(),
			e.getR46_PRODUCT(), e.getR46_AMOUNT(),
			e.getR50_PRODUCT(), e.getR50_AMOUNT(), e.getR51_PRODUCT(), e.getR51_AMOUNT(),
			e.getR52_PRODUCT(), e.getR52_AMOUNT(), e.getR53_PRODUCT(), e.getR53_AMOUNT(),
			e.getR54_PRODUCT(), e.getR54_AMOUNT(), e.getR55_PRODUCT(), e.getR55_AMOUNT(),
			e.getR58_PRODUCT(), e.getR58_AMOUNT(), e.getR59_PRODUCT(), e.getR59_AMOUNT(),
			e.getR60_PRODUCT(), e.getR60_AMOUNT()
		};
	}

	private Object[] rFieldValues(M_CA3_Resub_Detail_Entity e) {
		return new Object[]{
			e.getR10_PRODUCT(), e.getR10_AMOUNT(), e.getR11_PRODUCT(), e.getR11_AMOUNT(),
			e.getR12_PRODUCT(), e.getR12_AMOUNT(), e.getR13_PRODUCT(), e.getR13_AMOUNT(),
			e.getR14_PRODUCT(), e.getR14_AMOUNT(), e.getR15_PRODUCT(), e.getR15_AMOUNT(),
			e.getR16_PRODUCT(), e.getR16_AMOUNT(), e.getR17_PRODUCT(), e.getR17_AMOUNT(),
			e.getR18_PRODUCT(), e.getR18_AMOUNT(), e.getR19_PRODUCT(), e.getR19_AMOUNT(),
			e.getR20_PRODUCT(), e.getR20_AMOUNT(),
			e.getR24_PRODUCT(), e.getR24_AMOUNT(), e.getR25_PRODUCT(), e.getR25_AMOUNT(),
			e.getR26_PRODUCT(), e.getR26_AMOUNT(), e.getR27_PRODUCT(), e.getR27_AMOUNT(),
			e.getR28_PRODUCT(), e.getR28_AMOUNT(), e.getR29_PRODUCT(), e.getR29_AMOUNT(),
			e.getR36_PRODUCT(), e.getR36_AMOUNT(), e.getR37_PRODUCT(), e.getR37_AMOUNT(),
			e.getR38_PRODUCT(), e.getR38_AMOUNT(), e.getR39_PRODUCT(), e.getR39_AMOUNT(),
			e.getR40_PRODUCT(), e.getR40_AMOUNT(), e.getR41_PRODUCT(), e.getR41_AMOUNT(),
			e.getR44_PRODUCT(), e.getR44_AMOUNT(), e.getR45_PRODUCT(), e.getR45_AMOUNT(),
			e.getR46_PRODUCT(), e.getR46_AMOUNT(),
			e.getR50_PRODUCT(), e.getR50_AMOUNT(), e.getR51_PRODUCT(), e.getR51_AMOUNT(),
			e.getR52_PRODUCT(), e.getR52_AMOUNT(), e.getR53_PRODUCT(), e.getR53_AMOUNT(),
			e.getR54_PRODUCT(), e.getR54_AMOUNT(), e.getR55_PRODUCT(), e.getR55_AMOUNT(),
			e.getR58_PRODUCT(), e.getR58_AMOUNT(), e.getR59_PRODUCT(), e.getR59_AMOUNT(),
			e.getR60_PRODUCT(), e.getR60_AMOUNT()
		};
	}

	private void saveSummary(M_CA3_Summary_Entity e) {
		Object[] rVals = rFieldValues(e);
		Object[] params = new Object[rVals.length + 8];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length]     = e.getREPORT_VERSION();
		params[rVals.length + 1] = e.getREPORT_FREQUENCY();
		params[rVals.length + 2] = e.getREPORT_CODE();
		params[rVals.length + 3] = e.getREPORT_DESC();
		params[rVals.length + 4] = e.getENTITY_FLG();
		params[rVals.length + 5] = e.getMODIFY_FLG();
		params[rVals.length + 6] = e.getDEL_FLG();
		params[rVals.length + 7] = e.getREPORT_DATE();
		jdbcTemplate.update(
			"UPDATE BRRS_M_CA3_SUMMARYTABLE SET " + R_FIELDS_SET +
			",REPORT_VERSION=?,REPORT_FREQUENCY=?,REPORT_CODE=?,REPORT_DESC=?,ENTITY_FLG=?,MODIFY_FLG=?,DEL_FLG=?" +
			" WHERE REPORT_DATE=?", params);
	}

	private void saveDetail(M_CA3_Detail_Entity e) {
		int cnt = jdbcTemplate.queryForObject(
			"SELECT COUNT(*) FROM BRRS_M_CA3_DETAILTABLE WHERE REPORT_DATE=?",
			new Object[]{e.getREPORT_DATE()}, Integer.class);
		Object[] rVals = rFieldValues(e);
		if (cnt > 0) {
			Object[] params = new Object[rVals.length + 8];
			System.arraycopy(rVals, 0, params, 0, rVals.length);
			params[rVals.length]     = e.getREPORT_VERSION();
			params[rVals.length + 1] = e.getREPORT_FREQUENCY();
			params[rVals.length + 2] = e.getREPORT_CODE();
			params[rVals.length + 3] = e.getREPORT_DESC();
			params[rVals.length + 4] = e.getENTITY_FLG();
			params[rVals.length + 5] = e.getMODIFY_FLG();
			params[rVals.length + 6] = e.getDEL_FLG();
			params[rVals.length + 7] = e.getREPORT_DATE();
			jdbcTemplate.update(
				"UPDATE BRRS_M_CA3_DETAILTABLE SET " + R_FIELDS_SET +
				",REPORT_VERSION=?,REPORT_FREQUENCY=?,REPORT_CODE=?,REPORT_DESC=?,ENTITY_FLG=?,MODIFY_FLG=?,DEL_FLG=?" +
				" WHERE REPORT_DATE=?", params);
		} else {
			Object[] params = new Object[rVals.length + 8];
			System.arraycopy(rVals, 0, params, 0, rVals.length);
			params[rVals.length]     = e.getREPORT_DATE();
			params[rVals.length + 1] = e.getREPORT_VERSION();
			params[rVals.length + 2] = e.getREPORT_FREQUENCY();
			params[rVals.length + 3] = e.getREPORT_CODE();
			params[rVals.length + 4] = e.getREPORT_DESC();
			params[rVals.length + 5] = e.getENTITY_FLG();
			params[rVals.length + 6] = e.getMODIFY_FLG();
			params[rVals.length + 7] = e.getDEL_FLG();
			jdbcTemplate.update(
				"INSERT INTO BRRS_M_CA3_DETAILTABLE (REPORT_DATE," + R_COLS +
				",REPORT_VERSION,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC,ENTITY_FLG,MODIFY_FLG,DEL_FLG) VALUES (?," +
				R_PLACEHOLDERS + ",?,?,?,?,?,?,?)", params);
		}
	}

	private void insertResubSummary(M_CA3_Resub_Summary_Entity e) {
		Object[] rVals = rFieldValues(e);
		Object[] params = new Object[rVals.length + 6];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length]     = e.getReportDate();
		params[rVals.length + 1] = e.getReportVersion();
		params[rVals.length + 2] = e.getReportResubDate();
		params[rVals.length + 3] = e.getReport_frequency();
		params[rVals.length + 4] = e.getReport_code();
		params[rVals.length + 5] = e.getReport_desc();
		jdbcTemplate.update(
			"INSERT INTO BRRS_M_CA3_RESUB_SUMMARYTABLE (" + R_COLS +
			",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC) VALUES (" +
			R_PLACEHOLDERS + ",?,?,?,?,?,?)", params);
	}

	private void insertResubDetail(M_CA3_Resub_Detail_Entity e) {
		Object[] rVals = rFieldValues(e);
		Object[] params = new Object[rVals.length + 6];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length]     = e.getReportDate();
		params[rVals.length + 1] = e.getReportVersion();
		params[rVals.length + 2] = e.getReportResubDate();
		params[rVals.length + 3] = e.getReport_frequency();
		params[rVals.length + 4] = e.getReport_code();
		params[rVals.length + 5] = e.getReport_desc();
		jdbcTemplate.update(
			"INSERT INTO BRRS_M_CA3_RESUB_DETAILTABLE (" + R_COLS +
			",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC) VALUES (" +
			R_PLACEHOLDERS + ",?,?,?,?,?,?)", params);
	}

	private void insertArchivalSummary(M_CA3_Archival_Summary_Entity e) {
		Object[] rVals = rFieldValues(e);
		Object[] params = new Object[rVals.length + 6];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length]     = e.getReportDate();
		params[rVals.length + 1] = e.getReportVersion();
		params[rVals.length + 2] = e.getReportResubDate();
		params[rVals.length + 3] = e.getReport_frequency();
		params[rVals.length + 4] = e.getReport_code();
		params[rVals.length + 5] = e.getReport_desc();
		jdbcTemplate.update(
			"INSERT INTO BRRS_M_CA3_ARCHIVALTABLE_SUMMARY (" + R_COLS +
			",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC) VALUES (" +
			R_PLACEHOLDERS + ",?,?,?,?,?,?)", params);
	}

	private void insertArchivalDetail(M_CA3_Archival_Detail_Entity e) {
		Object[] rVals = rFieldValues(e);
		Object[] params = new Object[rVals.length + 6];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length]     = e.getReportDate();
		params[rVals.length + 1] = e.getReportVersion();
		params[rVals.length + 2] = e.getReportResubDate();
		params[rVals.length + 3] = e.getReport_frequency();
		params[rVals.length + 4] = e.getReport_code();
		params[rVals.length + 5] = e.getReport_desc();
		jdbcTemplate.update(
			"INSERT INTO BRRS_M_CA3_ARCHIVALTABLE_DETAIL (" + R_COLS +
			",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC) VALUES (" +
			R_PLACEHOLDERS + ",?,?,?,?,?,?)", params);
	}

	public ModelAndView getBRRS_M_CA3View(String reportId, String fromdate, String todate, String currency,
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
				List<M_CA3_Archival_Summary_Entity> T1Master = getArchivalSummaryByDateAndVersion(d1, version);
				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_CA3_Resub_Summary_Entity> T1Master = getResubSummaryByDateAndVersion(d1, version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<M_CA3_Summary_Entity> T1Master = getSummaryByDate(dateformat.parse(todate));
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_CA3_Archival_Detail_Entity> T1Master = getArchivalDetailByDateAndVersion(d1, version);
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_CA3_Resub_Detail_Entity> T1Master = getResubDetailByDateAndVersion(d1, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_CA3_Detail_Entity> T1Master = getDetailByDate(dateformat.parse(todate));
					System.out.println("Details......T1Master Size " + T1Master.size());
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_CA3");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	public void updateReport(M_CA3_Summary_Entity updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    // Fetch existing SUMMARY
	    List<M_CA3_Summary_Entity> summaryList1 = getSummaryByDate(updatedEntity.getREPORT_DATE());
	    if (summaryList1.isEmpty()) throw new RuntimeException("Summary record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE());
	    M_CA3_Summary_Entity existingSummary = summaryList1.get(0);

	    // Audit old copy
	    M_CA3_Summary_Entity oldcopy = new M_CA3_Summary_Entity();
	    BeanUtils.copyProperties(existingSummary, oldcopy);

	    // Fetch existing DETAIL
	    List<M_CA3_Detail_Entity> detailList1 = getDetailByDate(updatedEntity.getREPORT_DATE());
	    M_CA3_Detail_Entity existingDetail;
	    if (detailList1.isEmpty()) {
	        existingDetail = new M_CA3_Detail_Entity();
	        existingDetail.setREPORT_DATE(updatedEntity.getREPORT_DATE());
	    } else {
	        existingDetail = detailList1.get(0);
	    }

	    try {

	        String[] fields = { "AMOUNT" };

	        // Loop R10 -> R60
	        for (int i = 10; i <= 60; i++) {

	            String prefix = "R" + i + "_";

	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {

	                    Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);
	                    Object newValue = getter.invoke(updatedEntity);

	                    if (newValue != null) {

	                        Method summarySetter = M_CA3_Summary_Entity.class
	                                .getMethod(setterName, BigDecimal.class);

	                        Method detailSetter = M_CA3_Detail_Entity.class
	                                .getMethod(setterName, BigDecimal.class);

	                        // Update Summary
	                        summarySetter.invoke(existingSummary, newValue);

	                        // Update Detail
	                        detailSetter.invoke(existingDetail, newValue);
	                    }

	                } catch (NoSuchMethodException e) {
	                    continue; // Skip missing rows
	                }
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // Check changes
	    String changes = auditService.getChanges(oldcopy, existingSummary);

	    if (!changes.isEmpty()) {

	        saveSummary(existingSummary);
	        saveDetail(existingDetail);

	        auditService.compareEntitiesmanual(
	                oldcopy,
	                existingSummary,
	                updatedEntity.getREPORT_DATE().toString(),
	                "M CA3 Summary Screen",
	                "BRRS_M_CA3_SUMMARY");
	    }
	}

	public void updateReport2(M_CA3_Summary_Entity updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    List<M_CA3_Summary_Entity> summaryList2 = getSummaryByDate(updatedEntity.getREPORT_DATE());
	    if (summaryList2.isEmpty()) throw new RuntimeException("Summary record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE());
	    M_CA3_Summary_Entity existingSummary = summaryList2.get(0);

	    M_CA3_Summary_Entity oldcopy = new M_CA3_Summary_Entity();
	    BeanUtils.copyProperties(existingSummary, oldcopy);

	    List<M_CA3_Detail_Entity> detailList2 = getDetailByDate(updatedEntity.getREPORT_DATE());
	    M_CA3_Detail_Entity existingDetail;
	    if (detailList2.isEmpty()) {
	        existingDetail = new M_CA3_Detail_Entity();
	        existingDetail.setREPORT_DATE(updatedEntity.getREPORT_DATE());
	    } else {
	        existingDetail = detailList2.get(0);
	    }

	    try {

	        for (int i = 24; i <= 29; i++) {

	            String prefix = "R" + i + "_";

	            Method getter = M_CA3_Summary_Entity.class.getMethod(
	                    "get" + prefix + "AMOUNT");

	            Method summarySetter = M_CA3_Summary_Entity.class.getMethod(
	                    "set" + prefix + "AMOUNT", getter.getReturnType());

	            Method detailSetter = M_CA3_Detail_Entity.class.getMethod(
	                    "set" + prefix + "AMOUNT", getter.getReturnType());

	            Object value = getter.invoke(updatedEntity);

	            summarySetter.invoke(existingSummary, value);
	            detailSetter.invoke(existingDetail, value);
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    String changes = auditService.getChanges(oldcopy, existingSummary);

	    if (!changes.isEmpty()) {

	        saveSummary(existingSummary);
	        saveDetail(existingDetail);

	        auditService.compareEntitiesmanual(
	                oldcopy,
	                existingSummary,
	                updatedEntity.getREPORT_DATE().toString(),
	                "M CA3 Summary Screen",
	                "BRRS_M_CA3_SUMMARY");
	    }
	}
	
	public void updateReport3(M_CA3_Summary_Entity updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    List<M_CA3_Summary_Entity> summaryList3 = getSummaryByDate(updatedEntity.getREPORT_DATE());
	    if (summaryList3.isEmpty()) throw new RuntimeException("Summary record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE());
	    M_CA3_Summary_Entity existingSummary = summaryList3.get(0);

	    M_CA3_Summary_Entity oldcopy = new M_CA3_Summary_Entity();
	    BeanUtils.copyProperties(existingSummary, oldcopy);

	    List<M_CA3_Detail_Entity> detailList3 = getDetailByDate(updatedEntity.getREPORT_DATE());
	    M_CA3_Detail_Entity existingDetail;
	    if (detailList3.isEmpty()) {
	        existingDetail = new M_CA3_Detail_Entity();
	        existingDetail.setREPORT_DATE(updatedEntity.getREPORT_DATE());
	    } else {
	        existingDetail = detailList3.get(0);
	    }

	    try {

	        for (int i = 36; i <= 41; i++) {

	            String prefix = "R" + i + "_";

	            Method getter = M_CA3_Summary_Entity.class.getMethod(
	                    "get" + prefix + "AMOUNT");

	            Method summarySetter = M_CA3_Summary_Entity.class.getMethod(
	                    "set" + prefix + "AMOUNT", getter.getReturnType());

	            Method detailSetter = M_CA3_Detail_Entity.class.getMethod(
	                    "set" + prefix + "AMOUNT", getter.getReturnType());

	            Object value = getter.invoke(updatedEntity);

	            summarySetter.invoke(existingSummary, value);
	            detailSetter.invoke(existingDetail, value);
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    String changes = auditService.getChanges(oldcopy, existingSummary);

	    if (!changes.isEmpty()) {

	        saveSummary(existingSummary);
	        saveDetail(existingDetail);

	        auditService.compareEntitiesmanual(
	                oldcopy,
	                existingSummary,
	                updatedEntity.getREPORT_DATE().toString(),
	                "M CA3 Summary Screen",
	                "BRRS_M_CA3_SUMMARY");
	    }
	}
	
	public void updateReport4(M_CA3_Summary_Entity updatedEntity) {

	    List<M_CA3_Summary_Entity> summaryList4 = getSummaryByDate(updatedEntity.getREPORT_DATE());
	    if (summaryList4.isEmpty()) throw new RuntimeException("Record not found");
	    M_CA3_Summary_Entity existingSummary = summaryList4.get(0);

	    M_CA3_Summary_Entity oldcopy = new M_CA3_Summary_Entity();
	    BeanUtils.copyProperties(existingSummary, oldcopy);

	    List<M_CA3_Detail_Entity> detailList4 = getDetailByDate(updatedEntity.getREPORT_DATE());
	    M_CA3_Detail_Entity existingDetail;
	    if (detailList4.isEmpty()) {
	        existingDetail = new M_CA3_Detail_Entity();
	        existingDetail.setREPORT_DATE(updatedEntity.getREPORT_DATE());
	    } else {
	        existingDetail = detailList4.get(0);
	    }

	    try {

	        int[] rows = {44,45,46};

	        for (int i : rows) {

	            String prefix = "R" + i + "_";

	            Method getter = M_CA3_Summary_Entity.class.getMethod(
	                    "get" + prefix + "AMOUNT");

	            Method summarySetter = M_CA3_Summary_Entity.class.getMethod(
	                    "set" + prefix + "AMOUNT", getter.getReturnType());

	            Method detailSetter = M_CA3_Detail_Entity.class.getMethod(
	                    "set" + prefix + "AMOUNT", getter.getReturnType());

	            Object value = getter.invoke(updatedEntity);

	            summarySetter.invoke(existingSummary, value);
	            detailSetter.invoke(existingDetail, value);
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    String changes = auditService.getChanges(oldcopy, existingSummary);

	    if (!changes.isEmpty()) {

	        saveSummary(existingSummary);
	        saveDetail(existingDetail);

	        auditService.compareEntitiesmanual(
	                oldcopy,
	                existingSummary,
	                updatedEntity.getREPORT_DATE().toString(),
	                "M CA3 Summary Screen",
	                "BRRS_M_CA3_SUMMARY");
	    }
	}
	
	public void updateReport5(M_CA3_Summary_Entity updatedEntity) {

	    List<M_CA3_Summary_Entity> summaryList5 = getSummaryByDate(updatedEntity.getREPORT_DATE());
	    if (summaryList5.isEmpty()) throw new RuntimeException("Record not found");
	    M_CA3_Summary_Entity existingSummary = summaryList5.get(0);

	    M_CA3_Summary_Entity oldcopy = new M_CA3_Summary_Entity();
	    BeanUtils.copyProperties(existingSummary, oldcopy);

	    List<M_CA3_Detail_Entity> detailList5 = getDetailByDate(updatedEntity.getREPORT_DATE());
	    M_CA3_Detail_Entity existingDetail;
	    if (detailList5.isEmpty()) {
	        existingDetail = new M_CA3_Detail_Entity();
	        existingDetail.setREPORT_DATE(updatedEntity.getREPORT_DATE());
	    } else {
	        existingDetail = detailList5.get(0);
	    }

	    try {

	        for (int i = 50; i <= 55; i++) {

	            String prefix = "R" + i + "_";

	            Method getter = M_CA3_Summary_Entity.class.getMethod(
	                    "get" + prefix + "AMOUNT");

	            Method summarySetter = M_CA3_Summary_Entity.class.getMethod(
	                    "set" + prefix + "AMOUNT", getter.getReturnType());

	            Method detailSetter = M_CA3_Detail_Entity.class.getMethod(
	                    "set" + prefix + "AMOUNT", getter.getReturnType());

	            Object value = getter.invoke(updatedEntity);

	            summarySetter.invoke(existingSummary, value);
	            detailSetter.invoke(existingDetail, value);
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    String changes = auditService.getChanges(oldcopy, existingSummary);

	    if (!changes.isEmpty()) {

	        saveSummary(existingSummary);
	        saveDetail(existingDetail);

	        auditService.compareEntitiesmanual(
	                oldcopy,
	                existingSummary,
	                updatedEntity.getREPORT_DATE().toString(),
	                "M CA3 Summary Screen",
	                "BRRS_M_CA3_SUMMARY");
	    }
	}
	
	public void updateReport6(M_CA3_Summary_Entity updatedEntity) {

	    List<M_CA3_Summary_Entity> summaryList6 = getSummaryByDate(updatedEntity.getREPORT_DATE());
	    if (summaryList6.isEmpty()) throw new RuntimeException("Record not found");
	    M_CA3_Summary_Entity existingSummary = summaryList6.get(0);

	    M_CA3_Summary_Entity oldcopy = new M_CA3_Summary_Entity();
	    BeanUtils.copyProperties(existingSummary, oldcopy);

	    List<M_CA3_Detail_Entity> detailList6 = getDetailByDate(updatedEntity.getREPORT_DATE());
	    M_CA3_Detail_Entity existingDetail;
	    if (detailList6.isEmpty()) {
	        existingDetail = new M_CA3_Detail_Entity();
	        existingDetail.setREPORT_DATE(updatedEntity.getREPORT_DATE());
	    } else {
	        existingDetail = detailList6.get(0);
	    }

	    try {

	        for (int i = 58; i <= 60; i++) {

	            String prefix = "R" + i + "_";

	            Method getter = M_CA3_Summary_Entity.class.getMethod(
	                    "get" + prefix + "AMOUNT");

	            Method summarySetter = M_CA3_Summary_Entity.class.getMethod(
	                    "set" + prefix + "AMOUNT", getter.getReturnType());

	            Method detailSetter = M_CA3_Detail_Entity.class.getMethod(
	                    "set" + prefix + "AMOUNT", getter.getReturnType());

	            Object value = getter.invoke(updatedEntity);

	            summarySetter.invoke(existingSummary, value);
	            detailSetter.invoke(existingDetail, value);
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    String changes = auditService.getChanges(oldcopy, existingSummary);

	    if (!changes.isEmpty()) {

	        saveSummary(existingSummary);
	        saveDetail(existingDetail);

	        auditService.compareEntitiesmanual(
	                oldcopy,
	                existingSummary,
	                updatedEntity.getREPORT_DATE().toString(),
	                "M CA3 Summary Screen",
	                "BRRS_M_CA3_SUMMARY");
	    }
	}

	public void updateResubReport(M_CA3_Resub_Summary_Entity updatedEntity) {

		   System.out.println("Came toM_C Resub Service");
		Date reportDate = updatedEntity.getReportDate();

		BigDecimal maxResubVer = findMaxResubVersion(reportDate);
		if (maxResubVer == null) {
			throw new RuntimeException("No record for report date: " + reportDate);
		}

		BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);
		Date now = new Date();

		M_CA3_Resub_Summary_Entity resubSummary = new M_CA3_Resub_Summary_Entity();
		BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "reportResubDate");
		resubSummary.setReportDate(reportDate);
		resubSummary.setReportVersion(newVersion);
		resubSummary.setReportResubDate(now);

		M_CA3_Resub_Detail_Entity resubDetail = new M_CA3_Resub_Detail_Entity();
		BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "reportResubDate");
		resubDetail.setReportDate(reportDate);
		resubDetail.setReportVersion(newVersion);
		resubDetail.setReportResubDate(now);

		M_CA3_Archival_Summary_Entity archSummary = new M_CA3_Archival_Summary_Entity();
		BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "reportResubDate");
		archSummary.setReportDate(reportDate);
		archSummary.setReportVersion(newVersion);
		archSummary.setReportResubDate(now);

		M_CA3_Archival_Detail_Entity archDetail = new M_CA3_Archival_Detail_Entity();
		BeanUtils.copyProperties(updatedEntity, archDetail, "reportDate", "reportVersion", "reportResubDate");
		archDetail.setReportDate(reportDate);
		archDetail.setReportVersion(newVersion);
		archDetail.setReportResubDate(now);

		insertResubSummary(resubSummary);
		insertResubDetail(resubDetail);
		insertArchivalSummary(archSummary);
		insertArchivalDetail(archDetail);
	}

	public List<Object[]> getM_CA3Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_CA3_Archival_Summary_Entity> latestArchivalList = getArchivalSummaryWithVersionAll();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_CA3_Archival_Summary_Entity entity : latestArchivalList) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					resubList.add(row);
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}
		} catch (Exception e) {
			System.err.println("Error fetching M_CA3 Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	public List<Object[]> getM_CA3Archival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_CA3_Archival_Summary_Entity> repoData = getArchivalSummaryWithVersionAll();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_CA3_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_CA3_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_CA3 Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Normal format Excel

	public byte[] getBRRS_M_CA3Excel(String filename, String reportId, String fromdate, String todate, String currency,
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
				return getExcelM_CA3ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_CA3ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_CA3EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				List<M_CA3_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_CA3 report. Returning empty result.");
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
							M_CA3_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

							//REPORT_DATE
							row = sheet.getRow(5);
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

							
							// row10
							// Column c
							row = sheet.getRow(9);

							Cell cell2 = row.createCell(2);
							if (record.getR10_AMOUNT() != null) {
								cell2.setCellValue(record.getR10_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row11
							row = sheet.getRow(10);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR11_AMOUNT() != null) {
								cell2.setCellValue(record.getR11_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row12
							row = sheet.getRow(11);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR12_AMOUNT() != null) {
								cell2.setCellValue(record.getR12_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row13
							row = sheet.getRow(12);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR13_AMOUNT() != null) {
								cell2.setCellValue(record.getR13_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row14
							row = sheet.getRow(13);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR14_AMOUNT() != null) {
								cell2.setCellValue(record.getR14_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row15
							row = sheet.getRow(14);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR15_AMOUNT() != null) {
								cell2.setCellValue(record.getR15_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row16
							row = sheet.getRow(15);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR16_AMOUNT() != null) {
								cell2.setCellValue(record.getR16_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row17
							row = sheet.getRow(16);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR17_AMOUNT() != null) {
								cell2.setCellValue(record.getR17_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row18
							row = sheet.getRow(17);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR18_AMOUNT() != null) {
								cell2.setCellValue(record.getR18_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row19
							row = sheet.getRow(18);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR19_AMOUNT() != null) {
								cell2.setCellValue(record.getR19_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row20
							row = sheet.getRow(19);
							// Column b

							// Column c
							cell2 = row.getCell(2);
							if (record.getR20_AMOUNT() != null) {
								cell2.setCellValue(record.getR20_AMOUNT().doubleValue());

							} else {
								cell2.setCellValue("");
							}

							// row24
							row = sheet.getRow(23);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR24_AMOUNT() != null) {
								cell2.setCellValue(record.getR24_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row25
							row = sheet.getRow(24);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR25_AMOUNT() != null) {
								cell2.setCellValue(record.getR25_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row26
							row = sheet.getRow(25);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR26_AMOUNT() != null) {
								cell2.setCellValue(record.getR26_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row27
							row = sheet.getRow(26);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR27_AMOUNT() != null) {
								cell2.setCellValue(record.getR27_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row28
							row = sheet.getRow(27);
							// Column b

							// Column c
							cell2 = row.getCell(2);
							if (record.getR28_AMOUNT() != null) {
								cell2.setCellValue(record.getR28_AMOUNT().doubleValue());

							} else {
								cell2.setCellValue("");
							}

							// row29
							row = sheet.getRow(28);
							// Column b

							// Column c
							cell2 = row.getCell(2);
							if (record.getR29_AMOUNT() != null) {
								cell2.setCellValue(record.getR29_AMOUNT().doubleValue());

							} else {
								cell2.setCellValue("");

							}

							// row36
							row = sheet.getRow(35);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR36_AMOUNT() != null) {
								cell2.setCellValue(record.getR36_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}
							// row37
							row = sheet.getRow(36);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR37_AMOUNT() != null) {
								cell2.setCellValue(record.getR37_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row38
							row = sheet.getRow(37);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR38_AMOUNT() != null) {
								cell2.setCellValue(record.getR38_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row39
							row = sheet.getRow(38);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR39_AMOUNT() != null) {
								cell2.setCellValue(record.getR39_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row40
							row = sheet.getRow(39);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR40_AMOUNT() != null) {
								cell2.setCellValue(record.getR40_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row41
							row = sheet.getRow(40);
							// Column b

							// Column c
							cell2 = row.getCell(2);
							if (record.getR41_AMOUNT() != null) {
								cell2.setCellValue(record.getR41_AMOUNT().doubleValue());

							} else {
								cell2.setCellValue("");

							}

							// row44
							row = sheet.getRow(43);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR44_AMOUNT() != null) {
								cell2.setCellValue(record.getR44_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row45
							row = sheet.getRow(44);
							// Column b

							// Column c
							cell2 = row.getCell(2);
							if (record.getR45_AMOUNT() != null) {
								cell2.setCellValue(record.getR45_AMOUNT().doubleValue());

							} else {
								cell2.setCellValue("");

							}

							// row46
							row = sheet.getRow(45);
							// Column b

							// Column c
							cell2 = row.getCell(2);
							if (record.getR46_AMOUNT() != null) {
								cell2.setCellValue(record.getR46_AMOUNT().doubleValue());

							} else {
								cell2.setCellValue("");

							}

							// row50
							row = sheet.getRow(49);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR50_AMOUNT() != null) {
								cell2.setCellValue(record.getR50_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row51
							row = sheet.getRow(50);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR51_AMOUNT() != null) {
								cell2.setCellValue(record.getR51_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row52
							row = sheet.getRow(51);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR52_AMOUNT() != null) {
								cell2.setCellValue(record.getR52_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row53
							row = sheet.getRow(52);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR53_AMOUNT() != null) {
								cell2.setCellValue(record.getR53_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row54
							row = sheet.getRow(53);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR54_AMOUNT() != null) {
								cell2.setCellValue(record.getR54_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row55
							row = sheet.getRow(54);
							// Column b

							// Column c
							cell2 = row.getCell(2);
							if (record.getR55_AMOUNT() != null) {
								cell2.setCellValue(record.getR55_AMOUNT().doubleValue());

							} else {
								cell2.setCellValue("");

							}

							// row58
							row = sheet.getRow(57);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR58_AMOUNT() != null) {
								cell2.setCellValue(record.getR58_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row59
							row = sheet.getRow(58);
							// Column b

							// Column c
							cell2 = row.getCell(2);
							if (record.getR59_AMOUNT() != null) {
								cell2.setCellValue(record.getR59_AMOUNT().doubleValue());

							} else {
								cell2.setCellValue("");

							}

							// row60
							row = sheet.getRow(14);
							// Column b

							// Column c
							cell2 = row.getCell(2);
							if (record.getR15_AMOUNT() != null) {
								cell2.setCellValue(record.getR15_AMOUNT().doubleValue());

							} else {
								cell2.setCellValue("");

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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA3 SUMMARY", null, "BRRS_M_CA3_SUMMARYTABLE");
					}
					return out.toByteArray();
				}
			}
		}
	}

	// Normal Email Excel
	public byte[] BRRS_M_CA3EmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_CA3ArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_CA3ResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			List<M_CA3_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_CA3 report. Returning empty result.");
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
						M_CA3_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						//REPORT_DATE
						row = sheet.getRow(5);
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

						
						// row10
						// Column c
						row = sheet.getRow(9);
						Cell cell2 = row.createCell(2);
						if (record.getR10_AMOUNT() != null) {
							cell2.setCellValue(record.getR10_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row11
						row = sheet.getRow(10);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR11_AMOUNT() != null) {
							cell2.setCellValue(record.getR11_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row12
						row = sheet.getRow(11);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR12_AMOUNT() != null) {
							cell2.setCellValue(record.getR12_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row13
						row = sheet.getRow(12);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR13_AMOUNT() != null) {
							cell2.setCellValue(record.getR13_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row14
						row = sheet.getRow(13);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR14_AMOUNT() != null) {
							cell2.setCellValue(record.getR14_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row15
						row = sheet.getRow(14);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR15_AMOUNT() != null) {
							cell2.setCellValue(record.getR15_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row16
						row = sheet.getRow(15);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR16_AMOUNT() != null) {
							cell2.setCellValue(record.getR16_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row17
						row = sheet.getRow(16);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR17_AMOUNT() != null) {
							cell2.setCellValue(record.getR17_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row18
						row = sheet.getRow(17);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR18_AMOUNT() != null) {
							cell2.setCellValue(record.getR18_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row19
						row = sheet.getRow(18);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR19_AMOUNT() != null) {
							cell2.setCellValue(record.getR19_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row20
						row = sheet.getRow(19);
						// Column b

						// Column c
						cell2 = row.getCell(2);
						if (record.getR20_AMOUNT() != null) {
							cell2.setCellValue(record.getR20_AMOUNT().doubleValue());

						} else {
							cell2.setCellValue("");
						}

						// row24
						row = sheet.getRow(23);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR24_AMOUNT() != null) {
							cell2.setCellValue(record.getR24_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row25
						row = sheet.getRow(24);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR25_AMOUNT() != null) {
							cell2.setCellValue(record.getR25_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row26
						row = sheet.getRow(25);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR26_AMOUNT() != null) {
							cell2.setCellValue(record.getR26_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row27
						row = sheet.getRow(26);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR27_AMOUNT() != null) {
							cell2.setCellValue(record.getR27_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row28
						row = sheet.getRow(27);
						// Column b

						// Column c
						cell2 = row.getCell(2);
						if (record.getR28_AMOUNT() != null) {
							cell2.setCellValue(record.getR28_AMOUNT().doubleValue());

						} else {
							cell2.setCellValue("");
						}

						// row29
						row = sheet.getRow(28);
						// Column b

						// Column c
						cell2 = row.getCell(2);
						if (record.getR29_AMOUNT() != null) {
							cell2.setCellValue(record.getR29_AMOUNT().doubleValue());

						} else {
							cell2.setCellValue("");

						}
						
						
						// row36
						row = sheet.getRow(33);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR36_AMOUNT() != null) {
							cell2.setCellValue(record.getR36_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}
						// row37
						row = sheet.getRow(34);

						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR37_AMOUNT() != null) {
							cell2.setCellValue(record.getR37_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row38
						row = sheet.getRow(35);

						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR38_AMOUNT() != null) {
							cell2.setCellValue(record.getR38_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row39
						row = sheet.getRow(36);

						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR39_AMOUNT() != null) {
							cell2.setCellValue(record.getR39_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row40
						row = sheet.getRow(40);

						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR40_AMOUNT() != null) {
							cell2.setCellValue(record.getR40_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						/*
						 * // row41 row = sheet.getRow(38);
						 * 
						 * // Column b
						 * 
						 * // Column c cell2 = row.getCell(2); if (record.getR41_AMOUNT() != null) {
						 * cell2.setCellValue(record.getR41_AMOUNT().doubleValue());
						 * 
						 * } else { cell2.setCellValue("");
						 * 
						 * }
						 */

						// row44
						row = sheet.getRow(41);

						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR44_AMOUNT() != null) {
							cell2.setCellValue(record.getR44_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						/*
						 * // row45 row = sheet.getRow(44); // Column b
						 * 
						 * // Column c cell2 = row.getCell(2); if (record.getR45_AMOUNT() != null) {
						 * cell2.setCellValue(record.getR45_AMOUNT().doubleValue());
						 * 
						 * } else { cell2.setCellValue("");
						 * 
						 * }
						 * 
						 * // row46 row = sheet.getRow(45); // Column b
						 * 
						 * // Column c cell2 = row.getCell(2); if (record.getR46_AMOUNT() != null) {
						 * cell2.setCellValue(record.getR46_AMOUNT().doubleValue());
						 * 
						 * } else { cell2.setCellValue("");
						 * 
						 * }
						 */

						// row50
						row = sheet.getRow(46);

						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR50_AMOUNT() != null) {
							cell2.setCellValue(record.getR50_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row51
						row = sheet.getRow(47);

						// Column c
						cell2 = row.createCell(2);
						if (record.getR51_AMOUNT() != null) {
							cell2.setCellValue(record.getR51_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row52
						row = sheet.getRow(48);

						// Column c
						cell2 = row.createCell(2);
						if (record.getR52_AMOUNT() != null) {
							cell2.setCellValue(record.getR52_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row53
						row = sheet.getRow(49);

						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR53_AMOUNT() != null) {
							cell2.setCellValue(record.getR53_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row54
						row = sheet.getRow(53);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR54_AMOUNT() != null) {
							cell2.setCellValue(record.getR54_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row55
						row = sheet.getRow(50);

						// Column c
						cell2 = row.getCell(2);
						if (record.getR55_AMOUNT() != null) {
							cell2.setCellValue(record.getR55_AMOUNT().doubleValue());

						} else {
							cell2.setCellValue("");

						}

						// row58
						row = sheet.getRow(54);

						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR58_AMOUNT() != null) {
							cell2.setCellValue(record.getR58_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						/*
						 * //row59 row = sheet.getRow(58); // Column b
						 * 
						 * 
						 * // Column c cell2 = row.getCell(2); if (record.getR59_AMOUNT() != null) {
						 * cell2.setCellValue(record.getR59_AMOUNT().doubleValue());
						 * 
						 * } else { cell2.setCellValue("");
						 * 
						 * }
						 */

						// row60
						row = sheet.getRow(54);

						// Column c
						cell2 = row.getCell(2);
						if (record.getR59_AMOUNT() != null) {
							cell2.setCellValue(record.getR59_AMOUNT().doubleValue());

						} else {
							cell2.setCellValue("");

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
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA3 EMAIL SUMMARY", null, "BRRS_M_CA3_SUMMARYTABLE");
				}
				return out.toByteArray();
			}
		}
	}

	// Archival format excel
	public byte[] getExcelM_CA3ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_CA3ArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_CA3_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA3 report. Returning empty result.");
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
					M_CA3_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					//REPORT_DATE
					row = sheet.getRow(5);
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

					
					// row10
					// Column c
					row = sheet.getRow(9);
					Cell cell2 = row.createCell(2);
					if (record.getR10_AMOUNT() != null) {
						cell2.setCellValue(record.getR10_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR11_AMOUNT() != null) {
						cell2.setCellValue(record.getR11_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR12_AMOUNT() != null) {
						cell2.setCellValue(record.getR12_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR13_AMOUNT() != null) {
						cell2.setCellValue(record.getR13_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR14_AMOUNT() != null) {
						cell2.setCellValue(record.getR14_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR16_AMOUNT() != null) {
						cell2.setCellValue(record.getR16_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR17_AMOUNT() != null) {
						cell2.setCellValue(record.getR17_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR18_AMOUNT() != null) {
						cell2.setCellValue(record.getR18_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR19_AMOUNT() != null) {
						cell2.setCellValue(record.getR19_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR20_AMOUNT() != null) {
						cell2.setCellValue(record.getR20_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");
					}

					// row24
					row = sheet.getRow(23);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR24_AMOUNT() != null) {
						cell2.setCellValue(record.getR24_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR25_AMOUNT() != null) {
						cell2.setCellValue(record.getR25_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR26_AMOUNT() != null) {
						cell2.setCellValue(record.getR26_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR27_AMOUNT() != null) {
						cell2.setCellValue(record.getR27_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR28_AMOUNT() != null) {
						cell2.setCellValue(record.getR28_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");
					}

					// row29
					row = sheet.getRow(28);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR29_AMOUNT() != null) {
						cell2.setCellValue(record.getR29_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row36
					row = sheet.getRow(35);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR36_AMOUNT() != null) {
						cell2.setCellValue(record.getR36_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// row37
					row = sheet.getRow(36);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR37_AMOUNT() != null) {
						cell2.setCellValue(record.getR37_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(37);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR38_AMOUNT() != null) {
						cell2.setCellValue(record.getR38_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(38);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR39_AMOUNT() != null) {
						cell2.setCellValue(record.getR39_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row40
					row = sheet.getRow(39);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR40_AMOUNT() != null) {
						cell2.setCellValue(record.getR40_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row41
					row = sheet.getRow(40);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR41_AMOUNT() != null) {
						cell2.setCellValue(record.getR41_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row44
					row = sheet.getRow(43);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR44_AMOUNT() != null) {
						cell2.setCellValue(record.getR44_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row45
					row = sheet.getRow(44);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR45_AMOUNT() != null) {
						cell2.setCellValue(record.getR45_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row46
					row = sheet.getRow(45);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR46_AMOUNT() != null) {
						cell2.setCellValue(record.getR46_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row50
					row = sheet.getRow(49);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR50_AMOUNT() != null) {
						cell2.setCellValue(record.getR50_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(50);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR51_AMOUNT() != null) {
						cell2.setCellValue(record.getR51_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row52
					row = sheet.getRow(51);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR52_AMOUNT() != null) {
						cell2.setCellValue(record.getR52_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row53
					row = sheet.getRow(52);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR53_AMOUNT() != null) {
						cell2.setCellValue(record.getR53_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR54_AMOUNT() != null) {
						cell2.setCellValue(record.getR54_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(54);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR55_AMOUNT() != null) {
						cell2.setCellValue(record.getR55_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row58
					row = sheet.getRow(57);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR58_AMOUNT() != null) {
						cell2.setCellValue(record.getR58_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row59
					row = sheet.getRow(58);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR59_AMOUNT() != null) {
						cell2.setCellValue(record.getR59_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row60
					row = sheet.getRow(14);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA3 ARCHIVAL SUMMARY", null, "BRRS_M_CA3_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}

	}

	// Archival Email Excel
	public byte[] BRRS_M_CA3ArchivalEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_CA3_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_CA3 report. Returning empty result.");
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
					M_CA3_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					//REPORT_DATE
					row = sheet.getRow(5);
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

					
					// row10
					// Column c
					row = sheet.getRow(9);
					Cell cell2 = row.createCell(2);
					if (record.getR10_AMOUNT() != null) {
						cell2.setCellValue(record.getR10_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR11_AMOUNT() != null) {
						cell2.setCellValue(record.getR11_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR12_AMOUNT() != null) {
						cell2.setCellValue(record.getR12_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR13_AMOUNT() != null) {
						cell2.setCellValue(record.getR13_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR14_AMOUNT() != null) {
						cell2.setCellValue(record.getR14_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR16_AMOUNT() != null) {
						cell2.setCellValue(record.getR16_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR17_AMOUNT() != null) {
						cell2.setCellValue(record.getR17_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR18_AMOUNT() != null) {
						cell2.setCellValue(record.getR18_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR19_AMOUNT() != null) {
						cell2.setCellValue(record.getR19_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR20_AMOUNT() != null) {
						cell2.setCellValue(record.getR20_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");
					}

					// row24
					row = sheet.getRow(23);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR24_AMOUNT() != null) {
						cell2.setCellValue(record.getR24_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR25_AMOUNT() != null) {
						cell2.setCellValue(record.getR25_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR26_AMOUNT() != null) {
						cell2.setCellValue(record.getR26_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR27_AMOUNT() != null) {
						cell2.setCellValue(record.getR27_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR28_AMOUNT() != null) {
						cell2.setCellValue(record.getR28_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");
					}

					// row29
					row = sheet.getRow(28);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR29_AMOUNT() != null) {
						cell2.setCellValue(record.getR29_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}
					
					
					// row36
					row = sheet.getRow(33);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR36_AMOUNT() != null) {
						cell2.setCellValue(record.getR36_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// row37
					row = sheet.getRow(34);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR37_AMOUNT() != null) {
						cell2.setCellValue(record.getR37_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(35);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR38_AMOUNT() != null) {
						cell2.setCellValue(record.getR38_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(36);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR39_AMOUNT() != null) {
						cell2.setCellValue(record.getR39_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row40
					row = sheet.getRow(40);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR40_AMOUNT() != null) {
						cell2.setCellValue(record.getR40_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					/*
					 * // row41 row = sheet.getRow(38);
					 * 
					 * // Column b
					 * 
					 * // Column c cell2 = row.getCell(2); if (record.getR41_AMOUNT() != null) {
					 * cell2.setCellValue(record.getR41_AMOUNT().doubleValue());
					 * 
					 * } else { cell2.setCellValue("");
					 * 
					 * }
					 */

					// row44
					row = sheet.getRow(41);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR44_AMOUNT() != null) {
						cell2.setCellValue(record.getR44_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					/*
					 * // row45 row = sheet.getRow(44); // Column b
					 * 
					 * // Column c cell2 = row.getCell(2); if (record.getR45_AMOUNT() != null) {
					 * cell2.setCellValue(record.getR45_AMOUNT().doubleValue());
					 * 
					 * } else { cell2.setCellValue("");
					 * 
					 * }
					 * 
					 * // row46 row = sheet.getRow(45); // Column b
					 * 
					 * // Column c cell2 = row.getCell(2); if (record.getR46_AMOUNT() != null) {
					 * cell2.setCellValue(record.getR46_AMOUNT().doubleValue());
					 * 
					 * } else { cell2.setCellValue("");
					 * 
					 * }
					 */

					// row50
					row = sheet.getRow(46);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR50_AMOUNT() != null) {
						cell2.setCellValue(record.getR50_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(47);

					// Column c
					cell2 = row.createCell(2);
					if (record.getR51_AMOUNT() != null) {
						cell2.setCellValue(record.getR51_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row52
					row = sheet.getRow(48);

					// Column c
					cell2 = row.createCell(2);
					if (record.getR52_AMOUNT() != null) {
						cell2.setCellValue(record.getR52_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row53
					row = sheet.getRow(49);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR53_AMOUNT() != null) {
						cell2.setCellValue(record.getR53_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR54_AMOUNT() != null) {
						cell2.setCellValue(record.getR54_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(50);

					// Column c
					cell2 = row.getCell(2);
					if (record.getR55_AMOUNT() != null) {
						cell2.setCellValue(record.getR55_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row58
					row = sheet.getRow(54);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR58_AMOUNT() != null) {
						cell2.setCellValue(record.getR58_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					/*
					 * //row59 row = sheet.getRow(58); // Column b
					 * 
					 * 
					 * // Column c cell2 = row.getCell(2); if (record.getR59_AMOUNT() != null) {
					 * cell2.setCellValue(record.getR59_AMOUNT().doubleValue());
					 * 
					 * } else { cell2.setCellValue("");
					 * 
					 * }
					 */

					// row60
					row = sheet.getRow(54);

					// Column c
					cell2 = row.getCell(2);
					if (record.getR59_AMOUNT() != null) {
						cell2.setCellValue(record.getR59_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA3 EMAIL ARCHIVAL SUMMARY", null, "BRRS_M_CA3_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	// Resub Format excel
	public byte[] BRRS_M_CA3ResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_CA3ResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_CA3_Resub_Summary_Entity> dataList = getResubSummaryByDateAndVersion(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA3 report. Returning empty result.");
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

					M_CA3_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					//REPORT_DATE
					row = sheet.getRow(5);
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

					
					// row10
					// Column c
					row = sheet.getRow(9);
					Cell cell2 = row.createCell(2);
					if (record.getR10_AMOUNT() != null) {
						cell2.setCellValue(record.getR10_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR11_AMOUNT() != null) {
						cell2.setCellValue(record.getR11_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR12_AMOUNT() != null) {
						cell2.setCellValue(record.getR12_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR13_AMOUNT() != null) {
						cell2.setCellValue(record.getR13_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR14_AMOUNT() != null) {
						cell2.setCellValue(record.getR14_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR16_AMOUNT() != null) {
						cell2.setCellValue(record.getR16_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR17_AMOUNT() != null) {
						cell2.setCellValue(record.getR17_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR18_AMOUNT() != null) {
						cell2.setCellValue(record.getR18_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR19_AMOUNT() != null) {
						cell2.setCellValue(record.getR19_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR20_AMOUNT() != null) {
						cell2.setCellValue(record.getR20_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");
					}

					// row24
					row = sheet.getRow(23);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR24_AMOUNT() != null) {
						cell2.setCellValue(record.getR24_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR25_AMOUNT() != null) {
						cell2.setCellValue(record.getR25_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR26_AMOUNT() != null) {
						cell2.setCellValue(record.getR26_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR27_AMOUNT() != null) {
						cell2.setCellValue(record.getR27_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR28_AMOUNT() != null) {
						cell2.setCellValue(record.getR28_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");
					}

					// row29
					row = sheet.getRow(28);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR29_AMOUNT() != null) {
						cell2.setCellValue(record.getR29_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row36
					row = sheet.getRow(35);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR36_AMOUNT() != null) {
						cell2.setCellValue(record.getR36_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// row37
					row = sheet.getRow(36);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR37_AMOUNT() != null) {
						cell2.setCellValue(record.getR37_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(37);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR38_AMOUNT() != null) {
						cell2.setCellValue(record.getR38_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(38);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR39_AMOUNT() != null) {
						cell2.setCellValue(record.getR39_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row40
					row = sheet.getRow(39);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR40_AMOUNT() != null) {
						cell2.setCellValue(record.getR40_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row41
					row = sheet.getRow(40);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR41_AMOUNT() != null) {
						cell2.setCellValue(record.getR41_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row44
					row = sheet.getRow(43);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR44_AMOUNT() != null) {
						cell2.setCellValue(record.getR44_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row45
					row = sheet.getRow(44);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR45_AMOUNT() != null) {
						cell2.setCellValue(record.getR45_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row46
					row = sheet.getRow(45);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR46_AMOUNT() != null) {
						cell2.setCellValue(record.getR46_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row50
					row = sheet.getRow(49);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR50_AMOUNT() != null) {
						cell2.setCellValue(record.getR50_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(50);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR51_AMOUNT() != null) {
						cell2.setCellValue(record.getR51_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row52
					row = sheet.getRow(51);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR52_AMOUNT() != null) {
						cell2.setCellValue(record.getR52_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row53
					row = sheet.getRow(52);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR53_AMOUNT() != null) {
						cell2.setCellValue(record.getR53_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR54_AMOUNT() != null) {
						cell2.setCellValue(record.getR54_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(54);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR55_AMOUNT() != null) {
						cell2.setCellValue(record.getR55_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row58
					row = sheet.getRow(57);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR58_AMOUNT() != null) {
						cell2.setCellValue(record.getR58_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row59
					row = sheet.getRow(58);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR59_AMOUNT() != null) {
						cell2.setCellValue(record.getR59_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row60
					row = sheet.getRow(14);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA3 RESUB SUMMARY", null, "BRRS_M_CA3_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}

	}

	// Resub Email Excel
	public byte[] BRRS_M_CA3ResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_CA3_Resub_Summary_Entity> dataList = getResubSummaryByDateAndVersion(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_CA3 report. Returning empty result.");
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
					M_CA3_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					//REPORT_DATE
					row = sheet.getRow(5);
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

					
					// row10
					// Column c
					row = sheet.getRow(9);
					Cell cell2 = row.createCell(2);
					if (record.getR10_AMOUNT() != null) {
						cell2.setCellValue(record.getR10_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR11_AMOUNT() != null) {
						cell2.setCellValue(record.getR11_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR12_AMOUNT() != null) {
						cell2.setCellValue(record.getR12_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR13_AMOUNT() != null) {
						cell2.setCellValue(record.getR13_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR14_AMOUNT() != null) {
						cell2.setCellValue(record.getR14_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR16_AMOUNT() != null) {
						cell2.setCellValue(record.getR16_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR17_AMOUNT() != null) {
						cell2.setCellValue(record.getR17_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR18_AMOUNT() != null) {
						cell2.setCellValue(record.getR18_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR19_AMOUNT() != null) {
						cell2.setCellValue(record.getR19_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR20_AMOUNT() != null) {
						cell2.setCellValue(record.getR20_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");
					}

					// row24
					row = sheet.getRow(23);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR24_AMOUNT() != null) {
						cell2.setCellValue(record.getR24_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR25_AMOUNT() != null) {
						cell2.setCellValue(record.getR25_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR26_AMOUNT() != null) {
						cell2.setCellValue(record.getR26_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR27_AMOUNT() != null) {
						cell2.setCellValue(record.getR27_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR28_AMOUNT() != null) {
						cell2.setCellValue(record.getR28_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");
					}

					// row29
					row = sheet.getRow(28);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR29_AMOUNT() != null) {
						cell2.setCellValue(record.getR29_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}
					
					
					// row36
					row = sheet.getRow(33);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR36_AMOUNT() != null) {
						cell2.setCellValue(record.getR36_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// row37
					row = sheet.getRow(34);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR37_AMOUNT() != null) {
						cell2.setCellValue(record.getR37_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(35);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR38_AMOUNT() != null) {
						cell2.setCellValue(record.getR38_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(36);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR39_AMOUNT() != null) {
						cell2.setCellValue(record.getR39_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row40
					row = sheet.getRow(40);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR40_AMOUNT() != null) {
						cell2.setCellValue(record.getR40_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					/*
					 * // row41 row = sheet.getRow(38);
					 * 
					 * // Column b
					 * 
					 * // Column c cell2 = row.getCell(2); if (record.getR41_AMOUNT() != null) {
					 * cell2.setCellValue(record.getR41_AMOUNT().doubleValue());
					 * 
					 * } else { cell2.setCellValue("");
					 * 
					 * }
					 */

					// row44
					row = sheet.getRow(41);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR44_AMOUNT() != null) {
						cell2.setCellValue(record.getR44_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					/*
					 * // row45 row = sheet.getRow(44); // Column b
					 * 
					 * // Column c cell2 = row.getCell(2); if (record.getR45_AMOUNT() != null) {
					 * cell2.setCellValue(record.getR45_AMOUNT().doubleValue());
					 * 
					 * } else { cell2.setCellValue("");
					 * 
					 * }
					 * 
					 * // row46 row = sheet.getRow(45); // Column b
					 * 
					 * // Column c cell2 = row.getCell(2); if (record.getR46_AMOUNT() != null) {
					 * cell2.setCellValue(record.getR46_AMOUNT().doubleValue());
					 * 
					 * } else { cell2.setCellValue("");
					 * 
					 * }
					 */

					// row50
					row = sheet.getRow(46);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR50_AMOUNT() != null) {
						cell2.setCellValue(record.getR50_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(47);

					// Column c
					cell2 = row.createCell(2);
					if (record.getR51_AMOUNT() != null) {
						cell2.setCellValue(record.getR51_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row52
					row = sheet.getRow(48);

					// Column c
					cell2 = row.createCell(2);
					if (record.getR52_AMOUNT() != null) {
						cell2.setCellValue(record.getR52_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row53
					row = sheet.getRow(49);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR53_AMOUNT() != null) {
						cell2.setCellValue(record.getR53_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR54_AMOUNT() != null) {
						cell2.setCellValue(record.getR54_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(50);

					// Column c
					cell2 = row.getCell(2);
					if (record.getR55_AMOUNT() != null) {
						cell2.setCellValue(record.getR55_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row58
					row = sheet.getRow(54);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR58_AMOUNT() != null) {
						cell2.setCellValue(record.getR58_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					/*
					 * //row59 row = sheet.getRow(58); // Column b
					 * 
					 * 
					 * // Column c cell2 = row.getCell(2); if (record.getR59_AMOUNT() != null) {
					 * cell2.setCellValue(record.getR59_AMOUNT().doubleValue());
					 * 
					 * } else { cell2.setCellValue("");
					 * 
					 * }
					 */

					// row60
					row = sheet.getRow(54);

					// Column c
					cell2 = row.getCell(2);
					if (record.getR59_AMOUNT() != null) {
						cell2.setCellValue(record.getR59_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA3 EMAIL RESUB SUMMARY", null, "BRRS_M_CA3_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

	class M_CA3SummaryRowMapper implements RowMapper<M_CA3_Summary_Entity> {
		@Override
		public M_CA3_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA3_Summary_Entity obj = new M_CA3_Summary_Entity();
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));
			obj.setR10_PRODUCT(rs.getString("R10_PRODUCT")); obj.setR10_AMOUNT(rs.getBigDecimal("R10_AMOUNT"));
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT")); obj.setR11_AMOUNT(rs.getBigDecimal("R11_AMOUNT"));
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT")); obj.setR12_AMOUNT(rs.getBigDecimal("R12_AMOUNT"));
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT")); obj.setR13_AMOUNT(rs.getBigDecimal("R13_AMOUNT"));
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT")); obj.setR14_AMOUNT(rs.getBigDecimal("R14_AMOUNT"));
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT")); obj.setR15_AMOUNT(rs.getBigDecimal("R15_AMOUNT"));
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT")); obj.setR16_AMOUNT(rs.getBigDecimal("R16_AMOUNT"));
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT")); obj.setR17_AMOUNT(rs.getBigDecimal("R17_AMOUNT"));
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT")); obj.setR18_AMOUNT(rs.getBigDecimal("R18_AMOUNT"));
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT")); obj.setR19_AMOUNT(rs.getBigDecimal("R19_AMOUNT"));
			obj.setR20_PRODUCT(rs.getString("R20_PRODUCT")); obj.setR20_AMOUNT(rs.getBigDecimal("R20_AMOUNT"));
			obj.setR24_PRODUCT(rs.getString("R24_PRODUCT")); obj.setR24_AMOUNT(rs.getBigDecimal("R24_AMOUNT"));
			obj.setR25_PRODUCT(rs.getString("R25_PRODUCT")); obj.setR25_AMOUNT(rs.getBigDecimal("R25_AMOUNT"));
			obj.setR26_PRODUCT(rs.getString("R26_PRODUCT")); obj.setR26_AMOUNT(rs.getBigDecimal("R26_AMOUNT"));
			obj.setR27_PRODUCT(rs.getString("R27_PRODUCT")); obj.setR27_AMOUNT(rs.getBigDecimal("R27_AMOUNT"));
			obj.setR28_PRODUCT(rs.getString("R28_PRODUCT")); obj.setR28_AMOUNT(rs.getBigDecimal("R28_AMOUNT"));
			obj.setR29_PRODUCT(rs.getString("R29_PRODUCT")); obj.setR29_AMOUNT(rs.getBigDecimal("R29_AMOUNT"));
			obj.setR36_PRODUCT(rs.getString("R36_PRODUCT")); obj.setR36_AMOUNT(rs.getBigDecimal("R36_AMOUNT"));
			obj.setR37_PRODUCT(rs.getString("R37_PRODUCT")); obj.setR37_AMOUNT(rs.getBigDecimal("R37_AMOUNT"));
			obj.setR38_PRODUCT(rs.getString("R38_PRODUCT")); obj.setR38_AMOUNT(rs.getBigDecimal("R38_AMOUNT"));
			obj.setR39_PRODUCT(rs.getString("R39_PRODUCT")); obj.setR39_AMOUNT(rs.getBigDecimal("R39_AMOUNT"));
			obj.setR40_PRODUCT(rs.getString("R40_PRODUCT")); obj.setR40_AMOUNT(rs.getBigDecimal("R40_AMOUNT"));
			obj.setR41_PRODUCT(rs.getString("R41_PRODUCT")); obj.setR41_AMOUNT(rs.getBigDecimal("R41_AMOUNT"));
			obj.setR44_PRODUCT(rs.getString("R44_PRODUCT")); obj.setR44_AMOUNT(rs.getBigDecimal("R44_AMOUNT"));
			obj.setR45_PRODUCT(rs.getString("R45_PRODUCT")); obj.setR45_AMOUNT(rs.getBigDecimal("R45_AMOUNT"));
			obj.setR46_PRODUCT(rs.getString("R46_PRODUCT")); obj.setR46_AMOUNT(rs.getBigDecimal("R46_AMOUNT"));
			obj.setR50_PRODUCT(rs.getString("R50_PRODUCT")); obj.setR50_AMOUNT(rs.getBigDecimal("R50_AMOUNT"));
			obj.setR51_PRODUCT(rs.getString("R51_PRODUCT")); obj.setR51_AMOUNT(rs.getBigDecimal("R51_AMOUNT"));
			obj.setR52_PRODUCT(rs.getString("R52_PRODUCT")); obj.setR52_AMOUNT(rs.getBigDecimal("R52_AMOUNT"));
			obj.setR53_PRODUCT(rs.getString("R53_PRODUCT")); obj.setR53_AMOUNT(rs.getBigDecimal("R53_AMOUNT"));
			obj.setR54_PRODUCT(rs.getString("R54_PRODUCT")); obj.setR54_AMOUNT(rs.getBigDecimal("R54_AMOUNT"));
			obj.setR55_PRODUCT(rs.getString("R55_PRODUCT")); obj.setR55_AMOUNT(rs.getBigDecimal("R55_AMOUNT"));
			obj.setR58_PRODUCT(rs.getString("R58_PRODUCT")); obj.setR58_AMOUNT(rs.getBigDecimal("R58_AMOUNT"));
			obj.setR59_PRODUCT(rs.getString("R59_PRODUCT")); obj.setR59_AMOUNT(rs.getBigDecimal("R59_AMOUNT"));
			obj.setR60_PRODUCT(rs.getString("R60_PRODUCT")); obj.setR60_AMOUNT(rs.getBigDecimal("R60_AMOUNT"));
			return obj;
		}
	}

	class M_CA3DetailRowMapper implements RowMapper<M_CA3_Detail_Entity> {
		@Override
		public M_CA3_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA3_Detail_Entity obj = new M_CA3_Detail_Entity();
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));
			obj.setR10_PRODUCT(rs.getString("R10_PRODUCT")); obj.setR10_AMOUNT(rs.getBigDecimal("R10_AMOUNT"));
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT")); obj.setR11_AMOUNT(rs.getBigDecimal("R11_AMOUNT"));
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT")); obj.setR12_AMOUNT(rs.getBigDecimal("R12_AMOUNT"));
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT")); obj.setR13_AMOUNT(rs.getBigDecimal("R13_AMOUNT"));
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT")); obj.setR14_AMOUNT(rs.getBigDecimal("R14_AMOUNT"));
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT")); obj.setR15_AMOUNT(rs.getBigDecimal("R15_AMOUNT"));
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT")); obj.setR16_AMOUNT(rs.getBigDecimal("R16_AMOUNT"));
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT")); obj.setR17_AMOUNT(rs.getBigDecimal("R17_AMOUNT"));
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT")); obj.setR18_AMOUNT(rs.getBigDecimal("R18_AMOUNT"));
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT")); obj.setR19_AMOUNT(rs.getBigDecimal("R19_AMOUNT"));
			obj.setR20_PRODUCT(rs.getString("R20_PRODUCT")); obj.setR20_AMOUNT(rs.getBigDecimal("R20_AMOUNT"));
			obj.setR24_PRODUCT(rs.getString("R24_PRODUCT")); obj.setR24_AMOUNT(rs.getBigDecimal("R24_AMOUNT"));
			obj.setR25_PRODUCT(rs.getString("R25_PRODUCT")); obj.setR25_AMOUNT(rs.getBigDecimal("R25_AMOUNT"));
			obj.setR26_PRODUCT(rs.getString("R26_PRODUCT")); obj.setR26_AMOUNT(rs.getBigDecimal("R26_AMOUNT"));
			obj.setR27_PRODUCT(rs.getString("R27_PRODUCT")); obj.setR27_AMOUNT(rs.getBigDecimal("R27_AMOUNT"));
			obj.setR28_PRODUCT(rs.getString("R28_PRODUCT")); obj.setR28_AMOUNT(rs.getBigDecimal("R28_AMOUNT"));
			obj.setR29_PRODUCT(rs.getString("R29_PRODUCT")); obj.setR29_AMOUNT(rs.getBigDecimal("R29_AMOUNT"));
			obj.setR36_PRODUCT(rs.getString("R36_PRODUCT")); obj.setR36_AMOUNT(rs.getBigDecimal("R36_AMOUNT"));
			obj.setR37_PRODUCT(rs.getString("R37_PRODUCT")); obj.setR37_AMOUNT(rs.getBigDecimal("R37_AMOUNT"));
			obj.setR38_PRODUCT(rs.getString("R38_PRODUCT")); obj.setR38_AMOUNT(rs.getBigDecimal("R38_AMOUNT"));
			obj.setR39_PRODUCT(rs.getString("R39_PRODUCT")); obj.setR39_AMOUNT(rs.getBigDecimal("R39_AMOUNT"));
			obj.setR40_PRODUCT(rs.getString("R40_PRODUCT")); obj.setR40_AMOUNT(rs.getBigDecimal("R40_AMOUNT"));
			obj.setR41_PRODUCT(rs.getString("R41_PRODUCT")); obj.setR41_AMOUNT(rs.getBigDecimal("R41_AMOUNT"));
			obj.setR44_PRODUCT(rs.getString("R44_PRODUCT")); obj.setR44_AMOUNT(rs.getBigDecimal("R44_AMOUNT"));
			obj.setR45_PRODUCT(rs.getString("R45_PRODUCT")); obj.setR45_AMOUNT(rs.getBigDecimal("R45_AMOUNT"));
			obj.setR46_PRODUCT(rs.getString("R46_PRODUCT")); obj.setR46_AMOUNT(rs.getBigDecimal("R46_AMOUNT"));
			obj.setR50_PRODUCT(rs.getString("R50_PRODUCT")); obj.setR50_AMOUNT(rs.getBigDecimal("R50_AMOUNT"));
			obj.setR51_PRODUCT(rs.getString("R51_PRODUCT")); obj.setR51_AMOUNT(rs.getBigDecimal("R51_AMOUNT"));
			obj.setR52_PRODUCT(rs.getString("R52_PRODUCT")); obj.setR52_AMOUNT(rs.getBigDecimal("R52_AMOUNT"));
			obj.setR53_PRODUCT(rs.getString("R53_PRODUCT")); obj.setR53_AMOUNT(rs.getBigDecimal("R53_AMOUNT"));
			obj.setR54_PRODUCT(rs.getString("R54_PRODUCT")); obj.setR54_AMOUNT(rs.getBigDecimal("R54_AMOUNT"));
			obj.setR55_PRODUCT(rs.getString("R55_PRODUCT")); obj.setR55_AMOUNT(rs.getBigDecimal("R55_AMOUNT"));
			obj.setR58_PRODUCT(rs.getString("R58_PRODUCT")); obj.setR58_AMOUNT(rs.getBigDecimal("R58_AMOUNT"));
			obj.setR59_PRODUCT(rs.getString("R59_PRODUCT")); obj.setR59_AMOUNT(rs.getBigDecimal("R59_AMOUNT"));
			obj.setR60_PRODUCT(rs.getString("R60_PRODUCT")); obj.setR60_AMOUNT(rs.getBigDecimal("R60_AMOUNT"));
			return obj;
		}
	}

	class M_CA3ArchivalSummaryRowMapper implements RowMapper<M_CA3_Archival_Summary_Entity> {
		@Override
		public M_CA3_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA3_Archival_Summary_Entity obj = new M_CA3_Archival_Summary_Entity();
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			obj.setR10_PRODUCT(rs.getString("R10_PRODUCT")); obj.setR10_AMOUNT(rs.getBigDecimal("R10_AMOUNT"));
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT")); obj.setR11_AMOUNT(rs.getBigDecimal("R11_AMOUNT"));
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT")); obj.setR12_AMOUNT(rs.getBigDecimal("R12_AMOUNT"));
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT")); obj.setR13_AMOUNT(rs.getBigDecimal("R13_AMOUNT"));
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT")); obj.setR14_AMOUNT(rs.getBigDecimal("R14_AMOUNT"));
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT")); obj.setR15_AMOUNT(rs.getBigDecimal("R15_AMOUNT"));
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT")); obj.setR16_AMOUNT(rs.getBigDecimal("R16_AMOUNT"));
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT")); obj.setR17_AMOUNT(rs.getBigDecimal("R17_AMOUNT"));
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT")); obj.setR18_AMOUNT(rs.getBigDecimal("R18_AMOUNT"));
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT")); obj.setR19_AMOUNT(rs.getBigDecimal("R19_AMOUNT"));
			obj.setR20_PRODUCT(rs.getString("R20_PRODUCT")); obj.setR20_AMOUNT(rs.getBigDecimal("R20_AMOUNT"));
			obj.setR24_PRODUCT(rs.getString("R24_PRODUCT")); obj.setR24_AMOUNT(rs.getBigDecimal("R24_AMOUNT"));
			obj.setR25_PRODUCT(rs.getString("R25_PRODUCT")); obj.setR25_AMOUNT(rs.getBigDecimal("R25_AMOUNT"));
			obj.setR26_PRODUCT(rs.getString("R26_PRODUCT")); obj.setR26_AMOUNT(rs.getBigDecimal("R26_AMOUNT"));
			obj.setR27_PRODUCT(rs.getString("R27_PRODUCT")); obj.setR27_AMOUNT(rs.getBigDecimal("R27_AMOUNT"));
			obj.setR28_PRODUCT(rs.getString("R28_PRODUCT")); obj.setR28_AMOUNT(rs.getBigDecimal("R28_AMOUNT"));
			obj.setR29_PRODUCT(rs.getString("R29_PRODUCT")); obj.setR29_AMOUNT(rs.getBigDecimal("R29_AMOUNT"));
			obj.setR36_PRODUCT(rs.getString("R36_PRODUCT")); obj.setR36_AMOUNT(rs.getBigDecimal("R36_AMOUNT"));
			obj.setR37_PRODUCT(rs.getString("R37_PRODUCT")); obj.setR37_AMOUNT(rs.getBigDecimal("R37_AMOUNT"));
			obj.setR38_PRODUCT(rs.getString("R38_PRODUCT")); obj.setR38_AMOUNT(rs.getBigDecimal("R38_AMOUNT"));
			obj.setR39_PRODUCT(rs.getString("R39_PRODUCT")); obj.setR39_AMOUNT(rs.getBigDecimal("R39_AMOUNT"));
			obj.setR40_PRODUCT(rs.getString("R40_PRODUCT")); obj.setR40_AMOUNT(rs.getBigDecimal("R40_AMOUNT"));
			obj.setR41_PRODUCT(rs.getString("R41_PRODUCT")); obj.setR41_AMOUNT(rs.getBigDecimal("R41_AMOUNT"));
			obj.setR44_PRODUCT(rs.getString("R44_PRODUCT")); obj.setR44_AMOUNT(rs.getBigDecimal("R44_AMOUNT"));
			obj.setR45_PRODUCT(rs.getString("R45_PRODUCT")); obj.setR45_AMOUNT(rs.getBigDecimal("R45_AMOUNT"));
			obj.setR46_PRODUCT(rs.getString("R46_PRODUCT")); obj.setR46_AMOUNT(rs.getBigDecimal("R46_AMOUNT"));
			obj.setR50_PRODUCT(rs.getString("R50_PRODUCT")); obj.setR50_AMOUNT(rs.getBigDecimal("R50_AMOUNT"));
			obj.setR51_PRODUCT(rs.getString("R51_PRODUCT")); obj.setR51_AMOUNT(rs.getBigDecimal("R51_AMOUNT"));
			obj.setR52_PRODUCT(rs.getString("R52_PRODUCT")); obj.setR52_AMOUNT(rs.getBigDecimal("R52_AMOUNT"));
			obj.setR53_PRODUCT(rs.getString("R53_PRODUCT")); obj.setR53_AMOUNT(rs.getBigDecimal("R53_AMOUNT"));
			obj.setR54_PRODUCT(rs.getString("R54_PRODUCT")); obj.setR54_AMOUNT(rs.getBigDecimal("R54_AMOUNT"));
			obj.setR55_PRODUCT(rs.getString("R55_PRODUCT")); obj.setR55_AMOUNT(rs.getBigDecimal("R55_AMOUNT"));
			obj.setR58_PRODUCT(rs.getString("R58_PRODUCT")); obj.setR58_AMOUNT(rs.getBigDecimal("R58_AMOUNT"));
			obj.setR59_PRODUCT(rs.getString("R59_PRODUCT")); obj.setR59_AMOUNT(rs.getBigDecimal("R59_AMOUNT"));
			obj.setR60_PRODUCT(rs.getString("R60_PRODUCT")); obj.setR60_AMOUNT(rs.getBigDecimal("R60_AMOUNT"));
			return obj;
		}
	}

	class M_CA3ArchivalDetailRowMapper implements RowMapper<M_CA3_Archival_Detail_Entity> {
		@Override
		public M_CA3_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA3_Archival_Detail_Entity obj = new M_CA3_Archival_Detail_Entity();
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			obj.setR10_PRODUCT(rs.getString("R10_PRODUCT")); obj.setR10_AMOUNT(rs.getBigDecimal("R10_AMOUNT"));
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT")); obj.setR11_AMOUNT(rs.getBigDecimal("R11_AMOUNT"));
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT")); obj.setR12_AMOUNT(rs.getBigDecimal("R12_AMOUNT"));
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT")); obj.setR13_AMOUNT(rs.getBigDecimal("R13_AMOUNT"));
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT")); obj.setR14_AMOUNT(rs.getBigDecimal("R14_AMOUNT"));
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT")); obj.setR15_AMOUNT(rs.getBigDecimal("R15_AMOUNT"));
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT")); obj.setR16_AMOUNT(rs.getBigDecimal("R16_AMOUNT"));
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT")); obj.setR17_AMOUNT(rs.getBigDecimal("R17_AMOUNT"));
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT")); obj.setR18_AMOUNT(rs.getBigDecimal("R18_AMOUNT"));
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT")); obj.setR19_AMOUNT(rs.getBigDecimal("R19_AMOUNT"));
			obj.setR20_PRODUCT(rs.getString("R20_PRODUCT")); obj.setR20_AMOUNT(rs.getBigDecimal("R20_AMOUNT"));
			obj.setR24_PRODUCT(rs.getString("R24_PRODUCT")); obj.setR24_AMOUNT(rs.getBigDecimal("R24_AMOUNT"));
			obj.setR25_PRODUCT(rs.getString("R25_PRODUCT")); obj.setR25_AMOUNT(rs.getBigDecimal("R25_AMOUNT"));
			obj.setR26_PRODUCT(rs.getString("R26_PRODUCT")); obj.setR26_AMOUNT(rs.getBigDecimal("R26_AMOUNT"));
			obj.setR27_PRODUCT(rs.getString("R27_PRODUCT")); obj.setR27_AMOUNT(rs.getBigDecimal("R27_AMOUNT"));
			obj.setR28_PRODUCT(rs.getString("R28_PRODUCT")); obj.setR28_AMOUNT(rs.getBigDecimal("R28_AMOUNT"));
			obj.setR29_PRODUCT(rs.getString("R29_PRODUCT")); obj.setR29_AMOUNT(rs.getBigDecimal("R29_AMOUNT"));
			obj.setR36_PRODUCT(rs.getString("R36_PRODUCT")); obj.setR36_AMOUNT(rs.getBigDecimal("R36_AMOUNT"));
			obj.setR37_PRODUCT(rs.getString("R37_PRODUCT")); obj.setR37_AMOUNT(rs.getBigDecimal("R37_AMOUNT"));
			obj.setR38_PRODUCT(rs.getString("R38_PRODUCT")); obj.setR38_AMOUNT(rs.getBigDecimal("R38_AMOUNT"));
			obj.setR39_PRODUCT(rs.getString("R39_PRODUCT")); obj.setR39_AMOUNT(rs.getBigDecimal("R39_AMOUNT"));
			obj.setR40_PRODUCT(rs.getString("R40_PRODUCT")); obj.setR40_AMOUNT(rs.getBigDecimal("R40_AMOUNT"));
			obj.setR41_PRODUCT(rs.getString("R41_PRODUCT")); obj.setR41_AMOUNT(rs.getBigDecimal("R41_AMOUNT"));
			obj.setR44_PRODUCT(rs.getString("R44_PRODUCT")); obj.setR44_AMOUNT(rs.getBigDecimal("R44_AMOUNT"));
			obj.setR45_PRODUCT(rs.getString("R45_PRODUCT")); obj.setR45_AMOUNT(rs.getBigDecimal("R45_AMOUNT"));
			obj.setR46_PRODUCT(rs.getString("R46_PRODUCT")); obj.setR46_AMOUNT(rs.getBigDecimal("R46_AMOUNT"));
			obj.setR50_PRODUCT(rs.getString("R50_PRODUCT")); obj.setR50_AMOUNT(rs.getBigDecimal("R50_AMOUNT"));
			obj.setR51_PRODUCT(rs.getString("R51_PRODUCT")); obj.setR51_AMOUNT(rs.getBigDecimal("R51_AMOUNT"));
			obj.setR52_PRODUCT(rs.getString("R52_PRODUCT")); obj.setR52_AMOUNT(rs.getBigDecimal("R52_AMOUNT"));
			obj.setR53_PRODUCT(rs.getString("R53_PRODUCT")); obj.setR53_AMOUNT(rs.getBigDecimal("R53_AMOUNT"));
			obj.setR54_PRODUCT(rs.getString("R54_PRODUCT")); obj.setR54_AMOUNT(rs.getBigDecimal("R54_AMOUNT"));
			obj.setR55_PRODUCT(rs.getString("R55_PRODUCT")); obj.setR55_AMOUNT(rs.getBigDecimal("R55_AMOUNT"));
			obj.setR58_PRODUCT(rs.getString("R58_PRODUCT")); obj.setR58_AMOUNT(rs.getBigDecimal("R58_AMOUNT"));
			obj.setR59_PRODUCT(rs.getString("R59_PRODUCT")); obj.setR59_AMOUNT(rs.getBigDecimal("R59_AMOUNT"));
			obj.setR60_PRODUCT(rs.getString("R60_PRODUCT")); obj.setR60_AMOUNT(rs.getBigDecimal("R60_AMOUNT"));
			return obj;
		}
	}

	class M_CA3ResubSummaryRowMapper implements RowMapper<M_CA3_Resub_Summary_Entity> {
		@Override
		public M_CA3_Resub_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA3_Resub_Summary_Entity obj = new M_CA3_Resub_Summary_Entity();
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			obj.setR10_PRODUCT(rs.getString("R10_PRODUCT")); obj.setR10_AMOUNT(rs.getBigDecimal("R10_AMOUNT"));
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT")); obj.setR11_AMOUNT(rs.getBigDecimal("R11_AMOUNT"));
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT")); obj.setR12_AMOUNT(rs.getBigDecimal("R12_AMOUNT"));
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT")); obj.setR13_AMOUNT(rs.getBigDecimal("R13_AMOUNT"));
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT")); obj.setR14_AMOUNT(rs.getBigDecimal("R14_AMOUNT"));
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT")); obj.setR15_AMOUNT(rs.getBigDecimal("R15_AMOUNT"));
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT")); obj.setR16_AMOUNT(rs.getBigDecimal("R16_AMOUNT"));
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT")); obj.setR17_AMOUNT(rs.getBigDecimal("R17_AMOUNT"));
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT")); obj.setR18_AMOUNT(rs.getBigDecimal("R18_AMOUNT"));
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT")); obj.setR19_AMOUNT(rs.getBigDecimal("R19_AMOUNT"));
			obj.setR20_PRODUCT(rs.getString("R20_PRODUCT")); obj.setR20_AMOUNT(rs.getBigDecimal("R20_AMOUNT"));
			obj.setR24_PRODUCT(rs.getString("R24_PRODUCT")); obj.setR24_AMOUNT(rs.getBigDecimal("R24_AMOUNT"));
			obj.setR25_PRODUCT(rs.getString("R25_PRODUCT")); obj.setR25_AMOUNT(rs.getBigDecimal("R25_AMOUNT"));
			obj.setR26_PRODUCT(rs.getString("R26_PRODUCT")); obj.setR26_AMOUNT(rs.getBigDecimal("R26_AMOUNT"));
			obj.setR27_PRODUCT(rs.getString("R27_PRODUCT")); obj.setR27_AMOUNT(rs.getBigDecimal("R27_AMOUNT"));
			obj.setR28_PRODUCT(rs.getString("R28_PRODUCT")); obj.setR28_AMOUNT(rs.getBigDecimal("R28_AMOUNT"));
			obj.setR29_PRODUCT(rs.getString("R29_PRODUCT")); obj.setR29_AMOUNT(rs.getBigDecimal("R29_AMOUNT"));
			obj.setR36_PRODUCT(rs.getString("R36_PRODUCT")); obj.setR36_AMOUNT(rs.getBigDecimal("R36_AMOUNT"));
			obj.setR37_PRODUCT(rs.getString("R37_PRODUCT")); obj.setR37_AMOUNT(rs.getBigDecimal("R37_AMOUNT"));
			obj.setR38_PRODUCT(rs.getString("R38_PRODUCT")); obj.setR38_AMOUNT(rs.getBigDecimal("R38_AMOUNT"));
			obj.setR39_PRODUCT(rs.getString("R39_PRODUCT")); obj.setR39_AMOUNT(rs.getBigDecimal("R39_AMOUNT"));
			obj.setR40_PRODUCT(rs.getString("R40_PRODUCT")); obj.setR40_AMOUNT(rs.getBigDecimal("R40_AMOUNT"));
			obj.setR41_PRODUCT(rs.getString("R41_PRODUCT")); obj.setR41_AMOUNT(rs.getBigDecimal("R41_AMOUNT"));
			obj.setR44_PRODUCT(rs.getString("R44_PRODUCT")); obj.setR44_AMOUNT(rs.getBigDecimal("R44_AMOUNT"));
			obj.setR45_PRODUCT(rs.getString("R45_PRODUCT")); obj.setR45_AMOUNT(rs.getBigDecimal("R45_AMOUNT"));
			obj.setR46_PRODUCT(rs.getString("R46_PRODUCT")); obj.setR46_AMOUNT(rs.getBigDecimal("R46_AMOUNT"));
			obj.setR50_PRODUCT(rs.getString("R50_PRODUCT")); obj.setR50_AMOUNT(rs.getBigDecimal("R50_AMOUNT"));
			obj.setR51_PRODUCT(rs.getString("R51_PRODUCT")); obj.setR51_AMOUNT(rs.getBigDecimal("R51_AMOUNT"));
			obj.setR52_PRODUCT(rs.getString("R52_PRODUCT")); obj.setR52_AMOUNT(rs.getBigDecimal("R52_AMOUNT"));
			obj.setR53_PRODUCT(rs.getString("R53_PRODUCT")); obj.setR53_AMOUNT(rs.getBigDecimal("R53_AMOUNT"));
			obj.setR54_PRODUCT(rs.getString("R54_PRODUCT")); obj.setR54_AMOUNT(rs.getBigDecimal("R54_AMOUNT"));
			obj.setR55_PRODUCT(rs.getString("R55_PRODUCT")); obj.setR55_AMOUNT(rs.getBigDecimal("R55_AMOUNT"));
			obj.setR58_PRODUCT(rs.getString("R58_PRODUCT")); obj.setR58_AMOUNT(rs.getBigDecimal("R58_AMOUNT"));
			obj.setR59_PRODUCT(rs.getString("R59_PRODUCT")); obj.setR59_AMOUNT(rs.getBigDecimal("R59_AMOUNT"));
			obj.setR60_PRODUCT(rs.getString("R60_PRODUCT")); obj.setR60_AMOUNT(rs.getBigDecimal("R60_AMOUNT"));
			return obj;
		}
	}

	class M_CA3ResubDetailRowMapper implements RowMapper<M_CA3_Resub_Detail_Entity> {
		@Override
		public M_CA3_Resub_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA3_Resub_Detail_Entity obj = new M_CA3_Resub_Detail_Entity();
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			obj.setR10_PRODUCT(rs.getString("R10_PRODUCT")); obj.setR10_AMOUNT(rs.getBigDecimal("R10_AMOUNT"));
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT")); obj.setR11_AMOUNT(rs.getBigDecimal("R11_AMOUNT"));
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT")); obj.setR12_AMOUNT(rs.getBigDecimal("R12_AMOUNT"));
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT")); obj.setR13_AMOUNT(rs.getBigDecimal("R13_AMOUNT"));
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT")); obj.setR14_AMOUNT(rs.getBigDecimal("R14_AMOUNT"));
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT")); obj.setR15_AMOUNT(rs.getBigDecimal("R15_AMOUNT"));
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT")); obj.setR16_AMOUNT(rs.getBigDecimal("R16_AMOUNT"));
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT")); obj.setR17_AMOUNT(rs.getBigDecimal("R17_AMOUNT"));
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT")); obj.setR18_AMOUNT(rs.getBigDecimal("R18_AMOUNT"));
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT")); obj.setR19_AMOUNT(rs.getBigDecimal("R19_AMOUNT"));
			obj.setR20_PRODUCT(rs.getString("R20_PRODUCT")); obj.setR20_AMOUNT(rs.getBigDecimal("R20_AMOUNT"));
			obj.setR24_PRODUCT(rs.getString("R24_PRODUCT")); obj.setR24_AMOUNT(rs.getBigDecimal("R24_AMOUNT"));
			obj.setR25_PRODUCT(rs.getString("R25_PRODUCT")); obj.setR25_AMOUNT(rs.getBigDecimal("R25_AMOUNT"));
			obj.setR26_PRODUCT(rs.getString("R26_PRODUCT")); obj.setR26_AMOUNT(rs.getBigDecimal("R26_AMOUNT"));
			obj.setR27_PRODUCT(rs.getString("R27_PRODUCT")); obj.setR27_AMOUNT(rs.getBigDecimal("R27_AMOUNT"));
			obj.setR28_PRODUCT(rs.getString("R28_PRODUCT")); obj.setR28_AMOUNT(rs.getBigDecimal("R28_AMOUNT"));
			obj.setR29_PRODUCT(rs.getString("R29_PRODUCT")); obj.setR29_AMOUNT(rs.getBigDecimal("R29_AMOUNT"));
			obj.setR36_PRODUCT(rs.getString("R36_PRODUCT")); obj.setR36_AMOUNT(rs.getBigDecimal("R36_AMOUNT"));
			obj.setR37_PRODUCT(rs.getString("R37_PRODUCT")); obj.setR37_AMOUNT(rs.getBigDecimal("R37_AMOUNT"));
			obj.setR38_PRODUCT(rs.getString("R38_PRODUCT")); obj.setR38_AMOUNT(rs.getBigDecimal("R38_AMOUNT"));
			obj.setR39_PRODUCT(rs.getString("R39_PRODUCT")); obj.setR39_AMOUNT(rs.getBigDecimal("R39_AMOUNT"));
			obj.setR40_PRODUCT(rs.getString("R40_PRODUCT")); obj.setR40_AMOUNT(rs.getBigDecimal("R40_AMOUNT"));
			obj.setR41_PRODUCT(rs.getString("R41_PRODUCT")); obj.setR41_AMOUNT(rs.getBigDecimal("R41_AMOUNT"));
			obj.setR44_PRODUCT(rs.getString("R44_PRODUCT")); obj.setR44_AMOUNT(rs.getBigDecimal("R44_AMOUNT"));
			obj.setR45_PRODUCT(rs.getString("R45_PRODUCT")); obj.setR45_AMOUNT(rs.getBigDecimal("R45_AMOUNT"));
			obj.setR46_PRODUCT(rs.getString("R46_PRODUCT")); obj.setR46_AMOUNT(rs.getBigDecimal("R46_AMOUNT"));
			obj.setR50_PRODUCT(rs.getString("R50_PRODUCT")); obj.setR50_AMOUNT(rs.getBigDecimal("R50_AMOUNT"));
			obj.setR51_PRODUCT(rs.getString("R51_PRODUCT")); obj.setR51_AMOUNT(rs.getBigDecimal("R51_AMOUNT"));
			obj.setR52_PRODUCT(rs.getString("R52_PRODUCT")); obj.setR52_AMOUNT(rs.getBigDecimal("R52_AMOUNT"));
			obj.setR53_PRODUCT(rs.getString("R53_PRODUCT")); obj.setR53_AMOUNT(rs.getBigDecimal("R53_AMOUNT"));
			obj.setR54_PRODUCT(rs.getString("R54_PRODUCT")); obj.setR54_AMOUNT(rs.getBigDecimal("R54_AMOUNT"));
			obj.setR55_PRODUCT(rs.getString("R55_PRODUCT")); obj.setR55_AMOUNT(rs.getBigDecimal("R55_AMOUNT"));
			obj.setR58_PRODUCT(rs.getString("R58_PRODUCT")); obj.setR58_AMOUNT(rs.getBigDecimal("R58_AMOUNT"));
			obj.setR59_PRODUCT(rs.getString("R59_PRODUCT")); obj.setR59_AMOUNT(rs.getBigDecimal("R59_AMOUNT"));
			obj.setR60_PRODUCT(rs.getString("R60_PRODUCT")); obj.setR60_AMOUNT(rs.getBigDecimal("R60_AMOUNT"));
			return obj;
		}
	}

}