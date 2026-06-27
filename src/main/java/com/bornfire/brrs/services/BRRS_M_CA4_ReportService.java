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
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.M_CA4_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_CA4_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_CA4_Detail_Entity;
import com.bornfire.brrs.entities.M_CA4_Resub_Detail_Entity;
import com.bornfire.brrs.entities.M_CA4_Resub_Summary_Entity;
import com.bornfire.brrs.entities.M_CA4_Summary_Entity;
import com.bornfire.brrs.entities.UserProfileRep;

@Service
@Transactional
public class BRRS_M_CA4_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_CA4_ReportService.class);

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

	public List<M_CA4_Summary_Entity> getSummaryByDate(Date reportDate) {
		return jdbcTemplate.query("SELECT * FROM BRRS_M_CA4_SUMMARYTABLE WHERE REPORT_DATE = ?",
				new Object[] { reportDate }, new M_CA4SummaryRowMapper());
	}

	public List<M_CA4_Detail_Entity> getDetailByDate(Date reportDate) {
		return jdbcTemplate.query("SELECT * FROM BRRS_M_CA4_DETAILTABLE WHERE REPORT_DATE = ?",
				new Object[] { reportDate }, new M_CA4DetailRowMapper());
	}

	public List<M_CA4_Archival_Summary_Entity> getArchivalSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_CA4_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
				new Object[] { reportDate, version }, new M_CA4ArchivalSummaryRowMapper());
	}

	public List<M_CA4_Archival_Summary_Entity> getArchivalSummaryWithVersionAll() {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_CA4_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC",
				new M_CA4ArchivalSummaryRowMapper());
	}

	public List<M_CA4_Archival_Detail_Entity> getArchivalDetailByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_CA4_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
				new Object[] { reportDate, version }, new M_CA4ArchivalDetailRowMapper());
	}

	public List<M_CA4_Resub_Summary_Entity> getResubSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_CA4_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
				new Object[] { reportDate, version }, new M_CA4ResubSummaryRowMapper());
	}

	public List<M_CA4_Resub_Summary_Entity> getResubSummaryWithVersionAll() {
		return jdbcTemplate.query("SELECT * FROM BRRS_M_CA4_RESUB_SUMMARYTABLE WHERE REPORT_VERSION IS NOT NULL",
				new M_CA4ResubSummaryRowMapper());
	}

	public List<M_CA4_Resub_Detail_Entity> getResubDetailByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_CA4_RESUB_DETAILTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
				new Object[] { reportDate, version }, new M_CA4ResubDetailRowMapper());
	}

	public BigDecimal findMaxResubVersion(Date reportDate) {
		return jdbcTemplate.queryForObject(
				"SELECT MAX(REPORT_VERSION) FROM BRRS_M_CA4_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ?",
				new Object[] { reportDate }, BigDecimal.class);
	}

	// =========================================================
	// JDBC WRITE METHODS
	// =========================================================

	private static final int[] R_ROWS = { 10, 11, 12, 13, 14, 17, 18, 19, 22, 23, 24, 27, 28, 29, 32, 33, 34, 36, 37,
			38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58 };

	private static final String R_FIELDS_SET;
	private static final String R_COLS;
	private static final String R_PLACEHOLDERS;

	static {
		StringBuilder setClause = new StringBuilder();
		StringBuilder cols = new StringBuilder();
		StringBuilder ph = new StringBuilder();
		for (int n : new int[] { 10, 11, 12, 13, 14, 17, 18, 19, 22, 23, 24, 27, 28, 29, 32, 33, 34, 36, 37, 38, 39, 40,
				41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58 }) {
			String p = "R" + n + "_";
			setClause.append(p).append("ITEM=?,");
			setClause.append(p).append("AMT_NAME_OF_SUB1=?,");
			setClause.append(p).append("AMT_NAME_OF_SUB2=?,");
			setClause.append(p).append("AMT_NAME_OF_SUB3=?,");
			setClause.append(p).append("AMT_NAME_OF_SUB4=?,");
			setClause.append(p).append("AMT_NAME_OF_SUB5=?,");
			setClause.append(p).append("TOT_AMT=?,");
			cols.append(p).append("ITEM,");
			cols.append(p).append("AMT_NAME_OF_SUB1,");
			cols.append(p).append("AMT_NAME_OF_SUB2,");
			cols.append(p).append("AMT_NAME_OF_SUB3,");
			cols.append(p).append("AMT_NAME_OF_SUB4,");
			cols.append(p).append("AMT_NAME_OF_SUB5,");
			cols.append(p).append("TOT_AMT,");
			ph.append("?,?,?,?,?,?,?,");
		}
		R_FIELDS_SET = setClause.substring(0, setClause.length() - 1);
		R_COLS = cols.substring(0, cols.length() - 1);
		R_PLACEHOLDERS = ph.substring(0, ph.length() - 1);
	}

	private Object[] rFieldValues(M_CA4_Summary_Entity e) {
		return new Object[] { e.getR10_item(), e.getR10_amt_name_of_sub1(), e.getR10_amt_name_of_sub2(),
				e.getR10_amt_name_of_sub3(), e.getR10_amt_name_of_sub4(), e.getR10_amt_name_of_sub5(),
				e.getR10_tot_amt(), e.getR11_item(), e.getR11_amt_name_of_sub1(), e.getR11_amt_name_of_sub2(),
				e.getR11_amt_name_of_sub3(), e.getR11_amt_name_of_sub4(), e.getR11_amt_name_of_sub5(),
				e.getR11_tot_amt(), e.getR12_item(), e.getR12_amt_name_of_sub1(), e.getR12_amt_name_of_sub2(),
				e.getR12_amt_name_of_sub3(), e.getR12_amt_name_of_sub4(), e.getR12_amt_name_of_sub5(),
				e.getR12_tot_amt(), e.getR13_item(), e.getR13_amt_name_of_sub1(), e.getR13_amt_name_of_sub2(),
				e.getR13_amt_name_of_sub3(), e.getR13_amt_name_of_sub4(), e.getR13_amt_name_of_sub5(),
				e.getR13_tot_amt(), e.getR14_item(), e.getR14_amt_name_of_sub1(), e.getR14_amt_name_of_sub2(),
				e.getR14_amt_name_of_sub3(), e.getR14_amt_name_of_sub4(), e.getR14_amt_name_of_sub5(),
				e.getR14_tot_amt(), e.getR17_item(), e.getR17_amt_name_of_sub1(), e.getR17_amt_name_of_sub2(),
				e.getR17_amt_name_of_sub3(), e.getR17_amt_name_of_sub4(), e.getR17_amt_name_of_sub5(),
				e.getR17_tot_amt(), e.getR18_item(), e.getR18_amt_name_of_sub1(), e.getR18_amt_name_of_sub2(),
				e.getR18_amt_name_of_sub3(), e.getR18_amt_name_of_sub4(), e.getR18_amt_name_of_sub5(),
				e.getR18_tot_amt(), e.getR19_item(), e.getR19_amt_name_of_sub1(), e.getR19_amt_name_of_sub2(),
				e.getR19_amt_name_of_sub3(), e.getR19_amt_name_of_sub4(), e.getR19_amt_name_of_sub5(),
				e.getR19_tot_amt(), e.getR22_item(), e.getR22_amt_name_of_sub1(), e.getR22_amt_name_of_sub2(),
				e.getR22_amt_name_of_sub3(), e.getR22_amt_name_of_sub4(), e.getR22_amt_name_of_sub5(),
				e.getR22_tot_amt(), e.getR23_item(), e.getR23_amt_name_of_sub1(), e.getR23_amt_name_of_sub2(),
				e.getR23_amt_name_of_sub3(), e.getR23_amt_name_of_sub4(), e.getR23_amt_name_of_sub5(),
				e.getR23_tot_amt(), e.getR24_item(), e.getR24_amt_name_of_sub1(), e.getR24_amt_name_of_sub2(),
				e.getR24_amt_name_of_sub3(), e.getR24_amt_name_of_sub4(), e.getR24_amt_name_of_sub5(),
				e.getR24_tot_amt(), e.getR27_item(), e.getR27_amt_name_of_sub1(), e.getR27_amt_name_of_sub2(),
				e.getR27_amt_name_of_sub3(), e.getR27_amt_name_of_sub4(), e.getR27_amt_name_of_sub5(),
				e.getR27_tot_amt(), e.getR28_item(), e.getR28_amt_name_of_sub1(), e.getR28_amt_name_of_sub2(),
				e.getR28_amt_name_of_sub3(), e.getR28_amt_name_of_sub4(), e.getR28_amt_name_of_sub5(),
				e.getR28_tot_amt(), e.getR29_item(), e.getR29_amt_name_of_sub1(), e.getR29_amt_name_of_sub2(),
				e.getR29_amt_name_of_sub3(), e.getR29_amt_name_of_sub4(), e.getR29_amt_name_of_sub5(),
				e.getR29_tot_amt(), e.getR32_item(), e.getR32_amt_name_of_sub1(), e.getR32_amt_name_of_sub2(),
				e.getR32_amt_name_of_sub3(), e.getR32_amt_name_of_sub4(), e.getR32_amt_name_of_sub5(),
				e.getR32_tot_amt(), e.getR33_item(), e.getR33_amt_name_of_sub1(), e.getR33_amt_name_of_sub2(),
				e.getR33_amt_name_of_sub3(), e.getR33_amt_name_of_sub4(), e.getR33_amt_name_of_sub5(),
				e.getR33_tot_amt(), e.getR34_item(), e.getR34_amt_name_of_sub1(), e.getR34_amt_name_of_sub2(),
				e.getR34_amt_name_of_sub3(), e.getR34_amt_name_of_sub4(), e.getR34_amt_name_of_sub5(),
				e.getR34_tot_amt(), e.getR36_item(), e.getR36_amt_name_of_sub1(), e.getR36_amt_name_of_sub2(),
				e.getR36_amt_name_of_sub3(), e.getR36_amt_name_of_sub4(), e.getR36_amt_name_of_sub5(),
				e.getR36_tot_amt(), e.getR37_item(), e.getR37_amt_name_of_sub1(), e.getR37_amt_name_of_sub2(),
				e.getR37_amt_name_of_sub3(), e.getR37_amt_name_of_sub4(), e.getR37_amt_name_of_sub5(),
				e.getR37_tot_amt(), e.getR38_item(), e.getR38_amt_name_of_sub1(), e.getR38_amt_name_of_sub2(),
				e.getR38_amt_name_of_sub3(), e.getR38_amt_name_of_sub4(), e.getR38_amt_name_of_sub5(),
				e.getR38_tot_amt(), e.getR39_item(), e.getR39_amt_name_of_sub1(), e.getR39_amt_name_of_sub2(),
				e.getR39_amt_name_of_sub3(), e.getR39_amt_name_of_sub4(), e.getR39_amt_name_of_sub5(),
				e.getR39_tot_amt(), e.getR40_item(), e.getR40_amt_name_of_sub1(), e.getR40_amt_name_of_sub2(),
				e.getR40_amt_name_of_sub3(), e.getR40_amt_name_of_sub4(), e.getR40_amt_name_of_sub5(),
				e.getR40_tot_amt(), e.getR41_item(), e.getR41_amt_name_of_sub1(), e.getR41_amt_name_of_sub2(),
				e.getR41_amt_name_of_sub3(), e.getR41_amt_name_of_sub4(), e.getR41_amt_name_of_sub5(),
				e.getR41_tot_amt(), e.getR42_item(), e.getR42_amt_name_of_sub1(), e.getR42_amt_name_of_sub2(),
				e.getR42_amt_name_of_sub3(), e.getR42_amt_name_of_sub4(), e.getR42_amt_name_of_sub5(),
				e.getR42_tot_amt(), e.getR43_item(), e.getR43_amt_name_of_sub1(), e.getR43_amt_name_of_sub2(),
				e.getR43_amt_name_of_sub3(), e.getR43_amt_name_of_sub4(), e.getR43_amt_name_of_sub5(),
				e.getR43_tot_amt(), e.getR44_item(), e.getR44_amt_name_of_sub1(), e.getR44_amt_name_of_sub2(),
				e.getR44_amt_name_of_sub3(), e.getR44_amt_name_of_sub4(), e.getR44_amt_name_of_sub5(),
				e.getR44_tot_amt(), e.getR45_item(), e.getR45_amt_name_of_sub1(), e.getR45_amt_name_of_sub2(),
				e.getR45_amt_name_of_sub3(), e.getR45_amt_name_of_sub4(), e.getR45_amt_name_of_sub5(),
				e.getR45_tot_amt(), e.getR46_item(), e.getR46_amt_name_of_sub1(), e.getR46_amt_name_of_sub2(),
				e.getR46_amt_name_of_sub3(), e.getR46_amt_name_of_sub4(), e.getR46_amt_name_of_sub5(),
				e.getR46_tot_amt(), e.getR47_item(), e.getR47_amt_name_of_sub1(), e.getR47_amt_name_of_sub2(),
				e.getR47_amt_name_of_sub3(), e.getR47_amt_name_of_sub4(), e.getR47_amt_name_of_sub5(),
				e.getR47_tot_amt(), e.getR48_item(), e.getR48_amt_name_of_sub1(), e.getR48_amt_name_of_sub2(),
				e.getR48_amt_name_of_sub3(), e.getR48_amt_name_of_sub4(), e.getR48_amt_name_of_sub5(),
				e.getR48_tot_amt(), e.getR49_item(), e.getR49_amt_name_of_sub1(), e.getR49_amt_name_of_sub2(),
				e.getR49_amt_name_of_sub3(), e.getR49_amt_name_of_sub4(), e.getR49_amt_name_of_sub5(),
				e.getR49_tot_amt(), e.getR50_item(), e.getR50_amt_name_of_sub1(), e.getR50_amt_name_of_sub2(),
				e.getR50_amt_name_of_sub3(), e.getR50_amt_name_of_sub4(), e.getR50_amt_name_of_sub5(),
				e.getR50_tot_amt(), e.getR51_item(), e.getR51_amt_name_of_sub1(), e.getR51_amt_name_of_sub2(),
				e.getR51_amt_name_of_sub3(), e.getR51_amt_name_of_sub4(), e.getR51_amt_name_of_sub5(),
				e.getR51_tot_amt(), e.getR52_item(), e.getR52_amt_name_of_sub1(), e.getR52_amt_name_of_sub2(),
				e.getR52_amt_name_of_sub3(), e.getR52_amt_name_of_sub4(), e.getR52_amt_name_of_sub5(),
				e.getR52_tot_amt(), e.getR53_item(), e.getR53_amt_name_of_sub1(), e.getR53_amt_name_of_sub2(),
				e.getR53_amt_name_of_sub3(), e.getR53_amt_name_of_sub4(), e.getR53_amt_name_of_sub5(),
				e.getR53_tot_amt(), e.getR54_item(), e.getR54_amt_name_of_sub1(), e.getR54_amt_name_of_sub2(),
				e.getR54_amt_name_of_sub3(), e.getR54_amt_name_of_sub4(), e.getR54_amt_name_of_sub5(),
				e.getR54_tot_amt(), e.getR55_item(), e.getR55_amt_name_of_sub1(), e.getR55_amt_name_of_sub2(),
				e.getR55_amt_name_of_sub3(), e.getR55_amt_name_of_sub4(), e.getR55_amt_name_of_sub5(),
				e.getR55_tot_amt(), e.getR56_item(), e.getR56_amt_name_of_sub1(), e.getR56_amt_name_of_sub2(),
				e.getR56_amt_name_of_sub3(), e.getR56_amt_name_of_sub4(), e.getR56_amt_name_of_sub5(),
				e.getR56_tot_amt(), e.getR57_item(), e.getR57_amt_name_of_sub1(), e.getR57_amt_name_of_sub2(),
				e.getR57_amt_name_of_sub3(), e.getR57_amt_name_of_sub4(), e.getR57_amt_name_of_sub5(),
				e.getR57_tot_amt(), e.getR58_item(), e.getR58_amt_name_of_sub1(), e.getR58_amt_name_of_sub2(),
				e.getR58_amt_name_of_sub3(), e.getR58_amt_name_of_sub4(), e.getR58_amt_name_of_sub5(),
				e.getR58_tot_amt() };
	}

	private Object[] rFieldValues(M_CA4_Archival_Summary_Entity e) {
		return new Object[] { e.getR10_item(), e.getR10_amt_name_of_sub1(), e.getR10_amt_name_of_sub2(),
				e.getR10_amt_name_of_sub3(), e.getR10_amt_name_of_sub4(), e.getR10_amt_name_of_sub5(),
				e.getR10_tot_amt(), e.getR11_item(), e.getR11_amt_name_of_sub1(), e.getR11_amt_name_of_sub2(),
				e.getR11_amt_name_of_sub3(), e.getR11_amt_name_of_sub4(), e.getR11_amt_name_of_sub5(),
				e.getR11_tot_amt(), e.getR12_item(), e.getR12_amt_name_of_sub1(), e.getR12_amt_name_of_sub2(),
				e.getR12_amt_name_of_sub3(), e.getR12_amt_name_of_sub4(), e.getR12_amt_name_of_sub5(),
				e.getR12_tot_amt(), e.getR13_item(), e.getR13_amt_name_of_sub1(), e.getR13_amt_name_of_sub2(),
				e.getR13_amt_name_of_sub3(), e.getR13_amt_name_of_sub4(), e.getR13_amt_name_of_sub5(),
				e.getR13_tot_amt(), e.getR14_item(), e.getR14_amt_name_of_sub1(), e.getR14_amt_name_of_sub2(),
				e.getR14_amt_name_of_sub3(), e.getR14_amt_name_of_sub4(), e.getR14_amt_name_of_sub5(),
				e.getR14_tot_amt(), e.getR17_item(), e.getR17_amt_name_of_sub1(), e.getR17_amt_name_of_sub2(),
				e.getR17_amt_name_of_sub3(), e.getR17_amt_name_of_sub4(), e.getR17_amt_name_of_sub5(),
				e.getR17_tot_amt(), e.getR18_item(), e.getR18_amt_name_of_sub1(), e.getR18_amt_name_of_sub2(),
				e.getR18_amt_name_of_sub3(), e.getR18_amt_name_of_sub4(), e.getR18_amt_name_of_sub5(),
				e.getR18_tot_amt(), e.getR19_item(), e.getR19_amt_name_of_sub1(), e.getR19_amt_name_of_sub2(),
				e.getR19_amt_name_of_sub3(), e.getR19_amt_name_of_sub4(), e.getR19_amt_name_of_sub5(),
				e.getR19_tot_amt(), e.getR22_item(), e.getR22_amt_name_of_sub1(), e.getR22_amt_name_of_sub2(),
				e.getR22_amt_name_of_sub3(), e.getR22_amt_name_of_sub4(), e.getR22_amt_name_of_sub5(),
				e.getR22_tot_amt(), e.getR23_item(), e.getR23_amt_name_of_sub1(), e.getR23_amt_name_of_sub2(),
				e.getR23_amt_name_of_sub3(), e.getR23_amt_name_of_sub4(), e.getR23_amt_name_of_sub5(),
				e.getR23_tot_amt(), e.getR24_item(), e.getR24_amt_name_of_sub1(), e.getR24_amt_name_of_sub2(),
				e.getR24_amt_name_of_sub3(), e.getR24_amt_name_of_sub4(), e.getR24_amt_name_of_sub5(),
				e.getR24_tot_amt(), e.getR27_item(), e.getR27_amt_name_of_sub1(), e.getR27_amt_name_of_sub2(),
				e.getR27_amt_name_of_sub3(), e.getR27_amt_name_of_sub4(), e.getR27_amt_name_of_sub5(),
				e.getR27_tot_amt(), e.getR28_item(), e.getR28_amt_name_of_sub1(), e.getR28_amt_name_of_sub2(),
				e.getR28_amt_name_of_sub3(), e.getR28_amt_name_of_sub4(), e.getR28_amt_name_of_sub5(),
				e.getR28_tot_amt(), e.getR29_item(), e.getR29_amt_name_of_sub1(), e.getR29_amt_name_of_sub2(),
				e.getR29_amt_name_of_sub3(), e.getR29_amt_name_of_sub4(), e.getR29_amt_name_of_sub5(),
				e.getR29_tot_amt(), e.getR32_item(), e.getR32_amt_name_of_sub1(), e.getR32_amt_name_of_sub2(),
				e.getR32_amt_name_of_sub3(), e.getR32_amt_name_of_sub4(), e.getR32_amt_name_of_sub5(),
				e.getR32_tot_amt(), e.getR33_item(), e.getR33_amt_name_of_sub1(), e.getR33_amt_name_of_sub2(),
				e.getR33_amt_name_of_sub3(), e.getR33_amt_name_of_sub4(), e.getR33_amt_name_of_sub5(),
				e.getR33_tot_amt(), e.getR34_item(), e.getR34_amt_name_of_sub1(), e.getR34_amt_name_of_sub2(),
				e.getR34_amt_name_of_sub3(), e.getR34_amt_name_of_sub4(), e.getR34_amt_name_of_sub5(),
				e.getR34_tot_amt(), e.getR36_item(), e.getR36_amt_name_of_sub1(), e.getR36_amt_name_of_sub2(),
				e.getR36_amt_name_of_sub3(), e.getR36_amt_name_of_sub4(), e.getR36_amt_name_of_sub5(),
				e.getR36_tot_amt(), e.getR37_item(), e.getR37_amt_name_of_sub1(), e.getR37_amt_name_of_sub2(),
				e.getR37_amt_name_of_sub3(), e.getR37_amt_name_of_sub4(), e.getR37_amt_name_of_sub5(),
				e.getR37_tot_amt(), e.getR38_item(), e.getR38_amt_name_of_sub1(), e.getR38_amt_name_of_sub2(),
				e.getR38_amt_name_of_sub3(), e.getR38_amt_name_of_sub4(), e.getR38_amt_name_of_sub5(),
				e.getR38_tot_amt(), e.getR39_item(), e.getR39_amt_name_of_sub1(), e.getR39_amt_name_of_sub2(),
				e.getR39_amt_name_of_sub3(), e.getR39_amt_name_of_sub4(), e.getR39_amt_name_of_sub5(),
				e.getR39_tot_amt(), e.getR40_item(), e.getR40_amt_name_of_sub1(), e.getR40_amt_name_of_sub2(),
				e.getR40_amt_name_of_sub3(), e.getR40_amt_name_of_sub4(), e.getR40_amt_name_of_sub5(),
				e.getR40_tot_amt(), e.getR41_item(), e.getR41_amt_name_of_sub1(), e.getR41_amt_name_of_sub2(),
				e.getR41_amt_name_of_sub3(), e.getR41_amt_name_of_sub4(), e.getR41_amt_name_of_sub5(),
				e.getR41_tot_amt(), e.getR42_item(), e.getR42_amt_name_of_sub1(), e.getR42_amt_name_of_sub2(),
				e.getR42_amt_name_of_sub3(), e.getR42_amt_name_of_sub4(), e.getR42_amt_name_of_sub5(),
				e.getR42_tot_amt(), e.getR43_item(), e.getR43_amt_name_of_sub1(), e.getR43_amt_name_of_sub2(),
				e.getR43_amt_name_of_sub3(), e.getR43_amt_name_of_sub4(), e.getR43_amt_name_of_sub5(),
				e.getR43_tot_amt(), e.getR44_item(), e.getR44_amt_name_of_sub1(), e.getR44_amt_name_of_sub2(),
				e.getR44_amt_name_of_sub3(), e.getR44_amt_name_of_sub4(), e.getR44_amt_name_of_sub5(),
				e.getR44_tot_amt(), e.getR45_item(), e.getR45_amt_name_of_sub1(), e.getR45_amt_name_of_sub2(),
				e.getR45_amt_name_of_sub3(), e.getR45_amt_name_of_sub4(), e.getR45_amt_name_of_sub5(),
				e.getR45_tot_amt(), e.getR46_item(), e.getR46_amt_name_of_sub1(), e.getR46_amt_name_of_sub2(),
				e.getR46_amt_name_of_sub3(), e.getR46_amt_name_of_sub4(), e.getR46_amt_name_of_sub5(),
				e.getR46_tot_amt(), e.getR47_item(), e.getR47_amt_name_of_sub1(), e.getR47_amt_name_of_sub2(),
				e.getR47_amt_name_of_sub3(), e.getR47_amt_name_of_sub4(), e.getR47_amt_name_of_sub5(),
				e.getR47_tot_amt(), e.getR48_item(), e.getR48_amt_name_of_sub1(), e.getR48_amt_name_of_sub2(),
				e.getR48_amt_name_of_sub3(), e.getR48_amt_name_of_sub4(), e.getR48_amt_name_of_sub5(),
				e.getR48_tot_amt(), e.getR49_item(), e.getR49_amt_name_of_sub1(), e.getR49_amt_name_of_sub2(),
				e.getR49_amt_name_of_sub3(), e.getR49_amt_name_of_sub4(), e.getR49_amt_name_of_sub5(),
				e.getR49_tot_amt(), e.getR50_item(), e.getR50_amt_name_of_sub1(), e.getR50_amt_name_of_sub2(),
				e.getR50_amt_name_of_sub3(), e.getR50_amt_name_of_sub4(), e.getR50_amt_name_of_sub5(),
				e.getR50_tot_amt(), e.getR51_item(), e.getR51_amt_name_of_sub1(), e.getR51_amt_name_of_sub2(),
				e.getR51_amt_name_of_sub3(), e.getR51_amt_name_of_sub4(), e.getR51_amt_name_of_sub5(),
				e.getR51_tot_amt(), e.getR52_item(), e.getR52_amt_name_of_sub1(), e.getR52_amt_name_of_sub2(),
				e.getR52_amt_name_of_sub3(), e.getR52_amt_name_of_sub4(), e.getR52_amt_name_of_sub5(),
				e.getR52_tot_amt(), e.getR53_item(), e.getR53_amt_name_of_sub1(), e.getR53_amt_name_of_sub2(),
				e.getR53_amt_name_of_sub3(), e.getR53_amt_name_of_sub4(), e.getR53_amt_name_of_sub5(),
				e.getR53_tot_amt(), e.getR54_item(), e.getR54_amt_name_of_sub1(), e.getR54_amt_name_of_sub2(),
				e.getR54_amt_name_of_sub3(), e.getR54_amt_name_of_sub4(), e.getR54_amt_name_of_sub5(),
				e.getR54_tot_amt(), e.getR55_item(), e.getR55_amt_name_of_sub1(), e.getR55_amt_name_of_sub2(),
				e.getR55_amt_name_of_sub3(), e.getR55_amt_name_of_sub4(), e.getR55_amt_name_of_sub5(),
				e.getR55_tot_amt(), e.getR56_item(), e.getR56_amt_name_of_sub1(), e.getR56_amt_name_of_sub2(),
				e.getR56_amt_name_of_sub3(), e.getR56_amt_name_of_sub4(), e.getR56_amt_name_of_sub5(),
				e.getR56_tot_amt(), e.getR57_item(), e.getR57_amt_name_of_sub1(), e.getR57_amt_name_of_sub2(),
				e.getR57_amt_name_of_sub3(), e.getR57_amt_name_of_sub4(), e.getR57_amt_name_of_sub5(),
				e.getR57_tot_amt(), e.getR58_item(), e.getR58_amt_name_of_sub1(), e.getR58_amt_name_of_sub2(),
				e.getR58_amt_name_of_sub3(), e.getR58_amt_name_of_sub4(), e.getR58_amt_name_of_sub5(),
				e.getR58_tot_amt() };
	}

	private Object[] rFieldValues(M_CA4_Resub_Summary_Entity e) {
		return new Object[] { e.getR10_item(), e.getR10_amt_name_of_sub1(), e.getR10_amt_name_of_sub2(),
				e.getR10_amt_name_of_sub3(), e.getR10_amt_name_of_sub4(), e.getR10_amt_name_of_sub5(),
				e.getR10_tot_amt(), e.getR11_item(), e.getR11_amt_name_of_sub1(), e.getR11_amt_name_of_sub2(),
				e.getR11_amt_name_of_sub3(), e.getR11_amt_name_of_sub4(), e.getR11_amt_name_of_sub5(),
				e.getR11_tot_amt(), e.getR12_item(), e.getR12_amt_name_of_sub1(), e.getR12_amt_name_of_sub2(),
				e.getR12_amt_name_of_sub3(), e.getR12_amt_name_of_sub4(), e.getR12_amt_name_of_sub5(),
				e.getR12_tot_amt(), e.getR13_item(), e.getR13_amt_name_of_sub1(), e.getR13_amt_name_of_sub2(),
				e.getR13_amt_name_of_sub3(), e.getR13_amt_name_of_sub4(), e.getR13_amt_name_of_sub5(),
				e.getR13_tot_amt(), e.getR14_item(), e.getR14_amt_name_of_sub1(), e.getR14_amt_name_of_sub2(),
				e.getR14_amt_name_of_sub3(), e.getR14_amt_name_of_sub4(), e.getR14_amt_name_of_sub5(),
				e.getR14_tot_amt(), e.getR17_item(), e.getR17_amt_name_of_sub1(), e.getR17_amt_name_of_sub2(),
				e.getR17_amt_name_of_sub3(), e.getR17_amt_name_of_sub4(), e.getR17_amt_name_of_sub5(),
				e.getR17_tot_amt(), e.getR18_item(), e.getR18_amt_name_of_sub1(), e.getR18_amt_name_of_sub2(),
				e.getR18_amt_name_of_sub3(), e.getR18_amt_name_of_sub4(), e.getR18_amt_name_of_sub5(),
				e.getR18_tot_amt(), e.getR19_item(), e.getR19_amt_name_of_sub1(), e.getR19_amt_name_of_sub2(),
				e.getR19_amt_name_of_sub3(), e.getR19_amt_name_of_sub4(), e.getR19_amt_name_of_sub5(),
				e.getR19_tot_amt(), e.getR22_item(), e.getR22_amt_name_of_sub1(), e.getR22_amt_name_of_sub2(),
				e.getR22_amt_name_of_sub3(), e.getR22_amt_name_of_sub4(), e.getR22_amt_name_of_sub5(),
				e.getR22_tot_amt(), e.getR23_item(), e.getR23_amt_name_of_sub1(), e.getR23_amt_name_of_sub2(),
				e.getR23_amt_name_of_sub3(), e.getR23_amt_name_of_sub4(), e.getR23_amt_name_of_sub5(),
				e.getR23_tot_amt(), e.getR24_item(), e.getR24_amt_name_of_sub1(), e.getR24_amt_name_of_sub2(),
				e.getR24_amt_name_of_sub3(), e.getR24_amt_name_of_sub4(), e.getR24_amt_name_of_sub5(),
				e.getR24_tot_amt(), e.getR27_item(), e.getR27_amt_name_of_sub1(), e.getR27_amt_name_of_sub2(),
				e.getR27_amt_name_of_sub3(), e.getR27_amt_name_of_sub4(), e.getR27_amt_name_of_sub5(),
				e.getR27_tot_amt(), e.getR28_item(), e.getR28_amt_name_of_sub1(), e.getR28_amt_name_of_sub2(),
				e.getR28_amt_name_of_sub3(), e.getR28_amt_name_of_sub4(), e.getR28_amt_name_of_sub5(),
				e.getR28_tot_amt(), e.getR29_item(), e.getR29_amt_name_of_sub1(), e.getR29_amt_name_of_sub2(),
				e.getR29_amt_name_of_sub3(), e.getR29_amt_name_of_sub4(), e.getR29_amt_name_of_sub5(),
				e.getR29_tot_amt(), e.getR32_item(), e.getR32_amt_name_of_sub1(), e.getR32_amt_name_of_sub2(),
				e.getR32_amt_name_of_sub3(), e.getR32_amt_name_of_sub4(), e.getR32_amt_name_of_sub5(),
				e.getR32_tot_amt(), e.getR33_item(), e.getR33_amt_name_of_sub1(), e.getR33_amt_name_of_sub2(),
				e.getR33_amt_name_of_sub3(), e.getR33_amt_name_of_sub4(), e.getR33_amt_name_of_sub5(),
				e.getR33_tot_amt(), e.getR34_item(), e.getR34_amt_name_of_sub1(), e.getR34_amt_name_of_sub2(),
				e.getR34_amt_name_of_sub3(), e.getR34_amt_name_of_sub4(), e.getR34_amt_name_of_sub5(),
				e.getR34_tot_amt(), e.getR36_item(), e.getR36_amt_name_of_sub1(), e.getR36_amt_name_of_sub2(),
				e.getR36_amt_name_of_sub3(), e.getR36_amt_name_of_sub4(), e.getR36_amt_name_of_sub5(),
				e.getR36_tot_amt(), e.getR37_item(), e.getR37_amt_name_of_sub1(), e.getR37_amt_name_of_sub2(),
				e.getR37_amt_name_of_sub3(), e.getR37_amt_name_of_sub4(), e.getR37_amt_name_of_sub5(),
				e.getR37_tot_amt(), e.getR38_item(), e.getR38_amt_name_of_sub1(), e.getR38_amt_name_of_sub2(),
				e.getR38_amt_name_of_sub3(), e.getR38_amt_name_of_sub4(), e.getR38_amt_name_of_sub5(),
				e.getR38_tot_amt(), e.getR39_item(), e.getR39_amt_name_of_sub1(), e.getR39_amt_name_of_sub2(),
				e.getR39_amt_name_of_sub3(), e.getR39_amt_name_of_sub4(), e.getR39_amt_name_of_sub5(),
				e.getR39_tot_amt(), e.getR40_item(), e.getR40_amt_name_of_sub1(), e.getR40_amt_name_of_sub2(),
				e.getR40_amt_name_of_sub3(), e.getR40_amt_name_of_sub4(), e.getR40_amt_name_of_sub5(),
				e.getR40_tot_amt(), e.getR41_item(), e.getR41_amt_name_of_sub1(), e.getR41_amt_name_of_sub2(),
				e.getR41_amt_name_of_sub3(), e.getR41_amt_name_of_sub4(), e.getR41_amt_name_of_sub5(),
				e.getR41_tot_amt(), e.getR42_item(), e.getR42_amt_name_of_sub1(), e.getR42_amt_name_of_sub2(),
				e.getR42_amt_name_of_sub3(), e.getR42_amt_name_of_sub4(), e.getR42_amt_name_of_sub5(),
				e.getR42_tot_amt(), e.getR43_item(), e.getR43_amt_name_of_sub1(), e.getR43_amt_name_of_sub2(),
				e.getR43_amt_name_of_sub3(), e.getR43_amt_name_of_sub4(), e.getR43_amt_name_of_sub5(),
				e.getR43_tot_amt(), e.getR44_item(), e.getR44_amt_name_of_sub1(), e.getR44_amt_name_of_sub2(),
				e.getR44_amt_name_of_sub3(), e.getR44_amt_name_of_sub4(), e.getR44_amt_name_of_sub5(),
				e.getR44_tot_amt(), e.getR45_item(), e.getR45_amt_name_of_sub1(), e.getR45_amt_name_of_sub2(),
				e.getR45_amt_name_of_sub3(), e.getR45_amt_name_of_sub4(), e.getR45_amt_name_of_sub5(),
				e.getR45_tot_amt(), e.getR46_item(), e.getR46_amt_name_of_sub1(), e.getR46_amt_name_of_sub2(),
				e.getR46_amt_name_of_sub3(), e.getR46_amt_name_of_sub4(), e.getR46_amt_name_of_sub5(),
				e.getR46_tot_amt(), e.getR47_item(), e.getR47_amt_name_of_sub1(), e.getR47_amt_name_of_sub2(),
				e.getR47_amt_name_of_sub3(), e.getR47_amt_name_of_sub4(), e.getR47_amt_name_of_sub5(),
				e.getR47_tot_amt(), e.getR48_item(), e.getR48_amt_name_of_sub1(), e.getR48_amt_name_of_sub2(),
				e.getR48_amt_name_of_sub3(), e.getR48_amt_name_of_sub4(), e.getR48_amt_name_of_sub5(),
				e.getR48_tot_amt(), e.getR49_item(), e.getR49_amt_name_of_sub1(), e.getR49_amt_name_of_sub2(),
				e.getR49_amt_name_of_sub3(), e.getR49_amt_name_of_sub4(), e.getR49_amt_name_of_sub5(),
				e.getR49_tot_amt(), e.getR50_item(), e.getR50_amt_name_of_sub1(), e.getR50_amt_name_of_sub2(),
				e.getR50_amt_name_of_sub3(), e.getR50_amt_name_of_sub4(), e.getR50_amt_name_of_sub5(),
				e.getR50_tot_amt(), e.getR51_item(), e.getR51_amt_name_of_sub1(), e.getR51_amt_name_of_sub2(),
				e.getR51_amt_name_of_sub3(), e.getR51_amt_name_of_sub4(), e.getR51_amt_name_of_sub5(),
				e.getR51_tot_amt(), e.getR52_item(), e.getR52_amt_name_of_sub1(), e.getR52_amt_name_of_sub2(),
				e.getR52_amt_name_of_sub3(), e.getR52_amt_name_of_sub4(), e.getR52_amt_name_of_sub5(),
				e.getR52_tot_amt(), e.getR53_item(), e.getR53_amt_name_of_sub1(), e.getR53_amt_name_of_sub2(),
				e.getR53_amt_name_of_sub3(), e.getR53_amt_name_of_sub4(), e.getR53_amt_name_of_sub5(),
				e.getR53_tot_amt(), e.getR54_item(), e.getR54_amt_name_of_sub1(), e.getR54_amt_name_of_sub2(),
				e.getR54_amt_name_of_sub3(), e.getR54_amt_name_of_sub4(), e.getR54_amt_name_of_sub5(),
				e.getR54_tot_amt(), e.getR55_item(), e.getR55_amt_name_of_sub1(), e.getR55_amt_name_of_sub2(),
				e.getR55_amt_name_of_sub3(), e.getR55_amt_name_of_sub4(), e.getR55_amt_name_of_sub5(),
				e.getR55_tot_amt(), e.getR56_item(), e.getR56_amt_name_of_sub1(), e.getR56_amt_name_of_sub2(),
				e.getR56_amt_name_of_sub3(), e.getR56_amt_name_of_sub4(), e.getR56_amt_name_of_sub5(),
				e.getR56_tot_amt(), e.getR57_item(), e.getR57_amt_name_of_sub1(), e.getR57_amt_name_of_sub2(),
				e.getR57_amt_name_of_sub3(), e.getR57_amt_name_of_sub4(), e.getR57_amt_name_of_sub5(),
				e.getR57_tot_amt(), e.getR58_item(), e.getR58_amt_name_of_sub1(), e.getR58_amt_name_of_sub2(),
				e.getR58_amt_name_of_sub3(), e.getR58_amt_name_of_sub4(), e.getR58_amt_name_of_sub5(),
				e.getR58_tot_amt() };
	}

	private Object[] rFieldValues(M_CA4_Resub_Detail_Entity e) {
		return new Object[] { e.getR10_item(), e.getR10_amt_name_of_sub1(), e.getR10_amt_name_of_sub2(),
				e.getR10_amt_name_of_sub3(), e.getR10_amt_name_of_sub4(), e.getR10_amt_name_of_sub5(),
				e.getR10_tot_amt(), e.getR11_item(), e.getR11_amt_name_of_sub1(), e.getR11_amt_name_of_sub2(),
				e.getR11_amt_name_of_sub3(), e.getR11_amt_name_of_sub4(), e.getR11_amt_name_of_sub5(),
				e.getR11_tot_amt(), e.getR12_item(), e.getR12_amt_name_of_sub1(), e.getR12_amt_name_of_sub2(),
				e.getR12_amt_name_of_sub3(), e.getR12_amt_name_of_sub4(), e.getR12_amt_name_of_sub5(),
				e.getR12_tot_amt(), e.getR13_item(), e.getR13_amt_name_of_sub1(), e.getR13_amt_name_of_sub2(),
				e.getR13_amt_name_of_sub3(), e.getR13_amt_name_of_sub4(), e.getR13_amt_name_of_sub5(),
				e.getR13_tot_amt(), e.getR14_item(), e.getR14_amt_name_of_sub1(), e.getR14_amt_name_of_sub2(),
				e.getR14_amt_name_of_sub3(), e.getR14_amt_name_of_sub4(), e.getR14_amt_name_of_sub5(),
				e.getR14_tot_amt(), e.getR17_item(), e.getR17_amt_name_of_sub1(), e.getR17_amt_name_of_sub2(),
				e.getR17_amt_name_of_sub3(), e.getR17_amt_name_of_sub4(), e.getR17_amt_name_of_sub5(),
				e.getR17_tot_amt(), e.getR18_item(), e.getR18_amt_name_of_sub1(), e.getR18_amt_name_of_sub2(),
				e.getR18_amt_name_of_sub3(), e.getR18_amt_name_of_sub4(), e.getR18_amt_name_of_sub5(),
				e.getR18_tot_amt(), e.getR19_item(), e.getR19_amt_name_of_sub1(), e.getR19_amt_name_of_sub2(),
				e.getR19_amt_name_of_sub3(), e.getR19_amt_name_of_sub4(), e.getR19_amt_name_of_sub5(),
				e.getR19_tot_amt(), e.getR22_item(), e.getR22_amt_name_of_sub1(), e.getR22_amt_name_of_sub2(),
				e.getR22_amt_name_of_sub3(), e.getR22_amt_name_of_sub4(), e.getR22_amt_name_of_sub5(),
				e.getR22_tot_amt(), e.getR23_item(), e.getR23_amt_name_of_sub1(), e.getR23_amt_name_of_sub2(),
				e.getR23_amt_name_of_sub3(), e.getR23_amt_name_of_sub4(), e.getR23_amt_name_of_sub5(),
				e.getR23_tot_amt(), e.getR24_item(), e.getR24_amt_name_of_sub1(), e.getR24_amt_name_of_sub2(),
				e.getR24_amt_name_of_sub3(), e.getR24_amt_name_of_sub4(), e.getR24_amt_name_of_sub5(),
				e.getR24_tot_amt(), e.getR27_item(), e.getR27_amt_name_of_sub1(), e.getR27_amt_name_of_sub2(),
				e.getR27_amt_name_of_sub3(), e.getR27_amt_name_of_sub4(), e.getR27_amt_name_of_sub5(),
				e.getR27_tot_amt(), e.getR28_item(), e.getR28_amt_name_of_sub1(), e.getR28_amt_name_of_sub2(),
				e.getR28_amt_name_of_sub3(), e.getR28_amt_name_of_sub4(), e.getR28_amt_name_of_sub5(),
				e.getR28_tot_amt(), e.getR29_item(), e.getR29_amt_name_of_sub1(), e.getR29_amt_name_of_sub2(),
				e.getR29_amt_name_of_sub3(), e.getR29_amt_name_of_sub4(), e.getR29_amt_name_of_sub5(),
				e.getR29_tot_amt(), e.getR32_item(), e.getR32_amt_name_of_sub1(), e.getR32_amt_name_of_sub2(),
				e.getR32_amt_name_of_sub3(), e.getR32_amt_name_of_sub4(), e.getR32_amt_name_of_sub5(),
				e.getR32_tot_amt(), e.getR33_item(), e.getR33_amt_name_of_sub1(), e.getR33_amt_name_of_sub2(),
				e.getR33_amt_name_of_sub3(), e.getR33_amt_name_of_sub4(), e.getR33_amt_name_of_sub5(),
				e.getR33_tot_amt(), e.getR34_item(), e.getR34_amt_name_of_sub1(), e.getR34_amt_name_of_sub2(),
				e.getR34_amt_name_of_sub3(), e.getR34_amt_name_of_sub4(), e.getR34_amt_name_of_sub5(),
				e.getR34_tot_amt(), e.getR36_item(), e.getR36_amt_name_of_sub1(), e.getR36_amt_name_of_sub2(),
				e.getR36_amt_name_of_sub3(), e.getR36_amt_name_of_sub4(), e.getR36_amt_name_of_sub5(),
				e.getR36_tot_amt(), e.getR37_item(), e.getR37_amt_name_of_sub1(), e.getR37_amt_name_of_sub2(),
				e.getR37_amt_name_of_sub3(), e.getR37_amt_name_of_sub4(), e.getR37_amt_name_of_sub5(),
				e.getR37_tot_amt(), e.getR38_item(), e.getR38_amt_name_of_sub1(), e.getR38_amt_name_of_sub2(),
				e.getR38_amt_name_of_sub3(), e.getR38_amt_name_of_sub4(), e.getR38_amt_name_of_sub5(),
				e.getR38_tot_amt(), e.getR39_item(), e.getR39_amt_name_of_sub1(), e.getR39_amt_name_of_sub2(),
				e.getR39_amt_name_of_sub3(), e.getR39_amt_name_of_sub4(), e.getR39_amt_name_of_sub5(),
				e.getR39_tot_amt(), e.getR40_item(), e.getR40_amt_name_of_sub1(), e.getR40_amt_name_of_sub2(),
				e.getR40_amt_name_of_sub3(), e.getR40_amt_name_of_sub4(), e.getR40_amt_name_of_sub5(),
				e.getR40_tot_amt(), e.getR41_item(), e.getR41_amt_name_of_sub1(), e.getR41_amt_name_of_sub2(),
				e.getR41_amt_name_of_sub3(), e.getR41_amt_name_of_sub4(), e.getR41_amt_name_of_sub5(),
				e.getR41_tot_amt(), e.getR42_item(), e.getR42_amt_name_of_sub1(), e.getR42_amt_name_of_sub2(),
				e.getR42_amt_name_of_sub3(), e.getR42_amt_name_of_sub4(), e.getR42_amt_name_of_sub5(),
				e.getR42_tot_amt(), e.getR43_item(), e.getR43_amt_name_of_sub1(), e.getR43_amt_name_of_sub2(),
				e.getR43_amt_name_of_sub3(), e.getR43_amt_name_of_sub4(), e.getR43_amt_name_of_sub5(),
				e.getR43_tot_amt(), e.getR44_item(), e.getR44_amt_name_of_sub1(), e.getR44_amt_name_of_sub2(),
				e.getR44_amt_name_of_sub3(), e.getR44_amt_name_of_sub4(), e.getR44_amt_name_of_sub5(),
				e.getR44_tot_amt(), e.getR45_item(), e.getR45_amt_name_of_sub1(), e.getR45_amt_name_of_sub2(),
				e.getR45_amt_name_of_sub3(), e.getR45_amt_name_of_sub4(), e.getR45_amt_name_of_sub5(),
				e.getR45_tot_amt(), e.getR46_item(), e.getR46_amt_name_of_sub1(), e.getR46_amt_name_of_sub2(),
				e.getR46_amt_name_of_sub3(), e.getR46_amt_name_of_sub4(), e.getR46_amt_name_of_sub5(),
				e.getR46_tot_amt(), e.getR47_item(), e.getR47_amt_name_of_sub1(), e.getR47_amt_name_of_sub2(),
				e.getR47_amt_name_of_sub3(), e.getR47_amt_name_of_sub4(), e.getR47_amt_name_of_sub5(),
				e.getR47_tot_amt(), e.getR48_item(), e.getR48_amt_name_of_sub1(), e.getR48_amt_name_of_sub2(),
				e.getR48_amt_name_of_sub3(), e.getR48_amt_name_of_sub4(), e.getR48_amt_name_of_sub5(),
				e.getR48_tot_amt(), e.getR49_item(), e.getR49_amt_name_of_sub1(), e.getR49_amt_name_of_sub2(),
				e.getR49_amt_name_of_sub3(), e.getR49_amt_name_of_sub4(), e.getR49_amt_name_of_sub5(),
				e.getR49_tot_amt(), e.getR50_item(), e.getR50_amt_name_of_sub1(), e.getR50_amt_name_of_sub2(),
				e.getR50_amt_name_of_sub3(), e.getR50_amt_name_of_sub4(), e.getR50_amt_name_of_sub5(),
				e.getR50_tot_amt(), e.getR51_item(), e.getR51_amt_name_of_sub1(), e.getR51_amt_name_of_sub2(),
				e.getR51_amt_name_of_sub3(), e.getR51_amt_name_of_sub4(), e.getR51_amt_name_of_sub5(),
				e.getR51_tot_amt(), e.getR52_item(), e.getR52_amt_name_of_sub1(), e.getR52_amt_name_of_sub2(),
				e.getR52_amt_name_of_sub3(), e.getR52_amt_name_of_sub4(), e.getR52_amt_name_of_sub5(),
				e.getR52_tot_amt(), e.getR53_item(), e.getR53_amt_name_of_sub1(), e.getR53_amt_name_of_sub2(),
				e.getR53_amt_name_of_sub3(), e.getR53_amt_name_of_sub4(), e.getR53_amt_name_of_sub5(),
				e.getR53_tot_amt(), e.getR54_item(), e.getR54_amt_name_of_sub1(), e.getR54_amt_name_of_sub2(),
				e.getR54_amt_name_of_sub3(), e.getR54_amt_name_of_sub4(), e.getR54_amt_name_of_sub5(),
				e.getR54_tot_amt(), e.getR55_item(), e.getR55_amt_name_of_sub1(), e.getR55_amt_name_of_sub2(),
				e.getR55_amt_name_of_sub3(), e.getR55_amt_name_of_sub4(), e.getR55_amt_name_of_sub5(),
				e.getR55_tot_amt(), e.getR56_item(), e.getR56_amt_name_of_sub1(), e.getR56_amt_name_of_sub2(),
				e.getR56_amt_name_of_sub3(), e.getR56_amt_name_of_sub4(), e.getR56_amt_name_of_sub5(),
				e.getR56_tot_amt(), e.getR57_item(), e.getR57_amt_name_of_sub1(), e.getR57_amt_name_of_sub2(),
				e.getR57_amt_name_of_sub3(), e.getR57_amt_name_of_sub4(), e.getR57_amt_name_of_sub5(),
				e.getR57_tot_amt(), e.getR58_item(), e.getR58_amt_name_of_sub1(), e.getR58_amt_name_of_sub2(),
				e.getR58_amt_name_of_sub3(), e.getR58_amt_name_of_sub4(), e.getR58_amt_name_of_sub5(),
				e.getR58_tot_amt() };
	}

	private Object[] rFieldValues(M_CA4_Archival_Detail_Entity e) {
		return new Object[] { e.getR10_item(), e.getR10_amt_name_of_sub1(), e.getR10_amt_name_of_sub2(),
				e.getR10_amt_name_of_sub3(), e.getR10_amt_name_of_sub4(), e.getR10_amt_name_of_sub5(),
				e.getR10_tot_amt(), e.getR11_item(), e.getR11_amt_name_of_sub1(), e.getR11_amt_name_of_sub2(),
				e.getR11_amt_name_of_sub3(), e.getR11_amt_name_of_sub4(), e.getR11_amt_name_of_sub5(),
				e.getR11_tot_amt(), e.getR12_item(), e.getR12_amt_name_of_sub1(), e.getR12_amt_name_of_sub2(),
				e.getR12_amt_name_of_sub3(), e.getR12_amt_name_of_sub4(), e.getR12_amt_name_of_sub5(),
				e.getR12_tot_amt(), e.getR13_item(), e.getR13_amt_name_of_sub1(), e.getR13_amt_name_of_sub2(),
				e.getR13_amt_name_of_sub3(), e.getR13_amt_name_of_sub4(), e.getR13_amt_name_of_sub5(),
				e.getR13_tot_amt(), e.getR14_item(), e.getR14_amt_name_of_sub1(), e.getR14_amt_name_of_sub2(),
				e.getR14_amt_name_of_sub3(), e.getR14_amt_name_of_sub4(), e.getR14_amt_name_of_sub5(),
				e.getR14_tot_amt(), e.getR17_item(), e.getR17_amt_name_of_sub1(), e.getR17_amt_name_of_sub2(),
				e.getR17_amt_name_of_sub3(), e.getR17_amt_name_of_sub4(), e.getR17_amt_name_of_sub5(),
				e.getR17_tot_amt(), e.getR18_item(), e.getR18_amt_name_of_sub1(), e.getR18_amt_name_of_sub2(),
				e.getR18_amt_name_of_sub3(), e.getR18_amt_name_of_sub4(), e.getR18_amt_name_of_sub5(),
				e.getR18_tot_amt(), e.getR19_item(), e.getR19_amt_name_of_sub1(), e.getR19_amt_name_of_sub2(),
				e.getR19_amt_name_of_sub3(), e.getR19_amt_name_of_sub4(), e.getR19_amt_name_of_sub5(),
				e.getR19_tot_amt(), e.getR22_item(), e.getR22_amt_name_of_sub1(), e.getR22_amt_name_of_sub2(),
				e.getR22_amt_name_of_sub3(), e.getR22_amt_name_of_sub4(), e.getR22_amt_name_of_sub5(),
				e.getR22_tot_amt(), e.getR23_item(), e.getR23_amt_name_of_sub1(), e.getR23_amt_name_of_sub2(),
				e.getR23_amt_name_of_sub3(), e.getR23_amt_name_of_sub4(), e.getR23_amt_name_of_sub5(),
				e.getR23_tot_amt(), e.getR24_item(), e.getR24_amt_name_of_sub1(), e.getR24_amt_name_of_sub2(),
				e.getR24_amt_name_of_sub3(), e.getR24_amt_name_of_sub4(), e.getR24_amt_name_of_sub5(),
				e.getR24_tot_amt(), e.getR27_item(), e.getR27_amt_name_of_sub1(), e.getR27_amt_name_of_sub2(),
				e.getR27_amt_name_of_sub3(), e.getR27_amt_name_of_sub4(), e.getR27_amt_name_of_sub5(),
				e.getR27_tot_amt(), e.getR28_item(), e.getR28_amt_name_of_sub1(), e.getR28_amt_name_of_sub2(),
				e.getR28_amt_name_of_sub3(), e.getR28_amt_name_of_sub4(), e.getR28_amt_name_of_sub5(),
				e.getR28_tot_amt(), e.getR29_item(), e.getR29_amt_name_of_sub1(), e.getR29_amt_name_of_sub2(),
				e.getR29_amt_name_of_sub3(), e.getR29_amt_name_of_sub4(), e.getR29_amt_name_of_sub5(),
				e.getR29_tot_amt(), e.getR32_item(), e.getR32_amt_name_of_sub1(), e.getR32_amt_name_of_sub2(),
				e.getR32_amt_name_of_sub3(), e.getR32_amt_name_of_sub4(), e.getR32_amt_name_of_sub5(),
				e.getR32_tot_amt(), e.getR33_item(), e.getR33_amt_name_of_sub1(), e.getR33_amt_name_of_sub2(),
				e.getR33_amt_name_of_sub3(), e.getR33_amt_name_of_sub4(), e.getR33_amt_name_of_sub5(),
				e.getR33_tot_amt(), e.getR34_item(), e.getR34_amt_name_of_sub1(), e.getR34_amt_name_of_sub2(),
				e.getR34_amt_name_of_sub3(), e.getR34_amt_name_of_sub4(), e.getR34_amt_name_of_sub5(),
				e.getR34_tot_amt(), e.getR36_item(), e.getR36_amt_name_of_sub1(), e.getR36_amt_name_of_sub2(),
				e.getR36_amt_name_of_sub3(), e.getR36_amt_name_of_sub4(), e.getR36_amt_name_of_sub5(),
				e.getR36_tot_amt(), e.getR37_item(), e.getR37_amt_name_of_sub1(), e.getR37_amt_name_of_sub2(),
				e.getR37_amt_name_of_sub3(), e.getR37_amt_name_of_sub4(), e.getR37_amt_name_of_sub5(),
				e.getR37_tot_amt(), e.getR38_item(), e.getR38_amt_name_of_sub1(), e.getR38_amt_name_of_sub2(),
				e.getR38_amt_name_of_sub3(), e.getR38_amt_name_of_sub4(), e.getR38_amt_name_of_sub5(),
				e.getR38_tot_amt(), e.getR39_item(), e.getR39_amt_name_of_sub1(), e.getR39_amt_name_of_sub2(),
				e.getR39_amt_name_of_sub3(), e.getR39_amt_name_of_sub4(), e.getR39_amt_name_of_sub5(),
				e.getR39_tot_amt(), e.getR40_item(), e.getR40_amt_name_of_sub1(), e.getR40_amt_name_of_sub2(),
				e.getR40_amt_name_of_sub3(), e.getR40_amt_name_of_sub4(), e.getR40_amt_name_of_sub5(),
				e.getR40_tot_amt(), e.getR41_item(), e.getR41_amt_name_of_sub1(), e.getR41_amt_name_of_sub2(),
				e.getR41_amt_name_of_sub3(), e.getR41_amt_name_of_sub4(), e.getR41_amt_name_of_sub5(),
				e.getR41_tot_amt(), e.getR42_item(), e.getR42_amt_name_of_sub1(), e.getR42_amt_name_of_sub2(),
				e.getR42_amt_name_of_sub3(), e.getR42_amt_name_of_sub4(), e.getR42_amt_name_of_sub5(),
				e.getR42_tot_amt(), e.getR43_item(), e.getR43_amt_name_of_sub1(), e.getR43_amt_name_of_sub2(),
				e.getR43_amt_name_of_sub3(), e.getR43_amt_name_of_sub4(), e.getR43_amt_name_of_sub5(),
				e.getR43_tot_amt(), e.getR44_item(), e.getR44_amt_name_of_sub1(), e.getR44_amt_name_of_sub2(),
				e.getR44_amt_name_of_sub3(), e.getR44_amt_name_of_sub4(), e.getR44_amt_name_of_sub5(),
				e.getR44_tot_amt(), e.getR45_item(), e.getR45_amt_name_of_sub1(), e.getR45_amt_name_of_sub2(),
				e.getR45_amt_name_of_sub3(), e.getR45_amt_name_of_sub4(), e.getR45_amt_name_of_sub5(),
				e.getR45_tot_amt(), e.getR46_item(), e.getR46_amt_name_of_sub1(), e.getR46_amt_name_of_sub2(),
				e.getR46_amt_name_of_sub3(), e.getR46_amt_name_of_sub4(), e.getR46_amt_name_of_sub5(),
				e.getR46_tot_amt(), e.getR47_item(), e.getR47_amt_name_of_sub1(), e.getR47_amt_name_of_sub2(),
				e.getR47_amt_name_of_sub3(), e.getR47_amt_name_of_sub4(), e.getR47_amt_name_of_sub5(),
				e.getR47_tot_amt(), e.getR48_item(), e.getR48_amt_name_of_sub1(), e.getR48_amt_name_of_sub2(),
				e.getR48_amt_name_of_sub3(), e.getR48_amt_name_of_sub4(), e.getR48_amt_name_of_sub5(),
				e.getR48_tot_amt(), e.getR49_item(), e.getR49_amt_name_of_sub1(), e.getR49_amt_name_of_sub2(),
				e.getR49_amt_name_of_sub3(), e.getR49_amt_name_of_sub4(), e.getR49_amt_name_of_sub5(),
				e.getR49_tot_amt(), e.getR50_item(), e.getR50_amt_name_of_sub1(), e.getR50_amt_name_of_sub2(),
				e.getR50_amt_name_of_sub3(), e.getR50_amt_name_of_sub4(), e.getR50_amt_name_of_sub5(),
				e.getR50_tot_amt(), e.getR51_item(), e.getR51_amt_name_of_sub1(), e.getR51_amt_name_of_sub2(),
				e.getR51_amt_name_of_sub3(), e.getR51_amt_name_of_sub4(), e.getR51_amt_name_of_sub5(),
				e.getR51_tot_amt(), e.getR52_item(), e.getR52_amt_name_of_sub1(), e.getR52_amt_name_of_sub2(),
				e.getR52_amt_name_of_sub3(), e.getR52_amt_name_of_sub4(), e.getR52_amt_name_of_sub5(),
				e.getR52_tot_amt(), e.getR53_item(), e.getR53_amt_name_of_sub1(), e.getR53_amt_name_of_sub2(),
				e.getR53_amt_name_of_sub3(), e.getR53_amt_name_of_sub4(), e.getR53_amt_name_of_sub5(),
				e.getR53_tot_amt(), e.getR54_item(), e.getR54_amt_name_of_sub1(), e.getR54_amt_name_of_sub2(),
				e.getR54_amt_name_of_sub3(), e.getR54_amt_name_of_sub4(), e.getR54_amt_name_of_sub5(),
				e.getR54_tot_amt(), e.getR55_item(), e.getR55_amt_name_of_sub1(), e.getR55_amt_name_of_sub2(),
				e.getR55_amt_name_of_sub3(), e.getR55_amt_name_of_sub4(), e.getR55_amt_name_of_sub5(),
				e.getR55_tot_amt(), e.getR56_item(), e.getR56_amt_name_of_sub1(), e.getR56_amt_name_of_sub2(),
				e.getR56_amt_name_of_sub3(), e.getR56_amt_name_of_sub4(), e.getR56_amt_name_of_sub5(),
				e.getR56_tot_amt(), e.getR57_item(), e.getR57_amt_name_of_sub1(), e.getR57_amt_name_of_sub2(),
				e.getR57_amt_name_of_sub3(), e.getR57_amt_name_of_sub4(), e.getR57_amt_name_of_sub5(),
				e.getR57_tot_amt(), e.getR58_item(), e.getR58_amt_name_of_sub1(), e.getR58_amt_name_of_sub2(),
				e.getR58_amt_name_of_sub3(), e.getR58_amt_name_of_sub4(), e.getR58_amt_name_of_sub5(),
				e.getR58_tot_amt() };
	}

	private void saveSummary(M_CA4_Summary_Entity e) {
		Object[] rVals = rFieldValues(e);
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
		jdbcTemplate.update("UPDATE BRRS_M_CA4_SUMMARYTABLE SET " + R_FIELDS_SET
				+ ",REPORT_VERSION=?,REPORT_FREQUENCY=?,REPORT_CODE=?,REPORT_DESC=?,ENTITY_FLG=?,MODIFY_FLG=?,DEL_FLG=?"
				+ " WHERE REPORT_DATE=?", params);
	}

	private void insertResubSummary(M_CA4_Resub_Summary_Entity e) {
		Object[] rVals = rFieldValues(e);
		Object[] params = new Object[rVals.length + 9];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length] = e.getReport_date();
		params[rVals.length + 1] = e.getReport_version();
		params[rVals.length + 2] = e.getReportResubDate();
		params[rVals.length + 3] = e.getReport_frequency();
		params[rVals.length + 4] = e.getReport_code();
		params[rVals.length + 5] = e.getReport_desc();
		params[rVals.length + 6] = e.getEntity_flg();
		params[rVals.length + 7] = e.getModify_flg();
		params[rVals.length + 8] = e.getDel_flg();
		jdbcTemplate.update("INSERT INTO BRRS_M_CA4_RESUB_SUMMARYTABLE (" + R_COLS
				+ ",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC,ENTITY_FLG,MODIFY_FLG,DEL_FLG) VALUES ("
				+ R_PLACEHOLDERS + ",?,?,?,?,?,?,?,?,?)", params);
	}

	private void insertResubDetail(M_CA4_Resub_Detail_Entity e) {
		Object[] rVals = rFieldValues(e);
		Object[] params = new Object[rVals.length + 9];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length] = e.getReport_date();
		params[rVals.length + 1] = e.getReport_version();
		params[rVals.length + 2] = e.getReportResubDate();
		params[rVals.length + 3] = e.getReport_frequency();
		params[rVals.length + 4] = e.getReport_code();
		params[rVals.length + 5] = e.getReport_desc();
		params[rVals.length + 6] = e.getEntity_flg();
		params[rVals.length + 7] = e.getModify_flg();
		params[rVals.length + 8] = e.getDel_flg();
		jdbcTemplate.update("INSERT INTO BRRS_M_CA4_RESUB_DETAILTABLE (" + R_COLS
				+ ",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC,ENTITY_FLG,MODIFY_FLG,DEL_FLG) VALUES ("
				+ R_PLACEHOLDERS + ",?,?,?,?,?,?,?,?,?)", params);
	}

	private void insertArchivalSummary(M_CA4_Archival_Summary_Entity e) {
		Object[] rVals = rFieldValues(e);
		Object[] params = new Object[rVals.length + 9];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length] = e.getReport_date();
		params[rVals.length + 1] = e.getReport_version();
		params[rVals.length + 2] = e.getReportResubDate();
		params[rVals.length + 3] = e.getReport_frequency();
		params[rVals.length + 4] = e.getReport_code();
		params[rVals.length + 5] = e.getReport_desc();
		params[rVals.length + 6] = e.getEntity_flg();
		params[rVals.length + 7] = e.getModify_flg();
		params[rVals.length + 8] = e.getDel_flg();
		jdbcTemplate.update("INSERT INTO BRRS_M_CA4_ARCHIVALTABLE_SUMMARY (" + R_COLS
				+ ",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC,ENTITY_FLG,MODIFY_FLG,DEL_FLG) VALUES ("
				+ R_PLACEHOLDERS + ",?,?,?,?,?,?,?,?,?)", params);
	}

	private void insertArchivalDetail(M_CA4_Archival_Detail_Entity e) {
		Object[] rVals = rFieldValues(e);
		Object[] params = new Object[rVals.length + 9];
		System.arraycopy(rVals, 0, params, 0, rVals.length);
		params[rVals.length] = e.getReport_date();
		params[rVals.length + 1] = e.getReport_version();
		params[rVals.length + 2] = e.getReportResubDate();
		params[rVals.length + 3] = e.getReport_frequency();
		params[rVals.length + 4] = e.getReport_code();
		params[rVals.length + 5] = e.getReport_desc();
		params[rVals.length + 6] = e.getEntity_flg();
		params[rVals.length + 7] = e.getModify_flg();
		params[rVals.length + 8] = e.getDel_flg();
		jdbcTemplate.update("INSERT INTO BRRS_M_CA4_ARCHIVALTABLE_DETAIL (" + R_COLS
				+ ",REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,REPORT_FREQUENCY,REPORT_CODE,REPORT_DESC,ENTITY_FLG,MODIFY_FLG,DEL_FLG) VALUES ("
				+ R_PLACEHOLDERS + ",?,?,?,?,?,?,?,?,?)", params);
	}

	public ModelAndView getBRRS_M_CA4View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

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
				List<M_CA4_Archival_Summary_Entity> T1Master = getArchivalSummaryByDateAndVersion(d1, version);
				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_CA4_Resub_Summary_Entity> T1Master = getResubSummaryByDateAndVersion(d1, version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<M_CA4_Summary_Entity> T1Master = getSummaryByDate(d1);
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_CA4_Archival_Detail_Entity> T1Master = getArchivalDetailByDateAndVersion(d1, version);
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_CA4_Resub_Detail_Entity> T1Master = getResubDetailByDateAndVersion(d1, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_CA4_Detail_Entity> T1Master = getDetailByDate(d1);
					System.out.println("Details......T1Master Size " + T1Master.size());
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_CA4");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	public void updateReport(M_CA4_Summary_Entity updatedEntity) {
		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		List<M_CA4_Summary_Entity> existingList = getSummaryByDate(updatedEntity.getReport_date());
		if (existingList.isEmpty())
			throw new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReport_date());
		M_CA4_Summary_Entity existing = existingList.get(0);

		// Audit old copy
		M_CA4_Summary_Entity oldcopy = new M_CA4_Summary_Entity();
		BeanUtils.copyProperties(existing, oldcopy);

		try {
			// 1️⃣ Loop from R10 to R58 and copy fields

			for (int i = 10; i <= 58; i++) {

				String prefix = "R" + i + "_";

				String[] fields = { "item", "amt_name_of_sub1", "amt_name_of_sub2", "amt_name_of_sub3",
						"amt_name_of_sub4", "amt_name_of_sub5", "tot_amt" };

				for (String field : fields) {
					String getterName = "get" + prefix + field; // e.g., getR10_item
					String setterName = "set" + prefix + field; // e.g., setR10_item

					try {
						Method getter = M_CA4_Summary_Entity.class.getMethod(getterName);
						Method setter = M_CA4_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

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
		String changes = auditService.getChanges(oldcopy, existing);

		if (!changes.isEmpty()) {

			saveSummary(existing);

			auditService.compareEntitiesmanual(oldcopy, existing, updatedEntity.getReport_date().toString(),
					"M CA4 Summary Screen", "BRRS_M_CA4_SUMMARY");
		}
	}

	public void updateResubReport(M_CA4_Resub_Summary_Entity updatedEntity) {

		Date reportDate = updatedEntity.getReport_date();

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

		M_CA4_Resub_Summary_Entity resubSummary = new M_CA4_Resub_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "reportResubDate");

		resubSummary.setReport_date(reportDate);
		resubSummary.setReport_version(newVersion);
		resubSummary.setReportResubDate(now);

		// ====================================================
		// 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
		// ====================================================

		M_CA4_Resub_Detail_Entity resubDetail = new M_CA4_Resub_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "reportResubDate");

		resubDetail.setReport_date(reportDate);
		resubDetail.setReport_version(newVersion);
		resubDetail.setReportResubDate(now);

		// ====================================================
		// 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
		// ====================================================

		M_CA4_Archival_Summary_Entity archSummary = new M_CA4_Archival_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "reportResubDate");

		archSummary.setReport_date(reportDate);
		archSummary.setReport_version(newVersion); // SAME VERSION
		archSummary.setReportResubDate(now);

		// ====================================================
		// 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
		// ====================================================

		M_CA4_Archival_Detail_Entity archDetail = new M_CA4_Archival_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, archDetail, "reportDate", "reportVersion", "reportResubDate");

		archDetail.setReport_date(reportDate);
		archDetail.setReport_version(newVersion); // SAME VERSION
		archDetail.setReportResubDate(now);

		// ====================================================
		// 6️⃣ SAVE ALL WITH SAME DATA
		// ====================================================

		insertResubSummary(resubSummary);
		insertResubDetail(resubDetail);

		insertArchivalSummary(archSummary);
		insertArchivalDetail(archDetail);
	}

	public List<Object[]> getM_CA4Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_CA4_Resub_Summary_Entity> latestArchivalList = getResubSummaryWithVersionAll();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_CA4_Resub_Summary_Entity entity : latestArchivalList) {
					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() };
					resubList.add(row);
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}
		} catch (Exception e) {
			System.err.println("Error fetching M_CA4 Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	// Archival View
	public List<Object[]> getM_CA4Archival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_CA4_Archival_Summary_Entity> repoData = getArchivalSummaryWithVersionAll();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_CA4_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_CA4_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReport_version());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  M_CA4  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Normal format Excel

	public byte[] getBRRS_M_CA4Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		System.out.println("======= DOWNLOAD DETAILS =======");
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
				return getExcelM_CA4ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_CA4ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_CA4EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				List<M_CA4_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_CA4 report. Returning empty result.");
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
						SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");

						SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

						Date reportDateValue = inputFormat.parse(todate);

						// Set formatted date
						dateCell.setCellValue(outputFormat.format(reportDateValue));

						dateCell.setCellStyle(textStyle);

					} catch (ParseException e) {

						logger.error("Error parsing todate: {}", todate, e);
					}

					int startRow = 9;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_CA4_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

// row10

							// Column 2 - name of sub1
							Cell cellC = row.createCell(2);
							if (record.getR10_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR10_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							Cell cellD = row.createCell(3);
							if (record.getR10_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR10_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							Cell cellE = row.createCell(4);
							if (record.getR10_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR10_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							Cell cellF = row.createCell(5);
							if (record.getR10_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR10_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub4
							Cell cellG = row.createCell(6);
							if (record.getR10_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR10_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR10_tot_amt() != null) {
								cellG.setCellValue(record.getR10_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// row11
							row = sheet.getRow(10);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR11_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR11_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR11_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR11_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR11_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR11_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR11_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR11_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR11_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR11_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR11_tot_amt() != null) {
								cellG.setCellValue(record.getR11_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// row13
							row = sheet.getRow(12);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR13_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR13_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR13_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR13_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR13_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR13_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR13_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR13_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR13_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR13_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR13_tot_amt() != null) {
								cellG.setCellValue(record.getR13_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// row14
							row = sheet.getRow(13);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR14_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR14_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR14_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR14_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR14_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR14_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR14_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR14_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR14_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR14_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR14_tot_amt() != null) {
								cellG.setCellValue(record.getR14_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// row17
							row = sheet.getRow(16);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR17_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR17_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR17_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR17_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR17_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR17_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR17_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR17_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR17_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR17_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							/*
							 * // Column 2 - name of sub1 cellC = row.createCell(2); if
							 * (record.getR17_amt_name_of_sub1() != null) {
							 * cellC.setCellValue(record.getR17_amt_name_of_sub1().doubleValue());
							 * cellC.setCellStyle(numberStyle); } else { cellC.setCellValue("");
							 * cellC.setCellStyle(textStyle); }
							 * 
							 * // Column 3 - name of sub2 cellD = row.createCell(3); if
							 * (record.getR17_amt_name_of_sub2() != null) {
							 * cellD.setCellValue(record.getR17_amt_name_of_sub2().doubleValue());
							 * cellD.setCellStyle(numberStyle); } else { cellD.setCellValue("");
							 * cellD.setCellStyle(textStyle); }
							 * 
							 * // Column 4 - name of sub3 cellE = row.createCell(4); if
							 * (record.getR17_amt_name_of_sub3() != null) {
							 * cellE.setCellValue(record.getR17_amt_name_of_sub3().doubleValue());
							 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
							 * cellE.setCellStyle(textStyle); }
							 * 
							 * // Column 5 - name of sub4 cellF = row.createCell(5); if
							 * (record.getR17_amt_name_of_sub4() != null) {
							 * cellF.setCellValue(record.getR17_amt_name_of_sub4().doubleValue());
							 * cellF.setCellStyle(numberStyle); } else { cellF.setCellValue("");
							 * cellF.setCellStyle(textStyle); }
							 * 
							 * // Column 6 - name of sub5 cellG = row.createCell(6); if
							 * (record.getR17_amt_name_of_sub5() != null) {
							 * cellG.setCellValue(record.getR17_amt_name_of_sub5().doubleValue());
							 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
							 * cellG.setCellStyle(textStyle); }
							 */
							// COLUMN TOTAL
							cellG = row.getCell(7);
							if (cellG == null) {
								cellG = row.createCell(7);
							}

							if (record.getR17_tot_amt() != null) {
								cellG.setCellValue(record.getR17_tot_amt().doubleValue());
							} else {
								cellG.setCellValue("");
							}

							// row18
							row = sheet.getRow(17);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR18_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR18_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR18_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR18_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR18_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR18_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR18_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR18_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR18_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR18_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR18_tot_amt() != null) {
								cellG.setCellValue(record.getR18_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// row19
							row = sheet.getRow(18);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR19_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR19_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR19_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR19_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR19_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR19_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR19_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR19_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR19_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR19_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR19_tot_amt() != null) {
								cellG.setCellValue(record.getR19_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// row22
							row = sheet.getRow(21);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR22_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR22_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR22_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR22_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR22_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR22_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR22_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR22_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR22_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR22_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							/*
							 * // Column 3 - name of sub1 cellC = row.createCell(2); if
							 * (record.getR22_amt_name_of_sub1() != null) {
							 * cellC.setCellValue(record.getR22_amt_name_of_sub1().doubleValue());
							 * cellC.setCellStyle(numberStyle); } else { cellC.setCellValue("");
							 * cellC.setCellStyle(textStyle); }
							 * 
							 * // Column 4 - name of sub2 cellD = row.createCell(3); if
							 * (record.getR22_amt_name_of_sub2() != null) {
							 * cellD.setCellValue(record.getR22_amt_name_of_sub2().doubleValue());
							 * cellD.setCellStyle(numberStyle); } else { cellD.setCellValue("");
							 * cellD.setCellStyle(textStyle); }
							 * 
							 * // Column 5 - name of sub3 cellE = row.createCell(4); if
							 * (record.getR22_amt_name_of_sub3() != null) {
							 * cellE.setCellValue(record.getR22_amt_name_of_sub3().doubleValue());
							 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
							 * cellE.setCellStyle(textStyle); }
							 * 
							 * // Column 6 - name of sub4 cellF = row.createCell(5); if
							 * (record.getR22_amt_name_of_sub4() != null) {
							 * cellF.setCellValue(record.getR22_amt_name_of_sub4().doubleValue());
							 * cellF.setCellStyle(numberStyle); } else { cellF.setCellValue("");
							 * cellF.setCellStyle(textStyle); }
							 * 
							 * // Column 7 - name of sub5 cellG = row.createCell(6); if
							 * (record.getR22_amt_name_of_sub5() != null) {
							 * cellG.setCellValue(record.getR22_amt_name_of_sub5().doubleValue());
							 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
							 * cellG.setCellStyle(textStyle); }
							 */

							// COLUMN TOTAL
							cellG = row.getCell(7);
							if (cellG == null) {
								cellG = row.createCell(7);
							}

							if (record.getR22_tot_amt() != null) {
								cellG.setCellValue(record.getR22_tot_amt().doubleValue());
							} else {
								cellG.setCellValue("");
							}

							// row23
							row = sheet.getRow(22);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR23_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR23_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR23_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR23_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR23_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR23_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR23_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR23_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR23_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR23_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR23_tot_amt() != null) {
								cellG.setCellValue(record.getR23_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// row24
							row = sheet.getRow(23);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR24_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR24_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR24_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR24_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR24_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR24_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR24_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR24_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR24_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR24_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR24_tot_amt() != null) {
								cellG.setCellValue(record.getR24_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// row27
							row = sheet.getRow(26);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR27_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR27_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR27_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR27_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR27_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR27_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR27_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR27_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR27_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR27_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}
							// COLUMN TOTAL
							cellG = row.getCell(7);
							if (cellG == null) {
								cellG = row.createCell(7);
							}

							if (record.getR27_tot_amt() != null) {
								cellG.setCellValue(record.getR27_tot_amt().doubleValue());
							} else {
								cellG.setCellValue("");
							}

							// row28
							row = sheet.getRow(27);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR28_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR28_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR28_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR28_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR28_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR28_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR28_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR28_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR28_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR28_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR28_tot_amt() != null) {
								cellG.setCellValue(record.getR28_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// row29
							row = sheet.getRow(28);
							// Column 1 - item

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR29_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR29_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR29_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR29_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR29_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR29_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR29_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR29_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR29_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR29_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR29_tot_amt() != null) {
								cellG.setCellValue(record.getR29_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// row32
							row = sheet.getRow(31);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR32_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR32_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR32_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR32_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR32_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR32_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR32_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR32_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR32_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR32_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}
							// COLUMN TOTAL
							cellG = row.getCell(7);
							if (cellG == null) {
								cellG = row.createCell(7);
							}

							if (record.getR32_tot_amt() != null) {
								cellG.setCellValue(record.getR32_tot_amt().doubleValue());
							} else {
								cellG.setCellValue("");
							}

							// row33
							row = sheet.getRow(32);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR33_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR33_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR33_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR33_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR33_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR33_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR33_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR33_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR33_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR33_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR33_tot_amt() != null) {
								cellG.setCellValue(record.getR33_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							/*
							 * // row33 row = sheet.getRow(32);
							 * 
							 * 
							 * // Column 2 - name of sub1 cellC = row.createCell(2); if
							 * (record.getR33_amt_name_of_sub1() != null) {
							 * cellC.setCellValue(record.getR33_amt_name_of_sub1().doubleValue());
							 * cellC.setCellStyle(numberStyle); } else { cellC.setCellValue("");
							 * cellC.setCellStyle(textStyle); }
							 * 
							 * // Column 3 - name of sub2 cellD = row.createCell(3); if
							 * (record.getR33_amt_name_of_sub2() != null) {
							 * cellD.setCellValue(record.getR33_amt_name_of_sub2().doubleValue());
							 * cellD.setCellStyle(numberStyle); } else { cellD.setCellValue("");
							 * cellD.setCellStyle(textStyle); }
							 * 
							 * // Column 4 - name of sub3 cellE = row.createCell(4); if
							 * (record.getR33_amt_name_of_sub3() != null) {
							 * cellE.setCellValue(record.getR33_amt_name_of_sub3().doubleValue());
							 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
							 * cellE.setCellStyle(textStyle); }
							 * 
							 * // Column 5 - name of sub4 cellF = row.createCell(5); if
							 * (record.getR33_amt_name_of_sub4() != null) {
							 * cellF.setCellValue(record.getR33_amt_name_of_sub4().doubleValue());
							 * cellF.setCellStyle(numberStyle); } else { cellF.setCellValue("");
							 * cellF.setCellStyle(textStyle); }
							 * 
							 * // Column 6 - name of sub5 cellG = row.createCell(6); if
							 * (record.getR33_amt_name_of_sub5() != null) {
							 * cellG.setCellValue(record.getR33_amt_name_of_sub5().doubleValue());
							 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
							 * cellG.setCellStyle(textStyle); }
							 */

							// row34
							row = sheet.getRow(33);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR34_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR34_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR34_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR34_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR34_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR34_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR34_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR34_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR34_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR34_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR34_tot_amt() != null) {
								cellG.setCellValue(record.getR34_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// ====================== R36 ======================
							// row36
							row = sheet.getRow(35);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR36_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR36_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR36_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR36_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR36_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR36_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR36_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR36_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR36_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR36_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							// ====================== R37 ======================
							row = sheet.getRow(36);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR37_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR37_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR37_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR37_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR37_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR37_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR37_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR37_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR37_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR37_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR37_tot_amt() != null) {
								cellG.setCellValue(record.getR37_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// ====================== R38 ======================
							row = sheet.getRow(37);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR38_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR38_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR38_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR38_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR38_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR38_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR38_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR38_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR38_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR38_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}
							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR38_tot_amt() != null) {
								cellG.setCellValue(record.getR38_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// ====================== R39 ======================
							// row39
							row = sheet.getRow(38);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR39_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR39_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR39_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR39_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR39_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR39_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR39_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR39_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR39_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR39_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							// ====================== R40 ======================
							row = sheet.getRow(39);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR40_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR40_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR40_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR40_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR40_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR40_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR40_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR40_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR40_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR40_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}
							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR40_tot_amt() != null) {
								cellG.setCellValue(record.getR40_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// =========================
							// Row for R41
							// =========================
							row = sheet.getRow(40);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR41_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR41_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR41_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR41_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR41_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR41_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR41_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR41_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR41_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR41_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR41_tot_amt() != null) {
								cellG.setCellValue(record.getR41_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// =========================
							// Row for R42
							// =========================
							row = sheet.getRow(41);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR42_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR42_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR42_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR42_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR42_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR42_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR42_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR42_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR42_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR42_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}
							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR42_tot_amt() != null) {
								cellG.setCellValue(record.getR42_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// =========================
							// Row for R43
							// =========================
							row = sheet.getRow(42);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR43_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR43_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR43_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR43_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR43_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR43_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR43_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR43_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR43_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR43_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							/*
							 * // Column 2 - name of sub1 cellC = row.createCell(2); if
							 * (record.getR43_amt_name_of_sub1() != null) {
							 * cellC.setCellValue(record.getR43_amt_name_of_sub1().doubleValue());
							 * cellC.setCellStyle(numberStyle); } else { cellC.setCellValue("");
							 * cellC.setCellStyle(textStyle); }
							 * 
							 * // Column 3 - name of sub2 cellD = row.createCell(3); if
							 * (record.getR43_amt_name_of_sub2() != null) {
							 * cellD.setCellValue(record.getR43_amt_name_of_sub2().doubleValue());
							 * cellD.setCellStyle(numberStyle); } else { cellD.setCellValue("");
							 * cellD.setCellStyle(textStyle); }
							 * 
							 * // Column 4 - name of sub3 cellE = row.createCell(4); if
							 * (record.getR43_amt_name_of_sub3() != null) {
							 * cellE.setCellValue(record.getR43_amt_name_of_sub3().doubleValue());
							 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
							 * cellE.setCellStyle(textStyle); }
							 * 
							 * // Column 5 - name of sub4 cellF = row.createCell(5); if
							 * (record.getR43_amt_name_of_sub4() != null) {
							 * cellF.setCellValue(record.getR43_amt_name_of_sub4().doubleValue());
							 * cellF.setCellStyle(numberStyle); } else { cellF.setCellValue("");
							 * cellF.setCellStyle(textStyle); }
							 * 
							 * // Column 6 - name of sub5 cellG = row.createCell(6); if
							 * (record.getR43_amt_name_of_sub5() != null) {
							 * cellG.setCellValue(record.getR43_amt_name_of_sub5().doubleValue());
							 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
							 * cellG.setCellStyle(textStyle); }
							 */

							// row44
							// row44
							row = sheet.getRow(43);
							if (row == null)
								row = sheet.createRow(43);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR44_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR44_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR44_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR44_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR44_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR44_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR44_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR44_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR44_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR44_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}
							/*
							 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR44_tot_amt() !=
							 * null) { cellH.setCellValue(record.getR44_tot_amt().doubleValue());
							 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
							 * cellH.setCellStyle(textStyle); }
							 */

							// row45
							row = sheet.getRow(44);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR45_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR45_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR45_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR45_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR45_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR45_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR45_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR45_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR45_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR45_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR45_tot_amt() != null) {
								cellG.setCellValue(record.getR45_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// ================= R46 =================
							// row46
							row = sheet.getRow(45);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR46_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR46_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR46_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR46_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR46_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR46_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR46_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR46_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR46_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR46_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							// ================= R47 =================
							// row47
							row = sheet.getRow(46);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR47_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR47_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR47_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR47_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR47_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR47_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR47_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR47_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR47_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR47_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							// ================= R48 =================
							row = sheet.getRow(47);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR48_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR48_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}
							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR48_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR48_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}
							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR48_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR48_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}
							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR48_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR48_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}
							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR48_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR48_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}
							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR48_tot_amt() != null) {
								cellG.setCellValue(record.getR48_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// ================= R49 =================
							// row49
							row = sheet.getRow(48);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR49_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR49_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR49_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR49_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR49_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR49_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR49_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR49_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR49_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR49_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							// ================= R50 =================
							// row50
							row = sheet.getRow(49);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR50_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR50_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR50_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR50_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR50_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR50_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR50_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR50_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR50_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR50_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							// ====================== R51 ======================
							row = sheet.getRow(50);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR51_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR51_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR51_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR51_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR51_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR51_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR51_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR51_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR51_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR51_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR51_tot_amt() != null) {
								cellG.setCellValue(record.getR51_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// ====================== R52 ======================
							// row52
							row = sheet.getRow(51);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR52_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR52_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR52_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR52_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR52_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR52_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR52_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR52_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR52_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR52_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							/*
							 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR52_tot_amt() !=
							 * null) { cellH.setCellValue(record.getR52_tot_amt().doubleValue());
							 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
							 * cellH.setCellStyle(textStyle); }
							 */

							// ====================== R53 ======================

							// row53
							row = sheet.getRow(52);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR53_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR53_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR53_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR53_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR53_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR53_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR53_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR53_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR53_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR53_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							/*
							 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR53_tot_amt() !=
							 * null) { cellH.setCellValue(record.getR53_tot_amt().doubleValue());
							 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
							 * cellH.setCellStyle(textStyle); }
							 */

							// row54
							row = sheet.getRow(53);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR54_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR54_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR54_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR54_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR54_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR54_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR54_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR54_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR54_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR54_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}
							/*
							 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR54_tot_amt() !=
							 * null) { cellH.setCellValue(record.getR54_tot_amt().doubleValue());
							 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
							 * cellH.setCellStyle(textStyle); }
							 */

							// row55
							row = sheet.getRow(54);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR55_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR55_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR55_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR55_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR55_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR55_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR55_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR55_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR55_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR55_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							/*
							 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR55_tot_amt() !=
							 * null) { cellH.setCellValue(record.getR55_tot_amt().doubleValue());
							 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
							 * cellH.setCellStyle(textStyle); }
							 */

							// row56
							row = sheet.getRow(55);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR56_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR56_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR56_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR56_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR56_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR56_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR56_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR56_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR56_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR56_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							/*
							 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR56_tot_amt() !=
							 * null) { cellH.setCellValue(record.getR56_tot_amt().doubleValue());
							 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
							 * cellH.setCellStyle(textStyle); }
							 */

							// row57
							row = sheet.getRow(56);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR57_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR57_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR57_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR57_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR57_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR57_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR57_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR57_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR57_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR57_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							/*
							 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR57_tot_amt() !=
							 * null) { cellH.setCellValue(record.getR57_tot_amt().doubleValue());
							 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
							 * cellH.setCellStyle(textStyle); }
							 */

							// row58
							row = sheet.getRow(57);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR58_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR58_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR58_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR58_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR58_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR58_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR58_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR58_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR58_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR58_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

						}
						workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA4 SUMMARY", null,
								"BRRS_M_CA4_SUMMARYTABLE");
					}
					return out.toByteArray();
				}
			}
		}
	}

	// Normal Email Excel
	public byte[] BRRS_M_CA4EmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_CA4ArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_CA4ResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			List<M_CA4_Summary_Entity> dataList;
			try {
				dataList = getSummaryByDate(dateformat.parse(todate));
			} catch (ParseException e) {
				throw new RuntimeException("Date format must be dd-MMM-yyyy", e);
			}

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_CA4 report. Returning empty result.");
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
					SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");

					SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

					Date reportDateValue = inputFormat.parse(todate);

					// Set formatted date
					dateCell.setCellValue(outputFormat.format(reportDateValue));

					dateCell.setCellStyle(textStyle);

				} catch (ParseException e) {

					logger.error("Error parsing todate: {}", todate, e);
				}

				int startRow = 9;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_CA4_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

// row10

						// Column 2 - name of sub1
						Cell cellC = row.createCell(2);
						if (record.getR10_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR10_amt_name_of_sub1().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column 4 - name of sub2
						Cell cellD = row.createCell(4);
						if (record.getR10_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR10_amt_name_of_sub2().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column 6 - name of sub3
						Cell cellE = row.createCell(6);
						if (record.getR10_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR10_amt_name_of_sub3().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column 8 - name of sub4
						Cell cellF = row.createCell(8);
						if (record.getR10_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR10_amt_name_of_sub4().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column 10 - name of sub4
						Cell cellG = row.createCell(10);
						if (record.getR10_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR10_amt_name_of_sub5().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column 12 total
						cellG = row.createCell(12);
						if (record.getR10_tot_amt() != null) {
							cellG.setCellValue(record.getR10_tot_amt().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// row11
						row = sheet.getRow(10);

						// Column 2 - name of sub1
						cellC = row.createCell(2);
						if (record.getR11_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR11_amt_name_of_sub1().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column 3 - name of sub2
						cellD = row.createCell(4);
						if (record.getR11_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR11_amt_name_of_sub2().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column 4 - name of sub3
						cellE = row.createCell(6);
						if (record.getR11_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR11_amt_name_of_sub3().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column 5 - name of sub4
						cellF = row.createCell(8);
						if (record.getR11_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR11_amt_name_of_sub4().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column 6 - name of sub5
						cellG = row.createCell(10);
						if (record.getR11_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR11_amt_name_of_sub5().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column 7 total
						cellG = row.createCell(12);
						if (record.getR11_tot_amt() != null) {
							cellG.setCellValue(record.getR11_tot_amt().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// row13
						row = sheet.getRow(12);

						// Column 2 - name of sub1
						cellC = row.createCell(2);
						if (record.getR13_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR13_amt_name_of_sub1().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column 3 - name of sub2
						cellD = row.createCell(4);
						if (record.getR13_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR13_amt_name_of_sub2().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column 4 - name of sub3
						cellE = row.createCell(6);
						if (record.getR13_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR13_amt_name_of_sub3().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column 5 - name of sub4
						cellF = row.createCell(8);
						if (record.getR13_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR13_amt_name_of_sub4().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column 6 - name of sub5
						cellG = row.createCell(10);
						if (record.getR13_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR13_amt_name_of_sub5().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column 7 total
						cellG = row.createCell(12);
						if (record.getR13_tot_amt() != null) {
							cellG.setCellValue(record.getR13_tot_amt().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// row14
						row = sheet.getRow(13);

						// Column 2 - name of sub1
						cellC = row.createCell(2);
						if (record.getR14_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR14_amt_name_of_sub1().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column 3 - name of sub2
						cellD = row.createCell(4);
						if (record.getR14_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR14_amt_name_of_sub2().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column 4 - name of sub3
						cellE = row.createCell(6);
						if (record.getR14_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR14_amt_name_of_sub3().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column 5 - name of sub4
						cellF = row.createCell(8);
						if (record.getR14_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR14_amt_name_of_sub4().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column 6 - name of sub5
						cellG = row.createCell(10);
						if (record.getR14_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR14_amt_name_of_sub5().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column 7 total
						cellG = row.createCell(12);
						if (record.getR14_tot_amt() != null) {
							cellG.setCellValue(record.getR14_tot_amt().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// row17
						row = sheet.getRow(16);

						// Column 3 - name of sub1
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						if (record.getR17_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR17_amt_name_of_sub1().doubleValue());
						} else {
							cellC.setCellValue(0);
						}

						// Column 4 - name of sub2
						cellD = row.getCell(4);
						if (cellD == null)
							cellD = row.createCell(4);
						if (record.getR17_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR17_amt_name_of_sub2().doubleValue());
						} else {
							cellD.setCellValue(0);
						}

						// Column 5 - name of sub3
						cellE = row.getCell(6);
						if (cellE == null)
							cellE = row.createCell(6);
						if (record.getR17_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR17_amt_name_of_sub3().doubleValue());
						} else {
							cellE.setCellValue(0);
						}

						// Column 6 - name of sub4
						cellF = row.getCell(8);
						if (cellF == null)
							cellF = row.createCell(8);
						if (record.getR17_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR17_amt_name_of_sub4().doubleValue());
						} else {
							cellF.setCellValue(0);
						}

						// Column 7 - name of sub5
						cellG = row.getCell(10);
						if (cellG == null)
							cellG = row.createCell(10);
						if (record.getR17_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR17_amt_name_of_sub5().doubleValue());
						} else {
							cellG.setCellValue(0);
						}

						// COLUMN TOTAL
						cellG = row.getCell(12);
						if (cellG == null) {
							cellG = row.createCell(12);
						}

						if (record.getR17_tot_amt() != null) {
							cellG.setCellValue(record.getR17_tot_amt().doubleValue());
						} else {
							cellG.setCellValue("");
						}

						// row18
						row = sheet.getRow(17);

						// Column 2 - name of sub1
						cellC = row.createCell(2);
						if (record.getR18_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR18_amt_name_of_sub1().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column 3 - name of sub2
						cellD = row.createCell(4);
						if (record.getR18_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR18_amt_name_of_sub2().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column 4 - name of sub3
						cellE = row.createCell(6);
						if (record.getR18_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR18_amt_name_of_sub3().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column 5 - name of sub4
						cellF = row.createCell(8);
						if (record.getR18_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR18_amt_name_of_sub4().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column 6 - name of sub5
						cellG = row.createCell(10);
						if (record.getR18_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR18_amt_name_of_sub5().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column 7 total
						cellG = row.createCell(12);
						if (record.getR18_tot_amt() != null) {
							cellG.setCellValue(record.getR18_tot_amt().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// row19
						row = sheet.getRow(18);

						// Column 2 - name of sub1
						cellC = row.createCell(2);
						if (record.getR19_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR19_amt_name_of_sub1().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column 3 - name of sub2
						cellD = row.createCell(4);
						if (record.getR19_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR19_amt_name_of_sub2().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column 4 - name of sub3
						cellE = row.createCell(6);
						if (record.getR19_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR19_amt_name_of_sub3().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column 5 - name of sub4
						cellF = row.createCell(8);
						if (record.getR19_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR19_amt_name_of_sub4().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column 6 - name of sub5
						cellG = row.createCell(10);
						if (record.getR19_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR19_amt_name_of_sub5().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column 7 total
						cellG = row.createCell(12);
						if (record.getR19_tot_amt() != null) {
							cellG.setCellValue(record.getR19_tot_amt().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						/*
						 * 
						 * // row22 row = sheet.getRow(21);
						 * 
						 * 
						 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
						 * row.createCell(2); if (record.getR22_amt_name_of_sub1() != null) {
						 * cellC.setCellValue(record.getR22_amt_name_of_sub1().doubleValue()); } else {
						 * cellC.setCellValue(0); }
						 * 
						 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
						 * row.createCell(4); if (record.getR22_amt_name_of_sub2() != null) {
						 * cellD.setCellValue(record.getR22_amt_name_of_sub2().doubleValue()); } else {
						 * cellD.setCellValue(0); }
						 * 
						 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
						 * row.createCell(6); if (record.getR22_amt_name_of_sub3() != null) {
						 * cellE.setCellValue(record.getR22_amt_name_of_sub3().doubleValue()); } else {
						 * cellE.setCellValue(0); }
						 * 
						 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
						 * row.createCell(8); if (record.getR22_amt_name_of_sub4() != null) {
						 * cellF.setCellValue(record.getR22_amt_name_of_sub4().doubleValue()); } else {
						 * cellF.setCellValue(0); }
						 * 
						 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
						 * = row.createCell(10); if (record.getR22_amt_name_of_sub5() != null) {
						 * cellG.setCellValue(record.getR22_amt_name_of_sub5().doubleValue()); } else {
						 * cellG.setCellValue(0); }
						 * 
						 * 
						 * //COLUMN TOTAL cellG = row.getCell(12); if (cellG == null) { cellG =
						 * row.createCell(12); }
						 * 
						 * if (record.getR22_tot_amt() != null) {
						 * cellG.setCellValue(record.getR22_tot_amt().doubleValue()); } else {
						 * cellG.setCellValue(""); }
						 * 
						 */

						// row23
						row = sheet.getRow(22);

						// Column 2 - name of sub1
						cellC = row.createCell(2);
						if (record.getR23_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR23_amt_name_of_sub1().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column 3 - name of sub2
						cellD = row.createCell(4);
						if (record.getR23_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR23_amt_name_of_sub2().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column 4 - name of sub3
						cellE = row.createCell(6);
						if (record.getR23_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR23_amt_name_of_sub3().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column 5 - name of sub4
						cellF = row.createCell(8);
						if (record.getR23_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR23_amt_name_of_sub4().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column 6 - name of sub5
						cellG = row.createCell(10);
						if (record.getR23_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR23_amt_name_of_sub5().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column 7 total
						cellG = row.createCell(12);
						if (record.getR23_tot_amt() != null) {
							cellG.setCellValue(record.getR23_tot_amt().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// row24
						row = sheet.getRow(23);

						// Column 2 - name of sub1
						cellC = row.createCell(2);
						if (record.getR24_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR24_amt_name_of_sub1().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column 3 - name of sub2
						cellD = row.createCell(4);
						if (record.getR24_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR24_amt_name_of_sub2().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column 4 - name of sub3
						cellE = row.createCell(6);
						if (record.getR24_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR24_amt_name_of_sub3().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column 5 - name of sub4
						cellF = row.createCell(8);
						if (record.getR24_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR24_amt_name_of_sub4().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column 6 - name of sub5
						cellG = row.createCell(10);
						if (record.getR24_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR24_amt_name_of_sub5().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column 7 total
						cellG = row.createCell(12);
						if (record.getR24_tot_amt() != null) {
							cellG.setCellValue(record.getR24_tot_amt().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// row27
						row = sheet.getRow(21);

						// Column 3 - name of sub1
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						if (record.getR27_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR27_amt_name_of_sub1().doubleValue());
						} else {
							cellC.setCellValue(0);
						}

						// Column 4 - name of sub2
						cellD = row.getCell(4);
						if (cellD == null)
							cellD = row.createCell(4);
						if (record.getR27_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR27_amt_name_of_sub2().doubleValue());
						} else {
							cellD.setCellValue(0);
						}

						// Column 5 - name of sub3
						cellE = row.getCell(6);
						if (cellE == null)
							cellE = row.createCell(6);
						if (record.getR27_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR27_amt_name_of_sub3().doubleValue());
						} else {
							cellE.setCellValue(0);
						}

						// Column 6 - name of sub4
						cellF = row.getCell(8);
						if (cellF == null)
							cellF = row.createCell(8);
						if (record.getR27_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR27_amt_name_of_sub4().doubleValue());
						} else {
							cellF.setCellValue(0);
						}

						// Column 7 - name of sub5
						cellG = row.getCell(10);
						if (cellG == null)
							cellG = row.createCell(10);
						if (record.getR27_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR27_amt_name_of_sub5().doubleValue());
						} else {
							cellG.setCellValue(0);
						}
						// COLUMN TOTAL
						cellG = row.getCell(12);
						if (cellG == null) {
							cellG = row.createCell(12);
						}

						if (record.getR27_tot_amt() != null) {
							cellG.setCellValue(record.getR27_tot_amt().doubleValue());
						} else {
							cellG.setCellValue("");
						}

						// row28
						row = sheet.getRow(22);

						// Column 2 - name of sub1
						cellC = row.createCell(2);
						if (record.getR28_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR28_amt_name_of_sub1().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column 3 - name of sub2
						cellD = row.createCell(4);
						if (record.getR28_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR28_amt_name_of_sub2().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column 4 - name of sub3
						cellE = row.createCell(6);
						if (record.getR28_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR28_amt_name_of_sub3().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column 5 - name of sub4
						cellF = row.createCell(8);
						if (record.getR28_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR28_amt_name_of_sub4().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column 6 - name of sub5
						cellG = row.createCell(10);
						if (record.getR28_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR28_amt_name_of_sub5().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column 7 total
						cellG = row.createCell(12);
						if (record.getR28_tot_amt() != null) {
							cellG.setCellValue(record.getR28_tot_amt().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// row24
						row = sheet.getRow(23);
						// Column 1 - item

						// Column 2 - name of sub1
						cellC = row.createCell(2);
						if (record.getR29_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR29_amt_name_of_sub1().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column 3 - name of sub2
						cellD = row.createCell(4);
						if (record.getR29_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR29_amt_name_of_sub2().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column 4 - name of sub3
						cellE = row.createCell(6);
						if (record.getR29_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR29_amt_name_of_sub3().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column 5 - name of sub4
						cellF = row.createCell(8);
						if (record.getR29_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR29_amt_name_of_sub4().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column 6 - name of sub5
						cellG = row.createCell(10);
						if (record.getR29_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR29_amt_name_of_sub5().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column 7 total
						cellG = row.createCell(12);
						if (record.getR29_tot_amt() != null) {
							cellG.setCellValue(record.getR29_tot_amt().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// row27
						row = sheet.getRow(26);

						// Column 3 - name of sub1
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						if (record.getR32_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR32_amt_name_of_sub1().doubleValue());
						} else {
							cellC.setCellValue(0);
						}

						// Column 4 - name of sub2
						cellD = row.getCell(4);
						if (cellD == null)
							cellD = row.createCell(4);
						if (record.getR32_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR32_amt_name_of_sub2().doubleValue());
						} else {
							cellD.setCellValue(0);
						}

						// Column 5 - name of sub3
						cellE = row.getCell(6);
						if (cellE == null)
							cellE = row.createCell(6);
						if (record.getR32_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR32_amt_name_of_sub3().doubleValue());
						} else {
							cellE.setCellValue(0);
						}

						// Column 6 - name of sub4
						cellF = row.getCell(8);
						if (cellF == null)
							cellF = row.createCell(8);
						if (record.getR32_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR32_amt_name_of_sub4().doubleValue());
						} else {
							cellF.setCellValue(0);
						}

						// Column 7 - name of sub5
						cellG = row.getCell(10);
						if (cellG == null)
							cellG = row.createCell(10);
						if (record.getR32_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR32_amt_name_of_sub5().doubleValue());
						} else {
							cellG.setCellValue(0);
						}
						// COLUMN TOTAL
						cellG = row.getCell(12);
						if (cellG == null) {
							cellG = row.createCell(12);
						}

						if (record.getR32_tot_amt() != null) {
							cellG.setCellValue(record.getR32_tot_amt().doubleValue());
						} else {
							cellG.setCellValue("");
						}

						// row33
						row = sheet.getRow(27);

						// Column 2 - name of sub1
						cellC = row.createCell(2);
						if (record.getR33_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR33_amt_name_of_sub1().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column 3 - name of sub2
						cellD = row.createCell(4);
						if (record.getR33_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR33_amt_name_of_sub2().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column 4 - name of sub3
						cellE = row.createCell(6);
						if (record.getR33_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR33_amt_name_of_sub3().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column 5 - name of sub4
						cellF = row.createCell(8);
						if (record.getR33_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR33_amt_name_of_sub4().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column 6 - name of sub5
						cellG = row.createCell(10);
						if (record.getR33_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR33_amt_name_of_sub5().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column 7 total
						cellG = row.createCell(12);
						if (record.getR33_tot_amt() != null) {
							cellG.setCellValue(record.getR33_tot_amt().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// row34
						row = sheet.getRow(28);

						// Column 2 - name of sub1
						cellC = row.createCell(2);
						if (record.getR34_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR34_amt_name_of_sub1().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column 3 - name of sub2
						cellD = row.createCell(4);
						if (record.getR34_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR34_amt_name_of_sub2().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column 4 - name of sub3
						cellE = row.createCell(6);
						if (record.getR34_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR34_amt_name_of_sub3().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column 5 - name of sub4
						cellF = row.createCell(8);
						if (record.getR34_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR34_amt_name_of_sub4().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column 6 - name of sub5
						cellG = row.createCell(10);
						if (record.getR34_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR34_amt_name_of_sub5().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column 7 total
						cellG = row.createCell(12);
						if (record.getR34_tot_amt() != null) {
							cellG.setCellValue(record.getR34_tot_amt().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// ====================== R36 ======================
						// row36
						row = sheet.getRow(30);

						// Column 3 - name of sub1
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						if (record.getR36_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR36_amt_name_of_sub1().doubleValue());
						} else {
							cellC.setCellValue(0);
						}

						// Column 4 - name of sub2
						cellD = row.getCell(4);
						if (cellD == null)
							cellD = row.createCell(4);
						if (record.getR36_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR36_amt_name_of_sub2().doubleValue());
						} else {
							cellD.setCellValue(0);
						}

						// Column 5 - name of sub3
						cellE = row.getCell(6);
						if (cellE == null)
							cellE = row.createCell(6);
						if (record.getR36_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR36_amt_name_of_sub3().doubleValue());
						} else {
							cellE.setCellValue(0);
						}

						// Column 6 - name of sub4
						cellF = row.getCell(8);
						if (cellF == null)
							cellF = row.createCell(8);
						if (record.getR36_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR36_amt_name_of_sub4().doubleValue());
						} else {
							cellF.setCellValue(0);
						}

						// Column 7 - name of sub5
						cellG = row.getCell(10);
						if (cellG == null)
							cellG = row.createCell(10);
						if (record.getR36_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR36_amt_name_of_sub5().doubleValue());
						} else {
							cellG.setCellValue(0);
						}

						// ====================== R37 ======================
						row = sheet.getRow(31);

						// Column 2 - name of sub1
						cellC = row.createCell(2);
						if (record.getR37_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR37_amt_name_of_sub1().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column 3 - name of sub2
						cellD = row.createCell(4);
						if (record.getR37_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR37_amt_name_of_sub2().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column 4 - name of sub3
						cellE = row.createCell(6);
						if (record.getR37_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR37_amt_name_of_sub3().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column 5 - name of sub4
						cellF = row.createCell(8);
						if (record.getR37_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR37_amt_name_of_sub4().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column 6 - name of sub5
						cellG = row.createCell(10);
						if (record.getR37_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR37_amt_name_of_sub5().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column 7 total
						cellG = row.createCell(12);
						if (record.getR37_tot_amt() != null) {
							cellG.setCellValue(record.getR37_tot_amt().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// ====================== R38 ======================
						row = sheet.getRow(32);

						// Column 2 - name of sub1
						cellC = row.createCell(2);
						if (record.getR38_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR38_amt_name_of_sub1().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column 3 - name of sub2
						cellD = row.createCell(4);
						if (record.getR38_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR38_amt_name_of_sub2().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column 4 - name of sub3
						cellE = row.createCell(6);
						if (record.getR38_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR38_amt_name_of_sub3().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column 5 - name of sub4
						cellF = row.createCell(8);
						if (record.getR38_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR38_amt_name_of_sub4().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column 6 - name of sub5
						cellG = row.createCell(10);
						if (record.getR38_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR38_amt_name_of_sub5().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}
						// Column 7 total
						cellG = row.createCell(12);
						if (record.getR38_tot_amt() != null) {
							cellG.setCellValue(record.getR38_tot_amt().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// ====================== R39 ======================
						// row39
						row = sheet.getRow(33);

						// Column 3 - name of sub1
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						if (record.getR39_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR39_amt_name_of_sub1().doubleValue());
						} else {
							cellC.setCellValue(0);
						}

						// Column 4 - name of sub2
						cellD = row.getCell(4);
						if (cellD == null)
							cellD = row.createCell(4);
						if (record.getR39_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR39_amt_name_of_sub2().doubleValue());
						} else {
							cellD.setCellValue(0);
						}

						// Column 5 - name of sub3
						cellE = row.getCell(6);
						if (cellE == null)
							cellE = row.createCell(6);
						if (record.getR39_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR39_amt_name_of_sub3().doubleValue());
						} else {
							cellE.setCellValue(0);
						}

						// Column 6 - name of sub4
						cellF = row.getCell(8);
						if (cellF == null)
							cellF = row.createCell(8);
						if (record.getR39_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR39_amt_name_of_sub4().doubleValue());
						} else {
							cellF.setCellValue(0);
						}

						// Column 7 - name of sub5
						cellG = row.getCell(10);
						if (cellG == null)
							cellG = row.createCell(10);
						if (record.getR39_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR39_amt_name_of_sub5().doubleValue());
						} else {
							cellG.setCellValue(0);
						}

						// ====================== R40 ======================
						row = sheet.getRow(34);

						// Column 2 - name of sub1
						cellC = row.createCell(2);
						if (record.getR40_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR40_amt_name_of_sub1().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column 3 - name of sub2
						cellD = row.createCell(4);
						if (record.getR40_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR40_amt_name_of_sub2().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column 4 - name of sub3
						cellE = row.createCell(6);
						if (record.getR40_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR40_amt_name_of_sub3().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column 5 - name of sub4
						cellF = row.createCell(8);
						if (record.getR40_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR40_amt_name_of_sub4().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column 6 - name of sub5
						cellG = row.createCell(10);
						if (record.getR40_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR40_amt_name_of_sub5().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}
						// Column 7 total
						cellG = row.createCell(12);
						if (record.getR40_tot_amt() != null) {
							cellG.setCellValue(record.getR40_tot_amt().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// =========================
						// Row for R41
						// =========================
						row = sheet.getRow(35);

						// Column 2 - name of sub1
						cellC = row.createCell(2);
						if (record.getR41_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR41_amt_name_of_sub1().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column 3 - name of sub2
						cellD = row.createCell(4);
						if (record.getR41_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR41_amt_name_of_sub2().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column 4 - name of sub3
						cellE = row.createCell(6);
						if (record.getR41_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR41_amt_name_of_sub3().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column 5 - name of sub4
						cellF = row.createCell(8);
						if (record.getR41_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR41_amt_name_of_sub4().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column 6 - name of sub5
						cellG = row.createCell(10);
						if (record.getR41_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR41_amt_name_of_sub5().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column 7 total
						cellG = row.createCell(12);
						if (record.getR41_tot_amt() != null) {
							cellG.setCellValue(record.getR41_tot_amt().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// =========================
						// Row for R42
						// =========================
						row = sheet.getRow(36);

						// Column 2 - name of sub1
						cellC = row.createCell(2);
						if (record.getR42_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR42_amt_name_of_sub1().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column 3 - name of sub2
						cellD = row.createCell(4);
						if (record.getR42_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR42_amt_name_of_sub2().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column 4 - name of sub3
						cellE = row.createCell(6);
						if (record.getR42_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR42_amt_name_of_sub3().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column 5 - name of sub4
						cellF = row.createCell(8);
						if (record.getR42_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR42_amt_name_of_sub4().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column 6 - name of sub5
						cellG = row.createCell(10);
						if (record.getR42_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR42_amt_name_of_sub5().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}
						// Column 7 total
						cellG = row.createCell(12);
						if (record.getR42_tot_amt() != null) {
							cellG.setCellValue(record.getR42_tot_amt().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// =========================
						// Row for R43
						// =========================
						row = sheet.getRow(37);

						// Column 3 - name of sub1
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						if (record.getR43_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR43_amt_name_of_sub1().doubleValue());
						} else {
							cellC.setCellValue(0);
						}

						// Column 4 - name of sub2
						cellD = row.getCell(4);
						if (cellD == null)
							cellD = row.createCell(4);
						if (record.getR43_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR43_amt_name_of_sub2().doubleValue());
						} else {
							cellD.setCellValue(0);
						}

						// Column 5 - name of sub3
						cellE = row.getCell(6);
						if (cellE == null)
							cellE = row.createCell(6);
						if (record.getR43_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR43_amt_name_of_sub3().doubleValue());
						} else {
							cellE.setCellValue(0);
						}

						// Column 6 - name of sub4
						cellF = row.getCell(8);
						if (cellF == null)
							cellF = row.createCell(8);
						if (record.getR43_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR43_amt_name_of_sub4().doubleValue());
						} else {
							cellF.setCellValue(0);
						}

						// Column 7 - name of sub5
						cellG = row.getCell(10);
						if (cellG == null)
							cellG = row.createCell(10);
						if (record.getR43_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR43_amt_name_of_sub5().doubleValue());
						} else {
							cellG.setCellValue(0);
						}

						// row44
						// row44
						row = sheet.getRow(38);
						if (row == null)
							row = sheet.createRow(43);

						// Column 3 - name of sub1
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						if (record.getR44_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR44_amt_name_of_sub1().doubleValue());
						} else {
							cellC.setCellValue(0);
						}

						// Column 4 - name of sub2
						cellD = row.getCell(4);
						if (cellD == null)
							cellD = row.createCell(4);
						if (record.getR44_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR44_amt_name_of_sub2().doubleValue());
						} else {
							cellD.setCellValue(0);
						}

						// Column 5 - name of sub3
						cellE = row.getCell(6);
						if (cellE == null)
							cellE = row.createCell(6);
						if (record.getR44_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR44_amt_name_of_sub3().doubleValue());
						} else {
							cellE.setCellValue(0);
						}

						// Column 6 - name of sub4
						cellF = row.getCell(8);
						if (cellF == null)
							cellF = row.createCell(8);
						if (record.getR44_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR44_amt_name_of_sub4().doubleValue());
						} else {
							cellF.setCellValue(0);
						}

						// Column 7 - name of sub5
						cellG = row.getCell(10);
						if (cellG == null)
							cellG = row.createCell(10);
						if (record.getR44_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR44_amt_name_of_sub5().doubleValue());
						} else {
							cellG.setCellValue(0);
						}
						/*
						 * // Column 7 - TOTAL cellH = row.createCell(12); if (record.getR44_tot_amt()
						 * != null) { cellH.setCellValue(record.getR44_tot_amt().doubleValue());
						 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
						 * cellH.setCellStyle(textStyle); }
						 */

						// row45
						row = sheet.getRow(39);

						// Column 2 - name of sub1
						cellC = row.createCell(2);
						if (record.getR45_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR45_amt_name_of_sub1().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Column 3 - name of sub2
						cellD = row.createCell(4);
						if (record.getR45_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR45_amt_name_of_sub2().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column 4 - name of sub3
						cellE = row.createCell(6);
						if (record.getR45_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR45_amt_name_of_sub3().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// Column 5 - name of sub4
						cellF = row.createCell(8);
						if (record.getR45_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR45_amt_name_of_sub4().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// Column 6 - name of sub5
						cellG = row.createCell(10);
						if (record.getR45_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR45_amt_name_of_sub5().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Column 7 total
						cellG = row.createCell(12);
						if (record.getR45_tot_amt() != null) {
							cellG.setCellValue(record.getR45_tot_amt().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// ================= R46 =================
						// row46
						row = sheet.getRow(40);

						// Column 3 - name of sub1
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						if (record.getR46_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR46_amt_name_of_sub1().doubleValue());
						} else {
							cellC.setCellValue(0);
						}

						// Column 4 - name of sub2
						cellD = row.getCell(4);
						if (cellD == null)
							cellD = row.createCell(4);
						if (record.getR46_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR46_amt_name_of_sub2().doubleValue());
						} else {
							cellD.setCellValue(0);
						}

						// Column 5 - name of sub3
						cellE = row.getCell(6);
						if (cellE == null)
							cellE = row.createCell(6);
						if (record.getR46_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR46_amt_name_of_sub3().doubleValue());
						} else {
							cellE.setCellValue(0);
						}

						// Column 6 - name of sub4
						cellF = row.getCell(8);
						if (cellF == null)
							cellF = row.createCell(8);
						if (record.getR46_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR46_amt_name_of_sub4().doubleValue());
						} else {
							cellF.setCellValue(0);
						}

						// Column 7 - name of sub5
						cellG = row.getCell(10);
						if (cellG == null)
							cellG = row.createCell(10);
						if (record.getR46_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR46_amt_name_of_sub5().doubleValue());
						} else {
							cellG.setCellValue(0);
						}

						// ================= R47 =================
						// row47
						row = sheet.getRow(41);

						// Column 3 - name of sub1
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						if (record.getR47_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR47_amt_name_of_sub1().doubleValue());
						} else {
							cellC.setCellValue(0);
						}

						// Column 4 - name of sub2
						cellD = row.getCell(4);
						if (cellD == null)
							cellD = row.createCell(4);
						if (record.getR47_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR47_amt_name_of_sub2().doubleValue());
						} else {
							cellD.setCellValue(0);
						}

						// Column 5 - name of sub3
						cellE = row.getCell(6);
						if (cellE == null)
							cellE = row.createCell(6);
						if (record.getR47_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR47_amt_name_of_sub3().doubleValue());
						} else {
							cellE.setCellValue(0);
						}

						// Column 6 - name of sub4
						cellF = row.getCell(8);
						if (cellF == null)
							cellF = row.createCell(8);
						if (record.getR47_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR47_amt_name_of_sub4().doubleValue());
						} else {
							cellF.setCellValue(0);
						}

						// Column 7 - name of sub5
						cellG = row.getCell(10);
						if (cellG == null)
							cellG = row.createCell(10);
						if (record.getR47_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR47_amt_name_of_sub5().doubleValue());
						} else {
							cellG.setCellValue(0);
						}

						// ================= R48 =================
						row = sheet.getRow(42);
						/*
						 * // Column 2 - name of sub1 cellC = row.createCell(2); if
						 * (record.getR48_amt_name_of_sub1() != null) {
						 * cellC.setCellValue(record.getR48_amt_name_of_sub1().doubleValue());
						 * cellC.setCellStyle(numberStyle); } else { cellC.setCellValue("");
						 * cellC.setCellStyle(textStyle); } // Column 3 - name of sub2 cellD =
						 * row.createCell(4); if (record.getR48_amt_name_of_sub2() != null) {
						 * cellD.setCellValue(record.getR48_amt_name_of_sub2().doubleValue());
						 * cellD.setCellStyle(numberStyle); } else { cellD.setCellValue("");
						 * cellD.setCellStyle(textStyle); } // Column 4 - name of sub3 cellE =
						 * row.createCell(6); if (record.getR48_amt_name_of_sub3() != null) {
						 * cellE.setCellValue(record.getR48_amt_name_of_sub3().doubleValue());
						 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
						 * cellE.setCellStyle(textStyle); } // Column 5 - name of sub4 cellF =
						 * row.createCell(8); if (record.getR48_amt_name_of_sub4() != null) {
						 * cellF.setCellValue(record.getR48_amt_name_of_sub4().doubleValue());
						 * cellF.setCellStyle(numberStyle); } else { cellF.setCellValue("");
						 * cellF.setCellStyle(textStyle); } // Column 6 - name of sub5 cellG =
						 * row.createCell(10); if (record.getR48_amt_name_of_sub5() != null) {
						 * cellG.setCellValue(record.getR48_amt_name_of_sub5().doubleValue());
						 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
						 * cellG.setCellStyle(textStyle); } // Column 7 total cellG =
						 * row.createCell(12); if (record.getR48_tot_amt() != null) {
						 * cellG.setCellValue(record.getR48_tot_amt().doubleValue());
						 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
						 * cellG.setCellStyle(textStyle); }
						 * 
						 * 
						 * 
						 * // ================= R49 ================= // row49 row = sheet.getRow(43);
						 * 
						 * 
						 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
						 * row.createCell(2); if (record.getR49_amt_name_of_sub1() != null) {
						 * cellC.setCellValue(record.getR49_amt_name_of_sub1().doubleValue()); } else {
						 * cellC.setCellValue(0); }
						 * 
						 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
						 * row.createCell(4); if (record.getR49_amt_name_of_sub2() != null) {
						 * cellD.setCellValue(record.getR49_amt_name_of_sub2().doubleValue()); } else {
						 * cellD.setCellValue(0); }
						 * 
						 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
						 * row.createCell(6); if (record.getR49_amt_name_of_sub3() != null) {
						 * cellE.setCellValue(record.getR49_amt_name_of_sub3().doubleValue()); } else {
						 * cellE.setCellValue(0); }
						 * 
						 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
						 * row.createCell(8); if (record.getR49_amt_name_of_sub4() != null) {
						 * cellF.setCellValue(record.getR49_amt_name_of_sub4().doubleValue()); } else {
						 * cellF.setCellValue(0); }
						 * 
						 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
						 * = row.createCell(10); if (record.getR49_amt_name_of_sub5() != null) {
						 * cellG.setCellValue(record.getR49_amt_name_of_sub5().doubleValue()); } else {
						 * cellG.setCellValue(0); }
						 * 
						 * 
						 * // ================= R50 ================= // row50 row = sheet.getRow(44);
						 * 
						 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
						 * row.createCell(2); if (record.getR50_amt_name_of_sub1() != null) {
						 * cellC.setCellValue(record.getR50_amt_name_of_sub1().doubleValue()); } else {
						 * cellC.setCellValue(0); }
						 * 
						 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
						 * row.createCell(4); if (record.getR50_amt_name_of_sub2() != null) {
						 * cellD.setCellValue(record.getR50_amt_name_of_sub2().doubleValue()); } else {
						 * cellD.setCellValue(0); }
						 * 
						 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
						 * row.createCell(6); if (record.getR50_amt_name_of_sub3() != null) {
						 * cellE.setCellValue(record.getR50_amt_name_of_sub3().doubleValue()); } else {
						 * cellE.setCellValue(0); }
						 * 
						 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
						 * row.createCell(8); if (record.getR50_amt_name_of_sub4() != null) {
						 * cellF.setCellValue(record.getR50_amt_name_of_sub4().doubleValue()); } else {
						 * cellF.setCellValue(0); }
						 * 
						 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
						 * = row.createCell(10); if (record.getR50_amt_name_of_sub5() != null) {
						 * cellG.setCellValue(record.getR50_amt_name_of_sub5().doubleValue()); } else {
						 * cellG.setCellValue(0); }
						 * 
						 * 
						 * // ====================== R51 ====================== row = sheet.getRow(45);
						 * 
						 * 
						 * // Column 2 - name of sub1 cellC = row.createCell(2); if
						 * (record.getR51_amt_name_of_sub1() != null) {
						 * cellC.setCellValue(record.getR51_amt_name_of_sub1().doubleValue());
						 * cellC.setCellStyle(numberStyle); } else { cellC.setCellValue("");
						 * cellC.setCellStyle(textStyle); }
						 * 
						 * // Column 3 - name of sub2 cellD = row.createCell(4); if
						 * (record.getR51_amt_name_of_sub2() != null) {
						 * cellD.setCellValue(record.getR51_amt_name_of_sub2().doubleValue());
						 * cellD.setCellStyle(numberStyle); } else { cellD.setCellValue("");
						 * cellD.setCellStyle(textStyle); }
						 * 
						 * // Column 4 - name of sub3 cellE = row.createCell(6); if
						 * (record.getR51_amt_name_of_sub3() != null) {
						 * cellE.setCellValue(record.getR51_amt_name_of_sub3().doubleValue());
						 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
						 * cellE.setCellStyle(textStyle); }
						 * 
						 * // Column 5 - name of sub4 cellF = row.createCell(8); if
						 * (record.getR51_amt_name_of_sub4() != null) {
						 * cellF.setCellValue(record.getR51_amt_name_of_sub4().doubleValue());
						 * cellF.setCellStyle(numberStyle); } else { cellF.setCellValue("");
						 * cellF.setCellStyle(textStyle); }
						 * 
						 * // Column 6 - name of sub5 cellG = row.createCell(10); if
						 * (record.getR51_amt_name_of_sub5() != null) {
						 * cellG.setCellValue(record.getR51_amt_name_of_sub5().doubleValue());
						 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
						 * cellG.setCellStyle(textStyle); }
						 * 
						 * // Column 7 total cellG = row.createCell(12); if (record.getR51_tot_amt() !=
						 * null) { cellG.setCellValue(record.getR51_tot_amt().doubleValue());
						 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
						 * cellG.setCellStyle(textStyle); }
						 * 
						 * 
						 * 
						 * // ====================== R52 ====================== // row52 row =
						 * sheet.getRow(46);
						 * 
						 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
						 * row.createCell(2); if (record.getR52_amt_name_of_sub1() != null) {
						 * cellC.setCellValue(record.getR52_amt_name_of_sub1().doubleValue()); } else {
						 * cellC.setCellValue(0); }
						 * 
						 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
						 * row.createCell(4); if (record.getR52_amt_name_of_sub2() != null) {
						 * cellD.setCellValue(record.getR52_amt_name_of_sub2().doubleValue()); } else {
						 * cellD.setCellValue(0); }
						 * 
						 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
						 * row.createCell(6); if (record.getR52_amt_name_of_sub3() != null) {
						 * cellE.setCellValue(record.getR52_amt_name_of_sub3().doubleValue()); } else {
						 * cellE.setCellValue(0); }
						 * 
						 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
						 * row.createCell(8); if (record.getR52_amt_name_of_sub4() != null) {
						 * cellF.setCellValue(record.getR52_amt_name_of_sub4().doubleValue()); } else {
						 * cellF.setCellValue(0); }
						 * 
						 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
						 * = row.createCell(10); if (record.getR52_amt_name_of_sub5() != null) {
						 * cellG.setCellValue(record.getR52_amt_name_of_sub5().doubleValue()); } else {
						 * cellG.setCellValue(0); }
						 * 
						 */

						// ====================== R53 ======================

						// row53
						row = sheet.getRow(44);

						// Column 3 - name of sub1
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						if (record.getR53_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR53_amt_name_of_sub1().doubleValue());
						} else {
							cellC.setCellValue(0);
						}

						// Column 4 - name of sub2
						cellD = row.getCell(4);
						if (cellD == null)
							cellD = row.createCell(4);
						if (record.getR53_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR53_amt_name_of_sub2().doubleValue());
						} else {
							cellD.setCellValue(0);
						}

						// Column 5 - name of sub3
						cellE = row.getCell(6);
						if (cellE == null)
							cellE = row.createCell(6);
						if (record.getR53_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR53_amt_name_of_sub3().doubleValue());
						} else {
							cellE.setCellValue(0);
						}

						// Column 6 - name of sub4
						cellF = row.getCell(8);
						if (cellF == null)
							cellF = row.createCell(8);
						if (record.getR53_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR53_amt_name_of_sub4().doubleValue());
						} else {
							cellF.setCellValue(0);
						}

						// Column 7 - name of sub5
						cellG = row.getCell(10);
						if (cellG == null)
							cellG = row.createCell(10);
						if (record.getR53_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR53_amt_name_of_sub5().doubleValue());
						} else {
							cellG.setCellValue(0);
						}

						// row54
						row = sheet.getRow(45);

						// Column 3 - name of sub1
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						if (record.getR54_amt_name_of_sub1() != null) {
							cellC.setCellValue(record.getR54_amt_name_of_sub1().doubleValue());
						} else {
							cellC.setCellValue(0);
						}

						// Column 4 - name of sub2
						cellD = row.getCell(4);
						if (cellD == null)
							cellD = row.createCell(4);
						if (record.getR54_amt_name_of_sub2() != null) {
							cellD.setCellValue(record.getR54_amt_name_of_sub2().doubleValue());
						} else {
							cellD.setCellValue(0);
						}

						// Column 5 - name of sub3
						cellE = row.getCell(6);
						if (cellE == null)
							cellE = row.createCell(6);
						if (record.getR54_amt_name_of_sub3() != null) {
							cellE.setCellValue(record.getR54_amt_name_of_sub3().doubleValue());
						} else {
							cellE.setCellValue(0);
						}

						// Column 6 - name of sub4
						cellF = row.getCell(8);
						if (cellF == null)
							cellF = row.createCell(8);
						if (record.getR54_amt_name_of_sub4() != null) {
							cellF.setCellValue(record.getR54_amt_name_of_sub4().doubleValue());
						} else {
							cellF.setCellValue(0);
						}

						// Column 7 - name of sub5
						cellG = row.getCell(10);
						if (cellG == null)
							cellG = row.createCell(10);
						if (record.getR54_amt_name_of_sub5() != null) {
							cellG.setCellValue(record.getR54_amt_name_of_sub5().doubleValue());
						} else {
							cellG.setCellValue(0);
						}
						/*
						 * 
						 * 
						 * // row55 row = sheet.getRow(49);
						 * 
						 * 
						 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
						 * row.createCell(2); if (record.getR55_amt_name_of_sub1() != null) {
						 * cellC.setCellValue(record.getR55_amt_name_of_sub1().doubleValue()); } else {
						 * cellC.setCellValue(0); }
						 * 
						 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
						 * row.createCell(4); if (record.getR55_amt_name_of_sub2() != null) {
						 * cellD.setCellValue(record.getR55_amt_name_of_sub2().doubleValue()); } else {
						 * cellD.setCellValue(0); }
						 * 
						 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
						 * row.createCell(6); if (record.getR55_amt_name_of_sub3() != null) {
						 * cellE.setCellValue(record.getR55_amt_name_of_sub3().doubleValue()); } else {
						 * cellE.setCellValue(0); }
						 * 
						 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
						 * row.createCell(8); if (record.getR55_amt_name_of_sub4() != null) {
						 * cellF.setCellValue(record.getR55_amt_name_of_sub4().doubleValue()); } else {
						 * cellF.setCellValue(0); }
						 * 
						 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
						 * = row.createCell(10); if (record.getR55_amt_name_of_sub5() != null) {
						 * cellG.setCellValue(record.getR55_amt_name_of_sub5().doubleValue()); } else {
						 * cellG.setCellValue(0); }
						 * 
						 * 
						 * 
						 * // row56 row = sheet.getRow(50);
						 * 
						 * 
						 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
						 * row.createCell(2); if (record.getR56_amt_name_of_sub1() != null) {
						 * cellC.setCellValue(record.getR56_amt_name_of_sub1().doubleValue()); } else {
						 * cellC.setCellValue(0); }
						 * 
						 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
						 * row.createCell(4); if (record.getR56_amt_name_of_sub2() != null) {
						 * cellD.setCellValue(record.getR56_amt_name_of_sub2().doubleValue()); } else {
						 * cellD.setCellValue(0); }
						 * 
						 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
						 * row.createCell(6); if (record.getR56_amt_name_of_sub3() != null) {
						 * cellE.setCellValue(record.getR56_amt_name_of_sub3().doubleValue()); } else {
						 * cellE.setCellValue(0); }
						 * 
						 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
						 * row.createCell(8); if (record.getR56_amt_name_of_sub4() != null) {
						 * cellF.setCellValue(record.getR56_amt_name_of_sub4().doubleValue()); } else {
						 * cellF.setCellValue(0); }
						 * 
						 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
						 * = row.createCell(10); if (record.getR56_amt_name_of_sub5() != null) {
						 * cellG.setCellValue(record.getR56_amt_name_of_sub5().doubleValue()); } else {
						 * cellG.setCellValue(0); }
						 * 
						 * 
						 * // row57 row = sheet.getRow(51);
						 * 
						 * 
						 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
						 * row.createCell(2); if (record.getR57_amt_name_of_sub1() != null) {
						 * cellC.setCellValue(record.getR57_amt_name_of_sub1().doubleValue()); } else {
						 * cellC.setCellValue(0); }
						 * 
						 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
						 * row.createCell(4); if (record.getR57_amt_name_of_sub2() != null) {
						 * cellD.setCellValue(record.getR57_amt_name_of_sub2().doubleValue()); } else {
						 * cellD.setCellValue(0); }
						 * 
						 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
						 * row.createCell(6); if (record.getR57_amt_name_of_sub3() != null) {
						 * cellE.setCellValue(record.getR57_amt_name_of_sub3().doubleValue()); } else {
						 * cellE.setCellValue(0); }
						 * 
						 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
						 * row.createCell(8); if (record.getR57_amt_name_of_sub4() != null) {
						 * cellF.setCellValue(record.getR57_amt_name_of_sub4().doubleValue()); } else {
						 * cellF.setCellValue(0); }
						 * 
						 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
						 * = row.createCell(10); if (record.getR57_amt_name_of_sub5() != null) {
						 * cellG.setCellValue(record.getR57_amt_name_of_sub5().doubleValue()); } else {
						 * cellG.setCellValue(0); }
						 * 
						 * 
						 * 
						 * // row58 row = sheet.getRow(52);
						 * 
						 * 
						 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
						 * row.createCell(2); if (record.getR58_amt_name_of_sub1() != null) {
						 * cellC.setCellValue(record.getR58_amt_name_of_sub1().doubleValue()); } else {
						 * cellC.setCellValue(0); }
						 * 
						 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
						 * row.createCell(4); if (record.getR58_amt_name_of_sub2() != null) {
						 * cellD.setCellValue(record.getR58_amt_name_of_sub2().doubleValue()); } else {
						 * cellD.setCellValue(0); }
						 * 
						 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
						 * row.createCell(6); if (record.getR58_amt_name_of_sub3() != null) {
						 * cellE.setCellValue(record.getR58_amt_name_of_sub3().doubleValue()); } else {
						 * cellE.setCellValue(0); }
						 * 
						 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
						 * row.createCell(8); if (record.getR58_amt_name_of_sub4() != null) {
						 * cellF.setCellValue(record.getR58_amt_name_of_sub4().doubleValue()); } else {
						 * cellF.setCellValue(0); }
						 * 
						 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
						 * = row.createCell(10); if (record.getR58_amt_name_of_sub5() != null) {
						 * cellG.setCellValue(record.getR58_amt_name_of_sub5().doubleValue()); } else {
						 * cellG.setCellValue(0); }
						 */

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
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA4 EMAIL SUMMARY", null,
							"BRRS_M_CA4_SUMMARYTABLE");
				}
				return out.toByteArray();
			}
		}
	}

	// Archival format excel
	public byte[] getExcelM_CA4ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_CA4ArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_CA4_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA4 report. Returning empty result.");
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
				SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");

				SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

				Date reportDateValue = inputFormat.parse(todate);

				// Set formatted date
				dateCell.setCellValue(outputFormat.format(reportDateValue));

				dateCell.setCellStyle(textStyle);

			} catch (ParseException e) {

				logger.error("Error parsing todate: {}", todate, e);
			}

			int startRow = 9;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_CA4_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

// row10

					// Column 2 - name of sub1
					Cell cellC = row.createCell(2);
					if (record.getR10_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR10_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					Cell cellD = row.createCell(3);
					if (record.getR10_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR10_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					Cell cellE = row.createCell(4);
					if (record.getR10_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR10_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					Cell cellF = row.createCell(5);
					if (record.getR10_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR10_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub4
					Cell cellG = row.createCell(6);
					if (record.getR10_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR10_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR11_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR11_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR11_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR11_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR11_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR11_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR11_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR11_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR11_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR11_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR13_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR13_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR13_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR13_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR13_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR13_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR13_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR13_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR13_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR13_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR14_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR14_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR14_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR14_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR14_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR14_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR14_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR14_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR14_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR14_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR17_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR17_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR17_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR17_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR17_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR17_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR17_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR17_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR17_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR17_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR18_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR18_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR18_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR18_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR18_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR18_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR18_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR18_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR18_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR18_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR19_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR19_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR19_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR19_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR19_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR19_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR19_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR19_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR19_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR19_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);

					// Column 3 - name of sub1
					cellC = row.createCell(2);
					if (record.getR22_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR22_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - name of sub2
					cellD = row.createCell(3);
					if (record.getR22_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR22_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 5 - name of sub3
					cellE = row.createCell(4);
					if (record.getR22_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR22_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 6 - name of sub4
					cellF = row.createCell(5);
					if (record.getR22_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR22_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 7 - name of sub5
					cellG = row.createCell(6);
					if (record.getR22_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR22_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR23_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR23_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR23_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR23_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR23_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR23_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR23_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR23_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR23_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR23_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR24_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR24_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR24_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR24_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR24_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR24_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR24_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR24_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR24_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR24_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR27_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR27_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR27_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR27_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR27_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR27_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR27_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR27_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR27_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR27_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// row28
					row = sheet.getRow(27);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR28_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR28_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR28_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR28_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR28_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR28_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR28_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR28_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR28_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR28_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);
					// Column 1 - item

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR29_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR29_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR29_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR29_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR29_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR29_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR29_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR29_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR29_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR29_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR32_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR32_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR32_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR32_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR32_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR32_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR32_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR32_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR32_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR32_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// row33
					row = sheet.getRow(32);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR33_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR33_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR33_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR33_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR33_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR33_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR33_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR33_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR33_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR33_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR33_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR33_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR33_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR33_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR33_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR33_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR33_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR33_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR33_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR33_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR34_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR34_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR34_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR34_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR34_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR34_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR34_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR34_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR34_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR34_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R36 ======================
					// row36
					row = sheet.getRow(35);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR36_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR36_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR36_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR36_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR36_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR36_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR36_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR36_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR36_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR36_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ====================== R37 ======================
					row = sheet.getRow(36);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR37_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR37_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR37_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR37_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR37_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR37_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR37_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR37_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR37_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR37_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R38 ======================
					row = sheet.getRow(37);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR38_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR38_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR38_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR38_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR38_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR38_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR38_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR38_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR38_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR38_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R39 ======================
					// row39
					row = sheet.getRow(38);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR39_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR39_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR39_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR39_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR39_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR39_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR39_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR39_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR39_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR39_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ====================== R40 ======================
					row = sheet.getRow(39);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR40_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR40_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR40_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR40_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR40_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR40_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR40_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR40_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR40_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR40_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R41
					// =========================
					row = sheet.getRow(40);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR41_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR41_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR41_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR41_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR41_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR41_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR41_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR41_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR41_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR41_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R42
					// =========================
					row = sheet.getRow(41);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR42_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR42_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR42_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR42_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR42_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR42_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR42_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR42_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR42_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR42_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R43
					// =========================
					row = sheet.getRow(42);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR43_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR43_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR43_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR43_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR43_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR43_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR43_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR43_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR43_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR43_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row44
					// row44
					row = sheet.getRow(43);
					if (row == null)
						row = sheet.createRow(43);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR44_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR44_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR44_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR44_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR44_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR44_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR44_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR44_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR44_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR44_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR44_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR44_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row45
					row = sheet.getRow(44);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR45_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR45_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR45_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR45_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR45_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR45_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR45_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR45_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR45_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR45_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ================= R46 =================
					// row46
					row = sheet.getRow(45);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR46_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR46_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR46_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR46_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR46_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR46_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR46_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR46_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR46_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR46_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ================= R47 =================
					// row47
					row = sheet.getRow(46);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR47_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR47_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR47_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR47_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR47_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR47_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR47_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR47_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR47_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR47_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ================= R48 =================
					row = sheet.getRow(47);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR48_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR48_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}
					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR48_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR48_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}
					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR48_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR48_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR48_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR48_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}
					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR48_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR48_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ================= R49 =================
					// row49
					row = sheet.getRow(48);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR49_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR49_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR49_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR49_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR49_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR49_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR49_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR49_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR49_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR49_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ================= R50 =================
					// row50
					row = sheet.getRow(49);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR50_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR50_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR50_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR50_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR50_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR50_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR50_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR50_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR50_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR50_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ====================== R51 ======================
					row = sheet.getRow(50);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR51_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR51_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR51_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR51_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR51_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR51_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR51_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR51_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR51_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR51_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R52 ======================
					// row52
					row = sheet.getRow(51);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR52_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR52_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR52_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR52_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR52_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR52_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR52_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR52_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR52_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR52_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR52_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR52_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// ====================== R53 ======================

					// row53
					row = sheet.getRow(52);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR53_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR53_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR53_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR53_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR53_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR53_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR53_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR53_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR53_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR53_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR53_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR53_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row54
					row = sheet.getRow(53);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR54_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR54_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR54_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR54_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR54_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR54_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR54_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR54_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR54_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR54_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR54_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR54_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row55
					row = sheet.getRow(54);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR55_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR55_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR55_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR55_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR55_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR55_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR55_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR55_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR55_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR55_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR55_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR55_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row56
					row = sheet.getRow(55);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR56_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR56_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR56_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR56_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR56_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR56_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR56_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR56_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR56_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR56_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR56_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR56_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row57
					row = sheet.getRow(56);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR57_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR57_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR57_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR57_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR57_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR57_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR57_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR57_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR57_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR57_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR57_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR57_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row58
					row = sheet.getRow(57);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR58_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR58_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR58_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR58_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR58_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR58_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR58_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR58_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR58_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR58_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA4 ARCHIVAL SUMMARY", null,
						"BRRS_M_CA4_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}

	}

	// Archival Email Excel
	public byte[] BRRS_M_CA4ArchivalEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_CA4_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_CA4 report. Returning empty result.");
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
				SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");

				SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

				Date reportDateValue = inputFormat.parse(todate);

				// Set formatted date
				dateCell.setCellValue(outputFormat.format(reportDateValue));

				dateCell.setCellStyle(textStyle);

			} catch (ParseException e) {

				logger.error("Error parsing todate: {}", todate, e);
			}

			int startRow = 9;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_CA4_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

// row10

					// Column 2 - name of sub1
					Cell cellC = row.createCell(2);
					if (record.getR10_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR10_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - name of sub2
					Cell cellD = row.createCell(4);
					if (record.getR10_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR10_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 6 - name of sub3
					Cell cellE = row.createCell(6);
					if (record.getR10_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR10_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 8 - name of sub4
					Cell cellF = row.createCell(8);
					if (record.getR10_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR10_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 10 - name of sub4
					Cell cellG = row.createCell(10);
					if (record.getR10_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR10_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 12 total
					cellG = row.createCell(12);
					if (record.getR10_tot_amt() != null) {
						cellG.setCellValue(record.getR10_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR11_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR11_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR11_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR11_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR11_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR11_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR11_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR11_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR11_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR11_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR11_tot_amt() != null) {
						cellG.setCellValue(record.getR11_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR13_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR13_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR13_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR13_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR13_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR13_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR13_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR13_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR13_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR13_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR13_tot_amt() != null) {
						cellG.setCellValue(record.getR13_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR14_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR14_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR14_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR14_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR14_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR14_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR14_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR14_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR14_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR14_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR14_tot_amt() != null) {
						cellG.setCellValue(record.getR14_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR17_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR17_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR17_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR17_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR17_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR17_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR17_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR17_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR17_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR17_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// COLUMN TOTAL
					cellG = row.getCell(12);
					if (cellG == null) {
						cellG = row.createCell(12);
					}

					if (record.getR17_tot_amt() != null) {
						cellG.setCellValue(record.getR17_tot_amt().doubleValue());
					} else {
						cellG.setCellValue("");
					}

					// row18
					row = sheet.getRow(17);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR18_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR18_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR18_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR18_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR18_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR18_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR18_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR18_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR18_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR18_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR18_tot_amt() != null) {
						cellG.setCellValue(record.getR18_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR19_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR19_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR19_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR19_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR19_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR19_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR19_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR19_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR19_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR19_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR19_tot_amt() != null) {
						cellG.setCellValue(record.getR19_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/*
					 * 
					 * // row22 row = sheet.getRow(21);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR22_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR22_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR22_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR22_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR22_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR22_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR22_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR22_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR22_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR22_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * //COLUMN TOTAL cellG = row.getCell(12); if (cellG == null) { cellG =
					 * row.createCell(12); }
					 * 
					 * if (record.getR22_tot_amt() != null) {
					 * cellG.setCellValue(record.getR22_tot_amt().doubleValue()); } else {
					 * cellG.setCellValue(""); }
					 * 
					 */

					// row23
					row = sheet.getRow(22);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR23_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR23_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR23_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR23_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR23_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR23_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR23_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR23_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR23_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR23_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR23_tot_amt() != null) {
						cellG.setCellValue(record.getR23_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR24_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR24_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR24_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR24_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR24_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR24_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR24_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR24_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR24_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR24_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR24_tot_amt() != null) {
						cellG.setCellValue(record.getR24_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(21);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR27_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR27_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR27_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR27_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR27_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR27_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR27_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR27_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR27_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR27_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					// COLUMN TOTAL
					cellG = row.getCell(12);
					if (cellG == null) {
						cellG = row.createCell(12);
					}

					if (record.getR27_tot_amt() != null) {
						cellG.setCellValue(record.getR27_tot_amt().doubleValue());
					} else {
						cellG.setCellValue("");
					}

					// row28
					row = sheet.getRow(22);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR28_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR28_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR28_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR28_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR28_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR28_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR28_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR28_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR28_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR28_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR28_tot_amt() != null) {
						cellG.setCellValue(record.getR28_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column 1 - item

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR29_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR29_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR29_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR29_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR29_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR29_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR29_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR29_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR29_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR29_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR29_tot_amt() != null) {
						cellG.setCellValue(record.getR29_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR32_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR32_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR32_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR32_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR32_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR32_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR32_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR32_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR32_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR32_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					// COLUMN TOTAL
					cellG = row.getCell(12);
					if (cellG == null) {
						cellG = row.createCell(12);
					}

					if (record.getR32_tot_amt() != null) {
						cellG.setCellValue(record.getR32_tot_amt().doubleValue());
					} else {
						cellG.setCellValue("");
					}

					// row33
					row = sheet.getRow(27);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR33_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR33_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR33_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR33_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR33_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR33_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR33_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR33_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR33_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR33_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR33_tot_amt() != null) {
						cellG.setCellValue(record.getR33_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(28);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR34_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR34_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR34_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR34_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR34_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR34_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR34_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR34_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR34_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR34_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR34_tot_amt() != null) {
						cellG.setCellValue(record.getR34_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R36 ======================
					// row36
					row = sheet.getRow(30);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR36_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR36_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR36_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR36_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR36_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR36_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR36_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR36_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR36_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR36_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ====================== R37 ======================
					row = sheet.getRow(31);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR37_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR37_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR37_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR37_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR37_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR37_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR37_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR37_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR37_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR37_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR37_tot_amt() != null) {
						cellG.setCellValue(record.getR37_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R38 ======================
					row = sheet.getRow(32);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR38_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR38_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR38_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR38_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR38_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR38_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR38_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR38_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR38_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR38_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}
					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR38_tot_amt() != null) {
						cellG.setCellValue(record.getR38_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R39 ======================
					// row39
					row = sheet.getRow(33);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR39_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR39_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR39_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR39_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR39_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR39_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR39_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR39_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR39_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR39_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ====================== R40 ======================
					row = sheet.getRow(34);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR40_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR40_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR40_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR40_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR40_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR40_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR40_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR40_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR40_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR40_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}
					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR40_tot_amt() != null) {
						cellG.setCellValue(record.getR40_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R41
					// =========================
					row = sheet.getRow(35);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR41_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR41_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR41_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR41_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR41_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR41_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR41_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR41_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR41_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR41_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR41_tot_amt() != null) {
						cellG.setCellValue(record.getR41_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R42
					// =========================
					row = sheet.getRow(36);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR42_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR42_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR42_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR42_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR42_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR42_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR42_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR42_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR42_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR42_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}
					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR42_tot_amt() != null) {
						cellG.setCellValue(record.getR42_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R43
					// =========================
					row = sheet.getRow(37);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR43_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR43_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR43_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR43_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR43_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR43_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR43_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR43_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR43_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR43_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// row44
					// row44
					row = sheet.getRow(38);
					if (row == null)
						row = sheet.createRow(43);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR44_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR44_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR44_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR44_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR44_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR44_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR44_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR44_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR44_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR44_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					/*
					 * // Column 7 - TOTAL cellH = row.createCell(12); if (record.getR44_tot_amt()
					 * != null) { cellH.setCellValue(record.getR44_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row45
					row = sheet.getRow(39);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR45_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR45_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR45_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR45_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR45_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR45_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR45_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR45_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR45_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR45_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR45_tot_amt() != null) {
						cellG.setCellValue(record.getR45_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ================= R46 =================
					// row46
					row = sheet.getRow(40);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR46_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR46_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR46_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR46_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR46_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR46_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR46_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR46_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR46_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR46_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ================= R47 =================
					// row47
					row = sheet.getRow(41);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR47_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR47_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR47_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR47_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR47_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR47_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR47_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR47_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR47_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR47_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ================= R48 =================
					row = sheet.getRow(42);
					/*
					 * // Column 2 - name of sub1 cellC = row.createCell(2); if
					 * (record.getR48_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR48_amt_name_of_sub1().doubleValue());
					 * cellC.setCellStyle(numberStyle); } else { cellC.setCellValue("");
					 * cellC.setCellStyle(textStyle); } // Column 3 - name of sub2 cellD =
					 * row.createCell(4); if (record.getR48_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR48_amt_name_of_sub2().doubleValue());
					 * cellD.setCellStyle(numberStyle); } else { cellD.setCellValue("");
					 * cellD.setCellStyle(textStyle); } // Column 4 - name of sub3 cellE =
					 * row.createCell(6); if (record.getR48_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR48_amt_name_of_sub3().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); } // Column 5 - name of sub4 cellF =
					 * row.createCell(8); if (record.getR48_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR48_amt_name_of_sub4().doubleValue());
					 * cellF.setCellStyle(numberStyle); } else { cellF.setCellValue("");
					 * cellF.setCellStyle(textStyle); } // Column 6 - name of sub5 cellG =
					 * row.createCell(10); if (record.getR48_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR48_amt_name_of_sub5().doubleValue());
					 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
					 * cellG.setCellStyle(textStyle); } // Column 7 total cellG =
					 * row.createCell(12); if (record.getR48_tot_amt() != null) {
					 * cellG.setCellValue(record.getR48_tot_amt().doubleValue());
					 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
					 * cellG.setCellStyle(textStyle); }
					 * 
					 * 
					 * 
					 * // ================= R49 ================= // row49 row = sheet.getRow(43);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR49_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR49_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR49_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR49_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR49_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR49_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR49_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR49_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR49_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR49_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * // ================= R50 ================= // row50 row = sheet.getRow(44);
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR50_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR50_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR50_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR50_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR50_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR50_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR50_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR50_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR50_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR50_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * // ====================== R51 ====================== row = sheet.getRow(45);
					 * 
					 * 
					 * // Column 2 - name of sub1 cellC = row.createCell(2); if
					 * (record.getR51_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR51_amt_name_of_sub1().doubleValue());
					 * cellC.setCellStyle(numberStyle); } else { cellC.setCellValue("");
					 * cellC.setCellStyle(textStyle); }
					 * 
					 * // Column 3 - name of sub2 cellD = row.createCell(4); if
					 * (record.getR51_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR51_amt_name_of_sub2().doubleValue());
					 * cellD.setCellStyle(numberStyle); } else { cellD.setCellValue("");
					 * cellD.setCellStyle(textStyle); }
					 * 
					 * // Column 4 - name of sub3 cellE = row.createCell(6); if
					 * (record.getR51_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR51_amt_name_of_sub3().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); }
					 * 
					 * // Column 5 - name of sub4 cellF = row.createCell(8); if
					 * (record.getR51_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR51_amt_name_of_sub4().doubleValue());
					 * cellF.setCellStyle(numberStyle); } else { cellF.setCellValue("");
					 * cellF.setCellStyle(textStyle); }
					 * 
					 * // Column 6 - name of sub5 cellG = row.createCell(10); if
					 * (record.getR51_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR51_amt_name_of_sub5().doubleValue());
					 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
					 * cellG.setCellStyle(textStyle); }
					 * 
					 * // Column 7 total cellG = row.createCell(12); if (record.getR51_tot_amt() !=
					 * null) { cellG.setCellValue(record.getR51_tot_amt().doubleValue());
					 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
					 * cellG.setCellStyle(textStyle); }
					 * 
					 * 
					 * 
					 * // ====================== R52 ====================== // row52 row =
					 * sheet.getRow(46);
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR52_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR52_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR52_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR52_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR52_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR52_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR52_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR52_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR52_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR52_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 */

					// ====================== R53 ======================

					// row53
					row = sheet.getRow(44);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR53_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR53_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR53_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR53_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR53_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR53_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR53_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR53_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR53_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR53_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// row54
					row = sheet.getRow(45);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR54_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR54_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR54_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR54_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR54_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR54_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR54_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR54_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR54_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR54_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					/*
					 * 
					 * 
					 * // row55 row = sheet.getRow(49);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR55_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR55_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR55_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR55_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR55_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR55_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR55_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR55_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR55_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR55_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * 
					 * // row56 row = sheet.getRow(50);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR56_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR56_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR56_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR56_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR56_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR56_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR56_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR56_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR56_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR56_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * // row57 row = sheet.getRow(51);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR57_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR57_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR57_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR57_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR57_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR57_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR57_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR57_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR57_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR57_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * 
					 * // row58 row = sheet.getRow(52);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR58_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR58_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR58_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR58_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR58_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR58_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR58_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR58_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR58_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR58_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 */

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA4 EMAIL ARCHIVAL SUMMARY", null,
						"BRRS_M_CA4_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	// Resub Format excel
	public byte[] BRRS_M_CA4ResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_CA4ResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_CA4_Resub_Summary_Entity> dataList = getResubSummaryByDateAndVersion(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA4 report. Returning empty result.");
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
				SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");

				SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

				Date reportDateValue = inputFormat.parse(todate);

				// Set formatted date
				dateCell.setCellValue(outputFormat.format(reportDateValue));

				dateCell.setCellStyle(textStyle);

			} catch (ParseException e) {

				logger.error("Error parsing todate: {}", todate, e);
			}

			int startRow = 9;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_CA4_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row10

					// Column 2 - name of sub1
					Cell cellC = row.createCell(2);
					if (record.getR10_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR10_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					Cell cellD = row.createCell(3);
					if (record.getR10_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR10_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					Cell cellE = row.createCell(4);
					if (record.getR10_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR10_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					Cell cellF = row.createCell(5);
					if (record.getR10_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR10_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub4
					Cell cellG = row.createCell(6);
					if (record.getR10_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR10_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(7);
					if (record.getR10_tot_amt() != null) {
						cellG.setCellValue(record.getR10_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR11_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR11_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR11_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR11_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR11_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR11_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR11_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR11_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR11_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR11_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(7);
					if (record.getR11_tot_amt() != null) {
						cellG.setCellValue(record.getR11_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR13_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR13_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR13_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR13_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR13_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR13_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR13_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR13_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR13_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR13_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR14_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR14_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR14_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR14_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR14_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR14_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR14_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR14_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR14_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR14_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR17_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR17_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR17_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR17_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR17_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR17_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR17_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR17_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR17_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR17_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR18_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR18_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR18_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR18_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR18_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR18_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR18_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR18_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR18_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR18_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR19_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR19_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR19_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR19_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR19_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR19_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR19_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR19_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR19_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR19_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);

					// Column 3 - name of sub1
					cellC = row.createCell(2);
					if (record.getR22_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR22_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - name of sub2
					cellD = row.createCell(3);
					if (record.getR22_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR22_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 5 - name of sub3
					cellE = row.createCell(4);
					if (record.getR22_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR22_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 6 - name of sub4
					cellF = row.createCell(5);
					if (record.getR22_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR22_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 7 - name of sub5
					cellG = row.createCell(6);
					if (record.getR22_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR22_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR23_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR23_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR23_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR23_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR23_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR23_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR23_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR23_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR23_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR23_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR24_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR24_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR24_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR24_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR24_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR24_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR24_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR24_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR24_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR24_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR27_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR27_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR27_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR27_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR27_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR27_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR27_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR27_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR27_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR27_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// row28
					row = sheet.getRow(27);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR28_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR28_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR28_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR28_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR28_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR28_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR28_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR28_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR28_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR28_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);
					// Column 1 - item

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR29_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR29_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR29_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR29_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR29_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR29_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR29_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR29_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR29_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR29_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR32_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR32_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR32_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR32_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR32_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR32_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR32_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR32_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR32_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR32_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// row33
					row = sheet.getRow(32);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR33_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR33_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR33_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR33_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR33_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR33_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR33_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR33_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR33_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR33_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR33_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR33_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR33_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR33_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR33_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR33_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR33_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR33_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR33_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR33_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR34_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR34_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR34_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR34_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR34_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR34_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR34_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR34_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR34_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR34_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R36 ======================
					// row36
					row = sheet.getRow(35);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR36_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR36_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR36_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR36_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR36_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR36_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR36_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR36_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR36_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR36_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ====================== R37 ======================
					row = sheet.getRow(36);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR37_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR37_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR37_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR37_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR37_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR37_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR37_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR37_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR37_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR37_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R38 ======================
					row = sheet.getRow(37);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR38_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR38_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR38_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR38_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR38_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR38_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR38_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR38_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR38_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR38_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R39 ======================
					// row39
					row = sheet.getRow(38);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR39_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR39_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR39_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR39_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR39_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR39_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR39_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR39_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR39_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR39_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ====================== R40 ======================
					row = sheet.getRow(39);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR40_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR40_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR40_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR40_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR40_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR40_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR40_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR40_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR40_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR40_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R41
					// =========================
					row = sheet.getRow(40);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR41_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR41_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR41_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR41_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR41_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR41_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR41_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR41_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR41_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR41_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R42
					// =========================
					row = sheet.getRow(41);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR42_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR42_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR42_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR42_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR42_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR42_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR42_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR42_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR42_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR42_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R43
					// =========================
					row = sheet.getRow(42);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR43_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR43_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR43_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR43_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR43_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR43_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR43_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR43_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR43_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR43_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// row44
					// row44
					row = sheet.getRow(43);
					if (row == null)
						row = sheet.createRow(43);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR44_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR44_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR44_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR44_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR44_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR44_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR44_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR44_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR44_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR44_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR44_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR44_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row45
					row = sheet.getRow(44);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR45_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR45_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR45_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR45_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR45_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR45_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR45_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR45_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR45_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR45_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ================= R46 =================
					// row46
					row = sheet.getRow(45);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR46_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR46_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR46_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR46_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR46_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR46_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR46_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR46_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR46_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR46_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ================= R47 =================
					// row47
					row = sheet.getRow(46);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR47_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR47_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR47_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR47_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR47_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR47_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR47_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR47_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR47_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR47_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ================= R48 =================
					row = sheet.getRow(47);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR48_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR48_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}
					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR48_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR48_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}
					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR48_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR48_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR48_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR48_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}
					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR48_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR48_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ================= R49 =================
					// row49
					row = sheet.getRow(48);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR49_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR49_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR49_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR49_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR49_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR49_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR49_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR49_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR49_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR49_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ================= R50 =================
					// row50
					row = sheet.getRow(49);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR50_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR50_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR50_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR50_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR50_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR50_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR50_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR50_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR50_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR50_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ====================== R51 ======================
					row = sheet.getRow(50);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR51_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR51_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR51_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR51_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR51_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR51_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR51_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR51_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR51_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR51_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R52 ======================
					// row52
					row = sheet.getRow(51);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR52_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR52_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR52_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR52_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR52_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR52_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR52_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR52_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR52_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR52_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR52_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR52_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// ====================== R53 ======================

					// row53
					row = sheet.getRow(52);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR53_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR53_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR53_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR53_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR53_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR53_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR53_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR53_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR53_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR53_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR53_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR53_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row54
					row = sheet.getRow(53);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR54_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR54_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR54_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR54_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR54_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR54_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR54_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR54_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR54_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR54_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR54_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR54_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row55
					row = sheet.getRow(54);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR55_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR55_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR55_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR55_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR55_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR55_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR55_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR55_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR55_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR55_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR55_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR55_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row56
					row = sheet.getRow(55);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR56_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR56_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR56_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR56_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR56_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR56_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR56_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR56_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR56_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR56_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR56_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR56_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row57
					row = sheet.getRow(56);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR57_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR57_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR57_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR57_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR57_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR57_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR57_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR57_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR57_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR57_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR57_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR57_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row58
					row = sheet.getRow(57);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR58_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR58_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR58_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR58_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR58_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR58_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR58_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR58_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR58_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR58_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA4 RESUB SUMMARY", null,
						"BRRS_M_CA4_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}

	}

	// Resub Email Excel
	public byte[] BRRS_M_CA4ResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_CA4_Resub_Summary_Entity> dataList = getResubSummaryByDateAndVersion(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_CA4 report. Returning empty result.");
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
				SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");

				SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

				Date reportDateValue = inputFormat.parse(todate);

				// Set formatted date
				dateCell.setCellValue(outputFormat.format(reportDateValue));

				dateCell.setCellStyle(textStyle);

			} catch (ParseException e) {

				logger.error("Error parsing todate: {}", todate, e);
			}

			int startRow = 9;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_CA4_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

// row10

					// Column 2 - name of sub1
					Cell cellC = row.createCell(2);
					if (record.getR10_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR10_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - name of sub2
					Cell cellD = row.createCell(4);
					if (record.getR10_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR10_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 6 - name of sub3
					Cell cellE = row.createCell(6);
					if (record.getR10_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR10_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 8 - name of sub4
					Cell cellF = row.createCell(8);
					if (record.getR10_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR10_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 10 - name of sub4
					Cell cellG = row.createCell(10);
					if (record.getR10_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR10_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 12 total
					cellG = row.createCell(12);
					if (record.getR10_tot_amt() != null) {
						cellG.setCellValue(record.getR10_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR11_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR11_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR11_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR11_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR11_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR11_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR11_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR11_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR11_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR11_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR11_tot_amt() != null) {
						cellG.setCellValue(record.getR11_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR13_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR13_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR13_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR13_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR13_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR13_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR13_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR13_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR13_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR13_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR13_tot_amt() != null) {
						cellG.setCellValue(record.getR13_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR14_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR14_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR14_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR14_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR14_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR14_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR14_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR14_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR14_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR14_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR14_tot_amt() != null) {
						cellG.setCellValue(record.getR14_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR17_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR17_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR17_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR17_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR17_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR17_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR17_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR17_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR17_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR17_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// COLUMN TOTAL
					cellG = row.getCell(12);
					if (cellG == null) {
						cellG = row.createCell(12);
					}

					if (record.getR17_tot_amt() != null) {
						cellG.setCellValue(record.getR17_tot_amt().doubleValue());
					} else {
						cellG.setCellValue("");
					}

					// row18
					row = sheet.getRow(17);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR18_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR18_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR18_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR18_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR18_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR18_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR18_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR18_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR18_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR18_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR18_tot_amt() != null) {
						cellG.setCellValue(record.getR18_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR19_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR19_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR19_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR19_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR19_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR19_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR19_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR19_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR19_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR19_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR19_tot_amt() != null) {
						cellG.setCellValue(record.getR19_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/*
					 * 
					 * // row22 row = sheet.getRow(21);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR22_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR22_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR22_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR22_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR22_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR22_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR22_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR22_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR22_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR22_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * //COLUMN TOTAL cellG = row.getCell(12); if (cellG == null) { cellG =
					 * row.createCell(12); }
					 * 
					 * if (record.getR22_tot_amt() != null) {
					 * cellG.setCellValue(record.getR22_tot_amt().doubleValue()); } else {
					 * cellG.setCellValue(""); }
					 * 
					 */

					// row23
					row = sheet.getRow(22);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR23_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR23_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR23_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR23_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR23_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR23_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR23_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR23_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR23_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR23_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR23_tot_amt() != null) {
						cellG.setCellValue(record.getR23_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR24_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR24_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR24_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR24_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR24_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR24_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR24_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR24_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR24_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR24_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR24_tot_amt() != null) {
						cellG.setCellValue(record.getR24_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(21);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR27_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR27_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR27_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR27_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR27_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR27_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR27_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR27_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR27_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR27_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					// COLUMN TOTAL
					cellG = row.getCell(12);
					if (cellG == null) {
						cellG = row.createCell(12);
					}

					if (record.getR27_tot_amt() != null) {
						cellG.setCellValue(record.getR27_tot_amt().doubleValue());
					} else {
						cellG.setCellValue("");
					}

					// row28
					row = sheet.getRow(22);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR28_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR28_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR28_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR28_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR28_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR28_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR28_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR28_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR28_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR28_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR28_tot_amt() != null) {
						cellG.setCellValue(record.getR28_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column 1 - item

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR29_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR29_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR29_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR29_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR29_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR29_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR29_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR29_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR29_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR29_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR29_tot_amt() != null) {
						cellG.setCellValue(record.getR29_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR32_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR32_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR32_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR32_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR32_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR32_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR32_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR32_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR32_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR32_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					// COLUMN TOTAL
					cellG = row.getCell(12);
					if (cellG == null) {
						cellG = row.createCell(12);
					}

					if (record.getR32_tot_amt() != null) {
						cellG.setCellValue(record.getR32_tot_amt().doubleValue());
					} else {
						cellG.setCellValue("");
					}

					// row33
					row = sheet.getRow(27);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR33_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR33_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR33_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR33_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR33_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR33_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR33_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR33_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR33_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR33_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR33_tot_amt() != null) {
						cellG.setCellValue(record.getR33_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(28);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR34_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR34_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR34_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR34_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR34_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR34_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR34_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR34_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR34_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR34_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR34_tot_amt() != null) {
						cellG.setCellValue(record.getR34_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R36 ======================
					// row36
					row = sheet.getRow(30);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR36_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR36_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR36_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR36_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR36_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR36_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR36_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR36_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR36_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR36_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ====================== R37 ======================
					row = sheet.getRow(31);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR37_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR37_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR37_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR37_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR37_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR37_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR37_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR37_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR37_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR37_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR37_tot_amt() != null) {
						cellG.setCellValue(record.getR37_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R38 ======================
					row = sheet.getRow(32);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR38_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR38_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR38_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR38_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR38_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR38_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR38_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR38_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR38_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR38_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}
					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR38_tot_amt() != null) {
						cellG.setCellValue(record.getR38_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R39 ======================
					// row39
					row = sheet.getRow(33);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR39_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR39_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR39_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR39_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR39_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR39_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR39_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR39_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR39_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR39_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ====================== R40 ======================
					row = sheet.getRow(34);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR40_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR40_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR40_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR40_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR40_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR40_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR40_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR40_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR40_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR40_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}
					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR40_tot_amt() != null) {
						cellG.setCellValue(record.getR40_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R41
					// =========================
					row = sheet.getRow(35);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR41_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR41_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR41_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR41_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR41_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR41_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR41_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR41_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR41_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR41_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR41_tot_amt() != null) {
						cellG.setCellValue(record.getR41_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R42
					// =========================
					row = sheet.getRow(36);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR42_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR42_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR42_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR42_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR42_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR42_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR42_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR42_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR42_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR42_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}
					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR42_tot_amt() != null) {
						cellG.setCellValue(record.getR42_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R43
					// =========================
					row = sheet.getRow(37);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR43_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR43_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR43_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR43_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR43_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR43_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR43_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR43_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR43_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR43_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// row44
					// row44
					row = sheet.getRow(38);
					if (row == null)
						row = sheet.createRow(43);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR44_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR44_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR44_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR44_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR44_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR44_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR44_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR44_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR44_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR44_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					/*
					 * // Column 7 - TOTAL cellH = row.createCell(12); if (record.getR44_tot_amt()
					 * != null) { cellH.setCellValue(record.getR44_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row45
					row = sheet.getRow(39);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR45_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR45_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR45_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR45_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR45_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR45_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR45_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR45_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR45_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR45_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR45_tot_amt() != null) {
						cellG.setCellValue(record.getR45_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ================= R46 =================
					// row46
					row = sheet.getRow(40);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR46_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR46_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR46_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR46_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR46_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR46_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR46_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR46_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR46_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR46_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ================= R47 =================
					// row47
					row = sheet.getRow(41);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR47_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR47_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR47_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR47_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR47_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR47_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR47_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR47_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR47_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR47_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ================= R48 =================
					row = sheet.getRow(42);
					/*
					 * // Column 2 - name of sub1 cellC = row.createCell(2); if
					 * (record.getR48_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR48_amt_name_of_sub1().doubleValue());
					 * cellC.setCellStyle(numberStyle); } else { cellC.setCellValue("");
					 * cellC.setCellStyle(textStyle); } // Column 3 - name of sub2 cellD =
					 * row.createCell(4); if (record.getR48_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR48_amt_name_of_sub2().doubleValue());
					 * cellD.setCellStyle(numberStyle); } else { cellD.setCellValue("");
					 * cellD.setCellStyle(textStyle); } // Column 4 - name of sub3 cellE =
					 * row.createCell(6); if (record.getR48_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR48_amt_name_of_sub3().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); } // Column 5 - name of sub4 cellF =
					 * row.createCell(8); if (record.getR48_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR48_amt_name_of_sub4().doubleValue());
					 * cellF.setCellStyle(numberStyle); } else { cellF.setCellValue("");
					 * cellF.setCellStyle(textStyle); } // Column 6 - name of sub5 cellG =
					 * row.createCell(10); if (record.getR48_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR48_amt_name_of_sub5().doubleValue());
					 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
					 * cellG.setCellStyle(textStyle); } // Column 7 total cellG =
					 * row.createCell(12); if (record.getR48_tot_amt() != null) {
					 * cellG.setCellValue(record.getR48_tot_amt().doubleValue());
					 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
					 * cellG.setCellStyle(textStyle); }
					 * 
					 * 
					 * 
					 * // ================= R49 ================= // row49 row = sheet.getRow(43);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR49_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR49_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR49_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR49_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR49_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR49_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR49_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR49_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR49_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR49_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * // ================= R50 ================= // row50 row = sheet.getRow(44);
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR50_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR50_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR50_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR50_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR50_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR50_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR50_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR50_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR50_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR50_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * // ====================== R51 ====================== row = sheet.getRow(45);
					 * 
					 * 
					 * // Column 2 - name of sub1 cellC = row.createCell(2); if
					 * (record.getR51_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR51_amt_name_of_sub1().doubleValue());
					 * cellC.setCellStyle(numberStyle); } else { cellC.setCellValue("");
					 * cellC.setCellStyle(textStyle); }
					 * 
					 * // Column 3 - name of sub2 cellD = row.createCell(4); if
					 * (record.getR51_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR51_amt_name_of_sub2().doubleValue());
					 * cellD.setCellStyle(numberStyle); } else { cellD.setCellValue("");
					 * cellD.setCellStyle(textStyle); }
					 * 
					 * // Column 4 - name of sub3 cellE = row.createCell(6); if
					 * (record.getR51_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR51_amt_name_of_sub3().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); }
					 * 
					 * // Column 5 - name of sub4 cellF = row.createCell(8); if
					 * (record.getR51_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR51_amt_name_of_sub4().doubleValue());
					 * cellF.setCellStyle(numberStyle); } else { cellF.setCellValue("");
					 * cellF.setCellStyle(textStyle); }
					 * 
					 * // Column 6 - name of sub5 cellG = row.createCell(10); if
					 * (record.getR51_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR51_amt_name_of_sub5().doubleValue());
					 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
					 * cellG.setCellStyle(textStyle); }
					 * 
					 * // Column 7 total cellG = row.createCell(12); if (record.getR51_tot_amt() !=
					 * null) { cellG.setCellValue(record.getR51_tot_amt().doubleValue());
					 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
					 * cellG.setCellStyle(textStyle); }
					 * 
					 * 
					 * 
					 * // ====================== R52 ====================== // row52 row =
					 * sheet.getRow(46);
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR52_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR52_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR52_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR52_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR52_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR52_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR52_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR52_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR52_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR52_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 */

					// ====================== R53 ======================

					// row53
					row = sheet.getRow(44);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR53_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR53_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR53_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR53_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR53_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR53_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR53_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR53_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR53_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR53_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// row54
					row = sheet.getRow(45);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR54_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR54_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR54_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR54_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR54_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR54_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR54_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR54_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR54_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR54_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					/*
					 * 
					 * 
					 * // row55 row = sheet.getRow(49);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR55_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR55_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR55_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR55_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR55_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR55_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR55_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR55_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR55_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR55_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * 
					 * // row56 row = sheet.getRow(50);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR56_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR56_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR56_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR56_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR56_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR56_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR56_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR56_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR56_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR56_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * // row57 row = sheet.getRow(51);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR57_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR57_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR57_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR57_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR57_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR57_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR57_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR57_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR57_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR57_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * 
					 * // row58 row = sheet.getRow(52);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR58_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR58_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR58_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR58_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR58_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR58_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR58_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR58_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR58_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR58_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 */

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA4 EMAIL RESUB SUMMARY", null,
						"BRRS_M_CA4_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}

	}

	class M_CA4SummaryRowMapper implements RowMapper<M_CA4_Summary_Entity> {
		@Override
		public M_CA4_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA4_Summary_Entity obj = new M_CA4_Summary_Entity();

			// R10
			obj.setR10_item(rs.getString("R10_ITEM"));
			obj.setR10_amt_name_of_sub1(rs.getBigDecimal("R10_AMT_NAME_OF_SUB1"));
			obj.setR10_amt_name_of_sub2(rs.getBigDecimal("R10_AMT_NAME_OF_SUB2"));
			obj.setR10_amt_name_of_sub3(rs.getBigDecimal("R10_AMT_NAME_OF_SUB3"));
			obj.setR10_amt_name_of_sub4(rs.getBigDecimal("R10_AMT_NAME_OF_SUB4"));
			obj.setR10_amt_name_of_sub5(rs.getBigDecimal("R10_AMT_NAME_OF_SUB5"));
			obj.setR10_tot_amt(rs.getBigDecimal("R10_TOT_AMT"));

			// R11
			obj.setR11_item(rs.getString("R11_ITEM"));
			obj.setR11_amt_name_of_sub1(rs.getBigDecimal("R11_AMT_NAME_OF_SUB1"));
			obj.setR11_amt_name_of_sub2(rs.getBigDecimal("R11_AMT_NAME_OF_SUB2"));
			obj.setR11_amt_name_of_sub3(rs.getBigDecimal("R11_AMT_NAME_OF_SUB3"));
			obj.setR11_amt_name_of_sub4(rs.getBigDecimal("R11_AMT_NAME_OF_SUB4"));
			obj.setR11_amt_name_of_sub5(rs.getBigDecimal("R11_AMT_NAME_OF_SUB5"));
			obj.setR11_tot_amt(rs.getBigDecimal("R11_TOT_AMT"));

			// R12
			obj.setR12_item(rs.getString("R12_ITEM"));
			obj.setR12_amt_name_of_sub1(rs.getBigDecimal("R12_AMT_NAME_OF_SUB1"));
			obj.setR12_amt_name_of_sub2(rs.getBigDecimal("R12_AMT_NAME_OF_SUB2"));
			obj.setR12_amt_name_of_sub3(rs.getBigDecimal("R12_AMT_NAME_OF_SUB3"));
			obj.setR12_amt_name_of_sub4(rs.getBigDecimal("R12_AMT_NAME_OF_SUB4"));
			obj.setR12_amt_name_of_sub5(rs.getBigDecimal("R12_AMT_NAME_OF_SUB5"));
			obj.setR12_tot_amt(rs.getBigDecimal("R12_TOT_AMT"));

			// R13
			obj.setR13_item(rs.getString("R13_ITEM"));
			obj.setR13_amt_name_of_sub1(rs.getBigDecimal("R13_AMT_NAME_OF_SUB1"));
			obj.setR13_amt_name_of_sub2(rs.getBigDecimal("R13_AMT_NAME_OF_SUB2"));
			obj.setR13_amt_name_of_sub3(rs.getBigDecimal("R13_AMT_NAME_OF_SUB3"));
			obj.setR13_amt_name_of_sub4(rs.getBigDecimal("R13_AMT_NAME_OF_SUB4"));
			obj.setR13_amt_name_of_sub5(rs.getBigDecimal("R13_AMT_NAME_OF_SUB5"));
			obj.setR13_tot_amt(rs.getBigDecimal("R13_TOT_AMT"));

			// R14
			obj.setR14_item(rs.getString("R14_ITEM"));
			obj.setR14_amt_name_of_sub1(rs.getBigDecimal("R14_AMT_NAME_OF_SUB1"));
			obj.setR14_amt_name_of_sub2(rs.getBigDecimal("R14_AMT_NAME_OF_SUB2"));
			obj.setR14_amt_name_of_sub3(rs.getBigDecimal("R14_AMT_NAME_OF_SUB3"));
			obj.setR14_amt_name_of_sub4(rs.getBigDecimal("R14_AMT_NAME_OF_SUB4"));
			obj.setR14_amt_name_of_sub5(rs.getBigDecimal("R14_AMT_NAME_OF_SUB5"));
			obj.setR14_tot_amt(rs.getBigDecimal("R14_TOT_AMT"));

			// R17 (R15/R16 skipped — not in entity)
			obj.setR17_item(rs.getString("R17_ITEM"));
			obj.setR17_amt_name_of_sub1(rs.getBigDecimal("R17_AMT_NAME_OF_SUB1"));
			obj.setR17_amt_name_of_sub2(rs.getBigDecimal("R17_AMT_NAME_OF_SUB2"));
			obj.setR17_amt_name_of_sub3(rs.getBigDecimal("R17_AMT_NAME_OF_SUB3"));
			obj.setR17_amt_name_of_sub4(rs.getBigDecimal("R17_AMT_NAME_OF_SUB4"));
			obj.setR17_amt_name_of_sub5(rs.getBigDecimal("R17_AMT_NAME_OF_SUB5"));
			obj.setR17_tot_amt(rs.getBigDecimal("R17_TOT_AMT"));

			// R18
			obj.setR18_item(rs.getString("R18_ITEM"));
			obj.setR18_amt_name_of_sub1(rs.getBigDecimal("R18_AMT_NAME_OF_SUB1"));
			obj.setR18_amt_name_of_sub2(rs.getBigDecimal("R18_AMT_NAME_OF_SUB2"));
			obj.setR18_amt_name_of_sub3(rs.getBigDecimal("R18_AMT_NAME_OF_SUB3"));
			obj.setR18_amt_name_of_sub4(rs.getBigDecimal("R18_AMT_NAME_OF_SUB4"));
			obj.setR18_amt_name_of_sub5(rs.getBigDecimal("R18_AMT_NAME_OF_SUB5"));
			obj.setR18_tot_amt(rs.getBigDecimal("R18_TOT_AMT"));

			// R19
			obj.setR19_item(rs.getString("R19_ITEM"));
			obj.setR19_amt_name_of_sub1(rs.getBigDecimal("R19_AMT_NAME_OF_SUB1"));
			obj.setR19_amt_name_of_sub2(rs.getBigDecimal("R19_AMT_NAME_OF_SUB2"));
			obj.setR19_amt_name_of_sub3(rs.getBigDecimal("R19_AMT_NAME_OF_SUB3"));
			obj.setR19_amt_name_of_sub4(rs.getBigDecimal("R19_AMT_NAME_OF_SUB4"));
			obj.setR19_amt_name_of_sub5(rs.getBigDecimal("R19_AMT_NAME_OF_SUB5"));
			obj.setR19_tot_amt(rs.getBigDecimal("R19_TOT_AMT"));

			// R22 (R20/R21 skipped)
			obj.setR22_item(rs.getString("R22_ITEM"));
			obj.setR22_amt_name_of_sub1(rs.getBigDecimal("R22_AMT_NAME_OF_SUB1"));
			obj.setR22_amt_name_of_sub2(rs.getBigDecimal("R22_AMT_NAME_OF_SUB2"));
			obj.setR22_amt_name_of_sub3(rs.getBigDecimal("R22_AMT_NAME_OF_SUB3"));
			obj.setR22_amt_name_of_sub4(rs.getBigDecimal("R22_AMT_NAME_OF_SUB4"));
			obj.setR22_amt_name_of_sub5(rs.getBigDecimal("R22_AMT_NAME_OF_SUB5"));
			obj.setR22_tot_amt(rs.getBigDecimal("R22_TOT_AMT"));

			// R23
			obj.setR23_item(rs.getString("R23_ITEM"));
			obj.setR23_amt_name_of_sub1(rs.getBigDecimal("R23_AMT_NAME_OF_SUB1"));
			obj.setR23_amt_name_of_sub2(rs.getBigDecimal("R23_AMT_NAME_OF_SUB2"));
			obj.setR23_amt_name_of_sub3(rs.getBigDecimal("R23_AMT_NAME_OF_SUB3"));
			obj.setR23_amt_name_of_sub4(rs.getBigDecimal("R23_AMT_NAME_OF_SUB4"));
			obj.setR23_amt_name_of_sub5(rs.getBigDecimal("R23_AMT_NAME_OF_SUB5"));
			obj.setR23_tot_amt(rs.getBigDecimal("R23_TOT_AMT"));

			// R24
			obj.setR24_item(rs.getString("R24_ITEM"));
			obj.setR24_amt_name_of_sub1(rs.getBigDecimal("R24_AMT_NAME_OF_SUB1"));
			obj.setR24_amt_name_of_sub2(rs.getBigDecimal("R24_AMT_NAME_OF_SUB2"));
			obj.setR24_amt_name_of_sub3(rs.getBigDecimal("R24_AMT_NAME_OF_SUB3"));
			obj.setR24_amt_name_of_sub4(rs.getBigDecimal("R24_AMT_NAME_OF_SUB4"));
			obj.setR24_amt_name_of_sub5(rs.getBigDecimal("R24_AMT_NAME_OF_SUB5"));
			obj.setR24_tot_amt(rs.getBigDecimal("R24_TOT_AMT"));

			// R27 (R25/R26 skipped)
			obj.setR27_item(rs.getString("R27_ITEM"));
			obj.setR27_amt_name_of_sub1(rs.getBigDecimal("R27_AMT_NAME_OF_SUB1"));
			obj.setR27_amt_name_of_sub2(rs.getBigDecimal("R27_AMT_NAME_OF_SUB2"));
			obj.setR27_amt_name_of_sub3(rs.getBigDecimal("R27_AMT_NAME_OF_SUB3"));
			obj.setR27_amt_name_of_sub4(rs.getBigDecimal("R27_AMT_NAME_OF_SUB4"));
			obj.setR27_amt_name_of_sub5(rs.getBigDecimal("R27_AMT_NAME_OF_SUB5"));
			obj.setR27_tot_amt(rs.getBigDecimal("R27_TOT_AMT"));

			// R28
			obj.setR28_item(rs.getString("R28_ITEM"));
			obj.setR28_amt_name_of_sub1(rs.getBigDecimal("R28_AMT_NAME_OF_SUB1"));
			obj.setR28_amt_name_of_sub2(rs.getBigDecimal("R28_AMT_NAME_OF_SUB2"));
			obj.setR28_amt_name_of_sub3(rs.getBigDecimal("R28_AMT_NAME_OF_SUB3"));
			obj.setR28_amt_name_of_sub4(rs.getBigDecimal("R28_AMT_NAME_OF_SUB4"));
			obj.setR28_amt_name_of_sub5(rs.getBigDecimal("R28_AMT_NAME_OF_SUB5"));
			obj.setR28_tot_amt(rs.getBigDecimal("R28_TOT_AMT"));

			// R29
			obj.setR29_item(rs.getString("R29_ITEM"));
			obj.setR29_amt_name_of_sub1(rs.getBigDecimal("R29_AMT_NAME_OF_SUB1"));
			obj.setR29_amt_name_of_sub2(rs.getBigDecimal("R29_AMT_NAME_OF_SUB2"));
			obj.setR29_amt_name_of_sub3(rs.getBigDecimal("R29_AMT_NAME_OF_SUB3"));
			obj.setR29_amt_name_of_sub4(rs.getBigDecimal("R29_AMT_NAME_OF_SUB4"));
			obj.setR29_amt_name_of_sub5(rs.getBigDecimal("R29_AMT_NAME_OF_SUB5"));
			obj.setR29_tot_amt(rs.getBigDecimal("R29_TOT_AMT"));

			// R32 (R30/R31 skipped)
			obj.setR32_item(rs.getString("R32_ITEM"));
			obj.setR32_amt_name_of_sub1(rs.getBigDecimal("R32_AMT_NAME_OF_SUB1"));
			obj.setR32_amt_name_of_sub2(rs.getBigDecimal("R32_AMT_NAME_OF_SUB2"));
			obj.setR32_amt_name_of_sub3(rs.getBigDecimal("R32_AMT_NAME_OF_SUB3"));
			obj.setR32_amt_name_of_sub4(rs.getBigDecimal("R32_AMT_NAME_OF_SUB4"));
			obj.setR32_amt_name_of_sub5(rs.getBigDecimal("R32_AMT_NAME_OF_SUB5"));
			obj.setR32_tot_amt(rs.getBigDecimal("R32_TOT_AMT"));

			// R33
			obj.setR33_item(rs.getString("R33_ITEM"));
			obj.setR33_amt_name_of_sub1(rs.getBigDecimal("R33_AMT_NAME_OF_SUB1"));
			obj.setR33_amt_name_of_sub2(rs.getBigDecimal("R33_AMT_NAME_OF_SUB2"));
			obj.setR33_amt_name_of_sub3(rs.getBigDecimal("R33_AMT_NAME_OF_SUB3"));
			obj.setR33_amt_name_of_sub4(rs.getBigDecimal("R33_AMT_NAME_OF_SUB4"));
			obj.setR33_amt_name_of_sub5(rs.getBigDecimal("R33_AMT_NAME_OF_SUB5"));
			obj.setR33_tot_amt(rs.getBigDecimal("R33_TOT_AMT"));

			// R34
			obj.setR34_item(rs.getString("R34_ITEM"));
			obj.setR34_amt_name_of_sub1(rs.getBigDecimal("R34_AMT_NAME_OF_SUB1"));
			obj.setR34_amt_name_of_sub2(rs.getBigDecimal("R34_AMT_NAME_OF_SUB2"));
			obj.setR34_amt_name_of_sub3(rs.getBigDecimal("R34_AMT_NAME_OF_SUB3"));
			obj.setR34_amt_name_of_sub4(rs.getBigDecimal("R34_AMT_NAME_OF_SUB4"));
			obj.setR34_amt_name_of_sub5(rs.getBigDecimal("R34_AMT_NAME_OF_SUB5"));
			obj.setR34_tot_amt(rs.getBigDecimal("R34_TOT_AMT"));

			// R36 (R35 skipped)
			obj.setR36_item(rs.getString("R36_ITEM"));
			obj.setR36_amt_name_of_sub1(rs.getBigDecimal("R36_AMT_NAME_OF_SUB1"));
			obj.setR36_amt_name_of_sub2(rs.getBigDecimal("R36_AMT_NAME_OF_SUB2"));
			obj.setR36_amt_name_of_sub3(rs.getBigDecimal("R36_AMT_NAME_OF_SUB3"));
			obj.setR36_amt_name_of_sub4(rs.getBigDecimal("R36_AMT_NAME_OF_SUB4"));
			obj.setR36_amt_name_of_sub5(rs.getBigDecimal("R36_AMT_NAME_OF_SUB5"));
			obj.setR36_tot_amt(rs.getBigDecimal("R36_TOT_AMT"));

			// R37
			obj.setR37_item(rs.getString("R37_ITEM"));
			obj.setR37_amt_name_of_sub1(rs.getBigDecimal("R37_AMT_NAME_OF_SUB1"));
			obj.setR37_amt_name_of_sub2(rs.getBigDecimal("R37_AMT_NAME_OF_SUB2"));
			obj.setR37_amt_name_of_sub3(rs.getBigDecimal("R37_AMT_NAME_OF_SUB3"));
			obj.setR37_amt_name_of_sub4(rs.getBigDecimal("R37_AMT_NAME_OF_SUB4"));
			obj.setR37_amt_name_of_sub5(rs.getBigDecimal("R37_AMT_NAME_OF_SUB5"));
			obj.setR37_tot_amt(rs.getBigDecimal("R37_TOT_AMT"));

			// R38
			obj.setR38_item(rs.getString("R38_ITEM"));
			obj.setR38_amt_name_of_sub1(rs.getBigDecimal("R38_AMT_NAME_OF_SUB1"));
			obj.setR38_amt_name_of_sub2(rs.getBigDecimal("R38_AMT_NAME_OF_SUB2"));
			obj.setR38_amt_name_of_sub3(rs.getBigDecimal("R38_AMT_NAME_OF_SUB3"));
			obj.setR38_amt_name_of_sub4(rs.getBigDecimal("R38_AMT_NAME_OF_SUB4"));
			obj.setR38_amt_name_of_sub5(rs.getBigDecimal("R38_AMT_NAME_OF_SUB5"));
			obj.setR38_tot_amt(rs.getBigDecimal("R38_TOT_AMT"));

			// R39
			obj.setR39_item(rs.getString("R39_ITEM"));
			obj.setR39_amt_name_of_sub1(rs.getBigDecimal("R39_AMT_NAME_OF_SUB1"));
			obj.setR39_amt_name_of_sub2(rs.getBigDecimal("R39_AMT_NAME_OF_SUB2"));
			obj.setR39_amt_name_of_sub3(rs.getBigDecimal("R39_AMT_NAME_OF_SUB3"));
			obj.setR39_amt_name_of_sub4(rs.getBigDecimal("R39_AMT_NAME_OF_SUB4"));
			obj.setR39_amt_name_of_sub5(rs.getBigDecimal("R39_AMT_NAME_OF_SUB5"));
			obj.setR39_tot_amt(rs.getBigDecimal("R39_TOT_AMT"));

			// R40
			obj.setR40_item(rs.getString("R40_ITEM"));
			obj.setR40_amt_name_of_sub1(rs.getBigDecimal("R40_AMT_NAME_OF_SUB1"));
			obj.setR40_amt_name_of_sub2(rs.getBigDecimal("R40_AMT_NAME_OF_SUB2"));
			obj.setR40_amt_name_of_sub3(rs.getBigDecimal("R40_AMT_NAME_OF_SUB3"));
			obj.setR40_amt_name_of_sub4(rs.getBigDecimal("R40_AMT_NAME_OF_SUB4"));
			obj.setR40_amt_name_of_sub5(rs.getBigDecimal("R40_AMT_NAME_OF_SUB5"));
			obj.setR40_tot_amt(rs.getBigDecimal("R40_TOT_AMT"));

			// R41
			obj.setR41_item(rs.getString("R41_ITEM"));
			obj.setR41_amt_name_of_sub1(rs.getBigDecimal("R41_AMT_NAME_OF_SUB1"));
			obj.setR41_amt_name_of_sub2(rs.getBigDecimal("R41_AMT_NAME_OF_SUB2"));
			obj.setR41_amt_name_of_sub3(rs.getBigDecimal("R41_AMT_NAME_OF_SUB3"));
			obj.setR41_amt_name_of_sub4(rs.getBigDecimal("R41_AMT_NAME_OF_SUB4"));
			obj.setR41_amt_name_of_sub5(rs.getBigDecimal("R41_AMT_NAME_OF_SUB5"));
			obj.setR41_tot_amt(rs.getBigDecimal("R41_TOT_AMT"));

			// R42
			obj.setR42_item(rs.getString("R42_ITEM"));
			obj.setR42_amt_name_of_sub1(rs.getBigDecimal("R42_AMT_NAME_OF_SUB1"));
			obj.setR42_amt_name_of_sub2(rs.getBigDecimal("R42_AMT_NAME_OF_SUB2"));
			obj.setR42_amt_name_of_sub3(rs.getBigDecimal("R42_AMT_NAME_OF_SUB3"));
			obj.setR42_amt_name_of_sub4(rs.getBigDecimal("R42_AMT_NAME_OF_SUB4"));
			obj.setR42_amt_name_of_sub5(rs.getBigDecimal("R42_AMT_NAME_OF_SUB5"));
			obj.setR42_tot_amt(rs.getBigDecimal("R42_TOT_AMT"));

			// R43
			obj.setR43_item(rs.getString("R43_ITEM"));
			obj.setR43_amt_name_of_sub1(rs.getBigDecimal("R43_AMT_NAME_OF_SUB1"));
			obj.setR43_amt_name_of_sub2(rs.getBigDecimal("R43_AMT_NAME_OF_SUB2"));
			obj.setR43_amt_name_of_sub3(rs.getBigDecimal("R43_AMT_NAME_OF_SUB3"));
			obj.setR43_amt_name_of_sub4(rs.getBigDecimal("R43_AMT_NAME_OF_SUB4"));
			obj.setR43_amt_name_of_sub5(rs.getBigDecimal("R43_AMT_NAME_OF_SUB5"));
			obj.setR43_tot_amt(rs.getBigDecimal("R43_TOT_AMT"));

			// R44
			obj.setR44_item(rs.getString("R44_ITEM"));
			obj.setR44_amt_name_of_sub1(rs.getBigDecimal("R44_AMT_NAME_OF_SUB1"));
			obj.setR44_amt_name_of_sub2(rs.getBigDecimal("R44_AMT_NAME_OF_SUB2"));
			obj.setR44_amt_name_of_sub3(rs.getBigDecimal("R44_AMT_NAME_OF_SUB3"));
			obj.setR44_amt_name_of_sub4(rs.getBigDecimal("R44_AMT_NAME_OF_SUB4"));
			obj.setR44_amt_name_of_sub5(rs.getBigDecimal("R44_AMT_NAME_OF_SUB5"));
			obj.setR44_tot_amt(rs.getBigDecimal("R44_TOT_AMT"));

			// R45
			obj.setR45_item(rs.getString("R45_ITEM"));
			obj.setR45_amt_name_of_sub1(rs.getBigDecimal("R45_AMT_NAME_OF_SUB1"));
			obj.setR45_amt_name_of_sub2(rs.getBigDecimal("R45_AMT_NAME_OF_SUB2"));
			obj.setR45_amt_name_of_sub3(rs.getBigDecimal("R45_AMT_NAME_OF_SUB3"));
			obj.setR45_amt_name_of_sub4(rs.getBigDecimal("R45_AMT_NAME_OF_SUB4"));
			obj.setR45_amt_name_of_sub5(rs.getBigDecimal("R45_AMT_NAME_OF_SUB5"));
			obj.setR45_tot_amt(rs.getBigDecimal("R45_TOT_AMT"));

			// R46
			obj.setR46_item(rs.getString("R46_ITEM"));
			obj.setR46_amt_name_of_sub1(rs.getBigDecimal("R46_AMT_NAME_OF_SUB1"));
			obj.setR46_amt_name_of_sub2(rs.getBigDecimal("R46_AMT_NAME_OF_SUB2"));
			obj.setR46_amt_name_of_sub3(rs.getBigDecimal("R46_AMT_NAME_OF_SUB3"));
			obj.setR46_amt_name_of_sub4(rs.getBigDecimal("R46_AMT_NAME_OF_SUB4"));
			obj.setR46_amt_name_of_sub5(rs.getBigDecimal("R46_AMT_NAME_OF_SUB5"));
			obj.setR46_tot_amt(rs.getBigDecimal("R46_TOT_AMT"));

			// R47
			obj.setR47_item(rs.getString("R47_ITEM"));
			obj.setR47_amt_name_of_sub1(rs.getBigDecimal("R47_AMT_NAME_OF_SUB1"));
			obj.setR47_amt_name_of_sub2(rs.getBigDecimal("R47_AMT_NAME_OF_SUB2"));
			obj.setR47_amt_name_of_sub3(rs.getBigDecimal("R47_AMT_NAME_OF_SUB3"));
			obj.setR47_amt_name_of_sub4(rs.getBigDecimal("R47_AMT_NAME_OF_SUB4"));
			obj.setR47_amt_name_of_sub5(rs.getBigDecimal("R47_AMT_NAME_OF_SUB5"));
			obj.setR47_tot_amt(rs.getBigDecimal("R47_TOT_AMT"));

			// R48
			obj.setR48_item(rs.getString("R48_ITEM"));
			obj.setR48_amt_name_of_sub1(rs.getBigDecimal("R48_AMT_NAME_OF_SUB1"));
			obj.setR48_amt_name_of_sub2(rs.getBigDecimal("R48_AMT_NAME_OF_SUB2"));
			obj.setR48_amt_name_of_sub3(rs.getBigDecimal("R48_AMT_NAME_OF_SUB3"));
			obj.setR48_amt_name_of_sub4(rs.getBigDecimal("R48_AMT_NAME_OF_SUB4"));
			obj.setR48_amt_name_of_sub5(rs.getBigDecimal("R48_AMT_NAME_OF_SUB5"));
			obj.setR48_tot_amt(rs.getBigDecimal("R48_TOT_AMT"));

			// R49
			obj.setR49_item(rs.getString("R49_ITEM"));
			obj.setR49_amt_name_of_sub1(rs.getBigDecimal("R49_AMT_NAME_OF_SUB1"));
			obj.setR49_amt_name_of_sub2(rs.getBigDecimal("R49_AMT_NAME_OF_SUB2"));
			obj.setR49_amt_name_of_sub3(rs.getBigDecimal("R49_AMT_NAME_OF_SUB3"));
			obj.setR49_amt_name_of_sub4(rs.getBigDecimal("R49_AMT_NAME_OF_SUB4"));
			obj.setR49_amt_name_of_sub5(rs.getBigDecimal("R49_AMT_NAME_OF_SUB5"));
			obj.setR49_tot_amt(rs.getBigDecimal("R49_TOT_AMT"));

			// R50
			obj.setR50_item(rs.getString("R50_ITEM"));
			obj.setR50_amt_name_of_sub1(rs.getBigDecimal("R50_AMT_NAME_OF_SUB1"));
			obj.setR50_amt_name_of_sub2(rs.getBigDecimal("R50_AMT_NAME_OF_SUB2"));
			obj.setR50_amt_name_of_sub3(rs.getBigDecimal("R50_AMT_NAME_OF_SUB3"));
			obj.setR50_amt_name_of_sub4(rs.getBigDecimal("R50_AMT_NAME_OF_SUB4"));
			obj.setR50_amt_name_of_sub5(rs.getBigDecimal("R50_AMT_NAME_OF_SUB5"));
			obj.setR50_tot_amt(rs.getBigDecimal("R50_TOT_AMT"));

			// R51
			obj.setR51_item(rs.getString("R51_ITEM"));
			obj.setR51_amt_name_of_sub1(rs.getBigDecimal("R51_AMT_NAME_OF_SUB1"));
			obj.setR51_amt_name_of_sub2(rs.getBigDecimal("R51_AMT_NAME_OF_SUB2"));
			obj.setR51_amt_name_of_sub3(rs.getBigDecimal("R51_AMT_NAME_OF_SUB3"));
			obj.setR51_amt_name_of_sub4(rs.getBigDecimal("R51_AMT_NAME_OF_SUB4"));
			obj.setR51_amt_name_of_sub5(rs.getBigDecimal("R51_AMT_NAME_OF_SUB5"));
			obj.setR51_tot_amt(rs.getBigDecimal("R51_TOT_AMT"));

			// R52
			obj.setR52_item(rs.getString("R52_ITEM"));
			obj.setR52_amt_name_of_sub1(rs.getBigDecimal("R52_AMT_NAME_OF_SUB1"));
			obj.setR52_amt_name_of_sub2(rs.getBigDecimal("R52_AMT_NAME_OF_SUB2"));
			obj.setR52_amt_name_of_sub3(rs.getBigDecimal("R52_AMT_NAME_OF_SUB3"));
			obj.setR52_amt_name_of_sub4(rs.getBigDecimal("R52_AMT_NAME_OF_SUB4"));
			obj.setR52_amt_name_of_sub5(rs.getBigDecimal("R52_AMT_NAME_OF_SUB5"));
			obj.setR52_tot_amt(rs.getBigDecimal("R52_TOT_AMT"));

			// R53
			obj.setR53_item(rs.getString("R53_ITEM"));
			obj.setR53_amt_name_of_sub1(rs.getBigDecimal("R53_AMT_NAME_OF_SUB1"));
			obj.setR53_amt_name_of_sub2(rs.getBigDecimal("R53_AMT_NAME_OF_SUB2"));
			obj.setR53_amt_name_of_sub3(rs.getBigDecimal("R53_AMT_NAME_OF_SUB3"));
			obj.setR53_amt_name_of_sub4(rs.getBigDecimal("R53_AMT_NAME_OF_SUB4"));
			obj.setR53_amt_name_of_sub5(rs.getBigDecimal("R53_AMT_NAME_OF_SUB5"));
			obj.setR53_tot_amt(rs.getBigDecimal("R53_TOT_AMT"));

			// R54
			obj.setR54_item(rs.getString("R54_ITEM"));
			obj.setR54_amt_name_of_sub1(rs.getBigDecimal("R54_AMT_NAME_OF_SUB1"));
			obj.setR54_amt_name_of_sub2(rs.getBigDecimal("R54_AMT_NAME_OF_SUB2"));
			obj.setR54_amt_name_of_sub3(rs.getBigDecimal("R54_AMT_NAME_OF_SUB3"));
			obj.setR54_amt_name_of_sub4(rs.getBigDecimal("R54_AMT_NAME_OF_SUB4"));
			obj.setR54_amt_name_of_sub5(rs.getBigDecimal("R54_AMT_NAME_OF_SUB5"));
			obj.setR54_tot_amt(rs.getBigDecimal("R54_TOT_AMT"));

			// R55
			obj.setR55_item(rs.getString("R55_ITEM"));
			obj.setR55_amt_name_of_sub1(rs.getBigDecimal("R55_AMT_NAME_OF_SUB1"));
			obj.setR55_amt_name_of_sub2(rs.getBigDecimal("R55_AMT_NAME_OF_SUB2"));
			obj.setR55_amt_name_of_sub3(rs.getBigDecimal("R55_AMT_NAME_OF_SUB3"));
			obj.setR55_amt_name_of_sub4(rs.getBigDecimal("R55_AMT_NAME_OF_SUB4"));
			obj.setR55_amt_name_of_sub5(rs.getBigDecimal("R55_AMT_NAME_OF_SUB5"));
			obj.setR55_tot_amt(rs.getBigDecimal("R55_TOT_AMT"));

			// R56
			obj.setR56_item(rs.getString("R56_ITEM"));
			obj.setR56_amt_name_of_sub1(rs.getBigDecimal("R56_AMT_NAME_OF_SUB1"));
			obj.setR56_amt_name_of_sub2(rs.getBigDecimal("R56_AMT_NAME_OF_SUB2"));
			obj.setR56_amt_name_of_sub3(rs.getBigDecimal("R56_AMT_NAME_OF_SUB3"));
			obj.setR56_amt_name_of_sub4(rs.getBigDecimal("R56_AMT_NAME_OF_SUB4"));
			obj.setR56_amt_name_of_sub5(rs.getBigDecimal("R56_AMT_NAME_OF_SUB5"));
			obj.setR56_tot_amt(rs.getBigDecimal("R56_TOT_AMT"));

			// R57
			obj.setR57_item(rs.getString("R57_ITEM"));
			obj.setR57_amt_name_of_sub1(rs.getBigDecimal("R57_AMT_NAME_OF_SUB1"));
			obj.setR57_amt_name_of_sub2(rs.getBigDecimal("R57_AMT_NAME_OF_SUB2"));
			obj.setR57_amt_name_of_sub3(rs.getBigDecimal("R57_AMT_NAME_OF_SUB3"));
			obj.setR57_amt_name_of_sub4(rs.getBigDecimal("R57_AMT_NAME_OF_SUB4"));
			obj.setR57_amt_name_of_sub5(rs.getBigDecimal("R57_AMT_NAME_OF_SUB5"));
			obj.setR57_tot_amt(rs.getBigDecimal("R57_TOT_AMT"));

			// R58
			obj.setR58_item(rs.getString("R58_ITEM"));
			obj.setR58_amt_name_of_sub1(rs.getBigDecimal("R58_AMT_NAME_OF_SUB1"));
			obj.setR58_amt_name_of_sub2(rs.getBigDecimal("R58_AMT_NAME_OF_SUB2"));
			obj.setR58_amt_name_of_sub3(rs.getBigDecimal("R58_AMT_NAME_OF_SUB3"));
			obj.setR58_amt_name_of_sub4(rs.getBigDecimal("R58_AMT_NAME_OF_SUB4"));
			obj.setR58_amt_name_of_sub5(rs.getBigDecimal("R58_AMT_NAME_OF_SUB5"));
			obj.setR58_tot_amt(rs.getBigDecimal("R58_TOT_AMT"));

			// Metadata fields
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));

			return obj;
		}
	}

	class M_CA4DetailRowMapper implements RowMapper<M_CA4_Detail_Entity> {
		@Override
		public M_CA4_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA4_Detail_Entity obj = new M_CA4_Detail_Entity();

			// R10
			obj.setR10_item(rs.getString("R10_ITEM"));
			obj.setR10_amt_name_of_sub1(rs.getBigDecimal("R10_AMT_NAME_OF_SUB1"));
			obj.setR10_amt_name_of_sub2(rs.getBigDecimal("R10_AMT_NAME_OF_SUB2"));
			obj.setR10_amt_name_of_sub3(rs.getBigDecimal("R10_AMT_NAME_OF_SUB3"));
			obj.setR10_amt_name_of_sub4(rs.getBigDecimal("R10_AMT_NAME_OF_SUB4"));
			obj.setR10_amt_name_of_sub5(rs.getBigDecimal("R10_AMT_NAME_OF_SUB5"));
			obj.setR10_tot_amt(rs.getBigDecimal("R10_TOT_AMT"));

			// R11
			obj.setR11_item(rs.getString("R11_ITEM"));
			obj.setR11_amt_name_of_sub1(rs.getBigDecimal("R11_AMT_NAME_OF_SUB1"));
			obj.setR11_amt_name_of_sub2(rs.getBigDecimal("R11_AMT_NAME_OF_SUB2"));
			obj.setR11_amt_name_of_sub3(rs.getBigDecimal("R11_AMT_NAME_OF_SUB3"));
			obj.setR11_amt_name_of_sub4(rs.getBigDecimal("R11_AMT_NAME_OF_SUB4"));
			obj.setR11_amt_name_of_sub5(rs.getBigDecimal("R11_AMT_NAME_OF_SUB5"));
			obj.setR11_tot_amt(rs.getBigDecimal("R11_TOT_AMT"));

			// R12
			obj.setR12_item(rs.getString("R12_ITEM"));
			obj.setR12_amt_name_of_sub1(rs.getBigDecimal("R12_AMT_NAME_OF_SUB1"));
			obj.setR12_amt_name_of_sub2(rs.getBigDecimal("R12_AMT_NAME_OF_SUB2"));
			obj.setR12_amt_name_of_sub3(rs.getBigDecimal("R12_AMT_NAME_OF_SUB3"));
			obj.setR12_amt_name_of_sub4(rs.getBigDecimal("R12_AMT_NAME_OF_SUB4"));
			obj.setR12_amt_name_of_sub5(rs.getBigDecimal("R12_AMT_NAME_OF_SUB5"));
			obj.setR12_tot_amt(rs.getBigDecimal("R12_TOT_AMT"));

			// R13
			obj.setR13_item(rs.getString("R13_ITEM"));
			obj.setR13_amt_name_of_sub1(rs.getBigDecimal("R13_AMT_NAME_OF_SUB1"));
			obj.setR13_amt_name_of_sub2(rs.getBigDecimal("R13_AMT_NAME_OF_SUB2"));
			obj.setR13_amt_name_of_sub3(rs.getBigDecimal("R13_AMT_NAME_OF_SUB3"));
			obj.setR13_amt_name_of_sub4(rs.getBigDecimal("R13_AMT_NAME_OF_SUB4"));
			obj.setR13_amt_name_of_sub5(rs.getBigDecimal("R13_AMT_NAME_OF_SUB5"));
			obj.setR13_tot_amt(rs.getBigDecimal("R13_TOT_AMT"));

			// R14
			obj.setR14_item(rs.getString("R14_ITEM"));
			obj.setR14_amt_name_of_sub1(rs.getBigDecimal("R14_AMT_NAME_OF_SUB1"));
			obj.setR14_amt_name_of_sub2(rs.getBigDecimal("R14_AMT_NAME_OF_SUB2"));
			obj.setR14_amt_name_of_sub3(rs.getBigDecimal("R14_AMT_NAME_OF_SUB3"));
			obj.setR14_amt_name_of_sub4(rs.getBigDecimal("R14_AMT_NAME_OF_SUB4"));
			obj.setR14_amt_name_of_sub5(rs.getBigDecimal("R14_AMT_NAME_OF_SUB5"));
			obj.setR14_tot_amt(rs.getBigDecimal("R14_TOT_AMT"));

			// R17
			obj.setR17_item(rs.getString("R17_ITEM"));
			obj.setR17_amt_name_of_sub1(rs.getBigDecimal("R17_AMT_NAME_OF_SUB1"));
			obj.setR17_amt_name_of_sub2(rs.getBigDecimal("R17_AMT_NAME_OF_SUB2"));
			obj.setR17_amt_name_of_sub3(rs.getBigDecimal("R17_AMT_NAME_OF_SUB3"));
			obj.setR17_amt_name_of_sub4(rs.getBigDecimal("R17_AMT_NAME_OF_SUB4"));
			obj.setR17_amt_name_of_sub5(rs.getBigDecimal("R17_AMT_NAME_OF_SUB5"));
			obj.setR17_tot_amt(rs.getBigDecimal("R17_TOT_AMT"));

			// R18
			obj.setR18_item(rs.getString("R18_ITEM"));
			obj.setR18_amt_name_of_sub1(rs.getBigDecimal("R18_AMT_NAME_OF_SUB1"));
			obj.setR18_amt_name_of_sub2(rs.getBigDecimal("R18_AMT_NAME_OF_SUB2"));
			obj.setR18_amt_name_of_sub3(rs.getBigDecimal("R18_AMT_NAME_OF_SUB3"));
			obj.setR18_amt_name_of_sub4(rs.getBigDecimal("R18_AMT_NAME_OF_SUB4"));
			obj.setR18_amt_name_of_sub5(rs.getBigDecimal("R18_AMT_NAME_OF_SUB5"));
			obj.setR18_tot_amt(rs.getBigDecimal("R18_TOT_AMT"));

			// R19
			obj.setR19_item(rs.getString("R19_ITEM"));
			obj.setR19_amt_name_of_sub1(rs.getBigDecimal("R19_AMT_NAME_OF_SUB1"));
			obj.setR19_amt_name_of_sub2(rs.getBigDecimal("R19_AMT_NAME_OF_SUB2"));
			obj.setR19_amt_name_of_sub3(rs.getBigDecimal("R19_AMT_NAME_OF_SUB3"));
			obj.setR19_amt_name_of_sub4(rs.getBigDecimal("R19_AMT_NAME_OF_SUB4"));
			obj.setR19_amt_name_of_sub5(rs.getBigDecimal("R19_AMT_NAME_OF_SUB5"));
			obj.setR19_tot_amt(rs.getBigDecimal("R19_TOT_AMT"));

			// R22
			obj.setR22_item(rs.getString("R22_ITEM"));
			obj.setR22_amt_name_of_sub1(rs.getBigDecimal("R22_AMT_NAME_OF_SUB1"));
			obj.setR22_amt_name_of_sub2(rs.getBigDecimal("R22_AMT_NAME_OF_SUB2"));
			obj.setR22_amt_name_of_sub3(rs.getBigDecimal("R22_AMT_NAME_OF_SUB3"));
			obj.setR22_amt_name_of_sub4(rs.getBigDecimal("R22_AMT_NAME_OF_SUB4"));
			obj.setR22_amt_name_of_sub5(rs.getBigDecimal("R22_AMT_NAME_OF_SUB5"));
			obj.setR22_tot_amt(rs.getBigDecimal("R22_TOT_AMT"));

			// R23
			obj.setR23_item(rs.getString("R23_ITEM"));
			obj.setR23_amt_name_of_sub1(rs.getBigDecimal("R23_AMT_NAME_OF_SUB1"));
			obj.setR23_amt_name_of_sub2(rs.getBigDecimal("R23_AMT_NAME_OF_SUB2"));
			obj.setR23_amt_name_of_sub3(rs.getBigDecimal("R23_AMT_NAME_OF_SUB3"));
			obj.setR23_amt_name_of_sub4(rs.getBigDecimal("R23_AMT_NAME_OF_SUB4"));
			obj.setR23_amt_name_of_sub5(rs.getBigDecimal("R23_AMT_NAME_OF_SUB5"));
			obj.setR23_tot_amt(rs.getBigDecimal("R23_TOT_AMT"));

			// R24
			obj.setR24_item(rs.getString("R24_ITEM"));
			obj.setR24_amt_name_of_sub1(rs.getBigDecimal("R24_AMT_NAME_OF_SUB1"));
			obj.setR24_amt_name_of_sub2(rs.getBigDecimal("R24_AMT_NAME_OF_SUB2"));
			obj.setR24_amt_name_of_sub3(rs.getBigDecimal("R24_AMT_NAME_OF_SUB3"));
			obj.setR24_amt_name_of_sub4(rs.getBigDecimal("R24_AMT_NAME_OF_SUB4"));
			obj.setR24_amt_name_of_sub5(rs.getBigDecimal("R24_AMT_NAME_OF_SUB5"));
			obj.setR24_tot_amt(rs.getBigDecimal("R24_TOT_AMT"));

			// R27
			obj.setR27_item(rs.getString("R27_ITEM"));
			obj.setR27_amt_name_of_sub1(rs.getBigDecimal("R27_AMT_NAME_OF_SUB1"));
			obj.setR27_amt_name_of_sub2(rs.getBigDecimal("R27_AMT_NAME_OF_SUB2"));
			obj.setR27_amt_name_of_sub3(rs.getBigDecimal("R27_AMT_NAME_OF_SUB3"));
			obj.setR27_amt_name_of_sub4(rs.getBigDecimal("R27_AMT_NAME_OF_SUB4"));
			obj.setR27_amt_name_of_sub5(rs.getBigDecimal("R27_AMT_NAME_OF_SUB5"));
			obj.setR27_tot_amt(rs.getBigDecimal("R27_TOT_AMT"));

			// R28
			obj.setR28_item(rs.getString("R28_ITEM"));
			obj.setR28_amt_name_of_sub1(rs.getBigDecimal("R28_AMT_NAME_OF_SUB1"));
			obj.setR28_amt_name_of_sub2(rs.getBigDecimal("R28_AMT_NAME_OF_SUB2"));
			obj.setR28_amt_name_of_sub3(rs.getBigDecimal("R28_AMT_NAME_OF_SUB3"));
			obj.setR28_amt_name_of_sub4(rs.getBigDecimal("R28_AMT_NAME_OF_SUB4"));
			obj.setR28_amt_name_of_sub5(rs.getBigDecimal("R28_AMT_NAME_OF_SUB5"));
			obj.setR28_tot_amt(rs.getBigDecimal("R28_TOT_AMT"));

			// R29
			obj.setR29_item(rs.getString("R29_ITEM"));
			obj.setR29_amt_name_of_sub1(rs.getBigDecimal("R29_AMT_NAME_OF_SUB1"));
			obj.setR29_amt_name_of_sub2(rs.getBigDecimal("R29_AMT_NAME_OF_SUB2"));
			obj.setR29_amt_name_of_sub3(rs.getBigDecimal("R29_AMT_NAME_OF_SUB3"));
			obj.setR29_amt_name_of_sub4(rs.getBigDecimal("R29_AMT_NAME_OF_SUB4"));
			obj.setR29_amt_name_of_sub5(rs.getBigDecimal("R29_AMT_NAME_OF_SUB5"));
			obj.setR29_tot_amt(rs.getBigDecimal("R29_TOT_AMT"));

			// R32
			obj.setR32_item(rs.getString("R32_ITEM"));
			obj.setR32_amt_name_of_sub1(rs.getBigDecimal("R32_AMT_NAME_OF_SUB1"));
			obj.setR32_amt_name_of_sub2(rs.getBigDecimal("R32_AMT_NAME_OF_SUB2"));
			obj.setR32_amt_name_of_sub3(rs.getBigDecimal("R32_AMT_NAME_OF_SUB3"));
			obj.setR32_amt_name_of_sub4(rs.getBigDecimal("R32_AMT_NAME_OF_SUB4"));
			obj.setR32_amt_name_of_sub5(rs.getBigDecimal("R32_AMT_NAME_OF_SUB5"));
			obj.setR32_tot_amt(rs.getBigDecimal("R32_TOT_AMT"));

			// R33
			obj.setR33_item(rs.getString("R33_ITEM"));
			obj.setR33_amt_name_of_sub1(rs.getBigDecimal("R33_AMT_NAME_OF_SUB1"));
			obj.setR33_amt_name_of_sub2(rs.getBigDecimal("R33_AMT_NAME_OF_SUB2"));
			obj.setR33_amt_name_of_sub3(rs.getBigDecimal("R33_AMT_NAME_OF_SUB3"));
			obj.setR33_amt_name_of_sub4(rs.getBigDecimal("R33_AMT_NAME_OF_SUB4"));
			obj.setR33_amt_name_of_sub5(rs.getBigDecimal("R33_AMT_NAME_OF_SUB5"));
			obj.setR33_tot_amt(rs.getBigDecimal("R33_TOT_AMT"));

			// R34
			obj.setR34_item(rs.getString("R34_ITEM"));
			obj.setR34_amt_name_of_sub1(rs.getBigDecimal("R34_AMT_NAME_OF_SUB1"));
			obj.setR34_amt_name_of_sub2(rs.getBigDecimal("R34_AMT_NAME_OF_SUB2"));
			obj.setR34_amt_name_of_sub3(rs.getBigDecimal("R34_AMT_NAME_OF_SUB3"));
			obj.setR34_amt_name_of_sub4(rs.getBigDecimal("R34_AMT_NAME_OF_SUB4"));
			obj.setR34_amt_name_of_sub5(rs.getBigDecimal("R34_AMT_NAME_OF_SUB5"));
			obj.setR34_tot_amt(rs.getBigDecimal("R34_TOT_AMT"));

			// R36
			obj.setR36_item(rs.getString("R36_ITEM"));
			obj.setR36_amt_name_of_sub1(rs.getBigDecimal("R36_AMT_NAME_OF_SUB1"));
			obj.setR36_amt_name_of_sub2(rs.getBigDecimal("R36_AMT_NAME_OF_SUB2"));
			obj.setR36_amt_name_of_sub3(rs.getBigDecimal("R36_AMT_NAME_OF_SUB3"));
			obj.setR36_amt_name_of_sub4(rs.getBigDecimal("R36_AMT_NAME_OF_SUB4"));
			obj.setR36_amt_name_of_sub5(rs.getBigDecimal("R36_AMT_NAME_OF_SUB5"));
			obj.setR36_tot_amt(rs.getBigDecimal("R36_TOT_AMT"));

			// R37
			obj.setR37_item(rs.getString("R37_ITEM"));
			obj.setR37_amt_name_of_sub1(rs.getBigDecimal("R37_AMT_NAME_OF_SUB1"));
			obj.setR37_amt_name_of_sub2(rs.getBigDecimal("R37_AMT_NAME_OF_SUB2"));
			obj.setR37_amt_name_of_sub3(rs.getBigDecimal("R37_AMT_NAME_OF_SUB3"));
			obj.setR37_amt_name_of_sub4(rs.getBigDecimal("R37_AMT_NAME_OF_SUB4"));
			obj.setR37_amt_name_of_sub5(rs.getBigDecimal("R37_AMT_NAME_OF_SUB5"));
			obj.setR37_tot_amt(rs.getBigDecimal("R37_TOT_AMT"));

			// R38
			obj.setR38_item(rs.getString("R38_ITEM"));
			obj.setR38_amt_name_of_sub1(rs.getBigDecimal("R38_AMT_NAME_OF_SUB1"));
			obj.setR38_amt_name_of_sub2(rs.getBigDecimal("R38_AMT_NAME_OF_SUB2"));
			obj.setR38_amt_name_of_sub3(rs.getBigDecimal("R38_AMT_NAME_OF_SUB3"));
			obj.setR38_amt_name_of_sub4(rs.getBigDecimal("R38_AMT_NAME_OF_SUB4"));
			obj.setR38_amt_name_of_sub5(rs.getBigDecimal("R38_AMT_NAME_OF_SUB5"));
			obj.setR38_tot_amt(rs.getBigDecimal("R38_TOT_AMT"));

			// R39
			obj.setR39_item(rs.getString("R39_ITEM"));
			obj.setR39_amt_name_of_sub1(rs.getBigDecimal("R39_AMT_NAME_OF_SUB1"));
			obj.setR39_amt_name_of_sub2(rs.getBigDecimal("R39_AMT_NAME_OF_SUB2"));
			obj.setR39_amt_name_of_sub3(rs.getBigDecimal("R39_AMT_NAME_OF_SUB3"));
			obj.setR39_amt_name_of_sub4(rs.getBigDecimal("R39_AMT_NAME_OF_SUB4"));
			obj.setR39_amt_name_of_sub5(rs.getBigDecimal("R39_AMT_NAME_OF_SUB5"));
			obj.setR39_tot_amt(rs.getBigDecimal("R39_TOT_AMT"));

			// R40
			obj.setR40_item(rs.getString("R40_ITEM"));
			obj.setR40_amt_name_of_sub1(rs.getBigDecimal("R40_AMT_NAME_OF_SUB1"));
			obj.setR40_amt_name_of_sub2(rs.getBigDecimal("R40_AMT_NAME_OF_SUB2"));
			obj.setR40_amt_name_of_sub3(rs.getBigDecimal("R40_AMT_NAME_OF_SUB3"));
			obj.setR40_amt_name_of_sub4(rs.getBigDecimal("R40_AMT_NAME_OF_SUB4"));
			obj.setR40_amt_name_of_sub5(rs.getBigDecimal("R40_AMT_NAME_OF_SUB5"));
			obj.setR40_tot_amt(rs.getBigDecimal("R40_TOT_AMT"));

			// R41
			obj.setR41_item(rs.getString("R41_ITEM"));
			obj.setR41_amt_name_of_sub1(rs.getBigDecimal("R41_AMT_NAME_OF_SUB1"));
			obj.setR41_amt_name_of_sub2(rs.getBigDecimal("R41_AMT_NAME_OF_SUB2"));
			obj.setR41_amt_name_of_sub3(rs.getBigDecimal("R41_AMT_NAME_OF_SUB3"));
			obj.setR41_amt_name_of_sub4(rs.getBigDecimal("R41_AMT_NAME_OF_SUB4"));
			obj.setR41_amt_name_of_sub5(rs.getBigDecimal("R41_AMT_NAME_OF_SUB5"));
			obj.setR41_tot_amt(rs.getBigDecimal("R41_TOT_AMT"));

			// R42
			obj.setR42_item(rs.getString("R42_ITEM"));
			obj.setR42_amt_name_of_sub1(rs.getBigDecimal("R42_AMT_NAME_OF_SUB1"));
			obj.setR42_amt_name_of_sub2(rs.getBigDecimal("R42_AMT_NAME_OF_SUB2"));
			obj.setR42_amt_name_of_sub3(rs.getBigDecimal("R42_AMT_NAME_OF_SUB3"));
			obj.setR42_amt_name_of_sub4(rs.getBigDecimal("R42_AMT_NAME_OF_SUB4"));
			obj.setR42_amt_name_of_sub5(rs.getBigDecimal("R42_AMT_NAME_OF_SUB5"));
			obj.setR42_tot_amt(rs.getBigDecimal("R42_TOT_AMT"));

			// R43
			obj.setR43_item(rs.getString("R43_ITEM"));
			obj.setR43_amt_name_of_sub1(rs.getBigDecimal("R43_AMT_NAME_OF_SUB1"));
			obj.setR43_amt_name_of_sub2(rs.getBigDecimal("R43_AMT_NAME_OF_SUB2"));
			obj.setR43_amt_name_of_sub3(rs.getBigDecimal("R43_AMT_NAME_OF_SUB3"));
			obj.setR43_amt_name_of_sub4(rs.getBigDecimal("R43_AMT_NAME_OF_SUB4"));
			obj.setR43_amt_name_of_sub5(rs.getBigDecimal("R43_AMT_NAME_OF_SUB5"));
			obj.setR43_tot_amt(rs.getBigDecimal("R43_TOT_AMT"));

			// R44
			obj.setR44_item(rs.getString("R44_ITEM"));
			obj.setR44_amt_name_of_sub1(rs.getBigDecimal("R44_AMT_NAME_OF_SUB1"));
			obj.setR44_amt_name_of_sub2(rs.getBigDecimal("R44_AMT_NAME_OF_SUB2"));
			obj.setR44_amt_name_of_sub3(rs.getBigDecimal("R44_AMT_NAME_OF_SUB3"));
			obj.setR44_amt_name_of_sub4(rs.getBigDecimal("R44_AMT_NAME_OF_SUB4"));
			obj.setR44_amt_name_of_sub5(rs.getBigDecimal("R44_AMT_NAME_OF_SUB5"));
			obj.setR44_tot_amt(rs.getBigDecimal("R44_TOT_AMT"));

			// R45
			obj.setR45_item(rs.getString("R45_ITEM"));
			obj.setR45_amt_name_of_sub1(rs.getBigDecimal("R45_AMT_NAME_OF_SUB1"));
			obj.setR45_amt_name_of_sub2(rs.getBigDecimal("R45_AMT_NAME_OF_SUB2"));
			obj.setR45_amt_name_of_sub3(rs.getBigDecimal("R45_AMT_NAME_OF_SUB3"));
			obj.setR45_amt_name_of_sub4(rs.getBigDecimal("R45_AMT_NAME_OF_SUB4"));
			obj.setR45_amt_name_of_sub5(rs.getBigDecimal("R45_AMT_NAME_OF_SUB5"));
			obj.setR45_tot_amt(rs.getBigDecimal("R45_TOT_AMT"));

			// R46
			obj.setR46_item(rs.getString("R46_ITEM"));
			obj.setR46_amt_name_of_sub1(rs.getBigDecimal("R46_AMT_NAME_OF_SUB1"));
			obj.setR46_amt_name_of_sub2(rs.getBigDecimal("R46_AMT_NAME_OF_SUB2"));
			obj.setR46_amt_name_of_sub3(rs.getBigDecimal("R46_AMT_NAME_OF_SUB3"));
			obj.setR46_amt_name_of_sub4(rs.getBigDecimal("R46_AMT_NAME_OF_SUB4"));
			obj.setR46_amt_name_of_sub5(rs.getBigDecimal("R46_AMT_NAME_OF_SUB5"));
			obj.setR46_tot_amt(rs.getBigDecimal("R46_TOT_AMT"));

			// R47
			obj.setR47_item(rs.getString("R47_ITEM"));
			obj.setR47_amt_name_of_sub1(rs.getBigDecimal("R47_AMT_NAME_OF_SUB1"));
			obj.setR47_amt_name_of_sub2(rs.getBigDecimal("R47_AMT_NAME_OF_SUB2"));
			obj.setR47_amt_name_of_sub3(rs.getBigDecimal("R47_AMT_NAME_OF_SUB3"));
			obj.setR47_amt_name_of_sub4(rs.getBigDecimal("R47_AMT_NAME_OF_SUB4"));
			obj.setR47_amt_name_of_sub5(rs.getBigDecimal("R47_AMT_NAME_OF_SUB5"));
			obj.setR47_tot_amt(rs.getBigDecimal("R47_TOT_AMT"));

			// R48
			obj.setR48_item(rs.getString("R48_ITEM"));
			obj.setR48_amt_name_of_sub1(rs.getBigDecimal("R48_AMT_NAME_OF_SUB1"));
			obj.setR48_amt_name_of_sub2(rs.getBigDecimal("R48_AMT_NAME_OF_SUB2"));
			obj.setR48_amt_name_of_sub3(rs.getBigDecimal("R48_AMT_NAME_OF_SUB3"));
			obj.setR48_amt_name_of_sub4(rs.getBigDecimal("R48_AMT_NAME_OF_SUB4"));
			obj.setR48_amt_name_of_sub5(rs.getBigDecimal("R48_AMT_NAME_OF_SUB5"));
			obj.setR48_tot_amt(rs.getBigDecimal("R48_TOT_AMT"));

			// R49
			obj.setR49_item(rs.getString("R49_ITEM"));
			obj.setR49_amt_name_of_sub1(rs.getBigDecimal("R49_AMT_NAME_OF_SUB1"));
			obj.setR49_amt_name_of_sub2(rs.getBigDecimal("R49_AMT_NAME_OF_SUB2"));
			obj.setR49_amt_name_of_sub3(rs.getBigDecimal("R49_AMT_NAME_OF_SUB3"));
			obj.setR49_amt_name_of_sub4(rs.getBigDecimal("R49_AMT_NAME_OF_SUB4"));
			obj.setR49_amt_name_of_sub5(rs.getBigDecimal("R49_AMT_NAME_OF_SUB5"));
			obj.setR49_tot_amt(rs.getBigDecimal("R49_TOT_AMT"));

			// R50
			obj.setR50_item(rs.getString("R50_ITEM"));
			obj.setR50_amt_name_of_sub1(rs.getBigDecimal("R50_AMT_NAME_OF_SUB1"));
			obj.setR50_amt_name_of_sub2(rs.getBigDecimal("R50_AMT_NAME_OF_SUB2"));
			obj.setR50_amt_name_of_sub3(rs.getBigDecimal("R50_AMT_NAME_OF_SUB3"));
			obj.setR50_amt_name_of_sub4(rs.getBigDecimal("R50_AMT_NAME_OF_SUB4"));
			obj.setR50_amt_name_of_sub5(rs.getBigDecimal("R50_AMT_NAME_OF_SUB5"));
			obj.setR50_tot_amt(rs.getBigDecimal("R50_TOT_AMT"));

			// R51
			obj.setR51_item(rs.getString("R51_ITEM"));
			obj.setR51_amt_name_of_sub1(rs.getBigDecimal("R51_AMT_NAME_OF_SUB1"));
			obj.setR51_amt_name_of_sub2(rs.getBigDecimal("R51_AMT_NAME_OF_SUB2"));
			obj.setR51_amt_name_of_sub3(rs.getBigDecimal("R51_AMT_NAME_OF_SUB3"));
			obj.setR51_amt_name_of_sub4(rs.getBigDecimal("R51_AMT_NAME_OF_SUB4"));
			obj.setR51_amt_name_of_sub5(rs.getBigDecimal("R51_AMT_NAME_OF_SUB5"));
			obj.setR51_tot_amt(rs.getBigDecimal("R51_TOT_AMT"));

			// R52
			obj.setR52_item(rs.getString("R52_ITEM"));
			obj.setR52_amt_name_of_sub1(rs.getBigDecimal("R52_AMT_NAME_OF_SUB1"));
			obj.setR52_amt_name_of_sub2(rs.getBigDecimal("R52_AMT_NAME_OF_SUB2"));
			obj.setR52_amt_name_of_sub3(rs.getBigDecimal("R52_AMT_NAME_OF_SUB3"));
			obj.setR52_amt_name_of_sub4(rs.getBigDecimal("R52_AMT_NAME_OF_SUB4"));
			obj.setR52_amt_name_of_sub5(rs.getBigDecimal("R52_AMT_NAME_OF_SUB5"));
			obj.setR52_tot_amt(rs.getBigDecimal("R52_TOT_AMT"));

			// R53
			obj.setR53_item(rs.getString("R53_ITEM"));
			obj.setR53_amt_name_of_sub1(rs.getBigDecimal("R53_AMT_NAME_OF_SUB1"));
			obj.setR53_amt_name_of_sub2(rs.getBigDecimal("R53_AMT_NAME_OF_SUB2"));
			obj.setR53_amt_name_of_sub3(rs.getBigDecimal("R53_AMT_NAME_OF_SUB3"));
			obj.setR53_amt_name_of_sub4(rs.getBigDecimal("R53_AMT_NAME_OF_SUB4"));
			obj.setR53_amt_name_of_sub5(rs.getBigDecimal("R53_AMT_NAME_OF_SUB5"));
			obj.setR53_tot_amt(rs.getBigDecimal("R53_TOT_AMT"));

			// R54
			obj.setR54_item(rs.getString("R54_ITEM"));
			obj.setR54_amt_name_of_sub1(rs.getBigDecimal("R54_AMT_NAME_OF_SUB1"));
			obj.setR54_amt_name_of_sub2(rs.getBigDecimal("R54_AMT_NAME_OF_SUB2"));
			obj.setR54_amt_name_of_sub3(rs.getBigDecimal("R54_AMT_NAME_OF_SUB3"));
			obj.setR54_amt_name_of_sub4(rs.getBigDecimal("R54_AMT_NAME_OF_SUB4"));
			obj.setR54_amt_name_of_sub5(rs.getBigDecimal("R54_AMT_NAME_OF_SUB5"));
			obj.setR54_tot_amt(rs.getBigDecimal("R54_TOT_AMT"));

			// R55
			obj.setR55_item(rs.getString("R55_ITEM"));
			obj.setR55_amt_name_of_sub1(rs.getBigDecimal("R55_AMT_NAME_OF_SUB1"));
			obj.setR55_amt_name_of_sub2(rs.getBigDecimal("R55_AMT_NAME_OF_SUB2"));
			obj.setR55_amt_name_of_sub3(rs.getBigDecimal("R55_AMT_NAME_OF_SUB3"));
			obj.setR55_amt_name_of_sub4(rs.getBigDecimal("R55_AMT_NAME_OF_SUB4"));
			obj.setR55_amt_name_of_sub5(rs.getBigDecimal("R55_AMT_NAME_OF_SUB5"));
			obj.setR55_tot_amt(rs.getBigDecimal("R55_TOT_AMT"));

			// R56
			obj.setR56_item(rs.getString("R56_ITEM"));
			obj.setR56_amt_name_of_sub1(rs.getBigDecimal("R56_AMT_NAME_OF_SUB1"));
			obj.setR56_amt_name_of_sub2(rs.getBigDecimal("R56_AMT_NAME_OF_SUB2"));
			obj.setR56_amt_name_of_sub3(rs.getBigDecimal("R56_AMT_NAME_OF_SUB3"));
			obj.setR56_amt_name_of_sub4(rs.getBigDecimal("R56_AMT_NAME_OF_SUB4"));
			obj.setR56_amt_name_of_sub5(rs.getBigDecimal("R56_AMT_NAME_OF_SUB5"));
			obj.setR56_tot_amt(rs.getBigDecimal("R56_TOT_AMT"));

			// R57
			obj.setR57_item(rs.getString("R57_ITEM"));
			obj.setR57_amt_name_of_sub1(rs.getBigDecimal("R57_AMT_NAME_OF_SUB1"));
			obj.setR57_amt_name_of_sub2(rs.getBigDecimal("R57_AMT_NAME_OF_SUB2"));
			obj.setR57_amt_name_of_sub3(rs.getBigDecimal("R57_AMT_NAME_OF_SUB3"));
			obj.setR57_amt_name_of_sub4(rs.getBigDecimal("R57_AMT_NAME_OF_SUB4"));
			obj.setR57_amt_name_of_sub5(rs.getBigDecimal("R57_AMT_NAME_OF_SUB5"));
			obj.setR57_tot_amt(rs.getBigDecimal("R57_TOT_AMT"));

			// R58
			obj.setR58_item(rs.getString("R58_ITEM"));
			obj.setR58_amt_name_of_sub1(rs.getBigDecimal("R58_AMT_NAME_OF_SUB1"));
			obj.setR58_amt_name_of_sub2(rs.getBigDecimal("R58_AMT_NAME_OF_SUB2"));
			obj.setR58_amt_name_of_sub3(rs.getBigDecimal("R58_AMT_NAME_OF_SUB3"));
			obj.setR58_amt_name_of_sub4(rs.getBigDecimal("R58_AMT_NAME_OF_SUB4"));
			obj.setR58_amt_name_of_sub5(rs.getBigDecimal("R58_AMT_NAME_OF_SUB5"));
			obj.setR58_tot_amt(rs.getBigDecimal("R58_TOT_AMT"));

			// Metadata fields
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));

			return obj;
		}
	}

	class M_CA4ArchivalSummaryRowMapper implements RowMapper<M_CA4_Archival_Summary_Entity> {
		@Override
		public M_CA4_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA4_Archival_Summary_Entity obj = new M_CA4_Archival_Summary_Entity();

			// R10
			obj.setR10_item(rs.getString("R10_ITEM"));
			obj.setR10_amt_name_of_sub1(rs.getBigDecimal("R10_AMT_NAME_OF_SUB1"));
			obj.setR10_amt_name_of_sub2(rs.getBigDecimal("R10_AMT_NAME_OF_SUB2"));
			obj.setR10_amt_name_of_sub3(rs.getBigDecimal("R10_AMT_NAME_OF_SUB3"));
			obj.setR10_amt_name_of_sub4(rs.getBigDecimal("R10_AMT_NAME_OF_SUB4"));
			obj.setR10_amt_name_of_sub5(rs.getBigDecimal("R10_AMT_NAME_OF_SUB5"));
			obj.setR10_tot_amt(rs.getBigDecimal("R10_TOT_AMT"));

			// R11
			obj.setR11_item(rs.getString("R11_ITEM"));
			obj.setR11_amt_name_of_sub1(rs.getBigDecimal("R11_AMT_NAME_OF_SUB1"));
			obj.setR11_amt_name_of_sub2(rs.getBigDecimal("R11_AMT_NAME_OF_SUB2"));
			obj.setR11_amt_name_of_sub3(rs.getBigDecimal("R11_AMT_NAME_OF_SUB3"));
			obj.setR11_amt_name_of_sub4(rs.getBigDecimal("R11_AMT_NAME_OF_SUB4"));
			obj.setR11_amt_name_of_sub5(rs.getBigDecimal("R11_AMT_NAME_OF_SUB5"));
			obj.setR11_tot_amt(rs.getBigDecimal("R11_TOT_AMT"));

			// R12
			obj.setR12_item(rs.getString("R12_ITEM"));
			obj.setR12_amt_name_of_sub1(rs.getBigDecimal("R12_AMT_NAME_OF_SUB1"));
			obj.setR12_amt_name_of_sub2(rs.getBigDecimal("R12_AMT_NAME_OF_SUB2"));
			obj.setR12_amt_name_of_sub3(rs.getBigDecimal("R12_AMT_NAME_OF_SUB3"));
			obj.setR12_amt_name_of_sub4(rs.getBigDecimal("R12_AMT_NAME_OF_SUB4"));
			obj.setR12_amt_name_of_sub5(rs.getBigDecimal("R12_AMT_NAME_OF_SUB5"));
			obj.setR12_tot_amt(rs.getBigDecimal("R12_TOT_AMT"));

			// R13
			obj.setR13_item(rs.getString("R13_ITEM"));
			obj.setR13_amt_name_of_sub1(rs.getBigDecimal("R13_AMT_NAME_OF_SUB1"));
			obj.setR13_amt_name_of_sub2(rs.getBigDecimal("R13_AMT_NAME_OF_SUB2"));
			obj.setR13_amt_name_of_sub3(rs.getBigDecimal("R13_AMT_NAME_OF_SUB3"));
			obj.setR13_amt_name_of_sub4(rs.getBigDecimal("R13_AMT_NAME_OF_SUB4"));
			obj.setR13_amt_name_of_sub5(rs.getBigDecimal("R13_AMT_NAME_OF_SUB5"));
			obj.setR13_tot_amt(rs.getBigDecimal("R13_TOT_AMT"));

			// R14
			obj.setR14_item(rs.getString("R14_ITEM"));
			obj.setR14_amt_name_of_sub1(rs.getBigDecimal("R14_AMT_NAME_OF_SUB1"));
			obj.setR14_amt_name_of_sub2(rs.getBigDecimal("R14_AMT_NAME_OF_SUB2"));
			obj.setR14_amt_name_of_sub3(rs.getBigDecimal("R14_AMT_NAME_OF_SUB3"));
			obj.setR14_amt_name_of_sub4(rs.getBigDecimal("R14_AMT_NAME_OF_SUB4"));
			obj.setR14_amt_name_of_sub5(rs.getBigDecimal("R14_AMT_NAME_OF_SUB5"));
			obj.setR14_tot_amt(rs.getBigDecimal("R14_TOT_AMT"));

			// R17
			obj.setR17_item(rs.getString("R17_ITEM"));
			obj.setR17_amt_name_of_sub1(rs.getBigDecimal("R17_AMT_NAME_OF_SUB1"));
			obj.setR17_amt_name_of_sub2(rs.getBigDecimal("R17_AMT_NAME_OF_SUB2"));
			obj.setR17_amt_name_of_sub3(rs.getBigDecimal("R17_AMT_NAME_OF_SUB3"));
			obj.setR17_amt_name_of_sub4(rs.getBigDecimal("R17_AMT_NAME_OF_SUB4"));
			obj.setR17_amt_name_of_sub5(rs.getBigDecimal("R17_AMT_NAME_OF_SUB5"));
			obj.setR17_tot_amt(rs.getBigDecimal("R17_TOT_AMT"));

			// R18
			obj.setR18_item(rs.getString("R18_ITEM"));
			obj.setR18_amt_name_of_sub1(rs.getBigDecimal("R18_AMT_NAME_OF_SUB1"));
			obj.setR18_amt_name_of_sub2(rs.getBigDecimal("R18_AMT_NAME_OF_SUB2"));
			obj.setR18_amt_name_of_sub3(rs.getBigDecimal("R18_AMT_NAME_OF_SUB3"));
			obj.setR18_amt_name_of_sub4(rs.getBigDecimal("R18_AMT_NAME_OF_SUB4"));
			obj.setR18_amt_name_of_sub5(rs.getBigDecimal("R18_AMT_NAME_OF_SUB5"));
			obj.setR18_tot_amt(rs.getBigDecimal("R18_TOT_AMT"));

			// R19
			obj.setR19_item(rs.getString("R19_ITEM"));
			obj.setR19_amt_name_of_sub1(rs.getBigDecimal("R19_AMT_NAME_OF_SUB1"));
			obj.setR19_amt_name_of_sub2(rs.getBigDecimal("R19_AMT_NAME_OF_SUB2"));
			obj.setR19_amt_name_of_sub3(rs.getBigDecimal("R19_AMT_NAME_OF_SUB3"));
			obj.setR19_amt_name_of_sub4(rs.getBigDecimal("R19_AMT_NAME_OF_SUB4"));
			obj.setR19_amt_name_of_sub5(rs.getBigDecimal("R19_AMT_NAME_OF_SUB5"));
			obj.setR19_tot_amt(rs.getBigDecimal("R19_TOT_AMT"));

			// R22
			obj.setR22_item(rs.getString("R22_ITEM"));
			obj.setR22_amt_name_of_sub1(rs.getBigDecimal("R22_AMT_NAME_OF_SUB1"));
			obj.setR22_amt_name_of_sub2(rs.getBigDecimal("R22_AMT_NAME_OF_SUB2"));
			obj.setR22_amt_name_of_sub3(rs.getBigDecimal("R22_AMT_NAME_OF_SUB3"));
			obj.setR22_amt_name_of_sub4(rs.getBigDecimal("R22_AMT_NAME_OF_SUB4"));
			obj.setR22_amt_name_of_sub5(rs.getBigDecimal("R22_AMT_NAME_OF_SUB5"));
			obj.setR22_tot_amt(rs.getBigDecimal("R22_TOT_AMT"));

			// R23
			obj.setR23_item(rs.getString("R23_ITEM"));
			obj.setR23_amt_name_of_sub1(rs.getBigDecimal("R23_AMT_NAME_OF_SUB1"));
			obj.setR23_amt_name_of_sub2(rs.getBigDecimal("R23_AMT_NAME_OF_SUB2"));
			obj.setR23_amt_name_of_sub3(rs.getBigDecimal("R23_AMT_NAME_OF_SUB3"));
			obj.setR23_amt_name_of_sub4(rs.getBigDecimal("R23_AMT_NAME_OF_SUB4"));
			obj.setR23_amt_name_of_sub5(rs.getBigDecimal("R23_AMT_NAME_OF_SUB5"));
			obj.setR23_tot_amt(rs.getBigDecimal("R23_TOT_AMT"));

			// R24
			obj.setR24_item(rs.getString("R24_ITEM"));
			obj.setR24_amt_name_of_sub1(rs.getBigDecimal("R24_AMT_NAME_OF_SUB1"));
			obj.setR24_amt_name_of_sub2(rs.getBigDecimal("R24_AMT_NAME_OF_SUB2"));
			obj.setR24_amt_name_of_sub3(rs.getBigDecimal("R24_AMT_NAME_OF_SUB3"));
			obj.setR24_amt_name_of_sub4(rs.getBigDecimal("R24_AMT_NAME_OF_SUB4"));
			obj.setR24_amt_name_of_sub5(rs.getBigDecimal("R24_AMT_NAME_OF_SUB5"));
			obj.setR24_tot_amt(rs.getBigDecimal("R24_TOT_AMT"));

			// R27
			obj.setR27_item(rs.getString("R27_ITEM"));
			obj.setR27_amt_name_of_sub1(rs.getBigDecimal("R27_AMT_NAME_OF_SUB1"));
			obj.setR27_amt_name_of_sub2(rs.getBigDecimal("R27_AMT_NAME_OF_SUB2"));
			obj.setR27_amt_name_of_sub3(rs.getBigDecimal("R27_AMT_NAME_OF_SUB3"));
			obj.setR27_amt_name_of_sub4(rs.getBigDecimal("R27_AMT_NAME_OF_SUB4"));
			obj.setR27_amt_name_of_sub5(rs.getBigDecimal("R27_AMT_NAME_OF_SUB5"));
			obj.setR27_tot_amt(rs.getBigDecimal("R27_TOT_AMT"));

			// R28
			obj.setR28_item(rs.getString("R28_ITEM"));
			obj.setR28_amt_name_of_sub1(rs.getBigDecimal("R28_AMT_NAME_OF_SUB1"));
			obj.setR28_amt_name_of_sub2(rs.getBigDecimal("R28_AMT_NAME_OF_SUB2"));
			obj.setR28_amt_name_of_sub3(rs.getBigDecimal("R28_AMT_NAME_OF_SUB3"));
			obj.setR28_amt_name_of_sub4(rs.getBigDecimal("R28_AMT_NAME_OF_SUB4"));
			obj.setR28_amt_name_of_sub5(rs.getBigDecimal("R28_AMT_NAME_OF_SUB5"));
			obj.setR28_tot_amt(rs.getBigDecimal("R28_TOT_AMT"));

			// R29
			obj.setR29_item(rs.getString("R29_ITEM"));
			obj.setR29_amt_name_of_sub1(rs.getBigDecimal("R29_AMT_NAME_OF_SUB1"));
			obj.setR29_amt_name_of_sub2(rs.getBigDecimal("R29_AMT_NAME_OF_SUB2"));
			obj.setR29_amt_name_of_sub3(rs.getBigDecimal("R29_AMT_NAME_OF_SUB3"));
			obj.setR29_amt_name_of_sub4(rs.getBigDecimal("R29_AMT_NAME_OF_SUB4"));
			obj.setR29_amt_name_of_sub5(rs.getBigDecimal("R29_AMT_NAME_OF_SUB5"));
			obj.setR29_tot_amt(rs.getBigDecimal("R29_TOT_AMT"));

			// R32
			obj.setR32_item(rs.getString("R32_ITEM"));
			obj.setR32_amt_name_of_sub1(rs.getBigDecimal("R32_AMT_NAME_OF_SUB1"));
			obj.setR32_amt_name_of_sub2(rs.getBigDecimal("R32_AMT_NAME_OF_SUB2"));
			obj.setR32_amt_name_of_sub3(rs.getBigDecimal("R32_AMT_NAME_OF_SUB3"));
			obj.setR32_amt_name_of_sub4(rs.getBigDecimal("R32_AMT_NAME_OF_SUB4"));
			obj.setR32_amt_name_of_sub5(rs.getBigDecimal("R32_AMT_NAME_OF_SUB5"));
			obj.setR32_tot_amt(rs.getBigDecimal("R32_TOT_AMT"));

			// R33
			obj.setR33_item(rs.getString("R33_ITEM"));
			obj.setR33_amt_name_of_sub1(rs.getBigDecimal("R33_AMT_NAME_OF_SUB1"));
			obj.setR33_amt_name_of_sub2(rs.getBigDecimal("R33_AMT_NAME_OF_SUB2"));
			obj.setR33_amt_name_of_sub3(rs.getBigDecimal("R33_AMT_NAME_OF_SUB3"));
			obj.setR33_amt_name_of_sub4(rs.getBigDecimal("R33_AMT_NAME_OF_SUB4"));
			obj.setR33_amt_name_of_sub5(rs.getBigDecimal("R33_AMT_NAME_OF_SUB5"));
			obj.setR33_tot_amt(rs.getBigDecimal("R33_TOT_AMT"));

			// R34
			obj.setR34_item(rs.getString("R34_ITEM"));
			obj.setR34_amt_name_of_sub1(rs.getBigDecimal("R34_AMT_NAME_OF_SUB1"));
			obj.setR34_amt_name_of_sub2(rs.getBigDecimal("R34_AMT_NAME_OF_SUB2"));
			obj.setR34_amt_name_of_sub3(rs.getBigDecimal("R34_AMT_NAME_OF_SUB3"));
			obj.setR34_amt_name_of_sub4(rs.getBigDecimal("R34_AMT_NAME_OF_SUB4"));
			obj.setR34_amt_name_of_sub5(rs.getBigDecimal("R34_AMT_NAME_OF_SUB5"));
			obj.setR34_tot_amt(rs.getBigDecimal("R34_TOT_AMT"));

			// R36
			obj.setR36_item(rs.getString("R36_ITEM"));
			obj.setR36_amt_name_of_sub1(rs.getBigDecimal("R36_AMT_NAME_OF_SUB1"));
			obj.setR36_amt_name_of_sub2(rs.getBigDecimal("R36_AMT_NAME_OF_SUB2"));
			obj.setR36_amt_name_of_sub3(rs.getBigDecimal("R36_AMT_NAME_OF_SUB3"));
			obj.setR36_amt_name_of_sub4(rs.getBigDecimal("R36_AMT_NAME_OF_SUB4"));
			obj.setR36_amt_name_of_sub5(rs.getBigDecimal("R36_AMT_NAME_OF_SUB5"));
			obj.setR36_tot_amt(rs.getBigDecimal("R36_TOT_AMT"));

			// R37
			obj.setR37_item(rs.getString("R37_ITEM"));
			obj.setR37_amt_name_of_sub1(rs.getBigDecimal("R37_AMT_NAME_OF_SUB1"));
			obj.setR37_amt_name_of_sub2(rs.getBigDecimal("R37_AMT_NAME_OF_SUB2"));
			obj.setR37_amt_name_of_sub3(rs.getBigDecimal("R37_AMT_NAME_OF_SUB3"));
			obj.setR37_amt_name_of_sub4(rs.getBigDecimal("R37_AMT_NAME_OF_SUB4"));
			obj.setR37_amt_name_of_sub5(rs.getBigDecimal("R37_AMT_NAME_OF_SUB5"));
			obj.setR37_tot_amt(rs.getBigDecimal("R37_TOT_AMT"));

			// R38
			obj.setR38_item(rs.getString("R38_ITEM"));
			obj.setR38_amt_name_of_sub1(rs.getBigDecimal("R38_AMT_NAME_OF_SUB1"));
			obj.setR38_amt_name_of_sub2(rs.getBigDecimal("R38_AMT_NAME_OF_SUB2"));
			obj.setR38_amt_name_of_sub3(rs.getBigDecimal("R38_AMT_NAME_OF_SUB3"));
			obj.setR38_amt_name_of_sub4(rs.getBigDecimal("R38_AMT_NAME_OF_SUB4"));
			obj.setR38_amt_name_of_sub5(rs.getBigDecimal("R38_AMT_NAME_OF_SUB5"));
			obj.setR38_tot_amt(rs.getBigDecimal("R38_TOT_AMT"));

			// R39
			obj.setR39_item(rs.getString("R39_ITEM"));
			obj.setR39_amt_name_of_sub1(rs.getBigDecimal("R39_AMT_NAME_OF_SUB1"));
			obj.setR39_amt_name_of_sub2(rs.getBigDecimal("R39_AMT_NAME_OF_SUB2"));
			obj.setR39_amt_name_of_sub3(rs.getBigDecimal("R39_AMT_NAME_OF_SUB3"));
			obj.setR39_amt_name_of_sub4(rs.getBigDecimal("R39_AMT_NAME_OF_SUB4"));
			obj.setR39_amt_name_of_sub5(rs.getBigDecimal("R39_AMT_NAME_OF_SUB5"));
			obj.setR39_tot_amt(rs.getBigDecimal("R39_TOT_AMT"));

			// R40
			obj.setR40_item(rs.getString("R40_ITEM"));
			obj.setR40_amt_name_of_sub1(rs.getBigDecimal("R40_AMT_NAME_OF_SUB1"));
			obj.setR40_amt_name_of_sub2(rs.getBigDecimal("R40_AMT_NAME_OF_SUB2"));
			obj.setR40_amt_name_of_sub3(rs.getBigDecimal("R40_AMT_NAME_OF_SUB3"));
			obj.setR40_amt_name_of_sub4(rs.getBigDecimal("R40_AMT_NAME_OF_SUB4"));
			obj.setR40_amt_name_of_sub5(rs.getBigDecimal("R40_AMT_NAME_OF_SUB5"));
			obj.setR40_tot_amt(rs.getBigDecimal("R40_TOT_AMT"));

			// R41
			obj.setR41_item(rs.getString("R41_ITEM"));
			obj.setR41_amt_name_of_sub1(rs.getBigDecimal("R41_AMT_NAME_OF_SUB1"));
			obj.setR41_amt_name_of_sub2(rs.getBigDecimal("R41_AMT_NAME_OF_SUB2"));
			obj.setR41_amt_name_of_sub3(rs.getBigDecimal("R41_AMT_NAME_OF_SUB3"));
			obj.setR41_amt_name_of_sub4(rs.getBigDecimal("R41_AMT_NAME_OF_SUB4"));
			obj.setR41_amt_name_of_sub5(rs.getBigDecimal("R41_AMT_NAME_OF_SUB5"));
			obj.setR41_tot_amt(rs.getBigDecimal("R41_TOT_AMT"));

			// R42
			obj.setR42_item(rs.getString("R42_ITEM"));
			obj.setR42_amt_name_of_sub1(rs.getBigDecimal("R42_AMT_NAME_OF_SUB1"));
			obj.setR42_amt_name_of_sub2(rs.getBigDecimal("R42_AMT_NAME_OF_SUB2"));
			obj.setR42_amt_name_of_sub3(rs.getBigDecimal("R42_AMT_NAME_OF_SUB3"));
			obj.setR42_amt_name_of_sub4(rs.getBigDecimal("R42_AMT_NAME_OF_SUB4"));
			obj.setR42_amt_name_of_sub5(rs.getBigDecimal("R42_AMT_NAME_OF_SUB5"));
			obj.setR42_tot_amt(rs.getBigDecimal("R42_TOT_AMT"));

			// R43
			obj.setR43_item(rs.getString("R43_ITEM"));
			obj.setR43_amt_name_of_sub1(rs.getBigDecimal("R43_AMT_NAME_OF_SUB1"));
			obj.setR43_amt_name_of_sub2(rs.getBigDecimal("R43_AMT_NAME_OF_SUB2"));
			obj.setR43_amt_name_of_sub3(rs.getBigDecimal("R43_AMT_NAME_OF_SUB3"));
			obj.setR43_amt_name_of_sub4(rs.getBigDecimal("R43_AMT_NAME_OF_SUB4"));
			obj.setR43_amt_name_of_sub5(rs.getBigDecimal("R43_AMT_NAME_OF_SUB5"));
			obj.setR43_tot_amt(rs.getBigDecimal("R43_TOT_AMT"));

			// R44
			obj.setR44_item(rs.getString("R44_ITEM"));
			obj.setR44_amt_name_of_sub1(rs.getBigDecimal("R44_AMT_NAME_OF_SUB1"));
			obj.setR44_amt_name_of_sub2(rs.getBigDecimal("R44_AMT_NAME_OF_SUB2"));
			obj.setR44_amt_name_of_sub3(rs.getBigDecimal("R44_AMT_NAME_OF_SUB3"));
			obj.setR44_amt_name_of_sub4(rs.getBigDecimal("R44_AMT_NAME_OF_SUB4"));
			obj.setR44_amt_name_of_sub5(rs.getBigDecimal("R44_AMT_NAME_OF_SUB5"));
			obj.setR44_tot_amt(rs.getBigDecimal("R44_TOT_AMT"));

			// R45
			obj.setR45_item(rs.getString("R45_ITEM"));
			obj.setR45_amt_name_of_sub1(rs.getBigDecimal("R45_AMT_NAME_OF_SUB1"));
			obj.setR45_amt_name_of_sub2(rs.getBigDecimal("R45_AMT_NAME_OF_SUB2"));
			obj.setR45_amt_name_of_sub3(rs.getBigDecimal("R45_AMT_NAME_OF_SUB3"));
			obj.setR45_amt_name_of_sub4(rs.getBigDecimal("R45_AMT_NAME_OF_SUB4"));
			obj.setR45_amt_name_of_sub5(rs.getBigDecimal("R45_AMT_NAME_OF_SUB5"));
			obj.setR45_tot_amt(rs.getBigDecimal("R45_TOT_AMT"));

			// R46
			obj.setR46_item(rs.getString("R46_ITEM"));
			obj.setR46_amt_name_of_sub1(rs.getBigDecimal("R46_AMT_NAME_OF_SUB1"));
			obj.setR46_amt_name_of_sub2(rs.getBigDecimal("R46_AMT_NAME_OF_SUB2"));
			obj.setR46_amt_name_of_sub3(rs.getBigDecimal("R46_AMT_NAME_OF_SUB3"));
			obj.setR46_amt_name_of_sub4(rs.getBigDecimal("R46_AMT_NAME_OF_SUB4"));
			obj.setR46_amt_name_of_sub5(rs.getBigDecimal("R46_AMT_NAME_OF_SUB5"));
			obj.setR46_tot_amt(rs.getBigDecimal("R46_TOT_AMT"));

			// R47
			obj.setR47_item(rs.getString("R47_ITEM"));
			obj.setR47_amt_name_of_sub1(rs.getBigDecimal("R47_AMT_NAME_OF_SUB1"));
			obj.setR47_amt_name_of_sub2(rs.getBigDecimal("R47_AMT_NAME_OF_SUB2"));
			obj.setR47_amt_name_of_sub3(rs.getBigDecimal("R47_AMT_NAME_OF_SUB3"));
			obj.setR47_amt_name_of_sub4(rs.getBigDecimal("R47_AMT_NAME_OF_SUB4"));
			obj.setR47_amt_name_of_sub5(rs.getBigDecimal("R47_AMT_NAME_OF_SUB5"));
			obj.setR47_tot_amt(rs.getBigDecimal("R47_TOT_AMT"));

			// R48
			obj.setR48_item(rs.getString("R48_ITEM"));
			obj.setR48_amt_name_of_sub1(rs.getBigDecimal("R48_AMT_NAME_OF_SUB1"));
			obj.setR48_amt_name_of_sub2(rs.getBigDecimal("R48_AMT_NAME_OF_SUB2"));
			obj.setR48_amt_name_of_sub3(rs.getBigDecimal("R48_AMT_NAME_OF_SUB3"));
			obj.setR48_amt_name_of_sub4(rs.getBigDecimal("R48_AMT_NAME_OF_SUB4"));
			obj.setR48_amt_name_of_sub5(rs.getBigDecimal("R48_AMT_NAME_OF_SUB5"));
			obj.setR48_tot_amt(rs.getBigDecimal("R48_TOT_AMT"));

			// R49
			obj.setR49_item(rs.getString("R49_ITEM"));
			obj.setR49_amt_name_of_sub1(rs.getBigDecimal("R49_AMT_NAME_OF_SUB1"));
			obj.setR49_amt_name_of_sub2(rs.getBigDecimal("R49_AMT_NAME_OF_SUB2"));
			obj.setR49_amt_name_of_sub3(rs.getBigDecimal("R49_AMT_NAME_OF_SUB3"));
			obj.setR49_amt_name_of_sub4(rs.getBigDecimal("R49_AMT_NAME_OF_SUB4"));
			obj.setR49_amt_name_of_sub5(rs.getBigDecimal("R49_AMT_NAME_OF_SUB5"));
			obj.setR49_tot_amt(rs.getBigDecimal("R49_TOT_AMT"));

			// R50
			obj.setR50_item(rs.getString("R50_ITEM"));
			obj.setR50_amt_name_of_sub1(rs.getBigDecimal("R50_AMT_NAME_OF_SUB1"));
			obj.setR50_amt_name_of_sub2(rs.getBigDecimal("R50_AMT_NAME_OF_SUB2"));
			obj.setR50_amt_name_of_sub3(rs.getBigDecimal("R50_AMT_NAME_OF_SUB3"));
			obj.setR50_amt_name_of_sub4(rs.getBigDecimal("R50_AMT_NAME_OF_SUB4"));
			obj.setR50_amt_name_of_sub5(rs.getBigDecimal("R50_AMT_NAME_OF_SUB5"));
			obj.setR50_tot_amt(rs.getBigDecimal("R50_TOT_AMT"));

			// R51
			obj.setR51_item(rs.getString("R51_ITEM"));
			obj.setR51_amt_name_of_sub1(rs.getBigDecimal("R51_AMT_NAME_OF_SUB1"));
			obj.setR51_amt_name_of_sub2(rs.getBigDecimal("R51_AMT_NAME_OF_SUB2"));
			obj.setR51_amt_name_of_sub3(rs.getBigDecimal("R51_AMT_NAME_OF_SUB3"));
			obj.setR51_amt_name_of_sub4(rs.getBigDecimal("R51_AMT_NAME_OF_SUB4"));
			obj.setR51_amt_name_of_sub5(rs.getBigDecimal("R51_AMT_NAME_OF_SUB5"));
			obj.setR51_tot_amt(rs.getBigDecimal("R51_TOT_AMT"));

			// R52
			obj.setR52_item(rs.getString("R52_ITEM"));
			obj.setR52_amt_name_of_sub1(rs.getBigDecimal("R52_AMT_NAME_OF_SUB1"));
			obj.setR52_amt_name_of_sub2(rs.getBigDecimal("R52_AMT_NAME_OF_SUB2"));
			obj.setR52_amt_name_of_sub3(rs.getBigDecimal("R52_AMT_NAME_OF_SUB3"));
			obj.setR52_amt_name_of_sub4(rs.getBigDecimal("R52_AMT_NAME_OF_SUB4"));
			obj.setR52_amt_name_of_sub5(rs.getBigDecimal("R52_AMT_NAME_OF_SUB5"));
			obj.setR52_tot_amt(rs.getBigDecimal("R52_TOT_AMT"));

			// R53
			obj.setR53_item(rs.getString("R53_ITEM"));
			obj.setR53_amt_name_of_sub1(rs.getBigDecimal("R53_AMT_NAME_OF_SUB1"));
			obj.setR53_amt_name_of_sub2(rs.getBigDecimal("R53_AMT_NAME_OF_SUB2"));
			obj.setR53_amt_name_of_sub3(rs.getBigDecimal("R53_AMT_NAME_OF_SUB3"));
			obj.setR53_amt_name_of_sub4(rs.getBigDecimal("R53_AMT_NAME_OF_SUB4"));
			obj.setR53_amt_name_of_sub5(rs.getBigDecimal("R53_AMT_NAME_OF_SUB5"));
			obj.setR53_tot_amt(rs.getBigDecimal("R53_TOT_AMT"));

			// R54
			obj.setR54_item(rs.getString("R54_ITEM"));
			obj.setR54_amt_name_of_sub1(rs.getBigDecimal("R54_AMT_NAME_OF_SUB1"));
			obj.setR54_amt_name_of_sub2(rs.getBigDecimal("R54_AMT_NAME_OF_SUB2"));
			obj.setR54_amt_name_of_sub3(rs.getBigDecimal("R54_AMT_NAME_OF_SUB3"));
			obj.setR54_amt_name_of_sub4(rs.getBigDecimal("R54_AMT_NAME_OF_SUB4"));
			obj.setR54_amt_name_of_sub5(rs.getBigDecimal("R54_AMT_NAME_OF_SUB5"));
			obj.setR54_tot_amt(rs.getBigDecimal("R54_TOT_AMT"));

			// R55
			obj.setR55_item(rs.getString("R55_ITEM"));
			obj.setR55_amt_name_of_sub1(rs.getBigDecimal("R55_AMT_NAME_OF_SUB1"));
			obj.setR55_amt_name_of_sub2(rs.getBigDecimal("R55_AMT_NAME_OF_SUB2"));
			obj.setR55_amt_name_of_sub3(rs.getBigDecimal("R55_AMT_NAME_OF_SUB3"));
			obj.setR55_amt_name_of_sub4(rs.getBigDecimal("R55_AMT_NAME_OF_SUB4"));
			obj.setR55_amt_name_of_sub5(rs.getBigDecimal("R55_AMT_NAME_OF_SUB5"));
			obj.setR55_tot_amt(rs.getBigDecimal("R55_TOT_AMT"));

			// R56
			obj.setR56_item(rs.getString("R56_ITEM"));
			obj.setR56_amt_name_of_sub1(rs.getBigDecimal("R56_AMT_NAME_OF_SUB1"));
			obj.setR56_amt_name_of_sub2(rs.getBigDecimal("R56_AMT_NAME_OF_SUB2"));
			obj.setR56_amt_name_of_sub3(rs.getBigDecimal("R56_AMT_NAME_OF_SUB3"));
			obj.setR56_amt_name_of_sub4(rs.getBigDecimal("R56_AMT_NAME_OF_SUB4"));
			obj.setR56_amt_name_of_sub5(rs.getBigDecimal("R56_AMT_NAME_OF_SUB5"));
			obj.setR56_tot_amt(rs.getBigDecimal("R56_TOT_AMT"));

			// R57
			obj.setR57_item(rs.getString("R57_ITEM"));
			obj.setR57_amt_name_of_sub1(rs.getBigDecimal("R57_AMT_NAME_OF_SUB1"));
			obj.setR57_amt_name_of_sub2(rs.getBigDecimal("R57_AMT_NAME_OF_SUB2"));
			obj.setR57_amt_name_of_sub3(rs.getBigDecimal("R57_AMT_NAME_OF_SUB3"));
			obj.setR57_amt_name_of_sub4(rs.getBigDecimal("R57_AMT_NAME_OF_SUB4"));
			obj.setR57_amt_name_of_sub5(rs.getBigDecimal("R57_AMT_NAME_OF_SUB5"));
			obj.setR57_tot_amt(rs.getBigDecimal("R57_TOT_AMT"));

			// R58
			obj.setR58_item(rs.getString("R58_ITEM"));
			obj.setR58_amt_name_of_sub1(rs.getBigDecimal("R58_AMT_NAME_OF_SUB1"));
			obj.setR58_amt_name_of_sub2(rs.getBigDecimal("R58_AMT_NAME_OF_SUB2"));
			obj.setR58_amt_name_of_sub3(rs.getBigDecimal("R58_AMT_NAME_OF_SUB3"));
			obj.setR58_amt_name_of_sub4(rs.getBigDecimal("R58_AMT_NAME_OF_SUB4"));
			obj.setR58_amt_name_of_sub5(rs.getBigDecimal("R58_AMT_NAME_OF_SUB5"));
			obj.setR58_tot_amt(rs.getBigDecimal("R58_TOT_AMT"));

			// Metadata fields
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE")); // extra field
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));

			return obj;
		}
	}

	class M_CA4ArchivalDetailRowMapper implements RowMapper<M_CA4_Archival_Detail_Entity> {
		@Override
		public M_CA4_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA4_Archival_Detail_Entity obj = new M_CA4_Archival_Detail_Entity();

			// R10
			obj.setR10_item(rs.getString("R10_ITEM"));
			obj.setR10_amt_name_of_sub1(rs.getBigDecimal("R10_AMT_NAME_OF_SUB1"));
			obj.setR10_amt_name_of_sub2(rs.getBigDecimal("R10_AMT_NAME_OF_SUB2"));
			obj.setR10_amt_name_of_sub3(rs.getBigDecimal("R10_AMT_NAME_OF_SUB3"));
			obj.setR10_amt_name_of_sub4(rs.getBigDecimal("R10_AMT_NAME_OF_SUB4"));
			obj.setR10_amt_name_of_sub5(rs.getBigDecimal("R10_AMT_NAME_OF_SUB5"));
			obj.setR10_tot_amt(rs.getBigDecimal("R10_TOT_AMT"));

			// R11
			obj.setR11_item(rs.getString("R11_ITEM"));
			obj.setR11_amt_name_of_sub1(rs.getBigDecimal("R11_AMT_NAME_OF_SUB1"));
			obj.setR11_amt_name_of_sub2(rs.getBigDecimal("R11_AMT_NAME_OF_SUB2"));
			obj.setR11_amt_name_of_sub3(rs.getBigDecimal("R11_AMT_NAME_OF_SUB3"));
			obj.setR11_amt_name_of_sub4(rs.getBigDecimal("R11_AMT_NAME_OF_SUB4"));
			obj.setR11_amt_name_of_sub5(rs.getBigDecimal("R11_AMT_NAME_OF_SUB5"));
			obj.setR11_tot_amt(rs.getBigDecimal("R11_TOT_AMT"));

			// R12
			obj.setR12_item(rs.getString("R12_ITEM"));
			obj.setR12_amt_name_of_sub1(rs.getBigDecimal("R12_AMT_NAME_OF_SUB1"));
			obj.setR12_amt_name_of_sub2(rs.getBigDecimal("R12_AMT_NAME_OF_SUB2"));
			obj.setR12_amt_name_of_sub3(rs.getBigDecimal("R12_AMT_NAME_OF_SUB3"));
			obj.setR12_amt_name_of_sub4(rs.getBigDecimal("R12_AMT_NAME_OF_SUB4"));
			obj.setR12_amt_name_of_sub5(rs.getBigDecimal("R12_AMT_NAME_OF_SUB5"));
			obj.setR12_tot_amt(rs.getBigDecimal("R12_TOT_AMT"));

			// R13
			obj.setR13_item(rs.getString("R13_ITEM"));
			obj.setR13_amt_name_of_sub1(rs.getBigDecimal("R13_AMT_NAME_OF_SUB1"));
			obj.setR13_amt_name_of_sub2(rs.getBigDecimal("R13_AMT_NAME_OF_SUB2"));
			obj.setR13_amt_name_of_sub3(rs.getBigDecimal("R13_AMT_NAME_OF_SUB3"));
			obj.setR13_amt_name_of_sub4(rs.getBigDecimal("R13_AMT_NAME_OF_SUB4"));
			obj.setR13_amt_name_of_sub5(rs.getBigDecimal("R13_AMT_NAME_OF_SUB5"));
			obj.setR13_tot_amt(rs.getBigDecimal("R13_TOT_AMT"));

			// R14
			obj.setR14_item(rs.getString("R14_ITEM"));
			obj.setR14_amt_name_of_sub1(rs.getBigDecimal("R14_AMT_NAME_OF_SUB1"));
			obj.setR14_amt_name_of_sub2(rs.getBigDecimal("R14_AMT_NAME_OF_SUB2"));
			obj.setR14_amt_name_of_sub3(rs.getBigDecimal("R14_AMT_NAME_OF_SUB3"));
			obj.setR14_amt_name_of_sub4(rs.getBigDecimal("R14_AMT_NAME_OF_SUB4"));
			obj.setR14_amt_name_of_sub5(rs.getBigDecimal("R14_AMT_NAME_OF_SUB5"));
			obj.setR14_tot_amt(rs.getBigDecimal("R14_TOT_AMT"));

			// R17
			obj.setR17_item(rs.getString("R17_ITEM"));
			obj.setR17_amt_name_of_sub1(rs.getBigDecimal("R17_AMT_NAME_OF_SUB1"));
			obj.setR17_amt_name_of_sub2(rs.getBigDecimal("R17_AMT_NAME_OF_SUB2"));
			obj.setR17_amt_name_of_sub3(rs.getBigDecimal("R17_AMT_NAME_OF_SUB3"));
			obj.setR17_amt_name_of_sub4(rs.getBigDecimal("R17_AMT_NAME_OF_SUB4"));
			obj.setR17_amt_name_of_sub5(rs.getBigDecimal("R17_AMT_NAME_OF_SUB5"));
			obj.setR17_tot_amt(rs.getBigDecimal("R17_TOT_AMT"));

			// R18
			obj.setR18_item(rs.getString("R18_ITEM"));
			obj.setR18_amt_name_of_sub1(rs.getBigDecimal("R18_AMT_NAME_OF_SUB1"));
			obj.setR18_amt_name_of_sub2(rs.getBigDecimal("R18_AMT_NAME_OF_SUB2"));
			obj.setR18_amt_name_of_sub3(rs.getBigDecimal("R18_AMT_NAME_OF_SUB3"));
			obj.setR18_amt_name_of_sub4(rs.getBigDecimal("R18_AMT_NAME_OF_SUB4"));
			obj.setR18_amt_name_of_sub5(rs.getBigDecimal("R18_AMT_NAME_OF_SUB5"));
			obj.setR18_tot_amt(rs.getBigDecimal("R18_TOT_AMT"));

			// R19
			obj.setR19_item(rs.getString("R19_ITEM"));
			obj.setR19_amt_name_of_sub1(rs.getBigDecimal("R19_AMT_NAME_OF_SUB1"));
			obj.setR19_amt_name_of_sub2(rs.getBigDecimal("R19_AMT_NAME_OF_SUB2"));
			obj.setR19_amt_name_of_sub3(rs.getBigDecimal("R19_AMT_NAME_OF_SUB3"));
			obj.setR19_amt_name_of_sub4(rs.getBigDecimal("R19_AMT_NAME_OF_SUB4"));
			obj.setR19_amt_name_of_sub5(rs.getBigDecimal("R19_AMT_NAME_OF_SUB5"));
			obj.setR19_tot_amt(rs.getBigDecimal("R19_TOT_AMT"));

			// R22
			obj.setR22_item(rs.getString("R22_ITEM"));
			obj.setR22_amt_name_of_sub1(rs.getBigDecimal("R22_AMT_NAME_OF_SUB1"));
			obj.setR22_amt_name_of_sub2(rs.getBigDecimal("R22_AMT_NAME_OF_SUB2"));
			obj.setR22_amt_name_of_sub3(rs.getBigDecimal("R22_AMT_NAME_OF_SUB3"));
			obj.setR22_amt_name_of_sub4(rs.getBigDecimal("R22_AMT_NAME_OF_SUB4"));
			obj.setR22_amt_name_of_sub5(rs.getBigDecimal("R22_AMT_NAME_OF_SUB5"));
			obj.setR22_tot_amt(rs.getBigDecimal("R22_TOT_AMT"));

			// R23
			obj.setR23_item(rs.getString("R23_ITEM"));
			obj.setR23_amt_name_of_sub1(rs.getBigDecimal("R23_AMT_NAME_OF_SUB1"));
			obj.setR23_amt_name_of_sub2(rs.getBigDecimal("R23_AMT_NAME_OF_SUB2"));
			obj.setR23_amt_name_of_sub3(rs.getBigDecimal("R23_AMT_NAME_OF_SUB3"));
			obj.setR23_amt_name_of_sub4(rs.getBigDecimal("R23_AMT_NAME_OF_SUB4"));
			obj.setR23_amt_name_of_sub5(rs.getBigDecimal("R23_AMT_NAME_OF_SUB5"));
			obj.setR23_tot_amt(rs.getBigDecimal("R23_TOT_AMT"));

			// R24
			obj.setR24_item(rs.getString("R24_ITEM"));
			obj.setR24_amt_name_of_sub1(rs.getBigDecimal("R24_AMT_NAME_OF_SUB1"));
			obj.setR24_amt_name_of_sub2(rs.getBigDecimal("R24_AMT_NAME_OF_SUB2"));
			obj.setR24_amt_name_of_sub3(rs.getBigDecimal("R24_AMT_NAME_OF_SUB3"));
			obj.setR24_amt_name_of_sub4(rs.getBigDecimal("R24_AMT_NAME_OF_SUB4"));
			obj.setR24_amt_name_of_sub5(rs.getBigDecimal("R24_AMT_NAME_OF_SUB5"));
			obj.setR24_tot_amt(rs.getBigDecimal("R24_TOT_AMT"));

			// R27
			obj.setR27_item(rs.getString("R27_ITEM"));
			obj.setR27_amt_name_of_sub1(rs.getBigDecimal("R27_AMT_NAME_OF_SUB1"));
			obj.setR27_amt_name_of_sub2(rs.getBigDecimal("R27_AMT_NAME_OF_SUB2"));
			obj.setR27_amt_name_of_sub3(rs.getBigDecimal("R27_AMT_NAME_OF_SUB3"));
			obj.setR27_amt_name_of_sub4(rs.getBigDecimal("R27_AMT_NAME_OF_SUB4"));
			obj.setR27_amt_name_of_sub5(rs.getBigDecimal("R27_AMT_NAME_OF_SUB5"));
			obj.setR27_tot_amt(rs.getBigDecimal("R27_TOT_AMT"));

			// R28
			obj.setR28_item(rs.getString("R28_ITEM"));
			obj.setR28_amt_name_of_sub1(rs.getBigDecimal("R28_AMT_NAME_OF_SUB1"));
			obj.setR28_amt_name_of_sub2(rs.getBigDecimal("R28_AMT_NAME_OF_SUB2"));
			obj.setR28_amt_name_of_sub3(rs.getBigDecimal("R28_AMT_NAME_OF_SUB3"));
			obj.setR28_amt_name_of_sub4(rs.getBigDecimal("R28_AMT_NAME_OF_SUB4"));
			obj.setR28_amt_name_of_sub5(rs.getBigDecimal("R28_AMT_NAME_OF_SUB5"));
			obj.setR28_tot_amt(rs.getBigDecimal("R28_TOT_AMT"));

			// R29
			obj.setR29_item(rs.getString("R29_ITEM"));
			obj.setR29_amt_name_of_sub1(rs.getBigDecimal("R29_AMT_NAME_OF_SUB1"));
			obj.setR29_amt_name_of_sub2(rs.getBigDecimal("R29_AMT_NAME_OF_SUB2"));
			obj.setR29_amt_name_of_sub3(rs.getBigDecimal("R29_AMT_NAME_OF_SUB3"));
			obj.setR29_amt_name_of_sub4(rs.getBigDecimal("R29_AMT_NAME_OF_SUB4"));
			obj.setR29_amt_name_of_sub5(rs.getBigDecimal("R29_AMT_NAME_OF_SUB5"));
			obj.setR29_tot_amt(rs.getBigDecimal("R29_TOT_AMT"));

			// R32
			obj.setR32_item(rs.getString("R32_ITEM"));
			obj.setR32_amt_name_of_sub1(rs.getBigDecimal("R32_AMT_NAME_OF_SUB1"));
			obj.setR32_amt_name_of_sub2(rs.getBigDecimal("R32_AMT_NAME_OF_SUB2"));
			obj.setR32_amt_name_of_sub3(rs.getBigDecimal("R32_AMT_NAME_OF_SUB3"));
			obj.setR32_amt_name_of_sub4(rs.getBigDecimal("R32_AMT_NAME_OF_SUB4"));
			obj.setR32_amt_name_of_sub5(rs.getBigDecimal("R32_AMT_NAME_OF_SUB5"));
			obj.setR32_tot_amt(rs.getBigDecimal("R32_TOT_AMT"));

			// R33
			obj.setR33_item(rs.getString("R33_ITEM"));
			obj.setR33_amt_name_of_sub1(rs.getBigDecimal("R33_AMT_NAME_OF_SUB1"));
			obj.setR33_amt_name_of_sub2(rs.getBigDecimal("R33_AMT_NAME_OF_SUB2"));
			obj.setR33_amt_name_of_sub3(rs.getBigDecimal("R33_AMT_NAME_OF_SUB3"));
			obj.setR33_amt_name_of_sub4(rs.getBigDecimal("R33_AMT_NAME_OF_SUB4"));
			obj.setR33_amt_name_of_sub5(rs.getBigDecimal("R33_AMT_NAME_OF_SUB5"));
			obj.setR33_tot_amt(rs.getBigDecimal("R33_TOT_AMT"));

			// R34
			obj.setR34_item(rs.getString("R34_ITEM"));
			obj.setR34_amt_name_of_sub1(rs.getBigDecimal("R34_AMT_NAME_OF_SUB1"));
			obj.setR34_amt_name_of_sub2(rs.getBigDecimal("R34_AMT_NAME_OF_SUB2"));
			obj.setR34_amt_name_of_sub3(rs.getBigDecimal("R34_AMT_NAME_OF_SUB3"));
			obj.setR34_amt_name_of_sub4(rs.getBigDecimal("R34_AMT_NAME_OF_SUB4"));
			obj.setR34_amt_name_of_sub5(rs.getBigDecimal("R34_AMT_NAME_OF_SUB5"));
			obj.setR34_tot_amt(rs.getBigDecimal("R34_TOT_AMT"));

			// R36
			obj.setR36_item(rs.getString("R36_ITEM"));
			obj.setR36_amt_name_of_sub1(rs.getBigDecimal("R36_AMT_NAME_OF_SUB1"));
			obj.setR36_amt_name_of_sub2(rs.getBigDecimal("R36_AMT_NAME_OF_SUB2"));
			obj.setR36_amt_name_of_sub3(rs.getBigDecimal("R36_AMT_NAME_OF_SUB3"));
			obj.setR36_amt_name_of_sub4(rs.getBigDecimal("R36_AMT_NAME_OF_SUB4"));
			obj.setR36_amt_name_of_sub5(rs.getBigDecimal("R36_AMT_NAME_OF_SUB5"));
			obj.setR36_tot_amt(rs.getBigDecimal("R36_TOT_AMT"));

			// R37
			obj.setR37_item(rs.getString("R37_ITEM"));
			obj.setR37_amt_name_of_sub1(rs.getBigDecimal("R37_AMT_NAME_OF_SUB1"));
			obj.setR37_amt_name_of_sub2(rs.getBigDecimal("R37_AMT_NAME_OF_SUB2"));
			obj.setR37_amt_name_of_sub3(rs.getBigDecimal("R37_AMT_NAME_OF_SUB3"));
			obj.setR37_amt_name_of_sub4(rs.getBigDecimal("R37_AMT_NAME_OF_SUB4"));
			obj.setR37_amt_name_of_sub5(rs.getBigDecimal("R37_AMT_NAME_OF_SUB5"));
			obj.setR37_tot_amt(rs.getBigDecimal("R37_TOT_AMT"));

			// R38
			obj.setR38_item(rs.getString("R38_ITEM"));
			obj.setR38_amt_name_of_sub1(rs.getBigDecimal("R38_AMT_NAME_OF_SUB1"));
			obj.setR38_amt_name_of_sub2(rs.getBigDecimal("R38_AMT_NAME_OF_SUB2"));
			obj.setR38_amt_name_of_sub3(rs.getBigDecimal("R38_AMT_NAME_OF_SUB3"));
			obj.setR38_amt_name_of_sub4(rs.getBigDecimal("R38_AMT_NAME_OF_SUB4"));
			obj.setR38_amt_name_of_sub5(rs.getBigDecimal("R38_AMT_NAME_OF_SUB5"));
			obj.setR38_tot_amt(rs.getBigDecimal("R38_TOT_AMT"));

			// R39
			obj.setR39_item(rs.getString("R39_ITEM"));
			obj.setR39_amt_name_of_sub1(rs.getBigDecimal("R39_AMT_NAME_OF_SUB1"));
			obj.setR39_amt_name_of_sub2(rs.getBigDecimal("R39_AMT_NAME_OF_SUB2"));
			obj.setR39_amt_name_of_sub3(rs.getBigDecimal("R39_AMT_NAME_OF_SUB3"));
			obj.setR39_amt_name_of_sub4(rs.getBigDecimal("R39_AMT_NAME_OF_SUB4"));
			obj.setR39_amt_name_of_sub5(rs.getBigDecimal("R39_AMT_NAME_OF_SUB5"));
			obj.setR39_tot_amt(rs.getBigDecimal("R39_TOT_AMT"));

			// R40
			obj.setR40_item(rs.getString("R40_ITEM"));
			obj.setR40_amt_name_of_sub1(rs.getBigDecimal("R40_AMT_NAME_OF_SUB1"));
			obj.setR40_amt_name_of_sub2(rs.getBigDecimal("R40_AMT_NAME_OF_SUB2"));
			obj.setR40_amt_name_of_sub3(rs.getBigDecimal("R40_AMT_NAME_OF_SUB3"));
			obj.setR40_amt_name_of_sub4(rs.getBigDecimal("R40_AMT_NAME_OF_SUB4"));
			obj.setR40_amt_name_of_sub5(rs.getBigDecimal("R40_AMT_NAME_OF_SUB5"));
			obj.setR40_tot_amt(rs.getBigDecimal("R40_TOT_AMT"));

			// R41
			obj.setR41_item(rs.getString("R41_ITEM"));
			obj.setR41_amt_name_of_sub1(rs.getBigDecimal("R41_AMT_NAME_OF_SUB1"));
			obj.setR41_amt_name_of_sub2(rs.getBigDecimal("R41_AMT_NAME_OF_SUB2"));
			obj.setR41_amt_name_of_sub3(rs.getBigDecimal("R41_AMT_NAME_OF_SUB3"));
			obj.setR41_amt_name_of_sub4(rs.getBigDecimal("R41_AMT_NAME_OF_SUB4"));
			obj.setR41_amt_name_of_sub5(rs.getBigDecimal("R41_AMT_NAME_OF_SUB5"));
			obj.setR41_tot_amt(rs.getBigDecimal("R41_TOT_AMT"));

			// R42
			obj.setR42_item(rs.getString("R42_ITEM"));
			obj.setR42_amt_name_of_sub1(rs.getBigDecimal("R42_AMT_NAME_OF_SUB1"));
			obj.setR42_amt_name_of_sub2(rs.getBigDecimal("R42_AMT_NAME_OF_SUB2"));
			obj.setR42_amt_name_of_sub3(rs.getBigDecimal("R42_AMT_NAME_OF_SUB3"));
			obj.setR42_amt_name_of_sub4(rs.getBigDecimal("R42_AMT_NAME_OF_SUB4"));
			obj.setR42_amt_name_of_sub5(rs.getBigDecimal("R42_AMT_NAME_OF_SUB5"));
			obj.setR42_tot_amt(rs.getBigDecimal("R42_TOT_AMT"));

			// R43
			obj.setR43_item(rs.getString("R43_ITEM"));
			obj.setR43_amt_name_of_sub1(rs.getBigDecimal("R43_AMT_NAME_OF_SUB1"));
			obj.setR43_amt_name_of_sub2(rs.getBigDecimal("R43_AMT_NAME_OF_SUB2"));
			obj.setR43_amt_name_of_sub3(rs.getBigDecimal("R43_AMT_NAME_OF_SUB3"));
			obj.setR43_amt_name_of_sub4(rs.getBigDecimal("R43_AMT_NAME_OF_SUB4"));
			obj.setR43_amt_name_of_sub5(rs.getBigDecimal("R43_AMT_NAME_OF_SUB5"));
			obj.setR43_tot_amt(rs.getBigDecimal("R43_TOT_AMT"));

			// R44
			obj.setR44_item(rs.getString("R44_ITEM"));
			obj.setR44_amt_name_of_sub1(rs.getBigDecimal("R44_AMT_NAME_OF_SUB1"));
			obj.setR44_amt_name_of_sub2(rs.getBigDecimal("R44_AMT_NAME_OF_SUB2"));
			obj.setR44_amt_name_of_sub3(rs.getBigDecimal("R44_AMT_NAME_OF_SUB3"));
			obj.setR44_amt_name_of_sub4(rs.getBigDecimal("R44_AMT_NAME_OF_SUB4"));
			obj.setR44_amt_name_of_sub5(rs.getBigDecimal("R44_AMT_NAME_OF_SUB5"));
			obj.setR44_tot_amt(rs.getBigDecimal("R44_TOT_AMT"));

			// R45
			obj.setR45_item(rs.getString("R45_ITEM"));
			obj.setR45_amt_name_of_sub1(rs.getBigDecimal("R45_AMT_NAME_OF_SUB1"));
			obj.setR45_amt_name_of_sub2(rs.getBigDecimal("R45_AMT_NAME_OF_SUB2"));
			obj.setR45_amt_name_of_sub3(rs.getBigDecimal("R45_AMT_NAME_OF_SUB3"));
			obj.setR45_amt_name_of_sub4(rs.getBigDecimal("R45_AMT_NAME_OF_SUB4"));
			obj.setR45_amt_name_of_sub5(rs.getBigDecimal("R45_AMT_NAME_OF_SUB5"));
			obj.setR45_tot_amt(rs.getBigDecimal("R45_TOT_AMT"));

			// R46
			obj.setR46_item(rs.getString("R46_ITEM"));
			obj.setR46_amt_name_of_sub1(rs.getBigDecimal("R46_AMT_NAME_OF_SUB1"));
			obj.setR46_amt_name_of_sub2(rs.getBigDecimal("R46_AMT_NAME_OF_SUB2"));
			obj.setR46_amt_name_of_sub3(rs.getBigDecimal("R46_AMT_NAME_OF_SUB3"));
			obj.setR46_amt_name_of_sub4(rs.getBigDecimal("R46_AMT_NAME_OF_SUB4"));
			obj.setR46_amt_name_of_sub5(rs.getBigDecimal("R46_AMT_NAME_OF_SUB5"));
			obj.setR46_tot_amt(rs.getBigDecimal("R46_TOT_AMT"));

			// R47
			obj.setR47_item(rs.getString("R47_ITEM"));
			obj.setR47_amt_name_of_sub1(rs.getBigDecimal("R47_AMT_NAME_OF_SUB1"));
			obj.setR47_amt_name_of_sub2(rs.getBigDecimal("R47_AMT_NAME_OF_SUB2"));
			obj.setR47_amt_name_of_sub3(rs.getBigDecimal("R47_AMT_NAME_OF_SUB3"));
			obj.setR47_amt_name_of_sub4(rs.getBigDecimal("R47_AMT_NAME_OF_SUB4"));
			obj.setR47_amt_name_of_sub5(rs.getBigDecimal("R47_AMT_NAME_OF_SUB5"));
			obj.setR47_tot_amt(rs.getBigDecimal("R47_TOT_AMT"));

			// R48
			obj.setR48_item(rs.getString("R48_ITEM"));
			obj.setR48_amt_name_of_sub1(rs.getBigDecimal("R48_AMT_NAME_OF_SUB1"));
			obj.setR48_amt_name_of_sub2(rs.getBigDecimal("R48_AMT_NAME_OF_SUB2"));
			obj.setR48_amt_name_of_sub3(rs.getBigDecimal("R48_AMT_NAME_OF_SUB3"));
			obj.setR48_amt_name_of_sub4(rs.getBigDecimal("R48_AMT_NAME_OF_SUB4"));
			obj.setR48_amt_name_of_sub5(rs.getBigDecimal("R48_AMT_NAME_OF_SUB5"));
			obj.setR48_tot_amt(rs.getBigDecimal("R48_TOT_AMT"));

			// R49
			obj.setR49_item(rs.getString("R49_ITEM"));
			obj.setR49_amt_name_of_sub1(rs.getBigDecimal("R49_AMT_NAME_OF_SUB1"));
			obj.setR49_amt_name_of_sub2(rs.getBigDecimal("R49_AMT_NAME_OF_SUB2"));
			obj.setR49_amt_name_of_sub3(rs.getBigDecimal("R49_AMT_NAME_OF_SUB3"));
			obj.setR49_amt_name_of_sub4(rs.getBigDecimal("R49_AMT_NAME_OF_SUB4"));
			obj.setR49_amt_name_of_sub5(rs.getBigDecimal("R49_AMT_NAME_OF_SUB5"));
			obj.setR49_tot_amt(rs.getBigDecimal("R49_TOT_AMT"));

			// R50
			obj.setR50_item(rs.getString("R50_ITEM"));
			obj.setR50_amt_name_of_sub1(rs.getBigDecimal("R50_AMT_NAME_OF_SUB1"));
			obj.setR50_amt_name_of_sub2(rs.getBigDecimal("R50_AMT_NAME_OF_SUB2"));
			obj.setR50_amt_name_of_sub3(rs.getBigDecimal("R50_AMT_NAME_OF_SUB3"));
			obj.setR50_amt_name_of_sub4(rs.getBigDecimal("R50_AMT_NAME_OF_SUB4"));
			obj.setR50_amt_name_of_sub5(rs.getBigDecimal("R50_AMT_NAME_OF_SUB5"));
			obj.setR50_tot_amt(rs.getBigDecimal("R50_TOT_AMT"));

			// R51
			obj.setR51_item(rs.getString("R51_ITEM"));
			obj.setR51_amt_name_of_sub1(rs.getBigDecimal("R51_AMT_NAME_OF_SUB1"));
			obj.setR51_amt_name_of_sub2(rs.getBigDecimal("R51_AMT_NAME_OF_SUB2"));
			obj.setR51_amt_name_of_sub3(rs.getBigDecimal("R51_AMT_NAME_OF_SUB3"));
			obj.setR51_amt_name_of_sub4(rs.getBigDecimal("R51_AMT_NAME_OF_SUB4"));
			obj.setR51_amt_name_of_sub5(rs.getBigDecimal("R51_AMT_NAME_OF_SUB5"));
			obj.setR51_tot_amt(rs.getBigDecimal("R51_TOT_AMT"));

			// R52
			obj.setR52_item(rs.getString("R52_ITEM"));
			obj.setR52_amt_name_of_sub1(rs.getBigDecimal("R52_AMT_NAME_OF_SUB1"));
			obj.setR52_amt_name_of_sub2(rs.getBigDecimal("R52_AMT_NAME_OF_SUB2"));
			obj.setR52_amt_name_of_sub3(rs.getBigDecimal("R52_AMT_NAME_OF_SUB3"));
			obj.setR52_amt_name_of_sub4(rs.getBigDecimal("R52_AMT_NAME_OF_SUB4"));
			obj.setR52_amt_name_of_sub5(rs.getBigDecimal("R52_AMT_NAME_OF_SUB5"));
			obj.setR52_tot_amt(rs.getBigDecimal("R52_TOT_AMT"));

			// R53
			obj.setR53_item(rs.getString("R53_ITEM"));
			obj.setR53_amt_name_of_sub1(rs.getBigDecimal("R53_AMT_NAME_OF_SUB1"));
			obj.setR53_amt_name_of_sub2(rs.getBigDecimal("R53_AMT_NAME_OF_SUB2"));
			obj.setR53_amt_name_of_sub3(rs.getBigDecimal("R53_AMT_NAME_OF_SUB3"));
			obj.setR53_amt_name_of_sub4(rs.getBigDecimal("R53_AMT_NAME_OF_SUB4"));
			obj.setR53_amt_name_of_sub5(rs.getBigDecimal("R53_AMT_NAME_OF_SUB5"));
			obj.setR53_tot_amt(rs.getBigDecimal("R53_TOT_AMT"));

			// R54
			obj.setR54_item(rs.getString("R54_ITEM"));
			obj.setR54_amt_name_of_sub1(rs.getBigDecimal("R54_AMT_NAME_OF_SUB1"));
			obj.setR54_amt_name_of_sub2(rs.getBigDecimal("R54_AMT_NAME_OF_SUB2"));
			obj.setR54_amt_name_of_sub3(rs.getBigDecimal("R54_AMT_NAME_OF_SUB3"));
			obj.setR54_amt_name_of_sub4(rs.getBigDecimal("R54_AMT_NAME_OF_SUB4"));
			obj.setR54_amt_name_of_sub5(rs.getBigDecimal("R54_AMT_NAME_OF_SUB5"));
			obj.setR54_tot_amt(rs.getBigDecimal("R54_TOT_AMT"));

			// R55
			obj.setR55_item(rs.getString("R55_ITEM"));
			obj.setR55_amt_name_of_sub1(rs.getBigDecimal("R55_AMT_NAME_OF_SUB1"));
			obj.setR55_amt_name_of_sub2(rs.getBigDecimal("R55_AMT_NAME_OF_SUB2"));
			obj.setR55_amt_name_of_sub3(rs.getBigDecimal("R55_AMT_NAME_OF_SUB3"));
			obj.setR55_amt_name_of_sub4(rs.getBigDecimal("R55_AMT_NAME_OF_SUB4"));
			obj.setR55_amt_name_of_sub5(rs.getBigDecimal("R55_AMT_NAME_OF_SUB5"));
			obj.setR55_tot_amt(rs.getBigDecimal("R55_TOT_AMT"));

			// R56
			obj.setR56_item(rs.getString("R56_ITEM"));
			obj.setR56_amt_name_of_sub1(rs.getBigDecimal("R56_AMT_NAME_OF_SUB1"));
			obj.setR56_amt_name_of_sub2(rs.getBigDecimal("R56_AMT_NAME_OF_SUB2"));
			obj.setR56_amt_name_of_sub3(rs.getBigDecimal("R56_AMT_NAME_OF_SUB3"));
			obj.setR56_amt_name_of_sub4(rs.getBigDecimal("R56_AMT_NAME_OF_SUB4"));
			obj.setR56_amt_name_of_sub5(rs.getBigDecimal("R56_AMT_NAME_OF_SUB5"));
			obj.setR56_tot_amt(rs.getBigDecimal("R56_TOT_AMT"));

			// R57
			obj.setR57_item(rs.getString("R57_ITEM"));
			obj.setR57_amt_name_of_sub1(rs.getBigDecimal("R57_AMT_NAME_OF_SUB1"));
			obj.setR57_amt_name_of_sub2(rs.getBigDecimal("R57_AMT_NAME_OF_SUB2"));
			obj.setR57_amt_name_of_sub3(rs.getBigDecimal("R57_AMT_NAME_OF_SUB3"));
			obj.setR57_amt_name_of_sub4(rs.getBigDecimal("R57_AMT_NAME_OF_SUB4"));
			obj.setR57_amt_name_of_sub5(rs.getBigDecimal("R57_AMT_NAME_OF_SUB5"));
			obj.setR57_tot_amt(rs.getBigDecimal("R57_TOT_AMT"));

			// R58
			obj.setR58_item(rs.getString("R58_ITEM"));
			obj.setR58_amt_name_of_sub1(rs.getBigDecimal("R58_AMT_NAME_OF_SUB1"));
			obj.setR58_amt_name_of_sub2(rs.getBigDecimal("R58_AMT_NAME_OF_SUB2"));
			obj.setR58_amt_name_of_sub3(rs.getBigDecimal("R58_AMT_NAME_OF_SUB3"));
			obj.setR58_amt_name_of_sub4(rs.getBigDecimal("R58_AMT_NAME_OF_SUB4"));
			obj.setR58_amt_name_of_sub5(rs.getBigDecimal("R58_AMT_NAME_OF_SUB5"));
			obj.setR58_tot_amt(rs.getBigDecimal("R58_TOT_AMT"));

			// Metadata fields
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE")); // extra field
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));

			return obj;
		}
	}

	class M_CA4ResubSummaryRowMapper implements RowMapper<M_CA4_Resub_Summary_Entity> {

		@Override
		public M_CA4_Resub_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_CA4_Resub_Summary_Entity obj = new M_CA4_Resub_Summary_Entity();

			// REPORT FIELDS
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));

			// R10
			obj.setR10_item(rs.getString("R10_ITEM"));
			obj.setR10_amt_name_of_sub1(rs.getBigDecimal("R10_AMT_NAME_OF_SUB1"));
			obj.setR10_amt_name_of_sub2(rs.getBigDecimal("R10_AMT_NAME_OF_SUB2"));
			obj.setR10_amt_name_of_sub3(rs.getBigDecimal("R10_AMT_NAME_OF_SUB3"));
			obj.setR10_amt_name_of_sub4(rs.getBigDecimal("R10_AMT_NAME_OF_SUB4"));
			obj.setR10_amt_name_of_sub5(rs.getBigDecimal("R10_AMT_NAME_OF_SUB5"));
			obj.setR10_tot_amt(rs.getBigDecimal("R10_TOT_AMT"));

			// R11
			obj.setR11_item(rs.getString("R11_ITEM"));
			obj.setR11_amt_name_of_sub1(rs.getBigDecimal("R11_AMT_NAME_OF_SUB1"));
			obj.setR11_amt_name_of_sub2(rs.getBigDecimal("R11_AMT_NAME_OF_SUB2"));
			obj.setR11_amt_name_of_sub3(rs.getBigDecimal("R11_AMT_NAME_OF_SUB3"));
			obj.setR11_amt_name_of_sub4(rs.getBigDecimal("R11_AMT_NAME_OF_SUB4"));
			obj.setR11_amt_name_of_sub5(rs.getBigDecimal("R11_AMT_NAME_OF_SUB5"));
			obj.setR11_tot_amt(rs.getBigDecimal("R11_TOT_AMT"));

			// R12
			obj.setR12_item(rs.getString("R12_ITEM"));
			obj.setR12_amt_name_of_sub1(rs.getBigDecimal("R12_AMT_NAME_OF_SUB1"));
			obj.setR12_amt_name_of_sub2(rs.getBigDecimal("R12_AMT_NAME_OF_SUB2"));
			obj.setR12_amt_name_of_sub3(rs.getBigDecimal("R12_AMT_NAME_OF_SUB3"));
			obj.setR12_amt_name_of_sub4(rs.getBigDecimal("R12_AMT_NAME_OF_SUB4"));
			obj.setR12_amt_name_of_sub5(rs.getBigDecimal("R12_AMT_NAME_OF_SUB5"));
			obj.setR12_tot_amt(rs.getBigDecimal("R12_TOT_AMT"));

			// R13
			obj.setR13_item(rs.getString("R13_ITEM"));
			obj.setR13_amt_name_of_sub1(rs.getBigDecimal("R13_AMT_NAME_OF_SUB1"));
			obj.setR13_amt_name_of_sub2(rs.getBigDecimal("R13_AMT_NAME_OF_SUB2"));
			obj.setR13_amt_name_of_sub3(rs.getBigDecimal("R13_AMT_NAME_OF_SUB3"));
			obj.setR13_amt_name_of_sub4(rs.getBigDecimal("R13_AMT_NAME_OF_SUB4"));
			obj.setR13_amt_name_of_sub5(rs.getBigDecimal("R13_AMT_NAME_OF_SUB5"));
			obj.setR13_tot_amt(rs.getBigDecimal("R13_TOT_AMT"));

			// R14
			obj.setR14_item(rs.getString("R14_ITEM"));
			obj.setR14_amt_name_of_sub1(rs.getBigDecimal("R14_AMT_NAME_OF_SUB1"));
			obj.setR14_amt_name_of_sub2(rs.getBigDecimal("R14_AMT_NAME_OF_SUB2"));
			obj.setR14_amt_name_of_sub3(rs.getBigDecimal("R14_AMT_NAME_OF_SUB3"));
			obj.setR14_amt_name_of_sub4(rs.getBigDecimal("R14_AMT_NAME_OF_SUB4"));
			obj.setR14_amt_name_of_sub5(rs.getBigDecimal("R14_AMT_NAME_OF_SUB5"));
			obj.setR14_tot_amt(rs.getBigDecimal("R14_TOT_AMT"));

			// R17
			obj.setR17_item(rs.getString("R17_ITEM"));
			obj.setR17_amt_name_of_sub1(rs.getBigDecimal("R17_AMT_NAME_OF_SUB1"));
			obj.setR17_amt_name_of_sub2(rs.getBigDecimal("R17_AMT_NAME_OF_SUB2"));
			obj.setR17_amt_name_of_sub3(rs.getBigDecimal("R17_AMT_NAME_OF_SUB3"));
			obj.setR17_amt_name_of_sub4(rs.getBigDecimal("R17_AMT_NAME_OF_SUB4"));
			obj.setR17_amt_name_of_sub5(rs.getBigDecimal("R17_AMT_NAME_OF_SUB5"));
			obj.setR17_tot_amt(rs.getBigDecimal("R17_TOT_AMT"));

			// R18
			obj.setR18_item(rs.getString("R18_ITEM"));
			obj.setR18_amt_name_of_sub1(rs.getBigDecimal("R18_AMT_NAME_OF_SUB1"));
			obj.setR18_amt_name_of_sub2(rs.getBigDecimal("R18_AMT_NAME_OF_SUB2"));
			obj.setR18_amt_name_of_sub3(rs.getBigDecimal("R18_AMT_NAME_OF_SUB3"));
			obj.setR18_amt_name_of_sub4(rs.getBigDecimal("R18_AMT_NAME_OF_SUB4"));
			obj.setR18_amt_name_of_sub5(rs.getBigDecimal("R18_AMT_NAME_OF_SUB5"));
			obj.setR18_tot_amt(rs.getBigDecimal("R18_TOT_AMT"));

			// R19
			obj.setR19_item(rs.getString("R19_ITEM"));
			obj.setR19_amt_name_of_sub1(rs.getBigDecimal("R19_AMT_NAME_OF_SUB1"));
			obj.setR19_amt_name_of_sub2(rs.getBigDecimal("R19_AMT_NAME_OF_SUB2"));
			obj.setR19_amt_name_of_sub3(rs.getBigDecimal("R19_AMT_NAME_OF_SUB3"));
			obj.setR19_amt_name_of_sub4(rs.getBigDecimal("R19_AMT_NAME_OF_SUB4"));
			obj.setR19_amt_name_of_sub5(rs.getBigDecimal("R19_AMT_NAME_OF_SUB5"));
			obj.setR19_tot_amt(rs.getBigDecimal("R19_TOT_AMT"));

			// R22
			obj.setR22_item(rs.getString("R22_ITEM"));
			obj.setR22_amt_name_of_sub1(rs.getBigDecimal("R22_AMT_NAME_OF_SUB1"));
			obj.setR22_amt_name_of_sub2(rs.getBigDecimal("R22_AMT_NAME_OF_SUB2"));
			obj.setR22_amt_name_of_sub3(rs.getBigDecimal("R22_AMT_NAME_OF_SUB3"));
			obj.setR22_amt_name_of_sub4(rs.getBigDecimal("R22_AMT_NAME_OF_SUB4"));
			obj.setR22_amt_name_of_sub5(rs.getBigDecimal("R22_AMT_NAME_OF_SUB5"));
			obj.setR22_tot_amt(rs.getBigDecimal("R22_TOT_AMT"));

			// R23
			obj.setR23_item(rs.getString("R23_ITEM"));
			obj.setR23_amt_name_of_sub1(rs.getBigDecimal("R23_AMT_NAME_OF_SUB1"));
			obj.setR23_amt_name_of_sub2(rs.getBigDecimal("R23_AMT_NAME_OF_SUB2"));
			obj.setR23_amt_name_of_sub3(rs.getBigDecimal("R23_AMT_NAME_OF_SUB3"));
			obj.setR23_amt_name_of_sub4(rs.getBigDecimal("R23_AMT_NAME_OF_SUB4"));
			obj.setR23_amt_name_of_sub5(rs.getBigDecimal("R23_AMT_NAME_OF_SUB5"));
			obj.setR23_tot_amt(rs.getBigDecimal("R23_TOT_AMT"));

			// R24
			obj.setR24_item(rs.getString("R24_ITEM"));
			obj.setR24_amt_name_of_sub1(rs.getBigDecimal("R24_AMT_NAME_OF_SUB1"));
			obj.setR24_amt_name_of_sub2(rs.getBigDecimal("R24_AMT_NAME_OF_SUB2"));
			obj.setR24_amt_name_of_sub3(rs.getBigDecimal("R24_AMT_NAME_OF_SUB3"));
			obj.setR24_amt_name_of_sub4(rs.getBigDecimal("R24_AMT_NAME_OF_SUB4"));
			obj.setR24_amt_name_of_sub5(rs.getBigDecimal("R24_AMT_NAME_OF_SUB5"));
			obj.setR24_tot_amt(rs.getBigDecimal("R24_TOT_AMT"));

			// R27
			obj.setR27_item(rs.getString("R27_ITEM"));
			obj.setR27_amt_name_of_sub1(rs.getBigDecimal("R27_AMT_NAME_OF_SUB1"));
			obj.setR27_amt_name_of_sub2(rs.getBigDecimal("R27_AMT_NAME_OF_SUB2"));
			obj.setR27_amt_name_of_sub3(rs.getBigDecimal("R27_AMT_NAME_OF_SUB3"));
			obj.setR27_amt_name_of_sub4(rs.getBigDecimal("R27_AMT_NAME_OF_SUB4"));
			obj.setR27_amt_name_of_sub5(rs.getBigDecimal("R27_AMT_NAME_OF_SUB5"));
			obj.setR27_tot_amt(rs.getBigDecimal("R27_TOT_AMT"));

			// R28
			obj.setR28_item(rs.getString("R28_ITEM"));
			obj.setR28_amt_name_of_sub1(rs.getBigDecimal("R28_AMT_NAME_OF_SUB1"));
			obj.setR28_amt_name_of_sub2(rs.getBigDecimal("R28_AMT_NAME_OF_SUB2"));
			obj.setR28_amt_name_of_sub3(rs.getBigDecimal("R28_AMT_NAME_OF_SUB3"));
			obj.setR28_amt_name_of_sub4(rs.getBigDecimal("R28_AMT_NAME_OF_SUB4"));
			obj.setR28_amt_name_of_sub5(rs.getBigDecimal("R28_AMT_NAME_OF_SUB5"));
			obj.setR28_tot_amt(rs.getBigDecimal("R28_TOT_AMT"));

			// R29
			obj.setR29_item(rs.getString("R29_ITEM"));
			obj.setR29_amt_name_of_sub1(rs.getBigDecimal("R29_AMT_NAME_OF_SUB1"));
			obj.setR29_amt_name_of_sub2(rs.getBigDecimal("R29_AMT_NAME_OF_SUB2"));
			obj.setR29_amt_name_of_sub3(rs.getBigDecimal("R29_AMT_NAME_OF_SUB3"));
			obj.setR29_amt_name_of_sub4(rs.getBigDecimal("R29_AMT_NAME_OF_SUB4"));
			obj.setR29_amt_name_of_sub5(rs.getBigDecimal("R29_AMT_NAME_OF_SUB5"));
			obj.setR29_tot_amt(rs.getBigDecimal("R29_TOT_AMT"));

			// Metadata fields
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE")); // extra field
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));

			return obj;
		}
	}

	class M_CA4ResubDetailRowMapper implements RowMapper<M_CA4_Resub_Detail_Entity> {

		@Override
		public M_CA4_Resub_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_CA4_Resub_Detail_Entity obj = new M_CA4_Resub_Detail_Entity();

			// R10
			obj.setR10_item(rs.getString("R10_ITEM"));
			obj.setR10_amt_name_of_sub1(rs.getBigDecimal("R10_AMT_NAME_OF_SUB1"));
			obj.setR10_amt_name_of_sub2(rs.getBigDecimal("R10_AMT_NAME_OF_SUB2"));
			obj.setR10_amt_name_of_sub3(rs.getBigDecimal("R10_AMT_NAME_OF_SUB3"));
			obj.setR10_amt_name_of_sub4(rs.getBigDecimal("R10_AMT_NAME_OF_SUB4"));
			obj.setR10_amt_name_of_sub5(rs.getBigDecimal("R10_AMT_NAME_OF_SUB5"));
			obj.setR10_tot_amt(rs.getBigDecimal("R10_TOT_AMT"));

			// R11
			obj.setR11_item(rs.getString("R11_ITEM"));
			obj.setR11_amt_name_of_sub1(rs.getBigDecimal("R11_AMT_NAME_OF_SUB1"));
			obj.setR11_amt_name_of_sub2(rs.getBigDecimal("R11_AMT_NAME_OF_SUB2"));
			obj.setR11_amt_name_of_sub3(rs.getBigDecimal("R11_AMT_NAME_OF_SUB3"));
			obj.setR11_amt_name_of_sub4(rs.getBigDecimal("R11_AMT_NAME_OF_SUB4"));
			obj.setR11_amt_name_of_sub5(rs.getBigDecimal("R11_AMT_NAME_OF_SUB5"));
			obj.setR11_tot_amt(rs.getBigDecimal("R11_TOT_AMT"));

			// R12
			obj.setR12_item(rs.getString("R12_ITEM"));
			obj.setR12_amt_name_of_sub1(rs.getBigDecimal("R12_AMT_NAME_OF_SUB1"));
			obj.setR12_amt_name_of_sub2(rs.getBigDecimal("R12_AMT_NAME_OF_SUB2"));
			obj.setR12_amt_name_of_sub3(rs.getBigDecimal("R12_AMT_NAME_OF_SUB3"));
			obj.setR12_amt_name_of_sub4(rs.getBigDecimal("R12_AMT_NAME_OF_SUB4"));
			obj.setR12_amt_name_of_sub5(rs.getBigDecimal("R12_AMT_NAME_OF_SUB5"));
			obj.setR12_tot_amt(rs.getBigDecimal("R12_TOT_AMT"));

			// R13
			obj.setR13_item(rs.getString("R13_ITEM"));
			obj.setR13_amt_name_of_sub1(rs.getBigDecimal("R13_AMT_NAME_OF_SUB1"));
			obj.setR13_amt_name_of_sub2(rs.getBigDecimal("R13_AMT_NAME_OF_SUB2"));
			obj.setR13_amt_name_of_sub3(rs.getBigDecimal("R13_AMT_NAME_OF_SUB3"));
			obj.setR13_amt_name_of_sub4(rs.getBigDecimal("R13_AMT_NAME_OF_SUB4"));
			obj.setR13_amt_name_of_sub5(rs.getBigDecimal("R13_AMT_NAME_OF_SUB5"));
			obj.setR13_tot_amt(rs.getBigDecimal("R13_TOT_AMT"));

			// R14
			obj.setR14_item(rs.getString("R14_ITEM"));
			obj.setR14_amt_name_of_sub1(rs.getBigDecimal("R14_AMT_NAME_OF_SUB1"));
			obj.setR14_amt_name_of_sub2(rs.getBigDecimal("R14_AMT_NAME_OF_SUB2"));
			obj.setR14_amt_name_of_sub3(rs.getBigDecimal("R14_AMT_NAME_OF_SUB3"));
			obj.setR14_amt_name_of_sub4(rs.getBigDecimal("R14_AMT_NAME_OF_SUB4"));
			obj.setR14_amt_name_of_sub5(rs.getBigDecimal("R14_AMT_NAME_OF_SUB5"));
			obj.setR14_tot_amt(rs.getBigDecimal("R14_TOT_AMT"));

			// R17
			obj.setR17_item(rs.getString("R17_ITEM"));
			obj.setR17_amt_name_of_sub1(rs.getBigDecimal("R17_AMT_NAME_OF_SUB1"));
			obj.setR17_amt_name_of_sub2(rs.getBigDecimal("R17_AMT_NAME_OF_SUB2"));
			obj.setR17_amt_name_of_sub3(rs.getBigDecimal("R17_AMT_NAME_OF_SUB3"));
			obj.setR17_amt_name_of_sub4(rs.getBigDecimal("R17_AMT_NAME_OF_SUB4"));
			obj.setR17_amt_name_of_sub5(rs.getBigDecimal("R17_AMT_NAME_OF_SUB5"));
			obj.setR17_tot_amt(rs.getBigDecimal("R17_TOT_AMT"));

			// R18
			obj.setR18_item(rs.getString("R18_ITEM"));
			obj.setR18_amt_name_of_sub1(rs.getBigDecimal("R18_AMT_NAME_OF_SUB1"));
			obj.setR18_amt_name_of_sub2(rs.getBigDecimal("R18_AMT_NAME_OF_SUB2"));
			obj.setR18_amt_name_of_sub3(rs.getBigDecimal("R18_AMT_NAME_OF_SUB3"));
			obj.setR18_amt_name_of_sub4(rs.getBigDecimal("R18_AMT_NAME_OF_SUB4"));
			obj.setR18_amt_name_of_sub5(rs.getBigDecimal("R18_AMT_NAME_OF_SUB5"));
			obj.setR18_tot_amt(rs.getBigDecimal("R18_TOT_AMT"));

			// R19
			obj.setR19_item(rs.getString("R19_ITEM"));
			obj.setR19_amt_name_of_sub1(rs.getBigDecimal("R19_AMT_NAME_OF_SUB1"));
			obj.setR19_amt_name_of_sub2(rs.getBigDecimal("R19_AMT_NAME_OF_SUB2"));
			obj.setR19_amt_name_of_sub3(rs.getBigDecimal("R19_AMT_NAME_OF_SUB3"));
			obj.setR19_amt_name_of_sub4(rs.getBigDecimal("R19_AMT_NAME_OF_SUB4"));
			obj.setR19_amt_name_of_sub5(rs.getBigDecimal("R19_AMT_NAME_OF_SUB5"));
			obj.setR19_tot_amt(rs.getBigDecimal("R19_TOT_AMT"));

			// R22
			obj.setR22_item(rs.getString("R22_ITEM"));
			obj.setR22_amt_name_of_sub1(rs.getBigDecimal("R22_AMT_NAME_OF_SUB1"));
			obj.setR22_amt_name_of_sub2(rs.getBigDecimal("R22_AMT_NAME_OF_SUB2"));
			obj.setR22_amt_name_of_sub3(rs.getBigDecimal("R22_AMT_NAME_OF_SUB3"));
			obj.setR22_amt_name_of_sub4(rs.getBigDecimal("R22_AMT_NAME_OF_SUB4"));
			obj.setR22_amt_name_of_sub5(rs.getBigDecimal("R22_AMT_NAME_OF_SUB5"));
			obj.setR22_tot_amt(rs.getBigDecimal("R22_TOT_AMT"));

			// R23
			obj.setR23_item(rs.getString("R23_ITEM"));
			obj.setR23_amt_name_of_sub1(rs.getBigDecimal("R23_AMT_NAME_OF_SUB1"));
			obj.setR23_amt_name_of_sub2(rs.getBigDecimal("R23_AMT_NAME_OF_SUB2"));
			obj.setR23_amt_name_of_sub3(rs.getBigDecimal("R23_AMT_NAME_OF_SUB3"));
			obj.setR23_amt_name_of_sub4(rs.getBigDecimal("R23_AMT_NAME_OF_SUB4"));
			obj.setR23_amt_name_of_sub5(rs.getBigDecimal("R23_AMT_NAME_OF_SUB5"));
			obj.setR23_tot_amt(rs.getBigDecimal("R23_TOT_AMT"));

			// R24
			obj.setR24_item(rs.getString("R24_ITEM"));
			obj.setR24_amt_name_of_sub1(rs.getBigDecimal("R24_AMT_NAME_OF_SUB1"));
			obj.setR24_amt_name_of_sub2(rs.getBigDecimal("R24_AMT_NAME_OF_SUB2"));
			obj.setR24_amt_name_of_sub3(rs.getBigDecimal("R24_AMT_NAME_OF_SUB3"));
			obj.setR24_amt_name_of_sub4(rs.getBigDecimal("R24_AMT_NAME_OF_SUB4"));
			obj.setR24_amt_name_of_sub5(rs.getBigDecimal("R24_AMT_NAME_OF_SUB5"));
			obj.setR24_tot_amt(rs.getBigDecimal("R24_TOT_AMT"));

			// R27
			obj.setR27_item(rs.getString("R27_ITEM"));
			obj.setR27_amt_name_of_sub1(rs.getBigDecimal("R27_AMT_NAME_OF_SUB1"));
			obj.setR27_amt_name_of_sub2(rs.getBigDecimal("R27_AMT_NAME_OF_SUB2"));
			obj.setR27_amt_name_of_sub3(rs.getBigDecimal("R27_AMT_NAME_OF_SUB3"));
			obj.setR27_amt_name_of_sub4(rs.getBigDecimal("R27_AMT_NAME_OF_SUB4"));
			obj.setR27_amt_name_of_sub5(rs.getBigDecimal("R27_AMT_NAME_OF_SUB5"));
			obj.setR27_tot_amt(rs.getBigDecimal("R27_TOT_AMT"));

			// R28
			obj.setR28_item(rs.getString("R28_ITEM"));
			obj.setR28_amt_name_of_sub1(rs.getBigDecimal("R28_AMT_NAME_OF_SUB1"));
			obj.setR28_amt_name_of_sub2(rs.getBigDecimal("R28_AMT_NAME_OF_SUB2"));
			obj.setR28_amt_name_of_sub3(rs.getBigDecimal("R28_AMT_NAME_OF_SUB3"));
			obj.setR28_amt_name_of_sub4(rs.getBigDecimal("R28_AMT_NAME_OF_SUB4"));
			obj.setR28_amt_name_of_sub5(rs.getBigDecimal("R28_AMT_NAME_OF_SUB5"));
			obj.setR28_tot_amt(rs.getBigDecimal("R28_TOT_AMT"));

			// R29
			obj.setR29_item(rs.getString("R29_ITEM"));
			obj.setR29_amt_name_of_sub1(rs.getBigDecimal("R29_AMT_NAME_OF_SUB1"));
			obj.setR29_amt_name_of_sub2(rs.getBigDecimal("R29_AMT_NAME_OF_SUB2"));
			obj.setR29_amt_name_of_sub3(rs.getBigDecimal("R29_AMT_NAME_OF_SUB3"));
			obj.setR29_amt_name_of_sub4(rs.getBigDecimal("R29_AMT_NAME_OF_SUB4"));
			obj.setR29_amt_name_of_sub5(rs.getBigDecimal("R29_AMT_NAME_OF_SUB5"));
			obj.setR29_tot_amt(rs.getBigDecimal("R29_TOT_AMT"));

			// R32
			obj.setR32_item(rs.getString("R32_ITEM"));
			obj.setR32_amt_name_of_sub1(rs.getBigDecimal("R32_AMT_NAME_OF_SUB1"));
			obj.setR32_amt_name_of_sub2(rs.getBigDecimal("R32_AMT_NAME_OF_SUB2"));
			obj.setR32_amt_name_of_sub3(rs.getBigDecimal("R32_AMT_NAME_OF_SUB3"));
			obj.setR32_amt_name_of_sub4(rs.getBigDecimal("R32_AMT_NAME_OF_SUB4"));
			obj.setR32_amt_name_of_sub5(rs.getBigDecimal("R32_AMT_NAME_OF_SUB5"));
			obj.setR32_tot_amt(rs.getBigDecimal("R32_TOT_AMT"));

			// R33
			obj.setR33_item(rs.getString("R33_ITEM"));
			obj.setR33_amt_name_of_sub1(rs.getBigDecimal("R33_AMT_NAME_OF_SUB1"));
			obj.setR33_amt_name_of_sub2(rs.getBigDecimal("R33_AMT_NAME_OF_SUB2"));
			obj.setR33_amt_name_of_sub3(rs.getBigDecimal("R33_AMT_NAME_OF_SUB3"));
			obj.setR33_amt_name_of_sub4(rs.getBigDecimal("R33_AMT_NAME_OF_SUB4"));
			obj.setR33_amt_name_of_sub5(rs.getBigDecimal("R33_AMT_NAME_OF_SUB5"));
			obj.setR33_tot_amt(rs.getBigDecimal("R33_TOT_AMT"));

			// R34
			obj.setR34_item(rs.getString("R34_ITEM"));
			obj.setR34_amt_name_of_sub1(rs.getBigDecimal("R34_AMT_NAME_OF_SUB1"));
			obj.setR34_amt_name_of_sub2(rs.getBigDecimal("R34_AMT_NAME_OF_SUB2"));
			obj.setR34_amt_name_of_sub3(rs.getBigDecimal("R34_AMT_NAME_OF_SUB3"));
			obj.setR34_amt_name_of_sub4(rs.getBigDecimal("R34_AMT_NAME_OF_SUB4"));
			obj.setR34_amt_name_of_sub5(rs.getBigDecimal("R34_AMT_NAME_OF_SUB5"));
			obj.setR34_tot_amt(rs.getBigDecimal("R34_TOT_AMT"));

			// R36
			obj.setR36_item(rs.getString("R36_ITEM"));
			obj.setR36_amt_name_of_sub1(rs.getBigDecimal("R36_AMT_NAME_OF_SUB1"));
			obj.setR36_amt_name_of_sub2(rs.getBigDecimal("R36_AMT_NAME_OF_SUB2"));
			obj.setR36_amt_name_of_sub3(rs.getBigDecimal("R36_AMT_NAME_OF_SUB3"));
			obj.setR36_amt_name_of_sub4(rs.getBigDecimal("R36_AMT_NAME_OF_SUB4"));
			obj.setR36_amt_name_of_sub5(rs.getBigDecimal("R36_AMT_NAME_OF_SUB5"));
			obj.setR36_tot_amt(rs.getBigDecimal("R36_TOT_AMT"));

			// R37
			obj.setR37_item(rs.getString("R37_ITEM"));
			obj.setR37_amt_name_of_sub1(rs.getBigDecimal("R37_AMT_NAME_OF_SUB1"));
			obj.setR37_amt_name_of_sub2(rs.getBigDecimal("R37_AMT_NAME_OF_SUB2"));
			obj.setR37_amt_name_of_sub3(rs.getBigDecimal("R37_AMT_NAME_OF_SUB3"));
			obj.setR37_amt_name_of_sub4(rs.getBigDecimal("R37_AMT_NAME_OF_SUB4"));
			obj.setR37_amt_name_of_sub5(rs.getBigDecimal("R37_AMT_NAME_OF_SUB5"));
			obj.setR37_tot_amt(rs.getBigDecimal("R37_TOT_AMT"));

			// R38
			obj.setR38_item(rs.getString("R38_ITEM"));
			obj.setR38_amt_name_of_sub1(rs.getBigDecimal("R38_AMT_NAME_OF_SUB1"));
			obj.setR38_amt_name_of_sub2(rs.getBigDecimal("R38_AMT_NAME_OF_SUB2"));
			obj.setR38_amt_name_of_sub3(rs.getBigDecimal("R38_AMT_NAME_OF_SUB3"));
			obj.setR38_amt_name_of_sub4(rs.getBigDecimal("R38_AMT_NAME_OF_SUB4"));
			obj.setR38_amt_name_of_sub5(rs.getBigDecimal("R38_AMT_NAME_OF_SUB5"));
			obj.setR38_tot_amt(rs.getBigDecimal("R38_TOT_AMT"));

			// R39
			obj.setR39_item(rs.getString("R39_ITEM"));
			obj.setR39_amt_name_of_sub1(rs.getBigDecimal("R39_AMT_NAME_OF_SUB1"));
			obj.setR39_amt_name_of_sub2(rs.getBigDecimal("R39_AMT_NAME_OF_SUB2"));
			obj.setR39_amt_name_of_sub3(rs.getBigDecimal("R39_AMT_NAME_OF_SUB3"));
			obj.setR39_amt_name_of_sub4(rs.getBigDecimal("R39_AMT_NAME_OF_SUB4"));
			obj.setR39_amt_name_of_sub5(rs.getBigDecimal("R39_AMT_NAME_OF_SUB5"));
			obj.setR39_tot_amt(rs.getBigDecimal("R39_TOT_AMT"));

			// R40
			obj.setR40_item(rs.getString("R40_ITEM"));
			obj.setR40_amt_name_of_sub1(rs.getBigDecimal("R40_AMT_NAME_OF_SUB1"));
			obj.setR40_amt_name_of_sub2(rs.getBigDecimal("R40_AMT_NAME_OF_SUB2"));
			obj.setR40_amt_name_of_sub3(rs.getBigDecimal("R40_AMT_NAME_OF_SUB3"));
			obj.setR40_amt_name_of_sub4(rs.getBigDecimal("R40_AMT_NAME_OF_SUB4"));
			obj.setR40_amt_name_of_sub5(rs.getBigDecimal("R40_AMT_NAME_OF_SUB5"));
			obj.setR40_tot_amt(rs.getBigDecimal("R40_TOT_AMT"));

			// R41
			obj.setR41_item(rs.getString("R41_ITEM"));
			obj.setR41_amt_name_of_sub1(rs.getBigDecimal("R41_AMT_NAME_OF_SUB1"));
			obj.setR41_amt_name_of_sub2(rs.getBigDecimal("R41_AMT_NAME_OF_SUB2"));
			obj.setR41_amt_name_of_sub3(rs.getBigDecimal("R41_AMT_NAME_OF_SUB3"));
			obj.setR41_amt_name_of_sub4(rs.getBigDecimal("R41_AMT_NAME_OF_SUB4"));
			obj.setR41_amt_name_of_sub5(rs.getBigDecimal("R41_AMT_NAME_OF_SUB5"));
			obj.setR41_tot_amt(rs.getBigDecimal("R41_TOT_AMT"));

			// R42
			obj.setR42_item(rs.getString("R42_ITEM"));
			obj.setR42_amt_name_of_sub1(rs.getBigDecimal("R42_AMT_NAME_OF_SUB1"));
			obj.setR42_amt_name_of_sub2(rs.getBigDecimal("R42_AMT_NAME_OF_SUB2"));
			obj.setR42_amt_name_of_sub3(rs.getBigDecimal("R42_AMT_NAME_OF_SUB3"));
			obj.setR42_amt_name_of_sub4(rs.getBigDecimal("R42_AMT_NAME_OF_SUB4"));
			obj.setR42_amt_name_of_sub5(rs.getBigDecimal("R42_AMT_NAME_OF_SUB5"));
			obj.setR42_tot_amt(rs.getBigDecimal("R42_TOT_AMT"));

			// R43
			obj.setR43_item(rs.getString("R43_ITEM"));
			obj.setR43_amt_name_of_sub1(rs.getBigDecimal("R43_AMT_NAME_OF_SUB1"));
			obj.setR43_amt_name_of_sub2(rs.getBigDecimal("R43_AMT_NAME_OF_SUB2"));
			obj.setR43_amt_name_of_sub3(rs.getBigDecimal("R43_AMT_NAME_OF_SUB3"));
			obj.setR43_amt_name_of_sub4(rs.getBigDecimal("R43_AMT_NAME_OF_SUB4"));
			obj.setR43_amt_name_of_sub5(rs.getBigDecimal("R43_AMT_NAME_OF_SUB5"));
			obj.setR43_tot_amt(rs.getBigDecimal("R43_TOT_AMT"));

			// R44
			obj.setR44_item(rs.getString("R44_ITEM"));
			obj.setR44_amt_name_of_sub1(rs.getBigDecimal("R44_AMT_NAME_OF_SUB1"));
			obj.setR44_amt_name_of_sub2(rs.getBigDecimal("R44_AMT_NAME_OF_SUB2"));
			obj.setR44_amt_name_of_sub3(rs.getBigDecimal("R44_AMT_NAME_OF_SUB3"));
			obj.setR44_amt_name_of_sub4(rs.getBigDecimal("R44_AMT_NAME_OF_SUB4"));
			obj.setR44_amt_name_of_sub5(rs.getBigDecimal("R44_AMT_NAME_OF_SUB5"));
			obj.setR44_tot_amt(rs.getBigDecimal("R44_TOT_AMT"));

			// R45
			obj.setR45_item(rs.getString("R45_ITEM"));
			obj.setR45_amt_name_of_sub1(rs.getBigDecimal("R45_AMT_NAME_OF_SUB1"));
			obj.setR45_amt_name_of_sub2(rs.getBigDecimal("R45_AMT_NAME_OF_SUB2"));
			obj.setR45_amt_name_of_sub3(rs.getBigDecimal("R45_AMT_NAME_OF_SUB3"));
			obj.setR45_amt_name_of_sub4(rs.getBigDecimal("R45_AMT_NAME_OF_SUB4"));
			obj.setR45_amt_name_of_sub5(rs.getBigDecimal("R45_AMT_NAME_OF_SUB5"));
			obj.setR45_tot_amt(rs.getBigDecimal("R45_TOT_AMT"));

			// R46
			obj.setR46_item(rs.getString("R46_ITEM"));
			obj.setR46_amt_name_of_sub1(rs.getBigDecimal("R46_AMT_NAME_OF_SUB1"));
			obj.setR46_amt_name_of_sub2(rs.getBigDecimal("R46_AMT_NAME_OF_SUB2"));
			obj.setR46_amt_name_of_sub3(rs.getBigDecimal("R46_AMT_NAME_OF_SUB3"));
			obj.setR46_amt_name_of_sub4(rs.getBigDecimal("R46_AMT_NAME_OF_SUB4"));
			obj.setR46_amt_name_of_sub5(rs.getBigDecimal("R46_AMT_NAME_OF_SUB5"));
			obj.setR46_tot_amt(rs.getBigDecimal("R46_TOT_AMT"));

			// R47
			obj.setR47_item(rs.getString("R47_ITEM"));
			obj.setR47_amt_name_of_sub1(rs.getBigDecimal("R47_AMT_NAME_OF_SUB1"));
			obj.setR47_amt_name_of_sub2(rs.getBigDecimal("R47_AMT_NAME_OF_SUB2"));
			obj.setR47_amt_name_of_sub3(rs.getBigDecimal("R47_AMT_NAME_OF_SUB3"));
			obj.setR47_amt_name_of_sub4(rs.getBigDecimal("R47_AMT_NAME_OF_SUB4"));
			obj.setR47_amt_name_of_sub5(rs.getBigDecimal("R47_AMT_NAME_OF_SUB5"));
			obj.setR47_tot_amt(rs.getBigDecimal("R47_TOT_AMT"));

			// R48
			obj.setR48_item(rs.getString("R48_ITEM"));
			obj.setR48_amt_name_of_sub1(rs.getBigDecimal("R48_AMT_NAME_OF_SUB1"));
			obj.setR48_amt_name_of_sub2(rs.getBigDecimal("R48_AMT_NAME_OF_SUB2"));
			obj.setR48_amt_name_of_sub3(rs.getBigDecimal("R48_AMT_NAME_OF_SUB3"));
			obj.setR48_amt_name_of_sub4(rs.getBigDecimal("R48_AMT_NAME_OF_SUB4"));
			obj.setR48_amt_name_of_sub5(rs.getBigDecimal("R48_AMT_NAME_OF_SUB5"));
			obj.setR48_tot_amt(rs.getBigDecimal("R48_TOT_AMT"));

			// R49
			obj.setR49_item(rs.getString("R49_ITEM"));
			obj.setR49_amt_name_of_sub1(rs.getBigDecimal("R49_AMT_NAME_OF_SUB1"));
			obj.setR49_amt_name_of_sub2(rs.getBigDecimal("R49_AMT_NAME_OF_SUB2"));
			obj.setR49_amt_name_of_sub3(rs.getBigDecimal("R49_AMT_NAME_OF_SUB3"));
			obj.setR49_amt_name_of_sub4(rs.getBigDecimal("R49_AMT_NAME_OF_SUB4"));
			obj.setR49_amt_name_of_sub5(rs.getBigDecimal("R49_AMT_NAME_OF_SUB5"));
			obj.setR49_tot_amt(rs.getBigDecimal("R49_TOT_AMT"));

			// R50
			obj.setR50_item(rs.getString("R50_ITEM"));
			obj.setR50_amt_name_of_sub1(rs.getBigDecimal("R50_AMT_NAME_OF_SUB1"));
			obj.setR50_amt_name_of_sub2(rs.getBigDecimal("R50_AMT_NAME_OF_SUB2"));
			obj.setR50_amt_name_of_sub3(rs.getBigDecimal("R50_AMT_NAME_OF_SUB3"));
			obj.setR50_amt_name_of_sub4(rs.getBigDecimal("R50_AMT_NAME_OF_SUB4"));
			obj.setR50_amt_name_of_sub5(rs.getBigDecimal("R50_AMT_NAME_OF_SUB5"));
			obj.setR50_tot_amt(rs.getBigDecimal("R50_TOT_AMT"));

			// R51
			obj.setR51_item(rs.getString("R51_ITEM"));
			obj.setR51_amt_name_of_sub1(rs.getBigDecimal("R51_AMT_NAME_OF_SUB1"));
			obj.setR51_amt_name_of_sub2(rs.getBigDecimal("R51_AMT_NAME_OF_SUB2"));
			obj.setR51_amt_name_of_sub3(rs.getBigDecimal("R51_AMT_NAME_OF_SUB3"));
			obj.setR51_amt_name_of_sub4(rs.getBigDecimal("R51_AMT_NAME_OF_SUB4"));
			obj.setR51_amt_name_of_sub5(rs.getBigDecimal("R51_AMT_NAME_OF_SUB5"));
			obj.setR51_tot_amt(rs.getBigDecimal("R51_TOT_AMT"));

			// R52
			obj.setR52_item(rs.getString("R52_ITEM"));
			obj.setR52_amt_name_of_sub1(rs.getBigDecimal("R52_AMT_NAME_OF_SUB1"));
			obj.setR52_amt_name_of_sub2(rs.getBigDecimal("R52_AMT_NAME_OF_SUB2"));
			obj.setR52_amt_name_of_sub3(rs.getBigDecimal("R52_AMT_NAME_OF_SUB3"));
			obj.setR52_amt_name_of_sub4(rs.getBigDecimal("R52_AMT_NAME_OF_SUB4"));
			obj.setR52_amt_name_of_sub5(rs.getBigDecimal("R52_AMT_NAME_OF_SUB5"));
			obj.setR52_tot_amt(rs.getBigDecimal("R52_TOT_AMT"));

			// R53
			obj.setR53_item(rs.getString("R53_ITEM"));
			obj.setR53_amt_name_of_sub1(rs.getBigDecimal("R53_AMT_NAME_OF_SUB1"));
			obj.setR53_amt_name_of_sub2(rs.getBigDecimal("R53_AMT_NAME_OF_SUB2"));
			obj.setR53_amt_name_of_sub3(rs.getBigDecimal("R53_AMT_NAME_OF_SUB3"));
			obj.setR53_amt_name_of_sub4(rs.getBigDecimal("R53_AMT_NAME_OF_SUB4"));
			obj.setR53_amt_name_of_sub5(rs.getBigDecimal("R53_AMT_NAME_OF_SUB5"));
			obj.setR53_tot_amt(rs.getBigDecimal("R53_TOT_AMT"));

			// R54
			obj.setR54_item(rs.getString("R54_ITEM"));
			obj.setR54_amt_name_of_sub1(rs.getBigDecimal("R54_AMT_NAME_OF_SUB1"));
			obj.setR54_amt_name_of_sub2(rs.getBigDecimal("R54_AMT_NAME_OF_SUB2"));
			obj.setR54_amt_name_of_sub3(rs.getBigDecimal("R54_AMT_NAME_OF_SUB3"));
			obj.setR54_amt_name_of_sub4(rs.getBigDecimal("R54_AMT_NAME_OF_SUB4"));
			obj.setR54_amt_name_of_sub5(rs.getBigDecimal("R54_AMT_NAME_OF_SUB5"));
			obj.setR54_tot_amt(rs.getBigDecimal("R54_TOT_AMT"));

			// R55
			obj.setR55_item(rs.getString("R55_ITEM"));
			obj.setR55_amt_name_of_sub1(rs.getBigDecimal("R55_AMT_NAME_OF_SUB1"));
			obj.setR55_amt_name_of_sub2(rs.getBigDecimal("R55_AMT_NAME_OF_SUB2"));
			obj.setR55_amt_name_of_sub3(rs.getBigDecimal("R55_AMT_NAME_OF_SUB3"));
			obj.setR55_amt_name_of_sub4(rs.getBigDecimal("R55_AMT_NAME_OF_SUB4"));
			obj.setR55_amt_name_of_sub5(rs.getBigDecimal("R55_AMT_NAME_OF_SUB5"));
			obj.setR55_tot_amt(rs.getBigDecimal("R55_TOT_AMT"));

			// R56
			obj.setR56_item(rs.getString("R56_ITEM"));
			obj.setR56_amt_name_of_sub1(rs.getBigDecimal("R56_AMT_NAME_OF_SUB1"));
			obj.setR56_amt_name_of_sub2(rs.getBigDecimal("R56_AMT_NAME_OF_SUB2"));
			obj.setR56_amt_name_of_sub3(rs.getBigDecimal("R56_AMT_NAME_OF_SUB3"));
			obj.setR56_amt_name_of_sub4(rs.getBigDecimal("R56_AMT_NAME_OF_SUB4"));
			obj.setR56_amt_name_of_sub5(rs.getBigDecimal("R56_AMT_NAME_OF_SUB5"));
			obj.setR56_tot_amt(rs.getBigDecimal("R56_TOT_AMT"));

			// R57
			obj.setR57_item(rs.getString("R57_ITEM"));
			obj.setR57_amt_name_of_sub1(rs.getBigDecimal("R57_AMT_NAME_OF_SUB1"));
			obj.setR57_amt_name_of_sub2(rs.getBigDecimal("R57_AMT_NAME_OF_SUB2"));
			obj.setR57_amt_name_of_sub3(rs.getBigDecimal("R57_AMT_NAME_OF_SUB3"));
			obj.setR57_amt_name_of_sub4(rs.getBigDecimal("R57_AMT_NAME_OF_SUB4"));
			obj.setR57_amt_name_of_sub5(rs.getBigDecimal("R57_AMT_NAME_OF_SUB5"));
			obj.setR57_tot_amt(rs.getBigDecimal("R57_TOT_AMT"));

			// R58
			obj.setR58_item(rs.getString("R58_ITEM"));
			obj.setR58_amt_name_of_sub1(rs.getBigDecimal("R58_AMT_NAME_OF_SUB1"));
			obj.setR58_amt_name_of_sub2(rs.getBigDecimal("R58_AMT_NAME_OF_SUB2"));
			obj.setR58_amt_name_of_sub3(rs.getBigDecimal("R58_AMT_NAME_OF_SUB3"));
			obj.setR58_amt_name_of_sub4(rs.getBigDecimal("R58_AMT_NAME_OF_SUB4"));
			obj.setR58_amt_name_of_sub5(rs.getBigDecimal("R58_AMT_NAME_OF_SUB5"));
			obj.setR58_tot_amt(rs.getBigDecimal("R58_TOT_AMT"));

			// Metadata fields
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE")); // extra field
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));

			return obj;
		}

	}

}
