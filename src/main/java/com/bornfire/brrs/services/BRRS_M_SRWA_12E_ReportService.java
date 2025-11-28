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
import java.util.Optional;

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

import com.bornfire.brrs.entities.BRRS_M_SRWA_12E_LTV_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12E_LTV_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12E_LTV_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12E_LTV_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12E_LTV_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12E_LTV_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12E_LTV_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12E_LTV_Archival_Summary_Repo;

import java.lang.reflect.Method;

@Component
@Service
public class BRRS_M_SRWA_12E_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SRWA_12E_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	BRRS_M_SRWA_12E_LTV_Detail_Repo BRRS_M_SRWA_12E_LTV_Detail_Repo;

	@Autowired
	BRRS_M_SRWA_12E_LTV_Archival_Detail_Repo BRRS_M_SRWA_12E_LTV_Archival_Detail_Repo;

	@Autowired
	BRRS_M_SRWA_12E_LTV_Archival_Summary_Repo BRRS_M_SRWA_12E_LTV_Archival_Summary_Repo;

	@Autowired
	BRRS_M_SRWA_12E_LTV_Summary_Repo BRRS_M_SRWA_12E_LTV_Summary_Repo;
	
	
	

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
	
	public ModelAndView getM_SRWA_12EView(String reportId, String fromdate, String todate,
			String currency, String dtltype, Pageable pageable,
			String type, String version) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		try {
			Date d1 = dateformat.parse(todate);

	 // ---------- CASE 1: ARCHIVAL ----------
        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
            List<BRRS_M_SRWA_12E_LTV_Archival_Summary_Entity> T1Master = 
                BRRS_M_SRWA_12E_LTV_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 2: RESUB ----------
        else if ("RESUB".equalsIgnoreCase(type) && version != null) {
            List<BRRS_M_SRWA_12E_LTV_Archival_Summary_Entity> T1Master =
                BRRS_M_SRWA_12E_LTV_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 3: NORMAL ----------
        else {
            List<BRRS_M_SRWA_12E_LTV_Summary_Entity> T1Master = 
                BRRS_M_SRWA_12E_LTV_Summary_Repo.getdatabydateListWithVersion(todate);
            System.out.println("T1Master Size "+T1Master.size());
            mv.addObject("reportsummary", T1Master);
        }

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_SRWA_12E");
		mv.addObject("displaymode", "summary");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

//	custom implementation for archival

	// public List<Object> getM_SRWA_12EArchival() {
	// 	List<Object> M_SRWA_12EArchivallist = new ArrayList<>();
	// 	try {
	// 		M_SRWA_12EArchivallist = BRRS_M_SRWA_12E_LTV_Archival_Summary_Repo.getM_SRWA_12Earchival();
	// 		System.out.println("countser" + M_SRWA_12EArchivallist.size());
	// 	} catch (Exception e) {
	// 		// Log the exception
	// 		System.err.println("Error fetching M_SRWA_12E Archival data: " + e.getMessage());
	// 		e.printStackTrace();

	// 		// Optionally, you can rethrow it or return empty list
	// 		// throw new RuntimeException("Failed to fetch data", e);
	// 	}
	// 	return M_SRWA_12EArchivallist;
	// }

	public byte[] BRRS_M_SRWA_12EExcel(String filename, String reportId,
			String fromdate, String todate,
			String currency, String dtltype,
			String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// Convert string to Date
		Date reportDate = dateformat.parse(todate);

    // ARCHIVAL check
    if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
        logger.info("Service: Generating ARCHIVAL report for version {}", version);
        return getExcelM_SRWA_12EARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
    }
    // RESUB check
    else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
        logger.info("Service: Generating RESUB report for version {}", version);

       
        List<BRRS_M_SRWA_12E_LTV_Archival_Summary_Entity> T1Master =
                BRRS_M_SRWA_12E_LTV_Archival_Summary_Repo.getdatabydateListarchival(reportDate, version);

        // Generate Excel for RESUB
        return BRRS_M_SRWA_12EResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
    }




		// Default (LIVE) case
		List<BRRS_M_SRWA_12E_LTV_Summary_Entity> dataList = BRRS_M_SRWA_12E_LTV_Summary_Repo.getdatabydateList(reportDate);

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

			int startRow = 12;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					BRRS_M_SRWA_12E_LTV_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);

					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// ROW 10 DOWNLAOD

					Cell cell2 = row.createCell(2);
					if (record.getR13_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR13_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(3);
					if (record.getR13_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR13_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(4);
					if (record.getR13_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR13_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					

					row = sheet.getRow(13);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR14_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR14_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR14_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR14_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR14_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(14);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR15_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR15_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR15_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR15_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR15_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(15);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR16_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR16_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR16_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR16_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR16_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR16_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					

					row = sheet.getRow(16);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR17_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR17_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR17_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR17_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR17_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR17_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
										row = sheet.getRow(17);

					// ====================== R18 ======================
					cell2 = row.createCell(2);
					if (record.getR18_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR18_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR18_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR18_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR18_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR18_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
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

	public byte[] getExcelM_SRWA_12EARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Archival Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<BRRS_M_SRWA_12E_LTV_Archival_Summary_Entity> dataList = BRRS_M_SRWA_12E_LTV_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SRWA_12E report. Returning empty result.");
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

			int startRow = 12;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					BRRS_M_SRWA_12E_LTV_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}


					
					Cell cell2 = row.createCell(2);
					if (record.getR13_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR13_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(3);
					if (record.getR13_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR13_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(4);
					if (record.getR13_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR13_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					

					row = sheet.getRow(13);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR14_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR14_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR14_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR14_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR14_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(14);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR15_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR15_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR15_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR15_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR15_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(15);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR16_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR16_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR16_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR16_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR16_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR16_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					

					row = sheet.getRow(16);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR17_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR17_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR17_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR17_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR17_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR17_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
										row = sheet.getRow(17);

					// ====================== R18 ======================
					cell2 = row.createCell(2);
					if (record.getR18_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR18_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR18_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR18_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR18_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR18_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
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
	
	
	
	
	
	

	public void updateReport(BRRS_M_SRWA_12E_LTV_Summary_Entity updatedEntity) {
		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		BRRS_M_SRWA_12E_LTV_Summary_Entity existing = BRRS_M_SRWA_12E_LTV_Summary_Repo.findById(updatedEntity.getReportDate()).orElseThrow(
				() -> new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

		try {
			// 1️⃣ Loop from R11 to R50 and copy fields
			for (int i = 13; i <= 19; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "PERFORMING_EXPOSURE", "NON_PERFORMING", "SPECIFIC_PROV","UNSECURED_PORTION_NPL" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = BRRS_M_SRWA_12E_LTV_Summary_Entity.class.getMethod(getterName);
						Method setter = BRRS_M_SRWA_12E_LTV_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

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
		BRRS_M_SRWA_12E_LTV_Summary_Repo.save(existing);
	}



	//////////////////////////////////////////RESUBMISSION///////////////////////////////////////////////////////////////////	
/// Report Date | Report Version | Domain
/// RESUB VIEW
public List<Object[]> getM_SRWA_12EResub() {
    List<Object[]> resubList = new ArrayList<>();
    try {
        List<BRRS_M_SRWA_12E_LTV_Archival_Summary_Entity> latestArchivalList =
                BRRS_M_SRWA_12E_LTV_Archival_Summary_Repo.getdatabydateListWithVersion();

        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
            for (BRRS_M_SRWA_12E_LTV_Archival_Summary_Entity entity : latestArchivalList) {
                resubList.add(new Object[] {
                    entity.getReportDate(),
                    entity.getReportVersion()
                });
            }
            System.out.println("Fetched " + resubList.size() + " record(s)");
        } else {
            System.out.println("No archival data found.");
        }

    } catch (Exception e) {
        System.err.println("Error fetching M_SRWA_12E Resub data: " + e.getMessage());
        e.printStackTrace();
    }
    return resubList;
}



	//Archival View
	public List<Object[]> getM_SRWA_12EArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<BRRS_M_SRWA_12E_LTV_Archival_Summary_Entity> repoData = BRRS_M_SRWA_12E_LTV_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (BRRS_M_SRWA_12E_LTV_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] {
							entity.getReportDate(), 
							entity.getReportVersion() 
					};
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				BRRS_M_SRWA_12E_LTV_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_SRWA_12E Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Resubmit the values , latest version and Resub Date
	public void updateReportReSub(BRRS_M_SRWA_12E_LTV_Summary_Entity updatedEntity) {
		System.out.println("Came to Resub Service");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		Date reportDate = updatedEntity.getReportDate();
		int newVersion = 1;

		try {
			// Fetch the latest archival version for this report date
			Optional<BRRS_M_SRWA_12E_LTV_Archival_Summary_Entity> latestArchivalOpt = BRRS_M_SRWA_12E_LTV_Archival_Summary_Repo
					.getLatestArchivalVersionByDate(reportDate);

			// Determine next version number
			if (latestArchivalOpt.isPresent()) {
				BRRS_M_SRWA_12E_LTV_Archival_Summary_Entity latestArchival = latestArchivalOpt.get();
				try {
					newVersion = Integer.parseInt(latestArchival.getReportVersion()) + 1;
				} catch (NumberFormatException e) {
					System.err.println("Invalid version format. Defaulting to version 1");
					newVersion = 1;
				}
			} else {
				System.out.println("No previous archival found for date: " + reportDate);
			}

			// Prevent duplicate version number
			boolean exists = BRRS_M_SRWA_12E_LTV_Archival_Summary_Repo
					.findByReportDateAndReportVersion(reportDate, String.valueOf(newVersion))
					.isPresent();

			if (exists) {
				throw new RuntimeException("Version " + newVersion + " already exists for report date " + reportDate);
			}

			// Copy summary entity to archival entity
			BRRS_M_SRWA_12E_LTV_Archival_Summary_Entity archivalEntity = new BRRS_M_SRWA_12E_LTV_Archival_Summary_Entity();
			org.springframework.beans.BeanUtils.copyProperties(updatedEntity, archivalEntity);

			archivalEntity.setReportDate(reportDate);
			archivalEntity.setReportVersion(String.valueOf(newVersion));
			archivalEntity.setReportResubDate(new Date());

			System.out.println("Saving new archival version: " + newVersion);

			// Save new version to repository
			BRRS_M_SRWA_12E_LTV_Archival_Summary_Repo.save(archivalEntity);

			System.out.println(" Saved archival version successfully: " + newVersion);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error while creating archival resubmission record", e);
		}
	}


		/// Downloaded for Archival & Resub
	public byte[] BRRS_M_SRWA_12EResubExcel(String filename, String reportId, String fromdate,
        String todate, String currency, String dtltype,
        String type, String version) throws Exception {

    logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

    if (type.equals("RESUB") & version != null) {
       
    }

    List<BRRS_M_SRWA_12E_LTV_Archival_Summary_Entity> dataList =
        BRRS_M_SRWA_12E_LTV_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

    if (dataList.isEmpty()) {
        logger.warn("Service: No data found for M_SRWA_12E report. Returning empty result.");
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

			int startRow = 12;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					BRRS_M_SRWA_12E_LTV_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					
					
					
					Cell cell2 = row.createCell(2);
					if (record.getR13_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR13_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(3);
					if (record.getR13_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR13_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(4);
					if (record.getR13_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR13_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					

					row = sheet.getRow(13);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR14_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR14_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR14_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR14_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR14_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(14);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR15_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR15_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR15_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR15_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR15_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(15);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR16_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR16_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR16_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR16_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR16_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR16_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					

					row = sheet.getRow(16);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR17_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR17_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR17_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR17_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR17_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR17_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
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
}
