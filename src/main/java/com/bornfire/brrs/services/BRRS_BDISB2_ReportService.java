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


import com.bornfire.brrs.entities.BDISB2_Summary_Entity;
import com.bornfire.brrs.entities.BDISB2_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_BDISB2_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_BDISB2_Archival_Summary_Repo;


@Component
@Service
public class BRRS_BDISB2_ReportService {


	private static final Logger logger = LoggerFactory.getLogger(BRRS_BDISB2_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	BRRS_BDISB2_Summary_Repo	BRRS_BDISB2_Summary_Repo;
	
	@Autowired
	BRRS_BDISB2_Archival_Summary_Repo	BRRS_BDISB2_Archival_Summary_Repo;
				

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBDISB2View(String reportId, String fromdate, String todate, 
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
            List<BDISB2_Archival_Summary_Entity> T1Master = 
                BRRS_BDISB2_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 2: RESUB ----------
        else if ("RESUB".equalsIgnoreCase(type) && version != null) {
            List<BDISB2_Archival_Summary_Entity> T1Master =
                BRRS_BDISB2_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 3: NORMAL ----------
        else {
            List<BDISB2_Summary_Entity> T1Master = 
                BRRS_BDISB2_Summary_Repo.getdatabydateListWithVersion(todate);
            System.out.println("T1Master Size "+T1Master.size());
            mv.addObject("reportsummary", T1Master);
        }

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/BDISB2");
		mv.addObject("displaymode", "summary");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}
		
//		
//		else if ("RESUB".equalsIgnoreCase(type) && version != null) {
//            List<BDISB2_Resub_Summary_Entity1> T1Master = new ArrayList<BDISB2_Resub_Summary_Entity1>();
//    
//            try {
//				Date d1 = dateformat.parse(todate);
//            T1Master = BRRS_BDISB2_Resub_Summary_Repo1.getdatabydateListResub(dateformat.parse(todate), version);
//             
//            T2Master = BRRS_BDISB2_Resub_Summary_Repo2.getdatabydateListResub(dateformat.parse(todate), version);
//            
//            T3Master = BRRS_BDISB2_Resub_Summary_Repo3.getdatabydateListResub(dateformat.parse(todate), version);
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
//			List<BDISB2_Summary_Entity> T1Master = new ArrayList<BDISB2_Summary_Entity>();
//	
//			
//			try {
//				Date d1 = dateformat.parse(todate);
//
//				T1Master = BRRS_BDISB2_Summary_Repo.getdatabydateList(dateformat.parse(todate));
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
//		mv.setViewName("BRRS/BDISB2");
//		mv.addObject("displaymode", "summary");
//		System.out.println("scv" + mv.getViewName());
//		return mv;
//	}
	
	
	public void updateReport(BDISB2_Summary_Entity updatedEntity) {
	    System.out.println("Came to services1");
	    System.out.println("Report Date: " + updatedEntity.getReportDate());

	    BDISB2_Summary_Entity existing = BRRS_BDISB2_Summary_Repo
				.findTopByReportDateOrderByReportVersionDesc(updatedEntity.getReportDate())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

	    try {
	        // 1️⃣ Loop from R6 to R12 and copy fields
	        for (int i = 6; i <= 12; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = { "bank_spec_single_cust_rec_num", "company_name", "company_reg_num", "businees_phy_address",
	            		  "postal_address", "country_of_reg", "company_email", "company_landline", "company_mob_phone_num",
	            		  "product_type", "acct_num", "status_of_acct", "acct_status_fit_or_not_fit_for_straight_throu_payout",
	            		  "acct_branch", "acct_balance_pula", "currency_of_acct", "exchange_rate" };

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = BDISB2_Summary_Entity.class.getMethod(getterName);
	                    Method setter = BDISB2_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

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
	    BRRS_BDISB2_Summary_Repo.save(existing);
	}

	
//	public List<Object> getBDISB2Resub() {
//	    List<Object> BDISB2Resub = new ArrayList<>();
//	    try {
//	        List<Object> list1 = BRRS_BDISB2_Resub_Summary_Repo1.getBDISB2Resub();
//	        List<Object> list2 = BRRS_BDISB2_Resub_Summary_Repo2.getBDISB2Resub();
//	        List<Object> list3 = BRRS_BDISB2_Resub_Summary_Repo3.getBDISB2Resub();
//
//	        BDISB2Resub.addAll(list1);
//	        BDISB2Resub.addAll(list2);
//	        BDISB2Resub.addAll(list3);
//
//	        System.out.println("Total combined size: " + BDISB2Resub.size());
//	    } catch (Exception e) {
//	        System.err.println("Error fetching BDISB2 Resub data: " + e.getMessage());
//	        e.printStackTrace();
//	    }
//	    return BDISB2Resub;
//	}
//	
//	public void updateReportResub1(BDISB2_Resub_Summary_Entity1 updatedEntity) {
//	    System.out.println("Came to services1");
//	    System.out.println("Report Date: " + updatedEntity.getReportDate());
//
//	    BDISB2_Resub_Summary_Entity1 existing = BRRS_BDISB2_Resub_Summary_Repo1.findById(updatedEntity.getReportDate())
//	            .orElseThrow(() -> new RuntimeException(
//	                    "Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));
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
//	                    Method getter = BDISB2_Resub_Summary_Entity1.class.getMethod(getterName);
//	                    Method setter = BDISB2_Resub_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());
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
//	                Method getter = BDISB2_Resub_Summary_Entity1.class.getMethod(getterName);
//	                Method setter = BDISB2_Resub_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());
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
//	    BRRS_BDISB2_Resub_Summary_Repo1.save(existing);
//	}
//
//	public void updateReportResub2(BDISB2_Resub_Summary_Entity2 updatedEntity) {
//	    System.out.println("Came to services2");
//	    System.out.println("Report Date: " + updatedEntity.getReportDate());
//
//	    BDISB2_Resub_Summary_Entity2 existing = BRRS_BDISB2_Resub_Summary_Repo2.findById(updatedEntity.getReportDate())
//	            .orElseThrow(() -> new RuntimeException(
//	                    "Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));
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
//	                    Method getter = BDISB2_Resub_Summary_Entity2.class.getMethod(getterName);
//	                    Method setter = BDISB2_Resub_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());
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
//	                    Method getter = BDISB2_Resub_Summary_Entity2.class.getMethod(getterName);
//	                    Method setter = BDISB2_Resub_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());
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
//	                Method getter = BDISB2_Resub_Summary_Entity2.class.getMethod(getterName);
//	                Method setter = BDISB2_Resub_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());
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
//	    BRRS_BDISB2_Resub_Summary_Repo2.save(existing);
//	}
//
//	public void updateReportResub3(BDISB2_Resub_Summary_Entity3 updatedEntity) {
//	    System.out.println("Came to services3");
//	    System.out.println("Report Date: " + updatedEntity.getReportDate());
//
//	    BDISB2_Resub_Summary_Entity3 existing = BRRS_BDISB2_Resub_Summary_Repo3.findById(updatedEntity.getReportDate())
//	            .orElseThrow(() -> new RuntimeException(
//	                    "Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));
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
//	                    Method getter = BDISB2_Resub_Summary_Entity3.class.getMethod(getterName);
//	                    Method setter = BDISB2_Resub_Summary_Entity3.class.getMethod(setterName, getter.getReturnType());
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
//	    BRRS_BDISB2_Resub_Summary_Repo3.save(existing);
//}
//



	public byte[] getBDISB2Excel(String filename, String reportId, String fromdate, String todate, String currency,
									 String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

		// Convert string to Date
		Date reportDate = dateformat.parse(todate);

    // ARCHIVAL check
    if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
        logger.info("Service: Generating ARCHIVAL report for version {}", version);
        return getExcelBDISB2ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
    }
    // RESUB check
    else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
        logger.info("Service: Generating RESUB report for version {}", version);

       
        List<BDISB2_Archival_Summary_Entity> T1Master =
                BRRS_BDISB2_Archival_Summary_Repo.getdatabydateListarchival(reportDate, version);

        // Generate Excel for RESUB
        return BRRS_BDISB2ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
    }




		// Default (LIVE) case
		List<BDISB2_Summary_Entity> dataList1 = BRRS_BDISB2_Summary_Repo.getdatabydateList(reportDate);

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

			int startRow = 5;

			if (!dataList1.isEmpty()) {
			    for (int i = 0; i < dataList1.size(); i++) {
			        BDISB2_Summary_Entity record1 = dataList1.get(i);
			        System.out.println("rownumber=" + (startRow + i));
					
				       
			        Row row;
			        Cell cellA, cellB, cellC, cellD, cellE, cellF, cellG,
			        cellH, cellI, cellJ, cellK, cellL, cellM, cellN, cellO,
			        cellP, cellQ;
			        CellStyle originalStyle;



					// ===== R6 / Col A =====
			        row = sheet.getRow(5);
			        cellA = row.getCell(0);
			        if (cellA == null) cellA = row.createCell(0);
			        originalStyle = cellA.getCellStyle();
			        if (record1.getR6_bank_spec_single_cust_rec_num() != null) 
			        cellA.setCellValue(record1.getR6_bank_spec_single_cust_rec_num().doubleValue());
			        else cellA.setCellValue("");
			        cellA.setCellStyle(originalStyle);


					// ===== R6 / Col B =====

			        cellB = row.getCell(1);
			        if (cellB == null) cellB = row.createCell(1);
			        originalStyle = cellB.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_company_name() != null) 
			        cellB.setCellValue(record1.getR6_company_name()); // String directly
			        else cellB.setCellValue("");
			        cellB.setCellStyle(originalStyle);
			        
					// ===== R6 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR6_company_reg_num() != null) 
			        cellC.setCellValue(record1.getR6_company_reg_num().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);	
			        
					// ===== R6 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_businees_phy_address() != null) 
			        cellD.setCellValue(record1.getR6_businees_phy_address()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R6 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_postal_address() != null) 
			        cellE.setCellValue(record1.getR6_postal_address()); // String directly
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);		
			        
					// ===== R6 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_country_of_reg() != null) 
			        cellF.setCellValue(record1.getR6_country_of_reg()); // String directly
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);			  
			        
					// ===== R6 / Col G =====

			        cellG = row.getCell(6);
			        if (cellG == null) cellG = row.createCell(6);
			        originalStyle = cellG.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_company_email() != null) 
			        cellG.setCellValue(record1.getR6_company_email()); // String directly
			        else cellG.setCellValue("");
			        cellG.setCellStyle(originalStyle);		
			        
					// ===== R6 / Col H =====

			        cellH = row.getCell(7);
			        if (cellH == null) cellH = row.createCell(7);
			        originalStyle = cellH.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_company_landline() != null) 
			        cellH.setCellValue(record1.getR6_company_landline()); // String directly
			        else cellH.setCellValue("");
			        cellH.setCellStyle(originalStyle);	
			        
					// ===== R6 / Col I =====

