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

import com.bornfire.brrs.entities.BRRS_CASH_FLOW_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_CASH_FLOW_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_CASH_FLOW_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_CASH_FLOW_Summary_Repo;
import com.bornfire.brrs.entities.CASH_FLOW_Archival_Detail_Entity;
import com.bornfire.brrs.entities.CASH_FLOW_Archival_Summary_Entity;
import com.bornfire.brrs.entities.CASH_FLOW_Detail_Entity;
import com.bornfire.brrs.entities.CASH_FLOW_Summary_Entity;

@Component
@Service

public class BRRS_CASH_FLOW_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_CASH_FLOW_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_CASH_FLOW_Summary_Repo CASH_FLOW_summary_repo;

	@Autowired
	BRRS_CASH_FLOW_Archival_Summary_Repo CASH_FLOW_Archival_Summary_Repo;

	@Autowired
	BRRS_CASH_FLOW_Detail_Repo CASH_FLOW_detail_repo;

	@Autowired
	BRRS_CASH_FLOW_Archival_Detail_Repo CASH_FLOW_Archival_Detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getCASH_FLOWView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		try {
			Date d1 = dateformat.parse(todate);

			// ---------- CASE 1: ARCHIVAL ----------
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				List<CASH_FLOW_Archival_Summary_Entity> T1Master = CASH_FLOW_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);
				mv.addObject("reportsummary", T1Master);
				System.out.println("T1Master Size " + T1Master.size());

			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<CASH_FLOW_Summary_Entity> T1Master = CASH_FLOW_summary_repo
						.getdatabydateList(dateformat.parse(todate));

				mv.addObject("reportsummary", T1Master);
				System.out.println("T1Master Size " + T1Master.size());
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/CASH_FLOW");
		mv.addObject("displaymode", "summary");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	public void updateReport(CASH_FLOW_Summary_Entity updatedEntity) {

		CASH_FLOW_Summary_Entity existing = CASH_FLOW_summary_repo.findById(updatedEntity.getReportDate()).orElseThrow(
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
						Method getter = CASH_FLOW_Summary_Entity.class.getMethod(getterName);
						Method setter = CASH_FLOW_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

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

		CASH_FLOW_summary_repo.save(existing);
	}

	public ModelAndView getCASH_FLOWcurrentDtl(String reportId, String fromdate, String todate, String currency,
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
				List<CASH_FLOW_Archival_Detail_Entity> T1Dt1;
				if (reportLabel != null && reportAddlCriteria1 != null) {
					T1Dt1 = CASH_FLOW_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1,
							parsedDate, version);
				} else {
					T1Dt1 = CASH_FLOW_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// ?? Current branch
				List<CASH_FLOW_Detail_Entity> T1Dt1;

				if (reportLabel != null && reportAddlCriteria1 != null) {
					T1Dt1 = CASH_FLOW_detail_repo.GetDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1,
							parsedDate);
				} else {
					T1Dt1 = CASH_FLOW_detail_repo.getdatabydateList(parsedDate);
					totalPages = CASH_FLOW_detail_repo.getdatacount(parsedDate);
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

		mv.setViewName("BRRS/CASH_FLOW");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}

	public byte[] getCASH_FLOWExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		System.out.println(type);
		System.out.println(version);
		Date reportDate = dateformat.parse(todate);

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelCASH_FLOWARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);

		}

		List<CASH_FLOW_Summary_Entity> dataList = CASH_FLOW_summary_repo.getdatabydateList(dateformat.parse(todate));
