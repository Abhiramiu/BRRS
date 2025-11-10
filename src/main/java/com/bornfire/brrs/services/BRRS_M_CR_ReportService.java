package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.M_CR_Summary_Entity;
import com.bornfire.brrs.entities.M_CR_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_CR_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_CR_Archival_Summary_Repo;

import java.lang.reflect.Method;

@Component
@Service
public class BRRS_M_CR_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_CR_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	BRRS_M_CR_Summary_Repo BRRS_M_CR_Summary_Repo;

	@Autowired
	BRRS_M_CR_Archival_Summary_Repo BRRS_M_CR_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_CRView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, String version) {
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<M_CR_Archival_Summary_Entity> T1Master = new ArrayList<M_CR_Archival_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_CR_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} else {
			List<M_CR_Summary_Entity> T1Master = new ArrayList<M_CR_Summary_Entity>();

			try {
				Date d1 = dateformat.parse(todate);

				T1Master = BRRS_M_CR_Summary_Repo.getdatabydateList(dateformat.parse(todate));

				System.out.println("Size of t1master is :" + T1Master.size());

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/M_CR");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

//	custom implementation for archival

	public List<Object> getM_CRArchival() {
		List<Object> M_CRArchivallist = new ArrayList<>();
		try {
			M_CRArchivallist = BRRS_M_CR_Archival_Summary_Repo.getM_CRarchival();
			System.out.println("countser" + M_CRArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_CR Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_CRArchivallist;
	}

	public byte[] BRRS_M_CRExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		logger.info("type is : "+ type );
		logger.info("version is : "+ version );

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_CRARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data
		List<M_CR_Summary_Entity> dataList = BRRS_M_CR_Summary_Repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CR report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(filename);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}

		if (!Files.isReadable(templatePath)) {
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
			int startRow = 9;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_CR_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);

					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// ROW 10 DOWNLAOD

					Cell cell2 = row.createCell(0);
					if (record.getR10_PRODUCT() != null) {
						cell2.setCellValue(record.getR10_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(1);
					if (record.getR10_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR10_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(2);
					if (record.getR10_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR10_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					Cell cell5 = row.createCell(3);
					if (record.getR10_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR10_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);

					// ====================== R11 ======================
					cell2 = row.createCell(0);
					if (record.getR11_PRODUCT() != null) {
						cell2.setCellValue(record.getR11_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR11_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR11_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR11_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR11_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR11_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR11_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R12 ======================
					row = sheet.getRow(11);

					cell2 = row.createCell(0);
					if (record.getR12_PRODUCT() != null) {
						cell2.setCellValue(record.getR12_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR12_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR12_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR12_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR12_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR12_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR12_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R13 ======================
					row = sheet.getRow(12);

					// repeat same pattern...
					cell2 = row.createCell(0);
					if (record.getR13_PRODUCT() != null) {
						cell2.setCellValue(record.getR13_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR13_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR13_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR13_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR13_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR13_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR13_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R14 ======================
					row = sheet.getRow(13);

					cell2 = row.createCell(0);
					if (record.getR14_PRODUCT() != null) {
						cell2.setCellValue(record.getR14_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR14_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR14_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR14_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR14_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR14_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR14_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R15 ======================
					row = sheet.getRow(14);

					cell2 = row.createCell(0);
					if (record.getR15_PRODUCT() != null) {
						cell2.setCellValue(record.getR15_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR15_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR15_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR15_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR15_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR15_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR15_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R16 ======================
					row = sheet.getRow(15);

					cell2 = row.createCell(0);
					if (record.getR16_PRODUCT() != null) {
						cell2.setCellValue(record.getR16_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR16_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR16_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR16_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR16_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR16_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR16_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}
			workbook.write(out);
			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}
	}

	public byte[] getExcelM_CRARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Archival Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_CR_Archival_Summary_Entity> dataList = BRRS_M_CR_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CR report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(filename);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load Archival template from path: {}", templatePath.toAbsolutePath());

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

			int startRow = 9;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_CR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}


					
					// ROW 10 DOWNLAOD

					Cell cell2 = row.createCell(0);
					if (record.getR10_PRODUCT() != null) {
						cell2.setCellValue(record.getR10_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(1);
					if (record.getR10_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR10_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(2);
					if (record.getR10_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR10_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					Cell cell5 = row.createCell(3);
					if (record.getR10_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR10_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);

					// ====================== R11 ======================
					cell2 = row.createCell(0);
					if (record.getR11_PRODUCT() != null) {
						cell2.setCellValue(record.getR11_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR11_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR11_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR11_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR11_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR11_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR11_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R12 ======================
					row = sheet.getRow(11);

					cell2 = row.createCell(0);
					if (record.getR12_PRODUCT() != null) {
						cell2.setCellValue(record.getR12_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR12_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR12_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR12_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR12_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR12_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR12_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R13 ======================
					row = sheet.getRow(12);

					// repeat same pattern...
					cell2 = row.createCell(0);
					if (record.getR13_PRODUCT() != null) {
						cell2.setCellValue(record.getR13_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR13_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR13_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR13_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR13_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR13_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR13_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R14 ======================
					row = sheet.getRow(13);

					cell2 = row.createCell(0);
					if (record.getR14_PRODUCT() != null) {
						cell2.setCellValue(record.getR14_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR14_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR14_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR14_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR14_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR14_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR14_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R15 ======================
					row = sheet.getRow(14);

					cell2 = row.createCell(0);
					if (record.getR15_PRODUCT() != null) {
						cell2.setCellValue(record.getR15_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR15_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR15_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR15_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR15_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR15_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR15_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R16 ======================
					row = sheet.getRow(15);

					cell2 = row.createCell(0);
					if (record.getR16_PRODUCT() != null) {
						cell2.setCellValue(record.getR16_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR16_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR16_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR16_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR16_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR16_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR16_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					

				}

				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}
	}
	
	
	
	
	
	

	public void updateReport(M_CR_Summary_Entity updatedEntity) {
		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		M_CR_Summary_Entity existing = BRRS_M_CR_Summary_Repo.findById(updatedEntity.getReport_date()).orElseThrow(
				() -> new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

		try {
			// 1️⃣ Loop from R11 to R50 and copy fields
			for (int i = 10; i <= 17; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "PRODUCT", "TOTAL_LONG_POS", "TOTAL_SHORT_POS", "GROSS_OPEN_POS",
						"CHARGE_BASIS_RISK", "CAPITAL_CHARGE_BASIS_RISK", "NET_OPEN_POS", "CHARGE_DIR_RISK",
						"CAPITAL_CHARGE_DIR_RISK", "TOTAL_CAPITAL_CHARGE" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_CR_Summary_Entity.class.getMethod(getterName);
						Method setter = M_CR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

//	         2️⃣ Handle R51 totals
//	        String[] totalFields = { 
//                    "CAPITAL_CHARGE_BASIS_RISK", "NET_OPEN_POS","CAPITAL_CHARGE_DIR_RISK", "TOTAL_CAPITAL_CHARGE" };
//	        for (String field : totalFields) {
//	            String getterName = "getR17_" + field;
//	            String setterName = "setR17_" + field;
//	            
//
//	            try {
//	                Method getter = BRRS_M_CR_Summary_Entity.class.getMethod(getterName);
//	                Method setter = BRRS_M_CR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//
//	                Object newValue = getter.invoke(updatedEntity);
//	                setter.invoke(existing, newValue);
//
//	            } catch (NoSuchMethodException e) {
//	                // Skip if not present
//	                continue;
//	            }
//	        }

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 3️⃣ Save updated entity
		BRRS_M_CR_Summary_Repo.save(existing);
	}

}