			        cellI = row.getCell(8);
			        if (cellI == null) cellI = row.createCell(8);
			        originalStyle = cellI.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_company_mob_phone_num() != null) 
			        cellI.setCellValue(record1.getR6_company_mob_phone_num()); // String directly
			        else cellI.setCellValue("");
			        cellI.setCellStyle(originalStyle);		
			        
					// ===== R6 / Col J =====

			        cellJ = row.getCell(9);
			        if (cellJ == null) cellJ = row.createCell(9);
			        originalStyle = cellJ.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_product_type() != null) 
			        cellJ.setCellValue(record1.getR6_product_type()); // String directly
			        else cellJ.setCellValue("");
			        cellJ.setCellStyle(originalStyle);
			        
					// ===== R6 / Col K =====
			        
			        cellK = row.getCell(10);
			        if (cellK == null) cellK = row.createCell(10);
			        originalStyle = cellK.getCellStyle();
			        if (record1.getR6_acct_num() != null) 
			        cellK.setCellValue(record1.getR6_acct_num().doubleValue());
			        else cellK.setCellValue("");
			        cellK.setCellStyle(originalStyle);
			        
					// ===== R6 / Col L =====

			        cellL = row.getCell(11);
			        if (cellL == null) cellL = row.createCell(11);
			        originalStyle = cellL.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_status_of_acct() != null) 
			        cellL.setCellValue(record1.getR6_status_of_acct()); // String directly
			        else cellL.setCellValue("");
			        cellL.setCellStyle(originalStyle);
			        
					// ===== R6 / Col M =====

			        cellM = row.getCell(12);
			        if (cellM == null) cellM = row.createCell(12);
			        originalStyle = cellM.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_acct_status_fit_or_not_fit_for_straight_throu_payout() != null) 
			        cellM.setCellValue(record1.getR6_acct_status_fit_or_not_fit_for_straight_throu_payout()); // String directly
			        else cellM.setCellValue("");
			        cellM.setCellStyle(originalStyle);
			        
					// ===== R6 / Col N =====

			        cellN = row.getCell(13);
			        if (cellN == null) cellN = row.createCell(13);
			        originalStyle = cellN.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_acct_branch() != null) 
			        cellN.setCellValue(record1.getR6_acct_branch()); // String directly
			        else cellN.setCellValue("");
			        cellN.setCellStyle(originalStyle);
			        

					// ===== R6 / Col O =====
			        
			        cellO = row.getCell(14);
			        if (cellO == null) cellO = row.createCell(14);
			        originalStyle = cellO.getCellStyle();
			        if (record1.getR6_acct_balance_pula() != null) 
			        cellO.setCellValue(record1.getR6_acct_balance_pula().doubleValue());
			        else cellO.setCellValue("");
			        cellO.setCellStyle(originalStyle);
			        
			        
					// ===== R6 / Col P =====

			        cellP = row.getCell(15);
			        if (cellP == null) cellP = row.createCell(15);
			        originalStyle = cellP.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_currency_of_acct() != null) 
			        cellP.setCellValue(record1.getR6_currency_of_acct()); // String directly
			        else cellP.setCellValue("");
			        cellP.setCellStyle(originalStyle);
			        
					// ===== R6 / Col Q =====
			        
			        cellQ = row.getCell(16);
			        if (cellQ == null) cellQ = row.createCell(16);
			        originalStyle = cellQ.getCellStyle();
			        if (record1.getR6_exchange_rate() != null) 
			        cellQ.setCellValue(record1.getR6_exchange_rate().doubleValue());
			        else cellQ.setCellValue("");
			        cellQ.setCellStyle(originalStyle);
			        
			        


					// ===== R7 / Col A =====
			        row = sheet.getRow(6);
			        cellA = row.getCell(0);
			        if (cellA == null) cellA = row.createCell(0);
			        originalStyle = cellA.getCellStyle();
			        if (record1.getR7_bank_spec_single_cust_rec_num() != null) 
			        cellA.setCellValue(record1.getR7_bank_spec_single_cust_rec_num().doubleValue());
			        else cellA.setCellValue("");
			        cellA.setCellStyle(originalStyle);


					// ===== R7 / Col B =====

			        cellB = row.getCell(1);
			        if (cellB == null) cellB = row.createCell(1);
			        originalStyle = cellB.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_company_name() != null) 
			        cellB.setCellValue(record1.getR7_company_name()); // String directly
			        else cellB.setCellValue("");
			        cellB.setCellStyle(originalStyle);
			        
					// ===== R7 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR7_company_reg_num() != null) 
			        cellC.setCellValue(record1.getR7_company_reg_num().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);	
			        
					// ===== R7 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_businees_phy_address() != null) 
			        cellD.setCellValue(record1.getR7_businees_phy_address()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R7 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_postal_address() != null) 
			        cellE.setCellValue(record1.getR7_postal_address()); // String directly
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);		
			        
					// ===== R7 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_country_of_reg() != null) 
			        cellF.setCellValue(record1.getR7_country_of_reg()); // String directly
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);			  
			        
					// ===== R7 / Col G =====

			        cellG = row.getCell(6);
			        if (cellG == null) cellG = row.createCell(6);
			        originalStyle = cellG.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_company_email() != null) 
			        cellG.setCellValue(record1.getR7_company_email()); // String directly
			        else cellG.setCellValue("");
			        cellG.setCellStyle(originalStyle);		
			        
					// ===== R7 / Col H =====

			        cellH = row.getCell(7);
			        if (cellH == null) cellH = row.createCell(7);
			        originalStyle = cellH.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_company_landline() != null) 
			        cellH.setCellValue(record1.getR7_company_landline()); // String directly
			        else cellH.setCellValue("");
			        cellH.setCellStyle(originalStyle);	
			        
					// ===== R7 / Col I =====

			        cellI = row.getCell(8);
			        if (cellI == null) cellI = row.createCell(8);
			        originalStyle = cellI.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_company_mob_phone_num() != null) 
			        cellI.setCellValue(record1.getR7_company_mob_phone_num()); // String directly
			        else cellI.setCellValue("");
			        cellI.setCellStyle(originalStyle);		
			        
					// ===== R7 / Col J =====

			        cellJ = row.getCell(9);
			        if (cellJ == null) cellJ = row.createCell(9);
			        originalStyle = cellJ.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_product_type() != null) 
			        cellJ.setCellValue(record1.getR7_product_type()); // String directly
			        else cellJ.setCellValue("");
			        cellJ.setCellStyle(originalStyle);
			        
					// ===== R7 / Col K =====
			        
			        cellK = row.getCell(10);
			        if (cellK == null) cellK = row.createCell(10);
			        originalStyle = cellK.getCellStyle();
			        if (record1.getR7_acct_num() != null) 
			        cellK.setCellValue(record1.getR7_acct_num().doubleValue());
			        else cellK.setCellValue("");
			        cellK.setCellStyle(originalStyle);
			        
					// ===== R7 / Col L =====

			        cellL = row.getCell(11);
			        if (cellL == null) cellL = row.createCell(11);
			        originalStyle = cellL.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_status_of_acct() != null) 
			        cellL.setCellValue(record1.getR7_status_of_acct()); // String directly
			        else cellL.setCellValue("");
			        cellL.setCellStyle(originalStyle);
			        
					// ===== R7 / Col M =====

			        cellM = row.getCell(12);
			        if (cellM == null) cellM = row.createCell(12);
			        originalStyle = cellM.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_acct_status_fit_or_not_fit_for_straight_throu_payout() != null) 
			        cellM.setCellValue(record1.getR7_acct_status_fit_or_not_fit_for_straight_throu_payout()); // String directly
			        else cellM.setCellValue("");
			        cellM.setCellStyle(originalStyle);
			        
					// ===== R7 / Col N =====

			        cellN = row.getCell(13);
			        if (cellN == null) cellN = row.createCell(13);
			        originalStyle = cellN.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_acct_branch() != null) 
			        cellN.setCellValue(record1.getR7_acct_branch()); // String directly
			        else cellN.setCellValue("");
			        cellN.setCellStyle(originalStyle);
			        

					// ===== R7 / Col O =====
			        
			        cellO = row.getCell(14);
			        if (cellO == null) cellO = row.createCell(14);
			        originalStyle = cellO.getCellStyle();
			        if (record1.getR7_acct_balance_pula() != null) 
			        cellO.setCellValue(record1.getR7_acct_balance_pula().doubleValue());
			        else cellO.setCellValue("");
			        cellO.setCellStyle(originalStyle);
			        
			        
					// ===== R7 / Col P =====

			        cellP = row.getCell(15);
			        if (cellP == null) cellP = row.createCell(15);
			        originalStyle = cellP.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_currency_of_acct() != null) 
			        cellP.setCellValue(record1.getR7_currency_of_acct()); // String directly
			        else cellP.setCellValue("");
			        cellP.setCellStyle(originalStyle);
			        
					// ===== R7 / Col Q =====
			        
			        cellQ = row.getCell(16);
			        if (cellQ == null) cellQ = row.createCell(16);
			        originalStyle = cellQ.getCellStyle();
			        if (record1.getR7_exchange_rate() != null) 
			        cellQ.setCellValue(record1.getR7_exchange_rate().doubleValue());
			        else cellQ.setCellValue("");
			        cellQ.setCellStyle(originalStyle);



					// ===== R8 / Col A =====
			        row = sheet.getRow(7);
			        cellA = row.getCell(0);
			        if (cellA == null) cellA = row.createCell(0);
			        originalStyle = cellA.getCellStyle();
			        if (record1.getR8_bank_spec_single_cust_rec_num() != null) 
			        cellA.setCellValue(record1.getR8_bank_spec_single_cust_rec_num().doubleValue());
			        else cellA.setCellValue("");
			        cellA.setCellStyle(originalStyle);


					// ===== R8 / Col B =====

			        cellB = row.getCell(1);
			        if (cellB == null) cellB = row.createCell(1);
			        originalStyle = cellB.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_company_name() != null) 
			        cellB.setCellValue(record1.getR8_company_name()); // String directly
			        else cellB.setCellValue("");
			        cellB.setCellStyle(originalStyle);
			        
					// ===== R8 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR8_company_reg_num() != null) 
			        cellC.setCellValue(record1.getR8_company_reg_num().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);	
			        
					// ===== R8 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_businees_phy_address() != null) 
			        cellD.setCellValue(record1.getR8_businees_phy_address()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R8 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_postal_address() != null) 
			        cellE.setCellValue(record1.getR8_postal_address()); // String directly
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);		
			        
					// ===== R8 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_country_of_reg() != null) 
			        cellF.setCellValue(record1.getR8_country_of_reg()); // String directly
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);			  
			        
					// ===== R8 / Col G =====

			        cellG = row.getCell(6);
			        if (cellG == null) cellG = row.createCell(6);
			        originalStyle = cellG.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_company_email() != null) 
			        cellG.setCellValue(record1.getR8_company_email()); // String directly
			        else cellG.setCellValue("");
			        cellG.setCellStyle(originalStyle);		
			        
					// ===== R8 / Col H =====

			        cellH = row.getCell(7);
			        if (cellH == null) cellH = row.createCell(7);
			        originalStyle = cellH.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_company_landline() != null) 
			        cellH.setCellValue(record1.getR8_company_landline()); // String directly
			        else cellH.setCellValue("");
			        cellH.setCellStyle(originalStyle);	
			        
					// ===== R8 / Col I =====

			        cellI = row.getCell(8);
			        if (cellI == null) cellI = row.createCell(8);
			        originalStyle = cellI.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_company_mob_phone_num() != null) 
			        cellI.setCellValue(record1.getR8_company_mob_phone_num()); // String directly
			        else cellI.setCellValue("");
			        cellI.setCellStyle(originalStyle);		
			        
					// ===== R8 / Col J =====

			        cellJ = row.getCell(9);
			        if (cellJ == null) cellJ = row.createCell(9);
			        originalStyle = cellJ.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_product_type() != null) 
			        cellJ.setCellValue(record1.getR8_product_type()); // String directly
			        else cellJ.setCellValue("");
			        cellJ.setCellStyle(originalStyle);
			        
					// ===== R8 / Col K =====
			        
			        cellK = row.getCell(10);
			        if (cellK == null) cellK = row.createCell(10);
			        originalStyle = cellK.getCellStyle();
			        if (record1.getR8_acct_num() != null) 
			        cellK.setCellValue(record1.getR8_acct_num().doubleValue());
			        else cellK.setCellValue("");
			        cellK.setCellStyle(originalStyle);
			        
					// ===== R8 / Col L =====

			        cellL = row.getCell(11);
			        if (cellL == null) cellL = row.createCell(11);
			        originalStyle = cellL.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_status_of_acct() != null) 
			        cellL.setCellValue(record1.getR8_status_of_acct()); // String directly
			        else cellL.setCellValue("");
			        cellL.setCellStyle(originalStyle);
			        
					// ===== R8 / Col M =====

			        cellM = row.getCell(12);
			        if (cellM == null) cellM = row.createCell(12);
			        originalStyle = cellM.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_acct_status_fit_or_not_fit_for_straight_throu_payout() != null) 
			        cellM.setCellValue(record1.getR8_acct_status_fit_or_not_fit_for_straight_throu_payout()); // String directly
			        else cellM.setCellValue("");
			        cellM.setCellStyle(originalStyle);
			        
					// ===== R8 / Col N =====

			        cellN = row.getCell(13);
			        if (cellN == null) cellN = row.createCell(13);
			        originalStyle = cellN.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_acct_branch() != null) 
			        cellN.setCellValue(record1.getR8_acct_branch()); // String directly
			        else cellN.setCellValue("");
			        cellN.setCellStyle(originalStyle);
			        

					// ===== R8 / Col O =====
			        
			        cellO = row.getCell(14);
			        if (cellO == null) cellO = row.createCell(14);
			        originalStyle = cellO.getCellStyle();
			        if (record1.getR8_acct_balance_pula() != null) 
			        cellO.setCellValue(record1.getR8_acct_balance_pula().doubleValue());
			        else cellO.setCellValue("");
			        cellO.setCellStyle(originalStyle);
			        
			        
					// ===== R8 / Col P =====

			        cellP = row.getCell(15);
			        if (cellP == null) cellP = row.createCell(15);
			        originalStyle = cellP.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_currency_of_acct() != null) 
			        cellP.setCellValue(record1.getR8_currency_of_acct()); // String directly
			        else cellP.setCellValue("");
			        cellP.setCellStyle(originalStyle);
			        
					// ===== R8 / Col Q =====
			        
			        cellQ = row.getCell(16);
			        if (cellQ == null) cellQ = row.createCell(16);
			        originalStyle = cellQ.getCellStyle();
			        if (record1.getR8_exchange_rate() != null) 
			        cellQ.setCellValue(record1.getR8_exchange_rate().doubleValue());
			        else cellQ.setCellValue("");
			        cellQ.setCellStyle(originalStyle);



					// ===== R9 / Col A =====
			        row = sheet.getRow(8);
			        cellA = row.getCell(0);
			        if (cellA == null) cellA = row.createCell(0);
			        originalStyle = cellA.getCellStyle();
			        if (record1.getR9_bank_spec_single_cust_rec_num() != null) 
			        cellA.setCellValue(record1.getR9_bank_spec_single_cust_rec_num().doubleValue());
			        else cellA.setCellValue("");
			        cellA.setCellStyle(originalStyle);


					// ===== R9 / Col B =====

			        cellB = row.getCell(1);
			        if (cellB == null) cellB = row.createCell(1);
			        originalStyle = cellB.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_company_name() != null) 
			        cellB.setCellValue(record1.getR9_company_name()); // String directly
			        else cellB.setCellValue("");
			        cellB.setCellStyle(originalStyle);
			        
					// ===== R9 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR9_company_reg_num() != null) 
			        cellC.setCellValue(record1.getR9_company_reg_num().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);	
			        
					// ===== R9 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_businees_phy_address() != null) 
			        cellD.setCellValue(record1.getR9_businees_phy_address()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R9 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_postal_address() != null) 
			        cellE.setCellValue(record1.getR9_postal_address()); // String directly
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);		
			        
					// ===== R9 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_country_of_reg() != null) 
			        cellF.setCellValue(record1.getR9_country_of_reg()); // String directly
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);			  
			        
					// ===== R9 / Col G =====

			        cellG = row.getCell(6);
			        if (cellG == null) cellG = row.createCell(6);
			        originalStyle = cellG.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_company_email() != null) 
			        cellG.setCellValue(record1.getR9_company_email()); // String directly
			        else cellG.setCellValue("");
			        cellG.setCellStyle(originalStyle);		
			        
					// ===== R9 / Col H =====

			        cellH = row.getCell(7);
			        if (cellH == null) cellH = row.createCell(7);
			        originalStyle = cellH.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_company_landline() != null) 
			        cellH.setCellValue(record1.getR9_company_landline()); // String directly
			        else cellH.setCellValue("");
			        cellH.setCellStyle(originalStyle);	
			        
					// ===== R9 / Col I =====

			        cellI = row.getCell(8);
			        if (cellI == null) cellI = row.createCell(8);
			        originalStyle = cellI.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_company_mob_phone_num() != null) 
			        cellI.setCellValue(record1.getR9_company_mob_phone_num()); // String directly
			        else cellI.setCellValue("");
			        cellI.setCellStyle(originalStyle);		
			        
					// ===== R9 / Col J =====

			        cellJ = row.getCell(9);
			        if (cellJ == null) cellJ = row.createCell(9);
			        originalStyle = cellJ.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_product_type() != null) 
			        cellJ.setCellValue(record1.getR9_product_type()); // String directly
			        else cellJ.setCellValue("");
			        cellJ.setCellStyle(originalStyle);
			        
					// ===== R9 / Col K =====
			        
			        cellK = row.getCell(10);
			        if (cellK == null) cellK = row.createCell(10);
			        originalStyle = cellK.getCellStyle();
			        if (record1.getR9_acct_num() != null) 
			        cellK.setCellValue(record1.getR9_acct_num().doubleValue());
			        else cellK.setCellValue("");
			        cellK.setCellStyle(originalStyle);
			        
					// ===== R9 / Col L =====

			        cellL = row.getCell(11);
			        if (cellL == null) cellL = row.createCell(11);
			        originalStyle = cellL.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_status_of_acct() != null) 
			        cellL.setCellValue(record1.getR9_status_of_acct()); // String directly
			        else cellL.setCellValue("");
			        cellL.setCellStyle(originalStyle);
			        
					// ===== R9 / Col M =====

			        cellM = row.getCell(12);
			        if (cellM == null) cellM = row.createCell(12);
			        originalStyle = cellM.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_acct_status_fit_or_not_fit_for_straight_throu_payout() != null) 
			        cellM.setCellValue(record1.getR9_acct_status_fit_or_not_fit_for_straight_throu_payout()); // String directly
			        else cellM.setCellValue("");
			        cellM.setCellStyle(originalStyle);
			        
					// ===== R9 / Col N =====

			        cellN = row.getCell(13);
			        if (cellN == null) cellN = row.createCell(13);
			        originalStyle = cellN.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_acct_branch() != null) 
			        cellN.setCellValue(record1.getR9_acct_branch()); // String directly
			        else cellN.setCellValue("");
			        cellN.setCellStyle(originalStyle);
			        

					// ===== R9 / Col O =====
			        
			        cellO = row.getCell(14);
			        if (cellO == null) cellO = row.createCell(14);
			        originalStyle = cellO.getCellStyle();
			        if (record1.getR9_acct_balance_pula() != null) 
			        cellO.setCellValue(record1.getR9_acct_balance_pula().doubleValue());
			        else cellO.setCellValue("");
			        cellO.setCellStyle(originalStyle);
			        
			        
					// ===== R9 / Col P =====

			        cellP = row.getCell(15);
			        if (cellP == null) cellP = row.createCell(15);
			        originalStyle = cellP.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_currency_of_acct() != null) 
			        cellP.setCellValue(record1.getR9_currency_of_acct()); // String directly
			        else cellP.setCellValue("");
			        cellP.setCellStyle(originalStyle);
			        
					// ===== R9 / Col Q =====
			        
			        cellQ = row.getCell(16);
			        if (cellQ == null) cellQ = row.createCell(16);
			        originalStyle = cellQ.getCellStyle();
			        if (record1.getR9_exchange_rate() != null) 
			        cellQ.setCellValue(record1.getR9_exchange_rate().doubleValue());
			        else cellQ.setCellValue("");
			        cellQ.setCellStyle(originalStyle);
			        


					// ===== R10 / Col A =====
			        row = sheet.getRow(9);
			        cellA = row.getCell(0);
			        if (cellA == null) cellA = row.createCell(0);
			        originalStyle = cellA.getCellStyle();
			        if (record1.getR10_bank_spec_single_cust_rec_num() != null) 
			        cellA.setCellValue(record1.getR10_bank_spec_single_cust_rec_num().doubleValue());
			        else cellA.setCellValue("");
			        cellA.setCellStyle(originalStyle);


					// ===== R10 / Col B =====

			        cellB = row.getCell(1);
			        if (cellB == null) cellB = row.createCell(1);
			        originalStyle = cellB.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_company_name() != null) 
			        cellB.setCellValue(record1.getR10_company_name()); // String directly
			        else cellB.setCellValue("");
			        cellB.setCellStyle(originalStyle);
			        
					// ===== R10 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR10_company_reg_num() != null) 
			        cellC.setCellValue(record1.getR10_company_reg_num().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);	
			        
					// ===== R10 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_businees_phy_address() != null) 
			        cellD.setCellValue(record1.getR10_businees_phy_address()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R10 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_postal_address() != null) 
			        cellE.setCellValue(record1.getR10_postal_address()); // String directly
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);		
			        
					// ===== R10 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_country_of_reg() != null) 
			        cellF.setCellValue(record1.getR10_country_of_reg()); // String directly
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);			  
			        
					// ===== R10 / Col G =====

			        cellG = row.getCell(6);
			        if (cellG == null) cellG = row.createCell(6);
			        originalStyle = cellG.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_company_email() != null) 
			        cellG.setCellValue(record1.getR10_company_email()); // String directly
			        else cellG.setCellValue("");
			        cellG.setCellStyle(originalStyle);		
			        
					// ===== R10 / Col H =====

			        cellH = row.getCell(7);
			        if (cellH == null) cellH = row.createCell(7);
			        originalStyle = cellH.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_company_landline() != null) 
			        cellH.setCellValue(record1.getR10_company_landline()); // String directly
			        else cellH.setCellValue("");
			        cellH.setCellStyle(originalStyle);	
			        
					// ===== R10 / Col I =====

			        cellI = row.getCell(8);
			        if (cellI == null) cellI = row.createCell(8);
			        originalStyle = cellI.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_company_mob_phone_num() != null) 
			        cellI.setCellValue(record1.getR10_company_mob_phone_num()); // String directly
			        else cellI.setCellValue("");
			        cellI.setCellStyle(originalStyle);		
			        
					// ===== R10 / Col J =====

			        cellJ = row.getCell(9);
			        if (cellJ == null) cellJ = row.createCell(9);
			        originalStyle = cellJ.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_product_type() != null) 
			        cellJ.setCellValue(record1.getR10_product_type()); // String directly
			        else cellJ.setCellValue("");
			        cellJ.setCellStyle(originalStyle);
			        
					// ===== R10 / Col K =====
			        
			        cellK = row.getCell(10);
			        if (cellK == null) cellK = row.createCell(10);
			        originalStyle = cellK.getCellStyle();
			        if (record1.getR10_acct_num() != null) 
			        cellK.setCellValue(record1.getR10_acct_num().doubleValue());
			        else cellK.setCellValue("");
			        cellK.setCellStyle(originalStyle);
			        
					// ===== R10 / Col L =====

			        cellL = row.getCell(11);
			        if (cellL == null) cellL = row.createCell(11);
			        originalStyle = cellL.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_status_of_acct() != null) 
			        cellL.setCellValue(record1.getR10_status_of_acct()); // String directly
			        else cellL.setCellValue("");
			        cellL.setCellStyle(originalStyle);
			        
					// ===== R10 / Col M =====

			        cellM = row.getCell(12);
			        if (cellM == null) cellM = row.createCell(12);
			        originalStyle = cellM.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_acct_status_fit_or_not_fit_for_straight_throu_payout() != null) 
			        cellM.setCellValue(record1.getR10_acct_status_fit_or_not_fit_for_straight_throu_payout()); // String directly
			        else cellM.setCellValue("");
			        cellM.setCellStyle(originalStyle);
			        
					// ===== R10 / Col N =====

			        cellN = row.getCell(13);
			        if (cellN == null) cellN = row.createCell(13);
			        originalStyle = cellN.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_acct_branch() != null) 
			        cellN.setCellValue(record1.getR10_acct_branch()); // String directly
			        else cellN.setCellValue("");
			        cellN.setCellStyle(originalStyle);
			        

					// ===== R10 / Col O =====
			        
			        cellO = row.getCell(14);
			        if (cellO == null) cellO = row.createCell(14);
			        originalStyle = cellO.getCellStyle();
			        if (record1.getR10_acct_balance_pula() != null) 
			        cellO.setCellValue(record1.getR10_acct_balance_pula().doubleValue());
			        else cellO.setCellValue("");
			        cellO.setCellStyle(originalStyle);
			        
			        
					// ===== R10 / Col P =====

			        cellP = row.getCell(15);
			        if (cellP == null) cellP = row.createCell(15);
			        originalStyle = cellP.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_currency_of_acct() != null) 
			        cellP.setCellValue(record1.getR10_currency_of_acct()); // String directly
			        else cellP.setCellValue("");
			        cellP.setCellStyle(originalStyle);
			        
					// ===== R10 / Col Q =====
			        
			        cellQ = row.getCell(16);
			        if (cellQ == null) cellQ = row.createCell(16);
			        originalStyle = cellQ.getCellStyle();
			        if (record1.getR10_exchange_rate() != null) 
			        cellQ.setCellValue(record1.getR10_exchange_rate().doubleValue());
			        else cellQ.setCellValue("");
			        cellQ.setCellStyle(originalStyle);


					// ===== R11 / Col A =====
			        row = sheet.getRow(10);
			        cellA = row.getCell(0);
			        if (cellA == null) cellA = row.createCell(0);
			        originalStyle = cellA.getCellStyle();
			        if (record1.getR11_bank_spec_single_cust_rec_num() != null) 
			        cellA.setCellValue(record1.getR11_bank_spec_single_cust_rec_num().doubleValue());
			        else cellA.setCellValue("");
			        cellA.setCellStyle(originalStyle);


					// ===== R11 / Col B =====

			        cellB = row.getCell(1);
			        if (cellB == null) cellB = row.createCell(1);
			        originalStyle = cellB.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_company_name() != null) 
			        cellB.setCellValue(record1.getR11_company_name()); // String directly
			        else cellB.setCellValue("");
			        cellB.setCellStyle(originalStyle);
			        
					// ===== R11 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR11_company_reg_num() != null) 
			        cellC.setCellValue(record1.getR11_company_reg_num().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);	
			        
					// ===== R11 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_businees_phy_address() != null) 
			        cellD.setCellValue(record1.getR11_businees_phy_address()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R11 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_postal_address() != null) 
			        cellE.setCellValue(record1.getR11_postal_address()); // String directly
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);		
			        
					// ===== R11 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_country_of_reg() != null) 
			        cellF.setCellValue(record1.getR11_country_of_reg()); // String directly
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);			  
			        
					// ===== R11 / Col G =====

			        cellG = row.getCell(6);
			        if (cellG == null) cellG = row.createCell(6);
			        originalStyle = cellG.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_company_email() != null) 
			        cellG.setCellValue(record1.getR11_company_email()); // String directly
			        else cellG.setCellValue("");
			        cellG.setCellStyle(originalStyle);		
			        
					// ===== R11 / Col H =====

			        cellH = row.getCell(7);
			        if (cellH == null) cellH = row.createCell(7);
			        originalStyle = cellH.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_company_landline() != null) 
			        cellH.setCellValue(record1.getR11_company_landline()); // String directly
			        else cellH.setCellValue("");
			        cellH.setCellStyle(originalStyle);	
			        
					// ===== R11 / Col I =====

			        cellI = row.getCell(8);
			        if (cellI == null) cellI = row.createCell(8);
			        originalStyle = cellI.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_company_mob_phone_num() != null) 
			        cellI.setCellValue(record1.getR11_company_mob_phone_num()); // String directly
			        else cellI.setCellValue("");
			        cellI.setCellStyle(originalStyle);		
			        
					// ===== R11 / Col J =====

			        cellJ = row.getCell(9);
			        if (cellJ == null) cellJ = row.createCell(9);
			        originalStyle = cellJ.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_product_type() != null) 
			        cellJ.setCellValue(record1.getR11_product_type()); // String directly
			        else cellJ.setCellValue("");
			        cellJ.setCellStyle(originalStyle);
			        
					// ===== R11 / Col K =====
			        
			        cellK = row.getCell(10);
			        if (cellK == null) cellK = row.createCell(10);
			        originalStyle = cellK.getCellStyle();
			        if (record1.getR11_acct_num() != null) 
			        cellK.setCellValue(record1.getR11_acct_num().doubleValue());
			        else cellK.setCellValue("");
			        cellK.setCellStyle(originalStyle);
			        
					// ===== R11 / Col L =====

			        cellL = row.getCell(11);
			        if (cellL == null) cellL = row.createCell(11);
			        originalStyle = cellL.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_status_of_acct() != null) 
			        cellL.setCellValue(record1.getR11_status_of_acct()); // String directly
			        else cellL.setCellValue("");
			        cellL.setCellStyle(originalStyle);
			        
					// ===== R11 / Col M =====

			        cellM = row.getCell(12);
			        if (cellM == null) cellM = row.createCell(12);
			        originalStyle = cellM.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_acct_status_fit_or_not_fit_for_straight_throu_payout() != null) 
			        cellM.setCellValue(record1.getR11_acct_status_fit_or_not_fit_for_straight_throu_payout()); // String directly
			        else cellM.setCellValue("");
			        cellM.setCellStyle(originalStyle);
			        
					// ===== R11 / Col N =====

			        cellN = row.getCell(13);
			        if (cellN == null) cellN = row.createCell(13);
			        originalStyle = cellN.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_acct_branch() != null) 
			        cellN.setCellValue(record1.getR11_acct_branch()); // String directly
			        else cellN.setCellValue("");
			        cellN.setCellStyle(originalStyle);
			        

					// ===== R11 / Col O =====
			        
			        cellO = row.getCell(14);
			        if (cellO == null) cellO = row.createCell(14);
			        originalStyle = cellO.getCellStyle();
			        if (record1.getR11_acct_balance_pula() != null) 
			        cellO.setCellValue(record1.getR11_acct_balance_pula().doubleValue());
			        else cellO.setCellValue("");
			        cellO.setCellStyle(originalStyle);
			        
			        
					// ===== R11 / Col P =====

			        cellP = row.getCell(15);
			        if (cellP == null) cellP = row.createCell(15);
			        originalStyle = cellP.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_currency_of_acct() != null) 
			        cellP.setCellValue(record1.getR11_currency_of_acct()); // String directly
			        else cellP.setCellValue("");
			        cellP.setCellStyle(originalStyle);
			        
					// ===== R11 / Col Q =====
			        
			        cellQ = row.getCell(16);
			        if (cellQ == null) cellQ = row.createCell(16);
			        originalStyle = cellQ.getCellStyle();
			        if (record1.getR11_exchange_rate() != null) 
			        cellQ.setCellValue(record1.getR11_exchange_rate().doubleValue());
			        else cellQ.setCellValue("");
			        cellQ.setCellStyle(originalStyle);



					// ===== R12 / Col A =====
			        row = sheet.getRow(11);
			        cellA = row.getCell(0);
			        if (cellA == null) cellA = row.createCell(0);
			        originalStyle = cellA.getCellStyle();
			        if (record1.getR12_bank_spec_single_cust_rec_num() != null) 
			        cellA.setCellValue(record1.getR12_bank_spec_single_cust_rec_num().doubleValue());
			        else cellA.setCellValue("");
			        cellA.setCellStyle(originalStyle);


					// ===== R12 / Col B =====

			        cellB = row.getCell(1);
			        if (cellB == null) cellB = row.createCell(1);
			        originalStyle = cellB.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_company_name() != null) 
			        cellB.setCellValue(record1.getR12_company_name()); // String directly
			        else cellB.setCellValue("");
			        cellB.setCellStyle(originalStyle);
			        
					// ===== R12 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR12_company_reg_num() != null) 
			        cellC.setCellValue(record1.getR12_company_reg_num().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);	
			        
					// ===== R12 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_businees_phy_address() != null) 
			        cellD.setCellValue(record1.getR12_businees_phy_address()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R12 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_postal_address() != null) 
			        cellE.setCellValue(record1.getR12_postal_address()); // String directly
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);		
			        
					// ===== R12 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_country_of_reg() != null) 
			        cellF.setCellValue(record1.getR12_country_of_reg()); // String directly
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);			  
			        
					// ===== R12 / Col G =====

			        cellG = row.getCell(6);
			        if (cellG == null) cellG = row.createCell(6);
			        originalStyle = cellG.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_company_email() != null) 
			        cellG.setCellValue(record1.getR12_company_email()); // String directly
			        else cellG.setCellValue("");
			        cellG.setCellStyle(originalStyle);		
			        
					// ===== R12 / Col H =====

			        cellH = row.getCell(7);
			        if (cellH == null) cellH = row.createCell(7);
			        originalStyle = cellH.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_company_landline() != null) 
			        cellH.setCellValue(record1.getR12_company_landline()); // String directly
			        else cellH.setCellValue("");
			        cellH.setCellStyle(originalStyle);	
			        
					// ===== R12 / Col I =====

			        cellI = row.getCell(8);
			        if (cellI == null) cellI = row.createCell(8);
			        originalStyle = cellI.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_company_mob_phone_num() != null) 
			        cellI.setCellValue(record1.getR12_company_mob_phone_num()); // String directly
			        else cellI.setCellValue("");
			        cellI.setCellStyle(originalStyle);		
			        
					// ===== R12 / Col J =====

			        cellJ = row.getCell(9);
			        if (cellJ == null) cellJ = row.createCell(9);
			        originalStyle = cellJ.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_product_type() != null) 
			        cellJ.setCellValue(record1.getR12_product_type()); // String directly
			        else cellJ.setCellValue("");
			        cellJ.setCellStyle(originalStyle);
			        
					// ===== R12 / Col K =====
			        
			        cellK = row.getCell(10);
			        if (cellK == null) cellK = row.createCell(10);
			        originalStyle = cellK.getCellStyle();
			        if (record1.getR12_acct_num() != null) 
			        cellK.setCellValue(record1.getR12_acct_num().doubleValue());
			        else cellK.setCellValue("");
			        cellK.setCellStyle(originalStyle);
			        
					// ===== R12 / Col L =====

			        cellL = row.getCell(11);
			        if (cellL == null) cellL = row.createCell(11);
			        originalStyle = cellL.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_status_of_acct() != null) 
			        cellL.setCellValue(record1.getR12_status_of_acct()); // String directly
			        else cellL.setCellValue("");
			        cellL.setCellStyle(originalStyle);
			        
					// ===== R12 / Col M =====

			        cellM = row.getCell(12);
			        if (cellM == null) cellM = row.createCell(12);
			        originalStyle = cellM.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_acct_status_fit_or_not_fit_for_straight_throu_payout() != null) 
			        cellM.setCellValue(record1.getR12_acct_status_fit_or_not_fit_for_straight_throu_payout()); // String directly
			        else cellM.setCellValue("");
			        cellM.setCellStyle(originalStyle);
			        
					// ===== R12 / Col N =====

			        cellN = row.getCell(13);
			        if (cellN == null) cellN = row.createCell(13);
			        originalStyle = cellN.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_acct_branch() != null) 
			        cellN.setCellValue(record1.getR12_acct_branch()); // String directly
			        else cellN.setCellValue("");
			        cellN.setCellStyle(originalStyle);
			        

					// ===== R12 / Col O =====
			        
			        cellO = row.getCell(14);
			        if (cellO == null) cellO = row.createCell(14);
			        originalStyle = cellO.getCellStyle();
			        if (record1.getR12_acct_balance_pula() != null) 
			        cellO.setCellValue(record1.getR12_acct_balance_pula().doubleValue());
			        else cellO.setCellValue("");
			        cellO.setCellStyle(originalStyle);
			        
			        
					// ===== R12 / Col P =====

			        cellP = row.getCell(15);
			        if (cellP == null) cellP = row.createCell(15);
			        originalStyle = cellP.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_currency_of_acct() != null) 
			        cellP.setCellValue(record1.getR12_currency_of_acct()); // String directly
			        else cellP.setCellValue("");
			        cellP.setCellStyle(originalStyle);
			        
					// ===== R12 / Col Q =====
			        
			        cellQ = row.getCell(16);
			        if (cellQ == null) cellQ = row.createCell(16);
			        originalStyle = cellQ.getCellStyle();
			        if (record1.getR12_exchange_rate() != null) 
			        cellQ.setCellValue(record1.getR12_exchange_rate().doubleValue());
			        else cellQ.setCellValue("");
			        cellQ.setCellStyle(originalStyle);

		        
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

