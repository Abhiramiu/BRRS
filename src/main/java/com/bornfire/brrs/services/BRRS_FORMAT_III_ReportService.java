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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_FORMAT_III_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_FORMAT_III_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_FORMAT_III_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_FORMAT_III_Summary_Repo;
import com.bornfire.brrs.entities.FORMAT_III_Archival_Detail_Entity;
import com.bornfire.brrs.entities.FORMAT_III_Archival_Summary_Entity;
import com.bornfire.brrs.entities.FORMAT_III_Detail_Entity;
import com.bornfire.brrs.entities.FORMAT_III_Summary_Entity;

@Component
@Service

public class BRRS_FORMAT_III_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_FORMAT_III_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_FORMAT_III_Summary_Repo FORMAT_III_summary_repo;

	@Autowired
	BRRS_FORMAT_III_Archival_Summary_Repo FORMAT_III_Archival_Summary_Repo;

	@Autowired
	BRRS_FORMAT_III_Detail_Repo FORMAT_III_detail_repo;

	@Autowired
	BRRS_FORMAT_III_Archival_Detail_Repo FORMAT_III_Archival_Detail_Repo;


	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getFORMAT_IIIView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		try {
			Date d1 = dateformat.parse(todate);

			// ---------- CASE 1: ARCHIVAL ----------
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				List<FORMAT_III_Archival_Summary_Entity> T1Master = FORMAT_III_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);
				mv.addObject("reportsummary", T1Master);
				System.out.println("T1Master Size " + T1Master.size());

			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<FORMAT_III_Summary_Entity> T1Master = FORMAT_III_summary_repo
						.getdatabydateList(dateformat.parse(todate));

				mv.addObject("reportsummary", T1Master);
				System.out.println("T1Master Size " + T1Master.size());
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/FORMAT_III");
		mv.addObject("displaymode", "summary");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	public void updateReport(FORMAT_III_Summary_Entity updatedEntity) {

		FORMAT_III_Summary_Entity existing = FORMAT_III_summary_repo.findById(updatedEntity.getReportDate()).orElseThrow(
				() -> new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

		int[] rows = { 12, 18, 19, 21, 22, 23, 24, 25, 26, 27, 28, 29, 42, 54, 61 };

		String[] fields = { "intrest_div", "other_income", "operating_expenses", "fig_bal_sheet", "fig_bal_sheet_bwp",
				"amt_statement_adj", "amt_statement_adj_bwp", "net_amt", "net_amt_bwp", "bal_sub", "bal_sub_bwp",
				"bal_sub_diaries", "bal_sub_diaries_bwp" };

		try {
			for (int i : rows) {
				for (String field : fields) {

					String getterName = "getR" + i + "_" + field;
					String setterName = "setR" + i + "_" + field;

					try {
						Method getter = FORMAT_III_Summary_Entity.class.getMethod(getterName);
						Method setter = FORMAT_III_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Field not applicable for this row → skip safely
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		FORMAT_III_summary_repo.save(existing);
	}

	public ModelAndView getFORMAT_IIIcurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {

		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();

		// Session hs = sessionFactory.getCurrentSession();

		try {
			Date parsedDate = null;

			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

			String reportLabel = null;
			String reportAddlCriteria1 = null;
			// ? Split filter string into rowId & columnId
			if (filter != null && filter.contains(",")) {
				String[] parts = filter.split(",");
				if (parts.length >= 2) {
					reportLabel = parts[0];
					reportAddlCriteria1 = parts[1];
				}
			}

			System.out.println(type);
			if ("ARCHIVAL".equals(type) && version != null) {
				System.out.println(type);
				// ?? Archival branch
				List<FORMAT_III_Archival_Detail_Entity> T1Dt1;
				if (reportLabel != null && reportAddlCriteria1 != null) {
					T1Dt1 = FORMAT_III_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1,
							parsedDate, version);
				} else {
					T1Dt1 = FORMAT_III_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// ?? Current branch
				List<FORMAT_III_Detail_Entity> T1Dt1;

				if (reportLabel != null && reportAddlCriteria1 != null) {
					T1Dt1 = FORMAT_III_detail_repo.GetDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1, parsedDate);
				} else {
					T1Dt1 = FORMAT_III_detail_repo.getdatabydateList(parsedDate);
					totalPages = FORMAT_III_detail_repo.getdatacount(parsedDate);
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

		mv.setViewName("BRRS/FORMAT_III");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}

	public byte[] getFORMAT_IIIExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type,  BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		System.out.println(type);
		System.out.println(version);
		Date reportDate = dateformat.parse(todate);

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelFORMAT_IIIARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);

		}

		List<FORMAT_III_Summary_Entity> dataList = FORMAT_III_summary_repo.getdatabydateList(dateformat.parse(todate));
//		List<FORMAT_III_Manual_Summary_Entity> dataList1 = FORMAT_III_Manual_Summary_Repo
//				.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for brrs2.4 report. Returning empty result.");
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

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			// --- End of Style Definitions ---

			// --- End of Style Definitions ---
			int startRow = 12;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					FORMAT_III_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R13Cell1 = row.createCell(4);
					if (record.getR13_eff_increase() != null) {
						R13Cell1.setCellValue(record.getR13_eff_increase().doubleValue());
						R13Cell1.setCellStyle(numberStyle);
					} else {
						R13Cell1.setCellValue("");
						R13Cell1.setCellStyle(textStyle);
					}

					// R13 Col E
					Cell R13Cell2 = row.createCell(5);
					if (record.getR13_eff_decrease() != null) {
						R13Cell2.setCellValue(record.getR13_eff_decrease().doubleValue());
						R13Cell2.setCellStyle(numberStyle);
					} else {
						R13Cell2.setCellValue("");
						R13Cell2.setCellStyle(textStyle);
					}

					// R13 Col F
					Cell R13Cell3 = row.createCell(7);
					if (record.getR13_bal_increase() != null) {
						R13Cell3.setCellValue(record.getR13_bal_increase().doubleValue());
						R13Cell3.setCellStyle(numberStyle);
					} else {
						R13Cell3.setCellValue("");
						R13Cell3.setCellStyle(textStyle);
					}
					// R13 Col G
					Cell R13Cell4 = row.createCell(8);
					if (record.getR13_bal_decrease() != null) {
						R13Cell4.setCellValue(record.getR13_bal_decrease().doubleValue());
						R13Cell4.setCellStyle(numberStyle);
					} else {
						R13Cell4.setCellValue("");
						R13Cell4.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(13);
				Cell R14Cell1 = row.createCell(4);
					if (record.getR14_eff_increase() != null) {
						R14Cell1.setCellValue(record.getR14_eff_increase().doubleValue());
						R14Cell1.setCellStyle(numberStyle);
					} else {
						R14Cell1.setCellValue("");
						R14Cell1.setCellStyle(textStyle);
					}

					// R14 Col E
					Cell R14Cell2 = row.createCell(5);
					if (record.getR14_eff_decrease() != null) {
						R14Cell2.setCellValue(record.getR14_eff_decrease().doubleValue());
						R14Cell2.setCellStyle(numberStyle);
					} else {
						R14Cell2.setCellValue("");
						R14Cell2.setCellStyle(textStyle);
					}

					// R14 Col F
					Cell R14Cell3 = row.createCell(7);
					if (record.getR14_bal_increase() != null) {
						R14Cell3.setCellValue(record.getR14_bal_increase().doubleValue());
						R14Cell3.setCellStyle(numberStyle);
					} else {
						R14Cell3.setCellValue("");
						R14Cell3.setCellStyle(textStyle);
					}
					// R14 Col G
					Cell R14Cell4 = row.createCell(8);
					if (record.getR14_bal_decrease() != null) {
						R14Cell4.setCellValue(record.getR14_bal_decrease().doubleValue());
						R14Cell4.setCellStyle(numberStyle);
					} else {
						R14Cell4.setCellValue("");
						R14Cell4.setCellStyle(textStyle);
					}
				
				}
				workbook.setForceFormulaRecalculation(true);
			} else {

			}
			// Write the final workbook content to the in-memory stream.
			workbook.write(out);
			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			return out.toByteArray();
		}
	}

	public byte[] getExcelFORMAT_IIIARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<FORMAT_III_Archival_Summary_Entity> dataList = FORMAT_III_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for FORMAT_III report. Returning empty result.");
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

			int startRow = 8;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					FORMAT_III_Archival_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R13Cell1 = row.createCell(4);
					if (record.getR13_eff_increase() != null) {
						R13Cell1.setCellValue(record.getR13_eff_increase().doubleValue());
						R13Cell1.setCellStyle(numberStyle);
					} else {
						R13Cell1.setCellValue("");
						R13Cell1.setCellStyle(textStyle);
					}

					// R13 Col E
					Cell R13Cell2 = row.createCell(5);
					if (record.getR13_eff_decrease() != null) {
						R13Cell2.setCellValue(record.getR13_eff_decrease().doubleValue());
						R13Cell2.setCellStyle(numberStyle);
					} else {
						R13Cell2.setCellValue("");
						R13Cell2.setCellStyle(textStyle);
					}

					// R13 Col F
					Cell R13Cell3 = row.createCell(7);
					if (record.getR13_bal_increase() != null) {
						R13Cell3.setCellValue(record.getR13_bal_increase().doubleValue());
						R13Cell3.setCellStyle(numberStyle);
					} else {
						R13Cell3.setCellValue("");
						R13Cell3.setCellStyle(textStyle);
					}
					// R13 Col G
					Cell R13Cell4 = row.createCell(8);
					if (record.getR13_bal_decrease() != null) {
						R13Cell4.setCellValue(record.getR13_bal_decrease().doubleValue());
						R13Cell4.setCellStyle(numberStyle);
					} else {
						R13Cell4.setCellValue("");
						R13Cell4.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(13);
				Cell R14Cell1 = row.createCell(4);
					if (record.getR14_eff_increase() != null) {
						R14Cell1.setCellValue(record.getR14_eff_increase().doubleValue());
						R14Cell1.setCellStyle(numberStyle);
					} else {
						R14Cell1.setCellValue("");
						R14Cell1.setCellStyle(textStyle);
					}

					// R14 Col E
					Cell R14Cell2 = row.createCell(5);
					if (record.getR14_eff_decrease() != null) {
						R14Cell2.setCellValue(record.getR14_eff_decrease().doubleValue());
						R14Cell2.setCellStyle(numberStyle);
					} else {
						R14Cell2.setCellValue("");
						R14Cell2.setCellStyle(textStyle);
					}

					// R14 Col F
					Cell R14Cell3 = row.createCell(7);
					if (record.getR14_bal_increase() != null) {
						R14Cell3.setCellValue(record.getR14_bal_increase().doubleValue());
						R14Cell3.setCellStyle(numberStyle);
					} else {
						R14Cell3.setCellValue("");
						R14Cell3.setCellStyle(textStyle);
					}
					// R14 Col G
					Cell R14Cell4 = row.createCell(8);
					if (record.getR14_bal_decrease() != null) {
						R14Cell4.setCellValue(record.getR14_bal_decrease().doubleValue());
						R14Cell4.setCellStyle(numberStyle);
					} else {
						R14Cell4.setCellValue("");
						R14Cell4.setCellStyle(textStyle);
					}
				}
				workbook.setForceFormulaRecalculation(true);
			} else {

			}
			// Write the final workbook content to the in-memory stream.
			workbook.write(out);
			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			return out.toByteArray();
		}
	}

	public byte[] getFORMAT_IIIDetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for FORMAT_III Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getFORMAT_IIIDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype,
						type, version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("FORMAT_IIIDetails");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "AVERAGE", "REPORT LABLE",
					"REPORT ADDL CRITERIA1", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3 || i == 4) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<FORMAT_III_Detail_Entity> reportData = FORMAT_III_detail_repo.getdatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (FORMAT_III_Detail_Entity item : reportData) {
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

					// AVERAGE (right aligned, 3 decimal places)
					balanceCell = row.createCell(4);
					if (item.getAverage() != null) {
						balanceCell.setCellValue(item.getAverage().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(5).setCellValue(item.getReportLabel());
					row.createCell(6).setCellValue(item.getReportAddlCriteria1());
					row.createCell(7)
							.setCellValue(item.getREPORT_DATE() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getREPORT_DATE())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						if (j != 3 && j != 4) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for FORMAT_III — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating FORMAT_III Excel", e);
			return new byte[0];
		}
	}

	public byte[] getFORMAT_IIIDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for FORMAT_III ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("FORMAT_IIIDetail");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "AVERAGE", "REPORT LABLE",
					"REPORT ADDL CRITERIA1", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3 || i == 4) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<FORMAT_III_Archival_Detail_Entity> reportData = FORMAT_III_Archival_Detail_Repo
					.getdatabydateList(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (FORMAT_III_Archival_Detail_Entity item : reportData) {
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

					// AVERAGE (right aligned, 3 decimal places)
					 balanceCell = row.createCell(4);
					if (item.getAverage() != null) {
						balanceCell.setCellValue(item.getAverage().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(5).setCellValue(item.getReportLabel());
					row.createCell(6).setCellValue(item.getReportAddlCriteria1());
					row.createCell(7)
							.setCellValue(item.getREPORT_DATE() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getREPORT_DATE())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						if (j != 3 && j != 4) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for FORMAT_III — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating  FORMAT_III Excel", e);
			return new byte[0];
		}
	}

	@Autowired
	BRRS_FORMAT_III_Detail_Repo brrs_FORMAT_III_detail_repo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/FORMAT_III"); // ✅ match the report name

		if (acctNo != null) {
			FORMAT_III_Detail_Entity FORMAT_IIIEntity = brrs_FORMAT_III_detail_repo.findByAcctnumber(acctNo);
			if (FORMAT_IIIEntity != null && FORMAT_IIIEntity.getREPORT_DATE() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(FORMAT_IIIEntity.getREPORT_DATE());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("FORMAT_IIIData", FORMAT_IIIEntity);
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
			String average = request.getParameter("average");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			FORMAT_III_Detail_Entity existing = brrs_FORMAT_III_detail_repo.findByAcctnumber(acctNo);
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

			if (acctBalanceInpula != null && !acctBalanceInpula.isEmpty()) {
				BigDecimal newacctBalanceInpula = new BigDecimal(acctBalanceInpula);
				if (existing.getAcctBalanceInpula() == null
						|| existing.getAcctBalanceInpula().compareTo(newacctBalanceInpula) != 0) {
					existing.setAcctBalanceInpula(newacctBalanceInpula);
					isChanged = true;
					logger.info("Balance updated to {}", newacctBalanceInpula);
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
				brrs_FORMAT_III_detail_repo.save(existing);
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_FORMAT_III_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_FORMAT_III_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating FORMAT_III record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	public List<Object[]> getFORMAT_IIIResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<FORMAT_III_Archival_Summary_Entity> latestArchivalList = FORMAT_III_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (FORMAT_III_Archival_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getREPORT_RESUB_DATE() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching FORMAT_III Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	// Archival View
	public List<Object[]> getFORMAT_IIIArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<FORMAT_III_Archival_Summary_Entity> repoData = FORMAT_III_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (FORMAT_III_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getREPORT_RESUB_DATE() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				FORMAT_III_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching FORMAT_III Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

}