//		List<CASH_FLOW_Manual_Summary_Entity> dataList1 = CASH_FLOW_Manual_Summary_Repo
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
			int startRow = 8;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					CASH_FLOW_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// R9 Col C
					Cell R9Cell1 = row.createCell(2);
					if (record.getR9_lc_as_on_mar() != null) {
						R9Cell1.setCellValue(record.getR9_lc_as_on_mar().doubleValue());
						R9Cell1.setCellStyle(numberStyle);
					} else {
						R9Cell1.setCellValue("");
						R9Cell1.setCellStyle(textStyle);
					}

					// R9 Col D
					Cell R9Cell2 = row.createCell(3);
					if (record.getR9_lc_as_on_sep() != null) {
						R9Cell2.setCellValue(record.getR9_lc_as_on_sep().doubleValue());
						R9Cell2.setCellStyle(numberStyle);
					} else {
						R9Cell2.setCellValue("");
						R9Cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(9);
					Cell R10Cell1 = row.createCell(2);
					if (record.getR10_lc_as_on_mar() != null) {
						R10Cell1.setCellValue(record.getR10_lc_as_on_mar().doubleValue());
						R10Cell1.setCellStyle(numberStyle);
					} else {
						R10Cell1.setCellValue("");
						R10Cell1.setCellStyle(textStyle);
					}

					// R10 Col E
					Cell R10Cell2 = row.createCell(3);
					if (record.getR10_lc_as_on_sep() != null) {
						R10Cell2.setCellValue(record.getR10_lc_as_on_sep().doubleValue());
						R10Cell2.setCellStyle(numberStyle);
					} else {
						R10Cell2.setCellValue("");
						R10Cell2.setCellStyle(textStyle);
					}
					// R11
					row = sheet.getRow(10);
					Cell R11Cell1 = row.createCell(2);
					if (record.getR11_lc_as_on_mar() != null) {
						R11Cell1.setCellValue(record.getR11_lc_as_on_mar().doubleValue());
						R11Cell1.setCellStyle(numberStyle);
					} else {
						R11Cell1.setCellValue("");
						R11Cell1.setCellStyle(textStyle);
					}

					Cell R11Cell2 = row.createCell(3);
					if (record.getR11_lc_as_on_sep() != null) {
						R11Cell2.setCellValue(record.getR11_lc_as_on_sep().doubleValue());
						R11Cell2.setCellStyle(numberStyle);
					} else {
						R11Cell2.setCellValue("");
						R11Cell2.setCellStyle(textStyle);
					}

// R12
					row = sheet.getRow(11);
					Cell R12Cell1 = row.createCell(2);
					if (record.getR12_lc_as_on_mar() != null) {
						R12Cell1.setCellValue(record.getR12_lc_as_on_mar().doubleValue());
						R12Cell1.setCellStyle(numberStyle);
					} else {
						R12Cell1.setCellValue("");
						R12Cell1.setCellStyle(textStyle);
					}

					Cell R12Cell2 = row.createCell(3);
					if (record.getR12_lc_as_on_sep() != null) {
						R12Cell2.setCellValue(record.getR12_lc_as_on_sep().doubleValue());
						R12Cell2.setCellStyle(numberStyle);
					} else {
						R12Cell2.setCellValue("");
						R12Cell2.setCellStyle(textStyle);
					}

// R13
					row = sheet.getRow(12);
					Cell R13Cell1 = row.createCell(2);
					if (record.getR13_lc_as_on_mar() != null) {
						R13Cell1.setCellValue(record.getR13_lc_as_on_mar().doubleValue());
						R13Cell1.setCellStyle(numberStyle);
					} else {
						R13Cell1.setCellValue("");
						R13Cell1.setCellStyle(textStyle);
					}

					Cell R13Cell2 = row.createCell(3);
					if (record.getR13_lc_as_on_sep() != null) {
						R13Cell2.setCellValue(record.getR13_lc_as_on_sep().doubleValue());
						R13Cell2.setCellStyle(numberStyle);
					} else {
						R13Cell2.setCellValue("");
						R13Cell2.setCellStyle(textStyle);
					}

// R14
					row = sheet.getRow(13);
					Cell R14Cell1 = row.createCell(2);
					if (record.getR14_lc_as_on_mar() != null) {
						R14Cell1.setCellValue(record.getR14_lc_as_on_mar().doubleValue());
						R14Cell1.setCellStyle(numberStyle);
					} else {
						R14Cell1.setCellValue("");
						R14Cell1.setCellStyle(textStyle);
					}

					Cell R14Cell2 = row.createCell(3);
					if (record.getR14_lc_as_on_sep() != null) {
						R14Cell2.setCellValue(record.getR14_lc_as_on_sep().doubleValue());
						R14Cell2.setCellStyle(numberStyle);
					} else {
						R14Cell2.setCellValue("");
						R14Cell2.setCellStyle(textStyle);
					}

// R15
					row = sheet.getRow(14);
					Cell R15Cell1 = row.createCell(2);
					if (record.getR15_lc_as_on_mar() != null) {
						R15Cell1.setCellValue(record.getR15_lc_as_on_mar().doubleValue());
						R15Cell1.setCellStyle(numberStyle);
					} else {
						R15Cell1.setCellValue("");
						R15Cell1.setCellStyle(textStyle);
					}

					Cell R15Cell2 = row.createCell(3);
					if (record.getR15_lc_as_on_sep() != null) {
						R15Cell2.setCellValue(record.getR15_lc_as_on_sep().doubleValue());
						R15Cell2.setCellStyle(numberStyle);
					} else {
						R15Cell2.setCellValue("");
						R15Cell2.setCellStyle(textStyle);
					}

// R16
					row = sheet.getRow(15);
					Cell R16Cell1 = row.createCell(2);
					if (record.getR16_lc_as_on_mar() != null) {
						R16Cell1.setCellValue(record.getR16_lc_as_on_mar().doubleValue());
						R16Cell1.setCellStyle(numberStyle);
					} else {
						R16Cell1.setCellValue("");
						R16Cell1.setCellStyle(textStyle);
					}

					Cell R16Cell2 = row.createCell(3);
					if (record.getR16_lc_as_on_sep() != null) {
						R16Cell2.setCellValue(record.getR16_lc_as_on_sep().doubleValue());
						R16Cell2.setCellStyle(numberStyle);
					} else {
						R16Cell2.setCellValue("");
						R16Cell2.setCellStyle(textStyle);
					}

// R17
					row = sheet.getRow(16);
					Cell R17Cell1 = row.createCell(2);
					if (record.getR17_lc_as_on_mar() != null) {
						R17Cell1.setCellValue(record.getR17_lc_as_on_mar().doubleValue());
						R17Cell1.setCellStyle(numberStyle);
					} else {
						R17Cell1.setCellValue("");
						R17Cell1.setCellStyle(textStyle);
					}

					Cell R17Cell2 = row.createCell(3);
					if (record.getR17_lc_as_on_sep() != null) {
						R17Cell2.setCellValue(record.getR17_lc_as_on_sep().doubleValue());
						R17Cell2.setCellStyle(numberStyle);
					} else {
						R17Cell2.setCellValue("");
						R17Cell2.setCellStyle(textStyle);
					}

// R18
					row = sheet.getRow(17);
					Cell R18Cell1 = row.createCell(2);
					if (record.getR18_lc_as_on_mar() != null) {
						R18Cell1.setCellValue(record.getR18_lc_as_on_mar().doubleValue());
						R18Cell1.setCellStyle(numberStyle);
					} else {
						R18Cell1.setCellValue("");
						R18Cell1.setCellStyle(textStyle);
					}

					Cell R18Cell2 = row.createCell(3);
					if (record.getR18_lc_as_on_sep() != null) {
						R18Cell2.setCellValue(record.getR18_lc_as_on_sep().doubleValue());
						R18Cell2.setCellStyle(numberStyle);
					} else {
						R18Cell2.setCellValue("");
						R18Cell2.setCellStyle(textStyle);
					}

// R19
					row = sheet.getRow(18);
					Cell R19Cell1 = row.createCell(2);
					if (record.getR19_lc_as_on_mar() != null) {
						R19Cell1.setCellValue(record.getR19_lc_as_on_mar().doubleValue());
						R19Cell1.setCellStyle(numberStyle);
					} else {
						R19Cell1.setCellValue("");
						R19Cell1.setCellStyle(textStyle);
					}

					Cell R19Cell2 = row.createCell(3);
					if (record.getR19_lc_as_on_sep() != null) {
						R19Cell2.setCellValue(record.getR19_lc_as_on_sep().doubleValue());
						R19Cell2.setCellStyle(numberStyle);
					} else {
						R19Cell2.setCellValue("");
						R19Cell2.setCellStyle(textStyle);
					}

// R20
					row = sheet.getRow(19);
					Cell R20Cell1 = row.createCell(2);
					if (record.getR20_lc_as_on_mar() != null) {
						R20Cell1.setCellValue(record.getR20_lc_as_on_mar().doubleValue());
						R20Cell1.setCellStyle(numberStyle);
					} else {
						R20Cell1.setCellValue("");
						R20Cell1.setCellStyle(textStyle);
					}

					Cell R20Cell2 = row.createCell(3);
					if (record.getR20_lc_as_on_sep() != null) {
						R20Cell2.setCellValue(record.getR20_lc_as_on_sep().doubleValue());
						R20Cell2.setCellStyle(numberStyle);
					} else {
						R20Cell2.setCellValue("");
						R20Cell2.setCellStyle(textStyle);
					}

// R21
					row = sheet.getRow(20);
					Cell R21Cell1 = row.createCell(2);
					if (record.getR21_lc_as_on_mar() != null) {
						R21Cell1.setCellValue(record.getR21_lc_as_on_mar().doubleValue());
						R21Cell1.setCellStyle(numberStyle);
					} else {
						R21Cell1.setCellValue("");
						R21Cell1.setCellStyle(textStyle);
					}

					Cell R21Cell2 = row.createCell(3);
					if (record.getR21_lc_as_on_sep() != null) {
						R21Cell2.setCellValue(record.getR21_lc_as_on_sep().doubleValue());
						R21Cell2.setCellStyle(numberStyle);
					} else {
						R21Cell2.setCellValue("");
						R21Cell2.setCellStyle(textStyle);
					}

// R22
					row = sheet.getRow(21);
					Cell R22Cell1 = row.createCell(2);
					if (record.getR22_lc_as_on_mar() != null) {
						R22Cell1.setCellValue(record.getR22_lc_as_on_mar().doubleValue());
						R22Cell1.setCellStyle(numberStyle);
					} else {
						R22Cell1.setCellValue("");
						R22Cell1.setCellStyle(textStyle);
					}

					Cell R22Cell2 = row.createCell(3);
					if (record.getR22_lc_as_on_sep() != null) {
						R22Cell2.setCellValue(record.getR22_lc_as_on_sep().doubleValue());
						R22Cell2.setCellStyle(numberStyle);
					} else {
						R22Cell2.setCellValue("");
						R22Cell2.setCellStyle(textStyle);
					}
// R23
//row = sheet.getRow(22);
//Cell R23Cell1 = row.createCell(2);
//if (record.getR23_lc_as_on_mar() != null) {
//    R23Cell1.setCellValue(record.getR23_lc_as_on_mar().doubleValue());
//    R23Cell1.setCellStyle(numberStyle);
//} else {
//    R23Cell1.setCellValue("");
//    R23Cell1.setCellStyle(textStyle);
//}
//
//Cell R23Cell2 = row.createCell(3);
//if (record.getR23_lc_as_on_sep() != null) {
//    R23Cell2.setCellValue(record.getR23_lc_as_on_sep().doubleValue());
//    R23Cell2.setCellStyle(numberStyle);
//} else {
//    R23Cell2.setCellValue("");
//    R23Cell2.setCellStyle(textStyle);
//}

// R24
					row = sheet.getRow(23);
					Cell R24Cell1 = row.createCell(2);
					if (record.getR24_lc_as_on_mar() != null) {
						R24Cell1.setCellValue(record.getR24_lc_as_on_mar().doubleValue());
						R24Cell1.setCellStyle(numberStyle);
					} else {
						R24Cell1.setCellValue("");
						R24Cell1.setCellStyle(textStyle);
					}

					Cell R24Cell2 = row.createCell(3);
					if (record.getR24_lc_as_on_sep() != null) {
						R24Cell2.setCellValue(record.getR24_lc_as_on_sep().doubleValue());
						R24Cell2.setCellStyle(numberStyle);
					} else {
						R24Cell2.setCellValue("");
						R24Cell2.setCellStyle(textStyle);
					}

// R25
					row = sheet.getRow(24);
					Cell R25Cell1 = row.createCell(2);
					if (record.getR25_lc_as_on_mar() != null) {
						R25Cell1.setCellValue(record.getR25_lc_as_on_mar().doubleValue());
						R25Cell1.setCellStyle(numberStyle);
					} else {
						R25Cell1.setCellValue("");
						R25Cell1.setCellStyle(textStyle);
					}

					Cell R25Cell2 = row.createCell(3);
					if (record.getR25_lc_as_on_sep() != null) {
						R25Cell2.setCellValue(record.getR25_lc_as_on_sep().doubleValue());
						R25Cell2.setCellStyle(numberStyle);
					} else {
						R25Cell2.setCellValue("");
						R25Cell2.setCellStyle(textStyle);
					}

// R26
					row = sheet.getRow(25);
					Cell R26Cell1 = row.createCell(2);
					if (record.getR26_lc_as_on_mar() != null) {
						R26Cell1.setCellValue(record.getR26_lc_as_on_mar().doubleValue());
						R26Cell1.setCellStyle(numberStyle);
					} else {
						R26Cell1.setCellValue("");
						R26Cell1.setCellStyle(textStyle);
					}

					Cell R26Cell2 = row.createCell(3);
					if (record.getR26_lc_as_on_sep() != null) {
						R26Cell2.setCellValue(record.getR26_lc_as_on_sep().doubleValue());
						R26Cell2.setCellStyle(numberStyle);
					} else {
						R26Cell2.setCellValue("");
						R26Cell2.setCellStyle(textStyle);
					}

// R27
					row = sheet.getRow(26);
					Cell R27Cell1 = row.createCell(2);
					if (record.getR27_lc_as_on_mar() != null) {
						R27Cell1.setCellValue(record.getR27_lc_as_on_mar().doubleValue());
						R27Cell1.setCellStyle(numberStyle);
					} else {
						R27Cell1.setCellValue("");
						R27Cell1.setCellStyle(textStyle);
					}

					Cell R27Cell2 = row.createCell(3);
					if (record.getR27_lc_as_on_sep() != null) {
						R27Cell2.setCellValue(record.getR27_lc_as_on_sep().doubleValue());
						R27Cell2.setCellStyle(numberStyle);
					} else {
						R27Cell2.setCellValue("");
						R27Cell2.setCellStyle(textStyle);
					}

// R28
					row = sheet.getRow(27);
					Cell R28Cell1 = row.createCell(2);
					if (record.getR28_lc_as_on_mar() != null) {
						R28Cell1.setCellValue(record.getR28_lc_as_on_mar().doubleValue());
						R28Cell1.setCellStyle(numberStyle);
					} else {
						R28Cell1.setCellValue("");
						R28Cell1.setCellStyle(textStyle);
					}

					Cell R28Cell2 = row.createCell(3);
					if (record.getR28_lc_as_on_sep() != null) {
						R28Cell2.setCellValue(record.getR28_lc_as_on_sep().doubleValue());
						R28Cell2.setCellStyle(numberStyle);
					} else {
						R28Cell2.setCellValue("");
						R28Cell2.setCellStyle(textStyle);
					}

// R29
					row = sheet.getRow(28);
					Cell R29Cell1 = row.createCell(2);
					if (record.getR29_lc_as_on_mar() != null) {
						R29Cell1.setCellValue(record.getR29_lc_as_on_mar().doubleValue());
						R29Cell1.setCellStyle(numberStyle);
					} else {
						R29Cell1.setCellValue("");
						R29Cell1.setCellStyle(textStyle);
					}

					Cell R29Cell2 = row.createCell(3);
					if (record.getR29_lc_as_on_sep() != null) {
						R29Cell2.setCellValue(record.getR29_lc_as_on_sep().doubleValue());
						R29Cell2.setCellStyle(numberStyle);
					} else {
						R29Cell2.setCellValue("");
						R29Cell2.setCellStyle(textStyle);
					}

// R30
					row = sheet.getRow(29);
					Cell R30Cell1 = row.createCell(2);
					if (record.getR30_lc_as_on_mar() != null) {
						R30Cell1.setCellValue(record.getR30_lc_as_on_mar().doubleValue());
						R30Cell1.setCellStyle(numberStyle);
					} else {
						R30Cell1.setCellValue("");
						R30Cell1.setCellStyle(textStyle);
					}

					Cell R30Cell2 = row.createCell(3);
					if (record.getR30_lc_as_on_sep() != null) {
						R30Cell2.setCellValue(record.getR30_lc_as_on_sep().doubleValue());
						R30Cell2.setCellStyle(numberStyle);
					} else {
						R30Cell2.setCellValue("");
						R30Cell2.setCellStyle(textStyle);
					}

// R31
					row = sheet.getRow(30);
					Cell R31Cell1 = row.createCell(2);
					if (record.getR31_lc_as_on_mar() != null) {
						R31Cell1.setCellValue(record.getR31_lc_as_on_mar().doubleValue());
						R31Cell1.setCellStyle(numberStyle);
					} else {
						R31Cell1.setCellValue("");
						R31Cell1.setCellStyle(textStyle);
					}

					Cell R31Cell2 = row.createCell(3);
					if (record.getR31_lc_as_on_sep() != null) {
						R31Cell2.setCellValue(record.getR31_lc_as_on_sep().doubleValue());
						R31Cell2.setCellStyle(numberStyle);
					} else {
						R31Cell2.setCellValue("");
						R31Cell2.setCellStyle(textStyle);
					}

// R32
					row = sheet.getRow(31);
					Cell R32Cell1 = row.createCell(2);
					if (record.getR32_lc_as_on_mar() != null) {
						R32Cell1.setCellValue(record.getR32_lc_as_on_mar().doubleValue());
						R32Cell1.setCellStyle(numberStyle);
					} else {
						R32Cell1.setCellValue("");
						R32Cell1.setCellStyle(textStyle);
					}

					Cell R32Cell2 = row.createCell(3);
					if (record.getR32_lc_as_on_sep() != null) {
						R32Cell2.setCellValue(record.getR32_lc_as_on_sep().doubleValue());
						R32Cell2.setCellStyle(numberStyle);
					} else {
						R32Cell2.setCellValue("");
						R32Cell2.setCellStyle(textStyle);
					}

// R33
//row = sheet.getRow(32);
//Cell R33Cell1 = row.createCell(2);
//if (record.getR33_lc_as_on_mar() != null) {
//    R33Cell1.setCellValue(record.getR33_lc_as_on_mar().doubleValue());
//    R33Cell1.setCellStyle(numberStyle);
//} else {
//    R33Cell1.setCellValue("");
//    R33Cell1.setCellStyle(textStyle);
//}
//
//Cell R33Cell2 = row.createCell(3);
//if (record.getR33_lc_as_on_sep() != null) {
//    R33Cell2.setCellValue(record.getR33_lc_as_on_sep().doubleValue());
//    R33Cell2.setCellStyle(numberStyle);
//} else {
//    R33Cell2.setCellValue("");
//    R33Cell2.setCellStyle(textStyle);
//}

// R34
					row = sheet.getRow(33);
					Cell R34Cell1 = row.createCell(2);
					if (record.getR34_lc_as_on_mar() != null) {
						R34Cell1.setCellValue(record.getR34_lc_as_on_mar().doubleValue());
						R34Cell1.setCellStyle(numberStyle);
					} else {
						R34Cell1.setCellValue("");
						R34Cell1.setCellStyle(textStyle);
					}

					Cell R34Cell2 = row.createCell(3);
					if (record.getR34_lc_as_on_sep() != null) {
						R34Cell2.setCellValue(record.getR34_lc_as_on_sep().doubleValue());
						R34Cell2.setCellStyle(numberStyle);
					} else {
						R34Cell2.setCellValue("");
						R34Cell2.setCellStyle(textStyle);
					}

// R35
					row = sheet.getRow(34);
					Cell R35Cell1 = row.createCell(2);
					if (record.getR35_lc_as_on_mar() != null) {
						R35Cell1.setCellValue(record.getR35_lc_as_on_mar().doubleValue());
						R35Cell1.setCellStyle(numberStyle);
					} else {
						R35Cell1.setCellValue("");
						R35Cell1.setCellStyle(textStyle);
					}

					Cell R35Cell2 = row.createCell(3);
					if (record.getR35_lc_as_on_sep() != null) {
						R35Cell2.setCellValue(record.getR35_lc_as_on_sep().doubleValue());
						R35Cell2.setCellStyle(numberStyle);
					} else {
						R35Cell2.setCellValue("");
						R35Cell2.setCellStyle(textStyle);
					}

// R36
					row = sheet.getRow(35);
					Cell R36Cell1 = row.createCell(2);
					if (record.getR36_lc_as_on_mar() != null) {
						R36Cell1.setCellValue(record.getR36_lc_as_on_mar().doubleValue());
						R36Cell1.setCellStyle(numberStyle);
					} else {
						R36Cell1.setCellValue("");
						R36Cell1.setCellStyle(textStyle);
					}

					Cell R36Cell2 = row.createCell(3);
					if (record.getR36_lc_as_on_sep() != null) {
						R36Cell2.setCellValue(record.getR36_lc_as_on_sep().doubleValue());
						R36Cell2.setCellStyle(numberStyle);
					} else {
						R36Cell2.setCellValue("");
						R36Cell2.setCellStyle(textStyle);
					}

// R37
					row = sheet.getRow(36);
					Cell R37Cell1 = row.createCell(2);
					if (record.getR37_lc_as_on_mar() != null) {
						R37Cell1.setCellValue(record.getR37_lc_as_on_mar().doubleValue());
						R37Cell1.setCellStyle(numberStyle);
					} else {
						R37Cell1.setCellValue("");
						R37Cell1.setCellStyle(textStyle);
					}

					Cell R37Cell2 = row.createCell(3);
					if (record.getR37_lc_as_on_sep() != null) {
						R37Cell2.setCellValue(record.getR37_lc_as_on_sep().doubleValue());
						R37Cell2.setCellStyle(numberStyle);
					} else {
						R37Cell2.setCellValue("");
						R37Cell2.setCellStyle(textStyle);
					}

// R38
//row = sheet.getRow(37);
//Cell R38Cell1 = row.createCell(2);
//if (record.getR38_lc_as_on_mar() != null) {
//    R38Cell1.setCellValue(record.getR38_lc_as_on_mar().doubleValue());
//    R38Cell1.setCellStyle(numberStyle);
//} else {
//    R38Cell1.setCellValue("");
//    R38Cell1.setCellStyle(textStyle);
//}
//
//Cell R38Cell2 = row.createCell(3);
//if (record.getR38_lc_as_on_sep() != null) {
//    R38Cell2.setCellValue(record.getR38_lc_as_on_sep().doubleValue());
//    R38Cell2.setCellStyle(numberStyle);
//} else {
//    R38Cell2.setCellValue("");
//    R38Cell2.setCellStyle(textStyle);
//}

// R39
					row = sheet.getRow(38);
					Cell R39Cell1 = row.createCell(2);
					if (record.getR39_lc_as_on_mar() != null) {
						R39Cell1.setCellValue(record.getR39_lc_as_on_mar().doubleValue());
						R39Cell1.setCellStyle(numberStyle);
					} else {
						R39Cell1.setCellValue("");
						R39Cell1.setCellStyle(textStyle);
					}

					Cell R39Cell2 = row.createCell(3);
					if (record.getR39_lc_as_on_sep() != null) {
						R39Cell2.setCellValue(record.getR39_lc_as_on_sep().doubleValue());
						R39Cell2.setCellStyle(numberStyle);
					} else {
						R39Cell2.setCellValue("");
						R39Cell2.setCellStyle(textStyle);
					}

// R40
					row = sheet.getRow(39);
					Cell R40Cell1 = row.createCell(2);
					if (record.getR40_lc_as_on_mar() != null) {
						R40Cell1.setCellValue(record.getR40_lc_as_on_mar().doubleValue());
						R40Cell1.setCellStyle(numberStyle);
					} else {
						R40Cell1.setCellValue("");
						R40Cell1.setCellStyle(textStyle);
					}

					Cell R40Cell2 = row.createCell(3);
					if (record.getR40_lc_as_on_sep() != null) {
						R40Cell2.setCellValue(record.getR40_lc_as_on_sep().doubleValue());
						R40Cell2.setCellStyle(numberStyle);
					} else {
						R40Cell2.setCellValue("");
						R40Cell2.setCellStyle(textStyle);
					}

// R41
					row = sheet.getRow(40);
					Cell R41Cell1 = row.createCell(2);
					if (record.getR41_lc_as_on_mar() != null) {
						R41Cell1.setCellValue(record.getR41_lc_as_on_mar().doubleValue());
						R41Cell1.setCellStyle(numberStyle);
					} else {
						R41Cell1.setCellValue("");
						R41Cell1.setCellStyle(textStyle);
					}

					Cell R41Cell2 = row.createCell(3);
					if (record.getR41_lc_as_on_sep() != null) {
						R41Cell2.setCellValue(record.getR41_lc_as_on_sep().doubleValue());
						R41Cell2.setCellStyle(numberStyle);
					} else {
						R41Cell2.setCellValue("");
						R41Cell2.setCellStyle(textStyle);
					}

// R42
					row = sheet.getRow(41);
					Cell R42Cell1 = row.createCell(2);
					if (record.getR42_lc_as_on_mar() != null) {
						R42Cell1.setCellValue(record.getR42_lc_as_on_mar().doubleValue());
						R42Cell1.setCellStyle(numberStyle);
					} else {
						R42Cell1.setCellValue("");
						R42Cell1.setCellStyle(textStyle);
					}

					Cell R42Cell2 = row.createCell(3);
					if (record.getR42_lc_as_on_sep() != null) {
						R42Cell2.setCellValue(record.getR42_lc_as_on_sep().doubleValue());
						R42Cell2.setCellStyle(numberStyle);
					} else {
						R42Cell2.setCellValue("");
						R42Cell2.setCellStyle(textStyle);
					}

// R43
					row = sheet.getRow(42);
					Cell R43Cell1 = row.createCell(2);
					if (record.getR43_lc_as_on_mar() != null) {
						R43Cell1.setCellValue(record.getR43_lc_as_on_mar().doubleValue());
						R43Cell1.setCellStyle(numberStyle);
					} else {
						R43Cell1.setCellValue("");
						R43Cell1.setCellStyle(textStyle);
					}

					Cell R43Cell2 = row.createCell(3);
					if (record.getR43_lc_as_on_sep() != null) {
						R43Cell2.setCellValue(record.getR43_lc_as_on_sep().doubleValue());
						R43Cell2.setCellStyle(numberStyle);
					} else {
						R43Cell2.setCellValue("");
						R43Cell2.setCellStyle(textStyle);
					}
// R44
					row = sheet.getRow(43);
					Cell R44Cell1 = row.createCell(2);
					if (record.getR44_lc_as_on_mar() != null) {
						R44Cell1.setCellValue(record.getR44_lc_as_on_mar().doubleValue());
						R44Cell1.setCellStyle(numberStyle);
					} else {
						R44Cell1.setCellValue("");
						R44Cell1.setCellStyle(textStyle);
					}

					Cell R44Cell2 = row.createCell(3);
					if (record.getR44_lc_as_on_sep() != null) {
						R44Cell2.setCellValue(record.getR44_lc_as_on_sep().doubleValue());
						R44Cell2.setCellStyle(numberStyle);
					} else {
						R44Cell2.setCellValue("");
						R44Cell2.setCellStyle(textStyle);
					}

// R45
//row = sheet.getRow(44);
//Cell R45Cell1 = row.createCell(2);
//if (record.getR45_lc_as_on_mar() != null) {
//    R45Cell1.setCellValue(record.getR45_lc_as_on_mar().doubleValue());
//    R45Cell1.setCellStyle(numberStyle);
//} else {
//    R45Cell1.setCellValue("");
//    R45Cell1.setCellStyle(textStyle);
//}
//
//Cell R45Cell2 = row.createCell(3);
//if (record.getR45_lc_as_on_sep() != null) {
//    R45Cell2.setCellValue(record.getR45_lc_as_on_sep().doubleValue());
//    R45Cell2.setCellStyle(numberStyle);
//} else {
//    R45Cell2.setCellValue("");
//    R45Cell2.setCellStyle(textStyle);
//}

// R46
//row = sheet.getRow(45);
//Cell R46Cell1 = row.createCell(2);
//if (record.getR46_lc_as_on_mar() != null) {
//    R46Cell1.setCellValue(record.getR46_lc_as_on_mar().doubleValue());
//    R46Cell1.setCellStyle(numberStyle);
//} else {
//    R46Cell1.setCellValue("");
//    R46Cell1.setCellStyle(textStyle);
//}
//
//Cell R46Cell2 = row.createCell(3);
//if (record.getR46_lc_as_on_sep() != null) {
//    R46Cell2.setCellValue(record.getR46_lc_as_on_sep().doubleValue());
//    R46Cell2.setCellStyle(numberStyle);
//} else {
//    R46Cell2.setCellValue("");
//    R46Cell2.setCellStyle(textStyle);
//}

// R47
					row = sheet.getRow(46);
					Cell R47Cell1 = row.createCell(2);
					if (record.getR47_lc_as_on_mar() != null) {
						R47Cell1.setCellValue(record.getR47_lc_as_on_mar().doubleValue());
						R47Cell1.setCellStyle(numberStyle);
					} else {
						R47Cell1.setCellValue("");
						R47Cell1.setCellStyle(textStyle);
					}

					Cell R47Cell2 = row.createCell(3);
					if (record.getR47_lc_as_on_sep() != null) {
						R47Cell2.setCellValue(record.getR47_lc_as_on_sep().doubleValue());
						R47Cell2.setCellStyle(numberStyle);
					} else {
						R47Cell2.setCellValue("");
						R47Cell2.setCellStyle(textStyle);
					}

// R48
					row = sheet.getRow(47);
					Cell R48Cell1 = row.createCell(2);
					if (record.getR48_lc_as_on_mar() != null) {
						R48Cell1.setCellValue(record.getR48_lc_as_on_mar().doubleValue());
						R48Cell1.setCellStyle(numberStyle);
					} else {
						R48Cell1.setCellValue("");
						R48Cell1.setCellStyle(textStyle);
					}

//Cell R48Cell2 = row.createCell(3);
//if (record.getR48_lc_as_on_sep() != null) {
//    R48Cell2.setCellValue(record.getR48_lc_as_on_sep().doubleValue());
//    R48Cell2.setCellStyle(numberStyle);
//} else {
//    R48Cell2.setCellValue("");
//    R48Cell2.setCellStyle(textStyle);
//}

// R49
					row = sheet.getRow(48);
					Cell R49Cell1 = row.createCell(2);
					if (record.getR49_lc_as_on_mar() != null) {
						R49Cell1.setCellValue(record.getR49_lc_as_on_mar().doubleValue());
						R49Cell1.setCellStyle(numberStyle);
					} else {
						R49Cell1.setCellValue("");
						R49Cell1.setCellStyle(textStyle);
					}

//Cell R49Cell2 = row.createCell(3);
//if (record.getR49_lc_as_on_sep() != null) {
//    R49Cell2.setCellValue(record.getR49_lc_as_on_sep().doubleValue());
//    R49Cell2.setCellStyle(numberStyle);
//} else {
//    R49Cell2.setCellValue("");
//    R49Cell2.setCellStyle(textStyle);
//}

// R50
					row = sheet.getRow(49);
					Cell R50Cell1 = row.createCell(2);
					if (record.getR50_lc_as_on_mar() != null) {
						R50Cell1.setCellValue(record.getR50_lc_as_on_mar().doubleValue());
						R50Cell1.setCellStyle(numberStyle);
					} else {
						R50Cell1.setCellValue("");
						R50Cell1.setCellStyle(textStyle);
					}

					Cell R50Cell2 = row.createCell(3);
					if (record.getR50_lc_as_on_sep() != null) {
						R50Cell2.setCellValue(record.getR50_lc_as_on_sep().doubleValue());
						R50Cell2.setCellStyle(numberStyle);
					} else {
						R50Cell2.setCellValue("");
						R50Cell2.setCellStyle(textStyle);
					}

// R51
					row = sheet.getRow(50);
					Cell R51Cell1 = row.createCell(2);
					if (record.getR51_lc_as_on_mar() != null) {
						R51Cell1.setCellValue(record.getR51_lc_as_on_mar().doubleValue());
						R51Cell1.setCellStyle(numberStyle);
					} else {
						R51Cell1.setCellValue("");
						R51Cell1.setCellStyle(textStyle);
					}

//Cell R51Cell2 = row.createCell(3);
//if (record.getR51_lc_as_on_sep() != null) {
//    R51Cell2.setCellValue(record.getR51_lc_as_on_sep().doubleValue());
//    R51Cell2.setCellStyle(numberStyle);
//} else {
//    R51Cell2.setCellValue("");
//    R51Cell2.setCellStyle(textStyle);
//}

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

	public byte[] getExcelCASH_FLOWARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<CASH_FLOW_Archival_Summary_Entity> dataList = CASH_FLOW_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for CASH_FLOW report. Returning empty result.");
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
					CASH_FLOW_Archival_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R9 Col C
					Cell R9Cell1 = row.createCell(2);
					if (record.getR9_lc_as_on_mar() != null) {
						R9Cell1.setCellValue(record.getR9_lc_as_on_mar().doubleValue());
						R9Cell1.setCellStyle(numberStyle);
					} else {
						R9Cell1.setCellValue("");
						R9Cell1.setCellStyle(textStyle);
					}

					// R9 Col D
					Cell R9Cell2 = row.createCell(3);
					if (record.getR9_lc_as_on_sep() != null) {
						R9Cell2.setCellValue(record.getR9_lc_as_on_sep().doubleValue());
						R9Cell2.setCellStyle(numberStyle);
					} else {
						R9Cell2.setCellValue("");
						R9Cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(9);
					Cell R10Cell1 = row.createCell(2);
					if (record.getR10_lc_as_on_mar() != null) {
						R10Cell1.setCellValue(record.getR10_lc_as_on_mar().doubleValue());
						R10Cell1.setCellStyle(numberStyle);
					} else {
						R10Cell1.setCellValue("");
						R10Cell1.setCellStyle(textStyle);
					}

					// R10 Col E
					Cell R10Cell2 = row.createCell(3);
					if (record.getR10_lc_as_on_sep() != null) {
						R10Cell2.setCellValue(record.getR10_lc_as_on_sep().doubleValue());
						R10Cell2.setCellStyle(numberStyle);
					} else {
						R10Cell2.setCellValue("");
						R10Cell2.setCellStyle(textStyle);
					}
					// R11
					row = sheet.getRow(10);
					Cell R11Cell1 = row.createCell(2);
					if (record.getR11_lc_as_on_mar() != null) {
						R11Cell1.setCellValue(record.getR11_lc_as_on_mar().doubleValue());
						R11Cell1.setCellStyle(numberStyle);
					} else {
						R11Cell1.setCellValue("");
						R11Cell1.setCellStyle(textStyle);
					}

					Cell R11Cell2 = row.createCell(3);
					if (record.getR11_lc_as_on_sep() != null) {
						R11Cell2.setCellValue(record.getR11_lc_as_on_sep().doubleValue());
						R11Cell2.setCellStyle(numberStyle);
					} else {
						R11Cell2.setCellValue("");
						R11Cell2.setCellStyle(textStyle);
					}

// R12
					row = sheet.getRow(11);
					Cell R12Cell1 = row.createCell(2);
					if (record.getR12_lc_as_on_mar() != null) {
						R12Cell1.setCellValue(record.getR12_lc_as_on_mar().doubleValue());
						R12Cell1.setCellStyle(numberStyle);
					} else {
						R12Cell1.setCellValue("");
						R12Cell1.setCellStyle(textStyle);
					}

					Cell R12Cell2 = row.createCell(3);
					if (record.getR12_lc_as_on_sep() != null) {
						R12Cell2.setCellValue(record.getR12_lc_as_on_sep().doubleValue());
						R12Cell2.setCellStyle(numberStyle);
					} else {
						R12Cell2.setCellValue("");
						R12Cell2.setCellStyle(textStyle);
					}

// R13
					row = sheet.getRow(12);
					Cell R13Cell1 = row.createCell(2);
					if (record.getR13_lc_as_on_mar() != null) {
						R13Cell1.setCellValue(record.getR13_lc_as_on_mar().doubleValue());
						R13Cell1.setCellStyle(numberStyle);
					} else {
						R13Cell1.setCellValue("");
						R13Cell1.setCellStyle(textStyle);
					}

					Cell R13Cell2 = row.createCell(3);
					if (record.getR13_lc_as_on_sep() != null) {
						R13Cell2.setCellValue(record.getR13_lc_as_on_sep().doubleValue());
						R13Cell2.setCellStyle(numberStyle);
					} else {
						R13Cell2.setCellValue("");
						R13Cell2.setCellStyle(textStyle);
					}

// R14
					row = sheet.getRow(13);
					Cell R14Cell1 = row.createCell(2);
					if (record.getR14_lc_as_on_mar() != null) {
						R14Cell1.setCellValue(record.getR14_lc_as_on_mar().doubleValue());
						R14Cell1.setCellStyle(numberStyle);
					} else {
						R14Cell1.setCellValue("");
						R14Cell1.setCellStyle(textStyle);
					}

					Cell R14Cell2 = row.createCell(3);
					if (record.getR14_lc_as_on_sep() != null) {
						R14Cell2.setCellValue(record.getR14_lc_as_on_sep().doubleValue());
						R14Cell2.setCellStyle(numberStyle);
					} else {
						R14Cell2.setCellValue("");
						R14Cell2.setCellStyle(textStyle);
					}

// R15
					row = sheet.getRow(14);
					Cell R15Cell1 = row.createCell(2);
					if (record.getR15_lc_as_on_mar() != null) {
						R15Cell1.setCellValue(record.getR15_lc_as_on_mar().doubleValue());
						R15Cell1.setCellStyle(numberStyle);
					} else {
						R15Cell1.setCellValue("");
						R15Cell1.setCellStyle(textStyle);
					}

					Cell R15Cell2 = row.createCell(3);
					if (record.getR15_lc_as_on_sep() != null) {
						R15Cell2.setCellValue(record.getR15_lc_as_on_sep().doubleValue());
						R15Cell2.setCellStyle(numberStyle);
					} else {
						R15Cell2.setCellValue("");
						R15Cell2.setCellStyle(textStyle);
					}

// R16
					row = sheet.getRow(15);
					Cell R16Cell1 = row.createCell(2);
					if (record.getR16_lc_as_on_mar() != null) {
						R16Cell1.setCellValue(record.getR16_lc_as_on_mar().doubleValue());
						R16Cell1.setCellStyle(numberStyle);
					} else {
						R16Cell1.setCellValue("");
						R16Cell1.setCellStyle(textStyle);
					}

					Cell R16Cell2 = row.createCell(3);
					if (record.getR16_lc_as_on_sep() != null) {
						R16Cell2.setCellValue(record.getR16_lc_as_on_sep().doubleValue());
						R16Cell2.setCellStyle(numberStyle);
					} else {
						R16Cell2.setCellValue("");
						R16Cell2.setCellStyle(textStyle);
					}

// R17
					row = sheet.getRow(16);
					Cell R17Cell1 = row.createCell(2);
					if (record.getR17_lc_as_on_mar() != null) {
						R17Cell1.setCellValue(record.getR17_lc_as_on_mar().doubleValue());
						R17Cell1.setCellStyle(numberStyle);
					} else {
						R17Cell1.setCellValue("");
						R17Cell1.setCellStyle(textStyle);
					}

					Cell R17Cell2 = row.createCell(3);
					if (record.getR17_lc_as_on_sep() != null) {
						R17Cell2.setCellValue(record.getR17_lc_as_on_sep().doubleValue());
						R17Cell2.setCellStyle(numberStyle);
					} else {
						R17Cell2.setCellValue("");
						R17Cell2.setCellStyle(textStyle);
					}

// R18
					row = sheet.getRow(17);
					Cell R18Cell1 = row.createCell(2);
					if (record.getR18_lc_as_on_mar() != null) {
						R18Cell1.setCellValue(record.getR18_lc_as_on_mar().doubleValue());
						R18Cell1.setCellStyle(numberStyle);
					} else {
						R18Cell1.setCellValue("");
						R18Cell1.setCellStyle(textStyle);
					}

					Cell R18Cell2 = row.createCell(3);
					if (record.getR18_lc_as_on_sep() != null) {
						R18Cell2.setCellValue(record.getR18_lc_as_on_sep().doubleValue());
						R18Cell2.setCellStyle(numberStyle);
					} else {
						R18Cell2.setCellValue("");
						R18Cell2.setCellStyle(textStyle);
					}

// R19
					row = sheet.getRow(18);
					Cell R19Cell1 = row.createCell(2);
					if (record.getR19_lc_as_on_mar() != null) {
						R19Cell1.setCellValue(record.getR19_lc_as_on_mar().doubleValue());
						R19Cell1.setCellStyle(numberStyle);
					} else {
						R19Cell1.setCellValue("");
						R19Cell1.setCellStyle(textStyle);
					}

					Cell R19Cell2 = row.createCell(3);
					if (record.getR19_lc_as_on_sep() != null) {
						R19Cell2.setCellValue(record.getR19_lc_as_on_sep().doubleValue());
						R19Cell2.setCellStyle(numberStyle);
					} else {
						R19Cell2.setCellValue("");
						R19Cell2.setCellStyle(textStyle);
					}

// R20
					row = sheet.getRow(19);
					Cell R20Cell1 = row.createCell(2);
					if (record.getR20_lc_as_on_mar() != null) {
						R20Cell1.setCellValue(record.getR20_lc_as_on_mar().doubleValue());
						R20Cell1.setCellStyle(numberStyle);
					} else {
						R20Cell1.setCellValue("");
						R20Cell1.setCellStyle(textStyle);
					}

					Cell R20Cell2 = row.createCell(3);
					if (record.getR20_lc_as_on_sep() != null) {
						R20Cell2.setCellValue(record.getR20_lc_as_on_sep().doubleValue());
						R20Cell2.setCellStyle(numberStyle);
					} else {
						R20Cell2.setCellValue("");
						R20Cell2.setCellStyle(textStyle);
					}

// R21
					row = sheet.getRow(20);
					Cell R21Cell1 = row.createCell(2);
					if (record.getR21_lc_as_on_mar() != null) {
						R21Cell1.setCellValue(record.getR21_lc_as_on_mar().doubleValue());
						R21Cell1.setCellStyle(numberStyle);
					} else {
						R21Cell1.setCellValue("");
						R21Cell1.setCellStyle(textStyle);
					}

					Cell R21Cell2 = row.createCell(3);
					if (record.getR21_lc_as_on_sep() != null) {
						R21Cell2.setCellValue(record.getR21_lc_as_on_sep().doubleValue());
						R21Cell2.setCellStyle(numberStyle);
					} else {
						R21Cell2.setCellValue("");
						R21Cell2.setCellStyle(textStyle);
					}

// R22
					row = sheet.getRow(21);
					Cell R22Cell1 = row.createCell(2);
					if (record.getR22_lc_as_on_mar() != null) {
						R22Cell1.setCellValue(record.getR22_lc_as_on_mar().doubleValue());
						R22Cell1.setCellStyle(numberStyle);
					} else {
						R22Cell1.setCellValue("");
						R22Cell1.setCellStyle(textStyle);
					}

					Cell R22Cell2 = row.createCell(3);
					if (record.getR22_lc_as_on_sep() != null) {
						R22Cell2.setCellValue(record.getR22_lc_as_on_sep().doubleValue());
						R22Cell2.setCellStyle(numberStyle);
					} else {
						R22Cell2.setCellValue("");
						R22Cell2.setCellStyle(textStyle);
					}
// R23
//row = sheet.getRow(22);
//Cell R23Cell1 = row.createCell(2);
//if (record.getR23_lc_as_on_mar() != null) {
//    R23Cell1.setCellValue(record.getR23_lc_as_on_mar().doubleValue());
//    R23Cell1.setCellStyle(numberStyle);
//} else {
//    R23Cell1.setCellValue("");
//    R23Cell1.setCellStyle(textStyle);
//}
//
//Cell R23Cell2 = row.createCell(3);
//if (record.getR23_lc_as_on_sep() != null) {
//    R23Cell2.setCellValue(record.getR23_lc_as_on_sep().doubleValue());
//    R23Cell2.setCellStyle(numberStyle);
//} else {
//    R23Cell2.setCellValue("");
//    R23Cell2.setCellStyle(textStyle);
//}

// R24
					row = sheet.getRow(23);
					Cell R24Cell1 = row.createCell(2);
					if (record.getR24_lc_as_on_mar() != null) {
						R24Cell1.setCellValue(record.getR24_lc_as_on_mar().doubleValue());
						R24Cell1.setCellStyle(numberStyle);
					} else {
						R24Cell1.setCellValue("");
						R24Cell1.setCellStyle(textStyle);
					}

					Cell R24Cell2 = row.createCell(3);
					if (record.getR24_lc_as_on_sep() != null) {
						R24Cell2.setCellValue(record.getR24_lc_as_on_sep().doubleValue());
						R24Cell2.setCellStyle(numberStyle);
					} else {
						R24Cell2.setCellValue("");
						R24Cell2.setCellStyle(textStyle);
					}

// R25
					row = sheet.getRow(24);
					Cell R25Cell1 = row.createCell(2);
					if (record.getR25_lc_as_on_mar() != null) {
						R25Cell1.setCellValue(record.getR25_lc_as_on_mar().doubleValue());
						R25Cell1.setCellStyle(numberStyle);
					} else {
						R25Cell1.setCellValue("");
						R25Cell1.setCellStyle(textStyle);
					}

					Cell R25Cell2 = row.createCell(3);
					if (record.getR25_lc_as_on_sep() != null) {
						R25Cell2.setCellValue(record.getR25_lc_as_on_sep().doubleValue());
						R25Cell2.setCellStyle(numberStyle);
					} else {
						R25Cell2.setCellValue("");
						R25Cell2.setCellStyle(textStyle);
					}

// R26
					row = sheet.getRow(25);
					Cell R26Cell1 = row.createCell(2);
					if (record.getR26_lc_as_on_mar() != null) {
						R26Cell1.setCellValue(record.getR26_lc_as_on_mar().doubleValue());
						R26Cell1.setCellStyle(numberStyle);
					} else {
						R26Cell1.setCellValue("");
						R26Cell1.setCellStyle(textStyle);
					}

					Cell R26Cell2 = row.createCell(3);
					if (record.getR26_lc_as_on_sep() != null) {
						R26Cell2.setCellValue(record.getR26_lc_as_on_sep().doubleValue());
						R26Cell2.setCellStyle(numberStyle);
					} else {
						R26Cell2.setCellValue("");
						R26Cell2.setCellStyle(textStyle);
					}

// R27
					row = sheet.getRow(26);
					Cell R27Cell1 = row.createCell(2);
					if (record.getR27_lc_as_on_mar() != null) {
						R27Cell1.setCellValue(record.getR27_lc_as_on_mar().doubleValue());
						R27Cell1.setCellStyle(numberStyle);
					} else {
						R27Cell1.setCellValue("");
						R27Cell1.setCellStyle(textStyle);
					}

					Cell R27Cell2 = row.createCell(3);
					if (record.getR27_lc_as_on_sep() != null) {
						R27Cell2.setCellValue(record.getR27_lc_as_on_sep().doubleValue());
						R27Cell2.setCellStyle(numberStyle);
					} else {
						R27Cell2.setCellValue("");
						R27Cell2.setCellStyle(textStyle);
					}

// R28
					row = sheet.getRow(27);
					Cell R28Cell1 = row.createCell(2);
					if (record.getR28_lc_as_on_mar() != null) {
						R28Cell1.setCellValue(record.getR28_lc_as_on_mar().doubleValue());
						R28Cell1.setCellStyle(numberStyle);
					} else {
						R28Cell1.setCellValue("");
						R28Cell1.setCellStyle(textStyle);
					}

					Cell R28Cell2 = row.createCell(3);
					if (record.getR28_lc_as_on_sep() != null) {
						R28Cell2.setCellValue(record.getR28_lc_as_on_sep().doubleValue());
						R28Cell2.setCellStyle(numberStyle);
					} else {
						R28Cell2.setCellValue("");
						R28Cell2.setCellStyle(textStyle);
					}

// R29
					row = sheet.getRow(28);
					Cell R29Cell1 = row.createCell(2);
					if (record.getR29_lc_as_on_mar() != null) {
						R29Cell1.setCellValue(record.getR29_lc_as_on_mar().doubleValue());
						R29Cell1.setCellStyle(numberStyle);
					} else {
						R29Cell1.setCellValue("");
						R29Cell1.setCellStyle(textStyle);
					}

					Cell R29Cell2 = row.createCell(3);
					if (record.getR29_lc_as_on_sep() != null) {
						R29Cell2.setCellValue(record.getR29_lc_as_on_sep().doubleValue());
						R29Cell2.setCellStyle(numberStyle);
					} else {
						R29Cell2.setCellValue("");
						R29Cell2.setCellStyle(textStyle);
					}

// R30
					row = sheet.getRow(29);
					Cell R30Cell1 = row.createCell(2);
					if (record.getR30_lc_as_on_mar() != null) {
						R30Cell1.setCellValue(record.getR30_lc_as_on_mar().doubleValue());
						R30Cell1.setCellStyle(numberStyle);
					} else {
						R30Cell1.setCellValue("");
						R30Cell1.setCellStyle(textStyle);
					}

					Cell R30Cell2 = row.createCell(3);
					if (record.getR30_lc_as_on_sep() != null) {
						R30Cell2.setCellValue(record.getR30_lc_as_on_sep().doubleValue());
						R30Cell2.setCellStyle(numberStyle);
					} else {
						R30Cell2.setCellValue("");
						R30Cell2.setCellStyle(textStyle);
					}

// R31
					row = sheet.getRow(30);
					Cell R31Cell1 = row.createCell(2);
					if (record.getR31_lc_as_on_mar() != null) {
						R31Cell1.setCellValue(record.getR31_lc_as_on_mar().doubleValue());
						R31Cell1.setCellStyle(numberStyle);
					} else {
						R31Cell1.setCellValue("");
						R31Cell1.setCellStyle(textStyle);
					}

					Cell R31Cell2 = row.createCell(3);
					if (record.getR31_lc_as_on_sep() != null) {
						R31Cell2.setCellValue(record.getR31_lc_as_on_sep().doubleValue());
						R31Cell2.setCellStyle(numberStyle);
					} else {
						R31Cell2.setCellValue("");
						R31Cell2.setCellStyle(textStyle);
					}

// R32
					row = sheet.getRow(31);
					Cell R32Cell1 = row.createCell(2);
					if (record.getR32_lc_as_on_mar() != null) {
						R32Cell1.setCellValue(record.getR32_lc_as_on_mar().doubleValue());
						R32Cell1.setCellStyle(numberStyle);
					} else {
						R32Cell1.setCellValue("");
						R32Cell1.setCellStyle(textStyle);
					}

					Cell R32Cell2 = row.createCell(3);
					if (record.getR32_lc_as_on_sep() != null) {
						R32Cell2.setCellValue(record.getR32_lc_as_on_sep().doubleValue());
						R32Cell2.setCellStyle(numberStyle);
					} else {
						R32Cell2.setCellValue("");
						R32Cell2.setCellStyle(textStyle);
					}

// R33
//row = sheet.getRow(32);
//Cell R33Cell1 = row.createCell(2);
//if (record.getR33_lc_as_on_mar() != null) {
//    R33Cell1.setCellValue(record.getR33_lc_as_on_mar().doubleValue());
//    R33Cell1.setCellStyle(numberStyle);
//} else {
//    R33Cell1.setCellValue("");
//    R33Cell1.setCellStyle(textStyle);
//}
//
//Cell R33Cell2 = row.createCell(3);
//if (record.getR33_lc_as_on_sep() != null) {
//    R33Cell2.setCellValue(record.getR33_lc_as_on_sep().doubleValue());
//    R33Cell2.setCellStyle(numberStyle);
//} else {
//    R33Cell2.setCellValue("");
//    R33Cell2.setCellStyle(textStyle);
//}

// R34
					row = sheet.getRow(33);
					Cell R34Cell1 = row.createCell(2);
					if (record.getR34_lc_as_on_mar() != null) {
						R34Cell1.setCellValue(record.getR34_lc_as_on_mar().doubleValue());
						R34Cell1.setCellStyle(numberStyle);
					} else {
						R34Cell1.setCellValue("");
						R34Cell1.setCellStyle(textStyle);
					}

					Cell R34Cell2 = row.createCell(3);
					if (record.getR34_lc_as_on_sep() != null) {
						R34Cell2.setCellValue(record.getR34_lc_as_on_sep().doubleValue());
						R34Cell2.setCellStyle(numberStyle);
					} else {
						R34Cell2.setCellValue("");
						R34Cell2.setCellStyle(textStyle);
					}

// R35
					row = sheet.getRow(34);
					Cell R35Cell1 = row.createCell(2);
					if (record.getR35_lc_as_on_mar() != null) {
						R35Cell1.setCellValue(record.getR35_lc_as_on_mar().doubleValue());
						R35Cell1.setCellStyle(numberStyle);
					} else {
						R35Cell1.setCellValue("");
						R35Cell1.setCellStyle(textStyle);
					}

					Cell R35Cell2 = row.createCell(3);
					if (record.getR35_lc_as_on_sep() != null) {
						R35Cell2.setCellValue(record.getR35_lc_as_on_sep().doubleValue());
						R35Cell2.setCellStyle(numberStyle);
					} else {
						R35Cell2.setCellValue("");
						R35Cell2.setCellStyle(textStyle);
					}

// R36
					row = sheet.getRow(35);
					Cell R36Cell1 = row.createCell(2);
					if (record.getR36_lc_as_on_mar() != null) {
						R36Cell1.setCellValue(record.getR36_lc_as_on_mar().doubleValue());
						R36Cell1.setCellStyle(numberStyle);
					} else {
						R36Cell1.setCellValue("");
						R36Cell1.setCellStyle(textStyle);
					}

					Cell R36Cell2 = row.createCell(3);
					if (record.getR36_lc_as_on_sep() != null) {
						R36Cell2.setCellValue(record.getR36_lc_as_on_sep().doubleValue());
						R36Cell2.setCellStyle(numberStyle);
					} else {
						R36Cell2.setCellValue("");
						R36Cell2.setCellStyle(textStyle);
					}

// R37
					row = sheet.getRow(36);
					Cell R37Cell1 = row.createCell(2);
					if (record.getR37_lc_as_on_mar() != null) {
						R37Cell1.setCellValue(record.getR37_lc_as_on_mar().doubleValue());
						R37Cell1.setCellStyle(numberStyle);
					} else {
						R37Cell1.setCellValue("");
						R37Cell1.setCellStyle(textStyle);
					}

					Cell R37Cell2 = row.createCell(3);
					if (record.getR37_lc_as_on_sep() != null) {
						R37Cell2.setCellValue(record.getR37_lc_as_on_sep().doubleValue());
						R37Cell2.setCellStyle(numberStyle);
					} else {
						R37Cell2.setCellValue("");
						R37Cell2.setCellStyle(textStyle);
					}

// R38
//row = sheet.getRow(37);
//Cell R38Cell1 = row.createCell(2);
//if (record.getR38_lc_as_on_mar() != null) {
//    R38Cell1.setCellValue(record.getR38_lc_as_on_mar().doubleValue());
//    R38Cell1.setCellStyle(numberStyle);
//} else {
//    R38Cell1.setCellValue("");
//    R38Cell1.setCellStyle(textStyle);
//}
//
//Cell R38Cell2 = row.createCell(3);
//if (record.getR38_lc_as_on_sep() != null) {
//    R38Cell2.setCellValue(record.getR38_lc_as_on_sep().doubleValue());
//    R38Cell2.setCellStyle(numberStyle);
//} else {
//    R38Cell2.setCellValue("");
//    R38Cell2.setCellStyle(textStyle);
//}

// R39
					row = sheet.getRow(38);
					Cell R39Cell1 = row.createCell(2);
					if (record.getR39_lc_as_on_mar() != null) {
						R39Cell1.setCellValue(record.getR39_lc_as_on_mar().doubleValue());
						R39Cell1.setCellStyle(numberStyle);
					} else {
						R39Cell1.setCellValue("");
						R39Cell1.setCellStyle(textStyle);
					}

					Cell R39Cell2 = row.createCell(3);
					if (record.getR39_lc_as_on_sep() != null) {
						R39Cell2.setCellValue(record.getR39_lc_as_on_sep().doubleValue());
						R39Cell2.setCellStyle(numberStyle);
					} else {
						R39Cell2.setCellValue("");
						R39Cell2.setCellStyle(textStyle);
					}

// R40
					row = sheet.getRow(39);
					Cell R40Cell1 = row.createCell(2);
					if (record.getR40_lc_as_on_mar() != null) {
						R40Cell1.setCellValue(record.getR40_lc_as_on_mar().doubleValue());
						R40Cell1.setCellStyle(numberStyle);
					} else {
						R40Cell1.setCellValue("");
						R40Cell1.setCellStyle(textStyle);
					}

					Cell R40Cell2 = row.createCell(3);
					if (record.getR40_lc_as_on_sep() != null) {
						R40Cell2.setCellValue(record.getR40_lc_as_on_sep().doubleValue());
						R40Cell2.setCellStyle(numberStyle);
					} else {
						R40Cell2.setCellValue("");
						R40Cell2.setCellStyle(textStyle);
					}

// R41
					row = sheet.getRow(40);
					Cell R41Cell1 = row.createCell(2);
					if (record.getR41_lc_as_on_mar() != null) {
						R41Cell1.setCellValue(record.getR41_lc_as_on_mar().doubleValue());
						R41Cell1.setCellStyle(numberStyle);
					} else {
						R41Cell1.setCellValue("");
						R41Cell1.setCellStyle(textStyle);
					}

					Cell R41Cell2 = row.createCell(3);
					if (record.getR41_lc_as_on_sep() != null) {
						R41Cell2.setCellValue(record.getR41_lc_as_on_sep().doubleValue());
						R41Cell2.setCellStyle(numberStyle);
					} else {
						R41Cell2.setCellValue("");
						R41Cell2.setCellStyle(textStyle);
					}

// R42
					row = sheet.getRow(41);
					Cell R42Cell1 = row.createCell(2);
					if (record.getR42_lc_as_on_mar() != null) {
						R42Cell1.setCellValue(record.getR42_lc_as_on_mar().doubleValue());
						R42Cell1.setCellStyle(numberStyle);
					} else {
						R42Cell1.setCellValue("");
						R42Cell1.setCellStyle(textStyle);
					}

					Cell R42Cell2 = row.createCell(3);
					if (record.getR42_lc_as_on_sep() != null) {
						R42Cell2.setCellValue(record.getR42_lc_as_on_sep().doubleValue());
						R42Cell2.setCellStyle(numberStyle);
					} else {
						R42Cell2.setCellValue("");
						R42Cell2.setCellStyle(textStyle);
					}

// R43
					row = sheet.getRow(42);
					Cell R43Cell1 = row.createCell(2);
					if (record.getR43_lc_as_on_mar() != null) {
						R43Cell1.setCellValue(record.getR43_lc_as_on_mar().doubleValue());
						R43Cell1.setCellStyle(numberStyle);
					} else {
						R43Cell1.setCellValue("");
						R43Cell1.setCellStyle(textStyle);
					}

					Cell R43Cell2 = row.createCell(3);
					if (record.getR43_lc_as_on_sep() != null) {
						R43Cell2.setCellValue(record.getR43_lc_as_on_sep().doubleValue());
						R43Cell2.setCellStyle(numberStyle);
					} else {
						R43Cell2.setCellValue("");
						R43Cell2.setCellStyle(textStyle);
					}
// R44
					row = sheet.getRow(43);
					Cell R44Cell1 = row.createCell(2);
					if (record.getR44_lc_as_on_mar() != null) {
						R44Cell1.setCellValue(record.getR44_lc_as_on_mar().doubleValue());
						R44Cell1.setCellStyle(numberStyle);
					} else {
						R44Cell1.setCellValue("");
						R44Cell1.setCellStyle(textStyle);
					}

					Cell R44Cell2 = row.createCell(3);
					if (record.getR44_lc_as_on_sep() != null) {
						R44Cell2.setCellValue(record.getR44_lc_as_on_sep().doubleValue());
						R44Cell2.setCellStyle(numberStyle);
					} else {
						R44Cell2.setCellValue("");
						R44Cell2.setCellStyle(textStyle);
					}

// R45
//row = sheet.getRow(44);
//Cell R45Cell1 = row.createCell(2);
//if (record.getR45_lc_as_on_mar() != null) {
//    R45Cell1.setCellValue(record.getR45_lc_as_on_mar().doubleValue());
//    R45Cell1.setCellStyle(numberStyle);
//} else {
//    R45Cell1.setCellValue("");
//    R45Cell1.setCellStyle(textStyle);
//}
//
//Cell R45Cell2 = row.createCell(3);
//if (record.getR45_lc_as_on_sep() != null) {
//    R45Cell2.setCellValue(record.getR45_lc_as_on_sep().doubleValue());
//    R45Cell2.setCellStyle(numberStyle);
//} else {
//    R45Cell2.setCellValue("");
//    R45Cell2.setCellStyle(textStyle);
//}

// R46
//row = sheet.getRow(45);
//Cell R46Cell1 = row.createCell(2);
//if (record.getR46_lc_as_on_mar() != null) {
//    R46Cell1.setCellValue(record.getR46_lc_as_on_mar().doubleValue());
//    R46Cell1.setCellStyle(numberStyle);
//} else {
//    R46Cell1.setCellValue("");
//    R46Cell1.setCellStyle(textStyle);
//}
//
//Cell R46Cell2 = row.createCell(3);
//if (record.getR46_lc_as_on_sep() != null) {
//    R46Cell2.setCellValue(record.getR46_lc_as_on_sep().doubleValue());
//    R46Cell2.setCellStyle(numberStyle);
//} else {
//    R46Cell2.setCellValue("");
//    R46Cell2.setCellStyle(textStyle);
//}

// R47
					row = sheet.getRow(46);
					Cell R47Cell1 = row.createCell(2);
					if (record.getR47_lc_as_on_mar() != null) {
						R47Cell1.setCellValue(record.getR47_lc_as_on_mar().doubleValue());
						R47Cell1.setCellStyle(numberStyle);
					} else {
						R47Cell1.setCellValue("");
						R47Cell1.setCellStyle(textStyle);
					}

					Cell R47Cell2 = row.createCell(3);
					if (record.getR47_lc_as_on_sep() != null) {
						R47Cell2.setCellValue(record.getR47_lc_as_on_sep().doubleValue());
						R47Cell2.setCellStyle(numberStyle);
					} else {
						R47Cell2.setCellValue("");
						R47Cell2.setCellStyle(textStyle);
					}

// R48
					row = sheet.getRow(47);
					Cell R48Cell1 = row.createCell(2);
					if (record.getR48_lc_as_on_mar() != null) {
						R48Cell1.setCellValue(record.getR48_lc_as_on_mar().doubleValue());
						R48Cell1.setCellStyle(numberStyle);
					} else {
						R48Cell1.setCellValue("");
						R48Cell1.setCellStyle(textStyle);
					}

//Cell R48Cell2 = row.createCell(3);
//if (record.getR48_lc_as_on_sep() != null) {
//    R48Cell2.setCellValue(record.getR48_lc_as_on_sep().doubleValue());
//    R48Cell2.setCellStyle(numberStyle);
//} else {
//    R48Cell2.setCellValue("");
//    R48Cell2.setCellStyle(textStyle);
//}

// R49
					row = sheet.getRow(48);
					Cell R49Cell1 = row.createCell(2);
					if (record.getR49_lc_as_on_mar() != null) {
						R49Cell1.setCellValue(record.getR49_lc_as_on_mar().doubleValue());
						R49Cell1.setCellStyle(numberStyle);
					} else {
						R49Cell1.setCellValue("");
						R49Cell1.setCellStyle(textStyle);
					}

//Cell R49Cell2 = row.createCell(3);
//if (record.getR49_lc_as_on_sep() != null) {
//    R49Cell2.setCellValue(record.getR49_lc_as_on_sep().doubleValue());
//    R49Cell2.setCellStyle(numberStyle);
//} else {
//    R49Cell2.setCellValue("");
//    R49Cell2.setCellStyle(textStyle);
//}

// R50
					row = sheet.getRow(49);
					Cell R50Cell1 = row.createCell(2);
					if (record.getR50_lc_as_on_mar() != null) {
						R50Cell1.setCellValue(record.getR50_lc_as_on_mar().doubleValue());
						R50Cell1.setCellStyle(numberStyle);
					} else {
						R50Cell1.setCellValue("");
						R50Cell1.setCellStyle(textStyle);
					}

					Cell R50Cell2 = row.createCell(3);
					if (record.getR50_lc_as_on_sep() != null) {
						R50Cell2.setCellValue(record.getR50_lc_as_on_sep().doubleValue());
						R50Cell2.setCellStyle(numberStyle);
					} else {
						R50Cell2.setCellValue("");
						R50Cell2.setCellStyle(textStyle);
					}

// R51
					row = sheet.getRow(50);
					Cell R51Cell1 = row.createCell(2);
					if (record.getR51_lc_as_on_mar() != null) {
						R51Cell1.setCellValue(record.getR51_lc_as_on_mar().doubleValue());
						R51Cell1.setCellStyle(numberStyle);
					} else {
						R51Cell1.setCellValue("");
						R51Cell1.setCellStyle(textStyle);
					}

//Cell R51Cell2 = row.createCell(3);
//if (record.getR51_lc_as_on_sep() != null) {
//    R51Cell2.setCellValue(record.getR51_lc_as_on_sep().doubleValue());
//    R51Cell2.setCellStyle(numberStyle);
//} else {
//    R51Cell2.setCellValue("");
//    R51Cell2.setCellStyle(textStyle);
//}

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

	public byte[] getCASH_FLOWDetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for CASH_FLOW Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getCASH_FLOWDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype,
						type, version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("CASH_FLOWDetails");

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
			List<CASH_FLOW_Detail_Entity> reportData = CASH_FLOW_detail_repo.getdatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (CASH_FLOW_Detail_Entity item : reportData) {
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
				logger.info("No data found for CASH_FLOW — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating CASH_FLOW Excel", e);
			return new byte[0];
		}
	}

	public byte[] getCASH_FLOWDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for CASH_FLOW ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("CASH_FLOWDetail");

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
			List<CASH_FLOW_Archival_Detail_Entity> reportData = CASH_FLOW_Archival_Detail_Repo
					.getdatabydateList(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (CASH_FLOW_Archival_Detail_Entity item : reportData) {
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
				logger.info("No data found for CASH_FLOW — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating  CASH_FLOW Excel", e);
			return new byte[0];
		}
	}

	@Autowired
	BRRS_CASH_FLOW_Detail_Repo brrs_CASH_FLOW_detail_repo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/CASH_FLOW"); // ✅ match the report name

		if (acctNo != null) {
			CASH_FLOW_Detail_Entity CASH_FLOWEntity = brrs_CASH_FLOW_detail_repo.findByAcctnumber(acctNo);
			if (CASH_FLOWEntity != null && CASH_FLOWEntity.getREPORT_DATE() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(CASH_FLOWEntity.getREPORT_DATE());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("CASH_FLOWData", CASH_FLOWEntity);
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
			String reportDateStr = request.getParameter("REPORT_DATE");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			CASH_FLOW_Detail_Entity existing = brrs_CASH_FLOW_detail_repo.findByAcctnumber(acctNo);
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
				brrs_CASH_FLOW_detail_repo.save(existing);
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_CASH_FLOW_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_CASH_FLOW_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating CASH_FLOW record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	public List<Object[]> getCASH_FLOWResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<CASH_FLOW_Archival_Summary_Entity> latestArchivalList = CASH_FLOW_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (CASH_FLOW_Archival_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getREPORT_RESUB_DATE() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching CASH_FLOW Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	// Archival View
	public List<Object[]> getCASH_FLOWArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<CASH_FLOW_Archival_Summary_Entity> repoData = CASH_FLOW_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (CASH_FLOW_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getREPORT_RESUB_DATE() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				CASH_FLOW_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching CASH_FLOW Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

}