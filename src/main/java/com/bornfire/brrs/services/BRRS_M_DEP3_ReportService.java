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
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
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
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfileRep;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

// ------------------------------
// SERVICE FOR M_DEP3 REPORTS
// ------------------------------
@Component
@Service
public class BRRS_M_DEP3_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_DEP3_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	UserProfileRep userProfileRep;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// ------------------------------
	// GET SUMMARY VIEW FOR M_DEP3
	// ------------------------------
	public ModelAndView getM_DEP3View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {
		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		if (type.equals("ARCHIVAL") & version != null) {
			List<M_DEP3_Archival_Summary_Entity> T1Master = new ArrayList<M_DEP3_Archival_Summary_Entity>();

			try {
				Date d1 = dateformat.parse(todate);
				T1Master = jdbcTemplate.query(
						"select * from BRRS_M_DEP3_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ? and REPORT_VERSION = ?",
						new Object[] { dateformat.parse(todate), version },
						new M_DEP3_Archival_Summary_EntityRowMapper());
			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);

		} else {
			List<M_DEP3_Summary_Entity> T1Master = new ArrayList<M_DEP3_Summary_Entity>();

			try {
				Date d1 = dateformat.parse(todate);
				T1Master = jdbcTemplate.query("SELECT * FROM BRRS_M_DEP3_SUMMARYTABLE WHERE REPORT_DATE = ?",
						new Object[] { dateformat.parse(todate) }, new M_DEP3_Summary_EntityRowMapper());

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);

		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/M_DEP3");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

	// ------------------------------
	// GET DETAILS VIEW FOR M_DEP3
	// ------------------------------
	public ModelAndView getM_DEP3currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version, HttpServletRequest req1,
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

				List<M_DEP3_Archival_Detail_Entity> T1Dt1;

				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = jdbcTemplate.query(
							"select * from BRRS_M_DEP3_ARCHIVALTABLE_DETAIL where REPORT_LABEL =? and REPORT_ADDL_CRITERIA_1=? AND REPORT_DATE=? AND DATA_ENTRY_VERSION=?",
							new Object[] { reportLable, reportAddlCriteria_1, parsedDate, version },
							new M_DEP3_Archival_Detail_EntityRowMapper());
				} else {
					T1Dt1 = jdbcTemplate.query(
							"select * from BRRS_M_DEP3_ARCHIVALTABLE_DETAIL where REPORT_DATE=? AND DATA_ENTRY_VERSION=?",
							new Object[] { parsedDate, version }, new M_DEP3_Archival_Detail_EntityRowMapper());

					totalPages = jdbcTemplate.queryForObject(
							"SELECT COUNT(*) FROM BRRS_M_DEP3_ARCHIVALTABLE_DETAIL WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DATA_ENTRY_VERSION = ?",
							new Object[] { parsedDate, version }, Integer.class);

					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);

				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
			} else {
				// 🔹 Current branch
				List<M_DEP3_Detail_Entity> T1Dt1;

				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = jdbcTemplate.query(
							"select * from BRRS_M_DEP3_DETAILTABLE where REPORT_LABEL =? and REPORT_ADDL_CRITERIA_1=? AND REPORT_DATE=?",
							new Object[] { reportLable, reportAddlCriteria_1, parsedDate },
							new M_DEP3_Detail_EntityRowMapper());
				} else {
					T1Dt1 = jdbcTemplate.query("select * from BRRS_M_DEP3_DETAILTABLE where REPORT_DATE = ?",
							new Object[] { parsedDate }, new M_DEP3_Detail_EntityRowMapper());
					totalPages = jdbcTemplate.queryForObject(
							"select count(*) from BRRS_M_DEP3_DETAILTABLE where REPORT_DATE = ?",
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

		mv.setViewName("BRRS/M_DEP3");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}

	@Transactional
	// ------------------------------
	// UPDATE M_DEP3 SUMMARY REPORT
	// ------------------------------
	public void updateReport(M_DEP3_Summary_Entity updatedEntity) {
		System.out.println("Came to services1");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		List<M_DEP3_Summary_Entity> list = jdbcTemplate.query(
				"SELECT * FROM BRRS_M_DEP3_SUMMARYTABLE WHERE REPORT_DATE = ?",
				new Object[] { updatedEntity.getReportDate() }, new M_DEP3_Summary_EntityRowMapper());
		if (list.isEmpty()) {
			throw new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReportDate());
		}
		M_DEP3_Summary_Entity existing = list.get(0);

		// 🔹 Audit copy: Create clone of original DB state before altering any
		// attributes
		M_DEP3_Summary_Entity oldcopy = new M_DEP3_Summary_Entity();
		BeanUtils.copyProperties(existing, oldcopy);

		try {
			// ✅ Loop for table 2 fields
			int[] Rows = { 28, 29, 30, 31, 32, 33, 34 };
			for (int i : Rows) {
				String prefix = "R" + i + "_";
				String[] fields = { "import", "investment", "other", "residents", "non_residents" };

				for (String field : fields) {
					try {
						String getterName = "get" + prefix + field;
						String setterName = "set" + prefix + field;

						Method getter = M_DEP3_Summary_Entity.class.getMethod(getterName);
						Method setter = M_DEP3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						Object existingValue = getter.invoke(existing);

						// --- FIX: Normalize state differences to keep audit log payload minimal ---
						String currentValStr = (existingValue == null) ? "" : existingValue.toString().trim();
						String newValStr = (newValue == null) ? "" : newValue.toString().trim();

						if (currentValStr.equals(newValStr)) {
							continue;
						}

						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing getter/setter gracefully
						continue;
					}
				}
			}

			// ✅ Loop for exchange rate fields
			int[] Rows1 = { 11, 12, 13, 14, 15, 16 };
			for (int i : Rows1) {
				String prefix = "R" + i + "_";
				String[] fields = { "ex_rate_buy", "ex_rate_mid", "ex_rate_sell" };

				for (String field : fields) {
					try {
						String getterName = "get" + prefix + field;
						String setterName = "set" + prefix + field;

						Method getter = M_DEP3_Summary_Entity.class.getMethod(getterName);
						Method setter = M_DEP3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						Object existingValue = getter.invoke(existing);

						// --- FIX: Normalize state differences ---
						String currentValStr = (existingValue == null) ? "" : existingValue.toString().trim();
						String newValStr = (newValue == null) ? "" : newValue.toString().trim();

						if (currentValStr.equals(newValStr)) {
							continue;
						}

						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

			// ✅ Loop for notice/deposit fields
			int[] Rows2 = { 11, 12, 13, 14, 15, 16, 18 };
			for (int i : Rows2) {
				String prefix = "R" + i + "_";
				String[] fields = { "notice_0to31", "notice_32to88", "cer_of_depo" };

				for (String field : fields) {
					try {
						String getterName = "get" + prefix + field;
						String setterName = "set" + prefix + field;

						Method getter = M_DEP3_Summary_Entity.class.getMethod(getterName);
						Method setter = M_DEP3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						Object existingValue = getter.invoke(existing);

						// --- FIX: Normalize state differences ---
						String currentValStr = (existingValue == null) ? "" : existingValue.toString().trim();
						String newValStr = (newValue == null) ? "" : newValue.toString().trim();

						if (currentValStr.equals(newValStr)) {
							continue;
						}

						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

			// ✅ Save after all updates
			saveSummaryEntity(existing);

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// Evaluate actual adjustments made across processing threads
		String changes = auditService.getChanges(oldcopy, existing);
		System.out.println("M_DEP3 Changes Length = " + changes.length());

		// Post dynamic tracking to engine if changes are present
		if (changes != null && !changes.isEmpty()) {
			auditService.compareEntitiesmanual(oldcopy, existing, updatedEntity.getReportDate().toString(),
					"M_DEP3 Summary Screen", "BRRS_M_DEP3_SUMMARY");
		}

		System.out.println("Update completed successfully");
	}

	// ------------------------------
	// EXPORT DETAILS TO EXCEL
	// ------------------------------
	public byte[] getM_DEP3DetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for M_DEP3 Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_DEP3Detail");

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

//ACCT BALANCE style (right aligned with thousand separator)
			CellStyle balanceStyle = workbook.createCellStyle();
			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

//Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABEL",
					"REPORT ADDL CRITERIA1", "REPORT_DATE" };

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
			List<M_DEP3_Detail_Entity> reportData = jdbcTemplate.query(
					"select * from BRRS_M_DEP3_DETAILTABLE where REPORT_DATE = ?", new Object[] { parsedToDate },
					new M_DEP3_Detail_EntityRowMapper());

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_DEP3_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

//ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria1());
					row.createCell(6)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for M_DEP3 — only header will be written.");
			}

//Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_DEP3 Excel", e);
			return new byte[0];
		}
	}

	// ------------------------------
	// EXPORT ARCHIVAL DETAILS TO EXCEL
	// ------------------------------
	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_M_DEP3 ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_DEP3Detail");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABEL",
					"REPORT ADDL CRITERIA", "REPORT_DATE" };

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
			List<M_DEP3_Archival_Detail_Entity> reportData = jdbcTemplate.query(
					"select * from BRRS_M_DEP3_ARCHIVALTABLE_DETAIL where REPORT_DATE=? AND DATA_ENTRY_VERSION=?",
					new Object[] { parsedToDate, version }, new M_DEP3_Archival_Detail_EntityRowMapper());

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_DEP3_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

// ACCT BALANCE (right aligned, 3 decimal places with comma separator)
					Cell balanceCell = row.createCell(3);

					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}

// Create style with thousand separator and decimal point
					DataFormat format = workbook.createDataFormat();

// Format: 1,234,567
					balanceStyle.setDataFormat(format.getFormat("#,##0"));

// Right alignment (optional)
					balanceStyle.setAlignment(HorizontalAlignment.RIGHT);

					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria1());
					row.createCell(6)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for M_DEP3 — only header will be written.");
			}
// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_DEP3 Excel", e);
			return new byte[0];
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// ------------------------------
	// VIEW OR EDIT SINGLE RECORD PAGE
	// ------------------------------
	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_DEP3"); // ✅ match the report name
		System.out.println("Hello");
		if (acctNo != null) {
			List<M_DEP3_Detail_Entity> detailsList = jdbcTemplate.query(
					"SELECT * FROM BRRS_M_DEP3_DETAILTABLE WHERE ACCT_NUMBER = ?", new Object[] { acctNo },
					new M_DEP3_Detail_EntityRowMapper());
			M_DEP3_Detail_Entity dep3Entity = detailsList.isEmpty() ? null : detailsList.get(0);
			if (dep3Entity != null && dep3Entity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(dep3Entity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("Data", dep3Entity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	// ------------------------------
	// RETRIEVE DETAIL EDIT PAGE
	// ------------------------------
	public ModelAndView updateDetailEdit(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_DEP3"); // ✅ match the report name

		if (acctNo != null) {
			List<M_DEP3_Detail_Entity> detailsList = jdbcTemplate.query(
					"SELECT * FROM BRRS_M_DEP3_DETAILTABLE WHERE ACCT_NUMBER = ?", new Object[] { acctNo },
					new M_DEP3_Detail_EntityRowMapper());
			M_DEP3_Detail_Entity la1Entity = detailsList.isEmpty() ? null : detailsList.get(0);
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
	// UPDATE SINGLE DETAIL RECORD
	// ------------------------------
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String provisionStr = request.getParameter("acctBalanceInpula");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			List<M_DEP3_Detail_Entity> detailsList = jdbcTemplate.query(
					"SELECT * FROM BRRS_M_DEP3_DETAILTABLE WHERE ACCT_NUMBER = ?", new Object[] { acctNo },
					new M_DEP3_Detail_EntityRowMapper());
			M_DEP3_Detail_Entity existing = detailsList.isEmpty() ? null : detailsList.get(0);
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
				if (existing.getAcctBalanceInpula() == null
						|| existing.getAcctBalanceInpula().compareTo(newProvision) != 0) {
					existing.setAcctBalanceInpula(newProvision);
					isChanged = true;
					logger.info("Balance updated to {}", newProvision);
				}
			}

			if (isChanged) {
				jdbcTemplate.update(
						"UPDATE BRRS_M_DEP3_DETAILTABLE SET ACCT_NAME = ?, ACCT_BALANCE_IN_PULA = ? WHERE ACCT_NUMBER = ?",
						existing.getAcctName(), existing.getAcctBalanceInpula(), existing.getAcctNumber());
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_M_DEP3_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_M_DEP3_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating M_DEP3 record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	// ------------------------------
	// GET RESUBMISSION DATA
	// ------------------------------
	public List<Object[]> getM_DEP3Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_DEP3_Archival_Summary_Entity> latestArchivalList = jdbcTemplate.query(
					"SELECT * FROM BRRS_M_DEP3_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC",
					new M_DEP3_Archival_Summary_EntityRowMapper());

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_DEP3_Archival_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_DEP3 Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

//	// Archival View
	// ------------------------------
	// GET ARCHIVAL LIST DATA
	// ------------------------------
	public List<Object[]> getM_DEP3Archival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_DEP3_Archival_Summary_Entity> repoData = jdbcTemplate.query(
					"SELECT * FROM BRRS_M_DEP3_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC",
					new M_DEP3_Archival_Summary_EntityRowMapper());

			if (repoData != null && !repoData.isEmpty()) {
				for (M_DEP3_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_DEP3_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  M_DEP3  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}
	// DOWNLOAD

	// Normal format Excel

	// ------------------------------
	// EXPORT SUMMARY TO EXCEL
	// ------------------------------
	public byte[] BRRS_M_DEP3Excel(String filename, String reportId, String fromdate, String todate, String currency,
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
				return getExcelM_DEP3ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
//		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
//			logger.info("Service: Generating RESUB report for version {}", version);
//
//			try {
//				// ✅ Redirecting to Resub Excel
//				return BRRS_M_DEP3ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
//						version);
//
//			} catch (ParseException e) {
//				logger.error("Invalid report date format: {}", fromdate, e);
//				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
//			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_DEP3EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				List<M_DEP3_Summary_Entity> dataList = jdbcTemplate.query(
						"SELECT * FROM BRRS_M_DEP3_SUMMARYTABLE WHERE REPORT_DATE = ?",
						new Object[] { dateformat.parse(todate) }, new M_DEP3_Summary_EntityRowMapper());

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_DEP3 report. Returning empty result.");
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
					// NORMAL
// --- End of Style Definitions ---
					int startRow = 6;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {

							M_DEP3_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							Cell R12Cell = row.createCell(1);

							if (record.getReportDate() != null) {

								R12Cell.setCellValue(record.getReportDate());

								R12Cell.setCellStyle(dateStyle);

							} else {

								R12Cell.setCellValue("");

								R12Cell.setCellStyle(textStyle);
							}
							row = sheet.getRow(10);
							// R11 Col B

							Cell R11cell1 = row.createCell(1);
							if (record.getR11_ex_rate_buy() != null) {
								R11cell1.setCellValue(record.getR11_ex_rate_buy().doubleValue());
								R11cell1.setCellStyle(numberStyle);
							} else {
								R11cell1.setCellValue("");
								R11cell1.setCellStyle(textStyle);
							}

// R11 Col C
							Cell R11cell2 = row.createCell(2);
							if (record.getR11_ex_rate_mid() != null) {
								R11cell2.setCellValue(record.getR11_ex_rate_mid().doubleValue());
								R11cell2.setCellStyle(numberStyle);
							} else {
								R11cell2.setCellValue("");
								R11cell2.setCellStyle(textStyle);
							}

// R11 Col D
							Cell R11cell3 = row.createCell(3);
							if (record.getR11_ex_rate_sell() != null) {
								R11cell3.setCellValue(record.getR11_ex_rate_sell().doubleValue());
								R11cell3.setCellStyle(numberStyle);
							} else {
								R11cell3.setCellValue("");
								R11cell3.setCellStyle(textStyle);
							}

// R11 Col E
							Cell R11cell4 = row.createCell(4);
							if (record.getR11_current() != null) {
								R11cell4.setCellValue(record.getR11_current().doubleValue());
								R11cell4.setCellStyle(numberStyle);
							} else {
								R11cell4.setCellValue("");
								R11cell1.setCellStyle(textStyle);
							}

// R11 Col F
							Cell R11cell5 = row.createCell(5);
							if (record.getR11_call() != null) {
								R11cell5.setCellValue(record.getR11_call().doubleValue());
								R11cell5.setCellStyle(numberStyle);
							} else {
								R11cell5.setCellValue("");
								R11cell5.setCellStyle(textStyle);
							}

// R11 Col G
							Cell R11cell6 = row.createCell(6);
							if (record.getR11_savings() != null) {
								R11cell6.setCellValue(record.getR11_savings().doubleValue());
								R11cell6.setCellStyle(numberStyle);
							} else {
								R11cell6.setCellValue("");
								R11cell6.setCellStyle(textStyle);

							}

// R11 Col H
							Cell R11cell7 = row.createCell(7);
							if (record.getR11_notice_0to31() != null) {
								R11cell7.setCellValue(record.getR11_notice_0to31().doubleValue());
								R11cell7.setCellStyle(numberStyle);
							} else {
								R11cell7.setCellValue("");
								R11cell7.setCellStyle(textStyle);

							}

// R11 Col I
							Cell R11cell8 = row.createCell(8);
							if (record.getR11_notice_32to88() != null) {
								R11cell8.setCellValue(record.getR11_notice_32to88().doubleValue());
								R11cell8.setCellStyle(numberStyle);
							} else {
								R11cell8.setCellValue("");
								R11cell8.setCellStyle(textStyle);
							}

// R11 Col J
							Cell R11cell9 = row.createCell(9);
							if (record.getR11_fix_depo_91_day_depo() != null) {
								R11cell9.setCellValue(record.getR11_fix_depo_91_day_depo().doubleValue());
								R11cell9.setCellStyle(numberStyle);
							} else {
								R11cell9.setCellValue("");
								R11cell9.setCellStyle(textStyle);
							}

// R11 Col K
							Cell R11cell10 = row.createCell(10);
							if (record.getR11_fix_depo_1to2() != null) {
								R11cell10.setCellValue(record.getR11_fix_depo_1to2().doubleValue());
								R11cell10.setCellStyle(numberStyle);
							} else {
								R11cell10.setCellValue("");
								R11cell10.setCellStyle(textStyle);
							}

// R11 Col L
							Cell R11cell11 = row.createCell(11);
							if (record.getR11_fix_depo_4to6() != null) {
								R11cell11.setCellValue(record.getR11_fix_depo_4to6().doubleValue());
								R11cell11.setCellStyle(numberStyle);
							} else {
								R11cell11.setCellValue("");
								R11cell11.setCellStyle(textStyle);
							}

// R11 Col M
							Cell R11cell12 = row.createCell(12);
							if (record.getR11_fix_depo_7to12() != null) {
								R11cell12.setCellValue(record.getR11_fix_depo_7to12().doubleValue());
								R11cell12.setCellStyle(numberStyle);
							} else {
								R11cell12.setCellValue("");
								R11cell12.setCellStyle(textStyle);
							}

// R11 Col N
							Cell R11cell13 = row.createCell(13);
							if (record.getR11_fix_depo_13to18() != null) {
								R11cell13.setCellValue(record.getR11_fix_depo_13to18().doubleValue());
								R11cell13.setCellStyle(numberStyle);
							} else {
								R11cell13.setCellValue("");
								R11cell13.setCellStyle(textStyle);
							}

// R11 Col O
							Cell R11cell14 = row.createCell(14);
							if (record.getR11_fix_depo_19to24() != null) {
								R11cell14.setCellValue(record.getR11_fix_depo_19to24().doubleValue());
								R11cell14.setCellStyle(numberStyle);
							} else {
								R11cell14.setCellValue("");
								R11cell14.setCellStyle(textStyle);
							}

// R11 Col P
							Cell R11cell15 = row.createCell(15);
							if (record.getR11_fix_depo_over24() != null) {
								R11cell15.setCellValue(record.getR11_fix_depo_over24().doubleValue());
								R11cell15.setCellStyle(numberStyle);
							} else {
								R11cell15.setCellValue("");
								R11cell15.setCellStyle(textStyle);
							}

// R11 Col Q
							Cell R11cell16 = row.createCell(16);
							if (record.getR11_cer_of_depo() != null) {
								R11cell16.setCellValue(record.getR11_cer_of_depo().doubleValue());
								R11cell16.setCellStyle(numberStyle);
							} else {
								R11cell16.setCellValue("");
								R11cell16.setCellStyle(textStyle);

							}

// R11 Col S
							Cell R11cell18 = row.createCell(18);
							if (record.getR11_pula_equivalent() != null) {
								R11cell18.setCellValue(record.getR11_pula_equivalent().doubleValue());
								R11cell18.setCellStyle(numberStyle);
							} else {
								R11cell18.setCellValue("");
								R11cell18.setCellStyle(textStyle);
							}

// R11 Col T
//							Cell R11cell19 = row.createCell(19);
//							if (record.getR11_avg_pula_equivalent() != null) {
//								R11cell19.setCellValue(record.getR11_avg_pula_equivalent().doubleValue());
//								R11cell19.setCellStyle(numberStyle);
//							} else {
//								R11cell19.setCellValue("");
//								R11cell19.setCellStyle(textStyle);
//							}
							row = sheet.getRow(11);
// R12 Col B
							Cell R12cell1 = row.createCell(1);
							if (record.getR12_ex_rate_buy() != null) {
								R12cell1.setCellValue(record.getR12_ex_rate_buy().doubleValue());
								R12cell1.setCellStyle(numberStyle);
							} else {
								R12cell1.setCellValue("");
								R12cell1.setCellStyle(textStyle);
							}

// R12 Col C
							Cell R12cell2 = row.createCell(2);
							if (record.getR12_ex_rate_mid() != null) {
								R12cell2.setCellValue(record.getR12_ex_rate_mid().doubleValue());
								R12cell2.setCellStyle(numberStyle);
							} else {
								R12cell2.setCellValue("");
								R12cell2.setCellStyle(textStyle);
							}

// R12 Col D
							Cell R12cell3 = row.createCell(3);
							if (record.getR12_ex_rate_sell() != null) {
								R12cell3.setCellValue(record.getR12_ex_rate_sell().doubleValue());
								R12cell3.setCellStyle(numberStyle);
							} else {
								R12cell3.setCellValue("");
								R12cell3.setCellStyle(textStyle);
							}

// R12 Col E
							Cell R12cell4 = row.createCell(4);
							if (record.getR12_current() != null) {
								R12cell4.setCellValue(record.getR12_current().doubleValue());
								R12cell4.setCellStyle(numberStyle);
							} else {
								R12cell4.setCellValue("");
								R12cell1.setCellStyle(textStyle);
							}

// R12 Col F
							Cell R12cell5 = row.createCell(5);
							if (record.getR12_call() != null) {
								R12cell5.setCellValue(record.getR12_call().doubleValue());
								R12cell5.setCellStyle(numberStyle);
							} else {
								R12cell5.setCellValue("");
								R12cell5.setCellStyle(textStyle);
							}

// R12 Col G
							Cell R12cell6 = row.createCell(6);
							if (record.getR12_savings() != null) {
								R12cell6.setCellValue(record.getR12_savings().doubleValue());
								R12cell6.setCellStyle(numberStyle);
							} else {
								R12cell6.setCellValue("");
								R12cell6.setCellStyle(textStyle);

							}

// R12 Col H
							Cell R12cell7 = row.createCell(7);
							if (record.getR12_notice_0to31() != null) {
								R12cell7.setCellValue(record.getR12_notice_0to31().doubleValue());
								R12cell7.setCellStyle(numberStyle);
							} else {
								R12cell7.setCellValue("");
								R12cell7.setCellStyle(textStyle);

							}

// R12 Col I
							Cell R12cell8 = row.createCell(8);
							if (record.getR12_notice_32to88() != null) {
								R12cell8.setCellValue(record.getR12_notice_32to88().doubleValue());
								R12cell8.setCellStyle(numberStyle);
							} else {
								R12cell8.setCellValue("");
								R12cell8.setCellStyle(textStyle);
							}

// R12 Col J
							Cell R12cell9 = row.createCell(9);
							if (record.getR12_fix_depo_91_day_depo() != null) {
								R12cell9.setCellValue(record.getR12_fix_depo_91_day_depo().doubleValue());
								R12cell9.setCellStyle(numberStyle);
							} else {
								R12cell9.setCellValue("");
								R12cell9.setCellStyle(textStyle);
							}

// R12 Col K
							Cell R12cell10 = row.createCell(10);
							if (record.getR12_fix_depo_1to2() != null) {
								R12cell10.setCellValue(record.getR12_fix_depo_1to2().doubleValue());
								R12cell10.setCellStyle(numberStyle);
							} else {
								R12cell10.setCellValue("");
								R12cell10.setCellStyle(textStyle);
							}

// R12 Col L
							Cell R12cell11 = row.createCell(11);
							if (record.getR12_fix_depo_4to6() != null) {
								R12cell11.setCellValue(record.getR12_fix_depo_4to6().doubleValue());
								R12cell11.setCellStyle(numberStyle);
							} else {
								R12cell11.setCellValue("");
								R12cell11.setCellStyle(textStyle);
							}

// R12 Col M
							Cell R12cell12 = row.createCell(12);
							if (record.getR12_fix_depo_7to12() != null) {
								R12cell12.setCellValue(record.getR12_fix_depo_7to12().doubleValue());
								R12cell12.setCellStyle(numberStyle);
							} else {
								R12cell12.setCellValue("");
								R12cell12.setCellStyle(textStyle);
							}

// R12 Col N
							Cell R12cell13 = row.createCell(13);
							if (record.getR12_fix_depo_13to18() != null) {
								R12cell13.setCellValue(record.getR12_fix_depo_13to18().doubleValue());
								R12cell13.setCellStyle(numberStyle);
							} else {
								R12cell13.setCellValue("");
								R12cell13.setCellStyle(textStyle);
							}

// R12 Col O
							Cell R12cell14 = row.createCell(14);
							if (record.getR12_fix_depo_19to24() != null) {
								R12cell14.setCellValue(record.getR12_fix_depo_19to24().doubleValue());
								R12cell14.setCellStyle(numberStyle);
							} else {
								R12cell14.setCellValue("");
								R12cell14.setCellStyle(textStyle);
							}

// R12 Col P
							Cell R12cell15 = row.createCell(15);
							if (record.getR12_fix_depo_over24() != null) {
								R12cell15.setCellValue(record.getR12_fix_depo_over24().doubleValue());
								R12cell15.setCellStyle(numberStyle);
							} else {
								R12cell15.setCellValue("");
								R12cell15.setCellStyle(textStyle);
							}

// R12 Col Q
							Cell R12cell16 = row.createCell(16);
							if (record.getR12_cer_of_depo() != null) {
								R12cell16.setCellValue(record.getR12_cer_of_depo().doubleValue());
								R12cell16.setCellStyle(numberStyle);
							} else {
								R12cell16.setCellValue("");
								R12cell16.setCellStyle(textStyle);
							}

// R12 Col S
							Cell R12cell18 = row.createCell(18);
							if (record.getR12_pula_equivalent() != null) {
								R12cell18.setCellValue(record.getR12_pula_equivalent().doubleValue());
								R12cell18.setCellStyle(numberStyle);
							} else {
								R12cell18.setCellValue("");
								R12cell18.setCellStyle(textStyle);
							}

// R12 Col T
//							Cell R12cell19 = row.createCell(19);
//							if (record.getR12_avg_pula_equivalent() != null) {
//								R12cell19.setCellValue(record.getR12_avg_pula_equivalent().doubleValue());
//								R12cell19.setCellStyle(numberStyle);
//							} else {
//								R12cell19.setCellValue("");
//								R12cell19.setCellStyle(textStyle);
//							}

// R13 Col B
							row = sheet.getRow(12);
							Cell R13cell1 = row.createCell(1);
							if (record.getR13_ex_rate_buy() != null) {
								R13cell1.setCellValue(record.getR13_ex_rate_buy().doubleValue());
								R13cell1.setCellStyle(numberStyle);
							} else {
								R13cell1.setCellValue("");
								R13cell1.setCellStyle(textStyle);
							}

// R13 Col C
							Cell R13cell2 = row.createCell(2);
							if (record.getR13_ex_rate_mid() != null) {
								R13cell2.setCellValue(record.getR13_ex_rate_mid().doubleValue());
								R13cell2.setCellStyle(numberStyle);
							} else {
								R13cell2.setCellValue("");
								R13cell2.setCellStyle(textStyle);
							}

// R13 Col D
							Cell R13cell3 = row.createCell(3);
							if (record.getR13_ex_rate_sell() != null) {
								R13cell3.setCellValue(record.getR13_ex_rate_sell().doubleValue());
								R13cell3.setCellStyle(numberStyle);
							} else {
								R13cell3.setCellValue("");
								R13cell3.setCellStyle(textStyle);
							}

// R13 Col E
							Cell R13cell4 = row.createCell(4);
							if (record.getR13_current() != null) {
								R13cell4.setCellValue(record.getR13_current().doubleValue());
								R13cell4.setCellStyle(numberStyle);
							} else {
								R13cell4.setCellValue("");
								R13cell1.setCellStyle(textStyle);
							}

// R13 Col F
							Cell R13cell5 = row.createCell(5);
							if (record.getR13_call() != null) {
								R13cell5.setCellValue(record.getR13_call().doubleValue());
								R13cell5.setCellStyle(numberStyle);
							} else {
								R13cell5.setCellValue("");
								R13cell5.setCellStyle(textStyle);
							}

// R13 Col G
							Cell R13cell6 = row.createCell(6);
							if (record.getR13_savings() != null) {
								R13cell6.setCellValue(record.getR13_savings().doubleValue());
								R13cell6.setCellStyle(numberStyle);
							} else {
								R13cell6.setCellValue("");
								R13cell6.setCellStyle(textStyle);

							}

// R13 Col H
							Cell R13cell7 = row.createCell(7);
							if (record.getR13_notice_0to31() != null) {
								R13cell7.setCellValue(record.getR13_notice_0to31().doubleValue());
								R13cell7.setCellStyle(numberStyle);
							} else {
								R13cell7.setCellValue("");
								R13cell7.setCellStyle(textStyle);

							}

// R13 Col I
							Cell R13cell8 = row.createCell(8);
							if (record.getR13_notice_32to88() != null) {
								R13cell8.setCellValue(record.getR13_notice_32to88().doubleValue());
								R13cell8.setCellStyle(numberStyle);
							} else {
								R13cell8.setCellValue("");
								R13cell8.setCellStyle(textStyle);
							}

// R13 Col J
							Cell R13cell9 = row.createCell(9);
							if (record.getR13_fix_depo_91_day_depo() != null) {
								R13cell9.setCellValue(record.getR13_fix_depo_91_day_depo().doubleValue());
								R13cell9.setCellStyle(numberStyle);
							} else {
								R13cell9.setCellValue("");
								R13cell9.setCellStyle(textStyle);
							}

// R13 Col K
							Cell R13cell10 = row.createCell(10);
							if (record.getR13_fix_depo_1to2() != null) {
								R13cell10.setCellValue(record.getR13_fix_depo_1to2().doubleValue());
								R13cell10.setCellStyle(numberStyle);
							} else {
								R13cell10.setCellValue("");
								R13cell10.setCellStyle(textStyle);
							}

// R13 Col L
							Cell R13cell11 = row.createCell(11);
							if (record.getR13_fix_depo_4to6() != null) {
								R13cell11.setCellValue(record.getR13_fix_depo_4to6().doubleValue());
								R13cell11.setCellStyle(numberStyle);
							} else {
								R13cell11.setCellValue("");
								R13cell11.setCellStyle(textStyle);
							}

// R13 Col M
							Cell R13cell12 = row.createCell(12);
							if (record.getR13_fix_depo_7to12() != null) {
								R13cell12.setCellValue(record.getR13_fix_depo_7to12().doubleValue());
								R13cell12.setCellStyle(numberStyle);
							} else {
								R13cell12.setCellValue("");
								R13cell12.setCellStyle(textStyle);
							}

// R13 Col N
							Cell R13cell13 = row.createCell(13);
							if (record.getR13_fix_depo_13to18() != null) {
								R13cell13.setCellValue(record.getR13_fix_depo_13to18().doubleValue());
								R13cell13.setCellStyle(numberStyle);
							} else {
								R13cell13.setCellValue("");
								R13cell13.setCellStyle(textStyle);
							}

// R13 Col O
							Cell R13cell14 = row.createCell(14);
							if (record.getR13_fix_depo_19to24() != null) {
								R13cell14.setCellValue(record.getR13_fix_depo_19to24().doubleValue());
								R13cell14.setCellStyle(numberStyle);
							} else {
								R13cell14.setCellValue("");
								R13cell14.setCellStyle(textStyle);
							}

// R13 Col P
							Cell R13cell15 = row.createCell(15);
							if (record.getR13_fix_depo_over24() != null) {
								R13cell15.setCellValue(record.getR13_fix_depo_over24().doubleValue());
								R13cell15.setCellStyle(numberStyle);
							} else {
								R13cell15.setCellValue("");
								R13cell15.setCellStyle(textStyle);
							}

// R13 Col Q
							Cell R13cell16 = row.createCell(16);
							if (record.getR13_cer_of_depo() != null) {
								R13cell16.setCellValue(record.getR13_cer_of_depo().doubleValue());
								R13cell16.setCellStyle(numberStyle);
							} else {
								R13cell16.setCellValue("");
								R13cell16.setCellStyle(textStyle);
							}

// R13 Col S
							Cell R13cell18 = row.createCell(18);
							if (record.getR13_pula_equivalent() != null) {
								R13cell18.setCellValue(record.getR13_pula_equivalent().doubleValue());
								R13cell18.setCellStyle(numberStyle);
							} else {
								R13cell18.setCellValue("");
								R13cell18.setCellStyle(textStyle);
							}

// R13 Col T
//							Cell R13cell19 = row.createCell(19);
//							if (record.getR13_avg_pula_equivalent() != null) {
//								R13cell19.setCellValue(record.getR13_avg_pula_equivalent().doubleValue());
//								R13cell19.setCellStyle(numberStyle);
//							} else {
//								R13cell19.setCellValue("");
//								R13cell19.setCellStyle(textStyle);
//							}

// R14 Col B
							row = sheet.getRow(13);
							Cell R14cell1 = row.createCell(1);
							if (record.getR14_ex_rate_buy() != null) {
								R14cell1.setCellValue(record.getR14_ex_rate_buy().doubleValue());
								R14cell1.setCellStyle(numberStyle);
							} else {
								R14cell1.setCellValue("");
								R14cell1.setCellStyle(textStyle);
							}

// R14 Col C
							Cell R14cell2 = row.createCell(2);
							if (record.getR14_ex_rate_mid() != null) {
								R14cell2.setCellValue(record.getR14_ex_rate_mid().doubleValue());
								R14cell2.setCellStyle(numberStyle);
							} else {
								R14cell2.setCellValue("");
								R14cell2.setCellStyle(textStyle);
							}

// R14 Col D
							Cell R14cell3 = row.createCell(3);
							if (record.getR14_ex_rate_sell() != null) {
								R14cell3.setCellValue(record.getR14_ex_rate_sell().doubleValue());
								R14cell3.setCellStyle(numberStyle);
							} else {
								R14cell3.setCellValue("");
								R14cell3.setCellStyle(textStyle);
							}

// R14 Col E
							Cell R14cell4 = row.createCell(4);
							if (record.getR14_current() != null) {
								R14cell4.setCellValue(record.getR14_current().doubleValue());
								R14cell4.setCellStyle(numberStyle);
							} else {
								R14cell4.setCellValue("");
								R14cell1.setCellStyle(textStyle);
							}

// R14 Col F
							Cell R14cell5 = row.createCell(5);
							if (record.getR14_call() != null) {
								R14cell5.setCellValue(record.getR14_call().doubleValue());
								R14cell5.setCellStyle(numberStyle);
							} else {
								R14cell5.setCellValue("");
								R14cell5.setCellStyle(textStyle);
							}

// R14 Col G
							Cell R14cell6 = row.createCell(6);
							if (record.getR14_savings() != null) {
								R14cell6.setCellValue(record.getR14_savings().doubleValue());
								R14cell6.setCellStyle(numberStyle);
							} else {
								R14cell6.setCellValue("");
								R14cell6.setCellStyle(textStyle);

							}

// R14 Col H
							Cell R14cell7 = row.createCell(7);
							if (record.getR14_notice_0to31() != null) {
								R14cell7.setCellValue(record.getR14_notice_0to31().doubleValue());
								R14cell7.setCellStyle(numberStyle);
							} else {
								R14cell7.setCellValue("");
								R14cell7.setCellStyle(textStyle);

							}

// R14 Col I
							Cell R14cell8 = row.createCell(8);
							if (record.getR14_notice_32to88() != null) {
								R14cell8.setCellValue(record.getR14_notice_32to88().doubleValue());
								R14cell8.setCellStyle(numberStyle);
							} else {
								R14cell8.setCellValue("");
								R14cell8.setCellStyle(textStyle);
							}

// R14 Col J
							Cell R14cell9 = row.createCell(9);
							if (record.getR14_fix_depo_91_day_depo() != null) {
								R14cell9.setCellValue(record.getR14_fix_depo_91_day_depo().doubleValue());
								R14cell9.setCellStyle(numberStyle);
							} else {
								R14cell9.setCellValue("");
								R14cell9.setCellStyle(textStyle);
							}

// R14 Col K
							Cell R14cell10 = row.createCell(10);
							if (record.getR14_fix_depo_1to2() != null) {
								R14cell10.setCellValue(record.getR14_fix_depo_1to2().doubleValue());
								R14cell10.setCellStyle(numberStyle);
							} else {
								R14cell10.setCellValue("");
								R14cell10.setCellStyle(textStyle);
							}

// R14 Col L
							Cell R14cell11 = row.createCell(11);
							if (record.getR14_fix_depo_4to6() != null) {
								R14cell11.setCellValue(record.getR14_fix_depo_4to6().doubleValue());
								R14cell11.setCellStyle(numberStyle);
							} else {
								R14cell11.setCellValue("");
								R14cell11.setCellStyle(textStyle);
							}

// R14 Col M
							Cell R14cell12 = row.createCell(12);
							if (record.getR14_fix_depo_7to12() != null) {
								R14cell12.setCellValue(record.getR14_fix_depo_7to12().doubleValue());
								R14cell12.setCellStyle(numberStyle);
							} else {
								R14cell12.setCellValue("");
								R14cell12.setCellStyle(textStyle);
							}

// R14 Col N
							Cell R14cell13 = row.createCell(13);
							if (record.getR14_fix_depo_13to18() != null) {
								R14cell13.setCellValue(record.getR14_fix_depo_13to18().doubleValue());
								R14cell13.setCellStyle(numberStyle);
							} else {
								R14cell13.setCellValue("");
								R14cell13.setCellStyle(textStyle);
							}

// R14 Col O
							Cell R14cell14 = row.createCell(14);
							if (record.getR14_fix_depo_19to24() != null) {
								R14cell14.setCellValue(record.getR14_fix_depo_19to24().doubleValue());
								R14cell14.setCellStyle(numberStyle);
							} else {
								R14cell14.setCellValue("");
								R14cell14.setCellStyle(textStyle);
							}

// R14 Col P
							Cell R14cell15 = row.createCell(15);
							if (record.getR14_fix_depo_over24() != null) {
								R14cell15.setCellValue(record.getR14_fix_depo_over24().doubleValue());
								R14cell15.setCellStyle(numberStyle);
							} else {
								R14cell15.setCellValue("");
								R14cell15.setCellStyle(textStyle);
							}

// R14 Col Q
							Cell R14cell16 = row.createCell(16);
							if (record.getR14_cer_of_depo() != null) {
								R14cell16.setCellValue(record.getR14_cer_of_depo().doubleValue());
								R14cell16.setCellStyle(numberStyle);
							} else {
								R14cell16.setCellValue("");
								R14cell16.setCellStyle(textStyle);
							}

// R14 Col S
							Cell R14cell18 = row.createCell(18);
							if (record.getR14_pula_equivalent() != null) {
								R14cell18.setCellValue(record.getR14_pula_equivalent().doubleValue());
								R14cell18.setCellStyle(numberStyle);
							} else {
								R14cell18.setCellValue("");
								R14cell18.setCellStyle(textStyle);
							}

// R14 Col T
//							Cell R14cell19 = row.createCell(19);
//							if (record.getR14_avg_pula_equivalent() != null) {
//								R14cell19.setCellValue(record.getR14_avg_pula_equivalent().doubleValue());
//								R14cell19.setCellStyle(numberStyle);
//							} else {
//								R14cell19.setCellValue("");
//								R14cell19.setCellStyle(textStyle);
//							}

// R15 Col B
							row = sheet.getRow(14);
							Cell R15cell1 = row.createCell(1);
							if (record.getR15_ex_rate_buy() != null) {
								R15cell1.setCellValue(record.getR15_ex_rate_buy().doubleValue());
								R15cell1.setCellStyle(numberStyle);
							} else {
								R15cell1.setCellValue("");
								R15cell1.setCellStyle(textStyle);
							}

// R15 Col C
							Cell R15cell2 = row.createCell(2);
							if (record.getR15_ex_rate_mid() != null) {
								R15cell2.setCellValue(record.getR15_ex_rate_mid().doubleValue());
								R15cell2.setCellStyle(numberStyle);
							} else {
								R15cell2.setCellValue("");
								R15cell2.setCellStyle(textStyle);
							}

// R15 Col D
							Cell R15cell3 = row.createCell(3);
							if (record.getR15_ex_rate_sell() != null) {
								R15cell3.setCellValue(record.getR15_ex_rate_sell().doubleValue());
								R15cell3.setCellStyle(numberStyle);
							} else {
								R15cell3.setCellValue("");
								R15cell3.setCellStyle(textStyle);
							}

// R15 Col E
							Cell R15cell4 = row.createCell(4);
							if (record.getR15_current() != null) {
								R15cell4.setCellValue(record.getR15_current().doubleValue());
								R15cell4.setCellStyle(numberStyle);
							} else {
								R15cell4.setCellValue("");
								R15cell1.setCellStyle(textStyle);
							}

// R15 Col F
							Cell R15cell5 = row.createCell(5);
							if (record.getR15_call() != null) {
								R15cell5.setCellValue(record.getR15_call().doubleValue());
								R15cell5.setCellStyle(numberStyle);
							} else {
								R15cell5.setCellValue("");
								R15cell5.setCellStyle(textStyle);
							}

// R15 Col G
							Cell R15cell6 = row.createCell(6);
							if (record.getR15_savings() != null) {
								R15cell6.setCellValue(record.getR15_savings().doubleValue());
								R15cell6.setCellStyle(numberStyle);
							} else {
								R15cell6.setCellValue("");
								R15cell6.setCellStyle(textStyle);

							}

// R15 Col H
							Cell R15cell7 = row.createCell(7);
							if (record.getR15_notice_0to31() != null) {
								R15cell7.setCellValue(record.getR15_notice_0to31().doubleValue());
								R15cell7.setCellStyle(numberStyle);
							} else {
								R15cell7.setCellValue("");
								R15cell7.setCellStyle(textStyle);

							}

// R15 Col I
							Cell R15cell8 = row.createCell(8);
							if (record.getR15_notice_32to88() != null) {
								R15cell8.setCellValue(record.getR15_notice_32to88().doubleValue());
								R15cell8.setCellStyle(numberStyle);
							} else {
								R15cell8.setCellValue("");
								R15cell8.setCellStyle(textStyle);
							}

// R15 Col J
							Cell R15cell9 = row.createCell(9);
							if (record.getR15_fix_depo_91_day_depo() != null) {
								R15cell9.setCellValue(record.getR15_fix_depo_91_day_depo().doubleValue());
								R15cell9.setCellStyle(numberStyle);
							} else {
								R15cell9.setCellValue("");
								R15cell9.setCellStyle(textStyle);
							}

// R15 Col K
							Cell R15cell10 = row.createCell(10);
							if (record.getR15_fix_depo_1to2() != null) {
								R15cell10.setCellValue(record.getR15_fix_depo_1to2().doubleValue());
								R15cell10.setCellStyle(numberStyle);
							} else {
								R15cell10.setCellValue("");
								R15cell10.setCellStyle(textStyle);
							}

// R15 Col L
							Cell R15cell11 = row.createCell(11);
							if (record.getR15_fix_depo_4to6() != null) {
								R15cell11.setCellValue(record.getR15_fix_depo_4to6().doubleValue());
								R15cell11.setCellStyle(numberStyle);
							} else {
								R15cell11.setCellValue("");
								R15cell11.setCellStyle(textStyle);
							}

// R15 Col M
							Cell R15cell12 = row.createCell(12);
							if (record.getR15_fix_depo_7to12() != null) {
								R15cell12.setCellValue(record.getR15_fix_depo_7to12().doubleValue());
								R15cell12.setCellStyle(numberStyle);
							} else {
								R15cell12.setCellValue("");
								R15cell12.setCellStyle(textStyle);
							}

// R15 Col N
							Cell R15cell13 = row.createCell(13);
							if (record.getR15_fix_depo_13to18() != null) {
								R15cell13.setCellValue(record.getR15_fix_depo_13to18().doubleValue());
								R15cell13.setCellStyle(numberStyle);
							} else {
								R15cell13.setCellValue("");
								R15cell13.setCellStyle(textStyle);
							}

// R15 Col O
							Cell R15cell14 = row.createCell(14);
							if (record.getR15_fix_depo_19to24() != null) {
								R15cell14.setCellValue(record.getR15_fix_depo_19to24().doubleValue());
								R15cell14.setCellStyle(numberStyle);
							} else {
								R15cell14.setCellValue("");
								R15cell14.setCellStyle(textStyle);
							}

// R15 Col P
							Cell R15cell15 = row.createCell(15);
							if (record.getR15_fix_depo_over24() != null) {
								R15cell15.setCellValue(record.getR15_fix_depo_over24().doubleValue());
								R15cell15.setCellStyle(numberStyle);
							} else {
								R15cell15.setCellValue("");
								R15cell15.setCellStyle(textStyle);
							}

// R15 Col Q
							Cell R15cell16 = row.createCell(16);
							if (record.getR15_cer_of_depo() != null) {
								R15cell16.setCellValue(record.getR15_cer_of_depo().doubleValue());
								R15cell16.setCellStyle(numberStyle);
							} else {
								R15cell16.setCellValue("");
								R15cell16.setCellStyle(textStyle);
							}

// R15 Col S
							Cell R15cell18 = row.createCell(18);
							if (record.getR15_pula_equivalent() != null) {
								R15cell18.setCellValue(record.getR15_pula_equivalent().doubleValue());
								R15cell18.setCellStyle(numberStyle);
							} else {
								R15cell18.setCellValue("");
								R15cell18.setCellStyle(textStyle);
							}

// R15 Col T
//							Cell R15cell19 = row.createCell(19);
//							if (record.getR15_avg_pula_equivalent() != null) {
//								R15cell19.setCellValue(record.getR15_avg_pula_equivalent().doubleValue());
//								R15cell19.setCellStyle(numberStyle);
//							} else {
//								R15cell19.setCellValue("");
//								R15cell19.setCellStyle(textStyle);
//							}

// R16 Col B
							row = sheet.getRow(15);
							Cell R16cell1 = row.createCell(1);
							if (record.getR16_ex_rate_buy() != null) {
								R16cell1.setCellValue(record.getR16_ex_rate_buy().doubleValue());
								R16cell1.setCellStyle(numberStyle);
							} else {
								R16cell1.setCellValue("");
								R16cell1.setCellStyle(textStyle);
							}

// R16 Col C
							Cell R16cell2 = row.createCell(2);
							if (record.getR16_ex_rate_mid() != null) {
								R16cell2.setCellValue(record.getR16_ex_rate_mid().doubleValue());
								R16cell2.setCellStyle(numberStyle);
							} else {
								R16cell2.setCellValue("");
								R16cell2.setCellStyle(textStyle);
							}

// R16 Col D
							Cell R16cell3 = row.createCell(3);
							if (record.getR16_ex_rate_sell() != null) {
								R16cell3.setCellValue(record.getR16_ex_rate_sell().doubleValue());
								R16cell3.setCellStyle(numberStyle);
							} else {
								R16cell3.setCellValue("");
								R16cell3.setCellStyle(textStyle);
							}

// R16 Col E
							Cell R16cell4 = row.createCell(4);
							if (record.getR16_current() != null) {
								R16cell4.setCellValue(record.getR16_current().doubleValue());
								R16cell4.setCellStyle(numberStyle);
							} else {
								R16cell4.setCellValue("");
								R16cell1.setCellStyle(textStyle);
							}

// R16 Col F
							Cell R16cell5 = row.createCell(5);
							if (record.getR16_call() != null) {
								R16cell5.setCellValue(record.getR16_call().doubleValue());
								R16cell5.setCellStyle(numberStyle);
							} else {
								R16cell5.setCellValue("");
								R16cell5.setCellStyle(textStyle);
							}

// R16 Col G
							Cell R16cell6 = row.createCell(6);
							if (record.getR16_savings() != null) {
								R16cell6.setCellValue(record.getR16_savings().doubleValue());
								R16cell6.setCellStyle(numberStyle);
							} else {
								R16cell6.setCellValue("");
								R16cell6.setCellStyle(textStyle);

							}

// R16 Col H
							Cell R16cell7 = row.createCell(7);
							if (record.getR16_notice_0to31() != null) {
								R16cell7.setCellValue(record.getR16_notice_0to31().doubleValue());
								R16cell7.setCellStyle(numberStyle);
							} else {
								R16cell7.setCellValue("");
								R16cell7.setCellStyle(textStyle);

							}

// R16 Col I
							Cell R16cell8 = row.createCell(8);
							if (record.getR16_notice_32to88() != null) {
								R16cell8.setCellValue(record.getR16_notice_32to88().doubleValue());
								R16cell8.setCellStyle(numberStyle);
							} else {
								R16cell8.setCellValue("");
								R16cell8.setCellStyle(textStyle);
							}

// R16 Col J
							Cell R16cell9 = row.createCell(9);
							if (record.getR16_fix_depo_91_day_depo() != null) {
								R16cell9.setCellValue(record.getR16_fix_depo_91_day_depo().doubleValue());
								R16cell9.setCellStyle(numberStyle);
							} else {
								R16cell9.setCellValue("");
								R16cell9.setCellStyle(textStyle);
							}

// R16 Col K
							Cell R16cell10 = row.createCell(10);
							if (record.getR16_fix_depo_1to2() != null) {
								R16cell10.setCellValue(record.getR16_fix_depo_1to2().doubleValue());
								R16cell10.setCellStyle(numberStyle);
							} else {
								R16cell10.setCellValue("");
								R16cell10.setCellStyle(textStyle);
							}

// R16 Col L
							Cell R16cell11 = row.createCell(11);
							if (record.getR16_fix_depo_4to6() != null) {
								R16cell11.setCellValue(record.getR16_fix_depo_4to6().doubleValue());
								R16cell11.setCellStyle(numberStyle);
							} else {
								R16cell11.setCellValue("");
								R16cell11.setCellStyle(textStyle);
							}

// R16 Col M
							Cell R16cell12 = row.createCell(12);
							if (record.getR16_fix_depo_7to12() != null) {
								R16cell12.setCellValue(record.getR16_fix_depo_7to12().doubleValue());
								R16cell12.setCellStyle(numberStyle);
							} else {
								R16cell12.setCellValue("");
								R16cell12.setCellStyle(textStyle);
							}

// R16 Col N
							Cell R16cell13 = row.createCell(13);
							if (record.getR16_fix_depo_13to18() != null) {
								R16cell13.setCellValue(record.getR16_fix_depo_13to18().doubleValue());
								R16cell13.setCellStyle(numberStyle);
							} else {
								R16cell13.setCellValue("");
								R16cell13.setCellStyle(textStyle);
							}

// R16 Col O
							Cell R16cell14 = row.createCell(14);
							if (record.getR16_fix_depo_19to24() != null) {
								R16cell14.setCellValue(record.getR16_fix_depo_19to24().doubleValue());
								R16cell14.setCellStyle(numberStyle);
							} else {
								R16cell14.setCellValue("");
								R16cell14.setCellStyle(textStyle);
							}

// R16 Col P
							Cell R16cell15 = row.createCell(15);
							if (record.getR16_fix_depo_over24() != null) {
								R16cell15.setCellValue(record.getR16_fix_depo_over24().doubleValue());
								R16cell15.setCellStyle(numberStyle);
							} else {
								R16cell15.setCellValue("");
								R16cell15.setCellStyle(textStyle);

							}

// R16 Col Q
							Cell R16cell16 = row.createCell(16);
							if (record.getR16_cer_of_depo() != null) {
								R16cell16.setCellValue(record.getR16_cer_of_depo().doubleValue());
								R16cell16.setCellStyle(numberStyle);
							} else {
								R16cell16.setCellValue("");
								R16cell16.setCellStyle(textStyle);
							}

// R16 Col S
							Cell R16cell18 = row.createCell(18);
							if (record.getR16_pula_equivalent() != null) {
								R16cell18.setCellValue(record.getR16_pula_equivalent().doubleValue());
								R16cell18.setCellStyle(numberStyle);
							} else {
								R16cell18.setCellValue("");
								R16cell18.setCellStyle(textStyle);
							}

// R16 Col T
//							Cell R16cell19 = row.createCell(19);
//							if (record.getR16_avg_pula_equivalent() != null) {
//								R16cell19.setCellValue(record.getR16_avg_pula_equivalent().doubleValue());
//								R16cell19.setCellStyle(numberStyle);
//							} else {
//								R16cell19.setCellValue("");
//								R16cell19.setCellStyle(textStyle);
//							}

// R17 Col B
							row = sheet.getRow(16);
							Cell R17cell1 = row.createCell(1);
							if (record.getR17_ex_rate_buy() != null) {
								R17cell1.setCellValue(record.getR17_ex_rate_buy().doubleValue());
								R17cell1.setCellStyle(numberStyle);
							} else {
								R17cell1.setCellValue("");
								R17cell1.setCellStyle(textStyle);
							}

// R17 Col C
							Cell R17cell2 = row.createCell(2);
							if (record.getR17_ex_rate_mid() != null) {
								R17cell2.setCellValue(record.getR17_ex_rate_mid().doubleValue());
								R17cell2.setCellStyle(numberStyle);
							} else {
								R17cell2.setCellValue("");
								R17cell2.setCellStyle(textStyle);
							}

// R17 Col D
							Cell R17cell3 = row.createCell(3);
							if (record.getR17_ex_rate_sell() != null) {
								R17cell3.setCellValue(record.getR17_ex_rate_sell().doubleValue());
								R17cell3.setCellStyle(numberStyle);
							} else {
								R17cell3.setCellValue("");
								R17cell3.setCellStyle(textStyle);
							}

// R18 Col B
							row = sheet.getRow(17);
							Cell R18cell1 = row.createCell(1);
							if (record.getR18_ex_rate_buy() != null) {
								R18cell1.setCellValue(record.getR18_ex_rate_buy().doubleValue());
								R18cell1.setCellStyle(numberStyle);
							} else {
								R18cell1.setCellValue("");
								R18cell1.setCellStyle(textStyle);
							}

// R18 Col C
							Cell R18cell2 = row.createCell(2);
							if (record.getR18_ex_rate_mid() != null) {
								R18cell2.setCellValue(record.getR18_ex_rate_mid().doubleValue());
								R18cell2.setCellStyle(numberStyle);
							} else {
								R18cell2.setCellValue("");
								R18cell2.setCellStyle(textStyle);
							}

// R18 Col D
							Cell R18cell3 = row.createCell(3);
							if (record.getR18_ex_rate_sell() != null) {
								R18cell3.setCellValue(record.getR18_ex_rate_sell().doubleValue());
								R18cell3.setCellStyle(numberStyle);
							} else {
								R18cell3.setCellValue("");
								R18cell3.setCellStyle(textStyle);
							}

// R18 Col E
							Cell R18cell4 = row.createCell(4);
							if (record.getR18_current() != null) {
								R18cell4.setCellValue(record.getR18_current().doubleValue());
								R18cell4.setCellStyle(numberStyle);
							} else {
								R18cell4.setCellValue("");
								R18cell1.setCellStyle(textStyle);
							}

// R18 Col F
							Cell R18cell5 = row.createCell(5);
							if (record.getR18_call() != null) {
								R18cell5.setCellValue(record.getR18_call().doubleValue());
								R18cell5.setCellStyle(numberStyle);
							} else {
								R18cell5.setCellValue("");
								R18cell5.setCellStyle(textStyle);
							}

// R18 Col G
							Cell R18cell6 = row.createCell(6);
							if (record.getR18_savings() != null) {
								R18cell6.setCellValue(record.getR18_savings().doubleValue());
								R18cell6.setCellStyle(numberStyle);
							} else {
								R18cell6.setCellValue("");
								R18cell6.setCellStyle(textStyle);

							}

// R18 Col H
							Cell R18cell7 = row.createCell(7);
							if (record.getR18_notice_0to31() != null) {
								R18cell7.setCellValue(record.getR18_notice_0to31().doubleValue());
								R18cell7.setCellStyle(numberStyle);
							} else {
								R18cell7.setCellValue("");
								R18cell7.setCellStyle(textStyle);

							}

// R18 Col I
							Cell R18cell8 = row.createCell(8);
							if (record.getR18_notice_32to88() != null) {
								R18cell8.setCellValue(record.getR18_notice_32to88().doubleValue());
								R18cell8.setCellStyle(numberStyle);
							} else {
								R18cell8.setCellValue("");
								R18cell8.setCellStyle(textStyle);
							}

// R18 Col J
							Cell R18cell9 = row.createCell(9);
							if (record.getR18_fix_depo_91_day_depo() != null) {
								R18cell9.setCellValue(record.getR18_fix_depo_91_day_depo().doubleValue());
								R18cell9.setCellStyle(numberStyle);
							} else {
								R18cell9.setCellValue("");
								R18cell9.setCellStyle(textStyle);
							}

// R18 Col K
							Cell R18cell10 = row.createCell(10);
							if (record.getR18_fix_depo_1to2() != null) {
								R18cell10.setCellValue(record.getR18_fix_depo_1to2().doubleValue());
								R18cell10.setCellStyle(numberStyle);
							} else {
								R18cell10.setCellValue("");
								R18cell10.setCellStyle(textStyle);
							}

// R18 Col L
							Cell R18cell11 = row.createCell(11);
							if (record.getR18_fix_depo_4to6() != null) {
								R18cell11.setCellValue(record.getR18_fix_depo_4to6().doubleValue());
								R18cell11.setCellStyle(numberStyle);
							} else {
								R18cell11.setCellValue("");
								R18cell11.setCellStyle(textStyle);
							}

// R18 Col M
							Cell R18cell12 = row.createCell(12);
							if (record.getR18_fix_depo_7to12() != null) {
								R18cell12.setCellValue(record.getR18_fix_depo_7to12().doubleValue());
								R18cell12.setCellStyle(numberStyle);
							} else {
								R18cell12.setCellValue("");
								R18cell12.setCellStyle(textStyle);
							}

// R18 Col N
							Cell R18cell13 = row.createCell(13);
							if (record.getR18_fix_depo_13to18() != null) {
								R18cell13.setCellValue(record.getR18_fix_depo_13to18().doubleValue());
								R18cell13.setCellStyle(numberStyle);
							} else {
								R18cell13.setCellValue("");
								R18cell13.setCellStyle(textStyle);
							}

// R18 Col O
							Cell R18cell14 = row.createCell(14);
							if (record.getR18_fix_depo_19to24() != null) {
								R18cell14.setCellValue(record.getR18_fix_depo_19to24().doubleValue());
								R18cell14.setCellStyle(numberStyle);
							} else {
								R18cell14.setCellValue("");
								R18cell14.setCellStyle(textStyle);
							}

// R18 Col P
							Cell R18cell15 = row.createCell(15);
							if (record.getR18_fix_depo_over24() != null) {
								R18cell15.setCellValue(record.getR18_fix_depo_over24().doubleValue());
								R18cell15.setCellStyle(numberStyle);
							} else {
								R18cell15.setCellValue("");
								R18cell15.setCellStyle(textStyle);
							}

// R18 Col Q
							Cell R18cell16 = row.createCell(16);
							if (record.getR18_cer_of_depo() != null) {
								R18cell16.setCellValue(record.getR18_cer_of_depo().doubleValue());
								R18cell16.setCellStyle(numberStyle);
							} else {
								R18cell16.setCellValue("");
								R18cell16.setCellStyle(textStyle);
							}
// R18 Col R
							Cell R18cell17 = row.createCell(17);
							if (record.getR18_total() != null) {
								R18cell17.setCellValue(record.getR18_total().doubleValue());
								R18cell17.setCellStyle(numberStyle);
							} else {
								R18cell17.setCellValue("");
								R18cell17.setCellStyle(textStyle);
							}
// R18 Col S
							Cell R18cell18 = row.createCell(18);
							if (record.getR18_pula_equivalent() != null) {
								R18cell18.setCellValue(record.getR18_pula_equivalent().doubleValue());
								R18cell18.setCellStyle(numberStyle);
							} else {
								R18cell18.setCellValue("");
								R18cell18.setCellStyle(textStyle);
							}
// R18 Col S
//							Cell R18cell19 = row.createCell(19);
//							if (record.getR18_avg_pula_equivalent() != null) {
//								R18cell19.setCellValue(record.getR18_avg_pula_equivalent().doubleValue());
//								R18cell18.setCellStyle(numberStyle);
//							} else {
//								R18cell19.setCellValue("");
//								R18cell19.setCellStyle(textStyle);
//							}

//Entity 2
// R28 Col B
							row = sheet.getRow(27);
							Cell R28cell1 = row.createCell(1);
							if (record.getR28_import() != null) {
								R28cell1.setCellValue(record.getR28_import().doubleValue());
								R28cell1.setCellStyle(numberStyle);
							} else {
								R28cell1.setCellValue("");
								R28cell1.setCellStyle(textStyle);
							}
// R28 Col C
							Cell R28cell2 = row.createCell(2);
							if (record.getR28_investment() != null) {
								R28cell2.setCellValue(record.getR28_investment().doubleValue());
								R28cell2.setCellStyle(numberStyle);
							} else {
								R28cell2.setCellValue("");
								R28cell2.setCellStyle(textStyle);
							}
// R28 Col D
							Cell R28cell3 = row.createCell(3);
							if (record.getR28_other() != null) {
								R28cell3.setCellValue(record.getR28_other().doubleValue());
								R28cell3.setCellStyle(numberStyle);
							} else {
								R28cell3.setCellValue("");
								R28cell3.setCellStyle(textStyle);
							}
// R29 Col B
							row = sheet.getRow(28);
							Cell R29cell1 = row.createCell(1);
							if (record.getR29_import() != null) {
								R29cell1.setCellValue(record.getR29_import().doubleValue());
								R29cell1.setCellStyle(numberStyle);
							} else {
								R29cell1.setCellValue("");
								R29cell1.setCellStyle(textStyle);
							}
// R29 Col C
							Cell R29cell2 = row.createCell(2);
							if (record.getR29_investment() != null) {
								R29cell2.setCellValue(record.getR29_investment().doubleValue());
								R29cell2.setCellStyle(numberStyle);
							} else {
								R29cell2.setCellValue("");
								R29cell2.setCellStyle(textStyle);
							}
// R29 Col D
							Cell R29cell3 = row.createCell(3);
							if (record.getR29_other() != null) {
								R29cell3.setCellValue(record.getR29_other().doubleValue());
								R29cell3.setCellStyle(numberStyle);
							} else {
								R29cell3.setCellValue("");
								R29cell3.setCellStyle(textStyle);
							}

// R30 Col B
							row = sheet.getRow(29);
							Cell R30cell1 = row.createCell(1);
							if (record.getR30_import() != null) {
								R30cell1.setCellValue(record.getR30_import().doubleValue());
								R30cell1.setCellStyle(numberStyle);
							} else {
								R30cell1.setCellValue("");
								R30cell1.setCellStyle(textStyle);
							}
// R30 Col C
							Cell R30cell2 = row.createCell(2);
							if (record.getR30_investment() != null) {
								R30cell2.setCellValue(record.getR30_investment().doubleValue());
								R30cell2.setCellStyle(numberStyle);
							} else {
								R30cell2.setCellValue("");
								R30cell2.setCellStyle(textStyle);
							}
// R30 Col D
							Cell R30cell3 = row.createCell(3);
							if (record.getR30_other() != null) {
								R30cell3.setCellValue(record.getR30_other().doubleValue());
								R30cell3.setCellStyle(numberStyle);
							} else {
								R30cell3.setCellValue("");
								R30cell3.setCellStyle(textStyle);
							}
// R31 Col B
							row = sheet.getRow(30);
							Cell R31cell1 = row.createCell(1);
							if (record.getR31_import() != null) {
								R31cell1.setCellValue(record.getR31_import().doubleValue());
								R31cell1.setCellStyle(numberStyle);
							} else {
								R31cell1.setCellValue("");
								R31cell1.setCellStyle(textStyle);
							}
// R31 Col C
							Cell R31cell2 = row.createCell(2);
							if (record.getR31_investment() != null) {
								R31cell2.setCellValue(record.getR31_investment().doubleValue());
								R31cell2.setCellStyle(numberStyle);
							} else {
								R31cell2.setCellValue("");
								R31cell2.setCellStyle(textStyle);
							}
// R31 Col D
							Cell R31cell3 = row.createCell(3);
							if (record.getR31_other() != null) {
								R31cell3.setCellValue(record.getR31_other().doubleValue());
								R31cell3.setCellStyle(numberStyle);
							} else {
								R31cell3.setCellValue("");
								R31cell3.setCellStyle(textStyle);
							}
// R32 Col B
							row = sheet.getRow(31);
							Cell R32cell1 = row.createCell(1);
							if (record.getR32_import() != null) {
								R32cell1.setCellValue(record.getR32_import().doubleValue());
								R32cell1.setCellStyle(numberStyle);
							} else {
								R32cell1.setCellValue("");
								R32cell1.setCellStyle(textStyle);
							}
// R32 Col C
							Cell R32cell2 = row.createCell(2);
							if (record.getR32_investment() != null) {
								R32cell2.setCellValue(record.getR32_investment().doubleValue());
								R32cell2.setCellStyle(numberStyle);
							} else {
								R32cell2.setCellValue("");
								R32cell2.setCellStyle(textStyle);
							}
// R32 Col D
							Cell R32cell3 = row.createCell(3);
							if (record.getR32_other() != null) {
								R32cell3.setCellValue(record.getR32_other().doubleValue());
								R32cell3.setCellStyle(numberStyle);
							} else {
								R32cell3.setCellValue("");
								R32cell3.setCellStyle(textStyle);
							}
// R33 Col B
							row = sheet.getRow(32);
							Cell R33cell1 = row.createCell(1);
							if (record.getR33_import() != null) {
								R33cell1.setCellValue(record.getR33_import().doubleValue());
								R33cell1.setCellStyle(numberStyle);
							} else {
								R33cell1.setCellValue("");
								R33cell1.setCellStyle(textStyle);
							}
// R33 Col C
							Cell R33cell2 = row.createCell(2);
							if (record.getR33_investment() != null) {
								R33cell2.setCellValue(record.getR33_investment().doubleValue());
								R33cell2.setCellStyle(numberStyle);
							} else {
								R33cell2.setCellValue("");
								R33cell2.setCellStyle(textStyle);
							}
// R33 Col D
							Cell R33cell3 = row.createCell(3);
							if (record.getR33_other() != null) {
								R33cell3.setCellValue(record.getR33_other().doubleValue());
								R33cell3.setCellStyle(numberStyle);
							} else {
								R33cell3.setCellValue("");
								R33cell3.setCellStyle(textStyle);
							}

//Entity 3

							row = sheet.getRow(27);
							Cell R28cell1e3 = row.createCell(8);
							if (record.getR28_residents() != null) {
								R28cell1e3.setCellValue(record.getR28_residents().doubleValue());
								R28cell1e3.setCellStyle(numberStyle);
							} else {
								R28cell1e3.setCellValue("");
								R28cell1e3.setCellStyle(textStyle);
							}
// R28 Col C
							Cell R28cell2e3 = row.createCell(9);
							if (record.getR28_non_residents() != null) {
								R28cell2e3.setCellValue(record.getR28_non_residents().doubleValue());
								R28cell2e3.setCellStyle(numberStyle);
							} else {
								R28cell2e3.setCellValue("");
								R28cell2e3.setCellStyle(textStyle);
							}
// R29 Col B
							row = sheet.getRow(28);
							Cell R29cell1e3 = row.createCell(8);
							if (record.getR29_residents() != null) {
								R29cell1e3.setCellValue(record.getR29_residents().doubleValue());
								R29cell1e3.setCellStyle(numberStyle);
							} else {
								R29cell1e3.setCellValue("");
								R29cell1e3.setCellStyle(textStyle);
							}
// R29 Col C
							Cell R29cell2e3 = row.createCell(9);
							if (record.getR29_non_residents() != null) {
								R29cell2e3.setCellValue(record.getR29_non_residents().doubleValue());
								R29cell2e3.setCellStyle(numberStyle);
							} else {
								R29cell2e3.setCellValue("");
								R29cell2e3.setCellStyle(textStyle);
							}
// R30 Col B
							row = sheet.getRow(29);
							Cell R30cell1e3 = row.createCell(8);
							if (record.getR30_residents() != null) {
								R30cell1e3.setCellValue(record.getR30_residents().doubleValue());
								R30cell1e3.setCellStyle(numberStyle);
							} else {
								R30cell1e3.setCellValue("");
								R30cell1e3.setCellStyle(textStyle);
							}
// R30 Col C
							Cell R30cell2e3 = row.createCell(9);
							if (record.getR30_non_residents() != null) {
								R30cell2e3.setCellValue(record.getR30_non_residents().doubleValue());
								R30cell2e3.setCellStyle(numberStyle);
							} else {
								R30cell2e3.setCellValue("");
								R30cell2e3.setCellStyle(textStyle);
							}
// R31 Col B
							row = sheet.getRow(30);
							Cell R31cell1e3 = row.createCell(8);
							if (record.getR31_residents() != null) {
								R31cell1e3.setCellValue(record.getR31_residents().doubleValue());
								R31cell1e3.setCellStyle(numberStyle);
							} else {
								R31cell1e3.setCellValue("");
								R31cell1e3.setCellStyle(textStyle);
							}
// R31 Col C
							Cell R31cell2e3 = row.createCell(9);
							if (record.getR31_non_residents() != null) {
								R31cell2e3.setCellValue(record.getR31_non_residents().doubleValue());
								R31cell2e3.setCellStyle(numberStyle);
							} else {
								R31cell2e3.setCellValue("");
								R31cell2e3.setCellStyle(textStyle);
							}

// R32 Col B
							row = sheet.getRow(31);
							Cell R32cell1e3 = row.createCell(8);
							if (record.getR32_residents() != null) {
								R32cell1e3.setCellValue(record.getR32_residents().doubleValue());
								R32cell1e3.setCellStyle(numberStyle);
							} else {
								R32cell1e3.setCellValue("");
								R32cell1e3.setCellStyle(textStyle);
							}
// R32 Col C
							Cell R32cell2e3 = row.createCell(9);
							if (record.getR32_non_residents() != null) {
								R32cell2e3.setCellValue(record.getR32_non_residents().doubleValue());
								R32cell2e3.setCellStyle(numberStyle);
							} else {
								R32cell2e3.setCellValue("");
								R32cell2e3.setCellStyle(textStyle);
							}
// R33 Col B
							row = sheet.getRow(32);
							Cell R33cell1e3 = row.createCell(8);
							if (record.getR33_residents() != null) {
								R33cell1e3.setCellValue(record.getR33_residents().doubleValue());
								R33cell1e3.setCellStyle(numberStyle);
							} else {
								R33cell1e3.setCellValue("");
								R33cell1e3.setCellStyle(textStyle);
							}
// R33 Col C
							Cell R33cell2e3 = row.createCell(9);
							if (record.getR33_non_residents() != null) {
								R33cell2e3.setCellValue(record.getR33_non_residents().doubleValue());
								R33cell2e3.setCellStyle(numberStyle);
							} else {
								R33cell2e3.setCellValue("");
								R33cell2e3.setCellStyle(textStyle);
							}

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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_DEP3 SUMMARY", null,
								"M_DEP3_SUMMARYTABLE");
					}
					return out.toByteArray();
				}
			}
		}
	}

	// Normal Email Excel
	// ------------------------------
	// GENERATE EMAIL EXCEL REPORT
	// ------------------------------
	public byte[] BRRS_M_DEP3EmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_DEP3EmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
//		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
//			logger.info("Service: Generating RESUB report for version {}", version);
//
//			try {
//				// ✅ Redirecting to Resub Excel
//				return BRRS_M_DEP3ResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
//						version);
//
//			} catch (ParseException e) {
//				logger.error("Invalid report date format: {}", fromdate, e);
//				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
//			}
		} else {

			List<M_DEP3_Summary_Entity> dataList = jdbcTemplate.query(
					"SELECT * FROM BRRS_M_DEP3_SUMMARYTABLE WHERE REPORT_DATE = ?",
					new Object[] { dateformat.parse(todate) }, new M_DEP3_Summary_EntityRowMapper());

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_DEP3 report. Returning empty result.");
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
						M_DEP3_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
//EMAIL
						Cell R12Cell = row.createCell(3);

						if (record.getReportDate() != null) {

							R12Cell.setCellValue(record.getReportDate());

							R12Cell.setCellStyle(dateStyle);

						} else {

							R12Cell.setCellValue("");

							R12Cell.setCellStyle(textStyle);
						}
						row = sheet.getRow(10);
						// R11 Col B

						Cell R11cell1 = row.createCell(1);
						if (record.getR11_ex_rate_buy() != null) {
							R11cell1.setCellValue(record.getR11_ex_rate_buy().doubleValue());
							R11cell1.setCellStyle(numberStyle);
						} else {
							R11cell1.setCellValue("");
							R11cell1.setCellStyle(textStyle);
						}

// R11 Col C
						Cell R11cell2 = row.createCell(2);
						if (record.getR11_ex_rate_mid() != null) {
							R11cell2.setCellValue(record.getR11_ex_rate_mid().doubleValue());
							R11cell2.setCellStyle(numberStyle);
						} else {
							R11cell2.setCellValue("");
							R11cell2.setCellStyle(textStyle);
						}

// R11 Col D
						Cell R11cell3 = row.createCell(3);
						if (record.getR11_ex_rate_sell() != null) {
							R11cell3.setCellValue(record.getR11_ex_rate_sell().doubleValue());
							R11cell3.setCellStyle(numberStyle);
						} else {
							R11cell3.setCellValue("");
							R11cell3.setCellStyle(textStyle);
						}

// R11 Col E
						Cell R11cell4 = row.createCell(4);
						if (record.getR11_current() != null) {
							R11cell4.setCellValue(record.getR11_current().doubleValue());
							R11cell4.setCellStyle(numberStyle);
						} else {
							R11cell4.setCellValue("");
							R11cell1.setCellStyle(textStyle);
						}

// R11 Col F
						Cell R11cell5 = row.createCell(5);
						if (record.getR11_call() != null) {
							R11cell5.setCellValue(record.getR11_call().doubleValue());
							R11cell5.setCellStyle(numberStyle);
						} else {
							R11cell5.setCellValue("");
							R11cell5.setCellStyle(textStyle);
						}

// R11 Col G
						Cell R11cell6 = row.createCell(6);
						if (record.getR11_savings() != null) {
							R11cell6.setCellValue(record.getR11_savings().doubleValue());
							R11cell6.setCellStyle(numberStyle);
						} else {
							R11cell6.setCellValue("");
							R11cell6.setCellStyle(textStyle);

						}

// R11 Col H
						Cell R11cell7 = row.createCell(7);
						if (record.getR11_notice_0to31() != null) {
							R11cell7.setCellValue(record.getR11_notice_0to31().doubleValue());
							R11cell7.setCellStyle(numberStyle);
						} else {
							R11cell7.setCellValue("");
							R11cell7.setCellStyle(textStyle);

						}

// R11 Col I
						Cell R11cell8 = row.createCell(8);
						if (record.getR11_notice_32to88() != null) {
							R11cell8.setCellValue(record.getR11_notice_32to88().doubleValue());
							R11cell8.setCellStyle(numberStyle);
						} else {
							R11cell8.setCellValue("");
							R11cell8.setCellStyle(textStyle);
						}

// R11 Col J
						Cell R11cell9 = row.createCell(9);
						if (record.getR11_fix_depo_91_day_depo() != null) {
							R11cell9.setCellValue(record.getR11_fix_depo_91_day_depo().doubleValue());
							R11cell9.setCellStyle(numberStyle);
						} else {
							R11cell9.setCellValue("");
							R11cell9.setCellStyle(textStyle);
						}

// R11 Col K
						Cell R11cell10 = row.createCell(10);
						if (record.getR11_fix_depo_1to2() != null) {
							R11cell10.setCellValue(record.getR11_fix_depo_1to2().doubleValue());
							R11cell10.setCellStyle(numberStyle);
						} else {
							R11cell10.setCellValue("");
							R11cell10.setCellStyle(textStyle);
						}

// R11 Col L
						Cell R11cell11 = row.createCell(11);
						if (record.getR11_fix_depo_4to6() != null) {
							R11cell11.setCellValue(record.getR11_fix_depo_4to6().doubleValue());
							R11cell11.setCellStyle(numberStyle);
						} else {
							R11cell11.setCellValue("");
							R11cell11.setCellStyle(textStyle);
						}

// R11 Col M
						Cell R11cell12 = row.createCell(12);
						if (record.getR11_fix_depo_7to12() != null) {
							R11cell12.setCellValue(record.getR11_fix_depo_7to12().doubleValue());
							R11cell12.setCellStyle(numberStyle);
						} else {
							R11cell12.setCellValue("");
							R11cell12.setCellStyle(textStyle);
						}

// R11 Col N
						Cell R11cell13 = row.createCell(13);
						if (record.getR11_fix_depo_13to18() != null) {
							R11cell13.setCellValue(record.getR11_fix_depo_13to18().doubleValue());
							R11cell13.setCellStyle(numberStyle);
						} else {
							R11cell13.setCellValue("");
							R11cell13.setCellStyle(textStyle);
						}

// R11 Col O
						Cell R11cell14 = row.createCell(14);
						if (record.getR11_fix_depo_19to24() != null) {
							R11cell14.setCellValue(record.getR11_fix_depo_19to24().doubleValue());
							R11cell14.setCellStyle(numberStyle);
						} else {
							R11cell14.setCellValue("");
							R11cell14.setCellStyle(textStyle);
						}

// R11 Col P
						Cell R11cell15 = row.createCell(15);
						if (record.getR11_fix_depo_over24() != null) {
							R11cell15.setCellValue(record.getR11_fix_depo_over24().doubleValue());
							R11cell15.setCellStyle(numberStyle);
						} else {
							R11cell15.setCellValue("");
							R11cell15.setCellStyle(textStyle);
						}

// R11 Col Q
						Cell R11cell16 = row.createCell(16);
						if (record.getR11_cer_of_depo() != null) {
							R11cell16.setCellValue(record.getR11_cer_of_depo().doubleValue());
							R11cell16.setCellStyle(numberStyle);
						} else {
							R11cell16.setCellValue("");
							R11cell16.setCellStyle(textStyle);

						}

// R11 Col S
						Cell R11cell18 = row.createCell(18);
						if (record.getR11_pula_equivalent() != null) {
							R11cell18.setCellValue(record.getR11_pula_equivalent().doubleValue());
							R11cell18.setCellStyle(numberStyle);
						} else {
							R11cell18.setCellValue("");
							R11cell18.setCellStyle(textStyle);
						}

// R11 Col T
						Cell R11cell19 = row.createCell(19);
						if (record.getR11_avg_pula_equivalent() != null) {
							R11cell19.setCellValue(record.getR11_avg_pula_equivalent().doubleValue());
							R11cell19.setCellStyle(numberStyle);
						} else {
							R11cell19.setCellValue("");
							R11cell19.setCellStyle(textStyle);
						}
						row = sheet.getRow(11);
// R12 Col B
						Cell R12cell1 = row.createCell(1);
						if (record.getR12_ex_rate_buy() != null) {
							R12cell1.setCellValue(record.getR12_ex_rate_buy().doubleValue());
							R12cell1.setCellStyle(numberStyle);
						} else {
							R12cell1.setCellValue("");
							R12cell1.setCellStyle(textStyle);
						}

// R12 Col C
						Cell R12cell2 = row.createCell(2);
						if (record.getR12_ex_rate_mid() != null) {
							R12cell2.setCellValue(record.getR12_ex_rate_mid().doubleValue());
							R12cell2.setCellStyle(numberStyle);
						} else {
							R12cell2.setCellValue("");
							R12cell2.setCellStyle(textStyle);
						}

// R12 Col D
						Cell R12cell3 = row.createCell(3);
						if (record.getR12_ex_rate_sell() != null) {
							R12cell3.setCellValue(record.getR12_ex_rate_sell().doubleValue());
							R12cell3.setCellStyle(numberStyle);
						} else {
							R12cell3.setCellValue("");
							R12cell3.setCellStyle(textStyle);
						}

// R12 Col E
						Cell R12cell4 = row.createCell(4);
						if (record.getR12_current() != null) {
							R12cell4.setCellValue(record.getR12_current().doubleValue());
							R12cell4.setCellStyle(numberStyle);
						} else {
							R12cell4.setCellValue("");
							R12cell1.setCellStyle(textStyle);
						}

// R12 Col F
						Cell R12cell5 = row.createCell(5);
						if (record.getR12_call() != null) {
							R12cell5.setCellValue(record.getR12_call().doubleValue());
							R12cell5.setCellStyle(numberStyle);
						} else {
							R12cell5.setCellValue("");
							R12cell5.setCellStyle(textStyle);
						}

// R12 Col G
						Cell R12cell6 = row.createCell(6);
						if (record.getR12_savings() != null) {
							R12cell6.setCellValue(record.getR12_savings().doubleValue());
							R12cell6.setCellStyle(numberStyle);
						} else {
							R12cell6.setCellValue("");
							R12cell6.setCellStyle(textStyle);

						}

// R12 Col H
						Cell R12cell7 = row.createCell(7);
						if (record.getR12_notice_0to31() != null) {
							R12cell7.setCellValue(record.getR12_notice_0to31().doubleValue());
							R12cell7.setCellStyle(numberStyle);
						} else {
							R12cell7.setCellValue("");
							R12cell7.setCellStyle(textStyle);

						}

// R12 Col I
						Cell R12cell8 = row.createCell(8);
						if (record.getR12_notice_32to88() != null) {
							R12cell8.setCellValue(record.getR12_notice_32to88().doubleValue());
							R12cell8.setCellStyle(numberStyle);
						} else {
							R12cell8.setCellValue("");
							R12cell8.setCellStyle(textStyle);
						}

// R12 Col J
						Cell R12cell9 = row.createCell(9);
						if (record.getR12_fix_depo_91_day_depo() != null) {
							R12cell9.setCellValue(record.getR12_fix_depo_91_day_depo().doubleValue());
							R12cell9.setCellStyle(numberStyle);
						} else {
							R12cell9.setCellValue("");
							R12cell9.setCellStyle(textStyle);
						}

// R12 Col K
						Cell R12cell10 = row.createCell(10);
						if (record.getR12_fix_depo_1to2() != null) {
							R12cell10.setCellValue(record.getR12_fix_depo_1to2().doubleValue());
							R12cell10.setCellStyle(numberStyle);
						} else {
							R12cell10.setCellValue("");
							R12cell10.setCellStyle(textStyle);
						}

// R12 Col L
						Cell R12cell11 = row.createCell(11);
						if (record.getR12_fix_depo_4to6() != null) {
							R12cell11.setCellValue(record.getR12_fix_depo_4to6().doubleValue());
							R12cell11.setCellStyle(numberStyle);
						} else {
							R12cell11.setCellValue("");
							R12cell11.setCellStyle(textStyle);
						}

// R12 Col M
						Cell R12cell12 = row.createCell(12);
						if (record.getR12_fix_depo_7to12() != null) {
							R12cell12.setCellValue(record.getR12_fix_depo_7to12().doubleValue());
							R12cell12.setCellStyle(numberStyle);
						} else {
							R12cell12.setCellValue("");
							R12cell12.setCellStyle(textStyle);
						}

// R12 Col N
						Cell R12cell13 = row.createCell(13);
						if (record.getR12_fix_depo_13to18() != null) {
							R12cell13.setCellValue(record.getR12_fix_depo_13to18().doubleValue());
							R12cell13.setCellStyle(numberStyle);
						} else {
							R12cell13.setCellValue("");
							R12cell13.setCellStyle(textStyle);
						}

// R12 Col O
						Cell R12cell14 = row.createCell(14);
						if (record.getR12_fix_depo_19to24() != null) {
							R12cell14.setCellValue(record.getR12_fix_depo_19to24().doubleValue());
							R12cell14.setCellStyle(numberStyle);
						} else {
							R12cell14.setCellValue("");
							R12cell14.setCellStyle(textStyle);
						}

// R12 Col P
						Cell R12cell15 = row.createCell(15);
						if (record.getR12_fix_depo_over24() != null) {
							R12cell15.setCellValue(record.getR12_fix_depo_over24().doubleValue());
							R12cell15.setCellStyle(numberStyle);
						} else {
							R12cell15.setCellValue("");
							R12cell15.setCellStyle(textStyle);
						}

// R12 Col Q
						Cell R12cell16 = row.createCell(16);
						if (record.getR12_cer_of_depo() != null) {
							R12cell16.setCellValue(record.getR12_cer_of_depo().doubleValue());
							R12cell16.setCellStyle(numberStyle);
						} else {
							R12cell16.setCellValue("");
							R12cell16.setCellStyle(textStyle);
						}

// R12 Col S
						Cell R12cell18 = row.createCell(18);
						if (record.getR12_pula_equivalent() != null) {
							R12cell18.setCellValue(record.getR12_pula_equivalent().doubleValue());
							R12cell18.setCellStyle(numberStyle);
						} else {
							R12cell18.setCellValue("");
							R12cell18.setCellStyle(textStyle);
						}

// R12 Col T
						Cell R12cell19 = row.createCell(19);
						if (record.getR12_avg_pula_equivalent() != null) {
							R12cell19.setCellValue(record.getR12_avg_pula_equivalent().doubleValue());
							R12cell19.setCellStyle(numberStyle);
						} else {
							R12cell19.setCellValue("");
							R12cell19.setCellStyle(textStyle);
						}

// R13 Col B
						row = sheet.getRow(12);
						Cell R13cell1 = row.createCell(1);
						if (record.getR13_ex_rate_buy() != null) {
							R13cell1.setCellValue(record.getR13_ex_rate_buy().doubleValue());
							R13cell1.setCellStyle(numberStyle);
						} else {
							R13cell1.setCellValue("");
							R13cell1.setCellStyle(textStyle);
						}

// R13 Col C
						Cell R13cell2 = row.createCell(2);
						if (record.getR13_ex_rate_mid() != null) {
							R13cell2.setCellValue(record.getR13_ex_rate_mid().doubleValue());
							R13cell2.setCellStyle(numberStyle);
						} else {
							R13cell2.setCellValue("");
							R13cell2.setCellStyle(textStyle);
						}

// R13 Col D
						Cell R13cell3 = row.createCell(3);
						if (record.getR13_ex_rate_sell() != null) {
							R13cell3.setCellValue(record.getR13_ex_rate_sell().doubleValue());
							R13cell3.setCellStyle(numberStyle);
						} else {
							R13cell3.setCellValue("");
							R13cell3.setCellStyle(textStyle);
						}

// R13 Col E
						Cell R13cell4 = row.createCell(4);
						if (record.getR13_current() != null) {
							R13cell4.setCellValue(record.getR13_current().doubleValue());
							R13cell4.setCellStyle(numberStyle);
						} else {
							R13cell4.setCellValue("");
							R13cell1.setCellStyle(textStyle);
						}

// R13 Col F
						Cell R13cell5 = row.createCell(5);
						if (record.getR13_call() != null) {
							R13cell5.setCellValue(record.getR13_call().doubleValue());
							R13cell5.setCellStyle(numberStyle);
						} else {
							R13cell5.setCellValue("");
							R13cell5.setCellStyle(textStyle);
						}

// R13 Col G
						Cell R13cell6 = row.createCell(6);
						if (record.getR13_savings() != null) {
							R13cell6.setCellValue(record.getR13_savings().doubleValue());
							R13cell6.setCellStyle(numberStyle);
						} else {
							R13cell6.setCellValue("");
							R13cell6.setCellStyle(textStyle);

						}

// R13 Col H
						Cell R13cell7 = row.createCell(7);
						if (record.getR13_notice_0to31() != null) {
							R13cell7.setCellValue(record.getR13_notice_0to31().doubleValue());
							R13cell7.setCellStyle(numberStyle);
						} else {
							R13cell7.setCellValue("");
							R13cell7.setCellStyle(textStyle);

						}

// R13 Col I
						Cell R13cell8 = row.createCell(8);
						if (record.getR13_notice_32to88() != null) {
							R13cell8.setCellValue(record.getR13_notice_32to88().doubleValue());
							R13cell8.setCellStyle(numberStyle);
						} else {
							R13cell8.setCellValue("");
							R13cell8.setCellStyle(textStyle);
						}

// R13 Col J
						Cell R13cell9 = row.createCell(9);
						if (record.getR13_fix_depo_91_day_depo() != null) {
							R13cell9.setCellValue(record.getR13_fix_depo_91_day_depo().doubleValue());
							R13cell9.setCellStyle(numberStyle);
						} else {
							R13cell9.setCellValue("");
							R13cell9.setCellStyle(textStyle);
						}

// R13 Col K
						Cell R13cell10 = row.createCell(10);
						if (record.getR13_fix_depo_1to2() != null) {
							R13cell10.setCellValue(record.getR13_fix_depo_1to2().doubleValue());
							R13cell10.setCellStyle(numberStyle);
						} else {
							R13cell10.setCellValue("");
							R13cell10.setCellStyle(textStyle);
						}

// R13 Col L
						Cell R13cell11 = row.createCell(11);
						if (record.getR13_fix_depo_4to6() != null) {
							R13cell11.setCellValue(record.getR13_fix_depo_4to6().doubleValue());
							R13cell11.setCellStyle(numberStyle);
						} else {
							R13cell11.setCellValue("");
							R13cell11.setCellStyle(textStyle);
						}

// R13 Col M
						Cell R13cell12 = row.createCell(12);
						if (record.getR13_fix_depo_7to12() != null) {
							R13cell12.setCellValue(record.getR13_fix_depo_7to12().doubleValue());
							R13cell12.setCellStyle(numberStyle);
						} else {
							R13cell12.setCellValue("");
							R13cell12.setCellStyle(textStyle);
						}

// R13 Col N
						Cell R13cell13 = row.createCell(13);
						if (record.getR13_fix_depo_13to18() != null) {
							R13cell13.setCellValue(record.getR13_fix_depo_13to18().doubleValue());
							R13cell13.setCellStyle(numberStyle);
						} else {
							R13cell13.setCellValue("");
							R13cell13.setCellStyle(textStyle);
						}

// R13 Col O
						Cell R13cell14 = row.createCell(14);
						if (record.getR13_fix_depo_19to24() != null) {
							R13cell14.setCellValue(record.getR13_fix_depo_19to24().doubleValue());
							R13cell14.setCellStyle(numberStyle);
						} else {
							R13cell14.setCellValue("");
							R13cell14.setCellStyle(textStyle);
						}

// R13 Col P
						Cell R13cell15 = row.createCell(15);
						if (record.getR13_fix_depo_over24() != null) {
							R13cell15.setCellValue(record.getR13_fix_depo_over24().doubleValue());
							R13cell15.setCellStyle(numberStyle);
						} else {
							R13cell15.setCellValue("");
							R13cell15.setCellStyle(textStyle);
						}

// R13 Col Q
						Cell R13cell16 = row.createCell(16);
						if (record.getR13_cer_of_depo() != null) {
							R13cell16.setCellValue(record.getR13_cer_of_depo().doubleValue());
							R13cell16.setCellStyle(numberStyle);
						} else {
							R13cell16.setCellValue("");
							R13cell16.setCellStyle(textStyle);
						}

// R13 Col S
						Cell R13cell18 = row.createCell(18);
						if (record.getR13_pula_equivalent() != null) {
							R13cell18.setCellValue(record.getR13_pula_equivalent().doubleValue());
							R13cell18.setCellStyle(numberStyle);
						} else {
							R13cell18.setCellValue("");
							R13cell18.setCellStyle(textStyle);
						}

// R13 Col T
						Cell R13cell19 = row.createCell(19);
						if (record.getR13_avg_pula_equivalent() != null) {
							R13cell19.setCellValue(record.getR13_avg_pula_equivalent().doubleValue());
							R13cell19.setCellStyle(numberStyle);
						} else {
							R13cell19.setCellValue("");
							R13cell19.setCellStyle(textStyle);
						}

// R14 Col B
						row = sheet.getRow(13);
						Cell R14cell1 = row.createCell(1);
						if (record.getR14_ex_rate_buy() != null) {
							R14cell1.setCellValue(record.getR14_ex_rate_buy().doubleValue());
							R14cell1.setCellStyle(numberStyle);
						} else {
							R14cell1.setCellValue("");
							R14cell1.setCellStyle(textStyle);
						}

// R14 Col C
						Cell R14cell2 = row.createCell(2);
						if (record.getR14_ex_rate_mid() != null) {
							R14cell2.setCellValue(record.getR14_ex_rate_mid().doubleValue());
							R14cell2.setCellStyle(numberStyle);
						} else {
							R14cell2.setCellValue("");
							R14cell2.setCellStyle(textStyle);
						}

// R14 Col D
						Cell R14cell3 = row.createCell(3);
						if (record.getR14_ex_rate_sell() != null) {
							R14cell3.setCellValue(record.getR14_ex_rate_sell().doubleValue());
							R14cell3.setCellStyle(numberStyle);
						} else {
							R14cell3.setCellValue("");
							R14cell3.setCellStyle(textStyle);
						}

// R14 Col E
						Cell R14cell4 = row.createCell(4);
						if (record.getR14_current() != null) {
							R14cell4.setCellValue(record.getR14_current().doubleValue());
							R14cell4.setCellStyle(numberStyle);
						} else {
							R14cell4.setCellValue("");
							R14cell1.setCellStyle(textStyle);
						}

// R14 Col F
						Cell R14cell5 = row.createCell(5);
						if (record.getR14_call() != null) {
							R14cell5.setCellValue(record.getR14_call().doubleValue());
							R14cell5.setCellStyle(numberStyle);
						} else {
							R14cell5.setCellValue("");
							R14cell5.setCellStyle(textStyle);
						}

// R14 Col G
						Cell R14cell6 = row.createCell(6);
						if (record.getR14_savings() != null) {
							R14cell6.setCellValue(record.getR14_savings().doubleValue());
							R14cell6.setCellStyle(numberStyle);
						} else {
							R14cell6.setCellValue("");
							R14cell6.setCellStyle(textStyle);

						}

// R14 Col H
						Cell R14cell7 = row.createCell(7);
						if (record.getR14_notice_0to31() != null) {
							R14cell7.setCellValue(record.getR14_notice_0to31().doubleValue());
							R14cell7.setCellStyle(numberStyle);
						} else {
							R14cell7.setCellValue("");
							R14cell7.setCellStyle(textStyle);

						}

// R14 Col I
						Cell R14cell8 = row.createCell(8);
						if (record.getR14_notice_32to88() != null) {
							R14cell8.setCellValue(record.getR14_notice_32to88().doubleValue());
							R14cell8.setCellStyle(numberStyle);
						} else {
							R14cell8.setCellValue("");
							R14cell8.setCellStyle(textStyle);
						}

// R14 Col J
						Cell R14cell9 = row.createCell(9);
						if (record.getR14_fix_depo_91_day_depo() != null) {
							R14cell9.setCellValue(record.getR14_fix_depo_91_day_depo().doubleValue());
							R14cell9.setCellStyle(numberStyle);
						} else {
							R14cell9.setCellValue("");
							R14cell9.setCellStyle(textStyle);
						}

// R14 Col K
						Cell R14cell10 = row.createCell(10);
						if (record.getR14_fix_depo_1to2() != null) {
							R14cell10.setCellValue(record.getR14_fix_depo_1to2().doubleValue());
							R14cell10.setCellStyle(numberStyle);
						} else {
							R14cell10.setCellValue("");
							R14cell10.setCellStyle(textStyle);
						}

// R14 Col L
						Cell R14cell11 = row.createCell(11);
						if (record.getR14_fix_depo_4to6() != null) {
							R14cell11.setCellValue(record.getR14_fix_depo_4to6().doubleValue());
							R14cell11.setCellStyle(numberStyle);
						} else {
							R14cell11.setCellValue("");
							R14cell11.setCellStyle(textStyle);
						}

// R14 Col M
						Cell R14cell12 = row.createCell(12);
						if (record.getR14_fix_depo_7to12() != null) {
							R14cell12.setCellValue(record.getR14_fix_depo_7to12().doubleValue());
							R14cell12.setCellStyle(numberStyle);
						} else {
							R14cell12.setCellValue("");
							R14cell12.setCellStyle(textStyle);
						}

// R14 Col N
						Cell R14cell13 = row.createCell(13);
						if (record.getR14_fix_depo_13to18() != null) {
							R14cell13.setCellValue(record.getR14_fix_depo_13to18().doubleValue());
							R14cell13.setCellStyle(numberStyle);
						} else {
							R14cell13.setCellValue("");
							R14cell13.setCellStyle(textStyle);
						}

// R14 Col O
						Cell R14cell14 = row.createCell(14);
						if (record.getR14_fix_depo_19to24() != null) {
							R14cell14.setCellValue(record.getR14_fix_depo_19to24().doubleValue());
							R14cell14.setCellStyle(numberStyle);
						} else {
							R14cell14.setCellValue("");
							R14cell14.setCellStyle(textStyle);
						}

// R14 Col P
						Cell R14cell15 = row.createCell(15);
						if (record.getR14_fix_depo_over24() != null) {
							R14cell15.setCellValue(record.getR14_fix_depo_over24().doubleValue());
							R14cell15.setCellStyle(numberStyle);
						} else {
							R14cell15.setCellValue("");
							R14cell15.setCellStyle(textStyle);
						}

// R14 Col Q
						Cell R14cell16 = row.createCell(16);
						if (record.getR14_cer_of_depo() != null) {
							R14cell16.setCellValue(record.getR14_cer_of_depo().doubleValue());
							R14cell16.setCellStyle(numberStyle);
						} else {
							R14cell16.setCellValue("");
							R14cell16.setCellStyle(textStyle);
						}

// R14 Col S
						Cell R14cell18 = row.createCell(18);
						if (record.getR14_pula_equivalent() != null) {
							R14cell18.setCellValue(record.getR14_pula_equivalent().doubleValue());
							R14cell18.setCellStyle(numberStyle);
						} else {
							R14cell18.setCellValue("");
							R14cell18.setCellStyle(textStyle);
						}

// R14 Col T
						Cell R14cell19 = row.createCell(19);
						if (record.getR14_avg_pula_equivalent() != null) {
							R14cell19.setCellValue(record.getR14_avg_pula_equivalent().doubleValue());
							R14cell19.setCellStyle(numberStyle);
						} else {
							R14cell19.setCellValue("");
							R14cell19.setCellStyle(textStyle);
						}

// R15 Col B
						row = sheet.getRow(14);
						Cell R15cell1 = row.createCell(1);
						if (record.getR15_ex_rate_buy() != null) {
							R15cell1.setCellValue(record.getR15_ex_rate_buy().doubleValue());
							R15cell1.setCellStyle(numberStyle);
						} else {
							R15cell1.setCellValue("");
							R15cell1.setCellStyle(textStyle);
						}

// R15 Col C
						Cell R15cell2 = row.createCell(2);
						if (record.getR15_ex_rate_mid() != null) {
							R15cell2.setCellValue(record.getR15_ex_rate_mid().doubleValue());
							R15cell2.setCellStyle(numberStyle);
						} else {
							R15cell2.setCellValue("");
							R15cell2.setCellStyle(textStyle);
						}

// R15 Col D
						Cell R15cell3 = row.createCell(3);
						if (record.getR15_ex_rate_sell() != null) {
							R15cell3.setCellValue(record.getR15_ex_rate_sell().doubleValue());
							R15cell3.setCellStyle(numberStyle);
						} else {
							R15cell3.setCellValue("");
							R15cell3.setCellStyle(textStyle);
						}

// R15 Col E
						Cell R15cell4 = row.createCell(4);
						if (record.getR15_current() != null) {
							R15cell4.setCellValue(record.getR15_current().doubleValue());
							R15cell4.setCellStyle(numberStyle);
						} else {
							R15cell4.setCellValue("");
							R15cell1.setCellStyle(textStyle);
						}

// R15 Col F
						Cell R15cell5 = row.createCell(5);
						if (record.getR15_call() != null) {
							R15cell5.setCellValue(record.getR15_call().doubleValue());
							R15cell5.setCellStyle(numberStyle);
						} else {
							R15cell5.setCellValue("");
							R15cell5.setCellStyle(textStyle);
						}

// R15 Col G
						Cell R15cell6 = row.createCell(6);
						if (record.getR15_savings() != null) {
							R15cell6.setCellValue(record.getR15_savings().doubleValue());
							R15cell6.setCellStyle(numberStyle);
						} else {
							R15cell6.setCellValue("");
							R15cell6.setCellStyle(textStyle);

						}

// R15 Col H
						Cell R15cell7 = row.createCell(7);
						if (record.getR15_notice_0to31() != null) {
							R15cell7.setCellValue(record.getR15_notice_0to31().doubleValue());
							R15cell7.setCellStyle(numberStyle);
						} else {
							R15cell7.setCellValue("");
							R15cell7.setCellStyle(textStyle);

						}

// R15 Col I
						Cell R15cell8 = row.createCell(8);
						if (record.getR15_notice_32to88() != null) {
							R15cell8.setCellValue(record.getR15_notice_32to88().doubleValue());
							R15cell8.setCellStyle(numberStyle);
						} else {
							R15cell8.setCellValue("");
							R15cell8.setCellStyle(textStyle);
						}

// R15 Col J
						Cell R15cell9 = row.createCell(9);
						if (record.getR15_fix_depo_91_day_depo() != null) {
							R15cell9.setCellValue(record.getR15_fix_depo_91_day_depo().doubleValue());
							R15cell9.setCellStyle(numberStyle);
						} else {
							R15cell9.setCellValue("");
							R15cell9.setCellStyle(textStyle);
						}

// R15 Col K
						Cell R15cell10 = row.createCell(10);
						if (record.getR15_fix_depo_1to2() != null) {
							R15cell10.setCellValue(record.getR15_fix_depo_1to2().doubleValue());
							R15cell10.setCellStyle(numberStyle);
						} else {
							R15cell10.setCellValue("");
							R15cell10.setCellStyle(textStyle);
						}

// R15 Col L
						Cell R15cell11 = row.createCell(11);
						if (record.getR15_fix_depo_4to6() != null) {
							R15cell11.setCellValue(record.getR15_fix_depo_4to6().doubleValue());
							R15cell11.setCellStyle(numberStyle);
						} else {
							R15cell11.setCellValue("");
							R15cell11.setCellStyle(textStyle);
						}

// R15 Col M
						Cell R15cell12 = row.createCell(12);
						if (record.getR15_fix_depo_7to12() != null) {
							R15cell12.setCellValue(record.getR15_fix_depo_7to12().doubleValue());
							R15cell12.setCellStyle(numberStyle);
						} else {
							R15cell12.setCellValue("");
							R15cell12.setCellStyle(textStyle);
						}

// R15 Col N
						Cell R15cell13 = row.createCell(13);
						if (record.getR15_fix_depo_13to18() != null) {
							R15cell13.setCellValue(record.getR15_fix_depo_13to18().doubleValue());
							R15cell13.setCellStyle(numberStyle);
						} else {
							R15cell13.setCellValue("");
							R15cell13.setCellStyle(textStyle);
						}

// R15 Col O
						Cell R15cell14 = row.createCell(14);
						if (record.getR15_fix_depo_19to24() != null) {
							R15cell14.setCellValue(record.getR15_fix_depo_19to24().doubleValue());
							R15cell14.setCellStyle(numberStyle);
						} else {
							R15cell14.setCellValue("");
							R15cell14.setCellStyle(textStyle);
						}

// R15 Col P
						Cell R15cell15 = row.createCell(15);
						if (record.getR15_fix_depo_over24() != null) {
							R15cell15.setCellValue(record.getR15_fix_depo_over24().doubleValue());
							R15cell15.setCellStyle(numberStyle);
						} else {
							R15cell15.setCellValue("");
							R15cell15.setCellStyle(textStyle);
						}

// R15 Col Q
						Cell R15cell16 = row.createCell(16);
						if (record.getR15_cer_of_depo() != null) {
							R15cell16.setCellValue(record.getR15_cer_of_depo().doubleValue());
							R15cell16.setCellStyle(numberStyle);
						} else {
							R15cell16.setCellValue("");
							R15cell16.setCellStyle(textStyle);
						}

// R15 Col S
						Cell R15cell18 = row.createCell(18);
						if (record.getR15_pula_equivalent() != null) {
							R15cell18.setCellValue(record.getR15_pula_equivalent().doubleValue());
							R15cell18.setCellStyle(numberStyle);
						} else {
							R15cell18.setCellValue("");
							R15cell18.setCellStyle(textStyle);
						}

// R15 Col T
						Cell R15cell19 = row.createCell(19);
						if (record.getR15_avg_pula_equivalent() != null) {
							R15cell19.setCellValue(record.getR15_avg_pula_equivalent().doubleValue());
							R15cell19.setCellStyle(numberStyle);
						} else {
							R15cell19.setCellValue("");
							R15cell19.setCellStyle(textStyle);
						}

// R16 Col B
						row = sheet.getRow(15);
						Cell R16cell1 = row.createCell(1);
						if (record.getR16_ex_rate_buy() != null) {
							R16cell1.setCellValue(record.getR16_ex_rate_buy().doubleValue());
							R16cell1.setCellStyle(numberStyle);
						} else {
							R16cell1.setCellValue("");
							R16cell1.setCellStyle(textStyle);
						}

// R16 Col C
						Cell R16cell2 = row.createCell(2);
						if (record.getR16_ex_rate_mid() != null) {
							R16cell2.setCellValue(record.getR16_ex_rate_mid().doubleValue());
							R16cell2.setCellStyle(numberStyle);
						} else {
							R16cell2.setCellValue("");
							R16cell2.setCellStyle(textStyle);
						}

// R16 Col D
						Cell R16cell3 = row.createCell(3);
						if (record.getR16_ex_rate_sell() != null) {
							R16cell3.setCellValue(record.getR16_ex_rate_sell().doubleValue());
							R16cell3.setCellStyle(numberStyle);
						} else {
							R16cell3.setCellValue("");
							R16cell3.setCellStyle(textStyle);
						}

// R16 Col E
						Cell R16cell4 = row.createCell(4);
						if (record.getR16_current() != null) {
							R16cell4.setCellValue(record.getR16_current().doubleValue());
							R16cell4.setCellStyle(numberStyle);
						} else {
							R16cell4.setCellValue("");
							R16cell1.setCellStyle(textStyle);
						}

// R16 Col F
						Cell R16cell5 = row.createCell(5);
						if (record.getR16_call() != null) {
							R16cell5.setCellValue(record.getR16_call().doubleValue());
							R16cell5.setCellStyle(numberStyle);
						} else {
							R16cell5.setCellValue("");
							R16cell5.setCellStyle(textStyle);
						}

// R16 Col G
						Cell R16cell6 = row.createCell(6);
						if (record.getR16_savings() != null) {
							R16cell6.setCellValue(record.getR16_savings().doubleValue());
							R16cell6.setCellStyle(numberStyle);
						} else {
							R16cell6.setCellValue("");
							R16cell6.setCellStyle(textStyle);

						}

// R16 Col H
						Cell R16cell7 = row.createCell(7);
						if (record.getR16_notice_0to31() != null) {
							R16cell7.setCellValue(record.getR16_notice_0to31().doubleValue());
							R16cell7.setCellStyle(numberStyle);
						} else {
							R16cell7.setCellValue("");
							R16cell7.setCellStyle(textStyle);

						}

// R16 Col I
						Cell R16cell8 = row.createCell(8);
						if (record.getR16_notice_32to88() != null) {
							R16cell8.setCellValue(record.getR16_notice_32to88().doubleValue());
							R16cell8.setCellStyle(numberStyle);
						} else {
							R16cell8.setCellValue("");
							R16cell8.setCellStyle(textStyle);
						}

// R16 Col J
						Cell R16cell9 = row.createCell(9);
						if (record.getR16_fix_depo_91_day_depo() != null) {
							R16cell9.setCellValue(record.getR16_fix_depo_91_day_depo().doubleValue());
							R16cell9.setCellStyle(numberStyle);
						} else {
							R16cell9.setCellValue("");
							R16cell9.setCellStyle(textStyle);
						}

// R16 Col K
						Cell R16cell10 = row.createCell(10);
						if (record.getR16_fix_depo_1to2() != null) {
							R16cell10.setCellValue(record.getR16_fix_depo_1to2().doubleValue());
							R16cell10.setCellStyle(numberStyle);
						} else {
							R16cell10.setCellValue("");
							R16cell10.setCellStyle(textStyle);
						}

// R16 Col L
						Cell R16cell11 = row.createCell(11);
						if (record.getR16_fix_depo_4to6() != null) {
							R16cell11.setCellValue(record.getR16_fix_depo_4to6().doubleValue());
							R16cell11.setCellStyle(numberStyle);
						} else {
							R16cell11.setCellValue("");
							R16cell11.setCellStyle(textStyle);
						}

// R16 Col M
						Cell R16cell12 = row.createCell(12);
						if (record.getR16_fix_depo_7to12() != null) {
							R16cell12.setCellValue(record.getR16_fix_depo_7to12().doubleValue());
							R16cell12.setCellStyle(numberStyle);
						} else {
							R16cell12.setCellValue("");
							R16cell12.setCellStyle(textStyle);
						}

// R16 Col N
						Cell R16cell13 = row.createCell(13);
						if (record.getR16_fix_depo_13to18() != null) {
							R16cell13.setCellValue(record.getR16_fix_depo_13to18().doubleValue());
							R16cell13.setCellStyle(numberStyle);
						} else {
							R16cell13.setCellValue("");
							R16cell13.setCellStyle(textStyle);
						}

// R16 Col O
						Cell R16cell14 = row.createCell(14);
						if (record.getR16_fix_depo_19to24() != null) {
							R16cell14.setCellValue(record.getR16_fix_depo_19to24().doubleValue());
							R16cell14.setCellStyle(numberStyle);
						} else {
							R16cell14.setCellValue("");
							R16cell14.setCellStyle(textStyle);
						}

// R16 Col P
						Cell R16cell15 = row.createCell(15);
						if (record.getR16_fix_depo_over24() != null) {
							R16cell15.setCellValue(record.getR16_fix_depo_over24().doubleValue());
							R16cell15.setCellStyle(numberStyle);
						} else {
							R16cell15.setCellValue("");
							R16cell15.setCellStyle(textStyle);

						}

// R16 Col Q
						Cell R16cell16 = row.createCell(16);
						if (record.getR16_cer_of_depo() != null) {
							R16cell16.setCellValue(record.getR16_cer_of_depo().doubleValue());
							R16cell16.setCellStyle(numberStyle);
						} else {
							R16cell16.setCellValue("");
							R16cell16.setCellStyle(textStyle);
						}

// R16 Col S
						Cell R16cell18 = row.createCell(18);
						if (record.getR16_pula_equivalent() != null) {
							R16cell18.setCellValue(record.getR16_pula_equivalent().doubleValue());
							R16cell18.setCellStyle(numberStyle);
						} else {
							R16cell18.setCellValue("");
							R16cell18.setCellStyle(textStyle);
						}

// R16 Col T
						Cell R16cell19 = row.createCell(19);
						if (record.getR16_avg_pula_equivalent() != null) {
							R16cell19.setCellValue(record.getR16_avg_pula_equivalent().doubleValue());
							R16cell19.setCellStyle(numberStyle);
						} else {
							R16cell19.setCellValue("");
							R16cell19.setCellStyle(textStyle);
						}

// R17 Col B
						row = sheet.getRow(16);
						Cell R17cell1 = row.createCell(1);
						if (record.getR17_ex_rate_buy() != null) {
							R17cell1.setCellValue(record.getR17_ex_rate_buy().doubleValue());
							R17cell1.setCellStyle(numberStyle);
						} else {
							R17cell1.setCellValue("");
							R17cell1.setCellStyle(textStyle);
						}

// R17 Col C
						Cell R17cell2 = row.createCell(2);
						if (record.getR17_ex_rate_mid() != null) {
							R17cell2.setCellValue(record.getR17_ex_rate_mid().doubleValue());
							R17cell2.setCellStyle(numberStyle);
						} else {
							R17cell2.setCellValue("");
							R17cell2.setCellStyle(textStyle);
						}

// R17 Col D
						Cell R17cell3 = row.createCell(3);
						if (record.getR17_ex_rate_sell() != null) {
							R17cell3.setCellValue(record.getR17_ex_rate_sell().doubleValue());
							R17cell3.setCellStyle(numberStyle);
						} else {
							R17cell3.setCellValue("");
							R17cell3.setCellStyle(textStyle);
						}

// R18 Col B
						row = sheet.getRow(17);
						Cell R18cell1 = row.createCell(1);
						if (record.getR18_ex_rate_buy() != null) {
							R18cell1.setCellValue(record.getR18_ex_rate_buy().doubleValue());
							R18cell1.setCellStyle(numberStyle);
						} else {
							R18cell1.setCellValue("");
							R18cell1.setCellStyle(textStyle);
						}

// R18 Col C
						Cell R18cell2 = row.createCell(2);
						if (record.getR18_ex_rate_mid() != null) {
							R18cell2.setCellValue(record.getR18_ex_rate_mid().doubleValue());
							R18cell2.setCellStyle(numberStyle);
						} else {
							R18cell2.setCellValue("");
							R18cell2.setCellStyle(textStyle);
						}

// R18 Col D
						Cell R18cell3 = row.createCell(3);
						if (record.getR18_ex_rate_sell() != null) {
							R18cell3.setCellValue(record.getR18_ex_rate_sell().doubleValue());
							R18cell3.setCellStyle(numberStyle);
						} else {
							R18cell3.setCellValue("");
							R18cell3.setCellStyle(textStyle);
						}

// R18 Col E
						Cell R18cell4 = row.createCell(4);
						if (record.getR18_current() != null) {
							R18cell4.setCellValue(record.getR18_current().doubleValue());
							R18cell4.setCellStyle(numberStyle);
						} else {
							R18cell4.setCellValue("");
							R18cell1.setCellStyle(textStyle);
						}

// R18 Col F
						Cell R18cell5 = row.createCell(5);
						if (record.getR18_call() != null) {
							R18cell5.setCellValue(record.getR18_call().doubleValue());
							R18cell5.setCellStyle(numberStyle);
						} else {
							R18cell5.setCellValue("");
							R18cell5.setCellStyle(textStyle);
						}

// R18 Col G
						Cell R18cell6 = row.createCell(6);
						if (record.getR18_savings() != null) {
							R18cell6.setCellValue(record.getR18_savings().doubleValue());
							R18cell6.setCellStyle(numberStyle);
						} else {
							R18cell6.setCellValue("");
							R18cell6.setCellStyle(textStyle);

						}

// R18 Col H
						Cell R18cell7 = row.createCell(7);
						if (record.getR18_notice_0to31() != null) {
							R18cell7.setCellValue(record.getR18_notice_0to31().doubleValue());
							R18cell7.setCellStyle(numberStyle);
						} else {
							R18cell7.setCellValue("");
							R18cell7.setCellStyle(textStyle);

						}

// R18 Col I
						Cell R18cell8 = row.createCell(8);
						if (record.getR18_notice_32to88() != null) {
							R18cell8.setCellValue(record.getR18_notice_32to88().doubleValue());
							R18cell8.setCellStyle(numberStyle);
						} else {
							R18cell8.setCellValue("");
							R18cell8.setCellStyle(textStyle);
						}

// R18 Col J
						Cell R18cell9 = row.createCell(9);
						if (record.getR18_fix_depo_91_day_depo() != null) {
							R18cell9.setCellValue(record.getR18_fix_depo_91_day_depo().doubleValue());
							R18cell9.setCellStyle(numberStyle);
						} else {
							R18cell9.setCellValue("");
							R18cell9.setCellStyle(textStyle);
						}

// R18 Col K
						Cell R18cell10 = row.createCell(10);
						if (record.getR18_fix_depo_1to2() != null) {
							R18cell10.setCellValue(record.getR18_fix_depo_1to2().doubleValue());
							R18cell10.setCellStyle(numberStyle);
						} else {
							R18cell10.setCellValue("");
							R18cell10.setCellStyle(textStyle);
						}

// R18 Col L
						Cell R18cell11 = row.createCell(11);
						if (record.getR18_fix_depo_4to6() != null) {
							R18cell11.setCellValue(record.getR18_fix_depo_4to6().doubleValue());
							R18cell11.setCellStyle(numberStyle);
						} else {
							R18cell11.setCellValue("");
							R18cell11.setCellStyle(textStyle);
						}

// R18 Col M
						Cell R18cell12 = row.createCell(12);
						if (record.getR18_fix_depo_7to12() != null) {
							R18cell12.setCellValue(record.getR18_fix_depo_7to12().doubleValue());
							R18cell12.setCellStyle(numberStyle);
						} else {
							R18cell12.setCellValue("");
							R18cell12.setCellStyle(textStyle);
						}

// R18 Col N
						Cell R18cell13 = row.createCell(13);
						if (record.getR18_fix_depo_13to18() != null) {
							R18cell13.setCellValue(record.getR18_fix_depo_13to18().doubleValue());
							R18cell13.setCellStyle(numberStyle);
						} else {
							R18cell13.setCellValue("");
							R18cell13.setCellStyle(textStyle);
						}

// R18 Col O
						Cell R18cell14 = row.createCell(14);
						if (record.getR18_fix_depo_19to24() != null) {
							R18cell14.setCellValue(record.getR18_fix_depo_19to24().doubleValue());
							R18cell14.setCellStyle(numberStyle);
						} else {
							R18cell14.setCellValue("");
							R18cell14.setCellStyle(textStyle);
						}

// R18 Col P
						Cell R18cell15 = row.createCell(15);
						if (record.getR18_fix_depo_over24() != null) {
							R18cell15.setCellValue(record.getR18_fix_depo_over24().doubleValue());
							R18cell15.setCellStyle(numberStyle);
						} else {
							R18cell15.setCellValue("");
							R18cell15.setCellStyle(textStyle);
						}

// R18 Col Q
						Cell R18cell16 = row.createCell(16);
						if (record.getR18_cer_of_depo() != null) {
							R18cell16.setCellValue(record.getR18_cer_of_depo().doubleValue());
							R18cell16.setCellStyle(numberStyle);
						} else {
							R18cell16.setCellValue("");
							R18cell16.setCellStyle(textStyle);
						}
// R18 Col R
						Cell R18cell17 = row.createCell(17);
						if (record.getR18_total() != null) {
							R18cell17.setCellValue(record.getR18_total().doubleValue());
							R18cell17.setCellStyle(numberStyle);
						} else {
							R18cell17.setCellValue("");
							R18cell17.setCellStyle(textStyle);
						}
// R18 Col S
						Cell R18cell18 = row.createCell(18);
						if (record.getR18_pula_equivalent() != null) {
							R18cell18.setCellValue(record.getR18_pula_equivalent().doubleValue());
							R18cell18.setCellStyle(numberStyle);
						} else {
							R18cell18.setCellValue("");
							R18cell18.setCellStyle(textStyle);
						}
// R18 Col S
						Cell R18cell19 = row.createCell(19);
						if (record.getR18_avg_pula_equivalent() != null) {
							R18cell19.setCellValue(record.getR18_avg_pula_equivalent().doubleValue());
							R18cell18.setCellStyle(numberStyle);
						} else {
							R18cell19.setCellValue("");
							R18cell19.setCellStyle(textStyle);
						}

//Entity 2
// R28 Col B
						row = sheet.getRow(27);
						Cell R28cell1 = row.createCell(1);
						if (record.getR28_import() != null) {
							R28cell1.setCellValue(record.getR28_import().doubleValue());
							R28cell1.setCellStyle(numberStyle);
						} else {
							R28cell1.setCellValue("");
							R28cell1.setCellStyle(textStyle);
						}
// R28 Col C
						Cell R28cell2 = row.createCell(2);
						if (record.getR28_investment() != null) {
							R28cell2.setCellValue(record.getR28_investment().doubleValue());
							R28cell2.setCellStyle(numberStyle);
						} else {
							R28cell2.setCellValue("");
							R28cell2.setCellStyle(textStyle);
						}
// R28 Col D
						Cell R28cell3 = row.createCell(3);
						if (record.getR28_other() != null) {
							R28cell3.setCellValue(record.getR28_other().doubleValue());
							R28cell3.setCellStyle(numberStyle);
						} else {
							R28cell3.setCellValue("");
							R28cell3.setCellStyle(textStyle);
						}
// R29 Col B
						row = sheet.getRow(28);
						Cell R29cell1 = row.createCell(1);
						if (record.getR29_import() != null) {
							R29cell1.setCellValue(record.getR29_import().doubleValue());
							R29cell1.setCellStyle(numberStyle);
						} else {
							R29cell1.setCellValue("");
							R29cell1.setCellStyle(textStyle);
						}
// R29 Col C
						Cell R29cell2 = row.createCell(2);
						if (record.getR29_investment() != null) {
							R29cell2.setCellValue(record.getR29_investment().doubleValue());
							R29cell2.setCellStyle(numberStyle);
						} else {
							R29cell2.setCellValue("");
							R29cell2.setCellStyle(textStyle);
						}
// R29 Col D
						Cell R29cell3 = row.createCell(3);
						if (record.getR29_other() != null) {
							R29cell3.setCellValue(record.getR29_other().doubleValue());
							R29cell3.setCellStyle(numberStyle);
						} else {
							R29cell3.setCellValue("");
							R29cell3.setCellStyle(textStyle);
						}

// R30 Col B
						row = sheet.getRow(29);
						Cell R30cell1 = row.createCell(1);
						if (record.getR30_import() != null) {
							R30cell1.setCellValue(record.getR30_import().doubleValue());
							R30cell1.setCellStyle(numberStyle);
						} else {
							R30cell1.setCellValue("");
							R30cell1.setCellStyle(textStyle);
						}
// R30 Col C
						Cell R30cell2 = row.createCell(2);
						if (record.getR30_investment() != null) {
							R30cell2.setCellValue(record.getR30_investment().doubleValue());
							R30cell2.setCellStyle(numberStyle);
						} else {
							R30cell2.setCellValue("");
							R30cell2.setCellStyle(textStyle);
						}
// R30 Col D
						Cell R30cell3 = row.createCell(3);
						if (record.getR30_other() != null) {
							R30cell3.setCellValue(record.getR30_other().doubleValue());
							R30cell3.setCellStyle(numberStyle);
						} else {
							R30cell3.setCellValue("");
							R30cell3.setCellStyle(textStyle);
						}
// R31 Col B
						row = sheet.getRow(30);
						Cell R31cell1 = row.createCell(1);
						if (record.getR31_import() != null) {
							R31cell1.setCellValue(record.getR31_import().doubleValue());
							R31cell1.setCellStyle(numberStyle);
						} else {
							R31cell1.setCellValue("");
							R31cell1.setCellStyle(textStyle);
						}
// R31 Col C
						Cell R31cell2 = row.createCell(2);
						if (record.getR31_investment() != null) {
							R31cell2.setCellValue(record.getR31_investment().doubleValue());
							R31cell2.setCellStyle(numberStyle);
						} else {
							R31cell2.setCellValue("");
							R31cell2.setCellStyle(textStyle);
						}
// R31 Col D
						Cell R31cell3 = row.createCell(3);
						if (record.getR31_other() != null) {
							R31cell3.setCellValue(record.getR31_other().doubleValue());
							R31cell3.setCellStyle(numberStyle);
						} else {
							R31cell3.setCellValue("");
							R31cell3.setCellStyle(textStyle);
						}
// R32 Col B
						row = sheet.getRow(31);
						Cell R32cell1 = row.createCell(1);
						if (record.getR32_import() != null) {
							R32cell1.setCellValue(record.getR32_import().doubleValue());
							R32cell1.setCellStyle(numberStyle);
						} else {
							R32cell1.setCellValue("");
							R32cell1.setCellStyle(textStyle);
						}
// R32 Col C
						Cell R32cell2 = row.createCell(2);
						if (record.getR32_investment() != null) {
							R32cell2.setCellValue(record.getR32_investment().doubleValue());
							R32cell2.setCellStyle(numberStyle);
						} else {
							R32cell2.setCellValue("");
							R32cell2.setCellStyle(textStyle);
						}
// R32 Col D
						Cell R32cell3 = row.createCell(3);
						if (record.getR32_other() != null) {
							R32cell3.setCellValue(record.getR32_other().doubleValue());
							R32cell3.setCellStyle(numberStyle);
						} else {
							R32cell3.setCellValue("");
							R32cell3.setCellStyle(textStyle);
						}
// R33 Col B
						row = sheet.getRow(32);
						Cell R33cell1 = row.createCell(1);
						if (record.getR33_import() != null) {
							R33cell1.setCellValue(record.getR33_import().doubleValue());
							R33cell1.setCellStyle(numberStyle);
						} else {
							R33cell1.setCellValue("");
							R33cell1.setCellStyle(textStyle);
						}
// R33 Col C
						Cell R33cell2 = row.createCell(2);
						if (record.getR33_investment() != null) {
							R33cell2.setCellValue(record.getR33_investment().doubleValue());
							R33cell2.setCellStyle(numberStyle);
						} else {
							R33cell2.setCellValue("");
							R33cell2.setCellStyle(textStyle);
						}
// R33 Col D
						Cell R33cell3 = row.createCell(3);
						if (record.getR33_other() != null) {
							R33cell3.setCellValue(record.getR33_other().doubleValue());
							R33cell3.setCellStyle(numberStyle);
						} else {
							R33cell3.setCellValue("");
							R33cell3.setCellStyle(textStyle);
						}

//Entity 3

						row = sheet.getRow(27);
						Cell R28cell1e3 = row.createCell(8);
						if (record.getR28_residents() != null) {
							R28cell1e3.setCellValue(record.getR28_residents().doubleValue());
							R28cell1e3.setCellStyle(numberStyle);
						} else {
							R28cell1e3.setCellValue("");
							R28cell1e3.setCellStyle(textStyle);
						}
// R28 Col C
						Cell R28cell2e3 = row.createCell(10);
						if (record.getR28_non_residents() != null) {
							R28cell2e3.setCellValue(record.getR28_non_residents().doubleValue());
							R28cell2e3.setCellStyle(numberStyle);
						} else {
							R28cell2e3.setCellValue("");
							R28cell2e3.setCellStyle(textStyle);
						}
// R29 Col B
						row = sheet.getRow(28);
						Cell R29cell1e3 = row.createCell(8);
						if (record.getR29_residents() != null) {
							R29cell1e3.setCellValue(record.getR29_residents().doubleValue());
							R29cell1e3.setCellStyle(numberStyle);
						} else {
							R29cell1e3.setCellValue("");
							R29cell1e3.setCellStyle(textStyle);
						}
// R29 Col C
						Cell R29cell2e3 = row.createCell(10);
						if (record.getR29_non_residents() != null) {
							R29cell2e3.setCellValue(record.getR29_non_residents().doubleValue());
							R29cell2e3.setCellStyle(numberStyle);
						} else {
							R29cell2e3.setCellValue("");
							R29cell2e3.setCellStyle(textStyle);
						}
// R30 Col B
						row = sheet.getRow(29);
						Cell R30cell1e3 = row.createCell(8);
						if (record.getR30_residents() != null) {
							R30cell1e3.setCellValue(record.getR30_residents().doubleValue());
							R30cell1e3.setCellStyle(numberStyle);
						} else {
							R30cell1e3.setCellValue("");
							R30cell1e3.setCellStyle(textStyle);
						}
// R30 Col C
						Cell R30cell2e3 = row.createCell(10);
						if (record.getR30_non_residents() != null) {
							R30cell2e3.setCellValue(record.getR30_non_residents().doubleValue());
							R30cell2e3.setCellStyle(numberStyle);
						} else {
							R30cell2e3.setCellValue("");
							R30cell2e3.setCellStyle(textStyle);
						}
// R31 Col B
						row = sheet.getRow(30);
						Cell R31cell1e3 = row.createCell(8);
						if (record.getR31_residents() != null) {
							R31cell1e3.setCellValue(record.getR31_residents().doubleValue());
							R31cell1e3.setCellStyle(numberStyle);
						} else {
							R31cell1e3.setCellValue("");
							R31cell1e3.setCellStyle(textStyle);
						}
// R31 Col C
						Cell R31cell2e3 = row.createCell(10);
						if (record.getR31_non_residents() != null) {
							R31cell2e3.setCellValue(record.getR31_non_residents().doubleValue());
							R31cell2e3.setCellStyle(numberStyle);
						} else {
							R31cell2e3.setCellValue("");
							R31cell2e3.setCellStyle(textStyle);
						}

// R32 Col B
						row = sheet.getRow(31);
						Cell R32cell1e3 = row.createCell(8);
						if (record.getR32_residents() != null) {
							R32cell1e3.setCellValue(record.getR32_residents().doubleValue());
							R32cell1e3.setCellStyle(numberStyle);
						} else {
							R32cell1e3.setCellValue("");
							R32cell1e3.setCellStyle(textStyle);
						}
// R32 Col C
						Cell R32cell2e3 = row.createCell(10);
						if (record.getR32_non_residents() != null) {
							R32cell2e3.setCellValue(record.getR32_non_residents().doubleValue());
							R32cell2e3.setCellStyle(numberStyle);
						} else {
							R32cell2e3.setCellValue("");
							R32cell2e3.setCellStyle(textStyle);
						}
// R33 Col B
						row = sheet.getRow(32);
						Cell R33cell1e3 = row.createCell(8);
						if (record.getR33_residents() != null) {
							R33cell1e3.setCellValue(record.getR33_residents().doubleValue());
							R33cell1e3.setCellStyle(numberStyle);
						} else {
							R33cell1e3.setCellValue("");
							R33cell1e3.setCellStyle(textStyle);
						}
// R33 Col C
						Cell R33cell2e3 = row.createCell(10);
						if (record.getR33_non_residents() != null) {
							R33cell2e3.setCellValue(record.getR33_non_residents().doubleValue());
							R33cell2e3.setCellStyle(numberStyle);
						} else {
							R33cell2e3.setCellValue("");
							R33cell2e3.setCellStyle(textStyle);
						}

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
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_DEP3 EMAIL SUMMARY", null,
							"M_DEP3_SUMMARYTABLE");
				}
				return out.toByteArray();
			}
		}
	}

	// Archival format excel
	// ------------------------------
	// EXPORT ARCHIVAL SUMMARY TO EXCEL
	// ------------------------------
	public byte[] getExcelM_DEP3ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_DEP3EmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_DEP3_Archival_Summary_Entity> dataList = jdbcTemplate.query(
				"select * from BRRS_M_DEP3_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ? and REPORT_VERSION = ?",
				new Object[] { dateformat.parse(todate), version }, new M_DEP3_Archival_Summary_EntityRowMapper());

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_DEP3 report. Returning empty result.");
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
//NORMAL
			int startRow = 6;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_DEP3_Archival_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell R12Cell = row.createCell(1);

					if (record.getReportDate() != null) {

						R12Cell.setCellValue(record.getReportDate());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(10);
					// R11 Col B

					Cell R11cell1 = row.createCell(1);
					if (record.getR11_ex_rate_buy() != null) {
						R11cell1.setCellValue(record.getR11_ex_rate_buy().doubleValue());
						R11cell1.setCellStyle(numberStyle);
					} else {
						R11cell1.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}

// R11 Col C
					Cell R11cell2 = row.createCell(2);
					if (record.getR11_ex_rate_mid() != null) {
						R11cell2.setCellValue(record.getR11_ex_rate_mid().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}

// R11 Col D
					Cell R11cell3 = row.createCell(3);
					if (record.getR11_ex_rate_sell() != null) {
						R11cell3.setCellValue(record.getR11_ex_rate_sell().doubleValue());
						R11cell3.setCellStyle(numberStyle);
					} else {
						R11cell3.setCellValue("");
						R11cell3.setCellStyle(textStyle);
					}

// R11 Col E
					Cell R11cell4 = row.createCell(4);
					if (record.getR11_current() != null) {
						R11cell4.setCellValue(record.getR11_current().doubleValue());
						R11cell4.setCellStyle(numberStyle);
					} else {
						R11cell4.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}

// R11 Col F
					Cell R11cell5 = row.createCell(5);
					if (record.getR11_call() != null) {
						R11cell5.setCellValue(record.getR11_call().doubleValue());
						R11cell5.setCellStyle(numberStyle);
					} else {
						R11cell5.setCellValue("");
						R11cell5.setCellStyle(textStyle);
					}

// R11 Col G
					Cell R11cell6 = row.createCell(6);
					if (record.getR11_savings() != null) {
						R11cell6.setCellValue(record.getR11_savings().doubleValue());
						R11cell6.setCellStyle(numberStyle);
					} else {
						R11cell6.setCellValue("");
						R11cell6.setCellStyle(textStyle);

					}

// R11 Col H
					Cell R11cell7 = row.createCell(7);
					if (record.getR11_notice_0to31() != null) {
						R11cell7.setCellValue(record.getR11_notice_0to31().doubleValue());
						R11cell7.setCellStyle(numberStyle);
					} else {
						R11cell7.setCellValue("");
						R11cell7.setCellStyle(textStyle);

					}

// R11 Col I
					Cell R11cell8 = row.createCell(8);
					if (record.getR11_notice_32to88() != null) {
						R11cell8.setCellValue(record.getR11_notice_32to88().doubleValue());
						R11cell8.setCellStyle(numberStyle);
					} else {
						R11cell8.setCellValue("");
						R11cell8.setCellStyle(textStyle);
					}

// R11 Col J
					Cell R11cell9 = row.createCell(9);
					if (record.getR11_fix_depo_91_day_depo() != null) {
						R11cell9.setCellValue(record.getR11_fix_depo_91_day_depo().doubleValue());
						R11cell9.setCellStyle(numberStyle);
					} else {
						R11cell9.setCellValue("");
						R11cell9.setCellStyle(textStyle);
					}

// R11 Col K
					Cell R11cell10 = row.createCell(10);
					if (record.getR11_fix_depo_1to2() != null) {
						R11cell10.setCellValue(record.getR11_fix_depo_1to2().doubleValue());
						R11cell10.setCellStyle(numberStyle);
					} else {
						R11cell10.setCellValue("");
						R11cell10.setCellStyle(textStyle);
					}

// R11 Col L
					Cell R11cell11 = row.createCell(11);
					if (record.getR11_fix_depo_4to6() != null) {
						R11cell11.setCellValue(record.getR11_fix_depo_4to6().doubleValue());
						R11cell11.setCellStyle(numberStyle);
					} else {
						R11cell11.setCellValue("");
						R11cell11.setCellStyle(textStyle);
					}

// R11 Col M
					Cell R11cell12 = row.createCell(12);
					if (record.getR11_fix_depo_7to12() != null) {
						R11cell12.setCellValue(record.getR11_fix_depo_7to12().doubleValue());
						R11cell12.setCellStyle(numberStyle);
					} else {
						R11cell12.setCellValue("");
						R11cell12.setCellStyle(textStyle);
					}

// R11 Col N
					Cell R11cell13 = row.createCell(13);
					if (record.getR11_fix_depo_13to18() != null) {
						R11cell13.setCellValue(record.getR11_fix_depo_13to18().doubleValue());
						R11cell13.setCellStyle(numberStyle);
					} else {
						R11cell13.setCellValue("");
						R11cell13.setCellStyle(textStyle);
					}

// R11 Col O
					Cell R11cell14 = row.createCell(14);
					if (record.getR11_fix_depo_19to24() != null) {
						R11cell14.setCellValue(record.getR11_fix_depo_19to24().doubleValue());
						R11cell14.setCellStyle(numberStyle);
					} else {
						R11cell14.setCellValue("");
						R11cell14.setCellStyle(textStyle);
					}

// R11 Col P
					Cell R11cell15 = row.createCell(15);
					if (record.getR11_fix_depo_over24() != null) {
						R11cell15.setCellValue(record.getR11_fix_depo_over24().doubleValue());
						R11cell15.setCellStyle(numberStyle);
					} else {
						R11cell15.setCellValue("");
						R11cell15.setCellStyle(textStyle);
					}

// R11 Col Q
					Cell R11cell16 = row.createCell(16);
					if (record.getR11_cer_of_depo() != null) {
						R11cell16.setCellValue(record.getR11_cer_of_depo().doubleValue());
						R11cell16.setCellStyle(numberStyle);
					} else {
						R11cell16.setCellValue("");
						R11cell16.setCellStyle(textStyle);

					}

// R11 Col S
					Cell R11cell18 = row.createCell(18);
					if (record.getR11_pula_equivalent() != null) {
						R11cell18.setCellValue(record.getR11_pula_equivalent().doubleValue());
						R11cell18.setCellStyle(numberStyle);
					} else {
						R11cell18.setCellValue("");
						R11cell18.setCellStyle(textStyle);
					}

// R11 Col T
//					Cell R11cell19 = row.createCell(19);
//					if (record.getR11_avg_pula_equivalent() != null) {
//						R11cell19.setCellValue(record.getR11_avg_pula_equivalent().doubleValue());
//						R11cell19.setCellStyle(numberStyle);
//					} else {
//						R11cell19.setCellValue("");
//						R11cell19.setCellStyle(textStyle);
//					}
					row = sheet.getRow(11);
// R12 Col B
					Cell R12cell1 = row.createCell(1);
					if (record.getR12_ex_rate_buy() != null) {
						R12cell1.setCellValue(record.getR12_ex_rate_buy().doubleValue());
						R12cell1.setCellStyle(numberStyle);
					} else {
						R12cell1.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

// R12 Col C
					Cell R12cell2 = row.createCell(2);
					if (record.getR12_ex_rate_mid() != null) {
						R12cell2.setCellValue(record.getR12_ex_rate_mid().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}

// R12 Col D
					Cell R12cell3 = row.createCell(3);
					if (record.getR12_ex_rate_sell() != null) {
						R12cell3.setCellValue(record.getR12_ex_rate_sell().doubleValue());
						R12cell3.setCellStyle(numberStyle);
					} else {
						R12cell3.setCellValue("");
						R12cell3.setCellStyle(textStyle);
					}

// R12 Col E
					Cell R12cell4 = row.createCell(4);
					if (record.getR12_current() != null) {
						R12cell4.setCellValue(record.getR12_current().doubleValue());
						R12cell4.setCellStyle(numberStyle);
					} else {
						R12cell4.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

// R12 Col F
					Cell R12cell5 = row.createCell(5);
					if (record.getR12_call() != null) {
						R12cell5.setCellValue(record.getR12_call().doubleValue());
						R12cell5.setCellStyle(numberStyle);
					} else {
						R12cell5.setCellValue("");
						R12cell5.setCellStyle(textStyle);
					}

// R12 Col G
					Cell R12cell6 = row.createCell(6);
					if (record.getR12_savings() != null) {
						R12cell6.setCellValue(record.getR12_savings().doubleValue());
						R12cell6.setCellStyle(numberStyle);
					} else {
						R12cell6.setCellValue("");
						R12cell6.setCellStyle(textStyle);

					}

// R12 Col H
					Cell R12cell7 = row.createCell(7);
					if (record.getR12_notice_0to31() != null) {
						R12cell7.setCellValue(record.getR12_notice_0to31().doubleValue());
						R12cell7.setCellStyle(numberStyle);
					} else {
						R12cell7.setCellValue("");
						R12cell7.setCellStyle(textStyle);

					}

// R12 Col I
					Cell R12cell8 = row.createCell(8);
					if (record.getR12_notice_32to88() != null) {
						R12cell8.setCellValue(record.getR12_notice_32to88().doubleValue());
						R12cell8.setCellStyle(numberStyle);
					} else {
						R12cell8.setCellValue("");
						R12cell8.setCellStyle(textStyle);
					}

// R12 Col J
					Cell R12cell9 = row.createCell(9);
					if (record.getR12_fix_depo_91_day_depo() != null) {
						R12cell9.setCellValue(record.getR12_fix_depo_91_day_depo().doubleValue());
						R12cell9.setCellStyle(numberStyle);
					} else {
						R12cell9.setCellValue("");
						R12cell9.setCellStyle(textStyle);
					}

// R12 Col K
					Cell R12cell10 = row.createCell(10);
					if (record.getR12_fix_depo_1to2() != null) {
						R12cell10.setCellValue(record.getR12_fix_depo_1to2().doubleValue());
						R12cell10.setCellStyle(numberStyle);
					} else {
						R12cell10.setCellValue("");
						R12cell10.setCellStyle(textStyle);
					}

// R12 Col L
					Cell R12cell11 = row.createCell(11);
					if (record.getR12_fix_depo_4to6() != null) {
						R12cell11.setCellValue(record.getR12_fix_depo_4to6().doubleValue());
						R12cell11.setCellStyle(numberStyle);
					} else {
						R12cell11.setCellValue("");
						R12cell11.setCellStyle(textStyle);
					}

// R12 Col M
					Cell R12cell12 = row.createCell(12);
					if (record.getR12_fix_depo_7to12() != null) {
						R12cell12.setCellValue(record.getR12_fix_depo_7to12().doubleValue());
						R12cell12.setCellStyle(numberStyle);
					} else {
						R12cell12.setCellValue("");
						R12cell12.setCellStyle(textStyle);
					}

// R12 Col N
					Cell R12cell13 = row.createCell(13);
					if (record.getR12_fix_depo_13to18() != null) {
						R12cell13.setCellValue(record.getR12_fix_depo_13to18().doubleValue());
						R12cell13.setCellStyle(numberStyle);
					} else {
						R12cell13.setCellValue("");
						R12cell13.setCellStyle(textStyle);
					}

// R12 Col O
					Cell R12cell14 = row.createCell(14);
					if (record.getR12_fix_depo_19to24() != null) {
						R12cell14.setCellValue(record.getR12_fix_depo_19to24().doubleValue());
						R12cell14.setCellStyle(numberStyle);
					} else {
						R12cell14.setCellValue("");
						R12cell14.setCellStyle(textStyle);
					}

// R12 Col P
					Cell R12cell15 = row.createCell(15);
					if (record.getR12_fix_depo_over24() != null) {
						R12cell15.setCellValue(record.getR12_fix_depo_over24().doubleValue());
						R12cell15.setCellStyle(numberStyle);
					} else {
						R12cell15.setCellValue("");
						R12cell15.setCellStyle(textStyle);
					}

// R12 Col Q
					Cell R12cell16 = row.createCell(16);
					if (record.getR12_cer_of_depo() != null) {
						R12cell16.setCellValue(record.getR12_cer_of_depo().doubleValue());
						R12cell16.setCellStyle(numberStyle);
					} else {
						R12cell16.setCellValue("");
						R12cell16.setCellStyle(textStyle);
					}

// R12 Col S
					Cell R12cell18 = row.createCell(18);
					if (record.getR12_pula_equivalent() != null) {
						R12cell18.setCellValue(record.getR12_pula_equivalent().doubleValue());
						R12cell18.setCellStyle(numberStyle);
					} else {
						R12cell18.setCellValue("");
						R12cell18.setCellStyle(textStyle);
					}

// R12 Col T
//					Cell R12cell19 = row.createCell(19);
//					if (record.getR12_avg_pula_equivalent() != null) {
//						R12cell19.setCellValue(record.getR12_avg_pula_equivalent().doubleValue());
//						R12cell19.setCellStyle(numberStyle);
//					} else {
//						R12cell19.setCellValue("");
//						R12cell19.setCellStyle(textStyle);
//					}

// R13 Col B
					row = sheet.getRow(12);
					Cell R13cell1 = row.createCell(1);
					if (record.getR13_ex_rate_buy() != null) {
						R13cell1.setCellValue(record.getR13_ex_rate_buy().doubleValue());
						R13cell1.setCellStyle(numberStyle);
					} else {
						R13cell1.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

// R13 Col C
					Cell R13cell2 = row.createCell(2);
					if (record.getR13_ex_rate_mid() != null) {
						R13cell2.setCellValue(record.getR13_ex_rate_mid().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}

// R13 Col D
					Cell R13cell3 = row.createCell(3);
					if (record.getR13_ex_rate_sell() != null) {
						R13cell3.setCellValue(record.getR13_ex_rate_sell().doubleValue());
						R13cell3.setCellStyle(numberStyle);
					} else {
						R13cell3.setCellValue("");
						R13cell3.setCellStyle(textStyle);
					}

// R13 Col E
					Cell R13cell4 = row.createCell(4);
					if (record.getR13_current() != null) {
						R13cell4.setCellValue(record.getR13_current().doubleValue());
						R13cell4.setCellStyle(numberStyle);
					} else {
						R13cell4.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

// R13 Col F
					Cell R13cell5 = row.createCell(5);
					if (record.getR13_call() != null) {
						R13cell5.setCellValue(record.getR13_call().doubleValue());
						R13cell5.setCellStyle(numberStyle);
					} else {
						R13cell5.setCellValue("");
						R13cell5.setCellStyle(textStyle);
					}

// R13 Col G
					Cell R13cell6 = row.createCell(6);
					if (record.getR13_savings() != null) {
						R13cell6.setCellValue(record.getR13_savings().doubleValue());
						R13cell6.setCellStyle(numberStyle);
					} else {
						R13cell6.setCellValue("");
						R13cell6.setCellStyle(textStyle);

					}

// R13 Col H
					Cell R13cell7 = row.createCell(7);
					if (record.getR13_notice_0to31() != null) {
						R13cell7.setCellValue(record.getR13_notice_0to31().doubleValue());
						R13cell7.setCellStyle(numberStyle);
					} else {
						R13cell7.setCellValue("");
						R13cell7.setCellStyle(textStyle);

					}

// R13 Col I
					Cell R13cell8 = row.createCell(8);
					if (record.getR13_notice_32to88() != null) {
						R13cell8.setCellValue(record.getR13_notice_32to88().doubleValue());
						R13cell8.setCellStyle(numberStyle);
					} else {
						R13cell8.setCellValue("");
						R13cell8.setCellStyle(textStyle);
					}

// R13 Col J
					Cell R13cell9 = row.createCell(9);
					if (record.getR13_fix_depo_91_day_depo() != null) {
						R13cell9.setCellValue(record.getR13_fix_depo_91_day_depo().doubleValue());
						R13cell9.setCellStyle(numberStyle);
					} else {
						R13cell9.setCellValue("");
						R13cell9.setCellStyle(textStyle);
					}

// R13 Col K
					Cell R13cell10 = row.createCell(10);
					if (record.getR13_fix_depo_1to2() != null) {
						R13cell10.setCellValue(record.getR13_fix_depo_1to2().doubleValue());
						R13cell10.setCellStyle(numberStyle);
					} else {
						R13cell10.setCellValue("");
						R13cell10.setCellStyle(textStyle);
					}

// R13 Col L
					Cell R13cell11 = row.createCell(11);
					if (record.getR13_fix_depo_4to6() != null) {
						R13cell11.setCellValue(record.getR13_fix_depo_4to6().doubleValue());
						R13cell11.setCellStyle(numberStyle);
					} else {
						R13cell11.setCellValue("");
						R13cell11.setCellStyle(textStyle);
					}

// R13 Col M
					Cell R13cell12 = row.createCell(12);
					if (record.getR13_fix_depo_7to12() != null) {
						R13cell12.setCellValue(record.getR13_fix_depo_7to12().doubleValue());
						R13cell12.setCellStyle(numberStyle);
					} else {
						R13cell12.setCellValue("");
						R13cell12.setCellStyle(textStyle);
					}

// R13 Col N
					Cell R13cell13 = row.createCell(13);
					if (record.getR13_fix_depo_13to18() != null) {
						R13cell13.setCellValue(record.getR13_fix_depo_13to18().doubleValue());
						R13cell13.setCellStyle(numberStyle);
					} else {
						R13cell13.setCellValue("");
						R13cell13.setCellStyle(textStyle);
					}

// R13 Col O
					Cell R13cell14 = row.createCell(14);
					if (record.getR13_fix_depo_19to24() != null) {
						R13cell14.setCellValue(record.getR13_fix_depo_19to24().doubleValue());
						R13cell14.setCellStyle(numberStyle);
					} else {
						R13cell14.setCellValue("");
						R13cell14.setCellStyle(textStyle);
					}

// R13 Col P
					Cell R13cell15 = row.createCell(15);
					if (record.getR13_fix_depo_over24() != null) {
						R13cell15.setCellValue(record.getR13_fix_depo_over24().doubleValue());
						R13cell15.setCellStyle(numberStyle);
					} else {
						R13cell15.setCellValue("");
						R13cell15.setCellStyle(textStyle);
					}

// R13 Col Q
					Cell R13cell16 = row.createCell(16);
					if (record.getR13_cer_of_depo() != null) {
						R13cell16.setCellValue(record.getR13_cer_of_depo().doubleValue());
						R13cell16.setCellStyle(numberStyle);
					} else {
						R13cell16.setCellValue("");
						R13cell16.setCellStyle(textStyle);
					}

// R13 Col S
					Cell R13cell18 = row.createCell(18);
					if (record.getR13_pula_equivalent() != null) {
						R13cell18.setCellValue(record.getR13_pula_equivalent().doubleValue());
						R13cell18.setCellStyle(numberStyle);
					} else {
						R13cell18.setCellValue("");
						R13cell18.setCellStyle(textStyle);
					}

// R13 Col T
//					Cell R13cell19 = row.createCell(19);
//					if (record.getR13_avg_pula_equivalent() != null) {
//						R13cell19.setCellValue(record.getR13_avg_pula_equivalent().doubleValue());
//						R13cell19.setCellStyle(numberStyle);
//					} else {
//						R13cell19.setCellValue("");
//						R13cell19.setCellStyle(textStyle);
//					}

// R14 Col B
					row = sheet.getRow(13);
					Cell R14cell1 = row.createCell(1);
					if (record.getR14_ex_rate_buy() != null) {
						R14cell1.setCellValue(record.getR14_ex_rate_buy().doubleValue());
						R14cell1.setCellStyle(numberStyle);
					} else {
						R14cell1.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

// R14 Col C
					Cell R14cell2 = row.createCell(2);
					if (record.getR14_ex_rate_mid() != null) {
						R14cell2.setCellValue(record.getR14_ex_rate_mid().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}

// R14 Col D
					Cell R14cell3 = row.createCell(3);
					if (record.getR14_ex_rate_sell() != null) {
						R14cell3.setCellValue(record.getR14_ex_rate_sell().doubleValue());
						R14cell3.setCellStyle(numberStyle);
					} else {
						R14cell3.setCellValue("");
						R14cell3.setCellStyle(textStyle);
					}

// R14 Col E
					Cell R14cell4 = row.createCell(4);
					if (record.getR14_current() != null) {
						R14cell4.setCellValue(record.getR14_current().doubleValue());
						R14cell4.setCellStyle(numberStyle);
					} else {
						R14cell4.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

// R14 Col F
					Cell R14cell5 = row.createCell(5);
					if (record.getR14_call() != null) {
						R14cell5.setCellValue(record.getR14_call().doubleValue());
						R14cell5.setCellStyle(numberStyle);
					} else {
						R14cell5.setCellValue("");
						R14cell5.setCellStyle(textStyle);
					}

// R14 Col G
					Cell R14cell6 = row.createCell(6);
					if (record.getR14_savings() != null) {
						R14cell6.setCellValue(record.getR14_savings().doubleValue());
						R14cell6.setCellStyle(numberStyle);
					} else {
						R14cell6.setCellValue("");
						R14cell6.setCellStyle(textStyle);

					}

// R14 Col H
					Cell R14cell7 = row.createCell(7);
					if (record.getR14_notice_0to31() != null) {
						R14cell7.setCellValue(record.getR14_notice_0to31().doubleValue());
						R14cell7.setCellStyle(numberStyle);
					} else {
						R14cell7.setCellValue("");
						R14cell7.setCellStyle(textStyle);

					}

// R14 Col I
					Cell R14cell8 = row.createCell(8);
					if (record.getR14_notice_32to88() != null) {
						R14cell8.setCellValue(record.getR14_notice_32to88().doubleValue());
						R14cell8.setCellStyle(numberStyle);
					} else {
						R14cell8.setCellValue("");
						R14cell8.setCellStyle(textStyle);
					}

// R14 Col J
					Cell R14cell9 = row.createCell(9);
					if (record.getR14_fix_depo_91_day_depo() != null) {
						R14cell9.setCellValue(record.getR14_fix_depo_91_day_depo().doubleValue());
						R14cell9.setCellStyle(numberStyle);
					} else {
						R14cell9.setCellValue("");
						R14cell9.setCellStyle(textStyle);
					}

// R14 Col K
					Cell R14cell10 = row.createCell(10);
					if (record.getR14_fix_depo_1to2() != null) {
						R14cell10.setCellValue(record.getR14_fix_depo_1to2().doubleValue());
						R14cell10.setCellStyle(numberStyle);
					} else {
						R14cell10.setCellValue("");
						R14cell10.setCellStyle(textStyle);
					}

// R14 Col L
					Cell R14cell11 = row.createCell(11);
					if (record.getR14_fix_depo_4to6() != null) {
						R14cell11.setCellValue(record.getR14_fix_depo_4to6().doubleValue());
						R14cell11.setCellStyle(numberStyle);
					} else {
						R14cell11.setCellValue("");
						R14cell11.setCellStyle(textStyle);
					}

// R14 Col M
					Cell R14cell12 = row.createCell(12);
					if (record.getR14_fix_depo_7to12() != null) {
						R14cell12.setCellValue(record.getR14_fix_depo_7to12().doubleValue());
						R14cell12.setCellStyle(numberStyle);
					} else {
						R14cell12.setCellValue("");
						R14cell12.setCellStyle(textStyle);
					}

// R14 Col N
					Cell R14cell13 = row.createCell(13);
					if (record.getR14_fix_depo_13to18() != null) {
						R14cell13.setCellValue(record.getR14_fix_depo_13to18().doubleValue());
						R14cell13.setCellStyle(numberStyle);
					} else {
						R14cell13.setCellValue("");
						R14cell13.setCellStyle(textStyle);
					}

// R14 Col O
					Cell R14cell14 = row.createCell(14);
					if (record.getR14_fix_depo_19to24() != null) {
						R14cell14.setCellValue(record.getR14_fix_depo_19to24().doubleValue());
						R14cell14.setCellStyle(numberStyle);
					} else {
						R14cell14.setCellValue("");
						R14cell14.setCellStyle(textStyle);
					}

// R14 Col P
					Cell R14cell15 = row.createCell(15);
					if (record.getR14_fix_depo_over24() != null) {
						R14cell15.setCellValue(record.getR14_fix_depo_over24().doubleValue());
						R14cell15.setCellStyle(numberStyle);
					} else {
						R14cell15.setCellValue("");
						R14cell15.setCellStyle(textStyle);
					}

// R14 Col Q
					Cell R14cell16 = row.createCell(16);
					if (record.getR14_cer_of_depo() != null) {
						R14cell16.setCellValue(record.getR14_cer_of_depo().doubleValue());
						R14cell16.setCellStyle(numberStyle);
					} else {
						R14cell16.setCellValue("");
						R14cell16.setCellStyle(textStyle);
					}

// R14 Col S
					Cell R14cell18 = row.createCell(18);
					if (record.getR14_pula_equivalent() != null) {
						R14cell18.setCellValue(record.getR14_pula_equivalent().doubleValue());
						R14cell18.setCellStyle(numberStyle);
					} else {
						R14cell18.setCellValue("");
						R14cell18.setCellStyle(textStyle);
					}

// R14 Col T
//					Cell R14cell19 = row.createCell(19);
//					if (record.getR14_avg_pula_equivalent() != null) {
//						R14cell19.setCellValue(record.getR14_avg_pula_equivalent().doubleValue());
//						R14cell19.setCellStyle(numberStyle);
//					} else {
//						R14cell19.setCellValue("");
//						R14cell19.setCellStyle(textStyle);
//					}

// R15 Col B
					row = sheet.getRow(14);
					Cell R15cell1 = row.createCell(1);
					if (record.getR15_ex_rate_buy() != null) {
						R15cell1.setCellValue(record.getR15_ex_rate_buy().doubleValue());
						R15cell1.setCellStyle(numberStyle);
					} else {
						R15cell1.setCellValue("");
						R15cell1.setCellStyle(textStyle);
					}

// R15 Col C
					Cell R15cell2 = row.createCell(2);
					if (record.getR15_ex_rate_mid() != null) {
						R15cell2.setCellValue(record.getR15_ex_rate_mid().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);
					}

// R15 Col D
					Cell R15cell3 = row.createCell(3);
					if (record.getR15_ex_rate_sell() != null) {
						R15cell3.setCellValue(record.getR15_ex_rate_sell().doubleValue());
						R15cell3.setCellStyle(numberStyle);
					} else {
						R15cell3.setCellValue("");
						R15cell3.setCellStyle(textStyle);
					}

// R15 Col E
					Cell R15cell4 = row.createCell(4);
					if (record.getR15_current() != null) {
						R15cell4.setCellValue(record.getR15_current().doubleValue());
						R15cell4.setCellStyle(numberStyle);
					} else {
						R15cell4.setCellValue("");
						R15cell1.setCellStyle(textStyle);
					}

// R15 Col F
					Cell R15cell5 = row.createCell(5);
					if (record.getR15_call() != null) {
						R15cell5.setCellValue(record.getR15_call().doubleValue());
						R15cell5.setCellStyle(numberStyle);
					} else {
						R15cell5.setCellValue("");
						R15cell5.setCellStyle(textStyle);
					}

// R15 Col G
					Cell R15cell6 = row.createCell(6);
					if (record.getR15_savings() != null) {
						R15cell6.setCellValue(record.getR15_savings().doubleValue());
						R15cell6.setCellStyle(numberStyle);
					} else {
						R15cell6.setCellValue("");
						R15cell6.setCellStyle(textStyle);

					}

// R15 Col H
					Cell R15cell7 = row.createCell(7);
					if (record.getR15_notice_0to31() != null) {
						R15cell7.setCellValue(record.getR15_notice_0to31().doubleValue());
						R15cell7.setCellStyle(numberStyle);
					} else {
						R15cell7.setCellValue("");
						R15cell7.setCellStyle(textStyle);

					}

// R15 Col I
					Cell R15cell8 = row.createCell(8);
					if (record.getR15_notice_32to88() != null) {
						R15cell8.setCellValue(record.getR15_notice_32to88().doubleValue());
						R15cell8.setCellStyle(numberStyle);
					} else {
						R15cell8.setCellValue("");
						R15cell8.setCellStyle(textStyle);
					}

// R15 Col J
					Cell R15cell9 = row.createCell(9);
					if (record.getR15_fix_depo_91_day_depo() != null) {
						R15cell9.setCellValue(record.getR15_fix_depo_91_day_depo().doubleValue());
						R15cell9.setCellStyle(numberStyle);
					} else {
						R15cell9.setCellValue("");
						R15cell9.setCellStyle(textStyle);
					}

// R15 Col K
					Cell R15cell10 = row.createCell(10);
					if (record.getR15_fix_depo_1to2() != null) {
						R15cell10.setCellValue(record.getR15_fix_depo_1to2().doubleValue());
						R15cell10.setCellStyle(numberStyle);
					} else {
						R15cell10.setCellValue("");
						R15cell10.setCellStyle(textStyle);
					}

// R15 Col L
					Cell R15cell11 = row.createCell(11);
					if (record.getR15_fix_depo_4to6() != null) {
						R15cell11.setCellValue(record.getR15_fix_depo_4to6().doubleValue());
						R15cell11.setCellStyle(numberStyle);
					} else {
						R15cell11.setCellValue("");
						R15cell11.setCellStyle(textStyle);
					}

// R15 Col M
					Cell R15cell12 = row.createCell(12);
					if (record.getR15_fix_depo_7to12() != null) {
						R15cell12.setCellValue(record.getR15_fix_depo_7to12().doubleValue());
						R15cell12.setCellStyle(numberStyle);
					} else {
						R15cell12.setCellValue("");
						R15cell12.setCellStyle(textStyle);
					}

// R15 Col N
					Cell R15cell13 = row.createCell(13);
					if (record.getR15_fix_depo_13to18() != null) {
						R15cell13.setCellValue(record.getR15_fix_depo_13to18().doubleValue());
						R15cell13.setCellStyle(numberStyle);
					} else {
						R15cell13.setCellValue("");
						R15cell13.setCellStyle(textStyle);
					}

// R15 Col O
					Cell R15cell14 = row.createCell(14);
					if (record.getR15_fix_depo_19to24() != null) {
						R15cell14.setCellValue(record.getR15_fix_depo_19to24().doubleValue());
						R15cell14.setCellStyle(numberStyle);
					} else {
						R15cell14.setCellValue("");
						R15cell14.setCellStyle(textStyle);
					}

// R15 Col P
					Cell R15cell15 = row.createCell(15);
					if (record.getR15_fix_depo_over24() != null) {
						R15cell15.setCellValue(record.getR15_fix_depo_over24().doubleValue());
						R15cell15.setCellStyle(numberStyle);
					} else {
						R15cell15.setCellValue("");
						R15cell15.setCellStyle(textStyle);
					}

// R15 Col Q
					Cell R15cell16 = row.createCell(16);
					if (record.getR15_cer_of_depo() != null) {
						R15cell16.setCellValue(record.getR15_cer_of_depo().doubleValue());
						R15cell16.setCellStyle(numberStyle);
					} else {
						R15cell16.setCellValue("");
						R15cell16.setCellStyle(textStyle);
					}

// R15 Col S
					Cell R15cell18 = row.createCell(18);
					if (record.getR15_pula_equivalent() != null) {
						R15cell18.setCellValue(record.getR15_pula_equivalent().doubleValue());
						R15cell18.setCellStyle(numberStyle);
					} else {
						R15cell18.setCellValue("");
						R15cell18.setCellStyle(textStyle);
					}

// R15 Col T
//					Cell R15cell19 = row.createCell(19);
//					if (record.getR15_avg_pula_equivalent() != null) {
//						R15cell19.setCellValue(record.getR15_avg_pula_equivalent().doubleValue());
//						R15cell19.setCellStyle(numberStyle);
//					} else {
//						R15cell19.setCellValue("");
//						R15cell19.setCellStyle(textStyle);
//					}

// R16 Col B
					row = sheet.getRow(15);
					Cell R16cell1 = row.createCell(1);
					if (record.getR16_ex_rate_buy() != null) {
						R16cell1.setCellValue(record.getR16_ex_rate_buy().doubleValue());
						R16cell1.setCellStyle(numberStyle);
					} else {
						R16cell1.setCellValue("");
						R16cell1.setCellStyle(textStyle);
					}

// R16 Col C
					Cell R16cell2 = row.createCell(2);
					if (record.getR16_ex_rate_mid() != null) {
						R16cell2.setCellValue(record.getR16_ex_rate_mid().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);
					}

// R16 Col D
					Cell R16cell3 = row.createCell(3);
					if (record.getR16_ex_rate_sell() != null) {
						R16cell3.setCellValue(record.getR16_ex_rate_sell().doubleValue());
						R16cell3.setCellStyle(numberStyle);
					} else {
						R16cell3.setCellValue("");
						R16cell3.setCellStyle(textStyle);
					}

// R16 Col E
					Cell R16cell4 = row.createCell(4);
					if (record.getR16_current() != null) {
						R16cell4.setCellValue(record.getR16_current().doubleValue());
						R16cell4.setCellStyle(numberStyle);
					} else {
						R16cell4.setCellValue("");
						R16cell1.setCellStyle(textStyle);
					}

// R16 Col F
					Cell R16cell5 = row.createCell(5);
					if (record.getR16_call() != null) {
						R16cell5.setCellValue(record.getR16_call().doubleValue());
						R16cell5.setCellStyle(numberStyle);
					} else {
						R16cell5.setCellValue("");
						R16cell5.setCellStyle(textStyle);
					}

// R16 Col G
					Cell R16cell6 = row.createCell(6);
					if (record.getR16_savings() != null) {
						R16cell6.setCellValue(record.getR16_savings().doubleValue());
						R16cell6.setCellStyle(numberStyle);
					} else {
						R16cell6.setCellValue("");
						R16cell6.setCellStyle(textStyle);

					}

// R16 Col H
					Cell R16cell7 = row.createCell(7);
					if (record.getR16_notice_0to31() != null) {
						R16cell7.setCellValue(record.getR16_notice_0to31().doubleValue());
						R16cell7.setCellStyle(numberStyle);
					} else {
						R16cell7.setCellValue("");
						R16cell7.setCellStyle(textStyle);

					}

// R16 Col I
					Cell R16cell8 = row.createCell(8);
					if (record.getR16_notice_32to88() != null) {
						R16cell8.setCellValue(record.getR16_notice_32to88().doubleValue());
						R16cell8.setCellStyle(numberStyle);
					} else {
						R16cell8.setCellValue("");
						R16cell8.setCellStyle(textStyle);
					}

// R16 Col J
					Cell R16cell9 = row.createCell(9);
					if (record.getR16_fix_depo_91_day_depo() != null) {
						R16cell9.setCellValue(record.getR16_fix_depo_91_day_depo().doubleValue());
						R16cell9.setCellStyle(numberStyle);
					} else {
						R16cell9.setCellValue("");
						R16cell9.setCellStyle(textStyle);
					}

// R16 Col K
					Cell R16cell10 = row.createCell(10);
					if (record.getR16_fix_depo_1to2() != null) {
						R16cell10.setCellValue(record.getR16_fix_depo_1to2().doubleValue());
						R16cell10.setCellStyle(numberStyle);
					} else {
						R16cell10.setCellValue("");
						R16cell10.setCellStyle(textStyle);
					}

// R16 Col L
					Cell R16cell11 = row.createCell(11);
					if (record.getR16_fix_depo_4to6() != null) {
						R16cell11.setCellValue(record.getR16_fix_depo_4to6().doubleValue());
						R16cell11.setCellStyle(numberStyle);
					} else {
						R16cell11.setCellValue("");
						R16cell11.setCellStyle(textStyle);
					}

// R16 Col M
					Cell R16cell12 = row.createCell(12);
					if (record.getR16_fix_depo_7to12() != null) {
						R16cell12.setCellValue(record.getR16_fix_depo_7to12().doubleValue());
						R16cell12.setCellStyle(numberStyle);
					} else {
						R16cell12.setCellValue("");
						R16cell12.setCellStyle(textStyle);
					}

// R16 Col N
					Cell R16cell13 = row.createCell(13);
					if (record.getR16_fix_depo_13to18() != null) {
						R16cell13.setCellValue(record.getR16_fix_depo_13to18().doubleValue());
						R16cell13.setCellStyle(numberStyle);
					} else {
						R16cell13.setCellValue("");
						R16cell13.setCellStyle(textStyle);
					}

// R16 Col O
					Cell R16cell14 = row.createCell(14);
					if (record.getR16_fix_depo_19to24() != null) {
						R16cell14.setCellValue(record.getR16_fix_depo_19to24().doubleValue());
						R16cell14.setCellStyle(numberStyle);
					} else {
						R16cell14.setCellValue("");
						R16cell14.setCellStyle(textStyle);
					}

// R16 Col P
					Cell R16cell15 = row.createCell(15);
					if (record.getR16_fix_depo_over24() != null) {
						R16cell15.setCellValue(record.getR16_fix_depo_over24().doubleValue());
						R16cell15.setCellStyle(numberStyle);
					} else {
						R16cell15.setCellValue("");
						R16cell15.setCellStyle(textStyle);

					}

// R16 Col Q
					Cell R16cell16 = row.createCell(16);
					if (record.getR16_cer_of_depo() != null) {
						R16cell16.setCellValue(record.getR16_cer_of_depo().doubleValue());
						R16cell16.setCellStyle(numberStyle);
					} else {
						R16cell16.setCellValue("");
						R16cell16.setCellStyle(textStyle);
					}

// R16 Col S
					Cell R16cell18 = row.createCell(18);
					if (record.getR16_pula_equivalent() != null) {
						R16cell18.setCellValue(record.getR16_pula_equivalent().doubleValue());
						R16cell18.setCellStyle(numberStyle);
					} else {
						R16cell18.setCellValue("");
						R16cell18.setCellStyle(textStyle);
					}

// R16 Col T
//					Cell R16cell19 = row.createCell(19);
//					if (record.getR16_avg_pula_equivalent() != null) {
//						R16cell19.setCellValue(record.getR16_avg_pula_equivalent().doubleValue());
//						R16cell19.setCellStyle(numberStyle);
//					} else {
//						R16cell19.setCellValue("");
//						R16cell19.setCellStyle(textStyle);
//					}

// R17 Col B
					row = sheet.getRow(16);
					Cell R17cell1 = row.createCell(1);
					if (record.getR17_ex_rate_buy() != null) {
						R17cell1.setCellValue(record.getR17_ex_rate_buy().doubleValue());
						R17cell1.setCellStyle(numberStyle);
					} else {
						R17cell1.setCellValue("");
						R17cell1.setCellStyle(textStyle);
					}

// R17 Col C
					Cell R17cell2 = row.createCell(2);
					if (record.getR17_ex_rate_mid() != null) {
						R17cell2.setCellValue(record.getR17_ex_rate_mid().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);
					}

// R17 Col D
					Cell R17cell3 = row.createCell(3);
					if (record.getR17_ex_rate_sell() != null) {
						R17cell3.setCellValue(record.getR17_ex_rate_sell().doubleValue());
						R17cell3.setCellStyle(numberStyle);
					} else {
						R17cell3.setCellValue("");
						R17cell3.setCellStyle(textStyle);
					}

// R18 Col B
					row = sheet.getRow(17);
					Cell R18cell1 = row.createCell(1);
					if (record.getR18_ex_rate_buy() != null) {
						R18cell1.setCellValue(record.getR18_ex_rate_buy().doubleValue());
						R18cell1.setCellStyle(numberStyle);
					} else {
						R18cell1.setCellValue("");
						R18cell1.setCellStyle(textStyle);
					}

// R18 Col C
					Cell R18cell2 = row.createCell(2);
					if (record.getR18_ex_rate_mid() != null) {
						R18cell2.setCellValue(record.getR18_ex_rate_mid().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);
					}

// R18 Col D
					Cell R18cell3 = row.createCell(3);
					if (record.getR18_ex_rate_sell() != null) {
						R18cell3.setCellValue(record.getR18_ex_rate_sell().doubleValue());
						R18cell3.setCellStyle(numberStyle);
					} else {
						R18cell3.setCellValue("");
						R18cell3.setCellStyle(textStyle);
					}

// R18 Col E
					Cell R18cell4 = row.createCell(4);
					if (record.getR18_current() != null) {
						R18cell4.setCellValue(record.getR18_current().doubleValue());
						R18cell4.setCellStyle(numberStyle);
					} else {
						R18cell4.setCellValue("");
						R18cell1.setCellStyle(textStyle);
					}

// R18 Col F
					Cell R18cell5 = row.createCell(5);
					if (record.getR18_call() != null) {
						R18cell5.setCellValue(record.getR18_call().doubleValue());
						R18cell5.setCellStyle(numberStyle);
					} else {
						R18cell5.setCellValue("");
						R18cell5.setCellStyle(textStyle);
					}

// R18 Col G
					Cell R18cell6 = row.createCell(6);
					if (record.getR18_savings() != null) {
						R18cell6.setCellValue(record.getR18_savings().doubleValue());
						R18cell6.setCellStyle(numberStyle);
					} else {
						R18cell6.setCellValue("");
						R18cell6.setCellStyle(textStyle);

					}

// R18 Col H
					Cell R18cell7 = row.createCell(7);
					if (record.getR18_notice_0to31() != null) {
						R18cell7.setCellValue(record.getR18_notice_0to31().doubleValue());
						R18cell7.setCellStyle(numberStyle);
					} else {
						R18cell7.setCellValue("");
						R18cell7.setCellStyle(textStyle);

					}

// R18 Col I
					Cell R18cell8 = row.createCell(8);
					if (record.getR18_notice_32to88() != null) {
						R18cell8.setCellValue(record.getR18_notice_32to88().doubleValue());
						R18cell8.setCellStyle(numberStyle);
					} else {
						R18cell8.setCellValue("");
						R18cell8.setCellStyle(textStyle);
					}

// R18 Col J
					Cell R18cell9 = row.createCell(9);
					if (record.getR18_fix_depo_91_day_depo() != null) {
						R18cell9.setCellValue(record.getR18_fix_depo_91_day_depo().doubleValue());
						R18cell9.setCellStyle(numberStyle);
					} else {
						R18cell9.setCellValue("");
						R18cell9.setCellStyle(textStyle);
					}

// R18 Col K
					Cell R18cell10 = row.createCell(10);
					if (record.getR18_fix_depo_1to2() != null) {
						R18cell10.setCellValue(record.getR18_fix_depo_1to2().doubleValue());
						R18cell10.setCellStyle(numberStyle);
					} else {
						R18cell10.setCellValue("");
						R18cell10.setCellStyle(textStyle);
					}

// R18 Col L
					Cell R18cell11 = row.createCell(11);
					if (record.getR18_fix_depo_4to6() != null) {
						R18cell11.setCellValue(record.getR18_fix_depo_4to6().doubleValue());
						R18cell11.setCellStyle(numberStyle);
					} else {
						R18cell11.setCellValue("");
						R18cell11.setCellStyle(textStyle);
					}

// R18 Col M
					Cell R18cell12 = row.createCell(12);
					if (record.getR18_fix_depo_7to12() != null) {
						R18cell12.setCellValue(record.getR18_fix_depo_7to12().doubleValue());
						R18cell12.setCellStyle(numberStyle);
					} else {
						R18cell12.setCellValue("");
						R18cell12.setCellStyle(textStyle);
					}

// R18 Col N
					Cell R18cell13 = row.createCell(13);
					if (record.getR18_fix_depo_13to18() != null) {
						R18cell13.setCellValue(record.getR18_fix_depo_13to18().doubleValue());
						R18cell13.setCellStyle(numberStyle);
					} else {
						R18cell13.setCellValue("");
						R18cell13.setCellStyle(textStyle);
					}

// R18 Col O
					Cell R18cell14 = row.createCell(14);
					if (record.getR18_fix_depo_19to24() != null) {
						R18cell14.setCellValue(record.getR18_fix_depo_19to24().doubleValue());
						R18cell14.setCellStyle(numberStyle);
					} else {
						R18cell14.setCellValue("");
						R18cell14.setCellStyle(textStyle);
					}

// R18 Col P
					Cell R18cell15 = row.createCell(15);
					if (record.getR18_fix_depo_over24() != null) {
						R18cell15.setCellValue(record.getR18_fix_depo_over24().doubleValue());
						R18cell15.setCellStyle(numberStyle);
					} else {
						R18cell15.setCellValue("");
						R18cell15.setCellStyle(textStyle);
					}

// R18 Col Q
					Cell R18cell16 = row.createCell(16);
					if (record.getR18_cer_of_depo() != null) {
						R18cell16.setCellValue(record.getR18_cer_of_depo().doubleValue());
						R18cell16.setCellStyle(numberStyle);
					} else {
						R18cell16.setCellValue("");
						R18cell16.setCellStyle(textStyle);
					}
// R18 Col R
					Cell R18cell17 = row.createCell(17);
					if (record.getR18_total() != null) {
						R18cell17.setCellValue(record.getR18_total().doubleValue());
						R18cell17.setCellStyle(numberStyle);
					} else {
						R18cell17.setCellValue("");
						R18cell17.setCellStyle(textStyle);
					}
// R18 Col S
					Cell R18cell18 = row.createCell(18);
					if (record.getR18_pula_equivalent() != null) {
						R18cell18.setCellValue(record.getR18_pula_equivalent().doubleValue());
						R18cell18.setCellStyle(numberStyle);
					} else {
						R18cell18.setCellValue("");
						R18cell18.setCellStyle(textStyle);
					}
// R18 Col S
//					Cell R18cell19 = row.createCell(19);
//					if (record.getR18_avg_pula_equivalent() != null) {
//						R18cell19.setCellValue(record.getR18_avg_pula_equivalent().doubleValue());
//						R18cell18.setCellStyle(numberStyle);
//					} else {
//						R18cell19.setCellValue("");
//						R18cell19.setCellStyle(textStyle);
//					}

//Entity 2
// R28 Col B
					row = sheet.getRow(27);
					Cell R28cell1 = row.createCell(1);
					if (record.getR28_import() != null) {
						R28cell1.setCellValue(record.getR28_import().doubleValue());
						R28cell1.setCellStyle(numberStyle);
					} else {
						R28cell1.setCellValue("");
						R28cell1.setCellStyle(textStyle);
					}
// R28 Col C
					Cell R28cell2 = row.createCell(2);
					if (record.getR28_investment() != null) {
						R28cell2.setCellValue(record.getR28_investment().doubleValue());
						R28cell2.setCellStyle(numberStyle);
					} else {
						R28cell2.setCellValue("");
						R28cell2.setCellStyle(textStyle);
					}
// R28 Col D
					Cell R28cell3 = row.createCell(3);
					if (record.getR28_other() != null) {
						R28cell3.setCellValue(record.getR28_other().doubleValue());
						R28cell3.setCellStyle(numberStyle);
					} else {
						R28cell3.setCellValue("");
						R28cell3.setCellStyle(textStyle);
					}
// R29 Col B
					row = sheet.getRow(28);
					Cell R29cell1 = row.createCell(1);
					if (record.getR29_import() != null) {
						R29cell1.setCellValue(record.getR29_import().doubleValue());
						R29cell1.setCellStyle(numberStyle);
					} else {
						R29cell1.setCellValue("");
						R29cell1.setCellStyle(textStyle);
					}
// R29 Col C
					Cell R29cell2 = row.createCell(2);
					if (record.getR29_investment() != null) {
						R29cell2.setCellValue(record.getR29_investment().doubleValue());
						R29cell2.setCellStyle(numberStyle);
					} else {
						R29cell2.setCellValue("");
						R29cell2.setCellStyle(textStyle);
					}
// R29 Col D
					Cell R29cell3 = row.createCell(3);
					if (record.getR29_other() != null) {
						R29cell3.setCellValue(record.getR29_other().doubleValue());
						R29cell3.setCellStyle(numberStyle);
					} else {
						R29cell3.setCellValue("");
						R29cell3.setCellStyle(textStyle);
					}

// R30 Col B
					row = sheet.getRow(29);
					Cell R30cell1 = row.createCell(1);
					if (record.getR30_import() != null) {
						R30cell1.setCellValue(record.getR30_import().doubleValue());
						R30cell1.setCellStyle(numberStyle);
					} else {
						R30cell1.setCellValue("");
						R30cell1.setCellStyle(textStyle);
					}
// R30 Col C
					Cell R30cell2 = row.createCell(2);
					if (record.getR30_investment() != null) {
						R30cell2.setCellValue(record.getR30_investment().doubleValue());
						R30cell2.setCellStyle(numberStyle);
					} else {
						R30cell2.setCellValue("");
						R30cell2.setCellStyle(textStyle);
					}
// R30 Col D
					Cell R30cell3 = row.createCell(3);
					if (record.getR30_other() != null) {
						R30cell3.setCellValue(record.getR30_other().doubleValue());
						R30cell3.setCellStyle(numberStyle);
					} else {
						R30cell3.setCellValue("");
						R30cell3.setCellStyle(textStyle);
					}
// R31 Col B
					row = sheet.getRow(30);
					Cell R31cell1 = row.createCell(1);
					if (record.getR31_import() != null) {
						R31cell1.setCellValue(record.getR31_import().doubleValue());
						R31cell1.setCellStyle(numberStyle);
					} else {
						R31cell1.setCellValue("");
						R31cell1.setCellStyle(textStyle);
					}
// R31 Col C
					Cell R31cell2 = row.createCell(2);
					if (record.getR31_investment() != null) {
						R31cell2.setCellValue(record.getR31_investment().doubleValue());
						R31cell2.setCellStyle(numberStyle);
					} else {
						R31cell2.setCellValue("");
						R31cell2.setCellStyle(textStyle);
					}
// R31 Col D
					Cell R31cell3 = row.createCell(3);
					if (record.getR31_other() != null) {
						R31cell3.setCellValue(record.getR31_other().doubleValue());
						R31cell3.setCellStyle(numberStyle);
					} else {
						R31cell3.setCellValue("");
						R31cell3.setCellStyle(textStyle);
					}
// R32 Col B
					row = sheet.getRow(31);
					Cell R32cell1 = row.createCell(1);
					if (record.getR32_import() != null) {
						R32cell1.setCellValue(record.getR32_import().doubleValue());
						R32cell1.setCellStyle(numberStyle);
					} else {
						R32cell1.setCellValue("");
						R32cell1.setCellStyle(textStyle);
					}
// R32 Col C
					Cell R32cell2 = row.createCell(2);
					if (record.getR32_investment() != null) {
						R32cell2.setCellValue(record.getR32_investment().doubleValue());
						R32cell2.setCellStyle(numberStyle);
					} else {
						R32cell2.setCellValue("");
						R32cell2.setCellStyle(textStyle);
					}
// R32 Col D
					Cell R32cell3 = row.createCell(3);
					if (record.getR32_other() != null) {
						R32cell3.setCellValue(record.getR32_other().doubleValue());
						R32cell3.setCellStyle(numberStyle);
					} else {
						R32cell3.setCellValue("");
						R32cell3.setCellStyle(textStyle);
					}
// R33 Col B
					row = sheet.getRow(32);
					Cell R33cell1 = row.createCell(1);
					if (record.getR33_import() != null) {
						R33cell1.setCellValue(record.getR33_import().doubleValue());
						R33cell1.setCellStyle(numberStyle);
					} else {
						R33cell1.setCellValue("");
						R33cell1.setCellStyle(textStyle);
					}
// R33 Col C
					Cell R33cell2 = row.createCell(2);
					if (record.getR33_investment() != null) {
						R33cell2.setCellValue(record.getR33_investment().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}
// R33 Col D
					Cell R33cell3 = row.createCell(3);
					if (record.getR33_other() != null) {
						R33cell3.setCellValue(record.getR33_other().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

//Entity 3

					row = sheet.getRow(27);
					Cell R28cell1e3 = row.createCell(8);
					if (record.getR28_residents() != null) {
						R28cell1e3.setCellValue(record.getR28_residents().doubleValue());
						R28cell1e3.setCellStyle(numberStyle);
					} else {
						R28cell1e3.setCellValue("");
						R28cell1e3.setCellStyle(textStyle);
					}
// R28 Col C
					Cell R28cell2e3 = row.createCell(9);
					if (record.getR28_non_residents() != null) {
						R28cell2e3.setCellValue(record.getR28_non_residents().doubleValue());
						R28cell2e3.setCellStyle(numberStyle);
					} else {
						R28cell2e3.setCellValue("");
						R28cell2e3.setCellStyle(textStyle);
					}
// R29 Col B
					row = sheet.getRow(28);
					Cell R29cell1e3 = row.createCell(8);
					if (record.getR29_residents() != null) {
						R29cell1e3.setCellValue(record.getR29_residents().doubleValue());
						R29cell1e3.setCellStyle(numberStyle);
					} else {
						R29cell1e3.setCellValue("");
						R29cell1e3.setCellStyle(textStyle);
					}
// R29 Col C
					Cell R29cell2e3 = row.createCell(9);
					if (record.getR29_non_residents() != null) {
						R29cell2e3.setCellValue(record.getR29_non_residents().doubleValue());
						R29cell2e3.setCellStyle(numberStyle);
					} else {
						R29cell2e3.setCellValue("");
						R29cell2e3.setCellStyle(textStyle);
					}
// R30 Col B
					row = sheet.getRow(29);
					Cell R30cell1e3 = row.createCell(8);
					if (record.getR30_residents() != null) {
						R30cell1e3.setCellValue(record.getR30_residents().doubleValue());
						R30cell1e3.setCellStyle(numberStyle);
					} else {
						R30cell1e3.setCellValue("");
						R30cell1e3.setCellStyle(textStyle);
					}
// R30 Col C
					Cell R30cell2e3 = row.createCell(9);
					if (record.getR30_non_residents() != null) {
						R30cell2e3.setCellValue(record.getR30_non_residents().doubleValue());
						R30cell2e3.setCellStyle(numberStyle);
					} else {
						R30cell2e3.setCellValue("");
						R30cell2e3.setCellStyle(textStyle);
					}
// R31 Col B
					row = sheet.getRow(30);
					Cell R31cell1e3 = row.createCell(8);
					if (record.getR31_residents() != null) {
						R31cell1e3.setCellValue(record.getR31_residents().doubleValue());
						R31cell1e3.setCellStyle(numberStyle);
					} else {
						R31cell1e3.setCellValue("");
						R31cell1e3.setCellStyle(textStyle);
					}
// R31 Col C
					Cell R31cell2e3 = row.createCell(9);
					if (record.getR31_non_residents() != null) {
						R31cell2e3.setCellValue(record.getR31_non_residents().doubleValue());
						R31cell2e3.setCellStyle(numberStyle);
					} else {
						R31cell2e3.setCellValue("");
						R31cell2e3.setCellStyle(textStyle);
					}

// R32 Col B
					row = sheet.getRow(31);
					Cell R32cell1e3 = row.createCell(8);
					if (record.getR32_residents() != null) {
						R32cell1e3.setCellValue(record.getR32_residents().doubleValue());
						R32cell1e3.setCellStyle(numberStyle);
					} else {
						R32cell1e3.setCellValue("");
						R32cell1e3.setCellStyle(textStyle);
					}
// R32 Col C
					Cell R32cell2e3 = row.createCell(9);
					if (record.getR32_non_residents() != null) {
						R32cell2e3.setCellValue(record.getR32_non_residents().doubleValue());
						R32cell2e3.setCellStyle(numberStyle);
					} else {
						R32cell2e3.setCellValue("");
						R32cell2e3.setCellStyle(textStyle);
					}
// R33 Col B
					row = sheet.getRow(32);
					Cell R33cell1e3 = row.createCell(8);
					if (record.getR33_residents() != null) {
						R33cell1e3.setCellValue(record.getR33_residents().doubleValue());
						R33cell1e3.setCellStyle(numberStyle);
					} else {
						R33cell1e3.setCellValue("");
						R33cell1e3.setCellStyle(textStyle);
					}
// R33 Col C
					Cell R33cell2e3 = row.createCell(9);
					if (record.getR33_non_residents() != null) {
						R33cell2e3.setCellValue(record.getR33_non_residents().doubleValue());
						R33cell2e3.setCellStyle(numberStyle);
					} else {
						R33cell2e3.setCellValue("");
						R33cell2e3.setCellStyle(textStyle);
					}

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_DEP3 ARCHIVAL SUMMARY", null,
						"M_DEP3_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}

	}

	// Archival Email Excel
	// ------------------------------
	// GENERATE EMAIL ARCHIVAL EXCEL
	// ------------------------------
	public byte[] BRRS_M_DEP3EmailArchivalExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_DEP3_Archival_Summary_Entity> dataList = jdbcTemplate.query(
				"select * from BRRS_M_DEP3_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ? and REPORT_VERSION = ?",
				new Object[] { dateformat.parse(todate), version }, new M_DEP3_Archival_Summary_EntityRowMapper());

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_DEP3 report. Returning empty result.");
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
					M_DEP3_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//EMAIL
					Cell R12Cell = row.createCell(3);

					if (record.getReportDate() != null) {

						R12Cell.setCellValue(record.getReportDate());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(10);
					// R11 Col B

					Cell R11cell1 = row.createCell(1);
					if (record.getR11_ex_rate_buy() != null) {
						R11cell1.setCellValue(record.getR11_ex_rate_buy().doubleValue());
						R11cell1.setCellStyle(numberStyle);
					} else {
						R11cell1.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}

// R11 Col C
					Cell R11cell2 = row.createCell(2);
					if (record.getR11_ex_rate_mid() != null) {
						R11cell2.setCellValue(record.getR11_ex_rate_mid().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}

// R11 Col D
					Cell R11cell3 = row.createCell(3);
					if (record.getR11_ex_rate_sell() != null) {
						R11cell3.setCellValue(record.getR11_ex_rate_sell().doubleValue());
						R11cell3.setCellStyle(numberStyle);
					} else {
						R11cell3.setCellValue("");
						R11cell3.setCellStyle(textStyle);
					}

// R11 Col E
					Cell R11cell4 = row.createCell(4);
					if (record.getR11_current() != null) {
						R11cell4.setCellValue(record.getR11_current().doubleValue());
						R11cell4.setCellStyle(numberStyle);
					} else {
						R11cell4.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}

// R11 Col F
					Cell R11cell5 = row.createCell(5);
					if (record.getR11_call() != null) {
						R11cell5.setCellValue(record.getR11_call().doubleValue());
						R11cell5.setCellStyle(numberStyle);
					} else {
						R11cell5.setCellValue("");
						R11cell5.setCellStyle(textStyle);
					}

// R11 Col G
					Cell R11cell6 = row.createCell(6);
					if (record.getR11_savings() != null) {
						R11cell6.setCellValue(record.getR11_savings().doubleValue());
						R11cell6.setCellStyle(numberStyle);
					} else {
						R11cell6.setCellValue("");
						R11cell6.setCellStyle(textStyle);

					}

// R11 Col H
					Cell R11cell7 = row.createCell(7);
					if (record.getR11_notice_0to31() != null) {
						R11cell7.setCellValue(record.getR11_notice_0to31().doubleValue());
						R11cell7.setCellStyle(numberStyle);
					} else {
						R11cell7.setCellValue("");
						R11cell7.setCellStyle(textStyle);

					}

// R11 Col I
					Cell R11cell8 = row.createCell(8);
					if (record.getR11_notice_32to88() != null) {
						R11cell8.setCellValue(record.getR11_notice_32to88().doubleValue());
						R11cell8.setCellStyle(numberStyle);
					} else {
						R11cell8.setCellValue("");
						R11cell8.setCellStyle(textStyle);
					}

// R11 Col J
					Cell R11cell9 = row.createCell(9);
					if (record.getR11_fix_depo_91_day_depo() != null) {
						R11cell9.setCellValue(record.getR11_fix_depo_91_day_depo().doubleValue());
						R11cell9.setCellStyle(numberStyle);
					} else {
						R11cell9.setCellValue("");
						R11cell9.setCellStyle(textStyle);
					}

// R11 Col K
					Cell R11cell10 = row.createCell(10);
					if (record.getR11_fix_depo_1to2() != null) {
						R11cell10.setCellValue(record.getR11_fix_depo_1to2().doubleValue());
						R11cell10.setCellStyle(numberStyle);
					} else {
						R11cell10.setCellValue("");
						R11cell10.setCellStyle(textStyle);
					}

// R11 Col L
					Cell R11cell11 = row.createCell(11);
					if (record.getR11_fix_depo_4to6() != null) {
						R11cell11.setCellValue(record.getR11_fix_depo_4to6().doubleValue());
						R11cell11.setCellStyle(numberStyle);
					} else {
						R11cell11.setCellValue("");
						R11cell11.setCellStyle(textStyle);
					}

// R11 Col M
					Cell R11cell12 = row.createCell(12);
					if (record.getR11_fix_depo_7to12() != null) {
						R11cell12.setCellValue(record.getR11_fix_depo_7to12().doubleValue());
						R11cell12.setCellStyle(numberStyle);
					} else {
						R11cell12.setCellValue("");
						R11cell12.setCellStyle(textStyle);
					}

// R11 Col N
					Cell R11cell13 = row.createCell(13);
					if (record.getR11_fix_depo_13to18() != null) {
						R11cell13.setCellValue(record.getR11_fix_depo_13to18().doubleValue());
						R11cell13.setCellStyle(numberStyle);
					} else {
						R11cell13.setCellValue("");
						R11cell13.setCellStyle(textStyle);
					}

// R11 Col O
					Cell R11cell14 = row.createCell(14);
					if (record.getR11_fix_depo_19to24() != null) {
						R11cell14.setCellValue(record.getR11_fix_depo_19to24().doubleValue());
						R11cell14.setCellStyle(numberStyle);
					} else {
						R11cell14.setCellValue("");
						R11cell14.setCellStyle(textStyle);
					}

// R11 Col P
					Cell R11cell15 = row.createCell(15);
					if (record.getR11_fix_depo_over24() != null) {
						R11cell15.setCellValue(record.getR11_fix_depo_over24().doubleValue());
						R11cell15.setCellStyle(numberStyle);
					} else {
						R11cell15.setCellValue("");
						R11cell15.setCellStyle(textStyle);
					}

// R11 Col Q
					Cell R11cell16 = row.createCell(16);
					if (record.getR11_cer_of_depo() != null) {
						R11cell16.setCellValue(record.getR11_cer_of_depo().doubleValue());
						R11cell16.setCellStyle(numberStyle);
					} else {
						R11cell16.setCellValue("");
						R11cell16.setCellStyle(textStyle);

					}

// R11 Col S
					Cell R11cell18 = row.createCell(18);
					if (record.getR11_pula_equivalent() != null) {
						R11cell18.setCellValue(record.getR11_pula_equivalent().doubleValue());
						R11cell18.setCellStyle(numberStyle);
					} else {
						R11cell18.setCellValue("");
						R11cell18.setCellStyle(textStyle);
					}

// R11 Col T
					Cell R11cell19 = row.createCell(19);
					if (record.getR11_avg_pula_equivalent() != null) {
						R11cell19.setCellValue(record.getR11_avg_pula_equivalent().doubleValue());
						R11cell19.setCellStyle(numberStyle);
					} else {
						R11cell19.setCellValue("");
						R11cell19.setCellStyle(textStyle);
					}
					row = sheet.getRow(11);
// R12 Col B
					Cell R12cell1 = row.createCell(1);
					if (record.getR12_ex_rate_buy() != null) {
						R12cell1.setCellValue(record.getR12_ex_rate_buy().doubleValue());
						R12cell1.setCellStyle(numberStyle);
					} else {
						R12cell1.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

// R12 Col C
					Cell R12cell2 = row.createCell(2);
					if (record.getR12_ex_rate_mid() != null) {
						R12cell2.setCellValue(record.getR12_ex_rate_mid().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}

// R12 Col D
					Cell R12cell3 = row.createCell(3);
					if (record.getR12_ex_rate_sell() != null) {
						R12cell3.setCellValue(record.getR12_ex_rate_sell().doubleValue());
						R12cell3.setCellStyle(numberStyle);
					} else {
						R12cell3.setCellValue("");
						R12cell3.setCellStyle(textStyle);
					}

// R12 Col E
					Cell R12cell4 = row.createCell(4);
					if (record.getR12_current() != null) {
						R12cell4.setCellValue(record.getR12_current().doubleValue());
						R12cell4.setCellStyle(numberStyle);
					} else {
						R12cell4.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

// R12 Col F
					Cell R12cell5 = row.createCell(5);
					if (record.getR12_call() != null) {
						R12cell5.setCellValue(record.getR12_call().doubleValue());
						R12cell5.setCellStyle(numberStyle);
					} else {
						R12cell5.setCellValue("");
						R12cell5.setCellStyle(textStyle);
					}

// R12 Col G
					Cell R12cell6 = row.createCell(6);
					if (record.getR12_savings() != null) {
						R12cell6.setCellValue(record.getR12_savings().doubleValue());
						R12cell6.setCellStyle(numberStyle);
					} else {
						R12cell6.setCellValue("");
						R12cell6.setCellStyle(textStyle);

					}

// R12 Col H
					Cell R12cell7 = row.createCell(7);
					if (record.getR12_notice_0to31() != null) {
						R12cell7.setCellValue(record.getR12_notice_0to31().doubleValue());
						R12cell7.setCellStyle(numberStyle);
					} else {
						R12cell7.setCellValue("");
						R12cell7.setCellStyle(textStyle);

					}

// R12 Col I
					Cell R12cell8 = row.createCell(8);
					if (record.getR12_notice_32to88() != null) {
						R12cell8.setCellValue(record.getR12_notice_32to88().doubleValue());
						R12cell8.setCellStyle(numberStyle);
					} else {
						R12cell8.setCellValue("");
						R12cell8.setCellStyle(textStyle);
					}

// R12 Col J
					Cell R12cell9 = row.createCell(9);
					if (record.getR12_fix_depo_91_day_depo() != null) {
						R12cell9.setCellValue(record.getR12_fix_depo_91_day_depo().doubleValue());
						R12cell9.setCellStyle(numberStyle);
					} else {
						R12cell9.setCellValue("");
						R12cell9.setCellStyle(textStyle);
					}

// R12 Col K
					Cell R12cell10 = row.createCell(10);
					if (record.getR12_fix_depo_1to2() != null) {
						R12cell10.setCellValue(record.getR12_fix_depo_1to2().doubleValue());
						R12cell10.setCellStyle(numberStyle);
					} else {
						R12cell10.setCellValue("");
						R12cell10.setCellStyle(textStyle);
					}

// R12 Col L
					Cell R12cell11 = row.createCell(11);
					if (record.getR12_fix_depo_4to6() != null) {
						R12cell11.setCellValue(record.getR12_fix_depo_4to6().doubleValue());
						R12cell11.setCellStyle(numberStyle);
					} else {
						R12cell11.setCellValue("");
						R12cell11.setCellStyle(textStyle);
					}

// R12 Col M
					Cell R12cell12 = row.createCell(12);
					if (record.getR12_fix_depo_7to12() != null) {
						R12cell12.setCellValue(record.getR12_fix_depo_7to12().doubleValue());
						R12cell12.setCellStyle(numberStyle);
					} else {
						R12cell12.setCellValue("");
						R12cell12.setCellStyle(textStyle);
					}

// R12 Col N
					Cell R12cell13 = row.createCell(13);
					if (record.getR12_fix_depo_13to18() != null) {
						R12cell13.setCellValue(record.getR12_fix_depo_13to18().doubleValue());
						R12cell13.setCellStyle(numberStyle);
					} else {
						R12cell13.setCellValue("");
						R12cell13.setCellStyle(textStyle);
					}

// R12 Col O
					Cell R12cell14 = row.createCell(14);
					if (record.getR12_fix_depo_19to24() != null) {
						R12cell14.setCellValue(record.getR12_fix_depo_19to24().doubleValue());
						R12cell14.setCellStyle(numberStyle);
					} else {
						R12cell14.setCellValue("");
						R12cell14.setCellStyle(textStyle);
					}

// R12 Col P
					Cell R12cell15 = row.createCell(15);
					if (record.getR12_fix_depo_over24() != null) {
						R12cell15.setCellValue(record.getR12_fix_depo_over24().doubleValue());
						R12cell15.setCellStyle(numberStyle);
					} else {
						R12cell15.setCellValue("");
						R12cell15.setCellStyle(textStyle);
					}

// R12 Col Q
					Cell R12cell16 = row.createCell(16);
					if (record.getR12_cer_of_depo() != null) {
						R12cell16.setCellValue(record.getR12_cer_of_depo().doubleValue());
						R12cell16.setCellStyle(numberStyle);
					} else {
						R12cell16.setCellValue("");
						R12cell16.setCellStyle(textStyle);
					}

// R12 Col S
					Cell R12cell18 = row.createCell(18);
					if (record.getR12_pula_equivalent() != null) {
						R12cell18.setCellValue(record.getR12_pula_equivalent().doubleValue());
						R12cell18.setCellStyle(numberStyle);
					} else {
						R12cell18.setCellValue("");
						R12cell18.setCellStyle(textStyle);
					}

// R12 Col T
					Cell R12cell19 = row.createCell(19);
					if (record.getR12_avg_pula_equivalent() != null) {
						R12cell19.setCellValue(record.getR12_avg_pula_equivalent().doubleValue());
						R12cell19.setCellStyle(numberStyle);
					} else {
						R12cell19.setCellValue("");
						R12cell19.setCellStyle(textStyle);
					}

// R13 Col B
					row = sheet.getRow(12);
					Cell R13cell1 = row.createCell(1);
					if (record.getR13_ex_rate_buy() != null) {
						R13cell1.setCellValue(record.getR13_ex_rate_buy().doubleValue());
						R13cell1.setCellStyle(numberStyle);
					} else {
						R13cell1.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

// R13 Col C
					Cell R13cell2 = row.createCell(2);
					if (record.getR13_ex_rate_mid() != null) {
						R13cell2.setCellValue(record.getR13_ex_rate_mid().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}

// R13 Col D
					Cell R13cell3 = row.createCell(3);
					if (record.getR13_ex_rate_sell() != null) {
						R13cell3.setCellValue(record.getR13_ex_rate_sell().doubleValue());
						R13cell3.setCellStyle(numberStyle);
					} else {
						R13cell3.setCellValue("");
						R13cell3.setCellStyle(textStyle);
					}

// R13 Col E
					Cell R13cell4 = row.createCell(4);
					if (record.getR13_current() != null) {
						R13cell4.setCellValue(record.getR13_current().doubleValue());
						R13cell4.setCellStyle(numberStyle);
					} else {
						R13cell4.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

// R13 Col F
					Cell R13cell5 = row.createCell(5);
					if (record.getR13_call() != null) {
						R13cell5.setCellValue(record.getR13_call().doubleValue());
						R13cell5.setCellStyle(numberStyle);
					} else {
						R13cell5.setCellValue("");
						R13cell5.setCellStyle(textStyle);
					}

// R13 Col G
					Cell R13cell6 = row.createCell(6);
					if (record.getR13_savings() != null) {
						R13cell6.setCellValue(record.getR13_savings().doubleValue());
						R13cell6.setCellStyle(numberStyle);
					} else {
						R13cell6.setCellValue("");
						R13cell6.setCellStyle(textStyle);

					}

// R13 Col H
					Cell R13cell7 = row.createCell(7);
					if (record.getR13_notice_0to31() != null) {
						R13cell7.setCellValue(record.getR13_notice_0to31().doubleValue());
						R13cell7.setCellStyle(numberStyle);
					} else {
						R13cell7.setCellValue("");
						R13cell7.setCellStyle(textStyle);

					}

// R13 Col I
					Cell R13cell8 = row.createCell(8);
					if (record.getR13_notice_32to88() != null) {
						R13cell8.setCellValue(record.getR13_notice_32to88().doubleValue());
						R13cell8.setCellStyle(numberStyle);
					} else {
						R13cell8.setCellValue("");
						R13cell8.setCellStyle(textStyle);
					}

// R13 Col J
					Cell R13cell9 = row.createCell(9);
					if (record.getR13_fix_depo_91_day_depo() != null) {
						R13cell9.setCellValue(record.getR13_fix_depo_91_day_depo().doubleValue());
						R13cell9.setCellStyle(numberStyle);
					} else {
						R13cell9.setCellValue("");
						R13cell9.setCellStyle(textStyle);
					}

// R13 Col K
					Cell R13cell10 = row.createCell(10);
					if (record.getR13_fix_depo_1to2() != null) {
						R13cell10.setCellValue(record.getR13_fix_depo_1to2().doubleValue());
						R13cell10.setCellStyle(numberStyle);
					} else {
						R13cell10.setCellValue("");
						R13cell10.setCellStyle(textStyle);
					}

// R13 Col L
					Cell R13cell11 = row.createCell(11);
					if (record.getR13_fix_depo_4to6() != null) {
						R13cell11.setCellValue(record.getR13_fix_depo_4to6().doubleValue());
						R13cell11.setCellStyle(numberStyle);
					} else {
						R13cell11.setCellValue("");
						R13cell11.setCellStyle(textStyle);
					}

// R13 Col M
					Cell R13cell12 = row.createCell(12);
					if (record.getR13_fix_depo_7to12() != null) {
						R13cell12.setCellValue(record.getR13_fix_depo_7to12().doubleValue());
						R13cell12.setCellStyle(numberStyle);
					} else {
						R13cell12.setCellValue("");
						R13cell12.setCellStyle(textStyle);
					}

// R13 Col N
					Cell R13cell13 = row.createCell(13);
					if (record.getR13_fix_depo_13to18() != null) {
						R13cell13.setCellValue(record.getR13_fix_depo_13to18().doubleValue());
						R13cell13.setCellStyle(numberStyle);
					} else {
						R13cell13.setCellValue("");
						R13cell13.setCellStyle(textStyle);
					}

// R13 Col O
					Cell R13cell14 = row.createCell(14);
					if (record.getR13_fix_depo_19to24() != null) {
						R13cell14.setCellValue(record.getR13_fix_depo_19to24().doubleValue());
						R13cell14.setCellStyle(numberStyle);
					} else {
						R13cell14.setCellValue("");
						R13cell14.setCellStyle(textStyle);
					}

// R13 Col P
					Cell R13cell15 = row.createCell(15);
					if (record.getR13_fix_depo_over24() != null) {
						R13cell15.setCellValue(record.getR13_fix_depo_over24().doubleValue());
						R13cell15.setCellStyle(numberStyle);
					} else {
						R13cell15.setCellValue("");
						R13cell15.setCellStyle(textStyle);
					}

// R13 Col Q
					Cell R13cell16 = row.createCell(16);
					if (record.getR13_cer_of_depo() != null) {
						R13cell16.setCellValue(record.getR13_cer_of_depo().doubleValue());
						R13cell16.setCellStyle(numberStyle);
					} else {
						R13cell16.setCellValue("");
						R13cell16.setCellStyle(textStyle);
					}

// R13 Col S
					Cell R13cell18 = row.createCell(18);
					if (record.getR13_pula_equivalent() != null) {
						R13cell18.setCellValue(record.getR13_pula_equivalent().doubleValue());
						R13cell18.setCellStyle(numberStyle);
					} else {
						R13cell18.setCellValue("");
						R13cell18.setCellStyle(textStyle);
					}

// R13 Col T
					Cell R13cell19 = row.createCell(19);
					if (record.getR13_avg_pula_equivalent() != null) {
						R13cell19.setCellValue(record.getR13_avg_pula_equivalent().doubleValue());
						R13cell19.setCellStyle(numberStyle);
					} else {
						R13cell19.setCellValue("");
						R13cell19.setCellStyle(textStyle);
					}

// R14 Col B
					row = sheet.getRow(13);
					Cell R14cell1 = row.createCell(1);
					if (record.getR14_ex_rate_buy() != null) {
						R14cell1.setCellValue(record.getR14_ex_rate_buy().doubleValue());
						R14cell1.setCellStyle(numberStyle);
					} else {
						R14cell1.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

// R14 Col C
					Cell R14cell2 = row.createCell(2);
					if (record.getR14_ex_rate_mid() != null) {
						R14cell2.setCellValue(record.getR14_ex_rate_mid().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}

// R14 Col D
					Cell R14cell3 = row.createCell(3);
					if (record.getR14_ex_rate_sell() != null) {
						R14cell3.setCellValue(record.getR14_ex_rate_sell().doubleValue());
						R14cell3.setCellStyle(numberStyle);
					} else {
						R14cell3.setCellValue("");
						R14cell3.setCellStyle(textStyle);
					}

// R14 Col E
					Cell R14cell4 = row.createCell(4);
					if (record.getR14_current() != null) {
						R14cell4.setCellValue(record.getR14_current().doubleValue());
						R14cell4.setCellStyle(numberStyle);
					} else {
						R14cell4.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

// R14 Col F
					Cell R14cell5 = row.createCell(5);
					if (record.getR14_call() != null) {
						R14cell5.setCellValue(record.getR14_call().doubleValue());
						R14cell5.setCellStyle(numberStyle);
					} else {
						R14cell5.setCellValue("");
						R14cell5.setCellStyle(textStyle);
					}

// R14 Col G
					Cell R14cell6 = row.createCell(6);
					if (record.getR14_savings() != null) {
						R14cell6.setCellValue(record.getR14_savings().doubleValue());
						R14cell6.setCellStyle(numberStyle);
					} else {
						R14cell6.setCellValue("");
						R14cell6.setCellStyle(textStyle);

					}

// R14 Col H
					Cell R14cell7 = row.createCell(7);
					if (record.getR14_notice_0to31() != null) {
						R14cell7.setCellValue(record.getR14_notice_0to31().doubleValue());
						R14cell7.setCellStyle(numberStyle);
					} else {
						R14cell7.setCellValue("");
						R14cell7.setCellStyle(textStyle);

					}

// R14 Col I
					Cell R14cell8 = row.createCell(8);
					if (record.getR14_notice_32to88() != null) {
						R14cell8.setCellValue(record.getR14_notice_32to88().doubleValue());
						R14cell8.setCellStyle(numberStyle);
					} else {
						R14cell8.setCellValue("");
						R14cell8.setCellStyle(textStyle);
					}

// R14 Col J
					Cell R14cell9 = row.createCell(9);
					if (record.getR14_fix_depo_91_day_depo() != null) {
						R14cell9.setCellValue(record.getR14_fix_depo_91_day_depo().doubleValue());
						R14cell9.setCellStyle(numberStyle);
					} else {
						R14cell9.setCellValue("");
						R14cell9.setCellStyle(textStyle);
					}

// R14 Col K
					Cell R14cell10 = row.createCell(10);
					if (record.getR14_fix_depo_1to2() != null) {
						R14cell10.setCellValue(record.getR14_fix_depo_1to2().doubleValue());
						R14cell10.setCellStyle(numberStyle);
					} else {
						R14cell10.setCellValue("");
						R14cell10.setCellStyle(textStyle);
					}

// R14 Col L
					Cell R14cell11 = row.createCell(11);
					if (record.getR14_fix_depo_4to6() != null) {
						R14cell11.setCellValue(record.getR14_fix_depo_4to6().doubleValue());
						R14cell11.setCellStyle(numberStyle);
					} else {
						R14cell11.setCellValue("");
						R14cell11.setCellStyle(textStyle);
					}

// R14 Col M
					Cell R14cell12 = row.createCell(12);
					if (record.getR14_fix_depo_7to12() != null) {
						R14cell12.setCellValue(record.getR14_fix_depo_7to12().doubleValue());
						R14cell12.setCellStyle(numberStyle);
					} else {
						R14cell12.setCellValue("");
						R14cell12.setCellStyle(textStyle);
					}

// R14 Col N
					Cell R14cell13 = row.createCell(13);
					if (record.getR14_fix_depo_13to18() != null) {
						R14cell13.setCellValue(record.getR14_fix_depo_13to18().doubleValue());
						R14cell13.setCellStyle(numberStyle);
					} else {
						R14cell13.setCellValue("");
						R14cell13.setCellStyle(textStyle);
					}

// R14 Col O
					Cell R14cell14 = row.createCell(14);
					if (record.getR14_fix_depo_19to24() != null) {
						R14cell14.setCellValue(record.getR14_fix_depo_19to24().doubleValue());
						R14cell14.setCellStyle(numberStyle);
					} else {
						R14cell14.setCellValue("");
						R14cell14.setCellStyle(textStyle);
					}

// R14 Col P
					Cell R14cell15 = row.createCell(15);
					if (record.getR14_fix_depo_over24() != null) {
						R14cell15.setCellValue(record.getR14_fix_depo_over24().doubleValue());
						R14cell15.setCellStyle(numberStyle);
					} else {
						R14cell15.setCellValue("");
						R14cell15.setCellStyle(textStyle);
					}

// R14 Col Q
					Cell R14cell16 = row.createCell(16);
					if (record.getR14_cer_of_depo() != null) {
						R14cell16.setCellValue(record.getR14_cer_of_depo().doubleValue());
						R14cell16.setCellStyle(numberStyle);
					} else {
						R14cell16.setCellValue("");
						R14cell16.setCellStyle(textStyle);
					}

// R14 Col S
					Cell R14cell18 = row.createCell(18);
					if (record.getR14_pula_equivalent() != null) {
						R14cell18.setCellValue(record.getR14_pula_equivalent().doubleValue());
						R14cell18.setCellStyle(numberStyle);
					} else {
						R14cell18.setCellValue("");
						R14cell18.setCellStyle(textStyle);
					}

// R14 Col T
					Cell R14cell19 = row.createCell(19);
					if (record.getR14_avg_pula_equivalent() != null) {
						R14cell19.setCellValue(record.getR14_avg_pula_equivalent().doubleValue());
						R14cell19.setCellStyle(numberStyle);
					} else {
						R14cell19.setCellValue("");
						R14cell19.setCellStyle(textStyle);
					}

// R15 Col B
					row = sheet.getRow(14);
					Cell R15cell1 = row.createCell(1);
					if (record.getR15_ex_rate_buy() != null) {
						R15cell1.setCellValue(record.getR15_ex_rate_buy().doubleValue());
						R15cell1.setCellStyle(numberStyle);
					} else {
						R15cell1.setCellValue("");
						R15cell1.setCellStyle(textStyle);
					}

// R15 Col C
					Cell R15cell2 = row.createCell(2);
					if (record.getR15_ex_rate_mid() != null) {
						R15cell2.setCellValue(record.getR15_ex_rate_mid().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);
					}

// R15 Col D
					Cell R15cell3 = row.createCell(3);
					if (record.getR15_ex_rate_sell() != null) {
						R15cell3.setCellValue(record.getR15_ex_rate_sell().doubleValue());
						R15cell3.setCellStyle(numberStyle);
					} else {
						R15cell3.setCellValue("");
						R15cell3.setCellStyle(textStyle);
					}

// R15 Col E
					Cell R15cell4 = row.createCell(4);
					if (record.getR15_current() != null) {
						R15cell4.setCellValue(record.getR15_current().doubleValue());
						R15cell4.setCellStyle(numberStyle);
					} else {
						R15cell4.setCellValue("");
						R15cell1.setCellStyle(textStyle);
					}

// R15 Col F
					Cell R15cell5 = row.createCell(5);
					if (record.getR15_call() != null) {
						R15cell5.setCellValue(record.getR15_call().doubleValue());
						R15cell5.setCellStyle(numberStyle);
					} else {
						R15cell5.setCellValue("");
						R15cell5.setCellStyle(textStyle);
					}

// R15 Col G
					Cell R15cell6 = row.createCell(6);
					if (record.getR15_savings() != null) {
						R15cell6.setCellValue(record.getR15_savings().doubleValue());
						R15cell6.setCellStyle(numberStyle);
					} else {
						R15cell6.setCellValue("");
						R15cell6.setCellStyle(textStyle);

					}

// R15 Col H
					Cell R15cell7 = row.createCell(7);
					if (record.getR15_notice_0to31() != null) {
						R15cell7.setCellValue(record.getR15_notice_0to31().doubleValue());
						R15cell7.setCellStyle(numberStyle);
					} else {
						R15cell7.setCellValue("");
						R15cell7.setCellStyle(textStyle);

					}

// R15 Col I
					Cell R15cell8 = row.createCell(8);
					if (record.getR15_notice_32to88() != null) {
						R15cell8.setCellValue(record.getR15_notice_32to88().doubleValue());
						R15cell8.setCellStyle(numberStyle);
					} else {
						R15cell8.setCellValue("");
						R15cell8.setCellStyle(textStyle);
					}

// R15 Col J
					Cell R15cell9 = row.createCell(9);
					if (record.getR15_fix_depo_91_day_depo() != null) {
						R15cell9.setCellValue(record.getR15_fix_depo_91_day_depo().doubleValue());
						R15cell9.setCellStyle(numberStyle);
					} else {
						R15cell9.setCellValue("");
						R15cell9.setCellStyle(textStyle);
					}

// R15 Col K
					Cell R15cell10 = row.createCell(10);
					if (record.getR15_fix_depo_1to2() != null) {
						R15cell10.setCellValue(record.getR15_fix_depo_1to2().doubleValue());
						R15cell10.setCellStyle(numberStyle);
					} else {
						R15cell10.setCellValue("");
						R15cell10.setCellStyle(textStyle);
					}

// R15 Col L
					Cell R15cell11 = row.createCell(11);
					if (record.getR15_fix_depo_4to6() != null) {
						R15cell11.setCellValue(record.getR15_fix_depo_4to6().doubleValue());
						R15cell11.setCellStyle(numberStyle);
					} else {
						R15cell11.setCellValue("");
						R15cell11.setCellStyle(textStyle);
					}

// R15 Col M
					Cell R15cell12 = row.createCell(12);
					if (record.getR15_fix_depo_7to12() != null) {
						R15cell12.setCellValue(record.getR15_fix_depo_7to12().doubleValue());
						R15cell12.setCellStyle(numberStyle);
					} else {
						R15cell12.setCellValue("");
						R15cell12.setCellStyle(textStyle);
					}

// R15 Col N
					Cell R15cell13 = row.createCell(13);
					if (record.getR15_fix_depo_13to18() != null) {
						R15cell13.setCellValue(record.getR15_fix_depo_13to18().doubleValue());
						R15cell13.setCellStyle(numberStyle);
					} else {
						R15cell13.setCellValue("");
						R15cell13.setCellStyle(textStyle);
					}

// R15 Col O
					Cell R15cell14 = row.createCell(14);
					if (record.getR15_fix_depo_19to24() != null) {
						R15cell14.setCellValue(record.getR15_fix_depo_19to24().doubleValue());
						R15cell14.setCellStyle(numberStyle);
					} else {
						R15cell14.setCellValue("");
						R15cell14.setCellStyle(textStyle);
					}

// R15 Col P
					Cell R15cell15 = row.createCell(15);
					if (record.getR15_fix_depo_over24() != null) {
						R15cell15.setCellValue(record.getR15_fix_depo_over24().doubleValue());
						R15cell15.setCellStyle(numberStyle);
					} else {
						R15cell15.setCellValue("");
						R15cell15.setCellStyle(textStyle);
					}

// R15 Col Q
					Cell R15cell16 = row.createCell(16);
					if (record.getR15_cer_of_depo() != null) {
						R15cell16.setCellValue(record.getR15_cer_of_depo().doubleValue());
						R15cell16.setCellStyle(numberStyle);
					} else {
						R15cell16.setCellValue("");
						R15cell16.setCellStyle(textStyle);
					}

// R15 Col S
					Cell R15cell18 = row.createCell(18);
					if (record.getR15_pula_equivalent() != null) {
						R15cell18.setCellValue(record.getR15_pula_equivalent().doubleValue());
						R15cell18.setCellStyle(numberStyle);
					} else {
						R15cell18.setCellValue("");
						R15cell18.setCellStyle(textStyle);
					}

// R15 Col T
					Cell R15cell19 = row.createCell(19);
					if (record.getR15_avg_pula_equivalent() != null) {
						R15cell19.setCellValue(record.getR15_avg_pula_equivalent().doubleValue());
						R15cell19.setCellStyle(numberStyle);
					} else {
						R15cell19.setCellValue("");
						R15cell19.setCellStyle(textStyle);
					}

// R16 Col B
					row = sheet.getRow(15);
					Cell R16cell1 = row.createCell(1);
					if (record.getR16_ex_rate_buy() != null) {
						R16cell1.setCellValue(record.getR16_ex_rate_buy().doubleValue());
						R16cell1.setCellStyle(numberStyle);
					} else {
						R16cell1.setCellValue("");
						R16cell1.setCellStyle(textStyle);
					}

// R16 Col C
					Cell R16cell2 = row.createCell(2);
					if (record.getR16_ex_rate_mid() != null) {
						R16cell2.setCellValue(record.getR16_ex_rate_mid().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);
					}

// R16 Col D
					Cell R16cell3 = row.createCell(3);
					if (record.getR16_ex_rate_sell() != null) {
						R16cell3.setCellValue(record.getR16_ex_rate_sell().doubleValue());
						R16cell3.setCellStyle(numberStyle);
					} else {
						R16cell3.setCellValue("");
						R16cell3.setCellStyle(textStyle);
					}

// R16 Col E
					Cell R16cell4 = row.createCell(4);
					if (record.getR16_current() != null) {
						R16cell4.setCellValue(record.getR16_current().doubleValue());
						R16cell4.setCellStyle(numberStyle);
					} else {
						R16cell4.setCellValue("");
						R16cell1.setCellStyle(textStyle);
					}

// R16 Col F
					Cell R16cell5 = row.createCell(5);
					if (record.getR16_call() != null) {
						R16cell5.setCellValue(record.getR16_call().doubleValue());
						R16cell5.setCellStyle(numberStyle);
					} else {
						R16cell5.setCellValue("");
						R16cell5.setCellStyle(textStyle);
					}

// R16 Col G
					Cell R16cell6 = row.createCell(6);
					if (record.getR16_savings() != null) {
						R16cell6.setCellValue(record.getR16_savings().doubleValue());
						R16cell6.setCellStyle(numberStyle);
					} else {
						R16cell6.setCellValue("");
						R16cell6.setCellStyle(textStyle);

					}

// R16 Col H
					Cell R16cell7 = row.createCell(7);
					if (record.getR16_notice_0to31() != null) {
						R16cell7.setCellValue(record.getR16_notice_0to31().doubleValue());
						R16cell7.setCellStyle(numberStyle);
					} else {
						R16cell7.setCellValue("");
						R16cell7.setCellStyle(textStyle);

					}

// R16 Col I
					Cell R16cell8 = row.createCell(8);
					if (record.getR16_notice_32to88() != null) {
						R16cell8.setCellValue(record.getR16_notice_32to88().doubleValue());
						R16cell8.setCellStyle(numberStyle);
					} else {
						R16cell8.setCellValue("");
						R16cell8.setCellStyle(textStyle);
					}

// R16 Col J
					Cell R16cell9 = row.createCell(9);
					if (record.getR16_fix_depo_91_day_depo() != null) {
						R16cell9.setCellValue(record.getR16_fix_depo_91_day_depo().doubleValue());
						R16cell9.setCellStyle(numberStyle);
					} else {
						R16cell9.setCellValue("");
						R16cell9.setCellStyle(textStyle);
					}

// R16 Col K
					Cell R16cell10 = row.createCell(10);
					if (record.getR16_fix_depo_1to2() != null) {
						R16cell10.setCellValue(record.getR16_fix_depo_1to2().doubleValue());
						R16cell10.setCellStyle(numberStyle);
					} else {
						R16cell10.setCellValue("");
						R16cell10.setCellStyle(textStyle);
					}

// R16 Col L
					Cell R16cell11 = row.createCell(11);
					if (record.getR16_fix_depo_4to6() != null) {
						R16cell11.setCellValue(record.getR16_fix_depo_4to6().doubleValue());
						R16cell11.setCellStyle(numberStyle);
					} else {
						R16cell11.setCellValue("");
						R16cell11.setCellStyle(textStyle);
					}

// R16 Col M
					Cell R16cell12 = row.createCell(12);
					if (record.getR16_fix_depo_7to12() != null) {
						R16cell12.setCellValue(record.getR16_fix_depo_7to12().doubleValue());
						R16cell12.setCellStyle(numberStyle);
					} else {
						R16cell12.setCellValue("");
						R16cell12.setCellStyle(textStyle);
					}

// R16 Col N
					Cell R16cell13 = row.createCell(13);
					if (record.getR16_fix_depo_13to18() != null) {
						R16cell13.setCellValue(record.getR16_fix_depo_13to18().doubleValue());
						R16cell13.setCellStyle(numberStyle);
					} else {
						R16cell13.setCellValue("");
						R16cell13.setCellStyle(textStyle);
					}

// R16 Col O
					Cell R16cell14 = row.createCell(14);
					if (record.getR16_fix_depo_19to24() != null) {
						R16cell14.setCellValue(record.getR16_fix_depo_19to24().doubleValue());
						R16cell14.setCellStyle(numberStyle);
					} else {
						R16cell14.setCellValue("");
						R16cell14.setCellStyle(textStyle);
					}

// R16 Col P
					Cell R16cell15 = row.createCell(15);
					if (record.getR16_fix_depo_over24() != null) {
						R16cell15.setCellValue(record.getR16_fix_depo_over24().doubleValue());
						R16cell15.setCellStyle(numberStyle);
					} else {
						R16cell15.setCellValue("");
						R16cell15.setCellStyle(textStyle);

					}

// R16 Col Q
					Cell R16cell16 = row.createCell(16);
					if (record.getR16_cer_of_depo() != null) {
						R16cell16.setCellValue(record.getR16_cer_of_depo().doubleValue());
						R16cell16.setCellStyle(numberStyle);
					} else {
						R16cell16.setCellValue("");
						R16cell16.setCellStyle(textStyle);
					}

// R16 Col S
					Cell R16cell18 = row.createCell(18);
					if (record.getR16_pula_equivalent() != null) {
						R16cell18.setCellValue(record.getR16_pula_equivalent().doubleValue());
						R16cell18.setCellStyle(numberStyle);
					} else {
						R16cell18.setCellValue("");
						R16cell18.setCellStyle(textStyle);
					}

// R16 Col T
					Cell R16cell19 = row.createCell(19);
					if (record.getR16_avg_pula_equivalent() != null) {
						R16cell19.setCellValue(record.getR16_avg_pula_equivalent().doubleValue());
						R16cell19.setCellStyle(numberStyle);
					} else {
						R16cell19.setCellValue("");
						R16cell19.setCellStyle(textStyle);
					}

// R17 Col B
					row = sheet.getRow(16);
					Cell R17cell1 = row.createCell(1);
					if (record.getR17_ex_rate_buy() != null) {
						R17cell1.setCellValue(record.getR17_ex_rate_buy().doubleValue());
						R17cell1.setCellStyle(numberStyle);
					} else {
						R17cell1.setCellValue("");
						R17cell1.setCellStyle(textStyle);
					}

// R17 Col C
					Cell R17cell2 = row.createCell(2);
					if (record.getR17_ex_rate_mid() != null) {
						R17cell2.setCellValue(record.getR17_ex_rate_mid().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);
					}

// R17 Col D
					Cell R17cell3 = row.createCell(3);
					if (record.getR17_ex_rate_sell() != null) {
						R17cell3.setCellValue(record.getR17_ex_rate_sell().doubleValue());
						R17cell3.setCellStyle(numberStyle);
					} else {
						R17cell3.setCellValue("");
						R17cell3.setCellStyle(textStyle);
					}

// R18 Col B
					row = sheet.getRow(17);
					Cell R18cell1 = row.createCell(1);
					if (record.getR18_ex_rate_buy() != null) {
						R18cell1.setCellValue(record.getR18_ex_rate_buy().doubleValue());
						R18cell1.setCellStyle(numberStyle);
					} else {
						R18cell1.setCellValue("");
						R18cell1.setCellStyle(textStyle);
					}

// R18 Col C
					Cell R18cell2 = row.createCell(2);
					if (record.getR18_ex_rate_mid() != null) {
						R18cell2.setCellValue(record.getR18_ex_rate_mid().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);
					}

// R18 Col D
					Cell R18cell3 = row.createCell(3);
					if (record.getR18_ex_rate_sell() != null) {
						R18cell3.setCellValue(record.getR18_ex_rate_sell().doubleValue());
						R18cell3.setCellStyle(numberStyle);
					} else {
						R18cell3.setCellValue("");
						R18cell3.setCellStyle(textStyle);
					}

// R18 Col E
					Cell R18cell4 = row.createCell(4);
					if (record.getR18_current() != null) {
						R18cell4.setCellValue(record.getR18_current().doubleValue());
						R18cell4.setCellStyle(numberStyle);
					} else {
						R18cell4.setCellValue("");
						R18cell1.setCellStyle(textStyle);
					}

// R18 Col F
					Cell R18cell5 = row.createCell(5);
					if (record.getR18_call() != null) {
						R18cell5.setCellValue(record.getR18_call().doubleValue());
						R18cell5.setCellStyle(numberStyle);
					} else {
						R18cell5.setCellValue("");
						R18cell5.setCellStyle(textStyle);
					}

// R18 Col G
					Cell R18cell6 = row.createCell(6);
					if (record.getR18_savings() != null) {
						R18cell6.setCellValue(record.getR18_savings().doubleValue());
						R18cell6.setCellStyle(numberStyle);
					} else {
						R18cell6.setCellValue("");
						R18cell6.setCellStyle(textStyle);

					}

// R18 Col H
					Cell R18cell7 = row.createCell(7);
					if (record.getR18_notice_0to31() != null) {
						R18cell7.setCellValue(record.getR18_notice_0to31().doubleValue());
						R18cell7.setCellStyle(numberStyle);
					} else {
						R18cell7.setCellValue("");
						R18cell7.setCellStyle(textStyle);

					}

// R18 Col I
					Cell R18cell8 = row.createCell(8);
					if (record.getR18_notice_32to88() != null) {
						R18cell8.setCellValue(record.getR18_notice_32to88().doubleValue());
						R18cell8.setCellStyle(numberStyle);
					} else {
						R18cell8.setCellValue("");
						R18cell8.setCellStyle(textStyle);
					}

// R18 Col J
					Cell R18cell9 = row.createCell(9);
					if (record.getR18_fix_depo_91_day_depo() != null) {
						R18cell9.setCellValue(record.getR18_fix_depo_91_day_depo().doubleValue());
						R18cell9.setCellStyle(numberStyle);
					} else {
						R18cell9.setCellValue("");
						R18cell9.setCellStyle(textStyle);
					}

// R18 Col K
					Cell R18cell10 = row.createCell(10);
					if (record.getR18_fix_depo_1to2() != null) {
						R18cell10.setCellValue(record.getR18_fix_depo_1to2().doubleValue());
						R18cell10.setCellStyle(numberStyle);
					} else {
						R18cell10.setCellValue("");
						R18cell10.setCellStyle(textStyle);
					}

// R18 Col L
					Cell R18cell11 = row.createCell(11);
					if (record.getR18_fix_depo_4to6() != null) {
						R18cell11.setCellValue(record.getR18_fix_depo_4to6().doubleValue());
						R18cell11.setCellStyle(numberStyle);
					} else {
						R18cell11.setCellValue("");
						R18cell11.setCellStyle(textStyle);
					}

// R18 Col M
					Cell R18cell12 = row.createCell(12);
					if (record.getR18_fix_depo_7to12() != null) {
						R18cell12.setCellValue(record.getR18_fix_depo_7to12().doubleValue());
						R18cell12.setCellStyle(numberStyle);
					} else {
						R18cell12.setCellValue("");
						R18cell12.setCellStyle(textStyle);
					}

// R18 Col N
					Cell R18cell13 = row.createCell(13);
					if (record.getR18_fix_depo_13to18() != null) {
						R18cell13.setCellValue(record.getR18_fix_depo_13to18().doubleValue());
						R18cell13.setCellStyle(numberStyle);
					} else {
						R18cell13.setCellValue("");
						R18cell13.setCellStyle(textStyle);
					}

// R18 Col O
					Cell R18cell14 = row.createCell(14);
					if (record.getR18_fix_depo_19to24() != null) {
						R18cell14.setCellValue(record.getR18_fix_depo_19to24().doubleValue());
						R18cell14.setCellStyle(numberStyle);
					} else {
						R18cell14.setCellValue("");
						R18cell14.setCellStyle(textStyle);
					}

// R18 Col P
					Cell R18cell15 = row.createCell(15);
					if (record.getR18_fix_depo_over24() != null) {
						R18cell15.setCellValue(record.getR18_fix_depo_over24().doubleValue());
						R18cell15.setCellStyle(numberStyle);
					} else {
						R18cell15.setCellValue("");
						R18cell15.setCellStyle(textStyle);
					}

// R18 Col Q
					Cell R18cell16 = row.createCell(16);
					if (record.getR18_cer_of_depo() != null) {
						R18cell16.setCellValue(record.getR18_cer_of_depo().doubleValue());
						R18cell16.setCellStyle(numberStyle);
					} else {
						R18cell16.setCellValue("");
						R18cell16.setCellStyle(textStyle);
					}
// R18 Col R
					Cell R18cell17 = row.createCell(17);
					if (record.getR18_total() != null) {
						R18cell17.setCellValue(record.getR18_total().doubleValue());
						R18cell17.setCellStyle(numberStyle);
					} else {
						R18cell17.setCellValue("");
						R18cell17.setCellStyle(textStyle);
					}
// R18 Col S
					Cell R18cell18 = row.createCell(18);
					if (record.getR18_pula_equivalent() != null) {
						R18cell18.setCellValue(record.getR18_pula_equivalent().doubleValue());
						R18cell18.setCellStyle(numberStyle);
					} else {
						R18cell18.setCellValue("");
						R18cell18.setCellStyle(textStyle);
					}
// R18 Col S
					Cell R18cell19 = row.createCell(19);
					if (record.getR18_avg_pula_equivalent() != null) {
						R18cell19.setCellValue(record.getR18_avg_pula_equivalent().doubleValue());
						R18cell18.setCellStyle(numberStyle);
					} else {
						R18cell19.setCellValue("");
						R18cell19.setCellStyle(textStyle);
					}

//Entity 2
// R28 Col B
					row = sheet.getRow(27);
					Cell R28cell1 = row.createCell(1);
					if (record.getR28_import() != null) {
						R28cell1.setCellValue(record.getR28_import().doubleValue());
						R28cell1.setCellStyle(numberStyle);
					} else {
						R28cell1.setCellValue("");
						R28cell1.setCellStyle(textStyle);
					}
// R28 Col C
					Cell R28cell2 = row.createCell(2);
					if (record.getR28_investment() != null) {
						R28cell2.setCellValue(record.getR28_investment().doubleValue());
						R28cell2.setCellStyle(numberStyle);
					} else {
						R28cell2.setCellValue("");
						R28cell2.setCellStyle(textStyle);
					}
// R28 Col D
					Cell R28cell3 = row.createCell(3);
					if (record.getR28_other() != null) {
						R28cell3.setCellValue(record.getR28_other().doubleValue());
						R28cell3.setCellStyle(numberStyle);
					} else {
						R28cell3.setCellValue("");
						R28cell3.setCellStyle(textStyle);
					}
// R29 Col B
					row = sheet.getRow(28);
					Cell R29cell1 = row.createCell(1);
					if (record.getR29_import() != null) {
						R29cell1.setCellValue(record.getR29_import().doubleValue());
						R29cell1.setCellStyle(numberStyle);
					} else {
						R29cell1.setCellValue("");
						R29cell1.setCellStyle(textStyle);
					}
// R29 Col C
					Cell R29cell2 = row.createCell(2);
					if (record.getR29_investment() != null) {
						R29cell2.setCellValue(record.getR29_investment().doubleValue());
						R29cell2.setCellStyle(numberStyle);
					} else {
						R29cell2.setCellValue("");
						R29cell2.setCellStyle(textStyle);
					}
// R29 Col D
					Cell R29cell3 = row.createCell(3);
					if (record.getR29_other() != null) {
						R29cell3.setCellValue(record.getR29_other().doubleValue());
						R29cell3.setCellStyle(numberStyle);
					} else {
						R29cell3.setCellValue("");
						R29cell3.setCellStyle(textStyle);
					}

// R30 Col B
					row = sheet.getRow(29);
					Cell R30cell1 = row.createCell(1);
					if (record.getR30_import() != null) {
						R30cell1.setCellValue(record.getR30_import().doubleValue());
						R30cell1.setCellStyle(numberStyle);
					} else {
						R30cell1.setCellValue("");
						R30cell1.setCellStyle(textStyle);
					}
// R30 Col C
					Cell R30cell2 = row.createCell(2);
					if (record.getR30_investment() != null) {
						R30cell2.setCellValue(record.getR30_investment().doubleValue());
						R30cell2.setCellStyle(numberStyle);
					} else {
						R30cell2.setCellValue("");
						R30cell2.setCellStyle(textStyle);
					}
// R30 Col D
					Cell R30cell3 = row.createCell(3);
					if (record.getR30_other() != null) {
						R30cell3.setCellValue(record.getR30_other().doubleValue());
						R30cell3.setCellStyle(numberStyle);
					} else {
						R30cell3.setCellValue("");
						R30cell3.setCellStyle(textStyle);
					}
// R31 Col B
					row = sheet.getRow(30);
					Cell R31cell1 = row.createCell(1);
					if (record.getR31_import() != null) {
						R31cell1.setCellValue(record.getR31_import().doubleValue());
						R31cell1.setCellStyle(numberStyle);
					} else {
						R31cell1.setCellValue("");
						R31cell1.setCellStyle(textStyle);
					}
// R31 Col C
					Cell R31cell2 = row.createCell(2);
					if (record.getR31_investment() != null) {
						R31cell2.setCellValue(record.getR31_investment().doubleValue());
						R31cell2.setCellStyle(numberStyle);
					} else {
						R31cell2.setCellValue("");
						R31cell2.setCellStyle(textStyle);
					}
// R31 Col D
					Cell R31cell3 = row.createCell(3);
					if (record.getR31_other() != null) {
						R31cell3.setCellValue(record.getR31_other().doubleValue());
						R31cell3.setCellStyle(numberStyle);
					} else {
						R31cell3.setCellValue("");
						R31cell3.setCellStyle(textStyle);
					}
// R32 Col B
					row = sheet.getRow(31);
					Cell R32cell1 = row.createCell(1);
					if (record.getR32_import() != null) {
						R32cell1.setCellValue(record.getR32_import().doubleValue());
						R32cell1.setCellStyle(numberStyle);
					} else {
						R32cell1.setCellValue("");
						R32cell1.setCellStyle(textStyle);
					}
// R32 Col C
					Cell R32cell2 = row.createCell(2);
					if (record.getR32_investment() != null) {
						R32cell2.setCellValue(record.getR32_investment().doubleValue());
						R32cell2.setCellStyle(numberStyle);
					} else {
						R32cell2.setCellValue("");
						R32cell2.setCellStyle(textStyle);
					}
// R32 Col D
					Cell R32cell3 = row.createCell(3);
					if (record.getR32_other() != null) {
						R32cell3.setCellValue(record.getR32_other().doubleValue());
						R32cell3.setCellStyle(numberStyle);
					} else {
						R32cell3.setCellValue("");
						R32cell3.setCellStyle(textStyle);
					}
// R33 Col B
					row = sheet.getRow(32);
					Cell R33cell1 = row.createCell(1);
					if (record.getR33_import() != null) {
						R33cell1.setCellValue(record.getR33_import().doubleValue());
						R33cell1.setCellStyle(numberStyle);
					} else {
						R33cell1.setCellValue("");
						R33cell1.setCellStyle(textStyle);
					}
// R33 Col C
					Cell R33cell2 = row.createCell(2);
					if (record.getR33_investment() != null) {
						R33cell2.setCellValue(record.getR33_investment().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}
// R33 Col D
					Cell R33cell3 = row.createCell(3);
					if (record.getR33_other() != null) {
						R33cell3.setCellValue(record.getR33_other().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

//Entity 3

					row = sheet.getRow(27);
					Cell R28cell1e3 = row.createCell(8);
					if (record.getR28_residents() != null) {
						R28cell1e3.setCellValue(record.getR28_residents().doubleValue());
						R28cell1e3.setCellStyle(numberStyle);
					} else {
						R28cell1e3.setCellValue("");
						R28cell1e3.setCellStyle(textStyle);
					}
// R28 Col C
					Cell R28cell2e3 = row.createCell(10);
					if (record.getR28_non_residents() != null) {
						R28cell2e3.setCellValue(record.getR28_non_residents().doubleValue());
						R28cell2e3.setCellStyle(numberStyle);
					} else {
						R28cell2e3.setCellValue("");
						R28cell2e3.setCellStyle(textStyle);
					}
// R29 Col B
					row = sheet.getRow(28);
					Cell R29cell1e3 = row.createCell(8);
					if (record.getR29_residents() != null) {
						R29cell1e3.setCellValue(record.getR29_residents().doubleValue());
						R29cell1e3.setCellStyle(numberStyle);
					} else {
						R29cell1e3.setCellValue("");
						R29cell1e3.setCellStyle(textStyle);
					}
// R29 Col C
					Cell R29cell2e3 = row.createCell(10);
					if (record.getR29_non_residents() != null) {
						R29cell2e3.setCellValue(record.getR29_non_residents().doubleValue());
						R29cell2e3.setCellStyle(numberStyle);
					} else {
						R29cell2e3.setCellValue("");
						R29cell2e3.setCellStyle(textStyle);
					}
// R30 Col B
					row = sheet.getRow(29);
					Cell R30cell1e3 = row.createCell(8);
					if (record.getR30_residents() != null) {
						R30cell1e3.setCellValue(record.getR30_residents().doubleValue());
						R30cell1e3.setCellStyle(numberStyle);
					} else {
						R30cell1e3.setCellValue("");
						R30cell1e3.setCellStyle(textStyle);
					}
// R30 Col C
					Cell R30cell2e3 = row.createCell(10);
					if (record.getR30_non_residents() != null) {
						R30cell2e3.setCellValue(record.getR30_non_residents().doubleValue());
						R30cell2e3.setCellStyle(numberStyle);
					} else {
						R30cell2e3.setCellValue("");
						R30cell2e3.setCellStyle(textStyle);
					}
// R31 Col B
					row = sheet.getRow(30);
					Cell R31cell1e3 = row.createCell(8);
					if (record.getR31_residents() != null) {
						R31cell1e3.setCellValue(record.getR31_residents().doubleValue());
						R31cell1e3.setCellStyle(numberStyle);
					} else {
						R31cell1e3.setCellValue("");
						R31cell1e3.setCellStyle(textStyle);
					}
// R31 Col C
					Cell R31cell2e3 = row.createCell(10);
					if (record.getR31_non_residents() != null) {
						R31cell2e3.setCellValue(record.getR31_non_residents().doubleValue());
						R31cell2e3.setCellStyle(numberStyle);
					} else {
						R31cell2e3.setCellValue("");
						R31cell2e3.setCellStyle(textStyle);
					}

// R32 Col B
					row = sheet.getRow(31);
					Cell R32cell1e3 = row.createCell(8);
					if (record.getR32_residents() != null) {
						R32cell1e3.setCellValue(record.getR32_residents().doubleValue());
						R32cell1e3.setCellStyle(numberStyle);
					} else {
						R32cell1e3.setCellValue("");
						R32cell1e3.setCellStyle(textStyle);
					}
// R32 Col C
					Cell R32cell2e3 = row.createCell(10);
					if (record.getR32_non_residents() != null) {
						R32cell2e3.setCellValue(record.getR32_non_residents().doubleValue());
						R32cell2e3.setCellStyle(numberStyle);
					} else {
						R32cell2e3.setCellValue("");
						R32cell2e3.setCellStyle(textStyle);
					}
// R33 Col B
					row = sheet.getRow(32);
					Cell R33cell1e3 = row.createCell(8);
					if (record.getR33_residents() != null) {
						R33cell1e3.setCellValue(record.getR33_residents().doubleValue());
						R33cell1e3.setCellStyle(numberStyle);
					} else {
						R33cell1e3.setCellValue("");
						R33cell1e3.setCellStyle(textStyle);
					}
// R33 Col C
					Cell R33cell2e3 = row.createCell(10);
					if (record.getR33_non_residents() != null) {
						R33cell2e3.setCellValue(record.getR33_non_residents().doubleValue());
						R33cell2e3.setCellStyle(numberStyle);
					} else {
						R33cell2e3.setCellValue("");
						R33cell2e3.setCellStyle(textStyle);
					}

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_DEP3 EMAIL ARCHIVAL SUMMARY", null,
						"M_DEP3_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

//	// Resub Format excel
//	public byte[] BRRS_M_DEP3ResubExcel(String filename, String reportId, String fromdate, String todate,
//			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {
//
//		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");
//
//		if ("email".equalsIgnoreCase(format) && version != null) {
//			logger.info("Service: Generating RESUB report for version {}", version);
//
//			try {
//				// ✅ Redirecting to Resub Excel
//				return BRRS_M_DEP3ResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
//						version);
//
//			} catch (ParseException e) {
//				logger.error("Invalid report date format: {}", fromdate, e);
//				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
//			}
//		}
//
//		List<M_DEP3_Resub_Summary_Entity> dataList = brrs_M_DEP3_resub_summary_repo
//				.getdatabydateListarchival(dateformat.parse(todate), version);
//		List<M_DEP3_Resub_Archival_Summary_Entity> dataList1 = brrs_M_DEP3_resub_Archival_summary_repo
//				.getdatabydateListarchival(dateformat.parse(todate), version);
//
//		if (dataList.isEmpty()) {
//			logger.warn("Service: No data found for M_DEP3 report. Returning empty result.");
//			return new byte[0];
//		}
//
//		String templateDir = env.getProperty("output.exportpathtemp");
//		String templateFileName = filename;
//		System.out.println(filename);
//		Path templatePath = Paths.get(templateDir, templateFileName);
//		System.out.println(templatePath);
//
//		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());
//
//		if (!Files.exists(templatePath)) {
//			// This specific exception will be caught by the controller.
//			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
//		}
//		if (!Files.isReadable(templatePath)) {
//			// A specific exception for permission errors.
//			throw new SecurityException(
//					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
//		}
//
//		// This try-with-resources block is perfect. It guarantees all resources are
//		// closed automatically.
//		try (InputStream templateInputStream = Files.newInputStream(templatePath);
//				Workbook workbook = WorkbookFactory.create(templateInputStream);
//				ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//
//			Sheet sheet = workbook.getSheetAt(0);
//
//			// --- Style Definitions ---
//			CreationHelper createHelper = workbook.getCreationHelper();
//
//			CellStyle dateStyle = workbook.createCellStyle();
//			dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
//			dateStyle.setBorderBottom(BorderStyle.THIN);
//			dateStyle.setBorderTop(BorderStyle.THIN);
//			dateStyle.setBorderLeft(BorderStyle.THIN);
//			dateStyle.setBorderRight(BorderStyle.THIN);
//
//			CellStyle textStyle = workbook.createCellStyle();
//			textStyle.setBorderBottom(BorderStyle.THIN);
//			textStyle.setBorderTop(BorderStyle.THIN);
//			textStyle.setBorderLeft(BorderStyle.THIN);
//			textStyle.setBorderRight(BorderStyle.THIN);
//
//			// Create the font
//			Font font = workbook.createFont();
//			font.setFontHeightInPoints((short) 8); // size 8
//			font.setFontName("Arial");
//
//			CellStyle numberStyle = workbook.createCellStyle();
//			// numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
//			numberStyle.setBorderBottom(BorderStyle.THIN);
//			numberStyle.setBorderTop(BorderStyle.THIN);
//			numberStyle.setBorderLeft(BorderStyle.THIN);
//			numberStyle.setBorderRight(BorderStyle.THIN);
//			numberStyle.setFont(font);
//			// --- End of Style Definitions ---
//
//			int startRow = 10;
//
//			if (!dataList.isEmpty()) {
//				for (int i = 0; i < dataList.size(); i++) {
//
//					M_DEP3_Resub_Summary_Entity record = dataList.get(i);
//					M_DEP3_Resub_Archival_Summary_Entity record = dataList1.get(i);
//					System.out.println("rownumber=" + startRow + i);
//					System.out.println("rownumber=" + startRow + i);
//					Row row = sheet.getRow(startRow + i);
//					if (row == null) {
//						row = sheet.createRow(startRow + i);
//					}
////NORMAL
//
//					// R11 Col B
//            
//Cell R11cell1 = row.createCell(1);
//if (record.getR11_ex_rate_buy() != null) {
//    R11cell1.setCellValue(record.getR11_ex_rate_buy().doubleValue());
//    R11cell1.setCellStyle(numberStyle);
//} else {
//    R11cell1.setCellValue("");
//    R11cell1.setCellStyle(textStyle);
//}
//
//// R11 Col C
//Cell R11cell2 = row.createCell(2);
//if (record.getR11_ex_rate_mid() != null) {
//    R11cell2.setCellValue(record.getR11_ex_rate_mid().doubleValue());
//    R11cell2.setCellStyle(numberStyle);
//} else {
//    R11cell2.setCellValue("");
//    R11cell2.setCellStyle(textStyle);
//}
//
//// R11 Col D
//Cell R11cell3 = row.createCell(3);
//if (record.getR11_ex_rate_sell() != null) {
//    R11cell3.setCellValue(record.getR11_ex_rate_sell().doubleValue());
//    R11cell3.setCellStyle(numberStyle);
//} else {
//    R11cell3.setCellValue("");
//    R11cell3.setCellStyle(textStyle);
//}
//
//// R11 Col E
//Cell R11cell4 = row.createCell(4);
//if (record.getR11_current()!= null) {
//    R11cell4.setCellValue(record.getR11_current().doubleValue());
//    R11cell4.setCellStyle(numberStyle);
//} else {
//    R11cell4.setCellValue("");
//    R11cell1.setCellStyle(textStyle);
//}
//
//// R11 Col F
//Cell R11cell5 = row.createCell(5);
//if (record.getR11_call() != null) {
//    R11cell5.setCellValue(record.getR11_call().doubleValue());
//    R11cell5.setCellStyle(numberStyle);
//} else {
//    R11cell5.setCellValue("");
//    R11cell5.setCellStyle(textStyle);
//}
//
//// R11 Col G
//Cell R11cell6 = row.createCell(6);
//if (record.getR11_savings() != null) {
//    R11cell6.setCellValue(record.getR11_savings().doubleValue());
//    R11cell6.setCellStyle(numberStyle);
//} else {
//    R11cell6.setCellValue("");
//    R11cell6.setCellStyle(textStyle);
//	
//}
//
//// R11 Col H
//Cell R11cell7 = row.createCell(7);
//if (record.getR11_notice_0to31()!= null) {
//    R11cell7.setCellValue(record.getR11_notice_0to31().doubleValue());
//    R11cell7.setCellStyle(numberStyle);
//} else {
//    R11cell7.setCellValue("");
//    R11cell7.setCellStyle(textStyle);
//	
//}
//
//// R11 Col I
//Cell R11cell8= row.createCell(8);
//if (record.getR11_notice_32to88() != null) {
//    R11cell8.setCellValue(record.getR11_notice_32to88().doubleValue());
//    R11cell8.setCellStyle(numberStyle);
//} else {
//    R11cell8.setCellValue("");
//    R11cell8.setCellStyle(textStyle);
//}
//
//// R11 Col J
//Cell R11cell9 = row.createCell(9);
//if (record.getR11_fix_depo_91_day_depo() != null) {
//    R11cell9.setCellValue(record.getR11_fix_depo_91_day_depo().doubleValue());
//    R11cell9.setCellStyle(numberStyle);
//} else {
//    R11cell9.setCellValue("");
//    R11cell9.setCellStyle(textStyle);
//}
//
//// R11 Col K
//Cell R11cell10 = row.createCell(10);
//if (record.getR11_fix_depo_1to2() != null) {
//    R11cell10.setCellValue(record.getR11_fix_depo_1to2().doubleValue());
//    R11cell10.setCellStyle(numberStyle);
//} else {
//    R11cell10.setCellValue("");
//    R11cell10.setCellStyle(textStyle);
//}
//
//// R11 Col L
//Cell R11cell11 = row.createCell(11);
//if (record.getR11_fix_depo_4to6() != null) {
//    R11cell11.setCellValue(record.getR11_fix_depo_4to6().doubleValue());
//    R11cell11.setCellStyle(numberStyle);
//} else {
//    R11cell11.setCellValue("");
//    R11cell11.setCellStyle(textStyle);
//}
//
//// R11 Col M
//Cell R11cell12 = row.createCell(12);
//if (record.getR11_fix_depo_7to12() != null) {
//    R11cell12.setCellValue(record.getR11_fix_depo_7to12().doubleValue());
//    R11cell12.setCellStyle(numberStyle);
//} else {
//    R11cell12.setCellValue("");
//    R11cell12.setCellStyle(textStyle);
//}
//
//// R11 Col N
//Cell R11cell13 = row.createCell(13);
//if (record.getR11_fix_depo_13to18()!= null) {
//    R11cell13.setCellValue(record.getR11_fix_depo_13to18().doubleValue());
//    R11cell13.setCellStyle(numberStyle);
//} else {
//    R11cell13.setCellValue("");
//    R11cell13.setCellStyle(textStyle);
//}
//
//// R11 Col O
//Cell R11cell14 = row.createCell(14);
//if (record.getR11_fix_depo_19to24()!= null) {
//    R11cell14.setCellValue(record.getR11_fix_depo_19to24().doubleValue());
//    R11cell14.setCellStyle(numberStyle);
//} else {
//    R11cell14.setCellValue("");
//    R11cell14.setCellStyle(textStyle);
//}
//
//// R11 Col P
//Cell R11cell15 = row.createCell(15);
//if (record.getR11_fix_depo_over24()!= null) {
//    R11cell15.setCellValue(record.getR11_fix_depo_over24().doubleValue());
//    R11cell15.setCellStyle(numberStyle);
//} else {
//    R11cell15.setCellValue("");
//    R11cell15.setCellStyle(textStyle);
//}
//
//
//// R11 Col Q
//Cell R11cell16 = row.createCell(16);
//if (record.getR11_cer_of_depo()!= null) {
//    R11cell16.setCellValue(record.getR11_cer_of_depo().doubleValue());
//    R11cell16.setCellStyle(numberStyle);
//} else {
//    R11cell16.setCellValue("");
//    R11cell16.setCellStyle(textStyle);
// 
//}
//
//// R11 Col S
//Cell R11cell18 = row.createCell(18);
//if (record.getR11_pula_equivalent () != null) {
//    R11cell18.setCellValue(record.getR11_pula_equivalent().doubleValue());
//    R11cell18.setCellStyle(numberStyle);
//} else {
//    R11cell18.setCellValue("");
//    R11cell18.setCellStyle(textStyle);
//}
// 
//
//// R11 Col T
//Cell R11cell19 = row.createCell(19);
//if (record.getR11_avg_pula_equivalent ()!= null) {
//    R11cell19.setCellValue(record.getR11_avg_pula_equivalent().doubleValue());
//    R11cell19.setCellStyle(numberStyle);
//} else {
//    R11cell19.setCellValue("");
//    R11cell19.setCellStyle(textStyle);
//}
//row = sheet.getRow(11);
//// R12 Col B
//Cell R12cell1 = row.createCell(1);
//if (record.getR12_ex_rate_buy ()!= null) {
//    R12cell1.setCellValue(record.getR12_ex_rate_buy().doubleValue());
//    R12cell1.setCellStyle(numberStyle);
//} else {
//    R12cell1.setCellValue("");
//    R12cell1.setCellStyle(textStyle);
//}
//
//// R12 Col C
//Cell R12cell2 = row.createCell(2);
//if (record.getR12_ex_rate_mid() != null) {
//    R12cell2.setCellValue(record.getR12_ex_rate_mid().doubleValue());
//    R12cell2.setCellStyle(numberStyle);
//} else {
//    R12cell2.setCellValue("");
//    R12cell2.setCellStyle(textStyle);
//}
//
//// R12 Col D
//Cell R12cell3 = row.createCell(3);
//if (record.getR12_ex_rate_sell() != null) {
//    R12cell3.setCellValue(record.getR12_ex_rate_sell().doubleValue());
//    R12cell3.setCellStyle(numberStyle);
//} else {
//    R12cell3.setCellValue("");
//    R12cell3.setCellStyle(textStyle);
//}
//
//// R12 Col E
//Cell R12cell4 = row.createCell(4);
//if (record.getR12_current() != null) {
//    R12cell4.setCellValue(record.getR12_current().doubleValue());
//    R12cell4.setCellStyle(numberStyle);
//} else {
//    R12cell4.setCellValue("");
//    R12cell1.setCellStyle(textStyle);
//}
//
//// R12 Col F
//Cell R12cell5 = row.createCell(5);
//if (record.getR12_call() != null) {
//    R12cell5.setCellValue(record.getR12_call().doubleValue());
//    R12cell5.setCellStyle(numberStyle);
//} else {
//    R12cell5.setCellValue("");
//    R12cell5.setCellStyle(textStyle);
//}
//
//// R12 Col G
//Cell R12cell6 = row.createCell(6);
//if (record.getR12_savings() != null) {
//    R12cell6.setCellValue(record.getR12_savings().doubleValue());
//    R12cell6.setCellStyle(numberStyle);
//} else {
//    R12cell6.setCellValue("");
//    R12cell6.setCellStyle(textStyle);
//	
//}
//
//// R12 Col H
//Cell R12cell7 = row.createCell(7);
//if (record.getR12_notice_0to31()!= null) {
//    R12cell7.setCellValue(record.getR12_notice_0to31().doubleValue());
//    R12cell7.setCellStyle(numberStyle);
//} else {
//    R12cell7.setCellValue("");
//    R12cell7.setCellStyle(textStyle);
//	
//}
//
//// R12 Col I
//Cell R12cell8= row.createCell(8);
//if (record.getR12_notice_32to88() != null) {
//    R12cell8.setCellValue(record.getR12_notice_32to88().doubleValue());
//    R12cell8.setCellStyle(numberStyle);
//} else {
//    R12cell8.setCellValue("");
//    R12cell8.setCellStyle(textStyle);
//}
//
//// R12 Col J
//Cell R12cell9 = row.createCell(9);
//if (record.getR12_fix_depo_91_day_depo() != null) {
//    R12cell9.setCellValue(record.getR12_fix_depo_91_day_depo().doubleValue());
//    R12cell9.setCellStyle(numberStyle);
//} else {
//    R12cell9.setCellValue("");
//    R12cell9.setCellStyle(textStyle);
//}
//
//// R12 Col K
//Cell R12cell10 = row.createCell(10);
//if (record.getR12_fix_depo_1to2() != null) {
//    R12cell10.setCellValue(record.getR12_fix_depo_1to2().doubleValue());
//    R12cell10.setCellStyle(numberStyle);
//} else {
//    R12cell10.setCellValue("");
//    R12cell10.setCellStyle(textStyle);
//}
//
//// R12 Col L
//Cell R12cell11 = row.createCell(11);
//if (record.getR12_fix_depo_4to6() != null) {
//    R12cell11.setCellValue(record.getR12_fix_depo_4to6().doubleValue());
//    R12cell11.setCellStyle(numberStyle);
//} else {
//    R12cell11.setCellValue("");
//    R12cell11.setCellStyle(textStyle);
//}
//
//// R12 Col M
//Cell R12cell12 = row.createCell(12);
//if (record.getR12_fix_depo_7to12() != null) {
//    R12cell12.setCellValue(record.getR12_fix_depo_7to12().doubleValue());
//    R12cell12.setCellStyle(numberStyle);
//} else {
//    R12cell12.setCellValue("");
//    R12cell12.setCellStyle(textStyle);
//}
//
//// R12 Col N
//Cell R12cell13 = row.createCell(13);
//if (record.getR12_fix_depo_13to18()!= null) {
//    R12cell13.setCellValue(record.getR12_fix_depo_13to18().doubleValue());
//    R12cell13.setCellStyle(numberStyle);
//} else {
//    R12cell13.setCellValue("");
//    R12cell13.setCellStyle(textStyle);
//}
//
//// R12 Col O
//Cell R12cell14 = row.createCell(14);
//if (record.getR12_fix_depo_19to24()!= null) {
//    R12cell14.setCellValue(record.getR12_fix_depo_19to24().doubleValue());
//    R12cell14.setCellStyle(numberStyle);
//} else {
//    R12cell14.setCellValue("");
//    R12cell14.setCellStyle(textStyle);
//}
//
//// R12 Col P
//Cell R12cell15 = row.createCell(15);
//if (record.getR12_fix_depo_over24() != null) {
//    R12cell15.setCellValue(record.getR12_fix_depo_over24().doubleValue());
//    R12cell15.setCellStyle(numberStyle);
//} else {
//    R12cell15.setCellValue("");
//    R12cell15.setCellStyle(textStyle);
//}
//
//
//// R12 Col Q
//Cell R12cell16 = row.createCell(16);
//if (record.getR12_cer_of_depo() != null) {
//    R12cell16.setCellValue(record.getR12_cer_of_depo().doubleValue());
//    R12cell16.setCellStyle(numberStyle);
//} else {
//    R12cell16.setCellValue("");
//    R12cell16.setCellStyle(textStyle);
//}
//
//// R12 Col S
//Cell R12cell18 = row.createCell(18);
//if (record.getR12_pula_equivalent() != null) {
//    R12cell18.setCellValue(record.getR12_pula_equivalent().doubleValue());
//    R12cell18.setCellStyle(numberStyle);
//} else {
//    R12cell18.setCellValue("");
//    R12cell18.setCellStyle(textStyle);
//}
// 
//
//// R12 Col T
//Cell R12cell19 = row.createCell(19);
//if (record.getR12_avg_pula_equivalent() != null) {
//    R12cell19.setCellValue(record.getR12_avg_pula_equivalent().doubleValue());
//    R12cell19.setCellStyle(numberStyle);
//} else {
//    R12cell19.setCellValue("");
//    R12cell19.setCellStyle(textStyle);
//}
//
//// R13 Col B
//row = sheet.getRow(12);
//Cell R13cell1 = row.createCell(1);
//if (record.getR13_ex_rate_buy() != null) {
//    R13cell1.setCellValue(record.getR13_ex_rate_buy().doubleValue());
//    R13cell1.setCellStyle(numberStyle);
//} else {
//    R13cell1.setCellValue("");
//    R13cell1.setCellStyle(textStyle);
//}
//
//// R13 Col C
//Cell R13cell2 = row.createCell(2);
//if (record.getR13_ex_rate_mid() != null) {
//    R13cell2.setCellValue(record.getR13_ex_rate_mid().doubleValue());
//    R13cell2.setCellStyle(numberStyle);
//} else {
//    R13cell2.setCellValue("");
//    R13cell2.setCellStyle(textStyle);
//}
//
//// R13 Col D
//Cell R13cell3 = row.createCell(3);
//if (record.getR13_ex_rate_sell() != null) {
//    R13cell3.setCellValue(record.getR13_ex_rate_sell().doubleValue());
//    R13cell3.setCellStyle(numberStyle);
//} else {
//    R13cell3.setCellValue("");
//    R13cell3.setCellStyle(textStyle);
//}
//
//// R13 Col E
//Cell R13cell4 = row.createCell(4);
//if (record.getR13_current() != null) {
//    R13cell4.setCellValue(record.getR13_current().doubleValue());
//    R13cell4.setCellStyle(numberStyle);
//} else {
//    R13cell4.setCellValue("");
//    R13cell1.setCellStyle(textStyle);
//}
//
//// R13 Col F
//Cell R13cell5 = row.createCell(5);
//if (record.getR13_call() != null) {
//    R13cell5.setCellValue(record.getR13_call().doubleValue());
//    R13cell5.setCellStyle(numberStyle);
//} else {
//    R13cell5.setCellValue("");
//    R13cell5.setCellStyle(textStyle);
//}
//
//// R13 Col G
//Cell R13cell6 = row.createCell(6);
//if (record.getR13_savings() != null) {
//    R13cell6.setCellValue(record.getR13_savings().doubleValue());
//    R13cell6.setCellStyle(numberStyle);
//} else {
//    R13cell6.setCellValue("");
//    R13cell6.setCellStyle(textStyle);
//	
//}
//
//// R13 Col H
//Cell R13cell7 = row.createCell(7);
//if (record.getR13_notice_0to31()!= null) {
//    R13cell7.setCellValue(record.getR13_notice_0to31().doubleValue());
//    R13cell7.setCellStyle(numberStyle);
//} else {
//    R13cell7.setCellValue("");
//    R13cell7.setCellStyle(textStyle);
//	
//}
//
//// R13 Col I
//Cell R13cell8= row.createCell(8);
//if (record.getR13_notice_32to88() != null) {
//    R13cell8.setCellValue(record.getR13_notice_32to88().doubleValue());
//    R13cell8.setCellStyle(numberStyle);
//} else {
//    R13cell8.setCellValue("");
//    R13cell8.setCellStyle(textStyle);
//}
//
//// R13 Col J
//Cell R13cell9 = row.createCell(9);
//if (record.getR13_fix_depo_91_day_depo() != null) {
//    R13cell9.setCellValue(record.getR13_fix_depo_91_day_depo().doubleValue());
//    R13cell9.setCellStyle(numberStyle);
//} else {
//    R13cell9.setCellValue("");
//    R13cell9.setCellStyle(textStyle);
//}
//
//// R13 Col K
//Cell R13cell10 = row.createCell(10);
//if (record.getR13_fix_depo_1to2() != null) {
//    R13cell10.setCellValue(record.getR13_fix_depo_1to2().doubleValue());
//    R13cell10.setCellStyle(numberStyle);
//} else {
//    R13cell10.setCellValue("");
//    R13cell10.setCellStyle(textStyle);
//}
//
//// R13 Col L
//Cell R13cell11 = row.createCell(11);
//if (record.getR13_fix_depo_4to6() != null) {
//    R13cell11.setCellValue(record.getR13_fix_depo_4to6().doubleValue());
//    R13cell11.setCellStyle(numberStyle);
//} else {
//    R13cell11.setCellValue("");
//    R13cell11.setCellStyle(textStyle);
//}
//
//// R13 Col M
//Cell R13cell12 = row.createCell(12);
//if (record.getR13_fix_depo_7to12() != null) {
//    R13cell12.setCellValue(record.getR13_fix_depo_7to12().doubleValue());
//    R13cell12.setCellStyle(numberStyle);
//} else {
//    R13cell12.setCellValue("");
//    R13cell12.setCellStyle(textStyle);
//}
//
//// R13 Col N
//Cell R13cell13 = row.createCell(13);
//if (record.getR13_fix_depo_13to18()!= null) {
//    R13cell13.setCellValue(record.getR13_fix_depo_13to18().doubleValue());
//    R13cell13.setCellStyle(numberStyle);
//} else {
//    R13cell13.setCellValue("");
//    R13cell13.setCellStyle(textStyle);
//}
//
//// R13 Col O
//Cell R13cell14 = row.createCell(14);
//if (record.getR13_fix_depo_19to24()!= null) {
//    R13cell14.setCellValue(record.getR13_fix_depo_19to24().doubleValue());
//    R13cell14.setCellStyle(numberStyle);
//} else {
//    R13cell14.setCellValue("");
//    R13cell14.setCellStyle(textStyle);
//}
//
//// R13 Col P
//Cell R13cell15 = row.createCell(15);
//if (record.getR13_fix_depo_over24() != null) {
//    R13cell15.setCellValue(record.getR13_fix_depo_over24().doubleValue());
//    R13cell15.setCellStyle(numberStyle);
//} else {
//    R13cell15.setCellValue("");
//    R13cell15.setCellStyle(textStyle);
//}
//
//
//// R13 Col Q
//Cell R13cell16 = row.createCell(16);
//if (record.getR13_cer_of_depo() != null) {
//    R13cell16.setCellValue(record.getR13_cer_of_depo().doubleValue());
//    R13cell16.setCellStyle(numberStyle);
//} else {
//    R13cell16.setCellValue("");
//    R13cell16.setCellStyle(textStyle);
//}
//
//// R13 Col S
//Cell R13cell18 = row.createCell(18);
//if (record.getR13_pula_equivalent() != null) {
//    R13cell18.setCellValue(record.getR13_pula_equivalent().doubleValue());
//    R13cell18.setCellStyle(numberStyle);
//} else {
//    R13cell18.setCellValue("");
//    R13cell18.setCellStyle(textStyle);
//}
// 
//
//// R13 Col T
//Cell R13cell19 = row.createCell(19);
//if (record.getR13_avg_pula_equivalent() != null) {
//    R13cell19.setCellValue(record.getR13_avg_pula_equivalent().doubleValue());
//    R13cell19.setCellStyle(numberStyle);
//} else {
//    R13cell19.setCellValue("");
//    R13cell19.setCellStyle(textStyle);
//}
//
//// R14 Col B
//row = sheet.getRow(13);
//Cell R14cell1 = row.createCell(1);
//if (record.getR14_ex_rate_buy() != null) {
//    R14cell1.setCellValue(record.getR14_ex_rate_buy().doubleValue());
//    R14cell1.setCellStyle(numberStyle);
//} else {
//    R14cell1.setCellValue("");
//    R14cell1.setCellStyle(textStyle);
//}
//
//// R14 Col C
//Cell R14cell2 = row.createCell(2);
//if (record.getR14_ex_rate_mid() != null) {
//    R14cell2.setCellValue(record.getR14_ex_rate_mid().doubleValue());
//    R14cell2.setCellStyle(numberStyle);
//} else {
//    R14cell2.setCellValue("");
//    R14cell2.setCellStyle(textStyle);
//}
//
//// R14 Col D
//Cell R14cell3 = row.createCell(3);
//if (record.getR14_ex_rate_sell() != null) {
//    R14cell3.setCellValue(record.getR14_ex_rate_sell().doubleValue());
//    R14cell3.setCellStyle(numberStyle);
//} else {
//    R14cell3.setCellValue("");
//    R14cell3.setCellStyle(textStyle);
//}
//
//// R14 Col E
//Cell R14cell4 = row.createCell(4);
//if (record.getR14_current() != null) {
//    R14cell4.setCellValue(record.getR14_current().doubleValue());
//    R14cell4.setCellStyle(numberStyle);
//} else {
//    R14cell4.setCellValue("");
//    R14cell1.setCellStyle(textStyle);
//}
//
//// R14 Col F
//Cell R14cell5 = row.createCell(5);
//if (record.getR14_call() != null) {
//    R14cell5.setCellValue(record.getR14_call().doubleValue());
//    R14cell5.setCellStyle(numberStyle);
//} else {
//    R14cell5.setCellValue("");
//    R14cell5.setCellStyle(textStyle);
//}
//
//// R14 Col G
//Cell R14cell6 = row.createCell(6);
//if (record.getR14_savings() != null) {
//    R14cell6.setCellValue(record.getR14_savings().doubleValue());
//    R14cell6.setCellStyle(numberStyle);
//} else {
//    R14cell6.setCellValue("");
//    R14cell6.setCellStyle(textStyle);
//	
//}
//
//// R14 Col H
//Cell R14cell7 = row.createCell(7);
//if (record.getR14_notice_0to31()!= null) {
//    R14cell7.setCellValue(record.getR14_notice_0to31().doubleValue());
//    R14cell7.setCellStyle(numberStyle);
//} else {
//    R14cell7.setCellValue("");
//    R14cell7.setCellStyle(textStyle);
//	
//}
//
//// R14 Col I
//Cell R14cell8= row.createCell(8);
//if (record.getR14_notice_32to88() != null) {
//    R14cell8.setCellValue(record.getR14_notice_32to88().doubleValue());
//    R14cell8.setCellStyle(numberStyle);
//} else {
//    R14cell8.setCellValue("");
//    R14cell8.setCellStyle(textStyle);
//}
//
//// R14 Col J
//Cell R14cell9 = row.createCell(9);
//if (record.getR14_fix_depo_91_day_depo() != null) {
//    R14cell9.setCellValue(record.getR14_fix_depo_91_day_depo().doubleValue());
//    R14cell9.setCellStyle(numberStyle);
//} else {
//    R14cell9.setCellValue("");
//    R14cell9.setCellStyle(textStyle);
//}
//
//// R14 Col K
//Cell R14cell10 = row.createCell(10);
//if (record.getR14_fix_depo_1to2() != null) {
//    R14cell10.setCellValue(record.getR14_fix_depo_1to2().doubleValue());
//    R14cell10.setCellStyle(numberStyle);
//} else {
//    R14cell10.setCellValue("");
//    R14cell10.setCellStyle(textStyle);
//}
//
//// R14 Col L
//Cell R14cell11 = row.createCell(11);
//if (record.getR14_fix_depo_4to6() != null) {
//    R14cell11.setCellValue(record.getR14_fix_depo_4to6().doubleValue());
//    R14cell11.setCellStyle(numberStyle);
//} else {
//    R14cell11.setCellValue("");
//    R14cell11.setCellStyle(textStyle);
//}
//
//// R14 Col M
//Cell R14cell12 = row.createCell(12);
//if (record.getR14_fix_depo_7to12() != null) {
//    R14cell12.setCellValue(record.getR14_fix_depo_7to12().doubleValue());
//    R14cell12.setCellStyle(numberStyle);
//} else {
//    R14cell12.setCellValue("");
//    R14cell12.setCellStyle(textStyle);
//}
//
//// R14 Col N
//Cell R14cell13 = row.createCell(13);
//if (record.getR14_fix_depo_13to18()!= null) {
//    R14cell13.setCellValue(record.getR14_fix_depo_13to18().doubleValue());
//    R14cell13.setCellStyle(numberStyle);
//} else {
//    R14cell13.setCellValue("");
//    R14cell13.setCellStyle(textStyle);
//}
//
//// R14 Col O
//Cell R14cell14 = row.createCell(14);
//if (record.getR14_fix_depo_19to24()!= null) {
//    R14cell14.setCellValue(record.getR14_fix_depo_19to24().doubleValue());
//    R14cell14.setCellStyle(numberStyle);
//} else {
//    R14cell14.setCellValue("");
//    R14cell14.setCellStyle(textStyle);
//}
//
//// R14 Col P
//Cell R14cell15 = row.createCell(15);
//if (record.getR14_fix_depo_over24() != null) {
//    R14cell15.setCellValue(record.getR14_fix_depo_over24().doubleValue());
//    R14cell15.setCellStyle(numberStyle);
//} else {
//    R14cell15.setCellValue("");
//    R14cell15.setCellStyle(textStyle);
//}
//
//
//// R14 Col Q
//Cell R14cell16 = row.createCell(16);
//if (record.getR14_cer_of_depo() != null) {
//    R14cell16.setCellValue(record.getR14_cer_of_depo().doubleValue());
//    R14cell16.setCellStyle(numberStyle);
//} else {
//    R14cell16.setCellValue("");
//    R14cell16.setCellStyle(textStyle);
//}
//
//// R14 Col S
//Cell R14cell18 = row.createCell(18);
//if (record.getR14_pula_equivalent() != null) {
//    R14cell18.setCellValue(record.getR14_pula_equivalent().doubleValue());
//    R14cell18.setCellStyle(numberStyle);
//} else {
//    R14cell18.setCellValue("");
//    R14cell18.setCellStyle(textStyle);
//}
// 
//
//// R14 Col T
//Cell R14cell19 = row.createCell(19);
//if (record.getR14_avg_pula_equivalent() != null) {
//    R14cell19.setCellValue(record.getR14_avg_pula_equivalent().doubleValue());
//    R14cell19.setCellStyle(numberStyle);
//} else {
//    R14cell19.setCellValue("");
//    R14cell19.setCellStyle(textStyle);
//}
//
//// R15 Col B
//row = sheet.getRow(14);
//Cell R15cell1 = row.createCell(1);
//if (record.getR15_ex_rate_buy() != null) {
//    R15cell1.setCellValue(record.getR15_ex_rate_buy().doubleValue());
//    R15cell1.setCellStyle(numberStyle);
//} else {
//    R15cell1.setCellValue("");
//    R15cell1.setCellStyle(textStyle);
//}
//
//// R15 Col C
//Cell R15cell2 = row.createCell(2);
//if (record.getR15_ex_rate_mid() != null) {
//    R15cell2.setCellValue(record.getR15_ex_rate_mid().doubleValue());
//    R15cell2.setCellStyle(numberStyle);
//} else {
//    R15cell2.setCellValue("");
//    R15cell2.setCellStyle(textStyle);
//}
//
//// R15 Col D
//Cell R15cell3 = row.createCell(3);
//if (record.getR15_ex_rate_sell() != null) {
//    R15cell3.setCellValue(record.getR15_ex_rate_sell().doubleValue());
//    R15cell3.setCellStyle(numberStyle);
//} else {
//    R15cell3.setCellValue("");
//    R15cell3.setCellStyle(textStyle);
//}
//
//// R15 Col E
//Cell R15cell4 = row.createCell(4);
//if (record.getR15_current() != null) {
//    R15cell4.setCellValue(record.getR15_current().doubleValue());
//    R15cell4.setCellStyle(numberStyle);
//} else {
//    R15cell4.setCellValue("");
//    R15cell1.setCellStyle(textStyle);
//}
//
//// R15 Col F
//Cell R15cell5 = row.createCell(5);
//if (record.getR15_call() != null) {
//    R15cell5.setCellValue(record.getR15_call().doubleValue());
//    R15cell5.setCellStyle(numberStyle);
//} else {
//    R15cell5.setCellValue("");
//    R15cell5.setCellStyle(textStyle);
//}
//
//// R15 Col G
//Cell R15cell6 = row.createCell(6);
//if (record.getR15_savings() != null) {
//    R15cell6.setCellValue(record.getR15_savings().doubleValue());
//    R15cell6.setCellStyle(numberStyle);
//} else {
//    R15cell6.setCellValue("");
//    R15cell6.setCellStyle(textStyle);
//	
//}
//
//// R15 Col H
//Cell R15cell7 = row.createCell(7);
//if (record.getR15_notice_0to31()!= null) {
//    R15cell7.setCellValue(record.getR15_notice_0to31().doubleValue());
//    R15cell7.setCellStyle(numberStyle);
//} else {
//    R15cell7.setCellValue("");
//    R15cell7.setCellStyle(textStyle);
//	
//}
//
//// R15 Col I
//Cell R15cell8= row.createCell(8);
//if (record.getR15_notice_32to88() != null) {
//    R15cell8.setCellValue(record.getR15_notice_32to88().doubleValue());
//    R15cell8.setCellStyle(numberStyle);
//} else {
//    R15cell8.setCellValue("");
//    R15cell8.setCellStyle(textStyle);
//}
//
//// R15 Col J
//Cell R15cell9 = row.createCell(9);
//if (record.getR15_fix_depo_91_day_depo() != null) {
//    R15cell9.setCellValue(record.getR15_fix_depo_91_day_depo().doubleValue());
//    R15cell9.setCellStyle(numberStyle);
//} else {
//    R15cell9.setCellValue("");
//    R15cell9.setCellStyle(textStyle);
//}
//
//// R15 Col K
//Cell R15cell10 = row.createCell(10);
//if (record.getR15_fix_depo_1to2() != null) {
//    R15cell10.setCellValue(record.getR15_fix_depo_1to2().doubleValue());
//    R15cell10.setCellStyle(numberStyle);
//} else {
//    R15cell10.setCellValue("");
//    R15cell10.setCellStyle(textStyle);
//}
//
//// R15 Col L
//Cell R15cell11 = row.createCell(11);
//if (record.getR15_fix_depo_4to6() != null) {
//    R15cell11.setCellValue(record.getR15_fix_depo_4to6().doubleValue());
//    R15cell11.setCellStyle(numberStyle);
//} else {
//    R15cell11.setCellValue("");
//    R15cell11.setCellStyle(textStyle);
//}
//
//// R15 Col M
//Cell R15cell12 = row.createCell(12);
//if (record.getR15_fix_depo_7to12() != null) {
//    R15cell12.setCellValue(record.getR15_fix_depo_7to12().doubleValue());
//    R15cell12.setCellStyle(numberStyle);
//} else {
//    R15cell12.setCellValue("");
//    R15cell12.setCellStyle(textStyle);
//}
//
//// R15 Col N
//Cell R15cell13 = row.createCell(13);
//if (record.getR15_fix_depo_13to18()!= null) {
//    R15cell13.setCellValue(record.getR15_fix_depo_13to18().doubleValue());
//    R15cell13.setCellStyle(numberStyle);
//} else {
//    R15cell13.setCellValue("");
//    R15cell13.setCellStyle(textStyle);
//}
//
//// R15 Col O
//Cell R15cell14 = row.createCell(14);
//if (record.getR15_fix_depo_19to24()!= null) {
//    R15cell14.setCellValue(record.getR15_fix_depo_19to24().doubleValue());
//    R15cell14.setCellStyle(numberStyle);
//} else {
//    R15cell14.setCellValue("");
//    R15cell14.setCellStyle(textStyle);
//}
//
//// R15 Col P
//Cell R15cell15 = row.createCell(15);
//if (record.getR15_fix_depo_over24() != null) {
//    R15cell15.setCellValue(record.getR15_fix_depo_over24().doubleValue());
//    R15cell15.setCellStyle(numberStyle);
//} else {
//    R15cell15.setCellValue("");
//    R15cell15.setCellStyle(textStyle);
//}
//
//
//// R15 Col Q
//Cell R15cell16 = row.createCell(16);
//if (record.getR15_cer_of_depo() != null) {
//    R15cell16.setCellValue(record.getR15_cer_of_depo().doubleValue());
//    R15cell16.setCellStyle(numberStyle);
//} else {
//    R15cell16.setCellValue("");
//    R15cell16.setCellStyle(textStyle);
//}
//
//// R15 Col S
//Cell R15cell18 = row.createCell(18);
//if (record.getR15_pula_equivalent() != null) {
//    R15cell18.setCellValue(record.getR15_pula_equivalent().doubleValue());
//    R15cell18.setCellStyle(numberStyle);
//} else {
//    R15cell18.setCellValue("");
//    R15cell18.setCellStyle(textStyle);
//}
// 
//
//// R15 Col T
//Cell R15cell19 = row.createCell(19);
//if (record.getR15_avg_pula_equivalent() != null) {
//    R15cell19.setCellValue(record.getR15_avg_pula_equivalent().doubleValue());
//    R15cell19.setCellStyle(numberStyle);
//} else {
//    R15cell19.setCellValue("");
//    R15cell19.setCellStyle(textStyle);
//}
//
//// R16 Col B
//row = sheet.getRow(15);
//Cell R16cell1 = row.createCell(1);
//if (record.getR16_ex_rate_buy() != null) {
//    R16cell1.setCellValue(record.getR16_ex_rate_buy().doubleValue());
//    R16cell1.setCellStyle(numberStyle);
//} else {
//    R16cell1.setCellValue("");
//    R16cell1.setCellStyle(textStyle);
//}
//
//// R16 Col C
//Cell R16cell2 = row.createCell(2);
//if (record.getR16_ex_rate_mid() != null) {
//    R16cell2.setCellValue(record.getR16_ex_rate_mid().doubleValue());
//    R16cell2.setCellStyle(numberStyle);
//} else {
//    R16cell2.setCellValue("");
//    R16cell2.setCellStyle(textStyle);
//}
//
//// R16 Col D
//Cell R16cell3 = row.createCell(3);
//if (record.getR16_ex_rate_sell() != null) {
//    R16cell3.setCellValue(record.getR16_ex_rate_sell().doubleValue());
//    R16cell3.setCellStyle(numberStyle);
//} else {
//    R16cell3.setCellValue("");
//    R16cell3.setCellStyle(textStyle);
//}
//
//// R16 Col E
//Cell R16cell4 = row.createCell(4);
//if (record.getR16_current() != null) {
//    R16cell4.setCellValue(record.getR16_current().doubleValue());
//    R16cell4.setCellStyle(numberStyle);
//} else {
//    R16cell4.setCellValue("");
//    R16cell1.setCellStyle(textStyle);
//}
//
//// R16 Col F
//Cell R16cell5 = row.createCell(5);
//if (record.getR16_call() != null) {
//    R16cell5.setCellValue(record.getR16_call().doubleValue());
//    R16cell5.setCellStyle(numberStyle);
//} else {
//    R16cell5.setCellValue("");
//    R16cell5.setCellStyle(textStyle);
//}
//
//// R16 Col G
//Cell R16cell6 = row.createCell(6);
//if (record.getR16_savings() != null) {
//    R16cell6.setCellValue(record.getR16_savings().doubleValue());
//    R16cell6.setCellStyle(numberStyle);
//} else {
//    R16cell6.setCellValue("");
//    R16cell6.setCellStyle(textStyle);
//	
//}
//
//// R16 Col H
//Cell R16cell7 = row.createCell(7);
//if (record.getR16_notice_0to31()!= null) {
//    R16cell7.setCellValue(record.getR16_notice_0to31().doubleValue());
//    R16cell7.setCellStyle(numberStyle);
//} else {
//    R16cell7.setCellValue("");
//    R16cell7.setCellStyle(textStyle);
//	
//}
//
//// R16 Col I
//Cell R16cell8= row.createCell(8);
//if (record.getR16_notice_32to88() != null) {
//    R16cell8.setCellValue(record.getR16_notice_32to88().doubleValue());
//    R16cell8.setCellStyle(numberStyle);
//} else {
//    R16cell8.setCellValue("");
//    R16cell8.setCellStyle(textStyle);
//}
//
//// R16 Col J
//Cell R16cell9 = row.createCell(9);
//if (record.getR16_fix_depo_91_day_depo() != null) {
//    R16cell9.setCellValue(record.getR16_fix_depo_91_day_depo().doubleValue());
//    R16cell9.setCellStyle(numberStyle);
//} else {
//    R16cell9.setCellValue("");
//    R16cell9.setCellStyle(textStyle);
//}
//
//// R16 Col K
//Cell R16cell10 = row.createCell(10);
//if (record.getR16_fix_depo_1to2() != null) {
//    R16cell10.setCellValue(record.getR16_fix_depo_1to2().doubleValue());
//    R16cell10.setCellStyle(numberStyle);
//} else {
//    R16cell10.setCellValue("");
//    R16cell10.setCellStyle(textStyle);
//}
//
//// R16 Col L
//Cell R16cell11 = row.createCell(11);
//if (record.getR16_fix_depo_4to6() != null) {
//    R16cell11.setCellValue(record.getR16_fix_depo_4to6().doubleValue());
//    R16cell11.setCellStyle(numberStyle);
//} else {
//    R16cell11.setCellValue("");
//    R16cell11.setCellStyle(textStyle);
//}
//
//// R16 Col M
//Cell R16cell12 = row.createCell(12);
//if (record.getR16_fix_depo_7to12() != null) {
//    R16cell12.setCellValue(record.getR16_fix_depo_7to12().doubleValue());
//    R16cell12.setCellStyle(numberStyle);
//} else {
//    R16cell12.setCellValue("");
//    R16cell12.setCellStyle(textStyle);
//}
//
//// R16 Col N
//Cell R16cell13 = row.createCell(13);
//if (record.getR16_fix_depo_13to18()!= null) {
//    R16cell13.setCellValue(record.getR16_fix_depo_13to18().doubleValue());
//    R16cell13.setCellStyle(numberStyle);
//} else {
//    R16cell13.setCellValue("");
//    R16cell13.setCellStyle(textStyle);
//}
//
//// R16 Col O
//Cell R16cell14 = row.createCell(14);
//if (record.getR16_fix_depo_19to24()!= null) {
//    R16cell14.setCellValue(record.getR16_fix_depo_19to24().doubleValue());
//    R16cell14.setCellStyle(numberStyle);
//} else {
//    R16cell14.setCellValue("");
//    R16cell14.setCellStyle(textStyle);
//}
//
//// R16 Col P
//Cell R16cell15 = row.createCell(15);
//if (record.getR16_fix_depo_over24() != null) {
//    R16cell15.setCellValue(record.getR16_fix_depo_over24().doubleValue());
//    R16cell15.setCellStyle(numberStyle);
//} else {
//    R16cell15.setCellValue("");
//    R16cell15.setCellStyle(textStyle);
//
//}
//
//// R16 Col Q
//Cell R16cell16 = row.createCell(16);
//if (record.getR16_cer_of_depo() != null) {
//    R16cell16.setCellValue(record.getR16_cer_of_depo().doubleValue());
//    R16cell16.setCellStyle(numberStyle);
//} else {
//    R16cell16.setCellValue("");
//    R16cell16.setCellStyle(textStyle);
//}
//
//// R16 Col S
//Cell R16cell18 = row.createCell(18);
//if (record.getR16_pula_equivalent() != null) {
//    R16cell18.setCellValue(record.getR16_pula_equivalent().doubleValue());
//    R16cell18.setCellStyle(numberStyle);
//} else {
//    R16cell18.setCellValue("");
//    R16cell18.setCellStyle(textStyle);
//}
// 
//
//// R16 Col T
//Cell R16cell19 = row.createCell(19);
//if (record.getR16_avg_pula_equivalent() != null) {
//    R16cell19.setCellValue(record.getR16_avg_pula_equivalent().doubleValue());
//    R16cell19.setCellStyle(numberStyle);
//} else {
//    R16cell19.setCellValue("");
//    R16cell19.setCellStyle(textStyle);
//}
//
//// R17 Col B
//row = sheet.getRow(16);
//Cell R17cell1 = row.createCell(1);
//if (record.getR17_ex_rate_buy() != null) {
//    R17cell1.setCellValue(record.getR17_ex_rate_buy().doubleValue());
//    R17cell1.setCellStyle(numberStyle);
//} else {
//    R17cell1.setCellValue("");
//    R17cell1.setCellStyle(textStyle);
//}
//
//// R17 Col C
//Cell R17cell2 = row.createCell(2);
//if (record.getR17_ex_rate_mid() != null) {
//    R17cell2.setCellValue(record.getR17_ex_rate_mid().doubleValue());
//    R17cell2.setCellStyle(numberStyle);
//} else {
//    R17cell2.setCellValue("");
//    R17cell2.setCellStyle(textStyle);
//}
//
//// R17 Col D
//Cell R17cell3 = row.createCell(3);
//if (record.getR17_ex_rate_sell() != null) {
//    R17cell3.setCellValue(record.getR17_ex_rate_sell().doubleValue());
//    R17cell3.setCellStyle(numberStyle);
//} else {
//    R17cell3.setCellValue("");
//    R17cell3.setCellStyle(textStyle);
//}
//
//// R18 Col B
//row = sheet.getRow(17);
//Cell R18cell1 = row.createCell(1);
//if (record.getR18_ex_rate_buy() != null) {
//    R18cell1.setCellValue(record.getR18_ex_rate_buy().doubleValue());
//    R18cell1.setCellStyle(numberStyle);
//} else {
//    R18cell1.setCellValue("");
//    R18cell1.setCellStyle(textStyle);
//}
//
//// R18 Col C
//Cell R18cell2 = row.createCell(2);
//if (record.getR18_ex_rate_mid() != null) {
//    R18cell2.setCellValue(record.getR18_ex_rate_mid().doubleValue());
//    R18cell2.setCellStyle(numberStyle);
//} else {
//    R18cell2.setCellValue("");
//    R18cell2.setCellStyle(textStyle);
//}
//
//// R18 Col D
//Cell R18cell3 = row.createCell(3);
//if (record.getR18_ex_rate_sell() != null) {
//    R18cell3.setCellValue(record.getR18_ex_rate_sell().doubleValue());
//    R18cell3.setCellStyle(numberStyle);
//} else {
//    R18cell3.setCellValue("");
//    R18cell3.setCellStyle(textStyle);
//}
//
//// R18 Col E
//Cell R18cell4 = row.createCell(4);
//if (record.getR18_current() != null) {
//    R18cell4.setCellValue(record.getR18_current().doubleValue());
//    R18cell4.setCellStyle(numberStyle);
//} else {
//    R18cell4.setCellValue("");
//    R18cell1.setCellStyle(textStyle);
//}
//
//// R18 Col F
//Cell R18cell5 = row.createCell(5);
//if (record.getR18_call() != null) {
//    R18cell5.setCellValue(record.getR18_call().doubleValue());
//    R18cell5.setCellStyle(numberStyle);
//} else {
//    R18cell5.setCellValue("");
//    R18cell5.setCellStyle(textStyle);
//}
//
//// R18 Col G
//Cell R18cell6 = row.createCell(6);
//if (record.getR18_savings() != null) {
//    R18cell6.setCellValue(record.getR18_savings().doubleValue());
//    R18cell6.setCellStyle(numberStyle);
//} else {
//    R18cell6.setCellValue("");
//    R18cell6.setCellStyle(textStyle);
//	
//}
//
//// R18 Col H
//Cell R18cell7 = row.createCell(7);
//if (record.getR18_notice_0to31()!= null) {
//    R18cell7.setCellValue(record.getR18_notice_0to31().doubleValue());
//    R18cell7.setCellStyle(numberStyle);
//} else {
//    R18cell7.setCellValue("");
//    R18cell7.setCellStyle(textStyle);
//	
//}
//
//// R18 Col I
//Cell R18cell8= row.createCell(8);
//if (record.getR18_notice_32to88() != null) {
//    R18cell8.setCellValue(record.getR18_notice_32to88().doubleValue());
//    R18cell8.setCellStyle(numberStyle);
//} else {
//    R18cell8.setCellValue("");
//    R18cell8.setCellStyle(textStyle);
//}
//
//// R18 Col J
//Cell R18cell9 = row.createCell(9);
//if (record.getR18_fix_depo_91_day_depo() != null) {
//    R18cell9.setCellValue(record.getR18_fix_depo_91_day_depo().doubleValue());
//    R18cell9.setCellStyle(numberStyle);
//} else {
//    R18cell9.setCellValue("");
//    R18cell9.setCellStyle(textStyle);
//}
//
//// R18 Col K
//Cell R18cell10 = row.createCell(10);
//if (record.getR18_fix_depo_1to2() != null) {
//    R18cell10.setCellValue(record.getR18_fix_depo_1to2().doubleValue());
//    R18cell10.setCellStyle(numberStyle);
//} else {
//    R18cell10.setCellValue("");
//    R18cell10.setCellStyle(textStyle);
//}
//
//// R18 Col L
//Cell R18cell11 = row.createCell(11);
//if (record.getR18_fix_depo_4to6() != null) {
//    R18cell11.setCellValue(record.getR18_fix_depo_4to6().doubleValue());
//    R18cell11.setCellStyle(numberStyle);
//} else {
//    R18cell11.setCellValue("");
//    R18cell11.setCellStyle(textStyle);
//}
//
//// R18 Col M
//Cell R18cell12 = row.createCell(12);
//if (record.getR18_fix_depo_7to12() != null) {
//    R18cell12.setCellValue(record.getR18_fix_depo_7to12().doubleValue());
//    R18cell12.setCellStyle(numberStyle);
//} else {
//    R18cell12.setCellValue("");
//    R18cell12.setCellStyle(textStyle);
//}
//
//// R18 Col N
//Cell R18cell13 = row.createCell(13);
//if (record.getR18_fix_depo_13to18()!= null) {
//    R18cell13.setCellValue(record.getR18_fix_depo_13to18().doubleValue());
//    R18cell13.setCellStyle(numberStyle);
//} else {
//    R18cell13.setCellValue("");
//    R18cell13.setCellStyle(textStyle);
//}
//
//// R18 Col O
//Cell R18cell14 = row.createCell(14);
//if (record.getR18_fix_depo_19to24()!= null) {
//    R18cell14.setCellValue(record.getR18_fix_depo_19to24().doubleValue());
//    R18cell14.setCellStyle(numberStyle);
//} else {
//    R18cell14.setCellValue("");
//    R18cell14.setCellStyle(textStyle);
//}
//
//// R18 Col P
//Cell R18cell15 = row.createCell(15);
//if (record.getR18_fix_depo_over24() != null) {
//    R18cell15.setCellValue(record.getR18_fix_depo_over24().doubleValue());
//    R18cell15.setCellStyle(numberStyle);
//} else {
//    R18cell15.setCellValue("");
//    R18cell15.setCellStyle(textStyle);
//}
//
//
//// R18 Col Q
//Cell R18cell16 = row.createCell(16);
//if (record.getR18_cer_of_depo() != null) {
//    R18cell16.setCellValue(record.getR18_cer_of_depo().doubleValue());
//    R18cell16.setCellStyle(numberStyle);
//} else {
//    R18cell16.setCellValue("");
//    R18cell16.setCellStyle(textStyle);
//}
//// R18 Col R
//Cell R18cell17 = row.createCell(17);
//if (record.getR18_total() != null) {
//    R18cell17.setCellValue(record.getR18_total().doubleValue());
//    R18cell17.setCellStyle(numberStyle);
//} else {
//    R18cell17.setCellValue("");
//    R18cell17.setCellStyle(textStyle);
//}
//// R18 Col S
//Cell R18cell18 = row.createCell(18);
//if (record.getR18_pula_equivalent() != null) {
//    R18cell18.setCellValue(record.getR18_pula_equivalent().doubleValue());
//    R18cell18.setCellStyle(numberStyle);
//} else {
//    R18cell18.setCellValue("");
//    R18cell18.setCellStyle(textStyle);
//}
//// R18 Col S
//Cell R18cell19 = row.createCell(19);
//if (record.getR18_avg_pula_equivalent() != null) {
//    R18cell19.setCellValue(record.getR18_avg_pula_equivalent().doubleValue());
//    R18cell18.setCellStyle(numberStyle);
//} else {
//    R18cell19.setCellValue("");
//    R18cell19.setCellStyle(textStyle);
//}
//
////Entity 2
//// R28 Col B
//row = sheet.getRow(27);
//Cell R28cell1 = row.createCell(1);
//if (record.getR28_import() != null) {
//    R28cell1.setCellValue(record.getR28_import().doubleValue());
//    R28cell1.setCellStyle(numberStyle);
//} else {
//    R28cell1.setCellValue("");
//    R28cell1.setCellStyle(textStyle);
//}
//// R28 Col C
//Cell R28cell2 = row.createCell(2);
//if (record.getR28_investment () != null) {
//    R28cell2.setCellValue(record.getR28_investment().doubleValue());
//    R28cell2.setCellStyle(numberStyle);
//} else {
//    R28cell2.setCellValue("");
//    R28cell2.setCellStyle(textStyle);
//}
//// R28 Col D
//Cell R28cell3 = row.createCell(3);
//if (record.getR28_other () != null) {
//    R28cell3.setCellValue(record.getR28_other().doubleValue());
//    R28cell3.setCellStyle(numberStyle);
//} else {
//    R28cell3.setCellValue("");
//    R28cell3.setCellStyle(textStyle);
//}
//// R29 Col B
//row = sheet.getRow(28);
//Cell R29cell1 = row.createCell(1);
//if (record.getR29_import() != null) {
//    R29cell1.setCellValue(record.getR29_import().doubleValue());
//    R29cell1.setCellStyle(numberStyle);
//} else {
//    R29cell1.setCellValue("");
//    R29cell1.setCellStyle(textStyle);
//}
//// R29 Col C
//Cell R29cell2 = row.createCell(2);
//if (record.getR29_investment () != null) {
//    R29cell2.setCellValue(record.getR29_investment().doubleValue());
//    R29cell2.setCellStyle(numberStyle);
//} else {
//    R29cell2.setCellValue("");
//    R29cell2.setCellStyle(textStyle);
//}
//// R29 Col D
//Cell R29cell3 = row.createCell(3);
//if (record.getR29_other () != null) {
//    R29cell3.setCellValue(record.getR29_other().doubleValue());
//    R29cell3.setCellStyle(numberStyle);
//} else {
//    R29cell3.setCellValue("");
//    R29cell3.setCellStyle(textStyle);
//}
//
//// R30 Col B
//row = sheet.getRow(29);
//Cell R30cell1 = row.createCell(1);
//if (record.getR30_import() != null) {
//    R30cell1.setCellValue(record.getR30_import().doubleValue());
//    R30cell1.setCellStyle(numberStyle);
//} else {
//    R30cell1.setCellValue("");
//    R30cell1.setCellStyle(textStyle);
//}
//// R30 Col C
//Cell R30cell2 = row.createCell(2);
//if (record.getR30_investment () != null) {
//    R30cell2.setCellValue(record.getR30_investment().doubleValue());
//    R30cell2.setCellStyle(numberStyle);
//} else {
//    R30cell2.setCellValue("");
//    R30cell2.setCellStyle(textStyle);
//}
//// R30 Col D
//Cell R30cell3 = row.createCell(3);
//if (record.getR30_other () != null) {
//    R30cell3.setCellValue(record.getR30_other().doubleValue());
//    R30cell3.setCellStyle(numberStyle);
//} else {
//    R30cell3.setCellValue("");
//    R30cell3.setCellStyle(textStyle);
//}
//// R31 Col B
//row = sheet.getRow(30);
//Cell R31cell1 = row.createCell(1);
//if (record.getR31_import() != null) {
//    R31cell1.setCellValue(record.getR31_import().doubleValue());
//    R31cell1.setCellStyle(numberStyle);
//} else {
//    R31cell1.setCellValue("");
//    R31cell1.setCellStyle(textStyle);
//}
//// R31 Col C
//Cell R31cell2 = row.createCell(2);
//if (record.getR31_investment () != null) {
//    R31cell2.setCellValue(record.getR31_investment().doubleValue());
//    R31cell2.setCellStyle(numberStyle);
//} else {
//    R31cell2.setCellValue("");
//    R31cell2.setCellStyle(textStyle);
//}
//// R31 Col D
//Cell R31cell3 = row.createCell(3);
//if (record.getR31_other () != null) {
//    R31cell3.setCellValue(record.getR31_other().doubleValue());
//    R31cell3.setCellStyle(numberStyle);
//} else {
//    R31cell3.setCellValue("");
//    R31cell3.setCellStyle(textStyle);
//}
//// R32 Col B
//row = sheet.getRow(31);
//Cell R32cell1 = row.createCell(1);
//if (record.getR32_import() != null) {
//    R32cell1.setCellValue(record.getR32_import().doubleValue());
//    R32cell1.setCellStyle(numberStyle);
//} else {
//    R32cell1.setCellValue("");
//    R32cell1.setCellStyle(textStyle);
//}
//// R32 Col C
//Cell R32cell2 = row.createCell(2);
//if (record.getR32_investment () != null) {
//    R32cell2.setCellValue(record.getR32_investment().doubleValue());
//    R32cell2.setCellStyle(numberStyle);
//} else {
//    R32cell2.setCellValue("");
//    R32cell2.setCellStyle(textStyle);
//}
//// R32 Col D
//Cell R32cell3 = row.createCell(3);
//if (record.getR32_other () != null) {
//    R32cell3.setCellValue(record.getR32_other().doubleValue());
//    R32cell3.setCellStyle(numberStyle);
//} else {
//    R32cell3.setCellValue("");
//    R32cell3.setCellStyle(textStyle);
//}
//// R33 Col B
//row = sheet.getRow(32);
//Cell R33cell1 = row.createCell(1);
//if (record.getR33_import() != null) {
//    R33cell1.setCellValue(record.getR33_import().doubleValue());
//    R33cell1.setCellStyle(numberStyle);
//} else {
//    R33cell1.setCellValue("");
//    R33cell1.setCellStyle(textStyle);
//}
//// R33 Col C
//Cell R33cell2 = row.createCell(2);
//if (record.getR33_investment () != null) {
//    R33cell2.setCellValue(record.getR33_investment().doubleValue());
//    R33cell2.setCellStyle(numberStyle);
//} else {
//    R33cell2.setCellValue("");
//    R33cell2.setCellStyle(textStyle);
//}
//// R33 Col D
//Cell R33cell3 = row.createCell(3);
//if (record.getR33_other () != null) {
//    R33cell3.setCellValue(record.getR33_other().doubleValue());
//    R33cell3.setCellStyle(numberStyle);
//} else {
//    R33cell3.setCellValue("");
//    R33cell3.setCellStyle(textStyle);
//}
//
////Entity 3
//
//row = sheet.getRow(27);
//Cell R28cell1e3 = row.createCell(8);
//if (record.getR28_residents () != null) {
//    R28cell1e3.setCellValue(record.getR28_residents().doubleValue());
//    R28cell1e3.setCellStyle(numberStyle);
//} else {
//    R28cell1e3.setCellValue("");
//    R28cell1e3.setCellStyle(textStyle);
//}
//// R28 Col C
//Cell R28cell2e3 = row.createCell(9);
//if (record.getR28_non_residents () != null) {
//    R28cell2e3.setCellValue(record.getR28_non_residents().doubleValue());
//    R28cell2e3.setCellStyle(numberStyle);
//} else {
//    R28cell2e3.setCellValue("");
//    R28cell2e3.setCellStyle(textStyle);
//}
//// R29 Col B
//row = sheet.getRow(28);
//Cell R29cell1e3 = row.createCell(8);
//if (record.getR29_residents () != null) {
//    R29cell1e3.setCellValue(record.getR29_residents().doubleValue());
//    R29cell1e3.setCellStyle(numberStyle);
//} else {
//    R29cell1e3.setCellValue("");
//    R29cell1e3.setCellStyle(textStyle);
//}
//// R29 Col C
//Cell R29cell2e3 = row.createCell(9);
//if (record.getR29_non_residents () != null) {
//    R29cell2e3.setCellValue(record.getR29_non_residents().doubleValue());
//    R29cell2e3.setCellStyle(numberStyle);
//} else {
//    R29cell2e3.setCellValue("");
//    R29cell2e3.setCellStyle(textStyle);
//}
//// R30 Col B
//row = sheet.getRow(29);
//Cell R30cell1e3 = row.createCell(8);
//if (record.getR30_residents () != null) {
//    R30cell1e3.setCellValue(record.getR30_residents().doubleValue());
//    R30cell1e3.setCellStyle(numberStyle);
//} else {
//    R30cell1e3.setCellValue("");
//    R30cell1e3.setCellStyle(textStyle);
//}
//// R30 Col C
//Cell R30cell2e3 = row.createCell(9);
//if (record.getR30_non_residents () != null) {
//    R30cell2e3.setCellValue(record.getR30_non_residents().doubleValue());
//    R30cell2e3.setCellStyle(numberStyle);
//} else {
//    R30cell2e3.setCellValue("");
//    R30cell2e3.setCellStyle(textStyle);
//}
//// R31 Col B
//row = sheet.getRow(30);
//Cell R31cell1e3 = row.createCell(8);
//if (record.getR31_residents () != null) {
//    R31cell1e3.setCellValue(record.getR31_residents().doubleValue());
//    R31cell1e3.setCellStyle(numberStyle);
//} else {
//    R31cell1e3.setCellValue("");
//    R31cell1e3.setCellStyle(textStyle);
//}
//// R31 Col C
//Cell R31cell2e3 = row.createCell(9);
//if (record.getR31_non_residents () != null) {
//    R31cell2e3.setCellValue(record.getR31_non_residents().doubleValue());
//    R31cell2e3.setCellStyle(numberStyle);
//} else {
//    R31cell2e3.setCellValue("");
//    R31cell2e3.setCellStyle(textStyle);
//}
//
//// R32 Col B
//row = sheet.getRow(31);
//Cell R32cell1e3 = row.createCell(8);
//if (record.getR32_residents () != null) {
//    R32cell1e3.setCellValue(record.getR32_residents().doubleValue());
//    R32cell1e3.setCellStyle(numberStyle);
//} else {
//    R32cell1e3.setCellValue("");
//    R32cell1e3.setCellStyle(textStyle);
//}
//// R32 Col C
//Cell R32cell2e3 = row.createCell(9);
//if (record.getR32_non_residents () != null) {
//    R32cell2e3.setCellValue(record.getR32_non_residents().doubleValue());
//    R32cell2e3.setCellStyle(numberStyle);
//} else {
//    R32cell2e3.setCellValue("");
//    R32cell2e3.setCellStyle(textStyle);
//}
//// R33 Col B
//row = sheet.getRow(32);
//Cell R33cell1e3 = row.createCell(8);
//if (record.getR33_residents () != null) {
//    R33cell1e3.setCellValue(record.getR33_residents().doubleValue());
//    R33cell1e3.setCellStyle(numberStyle);
//} else {
//    R33cell1e3.setCellValue("");
//    R33cell1e3.setCellStyle(textStyle);
//}
//// R33 Col C
//Cell R33cell2e3 = row.createCell(9);
//if (record.getR33_non_residents () != null) {
//    R33cell2e3.setCellValue(record.getR33_non_residents().doubleValue());
//    R33cell2e3.setCellStyle(numberStyle);
//} else {
//    R33cell2e3.setCellValue("");
//    R33cell2e3.setCellStyle(textStyle);
//}	
//
//
//
//
//
//				}
//				workbook.setForceFormulaRecalculation(true);
//			} else {
//
//			}
//
//			// Write the final workbook content to the in-memory stream.
//			workbook.write(out);
//
//			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
//
//			return out.toByteArray();
//		}
//
//	}
//
//	// Resub Email Excel
//	public byte[] BRRS_M_DEP3ResubEmailExcel(String filename, String reportId, String fromdate, String todate,
//			String currency, String dtltype, String type, BigDecimal version) throws Exception {
//
//		logger.info("Service: Starting Archival Email Excel generation process in memory.");
//
//List<M_DEP3_Resub_Summary_Entity> dataList = brrs_M_DEP3_resub_summary_repo
//				.getdatabydateListarchival(dateformat.parse(todate), version);
//		List<M_DEP3_Resub_Archival_Summary_Entity> dataList1 = brrs_M_DEP3_resub_Archival_summary_repo
//				.getdatabydateListarchival(dateformat.parse(todate), version);
//
//		if (dataList.isEmpty()) {
//			logger.warn("Service: No data found for BRRS_M_DEP3 report. Returning empty result.");
//			return new byte[0];
//		}
//
//		String templateDir = env.getProperty("output.exportpathtemp");
//		String templateFileName = filename;
//		System.out.println(filename);
//		Path templatePath = Paths.get(templateDir, templateFileName);
//		System.out.println(templatePath);
//
//		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());
//
//		if (!Files.exists(templatePath)) {
//			// This specific exception will be caught by the controller.
//			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
//		}
//		if (!Files.isReadable(templatePath)) {
//			// A specific exception for permission errors.
//			throw new SecurityException(
//					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
//		}
//
//		// This try-with-resources block is perfect. It guarantees all resources are
//		// closed automatically.
//		try (InputStream templateInputStream = Files.newInputStream(templatePath);
//				Workbook workbook = WorkbookFactory.create(templateInputStream);
//				ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//
//			Sheet sheet = workbook.getSheetAt(0);
//
//			// --- Style Definitions ---
//			CreationHelper createHelper = workbook.getCreationHelper();
//
//			CellStyle dateStyle = workbook.createCellStyle();
//			dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
//			dateStyle.setBorderBottom(BorderStyle.THIN);
//			dateStyle.setBorderTop(BorderStyle.THIN);
//			dateStyle.setBorderLeft(BorderStyle.THIN);
//			dateStyle.setBorderRight(BorderStyle.THIN);
//
//			CellStyle textStyle = workbook.createCellStyle();
//			textStyle.setBorderBottom(BorderStyle.THIN);
//			textStyle.setBorderTop(BorderStyle.THIN);
//			textStyle.setBorderLeft(BorderStyle.THIN);
//			textStyle.setBorderRight(BorderStyle.THIN);
//
//			// Create the font
//			Font font = workbook.createFont();
//			font.setFontHeightInPoints((short) 8); // size 8
//			font.setFontName("Arial");
//
//			CellStyle numberStyle = workbook.createCellStyle();
//			// numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
//			numberStyle.setBorderBottom(BorderStyle.THIN);
//			numberStyle.setBorderTop(BorderStyle.THIN);
//			numberStyle.setBorderLeft(BorderStyle.THIN);
//			numberStyle.setBorderRight(BorderStyle.THIN);
//			numberStyle.setFont(font);
//			// --- End of Style Definitions ---
//
//			int startRow = 10;
//
//			if (!dataList.isEmpty()) {
//				for (int i = 0; i < dataList.size(); i++) {
//					
//					M_DEP3_Resub_Summary_Entity record = dataList.get(i);
//					M_DEP3_Resub_Archival_Summary_Entity record = dataList1.get(i);
//					
//					System.out.println("rownumber=" + startRow + i);
//					Row row = sheet.getRow(startRow + i);
//					if (row == null) {
//						row = sheet.createRow(startRow + i);
//					}
////EMAIL
//
//					// R11 Col B
//            
//Cell R11cell1 = row.createCell(1);
//if (record.getR11_ex_rate_buy() != null) {
//    R11cell1.setCellValue(record.getR11_ex_rate_buy().doubleValue());
//    R11cell1.setCellStyle(numberStyle);
//} else {
//    R11cell1.setCellValue("");
//    R11cell1.setCellStyle(textStyle);
//}
//
//// R11 Col C
//Cell R11cell2 = row.createCell(2);
//if (record.getR11_ex_rate_mid() != null) {
//    R11cell2.setCellValue(record.getR11_ex_rate_mid().doubleValue());
//    R11cell2.setCellStyle(numberStyle);
//} else {
//    R11cell2.setCellValue("");
//    R11cell2.setCellStyle(textStyle);
//}
//
//// R11 Col D
//Cell R11cell3 = row.createCell(3);
//if (record.getR11_ex_rate_sell() != null) {
//    R11cell3.setCellValue(record.getR11_ex_rate_sell().doubleValue());
//    R11cell3.setCellStyle(numberStyle);
//} else {
//    R11cell3.setCellValue("");
//    R11cell3.setCellStyle(textStyle);
//}
//
//// R11 Col E
//Cell R11cell4 = row.createCell(4);
//if (record.getR11_current()!= null) {
//    R11cell4.setCellValue(record.getR11_current().doubleValue());
//    R11cell4.setCellStyle(numberStyle);
//} else {
//    R11cell4.setCellValue("");
//    R11cell1.setCellStyle(textStyle);
//}
//
//// R11 Col F
//Cell R11cell5 = row.createCell(5);
//if (record.getR11_call() != null) {
//    R11cell5.setCellValue(record.getR11_call().doubleValue());
//    R11cell5.setCellStyle(numberStyle);
//} else {
//    R11cell5.setCellValue("");
//    R11cell5.setCellStyle(textStyle);
//}
//
//// R11 Col G
//Cell R11cell6 = row.createCell(6);
//if (record.getR11_savings() != null) {
//    R11cell6.setCellValue(record.getR11_savings().doubleValue());
//    R11cell6.setCellStyle(numberStyle);
//} else {
//    R11cell6.setCellValue("");
//    R11cell6.setCellStyle(textStyle);
//	
//}
//
//// R11 Col H
//Cell R11cell7 = row.createCell(7);
//if (record.getR11_notice_0to31()!= null) {
//    R11cell7.setCellValue(record.getR11_notice_0to31().doubleValue());
//    R11cell7.setCellStyle(numberStyle);
//} else {
//    R11cell7.setCellValue("");
//    R11cell7.setCellStyle(textStyle);
//	
//}
//
//// R11 Col I
//Cell R11cell8= row.createCell(8);
//if (record.getR11_notice_32to88() != null) {
//    R11cell8.setCellValue(record.getR11_notice_32to88().doubleValue());
//    R11cell8.setCellStyle(numberStyle);
//} else {
//    R11cell8.setCellValue("");
//    R11cell8.setCellStyle(textStyle);
//}
//
//// R11 Col J
//Cell R11cell9 = row.createCell(9);
//if (record.getR11_fix_depo_91_day_depo() != null) {
//    R11cell9.setCellValue(record.getR11_fix_depo_91_day_depo().doubleValue());
//    R11cell9.setCellStyle(numberStyle);
//} else {
//    R11cell9.setCellValue("");
//    R11cell9.setCellStyle(textStyle);
//}
//
//// R11 Col K
//Cell R11cell10 = row.createCell(10);
//if (record.getR11_fix_depo_1to2() != null) {
//    R11cell10.setCellValue(record.getR11_fix_depo_1to2().doubleValue());
//    R11cell10.setCellStyle(numberStyle);
//} else {
//    R11cell10.setCellValue("");
//    R11cell10.setCellStyle(textStyle);
//}
//
//// R11 Col L
//Cell R11cell11 = row.createCell(11);
//if (record.getR11_fix_depo_4to6() != null) {
//    R11cell11.setCellValue(record.getR11_fix_depo_4to6().doubleValue());
//    R11cell11.setCellStyle(numberStyle);
//} else {
//    R11cell11.setCellValue("");
//    R11cell11.setCellStyle(textStyle);
//}
//
//// R11 Col M
//Cell R11cell12 = row.createCell(12);
//if (record.getR11_fix_depo_7to12() != null) {
//    R11cell12.setCellValue(record.getR11_fix_depo_7to12().doubleValue());
//    R11cell12.setCellStyle(numberStyle);
//} else {
//    R11cell12.setCellValue("");
//    R11cell12.setCellStyle(textStyle);
//}
//
//// R11 Col N
//Cell R11cell13 = row.createCell(13);
//if (record.getR11_fix_depo_13to18()!= null) {
//    R11cell13.setCellValue(record.getR11_fix_depo_13to18().doubleValue());
//    R11cell13.setCellStyle(numberStyle);
//} else {
//    R11cell13.setCellValue("");
//    R11cell13.setCellStyle(textStyle);
//}
//
//// R11 Col O
//Cell R11cell14 = row.createCell(14);
//if (record.getR11_fix_depo_19to24()!= null) {
//    R11cell14.setCellValue(record.getR11_fix_depo_19to24().doubleValue());
//    R11cell14.setCellStyle(numberStyle);
//} else {
//    R11cell14.setCellValue("");
//    R11cell14.setCellStyle(textStyle);
//}
//
//// R11 Col P
//Cell R11cell15 = row.createCell(15);
//if (record.getR11_fix_depo_over24()!= null) {
//    R11cell15.setCellValue(record.getR11_fix_depo_over24().doubleValue());
//    R11cell15.setCellStyle(numberStyle);
//} else {
//    R11cell15.setCellValue("");
//    R11cell15.setCellStyle(textStyle);
//}
//
//
//// R11 Col Q
//Cell R11cell16 = row.createCell(16);
//if (record.getR11_cer_of_depo()!= null) {
//    R11cell16.setCellValue(record.getR11_cer_of_depo().doubleValue());
//    R11cell16.setCellStyle(numberStyle);
//} else {
//    R11cell16.setCellValue("");
//    R11cell16.setCellStyle(textStyle);
// 
//}
//
//// R11 Col S
//Cell R11cell18 = row.createCell(18);
//if (record.getR11_pula_equivalent () != null) {
//    R11cell18.setCellValue(record.getR11_pula_equivalent().doubleValue());
//    R11cell18.setCellStyle(numberStyle);
//} else {
//    R11cell18.setCellValue("");
//    R11cell18.setCellStyle(textStyle);
//}
// 
//
//// R11 Col T
//Cell R11cell19 = row.createCell(19);
//if (record.getR11_avg_pula_equivalent ()!= null) {
//    R11cell19.setCellValue(record.getR11_avg_pula_equivalent().doubleValue());
//    R11cell19.setCellStyle(numberStyle);
//} else {
//    R11cell19.setCellValue("");
//    R11cell19.setCellStyle(textStyle);
//}
//row = sheet.getRow(11);
//// R12 Col B
//Cell R12cell1 = row.createCell(1);
//if (record.getR12_ex_rate_buy ()!= null) {
//    R12cell1.setCellValue(record.getR12_ex_rate_buy().doubleValue());
//    R12cell1.setCellStyle(numberStyle);
//} else {
//    R12cell1.setCellValue("");
//    R12cell1.setCellStyle(textStyle);
//}
//
//// R12 Col C
//Cell R12cell2 = row.createCell(2);
//if (record.getR12_ex_rate_mid() != null) {
//    R12cell2.setCellValue(record.getR12_ex_rate_mid().doubleValue());
//    R12cell2.setCellStyle(numberStyle);
//} else {
//    R12cell2.setCellValue("");
//    R12cell2.setCellStyle(textStyle);
//}
//
//// R12 Col D
//Cell R12cell3 = row.createCell(3);
//if (record.getR12_ex_rate_sell() != null) {
//    R12cell3.setCellValue(record.getR12_ex_rate_sell().doubleValue());
//    R12cell3.setCellStyle(numberStyle);
//} else {
//    R12cell3.setCellValue("");
//    R12cell3.setCellStyle(textStyle);
//}
//
//// R12 Col E
//Cell R12cell4 = row.createCell(4);
//if (record.getR12_current() != null) {
//    R12cell4.setCellValue(record.getR12_current().doubleValue());
//    R12cell4.setCellStyle(numberStyle);
//} else {
//    R12cell4.setCellValue("");
//    R12cell1.setCellStyle(textStyle);
//}
//
//// R12 Col F
//Cell R12cell5 = row.createCell(5);
//if (record.getR12_call() != null) {
//    R12cell5.setCellValue(record.getR12_call().doubleValue());
//    R12cell5.setCellStyle(numberStyle);
//} else {
//    R12cell5.setCellValue("");
//    R12cell5.setCellStyle(textStyle);
//}
//
//// R12 Col G
//Cell R12cell6 = row.createCell(6);
//if (record.getR12_savings() != null) {
//    R12cell6.setCellValue(record.getR12_savings().doubleValue());
//    R12cell6.setCellStyle(numberStyle);
//} else {
//    R12cell6.setCellValue("");
//    R12cell6.setCellStyle(textStyle);
//	
//}
//
//// R12 Col H
//Cell R12cell7 = row.createCell(7);
//if (record.getR12_notice_0to31()!= null) {
//    R12cell7.setCellValue(record.getR12_notice_0to31().doubleValue());
//    R12cell7.setCellStyle(numberStyle);
//} else {
//    R12cell7.setCellValue("");
//    R12cell7.setCellStyle(textStyle);
//	
//}
//
//// R12 Col I
//Cell R12cell8= row.createCell(8);
//if (record.getR12_notice_32to88() != null) {
//    R12cell8.setCellValue(record.getR12_notice_32to88().doubleValue());
//    R12cell8.setCellStyle(numberStyle);
//} else {
//    R12cell8.setCellValue("");
//    R12cell8.setCellStyle(textStyle);
//}
//
//// R12 Col J
//Cell R12cell9 = row.createCell(9);
//if (record.getR12_fix_depo_91_day_depo() != null) {
//    R12cell9.setCellValue(record.getR12_fix_depo_91_day_depo().doubleValue());
//    R12cell9.setCellStyle(numberStyle);
//} else {
//    R12cell9.setCellValue("");
//    R12cell9.setCellStyle(textStyle);
//}
//
//// R12 Col K
//Cell R12cell10 = row.createCell(10);
//if (record.getR12_fix_depo_1to2() != null) {
//    R12cell10.setCellValue(record.getR12_fix_depo_1to2().doubleValue());
//    R12cell10.setCellStyle(numberStyle);
//} else {
//    R12cell10.setCellValue("");
//    R12cell10.setCellStyle(textStyle);
//}
//
//// R12 Col L
//Cell R12cell11 = row.createCell(11);
//if (record.getR12_fix_depo_4to6() != null) {
//    R12cell11.setCellValue(record.getR12_fix_depo_4to6().doubleValue());
//    R12cell11.setCellStyle(numberStyle);
//} else {
//    R12cell11.setCellValue("");
//    R12cell11.setCellStyle(textStyle);
//}
//
//// R12 Col M
//Cell R12cell12 = row.createCell(12);
//if (record.getR12_fix_depo_7to12() != null) {
//    R12cell12.setCellValue(record.getR12_fix_depo_7to12().doubleValue());
//    R12cell12.setCellStyle(numberStyle);
//} else {
//    R12cell12.setCellValue("");
//    R12cell12.setCellStyle(textStyle);
//}
//
//// R12 Col N
//Cell R12cell13 = row.createCell(13);
//if (record.getR12_fix_depo_13to18()!= null) {
//    R12cell13.setCellValue(record.getR12_fix_depo_13to18().doubleValue());
//    R12cell13.setCellStyle(numberStyle);
//} else {
//    R12cell13.setCellValue("");
//    R12cell13.setCellStyle(textStyle);
//}
//
//// R12 Col O
//Cell R12cell14 = row.createCell(14);
//if (record.getR12_fix_depo_19to24()!= null) {
//    R12cell14.setCellValue(record.getR12_fix_depo_19to24().doubleValue());
//    R12cell14.setCellStyle(numberStyle);
//} else {
//    R12cell14.setCellValue("");
//    R12cell14.setCellStyle(textStyle);
//}
//
//// R12 Col P
//Cell R12cell15 = row.createCell(15);
//if (record.getR12_fix_depo_over24() != null) {
//    R12cell15.setCellValue(record.getR12_fix_depo_over24().doubleValue());
//    R12cell15.setCellStyle(numberStyle);
//} else {
//    R12cell15.setCellValue("");
//    R12cell15.setCellStyle(textStyle);
//}
//
//
//// R12 Col Q
//Cell R12cell16 = row.createCell(16);
//if (record.getR12_cer_of_depo() != null) {
//    R12cell16.setCellValue(record.getR12_cer_of_depo().doubleValue());
//    R12cell16.setCellStyle(numberStyle);
//} else {
//    R12cell16.setCellValue("");
//    R12cell16.setCellStyle(textStyle);
//}
//
//// R12 Col S
//Cell R12cell18 = row.createCell(18);
//if (record.getR12_pula_equivalent() != null) {
//    R12cell18.setCellValue(record.getR12_pula_equivalent().doubleValue());
//    R12cell18.setCellStyle(numberStyle);
//} else {
//    R12cell18.setCellValue("");
//    R12cell18.setCellStyle(textStyle);
//}
// 
//
//// R12 Col T
//Cell R12cell19 = row.createCell(19);
//if (record.getR12_avg_pula_equivalent() != null) {
//    R12cell19.setCellValue(record.getR12_avg_pula_equivalent().doubleValue());
//    R12cell19.setCellStyle(numberStyle);
//} else {
//    R12cell19.setCellValue("");
//    R12cell19.setCellStyle(textStyle);
//}
//
//// R13 Col B
//row = sheet.getRow(12);
//Cell R13cell1 = row.createCell(1);
//if (record.getR13_ex_rate_buy() != null) {
//    R13cell1.setCellValue(record.getR13_ex_rate_buy().doubleValue());
//    R13cell1.setCellStyle(numberStyle);
//} else {
//    R13cell1.setCellValue("");
//    R13cell1.setCellStyle(textStyle);
//}
//
//// R13 Col C
//Cell R13cell2 = row.createCell(2);
//if (record.getR13_ex_rate_mid() != null) {
//    R13cell2.setCellValue(record.getR13_ex_rate_mid().doubleValue());
//    R13cell2.setCellStyle(numberStyle);
//} else {
//    R13cell2.setCellValue("");
//    R13cell2.setCellStyle(textStyle);
//}
//
//// R13 Col D
//Cell R13cell3 = row.createCell(3);
//if (record.getR13_ex_rate_sell() != null) {
//    R13cell3.setCellValue(record.getR13_ex_rate_sell().doubleValue());
//    R13cell3.setCellStyle(numberStyle);
//} else {
//    R13cell3.setCellValue("");
//    R13cell3.setCellStyle(textStyle);
//}
//
//// R13 Col E
//Cell R13cell4 = row.createCell(4);
//if (record.getR13_current() != null) {
//    R13cell4.setCellValue(record.getR13_current().doubleValue());
//    R13cell4.setCellStyle(numberStyle);
//} else {
//    R13cell4.setCellValue("");
//    R13cell1.setCellStyle(textStyle);
//}
//
//// R13 Col F
//Cell R13cell5 = row.createCell(5);
//if (record.getR13_call() != null) {
//    R13cell5.setCellValue(record.getR13_call().doubleValue());
//    R13cell5.setCellStyle(numberStyle);
//} else {
//    R13cell5.setCellValue("");
//    R13cell5.setCellStyle(textStyle);
//}
//
//// R13 Col G
//Cell R13cell6 = row.createCell(6);
//if (record.getR13_savings() != null) {
//    R13cell6.setCellValue(record.getR13_savings().doubleValue());
//    R13cell6.setCellStyle(numberStyle);
//} else {
//    R13cell6.setCellValue("");
//    R13cell6.setCellStyle(textStyle);
//	
//}
//
//// R13 Col H
//Cell R13cell7 = row.createCell(7);
//if (record.getR13_notice_0to31()!= null) {
//    R13cell7.setCellValue(record.getR13_notice_0to31().doubleValue());
//    R13cell7.setCellStyle(numberStyle);
//} else {
//    R13cell7.setCellValue("");
//    R13cell7.setCellStyle(textStyle);
//	
//}
//
//// R13 Col I
//Cell R13cell8= row.createCell(8);
//if (record.getR13_notice_32to88() != null) {
//    R13cell8.setCellValue(record.getR13_notice_32to88().doubleValue());
//    R13cell8.setCellStyle(numberStyle);
//} else {
//    R13cell8.setCellValue("");
//    R13cell8.setCellStyle(textStyle);
//}
//
//// R13 Col J
//Cell R13cell9 = row.createCell(9);
//if (record.getR13_fix_depo_91_day_depo() != null) {
//    R13cell9.setCellValue(record.getR13_fix_depo_91_day_depo().doubleValue());
//    R13cell9.setCellStyle(numberStyle);
//} else {
//    R13cell9.setCellValue("");
//    R13cell9.setCellStyle(textStyle);
//}
//
//// R13 Col K
//Cell R13cell10 = row.createCell(10);
//if (record.getR13_fix_depo_1to2() != null) {
//    R13cell10.setCellValue(record.getR13_fix_depo_1to2().doubleValue());
//    R13cell10.setCellStyle(numberStyle);
//} else {
//    R13cell10.setCellValue("");
//    R13cell10.setCellStyle(textStyle);
//}
//
//// R13 Col L
//Cell R13cell11 = row.createCell(11);
//if (record.getR13_fix_depo_4to6() != null) {
//    R13cell11.setCellValue(record.getR13_fix_depo_4to6().doubleValue());
//    R13cell11.setCellStyle(numberStyle);
//} else {
//    R13cell11.setCellValue("");
//    R13cell11.setCellStyle(textStyle);
//}
//
//// R13 Col M
//Cell R13cell12 = row.createCell(12);
//if (record.getR13_fix_depo_7to12() != null) {
//    R13cell12.setCellValue(record.getR13_fix_depo_7to12().doubleValue());
//    R13cell12.setCellStyle(numberStyle);
//} else {
//    R13cell12.setCellValue("");
//    R13cell12.setCellStyle(textStyle);
//}
//
//// R13 Col N
//Cell R13cell13 = row.createCell(13);
//if (record.getR13_fix_depo_13to18()!= null) {
//    R13cell13.setCellValue(record.getR13_fix_depo_13to18().doubleValue());
//    R13cell13.setCellStyle(numberStyle);
//} else {
//    R13cell13.setCellValue("");
//    R13cell13.setCellStyle(textStyle);
//}
//
//// R13 Col O
//Cell R13cell14 = row.createCell(14);
//if (record.getR13_fix_depo_19to24()!= null) {
//    R13cell14.setCellValue(record.getR13_fix_depo_19to24().doubleValue());
//    R13cell14.setCellStyle(numberStyle);
//} else {
//    R13cell14.setCellValue("");
//    R13cell14.setCellStyle(textStyle);
//}
//
//// R13 Col P
//Cell R13cell15 = row.createCell(15);
//if (record.getR13_fix_depo_over24() != null) {
//    R13cell15.setCellValue(record.getR13_fix_depo_over24().doubleValue());
//    R13cell15.setCellStyle(numberStyle);
//} else {
//    R13cell15.setCellValue("");
//    R13cell15.setCellStyle(textStyle);
//}
//
//
//// R13 Col Q
//Cell R13cell16 = row.createCell(16);
//if (record.getR13_cer_of_depo() != null) {
//    R13cell16.setCellValue(record.getR13_cer_of_depo().doubleValue());
//    R13cell16.setCellStyle(numberStyle);
//} else {
//    R13cell16.setCellValue("");
//    R13cell16.setCellStyle(textStyle);
//}
//
//// R13 Col S
//Cell R13cell18 = row.createCell(18);
//if (record.getR13_pula_equivalent() != null) {
//    R13cell18.setCellValue(record.getR13_pula_equivalent().doubleValue());
//    R13cell18.setCellStyle(numberStyle);
//} else {
//    R13cell18.setCellValue("");
//    R13cell18.setCellStyle(textStyle);
//}
// 
//
//// R13 Col T
//Cell R13cell19 = row.createCell(19);
//if (record.getR13_avg_pula_equivalent() != null) {
//    R13cell19.setCellValue(record.getR13_avg_pula_equivalent().doubleValue());
//    R13cell19.setCellStyle(numberStyle);
//} else {
//    R13cell19.setCellValue("");
//    R13cell19.setCellStyle(textStyle);
//}
//
//// R14 Col B
//row = sheet.getRow(13);
//Cell R14cell1 = row.createCell(1);
//if (record.getR14_ex_rate_buy() != null) {
//    R14cell1.setCellValue(record.getR14_ex_rate_buy().doubleValue());
//    R14cell1.setCellStyle(numberStyle);
//} else {
//    R14cell1.setCellValue("");
//    R14cell1.setCellStyle(textStyle);
//}
//
//// R14 Col C
//Cell R14cell2 = row.createCell(2);
//if (record.getR14_ex_rate_mid() != null) {
//    R14cell2.setCellValue(record.getR14_ex_rate_mid().doubleValue());
//    R14cell2.setCellStyle(numberStyle);
//} else {
//    R14cell2.setCellValue("");
//    R14cell2.setCellStyle(textStyle);
//}
//
//// R14 Col D
//Cell R14cell3 = row.createCell(3);
//if (record.getR14_ex_rate_sell() != null) {
//    R14cell3.setCellValue(record.getR14_ex_rate_sell().doubleValue());
//    R14cell3.setCellStyle(numberStyle);
//} else {
//    R14cell3.setCellValue("");
//    R14cell3.setCellStyle(textStyle);
//}
//
//// R14 Col E
//Cell R14cell4 = row.createCell(4);
//if (record.getR14_current() != null) {
//    R14cell4.setCellValue(record.getR14_current().doubleValue());
//    R14cell4.setCellStyle(numberStyle);
//} else {
//    R14cell4.setCellValue("");
//    R14cell1.setCellStyle(textStyle);
//}
//
//// R14 Col F
//Cell R14cell5 = row.createCell(5);
//if (record.getR14_call() != null) {
//    R14cell5.setCellValue(record.getR14_call().doubleValue());
//    R14cell5.setCellStyle(numberStyle);
//} else {
//    R14cell5.setCellValue("");
//    R14cell5.setCellStyle(textStyle);
//}
//
//// R14 Col G
//Cell R14cell6 = row.createCell(6);
//if (record.getR14_savings() != null) {
//    R14cell6.setCellValue(record.getR14_savings().doubleValue());
//    R14cell6.setCellStyle(numberStyle);
//} else {
//    R14cell6.setCellValue("");
//    R14cell6.setCellStyle(textStyle);
//	
//}
//
//// R14 Col H
//Cell R14cell7 = row.createCell(7);
//if (record.getR14_notice_0to31()!= null) {
//    R14cell7.setCellValue(record.getR14_notice_0to31().doubleValue());
//    R14cell7.setCellStyle(numberStyle);
//} else {
//    R14cell7.setCellValue("");
//    R14cell7.setCellStyle(textStyle);
//	
//}
//
//// R14 Col I
//Cell R14cell8= row.createCell(8);
//if (record.getR14_notice_32to88() != null) {
//    R14cell8.setCellValue(record.getR14_notice_32to88().doubleValue());
//    R14cell8.setCellStyle(numberStyle);
//} else {
//    R14cell8.setCellValue("");
//    R14cell8.setCellStyle(textStyle);
//}
//
//// R14 Col J
//Cell R14cell9 = row.createCell(9);
//if (record.getR14_fix_depo_91_day_depo() != null) {
//    R14cell9.setCellValue(record.getR14_fix_depo_91_day_depo().doubleValue());
//    R14cell9.setCellStyle(numberStyle);
//} else {
//    R14cell9.setCellValue("");
//    R14cell9.setCellStyle(textStyle);
//}
//
//// R14 Col K
//Cell R14cell10 = row.createCell(10);
//if (record.getR14_fix_depo_1to2() != null) {
//    R14cell10.setCellValue(record.getR14_fix_depo_1to2().doubleValue());
//    R14cell10.setCellStyle(numberStyle);
//} else {
//    R14cell10.setCellValue("");
//    R14cell10.setCellStyle(textStyle);
//}
//
//// R14 Col L
//Cell R14cell11 = row.createCell(11);
//if (record.getR14_fix_depo_4to6() != null) {
//    R14cell11.setCellValue(record.getR14_fix_depo_4to6().doubleValue());
//    R14cell11.setCellStyle(numberStyle);
//} else {
//    R14cell11.setCellValue("");
//    R14cell11.setCellStyle(textStyle);
//}
//
//// R14 Col M
//Cell R14cell12 = row.createCell(12);
//if (record.getR14_fix_depo_7to12() != null) {
//    R14cell12.setCellValue(record.getR14_fix_depo_7to12().doubleValue());
//    R14cell12.setCellStyle(numberStyle);
//} else {
//    R14cell12.setCellValue("");
//    R14cell12.setCellStyle(textStyle);
//}
//
//// R14 Col N
//Cell R14cell13 = row.createCell(13);
//if (record.getR14_fix_depo_13to18()!= null) {
//    R14cell13.setCellValue(record.getR14_fix_depo_13to18().doubleValue());
//    R14cell13.setCellStyle(numberStyle);
//} else {
//    R14cell13.setCellValue("");
//    R14cell13.setCellStyle(textStyle);
//}
//
//// R14 Col O
//Cell R14cell14 = row.createCell(14);
//if (record.getR14_fix_depo_19to24()!= null) {
//    R14cell14.setCellValue(record.getR14_fix_depo_19to24().doubleValue());
//    R14cell14.setCellStyle(numberStyle);
//} else {
//    R14cell14.setCellValue("");
//    R14cell14.setCellStyle(textStyle);
//}
//
//// R14 Col P
//Cell R14cell15 = row.createCell(15);
//if (record.getR14_fix_depo_over24() != null) {
//    R14cell15.setCellValue(record.getR14_fix_depo_over24().doubleValue());
//    R14cell15.setCellStyle(numberStyle);
//} else {
//    R14cell15.setCellValue("");
//    R14cell15.setCellStyle(textStyle);
//}
//
//
//// R14 Col Q
//Cell R14cell16 = row.createCell(16);
//if (record.getR14_cer_of_depo() != null) {
//    R14cell16.setCellValue(record.getR14_cer_of_depo().doubleValue());
//    R14cell16.setCellStyle(numberStyle);
//} else {
//    R14cell16.setCellValue("");
//    R14cell16.setCellStyle(textStyle);
//}
//
//// R14 Col S
//Cell R14cell18 = row.createCell(18);
//if (record.getR14_pula_equivalent() != null) {
//    R14cell18.setCellValue(record.getR14_pula_equivalent().doubleValue());
//    R14cell18.setCellStyle(numberStyle);
//} else {
//    R14cell18.setCellValue("");
//    R14cell18.setCellStyle(textStyle);
//}
// 
//
//// R14 Col T
//Cell R14cell19 = row.createCell(19);
//if (record.getR14_avg_pula_equivalent() != null) {
//    R14cell19.setCellValue(record.getR14_avg_pula_equivalent().doubleValue());
//    R14cell19.setCellStyle(numberStyle);
//} else {
//    R14cell19.setCellValue("");
//    R14cell19.setCellStyle(textStyle);
//}
//
//// R15 Col B
//row = sheet.getRow(14);
//Cell R15cell1 = row.createCell(1);
//if (record.getR15_ex_rate_buy() != null) {
//    R15cell1.setCellValue(record.getR15_ex_rate_buy().doubleValue());
//    R15cell1.setCellStyle(numberStyle);
//} else {
//    R15cell1.setCellValue("");
//    R15cell1.setCellStyle(textStyle);
//}
//
//// R15 Col C
//Cell R15cell2 = row.createCell(2);
//if (record.getR15_ex_rate_mid() != null) {
//    R15cell2.setCellValue(record.getR15_ex_rate_mid().doubleValue());
//    R15cell2.setCellStyle(numberStyle);
//} else {
//    R15cell2.setCellValue("");
//    R15cell2.setCellStyle(textStyle);
//}
//
//// R15 Col D
//Cell R15cell3 = row.createCell(3);
//if (record.getR15_ex_rate_sell() != null) {
//    R15cell3.setCellValue(record.getR15_ex_rate_sell().doubleValue());
//    R15cell3.setCellStyle(numberStyle);
//} else {
//    R15cell3.setCellValue("");
//    R15cell3.setCellStyle(textStyle);
//}
//
//// R15 Col E
//Cell R15cell4 = row.createCell(4);
//if (record.getR15_current() != null) {
//    R15cell4.setCellValue(record.getR15_current().doubleValue());
//    R15cell4.setCellStyle(numberStyle);
//} else {
//    R15cell4.setCellValue("");
//    R15cell1.setCellStyle(textStyle);
//}
//
//// R15 Col F
//Cell R15cell5 = row.createCell(5);
//if (record.getR15_call() != null) {
//    R15cell5.setCellValue(record.getR15_call().doubleValue());
//    R15cell5.setCellStyle(numberStyle);
//} else {
//    R15cell5.setCellValue("");
//    R15cell5.setCellStyle(textStyle);
//}
//
//// R15 Col G
//Cell R15cell6 = row.createCell(6);
//if (record.getR15_savings() != null) {
//    R15cell6.setCellValue(record.getR15_savings().doubleValue());
//    R15cell6.setCellStyle(numberStyle);
//} else {
//    R15cell6.setCellValue("");
//    R15cell6.setCellStyle(textStyle);
//	
//}
//
//// R15 Col H
//Cell R15cell7 = row.createCell(7);
//if (record.getR15_notice_0to31()!= null) {
//    R15cell7.setCellValue(record.getR15_notice_0to31().doubleValue());
//    R15cell7.setCellStyle(numberStyle);
//} else {
//    R15cell7.setCellValue("");
//    R15cell7.setCellStyle(textStyle);
//	
//}
//
//// R15 Col I
//Cell R15cell8= row.createCell(8);
//if (record.getR15_notice_32to88() != null) {
//    R15cell8.setCellValue(record.getR15_notice_32to88().doubleValue());
//    R15cell8.setCellStyle(numberStyle);
//} else {
//    R15cell8.setCellValue("");
//    R15cell8.setCellStyle(textStyle);
//}
//
//// R15 Col J
//Cell R15cell9 = row.createCell(9);
//if (record.getR15_fix_depo_91_day_depo() != null) {
//    R15cell9.setCellValue(record.getR15_fix_depo_91_day_depo().doubleValue());
//    R15cell9.setCellStyle(numberStyle);
//} else {
//    R15cell9.setCellValue("");
//    R15cell9.setCellStyle(textStyle);
//}
//
//// R15 Col K
//Cell R15cell10 = row.createCell(10);
//if (record.getR15_fix_depo_1to2() != null) {
//    R15cell10.setCellValue(record.getR15_fix_depo_1to2().doubleValue());
//    R15cell10.setCellStyle(numberStyle);
//} else {
//    R15cell10.setCellValue("");
//    R15cell10.setCellStyle(textStyle);
//}
//
//// R15 Col L
//Cell R15cell11 = row.createCell(11);
//if (record.getR15_fix_depo_4to6() != null) {
//    R15cell11.setCellValue(record.getR15_fix_depo_4to6().doubleValue());
//    R15cell11.setCellStyle(numberStyle);
//} else {
//    R15cell11.setCellValue("");
//    R15cell11.setCellStyle(textStyle);
//}
//
//// R15 Col M
//Cell R15cell12 = row.createCell(12);
//if (record.getR15_fix_depo_7to12() != null) {
//    R15cell12.setCellValue(record.getR15_fix_depo_7to12().doubleValue());
//    R15cell12.setCellStyle(numberStyle);
//} else {
//    R15cell12.setCellValue("");
//    R15cell12.setCellStyle(textStyle);
//}
//
//// R15 Col N
//Cell R15cell13 = row.createCell(13);
//if (record.getR15_fix_depo_13to18()!= null) {
//    R15cell13.setCellValue(record.getR15_fix_depo_13to18().doubleValue());
//    R15cell13.setCellStyle(numberStyle);
//} else {
//    R15cell13.setCellValue("");
//    R15cell13.setCellStyle(textStyle);
//}
//
//// R15 Col O
//Cell R15cell14 = row.createCell(14);
//if (record.getR15_fix_depo_19to24()!= null) {
//    R15cell14.setCellValue(record.getR15_fix_depo_19to24().doubleValue());
//    R15cell14.setCellStyle(numberStyle);
//} else {
//    R15cell14.setCellValue("");
//    R15cell14.setCellStyle(textStyle);
//}
//
//// R15 Col P
//Cell R15cell15 = row.createCell(15);
//if (record.getR15_fix_depo_over24() != null) {
//    R15cell15.setCellValue(record.getR15_fix_depo_over24().doubleValue());
//    R15cell15.setCellStyle(numberStyle);
//} else {
//    R15cell15.setCellValue("");
//    R15cell15.setCellStyle(textStyle);
//}
//
//
//// R15 Col Q
//Cell R15cell16 = row.createCell(16);
//if (record.getR15_cer_of_depo() != null) {
//    R15cell16.setCellValue(record.getR15_cer_of_depo().doubleValue());
//    R15cell16.setCellStyle(numberStyle);
//} else {
//    R15cell16.setCellValue("");
//    R15cell16.setCellStyle(textStyle);
//}
//
//// R15 Col S
//Cell R15cell18 = row.createCell(18);
//if (record.getR15_pula_equivalent() != null) {
//    R15cell18.setCellValue(record.getR15_pula_equivalent().doubleValue());
//    R15cell18.setCellStyle(numberStyle);
//} else {
//    R15cell18.setCellValue("");
//    R15cell18.setCellStyle(textStyle);
//}
// 
//
//// R15 Col T
//Cell R15cell19 = row.createCell(19);
//if (record.getR15_avg_pula_equivalent() != null) {
//    R15cell19.setCellValue(record.getR15_avg_pula_equivalent().doubleValue());
//    R15cell19.setCellStyle(numberStyle);
//} else {
//    R15cell19.setCellValue("");
//    R15cell19.setCellStyle(textStyle);
//}
//
//// R16 Col B
//row = sheet.getRow(15);
//Cell R16cell1 = row.createCell(1);
//if (record.getR16_ex_rate_buy() != null) {
//    R16cell1.setCellValue(record.getR16_ex_rate_buy().doubleValue());
//    R16cell1.setCellStyle(numberStyle);
//} else {
//    R16cell1.setCellValue("");
//    R16cell1.setCellStyle(textStyle);
//}
//
//// R16 Col C
//Cell R16cell2 = row.createCell(2);
//if (record.getR16_ex_rate_mid() != null) {
//    R16cell2.setCellValue(record.getR16_ex_rate_mid().doubleValue());
//    R16cell2.setCellStyle(numberStyle);
//} else {
//    R16cell2.setCellValue("");
//    R16cell2.setCellStyle(textStyle);
//}
//
//// R16 Col D
//Cell R16cell3 = row.createCell(3);
//if (record.getR16_ex_rate_sell() != null) {
//    R16cell3.setCellValue(record.getR16_ex_rate_sell().doubleValue());
//    R16cell3.setCellStyle(numberStyle);
//} else {
//    R16cell3.setCellValue("");
//    R16cell3.setCellStyle(textStyle);
//}
//
//// R16 Col E
//Cell R16cell4 = row.createCell(4);
//if (record.getR16_current() != null) {
//    R16cell4.setCellValue(record.getR16_current().doubleValue());
//    R16cell4.setCellStyle(numberStyle);
//} else {
//    R16cell4.setCellValue("");
//    R16cell1.setCellStyle(textStyle);
//}
//
//// R16 Col F
//Cell R16cell5 = row.createCell(5);
//if (record.getR16_call() != null) {
//    R16cell5.setCellValue(record.getR16_call().doubleValue());
//    R16cell5.setCellStyle(numberStyle);
//} else {
//    R16cell5.setCellValue("");
//    R16cell5.setCellStyle(textStyle);
//}
//
//// R16 Col G
//Cell R16cell6 = row.createCell(6);
//if (record.getR16_savings() != null) {
//    R16cell6.setCellValue(record.getR16_savings().doubleValue());
//    R16cell6.setCellStyle(numberStyle);
//} else {
//    R16cell6.setCellValue("");
//    R16cell6.setCellStyle(textStyle);
//	
//}
//
//// R16 Col H
//Cell R16cell7 = row.createCell(7);
//if (record.getR16_notice_0to31()!= null) {
//    R16cell7.setCellValue(record.getR16_notice_0to31().doubleValue());
//    R16cell7.setCellStyle(numberStyle);
//} else {
//    R16cell7.setCellValue("");
//    R16cell7.setCellStyle(textStyle);
//	
//}
//
//// R16 Col I
//Cell R16cell8= row.createCell(8);
//if (record.getR16_notice_32to88() != null) {
//    R16cell8.setCellValue(record.getR16_notice_32to88().doubleValue());
//    R16cell8.setCellStyle(numberStyle);
//} else {
//    R16cell8.setCellValue("");
//    R16cell8.setCellStyle(textStyle);
//}
//
//// R16 Col J
//Cell R16cell9 = row.createCell(9);
//if (record.getR16_fix_depo_91_day_depo() != null) {
//    R16cell9.setCellValue(record.getR16_fix_depo_91_day_depo().doubleValue());
//    R16cell9.setCellStyle(numberStyle);
//} else {
//    R16cell9.setCellValue("");
//    R16cell9.setCellStyle(textStyle);
//}
//
//// R16 Col K
//Cell R16cell10 = row.createCell(10);
//if (record.getR16_fix_depo_1to2() != null) {
//    R16cell10.setCellValue(record.getR16_fix_depo_1to2().doubleValue());
//    R16cell10.setCellStyle(numberStyle);
//} else {
//    R16cell10.setCellValue("");
//    R16cell10.setCellStyle(textStyle);
//}
//
//// R16 Col L
//Cell R16cell11 = row.createCell(11);
//if (record.getR16_fix_depo_4to6() != null) {
//    R16cell11.setCellValue(record.getR16_fix_depo_4to6().doubleValue());
//    R16cell11.setCellStyle(numberStyle);
//} else {
//    R16cell11.setCellValue("");
//    R16cell11.setCellStyle(textStyle);
//}
//
//// R16 Col M
//Cell R16cell12 = row.createCell(12);
//if (record.getR16_fix_depo_7to12() != null) {
//    R16cell12.setCellValue(record.getR16_fix_depo_7to12().doubleValue());
//    R16cell12.setCellStyle(numberStyle);
//} else {
//    R16cell12.setCellValue("");
//    R16cell12.setCellStyle(textStyle);
//}
//
//// R16 Col N
//Cell R16cell13 = row.createCell(13);
//if (record.getR16_fix_depo_13to18()!= null) {
//    R16cell13.setCellValue(record.getR16_fix_depo_13to18().doubleValue());
//    R16cell13.setCellStyle(numberStyle);
//} else {
//    R16cell13.setCellValue("");
//    R16cell13.setCellStyle(textStyle);
//}
//
//// R16 Col O
//Cell R16cell14 = row.createCell(14);
//if (record.getR16_fix_depo_19to24()!= null) {
//    R16cell14.setCellValue(record.getR16_fix_depo_19to24().doubleValue());
//    R16cell14.setCellStyle(numberStyle);
//} else {
//    R16cell14.setCellValue("");
//    R16cell14.setCellStyle(textStyle);
//}
//
//// R16 Col P
//Cell R16cell15 = row.createCell(15);
//if (record.getR16_fix_depo_over24() != null) {
//    R16cell15.setCellValue(record.getR16_fix_depo_over24().doubleValue());
//    R16cell15.setCellStyle(numberStyle);
//} else {
//    R16cell15.setCellValue("");
//    R16cell15.setCellStyle(textStyle);
//
//}
//
//// R16 Col Q
//Cell R16cell16 = row.createCell(16);
//if (record.getR16_cer_of_depo() != null) {
//    R16cell16.setCellValue(record.getR16_cer_of_depo().doubleValue());
//    R16cell16.setCellStyle(numberStyle);
//} else {
//    R16cell16.setCellValue("");
//    R16cell16.setCellStyle(textStyle);
//}
//
//// R16 Col S
//Cell R16cell18 = row.createCell(18);
//if (record.getR16_pula_equivalent() != null) {
//    R16cell18.setCellValue(record.getR16_pula_equivalent().doubleValue());
//    R16cell18.setCellStyle(numberStyle);
//} else {
//    R16cell18.setCellValue("");
//    R16cell18.setCellStyle(textStyle);
//}
// 
//
//// R16 Col T
//Cell R16cell19 = row.createCell(19);
//if (record.getR16_avg_pula_equivalent() != null) {
//    R16cell19.setCellValue(record.getR16_avg_pula_equivalent().doubleValue());
//    R16cell19.setCellStyle(numberStyle);
//} else {
//    R16cell19.setCellValue("");
//    R16cell19.setCellStyle(textStyle);
//}
//
//// R17 Col B
//row = sheet.getRow(16);
//Cell R17cell1 = row.createCell(1);
//if (record.getR17_ex_rate_buy() != null) {
//    R17cell1.setCellValue(record.getR17_ex_rate_buy().doubleValue());
//    R17cell1.setCellStyle(numberStyle);
//} else {
//    R17cell1.setCellValue("");
//    R17cell1.setCellStyle(textStyle);
//}
//
//// R17 Col C
//Cell R17cell2 = row.createCell(2);
//if (record.getR17_ex_rate_mid() != null) {
//    R17cell2.setCellValue(record.getR17_ex_rate_mid().doubleValue());
//    R17cell2.setCellStyle(numberStyle);
//} else {
//    R17cell2.setCellValue("");
//    R17cell2.setCellStyle(textStyle);
//}
//
//// R17 Col D
//Cell R17cell3 = row.createCell(3);
//if (record.getR17_ex_rate_sell() != null) {
//    R17cell3.setCellValue(record.getR17_ex_rate_sell().doubleValue());
//    R17cell3.setCellStyle(numberStyle);
//} else {
//    R17cell3.setCellValue("");
//    R17cell3.setCellStyle(textStyle);
//}
//
//// R18 Col B
//row = sheet.getRow(17);
//Cell R18cell1 = row.createCell(1);
//if (record.getR18_ex_rate_buy() != null) {
//    R18cell1.setCellValue(record.getR18_ex_rate_buy().doubleValue());
//    R18cell1.setCellStyle(numberStyle);
//} else {
//    R18cell1.setCellValue("");
//    R18cell1.setCellStyle(textStyle);
//}
//
//// R18 Col C
//Cell R18cell2 = row.createCell(2);
//if (record.getR18_ex_rate_mid() != null) {
//    R18cell2.setCellValue(record.getR18_ex_rate_mid().doubleValue());
//    R18cell2.setCellStyle(numberStyle);
//} else {
//    R18cell2.setCellValue("");
//    R18cell2.setCellStyle(textStyle);
//}
//
//// R18 Col D
//Cell R18cell3 = row.createCell(3);
//if (record.getR18_ex_rate_sell() != null) {
//    R18cell3.setCellValue(record.getR18_ex_rate_sell().doubleValue());
//    R18cell3.setCellStyle(numberStyle);
//} else {
//    R18cell3.setCellValue("");
//    R18cell3.setCellStyle(textStyle);
//}
//
//// R18 Col E
//Cell R18cell4 = row.createCell(4);
//if (record.getR18_current() != null) {
//    R18cell4.setCellValue(record.getR18_current().doubleValue());
//    R18cell4.setCellStyle(numberStyle);
//} else {
//    R18cell4.setCellValue("");
//    R18cell1.setCellStyle(textStyle);
//}
//
//// R18 Col F
//Cell R18cell5 = row.createCell(5);
//if (record.getR18_call() != null) {
//    R18cell5.setCellValue(record.getR18_call().doubleValue());
//    R18cell5.setCellStyle(numberStyle);
//} else {
//    R18cell5.setCellValue("");
//    R18cell5.setCellStyle(textStyle);
//}
//
//// R18 Col G
//Cell R18cell6 = row.createCell(6);
//if (record.getR18_savings() != null) {
//    R18cell6.setCellValue(record.getR18_savings().doubleValue());
//    R18cell6.setCellStyle(numberStyle);
//} else {
//    R18cell6.setCellValue("");
//    R18cell6.setCellStyle(textStyle);
//	
//}
//
//// R18 Col H
//Cell R18cell7 = row.createCell(7);
//if (record.getR18_notice_0to31()!= null) {
//    R18cell7.setCellValue(record.getR18_notice_0to31().doubleValue());
//    R18cell7.setCellStyle(numberStyle);
//} else {
//    R18cell7.setCellValue("");
//    R18cell7.setCellStyle(textStyle);
//	
//}
//
//// R18 Col I
//Cell R18cell8= row.createCell(8);
//if (record.getR18_notice_32to88() != null) {
//    R18cell8.setCellValue(record.getR18_notice_32to88().doubleValue());
//    R18cell8.setCellStyle(numberStyle);
//} else {
//    R18cell8.setCellValue("");
//    R18cell8.setCellStyle(textStyle);
//}
//
//// R18 Col J
//Cell R18cell9 = row.createCell(9);
//if (record.getR18_fix_depo_91_day_depo() != null) {
//    R18cell9.setCellValue(record.getR18_fix_depo_91_day_depo().doubleValue());
//    R18cell9.setCellStyle(numberStyle);
//} else {
//    R18cell9.setCellValue("");
//    R18cell9.setCellStyle(textStyle);
//}
//
//// R18 Col K
//Cell R18cell10 = row.createCell(10);
//if (record.getR18_fix_depo_1to2() != null) {
//    R18cell10.setCellValue(record.getR18_fix_depo_1to2().doubleValue());
//    R18cell10.setCellStyle(numberStyle);
//} else {
//    R18cell10.setCellValue("");
//    R18cell10.setCellStyle(textStyle);
//}
//
//// R18 Col L
//Cell R18cell11 = row.createCell(11);
//if (record.getR18_fix_depo_4to6() != null) {
//    R18cell11.setCellValue(record.getR18_fix_depo_4to6().doubleValue());
//    R18cell11.setCellStyle(numberStyle);
//} else {
//    R18cell11.setCellValue("");
//    R18cell11.setCellStyle(textStyle);
//}
//
//// R18 Col M
//Cell R18cell12 = row.createCell(12);
//if (record.getR18_fix_depo_7to12() != null) {
//    R18cell12.setCellValue(record.getR18_fix_depo_7to12().doubleValue());
//    R18cell12.setCellStyle(numberStyle);
//} else {
//    R18cell12.setCellValue("");
//    R18cell12.setCellStyle(textStyle);
//}
//
//// R18 Col N
//Cell R18cell13 = row.createCell(13);
//if (record.getR18_fix_depo_13to18()!= null) {
//    R18cell13.setCellValue(record.getR18_fix_depo_13to18().doubleValue());
//    R18cell13.setCellStyle(numberStyle);
//} else {
//    R18cell13.setCellValue("");
//    R18cell13.setCellStyle(textStyle);
//}
//
//// R18 Col O
//Cell R18cell14 = row.createCell(14);
//if (record.getR18_fix_depo_19to24()!= null) {
//    R18cell14.setCellValue(record.getR18_fix_depo_19to24().doubleValue());
//    R18cell14.setCellStyle(numberStyle);
//} else {
//    R18cell14.setCellValue("");
//    R18cell14.setCellStyle(textStyle);
//}
//
//// R18 Col P
//Cell R18cell15 = row.createCell(15);
//if (record.getR18_fix_depo_over24() != null) {
//    R18cell15.setCellValue(record.getR18_fix_depo_over24().doubleValue());
//    R18cell15.setCellStyle(numberStyle);
//} else {
//    R18cell15.setCellValue("");
//    R18cell15.setCellStyle(textStyle);
//}
//
//
//// R18 Col Q
//Cell R18cell16 = row.createCell(16);
//if (record.getR18_cer_of_depo() != null) {
//    R18cell16.setCellValue(record.getR18_cer_of_depo().doubleValue());
//    R18cell16.setCellStyle(numberStyle);
//} else {
//    R18cell16.setCellValue("");
//    R18cell16.setCellStyle(textStyle);
//}
//// R18 Col R
//Cell R18cell17 = row.createCell(17);
//if (record.getR18_total() != null) {
//    R18cell17.setCellValue(record.getR18_total().doubleValue());
//    R18cell17.setCellStyle(numberStyle);
//} else {
//    R18cell17.setCellValue("");
//    R18cell17.setCellStyle(textStyle);
//}
//// R18 Col S
//Cell R18cell18 = row.createCell(18);
//if (record.getR18_pula_equivalent() != null) {
//    R18cell18.setCellValue(record.getR18_pula_equivalent().doubleValue());
//    R18cell18.setCellStyle(numberStyle);
//} else {
//    R18cell18.setCellValue("");
//    R18cell18.setCellStyle(textStyle);
//}
//// R18 Col S
//Cell R18cell19 = row.createCell(19);
//if (record.getR18_avg_pula_equivalent() != null) {
//    R18cell19.setCellValue(record.getR18_avg_pula_equivalent().doubleValue());
//    R18cell18.setCellStyle(numberStyle);
//} else {
//    R18cell19.setCellValue("");
//    R18cell19.setCellStyle(textStyle);
//}
//
////Entity 2
//// R28 Col B
//row = sheet.getRow(27);
//Cell R28cell1 = row.createCell(1);
//if (record.getR28_import() != null) {
//    R28cell1.setCellValue(record.getR28_import().doubleValue());
//    R28cell1.setCellStyle(numberStyle);
//} else {
//    R28cell1.setCellValue("");
//    R28cell1.setCellStyle(textStyle);
//}
//// R28 Col C
//Cell R28cell2 = row.createCell(2);
//if (record.getR28_investment () != null) {
//    R28cell2.setCellValue(record.getR28_investment().doubleValue());
//    R28cell2.setCellStyle(numberStyle);
//} else {
//    R28cell2.setCellValue("");
//    R28cell2.setCellStyle(textStyle);
//}
//// R28 Col D
//Cell R28cell3 = row.createCell(3);
//if (record.getR28_other () != null) {
//    R28cell3.setCellValue(record.getR28_other().doubleValue());
//    R28cell3.setCellStyle(numberStyle);
//} else {
//    R28cell3.setCellValue("");
//    R28cell3.setCellStyle(textStyle);
//}
//// R29 Col B
//row = sheet.getRow(28);
//Cell R29cell1 = row.createCell(1);
//if (record.getR29_import() != null) {
//    R29cell1.setCellValue(record.getR29_import().doubleValue());
//    R29cell1.setCellStyle(numberStyle);
//} else {
//    R29cell1.setCellValue("");
//    R29cell1.setCellStyle(textStyle);
//}
//// R29 Col C
//Cell R29cell2 = row.createCell(2);
//if (record.getR29_investment () != null) {
//    R29cell2.setCellValue(record.getR29_investment().doubleValue());
//    R29cell2.setCellStyle(numberStyle);
//} else {
//    R29cell2.setCellValue("");
//    R29cell2.setCellStyle(textStyle);
//}
//// R29 Col D
//Cell R29cell3 = row.createCell(3);
//if (record.getR29_other () != null) {
//    R29cell3.setCellValue(record.getR29_other().doubleValue());
//    R29cell3.setCellStyle(numberStyle);
//} else {
//    R29cell3.setCellValue("");
//    R29cell3.setCellStyle(textStyle);
//}
//
//// R30 Col B
//row = sheet.getRow(29);
//Cell R30cell1 = row.createCell(1);
//if (record.getR30_import() != null) {
//    R30cell1.setCellValue(record.getR30_import().doubleValue());
//    R30cell1.setCellStyle(numberStyle);
//} else {
//    R30cell1.setCellValue("");
//    R30cell1.setCellStyle(textStyle);
//}
//// R30 Col C
//Cell R30cell2 = row.createCell(2);
//if (record.getR30_investment () != null) {
//    R30cell2.setCellValue(record.getR30_investment().doubleValue());
//    R30cell2.setCellStyle(numberStyle);
//} else {
//    R30cell2.setCellValue("");
//    R30cell2.setCellStyle(textStyle);
//}
//// R30 Col D
//Cell R30cell3 = row.createCell(3);
//if (record.getR30_other () != null) {
//    R30cell3.setCellValue(record.getR30_other().doubleValue());
//    R30cell3.setCellStyle(numberStyle);
//} else {
//    R30cell3.setCellValue("");
//    R30cell3.setCellStyle(textStyle);
//}
//// R31 Col B
//row = sheet.getRow(30);
//Cell R31cell1 = row.createCell(1);
//if (record.getR31_import() != null) {
//    R31cell1.setCellValue(record.getR31_import().doubleValue());
//    R31cell1.setCellStyle(numberStyle);
//} else {
//    R31cell1.setCellValue("");
//    R31cell1.setCellStyle(textStyle);
//}
//// R31 Col C
//Cell R31cell2 = row.createCell(2);
//if (record.getR31_investment () != null) {
//    R31cell2.setCellValue(record.getR31_investment().doubleValue());
//    R31cell2.setCellStyle(numberStyle);
//} else {
//    R31cell2.setCellValue("");
//    R31cell2.setCellStyle(textStyle);
//}
//// R31 Col D
//Cell R31cell3 = row.createCell(3);
//if (record.getR31_other () != null) {
//    R31cell3.setCellValue(record.getR31_other().doubleValue());
//    R31cell3.setCellStyle(numberStyle);
//} else {
//    R31cell3.setCellValue("");
//    R31cell3.setCellStyle(textStyle);
//}
//// R32 Col B
//row = sheet.getRow(31);
//Cell R32cell1 = row.createCell(1);
//if (record.getR32_import() != null) {
//    R32cell1.setCellValue(record.getR32_import().doubleValue());
//    R32cell1.setCellStyle(numberStyle);
//} else {
//    R32cell1.setCellValue("");
//    R32cell1.setCellStyle(textStyle);
//}
//// R32 Col C
//Cell R32cell2 = row.createCell(2);
//if (record.getR32_investment () != null) {
//    R32cell2.setCellValue(record.getR32_investment().doubleValue());
//    R32cell2.setCellStyle(numberStyle);
//} else {
//    R32cell2.setCellValue("");
//    R32cell2.setCellStyle(textStyle);
//}
//// R32 Col D
//Cell R32cell3 = row.createCell(3);
//if (record.getR32_other () != null) {
//    R32cell3.setCellValue(record.getR32_other().doubleValue());
//    R32cell3.setCellStyle(numberStyle);
//} else {
//    R32cell3.setCellValue("");
//    R32cell3.setCellStyle(textStyle);
//}
//// R33 Col B
//row = sheet.getRow(32);
//Cell R33cell1 = row.createCell(1);
//if (record.getR33_import() != null) {
//    R33cell1.setCellValue(record.getR33_import().doubleValue());
//    R33cell1.setCellStyle(numberStyle);
//} else {
//    R33cell1.setCellValue("");
//    R33cell1.setCellStyle(textStyle);
//}
//// R33 Col C
//Cell R33cell2 = row.createCell(2);
//if (record.getR33_investment () != null) {
//    R33cell2.setCellValue(record.getR33_investment().doubleValue());
//    R33cell2.setCellStyle(numberStyle);
//} else {
//    R33cell2.setCellValue("");
//    R33cell2.setCellStyle(textStyle);
//}
//// R33 Col D
//Cell R33cell3 = row.createCell(3);
//if (record.getR33_other () != null) {
//    R33cell3.setCellValue(record.getR33_other().doubleValue());
//    R33cell3.setCellStyle(numberStyle);
//} else {
//    R33cell3.setCellValue("");
//    R33cell3.setCellStyle(textStyle);
//}
//
////Entity 3
//
//row = sheet.getRow(27);
//Cell R28cell1e3 = row.createCell(8);
//if (record.getR28_residents () != null) {
//    R28cell1e3.setCellValue(record.getR28_residents().doubleValue());
//    R28cell1e3.setCellStyle(numberStyle);
//} else {
//    R28cell1e3.setCellValue("");
//    R28cell1e3.setCellStyle(textStyle);
//}
//// R28 Col C
//Cell R28cell2e3 = row.createCell(9);
//if (record.getR28_non_residents () != null) {
//    R28cell2e3.setCellValue(record.getR28_non_residents().doubleValue());
//    R28cell2e3.setCellStyle(numberStyle);
//} else {
//    R28cell2e3.setCellValue("");
//    R28cell2e3.setCellStyle(textStyle);
//}
//// R29 Col B
//row = sheet.getRow(28);
//Cell R29cell1e3 = row.createCell(8);
//if (record.getR29_residents () != null) {
//    R29cell1e3.setCellValue(record.getR29_residents().doubleValue());
//    R29cell1e3.setCellStyle(numberStyle);
//} else {
//    R29cell1e3.setCellValue("");
//    R29cell1e3.setCellStyle(textStyle);
//}
//// R29 Col C
//Cell R29cell2e3 = row.createCell(9);
//if (record.getR29_non_residents () != null) {
//    R29cell2e3.setCellValue(record.getR29_non_residents().doubleValue());
//    R29cell2e3.setCellStyle(numberStyle);
//} else {
//    R29cell2e3.setCellValue("");
//    R29cell2e3.setCellStyle(textStyle);
//}
//// R30 Col B
//row = sheet.getRow(29);
//Cell R30cell1e3 = row.createCell(8);
//if (record.getR30_residents () != null) {
//    R30cell1e3.setCellValue(record.getR30_residents().doubleValue());
//    R30cell1e3.setCellStyle(numberStyle);
//} else {
//    R30cell1e3.setCellValue("");
//    R30cell1e3.setCellStyle(textStyle);
//}
//// R30 Col C
//Cell R30cell2e3 = row.createCell(9);
//if (record.getR30_non_residents () != null) {
//    R30cell2e3.setCellValue(record.getR30_non_residents().doubleValue());
//    R30cell2e3.setCellStyle(numberStyle);
//} else {
//    R30cell2e3.setCellValue("");
//    R30cell2e3.setCellStyle(textStyle);
//}
//// R31 Col B
//row = sheet.getRow(30);
//Cell R31cell1e3 = row.createCell(8);
//if (record.getR31_residents () != null) {
//    R31cell1e3.setCellValue(record.getR31_residents().doubleValue());
//    R31cell1e3.setCellStyle(numberStyle);
//} else {
//    R31cell1e3.setCellValue("");
//    R31cell1e3.setCellStyle(textStyle);
//}
//// R31 Col C
//Cell R31cell2e3 = row.createCell(9);
//if (record.getR31_non_residents () != null) {
//    R31cell2e3.setCellValue(record.getR31_non_residents().doubleValue());
//    R31cell2e3.setCellStyle(numberStyle);
//} else {
//    R31cell2e3.setCellValue("");
//    R31cell2e3.setCellStyle(textStyle);
//}
//
//// R32 Col B
//row = sheet.getRow(31);
//Cell R32cell1e3 = row.createCell(8);
//if (record.getR32_residents () != null) {
//    R32cell1e3.setCellValue(record.getR32_residents().doubleValue());
//    R32cell1e3.setCellStyle(numberStyle);
//} else {
//    R32cell1e3.setCellValue("");
//    R32cell1e3.setCellStyle(textStyle);
//}
//// R32 Col C
//Cell R32cell2e3 = row.createCell(9);
//if (record.getR32_non_residents () != null) {
//    R32cell2e3.setCellValue(record.getR32_non_residents().doubleValue());
//    R32cell2e3.setCellStyle(numberStyle);
//} else {
//    R32cell2e3.setCellValue("");
//    R32cell2e3.setCellStyle(textStyle);
//}
//// R33 Col B
//row = sheet.getRow(32);
//Cell R33cell1e3 = row.createCell(8);
//if (record.getR33_residents () != null) {
//    R33cell1e3.setCellValue(record.getR33_residents().doubleValue());
//    R33cell1e3.setCellStyle(numberStyle);
//} else {
//    R33cell1e3.setCellValue("");
//    R33cell1e3.setCellStyle(textStyle);
//}
//// R33 Col C
//Cell R33cell2e3 = row.createCell(9);
//if (record.getR33_non_residents () != null) {
//    R33cell2e3.setCellValue(record.getR33_non_residents().doubleValue());
//    R33cell2e3.setCellStyle(numberStyle);
//} else {
//    R33cell2e3.setCellValue("");
//    R33cell2e3.setCellStyle(textStyle);
//}	
//
//
//
//				}
//				workbook.setForceFormulaRecalculation(true);
//			} else {
//
//			}
//
//			// Write the final workbook content to the in-memory stream.
//			workbook.write(out);
//
//			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
//
//			return out.toByteArray();
//		}
//	}

	// ------------------------------
	// SAVE OR UPDATE SUMMARY ENTITY
	// ------------------------------
	private void saveSummaryEntity(M_DEP3_Summary_Entity entity) {
		String sql = "UPDATE BRRS_M_DEP3_SUMMARYTABLE SET r11_foreign_curr_acc_by_curr = ?, r11_ex_rate_buy = ?, r11_ex_rate_mid = ?, r11_ex_rate_sell = ?, r11_current = ?, r11_call = ?, r11_savings = ?, r11_notice_0to31 = ?, r11_notice_32to88 = ?, r11_fix_depo_91_day_depo = ?, r11_fix_depo_1to2 = ?, r11_fix_depo_4to6 = ?, r11_fix_depo_7to12 = ?, r11_fix_depo_13to18 = ?, r11_fix_depo_19to24 = ?, r11_fix_depo_over24 = ?, r11_cer_of_depo = ?, r11_total = ?, r11_pula_equivalent = ?, r11_avg_pula_equivalent = ?, r12_foreign_curr_acc_by_curr = ?, r12_ex_rate_buy = ?, r12_ex_rate_mid = ?, r12_ex_rate_sell = ?, r12_current = ?, r12_call = ?, r12_savings = ?, r12_notice_0to31 = ?, r12_notice_32to88 = ?, r12_fix_depo_91_day_depo = ?, r12_fix_depo_1to2 = ?, r12_fix_depo_4to6 = ?, r12_fix_depo_7to12 = ?, r12_fix_depo_13to18 = ?, r12_fix_depo_19to24 = ?, r12_fix_depo_over24 = ?, r12_cer_of_depo = ?, r12_total = ?, r12_pula_equivalent = ?, r12_avg_pula_equivalent = ?, r13_foreign_curr_acc_by_curr = ?, r13_ex_rate_buy = ?, r13_ex_rate_mid = ?, r13_ex_rate_sell = ?, r13_current = ?, r13_call = ?, r13_savings = ?, r13_notice_0to31 = ?, r13_notice_32to88 = ?, r13_fix_depo_91_day_depo = ?, r13_fix_depo_1to2 = ?, r13_fix_depo_4to6 = ?, r13_fix_depo_7to12 = ?, r13_fix_depo_13to18 = ?, r13_fix_depo_19to24 = ?, r13_fix_depo_over24 = ?, r13_cer_of_depo = ?, r13_total = ?, r13_pula_equivalent = ?, r13_avg_pula_equivalent = ?, r14_foreign_curr_acc_by_curr = ?, r14_ex_rate_buy = ?, r14_ex_rate_mid = ?, r14_ex_rate_sell = ?, r14_current = ?, r14_call = ?, r14_savings = ?, r14_notice_0to31 = ?, r14_notice_32to88 = ?, r14_fix_depo_91_day_depo = ?, r14_fix_depo_1to2 = ?, r14_fix_depo_4to6 = ?, r14_fix_depo_7to12 = ?, r14_fix_depo_13to18 = ?, r14_fix_depo_19to24 = ?, r14_fix_depo_over24 = ?, r14_cer_of_depo = ?, r14_total = ?, r14_pula_equivalent = ?, r14_avg_pula_equivalent = ?, r15_foreign_curr_acc_by_curr = ?, r15_ex_rate_buy = ?, r15_ex_rate_mid = ?, r15_ex_rate_sell = ?, r15_current = ?, r15_call = ?, r15_savings = ?, r15_notice_0to31 = ?, r15_notice_32to88 = ?, r15_fix_depo_91_day_depo = ?, r15_fix_depo_1to2 = ?, r15_fix_depo_4to6 = ?, r15_fix_depo_7to12 = ?, r15_fix_depo_13to18 = ?, r15_fix_depo_19to24 = ?, r15_fix_depo_over24 = ?, r15_cer_of_depo = ?, r15_total = ?, r15_pula_equivalent = ?, r15_avg_pula_equivalent = ?, r16_foreign_curr_acc_by_curr = ?, r16_ex_rate_buy = ?, r16_ex_rate_mid = ?, r16_ex_rate_sell = ?, r16_current = ?, r16_call = ?, r16_savings = ?, r16_notice_0to31 = ?, r16_notice_32to88 = ?, r16_fix_depo_91_day_depo = ?, r16_fix_depo_1to2 = ?, r16_fix_depo_4to6 = ?, r16_fix_depo_7to12 = ?, r16_fix_depo_13to18 = ?, r16_fix_depo_19to24 = ?, r16_fix_depo_over24 = ?, r16_cer_of_depo = ?, r16_total = ?, r16_pula_equivalent = ?, r16_avg_pula_equivalent = ?, r17_foreign_curr_acc_by_curr = ?, r17_ex_rate_buy = ?, r17_ex_rate_mid = ?, r17_ex_rate_sell = ?, r17_current = ?, r17_call = ?, r17_savings = ?, r17_notice_0to31 = ?, r17_notice_32to88 = ?, r17_fix_depo_91_day_depo = ?, r17_fix_depo_1to2 = ?, r17_fix_depo_4to6 = ?, r17_fix_depo_7to12 = ?, r17_fix_depo_13to18 = ?, r17_fix_depo_19to24 = ?, r17_fix_depo_over24 = ?, r17_cer_of_depo = ?, r17_total = ?, r17_pula_equivalent = ?, r17_avg_pula_equivalent = ?, r18_foreign_curr_acc_by_curr = ?, r18_ex_rate_buy = ?, r18_ex_rate_mid = ?, r18_ex_rate_sell = ?, r18_current = ?, r18_call = ?, r18_savings = ?, r18_notice_0to31 = ?, r18_notice_32to88 = ?, r18_fix_depo_91_day_depo = ?, r18_fix_depo_1to2 = ?, r18_fix_depo_4to6 = ?, r18_fix_depo_7to12 = ?, r18_fix_depo_13to18 = ?, r18_fix_depo_19to24 = ?, r18_fix_depo_over24 = ?, r18_cer_of_depo = ?, r18_total = ?, r18_pula_equivalent = ?, r18_avg_pula_equivalent = ?, r28_import = ?, r28_investment = ?, r28_other = ?, r29_import = ?, r29_investment = ?, r29_other = ?, r30_import = ?, r30_investment = ?, r30_other = ?, r31_import = ?, r31_investment = ?, r31_other = ?, r32_import = ?, r32_investment = ?, r32_other = ?, r33_import = ?, r33_investment = ?, r33_other = ?, r34_import = ?, r34_investment = ?, r34_other = ?, r28_residents = ?, r28_non_residents = ?, r29_residents = ?, r29_non_residents = ?, r30_residents = ?, r30_non_residents = ?, r31_residents = ?, r31_non_residents = ?, r32_residents = ?, r32_non_residents = ?, r33_residents = ?, r33_non_residents = ?, r34_residents = ?, r34_non_residents = ?, REPORT_VERSION = ?, report_frequency = ?, report_code = ?, report_desc = ?, entity_flg = ?, modify_flg = ?, del_flg = ? WHERE REPORT_DATE = ?";
		jdbcTemplate.update(sql, entity.getR11_foreign_curr_acc_by_curr(), entity.getR11_ex_rate_buy(),
				entity.getR11_ex_rate_mid(), entity.getR11_ex_rate_sell(), entity.getR11_current(),
				entity.getR11_call(), entity.getR11_savings(), entity.getR11_notice_0to31(),
				entity.getR11_notice_32to88(), entity.getR11_fix_depo_91_day_depo(), entity.getR11_fix_depo_1to2(),
				entity.getR11_fix_depo_4to6(), entity.getR11_fix_depo_7to12(), entity.getR11_fix_depo_13to18(),
				entity.getR11_fix_depo_19to24(), entity.getR11_fix_depo_over24(), entity.getR11_cer_of_depo(),
				entity.getR11_total(), entity.getR11_pula_equivalent(), entity.getR11_avg_pula_equivalent(),
				entity.getR12_foreign_curr_acc_by_curr(), entity.getR12_ex_rate_buy(), entity.getR12_ex_rate_mid(),
				entity.getR12_ex_rate_sell(), entity.getR12_current(), entity.getR12_call(), entity.getR12_savings(),
				entity.getR12_notice_0to31(), entity.getR12_notice_32to88(), entity.getR12_fix_depo_91_day_depo(),
				entity.getR12_fix_depo_1to2(), entity.getR12_fix_depo_4to6(), entity.getR12_fix_depo_7to12(),
				entity.getR12_fix_depo_13to18(), entity.getR12_fix_depo_19to24(), entity.getR12_fix_depo_over24(),
				entity.getR12_cer_of_depo(), entity.getR12_total(), entity.getR12_pula_equivalent(),
				entity.getR12_avg_pula_equivalent(), entity.getR13_foreign_curr_acc_by_curr(),
				entity.getR13_ex_rate_buy(), entity.getR13_ex_rate_mid(), entity.getR13_ex_rate_sell(),
				entity.getR13_current(), entity.getR13_call(), entity.getR13_savings(), entity.getR13_notice_0to31(),
				entity.getR13_notice_32to88(), entity.getR13_fix_depo_91_day_depo(), entity.getR13_fix_depo_1to2(),
				entity.getR13_fix_depo_4to6(), entity.getR13_fix_depo_7to12(), entity.getR13_fix_depo_13to18(),
				entity.getR13_fix_depo_19to24(), entity.getR13_fix_depo_over24(), entity.getR13_cer_of_depo(),
				entity.getR13_total(), entity.getR13_pula_equivalent(), entity.getR13_avg_pula_equivalent(),
				entity.getR14_foreign_curr_acc_by_curr(), entity.getR14_ex_rate_buy(), entity.getR14_ex_rate_mid(),
				entity.getR14_ex_rate_sell(), entity.getR14_current(), entity.getR14_call(), entity.getR14_savings(),
				entity.getR14_notice_0to31(), entity.getR14_notice_32to88(), entity.getR14_fix_depo_91_day_depo(),
				entity.getR14_fix_depo_1to2(), entity.getR14_fix_depo_4to6(), entity.getR14_fix_depo_7to12(),
				entity.getR14_fix_depo_13to18(), entity.getR14_fix_depo_19to24(), entity.getR14_fix_depo_over24(),
				entity.getR14_cer_of_depo(), entity.getR14_total(), entity.getR14_pula_equivalent(),
				entity.getR14_avg_pula_equivalent(), entity.getR15_foreign_curr_acc_by_curr(),
				entity.getR15_ex_rate_buy(), entity.getR15_ex_rate_mid(), entity.getR15_ex_rate_sell(),
				entity.getR15_current(), entity.getR15_call(), entity.getR15_savings(), entity.getR15_notice_0to31(),
				entity.getR15_notice_32to88(), entity.getR15_fix_depo_91_day_depo(), entity.getR15_fix_depo_1to2(),
				entity.getR15_fix_depo_4to6(), entity.getR15_fix_depo_7to12(), entity.getR15_fix_depo_13to18(),
				entity.getR15_fix_depo_19to24(), entity.getR15_fix_depo_over24(), entity.getR15_cer_of_depo(),
				entity.getR15_total(), entity.getR15_pula_equivalent(), entity.getR15_avg_pula_equivalent(),
				entity.getR16_foreign_curr_acc_by_curr(), entity.getR16_ex_rate_buy(), entity.getR16_ex_rate_mid(),
				entity.getR16_ex_rate_sell(), entity.getR16_current(), entity.getR16_call(), entity.getR16_savings(),
				entity.getR16_notice_0to31(), entity.getR16_notice_32to88(), entity.getR16_fix_depo_91_day_depo(),
				entity.getR16_fix_depo_1to2(), entity.getR16_fix_depo_4to6(), entity.getR16_fix_depo_7to12(),
				entity.getR16_fix_depo_13to18(), entity.getR16_fix_depo_19to24(), entity.getR16_fix_depo_over24(),
				entity.getR16_cer_of_depo(), entity.getR16_total(), entity.getR16_pula_equivalent(),
				entity.getR16_avg_pula_equivalent(), entity.getR17_foreign_curr_acc_by_curr(),
				entity.getR17_ex_rate_buy(), entity.getR17_ex_rate_mid(), entity.getR17_ex_rate_sell(),
				entity.getR17_current(), entity.getR17_call(), entity.getR17_savings(), entity.getR17_notice_0to31(),
				entity.getR17_notice_32to88(), entity.getR17_fix_depo_91_day_depo(), entity.getR17_fix_depo_1to2(),
				entity.getR17_fix_depo_4to6(), entity.getR17_fix_depo_7to12(), entity.getR17_fix_depo_13to18(),
				entity.getR17_fix_depo_19to24(), entity.getR17_fix_depo_over24(), entity.getR17_cer_of_depo(),
				entity.getR17_total(), entity.getR17_pula_equivalent(), entity.getR17_avg_pula_equivalent(),
				entity.getR18_foreign_curr_acc_by_curr(), entity.getR18_ex_rate_buy(), entity.getR18_ex_rate_mid(),
				entity.getR18_ex_rate_sell(), entity.getR18_current(), entity.getR18_call(), entity.getR18_savings(),
				entity.getR18_notice_0to31(), entity.getR18_notice_32to88(), entity.getR18_fix_depo_91_day_depo(),
				entity.getR18_fix_depo_1to2(), entity.getR18_fix_depo_4to6(), entity.getR18_fix_depo_7to12(),
				entity.getR18_fix_depo_13to18(), entity.getR18_fix_depo_19to24(), entity.getR18_fix_depo_over24(),
				entity.getR18_cer_of_depo(), entity.getR18_total(), entity.getR18_pula_equivalent(),
				entity.getR18_avg_pula_equivalent(), entity.getR28_import(), entity.getR28_investment(),
				entity.getR28_other(), entity.getR29_import(), entity.getR29_investment(), entity.getR29_other(),
				entity.getR30_import(), entity.getR30_investment(), entity.getR30_other(), entity.getR31_import(),
				entity.getR31_investment(), entity.getR31_other(), entity.getR32_import(), entity.getR32_investment(),
				entity.getR32_other(), entity.getR33_import(), entity.getR33_investment(), entity.getR33_other(),
				entity.getR34_import(), entity.getR34_investment(), entity.getR34_other(), entity.getR28_residents(),
				entity.getR28_non_residents(), entity.getR29_residents(), entity.getR29_non_residents(),
				entity.getR30_residents(), entity.getR30_non_residents(), entity.getR31_residents(),
				entity.getR31_non_residents(), entity.getR32_residents(), entity.getR32_non_residents(),
				entity.getR33_residents(), entity.getR33_non_residents(), entity.getR34_residents(),
				entity.getR34_non_residents(), entity.getReportVersion(), entity.getReport_frequency(),
				entity.getReport_code(), entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(),
				entity.getDel_flg(), entity.getReportDate());
	}

	/* ENTITIES */

	// ------------------------------
	// COMPOSITE PRIMARY KEY FOR M_DEP3 ENTITIES
	// ------------------------------
	public static class M_DEP3_PK implements Serializable {

		private Date reportDate;
		private BigDecimal reportVersion;

		// default constructor
		public M_DEP3_PK() {
		}

		// parameterized constructor
		public M_DEP3_PK(Date reportDate, BigDecimal reportVersion) {
			this.reportDate = reportDate;
			this.reportVersion = reportVersion;
		}

		// equals and hashCode
		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof M_DEP3_PK))
				return false;
			M_DEP3_PK that = (M_DEP3_PK) o;
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
	// ENTITY: BRRS_M_DEP3_SUMMARYTABLE
	// ------------------------------
	public static class M_DEP3_Summary_Entity {

		private String r11_foreign_curr_acc_by_curr;
		private BigDecimal r11_ex_rate_buy;
		private BigDecimal r11_ex_rate_mid;
		private BigDecimal r11_ex_rate_sell;
		private BigDecimal r11_current;
		private BigDecimal r11_call;
		private BigDecimal r11_savings;
		private BigDecimal r11_notice_0to31;
		private BigDecimal r11_notice_32to88;
		private BigDecimal r11_fix_depo_91_day_depo;
		private BigDecimal r11_fix_depo_1to2;
		private BigDecimal r11_fix_depo_4to6;
		private BigDecimal r11_fix_depo_7to12;
		private BigDecimal r11_fix_depo_13to18;
		private BigDecimal r11_fix_depo_19to24;
		private BigDecimal r11_fix_depo_over24;
		private BigDecimal r11_cer_of_depo;
		private BigDecimal r11_total;
		private BigDecimal r11_pula_equivalent;
		private BigDecimal r11_avg_pula_equivalent;

		private String r12_foreign_curr_acc_by_curr;
		private BigDecimal r12_ex_rate_buy;
		private BigDecimal r12_ex_rate_mid;
		private BigDecimal r12_ex_rate_sell;
		private BigDecimal r12_current;
		private BigDecimal r12_call;
		private BigDecimal r12_savings;
		private BigDecimal r12_notice_0to31;
		private BigDecimal r12_notice_32to88;
		private BigDecimal r12_fix_depo_91_day_depo;
		private BigDecimal r12_fix_depo_1to2;
		private BigDecimal r12_fix_depo_4to6;
		private BigDecimal r12_fix_depo_7to12;
		private BigDecimal r12_fix_depo_13to18;
		private BigDecimal r12_fix_depo_19to24;
		private BigDecimal r12_fix_depo_over24;
		private BigDecimal r12_cer_of_depo;
		private BigDecimal r12_total;
		private BigDecimal r12_pula_equivalent;
		private BigDecimal r12_avg_pula_equivalent;

		private String r13_foreign_curr_acc_by_curr;
		private BigDecimal r13_ex_rate_buy;
		private BigDecimal r13_ex_rate_mid;
		private BigDecimal r13_ex_rate_sell;
		private BigDecimal r13_current;
		private BigDecimal r13_call;
		private BigDecimal r13_savings;
		private BigDecimal r13_notice_0to31;
		private BigDecimal r13_notice_32to88;
		private BigDecimal r13_fix_depo_91_day_depo;
		private BigDecimal r13_fix_depo_1to2;
		private BigDecimal r13_fix_depo_4to6;
		private BigDecimal r13_fix_depo_7to12;
		private BigDecimal r13_fix_depo_13to18;
		private BigDecimal r13_fix_depo_19to24;
		private BigDecimal r13_fix_depo_over24;
		private BigDecimal r13_cer_of_depo;
		private BigDecimal r13_total;
		private BigDecimal r13_pula_equivalent;
		private BigDecimal r13_avg_pula_equivalent;

		private String r14_foreign_curr_acc_by_curr;
		private BigDecimal r14_ex_rate_buy;
		private BigDecimal r14_ex_rate_mid;
		private BigDecimal r14_ex_rate_sell;
		private BigDecimal r14_current;
		private BigDecimal r14_call;
		private BigDecimal r14_savings;
		private BigDecimal r14_notice_0to31;
		private BigDecimal r14_notice_32to88;
		private BigDecimal r14_fix_depo_91_day_depo;
		private BigDecimal r14_fix_depo_1to2;
		private BigDecimal r14_fix_depo_4to6;
		private BigDecimal r14_fix_depo_7to12;
		private BigDecimal r14_fix_depo_13to18;
		private BigDecimal r14_fix_depo_19to24;
		private BigDecimal r14_fix_depo_over24;
		private BigDecimal r14_cer_of_depo;
		private BigDecimal r14_total;
		private BigDecimal r14_pula_equivalent;
		private BigDecimal r14_avg_pula_equivalent;

		private String r15_foreign_curr_acc_by_curr;
		private BigDecimal r15_ex_rate_buy;
		private BigDecimal r15_ex_rate_mid;
		private BigDecimal r15_ex_rate_sell;
		private BigDecimal r15_current;
		private BigDecimal r15_call;
		private BigDecimal r15_savings;
		private BigDecimal r15_notice_0to31;
		private BigDecimal r15_notice_32to88;
		private BigDecimal r15_fix_depo_91_day_depo;
		private BigDecimal r15_fix_depo_1to2;
		private BigDecimal r15_fix_depo_4to6;
		private BigDecimal r15_fix_depo_7to12;
		private BigDecimal r15_fix_depo_13to18;
		private BigDecimal r15_fix_depo_19to24;
		private BigDecimal r15_fix_depo_over24;
		private BigDecimal r15_cer_of_depo;
		private BigDecimal r15_total;
		private BigDecimal r15_pula_equivalent;
		private BigDecimal r15_avg_pula_equivalent;

		private String r16_foreign_curr_acc_by_curr;
		private BigDecimal r16_ex_rate_buy;
		private BigDecimal r16_ex_rate_mid;
		private BigDecimal r16_ex_rate_sell;
		private BigDecimal r16_current;
		private BigDecimal r16_call;
		private BigDecimal r16_savings;
		private BigDecimal r16_notice_0to31;
		private BigDecimal r16_notice_32to88;
		private BigDecimal r16_fix_depo_91_day_depo;
		private BigDecimal r16_fix_depo_1to2;
		private BigDecimal r16_fix_depo_4to6;
		private BigDecimal r16_fix_depo_7to12;
		private BigDecimal r16_fix_depo_13to18;
		private BigDecimal r16_fix_depo_19to24;
		private BigDecimal r16_fix_depo_over24;
		private BigDecimal r16_cer_of_depo;
		private BigDecimal r16_total;
		private BigDecimal r16_pula_equivalent;
		private BigDecimal r16_avg_pula_equivalent;

		private String r17_foreign_curr_acc_by_curr;
		private BigDecimal r17_ex_rate_buy;
		private BigDecimal r17_ex_rate_mid;
		private BigDecimal r17_ex_rate_sell;
		private BigDecimal r17_current;
		private BigDecimal r17_call;
		private BigDecimal r17_savings;
		private BigDecimal r17_notice_0to31;
		private BigDecimal r17_notice_32to88;
		private BigDecimal r17_fix_depo_91_day_depo;
		private BigDecimal r17_fix_depo_1to2;
		private BigDecimal r17_fix_depo_4to6;
		private BigDecimal r17_fix_depo_7to12;
		private BigDecimal r17_fix_depo_13to18;
		private BigDecimal r17_fix_depo_19to24;
		private BigDecimal r17_fix_depo_over24;
		private BigDecimal r17_cer_of_depo;
		private BigDecimal r17_total;
		private BigDecimal r17_pula_equivalent;
		private BigDecimal r17_avg_pula_equivalent;

		private String r18_foreign_curr_acc_by_curr;
		private BigDecimal r18_ex_rate_buy;
		private BigDecimal r18_ex_rate_mid;
		private BigDecimal r18_ex_rate_sell;
		private BigDecimal r18_current;
		private BigDecimal r18_call;
		private BigDecimal r18_savings;
		private BigDecimal r18_notice_0to31;
		private BigDecimal r18_notice_32to88;
		private BigDecimal r18_fix_depo_91_day_depo;
		private BigDecimal r18_fix_depo_1to2;
		private BigDecimal r18_fix_depo_4to6;
		private BigDecimal r18_fix_depo_7to12;
		private BigDecimal r18_fix_depo_13to18;
		private BigDecimal r18_fix_depo_19to24;
		private BigDecimal r18_fix_depo_over24;
		private BigDecimal r18_cer_of_depo;
		private BigDecimal r18_total;
		private BigDecimal r18_pula_equivalent;
		private BigDecimal r18_avg_pula_equivalent;

		private BigDecimal r28_import;
		private BigDecimal r28_investment;
		private BigDecimal r28_other;

		private BigDecimal r29_import;
		private BigDecimal r29_investment;
		private BigDecimal r29_other;

		private BigDecimal r30_import;
		private BigDecimal r30_investment;
		private BigDecimal r30_other;

		private BigDecimal r31_import;
		private BigDecimal r31_investment;
		private BigDecimal r31_other;

		private BigDecimal r32_import;
		private BigDecimal r32_investment;
		private BigDecimal r32_other;

		private BigDecimal r33_import;
		private BigDecimal r33_investment;
		private BigDecimal r33_other;

		private BigDecimal r34_import;
		private BigDecimal r34_investment;
		private BigDecimal r34_other;

		private BigDecimal r28_residents;
		private BigDecimal r28_non_residents;

		private BigDecimal r29_residents;
		private BigDecimal r29_non_residents;

		private BigDecimal r30_residents;
		private BigDecimal r30_non_residents;

		private BigDecimal r31_residents;
		private BigDecimal r31_non_residents;

		private BigDecimal r32_residents;
		private BigDecimal r32_non_residents;

		private BigDecimal r33_residents;
		private BigDecimal r33_non_residents;

		private BigDecimal r34_residents;
		private BigDecimal r34_non_residents;
		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date reportDate;

		@Column(name = "REPORT_VERSION")
		private BigDecimal reportVersion;

		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public M_DEP3_Summary_Entity() {
			super();

		}

		public String getR11_foreign_curr_acc_by_curr() {
			return r11_foreign_curr_acc_by_curr;
		}

		public void setR11_foreign_curr_acc_by_curr(String r11_foreign_curr_acc_by_curr) {
			this.r11_foreign_curr_acc_by_curr = r11_foreign_curr_acc_by_curr;
		}

		public BigDecimal getR11_ex_rate_buy() {
			return r11_ex_rate_buy;
		}

		public void setR11_ex_rate_buy(BigDecimal r11_ex_rate_buy) {
			this.r11_ex_rate_buy = r11_ex_rate_buy;
		}

		public BigDecimal getR11_ex_rate_mid() {
			return r11_ex_rate_mid;
		}

		public void setR11_ex_rate_mid(BigDecimal r11_ex_rate_mid) {
			this.r11_ex_rate_mid = r11_ex_rate_mid;
		}

		public BigDecimal getR11_ex_rate_sell() {
			return r11_ex_rate_sell;
		}

		public void setR11_ex_rate_sell(BigDecimal r11_ex_rate_sell) {
			this.r11_ex_rate_sell = r11_ex_rate_sell;
		}

		public BigDecimal getR11_current() {
			return r11_current;
		}

		public void setR11_current(BigDecimal r11_current) {
			this.r11_current = r11_current;
		}

		public BigDecimal getR11_call() {
			return r11_call;
		}

		public void setR11_call(BigDecimal r11_call) {
			this.r11_call = r11_call;
		}

		public BigDecimal getR11_savings() {
			return r11_savings;
		}

		public void setR11_savings(BigDecimal r11_savings) {
			this.r11_savings = r11_savings;
		}

		public BigDecimal getR11_notice_0to31() {
			return r11_notice_0to31;
		}

		public void setR11_notice_0to31(BigDecimal r11_notice_0to31) {
			this.r11_notice_0to31 = r11_notice_0to31;
		}

		public BigDecimal getR11_notice_32to88() {
			return r11_notice_32to88;
		}

		public void setR11_notice_32to88(BigDecimal r11_notice_32to88) {
			this.r11_notice_32to88 = r11_notice_32to88;
		}

		public BigDecimal getR11_fix_depo_91_day_depo() {
			return r11_fix_depo_91_day_depo;
		}

		public void setR11_fix_depo_91_day_depo(BigDecimal r11_fix_depo_91_day_depo) {
			this.r11_fix_depo_91_day_depo = r11_fix_depo_91_day_depo;
		}

		public BigDecimal getR11_fix_depo_1to2() {
			return r11_fix_depo_1to2;
		}

		public void setR11_fix_depo_1to2(BigDecimal r11_fix_depo_1to2) {
			this.r11_fix_depo_1to2 = r11_fix_depo_1to2;
		}

		public BigDecimal getR11_fix_depo_4to6() {
			return r11_fix_depo_4to6;
		}

		public void setR11_fix_depo_4to6(BigDecimal r11_fix_depo_4to6) {
			this.r11_fix_depo_4to6 = r11_fix_depo_4to6;
		}

		public BigDecimal getR11_fix_depo_7to12() {
			return r11_fix_depo_7to12;
		}

		public void setR11_fix_depo_7to12(BigDecimal r11_fix_depo_7to12) {
			this.r11_fix_depo_7to12 = r11_fix_depo_7to12;
		}

		public BigDecimal getR11_fix_depo_13to18() {
			return r11_fix_depo_13to18;
		}

		public void setR11_fix_depo_13to18(BigDecimal r11_fix_depo_13to18) {
			this.r11_fix_depo_13to18 = r11_fix_depo_13to18;
		}

		public BigDecimal getR11_fix_depo_19to24() {
			return r11_fix_depo_19to24;
		}

		public void setR11_fix_depo_19to24(BigDecimal r11_fix_depo_19to24) {
			this.r11_fix_depo_19to24 = r11_fix_depo_19to24;
		}

		public BigDecimal getR11_fix_depo_over24() {
			return r11_fix_depo_over24;
		}

		public void setR11_fix_depo_over24(BigDecimal r11_fix_depo_over24) {
			this.r11_fix_depo_over24 = r11_fix_depo_over24;
		}

		public BigDecimal getR11_cer_of_depo() {
			return r11_cer_of_depo;
		}

		public void setR11_cer_of_depo(BigDecimal r11_cer_of_depo) {
			this.r11_cer_of_depo = r11_cer_of_depo;
		}

		public BigDecimal getR11_total() {
			return r11_total;
		}

		public void setR11_total(BigDecimal r11_total) {
			this.r11_total = r11_total;
		}

		public BigDecimal getR11_pula_equivalent() {
			return r11_pula_equivalent;
		}

		public void setR11_pula_equivalent(BigDecimal r11_pula_equivalent) {
			this.r11_pula_equivalent = r11_pula_equivalent;
		}

		public BigDecimal getR11_avg_pula_equivalent() {
			return r11_avg_pula_equivalent;
		}

		public void setR11_avg_pula_equivalent(BigDecimal r11_avg_pula_equivalent) {
			this.r11_avg_pula_equivalent = r11_avg_pula_equivalent;
		}

		public String getR12_foreign_curr_acc_by_curr() {
			return r12_foreign_curr_acc_by_curr;
		}

		public void setR12_foreign_curr_acc_by_curr(String r12_foreign_curr_acc_by_curr) {
			this.r12_foreign_curr_acc_by_curr = r12_foreign_curr_acc_by_curr;
		}

		public BigDecimal getR12_ex_rate_buy() {
			return r12_ex_rate_buy;
		}

		public void setR12_ex_rate_buy(BigDecimal r12_ex_rate_buy) {
			this.r12_ex_rate_buy = r12_ex_rate_buy;
		}

		public BigDecimal getR12_ex_rate_mid() {
			return r12_ex_rate_mid;
		}

		public void setR12_ex_rate_mid(BigDecimal r12_ex_rate_mid) {
			this.r12_ex_rate_mid = r12_ex_rate_mid;
		}

		public BigDecimal getR12_ex_rate_sell() {
			return r12_ex_rate_sell;
		}

		public void setR12_ex_rate_sell(BigDecimal r12_ex_rate_sell) {
			this.r12_ex_rate_sell = r12_ex_rate_sell;
		}

		public BigDecimal getR12_current() {
			return r12_current;
		}

		public void setR12_current(BigDecimal r12_current) {
			this.r12_current = r12_current;
		}

		public BigDecimal getR12_call() {
			return r12_call;
		}

		public void setR12_call(BigDecimal r12_call) {
			this.r12_call = r12_call;
		}

		public BigDecimal getR12_savings() {
			return r12_savings;
		}

		public void setR12_savings(BigDecimal r12_savings) {
			this.r12_savings = r12_savings;
		}

		public BigDecimal getR12_notice_0to31() {
			return r12_notice_0to31;
		}

		public void setR12_notice_0to31(BigDecimal r12_notice_0to31) {
			this.r12_notice_0to31 = r12_notice_0to31;
		}

		public BigDecimal getR12_notice_32to88() {
			return r12_notice_32to88;
		}

		public void setR12_notice_32to88(BigDecimal r12_notice_32to88) {
			this.r12_notice_32to88 = r12_notice_32to88;
		}

		public BigDecimal getR12_fix_depo_91_day_depo() {
			return r12_fix_depo_91_day_depo;
		}

		public void setR12_fix_depo_91_day_depo(BigDecimal r12_fix_depo_91_day_depo) {
			this.r12_fix_depo_91_day_depo = r12_fix_depo_91_day_depo;
		}

		public BigDecimal getR12_fix_depo_1to2() {
			return r12_fix_depo_1to2;
		}

		public void setR12_fix_depo_1to2(BigDecimal r12_fix_depo_1to2) {
			this.r12_fix_depo_1to2 = r12_fix_depo_1to2;
		}

		public BigDecimal getR12_fix_depo_4to6() {
			return r12_fix_depo_4to6;
		}

		public void setR12_fix_depo_4to6(BigDecimal r12_fix_depo_4to6) {
			this.r12_fix_depo_4to6 = r12_fix_depo_4to6;
		}

		public BigDecimal getR12_fix_depo_7to12() {
			return r12_fix_depo_7to12;
		}

		public void setR12_fix_depo_7to12(BigDecimal r12_fix_depo_7to12) {
			this.r12_fix_depo_7to12 = r12_fix_depo_7to12;
		}

		public BigDecimal getR12_fix_depo_13to18() {
			return r12_fix_depo_13to18;
		}

		public void setR12_fix_depo_13to18(BigDecimal r12_fix_depo_13to18) {
			this.r12_fix_depo_13to18 = r12_fix_depo_13to18;
		}

		public BigDecimal getR12_fix_depo_19to24() {
			return r12_fix_depo_19to24;
		}

		public void setR12_fix_depo_19to24(BigDecimal r12_fix_depo_19to24) {
			this.r12_fix_depo_19to24 = r12_fix_depo_19to24;
		}

		public BigDecimal getR12_fix_depo_over24() {
			return r12_fix_depo_over24;
		}

		public void setR12_fix_depo_over24(BigDecimal r12_fix_depo_over24) {
			this.r12_fix_depo_over24 = r12_fix_depo_over24;
		}

		public BigDecimal getR12_cer_of_depo() {
			return r12_cer_of_depo;
		}

		public void setR12_cer_of_depo(BigDecimal r12_cer_of_depo) {
			this.r12_cer_of_depo = r12_cer_of_depo;
		}

		public BigDecimal getR12_total() {
			return r12_total;
		}

		public void setR12_total(BigDecimal r12_total) {
			this.r12_total = r12_total;
		}

		public BigDecimal getR12_pula_equivalent() {
			return r12_pula_equivalent;
		}

		public void setR12_pula_equivalent(BigDecimal r12_pula_equivalent) {
			this.r12_pula_equivalent = r12_pula_equivalent;
		}

		public BigDecimal getR12_avg_pula_equivalent() {
			return r12_avg_pula_equivalent;
		}

		public void setR12_avg_pula_equivalent(BigDecimal r12_avg_pula_equivalent) {
			this.r12_avg_pula_equivalent = r12_avg_pula_equivalent;
		}

		public String getR13_foreign_curr_acc_by_curr() {
			return r13_foreign_curr_acc_by_curr;
		}

		public void setR13_foreign_curr_acc_by_curr(String r13_foreign_curr_acc_by_curr) {
			this.r13_foreign_curr_acc_by_curr = r13_foreign_curr_acc_by_curr;
		}

		public BigDecimal getR13_ex_rate_buy() {
			return r13_ex_rate_buy;
		}

		public void setR13_ex_rate_buy(BigDecimal r13_ex_rate_buy) {
			this.r13_ex_rate_buy = r13_ex_rate_buy;
		}

		public BigDecimal getR13_ex_rate_mid() {
			return r13_ex_rate_mid;
		}

		public void setR13_ex_rate_mid(BigDecimal r13_ex_rate_mid) {
			this.r13_ex_rate_mid = r13_ex_rate_mid;
		}

		public BigDecimal getR13_ex_rate_sell() {
			return r13_ex_rate_sell;
		}

		public void setR13_ex_rate_sell(BigDecimal r13_ex_rate_sell) {
			this.r13_ex_rate_sell = r13_ex_rate_sell;
		}

		public BigDecimal getR13_current() {
			return r13_current;
		}

		public void setR13_current(BigDecimal r13_current) {
			this.r13_current = r13_current;
		}

		public BigDecimal getR13_call() {
			return r13_call;
		}

		public void setR13_call(BigDecimal r13_call) {
			this.r13_call = r13_call;
		}

		public BigDecimal getR13_savings() {
			return r13_savings;
		}

		public void setR13_savings(BigDecimal r13_savings) {
			this.r13_savings = r13_savings;
		}

		public BigDecimal getR13_notice_0to31() {
			return r13_notice_0to31;
		}

		public void setR13_notice_0to31(BigDecimal r13_notice_0to31) {
			this.r13_notice_0to31 = r13_notice_0to31;
		}

		public BigDecimal getR13_notice_32to88() {
			return r13_notice_32to88;
		}

		public void setR13_notice_32to88(BigDecimal r13_notice_32to88) {
			this.r13_notice_32to88 = r13_notice_32to88;
		}

		public BigDecimal getR13_fix_depo_91_day_depo() {
			return r13_fix_depo_91_day_depo;
		}

		public void setR13_fix_depo_91_day_depo(BigDecimal r13_fix_depo_91_day_depo) {
			this.r13_fix_depo_91_day_depo = r13_fix_depo_91_day_depo;
		}

		public BigDecimal getR13_fix_depo_1to2() {
			return r13_fix_depo_1to2;
		}

		public void setR13_fix_depo_1to2(BigDecimal r13_fix_depo_1to2) {
			this.r13_fix_depo_1to2 = r13_fix_depo_1to2;
		}

		public BigDecimal getR13_fix_depo_4to6() {
			return r13_fix_depo_4to6;
		}

		public void setR13_fix_depo_4to6(BigDecimal r13_fix_depo_4to6) {
			this.r13_fix_depo_4to6 = r13_fix_depo_4to6;
		}

		public BigDecimal getR13_fix_depo_7to12() {
			return r13_fix_depo_7to12;
		}

		public void setR13_fix_depo_7to12(BigDecimal r13_fix_depo_7to12) {
			this.r13_fix_depo_7to12 = r13_fix_depo_7to12;
		}

		public BigDecimal getR13_fix_depo_13to18() {
			return r13_fix_depo_13to18;
		}

		public void setR13_fix_depo_13to18(BigDecimal r13_fix_depo_13to18) {
			this.r13_fix_depo_13to18 = r13_fix_depo_13to18;
		}

		public BigDecimal getR13_fix_depo_19to24() {
			return r13_fix_depo_19to24;
		}

		public void setR13_fix_depo_19to24(BigDecimal r13_fix_depo_19to24) {
			this.r13_fix_depo_19to24 = r13_fix_depo_19to24;
		}

		public BigDecimal getR13_fix_depo_over24() {
			return r13_fix_depo_over24;
		}

		public void setR13_fix_depo_over24(BigDecimal r13_fix_depo_over24) {
			this.r13_fix_depo_over24 = r13_fix_depo_over24;
		}

		public BigDecimal getR13_cer_of_depo() {
			return r13_cer_of_depo;
		}

		public void setR13_cer_of_depo(BigDecimal r13_cer_of_depo) {
			this.r13_cer_of_depo = r13_cer_of_depo;
		}

		public BigDecimal getR13_total() {
			return r13_total;
		}

		public void setR13_total(BigDecimal r13_total) {
			this.r13_total = r13_total;
		}

		public BigDecimal getR13_pula_equivalent() {
			return r13_pula_equivalent;
		}

		public void setR13_pula_equivalent(BigDecimal r13_pula_equivalent) {
			this.r13_pula_equivalent = r13_pula_equivalent;
		}

		public BigDecimal getR13_avg_pula_equivalent() {
			return r13_avg_pula_equivalent;
		}

		public void setR13_avg_pula_equivalent(BigDecimal r13_avg_pula_equivalent) {
			this.r13_avg_pula_equivalent = r13_avg_pula_equivalent;
		}

		public String getR14_foreign_curr_acc_by_curr() {
			return r14_foreign_curr_acc_by_curr;
		}

		public void setR14_foreign_curr_acc_by_curr(String r14_foreign_curr_acc_by_curr) {
			this.r14_foreign_curr_acc_by_curr = r14_foreign_curr_acc_by_curr;
		}

		public BigDecimal getR14_ex_rate_buy() {
			return r14_ex_rate_buy;
		}

		public void setR14_ex_rate_buy(BigDecimal r14_ex_rate_buy) {
			this.r14_ex_rate_buy = r14_ex_rate_buy;
		}

		public BigDecimal getR14_ex_rate_mid() {
			return r14_ex_rate_mid;
		}

		public void setR14_ex_rate_mid(BigDecimal r14_ex_rate_mid) {
			this.r14_ex_rate_mid = r14_ex_rate_mid;
		}

		public BigDecimal getR14_ex_rate_sell() {
			return r14_ex_rate_sell;
		}

		public void setR14_ex_rate_sell(BigDecimal r14_ex_rate_sell) {
			this.r14_ex_rate_sell = r14_ex_rate_sell;
		}

		public BigDecimal getR14_current() {
			return r14_current;
		}

		public void setR14_current(BigDecimal r14_current) {
			this.r14_current = r14_current;
		}

		public BigDecimal getR14_call() {
			return r14_call;
		}

		public void setR14_call(BigDecimal r14_call) {
			this.r14_call = r14_call;
		}

		public BigDecimal getR14_savings() {
			return r14_savings;
		}

		public void setR14_savings(BigDecimal r14_savings) {
			this.r14_savings = r14_savings;
		}

		public BigDecimal getR14_notice_0to31() {
			return r14_notice_0to31;
		}

		public void setR14_notice_0to31(BigDecimal r14_notice_0to31) {
			this.r14_notice_0to31 = r14_notice_0to31;
		}

		public BigDecimal getR14_notice_32to88() {
			return r14_notice_32to88;
		}

		public void setR14_notice_32to88(BigDecimal r14_notice_32to88) {
			this.r14_notice_32to88 = r14_notice_32to88;
		}

		public BigDecimal getR14_fix_depo_91_day_depo() {
			return r14_fix_depo_91_day_depo;
		}

		public void setR14_fix_depo_91_day_depo(BigDecimal r14_fix_depo_91_day_depo) {
			this.r14_fix_depo_91_day_depo = r14_fix_depo_91_day_depo;
		}

		public BigDecimal getR14_fix_depo_1to2() {
			return r14_fix_depo_1to2;
		}

		public void setR14_fix_depo_1to2(BigDecimal r14_fix_depo_1to2) {
			this.r14_fix_depo_1to2 = r14_fix_depo_1to2;
		}

		public BigDecimal getR14_fix_depo_4to6() {
			return r14_fix_depo_4to6;
		}

		public void setR14_fix_depo_4to6(BigDecimal r14_fix_depo_4to6) {
			this.r14_fix_depo_4to6 = r14_fix_depo_4to6;
		}

		public BigDecimal getR14_fix_depo_7to12() {
			return r14_fix_depo_7to12;
		}

		public void setR14_fix_depo_7to12(BigDecimal r14_fix_depo_7to12) {
			this.r14_fix_depo_7to12 = r14_fix_depo_7to12;
		}

		public BigDecimal getR14_fix_depo_13to18() {
			return r14_fix_depo_13to18;
		}

		public void setR14_fix_depo_13to18(BigDecimal r14_fix_depo_13to18) {
			this.r14_fix_depo_13to18 = r14_fix_depo_13to18;
		}

		public BigDecimal getR14_fix_depo_19to24() {
			return r14_fix_depo_19to24;
		}

		public void setR14_fix_depo_19to24(BigDecimal r14_fix_depo_19to24) {
			this.r14_fix_depo_19to24 = r14_fix_depo_19to24;
		}

		public BigDecimal getR14_fix_depo_over24() {
			return r14_fix_depo_over24;
		}

		public void setR14_fix_depo_over24(BigDecimal r14_fix_depo_over24) {
			this.r14_fix_depo_over24 = r14_fix_depo_over24;
		}

		public BigDecimal getR14_cer_of_depo() {
			return r14_cer_of_depo;
		}

		public void setR14_cer_of_depo(BigDecimal r14_cer_of_depo) {
			this.r14_cer_of_depo = r14_cer_of_depo;
		}

		public BigDecimal getR14_total() {
			return r14_total;
		}

		public void setR14_total(BigDecimal r14_total) {
			this.r14_total = r14_total;
		}

		public BigDecimal getR14_pula_equivalent() {
			return r14_pula_equivalent;
		}

		public void setR14_pula_equivalent(BigDecimal r14_pula_equivalent) {
			this.r14_pula_equivalent = r14_pula_equivalent;
		}

		public BigDecimal getR14_avg_pula_equivalent() {
			return r14_avg_pula_equivalent;
		}

		public void setR14_avg_pula_equivalent(BigDecimal r14_avg_pula_equivalent) {
			this.r14_avg_pula_equivalent = r14_avg_pula_equivalent;
		}

		public String getR15_foreign_curr_acc_by_curr() {
			return r15_foreign_curr_acc_by_curr;
		}

		public void setR15_foreign_curr_acc_by_curr(String r15_foreign_curr_acc_by_curr) {
			this.r15_foreign_curr_acc_by_curr = r15_foreign_curr_acc_by_curr;
		}

		public BigDecimal getR15_ex_rate_buy() {
			return r15_ex_rate_buy;
		}

		public void setR15_ex_rate_buy(BigDecimal r15_ex_rate_buy) {
			this.r15_ex_rate_buy = r15_ex_rate_buy;
		}

		public BigDecimal getR15_ex_rate_mid() {
			return r15_ex_rate_mid;
		}

		public void setR15_ex_rate_mid(BigDecimal r15_ex_rate_mid) {
			this.r15_ex_rate_mid = r15_ex_rate_mid;
		}

		public BigDecimal getR15_ex_rate_sell() {
			return r15_ex_rate_sell;
		}

		public void setR15_ex_rate_sell(BigDecimal r15_ex_rate_sell) {
			this.r15_ex_rate_sell = r15_ex_rate_sell;
		}

		public BigDecimal getR15_current() {
			return r15_current;
		}

		public void setR15_current(BigDecimal r15_current) {
			this.r15_current = r15_current;
		}

		public BigDecimal getR15_call() {
			return r15_call;
		}

		public void setR15_call(BigDecimal r15_call) {
			this.r15_call = r15_call;
		}

		public BigDecimal getR15_savings() {
			return r15_savings;
		}

		public void setR15_savings(BigDecimal r15_savings) {
			this.r15_savings = r15_savings;
		}

		public BigDecimal getR15_notice_0to31() {
			return r15_notice_0to31;
		}

		public void setR15_notice_0to31(BigDecimal r15_notice_0to31) {
			this.r15_notice_0to31 = r15_notice_0to31;
		}

		public BigDecimal getR15_notice_32to88() {
			return r15_notice_32to88;
		}

		public void setR15_notice_32to88(BigDecimal r15_notice_32to88) {
			this.r15_notice_32to88 = r15_notice_32to88;
		}

		public BigDecimal getR15_fix_depo_91_day_depo() {
			return r15_fix_depo_91_day_depo;
		}

		public void setR15_fix_depo_91_day_depo(BigDecimal r15_fix_depo_91_day_depo) {
			this.r15_fix_depo_91_day_depo = r15_fix_depo_91_day_depo;
		}

		public BigDecimal getR15_fix_depo_1to2() {
			return r15_fix_depo_1to2;
		}

		public void setR15_fix_depo_1to2(BigDecimal r15_fix_depo_1to2) {
			this.r15_fix_depo_1to2 = r15_fix_depo_1to2;
		}

		public BigDecimal getR15_fix_depo_4to6() {
			return r15_fix_depo_4to6;
		}

		public void setR15_fix_depo_4to6(BigDecimal r15_fix_depo_4to6) {
			this.r15_fix_depo_4to6 = r15_fix_depo_4to6;
		}

		public BigDecimal getR15_fix_depo_7to12() {
			return r15_fix_depo_7to12;
		}

		public void setR15_fix_depo_7to12(BigDecimal r15_fix_depo_7to12) {
			this.r15_fix_depo_7to12 = r15_fix_depo_7to12;
		}

		public BigDecimal getR15_fix_depo_13to18() {
			return r15_fix_depo_13to18;
		}

		public void setR15_fix_depo_13to18(BigDecimal r15_fix_depo_13to18) {
			this.r15_fix_depo_13to18 = r15_fix_depo_13to18;
		}

		public BigDecimal getR15_fix_depo_19to24() {
			return r15_fix_depo_19to24;
		}

		public void setR15_fix_depo_19to24(BigDecimal r15_fix_depo_19to24) {
			this.r15_fix_depo_19to24 = r15_fix_depo_19to24;
		}

		public BigDecimal getR15_fix_depo_over24() {
			return r15_fix_depo_over24;
		}

		public void setR15_fix_depo_over24(BigDecimal r15_fix_depo_over24) {
			this.r15_fix_depo_over24 = r15_fix_depo_over24;
		}

		public BigDecimal getR15_cer_of_depo() {
			return r15_cer_of_depo;
		}

		public void setR15_cer_of_depo(BigDecimal r15_cer_of_depo) {
			this.r15_cer_of_depo = r15_cer_of_depo;
		}

		public BigDecimal getR15_total() {
			return r15_total;
		}

		public void setR15_total(BigDecimal r15_total) {
			this.r15_total = r15_total;
		}

		public BigDecimal getR15_pula_equivalent() {
			return r15_pula_equivalent;
		}

		public void setR15_pula_equivalent(BigDecimal r15_pula_equivalent) {
			this.r15_pula_equivalent = r15_pula_equivalent;
		}

		public BigDecimal getR15_avg_pula_equivalent() {
			return r15_avg_pula_equivalent;
		}

		public void setR15_avg_pula_equivalent(BigDecimal r15_avg_pula_equivalent) {
			this.r15_avg_pula_equivalent = r15_avg_pula_equivalent;
		}

		public String getR16_foreign_curr_acc_by_curr() {
			return r16_foreign_curr_acc_by_curr;
		}

		public void setR16_foreign_curr_acc_by_curr(String r16_foreign_curr_acc_by_curr) {
			this.r16_foreign_curr_acc_by_curr = r16_foreign_curr_acc_by_curr;
		}

		public BigDecimal getR16_ex_rate_buy() {
			return r16_ex_rate_buy;
		}

		public void setR16_ex_rate_buy(BigDecimal r16_ex_rate_buy) {
			this.r16_ex_rate_buy = r16_ex_rate_buy;
		}

		public BigDecimal getR16_ex_rate_mid() {
			return r16_ex_rate_mid;
		}

		public void setR16_ex_rate_mid(BigDecimal r16_ex_rate_mid) {
			this.r16_ex_rate_mid = r16_ex_rate_mid;
		}

		public BigDecimal getR16_ex_rate_sell() {
			return r16_ex_rate_sell;
		}

		public void setR16_ex_rate_sell(BigDecimal r16_ex_rate_sell) {
			this.r16_ex_rate_sell = r16_ex_rate_sell;
		}

		public BigDecimal getR16_current() {
			return r16_current;
		}

		public void setR16_current(BigDecimal r16_current) {
			this.r16_current = r16_current;
		}

		public BigDecimal getR16_call() {
			return r16_call;
		}

		public void setR16_call(BigDecimal r16_call) {
			this.r16_call = r16_call;
		}

		public BigDecimal getR16_savings() {
			return r16_savings;
		}

		public void setR16_savings(BigDecimal r16_savings) {
			this.r16_savings = r16_savings;
		}

		public BigDecimal getR16_notice_0to31() {
			return r16_notice_0to31;
		}

		public void setR16_notice_0to31(BigDecimal r16_notice_0to31) {
			this.r16_notice_0to31 = r16_notice_0to31;
		}

		public BigDecimal getR16_notice_32to88() {
			return r16_notice_32to88;
		}

		public void setR16_notice_32to88(BigDecimal r16_notice_32to88) {
			this.r16_notice_32to88 = r16_notice_32to88;
		}

		public BigDecimal getR16_fix_depo_91_day_depo() {
			return r16_fix_depo_91_day_depo;
		}

		public void setR16_fix_depo_91_day_depo(BigDecimal r16_fix_depo_91_day_depo) {
			this.r16_fix_depo_91_day_depo = r16_fix_depo_91_day_depo;
		}

		public BigDecimal getR16_fix_depo_1to2() {
			return r16_fix_depo_1to2;
		}

		public void setR16_fix_depo_1to2(BigDecimal r16_fix_depo_1to2) {
			this.r16_fix_depo_1to2 = r16_fix_depo_1to2;
		}

		public BigDecimal getR16_fix_depo_4to6() {
			return r16_fix_depo_4to6;
		}

		public void setR16_fix_depo_4to6(BigDecimal r16_fix_depo_4to6) {
			this.r16_fix_depo_4to6 = r16_fix_depo_4to6;
		}

		public BigDecimal getR16_fix_depo_7to12() {
			return r16_fix_depo_7to12;
		}

		public void setR16_fix_depo_7to12(BigDecimal r16_fix_depo_7to12) {
			this.r16_fix_depo_7to12 = r16_fix_depo_7to12;
		}

		public BigDecimal getR16_fix_depo_13to18() {
			return r16_fix_depo_13to18;
		}

		public void setR16_fix_depo_13to18(BigDecimal r16_fix_depo_13to18) {
			this.r16_fix_depo_13to18 = r16_fix_depo_13to18;
		}

		public BigDecimal getR16_fix_depo_19to24() {
			return r16_fix_depo_19to24;
		}

		public void setR16_fix_depo_19to24(BigDecimal r16_fix_depo_19to24) {
			this.r16_fix_depo_19to24 = r16_fix_depo_19to24;
		}

		public BigDecimal getR16_fix_depo_over24() {
			return r16_fix_depo_over24;
		}

		public void setR16_fix_depo_over24(BigDecimal r16_fix_depo_over24) {
			this.r16_fix_depo_over24 = r16_fix_depo_over24;
		}

		public BigDecimal getR16_cer_of_depo() {
			return r16_cer_of_depo;
		}

		public void setR16_cer_of_depo(BigDecimal r16_cer_of_depo) {
			this.r16_cer_of_depo = r16_cer_of_depo;
		}

		public BigDecimal getR16_total() {
			return r16_total;
		}

		public void setR16_total(BigDecimal r16_total) {
			this.r16_total = r16_total;
		}

		public BigDecimal getR16_pula_equivalent() {
			return r16_pula_equivalent;
		}

		public void setR16_pula_equivalent(BigDecimal r16_pula_equivalent) {
			this.r16_pula_equivalent = r16_pula_equivalent;
		}

		public BigDecimal getR16_avg_pula_equivalent() {
			return r16_avg_pula_equivalent;
		}

		public void setR16_avg_pula_equivalent(BigDecimal r16_avg_pula_equivalent) {
			this.r16_avg_pula_equivalent = r16_avg_pula_equivalent;
		}

		public String getR17_foreign_curr_acc_by_curr() {
			return r17_foreign_curr_acc_by_curr;
		}

		public void setR17_foreign_curr_acc_by_curr(String r17_foreign_curr_acc_by_curr) {
			this.r17_foreign_curr_acc_by_curr = r17_foreign_curr_acc_by_curr;
		}

		public BigDecimal getR17_ex_rate_buy() {
			return r17_ex_rate_buy;
		}

		public void setR17_ex_rate_buy(BigDecimal r17_ex_rate_buy) {
			this.r17_ex_rate_buy = r17_ex_rate_buy;
		}

		public BigDecimal getR17_ex_rate_mid() {
			return r17_ex_rate_mid;
		}

		public void setR17_ex_rate_mid(BigDecimal r17_ex_rate_mid) {
			this.r17_ex_rate_mid = r17_ex_rate_mid;
		}

		public BigDecimal getR17_ex_rate_sell() {
			return r17_ex_rate_sell;
		}

		public void setR17_ex_rate_sell(BigDecimal r17_ex_rate_sell) {
			this.r17_ex_rate_sell = r17_ex_rate_sell;
		}

		public BigDecimal getR17_current() {
			return r17_current;
		}

		public void setR17_current(BigDecimal r17_current) {
			this.r17_current = r17_current;
		}

		public BigDecimal getR17_call() {
			return r17_call;
		}

		public void setR17_call(BigDecimal r17_call) {
			this.r17_call = r17_call;
		}

		public BigDecimal getR17_savings() {
			return r17_savings;
		}

		public void setR17_savings(BigDecimal r17_savings) {
			this.r17_savings = r17_savings;
		}

		public BigDecimal getR17_notice_0to31() {
			return r17_notice_0to31;
		}

		public void setR17_notice_0to31(BigDecimal r17_notice_0to31) {
			this.r17_notice_0to31 = r17_notice_0to31;
		}

		public BigDecimal getR17_notice_32to88() {
			return r17_notice_32to88;
		}

		public void setR17_notice_32to88(BigDecimal r17_notice_32to88) {
			this.r17_notice_32to88 = r17_notice_32to88;
		}

		public BigDecimal getR17_fix_depo_91_day_depo() {
			return r17_fix_depo_91_day_depo;
		}

		public void setR17_fix_depo_91_day_depo(BigDecimal r17_fix_depo_91_day_depo) {
			this.r17_fix_depo_91_day_depo = r17_fix_depo_91_day_depo;
		}

		public BigDecimal getR17_fix_depo_1to2() {
			return r17_fix_depo_1to2;
		}

		public void setR17_fix_depo_1to2(BigDecimal r17_fix_depo_1to2) {
			this.r17_fix_depo_1to2 = r17_fix_depo_1to2;
		}

		public BigDecimal getR17_fix_depo_4to6() {
			return r17_fix_depo_4to6;
		}

		public void setR17_fix_depo_4to6(BigDecimal r17_fix_depo_4to6) {
			this.r17_fix_depo_4to6 = r17_fix_depo_4to6;
		}

		public BigDecimal getR17_fix_depo_7to12() {
			return r17_fix_depo_7to12;
		}

		public void setR17_fix_depo_7to12(BigDecimal r17_fix_depo_7to12) {
			this.r17_fix_depo_7to12 = r17_fix_depo_7to12;
		}

		public BigDecimal getR17_fix_depo_13to18() {
			return r17_fix_depo_13to18;
		}

		public void setR17_fix_depo_13to18(BigDecimal r17_fix_depo_13to18) {
			this.r17_fix_depo_13to18 = r17_fix_depo_13to18;
		}

		public BigDecimal getR17_fix_depo_19to24() {
			return r17_fix_depo_19to24;
		}

		public void setR17_fix_depo_19to24(BigDecimal r17_fix_depo_19to24) {
			this.r17_fix_depo_19to24 = r17_fix_depo_19to24;
		}

		public BigDecimal getR17_fix_depo_over24() {
			return r17_fix_depo_over24;
		}

		public void setR17_fix_depo_over24(BigDecimal r17_fix_depo_over24) {
			this.r17_fix_depo_over24 = r17_fix_depo_over24;
		}

		public BigDecimal getR17_cer_of_depo() {
			return r17_cer_of_depo;
		}

		public void setR17_cer_of_depo(BigDecimal r17_cer_of_depo) {
			this.r17_cer_of_depo = r17_cer_of_depo;
		}

		public BigDecimal getR17_total() {
			return r17_total;
		}

		public void setR17_total(BigDecimal r17_total) {
			this.r17_total = r17_total;
		}

		public BigDecimal getR17_pula_equivalent() {
			return r17_pula_equivalent;
		}

		public void setR17_pula_equivalent(BigDecimal r17_pula_equivalent) {
			this.r17_pula_equivalent = r17_pula_equivalent;
		}

		public BigDecimal getR17_avg_pula_equivalent() {
			return r17_avg_pula_equivalent;
		}

		public void setR17_avg_pula_equivalent(BigDecimal r17_avg_pula_equivalent) {
			this.r17_avg_pula_equivalent = r17_avg_pula_equivalent;
		}

		public String getR18_foreign_curr_acc_by_curr() {
			return r18_foreign_curr_acc_by_curr;
		}

		public void setR18_foreign_curr_acc_by_curr(String r18_foreign_curr_acc_by_curr) {
			this.r18_foreign_curr_acc_by_curr = r18_foreign_curr_acc_by_curr;
		}

		public BigDecimal getR18_ex_rate_buy() {
			return r18_ex_rate_buy;
		}

		public void setR18_ex_rate_buy(BigDecimal r18_ex_rate_buy) {
			this.r18_ex_rate_buy = r18_ex_rate_buy;
		}

		public BigDecimal getR18_ex_rate_mid() {
			return r18_ex_rate_mid;
		}

		public void setR18_ex_rate_mid(BigDecimal r18_ex_rate_mid) {
			this.r18_ex_rate_mid = r18_ex_rate_mid;
		}

		public BigDecimal getR18_ex_rate_sell() {
			return r18_ex_rate_sell;
		}

		public void setR18_ex_rate_sell(BigDecimal r18_ex_rate_sell) {
			this.r18_ex_rate_sell = r18_ex_rate_sell;
		}

		public BigDecimal getR18_current() {
			return r18_current;
		}

		public void setR18_current(BigDecimal r18_current) {
			this.r18_current = r18_current;
		}

		public BigDecimal getR18_call() {
			return r18_call;
		}

		public void setR18_call(BigDecimal r18_call) {
			this.r18_call = r18_call;
		}

		public BigDecimal getR18_savings() {
			return r18_savings;
		}

		public void setR18_savings(BigDecimal r18_savings) {
			this.r18_savings = r18_savings;
		}

		public BigDecimal getR18_notice_0to31() {
			return r18_notice_0to31;
		}

		public void setR18_notice_0to31(BigDecimal r18_notice_0to31) {
			this.r18_notice_0to31 = r18_notice_0to31;
		}

		public BigDecimal getR18_notice_32to88() {
			return r18_notice_32to88;
		}

		public void setR18_notice_32to88(BigDecimal r18_notice_32to88) {
			this.r18_notice_32to88 = r18_notice_32to88;
		}

		public BigDecimal getR18_fix_depo_91_day_depo() {
			return r18_fix_depo_91_day_depo;
		}

		public void setR18_fix_depo_91_day_depo(BigDecimal r18_fix_depo_91_day_depo) {
			this.r18_fix_depo_91_day_depo = r18_fix_depo_91_day_depo;
		}

		public BigDecimal getR18_fix_depo_1to2() {
			return r18_fix_depo_1to2;
		}

		public void setR18_fix_depo_1to2(BigDecimal r18_fix_depo_1to2) {
			this.r18_fix_depo_1to2 = r18_fix_depo_1to2;
		}

		public BigDecimal getR18_fix_depo_4to6() {
			return r18_fix_depo_4to6;
		}

		public void setR18_fix_depo_4to6(BigDecimal r18_fix_depo_4to6) {
			this.r18_fix_depo_4to6 = r18_fix_depo_4to6;
		}

		public BigDecimal getR18_fix_depo_7to12() {
			return r18_fix_depo_7to12;
		}

		public void setR18_fix_depo_7to12(BigDecimal r18_fix_depo_7to12) {
			this.r18_fix_depo_7to12 = r18_fix_depo_7to12;
		}

		public BigDecimal getR18_fix_depo_13to18() {
			return r18_fix_depo_13to18;
		}

		public void setR18_fix_depo_13to18(BigDecimal r18_fix_depo_13to18) {
			this.r18_fix_depo_13to18 = r18_fix_depo_13to18;
		}

		public BigDecimal getR18_fix_depo_19to24() {
			return r18_fix_depo_19to24;
		}

		public void setR18_fix_depo_19to24(BigDecimal r18_fix_depo_19to24) {
			this.r18_fix_depo_19to24 = r18_fix_depo_19to24;
		}

		public BigDecimal getR18_fix_depo_over24() {
			return r18_fix_depo_over24;
		}

		public void setR18_fix_depo_over24(BigDecimal r18_fix_depo_over24) {
			this.r18_fix_depo_over24 = r18_fix_depo_over24;
		}

		public BigDecimal getR18_cer_of_depo() {
			return r18_cer_of_depo;
		}

		public void setR18_cer_of_depo(BigDecimal r18_cer_of_depo) {
			this.r18_cer_of_depo = r18_cer_of_depo;
		}

		public BigDecimal getR18_total() {
			return r18_total;
		}

		public void setR18_total(BigDecimal r18_total) {
			this.r18_total = r18_total;
		}

		public BigDecimal getR18_pula_equivalent() {
			return r18_pula_equivalent;
		}

		public void setR18_pula_equivalent(BigDecimal r18_pula_equivalent) {
			this.r18_pula_equivalent = r18_pula_equivalent;
		}

		public BigDecimal getR18_avg_pula_equivalent() {
			return r18_avg_pula_equivalent;
		}

		public void setR18_avg_pula_equivalent(BigDecimal r18_avg_pula_equivalent) {
			this.r18_avg_pula_equivalent = r18_avg_pula_equivalent;
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

		public BigDecimal getR28_import() {
			return r28_import;
		}

		public void setR28_import(BigDecimal r28_import) {
			this.r28_import = r28_import;
		}

		public BigDecimal getR28_investment() {
			return r28_investment;
		}

		public void setR28_investment(BigDecimal r28_investment) {
			this.r28_investment = r28_investment;
		}

		public BigDecimal getR28_other() {
			return r28_other;
		}

		public void setR28_other(BigDecimal r28_other) {
			this.r28_other = r28_other;
		}

		public BigDecimal getR29_import() {
			return r29_import;
		}

		public void setR29_import(BigDecimal r29_import) {
			this.r29_import = r29_import;
		}

		public BigDecimal getR29_investment() {
			return r29_investment;
		}

		public void setR29_investment(BigDecimal r29_investment) {
			this.r29_investment = r29_investment;
		}

		public BigDecimal getR29_other() {
			return r29_other;
		}

		public void setR29_other(BigDecimal r29_other) {
			this.r29_other = r29_other;
		}

		public BigDecimal getR30_import() {
			return r30_import;
		}

		public void setR30_import(BigDecimal r30_import) {
			this.r30_import = r30_import;
		}

		public BigDecimal getR30_investment() {
			return r30_investment;
		}

		public void setR30_investment(BigDecimal r30_investment) {
			this.r30_investment = r30_investment;
		}

		public BigDecimal getR30_other() {
			return r30_other;
		}

		public void setR30_other(BigDecimal r30_other) {
			this.r30_other = r30_other;
		}

		public BigDecimal getR31_import() {
			return r31_import;
		}

		public void setR31_import(BigDecimal r31_import) {
			this.r31_import = r31_import;
		}

		public BigDecimal getR31_investment() {
			return r31_investment;
		}

		public void setR31_investment(BigDecimal r31_investment) {
			this.r31_investment = r31_investment;
		}

		public BigDecimal getR31_other() {
			return r31_other;
		}

		public void setR31_other(BigDecimal r31_other) {
			this.r31_other = r31_other;
		}

		public BigDecimal getR32_import() {
			return r32_import;
		}

		public void setR32_import(BigDecimal r32_import) {
			this.r32_import = r32_import;
		}

		public BigDecimal getR32_investment() {
			return r32_investment;
		}

		public void setR32_investment(BigDecimal r32_investment) {
			this.r32_investment = r32_investment;
		}

		public BigDecimal getR32_other() {
			return r32_other;
		}

		public void setR32_other(BigDecimal r32_other) {
			this.r32_other = r32_other;
		}

		public BigDecimal getR33_import() {
			return r33_import;
		}

		public void setR33_import(BigDecimal r33_import) {
			this.r33_import = r33_import;
		}

		public BigDecimal getR33_investment() {
			return r33_investment;
		}

		public void setR33_investment(BigDecimal r33_investment) {
			this.r33_investment = r33_investment;
		}

		public BigDecimal getR33_other() {
			return r33_other;
		}

		public void setR33_other(BigDecimal r33_other) {
			this.r33_other = r33_other;
		}

		public BigDecimal getR34_import() {
			return r34_import;
		}

		public void setR34_import(BigDecimal r34_import) {
			this.r34_import = r34_import;
		}

		public BigDecimal getR34_investment() {
			return r34_investment;
		}

		public void setR34_investment(BigDecimal r34_investment) {
			this.r34_investment = r34_investment;
		}

		public BigDecimal getR34_other() {
			return r34_other;
		}

		public void setR34_other(BigDecimal r34_other) {
			this.r34_other = r34_other;
		}

		public BigDecimal getR28_residents() {
			return r28_residents;
		}

		public void setR28_residents(BigDecimal r28_residents) {
			this.r28_residents = r28_residents;
		}

		public BigDecimal getR28_non_residents() {
			return r28_non_residents;
		}

		public void setR28_non_residents(BigDecimal r28_non_residents) {
			this.r28_non_residents = r28_non_residents;
		}

		public BigDecimal getR29_residents() {
			return r29_residents;
		}

		public void setR29_residents(BigDecimal r29_residents) {
			this.r29_residents = r29_residents;
		}

		public BigDecimal getR29_non_residents() {
			return r29_non_residents;
		}

		public void setR29_non_residents(BigDecimal r29_non_residents) {
			this.r29_non_residents = r29_non_residents;
		}

		public BigDecimal getR30_residents() {
			return r30_residents;
		}

		public void setR30_residents(BigDecimal r30_residents) {
			this.r30_residents = r30_residents;
		}

		public BigDecimal getR30_non_residents() {
			return r30_non_residents;
		}

		public void setR30_non_residents(BigDecimal r30_non_residents) {
			this.r30_non_residents = r30_non_residents;
		}

		public BigDecimal getR31_residents() {
			return r31_residents;
		}

		public void setR31_residents(BigDecimal r31_residents) {
			this.r31_residents = r31_residents;
		}

		public BigDecimal getR31_non_residents() {
			return r31_non_residents;
		}

		public void setR31_non_residents(BigDecimal r31_non_residents) {
			this.r31_non_residents = r31_non_residents;
		}

		public BigDecimal getR32_residents() {
			return r32_residents;
		}

		public void setR32_residents(BigDecimal r32_residents) {
			this.r32_residents = r32_residents;
		}

		public BigDecimal getR32_non_residents() {
			return r32_non_residents;
		}

		public void setR32_non_residents(BigDecimal r32_non_residents) {
			this.r32_non_residents = r32_non_residents;
		}

		public BigDecimal getR33_residents() {
			return r33_residents;
		}

		public void setR33_residents(BigDecimal r33_residents) {
			this.r33_residents = r33_residents;
		}

		public BigDecimal getR33_non_residents() {
			return r33_non_residents;
		}

		public void setR33_non_residents(BigDecimal r33_non_residents) {
			this.r33_non_residents = r33_non_residents;
		}

		public BigDecimal getR34_residents() {
			return r34_residents;
		}

		public void setR34_residents(BigDecimal r34_residents) {
			this.r34_residents = r34_residents;
		}

		public BigDecimal getR34_non_residents() {
			return r34_non_residents;
		}

		public void setR34_non_residents(BigDecimal r34_non_residents) {
			this.r34_non_residents = r34_non_residents;
		}

	}

	// ------------------------------
	// ENTITY: BRRS_M_DEP3_DETAILTABLE
	// ------------------------------
	public static class M_DEP3_Detail_Entity {

		@Column(name = "CUST_ID")
		private String custId;

		@Id
		@Column(name = "ACCT_NUMBER")
		private String acctNumber;

		@Column(name = "ACCT_NAME")
		private String acctName;

		@Column(name = "DATA_TYPE")
		private String dataType;

		@Column(name = "REPORT_LABEL")
		private String reportLabel;

		@Column(name = "REPORT_ADDL_CRITERIA_1")
		private String reportAddlCriteria1;

		@Column(name = "REPORT_REMARKS")
		private String reportRemarks;

		@Column(name = "MODIFICATION_REMARKS")
		private String modificationRemarks;

		@Column(name = "DATA_ENTRY_VERSION")
		private String dataEntryVersion;

		@Column(name = "ACCT_BALANCE_IN_PULA")
		private BigDecimal acctBalanceInpula;

		@Column(name = "REPORT_DATE")
		private Date reportDate;

		@Column(name = "REPORT_NAME")
		private String reportName;

		@Column(name = "CREATE_USER")
		private String createUser;

		@Column(name = "CREATE_TIME")
		private Date createTime;

		@Column(name = "MODIFY_USER")
		private String modifyUser;

		@Column(name = "MODIFY_TIME")
		private Date modifyTime;

		@Column(name = "VERIFY_USER")
		private String verifyUser;

		@Column(name = "VERIFY_TIME")
		private Date verifyTime;

		@Column(name = "ENTITY_FLG")
		private String entityFlg;

		@Column(name = "MODIFY_FLG")
		private String modifyFlg;

		@Column(name = "DEL_FLG")
		private String delFlg;

		@Column(name = "CCY")
		private String ccy;

		@Column(name = "SEGMENT")
		private String segment;

		@Column(name = "TYPE")
		private String type;

		@Column(name = "MAT_BUCK_1")
		private String matBuck1;

		@Column(name = "EX_RATE_BUY")
		private BigDecimal exRateBuy;

		@Column(name = "EX_RATE_SELL")
		private BigDecimal exRateSell;

		@Column(name = "NOTICE_0TO31")
		private BigDecimal notice0to31;

		@Column(name = "NOTICE_32TO88")
		private BigDecimal notice32to88;

		@Column(name = "CER_OF_DEPO")
		private BigDecimal cerOfDepo;

		@Column(name = "IMPORT")
		private BigDecimal importValue; // 'import' is Java keyword

		@Column(name = "INVESTMENT")
		private BigDecimal investment;

		@Column(name = "OTHER")
		private BigDecimal other;

		@Column(name = "RESIDENTS")
		private BigDecimal residents;

		@Column(name = "NON_RESIDENTS")
		private BigDecimal nonResidents;

		@Column(name = "SNO")
		private Long sno;

		@Column(name = "REPORT_CODE")
		private String reportCode;

		public M_DEP3_Detail_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

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

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String reportLabel) {
			this.reportLabel = reportLabel;
		}

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
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

		public BigDecimal getAcctBalanceInpula() {
			return acctBalanceInpula;
		}

		public void setAcctBalanceInpula(BigDecimal acctBalanceInpula) {
			this.acctBalanceInpula = acctBalanceInpula;
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

		public String getCcy() {
			return ccy;
		}

		public void setCcy(String ccy) {
			this.ccy = ccy;
		}

		public String getSegment() {
			return segment;
		}

		public void setSegment(String segment) {
			this.segment = segment;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getMatBuck1() {
			return matBuck1;
		}

		public void setMatBuck1(String matBuck1) {
			this.matBuck1 = matBuck1;
		}

		public BigDecimal getExRateBuy() {
			return exRateBuy;
		}

		public void setExRateBuy(BigDecimal exRateBuy) {
			this.exRateBuy = exRateBuy;
		}

		public BigDecimal getExRateSell() {
			return exRateSell;
		}

		public void setExRateSell(BigDecimal exRateSell) {
			this.exRateSell = exRateSell;
		}

		public BigDecimal getNotice0to31() {
			return notice0to31;
		}

		public void setNotice0to31(BigDecimal notice0to31) {
			this.notice0to31 = notice0to31;
		}

		public BigDecimal getNotice32to88() {
			return notice32to88;
		}

		public void setNotice32to88(BigDecimal notice32to88) {
			this.notice32to88 = notice32to88;
		}

		public BigDecimal getCerOfDepo() {
			return cerOfDepo;
		}

		public void setCerOfDepo(BigDecimal cerOfDepo) {
			this.cerOfDepo = cerOfDepo;
		}

		public BigDecimal getImportValue() {
			return importValue;
		}

		public void setImportValue(BigDecimal importValue) {
			this.importValue = importValue;
		}

		public BigDecimal getInvestment() {
			return investment;
		}

		public void setInvestment(BigDecimal investment) {
			this.investment = investment;
		}

		public BigDecimal getOther() {
			return other;
		}

		public void setOther(BigDecimal other) {
			this.other = other;
		}

		public BigDecimal getResidents() {
			return residents;
		}

		public void setResidents(BigDecimal residents) {
			this.residents = residents;
		}

		public BigDecimal getNonResidents() {
			return nonResidents;
		}

		public void setNonResidents(BigDecimal nonResidents) {
			this.nonResidents = nonResidents;
		}

		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

		public String getReportCode() {
			return reportCode;
		}

		public void setReportCode(String reportCode) {
			this.reportCode = reportCode;
		}

	}

	// ------------------------------
	// ENTITY: BRRS_M_DEP3_ARCHIVALTABLE_SUMMARY
	// ------------------------------
	public static class M_DEP3_Archival_Summary_Entity {

		private String r11_foreign_curr_acc_by_curr;
		private BigDecimal r11_ex_rate_buy;
		private BigDecimal r11_ex_rate_mid;
		private BigDecimal r11_ex_rate_sell;
		private BigDecimal r11_current;
		private BigDecimal r11_call;
		private BigDecimal r11_savings;
		private BigDecimal r11_notice_0to31;
		private BigDecimal r11_notice_32to88;
		private BigDecimal r11_fix_depo_91_day_depo;
		private BigDecimal r11_fix_depo_1to2;
		private BigDecimal r11_fix_depo_4to6;
		private BigDecimal r11_fix_depo_7to12;
		private BigDecimal r11_fix_depo_13to18;
		private BigDecimal r11_fix_depo_19to24;
		private BigDecimal r11_fix_depo_over24;
		private BigDecimal r11_cer_of_depo;
		private BigDecimal r11_total;
		private BigDecimal r11_pula_equivalent;
		private BigDecimal r11_avg_pula_equivalent;

		private String r12_foreign_curr_acc_by_curr;
		private BigDecimal r12_ex_rate_buy;
		private BigDecimal r12_ex_rate_mid;
		private BigDecimal r12_ex_rate_sell;
		private BigDecimal r12_current;
		private BigDecimal r12_call;
		private BigDecimal r12_savings;
		private BigDecimal r12_notice_0to31;
		private BigDecimal r12_notice_32to88;
		private BigDecimal r12_fix_depo_91_day_depo;
		private BigDecimal r12_fix_depo_1to2;
		private BigDecimal r12_fix_depo_4to6;
		private BigDecimal r12_fix_depo_7to12;
		private BigDecimal r12_fix_depo_13to18;
		private BigDecimal r12_fix_depo_19to24;
		private BigDecimal r12_fix_depo_over24;
		private BigDecimal r12_cer_of_depo;
		private BigDecimal r12_total;
		private BigDecimal r12_pula_equivalent;
		private BigDecimal r12_avg_pula_equivalent;

		private String r13_foreign_curr_acc_by_curr;
		private BigDecimal r13_ex_rate_buy;
		private BigDecimal r13_ex_rate_mid;
		private BigDecimal r13_ex_rate_sell;
		private BigDecimal r13_current;
		private BigDecimal r13_call;
		private BigDecimal r13_savings;
		private BigDecimal r13_notice_0to31;
		private BigDecimal r13_notice_32to88;
		private BigDecimal r13_fix_depo_91_day_depo;
		private BigDecimal r13_fix_depo_1to2;
		private BigDecimal r13_fix_depo_4to6;
		private BigDecimal r13_fix_depo_7to12;
		private BigDecimal r13_fix_depo_13to18;
		private BigDecimal r13_fix_depo_19to24;
		private BigDecimal r13_fix_depo_over24;
		private BigDecimal r13_cer_of_depo;
		private BigDecimal r13_total;
		private BigDecimal r13_pula_equivalent;
		private BigDecimal r13_avg_pula_equivalent;

		private String r14_foreign_curr_acc_by_curr;
		private BigDecimal r14_ex_rate_buy;
		private BigDecimal r14_ex_rate_mid;
		private BigDecimal r14_ex_rate_sell;
		private BigDecimal r14_current;
		private BigDecimal r14_call;
		private BigDecimal r14_savings;
		private BigDecimal r14_notice_0to31;
		private BigDecimal r14_notice_32to88;
		private BigDecimal r14_fix_depo_91_day_depo;
		private BigDecimal r14_fix_depo_1to2;
		private BigDecimal r14_fix_depo_4to6;
		private BigDecimal r14_fix_depo_7to12;
		private BigDecimal r14_fix_depo_13to18;
		private BigDecimal r14_fix_depo_19to24;
		private BigDecimal r14_fix_depo_over24;
		private BigDecimal r14_cer_of_depo;
		private BigDecimal r14_total;
		private BigDecimal r14_pula_equivalent;
		private BigDecimal r14_avg_pula_equivalent;

		private String r15_foreign_curr_acc_by_curr;
		private BigDecimal r15_ex_rate_buy;
		private BigDecimal r15_ex_rate_mid;
		private BigDecimal r15_ex_rate_sell;
		private BigDecimal r15_current;
		private BigDecimal r15_call;
		private BigDecimal r15_savings;
		private BigDecimal r15_notice_0to31;
		private BigDecimal r15_notice_32to88;
		private BigDecimal r15_fix_depo_91_day_depo;
		private BigDecimal r15_fix_depo_1to2;
		private BigDecimal r15_fix_depo_4to6;
		private BigDecimal r15_fix_depo_7to12;
		private BigDecimal r15_fix_depo_13to18;
		private BigDecimal r15_fix_depo_19to24;
		private BigDecimal r15_fix_depo_over24;
		private BigDecimal r15_cer_of_depo;
		private BigDecimal r15_total;
		private BigDecimal r15_pula_equivalent;
		private BigDecimal r15_avg_pula_equivalent;

		private String r16_foreign_curr_acc_by_curr;
		private BigDecimal r16_ex_rate_buy;
		private BigDecimal r16_ex_rate_mid;
		private BigDecimal r16_ex_rate_sell;
		private BigDecimal r16_current;
		private BigDecimal r16_call;
		private BigDecimal r16_savings;
		private BigDecimal r16_notice_0to31;
		private BigDecimal r16_notice_32to88;
		private BigDecimal r16_fix_depo_91_day_depo;
		private BigDecimal r16_fix_depo_1to2;
		private BigDecimal r16_fix_depo_4to6;
		private BigDecimal r16_fix_depo_7to12;
		private BigDecimal r16_fix_depo_13to18;
		private BigDecimal r16_fix_depo_19to24;
		private BigDecimal r16_fix_depo_over24;
		private BigDecimal r16_cer_of_depo;
		private BigDecimal r16_total;
		private BigDecimal r16_pula_equivalent;
		private BigDecimal r16_avg_pula_equivalent;

		private String r17_foreign_curr_acc_by_curr;
		private BigDecimal r17_ex_rate_buy;
		private BigDecimal r17_ex_rate_mid;
		private BigDecimal r17_ex_rate_sell;
		private BigDecimal r17_current;
		private BigDecimal r17_call;
		private BigDecimal r17_savings;
		private BigDecimal r17_notice_0to31;
		private BigDecimal r17_notice_32to88;
		private BigDecimal r17_fix_depo_91_day_depo;
		private BigDecimal r17_fix_depo_1to2;
		private BigDecimal r17_fix_depo_4to6;
		private BigDecimal r17_fix_depo_7to12;
		private BigDecimal r17_fix_depo_13to18;
		private BigDecimal r17_fix_depo_19to24;
		private BigDecimal r17_fix_depo_over24;
		private BigDecimal r17_cer_of_depo;
		private BigDecimal r17_total;
		private BigDecimal r17_pula_equivalent;
		private BigDecimal r17_avg_pula_equivalent;

		private String r18_foreign_curr_acc_by_curr;
		private BigDecimal r18_ex_rate_buy;
		private BigDecimal r18_ex_rate_mid;
		private BigDecimal r18_ex_rate_sell;
		private BigDecimal r18_current;
		private BigDecimal r18_call;
		private BigDecimal r18_savings;
		private BigDecimal r18_notice_0to31;
		private BigDecimal r18_notice_32to88;
		private BigDecimal r18_fix_depo_91_day_depo;
		private BigDecimal r18_fix_depo_1to2;
		private BigDecimal r18_fix_depo_4to6;
		private BigDecimal r18_fix_depo_7to12;
		private BigDecimal r18_fix_depo_13to18;
		private BigDecimal r18_fix_depo_19to24;
		private BigDecimal r18_fix_depo_over24;
		private BigDecimal r18_cer_of_depo;
		private BigDecimal r18_total;
		private BigDecimal r18_pula_equivalent;
		private BigDecimal r18_avg_pula_equivalent;

		private BigDecimal r28_import;
		private BigDecimal r28_investment;
		private BigDecimal r28_other;

		private BigDecimal r29_import;
		private BigDecimal r29_investment;
		private BigDecimal r29_other;

		private BigDecimal r30_import;
		private BigDecimal r30_investment;
		private BigDecimal r30_other;

		private BigDecimal r31_import;
		private BigDecimal r31_investment;
		private BigDecimal r31_other;

		private BigDecimal r32_import;
		private BigDecimal r32_investment;
		private BigDecimal r32_other;

		private BigDecimal r33_import;
		private BigDecimal r33_investment;
		private BigDecimal r33_other;

		private BigDecimal r34_import;
		private BigDecimal r34_investment;
		private BigDecimal r34_other;

		private BigDecimal r28_residents;
		private BigDecimal r28_non_residents;

		private BigDecimal r29_residents;
		private BigDecimal r29_non_residents;

		private BigDecimal r30_residents;
		private BigDecimal r30_non_residents;

		private BigDecimal r31_residents;
		private BigDecimal r31_non_residents;

		private BigDecimal r32_residents;
		private BigDecimal r32_non_residents;

		private BigDecimal r33_residents;
		private BigDecimal r33_non_residents;

		private BigDecimal r34_residents;
		private BigDecimal r34_non_residents;
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

		public M_DEP3_Archival_Summary_Entity() {
			super();

		}

		public String getR11_foreign_curr_acc_by_curr() {
			return r11_foreign_curr_acc_by_curr;
		}

		public void setR11_foreign_curr_acc_by_curr(String r11_foreign_curr_acc_by_curr) {
			this.r11_foreign_curr_acc_by_curr = r11_foreign_curr_acc_by_curr;
		}

		public BigDecimal getR11_ex_rate_buy() {
			return r11_ex_rate_buy;
		}

		public void setR11_ex_rate_buy(BigDecimal r11_ex_rate_buy) {
			this.r11_ex_rate_buy = r11_ex_rate_buy;
		}

		public BigDecimal getR11_ex_rate_mid() {
			return r11_ex_rate_mid;
		}

		public void setR11_ex_rate_mid(BigDecimal r11_ex_rate_mid) {
			this.r11_ex_rate_mid = r11_ex_rate_mid;
		}

		public BigDecimal getR11_ex_rate_sell() {
			return r11_ex_rate_sell;
		}

		public void setR11_ex_rate_sell(BigDecimal r11_ex_rate_sell) {
			this.r11_ex_rate_sell = r11_ex_rate_sell;
		}

		public BigDecimal getR11_current() {
			return r11_current;
		}

		public void setR11_current(BigDecimal r11_current) {
			this.r11_current = r11_current;
		}

		public BigDecimal getR11_call() {
			return r11_call;
		}

		public void setR11_call(BigDecimal r11_call) {
			this.r11_call = r11_call;
		}

		public BigDecimal getR11_savings() {
			return r11_savings;
		}

		public void setR11_savings(BigDecimal r11_savings) {
			this.r11_savings = r11_savings;
		}

		public BigDecimal getR11_notice_0to31() {
			return r11_notice_0to31;
		}

		public void setR11_notice_0to31(BigDecimal r11_notice_0to31) {
			this.r11_notice_0to31 = r11_notice_0to31;
		}

		public BigDecimal getR11_notice_32to88() {
			return r11_notice_32to88;
		}

		public void setR11_notice_32to88(BigDecimal r11_notice_32to88) {
			this.r11_notice_32to88 = r11_notice_32to88;
		}

		public BigDecimal getR11_fix_depo_91_day_depo() {
			return r11_fix_depo_91_day_depo;
		}

		public void setR11_fix_depo_91_day_depo(BigDecimal r11_fix_depo_91_day_depo) {
			this.r11_fix_depo_91_day_depo = r11_fix_depo_91_day_depo;
		}

		public BigDecimal getR11_fix_depo_1to2() {
			return r11_fix_depo_1to2;
		}

		public void setR11_fix_depo_1to2(BigDecimal r11_fix_depo_1to2) {
			this.r11_fix_depo_1to2 = r11_fix_depo_1to2;
		}

		public BigDecimal getR11_fix_depo_4to6() {
			return r11_fix_depo_4to6;
		}

		public void setR11_fix_depo_4to6(BigDecimal r11_fix_depo_4to6) {
			this.r11_fix_depo_4to6 = r11_fix_depo_4to6;
		}

		public BigDecimal getR11_fix_depo_7to12() {
			return r11_fix_depo_7to12;
		}

		public void setR11_fix_depo_7to12(BigDecimal r11_fix_depo_7to12) {
			this.r11_fix_depo_7to12 = r11_fix_depo_7to12;
		}

		public BigDecimal getR11_fix_depo_13to18() {
			return r11_fix_depo_13to18;
		}

		public void setR11_fix_depo_13to18(BigDecimal r11_fix_depo_13to18) {
			this.r11_fix_depo_13to18 = r11_fix_depo_13to18;
		}

		public BigDecimal getR11_fix_depo_19to24() {
			return r11_fix_depo_19to24;
		}

		public void setR11_fix_depo_19to24(BigDecimal r11_fix_depo_19to24) {
			this.r11_fix_depo_19to24 = r11_fix_depo_19to24;
		}

		public BigDecimal getR11_fix_depo_over24() {
			return r11_fix_depo_over24;
		}

		public void setR11_fix_depo_over24(BigDecimal r11_fix_depo_over24) {
			this.r11_fix_depo_over24 = r11_fix_depo_over24;
		}

		public BigDecimal getR11_cer_of_depo() {
			return r11_cer_of_depo;
		}

		public void setR11_cer_of_depo(BigDecimal r11_cer_of_depo) {
			this.r11_cer_of_depo = r11_cer_of_depo;
		}

		public BigDecimal getR11_total() {
			return r11_total;
		}

		public void setR11_total(BigDecimal r11_total) {
			this.r11_total = r11_total;
		}

		public BigDecimal getR11_pula_equivalent() {
			return r11_pula_equivalent;
		}

		public void setR11_pula_equivalent(BigDecimal r11_pula_equivalent) {
			this.r11_pula_equivalent = r11_pula_equivalent;
		}

		public BigDecimal getR11_avg_pula_equivalent() {
			return r11_avg_pula_equivalent;
		}

		public void setR11_avg_pula_equivalent(BigDecimal r11_avg_pula_equivalent) {
			this.r11_avg_pula_equivalent = r11_avg_pula_equivalent;
		}

		public String getR12_foreign_curr_acc_by_curr() {
			return r12_foreign_curr_acc_by_curr;
		}

		public void setR12_foreign_curr_acc_by_curr(String r12_foreign_curr_acc_by_curr) {
			this.r12_foreign_curr_acc_by_curr = r12_foreign_curr_acc_by_curr;
		}

		public BigDecimal getR12_ex_rate_buy() {
			return r12_ex_rate_buy;
		}

		public void setR12_ex_rate_buy(BigDecimal r12_ex_rate_buy) {
			this.r12_ex_rate_buy = r12_ex_rate_buy;
		}

		public BigDecimal getR12_ex_rate_mid() {
			return r12_ex_rate_mid;
		}

		public void setR12_ex_rate_mid(BigDecimal r12_ex_rate_mid) {
			this.r12_ex_rate_mid = r12_ex_rate_mid;
		}

		public BigDecimal getR12_ex_rate_sell() {
			return r12_ex_rate_sell;
		}

		public void setR12_ex_rate_sell(BigDecimal r12_ex_rate_sell) {
			this.r12_ex_rate_sell = r12_ex_rate_sell;
		}

		public BigDecimal getR12_current() {
			return r12_current;
		}

		public void setR12_current(BigDecimal r12_current) {
			this.r12_current = r12_current;
		}

		public BigDecimal getR12_call() {
			return r12_call;
		}

		public void setR12_call(BigDecimal r12_call) {
			this.r12_call = r12_call;
		}

		public BigDecimal getR12_savings() {
			return r12_savings;
		}

		public void setR12_savings(BigDecimal r12_savings) {
			this.r12_savings = r12_savings;
		}

		public BigDecimal getR12_notice_0to31() {
			return r12_notice_0to31;
		}

		public void setR12_notice_0to31(BigDecimal r12_notice_0to31) {
			this.r12_notice_0to31 = r12_notice_0to31;
		}

		public BigDecimal getR12_notice_32to88() {
			return r12_notice_32to88;
		}

		public void setR12_notice_32to88(BigDecimal r12_notice_32to88) {
			this.r12_notice_32to88 = r12_notice_32to88;
		}

		public BigDecimal getR12_fix_depo_91_day_depo() {
			return r12_fix_depo_91_day_depo;
		}

		public void setR12_fix_depo_91_day_depo(BigDecimal r12_fix_depo_91_day_depo) {
			this.r12_fix_depo_91_day_depo = r12_fix_depo_91_day_depo;
		}

		public BigDecimal getR12_fix_depo_1to2() {
			return r12_fix_depo_1to2;
		}

		public void setR12_fix_depo_1to2(BigDecimal r12_fix_depo_1to2) {
			this.r12_fix_depo_1to2 = r12_fix_depo_1to2;
		}

		public BigDecimal getR12_fix_depo_4to6() {
			return r12_fix_depo_4to6;
		}

		public void setR12_fix_depo_4to6(BigDecimal r12_fix_depo_4to6) {
			this.r12_fix_depo_4to6 = r12_fix_depo_4to6;
		}

		public BigDecimal getR12_fix_depo_7to12() {
			return r12_fix_depo_7to12;
		}

		public void setR12_fix_depo_7to12(BigDecimal r12_fix_depo_7to12) {
			this.r12_fix_depo_7to12 = r12_fix_depo_7to12;
		}

		public BigDecimal getR12_fix_depo_13to18() {
			return r12_fix_depo_13to18;
		}

		public void setR12_fix_depo_13to18(BigDecimal r12_fix_depo_13to18) {
			this.r12_fix_depo_13to18 = r12_fix_depo_13to18;
		}

		public BigDecimal getR12_fix_depo_19to24() {
			return r12_fix_depo_19to24;
		}

		public void setR12_fix_depo_19to24(BigDecimal r12_fix_depo_19to24) {
			this.r12_fix_depo_19to24 = r12_fix_depo_19to24;
		}

		public BigDecimal getR12_fix_depo_over24() {
			return r12_fix_depo_over24;
		}

		public void setR12_fix_depo_over24(BigDecimal r12_fix_depo_over24) {
			this.r12_fix_depo_over24 = r12_fix_depo_over24;
		}

		public BigDecimal getR12_cer_of_depo() {
			return r12_cer_of_depo;
		}

		public void setR12_cer_of_depo(BigDecimal r12_cer_of_depo) {
			this.r12_cer_of_depo = r12_cer_of_depo;
		}

		public BigDecimal getR12_total() {
			return r12_total;
		}

		public void setR12_total(BigDecimal r12_total) {
			this.r12_total = r12_total;
		}

		public BigDecimal getR12_pula_equivalent() {
			return r12_pula_equivalent;
		}

		public void setR12_pula_equivalent(BigDecimal r12_pula_equivalent) {
			this.r12_pula_equivalent = r12_pula_equivalent;
		}

		public BigDecimal getR12_avg_pula_equivalent() {
			return r12_avg_pula_equivalent;
		}

		public void setR12_avg_pula_equivalent(BigDecimal r12_avg_pula_equivalent) {
			this.r12_avg_pula_equivalent = r12_avg_pula_equivalent;
		}

		public String getR13_foreign_curr_acc_by_curr() {
			return r13_foreign_curr_acc_by_curr;
		}

		public void setR13_foreign_curr_acc_by_curr(String r13_foreign_curr_acc_by_curr) {
			this.r13_foreign_curr_acc_by_curr = r13_foreign_curr_acc_by_curr;
		}

		public BigDecimal getR13_ex_rate_buy() {
			return r13_ex_rate_buy;
		}

		public void setR13_ex_rate_buy(BigDecimal r13_ex_rate_buy) {
			this.r13_ex_rate_buy = r13_ex_rate_buy;
		}

		public BigDecimal getR13_ex_rate_mid() {
			return r13_ex_rate_mid;
		}

		public void setR13_ex_rate_mid(BigDecimal r13_ex_rate_mid) {
			this.r13_ex_rate_mid = r13_ex_rate_mid;
		}

		public BigDecimal getR13_ex_rate_sell() {
			return r13_ex_rate_sell;
		}

		public void setR13_ex_rate_sell(BigDecimal r13_ex_rate_sell) {
			this.r13_ex_rate_sell = r13_ex_rate_sell;
		}

		public BigDecimal getR13_current() {
			return r13_current;
		}

		public void setR13_current(BigDecimal r13_current) {
			this.r13_current = r13_current;
		}

		public BigDecimal getR13_call() {
			return r13_call;
		}

		public void setR13_call(BigDecimal r13_call) {
			this.r13_call = r13_call;
		}

		public BigDecimal getR13_savings() {
			return r13_savings;
		}

		public void setR13_savings(BigDecimal r13_savings) {
			this.r13_savings = r13_savings;
		}

		public BigDecimal getR13_notice_0to31() {
			return r13_notice_0to31;
		}

		public void setR13_notice_0to31(BigDecimal r13_notice_0to31) {
			this.r13_notice_0to31 = r13_notice_0to31;
		}

		public BigDecimal getR13_notice_32to88() {
			return r13_notice_32to88;
		}

		public void setR13_notice_32to88(BigDecimal r13_notice_32to88) {
			this.r13_notice_32to88 = r13_notice_32to88;
		}

		public BigDecimal getR13_fix_depo_91_day_depo() {
			return r13_fix_depo_91_day_depo;
		}

		public void setR13_fix_depo_91_day_depo(BigDecimal r13_fix_depo_91_day_depo) {
			this.r13_fix_depo_91_day_depo = r13_fix_depo_91_day_depo;
		}

		public BigDecimal getR13_fix_depo_1to2() {
			return r13_fix_depo_1to2;
		}

		public void setR13_fix_depo_1to2(BigDecimal r13_fix_depo_1to2) {
			this.r13_fix_depo_1to2 = r13_fix_depo_1to2;
		}

		public BigDecimal getR13_fix_depo_4to6() {
			return r13_fix_depo_4to6;
		}

		public void setR13_fix_depo_4to6(BigDecimal r13_fix_depo_4to6) {
			this.r13_fix_depo_4to6 = r13_fix_depo_4to6;
		}

		public BigDecimal getR13_fix_depo_7to12() {
			return r13_fix_depo_7to12;
		}

		public void setR13_fix_depo_7to12(BigDecimal r13_fix_depo_7to12) {
			this.r13_fix_depo_7to12 = r13_fix_depo_7to12;
		}

		public BigDecimal getR13_fix_depo_13to18() {
			return r13_fix_depo_13to18;
		}

		public void setR13_fix_depo_13to18(BigDecimal r13_fix_depo_13to18) {
			this.r13_fix_depo_13to18 = r13_fix_depo_13to18;
		}

		public BigDecimal getR13_fix_depo_19to24() {
			return r13_fix_depo_19to24;
		}

		public void setR13_fix_depo_19to24(BigDecimal r13_fix_depo_19to24) {
			this.r13_fix_depo_19to24 = r13_fix_depo_19to24;
		}

		public BigDecimal getR13_fix_depo_over24() {
			return r13_fix_depo_over24;
		}

		public void setR13_fix_depo_over24(BigDecimal r13_fix_depo_over24) {
			this.r13_fix_depo_over24 = r13_fix_depo_over24;
		}

		public BigDecimal getR13_cer_of_depo() {
			return r13_cer_of_depo;
		}

		public void setR13_cer_of_depo(BigDecimal r13_cer_of_depo) {
			this.r13_cer_of_depo = r13_cer_of_depo;
		}

		public BigDecimal getR13_total() {
			return r13_total;
		}

		public void setR13_total(BigDecimal r13_total) {
			this.r13_total = r13_total;
		}

		public BigDecimal getR13_pula_equivalent() {
			return r13_pula_equivalent;
		}

		public void setR13_pula_equivalent(BigDecimal r13_pula_equivalent) {
			this.r13_pula_equivalent = r13_pula_equivalent;
		}

		public BigDecimal getR13_avg_pula_equivalent() {
			return r13_avg_pula_equivalent;
		}

		public void setR13_avg_pula_equivalent(BigDecimal r13_avg_pula_equivalent) {
			this.r13_avg_pula_equivalent = r13_avg_pula_equivalent;
		}

		public String getR14_foreign_curr_acc_by_curr() {
			return r14_foreign_curr_acc_by_curr;
		}

		public void setR14_foreign_curr_acc_by_curr(String r14_foreign_curr_acc_by_curr) {
			this.r14_foreign_curr_acc_by_curr = r14_foreign_curr_acc_by_curr;
		}

		public BigDecimal getR14_ex_rate_buy() {
			return r14_ex_rate_buy;
		}

		public void setR14_ex_rate_buy(BigDecimal r14_ex_rate_buy) {
			this.r14_ex_rate_buy = r14_ex_rate_buy;
		}

		public BigDecimal getR14_ex_rate_mid() {
			return r14_ex_rate_mid;
		}

		public void setR14_ex_rate_mid(BigDecimal r14_ex_rate_mid) {
			this.r14_ex_rate_mid = r14_ex_rate_mid;
		}

		public BigDecimal getR14_ex_rate_sell() {
			return r14_ex_rate_sell;
		}

		public void setR14_ex_rate_sell(BigDecimal r14_ex_rate_sell) {
			this.r14_ex_rate_sell = r14_ex_rate_sell;
		}

		public BigDecimal getR14_current() {
			return r14_current;
		}

		public void setR14_current(BigDecimal r14_current) {
			this.r14_current = r14_current;
		}

		public BigDecimal getR14_call() {
			return r14_call;
		}

		public void setR14_call(BigDecimal r14_call) {
			this.r14_call = r14_call;
		}

		public BigDecimal getR14_savings() {
			return r14_savings;
		}

		public void setR14_savings(BigDecimal r14_savings) {
			this.r14_savings = r14_savings;
		}

		public BigDecimal getR14_notice_0to31() {
			return r14_notice_0to31;
		}

		public void setR14_notice_0to31(BigDecimal r14_notice_0to31) {
			this.r14_notice_0to31 = r14_notice_0to31;
		}

		public BigDecimal getR14_notice_32to88() {
			return r14_notice_32to88;
		}

		public void setR14_notice_32to88(BigDecimal r14_notice_32to88) {
			this.r14_notice_32to88 = r14_notice_32to88;
		}

		public BigDecimal getR14_fix_depo_91_day_depo() {
			return r14_fix_depo_91_day_depo;
		}

		public void setR14_fix_depo_91_day_depo(BigDecimal r14_fix_depo_91_day_depo) {
			this.r14_fix_depo_91_day_depo = r14_fix_depo_91_day_depo;
		}

		public BigDecimal getR14_fix_depo_1to2() {
			return r14_fix_depo_1to2;
		}

		public void setR14_fix_depo_1to2(BigDecimal r14_fix_depo_1to2) {
			this.r14_fix_depo_1to2 = r14_fix_depo_1to2;
		}

		public BigDecimal getR14_fix_depo_4to6() {
			return r14_fix_depo_4to6;
		}

		public void setR14_fix_depo_4to6(BigDecimal r14_fix_depo_4to6) {
			this.r14_fix_depo_4to6 = r14_fix_depo_4to6;
		}

		public BigDecimal getR14_fix_depo_7to12() {
			return r14_fix_depo_7to12;
		}

		public void setR14_fix_depo_7to12(BigDecimal r14_fix_depo_7to12) {
			this.r14_fix_depo_7to12 = r14_fix_depo_7to12;
		}

		public BigDecimal getR14_fix_depo_13to18() {
			return r14_fix_depo_13to18;
		}

		public void setR14_fix_depo_13to18(BigDecimal r14_fix_depo_13to18) {
			this.r14_fix_depo_13to18 = r14_fix_depo_13to18;
		}

		public BigDecimal getR14_fix_depo_19to24() {
			return r14_fix_depo_19to24;
		}

		public void setR14_fix_depo_19to24(BigDecimal r14_fix_depo_19to24) {
			this.r14_fix_depo_19to24 = r14_fix_depo_19to24;
		}

		public BigDecimal getR14_fix_depo_over24() {
			return r14_fix_depo_over24;
		}

		public void setR14_fix_depo_over24(BigDecimal r14_fix_depo_over24) {
			this.r14_fix_depo_over24 = r14_fix_depo_over24;
		}

		public BigDecimal getR14_cer_of_depo() {
			return r14_cer_of_depo;
		}

		public void setR14_cer_of_depo(BigDecimal r14_cer_of_depo) {
			this.r14_cer_of_depo = r14_cer_of_depo;
		}

		public BigDecimal getR14_total() {
			return r14_total;
		}

		public void setR14_total(BigDecimal r14_total) {
			this.r14_total = r14_total;
		}

		public BigDecimal getR14_pula_equivalent() {
			return r14_pula_equivalent;
		}

		public void setR14_pula_equivalent(BigDecimal r14_pula_equivalent) {
			this.r14_pula_equivalent = r14_pula_equivalent;
		}

		public BigDecimal getR14_avg_pula_equivalent() {
			return r14_avg_pula_equivalent;
		}

		public void setR14_avg_pula_equivalent(BigDecimal r14_avg_pula_equivalent) {
			this.r14_avg_pula_equivalent = r14_avg_pula_equivalent;
		}

		public String getR15_foreign_curr_acc_by_curr() {
			return r15_foreign_curr_acc_by_curr;
		}

		public void setR15_foreign_curr_acc_by_curr(String r15_foreign_curr_acc_by_curr) {
			this.r15_foreign_curr_acc_by_curr = r15_foreign_curr_acc_by_curr;
		}

		public BigDecimal getR15_ex_rate_buy() {
			return r15_ex_rate_buy;
		}

		public void setR15_ex_rate_buy(BigDecimal r15_ex_rate_buy) {
			this.r15_ex_rate_buy = r15_ex_rate_buy;
		}

		public BigDecimal getR15_ex_rate_mid() {
			return r15_ex_rate_mid;
		}

		public void setR15_ex_rate_mid(BigDecimal r15_ex_rate_mid) {
			this.r15_ex_rate_mid = r15_ex_rate_mid;
		}

		public BigDecimal getR15_ex_rate_sell() {
			return r15_ex_rate_sell;
		}

		public void setR15_ex_rate_sell(BigDecimal r15_ex_rate_sell) {
			this.r15_ex_rate_sell = r15_ex_rate_sell;
		}

		public BigDecimal getR15_current() {
			return r15_current;
		}

		public void setR15_current(BigDecimal r15_current) {
			this.r15_current = r15_current;
		}

		public BigDecimal getR15_call() {
			return r15_call;
		}

		public void setR15_call(BigDecimal r15_call) {
			this.r15_call = r15_call;
		}

		public BigDecimal getR15_savings() {
			return r15_savings;
		}

		public void setR15_savings(BigDecimal r15_savings) {
			this.r15_savings = r15_savings;
		}

		public BigDecimal getR15_notice_0to31() {
			return r15_notice_0to31;
		}

		public void setR15_notice_0to31(BigDecimal r15_notice_0to31) {
			this.r15_notice_0to31 = r15_notice_0to31;
		}

		public BigDecimal getR15_notice_32to88() {
			return r15_notice_32to88;
		}

		public void setR15_notice_32to88(BigDecimal r15_notice_32to88) {
			this.r15_notice_32to88 = r15_notice_32to88;
		}

		public BigDecimal getR15_fix_depo_91_day_depo() {
			return r15_fix_depo_91_day_depo;
		}

		public void setR15_fix_depo_91_day_depo(BigDecimal r15_fix_depo_91_day_depo) {
			this.r15_fix_depo_91_day_depo = r15_fix_depo_91_day_depo;
		}

		public BigDecimal getR15_fix_depo_1to2() {
			return r15_fix_depo_1to2;
		}

		public void setR15_fix_depo_1to2(BigDecimal r15_fix_depo_1to2) {
			this.r15_fix_depo_1to2 = r15_fix_depo_1to2;
		}

		public BigDecimal getR15_fix_depo_4to6() {
			return r15_fix_depo_4to6;
		}

		public void setR15_fix_depo_4to6(BigDecimal r15_fix_depo_4to6) {
			this.r15_fix_depo_4to6 = r15_fix_depo_4to6;
		}

		public BigDecimal getR15_fix_depo_7to12() {
			return r15_fix_depo_7to12;
		}

		public void setR15_fix_depo_7to12(BigDecimal r15_fix_depo_7to12) {
			this.r15_fix_depo_7to12 = r15_fix_depo_7to12;
		}

		public BigDecimal getR15_fix_depo_13to18() {
			return r15_fix_depo_13to18;
		}

		public void setR15_fix_depo_13to18(BigDecimal r15_fix_depo_13to18) {
			this.r15_fix_depo_13to18 = r15_fix_depo_13to18;
		}

		public BigDecimal getR15_fix_depo_19to24() {
			return r15_fix_depo_19to24;
		}

		public void setR15_fix_depo_19to24(BigDecimal r15_fix_depo_19to24) {
			this.r15_fix_depo_19to24 = r15_fix_depo_19to24;
		}

		public BigDecimal getR15_fix_depo_over24() {
			return r15_fix_depo_over24;
		}

		public void setR15_fix_depo_over24(BigDecimal r15_fix_depo_over24) {
			this.r15_fix_depo_over24 = r15_fix_depo_over24;
		}

		public BigDecimal getR15_cer_of_depo() {
			return r15_cer_of_depo;
		}

		public void setR15_cer_of_depo(BigDecimal r15_cer_of_depo) {
			this.r15_cer_of_depo = r15_cer_of_depo;
		}

		public BigDecimal getR15_total() {
			return r15_total;
		}

		public void setR15_total(BigDecimal r15_total) {
			this.r15_total = r15_total;
		}

		public BigDecimal getR15_pula_equivalent() {
			return r15_pula_equivalent;
		}

		public void setR15_pula_equivalent(BigDecimal r15_pula_equivalent) {
			this.r15_pula_equivalent = r15_pula_equivalent;
		}

		public BigDecimal getR15_avg_pula_equivalent() {
			return r15_avg_pula_equivalent;
		}

		public void setR15_avg_pula_equivalent(BigDecimal r15_avg_pula_equivalent) {
			this.r15_avg_pula_equivalent = r15_avg_pula_equivalent;
		}

		public String getR16_foreign_curr_acc_by_curr() {
			return r16_foreign_curr_acc_by_curr;
		}

		public void setR16_foreign_curr_acc_by_curr(String r16_foreign_curr_acc_by_curr) {
			this.r16_foreign_curr_acc_by_curr = r16_foreign_curr_acc_by_curr;
		}

		public BigDecimal getR16_ex_rate_buy() {
			return r16_ex_rate_buy;
		}

		public void setR16_ex_rate_buy(BigDecimal r16_ex_rate_buy) {
			this.r16_ex_rate_buy = r16_ex_rate_buy;
		}

		public BigDecimal getR16_ex_rate_mid() {
			return r16_ex_rate_mid;
		}

		public void setR16_ex_rate_mid(BigDecimal r16_ex_rate_mid) {
			this.r16_ex_rate_mid = r16_ex_rate_mid;
		}

		public BigDecimal getR16_ex_rate_sell() {
			return r16_ex_rate_sell;
		}

		public void setR16_ex_rate_sell(BigDecimal r16_ex_rate_sell) {
			this.r16_ex_rate_sell = r16_ex_rate_sell;
		}

		public BigDecimal getR16_current() {
			return r16_current;
		}

		public void setR16_current(BigDecimal r16_current) {
			this.r16_current = r16_current;
		}

		public BigDecimal getR16_call() {
			return r16_call;
		}

		public void setR16_call(BigDecimal r16_call) {
			this.r16_call = r16_call;
		}

		public BigDecimal getR16_savings() {
			return r16_savings;
		}

		public void setR16_savings(BigDecimal r16_savings) {
			this.r16_savings = r16_savings;
		}

		public BigDecimal getR16_notice_0to31() {
			return r16_notice_0to31;
		}

		public void setR16_notice_0to31(BigDecimal r16_notice_0to31) {
			this.r16_notice_0to31 = r16_notice_0to31;
		}

		public BigDecimal getR16_notice_32to88() {
			return r16_notice_32to88;
		}

		public void setR16_notice_32to88(BigDecimal r16_notice_32to88) {
			this.r16_notice_32to88 = r16_notice_32to88;
		}

		public BigDecimal getR16_fix_depo_91_day_depo() {
			return r16_fix_depo_91_day_depo;
		}

		public void setR16_fix_depo_91_day_depo(BigDecimal r16_fix_depo_91_day_depo) {
			this.r16_fix_depo_91_day_depo = r16_fix_depo_91_day_depo;
		}

		public BigDecimal getR16_fix_depo_1to2() {
			return r16_fix_depo_1to2;
		}

		public void setR16_fix_depo_1to2(BigDecimal r16_fix_depo_1to2) {
			this.r16_fix_depo_1to2 = r16_fix_depo_1to2;
		}

		public BigDecimal getR16_fix_depo_4to6() {
			return r16_fix_depo_4to6;
		}

		public void setR16_fix_depo_4to6(BigDecimal r16_fix_depo_4to6) {
			this.r16_fix_depo_4to6 = r16_fix_depo_4to6;
		}

		public BigDecimal getR16_fix_depo_7to12() {
			return r16_fix_depo_7to12;
		}

		public void setR16_fix_depo_7to12(BigDecimal r16_fix_depo_7to12) {
			this.r16_fix_depo_7to12 = r16_fix_depo_7to12;
		}

		public BigDecimal getR16_fix_depo_13to18() {
			return r16_fix_depo_13to18;
		}

		public void setR16_fix_depo_13to18(BigDecimal r16_fix_depo_13to18) {
			this.r16_fix_depo_13to18 = r16_fix_depo_13to18;
		}

		public BigDecimal getR16_fix_depo_19to24() {
			return r16_fix_depo_19to24;
		}

		public void setR16_fix_depo_19to24(BigDecimal r16_fix_depo_19to24) {
			this.r16_fix_depo_19to24 = r16_fix_depo_19to24;
		}

		public BigDecimal getR16_fix_depo_over24() {
			return r16_fix_depo_over24;
		}

		public void setR16_fix_depo_over24(BigDecimal r16_fix_depo_over24) {
			this.r16_fix_depo_over24 = r16_fix_depo_over24;
		}

		public BigDecimal getR16_cer_of_depo() {
			return r16_cer_of_depo;
		}

		public void setR16_cer_of_depo(BigDecimal r16_cer_of_depo) {
			this.r16_cer_of_depo = r16_cer_of_depo;
		}

		public BigDecimal getR16_total() {
			return r16_total;
		}

		public void setR16_total(BigDecimal r16_total) {
			this.r16_total = r16_total;
		}

		public BigDecimal getR16_pula_equivalent() {
			return r16_pula_equivalent;
		}

		public void setR16_pula_equivalent(BigDecimal r16_pula_equivalent) {
			this.r16_pula_equivalent = r16_pula_equivalent;
		}

		public BigDecimal getR16_avg_pula_equivalent() {
			return r16_avg_pula_equivalent;
		}

		public void setR16_avg_pula_equivalent(BigDecimal r16_avg_pula_equivalent) {
			this.r16_avg_pula_equivalent = r16_avg_pula_equivalent;
		}

		public String getR17_foreign_curr_acc_by_curr() {
			return r17_foreign_curr_acc_by_curr;
		}

		public void setR17_foreign_curr_acc_by_curr(String r17_foreign_curr_acc_by_curr) {
			this.r17_foreign_curr_acc_by_curr = r17_foreign_curr_acc_by_curr;
		}

		public BigDecimal getR17_ex_rate_buy() {
			return r17_ex_rate_buy;
		}

		public void setR17_ex_rate_buy(BigDecimal r17_ex_rate_buy) {
			this.r17_ex_rate_buy = r17_ex_rate_buy;
		}

		public BigDecimal getR17_ex_rate_mid() {
			return r17_ex_rate_mid;
		}

		public void setR17_ex_rate_mid(BigDecimal r17_ex_rate_mid) {
			this.r17_ex_rate_mid = r17_ex_rate_mid;
		}

		public BigDecimal getR17_ex_rate_sell() {
			return r17_ex_rate_sell;
		}

		public void setR17_ex_rate_sell(BigDecimal r17_ex_rate_sell) {
			this.r17_ex_rate_sell = r17_ex_rate_sell;
		}

		public BigDecimal getR17_current() {
			return r17_current;
		}

		public void setR17_current(BigDecimal r17_current) {
			this.r17_current = r17_current;
		}

		public BigDecimal getR17_call() {
			return r17_call;
		}

		public void setR17_call(BigDecimal r17_call) {
			this.r17_call = r17_call;
		}

		public BigDecimal getR17_savings() {
			return r17_savings;
		}

		public void setR17_savings(BigDecimal r17_savings) {
			this.r17_savings = r17_savings;
		}

		public BigDecimal getR17_notice_0to31() {
			return r17_notice_0to31;
		}

		public void setR17_notice_0to31(BigDecimal r17_notice_0to31) {
			this.r17_notice_0to31 = r17_notice_0to31;
		}

		public BigDecimal getR17_notice_32to88() {
			return r17_notice_32to88;
		}

		public void setR17_notice_32to88(BigDecimal r17_notice_32to88) {
			this.r17_notice_32to88 = r17_notice_32to88;
		}

		public BigDecimal getR17_fix_depo_91_day_depo() {
			return r17_fix_depo_91_day_depo;
		}

		public void setR17_fix_depo_91_day_depo(BigDecimal r17_fix_depo_91_day_depo) {
			this.r17_fix_depo_91_day_depo = r17_fix_depo_91_day_depo;
		}

		public BigDecimal getR17_fix_depo_1to2() {
			return r17_fix_depo_1to2;
		}

		public void setR17_fix_depo_1to2(BigDecimal r17_fix_depo_1to2) {
			this.r17_fix_depo_1to2 = r17_fix_depo_1to2;
		}

		public BigDecimal getR17_fix_depo_4to6() {
			return r17_fix_depo_4to6;
		}

		public void setR17_fix_depo_4to6(BigDecimal r17_fix_depo_4to6) {
			this.r17_fix_depo_4to6 = r17_fix_depo_4to6;
		}

		public BigDecimal getR17_fix_depo_7to12() {
			return r17_fix_depo_7to12;
		}

		public void setR17_fix_depo_7to12(BigDecimal r17_fix_depo_7to12) {
			this.r17_fix_depo_7to12 = r17_fix_depo_7to12;
		}

		public BigDecimal getR17_fix_depo_13to18() {
			return r17_fix_depo_13to18;
		}

		public void setR17_fix_depo_13to18(BigDecimal r17_fix_depo_13to18) {
			this.r17_fix_depo_13to18 = r17_fix_depo_13to18;
		}

		public BigDecimal getR17_fix_depo_19to24() {
			return r17_fix_depo_19to24;
		}

		public void setR17_fix_depo_19to24(BigDecimal r17_fix_depo_19to24) {
			this.r17_fix_depo_19to24 = r17_fix_depo_19to24;
		}

		public BigDecimal getR17_fix_depo_over24() {
			return r17_fix_depo_over24;
		}

		public void setR17_fix_depo_over24(BigDecimal r17_fix_depo_over24) {
			this.r17_fix_depo_over24 = r17_fix_depo_over24;
		}

		public BigDecimal getR17_cer_of_depo() {
			return r17_cer_of_depo;
		}

		public void setR17_cer_of_depo(BigDecimal r17_cer_of_depo) {
			this.r17_cer_of_depo = r17_cer_of_depo;
		}

		public BigDecimal getR17_total() {
			return r17_total;
		}

		public void setR17_total(BigDecimal r17_total) {
			this.r17_total = r17_total;
		}

		public BigDecimal getR17_pula_equivalent() {
			return r17_pula_equivalent;
		}

		public void setR17_pula_equivalent(BigDecimal r17_pula_equivalent) {
			this.r17_pula_equivalent = r17_pula_equivalent;
		}

		public BigDecimal getR17_avg_pula_equivalent() {
			return r17_avg_pula_equivalent;
		}

		public void setR17_avg_pula_equivalent(BigDecimal r17_avg_pula_equivalent) {
			this.r17_avg_pula_equivalent = r17_avg_pula_equivalent;
		}

		public String getR18_foreign_curr_acc_by_curr() {
			return r18_foreign_curr_acc_by_curr;
		}

		public void setR18_foreign_curr_acc_by_curr(String r18_foreign_curr_acc_by_curr) {
			this.r18_foreign_curr_acc_by_curr = r18_foreign_curr_acc_by_curr;
		}

		public BigDecimal getR18_ex_rate_buy() {
			return r18_ex_rate_buy;
		}

		public void setR18_ex_rate_buy(BigDecimal r18_ex_rate_buy) {
			this.r18_ex_rate_buy = r18_ex_rate_buy;
		}

		public BigDecimal getR18_ex_rate_mid() {
			return r18_ex_rate_mid;
		}

		public void setR18_ex_rate_mid(BigDecimal r18_ex_rate_mid) {
			this.r18_ex_rate_mid = r18_ex_rate_mid;
		}

		public BigDecimal getR18_ex_rate_sell() {
			return r18_ex_rate_sell;
		}

		public void setR18_ex_rate_sell(BigDecimal r18_ex_rate_sell) {
			this.r18_ex_rate_sell = r18_ex_rate_sell;
		}

		public BigDecimal getR18_current() {
			return r18_current;
		}

		public void setR18_current(BigDecimal r18_current) {
			this.r18_current = r18_current;
		}

		public BigDecimal getR18_call() {
			return r18_call;
		}

		public void setR18_call(BigDecimal r18_call) {
			this.r18_call = r18_call;
		}

		public BigDecimal getR18_savings() {
			return r18_savings;
		}

		public void setR18_savings(BigDecimal r18_savings) {
			this.r18_savings = r18_savings;
		}

		public BigDecimal getR18_notice_0to31() {
			return r18_notice_0to31;
		}

		public void setR18_notice_0to31(BigDecimal r18_notice_0to31) {
			this.r18_notice_0to31 = r18_notice_0to31;
		}

		public BigDecimal getR18_notice_32to88() {
			return r18_notice_32to88;
		}

		public void setR18_notice_32to88(BigDecimal r18_notice_32to88) {
			this.r18_notice_32to88 = r18_notice_32to88;
		}

		public BigDecimal getR18_fix_depo_91_day_depo() {
			return r18_fix_depo_91_day_depo;
		}

		public void setR18_fix_depo_91_day_depo(BigDecimal r18_fix_depo_91_day_depo) {
			this.r18_fix_depo_91_day_depo = r18_fix_depo_91_day_depo;
		}

		public BigDecimal getR18_fix_depo_1to2() {
			return r18_fix_depo_1to2;
		}

		public void setR18_fix_depo_1to2(BigDecimal r18_fix_depo_1to2) {
			this.r18_fix_depo_1to2 = r18_fix_depo_1to2;
		}

		public BigDecimal getR18_fix_depo_4to6() {
			return r18_fix_depo_4to6;
		}

		public void setR18_fix_depo_4to6(BigDecimal r18_fix_depo_4to6) {
			this.r18_fix_depo_4to6 = r18_fix_depo_4to6;
		}

		public BigDecimal getR18_fix_depo_7to12() {
			return r18_fix_depo_7to12;
		}

		public void setR18_fix_depo_7to12(BigDecimal r18_fix_depo_7to12) {
			this.r18_fix_depo_7to12 = r18_fix_depo_7to12;
		}

		public BigDecimal getR18_fix_depo_13to18() {
			return r18_fix_depo_13to18;
		}

		public void setR18_fix_depo_13to18(BigDecimal r18_fix_depo_13to18) {
			this.r18_fix_depo_13to18 = r18_fix_depo_13to18;
		}

		public BigDecimal getR18_fix_depo_19to24() {
			return r18_fix_depo_19to24;
		}

		public void setR18_fix_depo_19to24(BigDecimal r18_fix_depo_19to24) {
			this.r18_fix_depo_19to24 = r18_fix_depo_19to24;
		}

		public BigDecimal getR18_fix_depo_over24() {
			return r18_fix_depo_over24;
		}

		public void setR18_fix_depo_over24(BigDecimal r18_fix_depo_over24) {
			this.r18_fix_depo_over24 = r18_fix_depo_over24;
		}

		public BigDecimal getR18_cer_of_depo() {
			return r18_cer_of_depo;
		}

		public void setR18_cer_of_depo(BigDecimal r18_cer_of_depo) {
			this.r18_cer_of_depo = r18_cer_of_depo;
		}

		public BigDecimal getR18_total() {
			return r18_total;
		}

		public void setR18_total(BigDecimal r18_total) {
			this.r18_total = r18_total;
		}

		public BigDecimal getR18_pula_equivalent() {
			return r18_pula_equivalent;
		}

		public void setR18_pula_equivalent(BigDecimal r18_pula_equivalent) {
			this.r18_pula_equivalent = r18_pula_equivalent;
		}

		public BigDecimal getR18_avg_pula_equivalent() {
			return r18_avg_pula_equivalent;
		}

		public void setR18_avg_pula_equivalent(BigDecimal r18_avg_pula_equivalent) {
			this.r18_avg_pula_equivalent = r18_avg_pula_equivalent;
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

		public BigDecimal getR28_import() {
			return r28_import;
		}

		public void setR28_import(BigDecimal r28_import) {
			this.r28_import = r28_import;
		}

		public BigDecimal getR28_investment() {
			return r28_investment;
		}

		public void setR28_investment(BigDecimal r28_investment) {
			this.r28_investment = r28_investment;
		}

		public BigDecimal getR28_other() {
			return r28_other;
		}

		public void setR28_other(BigDecimal r28_other) {
			this.r28_other = r28_other;
		}

		public BigDecimal getR29_import() {
			return r29_import;
		}

		public void setR29_import(BigDecimal r29_import) {
			this.r29_import = r29_import;
		}

		public BigDecimal getR29_investment() {
			return r29_investment;
		}

		public void setR29_investment(BigDecimal r29_investment) {
			this.r29_investment = r29_investment;
		}

		public BigDecimal getR29_other() {
			return r29_other;
		}

		public void setR29_other(BigDecimal r29_other) {
			this.r29_other = r29_other;
		}

		public BigDecimal getR30_import() {
			return r30_import;
		}

		public void setR30_import(BigDecimal r30_import) {
			this.r30_import = r30_import;
		}

		public BigDecimal getR30_investment() {
			return r30_investment;
		}

		public void setR30_investment(BigDecimal r30_investment) {
			this.r30_investment = r30_investment;
		}

		public BigDecimal getR30_other() {
			return r30_other;
		}

		public void setR30_other(BigDecimal r30_other) {
			this.r30_other = r30_other;
		}

		public BigDecimal getR31_import() {
			return r31_import;
		}

		public void setR31_import(BigDecimal r31_import) {
			this.r31_import = r31_import;
		}

		public BigDecimal getR31_investment() {
			return r31_investment;
		}

		public void setR31_investment(BigDecimal r31_investment) {
			this.r31_investment = r31_investment;
		}

		public BigDecimal getR31_other() {
			return r31_other;
		}

		public void setR31_other(BigDecimal r31_other) {
			this.r31_other = r31_other;
		}

		public BigDecimal getR32_import() {
			return r32_import;
		}

		public void setR32_import(BigDecimal r32_import) {
			this.r32_import = r32_import;
		}

		public BigDecimal getR32_investment() {
			return r32_investment;
		}

		public void setR32_investment(BigDecimal r32_investment) {
			this.r32_investment = r32_investment;
		}

		public BigDecimal getR32_other() {
			return r32_other;
		}

		public void setR32_other(BigDecimal r32_other) {
			this.r32_other = r32_other;
		}

		public BigDecimal getR33_import() {
			return r33_import;
		}

		public void setR33_import(BigDecimal r33_import) {
			this.r33_import = r33_import;
		}

		public BigDecimal getR33_investment() {
			return r33_investment;
		}

		public void setR33_investment(BigDecimal r33_investment) {
			this.r33_investment = r33_investment;
		}

		public BigDecimal getR33_other() {
			return r33_other;
		}

		public void setR33_other(BigDecimal r33_other) {
			this.r33_other = r33_other;
		}

		public BigDecimal getR34_import() {
			return r34_import;
		}

		public void setR34_import(BigDecimal r34_import) {
			this.r34_import = r34_import;
		}

		public BigDecimal getR34_investment() {
			return r34_investment;
		}

		public void setR34_investment(BigDecimal r34_investment) {
			this.r34_investment = r34_investment;
		}

		public BigDecimal getR34_other() {
			return r34_other;
		}

		public void setR34_other(BigDecimal r34_other) {
			this.r34_other = r34_other;
		}

		public BigDecimal getR28_residents() {
			return r28_residents;
		}

		public void setR28_residents(BigDecimal r28_residents) {
			this.r28_residents = r28_residents;
		}

		public BigDecimal getR28_non_residents() {
			return r28_non_residents;
		}

		public void setR28_non_residents(BigDecimal r28_non_residents) {
			this.r28_non_residents = r28_non_residents;
		}

		public BigDecimal getR29_residents() {
			return r29_residents;
		}

		public void setR29_residents(BigDecimal r29_residents) {
			this.r29_residents = r29_residents;
		}

		public BigDecimal getR29_non_residents() {
			return r29_non_residents;
		}

		public void setR29_non_residents(BigDecimal r29_non_residents) {
			this.r29_non_residents = r29_non_residents;
		}

		public BigDecimal getR30_residents() {
			return r30_residents;
		}

		public void setR30_residents(BigDecimal r30_residents) {
			this.r30_residents = r30_residents;
		}

		public BigDecimal getR30_non_residents() {
			return r30_non_residents;
		}

		public void setR30_non_residents(BigDecimal r30_non_residents) {
			this.r30_non_residents = r30_non_residents;
		}

		public BigDecimal getR31_residents() {
			return r31_residents;
		}

		public void setR31_residents(BigDecimal r31_residents) {
			this.r31_residents = r31_residents;
		}

		public BigDecimal getR31_non_residents() {
			return r31_non_residents;
		}

		public void setR31_non_residents(BigDecimal r31_non_residents) {
			this.r31_non_residents = r31_non_residents;
		}

		public BigDecimal getR32_residents() {
			return r32_residents;
		}

		public void setR32_residents(BigDecimal r32_residents) {
			this.r32_residents = r32_residents;
		}

		public BigDecimal getR32_non_residents() {
			return r32_non_residents;
		}

		public void setR32_non_residents(BigDecimal r32_non_residents) {
			this.r32_non_residents = r32_non_residents;
		}

		public BigDecimal getR33_residents() {
			return r33_residents;
		}

		public void setR33_residents(BigDecimal r33_residents) {
			this.r33_residents = r33_residents;
		}

		public BigDecimal getR33_non_residents() {
			return r33_non_residents;
		}

		public void setR33_non_residents(BigDecimal r33_non_residents) {
			this.r33_non_residents = r33_non_residents;
		}

		public BigDecimal getR34_residents() {
			return r34_residents;
		}

		public void setR34_residents(BigDecimal r34_residents) {
			this.r34_residents = r34_residents;
		}

		public BigDecimal getR34_non_residents() {
			return r34_non_residents;
		}

		public void setR34_non_residents(BigDecimal r34_non_residents) {
			this.r34_non_residents = r34_non_residents;
		}

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
		}

	}

	// ------------------------------
	// ENTITY: BRRS_M_DEP3_ARCHIVALTABLE_DETAIL
	// ------------------------------
	public static class M_DEP3_Archival_Detail_Entity {

		@Column(name = "CUST_ID")
		private String custId;

		@Id
		@Column(name = "ACCT_NUMBER")
		private String acctNumber;

		@Column(name = "ACCT_NAME")
		private String acctName;

		@Column(name = "DATA_TYPE")
		private String dataType;

		@Column(name = "REPORT_LABEL")
		private String reportLabel;

		@Column(name = "REPORT_ADDL_CRITERIA_1")
		private String reportAddlCriteria1;

		@Column(name = "REPORT_REMARKS")
		private String reportRemarks;

		@Column(name = "MODIFICATION_REMARKS")
		private String modificationRemarks;

		@Column(name = "DATA_ENTRY_VERSION")
		private String dataEntryVersion;

		@Column(name = "ACCT_BALANCE_IN_PULA")
		private BigDecimal acctBalanceInpula;

		@Column(name = "REPORT_DATE")
		private Date reportDate;

		@Column(name = "REPORT_NAME")
		private String reportName;

		@Column(name = "CREATE_USER")
		private String createUser;

		@Column(name = "CREATE_TIME")
		private Date createTime;

		@Column(name = "MODIFY_USER")
		private String modifyUser;

		@Column(name = "MODIFY_TIME")
		private Date modifyTime;

		@Column(name = "VERIFY_USER")
		private String verifyUser;

		@Column(name = "VERIFY_TIME")
		private Date verifyTime;

		@Column(name = "ENTITY_FLG")
		private String entityFlg;

		@Column(name = "MODIFY_FLG")
		private String modifyFlg;

		@Column(name = "DEL_FLG")
		private String delFlg;

		@Column(name = "CCY")
		private String ccy;

		@Column(name = "SEGMENT")
		private String segment;

		@Column(name = "TYPE")
		private String type;

		@Column(name = "MAT_BUCK_1")
		private String matBuck1;

		@Column(name = "EX_RATE_BUY")
		private BigDecimal exRateBuy;

		@Column(name = "EX_RATE_SELL")
		private BigDecimal exRateSell;

		@Column(name = "NOTICE_0TO31")
		private BigDecimal notice0to31;

		@Column(name = "NOTICE_32TO88")
		private BigDecimal notice32to88;

		@Column(name = "CER_OF_DEPO")
		private BigDecimal cerOfDepo;

		@Column(name = "IMPORT")
		private BigDecimal importValue; // 'import' is Java keyword

		@Column(name = "INVESTMENT")
		private BigDecimal investment;

		@Column(name = "OTHER")
		private BigDecimal other;

		@Column(name = "RESIDENTS")
		private BigDecimal residents;

		@Column(name = "NON_RESIDENTS")
		private BigDecimal nonResidents;

		@Column(name = "SNO")
		private Long sno;

		@Column(name = "REPORT_CODE")
		private String reportCode;

		public M_DEP3_Archival_Detail_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

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

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String reportLabel) {
			this.reportLabel = reportLabel;
		}

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
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

		public BigDecimal getAcctBalanceInpula() {
			return acctBalanceInpula;
		}

		public void setAcctBalanceInpula(BigDecimal acctBalanceInpula) {
			this.acctBalanceInpula = acctBalanceInpula;
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

		public String getCcy() {
			return ccy;
		}

		public void setCcy(String ccy) {
			this.ccy = ccy;
		}

		public String getSegment() {
			return segment;
		}

		public void setSegment(String segment) {
			this.segment = segment;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getMatBuck1() {
			return matBuck1;
		}

		public void setMatBuck1(String matBuck1) {
			this.matBuck1 = matBuck1;
		}

		public BigDecimal getExRateBuy() {
			return exRateBuy;
		}

		public void setExRateBuy(BigDecimal exRateBuy) {
			this.exRateBuy = exRateBuy;
		}

		public BigDecimal getExRateSell() {
			return exRateSell;
		}

		public void setExRateSell(BigDecimal exRateSell) {
			this.exRateSell = exRateSell;
		}

		public BigDecimal getNotice0to31() {
			return notice0to31;
		}

		public void setNotice0to31(BigDecimal notice0to31) {
			this.notice0to31 = notice0to31;
		}

		public BigDecimal getNotice32to88() {
			return notice32to88;
		}

		public void setNotice32to88(BigDecimal notice32to88) {
			this.notice32to88 = notice32to88;
		}

		public BigDecimal getCerOfDepo() {
			return cerOfDepo;
		}

		public void setCerOfDepo(BigDecimal cerOfDepo) {
			this.cerOfDepo = cerOfDepo;
		}

		public BigDecimal getImportValue() {
			return importValue;
		}

		public void setImportValue(BigDecimal importValue) {
			this.importValue = importValue;
		}

		public BigDecimal getInvestment() {
			return investment;
		}

		public void setInvestment(BigDecimal investment) {
			this.investment = investment;
		}

		public BigDecimal getOther() {
			return other;
		}

		public void setOther(BigDecimal other) {
			this.other = other;
		}

		public BigDecimal getResidents() {
			return residents;
		}

		public void setResidents(BigDecimal residents) {
			this.residents = residents;
		}

		public BigDecimal getNonResidents() {
			return nonResidents;
		}

		public void setNonResidents(BigDecimal nonResidents) {
			this.nonResidents = nonResidents;
		}

		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

		public String getReportCode() {
			return reportCode;
		}

		public void setReportCode(String reportCode) {
			this.reportCode = reportCode;
		}

	}

	// ------------------------------
	// ENTITY: BRRS_M_DEP3_MANUAL_SUMMARY
	// ------------------------------
	public static class M_DEP3_Manual_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		private BigDecimal r11_ex_rate_buy;
		private BigDecimal r11_ex_rate_mid;
		private BigDecimal r11_ex_rate_sell;

		private BigDecimal r11_notice_0to31;
		private BigDecimal r11_notice_32to88;

		private BigDecimal r11_cer_of_depo;

		private BigDecimal r12_ex_rate_buy;
		private BigDecimal r12_ex_rate_mid;
		private BigDecimal r12_ex_rate_sell;

		private BigDecimal r12_notice_0to31;
		private BigDecimal r12_notice_32to88;

		private BigDecimal r12_cer_of_depo;

		private BigDecimal r13_ex_rate_buy;
		private BigDecimal r13_ex_rate_mid;
		private BigDecimal r13_ex_rate_sell;

		private BigDecimal r13_notice_0to31;
		private BigDecimal r13_notice_32to88;

		private BigDecimal r13_cer_of_depo;

		private BigDecimal r14_ex_rate_buy;
		private BigDecimal r14_ex_rate_mid;
		private BigDecimal r14_ex_rate_sell;

		private BigDecimal r14_notice_0to31;
		private BigDecimal r14_notice_32to88;

		private BigDecimal r14_cer_of_depo;

		private BigDecimal r15_ex_rate_buy;
		private BigDecimal r15_ex_rate_mid;
		private BigDecimal r15_ex_rate_sell;

		private BigDecimal r15_notice_0to31;
		private BigDecimal r15_notice_32to88;

		private BigDecimal r15_cer_of_depo;

		private BigDecimal r16_ex_rate_buy;
		private BigDecimal r16_ex_rate_mid;
		private BigDecimal r16_ex_rate_sell;

		private BigDecimal r16_notice_0to31;
		private BigDecimal r16_notice_32to88;

		private BigDecimal r16_cer_of_depo;

		private BigDecimal r18_notice_0to31;
		private BigDecimal r18_notice_32to88;

		private BigDecimal r18_cer_of_depo;

		private BigDecimal r28_import;
		private BigDecimal r28_investment;
		private BigDecimal r28_other;

		private BigDecimal r29_import;
		private BigDecimal r29_investment;
		private BigDecimal r29_other;

		private BigDecimal r30_import;
		private BigDecimal r30_investment;
		private BigDecimal r30_other;

		private BigDecimal r31_import;
		private BigDecimal r31_investment;
		private BigDecimal r31_other;

		private BigDecimal r32_import;
		private BigDecimal r32_investment;
		private BigDecimal r32_other;

		private BigDecimal r33_import;
		private BigDecimal r33_investment;
		private BigDecimal r33_other;

		private BigDecimal r34_import;
		private BigDecimal r34_investment;
		private BigDecimal r34_other;

		private BigDecimal r28_residents;
		private BigDecimal r28_non_residents;

		private BigDecimal r29_residents;
		private BigDecimal r29_non_residents;

		private BigDecimal r30_residents;
		private BigDecimal r30_non_residents;

		private BigDecimal r31_residents;
		private BigDecimal r31_non_residents;

		private BigDecimal r32_residents;
		private BigDecimal r32_non_residents;

		private BigDecimal r33_residents;
		private BigDecimal r33_non_residents;

		private BigDecimal r34_residents;
		private BigDecimal r34_non_residents;

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

		public BigDecimal getR11_ex_rate_buy() {
			return r11_ex_rate_buy;
		}

		public void setR11_ex_rate_buy(BigDecimal r11_ex_rate_buy) {
			this.r11_ex_rate_buy = r11_ex_rate_buy;
		}

		public BigDecimal getR11_ex_rate_sell() {
			return r11_ex_rate_sell;
		}

		public void setR11_ex_rate_sell(BigDecimal r11_ex_rate_sell) {
			this.r11_ex_rate_sell = r11_ex_rate_sell;
		}

		public BigDecimal getR11_notice_0to31() {
			return r11_notice_0to31;
		}

		public void setR11_notice_0to31(BigDecimal r11_notice_0to31) {
			this.r11_notice_0to31 = r11_notice_0to31;
		}

		public BigDecimal getR11_notice_32to88() {
			return r11_notice_32to88;
		}

		public void setR11_notice_32to88(BigDecimal r11_notice_32to88) {
			this.r11_notice_32to88 = r11_notice_32to88;
		}

		public BigDecimal getR11_cer_of_depo() {
			return r11_cer_of_depo;
		}

		public void setR11_cer_of_depo(BigDecimal r11_cer_of_depo) {
			this.r11_cer_of_depo = r11_cer_of_depo;
		}

		public BigDecimal getR12_ex_rate_buy() {
			return r12_ex_rate_buy;
		}

		public void setR12_ex_rate_buy(BigDecimal r12_ex_rate_buy) {
			this.r12_ex_rate_buy = r12_ex_rate_buy;
		}

		public BigDecimal getR12_ex_rate_sell() {
			return r12_ex_rate_sell;
		}

		public void setR12_ex_rate_sell(BigDecimal r12_ex_rate_sell) {
			this.r12_ex_rate_sell = r12_ex_rate_sell;
		}

		public BigDecimal getR12_notice_0to31() {
			return r12_notice_0to31;
		}

		public void setR12_notice_0to31(BigDecimal r12_notice_0to31) {
			this.r12_notice_0to31 = r12_notice_0to31;
		}

		public BigDecimal getR12_notice_32to88() {
			return r12_notice_32to88;
		}

		public void setR12_notice_32to88(BigDecimal r12_notice_32to88) {
			this.r12_notice_32to88 = r12_notice_32to88;
		}

		public BigDecimal getR12_cer_of_depo() {
			return r12_cer_of_depo;
		}

		public void setR12_cer_of_depo(BigDecimal r12_cer_of_depo) {
			this.r12_cer_of_depo = r12_cer_of_depo;
		}

		public BigDecimal getR13_ex_rate_buy() {
			return r13_ex_rate_buy;
		}

		public void setR13_ex_rate_buy(BigDecimal r13_ex_rate_buy) {
			this.r13_ex_rate_buy = r13_ex_rate_buy;
		}

		public BigDecimal getR13_ex_rate_sell() {
			return r13_ex_rate_sell;
		}

		public void setR13_ex_rate_sell(BigDecimal r13_ex_rate_sell) {
			this.r13_ex_rate_sell = r13_ex_rate_sell;
		}

		public BigDecimal getR13_notice_0to31() {
			return r13_notice_0to31;
		}

		public void setR13_notice_0to31(BigDecimal r13_notice_0to31) {
			this.r13_notice_0to31 = r13_notice_0to31;
		}

		public BigDecimal getR13_notice_32to88() {
			return r13_notice_32to88;
		}

		public void setR13_notice_32to88(BigDecimal r13_notice_32to88) {
			this.r13_notice_32to88 = r13_notice_32to88;
		}

		public BigDecimal getR13_cer_of_depo() {
			return r13_cer_of_depo;
		}

		public void setR13_cer_of_depo(BigDecimal r13_cer_of_depo) {
			this.r13_cer_of_depo = r13_cer_of_depo;
		}

		public BigDecimal getR14_ex_rate_buy() {
			return r14_ex_rate_buy;
		}

		public void setR14_ex_rate_buy(BigDecimal r14_ex_rate_buy) {
			this.r14_ex_rate_buy = r14_ex_rate_buy;
		}

		public BigDecimal getR14_ex_rate_sell() {
			return r14_ex_rate_sell;
		}

		public void setR14_ex_rate_sell(BigDecimal r14_ex_rate_sell) {
			this.r14_ex_rate_sell = r14_ex_rate_sell;
		}

		public BigDecimal getR14_notice_0to31() {
			return r14_notice_0to31;
		}

		public void setR14_notice_0to31(BigDecimal r14_notice_0to31) {
			this.r14_notice_0to31 = r14_notice_0to31;
		}

		public BigDecimal getR14_notice_32to88() {
			return r14_notice_32to88;
		}

		public void setR14_notice_32to88(BigDecimal r14_notice_32to88) {
			this.r14_notice_32to88 = r14_notice_32to88;
		}

		public BigDecimal getR14_cer_of_depo() {
			return r14_cer_of_depo;
		}

		public void setR14_cer_of_depo(BigDecimal r14_cer_of_depo) {
			this.r14_cer_of_depo = r14_cer_of_depo;
		}

		public BigDecimal getR15_ex_rate_buy() {
			return r15_ex_rate_buy;
		}

		public void setR15_ex_rate_buy(BigDecimal r15_ex_rate_buy) {
			this.r15_ex_rate_buy = r15_ex_rate_buy;
		}

		public BigDecimal getR15_ex_rate_sell() {
			return r15_ex_rate_sell;
		}

		public void setR15_ex_rate_sell(BigDecimal r15_ex_rate_sell) {
			this.r15_ex_rate_sell = r15_ex_rate_sell;
		}

		public BigDecimal getR15_notice_0to31() {
			return r15_notice_0to31;
		}

		public void setR15_notice_0to31(BigDecimal r15_notice_0to31) {
			this.r15_notice_0to31 = r15_notice_0to31;
		}

		public BigDecimal getR15_notice_32to88() {
			return r15_notice_32to88;
		}

		public void setR15_notice_32to88(BigDecimal r15_notice_32to88) {
			this.r15_notice_32to88 = r15_notice_32to88;
		}

		public BigDecimal getR15_cer_of_depo() {
			return r15_cer_of_depo;
		}

		public void setR15_cer_of_depo(BigDecimal r15_cer_of_depo) {
			this.r15_cer_of_depo = r15_cer_of_depo;
		}

		public BigDecimal getR16_ex_rate_buy() {
			return r16_ex_rate_buy;
		}

		public void setR16_ex_rate_buy(BigDecimal r16_ex_rate_buy) {
			this.r16_ex_rate_buy = r16_ex_rate_buy;
		}

		public BigDecimal getR16_ex_rate_sell() {
			return r16_ex_rate_sell;
		}

		public void setR16_ex_rate_sell(BigDecimal r16_ex_rate_sell) {
			this.r16_ex_rate_sell = r16_ex_rate_sell;
		}

		public BigDecimal getR16_notice_0to31() {
			return r16_notice_0to31;
		}

		public void setR16_notice_0to31(BigDecimal r16_notice_0to31) {
			this.r16_notice_0to31 = r16_notice_0to31;
		}

		public BigDecimal getR16_notice_32to88() {
			return r16_notice_32to88;
		}

		public void setR16_notice_32to88(BigDecimal r16_notice_32to88) {
			this.r16_notice_32to88 = r16_notice_32to88;
		}

		public BigDecimal getR16_cer_of_depo() {
			return r16_cer_of_depo;
		}

		public void setR16_cer_of_depo(BigDecimal r16_cer_of_depo) {
			this.r16_cer_of_depo = r16_cer_of_depo;
		}

		public BigDecimal getR18_notice_0to31() {
			return r18_notice_0to31;
		}

		public void setR18_notice_0to31(BigDecimal r18_notice_0to31) {
			this.r18_notice_0to31 = r18_notice_0to31;
		}

		public BigDecimal getR18_notice_32to88() {
			return r18_notice_32to88;
		}

		public void setR18_notice_32to88(BigDecimal r18_notice_32to88) {
			this.r18_notice_32to88 = r18_notice_32to88;
		}

		public BigDecimal getR18_cer_of_depo() {
			return r18_cer_of_depo;
		}

		public void setR18_cer_of_depo(BigDecimal r18_cer_of_depo) {
			this.r18_cer_of_depo = r18_cer_of_depo;
		}

		public BigDecimal getR28_import() {
			return r28_import;
		}

		public void setR28_import(BigDecimal r28_import) {
			this.r28_import = r28_import;
		}

		public BigDecimal getR28_investment() {
			return r28_investment;
		}

		public void setR28_investment(BigDecimal r28_investment) {
			this.r28_investment = r28_investment;
		}

		public BigDecimal getR28_other() {
			return r28_other;
		}

		public void setR28_other(BigDecimal r28_other) {
			this.r28_other = r28_other;
		}

		public BigDecimal getR29_import() {
			return r29_import;
		}

		public void setR29_import(BigDecimal r29_import) {
			this.r29_import = r29_import;
		}

		public BigDecimal getR29_investment() {
			return r29_investment;
		}

		public void setR29_investment(BigDecimal r29_investment) {
			this.r29_investment = r29_investment;
		}

		public BigDecimal getR29_other() {
			return r29_other;
		}

		public void setR29_other(BigDecimal r29_other) {
			this.r29_other = r29_other;
		}

		public BigDecimal getR30_import() {
			return r30_import;
		}

		public void setR30_import(BigDecimal r30_import) {
			this.r30_import = r30_import;
		}

		public BigDecimal getR30_investment() {
			return r30_investment;
		}

		public void setR30_investment(BigDecimal r30_investment) {
			this.r30_investment = r30_investment;
		}

		public BigDecimal getR30_other() {
			return r30_other;
		}

		public void setR30_other(BigDecimal r30_other) {
			this.r30_other = r30_other;
		}

		public BigDecimal getR31_import() {
			return r31_import;
		}

		public void setR31_import(BigDecimal r31_import) {
			this.r31_import = r31_import;
		}

		public BigDecimal getR31_investment() {
			return r31_investment;
		}

		public void setR31_investment(BigDecimal r31_investment) {
			this.r31_investment = r31_investment;
		}

		public BigDecimal getR31_other() {
			return r31_other;
		}

		public void setR31_other(BigDecimal r31_other) {
			this.r31_other = r31_other;
		}

		public BigDecimal getR32_import() {
			return r32_import;
		}

		public void setR32_import(BigDecimal r32_import) {
			this.r32_import = r32_import;
		}

		public BigDecimal getR32_investment() {
			return r32_investment;
		}

		public void setR32_investment(BigDecimal r32_investment) {
			this.r32_investment = r32_investment;
		}

		public BigDecimal getR32_other() {
			return r32_other;
		}

		public void setR32_other(BigDecimal r32_other) {
			this.r32_other = r32_other;
		}

		public BigDecimal getR33_import() {
			return r33_import;
		}

		public void setR33_import(BigDecimal r33_import) {
			this.r33_import = r33_import;
		}

		public BigDecimal getR33_investment() {
			return r33_investment;
		}

		public void setR33_investment(BigDecimal r33_investment) {
			this.r33_investment = r33_investment;
		}

		public BigDecimal getR33_other() {
			return r33_other;
		}

		public void setR33_other(BigDecimal r33_other) {
			this.r33_other = r33_other;
		}

		public BigDecimal getR34_import() {
			return r34_import;
		}

		public void setR34_import(BigDecimal r34_import) {
			this.r34_import = r34_import;
		}

		public BigDecimal getR34_investment() {
			return r34_investment;
		}

		public void setR34_investment(BigDecimal r34_investment) {
			this.r34_investment = r34_investment;
		}

		public BigDecimal getR34_other() {
			return r34_other;
		}

		public void setR34_other(BigDecimal r34_other) {
			this.r34_other = r34_other;
		}

		public BigDecimal getR28_residents() {
			return r28_residents;
		}

		public void setR28_residents(BigDecimal r28_residents) {
			this.r28_residents = r28_residents;
		}

		public BigDecimal getR28_non_residents() {
			return r28_non_residents;
		}

		public void setR28_non_residents(BigDecimal r28_non_residents) {
			this.r28_non_residents = r28_non_residents;
		}

		public BigDecimal getR29_residents() {
			return r29_residents;
		}

		public void setR29_residents(BigDecimal r29_residents) {
			this.r29_residents = r29_residents;
		}

		public BigDecimal getR29_non_residents() {
			return r29_non_residents;
		}

		public void setR29_non_residents(BigDecimal r29_non_residents) {
			this.r29_non_residents = r29_non_residents;
		}

		public BigDecimal getR30_residents() {
			return r30_residents;
		}

		public void setR30_residents(BigDecimal r30_residents) {
			this.r30_residents = r30_residents;
		}

		public BigDecimal getR30_non_residents() {
			return r30_non_residents;
		}

		public void setR30_non_residents(BigDecimal r30_non_residents) {
			this.r30_non_residents = r30_non_residents;
		}

		public BigDecimal getR31_residents() {
			return r31_residents;
		}

		public void setR31_residents(BigDecimal r31_residents) {
			this.r31_residents = r31_residents;
		}

		public BigDecimal getR31_non_residents() {
			return r31_non_residents;
		}

		public void setR31_non_residents(BigDecimal r31_non_residents) {
			this.r31_non_residents = r31_non_residents;
		}

		public BigDecimal getR32_residents() {
			return r32_residents;
		}

		public void setR32_residents(BigDecimal r32_residents) {
			this.r32_residents = r32_residents;
		}

		public BigDecimal getR32_non_residents() {
			return r32_non_residents;
		}

		public void setR32_non_residents(BigDecimal r32_non_residents) {
			this.r32_non_residents = r32_non_residents;
		}

		public BigDecimal getR33_residents() {
			return r33_residents;
		}

		public void setR33_residents(BigDecimal r33_residents) {
			this.r33_residents = r33_residents;
		}

		public BigDecimal getR33_non_residents() {
			return r33_non_residents;
		}

		public void setR33_non_residents(BigDecimal r33_non_residents) {
			this.r33_non_residents = r33_non_residents;
		}

		public BigDecimal getR34_residents() {
			return r34_residents;
		}

		public void setR34_residents(BigDecimal r34_residents) {
			this.r34_residents = r34_residents;
		}

		public BigDecimal getR34_non_residents() {
			return r34_non_residents;
		}

		public void setR34_non_residents(BigDecimal r34_non_residents) {
			this.r34_non_residents = r34_non_residents;
		}

		public BigDecimal getR11_ex_rate_mid() {
			return r11_ex_rate_mid;
		}

		public void setR11_ex_rate_mid(BigDecimal r11_ex_rate_mid) {
			this.r11_ex_rate_mid = r11_ex_rate_mid;
		}

		public BigDecimal getR12_ex_rate_mid() {
			return r12_ex_rate_mid;
		}

		public void setR12_ex_rate_mid(BigDecimal r12_ex_rate_mid) {
			this.r12_ex_rate_mid = r12_ex_rate_mid;
		}

		public BigDecimal getR13_ex_rate_mid() {
			return r13_ex_rate_mid;
		}

		public void setR13_ex_rate_mid(BigDecimal r13_ex_rate_mid) {
			this.r13_ex_rate_mid = r13_ex_rate_mid;
		}

		public BigDecimal getR14_ex_rate_mid() {
			return r14_ex_rate_mid;
		}

		public void setR14_ex_rate_mid(BigDecimal r14_ex_rate_mid) {
			this.r14_ex_rate_mid = r14_ex_rate_mid;
		}

		public BigDecimal getR15_ex_rate_mid() {
			return r15_ex_rate_mid;
		}

		public void setR15_ex_rate_mid(BigDecimal r15_ex_rate_mid) {
			this.r15_ex_rate_mid = r15_ex_rate_mid;
		}

		public BigDecimal getR16_ex_rate_mid() {
			return r16_ex_rate_mid;
		}

		public void setR16_ex_rate_mid(BigDecimal r16_ex_rate_mid) {
			this.r16_ex_rate_mid = r16_ex_rate_mid;
		}

		public M_DEP3_Manual_Summary_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

	// ------------------------------
	// ENTITY: BRRS_M_DEP3_MANUAL_ARCHIVAL_SUMMARY
	// ------------------------------
	public static class M_DEP3_Manual_Archival_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		private BigDecimal r11_ex_rate_buy;
		private BigDecimal r11_ex_rate_mid;
		private BigDecimal r11_ex_rate_sell;

		private BigDecimal r11_notice_0to31;
		private BigDecimal r11_notice_32to88;

		private BigDecimal r11_cer_of_depo;

		private BigDecimal r12_ex_rate_buy;
		private BigDecimal r12_ex_rate_mid;
		private BigDecimal r12_ex_rate_sell;

		private BigDecimal r12_notice_0to31;
		private BigDecimal r12_notice_32to88;

		private BigDecimal r12_cer_of_depo;

		private BigDecimal r13_ex_rate_buy;
		private BigDecimal r13_ex_rate_mid;
		private BigDecimal r13_ex_rate_sell;

		private BigDecimal r13_notice_0to31;
		private BigDecimal r13_notice_32to88;

		private BigDecimal r13_cer_of_depo;

		private BigDecimal r14_ex_rate_buy;
		private BigDecimal r14_ex_rate_mid;
		private BigDecimal r14_ex_rate_sell;

		private BigDecimal r14_notice_0to31;
		private BigDecimal r14_notice_32to88;

		private BigDecimal r14_cer_of_depo;

		private BigDecimal r15_ex_rate_buy;
		private BigDecimal r15_ex_rate_mid;
		private BigDecimal r15_ex_rate_sell;

		private BigDecimal r15_notice_0to31;
		private BigDecimal r15_notice_32to88;

		private BigDecimal r15_cer_of_depo;

		private BigDecimal r16_ex_rate_buy;
		private BigDecimal r16_ex_rate_mid;
		private BigDecimal r16_ex_rate_sell;

		private BigDecimal r16_notice_0to31;
		private BigDecimal r16_notice_32to88;

		private BigDecimal r16_cer_of_depo;

		private BigDecimal r18_notice_0to31;
		private BigDecimal r18_notice_32to88;

		private BigDecimal r18_cer_of_depo;

		private BigDecimal r28_import;
		private BigDecimal r28_investment;
		private BigDecimal r28_other;

		private BigDecimal r29_import;
		private BigDecimal r29_investment;
		private BigDecimal r29_other;

		private BigDecimal r30_import;
		private BigDecimal r30_investment;
		private BigDecimal r30_other;

		private BigDecimal r31_import;
		private BigDecimal r31_investment;
		private BigDecimal r31_other;

		private BigDecimal r32_import;
		private BigDecimal r32_investment;
		private BigDecimal r32_other;

		private BigDecimal r33_import;
		private BigDecimal r33_investment;
		private BigDecimal r33_other;

		private BigDecimal r34_import;
		private BigDecimal r34_investment;
		private BigDecimal r34_other;

		private BigDecimal r28_residents;
		private BigDecimal r28_non_residents;

		private BigDecimal r29_residents;
		private BigDecimal r29_non_residents;

		private BigDecimal r30_residents;
		private BigDecimal r30_non_residents;

		private BigDecimal r31_residents;
		private BigDecimal r31_non_residents;

		private BigDecimal r32_residents;
		private BigDecimal r32_non_residents;

		private BigDecimal r33_residents;
		private BigDecimal r33_non_residents;

		private BigDecimal r34_residents;
		private BigDecimal r34_non_residents;

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

		public BigDecimal getR11_ex_rate_buy() {
			return r11_ex_rate_buy;
		}

		public void setR11_ex_rate_buy(BigDecimal r11_ex_rate_buy) {
			this.r11_ex_rate_buy = r11_ex_rate_buy;
		}

		public BigDecimal getR11_ex_rate_sell() {
			return r11_ex_rate_sell;
		}

		public void setR11_ex_rate_sell(BigDecimal r11_ex_rate_sell) {
			this.r11_ex_rate_sell = r11_ex_rate_sell;
		}

		public BigDecimal getR11_notice_0to31() {
			return r11_notice_0to31;
		}

		public void setR11_notice_0to31(BigDecimal r11_notice_0to31) {
			this.r11_notice_0to31 = r11_notice_0to31;
		}

		public BigDecimal getR11_notice_32to88() {
			return r11_notice_32to88;
		}

		public void setR11_notice_32to88(BigDecimal r11_notice_32to88) {
			this.r11_notice_32to88 = r11_notice_32to88;
		}

		public BigDecimal getR11_cer_of_depo() {
			return r11_cer_of_depo;
		}

		public void setR11_cer_of_depo(BigDecimal r11_cer_of_depo) {
			this.r11_cer_of_depo = r11_cer_of_depo;
		}

		public BigDecimal getR12_ex_rate_buy() {
			return r12_ex_rate_buy;
		}

		public void setR12_ex_rate_buy(BigDecimal r12_ex_rate_buy) {
			this.r12_ex_rate_buy = r12_ex_rate_buy;
		}

		public BigDecimal getR12_ex_rate_sell() {
			return r12_ex_rate_sell;
		}

		public void setR12_ex_rate_sell(BigDecimal r12_ex_rate_sell) {
			this.r12_ex_rate_sell = r12_ex_rate_sell;
		}

		public BigDecimal getR12_notice_0to31() {
			return r12_notice_0to31;
		}

		public void setR12_notice_0to31(BigDecimal r12_notice_0to31) {
			this.r12_notice_0to31 = r12_notice_0to31;
		}

		public BigDecimal getR12_notice_32to88() {
			return r12_notice_32to88;
		}

		public void setR12_notice_32to88(BigDecimal r12_notice_32to88) {
			this.r12_notice_32to88 = r12_notice_32to88;
		}

		public BigDecimal getR12_cer_of_depo() {
			return r12_cer_of_depo;
		}

		public void setR12_cer_of_depo(BigDecimal r12_cer_of_depo) {
			this.r12_cer_of_depo = r12_cer_of_depo;
		}

		public BigDecimal getR13_ex_rate_buy() {
			return r13_ex_rate_buy;
		}

		public void setR13_ex_rate_buy(BigDecimal r13_ex_rate_buy) {
			this.r13_ex_rate_buy = r13_ex_rate_buy;
		}

		public BigDecimal getR13_ex_rate_sell() {
			return r13_ex_rate_sell;
		}

		public void setR13_ex_rate_sell(BigDecimal r13_ex_rate_sell) {
			this.r13_ex_rate_sell = r13_ex_rate_sell;
		}

		public BigDecimal getR13_notice_0to31() {
			return r13_notice_0to31;
		}

		public void setR13_notice_0to31(BigDecimal r13_notice_0to31) {
			this.r13_notice_0to31 = r13_notice_0to31;
		}

		public BigDecimal getR13_notice_32to88() {
			return r13_notice_32to88;
		}

		public void setR13_notice_32to88(BigDecimal r13_notice_32to88) {
			this.r13_notice_32to88 = r13_notice_32to88;
		}

		public BigDecimal getR13_cer_of_depo() {
			return r13_cer_of_depo;
		}

		public void setR13_cer_of_depo(BigDecimal r13_cer_of_depo) {
			this.r13_cer_of_depo = r13_cer_of_depo;
		}

		public BigDecimal getR14_ex_rate_buy() {
			return r14_ex_rate_buy;
		}

		public void setR14_ex_rate_buy(BigDecimal r14_ex_rate_buy) {
			this.r14_ex_rate_buy = r14_ex_rate_buy;
		}

		public BigDecimal getR14_ex_rate_sell() {
			return r14_ex_rate_sell;
		}

		public void setR14_ex_rate_sell(BigDecimal r14_ex_rate_sell) {
			this.r14_ex_rate_sell = r14_ex_rate_sell;
		}

		public BigDecimal getR14_notice_0to31() {
			return r14_notice_0to31;
		}

		public void setR14_notice_0to31(BigDecimal r14_notice_0to31) {
			this.r14_notice_0to31 = r14_notice_0to31;
		}

		public BigDecimal getR14_notice_32to88() {
			return r14_notice_32to88;
		}

		public void setR14_notice_32to88(BigDecimal r14_notice_32to88) {
			this.r14_notice_32to88 = r14_notice_32to88;
		}

		public BigDecimal getR14_cer_of_depo() {
			return r14_cer_of_depo;
		}

		public void setR14_cer_of_depo(BigDecimal r14_cer_of_depo) {
			this.r14_cer_of_depo = r14_cer_of_depo;
		}

		public BigDecimal getR15_ex_rate_buy() {
			return r15_ex_rate_buy;
		}

		public void setR15_ex_rate_buy(BigDecimal r15_ex_rate_buy) {
			this.r15_ex_rate_buy = r15_ex_rate_buy;
		}

		public BigDecimal getR15_ex_rate_sell() {
			return r15_ex_rate_sell;
		}

		public void setR15_ex_rate_sell(BigDecimal r15_ex_rate_sell) {
			this.r15_ex_rate_sell = r15_ex_rate_sell;
		}

		public BigDecimal getR15_notice_0to31() {
			return r15_notice_0to31;
		}

		public void setR15_notice_0to31(BigDecimal r15_notice_0to31) {
			this.r15_notice_0to31 = r15_notice_0to31;
		}

		public BigDecimal getR15_notice_32to88() {
			return r15_notice_32to88;
		}

		public void setR15_notice_32to88(BigDecimal r15_notice_32to88) {
			this.r15_notice_32to88 = r15_notice_32to88;
		}

		public BigDecimal getR15_cer_of_depo() {
			return r15_cer_of_depo;
		}

		public void setR15_cer_of_depo(BigDecimal r15_cer_of_depo) {
			this.r15_cer_of_depo = r15_cer_of_depo;
		}

		public BigDecimal getR16_ex_rate_buy() {
			return r16_ex_rate_buy;
		}

		public void setR16_ex_rate_buy(BigDecimal r16_ex_rate_buy) {
			this.r16_ex_rate_buy = r16_ex_rate_buy;
		}

		public BigDecimal getR16_ex_rate_sell() {
			return r16_ex_rate_sell;
		}

		public void setR16_ex_rate_sell(BigDecimal r16_ex_rate_sell) {
			this.r16_ex_rate_sell = r16_ex_rate_sell;
		}

		public BigDecimal getR16_notice_0to31() {
			return r16_notice_0to31;
		}

		public void setR16_notice_0to31(BigDecimal r16_notice_0to31) {
			this.r16_notice_0to31 = r16_notice_0to31;
		}

		public BigDecimal getR16_notice_32to88() {
			return r16_notice_32to88;
		}

		public void setR16_notice_32to88(BigDecimal r16_notice_32to88) {
			this.r16_notice_32to88 = r16_notice_32to88;
		}

		public BigDecimal getR16_cer_of_depo() {
			return r16_cer_of_depo;
		}

		public void setR16_cer_of_depo(BigDecimal r16_cer_of_depo) {
			this.r16_cer_of_depo = r16_cer_of_depo;
		}

		public BigDecimal getR18_notice_0to31() {
			return r18_notice_0to31;
		}

		public void setR18_notice_0to31(BigDecimal r18_notice_0to31) {
			this.r18_notice_0to31 = r18_notice_0to31;
		}

		public BigDecimal getR18_notice_32to88() {
			return r18_notice_32to88;
		}

		public void setR18_notice_32to88(BigDecimal r18_notice_32to88) {
			this.r18_notice_32to88 = r18_notice_32to88;
		}

		public BigDecimal getR18_cer_of_depo() {
			return r18_cer_of_depo;
		}

		public void setR18_cer_of_depo(BigDecimal r18_cer_of_depo) {
			this.r18_cer_of_depo = r18_cer_of_depo;
		}

		public BigDecimal getR28_import() {
			return r28_import;
		}

		public void setR28_import(BigDecimal r28_import) {
			this.r28_import = r28_import;
		}

		public BigDecimal getR28_investment() {
			return r28_investment;
		}

		public void setR28_investment(BigDecimal r28_investment) {
			this.r28_investment = r28_investment;
		}

		public BigDecimal getR28_other() {
			return r28_other;
		}

		public void setR28_other(BigDecimal r28_other) {
			this.r28_other = r28_other;
		}

		public BigDecimal getR29_import() {
			return r29_import;
		}

		public void setR29_import(BigDecimal r29_import) {
			this.r29_import = r29_import;
		}

		public BigDecimal getR29_investment() {
			return r29_investment;
		}

		public void setR29_investment(BigDecimal r29_investment) {
			this.r29_investment = r29_investment;
		}

		public BigDecimal getR29_other() {
			return r29_other;
		}

		public void setR29_other(BigDecimal r29_other) {
			this.r29_other = r29_other;
		}

		public BigDecimal getR30_import() {
			return r30_import;
		}

		public void setR30_import(BigDecimal r30_import) {
			this.r30_import = r30_import;
		}

		public BigDecimal getR30_investment() {
			return r30_investment;
		}

		public void setR30_investment(BigDecimal r30_investment) {
			this.r30_investment = r30_investment;
		}

		public BigDecimal getR30_other() {
			return r30_other;
		}

		public void setR30_other(BigDecimal r30_other) {
			this.r30_other = r30_other;
		}

		public BigDecimal getR31_import() {
			return r31_import;
		}

		public void setR31_import(BigDecimal r31_import) {
			this.r31_import = r31_import;
		}

		public BigDecimal getR31_investment() {
			return r31_investment;
		}

		public void setR31_investment(BigDecimal r31_investment) {
			this.r31_investment = r31_investment;
		}

		public BigDecimal getR31_other() {
			return r31_other;
		}

		public void setR31_other(BigDecimal r31_other) {
			this.r31_other = r31_other;
		}

		public BigDecimal getR32_import() {
			return r32_import;
		}

		public void setR32_import(BigDecimal r32_import) {
			this.r32_import = r32_import;
		}

		public BigDecimal getR32_investment() {
			return r32_investment;
		}

		public void setR32_investment(BigDecimal r32_investment) {
			this.r32_investment = r32_investment;
		}

		public BigDecimal getR32_other() {
			return r32_other;
		}

		public void setR32_other(BigDecimal r32_other) {
			this.r32_other = r32_other;
		}

		public BigDecimal getR33_import() {
			return r33_import;
		}

		public void setR33_import(BigDecimal r33_import) {
			this.r33_import = r33_import;
		}

		public BigDecimal getR33_investment() {
			return r33_investment;
		}

		public void setR33_investment(BigDecimal r33_investment) {
			this.r33_investment = r33_investment;
		}

		public BigDecimal getR33_other() {
			return r33_other;
		}

		public void setR33_other(BigDecimal r33_other) {
			this.r33_other = r33_other;
		}

		public BigDecimal getR34_import() {
			return r34_import;
		}

		public void setR34_import(BigDecimal r34_import) {
			this.r34_import = r34_import;
		}

		public BigDecimal getR34_investment() {
			return r34_investment;
		}

		public void setR34_investment(BigDecimal r34_investment) {
			this.r34_investment = r34_investment;
		}

		public BigDecimal getR34_other() {
			return r34_other;
		}

		public void setR34_other(BigDecimal r34_other) {
			this.r34_other = r34_other;
		}

		public BigDecimal getR28_residents() {
			return r28_residents;
		}

		public void setR28_residents(BigDecimal r28_residents) {
			this.r28_residents = r28_residents;
		}

		public BigDecimal getR28_non_residents() {
			return r28_non_residents;
		}

		public void setR28_non_residents(BigDecimal r28_non_residents) {
			this.r28_non_residents = r28_non_residents;
		}

		public BigDecimal getR29_residents() {
			return r29_residents;
		}

		public void setR29_residents(BigDecimal r29_residents) {
			this.r29_residents = r29_residents;
		}

		public BigDecimal getR29_non_residents() {
			return r29_non_residents;
		}

		public void setR29_non_residents(BigDecimal r29_non_residents) {
			this.r29_non_residents = r29_non_residents;
		}

		public BigDecimal getR30_residents() {
			return r30_residents;
		}

		public void setR30_residents(BigDecimal r30_residents) {
			this.r30_residents = r30_residents;
		}

		public BigDecimal getR30_non_residents() {
			return r30_non_residents;
		}

		public void setR30_non_residents(BigDecimal r30_non_residents) {
			this.r30_non_residents = r30_non_residents;
		}

		public BigDecimal getR31_residents() {
			return r31_residents;
		}

		public void setR31_residents(BigDecimal r31_residents) {
			this.r31_residents = r31_residents;
		}

		public BigDecimal getR31_non_residents() {
			return r31_non_residents;
		}

		public void setR31_non_residents(BigDecimal r31_non_residents) {
			this.r31_non_residents = r31_non_residents;
		}

		public BigDecimal getR32_residents() {
			return r32_residents;
		}

		public void setR32_residents(BigDecimal r32_residents) {
			this.r32_residents = r32_residents;
		}

		public BigDecimal getR32_non_residents() {
			return r32_non_residents;
		}

		public void setR32_non_residents(BigDecimal r32_non_residents) {
			this.r32_non_residents = r32_non_residents;
		}

		public BigDecimal getR33_residents() {
			return r33_residents;
		}

		public void setR33_residents(BigDecimal r33_residents) {
			this.r33_residents = r33_residents;
		}

		public BigDecimal getR33_non_residents() {
			return r33_non_residents;
		}

		public void setR33_non_residents(BigDecimal r33_non_residents) {
			this.r33_non_residents = r33_non_residents;
		}

		public BigDecimal getR34_residents() {
			return r34_residents;
		}

		public void setR34_residents(BigDecimal r34_residents) {
			this.r34_residents = r34_residents;
		}

		public BigDecimal getR34_non_residents() {
			return r34_non_residents;
		}

		public void setR34_non_residents(BigDecimal r34_non_residents) {
			this.r34_non_residents = r34_non_residents;
		}

		public BigDecimal getR11_ex_rate_mid() {
			return r11_ex_rate_mid;
		}

		public void setR11_ex_rate_mid(BigDecimal r11_ex_rate_mid) {
			this.r11_ex_rate_mid = r11_ex_rate_mid;
		}

		public BigDecimal getR12_ex_rate_mid() {
			return r12_ex_rate_mid;
		}

		public void setR12_ex_rate_mid(BigDecimal r12_ex_rate_mid) {
			this.r12_ex_rate_mid = r12_ex_rate_mid;
		}

		public BigDecimal getR13_ex_rate_mid() {
			return r13_ex_rate_mid;
		}

		public void setR13_ex_rate_mid(BigDecimal r13_ex_rate_mid) {
			this.r13_ex_rate_mid = r13_ex_rate_mid;
		}

		public BigDecimal getR14_ex_rate_mid() {
			return r14_ex_rate_mid;
		}

		public void setR14_ex_rate_mid(BigDecimal r14_ex_rate_mid) {
			this.r14_ex_rate_mid = r14_ex_rate_mid;
		}

		public BigDecimal getR15_ex_rate_mid() {
			return r15_ex_rate_mid;
		}

		public void setR15_ex_rate_mid(BigDecimal r15_ex_rate_mid) {
			this.r15_ex_rate_mid = r15_ex_rate_mid;
		}

		public BigDecimal getR16_ex_rate_mid() {
			return r16_ex_rate_mid;
		}

		public void setR16_ex_rate_mid(BigDecimal r16_ex_rate_mid) {
			this.r16_ex_rate_mid = r16_ex_rate_mid;
		}

		public M_DEP3_Manual_Archival_Summary_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

	/* MAPPERS */

	// ------------------------------
	// ROW MAPPER FOR M_DEP3 SUMMARY ENTITY
	// ------------------------------
	public static class M_DEP3_Summary_EntityRowMapper implements RowMapper<M_DEP3_Summary_Entity> {
		@Override
		public M_DEP3_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_DEP3_Summary_Entity entity = new M_DEP3_Summary_Entity();
			entity.setR11_foreign_curr_acc_by_curr(rs.getString("r11_foreign_curr_acc_by_curr"));
			entity.setR11_ex_rate_buy(rs.getBigDecimal("r11_ex_rate_buy"));
			entity.setR11_ex_rate_mid(rs.getBigDecimal("r11_ex_rate_mid"));
			entity.setR11_ex_rate_sell(rs.getBigDecimal("r11_ex_rate_sell"));
			entity.setR11_current(rs.getBigDecimal("r11_current"));
			entity.setR11_call(rs.getBigDecimal("r11_call"));
			entity.setR11_savings(rs.getBigDecimal("r11_savings"));
			entity.setR11_notice_0to31(rs.getBigDecimal("r11_notice_0to31"));
			entity.setR11_notice_32to88(rs.getBigDecimal("r11_notice_32to88"));
			entity.setR11_fix_depo_91_day_depo(rs.getBigDecimal("r11_fix_depo_91_day_depo"));
			entity.setR11_fix_depo_1to2(rs.getBigDecimal("r11_fix_depo_1to2"));
			entity.setR11_fix_depo_4to6(rs.getBigDecimal("r11_fix_depo_4to6"));
			entity.setR11_fix_depo_7to12(rs.getBigDecimal("r11_fix_depo_7to12"));
			entity.setR11_fix_depo_13to18(rs.getBigDecimal("r11_fix_depo_13to18"));
			entity.setR11_fix_depo_19to24(rs.getBigDecimal("r11_fix_depo_19to24"));
			entity.setR11_fix_depo_over24(rs.getBigDecimal("r11_fix_depo_over24"));
			entity.setR11_cer_of_depo(rs.getBigDecimal("r11_cer_of_depo"));
			entity.setR11_total(rs.getBigDecimal("r11_total"));
			entity.setR11_pula_equivalent(rs.getBigDecimal("r11_pula_equivalent"));
			entity.setR11_avg_pula_equivalent(rs.getBigDecimal("r11_avg_pula_equivalent"));
			entity.setR12_foreign_curr_acc_by_curr(rs.getString("r12_foreign_curr_acc_by_curr"));
			entity.setR12_ex_rate_buy(rs.getBigDecimal("r12_ex_rate_buy"));
			entity.setR12_ex_rate_mid(rs.getBigDecimal("r12_ex_rate_mid"));
			entity.setR12_ex_rate_sell(rs.getBigDecimal("r12_ex_rate_sell"));
			entity.setR12_current(rs.getBigDecimal("r12_current"));
			entity.setR12_call(rs.getBigDecimal("r12_call"));
			entity.setR12_savings(rs.getBigDecimal("r12_savings"));
			entity.setR12_notice_0to31(rs.getBigDecimal("r12_notice_0to31"));
			entity.setR12_notice_32to88(rs.getBigDecimal("r12_notice_32to88"));
			entity.setR12_fix_depo_91_day_depo(rs.getBigDecimal("r12_fix_depo_91_day_depo"));
			entity.setR12_fix_depo_1to2(rs.getBigDecimal("r12_fix_depo_1to2"));
			entity.setR12_fix_depo_4to6(rs.getBigDecimal("r12_fix_depo_4to6"));
			entity.setR12_fix_depo_7to12(rs.getBigDecimal("r12_fix_depo_7to12"));
			entity.setR12_fix_depo_13to18(rs.getBigDecimal("r12_fix_depo_13to18"));
			entity.setR12_fix_depo_19to24(rs.getBigDecimal("r12_fix_depo_19to24"));
			entity.setR12_fix_depo_over24(rs.getBigDecimal("r12_fix_depo_over24"));
			entity.setR12_cer_of_depo(rs.getBigDecimal("r12_cer_of_depo"));
			entity.setR12_total(rs.getBigDecimal("r12_total"));
			entity.setR12_pula_equivalent(rs.getBigDecimal("r12_pula_equivalent"));
			entity.setR12_avg_pula_equivalent(rs.getBigDecimal("r12_avg_pula_equivalent"));
			entity.setR13_foreign_curr_acc_by_curr(rs.getString("r13_foreign_curr_acc_by_curr"));
			entity.setR13_ex_rate_buy(rs.getBigDecimal("r13_ex_rate_buy"));
			entity.setR13_ex_rate_mid(rs.getBigDecimal("r13_ex_rate_mid"));
			entity.setR13_ex_rate_sell(rs.getBigDecimal("r13_ex_rate_sell"));
			entity.setR13_current(rs.getBigDecimal("r13_current"));
			entity.setR13_call(rs.getBigDecimal("r13_call"));
			entity.setR13_savings(rs.getBigDecimal("r13_savings"));
			entity.setR13_notice_0to31(rs.getBigDecimal("r13_notice_0to31"));
			entity.setR13_notice_32to88(rs.getBigDecimal("r13_notice_32to88"));
			entity.setR13_fix_depo_91_day_depo(rs.getBigDecimal("r13_fix_depo_91_day_depo"));
			entity.setR13_fix_depo_1to2(rs.getBigDecimal("r13_fix_depo_1to2"));
			entity.setR13_fix_depo_4to6(rs.getBigDecimal("r13_fix_depo_4to6"));
			entity.setR13_fix_depo_7to12(rs.getBigDecimal("r13_fix_depo_7to12"));
			entity.setR13_fix_depo_13to18(rs.getBigDecimal("r13_fix_depo_13to18"));
			entity.setR13_fix_depo_19to24(rs.getBigDecimal("r13_fix_depo_19to24"));
			entity.setR13_fix_depo_over24(rs.getBigDecimal("r13_fix_depo_over24"));
			entity.setR13_cer_of_depo(rs.getBigDecimal("r13_cer_of_depo"));
			entity.setR13_total(rs.getBigDecimal("r13_total"));
			entity.setR13_pula_equivalent(rs.getBigDecimal("r13_pula_equivalent"));
			entity.setR13_avg_pula_equivalent(rs.getBigDecimal("r13_avg_pula_equivalent"));
			entity.setR14_foreign_curr_acc_by_curr(rs.getString("r14_foreign_curr_acc_by_curr"));
			entity.setR14_ex_rate_buy(rs.getBigDecimal("r14_ex_rate_buy"));
			entity.setR14_ex_rate_mid(rs.getBigDecimal("r14_ex_rate_mid"));
			entity.setR14_ex_rate_sell(rs.getBigDecimal("r14_ex_rate_sell"));
			entity.setR14_current(rs.getBigDecimal("r14_current"));
			entity.setR14_call(rs.getBigDecimal("r14_call"));
			entity.setR14_savings(rs.getBigDecimal("r14_savings"));
			entity.setR14_notice_0to31(rs.getBigDecimal("r14_notice_0to31"));
			entity.setR14_notice_32to88(rs.getBigDecimal("r14_notice_32to88"));
			entity.setR14_fix_depo_91_day_depo(rs.getBigDecimal("r14_fix_depo_91_day_depo"));
			entity.setR14_fix_depo_1to2(rs.getBigDecimal("r14_fix_depo_1to2"));
			entity.setR14_fix_depo_4to6(rs.getBigDecimal("r14_fix_depo_4to6"));
			entity.setR14_fix_depo_7to12(rs.getBigDecimal("r14_fix_depo_7to12"));
			entity.setR14_fix_depo_13to18(rs.getBigDecimal("r14_fix_depo_13to18"));
			entity.setR14_fix_depo_19to24(rs.getBigDecimal("r14_fix_depo_19to24"));
			entity.setR14_fix_depo_over24(rs.getBigDecimal("r14_fix_depo_over24"));
			entity.setR14_cer_of_depo(rs.getBigDecimal("r14_cer_of_depo"));
			entity.setR14_total(rs.getBigDecimal("r14_total"));
			entity.setR14_pula_equivalent(rs.getBigDecimal("r14_pula_equivalent"));
			entity.setR14_avg_pula_equivalent(rs.getBigDecimal("r14_avg_pula_equivalent"));
			entity.setR15_foreign_curr_acc_by_curr(rs.getString("r15_foreign_curr_acc_by_curr"));
			entity.setR15_ex_rate_buy(rs.getBigDecimal("r15_ex_rate_buy"));
			entity.setR15_ex_rate_mid(rs.getBigDecimal("r15_ex_rate_mid"));
			entity.setR15_ex_rate_sell(rs.getBigDecimal("r15_ex_rate_sell"));
			entity.setR15_current(rs.getBigDecimal("r15_current"));
			entity.setR15_call(rs.getBigDecimal("r15_call"));
			entity.setR15_savings(rs.getBigDecimal("r15_savings"));
			entity.setR15_notice_0to31(rs.getBigDecimal("r15_notice_0to31"));
			entity.setR15_notice_32to88(rs.getBigDecimal("r15_notice_32to88"));
			entity.setR15_fix_depo_91_day_depo(rs.getBigDecimal("r15_fix_depo_91_day_depo"));
			entity.setR15_fix_depo_1to2(rs.getBigDecimal("r15_fix_depo_1to2"));
			entity.setR15_fix_depo_4to6(rs.getBigDecimal("r15_fix_depo_4to6"));
			entity.setR15_fix_depo_7to12(rs.getBigDecimal("r15_fix_depo_7to12"));
			entity.setR15_fix_depo_13to18(rs.getBigDecimal("r15_fix_depo_13to18"));
			entity.setR15_fix_depo_19to24(rs.getBigDecimal("r15_fix_depo_19to24"));
			entity.setR15_fix_depo_over24(rs.getBigDecimal("r15_fix_depo_over24"));
			entity.setR15_cer_of_depo(rs.getBigDecimal("r15_cer_of_depo"));
			entity.setR15_total(rs.getBigDecimal("r15_total"));
			entity.setR15_pula_equivalent(rs.getBigDecimal("r15_pula_equivalent"));
			entity.setR15_avg_pula_equivalent(rs.getBigDecimal("r15_avg_pula_equivalent"));
			entity.setR16_foreign_curr_acc_by_curr(rs.getString("r16_foreign_curr_acc_by_curr"));
			entity.setR16_ex_rate_buy(rs.getBigDecimal("r16_ex_rate_buy"));
			entity.setR16_ex_rate_mid(rs.getBigDecimal("r16_ex_rate_mid"));
			entity.setR16_ex_rate_sell(rs.getBigDecimal("r16_ex_rate_sell"));
			entity.setR16_current(rs.getBigDecimal("r16_current"));
			entity.setR16_call(rs.getBigDecimal("r16_call"));
			entity.setR16_savings(rs.getBigDecimal("r16_savings"));
			entity.setR16_notice_0to31(rs.getBigDecimal("r16_notice_0to31"));
			entity.setR16_notice_32to88(rs.getBigDecimal("r16_notice_32to88"));
			entity.setR16_fix_depo_91_day_depo(rs.getBigDecimal("r16_fix_depo_91_day_depo"));
			entity.setR16_fix_depo_1to2(rs.getBigDecimal("r16_fix_depo_1to2"));
			entity.setR16_fix_depo_4to6(rs.getBigDecimal("r16_fix_depo_4to6"));
			entity.setR16_fix_depo_7to12(rs.getBigDecimal("r16_fix_depo_7to12"));
			entity.setR16_fix_depo_13to18(rs.getBigDecimal("r16_fix_depo_13to18"));
			entity.setR16_fix_depo_19to24(rs.getBigDecimal("r16_fix_depo_19to24"));
			entity.setR16_fix_depo_over24(rs.getBigDecimal("r16_fix_depo_over24"));
			entity.setR16_cer_of_depo(rs.getBigDecimal("r16_cer_of_depo"));
			entity.setR16_total(rs.getBigDecimal("r16_total"));
			entity.setR16_pula_equivalent(rs.getBigDecimal("r16_pula_equivalent"));
			entity.setR16_avg_pula_equivalent(rs.getBigDecimal("r16_avg_pula_equivalent"));
			entity.setR17_foreign_curr_acc_by_curr(rs.getString("r17_foreign_curr_acc_by_curr"));
			entity.setR17_ex_rate_buy(rs.getBigDecimal("r17_ex_rate_buy"));
			entity.setR17_ex_rate_mid(rs.getBigDecimal("r17_ex_rate_mid"));
			entity.setR17_ex_rate_sell(rs.getBigDecimal("r17_ex_rate_sell"));
			entity.setR17_current(rs.getBigDecimal("r17_current"));
			entity.setR17_call(rs.getBigDecimal("r17_call"));
			entity.setR17_savings(rs.getBigDecimal("r17_savings"));
			entity.setR17_notice_0to31(rs.getBigDecimal("r17_notice_0to31"));
			entity.setR17_notice_32to88(rs.getBigDecimal("r17_notice_32to88"));
			entity.setR17_fix_depo_91_day_depo(rs.getBigDecimal("r17_fix_depo_91_day_depo"));
			entity.setR17_fix_depo_1to2(rs.getBigDecimal("r17_fix_depo_1to2"));
			entity.setR17_fix_depo_4to6(rs.getBigDecimal("r17_fix_depo_4to6"));
			entity.setR17_fix_depo_7to12(rs.getBigDecimal("r17_fix_depo_7to12"));
			entity.setR17_fix_depo_13to18(rs.getBigDecimal("r17_fix_depo_13to18"));
			entity.setR17_fix_depo_19to24(rs.getBigDecimal("r17_fix_depo_19to24"));
			entity.setR17_fix_depo_over24(rs.getBigDecimal("r17_fix_depo_over24"));
			entity.setR17_cer_of_depo(rs.getBigDecimal("r17_cer_of_depo"));
			entity.setR17_total(rs.getBigDecimal("r17_total"));
			entity.setR17_pula_equivalent(rs.getBigDecimal("r17_pula_equivalent"));
			entity.setR17_avg_pula_equivalent(rs.getBigDecimal("r17_avg_pula_equivalent"));
			entity.setR18_foreign_curr_acc_by_curr(rs.getString("r18_foreign_curr_acc_by_curr"));
			entity.setR18_ex_rate_buy(rs.getBigDecimal("r18_ex_rate_buy"));
			entity.setR18_ex_rate_mid(rs.getBigDecimal("r18_ex_rate_mid"));
			entity.setR18_ex_rate_sell(rs.getBigDecimal("r18_ex_rate_sell"));
			entity.setR18_current(rs.getBigDecimal("r18_current"));
			entity.setR18_call(rs.getBigDecimal("r18_call"));
			entity.setR18_savings(rs.getBigDecimal("r18_savings"));
			entity.setR18_notice_0to31(rs.getBigDecimal("r18_notice_0to31"));
			entity.setR18_notice_32to88(rs.getBigDecimal("r18_notice_32to88"));
			entity.setR18_fix_depo_91_day_depo(rs.getBigDecimal("r18_fix_depo_91_day_depo"));
			entity.setR18_fix_depo_1to2(rs.getBigDecimal("r18_fix_depo_1to2"));
			entity.setR18_fix_depo_4to6(rs.getBigDecimal("r18_fix_depo_4to6"));
			entity.setR18_fix_depo_7to12(rs.getBigDecimal("r18_fix_depo_7to12"));
			entity.setR18_fix_depo_13to18(rs.getBigDecimal("r18_fix_depo_13to18"));
			entity.setR18_fix_depo_19to24(rs.getBigDecimal("r18_fix_depo_19to24"));
			entity.setR18_fix_depo_over24(rs.getBigDecimal("r18_fix_depo_over24"));
			entity.setR18_cer_of_depo(rs.getBigDecimal("r18_cer_of_depo"));
			entity.setR18_total(rs.getBigDecimal("r18_total"));
			entity.setR18_pula_equivalent(rs.getBigDecimal("r18_pula_equivalent"));
			entity.setR18_avg_pula_equivalent(rs.getBigDecimal("r18_avg_pula_equivalent"));
			entity.setR28_import(rs.getBigDecimal("r28_import"));
			entity.setR28_investment(rs.getBigDecimal("r28_investment"));
			entity.setR28_other(rs.getBigDecimal("r28_other"));
			entity.setR29_import(rs.getBigDecimal("r29_import"));
			entity.setR29_investment(rs.getBigDecimal("r29_investment"));
			entity.setR29_other(rs.getBigDecimal("r29_other"));
			entity.setR30_import(rs.getBigDecimal("r30_import"));
			entity.setR30_investment(rs.getBigDecimal("r30_investment"));
			entity.setR30_other(rs.getBigDecimal("r30_other"));
			entity.setR31_import(rs.getBigDecimal("r31_import"));
			entity.setR31_investment(rs.getBigDecimal("r31_investment"));
			entity.setR31_other(rs.getBigDecimal("r31_other"));
			entity.setR32_import(rs.getBigDecimal("r32_import"));
			entity.setR32_investment(rs.getBigDecimal("r32_investment"));
			entity.setR32_other(rs.getBigDecimal("r32_other"));
			entity.setR33_import(rs.getBigDecimal("r33_import"));
			entity.setR33_investment(rs.getBigDecimal("r33_investment"));
			entity.setR33_other(rs.getBigDecimal("r33_other"));
			entity.setR34_import(rs.getBigDecimal("r34_import"));
			entity.setR34_investment(rs.getBigDecimal("r34_investment"));
			entity.setR34_other(rs.getBigDecimal("r34_other"));
			entity.setR28_residents(rs.getBigDecimal("r28_residents"));
			entity.setR28_non_residents(rs.getBigDecimal("r28_non_residents"));
			entity.setR29_residents(rs.getBigDecimal("r29_residents"));
			entity.setR29_non_residents(rs.getBigDecimal("r29_non_residents"));
			entity.setR30_residents(rs.getBigDecimal("r30_residents"));
			entity.setR30_non_residents(rs.getBigDecimal("r30_non_residents"));
			entity.setR31_residents(rs.getBigDecimal("r31_residents"));
			entity.setR31_non_residents(rs.getBigDecimal("r31_non_residents"));
			entity.setR32_residents(rs.getBigDecimal("r32_residents"));
			entity.setR32_non_residents(rs.getBigDecimal("r32_non_residents"));
			entity.setR33_residents(rs.getBigDecimal("r33_residents"));
			entity.setR33_non_residents(rs.getBigDecimal("r33_non_residents"));
			entity.setR34_residents(rs.getBigDecimal("r34_residents"));
			entity.setR34_non_residents(rs.getBigDecimal("r34_non_residents"));
			entity.setReportDate(rs.getTimestamp("REPORT_DATE"));
			entity.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			entity.setReport_frequency(rs.getString("report_frequency"));
			entity.setReport_code(rs.getString("report_code"));
			entity.setReport_desc(rs.getString("report_desc"));
			entity.setEntity_flg(rs.getString("entity_flg"));
			entity.setModify_flg(rs.getString("modify_flg"));
			entity.setDel_flg(rs.getString("del_flg"));
			return entity;
		}
	}

	// ------------------------------
	// ROW MAPPER FOR M_DEP3 DETAIL ENTITY
	// ------------------------------
	public static class M_DEP3_Detail_EntityRowMapper implements RowMapper<M_DEP3_Detail_Entity> {
		@Override
		public M_DEP3_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_DEP3_Detail_Entity entity = new M_DEP3_Detail_Entity();
			entity.setCustId(rs.getString("CUST_ID"));
			entity.setAcctNumber(rs.getString("ACCT_NUMBER"));
			entity.setAcctName(rs.getString("ACCT_NAME"));
			entity.setDataType(rs.getString("DATA_TYPE"));
			entity.setReportLabel(rs.getString("REPORT_LABEL"));
			entity.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			entity.setReportRemarks(rs.getString("REPORT_REMARKS"));
			entity.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			entity.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			entity.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			entity.setReportDate(rs.getTimestamp("REPORT_DATE"));
			entity.setReportName(rs.getString("REPORT_NAME"));
			entity.setCreateUser(rs.getString("CREATE_USER"));
			entity.setCreateTime(rs.getTimestamp("CREATE_TIME"));
			entity.setModifyUser(rs.getString("MODIFY_USER"));
			entity.setModifyTime(rs.getTimestamp("MODIFY_TIME"));
			entity.setVerifyUser(rs.getString("VERIFY_USER"));
			entity.setVerifyTime(rs.getTimestamp("VERIFY_TIME"));
			entity.setEntityFlg(rs.getString("ENTITY_FLG"));
			entity.setModifyFlg(rs.getString("MODIFY_FLG"));
			entity.setDelFlg(rs.getString("DEL_FLG"));
			entity.setCcy(rs.getString("CCY"));
			entity.setSegment(rs.getString("SEGMENT"));
			entity.setType(rs.getString("TYPE"));
			entity.setMatBuck1(rs.getString("MAT_BUCK_1"));
			entity.setExRateBuy(rs.getBigDecimal("EX_RATE_BUY"));
			entity.setExRateSell(rs.getBigDecimal("EX_RATE_SELL"));
			entity.setNotice0to31(rs.getBigDecimal("NOTICE_0TO31"));
			entity.setNotice32to88(rs.getBigDecimal("NOTICE_32TO88"));
			entity.setCerOfDepo(rs.getBigDecimal("CER_OF_DEPO"));
			entity.setImportValue(rs.getBigDecimal("IMPORT"));
			entity.setInvestment(rs.getBigDecimal("INVESTMENT"));
			entity.setOther(rs.getBigDecimal("OTHER"));
			entity.setResidents(rs.getBigDecimal("RESIDENTS"));
			entity.setNonResidents(rs.getBigDecimal("NON_RESIDENTS"));
			entity.setSno(rs.getObject("SNO") != null ? rs.getLong("SNO") : null);
			entity.setReportCode(rs.getString("REPORT_CODE"));
			return entity;
		}
	}

	// ------------------------------
	// ROW MAPPER FOR M_DEP3 ARCHIVAL SUMMARY
	// ------------------------------
	public static class M_DEP3_Archival_Summary_EntityRowMapper implements RowMapper<M_DEP3_Archival_Summary_Entity> {
		@Override
		public M_DEP3_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_DEP3_Archival_Summary_Entity entity = new M_DEP3_Archival_Summary_Entity();
			entity.setR11_foreign_curr_acc_by_curr(rs.getString("r11_foreign_curr_acc_by_curr"));
			entity.setR11_ex_rate_buy(rs.getBigDecimal("r11_ex_rate_buy"));
			entity.setR11_ex_rate_mid(rs.getBigDecimal("r11_ex_rate_mid"));
			entity.setR11_ex_rate_sell(rs.getBigDecimal("r11_ex_rate_sell"));
			entity.setR11_current(rs.getBigDecimal("r11_current"));
			entity.setR11_call(rs.getBigDecimal("r11_call"));
			entity.setR11_savings(rs.getBigDecimal("r11_savings"));
			entity.setR11_notice_0to31(rs.getBigDecimal("r11_notice_0to31"));
			entity.setR11_notice_32to88(rs.getBigDecimal("r11_notice_32to88"));
			entity.setR11_fix_depo_91_day_depo(rs.getBigDecimal("r11_fix_depo_91_day_depo"));
			entity.setR11_fix_depo_1to2(rs.getBigDecimal("r11_fix_depo_1to2"));
			entity.setR11_fix_depo_4to6(rs.getBigDecimal("r11_fix_depo_4to6"));
			entity.setR11_fix_depo_7to12(rs.getBigDecimal("r11_fix_depo_7to12"));
			entity.setR11_fix_depo_13to18(rs.getBigDecimal("r11_fix_depo_13to18"));
			entity.setR11_fix_depo_19to24(rs.getBigDecimal("r11_fix_depo_19to24"));
			entity.setR11_fix_depo_over24(rs.getBigDecimal("r11_fix_depo_over24"));
			entity.setR11_cer_of_depo(rs.getBigDecimal("r11_cer_of_depo"));
			entity.setR11_total(rs.getBigDecimal("r11_total"));
			entity.setR11_pula_equivalent(rs.getBigDecimal("r11_pula_equivalent"));
			entity.setR11_avg_pula_equivalent(rs.getBigDecimal("r11_avg_pula_equivalent"));
			entity.setR12_foreign_curr_acc_by_curr(rs.getString("r12_foreign_curr_acc_by_curr"));
			entity.setR12_ex_rate_buy(rs.getBigDecimal("r12_ex_rate_buy"));
			entity.setR12_ex_rate_mid(rs.getBigDecimal("r12_ex_rate_mid"));
			entity.setR12_ex_rate_sell(rs.getBigDecimal("r12_ex_rate_sell"));
			entity.setR12_current(rs.getBigDecimal("r12_current"));
			entity.setR12_call(rs.getBigDecimal("r12_call"));
			entity.setR12_savings(rs.getBigDecimal("r12_savings"));
			entity.setR12_notice_0to31(rs.getBigDecimal("r12_notice_0to31"));
			entity.setR12_notice_32to88(rs.getBigDecimal("r12_notice_32to88"));
			entity.setR12_fix_depo_91_day_depo(rs.getBigDecimal("r12_fix_depo_91_day_depo"));
			entity.setR12_fix_depo_1to2(rs.getBigDecimal("r12_fix_depo_1to2"));
			entity.setR12_fix_depo_4to6(rs.getBigDecimal("r12_fix_depo_4to6"));
			entity.setR12_fix_depo_7to12(rs.getBigDecimal("r12_fix_depo_7to12"));
			entity.setR12_fix_depo_13to18(rs.getBigDecimal("r12_fix_depo_13to18"));
			entity.setR12_fix_depo_19to24(rs.getBigDecimal("r12_fix_depo_19to24"));
			entity.setR12_fix_depo_over24(rs.getBigDecimal("r12_fix_depo_over24"));
			entity.setR12_cer_of_depo(rs.getBigDecimal("r12_cer_of_depo"));
			entity.setR12_total(rs.getBigDecimal("r12_total"));
			entity.setR12_pula_equivalent(rs.getBigDecimal("r12_pula_equivalent"));
			entity.setR12_avg_pula_equivalent(rs.getBigDecimal("r12_avg_pula_equivalent"));
			entity.setR13_foreign_curr_acc_by_curr(rs.getString("r13_foreign_curr_acc_by_curr"));
			entity.setR13_ex_rate_buy(rs.getBigDecimal("r13_ex_rate_buy"));
			entity.setR13_ex_rate_mid(rs.getBigDecimal("r13_ex_rate_mid"));
			entity.setR13_ex_rate_sell(rs.getBigDecimal("r13_ex_rate_sell"));
			entity.setR13_current(rs.getBigDecimal("r13_current"));
			entity.setR13_call(rs.getBigDecimal("r13_call"));
			entity.setR13_savings(rs.getBigDecimal("r13_savings"));
			entity.setR13_notice_0to31(rs.getBigDecimal("r13_notice_0to31"));
			entity.setR13_notice_32to88(rs.getBigDecimal("r13_notice_32to88"));
			entity.setR13_fix_depo_91_day_depo(rs.getBigDecimal("r13_fix_depo_91_day_depo"));
			entity.setR13_fix_depo_1to2(rs.getBigDecimal("r13_fix_depo_1to2"));
			entity.setR13_fix_depo_4to6(rs.getBigDecimal("r13_fix_depo_4to6"));
			entity.setR13_fix_depo_7to12(rs.getBigDecimal("r13_fix_depo_7to12"));
			entity.setR13_fix_depo_13to18(rs.getBigDecimal("r13_fix_depo_13to18"));
			entity.setR13_fix_depo_19to24(rs.getBigDecimal("r13_fix_depo_19to24"));
			entity.setR13_fix_depo_over24(rs.getBigDecimal("r13_fix_depo_over24"));
			entity.setR13_cer_of_depo(rs.getBigDecimal("r13_cer_of_depo"));
			entity.setR13_total(rs.getBigDecimal("r13_total"));
			entity.setR13_pula_equivalent(rs.getBigDecimal("r13_pula_equivalent"));
			entity.setR13_avg_pula_equivalent(rs.getBigDecimal("r13_avg_pula_equivalent"));
			entity.setR14_foreign_curr_acc_by_curr(rs.getString("r14_foreign_curr_acc_by_curr"));
			entity.setR14_ex_rate_buy(rs.getBigDecimal("r14_ex_rate_buy"));
			entity.setR14_ex_rate_mid(rs.getBigDecimal("r14_ex_rate_mid"));
			entity.setR14_ex_rate_sell(rs.getBigDecimal("r14_ex_rate_sell"));
			entity.setR14_current(rs.getBigDecimal("r14_current"));
			entity.setR14_call(rs.getBigDecimal("r14_call"));
			entity.setR14_savings(rs.getBigDecimal("r14_savings"));
			entity.setR14_notice_0to31(rs.getBigDecimal("r14_notice_0to31"));
			entity.setR14_notice_32to88(rs.getBigDecimal("r14_notice_32to88"));
			entity.setR14_fix_depo_91_day_depo(rs.getBigDecimal("r14_fix_depo_91_day_depo"));
			entity.setR14_fix_depo_1to2(rs.getBigDecimal("r14_fix_depo_1to2"));
			entity.setR14_fix_depo_4to6(rs.getBigDecimal("r14_fix_depo_4to6"));
			entity.setR14_fix_depo_7to12(rs.getBigDecimal("r14_fix_depo_7to12"));
			entity.setR14_fix_depo_13to18(rs.getBigDecimal("r14_fix_depo_13to18"));
			entity.setR14_fix_depo_19to24(rs.getBigDecimal("r14_fix_depo_19to24"));
			entity.setR14_fix_depo_over24(rs.getBigDecimal("r14_fix_depo_over24"));
			entity.setR14_cer_of_depo(rs.getBigDecimal("r14_cer_of_depo"));
			entity.setR14_total(rs.getBigDecimal("r14_total"));
			entity.setR14_pula_equivalent(rs.getBigDecimal("r14_pula_equivalent"));
			entity.setR14_avg_pula_equivalent(rs.getBigDecimal("r14_avg_pula_equivalent"));
			entity.setR15_foreign_curr_acc_by_curr(rs.getString("r15_foreign_curr_acc_by_curr"));
			entity.setR15_ex_rate_buy(rs.getBigDecimal("r15_ex_rate_buy"));
			entity.setR15_ex_rate_mid(rs.getBigDecimal("r15_ex_rate_mid"));
			entity.setR15_ex_rate_sell(rs.getBigDecimal("r15_ex_rate_sell"));
			entity.setR15_current(rs.getBigDecimal("r15_current"));
			entity.setR15_call(rs.getBigDecimal("r15_call"));
			entity.setR15_savings(rs.getBigDecimal("r15_savings"));
			entity.setR15_notice_0to31(rs.getBigDecimal("r15_notice_0to31"));
			entity.setR15_notice_32to88(rs.getBigDecimal("r15_notice_32to88"));
			entity.setR15_fix_depo_91_day_depo(rs.getBigDecimal("r15_fix_depo_91_day_depo"));
			entity.setR15_fix_depo_1to2(rs.getBigDecimal("r15_fix_depo_1to2"));
			entity.setR15_fix_depo_4to6(rs.getBigDecimal("r15_fix_depo_4to6"));
			entity.setR15_fix_depo_7to12(rs.getBigDecimal("r15_fix_depo_7to12"));
			entity.setR15_fix_depo_13to18(rs.getBigDecimal("r15_fix_depo_13to18"));
			entity.setR15_fix_depo_19to24(rs.getBigDecimal("r15_fix_depo_19to24"));
			entity.setR15_fix_depo_over24(rs.getBigDecimal("r15_fix_depo_over24"));
			entity.setR15_cer_of_depo(rs.getBigDecimal("r15_cer_of_depo"));
			entity.setR15_total(rs.getBigDecimal("r15_total"));
			entity.setR15_pula_equivalent(rs.getBigDecimal("r15_pula_equivalent"));
			entity.setR15_avg_pula_equivalent(rs.getBigDecimal("r15_avg_pula_equivalent"));
			entity.setR16_foreign_curr_acc_by_curr(rs.getString("r16_foreign_curr_acc_by_curr"));
			entity.setR16_ex_rate_buy(rs.getBigDecimal("r16_ex_rate_buy"));
			entity.setR16_ex_rate_mid(rs.getBigDecimal("r16_ex_rate_mid"));
			entity.setR16_ex_rate_sell(rs.getBigDecimal("r16_ex_rate_sell"));
			entity.setR16_current(rs.getBigDecimal("r16_current"));
			entity.setR16_call(rs.getBigDecimal("r16_call"));
			entity.setR16_savings(rs.getBigDecimal("r16_savings"));
			entity.setR16_notice_0to31(rs.getBigDecimal("r16_notice_0to31"));
			entity.setR16_notice_32to88(rs.getBigDecimal("r16_notice_32to88"));
			entity.setR16_fix_depo_91_day_depo(rs.getBigDecimal("r16_fix_depo_91_day_depo"));
			entity.setR16_fix_depo_1to2(rs.getBigDecimal("r16_fix_depo_1to2"));
			entity.setR16_fix_depo_4to6(rs.getBigDecimal("r16_fix_depo_4to6"));
			entity.setR16_fix_depo_7to12(rs.getBigDecimal("r16_fix_depo_7to12"));
			entity.setR16_fix_depo_13to18(rs.getBigDecimal("r16_fix_depo_13to18"));
			entity.setR16_fix_depo_19to24(rs.getBigDecimal("r16_fix_depo_19to24"));
			entity.setR16_fix_depo_over24(rs.getBigDecimal("r16_fix_depo_over24"));
			entity.setR16_cer_of_depo(rs.getBigDecimal("r16_cer_of_depo"));
			entity.setR16_total(rs.getBigDecimal("r16_total"));
			entity.setR16_pula_equivalent(rs.getBigDecimal("r16_pula_equivalent"));
			entity.setR16_avg_pula_equivalent(rs.getBigDecimal("r16_avg_pula_equivalent"));
			entity.setR17_foreign_curr_acc_by_curr(rs.getString("r17_foreign_curr_acc_by_curr"));
			entity.setR17_ex_rate_buy(rs.getBigDecimal("r17_ex_rate_buy"));
			entity.setR17_ex_rate_mid(rs.getBigDecimal("r17_ex_rate_mid"));
			entity.setR17_ex_rate_sell(rs.getBigDecimal("r17_ex_rate_sell"));
			entity.setR17_current(rs.getBigDecimal("r17_current"));
			entity.setR17_call(rs.getBigDecimal("r17_call"));
			entity.setR17_savings(rs.getBigDecimal("r17_savings"));
			entity.setR17_notice_0to31(rs.getBigDecimal("r17_notice_0to31"));
			entity.setR17_notice_32to88(rs.getBigDecimal("r17_notice_32to88"));
			entity.setR17_fix_depo_91_day_depo(rs.getBigDecimal("r17_fix_depo_91_day_depo"));
			entity.setR17_fix_depo_1to2(rs.getBigDecimal("r17_fix_depo_1to2"));
			entity.setR17_fix_depo_4to6(rs.getBigDecimal("r17_fix_depo_4to6"));
			entity.setR17_fix_depo_7to12(rs.getBigDecimal("r17_fix_depo_7to12"));
			entity.setR17_fix_depo_13to18(rs.getBigDecimal("r17_fix_depo_13to18"));
			entity.setR17_fix_depo_19to24(rs.getBigDecimal("r17_fix_depo_19to24"));
			entity.setR17_fix_depo_over24(rs.getBigDecimal("r17_fix_depo_over24"));
			entity.setR17_cer_of_depo(rs.getBigDecimal("r17_cer_of_depo"));
			entity.setR17_total(rs.getBigDecimal("r17_total"));
			entity.setR17_pula_equivalent(rs.getBigDecimal("r17_pula_equivalent"));
			entity.setR17_avg_pula_equivalent(rs.getBigDecimal("r17_avg_pula_equivalent"));
			entity.setR18_foreign_curr_acc_by_curr(rs.getString("r18_foreign_curr_acc_by_curr"));
			entity.setR18_ex_rate_buy(rs.getBigDecimal("r18_ex_rate_buy"));
			entity.setR18_ex_rate_mid(rs.getBigDecimal("r18_ex_rate_mid"));
			entity.setR18_ex_rate_sell(rs.getBigDecimal("r18_ex_rate_sell"));
			entity.setR18_current(rs.getBigDecimal("r18_current"));
			entity.setR18_call(rs.getBigDecimal("r18_call"));
			entity.setR18_savings(rs.getBigDecimal("r18_savings"));
			entity.setR18_notice_0to31(rs.getBigDecimal("r18_notice_0to31"));
			entity.setR18_notice_32to88(rs.getBigDecimal("r18_notice_32to88"));
			entity.setR18_fix_depo_91_day_depo(rs.getBigDecimal("r18_fix_depo_91_day_depo"));
			entity.setR18_fix_depo_1to2(rs.getBigDecimal("r18_fix_depo_1to2"));
			entity.setR18_fix_depo_4to6(rs.getBigDecimal("r18_fix_depo_4to6"));
			entity.setR18_fix_depo_7to12(rs.getBigDecimal("r18_fix_depo_7to12"));
			entity.setR18_fix_depo_13to18(rs.getBigDecimal("r18_fix_depo_13to18"));
			entity.setR18_fix_depo_19to24(rs.getBigDecimal("r18_fix_depo_19to24"));
			entity.setR18_fix_depo_over24(rs.getBigDecimal("r18_fix_depo_over24"));
			entity.setR18_cer_of_depo(rs.getBigDecimal("r18_cer_of_depo"));
			entity.setR18_total(rs.getBigDecimal("r18_total"));
			entity.setR18_pula_equivalent(rs.getBigDecimal("r18_pula_equivalent"));
			entity.setR18_avg_pula_equivalent(rs.getBigDecimal("r18_avg_pula_equivalent"));
			entity.setR28_import(rs.getBigDecimal("r28_import"));
			entity.setR28_investment(rs.getBigDecimal("r28_investment"));
			entity.setR28_other(rs.getBigDecimal("r28_other"));
			entity.setR29_import(rs.getBigDecimal("r29_import"));
			entity.setR29_investment(rs.getBigDecimal("r29_investment"));
			entity.setR29_other(rs.getBigDecimal("r29_other"));
			entity.setR30_import(rs.getBigDecimal("r30_import"));
			entity.setR30_investment(rs.getBigDecimal("r30_investment"));
			entity.setR30_other(rs.getBigDecimal("r30_other"));
			entity.setR31_import(rs.getBigDecimal("r31_import"));
			entity.setR31_investment(rs.getBigDecimal("r31_investment"));
			entity.setR31_other(rs.getBigDecimal("r31_other"));
			entity.setR32_import(rs.getBigDecimal("r32_import"));
			entity.setR32_investment(rs.getBigDecimal("r32_investment"));
			entity.setR32_other(rs.getBigDecimal("r32_other"));
			entity.setR33_import(rs.getBigDecimal("r33_import"));
			entity.setR33_investment(rs.getBigDecimal("r33_investment"));
			entity.setR33_other(rs.getBigDecimal("r33_other"));
			entity.setR34_import(rs.getBigDecimal("r34_import"));
			entity.setR34_investment(rs.getBigDecimal("r34_investment"));
			entity.setR34_other(rs.getBigDecimal("r34_other"));
			entity.setR28_residents(rs.getBigDecimal("r28_residents"));
			entity.setR28_non_residents(rs.getBigDecimal("r28_non_residents"));
			entity.setR29_residents(rs.getBigDecimal("r29_residents"));
			entity.setR29_non_residents(rs.getBigDecimal("r29_non_residents"));
			entity.setR30_residents(rs.getBigDecimal("r30_residents"));
			entity.setR30_non_residents(rs.getBigDecimal("r30_non_residents"));
			entity.setR31_residents(rs.getBigDecimal("r31_residents"));
			entity.setR31_non_residents(rs.getBigDecimal("r31_non_residents"));
			entity.setR32_residents(rs.getBigDecimal("r32_residents"));
			entity.setR32_non_residents(rs.getBigDecimal("r32_non_residents"));
			entity.setR33_residents(rs.getBigDecimal("r33_residents"));
			entity.setR33_non_residents(rs.getBigDecimal("r33_non_residents"));
			entity.setR34_residents(rs.getBigDecimal("r34_residents"));
			entity.setR34_non_residents(rs.getBigDecimal("r34_non_residents"));
			entity.setReportDate(rs.getTimestamp("REPORT_DATE"));
			entity.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			entity.setReportResubDate(rs.getTimestamp("REPORT_RESUBDATE"));
			entity.setReport_frequency(rs.getString("report_frequency"));
			entity.setReport_code(rs.getString("report_code"));
			entity.setReport_desc(rs.getString("report_desc"));
			entity.setEntity_flg(rs.getString("entity_flg"));
			entity.setModify_flg(rs.getString("modify_flg"));
			entity.setDel_flg(rs.getString("del_flg"));
			return entity;
		}
	}

	// ------------------------------
	// ROW MAPPER FOR M_DEP3 ARCHIVAL DETAIL
	// ------------------------------
	public static class M_DEP3_Archival_Detail_EntityRowMapper implements RowMapper<M_DEP3_Archival_Detail_Entity> {
		@Override
		public M_DEP3_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_DEP3_Archival_Detail_Entity entity = new M_DEP3_Archival_Detail_Entity();
			entity.setCustId(rs.getString("CUST_ID"));
			entity.setAcctNumber(rs.getString("ACCT_NUMBER"));
			entity.setAcctName(rs.getString("ACCT_NAME"));
			entity.setDataType(rs.getString("DATA_TYPE"));
			entity.setReportLabel(rs.getString("REPORT_LABEL"));
			entity.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			entity.setReportRemarks(rs.getString("REPORT_REMARKS"));
			entity.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			entity.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			entity.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			entity.setReportDate(rs.getTimestamp("REPORT_DATE"));
			entity.setReportName(rs.getString("REPORT_NAME"));
			entity.setCreateUser(rs.getString("CREATE_USER"));
			entity.setCreateTime(rs.getTimestamp("CREATE_TIME"));
			entity.setModifyUser(rs.getString("MODIFY_USER"));
			entity.setModifyTime(rs.getTimestamp("MODIFY_TIME"));
			entity.setVerifyUser(rs.getString("VERIFY_USER"));
			entity.setVerifyTime(rs.getTimestamp("VERIFY_TIME"));
			entity.setEntityFlg(rs.getString("ENTITY_FLG"));
			entity.setModifyFlg(rs.getString("MODIFY_FLG"));
			entity.setDelFlg(rs.getString("DEL_FLG"));
			entity.setCcy(rs.getString("CCY"));
			entity.setSegment(rs.getString("SEGMENT"));
			entity.setType(rs.getString("TYPE"));
			entity.setMatBuck1(rs.getString("MAT_BUCK_1"));
			entity.setExRateBuy(rs.getBigDecimal("EX_RATE_BUY"));
			entity.setExRateSell(rs.getBigDecimal("EX_RATE_SELL"));
			entity.setNotice0to31(rs.getBigDecimal("NOTICE_0TO31"));
			entity.setNotice32to88(rs.getBigDecimal("NOTICE_32TO88"));
			entity.setCerOfDepo(rs.getBigDecimal("CER_OF_DEPO"));
			entity.setImportValue(rs.getBigDecimal("IMPORT"));
			entity.setInvestment(rs.getBigDecimal("INVESTMENT"));
			entity.setOther(rs.getBigDecimal("OTHER"));
			entity.setResidents(rs.getBigDecimal("RESIDENTS"));
			entity.setNonResidents(rs.getBigDecimal("NON_RESIDENTS"));
			entity.setSno(rs.getObject("SNO") != null ? rs.getLong("SNO") : null);
			entity.setReportCode(rs.getString("REPORT_CODE"));
			return entity;
		}
	}

	// ------------------------------
	// ROW MAPPER FOR M_DEP3 MANUAL SUMMARY
	// ------------------------------
	public static class M_DEP3_Manual_Summary_EntityRowMapper implements RowMapper<M_DEP3_Manual_Summary_Entity> {
		@Override
		public M_DEP3_Manual_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_DEP3_Manual_Summary_Entity entity = new M_DEP3_Manual_Summary_Entity();
			entity.setReport_date(rs.getTimestamp("report_date"));
			entity.setReport_version(rs.getBigDecimal("report_version"));
			entity.setReport_frequency(rs.getString("report_frequency"));
			entity.setReport_code(rs.getString("report_code"));
			entity.setReport_desc(rs.getString("report_desc"));
			entity.setEntity_flg(rs.getString("entity_flg"));
			entity.setModify_flg(rs.getString("modify_flg"));
			entity.setDel_flg(rs.getString("del_flg"));
			entity.setR11_ex_rate_buy(rs.getBigDecimal("r11_ex_rate_buy"));
			entity.setR11_ex_rate_mid(rs.getBigDecimal("r11_ex_rate_mid"));
			entity.setR11_ex_rate_sell(rs.getBigDecimal("r11_ex_rate_sell"));
			entity.setR11_notice_0to31(rs.getBigDecimal("r11_notice_0to31"));
			entity.setR11_notice_32to88(rs.getBigDecimal("r11_notice_32to88"));
			entity.setR11_cer_of_depo(rs.getBigDecimal("r11_cer_of_depo"));
			entity.setR12_ex_rate_buy(rs.getBigDecimal("r12_ex_rate_buy"));
			entity.setR12_ex_rate_mid(rs.getBigDecimal("r12_ex_rate_mid"));
			entity.setR12_ex_rate_sell(rs.getBigDecimal("r12_ex_rate_sell"));
			entity.setR12_notice_0to31(rs.getBigDecimal("r12_notice_0to31"));
			entity.setR12_notice_32to88(rs.getBigDecimal("r12_notice_32to88"));
			entity.setR12_cer_of_depo(rs.getBigDecimal("r12_cer_of_depo"));
			entity.setR13_ex_rate_buy(rs.getBigDecimal("r13_ex_rate_buy"));
			entity.setR13_ex_rate_mid(rs.getBigDecimal("r13_ex_rate_mid"));
			entity.setR13_ex_rate_sell(rs.getBigDecimal("r13_ex_rate_sell"));
			entity.setR13_notice_0to31(rs.getBigDecimal("r13_notice_0to31"));
			entity.setR13_notice_32to88(rs.getBigDecimal("r13_notice_32to88"));
			entity.setR13_cer_of_depo(rs.getBigDecimal("r13_cer_of_depo"));
			entity.setR14_ex_rate_buy(rs.getBigDecimal("r14_ex_rate_buy"));
			entity.setR14_ex_rate_mid(rs.getBigDecimal("r14_ex_rate_mid"));
			entity.setR14_ex_rate_sell(rs.getBigDecimal("r14_ex_rate_sell"));
			entity.setR14_notice_0to31(rs.getBigDecimal("r14_notice_0to31"));
			entity.setR14_notice_32to88(rs.getBigDecimal("r14_notice_32to88"));
			entity.setR14_cer_of_depo(rs.getBigDecimal("r14_cer_of_depo"));
			entity.setR15_ex_rate_buy(rs.getBigDecimal("r15_ex_rate_buy"));
			entity.setR15_ex_rate_mid(rs.getBigDecimal("r15_ex_rate_mid"));
			entity.setR15_ex_rate_sell(rs.getBigDecimal("r15_ex_rate_sell"));
			entity.setR15_notice_0to31(rs.getBigDecimal("r15_notice_0to31"));
			entity.setR15_notice_32to88(rs.getBigDecimal("r15_notice_32to88"));
			entity.setR15_cer_of_depo(rs.getBigDecimal("r15_cer_of_depo"));
			entity.setR16_ex_rate_buy(rs.getBigDecimal("r16_ex_rate_buy"));
			entity.setR16_ex_rate_mid(rs.getBigDecimal("r16_ex_rate_mid"));
			entity.setR16_ex_rate_sell(rs.getBigDecimal("r16_ex_rate_sell"));
			entity.setR16_notice_0to31(rs.getBigDecimal("r16_notice_0to31"));
			entity.setR16_notice_32to88(rs.getBigDecimal("r16_notice_32to88"));
			entity.setR16_cer_of_depo(rs.getBigDecimal("r16_cer_of_depo"));
			entity.setR18_notice_0to31(rs.getBigDecimal("r18_notice_0to31"));
			entity.setR18_notice_32to88(rs.getBigDecimal("r18_notice_32to88"));
			entity.setR18_cer_of_depo(rs.getBigDecimal("r18_cer_of_depo"));
			entity.setR28_import(rs.getBigDecimal("r28_import"));
			entity.setR28_investment(rs.getBigDecimal("r28_investment"));
			entity.setR28_other(rs.getBigDecimal("r28_other"));
			entity.setR29_import(rs.getBigDecimal("r29_import"));
			entity.setR29_investment(rs.getBigDecimal("r29_investment"));
			entity.setR29_other(rs.getBigDecimal("r29_other"));
			entity.setR30_import(rs.getBigDecimal("r30_import"));
			entity.setR30_investment(rs.getBigDecimal("r30_investment"));
			entity.setR30_other(rs.getBigDecimal("r30_other"));
			entity.setR31_import(rs.getBigDecimal("r31_import"));
			entity.setR31_investment(rs.getBigDecimal("r31_investment"));
			entity.setR31_other(rs.getBigDecimal("r31_other"));
			entity.setR32_import(rs.getBigDecimal("r32_import"));
			entity.setR32_investment(rs.getBigDecimal("r32_investment"));
			entity.setR32_other(rs.getBigDecimal("r32_other"));
			entity.setR33_import(rs.getBigDecimal("r33_import"));
			entity.setR33_investment(rs.getBigDecimal("r33_investment"));
			entity.setR33_other(rs.getBigDecimal("r33_other"));
			entity.setR34_import(rs.getBigDecimal("r34_import"));
			entity.setR34_investment(rs.getBigDecimal("r34_investment"));
			entity.setR34_other(rs.getBigDecimal("r34_other"));
			entity.setR28_residents(rs.getBigDecimal("r28_residents"));
			entity.setR28_non_residents(rs.getBigDecimal("r28_non_residents"));
			entity.setR29_residents(rs.getBigDecimal("r29_residents"));
			entity.setR29_non_residents(rs.getBigDecimal("r29_non_residents"));
			entity.setR30_residents(rs.getBigDecimal("r30_residents"));
			entity.setR30_non_residents(rs.getBigDecimal("r30_non_residents"));
			entity.setR31_residents(rs.getBigDecimal("r31_residents"));
			entity.setR31_non_residents(rs.getBigDecimal("r31_non_residents"));
			entity.setR32_residents(rs.getBigDecimal("r32_residents"));
			entity.setR32_non_residents(rs.getBigDecimal("r32_non_residents"));
			entity.setR33_residents(rs.getBigDecimal("r33_residents"));
			entity.setR33_non_residents(rs.getBigDecimal("r33_non_residents"));
			entity.setR34_residents(rs.getBigDecimal("r34_residents"));
			entity.setR34_non_residents(rs.getBigDecimal("r34_non_residents"));
			return entity;
		}
	}

	// ------------------------------
	// ROW MAPPER FOR M_DEP3 MANUAL ARCHIVAL SUMMARY
	// ------------------------------
	public static class M_DEP3_Manual_Archival_Summary_EntityRowMapper
			implements RowMapper<M_DEP3_Manual_Archival_Summary_Entity> {
		@Override
		public M_DEP3_Manual_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_DEP3_Manual_Archival_Summary_Entity entity = new M_DEP3_Manual_Archival_Summary_Entity();
			entity.setReport_date(rs.getTimestamp("report_date"));
			entity.setReport_version(rs.getBigDecimal("report_version"));
			entity.setReport_frequency(rs.getString("report_frequency"));
			entity.setReport_code(rs.getString("report_code"));
			entity.setReport_desc(rs.getString("report_desc"));
			entity.setEntity_flg(rs.getString("entity_flg"));
			entity.setModify_flg(rs.getString("modify_flg"));
			entity.setDel_flg(rs.getString("del_flg"));
			entity.setR11_ex_rate_buy(rs.getBigDecimal("r11_ex_rate_buy"));
			entity.setR11_ex_rate_mid(rs.getBigDecimal("r11_ex_rate_mid"));
			entity.setR11_ex_rate_sell(rs.getBigDecimal("r11_ex_rate_sell"));
			entity.setR11_notice_0to31(rs.getBigDecimal("r11_notice_0to31"));
			entity.setR11_notice_32to88(rs.getBigDecimal("r11_notice_32to88"));
			entity.setR11_cer_of_depo(rs.getBigDecimal("r11_cer_of_depo"));
			entity.setR12_ex_rate_buy(rs.getBigDecimal("r12_ex_rate_buy"));
			entity.setR12_ex_rate_mid(rs.getBigDecimal("r12_ex_rate_mid"));
			entity.setR12_ex_rate_sell(rs.getBigDecimal("r12_ex_rate_sell"));
			entity.setR12_notice_0to31(rs.getBigDecimal("r12_notice_0to31"));
			entity.setR12_notice_32to88(rs.getBigDecimal("r12_notice_32to88"));
			entity.setR12_cer_of_depo(rs.getBigDecimal("r12_cer_of_depo"));
			entity.setR13_ex_rate_buy(rs.getBigDecimal("r13_ex_rate_buy"));
			entity.setR13_ex_rate_mid(rs.getBigDecimal("r13_ex_rate_mid"));
			entity.setR13_ex_rate_sell(rs.getBigDecimal("r13_ex_rate_sell"));
			entity.setR13_notice_0to31(rs.getBigDecimal("r13_notice_0to31"));
			entity.setR13_notice_32to88(rs.getBigDecimal("r13_notice_32to88"));
			entity.setR13_cer_of_depo(rs.getBigDecimal("r13_cer_of_depo"));
			entity.setR14_ex_rate_buy(rs.getBigDecimal("r14_ex_rate_buy"));
			entity.setR14_ex_rate_mid(rs.getBigDecimal("r14_ex_rate_mid"));
			entity.setR14_ex_rate_sell(rs.getBigDecimal("r14_ex_rate_sell"));
			entity.setR14_notice_0to31(rs.getBigDecimal("r14_notice_0to31"));
			entity.setR14_notice_32to88(rs.getBigDecimal("r14_notice_32to88"));
			entity.setR14_cer_of_depo(rs.getBigDecimal("r14_cer_of_depo"));
			entity.setR15_ex_rate_buy(rs.getBigDecimal("r15_ex_rate_buy"));
			entity.setR15_ex_rate_mid(rs.getBigDecimal("r15_ex_rate_mid"));
			entity.setR15_ex_rate_sell(rs.getBigDecimal("r15_ex_rate_sell"));
			entity.setR15_notice_0to31(rs.getBigDecimal("r15_notice_0to31"));
			entity.setR15_notice_32to88(rs.getBigDecimal("r15_notice_32to88"));
			entity.setR15_cer_of_depo(rs.getBigDecimal("r15_cer_of_depo"));
			entity.setR16_ex_rate_buy(rs.getBigDecimal("r16_ex_rate_buy"));
			entity.setR16_ex_rate_mid(rs.getBigDecimal("r16_ex_rate_mid"));
			entity.setR16_ex_rate_sell(rs.getBigDecimal("r16_ex_rate_sell"));
			entity.setR16_notice_0to31(rs.getBigDecimal("r16_notice_0to31"));
			entity.setR16_notice_32to88(rs.getBigDecimal("r16_notice_32to88"));
			entity.setR16_cer_of_depo(rs.getBigDecimal("r16_cer_of_depo"));
			entity.setR18_notice_0to31(rs.getBigDecimal("r18_notice_0to31"));
			entity.setR18_notice_32to88(rs.getBigDecimal("r18_notice_32to88"));
			entity.setR18_cer_of_depo(rs.getBigDecimal("r18_cer_of_depo"));
			entity.setR28_import(rs.getBigDecimal("r28_import"));
			entity.setR28_investment(rs.getBigDecimal("r28_investment"));
			entity.setR28_other(rs.getBigDecimal("r28_other"));
			entity.setR29_import(rs.getBigDecimal("r29_import"));
			entity.setR29_investment(rs.getBigDecimal("r29_investment"));
			entity.setR29_other(rs.getBigDecimal("r29_other"));
			entity.setR30_import(rs.getBigDecimal("r30_import"));
			entity.setR30_investment(rs.getBigDecimal("r30_investment"));
			entity.setR30_other(rs.getBigDecimal("r30_other"));
			entity.setR31_import(rs.getBigDecimal("r31_import"));
			entity.setR31_investment(rs.getBigDecimal("r31_investment"));
			entity.setR31_other(rs.getBigDecimal("r31_other"));
			entity.setR32_import(rs.getBigDecimal("r32_import"));
			entity.setR32_investment(rs.getBigDecimal("r32_investment"));
			entity.setR32_other(rs.getBigDecimal("r32_other"));
			entity.setR33_import(rs.getBigDecimal("r33_import"));
			entity.setR33_investment(rs.getBigDecimal("r33_investment"));
			entity.setR33_other(rs.getBigDecimal("r33_other"));
			entity.setR34_import(rs.getBigDecimal("r34_import"));
			entity.setR34_investment(rs.getBigDecimal("r34_investment"));
			entity.setR34_other(rs.getBigDecimal("r34_other"));
			entity.setR28_residents(rs.getBigDecimal("r28_residents"));
			entity.setR28_non_residents(rs.getBigDecimal("r28_non_residents"));
			entity.setR29_residents(rs.getBigDecimal("r29_residents"));
			entity.setR29_non_residents(rs.getBigDecimal("r29_non_residents"));
			entity.setR30_residents(rs.getBigDecimal("r30_residents"));
			entity.setR30_non_residents(rs.getBigDecimal("r30_non_residents"));
			entity.setR31_residents(rs.getBigDecimal("r31_residents"));
			entity.setR31_non_residents(rs.getBigDecimal("r31_non_residents"));
			entity.setR32_residents(rs.getBigDecimal("r32_residents"));
			entity.setR32_non_residents(rs.getBigDecimal("r32_non_residents"));
			entity.setR33_residents(rs.getBigDecimal("r33_residents"));
			entity.setR33_non_residents(rs.getBigDecimal("r33_non_residents"));
			entity.setR34_residents(rs.getBigDecimal("r34_residents"));
			entity.setR34_non_residents(rs.getBigDecimal("r34_non_residents"));
			return entity;
		}
	}

}
