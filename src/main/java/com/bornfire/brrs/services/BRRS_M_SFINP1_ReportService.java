package com.bornfire.brrs.services;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.IdClass;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Objects;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfileRep;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Component
@Service
public class BRRS_M_SFINP1_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SFINP1_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	AuditService auditService;

	@Autowired
	UserProfileRep userProfileRep;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// ------------------------------
	// Gets the main view 
	// ------------------------------
	public ModelAndView getM_SFINP1View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		System.out.println("🟢 getM_SFINP1View() called");

		String userid = (String) req1.getSession().getAttribute("USERID");

		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		String displaymode = "summary";

		try {
			Date reportDate = dateformat.parse(todate);

// ======= CASE 1: ARCHIVAL =======
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				System.out.println("📦 Fetching ARCHIVAL data for version: " + version);

				List<M_SFINP1_Archival_Summary_Entity> mainList = jdbcTemplate.query(
						"select * from BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ? and REPORT_VERSION = ?",
						new Object[] { reportDate, version }, new M_SFINP1ArchivalSummaryRowMapper());
				List<M_SFINP1_Archival_Summary_Manual_Entity> archivalList = jdbcTemplate.query(
						"SELECT * FROM BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY_MANUAL WHERE REPORT_DATE = ?",
						new Object[] { reportDate }, new M_SFINP1ArchivalSummaryManualRowMapper());

				mv.addObject("reportsummary", mainList);
				mv.addObject("reportsummary1", archivalList);

// ======= CASE 2: RESUB (reuses the archival tables — a resub is just an archival version viewed via a different path) =======
			} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				System.out.println("♻️ Fetching RESUB data for version: " + version);

				List<M_SFINP1_Archival_Summary_Entity> mainList = jdbcTemplate.query(
						"select * from BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ? and REPORT_VERSION = ?",
						new Object[] { reportDate, version }, new M_SFINP1ArchivalSummaryRowMapper());
				List<M_SFINP1_Archival_Summary_Manual_Entity> archivalList = jdbcTemplate.query(
						"SELECT * FROM BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY_MANUAL WHERE REPORT_DATE = ?",
						new Object[] { reportDate }, new M_SFINP1ArchivalSummaryManualRowMapper());

				mv.addObject("reportsummary", mainList);
				mv.addObject("reportsummary1", archivalList);
				displaymode = "resubSummary";

			} else {

				List<M_SFINP1_Summary_Entity> T1Master = new ArrayList<M_SFINP1_Summary_Entity>();
				List<M_SFINP1_Summary_Manual_Entity> T2Master = new ArrayList<M_SFINP1_Summary_Manual_Entity>();
				try {
					Date d1 = dateformat.parse(todate);

					T1Master = jdbcTemplate.query("SELECT * FROM BRRS_M_SFINP1_SUMMARYTABLE WHERE REPORT_DATE = ?",
							new Object[] { dateformat.parse(todate) }, new M_SFINP1SummaryRowMapper());
					T2Master = jdbcTemplate.query(
							"SELECT * FROM BRRS_M_SFINP1_SUMMARYTABLE_MANUAL WHERE REPORT_DATE = ?",
							new Object[] { dateformat.parse(todate) }, new M_SFINP1SummaryManualRowMapper());
					System.out.println("T2Master size " + T2Master.size());
					mv.addObject("report_date", dateformat.format(d1));

				} catch (ParseException e) {
					e.printStackTrace();
				}
				mv.addObject("reportsummary", T1Master);
				mv.addObject("reportsummary1", T2Master);

				// Flag set when this live view was reached via the resub "Modify" promotion,
				// so the HTML can show the Finalise button only in that context.
				String fromResub = req1.getParameter("fromResub");
				mv.addObject("fromResub", "true".equalsIgnoreCase(fromResub));
			}

