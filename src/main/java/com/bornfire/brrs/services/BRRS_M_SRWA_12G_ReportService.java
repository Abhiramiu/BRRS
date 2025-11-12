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

import java.lang.reflect.Method;
import java.math.BigDecimal;


import com.bornfire.brrs.entities.M_SRWA_12G_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12G_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12G_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12G_Archival_Summary_Repo;


@Component
@Service
public class BRRS_M_SRWA_12G_ReportService {


	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SRWA_12G_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	BRRS_M_SRWA_12G_Summary_Repo	BRRS_M_SRWA_12G_Summary_Repo;
	
	@Autowired
	BRRS_M_SRWA_12G_Archival_Summary_Repo	BRRS_M_SRWA_12G_Archival_Summary_Repo;
				

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_SRWA_12GView(String reportId, String fromdate, String todate, 
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
            List<M_SRWA_12G_Archival_Summary_Entity> T1Master = 
                BRRS_M_SRWA_12G_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 2: RESUB ----------
        else if ("RESUB".equalsIgnoreCase(type) && version != null) {
            List<M_SRWA_12G_Archival_Summary_Entity> T1Master =
                BRRS_M_SRWA_12G_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 3: NORMAL ----------
        else {
            List<M_SRWA_12G_Summary_Entity> T1Master = 
                BRRS_M_SRWA_12G_Summary_Repo.getdatabydateListWithVersion(todate);
            System.out.println("T1Master Size "+T1Master.size());
            mv.addObject("reportsummary", T1Master);
        }

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_SRWA_12G");
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
	
	
	public void updateReport(M_SRWA_12G_Summary_Entity updatedEntity) {
	    System.out.println("Came to services1");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    M_SRWA_12G_Summary_Entity existing = BRRS_M_SRWA_12G_Summary_Repo
				.findTopByReportDateOrderByReportVersionDesc(updatedEntity.getReport_date())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

	    try {
	        // 1️⃣ Loop from R11 to R60 and copy fields
	        for (int i = 11; i <= 60; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = { "credit_rating", "security_firm", "exposure_amount",
	                    "risk_weight", "rating_agency" };

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_SRWA_12G_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_SRWA_12G_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        // 2️⃣ Handle formula column
	        for (int i = 11; i <= 60; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = { "risk_weighted_amount" };

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_SRWA_12G_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_SRWA_12G_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }
	        
	        String[] totalFields = { "exposure_amount", "risk_weighted_amount" };
	        for (String field : totalFields) {
	            String getterName = "getR61_" + field;
	            String setterName = "setR61_" + field;

	            try {
	                Method getter = M_SRWA_12G_Summary_Entity.class.getMethod(getterName);
	                Method setter = M_SRWA_12G_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                Object newValue = getter.invoke(updatedEntity);
	                setter.invoke(existing, newValue);

	            } catch (NoSuchMethodException e) {
	                // Skip if not present
	                continue;
	            }
	        }

	        
	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // 3️⃣ Save updated entity
	    BRRS_M_SRWA_12G_Summary_Repo.save(existing);
	}

	
//	public List<Object> getM_SRWA_12GResub() {
//	    List<Object> M_SRWA_12GResub = new ArrayList<>();
//	    try {
//	        List<Object> list1 = BRRS_M_SRWA_12G_Resub_Summary_Repo1.getM_SRWA_12GResub();
//	        List<Object> list2 = BRRS_M_SRWA_12G_Resub_Summary_Repo2.getM_SRWA_12GResub();
//	        List<Object> list3 = BRRS_M_SRWA_12G_Resub_Summary_Repo3.getM_SRWA_12GResub();
//
//	        M_SRWA_12GResub.addAll(list1);
//	        M_SRWA_12GResub.addAll(list2);
//	        M_SRWA_12GResub.addAll(list3);
//
//	        System.out.println("Total combined size: " + M_SRWA_12GResub.size());
//	    } catch (Exception e) {
//	        System.err.println("Error fetching M_SRWA_12G Resub data: " + e.getMessage());
//	        e.printStackTrace();
//	    }
//	    return M_SRWA_12GResub;
//	}
//	
//	public void updateReportResub1(M_SRWA_12G_Resub_Summary_Entity1 updatedEntity) {
//	    System.out.println("Came to services1");
//	    System.out.println("Report Date: " + updatedEntity.getReport_date());
//
//	    M_SRWA_12G_Resub_Summary_Entity1 existing = BRRS_M_SRWA_12G_Resub_Summary_Repo1.findById(updatedEntity.getReport_date())
//	            .orElseThrow(() -> new RuntimeException(
//	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));
//
//	    try {
//	        // 1️⃣ Loop from R11 to R16 and copy fields
//	        for (int i = 11; i <= 16; i++) {
//	            String prefix = "R" + i + "_";
//
//	            String[] fields = { "currency", "net_spot_position", "net_forward_position", "guarantees",
//	                                "net_future_inc_or_exp", "net_delta_wei_fx_opt_posi", "other_items",
//	                                "net_long_position", "or", "net_short_position" };
//
//	            for (String field : fields) {
//	                String getterName = "get" + prefix + field;
//	                String setterName = "set" + prefix + field;
//
//	                try {
//	                    Method getter = M_SRWA_12G_Resub_Summary_Entity1.class.getMethod(getterName);
//	                    Method setter = M_SRWA_12G_Resub_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());
//
//	                    Object newValue = getter.invoke(updatedEntity);
//	                    setter.invoke(existing, newValue);
//
//	                } catch (NoSuchMethodException e) {
//	                    // Skip missing fields
//	                    continue;
//	                }
//	            }
//	        }
//
//	        // 2️⃣ Handle R17 totals
//	        String[] totalFields = { "net_long_position", "net_short_position" };
//	        for (String field : totalFields) {
//	            String getterName = "getR17_" + field;
//	            String setterName = "setR17_" + field;
//
//	            try {
//	                Method getter = M_SRWA_12G_Resub_Summary_Entity1.class.getMethod(getterName);
//	                Method setter = M_SRWA_12G_Resub_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());
//
//	                Object newValue = getter.invoke(updatedEntity);
//	                setter.invoke(existing, newValue);
//
//	            } catch (NoSuchMethodException e) {
//	                // Skip if not present
//	                continue;
//	            }
//	        }
//
//	    } catch (Exception e) {
//	        throw new RuntimeException("Error while updating report fields", e);
//	    }
//
//	    // 3️⃣ Save updated entity
//	    BRRS_M_SRWA_12G_Resub_Summary_Repo1.save(existing);
//	}
//
//	public void updateReportResub2(M_SRWA_12G_Resub_Summary_Entity2 updatedEntity) {
//	    System.out.println("Came to services2");
//	    System.out.println("Report Date: " + updatedEntity.getReport_date());
//
//	    M_SRWA_12G_Resub_Summary_Entity2 existing = BRRS_M_SRWA_12G_Resub_Summary_Repo2.findById(updatedEntity.getReport_date())
//	            .orElseThrow(() -> new RuntimeException(
//	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));
//
//	    try {
//	        // 1️⃣ Loop from R11 to R50 and copy fields
//	        for (int i = 21; i <= 22; i++) {
//	            String prefix = "R" + i + "_";
//
//	            String[] fields = { "long", "short" };
//
//	            for (String field : fields) {
//	                String getterName = "get" + prefix + field;
//	                String setterName = "set" + prefix + field;
//
//	                try {
//	                    Method getter = M_SRWA_12G_Resub_Summary_Entity2.class.getMethod(getterName);
//	                    Method setter = M_SRWA_12G_Resub_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());
//
//	                    Object newValue = getter.invoke(updatedEntity);
//	                    setter.invoke(existing, newValue);
//
//	                } catch (NoSuchMethodException e) {
//	                    // Skip missing fields
//	                    continue;
//	                }
//	            }
//	            String[] formulaFields = { "total_gross_long_short", "net_position" };
//	            for (String field : formulaFields) {
//	                String getterName = "get" + prefix + field;
//	                String setterName = "set" + prefix + field;
//
//	                try {
//	                    Method getter = M_SRWA_12G_Resub_Summary_Entity2.class.getMethod(getterName);
//	                    Method setter = M_SRWA_12G_Resub_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());
//
//	                    Object newValue = getter.invoke(updatedEntity);
//	                    setter.invoke(existing, newValue);
//
//	                } catch (NoSuchMethodException e) {
//	                    continue;
//	                }
//	            }
//	        }
//
//	        // 2️⃣ Handle R23 totals
//	            String getterName = "getR23_net_position";
//	            String setterName = "setR23_net_position";
//
//	            try {
//	                Method getter = M_SRWA_12G_Resub_Summary_Entity2.class.getMethod(getterName);
//	                Method setter = M_SRWA_12G_Resub_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());
//
//	                Object newValue = getter.invoke(updatedEntity);
//	                setter.invoke(existing, newValue);
//
//	            } catch (NoSuchMethodException e) {
//	                // Skip if not present
//	                //continue;
//	            }
//	        
//
//	    } catch (Exception e) {
//	        throw new RuntimeException("Error while updating report fields", e);
//	    }
//
//	    // 3️⃣ Save updated entity
//	    BRRS_M_SRWA_12G_Resub_Summary_Repo2.save(existing);
//	}
//
//	public void updateReportResub3(M_SRWA_12G_Resub_Summary_Entity3 updatedEntity) {
//	    System.out.println("Came to services3");
//	    System.out.println("Report Date: " + updatedEntity.getReport_date());
//
//	    M_SRWA_12G_Resub_Summary_Entity3 existing = BRRS_M_SRWA_12G_Resub_Summary_Repo3.findById(updatedEntity.getReport_date())
//	            .orElseThrow(() -> new RuntimeException(
//	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));
//
//
//	    try {
//
//	            String[] fields = {"greater_net_long_or_short", "abs_value_net_gold_posi", "capital_require", "capital_charge"};
//
//	            for (String field : fields) {
//	                String getterName = "getR29_" + field;
//	                String setterName = "setR29_" + field;
//
//	                try {
//	                    Method getter = M_SRWA_12G_Resub_Summary_Entity3.class.getMethod(getterName);
//	                    Method setter = M_SRWA_12G_Resub_Summary_Entity3.class.getMethod(setterName, getter.getReturnType());
//
//	                    Object newValue = getter.invoke(updatedEntity);
//	                    setter.invoke(existing, newValue);
//
//	                } catch (NoSuchMethodException e) {
//	                    // Skip missing fields
//	                    continue;
//	                }
//	            }
//	    
//	    }catch (Exception e) {
//	        throw new RuntimeException("Error while updating report fields", e);
//	    }
//
//	    // 3️⃣ Save updated entity
//	    BRRS_M_SRWA_12G_Resub_Summary_Repo3.save(existing);
//}
//



	public byte[] getM_SRWA_12GExcel(String filename, String reportId, String fromdate, String todate, String currency,
									 String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

		// Convert string to Date
		Date reportDate = dateformat.parse(todate);

    // ARCHIVAL check
    if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
        logger.info("Service: Generating ARCHIVAL report for version {}", version);
        return getExcelM_SRWA_12GARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
    }
    // RESUB check
    else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
        logger.info("Service: Generating RESUB report for version {}", version);

       
        List<M_SRWA_12G_Archival_Summary_Entity> T1Master =
                BRRS_M_SRWA_12G_Archival_Summary_Repo.getdatabydateListarchival(reportDate, version);

        // Generate Excel for RESUB
        return BRRS_M_SRWA_12GResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
    }




		// Default (LIVE) case
		List<M_SRWA_12G_Summary_Entity> dataList1 = BRRS_M_SRWA_12G_Summary_Repo.getdatabydateList(reportDate);

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
			        M_SRWA_12G_Summary_Entity record1 = dataList1.get(i);
			        System.out.println("rownumber=" + (startRow + i));
					
				       
			       Row row;
			        Cell cellB, cellC, cellD, cellE, cellF;
			        CellStyle originalStyle;

			     
			        
			        
			        // ===== Row 11 / Col B =====
					row = sheet.getRow(10);
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
			// ✅ Handle String value 
					if (record1.getR11_security_firm() != null)
					cellB.setCellValue(record1.getR11_security_firm()); // String directly 
					else cellB.setCellValue(""); 
					cellB.setCellStyle(originalStyle);


					// ===== R11 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR11_credit_rating() != null) 
			        cellC.setCellValue(record1.getR11_credit_rating().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);


					// ===== R11 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_rating_agency() != null) 
			        cellD.setCellValue(record1.getR11_rating_agency()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R11 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			        if (record1.getR11_exposure_amount() != null) 
			        cellE.setCellValue(record1.getR11_exposure_amount().doubleValue());
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);
			        
					// ===== R11 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			        if (record1.getR11_risk_weight() != null) 
			        cellF.setCellValue(record1.getR11_risk_weight().doubleValue());
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);
			     
			     // ===== Row 12 / Col B =====
                    row = sheet.getRow(11);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR12_security_firm() != null)
                        cellB.setCellValue(record1.getR12_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R12 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR12_credit_rating() != null)
                        cellC.setCellValue(record1.getR12_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R12 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR12_rating_agency() != null)
                        cellD.setCellValue(record1.getR12_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R12 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR12_exposure_amount() != null)
                        cellE.setCellValue(record1.getR12_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R12 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR12_risk_weight() != null)
                        cellF.setCellValue(record1.getR12_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 13 / Col B =====
                    row = sheet.getRow(12);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR13_security_firm() != null)
                        cellB.setCellValue(record1.getR13_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R13 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR13_credit_rating() != null)
                        cellC.setCellValue(record1.getR13_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R13 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR13_rating_agency() != null)
                        cellD.setCellValue(record1.getR13_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R13 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR13_exposure_amount() != null)
                        cellE.setCellValue(record1.getR13_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R13 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR13_risk_weight() != null)
                        cellF.setCellValue(record1.getR13_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 14 / Col B =====
                    row = sheet.getRow(13);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR14_security_firm() != null)
                        cellB.setCellValue(record1.getR14_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R14 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR14_credit_rating() != null)
                        cellC.setCellValue(record1.getR14_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R14 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR14_rating_agency() != null)
                        cellD.setCellValue(record1.getR14_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R14 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR14_exposure_amount() != null)
                        cellE.setCellValue(record1.getR14_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R14 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR14_risk_weight() != null)
                        cellF.setCellValue(record1.getR14_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 15 / Col B =====
                    row = sheet.getRow(14);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR15_security_firm() != null)
                        cellB.setCellValue(record1.getR15_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R15 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR15_credit_rating() != null)
                        cellC.setCellValue(record1.getR15_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R15 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR15_rating_agency() != null)
                        cellD.setCellValue(record1.getR15_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R15 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR15_exposure_amount() != null)
                        cellE.setCellValue(record1.getR15_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R15 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR15_risk_weight() != null)
                        cellF.setCellValue(record1.getR15_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 16 / Col B =====
                    row = sheet.getRow(15);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR16_security_firm() != null)
                        cellB.setCellValue(record1.getR16_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R16 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR16_credit_rating() != null)
                        cellC.setCellValue(record1.getR16_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R16 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR16_rating_agency() != null)
                        cellD.setCellValue(record1.getR16_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R16 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR16_exposure_amount() != null)
                        cellE.setCellValue(record1.getR16_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R16 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR16_risk_weight() != null)
                        cellF.setCellValue(record1.getR16_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 17 / Col B =====
                    row = sheet.getRow(16);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR17_security_firm() != null)
                        cellB.setCellValue(record1.getR17_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R17 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR17_credit_rating() != null)
                        cellC.setCellValue(record1.getR17_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R17 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR17_rating_agency() != null)
                        cellD.setCellValue(record1.getR17_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R17 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR17_exposure_amount() != null)
                        cellE.setCellValue(record1.getR17_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R17 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR17_risk_weight() != null)
                        cellF.setCellValue(record1.getR17_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 18 / Col B =====
                    row = sheet.getRow(17);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR18_security_firm() != null)
                        cellB.setCellValue(record1.getR18_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R18 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR18_credit_rating() != null)
                        cellC.setCellValue(record1.getR18_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R18 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR18_rating_agency() != null)
                        cellD.setCellValue(record1.getR18_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R18 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR18_exposure_amount() != null)
                        cellE.setCellValue(record1.getR18_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R18 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR18_risk_weight() != null)
                        cellF.setCellValue(record1.getR18_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 19 / Col B =====
                    row = sheet.getRow(18);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR19_security_firm() != null)
                        cellB.setCellValue(record1.getR19_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R19 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR19_credit_rating() != null)
                        cellC.setCellValue(record1.getR19_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R19 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR19_rating_agency() != null)
                        cellD.setCellValue(record1.getR19_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R19 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR19_exposure_amount() != null)
                        cellE.setCellValue(record1.getR19_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R19 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR19_risk_weight() != null)
                        cellF.setCellValue(record1.getR19_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 20 / Col B =====
                    row = sheet.getRow(19);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR20_security_firm() != null)
                        cellB.setCellValue(record1.getR20_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R20 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR20_credit_rating() != null)
                        cellC.setCellValue(record1.getR20_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R20 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR20_rating_agency() != null)
                        cellD.setCellValue(record1.getR20_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R20 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR20_exposure_amount() != null)
                        cellE.setCellValue(record1.getR20_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R20 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR20_risk_weight() != null)
                        cellF.setCellValue(record1.getR20_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 21 / Col B =====
                    row = sheet.getRow(20);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR21_security_firm() != null)
                        cellB.setCellValue(record1.getR21_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R21 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR21_credit_rating() != null)
                        cellC.setCellValue(record1.getR21_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R21 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR21_rating_agency() != null)
                        cellD.setCellValue(record1.getR21_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R21 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR21_exposure_amount() != null)
                        cellE.setCellValue(record1.getR21_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R21 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR21_risk_weight() != null)
                        cellF.setCellValue(record1.getR21_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 22 / Col B =====
                    row = sheet.getRow(21);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR22_security_firm() != null)
                        cellB.setCellValue(record1.getR22_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R22 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR22_credit_rating() != null)
                        cellC.setCellValue(record1.getR22_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R22 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR22_rating_agency() != null)
                        cellD.setCellValue(record1.getR22_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R22 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR22_exposure_amount() != null)
                        cellE.setCellValue(record1.getR22_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R22 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR22_risk_weight() != null)
                        cellF.setCellValue(record1.getR22_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 23 / Col B =====
                    row = sheet.getRow(22);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR23_security_firm() != null)
                        cellB.setCellValue(record1.getR23_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R23 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR23_credit_rating() != null)
                        cellC.setCellValue(record1.getR23_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R23 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR23_rating_agency() != null)
                        cellD.setCellValue(record1.getR23_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R23 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR23_exposure_amount() != null)
                        cellE.setCellValue(record1.getR23_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R23 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR23_risk_weight() != null)
                        cellF.setCellValue(record1.getR23_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 24 / Col B =====
                    row = sheet.getRow(23);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR24_security_firm() != null)
                        cellB.setCellValue(record1.getR24_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R24 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR24_credit_rating() != null)
                        cellC.setCellValue(record1.getR24_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R24 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR24_rating_agency() != null)
                        cellD.setCellValue(record1.getR24_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R24 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR24_exposure_amount() != null)
                        cellE.setCellValue(record1.getR24_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R24 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR24_risk_weight() != null)
                        cellF.setCellValue(record1.getR24_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 25 / Col B =====
                    row = sheet.getRow(24);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR25_security_firm() != null)
                        cellB.setCellValue(record1.getR25_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R25 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR25_credit_rating() != null)
                        cellC.setCellValue(record1.getR25_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R25 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR25_rating_agency() != null)
                        cellD.setCellValue(record1.getR25_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R25 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR25_exposure_amount() != null)
                        cellE.setCellValue(record1.getR25_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R25 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR25_risk_weight() != null)
                        cellF.setCellValue(record1.getR25_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 26 / Col B =====
                    row = sheet.getRow(25);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR26_security_firm() != null)
                        cellB.setCellValue(record1.getR26_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R26 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR26_credit_rating() != null)
                        cellC.setCellValue(record1.getR26_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R26 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR26_rating_agency() != null)
                        cellD.setCellValue(record1.getR26_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R26 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR26_exposure_amount() != null)
                        cellE.setCellValue(record1.getR26_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R26 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR26_risk_weight() != null)
                        cellF.setCellValue(record1.getR26_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 27 / Col B =====
                    row = sheet.getRow(26);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR27_security_firm() != null)
                        cellB.setCellValue(record1.getR27_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R27 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR27_credit_rating() != null)
                        cellC.setCellValue(record1.getR27_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R27 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR27_rating_agency() != null)
                        cellD.setCellValue(record1.getR27_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R27 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR27_exposure_amount() != null)
                        cellE.setCellValue(record1.getR27_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R27 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR27_risk_weight() != null)
                        cellF.setCellValue(record1.getR27_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 28 / Col B =====
                    row = sheet.getRow(27);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR28_security_firm() != null)
                        cellB.setCellValue(record1.getR28_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R28 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR28_credit_rating() != null)
                        cellC.setCellValue(record1.getR28_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R28 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR28_rating_agency() != null)
                        cellD.setCellValue(record1.getR28_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R28 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR28_exposure_amount() != null)
                        cellE.setCellValue(record1.getR28_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R28 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR28_risk_weight() != null)
                        cellF.setCellValue(record1.getR28_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 29 / Col B =====
                    row = sheet.getRow(28);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR29_security_firm() != null)
                        cellB.setCellValue(record1.getR29_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R29 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR29_credit_rating() != null)
                        cellC.setCellValue(record1.getR29_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R29 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR29_rating_agency() != null)
                        cellD.setCellValue(record1.getR29_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R29 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR29_exposure_amount() != null)
                        cellE.setCellValue(record1.getR29_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R29 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR29_risk_weight() != null)
                        cellF.setCellValue(record1.getR29_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 30 / Col B =====
                    row = sheet.getRow(29);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR30_security_firm() != null)
                        cellB.setCellValue(record1.getR30_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R30 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR30_credit_rating() != null)
                        cellC.setCellValue(record1.getR30_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R30 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR30_rating_agency() != null)
                        cellD.setCellValue(record1.getR30_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R30 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR30_exposure_amount() != null)
                        cellE.setCellValue(record1.getR30_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R30 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR30_risk_weight() != null)
                        cellF.setCellValue(record1.getR30_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 31 / Col B =====
                    row = sheet.getRow(30);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR31_security_firm() != null)
                        cellB.setCellValue(record1.getR31_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R31 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR31_credit_rating() != null)
                        cellC.setCellValue(record1.getR31_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R31 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR31_rating_agency() != null)
                        cellD.setCellValue(record1.getR31_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R31 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR31_exposure_amount() != null)
                        cellE.setCellValue(record1.getR31_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R31 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR31_risk_weight() != null)
                        cellF.setCellValue(record1.getR31_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 32 / Col B =====
                    row = sheet.getRow(31);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR32_security_firm() != null)
                        cellB.setCellValue(record1.getR32_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R32 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR32_credit_rating() != null)
                        cellC.setCellValue(record1.getR32_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R32 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR32_rating_agency() != null)
                        cellD.setCellValue(record1.getR32_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R32 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR32_exposure_amount() != null)
                        cellE.setCellValue(record1.getR32_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R32 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR32_risk_weight() != null)
                        cellF.setCellValue(record1.getR32_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 33 / Col B =====
                    row = sheet.getRow(32);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR33_security_firm() != null)
                        cellB.setCellValue(record1.getR33_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R33 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR33_credit_rating() != null)
                        cellC.setCellValue(record1.getR33_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R33 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR33_rating_agency() != null)
                        cellD.setCellValue(record1.getR33_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R33 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR33_exposure_amount() != null)
                        cellE.setCellValue(record1.getR33_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R33 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR33_risk_weight() != null)
                        cellF.setCellValue(record1.getR33_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 34 / Col B =====
                    row = sheet.getRow(33);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR34_security_firm() != null)
                        cellB.setCellValue(record1.getR34_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R34 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR34_credit_rating() != null)
                        cellC.setCellValue(record1.getR34_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R34 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR34_rating_agency() != null)
                        cellD.setCellValue(record1.getR34_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R34 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR34_exposure_amount() != null)
                        cellE.setCellValue(record1.getR34_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R34 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR34_risk_weight() != null)
                        cellF.setCellValue(record1.getR34_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 35 / Col B =====
                    row = sheet.getRow(34);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR35_security_firm() != null)
                        cellB.setCellValue(record1.getR35_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R35 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR35_credit_rating() != null)
                        cellC.setCellValue(record1.getR35_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R35 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR35_rating_agency() != null)
                        cellD.setCellValue(record1.getR35_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R35 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR35_exposure_amount() != null)
                        cellE.setCellValue(record1.getR35_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R35 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR35_risk_weight() != null)
                        cellF.setCellValue(record1.getR35_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 36 / Col B =====
                    row = sheet.getRow(35);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR36_security_firm() != null)
                        cellB.setCellValue(record1.getR36_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R36 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR36_credit_rating() != null)
                        cellC.setCellValue(record1.getR36_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R36 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR36_rating_agency() != null)
                        cellD.setCellValue(record1.getR36_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R36 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR36_exposure_amount() != null)
                        cellE.setCellValue(record1.getR36_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R36 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR36_risk_weight() != null)
                        cellF.setCellValue(record1.getR36_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 37 / Col B =====
                    row = sheet.getRow(36);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR37_security_firm() != null)
                        cellB.setCellValue(record1.getR37_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R37 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR37_credit_rating() != null)
                        cellC.setCellValue(record1.getR37_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R37 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR37_rating_agency() != null)
                        cellD.setCellValue(record1.getR37_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R37 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR37_exposure_amount() != null)
                        cellE.setCellValue(record1.getR37_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R37 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR37_risk_weight() != null)
                        cellF.setCellValue(record1.getR37_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 38 / Col B =====
                    row = sheet.getRow(37);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR38_security_firm() != null)
                        cellB.setCellValue(record1.getR38_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R38 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR38_credit_rating() != null)
                        cellC.setCellValue(record1.getR38_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R38 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR38_rating_agency() != null)
                        cellD.setCellValue(record1.getR38_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R38 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR38_exposure_amount() != null)
                        cellE.setCellValue(record1.getR38_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R38 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR38_risk_weight() != null)
                        cellF.setCellValue(record1.getR38_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 39 / Col B =====
                    row = sheet.getRow(38);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR39_security_firm() != null)
                        cellB.setCellValue(record1.getR39_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R39 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR39_credit_rating() != null)
                        cellC.setCellValue(record1.getR39_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R39 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR39_rating_agency() != null)
                        cellD.setCellValue(record1.getR39_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R39 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR39_exposure_amount() != null)
                        cellE.setCellValue(record1.getR39_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R39 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR39_risk_weight() != null)
                        cellF.setCellValue(record1.getR39_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 40 / Col B =====
                    row = sheet.getRow(39);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR40_security_firm() != null)
                        cellB.setCellValue(record1.getR40_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R40 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR40_credit_rating() != null)
                        cellC.setCellValue(record1.getR40_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R40 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR40_rating_agency() != null)
                        cellD.setCellValue(record1.getR40_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R40 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR40_exposure_amount() != null)
                        cellE.setCellValue(record1.getR40_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R40 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR40_risk_weight() != null)
                        cellF.setCellValue(record1.getR40_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 41 / Col B =====
                    row = sheet.getRow(40);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR41_security_firm() != null)
                        cellB.setCellValue(record1.getR41_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R41 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR41_credit_rating() != null)
                        cellC.setCellValue(record1.getR41_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R41 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR41_rating_agency() != null)
                        cellD.setCellValue(record1.getR41_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R41 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR41_exposure_amount() != null)
                        cellE.setCellValue(record1.getR41_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R41 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR41_risk_weight() != null)
                        cellF.setCellValue(record1.getR41_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 42 / Col B =====
                    row = sheet.getRow(41);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR42_security_firm() != null)
                        cellB.setCellValue(record1.getR42_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R42 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR42_credit_rating() != null)
                        cellC.setCellValue(record1.getR42_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R42 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR42_rating_agency() != null)
                        cellD.setCellValue(record1.getR42_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R42 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR42_exposure_amount() != null)
                        cellE.setCellValue(record1.getR42_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R42 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR42_risk_weight() != null)
                        cellF.setCellValue(record1.getR42_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 43 / Col B =====
                    row = sheet.getRow(42);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR43_security_firm() != null)
                        cellB.setCellValue(record1.getR43_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R43 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR43_credit_rating() != null)
                        cellC.setCellValue(record1.getR43_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R43 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR43_rating_agency() != null)
                        cellD.setCellValue(record1.getR43_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R43 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR43_exposure_amount() != null)
                        cellE.setCellValue(record1.getR43_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R43 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR43_risk_weight() != null)
                        cellF.setCellValue(record1.getR43_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 44 / Col B =====
                    row = sheet.getRow(43);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR44_security_firm() != null)
                        cellB.setCellValue(record1.getR44_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R44 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR44_credit_rating() != null)
                        cellC.setCellValue(record1.getR44_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R44 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR44_rating_agency() != null)
                        cellD.setCellValue(record1.getR44_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R44 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR44_exposure_amount() != null)
                        cellE.setCellValue(record1.getR44_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R44 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR44_risk_weight() != null)
                        cellF.setCellValue(record1.getR44_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 45 / Col B =====
                    row = sheet.getRow(44);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR45_security_firm() != null)
                        cellB.setCellValue(record1.getR45_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R45 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR45_credit_rating() != null)
                        cellC.setCellValue(record1.getR45_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R45 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR45_rating_agency() != null)
                        cellD.setCellValue(record1.getR45_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R45 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR45_exposure_amount() != null)
                        cellE.setCellValue(record1.getR45_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R45 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR45_risk_weight() != null)
                        cellF.setCellValue(record1.getR45_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 46 / Col B =====
                    row = sheet.getRow(45);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR46_security_firm() != null)
                        cellB.setCellValue(record1.getR46_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R46 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR46_credit_rating() != null)
                        cellC.setCellValue(record1.getR46_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R46 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR46_rating_agency() != null)
                        cellD.setCellValue(record1.getR46_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R46 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR46_exposure_amount() != null)
                        cellE.setCellValue(record1.getR46_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R46 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR46_risk_weight() != null)
                        cellF.setCellValue(record1.getR46_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 47 / Col B =====
                    row = sheet.getRow(46);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR47_security_firm() != null)
                        cellB.setCellValue(record1.getR47_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R47 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR47_credit_rating() != null)
                        cellC.setCellValue(record1.getR47_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R47 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR47_rating_agency() != null)
                        cellD.setCellValue(record1.getR47_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R47 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR47_exposure_amount() != null)
                        cellE.setCellValue(record1.getR47_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R47 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR47_risk_weight() != null)
                        cellF.setCellValue(record1.getR47_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 48 / Col B =====
                    row = sheet.getRow(47);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR48_security_firm() != null)
                        cellB.setCellValue(record1.getR48_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R48 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR48_credit_rating() != null)
                        cellC.setCellValue(record1.getR48_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R48 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR48_rating_agency() != null)
                        cellD.setCellValue(record1.getR48_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R48 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR48_exposure_amount() != null)
                        cellE.setCellValue(record1.getR48_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R48 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR48_risk_weight() != null)
                        cellF.setCellValue(record1.getR48_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 49 / Col B =====
                    row = sheet.getRow(48);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR49_security_firm() != null)
                        cellB.setCellValue(record1.getR49_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R49 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR49_credit_rating() != null)
                        cellC.setCellValue(record1.getR49_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R49 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR49_rating_agency() != null)
                        cellD.setCellValue(record1.getR49_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R49 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR49_exposure_amount() != null)
                        cellE.setCellValue(record1.getR49_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R49 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR49_risk_weight() != null)
                        cellF.setCellValue(record1.getR49_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 50 / Col B =====
                    row = sheet.getRow(49);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR50_security_firm() != null)
                        cellB.setCellValue(record1.getR50_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R50 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR50_credit_rating() != null)
                        cellC.setCellValue(record1.getR50_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R50 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR50_rating_agency() != null)
                        cellD.setCellValue(record1.getR50_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R50 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR50_exposure_amount() != null)
                        cellE.setCellValue(record1.getR50_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R50 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR50_risk_weight() != null)
                        cellF.setCellValue(record1.getR50_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 51 / Col B =====
                    row = sheet.getRow(50);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR51_security_firm() != null)
                        cellB.setCellValue(record1.getR51_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R51 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR51_credit_rating() != null)
                        cellC.setCellValue(record1.getR51_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R51 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR51_rating_agency() != null)
                        cellD.setCellValue(record1.getR51_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R51 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR51_exposure_amount() != null)
                        cellE.setCellValue(record1.getR51_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R51 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR51_risk_weight() != null)
                        cellF.setCellValue(record1.getR51_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 52 / Col B =====
                    row = sheet.getRow(51);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR52_security_firm() != null)
                        cellB.setCellValue(record1.getR52_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R52 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR52_credit_rating() != null)
                        cellC.setCellValue(record1.getR52_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R52 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR52_rating_agency() != null)
                        cellD.setCellValue(record1.getR52_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R52 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR52_exposure_amount() != null)
                        cellE.setCellValue(record1.getR52_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R52 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR52_risk_weight() != null)
                        cellF.setCellValue(record1.getR52_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 53 / Col B =====
                    row = sheet.getRow(52);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR53_security_firm() != null)
                        cellB.setCellValue(record1.getR53_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R53 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR53_credit_rating() != null)
                        cellC.setCellValue(record1.getR53_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R53 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR53_rating_agency() != null)
                        cellD.setCellValue(record1.getR53_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R53 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR53_exposure_amount() != null)
                        cellE.setCellValue(record1.getR53_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R53 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR53_risk_weight() != null)
                        cellF.setCellValue(record1.getR53_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 54 / Col B =====
                    row = sheet.getRow(53);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR54_security_firm() != null)
                        cellB.setCellValue(record1.getR54_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R54 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR54_credit_rating() != null)
                        cellC.setCellValue(record1.getR54_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R54 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR54_rating_agency() != null)
                        cellD.setCellValue(record1.getR54_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R54 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR54_exposure_amount() != null)
                        cellE.setCellValue(record1.getR54_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R54 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR54_risk_weight() != null)
                        cellF.setCellValue(record1.getR54_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 55 / Col B =====
                    row = sheet.getRow(54);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR55_security_firm() != null)
                        cellB.setCellValue(record1.getR55_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R55 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR55_credit_rating() != null)
                        cellC.setCellValue(record1.getR55_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R55 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR55_rating_agency() != null)
                        cellD.setCellValue(record1.getR55_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R55 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR55_exposure_amount() != null)
                        cellE.setCellValue(record1.getR55_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R55 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR55_risk_weight() != null)
                        cellF.setCellValue(record1.getR55_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 56 / Col B =====
                    row = sheet.getRow(55);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR56_security_firm() != null)
                        cellB.setCellValue(record1.getR56_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R56 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR56_credit_rating() != null)
                        cellC.setCellValue(record1.getR56_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R56 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR56_rating_agency() != null)
                        cellD.setCellValue(record1.getR56_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R56 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR56_exposure_amount() != null)
                        cellE.setCellValue(record1.getR56_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R56 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR56_risk_weight() != null)
                        cellF.setCellValue(record1.getR56_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 57 / Col B =====
                    row = sheet.getRow(56);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR57_security_firm() != null)
                        cellB.setCellValue(record1.getR57_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R57 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR57_credit_rating() != null)
                        cellC.setCellValue(record1.getR57_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R57 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR57_rating_agency() != null)
                        cellD.setCellValue(record1.getR57_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R57 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR57_exposure_amount() != null)
                        cellE.setCellValue(record1.getR57_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R57 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR57_risk_weight() != null)
                        cellF.setCellValue(record1.getR57_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 58 / Col B =====
                    row = sheet.getRow(57);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR58_security_firm() != null)
                        cellB.setCellValue(record1.getR58_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R58 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR58_credit_rating() != null)
                        cellC.setCellValue(record1.getR58_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R58 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR58_rating_agency() != null)
                        cellD.setCellValue(record1.getR58_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R58 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR58_exposure_amount() != null)
                        cellE.setCellValue(record1.getR58_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R58 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR58_risk_weight() != null)
                        cellF.setCellValue(record1.getR58_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 59 / Col B =====
                    row = sheet.getRow(58);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR59_security_firm() != null)
                        cellB.setCellValue(record1.getR59_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R59 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR59_credit_rating() != null)
                        cellC.setCellValue(record1.getR59_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R59 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR59_rating_agency() != null)
                        cellD.setCellValue(record1.getR59_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R59 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR59_exposure_amount() != null)
                        cellE.setCellValue(record1.getR59_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R59 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR59_risk_weight() != null)
                        cellF.setCellValue(record1.getR59_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);
    
                    // ===== Row 60 / Col B =====
                    row = sheet.getRow(59);
                    cellB = row.getCell(1);
                    if (cellB == null) cellB = row.createCell(1);
                    originalStyle = cellB.getCellStyle();
                    if (record1.getR60_security_firm() != null)
                        cellB.setCellValue(record1.getR60_security_firm());
                    else cellB.setCellValue("");
                    cellB.setCellStyle(originalStyle);

                    // ===== R60 / Col C =====
                    cellC = row.getCell(2);
                    if (cellC == null) cellC = row.createCell(2);
                    originalStyle = cellC.getCellStyle();
                    if (record1.getR60_credit_rating() != null)
                        cellC.setCellValue(record1.getR60_credit_rating().doubleValue());
                    else cellC.setCellValue("");
                    cellC.setCellStyle(originalStyle);

                    // ===== R60 / Col D =====
                    cellD = row.getCell(3);
                    if (cellD == null) cellD = row.createCell(3);
                    originalStyle = cellD.getCellStyle();
                    if (record1.getR60_rating_agency() != null)
                        cellD.setCellValue(record1.getR60_rating_agency());
                    else cellD.setCellValue("");
                    cellD.setCellStyle(originalStyle);

                    // ===== R60 / Col E =====
                    cellE = row.getCell(4);
                    if (cellE == null) cellE = row.createCell(4);
                    originalStyle = cellE.getCellStyle();
                    if (record1.getR60_exposure_amount() != null)
                        cellE.setCellValue(record1.getR60_exposure_amount().doubleValue());
                    else cellE.setCellValue("");
                    cellE.setCellStyle(originalStyle);

                    // ===== R60 / Col F =====
                    cellF = row.getCell(5);
                    if (cellF == null) cellF = row.createCell(5);
                    originalStyle = cellF.getCellStyle();
                    if (record1.getR60_risk_weight() != null)
                        cellF.setCellValue(record1.getR60_risk_weight().doubleValue());
                    else cellF.setCellValue("");
                    cellF.setCellStyle(originalStyle);		    
		        
		        
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

//	public List<Object> getM_SRWA_12GArchival() {
//		List<Object> M_SRWA_12GArchivallist = new ArrayList<>();
////		List<Object> M_SRWA_12GArchivallist2 = new ArrayList<>();
////		List<Object> M_SRWA_12GArchivallist3 = new ArrayList<>();
//		try {
//			M_SRWA_12GArchivallist = BRRS_M_SRWA_12G_Archival_Summary_Repo.getM_SRWA_12Garchival();
//			
//			System.out.println("countser" + M_SRWA_12GArchivallist.size());
////			System.out.println("countser" + M_SRWA_12GArchivallist.size());
////			System.out.println("countser" + M_SRWA_12GArchivallist.size());
//		} catch (Exception e) {
//			// Log the exception
//			System.err.println("Error fetching M_SRWA_12G Archival data: " + e.getMessage());
//			e.printStackTrace();
//
//			// Optionally, you can rethrow it or return empty list
//			// throw new RuntimeException("Failed to fetch data", e);
//		}
//		return M_SRWA_12GArchivallist;
//	}
//


	public byte[] getExcelM_SRWA_12GARCHIVAL(String filename, String reportId, String fromdate,
			String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if ("ARCHIVAL".equals(type) && version != null) {
		}
			List<M_SRWA_12G_Archival_Summary_Entity> dataList1 = BRRS_M_SRWA_12G_Archival_Summary_Repo
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
			        M_SRWA_12G_Archival_Summary_Entity record1 = dataList1.get(i);
			        System.out.println("rownumber=" + (startRow + i));
					
			        
				       
			       Row row;
			        Cell cellB, cellC, cellD, cellE, cellF;
			        CellStyle originalStyle;

			     
			        
			        
			        // ===== Row 11 / Col B =====
					row = sheet.getRow(10);
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
			// ✅ Handle String value 
					if (record1.getR11_security_firm() != null)
					cellB.setCellValue(record1.getR11_security_firm()); // String directly 
					else cellB.setCellValue(""); 
					cellB.setCellStyle(originalStyle);


					// ===== R11 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR11_credit_rating() != null) 
			        cellC.setCellValue(record1.getR11_credit_rating().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);


					// ===== R11 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_rating_agency() != null) 
			        cellD.setCellValue(record1.getR11_rating_agency()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R11 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			        if (record1.getR11_exposure_amount() != null) 
			        cellE.setCellValue(record1.getR11_exposure_amount().doubleValue());
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);
			        
					// ===== R11 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			        if (record1.getR11_risk_weight() != null) 
			        cellF.setCellValue(record1.getR11_risk_weight().doubleValue());
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);
			     
			     // ===== Row 12 / Col B =====
                 row = sheet.getRow(11);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR12_security_firm() != null)
                     cellB.setCellValue(record1.getR12_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R12 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR12_credit_rating() != null)
                     cellC.setCellValue(record1.getR12_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R12 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR12_rating_agency() != null)
                     cellD.setCellValue(record1.getR12_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R12 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR12_exposure_amount() != null)
                     cellE.setCellValue(record1.getR12_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R12 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR12_risk_weight() != null)
                     cellF.setCellValue(record1.getR12_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 13 / Col B =====
                 row = sheet.getRow(12);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR13_security_firm() != null)
                     cellB.setCellValue(record1.getR13_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R13 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR13_credit_rating() != null)
                     cellC.setCellValue(record1.getR13_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R13 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR13_rating_agency() != null)
                     cellD.setCellValue(record1.getR13_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R13 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR13_exposure_amount() != null)
                     cellE.setCellValue(record1.getR13_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R13 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR13_risk_weight() != null)
                     cellF.setCellValue(record1.getR13_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 14 / Col B =====
                 row = sheet.getRow(13);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR14_security_firm() != null)
                     cellB.setCellValue(record1.getR14_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R14 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR14_credit_rating() != null)
                     cellC.setCellValue(record1.getR14_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R14 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR14_rating_agency() != null)
                     cellD.setCellValue(record1.getR14_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R14 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR14_exposure_amount() != null)
                     cellE.setCellValue(record1.getR14_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R14 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR14_risk_weight() != null)
                     cellF.setCellValue(record1.getR14_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 15 / Col B =====
                 row = sheet.getRow(14);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR15_security_firm() != null)
                     cellB.setCellValue(record1.getR15_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R15 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR15_credit_rating() != null)
                     cellC.setCellValue(record1.getR15_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R15 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR15_rating_agency() != null)
                     cellD.setCellValue(record1.getR15_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R15 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR15_exposure_amount() != null)
                     cellE.setCellValue(record1.getR15_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R15 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR15_risk_weight() != null)
                     cellF.setCellValue(record1.getR15_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 16 / Col B =====
                 row = sheet.getRow(15);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR16_security_firm() != null)
                     cellB.setCellValue(record1.getR16_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R16 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR16_credit_rating() != null)
                     cellC.setCellValue(record1.getR16_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R16 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR16_rating_agency() != null)
                     cellD.setCellValue(record1.getR16_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R16 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR16_exposure_amount() != null)
                     cellE.setCellValue(record1.getR16_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R16 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR16_risk_weight() != null)
                     cellF.setCellValue(record1.getR16_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 17 / Col B =====
                 row = sheet.getRow(16);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR17_security_firm() != null)
                     cellB.setCellValue(record1.getR17_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R17 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR17_credit_rating() != null)
                     cellC.setCellValue(record1.getR17_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R17 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR17_rating_agency() != null)
                     cellD.setCellValue(record1.getR17_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R17 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR17_exposure_amount() != null)
                     cellE.setCellValue(record1.getR17_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R17 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR17_risk_weight() != null)
                     cellF.setCellValue(record1.getR17_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 18 / Col B =====
                 row = sheet.getRow(17);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR18_security_firm() != null)
                     cellB.setCellValue(record1.getR18_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R18 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR18_credit_rating() != null)
                     cellC.setCellValue(record1.getR18_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R18 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR18_rating_agency() != null)
                     cellD.setCellValue(record1.getR18_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R18 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR18_exposure_amount() != null)
                     cellE.setCellValue(record1.getR18_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R18 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR18_risk_weight() != null)
                     cellF.setCellValue(record1.getR18_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 19 / Col B =====
                 row = sheet.getRow(18);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR19_security_firm() != null)
                     cellB.setCellValue(record1.getR19_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R19 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR19_credit_rating() != null)
                     cellC.setCellValue(record1.getR19_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R19 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR19_rating_agency() != null)
                     cellD.setCellValue(record1.getR19_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R19 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR19_exposure_amount() != null)
                     cellE.setCellValue(record1.getR19_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R19 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR19_risk_weight() != null)
                     cellF.setCellValue(record1.getR19_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 20 / Col B =====
                 row = sheet.getRow(19);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR20_security_firm() != null)
                     cellB.setCellValue(record1.getR20_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R20 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR20_credit_rating() != null)
                     cellC.setCellValue(record1.getR20_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R20 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR20_rating_agency() != null)
                     cellD.setCellValue(record1.getR20_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R20 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR20_exposure_amount() != null)
                     cellE.setCellValue(record1.getR20_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R20 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR20_risk_weight() != null)
                     cellF.setCellValue(record1.getR20_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 21 / Col B =====
                 row = sheet.getRow(20);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR21_security_firm() != null)
                     cellB.setCellValue(record1.getR21_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R21 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR21_credit_rating() != null)
                     cellC.setCellValue(record1.getR21_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R21 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR21_rating_agency() != null)
                     cellD.setCellValue(record1.getR21_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R21 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR21_exposure_amount() != null)
                     cellE.setCellValue(record1.getR21_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R21 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR21_risk_weight() != null)
                     cellF.setCellValue(record1.getR21_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 22 / Col B =====
                 row = sheet.getRow(21);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR22_security_firm() != null)
                     cellB.setCellValue(record1.getR22_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R22 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR22_credit_rating() != null)
                     cellC.setCellValue(record1.getR22_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R22 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR22_rating_agency() != null)
                     cellD.setCellValue(record1.getR22_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R22 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR22_exposure_amount() != null)
                     cellE.setCellValue(record1.getR22_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R22 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR22_risk_weight() != null)
                     cellF.setCellValue(record1.getR22_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 23 / Col B =====
                 row = sheet.getRow(22);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR23_security_firm() != null)
                     cellB.setCellValue(record1.getR23_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R23 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR23_credit_rating() != null)
                     cellC.setCellValue(record1.getR23_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R23 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR23_rating_agency() != null)
                     cellD.setCellValue(record1.getR23_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R23 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR23_exposure_amount() != null)
                     cellE.setCellValue(record1.getR23_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R23 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR23_risk_weight() != null)
                     cellF.setCellValue(record1.getR23_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 24 / Col B =====
                 row = sheet.getRow(23);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR24_security_firm() != null)
                     cellB.setCellValue(record1.getR24_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R24 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR24_credit_rating() != null)
                     cellC.setCellValue(record1.getR24_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R24 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR24_rating_agency() != null)
                     cellD.setCellValue(record1.getR24_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R24 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR24_exposure_amount() != null)
                     cellE.setCellValue(record1.getR24_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R24 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR24_risk_weight() != null)
                     cellF.setCellValue(record1.getR24_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 25 / Col B =====
                 row = sheet.getRow(24);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR25_security_firm() != null)
                     cellB.setCellValue(record1.getR25_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R25 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR25_credit_rating() != null)
                     cellC.setCellValue(record1.getR25_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R25 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR25_rating_agency() != null)
                     cellD.setCellValue(record1.getR25_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R25 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR25_exposure_amount() != null)
                     cellE.setCellValue(record1.getR25_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R25 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR25_risk_weight() != null)
                     cellF.setCellValue(record1.getR25_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 26 / Col B =====
                 row = sheet.getRow(25);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR26_security_firm() != null)
                     cellB.setCellValue(record1.getR26_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R26 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR26_credit_rating() != null)
                     cellC.setCellValue(record1.getR26_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R26 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR26_rating_agency() != null)
                     cellD.setCellValue(record1.getR26_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R26 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR26_exposure_amount() != null)
                     cellE.setCellValue(record1.getR26_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R26 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR26_risk_weight() != null)
                     cellF.setCellValue(record1.getR26_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 27 / Col B =====
                 row = sheet.getRow(26);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR27_security_firm() != null)
                     cellB.setCellValue(record1.getR27_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R27 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR27_credit_rating() != null)
                     cellC.setCellValue(record1.getR27_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R27 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR27_rating_agency() != null)
                     cellD.setCellValue(record1.getR27_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R27 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR27_exposure_amount() != null)
                     cellE.setCellValue(record1.getR27_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R27 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR27_risk_weight() != null)
                     cellF.setCellValue(record1.getR27_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 28 / Col B =====
                 row = sheet.getRow(27);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR28_security_firm() != null)
                     cellB.setCellValue(record1.getR28_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R28 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR28_credit_rating() != null)
                     cellC.setCellValue(record1.getR28_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R28 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR28_rating_agency() != null)
                     cellD.setCellValue(record1.getR28_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R28 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR28_exposure_amount() != null)
                     cellE.setCellValue(record1.getR28_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R28 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR28_risk_weight() != null)
                     cellF.setCellValue(record1.getR28_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 29 / Col B =====
                 row = sheet.getRow(28);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR29_security_firm() != null)
                     cellB.setCellValue(record1.getR29_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R29 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR29_credit_rating() != null)
                     cellC.setCellValue(record1.getR29_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R29 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR29_rating_agency() != null)
                     cellD.setCellValue(record1.getR29_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R29 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR29_exposure_amount() != null)
                     cellE.setCellValue(record1.getR29_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R29 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR29_risk_weight() != null)
                     cellF.setCellValue(record1.getR29_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 30 / Col B =====
                 row = sheet.getRow(29);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR30_security_firm() != null)
                     cellB.setCellValue(record1.getR30_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R30 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR30_credit_rating() != null)
                     cellC.setCellValue(record1.getR30_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R30 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR30_rating_agency() != null)
                     cellD.setCellValue(record1.getR30_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R30 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR30_exposure_amount() != null)
                     cellE.setCellValue(record1.getR30_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R30 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR30_risk_weight() != null)
                     cellF.setCellValue(record1.getR30_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 31 / Col B =====
                 row = sheet.getRow(30);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR31_security_firm() != null)
                     cellB.setCellValue(record1.getR31_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R31 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR31_credit_rating() != null)
                     cellC.setCellValue(record1.getR31_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R31 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR31_rating_agency() != null)
                     cellD.setCellValue(record1.getR31_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R31 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR31_exposure_amount() != null)
                     cellE.setCellValue(record1.getR31_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R31 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR31_risk_weight() != null)
                     cellF.setCellValue(record1.getR31_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 32 / Col B =====
                 row = sheet.getRow(31);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR32_security_firm() != null)
                     cellB.setCellValue(record1.getR32_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R32 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR32_credit_rating() != null)
                     cellC.setCellValue(record1.getR32_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R32 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR32_rating_agency() != null)
                     cellD.setCellValue(record1.getR32_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R32 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR32_exposure_amount() != null)
                     cellE.setCellValue(record1.getR32_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R32 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR32_risk_weight() != null)
                     cellF.setCellValue(record1.getR32_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 33 / Col B =====
                 row = sheet.getRow(32);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR33_security_firm() != null)
                     cellB.setCellValue(record1.getR33_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R33 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR33_credit_rating() != null)
                     cellC.setCellValue(record1.getR33_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R33 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR33_rating_agency() != null)
                     cellD.setCellValue(record1.getR33_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R33 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR33_exposure_amount() != null)
                     cellE.setCellValue(record1.getR33_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R33 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR33_risk_weight() != null)
                     cellF.setCellValue(record1.getR33_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 34 / Col B =====
                 row = sheet.getRow(33);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR34_security_firm() != null)
                     cellB.setCellValue(record1.getR34_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R34 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR34_credit_rating() != null)
                     cellC.setCellValue(record1.getR34_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R34 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR34_rating_agency() != null)
                     cellD.setCellValue(record1.getR34_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R34 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR34_exposure_amount() != null)
                     cellE.setCellValue(record1.getR34_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R34 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR34_risk_weight() != null)
                     cellF.setCellValue(record1.getR34_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 35 / Col B =====
                 row = sheet.getRow(34);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR35_security_firm() != null)
                     cellB.setCellValue(record1.getR35_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R35 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR35_credit_rating() != null)
                     cellC.setCellValue(record1.getR35_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R35 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR35_rating_agency() != null)
                     cellD.setCellValue(record1.getR35_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R35 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR35_exposure_amount() != null)
                     cellE.setCellValue(record1.getR35_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R35 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR35_risk_weight() != null)
                     cellF.setCellValue(record1.getR35_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 36 / Col B =====
                 row = sheet.getRow(35);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR36_security_firm() != null)
                     cellB.setCellValue(record1.getR36_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R36 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR36_credit_rating() != null)
                     cellC.setCellValue(record1.getR36_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R36 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR36_rating_agency() != null)
                     cellD.setCellValue(record1.getR36_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R36 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR36_exposure_amount() != null)
                     cellE.setCellValue(record1.getR36_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R36 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR36_risk_weight() != null)
                     cellF.setCellValue(record1.getR36_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 37 / Col B =====
                 row = sheet.getRow(36);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR37_security_firm() != null)
                     cellB.setCellValue(record1.getR37_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R37 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR37_credit_rating() != null)
                     cellC.setCellValue(record1.getR37_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R37 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR37_rating_agency() != null)
                     cellD.setCellValue(record1.getR37_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R37 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR37_exposure_amount() != null)
                     cellE.setCellValue(record1.getR37_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R37 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR37_risk_weight() != null)
                     cellF.setCellValue(record1.getR37_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 38 / Col B =====
                 row = sheet.getRow(37);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR38_security_firm() != null)
                     cellB.setCellValue(record1.getR38_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R38 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR38_credit_rating() != null)
                     cellC.setCellValue(record1.getR38_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R38 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR38_rating_agency() != null)
                     cellD.setCellValue(record1.getR38_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R38 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR38_exposure_amount() != null)
                     cellE.setCellValue(record1.getR38_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R38 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR38_risk_weight() != null)
                     cellF.setCellValue(record1.getR38_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 39 / Col B =====
                 row = sheet.getRow(38);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR39_security_firm() != null)
                     cellB.setCellValue(record1.getR39_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R39 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR39_credit_rating() != null)
                     cellC.setCellValue(record1.getR39_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R39 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR39_rating_agency() != null)
                     cellD.setCellValue(record1.getR39_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R39 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR39_exposure_amount() != null)
                     cellE.setCellValue(record1.getR39_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R39 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR39_risk_weight() != null)
                     cellF.setCellValue(record1.getR39_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 40 / Col B =====
                 row = sheet.getRow(39);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR40_security_firm() != null)
                     cellB.setCellValue(record1.getR40_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R40 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR40_credit_rating() != null)
                     cellC.setCellValue(record1.getR40_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R40 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR40_rating_agency() != null)
                     cellD.setCellValue(record1.getR40_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R40 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR40_exposure_amount() != null)
                     cellE.setCellValue(record1.getR40_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R40 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR40_risk_weight() != null)
                     cellF.setCellValue(record1.getR40_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 41 / Col B =====
                 row = sheet.getRow(40);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR41_security_firm() != null)
                     cellB.setCellValue(record1.getR41_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R41 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR41_credit_rating() != null)
                     cellC.setCellValue(record1.getR41_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R41 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR41_rating_agency() != null)
                     cellD.setCellValue(record1.getR41_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R41 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR41_exposure_amount() != null)
                     cellE.setCellValue(record1.getR41_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R41 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR41_risk_weight() != null)
                     cellF.setCellValue(record1.getR41_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 42 / Col B =====
                 row = sheet.getRow(41);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR42_security_firm() != null)
                     cellB.setCellValue(record1.getR42_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R42 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR42_credit_rating() != null)
                     cellC.setCellValue(record1.getR42_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R42 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR42_rating_agency() != null)
                     cellD.setCellValue(record1.getR42_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R42 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR42_exposure_amount() != null)
                     cellE.setCellValue(record1.getR42_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R42 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR42_risk_weight() != null)
                     cellF.setCellValue(record1.getR42_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 43 / Col B =====
                 row = sheet.getRow(42);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR43_security_firm() != null)
                     cellB.setCellValue(record1.getR43_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R43 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR43_credit_rating() != null)
                     cellC.setCellValue(record1.getR43_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R43 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR43_rating_agency() != null)
                     cellD.setCellValue(record1.getR43_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R43 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR43_exposure_amount() != null)
                     cellE.setCellValue(record1.getR43_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R43 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR43_risk_weight() != null)
                     cellF.setCellValue(record1.getR43_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 44 / Col B =====
                 row = sheet.getRow(43);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR44_security_firm() != null)
                     cellB.setCellValue(record1.getR44_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R44 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR44_credit_rating() != null)
                     cellC.setCellValue(record1.getR44_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R44 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR44_rating_agency() != null)
                     cellD.setCellValue(record1.getR44_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R44 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR44_exposure_amount() != null)
                     cellE.setCellValue(record1.getR44_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R44 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR44_risk_weight() != null)
                     cellF.setCellValue(record1.getR44_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 45 / Col B =====
                 row = sheet.getRow(44);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR45_security_firm() != null)
                     cellB.setCellValue(record1.getR45_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R45 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR45_credit_rating() != null)
                     cellC.setCellValue(record1.getR45_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R45 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR45_rating_agency() != null)
                     cellD.setCellValue(record1.getR45_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R45 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR45_exposure_amount() != null)
                     cellE.setCellValue(record1.getR45_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R45 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR45_risk_weight() != null)
                     cellF.setCellValue(record1.getR45_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 46 / Col B =====
                 row = sheet.getRow(45);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR46_security_firm() != null)
                     cellB.setCellValue(record1.getR46_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R46 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR46_credit_rating() != null)
                     cellC.setCellValue(record1.getR46_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R46 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR46_rating_agency() != null)
                     cellD.setCellValue(record1.getR46_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R46 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR46_exposure_amount() != null)
                     cellE.setCellValue(record1.getR46_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R46 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR46_risk_weight() != null)
                     cellF.setCellValue(record1.getR46_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 47 / Col B =====
                 row = sheet.getRow(46);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR47_security_firm() != null)
                     cellB.setCellValue(record1.getR47_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R47 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR47_credit_rating() != null)
                     cellC.setCellValue(record1.getR47_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R47 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR47_rating_agency() != null)
                     cellD.setCellValue(record1.getR47_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R47 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR47_exposure_amount() != null)
                     cellE.setCellValue(record1.getR47_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R47 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR47_risk_weight() != null)
                     cellF.setCellValue(record1.getR47_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 48 / Col B =====
                 row = sheet.getRow(47);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR48_security_firm() != null)
                     cellB.setCellValue(record1.getR48_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R48 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR48_credit_rating() != null)
                     cellC.setCellValue(record1.getR48_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R48 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR48_rating_agency() != null)
                     cellD.setCellValue(record1.getR48_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R48 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR48_exposure_amount() != null)
                     cellE.setCellValue(record1.getR48_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R48 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR48_risk_weight() != null)
                     cellF.setCellValue(record1.getR48_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 49 / Col B =====
                 row = sheet.getRow(48);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR49_security_firm() != null)
                     cellB.setCellValue(record1.getR49_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R49 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR49_credit_rating() != null)
                     cellC.setCellValue(record1.getR49_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R49 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR49_rating_agency() != null)
                     cellD.setCellValue(record1.getR49_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R49 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR49_exposure_amount() != null)
                     cellE.setCellValue(record1.getR49_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R49 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR49_risk_weight() != null)
                     cellF.setCellValue(record1.getR49_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 50 / Col B =====
                 row = sheet.getRow(49);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR50_security_firm() != null)
                     cellB.setCellValue(record1.getR50_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R50 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR50_credit_rating() != null)
                     cellC.setCellValue(record1.getR50_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R50 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR50_rating_agency() != null)
                     cellD.setCellValue(record1.getR50_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R50 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR50_exposure_amount() != null)
                     cellE.setCellValue(record1.getR50_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R50 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR50_risk_weight() != null)
                     cellF.setCellValue(record1.getR50_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 51 / Col B =====
                 row = sheet.getRow(50);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR51_security_firm() != null)
                     cellB.setCellValue(record1.getR51_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R51 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR51_credit_rating() != null)
                     cellC.setCellValue(record1.getR51_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R51 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR51_rating_agency() != null)
                     cellD.setCellValue(record1.getR51_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R51 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR51_exposure_amount() != null)
                     cellE.setCellValue(record1.getR51_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R51 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR51_risk_weight() != null)
                     cellF.setCellValue(record1.getR51_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 52 / Col B =====
                 row = sheet.getRow(51);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR52_security_firm() != null)
                     cellB.setCellValue(record1.getR52_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R52 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR52_credit_rating() != null)
                     cellC.setCellValue(record1.getR52_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R52 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR52_rating_agency() != null)
                     cellD.setCellValue(record1.getR52_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R52 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR52_exposure_amount() != null)
                     cellE.setCellValue(record1.getR52_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R52 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR52_risk_weight() != null)
                     cellF.setCellValue(record1.getR52_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 53 / Col B =====
                 row = sheet.getRow(52);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR53_security_firm() != null)
                     cellB.setCellValue(record1.getR53_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R53 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR53_credit_rating() != null)
                     cellC.setCellValue(record1.getR53_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R53 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR53_rating_agency() != null)
                     cellD.setCellValue(record1.getR53_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R53 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR53_exposure_amount() != null)
                     cellE.setCellValue(record1.getR53_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R53 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR53_risk_weight() != null)
                     cellF.setCellValue(record1.getR53_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 54 / Col B =====
                 row = sheet.getRow(53);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR54_security_firm() != null)
                     cellB.setCellValue(record1.getR54_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R54 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR54_credit_rating() != null)
                     cellC.setCellValue(record1.getR54_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R54 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR54_rating_agency() != null)
                     cellD.setCellValue(record1.getR54_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R54 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR54_exposure_amount() != null)
                     cellE.setCellValue(record1.getR54_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R54 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR54_risk_weight() != null)
                     cellF.setCellValue(record1.getR54_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 55 / Col B =====
                 row = sheet.getRow(54);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR55_security_firm() != null)
                     cellB.setCellValue(record1.getR55_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R55 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR55_credit_rating() != null)
                     cellC.setCellValue(record1.getR55_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R55 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR55_rating_agency() != null)
                     cellD.setCellValue(record1.getR55_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R55 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR55_exposure_amount() != null)
                     cellE.setCellValue(record1.getR55_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R55 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR55_risk_weight() != null)
                     cellF.setCellValue(record1.getR55_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 56 / Col B =====
                 row = sheet.getRow(55);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR56_security_firm() != null)
                     cellB.setCellValue(record1.getR56_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R56 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR56_credit_rating() != null)
                     cellC.setCellValue(record1.getR56_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R56 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR56_rating_agency() != null)
                     cellD.setCellValue(record1.getR56_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R56 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR56_exposure_amount() != null)
                     cellE.setCellValue(record1.getR56_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R56 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR56_risk_weight() != null)
                     cellF.setCellValue(record1.getR56_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 57 / Col B =====
                 row = sheet.getRow(56);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR57_security_firm() != null)
                     cellB.setCellValue(record1.getR57_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R57 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR57_credit_rating() != null)
                     cellC.setCellValue(record1.getR57_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R57 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR57_rating_agency() != null)
                     cellD.setCellValue(record1.getR57_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R57 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR57_exposure_amount() != null)
                     cellE.setCellValue(record1.getR57_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R57 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR57_risk_weight() != null)
                     cellF.setCellValue(record1.getR57_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 58 / Col B =====
                 row = sheet.getRow(57);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR58_security_firm() != null)
                     cellB.setCellValue(record1.getR58_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R58 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR58_credit_rating() != null)
                     cellC.setCellValue(record1.getR58_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R58 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR58_rating_agency() != null)
                     cellD.setCellValue(record1.getR58_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R58 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR58_exposure_amount() != null)
                     cellE.setCellValue(record1.getR58_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R58 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR58_risk_weight() != null)
                     cellF.setCellValue(record1.getR58_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 59 / Col B =====
                 row = sheet.getRow(58);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR59_security_firm() != null)
                     cellB.setCellValue(record1.getR59_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R59 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR59_credit_rating() != null)
                     cellC.setCellValue(record1.getR59_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R59 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR59_rating_agency() != null)
                     cellD.setCellValue(record1.getR59_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R59 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR59_exposure_amount() != null)
                     cellE.setCellValue(record1.getR59_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R59 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR59_risk_weight() != null)
                     cellF.setCellValue(record1.getR59_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 60 / Col B =====
                 row = sheet.getRow(59);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR60_security_firm() != null)
                     cellB.setCellValue(record1.getR60_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R60 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR60_credit_rating() != null)
                     cellC.setCellValue(record1.getR60_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R60 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR60_rating_agency() != null)
                     cellD.setCellValue(record1.getR60_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R60 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR60_exposure_amount() != null)
                     cellE.setCellValue(record1.getR60_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R60 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR60_risk_weight() != null)
                     cellF.setCellValue(record1.getR60_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);		    
		        		        
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
	
//////////////////////////////////////////RESUBMISSION///////////////////////////////////////////////////////////////////	
/// Report Date | Report Version | Domain
/// RESUB VIEW
public List<Object[]> getM_SRWA_12GResub() {
List<Object[]> resubList = new ArrayList<>();
try {
List<M_SRWA_12G_Archival_Summary_Entity> latestArchivalList = 
BRRS_M_SRWA_12G_Archival_Summary_Repo.getdatabydateListWithVersionAll();

if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
for (M_SRWA_12G_Archival_Summary_Entity entity : latestArchivalList) {
Object[] row = new Object[] {
entity.getReport_date(),
entity.getReport_version()
};
resubList.add(row);
}
System.out.println("Fetched " + resubList.size() + " record(s)");
} else {
System.out.println("No archival data found.");
}
} catch (Exception e) {
System.err.println("Error fetching M_SRWA_12G Resub data: " + e.getMessage());
e.printStackTrace();
}
return resubList;
}
	
	
	//Archival View
	public List<Object[]> getM_SRWA_12GArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_SRWA_12G_Archival_Summary_Entity> repoData = BRRS_M_SRWA_12G_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_SRWA_12G_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] {
							entity.getReport_date(), 
							entity.getReport_version() 
					};
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_SRWA_12G_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReport_version());
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
	public void updateReportReSub(M_SRWA_12G_Summary_Entity updatedEntity) {
		System.out.println("Came to Resub Service");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		Date reportDate = updatedEntity.getReport_date();
		int newVersion = 1;

		try {
			// Fetch the latest archival version for this report date
			Optional<M_SRWA_12G_Archival_Summary_Entity> latestArchivalOpt = BRRS_M_SRWA_12G_Archival_Summary_Repo
					.getLatestArchivalVersionByDate(reportDate);

			// Determine next version number
			if (latestArchivalOpt.isPresent()) {
				M_SRWA_12G_Archival_Summary_Entity latestArchival = latestArchivalOpt.get();
				try {
					newVersion = Integer.parseInt(latestArchival.getReport_version()) + 1;
				} catch (NumberFormatException e) {
					System.err.println("Invalid version format. Defaulting to version 1");
					newVersion = 1;
				}
			} else {
				System.out.println("No previous archival found for date: " + reportDate);
			}

			// Prevent duplicate version number
			boolean exists = BRRS_M_SRWA_12G_Archival_Summary_Repo
					.findByReportDateAndReportVersion(reportDate, String.valueOf(newVersion))
					.isPresent();

			if (exists) {
				throw new RuntimeException("Version " + newVersion + " already exists for report date " + reportDate);
			}

			// Copy summary entity to archival entity
			M_SRWA_12G_Archival_Summary_Entity archivalEntity = new M_SRWA_12G_Archival_Summary_Entity();
			org.springframework.beans.BeanUtils.copyProperties(updatedEntity, archivalEntity);

			archivalEntity.setReport_date(reportDate);
			archivalEntity.setReport_version(String.valueOf(newVersion));
			archivalEntity.setReportResubDate(new Date());

			System.out.println("Saving new archival version: " + newVersion);

			// Save new version to repository
			BRRS_M_SRWA_12G_Archival_Summary_Repo.save(archivalEntity);

			System.out.println(" Saved archival version successfully: " + newVersion);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error while creating archival resubmission record", e);
		}
	}

	/// Downloaded for Archival & Resub
	public byte[] BRRS_M_SRWA_12GResubExcel(String filename, String reportId, String fromdate,
        String todate, String currency, String dtltype,
        String type, String version) throws Exception {

    logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

    if (type.equals("RESUB") & version != null) {
       
    }

    List<M_SRWA_12G_Archival_Summary_Entity> dataList1 =
        BRRS_M_SRWA_12G_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

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
			        M_SRWA_12G_Archival_Summary_Entity record1 = dataList1.get(i);
			        System.out.println("rownumber=" + (startRow + i));
					
			        
				       
			       Row row;
			        Cell cellB, cellC, cellD, cellE, cellF;
			        CellStyle originalStyle;

			     
			        
			        
			        // ===== Row 11 / Col B =====
					row = sheet.getRow(10);
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
			// ✅ Handle String value 
					if (record1.getR11_security_firm() != null)
					cellB.setCellValue(record1.getR11_security_firm()); // String directly 
					else cellB.setCellValue(""); 
					cellB.setCellStyle(originalStyle);


					// ===== R11 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR11_credit_rating() != null) 
			        cellC.setCellValue(record1.getR11_credit_rating().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);


					// ===== R11 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_rating_agency() != null) 
			        cellD.setCellValue(record1.getR11_rating_agency()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R11 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			        if (record1.getR11_exposure_amount() != null) 
			        cellE.setCellValue(record1.getR11_exposure_amount().doubleValue());
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);
			        
					// ===== R11 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			        if (record1.getR11_risk_weight() != null) 
			        cellF.setCellValue(record1.getR11_risk_weight().doubleValue());
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);
			     
			     // ===== Row 12 / Col B =====
                 row = sheet.getRow(11);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR12_security_firm() != null)
                     cellB.setCellValue(record1.getR12_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R12 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR12_credit_rating() != null)
                     cellC.setCellValue(record1.getR12_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R12 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR12_rating_agency() != null)
                     cellD.setCellValue(record1.getR12_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R12 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR12_exposure_amount() != null)
                     cellE.setCellValue(record1.getR12_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R12 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR12_risk_weight() != null)
                     cellF.setCellValue(record1.getR12_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 13 / Col B =====
                 row = sheet.getRow(12);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR13_security_firm() != null)
                     cellB.setCellValue(record1.getR13_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R13 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR13_credit_rating() != null)
                     cellC.setCellValue(record1.getR13_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R13 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR13_rating_agency() != null)
                     cellD.setCellValue(record1.getR13_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R13 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR13_exposure_amount() != null)
                     cellE.setCellValue(record1.getR13_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R13 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR13_risk_weight() != null)
                     cellF.setCellValue(record1.getR13_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 14 / Col B =====
                 row = sheet.getRow(13);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR14_security_firm() != null)
                     cellB.setCellValue(record1.getR14_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R14 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR14_credit_rating() != null)
                     cellC.setCellValue(record1.getR14_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R14 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR14_rating_agency() != null)
                     cellD.setCellValue(record1.getR14_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R14 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR14_exposure_amount() != null)
                     cellE.setCellValue(record1.getR14_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R14 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR14_risk_weight() != null)
                     cellF.setCellValue(record1.getR14_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 15 / Col B =====
                 row = sheet.getRow(14);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR15_security_firm() != null)
                     cellB.setCellValue(record1.getR15_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R15 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR15_credit_rating() != null)
                     cellC.setCellValue(record1.getR15_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R15 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR15_rating_agency() != null)
                     cellD.setCellValue(record1.getR15_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R15 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR15_exposure_amount() != null)
                     cellE.setCellValue(record1.getR15_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R15 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR15_risk_weight() != null)
                     cellF.setCellValue(record1.getR15_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 16 / Col B =====
                 row = sheet.getRow(15);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR16_security_firm() != null)
                     cellB.setCellValue(record1.getR16_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R16 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR16_credit_rating() != null)
                     cellC.setCellValue(record1.getR16_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R16 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR16_rating_agency() != null)
                     cellD.setCellValue(record1.getR16_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R16 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR16_exposure_amount() != null)
                     cellE.setCellValue(record1.getR16_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R16 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR16_risk_weight() != null)
                     cellF.setCellValue(record1.getR16_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 17 / Col B =====
                 row = sheet.getRow(16);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR17_security_firm() != null)
                     cellB.setCellValue(record1.getR17_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R17 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR17_credit_rating() != null)
                     cellC.setCellValue(record1.getR17_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R17 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR17_rating_agency() != null)
                     cellD.setCellValue(record1.getR17_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R17 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR17_exposure_amount() != null)
                     cellE.setCellValue(record1.getR17_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R17 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR17_risk_weight() != null)
                     cellF.setCellValue(record1.getR17_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 18 / Col B =====
                 row = sheet.getRow(17);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR18_security_firm() != null)
                     cellB.setCellValue(record1.getR18_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R18 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR18_credit_rating() != null)
                     cellC.setCellValue(record1.getR18_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R18 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR18_rating_agency() != null)
                     cellD.setCellValue(record1.getR18_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R18 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR18_exposure_amount() != null)
                     cellE.setCellValue(record1.getR18_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R18 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR18_risk_weight() != null)
                     cellF.setCellValue(record1.getR18_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 19 / Col B =====
                 row = sheet.getRow(18);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR19_security_firm() != null)
                     cellB.setCellValue(record1.getR19_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R19 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR19_credit_rating() != null)
                     cellC.setCellValue(record1.getR19_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R19 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR19_rating_agency() != null)
                     cellD.setCellValue(record1.getR19_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R19 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR19_exposure_amount() != null)
                     cellE.setCellValue(record1.getR19_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R19 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR19_risk_weight() != null)
                     cellF.setCellValue(record1.getR19_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 20 / Col B =====
                 row = sheet.getRow(19);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR20_security_firm() != null)
                     cellB.setCellValue(record1.getR20_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R20 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR20_credit_rating() != null)
                     cellC.setCellValue(record1.getR20_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R20 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR20_rating_agency() != null)
                     cellD.setCellValue(record1.getR20_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R20 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR20_exposure_amount() != null)
                     cellE.setCellValue(record1.getR20_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R20 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR20_risk_weight() != null)
                     cellF.setCellValue(record1.getR20_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 21 / Col B =====
                 row = sheet.getRow(20);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR21_security_firm() != null)
                     cellB.setCellValue(record1.getR21_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R21 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR21_credit_rating() != null)
                     cellC.setCellValue(record1.getR21_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R21 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR21_rating_agency() != null)
                     cellD.setCellValue(record1.getR21_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R21 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR21_exposure_amount() != null)
                     cellE.setCellValue(record1.getR21_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R21 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR21_risk_weight() != null)
                     cellF.setCellValue(record1.getR21_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 22 / Col B =====
                 row = sheet.getRow(21);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR22_security_firm() != null)
                     cellB.setCellValue(record1.getR22_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R22 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR22_credit_rating() != null)
                     cellC.setCellValue(record1.getR22_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R22 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR22_rating_agency() != null)
                     cellD.setCellValue(record1.getR22_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R22 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR22_exposure_amount() != null)
                     cellE.setCellValue(record1.getR22_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R22 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR22_risk_weight() != null)
                     cellF.setCellValue(record1.getR22_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 23 / Col B =====
                 row = sheet.getRow(22);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR23_security_firm() != null)
                     cellB.setCellValue(record1.getR23_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R23 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR23_credit_rating() != null)
                     cellC.setCellValue(record1.getR23_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R23 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR23_rating_agency() != null)
                     cellD.setCellValue(record1.getR23_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R23 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR23_exposure_amount() != null)
                     cellE.setCellValue(record1.getR23_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R23 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR23_risk_weight() != null)
                     cellF.setCellValue(record1.getR23_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 24 / Col B =====
                 row = sheet.getRow(23);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR24_security_firm() != null)
                     cellB.setCellValue(record1.getR24_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R24 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR24_credit_rating() != null)
                     cellC.setCellValue(record1.getR24_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R24 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR24_rating_agency() != null)
                     cellD.setCellValue(record1.getR24_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R24 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR24_exposure_amount() != null)
                     cellE.setCellValue(record1.getR24_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R24 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR24_risk_weight() != null)
                     cellF.setCellValue(record1.getR24_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 25 / Col B =====
                 row = sheet.getRow(24);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR25_security_firm() != null)
                     cellB.setCellValue(record1.getR25_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R25 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR25_credit_rating() != null)
                     cellC.setCellValue(record1.getR25_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R25 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR25_rating_agency() != null)
                     cellD.setCellValue(record1.getR25_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R25 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR25_exposure_amount() != null)
                     cellE.setCellValue(record1.getR25_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R25 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR25_risk_weight() != null)
                     cellF.setCellValue(record1.getR25_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 26 / Col B =====
                 row = sheet.getRow(25);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR26_security_firm() != null)
                     cellB.setCellValue(record1.getR26_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R26 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR26_credit_rating() != null)
                     cellC.setCellValue(record1.getR26_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R26 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR26_rating_agency() != null)
                     cellD.setCellValue(record1.getR26_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R26 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR26_exposure_amount() != null)
                     cellE.setCellValue(record1.getR26_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R26 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR26_risk_weight() != null)
                     cellF.setCellValue(record1.getR26_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 27 / Col B =====
                 row = sheet.getRow(26);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR27_security_firm() != null)
                     cellB.setCellValue(record1.getR27_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R27 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR27_credit_rating() != null)
                     cellC.setCellValue(record1.getR27_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R27 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR27_rating_agency() != null)
                     cellD.setCellValue(record1.getR27_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R27 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR27_exposure_amount() != null)
                     cellE.setCellValue(record1.getR27_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R27 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR27_risk_weight() != null)
                     cellF.setCellValue(record1.getR27_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 28 / Col B =====
                 row = sheet.getRow(27);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR28_security_firm() != null)
                     cellB.setCellValue(record1.getR28_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R28 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR28_credit_rating() != null)
                     cellC.setCellValue(record1.getR28_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R28 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR28_rating_agency() != null)
                     cellD.setCellValue(record1.getR28_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R28 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR28_exposure_amount() != null)
                     cellE.setCellValue(record1.getR28_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R28 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR28_risk_weight() != null)
                     cellF.setCellValue(record1.getR28_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 29 / Col B =====
                 row = sheet.getRow(28);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR29_security_firm() != null)
                     cellB.setCellValue(record1.getR29_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R29 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR29_credit_rating() != null)
                     cellC.setCellValue(record1.getR29_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R29 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR29_rating_agency() != null)
                     cellD.setCellValue(record1.getR29_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R29 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR29_exposure_amount() != null)
                     cellE.setCellValue(record1.getR29_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R29 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR29_risk_weight() != null)
                     cellF.setCellValue(record1.getR29_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 30 / Col B =====
                 row = sheet.getRow(29);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR30_security_firm() != null)
                     cellB.setCellValue(record1.getR30_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R30 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR30_credit_rating() != null)
                     cellC.setCellValue(record1.getR30_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R30 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR30_rating_agency() != null)
                     cellD.setCellValue(record1.getR30_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R30 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR30_exposure_amount() != null)
                     cellE.setCellValue(record1.getR30_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R30 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR30_risk_weight() != null)
                     cellF.setCellValue(record1.getR30_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 31 / Col B =====
                 row = sheet.getRow(30);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR31_security_firm() != null)
                     cellB.setCellValue(record1.getR31_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R31 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR31_credit_rating() != null)
                     cellC.setCellValue(record1.getR31_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R31 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR31_rating_agency() != null)
                     cellD.setCellValue(record1.getR31_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R31 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR31_exposure_amount() != null)
                     cellE.setCellValue(record1.getR31_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R31 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR31_risk_weight() != null)
                     cellF.setCellValue(record1.getR31_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 32 / Col B =====
                 row = sheet.getRow(31);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR32_security_firm() != null)
                     cellB.setCellValue(record1.getR32_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R32 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR32_credit_rating() != null)
                     cellC.setCellValue(record1.getR32_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R32 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR32_rating_agency() != null)
                     cellD.setCellValue(record1.getR32_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R32 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR32_exposure_amount() != null)
                     cellE.setCellValue(record1.getR32_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R32 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR32_risk_weight() != null)
                     cellF.setCellValue(record1.getR32_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 33 / Col B =====
                 row = sheet.getRow(32);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR33_security_firm() != null)
                     cellB.setCellValue(record1.getR33_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R33 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR33_credit_rating() != null)
                     cellC.setCellValue(record1.getR33_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R33 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR33_rating_agency() != null)
                     cellD.setCellValue(record1.getR33_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R33 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR33_exposure_amount() != null)
                     cellE.setCellValue(record1.getR33_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R33 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR33_risk_weight() != null)
                     cellF.setCellValue(record1.getR33_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 34 / Col B =====
                 row = sheet.getRow(33);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR34_security_firm() != null)
                     cellB.setCellValue(record1.getR34_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R34 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR34_credit_rating() != null)
                     cellC.setCellValue(record1.getR34_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R34 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR34_rating_agency() != null)
                     cellD.setCellValue(record1.getR34_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R34 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR34_exposure_amount() != null)
                     cellE.setCellValue(record1.getR34_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R34 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR34_risk_weight() != null)
                     cellF.setCellValue(record1.getR34_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 35 / Col B =====
                 row = sheet.getRow(34);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR35_security_firm() != null)
                     cellB.setCellValue(record1.getR35_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R35 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR35_credit_rating() != null)
                     cellC.setCellValue(record1.getR35_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R35 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR35_rating_agency() != null)
                     cellD.setCellValue(record1.getR35_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R35 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR35_exposure_amount() != null)
                     cellE.setCellValue(record1.getR35_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R35 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR35_risk_weight() != null)
                     cellF.setCellValue(record1.getR35_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 36 / Col B =====
                 row = sheet.getRow(35);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR36_security_firm() != null)
                     cellB.setCellValue(record1.getR36_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R36 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR36_credit_rating() != null)
                     cellC.setCellValue(record1.getR36_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R36 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR36_rating_agency() != null)
                     cellD.setCellValue(record1.getR36_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R36 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR36_exposure_amount() != null)
                     cellE.setCellValue(record1.getR36_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R36 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR36_risk_weight() != null)
                     cellF.setCellValue(record1.getR36_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 37 / Col B =====
                 row = sheet.getRow(36);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR37_security_firm() != null)
                     cellB.setCellValue(record1.getR37_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R37 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR37_credit_rating() != null)
                     cellC.setCellValue(record1.getR37_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R37 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR37_rating_agency() != null)
                     cellD.setCellValue(record1.getR37_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R37 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR37_exposure_amount() != null)
                     cellE.setCellValue(record1.getR37_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R37 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR37_risk_weight() != null)
                     cellF.setCellValue(record1.getR37_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 38 / Col B =====
                 row = sheet.getRow(37);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR38_security_firm() != null)
                     cellB.setCellValue(record1.getR38_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R38 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR38_credit_rating() != null)
                     cellC.setCellValue(record1.getR38_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R38 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR38_rating_agency() != null)
                     cellD.setCellValue(record1.getR38_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R38 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR38_exposure_amount() != null)
                     cellE.setCellValue(record1.getR38_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R38 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR38_risk_weight() != null)
                     cellF.setCellValue(record1.getR38_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 39 / Col B =====
                 row = sheet.getRow(38);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR39_security_firm() != null)
                     cellB.setCellValue(record1.getR39_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R39 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR39_credit_rating() != null)
                     cellC.setCellValue(record1.getR39_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R39 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR39_rating_agency() != null)
                     cellD.setCellValue(record1.getR39_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R39 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR39_exposure_amount() != null)
                     cellE.setCellValue(record1.getR39_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R39 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR39_risk_weight() != null)
                     cellF.setCellValue(record1.getR39_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 40 / Col B =====
                 row = sheet.getRow(39);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR40_security_firm() != null)
                     cellB.setCellValue(record1.getR40_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R40 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR40_credit_rating() != null)
                     cellC.setCellValue(record1.getR40_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R40 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR40_rating_agency() != null)
                     cellD.setCellValue(record1.getR40_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R40 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR40_exposure_amount() != null)
                     cellE.setCellValue(record1.getR40_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R40 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR40_risk_weight() != null)
                     cellF.setCellValue(record1.getR40_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 41 / Col B =====
                 row = sheet.getRow(40);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR41_security_firm() != null)
                     cellB.setCellValue(record1.getR41_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R41 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR41_credit_rating() != null)
                     cellC.setCellValue(record1.getR41_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R41 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR41_rating_agency() != null)
                     cellD.setCellValue(record1.getR41_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R41 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR41_exposure_amount() != null)
                     cellE.setCellValue(record1.getR41_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R41 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR41_risk_weight() != null)
                     cellF.setCellValue(record1.getR41_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 42 / Col B =====
                 row = sheet.getRow(41);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR42_security_firm() != null)
                     cellB.setCellValue(record1.getR42_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R42 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR42_credit_rating() != null)
                     cellC.setCellValue(record1.getR42_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R42 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR42_rating_agency() != null)
                     cellD.setCellValue(record1.getR42_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R42 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR42_exposure_amount() != null)
                     cellE.setCellValue(record1.getR42_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R42 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR42_risk_weight() != null)
                     cellF.setCellValue(record1.getR42_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 43 / Col B =====
                 row = sheet.getRow(42);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR43_security_firm() != null)
                     cellB.setCellValue(record1.getR43_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R43 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR43_credit_rating() != null)
                     cellC.setCellValue(record1.getR43_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R43 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR43_rating_agency() != null)
                     cellD.setCellValue(record1.getR43_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R43 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR43_exposure_amount() != null)
                     cellE.setCellValue(record1.getR43_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R43 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR43_risk_weight() != null)
                     cellF.setCellValue(record1.getR43_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 44 / Col B =====
                 row = sheet.getRow(43);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR44_security_firm() != null)
                     cellB.setCellValue(record1.getR44_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R44 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR44_credit_rating() != null)
                     cellC.setCellValue(record1.getR44_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R44 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR44_rating_agency() != null)
                     cellD.setCellValue(record1.getR44_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R44 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR44_exposure_amount() != null)
                     cellE.setCellValue(record1.getR44_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R44 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR44_risk_weight() != null)
                     cellF.setCellValue(record1.getR44_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 45 / Col B =====
                 row = sheet.getRow(44);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR45_security_firm() != null)
                     cellB.setCellValue(record1.getR45_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R45 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR45_credit_rating() != null)
                     cellC.setCellValue(record1.getR45_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R45 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR45_rating_agency() != null)
                     cellD.setCellValue(record1.getR45_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R45 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR45_exposure_amount() != null)
                     cellE.setCellValue(record1.getR45_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R45 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR45_risk_weight() != null)
                     cellF.setCellValue(record1.getR45_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 46 / Col B =====
                 row = sheet.getRow(45);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR46_security_firm() != null)
                     cellB.setCellValue(record1.getR46_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R46 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR46_credit_rating() != null)
                     cellC.setCellValue(record1.getR46_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R46 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR46_rating_agency() != null)
                     cellD.setCellValue(record1.getR46_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R46 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR46_exposure_amount() != null)
                     cellE.setCellValue(record1.getR46_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R46 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR46_risk_weight() != null)
                     cellF.setCellValue(record1.getR46_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 47 / Col B =====
                 row = sheet.getRow(46);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR47_security_firm() != null)
                     cellB.setCellValue(record1.getR47_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R47 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR47_credit_rating() != null)
                     cellC.setCellValue(record1.getR47_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R47 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR47_rating_agency() != null)
                     cellD.setCellValue(record1.getR47_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R47 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR47_exposure_amount() != null)
                     cellE.setCellValue(record1.getR47_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R47 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR47_risk_weight() != null)
                     cellF.setCellValue(record1.getR47_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 48 / Col B =====
                 row = sheet.getRow(47);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR48_security_firm() != null)
                     cellB.setCellValue(record1.getR48_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R48 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR48_credit_rating() != null)
                     cellC.setCellValue(record1.getR48_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R48 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR48_rating_agency() != null)
                     cellD.setCellValue(record1.getR48_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R48 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR48_exposure_amount() != null)
                     cellE.setCellValue(record1.getR48_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R48 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR48_risk_weight() != null)
                     cellF.setCellValue(record1.getR48_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 49 / Col B =====
                 row = sheet.getRow(48);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR49_security_firm() != null)
                     cellB.setCellValue(record1.getR49_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R49 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR49_credit_rating() != null)
                     cellC.setCellValue(record1.getR49_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R49 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR49_rating_agency() != null)
                     cellD.setCellValue(record1.getR49_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R49 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR49_exposure_amount() != null)
                     cellE.setCellValue(record1.getR49_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R49 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR49_risk_weight() != null)
                     cellF.setCellValue(record1.getR49_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 50 / Col B =====
                 row = sheet.getRow(49);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR50_security_firm() != null)
                     cellB.setCellValue(record1.getR50_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R50 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR50_credit_rating() != null)
                     cellC.setCellValue(record1.getR50_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R50 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR50_rating_agency() != null)
                     cellD.setCellValue(record1.getR50_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R50 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR50_exposure_amount() != null)
                     cellE.setCellValue(record1.getR50_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R50 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR50_risk_weight() != null)
                     cellF.setCellValue(record1.getR50_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 51 / Col B =====
                 row = sheet.getRow(50);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR51_security_firm() != null)
                     cellB.setCellValue(record1.getR51_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R51 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR51_credit_rating() != null)
                     cellC.setCellValue(record1.getR51_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R51 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR51_rating_agency() != null)
                     cellD.setCellValue(record1.getR51_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R51 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR51_exposure_amount() != null)
                     cellE.setCellValue(record1.getR51_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R51 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR51_risk_weight() != null)
                     cellF.setCellValue(record1.getR51_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 52 / Col B =====
                 row = sheet.getRow(51);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR52_security_firm() != null)
                     cellB.setCellValue(record1.getR52_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R52 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR52_credit_rating() != null)
                     cellC.setCellValue(record1.getR52_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R52 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR52_rating_agency() != null)
                     cellD.setCellValue(record1.getR52_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R52 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR52_exposure_amount() != null)
                     cellE.setCellValue(record1.getR52_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R52 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR52_risk_weight() != null)
                     cellF.setCellValue(record1.getR52_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 53 / Col B =====
                 row = sheet.getRow(52);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR53_security_firm() != null)
                     cellB.setCellValue(record1.getR53_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R53 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR53_credit_rating() != null)
                     cellC.setCellValue(record1.getR53_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R53 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR53_rating_agency() != null)
                     cellD.setCellValue(record1.getR53_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R53 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR53_exposure_amount() != null)
                     cellE.setCellValue(record1.getR53_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R53 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR53_risk_weight() != null)
                     cellF.setCellValue(record1.getR53_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 54 / Col B =====
                 row = sheet.getRow(53);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR54_security_firm() != null)
                     cellB.setCellValue(record1.getR54_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R54 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR54_credit_rating() != null)
                     cellC.setCellValue(record1.getR54_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R54 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR54_rating_agency() != null)
                     cellD.setCellValue(record1.getR54_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R54 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR54_exposure_amount() != null)
                     cellE.setCellValue(record1.getR54_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R54 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR54_risk_weight() != null)
                     cellF.setCellValue(record1.getR54_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 55 / Col B =====
                 row = sheet.getRow(54);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR55_security_firm() != null)
                     cellB.setCellValue(record1.getR55_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R55 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR55_credit_rating() != null)
                     cellC.setCellValue(record1.getR55_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R55 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR55_rating_agency() != null)
                     cellD.setCellValue(record1.getR55_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R55 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR55_exposure_amount() != null)
                     cellE.setCellValue(record1.getR55_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R55 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR55_risk_weight() != null)
                     cellF.setCellValue(record1.getR55_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 56 / Col B =====
                 row = sheet.getRow(55);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR56_security_firm() != null)
                     cellB.setCellValue(record1.getR56_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R56 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR56_credit_rating() != null)
                     cellC.setCellValue(record1.getR56_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R56 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR56_rating_agency() != null)
                     cellD.setCellValue(record1.getR56_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R56 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR56_exposure_amount() != null)
                     cellE.setCellValue(record1.getR56_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R56 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR56_risk_weight() != null)
                     cellF.setCellValue(record1.getR56_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 57 / Col B =====
                 row = sheet.getRow(56);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR57_security_firm() != null)
                     cellB.setCellValue(record1.getR57_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R57 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR57_credit_rating() != null)
                     cellC.setCellValue(record1.getR57_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R57 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR57_rating_agency() != null)
                     cellD.setCellValue(record1.getR57_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R57 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR57_exposure_amount() != null)
                     cellE.setCellValue(record1.getR57_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R57 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR57_risk_weight() != null)
                     cellF.setCellValue(record1.getR57_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 58 / Col B =====
                 row = sheet.getRow(57);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR58_security_firm() != null)
                     cellB.setCellValue(record1.getR58_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R58 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR58_credit_rating() != null)
                     cellC.setCellValue(record1.getR58_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R58 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR58_rating_agency() != null)
                     cellD.setCellValue(record1.getR58_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R58 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR58_exposure_amount() != null)
                     cellE.setCellValue(record1.getR58_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R58 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR58_risk_weight() != null)
                     cellF.setCellValue(record1.getR58_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 59 / Col B =====
                 row = sheet.getRow(58);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR59_security_firm() != null)
                     cellB.setCellValue(record1.getR59_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R59 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR59_credit_rating() != null)
                     cellC.setCellValue(record1.getR59_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R59 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR59_rating_agency() != null)
                     cellD.setCellValue(record1.getR59_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R59 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR59_exposure_amount() != null)
                     cellE.setCellValue(record1.getR59_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R59 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR59_risk_weight() != null)
                     cellF.setCellValue(record1.getR59_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);
 
                 // ===== Row 60 / Col B =====
                 row = sheet.getRow(59);
                 cellB = row.getCell(1);
                 if (cellB == null) cellB = row.createCell(1);
                 originalStyle = cellB.getCellStyle();
                 if (record1.getR60_security_firm() != null)
                     cellB.setCellValue(record1.getR60_security_firm());
                 else cellB.setCellValue("");
                 cellB.setCellStyle(originalStyle);

                 // ===== R60 / Col C =====
                 cellC = row.getCell(2);
                 if (cellC == null) cellC = row.createCell(2);
                 originalStyle = cellC.getCellStyle();
                 if (record1.getR60_credit_rating() != null)
                     cellC.setCellValue(record1.getR60_credit_rating().doubleValue());
                 else cellC.setCellValue("");
                 cellC.setCellStyle(originalStyle);

                 // ===== R60 / Col D =====
                 cellD = row.getCell(3);
                 if (cellD == null) cellD = row.createCell(3);
                 originalStyle = cellD.getCellStyle();
                 if (record1.getR60_rating_agency() != null)
                     cellD.setCellValue(record1.getR60_rating_agency());
                 else cellD.setCellValue("");
                 cellD.setCellStyle(originalStyle);

                 // ===== R60 / Col E =====
                 cellE = row.getCell(4);
                 if (cellE == null) cellE = row.createCell(4);
                 originalStyle = cellE.getCellStyle();
                 if (record1.getR60_exposure_amount() != null)
                     cellE.setCellValue(record1.getR60_exposure_amount().doubleValue());
                 else cellE.setCellValue("");
                 cellE.setCellStyle(originalStyle);

                 // ===== R60 / Col F =====
                 cellF = row.getCell(5);
                 if (cellF == null) cellF = row.createCell(5);
                 originalStyle = cellF.getCellStyle();
                 if (record1.getR60_risk_weight() != null)
                     cellF.setCellValue(record1.getR60_risk_weight().doubleValue());
                 else cellF.setCellValue("");
                 cellF.setCellStyle(originalStyle);		    
		        		        
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


	
	

