package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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

import com.bornfire.brrs.entities.BRRS_MDISB5_Archival_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_MDISB5_Archival_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_MDISB5_Archival_Summary_Repo3;
import com.bornfire.brrs.entities.BRRS_MDISB5_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_MDISB5_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_MDISB5_Summary_Repo3;
import com.bornfire.brrs.entities.MDISB5_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.MDISB5_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.MDISB5_Archival_Summary_Entity3;
import com.bornfire.brrs.entities.MDISB5_Summary_Entity1;
import com.bornfire.brrs.entities.MDISB5_Summary_Entity2;
import com.bornfire.brrs.entities.MDISB5_Summary_Entity3;
import com.bornfire.brrs.entities.M_SRWA_12G_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12G_Summary_Entity;
import com.bornfire.brrs.entities.Q_BRANCHNET_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.Q_BRANCHNET_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.Q_BRANCHNET_Archival_Summary_Entity3;
import com.bornfire.brrs.entities.Q_BRANCHNET_Archival_Summary_Entity4;
import com.bornfire.brrs.entities.Q_BRANCHNET_Summary_Entity1;
import com.bornfire.brrs.entities.Q_BRANCHNET_Summary_Entity2;
import com.bornfire.brrs.entities.Q_BRANCHNET_Summary_Entity3;
import com.bornfire.brrs.entities.Q_BRANCHNET_Summary_Entity4;

@Component
@Service

public class BRRS_MDISB5_ReportService {
	
	private static final Logger logger = LoggerFactory.getLogger(BRRS_MDISB5_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	BRRS_MDISB5_Summary_Repo1	BRRS_MDISB5_Summary_Repo1;
	
	@Autowired
	BRRS_MDISB5_Summary_Repo2	BRRS_MDISB5_Summary_Repo2;
	
	@Autowired
	BRRS_MDISB5_Summary_Repo3	BRRS_MDISB5_Summary_Repo3;
	
	@Autowired
	BRRS_MDISB5_Archival_Summary_Repo1	BRRS_MDISB5_Archival_Summary_Repo1;
	
	@Autowired
	BRRS_MDISB5_Archival_Summary_Repo2	BRRS_MDISB5_Archival_Summary_Repo2;
	
	@Autowired
	BRRS_MDISB5_Archival_Summary_Repo3	BRRS_MDISB5_Archival_Summary_Repo3;
				

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
	
	public ModelAndView getMDISB5View(String reportId, String fromdate, String todate, 
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
            List<MDISB5_Archival_Summary_Entity1> T1Master = 
            		BRRS_MDISB5_Archival_Summary_Repo1.getdatabydateListarchival(d1, version);
            List<MDISB5_Archival_Summary_Entity2> T2Master = 
            		BRRS_MDISB5_Archival_Summary_Repo2.getdatabydateListarchival(d1, version);
            List<MDISB5_Archival_Summary_Entity3> T3Master = 
            		BRRS_MDISB5_Archival_Summary_Repo3.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary1", T1Master);
            mv.addObject("reportsummary2", T2Master);
            mv.addObject("reportsummary3", T3Master);
        }

        // ---------- CASE 2: RESUB ----------
        else if ("RESUB".equalsIgnoreCase(type) && version != null) {
        	List<MDISB5_Archival_Summary_Entity1> T1Master = 
            		BRRS_MDISB5_Archival_Summary_Repo1.getdatabydateListarchival(d1, version);
            List<MDISB5_Archival_Summary_Entity2> T2Master = 
            		BRRS_MDISB5_Archival_Summary_Repo2.getdatabydateListarchival(d1, version);
            List<MDISB5_Archival_Summary_Entity3> T3Master = 
            		BRRS_MDISB5_Archival_Summary_Repo3.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary1", T1Master);
            mv.addObject("reportsummary2", T2Master);
            mv.addObject("reportsummary3", T3Master);
        }

        // ---------- CASE 3: NORMAL ----------
        else {
            List<MDISB5_Summary_Entity1> T1Master = 
            		BRRS_MDISB5_Summary_Repo1.getdatabydateList(dateformat.parse(todate));
            List<MDISB5_Summary_Entity2> T2Master = 
            		BRRS_MDISB5_Summary_Repo2.getdatabydateList(dateformat.parse(todate));
            List<MDISB5_Summary_Entity3> T3Master = 
            		BRRS_MDISB5_Summary_Repo3.getdatabydateList(dateformat.parse(todate));
            
            
            System.out.println("T1Master Size "+T1Master.size());
            
            mv.addObject("reportsummary1", T1Master);
            mv.addObject("reportsummary2", T2Master);
            mv.addObject("reportsummary3", T3Master);
        }

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/MDISB5");
		mv.addObject("displaymode", "summary");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}
	
	private <T> List<T> safeList(List<T> list) {
		if (list == null) return new ArrayList<>();
		return list.stream().filter(Objects::nonNull).collect(Collectors.toList());
		}
	