// ======= Common view settings =======
			mv.setViewName("BRRS/M_SFINP1");
			mv.addObject("displaymode", displaymode);
			System.out.println("✅ View set to: " + mv.getViewName());

		} catch (ParseException e) {
			System.err.println("❌ Error parsing todate: " + todate);
			e.printStackTrace();
		} catch (Exception ex) {
			System.err.println("❌ Unexpected error in getM_SFINP1View()");
			ex.printStackTrace();
		}

		return mv;
	}

	// ------------------------------
	// Gets the current detail view 
	// ------------------------------
	public ModelAndView getM_SFINP1currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String Filter, String type, String version, HttpServletRequest req1,
			Model md) {
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
		String displaymode = "Details";
		try {
			Date parsedDate = null;
			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}
			String rowId = null;
			String columnId = null;
			// ✅ Split filter string into rowId & columnId
			if (Filter != null && Filter.contains(",")) {
				String[] parts = Filter.split(",");
				if (parts.length >= 2) {
					rowId = parts[0];
					columnId = parts[1];
				}
			}
			System.out.println(type);
			if ("ARCHIVAL".equals(type) && version != null) {

				List<java.util.Map<String, Object>> dbRows = jdbcTemplate.queryForList("SELECT DISTINCT REPORT_DATE, DATA_ENTRY_VERSION FROM BRRS_M_SFINP1_ARCHIVALTABLE_DETAIL");
				for (java.util.Map<String, Object> r : dbRows) {
					System.out.println("DB Row: " + r.get("REPORT_DATE") + " - Version: [" + r.get("DATA_ENTRY_VERSION") + "]");
				}
				System.out.println("Querying with parsedDate: " + parsedDate + " - version: [" + version + "]");

				List<M_SFINP1_Archival_Detail_Entity> T1Dt1;

				if (rowId != null && columnId != null) {
					T1Dt1 = jdbcTemplate.query(
							"select * from BRRS_M_SFINP1_ARCHIVALTABLE_DETAIL where ROW_ID =? and COLUMN_ID=? AND REPORT_DATE=? AND DATA_ENTRY_VERSION=?",
							new Object[] { rowId, columnId, parsedDate, version },
							new M_SFINP1ArchivalDetailRowMapper());
				} else {
					T1Dt1 = jdbcTemplate.query(
							"SELECT * FROM BRRS_M_SFINP1_ARCHIVALTABLE_DETAIL WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DATA_ENTRY_VERSION = ?",
							new Object[] { parsedDate, version }, new M_SFINP1ArchivalDetailRowMapper());

					totalPages = jdbcTemplate.queryForObject(
							"SELECT COUNT(*) FROM BRRS_M_SFINP1_ARCHIVALTABLE_DETAIL WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DATA_ENTRY_VERSION = ?",
							new Object[] { parsedDate, version }, Integer.class);

					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);

				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
			} else if ("RESUB".equals(type) && version != null) {
				// Resub detail reuses the archival detail table — a resub is just an archival version viewed via a different path.
				System.out.println("Querying RESUB with parsedDate: " + parsedDate + " - version: [" + version + "]");

				List<M_SFINP1_Archival_Detail_Entity> T1Dt1;

				if (rowId != null && columnId != null) {
					T1Dt1 = jdbcTemplate.query(
							"select * from BRRS_M_SFINP1_ARCHIVALTABLE_DETAIL where ROW_ID =? and COLUMN_ID=? AND REPORT_DATE=? AND DATA_ENTRY_VERSION=?",
							new Object[] { rowId, columnId, parsedDate, version },
							new M_SFINP1ArchivalDetailRowMapper());
				} else {
					T1Dt1 = jdbcTemplate.query(
							"SELECT * FROM BRRS_M_SFINP1_ARCHIVALTABLE_DETAIL WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DATA_ENTRY_VERSION = ?",
							new Object[] { parsedDate, version }, new M_SFINP1ArchivalDetailRowMapper());

					totalPages = jdbcTemplate.queryForObject(
							"SELECT COUNT(*) FROM BRRS_M_SFINP1_ARCHIVALTABLE_DETAIL WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DATA_ENTRY_VERSION = ?",
							new Object[] { parsedDate, version }, Integer.class);

					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				displaymode = "resubDetail";

				System.out.println("RESUB COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
			} else {
				// 🔹 Current branch
				List<M_SFINP1_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = jdbcTemplate.query(
							"select * from BRRS_M_SFINP1_DETAILTABLE where ROW_ID =? and COLUMN_ID=? AND REPORT_DATE=?",
							new Object[] { rowId, columnId, parsedDate }, new M_SFINP1DetailRowMapper());
				} else {
					T1Dt1 = jdbcTemplate.query(
							"select * from BRRS_M_SFINP1_DETAILTABLE where REPORT_DATE = ? offset ? rows fetch next ? rows only",
							new Object[] { parsedDate, currentPage, pageSize }, new M_SFINP1DetailRowMapper());
					totalPages = jdbcTemplate.queryForObject(
							"select count(*) from BRRS_M_SFINP1_DETAILTABLE where REPORT_DATE = ?",
							new Object[] { parsedDate }, Integer.class);
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
		// ✅ Common attributes
		// Flag set when this view was reached via the resub "Modify" promotion, so the HTML
		// can show the Finalise button only in that context — mirrors getM_SFINP1View().
		String fromResub = req1.getParameter("fromResub");
		mv.addObject("fromResub", "true".equalsIgnoreCase(fromResub));
		mv.setViewName("BRRS/M_SFINP1");
		mv.addObject("displaymode", displaymode);
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / pageSize));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}

	// ------------------------------
	// Updates the report manual values and updates averages in summary table
	// ------------------------------

	@Transactional
	public void updateReport(M_SFINP1_Summary_Manual_Entity updatedEntity) {

		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

		// =====================================================
		// STEP 1 : FETCH MANUAL TABLE RECORD
		// =====================================================

		List<M_SFINP1_Summary_Manual_Entity> list = jdbcTemplate.query(
				"SELECT * FROM BRRS_M_SFINP1_SUMMARYTABLE_MANUAL WHERE REPORT_DATE = ?",
				new Object[] { updatedEntity.getREPORT_DATE() }, new M_SFINP1SummaryManualRowMapper());
		if (list.isEmpty()) {
			throw new RuntimeException("Record not found for REPORT_DATE : " + updatedEntity.getREPORT_DATE());
		}
		M_SFINP1_Summary_Manual_Entity existing = list.get(0);

		// FIX: Create audit copy of the ORIGINAL database state before applying changes
		M_SFINP1_Summary_Manual_Entity oldcopy = new M_SFINP1_Summary_Manual_Entity();
		BeanUtils.copyProperties(existing, oldcopy);

		// =====================================================
		// STEP 2 : UPDATE ONLY MANUAL FIELDS
		// =====================================================

		try {

			int[] specialRows = { 14, 34, 37, 39, 42, 43, 50, 51, 52, 57, 59 };

			for (int i : specialRows) {

				String prefix = "R" + i + "_";
				String field = "MONTH_END";

				String getterName = "get" + prefix + field;
				String setterName = "set" + prefix + field;

				try {

					Method getter = M_SFINP1_Summary_Manual_Entity.class.getMethod(getterName);
					Method setter = M_SFINP1_Summary_Manual_Entity.class.getMethod(setterName, getter.getReturnType());

					Object newValue = getter.invoke(updatedEntity);
					Object existingValue = getter.invoke(existing);

					// Normalize nulls vs empty strings to prevent audit bloat
					String currentValStr = (existingValue == null) ? "" : existingValue.toString().trim();
					String newValStr = (newValue == null) ? "" : newValue.toString().trim();

					// If values are functionally identical, skip updating to avoid dirtying fields
					if (currentValStr.equals(newValStr)) {
						continue;
					}

					setter.invoke(existing, newValue);

				} catch (NoSuchMethodException e) {
					continue;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// =====================================================
		// STEP 3 : SAVE MANUAL TABLE
		// =====================================================

		saveSummaryManual(existing);

		// =====================================================
		// STEP 4 : FETCH MAIN SUMMARY ENTITY
		// =====================================================

		List<M_SFINP1_Summary_Entity> sumList = jdbcTemplate.query(
				"SELECT * FROM BRRS_M_SFINP1_SUMMARYTABLE WHERE REPORT_DATE = ?",
				new Object[] { updatedEntity.getREPORT_DATE() }, new M_SFINP1SummaryRowMapper());
		if (sumList.isEmpty()) {
			throw new RuntimeException("Summary Record not found");
		}
		M_SFINP1_Summary_Entity summaryEntity = sumList.get(0);

		// =====================================================
		// STEP 5 : PREVIOUS MONTH DATE
		// =====================================================

		Date reportDate = updatedEntity.getREPORT_DATE();

		Calendar cal = Calendar.getInstance();
		cal.setTime(reportDate);
		cal.add(Calendar.MONTH, -1);

		Date previousMonthDate = cal.getTime();

		// =====================================================
		// STEP 6 : FETCH PREVIOUS MONTH ARCHIVAL DATA
		// =====================================================

		List<M_SFINP1_Archival_Summary_Manual_Entity> archList = jdbcTemplate.query(
				"SELECT * FROM BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY_MANUAL WHERE REPORT_DATE = ?",
				new Object[] { previousMonthDate }, new M_SFINP1ArchivalSummaryManualRowMapper());
		M_SFINP1_Archival_Summary_Manual_Entity archival = archList.isEmpty() ? null : archList.get(0);

		// =====================================================
		// STEP 7 : CALCULATE AVERAGES
		// =====================================================

		BigDecimal r34Avg = calculateAverage(existing.getR34_MONTH_END(),
				archival != null ? archival.getR34_MONTH_END() : null);

		BigDecimal r37Avg = calculateAverage(existing.getR37_MONTH_END(),
				archival != null ? archival.getR37_MONTH_END() : null);

		BigDecimal r39Avg = calculateAverage(existing.getR39_MONTH_END(),
				archival != null ? archival.getR39_MONTH_END() : null);

		BigDecimal r43Avg = calculateAverage(existing.getR43_MONTH_END(),
				archival != null ? archival.getR43_MONTH_END() : null);
		BigDecimal r42Avg = calculateAverage(existing.getR42_MONTH_END(),
				archival != null ? archival.getR42_MONTH_END() : null);
		BigDecimal r50Avg = calculateAverage(existing.getR50_MONTH_END(),
				archival != null ? archival.getR50_MONTH_END() : null);

		BigDecimal r51Avg = calculateAverage(existing.getR51_MONTH_END(),
				archival != null ? archival.getR51_MONTH_END() : null);

		BigDecimal r52Avg = calculateAverage(existing.getR52_MONTH_END(),
				archival != null ? archival.getR52_MONTH_END() : null);

		BigDecimal r57Avg = calculateAverage(existing.getR57_MONTH_END(),
				archival != null ? archival.getR57_MONTH_END() : null);

		BigDecimal r59Avg = calculateAverage(existing.getR59_MONTH_END(),
				archival != null ? archival.getR59_MONTH_END() : null);

		// =====================================================
		// STEP 8 : STORE INTO MAIN SUMMARY ENTITY
		// =====================================================

		summaryEntity.setR34_average(r34Avg);
		summaryEntity.setR37_average(r37Avg);
		summaryEntity.setR39_average(r39Avg);
		summaryEntity.setR43_average(r43Avg);
		summaryEntity.setR42_average(r42Avg);
		summaryEntity.setR50_average(r50Avg);
		summaryEntity.setR51_average(r51Avg);
		summaryEntity.setR52_average(r52Avg);
		summaryEntity.setR57_average(r57Avg);
		summaryEntity.setR59_average(r59Avg);

		// =====================================================
		// STEP 9 : SAVE MAIN SUMMARY TABLE
		// =====================================================

		jdbcTemplate.update(
				"UPDATE BRRS_M_SFINP1_SUMMARYTABLE SET "
						+ "R34_AVERAGE = ?, R37_AVERAGE = ?, R39_AVERAGE = ?, R43_AVERAGE = ?, "
						+ "R42_AVERAGE = ?, R50_AVERAGE = ?, R51_AVERAGE = ?, R52_AVERAGE = ?, "
						+ "R57_AVERAGE = ?, R59_AVERAGE = ? WHERE REPORT_DATE = ?",
				summaryEntity.getR34_average(), summaryEntity.getR37_average(), summaryEntity.getR39_average(),
				summaryEntity.getR43_average(), summaryEntity.getR42_average(), summaryEntity.getR50_average(),
				summaryEntity.getR51_average(), summaryEntity.getR52_average(), summaryEntity.getR57_average(),
				summaryEntity.getR59_average(), summaryEntity.getReport_date());

		// =====================================================
		// STEP 10 : EVALUATE AND LOG AUDIT TRAIL
		// =====================================================
		String changes = auditService.getChanges(oldcopy, existing);
		System.out.println("M_SFINP1 Changes Length = " + changes.length());

		if (changes != null && !changes.isEmpty()) {
			auditService.compareEntitiesmanual(oldcopy, existing, updatedEntity.getREPORT_DATE().toString(),
					"M_SFINP1 Summary Screen", "BRRS_M_SFINP1_SUMMARY_MANUAL");
		}

		System.out.println("Updated Successfully");
	}

	// ------------------------------
	// Calculates the average between current and previous values
	// ------------------------------
	private BigDecimal calculateAverage(BigDecimal current, BigDecimal previous) {

		BigDecimal curr = current == null ? BigDecimal.ZERO : current.abs();

		BigDecimal prev = previous == null ? BigDecimal.ZERO : previous.abs();

		return curr.add(prev).divide(BigDecimal.valueOf(2), 0, RoundingMode.HALF_UP);
	}

	// ------------------------------
	// Generates the detail excel file for M_SFINP1
	// ------------------------------
	public byte[] getM_SFINP1DetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRS_M_SFINP1 Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRRS_M_SFINP1Details");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "CREDIT EQUIVALENT", "DEBIT EQUIVALENT",
					"REPORT LABEL", "REPORT ADDL CRETIRIA", "REPORT_DATE" };
			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				// Amount columns: ACCT BALANCE (i=3) and average (i=4)
				if (i == 3 || i == 4) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}
			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_SFINP1_Detail_Entity> reportData = jdbcTemplate.query(
					"select * from BRRS_M_SFINP1_DETAILTABLE where REPORT_DATE = ?", new Object[] { parsedToDate },
					new M_SFINP1DetailRowMapper());
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_SFINP1_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());
					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInPula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);
					// Average (right aligned, 3 decimal places)
					balanceCell = row.createCell(4);
					if (item.getAverage() != null) {
						balanceCell.setCellValue(item.getAverage().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);
					row.createCell(5).setCellValue(item.getRowId());
					row.createCell(6).setCellValue(item.getColumnId());
					row.createCell(7)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");
					// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						if (j != 3 && j != 4) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for BRRS_M_SFINP1 — only header will be written.");
			}
			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();
		} catch (Exception e) {
			logger.error("Error generating BRRS_M_SFINP1 Excel", e);
			return null; // important
		}
	}

	// ------------------------------
	// Generates the detail excel
	// ------------------------------
	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_M_SFINP1 ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("MSFinP1Detail");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "CREDIT EQUIVALENT", "DEBIT EQUIVALENT",
					"REPORT LABEL", "REPORT ADDL CRETIRIA", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				// Amount columns: ACCT BALANCE (i=3) and average (i=4)
				if (i == 3 || i == 4) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_SFINP1_Archival_Detail_Entity> reportData = jdbcTemplate.query(
					"SELECT * FROM BRRS_M_SFINP1_ARCHIVALTABLE_DETAIL WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DATA_ENTRY_VERSION = ?",
					new Object[] { parsedToDate, version }, new M_SFINP1ArchivalDetailRowMapper());

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_SFINP1_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInPula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					// Average (right aligned, 3 decimal places)
					balanceCell = row.createCell(4);
					if (item.getAverage() != null) {
						balanceCell.setCellValue(item.getAverage().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(5).setCellValue(item.getRowId());
					row.createCell(6).setCellValue(item.getColumnId());
					row.createCell(7)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						if (j != 3 && j != 4) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for BRRS_M_SFINP1 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_M_SFINP1Excel", e);
			return new byte[0];
		}
	}

	// ------------------------------
	// Retrieves the list of resub
	// ------------------------------
	public List<Object[]> getM_SFINP1Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_SFINP1_Archival_Summary_Entity> latestArchivalList = jdbcTemplate.query(
					"SELECT * FROM BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC",
					new M_SFINP1ArchivalSummaryRowMapper());

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_SFINP1_Archival_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_SFINP1 Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	// ------------------------------
	// Retrieves the list of archival summary 
	// ------------------------------
	public List<Object[]> getM_SFINP1Archival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			jdbcTemplate.query("SELECT * FROM BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY WHERE ROWNUM = 1", rs -> {
				java.sql.ResultSetMetaData md = rs.getMetaData();
				System.out.print("Columns of BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY: ");
				for (int i = 1; i <= md.getColumnCount(); i++) {
					System.out.print(md.getColumnName(i) + ", ");
				}
				System.out.println();
			});
			jdbcTemplate.query("SELECT * FROM BRRS_M_SFINP1_ARCHIVALTABLE_DETAIL WHERE ROWNUM = 1", rs -> {
				java.sql.ResultSetMetaData md = rs.getMetaData();
				System.out.print("Columns of BRRS_M_SFINP1_ARCHIVALTABLE_DETAIL: ");
				for (int i = 1; i <= md.getColumnCount(); i++) {
					System.out.print(md.getColumnName(i) + ", ");
				}
				System.out.println();
			});
			List<M_SFINP1_Archival_Summary_Entity> repoData = jdbcTemplate.query(
					"SELECT * FROM BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC",
					new M_SFINP1ArchivalSummaryRowMapper());

			if (repoData != null && !repoData.isEmpty()) {
				for (M_SFINP1_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_SFINP1_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  M_SFINP1  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// ------------------------------
	// Determines the PDF page size based on Excel column counts
	// ------------------------------
	private Rectangle getPageSizeForColumns(int columnCount) {

		// Approx width per column (10 pts per column)
		float requiredWidth = columnCount * 55;

		if (requiredWidth <= PageSize.A4.getWidth())
			return PageSize.A4.rotate();

		if (requiredWidth <= PageSize.A3.getWidth())
			return PageSize.A3.rotate();

		if (requiredWidth <= PageSize.A2.getWidth())
			return PageSize.A2.rotate();

		if (requiredWidth <= PageSize.A1.getWidth())
			return PageSize.A1.rotate();

		if (requiredWidth <= PageSize.A0.getWidth())
			return PageSize.A0.rotate();

		// If still bigger → custom large canvas
		return new Rectangle(requiredWidth + 100, PageSize.A0.getHeight()).rotate();
	}

	// ------------------------------
	// Finds the last column index that contains data in a sheet
	// ------------------------------
	private int findLastUsedColumn(Sheet sheet) {
		int lastCol = 0;
		for (int r = 0; r <= sheet.getLastRowNum(); r++) {
			Row row = sheet.getRow(r);
			if (row == null)
				continue;

			short lastCell = row.getLastCellNum();
			if (lastCell > lastCol) {
				// Check if there is at least one non-empty cell
				for (int c = lastCol; c < lastCell; c++) {
					Cell cell = row.getCell(c);
					if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK
							&& !cell.toString().trim().isEmpty()) {
						lastCol = lastCell;
						break;
					}
				}
			}
		}
		return lastCol;
	}

	// ------------------------------
	// Counts the total number of columns used in a sheet
	// ------------------------------
	private int getUsedColumnCount(Sheet sheet) {
		int maxCol = 0;

		for (Row row : sheet) {
			if (row == null)
				continue;
			if (row.getLastCellNum() > maxCol) {
				maxCol = row.getLastCellNum();
			}
		}

		// Now remove trailing blank columns
		boolean columnUsed;

		for (int col = maxCol - 1; col >= 0; col--) {
			columnUsed = false;
			for (Row row : sheet) {
				Cell cell = (row == null) ? null : row.getCell(col);
				if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK
						&& !new DataFormatter().formatCellValue(cell).trim().isEmpty()) {
					columnUsed = true;
					break;
				}
			}
			if (columnUsed) {
				return col + 1; // This is last actual column
			}
		}

		return 1; // Fallback
	}

	// ------------------------------
	// Converts an Excel color object to an iText BaseColor
	// ------------------------------
	private BaseColor toBaseColor(Color excelColor) {
		if (excelColor == null)
			return BaseColor.WHITE;
		return new BaseColor(excelColor.getRed(), excelColor.getGreen(), excelColor.getBlue());
	}

	// ------------------------------
	// Applies cell styling options from an Excel cell to a PDF cell
	// ------------------------------
	private void applyCellStyleToPdf(Cell excelCell, PdfPCell pdfCell, Workbook workbook) {
		if (excelCell == null)
			return;
		CellStyle style = excelCell.getCellStyle();
		if (style == null)
			return;

		// ===== Background color (XSSF) =====
		try {
			if (workbook instanceof XSSFWorkbook) {
				XSSFCellStyle xssfStyle = (XSSFCellStyle) style;
				XSSFColor bg = xssfStyle.getFillForegroundXSSFColor();
				if (bg != null && bg.getRGB() != null) {
					byte[] rgb = bg.getRGB();
					pdfCell.setBackgroundColor(new BaseColor(rgb[0] & 0xFF, rgb[1] & 0xFF, rgb[2] & 0xFF));
				}
			}
		} catch (Throwable t) {
			// ignore background color extraction errors
		}

		// ===== Alignment (use reflection to support both old and new POI) =====
		int pdfAlignment = Element.ALIGN_LEFT;
		try {
			java.lang.reflect.Method m = style.getClass().getMethod("getAlignment");
			Object alignVal = m.invoke(style);
			if (alignVal instanceof Short) {
				short s = ((Short) alignVal).shortValue();
				// Old POI short codes: 1 = LEFT, 2 = CENTER, 3 = RIGHT (common mapping)
				if (s == 2)
					pdfAlignment = Element.ALIGN_CENTER;
				else if (s == 3)
					pdfAlignment = Element.ALIGN_RIGHT;
				else
					pdfAlignment = Element.ALIGN_LEFT;
			} else if (alignVal != null) {
				// New POI: enum HorizontalAlignment (toString -> "CENTER"/"RIGHT"/"LEFT")
				String name = alignVal.toString();
				if ("CENTER".equalsIgnoreCase(name))
					pdfAlignment = Element.ALIGN_CENTER;
				else if ("RIGHT".equalsIgnoreCase(name))
					pdfAlignment = Element.ALIGN_RIGHT;
				else
					pdfAlignment = Element.ALIGN_LEFT;
			}
		} catch (Throwable t) {
			// fallback to left alignment
			pdfAlignment = Element.ALIGN_LEFT;
		}
		pdfCell.setHorizontalAlignment(pdfAlignment);

		// ===== Borders (reflection: support short or enum) =====
		boolean leftBorder = false, rightBorder = false, topBorder = false, bottomBorder = false;
		try {
			leftBorder = borderPresent(style, "Left");
			rightBorder = borderPresent(style, "Right");
			topBorder = borderPresent(style, "Top");
			bottomBorder = borderPresent(style, "Bottom");
		} catch (Throwable t) {
			// ignore -> keep false defaults
		}
		pdfCell.setBorderWidthLeft(leftBorder ? 0.8f : 0f);
		pdfCell.setBorderWidthRight(rightBorder ? 0.8f : 0f);
		pdfCell.setBorderWidthTop(topBorder ? 0.8f : 0f);
		pdfCell.setBorderWidthBottom(bottomBorder ? 0.8f : 0f);

		// ===== Font extraction =====
		org.apache.poi.ss.usermodel.Font excelFont = null;
		try {
			excelFont = workbook.getFontAt(style.getFontIndex());
		} catch (Throwable t) {
			// fallback: create a default workbook font
			try {
				excelFont = workbook.createFont();
			} catch (Throwable ignored) {
			}
		}

		BaseColor fontColor = BaseColor.BLACK;
		int fontSize = 9;
		boolean isBold = false;

		if (excelFont != null) {
			try {
				fontSize = excelFont.getFontHeightInPoints();
				if (fontSize <= 0)
					fontSize = 9;
			} catch (Throwable ignored) {
				fontSize = 9;
			}
			try {
				isBold = excelFont.getBold();
			} catch (Throwable ignored) {
				isBold = false;
			}

			// try to extract XSSF font color (if available)
			try {
				if (excelFont instanceof XSSFFont) {
					XSSFFont xssfFont = (XSSFFont) excelFont;
					XSSFColor xColor = xssfFont.getXSSFColor();
					if (xColor != null && xColor.getRGB() != null) {
						byte[] rgb = xColor.getRGB();
						fontColor = new BaseColor(rgb[0] & 0xFF, rgb[1] & 0xFF, rgb[2] & 0xFF);
					}
				}
			} catch (Throwable ignored) {
			}
		}

		// ===== Create iText font (iText 5-style) =====
		com.itextpdf.text.Font pdfFont;
		try {
			pdfFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, fontSize,
					isBold ? com.itextpdf.text.Font.BOLD : com.itextpdf.text.Font.NORMAL, fontColor);
		} catch (Throwable tt) {
			// fallback font if HELEVETICA constant not available
			pdfFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, fontSize,
					isBold ? com.itextpdf.text.Font.BOLD : com.itextpdf.text.Font.NORMAL, fontColor);
		}

		// ===== Phrase safe set (old iText constructors compatibility) =====
		String text = "";
		Phrase existing = pdfCell.getPhrase();
		if (existing != null) {
			try {
				text = existing.getContent();
			} catch (Throwable ignored) {
				text = "";
			}
		}
		Phrase phrase = new Phrase(text, pdfFont);
		phrase.setLeading(pdfFont.getSize() + 2); // proper line spacing
		pdfCell.setPhrase(phrase);
		pdfCell.setNoWrap(false); // always wrap
		pdfCell.setPadding(4f); // spacing so text doesn’t collide

		// ===== Wrap text =====
		try {
			pdfCell.setNoWrap(false);
		} catch (Throwable t) {
			// ignore
		}
	}

	// ------------------------------
	// Checks if a cell border style is present in the specified side
	// ------------------------------
	private boolean borderPresent(CellStyle style, String which) {
		// which should be "Left", "Right", "Top", or "Bottom"
		try {
			java.lang.reflect.Method m = style.getClass().getMethod("getBorder" + which);
			Object val = m.invoke(style);
			if (val == null)
				return false;
			if (val instanceof Short) {
				short s = ((Short) val).shortValue();
				return s != 0; // old POI: 0 means BORDER_NONE
			} else {
				// new POI: enum BorderStyle, e.g., NONE, THIN, etc.
				String name = val.toString();
				return !"NONE".equalsIgnoreCase(name);
			}
		} catch (NoSuchMethodException nsme) {
			// try alternative new-style method names (getBorderLeftEnum) for some versions
			try {
				java.lang.reflect.Method m2 = style.getClass().getMethod("getBorder" + which + "Enum");
				Object val2 = m2.invoke(style);
				if (val2 == null)
					return false;
				return !"NONE".equalsIgnoreCase(val2.toString());
			} catch (Throwable t) {
				return false;
			}
		} catch (Throwable t) {
			return false;
		}
	}

	// ------------------------------
	// Converts an Excel workbook in byte form into a PDF byte array
	// ------------------------------
	public byte[] convertExcelBytesToPdf(byte[] excelBytes) throws Exception {

		try (InputStream inputStream = new ByteArrayInputStream(excelBytes);
				Workbook workbook = WorkbookFactory.create(inputStream);
				ByteArrayOutputStream pdfOut = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

			// Determine number of columns
			int colCount = getUsedColumnCount(sheet);
			System.out.println("Final usable column count = " + colCount);

			// Get dynamic page size
			Rectangle pageSize = getPageSizeForColumns(colCount);

			Document document = new Document(pageSize, 20, 20, 20, 20);
			PdfWriter.getInstance(document, pdfOut);
			document.open();

			PdfPTable table = new PdfPTable(colCount);
			table.setWidthPercentage(100);

			// Auto column width
			float[] widths = new float[colCount];
			for (int i = 0; i < colCount; i++) {
				int excelWidth = sheet.getColumnWidth(i);

				// Prevent ultra-small or ultra-big widths
				widths[i] = Math.max(50f, Math.min(excelWidth / 30f, 300f));
			}
			table.setWidths(widths);

			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			DataFormatter formatter = new DataFormatter();

			for (Row row : sheet) {
				if (row == null)
					continue;

				for (int i = 0; i < colCount; i++) {
					Cell cell = row.getCell(i);
					String value = formatter.formatCellValue(cell, evaluator);
					PdfPCell pdfCell = new PdfPCell(new Phrase(value));
					applyCellStyleToPdf(cell, pdfCell, workbook);
					pdfCell.setPadding(4);
					table.addCell(pdfCell);

				}
			}

			document.add(table);
			document.close();
			return pdfOut.toByteArray();
		}
	}

	// ------------------------------
	// Renders the detail view/edit page for a given account
	// ------------------------------
	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_SFINP1"); // ✅ match the report name
		System.out.println("Hello");
		if (acctNo != null) {
			M_SFINP1_Detail_Entity la1Entity = findDetailByAcctnumber(acctNo);
			if (la1Entity != null && la1Entity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(la1Entity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("Data", la1Entity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	// ------------------------------
	// Prepares details to update an edited account
	// ------------------------------
	public ModelAndView updateDetailEdit(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_SFINP1"); // ✅ match the report name

		if (acctNo != null) {
			M_SFINP1_Detail_Entity la1Entity = findDetailByAcctnumber(acctNo);
			if (la1Entity != null && la1Entity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(la1Entity.getReportDate());
				mv.addObject("asondate", formattedDate);
				System.out.println(formattedDate);
			}
			mv.addObject("Data", la1Entity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	// ------------------------------
	// Updates account detail fields in the database and runs the summary procedure
	// ------------------------------
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String provisionStr = request.getParameter("acctBalanceInPula");
			String average = request.getParameter("average");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			M_SFINP1_Detail_Entity existing = findDetailByAcctnumber(acctNo);
			if (existing == null) {
				logger.warn("No record found for ACCT_NO: {}", acctNo);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}

			boolean isChanged = false;

			if (acctName != null && !acctName.isEmpty()) {
				if (existing.getAcctName() == null || !existing.getAcctName().equals(acctName)) {
					existing.setAcctName(acctName);
					isChanged = true;
					logger.info("Account name updated to {}", acctName);
				}
			}

			if (provisionStr != null && !provisionStr.isEmpty()) {
				BigDecimal newProvision = new BigDecimal(provisionStr);
				if (existing.getAcctBalanceInPula() == null
						|| existing.getAcctBalanceInPula().compareTo(newProvision) != 0) {
					existing.setAcctBalanceInPula(newProvision);
					isChanged = true;
					logger.info("Balance updated to {}", newProvision);
				}
			}
			if (average != null && !average.isEmpty()) {
				BigDecimal newaverage = new BigDecimal(average);
				if (existing.getAverage() == null || existing.getAverage().compareTo(newaverage) != 0) {
					existing.setAverage(newaverage);
					isChanged = true;
					logger.info("Balance updated to {}", newaverage);
				}
			}

			if (isChanged) {
				saveDetail(existing);
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_M_SFINP1_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_M_SFINP1_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating M_CA2 record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	// ------------------------------
	// Generates the summary Excel
	// ------------------------------
	public byte[] getM_SFINP1Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		System.out.println("======= VIEW SCREEN =======");
		System.out.println("TYPE      : " + type);
		System.out.println("FORMAT      : " + format);
		System.out.println("DTLTYPE   : " + dtltype);
		System.out.println("DATE      : " + dateformat.parse(todate));
		System.out.println("VERSION   : " + version);
		System.out.println("==========================");

		DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
		DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);

		LocalDate date = LocalDate.parse(todate, inputFormat);
		String formattedDate = date.format(outputFormat);
		// ARCHIVAL/RESUB check — a resub is just an archival version, so both are exported the same way
		if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null) {
			try {
				// Redirecting to Archival
				return getExcelM_SFINP1ARCHIVAL(filename, reportId, fromdate, formattedDate, // todate
						currency, dtltype, type, format, // ✅ ADD THIS
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_SFINP1EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				List<M_SFINP1_Summary_Entity> dataList = jdbcTemplate.query(
						"SELECT * FROM BRRS_M_SFINP1_SUMMARYTABLE WHERE REPORT_DATE = ?",
						new Object[] { dateformat.parse(todate) }, new M_SFINP1SummaryRowMapper());
				List<M_SFINP1_Summary_Manual_Entity> dataList1 = jdbcTemplate.query(
						"SELECT * FROM BRRS_M_SFINP1_SUMMARYTABLE_MANUAL WHERE TRUNC(REPORT_DATE) = TO_DATE(?, 'DD-MON-YYYY')",
						new Object[] { formattedDate }, new M_SFINP1SummaryManualRowMapper());

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_SFINP1 report. Returning empty result.");
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
							M_SFINP1_Summary_Entity record = dataList.get(i);
							M_SFINP1_Summary_Manual_Entity record1 = dataList1.get(i);

							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							// NORMAL

							Cell cellC, cellD;
							CellStyle originalStyle;
							// ===== Row 6 / Col b (Date) =====

							cellC = row.getCell(1);

							if (cellC == null)
								cellC = row.createCell(1);

							originalStyle = cellC.getCellStyle();

							if (record.getReport_date() != null) {
								cellC.setCellValue(record.getReport_date()); // java.util.Date
								cellC.setCellStyle(dateStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(originalStyle);
							}

							// ===== Row 10 / Col C =====
							row = sheet.getRow(9);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR10_month_end() != null)
								cellC.setCellValue(record.getR10_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 10 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR10_average() != null)
								cellD.setCellValue(record.getR10_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 11 / Col C =====
							row = sheet.getRow(10);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR11_month_end() != null)
								cellC.setCellValue(record.getR11_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 11 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR11_average() != null)
								cellD.setCellValue(record.getR11_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 13 / Col C =====
							row = sheet.getRow(12);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR13_month_end() != null)
								cellC.setCellValue(record.getR13_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 13 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR13_average() != null)
								cellD.setCellValue(record.getR13_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 14 / Col C =====
							row = sheet.getRow(13);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR14_MONTH_END() != null)
								cellC.setCellValue(record1.getR14_MONTH_END().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 14 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR14_average() != null)
								cellD.setCellValue(record.getR14_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 15 / Col C =====
							row = sheet.getRow(14);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR15_month_end() != null)
								cellC.setCellValue(record.getR15_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 15 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR15_average() != null)
								cellD.setCellValue(record.getR15_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 16 / Col C =====
							row = sheet.getRow(15);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR16_month_end() != null)
								cellC.setCellValue(record.getR16_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 16 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR16_average() != null)
								cellD.setCellValue(record.getR16_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 17 / Col C =====
							row = sheet.getRow(16);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR17_month_end() != null)
								cellC.setCellValue(record.getR17_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 17 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR17_average() != null)
								cellD.setCellValue(record.getR17_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 18 / Col C =====
							row = sheet.getRow(17);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR18_month_end() != null)
								cellC.setCellValue(record.getR18_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 18 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR18_average() != null)
								cellD.setCellValue(record.getR18_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 19 / Col C =====
							row = sheet.getRow(18);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR19_month_end() != null)
								cellC.setCellValue(record.getR19_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 19 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR19_average() != null)
								cellD.setCellValue(record.getR19_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 22 / Col C =====
							row = sheet.getRow(21);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR22_month_end() != null)
								cellC.setCellValue(record.getR22_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 22 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR22_average() != null)
								cellD.setCellValue(record.getR22_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 23 / Col C =====
							row = sheet.getRow(22);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR23_month_end() != null)
								cellC.setCellValue(record.getR23_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 23 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR23_average() != null)
								cellD.setCellValue(record.getR23_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 25 / Col C =====
							row = sheet.getRow(24);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR25_month_end() != null)
								cellC.setCellValue(record.getR25_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 25 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR25_average() != null)
								cellD.setCellValue(record.getR25_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 26 / Col C =====
							row = sheet.getRow(25);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR26_month_end() != null)
								cellC.setCellValue(record.getR26_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 26 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR26_average() != null)
								cellD.setCellValue(record.getR26_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 28 / Col C =====
							row = sheet.getRow(27);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR28_month_end() != null)
								cellC.setCellValue(record.getR28_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 28 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR28_average() != null)
								cellD.setCellValue(record.getR28_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 29 / Col C =====
							row = sheet.getRow(28);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR29_month_end() != null)
								cellC.setCellValue(record.getR29_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 29 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR29_average() != null)
								cellD.setCellValue(record.getR29_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 30 / Col C =====
							row = sheet.getRow(29);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR30_month_end() != null)
								cellC.setCellValue(record.getR30_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 30 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR30_average() != null)
								cellD.setCellValue(record.getR30_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 32 / Col C =====
							row = sheet.getRow(31);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR32_month_end() != null)
								cellC.setCellValue(record.getR32_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 32 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR32_average() != null)
								cellD.setCellValue(record.getR32_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 33 / Col C =====
							row = sheet.getRow(32);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR33_month_end() != null)
								cellC.setCellValue(record.getR33_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 33 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR33_average() != null)
								cellD.setCellValue(record.getR33_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 34 / Col C =====
							row = sheet.getRow(33);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR34_MONTH_END() != null)
								cellC.setCellValue(record1.getR34_MONTH_END().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 34 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR34_average() != null)
								cellD.setCellValue(record.getR34_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 36 / Col C =====
							row = sheet.getRow(35);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR36_month_end() != null)
								cellC.setCellValue(record.getR36_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 36 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR36_average() != null)
								cellD.setCellValue(record.getR36_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 37 / Col C =====
							row = sheet.getRow(36);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR37_MONTH_END() != null)
								cellC.setCellValue(record1.getR37_MONTH_END().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 37 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR37_average() != null)
								cellD.setCellValue(record.getR37_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 39 / Col C =====
							row = sheet.getRow(38);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR39_month_end() != null)
								cellC.setCellValue(record.getR39_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 39 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR39_average() != null)
								cellD.setCellValue(record.getR39_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 41 / Col C =====
							row = sheet.getRow(40);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR41_month_end() != null)
								cellC.setCellValue(record.getR41_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 41 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR41_average() != null)
								cellD.setCellValue(record.getR41_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 42 / Col C =====
							row = sheet.getRow(41);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR42_MONTH_END() != null)
								cellC.setCellValue(record1.getR42_MONTH_END().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 42 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR42_average() != null)
								cellD.setCellValue(record.getR42_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 43 / Col C =====
							row = sheet.getRow(42);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR43_MONTH_END() != null)
								cellC.setCellValue(record1.getR43_MONTH_END().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 43 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR43_average() != null)
								cellD.setCellValue(record.getR43_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 45 / Col C =====
							row = sheet.getRow(44);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR45_month_end() != null)
								cellC.setCellValue(record.getR45_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 45 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR45_average() != null)
								cellD.setCellValue(record.getR45_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 46 / Col C =====
							row = sheet.getRow(45);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR46_month_end() != null)
								cellC.setCellValue(record.getR46_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 46 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR46_average() != null)
								cellD.setCellValue(record.getR46_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 47 / Col C =====
							row = sheet.getRow(46);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR47_month_end() != null)
								cellC.setCellValue(record.getR47_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 47 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR47_average() != null)
								cellD.setCellValue(record.getR47_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 48 / Col C =====
							row = sheet.getRow(47);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR48_month_end() != null)
								cellC.setCellValue(record.getR48_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 48 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR48_average() != null)
								cellD.setCellValue(record.getR48_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 50 / Col C =====
							row = sheet.getRow(49);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR50_month_end() != null)
								cellC.setCellValue(record.getR50_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 50 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR50_average() != null)
								cellD.setCellValue(record.getR50_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 51 / Col C =====
							row = sheet.getRow(50);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR51_MONTH_END() != null)
								cellC.setCellValue(record1.getR51_MONTH_END().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 51 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR51_average() != null)
								cellD.setCellValue(record.getR51_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 52 / Col C =====
							row = sheet.getRow(51);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR52_MONTH_END() != null)
								cellC.setCellValue(record1.getR52_MONTH_END().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 52 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR52_average() != null)
								cellD.setCellValue(record.getR52_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 53 / Col C =====
							row = sheet.getRow(52);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR53_month_end() != null)
								cellC.setCellValue(record.getR53_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 53 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR53_average() != null)
								cellD.setCellValue(record.getR53_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 54 / Col C =====
							row = sheet.getRow(53);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR54_month_end() != null)
								cellC.setCellValue(record.getR54_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 54 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR54_average() != null)
								cellD.setCellValue(record.getR54_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 55 / Col C =====
							row = sheet.getRow(54);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR55_month_end() != null)
								cellC.setCellValue(record.getR55_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 55 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR55_average() != null)
								cellD.setCellValue(record.getR55_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 57 / Col C =====
							row = sheet.getRow(56);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR57_MONTH_END() != null)
								cellC.setCellValue(record1.getR57_MONTH_END().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 57 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR57_average() != null)
								cellD.setCellValue(record.getR57_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 58 / Col C =====
							row = sheet.getRow(57);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR58_month_end() != null)
								cellC.setCellValue(record.getR58_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 58 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR58_average() != null)
								cellD.setCellValue(record.getR58_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 59 / Col C =====
							row = sheet.getRow(58);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR59_MONTH_END() != null)
								cellC.setCellValue(record1.getR59_MONTH_END().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 59 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR59_average() != null)
								cellD.setCellValue(record.getR59_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							// ===== Row 60 / Col C =====
							row = sheet.getRow(59);
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record.getR60_month_end() != null)
								cellC.setCellValue(record.getR60_month_end().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
							// ===== Row 60 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record.getR60_average() != null)
								cellD.setCellValue(record.getR60_average().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SFINP1 SUMMARY", null,
								"BRRS_M_SFINP1_SUMMARYTABLE");
					}
					return out.toByteArray();
				}
			}
		}
	}

	// ------------------------------
	// Generates the Email Excel 
	// ------------------------------
	public byte[] BRRS_M_SFINP1EmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");
		DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
		DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);

		LocalDate date = LocalDate.parse(todate, inputFormat);
		String formattedDate = date.format(outputFormat);
		if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_SFINP1EmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			// Fetch data
			List<M_SFINP1_Summary_Entity> dataList = jdbcTemplate.query(
					"SELECT * FROM BRRS_M_SFINP1_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TO_DATE(?, 'DD-MON-YYYY')",
					new Object[] { formattedDate }, new M_SFINP1SummaryRowMapper());
			List<M_SFINP1_Summary_Manual_Entity> dataList1 = jdbcTemplate.query(
					"SELECT * FROM BRRS_M_SFINP1_SUMMARYTABLE_MANUAL WHERE TRUNC(REPORT_DATE) = TO_DATE(?, 'DD-MON-YYYY')",
					new Object[] { formattedDate }, new M_SFINP1SummaryManualRowMapper());

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_SFINP1 report. Returning empty result.");
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
						M_SFINP1_Summary_Entity record = dataList.get(i);
						M_SFINP1_Summary_Manual_Entity record1 = dataList1.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						// EMAIL

						Cell cellC, cellD;
						CellStyle originalStyle;

						cellC = row.getCell(1);

						if (cellC == null)
							cellC = row.createCell(1);

						originalStyle = cellC.getCellStyle();

						if (record.getReport_date() != null) {
							cellC.setCellValue(record.getReport_date()); // java.util.Date
							cellC.setCellStyle(dateStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);
						}

						// ===== Row 10 / Col C =====
						row = sheet.getRow(9);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR10_month_end() != null)
							cellC.setCellValue(record.getR10_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 10 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR10_average() != null)
							cellD.setCellValue(record.getR10_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 11 / Col C =====
						row = sheet.getRow(10);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR11_month_end() != null)
							cellC.setCellValue(record.getR11_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 11 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR11_average() != null)
							cellD.setCellValue(record.getR11_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 13 / Col C =====
						row = sheet.getRow(12);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR13_month_end() != null)
							cellC.setCellValue(record.getR13_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 13 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR13_average() != null)
							cellD.setCellValue(record.getR13_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 14 / Col C =====
						row = sheet.getRow(13);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record1.getR14_MONTH_END() != null)
							cellC.setCellValue(record1.getR14_MONTH_END().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 14 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR14_average() != null)
							cellD.setCellValue(record.getR14_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 15 / Col C =====
						row = sheet.getRow(14);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR15_month_end() != null)
							cellC.setCellValue(record.getR15_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 15 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR15_average() != null)
							cellD.setCellValue(record.getR15_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 16 / Col C =====
						row = sheet.getRow(15);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR16_month_end() != null)
							cellC.setCellValue(record.getR16_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 16 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR16_average() != null)
							cellD.setCellValue(record.getR16_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 17 / Col C =====
						row = sheet.getRow(16);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR17_month_end() != null)
							cellC.setCellValue(record.getR17_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 17 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR17_average() != null)
							cellD.setCellValue(record.getR17_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 18 / Col C =====
						row = sheet.getRow(17);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR18_month_end() != null)
							cellC.setCellValue(record.getR18_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 18 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR18_average() != null)
							cellD.setCellValue(record.getR18_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 19 / Col C =====
						row = sheet.getRow(18);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR19_month_end() != null)
							cellC.setCellValue(record.getR19_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 19 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR19_average() != null)
							cellD.setCellValue(record.getR19_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 22 / Col C =====
						row = sheet.getRow(21);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR22_month_end() != null)
							cellC.setCellValue(record.getR22_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 22 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR22_average() != null)
							cellD.setCellValue(record.getR22_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 23 / Col C =====
						row = sheet.getRow(22);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR23_month_end() != null)
							cellC.setCellValue(record.getR23_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 23 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR23_average() != null)
							cellD.setCellValue(record.getR23_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 25 / Col C =====
						row = sheet.getRow(24);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR25_month_end() != null)
							cellC.setCellValue(record.getR25_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 25 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR25_average() != null)
							cellD.setCellValue(record.getR25_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 26 / Col C =====
						row = sheet.getRow(25);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR26_month_end() != null)
							cellC.setCellValue(record.getR26_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 26 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR26_average() != null)
							cellD.setCellValue(record.getR26_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 28 / Col C =====
						row = sheet.getRow(27);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR28_month_end() != null)
							cellC.setCellValue(record.getR28_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 28 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR28_average() != null)
							cellD.setCellValue(record.getR28_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 29 / Col C =====
						row = sheet.getRow(28);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR29_month_end() != null)
							cellC.setCellValue(record.getR29_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 29 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR29_average() != null)
							cellD.setCellValue(record.getR29_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 30 / Col C =====
						row = sheet.getRow(29);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR30_month_end() != null)
							cellC.setCellValue(record.getR30_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 30 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR30_average() != null)
							cellD.setCellValue(record.getR30_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 32 / Col C =====
						row = sheet.getRow(31);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR32_month_end() != null)
							cellC.setCellValue(record.getR32_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 32 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR32_average() != null)
							cellD.setCellValue(record.getR32_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 33 / Col C =====
						row = sheet.getRow(32);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR33_month_end() != null)
							cellC.setCellValue(record.getR33_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 33 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR33_average() != null)
							cellD.setCellValue(record.getR33_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 34 / Col C =====
						row = sheet.getRow(33);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record1.getR34_MONTH_END() != null)
							cellC.setCellValue(record1.getR34_MONTH_END().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 34 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR34_average() != null)
							cellD.setCellValue(record.getR34_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 36 / Col C =====
						row = sheet.getRow(35);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR36_month_end() != null)
							cellC.setCellValue(record.getR36_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 36 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR36_average() != null)
							cellD.setCellValue(record.getR36_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 37 / Col C =====
						row = sheet.getRow(36);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record1.getR37_MONTH_END() != null)
							cellC.setCellValue(record1.getR37_MONTH_END().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 37 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR37_average() != null)
							cellD.setCellValue(record.getR37_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 39 / Col C =====
						row = sheet.getRow(38);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR39_month_end() != null)
							cellC.setCellValue(record.getR39_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 39 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR39_average() != null)
							cellD.setCellValue(record.getR39_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 41 / Col C =====
						row = sheet.getRow(40);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR41_month_end() != null)
							cellC.setCellValue(record.getR41_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 41 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR41_average() != null)
							cellD.setCellValue(record.getR41_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 42 / Col C =====
						row = sheet.getRow(41);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record1.getR42_MONTH_END() != null)
							cellC.setCellValue(record1.getR42_MONTH_END().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 42 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR42_average() != null)
							cellD.setCellValue(record.getR42_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 43 / Col C =====
						row = sheet.getRow(42);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record1.getR43_MONTH_END() != null)
							cellC.setCellValue(record1.getR43_MONTH_END().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 43 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR43_average() != null)
							cellD.setCellValue(record.getR43_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 45 / Col C =====
						row = sheet.getRow(44);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR45_month_end() != null)
							cellC.setCellValue(record.getR45_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 45 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR45_average() != null)
							cellD.setCellValue(record.getR45_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 46 / Col C =====
						row = sheet.getRow(45);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR46_month_end() != null)
							cellC.setCellValue(record.getR46_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 46 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR46_average() != null)
							cellD.setCellValue(record.getR46_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 47 / Col C =====
						row = sheet.getRow(46);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR47_month_end() != null)
							cellC.setCellValue(record.getR47_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 47 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR47_average() != null)
							cellD.setCellValue(record.getR47_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 48 / Col C =====
						row = sheet.getRow(47);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR48_month_end() != null)
							cellC.setCellValue(record.getR48_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 48 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR48_average() != null)
							cellD.setCellValue(record.getR48_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 50 / Col C =====
						row = sheet.getRow(49);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR50_month_end() != null)
							cellC.setCellValue(record.getR50_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 50 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR50_average() != null)
							cellD.setCellValue(record.getR50_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 51 / Col C =====
						row = sheet.getRow(50);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record1.getR51_MONTH_END() != null)
							cellC.setCellValue(record1.getR51_MONTH_END().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 51 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR51_average() != null)
							cellD.setCellValue(record.getR51_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 52 / Col C =====
						row = sheet.getRow(51);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record1.getR52_MONTH_END() != null)
							cellC.setCellValue(record1.getR52_MONTH_END().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 52 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR52_average() != null)
							cellD.setCellValue(record.getR52_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 53 / Col C =====
						row = sheet.getRow(52);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR53_month_end() != null)
							cellC.setCellValue(record.getR53_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 53 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR53_average() != null)
							cellD.setCellValue(record.getR53_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 54 / Col C =====
						row = sheet.getRow(53);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR54_month_end() != null)
							cellC.setCellValue(record.getR54_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 54 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR54_average() != null)
							cellD.setCellValue(record.getR54_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 55 / Col C =====
						row = sheet.getRow(54);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR55_month_end() != null)
							cellC.setCellValue(record.getR55_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 55 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR55_average() != null)
							cellD.setCellValue(record.getR55_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 57 / Col C =====
						row = sheet.getRow(56);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record1.getR57_MONTH_END() != null)
							cellC.setCellValue(record1.getR57_MONTH_END().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 57 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR57_average() != null)
							cellD.setCellValue(record.getR57_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 58 / Col C =====
						row = sheet.getRow(57);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR58_month_end() != null)
							cellC.setCellValue(record.getR58_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 58 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR58_average() != null)
							cellD.setCellValue(record.getR58_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 59 / Col C =====
						row = sheet.getRow(58);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record1.getR59_MONTH_END() != null)
							cellC.setCellValue(record1.getR59_MONTH_END().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 59 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR59_average() != null)
							cellD.setCellValue(record.getR59_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
						// ===== Row 60 / Col C =====
						row = sheet.getRow(59);
						cellC = row.getCell(2);
						if (cellC == null)
							cellC = row.createCell(2);
						originalStyle = cellC.getCellStyle();
						if (record.getR60_month_end() != null)
							cellC.setCellValue(record.getR60_month_end().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
						// ===== Row 60 / Col D =====
						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						if (record.getR60_average() != null)
							cellD.setCellValue(record.getR60_average().doubleValue());
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);
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
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SFINP1 EMAIL SUMMARY", null,
							"BRRS_M_SFINP1_SUMMARYTABLE");
				}
				return out.toByteArray();
			}
		}
	}

	// ------------------------------
	// Generates the archival summary Excel
	// ------------------------------
	public byte[] getExcelM_SFINP1ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");
		Date parsedDate = dateformat.parse(todate);
		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_SFINP1EmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_SFINP1_Archival_Summary_Entity> dataList = jdbcTemplate.query(
				"select * from BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ? and REPORT_VERSION = ?",
				new Object[] { dateformat.parse(todate), version }, new M_SFINP1ArchivalSummaryRowMapper());
		List<M_SFINP1_Archival_Summary_Manual_Entity> dataList1 = jdbcTemplate.query(
				"SELECT * FROM BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY_MANUAL WHERE REPORT_DATE = ?",
				new Object[] { parsedDate }, new M_SFINP1ArchivalSummaryManualRowMapper());

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SFINP1 report. Returning empty result.");
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
					M_SFINP1_Archival_Summary_Entity record = dataList.get(i);
					M_SFINP1_Archival_Summary_Manual_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// NORMAL

					Cell cellC, cellD;
					CellStyle originalStyle;

					cellC = row.getCell(1);

					if (cellC == null)
						cellC = row.createCell(1);

					originalStyle = cellC.getCellStyle();

					if (record.getReportDate() != null) {
						cellC.setCellValue(record.getReportDate()); // java.util.Date
						cellC.setCellStyle(dateStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
					}

					// ===== Row 10 / Col C =====
					row = sheet.getRow(9);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR10_month_end() != null)
						cellC.setCellValue(record.getR10_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 10 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR10_average() != null)
						cellD.setCellValue(record.getR10_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 11 / Col C =====
					row = sheet.getRow(10);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR11_month_end() != null)
						cellC.setCellValue(record.getR11_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 11 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR11_average() != null)
						cellD.setCellValue(record.getR11_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 13 / Col C =====
					row = sheet.getRow(12);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR13_month_end() != null)
						cellC.setCellValue(record.getR13_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 13 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR13_average() != null)
						cellD.setCellValue(record.getR13_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 14 / Col C =====
					row = sheet.getRow(13);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR14_MONTH_END() != null)
						cellC.setCellValue(record1.getR14_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 14 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR14_average() != null)
						cellD.setCellValue(record.getR14_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 15 / Col C =====
					row = sheet.getRow(14);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR15_month_end() != null)
						cellC.setCellValue(record.getR15_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 15 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR15_average() != null)
						cellD.setCellValue(record.getR15_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 16 / Col C =====
					row = sheet.getRow(15);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR16_month_end() != null)
						cellC.setCellValue(record.getR16_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 16 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR16_average() != null)
						cellD.setCellValue(record.getR16_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 17 / Col C =====
					row = sheet.getRow(16);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR17_month_end() != null)
						cellC.setCellValue(record.getR17_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 17 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR17_average() != null)
						cellD.setCellValue(record.getR17_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 18 / Col C =====
					row = sheet.getRow(17);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR18_month_end() != null)
						cellC.setCellValue(record.getR18_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 18 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR18_average() != null)
						cellD.setCellValue(record.getR18_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 19 / Col C =====
					row = sheet.getRow(18);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR19_month_end() != null)
						cellC.setCellValue(record.getR19_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 19 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR19_average() != null)
						cellD.setCellValue(record.getR19_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 22 / Col C =====
					row = sheet.getRow(21);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR22_month_end() != null)
						cellC.setCellValue(record.getR22_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 22 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR22_average() != null)
						cellD.setCellValue(record.getR22_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 23 / Col C =====
					row = sheet.getRow(22);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR23_month_end() != null)
						cellC.setCellValue(record.getR23_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 23 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR23_average() != null)
						cellD.setCellValue(record.getR23_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 25 / Col C =====
					row = sheet.getRow(24);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR25_month_end() != null)
						cellC.setCellValue(record.getR25_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 25 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR25_average() != null)
						cellD.setCellValue(record.getR25_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 26 / Col C =====
					row = sheet.getRow(25);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR26_month_end() != null)
						cellC.setCellValue(record.getR26_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 26 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR26_average() != null)
						cellD.setCellValue(record.getR26_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 28 / Col C =====
					row = sheet.getRow(27);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR28_month_end() != null)
						cellC.setCellValue(record.getR28_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 28 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR28_average() != null)
						cellD.setCellValue(record.getR28_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 29 / Col C =====
					row = sheet.getRow(28);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR29_month_end() != null)
						cellC.setCellValue(record.getR29_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 29 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR29_average() != null)
						cellD.setCellValue(record.getR29_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 30 / Col C =====
					row = sheet.getRow(29);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR30_month_end() != null)
						cellC.setCellValue(record.getR30_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 30 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR30_average() != null)
						cellD.setCellValue(record.getR30_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 32 / Col C =====
					row = sheet.getRow(31);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR32_month_end() != null)
						cellC.setCellValue(record.getR32_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 32 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR32_average() != null)
						cellD.setCellValue(record.getR32_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 33 / Col C =====
					row = sheet.getRow(32);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR33_month_end() != null)
						cellC.setCellValue(record.getR33_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 33 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR33_average() != null)
						cellD.setCellValue(record.getR33_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 34 / Col C =====
					row = sheet.getRow(33);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR34_MONTH_END() != null)
						cellC.setCellValue(record1.getR34_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 34 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR34_average() != null)
						cellD.setCellValue(record.getR34_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 36 / Col C =====
					row = sheet.getRow(35);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR36_month_end() != null)
						cellC.setCellValue(record.getR36_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 36 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR36_average() != null)
						cellD.setCellValue(record.getR36_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 37 / Col C =====
					row = sheet.getRow(36);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR37_MONTH_END() != null)
						cellC.setCellValue(record1.getR37_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 37 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR37_average() != null)
						cellD.setCellValue(record.getR37_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 39 / Col C =====
					row = sheet.getRow(38);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR39_month_end() != null)
						cellC.setCellValue(record.getR39_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 39 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR39_average() != null)
						cellD.setCellValue(record.getR39_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 41 / Col C =====
					row = sheet.getRow(40);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR41_month_end() != null)
						cellC.setCellValue(record.getR41_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 41 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR41_average() != null)
						cellD.setCellValue(record.getR41_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 42 / Col C =====
					row = sheet.getRow(41);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR42_month_end() != null)
						cellC.setCellValue(record.getR42_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 42 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR42_average() != null)
						cellD.setCellValue(record.getR42_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 43 / Col C =====
					row = sheet.getRow(42);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR43_MONTH_END() != null)
						cellC.setCellValue(record1.getR43_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 43 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR43_average() != null)
						cellD.setCellValue(record.getR43_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 45 / Col C =====
					row = sheet.getRow(44);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR45_month_end() != null)
						cellC.setCellValue(record.getR45_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 45 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR45_average() != null)
						cellD.setCellValue(record.getR45_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 46 / Col C =====
					row = sheet.getRow(45);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR46_month_end() != null)
						cellC.setCellValue(record.getR46_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 46 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR46_average() != null)
						cellD.setCellValue(record.getR46_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 47 / Col C =====
					row = sheet.getRow(46);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR47_month_end() != null)
						cellC.setCellValue(record.getR47_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 47 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR47_average() != null)
						cellD.setCellValue(record.getR47_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 48 / Col C =====
					row = sheet.getRow(47);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR48_month_end() != null)
						cellC.setCellValue(record.getR48_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 48 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR48_average() != null)
						cellD.setCellValue(record.getR48_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 50 / Col C =====
					row = sheet.getRow(49);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR50_month_end() != null)
						cellC.setCellValue(record.getR50_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 50 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR50_average() != null)
						cellD.setCellValue(record.getR50_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 51 / Col C =====
					row = sheet.getRow(50);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR51_MONTH_END() != null)
						cellC.setCellValue(record1.getR51_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 51 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR51_average() != null)
						cellD.setCellValue(record.getR51_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 52 / Col C =====
					row = sheet.getRow(51);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR52_MONTH_END() != null)
						cellC.setCellValue(record1.getR52_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 52 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR52_average() != null)
						cellD.setCellValue(record.getR52_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 53 / Col C =====
					row = sheet.getRow(52);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR53_month_end() != null)
						cellC.setCellValue(record.getR53_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 53 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR53_average() != null)
						cellD.setCellValue(record.getR53_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 54 / Col C =====
					row = sheet.getRow(53);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR54_month_end() != null)
						cellC.setCellValue(record.getR54_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 54 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR54_average() != null)
						cellD.setCellValue(record.getR54_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 55 / Col C =====
					row = sheet.getRow(54);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR55_month_end() != null)
						cellC.setCellValue(record.getR55_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 55 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR55_average() != null)
						cellD.setCellValue(record.getR55_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 57 / Col C =====
					row = sheet.getRow(56);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR57_MONTH_END() != null)
						cellC.setCellValue(record1.getR57_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 57 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR57_average() != null)
						cellD.setCellValue(record.getR57_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 58 / Col C =====
					row = sheet.getRow(57);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR58_month_end() != null)
						cellC.setCellValue(record.getR58_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 58 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR58_average() != null)
						cellD.setCellValue(record.getR58_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 59 / Col C =====
					row = sheet.getRow(58);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR59_MONTH_END() != null)
						cellC.setCellValue(record1.getR59_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 59 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR59_average() != null)
						cellD.setCellValue(record.getR59_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 60 / Col C =====
					row = sheet.getRow(59);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR60_month_end() != null)
						cellC.setCellValue(record.getR60_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 60 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR60_average() != null)
						cellD.setCellValue(record.getR60_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SFINP1 ARCHIVAL SUMMARY", null,
						"BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}

	}

	// ------------------------------
	// Generates the archival Email Excel 
	// ------------------------------
	public byte[] BRRS_M_SFINP1EmailArchivalExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
		DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);

		LocalDate date = LocalDate.parse(todate, inputFormat);
		String formattedDate = date.format(outputFormat);
		Date parsedDate = dateformat.parse(todate);
		if (type.equals("ARCHIVAL") & version != null) {
		}
		List<M_SFINP1_Archival_Summary_Entity> dataList = jdbcTemplate.query(
				"select * from BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ? and REPORT_VERSION = ?",
				new Object[] { dateformat.parse(todate), version }, new M_SFINP1ArchivalSummaryRowMapper());
		List<M_SFINP1_Archival_Summary_Manual_Entity> dataList1 = jdbcTemplate.query(
				"SELECT * FROM BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY_MANUAL WHERE REPORT_DATE = ?",
				new Object[] { parsedDate }, new M_SFINP1ArchivalSummaryManualRowMapper());

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_SFINP1 report. Returning empty result.");
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
					M_SFINP1_Archival_Summary_Entity record = dataList.get(i);
					M_SFINP1_Archival_Summary_Manual_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// EMAIL

					Cell cellC, cellD;
					CellStyle originalStyle;

					cellC = row.getCell(1);

					if (cellC == null)
						cellC = row.createCell(1);

					originalStyle = cellC.getCellStyle();

					if (record.getReportDate() != null) {
						cellC.setCellValue(record.getReportDate()); // java.util.Date
						cellC.setCellStyle(dateStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);
					}

					// ===== Row 10 / Col C =====
					row = sheet.getRow(9);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR10_month_end() != null)
						cellC.setCellValue(record.getR10_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 10 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR10_average() != null)
						cellD.setCellValue(record.getR10_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 11 / Col C =====
					row = sheet.getRow(10);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR11_month_end() != null)
						cellC.setCellValue(record.getR11_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 11 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR11_average() != null)
						cellD.setCellValue(record.getR11_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 13 / Col C =====
					row = sheet.getRow(12);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR13_month_end() != null)
						cellC.setCellValue(record.getR13_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 13 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR13_average() != null)
						cellD.setCellValue(record.getR13_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 14 / Col C =====
					row = sheet.getRow(13);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR14_MONTH_END() != null)
						cellC.setCellValue(record1.getR14_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 14 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR14_average() != null)
						cellD.setCellValue(record.getR14_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 15 / Col C =====
					row = sheet.getRow(14);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR15_month_end() != null)
						cellC.setCellValue(record.getR15_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 15 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR15_average() != null)
						cellD.setCellValue(record.getR15_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 16 / Col C =====
					row = sheet.getRow(15);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR16_month_end() != null)
						cellC.setCellValue(record.getR16_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 16 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR16_average() != null)
						cellD.setCellValue(record.getR16_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 17 / Col C =====
					row = sheet.getRow(16);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR17_month_end() != null)
						cellC.setCellValue(record.getR17_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 17 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR17_average() != null)
						cellD.setCellValue(record.getR17_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 18 / Col C =====
					row = sheet.getRow(17);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR18_month_end() != null)
						cellC.setCellValue(record.getR18_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 18 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR18_average() != null)
						cellD.setCellValue(record.getR18_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 19 / Col C =====
					row = sheet.getRow(18);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR19_month_end() != null)
						cellC.setCellValue(record.getR19_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 19 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR19_average() != null)
						cellD.setCellValue(record.getR19_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 22 / Col C =====
					row = sheet.getRow(21);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR22_month_end() != null)
						cellC.setCellValue(record.getR22_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 22 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR22_average() != null)
						cellD.setCellValue(record.getR22_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 23 / Col C =====
					row = sheet.getRow(22);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR23_month_end() != null)
						cellC.setCellValue(record.getR23_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 23 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR23_average() != null)
						cellD.setCellValue(record.getR23_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 25 / Col C =====
					row = sheet.getRow(24);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR25_month_end() != null)
						cellC.setCellValue(record.getR25_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 25 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR25_average() != null)
						cellD.setCellValue(record.getR25_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 26 / Col C =====
					row = sheet.getRow(25);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR26_month_end() != null)
						cellC.setCellValue(record.getR26_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 26 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR26_average() != null)
						cellD.setCellValue(record.getR26_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 28 / Col C =====
					row = sheet.getRow(27);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR28_month_end() != null)
						cellC.setCellValue(record.getR28_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 28 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR28_average() != null)
						cellD.setCellValue(record.getR28_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 29 / Col C =====
					row = sheet.getRow(28);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR29_month_end() != null)
						cellC.setCellValue(record.getR29_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 29 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR29_average() != null)
						cellD.setCellValue(record.getR29_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 30 / Col C =====
					row = sheet.getRow(29);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR30_month_end() != null)
						cellC.setCellValue(record.getR30_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 30 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR30_average() != null)
						cellD.setCellValue(record.getR30_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 32 / Col C =====
					row = sheet.getRow(31);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR32_month_end() != null)
						cellC.setCellValue(record.getR32_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 32 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR32_average() != null)
						cellD.setCellValue(record.getR32_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 33 / Col C =====
					row = sheet.getRow(32);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR33_month_end() != null)
						cellC.setCellValue(record.getR33_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 33 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR33_average() != null)
						cellD.setCellValue(record.getR33_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 34 / Col C =====
					row = sheet.getRow(33);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR34_MONTH_END() != null)
						cellC.setCellValue(record1.getR34_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 34 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR34_average() != null)
						cellD.setCellValue(record.getR34_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 36 / Col C =====
					row = sheet.getRow(35);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR36_month_end() != null)
						cellC.setCellValue(record.getR36_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 36 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR36_average() != null)
						cellD.setCellValue(record.getR36_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 37 / Col C =====
					row = sheet.getRow(36);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR37_MONTH_END() != null)
						cellC.setCellValue(record1.getR37_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 37 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR37_average() != null)
						cellD.setCellValue(record.getR37_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 39 / Col C =====
					row = sheet.getRow(38);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR39_month_end() != null)
						cellC.setCellValue(record.getR39_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 39 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR39_average() != null)
						cellD.setCellValue(record.getR39_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 41 / Col C =====
					row = sheet.getRow(40);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR41_month_end() != null)
						cellC.setCellValue(record.getR41_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 41 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR41_average() != null)
						cellD.setCellValue(record.getR41_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 42 / Col C =====
					row = sheet.getRow(41);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR42_month_end() != null)
						cellC.setCellValue(record.getR42_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 42 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR42_average() != null)
						cellD.setCellValue(record.getR42_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 43 / Col C =====
					row = sheet.getRow(42);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR43_MONTH_END() != null)
						cellC.setCellValue(record1.getR43_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 43 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR43_average() != null)
						cellD.setCellValue(record.getR43_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 45 / Col C =====
					row = sheet.getRow(44);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR45_month_end() != null)
						cellC.setCellValue(record.getR45_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 45 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR45_average() != null)
						cellD.setCellValue(record.getR45_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 46 / Col C =====
					row = sheet.getRow(45);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR46_month_end() != null)
						cellC.setCellValue(record.getR46_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 46 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR46_average() != null)
						cellD.setCellValue(record.getR46_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 47 / Col C =====
					row = sheet.getRow(46);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR47_month_end() != null)
						cellC.setCellValue(record.getR47_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 47 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR47_average() != null)
						cellD.setCellValue(record.getR47_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 48 / Col C =====
					row = sheet.getRow(47);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR48_month_end() != null)
						cellC.setCellValue(record.getR48_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 48 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR48_average() != null)
						cellD.setCellValue(record.getR48_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 50 / Col C =====
					row = sheet.getRow(49);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR50_month_end() != null)
						cellC.setCellValue(record.getR50_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 50 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR50_average() != null)
						cellD.setCellValue(record.getR50_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 51 / Col C =====
					row = sheet.getRow(50);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR51_MONTH_END() != null)
						cellC.setCellValue(record1.getR51_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 51 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR51_average() != null)
						cellD.setCellValue(record.getR51_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 52 / Col C =====
					row = sheet.getRow(51);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR52_MONTH_END() != null)
						cellC.setCellValue(record1.getR52_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 52 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR52_average() != null)
						cellD.setCellValue(record.getR52_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 53 / Col C =====
					row = sheet.getRow(52);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR53_month_end() != null)
						cellC.setCellValue(record.getR53_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 53 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR53_average() != null)
						cellD.setCellValue(record.getR53_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 54 / Col C =====
					row = sheet.getRow(53);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR54_month_end() != null)
						cellC.setCellValue(record.getR54_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 54 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR54_average() != null)
						cellD.setCellValue(record.getR54_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 55 / Col C =====
					row = sheet.getRow(54);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR55_month_end() != null)
						cellC.setCellValue(record.getR55_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 55 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR55_average() != null)
						cellD.setCellValue(record.getR55_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 57 / Col C =====
					row = sheet.getRow(56);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR57_MONTH_END() != null)
						cellC.setCellValue(record1.getR57_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 57 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR57_average() != null)
						cellD.setCellValue(record.getR57_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 58 / Col C =====
					row = sheet.getRow(57);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR58_month_end() != null)
						cellC.setCellValue(record.getR58_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 58 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR58_average() != null)
						cellD.setCellValue(record.getR58_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 59 / Col C =====
					row = sheet.getRow(58);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR59_MONTH_END() != null)
						cellC.setCellValue(record1.getR59_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 59 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR59_average() != null)
						cellD.setCellValue(record.getR59_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 60 / Col C =====
					row = sheet.getRow(59);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR60_month_end() != null)
						cellC.setCellValue(record.getR60_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 60 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR60_average() != null)
						cellD.setCellValue(record.getR60_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SFINP1 EMAIL ARCHIVALSUMMARY", null,
						"BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	// ------------------------------
	// Finds detail entity by account number
	// ------------------------------
	public M_SFINP1_Detail_Entity findDetailByAcctnumber(String acctNumber) {
		String sql = "SELECT * FROM BRRS_M_SFINP1_DETAILTABLE WHERE ACCT_NUMBER = ?";
		List<M_SFINP1_Detail_Entity> list = jdbcTemplate.query(sql, new Object[] { acctNumber },
				new M_SFINP1DetailRowMapper());
		return list.isEmpty() ? null : list.get(0);
	}

	// ------------------------------
	// Saves detail entity
	// ------------------------------
	private void saveDetail(M_SFINP1_Detail_Entity entity) {
		String sql = "UPDATE BRRS_M_SFINP1_DETAILTABLE SET ACCT_NAME = ?, ACCT_BALANCE_IN_PULA = ?, AVERAGE = ? WHERE ACCT_NUMBER = ?";
		jdbcTemplate.update(sql, entity.getAcctName(), entity.getAcctBalanceInPula(), entity.getAverage(),
				entity.getAcctNumber());
	}

	// ------------------------------
	// Saves summary manual entity
	// ------------------------------
	private void saveSummaryManual(M_SFINP1_Summary_Manual_Entity entity) {
		String sql = "UPDATE BRRS_M_SFINP1_SUMMARYTABLE_MANUAL SET "
				+ "R14_MONTH_END = ?, R34_MONTH_END = ?, R37_MONTH_END = ?, R39_MONTH_END = ?, "
				+ "R43_MONTH_END = ?, R42_MONTH_END = ?, R50_MONTH_END = ?, R51_MONTH_END = ?, "
				+ "R52_MONTH_END = ?, R57_MONTH_END = ?, R59_MONTH_END = ?, REPORT_VERSION = ?, "
				+ "REPORT_FREQUENCY = ?, REPORT_CODE = ?, REPORT_DESC = ?, ENTITY_FLG = ?, "
				+ "MODIFY_FLG = ?, DEL_FLG = ? WHERE REPORT_DATE = ?";
		jdbcTemplate.update(sql, entity.getR14_MONTH_END(), entity.getR34_MONTH_END(), entity.getR37_MONTH_END(),
				entity.getR39_MONTH_END(), entity.getR43_MONTH_END(), entity.getR42_MONTH_END(),
				entity.getR50_MONTH_END(), entity.getR51_MONTH_END(), entity.getR52_MONTH_END(),
				entity.getR57_MONTH_END(), entity.getR59_MONTH_END(), entity.getREPORT_VERSION(),
				entity.getREPORT_FREQUENCY(), entity.getREPORT_CODE(), entity.getREPORT_DESC(), entity.getENTITY_FLG(),
				entity.getMODIFY_FLG(), entity.getDEL_FLG(), entity.getREPORT_DATE());
	}

	// ------------------------------
	// Generic reflection-based INSERT — builds the column list/placeholders from
	// the entity's declared fields (field name == column name throughout this file),
	// so it works unmodified for any of the SUMMARYTABLE / SUMMARYTABLE_MANUAL /
	// DETAILTABLE / ARCHIVALTABLE_* entity classes.
	// ------------------------------
	private void insertEntityReflective(String tableName, Object entity) {
		StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
		StringBuilder values = new StringBuilder(" VALUES (");
		List<Object> params = new ArrayList<>();
		java.lang.reflect.Field[] fields = entity.getClass().getDeclaredFields();
		boolean first = true;
		for (java.lang.reflect.Field field : fields) {
			if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			if (!first) {
				sql.append(", ");
				values.append(", ");
			}
			Column columnAnn = field.getAnnotation(Column.class);
			String columnName = (columnAnn != null && !columnAnn.name().isEmpty()) ? columnAnn.name()
					: field.getName();
			sql.append(columnName);
			values.append("?");
			try {
				field.setAccessible(true);
				params.add(field.get(entity));
			} catch (Exception e) {
				params.add(null);
			}
			first = false;
		}
		sql.append(")").append(values).append(")");
		jdbcTemplate.update(sql.toString(), params.toArray());
	}

	// ------------------------------
	// Finds the highest REPORT_VERSION already archived for a given date
	// ------------------------------
	private BigDecimal findMaxVersion(Date reportDate) {
		BigDecimal maxVersion = jdbcTemplate.queryForObject(
				"SELECT MAX(REPORT_VERSION) FROM BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?",
				new Object[] { reportDate }, BigDecimal.class);
		return maxVersion != null ? maxVersion : BigDecimal.ZERO;
	}

	// ------------------------------
	// Promotes one archived (date, version) resub snapshot into the live tables,
	// replacing whatever is currently live for that date, so it can be corrected
	// using the report's normal live-edit tooling.
	// ------------------------------
	@Transactional
	public void promoteResubToLive(Date reportDate, BigDecimal version) {
		logger.info("Promoting M_SFINP1 resub snapshot to live — date={}, version={}", reportDate, version);

		List<M_SFINP1_Archival_Summary_Entity> archSummaryList = jdbcTemplate.query(
				"SELECT * FROM BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
				new Object[] { reportDate, version }, new M_SFINP1ArchivalSummaryRowMapper());
		if (archSummaryList.isEmpty()) {
			throw new RuntimeException(
					"No archival summary found for REPORT_DATE=" + reportDate + " REPORT_VERSION=" + version);
		}
		M_SFINP1_Archival_Summary_Entity archSummary = archSummaryList.get(0);

		List<M_SFINP1_Archival_Summary_Manual_Entity> archSummaryManualList = jdbcTemplate.query(
				"SELECT * FROM BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY_MANUAL WHERE REPORT_DATE = ?",
				new Object[] { reportDate }, new M_SFINP1ArchivalSummaryManualRowMapper());

		List<M_SFINP1_Archival_Detail_Entity> archDetailList = jdbcTemplate.query(
				"SELECT * FROM BRRS_M_SFINP1_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?",
				new Object[] { reportDate, version.toString() }, new M_SFINP1ArchivalDetailRowMapper());

		// Replace whatever is currently live for this date with the archived snapshot.
		jdbcTemplate.update("DELETE FROM BRRS_M_SFINP1_DETAILTABLE WHERE REPORT_DATE = ?", reportDate);
		jdbcTemplate.update("DELETE FROM BRRS_M_SFINP1_SUMMARYTABLE_MANUAL WHERE REPORT_DATE = ?", reportDate);
		jdbcTemplate.update("DELETE FROM BRRS_M_SFINP1_SUMMARYTABLE WHERE REPORT_DATE = ?", reportDate);

		M_SFINP1_Summary_Entity liveSummary = new M_SFINP1_Summary_Entity();
		BeanUtils.copyProperties(archSummary, liveSummary);
		// REPORT_DATE/REPORT_VERSION getters differ in case between the archival and live
		// summary entities, so BeanUtils won't match them by property name — set explicitly.
		liveSummary.setReport_date(reportDate);
		liveSummary.setReport_version(version);
		insertEntityReflective("BRRS_M_SFINP1_SUMMARYTABLE", liveSummary);

		if (!archSummaryManualList.isEmpty()) {
			M_SFINP1_Summary_Manual_Entity liveSummaryManual = new M_SFINP1_Summary_Manual_Entity();
			BeanUtils.copyProperties(archSummaryManualList.get(0), liveSummaryManual);
			liveSummaryManual.setREPORT_DATE(reportDate);
			insertEntityReflective("BRRS_M_SFINP1_SUMMARYTABLE_MANUAL", liveSummaryManual);
		}

		for (M_SFINP1_Archival_Detail_Entity archDetail : archDetailList) {
			M_SFINP1_Detail_Entity liveDetail = new M_SFINP1_Detail_Entity();
			BeanUtils.copyProperties(archDetail, liveDetail);
			liveDetail.setReportDate(reportDate);
			insertEntityReflective("BRRS_M_SFINP1_DETAILTABLE", liveDetail);
		}

		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs != null) {
			HttpServletRequest request = attrs.getRequest();
			String userid = (String) request.getSession().getAttribute("USERID");
			auditService.createBusinessAudit(userid, "PROMOTE_RESUB_TO_LIVE", "M_SFINP1 Resub Promote to Live",
					null, "BRRS_M_SFINP1_SUMMARYTABLE");
		}

		logger.info("M_SFINP1 resub promote-to-live complete for date={} version={}", reportDate, version);
	}

	// ------------------------------
	// Finalises a live report that was promoted from resub: runs the existing
	// BRRS_M_SFINP1_SUMMARY_PROCEDURE for the report's own date, then snapshots
	// whatever the procedure leaves live back into the archival tables as a new,
	// incremented REPORT_VERSION (the procedure itself always hardcodes version
	// '1', so the increment has to happen here in Java, not in the DB).
	// ------------------------------
	@Transactional
	public void finaliseResub(Date reportDate) {
		String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(reportDate);
		logger.info("Finalising M_SFINP1 resub for date={} — running BRRS_M_SFINP1_SUMMARY_PROCEDURE({})", reportDate,
				formattedDate);

		// Run synchronously (not afterCommit) — the version-increment step below depends on
		// reading the procedure's output, so it must complete before we continue.
		jdbcTemplate.update("BEGIN BRRS_M_SFINP1_SUMMARY_PROCEDURE(?); END;", formattedDate);
		logger.info("BRRS_M_SFINP1_SUMMARY_PROCEDURE completed for date={}", reportDate);

		List<M_SFINP1_Summary_Entity> liveSummaryList = jdbcTemplate.query(
				"SELECT * FROM BRRS_M_SFINP1_SUMMARYTABLE WHERE REPORT_DATE = ?", new Object[] { reportDate },
				new M_SFINP1SummaryRowMapper());
		if (liveSummaryList.isEmpty()) {
			throw new RuntimeException(
					"BRRS_M_SFINP1_SUMMARY_PROCEDURE left no live summary row for REPORT_DATE=" + reportDate);
		}
		M_SFINP1_Summary_Entity liveSummary = liveSummaryList.get(0);

		List<M_SFINP1_Summary_Manual_Entity> liveSummaryManualList = jdbcTemplate.query(
				"SELECT * FROM BRRS_M_SFINP1_SUMMARYTABLE_MANUAL WHERE REPORT_DATE = ?", new Object[] { reportDate },
				new M_SFINP1SummaryManualRowMapper());

		List<M_SFINP1_Detail_Entity> liveDetailList = jdbcTemplate.query(
				"SELECT * FROM BRRS_M_SFINP1_DETAILTABLE WHERE REPORT_DATE = ?", new Object[] { reportDate },
				new M_SFINP1DetailRowMapper());

		BigDecimal newVersion = findMaxVersion(reportDate).add(BigDecimal.ONE);

		M_SFINP1_Archival_Summary_Entity archSummary = new M_SFINP1_Archival_Summary_Entity();
		BeanUtils.copyProperties(liveSummary, archSummary);
		archSummary.setReportDate(reportDate);
		archSummary.setReportVersion(newVersion);
		archSummary.setReportResubDate(new Date());
		insertEntityReflective("BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY", archSummary);

		if (!liveSummaryManualList.isEmpty()) {
			M_SFINP1_Archival_Summary_Manual_Entity archSummaryManual = new M_SFINP1_Archival_Summary_Manual_Entity();
			BeanUtils.copyProperties(liveSummaryManualList.get(0), archSummaryManual);
			archSummaryManual.setREPORT_DATE(reportDate);
			archSummaryManual.setREPORT_VERSION(newVersion);
			insertEntityReflective("BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY_MANUAL", archSummaryManual);
		}

		for (M_SFINP1_Detail_Entity liveDetail : liveDetailList) {
			M_SFINP1_Archival_Detail_Entity archDetail = new M_SFINP1_Archival_Detail_Entity();
			BeanUtils.copyProperties(liveDetail, archDetail);
			archDetail.setReportDate(reportDate);
			archDetail.setDataEntryVersion(newVersion.toString());
			insertEntityReflective("BRRS_M_SFINP1_ARCHIVALTABLE_DETAIL", archDetail);
		}

		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs != null) {
			HttpServletRequest request = attrs.getRequest();
			String userid = (String) request.getSession().getAttribute("USERID");
			auditService.createBusinessAudit(userid, "FINALISE_RESUB", "M_SFINP1 Resub Finalise",
					null, "BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY");
		}

		logger.info("M_SFINP1 resub finalised for date={} — new REPORT_VERSION={}", reportDate, newVersion);
	}

