package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
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
import java.util.function.Function;

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
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_M_TOP_100_BORROWER_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_TOP_100_BORROWER_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_TOP_100_BORROWER_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_TOP_100_BORROWER_Manual_Archival_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_TOP_100_BORROWER_Manual_Archival_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_M_TOP_100_BORROWER_Manual_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_TOP_100_BORROWER_Manual_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_M_TOP_100_BORROWER_Summary_Repo;
import com.bornfire.brrs.entities.M_CA2_Manual_Summary_Entity;
import com.bornfire.brrs.entities.M_LCR_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_TOP_100_BORROWER_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_TOP_100_BORROWER_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_TOP_100_BORROWER_Detail_Entity;
import com.bornfire.brrs.entities.M_TOP_100_BORROWER_Manual_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.M_TOP_100_BORROWER_Manual_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.M_TOP_100_BORROWER_Manual_Summary_Entity1;
import com.bornfire.brrs.entities.M_TOP_100_BORROWER_Manual_Summary_Entity2;
import com.bornfire.brrs.entities.M_TOP_100_BORROWER_Summary_Entity;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Summary_Entity1;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;

@Component
@Service

public class BRRS_M_TOP_100_BORROWER_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_TOP_100_BORROWER_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_TOP_100_BORROWER_Detail_Repo BRRS_M_TOP_100_BORROWER_Detail_Repo;

	@Autowired
	BRRS_M_TOP_100_BORROWER_Summary_Repo BRRS_M_TOP_100_BORROWER_Summary_Repo;

	@Autowired
	BRRS_M_TOP_100_BORROWER_Archival_Detail_Repo BRRS_M_TOP_100_BORROWER_Archival_Detail_Repo;

	@Autowired
	BRRS_M_TOP_100_BORROWER_Archival_Summary_Repo BRRS_M_TOP_100_BORROWER_Archival_Summary_Repo;

	@Autowired
	BRRS_M_TOP_100_BORROWER_Manual_Archival_Summary_Repo1 BRRS_M_TOP_100_BORROWER_Manual_Archival_Summary_Repo1;

	@Autowired
	BRRS_M_TOP_100_BORROWER_Manual_Archival_Summary_Repo2 BRRS_M_TOP_100_BORROWER_Manual_Archival_Summary_Repo2;

	@Autowired
	BRRS_M_TOP_100_BORROWER_Manual_Summary_Repo1 BRRS_M_TOP_100_BORROWER_Manual_Summary_Repo1;

	@Autowired
	BRRS_M_TOP_100_BORROWER_Manual_Summary_Repo2 BRRS_M_TOP_100_BORROWER_Manual_Summary_Repo2;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_TOP_100_BORROWERView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version) {
		ModelAndView mv = new ModelAndView();
		/*
		 * Session hs = sessionFactory.getCurrentSession(); int pageSize =
		 * pageable.getPageSize(); int currentPage = pageable.getPageNumber(); int
		 * startItem = currentPage * pageSize;
		 */

		System.out.println("testing");
		System.out.println(version);

		if (type.equals("ARCHIVAL") & version != null) {
			System.out.println(type);
			List<M_TOP_100_BORROWER_Archival_Summary_Entity> T1Master = new ArrayList<M_TOP_100_BORROWER_Archival_Summary_Entity>();
			List<M_TOP_100_BORROWER_Manual_Archival_Summary_Entity1> T2Master = new ArrayList<M_TOP_100_BORROWER_Manual_Archival_Summary_Entity1>();
			List<M_TOP_100_BORROWER_Manual_Archival_Summary_Entity2> T3Master = new ArrayList<M_TOP_100_BORROWER_Manual_Archival_Summary_Entity2>();
			System.out.println(version);
			try {
				// Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_TOP_100_BORROWER_Archival_Summary_Repo
						.getdatabydateListarchival(dateformat.parse(todate), version);
				T2Master = BRRS_M_TOP_100_BORROWER_Manual_Archival_Summary_Repo1
						.getdatabydateListarchival(dateformat.parse(todate), version);
				T3Master = BRRS_M_TOP_100_BORROWER_Manual_Archival_Summary_Repo2
						.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);
			mv.addObject("reportsummary2", T3Master);
		} else {

			List<M_TOP_100_BORROWER_Summary_Entity> T1Master = new ArrayList<M_TOP_100_BORROWER_Summary_Entity>();
			List<M_TOP_100_BORROWER_Manual_Summary_Entity1> T2Master = new ArrayList<M_TOP_100_BORROWER_Manual_Summary_Entity1>();
			List<M_TOP_100_BORROWER_Manual_Summary_Entity2> T3Master = new ArrayList<M_TOP_100_BORROWER_Manual_Summary_Entity2>();

			try {
				// Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_TOP_100_BORROWER_Summary_Repo.getdatabydateList(dateformat.parse(todate));
				T2Master = BRRS_M_TOP_100_BORROWER_Manual_Summary_Repo1.getdatabydateList(dateformat.parse(todate));
				T3Master = BRRS_M_TOP_100_BORROWER_Manual_Summary_Repo2.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);
			mv.addObject("reportsummary2", T3Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

		mv.setViewName("BRRS/M_TOP_100_BORROWER");

		// mv.addObject("reportsummary", T1Master);
		// mv.addObject("reportmaster", T1Master);
		mv.addObject("displaymode", "summary");
		// mv.addObject("reportsflag", "reportsflag");
		// mv.addObject("menu", reportId);
		System.out.println("scv" + mv.getViewName());

		return mv;
	}

	public ModelAndView getM_TOP_100_BORROWERcurrentDtl(String reportId, String fromdate, String todate,
			String currency, String dtltype, Pageable pageable, String Filter, String type, String version) {

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
				System.out.println(type);
				// 🔹 Archival branch
				List<M_TOP_100_BORROWER_Archival_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BRRS_M_TOP_100_BORROWER_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId,
							parsedDate, version);
				} else {
					T1Dt1 = BRRS_M_TOP_100_BORROWER_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<M_TOP_100_BORROWER_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BRRS_M_TOP_100_BORROWER_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
				} else {
					T1Dt1 = BRRS_M_TOP_100_BORROWER_Detail_Repo.getdatabydateList(parsedDate, currentPage, pageSize);
					totalPages = BRRS_M_TOP_100_BORROWER_Detail_Repo.getdatacount(parsedDate);
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
		mv.setViewName("BRRS/M_TOP_100_BORROWER");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}




	public byte[] getM_TOP_100_BORROWERExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_TOP_100_BORROWERARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
					version);
		}

		// Fetch data
		logger.info("report date: {}", todate);
		List<M_TOP_100_BORROWER_Summary_Entity> dataList = BRRS_M_TOP_100_BORROWER_Summary_Repo
				.getdatabydateList(dateformat.parse(todate));
		List<M_TOP_100_BORROWER_Manual_Summary_Entity1> dataList1 = BRRS_M_TOP_100_BORROWER_Manual_Summary_Repo1
				.getdatabydateList(dateformat.parse(todate));
		List<M_TOP_100_BORROWER_Manual_Summary_Entity2> dataList2 = BRRS_M_TOP_100_BORROWER_Manual_Summary_Repo2
				.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_TOP_100_BORROWER report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		Path templatePath = Paths.get(templateDir, filename);
		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

			// --- Style Definitions (Optional, usually template handles this, but creating
			// logic to avoid nulls) ---
			CreationHelper createHelper = workbook.getCreationHelper();
			// ... (Your existing style definitions can stay here if needed for new rows)
			// ...

			// Start processing data
			if (!dataList.isEmpty()) {
				// Assuming 1:1 mapping between the lists.
				// Note: The original code implies one massive entity holding all 100 rows.
				// We assume dataList.size() is usually 1 for a specific report date.

				for (int i = 0; i < dataList.size(); i++) {
					M_TOP_100_BORROWER_Summary_Entity record = dataList.get(i);
					M_TOP_100_BORROWER_Manual_Summary_Entity1 record1 = (dataList1.size() > i) ? dataList1.get(i)
							: new M_TOP_100_BORROWER_Manual_Summary_Entity1();
					M_TOP_100_BORROWER_Manual_Summary_Entity2 record2 = (dataList2.size() > i) ? dataList2.get(i)
							: new M_TOP_100_BORROWER_Manual_Summary_Entity2();

					// The loop runs from R03 to R102
					// R03 corresponds to Row index 2 (in 0-based index) based on your original code
					// logic:
					// "int startRow = 2;" then "row = sheet.createRow(startRow + i)"
					// However, in the huge block, you hardcoded sheet.getRow(3) for R04, etc.
					// We will map R3 -> row 2, R4 -> row 3, etc.

					int excelStartRowIndex = 2; // Corresponds to R03
					int startR = 3;
					int endR = 102;

					for (int r = startR; r <= endR; r++) {
						int currentRowIndex = excelStartRowIndex + (r - startR);

						// Call helper method to write one specific borrower row
						writeSingleBorrowerRow(sheet, currentRowIndex, r, record, record1, record2);
					}
				}
			}

			workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			workbook.write(out);
			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			return out.toByteArray();
		}
	}

	/**
	 * Helper method to write a single row (e.g., R03, R50) using Reflection. This
	 * replaces the 100 blocks of repetitive code.
	 */
	private void writeSingleBorrowerRow(Sheet sheet, int rowIndex, int rNumber,
			M_TOP_100_BORROWER_Summary_Entity summaryEntity, M_TOP_100_BORROWER_Manual_Summary_Entity1 manualEntity1,
			M_TOP_100_BORROWER_Manual_Summary_Entity2 manualEntity2) {

		Row row = sheet.getRow(rowIndex);
		if (row == null) {
			row = sheet.createRow(rowIndex);
		}

		// Construct the prefix, e.g., "R03", "R10", "R100"
		String prefix = "R" + (rNumber < 10 ? "0" + rNumber : rNumber);

		// Determine which manual entity to use.
		// Entity1 has R03 to R70. Entity2 has R71 to R102.
		Object currentManualEntity = (rNumber <= 70) ? manualEntity1 : manualEntity2;

		try {
			// --- Summary Entity Fields ---
			// Col B (1): PAN_NUM
			setCellValue(row, 1, getReflectedValue(summaryEntity, prefix + "_PAN_NUM"));
			// Col C (2): NAME_OF_BORROWER
			setCellValue(row, 2, getReflectedValue(summaryEntity, prefix + "_NAME_OF_BORROWER"));

			// --- Manual Entity Fields ---
			// Col D (3): GROUP_CODE
			setCellValue(row, 3, getReflectedValue(currentManualEntity, prefix + "_GROUP_CODE"));
			// Col E (4): GROUP_NAME
			setCellValue(row, 4, getReflectedValue(currentManualEntity, prefix + "_GROUP_NAME"));

			// --- Summary Entity Fields (Financials) ---
			// Col F (5): FBLIMIT
			setCellValue(row, 5, getReflectedValue(summaryEntity, prefix + "_FBLIMIT"));
			// Col G (6): FBOS
			setCellValue(row, 6, getReflectedValue(summaryEntity, prefix + "_FBOS"));

			// --- Manual Entity Fields ---
			// Col H (7): CRM
			setCellValue(row, 7, getReflectedValue(currentManualEntity, prefix + "_CRM"));

			// --- Summary Entity Fields ---
			// Col I (8): FB_EXPOSURE
			setCellValue(row, 8, getReflectedValue(summaryEntity, prefix + "_FB_EXPOSURE"));

			// --- Manual Entity Fields ---
			// Col J (9): NFBLT
			setCellValue(row, 9, getReflectedValue(currentManualEntity, prefix + "_NFBLT"));
			// Col K (10): NFBOS
			setCellValue(row, 10, getReflectedValue(currentManualEntity, prefix + "_NFBOS"));
			// Col L (11): CRM_2 (Note: check if your entity uses CRM_2 or just CRM for the
			// second one. Snippet said CRM_2)
			setCellValue(row, 11, getReflectedValue(currentManualEntity, prefix + "_CRM_2"));
			// Col M (12): NFB
			setCellValue(row, 12, getReflectedValue(currentManualEntity, prefix + "_NFB"));
			// Col N (13): BOND
			setCellValue(row, 13, getReflectedValue(currentManualEntity, prefix + "_BOND"));
			// Col O (14): CP
			setCellValue(row, 14, getReflectedValue(currentManualEntity, prefix + "_CP"));
			// Col P (15): EQUITY (Code said EQULITY - preserving typo from your entity)
			setCellValue(row, 15, getReflectedValue(currentManualEntity, prefix + "_EQULITY"));
			// Col Q (16): FOREX
			setCellValue(row, 16, getReflectedValue(currentManualEntity, prefix + "_FOREX"));
			// Col R (17): OTHERS
			setCellValue(row, 17, getReflectedValue(currentManualEntity, prefix + "_OTHERS"));
			// Col S (18): INT_BANK
			setCellValue(row, 18, getReflectedValue(currentManualEntity, prefix + "_INT_BANK"));
			// Col T (19): DERIVATIVE
			setCellValue(row, 19, getReflectedValue(currentManualEntity, prefix + "_DERIVATIVE"));

			// Note: Skipping Column U (20) based on your original code logic

			// --- Summary Entity Fields (Ratings) ---
			// Col V (21): IRAC
			setCellValue(row, 21, getReflectedValue(summaryEntity, prefix + "_IRAC"));
			// Col W (22): CR_RATING
			setCellValue(row, 22, getReflectedValue(summaryEntity, prefix + "_CR_RATING"));

		} catch (Exception e) {
			System.err.println("Error processing row " + prefix + ": " + e.getMessage());
			// Optionally log error but continue processing
		}
	}

	/**
	 * Reflection helper to get value from entity by field name. Expects standard
	 * getter naming: getFIELD_NAME()
	 */
	private Object getReflectedValue(Object entity, String fieldName) {
		if (entity == null)
			return null;
		try {
			// Construct getter name: "R03_PAN_NUM" -> "getR03_PAN_NUM"
			String getterName = "get" + fieldName;
			Method method = entity.getClass().getMethod(getterName);
			return method.invoke(entity);
		} catch (NoSuchMethodException e) {
			// Field might not exist (e.g. if naming convention varies slightly)
			// System.out.println("Method not found: get" + fieldName);
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Helper to safely set cell value based on type.
	 */
	private void setCellValue(Row row, int colIndex, Object value) {
		Cell cell = row.getCell(colIndex);
		if (cell == null) {
			cell = row.createCell(colIndex);
		}

		if (value == null) {
			cell.setCellValue("");
		} else if (value instanceof BigDecimal) {
			cell.setCellValue(((BigDecimal) value).doubleValue());
		} else if (value instanceof Double) {
			cell.setCellValue((Double) value);
		} else if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else {
			cell.setCellValue(value.toString());
		}
	}

	public byte[] getM_TOP_100_BORROWERDetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for M_TOP_100_BORROWER Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_TOP_100_BORROWERDetail");

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

//ACCT BALANCE style (right aligned with thousand separator)
			CellStyle balanceStyle = workbook.createCellStyle();
			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "ROWID", "COLUMNID",
					"REPORT_DATE" };

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
			List<M_TOP_100_BORROWER_Detail_Entity> reportData = BRRS_M_TOP_100_BORROWER_Detail_Repo
					.getdatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_TOP_100_BORROWER_Detail_Entity item : reportData) {
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

					row.createCell(4).setCellValue(item.getReportLable());
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
				logger.info("No data found for M_TOP_100_BORROWER — only header will be written.");
			}

// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_TOP_100_BORROWER Excel", e);
			return new byte[0];
		}
	}

	public List<Object> getM_TOP_100_BORROWERArchival() {
		List<Object> M_TOP_100_BORROWERArchivallist = new ArrayList<>();
		try {
			M_TOP_100_BORROWERArchivallist = BRRS_M_TOP_100_BORROWER_Archival_Summary_Repo
					.getM_TOP_100_BORROWERarchival();
			M_TOP_100_BORROWERArchivallist = BRRS_M_TOP_100_BORROWER_Manual_Archival_Summary_Repo1
					.getM_TOP_100_BORROWERarchival();
			M_TOP_100_BORROWERArchivallist = BRRS_M_TOP_100_BORROWER_Manual_Archival_Summary_Repo2
					.getM_TOP_100_BORROWERarchival();
			System.out.println("countser" + M_TOP_100_BORROWERArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_TOP_100_BORROWER Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_TOP_100_BORROWERArchivallist;
	}

	public byte[] getExcelM_TOP_100_BORROWERARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {
			//
					}

// Fetch data
		logger.info("report date: {}", todate);
		
		List<M_TOP_100_BORROWER_Archival_Summary_Entity> dataList = BRRS_M_TOP_100_BORROWER_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		List<M_TOP_100_BORROWER_Manual_Archival_Summary_Entity1> dataList1 = BRRS_M_TOP_100_BORROWER_Manual_Archival_Summary_Repo1
				.getdatabydateListarchival(dateformat.parse(todate),version);
		List<M_TOP_100_BORROWER_Manual_Archival_Summary_Entity2> dataList2 = BRRS_M_TOP_100_BORROWER_Manual_Archival_Summary_Repo2
				.getdatabydateListarchival(dateformat.parse(todate),version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_TOP_100_BORROWER report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		Path templatePath = Paths.get(templateDir, filename);
		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

// --- Style Definitions (Optional, usually template handles this, but creating logic to avoid nulls) ---
			CreationHelper createHelper = workbook.getCreationHelper();
// ... (Your existing style definitions can stay here if needed for new rows) ...

// Start processing data
			if (!dataList.isEmpty()) {
// Assuming 1:1 mapping between the lists.
// Note: The original code implies one massive entity holding all 100 rows.
// We assume dataList.size() is usually 1 for a specific report date.

				for (int i = 0; i < dataList.size(); i++) {
					M_TOP_100_BORROWER_Archival_Summary_Entity record = dataList.get(i);
					M_TOP_100_BORROWER_Manual_Archival_Summary_Entity1 record1 = (dataList1.size() > i) ? dataList1.get(i)
							: new M_TOP_100_BORROWER_Manual_Archival_Summary_Entity1();
					M_TOP_100_BORROWER_Manual_Archival_Summary_Entity2 record2 = (dataList2.size() > i) ? dataList2.get(i)
							: new M_TOP_100_BORROWER_Manual_Archival_Summary_Entity2();

// The loop runs from R03 to R102
// R03 corresponds to Row index 2 (in 0-based index) based on your original code logic:
// "int startRow = 2;" then "row = sheet.createRow(startRow + i)" 
// However, in the huge block, you hardcoded sheet.getRow(3) for R04, etc.
// We will map R3 -> row 2, R4 -> row 3, etc.

					int excelStartRowIndex = 2; // Corresponds to R03
					int startR = 3;
					int endR = 102;

					for (int r = startR; r <= endR; r++) {
						int currentRowIndex = excelStartRowIndex + (r - startR);

// Call helper method to write one specific borrower row
						writeSingleBorrowerRow1(sheet, currentRowIndex, r, record, record1, record2);
					}
				}
			}

			workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			workbook.write(out);
			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			return out.toByteArray();
		}
	}

	/**
	 * Helper method to write a single row (e.g., R03, R50) using Reflection. This
	 * replaces the 100 blocks of repetitive code.
	 */
	private void writeSingleBorrowerRow1(Sheet sheet, int rowIndex, int rNumber,
			M_TOP_100_BORROWER_Archival_Summary_Entity summaryEntity, M_TOP_100_BORROWER_Manual_Archival_Summary_Entity1 manualEntity1,
			M_TOP_100_BORROWER_Manual_Archival_Summary_Entity2 manualEntity2) {

		Row row = sheet.getRow(rowIndex);
		if (row == null) {
			row = sheet.createRow(rowIndex);
		}

// Construct the prefix, e.g., "R03", "R10", "R100"
		String prefix = "R" + (rNumber < 10 ? "0" + rNumber : rNumber);

// Determine which manual entity to use.
// Entity1 has R03 to R70. Entity2 has R71 to R102.
		Object currentManualEntity = (rNumber <= 70) ? manualEntity1 : manualEntity2;

		try {
// --- Summary Entity Fields ---
// Col B (1): PAN_NUM
			setCellValue(row, 1, getReflectedValue(summaryEntity, prefix + "_PAN_NUM"));
// Col C (2): NAME_OF_BORROWER
			setCellValue(row, 2, getReflectedValue(summaryEntity, prefix + "_NAME_OF_BORROWER"));

// --- Manual Entity Fields ---
// Col D (3): GROUP_CODE
			setCellValue(row, 3, getReflectedValue(currentManualEntity, prefix + "_GROUP_CODE"));
// Col E (4): GROUP_NAME
			setCellValue(row, 4, getReflectedValue(currentManualEntity, prefix + "_GROUP_NAME"));

// --- Summary Entity Fields (Financials) ---
// Col F (5): FBLIMIT
			setCellValue(row, 5, getReflectedValue(summaryEntity, prefix + "_FBLIMIT"));
// Col G (6): FBOS
			setCellValue(row, 6, getReflectedValue(summaryEntity, prefix + "_FBOS"));

// --- Manual Entity Fields ---
// Col H (7): CRM
			setCellValue(row, 7, getReflectedValue(currentManualEntity, prefix + "_CRM"));

// --- Summary Entity Fields ---
// Col I (8): FB_EXPOSURE
			setCellValue(row, 8, getReflectedValue(summaryEntity, prefix + "_FB_EXPOSURE"));

// --- Manual Entity Fields ---
// Col J (9): NFBLT
			setCellValue(row, 9, getReflectedValue(currentManualEntity, prefix + "_NFBLT"));
// Col K (10): NFBOS
			setCellValue(row, 10, getReflectedValue(currentManualEntity, prefix + "_NFBOS"));
// Col L (11): CRM_2 (Note: check if your entity uses CRM_2 or just CRM for the second one. Snippet said CRM_2)
			setCellValue(row, 11, getReflectedValue(currentManualEntity, prefix + "_CRM_2"));
// Col M (12): NFB
			setCellValue(row, 12, getReflectedValue(currentManualEntity, prefix + "_NFB"));
// Col N (13): BOND
			setCellValue(row, 13, getReflectedValue(currentManualEntity, prefix + "_BOND"));
// Col O (14): CP
			setCellValue(row, 14, getReflectedValue(currentManualEntity, prefix + "_CP"));
// Col P (15): EQUITY (Code said EQULITY - preserving typo from your entity)
			setCellValue(row, 15, getReflectedValue(currentManualEntity, prefix + "_EQULITY"));
// Col Q (16): FOREX
			setCellValue(row, 16, getReflectedValue(currentManualEntity, prefix + "_FOREX"));
// Col R (17): OTHERS
			setCellValue(row, 17, getReflectedValue(currentManualEntity, prefix + "_OTHERS"));
// Col S (18): INT_BANK
			setCellValue(row, 18, getReflectedValue(currentManualEntity, prefix + "_INT_BANK"));
// Col T (19): DERIVATIVE
			setCellValue(row, 19, getReflectedValue(currentManualEntity, prefix + "_DERIVATIVE"));

// Note: Skipping Column U (20) based on your original code logic

// --- Summary Entity Fields (Ratings) ---
// Col V (21): IRAC
			setCellValue(row, 21, getReflectedValue(summaryEntity, prefix + "_IRAC"));
// Col W (22): CR_RATING
			setCellValue(row, 22, getReflectedValue(summaryEntity, prefix + "_CR_RATING"));

		} catch (Exception e) {
			System.err.println("Error processing row " + prefix + ": " + e.getMessage());
// Optionally log error but continue processing
		}
	}

	/**
	 * Reflection helper to get value from entity by field name. Expects standard
	 * getter naming: getFIELD_NAME()
	 */
//	private Object getReflectedValue(Object entity, String fieldName) {
//		if (entity == null)
//			return null;
//		try {
//// Construct getter name: "R03_PAN_NUM" -> "getR03_PAN_NUM"
//			String getterName = "get" + fieldName;
//			Method method = entity.getClass().getMethod(getterName);
//			return method.invoke(entity);
//		} catch (NoSuchMethodException e) {
//// Field might not exist (e.g. if naming convention varies slightly)
//// System.out.println("Method not found: get" + fieldName);
//			return null;
//		} catch (Exception e) {
//			return null;
//		}
//	}

	/**
	 * Helper to safely set cell value based on type.
	 */
//	private void setCellValue(Row row, int colIndex, Object value) {
//		Cell cell = row.getCell(colIndex);
//		if (cell == null) {
//			cell = row.createCell(colIndex);
//		}
//
//		if (value == null) {
//			cell.setCellValue("");
//		} else if (value instanceof BigDecimal) {
//			cell.setCellValue(((BigDecimal) value).doubleValue());
//		} else if (value instanceof Double) {
//			cell.setCellValue((Double) value);
//		} else if (value instanceof Integer) {
//			cell.setCellValue((Integer) value);
//		} else {
//			cell.setCellValue(value.toString());
//		}
//	}


	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_M_TOP_100_BORROWER ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_TOP_100_BORROWERDetail");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "ROWID", "COLUMNID",
					"REPORT_DATE" };

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
			List<M_TOP_100_BORROWER_Archival_Detail_Entity> reportData = BRRS_M_TOP_100_BORROWER_Archival_Detail_Repo
					.getdatabydateList(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_TOP_100_BORROWER_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places with comma separator)
					Cell balanceCell = row.createCell(3);

					if (item.getAcctBalanceInPula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
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

					row.createCell(4).setCellValue(item.getReportLable());
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
				logger.info("No data found for M_TOP_100_BORROWER — only header will be written.");
			}
			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_TOP_100_BORROWER Excel", e);
			return new byte[0];
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_TOP_100_BORROWER"); // ✅ match the report name
		System.out.println("Hello");
		if (acctNo != null) {
			M_TOP_100_BORROWER_Detail_Entity la1Entity = BRRS_M_TOP_100_BORROWER_Detail_Repo.findByAcctnumber(acctNo);
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

	public ModelAndView updateDetailEdit(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_TOP_100_BORROWER"); // ✅ match the report name

		if (acctNo != null) {
			M_TOP_100_BORROWER_Detail_Entity la1Entity = BRRS_M_TOP_100_BORROWER_Detail_Repo.findByAcctnumber(acctNo);
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
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String provisionStr = request.getParameter("acctBalanceInPula");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			M_TOP_100_BORROWER_Detail_Entity existing = BRRS_M_TOP_100_BORROWER_Detail_Repo.findByAcctnumber(acctNo);
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

			if (isChanged) {
				BRRS_M_TOP_100_BORROWER_Detail_Repo.save(existing);
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_M_TOP_100_BORROWER_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_M_TOP_100_BORROWER_SUMMARY_PROCEDURE(?); END;",
									formattedDate);
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
			logger.error("Error updating M_TOP_100_BORROWER record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}
	
	public void updateReport(M_TOP_100_BORROWER_Manual_Summary_Entity1 updatedEntity) {
	    System.out.println("Came to services1");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    M_TOP_100_BORROWER_Manual_Summary_Entity1 existing = BRRS_M_TOP_100_BORROWER_Manual_Summary_Repo1.findById(updatedEntity.getReport_date())
	    		
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

	    try {
	        // 1️⃣ Loop from R11 to R15 and copy fields
	    	for (int i = 3; i <= 70; i++) {
	    	    String prefix = "R" + String.format("%02d", i) + "_";
	            
	            String[] fields = {  "GROUP_CODE", "GROUP_NAME", "CRM",
	                                "NFBLT","NFBOS","CRM_2","NFB","BOND","CP","EQULITY","FOREX","OTHERS","INT_BANK","DERIVATIVE"};

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_TOP_100_BORROWER_Manual_Summary_Entity1.class.getMethod(getterName);
	                    Method setter = M_TOP_100_BORROWER_Manual_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);
	                    

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }
	    

	        // ✅ Save after all updates
	        BRRS_M_TOP_100_BORROWER_Manual_Summary_Repo1.save(existing);
	        System.out.println("✅ M_TOP_100_BORROWER Summary updated and COMMITTED");

		    // NOW PROCEDURE CAN SEE UPDATED DATA
		    String oracleDate = new SimpleDateFormat("dd-MM-yyyy")
		            .format(updatedEntity.getReport_date())
		            .toUpperCase();

		    String sql = "BEGIN BRRS.BRRS_M_TOP_100_BORROWER_SUMMARY_PROCEDURE('" + oracleDate + "'); END;";
		    jdbcTemplate.execute(sql);

		    System.out.println("Procedure executed for date: " + oracleDate);

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }
	}
	
	public void updateReport1(M_TOP_100_BORROWER_Manual_Summary_Entity2 updatedEntity) {
	    System.out.println("Came to services1");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    M_TOP_100_BORROWER_Manual_Summary_Entity2 existing = BRRS_M_TOP_100_BORROWER_Manual_Summary_Repo2.findById(updatedEntity.getReport_date())
	    		
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

	    try {
	        // 1️⃣ Loop from R11 to R15 and copy fields
	        for (int i = 71; i <= 102; i++) {
	            String prefix = "R" + i + "_";
	            
	            String[] fields = {  "GROUP_CODE", "GROUP_NAME", "CRM",
	                                "NFBLT","NFBOS","CRM_2","NFB","BOND","CP","EQULITY","FOREX","OTHERS","INT_BANK","DERIVATIVE"};

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_TOP_100_BORROWER_Manual_Summary_Entity2.class.getMethod(getterName);
	                    Method setter = M_TOP_100_BORROWER_Manual_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);
	                    

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }
	        
	        // ✅ Save after all updates
	        BRRS_M_TOP_100_BORROWER_Manual_Summary_Repo2.save(existing);

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }
	}

}
