package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfileRep;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Service
@Transactional
public class BRRS_CRB_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_CRB_ReportService.class);

	private static final String SUMMARY_TABLE = "CRB_SUMMARYTABLE";
	private static final String ARCHIVAL_TABLE = "CRB_ARCHIVAL_SUMMARYTABLE";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	UserProfileRep userProfileRep;

	// Column names = entity field names (used for the Excel export)
	private static final String[] CRB_COLUMNS = { "DATA_RECORD", "OMANG_ID", "PASSPORT_NUMBER", "GENDER",
			"DATE_OF_BIRTH", "BRANCH_CODE", "ACCOUNT_NUMBER", "SUB_ACCOUNT_NUMBER", "SURNAME", "TITLE",
			"FORENAME_OR_INITIAL_1", "FORENAME_OR_INITIAL_2", "FORENAME_OR_INITIAL_3", "RESIDENTIAL_ADDRESS_LINE_1",
			"RESIDENTIAL_ADDRESS_LINE_2", "RESIDENTIAL_ADDRESS_LINE_3", "RESIDENTIAL_ADDRESS_LINE_4",
			"POSTAL_CODE_RESIDENTIAL", "OWNER_TENANT", "POSTAL_ADDRESS_LINE_1", "POSTAL_ADDRESS_LINE_2",
			"POSTAL_ADDRESS_LINE_3", "POSTAL_ADDRESS_LINE_4", "POST_CODE_POSTAL", "OWNERSHIP_TYPE", "REASON",
			"PAYMENT_TYPE", "TYPE_OF_ACCOUNT", "DATE_ACCOUNT_OPENED", "DEFERRED_PAYMENT_START_DATE",
			"LAST_PAYMENT_RECEIVED_DATE", "OPENING_BALANCE_CREDIT_LIMIT", "CURRENT_BALANCE",
			"CURRENT_BALANCE_DEBIT_IND", "AMOUNT_OVERDUE", "INSTALMENT_AMOUNT", "MONTHS_IN_ARREARS", "STATUS_CODE",
			"REPAYMENT_FREQUENCY", "TERMS", "STATUS_DATE", "OLD_SUPPLIER_BRANCH_CODE", "OLD_ACCOUNT_NUMBER",
			"OLD_SUB_ACCOUNT_NO", "OLD_SUPPLIER_REFERENCE_NO", "H_TELEPHONE", "CELLULAR_TELEPHONE", "W_TELEPHONE",
			"EMPLOYER_DETAIL", "INCOME", "INCOME_FREQUENCY", "OCCUPATION", "THIRD_PARTY_NAME",
			"ACCOUNT_SOLD_TO_THIRD_PARTY", "NO_OF_PARTICIPANTS_JOINT", "FILLER", "REPORT_DATE", "REPORT_VERSION",
			"REPORT_FREQUENCY", "REPORT_CODE", "REPORT_DESC", "ENTITY_FLG", "MODIFY_FLG", "DEL_FLG",
			"REPORT_RESUBDATE", "SNO" };

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// ------------------------------
	// Parses a date string robustly supporting multiple formats
	// ------------------------------
	private Date parseDateRobustly(String dateStr) {
		if (dateStr == null || dateStr.isEmpty()) {
			return null;
		}
		try {
			return new SimpleDateFormat("dd-MMM-yyyy").parse(dateStr);
		} catch (ParseException e) {
			try {
				return new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);
			} catch (ParseException ex) {
				try {
					return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
				} catch (ParseException ex2) {
					logger.error("Failed to parse date: {}", dateStr);
					return null;
				}
			}
		}
	}

	// ------------------------------
	// Retrieves the CRB report summary view
	// ------------------------------
	public ModelAndView getCRBView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {
		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		String role = userid != null ? userProfileRep.getUserRole(userid) : null;
		md.addAttribute("role", role);
		logger.info("User Id: {}, Role: {}", userid, role);

		Date parsedDate = parseDateRobustly(todate);

		if ("ARCHIVAL".equals(type)) {
			if (version == null && parsedDate != null) {
				version = findMaxVersion(parsedDate);
				logger.info("Auto-detected version: {}", version);
			}

			List<CRB_Archival_Summary_Entity> T1Master = new ArrayList<CRB_Archival_Summary_Entity>();
			if (version != null && parsedDate != null) {
				T1Master = jdbcTemplate.query(
						"SELECT * FROM " + ARCHIVAL_TABLE + " WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
						new Object[] { parsedDate, version }, new CRBArchivalSummaryRowMapper());
			}
			mv.addObject("reportsummary", T1Master);
		} else {
			List<CRB_Summary_Entity> T1Master = new ArrayList<CRB_Summary_Entity>();
			if (parsedDate != null) {
				T1Master = jdbcTemplate.query("SELECT * FROM " + SUMMARY_TABLE + " WHERE REPORT_DATE = ?",
						new Object[] { parsedDate }, new CRBSummaryRowMapper());
			}
			mv.addObject("reportsummary", T1Master);
		}

		mv.setViewName("BRRS/CRB");
		mv.addObject("displaymode", "summary");
		mv.addObject("reportdate", todate);
		mv.addObject("reportid", reportId);
		mv.addObject("asondate", todate);
		mv.addObject("fromdate", fromdate);
		mv.addObject("todate", todate);
		mv.addObject("currency", currency);
		mv.addObject("dtltype", dtltype);
		mv.addObject("type", type);
		mv.addObject("version", version);

		return mv;
	}

	// ------------------------------
	// Generates Excel report for CRB summary (current)
	// ------------------------------
	public byte[] getCRBExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting CRB Excel generation.");

		List<CRB_Summary_Entity> dataList = jdbcTemplate.query(
				"SELECT * FROM " + SUMMARY_TABLE + " WHERE REPORT_DATE = ?",
				new Object[] { parseDateRobustly(todate) }, new CRBSummaryRowMapper());

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for CRB report. Returning empty result.");
			return new byte[0];
		}
		return buildExcel(dataList, CRB_Summary_Entity.class);
	}

	// ------------------------------
	// Generates Excel report for CRB archival summary
	// ------------------------------
	public byte[] getExcelCRBARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting CRB ARCHIVAL Excel generation.");

		List<CRB_Archival_Summary_Entity> dataList = jdbcTemplate.query(
				"SELECT * FROM " + ARCHIVAL_TABLE + " WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
				new Object[] { parseDateRobustly(todate), version }, new CRBArchivalSummaryRowMapper());

		if (dataList.isEmpty()) {
			logger.warn("Service: No archival data found for CRB report. Returning empty result.");
			return new byte[0];
		}
		return buildExcel(dataList, CRB_Archival_Summary_Entity.class);
	}

	// ------------------------------
	// Common Excel builder (header row + one row per record)
	// Columns = DATA_COLUMNS (the same 56 columns the upload reads)
	// ------------------------------
	private byte[] buildExcel(List<?> dataList, Class<?> entityClass) throws Exception {
		try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			XSSFSheet sheet = workbook.createSheet("CRB");
			BorderStyle border = BorderStyle.THIN;

			CellStyle headerStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 10);
			headerStyle.setFont(headerFont);
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setBorderTop(border);
			headerStyle.setBorderBottom(border);
			headerStyle.setBorderLeft(border);
			headerStyle.setBorderRight(border);

			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setBorderTop(border);
			dataStyle.setBorderBottom(border);
			dataStyle.setBorderLeft(border);
			dataStyle.setBorderRight(border);

			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

			// ---- header row ----
			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < DATA_COLUMNS.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(DATA_COLUMNS[i]);
				cell.setCellStyle(headerStyle);
				sheet.setColumnWidth(i, 5000);
			}

			// ---- data rows ----
			int rowIndex = 1; // <-- this was missing
			for (Object record : dataList) {
				XSSFRow row = sheet.createRow(rowIndex++);
				for (int c = 0; c < DATA_COLUMNS.length; c++) {
					Cell cell = row.createCell(c);
					cell.setCellStyle(dataStyle);
					try {
						Field field = entityClass.getDeclaredField(DATA_COLUMNS[c]); // <-- DATA_COLUMNS, not CRB_COLUMNS
						field.setAccessible(true);
						Object value = field.get(record);
						if (value == null) {
							cell.setCellValue("");
						} else if (value instanceof BigDecimal) {
							cell.setCellValue(((BigDecimal) value).doubleValue());
						} else if (value instanceof Date) {
							cell.setCellValue(df.format((Date) value));
						} else {
							cell.setCellValue(value.toString());
						}
					} catch (NoSuchFieldException | IllegalAccessException e) {
						cell.setCellValue("");
						logger.warn("Field not found or inaccessible: {}", DATA_COLUMNS[c]);
					}
				}
			}

			workbook.write(out);
			logger.info("Service: CRB Excel written to memory ({} bytes).", out.size());
			return out.toByteArray();
		}
	}

	// ------------------------------
	// Fetches CRB archival list
	// ------------------------------
	public List<Object> getCRBArchival() {
		List<Object> CRBArchivallist = new ArrayList<>();
		try {
			CRBArchivallist = jdbcTemplate.query(
					"select DISTINCT REPORT_DATE, REPORT_VERSION from " + ARCHIVAL_TABLE
							+ " order by REPORT_VERSION DESC",
					(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION"),
							null });
			logger.info("CRB archival count: {}", CRBArchivallist.size());
		} catch (Exception e) {
			logger.error("Error fetching CRB Archival data", e);
		}
		return CRBArchivallist;
	}

	// ------------------------------
	// Finds the maximum version for a given report date
	// ------------------------------
	public BigDecimal findMaxVersion(Date reportDate) {
		try {
			String sql = "SELECT MAX(REPORT_VERSION) FROM " + ARCHIVAL_TABLE + " WHERE REPORT_DATE = ?";
			return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
		} catch (Exception e) {
			logger.warn("No version found for date {}: {}", reportDate, e.getMessage());
			return null;
		}
	}

	// ==============================
	// Row Mappers
	// ==============================

	class CRBSummaryRowMapper implements RowMapper<CRB_Summary_Entity> {
		@Override
		public CRB_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			CRB_Summary_Entity obj = new CRB_Summary_Entity();
			obj.setSNO(rs.getString("SNO"));
			obj.setDATA_RECORD(rs.getString("DATA_RECORD"));
			obj.setOMANG_ID(rs.getBigDecimal("OMANG_ID"));
			obj.setPASSPORT_NUMBER(rs.getString("PASSPORT_NUMBER"));
			obj.setGENDER(rs.getString("GENDER"));
			obj.setDATE_OF_BIRTH(rs.getBigDecimal("DATE_OF_BIRTH"));
			obj.setBRANCH_CODE(rs.getString("BRANCH_CODE"));
			obj.setACCOUNT_NUMBER(rs.getString("ACCOUNT_NUMBER"));
			obj.setSUB_ACCOUNT_NUMBER(rs.getString("SUB_ACCOUNT_NUMBER"));
			obj.setSURNAME(rs.getString("SURNAME"));
			obj.setTITLE(rs.getString("TITLE"));
			obj.setFORENAME_OR_INITIAL_1(rs.getString("FORENAME_OR_INITIAL_1"));
			obj.setFORENAME_OR_INITIAL_2(rs.getString("FORENAME_OR_INITIAL_2"));
			obj.setFORENAME_OR_INITIAL_3(rs.getString("FORENAME_OR_INITIAL_3"));
			obj.setRESIDENTIAL_ADDRESS_LINE_1(rs.getString("RESIDENTIAL_ADDRESS_LINE_1"));
			obj.setRESIDENTIAL_ADDRESS_LINE_2(rs.getString("RESIDENTIAL_ADDRESS_LINE_2"));
			obj.setRESIDENTIAL_ADDRESS_LINE_3(rs.getString("RESIDENTIAL_ADDRESS_LINE_3"));
			obj.setRESIDENTIAL_ADDRESS_LINE_4(rs.getString("RESIDENTIAL_ADDRESS_LINE_4"));
			obj.setPOSTAL_CODE_RESIDENTIAL(rs.getString("POSTAL_CODE_RESIDENTIAL"));
			obj.setOWNER_TENANT(rs.getString("OWNER_TENANT"));
			obj.setPOSTAL_ADDRESS_LINE_1(rs.getString("POSTAL_ADDRESS_LINE_1"));
			obj.setPOSTAL_ADDRESS_LINE_2(rs.getString("POSTAL_ADDRESS_LINE_2"));
			obj.setPOSTAL_ADDRESS_LINE_3(rs.getString("POSTAL_ADDRESS_LINE_3"));
			obj.setPOSTAL_ADDRESS_LINE_4(rs.getString("POSTAL_ADDRESS_LINE_4"));
			obj.setPOST_CODE_POSTAL(rs.getString("POST_CODE_POSTAL"));
			obj.setOWNERSHIP_TYPE(rs.getString("OWNERSHIP_TYPE"));
			obj.setREASON(rs.getString("REASON"));
			obj.setPAYMENT_TYPE(rs.getString("PAYMENT_TYPE"));
			obj.setTYPE_OF_ACCOUNT(rs.getString("TYPE_OF_ACCOUNT"));
			obj.setDATE_ACCOUNT_OPENED(rs.getBigDecimal("DATE_ACCOUNT_OPENED"));
			obj.setDEFERRED_PAYMENT_START_DATE(rs.getBigDecimal("DEFERRED_PAYMENT_START_DATE"));
			obj.setLAST_PAYMENT_RECEIVED_DATE(rs.getBigDecimal("LAST_PAYMENT_RECEIVED_DATE"));
			obj.setOPENING_BALANCE_CREDIT_LIMIT(rs.getBigDecimal("OPENING_BALANCE_CREDIT_LIMIT"));
			obj.setCURRENT_BALANCE(rs.getBigDecimal("CURRENT_BALANCE"));
			obj.setCURRENT_BALANCE_DEBIT_IND(rs.getString("CURRENT_BALANCE_DEBIT_IND"));
			obj.setAMOUNT_OVERDUE(rs.getBigDecimal("AMOUNT_OVERDUE"));
			obj.setINSTALMENT_AMOUNT(rs.getBigDecimal("INSTALMENT_AMOUNT"));
			obj.setMONTHS_IN_ARREARS(rs.getBigDecimal("MONTHS_IN_ARREARS"));
			obj.setSTATUS_CODE(rs.getString("STATUS_CODE"));
			obj.setREPAYMENT_FREQUENCY(rs.getBigDecimal("REPAYMENT_FREQUENCY"));
			obj.setTERMS(rs.getBigDecimal("TERMS"));
			obj.setSTATUS_DATE(rs.getBigDecimal("STATUS_DATE"));
			obj.setOLD_SUPPLIER_BRANCH_CODE(rs.getString("OLD_SUPPLIER_BRANCH_CODE"));
			obj.setOLD_ACCOUNT_NUMBER(rs.getString("OLD_ACCOUNT_NUMBER"));
			obj.setOLD_SUB_ACCOUNT_NO(rs.getString("OLD_SUB_ACCOUNT_NO"));
			obj.setOLD_SUPPLIER_REFERENCE_NO(rs.getString("OLD_SUPPLIER_REFERENCE_NO"));
			obj.setH_TELEPHONE(rs.getString("H_TELEPHONE"));
			obj.setCELLULAR_TELEPHONE(rs.getString("CELLULAR_TELEPHONE"));
			obj.setW_TELEPHONE(rs.getString("W_TELEPHONE"));
			obj.setEMPLOYER_DETAIL(rs.getString("EMPLOYER_DETAIL"));
			obj.setINCOME(rs.getBigDecimal("INCOME"));
			obj.setINCOME_FREQUENCY(rs.getString("INCOME_FREQUENCY"));
			obj.setOCCUPATION(rs.getString("OCCUPATION"));
			obj.setTHIRD_PARTY_NAME(rs.getString("THIRD_PARTY_NAME"));
			obj.setACCOUNT_SOLD_TO_THIRD_PARTY(rs.getBigDecimal("ACCOUNT_SOLD_TO_THIRD_PARTY"));
			obj.setNO_OF_PARTICIPANTS_JOINT(rs.getBigDecimal("NO_OF_PARTICIPANTS_JOINT"));
			obj.setFILLER(rs.getString("FILLER"));
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));
			obj.setREPORT_RESUBDATE(rs.getDate("REPORT_RESUBDATE"));
			return obj;
		}
	}

	class CRBArchivalSummaryRowMapper implements RowMapper<CRB_Archival_Summary_Entity> {
		@Override
		public CRB_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			CRB_Archival_Summary_Entity obj = new CRB_Archival_Summary_Entity();
			obj.setSNO(rs.getString("SNO"));
			obj.setDATA_RECORD(rs.getString("DATA_RECORD"));
			obj.setOMANG_ID(rs.getBigDecimal("OMANG_ID"));
			obj.setPASSPORT_NUMBER(rs.getString("PASSPORT_NUMBER"));
			obj.setGENDER(rs.getString("GENDER"));
			obj.setDATE_OF_BIRTH(rs.getBigDecimal("DATE_OF_BIRTH"));
			obj.setBRANCH_CODE(rs.getString("BRANCH_CODE"));
			obj.setACCOUNT_NUMBER(rs.getString("ACCOUNT_NUMBER"));
			obj.setSUB_ACCOUNT_NUMBER(rs.getString("SUB_ACCOUNT_NUMBER"));
			obj.setSURNAME(rs.getString("SURNAME"));
			obj.setTITLE(rs.getString("TITLE"));
			obj.setFORENAME_OR_INITIAL_1(rs.getString("FORENAME_OR_INITIAL_1"));
			obj.setFORENAME_OR_INITIAL_2(rs.getString("FORENAME_OR_INITIAL_2"));
			obj.setFORENAME_OR_INITIAL_3(rs.getString("FORENAME_OR_INITIAL_3"));
			obj.setRESIDENTIAL_ADDRESS_LINE_1(rs.getString("RESIDENTIAL_ADDRESS_LINE_1"));
			obj.setRESIDENTIAL_ADDRESS_LINE_2(rs.getString("RESIDENTIAL_ADDRESS_LINE_2"));
			obj.setRESIDENTIAL_ADDRESS_LINE_3(rs.getString("RESIDENTIAL_ADDRESS_LINE_3"));
			obj.setRESIDENTIAL_ADDRESS_LINE_4(rs.getString("RESIDENTIAL_ADDRESS_LINE_4"));
			obj.setPOSTAL_CODE_RESIDENTIAL(rs.getString("POSTAL_CODE_RESIDENTIAL"));
			obj.setOWNER_TENANT(rs.getString("OWNER_TENANT"));
			obj.setPOSTAL_ADDRESS_LINE_1(rs.getString("POSTAL_ADDRESS_LINE_1"));
			obj.setPOSTAL_ADDRESS_LINE_2(rs.getString("POSTAL_ADDRESS_LINE_2"));
			obj.setPOSTAL_ADDRESS_LINE_3(rs.getString("POSTAL_ADDRESS_LINE_3"));
			obj.setPOSTAL_ADDRESS_LINE_4(rs.getString("POSTAL_ADDRESS_LINE_4"));
			obj.setPOST_CODE_POSTAL(rs.getString("POST_CODE_POSTAL"));
			obj.setOWNERSHIP_TYPE(rs.getString("OWNERSHIP_TYPE"));
			obj.setREASON(rs.getString("REASON"));
			obj.setPAYMENT_TYPE(rs.getString("PAYMENT_TYPE"));
			obj.setTYPE_OF_ACCOUNT(rs.getString("TYPE_OF_ACCOUNT"));
			obj.setDATE_ACCOUNT_OPENED(rs.getBigDecimal("DATE_ACCOUNT_OPENED"));
			obj.setDEFERRED_PAYMENT_START_DATE(rs.getBigDecimal("DEFERRED_PAYMENT_START_DATE"));
			obj.setLAST_PAYMENT_RECEIVED_DATE(rs.getBigDecimal("LAST_PAYMENT_RECEIVED_DATE"));
			obj.setOPENING_BALANCE_CREDIT_LIMIT(rs.getBigDecimal("OPENING_BALANCE_CREDIT_LIMIT"));
			obj.setCURRENT_BALANCE(rs.getBigDecimal("CURRENT_BALANCE"));
			obj.setCURRENT_BALANCE_DEBIT_IND(rs.getString("CURRENT_BALANCE_DEBIT_IND"));
			obj.setAMOUNT_OVERDUE(rs.getBigDecimal("AMOUNT_OVERDUE"));
			obj.setINSTALMENT_AMOUNT(rs.getBigDecimal("INSTALMENT_AMOUNT"));
			obj.setMONTHS_IN_ARREARS(rs.getBigDecimal("MONTHS_IN_ARREARS"));
			obj.setSTATUS_CODE(rs.getString("STATUS_CODE"));
			obj.setREPAYMENT_FREQUENCY(rs.getBigDecimal("REPAYMENT_FREQUENCY"));
			obj.setTERMS(rs.getBigDecimal("TERMS"));
			obj.setSTATUS_DATE(rs.getBigDecimal("STATUS_DATE"));
			obj.setOLD_SUPPLIER_BRANCH_CODE(rs.getString("OLD_SUPPLIER_BRANCH_CODE"));
			obj.setOLD_ACCOUNT_NUMBER(rs.getString("OLD_ACCOUNT_NUMBER"));
			obj.setOLD_SUB_ACCOUNT_NO(rs.getString("OLD_SUB_ACCOUNT_NO"));
			obj.setOLD_SUPPLIER_REFERENCE_NO(rs.getString("OLD_SUPPLIER_REFERENCE_NO"));
			obj.setH_TELEPHONE(rs.getString("H_TELEPHONE"));
			obj.setCELLULAR_TELEPHONE(rs.getString("CELLULAR_TELEPHONE"));
			obj.setW_TELEPHONE(rs.getString("W_TELEPHONE"));
			obj.setEMPLOYER_DETAIL(rs.getString("EMPLOYER_DETAIL"));
			obj.setINCOME(rs.getBigDecimal("INCOME"));
			obj.setINCOME_FREQUENCY(rs.getString("INCOME_FREQUENCY"));
			obj.setOCCUPATION(rs.getString("OCCUPATION"));
			obj.setTHIRD_PARTY_NAME(rs.getString("THIRD_PARTY_NAME"));
			obj.setACCOUNT_SOLD_TO_THIRD_PARTY(rs.getBigDecimal("ACCOUNT_SOLD_TO_THIRD_PARTY"));
			obj.setNO_OF_PARTICIPANTS_JOINT(rs.getBigDecimal("NO_OF_PARTICIPANTS_JOINT"));
			obj.setFILLER(rs.getString("FILLER"));
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));
			obj.setREPORT_RESUBDATE(rs.getDate("REPORT_RESUBDATE"));
			return obj;
		}
	}
 	public static class CRB_Archival_Summary_Entity {
 		@Id
 		private String SNO;
 		private String DATA_RECORD;
 		private BigDecimal OMANG_ID;
 		private String PASSPORT_NUMBER;
 		private String GENDER;
 		private BigDecimal DATE_OF_BIRTH;
 		private String BRANCH_CODE;
 		private String ACCOUNT_NUMBER;
 		private String SUB_ACCOUNT_NUMBER;
 		private String SURNAME;
 		private String TITLE;
 		private String FORENAME_OR_INITIAL_1;
 		private String FORENAME_OR_INITIAL_2;
 		private String FORENAME_OR_INITIAL_3;
 		private String RESIDENTIAL_ADDRESS_LINE_1;
 		private String RESIDENTIAL_ADDRESS_LINE_2;
 		private String RESIDENTIAL_ADDRESS_LINE_3;
 		private String RESIDENTIAL_ADDRESS_LINE_4;
 		private String POSTAL_CODE_RESIDENTIAL;
 		private String OWNER_TENANT;
 		private String POSTAL_ADDRESS_LINE_1;
 		private String POSTAL_ADDRESS_LINE_2;
 		private String POSTAL_ADDRESS_LINE_3;
 		private String POSTAL_ADDRESS_LINE_4;
 		private String POST_CODE_POSTAL;
 		private String OWNERSHIP_TYPE;
 		private String REASON;
 		private String PAYMENT_TYPE;
 		private String TYPE_OF_ACCOUNT;
 		private BigDecimal DATE_ACCOUNT_OPENED;
 		private BigDecimal DEFERRED_PAYMENT_START_DATE;
 		private BigDecimal LAST_PAYMENT_RECEIVED_DATE;
 		private BigDecimal OPENING_BALANCE_CREDIT_LIMIT;
 		private BigDecimal CURRENT_BALANCE;
 		private String CURRENT_BALANCE_DEBIT_IND;
 		private BigDecimal AMOUNT_OVERDUE;
 		private BigDecimal INSTALMENT_AMOUNT;
 		private BigDecimal MONTHS_IN_ARREARS;
 		private String STATUS_CODE;
 		private BigDecimal REPAYMENT_FREQUENCY;
 		private BigDecimal TERMS;
 		private BigDecimal STATUS_DATE;
 		private String OLD_SUPPLIER_BRANCH_CODE;
 		private String OLD_ACCOUNT_NUMBER;
 		private String OLD_SUB_ACCOUNT_NO;
 		private String OLD_SUPPLIER_REFERENCE_NO;
 		private String H_TELEPHONE;
 		private String CELLULAR_TELEPHONE;
 		private String W_TELEPHONE;
 		private String EMPLOYER_DETAIL;
 		private BigDecimal INCOME;
 		private String INCOME_FREQUENCY;
 		private String OCCUPATION;
 		private String THIRD_PARTY_NAME;
 		private BigDecimal ACCOUNT_SOLD_TO_THIRD_PARTY;
 		private BigDecimal NO_OF_PARTICIPANTS_JOINT;
 		private String FILLER;
 		@Temporal(TemporalType.DATE)
 		@DateTimeFormat(pattern = "dd/MM/yyyy")
 		private Date REPORT_DATE;
 		private BigDecimal REPORT_VERSION;
 		private String REPORT_FREQUENCY;
 		private String REPORT_CODE;
 		private String REPORT_DESC;
 		private String ENTITY_FLG;
 		private String MODIFY_FLG;
 		private String DEL_FLG;
 		private Date REPORT_RESUBDATE;
		public String getSNO() {
			return SNO;
		}
		public void setSNO(String sNO) {
			SNO = sNO;
		}
		public String getDATA_RECORD() {
			return DATA_RECORD;
		}
		public void setDATA_RECORD(String dATA_RECORD) {
			DATA_RECORD = dATA_RECORD;
		}
		public BigDecimal getOMANG_ID() {
			return OMANG_ID;
		}
		public void setOMANG_ID(BigDecimal oMANG_ID) {
			OMANG_ID = oMANG_ID;
		}
		public String getPASSPORT_NUMBER() {
			return PASSPORT_NUMBER;
		}
		public void setPASSPORT_NUMBER(String pASSPORT_NUMBER) {
			PASSPORT_NUMBER = pASSPORT_NUMBER;
		}
		public String getGENDER() {
			return GENDER;
		}
		public void setGENDER(String gENDER) {
			GENDER = gENDER;
		}
		public BigDecimal getDATE_OF_BIRTH() {
			return DATE_OF_BIRTH;
		}
		public void setDATE_OF_BIRTH(BigDecimal dATE_OF_BIRTH) {
			DATE_OF_BIRTH = dATE_OF_BIRTH;
		}
		public String getBRANCH_CODE() {
			return BRANCH_CODE;
		}
		public void setBRANCH_CODE(String bRANCH_CODE) {
			BRANCH_CODE = bRANCH_CODE;
		}
		public String getACCOUNT_NUMBER() {
			return ACCOUNT_NUMBER;
		}
		public void setACCOUNT_NUMBER(String aCCOUNT_NUMBER) {
			ACCOUNT_NUMBER = aCCOUNT_NUMBER;
		}
		public String getSUB_ACCOUNT_NUMBER() {
			return SUB_ACCOUNT_NUMBER;
		}
		public void setSUB_ACCOUNT_NUMBER(String sUB_ACCOUNT_NUMBER) {
			SUB_ACCOUNT_NUMBER = sUB_ACCOUNT_NUMBER;
		}
		public String getSURNAME() {
			return SURNAME;
		}
		public void setSURNAME(String sURNAME) {
			SURNAME = sURNAME;
		}
		public String getTITLE() {
			return TITLE;
		}
		public void setTITLE(String tITLE) {
			TITLE = tITLE;
		}
		public String getFORENAME_OR_INITIAL_1() {
			return FORENAME_OR_INITIAL_1;
		}
		public void setFORENAME_OR_INITIAL_1(String fORENAME_OR_INITIAL_1) {
			FORENAME_OR_INITIAL_1 = fORENAME_OR_INITIAL_1;
		}
		public String getFORENAME_OR_INITIAL_2() {
			return FORENAME_OR_INITIAL_2;
		}
		public void setFORENAME_OR_INITIAL_2(String fORENAME_OR_INITIAL_2) {
			FORENAME_OR_INITIAL_2 = fORENAME_OR_INITIAL_2;
		}
		public String getFORENAME_OR_INITIAL_3() {
			return FORENAME_OR_INITIAL_3;
		}
		public void setFORENAME_OR_INITIAL_3(String fORENAME_OR_INITIAL_3) {
			FORENAME_OR_INITIAL_3 = fORENAME_OR_INITIAL_3;
		}
		public String getRESIDENTIAL_ADDRESS_LINE_1() {
			return RESIDENTIAL_ADDRESS_LINE_1;
		}
		public void setRESIDENTIAL_ADDRESS_LINE_1(String rESIDENTIAL_ADDRESS_LINE_1) {
			RESIDENTIAL_ADDRESS_LINE_1 = rESIDENTIAL_ADDRESS_LINE_1;
		}
		public String getRESIDENTIAL_ADDRESS_LINE_2() {
			return RESIDENTIAL_ADDRESS_LINE_2;
		}
		public void setRESIDENTIAL_ADDRESS_LINE_2(String rESIDENTIAL_ADDRESS_LINE_2) {
			RESIDENTIAL_ADDRESS_LINE_2 = rESIDENTIAL_ADDRESS_LINE_2;
		}
		public String getRESIDENTIAL_ADDRESS_LINE_3() {
			return RESIDENTIAL_ADDRESS_LINE_3;
		}
		public void setRESIDENTIAL_ADDRESS_LINE_3(String rESIDENTIAL_ADDRESS_LINE_3) {
			RESIDENTIAL_ADDRESS_LINE_3 = rESIDENTIAL_ADDRESS_LINE_3;
		}
		public String getRESIDENTIAL_ADDRESS_LINE_4() {
			return RESIDENTIAL_ADDRESS_LINE_4;
		}
		public void setRESIDENTIAL_ADDRESS_LINE_4(String rESIDENTIAL_ADDRESS_LINE_4) {
			RESIDENTIAL_ADDRESS_LINE_4 = rESIDENTIAL_ADDRESS_LINE_4;
		}
		public String getPOSTAL_CODE_RESIDENTIAL() {
			return POSTAL_CODE_RESIDENTIAL;
		}
		public void setPOSTAL_CODE_RESIDENTIAL(String pOSTAL_CODE_RESIDENTIAL) {
			POSTAL_CODE_RESIDENTIAL = pOSTAL_CODE_RESIDENTIAL;
		}
		public String getOWNER_TENANT() {
			return OWNER_TENANT;
		}
		public void setOWNER_TENANT(String oWNER_TENANT) {
			OWNER_TENANT = oWNER_TENANT;
		}
		public String getPOSTAL_ADDRESS_LINE_1() {
			return POSTAL_ADDRESS_LINE_1;
		}
		public void setPOSTAL_ADDRESS_LINE_1(String pOSTAL_ADDRESS_LINE_1) {
			POSTAL_ADDRESS_LINE_1 = pOSTAL_ADDRESS_LINE_1;
		}
		public String getPOSTAL_ADDRESS_LINE_2() {
			return POSTAL_ADDRESS_LINE_2;
		}
		public void setPOSTAL_ADDRESS_LINE_2(String pOSTAL_ADDRESS_LINE_2) {
			POSTAL_ADDRESS_LINE_2 = pOSTAL_ADDRESS_LINE_2;
		}
		public String getPOSTAL_ADDRESS_LINE_3() {
			return POSTAL_ADDRESS_LINE_3;
		}
		public void setPOSTAL_ADDRESS_LINE_3(String pOSTAL_ADDRESS_LINE_3) {
			POSTAL_ADDRESS_LINE_3 = pOSTAL_ADDRESS_LINE_3;
		}
		public String getPOSTAL_ADDRESS_LINE_4() {
			return POSTAL_ADDRESS_LINE_4;
		}
		public void setPOSTAL_ADDRESS_LINE_4(String pOSTAL_ADDRESS_LINE_4) {
			POSTAL_ADDRESS_LINE_4 = pOSTAL_ADDRESS_LINE_4;
		}
		public String getPOST_CODE_POSTAL() {
			return POST_CODE_POSTAL;
		}
		public void setPOST_CODE_POSTAL(String pOST_CODE_POSTAL) {
			POST_CODE_POSTAL = pOST_CODE_POSTAL;
		}
		public String getOWNERSHIP_TYPE() {
			return OWNERSHIP_TYPE;
		}
		public void setOWNERSHIP_TYPE(String oWNERSHIP_TYPE) {
			OWNERSHIP_TYPE = oWNERSHIP_TYPE;
		}
		public String getREASON() {
			return REASON;
		}
		public void setREASON(String rEASON) {
			REASON = rEASON;
		}
		public String getPAYMENT_TYPE() {
			return PAYMENT_TYPE;
		}
		public void setPAYMENT_TYPE(String pAYMENT_TYPE) {
			PAYMENT_TYPE = pAYMENT_TYPE;
		}
		public String getTYPE_OF_ACCOUNT() {
			return TYPE_OF_ACCOUNT;
		}
		public void setTYPE_OF_ACCOUNT(String tYPE_OF_ACCOUNT) {
			TYPE_OF_ACCOUNT = tYPE_OF_ACCOUNT;
		}
		public BigDecimal getDATE_ACCOUNT_OPENED() {
			return DATE_ACCOUNT_OPENED;
		}
		public void setDATE_ACCOUNT_OPENED(BigDecimal dATE_ACCOUNT_OPENED) {
			DATE_ACCOUNT_OPENED = dATE_ACCOUNT_OPENED;
		}
		public BigDecimal getDEFERRED_PAYMENT_START_DATE() {
			return DEFERRED_PAYMENT_START_DATE;
		}
		public void setDEFERRED_PAYMENT_START_DATE(BigDecimal dEFERRED_PAYMENT_START_DATE) {
			DEFERRED_PAYMENT_START_DATE = dEFERRED_PAYMENT_START_DATE;
		}
		public BigDecimal getLAST_PAYMENT_RECEIVED_DATE() {
			return LAST_PAYMENT_RECEIVED_DATE;
		}
		public void setLAST_PAYMENT_RECEIVED_DATE(BigDecimal lAST_PAYMENT_RECEIVED_DATE) {
			LAST_PAYMENT_RECEIVED_DATE = lAST_PAYMENT_RECEIVED_DATE;
		}
		public BigDecimal getOPENING_BALANCE_CREDIT_LIMIT() {
			return OPENING_BALANCE_CREDIT_LIMIT;
		}
		public void setOPENING_BALANCE_CREDIT_LIMIT(BigDecimal oPENING_BALANCE_CREDIT_LIMIT) {
			OPENING_BALANCE_CREDIT_LIMIT = oPENING_BALANCE_CREDIT_LIMIT;
		}
		public BigDecimal getCURRENT_BALANCE() {
			return CURRENT_BALANCE;
		}
		public void setCURRENT_BALANCE(BigDecimal cURRENT_BALANCE) {
			CURRENT_BALANCE = cURRENT_BALANCE;
		}
		public String getCURRENT_BALANCE_DEBIT_IND() {
			return CURRENT_BALANCE_DEBIT_IND;
		}
		public void setCURRENT_BALANCE_DEBIT_IND(String cURRENT_BALANCE_DEBIT_IND) {
			CURRENT_BALANCE_DEBIT_IND = cURRENT_BALANCE_DEBIT_IND;
		}
		public BigDecimal getAMOUNT_OVERDUE() {
			return AMOUNT_OVERDUE;
		}
		public void setAMOUNT_OVERDUE(BigDecimal aMOUNT_OVERDUE) {
			AMOUNT_OVERDUE = aMOUNT_OVERDUE;
		}
		public BigDecimal getINSTALMENT_AMOUNT() {
			return INSTALMENT_AMOUNT;
		}
		public void setINSTALMENT_AMOUNT(BigDecimal iNSTALMENT_AMOUNT) {
			INSTALMENT_AMOUNT = iNSTALMENT_AMOUNT;
		}
		public BigDecimal getMONTHS_IN_ARREARS() {
			return MONTHS_IN_ARREARS;
		}
		public void setMONTHS_IN_ARREARS(BigDecimal mONTHS_IN_ARREARS) {
			MONTHS_IN_ARREARS = mONTHS_IN_ARREARS;
		}
		public String getSTATUS_CODE() {
			return STATUS_CODE;
		}
		public void setSTATUS_CODE(String sTATUS_CODE) {
			STATUS_CODE = sTATUS_CODE;
		}
		public BigDecimal getREPAYMENT_FREQUENCY() {
			return REPAYMENT_FREQUENCY;
		}
		public void setREPAYMENT_FREQUENCY(BigDecimal rEPAYMENT_FREQUENCY) {
			REPAYMENT_FREQUENCY = rEPAYMENT_FREQUENCY;
		}
		public BigDecimal getTERMS() {
			return TERMS;
		}
		public void setTERMS(BigDecimal tERMS) {
			TERMS = tERMS;
		}
		public BigDecimal getSTATUS_DATE() {
			return STATUS_DATE;
		}
		public void setSTATUS_DATE(BigDecimal sTATUS_DATE) {
			STATUS_DATE = sTATUS_DATE;
		}
		public String getOLD_SUPPLIER_BRANCH_CODE() {
			return OLD_SUPPLIER_BRANCH_CODE;
		}
		public void setOLD_SUPPLIER_BRANCH_CODE(String oLD_SUPPLIER_BRANCH_CODE) {
			OLD_SUPPLIER_BRANCH_CODE = oLD_SUPPLIER_BRANCH_CODE;
		}
		public String getOLD_ACCOUNT_NUMBER() {
			return OLD_ACCOUNT_NUMBER;
		}
		public void setOLD_ACCOUNT_NUMBER(String oLD_ACCOUNT_NUMBER) {
			OLD_ACCOUNT_NUMBER = oLD_ACCOUNT_NUMBER;
		}
		public String getOLD_SUB_ACCOUNT_NO() {
			return OLD_SUB_ACCOUNT_NO;
		}
		public void setOLD_SUB_ACCOUNT_NO(String oLD_SUB_ACCOUNT_NO) {
			OLD_SUB_ACCOUNT_NO = oLD_SUB_ACCOUNT_NO;
		}
		public String getOLD_SUPPLIER_REFERENCE_NO() {
			return OLD_SUPPLIER_REFERENCE_NO;
		}
		public void setOLD_SUPPLIER_REFERENCE_NO(String oLD_SUPPLIER_REFERENCE_NO) {
			OLD_SUPPLIER_REFERENCE_NO = oLD_SUPPLIER_REFERENCE_NO;
		}
		public String getH_TELEPHONE() {
			return H_TELEPHONE;
		}
		public void setH_TELEPHONE(String h_TELEPHONE) {
			H_TELEPHONE = h_TELEPHONE;
		}
		public String getCELLULAR_TELEPHONE() {
			return CELLULAR_TELEPHONE;
		}
		public void setCELLULAR_TELEPHONE(String cELLULAR_TELEPHONE) {
			CELLULAR_TELEPHONE = cELLULAR_TELEPHONE;
		}
		public String getW_TELEPHONE() {
			return W_TELEPHONE;
		}
		public void setW_TELEPHONE(String w_TELEPHONE) {
			W_TELEPHONE = w_TELEPHONE;
		}
		public String getEMPLOYER_DETAIL() {
			return EMPLOYER_DETAIL;
		}
		public void setEMPLOYER_DETAIL(String eMPLOYER_DETAIL) {
			EMPLOYER_DETAIL = eMPLOYER_DETAIL;
		}
		public BigDecimal getINCOME() {
			return INCOME;
		}
		public void setINCOME(BigDecimal iNCOME) {
			INCOME = iNCOME;
		}
		public String getINCOME_FREQUENCY() {
			return INCOME_FREQUENCY;
		}
		public void setINCOME_FREQUENCY(String iNCOME_FREQUENCY) {
			INCOME_FREQUENCY = iNCOME_FREQUENCY;
		}
		public String getOCCUPATION() {
			return OCCUPATION;
		}
		public void setOCCUPATION(String oCCUPATION) {
			OCCUPATION = oCCUPATION;
		}
		public String getTHIRD_PARTY_NAME() {
			return THIRD_PARTY_NAME;
		}
		public void setTHIRD_PARTY_NAME(String tHIRD_PARTY_NAME) {
			THIRD_PARTY_NAME = tHIRD_PARTY_NAME;
		}
		public BigDecimal getACCOUNT_SOLD_TO_THIRD_PARTY() {
			return ACCOUNT_SOLD_TO_THIRD_PARTY;
		}
		public void setACCOUNT_SOLD_TO_THIRD_PARTY(BigDecimal aCCOUNT_SOLD_TO_THIRD_PARTY) {
			ACCOUNT_SOLD_TO_THIRD_PARTY = aCCOUNT_SOLD_TO_THIRD_PARTY;
		}
		public BigDecimal getNO_OF_PARTICIPANTS_JOINT() {
			return NO_OF_PARTICIPANTS_JOINT;
		}
		public void setNO_OF_PARTICIPANTS_JOINT(BigDecimal nO_OF_PARTICIPANTS_JOINT) {
			NO_OF_PARTICIPANTS_JOINT = nO_OF_PARTICIPANTS_JOINT;
		}
		public String getFILLER() {
			return FILLER;
		}
		public void setFILLER(String fILLER) {
			FILLER = fILLER;
		}
		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}
		public void setREPORT_DATE(Date rEPORT_DATE) {
			REPORT_DATE = rEPORT_DATE;
		}
		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}
		public void setREPORT_VERSION(BigDecimal rEPORT_VERSION) {
			REPORT_VERSION = rEPORT_VERSION;
		}
		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}
		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}
		public String getREPORT_CODE() {
			return REPORT_CODE;
		}
		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}
		public String getREPORT_DESC() {
			return REPORT_DESC;
		}
		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}
		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}
		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}
		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}
		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}
		public String getDEL_FLG() {
			return DEL_FLG;
		}
		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}
		public Date getREPORT_RESUBDATE() {
			return REPORT_RESUBDATE;
		}
		public void setREPORT_RESUBDATE(Date rEPORT_RESUBDATE) {
			REPORT_RESUBDATE = rEPORT_RESUBDATE;
		}
		
 		}
 	
 	
 	public static class CRB_Summary_Entity {
 		@Id
 		private String SNO;
 		private String DATA_RECORD;
 		private BigDecimal OMANG_ID;
 		private String PASSPORT_NUMBER;
 		private String GENDER;
 		private BigDecimal DATE_OF_BIRTH;
 		private String BRANCH_CODE;
 		private String ACCOUNT_NUMBER;
 		private String SUB_ACCOUNT_NUMBER;
 		private String SURNAME;
 		private String TITLE;
 		private String FORENAME_OR_INITIAL_1;
 		private String FORENAME_OR_INITIAL_2;
 		private String FORENAME_OR_INITIAL_3;
 		private String RESIDENTIAL_ADDRESS_LINE_1;
 		private String RESIDENTIAL_ADDRESS_LINE_2;
 		private String RESIDENTIAL_ADDRESS_LINE_3;
 		private String RESIDENTIAL_ADDRESS_LINE_4;
 		private String POSTAL_CODE_RESIDENTIAL;
 		private String OWNER_TENANT;
 		private String POSTAL_ADDRESS_LINE_1;
 		private String POSTAL_ADDRESS_LINE_2;
 		private String POSTAL_ADDRESS_LINE_3;
 		private String POSTAL_ADDRESS_LINE_4;
 		private String POST_CODE_POSTAL;
 		private String OWNERSHIP_TYPE;
 		private String REASON;
 		private String PAYMENT_TYPE;
 		private String TYPE_OF_ACCOUNT;
 		private BigDecimal DATE_ACCOUNT_OPENED;
 		private BigDecimal DEFERRED_PAYMENT_START_DATE;
 		private BigDecimal LAST_PAYMENT_RECEIVED_DATE;
 		private BigDecimal OPENING_BALANCE_CREDIT_LIMIT;
 		private BigDecimal CURRENT_BALANCE;
 		private String CURRENT_BALANCE_DEBIT_IND;
 		private BigDecimal AMOUNT_OVERDUE;
 		private BigDecimal INSTALMENT_AMOUNT;
 		private BigDecimal MONTHS_IN_ARREARS;
 		private String STATUS_CODE;
 		private BigDecimal REPAYMENT_FREQUENCY;
 		private BigDecimal TERMS;
 		private BigDecimal STATUS_DATE;
 		private String OLD_SUPPLIER_BRANCH_CODE;
 		private String OLD_ACCOUNT_NUMBER;
 		private String OLD_SUB_ACCOUNT_NO;
 		private String OLD_SUPPLIER_REFERENCE_NO;
 		private String H_TELEPHONE;
 		private String CELLULAR_TELEPHONE;
 		private String W_TELEPHONE;
 		private String EMPLOYER_DETAIL;
 		private BigDecimal INCOME;
 		private String INCOME_FREQUENCY;
 		private String OCCUPATION;
 		private String THIRD_PARTY_NAME;
 		private BigDecimal ACCOUNT_SOLD_TO_THIRD_PARTY;
 		private BigDecimal NO_OF_PARTICIPANTS_JOINT;
 		private String FILLER;
 		@Temporal(TemporalType.DATE)
 		@DateTimeFormat(pattern = "dd/MM/yyyy")
 		private Date REPORT_DATE;
 		private BigDecimal REPORT_VERSION;
 		private String REPORT_FREQUENCY;
 		private String REPORT_CODE;
 		private String REPORT_DESC;
 		private String ENTITY_FLG;
 		private String MODIFY_FLG;
 		private String DEL_FLG;
 		private Date REPORT_RESUBDATE;
		public String getSNO() {
			return SNO;
		}
		public void setSNO(String sNO) {
			SNO = sNO;
		}
		public String getDATA_RECORD() {
			return DATA_RECORD;
		}
		public void setDATA_RECORD(String dATA_RECORD) {
			DATA_RECORD = dATA_RECORD;
		}
		public BigDecimal getOMANG_ID() {
			return OMANG_ID;
		}
		public void setOMANG_ID(BigDecimal oMANG_ID) {
			OMANG_ID = oMANG_ID;
		}
		public String getPASSPORT_NUMBER() {
			return PASSPORT_NUMBER;
		}
		public void setPASSPORT_NUMBER(String pASSPORT_NUMBER) {
			PASSPORT_NUMBER = pASSPORT_NUMBER;
		}
		public String getGENDER() {
			return GENDER;
		}
		public void setGENDER(String gENDER) {
			GENDER = gENDER;
		}
		public BigDecimal getDATE_OF_BIRTH() {
			return DATE_OF_BIRTH;
		}
		public void setDATE_OF_BIRTH(BigDecimal dATE_OF_BIRTH) {
			DATE_OF_BIRTH = dATE_OF_BIRTH;
		}
		public String getBRANCH_CODE() {
			return BRANCH_CODE;
		}
		public void setBRANCH_CODE(String bRANCH_CODE) {
			BRANCH_CODE = bRANCH_CODE;
		}
		public String getACCOUNT_NUMBER() {
			return ACCOUNT_NUMBER;
		}
		public void setACCOUNT_NUMBER(String aCCOUNT_NUMBER) {
			ACCOUNT_NUMBER = aCCOUNT_NUMBER;
		}
		public String getSUB_ACCOUNT_NUMBER() {
			return SUB_ACCOUNT_NUMBER;
		}
		public void setSUB_ACCOUNT_NUMBER(String sUB_ACCOUNT_NUMBER) {
			SUB_ACCOUNT_NUMBER = sUB_ACCOUNT_NUMBER;
		}
		public String getSURNAME() {
			return SURNAME;
		}
		public void setSURNAME(String sURNAME) {
			SURNAME = sURNAME;
		}
		public String getTITLE() {
			return TITLE;
		}
		public void setTITLE(String tITLE) {
			TITLE = tITLE;
		}
		public String getFORENAME_OR_INITIAL_1() {
			return FORENAME_OR_INITIAL_1;
		}
		public void setFORENAME_OR_INITIAL_1(String fORENAME_OR_INITIAL_1) {
			FORENAME_OR_INITIAL_1 = fORENAME_OR_INITIAL_1;
		}
		public String getFORENAME_OR_INITIAL_2() {
			return FORENAME_OR_INITIAL_2;
		}
		public void setFORENAME_OR_INITIAL_2(String fORENAME_OR_INITIAL_2) {
			FORENAME_OR_INITIAL_2 = fORENAME_OR_INITIAL_2;
		}
		public String getFORENAME_OR_INITIAL_3() {
			return FORENAME_OR_INITIAL_3;
		}
		public void setFORENAME_OR_INITIAL_3(String fORENAME_OR_INITIAL_3) {
			FORENAME_OR_INITIAL_3 = fORENAME_OR_INITIAL_3;
		}
		public String getRESIDENTIAL_ADDRESS_LINE_1() {
			return RESIDENTIAL_ADDRESS_LINE_1;
		}
		public void setRESIDENTIAL_ADDRESS_LINE_1(String rESIDENTIAL_ADDRESS_LINE_1) {
			RESIDENTIAL_ADDRESS_LINE_1 = rESIDENTIAL_ADDRESS_LINE_1;
		}
		public String getRESIDENTIAL_ADDRESS_LINE_2() {
			return RESIDENTIAL_ADDRESS_LINE_2;
		}
		public void setRESIDENTIAL_ADDRESS_LINE_2(String rESIDENTIAL_ADDRESS_LINE_2) {
			RESIDENTIAL_ADDRESS_LINE_2 = rESIDENTIAL_ADDRESS_LINE_2;
		}
		public String getRESIDENTIAL_ADDRESS_LINE_3() {
			return RESIDENTIAL_ADDRESS_LINE_3;
		}
		public void setRESIDENTIAL_ADDRESS_LINE_3(String rESIDENTIAL_ADDRESS_LINE_3) {
			RESIDENTIAL_ADDRESS_LINE_3 = rESIDENTIAL_ADDRESS_LINE_3;
		}
		public String getRESIDENTIAL_ADDRESS_LINE_4() {
			return RESIDENTIAL_ADDRESS_LINE_4;
		}
		public void setRESIDENTIAL_ADDRESS_LINE_4(String rESIDENTIAL_ADDRESS_LINE_4) {
			RESIDENTIAL_ADDRESS_LINE_4 = rESIDENTIAL_ADDRESS_LINE_4;
		}
		public String getPOSTAL_CODE_RESIDENTIAL() {
			return POSTAL_CODE_RESIDENTIAL;
		}
		public void setPOSTAL_CODE_RESIDENTIAL(String pOSTAL_CODE_RESIDENTIAL) {
			POSTAL_CODE_RESIDENTIAL = pOSTAL_CODE_RESIDENTIAL;
		}
		public String getOWNER_TENANT() {
			return OWNER_TENANT;
		}
		public void setOWNER_TENANT(String oWNER_TENANT) {
			OWNER_TENANT = oWNER_TENANT;
		}
		public String getPOSTAL_ADDRESS_LINE_1() {
			return POSTAL_ADDRESS_LINE_1;
		}
		public void setPOSTAL_ADDRESS_LINE_1(String pOSTAL_ADDRESS_LINE_1) {
			POSTAL_ADDRESS_LINE_1 = pOSTAL_ADDRESS_LINE_1;
		}
		public String getPOSTAL_ADDRESS_LINE_2() {
			return POSTAL_ADDRESS_LINE_2;
		}
		public void setPOSTAL_ADDRESS_LINE_2(String pOSTAL_ADDRESS_LINE_2) {
			POSTAL_ADDRESS_LINE_2 = pOSTAL_ADDRESS_LINE_2;
		}
		public String getPOSTAL_ADDRESS_LINE_3() {
			return POSTAL_ADDRESS_LINE_3;
		}
		public void setPOSTAL_ADDRESS_LINE_3(String pOSTAL_ADDRESS_LINE_3) {
			POSTAL_ADDRESS_LINE_3 = pOSTAL_ADDRESS_LINE_3;
		}
		public String getPOSTAL_ADDRESS_LINE_4() {
			return POSTAL_ADDRESS_LINE_4;
		}
		public void setPOSTAL_ADDRESS_LINE_4(String pOSTAL_ADDRESS_LINE_4) {
			POSTAL_ADDRESS_LINE_4 = pOSTAL_ADDRESS_LINE_4;
		}
		public String getPOST_CODE_POSTAL() {
			return POST_CODE_POSTAL;
		}
		public void setPOST_CODE_POSTAL(String pOST_CODE_POSTAL) {
			POST_CODE_POSTAL = pOST_CODE_POSTAL;
		}
		public String getOWNERSHIP_TYPE() {
			return OWNERSHIP_TYPE;
		}
		public void setOWNERSHIP_TYPE(String oWNERSHIP_TYPE) {
			OWNERSHIP_TYPE = oWNERSHIP_TYPE;
		}
		public String getREASON() {
			return REASON;
		}
		public void setREASON(String rEASON) {
			REASON = rEASON;
		}
		public String getPAYMENT_TYPE() {
			return PAYMENT_TYPE;
		}
		public void setPAYMENT_TYPE(String pAYMENT_TYPE) {
			PAYMENT_TYPE = pAYMENT_TYPE;
		}
		public String getTYPE_OF_ACCOUNT() {
			return TYPE_OF_ACCOUNT;
		}
		public void setTYPE_OF_ACCOUNT(String tYPE_OF_ACCOUNT) {
			TYPE_OF_ACCOUNT = tYPE_OF_ACCOUNT;
		}
		public BigDecimal getDATE_ACCOUNT_OPENED() {
			return DATE_ACCOUNT_OPENED;
		}
		public void setDATE_ACCOUNT_OPENED(BigDecimal dATE_ACCOUNT_OPENED) {
			DATE_ACCOUNT_OPENED = dATE_ACCOUNT_OPENED;
		}
		public BigDecimal getDEFERRED_PAYMENT_START_DATE() {
			return DEFERRED_PAYMENT_START_DATE;
		}
		public void setDEFERRED_PAYMENT_START_DATE(BigDecimal dEFERRED_PAYMENT_START_DATE) {
			DEFERRED_PAYMENT_START_DATE = dEFERRED_PAYMENT_START_DATE;
		}
		public BigDecimal getLAST_PAYMENT_RECEIVED_DATE() {
			return LAST_PAYMENT_RECEIVED_DATE;
		}
		public void setLAST_PAYMENT_RECEIVED_DATE(BigDecimal lAST_PAYMENT_RECEIVED_DATE) {
			LAST_PAYMENT_RECEIVED_DATE = lAST_PAYMENT_RECEIVED_DATE;
		}
		public BigDecimal getOPENING_BALANCE_CREDIT_LIMIT() {
			return OPENING_BALANCE_CREDIT_LIMIT;
		}
		public void setOPENING_BALANCE_CREDIT_LIMIT(BigDecimal oPENING_BALANCE_CREDIT_LIMIT) {
			OPENING_BALANCE_CREDIT_LIMIT = oPENING_BALANCE_CREDIT_LIMIT;
		}
		public BigDecimal getCURRENT_BALANCE() {
			return CURRENT_BALANCE;
		}
		public void setCURRENT_BALANCE(BigDecimal cURRENT_BALANCE) {
			CURRENT_BALANCE = cURRENT_BALANCE;
		}
		public String getCURRENT_BALANCE_DEBIT_IND() {
			return CURRENT_BALANCE_DEBIT_IND;
		}
		public void setCURRENT_BALANCE_DEBIT_IND(String cURRENT_BALANCE_DEBIT_IND) {
			CURRENT_BALANCE_DEBIT_IND = cURRENT_BALANCE_DEBIT_IND;
		}
		public BigDecimal getAMOUNT_OVERDUE() {
			return AMOUNT_OVERDUE;
		}
		public void setAMOUNT_OVERDUE(BigDecimal aMOUNT_OVERDUE) {
			AMOUNT_OVERDUE = aMOUNT_OVERDUE;
		}
		public BigDecimal getINSTALMENT_AMOUNT() {
			return INSTALMENT_AMOUNT;
		}
		public void setINSTALMENT_AMOUNT(BigDecimal iNSTALMENT_AMOUNT) {
			INSTALMENT_AMOUNT = iNSTALMENT_AMOUNT;
		}
		public BigDecimal getMONTHS_IN_ARREARS() {
			return MONTHS_IN_ARREARS;
		}
		public void setMONTHS_IN_ARREARS(BigDecimal mONTHS_IN_ARREARS) {
			MONTHS_IN_ARREARS = mONTHS_IN_ARREARS;
		}
		public String getSTATUS_CODE() {
			return STATUS_CODE;
		}
		public void setSTATUS_CODE(String sTATUS_CODE) {
			STATUS_CODE = sTATUS_CODE;
		}
		public BigDecimal getREPAYMENT_FREQUENCY() {
			return REPAYMENT_FREQUENCY;
		}
		public void setREPAYMENT_FREQUENCY(BigDecimal rEPAYMENT_FREQUENCY) {
			REPAYMENT_FREQUENCY = rEPAYMENT_FREQUENCY;
		}
		public BigDecimal getTERMS() {
			return TERMS;
		}
		public void setTERMS(BigDecimal tERMS) {
			TERMS = tERMS;
		}
		public BigDecimal getSTATUS_DATE() {
			return STATUS_DATE;
		}
		public void setSTATUS_DATE(BigDecimal sTATUS_DATE) {
			STATUS_DATE = sTATUS_DATE;
		}
		public String getOLD_SUPPLIER_BRANCH_CODE() {
			return OLD_SUPPLIER_BRANCH_CODE;
		}
		public void setOLD_SUPPLIER_BRANCH_CODE(String oLD_SUPPLIER_BRANCH_CODE) {
			OLD_SUPPLIER_BRANCH_CODE = oLD_SUPPLIER_BRANCH_CODE;
		}
		public String getOLD_ACCOUNT_NUMBER() {
			return OLD_ACCOUNT_NUMBER;
		}
		public void setOLD_ACCOUNT_NUMBER(String oLD_ACCOUNT_NUMBER) {
			OLD_ACCOUNT_NUMBER = oLD_ACCOUNT_NUMBER;
		}
		public String getOLD_SUB_ACCOUNT_NO() {
			return OLD_SUB_ACCOUNT_NO;
		}
		public void setOLD_SUB_ACCOUNT_NO(String oLD_SUB_ACCOUNT_NO) {
			OLD_SUB_ACCOUNT_NO = oLD_SUB_ACCOUNT_NO;
		}
		public String getOLD_SUPPLIER_REFERENCE_NO() {
			return OLD_SUPPLIER_REFERENCE_NO;
		}
		public void setOLD_SUPPLIER_REFERENCE_NO(String oLD_SUPPLIER_REFERENCE_NO) {
			OLD_SUPPLIER_REFERENCE_NO = oLD_SUPPLIER_REFERENCE_NO;
		}
		public String getH_TELEPHONE() {
			return H_TELEPHONE;
		}
		public void setH_TELEPHONE(String h_TELEPHONE) {
			H_TELEPHONE = h_TELEPHONE;
		}
		public String getCELLULAR_TELEPHONE() {
			return CELLULAR_TELEPHONE;
		}
		public void setCELLULAR_TELEPHONE(String cELLULAR_TELEPHONE) {
			CELLULAR_TELEPHONE = cELLULAR_TELEPHONE;
		}
		public String getW_TELEPHONE() {
			return W_TELEPHONE;
		}
		public void setW_TELEPHONE(String w_TELEPHONE) {
			W_TELEPHONE = w_TELEPHONE;
		}
		public String getEMPLOYER_DETAIL() {
			return EMPLOYER_DETAIL;
		}
		public void setEMPLOYER_DETAIL(String eMPLOYER_DETAIL) {
			EMPLOYER_DETAIL = eMPLOYER_DETAIL;
		}
		public BigDecimal getINCOME() {
			return INCOME;
		}
		public void setINCOME(BigDecimal iNCOME) {
			INCOME = iNCOME;
		}
		public String getINCOME_FREQUENCY() {
			return INCOME_FREQUENCY;
		}
		public void setINCOME_FREQUENCY(String iNCOME_FREQUENCY) {
			INCOME_FREQUENCY = iNCOME_FREQUENCY;
		}
		public String getOCCUPATION() {
			return OCCUPATION;
		}
		public void setOCCUPATION(String oCCUPATION) {
			OCCUPATION = oCCUPATION;
		}
		public String getTHIRD_PARTY_NAME() {
			return THIRD_PARTY_NAME;
		}
		public void setTHIRD_PARTY_NAME(String tHIRD_PARTY_NAME) {
			THIRD_PARTY_NAME = tHIRD_PARTY_NAME;
		}
		public BigDecimal getACCOUNT_SOLD_TO_THIRD_PARTY() {
			return ACCOUNT_SOLD_TO_THIRD_PARTY;
		}
		public void setACCOUNT_SOLD_TO_THIRD_PARTY(BigDecimal aCCOUNT_SOLD_TO_THIRD_PARTY) {
			ACCOUNT_SOLD_TO_THIRD_PARTY = aCCOUNT_SOLD_TO_THIRD_PARTY;
		}
		public BigDecimal getNO_OF_PARTICIPANTS_JOINT() {
			return NO_OF_PARTICIPANTS_JOINT;
		}
		public void setNO_OF_PARTICIPANTS_JOINT(BigDecimal nO_OF_PARTICIPANTS_JOINT) {
			NO_OF_PARTICIPANTS_JOINT = nO_OF_PARTICIPANTS_JOINT;
		}
		public String getFILLER() {
			return FILLER;
		}
		public void setFILLER(String fILLER) {
			FILLER = fILLER;
		}
		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}
		public void setREPORT_DATE(Date rEPORT_DATE) {
			REPORT_DATE = rEPORT_DATE;
		}
		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}
		public void setREPORT_VERSION(BigDecimal rEPORT_VERSION) {
			REPORT_VERSION = rEPORT_VERSION;
		}
		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}
		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}
		public String getREPORT_CODE() {
			return REPORT_CODE;
		}
		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}
		public String getREPORT_DESC() {
			return REPORT_DESC;
		}
		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}
		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}
		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}
		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}
		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}
		public String getDEL_FLG() {
			return DEL_FLG;
		}
		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}
		public Date getREPORT_RESUBDATE() {
			return REPORT_RESUBDATE;
		}
		public void setREPORT_RESUBDATE(Date rEPORT_RESUBDATE) {
			REPORT_RESUBDATE = rEPORT_RESUBDATE;
		}
			
 		}
 	
 	
 	
 // ---- constants (put right after CRB_COLUMNS) ----

 // The 56 Excel columns A..BD = the first 56 entries of CRB_COLUMNS (DATA_RECORD .. FILLER)
 private static final String[] DATA_COLUMNS = Arrays.copyOfRange(CRB_COLUMNS, 0, 56);

 // Columns that are NUMBER in the table; everything else is VARCHAR2
 private static final Set<String> NUMERIC_COLUMNS = new HashSet<String>(Arrays.asList(
 		"OMANG_ID", "DATE_OF_BIRTH", "DATE_ACCOUNT_OPENED", "DEFERRED_PAYMENT_START_DATE",
 		"LAST_PAYMENT_RECEIVED_DATE", "OPENING_BALANCE_CREDIT_LIMIT", "CURRENT_BALANCE", "AMOUNT_OVERDUE",
 		"INSTALMENT_AMOUNT", "MONTHS_IN_ARREARS", "REPAYMENT_FREQUENCY", "TERMS", "STATUS_DATE", "INCOME",
 		"ACCOUNT_SOLD_TO_THIRD_PARTY", "NO_OF_PARTICIPANTS_JOINT"));

 // ---- the upload method ----

 // ------------------------------
 // Reads the uploaded Excel (output format) and stores it in CRB_SUMMARYTABLE
 //  - row 1 is the header and is skipped
 //  - only rows whose first column is "D" are loaded (the "H" header record and any trailer are skipped)
 //  - existing rows for the same REPORT_DATE are replaced (re-upload = overwrite)
 //  - everything is parsed BEFORE the table is touched, so a bad file changes nothing
 // ------------------------------
 public int uploadCRBFile(MultipartFile file, String todate) throws Exception {

 	Date reportDate = parseDateRobustly(todate);
 	if (reportDate == null) {
 		throw new IllegalArgumentException("Invalid report date: " + todate);
 	}
 	if (file == null || file.isEmpty()) {
 		throw new IllegalArgumentException("The selected file is empty.");
 	}
 	String fileName = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase();
 	if (!(fileName.endsWith(".xlsx") || fileName.endsWith(".xls"))) {
 		throw new IllegalArgumentException("Please upload an Excel file (.xlsx or .xls).");
 	}

 	java.sql.Date sqlReportDate = new java.sql.Date(reportDate.getTime());
 	String datePrefix = new SimpleDateFormat("yyyyMMdd").format(reportDate);
 	DataFormatter fmt = new DataFormatter();
 	List<Object[]> batch = new ArrayList<Object[]>();

 	// ---------- 1. parse the whole file ----------
 	try (InputStream is = file.getInputStream(); Workbook wb = WorkbookFactory.create(is)) {
 		Sheet sheet = wb.getSheetAt(0);
 		int seq = 0;

 		for (int r = 1; r <= sheet.getLastRowNum(); r++) { // r = 0 is the header row
 			Row row = sheet.getRow(r);
 			if (row == null) {
 				continue;
 			}
 			String recType = fmt.formatCellValue(row.getCell(0));
 			if (recType == null || !recType.trim().equalsIgnoreCase("D")) {
 				continue; // skip the "H" header record / trailer / blank rows
 			}

 			Object[] p = new Object[1 + DATA_COLUMNS.length + 8];
 			p[0] = datePrefix + "_" + String.format("%05d", ++seq); // SNO (max 20 chars)

 			for (int c = 0; c < DATA_COLUMNS.length; c++) {
 				String raw = fmt.formatCellValue(row.getCell(c));
 				if (NUMERIC_COLUMNS.contains(DATA_COLUMNS[c])) {
 					String t = raw == null ? "" : raw.trim();
 					try {
 						p[1 + c] = t.isEmpty() ? null : new BigDecimal(t);
 					} catch (NumberFormatException nfe) {
 						throw new IllegalArgumentException("Row " + (r + 1) + ", column " + DATA_COLUMNS[c]
 								+ ": '" + raw + "' is not a valid number.");
 					}
 				} else {
 					p[1 + c] = raw; // keep the fixed-width text as it is in the file
 				}
 			}

 			int i = 1 + DATA_COLUMNS.length;
 			p[i++] = sqlReportDate; // REPORT_DATE
 			p[i++] = null; // REPORT_VERSION  (live table - set if you version it)
 			p[i++] = null; // REPORT_FREQUENCY
 			p[i++] = "CRB"; // REPORT_CODE
 			p[i++] = "CRB"; // REPORT_DESC
 			p[i++] = "Y"; // ENTITY_FLG
 			p[i++] = "N"; // MODIFY_FLG
 			p[i++] = "N"; // DEL_FLG
 			batch.add(p);
 		}
 	}

 	if (batch.isEmpty()) {
 		throw new IllegalArgumentException("No data records (rows starting with 'D') found in the file.");
 	}

 	// ---------- 2. build the INSERT ----------
 	StringBuilder cols = new StringBuilder("SNO");
 	StringBuilder marks = new StringBuilder("?");
 	for (String c : DATA_COLUMNS) {
 		cols.append(", ").append(c);
 		marks.append(", ?");
 	}
 	cols.append(", REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG");
 	marks.append(", ?, ?, ?, ?, ?, ?, ?, ?");
 	String insertSql = "INSERT INTO " + SUMMARY_TABLE + " (" + cols + ") VALUES (" + marks + ")";

 	// ---------- 3. replace the data for this report date ----------
 	jdbcTemplate.update("DELETE FROM " + SUMMARY_TABLE + " WHERE REPORT_DATE = ?", sqlReportDate);
 	jdbcTemplate.batchUpdate(insertSql, batch);

 	logger.info("CRB upload: {} records stored for {}", batch.size(), todate);
 	return batch.size();
 }

}