// =========================================================================
// M_SFINP1 ENTITIES & PK DEFINITIONS
// =========================================================================

	// ------------------------------
	// PK class for composite keys
	// ------------------------------
	public static class M_SFINP1_PK implements Serializable {
		private Date reportDate;
		private BigDecimal reportVersion;

		// default constructor
		public M_SFINP1_PK() {
		}

		// parameterized constructor
		public M_SFINP1_PK(Date reportDate, BigDecimal reportVersion) {
			this.reportDate = reportDate;
			this.reportVersion = reportVersion;
		}

		// equals and hashCode
		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof M_SFINP1_PK))
				return false;
			M_SFINP1_PK that = (M_SFINP1_PK) o;
			return Objects.equals(reportDate, that.reportDate) && Objects.equals(reportVersion, that.reportVersion);
		}

		@Override
		public int hashCode() {
			return Objects.hash(reportDate, reportVersion);
		}

		// getters & setters
		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public BigDecimal getReportVersion() {
			return reportVersion;
		}

		public void setReportVersion(BigDecimal reportVersion) {
			this.reportVersion = reportVersion;
		}
	}

	// ------------------------------
	// Summary Entity
	// ------------------------------
	public static class M_SFINP1_Summary_Entity {
		private String r10_product;
		private String r10_cross_ref;
		private BigDecimal r10_month_end;
		private BigDecimal r10_average;
		private String r11_product;
		private String r11_cross_ref;
		private BigDecimal r11_month_end;
		private BigDecimal r11_average;
		private String r12_product;
		private String r12_cross_ref;
		private BigDecimal r12_month_end;
		private BigDecimal r12_average;
		private String r13_product;
		private String r13_cross_ref;
		private BigDecimal r13_month_end;
		private BigDecimal r13_average;
		private String r14_product;
		private String r14_cross_ref;
		private BigDecimal r14_month_end;
		private BigDecimal r14_average;
		private String r15_product;
		private String r15_cross_ref;
		private BigDecimal r15_month_end;
		private BigDecimal r15_average;
		private String r16_product;
		private String r16_cross_ref;
		private BigDecimal r16_month_end;
		private BigDecimal r16_average;
		private String r17_product;
		private String r17_cross_ref;
		private BigDecimal r17_month_end;
		private BigDecimal r17_average;
		private String r18_product;
		private String r18_cross_ref;
		private BigDecimal r18_month_end;
		private BigDecimal r18_average;
		private String r19_product;
		private String r19_cross_ref;
		private BigDecimal r19_month_end;
		private BigDecimal r19_average;
		private String r20_product;
		private String r20_cross_ref;
		private BigDecimal r20_month_end;
		private BigDecimal r20_average;
		private String r21_product;
		private String r21_cross_ref;
		private BigDecimal r21_month_end;
		private BigDecimal r21_average;
		private String r22_product;
		private String r22_cross_ref;
		private BigDecimal r22_month_end;
		private BigDecimal r22_average;
		private String r23_product;
		private String r23_cross_ref;
		private BigDecimal r23_month_end;
		private BigDecimal r23_average;
		private String r24_product;
		private String r24_cross_ref;
		private BigDecimal r24_month_end;
		private BigDecimal r24_average;
		private String r25_product;
		private String r25_cross_ref;
		private BigDecimal r25_month_end;
		private BigDecimal r25_average;
		private String r26_product;
		private String r26_cross_ref;
		private BigDecimal r26_month_end;
		private BigDecimal r26_average;
		private String r27_product;
		private String r27_cross_ref;
		private BigDecimal r27_month_end;
		private BigDecimal r27_average;
		private String r28_product;
		private String r28_cross_ref;
		private BigDecimal r28_month_end;
		private BigDecimal r28_average;
		private String r29_product;
		private String r29_cross_ref;
		private BigDecimal r29_month_end;
		private BigDecimal r29_average;
		private String r30_product;
		private String r30_cross_ref;
		private BigDecimal r30_month_end;
		private BigDecimal r30_average;
		private String r31_product;
		private String r31_cross_ref;
		private BigDecimal r31_month_end;
		private BigDecimal r31_average;
		private String r32_product;
		private String r32_cross_ref;
		private BigDecimal r32_month_end;
		private BigDecimal r32_average;
		private String r33_product;
		private String r33_cross_ref;
		private BigDecimal r33_month_end;
		private BigDecimal r33_average;
		private String r34_product;
		private String r34_cross_ref;
		private BigDecimal r34_month_end;
		private BigDecimal r34_average;
		private String r35_product;
		private String r35_cross_ref;
		private BigDecimal r35_month_end;
		private BigDecimal r35_average;
		private String r36_product;
		private String r36_cross_ref;
		private BigDecimal r36_month_end;
		private BigDecimal r36_average;
		private String r37_product;
		private String r37_cross_ref;
		private BigDecimal r37_month_end;
		private BigDecimal r37_average;
		private String r38_product;
		private String r38_cross_ref;
		private BigDecimal r38_month_end;
		private BigDecimal r38_average;
		private String r39_product;
		private String r39_cross_ref;
		private BigDecimal r39_month_end;
		private BigDecimal r39_average;
		private String r40_product;
		private String r40_cross_ref;
		private BigDecimal r40_month_end;
		private BigDecimal r40_average;
		private String r41_product;
		private String r41_cross_ref;
		private BigDecimal r41_month_end;
		private BigDecimal r41_average;
		private String r42_product;
		private String r42_cross_ref;
		private BigDecimal r42_month_end;
		private BigDecimal r42_average;
		private String r43_product;
		private String r43_cross_ref;
		private BigDecimal r43_month_end;
		private BigDecimal r43_average;
		private String r44_product;
		private String r44_cross_ref;
		private BigDecimal r44_month_end;
		private BigDecimal r44_average;
		private String r45_product;
		private String r45_cross_ref;
		private BigDecimal r45_month_end;
		private BigDecimal r45_average;
		private String r46_product;
		private String r46_cross_ref;
		private BigDecimal r46_month_end;
		private BigDecimal r46_average;
		private String r47_product;
		private String r47_cross_ref;
		private BigDecimal r47_month_end;
		private BigDecimal r47_average;
		private String r48_product;
		private String r48_cross_ref;
		private BigDecimal r48_month_end;
		private BigDecimal r48_average;
		private String r49_product;
		private String r49_cross_ref;
		private BigDecimal r49_month_end;
		private BigDecimal r49_average;
		private String r50_product;
		private String r50_cross_ref;
		private BigDecimal r50_month_end;
		private BigDecimal r50_average;
		private String r51_product;
		private String r51_cross_ref;
		private BigDecimal r51_month_end;
		private BigDecimal r51_average;
		private String r52_product;
		private String r52_cross_ref;
		private BigDecimal r52_month_end;
		private BigDecimal r52_average;
		private String r53_product;
		private String r53_cross_ref;
		private BigDecimal r53_month_end;
		private BigDecimal r53_average;
		private String r54_product;
		private String r54_cross_ref;
		private BigDecimal r54_month_end;
		private BigDecimal r54_average;
		private String r55_product;
		private String r55_cross_ref;
		private BigDecimal r55_month_end;
		private BigDecimal r55_average;
		private String r56_product;
		private String r56_cross_ref;
		private BigDecimal r56_month_end;
		private BigDecimal r56_average;
		private String r57_product;
		private String r57_cross_ref;
		private BigDecimal r57_month_end;
		private BigDecimal r57_average;
		private String r58_product;
		private String r58_cross_ref;
		private BigDecimal r58_month_end;
		private BigDecimal r58_average;
		private String r59_product;
		private String r59_cross_ref;
		private BigDecimal r59_month_end;
		private BigDecimal r59_average;
		private String r60_product;
		private String r60_cross_ref;
		private BigDecimal r60_month_end;
		private BigDecimal r60_average;
		private String r61_product;
		private String r61_cross_ref;
		private BigDecimal r61_month_end;
		private BigDecimal r61_average;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String r10_product) {
			this.r10_product = r10_product;
		}

		public String getR10_cross_ref() {
			return r10_cross_ref;
		}

		public void setR10_cross_ref(String r10_cross_ref) {
			this.r10_cross_ref = r10_cross_ref;
		}

		public BigDecimal getR10_month_end() {
			return r10_month_end;
		}

		public void setR10_month_end(BigDecimal r10_month_end) {
			this.r10_month_end = r10_month_end;
		}

		public BigDecimal getR10_average() {
			return r10_average;
		}

		public void setR10_average(BigDecimal r10_average) {
			this.r10_average = r10_average;
		}

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public String getR11_cross_ref() {
			return r11_cross_ref;
		}

		public void setR11_cross_ref(String r11_cross_ref) {
			this.r11_cross_ref = r11_cross_ref;
		}

		public BigDecimal getR11_month_end() {
			return r11_month_end;
		}

		public void setR11_month_end(BigDecimal r11_month_end) {
			this.r11_month_end = r11_month_end;
		}

		public BigDecimal getR11_average() {
			return r11_average;
		}

		public void setR11_average(BigDecimal r11_average) {
			this.r11_average = r11_average;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public String getR12_cross_ref() {
			return r12_cross_ref;
		}

		public void setR12_cross_ref(String r12_cross_ref) {
			this.r12_cross_ref = r12_cross_ref;
		}

		public BigDecimal getR12_month_end() {
			return r12_month_end;
		}

		public void setR12_month_end(BigDecimal r12_month_end) {
			this.r12_month_end = r12_month_end;
		}

		public BigDecimal getR12_average() {
			return r12_average;
		}

		public void setR12_average(BigDecimal r12_average) {
			this.r12_average = r12_average;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public String getR13_cross_ref() {
			return r13_cross_ref;
		}

		public void setR13_cross_ref(String r13_cross_ref) {
			this.r13_cross_ref = r13_cross_ref;
		}

		public BigDecimal getR13_month_end() {
			return r13_month_end;
		}

		public void setR13_month_end(BigDecimal r13_month_end) {
			this.r13_month_end = r13_month_end;
		}

		public BigDecimal getR13_average() {
			return r13_average;
		}

		public void setR13_average(BigDecimal r13_average) {
			this.r13_average = r13_average;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public String getR14_cross_ref() {
			return r14_cross_ref;
		}

		public void setR14_cross_ref(String r14_cross_ref) {
			this.r14_cross_ref = r14_cross_ref;
		}

		public BigDecimal getR14_month_end() {
			return r14_month_end;
		}

		public void setR14_month_end(BigDecimal r14_month_end) {
			this.r14_month_end = r14_month_end;
		}

		public BigDecimal getR14_average() {
			return r14_average;
		}

		public void setR14_average(BigDecimal r14_average) {
			this.r14_average = r14_average;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public String getR15_cross_ref() {
			return r15_cross_ref;
		}

		public void setR15_cross_ref(String r15_cross_ref) {
			this.r15_cross_ref = r15_cross_ref;
		}

		public BigDecimal getR15_month_end() {
			return r15_month_end;
		}

		public void setR15_month_end(BigDecimal r15_month_end) {
			this.r15_month_end = r15_month_end;
		}

		public BigDecimal getR15_average() {
			return r15_average;
		}

		public void setR15_average(BigDecimal r15_average) {
			this.r15_average = r15_average;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public String getR16_cross_ref() {
			return r16_cross_ref;
		}

		public void setR16_cross_ref(String r16_cross_ref) {
			this.r16_cross_ref = r16_cross_ref;
		}

		public BigDecimal getR16_month_end() {
			return r16_month_end;
		}

		public void setR16_month_end(BigDecimal r16_month_end) {
			this.r16_month_end = r16_month_end;
		}

		public BigDecimal getR16_average() {
			return r16_average;
		}

		public void setR16_average(BigDecimal r16_average) {
			this.r16_average = r16_average;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public String getR17_cross_ref() {
			return r17_cross_ref;
		}

		public void setR17_cross_ref(String r17_cross_ref) {
			this.r17_cross_ref = r17_cross_ref;
		}

		public BigDecimal getR17_month_end() {
			return r17_month_end;
		}

		public void setR17_month_end(BigDecimal r17_month_end) {
			this.r17_month_end = r17_month_end;
		}

		public BigDecimal getR17_average() {
			return r17_average;
		}

		public void setR17_average(BigDecimal r17_average) {
			this.r17_average = r17_average;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public String getR18_cross_ref() {
			return r18_cross_ref;
		}

		public void setR18_cross_ref(String r18_cross_ref) {
			this.r18_cross_ref = r18_cross_ref;
		}

		public BigDecimal getR18_month_end() {
			return r18_month_end;
		}

		public void setR18_month_end(BigDecimal r18_month_end) {
			this.r18_month_end = r18_month_end;
		}

		public BigDecimal getR18_average() {
			return r18_average;
		}

		public void setR18_average(BigDecimal r18_average) {
			this.r18_average = r18_average;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public String getR19_cross_ref() {
			return r19_cross_ref;
		}

		public void setR19_cross_ref(String r19_cross_ref) {
			this.r19_cross_ref = r19_cross_ref;
		}

		public BigDecimal getR19_month_end() {
			return r19_month_end;
		}

		public void setR19_month_end(BigDecimal r19_month_end) {
			this.r19_month_end = r19_month_end;
		}

		public BigDecimal getR19_average() {
			return r19_average;
		}

		public void setR19_average(BigDecimal r19_average) {
			this.r19_average = r19_average;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public String getR20_cross_ref() {
			return r20_cross_ref;
		}

		public void setR20_cross_ref(String r20_cross_ref) {
			this.r20_cross_ref = r20_cross_ref;
		}

		public BigDecimal getR20_month_end() {
			return r20_month_end;
		}

		public void setR20_month_end(BigDecimal r20_month_end) {
			this.r20_month_end = r20_month_end;
		}

		public BigDecimal getR20_average() {
			return r20_average;
		}

		public void setR20_average(BigDecimal r20_average) {
			this.r20_average = r20_average;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public String getR21_cross_ref() {
			return r21_cross_ref;
		}

		public void setR21_cross_ref(String r21_cross_ref) {
			this.r21_cross_ref = r21_cross_ref;
		}

		public BigDecimal getR21_month_end() {
			return r21_month_end;
		}

		public void setR21_month_end(BigDecimal r21_month_end) {
			this.r21_month_end = r21_month_end;
		}

		public BigDecimal getR21_average() {
			return r21_average;
		}

		public void setR21_average(BigDecimal r21_average) {
			this.r21_average = r21_average;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public String getR22_cross_ref() {
			return r22_cross_ref;
		}

		public void setR22_cross_ref(String r22_cross_ref) {
			this.r22_cross_ref = r22_cross_ref;
		}

		public BigDecimal getR22_month_end() {
			return r22_month_end;
		}

		public void setR22_month_end(BigDecimal r22_month_end) {
			this.r22_month_end = r22_month_end;
		}

		public BigDecimal getR22_average() {
			return r22_average;
		}

		public void setR22_average(BigDecimal r22_average) {
			this.r22_average = r22_average;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public String getR23_cross_ref() {
			return r23_cross_ref;
		}

		public void setR23_cross_ref(String r23_cross_ref) {
			this.r23_cross_ref = r23_cross_ref;
		}

		public BigDecimal getR23_month_end() {
			return r23_month_end;
		}

		public void setR23_month_end(BigDecimal r23_month_end) {
			this.r23_month_end = r23_month_end;
		}

		public BigDecimal getR23_average() {
			return r23_average;
		}

		public void setR23_average(BigDecimal r23_average) {
			this.r23_average = r23_average;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public String getR24_cross_ref() {
			return r24_cross_ref;
		}

		public void setR24_cross_ref(String r24_cross_ref) {
			this.r24_cross_ref = r24_cross_ref;
		}

		public BigDecimal getR24_month_end() {
			return r24_month_end;
		}

		public void setR24_month_end(BigDecimal r24_month_end) {
			this.r24_month_end = r24_month_end;
		}

		public BigDecimal getR24_average() {
			return r24_average;
		}

		public void setR24_average(BigDecimal r24_average) {
			this.r24_average = r24_average;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public String getR25_cross_ref() {
			return r25_cross_ref;
		}

		public void setR25_cross_ref(String r25_cross_ref) {
			this.r25_cross_ref = r25_cross_ref;
		}

		public BigDecimal getR25_month_end() {
			return r25_month_end;
		}

		public void setR25_month_end(BigDecimal r25_month_end) {
			this.r25_month_end = r25_month_end;
		}

		public BigDecimal getR25_average() {
			return r25_average;
		}

		public void setR25_average(BigDecimal r25_average) {
			this.r25_average = r25_average;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public String getR26_cross_ref() {
			return r26_cross_ref;
		}

		public void setR26_cross_ref(String r26_cross_ref) {
			this.r26_cross_ref = r26_cross_ref;
		}

		public BigDecimal getR26_month_end() {
			return r26_month_end;
		}

		public void setR26_month_end(BigDecimal r26_month_end) {
			this.r26_month_end = r26_month_end;
		}

		public BigDecimal getR26_average() {
			return r26_average;
		}

		public void setR26_average(BigDecimal r26_average) {
			this.r26_average = r26_average;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public String getR27_cross_ref() {
			return r27_cross_ref;
		}

		public void setR27_cross_ref(String r27_cross_ref) {
			this.r27_cross_ref = r27_cross_ref;
		}

		public BigDecimal getR27_month_end() {
			return r27_month_end;
		}

		public void setR27_month_end(BigDecimal r27_month_end) {
			this.r27_month_end = r27_month_end;
		}

		public BigDecimal getR27_average() {
			return r27_average;
		}

		public void setR27_average(BigDecimal r27_average) {
			this.r27_average = r27_average;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public String getR28_cross_ref() {
			return r28_cross_ref;
		}

		public void setR28_cross_ref(String r28_cross_ref) {
			this.r28_cross_ref = r28_cross_ref;
		}

		public BigDecimal getR28_month_end() {
			return r28_month_end;
		}

		public void setR28_month_end(BigDecimal r28_month_end) {
			this.r28_month_end = r28_month_end;
		}

		public BigDecimal getR28_average() {
			return r28_average;
		}

		public void setR28_average(BigDecimal r28_average) {
			this.r28_average = r28_average;
		}

		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public String getR29_cross_ref() {
			return r29_cross_ref;
		}

		public void setR29_cross_ref(String r29_cross_ref) {
			this.r29_cross_ref = r29_cross_ref;
		}

		public BigDecimal getR29_month_end() {
			return r29_month_end;
		}

		public void setR29_month_end(BigDecimal r29_month_end) {
			this.r29_month_end = r29_month_end;
		}

		public BigDecimal getR29_average() {
			return r29_average;
		}

		public void setR29_average(BigDecimal r29_average) {
			this.r29_average = r29_average;
		}

		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public String getR30_cross_ref() {
			return r30_cross_ref;
		}

		public void setR30_cross_ref(String r30_cross_ref) {
			this.r30_cross_ref = r30_cross_ref;
		}

		public BigDecimal getR30_month_end() {
			return r30_month_end;
		}

		public void setR30_month_end(BigDecimal r30_month_end) {
			this.r30_month_end = r30_month_end;
		}

		public BigDecimal getR30_average() {
			return r30_average;
		}

		public void setR30_average(BigDecimal r30_average) {
			this.r30_average = r30_average;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public String getR31_cross_ref() {
			return r31_cross_ref;
		}

		public void setR31_cross_ref(String r31_cross_ref) {
			this.r31_cross_ref = r31_cross_ref;
		}

		public BigDecimal getR31_month_end() {
			return r31_month_end;
		}

		public void setR31_month_end(BigDecimal r31_month_end) {
			this.r31_month_end = r31_month_end;
		}

		public BigDecimal getR31_average() {
			return r31_average;
		}

		public void setR31_average(BigDecimal r31_average) {
			this.r31_average = r31_average;
		}

		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String r32_product) {
			this.r32_product = r32_product;
		}

		public String getR32_cross_ref() {
			return r32_cross_ref;
		}

		public void setR32_cross_ref(String r32_cross_ref) {
			this.r32_cross_ref = r32_cross_ref;
		}

		public BigDecimal getR32_month_end() {
			return r32_month_end;
		}

		public void setR32_month_end(BigDecimal r32_month_end) {
			this.r32_month_end = r32_month_end;
		}

		public BigDecimal getR32_average() {
			return r32_average;
		}

		public void setR32_average(BigDecimal r32_average) {
			this.r32_average = r32_average;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public String getR33_cross_ref() {
			return r33_cross_ref;
		}

		public void setR33_cross_ref(String r33_cross_ref) {
			this.r33_cross_ref = r33_cross_ref;
		}

		public BigDecimal getR33_month_end() {
			return r33_month_end;
		}

		public void setR33_month_end(BigDecimal r33_month_end) {
			this.r33_month_end = r33_month_end;
		}

		public BigDecimal getR33_average() {
			return r33_average;
		}

		public void setR33_average(BigDecimal r33_average) {
			this.r33_average = r33_average;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public String getR34_cross_ref() {
			return r34_cross_ref;
		}

		public void setR34_cross_ref(String r34_cross_ref) {
			this.r34_cross_ref = r34_cross_ref;
		}

		public BigDecimal getR34_month_end() {
			return r34_month_end;
		}

		public void setR34_month_end(BigDecimal r34_month_end) {
			this.r34_month_end = r34_month_end;
		}

		public BigDecimal getR34_average() {
			return r34_average;
		}

		public void setR34_average(BigDecimal r34_average) {
			this.r34_average = r34_average;
		}

		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String r35_product) {
			this.r35_product = r35_product;
		}

		public String getR35_cross_ref() {
			return r35_cross_ref;
		}

		public void setR35_cross_ref(String r35_cross_ref) {
			this.r35_cross_ref = r35_cross_ref;
		}

		public BigDecimal getR35_month_end() {
			return r35_month_end;
		}

		public void setR35_month_end(BigDecimal r35_month_end) {
			this.r35_month_end = r35_month_end;
		}

		public BigDecimal getR35_average() {
			return r35_average;
		}

		public void setR35_average(BigDecimal r35_average) {
			this.r35_average = r35_average;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public String getR36_cross_ref() {
			return r36_cross_ref;
		}

		public void setR36_cross_ref(String r36_cross_ref) {
			this.r36_cross_ref = r36_cross_ref;
		}

		public BigDecimal getR36_month_end() {
			return r36_month_end;
		}

		public void setR36_month_end(BigDecimal r36_month_end) {
			this.r36_month_end = r36_month_end;
		}

		public BigDecimal getR36_average() {
			return r36_average;
		}

		public void setR36_average(BigDecimal r36_average) {
			this.r36_average = r36_average;
		}

		public String getR37_product() {
			return r37_product;
		}

		public void setR37_product(String r37_product) {
			this.r37_product = r37_product;
		}

		public String getR37_cross_ref() {
			return r37_cross_ref;
		}

		public void setR37_cross_ref(String r37_cross_ref) {
			this.r37_cross_ref = r37_cross_ref;
		}

		public BigDecimal getR37_month_end() {
			return r37_month_end;
		}

		public void setR37_month_end(BigDecimal r37_month_end) {
			this.r37_month_end = r37_month_end;
		}

		public BigDecimal getR37_average() {
			return r37_average;
		}

		public void setR37_average(BigDecimal r37_average) {
			this.r37_average = r37_average;
		}

		public String getR38_product() {
			return r38_product;
		}

		public void setR38_product(String r38_product) {
			this.r38_product = r38_product;
		}

		public String getR38_cross_ref() {
			return r38_cross_ref;
		}

		public void setR38_cross_ref(String r38_cross_ref) {
			this.r38_cross_ref = r38_cross_ref;
		}

		public BigDecimal getR38_month_end() {
			return r38_month_end;
		}

		public void setR38_month_end(BigDecimal r38_month_end) {
			this.r38_month_end = r38_month_end;
		}

		public BigDecimal getR38_average() {
			return r38_average;
		}

		public void setR38_average(BigDecimal r38_average) {
			this.r38_average = r38_average;
		}

		public String getR39_product() {
			return r39_product;
		}

		public void setR39_product(String r39_product) {
			this.r39_product = r39_product;
		}

		public String getR39_cross_ref() {
			return r39_cross_ref;
		}

		public void setR39_cross_ref(String r39_cross_ref) {
			this.r39_cross_ref = r39_cross_ref;
		}

		public BigDecimal getR39_month_end() {
			return r39_month_end;
		}

		public void setR39_month_end(BigDecimal r39_month_end) {
			this.r39_month_end = r39_month_end;
		}

		public BigDecimal getR39_average() {
			return r39_average;
		}

		public void setR39_average(BigDecimal r39_average) {
			this.r39_average = r39_average;
		}

		public String getR40_product() {
			return r40_product;
		}

		public void setR40_product(String r40_product) {
			this.r40_product = r40_product;
		}

		public String getR40_cross_ref() {
			return r40_cross_ref;
		}

		public void setR40_cross_ref(String r40_cross_ref) {
			this.r40_cross_ref = r40_cross_ref;
		}

		public BigDecimal getR40_month_end() {
			return r40_month_end;
		}

		public void setR40_month_end(BigDecimal r40_month_end) {
			this.r40_month_end = r40_month_end;
		}

		public BigDecimal getR40_average() {
			return r40_average;
		}

		public void setR40_average(BigDecimal r40_average) {
			this.r40_average = r40_average;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public String getR41_cross_ref() {
			return r41_cross_ref;
		}

		public void setR41_cross_ref(String r41_cross_ref) {
			this.r41_cross_ref = r41_cross_ref;
		}

		public BigDecimal getR41_month_end() {
			return r41_month_end;
		}

		public void setR41_month_end(BigDecimal r41_month_end) {
			this.r41_month_end = r41_month_end;
		}

		public BigDecimal getR41_average() {
			return r41_average;
		}

		public void setR41_average(BigDecimal r41_average) {
			this.r41_average = r41_average;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public String getR42_cross_ref() {
			return r42_cross_ref;
		}

		public void setR42_cross_ref(String r42_cross_ref) {
			this.r42_cross_ref = r42_cross_ref;
		}

		public BigDecimal getR42_month_end() {
			return r42_month_end;
		}

		public void setR42_month_end(BigDecimal r42_month_end) {
			this.r42_month_end = r42_month_end;
		}

		public BigDecimal getR42_average() {
			return r42_average;
		}

		public void setR42_average(BigDecimal r42_average) {
			this.r42_average = r42_average;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public String getR43_cross_ref() {
			return r43_cross_ref;
		}

		public void setR43_cross_ref(String r43_cross_ref) {
			this.r43_cross_ref = r43_cross_ref;
		}

		public BigDecimal getR43_month_end() {
			return r43_month_end;
		}

		public void setR43_month_end(BigDecimal r43_month_end) {
			this.r43_month_end = r43_month_end;
		}

		public BigDecimal getR43_average() {
			return r43_average;
		}

		public void setR43_average(BigDecimal r43_average) {
			this.r43_average = r43_average;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public String getR44_cross_ref() {
			return r44_cross_ref;
		}

		public void setR44_cross_ref(String r44_cross_ref) {
			this.r44_cross_ref = r44_cross_ref;
		}

		public BigDecimal getR44_month_end() {
			return r44_month_end;
		}

		public void setR44_month_end(BigDecimal r44_month_end) {
			this.r44_month_end = r44_month_end;
		}

		public BigDecimal getR44_average() {
			return r44_average;
		}

		public void setR44_average(BigDecimal r44_average) {
			this.r44_average = r44_average;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public String getR45_cross_ref() {
			return r45_cross_ref;
		}

		public void setR45_cross_ref(String r45_cross_ref) {
			this.r45_cross_ref = r45_cross_ref;
		}

		public BigDecimal getR45_month_end() {
			return r45_month_end;
		}

		public void setR45_month_end(BigDecimal r45_month_end) {
			this.r45_month_end = r45_month_end;
		}

		public BigDecimal getR45_average() {
			return r45_average;
		}

		public void setR45_average(BigDecimal r45_average) {
			this.r45_average = r45_average;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public String getR46_cross_ref() {
			return r46_cross_ref;
		}

		public void setR46_cross_ref(String r46_cross_ref) {
			this.r46_cross_ref = r46_cross_ref;
		}

		public BigDecimal getR46_month_end() {
			return r46_month_end;
		}

		public void setR46_month_end(BigDecimal r46_month_end) {
			this.r46_month_end = r46_month_end;
		}

		public BigDecimal getR46_average() {
			return r46_average;
		}

		public void setR46_average(BigDecimal r46_average) {
			this.r46_average = r46_average;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public String getR47_cross_ref() {
			return r47_cross_ref;
		}

		public void setR47_cross_ref(String r47_cross_ref) {
			this.r47_cross_ref = r47_cross_ref;
		}

		public BigDecimal getR47_month_end() {
			return r47_month_end;
		}

		public void setR47_month_end(BigDecimal r47_month_end) {
			this.r47_month_end = r47_month_end;
		}

		public BigDecimal getR47_average() {
			return r47_average;
		}

		public void setR47_average(BigDecimal r47_average) {
			this.r47_average = r47_average;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public String getR48_cross_ref() {
			return r48_cross_ref;
		}

		public void setR48_cross_ref(String r48_cross_ref) {
			this.r48_cross_ref = r48_cross_ref;
		}

		public BigDecimal getR48_month_end() {
			return r48_month_end;
		}

		public void setR48_month_end(BigDecimal r48_month_end) {
			this.r48_month_end = r48_month_end;
		}

		public BigDecimal getR48_average() {
			return r48_average;
		}

		public void setR48_average(BigDecimal r48_average) {
			this.r48_average = r48_average;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public String getR49_cross_ref() {
			return r49_cross_ref;
		}

		public void setR49_cross_ref(String r49_cross_ref) {
			this.r49_cross_ref = r49_cross_ref;
		}

		public BigDecimal getR49_month_end() {
			return r49_month_end;
		}

		public void setR49_month_end(BigDecimal r49_month_end) {
			this.r49_month_end = r49_month_end;
		}

		public BigDecimal getR49_average() {
			return r49_average;
		}

		public void setR49_average(BigDecimal r49_average) {
			this.r49_average = r49_average;
		}

		public String getR50_product() {
			return r50_product;
		}

		public void setR50_product(String r50_product) {
			this.r50_product = r50_product;
		}

		public String getR50_cross_ref() {
			return r50_cross_ref;
		}

		public void setR50_cross_ref(String r50_cross_ref) {
			this.r50_cross_ref = r50_cross_ref;
		}

		public BigDecimal getR50_month_end() {
			return r50_month_end;
		}

		public void setR50_month_end(BigDecimal r50_month_end) {
			this.r50_month_end = r50_month_end;
		}

		public BigDecimal getR50_average() {
			return r50_average;
		}

		public void setR50_average(BigDecimal r50_average) {
			this.r50_average = r50_average;
		}

		public String getR51_product() {
			return r51_product;
		}

		public void setR51_product(String r51_product) {
			this.r51_product = r51_product;
		}

		public String getR51_cross_ref() {
			return r51_cross_ref;
		}

		public void setR51_cross_ref(String r51_cross_ref) {
			this.r51_cross_ref = r51_cross_ref;
		}

		public BigDecimal getR51_month_end() {
			return r51_month_end;
		}

		public void setR51_month_end(BigDecimal r51_month_end) {
			this.r51_month_end = r51_month_end;
		}

		public BigDecimal getR51_average() {
			return r51_average;
		}

		public void setR51_average(BigDecimal r51_average) {
			this.r51_average = r51_average;
		}

		public String getR52_product() {
			return r52_product;
		}

		public void setR52_product(String r52_product) {
			this.r52_product = r52_product;
		}

		public String getR52_cross_ref() {
			return r52_cross_ref;
		}

		public void setR52_cross_ref(String r52_cross_ref) {
			this.r52_cross_ref = r52_cross_ref;
		}

		public BigDecimal getR52_month_end() {
			return r52_month_end;
		}

		public void setR52_month_end(BigDecimal r52_month_end) {
			this.r52_month_end = r52_month_end;
		}

		public BigDecimal getR52_average() {
			return r52_average;
		}

		public void setR52_average(BigDecimal r52_average) {
			this.r52_average = r52_average;
		}

		public String getR53_product() {
			return r53_product;
		}

		public void setR53_product(String r53_product) {
			this.r53_product = r53_product;
		}

		public String getR53_cross_ref() {
			return r53_cross_ref;
		}

		public void setR53_cross_ref(String r53_cross_ref) {
			this.r53_cross_ref = r53_cross_ref;
		}

		public BigDecimal getR53_month_end() {
			return r53_month_end;
		}

		public void setR53_month_end(BigDecimal r53_month_end) {
			this.r53_month_end = r53_month_end;
		}

		public BigDecimal getR53_average() {
			return r53_average;
		}

		public void setR53_average(BigDecimal r53_average) {
			this.r53_average = r53_average;
		}

		public String getR54_product() {
			return r54_product;
		}

		public void setR54_product(String r54_product) {
			this.r54_product = r54_product;
		}

		public String getR54_cross_ref() {
			return r54_cross_ref;
		}

		public void setR54_cross_ref(String r54_cross_ref) {
			this.r54_cross_ref = r54_cross_ref;
		}

		public BigDecimal getR54_month_end() {
			return r54_month_end;
		}

		public void setR54_month_end(BigDecimal r54_month_end) {
			this.r54_month_end = r54_month_end;
		}

		public BigDecimal getR54_average() {
			return r54_average;
		}

		public void setR54_average(BigDecimal r54_average) {
			this.r54_average = r54_average;
		}

		public String getR55_product() {
			return r55_product;
		}

		public void setR55_product(String r55_product) {
			this.r55_product = r55_product;
		}

		public String getR55_cross_ref() {
			return r55_cross_ref;
		}

		public void setR55_cross_ref(String r55_cross_ref) {
			this.r55_cross_ref = r55_cross_ref;
		}

		public BigDecimal getR55_month_end() {
			return r55_month_end;
		}

		public void setR55_month_end(BigDecimal r55_month_end) {
			this.r55_month_end = r55_month_end;
		}

		public BigDecimal getR55_average() {
			return r55_average;
		}

		public void setR55_average(BigDecimal r55_average) {
			this.r55_average = r55_average;
		}

		public String getR56_product() {
			return r56_product;
		}

		public void setR56_product(String r56_product) {
			this.r56_product = r56_product;
		}

		public String getR56_cross_ref() {
			return r56_cross_ref;
		}

		public void setR56_cross_ref(String r56_cross_ref) {
			this.r56_cross_ref = r56_cross_ref;
		}

		public BigDecimal getR56_month_end() {
			return r56_month_end;
		}

		public void setR56_month_end(BigDecimal r56_month_end) {
			this.r56_month_end = r56_month_end;
		}

		public BigDecimal getR56_average() {
			return r56_average;
		}

		public void setR56_average(BigDecimal r56_average) {
			this.r56_average = r56_average;
		}

		public String getR57_product() {
			return r57_product;
		}

		public void setR57_product(String r57_product) {
			this.r57_product = r57_product;
		}

		public String getR57_cross_ref() {
			return r57_cross_ref;
		}

		public void setR57_cross_ref(String r57_cross_ref) {
			this.r57_cross_ref = r57_cross_ref;
		}

		public BigDecimal getR57_month_end() {
			return r57_month_end;
		}

		public void setR57_month_end(BigDecimal r57_month_end) {
			this.r57_month_end = r57_month_end;
		}

		public BigDecimal getR57_average() {
			return r57_average;
		}

		public void setR57_average(BigDecimal r57_average) {
			this.r57_average = r57_average;
		}

		public String getR58_product() {
			return r58_product;
		}

		public void setR58_product(String r58_product) {
			this.r58_product = r58_product;
		}

		public String getR58_cross_ref() {
			return r58_cross_ref;
		}

		public void setR58_cross_ref(String r58_cross_ref) {
			this.r58_cross_ref = r58_cross_ref;
		}

		public BigDecimal getR58_month_end() {
			return r58_month_end;
		}

		public void setR58_month_end(BigDecimal r58_month_end) {
			this.r58_month_end = r58_month_end;
		}

		public BigDecimal getR58_average() {
			return r58_average;
		}

		public void setR58_average(BigDecimal r58_average) {
			this.r58_average = r58_average;
		}

		public String getR59_product() {
			return r59_product;
		}

		public void setR59_product(String r59_product) {
			this.r59_product = r59_product;
		}

		public String getR59_cross_ref() {
			return r59_cross_ref;
		}

		public void setR59_cross_ref(String r59_cross_ref) {
			this.r59_cross_ref = r59_cross_ref;
		}

		public BigDecimal getR59_month_end() {
			return r59_month_end;
		}

		public void setR59_month_end(BigDecimal r59_month_end) {
			this.r59_month_end = r59_month_end;
		}

		public BigDecimal getR59_average() {
			return r59_average;
		}

		public void setR59_average(BigDecimal r59_average) {
			this.r59_average = r59_average;
		}

		public String getR60_product() {
			return r60_product;
		}

		public void setR60_product(String r60_product) {
			this.r60_product = r60_product;
		}

		public String getR60_cross_ref() {
			return r60_cross_ref;
		}

		public void setR60_cross_ref(String r60_cross_ref) {
			this.r60_cross_ref = r60_cross_ref;
		}

		public BigDecimal getR60_month_end() {
			return r60_month_end;
		}

		public void setR60_month_end(BigDecimal r60_month_end) {
			this.r60_month_end = r60_month_end;
		}

		public BigDecimal getR60_average() {
			return r60_average;
		}

		public void setR60_average(BigDecimal r60_average) {
			this.r60_average = r60_average;
		}

		public String getR61_product() {
			return r61_product;
		}

		public void setR61_product(String r61_product) {
			this.r61_product = r61_product;
		}

		public String getR61_cross_ref() {
			return r61_cross_ref;
		}

		public void setR61_cross_ref(String r61_cross_ref) {
			this.r61_cross_ref = r61_cross_ref;
		}

		public BigDecimal getR61_month_end() {
			return r61_month_end;
		}

		public void setR61_month_end(BigDecimal r61_month_end) {
			this.r61_month_end = r61_month_end;
		}

		public BigDecimal getR61_average() {
			return r61_average;
		}

		public void setR61_average(BigDecimal r61_average) {
			this.r61_average = r61_average;
		}

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public BigDecimal getReport_version() {
			return report_version;
		}

		public void setReport_version(BigDecimal report_version) {
			this.report_version = report_version;
		}

		public String getReport_frequency() {
			return report_frequency;
		}

		public void setReport_frequency(String report_frequency) {
			this.report_frequency = report_frequency;
		}

		public String getReport_code() {
			return report_code;
		}

		public void setReport_code(String report_code) {
			this.report_code = report_code;
		}

		public String getReport_desc() {
			return report_desc;
		}

		public void setReport_desc(String report_desc) {
			this.report_desc = report_desc;
		}

		public String getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(String entity_flg) {
			this.entity_flg = entity_flg;
		}

		public String getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(String modify_flg) {
			this.modify_flg = modify_flg;
		}

		public String getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(String del_flg) {
			this.del_flg = del_flg;
		}

		public M_SFINP1_Summary_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}
	}

	// ------------------------------
	// Summary Manual Entity
	// ------------------------------
	public static class M_SFINP1_Summary_Manual_Entity {
		private BigDecimal R14_MONTH_END;
		private BigDecimal R34_MONTH_END;
		private BigDecimal R37_MONTH_END;
		private BigDecimal R39_MONTH_END;
		private BigDecimal R43_MONTH_END;
		private BigDecimal R42_MONTH_END;
		private BigDecimal R50_MONTH_END;
		private BigDecimal R51_MONTH_END;
		private BigDecimal R52_MONTH_END;
		private BigDecimal R57_MONTH_END;
		private BigDecimal R59_MONTH_END;
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date REPORT_DATE;
		private BigDecimal REPORT_VERSION;
		private String REPORT_FREQUENCY;
		private String REPORT_CODE;
		private String REPORT_DESC;
		private String ENTITY_FLG;
		private String MODIFY_FLG;
		private String DEL_FLG;

		public BigDecimal getR14_MONTH_END() {
			return R14_MONTH_END;
		}

		public void setR14_MONTH_END(BigDecimal r14_MONTH_END) {
			R14_MONTH_END = r14_MONTH_END;
		}

		public BigDecimal getR34_MONTH_END() {
			return R34_MONTH_END;
		}

		public void setR34_MONTH_END(BigDecimal r34_MONTH_END) {
			R34_MONTH_END = r34_MONTH_END;
		}

		public BigDecimal getR37_MONTH_END() {
			return R37_MONTH_END;
		}

		public void setR37_MONTH_END(BigDecimal r37_MONTH_END) {
			R37_MONTH_END = r37_MONTH_END;
		}

		public BigDecimal getR39_MONTH_END() {
			return R39_MONTH_END;
		}

		public void setR39_MONTH_END(BigDecimal r39_MONTH_END) {
			R39_MONTH_END = r39_MONTH_END;
		}

		public BigDecimal getR43_MONTH_END() {
			return R43_MONTH_END;
		}

		public void setR43_MONTH_END(BigDecimal r43_MONTH_END) {
			R43_MONTH_END = r43_MONTH_END;
		}

		public BigDecimal getR50_MONTH_END() {
			return R50_MONTH_END;
		}

		public void setR50_MONTH_END(BigDecimal r50_MONTH_END) {
			R50_MONTH_END = r50_MONTH_END;
		}

		public BigDecimal getR51_MONTH_END() {
			return R51_MONTH_END;
		}

		public void setR51_MONTH_END(BigDecimal r51_MONTH_END) {
			R51_MONTH_END = r51_MONTH_END;
		}

		public BigDecimal getR52_MONTH_END() {
			return R52_MONTH_END;
		}

		public void setR52_MONTH_END(BigDecimal r52_MONTH_END) {
			R52_MONTH_END = r52_MONTH_END;
		}

		public BigDecimal getR57_MONTH_END() {
			return R57_MONTH_END;
		}

		public void setR57_MONTH_END(BigDecimal r57_MONTH_END) {
			R57_MONTH_END = r57_MONTH_END;
		}

		public BigDecimal getR59_MONTH_END() {
			return R59_MONTH_END;
		}

		public void setR59_MONTH_END(BigDecimal r59_MONTH_END) {
			R59_MONTH_END = r59_MONTH_END;
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

		public BigDecimal getR42_MONTH_END() {
			return R42_MONTH_END;
		}

		public void setR42_MONTH_END(BigDecimal r42_MONTH_END) {
			R42_MONTH_END = r42_MONTH_END;
		}

		public M_SFINP1_Summary_Manual_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}
	}

	// ------------------------------
	// Detail Entity
	// ------------------------------
	public static class M_SFINP1_Detail_Entity {
		@Column(name = "CUST_ID", length = 100)
		private String custId;

		@Id
		@Column(name = "ACCT_NUMBER", length = 100)
		private String acctNumber;

		@Column(name = "ACCT_NAME", length = 100)
		private String acctName;

		@Column(name = "DATA_TYPE", length = 100)
		private String dataType;

		@Column(name = "ROW_ID", length = 100)
		private String rowId;

		@Column(name = "COLUMN_ID", length = 100)
		private String columnId;

		@Column(name = "REPORT_REMARKS", length = 100)
		private String reportRemarks;

		@Column(name = "MODIFICATION_REMARKS", length = 100)
		private String modificationRemarks;

		@Column(name = "DATA_ENTRY_VERSION", length = 100)
		private String dataEntryVersion;

		@Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 3)
		private BigDecimal acctBalanceInPula;

		@Column(name = "AVERAGE", precision = 24, scale = 3)
		private BigDecimal average;

		@Column(name = "REPORT_DATE")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date reportDate;

		@Column(name = "REPORT_NAME", length = 100)
		private String reportName;

		@Column(name = "CREATE_USER", length = 50)
		private String createUser;

		@Column(name = "CREATE_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date createTime;

		@Column(name = "MODIFY_USER", length = 50)
		private String modifyUser;

		@Column(name = "MODIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date modifyTime;

		@Column(name = "VERIFY_USER", length = 50)
		private String verifyUser;

		@Column(name = "VERIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date verifyTime;

		@Column(name = "ENTITY_FLG", length = 1)
		private String entityFlg;

		@Column(name = "MODIFY_FLG", length = 1)
		private String modifyFlg;

		@Column(name = "DEL_FLG", length = 1)
		private String delFlg;

		public String getCustId() {
			return custId;
		}

		public void setCustId(String custId) {
			this.custId = custId;
		}

		public String getAcctNumber() {
			return acctNumber;
		}

		public void setAcctNumber(String acctNumber) {
			this.acctNumber = acctNumber;
		}

		public String getAcctName() {
			return acctName;
		}

		public void setAcctName(String acctName) {
			this.acctName = acctName;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		public String getRowId() {
			return rowId;
		}

		public void setRowId(String rowId) {
			this.rowId = rowId;
		}

		public String getColumnId() {
			return columnId;
		}

		public void setColumnId(String columnId) {
			this.columnId = columnId;
		}

		public String getReportRemarks() {
			return reportRemarks;
		}

		public void setReportRemarks(String reportRemarks) {
			this.reportRemarks = reportRemarks;
		}

		public String getModificationRemarks() {
			return modificationRemarks;
		}

		public void setModificationRemarks(String modificationRemarks) {
			this.modificationRemarks = modificationRemarks;
		}

		public String getDataEntryVersion() {
			return dataEntryVersion;
		}

		public void setDataEntryVersion(String dataEntryVersion) {
			this.dataEntryVersion = dataEntryVersion;
		}

		public BigDecimal getAcctBalanceInPula() {
			return acctBalanceInPula;
		}

		public void setAcctBalanceInPula(BigDecimal acctBalanceInPula) {
			this.acctBalanceInPula = acctBalanceInPula;
		}

		public BigDecimal getAverage() {
			return average;
		}

		public void setAverage(BigDecimal average) {
			this.average = average;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
		}

		public String getCreateUser() {
			return createUser;
		}

		public void setCreateUser(String createUser) {
			this.createUser = createUser;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public String getModifyUser() {
			return modifyUser;
		}

		public void setModifyUser(String modifyUser) {
			this.modifyUser = modifyUser;
		}

		public Date getModifyTime() {
			return modifyTime;
		}

		public void setModifyTime(Date modifyTime) {
			this.modifyTime = modifyTime;
		}

		public String getVerifyUser() {
			return verifyUser;
		}

		public void setVerifyUser(String verifyUser) {
			this.verifyUser = verifyUser;
		}

		public Date getVerifyTime() {
			return verifyTime;
		}

		public void setVerifyTime(Date verifyTime) {
			this.verifyTime = verifyTime;
		}

		public String getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(String entityFlg) {
			this.entityFlg = entityFlg;
		}

		public String getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(String modifyFlg) {
			this.modifyFlg = modifyFlg;
		}

		public String getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(String delFlg) {
			this.delFlg = delFlg;
		}

		public M_SFINP1_Detail_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}
	}

	// ------------------------------
	// Archival Detail Entity
	// ------------------------------
	public static class M_SFINP1_Archival_Detail_Entity {
		@Column(name = "CUST_ID", length = 100)
		private String custId;

		@Id
		@Column(name = "ACCT_NUMBER", length = 100)
		private String acctNumber;

		@Column(name = "ACCT_NAME", length = 100)
		private String acctName;

		@Column(name = "DATA_TYPE", length = 100)
		private String dataType;

		@Column(name = "ROW_ID", length = 100)
		private String rowId;

		@Column(name = "COLUMN_ID", length = 100)
		private String columnId;

		@Column(name = "REPORT_REMARKS", length = 100)
		private String reportRemarks;

		@Column(name = "MODIFICATION_REMARKS", length = 100)
		private String modificationRemarks;

		@Column(name = "DATA_ENTRY_VERSION", length = 100)
		private String dataEntryVersion;

		@Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 3)
		private BigDecimal acctBalanceInPula;

		@Column(name = "AVERAGE", precision = 24, scale = 3)
		private BigDecimal average;

		@Column(name = "REPORT_DATE")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date reportDate;

		@Column(name = "REPORT_NAME", length = 100)
		private String reportName;

		@Column(name = "CREATE_USER", length = 50)
		private String createUser;

		@Column(name = "CREATE_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date createTime;

		@Column(name = "MODIFY_USER", length = 50)
		private String modifyUser;

		@Column(name = "MODIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date modifyTime;

		@Column(name = "VERIFY_USER", length = 50)
		private String verifyUser;

		@Column(name = "VERIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date verifyTime;

		@Column(name = "ENTITY_FLG", length = 1)
		private String entityFlg;

		@Column(name = "MODIFY_FLG", length = 1)
		private String modifyFlg;

		@Column(name = "DEL_FLG", length = 1)
		private String delFlg;

		public String getCustId() {
			return custId;
		}

		public void setCustId(String custId) {
			this.custId = custId;
		}

		public String getAcctNumber() {
			return acctNumber;
		}

		public void setAcctNumber(String acctNumber) {
			this.acctNumber = acctNumber;
		}

		public String getAcctName() {
			return acctName;
		}

		public void setAcctName(String acctName) {
			this.acctName = acctName;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		public String getRowId() {
			return rowId;
		}

		public void setRowId(String rowId) {
			this.rowId = rowId;
		}

		public String getColumnId() {
			return columnId;
		}

		public void setColumnId(String columnId) {
			this.columnId = columnId;
		}

		public String getReportRemarks() {
			return reportRemarks;
		}

		public void setReportRemarks(String reportRemarks) {
			this.reportRemarks = reportRemarks;
		}

		public String getModificationRemarks() {
			return modificationRemarks;
		}

		public void setModificationRemarks(String modificationRemarks) {
			this.modificationRemarks = modificationRemarks;
		}

		public String getDataEntryVersion() {
			return dataEntryVersion;
		}

		public void setDataEntryVersion(String dataEntryVersion) {
			this.dataEntryVersion = dataEntryVersion;
		}

		public BigDecimal getAcctBalanceInPula() {
			return acctBalanceInPula;
		}

		public void setAcctBalanceInPula(BigDecimal acctBalanceInPula) {
			this.acctBalanceInPula = acctBalanceInPula;
		}

		public BigDecimal getAverage() {
			return average;
		}

		public void setAverage(BigDecimal average) {
			this.average = average;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
		}

		public String getCreateUser() {
			return createUser;
		}

		public void setCreateUser(String createUser) {
			this.createUser = createUser;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public String getModifyUser() {
			return modifyUser;
		}

		public void setModifyUser(String modifyUser) {
			this.modifyUser = modifyUser;
		}

		public Date getModifyTime() {
			return modifyTime;
		}

		public void setModifyTime(Date modifyTime) {
			this.modifyTime = modifyTime;
		}

		public String getVerifyUser() {
			return verifyUser;
		}

		public void setVerifyUser(String verifyUser) {
			this.verifyUser = verifyUser;
		}

		public Date getVerifyTime() {
			return verifyTime;
		}

		public void setVerifyTime(Date verifyTime) {
			this.verifyTime = verifyTime;
		}

		public String getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(String entityFlg) {
			this.entityFlg = entityFlg;
		}

		public String getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(String modifyFlg) {
			this.modifyFlg = modifyFlg;
		}

		public String getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(String delFlg) {
			this.delFlg = delFlg;
		}

		public M_SFINP1_Archival_Detail_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}
	}

	// ------------------------------
	// Archival Summary Entity
	// ------------------------------
	public static class M_SFINP1_Archival_Summary_Entity {
		private String r10_product;
		private String r10_cross_ref;
		private BigDecimal r10_month_end;
		private BigDecimal r10_average;
		private String r11_product;
		private String r11_cross_ref;
		private BigDecimal r11_month_end;
		private BigDecimal r11_average;
		private String r12_product;
		private String r12_cross_ref;
		private BigDecimal r12_month_end;
		private BigDecimal r12_average;
		private String r13_product;
		private String r13_cross_ref;
		private BigDecimal r13_month_end;
		private BigDecimal r13_average;
		private String r14_product;
		private String r14_cross_ref;
		private BigDecimal r14_month_end;
		private BigDecimal r14_average;
		private String r15_product;
		private String r15_cross_ref;
		private BigDecimal r15_month_end;
		private BigDecimal r15_average;
		private String r16_product;
		private String r16_cross_ref;
		private BigDecimal r16_month_end;
		private BigDecimal r16_average;
		private String r17_product;
		private String r17_cross_ref;
		private BigDecimal r17_month_end;
		private BigDecimal r17_average;
		private String r18_product;
		private String r18_cross_ref;
		private BigDecimal r18_month_end;
		private BigDecimal r18_average;
		private String r19_product;
		private String r19_cross_ref;
		private BigDecimal r19_month_end;
		private BigDecimal r19_average;
		private String r20_product;
		private String r20_cross_ref;
		private BigDecimal r20_month_end;
		private BigDecimal r20_average;
		private String r21_product;
		private String r21_cross_ref;
		private BigDecimal r21_month_end;
		private BigDecimal r21_average;
		private String r22_product;
		private String r22_cross_ref;
		private BigDecimal r22_month_end;
		private BigDecimal r22_average;
		private String r23_product;
		private String r23_cross_ref;
		private BigDecimal r23_month_end;
		private BigDecimal r23_average;
		private String r24_product;
		private String r24_cross_ref;
		private BigDecimal r24_month_end;
		private BigDecimal r24_average;
		private String r25_product;
		private String r25_cross_ref;
		private BigDecimal r25_month_end;
		private BigDecimal r25_average;
		private String r26_product;
		private String r26_cross_ref;
		private BigDecimal r26_month_end;
		private BigDecimal r26_average;
		private String r27_product;
		private String r27_cross_ref;
		private BigDecimal r27_month_end;
		private BigDecimal r27_average;
		private String r28_product;
		private String r28_cross_ref;
		private BigDecimal r28_month_end;
		private BigDecimal r28_average;
		private String r29_product;
		private String r29_cross_ref;
		private BigDecimal r29_month_end;
		private BigDecimal r29_average;
		private String r30_product;
		private String r30_cross_ref;
		private BigDecimal r30_month_end;
		private BigDecimal r30_average;
		private String r31_product;
		private String r31_cross_ref;
		private BigDecimal r31_month_end;
		private BigDecimal r31_average;
		private String r32_product;
		private String r32_cross_ref;
		private BigDecimal r32_month_end;
		private BigDecimal r32_average;
		private String r33_product;
		private String r33_cross_ref;
		private BigDecimal r33_month_end;
		private BigDecimal r33_average;
		private String r34_product;
		private String r34_cross_ref;
		private BigDecimal r34_month_end;
		private BigDecimal r34_average;
		private String r35_product;
		private String r35_cross_ref;
		private BigDecimal r35_month_end;
		private BigDecimal r35_average;
		private String r36_product;
		private String r36_cross_ref;
		private BigDecimal r36_month_end;
		private BigDecimal r36_average;
		private String r37_product;
		private String r37_cross_ref;
		private BigDecimal r37_month_end;
		private BigDecimal r37_average;
		private String r38_product;
		private String r38_cross_ref;
		private BigDecimal r38_month_end;
		private BigDecimal r38_average;
		private String r39_product;
		private String r39_cross_ref;
		private BigDecimal r39_month_end;
		private BigDecimal r39_average;
		private String r40_product;
		private String r40_cross_ref;
		private BigDecimal r40_month_end;
		private BigDecimal r40_average;
		private String r41_product;
		private String r41_cross_ref;
		private BigDecimal r41_month_end;
		private BigDecimal r41_average;
		private String r42_product;
		private String r42_cross_ref;
		private BigDecimal r42_month_end;
		private BigDecimal r42_average;
		private String r43_product;
		private String r43_cross_ref;
		private BigDecimal r43_month_end;
		private BigDecimal r43_average;
		private String r44_product;
		private String r44_cross_ref;
		private BigDecimal r44_month_end;
		private BigDecimal r44_average;
		private String r45_product;
		private String r45_cross_ref;
		private BigDecimal r45_month_end;
		private BigDecimal r45_average;
		private String r46_product;
		private String r46_cross_ref;
		private BigDecimal r46_month_end;
		private BigDecimal r46_average;
		private String r47_product;
		private String r47_cross_ref;
		private BigDecimal r47_month_end;
		private BigDecimal r47_average;
		private String r48_product;
		private String r48_cross_ref;
		private BigDecimal r48_month_end;
		private BigDecimal r48_average;
		private String r49_product;
		private String r49_cross_ref;
		private BigDecimal r49_month_end;
		private BigDecimal r49_average;
		private String r50_product;
		private String r50_cross_ref;
		private BigDecimal r50_month_end;
		private BigDecimal r50_average;
		private String r51_product;
		private String r51_cross_ref;
		private BigDecimal r51_month_end;
		private BigDecimal r51_average;
		private String r52_product;
		private String r52_cross_ref;
		private BigDecimal r52_month_end;
		private BigDecimal r52_average;
		private String r53_product;
		private String r53_cross_ref;
		private BigDecimal r53_month_end;
		private BigDecimal r53_average;
		private String r54_product;
		private String r54_cross_ref;
		private BigDecimal r54_month_end;
		private BigDecimal r54_average;
		private String r55_product;
		private String r55_cross_ref;
		private BigDecimal r55_month_end;
		private BigDecimal r55_average;
		private String r56_product;
		private String r56_cross_ref;
		private BigDecimal r56_month_end;
		private BigDecimal r56_average;
		private String r57_product;
		private String r57_cross_ref;
		private BigDecimal r57_month_end;
		private BigDecimal r57_average;
		private String r58_product;
		private String r58_cross_ref;
		private BigDecimal r58_month_end;
		private BigDecimal r58_average;
		private String r59_product;
		private String r59_cross_ref;
		private BigDecimal r59_month_end;
		private BigDecimal r59_average;
		private String r60_product;
		private String r60_cross_ref;
		private BigDecimal r60_month_end;
		private BigDecimal r60_average;
		private String r61_product;
		private String r61_cross_ref;
		private BigDecimal r61_month_end;
		private BigDecimal r61_average;
		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date reportDate;

		@Id
		@Column(name = "REPORT_VERSION")
		private BigDecimal reportVersion;

		@Column(name = "REPORT_RESUBDATE")
		@Temporal(TemporalType.TIMESTAMP)
		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
		}

		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String r10_product) {
			this.r10_product = r10_product;
		}

		public String getR10_cross_ref() {
			return r10_cross_ref;
		}

		public void setR10_cross_ref(String r10_cross_ref) {
			this.r10_cross_ref = r10_cross_ref;
		}

		public BigDecimal getR10_month_end() {
			return r10_month_end;
		}

		public void setR10_month_end(BigDecimal r10_month_end) {
			this.r10_month_end = r10_month_end;
		}

		public BigDecimal getR10_average() {
			return r10_average;
		}

		public void setR10_average(BigDecimal r10_average) {
			this.r10_average = r10_average;
		}

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public String getR11_cross_ref() {
			return r11_cross_ref;
		}

		public void setR11_cross_ref(String r11_cross_ref) {
			this.r11_cross_ref = r11_cross_ref;
		}

		public BigDecimal getR11_month_end() {
			return r11_month_end;
		}

		public void setR11_month_end(BigDecimal r11_month_end) {
			this.r11_month_end = r11_month_end;
		}

		public BigDecimal getR11_average() {
			return r11_average;
		}

		public void setR11_average(BigDecimal r11_average) {
			this.r11_average = r11_average;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public String getR12_cross_ref() {
			return r12_cross_ref;
		}

		public void setR12_cross_ref(String r12_cross_ref) {
			this.r12_cross_ref = r12_cross_ref;
		}

		public BigDecimal getR12_month_end() {
			return r12_month_end;
		}

		public void setR12_month_end(BigDecimal r12_month_end) {
			this.r12_month_end = r12_month_end;
		}

		public BigDecimal getR12_average() {
			return r12_average;
		}

		public void setR12_average(BigDecimal r12_average) {
			this.r12_average = r12_average;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public String getR13_cross_ref() {
			return r13_cross_ref;
		}

		public void setR13_cross_ref(String r13_cross_ref) {
			this.r13_cross_ref = r13_cross_ref;
		}

		public BigDecimal getR13_month_end() {
			return r13_month_end;
		}

		public void setR13_month_end(BigDecimal r13_month_end) {
			this.r13_month_end = r13_month_end;
		}

		public BigDecimal getR13_average() {
			return r13_average;
		}

		public void setR13_average(BigDecimal r13_average) {
			this.r13_average = r13_average;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public String getR14_cross_ref() {
			return r14_cross_ref;
		}

		public void setR14_cross_ref(String r14_cross_ref) {
			this.r14_cross_ref = r14_cross_ref;
		}

		public BigDecimal getR14_month_end() {
			return r14_month_end;
		}

		public void setR14_month_end(BigDecimal r14_month_end) {
			this.r14_month_end = r14_month_end;
		}

		public BigDecimal getR14_average() {
			return r14_average;
		}

		public void setR14_average(BigDecimal r14_average) {
			this.r14_average = r14_average;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public String getR15_cross_ref() {
			return r15_cross_ref;
		}

		public void setR15_cross_ref(String r15_cross_ref) {
			this.r15_cross_ref = r15_cross_ref;
		}

		public BigDecimal getR15_month_end() {
			return r15_month_end;
		}

		public void setR15_month_end(BigDecimal r15_month_end) {
			this.r15_month_end = r15_month_end;
		}

		public BigDecimal getR15_average() {
			return r15_average;
		}

		public void setR15_average(BigDecimal r15_average) {
			this.r15_average = r15_average;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public String getR16_cross_ref() {
			return r16_cross_ref;
		}

		public void setR16_cross_ref(String r16_cross_ref) {
			this.r16_cross_ref = r16_cross_ref;
		}

		public BigDecimal getR16_month_end() {
			return r16_month_end;
		}

		public void setR16_month_end(BigDecimal r16_month_end) {
			this.r16_month_end = r16_month_end;
		}

		public BigDecimal getR16_average() {
			return r16_average;
		}

		public void setR16_average(BigDecimal r16_average) {
			this.r16_average = r16_average;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public String getR17_cross_ref() {
			return r17_cross_ref;
		}

		public void setR17_cross_ref(String r17_cross_ref) {
			this.r17_cross_ref = r17_cross_ref;
		}

		public BigDecimal getR17_month_end() {
			return r17_month_end;
		}

		public void setR17_month_end(BigDecimal r17_month_end) {
			this.r17_month_end = r17_month_end;
		}

		public BigDecimal getR17_average() {
			return r17_average;
		}

		public void setR17_average(BigDecimal r17_average) {
			this.r17_average = r17_average;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public String getR18_cross_ref() {
			return r18_cross_ref;
		}

		public void setR18_cross_ref(String r18_cross_ref) {
			this.r18_cross_ref = r18_cross_ref;
		}

		public BigDecimal getR18_month_end() {
			return r18_month_end;
		}

		public void setR18_month_end(BigDecimal r18_month_end) {
			this.r18_month_end = r18_month_end;
		}

		public BigDecimal getR18_average() {
			return r18_average;
		}

		public void setR18_average(BigDecimal r18_average) {
			this.r18_average = r18_average;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public String getR19_cross_ref() {
			return r19_cross_ref;
		}

		public void setR19_cross_ref(String r19_cross_ref) {
			this.r19_cross_ref = r19_cross_ref;
		}

		public BigDecimal getR19_month_end() {
			return r19_month_end;
		}

		public void setR19_month_end(BigDecimal r19_month_end) {
			this.r19_month_end = r19_month_end;
		}

		public BigDecimal getR19_average() {
			return r19_average;
		}

		public void setR19_average(BigDecimal r19_average) {
			this.r19_average = r19_average;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public String getR20_cross_ref() {
			return r20_cross_ref;
		}

		public void setR20_cross_ref(String r20_cross_ref) {
			this.r20_cross_ref = r20_cross_ref;
		}

		public BigDecimal getR20_month_end() {
			return r20_month_end;
		}

		public void setR20_month_end(BigDecimal r20_month_end) {
			this.r20_month_end = r20_month_end;
		}

		public BigDecimal getR20_average() {
			return r20_average;
		}

		public void setR20_average(BigDecimal r20_average) {
			this.r20_average = r20_average;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public String getR21_cross_ref() {
			return r21_cross_ref;
		}

		public void setR21_cross_ref(String r21_cross_ref) {
			this.r21_cross_ref = r21_cross_ref;
		}

		public BigDecimal getR21_month_end() {
			return r21_month_end;
		}

		public void setR21_month_end(BigDecimal r21_month_end) {
			this.r21_month_end = r21_month_end;
		}

		public BigDecimal getR21_average() {
			return r21_average;
		}

		public void setR21_average(BigDecimal r21_average) {
			this.r21_average = r21_average;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public String getR22_cross_ref() {
			return r22_cross_ref;
		}

		public void setR22_cross_ref(String r22_cross_ref) {
			this.r22_cross_ref = r22_cross_ref;
		}

		public BigDecimal getR22_month_end() {
			return r22_month_end;
		}

		public void setR22_month_end(BigDecimal r22_month_end) {
			this.r22_month_end = r22_month_end;
		}

		public BigDecimal getR22_average() {
			return r22_average;
		}

		public void setR22_average(BigDecimal r22_average) {
			this.r22_average = r22_average;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public String getR23_cross_ref() {
			return r23_cross_ref;
		}

		public void setR23_cross_ref(String r23_cross_ref) {
			this.r23_cross_ref = r23_cross_ref;
		}

		public BigDecimal getR23_month_end() {
			return r23_month_end;
		}

		public void setR23_month_end(BigDecimal r23_month_end) {
			this.r23_month_end = r23_month_end;
		}

		public BigDecimal getR23_average() {
			return r23_average;
		}

		public void setR23_average(BigDecimal r23_average) {
			this.r23_average = r23_average;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public String getR24_cross_ref() {
			return r24_cross_ref;
		}

		public void setR24_cross_ref(String r24_cross_ref) {
			this.r24_cross_ref = r24_cross_ref;
		}

		public BigDecimal getR24_month_end() {
			return r24_month_end;
		}

		public void setR24_month_end(BigDecimal r24_month_end) {
			this.r24_month_end = r24_month_end;
		}

		public BigDecimal getR24_average() {
			return r24_average;
		}

		public void setR24_average(BigDecimal r24_average) {
			this.r24_average = r24_average;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public String getR25_cross_ref() {
			return r25_cross_ref;
		}

		public void setR25_cross_ref(String r25_cross_ref) {
			this.r25_cross_ref = r25_cross_ref;
		}

		public BigDecimal getR25_month_end() {
			return r25_month_end;
		}

		public void setR25_month_end(BigDecimal r25_month_end) {
			this.r25_month_end = r25_month_end;
		}

		public BigDecimal getR25_average() {
			return r25_average;
		}

		public void setR25_average(BigDecimal r25_average) {
			this.r25_average = r25_average;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public String getR26_cross_ref() {
			return r26_cross_ref;
		}

		public void setR26_cross_ref(String r26_cross_ref) {
			this.r26_cross_ref = r26_cross_ref;
		}

		public BigDecimal getR26_month_end() {
			return r26_month_end;
		}

		public void setR26_month_end(BigDecimal r26_month_end) {
			this.r26_month_end = r26_month_end;
		}

		public BigDecimal getR26_average() {
			return r26_average;
		}

		public void setR26_average(BigDecimal r26_average) {
			this.r26_average = r26_average;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public String getR27_cross_ref() {
			return r27_cross_ref;
		}

		public void setR27_cross_ref(String r27_cross_ref) {
			this.r27_cross_ref = r27_cross_ref;
		}

		public BigDecimal getR27_month_end() {
			return r27_month_end;
		}

		public void setR27_month_end(BigDecimal r27_month_end) {
			this.r27_month_end = r27_month_end;
		}

		public BigDecimal getR27_average() {
			return r27_average;
		}

		public void setR27_average(BigDecimal r27_average) {
			this.r27_average = r27_average;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public String getR28_cross_ref() {
			return r28_cross_ref;
		}

		public void setR28_cross_ref(String r28_cross_ref) {
			this.r28_cross_ref = r28_cross_ref;
		}

		public BigDecimal getR28_month_end() {
			return r28_month_end;
		}

		public void setR28_month_end(BigDecimal r28_month_end) {
			this.r28_month_end = r28_month_end;
		}

		public BigDecimal getR28_average() {
			return r28_average;
		}

		public void setR28_average(BigDecimal r28_average) {
			this.r28_average = r28_average;
		}

		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public String getR29_cross_ref() {
			return r29_cross_ref;
		}

		public void setR29_cross_ref(String r29_cross_ref) {
			this.r29_cross_ref = r29_cross_ref;
		}

		public BigDecimal getR29_month_end() {
			return r29_month_end;
		}

		public void setR29_month_end(BigDecimal r29_month_end) {
			this.r29_month_end = r29_month_end;
		}

		public BigDecimal getR29_average() {
			return r29_average;
		}

		public void setR29_average(BigDecimal r29_average) {
			this.r29_average = r29_average;
		}

		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public String getR30_cross_ref() {
			return r30_cross_ref;
		}

		public void setR30_cross_ref(String r30_cross_ref) {
			this.r30_cross_ref = r30_cross_ref;
		}

		public BigDecimal getR30_month_end() {
			return r30_month_end;
		}

		public void setR30_month_end(BigDecimal r30_month_end) {
			this.r30_month_end = r30_month_end;
		}

		public BigDecimal getR30_average() {
			return r30_average;
		}

		public void setR30_average(BigDecimal r30_average) {
			this.r30_average = r30_average;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public String getR31_cross_ref() {
			return r31_cross_ref;
		}

		public void setR31_cross_ref(String r31_cross_ref) {
			this.r31_cross_ref = r31_cross_ref;
		}

		public BigDecimal getR31_month_end() {
			return r31_month_end;
		}

		public void setR31_month_end(BigDecimal r31_month_end) {
			this.r31_month_end = r31_month_end;
		}

		public BigDecimal getR31_average() {
			return r31_average;
		}

		public void setR31_average(BigDecimal r31_average) {
			this.r31_average = r31_average;
		}

		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String r32_product) {
			this.r32_product = r32_product;
		}

		public String getR32_cross_ref() {
			return r32_cross_ref;
		}

		public void setR32_cross_ref(String r32_cross_ref) {
			this.r32_cross_ref = r32_cross_ref;
		}

		public BigDecimal getR32_month_end() {
			return r32_month_end;
		}

		public void setR32_month_end(BigDecimal r32_month_end) {
			this.r32_month_end = r32_month_end;
		}

		public BigDecimal getR32_average() {
			return r32_average;
		}

		public void setR32_average(BigDecimal r32_average) {
			this.r32_average = r32_average;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public String getR33_cross_ref() {
			return r33_cross_ref;
		}

		public void setR33_cross_ref(String r33_cross_ref) {
			this.r33_cross_ref = r33_cross_ref;
		}

		public BigDecimal getR33_month_end() {
			return r33_month_end;
		}

		public void setR33_month_end(BigDecimal r33_month_end) {
			this.r33_month_end = r33_month_end;
		}

		public BigDecimal getR33_average() {
			return r33_average;
		}

		public void setR33_average(BigDecimal r33_average) {
			this.r33_average = r33_average;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public String getR34_cross_ref() {
			return r34_cross_ref;
		}

		public void setR34_cross_ref(String r34_cross_ref) {
			this.r34_cross_ref = r34_cross_ref;
		}

		public BigDecimal getR34_month_end() {
			return r34_month_end;
		}

		public void setR34_month_end(BigDecimal r34_month_end) {
			this.r34_month_end = r34_month_end;
		}

		public BigDecimal getR34_average() {
			return r34_average;
		}

		public void setR34_average(BigDecimal r34_average) {
			this.r34_average = r34_average;
		}

		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String r35_product) {
			this.r35_product = r35_product;
		}

		public String getR35_cross_ref() {
			return r35_cross_ref;
		}

		public void setR35_cross_ref(String r35_cross_ref) {
			this.r35_cross_ref = r35_cross_ref;
		}

		public BigDecimal getR35_month_end() {
			return r35_month_end;
		}

		public void setR35_month_end(BigDecimal r35_month_end) {
			this.r35_month_end = r35_month_end;
		}

		public BigDecimal getR35_average() {
			return r35_average;
		}

		public void setR35_average(BigDecimal r35_average) {
			this.r35_average = r35_average;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public String getR36_cross_ref() {
			return r36_cross_ref;
		}

		public void setR36_cross_ref(String r36_cross_ref) {
			this.r36_cross_ref = r36_cross_ref;
		}

		public BigDecimal getR36_month_end() {
			return r36_month_end;
		}

		public void setR36_month_end(BigDecimal r36_month_end) {
			this.r36_month_end = r36_month_end;
		}

		public BigDecimal getR36_average() {
			return r36_average;
		}

		public void setR36_average(BigDecimal r36_average) {
			this.r36_average = r36_average;
		}

		public String getR37_product() {
			return r37_product;
		}

		public void setR37_product(String r37_product) {
			this.r37_product = r37_product;
		}

		public String getR37_cross_ref() {
			return r37_cross_ref;
		}

		public void setR37_cross_ref(String r37_cross_ref) {
			this.r37_cross_ref = r37_cross_ref;
		}

		public BigDecimal getR37_month_end() {
			return r37_month_end;
		}

		public void setR37_month_end(BigDecimal r37_month_end) {
			this.r37_month_end = r37_month_end;
		}

		public BigDecimal getR37_average() {
			return r37_average;
		}

		public void setR37_average(BigDecimal r37_average) {
			this.r37_average = r37_average;
		}

		public String getR38_product() {
			return r38_product;
		}

		public void setR38_product(String r38_product) {
			this.r38_product = r38_product;
		}

		public String getR38_cross_ref() {
			return r38_cross_ref;
		}

		public void setR38_cross_ref(String r38_cross_ref) {
			this.r38_cross_ref = r38_cross_ref;
		}

		public BigDecimal getR38_month_end() {
			return r38_month_end;
		}

		public void setR38_month_end(BigDecimal r38_month_end) {
			this.r38_month_end = r38_month_end;
		}

		public BigDecimal getR38_average() {
			return r38_average;
		}

		public void setR38_average(BigDecimal r38_average) {
			this.r38_average = r38_average;
		}

		public String getR39_product() {
			return r39_product;
		}

		public void setR39_product(String r39_product) {
			this.r39_product = r39_product;
		}

		public String getR39_cross_ref() {
			return r39_cross_ref;
		}

		public void setR39_cross_ref(String r39_cross_ref) {
			this.r39_cross_ref = r39_cross_ref;
		}

		public BigDecimal getR39_month_end() {
			return r39_month_end;
		}

		public void setR39_month_end(BigDecimal r39_month_end) {
			this.r39_month_end = r39_month_end;
		}

		public BigDecimal getR39_average() {
			return r39_average;
		}

		public void setR39_average(BigDecimal r39_average) {
			this.r39_average = r39_average;
		}

		public String getR40_product() {
			return r40_product;
		}

		public void setR40_product(String r40_product) {
			this.r40_product = r40_product;
		}

		public String getR40_cross_ref() {
			return r40_cross_ref;
		}

		public void setR40_cross_ref(String r40_cross_ref) {
			this.r40_cross_ref = r40_cross_ref;
		}

		public BigDecimal getR40_month_end() {
			return r40_month_end;
		}

		public void setR40_month_end(BigDecimal r40_month_end) {
			this.r40_month_end = r40_month_end;
		}

		public BigDecimal getR40_average() {
			return r40_average;
		}

		public void setR40_average(BigDecimal r40_average) {
			this.r40_average = r40_average;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public String getR41_cross_ref() {
			return r41_cross_ref;
		}

		public void setR41_cross_ref(String r41_cross_ref) {
			this.r41_cross_ref = r41_cross_ref;
		}

		public BigDecimal getR41_month_end() {
			return r41_month_end;
		}

		public void setR41_month_end(BigDecimal r41_month_end) {
			this.r41_month_end = r41_month_end;
		}

		public BigDecimal getR41_average() {
			return r41_average;
		}

		public void setR41_average(BigDecimal r41_average) {
			this.r41_average = r41_average;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public String getR42_cross_ref() {
			return r42_cross_ref;
		}

		public void setR42_cross_ref(String r42_cross_ref) {
			this.r42_cross_ref = r42_cross_ref;
		}

		public BigDecimal getR42_month_end() {
			return r42_month_end;
		}

		public void setR42_month_end(BigDecimal r42_month_end) {
			this.r42_month_end = r42_month_end;
		}

		public BigDecimal getR42_average() {
			return r42_average;
		}

		public void setR42_average(BigDecimal r42_average) {
			this.r42_average = r42_average;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public String getR43_cross_ref() {
			return r43_cross_ref;
		}

		public void setR43_cross_ref(String r43_cross_ref) {
			this.r43_cross_ref = r43_cross_ref;
		}

		public BigDecimal getR43_month_end() {
			return r43_month_end;
		}

		public void setR43_month_end(BigDecimal r43_month_end) {
			this.r43_month_end = r43_month_end;
		}

		public BigDecimal getR43_average() {
			return r43_average;
		}

		public void setR43_average(BigDecimal r43_average) {
			this.r43_average = r43_average;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public String getR44_cross_ref() {
			return r44_cross_ref;
		}

		public void setR44_cross_ref(String r44_cross_ref) {
			this.r44_cross_ref = r44_cross_ref;
		}

		public BigDecimal getR44_month_end() {
			return r44_month_end;
		}

		public void setR44_month_end(BigDecimal r44_month_end) {
			this.r44_month_end = r44_month_end;
		}

		public BigDecimal getR44_average() {
			return r44_average;
		}

		public void setR44_average(BigDecimal r44_average) {
			this.r44_average = r44_average;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public String getR45_cross_ref() {
			return r45_cross_ref;
		}

		public void setR45_cross_ref(String r45_cross_ref) {
			this.r45_cross_ref = r45_cross_ref;
		}

		public BigDecimal getR45_month_end() {
			return r45_month_end;
		}

		public void setR45_month_end(BigDecimal r45_month_end) {
			this.r45_month_end = r45_month_end;
		}

		public BigDecimal getR45_average() {
			return r45_average;
		}

		public void setR45_average(BigDecimal r45_average) {
			this.r45_average = r45_average;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public String getR46_cross_ref() {
			return r46_cross_ref;
		}

		public void setR46_cross_ref(String r46_cross_ref) {
			this.r46_cross_ref = r46_cross_ref;
		}

		public BigDecimal getR46_month_end() {
			return r46_month_end;
		}

		public void setR46_month_end(BigDecimal r46_month_end) {
			this.r46_month_end = r46_month_end;
		}

		public BigDecimal getR46_average() {
			return r46_average;
		}

		public void setR46_average(BigDecimal r46_average) {
			this.r46_average = r46_average;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public String getR47_cross_ref() {
			return r47_cross_ref;
		}

		public void setR47_cross_ref(String r47_cross_ref) {
			this.r47_cross_ref = r47_cross_ref;
		}

		public BigDecimal getR47_month_end() {
			return r47_month_end;
		}

		public void setR47_month_end(BigDecimal r47_month_end) {
			this.r47_month_end = r47_month_end;
		}

		public BigDecimal getR47_average() {
			return r47_average;
		}

		public void setR47_average(BigDecimal r47_average) {
			this.r47_average = r47_average;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public String getR48_cross_ref() {
			return r48_cross_ref;
		}

		public void setR48_cross_ref(String r48_cross_ref) {
			this.r48_cross_ref = r48_cross_ref;
		}

		public BigDecimal getR48_month_end() {
			return r48_month_end;
		}

		public void setR48_month_end(BigDecimal r48_month_end) {
			this.r48_month_end = r48_month_end;
		}

		public BigDecimal getR48_average() {
			return r48_average;
		}

		public void setR48_average(BigDecimal r48_average) {
			this.r48_average = r48_average;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public String getR49_cross_ref() {
			return r49_cross_ref;
		}

		public void setR49_cross_ref(String r49_cross_ref) {
			this.r49_cross_ref = r49_cross_ref;
		}

		public BigDecimal getR49_month_end() {
			return r49_month_end;
		}

		public void setR49_month_end(BigDecimal r49_month_end) {
			this.r49_month_end = r49_month_end;
		}

		public BigDecimal getR49_average() {
			return r49_average;
		}

		public void setR49_average(BigDecimal r49_average) {
			this.r49_average = r49_average;
		}

		public String getR50_product() {
			return r50_product;
		}

		public void setR50_product(String r50_product) {
			this.r50_product = r50_product;
		}

		public String getR50_cross_ref() {
			return r50_cross_ref;
		}

		public void setR50_cross_ref(String r50_cross_ref) {
			this.r50_cross_ref = r50_cross_ref;
		}

		public BigDecimal getR50_month_end() {
			return r50_month_end;
		}

		public void setR50_month_end(BigDecimal r50_month_end) {
			this.r50_month_end = r50_month_end;
		}

		public BigDecimal getR50_average() {
			return r50_average;
		}

		public void setR50_average(BigDecimal r50_average) {
			this.r50_average = r50_average;
		}

		public String getR51_product() {
			return r51_product;
		}

		public void setR51_product(String r51_product) {
			this.r51_product = r51_product;
		}

		public String getR51_cross_ref() {
			return r51_cross_ref;
		}

		public void setR51_cross_ref(String r51_cross_ref) {
			this.r51_cross_ref = r51_cross_ref;
		}

		public BigDecimal getR51_month_end() {
			return r51_month_end;
		}

		public void setR51_month_end(BigDecimal r51_month_end) {
			this.r51_month_end = r51_month_end;
		}

		public BigDecimal getR51_average() {
			return r51_average;
		}

		public void setR51_average(BigDecimal r51_average) {
			this.r51_average = r51_average;
		}

		public String getR52_product() {
			return r52_product;
		}

		public void setR52_product(String r52_product) {
			this.r52_product = r52_product;
		}

		public String getR52_cross_ref() {
			return r52_cross_ref;
		}

		public void setR52_cross_ref(String r52_cross_ref) {
			this.r52_cross_ref = r52_cross_ref;
		}

		public BigDecimal getR52_month_end() {
			return r52_month_end;
		}

		public void setR52_month_end(BigDecimal r52_month_end) {
			this.r52_month_end = r52_month_end;
		}

		public BigDecimal getR52_average() {
			return r52_average;
		}

		public void setR52_average(BigDecimal r52_average) {
			this.r52_average = r52_average;
		}

		public String getR53_product() {
			return r53_product;
		}

		public void setR53_product(String r53_product) {
			this.r53_product = r53_product;
		}

		public String getR53_cross_ref() {
			return r53_cross_ref;
		}

		public void setR53_cross_ref(String r53_cross_ref) {
			this.r53_cross_ref = r53_cross_ref;
		}

		public BigDecimal getR53_month_end() {
			return r53_month_end;
		}

		public void setR53_month_end(BigDecimal r53_month_end) {
			this.r53_month_end = r53_month_end;
		}

		public BigDecimal getR53_average() {
			return r53_average;
		}

		public void setR53_average(BigDecimal r53_average) {
			this.r53_average = r53_average;
		}

		public String getR54_product() {
			return r54_product;
		}

		public void setR54_product(String r54_product) {
			this.r54_product = r54_product;
		}

		public String getR54_cross_ref() {
			return r54_cross_ref;
		}

		public void setR54_cross_ref(String r54_cross_ref) {
			this.r54_cross_ref = r54_cross_ref;
		}

		public BigDecimal getR54_month_end() {
			return r54_month_end;
		}

		public void setR54_month_end(BigDecimal r54_month_end) {
			this.r54_month_end = r54_month_end;
		}

		public BigDecimal getR54_average() {
			return r54_average;
		}

		public void setR54_average(BigDecimal r54_average) {
			this.r54_average = r54_average;
		}

		public String getR55_product() {
			return r55_product;
		}

		public void setR55_product(String r55_product) {
			this.r55_product = r55_product;
		}

		public String getR55_cross_ref() {
			return r55_cross_ref;
		}

		public void setR55_cross_ref(String r55_cross_ref) {
			this.r55_cross_ref = r55_cross_ref;
		}

		public BigDecimal getR55_month_end() {
			return r55_month_end;
		}

		public void setR55_month_end(BigDecimal r55_month_end) {
			this.r55_month_end = r55_month_end;
		}

		public BigDecimal getR55_average() {
			return r55_average;
		}

		public void setR55_average(BigDecimal r55_average) {
			this.r55_average = r55_average;
		}

		public String getR56_product() {
			return r56_product;
		}

		public void setR56_product(String r56_product) {
			this.r56_product = r56_product;
		}

		public String getR56_cross_ref() {
			return r56_cross_ref;
		}

		public void setR56_cross_ref(String r56_cross_ref) {
			this.r56_cross_ref = r56_cross_ref;
		}

		public BigDecimal getR56_month_end() {
			return r56_month_end;
		}

		public void setR56_month_end(BigDecimal r56_month_end) {
			this.r56_month_end = r56_month_end;
		}

		public BigDecimal getR56_average() {
			return r56_average;
		}

		public void setR56_average(BigDecimal r56_average) {
			this.r56_average = r56_average;
		}

		public String getR57_product() {
			return r57_product;
		}

		public void setR57_product(String r57_product) {
			this.r57_product = r57_product;
		}

		public String getR57_cross_ref() {
			return r57_cross_ref;
		}

		public void setR57_cross_ref(String r57_cross_ref) {
			this.r57_cross_ref = r57_cross_ref;
		}

		public BigDecimal getR57_month_end() {
			return r57_month_end;
		}

		public void setR57_month_end(BigDecimal r57_month_end) {
			this.r57_month_end = r57_month_end;
		}

		public BigDecimal getR57_average() {
			return r57_average;
		}

		public void setR57_average(BigDecimal r57_average) {
			this.r57_average = r57_average;
		}

		public String getR58_product() {
			return r58_product;
		}

		public void setR58_product(String r58_product) {
			this.r58_product = r58_product;
		}

		public String getR58_cross_ref() {
			return r58_cross_ref;
		}

		public void setR58_cross_ref(String r58_cross_ref) {
			this.r58_cross_ref = r58_cross_ref;
		}

		public BigDecimal getR58_month_end() {
			return r58_month_end;
		}

		public void setR58_month_end(BigDecimal r58_month_end) {
			this.r58_month_end = r58_month_end;
		}

		public BigDecimal getR58_average() {
			return r58_average;
		}

		public void setR58_average(BigDecimal r58_average) {
			this.r58_average = r58_average;
		}

		public String getR59_product() {
			return r59_product;
		}

		public void setR59_product(String r59_product) {
			this.r59_product = r59_product;
		}

		public String getR59_cross_ref() {
			return r59_cross_ref;
		}

		public void setR59_cross_ref(String r59_cross_ref) {
			this.r59_cross_ref = r59_cross_ref;
		}

		public BigDecimal getR59_month_end() {
			return r59_month_end;
		}

		public void setR59_month_end(BigDecimal r59_month_end) {
			this.r59_month_end = r59_month_end;
		}

		public BigDecimal getR59_average() {
			return r59_average;
		}

		public void setR59_average(BigDecimal r59_average) {
			this.r59_average = r59_average;
		}

		public String getR60_product() {
			return r60_product;
		}

		public void setR60_product(String r60_product) {
			this.r60_product = r60_product;
		}

		public String getR60_cross_ref() {
			return r60_cross_ref;
		}

		public void setR60_cross_ref(String r60_cross_ref) {
			this.r60_cross_ref = r60_cross_ref;
		}

		public BigDecimal getR60_month_end() {
			return r60_month_end;
		}

		public void setR60_month_end(BigDecimal r60_month_end) {
			this.r60_month_end = r60_month_end;
		}

		public BigDecimal getR60_average() {
			return r60_average;
		}

		public void setR60_average(BigDecimal r60_average) {
			this.r60_average = r60_average;
		}

		public String getR61_product() {
			return r61_product;
		}

		public void setR61_product(String r61_product) {
			this.r61_product = r61_product;
		}

		public String getR61_cross_ref() {
			return r61_cross_ref;
		}

		public void setR61_cross_ref(String r61_cross_ref) {
			this.r61_cross_ref = r61_cross_ref;
		}

		public BigDecimal getR61_month_end() {
			return r61_month_end;
		}

		public void setR61_month_end(BigDecimal r61_month_end) {
			this.r61_month_end = r61_month_end;
		}

		public BigDecimal getR61_average() {
			return r61_average;
		}

		public void setR61_average(BigDecimal r61_average) {
			this.r61_average = r61_average;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public BigDecimal getReportVersion() {
			return reportVersion;
		}

		public void setReportVersion(BigDecimal reportVersion) {
			this.reportVersion = reportVersion;
		}

		public String getReport_frequency() {
			return report_frequency;
		}

		public void setReport_frequency(String report_frequency) {
			this.report_frequency = report_frequency;
		}

		public String getReport_code() {
			return report_code;
		}

		public void setReport_code(String report_code) {
			this.report_code = report_code;
		}

		public String getReport_desc() {
			return report_desc;
		}

		public void setReport_desc(String report_desc) {
			this.report_desc = report_desc;
		}

		public String getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(String entity_flg) {
			this.entity_flg = entity_flg;
		}

		public String getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(String modify_flg) {
			this.modify_flg = modify_flg;
		}

		public String getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(String del_flg) {
			this.del_flg = del_flg;
		}

		public M_SFINP1_Archival_Summary_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}
	}

	// ------------------------------
	// Archival Summary Manual Entity
	// ------------------------------
	public static class M_SFINP1_Archival_Summary_Manual_Entity {
		private BigDecimal R14_MONTH_END;
		private BigDecimal R34_MONTH_END;
		private BigDecimal R37_MONTH_END;
		private BigDecimal R39_MONTH_END;
		private BigDecimal R43_MONTH_END;
		private BigDecimal R42_MONTH_END;
		private BigDecimal R50_MONTH_END;
		private BigDecimal R51_MONTH_END;
		private BigDecimal R52_MONTH_END;
		private BigDecimal R57_MONTH_END;
		private BigDecimal R59_MONTH_END;
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date REPORT_DATE;
		private BigDecimal REPORT_VERSION;
		private String REPORT_FREQUENCY;
		private String REPORT_CODE;
		private String REPORT_DESC;
		private String ENTITY_FLG;
		private String MODIFY_FLG;
		private String DEL_FLG;

		public BigDecimal getR14_MONTH_END() {
			return R14_MONTH_END;
		}

		public void setR14_MONTH_END(BigDecimal r14_MONTH_END) {
			R14_MONTH_END = r14_MONTH_END;
		}

		public BigDecimal getR34_MONTH_END() {
			return R34_MONTH_END;
		}

		public void setR34_MONTH_END(BigDecimal r34_MONTH_END) {
			R34_MONTH_END = r34_MONTH_END;
		}

		public BigDecimal getR37_MONTH_END() {
			return R37_MONTH_END;
		}

		public void setR37_MONTH_END(BigDecimal r37_MONTH_END) {
			R37_MONTH_END = r37_MONTH_END;
		}

		public BigDecimal getR39_MONTH_END() {
			return R39_MONTH_END;
		}

		public void setR39_MONTH_END(BigDecimal r39_MONTH_END) {
			R39_MONTH_END = r39_MONTH_END;
		}

		public BigDecimal getR43_MONTH_END() {
			return R43_MONTH_END;
		}

		public void setR43_MONTH_END(BigDecimal r43_MONTH_END) {
			R43_MONTH_END = r43_MONTH_END;
		}

		public BigDecimal getR50_MONTH_END() {
			return R50_MONTH_END;
		}

		public void setR50_MONTH_END(BigDecimal r50_MONTH_END) {
			R50_MONTH_END = r50_MONTH_END;
		}

		public BigDecimal getR51_MONTH_END() {
			return R51_MONTH_END;
		}

		public void setR51_MONTH_END(BigDecimal r51_MONTH_END) {
			R51_MONTH_END = r51_MONTH_END;
		}

		public BigDecimal getR52_MONTH_END() {
			return R52_MONTH_END;
		}

		public void setR52_MONTH_END(BigDecimal r52_MONTH_END) {
			R52_MONTH_END = r52_MONTH_END;
		}

		public BigDecimal getR57_MONTH_END() {
			return R57_MONTH_END;
		}

		public void setR57_MONTH_END(BigDecimal r57_MONTH_END) {
			R57_MONTH_END = r57_MONTH_END;
		}

		public BigDecimal getR59_MONTH_END() {
			return R59_MONTH_END;
		}

		public void setR59_MONTH_END(BigDecimal r59_MONTH_END) {
			R59_MONTH_END = r59_MONTH_END;
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

		public BigDecimal getR42_MONTH_END() {
			return R42_MONTH_END;
		}

		public void setR42_MONTH_END(BigDecimal r42_MONTH_END) {
			R42_MONTH_END = r42_MONTH_END;
		}

		public M_SFINP1_Archival_Summary_Manual_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}
	}

	// ------------------------------
	// RowMapper for M_SFINP1_Summary_Entity
	// ------------------------------
	class M_SFINP1SummaryRowMapper implements RowMapper<M_SFINP1_Summary_Entity> {
		@Override
		public M_SFINP1_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SFINP1_Summary_Entity obj = new M_SFINP1_Summary_Entity();
			obj.setR10_product(rs.getString("r10_product"));
			obj.setR10_cross_ref(rs.getString("r10_cross_ref"));
			obj.setR10_month_end(rs.getBigDecimal("r10_month_end"));
			obj.setR10_average(rs.getBigDecimal("r10_average"));
			obj.setR11_product(rs.getString("r11_product"));
			obj.setR11_cross_ref(rs.getString("r11_cross_ref"));
			obj.setR11_month_end(rs.getBigDecimal("r11_month_end"));
			obj.setR11_average(rs.getBigDecimal("r11_average"));
			obj.setR12_product(rs.getString("r12_product"));
			obj.setR12_cross_ref(rs.getString("r12_cross_ref"));
			obj.setR12_month_end(rs.getBigDecimal("r12_month_end"));
			obj.setR12_average(rs.getBigDecimal("r12_average"));
			obj.setR13_product(rs.getString("r13_product"));
			obj.setR13_cross_ref(rs.getString("r13_cross_ref"));
			obj.setR13_month_end(rs.getBigDecimal("r13_month_end"));
			obj.setR13_average(rs.getBigDecimal("r13_average"));
			obj.setR14_product(rs.getString("r14_product"));
			obj.setR14_cross_ref(rs.getString("r14_cross_ref"));
			obj.setR14_month_end(rs.getBigDecimal("r14_month_end"));
			obj.setR14_average(rs.getBigDecimal("r14_average"));
			obj.setR15_product(rs.getString("r15_product"));
			obj.setR15_cross_ref(rs.getString("r15_cross_ref"));
			obj.setR15_month_end(rs.getBigDecimal("r15_month_end"));
			obj.setR15_average(rs.getBigDecimal("r15_average"));
			obj.setR16_product(rs.getString("r16_product"));
			obj.setR16_cross_ref(rs.getString("r16_cross_ref"));
			obj.setR16_month_end(rs.getBigDecimal("r16_month_end"));
			obj.setR16_average(rs.getBigDecimal("r16_average"));
			obj.setR17_product(rs.getString("r17_product"));
			obj.setR17_cross_ref(rs.getString("r17_cross_ref"));
			obj.setR17_month_end(rs.getBigDecimal("r17_month_end"));
			obj.setR17_average(rs.getBigDecimal("r17_average"));
			obj.setR18_product(rs.getString("r18_product"));
			obj.setR18_cross_ref(rs.getString("r18_cross_ref"));
			obj.setR18_month_end(rs.getBigDecimal("r18_month_end"));
			obj.setR18_average(rs.getBigDecimal("r18_average"));
			obj.setR19_product(rs.getString("r19_product"));
			obj.setR19_cross_ref(rs.getString("r19_cross_ref"));
			obj.setR19_month_end(rs.getBigDecimal("r19_month_end"));
			obj.setR19_average(rs.getBigDecimal("r19_average"));
			obj.setR20_product(rs.getString("r20_product"));
			obj.setR20_cross_ref(rs.getString("r20_cross_ref"));
			obj.setR20_month_end(rs.getBigDecimal("r20_month_end"));
			obj.setR20_average(rs.getBigDecimal("r20_average"));
			obj.setR21_product(rs.getString("r21_product"));
			obj.setR21_cross_ref(rs.getString("r21_cross_ref"));
			obj.setR21_month_end(rs.getBigDecimal("r21_month_end"));
			obj.setR21_average(rs.getBigDecimal("r21_average"));
			obj.setR22_product(rs.getString("r22_product"));
			obj.setR22_cross_ref(rs.getString("r22_cross_ref"));
			obj.setR22_month_end(rs.getBigDecimal("r22_month_end"));
			obj.setR22_average(rs.getBigDecimal("r22_average"));
			obj.setR23_product(rs.getString("r23_product"));
			obj.setR23_cross_ref(rs.getString("r23_cross_ref"));
			obj.setR23_month_end(rs.getBigDecimal("r23_month_end"));
			obj.setR23_average(rs.getBigDecimal("r23_average"));
			obj.setR24_product(rs.getString("r24_product"));
			obj.setR24_cross_ref(rs.getString("r24_cross_ref"));
			obj.setR24_month_end(rs.getBigDecimal("r24_month_end"));
			obj.setR24_average(rs.getBigDecimal("r24_average"));
			obj.setR25_product(rs.getString("r25_product"));
			obj.setR25_cross_ref(rs.getString("r25_cross_ref"));
			obj.setR25_month_end(rs.getBigDecimal("r25_month_end"));
			obj.setR25_average(rs.getBigDecimal("r25_average"));
			obj.setR26_product(rs.getString("r26_product"));
			obj.setR26_cross_ref(rs.getString("r26_cross_ref"));
			obj.setR26_month_end(rs.getBigDecimal("r26_month_end"));
			obj.setR26_average(rs.getBigDecimal("r26_average"));
			obj.setR27_product(rs.getString("r27_product"));
			obj.setR27_cross_ref(rs.getString("r27_cross_ref"));
			obj.setR27_month_end(rs.getBigDecimal("r27_month_end"));
			obj.setR27_average(rs.getBigDecimal("r27_average"));
			obj.setR28_product(rs.getString("r28_product"));
			obj.setR28_cross_ref(rs.getString("r28_cross_ref"));
			obj.setR28_month_end(rs.getBigDecimal("r28_month_end"));
			obj.setR28_average(rs.getBigDecimal("r28_average"));
			obj.setR29_product(rs.getString("r29_product"));
			obj.setR29_cross_ref(rs.getString("r29_cross_ref"));
			obj.setR29_month_end(rs.getBigDecimal("r29_month_end"));
			obj.setR29_average(rs.getBigDecimal("r29_average"));
			obj.setR30_product(rs.getString("r30_product"));
			obj.setR30_cross_ref(rs.getString("r30_cross_ref"));
			obj.setR30_month_end(rs.getBigDecimal("r30_month_end"));
			obj.setR30_average(rs.getBigDecimal("r30_average"));
			obj.setR31_product(rs.getString("r31_product"));
			obj.setR31_cross_ref(rs.getString("r31_cross_ref"));
			obj.setR31_month_end(rs.getBigDecimal("r31_month_end"));
			obj.setR31_average(rs.getBigDecimal("r31_average"));
			obj.setR32_product(rs.getString("r32_product"));
			obj.setR32_cross_ref(rs.getString("r32_cross_ref"));
			obj.setR32_month_end(rs.getBigDecimal("r32_month_end"));
			obj.setR32_average(rs.getBigDecimal("r32_average"));
			obj.setR33_product(rs.getString("r33_product"));
			obj.setR33_cross_ref(rs.getString("r33_cross_ref"));
			obj.setR33_month_end(rs.getBigDecimal("r33_month_end"));
			obj.setR33_average(rs.getBigDecimal("r33_average"));
			obj.setR34_product(rs.getString("r34_product"));
			obj.setR34_cross_ref(rs.getString("r34_cross_ref"));
			obj.setR34_month_end(rs.getBigDecimal("r34_month_end"));
			obj.setR34_average(rs.getBigDecimal("r34_average"));
			obj.setR35_product(rs.getString("r35_product"));
			obj.setR35_cross_ref(rs.getString("r35_cross_ref"));
			obj.setR35_month_end(rs.getBigDecimal("r35_month_end"));
			obj.setR35_average(rs.getBigDecimal("r35_average"));
			obj.setR36_product(rs.getString("r36_product"));
			obj.setR36_cross_ref(rs.getString("r36_cross_ref"));
			obj.setR36_month_end(rs.getBigDecimal("r36_month_end"));
			obj.setR36_average(rs.getBigDecimal("r36_average"));
			obj.setR37_product(rs.getString("r37_product"));
			obj.setR37_cross_ref(rs.getString("r37_cross_ref"));
			obj.setR37_month_end(rs.getBigDecimal("r37_month_end"));
			obj.setR37_average(rs.getBigDecimal("r37_average"));
			obj.setR38_product(rs.getString("r38_product"));
			obj.setR38_cross_ref(rs.getString("r38_cross_ref"));
			obj.setR38_month_end(rs.getBigDecimal("r38_month_end"));
			obj.setR38_average(rs.getBigDecimal("r38_average"));
			obj.setR39_product(rs.getString("r39_product"));
			obj.setR39_cross_ref(rs.getString("r39_cross_ref"));
			obj.setR39_month_end(rs.getBigDecimal("r39_month_end"));
			obj.setR39_average(rs.getBigDecimal("r39_average"));
			obj.setR40_product(rs.getString("r40_product"));
			obj.setR40_cross_ref(rs.getString("r40_cross_ref"));
			obj.setR40_month_end(rs.getBigDecimal("r40_month_end"));
			obj.setR40_average(rs.getBigDecimal("r40_average"));
			obj.setR41_product(rs.getString("r41_product"));
			obj.setR41_cross_ref(rs.getString("r41_cross_ref"));
			obj.setR41_month_end(rs.getBigDecimal("r41_month_end"));
			obj.setR41_average(rs.getBigDecimal("r41_average"));
			obj.setR42_product(rs.getString("r42_product"));
			obj.setR42_cross_ref(rs.getString("r42_cross_ref"));
			obj.setR42_month_end(rs.getBigDecimal("r42_month_end"));
			obj.setR42_average(rs.getBigDecimal("r42_average"));
			obj.setR43_product(rs.getString("r43_product"));
			obj.setR43_cross_ref(rs.getString("r43_cross_ref"));
			obj.setR43_month_end(rs.getBigDecimal("r43_month_end"));
			obj.setR43_average(rs.getBigDecimal("r43_average"));
			obj.setR44_product(rs.getString("r44_product"));
			obj.setR44_cross_ref(rs.getString("r44_cross_ref"));
			obj.setR44_month_end(rs.getBigDecimal("r44_month_end"));
			obj.setR44_average(rs.getBigDecimal("r44_average"));
			obj.setR45_product(rs.getString("r45_product"));
			obj.setR45_cross_ref(rs.getString("r45_cross_ref"));
			obj.setR45_month_end(rs.getBigDecimal("r45_month_end"));
			obj.setR45_average(rs.getBigDecimal("r45_average"));
			obj.setR46_product(rs.getString("r46_product"));
			obj.setR46_cross_ref(rs.getString("r46_cross_ref"));
			obj.setR46_month_end(rs.getBigDecimal("r46_month_end"));
			obj.setR46_average(rs.getBigDecimal("r46_average"));
			obj.setR47_product(rs.getString("r47_product"));
			obj.setR47_cross_ref(rs.getString("r47_cross_ref"));
			obj.setR47_month_end(rs.getBigDecimal("r47_month_end"));
			obj.setR47_average(rs.getBigDecimal("r47_average"));
			obj.setR48_product(rs.getString("r48_product"));
			obj.setR48_cross_ref(rs.getString("r48_cross_ref"));
			obj.setR48_month_end(rs.getBigDecimal("r48_month_end"));
			obj.setR48_average(rs.getBigDecimal("r48_average"));
			obj.setR49_product(rs.getString("r49_product"));
			obj.setR49_cross_ref(rs.getString("r49_cross_ref"));
			obj.setR49_month_end(rs.getBigDecimal("r49_month_end"));
			obj.setR49_average(rs.getBigDecimal("r49_average"));
			obj.setR50_product(rs.getString("r50_product"));
			obj.setR50_cross_ref(rs.getString("r50_cross_ref"));
			obj.setR50_month_end(rs.getBigDecimal("r50_month_end"));
			obj.setR50_average(rs.getBigDecimal("r50_average"));
			obj.setR51_product(rs.getString("r51_product"));
			obj.setR51_cross_ref(rs.getString("r51_cross_ref"));
			obj.setR51_month_end(rs.getBigDecimal("r51_month_end"));
			obj.setR51_average(rs.getBigDecimal("r51_average"));
			obj.setR52_product(rs.getString("r52_product"));
			obj.setR52_cross_ref(rs.getString("r52_cross_ref"));
			obj.setR52_month_end(rs.getBigDecimal("r52_month_end"));
			obj.setR52_average(rs.getBigDecimal("r52_average"));
			obj.setR53_product(rs.getString("r53_product"));
			obj.setR53_cross_ref(rs.getString("r53_cross_ref"));
			obj.setR53_month_end(rs.getBigDecimal("r53_month_end"));
			obj.setR53_average(rs.getBigDecimal("r53_average"));
			obj.setR54_product(rs.getString("r54_product"));
			obj.setR54_cross_ref(rs.getString("r54_cross_ref"));
			obj.setR54_month_end(rs.getBigDecimal("r54_month_end"));
			obj.setR54_average(rs.getBigDecimal("r54_average"));
			obj.setR55_product(rs.getString("r55_product"));
			obj.setR55_cross_ref(rs.getString("r55_cross_ref"));
			obj.setR55_month_end(rs.getBigDecimal("r55_month_end"));
			obj.setR55_average(rs.getBigDecimal("r55_average"));
			obj.setR56_product(rs.getString("r56_product"));
			obj.setR56_cross_ref(rs.getString("r56_cross_ref"));
			obj.setR56_month_end(rs.getBigDecimal("r56_month_end"));
			obj.setR56_average(rs.getBigDecimal("r56_average"));
			obj.setR57_product(rs.getString("r57_product"));
			obj.setR57_cross_ref(rs.getString("r57_cross_ref"));
			obj.setR57_month_end(rs.getBigDecimal("r57_month_end"));
			obj.setR57_average(rs.getBigDecimal("r57_average"));
			obj.setR58_product(rs.getString("r58_product"));
			obj.setR58_cross_ref(rs.getString("r58_cross_ref"));
			obj.setR58_month_end(rs.getBigDecimal("r58_month_end"));
			obj.setR58_average(rs.getBigDecimal("r58_average"));
			obj.setR59_product(rs.getString("r59_product"));
			obj.setR59_cross_ref(rs.getString("r59_cross_ref"));
			obj.setR59_month_end(rs.getBigDecimal("r59_month_end"));
			obj.setR59_average(rs.getBigDecimal("r59_average"));
			obj.setR60_product(rs.getString("r60_product"));
			obj.setR60_cross_ref(rs.getString("r60_cross_ref"));
			obj.setR60_month_end(rs.getBigDecimal("r60_month_end"));
			obj.setR60_average(rs.getBigDecimal("r60_average"));
			obj.setR61_product(rs.getString("r61_product"));
			obj.setR61_cross_ref(rs.getString("r61_cross_ref"));
			obj.setR61_month_end(rs.getBigDecimal("r61_month_end"));
			obj.setR61_average(rs.getBigDecimal("r61_average"));
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));
			return obj;
		}
	}

	// ------------------------------
	// RowMapper for M_SFINP1_Summary_Manual_Entity
	// ------------------------------
	class M_SFINP1SummaryManualRowMapper implements RowMapper<M_SFINP1_Summary_Manual_Entity> {
		@Override
		public M_SFINP1_Summary_Manual_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SFINP1_Summary_Manual_Entity obj = new M_SFINP1_Summary_Manual_Entity();
			obj.setR14_MONTH_END(rs.getBigDecimal("R14_MONTH_END"));
			obj.setR34_MONTH_END(rs.getBigDecimal("R34_MONTH_END"));
			obj.setR37_MONTH_END(rs.getBigDecimal("R37_MONTH_END"));
			obj.setR39_MONTH_END(rs.getBigDecimal("R39_MONTH_END"));
			obj.setR43_MONTH_END(rs.getBigDecimal("R43_MONTH_END"));
			obj.setR42_MONTH_END(rs.getBigDecimal("R42_MONTH_END"));
			obj.setR50_MONTH_END(rs.getBigDecimal("R50_MONTH_END"));
			obj.setR51_MONTH_END(rs.getBigDecimal("R51_MONTH_END"));
			obj.setR52_MONTH_END(rs.getBigDecimal("R52_MONTH_END"));
			obj.setR57_MONTH_END(rs.getBigDecimal("R57_MONTH_END"));
			obj.setR59_MONTH_END(rs.getBigDecimal("R59_MONTH_END"));
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));
			return obj;
		}
	}

	// ------------------------------
	// RowMapper for M_SFINP1_Detail_Entity
	// ------------------------------
	class M_SFINP1DetailRowMapper implements RowMapper<M_SFINP1_Detail_Entity> {
		@Override
		public M_SFINP1_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SFINP1_Detail_Entity obj = new M_SFINP1_Detail_Entity();
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setRowId(rs.getString("ROW_ID"));
			obj.setColumnId(rs.getString("COLUMN_ID"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInPula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setAverage(rs.getBigDecimal("AVERAGE"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
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

	// ------------------------------
	// RowMapper for M_SFINP1_Archival_Summary_Entity
	// ------------------------------
	class M_SFINP1ArchivalSummaryRowMapper implements RowMapper<M_SFINP1_Archival_Summary_Entity> {
		@Override
		public M_SFINP1_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SFINP1_Archival_Summary_Entity obj = new M_SFINP1_Archival_Summary_Entity();
			obj.setR10_product(rs.getString("r10_product"));
			obj.setR10_cross_ref(rs.getString("r10_cross_ref"));
			obj.setR10_month_end(rs.getBigDecimal("r10_month_end"));
			obj.setR10_average(rs.getBigDecimal("r10_average"));
			obj.setR11_product(rs.getString("r11_product"));
			obj.setR11_cross_ref(rs.getString("r11_cross_ref"));
			obj.setR11_month_end(rs.getBigDecimal("r11_month_end"));
			obj.setR11_average(rs.getBigDecimal("r11_average"));
			obj.setR12_product(rs.getString("r12_product"));
			obj.setR12_cross_ref(rs.getString("r12_cross_ref"));
			obj.setR12_month_end(rs.getBigDecimal("r12_month_end"));
			obj.setR12_average(rs.getBigDecimal("r12_average"));
			obj.setR13_product(rs.getString("r13_product"));
			obj.setR13_cross_ref(rs.getString("r13_cross_ref"));
			obj.setR13_month_end(rs.getBigDecimal("r13_month_end"));
			obj.setR13_average(rs.getBigDecimal("r13_average"));
			obj.setR14_product(rs.getString("r14_product"));
			obj.setR14_cross_ref(rs.getString("r14_cross_ref"));
			obj.setR14_month_end(rs.getBigDecimal("r14_month_end"));
			obj.setR14_average(rs.getBigDecimal("r14_average"));
			obj.setR15_product(rs.getString("r15_product"));
			obj.setR15_cross_ref(rs.getString("r15_cross_ref"));
			obj.setR15_month_end(rs.getBigDecimal("r15_month_end"));
			obj.setR15_average(rs.getBigDecimal("r15_average"));
			obj.setR16_product(rs.getString("r16_product"));
			obj.setR16_cross_ref(rs.getString("r16_cross_ref"));
			obj.setR16_month_end(rs.getBigDecimal("r16_month_end"));
			obj.setR16_average(rs.getBigDecimal("r16_average"));
			obj.setR17_product(rs.getString("r17_product"));
			obj.setR17_cross_ref(rs.getString("r17_cross_ref"));
			obj.setR17_month_end(rs.getBigDecimal("r17_month_end"));
			obj.setR17_average(rs.getBigDecimal("r17_average"));
			obj.setR18_product(rs.getString("r18_product"));
			obj.setR18_cross_ref(rs.getString("r18_cross_ref"));
			obj.setR18_month_end(rs.getBigDecimal("r18_month_end"));
			obj.setR18_average(rs.getBigDecimal("r18_average"));
			obj.setR19_product(rs.getString("r19_product"));
			obj.setR19_cross_ref(rs.getString("r19_cross_ref"));
			obj.setR19_month_end(rs.getBigDecimal("r19_month_end"));
			obj.setR19_average(rs.getBigDecimal("r19_average"));
			obj.setR20_product(rs.getString("r20_product"));
			obj.setR20_cross_ref(rs.getString("r20_cross_ref"));
			obj.setR20_month_end(rs.getBigDecimal("r20_month_end"));
			obj.setR20_average(rs.getBigDecimal("r20_average"));
			obj.setR21_product(rs.getString("r21_product"));
			obj.setR21_cross_ref(rs.getString("r21_cross_ref"));
			obj.setR21_month_end(rs.getBigDecimal("r21_month_end"));
			obj.setR21_average(rs.getBigDecimal("r21_average"));
			obj.setR22_product(rs.getString("r22_product"));
			obj.setR22_cross_ref(rs.getString("r22_cross_ref"));
			obj.setR22_month_end(rs.getBigDecimal("r22_month_end"));
			obj.setR22_average(rs.getBigDecimal("r22_average"));
			obj.setR23_product(rs.getString("r23_product"));
			obj.setR23_cross_ref(rs.getString("r23_cross_ref"));
			obj.setR23_month_end(rs.getBigDecimal("r23_month_end"));
			obj.setR23_average(rs.getBigDecimal("r23_average"));
			obj.setR24_product(rs.getString("r24_product"));
			obj.setR24_cross_ref(rs.getString("r24_cross_ref"));
			obj.setR24_month_end(rs.getBigDecimal("r24_month_end"));
			obj.setR24_average(rs.getBigDecimal("r24_average"));
			obj.setR25_product(rs.getString("r25_product"));
			obj.setR25_cross_ref(rs.getString("r25_cross_ref"));
			obj.setR25_month_end(rs.getBigDecimal("r25_month_end"));
			obj.setR25_average(rs.getBigDecimal("r25_average"));
			obj.setR26_product(rs.getString("r26_product"));
			obj.setR26_cross_ref(rs.getString("r26_cross_ref"));
			obj.setR26_month_end(rs.getBigDecimal("r26_month_end"));
			obj.setR26_average(rs.getBigDecimal("r26_average"));
			obj.setR27_product(rs.getString("r27_product"));
			obj.setR27_cross_ref(rs.getString("r27_cross_ref"));
			obj.setR27_month_end(rs.getBigDecimal("r27_month_end"));
			obj.setR27_average(rs.getBigDecimal("r27_average"));
			obj.setR28_product(rs.getString("r28_product"));
			obj.setR28_cross_ref(rs.getString("r28_cross_ref"));
			obj.setR28_month_end(rs.getBigDecimal("r28_month_end"));
			obj.setR28_average(rs.getBigDecimal("r28_average"));
			obj.setR29_product(rs.getString("r29_product"));
			obj.setR29_cross_ref(rs.getString("r29_cross_ref"));
			obj.setR29_month_end(rs.getBigDecimal("r29_month_end"));
			obj.setR29_average(rs.getBigDecimal("r29_average"));
			obj.setR30_product(rs.getString("r30_product"));
			obj.setR30_cross_ref(rs.getString("r30_cross_ref"));
			obj.setR30_month_end(rs.getBigDecimal("r30_month_end"));
			obj.setR30_average(rs.getBigDecimal("r30_average"));
			obj.setR31_product(rs.getString("r31_product"));
			obj.setR31_cross_ref(rs.getString("r31_cross_ref"));
			obj.setR31_month_end(rs.getBigDecimal("r31_month_end"));
			obj.setR31_average(rs.getBigDecimal("r31_average"));
			obj.setR32_product(rs.getString("r32_product"));
			obj.setR32_cross_ref(rs.getString("r32_cross_ref"));
			obj.setR32_month_end(rs.getBigDecimal("r32_month_end"));
			obj.setR32_average(rs.getBigDecimal("r32_average"));
			obj.setR33_product(rs.getString("r33_product"));
			obj.setR33_cross_ref(rs.getString("r33_cross_ref"));
			obj.setR33_month_end(rs.getBigDecimal("r33_month_end"));
			obj.setR33_average(rs.getBigDecimal("r33_average"));
			obj.setR34_product(rs.getString("r34_product"));
			obj.setR34_cross_ref(rs.getString("r34_cross_ref"));
			obj.setR34_month_end(rs.getBigDecimal("r34_month_end"));
			obj.setR34_average(rs.getBigDecimal("r34_average"));
			obj.setR35_product(rs.getString("r35_product"));
			obj.setR35_cross_ref(rs.getString("r35_cross_ref"));
			obj.setR35_month_end(rs.getBigDecimal("r35_month_end"));
			obj.setR35_average(rs.getBigDecimal("r35_average"));
			obj.setR36_product(rs.getString("r36_product"));
			obj.setR36_cross_ref(rs.getString("r36_cross_ref"));
			obj.setR36_month_end(rs.getBigDecimal("r36_month_end"));
			obj.setR36_average(rs.getBigDecimal("r36_average"));
			obj.setR37_product(rs.getString("r37_product"));
			obj.setR37_cross_ref(rs.getString("r37_cross_ref"));
			obj.setR37_month_end(rs.getBigDecimal("r37_month_end"));
			obj.setR37_average(rs.getBigDecimal("r37_average"));
			obj.setR38_product(rs.getString("r38_product"));
			obj.setR38_cross_ref(rs.getString("r38_cross_ref"));
			obj.setR38_month_end(rs.getBigDecimal("r38_month_end"));
			obj.setR38_average(rs.getBigDecimal("r38_average"));
			obj.setR39_product(rs.getString("r39_product"));
			obj.setR39_cross_ref(rs.getString("r39_cross_ref"));
			obj.setR39_month_end(rs.getBigDecimal("r39_month_end"));
			obj.setR39_average(rs.getBigDecimal("r39_average"));
			obj.setR40_product(rs.getString("r40_product"));
			obj.setR40_cross_ref(rs.getString("r40_cross_ref"));
			obj.setR40_month_end(rs.getBigDecimal("r40_month_end"));
			obj.setR40_average(rs.getBigDecimal("r40_average"));
			obj.setR41_product(rs.getString("r41_product"));
			obj.setR41_cross_ref(rs.getString("r41_cross_ref"));
			obj.setR41_month_end(rs.getBigDecimal("r41_month_end"));
			obj.setR41_average(rs.getBigDecimal("r41_average"));
			obj.setR42_product(rs.getString("r42_product"));
			obj.setR42_cross_ref(rs.getString("r42_cross_ref"));
			obj.setR42_month_end(rs.getBigDecimal("r42_month_end"));
			obj.setR42_average(rs.getBigDecimal("r42_average"));
			obj.setR43_product(rs.getString("r43_product"));
			obj.setR43_cross_ref(rs.getString("r43_cross_ref"));
			obj.setR43_month_end(rs.getBigDecimal("r43_month_end"));
			obj.setR43_average(rs.getBigDecimal("r43_average"));
			obj.setR44_product(rs.getString("r44_product"));
			obj.setR44_cross_ref(rs.getString("r44_cross_ref"));
			obj.setR44_month_end(rs.getBigDecimal("r44_month_end"));
			obj.setR44_average(rs.getBigDecimal("r44_average"));
			obj.setR45_product(rs.getString("r45_product"));
			obj.setR45_cross_ref(rs.getString("r45_cross_ref"));
			obj.setR45_month_end(rs.getBigDecimal("r45_month_end"));
			obj.setR45_average(rs.getBigDecimal("r45_average"));
			obj.setR46_product(rs.getString("r46_product"));
			obj.setR46_cross_ref(rs.getString("r46_cross_ref"));
			obj.setR46_month_end(rs.getBigDecimal("r46_month_end"));
			obj.setR46_average(rs.getBigDecimal("r46_average"));
			obj.setR47_product(rs.getString("r47_product"));
			obj.setR47_cross_ref(rs.getString("r47_cross_ref"));
			obj.setR47_month_end(rs.getBigDecimal("r47_month_end"));
			obj.setR47_average(rs.getBigDecimal("r47_average"));
			obj.setR48_product(rs.getString("r48_product"));
			obj.setR48_cross_ref(rs.getString("r48_cross_ref"));
			obj.setR48_month_end(rs.getBigDecimal("r48_month_end"));
			obj.setR48_average(rs.getBigDecimal("r48_average"));
			obj.setR49_product(rs.getString("r49_product"));
			obj.setR49_cross_ref(rs.getString("r49_cross_ref"));
			obj.setR49_month_end(rs.getBigDecimal("r49_month_end"));
			obj.setR49_average(rs.getBigDecimal("r49_average"));
			obj.setR50_product(rs.getString("r50_product"));
			obj.setR50_cross_ref(rs.getString("r50_cross_ref"));
			obj.setR50_month_end(rs.getBigDecimal("r50_month_end"));
			obj.setR50_average(rs.getBigDecimal("r50_average"));
			obj.setR51_product(rs.getString("r51_product"));
			obj.setR51_cross_ref(rs.getString("r51_cross_ref"));
			obj.setR51_month_end(rs.getBigDecimal("r51_month_end"));
			obj.setR51_average(rs.getBigDecimal("r51_average"));
			obj.setR52_product(rs.getString("r52_product"));
			obj.setR52_cross_ref(rs.getString("r52_cross_ref"));
			obj.setR52_month_end(rs.getBigDecimal("r52_month_end"));
			obj.setR52_average(rs.getBigDecimal("r52_average"));
			obj.setR53_product(rs.getString("r53_product"));
			obj.setR53_cross_ref(rs.getString("r53_cross_ref"));
			obj.setR53_month_end(rs.getBigDecimal("r53_month_end"));
			obj.setR53_average(rs.getBigDecimal("r53_average"));
			obj.setR54_product(rs.getString("r54_product"));
			obj.setR54_cross_ref(rs.getString("r54_cross_ref"));
			obj.setR54_month_end(rs.getBigDecimal("r54_month_end"));
			obj.setR54_average(rs.getBigDecimal("r54_average"));
			obj.setR55_product(rs.getString("r55_product"));
			obj.setR55_cross_ref(rs.getString("r55_cross_ref"));
			obj.setR55_month_end(rs.getBigDecimal("r55_month_end"));
			obj.setR55_average(rs.getBigDecimal("r55_average"));
			obj.setR56_product(rs.getString("r56_product"));
			obj.setR56_cross_ref(rs.getString("r56_cross_ref"));
			obj.setR56_month_end(rs.getBigDecimal("r56_month_end"));
			obj.setR56_average(rs.getBigDecimal("r56_average"));
			obj.setR57_product(rs.getString("r57_product"));
			obj.setR57_cross_ref(rs.getString("r57_cross_ref"));
			obj.setR57_month_end(rs.getBigDecimal("r57_month_end"));
			obj.setR57_average(rs.getBigDecimal("r57_average"));
			obj.setR58_product(rs.getString("r58_product"));
			obj.setR58_cross_ref(rs.getString("r58_cross_ref"));
			obj.setR58_month_end(rs.getBigDecimal("r58_month_end"));
			obj.setR58_average(rs.getBigDecimal("r58_average"));
			obj.setR59_product(rs.getString("r59_product"));
			obj.setR59_cross_ref(rs.getString("r59_cross_ref"));
			obj.setR59_month_end(rs.getBigDecimal("r59_month_end"));
			obj.setR59_average(rs.getBigDecimal("r59_average"));
			obj.setR60_product(rs.getString("r60_product"));
			obj.setR60_cross_ref(rs.getString("r60_cross_ref"));
			obj.setR60_month_end(rs.getBigDecimal("r60_month_end"));
			obj.setR60_average(rs.getBigDecimal("r60_average"));
			obj.setR61_product(rs.getString("r61_product"));
			obj.setR61_cross_ref(rs.getString("r61_cross_ref"));
			obj.setR61_month_end(rs.getBigDecimal("r61_month_end"));
			obj.setR61_average(rs.getBigDecimal("r61_average"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));
			return obj;
		}
	}

	// ------------------------------
	// RowMapper for M_SFINP1_Archival_Summary_Manual_Entity
	// ------------------------------
	class M_SFINP1ArchivalSummaryManualRowMapper implements RowMapper<M_SFINP1_Archival_Summary_Manual_Entity> {
		@Override
		public M_SFINP1_Archival_Summary_Manual_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SFINP1_Archival_Summary_Manual_Entity obj = new M_SFINP1_Archival_Summary_Manual_Entity();
			obj.setR14_MONTH_END(rs.getBigDecimal("R14_MONTH_END"));
			obj.setR34_MONTH_END(rs.getBigDecimal("R34_MONTH_END"));
			obj.setR37_MONTH_END(rs.getBigDecimal("R37_MONTH_END"));
			obj.setR39_MONTH_END(rs.getBigDecimal("R39_MONTH_END"));
			obj.setR43_MONTH_END(rs.getBigDecimal("R43_MONTH_END"));
			obj.setR42_MONTH_END(rs.getBigDecimal("R42_MONTH_END"));
			obj.setR50_MONTH_END(rs.getBigDecimal("R50_MONTH_END"));
			obj.setR51_MONTH_END(rs.getBigDecimal("R51_MONTH_END"));
			obj.setR52_MONTH_END(rs.getBigDecimal("R52_MONTH_END"));
			obj.setR57_MONTH_END(rs.getBigDecimal("R57_MONTH_END"));
			obj.setR59_MONTH_END(rs.getBigDecimal("R59_MONTH_END"));
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));
			return obj;
		}
	}

	// ------------------------------
	// RowMapper for M_SFINP1_Archival_Detail_Entity
	// ------------------------------
	class M_SFINP1ArchivalDetailRowMapper implements RowMapper<M_SFINP1_Archival_Detail_Entity> {
		@Override
		public M_SFINP1_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SFINP1_Archival_Detail_Entity obj = new M_SFINP1_Archival_Detail_Entity();
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setRowId(rs.getString("ROW_ID"));
			obj.setColumnId(rs.getString("COLUMN_ID"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInPula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setAverage(rs.getBigDecimal("AVERAGE"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
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