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
/*import org.apache.poi.ss.usermodel.FillPatternType;*/
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
/*import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;*/
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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

import com.bornfire.brrs.entities.M_I_S_CA_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_I_S_CA_Arcihval_Summary_Entity;
import com.bornfire.brrs.entities.M_I_S_CA_Detail_Entity;
import com.bornfire.brrs.entities.M_I_S_CA_RESUB_Summary_Entity;
import com.bornfire.brrs.entities.M_I_S_CA_Summary_Entity;
//import com.bornfire.brrs.entities.M_SFINP2_Detail_Entity;
import com.bornfire.brrs.entities.UserProfileRep;

@Service
@Transactional

public class BRRS_M_I_S_CA_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_I_S_CA_ReportService.class);

	@Autowired
	AuditService auditService;

	@Autowired
	private Environment env;

	@Autowired
	UserProfileRep userProfileRep;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// =========================================================
	// JDBC QUERY METHODS
	// =========================================================

	public List<M_I_S_CA_Summary_Entity> getSummaryByDate(Date reportDate) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_I_S_CA_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				new Object[] { reportDate }, new M_I_S_CASummaryRowMapper());
	}

	public List<M_I_S_CA_Arcihval_Summary_Entity> getArchivalSummaryByDateAndVersion(Date reportDate,
			BigDecimal version) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_I_S_CA_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
				new Object[] { reportDate, version }, new M_I_S_CAArchivalSummaryRowMapper());
	}

	public List<M_I_S_CA_Arcihval_Summary_Entity> getArchivalSummaryWithVersion() {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_I_S_CA_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC",
				new M_I_S_CAArchivalSummaryRowMapper());
	}

	public List<M_I_S_CA_RESUB_Summary_Entity> getResubSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_I_S_CA_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
				new Object[] { reportDate, version }, new M_I_S_CAResubSummaryRowMapper());
	}

	public List<M_I_S_CA_Detail_Entity> getDetailByDate(Date reportDate) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_I_S_CA_DETAILTABLE WHERE REPORT_DATE = ?",
				new Object[] { reportDate }, new M_I_S_CADetailRowMapper());
	}

	public int getDetailCount(Date reportDate) {
		return jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM BRRS_M_I_S_CA_DETAILTABLE WHERE REPORT_DATE = ?",
				new Object[] { reportDate }, Integer.class);
	}

	public List<M_I_S_CA_Detail_Entity> getDetailByRowIdAndColumnId(String reportLabel, String reportLabel1,
			String reportAddlCriteria_1, String reportAddlCriteria_2, String reportAddlCriteria_3, Date reportDate) {
		String sql = "SELECT * FROM BRRS_M_I_S_CA_DETAILTABLE " +
				"WHERE REPORT_LABEL = ? " +
				"AND REPORT_LABEL_1 = ? " +
				"AND ( ? IS NULL OR ? = '' OR REPORT_ADDL_CRITERIA_1 = ? ) " +
				"AND ( ? IS NULL OR ? = '' OR REPORT_ADDL_CRITERIA_2 = ? ) " +
				"AND ( ? IS NULL OR ? = '' OR REPORT_ADDL_CRITERIA_3 = ? ) " +
				"AND REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportLabel1,
				reportAddlCriteria_1, reportAddlCriteria_1, reportAddlCriteria_1,
				reportAddlCriteria_2, reportAddlCriteria_2, reportAddlCriteria_2,
				reportAddlCriteria_3, reportAddlCriteria_3, reportAddlCriteria_3,
				reportDate }, new M_I_S_CADetailRowMapper());
	}

	public M_I_S_CA_Detail_Entity getDetailByAcctNumber(String acctNumber) {
		List<M_I_S_CA_Detail_Entity> list = jdbcTemplate.query(
				"SELECT * FROM BRRS_M_I_S_CA_DETAILTABLE WHERE ACCT_NUMBER = ?",
				new Object[] { acctNumber }, new M_I_S_CADetailRowMapper());
		return list.isEmpty() ? null : list.get(0);
	}

	public List<M_I_S_CA_Archival_Detail_Entity> getArchivalDetailByDateAndVersion(Date reportDate, String version) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_I_S_CA_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE=? AND DATA_ENTRY_VERSION=?",
				new Object[] { reportDate, version }, new M_I_S_CAArchivalDetailRowMapper());
	}

	public List<M_I_S_CA_Archival_Detail_Entity> getArchivalDetailByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria_1, String reportAddlCriteria_2, String reportAddlCriteria_3,
			String reportLabel_1, Date reportDate) {
		String sql = "SELECT * FROM BRRS_M_I_S_CA_ARCHIVALTABLE_DETAIL " +
				"WHERE REPORT_LABEL = ? " +
				"AND ( ? IS NULL OR ? = '' OR REPORT_ADDL_CRITERIA_1 = ? ) " +
				"AND ( ? IS NULL OR ? = '' OR REPORT_ADDL_CRITERIA_2 = ? ) " +
				"AND ( ? IS NULL OR ? = '' OR REPORT_ADDL_CRITERIA_3 = ? ) " +
				"AND REPORT_LABEL_1 = ? " +
				"AND REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new Object[] { reportLabel,
				reportAddlCriteria_1, reportAddlCriteria_1, reportAddlCriteria_1,
				reportAddlCriteria_2, reportAddlCriteria_2, reportAddlCriteria_2,
				reportAddlCriteria_3, reportAddlCriteria_3, reportAddlCriteria_3,
				reportLabel_1, reportDate }, new M_I_S_CAArchivalDetailRowMapper());
	}

	// =========================================================
	// JDBC WRITE METHODS
	// =========================================================

	private static final String R_FIELDS_SET =
			"R10_SPEC_IMPAIRMENTS=?,R10_RECOVERIES=?,R10_WRITE_OFFS=?,R10_NEW_CHARS=?,R10_TOTAL=?," +
			"R11_SPEC_IMPAIRMENTS=?,R11_RECOVERIES=?,R11_WRITE_OFFS=?,R11_NEW_CHARS=?,R11_TOTAL=?," +
			"R12_SPEC_IMPAIRMENTS=?,R12_RECOVERIES=?,R12_WRITE_OFFS=?,R12_NEW_CHARS=?,R12_TOTAL=?," +
			"R13_SPEC_IMPAIRMENTS=?,R13_RECOVERIES=?,R13_WRITE_OFFS=?,R13_NEW_CHARS=?,R13_TOTAL=?," +
			"R14_SPEC_IMPAIRMENTS=?,R14_RECOVERIES=?,R14_WRITE_OFFS=?,R14_NEW_CHARS=?,R14_TOTAL=?," +
			"R15_SPEC_IMPAIRMENTS=?,R15_RECOVERIES=?,R15_WRITE_OFFS=?,R15_NEW_CHARS=?,R15_TOTAL=?," +
			"R16_SPEC_IMPAIRMENTS=?,R16_RECOVERIES=?,R16_WRITE_OFFS=?,R16_NEW_CHARS=?,R16_TOTAL=?," +
			"R17_SPEC_IMPAIRMENTS=?,R17_RECOVERIES=?,R17_WRITE_OFFS=?,R17_NEW_CHARS=?,R17_TOTAL=?," +
			"R18_SPEC_IMPAIRMENTS=?,R18_RECOVERIES=?,R18_WRITE_OFFS=?,R18_NEW_CHARS=?,R18_TOTAL=?," +
			"R19_SPEC_IMPAIRMENTS=?,R19_RECOVERIES=?,R19_WRITE_OFFS=?,R19_NEW_CHARS=?,R19_TOTAL=?," +
			"R20_SPEC_IMPAIRMENTS=?,R20_RECOVERIES=?,R20_WRITE_OFFS=?,R20_NEW_CHARS=?,R20_TOTAL=?," +
			"R21_SPEC_IMPAIRMENTS=?,R21_RECOVERIES=?,R21_WRITE_OFFS=?,R21_NEW_CHARS=?,R21_TOTAL=?," +
			"R22_SPEC_IMPAIRMENTS=?,R22_RECOVERIES=?,R22_WRITE_OFFS=?,R22_NEW_CHARS=?,R22_TOTAL=?," +
			"R23_SPEC_IMPAIRMENTS=?,R23_RECOVERIES=?,R23_WRITE_OFFS=?,R23_NEW_CHARS=?,R23_TOTAL=?," +
			"R24_SPEC_IMPAIRMENTS=?,R24_RECOVERIES=?,R24_WRITE_OFFS=?,R24_NEW_CHARS=?,R24_TOTAL=?," +
			"R25_SPEC_IMPAIRMENTS=?,R25_RECOVERIES=?,R25_WRITE_OFFS=?,R25_NEW_CHARS=?,R25_TOTAL=?," +
			"R26_SPEC_IMPAIRMENTS=?,R26_RECOVERIES=?,R26_WRITE_OFFS=?,R26_NEW_CHARS=?,R26_TOTAL=?," +
			"R27_SPEC_IMPAIRMENTS=?,R27_RECOVERIES=?,R27_WRITE_OFFS=?,R27_NEW_CHARS=?,R27_TOTAL=?," +
			"R28_SPEC_IMPAIRMENTS=?,R28_RECOVERIES=?,R28_WRITE_OFFS=?,R28_NEW_CHARS=?,R28_TOTAL=?," +
			"R29_SPEC_IMPAIRMENTS=?,R29_RECOVERIES=?,R29_WRITE_OFFS=?,R29_NEW_CHARS=?,R29_TOTAL=?," +
			"R30_SPEC_IMPAIRMENTS=?,R30_RECOVERIES=?,R30_WRITE_OFFS=?,R30_NEW_CHARS=?,R30_TOTAL=?," +
			"R31_SPEC_IMPAIRMENTS=?,R31_RECOVERIES=?,R31_WRITE_OFFS=?,R31_NEW_CHARS=?,R31_TOTAL=?," +
			"R32_SPEC_IMPAIRMENTS=?,R32_RECOVERIES=?,R32_WRITE_OFFS=?,R32_NEW_CHARS=?,R32_TOTAL=?," +
			"R33_SPEC_IMPAIRMENTS=?,R33_RECOVERIES=?,R33_WRITE_OFFS=?,R33_NEW_CHARS=?,R33_TOTAL=?," +
			"R34_SPEC_IMPAIRMENTS=?,R34_RECOVERIES=?,R34_WRITE_OFFS=?,R34_NEW_CHARS=?,R34_TOTAL=?," +
			"R35_SPEC_IMPAIRMENTS=?,R35_RECOVERIES=?,R35_WRITE_OFFS=?,R35_NEW_CHARS=?,R35_TOTAL=?," +
			"R36_SPEC_IMPAIRMENTS=?,R36_RECOVERIES=?,R36_WRITE_OFFS=?,R36_NEW_CHARS=?,R36_TOTAL=?," +
			"R37_SPEC_IMPAIRMENTS=?,R37_RECOVERIES=?,R37_WRITE_OFFS=?,R37_NEW_CHARS=?,R37_TOTAL=?," +
			"R38_SPEC_IMPAIRMENTS=?,R38_RECOVERIES=?,R38_WRITE_OFFS=?,R38_NEW_CHARS=?,R38_TOTAL=?," +
			"R39_SPEC_IMPAIRMENTS=?,R39_RECOVERIES=?,R39_WRITE_OFFS=?,R39_NEW_CHARS=?,R39_TOTAL=?," +
			"R40_SPEC_IMPAIRMENTS=?,R40_RECOVERIES=?,R40_WRITE_OFFS=?,R40_NEW_CHARS=?,R40_TOTAL=?," +
			"R41_SPEC_IMPAIRMENTS=?,R41_RECOVERIES=?,R41_WRITE_OFFS=?,R41_NEW_CHARS=?,R41_TOTAL=?," +
			"R42_SPEC_IMPAIRMENTS=?,R42_RECOVERIES=?,R42_WRITE_OFFS=?,R42_NEW_CHARS=?,R42_TOTAL=?," +
			"R43_SPEC_IMPAIRMENTS=?,R43_RECOVERIES=?,R43_WRITE_OFFS=?,R43_NEW_CHARS=?,R43_TOTAL=?," +
			"R44_SPEC_IMPAIRMENTS=?,R44_RECOVERIES=?,R44_WRITE_OFFS=?,R44_NEW_CHARS=?,R44_TOTAL=?," +
			"R45_SPEC_IMPAIRMENTS=?,R45_RECOVERIES=?,R45_WRITE_OFFS=?,R45_NEW_CHARS=?,R45_TOTAL=?," +
			"R46_SPEC_IMPAIRMENTS=?,R46_RECOVERIES=?,R46_WRITE_OFFS=?,R46_NEW_CHARS=?,R46_TOTAL=?," +
			"R47_SPEC_IMPAIRMENTS=?,R47_RECOVERIES=?,R47_WRITE_OFFS=?,R47_NEW_CHARS=?,R47_TOTAL=?," +
			"R48_SPEC_IMPAIRMENTS=?,R48_RECOVERIES=?,R48_WRITE_OFFS=?,R48_NEW_CHARS=?,R48_TOTAL=?," +
			"R49_SPEC_IMPAIRMENTS=?,R49_RECOVERIES=?,R49_WRITE_OFFS=?,R49_NEW_CHARS=?,R49_TOTAL=?," +
			"R50_SPEC_IMPAIRMENTS=?,R50_RECOVERIES=?,R50_WRITE_OFFS=?,R50_NEW_CHARS=?,R50_TOTAL=?," +
			"R51_SPEC_IMPAIRMENTS=?,R51_RECOVERIES=?,R51_WRITE_OFFS=?,R51_NEW_CHARS=?,R51_TOTAL=?," +
			"R52_SPEC_IMPAIRMENTS=?,R52_RECOVERIES=?,R52_WRITE_OFFS=?,R52_NEW_CHARS=?,R52_TOTAL=?," +
			"R53_SPEC_IMPAIRMENTS=?,R53_RECOVERIES=?,R53_WRITE_OFFS=?,R53_NEW_CHARS=?,R53_TOTAL=?," +
			"R54_SPEC_IMPAIRMENTS=?,R54_RECOVERIES=?,R54_WRITE_OFFS=?,R54_NEW_CHARS=?,R54_TOTAL=?," +
			"R55_SPEC_IMPAIRMENTS=?,R55_RECOVERIES=?,R55_WRITE_OFFS=?,R55_NEW_CHARS=?,R55_TOTAL=?," +
			"R56_SPEC_IMPAIRMENTS=?,R56_RECOVERIES=?,R56_WRITE_OFFS=?,R56_NEW_CHARS=?,R56_TOTAL=?," +
			"R57_SPEC_IMPAIRMENTS=?,R57_RECOVERIES=?,R57_WRITE_OFFS=?,R57_NEW_CHARS=?,R57_TOTAL=?," +
			"R58_SPEC_IMPAIRMENTS=?,R58_RECOVERIES=?,R58_WRITE_OFFS=?,R58_NEW_CHARS=?,R58_TOTAL=?," +
			"R59_SPEC_IMPAIRMENTS=?,R59_RECOVERIES=?,R59_WRITE_OFFS=?,R59_NEW_CHARS=?,R59_TOTAL=?," +
			"R60_SPEC_IMPAIRMENTS=?,R60_RECOVERIES=?,R60_WRITE_OFFS=?,R60_NEW_CHARS=?,R60_TOTAL=?," +
			"R61_SPEC_IMPAIRMENTS=?,R61_RECOVERIES=?,R61_WRITE_OFFS=?,R61_NEW_CHARS=?,R61_TOTAL=?," +
			"R62_SPEC_IMPAIRMENTS=?,R62_RECOVERIES=?,R62_WRITE_OFFS=?,R62_NEW_CHARS=?,R62_TOTAL=?," +
			"R63_SPEC_IMPAIRMENTS=?,R63_RECOVERIES=?,R63_WRITE_OFFS=?,R63_NEW_CHARS=?,R63_TOTAL=?," +
			"R64_SPEC_IMPAIRMENTS=?,R64_RECOVERIES=?,R64_WRITE_OFFS=?,R64_NEW_CHARS=?,R64_TOTAL=?," +
			"R65_SPEC_IMPAIRMENTS=?,R65_RECOVERIES=?,R65_WRITE_OFFS=?,R65_NEW_CHARS=?,R65_TOTAL=?," +
			"R66_SPEC_IMPAIRMENTS=?,R66_RECOVERIES=?,R66_WRITE_OFFS=?,R66_NEW_CHARS=?,R66_TOTAL=?," +
			"R67_SPEC_IMPAIRMENTS=?,R67_RECOVERIES=?,R67_WRITE_OFFS=?,R67_NEW_CHARS=?,R67_TOTAL=?," +
			"R68_SPEC_IMPAIRMENTS=?,R68_RECOVERIES=?,R68_WRITE_OFFS=?,R68_NEW_CHARS=?,R68_TOTAL=?," +
			"R69_SPEC_IMPAIRMENTS=?,R69_RECOVERIES=?,R69_WRITE_OFFS=?,R69_NEW_CHARS=?,R69_TOTAL=?," +
			"R70_SPEC_IMPAIRMENTS=?,R70_RECOVERIES=?,R70_WRITE_OFFS=?,R70_NEW_CHARS=?,R70_TOTAL=?";

	private Object[] rFieldValues(M_I_S_CA_Summary_Entity e) {
		return new Object[] {
			e.getR10_spec_impairments(), e.getR10_recoveries(), e.getR10_write_offs(), e.getR10_new_chars(), e.getR10_total(),
			e.getR11_spec_impairments(), e.getR11_recoveries(), e.getR11_write_offs(), e.getR11_new_chars(), e.getR11_total(),
			e.getR12_spec_impairments(), e.getR12_recoveries(), e.getR12_write_offs(), e.getR12_new_chars(), e.getR12_total(),
			e.getR13_spec_impairments(), e.getR13_recoveries(), e.getR13_write_offs(), e.getR13_new_chars(), e.getR13_total(),
			e.getR14_spec_impairments(), e.getR14_recoveries(), e.getR14_write_offs(), e.getR14_new_chars(), e.getR14_total(),
			e.getR15_spec_impairments(), e.getR15_recoveries(), e.getR15_write_offs(), e.getR15_new_chars(), e.getR15_total(),
			e.getR16_spec_impairments(), e.getR16_recoveries(), e.getR16_write_offs(), e.getR16_new_chars(), e.getR16_total(),
			e.getR17_spec_impairments(), e.getR17_recoveries(), e.getR17_write_offs(), e.getR17_new_chars(), e.getR17_total(),
			e.getR18_spec_impairments(), e.getR18_recoveries(), e.getR18_write_offs(), e.getR18_new_chars(), e.getR18_total(),
			e.getR19_spec_impairments(), e.getR19_recoveries(), e.getR19_write_offs(), e.getR19_new_chars(), e.getR19_total(),
			e.getR20_spec_impairments(), e.getR20_recoveries(), e.getR20_write_offs(), e.getR20_new_chars(), e.getR20_total(),
			e.getR21_spec_impairments(), e.getR21_recoveries(), e.getR21_write_offs(), e.getR21_new_chars(), e.getR21_total(),
			e.getR22_spec_impairments(), e.getR22_recoveries(), e.getR22_write_offs(), e.getR22_new_chars(), e.getR22_total(),
			e.getR23_spec_impairments(), e.getR23_recoveries(), e.getR23_write_offs(), e.getR23_new_chars(), e.getR23_total(),
			e.getR24_spec_impairments(), e.getR24_recoveries(), e.getR24_write_offs(), e.getR24_new_chars(), e.getR24_total(),
			e.getR25_spec_impairments(), e.getR25_recoveries(), e.getR25_write_offs(), e.getR25_new_chars(), e.getR25_total(),
			e.getR26_spec_impairments(), e.getR26_recoveries(), e.getR26_write_offs(), e.getR26_new_chars(), e.getR26_total(),
			e.getR27_spec_impairments(), e.getR27_recoveries(), e.getR27_write_offs(), e.getR27_new_chars(), e.getR27_total(),
			e.getR28_spec_impairments(), e.getR28_recoveries(), e.getR28_write_offs(), e.getR28_new_chars(), e.getR28_total(),
			e.getR29_spec_impairments(), e.getR29_recoveries(), e.getR29_write_offs(), e.getR29_new_chars(), e.getR29_total(),
			e.getR30_spec_impairments(), e.getR30_recoveries(), e.getR30_write_offs(), e.getR30_new_chars(), e.getR30_total(),
			e.getR31_spec_impairments(), e.getR31_recoveries(), e.getR31_write_offs(), e.getR31_new_chars(), e.getR31_total(),
			e.getR32_spec_impairments(), e.getR32_recoveries(), e.getR32_write_offs(), e.getR32_new_chars(), e.getR32_total(),
			e.getR33_spec_impairments(), e.getR33_recoveries(), e.getR33_write_offs(), e.getR33_new_chars(), e.getR33_total(),
			e.getR34_spec_impairments(), e.getR34_recoveries(), e.getR34_write_offs(), e.getR34_new_chars(), e.getR34_total(),
			e.getR35_spec_impairments(), e.getR35_recoveries(), e.getR35_write_offs(), e.getR35_new_chars(), e.getR35_total(),
			e.getR36_spec_impairments(), e.getR36_recoveries(), e.getR36_write_offs(), e.getR36_new_chars(), e.getR36_total(),
			e.getR37_spec_impairments(), e.getR37_recoveries(), e.getR37_write_offs(), e.getR37_new_chars(), e.getR37_total(),
			e.getR38_spec_impairments(), e.getR38_recoveries(), e.getR38_write_offs(), e.getR38_new_chars(), e.getR38_total(),
			e.getR39_spec_impairments(), e.getR39_recoveries(), e.getR39_write_offs(), e.getR39_new_chars(), e.getR39_total(),
			e.getR40_spec_impairments(), e.getR40_recoveries(), e.getR40_write_offs(), e.getR40_new_chars(), e.getR40_total(),
			e.getR41_spec_impairments(), e.getR41_recoveries(), e.getR41_write_offs(), e.getR41_new_chars(), e.getR41_total(),
			e.getR42_spec_impairments(), e.getR42_recoveries(), e.getR42_write_offs(), e.getR42_new_chars(), e.getR42_total(),
			e.getR43_spec_impairments(), e.getR43_recoveries(), e.getR43_write_offs(), e.getR43_new_chars(), e.getR43_total(),
			e.getR44_spec_impairments(), e.getR44_recoveries(), e.getR44_write_offs(), e.getR44_new_chars(), e.getR44_total(),
			e.getR45_spec_impairments(), e.getR45_recoveries(), e.getR45_write_offs(), e.getR45_new_chars(), e.getR45_total(),
			e.getR46_spec_impairments(), e.getR46_recoveries(), e.getR46_write_offs(), e.getR46_new_chars(), e.getR46_total(),
			e.getR47_spec_impairments(), e.getR47_recoveries(), e.getR47_write_offs(), e.getR47_new_chars(), e.getR47_total(),
			e.getR48_spec_impairments(), e.getR48_recoveries(), e.getR48_write_offs(), e.getR48_new_chars(), e.getR48_total(),
			e.getR49_spec_impairments(), e.getR49_recoveries(), e.getR49_write_offs(), e.getR49_new_chars(), e.getR49_total(),
			e.getR50_spec_impairments(), e.getR50_recoveries(), e.getR50_write_offs(), e.getR50_new_chars(), e.getR50_total(),
			e.getR51_spec_impairments(), e.getR51_recoveries(), e.getR51_write_offs(), e.getR51_new_chars(), e.getR51_total(),
			e.getR52_spec_impairments(), e.getR52_recoveries(), e.getR52_write_offs(), e.getR52_new_chars(), e.getR52_total(),
			e.getR53_spec_impairments(), e.getR53_recoveries(), e.getR53_write_offs(), e.getR53_new_chars(), e.getR53_total(),
			e.getR54_spec_impairments(), e.getR54_recoveries(), e.getR54_write_offs(), e.getR54_new_chars(), e.getR54_total(),
			e.getR55_spec_impairments(), e.getR55_recoveries(), e.getR55_write_offs(), e.getR55_new_chars(), e.getR55_total(),
			e.getR56_spec_impairments(), e.getR56_recoveries(), e.getR56_write_offs(), e.getR56_new_chars(), e.getR56_total(),
			e.getR57_spec_impairments(), e.getR57_recoveries(), e.getR57_write_offs(), e.getR57_new_chars(), e.getR57_total(),
			e.getR58_spec_impairments(), e.getR58_recoveries(), e.getR58_write_offs(), e.getR58_new_chars(), e.getR58_total(),
			e.getR59_spec_impairments(), e.getR59_recoveries(), e.getR59_write_offs(), e.getR59_new_chars(), e.getR59_total(),
			e.getR60_spec_impairments(), e.getR60_recoveries(), e.getR60_write_offs(), e.getR60_new_chars(), e.getR60_total(),
			e.getR61_spec_impairments(), e.getR61_recoveries(), e.getR61_write_offs(), e.getR61_new_chars(), e.getR61_total(),
			e.getR62_spec_impairments(), e.getR62_recoveries(), e.getR62_write_offs(), e.getR62_new_chars(), e.getR62_total(),
			e.getR63_spec_impairments(), e.getR63_recoveries(), e.getR63_write_offs(), e.getR63_new_chars(), e.getR63_total(),
			e.getR64_spec_impairments(), e.getR64_recoveries(), e.getR64_write_offs(), e.getR64_new_chars(), e.getR64_total(),
			e.getR65_spec_impairments(), e.getR65_recoveries(), e.getR65_write_offs(), e.getR65_new_chars(), e.getR65_total(),
			e.getR66_spec_impairments(), e.getR66_recoveries(), e.getR66_write_offs(), e.getR66_new_chars(), e.getR66_total(),
			e.getR67_spec_impairments(), e.getR67_recoveries(), e.getR67_write_offs(), e.getR67_new_chars(), e.getR67_total(),
			e.getR68_spec_impairments(), e.getR68_recoveries(), e.getR68_write_offs(), e.getR68_new_chars(), e.getR68_total(),
			e.getR69_spec_impairments(), e.getR69_recoveries(), e.getR69_write_offs(), e.getR69_new_chars(), e.getR69_total(),
			e.getR70_spec_impairments(), e.getR70_recoveries(), e.getR70_write_offs(), e.getR70_new_chars(), e.getR70_total(),
		};
	}

	private void updateSummary(M_I_S_CA_Summary_Entity e) {
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
		jdbcTemplate.update(
				"UPDATE BRRS_M_I_S_CA_SUMMARYTABLE SET " + R_FIELDS_SET +
				",REPORT_VERSION=?,REPORT_FREQUENCY=?,REPORT_CODE=?,REPORT_DESC=?,ENTITY_FLG=?,MODIFY_FLG=?,DEL_FLG=?" +
				" WHERE REPORT_DATE=?", params);
	}

	private void updateDetail(M_I_S_CA_Detail_Entity e) {
		Object[] params = new Object[] {
				e.getCustId(), e.getAcctName(), e.getDataType(), e.getReportName(), e.getReportLable(),
				e.getReportAddlCriteria_1(), e.getReportAddlCriteria_2(), e.getReportAddlCriteria_3(),
				e.getReportLabel1(), e.getReportRemarks(), e.getModificationRemarks(), e.getDataEntryVersion(),
				e.getAcctBalanceInpula(), e.getCreateUser(), e.getCreateTime(), e.getModifyUser(), e.getModifyTime(),
				e.getVerifyUser(), e.getVerifyTime(), e.getEntityFlg(), e.getModifyFlg(), e.getDelFlg(),
				e.getAcctNumber() };
		jdbcTemplate.update(
				"UPDATE BRRS_M_I_S_CA_DETAILTABLE SET CUST_ID=?, ACCT_NAME=?, DATA_TYPE=?, REPORT_NAME=?, REPORT_LABEL=?, " +
				"REPORT_ADDL_CRITERIA_1=?, REPORT_ADDL_CRITERIA_2=?, REPORT_ADDL_CRITERIA_3=?, REPORT_LABEL_1=?, " +
				"REPORT_REMARKS=?, MODIFICATION_REMARKS=?, DATA_ENTRY_VERSION=?, ACCT_BALANCE_IN_PULA=?, CREATE_USER=?, " +
				"CREATE_TIME=?, MODIFY_USER=?, MODIFY_TIME=?, VERIFY_USER=?, VERIFY_TIME=?, ENTITY_FLG=?, MODIFY_FLG=?, DEL_FLG=?" +
				" WHERE ACCT_NUMBER=?", params);
	}

	
	public ModelAndView getM_I_S_CAView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version,HttpServletRequest req1,Model md) {

		ModelAndView mv = new ModelAndView();
		
		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		System.out.println("testing");
		System.out.println(version);

		if ("ARCHIVAL".equals(type) && version != null ) {

		    System.out.println("ARCHIVAL MODE");
		    System.out.println("version = " + version);

		    List<M_I_S_CA_Arcihval_Summary_Entity> T1Master = new ArrayList<>();
			

		    try {
		        Date dt = dateformat.parse(todate);

		        T1Master = getArchivalSummaryByDateAndVersion(dt, version);
		        

		        System.out.println("T1Master size = " + T1Master.size());
		        

		    } catch (ParseException e) {
		        e.printStackTrace();
		    }

		    mv.addObject("reportsummary", T1Master);
		  
		} else {

			List<M_I_S_CA_Summary_Entity> T1Master = new ArrayList<M_I_S_CA_Summary_Entity>();
			
			try {
				Date d1 = dateformat.parse(todate);
				
				T1Master = getSummaryByDate(dateformat.parse(todate));

				System.out.println("T1Master size " + T1Master.size());
				mv.addObject("report_date", dateformat.format(d1));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		
		}

	

		mv.setViewName("BRRS/M_I_S_CA");

		mv.addObject("displaymode", "summary");

		System.out.println("scv" + mv.getViewName());

		return mv;

	}
	 
	
	
	public ModelAndView getM_I_S_CAcurrentDtl(String reportId, String fromdate, String todate, String currency,
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


		//Session hs = sessionFactory.getCurrentSession();

		try {
			Date parsedDate = null;

			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

			String reportLable = null;
			String reportLable_1 = null;
			String reportAddlCriteria_1 = null;
			String reportAddlCriteria_2 = null;
			String reportAddlCriteria_3 = null;
			
			
			
			// ✅ Split filter string into rowId & columnId
			if (filter != null && !filter.isEmpty()) {
				String[] parts = filter.split(",", -1);
				
					reportLable = parts.length > 0 ? parts[0] : null;
					
					reportAddlCriteria_1 = parts.length > 1 ? parts[1] : null;
					reportAddlCriteria_2 = parts.length > 2 ? parts[2] : null;
					reportAddlCriteria_3 = parts.length > 3 ? parts[3] : null;
					reportLable_1 = parts.length > 4 ? parts[4] : null;
				
			}

			System.out.println(type);
			if ("ARCHIVAL".equals(type) && version != null) {
				System.out.println(type);
				// 🔹 Archival branch
				List<M_I_S_CA_Archival_Detail_Entity> T1Dt1;
				if (reportLable != null || reportLable_1 != null || reportAddlCriteria_1 != null || reportAddlCriteria_2 != null || reportAddlCriteria_3 != null ) {
					T1Dt1 = getArchivalDetailByRowIdAndColumnId(reportLable, reportAddlCriteria_1,reportAddlCriteria_2,reportAddlCriteria_3, reportLable_1,parsedDate);
				} else {
					T1Dt1 = getArchivalDetailByDateAndVersion(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<M_I_S_CA_Detail_Entity> T1Dt1;

				if (reportLable != null || reportLable_1 != null || reportAddlCriteria_1 != null || reportAddlCriteria_2 != null || reportAddlCriteria_3 != null ) {
					T1Dt1 = getDetailByRowIdAndColumnId(reportLable, reportAddlCriteria_1,reportAddlCriteria_2,reportAddlCriteria_3,reportLable_1, parsedDate);
				} else {
					T1Dt1 = getDetailByDate(parsedDate);
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

	

	
		mv.setViewName("BRRS/M_I_S_CA");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}
	
	
	
	

	//Archival View
public List<Object[]> getM_I_S_CAArchival() {
	List<Object[]> archivalList = new ArrayList<>();

	try {
		List<M_I_S_CA_Arcihval_Summary_Entity> repoData = getArchivalSummaryWithVersion();

		if (repoData != null && !repoData.isEmpty()) {
			for (M_I_S_CA_Arcihval_Summary_Entity entity : repoData) {
				Object[] row = new Object[] {
						entity.getReport_date(), 
						entity.getReport_version(), 
						 entity.getReportResubDate()
				};
				archivalList.add(row);
			}

			System.out.println("Fetched " + archivalList.size() + " archival records");
			M_I_S_CA_Arcihval_Summary_Entity first = repoData.get(0);
			System.out.println("Latest archival version: " + first.getReport_version());
		} else {
			System.out.println("No archival data found.");
		}

	} catch (Exception e) {
		System.err.println("Error fetching  M_I_S_CA  Archival data: " + e.getMessage());
		e.printStackTrace();
	}

	return archivalList;
}


public List<Object[]> getM_I_S_CAResub() {
    List<Object[]> resubList = new ArrayList<>();
    try {
        List<M_I_S_CA_Arcihval_Summary_Entity> latestArchivalList = getArchivalSummaryWithVersion();

        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
            for (M_I_S_CA_Arcihval_Summary_Entity entity : latestArchivalList) {
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
        System.err.println("Error fetching M_I_S_CA Resub data: " + e.getMessage());
        e.printStackTrace();
    }
    return resubList;
}

	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	public byte[] getM_I_S_CADetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for M_I_S_CA Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getM_I_S_CADetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_I_S_CADetails");

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
			String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT ADDL CRITERIA2","REPORT_DATE" };

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
			List<M_I_S_CA_Detail_Entity> reportData = getDetailByDate(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_I_S_CA_Detail_Entity item : reportData) {
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

					

					row.createCell(4).setCellValue(item.getReportLable());
					row.createCell(5).setCellValue(item.getReportAddlCriteria_1());
					row.createCell(6).setCellValue(item.getReportAddlCriteria_2());
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
				logger.info("No data found for M_I_S_CA — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_I_S_CA Excel", e);
			return new byte[0];
		}
	}
	
	
	
	
	
	public byte[] getM_I_S_CADetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for M_I_S_CA ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_I_S_CADetail");

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
			String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE",  "REPORT LABLE", "REPORT ADDL CRITERIA1","REPORT ADDL CRITERIA2", "REPORT_DATE" };

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
			List<M_I_S_CA_Archival_Detail_Entity> reportData = getArchivalDetailByDateAndVersion(parsedToDate,
					version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_I_S_CA_Archival_Detail_Entity item : reportData) {
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

					
					row.createCell(4).setCellValue(item.getReportLable());
					row.createCell(5).setCellValue(item.getReportAddlCriteria_1());
					row.createCell(6).setCellValue(item.getReportAddlCriteria_2());
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
				logger.info("No data found for M_I_S_CA — only header will be written.");
			}

// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_I_S_CAExcel", e);
			return new byte[0];
		}
	}
	
	
	
//	public void updateReport(M_I_S_CA_Summary_Entity updatedEntity) {
//
//	    System.out.println("Came to services");
//	    System.out.println("Report Date: " + updatedEntity.getReport_date());
//
//	    M_I_S_CA_Summary_Entity existing =
//	            brrs_m_i_s_ca_summary_repo.findById(updatedEntity.getReport_date())
//	            .orElseThrow(() ->
//	                    new RuntimeException("Record not found for REPORT_DATE: "
//	                            + updatedEntity.getReport_date()));
//
//	    try {
//
//	        // -----------------------------
//	        // 1️⃣ WRITE_OFFS (R11 – R64)
//	        // -----------------------------
//	        for (int i = 11; i <= 64; i++) {
//
//	            String field = "write_offs";
//
//	            String getterName = "getR" + i + "_" + field;
//	            String setterName = "setR" + i + "_" + field;
//
//	            try {
//	                Method getter = M_I_S_CA_Summary_Entity.class.getMethod(getterName);
//
//	                Method setter = M_I_S_CA_Summary_Entity.class.getMethod(
//	                        setterName,
//	                        getter.getReturnType()
//	                );
//
//	                Object newValue = getter.invoke(updatedEntity);
//	                setter.invoke(existing, newValue);
//
//	            } catch (NoSuchMethodException e) {
//	                continue;
//	            }
//	        }
//
////	        // -----------------------------
////	        // 2️⃣ TOTAL (R11 – R64)
////	        // -----------------------------
////	        for (int i = 11; i <= 64; i++) {
////
////	            String field = "total";
////
////	            String getterName = "getR" + i + "_" + field;
////	            String setterName = "setR" + i + "_" + field;
////
////	            try {
////	                Method getter = M_I_S_CA_Summary_Entity.class.getMethod(getterName);
////
////	                Method setter = M_I_S_CA_Summary_Entity.class.getMethod(
////	                        setterName,
////	                        getter.getReturnType()
////	                );
////
////	                Object newValue = getter.invoke(updatedEntity);
////	                setter.invoke(existing, newValue);
////
////	            } catch (NoSuchMethodException e) {
////	                continue;
////	            }
////	        } {65, 67, 68, 69, 70};
//
//	        // -----------------------------
//	        // 3️⃣ EXTRA TOTAL FIELDS
//	        // -----------------------------
//	        int[] extraTotals = { 67, 68 };
//
//	        for (int i : extraTotals) {
//
//	            String field = "total";
//
//	            String getterName = "getR" + i + "_" + field;
//	            String setterName = "setR" + i + "_" + field;
//
//	            try {
//	                Method getter = M_I_S_CA_Summary_Entity.class.getMethod(getterName);
//
//	                Method setter = M_I_S_CA_Summary_Entity.class.getMethod(
//	                        setterName,
//	                        getter.getReturnType()
//	                );
//
//	                Object newValue = getter.invoke(updatedEntity);
//	                setter.invoke(existing, newValue);
//
//	            } catch (NoSuchMethodException e) {
//	                continue;
//	            }
//	        }
//
//	    } catch (Exception e) {
//	        throw new RuntimeException("Error while updating report fields", e);
//	    }
//
//	    // Save
//	    brrs_m_i_s_ca_summary_repo.save(existing);
//	}
	
	@Transactional
	public void updateReport(M_I_S_CA_Summary_Entity updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    List<M_I_S_CA_Summary_Entity> existingList = getSummaryByDate(updatedEntity.getReport_date());
	    if (existingList.isEmpty()) {
	        throw new RuntimeException("Record not found for REPORT_DATE: "
	                + updatedEntity.getReport_date());
	    }
	    M_I_S_CA_Summary_Entity existing = existingList.get(0);

	    // =========================================
	    // AUDIT OLD COPY
	    // =========================================

	    M_I_S_CA_Summary_Entity oldcopy =
	            new M_I_S_CA_Summary_Entity();

	    BeanUtils.copyProperties(existing, oldcopy);

	    try {

	        // -----------------------------
	        // 1️⃣ WRITE_OFFS (R11 – R64)
	        // -----------------------------
	        for (int i = 11; i <= 64; i++) {

	            String field = "write_offs";

	            String getterName = "getR" + i + "_" + field;
	            String setterName = "setR" + i + "_" + field;

	            try {

	                Method getter =
	                        M_I_S_CA_Summary_Entity.class.getMethod(getterName);

	                Method setter =
	                        M_I_S_CA_Summary_Entity.class.getMethod(
	                                setterName,
	                                getter.getReturnType()
	                        );

	                Object newValue = getter.invoke(updatedEntity);

	                setter.invoke(existing, newValue);

	            } catch (NoSuchMethodException e) {
	                continue;
	            }
	        }

	        // -----------------------------
	        // 2️⃣ EXTRA TOTAL FIELDS
	        // -----------------------------
	        int[] extraTotals = {67, 68};

	        for (int i : extraTotals) {

	            String field = "total";

	            String getterName = "getR" + i + "_" + field;
	            String setterName = "setR" + i + "_" + field;

	            try {

	                Method getter =
	                        M_I_S_CA_Summary_Entity.class.getMethod(getterName);

	                Method setter =
	                        M_I_S_CA_Summary_Entity.class.getMethod(
	                                setterName,
	                                getter.getReturnType()
	                        );

	                Object newValue = getter.invoke(updatedEntity);

	                setter.invoke(existing, newValue);

	            } catch (NoSuchMethodException e) {
	                continue;
	            }
	        }

	    } catch (Exception e) {

	        throw new RuntimeException(
	                "Error while updating report fields", e);
	    }

	    // =========================================
	    // CHECK CHANGES
	    // =========================================

	    String changes =
	            auditService.getChanges(
	                    oldcopy,
	                    existing);

	    // =========================================
	    // SAVE ENTITY
	    // =========================================

	    updateSummary(existing);

	    // =========================================
	    // AUDIT ONLY IF CHANGES FOUND
	    // =========================================

	    if (!changes.isEmpty()) {

	        auditService.compareEntitiesmanual(
	                oldcopy,
	                existing,
	                updatedEntity.getReport_date().toString(),
	                "M I S CA Summary Screen",
	                "BRRS_M_I_S_CA_SUMMARY"
	        );
	    }
	}

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_I_S_CA");

		if (acctNo != null) {
			M_I_S_CA_Detail_Entity miscaEntity = getDetailByAcctNumber(acctNo);
			if (miscaEntity != null && miscaEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(miscaEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("miscaData", miscaEntity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String acctBalanceInpula = request.getParameter("acctBalanceInpula");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			M_I_S_CA_Detail_Entity existing = getDetailByAcctNumber(acctNo);
			if (existing == null) {
				logger.warn("No record found for ACCT_NO: {}", acctNo);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}
			
			 // Create old copy for audit comparison
			M_I_S_CA_Detail_Entity oldcopy = new M_I_S_CA_Detail_Entity();
	        BeanUtils.copyProperties(existing, oldcopy);


			boolean isChanged = false;

			if (acctName != null && !acctName.isEmpty()) {
				if (existing.getAcctName() == null || !existing.getAcctName().equals(acctName)) {
					existing.setAcctName(acctName);
					isChanged = true;
					logger.info("Account name updated to {}", acctName);
				}
			}

			 if (acctBalanceInpula != null && !acctBalanceInpula.isEmpty()) {
		            BigDecimal newacctBalanceInpula = new BigDecimal(acctBalanceInpula);
		            if (existing.getAcctBalanceInpula()  == null ||
		                existing.getAcctBalanceInpula().compareTo(newacctBalanceInpula) != 0) {
		            	 existing.setAcctBalanceInpula(newacctBalanceInpula);
		                isChanged = true;
		                logger.info("Balance updated to {}", newacctBalanceInpula);
		            }
		        }
		        
			if (isChanged) {
				updateDetail(existing);
				
				   // Audit comparison
	            auditService.compareEntitiesmanual(
	                    oldcopy,
	                    existing,
	                    acctNo,
	                    "M_I_S_CA Detail Screen",
	                    "BRRS_M_I_S_CA_DETAIL"
	            );
				
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_M_I_S_CA_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_M_I_S_CA_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating M_I_S_CA record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	
	
	

//Normal format Excel

public byte[] getM_I_S_CAExcel(String filename, String reportId, String fromdate, String todate, String currency,
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
return getExcelM_I_S_CAARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);
} catch (ParseException e) {
logger.error("Invalid report date format: {}", fromdate, e);
throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
}
} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
logger.info("Service: Generating RESUB report for version {}", version);

try {
// ✅ Redirecting to Resub Excel
return BRRS_M_I_S_CAResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);

} catch (ParseException e) {
logger.error("Invalid report date format: {}", fromdate, e);
throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
}
} else {

if ("email".equalsIgnoreCase(format) && version == null) {
logger.info("Got format as Email");
logger.info("Service: Generating Email report for version {}", version);
return BRRS_M_I_S_CAEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
} else {

// Fetch data

List<M_I_S_CA_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

if (dataList.isEmpty()) {
logger.warn("Service: No data found for BRRS_M_I_S_CA report. Returning empty result.");
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
M_I_S_CA_Summary_Entity record = dataList.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}

//row7
// Column B
Cell cellBdate = row.createCell(1);
if (record.getReport_date() != null) {
	cellBdate.setCellValue(record.getReport_date());
	cellBdate.setCellStyle(dateStyle);
} else {
	cellBdate.setCellValue("");
	cellBdate.setCellStyle(textStyle);
}



//ROW 10
row = sheet.getRow(9);


// row10
// Column E
Cell cellE = row.createCell(4);
if (record.getR10_total() != null) {
cellE.setCellValue(record.getR10_total().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}

// row12
// Column B

row = sheet.getRow(11);

Cell cellB = row.createCell(1);
if (record.getR12_recoveries() != null) {
cellB.setCellValue(record.getR12_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row12
// Column C
Cell cellC = row.createCell(2);
if (record.getR12_write_offs() != null) {
cellC.setCellValue(record.getR12_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row12
// Column D
Cell cellD = row.createCell(3);
if (record.getR12_new_chars() != null) {
cellD.setCellValue(record.getR12_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




// row13
// Column B

row = sheet.getRow(12);

cellB = row.createCell(1);
if (record.getR13_recoveries() != null) {
cellB.setCellValue(record.getR13_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row13
// Column C
cellC = row.createCell(2);
if (record.getR13_write_offs() != null) {
cellC.setCellValue(record.getR13_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row13
// Column D
cellD = row.createCell(3);
if (record.getR13_new_chars() != null) {
cellD.setCellValue(record.getR13_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row14
// Column B
row = sheet.getRow(13);

cellB = row.createCell(1);
if (record.getR14_recoveries() != null) {
cellB.setCellValue(record.getR14_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row14
// Column C
cellC = row.createCell(2);
if (record.getR14_write_offs() != null) {
cellC.setCellValue(record.getR14_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row14
// Column D
cellD = row.createCell(3);
if (record.getR14_new_chars() != null) {
cellD.setCellValue(record.getR14_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


// row16
// Column B
row = sheet.getRow(15);

cellB = row.createCell(1);
if (record.getR16_recoveries() != null) {
cellB.setCellValue(record.getR16_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row16
// Column C
cellC = row.createCell(2);
if (record.getR16_write_offs() != null) {
cellC.setCellValue(record.getR16_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row16
// Column D
cellD = row.createCell(3);
if (record.getR16_new_chars() != null) {
cellD.setCellValue(record.getR16_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row17
// Column B
row = sheet.getRow(16);

cellB = row.createCell(1);
if (record.getR17_recoveries() != null) {
cellB.setCellValue(record.getR17_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row17
// Column C
cellC = row.createCell(2);
if (record.getR17_write_offs() != null) {
cellC.setCellValue(record.getR17_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row17
// Column D
cellD = row.createCell(3);
if (record.getR17_new_chars() != null) {
cellD.setCellValue(record.getR17_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row18
// Column B
row = sheet.getRow(17);

cellB = row.createCell(1);
if (record.getR18_recoveries() != null) {
cellB.setCellValue(record.getR18_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row18
// Column C
cellC = row.createCell(2);
if (record.getR18_write_offs() != null) {
cellC.setCellValue(record.getR18_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row18
// Column D
cellD = row.createCell(3);
if (record.getR18_new_chars() != null) {
cellD.setCellValue(record.getR18_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row19
// Column B
row = sheet.getRow(18);

cellB = row.createCell(1);
if (record.getR19_recoveries() != null) {
cellB.setCellValue(record.getR19_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row19
// Column C
cellC = row.createCell(2);
if (record.getR19_write_offs() != null) {
cellC.setCellValue(record.getR19_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row19
// Column D
cellD = row.createCell(3);
if (record.getR19_new_chars() != null) {
cellD.setCellValue(record.getR19_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row20
// Column B
row = sheet.getRow(19);

cellB = row.createCell(1);
if (record.getR20_recoveries() != null) {
cellB.setCellValue(record.getR20_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row20
// Column C
cellC = row.createCell(2);
if (record.getR20_write_offs() != null) {
cellC.setCellValue(record.getR20_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row20
// Column D
cellD = row.createCell(3);
if (record.getR20_new_chars() != null) {
cellD.setCellValue(record.getR20_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row21
// Column B
row = sheet.getRow(20);

cellB = row.createCell(1);
if (record.getR21_recoveries() != null) {
cellB.setCellValue(record.getR21_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row21
// Column C
cellC = row.createCell(2);
if (record.getR21_write_offs() != null) {
cellC.setCellValue(record.getR21_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row21
// Column D
cellD = row.createCell(3);
if (record.getR21_new_chars() != null) {
cellD.setCellValue(record.getR21_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row22
// Column B
row = sheet.getRow(21);

cellB = row.createCell(1);
if (record.getR22_recoveries() != null) {
cellB.setCellValue(record.getR22_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row22
// Column C
cellC = row.createCell(2);
if (record.getR22_write_offs() != null) {
cellC.setCellValue(record.getR22_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row22
// Column D
cellD = row.createCell(3);
if (record.getR22_new_chars() != null) {
cellD.setCellValue(record.getR22_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row23
// Column B
row = sheet.getRow(22);

cellB = row.createCell(1);
if (record.getR23_recoveries() != null) {
cellB.setCellValue(record.getR23_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row23
// Column C
cellC = row.createCell(2);
if (record.getR23_write_offs() != null) {
cellC.setCellValue(record.getR23_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row23
// Column D
cellD = row.createCell(3);
if (record.getR23_new_chars() != null) {
cellD.setCellValue(record.getR23_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row24
// Column B
row = sheet.getRow(23);

cellB = row.createCell(1);
if (record.getR24_recoveries() != null) {
cellB.setCellValue(record.getR24_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row24
// Column C
cellC = row.createCell(2);
if (record.getR24_write_offs() != null) {
cellC.setCellValue(record.getR24_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row24
// Column D
cellD = row.createCell(3);
if (record.getR24_new_chars() != null) {
cellD.setCellValue(record.getR24_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row25
// Column B
row = sheet.getRow(24);

cellB = row.createCell(1);
if (record.getR25_recoveries() != null) {
cellB.setCellValue(record.getR25_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row25
// Column C
cellC = row.createCell(2);
if (record.getR25_write_offs() != null) {
cellC.setCellValue(record.getR25_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row25
// Column D
cellD = row.createCell(3);
if (record.getR25_new_chars() != null) {
cellD.setCellValue(record.getR25_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row26
// Column B
row = sheet.getRow(25);

cellB = row.createCell(1);
if (record.getR26_recoveries() != null) {
cellB.setCellValue(record.getR26_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row26
// Column C
cellC = row.createCell(2);
if (record.getR26_write_offs() != null) {
cellC.setCellValue(record.getR26_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row26
// Column D
cellD = row.createCell(3);
if (record.getR26_new_chars() != null) {
cellD.setCellValue(record.getR26_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row27
// Column B
row = sheet.getRow(26);

cellB = row.createCell(1);
if (record.getR27_recoveries() != null) {
cellB.setCellValue(record.getR27_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row27
// Column C
cellC = row.createCell(2);
if (record.getR27_write_offs() != null) {
cellC.setCellValue(record.getR27_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row27
// Column D
cellD = row.createCell(3);
if (record.getR27_new_chars() != null) {
cellD.setCellValue(record.getR27_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row28
// Column B
row = sheet.getRow(27);

cellB = row.createCell(1);
if (record.getR28_recoveries() != null) {
cellB.setCellValue(record.getR28_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row28
// Column C
cellC = row.createCell(2);
if (record.getR28_write_offs() != null) {
cellC.setCellValue(record.getR28_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row28
// Column D
cellD = row.createCell(3);
if (record.getR28_new_chars() != null) {
cellD.setCellValue(record.getR28_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


// row30
// Column B
row = sheet.getRow(29);

cellB = row.createCell(1);
if (record.getR30_recoveries() != null) {
cellB.setCellValue(record.getR30_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row30
// Column C
cellC = row.createCell(2);
if (record.getR30_write_offs() != null) {
cellC.setCellValue(record.getR30_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row30
// Column D
cellD = row.createCell(3);
if (record.getR30_new_chars() != null) {
cellD.setCellValue(record.getR30_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row31
// Column B
row = sheet.getRow(30);

cellB = row.createCell(1);
if (record.getR31_recoveries() != null) {
cellB.setCellValue(record.getR31_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row31
// Column C
cellC = row.createCell(2);
if (record.getR31_write_offs() != null) {
cellC.setCellValue(record.getR31_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row31
// Column D
cellD = row.createCell(3);
if (record.getR31_new_chars() != null) {
cellD.setCellValue(record.getR31_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row32
// Column B
row = sheet.getRow(31);

cellB = row.createCell(1);
if (record.getR32_recoveries() != null) {
cellB.setCellValue(record.getR32_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row32
// Column C
cellC = row.createCell(2);
if (record.getR32_write_offs() != null) {
cellC.setCellValue(record.getR32_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row32
// Column D
cellD = row.createCell(3);
if (record.getR32_new_chars() != null) {
cellD.setCellValue(record.getR32_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row33
// Column B
row = sheet.getRow(32);

cellB = row.createCell(1);
if (record.getR33_recoveries() != null) {
cellB.setCellValue(record.getR33_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row33
// Column C
cellC = row.createCell(2);
if (record.getR33_write_offs() != null) {
cellC.setCellValue(record.getR33_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row33
// Column D
cellD = row.createCell(3);
if (record.getR33_new_chars() != null) {
cellD.setCellValue(record.getR33_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row34
// Column B
row = sheet.getRow(33);

cellB = row.createCell(1);
if (record.getR34_recoveries() != null) {
cellB.setCellValue(record.getR34_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row34
// Column C
cellC = row.createCell(2);
if (record.getR34_write_offs() != null) {
cellC.setCellValue(record.getR34_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row34
// Column D
cellD = row.createCell(3);
if (record.getR34_new_chars() != null) {
cellD.setCellValue(record.getR34_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row35
// Column B
row = sheet.getRow(34);

cellB = row.createCell(1);
if (record.getR35_recoveries() != null) {
cellB.setCellValue(record.getR35_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row35
// Column C
cellC = row.createCell(2);
if (record.getR35_write_offs() != null) {
cellC.setCellValue(record.getR35_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row35
// Column D
cellD = row.createCell(3);
if (record.getR35_new_chars() != null) {
cellD.setCellValue(record.getR35_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row36
// Column B
row = sheet.getRow(35);

cellB = row.createCell(1);
if (record.getR36_recoveries() != null) {
cellB.setCellValue(record.getR36_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row36
// Column C
cellC = row.createCell(2);
if (record.getR36_write_offs() != null) {
cellC.setCellValue(record.getR36_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row36
// Column D
cellD = row.createCell(3);
if (record.getR36_new_chars() != null) {
cellD.setCellValue(record.getR36_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row37
// Column B
row = sheet.getRow(36);

cellB = row.createCell(1);
if (record.getR37_recoveries() != null) {
cellB.setCellValue(record.getR37_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row37
// Column C
cellC = row.createCell(2);
if (record.getR37_write_offs() != null) {
cellC.setCellValue(record.getR37_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row37
// Column D
cellD = row.createCell(3);
if (record.getR37_new_chars() != null) {
cellD.setCellValue(record.getR37_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


// row39
// Column B
row = sheet.getRow(38);

cellB = row.createCell(1);
if (record.getR39_recoveries() != null) {
cellB.setCellValue(record.getR39_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row39
// Column C
cellC = row.createCell(2);
if (record.getR39_write_offs() != null) {
cellC.setCellValue(record.getR39_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row39
// Column D
cellD = row.createCell(3);
if (record.getR39_new_chars() != null) {
cellD.setCellValue(record.getR39_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row40
// Column B
row = sheet.getRow(39);

cellB = row.createCell(1);
if (record.getR40_recoveries() != null) {
cellB.setCellValue(record.getR40_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row40
// Column C
cellC = row.createCell(2);
if (record.getR40_write_offs() != null) {
cellC.setCellValue(record.getR40_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row40
// Column D
cellD = row.createCell(3);
if (record.getR40_new_chars() != null) {
cellD.setCellValue(record.getR40_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row42
// Column B
row = sheet.getRow(41);

cellB = row.createCell(1);
if (record.getR42_recoveries() != null) {
cellB.setCellValue(record.getR42_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row42
// Column C
cellC = row.createCell(2);
if (record.getR42_write_offs() != null) {
cellC.setCellValue(record.getR42_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row42
// Column D
cellD = row.createCell(3);
if (record.getR42_new_chars() != null) {
cellD.setCellValue(record.getR42_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row43
// Column B
row = sheet.getRow(42);

cellB = row.createCell(1);
if (record.getR43_recoveries() != null) {
cellB.setCellValue(record.getR43_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row43
// Column C
cellC = row.createCell(2);
if (record.getR43_write_offs() != null) {
cellC.setCellValue(record.getR43_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row43
// Column D
cellD = row.createCell(3);
if (record.getR43_new_chars() != null) {
cellD.setCellValue(record.getR43_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row44
// Column B
row = sheet.getRow(43);

cellB = row.createCell(1);
if (record.getR44_recoveries() != null) {
cellB.setCellValue(record.getR44_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row44
// Column C
cellC = row.createCell(2);
if (record.getR44_write_offs() != null) {
cellC.setCellValue(record.getR44_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row44
// Column D
cellD = row.createCell(3);
if (record.getR44_new_chars() != null) {
cellD.setCellValue(record.getR44_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row45
// Column B
row = sheet.getRow(44);

cellB = row.createCell(1);
if (record.getR45_recoveries() != null) {
cellB.setCellValue(record.getR45_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row45
// Column C
cellC = row.createCell(2);
if (record.getR45_write_offs() != null) {
cellC.setCellValue(record.getR45_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row45
// Column D
cellD = row.createCell(3);
if (record.getR45_new_chars() != null) {
cellD.setCellValue(record.getR45_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row46
// Column B
row = sheet.getRow(45);

cellB = row.createCell(1);
if (record.getR46_recoveries() != null) {
cellB.setCellValue(record.getR46_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row46
// Column C
cellC = row.createCell(2);
if (record.getR46_write_offs() != null) {
cellC.setCellValue(record.getR46_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row46
// Column D
cellD = row.createCell(3);
if (record.getR46_new_chars() != null) {
cellD.setCellValue(record.getR46_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row47
// Column B
row = sheet.getRow(46);

cellB = row.createCell(1);
if (record.getR47_recoveries() != null) {
cellB.setCellValue(record.getR47_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row47
// Column C
cellC = row.createCell(2);
if (record.getR47_write_offs() != null) {
cellC.setCellValue(record.getR47_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row47
// Column D
cellD = row.createCell(3);
if (record.getR47_new_chars() != null) {
cellD.setCellValue(record.getR47_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row48
// Column B
row = sheet.getRow(47);

cellB = row.createCell(1);
if (record.getR48_recoveries() != null) {
cellB.setCellValue(record.getR48_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row48
// Column C
cellC = row.createCell(2);
if (record.getR48_write_offs() != null) {
cellC.setCellValue(record.getR48_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row48
// Column D
cellD = row.createCell(3);
if (record.getR48_new_chars() != null) {
cellD.setCellValue(record.getR48_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row50
// Column B
row = sheet.getRow(49);

cellB = row.createCell(1);
if (record.getR50_recoveries() != null) {
cellB.setCellValue(record.getR50_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row50
// Column C
cellC = row.createCell(2);
if (record.getR50_write_offs() != null) {
cellC.setCellValue(record.getR50_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row50
// Column D
cellD = row.createCell(3);
if (record.getR50_new_chars() != null) {
cellD.setCellValue(record.getR50_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row51
// Column B
row = sheet.getRow(50);

cellB = row.createCell(1);
if (record.getR51_recoveries() != null) {
cellB.setCellValue(record.getR51_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row51
// Column C
cellC = row.createCell(2);
if (record.getR51_write_offs() != null) {
cellC.setCellValue(record.getR51_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row51
// Column D
cellD = row.createCell(3);
if (record.getR51_new_chars() != null) {
cellD.setCellValue(record.getR51_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row52
// Column B
row = sheet.getRow(51);

cellB = row.createCell(1);
if (record.getR52_recoveries() != null) {
cellB.setCellValue(record.getR52_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row52
// Column C
cellC = row.createCell(2);
if (record.getR52_write_offs() != null) {
cellC.setCellValue(record.getR52_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row52
// Column D
cellD = row.createCell(3);
if (record.getR52_new_chars() != null) {
cellD.setCellValue(record.getR52_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row54
// Column B
row = sheet.getRow(53);

cellB = row.createCell(1);
if (record.getR54_recoveries() != null) {
cellB.setCellValue(record.getR54_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row54
// Column C
cellC = row.createCell(2);
if (record.getR54_write_offs() != null) {
cellC.setCellValue(record.getR54_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row54
// Column D
cellD = row.createCell(3);
if (record.getR54_new_chars() != null) {
cellD.setCellValue(record.getR54_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row55
// Column B
row = sheet.getRow(54);

cellB = row.createCell(1);
if (record.getR55_recoveries() != null) {
cellB.setCellValue(record.getR55_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row55
// Column C
cellC = row.createCell(2);
if (record.getR55_write_offs() != null) {
cellC.setCellValue(record.getR55_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row55
// Column D
cellD = row.createCell(3);
if (record.getR55_new_chars() != null) {
cellD.setCellValue(record.getR55_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row56
// Column B
row = sheet.getRow(55);

cellB = row.createCell(1);
if (record.getR56_recoveries() != null) {
cellB.setCellValue(record.getR56_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row56
// Column C
cellC = row.createCell(2);
if (record.getR56_write_offs() != null) {
cellC.setCellValue(record.getR56_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row56
// Column D
cellD = row.createCell(3);
if (record.getR56_new_chars() != null) {
cellD.setCellValue(record.getR56_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


// row58
// Column B
row = sheet.getRow(57);

cellB = row.createCell(1);
if (record.getR58_recoveries() != null) {
cellB.setCellValue(record.getR58_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row58
// Column C
cellC = row.createCell(2);
if (record.getR58_write_offs() != null) {
cellC.setCellValue(record.getR58_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row58
// Column D
cellD = row.createCell(3);
if (record.getR58_new_chars() != null) {
cellD.setCellValue(record.getR58_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row59
// Column B
row = sheet.getRow(58);

cellB = row.createCell(1);
if (record.getR59_recoveries() != null) {
cellB.setCellValue(record.getR59_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row59
// Column C
cellC = row.createCell(2);
if (record.getR59_write_offs() != null) {
cellC.setCellValue(record.getR59_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row59
// Column D
cellD = row.createCell(3);
if (record.getR59_new_chars() != null) {
cellD.setCellValue(record.getR59_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row60
// Column B
row = sheet.getRow(59);

cellB = row.createCell(1);
if (record.getR60_recoveries() != null) {
cellB.setCellValue(record.getR60_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row60
// Column C
cellC = row.createCell(2);
if (record.getR60_write_offs() != null) {
cellC.setCellValue(record.getR60_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row60
// Column D
cellD = row.createCell(3);
if (record.getR60_new_chars() != null) {
cellD.setCellValue(record.getR60_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row61
// Column B
row = sheet.getRow(60);

cellB = row.createCell(1);
if (record.getR61_recoveries() != null) {
cellB.setCellValue(record.getR61_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row61
// Column C
cellC = row.createCell(2);
if (record.getR61_write_offs() != null) {
cellC.setCellValue(record.getR61_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row61
// Column D
cellD = row.createCell(3);
if (record.getR61_new_chars() != null) {
cellD.setCellValue(record.getR61_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row62
// Column B
row = sheet.getRow(61);

cellB = row.createCell(1);
if (record.getR62_recoveries() != null) {
cellB.setCellValue(record.getR62_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row62
// Column C
cellC = row.createCell(2);
if (record.getR62_write_offs() != null) {
cellC.setCellValue(record.getR62_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row62
// Column D
cellD = row.createCell(3);
if (record.getR62_new_chars() != null) {
cellD.setCellValue(record.getR62_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row63
// Column B
row = sheet.getRow(62);

cellB = row.createCell(1);
if (record.getR63_recoveries() != null) {
cellB.setCellValue(record.getR63_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row63
// Column C
cellC = row.createCell(2);
if (record.getR63_write_offs() != null) {
cellC.setCellValue(record.getR63_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row63
// Column D
cellD = row.createCell(3);
if (record.getR63_new_chars() != null) {
cellD.setCellValue(record.getR63_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row67
// Column E

row = sheet.getRow(66);

cellE = row.createCell(4);
if (record.getR67_total() != null) {
cellE.setCellValue(record.getR67_total().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}

// row68
// Column E

row = sheet.getRow(67);

cellE = row.createCell(4);
if (record.getR68_total() != null) {
cellE.setCellValue(record.getR68_total().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}


}
workbook.setForceFormulaRecalculation(true);
} else {

}

// Write the final workbook content to the in-memory stream.
workbook.write(out);

logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());


//audit service summary format

ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
							if (attrs != null) {
								HttpServletRequest request = attrs.getRequest();
								String userid = (String) request.getSession().getAttribute("USERID");
								auditService.createBusinessAudit(userid, "DOWNLOAD", "M_I_S_CA SUMMARY", null, "BRRS_M_I_S_CA_SUMMARYTABLE");
							}


return out.toByteArray();
}	
}
}
}

// Normal Email Excel
public byte[] BRRS_M_I_S_CAEmailExcel(String filename, String reportId, String fromdate, String todate,
String currency, String dtltype, String type, BigDecimal version) throws Exception {

logger.info("Service: Starting Email Excel generation process in memory.");

if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
try {
// Redirecting to Archival
return BRRS_M_I_S_CAARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
} catch (ParseException e) {
logger.error("Invalid report date format: {}", fromdate, e);
throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
}
} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
logger.info("Service: Generating RESUB report for version {}", version);

try {
// ✅ Redirecting to Resub Excel
return BRRS_M_I_S_CAEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

} catch (ParseException e) {
logger.error("Invalid report date format: {}", fromdate, e);
throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
}
} 
else {
List<M_I_S_CA_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

if (dataList.isEmpty()) {
logger.warn("Service: No data found for BRRS_M_I_S_CA report. Returning empty result.");
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
M_I_S_CA_Summary_Entity record = dataList.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}


//row7
//Column B
Cell cellBdate = row.createCell(1);
if (record.getReport_date() != null) {
	cellBdate.setCellValue(record.getReport_date());
	cellBdate.setCellStyle(dateStyle);
} else {
	cellBdate.setCellValue("");
	cellBdate.setCellStyle(textStyle);
}



//ROW 10
row = sheet.getRow(9);

//ROW10  Balance at the beginning of period


// row10
// Column E
Cell cellE = row.createCell(4);
if (record.getR10_total() != null) {
cellE.setCellValue(record.getR10_total().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}



//ROW11  Non-financial institutional units (sum of lines (i) to (vii))

//ROW12  Central Government

// row12
// Column B

row = sheet.getRow(11);

Cell cellB = row.createCell(1);
if (record.getR12_recoveries() != null) {
cellB.setCellValue(record.getR12_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row12
// Column C
Cell cellC = row.createCell(2);
if (record.getR12_write_offs() != null) {
cellC.setCellValue(record.getR12_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row12
// Column D
Cell cellD = row.createCell(3);
if (record.getR12_new_chars() != null) {
cellD.setCellValue(record.getR12_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW13  Local Government

// row13
// Column B

row = sheet.getRow(12);

cellB = row.createCell(1);
if (record.getR13_recoveries() != null) {
cellB.setCellValue(record.getR13_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row13
// Column C
cellC = row.createCell(2);
if (record.getR13_write_offs() != null) {
cellC.setCellValue(record.getR13_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row13
// Column D
cellD = row.createCell(3);
if (record.getR13_new_chars() != null) {
cellD.setCellValue(record.getR13_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW14  Public Non-Financial Corporations

// row14
// Column B
row = sheet.getRow(13);

cellB = row.createCell(1);
if (record.getR14_recoveries() != null) {
cellB.setCellValue(record.getR14_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row14
// Column C
cellC = row.createCell(2);
if (record.getR14_write_offs() != null) {
cellC.setCellValue(record.getR14_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row14
// Column D
cellD = row.createCell(3);
if (record.getR14_new_chars() != null) {
cellD.setCellValue(record.getR14_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




//ROW15  Other Non-Financial Corporations (Private business enterprises)

//ROW16  Agriculture, Forestry, Fishing

// row16
// Column B
row = sheet.getRow(15);

cellB = row.createCell(1);
if (record.getR16_recoveries() != null) {
cellB.setCellValue(record.getR16_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row16
// Column C
cellC = row.createCell(2);
if (record.getR16_write_offs() != null) {
cellC.setCellValue(record.getR16_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row16
// Column D
cellD = row.createCell(3);
if (record.getR16_new_chars() != null) {
cellD.setCellValue(record.getR16_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW17  Mining and Quarying

// row17
// Column B
row = sheet.getRow(16);

cellB = row.createCell(1);
if (record.getR17_recoveries() != null) {
cellB.setCellValue(record.getR17_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row17
// Column C
cellC = row.createCell(2);
if (record.getR17_write_offs() != null) {
cellC.setCellValue(record.getR17_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row17
// Column D
cellD = row.createCell(3);
if (record.getR17_new_chars() != null) {
cellD.setCellValue(record.getR17_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW18  Manufacturing

// row18
// Column B
row = sheet.getRow(17);

cellB = row.createCell(1);
if (record.getR18_recoveries() != null) {
cellB.setCellValue(record.getR18_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row18
// Column C
cellC = row.createCell(2);
if (record.getR18_write_offs() != null) {
cellC.setCellValue(record.getR18_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row18
// Column D
cellD = row.createCell(3);
if (record.getR18_new_chars() != null) {
cellD.setCellValue(record.getR18_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW19  Construction

// row19
// Column B
row = sheet.getRow(18);

cellB = row.createCell(1);
if (record.getR19_recoveries() != null) {
cellB.setCellValue(record.getR19_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row19
// Column C
cellC = row.createCell(2);
if (record.getR19_write_offs() != null) {
cellC.setCellValue(record.getR19_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row19
// Column D
cellD = row.createCell(3);
if (record.getR19_new_chars() != null) {
cellD.setCellValue(record.getR19_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW20  Commercial real estate

// row20
// Column B
row = sheet.getRow(19);

cellB = row.createCell(1);
if (record.getR20_recoveries() != null) {
cellB.setCellValue(record.getR20_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row20
// Column C
cellC = row.createCell(2);
if (record.getR20_write_offs() != null) {
cellC.setCellValue(record.getR20_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row20
// Column D
cellD = row.createCell(3);
if (record.getR20_new_chars() != null) {
cellD.setCellValue(record.getR20_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW21  Electricity

// row21
// Column B
row = sheet.getRow(20);

cellB = row.createCell(1);
if (record.getR21_recoveries() != null) {
cellB.setCellValue(record.getR21_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row21
// Column C
cellC = row.createCell(2);
if (record.getR21_write_offs() != null) {
cellC.setCellValue(record.getR21_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row21
// Column D
cellD = row.createCell(3);
if (record.getR21_new_chars() != null) {
cellD.setCellValue(record.getR21_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW22  Water

// row22
// Column B
row = sheet.getRow(21);

cellB = row.createCell(1);
if (record.getR22_recoveries() != null) {
cellB.setCellValue(record.getR22_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row22
// Column C
cellC = row.createCell(2);
if (record.getR22_write_offs() != null) {
cellC.setCellValue(record.getR22_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row22
// Column D
cellD = row.createCell(3);
if (record.getR22_new_chars() != null) {
cellD.setCellValue(record.getR22_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW23  Telecommunication and Post

// row23
// Column B
row = sheet.getRow(22);

cellB = row.createCell(1);
if (record.getR23_recoveries() != null) {
cellB.setCellValue(record.getR23_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row23
// Column C
cellC = row.createCell(2);
if (record.getR23_write_offs() != null) {
cellC.setCellValue(record.getR23_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row23
// Column D
cellD = row.createCell(3);
if (record.getR23_new_chars() != null) {
cellD.setCellValue(record.getR23_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW24  Tourism and hotels

// row24
// Column B
row = sheet.getRow(23);

cellB = row.createCell(1);
if (record.getR24_recoveries() != null) {
cellB.setCellValue(record.getR24_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row24
// Column C
cellC = row.createCell(2);
if (record.getR24_write_offs() != null) {
cellC.setCellValue(record.getR24_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row24
// Column D
cellD = row.createCell(3);
if (record.getR24_new_chars() != null) {
cellD.setCellValue(record.getR24_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row24
// Column E

cellE = row.createCell(4);
if (record.getR24_total() != null) {
cellE.setCellValue(record.getR24_total().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}

//ROW25  Transport and Storage

// row25
// Column B
row = sheet.getRow(24);

cellB = row.createCell(1);
if (record.getR25_recoveries() != null) {
cellB.setCellValue(record.getR25_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row25
// Column C
cellC = row.createCell(2);
if (record.getR25_write_offs() != null) {
cellC.setCellValue(record.getR25_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row25
// Column D
cellD = row.createCell(3);
if (record.getR25_new_chars() != null) {
cellD.setCellValue(record.getR25_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW26  Trade, restaurants and bars

// row26
// Column B
row = sheet.getRow(25);

cellB = row.createCell(1);
if (record.getR26_recoveries() != null) {
cellB.setCellValue(record.getR26_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row26
// Column C
cellC = row.createCell(2);
if (record.getR26_write_offs() != null) {
cellC.setCellValue(record.getR26_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row26
// Column D
cellD = row.createCell(3);
if (record.getR26_new_chars() != null) {
cellD.setCellValue(record.getR26_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW27  Business Services

// row27
// Column B
row = sheet.getRow(26);

cellB = row.createCell(1);
if (record.getR27_recoveries() != null) {
cellB.setCellValue(record.getR27_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row27
// Column C
cellC = row.createCell(2);
if (record.getR27_write_offs() != null) {
cellC.setCellValue(record.getR27_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row27
// Column D
cellD = row.createCell(3);
if (record.getR27_new_chars() != null) {
cellD.setCellValue(record.getR27_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW28  Other Community, Social and Personal Services

// row28
// Column B
row = sheet.getRow(27);

cellB = row.createCell(1);
if (record.getR28_recoveries() != null) {
cellB.setCellValue(record.getR28_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row28
// Column C
cellC = row.createCell(2);
if (record.getR28_write_offs() != null) {
cellC.setCellValue(record.getR28_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row28
// Column D
cellD = row.createCell(3);
if (record.getR28_new_chars() != null) {
cellD.setCellValue(record.getR28_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW29  Households

//ROW30  Residential property (owner occupied)

// row30
// Column B
row = sheet.getRow(29);

cellB = row.createCell(1);
if (record.getR30_recoveries() != null) {
cellB.setCellValue(record.getR30_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row30
// Column C
cellC = row.createCell(2);
if (record.getR30_write_offs() != null) {
cellC.setCellValue(record.getR30_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row30
// Column D
cellD = row.createCell(3);
if (record.getR30_new_chars() != null) {
cellD.setCellValue(record.getR30_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW31  Residential property (rented)

// row31
// Column B
row = sheet.getRow(30);

cellB = row.createCell(1);
if (record.getR31_recoveries() != null) {
cellB.setCellValue(record.getR31_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row31
// Column C
cellC = row.createCell(2);
if (record.getR31_write_offs() != null) {
cellC.setCellValue(record.getR31_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row31
// Column D
cellD = row.createCell(3);
if (record.getR31_new_chars() != null) {
cellD.setCellValue(record.getR31_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW32  Personal Loans

// row32
// Column B
row = sheet.getRow(31);

cellB = row.createCell(1);
if (record.getR32_recoveries() != null) {
cellB.setCellValue(record.getR32_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row32
// Column C
cellC = row.createCell(2);
if (record.getR32_write_offs() != null) {
cellC.setCellValue(record.getR32_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row32
// Column D
cellD = row.createCell(3);
if (record.getR32_new_chars() != null) {
cellD.setCellValue(record.getR32_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




//ROW33  Motor vehicle

// row33
// Column B
row = sheet.getRow(32);

cellB = row.createCell(1);
if (record.getR33_recoveries() != null) {
cellB.setCellValue(record.getR33_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}


// row33
// Column C
cellC = row.createCell(2);
if (record.getR33_write_offs() != null) {
cellC.setCellValue(record.getR33_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row33
// Column D
cellD = row.createCell(3);
if (record.getR33_new_chars() != null) {
cellD.setCellValue(record.getR33_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW34  Household goods

// row34
// Column B
row = sheet.getRow(33);

cellB = row.createCell(1);
if (record.getR34_recoveries() != null) {
cellB.setCellValue(record.getR34_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row34
// Column C
cellC = row.createCell(2);
if (record.getR34_write_offs() != null) {
cellC.setCellValue(record.getR34_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row34
// Column D
cellD = row.createCell(3);
if (record.getR34_new_chars() != null) {
cellD.setCellValue(record.getR34_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW35  Credit card loans

// row35
// Column B
row = sheet.getRow(34);

cellB = row.createCell(1);
if (record.getR35_recoveries() != null) {
cellB.setCellValue(record.getR35_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row35
// Column C
cellC = row.createCell(2);
if (record.getR35_write_offs() != null) {
cellC.setCellValue(record.getR35_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row35
// Column D
cellD = row.createCell(3);
if (record.getR35_new_chars() != null) {
cellD.setCellValue(record.getR35_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




//ROW36  Non-Profit Institutions Serving Households

// row36
// Column B
row = sheet.getRow(35);

cellB = row.createCell(1);
if (record.getR36_recoveries() != null) {
cellB.setCellValue(record.getR36_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row36
// Column C
cellC = row.createCell(2);
if (record.getR36_write_offs() != null) {
cellC.setCellValue(record.getR36_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row36
// Column D
cellD = row.createCell(3);
if (record.getR36_new_chars() != null) {
cellD.setCellValue(record.getR36_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW37  Other

// row37
// Column B
row = sheet.getRow(36);

cellB = row.createCell(1);
if (record.getR37_recoveries() != null) {
cellB.setCellValue(record.getR37_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row37
// Column C
cellC = row.createCell(2);
if (record.getR37_write_offs() != null) {
cellC.setCellValue(record.getR37_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row37
// Column D
cellD = row.createCell(3);
if (record.getR37_new_chars() != null) {
cellD.setCellValue(record.getR37_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW38  Non-Residents

// row38
// Column B
row = sheet.getRow(37);

cellB = row.createCell(1);
if (record.getR38_recoveries() != null) {
cellB.setCellValue(record.getR38_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}


// row38
// Column D
cellD = row.createCell(3);
if (record.getR38_new_chars() != null) {
cellD.setCellValue(record.getR38_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW39  Other Non-Financial Corporations

// row39
// Column B
row = sheet.getRow(38);

cellB = row.createCell(1);
if (record.getR39_recoveries() != null) {
cellB.setCellValue(record.getR39_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row39
// Column C
cellC = row.createCell(2);
if (record.getR39_write_offs() != null) {
cellC.setCellValue(record.getR39_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row39
// Column D
cellD = row.createCell(3);
if (record.getR39_new_chars() != null) {
cellD.setCellValue(record.getR39_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW40  Households


// row40
// Column B
row = sheet.getRow(39);

cellB = row.createCell(1);
if (record.getR40_recoveries() != null) {
cellB.setCellValue(record.getR40_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row40
// Column C
cellC = row.createCell(2);
if (record.getR40_write_offs() != null) {
cellC.setCellValue(record.getR40_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row40
// Column D
cellD = row.createCell(3);
if (record.getR40_new_chars() != null) {
cellD.setCellValue(record.getR40_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW41  Financial institutional units

// row41
// Column B
row = sheet.getRow(40);

cellB = row.createCell(1);
if (record.getR41_recoveries() != null) {
cellB.setCellValue(record.getR41_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}


// row41
// Column D
cellD = row.createCell(3);
if (record.getR41_new_chars() != null) {
cellD.setCellValue(record.getR41_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW42  Central Bank

// row42
// Column B
row = sheet.getRow(41);

cellB = row.createCell(1);
if (record.getR42_recoveries() != null) {
cellB.setCellValue(record.getR42_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row42
// Column C
cellC = row.createCell(2);
if (record.getR42_write_offs() != null) {
cellC.setCellValue(record.getR42_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row42
// Column D
cellD = row.createCell(3);
if (record.getR42_new_chars() != null) {
cellD.setCellValue(record.getR42_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW43  Commercial Banks

// row43
// Column B
row = sheet.getRow(42);

cellB = row.createCell(1);
if (record.getR43_recoveries() != null) {
cellB.setCellValue(record.getR43_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row43
// Column C
cellC = row.createCell(2);
if (record.getR43_write_offs() != null) {
cellC.setCellValue(record.getR43_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row43
// Column D
cellD = row.createCell(3);
if (record.getR43_new_chars() != null) {
cellD.setCellValue(record.getR43_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW44  Other Depository Corporations

// row44
// Column B
row = sheet.getRow(43);

cellB = row.createCell(1);
if (record.getR44_recoveries() != null) {
cellB.setCellValue(record.getR44_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row44
// Column C
cellC = row.createCell(2);
if (record.getR44_write_offs() != null) {
cellC.setCellValue(record.getR44_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row44
// Column D
cellD = row.createCell(3);
if (record.getR44_new_chars() != null) {
cellD.setCellValue(record.getR44_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW45  Botswana Savings Bank (BSB)

// row45
// Column B
row = sheet.getRow(44);

cellB = row.createCell(1);
if (record.getR45_recoveries() != null) {
cellB.setCellValue(record.getR45_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row45
// Column C
cellC = row.createCell(2);
if (record.getR45_write_offs() != null) {
cellC.setCellValue(record.getR45_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row45
// Column D
cellD = row.createCell(3);
if (record.getR45_new_chars() != null) {
cellD.setCellValue(record.getR45_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW46  Botswana Building Society (BBS)

// row46
// Column B
row = sheet.getRow(45);

cellB = row.createCell(1);
if (record.getR46_recoveries() != null) {
cellB.setCellValue(record.getR46_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row46
// Column C
cellC = row.createCell(2);
if (record.getR46_write_offs() != null) {
cellC.setCellValue(record.getR46_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row46
// Column D
cellD = row.createCell(3);
if (record.getR46_new_chars() != null) {
cellD.setCellValue(record.getR46_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW47  Domestic Money Market Unit Trust

// row47
// Column B
row = sheet.getRow(46);

cellB = row.createCell(1);
if (record.getR47_recoveries() != null) {
cellB.setCellValue(record.getR47_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row47
// Column C
cellC = row.createCell(2);
if (record.getR47_write_offs() != null) {
cellC.setCellValue(record.getR47_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row47
// Column D
cellD = row.createCell(3);
if (record.getR47_new_chars() != null) {
cellD.setCellValue(record.getR47_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW48  Other (specify)

// row48
// Column B
row = sheet.getRow(47);

cellB = row.createCell(1);
if (record.getR48_recoveries() != null) {
cellB.setCellValue(record.getR48_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row48
// Column C
cellC = row.createCell(2);
if (record.getR48_write_offs() != null) {
cellC.setCellValue(record.getR48_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row48
// Column D
cellD = row.createCell(3);
if (record.getR48_new_chars() != null) {
cellD.setCellValue(record.getR48_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW49  Other Financial Corporations

// row49
// Column B
row = sheet.getRow(48);

cellB = row.createCell(1);
if (record.getR49_recoveries() != null) {
cellB.setCellValue(record.getR49_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}


// row49
// Column D
cellD = row.createCell(3);
if (record.getR49_new_chars() != null) {
cellD.setCellValue(record.getR49_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW50  Insurance Companies

// row50
// Column B
row = sheet.getRow(49);

cellB = row.createCell(1);
if (record.getR50_recoveries() != null) {
cellB.setCellValue(record.getR50_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row50
// Column C
cellC = row.createCell(2);
if (record.getR50_write_offs() != null) {
cellC.setCellValue(record.getR50_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row50
// Column D
cellD = row.createCell(3);
if (record.getR50_new_chars() != null) {
cellD.setCellValue(record.getR50_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW51  Pension Funds

// row51
// Column B
row = sheet.getRow(50);

cellB = row.createCell(1);
if (record.getR51_recoveries() != null) {
cellB.setCellValue(record.getR51_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row51
// Column C
cellC = row.createCell(2);
if (record.getR51_write_offs() != null) {
cellC.setCellValue(record.getR51_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row51
// Column D
cellD = row.createCell(3);
if (record.getR51_new_chars() != null) {
cellD.setCellValue(record.getR51_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW52  SACCOs

// row52
// Column B
row = sheet.getRow(51);

cellB = row.createCell(1);
if (record.getR52_recoveries() != null) {
cellB.setCellValue(record.getR52_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row52
// Column C
cellC = row.createCell(2);
if (record.getR52_write_offs() != null) {
cellC.setCellValue(record.getR52_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row52
// Column D
cellD = row.createCell(3);
if (record.getR52_new_chars() != null) {
cellD.setCellValue(record.getR52_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW53  Other Financial Intermediaries

// row53
// Column B
row = sheet.getRow(52);

cellB = row.createCell(1);
if (record.getR53_recoveries() != null) {
cellB.setCellValue(record.getR53_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}


// row53
// Column D
cellD = row.createCell(3);
if (record.getR53_new_chars() != null) {
cellD.setCellValue(record.getR53_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW54  Finance companies

// row54
// Column B
row = sheet.getRow(53);

cellB = row.createCell(1);
if (record.getR54_recoveries() != null) {
cellB.setCellValue(record.getR54_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row54
// Column C
cellC = row.createCell(2);
if (record.getR54_write_offs() != null) {
cellC.setCellValue(record.getR54_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row54
// Column D
cellD = row.createCell(3);
if (record.getR54_new_chars() != null) {
cellD.setCellValue(record.getR54_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW55  Medical Aid Schemes

// row55
// Column B
row = sheet.getRow(54);

cellB = row.createCell(1);
if (record.getR55_recoveries() != null) {
cellB.setCellValue(record.getR55_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row55
// Column C
cellC = row.createCell(2);
if (record.getR55_write_offs() != null) {
cellC.setCellValue(record.getR55_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row55
// Column D
cellD = row.createCell(3);
if (record.getR55_new_chars() != null) {
cellD.setCellValue(record.getR55_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW56  Public sector financial intermediaries

// row56
// Column B
row = sheet.getRow(55);

cellB = row.createCell(1);
if (record.getR56_recoveries() != null) {
cellB.setCellValue(record.getR56_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row56
// Column C
cellC = row.createCell(2);
if (record.getR56_write_offs() != null) {
cellC.setCellValue(record.getR56_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row56
// Column D
cellD = row.createCell(3);
if (record.getR56_new_chars() != null) {
cellD.setCellValue(record.getR56_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW57  Financial Auxiliaries

// row57
// Column B
row = sheet.getRow(56);

cellB = row.createCell(1);
if (record.getR57_recoveries() != null) {
cellB.setCellValue(record.getR57_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}


// row57
// Column D
cellD = row.createCell(3);
if (record.getR57_new_chars() != null) {
cellD.setCellValue(record.getR57_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW58  Insurance brokers


// row58
// Column B
row = sheet.getRow(57);

cellB = row.createCell(1);
if (record.getR58_recoveries() != null) {
cellB.setCellValue(record.getR58_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row58
// Column C
cellC = row.createCell(2);
if (record.getR58_write_offs() != null) {
cellC.setCellValue(record.getR58_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row58
// Column D
cellD = row.createCell(3);
if (record.getR58_new_chars() != null) {
cellD.setCellValue(record.getR58_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW59  fund administrators

// row59
// Column B
row = sheet.getRow(58);

cellB = row.createCell(1);
if (record.getR59_recoveries() != null) {
cellB.setCellValue(record.getR59_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row59
// Column C
cellC = row.createCell(2);
if (record.getR59_write_offs() != null) {
cellC.setCellValue(record.getR59_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row59
// Column D
cellD = row.createCell(3);
if (record.getR59_new_chars() != null) {
cellD.setCellValue(record.getR59_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW60  Bureau de change

// row60
// Column B
row = sheet.getRow(59);

cellB = row.createCell(1);
if (record.getR60_recoveries() != null) {
cellB.setCellValue(record.getR60_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row60
// Column C
cellC = row.createCell(2);
if (record.getR60_write_offs() != null) {
cellC.setCellValue(record.getR60_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row60
// Column D
cellD = row.createCell(3);
if (record.getR60_new_chars() != null) {
cellD.setCellValue(record.getR60_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW61  Asset managers

// row61
// Column B
row = sheet.getRow(60);

cellB = row.createCell(1);
if (record.getR61_recoveries() != null) {
cellB.setCellValue(record.getR61_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row61
// Column C
cellC = row.createCell(2);
if (record.getR61_write_offs() != null) {
cellC.setCellValue(record.getR61_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row61
// Column D
cellD = row.createCell(3);
if (record.getR61_new_chars() != null) {
cellD.setCellValue(record.getR61_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW62  Other (specify)

// row62
// Column B
row = sheet.getRow(61);

cellB = row.createCell(1);
if (record.getR62_recoveries() != null) {
cellB.setCellValue(record.getR62_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row62
// Column C
cellC = row.createCell(2);
if (record.getR62_write_offs() != null) {
cellC.setCellValue(record.getR62_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row62
// Column D
cellD = row.createCell(3);
if (record.getR62_new_chars() != null) {
cellD.setCellValue(record.getR62_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW63  Non-residents

// row63
// Column B
row = sheet.getRow(62);

cellB = row.createCell(1);
if (record.getR63_recoveries() != null) {
cellB.setCellValue(record.getR63_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row63
// Column C
cellC = row.createCell(2);
if (record.getR63_write_offs() != null) {
cellC.setCellValue(record.getR63_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row63
// Column D
cellD = row.createCell(3);
if (record.getR63_new_chars() != null) {
cellD.setCellValue(record.getR63_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW64  Total specific impairments
//ROW65  Specific impairments Balance at end of period (after adj. for recoveries and write-offs)
//ROW66  Collectively assessed (General) impairments
//ROW67  Balance at beginning of period

// row67
// Column E

row = sheet.getRow(66);

cellE = row.createCell(4);
if (record.getR67_total() != null) {
cellE.setCellValue(record.getR67_total().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}


//ROW68  Charge to P&L
//ROW69  Balance at end of period
//ROW70  TOTAL IMPAIRMENTS (LINES 5 AND 9)

}
workbook.setForceFormulaRecalculation(true);
} else {

}

// Write the final workbook content to the in-memory stream.
workbook.write(out);

logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

//audit service summary email

ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_I_S_CA EMAIL SUMMARY", null, "BRRS_M_I_S_CA_SUMMARYTABLE");
					}

return out.toByteArray();
}
}
}



// Archival format excel
public byte[] getExcelM_I_S_CAARCHIVAL(String filename, String reportId, String fromdate, String todate,
String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

logger.info("Service: Starting Excel generation process in memory in Archival.");

if ("email".equalsIgnoreCase(format) && version != null) {
try {
// Redirecting to Archival
return BRRS_M_I_S_CAARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
} catch (ParseException e) {
logger.error("Invalid report date format: {}", fromdate, e);
throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
}
} 

List<M_I_S_CA_Arcihval_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);

if (dataList.isEmpty()) {
logger.warn("Service: No data found for M_I_S_CA report. Returning empty result.");
return new byte[0];
}

String templateDir = env.getProperty("output.exportpathtemp");
String templateFileName = filename;
System.out.println(filename);
Path templatePath = Paths.get(templateDir, templateFileName);
System.out.println(templatePath);

logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

if (!Files.exists(templatePath)) {
//This specific exception will be caught by the controller.
throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
}
if (!Files.isReadable(templatePath)) {
//A specific exception for permission errors.
throw new SecurityException(
"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
}

//This try-with-resources block is perfect. It guarantees all resources are
//closed automatically.
try (InputStream templateInputStream = Files.newInputStream(templatePath);
Workbook workbook = WorkbookFactory.create(templateInputStream);
ByteArrayOutputStream out = new ByteArrayOutputStream()) {

Sheet sheet = workbook.getSheetAt(0);

//--- Style Definitions ---
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
//--- End of Style Definitions ---

int startRow = 6;

if (!dataList.isEmpty()) {
for (int i = 0; i < dataList.size(); i++) {
	M_I_S_CA_Arcihval_Summary_Entity record = dataList.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}


//row7
//Column B
Cell cellBdate = row.createCell(1);
if (record.getReport_date() != null) {
	cellBdate.setCellValue(record.getReport_date());
	cellBdate.setCellStyle(dateStyle);
} else {
	cellBdate.setCellValue("");
	cellBdate.setCellStyle(textStyle);
}



//ROW 10
row = sheet.getRow(9);
// row10
// Column E
Cell cellE = row.createCell(4);
if (record.getR10_total() != null) {
cellE.setCellValue(record.getR10_total().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}

// row12
// Column B

row = sheet.getRow(11);

Cell cellB = row.createCell(1);
if (record.getR12_recoveries() != null) {
cellB.setCellValue(record.getR12_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row12
// Column C
Cell cellC = row.createCell(2);
if (record.getR12_write_offs() != null) {
cellC.setCellValue(record.getR12_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row12
// Column D
Cell cellD = row.createCell(3);
if (record.getR12_new_chars() != null) {
cellD.setCellValue(record.getR12_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




// row13
// Column B

row = sheet.getRow(12);

cellB = row.createCell(1);
if (record.getR13_recoveries() != null) {
cellB.setCellValue(record.getR13_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row13
// Column C
cellC = row.createCell(2);
if (record.getR13_write_offs() != null) {
cellC.setCellValue(record.getR13_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row13
// Column D
cellD = row.createCell(3);
if (record.getR13_new_chars() != null) {
cellD.setCellValue(record.getR13_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row14
// Column B
row = sheet.getRow(13);

cellB = row.createCell(1);
if (record.getR14_recoveries() != null) {
cellB.setCellValue(record.getR14_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row14
// Column C
cellC = row.createCell(2);
if (record.getR14_write_offs() != null) {
cellC.setCellValue(record.getR14_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row14
// Column D
cellD = row.createCell(3);
if (record.getR14_new_chars() != null) {
cellD.setCellValue(record.getR14_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


// row16
// Column B
row = sheet.getRow(15);

cellB = row.createCell(1);
if (record.getR16_recoveries() != null) {
cellB.setCellValue(record.getR16_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row16
// Column C
cellC = row.createCell(2);
if (record.getR16_write_offs() != null) {
cellC.setCellValue(record.getR16_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row16
// Column D
cellD = row.createCell(3);
if (record.getR16_new_chars() != null) {
cellD.setCellValue(record.getR16_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row17
// Column B
row = sheet.getRow(16);

cellB = row.createCell(1);
if (record.getR17_recoveries() != null) {
cellB.setCellValue(record.getR17_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row17
// Column C
cellC = row.createCell(2);
if (record.getR17_write_offs() != null) {
cellC.setCellValue(record.getR17_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row17
// Column D
cellD = row.createCell(3);
if (record.getR17_new_chars() != null) {
cellD.setCellValue(record.getR17_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row18
// Column B
row = sheet.getRow(17);

cellB = row.createCell(1);
if (record.getR18_recoveries() != null) {
cellB.setCellValue(record.getR18_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row18
// Column C
cellC = row.createCell(2);
if (record.getR18_write_offs() != null) {
cellC.setCellValue(record.getR18_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row18
// Column D
cellD = row.createCell(3);
if (record.getR18_new_chars() != null) {
cellD.setCellValue(record.getR18_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row19
// Column B
row = sheet.getRow(18);

cellB = row.createCell(1);
if (record.getR19_recoveries() != null) {
cellB.setCellValue(record.getR19_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row19
// Column C
cellC = row.createCell(2);
if (record.getR19_write_offs() != null) {
cellC.setCellValue(record.getR19_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row19
// Column D
cellD = row.createCell(3);
if (record.getR19_new_chars() != null) {
cellD.setCellValue(record.getR19_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row20
// Column B
row = sheet.getRow(19);

cellB = row.createCell(1);
if (record.getR20_recoveries() != null) {
cellB.setCellValue(record.getR20_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row20
// Column C
cellC = row.createCell(2);
if (record.getR20_write_offs() != null) {
cellC.setCellValue(record.getR20_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row20
// Column D
cellD = row.createCell(3);
if (record.getR20_new_chars() != null) {
cellD.setCellValue(record.getR20_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row21
// Column B
row = sheet.getRow(20);

cellB = row.createCell(1);
if (record.getR21_recoveries() != null) {
cellB.setCellValue(record.getR21_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row21
// Column C
cellC = row.createCell(2);
if (record.getR21_write_offs() != null) {
cellC.setCellValue(record.getR21_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row21
// Column D
cellD = row.createCell(3);
if (record.getR21_new_chars() != null) {
cellD.setCellValue(record.getR21_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row22
// Column B
row = sheet.getRow(21);

cellB = row.createCell(1);
if (record.getR22_recoveries() != null) {
cellB.setCellValue(record.getR22_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row22
// Column C
cellC = row.createCell(2);
if (record.getR22_write_offs() != null) {
cellC.setCellValue(record.getR22_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row22
// Column D
cellD = row.createCell(3);
if (record.getR22_new_chars() != null) {
cellD.setCellValue(record.getR22_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row23
// Column B
row = sheet.getRow(22);

cellB = row.createCell(1);
if (record.getR23_recoveries() != null) {
cellB.setCellValue(record.getR23_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row23
// Column C
cellC = row.createCell(2);
if (record.getR23_write_offs() != null) {
cellC.setCellValue(record.getR23_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row23
// Column D
cellD = row.createCell(3);
if (record.getR23_new_chars() != null) {
cellD.setCellValue(record.getR23_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row24
// Column B
row = sheet.getRow(23);

cellB = row.createCell(1);
if (record.getR24_recoveries() != null) {
cellB.setCellValue(record.getR24_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row24
// Column C
cellC = row.createCell(2);
if (record.getR24_write_offs() != null) {
cellC.setCellValue(record.getR24_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row24
// Column D
cellD = row.createCell(3);
if (record.getR24_new_chars() != null) {
cellD.setCellValue(record.getR24_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row25
// Column B
row = sheet.getRow(24);

cellB = row.createCell(1);
if (record.getR25_recoveries() != null) {
cellB.setCellValue(record.getR25_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row25
// Column C
cellC = row.createCell(2);
if (record.getR25_write_offs() != null) {
cellC.setCellValue(record.getR25_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row25
// Column D
cellD = row.createCell(3);
if (record.getR25_new_chars() != null) {
cellD.setCellValue(record.getR25_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row26
// Column B
row = sheet.getRow(25);

cellB = row.createCell(1);
if (record.getR26_recoveries() != null) {
cellB.setCellValue(record.getR26_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row26
// Column C
cellC = row.createCell(2);
if (record.getR26_write_offs() != null) {
cellC.setCellValue(record.getR26_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row26
// Column D
cellD = row.createCell(3);
if (record.getR26_new_chars() != null) {
cellD.setCellValue(record.getR26_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row27
// Column B
row = sheet.getRow(26);

cellB = row.createCell(1);
if (record.getR27_recoveries() != null) {
cellB.setCellValue(record.getR27_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row27
// Column C
cellC = row.createCell(2);
if (record.getR27_write_offs() != null) {
cellC.setCellValue(record.getR27_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row27
// Column D
cellD = row.createCell(3);
if (record.getR27_new_chars() != null) {
cellD.setCellValue(record.getR27_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row28
// Column B
row = sheet.getRow(27);

cellB = row.createCell(1);
if (record.getR28_recoveries() != null) {
cellB.setCellValue(record.getR28_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row28
// Column C
cellC = row.createCell(2);
if (record.getR28_write_offs() != null) {
cellC.setCellValue(record.getR28_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row28
// Column D
cellD = row.createCell(3);
if (record.getR28_new_chars() != null) {
cellD.setCellValue(record.getR28_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


// row30
// Column B
row = sheet.getRow(29);

cellB = row.createCell(1);
if (record.getR30_recoveries() != null) {
cellB.setCellValue(record.getR30_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row30
// Column C
cellC = row.createCell(2);
if (record.getR30_write_offs() != null) {
cellC.setCellValue(record.getR30_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row30
// Column D
cellD = row.createCell(3);
if (record.getR30_new_chars() != null) {
cellD.setCellValue(record.getR30_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row31
// Column B
row = sheet.getRow(30);

cellB = row.createCell(1);
if (record.getR31_recoveries() != null) {
cellB.setCellValue(record.getR31_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row31
// Column C
cellC = row.createCell(2);
if (record.getR31_write_offs() != null) {
cellC.setCellValue(record.getR31_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row31
// Column D
cellD = row.createCell(3);
if (record.getR31_new_chars() != null) {
cellD.setCellValue(record.getR31_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row32
// Column B
row = sheet.getRow(31);

cellB = row.createCell(1);
if (record.getR32_recoveries() != null) {
cellB.setCellValue(record.getR32_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row32
// Column C
cellC = row.createCell(2);
if (record.getR32_write_offs() != null) {
cellC.setCellValue(record.getR32_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row32
// Column D
cellD = row.createCell(3);
if (record.getR32_new_chars() != null) {
cellD.setCellValue(record.getR32_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row33
// Column B
row = sheet.getRow(32);

cellB = row.createCell(1);
if (record.getR33_recoveries() != null) {
cellB.setCellValue(record.getR33_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row33
// Column C
cellC = row.createCell(2);
if (record.getR33_write_offs() != null) {
cellC.setCellValue(record.getR33_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row33
// Column D
cellD = row.createCell(3);
if (record.getR33_new_chars() != null) {
cellD.setCellValue(record.getR33_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row34
// Column B
row = sheet.getRow(33);

cellB = row.createCell(1);
if (record.getR34_recoveries() != null) {
cellB.setCellValue(record.getR34_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row34
// Column C
cellC = row.createCell(2);
if (record.getR34_write_offs() != null) {
cellC.setCellValue(record.getR34_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row34
// Column D
cellD = row.createCell(3);
if (record.getR34_new_chars() != null) {
cellD.setCellValue(record.getR34_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row35
// Column B
row = sheet.getRow(34);

cellB = row.createCell(1);
if (record.getR35_recoveries() != null) {
cellB.setCellValue(record.getR35_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row35
// Column C
cellC = row.createCell(2);
if (record.getR35_write_offs() != null) {
cellC.setCellValue(record.getR35_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row35
// Column D
cellD = row.createCell(3);
if (record.getR35_new_chars() != null) {
cellD.setCellValue(record.getR35_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row36
// Column B
row = sheet.getRow(35);

cellB = row.createCell(1);
if (record.getR36_recoveries() != null) {
cellB.setCellValue(record.getR36_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row36
// Column C
cellC = row.createCell(2);
if (record.getR36_write_offs() != null) {
cellC.setCellValue(record.getR36_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row36
// Column D
cellD = row.createCell(3);
if (record.getR36_new_chars() != null) {
cellD.setCellValue(record.getR36_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row37
// Column B
row = sheet.getRow(36);

cellB = row.createCell(1);
if (record.getR37_recoveries() != null) {
cellB.setCellValue(record.getR37_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row37
// Column C
cellC = row.createCell(2);
if (record.getR37_write_offs() != null) {
cellC.setCellValue(record.getR37_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row37
// Column D
cellD = row.createCell(3);
if (record.getR37_new_chars() != null) {
cellD.setCellValue(record.getR37_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


// row39
// Column B
row = sheet.getRow(38);

cellB = row.createCell(1);
if (record.getR39_recoveries() != null) {
cellB.setCellValue(record.getR39_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row39
// Column C
cellC = row.createCell(2);
if (record.getR39_write_offs() != null) {
cellC.setCellValue(record.getR39_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row39
// Column D
cellD = row.createCell(3);
if (record.getR39_new_chars() != null) {
cellD.setCellValue(record.getR39_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row40
// Column B
row = sheet.getRow(39);

cellB = row.createCell(1);
if (record.getR40_recoveries() != null) {
cellB.setCellValue(record.getR40_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row40
// Column C
cellC = row.createCell(2);
if (record.getR40_write_offs() != null) {
cellC.setCellValue(record.getR40_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row40
// Column D
cellD = row.createCell(3);
if (record.getR40_new_chars() != null) {
cellD.setCellValue(record.getR40_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row42
// Column B
row = sheet.getRow(41);

cellB = row.createCell(1);
if (record.getR42_recoveries() != null) {
cellB.setCellValue(record.getR42_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row42
// Column C
cellC = row.createCell(2);
if (record.getR42_write_offs() != null) {
cellC.setCellValue(record.getR42_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row42
// Column D
cellD = row.createCell(3);
if (record.getR42_new_chars() != null) {
cellD.setCellValue(record.getR42_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row43
// Column B
row = sheet.getRow(42);

cellB = row.createCell(1);
if (record.getR43_recoveries() != null) {
cellB.setCellValue(record.getR43_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row43
// Column C
cellC = row.createCell(2);
if (record.getR43_write_offs() != null) {
cellC.setCellValue(record.getR43_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row43
// Column D
cellD = row.createCell(3);
if (record.getR43_new_chars() != null) {
cellD.setCellValue(record.getR43_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row44
// Column B
row = sheet.getRow(43);

cellB = row.createCell(1);
if (record.getR44_recoveries() != null) {
cellB.setCellValue(record.getR44_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row44
// Column C
cellC = row.createCell(2);
if (record.getR44_write_offs() != null) {
cellC.setCellValue(record.getR44_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row44
// Column D
cellD = row.createCell(3);
if (record.getR44_new_chars() != null) {
cellD.setCellValue(record.getR44_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row45
// Column B
row = sheet.getRow(44);

cellB = row.createCell(1);
if (record.getR45_recoveries() != null) {
cellB.setCellValue(record.getR45_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row45
// Column C
cellC = row.createCell(2);
if (record.getR45_write_offs() != null) {
cellC.setCellValue(record.getR45_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row45
// Column D
cellD = row.createCell(3);
if (record.getR45_new_chars() != null) {
cellD.setCellValue(record.getR45_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row46
// Column B
row = sheet.getRow(45);

cellB = row.createCell(1);
if (record.getR46_recoveries() != null) {
cellB.setCellValue(record.getR46_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row46
// Column C
cellC = row.createCell(2);
if (record.getR46_write_offs() != null) {
cellC.setCellValue(record.getR46_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row46
// Column D
cellD = row.createCell(3);
if (record.getR46_new_chars() != null) {
cellD.setCellValue(record.getR46_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row47
// Column B
row = sheet.getRow(46);

cellB = row.createCell(1);
if (record.getR47_recoveries() != null) {
cellB.setCellValue(record.getR47_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row47
// Column C
cellC = row.createCell(2);
if (record.getR47_write_offs() != null) {
cellC.setCellValue(record.getR47_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row47
// Column D
cellD = row.createCell(3);
if (record.getR47_new_chars() != null) {
cellD.setCellValue(record.getR47_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row48
// Column B
row = sheet.getRow(47);

cellB = row.createCell(1);
if (record.getR48_recoveries() != null) {
cellB.setCellValue(record.getR48_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row48
// Column C
cellC = row.createCell(2);
if (record.getR48_write_offs() != null) {
cellC.setCellValue(record.getR48_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row48
// Column D
cellD = row.createCell(3);
if (record.getR48_new_chars() != null) {
cellD.setCellValue(record.getR48_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row50
// Column B
row = sheet.getRow(49);

cellB = row.createCell(1);
if (record.getR50_recoveries() != null) {
cellB.setCellValue(record.getR50_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row50
// Column C
cellC = row.createCell(2);
if (record.getR50_write_offs() != null) {
cellC.setCellValue(record.getR50_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row50
// Column D
cellD = row.createCell(3);
if (record.getR50_new_chars() != null) {
cellD.setCellValue(record.getR50_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row51
// Column B
row = sheet.getRow(50);

cellB = row.createCell(1);
if (record.getR51_recoveries() != null) {
cellB.setCellValue(record.getR51_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row51
// Column C
cellC = row.createCell(2);
if (record.getR51_write_offs() != null) {
cellC.setCellValue(record.getR51_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row51
// Column D
cellD = row.createCell(3);
if (record.getR51_new_chars() != null) {
cellD.setCellValue(record.getR51_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row52
// Column B
row = sheet.getRow(51);

cellB = row.createCell(1);
if (record.getR52_recoveries() != null) {
cellB.setCellValue(record.getR52_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row52
// Column C
cellC = row.createCell(2);
if (record.getR52_write_offs() != null) {
cellC.setCellValue(record.getR52_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row52
// Column D
cellD = row.createCell(3);
if (record.getR52_new_chars() != null) {
cellD.setCellValue(record.getR52_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row54
// Column B
row = sheet.getRow(53);

cellB = row.createCell(1);
if (record.getR54_recoveries() != null) {
cellB.setCellValue(record.getR54_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row54
// Column C
cellC = row.createCell(2);
if (record.getR54_write_offs() != null) {
cellC.setCellValue(record.getR54_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row54
// Column D
cellD = row.createCell(3);
if (record.getR54_new_chars() != null) {
cellD.setCellValue(record.getR54_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row55
// Column B
row = sheet.getRow(54);

cellB = row.createCell(1);
if (record.getR55_recoveries() != null) {
cellB.setCellValue(record.getR55_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row55
// Column C
cellC = row.createCell(2);
if (record.getR55_write_offs() != null) {
cellC.setCellValue(record.getR55_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row55
// Column D
cellD = row.createCell(3);
if (record.getR55_new_chars() != null) {
cellD.setCellValue(record.getR55_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row56
// Column B
row = sheet.getRow(55);

cellB = row.createCell(1);
if (record.getR56_recoveries() != null) {
cellB.setCellValue(record.getR56_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row56
// Column C
cellC = row.createCell(2);
if (record.getR56_write_offs() != null) {
cellC.setCellValue(record.getR56_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row56
// Column D
cellD = row.createCell(3);
if (record.getR56_new_chars() != null) {
cellD.setCellValue(record.getR56_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


// row58
// Column B
row = sheet.getRow(57);

cellB = row.createCell(1);
if (record.getR58_recoveries() != null) {
cellB.setCellValue(record.getR58_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row58
// Column C
cellC = row.createCell(2);
if (record.getR58_write_offs() != null) {
cellC.setCellValue(record.getR58_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row58
// Column D
cellD = row.createCell(3);
if (record.getR58_new_chars() != null) {
cellD.setCellValue(record.getR58_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row59
// Column B
row = sheet.getRow(58);

cellB = row.createCell(1);
if (record.getR59_recoveries() != null) {
cellB.setCellValue(record.getR59_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row59
// Column C
cellC = row.createCell(2);
if (record.getR59_write_offs() != null) {
cellC.setCellValue(record.getR59_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row59
// Column D
cellD = row.createCell(3);
if (record.getR59_new_chars() != null) {
cellD.setCellValue(record.getR59_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row60
// Column B
row = sheet.getRow(59);

cellB = row.createCell(1);
if (record.getR60_recoveries() != null) {
cellB.setCellValue(record.getR60_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row60
// Column C
cellC = row.createCell(2);
if (record.getR60_write_offs() != null) {
cellC.setCellValue(record.getR60_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row60
// Column D
cellD = row.createCell(3);
if (record.getR60_new_chars() != null) {
cellD.setCellValue(record.getR60_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row61
// Column B
row = sheet.getRow(60);

cellB = row.createCell(1);
if (record.getR61_recoveries() != null) {
cellB.setCellValue(record.getR61_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row61
// Column C
cellC = row.createCell(2);
if (record.getR61_write_offs() != null) {
cellC.setCellValue(record.getR61_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row61
// Column D
cellD = row.createCell(3);
if (record.getR61_new_chars() != null) {
cellD.setCellValue(record.getR61_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row62
// Column B
row = sheet.getRow(61);

cellB = row.createCell(1);
if (record.getR62_recoveries() != null) {
cellB.setCellValue(record.getR62_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row62
// Column C
cellC = row.createCell(2);
if (record.getR62_write_offs() != null) {
cellC.setCellValue(record.getR62_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row62
// Column D
cellD = row.createCell(3);
if (record.getR62_new_chars() != null) {
cellD.setCellValue(record.getR62_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row63
// Column B
row = sheet.getRow(62);

cellB = row.createCell(1);
if (record.getR63_recoveries() != null) {
cellB.setCellValue(record.getR63_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row63
// Column C
cellC = row.createCell(2);
if (record.getR63_write_offs() != null) {
cellC.setCellValue(record.getR63_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row63
// Column D
cellD = row.createCell(3);
if (record.getR63_new_chars() != null) {
cellD.setCellValue(record.getR63_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row67
// Column E

row = sheet.getRow(66);

cellE = row.createCell(4);
if (record.getR67_total() != null) {
cellE.setCellValue(record.getR67_total().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}

// row68
// Column E

row = sheet.getRow(67);

cellE = row.createCell(4);
if (record.getR68_total() != null) {
cellE.setCellValue(record.getR68_total().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}


}

workbook.setForceFormulaRecalculation(true);
} else {

}

//Write the final workbook content to the in-memory stream.
workbook.write(out);

logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

//audit service archival summary format

ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_I_S_CA ARCHIVAL SUMMARY", null, "BRRS_M_I_S_CA_ARCHIVALTABLE_SUMMARY");
					}

return out.toByteArray();
}

}

// Archival Email Excel
public byte[] BRRS_M_I_S_CAARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
String currency, String dtltype, String type, BigDecimal version) throws Exception {

logger.info("Service: Starting Archival Email Excel generation process in memory.");

List<M_I_S_CA_Arcihval_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);

if (dataList.isEmpty()) {
logger.warn("Service: No data found for BRRS_M_I_S_CA report. Returning empty result.");
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
	M_I_S_CA_Arcihval_Summary_Entity record = dataList.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}


//row7
//Column B
Cell cellBdate = row.createCell(1);
if (record.getReport_date() != null) {
	cellBdate.setCellValue(record.getReport_date());
	cellBdate.setCellStyle(dateStyle);
} else {
	cellBdate.setCellValue("");
	cellBdate.setCellStyle(textStyle);
}



//ROW 10
row = sheet.getRow(9);

//EMAIL EXCEL 

//int startRow = 9;


//ROW10  Balance at the beginning of period


// row10
// Column E
Cell cellE = row.createCell(4);
if (record.getR10_total() != null) {
cellE.setCellValue(record.getR10_total().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}



//ROW11  Non-financial institutional units (sum of lines (i) to (vii))

//ROW12  Central Government

// row12
// Column B

row = sheet.getRow(11);

Cell cellB = row.createCell(1);
if (record.getR12_recoveries() != null) {
cellB.setCellValue(record.getR12_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row12
// Column C
Cell cellC = row.createCell(2);
if (record.getR12_write_offs() != null) {
cellC.setCellValue(record.getR12_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row12
// Column D
Cell cellD = row.createCell(3);
if (record.getR12_new_chars() != null) {
cellD.setCellValue(record.getR12_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW13  Local Government

// row13
// Column B

row = sheet.getRow(12);

cellB = row.createCell(1);
if (record.getR13_recoveries() != null) {
cellB.setCellValue(record.getR13_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row13
// Column C
cellC = row.createCell(2);
if (record.getR13_write_offs() != null) {
cellC.setCellValue(record.getR13_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row13
// Column D
cellD = row.createCell(3);
if (record.getR13_new_chars() != null) {
cellD.setCellValue(record.getR13_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW14  Public Non-Financial Corporations

// row14
// Column B
row = sheet.getRow(13);

cellB = row.createCell(1);
if (record.getR14_recoveries() != null) {
cellB.setCellValue(record.getR14_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row14
// Column C
cellC = row.createCell(2);
if (record.getR14_write_offs() != null) {
cellC.setCellValue(record.getR14_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row14
// Column D
cellD = row.createCell(3);
if (record.getR14_new_chars() != null) {
cellD.setCellValue(record.getR14_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




//ROW15  Other Non-Financial Corporations (Private business enterprises)

//ROW16  Agriculture, Forestry, Fishing

// row16
// Column B
row = sheet.getRow(15);

cellB = row.createCell(1);
if (record.getR16_recoveries() != null) {
cellB.setCellValue(record.getR16_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row16
// Column C
cellC = row.createCell(2);
if (record.getR16_write_offs() != null) {
cellC.setCellValue(record.getR16_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row16
// Column D
cellD = row.createCell(3);
if (record.getR16_new_chars() != null) {
cellD.setCellValue(record.getR16_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW17  Mining and Quarying

// row17
// Column B
row = sheet.getRow(16);

cellB = row.createCell(1);
if (record.getR17_recoveries() != null) {
cellB.setCellValue(record.getR17_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row17
// Column C
cellC = row.createCell(2);
if (record.getR17_write_offs() != null) {
cellC.setCellValue(record.getR17_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row17
// Column D
cellD = row.createCell(3);
if (record.getR17_new_chars() != null) {
cellD.setCellValue(record.getR17_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW18  Manufacturing

// row18
// Column B
row = sheet.getRow(17);

cellB = row.createCell(1);
if (record.getR18_recoveries() != null) {
cellB.setCellValue(record.getR18_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row18
// Column C
cellC = row.createCell(2);
if (record.getR18_write_offs() != null) {
cellC.setCellValue(record.getR18_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row18
// Column D
cellD = row.createCell(3);
if (record.getR18_new_chars() != null) {
cellD.setCellValue(record.getR18_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW19  Construction

// row19
// Column B
row = sheet.getRow(18);

cellB = row.createCell(1);
if (record.getR19_recoveries() != null) {
cellB.setCellValue(record.getR19_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row19
// Column C
cellC = row.createCell(2);
if (record.getR19_write_offs() != null) {
cellC.setCellValue(record.getR19_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row19
// Column D
cellD = row.createCell(3);
if (record.getR19_new_chars() != null) {
cellD.setCellValue(record.getR19_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW20  Commercial real estate

// row20
// Column B
row = sheet.getRow(19);

cellB = row.createCell(1);
if (record.getR20_recoveries() != null) {
cellB.setCellValue(record.getR20_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row20
// Column C
cellC = row.createCell(2);
if (record.getR20_write_offs() != null) {
cellC.setCellValue(record.getR20_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row20
// Column D
cellD = row.createCell(3);
if (record.getR20_new_chars() != null) {
cellD.setCellValue(record.getR20_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW21  Electricity

// row21
// Column B
row = sheet.getRow(20);

cellB = row.createCell(1);
if (record.getR21_recoveries() != null) {
cellB.setCellValue(record.getR21_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row21
// Column C
cellC = row.createCell(2);
if (record.getR21_write_offs() != null) {
cellC.setCellValue(record.getR21_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row21
// Column D
cellD = row.createCell(3);
if (record.getR21_new_chars() != null) {
cellD.setCellValue(record.getR21_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW22  Water

// row22
// Column B
row = sheet.getRow(21);

cellB = row.createCell(1);
if (record.getR22_recoveries() != null) {
cellB.setCellValue(record.getR22_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row22
// Column C
cellC = row.createCell(2);
if (record.getR22_write_offs() != null) {
cellC.setCellValue(record.getR22_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row22
// Column D
cellD = row.createCell(3);
if (record.getR22_new_chars() != null) {
cellD.setCellValue(record.getR22_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW23  Telecommunication and Post

// row23
// Column B
row = sheet.getRow(22);

cellB = row.createCell(1);
if (record.getR23_recoveries() != null) {
cellB.setCellValue(record.getR23_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row23
// Column C
cellC = row.createCell(2);
if (record.getR23_write_offs() != null) {
cellC.setCellValue(record.getR23_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row23
// Column D
cellD = row.createCell(3);
if (record.getR23_new_chars() != null) {
cellD.setCellValue(record.getR23_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW24  Tourism and hotels

// row24
// Column B
row = sheet.getRow(23);

cellB = row.createCell(1);
if (record.getR24_recoveries() != null) {
cellB.setCellValue(record.getR24_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row24
// Column C
cellC = row.createCell(2);
if (record.getR24_write_offs() != null) {
cellC.setCellValue(record.getR24_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row24
// Column D
cellD = row.createCell(3);
if (record.getR24_new_chars() != null) {
cellD.setCellValue(record.getR24_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row24
// Column E

cellE = row.createCell(4);
if (record.getR24_total() != null) {
cellE.setCellValue(record.getR24_total().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}

//ROW25  Transport and Storage

// row25
// Column B
row = sheet.getRow(24);

cellB = row.createCell(1);
if (record.getR25_recoveries() != null) {
cellB.setCellValue(record.getR25_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row25
// Column C
cellC = row.createCell(2);
if (record.getR25_write_offs() != null) {
cellC.setCellValue(record.getR25_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row25
// Column D
cellD = row.createCell(3);
if (record.getR25_new_chars() != null) {
cellD.setCellValue(record.getR25_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW26  Trade, restaurants and bars

// row26
// Column B
row = sheet.getRow(25);

cellB = row.createCell(1);
if (record.getR26_recoveries() != null) {
cellB.setCellValue(record.getR26_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row26
// Column C
cellC = row.createCell(2);
if (record.getR26_write_offs() != null) {
cellC.setCellValue(record.getR26_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row26
// Column D
cellD = row.createCell(3);
if (record.getR26_new_chars() != null) {
cellD.setCellValue(record.getR26_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW27  Business Services

// row27
// Column B
row = sheet.getRow(26);

cellB = row.createCell(1);
if (record.getR27_recoveries() != null) {
cellB.setCellValue(record.getR27_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row27
// Column C
cellC = row.createCell(2);
if (record.getR27_write_offs() != null) {
cellC.setCellValue(record.getR27_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row27
// Column D
cellD = row.createCell(3);
if (record.getR27_new_chars() != null) {
cellD.setCellValue(record.getR27_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW28  Other Community, Social and Personal Services

// row28
// Column B
row = sheet.getRow(27);

cellB = row.createCell(1);
if (record.getR28_recoveries() != null) {
cellB.setCellValue(record.getR28_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row28
// Column C
cellC = row.createCell(2);
if (record.getR28_write_offs() != null) {
cellC.setCellValue(record.getR28_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row28
// Column D
cellD = row.createCell(3);
if (record.getR28_new_chars() != null) {
cellD.setCellValue(record.getR28_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW29  Households

//ROW30  Residential property (owner occupied)

// row30
// Column B
row = sheet.getRow(29);

cellB = row.createCell(1);
if (record.getR30_recoveries() != null) {
cellB.setCellValue(record.getR30_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row30
// Column C
cellC = row.createCell(2);
if (record.getR30_write_offs() != null) {
cellC.setCellValue(record.getR30_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row30
// Column D
cellD = row.createCell(3);
if (record.getR30_new_chars() != null) {
cellD.setCellValue(record.getR30_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW31  Residential property (rented)

// row31
// Column B
row = sheet.getRow(30);

cellB = row.createCell(1);
if (record.getR31_recoveries() != null) {
cellB.setCellValue(record.getR31_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row31
// Column C
cellC = row.createCell(2);
if (record.getR31_write_offs() != null) {
cellC.setCellValue(record.getR31_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row31
// Column D
cellD = row.createCell(3);
if (record.getR31_new_chars() != null) {
cellD.setCellValue(record.getR31_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW32  Personal Loans

// row32
// Column B
row = sheet.getRow(31);

cellB = row.createCell(1);
if (record.getR32_recoveries() != null) {
cellB.setCellValue(record.getR32_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row32
// Column C
cellC = row.createCell(2);
if (record.getR32_write_offs() != null) {
cellC.setCellValue(record.getR32_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row32
// Column D
cellD = row.createCell(3);
if (record.getR32_new_chars() != null) {
cellD.setCellValue(record.getR32_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




//ROW33  Motor vehicle

// row33
// Column B
row = sheet.getRow(32);

cellB = row.createCell(1);
if (record.getR33_recoveries() != null) {
cellB.setCellValue(record.getR33_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}


// row33
// Column C
cellC = row.createCell(2);
if (record.getR33_write_offs() != null) {
cellC.setCellValue(record.getR33_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row33
// Column D
cellD = row.createCell(3);
if (record.getR33_new_chars() != null) {
cellD.setCellValue(record.getR33_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW34  Household goods

// row34
// Column B
row = sheet.getRow(33);

cellB = row.createCell(1);
if (record.getR34_recoveries() != null) {
cellB.setCellValue(record.getR34_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row34
// Column C
cellC = row.createCell(2);
if (record.getR34_write_offs() != null) {
cellC.setCellValue(record.getR34_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row34
// Column D
cellD = row.createCell(3);
if (record.getR34_new_chars() != null) {
cellD.setCellValue(record.getR34_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW35  Credit card loans

// row35
// Column B
row = sheet.getRow(34);

cellB = row.createCell(1);
if (record.getR35_recoveries() != null) {
cellB.setCellValue(record.getR35_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row35
// Column C
cellC = row.createCell(2);
if (record.getR35_write_offs() != null) {
cellC.setCellValue(record.getR35_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row35
// Column D
cellD = row.createCell(3);
if (record.getR35_new_chars() != null) {
cellD.setCellValue(record.getR35_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




//ROW36  Non-Profit Institutions Serving Households

// row36
// Column B
row = sheet.getRow(35);

cellB = row.createCell(1);
if (record.getR36_recoveries() != null) {
cellB.setCellValue(record.getR36_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row36
// Column C
cellC = row.createCell(2);
if (record.getR36_write_offs() != null) {
cellC.setCellValue(record.getR36_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row36
// Column D
cellD = row.createCell(3);
if (record.getR36_new_chars() != null) {
cellD.setCellValue(record.getR36_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW37  Other

// row37
// Column B
row = sheet.getRow(36);

cellB = row.createCell(1);
if (record.getR37_recoveries() != null) {
cellB.setCellValue(record.getR37_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row37
// Column C
cellC = row.createCell(2);
if (record.getR37_write_offs() != null) {
cellC.setCellValue(record.getR37_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row37
// Column D
cellD = row.createCell(3);
if (record.getR37_new_chars() != null) {
cellD.setCellValue(record.getR37_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW38  Non-Residents

// row38
// Column B
row = sheet.getRow(37);

cellB = row.createCell(1);
if (record.getR38_recoveries() != null) {
cellB.setCellValue(record.getR38_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}


// row38
// Column D
cellD = row.createCell(3);
if (record.getR38_new_chars() != null) {
cellD.setCellValue(record.getR38_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW39  Other Non-Financial Corporations

// row39
// Column B
row = sheet.getRow(38);

cellB = row.createCell(1);
if (record.getR39_recoveries() != null) {
cellB.setCellValue(record.getR39_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row39
// Column C
cellC = row.createCell(2);
if (record.getR39_write_offs() != null) {
cellC.setCellValue(record.getR39_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row39
// Column D
cellD = row.createCell(3);
if (record.getR39_new_chars() != null) {
cellD.setCellValue(record.getR39_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW40  Households


// row40
// Column B
row = sheet.getRow(39);

cellB = row.createCell(1);
if (record.getR40_recoveries() != null) {
cellB.setCellValue(record.getR40_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row40
// Column C
cellC = row.createCell(2);
if (record.getR40_write_offs() != null) {
cellC.setCellValue(record.getR40_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row40
// Column D
cellD = row.createCell(3);
if (record.getR40_new_chars() != null) {
cellD.setCellValue(record.getR40_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW41  Financial institutional units

// row41
// Column B
row = sheet.getRow(40);

cellB = row.createCell(1);
if (record.getR41_recoveries() != null) {
cellB.setCellValue(record.getR41_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}


// row41
// Column D
cellD = row.createCell(3);
if (record.getR41_new_chars() != null) {
cellD.setCellValue(record.getR41_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW42  Central Bank

// row42
// Column B
row = sheet.getRow(41);

cellB = row.createCell(1);
if (record.getR42_recoveries() != null) {
cellB.setCellValue(record.getR42_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row42
// Column C
cellC = row.createCell(2);
if (record.getR42_write_offs() != null) {
cellC.setCellValue(record.getR42_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row42
// Column D
cellD = row.createCell(3);
if (record.getR42_new_chars() != null) {
cellD.setCellValue(record.getR42_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW43  Commercial Banks

// row43
// Column B
row = sheet.getRow(42);

cellB = row.createCell(1);
if (record.getR43_recoveries() != null) {
cellB.setCellValue(record.getR43_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row43
// Column C
cellC = row.createCell(2);
if (record.getR43_write_offs() != null) {
cellC.setCellValue(record.getR43_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row43
// Column D
cellD = row.createCell(3);
if (record.getR43_new_chars() != null) {
cellD.setCellValue(record.getR43_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW44  Other Depository Corporations

// row44
// Column B
row = sheet.getRow(43);

cellB = row.createCell(1);
if (record.getR44_recoveries() != null) {
cellB.setCellValue(record.getR44_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row44
// Column C
cellC = row.createCell(2);
if (record.getR44_write_offs() != null) {
cellC.setCellValue(record.getR44_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row44
// Column D
cellD = row.createCell(3);
if (record.getR44_new_chars() != null) {
cellD.setCellValue(record.getR44_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW45  Botswana Savings Bank (BSB)

// row45
// Column B
row = sheet.getRow(44);

cellB = row.createCell(1);
if (record.getR45_recoveries() != null) {
cellB.setCellValue(record.getR45_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row45
// Column C
cellC = row.createCell(2);
if (record.getR45_write_offs() != null) {
cellC.setCellValue(record.getR45_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row45
// Column D
cellD = row.createCell(3);
if (record.getR45_new_chars() != null) {
cellD.setCellValue(record.getR45_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW46  Botswana Building Society (BBS)

// row46
// Column B
row = sheet.getRow(45);

cellB = row.createCell(1);
if (record.getR46_recoveries() != null) {
cellB.setCellValue(record.getR46_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row46
// Column C
cellC = row.createCell(2);
if (record.getR46_write_offs() != null) {
cellC.setCellValue(record.getR46_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row46
// Column D
cellD = row.createCell(3);
if (record.getR46_new_chars() != null) {
cellD.setCellValue(record.getR46_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW47  Domestic Money Market Unit Trust

// row47
// Column B
row = sheet.getRow(46);

cellB = row.createCell(1);
if (record.getR47_recoveries() != null) {
cellB.setCellValue(record.getR47_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row47
// Column C
cellC = row.createCell(2);
if (record.getR47_write_offs() != null) {
cellC.setCellValue(record.getR47_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row47
// Column D
cellD = row.createCell(3);
if (record.getR47_new_chars() != null) {
cellD.setCellValue(record.getR47_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW48  Other (specify)

// row48
// Column B
row = sheet.getRow(47);

cellB = row.createCell(1);
if (record.getR48_recoveries() != null) {
cellB.setCellValue(record.getR48_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row48
// Column C
cellC = row.createCell(2);
if (record.getR48_write_offs() != null) {
cellC.setCellValue(record.getR48_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row48
// Column D
cellD = row.createCell(3);
if (record.getR48_new_chars() != null) {
cellD.setCellValue(record.getR48_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW49  Other Financial Corporations

// row49
// Column B
row = sheet.getRow(48);

cellB = row.createCell(1);
if (record.getR49_recoveries() != null) {
cellB.setCellValue(record.getR49_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}


// row49
// Column D
cellD = row.createCell(3);
if (record.getR49_new_chars() != null) {
cellD.setCellValue(record.getR49_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW50  Insurance Companies

// row50
// Column B
row = sheet.getRow(49);

cellB = row.createCell(1);
if (record.getR50_recoveries() != null) {
cellB.setCellValue(record.getR50_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row50
// Column C
cellC = row.createCell(2);
if (record.getR50_write_offs() != null) {
cellC.setCellValue(record.getR50_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row50
// Column D
cellD = row.createCell(3);
if (record.getR50_new_chars() != null) {
cellD.setCellValue(record.getR50_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW51  Pension Funds

// row51
// Column B
row = sheet.getRow(50);

cellB = row.createCell(1);
if (record.getR51_recoveries() != null) {
cellB.setCellValue(record.getR51_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row51
// Column C
cellC = row.createCell(2);
if (record.getR51_write_offs() != null) {
cellC.setCellValue(record.getR51_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row51
// Column D
cellD = row.createCell(3);
if (record.getR51_new_chars() != null) {
cellD.setCellValue(record.getR51_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW52  SACCOs

// row52
// Column B
row = sheet.getRow(51);

cellB = row.createCell(1);
if (record.getR52_recoveries() != null) {
cellB.setCellValue(record.getR52_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row52
// Column C
cellC = row.createCell(2);
if (record.getR52_write_offs() != null) {
cellC.setCellValue(record.getR52_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row52
// Column D
cellD = row.createCell(3);
if (record.getR52_new_chars() != null) {
cellD.setCellValue(record.getR52_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW53  Other Financial Intermediaries

// row53
// Column B
row = sheet.getRow(52);

cellB = row.createCell(1);
if (record.getR53_recoveries() != null) {
cellB.setCellValue(record.getR53_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}


// row53
// Column D
cellD = row.createCell(3);
if (record.getR53_new_chars() != null) {
cellD.setCellValue(record.getR53_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW54  Finance companies

// row54
// Column B
row = sheet.getRow(53);

cellB = row.createCell(1);
if (record.getR54_recoveries() != null) {
cellB.setCellValue(record.getR54_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row54
// Column C
cellC = row.createCell(2);
if (record.getR54_write_offs() != null) {
cellC.setCellValue(record.getR54_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row54
// Column D
cellD = row.createCell(3);
if (record.getR54_new_chars() != null) {
cellD.setCellValue(record.getR54_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW55  Medical Aid Schemes

// row55
// Column B
row = sheet.getRow(54);

cellB = row.createCell(1);
if (record.getR55_recoveries() != null) {
cellB.setCellValue(record.getR55_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row55
// Column C
cellC = row.createCell(2);
if (record.getR55_write_offs() != null) {
cellC.setCellValue(record.getR55_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row55
// Column D
cellD = row.createCell(3);
if (record.getR55_new_chars() != null) {
cellD.setCellValue(record.getR55_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW56  Public sector financial intermediaries

// row56
// Column B
row = sheet.getRow(55);

cellB = row.createCell(1);
if (record.getR56_recoveries() != null) {
cellB.setCellValue(record.getR56_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row56
// Column C
cellC = row.createCell(2);
if (record.getR56_write_offs() != null) {
cellC.setCellValue(record.getR56_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row56
// Column D
cellD = row.createCell(3);
if (record.getR56_new_chars() != null) {
cellD.setCellValue(record.getR56_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW57  Financial Auxiliaries

// row57
// Column B
row = sheet.getRow(56);

cellB = row.createCell(1);
if (record.getR57_recoveries() != null) {
cellB.setCellValue(record.getR57_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}


// row57
// Column D
cellD = row.createCell(3);
if (record.getR57_new_chars() != null) {
cellD.setCellValue(record.getR57_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW58  Insurance brokers


// row58
// Column B
row = sheet.getRow(57);

cellB = row.createCell(1);
if (record.getR58_recoveries() != null) {
cellB.setCellValue(record.getR58_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row58
// Column C
cellC = row.createCell(2);
if (record.getR58_write_offs() != null) {
cellC.setCellValue(record.getR58_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row58
// Column D
cellD = row.createCell(3);
if (record.getR58_new_chars() != null) {
cellD.setCellValue(record.getR58_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW59  fund administrators

// row59
// Column B
row = sheet.getRow(58);

cellB = row.createCell(1);
if (record.getR59_recoveries() != null) {
cellB.setCellValue(record.getR59_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row59
// Column C
cellC = row.createCell(2);
if (record.getR59_write_offs() != null) {
cellC.setCellValue(record.getR59_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row59
// Column D
cellD = row.createCell(3);
if (record.getR59_new_chars() != null) {
cellD.setCellValue(record.getR59_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW60  Bureau de change

// row60
// Column B
row = sheet.getRow(59);

cellB = row.createCell(1);
if (record.getR60_recoveries() != null) {
cellB.setCellValue(record.getR60_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row60
// Column C
cellC = row.createCell(2);
if (record.getR60_write_offs() != null) {
cellC.setCellValue(record.getR60_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row60
// Column D
cellD = row.createCell(3);
if (record.getR60_new_chars() != null) {
cellD.setCellValue(record.getR60_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW61  Asset managers

// row61
// Column B
row = sheet.getRow(60);

cellB = row.createCell(1);
if (record.getR61_recoveries() != null) {
cellB.setCellValue(record.getR61_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row61
// Column C
cellC = row.createCell(2);
if (record.getR61_write_offs() != null) {
cellC.setCellValue(record.getR61_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row61
// Column D
cellD = row.createCell(3);
if (record.getR61_new_chars() != null) {
cellD.setCellValue(record.getR61_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW62  Other (specify)

// row62
// Column B
row = sheet.getRow(61);

cellB = row.createCell(1);
if (record.getR62_recoveries() != null) {
cellB.setCellValue(record.getR62_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row62
// Column C
cellC = row.createCell(2);
if (record.getR62_write_offs() != null) {
cellC.setCellValue(record.getR62_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row62
// Column D
cellD = row.createCell(3);
if (record.getR62_new_chars() != null) {
cellD.setCellValue(record.getR62_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW63  Non-residents

// row63
// Column B
row = sheet.getRow(62);

cellB = row.createCell(1);
if (record.getR63_recoveries() != null) {
cellB.setCellValue(record.getR63_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row63
// Column C
cellC = row.createCell(2);
if (record.getR63_write_offs() != null) {
cellC.setCellValue(record.getR63_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row63
// Column D
cellD = row.createCell(3);
if (record.getR63_new_chars() != null) {
cellD.setCellValue(record.getR63_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW64  Total specific impairments
//ROW65  Specific impairments Balance at end of period (after adj. for recoveries and write-offs)
//ROW66  Collectively assessed (General) impairments
//ROW67  Balance at beginning of period

// row67
// Column E

row = sheet.getRow(66);

cellE = row.createCell(4);
if (record.getR67_total() != null) {
cellE.setCellValue(record.getR67_total().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}


//ROW68  Charge to P&L
//ROW69  Balance at end of period
//ROW70  TOTAL IMPAIRMENTS (LINES 5 AND 9)

}
workbook.setForceFormulaRecalculation(true);
} else {

}

// Write the final workbook content to the in-memory stream.
workbook.write(out);

logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

//audit service archival summary email


	ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_I_S_CA EMAIL ARCHIVAL SUMMARY", null, "BRRS_M_I_S_CA_ARCHIVALTABLE_SUMMARY");
					}

return out.toByteArray();
}
}

// Resub Format excel
public byte[] BRRS_M_I_S_CAResubExcel(String filename, String reportId, String fromdate, String todate,
String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

if ("email".equalsIgnoreCase(format) && version != null) {
logger.info("Service: Generating RESUB report for version {}", version);

try {
// ✅ Redirecting to Resub Excel
return BRRS_M_I_S_CAEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

} catch (ParseException e) {
logger.error("Invalid report date format: {}", fromdate, e);
throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
}
}

List<M_I_S_CA_RESUB_Summary_Entity> dataList = getResubSummaryByDateAndVersion(dateformat.parse(todate), version);

if (dataList.isEmpty()) {
logger.warn("Service: No data found for M_I_S_CA report. Returning empty result.");
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

M_I_S_CA_RESUB_Summary_Entity record = dataList.get(i);
System.out.println("rownumber=" + startRow + i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}


//row7
//Column B
Cell cellBdate = row.createCell(1);
if (record.getReport_date() != null) {
	cellBdate.setCellValue(record.getReport_date());
	cellBdate.setCellStyle(dateStyle);
} else {
	cellBdate.setCellValue("");
	cellBdate.setCellStyle(textStyle);
}



//ROW 10
row = sheet.getRow(9);
// row10
// Column E
Cell cellE = row.createCell(4);
if (record.getR10_total() != null) {
cellE.setCellValue(record.getR10_total().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}

// row12
// Column B

row = sheet.getRow(11);

Cell cellB = row.createCell(1);
if (record.getR12_recoveries() != null) {
cellB.setCellValue(record.getR12_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row12
// Column C
Cell cellC = row.createCell(2);
if (record.getR12_write_offs() != null) {
cellC.setCellValue(record.getR12_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row12
// Column D
Cell cellD = row.createCell(3);
if (record.getR12_new_chars() != null) {
cellD.setCellValue(record.getR12_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




// row13
// Column B

row = sheet.getRow(12);

cellB = row.createCell(1);
if (record.getR13_recoveries() != null) {
cellB.setCellValue(record.getR13_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row13
// Column C
cellC = row.createCell(2);
if (record.getR13_write_offs() != null) {
cellC.setCellValue(record.getR13_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row13
// Column D
cellD = row.createCell(3);
if (record.getR13_new_chars() != null) {
cellD.setCellValue(record.getR13_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row14
// Column B
row = sheet.getRow(13);

cellB = row.createCell(1);
if (record.getR14_recoveries() != null) {
cellB.setCellValue(record.getR14_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row14
// Column C
cellC = row.createCell(2);
if (record.getR14_write_offs() != null) {
cellC.setCellValue(record.getR14_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row14
// Column D
cellD = row.createCell(3);
if (record.getR14_new_chars() != null) {
cellD.setCellValue(record.getR14_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


// row16
// Column B
row = sheet.getRow(15);

cellB = row.createCell(1);
if (record.getR16_recoveries() != null) {
cellB.setCellValue(record.getR16_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row16
// Column C
cellC = row.createCell(2);
if (record.getR16_write_offs() != null) {
cellC.setCellValue(record.getR16_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row16
// Column D
cellD = row.createCell(3);
if (record.getR16_new_chars() != null) {
cellD.setCellValue(record.getR16_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row17
// Column B
row = sheet.getRow(16);

cellB = row.createCell(1);
if (record.getR17_recoveries() != null) {
cellB.setCellValue(record.getR17_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row17
// Column C
cellC = row.createCell(2);
if (record.getR17_write_offs() != null) {
cellC.setCellValue(record.getR17_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row17
// Column D
cellD = row.createCell(3);
if (record.getR17_new_chars() != null) {
cellD.setCellValue(record.getR17_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row18
// Column B
row = sheet.getRow(17);

cellB = row.createCell(1);
if (record.getR18_recoveries() != null) {
cellB.setCellValue(record.getR18_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row18
// Column C
cellC = row.createCell(2);
if (record.getR18_write_offs() != null) {
cellC.setCellValue(record.getR18_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row18
// Column D
cellD = row.createCell(3);
if (record.getR18_new_chars() != null) {
cellD.setCellValue(record.getR18_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row19
// Column B
row = sheet.getRow(18);

cellB = row.createCell(1);
if (record.getR19_recoveries() != null) {
cellB.setCellValue(record.getR19_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row19
// Column C
cellC = row.createCell(2);
if (record.getR19_write_offs() != null) {
cellC.setCellValue(record.getR19_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row19
// Column D
cellD = row.createCell(3);
if (record.getR19_new_chars() != null) {
cellD.setCellValue(record.getR19_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row20
// Column B
row = sheet.getRow(19);

cellB = row.createCell(1);
if (record.getR20_recoveries() != null) {
cellB.setCellValue(record.getR20_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row20
// Column C
cellC = row.createCell(2);
if (record.getR20_write_offs() != null) {
cellC.setCellValue(record.getR20_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row20
// Column D
cellD = row.createCell(3);
if (record.getR20_new_chars() != null) {
cellD.setCellValue(record.getR20_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row21
// Column B
row = sheet.getRow(20);

cellB = row.createCell(1);
if (record.getR21_recoveries() != null) {
cellB.setCellValue(record.getR21_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row21
// Column C
cellC = row.createCell(2);
if (record.getR21_write_offs() != null) {
cellC.setCellValue(record.getR21_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row21
// Column D
cellD = row.createCell(3);
if (record.getR21_new_chars() != null) {
cellD.setCellValue(record.getR21_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row22
// Column B
row = sheet.getRow(21);

cellB = row.createCell(1);
if (record.getR22_recoveries() != null) {
cellB.setCellValue(record.getR22_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row22
// Column C
cellC = row.createCell(2);
if (record.getR22_write_offs() != null) {
cellC.setCellValue(record.getR22_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row22
// Column D
cellD = row.createCell(3);
if (record.getR22_new_chars() != null) {
cellD.setCellValue(record.getR22_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row23
// Column B
row = sheet.getRow(22);

cellB = row.createCell(1);
if (record.getR23_recoveries() != null) {
cellB.setCellValue(record.getR23_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row23
// Column C
cellC = row.createCell(2);
if (record.getR23_write_offs() != null) {
cellC.setCellValue(record.getR23_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row23
// Column D
cellD = row.createCell(3);
if (record.getR23_new_chars() != null) {
cellD.setCellValue(record.getR23_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row24
// Column B
row = sheet.getRow(23);

cellB = row.createCell(1);
if (record.getR24_recoveries() != null) {
cellB.setCellValue(record.getR24_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row24
// Column C
cellC = row.createCell(2);
if (record.getR24_write_offs() != null) {
cellC.setCellValue(record.getR24_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row24
// Column D
cellD = row.createCell(3);
if (record.getR24_new_chars() != null) {
cellD.setCellValue(record.getR24_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row25
// Column B
row = sheet.getRow(24);

cellB = row.createCell(1);
if (record.getR25_recoveries() != null) {
cellB.setCellValue(record.getR25_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row25
// Column C
cellC = row.createCell(2);
if (record.getR25_write_offs() != null) {
cellC.setCellValue(record.getR25_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row25
// Column D
cellD = row.createCell(3);
if (record.getR25_new_chars() != null) {
cellD.setCellValue(record.getR25_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row26
// Column B
row = sheet.getRow(25);

cellB = row.createCell(1);
if (record.getR26_recoveries() != null) {
cellB.setCellValue(record.getR26_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row26
// Column C
cellC = row.createCell(2);
if (record.getR26_write_offs() != null) {
cellC.setCellValue(record.getR26_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row26
// Column D
cellD = row.createCell(3);
if (record.getR26_new_chars() != null) {
cellD.setCellValue(record.getR26_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row27
// Column B
row = sheet.getRow(26);

cellB = row.createCell(1);
if (record.getR27_recoveries() != null) {
cellB.setCellValue(record.getR27_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row27
// Column C
cellC = row.createCell(2);
if (record.getR27_write_offs() != null) {
cellC.setCellValue(record.getR27_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row27
// Column D
cellD = row.createCell(3);
if (record.getR27_new_chars() != null) {
cellD.setCellValue(record.getR27_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row28
// Column B
row = sheet.getRow(27);

cellB = row.createCell(1);
if (record.getR28_recoveries() != null) {
cellB.setCellValue(record.getR28_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row28
// Column C
cellC = row.createCell(2);
if (record.getR28_write_offs() != null) {
cellC.setCellValue(record.getR28_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row28
// Column D
cellD = row.createCell(3);
if (record.getR28_new_chars() != null) {
cellD.setCellValue(record.getR28_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


// row30
// Column B
row = sheet.getRow(29);

cellB = row.createCell(1);
if (record.getR30_recoveries() != null) {
cellB.setCellValue(record.getR30_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row30
// Column C
cellC = row.createCell(2);
if (record.getR30_write_offs() != null) {
cellC.setCellValue(record.getR30_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row30
// Column D
cellD = row.createCell(3);
if (record.getR30_new_chars() != null) {
cellD.setCellValue(record.getR30_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row31
// Column B
row = sheet.getRow(30);

cellB = row.createCell(1);
if (record.getR31_recoveries() != null) {
cellB.setCellValue(record.getR31_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row31
// Column C
cellC = row.createCell(2);
if (record.getR31_write_offs() != null) {
cellC.setCellValue(record.getR31_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row31
// Column D
cellD = row.createCell(3);
if (record.getR31_new_chars() != null) {
cellD.setCellValue(record.getR31_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row32
// Column B
row = sheet.getRow(31);

cellB = row.createCell(1);
if (record.getR32_recoveries() != null) {
cellB.setCellValue(record.getR32_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row32
// Column C
cellC = row.createCell(2);
if (record.getR32_write_offs() != null) {
cellC.setCellValue(record.getR32_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row32
// Column D
cellD = row.createCell(3);
if (record.getR32_new_chars() != null) {
cellD.setCellValue(record.getR32_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row33
// Column B
row = sheet.getRow(32);

cellB = row.createCell(1);
if (record.getR33_recoveries() != null) {
cellB.setCellValue(record.getR33_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row33
// Column C
cellC = row.createCell(2);
if (record.getR33_write_offs() != null) {
cellC.setCellValue(record.getR33_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row33
// Column D
cellD = row.createCell(3);
if (record.getR33_new_chars() != null) {
cellD.setCellValue(record.getR33_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row34
// Column B
row = sheet.getRow(33);

cellB = row.createCell(1);
if (record.getR34_recoveries() != null) {
cellB.setCellValue(record.getR34_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row34
// Column C
cellC = row.createCell(2);
if (record.getR34_write_offs() != null) {
cellC.setCellValue(record.getR34_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row34
// Column D
cellD = row.createCell(3);
if (record.getR34_new_chars() != null) {
cellD.setCellValue(record.getR34_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row35
// Column B
row = sheet.getRow(34);

cellB = row.createCell(1);
if (record.getR35_recoveries() != null) {
cellB.setCellValue(record.getR35_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row35
// Column C
cellC = row.createCell(2);
if (record.getR35_write_offs() != null) {
cellC.setCellValue(record.getR35_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row35
// Column D
cellD = row.createCell(3);
if (record.getR35_new_chars() != null) {
cellD.setCellValue(record.getR35_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row36
// Column B
row = sheet.getRow(35);

cellB = row.createCell(1);
if (record.getR36_recoveries() != null) {
cellB.setCellValue(record.getR36_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row36
// Column C
cellC = row.createCell(2);
if (record.getR36_write_offs() != null) {
cellC.setCellValue(record.getR36_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row36
// Column D
cellD = row.createCell(3);
if (record.getR36_new_chars() != null) {
cellD.setCellValue(record.getR36_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row37
// Column B
row = sheet.getRow(36);

cellB = row.createCell(1);
if (record.getR37_recoveries() != null) {
cellB.setCellValue(record.getR37_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row37
// Column C
cellC = row.createCell(2);
if (record.getR37_write_offs() != null) {
cellC.setCellValue(record.getR37_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row37
// Column D
cellD = row.createCell(3);
if (record.getR37_new_chars() != null) {
cellD.setCellValue(record.getR37_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


// row39
// Column B
row = sheet.getRow(38);

cellB = row.createCell(1);
if (record.getR39_recoveries() != null) {
cellB.setCellValue(record.getR39_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row39
// Column C
cellC = row.createCell(2);
if (record.getR39_write_offs() != null) {
cellC.setCellValue(record.getR39_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row39
// Column D
cellD = row.createCell(3);
if (record.getR39_new_chars() != null) {
cellD.setCellValue(record.getR39_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row40
// Column B
row = sheet.getRow(39);

cellB = row.createCell(1);
if (record.getR40_recoveries() != null) {
cellB.setCellValue(record.getR40_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row40
// Column C
cellC = row.createCell(2);
if (record.getR40_write_offs() != null) {
cellC.setCellValue(record.getR40_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row40
// Column D
cellD = row.createCell(3);
if (record.getR40_new_chars() != null) {
cellD.setCellValue(record.getR40_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row42
// Column B
row = sheet.getRow(41);

cellB = row.createCell(1);
if (record.getR42_recoveries() != null) {
cellB.setCellValue(record.getR42_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row42
// Column C
cellC = row.createCell(2);
if (record.getR42_write_offs() != null) {
cellC.setCellValue(record.getR42_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row42
// Column D
cellD = row.createCell(3);
if (record.getR42_new_chars() != null) {
cellD.setCellValue(record.getR42_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row43
// Column B
row = sheet.getRow(42);

cellB = row.createCell(1);
if (record.getR43_recoveries() != null) {
cellB.setCellValue(record.getR43_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row43
// Column C
cellC = row.createCell(2);
if (record.getR43_write_offs() != null) {
cellC.setCellValue(record.getR43_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row43
// Column D
cellD = row.createCell(3);
if (record.getR43_new_chars() != null) {
cellD.setCellValue(record.getR43_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row44
// Column B
row = sheet.getRow(43);

cellB = row.createCell(1);
if (record.getR44_recoveries() != null) {
cellB.setCellValue(record.getR44_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row44
// Column C
cellC = row.createCell(2);
if (record.getR44_write_offs() != null) {
cellC.setCellValue(record.getR44_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row44
// Column D
cellD = row.createCell(3);
if (record.getR44_new_chars() != null) {
cellD.setCellValue(record.getR44_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row45
// Column B
row = sheet.getRow(44);

cellB = row.createCell(1);
if (record.getR45_recoveries() != null) {
cellB.setCellValue(record.getR45_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row45
// Column C
cellC = row.createCell(2);
if (record.getR45_write_offs() != null) {
cellC.setCellValue(record.getR45_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row45
// Column D
cellD = row.createCell(3);
if (record.getR45_new_chars() != null) {
cellD.setCellValue(record.getR45_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row46
// Column B
row = sheet.getRow(45);

cellB = row.createCell(1);
if (record.getR46_recoveries() != null) {
cellB.setCellValue(record.getR46_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row46
// Column C
cellC = row.createCell(2);
if (record.getR46_write_offs() != null) {
cellC.setCellValue(record.getR46_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row46
// Column D
cellD = row.createCell(3);
if (record.getR46_new_chars() != null) {
cellD.setCellValue(record.getR46_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row47
// Column B
row = sheet.getRow(46);

cellB = row.createCell(1);
if (record.getR47_recoveries() != null) {
cellB.setCellValue(record.getR47_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row47
// Column C
cellC = row.createCell(2);
if (record.getR47_write_offs() != null) {
cellC.setCellValue(record.getR47_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row47
// Column D
cellD = row.createCell(3);
if (record.getR47_new_chars() != null) {
cellD.setCellValue(record.getR47_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row48
// Column B
row = sheet.getRow(47);

cellB = row.createCell(1);
if (record.getR48_recoveries() != null) {
cellB.setCellValue(record.getR48_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row48
// Column C
cellC = row.createCell(2);
if (record.getR48_write_offs() != null) {
cellC.setCellValue(record.getR48_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row48
// Column D
cellD = row.createCell(3);
if (record.getR48_new_chars() != null) {
cellD.setCellValue(record.getR48_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row50
// Column B
row = sheet.getRow(49);

cellB = row.createCell(1);
if (record.getR50_recoveries() != null) {
cellB.setCellValue(record.getR50_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row50
// Column C
cellC = row.createCell(2);
if (record.getR50_write_offs() != null) {
cellC.setCellValue(record.getR50_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row50
// Column D
cellD = row.createCell(3);
if (record.getR50_new_chars() != null) {
cellD.setCellValue(record.getR50_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row51
// Column B
row = sheet.getRow(50);

cellB = row.createCell(1);
if (record.getR51_recoveries() != null) {
cellB.setCellValue(record.getR51_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row51
// Column C
cellC = row.createCell(2);
if (record.getR51_write_offs() != null) {
cellC.setCellValue(record.getR51_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row51
// Column D
cellD = row.createCell(3);
if (record.getR51_new_chars() != null) {
cellD.setCellValue(record.getR51_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row52
// Column B
row = sheet.getRow(51);

cellB = row.createCell(1);
if (record.getR52_recoveries() != null) {
cellB.setCellValue(record.getR52_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row52
// Column C
cellC = row.createCell(2);
if (record.getR52_write_offs() != null) {
cellC.setCellValue(record.getR52_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row52
// Column D
cellD = row.createCell(3);
if (record.getR52_new_chars() != null) {
cellD.setCellValue(record.getR52_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row54
// Column B
row = sheet.getRow(53);

cellB = row.createCell(1);
if (record.getR54_recoveries() != null) {
cellB.setCellValue(record.getR54_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row54
// Column C
cellC = row.createCell(2);
if (record.getR54_write_offs() != null) {
cellC.setCellValue(record.getR54_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row54
// Column D
cellD = row.createCell(3);
if (record.getR54_new_chars() != null) {
cellD.setCellValue(record.getR54_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row55
// Column B
row = sheet.getRow(54);

cellB = row.createCell(1);
if (record.getR55_recoveries() != null) {
cellB.setCellValue(record.getR55_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row55
// Column C
cellC = row.createCell(2);
if (record.getR55_write_offs() != null) {
cellC.setCellValue(record.getR55_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row55
// Column D
cellD = row.createCell(3);
if (record.getR55_new_chars() != null) {
cellD.setCellValue(record.getR55_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row56
// Column B
row = sheet.getRow(55);

cellB = row.createCell(1);
if (record.getR56_recoveries() != null) {
cellB.setCellValue(record.getR56_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row56
// Column C
cellC = row.createCell(2);
if (record.getR56_write_offs() != null) {
cellC.setCellValue(record.getR56_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row56
// Column D
cellD = row.createCell(3);
if (record.getR56_new_chars() != null) {
cellD.setCellValue(record.getR56_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


// row58
// Column B
row = sheet.getRow(57);

cellB = row.createCell(1);
if (record.getR58_recoveries() != null) {
cellB.setCellValue(record.getR58_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row58
// Column C
cellC = row.createCell(2);
if (record.getR58_write_offs() != null) {
cellC.setCellValue(record.getR58_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row58
// Column D
cellD = row.createCell(3);
if (record.getR58_new_chars() != null) {
cellD.setCellValue(record.getR58_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row59
// Column B
row = sheet.getRow(58);

cellB = row.createCell(1);
if (record.getR59_recoveries() != null) {
cellB.setCellValue(record.getR59_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row59
// Column C
cellC = row.createCell(2);
if (record.getR59_write_offs() != null) {
cellC.setCellValue(record.getR59_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row59
// Column D
cellD = row.createCell(3);
if (record.getR59_new_chars() != null) {
cellD.setCellValue(record.getR59_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row60
// Column B
row = sheet.getRow(59);

cellB = row.createCell(1);
if (record.getR60_recoveries() != null) {
cellB.setCellValue(record.getR60_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row60
// Column C
cellC = row.createCell(2);
if (record.getR60_write_offs() != null) {
cellC.setCellValue(record.getR60_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row60
// Column D
cellD = row.createCell(3);
if (record.getR60_new_chars() != null) {
cellD.setCellValue(record.getR60_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row61
// Column B
row = sheet.getRow(60);

cellB = row.createCell(1);
if (record.getR61_recoveries() != null) {
cellB.setCellValue(record.getR61_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row61
// Column C
cellC = row.createCell(2);
if (record.getR61_write_offs() != null) {
cellC.setCellValue(record.getR61_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row61
// Column D
cellD = row.createCell(3);
if (record.getR61_new_chars() != null) {
cellD.setCellValue(record.getR61_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row62
// Column B
row = sheet.getRow(61);

cellB = row.createCell(1);
if (record.getR62_recoveries() != null) {
cellB.setCellValue(record.getR62_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row62
// Column C
cellC = row.createCell(2);
if (record.getR62_write_offs() != null) {
cellC.setCellValue(record.getR62_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row62
// Column D
cellD = row.createCell(3);
if (record.getR62_new_chars() != null) {
cellD.setCellValue(record.getR62_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row63
// Column B
row = sheet.getRow(62);

cellB = row.createCell(1);
if (record.getR63_recoveries() != null) {
cellB.setCellValue(record.getR63_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row63
// Column C
cellC = row.createCell(2);
if (record.getR63_write_offs() != null) {
cellC.setCellValue(record.getR63_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row63
// Column D
cellD = row.createCell(3);
if (record.getR63_new_chars() != null) {
cellD.setCellValue(record.getR63_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row67
// Column E

row = sheet.getRow(66);

cellE = row.createCell(4);
if (record.getR67_total() != null) {
cellE.setCellValue(record.getR67_total().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}

// row68
// Column E

row = sheet.getRow(67);

cellE = row.createCell(4);
if (record.getR68_total() != null) {
cellE.setCellValue(record.getR68_total().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}

}
workbook.setForceFormulaRecalculation(true);
} else {

}

// Write the final workbook content to the in-memory stream.
workbook.write(out);

logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

//audit service summary resub format


ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_I_S_CA RESUB SUMMARY", null, "BRRS_M_I_S_CA_RESUB_SUMMARYTABLE");
					}

return out.toByteArray();
}

}

// Resub Email Excel
public byte[] BRRS_M_I_S_CAEmailResubExcel(String filename, String reportId, String fromdate, String todate,
String currency, String dtltype, String type, BigDecimal version) throws Exception {

logger.info("Service: Starting Archival Email Excel generation process in memory.");

List<M_I_S_CA_RESUB_Summary_Entity> dataList = getResubSummaryByDateAndVersion(dateformat.parse(todate), version);

if (dataList.isEmpty()) {
logger.warn("Service: No data found for BRRS_M_I_S_CA report. Returning empty result.");
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
M_I_S_CA_RESUB_Summary_Entity record = dataList.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}

//row7
//Column B
Cell cellBdate = row.createCell(1);
if (record.getReport_date() != null) {
	cellBdate.setCellValue(record.getReport_date());
	cellBdate.setCellStyle(dateStyle);
} else {
	cellBdate.setCellValue("");
	cellBdate.setCellStyle(textStyle);
}



//ROW 10
row = sheet.getRow(9);
//EMAIL EXCEL 

//int startRow = 9;


//ROW10  Balance at the beginning of period


// row10
// Column E
Cell cellE = row.createCell(4);
if (record.getR10_total() != null) {
cellE.setCellValue(record.getR10_total().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}



//ROW11  Non-financial institutional units (sum of lines (i) to (vii))

//ROW12  Central Government

// row12
// Column B

row = sheet.getRow(11);

Cell cellB = row.createCell(1);
if (record.getR12_recoveries() != null) {
cellB.setCellValue(record.getR12_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row12
// Column C
Cell cellC = row.createCell(2);
if (record.getR12_write_offs() != null) {
cellC.setCellValue(record.getR12_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row12
// Column D
Cell cellD = row.createCell(3);
if (record.getR12_new_chars() != null) {
cellD.setCellValue(record.getR12_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW13  Local Government

// row13
// Column B

row = sheet.getRow(12);

cellB = row.createCell(1);
if (record.getR13_recoveries() != null) {
cellB.setCellValue(record.getR13_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row13
// Column C
cellC = row.createCell(2);
if (record.getR13_write_offs() != null) {
cellC.setCellValue(record.getR13_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row13
// Column D
cellD = row.createCell(3);
if (record.getR13_new_chars() != null) {
cellD.setCellValue(record.getR13_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW14  Public Non-Financial Corporations

// row14
// Column B
row = sheet.getRow(13);

cellB = row.createCell(1);
if (record.getR14_recoveries() != null) {
cellB.setCellValue(record.getR14_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row14
// Column C
cellC = row.createCell(2);
if (record.getR14_write_offs() != null) {
cellC.setCellValue(record.getR14_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row14
// Column D
cellD = row.createCell(3);
if (record.getR14_new_chars() != null) {
cellD.setCellValue(record.getR14_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




//ROW15  Other Non-Financial Corporations (Private business enterprises)

//ROW16  Agriculture, Forestry, Fishing

// row16
// Column B
row = sheet.getRow(15);

cellB = row.createCell(1);
if (record.getR16_recoveries() != null) {
cellB.setCellValue(record.getR16_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row16
// Column C
cellC = row.createCell(2);
if (record.getR16_write_offs() != null) {
cellC.setCellValue(record.getR16_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row16
// Column D
cellD = row.createCell(3);
if (record.getR16_new_chars() != null) {
cellD.setCellValue(record.getR16_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW17  Mining and Quarying

// row17
// Column B
row = sheet.getRow(16);

cellB = row.createCell(1);
if (record.getR17_recoveries() != null) {
cellB.setCellValue(record.getR17_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row17
// Column C
cellC = row.createCell(2);
if (record.getR17_write_offs() != null) {
cellC.setCellValue(record.getR17_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row17
// Column D
cellD = row.createCell(3);
if (record.getR17_new_chars() != null) {
cellD.setCellValue(record.getR17_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW18  Manufacturing

// row18
// Column B
row = sheet.getRow(17);

cellB = row.createCell(1);
if (record.getR18_recoveries() != null) {
cellB.setCellValue(record.getR18_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row18
// Column C
cellC = row.createCell(2);
if (record.getR18_write_offs() != null) {
cellC.setCellValue(record.getR18_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row18
// Column D
cellD = row.createCell(3);
if (record.getR18_new_chars() != null) {
cellD.setCellValue(record.getR18_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW19  Construction

// row19
// Column B
row = sheet.getRow(18);

cellB = row.createCell(1);
if (record.getR19_recoveries() != null) {
cellB.setCellValue(record.getR19_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row19
// Column C
cellC = row.createCell(2);
if (record.getR19_write_offs() != null) {
cellC.setCellValue(record.getR19_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row19
// Column D
cellD = row.createCell(3);
if (record.getR19_new_chars() != null) {
cellD.setCellValue(record.getR19_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW20  Commercial real estate

// row20
// Column B
row = sheet.getRow(19);

cellB = row.createCell(1);
if (record.getR20_recoveries() != null) {
cellB.setCellValue(record.getR20_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row20
// Column C
cellC = row.createCell(2);
if (record.getR20_write_offs() != null) {
cellC.setCellValue(record.getR20_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row20
// Column D
cellD = row.createCell(3);
if (record.getR20_new_chars() != null) {
cellD.setCellValue(record.getR20_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW21  Electricity

// row21
// Column B
row = sheet.getRow(20);

cellB = row.createCell(1);
if (record.getR21_recoveries() != null) {
cellB.setCellValue(record.getR21_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row21
// Column C
cellC = row.createCell(2);
if (record.getR21_write_offs() != null) {
cellC.setCellValue(record.getR21_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row21
// Column D
cellD = row.createCell(3);
if (record.getR21_new_chars() != null) {
cellD.setCellValue(record.getR21_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW22  Water

// row22
// Column B
row = sheet.getRow(21);

cellB = row.createCell(1);
if (record.getR22_recoveries() != null) {
cellB.setCellValue(record.getR22_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row22
// Column C
cellC = row.createCell(2);
if (record.getR22_write_offs() != null) {
cellC.setCellValue(record.getR22_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row22
// Column D
cellD = row.createCell(3);
if (record.getR22_new_chars() != null) {
cellD.setCellValue(record.getR22_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW23  Telecommunication and Post

// row23
// Column B
row = sheet.getRow(22);

cellB = row.createCell(1);
if (record.getR23_recoveries() != null) {
cellB.setCellValue(record.getR23_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row23
// Column C
cellC = row.createCell(2);
if (record.getR23_write_offs() != null) {
cellC.setCellValue(record.getR23_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row23
// Column D
cellD = row.createCell(3);
if (record.getR23_new_chars() != null) {
cellD.setCellValue(record.getR23_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW24  Tourism and hotels

// row24
// Column B
row = sheet.getRow(23);

cellB = row.createCell(1);
if (record.getR24_recoveries() != null) {
cellB.setCellValue(record.getR24_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row24
// Column C
cellC = row.createCell(2);
if (record.getR24_write_offs() != null) {
cellC.setCellValue(record.getR24_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row24
// Column D
cellD = row.createCell(3);
if (record.getR24_new_chars() != null) {
cellD.setCellValue(record.getR24_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row24
// Column E

cellE = row.createCell(4);
if (record.getR24_total() != null) {
cellE.setCellValue(record.getR24_total().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}

//ROW25  Transport and Storage

// row25
// Column B
row = sheet.getRow(24);

cellB = row.createCell(1);
if (record.getR25_recoveries() != null) {
cellB.setCellValue(record.getR25_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row25
// Column C
cellC = row.createCell(2);
if (record.getR25_write_offs() != null) {
cellC.setCellValue(record.getR25_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row25
// Column D
cellD = row.createCell(3);
if (record.getR25_new_chars() != null) {
cellD.setCellValue(record.getR25_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW26  Trade, restaurants and bars

// row26
// Column B
row = sheet.getRow(25);

cellB = row.createCell(1);
if (record.getR26_recoveries() != null) {
cellB.setCellValue(record.getR26_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row26
// Column C
cellC = row.createCell(2);
if (record.getR26_write_offs() != null) {
cellC.setCellValue(record.getR26_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row26
// Column D
cellD = row.createCell(3);
if (record.getR26_new_chars() != null) {
cellD.setCellValue(record.getR26_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW27  Business Services

// row27
// Column B
row = sheet.getRow(26);

cellB = row.createCell(1);
if (record.getR27_recoveries() != null) {
cellB.setCellValue(record.getR27_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row27
// Column C
cellC = row.createCell(2);
if (record.getR27_write_offs() != null) {
cellC.setCellValue(record.getR27_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row27
// Column D
cellD = row.createCell(3);
if (record.getR27_new_chars() != null) {
cellD.setCellValue(record.getR27_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW28  Other Community, Social and Personal Services

// row28
// Column B
row = sheet.getRow(27);

cellB = row.createCell(1);
if (record.getR28_recoveries() != null) {
cellB.setCellValue(record.getR28_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row28
// Column C
cellC = row.createCell(2);
if (record.getR28_write_offs() != null) {
cellC.setCellValue(record.getR28_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row28
// Column D
cellD = row.createCell(3);
if (record.getR28_new_chars() != null) {
cellD.setCellValue(record.getR28_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW29  Households

//ROW30  Residential property (owner occupied)

// row30
// Column B
row = sheet.getRow(29);

cellB = row.createCell(1);
if (record.getR30_recoveries() != null) {
cellB.setCellValue(record.getR30_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row30
// Column C
cellC = row.createCell(2);
if (record.getR30_write_offs() != null) {
cellC.setCellValue(record.getR30_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row30
// Column D
cellD = row.createCell(3);
if (record.getR30_new_chars() != null) {
cellD.setCellValue(record.getR30_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW31  Residential property (rented)

// row31
// Column B
row = sheet.getRow(30);

cellB = row.createCell(1);
if (record.getR31_recoveries() != null) {
cellB.setCellValue(record.getR31_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row31
// Column C
cellC = row.createCell(2);
if (record.getR31_write_offs() != null) {
cellC.setCellValue(record.getR31_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row31
// Column D
cellD = row.createCell(3);
if (record.getR31_new_chars() != null) {
cellD.setCellValue(record.getR31_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW32  Personal Loans

// row32
// Column B
row = sheet.getRow(31);

cellB = row.createCell(1);
if (record.getR32_recoveries() != null) {
cellB.setCellValue(record.getR32_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row32
// Column C
cellC = row.createCell(2);
if (record.getR32_write_offs() != null) {
cellC.setCellValue(record.getR32_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row32
// Column D
cellD = row.createCell(3);
if (record.getR32_new_chars() != null) {
cellD.setCellValue(record.getR32_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




//ROW33  Motor vehicle

// row33
// Column B
row = sheet.getRow(32);

cellB = row.createCell(1);
if (record.getR33_recoveries() != null) {
cellB.setCellValue(record.getR33_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}


// row33
// Column C
cellC = row.createCell(2);
if (record.getR33_write_offs() != null) {
cellC.setCellValue(record.getR33_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row33
// Column D
cellD = row.createCell(3);
if (record.getR33_new_chars() != null) {
cellD.setCellValue(record.getR33_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW34  Household goods

// row34
// Column B
row = sheet.getRow(33);

cellB = row.createCell(1);
if (record.getR34_recoveries() != null) {
cellB.setCellValue(record.getR34_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row34
// Column C
cellC = row.createCell(2);
if (record.getR34_write_offs() != null) {
cellC.setCellValue(record.getR34_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row34
// Column D
cellD = row.createCell(3);
if (record.getR34_new_chars() != null) {
cellD.setCellValue(record.getR34_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW35  Credit card loans

// row35
// Column B
row = sheet.getRow(34);

cellB = row.createCell(1);
if (record.getR35_recoveries() != null) {
cellB.setCellValue(record.getR35_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row35
// Column C
cellC = row.createCell(2);
if (record.getR35_write_offs() != null) {
cellC.setCellValue(record.getR35_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row35
// Column D
cellD = row.createCell(3);
if (record.getR35_new_chars() != null) {
cellD.setCellValue(record.getR35_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




//ROW36  Non-Profit Institutions Serving Households

// row36
// Column B
row = sheet.getRow(35);

cellB = row.createCell(1);
if (record.getR36_recoveries() != null) {
cellB.setCellValue(record.getR36_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row36
// Column C
cellC = row.createCell(2);
if (record.getR36_write_offs() != null) {
cellC.setCellValue(record.getR36_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row36
// Column D
cellD = row.createCell(3);
if (record.getR36_new_chars() != null) {
cellD.setCellValue(record.getR36_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW37  Other

// row37
// Column B
row = sheet.getRow(36);

cellB = row.createCell(1);
if (record.getR37_recoveries() != null) {
cellB.setCellValue(record.getR37_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row37
// Column C
cellC = row.createCell(2);
if (record.getR37_write_offs() != null) {
cellC.setCellValue(record.getR37_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row37
// Column D
cellD = row.createCell(3);
if (record.getR37_new_chars() != null) {
cellD.setCellValue(record.getR37_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW38  Non-Residents

// row38
// Column B
row = sheet.getRow(37);

cellB = row.createCell(1);
if (record.getR38_recoveries() != null) {
cellB.setCellValue(record.getR38_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}


// row38
// Column D
cellD = row.createCell(3);
if (record.getR38_new_chars() != null) {
cellD.setCellValue(record.getR38_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW39  Other Non-Financial Corporations

// row39
// Column B
row = sheet.getRow(38);

cellB = row.createCell(1);
if (record.getR39_recoveries() != null) {
cellB.setCellValue(record.getR39_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row39
// Column C
cellC = row.createCell(2);
if (record.getR39_write_offs() != null) {
cellC.setCellValue(record.getR39_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row39
// Column D
cellD = row.createCell(3);
if (record.getR39_new_chars() != null) {
cellD.setCellValue(record.getR39_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW40  Households


// row40
// Column B
row = sheet.getRow(39);

cellB = row.createCell(1);
if (record.getR40_recoveries() != null) {
cellB.setCellValue(record.getR40_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row40
// Column C
cellC = row.createCell(2);
if (record.getR40_write_offs() != null) {
cellC.setCellValue(record.getR40_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row40
// Column D
cellD = row.createCell(3);
if (record.getR40_new_chars() != null) {
cellD.setCellValue(record.getR40_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW41  Financial institutional units

// row41
// Column B
row = sheet.getRow(40);

cellB = row.createCell(1);
if (record.getR41_recoveries() != null) {
cellB.setCellValue(record.getR41_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}


// row41
// Column D
cellD = row.createCell(3);
if (record.getR41_new_chars() != null) {
cellD.setCellValue(record.getR41_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW42  Central Bank

// row42
// Column B
row = sheet.getRow(41);

cellB = row.createCell(1);
if (record.getR42_recoveries() != null) {
cellB.setCellValue(record.getR42_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row42
// Column C
cellC = row.createCell(2);
if (record.getR42_write_offs() != null) {
cellC.setCellValue(record.getR42_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row42
// Column D
cellD = row.createCell(3);
if (record.getR42_new_chars() != null) {
cellD.setCellValue(record.getR42_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW43  Commercial Banks

// row43
// Column B
row = sheet.getRow(42);

cellB = row.createCell(1);
if (record.getR43_recoveries() != null) {
cellB.setCellValue(record.getR43_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row43
// Column C
cellC = row.createCell(2);
if (record.getR43_write_offs() != null) {
cellC.setCellValue(record.getR43_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row43
// Column D
cellD = row.createCell(3);
if (record.getR43_new_chars() != null) {
cellD.setCellValue(record.getR43_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW44  Other Depository Corporations

// row44
// Column B
row = sheet.getRow(43);

cellB = row.createCell(1);
if (record.getR44_recoveries() != null) {
cellB.setCellValue(record.getR44_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row44
// Column C
cellC = row.createCell(2);
if (record.getR44_write_offs() != null) {
cellC.setCellValue(record.getR44_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row44
// Column D
cellD = row.createCell(3);
if (record.getR44_new_chars() != null) {
cellD.setCellValue(record.getR44_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW45  Botswana Savings Bank (BSB)

// row45
// Column B
row = sheet.getRow(44);

cellB = row.createCell(1);
if (record.getR45_recoveries() != null) {
cellB.setCellValue(record.getR45_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row45
// Column C
cellC = row.createCell(2);
if (record.getR45_write_offs() != null) {
cellC.setCellValue(record.getR45_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row45
// Column D
cellD = row.createCell(3);
if (record.getR45_new_chars() != null) {
cellD.setCellValue(record.getR45_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW46  Botswana Building Society (BBS)

// row46
// Column B
row = sheet.getRow(45);

cellB = row.createCell(1);
if (record.getR46_recoveries() != null) {
cellB.setCellValue(record.getR46_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row46
// Column C
cellC = row.createCell(2);
if (record.getR46_write_offs() != null) {
cellC.setCellValue(record.getR46_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row46
// Column D
cellD = row.createCell(3);
if (record.getR46_new_chars() != null) {
cellD.setCellValue(record.getR46_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW47  Domestic Money Market Unit Trust

// row47
// Column B
row = sheet.getRow(46);

cellB = row.createCell(1);
if (record.getR47_recoveries() != null) {
cellB.setCellValue(record.getR47_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row47
// Column C
cellC = row.createCell(2);
if (record.getR47_write_offs() != null) {
cellC.setCellValue(record.getR47_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row47
// Column D
cellD = row.createCell(3);
if (record.getR47_new_chars() != null) {
cellD.setCellValue(record.getR47_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW48  Other (specify)

// row48
// Column B
row = sheet.getRow(47);

cellB = row.createCell(1);
if (record.getR48_recoveries() != null) {
cellB.setCellValue(record.getR48_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row48
// Column C
cellC = row.createCell(2);
if (record.getR48_write_offs() != null) {
cellC.setCellValue(record.getR48_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row48
// Column D
cellD = row.createCell(3);
if (record.getR48_new_chars() != null) {
cellD.setCellValue(record.getR48_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW49  Other Financial Corporations

// row49
// Column B
row = sheet.getRow(48);

cellB = row.createCell(1);
if (record.getR49_recoveries() != null) {
cellB.setCellValue(record.getR49_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}


// row49
// Column D
cellD = row.createCell(3);
if (record.getR49_new_chars() != null) {
cellD.setCellValue(record.getR49_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW50  Insurance Companies

// row50
// Column B
row = sheet.getRow(49);

cellB = row.createCell(1);
if (record.getR50_recoveries() != null) {
cellB.setCellValue(record.getR50_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row50
// Column C
cellC = row.createCell(2);
if (record.getR50_write_offs() != null) {
cellC.setCellValue(record.getR50_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row50
// Column D
cellD = row.createCell(3);
if (record.getR50_new_chars() != null) {
cellD.setCellValue(record.getR50_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW51  Pension Funds

// row51
// Column B
row = sheet.getRow(50);

cellB = row.createCell(1);
if (record.getR51_recoveries() != null) {
cellB.setCellValue(record.getR51_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row51
// Column C
cellC = row.createCell(2);
if (record.getR51_write_offs() != null) {
cellC.setCellValue(record.getR51_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row51
// Column D
cellD = row.createCell(3);
if (record.getR51_new_chars() != null) {
cellD.setCellValue(record.getR51_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW52  SACCOs

// row52
// Column B
row = sheet.getRow(51);

cellB = row.createCell(1);
if (record.getR52_recoveries() != null) {
cellB.setCellValue(record.getR52_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row52
// Column C
cellC = row.createCell(2);
if (record.getR52_write_offs() != null) {
cellC.setCellValue(record.getR52_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row52
// Column D
cellD = row.createCell(3);
if (record.getR52_new_chars() != null) {
cellD.setCellValue(record.getR52_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW53  Other Financial Intermediaries

// row53
// Column B
row = sheet.getRow(52);

cellB = row.createCell(1);
if (record.getR53_recoveries() != null) {
cellB.setCellValue(record.getR53_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}


// row53
// Column D
cellD = row.createCell(3);
if (record.getR53_new_chars() != null) {
cellD.setCellValue(record.getR53_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW54  Finance companies

// row54
// Column B
row = sheet.getRow(53);

cellB = row.createCell(1);
if (record.getR54_recoveries() != null) {
cellB.setCellValue(record.getR54_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row54
// Column C
cellC = row.createCell(2);
if (record.getR54_write_offs() != null) {
cellC.setCellValue(record.getR54_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row54
// Column D
cellD = row.createCell(3);
if (record.getR54_new_chars() != null) {
cellD.setCellValue(record.getR54_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW55  Medical Aid Schemes

// row55
// Column B
row = sheet.getRow(54);

cellB = row.createCell(1);
if (record.getR55_recoveries() != null) {
cellB.setCellValue(record.getR55_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row55
// Column C
cellC = row.createCell(2);
if (record.getR55_write_offs() != null) {
cellC.setCellValue(record.getR55_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row55
// Column D
cellD = row.createCell(3);
if (record.getR55_new_chars() != null) {
cellD.setCellValue(record.getR55_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW56  Public sector financial intermediaries

// row56
// Column B
row = sheet.getRow(55);

cellB = row.createCell(1);
if (record.getR56_recoveries() != null) {
cellB.setCellValue(record.getR56_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row56
// Column C
cellC = row.createCell(2);
if (record.getR56_write_offs() != null) {
cellC.setCellValue(record.getR56_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row56
// Column D
cellD = row.createCell(3);
if (record.getR56_new_chars() != null) {
cellD.setCellValue(record.getR56_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW57  Financial Auxiliaries

// row57
// Column B
row = sheet.getRow(56);

cellB = row.createCell(1);
if (record.getR57_recoveries() != null) {
cellB.setCellValue(record.getR57_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}


// row57
// Column D
cellD = row.createCell(3);
if (record.getR57_new_chars() != null) {
cellD.setCellValue(record.getR57_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW58  Insurance brokers


// row58
// Column B
row = sheet.getRow(57);

cellB = row.createCell(1);
if (record.getR58_recoveries() != null) {
cellB.setCellValue(record.getR58_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row58
// Column C
cellC = row.createCell(2);
if (record.getR58_write_offs() != null) {
cellC.setCellValue(record.getR58_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row58
// Column D
cellD = row.createCell(3);
if (record.getR58_new_chars() != null) {
cellD.setCellValue(record.getR58_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW59  fund administrators

// row59
// Column B
row = sheet.getRow(58);

cellB = row.createCell(1);
if (record.getR59_recoveries() != null) {
cellB.setCellValue(record.getR59_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row59
// Column C
cellC = row.createCell(2);
if (record.getR59_write_offs() != null) {
cellC.setCellValue(record.getR59_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row59
// Column D
cellD = row.createCell(3);
if (record.getR59_new_chars() != null) {
cellD.setCellValue(record.getR59_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW60  Bureau de change

// row60
// Column B
row = sheet.getRow(59);

cellB = row.createCell(1);
if (record.getR60_recoveries() != null) {
cellB.setCellValue(record.getR60_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row60
// Column C
cellC = row.createCell(2);
if (record.getR60_write_offs() != null) {
cellC.setCellValue(record.getR60_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row60
// Column D
cellD = row.createCell(3);
if (record.getR60_new_chars() != null) {
cellD.setCellValue(record.getR60_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW61  Asset managers

// row61
// Column B
row = sheet.getRow(60);

cellB = row.createCell(1);
if (record.getR61_recoveries() != null) {
cellB.setCellValue(record.getR61_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row61
// Column C
cellC = row.createCell(2);
if (record.getR61_write_offs() != null) {
cellC.setCellValue(record.getR61_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row61
// Column D
cellD = row.createCell(3);
if (record.getR61_new_chars() != null) {
cellD.setCellValue(record.getR61_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//ROW62  Other (specify)

// row62
// Column B
row = sheet.getRow(61);

cellB = row.createCell(1);
if (record.getR62_recoveries() != null) {
cellB.setCellValue(record.getR62_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row62
// Column C
cellC = row.createCell(2);
if (record.getR62_write_offs() != null) {
cellC.setCellValue(record.getR62_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row62
// Column D
cellD = row.createCell(3);
if (record.getR62_new_chars() != null) {
cellD.setCellValue(record.getR62_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//ROW63  Non-residents

// row63
// Column B
row = sheet.getRow(62);

cellB = row.createCell(1);
if (record.getR63_recoveries() != null) {
cellB.setCellValue(record.getR63_recoveries().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

// row63
// Column C
cellC = row.createCell(2);
if (record.getR63_write_offs() != null) {
cellC.setCellValue(record.getR63_write_offs().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// row63
// Column D
cellD = row.createCell(3);
if (record.getR63_new_chars() != null) {
cellD.setCellValue(record.getR63_new_chars().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//ROW64  Total specific impairments
//ROW65  Specific impairments Balance at end of period (after adj. for recoveries and write-offs)
//ROW66  Collectively assessed (General) impairments
//ROW67  Balance at beginning of period

// row67
// Column E

row = sheet.getRow(66);

cellE = row.createCell(4);
if (record.getR67_total() != null) {
cellE.setCellValue(record.getR67_total().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}


//ROW68  Charge to P&L
//ROW69  Balance at end of period
//ROW70  TOTAL IMPAIRMENTS (LINES 5 AND 9)


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
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_I_S_CA EMAIL RESUB SUMMARY", null, "BRRS_M_I_S_CA_RESUB_SUMMARYTABLE");
				}



return out.toByteArray();
}
}

	class M_I_S_CASummaryRowMapper implements RowMapper<M_I_S_CA_Summary_Entity> {
		@Override
		public M_I_S_CA_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_I_S_CA_Summary_Entity obj = new M_I_S_CA_Summary_Entity();
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getString("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			obj.setR10_spec_impairments(rs.getString("R10_SPEC_IMPAIRMENTS"));
			obj.setR10_recoveries(rs.getBigDecimal("R10_RECOVERIES"));
			obj.setR10_write_offs(rs.getBigDecimal("R10_WRITE_OFFS"));
			obj.setR10_new_chars(rs.getBigDecimal("R10_NEW_CHARS"));
			obj.setR10_total(rs.getBigDecimal("R10_TOTAL"));
			obj.setR11_spec_impairments(rs.getString("R11_SPEC_IMPAIRMENTS"));
			obj.setR11_recoveries(rs.getBigDecimal("R11_RECOVERIES"));
			obj.setR11_write_offs(rs.getBigDecimal("R11_WRITE_OFFS"));
			obj.setR11_new_chars(rs.getBigDecimal("R11_NEW_CHARS"));
			obj.setR11_total(rs.getBigDecimal("R11_TOTAL"));
			obj.setR12_spec_impairments(rs.getString("R12_SPEC_IMPAIRMENTS"));
			obj.setR12_recoveries(rs.getBigDecimal("R12_RECOVERIES"));
			obj.setR12_write_offs(rs.getBigDecimal("R12_WRITE_OFFS"));
			obj.setR12_new_chars(rs.getBigDecimal("R12_NEW_CHARS"));
			obj.setR12_total(rs.getBigDecimal("R12_TOTAL"));
			obj.setR13_spec_impairments(rs.getString("R13_SPEC_IMPAIRMENTS"));
			obj.setR13_recoveries(rs.getBigDecimal("R13_RECOVERIES"));
			obj.setR13_write_offs(rs.getBigDecimal("R13_WRITE_OFFS"));
			obj.setR13_new_chars(rs.getBigDecimal("R13_NEW_CHARS"));
			obj.setR13_total(rs.getBigDecimal("R13_TOTAL"));
			obj.setR14_spec_impairments(rs.getString("R14_SPEC_IMPAIRMENTS"));
			obj.setR14_recoveries(rs.getBigDecimal("R14_RECOVERIES"));
			obj.setR14_write_offs(rs.getBigDecimal("R14_WRITE_OFFS"));
			obj.setR14_new_chars(rs.getBigDecimal("R14_NEW_CHARS"));
			obj.setR14_total(rs.getBigDecimal("R14_TOTAL"));
			obj.setR15_spec_impairments(rs.getString("R15_SPEC_IMPAIRMENTS"));
			obj.setR15_recoveries(rs.getBigDecimal("R15_RECOVERIES"));
			obj.setR15_write_offs(rs.getBigDecimal("R15_WRITE_OFFS"));
			obj.setR15_new_chars(rs.getBigDecimal("R15_NEW_CHARS"));
			obj.setR15_total(rs.getBigDecimal("R15_TOTAL"));
			obj.setR16_spec_impairments(rs.getString("R16_SPEC_IMPAIRMENTS"));
			obj.setR16_recoveries(rs.getBigDecimal("R16_RECOVERIES"));
			obj.setR16_write_offs(rs.getBigDecimal("R16_WRITE_OFFS"));
			obj.setR16_new_chars(rs.getBigDecimal("R16_NEW_CHARS"));
			obj.setR16_total(rs.getBigDecimal("R16_TOTAL"));
			obj.setR17_spec_impairments(rs.getString("R17_SPEC_IMPAIRMENTS"));
			obj.setR17_recoveries(rs.getBigDecimal("R17_RECOVERIES"));
			obj.setR17_write_offs(rs.getBigDecimal("R17_WRITE_OFFS"));
			obj.setR17_new_chars(rs.getBigDecimal("R17_NEW_CHARS"));
			obj.setR17_total(rs.getBigDecimal("R17_TOTAL"));
			obj.setR18_spec_impairments(rs.getString("R18_SPEC_IMPAIRMENTS"));
			obj.setR18_recoveries(rs.getBigDecimal("R18_RECOVERIES"));
			obj.setR18_write_offs(rs.getBigDecimal("R18_WRITE_OFFS"));
			obj.setR18_new_chars(rs.getBigDecimal("R18_NEW_CHARS"));
			obj.setR18_total(rs.getBigDecimal("R18_TOTAL"));
			obj.setR19_spec_impairments(rs.getString("R19_SPEC_IMPAIRMENTS"));
			obj.setR19_recoveries(rs.getBigDecimal("R19_RECOVERIES"));
			obj.setR19_write_offs(rs.getBigDecimal("R19_WRITE_OFFS"));
			obj.setR19_new_chars(rs.getBigDecimal("R19_NEW_CHARS"));
			obj.setR19_total(rs.getBigDecimal("R19_TOTAL"));
			obj.setR20_spec_impairments(rs.getString("R20_SPEC_IMPAIRMENTS"));
			obj.setR20_recoveries(rs.getBigDecimal("R20_RECOVERIES"));
			obj.setR20_write_offs(rs.getBigDecimal("R20_WRITE_OFFS"));
			obj.setR20_new_chars(rs.getBigDecimal("R20_NEW_CHARS"));
			obj.setR20_total(rs.getBigDecimal("R20_TOTAL"));
			obj.setR21_spec_impairments(rs.getString("R21_SPEC_IMPAIRMENTS"));
			obj.setR21_recoveries(rs.getBigDecimal("R21_RECOVERIES"));
			obj.setR21_write_offs(rs.getBigDecimal("R21_WRITE_OFFS"));
			obj.setR21_new_chars(rs.getBigDecimal("R21_NEW_CHARS"));
			obj.setR21_total(rs.getBigDecimal("R21_TOTAL"));
			obj.setR22_spec_impairments(rs.getString("R22_SPEC_IMPAIRMENTS"));
			obj.setR22_recoveries(rs.getBigDecimal("R22_RECOVERIES"));
			obj.setR22_write_offs(rs.getBigDecimal("R22_WRITE_OFFS"));
			obj.setR22_new_chars(rs.getBigDecimal("R22_NEW_CHARS"));
			obj.setR22_total(rs.getBigDecimal("R22_TOTAL"));
			obj.setR23_spec_impairments(rs.getString("R23_SPEC_IMPAIRMENTS"));
			obj.setR23_recoveries(rs.getBigDecimal("R23_RECOVERIES"));
			obj.setR23_write_offs(rs.getBigDecimal("R23_WRITE_OFFS"));
			obj.setR23_new_chars(rs.getBigDecimal("R23_NEW_CHARS"));
			obj.setR23_total(rs.getBigDecimal("R23_TOTAL"));
			obj.setR24_spec_impairments(rs.getString("R24_SPEC_IMPAIRMENTS"));
			obj.setR24_recoveries(rs.getBigDecimal("R24_RECOVERIES"));
			obj.setR24_write_offs(rs.getBigDecimal("R24_WRITE_OFFS"));
			obj.setR24_new_chars(rs.getBigDecimal("R24_NEW_CHARS"));
			obj.setR24_total(rs.getBigDecimal("R24_TOTAL"));
			obj.setR25_spec_impairments(rs.getString("R25_SPEC_IMPAIRMENTS"));
			obj.setR25_recoveries(rs.getBigDecimal("R25_RECOVERIES"));
			obj.setR25_write_offs(rs.getBigDecimal("R25_WRITE_OFFS"));
			obj.setR25_new_chars(rs.getBigDecimal("R25_NEW_CHARS"));
			obj.setR25_total(rs.getBigDecimal("R25_TOTAL"));
			obj.setR26_spec_impairments(rs.getString("R26_SPEC_IMPAIRMENTS"));
			obj.setR26_recoveries(rs.getBigDecimal("R26_RECOVERIES"));
			obj.setR26_write_offs(rs.getBigDecimal("R26_WRITE_OFFS"));
			obj.setR26_new_chars(rs.getBigDecimal("R26_NEW_CHARS"));
			obj.setR26_total(rs.getBigDecimal("R26_TOTAL"));
			obj.setR27_spec_impairments(rs.getString("R27_SPEC_IMPAIRMENTS"));
			obj.setR27_recoveries(rs.getBigDecimal("R27_RECOVERIES"));
			obj.setR27_write_offs(rs.getBigDecimal("R27_WRITE_OFFS"));
			obj.setR27_new_chars(rs.getBigDecimal("R27_NEW_CHARS"));
			obj.setR27_total(rs.getBigDecimal("R27_TOTAL"));
			obj.setR28_spec_impairments(rs.getString("R28_SPEC_IMPAIRMENTS"));
			obj.setR28_recoveries(rs.getBigDecimal("R28_RECOVERIES"));
			obj.setR28_write_offs(rs.getBigDecimal("R28_WRITE_OFFS"));
			obj.setR28_new_chars(rs.getBigDecimal("R28_NEW_CHARS"));
			obj.setR28_total(rs.getBigDecimal("R28_TOTAL"));
			obj.setR29_spec_impairments(rs.getString("R29_SPEC_IMPAIRMENTS"));
			obj.setR29_recoveries(rs.getBigDecimal("R29_RECOVERIES"));
			obj.setR29_write_offs(rs.getBigDecimal("R29_WRITE_OFFS"));
			obj.setR29_new_chars(rs.getBigDecimal("R29_NEW_CHARS"));
			obj.setR29_total(rs.getBigDecimal("R29_TOTAL"));
			obj.setR30_spec_impairments(rs.getString("R30_SPEC_IMPAIRMENTS"));
			obj.setR30_recoveries(rs.getBigDecimal("R30_RECOVERIES"));
			obj.setR30_write_offs(rs.getBigDecimal("R30_WRITE_OFFS"));
			obj.setR30_new_chars(rs.getBigDecimal("R30_NEW_CHARS"));
			obj.setR30_total(rs.getBigDecimal("R30_TOTAL"));
			obj.setR31_spec_impairments(rs.getString("R31_SPEC_IMPAIRMENTS"));
			obj.setR31_recoveries(rs.getBigDecimal("R31_RECOVERIES"));
			obj.setR31_write_offs(rs.getBigDecimal("R31_WRITE_OFFS"));
			obj.setR31_new_chars(rs.getBigDecimal("R31_NEW_CHARS"));
			obj.setR31_total(rs.getBigDecimal("R31_TOTAL"));
			obj.setR32_spec_impairments(rs.getString("R32_SPEC_IMPAIRMENTS"));
			obj.setR32_recoveries(rs.getBigDecimal("R32_RECOVERIES"));
			obj.setR32_write_offs(rs.getBigDecimal("R32_WRITE_OFFS"));
			obj.setR32_new_chars(rs.getBigDecimal("R32_NEW_CHARS"));
			obj.setR32_total(rs.getBigDecimal("R32_TOTAL"));
			obj.setR33_spec_impairments(rs.getString("R33_SPEC_IMPAIRMENTS"));
			obj.setR33_recoveries(rs.getBigDecimal("R33_RECOVERIES"));
			obj.setR33_write_offs(rs.getBigDecimal("R33_WRITE_OFFS"));
			obj.setR33_new_chars(rs.getBigDecimal("R33_NEW_CHARS"));
			obj.setR33_total(rs.getBigDecimal("R33_TOTAL"));
			obj.setR34_spec_impairments(rs.getString("R34_SPEC_IMPAIRMENTS"));
			obj.setR34_recoveries(rs.getBigDecimal("R34_RECOVERIES"));
			obj.setR34_write_offs(rs.getBigDecimal("R34_WRITE_OFFS"));
			obj.setR34_new_chars(rs.getBigDecimal("R34_NEW_CHARS"));
			obj.setR34_total(rs.getBigDecimal("R34_TOTAL"));
			obj.setR35_spec_impairments(rs.getString("R35_SPEC_IMPAIRMENTS"));
			obj.setR35_recoveries(rs.getBigDecimal("R35_RECOVERIES"));
			obj.setR35_write_offs(rs.getBigDecimal("R35_WRITE_OFFS"));
			obj.setR35_new_chars(rs.getBigDecimal("R35_NEW_CHARS"));
			obj.setR35_total(rs.getBigDecimal("R35_TOTAL"));
			obj.setR36_spec_impairments(rs.getString("R36_SPEC_IMPAIRMENTS"));
			obj.setR36_recoveries(rs.getBigDecimal("R36_RECOVERIES"));
			obj.setR36_write_offs(rs.getBigDecimal("R36_WRITE_OFFS"));
			obj.setR36_new_chars(rs.getBigDecimal("R36_NEW_CHARS"));
			obj.setR36_total(rs.getBigDecimal("R36_TOTAL"));
			obj.setR37_spec_impairments(rs.getString("R37_SPEC_IMPAIRMENTS"));
			obj.setR37_recoveries(rs.getBigDecimal("R37_RECOVERIES"));
			obj.setR37_write_offs(rs.getBigDecimal("R37_WRITE_OFFS"));
			obj.setR37_new_chars(rs.getBigDecimal("R37_NEW_CHARS"));
			obj.setR37_total(rs.getBigDecimal("R37_TOTAL"));
			obj.setR38_spec_impairments(rs.getString("R38_SPEC_IMPAIRMENTS"));
			obj.setR38_recoveries(rs.getBigDecimal("R38_RECOVERIES"));
			obj.setR38_write_offs(rs.getBigDecimal("R38_WRITE_OFFS"));
			obj.setR38_new_chars(rs.getBigDecimal("R38_NEW_CHARS"));
			obj.setR38_total(rs.getBigDecimal("R38_TOTAL"));
			obj.setR39_spec_impairments(rs.getString("R39_SPEC_IMPAIRMENTS"));
			obj.setR39_recoveries(rs.getBigDecimal("R39_RECOVERIES"));
			obj.setR39_write_offs(rs.getBigDecimal("R39_WRITE_OFFS"));
			obj.setR39_new_chars(rs.getBigDecimal("R39_NEW_CHARS"));
			obj.setR39_total(rs.getBigDecimal("R39_TOTAL"));
			obj.setR40_spec_impairments(rs.getString("R40_SPEC_IMPAIRMENTS"));
			obj.setR40_recoveries(rs.getBigDecimal("R40_RECOVERIES"));
			obj.setR40_write_offs(rs.getBigDecimal("R40_WRITE_OFFS"));
			obj.setR40_new_chars(rs.getBigDecimal("R40_NEW_CHARS"));
			obj.setR40_total(rs.getBigDecimal("R40_TOTAL"));
			obj.setR41_spec_impairments(rs.getString("R41_SPEC_IMPAIRMENTS"));
			obj.setR41_recoveries(rs.getBigDecimal("R41_RECOVERIES"));
			obj.setR41_write_offs(rs.getBigDecimal("R41_WRITE_OFFS"));
			obj.setR41_new_chars(rs.getBigDecimal("R41_NEW_CHARS"));
			obj.setR41_total(rs.getBigDecimal("R41_TOTAL"));
			obj.setR42_spec_impairments(rs.getString("R42_SPEC_IMPAIRMENTS"));
			obj.setR42_recoveries(rs.getBigDecimal("R42_RECOVERIES"));
			obj.setR42_write_offs(rs.getBigDecimal("R42_WRITE_OFFS"));
			obj.setR42_new_chars(rs.getBigDecimal("R42_NEW_CHARS"));
			obj.setR42_total(rs.getBigDecimal("R42_TOTAL"));
			obj.setR43_spec_impairments(rs.getString("R43_SPEC_IMPAIRMENTS"));
			obj.setR43_recoveries(rs.getBigDecimal("R43_RECOVERIES"));
			obj.setR43_write_offs(rs.getBigDecimal("R43_WRITE_OFFS"));
			obj.setR43_new_chars(rs.getBigDecimal("R43_NEW_CHARS"));
			obj.setR43_total(rs.getBigDecimal("R43_TOTAL"));
			obj.setR44_spec_impairments(rs.getString("R44_SPEC_IMPAIRMENTS"));
			obj.setR44_recoveries(rs.getBigDecimal("R44_RECOVERIES"));
			obj.setR44_write_offs(rs.getBigDecimal("R44_WRITE_OFFS"));
			obj.setR44_new_chars(rs.getBigDecimal("R44_NEW_CHARS"));
			obj.setR44_total(rs.getBigDecimal("R44_TOTAL"));
			obj.setR45_spec_impairments(rs.getString("R45_SPEC_IMPAIRMENTS"));
			obj.setR45_recoveries(rs.getBigDecimal("R45_RECOVERIES"));
			obj.setR45_write_offs(rs.getBigDecimal("R45_WRITE_OFFS"));
			obj.setR45_new_chars(rs.getBigDecimal("R45_NEW_CHARS"));
			obj.setR45_total(rs.getBigDecimal("R45_TOTAL"));
			obj.setR46_spec_impairments(rs.getString("R46_SPEC_IMPAIRMENTS"));
			obj.setR46_recoveries(rs.getBigDecimal("R46_RECOVERIES"));
			obj.setR46_write_offs(rs.getBigDecimal("R46_WRITE_OFFS"));
			obj.setR46_new_chars(rs.getBigDecimal("R46_NEW_CHARS"));
			obj.setR46_total(rs.getBigDecimal("R46_TOTAL"));
			obj.setR47_spec_impairments(rs.getString("R47_SPEC_IMPAIRMENTS"));
			obj.setR47_recoveries(rs.getBigDecimal("R47_RECOVERIES"));
			obj.setR47_write_offs(rs.getBigDecimal("R47_WRITE_OFFS"));
			obj.setR47_new_chars(rs.getBigDecimal("R47_NEW_CHARS"));
			obj.setR47_total(rs.getBigDecimal("R47_TOTAL"));
			obj.setR48_spec_impairments(rs.getString("R48_SPEC_IMPAIRMENTS"));
			obj.setR48_recoveries(rs.getBigDecimal("R48_RECOVERIES"));
			obj.setR48_write_offs(rs.getBigDecimal("R48_WRITE_OFFS"));
			obj.setR48_new_chars(rs.getBigDecimal("R48_NEW_CHARS"));
			obj.setR48_total(rs.getBigDecimal("R48_TOTAL"));
			obj.setR49_spec_impairments(rs.getString("R49_SPEC_IMPAIRMENTS"));
			obj.setR49_recoveries(rs.getBigDecimal("R49_RECOVERIES"));
			obj.setR49_write_offs(rs.getBigDecimal("R49_WRITE_OFFS"));
			obj.setR49_new_chars(rs.getBigDecimal("R49_NEW_CHARS"));
			obj.setR49_total(rs.getBigDecimal("R49_TOTAL"));
			obj.setR50_spec_impairments(rs.getString("R50_SPEC_IMPAIRMENTS"));
			obj.setR50_recoveries(rs.getBigDecimal("R50_RECOVERIES"));
			obj.setR50_write_offs(rs.getBigDecimal("R50_WRITE_OFFS"));
			obj.setR50_new_chars(rs.getBigDecimal("R50_NEW_CHARS"));
			obj.setR50_total(rs.getBigDecimal("R50_TOTAL"));
			obj.setR51_spec_impairments(rs.getString("R51_SPEC_IMPAIRMENTS"));
			obj.setR51_recoveries(rs.getBigDecimal("R51_RECOVERIES"));
			obj.setR51_write_offs(rs.getBigDecimal("R51_WRITE_OFFS"));
			obj.setR51_new_chars(rs.getBigDecimal("R51_NEW_CHARS"));
			obj.setR51_total(rs.getBigDecimal("R51_TOTAL"));
			obj.setR52_spec_impairments(rs.getString("R52_SPEC_IMPAIRMENTS"));
			obj.setR52_recoveries(rs.getBigDecimal("R52_RECOVERIES"));
			obj.setR52_write_offs(rs.getBigDecimal("R52_WRITE_OFFS"));
			obj.setR52_new_chars(rs.getBigDecimal("R52_NEW_CHARS"));
			obj.setR52_total(rs.getBigDecimal("R52_TOTAL"));
			obj.setR53_spec_impairments(rs.getString("R53_SPEC_IMPAIRMENTS"));
			obj.setR53_recoveries(rs.getBigDecimal("R53_RECOVERIES"));
			obj.setR53_write_offs(rs.getBigDecimal("R53_WRITE_OFFS"));
			obj.setR53_new_chars(rs.getBigDecimal("R53_NEW_CHARS"));
			obj.setR53_total(rs.getBigDecimal("R53_TOTAL"));
			obj.setR54_spec_impairments(rs.getString("R54_SPEC_IMPAIRMENTS"));
			obj.setR54_recoveries(rs.getBigDecimal("R54_RECOVERIES"));
			obj.setR54_write_offs(rs.getBigDecimal("R54_WRITE_OFFS"));
			obj.setR54_new_chars(rs.getBigDecimal("R54_NEW_CHARS"));
			obj.setR54_total(rs.getBigDecimal("R54_TOTAL"));
			obj.setR55_spec_impairments(rs.getString("R55_SPEC_IMPAIRMENTS"));
			obj.setR55_recoveries(rs.getBigDecimal("R55_RECOVERIES"));
			obj.setR55_write_offs(rs.getBigDecimal("R55_WRITE_OFFS"));
			obj.setR55_new_chars(rs.getBigDecimal("R55_NEW_CHARS"));
			obj.setR55_total(rs.getBigDecimal("R55_TOTAL"));
			obj.setR56_spec_impairments(rs.getString("R56_SPEC_IMPAIRMENTS"));
			obj.setR56_recoveries(rs.getBigDecimal("R56_RECOVERIES"));
			obj.setR56_write_offs(rs.getBigDecimal("R56_WRITE_OFFS"));
			obj.setR56_new_chars(rs.getBigDecimal("R56_NEW_CHARS"));
			obj.setR56_total(rs.getBigDecimal("R56_TOTAL"));
			obj.setR57_spec_impairments(rs.getString("R57_SPEC_IMPAIRMENTS"));
			obj.setR57_recoveries(rs.getBigDecimal("R57_RECOVERIES"));
			obj.setR57_write_offs(rs.getBigDecimal("R57_WRITE_OFFS"));
			obj.setR57_new_chars(rs.getBigDecimal("R57_NEW_CHARS"));
			obj.setR57_total(rs.getBigDecimal("R57_TOTAL"));
			obj.setR58_spec_impairments(rs.getString("R58_SPEC_IMPAIRMENTS"));
			obj.setR58_recoveries(rs.getBigDecimal("R58_RECOVERIES"));
			obj.setR58_write_offs(rs.getBigDecimal("R58_WRITE_OFFS"));
			obj.setR58_new_chars(rs.getBigDecimal("R58_NEW_CHARS"));
			obj.setR58_total(rs.getBigDecimal("R58_TOTAL"));
			obj.setR59_spec_impairments(rs.getString("R59_SPEC_IMPAIRMENTS"));
			obj.setR59_recoveries(rs.getBigDecimal("R59_RECOVERIES"));
			obj.setR59_write_offs(rs.getBigDecimal("R59_WRITE_OFFS"));
			obj.setR59_new_chars(rs.getBigDecimal("R59_NEW_CHARS"));
			obj.setR59_total(rs.getBigDecimal("R59_TOTAL"));
			obj.setR60_spec_impairments(rs.getString("R60_SPEC_IMPAIRMENTS"));
			obj.setR60_recoveries(rs.getBigDecimal("R60_RECOVERIES"));
			obj.setR60_write_offs(rs.getBigDecimal("R60_WRITE_OFFS"));
			obj.setR60_new_chars(rs.getBigDecimal("R60_NEW_CHARS"));
			obj.setR60_total(rs.getBigDecimal("R60_TOTAL"));
			obj.setR61_spec_impairments(rs.getString("R61_SPEC_IMPAIRMENTS"));
			obj.setR61_recoveries(rs.getBigDecimal("R61_RECOVERIES"));
			obj.setR61_write_offs(rs.getBigDecimal("R61_WRITE_OFFS"));
			obj.setR61_new_chars(rs.getBigDecimal("R61_NEW_CHARS"));
			obj.setR61_total(rs.getBigDecimal("R61_TOTAL"));
			obj.setR62_spec_impairments(rs.getString("R62_SPEC_IMPAIRMENTS"));
			obj.setR62_recoveries(rs.getBigDecimal("R62_RECOVERIES"));
			obj.setR62_write_offs(rs.getBigDecimal("R62_WRITE_OFFS"));
			obj.setR62_new_chars(rs.getBigDecimal("R62_NEW_CHARS"));
			obj.setR62_total(rs.getBigDecimal("R62_TOTAL"));
			obj.setR63_spec_impairments(rs.getString("R63_SPEC_IMPAIRMENTS"));
			obj.setR63_recoveries(rs.getBigDecimal("R63_RECOVERIES"));
			obj.setR63_write_offs(rs.getBigDecimal("R63_WRITE_OFFS"));
			obj.setR63_new_chars(rs.getBigDecimal("R63_NEW_CHARS"));
			obj.setR63_total(rs.getBigDecimal("R63_TOTAL"));
			obj.setR64_spec_impairments(rs.getString("R64_SPEC_IMPAIRMENTS"));
			obj.setR64_recoveries(rs.getBigDecimal("R64_RECOVERIES"));
			obj.setR64_write_offs(rs.getBigDecimal("R64_WRITE_OFFS"));
			obj.setR64_new_chars(rs.getBigDecimal("R64_NEW_CHARS"));
			obj.setR64_total(rs.getBigDecimal("R64_TOTAL"));
			obj.setR65_spec_impairments(rs.getString("R65_SPEC_IMPAIRMENTS"));
			obj.setR65_recoveries(rs.getBigDecimal("R65_RECOVERIES"));
			obj.setR65_write_offs(rs.getBigDecimal("R65_WRITE_OFFS"));
			obj.setR65_new_chars(rs.getBigDecimal("R65_NEW_CHARS"));
			obj.setR65_total(rs.getBigDecimal("R65_TOTAL"));
			obj.setR66_spec_impairments(rs.getString("R66_SPEC_IMPAIRMENTS"));
			obj.setR66_recoveries(rs.getBigDecimal("R66_RECOVERIES"));
			obj.setR66_write_offs(rs.getBigDecimal("R66_WRITE_OFFS"));
			obj.setR66_new_chars(rs.getBigDecimal("R66_NEW_CHARS"));
			obj.setR66_total(rs.getBigDecimal("R66_TOTAL"));
			obj.setR67_spec_impairments(rs.getString("R67_SPEC_IMPAIRMENTS"));
			obj.setR67_recoveries(rs.getBigDecimal("R67_RECOVERIES"));
			obj.setR67_write_offs(rs.getBigDecimal("R67_WRITE_OFFS"));
			obj.setR67_new_chars(rs.getBigDecimal("R67_NEW_CHARS"));
			obj.setR67_total(rs.getBigDecimal("R67_TOTAL"));
			obj.setR68_spec_impairments(rs.getString("R68_SPEC_IMPAIRMENTS"));
			obj.setR68_recoveries(rs.getBigDecimal("R68_RECOVERIES"));
			obj.setR68_write_offs(rs.getBigDecimal("R68_WRITE_OFFS"));
			obj.setR68_new_chars(rs.getBigDecimal("R68_NEW_CHARS"));
			obj.setR68_total(rs.getBigDecimal("R68_TOTAL"));
			obj.setR69_spec_impairments(rs.getString("R69_SPEC_IMPAIRMENTS"));
			obj.setR69_recoveries(rs.getBigDecimal("R69_RECOVERIES"));
			obj.setR69_write_offs(rs.getBigDecimal("R69_WRITE_OFFS"));
			obj.setR69_new_chars(rs.getBigDecimal("R69_NEW_CHARS"));
			obj.setR69_total(rs.getBigDecimal("R69_TOTAL"));
			obj.setR70_spec_impairments(rs.getString("R70_SPEC_IMPAIRMENTS"));
			obj.setR70_recoveries(rs.getBigDecimal("R70_RECOVERIES"));
			obj.setR70_write_offs(rs.getBigDecimal("R70_WRITE_OFFS"));
			obj.setR70_new_chars(rs.getBigDecimal("R70_NEW_CHARS"));
			obj.setR70_total(rs.getBigDecimal("R70_TOTAL"));
			return obj;
		}
	}

	class M_I_S_CAArchivalSummaryRowMapper implements RowMapper<M_I_S_CA_Arcihval_Summary_Entity> {
		@Override
		public M_I_S_CA_Arcihval_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_I_S_CA_Arcihval_Summary_Entity obj = new M_I_S_CA_Arcihval_Summary_Entity();
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			obj.setR10_spec_impairments(rs.getString("R10_SPEC_IMPAIRMENTS"));
			obj.setR10_recoveries(rs.getBigDecimal("R10_RECOVERIES"));
			obj.setR10_write_offs(rs.getBigDecimal("R10_WRITE_OFFS"));
			obj.setR10_new_chars(rs.getBigDecimal("R10_NEW_CHARS"));
			obj.setR10_total(rs.getBigDecimal("R10_TOTAL"));
			obj.setR11_spec_impairments(rs.getString("R11_SPEC_IMPAIRMENTS"));
			obj.setR11_recoveries(rs.getBigDecimal("R11_RECOVERIES"));
			obj.setR11_write_offs(rs.getBigDecimal("R11_WRITE_OFFS"));
			obj.setR11_new_chars(rs.getBigDecimal("R11_NEW_CHARS"));
			obj.setR11_total(rs.getBigDecimal("R11_TOTAL"));
			obj.setR12_spec_impairments(rs.getString("R12_SPEC_IMPAIRMENTS"));
			obj.setR12_recoveries(rs.getBigDecimal("R12_RECOVERIES"));
			obj.setR12_write_offs(rs.getBigDecimal("R12_WRITE_OFFS"));
			obj.setR12_new_chars(rs.getBigDecimal("R12_NEW_CHARS"));
			obj.setR12_total(rs.getBigDecimal("R12_TOTAL"));
			obj.setR13_spec_impairments(rs.getString("R13_SPEC_IMPAIRMENTS"));
			obj.setR13_recoveries(rs.getBigDecimal("R13_RECOVERIES"));
			obj.setR13_write_offs(rs.getBigDecimal("R13_WRITE_OFFS"));
			obj.setR13_new_chars(rs.getBigDecimal("R13_NEW_CHARS"));
			obj.setR13_total(rs.getBigDecimal("R13_TOTAL"));
			obj.setR14_spec_impairments(rs.getString("R14_SPEC_IMPAIRMENTS"));
			obj.setR14_recoveries(rs.getBigDecimal("R14_RECOVERIES"));
			obj.setR14_write_offs(rs.getBigDecimal("R14_WRITE_OFFS"));
			obj.setR14_new_chars(rs.getBigDecimal("R14_NEW_CHARS"));
			obj.setR14_total(rs.getBigDecimal("R14_TOTAL"));
			obj.setR15_spec_impairments(rs.getString("R15_SPEC_IMPAIRMENTS"));
			obj.setR15_recoveries(rs.getBigDecimal("R15_RECOVERIES"));
			obj.setR15_write_offs(rs.getBigDecimal("R15_WRITE_OFFS"));
			obj.setR15_new_chars(rs.getBigDecimal("R15_NEW_CHARS"));
			obj.setR15_total(rs.getBigDecimal("R15_TOTAL"));
			obj.setR16_spec_impairments(rs.getString("R16_SPEC_IMPAIRMENTS"));
			obj.setR16_recoveries(rs.getBigDecimal("R16_RECOVERIES"));
			obj.setR16_write_offs(rs.getBigDecimal("R16_WRITE_OFFS"));
			obj.setR16_new_chars(rs.getBigDecimal("R16_NEW_CHARS"));
			obj.setR16_total(rs.getBigDecimal("R16_TOTAL"));
			obj.setR17_spec_impairments(rs.getString("R17_SPEC_IMPAIRMENTS"));
			obj.setR17_recoveries(rs.getBigDecimal("R17_RECOVERIES"));
			obj.setR17_write_offs(rs.getBigDecimal("R17_WRITE_OFFS"));
			obj.setR17_new_chars(rs.getBigDecimal("R17_NEW_CHARS"));
			obj.setR17_total(rs.getBigDecimal("R17_TOTAL"));
			obj.setR18_spec_impairments(rs.getString("R18_SPEC_IMPAIRMENTS"));
			obj.setR18_recoveries(rs.getBigDecimal("R18_RECOVERIES"));
			obj.setR18_write_offs(rs.getBigDecimal("R18_WRITE_OFFS"));
			obj.setR18_new_chars(rs.getBigDecimal("R18_NEW_CHARS"));
			obj.setR18_total(rs.getBigDecimal("R18_TOTAL"));
			obj.setR19_spec_impairments(rs.getString("R19_SPEC_IMPAIRMENTS"));
			obj.setR19_recoveries(rs.getBigDecimal("R19_RECOVERIES"));
			obj.setR19_write_offs(rs.getBigDecimal("R19_WRITE_OFFS"));
			obj.setR19_new_chars(rs.getBigDecimal("R19_NEW_CHARS"));
			obj.setR19_total(rs.getBigDecimal("R19_TOTAL"));
			obj.setR20_spec_impairments(rs.getString("R20_SPEC_IMPAIRMENTS"));
			obj.setR20_recoveries(rs.getBigDecimal("R20_RECOVERIES"));
			obj.setR20_write_offs(rs.getBigDecimal("R20_WRITE_OFFS"));
			obj.setR20_new_chars(rs.getBigDecimal("R20_NEW_CHARS"));
			obj.setR20_total(rs.getBigDecimal("R20_TOTAL"));
			obj.setR21_spec_impairments(rs.getString("R21_SPEC_IMPAIRMENTS"));
			obj.setR21_recoveries(rs.getBigDecimal("R21_RECOVERIES"));
			obj.setR21_write_offs(rs.getBigDecimal("R21_WRITE_OFFS"));
			obj.setR21_new_chars(rs.getBigDecimal("R21_NEW_CHARS"));
			obj.setR21_total(rs.getBigDecimal("R21_TOTAL"));
			obj.setR22_spec_impairments(rs.getString("R22_SPEC_IMPAIRMENTS"));
			obj.setR22_recoveries(rs.getBigDecimal("R22_RECOVERIES"));
			obj.setR22_write_offs(rs.getBigDecimal("R22_WRITE_OFFS"));
			obj.setR22_new_chars(rs.getBigDecimal("R22_NEW_CHARS"));
			obj.setR22_total(rs.getBigDecimal("R22_TOTAL"));
			obj.setR23_spec_impairments(rs.getString("R23_SPEC_IMPAIRMENTS"));
			obj.setR23_recoveries(rs.getBigDecimal("R23_RECOVERIES"));
			obj.setR23_write_offs(rs.getBigDecimal("R23_WRITE_OFFS"));
			obj.setR23_new_chars(rs.getBigDecimal("R23_NEW_CHARS"));
			obj.setR23_total(rs.getBigDecimal("R23_TOTAL"));
			obj.setR24_spec_impairments(rs.getString("R24_SPEC_IMPAIRMENTS"));
			obj.setR24_recoveries(rs.getBigDecimal("R24_RECOVERIES"));
			obj.setR24_write_offs(rs.getBigDecimal("R24_WRITE_OFFS"));
			obj.setR24_new_chars(rs.getBigDecimal("R24_NEW_CHARS"));
			obj.setR24_total(rs.getBigDecimal("R24_TOTAL"));
			obj.setR25_spec_impairments(rs.getString("R25_SPEC_IMPAIRMENTS"));
			obj.setR25_recoveries(rs.getBigDecimal("R25_RECOVERIES"));
			obj.setR25_write_offs(rs.getBigDecimal("R25_WRITE_OFFS"));
			obj.setR25_new_chars(rs.getBigDecimal("R25_NEW_CHARS"));
			obj.setR25_total(rs.getBigDecimal("R25_TOTAL"));
			obj.setR26_spec_impairments(rs.getString("R26_SPEC_IMPAIRMENTS"));
			obj.setR26_recoveries(rs.getBigDecimal("R26_RECOVERIES"));
			obj.setR26_write_offs(rs.getBigDecimal("R26_WRITE_OFFS"));
			obj.setR26_new_chars(rs.getBigDecimal("R26_NEW_CHARS"));
			obj.setR26_total(rs.getBigDecimal("R26_TOTAL"));
			obj.setR27_spec_impairments(rs.getString("R27_SPEC_IMPAIRMENTS"));
			obj.setR27_recoveries(rs.getBigDecimal("R27_RECOVERIES"));
			obj.setR27_write_offs(rs.getBigDecimal("R27_WRITE_OFFS"));
			obj.setR27_new_chars(rs.getBigDecimal("R27_NEW_CHARS"));
			obj.setR27_total(rs.getBigDecimal("R27_TOTAL"));
			obj.setR28_spec_impairments(rs.getString("R28_SPEC_IMPAIRMENTS"));
			obj.setR28_recoveries(rs.getBigDecimal("R28_RECOVERIES"));
			obj.setR28_write_offs(rs.getBigDecimal("R28_WRITE_OFFS"));
			obj.setR28_new_chars(rs.getBigDecimal("R28_NEW_CHARS"));
			obj.setR28_total(rs.getBigDecimal("R28_TOTAL"));
			obj.setR29_spec_impairments(rs.getString("R29_SPEC_IMPAIRMENTS"));
			obj.setR29_recoveries(rs.getBigDecimal("R29_RECOVERIES"));
			obj.setR29_write_offs(rs.getBigDecimal("R29_WRITE_OFFS"));
			obj.setR29_new_chars(rs.getBigDecimal("R29_NEW_CHARS"));
			obj.setR29_total(rs.getBigDecimal("R29_TOTAL"));
			obj.setR30_spec_impairments(rs.getString("R30_SPEC_IMPAIRMENTS"));
			obj.setR30_recoveries(rs.getBigDecimal("R30_RECOVERIES"));
			obj.setR30_write_offs(rs.getBigDecimal("R30_WRITE_OFFS"));
			obj.setR30_new_chars(rs.getBigDecimal("R30_NEW_CHARS"));
			obj.setR30_total(rs.getBigDecimal("R30_TOTAL"));
			obj.setR31_spec_impairments(rs.getString("R31_SPEC_IMPAIRMENTS"));
			obj.setR31_recoveries(rs.getBigDecimal("R31_RECOVERIES"));
			obj.setR31_write_offs(rs.getBigDecimal("R31_WRITE_OFFS"));
			obj.setR31_new_chars(rs.getBigDecimal("R31_NEW_CHARS"));
			obj.setR31_total(rs.getBigDecimal("R31_TOTAL"));
			obj.setR32_spec_impairments(rs.getString("R32_SPEC_IMPAIRMENTS"));
			obj.setR32_recoveries(rs.getBigDecimal("R32_RECOVERIES"));
			obj.setR32_write_offs(rs.getBigDecimal("R32_WRITE_OFFS"));
			obj.setR32_new_chars(rs.getBigDecimal("R32_NEW_CHARS"));
			obj.setR32_total(rs.getBigDecimal("R32_TOTAL"));
			obj.setR33_spec_impairments(rs.getString("R33_SPEC_IMPAIRMENTS"));
			obj.setR33_recoveries(rs.getBigDecimal("R33_RECOVERIES"));
			obj.setR33_write_offs(rs.getBigDecimal("R33_WRITE_OFFS"));
			obj.setR33_new_chars(rs.getBigDecimal("R33_NEW_CHARS"));
			obj.setR33_total(rs.getBigDecimal("R33_TOTAL"));
			obj.setR34_spec_impairments(rs.getString("R34_SPEC_IMPAIRMENTS"));
			obj.setR34_recoveries(rs.getBigDecimal("R34_RECOVERIES"));
			obj.setR34_write_offs(rs.getBigDecimal("R34_WRITE_OFFS"));
			obj.setR34_new_chars(rs.getBigDecimal("R34_NEW_CHARS"));
			obj.setR34_total(rs.getBigDecimal("R34_TOTAL"));
			obj.setR35_spec_impairments(rs.getString("R35_SPEC_IMPAIRMENTS"));
			obj.setR35_recoveries(rs.getBigDecimal("R35_RECOVERIES"));
			obj.setR35_write_offs(rs.getBigDecimal("R35_WRITE_OFFS"));
			obj.setR35_new_chars(rs.getBigDecimal("R35_NEW_CHARS"));
			obj.setR35_total(rs.getBigDecimal("R35_TOTAL"));
			obj.setR36_spec_impairments(rs.getString("R36_SPEC_IMPAIRMENTS"));
			obj.setR36_recoveries(rs.getBigDecimal("R36_RECOVERIES"));
			obj.setR36_write_offs(rs.getBigDecimal("R36_WRITE_OFFS"));
			obj.setR36_new_chars(rs.getBigDecimal("R36_NEW_CHARS"));
			obj.setR36_total(rs.getBigDecimal("R36_TOTAL"));
			obj.setR37_spec_impairments(rs.getString("R37_SPEC_IMPAIRMENTS"));
			obj.setR37_recoveries(rs.getBigDecimal("R37_RECOVERIES"));
			obj.setR37_write_offs(rs.getBigDecimal("R37_WRITE_OFFS"));
			obj.setR37_new_chars(rs.getBigDecimal("R37_NEW_CHARS"));
			obj.setR37_total(rs.getBigDecimal("R37_TOTAL"));
			obj.setR38_spec_impairments(rs.getString("R38_SPEC_IMPAIRMENTS"));
			obj.setR38_recoveries(rs.getBigDecimal("R38_RECOVERIES"));
			obj.setR38_write_offs(rs.getBigDecimal("R38_WRITE_OFFS"));
			obj.setR38_new_chars(rs.getBigDecimal("R38_NEW_CHARS"));
			obj.setR38_total(rs.getBigDecimal("R38_TOTAL"));
			obj.setR39_spec_impairments(rs.getString("R39_SPEC_IMPAIRMENTS"));
			obj.setR39_recoveries(rs.getBigDecimal("R39_RECOVERIES"));
			obj.setR39_write_offs(rs.getBigDecimal("R39_WRITE_OFFS"));
			obj.setR39_new_chars(rs.getBigDecimal("R39_NEW_CHARS"));
			obj.setR39_total(rs.getBigDecimal("R39_TOTAL"));
			obj.setR40_spec_impairments(rs.getString("R40_SPEC_IMPAIRMENTS"));
			obj.setR40_recoveries(rs.getBigDecimal("R40_RECOVERIES"));
			obj.setR40_write_offs(rs.getBigDecimal("R40_WRITE_OFFS"));
			obj.setR40_new_chars(rs.getBigDecimal("R40_NEW_CHARS"));
			obj.setR40_total(rs.getBigDecimal("R40_TOTAL"));
			obj.setR41_spec_impairments(rs.getString("R41_SPEC_IMPAIRMENTS"));
			obj.setR41_recoveries(rs.getBigDecimal("R41_RECOVERIES"));
			obj.setR41_write_offs(rs.getBigDecimal("R41_WRITE_OFFS"));
			obj.setR41_new_chars(rs.getBigDecimal("R41_NEW_CHARS"));
			obj.setR41_total(rs.getBigDecimal("R41_TOTAL"));
			obj.setR42_spec_impairments(rs.getString("R42_SPEC_IMPAIRMENTS"));
			obj.setR42_recoveries(rs.getBigDecimal("R42_RECOVERIES"));
			obj.setR42_write_offs(rs.getBigDecimal("R42_WRITE_OFFS"));
			obj.setR42_new_chars(rs.getBigDecimal("R42_NEW_CHARS"));
			obj.setR42_total(rs.getBigDecimal("R42_TOTAL"));
			obj.setR43_spec_impairments(rs.getString("R43_SPEC_IMPAIRMENTS"));
			obj.setR43_recoveries(rs.getBigDecimal("R43_RECOVERIES"));
			obj.setR43_write_offs(rs.getBigDecimal("R43_WRITE_OFFS"));
			obj.setR43_new_chars(rs.getBigDecimal("R43_NEW_CHARS"));
			obj.setR43_total(rs.getBigDecimal("R43_TOTAL"));
			obj.setR44_spec_impairments(rs.getString("R44_SPEC_IMPAIRMENTS"));
			obj.setR44_recoveries(rs.getBigDecimal("R44_RECOVERIES"));
			obj.setR44_write_offs(rs.getBigDecimal("R44_WRITE_OFFS"));
			obj.setR44_new_chars(rs.getBigDecimal("R44_NEW_CHARS"));
			obj.setR44_total(rs.getBigDecimal("R44_TOTAL"));
			obj.setR45_spec_impairments(rs.getString("R45_SPEC_IMPAIRMENTS"));
			obj.setR45_recoveries(rs.getBigDecimal("R45_RECOVERIES"));
			obj.setR45_write_offs(rs.getBigDecimal("R45_WRITE_OFFS"));
			obj.setR45_new_chars(rs.getBigDecimal("R45_NEW_CHARS"));
			obj.setR45_total(rs.getBigDecimal("R45_TOTAL"));
			obj.setR46_spec_impairments(rs.getString("R46_SPEC_IMPAIRMENTS"));
			obj.setR46_recoveries(rs.getBigDecimal("R46_RECOVERIES"));
			obj.setR46_write_offs(rs.getBigDecimal("R46_WRITE_OFFS"));
			obj.setR46_new_chars(rs.getBigDecimal("R46_NEW_CHARS"));
			obj.setR46_total(rs.getBigDecimal("R46_TOTAL"));
			obj.setR47_spec_impairments(rs.getString("R47_SPEC_IMPAIRMENTS"));
			obj.setR47_recoveries(rs.getBigDecimal("R47_RECOVERIES"));
			obj.setR47_write_offs(rs.getBigDecimal("R47_WRITE_OFFS"));
			obj.setR47_new_chars(rs.getBigDecimal("R47_NEW_CHARS"));
			obj.setR47_total(rs.getBigDecimal("R47_TOTAL"));
			obj.setR48_spec_impairments(rs.getString("R48_SPEC_IMPAIRMENTS"));
			obj.setR48_recoveries(rs.getBigDecimal("R48_RECOVERIES"));
			obj.setR48_write_offs(rs.getBigDecimal("R48_WRITE_OFFS"));
			obj.setR48_new_chars(rs.getBigDecimal("R48_NEW_CHARS"));
			obj.setR48_total(rs.getBigDecimal("R48_TOTAL"));
			obj.setR49_spec_impairments(rs.getString("R49_SPEC_IMPAIRMENTS"));
			obj.setR49_recoveries(rs.getBigDecimal("R49_RECOVERIES"));
			obj.setR49_write_offs(rs.getBigDecimal("R49_WRITE_OFFS"));
			obj.setR49_new_chars(rs.getBigDecimal("R49_NEW_CHARS"));
			obj.setR49_total(rs.getBigDecimal("R49_TOTAL"));
			obj.setR50_spec_impairments(rs.getString("R50_SPEC_IMPAIRMENTS"));
			obj.setR50_recoveries(rs.getBigDecimal("R50_RECOVERIES"));
			obj.setR50_write_offs(rs.getBigDecimal("R50_WRITE_OFFS"));
			obj.setR50_new_chars(rs.getBigDecimal("R50_NEW_CHARS"));
			obj.setR50_total(rs.getBigDecimal("R50_TOTAL"));
			obj.setR51_spec_impairments(rs.getString("R51_SPEC_IMPAIRMENTS"));
			obj.setR51_recoveries(rs.getBigDecimal("R51_RECOVERIES"));
			obj.setR51_write_offs(rs.getBigDecimal("R51_WRITE_OFFS"));
			obj.setR51_new_chars(rs.getBigDecimal("R51_NEW_CHARS"));
			obj.setR51_total(rs.getBigDecimal("R51_TOTAL"));
			obj.setR52_spec_impairments(rs.getString("R52_SPEC_IMPAIRMENTS"));
			obj.setR52_recoveries(rs.getBigDecimal("R52_RECOVERIES"));
			obj.setR52_write_offs(rs.getBigDecimal("R52_WRITE_OFFS"));
			obj.setR52_new_chars(rs.getBigDecimal("R52_NEW_CHARS"));
			obj.setR52_total(rs.getBigDecimal("R52_TOTAL"));
			obj.setR53_spec_impairments(rs.getString("R53_SPEC_IMPAIRMENTS"));
			obj.setR53_recoveries(rs.getBigDecimal("R53_RECOVERIES"));
			obj.setR53_write_offs(rs.getBigDecimal("R53_WRITE_OFFS"));
			obj.setR53_new_chars(rs.getBigDecimal("R53_NEW_CHARS"));
			obj.setR53_total(rs.getBigDecimal("R53_TOTAL"));
			obj.setR54_spec_impairments(rs.getString("R54_SPEC_IMPAIRMENTS"));
			obj.setR54_recoveries(rs.getBigDecimal("R54_RECOVERIES"));
			obj.setR54_write_offs(rs.getBigDecimal("R54_WRITE_OFFS"));
			obj.setR54_new_chars(rs.getBigDecimal("R54_NEW_CHARS"));
			obj.setR54_total(rs.getBigDecimal("R54_TOTAL"));
			obj.setR55_spec_impairments(rs.getString("R55_SPEC_IMPAIRMENTS"));
			obj.setR55_recoveries(rs.getBigDecimal("R55_RECOVERIES"));
			obj.setR55_write_offs(rs.getBigDecimal("R55_WRITE_OFFS"));
			obj.setR55_new_chars(rs.getBigDecimal("R55_NEW_CHARS"));
			obj.setR55_total(rs.getBigDecimal("R55_TOTAL"));
			obj.setR56_spec_impairments(rs.getString("R56_SPEC_IMPAIRMENTS"));
			obj.setR56_recoveries(rs.getBigDecimal("R56_RECOVERIES"));
			obj.setR56_write_offs(rs.getBigDecimal("R56_WRITE_OFFS"));
			obj.setR56_new_chars(rs.getBigDecimal("R56_NEW_CHARS"));
			obj.setR56_total(rs.getBigDecimal("R56_TOTAL"));
			obj.setR57_spec_impairments(rs.getString("R57_SPEC_IMPAIRMENTS"));
			obj.setR57_recoveries(rs.getBigDecimal("R57_RECOVERIES"));
			obj.setR57_write_offs(rs.getBigDecimal("R57_WRITE_OFFS"));
			obj.setR57_new_chars(rs.getBigDecimal("R57_NEW_CHARS"));
			obj.setR57_total(rs.getBigDecimal("R57_TOTAL"));
			obj.setR58_spec_impairments(rs.getString("R58_SPEC_IMPAIRMENTS"));
			obj.setR58_recoveries(rs.getBigDecimal("R58_RECOVERIES"));
			obj.setR58_write_offs(rs.getBigDecimal("R58_WRITE_OFFS"));
			obj.setR58_new_chars(rs.getBigDecimal("R58_NEW_CHARS"));
			obj.setR58_total(rs.getBigDecimal("R58_TOTAL"));
			obj.setR59_spec_impairments(rs.getString("R59_SPEC_IMPAIRMENTS"));
			obj.setR59_recoveries(rs.getBigDecimal("R59_RECOVERIES"));
			obj.setR59_write_offs(rs.getBigDecimal("R59_WRITE_OFFS"));
			obj.setR59_new_chars(rs.getBigDecimal("R59_NEW_CHARS"));
			obj.setR59_total(rs.getBigDecimal("R59_TOTAL"));
			obj.setR60_spec_impairments(rs.getString("R60_SPEC_IMPAIRMENTS"));
			obj.setR60_recoveries(rs.getBigDecimal("R60_RECOVERIES"));
			obj.setR60_write_offs(rs.getBigDecimal("R60_WRITE_OFFS"));
			obj.setR60_new_chars(rs.getBigDecimal("R60_NEW_CHARS"));
			obj.setR60_total(rs.getBigDecimal("R60_TOTAL"));
			obj.setR61_spec_impairments(rs.getString("R61_SPEC_IMPAIRMENTS"));
			obj.setR61_recoveries(rs.getBigDecimal("R61_RECOVERIES"));
			obj.setR61_write_offs(rs.getBigDecimal("R61_WRITE_OFFS"));
			obj.setR61_new_chars(rs.getBigDecimal("R61_NEW_CHARS"));
			obj.setR61_total(rs.getBigDecimal("R61_TOTAL"));
			obj.setR62_spec_impairments(rs.getString("R62_SPEC_IMPAIRMENTS"));
			obj.setR62_recoveries(rs.getBigDecimal("R62_RECOVERIES"));
			obj.setR62_write_offs(rs.getBigDecimal("R62_WRITE_OFFS"));
			obj.setR62_new_chars(rs.getBigDecimal("R62_NEW_CHARS"));
			obj.setR62_total(rs.getBigDecimal("R62_TOTAL"));
			obj.setR63_spec_impairments(rs.getString("R63_SPEC_IMPAIRMENTS"));
			obj.setR63_recoveries(rs.getBigDecimal("R63_RECOVERIES"));
			obj.setR63_write_offs(rs.getBigDecimal("R63_WRITE_OFFS"));
			obj.setR63_new_chars(rs.getBigDecimal("R63_NEW_CHARS"));
			obj.setR63_total(rs.getBigDecimal("R63_TOTAL"));
			obj.setR64_spec_impairments(rs.getString("R64_SPEC_IMPAIRMENTS"));
			obj.setR64_recoveries(rs.getBigDecimal("R64_RECOVERIES"));
			obj.setR64_write_offs(rs.getBigDecimal("R64_WRITE_OFFS"));
			obj.setR64_new_chars(rs.getBigDecimal("R64_NEW_CHARS"));
			obj.setR64_total(rs.getBigDecimal("R64_TOTAL"));
			obj.setR65_spec_impairments(rs.getString("R65_SPEC_IMPAIRMENTS"));
			obj.setR65_recoveries(rs.getBigDecimal("R65_RECOVERIES"));
			obj.setR65_write_offs(rs.getBigDecimal("R65_WRITE_OFFS"));
			obj.setR65_new_chars(rs.getBigDecimal("R65_NEW_CHARS"));
			obj.setR65_total(rs.getBigDecimal("R65_TOTAL"));
			obj.setR66_spec_impairments(rs.getString("R66_SPEC_IMPAIRMENTS"));
			obj.setR66_recoveries(rs.getBigDecimal("R66_RECOVERIES"));
			obj.setR66_write_offs(rs.getBigDecimal("R66_WRITE_OFFS"));
			obj.setR66_new_chars(rs.getBigDecimal("R66_NEW_CHARS"));
			obj.setR66_total(rs.getBigDecimal("R66_TOTAL"));
			obj.setR67_spec_impairments(rs.getString("R67_SPEC_IMPAIRMENTS"));
			obj.setR67_recoveries(rs.getBigDecimal("R67_RECOVERIES"));
			obj.setR67_write_offs(rs.getBigDecimal("R67_WRITE_OFFS"));
			obj.setR67_new_chars(rs.getBigDecimal("R67_NEW_CHARS"));
			obj.setR67_total(rs.getBigDecimal("R67_TOTAL"));
			obj.setR68_spec_impairments(rs.getString("R68_SPEC_IMPAIRMENTS"));
			obj.setR68_recoveries(rs.getBigDecimal("R68_RECOVERIES"));
			obj.setR68_write_offs(rs.getBigDecimal("R68_WRITE_OFFS"));
			obj.setR68_new_chars(rs.getBigDecimal("R68_NEW_CHARS"));
			obj.setR68_total(rs.getBigDecimal("R68_TOTAL"));
			obj.setR69_spec_impairments(rs.getString("R69_SPEC_IMPAIRMENTS"));
			obj.setR69_recoveries(rs.getBigDecimal("R69_RECOVERIES"));
			obj.setR69_write_offs(rs.getBigDecimal("R69_WRITE_OFFS"));
			obj.setR69_new_chars(rs.getBigDecimal("R69_NEW_CHARS"));
			obj.setR69_total(rs.getBigDecimal("R69_TOTAL"));
			obj.setR70_spec_impairments(rs.getString("R70_SPEC_IMPAIRMENTS"));
			obj.setR70_recoveries(rs.getBigDecimal("R70_RECOVERIES"));
			obj.setR70_write_offs(rs.getBigDecimal("R70_WRITE_OFFS"));
			obj.setR70_new_chars(rs.getBigDecimal("R70_NEW_CHARS"));
			obj.setR70_total(rs.getBigDecimal("R70_TOTAL"));
			return obj;
		}
	}

	class M_I_S_CAResubSummaryRowMapper implements RowMapper<M_I_S_CA_RESUB_Summary_Entity> {
		@Override
		public M_I_S_CA_RESUB_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_I_S_CA_RESUB_Summary_Entity obj = new M_I_S_CA_RESUB_Summary_Entity();
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			obj.setR10_spec_impairments(rs.getString("R10_SPEC_IMPAIRMENTS"));
			obj.setR10_recoveries(rs.getBigDecimal("R10_RECOVERIES"));
			obj.setR10_write_offs(rs.getBigDecimal("R10_WRITE_OFFS"));
			obj.setR10_new_chars(rs.getBigDecimal("R10_NEW_CHARS"));
			obj.setR10_total(rs.getBigDecimal("R10_TOTAL"));
			obj.setR11_spec_impairments(rs.getString("R11_SPEC_IMPAIRMENTS"));
			obj.setR11_recoveries(rs.getBigDecimal("R11_RECOVERIES"));
			obj.setR11_write_offs(rs.getBigDecimal("R11_WRITE_OFFS"));
			obj.setR11_new_chars(rs.getBigDecimal("R11_NEW_CHARS"));
			obj.setR11_total(rs.getBigDecimal("R11_TOTAL"));
			obj.setR12_spec_impairments(rs.getString("R12_SPEC_IMPAIRMENTS"));
			obj.setR12_recoveries(rs.getBigDecimal("R12_RECOVERIES"));
			obj.setR12_write_offs(rs.getBigDecimal("R12_WRITE_OFFS"));
			obj.setR12_new_chars(rs.getBigDecimal("R12_NEW_CHARS"));
			obj.setR12_total(rs.getBigDecimal("R12_TOTAL"));
			obj.setR13_spec_impairments(rs.getString("R13_SPEC_IMPAIRMENTS"));
			obj.setR13_recoveries(rs.getBigDecimal("R13_RECOVERIES"));
			obj.setR13_write_offs(rs.getBigDecimal("R13_WRITE_OFFS"));
			obj.setR13_new_chars(rs.getBigDecimal("R13_NEW_CHARS"));
			obj.setR13_total(rs.getBigDecimal("R13_TOTAL"));
			obj.setR14_spec_impairments(rs.getString("R14_SPEC_IMPAIRMENTS"));
			obj.setR14_recoveries(rs.getBigDecimal("R14_RECOVERIES"));
			obj.setR14_write_offs(rs.getBigDecimal("R14_WRITE_OFFS"));
			obj.setR14_new_chars(rs.getBigDecimal("R14_NEW_CHARS"));
			obj.setR14_total(rs.getBigDecimal("R14_TOTAL"));
			obj.setR15_spec_impairments(rs.getString("R15_SPEC_IMPAIRMENTS"));
			obj.setR15_recoveries(rs.getBigDecimal("R15_RECOVERIES"));
			obj.setR15_write_offs(rs.getBigDecimal("R15_WRITE_OFFS"));
			obj.setR15_new_chars(rs.getBigDecimal("R15_NEW_CHARS"));
			obj.setR15_total(rs.getBigDecimal("R15_TOTAL"));
			obj.setR16_spec_impairments(rs.getString("R16_SPEC_IMPAIRMENTS"));
			obj.setR16_recoveries(rs.getBigDecimal("R16_RECOVERIES"));
			obj.setR16_write_offs(rs.getBigDecimal("R16_WRITE_OFFS"));
			obj.setR16_new_chars(rs.getBigDecimal("R16_NEW_CHARS"));
			obj.setR16_total(rs.getBigDecimal("R16_TOTAL"));
			obj.setR17_spec_impairments(rs.getString("R17_SPEC_IMPAIRMENTS"));
			obj.setR17_recoveries(rs.getBigDecimal("R17_RECOVERIES"));
			obj.setR17_write_offs(rs.getBigDecimal("R17_WRITE_OFFS"));
			obj.setR17_new_chars(rs.getBigDecimal("R17_NEW_CHARS"));
			obj.setR17_total(rs.getBigDecimal("R17_TOTAL"));
			obj.setR18_spec_impairments(rs.getString("R18_SPEC_IMPAIRMENTS"));
			obj.setR18_recoveries(rs.getBigDecimal("R18_RECOVERIES"));
			obj.setR18_write_offs(rs.getBigDecimal("R18_WRITE_OFFS"));
			obj.setR18_new_chars(rs.getBigDecimal("R18_NEW_CHARS"));
			obj.setR18_total(rs.getBigDecimal("R18_TOTAL"));
			obj.setR19_spec_impairments(rs.getString("R19_SPEC_IMPAIRMENTS"));
			obj.setR19_recoveries(rs.getBigDecimal("R19_RECOVERIES"));
			obj.setR19_write_offs(rs.getBigDecimal("R19_WRITE_OFFS"));
			obj.setR19_new_chars(rs.getBigDecimal("R19_NEW_CHARS"));
			obj.setR19_total(rs.getBigDecimal("R19_TOTAL"));
			obj.setR20_spec_impairments(rs.getString("R20_SPEC_IMPAIRMENTS"));
			obj.setR20_recoveries(rs.getBigDecimal("R20_RECOVERIES"));
			obj.setR20_write_offs(rs.getBigDecimal("R20_WRITE_OFFS"));
			obj.setR20_new_chars(rs.getBigDecimal("R20_NEW_CHARS"));
			obj.setR20_total(rs.getBigDecimal("R20_TOTAL"));
			obj.setR21_spec_impairments(rs.getString("R21_SPEC_IMPAIRMENTS"));
			obj.setR21_recoveries(rs.getBigDecimal("R21_RECOVERIES"));
			obj.setR21_write_offs(rs.getBigDecimal("R21_WRITE_OFFS"));
			obj.setR21_new_chars(rs.getBigDecimal("R21_NEW_CHARS"));
			obj.setR21_total(rs.getBigDecimal("R21_TOTAL"));
			obj.setR22_spec_impairments(rs.getString("R22_SPEC_IMPAIRMENTS"));
			obj.setR22_recoveries(rs.getBigDecimal("R22_RECOVERIES"));
			obj.setR22_write_offs(rs.getBigDecimal("R22_WRITE_OFFS"));
			obj.setR22_new_chars(rs.getBigDecimal("R22_NEW_CHARS"));
			obj.setR22_total(rs.getBigDecimal("R22_TOTAL"));
			obj.setR23_spec_impairments(rs.getString("R23_SPEC_IMPAIRMENTS"));
			obj.setR23_recoveries(rs.getBigDecimal("R23_RECOVERIES"));
			obj.setR23_write_offs(rs.getBigDecimal("R23_WRITE_OFFS"));
			obj.setR23_new_chars(rs.getBigDecimal("R23_NEW_CHARS"));
			obj.setR23_total(rs.getBigDecimal("R23_TOTAL"));
			obj.setR24_spec_impairments(rs.getString("R24_SPEC_IMPAIRMENTS"));
			obj.setR24_recoveries(rs.getBigDecimal("R24_RECOVERIES"));
			obj.setR24_write_offs(rs.getBigDecimal("R24_WRITE_OFFS"));
			obj.setR24_new_chars(rs.getBigDecimal("R24_NEW_CHARS"));
			obj.setR24_total(rs.getBigDecimal("R24_TOTAL"));
			obj.setR25_spec_impairments(rs.getString("R25_SPEC_IMPAIRMENTS"));
			obj.setR25_recoveries(rs.getBigDecimal("R25_RECOVERIES"));
			obj.setR25_write_offs(rs.getBigDecimal("R25_WRITE_OFFS"));
			obj.setR25_new_chars(rs.getBigDecimal("R25_NEW_CHARS"));
			obj.setR25_total(rs.getBigDecimal("R25_TOTAL"));
			obj.setR26_spec_impairments(rs.getString("R26_SPEC_IMPAIRMENTS"));
			obj.setR26_recoveries(rs.getBigDecimal("R26_RECOVERIES"));
			obj.setR26_write_offs(rs.getBigDecimal("R26_WRITE_OFFS"));
			obj.setR26_new_chars(rs.getBigDecimal("R26_NEW_CHARS"));
			obj.setR26_total(rs.getBigDecimal("R26_TOTAL"));
			obj.setR27_spec_impairments(rs.getString("R27_SPEC_IMPAIRMENTS"));
			obj.setR27_recoveries(rs.getBigDecimal("R27_RECOVERIES"));
			obj.setR27_write_offs(rs.getBigDecimal("R27_WRITE_OFFS"));
			obj.setR27_new_chars(rs.getBigDecimal("R27_NEW_CHARS"));
			obj.setR27_total(rs.getBigDecimal("R27_TOTAL"));
			obj.setR28_spec_impairments(rs.getString("R28_SPEC_IMPAIRMENTS"));
			obj.setR28_recoveries(rs.getBigDecimal("R28_RECOVERIES"));
			obj.setR28_write_offs(rs.getBigDecimal("R28_WRITE_OFFS"));
			obj.setR28_new_chars(rs.getBigDecimal("R28_NEW_CHARS"));
			obj.setR28_total(rs.getBigDecimal("R28_TOTAL"));
			obj.setR29_spec_impairments(rs.getString("R29_SPEC_IMPAIRMENTS"));
			obj.setR29_recoveries(rs.getBigDecimal("R29_RECOVERIES"));
			obj.setR29_write_offs(rs.getBigDecimal("R29_WRITE_OFFS"));
			obj.setR29_new_chars(rs.getBigDecimal("R29_NEW_CHARS"));
			obj.setR29_total(rs.getBigDecimal("R29_TOTAL"));
			obj.setR30_spec_impairments(rs.getString("R30_SPEC_IMPAIRMENTS"));
			obj.setR30_recoveries(rs.getBigDecimal("R30_RECOVERIES"));
			obj.setR30_write_offs(rs.getBigDecimal("R30_WRITE_OFFS"));
			obj.setR30_new_chars(rs.getBigDecimal("R30_NEW_CHARS"));
			obj.setR30_total(rs.getBigDecimal("R30_TOTAL"));
			obj.setR31_spec_impairments(rs.getString("R31_SPEC_IMPAIRMENTS"));
			obj.setR31_recoveries(rs.getBigDecimal("R31_RECOVERIES"));
			obj.setR31_write_offs(rs.getBigDecimal("R31_WRITE_OFFS"));
			obj.setR31_new_chars(rs.getBigDecimal("R31_NEW_CHARS"));
			obj.setR31_total(rs.getBigDecimal("R31_TOTAL"));
			obj.setR32_spec_impairments(rs.getString("R32_SPEC_IMPAIRMENTS"));
			obj.setR32_recoveries(rs.getBigDecimal("R32_RECOVERIES"));
			obj.setR32_write_offs(rs.getBigDecimal("R32_WRITE_OFFS"));
			obj.setR32_new_chars(rs.getBigDecimal("R32_NEW_CHARS"));
			obj.setR32_total(rs.getBigDecimal("R32_TOTAL"));
			obj.setR33_spec_impairments(rs.getString("R33_SPEC_IMPAIRMENTS"));
			obj.setR33_recoveries(rs.getBigDecimal("R33_RECOVERIES"));
			obj.setR33_write_offs(rs.getBigDecimal("R33_WRITE_OFFS"));
			obj.setR33_new_chars(rs.getBigDecimal("R33_NEW_CHARS"));
			obj.setR33_total(rs.getBigDecimal("R33_TOTAL"));
			obj.setR34_spec_impairments(rs.getString("R34_SPEC_IMPAIRMENTS"));
			obj.setR34_recoveries(rs.getBigDecimal("R34_RECOVERIES"));
			obj.setR34_write_offs(rs.getBigDecimal("R34_WRITE_OFFS"));
			obj.setR34_new_chars(rs.getBigDecimal("R34_NEW_CHARS"));
			obj.setR34_total(rs.getBigDecimal("R34_TOTAL"));
			obj.setR35_spec_impairments(rs.getString("R35_SPEC_IMPAIRMENTS"));
			obj.setR35_recoveries(rs.getBigDecimal("R35_RECOVERIES"));
			obj.setR35_write_offs(rs.getBigDecimal("R35_WRITE_OFFS"));
			obj.setR35_new_chars(rs.getBigDecimal("R35_NEW_CHARS"));
			obj.setR35_total(rs.getBigDecimal("R35_TOTAL"));
			obj.setR36_spec_impairments(rs.getString("R36_SPEC_IMPAIRMENTS"));
			obj.setR36_recoveries(rs.getBigDecimal("R36_RECOVERIES"));
			obj.setR36_write_offs(rs.getBigDecimal("R36_WRITE_OFFS"));
			obj.setR36_new_chars(rs.getBigDecimal("R36_NEW_CHARS"));
			obj.setR36_total(rs.getBigDecimal("R36_TOTAL"));
			obj.setR37_spec_impairments(rs.getString("R37_SPEC_IMPAIRMENTS"));
			obj.setR37_recoveries(rs.getBigDecimal("R37_RECOVERIES"));
			obj.setR37_write_offs(rs.getBigDecimal("R37_WRITE_OFFS"));
			obj.setR37_new_chars(rs.getBigDecimal("R37_NEW_CHARS"));
			obj.setR37_total(rs.getBigDecimal("R37_TOTAL"));
			obj.setR38_spec_impairments(rs.getString("R38_SPEC_IMPAIRMENTS"));
			obj.setR38_recoveries(rs.getBigDecimal("R38_RECOVERIES"));
			obj.setR38_write_offs(rs.getBigDecimal("R38_WRITE_OFFS"));
			obj.setR38_new_chars(rs.getBigDecimal("R38_NEW_CHARS"));
			obj.setR38_total(rs.getBigDecimal("R38_TOTAL"));
			obj.setR39_spec_impairments(rs.getString("R39_SPEC_IMPAIRMENTS"));
			obj.setR39_recoveries(rs.getBigDecimal("R39_RECOVERIES"));
			obj.setR39_write_offs(rs.getBigDecimal("R39_WRITE_OFFS"));
			obj.setR39_new_chars(rs.getBigDecimal("R39_NEW_CHARS"));
			obj.setR39_total(rs.getBigDecimal("R39_TOTAL"));
			obj.setR40_spec_impairments(rs.getString("R40_SPEC_IMPAIRMENTS"));
			obj.setR40_recoveries(rs.getBigDecimal("R40_RECOVERIES"));
			obj.setR40_write_offs(rs.getBigDecimal("R40_WRITE_OFFS"));
			obj.setR40_new_chars(rs.getBigDecimal("R40_NEW_CHARS"));
			obj.setR40_total(rs.getBigDecimal("R40_TOTAL"));
			obj.setR41_spec_impairments(rs.getString("R41_SPEC_IMPAIRMENTS"));
			obj.setR41_recoveries(rs.getBigDecimal("R41_RECOVERIES"));
			obj.setR41_write_offs(rs.getBigDecimal("R41_WRITE_OFFS"));
			obj.setR41_new_chars(rs.getBigDecimal("R41_NEW_CHARS"));
			obj.setR41_total(rs.getBigDecimal("R41_TOTAL"));
			obj.setR42_spec_impairments(rs.getString("R42_SPEC_IMPAIRMENTS"));
			obj.setR42_recoveries(rs.getBigDecimal("R42_RECOVERIES"));
			obj.setR42_write_offs(rs.getBigDecimal("R42_WRITE_OFFS"));
			obj.setR42_new_chars(rs.getBigDecimal("R42_NEW_CHARS"));
			obj.setR42_total(rs.getBigDecimal("R42_TOTAL"));
			obj.setR43_spec_impairments(rs.getString("R43_SPEC_IMPAIRMENTS"));
			obj.setR43_recoveries(rs.getBigDecimal("R43_RECOVERIES"));
			obj.setR43_write_offs(rs.getBigDecimal("R43_WRITE_OFFS"));
			obj.setR43_new_chars(rs.getBigDecimal("R43_NEW_CHARS"));
			obj.setR43_total(rs.getBigDecimal("R43_TOTAL"));
			obj.setR44_spec_impairments(rs.getString("R44_SPEC_IMPAIRMENTS"));
			obj.setR44_recoveries(rs.getBigDecimal("R44_RECOVERIES"));
			obj.setR44_write_offs(rs.getBigDecimal("R44_WRITE_OFFS"));
			obj.setR44_new_chars(rs.getBigDecimal("R44_NEW_CHARS"));
			obj.setR44_total(rs.getBigDecimal("R44_TOTAL"));
			obj.setR45_spec_impairments(rs.getString("R45_SPEC_IMPAIRMENTS"));
			obj.setR45_recoveries(rs.getBigDecimal("R45_RECOVERIES"));
			obj.setR45_write_offs(rs.getBigDecimal("R45_WRITE_OFFS"));
			obj.setR45_new_chars(rs.getBigDecimal("R45_NEW_CHARS"));
			obj.setR45_total(rs.getBigDecimal("R45_TOTAL"));
			obj.setR46_spec_impairments(rs.getString("R46_SPEC_IMPAIRMENTS"));
			obj.setR46_recoveries(rs.getBigDecimal("R46_RECOVERIES"));
			obj.setR46_write_offs(rs.getBigDecimal("R46_WRITE_OFFS"));
			obj.setR46_new_chars(rs.getBigDecimal("R46_NEW_CHARS"));
			obj.setR46_total(rs.getBigDecimal("R46_TOTAL"));
			obj.setR47_spec_impairments(rs.getString("R47_SPEC_IMPAIRMENTS"));
			obj.setR47_recoveries(rs.getBigDecimal("R47_RECOVERIES"));
			obj.setR47_write_offs(rs.getBigDecimal("R47_WRITE_OFFS"));
			obj.setR47_new_chars(rs.getBigDecimal("R47_NEW_CHARS"));
			obj.setR47_total(rs.getBigDecimal("R47_TOTAL"));
			obj.setR48_spec_impairments(rs.getString("R48_SPEC_IMPAIRMENTS"));
			obj.setR48_recoveries(rs.getBigDecimal("R48_RECOVERIES"));
			obj.setR48_write_offs(rs.getBigDecimal("R48_WRITE_OFFS"));
			obj.setR48_new_chars(rs.getBigDecimal("R48_NEW_CHARS"));
			obj.setR48_total(rs.getBigDecimal("R48_TOTAL"));
			obj.setR49_spec_impairments(rs.getString("R49_SPEC_IMPAIRMENTS"));
			obj.setR49_recoveries(rs.getBigDecimal("R49_RECOVERIES"));
			obj.setR49_write_offs(rs.getBigDecimal("R49_WRITE_OFFS"));
			obj.setR49_new_chars(rs.getBigDecimal("R49_NEW_CHARS"));
			obj.setR49_total(rs.getBigDecimal("R49_TOTAL"));
			obj.setR50_spec_impairments(rs.getString("R50_SPEC_IMPAIRMENTS"));
			obj.setR50_recoveries(rs.getBigDecimal("R50_RECOVERIES"));
			obj.setR50_write_offs(rs.getBigDecimal("R50_WRITE_OFFS"));
			obj.setR50_new_chars(rs.getBigDecimal("R50_NEW_CHARS"));
			obj.setR50_total(rs.getBigDecimal("R50_TOTAL"));
			obj.setR51_spec_impairments(rs.getString("R51_SPEC_IMPAIRMENTS"));
			obj.setR51_recoveries(rs.getBigDecimal("R51_RECOVERIES"));
			obj.setR51_write_offs(rs.getBigDecimal("R51_WRITE_OFFS"));
			obj.setR51_new_chars(rs.getBigDecimal("R51_NEW_CHARS"));
			obj.setR51_total(rs.getBigDecimal("R51_TOTAL"));
			obj.setR52_spec_impairments(rs.getString("R52_SPEC_IMPAIRMENTS"));
			obj.setR52_recoveries(rs.getBigDecimal("R52_RECOVERIES"));
			obj.setR52_write_offs(rs.getBigDecimal("R52_WRITE_OFFS"));
			obj.setR52_new_chars(rs.getBigDecimal("R52_NEW_CHARS"));
			obj.setR52_total(rs.getBigDecimal("R52_TOTAL"));
			obj.setR53_spec_impairments(rs.getString("R53_SPEC_IMPAIRMENTS"));
			obj.setR53_recoveries(rs.getBigDecimal("R53_RECOVERIES"));
			obj.setR53_write_offs(rs.getBigDecimal("R53_WRITE_OFFS"));
			obj.setR53_new_chars(rs.getBigDecimal("R53_NEW_CHARS"));
			obj.setR53_total(rs.getBigDecimal("R53_TOTAL"));
			obj.setR54_spec_impairments(rs.getString("R54_SPEC_IMPAIRMENTS"));
			obj.setR54_recoveries(rs.getBigDecimal("R54_RECOVERIES"));
			obj.setR54_write_offs(rs.getBigDecimal("R54_WRITE_OFFS"));
			obj.setR54_new_chars(rs.getBigDecimal("R54_NEW_CHARS"));
			obj.setR54_total(rs.getBigDecimal("R54_TOTAL"));
			obj.setR55_spec_impairments(rs.getString("R55_SPEC_IMPAIRMENTS"));
			obj.setR55_recoveries(rs.getBigDecimal("R55_RECOVERIES"));
			obj.setR55_write_offs(rs.getBigDecimal("R55_WRITE_OFFS"));
			obj.setR55_new_chars(rs.getBigDecimal("R55_NEW_CHARS"));
			obj.setR55_total(rs.getBigDecimal("R55_TOTAL"));
			obj.setR56_spec_impairments(rs.getString("R56_SPEC_IMPAIRMENTS"));
			obj.setR56_recoveries(rs.getBigDecimal("R56_RECOVERIES"));
			obj.setR56_write_offs(rs.getBigDecimal("R56_WRITE_OFFS"));
			obj.setR56_new_chars(rs.getBigDecimal("R56_NEW_CHARS"));
			obj.setR56_total(rs.getBigDecimal("R56_TOTAL"));
			obj.setR57_spec_impairments(rs.getString("R57_SPEC_IMPAIRMENTS"));
			obj.setR57_recoveries(rs.getBigDecimal("R57_RECOVERIES"));
			obj.setR57_write_offs(rs.getBigDecimal("R57_WRITE_OFFS"));
			obj.setR57_new_chars(rs.getBigDecimal("R57_NEW_CHARS"));
			obj.setR57_total(rs.getBigDecimal("R57_TOTAL"));
			obj.setR58_spec_impairments(rs.getString("R58_SPEC_IMPAIRMENTS"));
			obj.setR58_recoveries(rs.getBigDecimal("R58_RECOVERIES"));
			obj.setR58_write_offs(rs.getBigDecimal("R58_WRITE_OFFS"));
			obj.setR58_new_chars(rs.getBigDecimal("R58_NEW_CHARS"));
			obj.setR58_total(rs.getBigDecimal("R58_TOTAL"));
			obj.setR59_spec_impairments(rs.getString("R59_SPEC_IMPAIRMENTS"));
			obj.setR59_recoveries(rs.getBigDecimal("R59_RECOVERIES"));
			obj.setR59_write_offs(rs.getBigDecimal("R59_WRITE_OFFS"));
			obj.setR59_new_chars(rs.getBigDecimal("R59_NEW_CHARS"));
			obj.setR59_total(rs.getBigDecimal("R59_TOTAL"));
			obj.setR60_spec_impairments(rs.getString("R60_SPEC_IMPAIRMENTS"));
			obj.setR60_recoveries(rs.getBigDecimal("R60_RECOVERIES"));
			obj.setR60_write_offs(rs.getBigDecimal("R60_WRITE_OFFS"));
			obj.setR60_new_chars(rs.getBigDecimal("R60_NEW_CHARS"));
			obj.setR60_total(rs.getBigDecimal("R60_TOTAL"));
			obj.setR61_spec_impairments(rs.getString("R61_SPEC_IMPAIRMENTS"));
			obj.setR61_recoveries(rs.getBigDecimal("R61_RECOVERIES"));
			obj.setR61_write_offs(rs.getBigDecimal("R61_WRITE_OFFS"));
			obj.setR61_new_chars(rs.getBigDecimal("R61_NEW_CHARS"));
			obj.setR61_total(rs.getBigDecimal("R61_TOTAL"));
			obj.setR62_spec_impairments(rs.getString("R62_SPEC_IMPAIRMENTS"));
			obj.setR62_recoveries(rs.getBigDecimal("R62_RECOVERIES"));
			obj.setR62_write_offs(rs.getBigDecimal("R62_WRITE_OFFS"));
			obj.setR62_new_chars(rs.getBigDecimal("R62_NEW_CHARS"));
			obj.setR62_total(rs.getBigDecimal("R62_TOTAL"));
			obj.setR63_spec_impairments(rs.getString("R63_SPEC_IMPAIRMENTS"));
			obj.setR63_recoveries(rs.getBigDecimal("R63_RECOVERIES"));
			obj.setR63_write_offs(rs.getBigDecimal("R63_WRITE_OFFS"));
			obj.setR63_new_chars(rs.getBigDecimal("R63_NEW_CHARS"));
			obj.setR63_total(rs.getBigDecimal("R63_TOTAL"));
			obj.setR64_spec_impairments(rs.getString("R64_SPEC_IMPAIRMENTS"));
			obj.setR64_recoveries(rs.getBigDecimal("R64_RECOVERIES"));
			obj.setR64_write_offs(rs.getBigDecimal("R64_WRITE_OFFS"));
			obj.setR64_new_chars(rs.getBigDecimal("R64_NEW_CHARS"));
			obj.setR64_total(rs.getBigDecimal("R64_TOTAL"));
			obj.setR65_spec_impairments(rs.getString("R65_SPEC_IMPAIRMENTS"));
			obj.setR65_recoveries(rs.getBigDecimal("R65_RECOVERIES"));
			obj.setR65_write_offs(rs.getBigDecimal("R65_WRITE_OFFS"));
			obj.setR65_new_chars(rs.getBigDecimal("R65_NEW_CHARS"));
			obj.setR65_total(rs.getBigDecimal("R65_TOTAL"));
			obj.setR66_spec_impairments(rs.getString("R66_SPEC_IMPAIRMENTS"));
			obj.setR66_recoveries(rs.getBigDecimal("R66_RECOVERIES"));
			obj.setR66_write_offs(rs.getBigDecimal("R66_WRITE_OFFS"));
			obj.setR66_new_chars(rs.getBigDecimal("R66_NEW_CHARS"));
			obj.setR66_total(rs.getBigDecimal("R66_TOTAL"));
			obj.setR67_spec_impairments(rs.getString("R67_SPEC_IMPAIRMENTS"));
			obj.setR67_recoveries(rs.getBigDecimal("R67_RECOVERIES"));
			obj.setR67_write_offs(rs.getBigDecimal("R67_WRITE_OFFS"));
			obj.setR67_new_chars(rs.getBigDecimal("R67_NEW_CHARS"));
			obj.setR67_total(rs.getBigDecimal("R67_TOTAL"));
			obj.setR68_spec_impairments(rs.getString("R68_SPEC_IMPAIRMENTS"));
			obj.setR68_recoveries(rs.getBigDecimal("R68_RECOVERIES"));
			obj.setR68_write_offs(rs.getBigDecimal("R68_WRITE_OFFS"));
			obj.setR68_new_chars(rs.getBigDecimal("R68_NEW_CHARS"));
			obj.setR68_total(rs.getBigDecimal("R68_TOTAL"));
			obj.setR69_spec_impairments(rs.getString("R69_SPEC_IMPAIRMENTS"));
			obj.setR69_recoveries(rs.getBigDecimal("R69_RECOVERIES"));
			obj.setR69_write_offs(rs.getBigDecimal("R69_WRITE_OFFS"));
			obj.setR69_new_chars(rs.getBigDecimal("R69_NEW_CHARS"));
			obj.setR69_total(rs.getBigDecimal("R69_TOTAL"));
			obj.setR70_spec_impairments(rs.getString("R70_SPEC_IMPAIRMENTS"));
			obj.setR70_recoveries(rs.getBigDecimal("R70_RECOVERIES"));
			obj.setR70_write_offs(rs.getBigDecimal("R70_WRITE_OFFS"));
			obj.setR70_new_chars(rs.getBigDecimal("R70_NEW_CHARS"));
			obj.setR70_total(rs.getBigDecimal("R70_TOTAL"));
			return obj;
		}
	}

	class M_I_S_CADetailRowMapper implements RowMapper<M_I_S_CA_Detail_Entity> {
		@Override
		public M_I_S_CA_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_I_S_CA_Detail_Entity obj = new M_I_S_CA_Detail_Entity();
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setReportLable(rs.getString("REPORT_LABEL"));
			obj.setReportAddlCriteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportAddlCriteria_2(rs.getString("REPORT_ADDL_CRITERIA_2"));
			obj.setReportAddlCriteria_3(rs.getString("REPORT_ADDL_CRITERIA_3"));
			obj.setReportLabel1(rs.getString("REPORT_LABEL_1"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setCreateUser(rs.getString("CREATE_USER"));
			obj.setCreateTime(rs.getDate("CREATE_TIME"));
			obj.setModifyUser(rs.getString("MODIFY_USER"));
			obj.setModifyTime(rs.getDate("MODIFY_TIME"));
			obj.setVerifyUser(rs.getString("VERIFY_USER"));
			obj.setVerifyTime(rs.getDate("VERIFY_TIME"));
			obj.setEntityFlg(rs.getString("ENTITY_FLG"));
			obj.setModifyFlg(rs.getString("MODIFY_FLG"));
			obj.setDelFlg(rs.getString("DEL_FLG"));
			return obj;
		}
	}

	class M_I_S_CAArchivalDetailRowMapper implements RowMapper<M_I_S_CA_Archival_Detail_Entity> {
		@Override
		public M_I_S_CA_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_I_S_CA_Archival_Detail_Entity obj = new M_I_S_CA_Archival_Detail_Entity();
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setReportLable(rs.getString("REPORT_LABEL"));
			obj.setReportAddlCriteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportAddlCriteria_2(rs.getString("REPORT_ADDL_CRITERIA_2"));
			obj.setReportAddlCriteria_3(rs.getString("REPORT_ADDL_CRITERIA_3"));
			obj.setReportLabel1(rs.getString("REPORT_LABEL_1"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setCreateUser(rs.getString("CREATE_USER"));
			obj.setCreateTime(rs.getDate("CREATE_TIME"));
			obj.setModifyUser(rs.getString("MODIFY_USER"));
			obj.setModifyTime(rs.getDate("MODIFY_TIME"));
			obj.setVerifyUser(rs.getString("VERIFY_USER"));
			obj.setVerifyTime(rs.getDate("VERIFY_TIME"));
			obj.setEntityFlg(rs.getString("ENTITY_FLG"));
			obj.setModifyFlg(rs.getString("MODIFY_FLG"));
			obj.setDelFlg(rs.getString("DEL_FLG"));
			return obj;
		}
	}
}