	public void updateReport1(MDISB5_Summary_Entity1 updatedEntity) {
	    System.out.println("Came to services1");
	    System.out.println("Report Date: " + updatedEntity.getReportDate());

	    MDISB5_Summary_Entity1 existing = BRRS_MDISB5_Summary_Repo1
				.findTopByReportDateOrderByReportVersionDesc(updatedEntity.getReportDate())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

	    try {
	        // 1️⃣ Loop from R5 to R15 and copy fields
	        for (int i = 5; i <= 15; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = { "NAME_OF_SHAREHOLDER", "PERCENTAGE_SHAREHOLDING", "NUMBER_OF_ACCOUNTS","AMOUNT"};

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = MDISB5_Summary_Entity1.class.getMethod(getterName);
	                    Method setter = MDISB5_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        
	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // 3️⃣ Save updated entity
	    BRRS_MDISB5_Summary_Repo1.save(existing);
	}
	
	
	public void updateReport2(MDISB5_Summary_Entity2 updatedEntity) {
	    System.out.println("Came to services2");
	    System.out.println("Report Date: " + updatedEntity.getReportDate());

	    MDISB5_Summary_Entity2 existing = BRRS_MDISB5_Summary_Repo2
				.findTopByReportDateOrderByReportVersionDesc(updatedEntity.getReportDate())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

	    try {
	        // 1️⃣ Loop from R20 to R33 and copy fields
	        for (int i = 20; i <= 33; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = { "NAME_OF_BOARD_MEMBERS", "EXECUTIVE_OR_NONEXECUTIVE", "NUMBER_OF_ACCOUNTS","AMOUNT"};

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = MDISB5_Summary_Entity2.class.getMethod(getterName);
	                    Method setter = MDISB5_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        
	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // 3️⃣ Save updated entity
	    BRRS_MDISB5_Summary_Repo2.save(existing);
	}
	
	
	public void updateReport3(MDISB5_Summary_Entity3 updatedEntity) {
	    System.out.println("Came to services3");
	    System.out.println("Report Date: " + updatedEntity.getReportDate());

	    MDISB5_Summary_Entity3 existing = BRRS_MDISB5_Summary_Repo3
				.findTopByReportDateOrderByReportVersionDesc(updatedEntity.getReportDate())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

	    try {
	        // 1️⃣ Loop from R37 to R44 and copy fields
	        for (int i = 37; i <= 44; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = { "NAME", "DESIGNATION_OR_POSITION", "NUMBER_OF_ACCOUNTS","AMOUNT"};

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = MDISB5_Summary_Entity3.class.getMethod(getterName);
	                    Method setter = MDISB5_Summary_Entity3.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        
	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // 3️⃣ Save updated entity
	    BRRS_MDISB5_Summary_Repo3.save(existing);
	}
	
	
	public byte[] getMDISB5Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

		// Convert string to Date
		Date reportDate = dateformat.parse(todate);

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelMDISB5ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}
		// RESUB check
		else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating RESUB report for version {}", version);

			List<MDISB5_Archival_Summary_Entity1> T1Master = BRRS_MDISB5_Archival_Summary_Repo1
					.getdatabydateListarchival(reportDate, version);
			
			List<MDISB5_Archival_Summary_Entity2> T2Master = BRRS_MDISB5_Archival_Summary_Repo2
					.getdatabydateListarchival(reportDate, version);
			
			List<MDISB5_Archival_Summary_Entity3> T3Master = BRRS_MDISB5_Archival_Summary_Repo3
					.getdatabydateListarchival(reportDate, version);

			// Generate Excel for RESUB
			return BRRS_MDISB5ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Default (LIVE) case
		List<MDISB5_Summary_Entity1> dataList1 = BRRS_MDISB5_Summary_Repo1.getdatabydateList(reportDate);
		List<MDISB5_Summary_Entity2> dataList2 = BRRS_MDISB5_Summary_Repo2.getdatabydateList(reportDate);
		List<MDISB5_Summary_Entity3> dataList3 = BRRS_MDISB5_Summary_Repo3.getdatabydateList(reportDate);

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
//numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
// --- End of Style Definitions ---

			int startRow = 4;

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					MDISB5_Summary_Entity1 record1 = dataList1.get(i);
					MDISB5_Summary_Entity2 record2 = dataList2.get(i);
					MDISB5_Summary_Entity3 record3 = dataList3.get(i);
					
					System.out.println("rownumber=" + (startRow + i));

					Row row;
					Cell cellA, cellB, cellC, cellD;
					CellStyle originalStyle;

					// ===== Row 5 / Col A =====
					row = sheet.getRow(4);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR5_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR5_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// ===== Row 5 / Col B =====
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR5_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR5_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R11 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR5_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR5_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R11 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR5_AMOUNT() != null)
						cellD.setCellValue(record1.getR5_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 6 / Col A =====
					row = sheet.getRow(5);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR6_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR6_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR6_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR6_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR6_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR6_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR6_AMOUNT() != null)
						cellD.setCellValue(record1.getR6_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 7 / Col A =====
					row = sheet.getRow(6);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR7_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR7_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR7_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR7_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR7_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR7_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR7_AMOUNT() != null)
						cellD.setCellValue(record1.getR7_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 8 / Col A =====
					row = sheet.getRow(7);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR8_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR8_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR8_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR8_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR8_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR8_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR8_AMOUNT() != null)
						cellD.setCellValue(record1.getR8_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 9 / Col A =====
					row = sheet.getRow(8);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR9_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR9_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR9_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR9_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR9_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR9_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR9_AMOUNT() != null)
						cellD.setCellValue(record1.getR9_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 10 / Col A =====
					row = sheet.getRow(9);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR10_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR10_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR10_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR10_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR10_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR10_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR10_AMOUNT() != null)
						cellD.setCellValue(record1.getR10_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 11 / Col A =====
					row = sheet.getRow(10);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR11_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR11_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR11_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR11_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR11_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR11_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR11_AMOUNT() != null)
						cellD.setCellValue(record1.getR11_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 12 / Col A =====
					row = sheet.getRow(11);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR12_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR12_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR12_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR12_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR12_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR12_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR12_AMOUNT() != null)
						cellD.setCellValue(record1.getR12_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 13 / Col A =====
					row = sheet.getRow(12);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR13_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR13_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR13_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR13_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR13_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR13_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR13_AMOUNT() != null)
						cellD.setCellValue(record1.getR13_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 14 / Col A =====
					row = sheet.getRow(13);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR14_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR14_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR14_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR14_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR14_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR14_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR14_AMOUNT() != null)
						cellD.setCellValue(record1.getR14_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 15 / Col A =====
					row = sheet.getRow(14);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR15_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR15_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR15_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR15_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR15_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR15_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR15_AMOUNT() != null)
						cellD.setCellValue(record1.getR15_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 20 / Col A =====
					row = sheet.getRow(19);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR20_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR20_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR20_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR20_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR20_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR20_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR20_AMOUNT() != null)
						cellD.setCellValue(record2.getR20_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 21 / Col A =====
					row = sheet.getRow(20);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR21_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR21_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR21_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR21_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR21_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR21_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR21_AMOUNT() != null)
						cellD.setCellValue(record2.getR21_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 22 / Col A =====
					row = sheet.getRow(21);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR22_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR22_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR22_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR22_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR22_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR22_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR22_AMOUNT() != null)
						cellD.setCellValue(record2.getR22_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 23 / Col A =====
					row = sheet.getRow(22);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR23_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR23_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR23_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR23_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR23_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR23_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR23_AMOUNT() != null)
						cellD.setCellValue(record2.getR23_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 24 / Col A =====
					row = sheet.getRow(23);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR24_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR24_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR24_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR24_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR24_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR24_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR24_AMOUNT() != null)
						cellD.setCellValue(record2.getR24_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 25 / Col A =====
					row = sheet.getRow(24);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR25_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR25_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR25_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR25_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR25_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR25_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR25_AMOUNT() != null)
						cellD.setCellValue(record2.getR25_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 26 / Col A =====
					row = sheet.getRow(25);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR26_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR26_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR26_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR26_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR26_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR26_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR26_AMOUNT() != null)
						cellD.setCellValue(record2.getR26_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 27 / Col A =====
					row = sheet.getRow(26);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR27_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR27_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR27_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR27_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR27_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR27_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR27_AMOUNT() != null)
						cellD.setCellValue(record2.getR27_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 28 / Col A =====
					row = sheet.getRow(27);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR28_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR28_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR28_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR28_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR28_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR28_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR28_AMOUNT() != null)
						cellD.setCellValue(record2.getR28_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 29 / Col A =====
					row = sheet.getRow(28);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR29_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR29_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR29_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR29_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR29_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR29_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR29_AMOUNT() != null)
						cellD.setCellValue(record2.getR29_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 30 / Col A =====
					row = sheet.getRow(29);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR30_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR30_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR30_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR30_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR30_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR30_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR30_AMOUNT() != null)
						cellD.setCellValue(record2.getR30_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 31 / Col A =====
					row = sheet.getRow(30);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR31_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR31_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR31_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR31_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR31_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR31_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR31_AMOUNT() != null)
						cellD.setCellValue(record2.getR31_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 32 / Col A =====
					row = sheet.getRow(31);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR32_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR32_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR32_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR32_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR32_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR32_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR32_AMOUNT() != null)
						cellD.setCellValue(record2.getR32_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 33 / Col A =====
					row = sheet.getRow(32);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR33_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR33_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR33_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR33_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR33_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR33_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR33_AMOUNT() != null)
						cellD.setCellValue(record2.getR33_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 37 / Col A =====
					row = sheet.getRow(36);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR37_NAME() != null)
						cellA.setCellValue(record3.getR37_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR37_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR37_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR37_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR37_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR37_AMOUNT() != null)
						cellD.setCellValue(record3.getR37_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 38 / Col A =====
					row = sheet.getRow(37);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR38_NAME() != null)
						cellA.setCellValue(record3.getR38_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR38_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR38_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR38_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR38_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR38_AMOUNT() != null)
						cellD.setCellValue(record3.getR38_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 39 / Col A =====
					row = sheet.getRow(38);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR39_NAME() != null)
						cellA.setCellValue(record3.getR39_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR39_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR39_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR39_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR39_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR39_AMOUNT() != null)
						cellD.setCellValue(record3.getR39_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 40 / Col A =====
					row = sheet.getRow(39);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR40_NAME() != null)
						cellA.setCellValue(record3.getR40_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR40_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR40_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR40_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR40_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR40_AMOUNT() != null)
						cellD.setCellValue(record3.getR40_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 41 / Col A =====
					row = sheet.getRow(40);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR41_NAME() != null)
						cellA.setCellValue(record3.getR41_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR41_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR41_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR41_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR41_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR41_AMOUNT() != null)
						cellD.setCellValue(record3.getR41_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 42 / Col A =====
					row = sheet.getRow(41);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR42_NAME() != null)
						cellA.setCellValue(record3.getR42_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR42_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR42_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR42_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR42_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR42_AMOUNT() != null)
						cellD.setCellValue(record3.getR42_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 43 / Col A =====
					row = sheet.getRow(42);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR43_NAME() != null)
						cellA.setCellValue(record3.getR43_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR43_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR43_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR43_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR43_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR43_AMOUNT() != null)
						cellD.setCellValue(record3.getR43_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 44 / Col A =====
					row = sheet.getRow(43);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR44_NAME() != null)
						cellA.setCellValue(record3.getR44_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR44_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR44_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR44_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR44_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR44_AMOUNT() != null)
						cellD.setCellValue(record3.getR44_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					

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
	
	
	public byte[] getExcelMDISB5ARCHIVAL(String filename, String reportId, String fromdate,
			String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if ("ARCHIVAL".equals(type) && version != null) {
		}
			List<MDISB5_Archival_Summary_Entity1> dataList1 = BRRS_MDISB5_Archival_Summary_Repo1
					.getdatabydateListarchival(dateformat.parse(todate), version);
			
			List<MDISB5_Archival_Summary_Entity2> dataList2 = BRRS_MDISB5_Archival_Summary_Repo2
					.getdatabydateListarchival(dateformat.parse(todate), version);
			
			List<MDISB5_Archival_Summary_Entity3> dataList3 = BRRS_MDISB5_Archival_Summary_Repo3
					.getdatabydateListarchival(dateformat.parse(todate), version);
		

			
		
		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for MDISB5 report. Returning empty result.");
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
			int startRow = 4;
			
			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					MDISB5_Archival_Summary_Entity1 record1 = dataList1.get(i);
					MDISB5_Archival_Summary_Entity2 record2 = dataList2.get(i);
					MDISB5_Archival_Summary_Entity3 record3 = dataList3.get(i);
					
					System.out.println("rownumber=" + (startRow + i));

					Row row;
					Cell cellA, cellB, cellC, cellD;
					CellStyle originalStyle;

					// ===== Row 5 / Col A =====
					row = sheet.getRow(4);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR5_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR5_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// ===== Row 5 / Col B =====
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR5_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR5_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R11 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR5_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR5_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R11 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR5_AMOUNT() != null)
						cellD.setCellValue(record1.getR5_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 6 / Col A =====
					row = sheet.getRow(5);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR6_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR6_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR6_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR6_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR6_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR6_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR6_AMOUNT() != null)
						cellD.setCellValue(record1.getR6_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 7 / Col A =====
					row = sheet.getRow(6);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR7_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR7_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR7_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR7_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR7_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR7_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR7_AMOUNT() != null)
						cellD.setCellValue(record1.getR7_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 8 / Col A =====
					row = sheet.getRow(7);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR8_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR8_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR8_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR8_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR8_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR8_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR8_AMOUNT() != null)
						cellD.setCellValue(record1.getR8_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 9 / Col A =====
					row = sheet.getRow(8);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR9_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR9_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR9_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR9_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR9_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR9_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR9_AMOUNT() != null)
						cellD.setCellValue(record1.getR9_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 10 / Col A =====
					row = sheet.getRow(9);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR10_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR10_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR10_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR10_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR10_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR10_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR10_AMOUNT() != null)
						cellD.setCellValue(record1.getR10_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 11 / Col A =====
					row = sheet.getRow(10);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR11_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR11_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR11_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR11_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR11_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR11_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR11_AMOUNT() != null)
						cellD.setCellValue(record1.getR11_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 12 / Col A =====
					row = sheet.getRow(11);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR12_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR12_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR12_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR12_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR12_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR12_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR12_AMOUNT() != null)
						cellD.setCellValue(record1.getR12_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 13 / Col A =====
					row = sheet.getRow(12);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR13_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR13_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR13_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR13_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR13_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR13_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR13_AMOUNT() != null)
						cellD.setCellValue(record1.getR13_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 14 / Col A =====
					row = sheet.getRow(13);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR14_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR14_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR14_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR14_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR14_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR14_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR14_AMOUNT() != null)
						cellD.setCellValue(record1.getR14_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 15 / Col A =====
					row = sheet.getRow(14);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR15_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR15_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR15_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR15_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR15_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR15_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR15_AMOUNT() != null)
						cellD.setCellValue(record1.getR15_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 20 / Col A =====
					row = sheet.getRow(19);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR20_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR20_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR20_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR20_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR20_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR20_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR20_AMOUNT() != null)
						cellD.setCellValue(record2.getR20_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 21 / Col A =====
					row = sheet.getRow(20);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR21_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR21_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR21_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR21_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR21_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR21_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR21_AMOUNT() != null)
						cellD.setCellValue(record2.getR21_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 22 / Col A =====
					row = sheet.getRow(21);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR22_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR22_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR22_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR22_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR22_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR22_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR22_AMOUNT() != null)
						cellD.setCellValue(record2.getR22_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 23 / Col A =====
					row = sheet.getRow(22);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR23_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR23_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR23_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR23_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR23_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR23_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR23_AMOUNT() != null)
						cellD.setCellValue(record2.getR23_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 24 / Col A =====
					row = sheet.getRow(23);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR24_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR24_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR24_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR24_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR24_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR24_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR24_AMOUNT() != null)
						cellD.setCellValue(record2.getR24_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 25 / Col A =====
					row = sheet.getRow(24);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR25_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR25_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR25_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR25_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR25_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR25_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR25_AMOUNT() != null)
						cellD.setCellValue(record2.getR25_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 26 / Col A =====
					row = sheet.getRow(25);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR26_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR26_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR26_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR26_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR26_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR26_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR26_AMOUNT() != null)
						cellD.setCellValue(record2.getR26_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 27 / Col A =====
					row = sheet.getRow(26);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR27_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR27_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR27_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR27_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR27_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR27_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR27_AMOUNT() != null)
						cellD.setCellValue(record2.getR27_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 28 / Col A =====
					row = sheet.getRow(27);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR28_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR28_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR28_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR28_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR28_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR28_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR28_AMOUNT() != null)
						cellD.setCellValue(record2.getR28_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 29 / Col A =====
					row = sheet.getRow(28);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR29_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR29_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR29_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR29_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR29_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR29_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR29_AMOUNT() != null)
						cellD.setCellValue(record2.getR29_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 30 / Col A =====
					row = sheet.getRow(29);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR30_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR30_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR30_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR30_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR30_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR30_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR30_AMOUNT() != null)
						cellD.setCellValue(record2.getR30_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 31 / Col A =====
					row = sheet.getRow(30);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR31_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR31_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR31_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR31_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR31_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR31_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR31_AMOUNT() != null)
						cellD.setCellValue(record2.getR31_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 32 / Col A =====
					row = sheet.getRow(31);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR32_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR32_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR32_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR32_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR32_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR32_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR32_AMOUNT() != null)
						cellD.setCellValue(record2.getR32_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 33 / Col A =====
					row = sheet.getRow(32);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR33_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR33_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR33_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR33_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR33_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR33_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR33_AMOUNT() != null)
						cellD.setCellValue(record2.getR33_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 37 / Col A =====
					row = sheet.getRow(36);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR37_NAME() != null)
						cellA.setCellValue(record3.getR37_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR37_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR37_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR37_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR37_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR37_AMOUNT() != null)
						cellD.setCellValue(record3.getR37_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 38 / Col A =====
					row = sheet.getRow(37);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR38_NAME() != null)
						cellA.setCellValue(record3.getR38_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR38_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR38_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR38_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR38_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR38_AMOUNT() != null)
						cellD.setCellValue(record3.getR38_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 39 / Col A =====
					row = sheet.getRow(38);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR39_NAME() != null)
						cellA.setCellValue(record3.getR39_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR39_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR39_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR39_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR39_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR39_AMOUNT() != null)
						cellD.setCellValue(record3.getR39_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 40 / Col A =====
					row = sheet.getRow(39);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR40_NAME() != null)
						cellA.setCellValue(record3.getR40_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR40_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR40_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR40_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR40_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR40_AMOUNT() != null)
						cellD.setCellValue(record3.getR40_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 41 / Col A =====
					row = sheet.getRow(40);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR41_NAME() != null)
						cellA.setCellValue(record3.getR41_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR41_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR41_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR41_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR41_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR41_AMOUNT() != null)
						cellD.setCellValue(record3.getR41_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 42 / Col A =====
					row = sheet.getRow(41);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR42_NAME() != null)
						cellA.setCellValue(record3.getR42_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR42_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR42_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR42_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR42_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR42_AMOUNT() != null)
						cellD.setCellValue(record3.getR42_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 43 / Col A =====
					row = sheet.getRow(42);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR43_NAME() != null)
						cellA.setCellValue(record3.getR43_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR43_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR43_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR43_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR43_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR43_AMOUNT() != null)
						cellD.setCellValue(record3.getR43_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 44 / Col A =====
					row = sheet.getRow(43);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR44_NAME() != null)
						cellA.setCellValue(record3.getR44_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR44_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR44_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR44_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR44_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR44_AMOUNT() != null)
						cellD.setCellValue(record3.getR44_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
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
	
	
	public List<Object[]> getMDISB5Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
		List<MDISB5_Archival_Summary_Entity1> latestArchivalList = 
				BRRS_MDISB5_Archival_Summary_Repo1.getdatabydateListWithVersionAll();

		if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
		for (MDISB5_Archival_Summary_Entity1 entity : latestArchivalList) {
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
		System.err.println("Error fetching MDISB5 Resub data: " + e.getMessage());
		e.printStackTrace();
		}
		return resubList;
		}
			
			
			//Archival View
			public List<Object[]> getMDISB5Archival() {
				List<Object[]> archivalList = new ArrayList<>();

				try {
					List<MDISB5_Archival_Summary_Entity1> repoData = BRRS_MDISB5_Archival_Summary_Repo1
							.getdatabydateListWithVersionAll();

					if (repoData != null && !repoData.isEmpty()) {
						for (MDISB5_Archival_Summary_Entity1 entity : repoData) {
							Object[] row = new Object[] {
									entity.getReportDate(), 
									entity.getReportVersion() 
							};
							archivalList.add(row);
						}

						System.out.println("Fetched " + archivalList.size() + " archival records");
						MDISB5_Archival_Summary_Entity1 first = repoData.get(0);
						System.out.println("Latest archival version: " + first.getReportVersion());
					} else {
						System.out.println("No archival data found.");
					}

				} catch (Exception e) {
					System.err.println("Error fetching MDISB5 Archival data: " + e.getMessage());
					e.printStackTrace();
				}

				return archivalList;
			}

			
			
			public void updateReportReSub(
					MDISB5_Summary_Entity1 updatedEntity1,
					MDISB5_Summary_Entity2 updatedEntity2,
					MDISB5_Summary_Entity3 updatedEntity3) {

		        System.out.println("Came to MDISB5 Resub Service");
		        System.out.println("Report Date: " + updatedEntity1.getReportDate());

		        Date reportDate = updatedEntity1.getReportDate();
		        int newVersion = 1;

		        try {
		            // 🔹 Fetch the latest archival version for this report date from Entity1
		            Optional<MDISB5_Archival_Summary_Entity1> latestArchivalOpt1 = BRRS_MDISB5_Archival_Summary_Repo1
		                    .getLatestArchivalVersionByDate(reportDate);

		            if (latestArchivalOpt1.isPresent()) {
		            	MDISB5_Archival_Summary_Entity1 latestArchival = latestArchivalOpt1.get();
		                try {
		                    newVersion = Integer.parseInt(latestArchival.getReportVersion()) + 1;
		                } catch (NumberFormatException e) {
		                    System.err.println("Invalid version format. Defaulting to version 1");
		                    newVersion = 1;
		                }
		            } else {
		                System.out.println("No previous archival found for date: " + reportDate);
		            }

		            // 🔹 Prevent duplicate version number in Repo1
		            boolean exists = BRRS_MDISB5_Archival_Summary_Repo1
		                    .findByReportDateAndReportVersion(reportDate, String.valueOf(newVersion))
		                    .isPresent();

		            if (exists) {
		                throw new RuntimeException("⚠ Version " + newVersion + " already exists for report date " + reportDate);
		            }

		            // Copy data from summary to archival entities for all 3 entities
		            MDISB5_Archival_Summary_Entity1 archivalEntity1 = new MDISB5_Archival_Summary_Entity1();
		            MDISB5_Archival_Summary_Entity2 archivalEntity2 = new MDISB5_Archival_Summary_Entity2();
		            MDISB5_Archival_Summary_Entity3 archivalEntity3 = new MDISB5_Archival_Summary_Entity3();

		            org.springframework.beans.BeanUtils.copyProperties(updatedEntity1, archivalEntity1);
		            org.springframework.beans.BeanUtils.copyProperties(updatedEntity2, archivalEntity2);
		            org.springframework.beans.BeanUtils.copyProperties(updatedEntity3, archivalEntity3);

		            // Set common fields
		            Date now = new Date();
		            archivalEntity1.setReportDate(reportDate);
		            archivalEntity2.setReportDate(reportDate);
		            archivalEntity3.setReportDate(reportDate);

		            archivalEntity1.setReportVersion(String.valueOf(newVersion));
		            archivalEntity2.setReportVersion(String.valueOf(newVersion));
		            archivalEntity3.setReportVersion(String.valueOf(newVersion));

		            archivalEntity1.setREPORT_RESUBDATE(now);
		            archivalEntity2.setREPORT_RESUBDATE(now);
		            archivalEntity3.setREPORT_RESUBDATE(now);

		            System.out.println("Saving new archival version: " + newVersion);

		            // Save to all three archival repositories
		            BRRS_MDISB5_Archival_Summary_Repo1.save(archivalEntity1);
		            BRRS_MDISB5_Archival_Summary_Repo2.save(archivalEntity2);
		            BRRS_MDISB5_Archival_Summary_Repo3.save(archivalEntity3);

		            System.out.println("Saved archival version successfully: " + newVersion);

		        } catch (Exception e) {
		            e.printStackTrace();
		            throw new RuntimeException("Error while creating MDISB5 archival resubmission record", e);
		        }
		    }
			
			
			
		    public byte[] BRRS_MDISB5ResubExcel(String filename, String reportId, String fromdate,
		            String todate, String currency, String dtltype,
		            String type, String version) throws Exception {

		        logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

		        if (type.equals("RESUB") & version != null) {

		        }

		        List<MDISB5_Archival_Summary_Entity1> dataList = BRRS_MDISB5_Archival_Summary_Repo1
		                .getdatabydateListarchival(dateformat.parse(todate), version);
		        List<MDISB5_Archival_Summary_Entity2> dataList1 = BRRS_MDISB5_Archival_Summary_Repo2
		                .getdatabydateListarchival(dateformat.parse(todate), version);
		        List<MDISB5_Archival_Summary_Entity3> dataList2 = BRRS_MDISB5_Archival_Summary_Repo3
		                .getdatabydateListarchival(dateformat.parse(todate), version);

		        if (dataList.isEmpty()) {
		            logger.warn("Service: No data found for MDISB5 report. Returning empty result.");
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
		            int startRow = 9;

		            if (!dataList.isEmpty()) {
		                for (int i = 0; i < dataList.size(); i++) {

		                	MDISB5_Archival_Summary_Entity1 record1 = dataList.get(i);
		                	MDISB5_Archival_Summary_Entity2 record2 = dataList1.get(i);
		                	MDISB5_Archival_Summary_Entity3 record3 = dataList2.get(i);
		                    System.out.println("rownumber=" + startRow + i);

							Row row;
							Cell cellA, cellB, cellC, cellD;
							CellStyle originalStyle;

							// ===== Row 5 / Col A =====
							row = sheet.getRow(4);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record1.getR5_NAME_OF_SHAREHOLDER() != null)
								cellA.setCellValue(record1.getR5_NAME_OF_SHAREHOLDER()); // String directly
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// ===== Row 5 / Col B =====
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR5_PERCENTAGE_SHAREHOLDING() != null)
								cellB.setCellValue(record1.getR5_PERCENTAGE_SHAREHOLDING().doubleValue());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R11 / Col C =====

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR5_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record1.getR5_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R11 / Col D =====

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR5_AMOUNT() != null)
								cellD.setCellValue(record1.getR5_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 6 / Col A =====
							row = sheet.getRow(5);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record1.getR6_NAME_OF_SHAREHOLDER() != null)
								cellA.setCellValue(record1.getR6_NAME_OF_SHAREHOLDER()); // String directly
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR6_PERCENTAGE_SHAREHOLDING() != null)
								cellB.setCellValue(record1.getR6_PERCENTAGE_SHAREHOLDING().doubleValue());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR6_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record1.getR6_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR6_AMOUNT() != null)
								cellD.setCellValue(record1.getR6_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 7 / Col A =====
							row = sheet.getRow(6);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record1.getR7_NAME_OF_SHAREHOLDER() != null)
								cellA.setCellValue(record1.getR7_NAME_OF_SHAREHOLDER()); // String directly
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR7_PERCENTAGE_SHAREHOLDING() != null)
								cellB.setCellValue(record1.getR7_PERCENTAGE_SHAREHOLDING().doubleValue());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR7_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record1.getR7_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR7_AMOUNT() != null)
								cellD.setCellValue(record1.getR7_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							// ===== Row 8 / Col A =====
							row = sheet.getRow(7);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record1.getR8_NAME_OF_SHAREHOLDER() != null)
								cellA.setCellValue(record1.getR8_NAME_OF_SHAREHOLDER()); // String directly
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR8_PERCENTAGE_SHAREHOLDING() != null)
								cellB.setCellValue(record1.getR8_PERCENTAGE_SHAREHOLDING().doubleValue());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR8_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record1.getR8_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR8_AMOUNT() != null)
								cellD.setCellValue(record1.getR8_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 9 / Col A =====
							row = sheet.getRow(8);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record1.getR9_NAME_OF_SHAREHOLDER() != null)
								cellA.setCellValue(record1.getR9_NAME_OF_SHAREHOLDER()); // String directly
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR9_PERCENTAGE_SHAREHOLDING() != null)
								cellB.setCellValue(record1.getR9_PERCENTAGE_SHAREHOLDING().doubleValue());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR9_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record1.getR9_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR9_AMOUNT() != null)
								cellD.setCellValue(record1.getR9_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 10 / Col A =====
							row = sheet.getRow(9);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record1.getR10_NAME_OF_SHAREHOLDER() != null)
								cellA.setCellValue(record1.getR10_NAME_OF_SHAREHOLDER()); // String directly
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR10_PERCENTAGE_SHAREHOLDING() != null)
								cellB.setCellValue(record1.getR10_PERCENTAGE_SHAREHOLDING().doubleValue());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR10_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record1.getR10_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR10_AMOUNT() != null)
								cellD.setCellValue(record1.getR10_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 11 / Col A =====
							row = sheet.getRow(10);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record1.getR11_NAME_OF_SHAREHOLDER() != null)
								cellA.setCellValue(record1.getR11_NAME_OF_SHAREHOLDER()); // String directly
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR11_PERCENTAGE_SHAREHOLDING() != null)
								cellB.setCellValue(record1.getR11_PERCENTAGE_SHAREHOLDING().doubleValue());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR11_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record1.getR11_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR11_AMOUNT() != null)
								cellD.setCellValue(record1.getR11_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							// ===== Row 12 / Col A =====
							row = sheet.getRow(11);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record1.getR12_NAME_OF_SHAREHOLDER() != null)
								cellA.setCellValue(record1.getR12_NAME_OF_SHAREHOLDER()); // String directly
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR12_PERCENTAGE_SHAREHOLDING() != null)
								cellB.setCellValue(record1.getR12_PERCENTAGE_SHAREHOLDING().doubleValue());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR12_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record1.getR12_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR12_AMOUNT() != null)
								cellD.setCellValue(record1.getR12_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 13 / Col A =====
							row = sheet.getRow(12);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record1.getR13_NAME_OF_SHAREHOLDER() != null)
								cellA.setCellValue(record1.getR13_NAME_OF_SHAREHOLDER()); // String directly
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR13_PERCENTAGE_SHAREHOLDING() != null)
								cellB.setCellValue(record1.getR13_PERCENTAGE_SHAREHOLDING().doubleValue());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR13_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record1.getR13_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR13_AMOUNT() != null)
								cellD.setCellValue(record1.getR13_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 14 / Col A =====
							row = sheet.getRow(13);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record1.getR14_NAME_OF_SHAREHOLDER() != null)
								cellA.setCellValue(record1.getR14_NAME_OF_SHAREHOLDER()); // String directly
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR14_PERCENTAGE_SHAREHOLDING() != null)
								cellB.setCellValue(record1.getR14_PERCENTAGE_SHAREHOLDING().doubleValue());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR14_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record1.getR14_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR14_AMOUNT() != null)
								cellD.setCellValue(record1.getR14_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							// ===== Row 15 / Col A =====
							row = sheet.getRow(14);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record1.getR15_NAME_OF_SHAREHOLDER() != null)
								cellA.setCellValue(record1.getR15_NAME_OF_SHAREHOLDER()); // String directly
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR15_PERCENTAGE_SHAREHOLDING() != null)
								cellB.setCellValue(record1.getR15_PERCENTAGE_SHAREHOLDING().doubleValue());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR15_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record1.getR15_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR15_AMOUNT() != null)
								cellD.setCellValue(record1.getR15_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 20 / Col A =====
							row = sheet.getRow(19);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR20_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR20_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR20_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR20_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR20_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR20_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR20_AMOUNT() != null)
								cellD.setCellValue(record2.getR20_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 21 / Col A =====
							row = sheet.getRow(20);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR21_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR21_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR21_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR21_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR21_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR21_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR21_AMOUNT() != null)
								cellD.setCellValue(record2.getR21_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							// ===== Row 22 / Col A =====
							row = sheet.getRow(21);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR22_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR22_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR22_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR22_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR22_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR22_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR22_AMOUNT() != null)
								cellD.setCellValue(record2.getR22_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 23 / Col A =====
							row = sheet.getRow(22);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR23_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR23_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR23_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR23_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR23_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR23_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR23_AMOUNT() != null)
								cellD.setCellValue(record2.getR23_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							// ===== Row 24 / Col A =====
							row = sheet.getRow(23);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR24_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR24_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR24_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR24_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR24_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR24_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR24_AMOUNT() != null)
								cellD.setCellValue(record2.getR24_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 25 / Col A =====
							row = sheet.getRow(24);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR25_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR25_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR25_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR25_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR25_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR25_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR25_AMOUNT() != null)
								cellD.setCellValue(record2.getR25_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 26 / Col A =====
							row = sheet.getRow(25);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR26_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR26_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR26_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR26_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR26_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR26_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR26_AMOUNT() != null)
								cellD.setCellValue(record2.getR26_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 27 / Col A =====
							row = sheet.getRow(26);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR27_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR27_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR27_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR27_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR27_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR27_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR27_AMOUNT() != null)
								cellD.setCellValue(record2.getR27_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 28 / Col A =====
							row = sheet.getRow(27);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR28_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR28_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR28_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR28_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR28_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR28_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR28_AMOUNT() != null)
								cellD.setCellValue(record2.getR28_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 29 / Col A =====
							row = sheet.getRow(28);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR29_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR29_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR29_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR29_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR29_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR29_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR29_AMOUNT() != null)
								cellD.setCellValue(record2.getR29_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							// ===== Row 30 / Col A =====
							row = sheet.getRow(29);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR30_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR30_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR30_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR30_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR30_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR30_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR30_AMOUNT() != null)
								cellD.setCellValue(record2.getR30_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							// ===== Row 31 / Col A =====
							row = sheet.getRow(30);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR31_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR31_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR31_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR31_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR31_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR31_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR31_AMOUNT() != null)
								cellD.setCellValue(record2.getR31_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							// ===== Row 32 / Col A =====
							row = sheet.getRow(31);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR32_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR32_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR32_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR32_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR32_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR32_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR32_AMOUNT() != null)
								cellD.setCellValue(record2.getR32_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							// ===== Row 33 / Col A =====
							row = sheet.getRow(32);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR33_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR33_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR33_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR33_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR33_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR33_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR33_AMOUNT() != null)
								cellD.setCellValue(record2.getR33_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							// ===== Row 37 / Col A =====
							row = sheet.getRow(36);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR37_NAME() != null)
								cellA.setCellValue(record3.getR37_NAME()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR37_DESIGNATION_OR_POSITION() != null)
								cellB.setCellValue(record3.getR37_DESIGNATION_OR_POSITION());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record3.getR37_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record3.getR37_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record3.getR37_AMOUNT() != null)
								cellD.setCellValue(record3.getR37_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 38 / Col A =====
							row = sheet.getRow(37);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR38_NAME() != null)
								cellA.setCellValue(record3.getR38_NAME()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR38_DESIGNATION_OR_POSITION() != null)
								cellB.setCellValue(record3.getR38_DESIGNATION_OR_POSITION());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record3.getR38_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record3.getR38_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record3.getR38_AMOUNT() != null)
								cellD.setCellValue(record3.getR38_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 39 / Col A =====
							row = sheet.getRow(38);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR39_NAME() != null)
								cellA.setCellValue(record3.getR39_NAME()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR39_DESIGNATION_OR_POSITION() != null)
								cellB.setCellValue(record3.getR39_DESIGNATION_OR_POSITION());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record3.getR39_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record3.getR39_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record3.getR39_AMOUNT() != null)
								cellD.setCellValue(record3.getR39_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 40 / Col A =====
							row = sheet.getRow(39);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR40_NAME() != null)
								cellA.setCellValue(record3.getR40_NAME()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR40_DESIGNATION_OR_POSITION() != null)
								cellB.setCellValue(record3.getR40_DESIGNATION_OR_POSITION());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record3.getR40_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record3.getR40_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record3.getR40_AMOUNT() != null)
								cellD.setCellValue(record3.getR40_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 41 / Col A =====
							row = sheet.getRow(40);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR41_NAME() != null)
								cellA.setCellValue(record3.getR41_NAME()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR41_DESIGNATION_OR_POSITION() != null)
								cellB.setCellValue(record3.getR41_DESIGNATION_OR_POSITION());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record3.getR41_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record3.getR41_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record3.getR41_AMOUNT() != null)
								cellD.setCellValue(record3.getR41_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 42 / Col A =====
							row = sheet.getRow(41);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR42_NAME() != null)
								cellA.setCellValue(record3.getR42_NAME()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR42_DESIGNATION_OR_POSITION() != null)
								cellB.setCellValue(record3.getR42_DESIGNATION_OR_POSITION());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record3.getR42_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record3.getR42_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record3.getR42_AMOUNT() != null)
								cellD.setCellValue(record3.getR42_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 43 / Col A =====
							row = sheet.getRow(42);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR43_NAME() != null)
								cellA.setCellValue(record3.getR43_NAME()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR43_DESIGNATION_OR_POSITION() != null)
								cellB.setCellValue(record3.getR43_DESIGNATION_OR_POSITION());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record3.getR43_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record3.getR43_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record3.getR43_AMOUNT() != null)
								cellD.setCellValue(record3.getR43_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 44 / Col A =====
							row = sheet.getRow(43);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR44_NAME() != null)
								cellA.setCellValue(record3.getR44_NAME()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR44_DESIGNATION_OR_POSITION() != null)
								cellB.setCellValue(record3.getR44_DESIGNATION_OR_POSITION());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record3.getR44_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record3.getR44_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record3.getR44_AMOUNT() != null)
								cellD.setCellValue(record3.getR44_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
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
