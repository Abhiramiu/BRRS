package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
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
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_M_SIR_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SIR_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SIR_Detail_Repo;
import com.bornfire.brrs.entities.M_SIR_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SIR_Detail_Entity;
import com.bornfire.brrs.entities.M_SIR_Summary_Entity;
import com.bornfire.brrs.entities.Q_STAFF_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_SIR_Summary_Repo;
import com.bornfire.brrs.entities.M_CA7_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_CA7_Summary_Entity;
import com.bornfire.brrs.entities.M_SIR_Archival_Detail_Entity;

@Service
@Component
public class BRRS_M_SIR_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SIR_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_SIR_Summary_Repo BRRS_M_SIR_Summary_Repo;

	@Autowired
	BRRS_M_SIR_Archival_Summary_Repo BRRS_M_SIR_Archival_Summary_Repo;

	BRRS_M_SIR_Detail_Repo M_SIR_Detail_Repo;
	BRRS_M_SIR_Archival_Detail_Repo M_SIR_Archival_Detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_SIRView(String reportId, String fromdate, String todate, String currency, String dtltype,
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

				List<M_SIR_Archival_Summary_Entity> T1Master = BRRS_M_SIR_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {

				List<M_SIR_Archival_Summary_Entity> T1Master = BRRS_M_SIR_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {

				List<M_SIR_Summary_Entity> T1Master = BRRS_M_SIR_Summary_Repo
						.getdatabydateList(dateformat.parse(todate));
				mv.addObject("displaymode", "summary");
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if (version != null) {
					List<M_SIR_Archival_Detail_Entity> T1Master = M_SIR_Archival_Detail_Repo
							.getdatabydateListarchival(d1, version);
					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_SIR_Detail_Entity> T1Master = M_SIR_Detail_Repo.getdatabydateList(dateformat.parse(todate));
					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_SIR");

		System.out.println("View set to: " + mv.getViewName());

		return mv;
	}

	// ==========================================================
	// MAIN METHOD
	// ==========================================================

	@Transactional
	public void updateReport(M_SIR_Summary_Entity entity) {

		System.out.println("Report Date: " + entity.getReportDate());

		// 1️⃣ Load SUMMARY
		M_SIR_Summary_Entity existingSummary = BRRS_M_SIR_Summary_Repo.findById(entity.getReportDate())
				.orElseThrow(() -> new RuntimeException("Record not found for REPORT_DATE: " + entity.getReportDate()));

		// 2️⃣ Load DETAIL (create if not present)
		M_SIR_Detail_Entity existingDetail = M_SIR_Detail_Repo.findById(entity.getReportDate()).orElseGet(() -> {
			M_SIR_Detail_Entity d = new M_SIR_Detail_Entity();
			d.setReportDate(entity.getReportDate());
			return d;
		});

		// 3️⃣ Copy all fields into BOTH
		copyFields(entity, existingSummary);
		copyFields(entity, existingDetail);

		// 4️⃣ Save BOTH
		BRRS_M_SIR_Summary_Repo.save(existingSummary);
		M_SIR_Detail_Repo.save(existingDetail);
	}

	// ==========================================================
	// COPY ALL LOGIC
	// ==========================================================

	private void copyFields(M_SIR_Summary_Entity source, Object target) {

		try {

			// ---- R13 to R17 ----
			copyRange(source, target, 13, 17);
			copyTotal(source, target, "R12");

			// ---- R19 to R23 ----
			copyRange(source, target, 19, 23);
			copyTotal(source, target, "R18");

			// ---- R24 to R26 ----
			copyRange(source, target, 24, 26);

			// ---- R28 to R32 ----
			copyRange(source, target, 28, 32);
			copyTotal(source, target, "R27");

			// ---- R33 ----
			copySingleFields(source, target, "R33", new String[] { "capital_6m", "capital_6to24m", "capital_gt24m" });

			// ---- R35 ----
			copySingleFields(source, target, "R35", new String[] { "tot_spec_risk_ch" });

		} catch (Exception e) {
			throw new RuntimeException("Error while copying fields", e);
		}
	}

	// ==========================================================
	// RANGE COPY (R13-17, R19-23, R24-26, R28-32)
	// ==========================================================

	private void copyRange(Object source, Object target, int from, int to) throws Exception {

		String[] fields = { "amt_6m", "risk_6m", "capital_6m", "amt_6to24m", "risk_6to24m", "capital_6to24m",
				"amt_gt24m", "risk_gt24m", "capital_gt24m" };

		for (int i = from; i <= to; i++) {

			String prefix = "R" + i + "_";

			for (String field : fields) {

				String getterName = "get" + prefix + field;
				String setterName = "set" + prefix + field;

				try {
					Method getter = source.getClass().getMethod(getterName);

					Method setter = target.getClass().getMethod(setterName, getter.getReturnType());

					Object value = getter.invoke(source);
					setter.invoke(target, value);

				} catch (NoSuchMethodException e) {
					continue; // skip missing
				}
			}
		}
	}

	// ==========================================================
	// TOTAL ROW COPY (R12, R18, R27)
	// ==========================================================

	private void copyTotal(Object source, Object target, String row) throws Exception {

		String[] fields = { "amt_6m", "risk_6m", "capital_6m", "amt_6to24m", "risk_6to24m", "capital_6to24m",
				"amt_gt24m", "risk_gt24m", "capital_gt24m" };

		for (String field : fields) {

			String getterName = "get" + row + "_" + field;
			String setterName = "set" + row + "_" + field;

			try {

				Method getter = source.getClass().getMethod(getterName);

				Method setter = target.getClass().getMethod(setterName, getter.getReturnType());

				Object value = getter.invoke(source);
				setter.invoke(target, value);

			} catch (NoSuchMethodException e) {
				continue;
			}
		}
	}

	// ==========================================================
	// SINGLE FIELDS (R33, R35)
	// ==========================================================

	private void copySingleFields(Object source, Object target, String prefix, String[] fields) throws Exception {

		for (String field : fields) {

			String getterName = "get" + prefix + "_" + field;
			String setterName = "set" + prefix + "_" + field;

			try {

				Method getter = source.getClass().getMethod(getterName);

				Method setter = target.getClass().getMethod(setterName, getter.getReturnType());

				Object value = getter.invoke(source);
				setter.invoke(target, value);

			} catch (NoSuchMethodException e) {
				continue;
			}
		}
	}

	public byte[] getM_SIRExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

// Convert string to Date
		Date reportDate = dateformat.parse(todate);

		System.out.println(type);
		System.out.println(version);
		if (type.equals("ARCHIVAL") & version != null) {
			byte[] ARCHIVALreport = getExcelM_SIRARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
					version);
			return ARCHIVALreport;
		}
		// Email check
		if ("email".equalsIgnoreCase(type) && version == null) {
			logger.info("Service: Generating Email report for version {}", version);
			return BRRS_M_SIREmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		} else if ("email".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating Email report for version {}", version);
			return BRRS_M_SIREmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

		}
//		// RESUB check
//		else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
//		logger.info("Service: Generating RESUB report for version {}", version);
//
//
//		List<M_SIR_Archival_Summary_Entity> T1Master =
//				BRRS_M_SIR_Archival_Summary_Repo.getdatabydateListarchival(reportDate, version);

		// Generate Excel for RESUB
//		return BRRS_M_SIRResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
//		}
		List<M_SIR_Summary_Entity> dataList = BRRS_M_SIR_Summary_Repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SIR report. Returning empty result.");
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
			font.setFontName("calibriLight");

			CellStyle numberStyle = workbook.createCellStyle();
			// numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
			// --- End of Style Definitions ---

			// Create pure light green style (Excel highlight green)
			XSSFCellStyle greenStyle = (XSSFCellStyle) workbook.createCellStyle();
			greenStyle.cloneStyleFrom(textStyle);

			byte[] rgb = new byte[] { (byte) 146, (byte) 208, (byte) 80 }; // exact Excel light green
			XSSFColor green = new XSSFColor(rgb, null);

			greenStyle.setFillForegroundColor(green);
			greenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			greenStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SIR_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//----------

					// row11
					// Column C
					row = sheet.getRow(11);

					Cell cell1 = row.createCell(2);
					if (record.getR12_amt_6m() != null) {
						cell1.setCellValue(record.getR12_amt_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.createCell(4);
					if (record.getR12_capital_6m() != null) {
						cell1.setCellValue(record.getR12_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F

					cell1 = row.createCell(5);
					if (record.getR12_amt_6to24m() != null) {
						cell1.setCellValue(record.getR12_amt_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column H
					cell1 = row.createCell(7);
					if (record.getR12_capital_6to24m() != null) {
						cell1.setCellValue(record.getR12_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column I

					cell1 = row.createCell(8);
					if (record.getR12_amt_gt24m() != null) {
						cell1.setCellValue(record.getR12_amt_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row11
					// Column K
					cell1 = row.createCell(10);
					if (record.getR12_capital_gt24m() != null) {
						cell1.setCellValue(record.getR12_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
//-------------

					// row12
					// Column b
					row = sheet.getRow(12);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR13_amt_6m() != null) {
						cell1.setCellValue(record.getR13_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR13_capital_6m() != null) {
						cell1.setCellValue(record.getR13_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR13_amt_6to24m() != null) {
						cell1.setCellValue(record.getR13_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR13_capital_6to24m() != null) {
						cell1.setCellValue(record.getR13_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR13_amt_gt24m() != null) {
						cell1.setCellValue(record.getR13_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR13_capital_gt24m() != null) {
						cell1.setCellValue(record.getR13_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row13
					// Column b
					row = sheet.getRow(13);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR14_amt_6m() != null) {
						cell1.setCellValue(record.getR14_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR14_capital_6m() != null) {
						cell1.setCellValue(record.getR14_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR14_amt_6to24m() != null) {
						cell1.setCellValue(record.getR14_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR14_capital_6to24m() != null) {
						cell1.setCellValue(record.getR14_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR14_amt_gt24m() != null) {
						cell1.setCellValue(record.getR14_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR14_capital_gt24m() != null) {
						cell1.setCellValue(record.getR14_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row14
					// Column b
					row = sheet.getRow(14);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR15_amt_6m() != null) {
						cell1.setCellValue(record.getR15_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR15_capital_6m() != null) {
						cell1.setCellValue(record.getR15_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR15_amt_6to24m() != null) {
						cell1.setCellValue(record.getR15_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR15_capital_6to24m() != null) {
						cell1.setCellValue(record.getR15_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR15_amt_gt24m() != null) {
						cell1.setCellValue(record.getR15_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR15_capital_gt24m() != null) {
						cell1.setCellValue(record.getR15_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
//				//row15  (R16)
					// Column B
					row = sheet.getRow(15);
					cell1 = row.getCell(1);

					// Column C
					cell1 = row.getCell(2);
					if (record.getR16_amt_6m() != null) {
						cell1.setCellValue(record.getR16_amt_6m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR16_capital_6m() != null) {
						cell1.setCellValue(record.getR16_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR16_amt_6to24m() != null) {
						cell1.setCellValue(record.getR16_amt_6to24m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR16_capital_6to24m() != null) {
						cell1.setCellValue(record.getR16_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR16_amt_gt24m() != null) {
						cell1.setCellValue(record.getR16_amt_gt24m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR16_capital_gt24m() != null) {
						cell1.setCellValue(record.getR16_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row16 (R17)
					row = sheet.getRow(16);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR17_amt_6m() != null)
						cell1.setCellValue(record.getR17_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR17_capital_6m() != null) {
						cell1.setCellValue(record.getR17_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR17_amt_6to24m() != null)
						cell1.setCellValue(record.getR17_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR17_capital_6to24m() != null) {
						cell1.setCellValue(record.getR17_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR17_amt_gt24m() != null)
						cell1.setCellValue(record.getR17_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR17_capital_gt24m() != null) {
						cell1.setCellValue(record.getR17_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row17 (R18)
					row = sheet.getRow(17);

					// Column B

					// Column C

					cell1 = row.createCell(2);
					if (record.getR18_amt_6m() != null) {
						cell1.setCellValue(record.getR18_amt_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column E
					cell1 = row.createCell(4);
					if (record.getR18_capital_6m() != null) {
						cell1.setCellValue(record.getR18_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F

					cell1 = row.createCell(5);
					if (record.getR18_amt_6to24m() != null) {
						cell1.setCellValue(record.getR18_amt_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column H
					cell1 = row.createCell(7);
					if (record.getR18_capital_6to24m() != null) {
						cell1.setCellValue(record.getR18_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I

					cell1 = row.createCell(8);
					if (record.getR18_amt_gt24m() != null) {
						cell1.setCellValue(record.getR18_amt_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column K
					cell1 = row.createCell(10);
					if (record.getR18_capital_gt24m() != null) {
						cell1.setCellValue(record.getR18_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row18 (R19)
					row = sheet.getRow(18);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR19_amt_6m() != null)
						cell1.setCellValue(record.getR19_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR19_capital_6m() != null) {
						cell1.setCellValue(record.getR19_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR19_amt_6to24m() != null)
						cell1.setCellValue(record.getR19_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR19_capital_6to24m() != null) {
						cell1.setCellValue(record.getR19_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR19_amt_gt24m() != null)
						cell1.setCellValue(record.getR19_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR19_capital_gt24m() != null) {
						cell1.setCellValue(record.getR19_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row19 (R20)
					row = sheet.getRow(19);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR20_amt_6m() != null)
						cell1.setCellValue(record.getR20_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR20_capital_6m() != null) {
						cell1.setCellValue(record.getR20_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR20_amt_6to24m() != null)
						cell1.setCellValue(record.getR20_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR20_capital_6to24m() != null) {
						cell1.setCellValue(record.getR20_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR20_amt_gt24m() != null)
						cell1.setCellValue(record.getR20_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR20_capital_gt24m() != null) {
						cell1.setCellValue(record.getR20_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row20 (R21)
					row = sheet.getRow(20);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR21_amt_6m() != null)
						cell1.setCellValue(record.getR21_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR21_capital_6m() != null) {
						cell1.setCellValue(record.getR21_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR21_amt_6to24m() != null)
						cell1.setCellValue(record.getR21_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR21_capital_6to24m() != null) {
						cell1.setCellValue(record.getR21_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR21_amt_gt24m() != null)
						cell1.setCellValue(record.getR21_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR21_capital_gt24m() != null) {
						cell1.setCellValue(record.getR21_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row21 (R22)
					row = sheet.getRow(21);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR22_amt_6m() != null)
						cell1.setCellValue(record.getR22_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR22_capital_6m() != null) {
						cell1.setCellValue(record.getR22_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR22_amt_6to24m() != null)
						cell1.setCellValue(record.getR22_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR22_capital_6to24m() != null) {
						cell1.setCellValue(record.getR22_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR22_amt_gt24m() != null)
						cell1.setCellValue(record.getR22_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR22_capital_gt24m() != null) {
						cell1.setCellValue(record.getR22_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row22 (R23)
					row = sheet.getRow(22);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR23_amt_6m() != null)
						cell1.setCellValue(record.getR23_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR23_capital_6m() != null) {
						cell1.setCellValue(record.getR23_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR23_amt_6to24m() != null)
						cell1.setCellValue(record.getR23_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR23_capital_6to24m() != null) {
						cell1.setCellValue(record.getR23_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR23_amt_gt24m() != null)
						cell1.setCellValue(record.getR23_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR23_capital_gt24m() != null) {
						cell1.setCellValue(record.getR23_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row23 (R24)
					row = sheet.getRow(23);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR24_amt_6m() != null)
						cell1.setCellValue(record.getR24_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR24_capital_6m() != null) {
						cell1.setCellValue(record.getR24_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR24_amt_6to24m() != null)
						cell1.setCellValue(record.getR24_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR24_capital_6to24m() != null) {
						cell1.setCellValue(record.getR24_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR24_amt_gt24m() != null)
						cell1.setCellValue(record.getR24_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR24_capital_gt24m() != null) {
						cell1.setCellValue(record.getR24_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row24 (R25)
					row = sheet.getRow(24);

					// Column C
					cell1 = row.getCell(2);
					if (record.getR25_amt_6m() != null)
						cell1.setCellValue(record.getR25_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR25_capital_6m() != null) {
						cell1.setCellValue(record.getR25_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR25_amt_6to24m() != null)
						cell1.setCellValue(record.getR25_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR25_capital_6to24m() != null) {
						cell1.setCellValue(record.getR25_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR25_amt_gt24m() != null)
						cell1.setCellValue(record.getR25_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR25_capital_gt24m() != null) {
						cell1.setCellValue(record.getR25_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row25 (R26)
					row = sheet.getRow(25);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR26_amt_6m() != null)
						cell1.setCellValue(record.getR26_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR26_capital_6m() != null) {
						cell1.setCellValue(record.getR26_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR26_amt_6to24m() != null)
						cell1.setCellValue(record.getR26_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR26_capital_6to24m() != null) {
						cell1.setCellValue(record.getR26_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR26_amt_gt24m() != null)
						cell1.setCellValue(record.getR26_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR26_capital_gt24m() != null) {
						cell1.setCellValue(record.getR26_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row26 (R27)
					row = sheet.getRow(26);

					// Column B

					// Column C

					cell1 = row.createCell(2);
					if (record.getR27_amt_6m() != null) {
						cell1.setCellValue(record.getR27_amt_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column E
					cell1 = row.createCell(4);
					if (record.getR27_capital_6m() != null) {
						cell1.setCellValue(record.getR27_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F

					cell1 = row.createCell(5);
					if (record.getR27_amt_6to24m() != null) {
						cell1.setCellValue(record.getR27_amt_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column H
					cell1 = row.createCell(7);
					if (record.getR27_capital_6to24m() != null) {
						cell1.setCellValue(record.getR27_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I

					cell1 = row.createCell(8);
					if (record.getR27_amt_gt24m() != null) {
						cell1.setCellValue(record.getR27_amt_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column K
					cell1 = row.createCell(10);
					if (record.getR27_capital_gt24m() != null) {
						cell1.setCellValue(record.getR27_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row27 (R28)
					row = sheet.getRow(27);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR28_amt_6m() != null)
						cell1.setCellValue(record.getR28_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR28_capital_6m() != null) {
						cell1.setCellValue(record.getR28_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR28_amt_6to24m() != null)
						cell1.setCellValue(record.getR28_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR28_capital_6to24m() != null) {
						cell1.setCellValue(record.getR28_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR28_amt_gt24m() != null)
						cell1.setCellValue(record.getR28_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR28_capital_gt24m() != null) {
						cell1.setCellValue(record.getR28_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row28 (R29)
					row = sheet.getRow(28);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR29_amt_6m() != null)
						cell1.setCellValue(record.getR29_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR29_capital_6m() != null) {
						cell1.setCellValue(record.getR29_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR29_amt_6to24m() != null)
						cell1.setCellValue(record.getR29_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR29_capital_6to24m() != null) {
						cell1.setCellValue(record.getR29_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR29_amt_gt24m() != null)
						cell1.setCellValue(record.getR29_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR29_capital_gt24m() != null) {
						cell1.setCellValue(record.getR29_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row29 (R30)
					row = sheet.getRow(29);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR30_amt_6m() != null)
						cell1.setCellValue(record.getR30_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR30_capital_6m() != null) {
						cell1.setCellValue(record.getR30_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR30_amt_6to24m() != null)
						cell1.setCellValue(record.getR30_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR30_capital_6to24m() != null) {
						cell1.setCellValue(record.getR30_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR30_amt_gt24m() != null)
						cell1.setCellValue(record.getR30_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR30_capital_gt24m() != null) {
						cell1.setCellValue(record.getR30_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row30 (R31)
					row = sheet.getRow(30);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR31_amt_6m() != null)
						cell1.setCellValue(record.getR31_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR31_capital_6m() != null) {
						cell1.setCellValue(record.getR31_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR31_amt_6to24m() != null)
						cell1.setCellValue(record.getR31_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR31_capital_6to24m() != null) {
						cell1.setCellValue(record.getR31_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR31_amt_gt24m() != null)
						cell1.setCellValue(record.getR31_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR31_capital_gt24m() != null) {
						cell1.setCellValue(record.getR31_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row31 (R32)
					row = sheet.getRow(31);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR32_amt_6m() != null)
						cell1.setCellValue(record.getR32_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR32_capital_6m() != null) {
						cell1.setCellValue(record.getR32_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR32_amt_6to24m() != null)
						cell1.setCellValue(record.getR32_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR32_capital_6to24m() != null) {
						cell1.setCellValue(record.getR32_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR32_amt_gt24m() != null)
						cell1.setCellValue(record.getR32_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR32_capital_gt24m() != null) {
						cell1.setCellValue(record.getR32_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row32 (R33)
					row = sheet.getRow(32);

					// Column B

					// Column E
					cell1 = row.createCell(4);
					if (record.getR33_capital_6m() != null) {
						cell1.setCellValue(record.getR33_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR33_capital_6to24m() != null) {
						cell1.setCellValue(record.getR33_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR33_capital_gt24m() != null) {
						cell1.setCellValue(record.getR33_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row34
					// Column K
					row = sheet.getRow(34);
					cell1 = row.createCell(4);
					if (record.getR35_tot_spec_risk_ch() != null) {
						cell1.setCellValue(record.getR35_tot_spec_risk_ch().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column K
				} // end of loop

				// ❌ REMOVE this (do NOT evaluate formulas)
				// workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();

			} else {
				// no data case
			}

			// Write final workbook to output stream
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}
	}

	public byte[] getExcelM_SIRARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {
		}
		List<M_SIR_Archival_Summary_Entity> dataList = BRRS_M_SIR_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		System.out.println("comming to archival ............." + type);
		System.out.println("excel archival......" + filename);
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SIR report. Returning empty result.");
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
			font.setFontName("calibriLight");

			CellStyle numberStyle = workbook.createCellStyle();
			// numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
			// --- End of Style Definitions ---

			// Create pure light green style (Excel highlight green)
			XSSFCellStyle greenStyle = (XSSFCellStyle) workbook.createCellStyle();
			greenStyle.cloneStyleFrom(textStyle);

			byte[] rgb = new byte[] { (byte) 146, (byte) 208, (byte) 80 }; // exact Excel light green
			XSSFColor green = new XSSFColor(rgb, null);

			greenStyle.setFillForegroundColor(green);
			greenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			greenStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SIR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//----------
					// row11
					// Column C
					row = sheet.getRow(11);

					Cell cell1 = row.createCell(2);
					if (record.getR12_amt_6m() != null) {
						cell1.setCellValue(record.getR12_amt_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.createCell(4);
					if (record.getR12_capital_6m() != null) {
						cell1.setCellValue(record.getR12_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F

					cell1 = row.createCell(5);
					if (record.getR12_amt_6to24m() != null) {
						cell1.setCellValue(record.getR12_amt_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column H
					cell1 = row.createCell(7);
					if (record.getR12_capital_6to24m() != null) {
						cell1.setCellValue(record.getR12_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column I

					cell1 = row.createCell(8);
					if (record.getR12_amt_gt24m() != null) {
						cell1.setCellValue(record.getR12_amt_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row11
					// Column K
					cell1 = row.createCell(10);
					if (record.getR12_capital_gt24m() != null) {
						cell1.setCellValue(record.getR12_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
//-------------

					// row12
					// Column b
					row = sheet.getRow(12);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR13_amt_6m() != null) {
						cell1.setCellValue(record.getR13_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR13_capital_6m() != null) {
						cell1.setCellValue(record.getR13_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR13_amt_6to24m() != null) {
						cell1.setCellValue(record.getR13_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR13_capital_6to24m() != null) {
						cell1.setCellValue(record.getR13_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR13_amt_gt24m() != null) {
						cell1.setCellValue(record.getR13_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR13_capital_gt24m() != null) {
						cell1.setCellValue(record.getR13_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row13
					// Column b
					row = sheet.getRow(13);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR14_amt_6m() != null) {
						cell1.setCellValue(record.getR14_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR14_capital_6m() != null) {
						cell1.setCellValue(record.getR14_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR14_amt_6to24m() != null) {
						cell1.setCellValue(record.getR14_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR14_capital_6to24m() != null) {
						cell1.setCellValue(record.getR14_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR14_amt_gt24m() != null) {
						cell1.setCellValue(record.getR14_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR14_capital_gt24m() != null) {
						cell1.setCellValue(record.getR14_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row14
					// Column b
					row = sheet.getRow(14);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR15_amt_6m() != null) {
						cell1.setCellValue(record.getR15_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR15_capital_6m() != null) {
						cell1.setCellValue(record.getR15_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR15_amt_6to24m() != null) {
						cell1.setCellValue(record.getR15_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR15_capital_6to24m() != null) {
						cell1.setCellValue(record.getR15_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR15_amt_gt24m() != null) {
						cell1.setCellValue(record.getR15_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR15_capital_gt24m() != null) {
						cell1.setCellValue(record.getR15_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
//				//row15  (R16)
					// Column B
					row = sheet.getRow(15);
					cell1 = row.getCell(1);

					// Column C
					cell1 = row.getCell(2);
					if (record.getR16_amt_6m() != null) {
						cell1.setCellValue(record.getR16_amt_6m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR16_capital_6m() != null) {
						cell1.setCellValue(record.getR16_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR16_amt_6to24m() != null) {
						cell1.setCellValue(record.getR16_amt_6to24m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR16_capital_6to24m() != null) {
						cell1.setCellValue(record.getR16_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR16_amt_gt24m() != null) {
						cell1.setCellValue(record.getR16_amt_gt24m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR16_capital_gt24m() != null) {
						cell1.setCellValue(record.getR16_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row16 (R17)
					row = sheet.getRow(16);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR17_amt_6m() != null)
						cell1.setCellValue(record.getR17_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR17_capital_6m() != null) {
						cell1.setCellValue(record.getR17_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR17_amt_6to24m() != null)
						cell1.setCellValue(record.getR17_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR17_capital_6to24m() != null) {
						cell1.setCellValue(record.getR17_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR17_amt_gt24m() != null)
						cell1.setCellValue(record.getR17_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR17_capital_gt24m() != null) {
						cell1.setCellValue(record.getR17_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row17 (R18)
					row = sheet.getRow(17);

					// Column B

					// Column C

					cell1 = row.createCell(2);
					if (record.getR18_amt_6m() != null) {
						cell1.setCellValue(record.getR18_amt_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column E
					cell1 = row.createCell(4);
					if (record.getR18_capital_6m() != null) {
						cell1.setCellValue(record.getR18_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F

					cell1 = row.createCell(5);
					if (record.getR18_amt_6to24m() != null) {
						cell1.setCellValue(record.getR18_amt_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column H
					cell1 = row.createCell(7);
					if (record.getR18_capital_6to24m() != null) {
						cell1.setCellValue(record.getR18_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I

					cell1 = row.createCell(8);
					if (record.getR18_amt_gt24m() != null) {
						cell1.setCellValue(record.getR18_amt_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column K
					cell1 = row.createCell(10);
					if (record.getR18_capital_gt24m() != null) {
						cell1.setCellValue(record.getR18_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row18 (R19)
					row = sheet.getRow(18);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR19_amt_6m() != null)
						cell1.setCellValue(record.getR19_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR19_capital_6m() != null) {
						cell1.setCellValue(record.getR19_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR19_amt_6to24m() != null)
						cell1.setCellValue(record.getR19_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR19_capital_6to24m() != null) {
						cell1.setCellValue(record.getR19_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR19_amt_gt24m() != null)
						cell1.setCellValue(record.getR19_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR19_capital_gt24m() != null) {
						cell1.setCellValue(record.getR19_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row19 (R20)
					row = sheet.getRow(19);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR20_amt_6m() != null)
						cell1.setCellValue(record.getR20_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR20_capital_6m() != null) {
						cell1.setCellValue(record.getR20_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR20_amt_6to24m() != null)
						cell1.setCellValue(record.getR20_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR20_capital_6to24m() != null) {
						cell1.setCellValue(record.getR20_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR20_amt_gt24m() != null)
						cell1.setCellValue(record.getR20_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR20_capital_gt24m() != null) {
						cell1.setCellValue(record.getR20_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row20 (R21)
					row = sheet.getRow(20);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR21_amt_6m() != null)
						cell1.setCellValue(record.getR21_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR21_capital_6m() != null) {
						cell1.setCellValue(record.getR21_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR21_amt_6to24m() != null)
						cell1.setCellValue(record.getR21_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR21_capital_6to24m() != null) {
						cell1.setCellValue(record.getR21_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR21_amt_gt24m() != null)
						cell1.setCellValue(record.getR21_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR21_capital_gt24m() != null) {
						cell1.setCellValue(record.getR21_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row21 (R22)
					row = sheet.getRow(21);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR22_amt_6m() != null)
						cell1.setCellValue(record.getR22_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR22_capital_6m() != null) {
						cell1.setCellValue(record.getR22_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR22_amt_6to24m() != null)
						cell1.setCellValue(record.getR22_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR22_capital_6to24m() != null) {
						cell1.setCellValue(record.getR22_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR22_amt_gt24m() != null)
						cell1.setCellValue(record.getR22_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR22_capital_gt24m() != null) {
						cell1.setCellValue(record.getR22_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row22 (R23)
					row = sheet.getRow(22);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR23_amt_6m() != null)
						cell1.setCellValue(record.getR23_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR23_capital_6m() != null) {
						cell1.setCellValue(record.getR23_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR23_amt_6to24m() != null)
						cell1.setCellValue(record.getR23_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR23_capital_6to24m() != null) {
						cell1.setCellValue(record.getR23_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR23_amt_gt24m() != null)
						cell1.setCellValue(record.getR23_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR23_capital_gt24m() != null) {
						cell1.setCellValue(record.getR23_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row23 (R24)
					row = sheet.getRow(23);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR24_amt_6m() != null)
						cell1.setCellValue(record.getR24_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR24_capital_6m() != null) {
						cell1.setCellValue(record.getR24_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR24_amt_6to24m() != null)
						cell1.setCellValue(record.getR24_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR24_capital_6to24m() != null) {
						cell1.setCellValue(record.getR24_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR24_amt_gt24m() != null)
						cell1.setCellValue(record.getR24_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR24_capital_gt24m() != null) {
						cell1.setCellValue(record.getR24_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row24 (R25)
					row = sheet.getRow(24);

					// Column C
					cell1 = row.getCell(2);
					if (record.getR25_amt_6m() != null)
						cell1.setCellValue(record.getR25_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR25_capital_6m() != null) {
						cell1.setCellValue(record.getR25_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR25_amt_6to24m() != null)
						cell1.setCellValue(record.getR25_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR25_capital_6to24m() != null) {
						cell1.setCellValue(record.getR25_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR25_amt_gt24m() != null)
						cell1.setCellValue(record.getR25_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR25_capital_gt24m() != null) {
						cell1.setCellValue(record.getR25_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row25 (R26)
					row = sheet.getRow(25);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR26_amt_6m() != null)
						cell1.setCellValue(record.getR26_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR26_capital_6m() != null) {
						cell1.setCellValue(record.getR26_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR26_amt_6to24m() != null)
						cell1.setCellValue(record.getR26_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR26_capital_6to24m() != null) {
						cell1.setCellValue(record.getR26_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR26_amt_gt24m() != null)
						cell1.setCellValue(record.getR26_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR26_capital_gt24m() != null) {
						cell1.setCellValue(record.getR26_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row26 (R27)
					row = sheet.getRow(26);

					// Column B

					// Column C

					cell1 = row.createCell(2);
					if (record.getR27_amt_6m() != null) {
						cell1.setCellValue(record.getR27_amt_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column E
					cell1 = row.createCell(4);
					if (record.getR27_capital_6m() != null) {
						cell1.setCellValue(record.getR27_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F

					cell1 = row.createCell(5);
					if (record.getR27_amt_6to24m() != null) {
						cell1.setCellValue(record.getR27_amt_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column H
					cell1 = row.createCell(7);
					if (record.getR27_capital_6to24m() != null) {
						cell1.setCellValue(record.getR27_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I

					cell1 = row.createCell(8);
					if (record.getR27_amt_gt24m() != null) {
						cell1.setCellValue(record.getR27_amt_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column K
					cell1 = row.createCell(10);
					if (record.getR27_capital_gt24m() != null) {
						cell1.setCellValue(record.getR27_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row27 (R28)
					row = sheet.getRow(27);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR28_amt_6m() != null)
						cell1.setCellValue(record.getR28_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR28_capital_6m() != null) {
						cell1.setCellValue(record.getR28_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR28_amt_6to24m() != null)
						cell1.setCellValue(record.getR28_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR28_capital_6to24m() != null) {
						cell1.setCellValue(record.getR28_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR28_amt_gt24m() != null)
						cell1.setCellValue(record.getR28_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR28_capital_gt24m() != null) {
						cell1.setCellValue(record.getR28_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row28 (R29)
					row = sheet.getRow(28);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR29_amt_6m() != null)
						cell1.setCellValue(record.getR29_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR29_capital_6m() != null) {
						cell1.setCellValue(record.getR29_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR29_amt_6to24m() != null)
						cell1.setCellValue(record.getR29_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR29_capital_6to24m() != null) {
						cell1.setCellValue(record.getR29_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR29_amt_gt24m() != null)
						cell1.setCellValue(record.getR29_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR29_capital_gt24m() != null) {
						cell1.setCellValue(record.getR29_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row29 (R30)
					row = sheet.getRow(29);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR30_amt_6m() != null)
						cell1.setCellValue(record.getR30_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR30_capital_6m() != null) {
						cell1.setCellValue(record.getR30_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR30_amt_6to24m() != null)
						cell1.setCellValue(record.getR30_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR30_capital_6to24m() != null) {
						cell1.setCellValue(record.getR30_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR30_amt_gt24m() != null)
						cell1.setCellValue(record.getR30_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR30_capital_gt24m() != null) {
						cell1.setCellValue(record.getR30_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row30 (R31)
					row = sheet.getRow(30);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR31_amt_6m() != null)
						cell1.setCellValue(record.getR31_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR31_capital_6m() != null) {
						cell1.setCellValue(record.getR31_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR31_amt_6to24m() != null)
						cell1.setCellValue(record.getR31_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR31_capital_6to24m() != null) {
						cell1.setCellValue(record.getR31_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR31_amt_gt24m() != null)
						cell1.setCellValue(record.getR31_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR31_capital_gt24m() != null) {
						cell1.setCellValue(record.getR31_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row31 (R32)
					row = sheet.getRow(31);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR32_amt_6m() != null)
						cell1.setCellValue(record.getR32_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR32_capital_6m() != null) {
						cell1.setCellValue(record.getR32_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR32_amt_6to24m() != null)
						cell1.setCellValue(record.getR32_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR32_capital_6to24m() != null) {
						cell1.setCellValue(record.getR32_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR32_amt_gt24m() != null)
						cell1.setCellValue(record.getR32_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR32_capital_gt24m() != null) {
						cell1.setCellValue(record.getR32_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row32 (R33)
					row = sheet.getRow(32);

					// Column B

					// Column E
					cell1 = row.createCell(4);
					if (record.getR33_capital_6m() != null) {
						cell1.setCellValue(record.getR33_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR33_capital_6to24m() != null) {
						cell1.setCellValue(record.getR33_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR33_capital_gt24m() != null) {
						cell1.setCellValue(record.getR33_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row34
					// Column K
					row = sheet.getRow(34);
					cell1 = row.createCell(4);
					if (record.getR35_tot_spec_risk_ch() != null) {
						cell1.setCellValue(record.getR35_tot_spec_risk_ch().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column K
				} // end of loop

				// ❌ REMOVE this (do NOT evaluate formulas)
				// workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();

			} else {
				// no data case
			}

			// Write final workbook to output stream
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}
	}

	// Archival
	public List<Object[]> getM_SIRArchival() {
		List<Object[]> archivalList = new ArrayList<>();
		try {
			List<M_SIR_Archival_Summary_Entity> latestArchivalList = BRRS_M_SIR_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_SIR_Archival_Summary_Entity entity : latestArchivalList) {
					archivalList.add(new Object[] { entity.getReportDate(), entity.getReportVersion() });
				}
				System.out.println("Fetched " + archivalList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching Q_staff Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return archivalList;
	}

	/// RESUB VIEW
//		public List<Object[]> getM_SIRResub() {
//		List<Object[]> resubList = new ArrayList<>();
//		try {
//		List<M_SIR_Archival_Summary_Entity> latestArchivalList = 
//				BRRS_M_SIR_Archival_Summary_Repo.getdatabydateListWithVersionAll();
	//
//		if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
//		for (M_SIR_Archival_Summary_Entity entity : latestArchivalList) {
//		Object[] row = new Object[] {
//		entity.getReportDate(),
//		entity.getReportVersion()
//		};
//		resubList.add(row);
//		}
//		System.out.println("Fetched " + resubList.size() + " record(s)");
//		} else {
//		System.out.println("No archival data found.");
//		}
//		} catch (Exception e) {
//		System.err.println("Error fetching M_SIR Resub data: " + e.getMessage());
//		e.printStackTrace();
//		}
//		return resubList;
//		}
	// Archival View
//			public List<Object[]> getM_SIRArchival() {
//				List<Object[]> archivalList = new ArrayList<>();
//
//				try {
//					List<M_SIR_Archival_Summary_Entity> repoData = BRRS_M_SIR_Archival_Summary_Repo
//							.getdatabydateListWithVersionAll();
//
//					if (repoData != null && !repoData.isEmpty()) {
//						for (M_SIR_Archival_Summary_Entity entity : repoData) {
//							Object[] row = new Object[] {
//									entity.getReportDate(), 
//									entity.getReportVersion() 
//							};
//							archivalList.add(row);
//						}
//
//						System.out.println("Fetched " + archivalList.size() + " archival records");
//						M_SIR_Archival_Summary_Entity first = repoData.get(0);
//						System.out.println("Latest archival version: " + first.getReportVersion());
//					} else {
//						System.out.println("No archival data found.");
//					}
//
//				} catch (Exception e) {
//					System.err.println("Error fetching M_SIR Archival data: " + e.getMessage());
//					e.printStackTrace();
//				}
//
//				return archivalList;
//			}

//			// Resubmit the values , latest version and Resub Date
//			public void updateReportReSub(M_SIR_Summary_Entity updatedEntity) {
//				System.out.println("Came to Resub Service");
//				System.out.println("Report Date: " + updatedEntity.getReportDate());
//
//				Date reportDate = updatedEntity.getReportDate();
//				int newVersion = 1;
//
//				try {
//					// Fetch the latest archival version for this report date
//					Optional<M_SIR_Archival_Summary_Entity> latestArchivalOpt = BRRS_M_SIR_Archival_Summary_Repo
//							.getLatestArchivalVersionByDate(reportDate);
//
//					// Determine next version number
//					if (latestArchivalOpt.isPresent()) {
//						M_SIR_Archival_Summary_Entity latestArchival = latestArchivalOpt.get();
//						try {
//							newVersion = Integer.parseInt(latestArchival.getReportVersion()) + 1;
//						} catch (NumberFormatException e) {
//							System.err.println("Invalid version format. Defaulting to version 1");
//							newVersion = 1;
//						}
//					} else {
//						System.out.println("No previous archival found for date: " + reportDate);
//					}
//
//					// Prevent duplicate version number
//					boolean exists = BRRS_M_SIR_Archival_Summary_Repo
//							.findByReportDateAndReportVersion(reportDate, String.valueOf(newVersion))
//							.isPresent();
//
//					if (exists) {
//						throw new RuntimeException("Version " + newVersion + " already exists for report date " + reportDate);
//					}
//
//					// Copy summary entity to archival entity
//					M_SIR_Archival_Summary_Entity archivalEntity = new M_SIR_Archival_Summary_Entity();
//					org.springframework.beans.BeanUtils.copyProperties(updatedEntity, archivalEntity);
//
//					archivalEntity.setReportDate(reportDate);
//					archivalEntity.setReportVersion(String.valueOf(newVersion));
//					archivalEntity.setReportResubDate(new Date());
//
//					System.out.println("Saving new archival version: " + newVersion);
//
//					// Save new version to repository
//					BRRS_M_SIR_Archival_Summary_Repo.save(archivalEntity);
//
//					System.out.println(" Saved archival version successfully: " + newVersion);
//
//				} catch (Exception e) {
//					e.printStackTrace();
//					throw new RuntimeException("Error while creating archival resubmission record", e);
//				}
//			}
//			/// Downloaded for Archival & Resub
//			public byte[] BRRS_M_SIRResubExcel(String filename, String reportId, String fromdate,
//		        String todate, String currency, String dtltype,
//		        String type, String version) throws Exception {
//
//		    logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");
//
//		    if (type.equals("RESUB") & version != null) {
//		       
//		    }
//
//		    List<M_SIR_Archival_Summary_Entity> dataList1 =
//		        BRRS_M_SIR_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
//
//		    if (dataList1.isEmpty()) {
//		        logger.warn("Service: No data found for M_SIR report. Returning empty result.");
//		        return new byte[0];
//		    }
//
//				String templateDir = env.getProperty("output.exportpathtemp");
//				String templateFileName = filename;
//				System.out.println(filename);
//				Path templatePath = Paths.get(templateDir, templateFileName);
//				System.out.println(templatePath);
//
//				logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());
//
//				if (!Files.exists(templatePath)) {
//					// This specific exception will be caught by the controller.
//					throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
//				}
//				if (!Files.isReadable(templatePath)) {
//					// A specific exception for permission errors.
//					throw new SecurityException(
//							"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
//				}
//
//				// This try-with-resources block is perfect. It guarantees all resources are
//				// closed automatically.
//				try (InputStream templateInputStream = Files.newInputStream(templatePath);
//						Workbook workbook = WorkbookFactory.create(templateInputStream);
//						ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//
//
//					Sheet sheet = workbook.getSheetAt(0);
//
//					// --- Style Definitions ---
//					CreationHelper createHelper = workbook.getCreationHelper();
//
//					CellStyle dateStyle = workbook.createCellStyle();
//					dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
//					dateStyle.setBorderBottom(BorderStyle.THIN);
//					dateStyle.setBorderTop(BorderStyle.THIN);
//					dateStyle.setBorderLeft(BorderStyle.THIN);
//					dateStyle.setBorderRight(BorderStyle.THIN);
//
//					CellStyle textStyle = workbook.createCellStyle();
//					textStyle.setBorderBottom(BorderStyle.THIN);
//					textStyle.setBorderTop(BorderStyle.THIN);
//					textStyle.setBorderLeft(BorderStyle.THIN);
//					textStyle.setBorderRight(BorderStyle.THIN);
//					
//					// Create the font
//					Font font = workbook.createFont();
//					font.setFontHeightInPoints((short)8); // size 8
//					font.setFontName("calibriLight");    
//
//					CellStyle numberStyle = workbook.createCellStyle();
//					//numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
//					numberStyle.setBorderBottom(BorderStyle.THIN);
//					numberStyle.setBorderTop(BorderStyle.THIN);
//					numberStyle.setBorderLeft(BorderStyle.THIN);
//					numberStyle.setBorderRight(BorderStyle.THIN);
//					numberStyle.setFont(font);
//					// --- End of Style Definitions ---
//					
//					//  Create pure light green style (Excel highlight green)
//					XSSFCellStyle greenStyle = (XSSFCellStyle) workbook.createCellStyle();
//					greenStyle.cloneStyleFrom(textStyle);
//
//					byte[] rgb = new byte[]{(byte)146, (byte)208, (byte)80}; // exact Excel light green
//					XSSFColor green = new XSSFColor(rgb, null);
//
//					greenStyle.setFillForegroundColor(green);
//					greenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//
//greenStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
//					CellStyle percentStyle = workbook.createCellStyle();
//					percentStyle.cloneStyleFrom(numberStyle);
//					percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
//					percentStyle.setAlignment(HorizontalAlignment.RIGHT);
//					int startRow = 11;
//
//					if (!dataList1.isEmpty()) {
//						for (int i = 0; i < dataList1.size(); i++) {
//							M_SIR_Archival_Summary_Entity record = dataList1.get(i);
//							System.out.println("rownumber="+startRow + i);
//							Row row = sheet.getRow(startRow + i);
//							if (row == null) {
//								row = sheet.createRow(startRow + i);
//							}
//		//----------
//							
//							//row11
//							// Column C 
//							row=sheet.getRow(11);
//						
//							
//							Cell cell1 = row.createCell(2);
//								if (record.getR12_amt_6m() != null) {
//									cell1.setCellValue(record.getR12_amt_6m().doubleValue());
//									cell1.setCellStyle(greenStyle);
//								} else {
//									cell1.setCellValue("");
//									cell1.setCellStyle(textStyle);
//								}
//							//row11
//							// Column E 
//							 cell1 = row.createCell(4);
//							if (record.getR12_capital_6m() != null) {
//								cell1.setCellValue(record.getR12_capital_6m().doubleValue());
//								cell1.setCellStyle(greenStyle);
//							} else {
//								cell1.setCellValue("");
//								cell1.setCellStyle(textStyle);
//							}
//							//row11
//							// Column F 
//							
//
//
//								cell1 = row.createCell(5);
//								if (record.getR12_amt_6to24m() != null) {
//									cell1.setCellValue(record.getR12_amt_6to24m().doubleValue());
//									cell1.setCellStyle(greenStyle);
//								} else {
//									cell1.setCellValue("");
//									cell1.setCellStyle(textStyle);
//								}
//							//row11
//							// Column H
//							 cell1 = row.createCell(7);
//							if (record.getR12_capital_6to24m() != null) {
//								cell1.setCellValue(record.getR12_capital_6to24m().doubleValue());
//								cell1.setCellStyle(greenStyle);
//							} else {
//								cell1.setCellValue("");
//								cell1.setCellStyle(textStyle);
//							}					
//							//row11
//							// Column I 
//						
//
//							cell1 = row.createCell(8);
//							if (record.getR12_amt_gt24m() != null) {
//								cell1.setCellValue(record.getR12_amt_gt24m().doubleValue());
//								cell1.setCellStyle(greenStyle);
//							} else {
//								cell1.setCellValue("");
//								cell1.setCellStyle(textStyle);
//							}
//							
//							//row11
//							// Column K
//							 cell1 = row.createCell(10);
//							if (record.getR12_capital_gt24m() != null) {
//								cell1.setCellValue(record.getR12_capital_gt24m().doubleValue());
//								cell1.setCellStyle(greenStyle);
//							} else {
//								cell1.setCellValue("");
//								cell1.setCellStyle(textStyle);
//							}												
//		//-------------
//
//							//row12
//							// Column b
//							row=sheet.getRow(12);
//							
//							//row12
//							// Column C 
//							
//							 cell1 = row.getCell(2);
//							if (record.getR13_amt_6m() != null) {
//								cell1.setCellValue(record.getR13_amt_6m().doubleValue());
//								
//							} else {
//								cell1.setCellValue("");
//								cell1.setCellStyle(textStyle);
//							}
//							
//							 cell1 = row.createCell(4);
//								if (record.getR13_capital_6m() != null) {
//									cell1.setCellValue(record.getR13_capital_6m().doubleValue());
//									cell1.setCellStyle(greenStyle);
//								} else {
//									cell1.setCellValue("");
//									cell1.setCellStyle(textStyle);
//								}
//							
//							// Column F 
//							 cell1 = row.getCell(5);
//							if (record.getR13_amt_6to24m() != null) {
//								cell1.setCellValue(record.getR13_amt_6to24m().doubleValue());
//								
//							} else {
//								cell1.setCellValue("");
//								cell1.setCellStyle(textStyle);
//							}					
//						
//							 cell1 = row.createCell(7);
//								if (record.getR13_capital_6to24m() != null) {
//									cell1.setCellValue(record.getR13_capital_6to24m().doubleValue());
//									cell1.setCellStyle(greenStyle);
//								} else {
//									cell1.setCellValue("");
//									cell1.setCellStyle(textStyle);
//								}				
//						
//							// Column I 
//							 cell1 = row.getCell(8);
//							if (record.getR13_amt_gt24m() != null) {
//								cell1.setCellValue(record.getR13_amt_gt24m().doubleValue());
//						
//							} else {
//								cell1.setCellValue("");
//								cell1.setCellStyle(textStyle);
//							}					
//							 cell1 = row.createCell(10);
//								if (record.getR13_capital_gt24m() != null) {
//									cell1.setCellValue(record.getR13_capital_gt24m().doubleValue());
//									cell1.setCellStyle(greenStyle);
//								} else {
//									cell1.setCellValue("");
//									cell1.setCellStyle(textStyle);
//								}
//
//
//								//row13
//								// Column b
//								row=sheet.getRow(13);
//								
//								//row12
//								// Column C 
//								
//								 cell1 = row.getCell(2);
//								if (record.getR14_amt_6m() != null) {
//									cell1.setCellValue(record.getR14_amt_6m().doubleValue());
//									
//								} else {
//									cell1.setCellValue("");
//									cell1.setCellStyle(textStyle);
//								}
//								
//								 cell1 = row.createCell(4);
//									if (record.getR14_capital_6m() != null) {
//										cell1.setCellValue(record.getR14_capital_6m().doubleValue());
//										cell1.setCellStyle(greenStyle);
//									} else {
//										cell1.setCellValue("");
//										cell1.setCellStyle(textStyle);
//									}
//								
//								// Column F 
//								 cell1 = row.getCell(5);
//								if (record.getR14_amt_6to24m() != null) {
//									cell1.setCellValue(record.getR14_amt_6to24m().doubleValue());
//									
//								} else {
//									cell1.setCellValue("");
//									cell1.setCellStyle(textStyle);
//								}					
//							
//								 cell1 = row.createCell(7);
//									if (record.getR14_capital_6to24m() != null) {
//										cell1.setCellValue(record.getR14_capital_6to24m().doubleValue());
//										cell1.setCellStyle(greenStyle);
//									} else {
//										cell1.setCellValue("");
//										cell1.setCellStyle(textStyle);
//									}				
//							
//								// Column I 
//								 cell1 = row.getCell(8);
//								if (record.getR14_amt_gt24m() != null) {
//									cell1.setCellValue(record.getR14_amt_gt24m().doubleValue());
//							
//								} else {
//									cell1.setCellValue("");
//									cell1.setCellStyle(textStyle);
//								}					
//								 cell1 = row.createCell(10);
//									if (record.getR14_capital_gt24m() != null) {
//										cell1.setCellValue(record.getR14_capital_gt24m().doubleValue());
//										cell1.setCellStyle(greenStyle);
//									} else {
//										cell1.setCellValue("");
//										cell1.setCellStyle(textStyle);
//									}
//
//
//									//row14
//									// Column b
//									row=sheet.getRow(14);
//									
//									//row12
//									// Column C 
//									
//									 cell1 = row.getCell(2);
//									if (record.getR15_amt_6m() != null) {
//										cell1.setCellValue(record.getR15_amt_6m().doubleValue());
//										
//									} else {
//										cell1.setCellValue("");
//										cell1.setCellStyle(textStyle);
//									}
//									
//									 cell1 = row.createCell(4);
//										if (record.getR15_capital_6m() != null) {
//											cell1.setCellValue(record.getR15_capital_6m().doubleValue());
//											cell1.setCellStyle(greenStyle);
//										} else {
//											cell1.setCellValue("");
//											cell1.setCellStyle(textStyle);
//										}
//									
//									// Column F 
//									 cell1 = row.getCell(5);
//									if (record.getR15_amt_6to24m() != null) {
//										cell1.setCellValue(record.getR15_amt_6to24m().doubleValue());
//										
//									} else {
//										cell1.setCellValue("");
//										cell1.setCellStyle(textStyle);
//									}					
//								
//									 cell1 = row.createCell(7);
//										if (record.getR15_capital_6to24m() != null) {
//											cell1.setCellValue(record.getR15_capital_6to24m().doubleValue());
//											cell1.setCellStyle(greenStyle);
//										} else {
//											cell1.setCellValue("");
//											cell1.setCellStyle(textStyle);
//										}				
//								
//									// Column I 
//									 cell1 = row.getCell(8);
//									if (record.getR15_amt_gt24m() != null) {
//										cell1.setCellValue(record.getR15_amt_gt24m().doubleValue());
//								
//									} else {
//										cell1.setCellValue("");
//										cell1.setCellStyle(textStyle);
//									}					
//									 cell1 = row.createCell(10);
//										if (record.getR15_capital_gt24m() != null) {
//											cell1.setCellValue(record.getR15_capital_gt24m().doubleValue());
//											cell1.setCellStyle(greenStyle);
//										} else {
//											cell1.setCellValue("");
//											cell1.setCellStyle(textStyle);
//										}
////						//row15  (R16)
//										// Column B
//										row = sheet.getRow(15);
//										cell1 = row.getCell(1);
//										
//
//										// Column C 
//										cell1 = row.getCell(2);
//										if (record.getR16_amt_6m() != null) {
//										    cell1.setCellValue(record.getR16_amt_6m().doubleValue());
//										} else {
//										    cell1.setCellValue("");
//										    cell1.setCellStyle(textStyle);
//										}
//
//										// Column E
//										cell1 = row.createCell(4);
//										if (record.getR16_capital_6m() != null) {
//										    cell1.setCellValue(record.getR16_capital_6m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else {
//										    cell1.setCellValue("");
//										    cell1.setCellStyle(textStyle);
//										}
//
//										// Column F 
//										cell1 = row.getCell(5);
//										if (record.getR16_amt_6to24m() != null) {
//										    cell1.setCellValue(record.getR16_amt_6to24m().doubleValue());
//										} else {
//										    cell1.setCellValue("");
//										    cell1.setCellStyle(textStyle);
//										}
//
//										// Column H
//										cell1 = row.createCell(7);
//										if (record.getR16_capital_6to24m() != null) {
//										    cell1.setCellValue(record.getR16_capital_6to24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else {
//										    cell1.setCellValue("");
//										    cell1.setCellStyle(textStyle);
//										}
//
//										// Column I 
//										cell1 = row.getCell(8);
//										if (record.getR16_amt_gt24m() != null) {
//										    cell1.setCellValue(record.getR16_amt_gt24m().doubleValue());
//										} else {
//										    cell1.setCellValue("");
//										    cell1.setCellStyle(textStyle);
//										}
//
//										// Column K
//										cell1 = row.createCell(10);
//										if (record.getR16_capital_gt24m() != null) {
//										    cell1.setCellValue(record.getR16_capital_gt24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else {
//										    cell1.setCellValue("");
//										    cell1.setCellStyle(textStyle);
//										}
//										//row16  (R17)
//										row = sheet.getRow(16);
//
//										// Column B
//										
//										// Column C
//										cell1 = row.getCell(2);
//										if (record.getR17_amt_6m() != null) cell1.setCellValue(record.getR17_amt_6m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column E
//										cell1 = row.createCell(4);
//										if (record.getR17_capital_6m() != null) {
//										    cell1.setCellValue(record.getR17_capital_6m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column F
//										cell1 = row.getCell(5);
//										if (record.getR17_amt_6to24m() != null) cell1.setCellValue(record.getR17_amt_6to24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column H
//										cell1 = row.createCell(7);
//										if (record.getR17_capital_6to24m() != null) {
//										    cell1.setCellValue(record.getR17_capital_6to24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column I
//										cell1 = row.getCell(8);
//										if (record.getR17_amt_gt24m() != null) cell1.setCellValue(record.getR17_amt_gt24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column K
//										cell1 = row.createCell(10);
//										if (record.getR17_capital_gt24m() != null) {
//										    cell1.setCellValue(record.getR17_capital_gt24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//										//row17  (R18)
//										row = sheet.getRow(17);
//
//										// Column B
//										
//										// Column C
//										
//										 cell1 = row.createCell(2);
//										if (record.getR18_amt_6m() != null) {
//											cell1.setCellValue(record.getR18_amt_6m().doubleValue());
//											 cell1.setCellStyle(greenStyle);
//										} else {
//											cell1.setCellValue("");
//											cell1.setCellStyle(textStyle);
//										}
//										// Column E
//										cell1 = row.createCell(4);
//										if (record.getR18_capital_6m() != null) {
//										    cell1.setCellValue(record.getR18_capital_6m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column F
//										
//										 cell1 = row.createCell(5);
//											if (record.getR18_amt_6to24m() != null) {
//												cell1.setCellValue(record.getR18_amt_6to24m().doubleValue());
//												 cell1.setCellStyle(greenStyle);
//											} else {
//												cell1.setCellValue("");
//												cell1.setCellStyle(textStyle);
//											}
//										// Column H
//										cell1 = row.createCell(7);
//										if (record.getR18_capital_6to24m() != null) {
//										    cell1.setCellValue(record.getR18_capital_6to24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column I
//										
//										 cell1 = row.createCell(8);
//											if (record.getR18_amt_gt24m() != null) {
//												cell1.setCellValue(record.getR18_amt_gt24m().doubleValue());
//												 cell1.setCellStyle(greenStyle);
//											} else {
//												cell1.setCellValue("");
//												cell1.setCellStyle(textStyle);
//											}
//										// Column K
//										cell1 = row.createCell(10);
//										if (record.getR18_capital_gt24m() != null) {
//										    cell1.setCellValue(record.getR18_capital_gt24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//										//row18  (R19)
//										row = sheet.getRow(18);
//
//										// Column B
//										
//										// Column C
//										cell1 = row.getCell(2);
//										if (record.getR19_amt_6m() != null) cell1.setCellValue(record.getR19_amt_6m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column E
//										cell1 = row.createCell(4);
//										if (record.getR19_capital_6m() != null) {
//										    cell1.setCellValue(record.getR19_capital_6m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column F
//										cell1 = row.getCell(5);
//										if (record.getR19_amt_6to24m() != null) cell1.setCellValue(record.getR19_amt_6to24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column H
//										cell1 = row.createCell(7);
//										if (record.getR19_capital_6to24m() != null) {
//										    cell1.setCellValue(record.getR19_capital_6to24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column I
//										cell1 = row.getCell(8);
//										if (record.getR19_amt_gt24m() != null) cell1.setCellValue(record.getR19_amt_gt24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column K
//										cell1 = row.createCell(10);
//										if (record.getR19_capital_gt24m() != null) {
//										    cell1.setCellValue(record.getR19_capital_gt24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//										//row19  (R20)
//										row = sheet.getRow(19);
//
//										// Column B
//										
//										// Column C
//										cell1 = row.getCell(2);
//										if (record.getR20_amt_6m() != null) cell1.setCellValue(record.getR20_amt_6m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column E
//										cell1 = row.createCell(4);
//										if (record.getR20_capital_6m() != null) {
//										    cell1.setCellValue(record.getR20_capital_6m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column F
//										cell1 = row.getCell(5);
//										if (record.getR20_amt_6to24m() != null) cell1.setCellValue(record.getR20_amt_6to24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column H
//										cell1 = row.createCell(7);
//										if (record.getR20_capital_6to24m() != null) {
//										    cell1.setCellValue(record.getR20_capital_6to24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column I
//										cell1 = row.getCell(8);
//										if (record.getR20_amt_gt24m() != null) cell1.setCellValue(record.getR20_amt_gt24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column K
//										cell1 = row.createCell(10);
//										if (record.getR20_capital_gt24m() != null) {
//										    cell1.setCellValue(record.getR20_capital_gt24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//										//row20  (R21)
//										row = sheet.getRow(20);
//
//										// Column B
//									
//										// Column C
//										cell1 = row.getCell(2);
//										if (record.getR21_amt_6m() != null) cell1.setCellValue(record.getR21_amt_6m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column E
//										cell1 = row.createCell(4);
//										if (record.getR21_capital_6m() != null) {
//										    cell1.setCellValue(record.getR21_capital_6m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column F
//										cell1 = row.getCell(5);
//										if (record.getR21_amt_6to24m() != null) cell1.setCellValue(record.getR21_amt_6to24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column H
//										cell1 = row.createCell(7);
//										if (record.getR21_capital_6to24m() != null) {
//										    cell1.setCellValue(record.getR21_capital_6to24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column I
//										cell1 = row.getCell(8);
//										if (record.getR21_amt_gt24m() != null) cell1.setCellValue(record.getR21_amt_gt24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column K
//										cell1 = row.createCell(10);
//										if (record.getR21_capital_gt24m() != null) {
//										    cell1.setCellValue(record.getR21_capital_gt24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//										//row21  (R22)
//										row = sheet.getRow(21);
//
//										// Column B
//										
//										// Column C
//										cell1 = row.getCell(2);
//										if (record.getR22_amt_6m() != null) cell1.setCellValue(record.getR22_amt_6m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column E
//										cell1 = row.createCell(4);
//										if (record.getR22_capital_6m() != null) {
//										    cell1.setCellValue(record.getR22_capital_6m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column F
//										cell1 = row.getCell(5);
//										if (record.getR22_amt_6to24m() != null) cell1.setCellValue(record.getR22_amt_6to24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column H
//										cell1 = row.createCell(7);
//										if (record.getR22_capital_6to24m() != null) {
//										    cell1.setCellValue(record.getR22_capital_6to24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column I
//										cell1 = row.getCell(8);
//										if (record.getR22_amt_gt24m() != null) cell1.setCellValue(record.getR22_amt_gt24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column K
//										cell1 = row.createCell(10);
//										if (record.getR22_capital_gt24m() != null) {
//										    cell1.setCellValue(record.getR22_capital_gt24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//										//row22  (R23)
//										row = sheet.getRow(22);
//
//										// Column B
//										
//										// Column C
//										cell1 = row.getCell(2);
//										if (record.getR23_amt_6m() != null) cell1.setCellValue(record.getR23_amt_6m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column E
//										cell1 = row.createCell(4);
//										if (record.getR23_capital_6m() != null) {
//										    cell1.setCellValue(record.getR23_capital_6m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column F
//										cell1 = row.getCell(5);
//										if (record.getR23_amt_6to24m() != null) cell1.setCellValue(record.getR23_amt_6to24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column H
//										cell1 = row.createCell(7);
//										if (record.getR23_capital_6to24m() != null) {
//										    cell1.setCellValue(record.getR23_capital_6to24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column I
//										cell1 = row.getCell(8);
//										if (record.getR23_amt_gt24m() != null) cell1.setCellValue(record.getR23_amt_gt24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column K
//										cell1 = row.createCell(10);
//										if (record.getR23_capital_gt24m() != null) {
//										    cell1.setCellValue(record.getR23_capital_gt24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//										//row23  (R24)
//										row = sheet.getRow(23);
//
//										// Column B
//										
//										// Column C
//										cell1 = row.getCell(2);
//										if (record.getR24_amt_6m() != null) cell1.setCellValue(record.getR24_amt_6m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column E
//										cell1 = row.createCell(4);
//										if (record.getR24_capital_6m() != null) {
//										    cell1.setCellValue(record.getR24_capital_6m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column F
//										cell1 = row.getCell(5);
//										if (record.getR24_amt_6to24m() != null) cell1.setCellValue(record.getR24_amt_6to24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column H
//										cell1 = row.createCell(7);
//										if (record.getR24_capital_6to24m() != null) {
//										    cell1.setCellValue(record.getR24_capital_6to24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column I
//										cell1 = row.getCell(8);
//										if (record.getR24_amt_gt24m() != null) cell1.setCellValue(record.getR24_amt_gt24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column K
//										cell1 = row.createCell(10);
//										if (record.getR24_capital_gt24m() != null) {
//										    cell1.setCellValue(record.getR24_capital_gt24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//										//row24  (R25)
//										row = sheet.getRow(24);
//
//										
//										// Column C
//										cell1 = row.getCell(2);
//										if (record.getR25_amt_6m() != null) cell1.setCellValue(record.getR25_amt_6m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column E
//										cell1 = row.createCell(4);
//										if (record.getR25_capital_6m() != null) {
//										    cell1.setCellValue(record.getR25_capital_6m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column F
//										cell1 = row.getCell(5);
//										if (record.getR25_amt_6to24m() != null) cell1.setCellValue(record.getR25_amt_6to24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column H
//										cell1 = row.createCell(7);
//										if (record.getR25_capital_6to24m() != null) {
//										    cell1.setCellValue(record.getR25_capital_6to24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column I
//										cell1 = row.getCell(8);
//										if (record.getR25_amt_gt24m() != null) cell1.setCellValue(record.getR25_amt_gt24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column K
//										cell1 = row.createCell(10);
//										if (record.getR25_capital_gt24m() != null) {
//										    cell1.setCellValue(record.getR25_capital_gt24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//										//row25  (R26)
//										row = sheet.getRow(25);
//
//										// Column B
//										
//										// Column C
//										cell1 = row.getCell(2);
//										if (record.getR26_amt_6m() != null) cell1.setCellValue(record.getR26_amt_6m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column E
//										cell1 = row.createCell(4);
//										if (record.getR26_capital_6m() != null) {
//										    cell1.setCellValue(record.getR26_capital_6m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column F
//										cell1 = row.getCell(5);
//										if (record.getR26_amt_6to24m() != null) cell1.setCellValue(record.getR26_amt_6to24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column H
//										cell1 = row.createCell(7);
//										if (record.getR26_capital_6to24m() != null) {
//										    cell1.setCellValue(record.getR26_capital_6to24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column I
//										cell1 = row.getCell(8);
//										if (record.getR26_amt_gt24m() != null) cell1.setCellValue(record.getR26_amt_gt24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column K
//										cell1 = row.createCell(10);
//										if (record.getR26_capital_gt24m() != null) {
//										    cell1.setCellValue(record.getR26_capital_gt24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//										//row26  (R27)
//										row = sheet.getRow(26);
//
//										// Column B
//										
//										// Column C
//										
//										 cell1 = row.createCell(2);
//											if (record.getR27_amt_6m() != null) {
//												cell1.setCellValue(record.getR27_amt_6m().doubleValue());
//												 cell1.setCellStyle(greenStyle);
//											} else {
//												cell1.setCellValue("");
//												cell1.setCellStyle(textStyle);
//											}
//										// Column E
//										cell1 = row.createCell(4);
//										if (record.getR27_capital_6m() != null) {
//										    cell1.setCellValue(record.getR27_capital_6m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column F
//										
//										 cell1 = row.createCell(5);
//											if (record.getR27_amt_6to24m() != null) {
//												cell1.setCellValue(record.getR27_amt_6to24m().doubleValue());
//												 cell1.setCellStyle(greenStyle);
//											} else {
//												cell1.setCellValue("");
//												cell1.setCellStyle(textStyle);
//											}
//										// Column H
//										cell1 = row.createCell(7);
//										if (record.getR27_capital_6to24m() != null) {
//										    cell1.setCellValue(record.getR27_capital_6to24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column I
//										
//										
//										 cell1 = row.createCell(8);
//											if (record.getR27_amt_gt24m() != null) {
//												cell1.setCellValue(record.getR27_amt_gt24m().doubleValue());
//												 cell1.setCellStyle(greenStyle);
//											} else {
//												cell1.setCellValue("");
//												cell1.setCellStyle(textStyle);
//											}
//										// Column K
//										cell1 = row.createCell(10);
//										if (record.getR27_capital_gt24m() != null) {
//										    cell1.setCellValue(record.getR27_capital_gt24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//										//row27  (R28)
//										row = sheet.getRow(27);
//
//										// Column B
//									
//										// Column C
//										cell1 = row.getCell(2);
//										if (record.getR28_amt_6m() != null) cell1.setCellValue(record.getR28_amt_6m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column E
//										cell1 = row.createCell(4);
//										if (record.getR28_capital_6m() != null) {
//										    cell1.setCellValue(record.getR28_capital_6m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column F
//										cell1 = row.getCell(5);
//										if (record.getR28_amt_6to24m() != null) cell1.setCellValue(record.getR28_amt_6to24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column H
//										cell1 = row.createCell(7);
//										if (record.getR28_capital_6to24m() != null) {
//										    cell1.setCellValue(record.getR28_capital_6to24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column I
//										cell1 = row.getCell(8);
//										if (record.getR28_amt_gt24m() != null) cell1.setCellValue(record.getR28_amt_gt24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column K
//										cell1 = row.createCell(10);
//										if (record.getR28_capital_gt24m() != null) {
//										    cell1.setCellValue(record.getR28_capital_gt24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//										//row28  (R29)
//										row = sheet.getRow(28);
//
//										// Column B
//										
//										// Column C
//										cell1 = row.getCell(2);
//										if (record.getR29_amt_6m() != null) cell1.setCellValue(record.getR29_amt_6m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column E
//										cell1 = row.createCell(4);
//										if (record.getR29_capital_6m() != null) {
//										    cell1.setCellValue(record.getR29_capital_6m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column F
//										cell1 = row.getCell(5);
//										if (record.getR29_amt_6to24m() != null) cell1.setCellValue(record.getR29_amt_6to24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column H
//										cell1 = row.createCell(7);
//										if (record.getR29_capital_6to24m() != null) {
//										    cell1.setCellValue(record.getR29_capital_6to24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column I
//										cell1 = row.getCell(8);
//										if (record.getR29_amt_gt24m() != null) cell1.setCellValue(record.getR29_amt_gt24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column K
//										cell1 = row.createCell(10);
//										if (record.getR29_capital_gt24m() != null) {
//										    cell1.setCellValue(record.getR29_capital_gt24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//										//row29  (R30)
//										row = sheet.getRow(29);
//
//										// Column B
//										
//										// Column C
//										cell1 = row.getCell(2);
//										if (record.getR30_amt_6m() != null) cell1.setCellValue(record.getR30_amt_6m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column E
//										cell1 = row.createCell(4);
//										if (record.getR30_capital_6m() != null) {
//										    cell1.setCellValue(record.getR30_capital_6m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column F
//										cell1 = row.getCell(5);
//										if (record.getR30_amt_6to24m() != null) cell1.setCellValue(record.getR30_amt_6to24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column H
//										cell1 = row.createCell(7);
//										if (record.getR30_capital_6to24m() != null) {
//										    cell1.setCellValue(record.getR30_capital_6to24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column I
//										cell1 = row.getCell(8);
//										if (record.getR30_amt_gt24m() != null) cell1.setCellValue(record.getR30_amt_gt24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column K
//										cell1 = row.createCell(10);
//										if (record.getR30_capital_gt24m() != null) {
//										    cell1.setCellValue(record.getR30_capital_gt24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//										//row30  (R31)
//										row = sheet.getRow(30);
//
//										// Column B
//										
//										// Column C
//										cell1 = row.getCell(2);
//										if (record.getR31_amt_6m() != null) cell1.setCellValue(record.getR31_amt_6m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column E
//										cell1 = row.createCell(4);
//										if (record.getR31_capital_6m() != null) {
//										    cell1.setCellValue(record.getR31_capital_6m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column F
//										cell1 = row.getCell(5);
//										if (record.getR31_amt_6to24m() != null) cell1.setCellValue(record.getR31_amt_6to24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column H
//										cell1 = row.createCell(7);
//										if (record.getR31_capital_6to24m() != null) {
//										    cell1.setCellValue(record.getR31_capital_6to24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column I
//										cell1 = row.getCell(8);
//										if (record.getR31_amt_gt24m() != null) cell1.setCellValue(record.getR31_amt_gt24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column K
//										cell1 = row.createCell(10);
//										if (record.getR31_capital_gt24m() != null) {
//										    cell1.setCellValue(record.getR31_capital_gt24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//										//row31  (R32)
//										row = sheet.getRow(31);
//
//										// Column B
//										
//										// Column C
//										cell1 = row.getCell(2);
//										if (record.getR32_amt_6m() != null) cell1.setCellValue(record.getR32_amt_6m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column E
//										cell1 = row.createCell(4);
//										if (record.getR32_capital_6m() != null) {
//										    cell1.setCellValue(record.getR32_capital_6m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column F
//										cell1 = row.getCell(5);
//										if (record.getR32_amt_6to24m() != null) cell1.setCellValue(record.getR32_amt_6to24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column H
//										cell1 = row.createCell(7);
//										if (record.getR32_capital_6to24m() != null) {
//										    cell1.setCellValue(record.getR32_capital_6to24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column I
//										cell1 = row.getCell(8);
//										if (record.getR32_amt_gt24m() != null) cell1.setCellValue(record.getR32_amt_gt24m().doubleValue());
//										else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//										// Column K
//										cell1 = row.createCell(10);
//										if (record.getR32_capital_gt24m() != null) {
//										    cell1.setCellValue(record.getR32_capital_gt24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//										//row32  (R33)
//										row = sheet.getRow(32);
//
//										// Column B
//										
//									
//										// Column E
//										cell1 = row.createCell(4);
//										if (record.getR33_capital_6m() != null) {
//										    cell1.setCellValue(record.getR33_capital_6m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//								
//
//										// Column H
//										cell1 = row.createCell(7);
//										if (record.getR33_capital_6to24m() != null) {
//										    cell1.setCellValue(record.getR33_capital_6to24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//
//								
//
//										// Column K
//										cell1 = row.createCell(10);
//										if (record.getR33_capital_gt24m() != null) {
//										    cell1.setCellValue(record.getR33_capital_gt24m().doubleValue());
//										    cell1.setCellStyle(greenStyle);
//										} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
//										//row34
//										//Column K
//										row=sheet.getRow(34);
//										cell1 = row.createCell(4);
//										if (record.getR35_tot_spec_risk_ch() != null) {
//										cell1.setCellValue(record.getR35_tot_spec_risk_ch().doubleValue());
//										cell1.setCellStyle(greenStyle);} else {
//										cell1.setCellValue("");
//										cell1.setCellStyle(textStyle);
//										}
//							// Column K
//						}  // end of loop
//
//						// ❌ REMOVE this (do NOT evaluate formulas)
//						// workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
//
//						} else {
//						    // no data case
//						}
//
//						// Write final workbook to output stream
//						workbook.write(out);
//
//						logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
//
//						return out.toByteArray();
//						}
//		}

	public byte[] BRRS_M_SIREmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		Date reportDate = dateformat.parse(todate);
		if (type.equals("email") & version != null) {

		}
		List<M_SIR_Summary_Entity> dataList = BRRS_M_SIR_Summary_Repo.getdatabydateList(reportDate);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found forM_SIR report. Returning empty result.");
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
			// Create pure light green style (Excel highlight green)
			XSSFCellStyle greenStyle = (XSSFCellStyle) workbook.createCellStyle();
			greenStyle.cloneStyleFrom(textStyle);

			byte[] rgb = new byte[] { (byte) 146, (byte) 208, (byte) 80 }; // exact Excel light green
			XSSFColor green = new XSSFColor(rgb, null);

			greenStyle.setFillForegroundColor(green);
			greenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			greenStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SIR_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// ----------

					// row11
					// Column C
					row = sheet.getRow(11);

					Cell cell1 = row.createCell(2);
					if (record.getR12_amt_6m() != null) {
						cell1.setCellValue(record.getR12_amt_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.createCell(4);
					if (record.getR12_capital_6m() != null) {
						cell1.setCellValue(record.getR12_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F

					cell1 = row.createCell(5);
					if (record.getR12_amt_6to24m() != null) {
						cell1.setCellValue(record.getR12_amt_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column H
					cell1 = row.createCell(7);
					if (record.getR12_capital_6to24m() != null) {
						cell1.setCellValue(record.getR12_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column I

					cell1 = row.createCell(8);
					if (record.getR12_amt_gt24m() != null) {
						cell1.setCellValue(record.getR12_amt_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row11
					// Column K
					cell1 = row.createCell(10);
					if (record.getR12_capital_gt24m() != null) {
						cell1.setCellValue(record.getR12_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// -------------

					// row12
					// Column b
					row = sheet.getRow(12);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR13_amt_6m() != null) {
						cell1.setCellValue(record.getR13_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR13_capital_6m() != null) {
						cell1.setCellValue(record.getR13_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR13_amt_6to24m() != null) {
						cell1.setCellValue(record.getR13_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR13_capital_6to24m() != null) {
						cell1.setCellValue(record.getR13_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR13_amt_gt24m() != null) {
						cell1.setCellValue(record.getR13_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR13_capital_gt24m() != null) {
						cell1.setCellValue(record.getR13_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row13
					// Column b
					row = sheet.getRow(13);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR14_amt_6m() != null) {
						cell1.setCellValue(record.getR14_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR14_capital_6m() != null) {
						cell1.setCellValue(record.getR14_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR14_amt_6to24m() != null) {
						cell1.setCellValue(record.getR14_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR14_capital_6to24m() != null) {
						cell1.setCellValue(record.getR14_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR14_amt_gt24m() != null) {
						cell1.setCellValue(record.getR14_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR14_capital_gt24m() != null) {
						cell1.setCellValue(record.getR14_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row14
					// Column b
					row = sheet.getRow(14);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR15_amt_6m() != null) {
						cell1.setCellValue(record.getR15_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR15_capital_6m() != null) {
						cell1.setCellValue(record.getR15_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR15_amt_6to24m() != null) {
						cell1.setCellValue(record.getR15_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR15_capital_6to24m() != null) {
						cell1.setCellValue(record.getR15_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR15_amt_gt24m() != null) {
						cell1.setCellValue(record.getR15_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR15_capital_gt24m() != null) {
						cell1.setCellValue(record.getR15_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
//					//row15  (R16)
					// Column B
					row = sheet.getRow(15);
					cell1 = row.getCell(1);

					// Column C
					cell1 = row.getCell(2);
					if (record.getR16_amt_6m() != null) {
						cell1.setCellValue(record.getR16_amt_6m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR16_capital_6m() != null) {
						cell1.setCellValue(record.getR16_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR16_amt_6to24m() != null) {
						cell1.setCellValue(record.getR16_amt_6to24m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR16_capital_6to24m() != null) {
						cell1.setCellValue(record.getR16_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR16_amt_gt24m() != null) {
						cell1.setCellValue(record.getR16_amt_gt24m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR16_capital_gt24m() != null) {
						cell1.setCellValue(record.getR16_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row16 (R17)
					row = sheet.getRow(16);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR17_amt_6m() != null)
						cell1.setCellValue(record.getR17_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR17_capital_6m() != null) {
						cell1.setCellValue(record.getR17_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR17_amt_6to24m() != null)
						cell1.setCellValue(record.getR17_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR17_capital_6to24m() != null) {
						cell1.setCellValue(record.getR17_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR17_amt_gt24m() != null)
						cell1.setCellValue(record.getR17_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR17_capital_gt24m() != null) {
						cell1.setCellValue(record.getR17_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row17 (R18)
					row = sheet.getRow(17);

					// Column B

					// Column C

					cell1 = row.createCell(2);
					if (record.getR18_amt_6m() != null) {
						cell1.setCellValue(record.getR18_amt_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column E
					cell1 = row.createCell(4);
					if (record.getR18_capital_6m() != null) {
						cell1.setCellValue(record.getR18_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F

					cell1 = row.createCell(5);
					if (record.getR18_amt_6to24m() != null) {
						cell1.setCellValue(record.getR18_amt_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column H
					cell1 = row.createCell(7);
					if (record.getR18_capital_6to24m() != null) {
						cell1.setCellValue(record.getR18_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I

					cell1 = row.createCell(8);
					if (record.getR18_amt_gt24m() != null) {
						cell1.setCellValue(record.getR18_amt_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column K
					cell1 = row.createCell(10);
					if (record.getR18_capital_gt24m() != null) {
						cell1.setCellValue(record.getR18_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row18 (R19)
					row = sheet.getRow(18);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR19_amt_6m() != null)
						cell1.setCellValue(record.getR19_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR19_capital_6m() != null) {
						cell1.setCellValue(record.getR19_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR19_amt_6to24m() != null)
						cell1.setCellValue(record.getR19_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR19_capital_6to24m() != null) {
						cell1.setCellValue(record.getR19_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR19_amt_gt24m() != null)
						cell1.setCellValue(record.getR19_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR19_capital_gt24m() != null) {
						cell1.setCellValue(record.getR19_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row19 (R20)
					row = sheet.getRow(19);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR20_amt_6m() != null)
						cell1.setCellValue(record.getR20_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR20_capital_6m() != null) {
						cell1.setCellValue(record.getR20_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR20_amt_6to24m() != null)
						cell1.setCellValue(record.getR20_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR20_capital_6to24m() != null) {
						cell1.setCellValue(record.getR20_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR20_amt_gt24m() != null)
						cell1.setCellValue(record.getR20_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR20_capital_gt24m() != null) {
						cell1.setCellValue(record.getR20_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row20 (R21)
					row = sheet.getRow(20);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR21_amt_6m() != null)
						cell1.setCellValue(record.getR21_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR21_capital_6m() != null) {
						cell1.setCellValue(record.getR21_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR21_amt_6to24m() != null)
						cell1.setCellValue(record.getR21_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR21_capital_6to24m() != null) {
						cell1.setCellValue(record.getR21_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR21_amt_gt24m() != null)
						cell1.setCellValue(record.getR21_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR21_capital_gt24m() != null) {
						cell1.setCellValue(record.getR21_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row21 (R22)
					row = sheet.getRow(21);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR22_amt_6m() != null)
						cell1.setCellValue(record.getR22_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR22_capital_6m() != null) {
						cell1.setCellValue(record.getR22_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR22_amt_6to24m() != null)
						cell1.setCellValue(record.getR22_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR22_capital_6to24m() != null) {
						cell1.setCellValue(record.getR22_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR22_amt_gt24m() != null)
						cell1.setCellValue(record.getR22_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR22_capital_gt24m() != null) {
						cell1.setCellValue(record.getR22_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row22 (R23)
					row = sheet.getRow(22);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR23_amt_6m() != null)
						cell1.setCellValue(record.getR23_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR23_capital_6m() != null) {
						cell1.setCellValue(record.getR23_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR23_amt_6to24m() != null)
						cell1.setCellValue(record.getR23_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR23_capital_6to24m() != null) {
						cell1.setCellValue(record.getR23_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR23_amt_gt24m() != null)
						cell1.setCellValue(record.getR23_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR23_capital_gt24m() != null) {
						cell1.setCellValue(record.getR23_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row23 (R24)
					row = sheet.getRow(23);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR24_amt_6m() != null)
						cell1.setCellValue(record.getR24_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR24_capital_6m() != null) {
						cell1.setCellValue(record.getR24_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR24_amt_6to24m() != null)
						cell1.setCellValue(record.getR24_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR24_capital_6to24m() != null) {
						cell1.setCellValue(record.getR24_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR24_amt_gt24m() != null)
						cell1.setCellValue(record.getR24_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR24_capital_gt24m() != null) {
						cell1.setCellValue(record.getR24_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row24 (R25)
					row = sheet.getRow(24);

					// Column C
					cell1 = row.getCell(2);
					if (record.getR25_amt_6m() != null)
						cell1.setCellValue(record.getR25_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR25_capital_6m() != null) {
						cell1.setCellValue(record.getR25_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR25_amt_6to24m() != null)
						cell1.setCellValue(record.getR25_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR25_capital_6to24m() != null) {
						cell1.setCellValue(record.getR25_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR25_amt_gt24m() != null)
						cell1.setCellValue(record.getR25_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR25_capital_gt24m() != null) {
						cell1.setCellValue(record.getR25_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row25 (R26)
					row = sheet.getRow(25);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR26_amt_6m() != null)
						cell1.setCellValue(record.getR26_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR26_capital_6m() != null) {
						cell1.setCellValue(record.getR26_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR26_amt_6to24m() != null)
						cell1.setCellValue(record.getR26_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR26_capital_6to24m() != null) {
						cell1.setCellValue(record.getR26_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR26_amt_gt24m() != null)
						cell1.setCellValue(record.getR26_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR26_capital_gt24m() != null) {
						cell1.setCellValue(record.getR26_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row26 (R27)
					row = sheet.getRow(26);

					// Column B

					// Column C

					cell1 = row.createCell(2);
					if (record.getR27_amt_6m() != null) {
						cell1.setCellValue(record.getR27_amt_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column E
					cell1 = row.createCell(4);
					if (record.getR27_capital_6m() != null) {
						cell1.setCellValue(record.getR27_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F

					cell1 = row.createCell(5);
					if (record.getR27_amt_6to24m() != null) {
						cell1.setCellValue(record.getR27_amt_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column H
					cell1 = row.createCell(7);
					if (record.getR27_capital_6to24m() != null) {
						cell1.setCellValue(record.getR27_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I

					cell1 = row.createCell(8);
					if (record.getR27_amt_gt24m() != null) {
						cell1.setCellValue(record.getR27_amt_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column K
					cell1 = row.createCell(10);
					if (record.getR27_capital_gt24m() != null) {
						cell1.setCellValue(record.getR27_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row27 (R28)
					row = sheet.getRow(27);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR28_amt_6m() != null)
						cell1.setCellValue(record.getR28_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR28_capital_6m() != null) {
						cell1.setCellValue(record.getR28_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR28_amt_6to24m() != null)
						cell1.setCellValue(record.getR28_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR28_capital_6to24m() != null) {
						cell1.setCellValue(record.getR28_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR28_amt_gt24m() != null)
						cell1.setCellValue(record.getR28_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR28_capital_gt24m() != null) {
						cell1.setCellValue(record.getR28_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row28 (R29)
					row = sheet.getRow(28);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR29_amt_6m() != null)
						cell1.setCellValue(record.getR29_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR29_capital_6m() != null) {
						cell1.setCellValue(record.getR29_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR29_amt_6to24m() != null)
						cell1.setCellValue(record.getR29_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR29_capital_6to24m() != null) {
						cell1.setCellValue(record.getR29_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR29_amt_gt24m() != null)
						cell1.setCellValue(record.getR29_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR29_capital_gt24m() != null) {
						cell1.setCellValue(record.getR29_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row29 (R30)
					row = sheet.getRow(29);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR30_amt_6m() != null)
						cell1.setCellValue(record.getR30_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR30_capital_6m() != null) {
						cell1.setCellValue(record.getR30_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR30_amt_6to24m() != null)
						cell1.setCellValue(record.getR30_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR30_capital_6to24m() != null) {
						cell1.setCellValue(record.getR30_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR30_amt_gt24m() != null)
						cell1.setCellValue(record.getR30_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR30_capital_gt24m() != null) {
						cell1.setCellValue(record.getR30_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row30 (R31)
					row = sheet.getRow(30);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR31_amt_6m() != null)
						cell1.setCellValue(record.getR31_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR31_capital_6m() != null) {
						cell1.setCellValue(record.getR31_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR31_amt_6to24m() != null)
						cell1.setCellValue(record.getR31_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR31_capital_6to24m() != null) {
						cell1.setCellValue(record.getR31_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR31_amt_gt24m() != null)
						cell1.setCellValue(record.getR31_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR31_capital_gt24m() != null) {
						cell1.setCellValue(record.getR31_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row31 (R32)
					row = sheet.getRow(31);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR32_amt_6m() != null)
						cell1.setCellValue(record.getR32_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR32_capital_6m() != null) {
						cell1.setCellValue(record.getR32_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR32_amt_6to24m() != null)
						cell1.setCellValue(record.getR32_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR32_capital_6to24m() != null) {
						cell1.setCellValue(record.getR32_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR32_amt_gt24m() != null)
						cell1.setCellValue(record.getR32_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR32_capital_gt24m() != null) {
						cell1.setCellValue(record.getR32_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row32 (R33)
					row = sheet.getRow(32);

					// Column B

					// Column E
					cell1 = row.createCell(4);
					if (record.getR33_capital_6m() != null) {
						cell1.setCellValue(record.getR33_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR33_capital_6to24m() != null) {
						cell1.setCellValue(record.getR33_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR33_capital_gt24m() != null) {
						cell1.setCellValue(record.getR33_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row34
					// Column K
					row = sheet.getRow(34);
					cell1 = row.createCell(4);
					if (record.getR35_tot_spec_risk_ch() != null) {
						cell1.setCellValue(record.getR35_tot_spec_risk_ch().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column K
				} // end of loop
				workbook.setForceFormulaRecalculation(true);
			} else {

			}
			// Write the final workbook content to the in-memory stream.
			workbook.write(out);
			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			return out.toByteArray();
		}
	}

	public byte[] BRRS_M_SIREmailArchivalExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		List<M_SIR_Archival_Summary_Entity> dataList = BRRS_M_SIR_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found forQ_STAFF report. Returning empty result.");
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
			// Create pure light green style (Excel highlight green)
			XSSFCellStyle greenStyle = (XSSFCellStyle) workbook.createCellStyle();
			greenStyle.cloneStyleFrom(textStyle);

			byte[] rgb = new byte[] { (byte) 146, (byte) 208, (byte) 80 }; // exact Excel light green
			XSSFColor green = new XSSFColor(rgb, null);

			greenStyle.setFillForegroundColor(green);
			greenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			greenStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SIR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// ----------

					// row11
					// Column C
					row = sheet.getRow(11);

					Cell cell1 = row.createCell(2);
					if (record.getR12_amt_6m() != null) {
						cell1.setCellValue(record.getR12_amt_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.createCell(4);
					if (record.getR12_capital_6m() != null) {
						cell1.setCellValue(record.getR12_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F

					cell1 = row.createCell(5);
					if (record.getR12_amt_6to24m() != null) {
						cell1.setCellValue(record.getR12_amt_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column H
					cell1 = row.createCell(7);
					if (record.getR12_capital_6to24m() != null) {
						cell1.setCellValue(record.getR12_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column I

					cell1 = row.createCell(8);
					if (record.getR12_amt_gt24m() != null) {
						cell1.setCellValue(record.getR12_amt_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row11
					// Column K
					cell1 = row.createCell(10);
					if (record.getR12_capital_gt24m() != null) {
						cell1.setCellValue(record.getR12_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// -------------

					// row12
					// Column b
					row = sheet.getRow(12);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR13_amt_6m() != null) {
						cell1.setCellValue(record.getR13_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR13_capital_6m() != null) {
						cell1.setCellValue(record.getR13_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR13_amt_6to24m() != null) {
						cell1.setCellValue(record.getR13_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR13_capital_6to24m() != null) {
						cell1.setCellValue(record.getR13_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR13_amt_gt24m() != null) {
						cell1.setCellValue(record.getR13_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR13_capital_gt24m() != null) {
						cell1.setCellValue(record.getR13_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row13
					// Column b
					row = sheet.getRow(13);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR14_amt_6m() != null) {
						cell1.setCellValue(record.getR14_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR14_capital_6m() != null) {
						cell1.setCellValue(record.getR14_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR14_amt_6to24m() != null) {
						cell1.setCellValue(record.getR14_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR14_capital_6to24m() != null) {
						cell1.setCellValue(record.getR14_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR14_amt_gt24m() != null) {
						cell1.setCellValue(record.getR14_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR14_capital_gt24m() != null) {
						cell1.setCellValue(record.getR14_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row14
					// Column b
					row = sheet.getRow(14);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR15_amt_6m() != null) {
						cell1.setCellValue(record.getR15_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR15_capital_6m() != null) {
						cell1.setCellValue(record.getR15_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR15_amt_6to24m() != null) {
						cell1.setCellValue(record.getR15_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR15_capital_6to24m() != null) {
						cell1.setCellValue(record.getR15_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR15_amt_gt24m() != null) {
						cell1.setCellValue(record.getR15_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR15_capital_gt24m() != null) {
						cell1.setCellValue(record.getR15_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
//					//row15  (R16)
					// Column B
					row = sheet.getRow(15);
					cell1 = row.getCell(1);

					// Column C
					cell1 = row.getCell(2);
					if (record.getR16_amt_6m() != null) {
						cell1.setCellValue(record.getR16_amt_6m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR16_capital_6m() != null) {
						cell1.setCellValue(record.getR16_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR16_amt_6to24m() != null) {
						cell1.setCellValue(record.getR16_amt_6to24m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR16_capital_6to24m() != null) {
						cell1.setCellValue(record.getR16_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR16_amt_gt24m() != null) {
						cell1.setCellValue(record.getR16_amt_gt24m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR16_capital_gt24m() != null) {
						cell1.setCellValue(record.getR16_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row16 (R17)
					row = sheet.getRow(16);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR17_amt_6m() != null)
						cell1.setCellValue(record.getR17_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR17_capital_6m() != null) {
						cell1.setCellValue(record.getR17_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR17_amt_6to24m() != null)
						cell1.setCellValue(record.getR17_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR17_capital_6to24m() != null) {
						cell1.setCellValue(record.getR17_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR17_amt_gt24m() != null)
						cell1.setCellValue(record.getR17_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR17_capital_gt24m() != null) {
						cell1.setCellValue(record.getR17_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row17 (R18)
					row = sheet.getRow(17);

					// Column B

					// Column C

					cell1 = row.createCell(2);
					if (record.getR18_amt_6m() != null) {
						cell1.setCellValue(record.getR18_amt_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column E
					cell1 = row.createCell(4);
					if (record.getR18_capital_6m() != null) {
						cell1.setCellValue(record.getR18_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F

					cell1 = row.createCell(5);
					if (record.getR18_amt_6to24m() != null) {
						cell1.setCellValue(record.getR18_amt_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column H
					cell1 = row.createCell(7);
					if (record.getR18_capital_6to24m() != null) {
						cell1.setCellValue(record.getR18_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I

					cell1 = row.createCell(8);
					if (record.getR18_amt_gt24m() != null) {
						cell1.setCellValue(record.getR18_amt_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column K
					cell1 = row.createCell(10);
					if (record.getR18_capital_gt24m() != null) {
						cell1.setCellValue(record.getR18_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row18 (R19)
					row = sheet.getRow(18);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR19_amt_6m() != null)
						cell1.setCellValue(record.getR19_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR19_capital_6m() != null) {
						cell1.setCellValue(record.getR19_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR19_amt_6to24m() != null)
						cell1.setCellValue(record.getR19_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR19_capital_6to24m() != null) {
						cell1.setCellValue(record.getR19_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR19_amt_gt24m() != null)
						cell1.setCellValue(record.getR19_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR19_capital_gt24m() != null) {
						cell1.setCellValue(record.getR19_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row19 (R20)
					row = sheet.getRow(19);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR20_amt_6m() != null)
						cell1.setCellValue(record.getR20_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR20_capital_6m() != null) {
						cell1.setCellValue(record.getR20_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR20_amt_6to24m() != null)
						cell1.setCellValue(record.getR20_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR20_capital_6to24m() != null) {
						cell1.setCellValue(record.getR20_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR20_amt_gt24m() != null)
						cell1.setCellValue(record.getR20_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR20_capital_gt24m() != null) {
						cell1.setCellValue(record.getR20_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row20 (R21)
					row = sheet.getRow(20);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR21_amt_6m() != null)
						cell1.setCellValue(record.getR21_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR21_capital_6m() != null) {
						cell1.setCellValue(record.getR21_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR21_amt_6to24m() != null)
						cell1.setCellValue(record.getR21_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR21_capital_6to24m() != null) {
						cell1.setCellValue(record.getR21_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR21_amt_gt24m() != null)
						cell1.setCellValue(record.getR21_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR21_capital_gt24m() != null) {
						cell1.setCellValue(record.getR21_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row21 (R22)
					row = sheet.getRow(21);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR22_amt_6m() != null)
						cell1.setCellValue(record.getR22_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR22_capital_6m() != null) {
						cell1.setCellValue(record.getR22_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR22_amt_6to24m() != null)
						cell1.setCellValue(record.getR22_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR22_capital_6to24m() != null) {
						cell1.setCellValue(record.getR22_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR22_amt_gt24m() != null)
						cell1.setCellValue(record.getR22_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR22_capital_gt24m() != null) {
						cell1.setCellValue(record.getR22_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row22 (R23)
					row = sheet.getRow(22);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR23_amt_6m() != null)
						cell1.setCellValue(record.getR23_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR23_capital_6m() != null) {
						cell1.setCellValue(record.getR23_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR23_amt_6to24m() != null)
						cell1.setCellValue(record.getR23_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR23_capital_6to24m() != null) {
						cell1.setCellValue(record.getR23_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR23_amt_gt24m() != null)
						cell1.setCellValue(record.getR23_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR23_capital_gt24m() != null) {
						cell1.setCellValue(record.getR23_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row23 (R24)
					row = sheet.getRow(23);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR24_amt_6m() != null)
						cell1.setCellValue(record.getR24_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR24_capital_6m() != null) {
						cell1.setCellValue(record.getR24_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR24_amt_6to24m() != null)
						cell1.setCellValue(record.getR24_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR24_capital_6to24m() != null) {
						cell1.setCellValue(record.getR24_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR24_amt_gt24m() != null)
						cell1.setCellValue(record.getR24_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR24_capital_gt24m() != null) {
						cell1.setCellValue(record.getR24_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row24 (R25)
					row = sheet.getRow(24);

					// Column C
					cell1 = row.getCell(2);
					if (record.getR25_amt_6m() != null)
						cell1.setCellValue(record.getR25_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR25_capital_6m() != null) {
						cell1.setCellValue(record.getR25_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR25_amt_6to24m() != null)
						cell1.setCellValue(record.getR25_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR25_capital_6to24m() != null) {
						cell1.setCellValue(record.getR25_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR25_amt_gt24m() != null)
						cell1.setCellValue(record.getR25_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR25_capital_gt24m() != null) {
						cell1.setCellValue(record.getR25_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row25 (R26)
					row = sheet.getRow(25);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR26_amt_6m() != null)
						cell1.setCellValue(record.getR26_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR26_capital_6m() != null) {
						cell1.setCellValue(record.getR26_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR26_amt_6to24m() != null)
						cell1.setCellValue(record.getR26_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR26_capital_6to24m() != null) {
						cell1.setCellValue(record.getR26_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR26_amt_gt24m() != null)
						cell1.setCellValue(record.getR26_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR26_capital_gt24m() != null) {
						cell1.setCellValue(record.getR26_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row26 (R27)
					row = sheet.getRow(26);

					// Column B

					// Column C

					cell1 = row.createCell(2);
					if (record.getR27_amt_6m() != null) {
						cell1.setCellValue(record.getR27_amt_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column E
					cell1 = row.createCell(4);
					if (record.getR27_capital_6m() != null) {
						cell1.setCellValue(record.getR27_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F

					cell1 = row.createCell(5);
					if (record.getR27_amt_6to24m() != null) {
						cell1.setCellValue(record.getR27_amt_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column H
					cell1 = row.createCell(7);
					if (record.getR27_capital_6to24m() != null) {
						cell1.setCellValue(record.getR27_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I

					cell1 = row.createCell(8);
					if (record.getR27_amt_gt24m() != null) {
						cell1.setCellValue(record.getR27_amt_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column K
					cell1 = row.createCell(10);
					if (record.getR27_capital_gt24m() != null) {
						cell1.setCellValue(record.getR27_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row27 (R28)
					row = sheet.getRow(27);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR28_amt_6m() != null)
						cell1.setCellValue(record.getR28_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR28_capital_6m() != null) {
						cell1.setCellValue(record.getR28_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR28_amt_6to24m() != null)
						cell1.setCellValue(record.getR28_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR28_capital_6to24m() != null) {
						cell1.setCellValue(record.getR28_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR28_amt_gt24m() != null)
						cell1.setCellValue(record.getR28_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR28_capital_gt24m() != null) {
						cell1.setCellValue(record.getR28_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row28 (R29)
					row = sheet.getRow(28);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR29_amt_6m() != null)
						cell1.setCellValue(record.getR29_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR29_capital_6m() != null) {
						cell1.setCellValue(record.getR29_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR29_amt_6to24m() != null)
						cell1.setCellValue(record.getR29_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR29_capital_6to24m() != null) {
						cell1.setCellValue(record.getR29_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR29_amt_gt24m() != null)
						cell1.setCellValue(record.getR29_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR29_capital_gt24m() != null) {
						cell1.setCellValue(record.getR29_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row29 (R30)
					row = sheet.getRow(29);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR30_amt_6m() != null)
						cell1.setCellValue(record.getR30_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR30_capital_6m() != null) {
						cell1.setCellValue(record.getR30_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR30_amt_6to24m() != null)
						cell1.setCellValue(record.getR30_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR30_capital_6to24m() != null) {
						cell1.setCellValue(record.getR30_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR30_amt_gt24m() != null)
						cell1.setCellValue(record.getR30_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR30_capital_gt24m() != null) {
						cell1.setCellValue(record.getR30_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row30 (R31)
					row = sheet.getRow(30);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR31_amt_6m() != null)
						cell1.setCellValue(record.getR31_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR31_capital_6m() != null) {
						cell1.setCellValue(record.getR31_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR31_amt_6to24m() != null)
						cell1.setCellValue(record.getR31_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR31_capital_6to24m() != null) {
						cell1.setCellValue(record.getR31_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR31_amt_gt24m() != null)
						cell1.setCellValue(record.getR31_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR31_capital_gt24m() != null) {
						cell1.setCellValue(record.getR31_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row31 (R32)
					row = sheet.getRow(31);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR32_amt_6m() != null)
						cell1.setCellValue(record.getR32_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR32_capital_6m() != null) {
						cell1.setCellValue(record.getR32_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR32_amt_6to24m() != null)
						cell1.setCellValue(record.getR32_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR32_capital_6to24m() != null) {
						cell1.setCellValue(record.getR32_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR32_amt_gt24m() != null)
						cell1.setCellValue(record.getR32_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR32_capital_gt24m() != null) {
						cell1.setCellValue(record.getR32_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row32 (R33)
					row = sheet.getRow(32);

					// Column B

					// Column E
					cell1 = row.createCell(4);
					if (record.getR33_capital_6m() != null) {
						cell1.setCellValue(record.getR33_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR33_capital_6to24m() != null) {
						cell1.setCellValue(record.getR33_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR33_capital_gt24m() != null) {
						cell1.setCellValue(record.getR33_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row34
					// Column K
					row = sheet.getRow(34);
					cell1 = row.createCell(4);
					if (record.getR35_tot_spec_risk_ch() != null) {
						cell1.setCellValue(record.getR35_tot_spec_risk_ch().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column K
				} // end of loop
				workbook.setForceFormulaRecalculation(true);
			} else {

			}
			// Write the final workbook content to the in-memory stream.
			workbook.write(out);
			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			return out.toByteArray();
		}
	}

}