//	public List<Object> getBDISB2Archival() {
//		List<Object> BDISB2Archivallist = new ArrayList<>();
////		List<Object> BDISB2Archivallist2 = new ArrayList<>();
////		List<Object> BDISB2Archivallist3 = new ArrayList<>();
//		try {
//			BDISB2Archivallist = BRRS_BDISB2_Archival_Summary_Repo.getBDISB2archival();
//			
//			System.out.println("countser" + BDISB2Archivallist.size());
////			System.out.println("countser" + BDISB2Archivallist.size());
////			System.out.println("countser" + BDISB2Archivallist.size());
//		} catch (Exception e) {
//			// Log the exception
//			System.err.println("Error fetching BDISB2 Archival data: " + e.getMessage());
//			e.printStackTrace();
//
//			// Optionally, you can rethrow it or return empty list
//			// throw new RuntimeException("Failed to fetch data", e);
//		}
//		return BDISB2Archivallist;
//	}
//


	public byte[] getExcelBDISB2ARCHIVAL(String filename, String reportId, String fromdate,
			String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if ("ARCHIVAL".equals(type) && version != null) {
		}
			List<BDISB2_Archival_Summary_Entity> dataList1 = BRRS_BDISB2_Archival_Summary_Repo
					.getdatabydateListarchival(dateformat.parse(todate), version);
		

			
		
		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for BDISB2 report. Returning empty result.");
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
			        BDISB2_Archival_Summary_Entity record1 = dataList1.get(i);
			        System.out.println("rownumber=" + (startRow + i));
					
			        
				       
			        Row row;
			        Cell cellA, cellB, cellC, cellD, cellE, cellF, cellG,
			        cellH, cellI, cellJ, cellK, cellL, cellM, cellN, cellO,
			        cellP, cellQ;
			        CellStyle originalStyle;



					// ===== R6 / Col A =====
			        row = sheet.getRow(5);
			        cellA = row.getCell(0);
			        if (cellA == null) cellA = row.createCell(0);
			        originalStyle = cellA.getCellStyle();
			        if (record1.getR6_bank_spec_single_cust_rec_num() != null) 
			        cellA.setCellValue(record1.getR6_bank_spec_single_cust_rec_num().doubleValue());
			        else cellA.setCellValue("");
			        cellA.setCellStyle(originalStyle);


					// ===== R6 / Col B =====

			        cellB = row.getCell(1);
			        if (cellB == null) cellB = row.createCell(1);
			        originalStyle = cellB.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_company_name() != null) 
			        cellB.setCellValue(record1.getR6_company_name()); // String directly
			        else cellB.setCellValue("");
			        cellB.setCellStyle(originalStyle);
			        
					// ===== R6 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR6_company_reg_num() != null) 
			        cellC.setCellValue(record1.getR6_company_reg_num().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);	
			        
					// ===== R6 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_businees_phy_address() != null) 
			        cellD.setCellValue(record1.getR6_businees_phy_address()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R6 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_postal_address() != null) 
			        cellE.setCellValue(record1.getR6_postal_address()); // String directly
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);		
			        
					// ===== R6 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_country_of_reg() != null) 
			        cellF.setCellValue(record1.getR6_country_of_reg()); // String directly
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);			  
			        
					// ===== R6 / Col G =====

			        cellG = row.getCell(6);
			        if (cellG == null) cellG = row.createCell(6);
			        originalStyle = cellG.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_company_email() != null) 
			        cellG.setCellValue(record1.getR6_company_email()); // String directly
			        else cellG.setCellValue("");
			        cellG.setCellStyle(originalStyle);		
			        
					// ===== R6 / Col H =====

			        cellH = row.getCell(7);
			        if (cellH == null) cellH = row.createCell(7);
			        originalStyle = cellH.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_company_landline() != null) 
			        cellH.setCellValue(record1.getR6_company_landline()); // String directly
			        else cellH.setCellValue("");
			        cellH.setCellStyle(originalStyle);	
			        
					// ===== R6 / Col I =====

			        cellI = row.getCell(8);
			        if (cellI == null) cellI = row.createCell(8);
			        originalStyle = cellI.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_company_mob_phone_num() != null) 
			        cellI.setCellValue(record1.getR6_company_mob_phone_num()); // String directly
			        else cellI.setCellValue("");
			        cellI.setCellStyle(originalStyle);		
			        
					// ===== R6 / Col J =====

			        cellJ = row.getCell(9);
			        if (cellJ == null) cellJ = row.createCell(9);
			        originalStyle = cellJ.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_product_type() != null) 
			        cellJ.setCellValue(record1.getR6_product_type()); // String directly
			        else cellJ.setCellValue("");
			        cellJ.setCellStyle(originalStyle);
			        
					// ===== R6 / Col K =====
			        
			        cellK = row.getCell(10);
			        if (cellK == null) cellK = row.createCell(10);
			        originalStyle = cellK.getCellStyle();
			        if (record1.getR6_acct_num() != null) 
			        cellK.setCellValue(record1.getR6_acct_num().doubleValue());
			        else cellK.setCellValue("");
			        cellK.setCellStyle(originalStyle);
			        
					// ===== R6 / Col L =====

			        cellL = row.getCell(11);
			        if (cellL == null) cellL = row.createCell(11);
			        originalStyle = cellL.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_status_of_acct() != null) 
			        cellL.setCellValue(record1.getR6_status_of_acct()); // String directly
			        else cellL.setCellValue("");
			        cellL.setCellStyle(originalStyle);
			        
					// ===== R6 / Col M =====

			        cellM = row.getCell(12);
			        if (cellM == null) cellM = row.createCell(12);
			        originalStyle = cellM.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_acct_status_fit_or_not_fit_for_straight_throu_payout() != null) 
			        cellM.setCellValue(record1.getR6_acct_status_fit_or_not_fit_for_straight_throu_payout()); // String directly
			        else cellM.setCellValue("");
			        cellM.setCellStyle(originalStyle);
			        
					// ===== R6 / Col N =====

			        cellN = row.getCell(13);
			        if (cellN == null) cellN = row.createCell(13);
			        originalStyle = cellN.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_acct_branch() != null) 
			        cellN.setCellValue(record1.getR6_acct_branch()); // String directly
			        else cellN.setCellValue("");
			        cellN.setCellStyle(originalStyle);
			        

					// ===== R6 / Col O =====
			        
			        cellO = row.getCell(14);
			        if (cellO == null) cellO = row.createCell(14);
			        originalStyle = cellO.getCellStyle();
			        if (record1.getR6_acct_balance_pula() != null) 
			        cellO.setCellValue(record1.getR6_acct_balance_pula().doubleValue());
			        else cellO.setCellValue("");
			        cellO.setCellStyle(originalStyle);
			        
			        
					// ===== R6 / Col P =====

			        cellP = row.getCell(15);
			        if (cellP == null) cellP = row.createCell(15);
			        originalStyle = cellP.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_currency_of_acct() != null) 
			        cellP.setCellValue(record1.getR6_currency_of_acct()); // String directly
			        else cellP.setCellValue("");
			        cellP.setCellStyle(originalStyle);
			        
					// ===== R6 / Col Q =====
			        
			        cellQ = row.getCell(16);
			        if (cellQ == null) cellQ = row.createCell(16);
			        originalStyle = cellQ.getCellStyle();
			        if (record1.getR6_exchange_rate() != null) 
			        cellQ.setCellValue(record1.getR6_exchange_rate().doubleValue());
			        else cellQ.setCellValue("");
			        cellQ.setCellStyle(originalStyle);
			        
			        


					// ===== R7 / Col A =====
			        row = sheet.getRow(6);
			        cellA = row.getCell(0);
			        if (cellA == null) cellA = row.createCell(0);
			        originalStyle = cellA.getCellStyle();
			        if (record1.getR7_bank_spec_single_cust_rec_num() != null) 
			        cellA.setCellValue(record1.getR7_bank_spec_single_cust_rec_num().doubleValue());
			        else cellA.setCellValue("");
			        cellA.setCellStyle(originalStyle);


					// ===== R7 / Col B =====

			        cellB = row.getCell(1);
			        if (cellB == null) cellB = row.createCell(1);
			        originalStyle = cellB.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_company_name() != null) 
			        cellB.setCellValue(record1.getR7_company_name()); // String directly
			        else cellB.setCellValue("");
			        cellB.setCellStyle(originalStyle);
			        
					// ===== R7 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR7_company_reg_num() != null) 
			        cellC.setCellValue(record1.getR7_company_reg_num().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);	
			        
					// ===== R7 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_businees_phy_address() != null) 
			        cellD.setCellValue(record1.getR7_businees_phy_address()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R7 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_postal_address() != null) 
			        cellE.setCellValue(record1.getR7_postal_address()); // String directly
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);		
			        
					// ===== R7 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_country_of_reg() != null) 
			        cellF.setCellValue(record1.getR7_country_of_reg()); // String directly
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);			  
			        
					// ===== R7 / Col G =====

			        cellG = row.getCell(6);
			        if (cellG == null) cellG = row.createCell(6);
			        originalStyle = cellG.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_company_email() != null) 
			        cellG.setCellValue(record1.getR7_company_email()); // String directly
			        else cellG.setCellValue("");
			        cellG.setCellStyle(originalStyle);		
			        
					// ===== R7 / Col H =====

			        cellH = row.getCell(7);
			        if (cellH == null) cellH = row.createCell(7);
			        originalStyle = cellH.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_company_landline() != null) 
			        cellH.setCellValue(record1.getR7_company_landline()); // String directly
			        else cellH.setCellValue("");
			        cellH.setCellStyle(originalStyle);	
			        
					// ===== R7 / Col I =====

			        cellI = row.getCell(8);
			        if (cellI == null) cellI = row.createCell(8);
			        originalStyle = cellI.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_company_mob_phone_num() != null) 
			        cellI.setCellValue(record1.getR7_company_mob_phone_num()); // String directly
			        else cellI.setCellValue("");
			        cellI.setCellStyle(originalStyle);		
			        
					// ===== R7 / Col J =====

			        cellJ = row.getCell(9);
			        if (cellJ == null) cellJ = row.createCell(9);
			        originalStyle = cellJ.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_product_type() != null) 
			        cellJ.setCellValue(record1.getR7_product_type()); // String directly
			        else cellJ.setCellValue("");
			        cellJ.setCellStyle(originalStyle);
			        
					// ===== R7 / Col K =====
			        
			        cellK = row.getCell(10);
			        if (cellK == null) cellK = row.createCell(10);
			        originalStyle = cellK.getCellStyle();
			        if (record1.getR7_acct_num() != null) 
			        cellK.setCellValue(record1.getR7_acct_num().doubleValue());
			        else cellK.setCellValue("");
			        cellK.setCellStyle(originalStyle);
			        
					// ===== R7 / Col L =====

			        cellL = row.getCell(11);
			        if (cellL == null) cellL = row.createCell(11);
			        originalStyle = cellL.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_status_of_acct() != null) 
			        cellL.setCellValue(record1.getR7_status_of_acct()); // String directly
			        else cellL.setCellValue("");
			        cellL.setCellStyle(originalStyle);
			        
					// ===== R7 / Col M =====

			        cellM = row.getCell(12);
			        if (cellM == null) cellM = row.createCell(12);
			        originalStyle = cellM.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_acct_status_fit_or_not_fit_for_straight_throu_payout() != null) 
			        cellM.setCellValue(record1.getR7_acct_status_fit_or_not_fit_for_straight_throu_payout()); // String directly
			        else cellM.setCellValue("");
			        cellM.setCellStyle(originalStyle);
			        
					// ===== R7 / Col N =====

			        cellN = row.getCell(13);
			        if (cellN == null) cellN = row.createCell(13);
			        originalStyle = cellN.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_acct_branch() != null) 
			        cellN.setCellValue(record1.getR7_acct_branch()); // String directly
			        else cellN.setCellValue("");
			        cellN.setCellStyle(originalStyle);
			        

					// ===== R7 / Col O =====
			        
			        cellO = row.getCell(14);
			        if (cellO == null) cellO = row.createCell(14);
			        originalStyle = cellO.getCellStyle();
			        if (record1.getR7_acct_balance_pula() != null) 
			        cellO.setCellValue(record1.getR7_acct_balance_pula().doubleValue());
			        else cellO.setCellValue("");
			        cellO.setCellStyle(originalStyle);
			        
			        
					// ===== R7 / Col P =====

			        cellP = row.getCell(15);
			        if (cellP == null) cellP = row.createCell(15);
			        originalStyle = cellP.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_currency_of_acct() != null) 
			        cellP.setCellValue(record1.getR7_currency_of_acct()); // String directly
			        else cellP.setCellValue("");
			        cellP.setCellStyle(originalStyle);
			        
					// ===== R7 / Col Q =====
			        
			        cellQ = row.getCell(16);
			        if (cellQ == null) cellQ = row.createCell(16);
			        originalStyle = cellQ.getCellStyle();
			        if (record1.getR7_exchange_rate() != null) 
			        cellQ.setCellValue(record1.getR7_exchange_rate().doubleValue());
			        else cellQ.setCellValue("");
			        cellQ.setCellStyle(originalStyle);



					// ===== R8 / Col A =====
			        row = sheet.getRow(7);
			        cellA = row.getCell(0);
			        if (cellA == null) cellA = row.createCell(0);
			        originalStyle = cellA.getCellStyle();
			        if (record1.getR8_bank_spec_single_cust_rec_num() != null) 
			        cellA.setCellValue(record1.getR8_bank_spec_single_cust_rec_num().doubleValue());
			        else cellA.setCellValue("");
			        cellA.setCellStyle(originalStyle);


					// ===== R8 / Col B =====

			        cellB = row.getCell(1);
			        if (cellB == null) cellB = row.createCell(1);
			        originalStyle = cellB.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_company_name() != null) 
			        cellB.setCellValue(record1.getR8_company_name()); // String directly
			        else cellB.setCellValue("");
			        cellB.setCellStyle(originalStyle);
			        
					// ===== R8 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR8_company_reg_num() != null) 
			        cellC.setCellValue(record1.getR8_company_reg_num().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);	
			        
					// ===== R8 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_businees_phy_address() != null) 
			        cellD.setCellValue(record1.getR8_businees_phy_address()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R8 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_postal_address() != null) 
			        cellE.setCellValue(record1.getR8_postal_address()); // String directly
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);		
			        
					// ===== R8 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_country_of_reg() != null) 
			        cellF.setCellValue(record1.getR8_country_of_reg()); // String directly
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);			  
			        
					// ===== R8 / Col G =====

			        cellG = row.getCell(6);
			        if (cellG == null) cellG = row.createCell(6);
			        originalStyle = cellG.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_company_email() != null) 
			        cellG.setCellValue(record1.getR8_company_email()); // String directly
			        else cellG.setCellValue("");
			        cellG.setCellStyle(originalStyle);		
			        
					// ===== R8 / Col H =====

			        cellH = row.getCell(7);
			        if (cellH == null) cellH = row.createCell(7);
			        originalStyle = cellH.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_company_landline() != null) 
			        cellH.setCellValue(record1.getR8_company_landline()); // String directly
			        else cellH.setCellValue("");
			        cellH.setCellStyle(originalStyle);	
			        
					// ===== R8 / Col I =====

			        cellI = row.getCell(8);
			        if (cellI == null) cellI = row.createCell(8);
			        originalStyle = cellI.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_company_mob_phone_num() != null) 
			        cellI.setCellValue(record1.getR8_company_mob_phone_num()); // String directly
			        else cellI.setCellValue("");
			        cellI.setCellStyle(originalStyle);		
			        
					// ===== R8 / Col J =====

			        cellJ = row.getCell(9);
			        if (cellJ == null) cellJ = row.createCell(9);
			        originalStyle = cellJ.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_product_type() != null) 
			        cellJ.setCellValue(record1.getR8_product_type()); // String directly
			        else cellJ.setCellValue("");
			        cellJ.setCellStyle(originalStyle);
			        
					// ===== R8 / Col K =====
			        
			        cellK = row.getCell(10);
			        if (cellK == null) cellK = row.createCell(10);
			        originalStyle = cellK.getCellStyle();
			        if (record1.getR8_acct_num() != null) 
			        cellK.setCellValue(record1.getR8_acct_num().doubleValue());
			        else cellK.setCellValue("");
			        cellK.setCellStyle(originalStyle);
			        
					// ===== R8 / Col L =====

			        cellL = row.getCell(11);
			        if (cellL == null) cellL = row.createCell(11);
			        originalStyle = cellL.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_status_of_acct() != null) 
			        cellL.setCellValue(record1.getR8_status_of_acct()); // String directly
			        else cellL.setCellValue("");
			        cellL.setCellStyle(originalStyle);
			        
					// ===== R8 / Col M =====

			        cellM = row.getCell(12);
			        if (cellM == null) cellM = row.createCell(12);
			        originalStyle = cellM.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_acct_status_fit_or_not_fit_for_straight_throu_payout() != null) 
			        cellM.setCellValue(record1.getR8_acct_status_fit_or_not_fit_for_straight_throu_payout()); // String directly
			        else cellM.setCellValue("");
			        cellM.setCellStyle(originalStyle);
			        
					// ===== R8 / Col N =====

			        cellN = row.getCell(13);
			        if (cellN == null) cellN = row.createCell(13);
			        originalStyle = cellN.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_acct_branch() != null) 
			        cellN.setCellValue(record1.getR8_acct_branch()); // String directly
			        else cellN.setCellValue("");
			        cellN.setCellStyle(originalStyle);
			        

					// ===== R8 / Col O =====
			        
			        cellO = row.getCell(14);
			        if (cellO == null) cellO = row.createCell(14);
			        originalStyle = cellO.getCellStyle();
			        if (record1.getR8_acct_balance_pula() != null) 
			        cellO.setCellValue(record1.getR8_acct_balance_pula().doubleValue());
			        else cellO.setCellValue("");
			        cellO.setCellStyle(originalStyle);
			        
			        
					// ===== R8 / Col P =====

			        cellP = row.getCell(15);
			        if (cellP == null) cellP = row.createCell(15);
			        originalStyle = cellP.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_currency_of_acct() != null) 
			        cellP.setCellValue(record1.getR8_currency_of_acct()); // String directly
			        else cellP.setCellValue("");
			        cellP.setCellStyle(originalStyle);
			        
					// ===== R8 / Col Q =====
			        
			        cellQ = row.getCell(16);
			        if (cellQ == null) cellQ = row.createCell(16);
			        originalStyle = cellQ.getCellStyle();
			        if (record1.getR8_exchange_rate() != null) 
			        cellQ.setCellValue(record1.getR8_exchange_rate().doubleValue());
			        else cellQ.setCellValue("");
			        cellQ.setCellStyle(originalStyle);



					// ===== R9 / Col A =====
			        row = sheet.getRow(8);
			        cellA = row.getCell(0);
			        if (cellA == null) cellA = row.createCell(0);
			        originalStyle = cellA.getCellStyle();
			        if (record1.getR9_bank_spec_single_cust_rec_num() != null) 
			        cellA.setCellValue(record1.getR9_bank_spec_single_cust_rec_num().doubleValue());
			        else cellA.setCellValue("");
			        cellA.setCellStyle(originalStyle);


					// ===== R9 / Col B =====

			        cellB = row.getCell(1);
			        if (cellB == null) cellB = row.createCell(1);
			        originalStyle = cellB.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_company_name() != null) 
			        cellB.setCellValue(record1.getR9_company_name()); // String directly
			        else cellB.setCellValue("");
			        cellB.setCellStyle(originalStyle);
			        
					// ===== R9 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR9_company_reg_num() != null) 
			        cellC.setCellValue(record1.getR9_company_reg_num().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);	
			        
					// ===== R9 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_businees_phy_address() != null) 
			        cellD.setCellValue(record1.getR9_businees_phy_address()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R9 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_postal_address() != null) 
			        cellE.setCellValue(record1.getR9_postal_address()); // String directly
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);		
			        
					// ===== R9 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_country_of_reg() != null) 
			        cellF.setCellValue(record1.getR9_country_of_reg()); // String directly
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);			  
			        
					// ===== R9 / Col G =====

			        cellG = row.getCell(6);
			        if (cellG == null) cellG = row.createCell(6);
			        originalStyle = cellG.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_company_email() != null) 
			        cellG.setCellValue(record1.getR9_company_email()); // String directly
			        else cellG.setCellValue("");
			        cellG.setCellStyle(originalStyle);		
			        
					// ===== R9 / Col H =====

			        cellH = row.getCell(7);
			        if (cellH == null) cellH = row.createCell(7);
			        originalStyle = cellH.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_company_landline() != null) 
			        cellH.setCellValue(record1.getR9_company_landline()); // String directly
			        else cellH.setCellValue("");
			        cellH.setCellStyle(originalStyle);	
			        
					// ===== R9 / Col I =====

			        cellI = row.getCell(8);
			        if (cellI == null) cellI = row.createCell(8);
			        originalStyle = cellI.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_company_mob_phone_num() != null) 
			        cellI.setCellValue(record1.getR9_company_mob_phone_num()); // String directly
			        else cellI.setCellValue("");
			        cellI.setCellStyle(originalStyle);		
			        
					// ===== R9 / Col J =====

			        cellJ = row.getCell(9);
			        if (cellJ == null) cellJ = row.createCell(9);
			        originalStyle = cellJ.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_product_type() != null) 
			        cellJ.setCellValue(record1.getR9_product_type()); // String directly
			        else cellJ.setCellValue("");
			        cellJ.setCellStyle(originalStyle);
			        
					// ===== R9 / Col K =====
			        
			        cellK = row.getCell(10);
			        if (cellK == null) cellK = row.createCell(10);
			        originalStyle = cellK.getCellStyle();
			        if (record1.getR9_acct_num() != null) 
			        cellK.setCellValue(record1.getR9_acct_num().doubleValue());
			        else cellK.setCellValue("");
			        cellK.setCellStyle(originalStyle);
			        
					// ===== R9 / Col L =====

			        cellL = row.getCell(11);
			        if (cellL == null) cellL = row.createCell(11);
			        originalStyle = cellL.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_status_of_acct() != null) 
			        cellL.setCellValue(record1.getR9_status_of_acct()); // String directly
			        else cellL.setCellValue("");
			        cellL.setCellStyle(originalStyle);
			        
					// ===== R9 / Col M =====

			        cellM = row.getCell(12);
			        if (cellM == null) cellM = row.createCell(12);
			        originalStyle = cellM.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_acct_status_fit_or_not_fit_for_straight_throu_payout() != null) 
			        cellM.setCellValue(record1.getR9_acct_status_fit_or_not_fit_for_straight_throu_payout()); // String directly
			        else cellM.setCellValue("");
			        cellM.setCellStyle(originalStyle);
			        
					// ===== R9 / Col N =====

			        cellN = row.getCell(13);
			        if (cellN == null) cellN = row.createCell(13);
			        originalStyle = cellN.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_acct_branch() != null) 
			        cellN.setCellValue(record1.getR9_acct_branch()); // String directly
			        else cellN.setCellValue("");
			        cellN.setCellStyle(originalStyle);
			        

					// ===== R9 / Col O =====
			        
			        cellO = row.getCell(14);
			        if (cellO == null) cellO = row.createCell(14);
			        originalStyle = cellO.getCellStyle();
			        if (record1.getR9_acct_balance_pula() != null) 
			        cellO.setCellValue(record1.getR9_acct_balance_pula().doubleValue());
			        else cellO.setCellValue("");
			        cellO.setCellStyle(originalStyle);
			        
			        
					// ===== R9 / Col P =====

			        cellP = row.getCell(15);
			        if (cellP == null) cellP = row.createCell(15);
			        originalStyle = cellP.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_currency_of_acct() != null) 
			        cellP.setCellValue(record1.getR9_currency_of_acct()); // String directly
			        else cellP.setCellValue("");
			        cellP.setCellStyle(originalStyle);
			        
					// ===== R9 / Col Q =====
			        
			        cellQ = row.getCell(16);
			        if (cellQ == null) cellQ = row.createCell(16);
			        originalStyle = cellQ.getCellStyle();
			        if (record1.getR9_exchange_rate() != null) 
			        cellQ.setCellValue(record1.getR9_exchange_rate().doubleValue());
			        else cellQ.setCellValue("");
			        cellQ.setCellStyle(originalStyle);
			        


					// ===== R10 / Col A =====
			        row = sheet.getRow(9);
			        cellA = row.getCell(0);
			        if (cellA == null) cellA = row.createCell(0);
			        originalStyle = cellA.getCellStyle();
			        if (record1.getR10_bank_spec_single_cust_rec_num() != null) 
			        cellA.setCellValue(record1.getR10_bank_spec_single_cust_rec_num().doubleValue());
			        else cellA.setCellValue("");
			        cellA.setCellStyle(originalStyle);


					// ===== R10 / Col B =====

			        cellB = row.getCell(1);
			        if (cellB == null) cellB = row.createCell(1);
			        originalStyle = cellB.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_company_name() != null) 
			        cellB.setCellValue(record1.getR10_company_name()); // String directly
			        else cellB.setCellValue("");
			        cellB.setCellStyle(originalStyle);
			        
					// ===== R10 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR10_company_reg_num() != null) 
			        cellC.setCellValue(record1.getR10_company_reg_num().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);	
			        
					// ===== R10 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_businees_phy_address() != null) 
			        cellD.setCellValue(record1.getR10_businees_phy_address()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R10 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_postal_address() != null) 
			        cellE.setCellValue(record1.getR10_postal_address()); // String directly
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);		
			        
					// ===== R10 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_country_of_reg() != null) 
			        cellF.setCellValue(record1.getR10_country_of_reg()); // String directly
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);			  
			        
					// ===== R10 / Col G =====

			        cellG = row.getCell(6);
			        if (cellG == null) cellG = row.createCell(6);
			        originalStyle = cellG.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_company_email() != null) 
			        cellG.setCellValue(record1.getR10_company_email()); // String directly
			        else cellG.setCellValue("");
			        cellG.setCellStyle(originalStyle);		
			        
					// ===== R10 / Col H =====

			        cellH = row.getCell(7);
			        if (cellH == null) cellH = row.createCell(7);
			        originalStyle = cellH.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_company_landline() != null) 
			        cellH.setCellValue(record1.getR10_company_landline()); // String directly
			        else cellH.setCellValue("");
			        cellH.setCellStyle(originalStyle);	
			        
					// ===== R10 / Col I =====

			        cellI = row.getCell(8);
			        if (cellI == null) cellI = row.createCell(8);
			        originalStyle = cellI.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_company_mob_phone_num() != null) 
			        cellI.setCellValue(record1.getR10_company_mob_phone_num()); // String directly
			        else cellI.setCellValue("");
			        cellI.setCellStyle(originalStyle);		
			        
					// ===== R10 / Col J =====

			        cellJ = row.getCell(9);
			        if (cellJ == null) cellJ = row.createCell(9);
			        originalStyle = cellJ.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_product_type() != null) 
			        cellJ.setCellValue(record1.getR10_product_type()); // String directly
			        else cellJ.setCellValue("");
			        cellJ.setCellStyle(originalStyle);
			        
					// ===== R10 / Col K =====
			        
			        cellK = row.getCell(10);
			        if (cellK == null) cellK = row.createCell(10);
			        originalStyle = cellK.getCellStyle();
			        if (record1.getR10_acct_num() != null) 
			        cellK.setCellValue(record1.getR10_acct_num().doubleValue());
			        else cellK.setCellValue("");
			        cellK.setCellStyle(originalStyle);
			        
					// ===== R10 / Col L =====

			        cellL = row.getCell(11);
			        if (cellL == null) cellL = row.createCell(11);
			        originalStyle = cellL.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_status_of_acct() != null) 
			        cellL.setCellValue(record1.getR10_status_of_acct()); // String directly
			        else cellL.setCellValue("");
			        cellL.setCellStyle(originalStyle);
			        
					// ===== R10 / Col M =====

			        cellM = row.getCell(12);
			        if (cellM == null) cellM = row.createCell(12);
			        originalStyle = cellM.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_acct_status_fit_or_not_fit_for_straight_throu_payout() != null) 
			        cellM.setCellValue(record1.getR10_acct_status_fit_or_not_fit_for_straight_throu_payout()); // String directly
			        else cellM.setCellValue("");
			        cellM.setCellStyle(originalStyle);
			        
					// ===== R10 / Col N =====

			        cellN = row.getCell(13);
			        if (cellN == null) cellN = row.createCell(13);
			        originalStyle = cellN.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_acct_branch() != null) 
			        cellN.setCellValue(record1.getR10_acct_branch()); // String directly
			        else cellN.setCellValue("");
			        cellN.setCellStyle(originalStyle);
			        

					// ===== R10 / Col O =====
			        
			        cellO = row.getCell(14);
			        if (cellO == null) cellO = row.createCell(14);
			        originalStyle = cellO.getCellStyle();
			        if (record1.getR10_acct_balance_pula() != null) 
			        cellO.setCellValue(record1.getR10_acct_balance_pula().doubleValue());
			        else cellO.setCellValue("");
			        cellO.setCellStyle(originalStyle);
			        
			        
					// ===== R10 / Col P =====

			        cellP = row.getCell(15);
			        if (cellP == null) cellP = row.createCell(15);
			        originalStyle = cellP.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_currency_of_acct() != null) 
			        cellP.setCellValue(record1.getR10_currency_of_acct()); // String directly
			        else cellP.setCellValue("");
			        cellP.setCellStyle(originalStyle);
			        
					// ===== R10 / Col Q =====
			        
			        cellQ = row.getCell(16);
			        if (cellQ == null) cellQ = row.createCell(16);
			        originalStyle = cellQ.getCellStyle();
			        if (record1.getR10_exchange_rate() != null) 
			        cellQ.setCellValue(record1.getR10_exchange_rate().doubleValue());
			        else cellQ.setCellValue("");
			        cellQ.setCellStyle(originalStyle);


					// ===== R11 / Col A =====
			        row = sheet.getRow(10);
			        cellA = row.getCell(0);
			        if (cellA == null) cellA = row.createCell(0);
			        originalStyle = cellA.getCellStyle();
			        if (record1.getR11_bank_spec_single_cust_rec_num() != null) 
			        cellA.setCellValue(record1.getR11_bank_spec_single_cust_rec_num().doubleValue());
			        else cellA.setCellValue("");
			        cellA.setCellStyle(originalStyle);


					// ===== R11 / Col B =====

			        cellB = row.getCell(1);
			        if (cellB == null) cellB = row.createCell(1);
			        originalStyle = cellB.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_company_name() != null) 
			        cellB.setCellValue(record1.getR11_company_name()); // String directly
			        else cellB.setCellValue("");
			        cellB.setCellStyle(originalStyle);
			        
					// ===== R11 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR11_company_reg_num() != null) 
			        cellC.setCellValue(record1.getR11_company_reg_num().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);	
			        
					// ===== R11 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_businees_phy_address() != null) 
			        cellD.setCellValue(record1.getR11_businees_phy_address()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R11 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_postal_address() != null) 
			        cellE.setCellValue(record1.getR11_postal_address()); // String directly
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);		
			        
					// ===== R11 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_country_of_reg() != null) 
			        cellF.setCellValue(record1.getR11_country_of_reg()); // String directly
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);			  
			        
					// ===== R11 / Col G =====

			        cellG = row.getCell(6);
			        if (cellG == null) cellG = row.createCell(6);
			        originalStyle = cellG.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_company_email() != null) 
			        cellG.setCellValue(record1.getR11_company_email()); // String directly
			        else cellG.setCellValue("");
			        cellG.setCellStyle(originalStyle);		
			        
					// ===== R11 / Col H =====

			        cellH = row.getCell(7);
			        if (cellH == null) cellH = row.createCell(7);
			        originalStyle = cellH.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_company_landline() != null) 
			        cellH.setCellValue(record1.getR11_company_landline()); // String directly
			        else cellH.setCellValue("");
			        cellH.setCellStyle(originalStyle);	
			        
					// ===== R11 / Col I =====

			        cellI = row.getCell(8);
			        if (cellI == null) cellI = row.createCell(8);
			        originalStyle = cellI.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_company_mob_phone_num() != null) 
			        cellI.setCellValue(record1.getR11_company_mob_phone_num()); // String directly
			        else cellI.setCellValue("");
			        cellI.setCellStyle(originalStyle);		
			        
					// ===== R11 / Col J =====

			        cellJ = row.getCell(9);
			        if (cellJ == null) cellJ = row.createCell(9);
			        originalStyle = cellJ.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_product_type() != null) 
			        cellJ.setCellValue(record1.getR11_product_type()); // String directly
			        else cellJ.setCellValue("");
			        cellJ.setCellStyle(originalStyle);
			        
					// ===== R11 / Col K =====
			        
			        cellK = row.getCell(10);
			        if (cellK == null) cellK = row.createCell(10);
			        originalStyle = cellK.getCellStyle();
			        if (record1.getR11_acct_num() != null) 
			        cellK.setCellValue(record1.getR11_acct_num().doubleValue());
			        else cellK.setCellValue("");
			        cellK.setCellStyle(originalStyle);
			        
					// ===== R11 / Col L =====

			        cellL = row.getCell(11);
			        if (cellL == null) cellL = row.createCell(11);
			        originalStyle = cellL.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_status_of_acct() != null) 
			        cellL.setCellValue(record1.getR11_status_of_acct()); // String directly
			        else cellL.setCellValue("");
			        cellL.setCellStyle(originalStyle);
			        
					// ===== R11 / Col M =====

			        cellM = row.getCell(12);
			        if (cellM == null) cellM = row.createCell(12);
			        originalStyle = cellM.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_acct_status_fit_or_not_fit_for_straight_throu_payout() != null) 
			        cellM.setCellValue(record1.getR11_acct_status_fit_or_not_fit_for_straight_throu_payout()); // String directly
			        else cellM.setCellValue("");
			        cellM.setCellStyle(originalStyle);
			        
					// ===== R11 / Col N =====

			        cellN = row.getCell(13);
			        if (cellN == null) cellN = row.createCell(13);
			        originalStyle = cellN.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_acct_branch() != null) 
			        cellN.setCellValue(record1.getR11_acct_branch()); // String directly
			        else cellN.setCellValue("");
			        cellN.setCellStyle(originalStyle);
			        

					// ===== R11 / Col O =====
			        
			        cellO = row.getCell(14);
			        if (cellO == null) cellO = row.createCell(14);
			        originalStyle = cellO.getCellStyle();
			        if (record1.getR11_acct_balance_pula() != null) 
			        cellO.setCellValue(record1.getR11_acct_balance_pula().doubleValue());
			        else cellO.setCellValue("");
			        cellO.setCellStyle(originalStyle);
			        
			        
					// ===== R11 / Col P =====

			        cellP = row.getCell(15);
			        if (cellP == null) cellP = row.createCell(15);
			        originalStyle = cellP.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_currency_of_acct() != null) 
			        cellP.setCellValue(record1.getR11_currency_of_acct()); // String directly
			        else cellP.setCellValue("");
			        cellP.setCellStyle(originalStyle);
			        
					// ===== R11 / Col Q =====
			        
			        cellQ = row.getCell(16);
			        if (cellQ == null) cellQ = row.createCell(16);
			        originalStyle = cellQ.getCellStyle();
			        if (record1.getR11_exchange_rate() != null) 
			        cellQ.setCellValue(record1.getR11_exchange_rate().doubleValue());
			        else cellQ.setCellValue("");
			        cellQ.setCellStyle(originalStyle);



					// ===== R12 / Col A =====
			        row = sheet.getRow(11);
			        cellA = row.getCell(0);
			        if (cellA == null) cellA = row.createCell(0);
			        originalStyle = cellA.getCellStyle();
			        if (record1.getR12_bank_spec_single_cust_rec_num() != null) 
			        cellA.setCellValue(record1.getR12_bank_spec_single_cust_rec_num().doubleValue());
			        else cellA.setCellValue("");
			        cellA.setCellStyle(originalStyle);


					// ===== R12 / Col B =====

			        cellB = row.getCell(1);
			        if (cellB == null) cellB = row.createCell(1);
			        originalStyle = cellB.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_company_name() != null) 
			        cellB.setCellValue(record1.getR12_company_name()); // String directly
			        else cellB.setCellValue("");
			        cellB.setCellStyle(originalStyle);
			        
					// ===== R12 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR12_company_reg_num() != null) 
			        cellC.setCellValue(record1.getR12_company_reg_num().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);	
			        
					// ===== R12 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_businees_phy_address() != null) 
			        cellD.setCellValue(record1.getR12_businees_phy_address()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R12 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_postal_address() != null) 
			        cellE.setCellValue(record1.getR12_postal_address()); // String directly
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);		
			        
					// ===== R12 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_country_of_reg() != null) 
			        cellF.setCellValue(record1.getR12_country_of_reg()); // String directly
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);			  
			        
					// ===== R12 / Col G =====

			        cellG = row.getCell(6);
			        if (cellG == null) cellG = row.createCell(6);
			        originalStyle = cellG.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_company_email() != null) 
			        cellG.setCellValue(record1.getR12_company_email()); // String directly
			        else cellG.setCellValue("");
			        cellG.setCellStyle(originalStyle);		
			        
					// ===== R12 / Col H =====

			        cellH = row.getCell(7);
			        if (cellH == null) cellH = row.createCell(7);
			        originalStyle = cellH.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_company_landline() != null) 
			        cellH.setCellValue(record1.getR12_company_landline()); // String directly
			        else cellH.setCellValue("");
			        cellH.setCellStyle(originalStyle);	
			        
					// ===== R12 / Col I =====

			        cellI = row.getCell(8);
			        if (cellI == null) cellI = row.createCell(8);
			        originalStyle = cellI.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_company_mob_phone_num() != null) 
			        cellI.setCellValue(record1.getR12_company_mob_phone_num()); // String directly
			        else cellI.setCellValue("");
			        cellI.setCellStyle(originalStyle);		
			        
					// ===== R12 / Col J =====

			        cellJ = row.getCell(9);
			        if (cellJ == null) cellJ = row.createCell(9);
			        originalStyle = cellJ.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_product_type() != null) 
			        cellJ.setCellValue(record1.getR12_product_type()); // String directly
			        else cellJ.setCellValue("");
			        cellJ.setCellStyle(originalStyle);
			        
					// ===== R12 / Col K =====
			        
			        cellK = row.getCell(10);
			        if (cellK == null) cellK = row.createCell(10);
			        originalStyle = cellK.getCellStyle();
			        if (record1.getR12_acct_num() != null) 
			        cellK.setCellValue(record1.getR12_acct_num().doubleValue());
			        else cellK.setCellValue("");
			        cellK.setCellStyle(originalStyle);
			        
					// ===== R12 / Col L =====

			        cellL = row.getCell(11);
			        if (cellL == null) cellL = row.createCell(11);
			        originalStyle = cellL.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_status_of_acct() != null) 
			        cellL.setCellValue(record1.getR12_status_of_acct()); // String directly
			        else cellL.setCellValue("");
			        cellL.setCellStyle(originalStyle);
			        
					// ===== R12 / Col M =====

			        cellM = row.getCell(12);
			        if (cellM == null) cellM = row.createCell(12);
			        originalStyle = cellM.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_acct_status_fit_or_not_fit_for_straight_throu_payout() != null) 
			        cellM.setCellValue(record1.getR12_acct_status_fit_or_not_fit_for_straight_throu_payout()); // String directly
			        else cellM.setCellValue("");
			        cellM.setCellStyle(originalStyle);
			        
					// ===== R12 / Col N =====

			        cellN = row.getCell(13);
			        if (cellN == null) cellN = row.createCell(13);
			        originalStyle = cellN.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_acct_branch() != null) 
			        cellN.setCellValue(record1.getR12_acct_branch()); // String directly
			        else cellN.setCellValue("");
			        cellN.setCellStyle(originalStyle);
			        

					// ===== R12 / Col O =====
			        
			        cellO = row.getCell(14);
			        if (cellO == null) cellO = row.createCell(14);
			        originalStyle = cellO.getCellStyle();
			        if (record1.getR12_acct_balance_pula() != null) 
			        cellO.setCellValue(record1.getR12_acct_balance_pula().doubleValue());
			        else cellO.setCellValue("");
			        cellO.setCellStyle(originalStyle);
			        
			        
					// ===== R12 / Col P =====

			        cellP = row.getCell(15);
			        if (cellP == null) cellP = row.createCell(15);
			        originalStyle = cellP.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_currency_of_acct() != null) 
			        cellP.setCellValue(record1.getR12_currency_of_acct()); // String directly
			        else cellP.setCellValue("");
			        cellP.setCellStyle(originalStyle);
			        
					// ===== R12 / Col Q =====
			        
			        cellQ = row.getCell(16);
			        if (cellQ == null) cellQ = row.createCell(16);
			        originalStyle = cellQ.getCellStyle();
			        if (record1.getR12_exchange_rate() != null) 
			        cellQ.setCellValue(record1.getR12_exchange_rate().doubleValue());
			        else cellQ.setCellValue("");
			        cellQ.setCellStyle(originalStyle);

		        		        
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
public List<Object[]> getBDISB2Resub() {
List<Object[]> resubList = new ArrayList<>();
try {
List<BDISB2_Archival_Summary_Entity> latestArchivalList = 
BRRS_BDISB2_Archival_Summary_Repo.getdatabydateListWithVersionAll();

if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
for (BDISB2_Archival_Summary_Entity entity : latestArchivalList) {
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
System.err.println("Error fetching BDISB2 Resub data: " + e.getMessage());
e.printStackTrace();
}
return resubList;
}
	
	
	//Archival View
	public List<Object[]> getBDISB2Archival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<BDISB2_Archival_Summary_Entity> repoData = BRRS_BDISB2_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (repoData != null && !repoData.isEmpty()) {
				for (BDISB2_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] {
							entity.getReportDate(), 
							entity.getReportVersion() 
					};
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				BDISB2_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching BDISB2 Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	
	// Resubmit the values , latest version and Resub Date
	public void updateReportReSub(BDISB2_Summary_Entity updatedEntity) {
		System.out.println("Came to Resub Service");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		Date reportDate = updatedEntity.getReportDate();
		int newVersion = 1;

		try {
			// Fetch the latest archival version for this report date
			Optional<BDISB2_Archival_Summary_Entity> latestArchivalOpt = BRRS_BDISB2_Archival_Summary_Repo
					.getLatestArchivalVersionByDate(reportDate);

			// Determine next version number
			if (latestArchivalOpt.isPresent()) {
				BDISB2_Archival_Summary_Entity latestArchival = latestArchivalOpt.get();
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
			boolean exists = BRRS_BDISB2_Archival_Summary_Repo
					.findByReportDateAndReportVersion(reportDate, String.valueOf(newVersion))
					.isPresent();

			if (exists) {
				throw new RuntimeException("Version " + newVersion + " already exists for report date " + reportDate);
			}

			// Copy summary entity to archival entity
			BDISB2_Archival_Summary_Entity archivalEntity = new BDISB2_Archival_Summary_Entity();
			org.springframework.beans.BeanUtils.copyProperties(updatedEntity, archivalEntity);

			archivalEntity.setReportDate(reportDate);
			archivalEntity.setReportVersion(String.valueOf(newVersion));
			archivalEntity.setReportResubDate(new Date());

			System.out.println("Saving new archival version: " + newVersion);

			// Save new version to repository
			BRRS_BDISB2_Archival_Summary_Repo.save(archivalEntity);

			System.out.println(" Saved archival version successfully: " + newVersion);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error while creating archival resubmission record", e);
		}
	}

	/// Downloaded for Archival & Resub
	public byte[] BRRS_BDISB2ResubExcel(String filename, String reportId, String fromdate,
        String todate, String currency, String dtltype,
        String type, String version) throws Exception {

    logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

    if (type.equals("RESUB") & version != null) {
       
    }

    List<BDISB2_Archival_Summary_Entity> dataList1 =
        BRRS_BDISB2_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

    if (dataList1.isEmpty()) {
        logger.warn("Service: No data found for BDISB2 report. Returning empty result.");
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
			        BDISB2_Archival_Summary_Entity record1 = dataList1.get(i);
			        System.out.println("rownumber=" + (startRow + i));
					
			        
				       
			        Row row;
			        Cell cellA, cellB, cellC, cellD, cellE, cellF, cellG,
			        cellH, cellI, cellJ, cellK, cellL, cellM, cellN, cellO,
			        cellP, cellQ;
			        CellStyle originalStyle;



					// ===== R6 / Col A =====
			        row = sheet.getRow(5);
			        cellA = row.getCell(0);
			        if (cellA == null) cellA = row.createCell(0);
			        originalStyle = cellA.getCellStyle();
			        if (record1.getR6_bank_spec_single_cust_rec_num() != null) 
			        cellA.setCellValue(record1.getR6_bank_spec_single_cust_rec_num().doubleValue());
			        else cellA.setCellValue("");
			        cellA.setCellStyle(originalStyle);


					// ===== R6 / Col B =====

			        cellB = row.getCell(1);
			        if (cellB == null) cellB = row.createCell(1);
			        originalStyle = cellB.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_company_name() != null) 
			        cellB.setCellValue(record1.getR6_company_name()); // String directly
			        else cellB.setCellValue("");
			        cellB.setCellStyle(originalStyle);
			        
					// ===== R6 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR6_company_reg_num() != null) 
			        cellC.setCellValue(record1.getR6_company_reg_num().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);	
			        
					// ===== R6 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_businees_phy_address() != null) 
			        cellD.setCellValue(record1.getR6_businees_phy_address()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R6 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_postal_address() != null) 
			        cellE.setCellValue(record1.getR6_postal_address()); // String directly
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);		
			        
					// ===== R6 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_country_of_reg() != null) 
			        cellF.setCellValue(record1.getR6_country_of_reg()); // String directly
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);			  
			        
					// ===== R6 / Col G =====

			        cellG = row.getCell(6);
			        if (cellG == null) cellG = row.createCell(6);
			        originalStyle = cellG.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_company_email() != null) 
			        cellG.setCellValue(record1.getR6_company_email()); // String directly
			        else cellG.setCellValue("");
			        cellG.setCellStyle(originalStyle);		
			        
					// ===== R6 / Col H =====

			        cellH = row.getCell(7);
			        if (cellH == null) cellH = row.createCell(7);
			        originalStyle = cellH.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_company_landline() != null) 
			        cellH.setCellValue(record1.getR6_company_landline()); // String directly
			        else cellH.setCellValue("");
			        cellH.setCellStyle(originalStyle);	
			        
					// ===== R6 / Col I =====

			        cellI = row.getCell(8);
			        if (cellI == null) cellI = row.createCell(8);
			        originalStyle = cellI.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_company_mob_phone_num() != null) 
			        cellI.setCellValue(record1.getR6_company_mob_phone_num()); // String directly
			        else cellI.setCellValue("");
			        cellI.setCellStyle(originalStyle);		
			        
					// ===== R6 / Col J =====

			        cellJ = row.getCell(9);
			        if (cellJ == null) cellJ = row.createCell(9);
			        originalStyle = cellJ.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_product_type() != null) 
			        cellJ.setCellValue(record1.getR6_product_type()); // String directly
			        else cellJ.setCellValue("");
			        cellJ.setCellStyle(originalStyle);
			        
					// ===== R6 / Col K =====
			        
			        cellK = row.getCell(10);
			        if (cellK == null) cellK = row.createCell(10);
			        originalStyle = cellK.getCellStyle();
			        if (record1.getR6_acct_num() != null) 
			        cellK.setCellValue(record1.getR6_acct_num().doubleValue());
			        else cellK.setCellValue("");
			        cellK.setCellStyle(originalStyle);
			        
					// ===== R6 / Col L =====

			        cellL = row.getCell(11);
			        if (cellL == null) cellL = row.createCell(11);
			        originalStyle = cellL.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_status_of_acct() != null) 
			        cellL.setCellValue(record1.getR6_status_of_acct()); // String directly
			        else cellL.setCellValue("");
			        cellL.setCellStyle(originalStyle);
			        
					// ===== R6 / Col M =====

			        cellM = row.getCell(12);
			        if (cellM == null) cellM = row.createCell(12);
			        originalStyle = cellM.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_acct_status_fit_or_not_fit_for_straight_throu_payout() != null) 
			        cellM.setCellValue(record1.getR6_acct_status_fit_or_not_fit_for_straight_throu_payout()); // String directly
			        else cellM.setCellValue("");
			        cellM.setCellStyle(originalStyle);
			        
					// ===== R6 / Col N =====

			        cellN = row.getCell(13);
			        if (cellN == null) cellN = row.createCell(13);
			        originalStyle = cellN.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_acct_branch() != null) 
			        cellN.setCellValue(record1.getR6_acct_branch()); // String directly
			        else cellN.setCellValue("");
			        cellN.setCellStyle(originalStyle);
			        

					// ===== R6 / Col O =====
			        
			        cellO = row.getCell(14);
			        if (cellO == null) cellO = row.createCell(14);
			        originalStyle = cellO.getCellStyle();
			        if (record1.getR6_acct_balance_pula() != null) 
			        cellO.setCellValue(record1.getR6_acct_balance_pula().doubleValue());
			        else cellO.setCellValue("");
			        cellO.setCellStyle(originalStyle);
			        
			        
					// ===== R6 / Col P =====

			        cellP = row.getCell(15);
			        if (cellP == null) cellP = row.createCell(15);
			        originalStyle = cellP.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR6_currency_of_acct() != null) 
			        cellP.setCellValue(record1.getR6_currency_of_acct()); // String directly
			        else cellP.setCellValue("");
			        cellP.setCellStyle(originalStyle);
			        
					// ===== R6 / Col Q =====
			        
			        cellQ = row.getCell(16);
			        if (cellQ == null) cellQ = row.createCell(16);
			        originalStyle = cellQ.getCellStyle();
			        if (record1.getR6_exchange_rate() != null) 
			        cellQ.setCellValue(record1.getR6_exchange_rate().doubleValue());
			        else cellQ.setCellValue("");
			        cellQ.setCellStyle(originalStyle);
			        
			        


					// ===== R7 / Col A =====
			        row = sheet.getRow(6);
			        cellA = row.getCell(0);
			        if (cellA == null) cellA = row.createCell(0);
			        originalStyle = cellA.getCellStyle();
			        if (record1.getR7_bank_spec_single_cust_rec_num() != null) 
			        cellA.setCellValue(record1.getR7_bank_spec_single_cust_rec_num().doubleValue());
			        else cellA.setCellValue("");
			        cellA.setCellStyle(originalStyle);


					// ===== R7 / Col B =====

			        cellB = row.getCell(1);
			        if (cellB == null) cellB = row.createCell(1);
			        originalStyle = cellB.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_company_name() != null) 
			        cellB.setCellValue(record1.getR7_company_name()); // String directly
			        else cellB.setCellValue("");
			        cellB.setCellStyle(originalStyle);
			        
					// ===== R7 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR7_company_reg_num() != null) 
			        cellC.setCellValue(record1.getR7_company_reg_num().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);	
			        
					// ===== R7 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_businees_phy_address() != null) 
			        cellD.setCellValue(record1.getR7_businees_phy_address()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R7 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_postal_address() != null) 
			        cellE.setCellValue(record1.getR7_postal_address()); // String directly
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);		
			        
					// ===== R7 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_country_of_reg() != null) 
			        cellF.setCellValue(record1.getR7_country_of_reg()); // String directly
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);			  
			        
					// ===== R7 / Col G =====

			        cellG = row.getCell(6);
			        if (cellG == null) cellG = row.createCell(6);
			        originalStyle = cellG.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_company_email() != null) 
			        cellG.setCellValue(record1.getR7_company_email()); // String directly
			        else cellG.setCellValue("");
			        cellG.setCellStyle(originalStyle);		
			        
					// ===== R7 / Col H =====

			        cellH = row.getCell(7);
			        if (cellH == null) cellH = row.createCell(7);
			        originalStyle = cellH.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_company_landline() != null) 
			        cellH.setCellValue(record1.getR7_company_landline()); // String directly
			        else cellH.setCellValue("");
			        cellH.setCellStyle(originalStyle);	
			        
					// ===== R7 / Col I =====

			        cellI = row.getCell(8);
			        if (cellI == null) cellI = row.createCell(8);
			        originalStyle = cellI.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_company_mob_phone_num() != null) 
			        cellI.setCellValue(record1.getR7_company_mob_phone_num()); // String directly
			        else cellI.setCellValue("");
			        cellI.setCellStyle(originalStyle);		
			        
					// ===== R7 / Col J =====

			        cellJ = row.getCell(9);
			        if (cellJ == null) cellJ = row.createCell(9);
			        originalStyle = cellJ.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_product_type() != null) 
			        cellJ.setCellValue(record1.getR7_product_type()); // String directly
			        else cellJ.setCellValue("");
			        cellJ.setCellStyle(originalStyle);
			        
					// ===== R7 / Col K =====
			        
			        cellK = row.getCell(10);
			        if (cellK == null) cellK = row.createCell(10);
			        originalStyle = cellK.getCellStyle();
			        if (record1.getR7_acct_num() != null) 
			        cellK.setCellValue(record1.getR7_acct_num().doubleValue());
			        else cellK.setCellValue("");
			        cellK.setCellStyle(originalStyle);
			        
					// ===== R7 / Col L =====

			        cellL = row.getCell(11);
			        if (cellL == null) cellL = row.createCell(11);
			        originalStyle = cellL.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_status_of_acct() != null) 
			        cellL.setCellValue(record1.getR7_status_of_acct()); // String directly
			        else cellL.setCellValue("");
			        cellL.setCellStyle(originalStyle);
			        
					// ===== R7 / Col M =====

			        cellM = row.getCell(12);
			        if (cellM == null) cellM = row.createCell(12);
			        originalStyle = cellM.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_acct_status_fit_or_not_fit_for_straight_throu_payout() != null) 
			        cellM.setCellValue(record1.getR7_acct_status_fit_or_not_fit_for_straight_throu_payout()); // String directly
			        else cellM.setCellValue("");
			        cellM.setCellStyle(originalStyle);
			        
					// ===== R7 / Col N =====

			        cellN = row.getCell(13);
			        if (cellN == null) cellN = row.createCell(13);
			        originalStyle = cellN.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_acct_branch() != null) 
			        cellN.setCellValue(record1.getR7_acct_branch()); // String directly
			        else cellN.setCellValue("");
			        cellN.setCellStyle(originalStyle);
			        

					// ===== R7 / Col O =====
			        
			        cellO = row.getCell(14);
			        if (cellO == null) cellO = row.createCell(14);
			        originalStyle = cellO.getCellStyle();
			        if (record1.getR7_acct_balance_pula() != null) 
			        cellO.setCellValue(record1.getR7_acct_balance_pula().doubleValue());
			        else cellO.setCellValue("");
			        cellO.setCellStyle(originalStyle);
			        
			        
					// ===== R7 / Col P =====

			        cellP = row.getCell(15);
			        if (cellP == null) cellP = row.createCell(15);
			        originalStyle = cellP.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR7_currency_of_acct() != null) 
			        cellP.setCellValue(record1.getR7_currency_of_acct()); // String directly
			        else cellP.setCellValue("");
			        cellP.setCellStyle(originalStyle);
			        
					// ===== R7 / Col Q =====
			        
			        cellQ = row.getCell(16);
			        if (cellQ == null) cellQ = row.createCell(16);
			        originalStyle = cellQ.getCellStyle();
			        if (record1.getR7_exchange_rate() != null) 
			        cellQ.setCellValue(record1.getR7_exchange_rate().doubleValue());
			        else cellQ.setCellValue("");
			        cellQ.setCellStyle(originalStyle);



					// ===== R8 / Col A =====
			        row = sheet.getRow(7);
			        cellA = row.getCell(0);
			        if (cellA == null) cellA = row.createCell(0);
			        originalStyle = cellA.getCellStyle();
			        if (record1.getR8_bank_spec_single_cust_rec_num() != null) 
			        cellA.setCellValue(record1.getR8_bank_spec_single_cust_rec_num().doubleValue());
			        else cellA.setCellValue("");
			        cellA.setCellStyle(originalStyle);


					// ===== R8 / Col B =====

			        cellB = row.getCell(1);
			        if (cellB == null) cellB = row.createCell(1);
			        originalStyle = cellB.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_company_name() != null) 
			        cellB.setCellValue(record1.getR8_company_name()); // String directly
			        else cellB.setCellValue("");
			        cellB.setCellStyle(originalStyle);
			        
					// ===== R8 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR8_company_reg_num() != null) 
			        cellC.setCellValue(record1.getR8_company_reg_num().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);	
			        
					// ===== R8 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_businees_phy_address() != null) 
			        cellD.setCellValue(record1.getR8_businees_phy_address()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R8 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_postal_address() != null) 
			        cellE.setCellValue(record1.getR8_postal_address()); // String directly
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);		
			        
					// ===== R8 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_country_of_reg() != null) 
			        cellF.setCellValue(record1.getR8_country_of_reg()); // String directly
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);			  
			        
					// ===== R8 / Col G =====

			        cellG = row.getCell(6);
			        if (cellG == null) cellG = row.createCell(6);
			        originalStyle = cellG.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_company_email() != null) 
			        cellG.setCellValue(record1.getR8_company_email()); // String directly
			        else cellG.setCellValue("");
			        cellG.setCellStyle(originalStyle);		
			        
					// ===== R8 / Col H =====

			        cellH = row.getCell(7);
			        if (cellH == null) cellH = row.createCell(7);
			        originalStyle = cellH.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_company_landline() != null) 
			        cellH.setCellValue(record1.getR8_company_landline()); // String directly
			        else cellH.setCellValue("");
			        cellH.setCellStyle(originalStyle);	
			        
					// ===== R8 / Col I =====

			        cellI = row.getCell(8);
			        if (cellI == null) cellI = row.createCell(8);
			        originalStyle = cellI.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_company_mob_phone_num() != null) 
			        cellI.setCellValue(record1.getR8_company_mob_phone_num()); // String directly
			        else cellI.setCellValue("");
			        cellI.setCellStyle(originalStyle);		
			        
					// ===== R8 / Col J =====

			        cellJ = row.getCell(9);
			        if (cellJ == null) cellJ = row.createCell(9);
			        originalStyle = cellJ.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_product_type() != null) 
			        cellJ.setCellValue(record1.getR8_product_type()); // String directly
			        else cellJ.setCellValue("");
			        cellJ.setCellStyle(originalStyle);
			        
					// ===== R8 / Col K =====
			        
			        cellK = row.getCell(10);
			        if (cellK == null) cellK = row.createCell(10);
			        originalStyle = cellK.getCellStyle();
			        if (record1.getR8_acct_num() != null) 
			        cellK.setCellValue(record1.getR8_acct_num().doubleValue());
			        else cellK.setCellValue("");
			        cellK.setCellStyle(originalStyle);
			        
					// ===== R8 / Col L =====

			        cellL = row.getCell(11);
			        if (cellL == null) cellL = row.createCell(11);
			        originalStyle = cellL.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_status_of_acct() != null) 
			        cellL.setCellValue(record1.getR8_status_of_acct()); // String directly
			        else cellL.setCellValue("");
			        cellL.setCellStyle(originalStyle);
			        
					// ===== R8 / Col M =====

			        cellM = row.getCell(12);
			        if (cellM == null) cellM = row.createCell(12);
			        originalStyle = cellM.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_acct_status_fit_or_not_fit_for_straight_throu_payout() != null) 
			        cellM.setCellValue(record1.getR8_acct_status_fit_or_not_fit_for_straight_throu_payout()); // String directly
			        else cellM.setCellValue("");
			        cellM.setCellStyle(originalStyle);
			        
					// ===== R8 / Col N =====

			        cellN = row.getCell(13);
			        if (cellN == null) cellN = row.createCell(13);
			        originalStyle = cellN.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_acct_branch() != null) 
			        cellN.setCellValue(record1.getR8_acct_branch()); // String directly
			        else cellN.setCellValue("");
			        cellN.setCellStyle(originalStyle);
			        

					// ===== R8 / Col O =====
			        
			        cellO = row.getCell(14);
			        if (cellO == null) cellO = row.createCell(14);
			        originalStyle = cellO.getCellStyle();
			        if (record1.getR8_acct_balance_pula() != null) 
			        cellO.setCellValue(record1.getR8_acct_balance_pula().doubleValue());
			        else cellO.setCellValue("");
			        cellO.setCellStyle(originalStyle);
			        
			        
					// ===== R8 / Col P =====

			        cellP = row.getCell(15);
			        if (cellP == null) cellP = row.createCell(15);
			        originalStyle = cellP.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR8_currency_of_acct() != null) 
			        cellP.setCellValue(record1.getR8_currency_of_acct()); // String directly
			        else cellP.setCellValue("");
			        cellP.setCellStyle(originalStyle);
			        
					// ===== R8 / Col Q =====
			        
			        cellQ = row.getCell(16);
			        if (cellQ == null) cellQ = row.createCell(16);
			        originalStyle = cellQ.getCellStyle();
			        if (record1.getR8_exchange_rate() != null) 
			        cellQ.setCellValue(record1.getR8_exchange_rate().doubleValue());
			        else cellQ.setCellValue("");
			        cellQ.setCellStyle(originalStyle);



					// ===== R9 / Col A =====
			        row = sheet.getRow(8);
			        cellA = row.getCell(0);
			        if (cellA == null) cellA = row.createCell(0);
			        originalStyle = cellA.getCellStyle();
			        if (record1.getR9_bank_spec_single_cust_rec_num() != null) 
			        cellA.setCellValue(record1.getR9_bank_spec_single_cust_rec_num().doubleValue());
			        else cellA.setCellValue("");
			        cellA.setCellStyle(originalStyle);


					// ===== R9 / Col B =====

			        cellB = row.getCell(1);
			        if (cellB == null) cellB = row.createCell(1);
			        originalStyle = cellB.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_company_name() != null) 
			        cellB.setCellValue(record1.getR9_company_name()); // String directly
			        else cellB.setCellValue("");
			        cellB.setCellStyle(originalStyle);
			        
					// ===== R9 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR9_company_reg_num() != null) 
			        cellC.setCellValue(record1.getR9_company_reg_num().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);	
			        
					// ===== R9 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_businees_phy_address() != null) 
			        cellD.setCellValue(record1.getR9_businees_phy_address()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R9 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_postal_address() != null) 
			        cellE.setCellValue(record1.getR9_postal_address()); // String directly
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);		
			        
					// ===== R9 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_country_of_reg() != null) 
			        cellF.setCellValue(record1.getR9_country_of_reg()); // String directly
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);			  
			        
					// ===== R9 / Col G =====

			        cellG = row.getCell(6);
			        if (cellG == null) cellG = row.createCell(6);
			        originalStyle = cellG.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_company_email() != null) 
			        cellG.setCellValue(record1.getR9_company_email()); // String directly
			        else cellG.setCellValue("");
			        cellG.setCellStyle(originalStyle);		
			        
					// ===== R9 / Col H =====

			        cellH = row.getCell(7);
			        if (cellH == null) cellH = row.createCell(7);
			        originalStyle = cellH.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_company_landline() != null) 
			        cellH.setCellValue(record1.getR9_company_landline()); // String directly
			        else cellH.setCellValue("");
			        cellH.setCellStyle(originalStyle);	
			        
					// ===== R9 / Col I =====

			        cellI = row.getCell(8);
			        if (cellI == null) cellI = row.createCell(8);
			        originalStyle = cellI.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_company_mob_phone_num() != null) 
			        cellI.setCellValue(record1.getR9_company_mob_phone_num()); // String directly
			        else cellI.setCellValue("");
			        cellI.setCellStyle(originalStyle);		
			        
					// ===== R9 / Col J =====

			        cellJ = row.getCell(9);
			        if (cellJ == null) cellJ = row.createCell(9);
			        originalStyle = cellJ.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_product_type() != null) 
			        cellJ.setCellValue(record1.getR9_product_type()); // String directly
			        else cellJ.setCellValue("");
			        cellJ.setCellStyle(originalStyle);
			        
					// ===== R9 / Col K =====
			        
			        cellK = row.getCell(10);
			        if (cellK == null) cellK = row.createCell(10);
			        originalStyle = cellK.getCellStyle();
			        if (record1.getR9_acct_num() != null) 
			        cellK.setCellValue(record1.getR9_acct_num().doubleValue());
			        else cellK.setCellValue("");
			        cellK.setCellStyle(originalStyle);
			        
					// ===== R9 / Col L =====

			        cellL = row.getCell(11);
			        if (cellL == null) cellL = row.createCell(11);
			        originalStyle = cellL.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_status_of_acct() != null) 
			        cellL.setCellValue(record1.getR9_status_of_acct()); // String directly
			        else cellL.setCellValue("");
			        cellL.setCellStyle(originalStyle);
			        
					// ===== R9 / Col M =====

			        cellM = row.getCell(12);
			        if (cellM == null) cellM = row.createCell(12);
			        originalStyle = cellM.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_acct_status_fit_or_not_fit_for_straight_throu_payout() != null) 
			        cellM.setCellValue(record1.getR9_acct_status_fit_or_not_fit_for_straight_throu_payout()); // String directly
			        else cellM.setCellValue("");
			        cellM.setCellStyle(originalStyle);
			        
					// ===== R9 / Col N =====

			        cellN = row.getCell(13);
			        if (cellN == null) cellN = row.createCell(13);
			        originalStyle = cellN.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_acct_branch() != null) 
			        cellN.setCellValue(record1.getR9_acct_branch()); // String directly
			        else cellN.setCellValue("");
			        cellN.setCellStyle(originalStyle);
			        

					// ===== R9 / Col O =====
			        
			        cellO = row.getCell(14);
			        if (cellO == null) cellO = row.createCell(14);
			        originalStyle = cellO.getCellStyle();
			        if (record1.getR9_acct_balance_pula() != null) 
			        cellO.setCellValue(record1.getR9_acct_balance_pula().doubleValue());
			        else cellO.setCellValue("");
			        cellO.setCellStyle(originalStyle);
			        
			        
					// ===== R9 / Col P =====

			        cellP = row.getCell(15);
			        if (cellP == null) cellP = row.createCell(15);
			        originalStyle = cellP.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR9_currency_of_acct() != null) 
			        cellP.setCellValue(record1.getR9_currency_of_acct()); // String directly
			        else cellP.setCellValue("");
			        cellP.setCellStyle(originalStyle);
			        
					// ===== R9 / Col Q =====
			        
			        cellQ = row.getCell(16);
			        if (cellQ == null) cellQ = row.createCell(16);
			        originalStyle = cellQ.getCellStyle();
			        if (record1.getR9_exchange_rate() != null) 
			        cellQ.setCellValue(record1.getR9_exchange_rate().doubleValue());
			        else cellQ.setCellValue("");
			        cellQ.setCellStyle(originalStyle);
			        


					// ===== R10 / Col A =====
			        row = sheet.getRow(9);
			        cellA = row.getCell(0);
			        if (cellA == null) cellA = row.createCell(0);
			        originalStyle = cellA.getCellStyle();
			        if (record1.getR10_bank_spec_single_cust_rec_num() != null) 
			        cellA.setCellValue(record1.getR10_bank_spec_single_cust_rec_num().doubleValue());
			        else cellA.setCellValue("");
			        cellA.setCellStyle(originalStyle);


					// ===== R10 / Col B =====

			        cellB = row.getCell(1);
			        if (cellB == null) cellB = row.createCell(1);
			        originalStyle = cellB.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_company_name() != null) 
			        cellB.setCellValue(record1.getR10_company_name()); // String directly
			        else cellB.setCellValue("");
			        cellB.setCellStyle(originalStyle);
			        
					// ===== R10 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR10_company_reg_num() != null) 
			        cellC.setCellValue(record1.getR10_company_reg_num().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);	
			        
					// ===== R10 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_businees_phy_address() != null) 
			        cellD.setCellValue(record1.getR10_businees_phy_address()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R10 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_postal_address() != null) 
			        cellE.setCellValue(record1.getR10_postal_address()); // String directly
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);		
			        
					// ===== R10 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_country_of_reg() != null) 
			        cellF.setCellValue(record1.getR10_country_of_reg()); // String directly
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);			  
			        
					// ===== R10 / Col G =====

			        cellG = row.getCell(6);
			        if (cellG == null) cellG = row.createCell(6);
			        originalStyle = cellG.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_company_email() != null) 
			        cellG.setCellValue(record1.getR10_company_email()); // String directly
			        else cellG.setCellValue("");
			        cellG.setCellStyle(originalStyle);		
			        
					// ===== R10 / Col H =====

			        cellH = row.getCell(7);
			        if (cellH == null) cellH = row.createCell(7);
			        originalStyle = cellH.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_company_landline() != null) 
			        cellH.setCellValue(record1.getR10_company_landline()); // String directly
			        else cellH.setCellValue("");
			        cellH.setCellStyle(originalStyle);	
			        
					// ===== R10 / Col I =====

			        cellI = row.getCell(8);
			        if (cellI == null) cellI = row.createCell(8);
			        originalStyle = cellI.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_company_mob_phone_num() != null) 
			        cellI.setCellValue(record1.getR10_company_mob_phone_num()); // String directly
			        else cellI.setCellValue("");
			        cellI.setCellStyle(originalStyle);		
			        
					// ===== R10 / Col J =====

			        cellJ = row.getCell(9);
			        if (cellJ == null) cellJ = row.createCell(9);
			        originalStyle = cellJ.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_product_type() != null) 
			        cellJ.setCellValue(record1.getR10_product_type()); // String directly
			        else cellJ.setCellValue("");
			        cellJ.setCellStyle(originalStyle);
			        
					// ===== R10 / Col K =====
			        
			        cellK = row.getCell(10);
			        if (cellK == null) cellK = row.createCell(10);
			        originalStyle = cellK.getCellStyle();
			        if (record1.getR10_acct_num() != null) 
			        cellK.setCellValue(record1.getR10_acct_num().doubleValue());
			        else cellK.setCellValue("");
			        cellK.setCellStyle(originalStyle);
			        
					// ===== R10 / Col L =====

			        cellL = row.getCell(11);
			        if (cellL == null) cellL = row.createCell(11);
			        originalStyle = cellL.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_status_of_acct() != null) 
			        cellL.setCellValue(record1.getR10_status_of_acct()); // String directly
			        else cellL.setCellValue("");
			        cellL.setCellStyle(originalStyle);
			        
					// ===== R10 / Col M =====

			        cellM = row.getCell(12);
			        if (cellM == null) cellM = row.createCell(12);
			        originalStyle = cellM.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_acct_status_fit_or_not_fit_for_straight_throu_payout() != null) 
			        cellM.setCellValue(record1.getR10_acct_status_fit_or_not_fit_for_straight_throu_payout()); // String directly
			        else cellM.setCellValue("");
			        cellM.setCellStyle(originalStyle);
			        
					// ===== R10 / Col N =====

			        cellN = row.getCell(13);
			        if (cellN == null) cellN = row.createCell(13);
			        originalStyle = cellN.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_acct_branch() != null) 
			        cellN.setCellValue(record1.getR10_acct_branch()); // String directly
			        else cellN.setCellValue("");
			        cellN.setCellStyle(originalStyle);
			        

					// ===== R10 / Col O =====
			        
			        cellO = row.getCell(14);
			        if (cellO == null) cellO = row.createCell(14);
			        originalStyle = cellO.getCellStyle();
			        if (record1.getR10_acct_balance_pula() != null) 
			        cellO.setCellValue(record1.getR10_acct_balance_pula().doubleValue());
			        else cellO.setCellValue("");
			        cellO.setCellStyle(originalStyle);
			        
			        
					// ===== R10 / Col P =====

			        cellP = row.getCell(15);
			        if (cellP == null) cellP = row.createCell(15);
			        originalStyle = cellP.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR10_currency_of_acct() != null) 
			        cellP.setCellValue(record1.getR10_currency_of_acct()); // String directly
			        else cellP.setCellValue("");
			        cellP.setCellStyle(originalStyle);
			        
					// ===== R10 / Col Q =====
			        
			        cellQ = row.getCell(16);
			        if (cellQ == null) cellQ = row.createCell(16);
			        originalStyle = cellQ.getCellStyle();
			        if (record1.getR10_exchange_rate() != null) 
			        cellQ.setCellValue(record1.getR10_exchange_rate().doubleValue());
			        else cellQ.setCellValue("");
			        cellQ.setCellStyle(originalStyle);


					// ===== R11 / Col A =====
			        row = sheet.getRow(10);
			        cellA = row.getCell(0);
			        if (cellA == null) cellA = row.createCell(0);
			        originalStyle = cellA.getCellStyle();
			        if (record1.getR11_bank_spec_single_cust_rec_num() != null) 
			        cellA.setCellValue(record1.getR11_bank_spec_single_cust_rec_num().doubleValue());
			        else cellA.setCellValue("");
			        cellA.setCellStyle(originalStyle);


					// ===== R11 / Col B =====

			        cellB = row.getCell(1);
			        if (cellB == null) cellB = row.createCell(1);
			        originalStyle = cellB.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_company_name() != null) 
			        cellB.setCellValue(record1.getR11_company_name()); // String directly
			        else cellB.setCellValue("");
			        cellB.setCellStyle(originalStyle);
			        
					// ===== R11 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR11_company_reg_num() != null) 
			        cellC.setCellValue(record1.getR11_company_reg_num().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);	
			        
					// ===== R11 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_businees_phy_address() != null) 
			        cellD.setCellValue(record1.getR11_businees_phy_address()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R11 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_postal_address() != null) 
			        cellE.setCellValue(record1.getR11_postal_address()); // String directly
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);		
			        
					// ===== R11 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_country_of_reg() != null) 
			        cellF.setCellValue(record1.getR11_country_of_reg()); // String directly
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);			  
			        
					// ===== R11 / Col G =====

			        cellG = row.getCell(6);
			        if (cellG == null) cellG = row.createCell(6);
			        originalStyle = cellG.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_company_email() != null) 
			        cellG.setCellValue(record1.getR11_company_email()); // String directly
			        else cellG.setCellValue("");
			        cellG.setCellStyle(originalStyle);		
			        
					// ===== R11 / Col H =====

			        cellH = row.getCell(7);
			        if (cellH == null) cellH = row.createCell(7);
			        originalStyle = cellH.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_company_landline() != null) 
			        cellH.setCellValue(record1.getR11_company_landline()); // String directly
			        else cellH.setCellValue("");
			        cellH.setCellStyle(originalStyle);	
			        
					// ===== R11 / Col I =====

			        cellI = row.getCell(8);
			        if (cellI == null) cellI = row.createCell(8);
			        originalStyle = cellI.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_company_mob_phone_num() != null) 
			        cellI.setCellValue(record1.getR11_company_mob_phone_num()); // String directly
			        else cellI.setCellValue("");
			        cellI.setCellStyle(originalStyle);		
			        
					// ===== R11 / Col J =====

			        cellJ = row.getCell(9);
			        if (cellJ == null) cellJ = row.createCell(9);
			        originalStyle = cellJ.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_product_type() != null) 
			        cellJ.setCellValue(record1.getR11_product_type()); // String directly
			        else cellJ.setCellValue("");
			        cellJ.setCellStyle(originalStyle);
			        
					// ===== R11 / Col K =====
			        
			        cellK = row.getCell(10);
			        if (cellK == null) cellK = row.createCell(10);
			        originalStyle = cellK.getCellStyle();
			        if (record1.getR11_acct_num() != null) 
			        cellK.setCellValue(record1.getR11_acct_num().doubleValue());
			        else cellK.setCellValue("");
			        cellK.setCellStyle(originalStyle);
			        
					// ===== R11 / Col L =====

			        cellL = row.getCell(11);
			        if (cellL == null) cellL = row.createCell(11);
			        originalStyle = cellL.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_status_of_acct() != null) 
			        cellL.setCellValue(record1.getR11_status_of_acct()); // String directly
			        else cellL.setCellValue("");
			        cellL.setCellStyle(originalStyle);
			        
					// ===== R11 / Col M =====

			        cellM = row.getCell(12);
			        if (cellM == null) cellM = row.createCell(12);
			        originalStyle = cellM.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_acct_status_fit_or_not_fit_for_straight_throu_payout() != null) 
			        cellM.setCellValue(record1.getR11_acct_status_fit_or_not_fit_for_straight_throu_payout()); // String directly
			        else cellM.setCellValue("");
			        cellM.setCellStyle(originalStyle);
			        
					// ===== R11 / Col N =====

			        cellN = row.getCell(13);
			        if (cellN == null) cellN = row.createCell(13);
			        originalStyle = cellN.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_acct_branch() != null) 
			        cellN.setCellValue(record1.getR11_acct_branch()); // String directly
			        else cellN.setCellValue("");
			        cellN.setCellStyle(originalStyle);
			        

					// ===== R11 / Col O =====
			        
			        cellO = row.getCell(14);
			        if (cellO == null) cellO = row.createCell(14);
			        originalStyle = cellO.getCellStyle();
			        if (record1.getR11_acct_balance_pula() != null) 
			        cellO.setCellValue(record1.getR11_acct_balance_pula().doubleValue());
			        else cellO.setCellValue("");
			        cellO.setCellStyle(originalStyle);
			        
			        
					// ===== R11 / Col P =====

			        cellP = row.getCell(15);
			        if (cellP == null) cellP = row.createCell(15);
			        originalStyle = cellP.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR11_currency_of_acct() != null) 
			        cellP.setCellValue(record1.getR11_currency_of_acct()); // String directly
			        else cellP.setCellValue("");
			        cellP.setCellStyle(originalStyle);
			        
					// ===== R11 / Col Q =====
			        
			        cellQ = row.getCell(16);
			        if (cellQ == null) cellQ = row.createCell(16);
			        originalStyle = cellQ.getCellStyle();
			        if (record1.getR11_exchange_rate() != null) 
			        cellQ.setCellValue(record1.getR11_exchange_rate().doubleValue());
			        else cellQ.setCellValue("");
			        cellQ.setCellStyle(originalStyle);



					// ===== R12 / Col A =====
			        row = sheet.getRow(11);
			        cellA = row.getCell(0);
			        if (cellA == null) cellA = row.createCell(0);
			        originalStyle = cellA.getCellStyle();
			        if (record1.getR12_bank_spec_single_cust_rec_num() != null) 
			        cellA.setCellValue(record1.getR12_bank_spec_single_cust_rec_num().doubleValue());
			        else cellA.setCellValue("");
			        cellA.setCellStyle(originalStyle);


					// ===== R12 / Col B =====

			        cellB = row.getCell(1);
			        if (cellB == null) cellB = row.createCell(1);
			        originalStyle = cellB.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_company_name() != null) 
			        cellB.setCellValue(record1.getR12_company_name()); // String directly
			        else cellB.setCellValue("");
			        cellB.setCellStyle(originalStyle);
			        
					// ===== R12 / Col C =====
			        
			        cellC = row.getCell(2);
			        if (cellC == null) cellC = row.createCell(2);
			        originalStyle = cellC.getCellStyle();
			        if (record1.getR12_company_reg_num() != null) 
			        cellC.setCellValue(record1.getR12_company_reg_num().doubleValue());
			        else cellC.setCellValue("");
			        cellC.setCellStyle(originalStyle);	
			        
					// ===== R12 / Col D =====

			        cellD = row.getCell(3);
			        if (cellD == null) cellD = row.createCell(3);
			        originalStyle = cellD.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_businees_phy_address() != null) 
			        cellD.setCellValue(record1.getR12_businees_phy_address()); // String directly
			        else cellD.setCellValue("");
			        cellD.setCellStyle(originalStyle);
			        
					// ===== R12 / Col E =====

			        cellE = row.getCell(4);
			        if (cellE == null) cellE = row.createCell(4);
			        originalStyle = cellE.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_postal_address() != null) 
			        cellE.setCellValue(record1.getR12_postal_address()); // String directly
			        else cellE.setCellValue("");
			        cellE.setCellStyle(originalStyle);		
			        
					// ===== R12 / Col F =====

			        cellF = row.getCell(5);
			        if (cellF == null) cellF = row.createCell(5);
			        originalStyle = cellF.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_country_of_reg() != null) 
			        cellF.setCellValue(record1.getR12_country_of_reg()); // String directly
			        else cellF.setCellValue("");
			        cellF.setCellStyle(originalStyle);			  
			        
					// ===== R12 / Col G =====

			        cellG = row.getCell(6);
			        if (cellG == null) cellG = row.createCell(6);
			        originalStyle = cellG.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_company_email() != null) 
			        cellG.setCellValue(record1.getR12_company_email()); // String directly
			        else cellG.setCellValue("");
			        cellG.setCellStyle(originalStyle);		
			        
					// ===== R12 / Col H =====

			        cellH = row.getCell(7);
			        if (cellH == null) cellH = row.createCell(7);
			        originalStyle = cellH.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_company_landline() != null) 
			        cellH.setCellValue(record1.getR12_company_landline()); // String directly
			        else cellH.setCellValue("");
			        cellH.setCellStyle(originalStyle);	
			        
					// ===== R12 / Col I =====

			        cellI = row.getCell(8);
			        if (cellI == null) cellI = row.createCell(8);
			        originalStyle = cellI.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_company_mob_phone_num() != null) 
			        cellI.setCellValue(record1.getR12_company_mob_phone_num()); // String directly
			        else cellI.setCellValue("");
			        cellI.setCellStyle(originalStyle);		
			        
					// ===== R12 / Col J =====

			        cellJ = row.getCell(9);
			        if (cellJ == null) cellJ = row.createCell(9);
			        originalStyle = cellJ.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_product_type() != null) 
			        cellJ.setCellValue(record1.getR12_product_type()); // String directly
			        else cellJ.setCellValue("");
			        cellJ.setCellStyle(originalStyle);
			        
					// ===== R12 / Col K =====
			        
			        cellK = row.getCell(10);
			        if (cellK == null) cellK = row.createCell(10);
			        originalStyle = cellK.getCellStyle();
			        if (record1.getR12_acct_num() != null) 
			        cellK.setCellValue(record1.getR12_acct_num().doubleValue());
			        else cellK.setCellValue("");
			        cellK.setCellStyle(originalStyle);
			        
					// ===== R12 / Col L =====

			        cellL = row.getCell(11);
			        if (cellL == null) cellL = row.createCell(11);
			        originalStyle = cellL.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_status_of_acct() != null) 
			        cellL.setCellValue(record1.getR12_status_of_acct()); // String directly
			        else cellL.setCellValue("");
			        cellL.setCellStyle(originalStyle);
			        
					// ===== R12 / Col M =====

			        cellM = row.getCell(12);
			        if (cellM == null) cellM = row.createCell(12);
			        originalStyle = cellM.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_acct_status_fit_or_not_fit_for_straight_throu_payout() != null) 
			        cellM.setCellValue(record1.getR12_acct_status_fit_or_not_fit_for_straight_throu_payout()); // String directly
			        else cellM.setCellValue("");
			        cellM.setCellStyle(originalStyle);
			        
					// ===== R12 / Col N =====

			        cellN = row.getCell(13);
			        if (cellN == null) cellN = row.createCell(13);
			        originalStyle = cellN.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_acct_branch() != null) 
			        cellN.setCellValue(record1.getR12_acct_branch()); // String directly
			        else cellN.setCellValue("");
			        cellN.setCellStyle(originalStyle);
			        

					// ===== R12 / Col O =====
			        
			        cellO = row.getCell(14);
			        if (cellO == null) cellO = row.createCell(14);
			        originalStyle = cellO.getCellStyle();
			        if (record1.getR12_acct_balance_pula() != null) 
			        cellO.setCellValue(record1.getR12_acct_balance_pula().doubleValue());
			        else cellO.setCellValue("");
			        cellO.setCellStyle(originalStyle);
			        
			        
					// ===== R12 / Col P =====

			        cellP = row.getCell(15);
			        if (cellP == null) cellP = row.createCell(15);
			        originalStyle = cellP.getCellStyle();
			     // ✅ Handle String value
			        if (record1.getR12_currency_of_acct() != null) 
			        cellP.setCellValue(record1.getR12_currency_of_acct()); // String directly
			        else cellP.setCellValue("");
			        cellP.setCellStyle(originalStyle);
			        
					// ===== R12 / Col Q =====
			        
			        cellQ = row.getCell(16);
			        if (cellQ == null) cellQ = row.createCell(16);
			        originalStyle = cellQ.getCellStyle();
			        if (record1.getR12_exchange_rate() != null) 
			        cellQ.setCellValue(record1.getR12_exchange_rate().doubleValue());
			        else cellQ.setCellValue("");
			        cellQ.setCellStyle(originalStyle);
		        		        
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


	
	

