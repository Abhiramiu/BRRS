package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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


import com.bornfire.brrs.entities.BRRS_M_SRWA_12F_Archival_Summary_Repo;
import com.bornfire.brrs.entities.M_SRWA_12F_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12G_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12H_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12F_Summary_Repo;

import com.bornfire.brrs.entities.M_SRWA_12F_Archival_Summary_Entity;

import java.math.BigDecimal;

@Component
@Service

public class BRRS_M_SRWA_12F_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SRWA_12F_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_SRWA_12F_Summary_Repo M_SRWA_12F_Summary_Repo;

	@Autowired
	BRRS_M_SRWA_12F_Archival_Summary_Repo M_SRWA_12F_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_SRWA12FView(String reportId, String fromdate, String todate, 
			String currency, String dtltype, Pageable pageable, String type, String version) 
	{
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		try {
			Date d1 = dateformat.parse(todate);

	 // ---------- CASE 1: ARCHIVAL ----------
        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
            List<M_SRWA_12F_Archival_Summary_Entity> T1Master = 
                M_SRWA_12F_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 2: RESUB ----------
        else if ("RESUB".equalsIgnoreCase(type) && version != null) {
            List<M_SRWA_12F_Archival_Summary_Entity> T1Master =
                M_SRWA_12F_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 3: NORMAL ----------
        else {
            List<M_SRWA_12F_Summary_Entity> T1Master = 
                M_SRWA_12F_Summary_Repo.getdatabydateListWithVersion(todate);
            System.out.println("T1Master Size "+T1Master.size());
            mv.addObject("reportsummary", T1Master);
        }

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_SRWA_12F");
		mv.addObject("displaymode", "summary");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}
		
//		
//		else if ("RESUB".equalsIgnoreCase(type) && version != null) {
//            List<M_SRWA_12G_Resub_Summary_Entity1> T1Master = new ArrayList<M_SRWA_12G_Resub_Summary_Entity1>();
//    
//            try {
//				Date d1 = dateformat.parse(todate);
//            T1Master = BRRS_M_SRWA_12G_Resub_Summary_Repo1.getdatabydateListResub(dateformat.parse(todate), version);
//             
//            T2Master = BRRS_M_SRWA_12G_Resub_Summary_Repo2.getdatabydateListResub(dateformat.parse(todate), version);
//            
//            T3Master = BRRS_M_SRWA_12G_Resub_Summary_Repo3.getdatabydateListResub(dateformat.parse(todate), version);
//            
//            } catch (ParseException e) {
//				e.printStackTrace();
//			}
//                
//                mv.addObject("reportsummary1", T1Master);
//                mv.addObject("reportsummary2", T2Master);
//                mv.addObject("reportsummary3", T3Master);
//		}
//		
//		
//		else {
//			List<M_SRWA_12G_Summary_Entity> T1Master = new ArrayList<M_SRWA_12G_Summary_Entity>();
//	
//			
//			try {
//				Date d1 = dateformat.parse(todate);
//
//				T1Master = BRRS_M_SRWA_12G_Summary_Repo.getdatabydateList(dateformat.parse(todate));
//		
//				
//				
//				
//				System.out.println("Size of t1master is :"+T1Master.size());
//				
//				
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			mv.addObject("reportsummary1", T1Master);
//		
//		}
//
//		
//		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
//		mv.setViewName("BRRS/M_SRWA_12G");
//		mv.addObject("displaymode", "summary");
//		System.out.println("scv" + mv.getViewName());
//		return mv;
//	}



	public void updateReport(M_SRWA_12F_Summary_Entity updatedEntity) {
		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		M_SRWA_12F_Summary_Entity existing = M_SRWA_12F_Summary_Repo
				.findTopByReportDateOrderByReportVersionDesc(updatedEntity.getReportDate())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

		try {
			// 1️⃣ Loop through R14 to R100
			for (int i = 11; i <= 36; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "NAME_OF_CORPORATE", "CREDIT_RATING", "RATING_AGENCY", "EXPOSURE_AMT",
						"RISK_WEIGHT", };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_SRWA_12F_Summary_Entity.class.getMethod(getterName);
						Method setter = M_SRWA_12F_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

			// 2️⃣ Handle R100 total fields using same structure
			String prefix = "R37_";
			String[] totalFields = { "NAME_OF_CORPORATE", "CREDIT_RATING", "RATING_AGENCY", "EXPOSURE_AMT",
					"RISK_WEIGHT", "RISK_WEIGHTED_AMT" };

			for (String field : totalFields) {
				String getterName = "get" + prefix + field;
				String setterName = "set" + prefix + field;

				try {
					Method getter = M_SRWA_12F_Summary_Entity.class.getMethod(getterName);
					Method setter = M_SRWA_12F_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

					Object newValue = getter.invoke(updatedEntity);
					setter.invoke(existing, newValue);

				} catch (NoSuchMethodException e) {
					// Skip missing total fields
					continue;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// Save updated entity
		System.out.println("abc");
		M_SRWA_12F_Summary_Repo.save(existing);
	}


	public byte[] getM_SRWA_12FExcel(String filename, String reportId, String fromdate, String todate, String currency,
									 String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

		// Convert string to Date
		Date reportDate = dateformat.parse(todate);

    // ARCHIVAL check
    if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
        logger.info("Service: Generating ARCHIVAL report for version {}", version);
        return getExcelM_SRWA_12FARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
    }
    // RESUB check
    else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
        logger.info("Service: Generating RESUB report for version {}", version);

       
        List<M_SRWA_12F_Archival_Summary_Entity> T1Master =
                M_SRWA_12F_Archival_Summary_Repo.getdatabydateListarchival(reportDate, version);

        // Generate Excel for RESUB
        return BRRS_M_SRWA_12FResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
    }




		// Default (LIVE) case
		List<M_SRWA_12F_Summary_Entity> dataList1 = M_SRWA_12F_Summary_Repo.getdatabydateList(reportDate);

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
			font.setFontHeightInPoints((short)8); // size 8
			font.setFontName("Arial");    

			CellStyle numberStyle = workbook.createCellStyle();
			//numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
			// --- End of Style Definitions ---

			int startRow = 10;

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {

					M_SRWA_12F_Summary_Entity record = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row11
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR11_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR11_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row11
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR11_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR11_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR12_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR12_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row12
					// Column F
					cell5 = row.createCell(5);
					if (record.getR12_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR12_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR13_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR13_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR13_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR14_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row14
					// Column F
					cell5 = row.createCell(5);
					if (record.getR14_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR14_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR15_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row15
					// Column F
					cell5 = row.createCell(5);
					if (record.getR15_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR15_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR16_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR16_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row16
					// Column F
					cell5 = row.createCell(5);
					if (record.getR16_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR16_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR17_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR17_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row17
					// Column F
					cell5 = row.createCell(5);
					if (record.getR17_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR17_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR18_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR18_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row18
					// Column F
					cell5 = row.createCell(5);
					if (record.getR18_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR18_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR19_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR19_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row19
					// Column F
					cell5 = row.createCell(5);
					if (record.getR19_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR19_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR20_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row20
					// Column F
					cell5 = row.createCell(5);
					if (record.getR20_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR20_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR21_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row21
					// Column F
					cell5 = row.createCell(5);
					if (record.getR21_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR21_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR22_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					// Column F
					cell5 = row.createCell(5);
					if (record.getR22_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR22_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR23_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row23
					// Column F
					cell5 = row.createCell(5);
					if (record.getR23_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR23_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR24_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row24
					// Column F
					cell5 = row.createCell(5);
					if (record.getR24_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR24_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR25_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR25_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row25
					// Column F
					cell5 = row.createCell(5);
					if (record.getR25_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR25_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR26_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR26_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row26
					// Column F
					cell5 = row.createCell(5);
					if (record.getR26_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR26_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR27_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR27_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row27
					// Column F
					cell5 = row.createCell(5);
					if (record.getR27_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR27_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR28_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR28_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row28
					// Column F
					cell5 = row.createCell(5);
					if (record.getR28_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR28_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR29_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR29_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row29
					// Column F
					cell5 = row.createCell(5);
					if (record.getR29_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR29_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR30_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR30_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row30
					// Column F
					cell5 = row.createCell(5);
					if (record.getR30_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR30_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR31_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR31_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row31
					// Column F
					cell5 = row.createCell(5);
					if (record.getR31_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR31_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR32_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR32_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row32
					// Column F
					cell5 = row.createCell(5);
					if (record.getR32_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR32_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR33_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR33_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR33_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR33_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR34_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR34_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row34
					// Column F
					cell5 = row.createCell(5);
					if (record.getR34_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR34_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR35_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR35_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row35
					// Column F
					cell5 = row.createCell(5);
					if (record.getR35_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR35_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR36_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR36_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row36
					// Column F
					cell5 = row.createCell(5);
					if (record.getR36_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR36_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
				

				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}
	}

//	public List<Object> getM_SRWA12FArchival() {
//		List<Object> M_SRWA12FArchivallist = new ArrayList<>();
//		List<Object> M_FXRArchivallist2 = new ArrayList<>();
//		List<Object> M_FXRArchivallist3 = new ArrayList<>();
//		try {
//			M_SRWA12FArchivallist = M_SRWA_12F_Archival_Summary_Repo.getM_SRWA12Farchival();

//			System.out.println("countser" + M_SRWA12FArchivallist.size());
//			System.out.println("countser" + M_FXRArchivallist.size());
//			System.out.println("countser" + M_FXRArchivallist.size());
//		} catch (Exception e) {
			// Log the exception
//			System.err.println("Error fetching M_SECL Archival data: " + e.getMessage());
//			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
	//	}
	//	return M_SRWA12FArchivallist;
//	}

	public byte[] getExcelM_SRWA_12FARCHIVAL(String filename, String reportId, String fromdate,
			String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if ("ARCHIVAL".equals(type) && version != null) {
		}
			List<M_SRWA_12F_Archival_Summary_Entity> dataList1 = M_SRWA_12F_Archival_Summary_Repo
					.getdatabydateListarchival(dateformat.parse(todate), version);
		

			
		
		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for M_SRWA_12G report. Returning empty result.");
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

			int startRow = 10;

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {

					M_SRWA_12F_Archival_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row11
					// Column E
					Cell cell4 = row.createCell(4);
					if (record1.getR11_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR11_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row11
					// Column F
					Cell cell5 = row.createCell(5);
					if (record1.getR11_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR11_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR12_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR12_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row12
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR12_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR12_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR13_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR13_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR13_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR13_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR14_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR14_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row14
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR14_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR14_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR15_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR15_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row15
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR15_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR15_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR16_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR16_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row16
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR16_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR16_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR17_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR17_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row17
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR17_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR17_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR18_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR18_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row18
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR18_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR18_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR19_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR19_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row19
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR19_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR19_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR20_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR20_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row20
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR20_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR20_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR21_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR21_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row21
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR21_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR21_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR22_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR22_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR22_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR22_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR23_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR23_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row23
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR23_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR23_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR24_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR24_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row24
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR24_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR24_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR25_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR25_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row25
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR25_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR25_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR26_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR26_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row26
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR26_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR26_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR27_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR27_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row27
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR27_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR27_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR28_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR28_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row28
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR28_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR28_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR29_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR29_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row29
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR29_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR29_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR30_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR30_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row30
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR30_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR30_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR31_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR31_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row31
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR31_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR31_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR32_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR32_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row32
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR32_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR32_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR33_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR33_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR33_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR33_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR34_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR34_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row34
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR34_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR34_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR35_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR35_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row35
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR35_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR35_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR36_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR36_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row36
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR36_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR36_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}
	}
	
/////////////////////////////////////////RESUBMISSION///////////////////////////////////////////////////////////////////	
/// Report Date | Report Version | Domain
/// RESUB VIEW
public List<Object[]> getM_SRWA_12FResub() {
List<Object[]> resubList = new ArrayList<>();
try {
List<M_SRWA_12F_Archival_Summary_Entity> latestArchivalList = 
M_SRWA_12F_Archival_Summary_Repo.getdatabydateListWithVersionAll();

if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
for (M_SRWA_12F_Archival_Summary_Entity entity : latestArchivalList) {
Object[] row = new Object[] {
entity.getReportDate(),
entity.getReportVersion()
};
resubList.add(row);
}
System.out.println("Fetched " + resubList.size() + " record(s)");
} else {
System.out.println("No archival data found.");
}
} catch (Exception e) {
System.err.println("Error fetching M_SRWA_12F Resub data: " + e.getMessage());
e.printStackTrace();
}
return resubList;
}


//Archival View
public List<Object[]> getM_SRWA_12FArchival() {
List<Object[]> archivalList = new ArrayList<>();

try {
List<M_SRWA_12F_Archival_Summary_Entity> repoData = M_SRWA_12F_Archival_Summary_Repo
.getdatabydateListWithVersionAll();

if (repoData != null && !repoData.isEmpty()) {
for (M_SRWA_12F_Archival_Summary_Entity entity : repoData) {
Object[] row = new Object[] {
entity.getReportDate(), 
entity.getReportVersion() 
};
archivalList.add(row);
}

System.out.println("Fetched " + archivalList.size() + " archival records");
M_SRWA_12F_Archival_Summary_Entity first = repoData.get(0);
System.out.println("Latest archival version: " + first.getReportVersion());
} else {
System.out.println("No archival data found.");
}

} catch (Exception e) {
System.err.println("Error fetching M_SRWA_12G Archival data: " + e.getMessage());
e.printStackTrace();
}

return archivalList;
}


// Resubmit the values , latest version and Resub Date
public void updateReportReSub(M_SRWA_12F_Summary_Entity updatedEntity) {
System.out.println("Came to Resub Service");
System.out.println("Report Date: " + updatedEntity.getReportDate());

Date reportDate = updatedEntity.getReportDate();
int newVersion = 1;

try {
// Fetch the latest archival version for this report date
Optional<M_SRWA_12F_Archival_Summary_Entity> latestArchivalOpt = M_SRWA_12F_Archival_Summary_Repo
.getLatestArchivalVersionByDate(reportDate);

// Determine next version number
if (latestArchivalOpt.isPresent()) {
M_SRWA_12F_Archival_Summary_Entity latestArchival = latestArchivalOpt.get();
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
boolean exists = M_SRWA_12F_Archival_Summary_Repo
.findByReportDateAndReportVersion(reportDate, String.valueOf(newVersion))
.isPresent();

if (exists) {
throw new RuntimeException("Version " + newVersion + " already exists for report date " + reportDate);
}

// Copy summary entity to archival entity
M_SRWA_12F_Archival_Summary_Entity archivalEntity = new M_SRWA_12F_Archival_Summary_Entity();
org.springframework.beans.BeanUtils.copyProperties(updatedEntity, archivalEntity);

archivalEntity.setReportDate(reportDate);
archivalEntity.setReportVersion(String.valueOf(newVersion));
archivalEntity.setReportResubDate(new Date());

System.out.println("Saving new archival version: " + newVersion);

// Save new version to repository
M_SRWA_12F_Archival_Summary_Repo.save(archivalEntity);

System.out.println(" Saved archival version successfully: " + newVersion);

} catch (Exception e) {
e.printStackTrace();
throw new RuntimeException("Error while creating archival resubmission record", e);
}
}

/// Downloaded for Archival & Resub
public byte[] BRRS_M_SRWA_12FResubExcel(String filename, String reportId, String fromdate,
String todate, String currency, String dtltype,
String type, String version) throws Exception {

logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

if (type.equals("RESUB") & version != null) {

}

List<M_SRWA_12F_Archival_Summary_Entity> dataList1 =
M_SRWA_12F_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

if (dataList1.isEmpty()) {
logger.warn("Service: No data found for M_SRWA_12G report. Returning empty result.");
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
int startRow = 10;

if (!dataList1.isEmpty()) {
	for (int i = 0; i < dataList1.size(); i++) {

		M_SRWA_12F_Archival_Summary_Entity record1 = dataList1.get(i);
		System.out.println("rownumber=" + startRow + i);
		System.out.println("rownumber=" + startRow + i);
		Row row = sheet.getRow(startRow + i);
		if (row == null) {
			row = sheet.createRow(startRow + i);
		}

		// row11
		// Column E
		Cell cell4 = row.createCell(4);
		if (record1.getR11_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR11_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row11
		// Column F
		Cell cell5 = row.createCell(5);
		if (record1.getR11_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR11_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row12
		row = sheet.getRow(11);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR12_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR12_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row12
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR12_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR12_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row13
		row = sheet.getRow(12);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR13_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR13_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row13
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR13_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR13_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row14
		row = sheet.getRow(13);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR14_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR14_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row14
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR14_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR14_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row15
		row = sheet.getRow(14);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR15_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR15_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row15
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR15_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR15_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row16
		row = sheet.getRow(15);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR16_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR16_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row16
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR16_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR16_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row17
		row = sheet.getRow(16);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR17_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR17_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row17
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR17_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR17_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row18
		row = sheet.getRow(17);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR18_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR18_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row18
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR18_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR18_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row19
		row = sheet.getRow(18);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR19_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR19_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row19
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR19_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR19_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row20
		row = sheet.getRow(19);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR20_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR20_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row20
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR20_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR20_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row21
		row = sheet.getRow(20);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR21_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR21_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row21
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR21_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR21_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row22
		row = sheet.getRow(21);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR22_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR22_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row22
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR22_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR22_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row23
		row = sheet.getRow(22);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR23_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR23_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row23
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR23_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR23_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row24
		row = sheet.getRow(23);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR24_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR24_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row24
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR24_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR24_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row25
		row = sheet.getRow(24);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR25_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR25_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row25
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR25_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR25_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row26
		row = sheet.getRow(25);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR26_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR26_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row26
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR26_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR26_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row27
		row = sheet.getRow(26);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR27_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR27_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row27
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR27_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR27_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row28
		row = sheet.getRow(27);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR28_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR28_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row28
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR28_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR28_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row29
		row = sheet.getRow(28);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR29_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR29_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row29
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR29_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR29_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row30
		row = sheet.getRow(29);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR30_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR30_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row30
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR30_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR30_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row31
		row = sheet.getRow(30);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR31_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR31_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row31
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR31_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR31_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row32
		row = sheet.getRow(31);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR32_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR32_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row32
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR32_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR32_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row33
		row = sheet.getRow(32);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR33_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR33_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row33
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR33_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR33_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row34
		row = sheet.getRow(33);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR34_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR34_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row34
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR34_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR34_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row35
		row = sheet.getRow(34);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR35_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR35_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row35
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR35_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR35_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row36
		row = sheet.getRow(35);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR36_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR36_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row36
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR36_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR36_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}


	}
	workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
} else {

}

// Write the final workbook content to the in-memory stream.
workbook.write(out);

logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

return out.toByteArray();
}
}



}
