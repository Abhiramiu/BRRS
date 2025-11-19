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
// import java.util.ArrayList;  // SHOW WARNING HERE
import java.util.Date;
import java.util.List;
import java.util.Optional;

// import javax.servlet.http.HttpServletRequest; // SHOW WARNING HERE

import org.apache.poi.ss.usermodel.*;
// import org.apache.poi.xssf.usermodel.XSSFWorkbook; // SHOW WARNING HERE
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


import com.bornfire.brrs.entities.BRRS_M_OR2_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_OR2_Summary_Repo;
import com.bornfire.brrs.entities.M_OR2_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_OR2_Summary_Entity;
import com.bornfire.brrs.entities.M_SECL_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12F_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12F_Summary_Entity;


@Component
@Service
public class BRRS_M_OR2_ReportService {
	
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_OR2_ReportService.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	SessionFactory sessionFactory;
	
	
	@Autowired
	BRRS_M_OR2_Summary_Repo M_OR2_Summary_Repo; 
	
	@Autowired
	BRRS_M_OR2_Archival_Summary_Repo M_OR2_Archival_Summary_Repo; 

	
	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_OR2View(String reportId, String fromdate, String todate, 
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
            List<M_OR2_Archival_Summary_Entity> T1Master = 
                M_OR2_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 2: RESUB ----------
        else if ("RESUB".equalsIgnoreCase(type) && version != null) {
            List<M_OR2_Archival_Summary_Entity> T1Master =
                M_OR2_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 3: NORMAL ----------
        else {
            List<M_OR2_Summary_Entity> T1Master = 
                M_OR2_Summary_Repo.getdatabydateListWithVersion(todate);
            System.out.println("T1Master Size "+T1Master.size());
            mv.addObject("reportsummary", T1Master);
        }

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_OR2");
		mv.addObject("displaymode", "summary");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}
	
	
	public void updateReport(M_OR2_Summary_Entity updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("report_date: " + updatedEntity.getReportDate());

	    M_OR2_Summary_Entity existing = M_OR2_Summary_Repo.findById(updatedEntity.getReportDate())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

	    try {
	        // Loop from R11 to R50 and copy fields
	        for (int i = 12; i <= 23; i++) {
	            String prefix = "r" + i + "_";
	            String[] fields = {"corporate_finance",
	            		"trading_and_sales",
	            		"retail_banking",
	            		"commercial_banking",
	            		"payments_and_settlements",
	            		"agency_services",
	            		"asset_management"};

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_OR2_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_OR2_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
	                    

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);
	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        // Loop from R17 to R30 and copy fields
	        for (int i = 28; i <= 39; i++) {
	            String prefix = "r" + i + "_";
	            String[] fields = {"corporate_finance",
	            		"trading_and_sales",
	            		"retail_banking",
	            		"commercial_banking",
	            		"payments_and_settlements",
	            		"agency_services",
	            		"asset_management"};

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_OR2_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_OR2_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);
	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        // Loop from R32 to R33 and copy fields
	        for (int i = 44; i <= 55; i++) {
	            String prefix = "r" + i + "_";
	            String[] fields = {"corporate_finance",
	            		"trading_and_sales",
	            		"retail_banking",
	            		"commercial_banking",
	            		"payments_and_settlements",
	            		"agency_services",
	            		"asset_management"};

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_OR2_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_OR2_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);
	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        // Loop from R35 to R36 and copy fields
	        for (int i = 59; i <= 65; i++) {
	            String prefix = "r" + i + "_";
	            String[] fields = {"operational_risk_cap_charge","risk_weight_factor"};

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_OR2_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_OR2_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);
	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	            String[] totalFields = {
	            		"corporate_finance",
	            		"trading_and_sales",
	            		"retail_banking",
	            		"commercial_banking",
	            		"payments_and_settlements",
	            		"agency_services",
	            		"asset_management",
	            		"operational_risk_cap_charge",
	            		"risk_weight_factor"
	            };

	            // Loop from R12 to R57 and copy fields
	            for (int i = 12; i <= 65; i++) {
	                String prefix = "r" + i + "_";

	                for (String field : totalFields) {
	                    String getterName = "get" + prefix + field;
	                    String setterName = "set" + prefix + field;

	                    try {
	                        Method getter = M_OR2_Summary_Entity.class.getMethod(getterName);
	                        Method setter = M_OR2_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                        Object newValue = getter.invoke(updatedEntity);
	                        setter.invoke(existing, newValue);
	                    } catch (NoSuchMethodException e) {
	                        // Skip if field does not exist for this row/field
	                        continue;
	                    }
	                }
	            }
	            
	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // Save updated entity
	    System.out.println("abc");
	    M_OR2_Summary_Repo.save(existing);
	}
	
	
	
	public byte[] getM_OR2Excel(String filename, String reportId, String fromdate, String todate, String currency,
			 String dtltype, String type, String version) throws Exception {
logger.info("Service: Starting Excel generation process in memory.");
logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

// Convert string to Date
Date reportDate = dateformat.parse(todate);

// ARCHIVAL check
if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
logger.info("Service: Generating ARCHIVAL report for version {}", version);
return getExcelM_OR2ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
}
// RESUB check
else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
logger.info("Service: Generating RESUB report for version {}", version);


List<M_OR2_Archival_Summary_Entity> T1Master =
M_OR2_Archival_Summary_Repo.getdatabydateListarchival(reportDate, version);

// Generate Excel for RESUB
return BRRS_M_OR2ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
}




// Default (LIVE) case
List<M_OR2_Summary_Entity> dataList1 = M_OR2_Summary_Repo.getdatabydateList(reportDate);

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

			
			int startRow = 11;
			
			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					M_OR2_Summary_Entity record = dataList1.get(i);
					System.out.println("rownumber="+startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

		
					//row12
					// Column D 
					Cell cell4 = row.createCell(3);
					if (record.getR12_corporate_finance() != null) {
						cell4.setCellValue(record.getR12_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row12
					// Column E 
					Cell cell5 = row.createCell(4);
					if (record.getR12_trading_and_sales() != null) {
						cell5.setCellValue(record.getR12_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					} 
					
					
					//row12
					// Column F
					Cell cell6 = row.createCell(5);
					if (record.getR12_retail_banking() != null) {
						cell6.setCellValue(record.getR12_retail_banking().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					//row12
					// Column G
					Cell cell7 = row.createCell(6);
					if (record.getR12_commercial_banking()  != null) {
						cell7.setCellValue(record.getR12_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					//row12
					// Column H
					Cell cell8 = row.createCell(7);
					if (record.getR12_payments_and_settlements()  != null) {
						cell8.setCellValue(record.getR12_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					//row12
					// Column I
					Cell cell9 = row.createCell(8);
					if (record.getR12_agency_services() != null) {
						cell9.setCellValue(record.getR12_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					//row12
					// Column J
					Cell cell10 = row.createCell(9);
					if (record.getR12_asset_management()  != null) {
						cell10.setCellValue(record.getR12_asset_management() .doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					//row12
					// Column K
					Cell cell11 = row.createCell(10);
					if (record.getR12_retail_brokerage() != null) {
						cell11.setCellValue(record.getR12_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row13
					row = sheet.getRow(12);
					
					// Column D
					 cell4 = row.createCell(3);
					if (record.getR13_corporate_finance() != null) {
						cell4.setCellValue(record.getR13_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR13_trading_and_sales() != null) {
						cell5.setCellValue(record.getR13_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR13_retail_banking()  != null) {
						cell6.setCellValue(record.getR13_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR13_commercial_banking() != null) {
						cell7.setCellValue(record.getR13_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR13_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR13_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR13_agency_services() != null) {
						cell9.setCellValue(record.getR13_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR13_asset_management() != null) {
						cell10.setCellValue(record.getR13_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR13_retail_brokerage() != null) {
						cell11.setCellValue(record.getR13_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					
					
					//row14
					row = sheet.getRow(13);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR14_corporate_finance() != null) {
						cell4.setCellValue(record.getR14_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR14_trading_and_sales() != null) {
						cell5.setCellValue(record.getR14_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR14_retail_banking()  != null) {
						cell6.setCellValue(record.getR14_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR14_commercial_banking() != null) {
						cell7.setCellValue(record.getR14_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR14_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR14_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR14_agency_services() != null) {
						cell9.setCellValue(record.getR14_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR14_asset_management() != null) {
						cell10.setCellValue(record.getR14_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR14_retail_brokerage() != null) {
						cell11.setCellValue(record.getR14_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row15
					row = sheet.getRow(14);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR15_corporate_finance() != null) {
						cell4.setCellValue(record.getR15_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR15_trading_and_sales() != null) {
						cell5.setCellValue(record.getR15_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR15_retail_banking()  != null) {
						cell6.setCellValue(record.getR15_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR15_commercial_banking() != null) {
						cell7.setCellValue(record.getR15_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR15_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR15_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR15_agency_services() != null) {
						cell9.setCellValue(record.getR15_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR15_asset_management() != null) {
						cell10.setCellValue(record.getR15_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR15_retail_brokerage() != null) {
						cell11.setCellValue(record.getR15_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row16
					row = sheet.getRow(15);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR16_corporate_finance() != null) {
						cell4.setCellValue(record.getR16_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR16_trading_and_sales() != null) {
						cell5.setCellValue(record.getR16_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR16_retail_banking()  != null) {
						cell6.setCellValue(record.getR16_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR16_commercial_banking() != null) {
						cell7.setCellValue(record.getR16_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR16_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR16_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR16_agency_services() != null) {
						cell9.setCellValue(record.getR16_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR16_asset_management() != null) {
						cell10.setCellValue(record.getR16_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR16_retail_brokerage() != null) {
						cell11.setCellValue(record.getR16_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row17
					row = sheet.getRow(16);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR17_corporate_finance() != null) {
						cell4.setCellValue(record.getR17_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR17_trading_and_sales() != null) {
						cell5.setCellValue(record.getR17_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR17_retail_banking()  != null) {
						cell6.setCellValue(record.getR17_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR17_commercial_banking() != null) {
						cell7.setCellValue(record.getR17_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR17_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR17_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR17_agency_services() != null) {
						cell9.setCellValue(record.getR17_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR17_asset_management() != null) {
						cell10.setCellValue(record.getR17_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR17_retail_brokerage() != null) {
						cell11.setCellValue(record.getR17_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row18
					row = sheet.getRow(17);
					
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR18_corporate_finance() != null) {
						cell4.setCellValue(record.getR18_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR18_trading_and_sales() != null) {
						cell5.setCellValue(record.getR18_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR18_retail_banking()  != null) {
						cell6.setCellValue(record.getR18_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR18_commercial_banking() != null) {
						cell7.setCellValue(record.getR18_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR18_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR18_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR18_agency_services() != null) {
						cell9.setCellValue(record.getR18_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR18_asset_management() != null) {
						cell10.setCellValue(record.getR18_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR18_retail_brokerage() != null) {
						cell11.setCellValue(record.getR18_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row19
					row = sheet.getRow(18);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR19_corporate_finance() != null) {
						cell4.setCellValue(record.getR19_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR19_trading_and_sales() != null) {
						cell5.setCellValue(record.getR19_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR19_retail_banking()  != null) {
						cell6.setCellValue(record.getR19_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR19_commercial_banking() != null) {
						cell7.setCellValue(record.getR19_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR19_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR19_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR19_agency_services() != null) {
						cell9.setCellValue(record.getR19_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR19_asset_management() != null) {
						cell10.setCellValue(record.getR19_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR19_retail_brokerage() != null) {
						cell11.setCellValue(record.getR19_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row20
					row = sheet.getRow(19);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR20_corporate_finance() != null) {
						cell4.setCellValue(record.getR20_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR20_trading_and_sales() != null) {
						cell5.setCellValue(record.getR20_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR20_retail_banking()  != null) {
						cell6.setCellValue(record.getR20_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR20_commercial_banking() != null) {
						cell7.setCellValue(record.getR20_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR20_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR20_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR20_agency_services() != null) {
						cell9.setCellValue(record.getR20_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR20_asset_management() != null) {
						cell10.setCellValue(record.getR20_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR20_retail_brokerage() != null) {
						cell11.setCellValue(record.getR20_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row21
					row = sheet.getRow(20);
					
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR21_corporate_finance() != null) {
						cell4.setCellValue(record.getR21_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR21_trading_and_sales() != null) {
						cell5.setCellValue(record.getR21_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR21_retail_banking()  != null) {
						cell6.setCellValue(record.getR21_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR21_commercial_banking() != null) {
						cell7.setCellValue(record.getR21_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR21_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR21_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR21_agency_services() != null) {
						cell9.setCellValue(record.getR21_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR21_asset_management() != null) {
						cell10.setCellValue(record.getR21_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR21_retail_brokerage() != null) {
						cell11.setCellValue(record.getR21_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row22
					row = sheet.getRow(21);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR22_corporate_finance() != null) {
						cell4.setCellValue(record.getR22_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR22_trading_and_sales() != null) {
						cell5.setCellValue(record.getR22_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR22_retail_banking()  != null) {
						cell6.setCellValue(record.getR22_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR22_commercial_banking() != null) {
						cell7.setCellValue(record.getR22_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR22_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR22_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR22_agency_services() != null) {
						cell9.setCellValue(record.getR22_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR22_asset_management() != null) {
						cell10.setCellValue(record.getR22_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR22_retail_brokerage() != null) {
						cell11.setCellValue(record.getR22_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row23
					row = sheet.getRow(22);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR23_corporate_finance() != null) {
						cell4.setCellValue(record.getR23_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR23_trading_and_sales() != null) {
						cell5.setCellValue(record.getR23_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR23_retail_banking()  != null) {
						cell6.setCellValue(record.getR23_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR23_commercial_banking() != null) {
						cell7.setCellValue(record.getR23_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR23_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR23_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR23_agency_services() != null) {
						cell9.setCellValue(record.getR23_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR23_asset_management() != null) {
						cell10.setCellValue(record.getR23_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR23_retail_brokerage() != null) {
						cell11.setCellValue(record.getR23_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row28
					row = sheet.getRow(27);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR28_corporate_finance() != null) {
						cell4.setCellValue(record.getR28_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR28_trading_and_sales() != null) {
						cell5.setCellValue(record.getR28_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR28_retail_banking()  != null) {
						cell6.setCellValue(record.getR28_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR28_commercial_banking() != null) {
						cell7.setCellValue(record.getR28_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR28_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR28_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR28_agency_services() != null) {
						cell9.setCellValue(record.getR28_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR28_asset_management() != null) {
						cell10.setCellValue(record.getR28_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR28_retail_brokerage() != null) {
						cell11.setCellValue(record.getR28_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row29
					row = sheet.getRow(28);
					
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR29_corporate_finance() != null) {
						cell4.setCellValue(record.getR29_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR29_trading_and_sales() != null) {
						cell5.setCellValue(record.getR29_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR29_retail_banking()  != null) {
						cell6.setCellValue(record.getR29_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR29_commercial_banking() != null) {
						cell7.setCellValue(record.getR29_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR29_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR29_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR29_agency_services() != null) {
						cell9.setCellValue(record.getR29_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR29_asset_management() != null) {
						cell10.setCellValue(record.getR29_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR29_retail_brokerage() != null) {
						cell11.setCellValue(record.getR29_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row30
					row = sheet.getRow(29);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR30_corporate_finance() != null) {
						cell4.setCellValue(record.getR30_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR30_trading_and_sales() != null) {
						cell5.setCellValue(record.getR30_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR30_retail_banking()  != null) {
						cell6.setCellValue(record.getR30_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR30_commercial_banking() != null) {
						cell7.setCellValue(record.getR30_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR30_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR30_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR30_agency_services() != null) {
						cell9.setCellValue(record.getR30_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR30_asset_management() != null) {
						cell10.setCellValue(record.getR30_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR30_retail_brokerage() != null) {
						cell11.setCellValue(record.getR30_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row31
					row = sheet.getRow(30);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR31_corporate_finance() != null) {
						cell4.setCellValue(record.getR31_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR31_trading_and_sales() != null) {
						cell5.setCellValue(record.getR31_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR31_retail_banking()  != null) {
						cell6.setCellValue(record.getR31_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR31_commercial_banking() != null) {
						cell7.setCellValue(record.getR31_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR31_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR31_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR31_agency_services() != null) {
						cell9.setCellValue(record.getR31_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR31_asset_management() != null) {
						cell10.setCellValue(record.getR31_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR31_retail_brokerage() != null) {
						cell11.setCellValue(record.getR31_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row32
					row = sheet.getRow(31);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR32_corporate_finance() != null) {
						cell4.setCellValue(record.getR32_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR32_trading_and_sales() != null) {
						cell5.setCellValue(record.getR32_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR32_retail_banking()  != null) {
						cell6.setCellValue(record.getR32_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR32_commercial_banking() != null) {
						cell7.setCellValue(record.getR32_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR32_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR32_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR32_agency_services() != null) {
						cell9.setCellValue(record.getR32_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR32_asset_management() != null) {
						cell10.setCellValue(record.getR32_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR32_retail_brokerage() != null) {
						cell11.setCellValue(record.getR32_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row33
					row = sheet.getRow(32);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR33_corporate_finance() != null) {
						cell4.setCellValue(record.getR33_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR33_trading_and_sales() != null) {
						cell5.setCellValue(record.getR33_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR33_retail_banking()  != null) {
						cell6.setCellValue(record.getR33_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR33_commercial_banking() != null) {
						cell7.setCellValue(record.getR33_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR33_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR33_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR33_agency_services() != null) {
						cell9.setCellValue(record.getR33_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR33_asset_management() != null) {
						cell10.setCellValue(record.getR33_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR33_retail_brokerage() != null) {
						cell11.setCellValue(record.getR33_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row34
					row = sheet.getRow(33);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR34_corporate_finance() != null) {
						cell4.setCellValue(record.getR34_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR34_trading_and_sales() != null) {
						cell5.setCellValue(record.getR34_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR34_retail_banking()  != null) {
						cell6.setCellValue(record.getR34_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR34_commercial_banking() != null) {
						cell7.setCellValue(record.getR34_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR34_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR34_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR34_agency_services() != null) {
						cell9.setCellValue(record.getR34_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR34_asset_management() != null) {
						cell10.setCellValue(record.getR34_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR34_retail_brokerage() != null) {
						cell11.setCellValue(record.getR34_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row35
					row = sheet.getRow(34);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR35_corporate_finance() != null) {
						cell4.setCellValue(record.getR35_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR35_trading_and_sales() != null) {
						cell5.setCellValue(record.getR35_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR35_retail_banking()  != null) {
						cell6.setCellValue(record.getR35_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR35_commercial_banking() != null) {
						cell7.setCellValue(record.getR35_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR35_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR35_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR35_agency_services() != null) {
						cell9.setCellValue(record.getR35_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR35_asset_management() != null) {
						cell10.setCellValue(record.getR35_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR35_retail_brokerage() != null) {
						cell11.setCellValue(record.getR35_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row 36
					row = sheet.getRow(35);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR36_corporate_finance() != null) {
						cell4.setCellValue(record.getR36_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR36_trading_and_sales() != null) {
						cell5.setCellValue(record.getR36_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR36_retail_banking()  != null) {
						cell6.setCellValue(record.getR36_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR36_commercial_banking() != null) {
						cell7.setCellValue(record.getR36_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR36_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR36_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR36_agency_services() != null) {
						cell9.setCellValue(record.getR36_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR36_asset_management() != null) {
						cell10.setCellValue(record.getR36_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR36_retail_brokerage() != null) {
						cell11.setCellValue(record.getR36_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row37
					row = sheet.getRow(36);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR37_corporate_finance() != null) {
						cell4.setCellValue(record.getR37_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR37_trading_and_sales() != null) {
						cell5.setCellValue(record.getR37_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR37_retail_banking()  != null) {
						cell6.setCellValue(record.getR37_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR37_commercial_banking() != null) {
						cell7.setCellValue(record.getR37_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR37_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR37_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR37_agency_services() != null) {
						cell9.setCellValue(record.getR37_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR37_asset_management() != null) {
						cell10.setCellValue(record.getR37_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR37_retail_brokerage() != null) {
						cell11.setCellValue(record.getR37_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row38
					row = sheet.getRow(37);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR38_corporate_finance() != null) {
						cell4.setCellValue(record.getR38_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR38_trading_and_sales() != null) {
						cell5.setCellValue(record.getR38_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR38_retail_banking()  != null) {
						cell6.setCellValue(record.getR38_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR38_commercial_banking() != null) {
						cell7.setCellValue(record.getR38_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR38_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR38_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR38_agency_services() != null) {
						cell9.setCellValue(record.getR38_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR38_asset_management() != null) {
						cell10.setCellValue(record.getR38_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR38_retail_brokerage() != null) {
						cell11.setCellValue(record.getR38_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row 39
					row = sheet.getRow(38);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR39_corporate_finance() != null) {
						cell4.setCellValue(record.getR39_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR39_trading_and_sales() != null) {
						cell5.setCellValue(record.getR39_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR39_retail_banking()  != null) {
						cell6.setCellValue(record.getR39_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR39_commercial_banking() != null) {
						cell7.setCellValue(record.getR39_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR39_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR39_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR39_agency_services() != null) {
						cell9.setCellValue(record.getR39_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR39_asset_management() != null) {
						cell10.setCellValue(record.getR39_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR39_retail_brokerage() != null) {
						cell11.setCellValue(record.getR39_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row44
					row = sheet.getRow(43);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR44_corporate_finance() != null) {
						cell4.setCellValue(record.getR44_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR44_trading_and_sales() != null) {
						cell5.setCellValue(record.getR44_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR44_retail_banking()  != null) {
						cell6.setCellValue(record.getR44_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR44_commercial_banking() != null) {
						cell7.setCellValue(record.getR44_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR44_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR44_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR44_agency_services() != null) {
						cell9.setCellValue(record.getR44_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR44_asset_management() != null) {
						cell10.setCellValue(record.getR44_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR44_retail_brokerage() != null) {
						cell11.setCellValue(record.getR44_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row45
					row = sheet.getRow(44);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR45_corporate_finance() != null) {
						cell4.setCellValue(record.getR45_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR45_trading_and_sales() != null) {
						cell5.setCellValue(record.getR45_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR45_retail_banking()  != null) {
						cell6.setCellValue(record.getR45_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR45_commercial_banking() != null) {
						cell7.setCellValue(record.getR45_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR45_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR45_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR45_agency_services() != null) {
						cell9.setCellValue(record.getR45_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR45_asset_management() != null) {
						cell10.setCellValue(record.getR45_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR45_retail_brokerage() != null) {
						cell11.setCellValue(record.getR45_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row46
					row = sheet.getRow(45);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR46_corporate_finance() != null) {
						cell4.setCellValue(record.getR46_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR46_trading_and_sales() != null) {
						cell5.setCellValue(record.getR46_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR46_retail_banking()  != null) {
						cell6.setCellValue(record.getR46_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR46_commercial_banking() != null) {
						cell7.setCellValue(record.getR46_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR46_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR46_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR46_agency_services() != null) {
						cell9.setCellValue(record.getR46_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR46_asset_management() != null) {
						cell10.setCellValue(record.getR46_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR46_retail_brokerage() != null) {
						cell11.setCellValue(record.getR46_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row47
					row = sheet.getRow(46);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR47_corporate_finance() != null) {
						cell4.setCellValue(record.getR47_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR47_trading_and_sales() != null) {
						cell5.setCellValue(record.getR47_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR47_retail_banking()  != null) {
						cell6.setCellValue(record.getR47_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR47_commercial_banking() != null) {
						cell7.setCellValue(record.getR47_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR47_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR47_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR47_agency_services() != null) {
						cell9.setCellValue(record.getR47_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR47_asset_management() != null) {
						cell10.setCellValue(record.getR47_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR47_retail_brokerage() != null) {
						cell11.setCellValue(record.getR47_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row48
					row = sheet.getRow(47);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR48_corporate_finance() != null) {
						cell4.setCellValue(record.getR48_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR48_trading_and_sales() != null) {
						cell5.setCellValue(record.getR48_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR48_retail_banking()  != null) {
						cell6.setCellValue(record.getR48_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR48_commercial_banking() != null) {
						cell7.setCellValue(record.getR48_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR48_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR48_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR48_agency_services() != null) {
						cell9.setCellValue(record.getR48_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR48_asset_management() != null) {
						cell10.setCellValue(record.getR48_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR48_retail_brokerage() != null) {
						cell11.setCellValue(record.getR48_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row49
					row = sheet.getRow(48);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR49_corporate_finance() != null) {
						cell4.setCellValue(record.getR49_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR49_trading_and_sales() != null) {
						cell5.setCellValue(record.getR49_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR49_retail_banking()  != null) {
						cell6.setCellValue(record.getR49_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR49_commercial_banking() != null) {
						cell7.setCellValue(record.getR49_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR49_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR49_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR49_agency_services() != null) {
						cell9.setCellValue(record.getR49_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR49_asset_management() != null) {
						cell10.setCellValue(record.getR49_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR49_retail_brokerage() != null) {
						cell11.setCellValue(record.getR49_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row50
					row = sheet.getRow(49);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR50_corporate_finance() != null) {
						cell4.setCellValue(record.getR50_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR50_trading_and_sales() != null) {
						cell5.setCellValue(record.getR50_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR50_retail_banking()  != null) {
						cell6.setCellValue(record.getR50_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR50_commercial_banking() != null) {
						cell7.setCellValue(record.getR50_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR50_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR50_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR50_agency_services() != null) {
						cell9.setCellValue(record.getR50_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR50_asset_management() != null) {
						cell10.setCellValue(record.getR50_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR50_retail_brokerage() != null) {
						cell11.setCellValue(record.getR50_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row51
					row = sheet.getRow(50);
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR51_corporate_finance() != null) {
						cell4.setCellValue(record.getR51_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR51_trading_and_sales() != null) {
						cell5.setCellValue(record.getR51_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR51_retail_banking()  != null) {
						cell6.setCellValue(record.getR51_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR51_commercial_banking() != null) {
						cell7.setCellValue(record.getR51_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR51_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR51_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR51_agency_services() != null) {
						cell9.setCellValue(record.getR51_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR51_asset_management() != null) {
						cell10.setCellValue(record.getR51_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR51_retail_brokerage() != null) {
						cell11.setCellValue(record.getR51_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row52
					row = sheet.getRow(51);
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR52_corporate_finance() != null) {
						cell4.setCellValue(record.getR52_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR52_trading_and_sales() != null) {
						cell5.setCellValue(record.getR52_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR52_retail_banking()  != null) {
						cell6.setCellValue(record.getR52_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR52_commercial_banking() != null) {
						cell7.setCellValue(record.getR52_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR52_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR52_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR52_agency_services() != null) {
						cell9.setCellValue(record.getR52_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR52_asset_management() != null) {
						cell10.setCellValue(record.getR52_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR52_retail_brokerage() != null) {
						cell11.setCellValue(record.getR52_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row53
					row = sheet.getRow(52);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR53_corporate_finance() != null) {
						cell4.setCellValue(record.getR53_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR53_trading_and_sales() != null) {
						cell5.setCellValue(record.getR53_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR53_retail_banking()  != null) {
						cell6.setCellValue(record.getR53_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR53_commercial_banking() != null) {
						cell7.setCellValue(record.getR53_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR53_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR53_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR53_agency_services() != null) {
						cell9.setCellValue(record.getR53_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR53_asset_management() != null) {
						cell10.setCellValue(record.getR53_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR53_retail_brokerage() != null) {
						cell11.setCellValue(record.getR53_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row54
					row = sheet.getRow(53);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR54_corporate_finance() != null) {
						cell4.setCellValue(record.getR54_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR54_trading_and_sales() != null) {
						cell5.setCellValue(record.getR54_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR54_retail_banking()  != null) {
						cell6.setCellValue(record.getR54_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR54_commercial_banking() != null) {
						cell7.setCellValue(record.getR54_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR54_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR54_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR54_agency_services() != null) {
						cell9.setCellValue(record.getR54_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR54_asset_management() != null) {
						cell10.setCellValue(record.getR54_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR54_retail_brokerage() != null) {
						cell11.setCellValue(record.getR54_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row55
					row = sheet.getRow(54);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR55_corporate_finance() != null) {
						cell4.setCellValue(record.getR55_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR55_trading_and_sales() != null) {
						cell5.setCellValue(record.getR55_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR55_retail_banking()  != null) {
						cell6.setCellValue(record.getR55_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR55_commercial_banking() != null) {
						cell7.setCellValue(record.getR55_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR55_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR55_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR55_agency_services() != null) {
						cell9.setCellValue(record.getR55_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR55_asset_management() != null) {
						cell10.setCellValue(record.getR55_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR55_retail_brokerage() != null) {
						cell11.setCellValue(record.getR55_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
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
	
	public byte[] getExcelM_OR2ARCHIVAL(String filename, String reportId, String fromdate,
			String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if ("ARCHIVAL".equals(type) && version != null) {
		}
			List<M_OR2_Archival_Summary_Entity> dataList1 = M_OR2_Archival_Summary_Repo
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
			
			int startRow = 11;
			
			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					M_OR2_Archival_Summary_Entity record = dataList1.get(i);  
					System.out.println("rownumber="+startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

		
					//row12
					// Column D 
					Cell cell4 = row.createCell(3);
					if (record.getR12_corporate_finance() != null) {
						cell4.setCellValue(record.getR12_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row12
					// Column E 
					Cell cell5 = row.createCell(4);
					if (record.getR12_trading_and_sales() != null) {
						cell5.setCellValue(record.getR12_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					} 
					
					
					//row12
					// Column F
					Cell cell6 = row.createCell(5);
					if (record.getR12_retail_banking() != null) {
						cell6.setCellValue(record.getR12_retail_banking().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					//row12
					// Column G
					Cell cell7 = row.createCell(6);
					if (record.getR12_commercial_banking()  != null) {
						cell7.setCellValue(record.getR12_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					//row12
					// Column H
					Cell cell8 = row.createCell(7);
					if (record.getR12_payments_and_settlements()  != null) {
						cell8.setCellValue(record.getR12_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					//row12
					// Column I
					Cell cell9 = row.createCell(8);
					if (record.getR12_agency_services() != null) {
						cell9.setCellValue(record.getR12_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					//row12
					// Column J
					Cell cell10 = row.createCell(9);
					if (record.getR12_asset_management()  != null) {
						cell10.setCellValue(record.getR12_asset_management() .doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					//row12
					// Column K
					Cell cell11 = row.createCell(10);
					if (record.getR12_retail_brokerage() != null) {
						cell11.setCellValue(record.getR12_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row13
					row = sheet.getRow(12);
					
					// Column D
					 cell4 = row.createCell(3);
					if (record.getR13_corporate_finance() != null) {
						cell4.setCellValue(record.getR13_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR13_trading_and_sales() != null) {
						cell5.setCellValue(record.getR13_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR13_retail_banking()  != null) {
						cell6.setCellValue(record.getR13_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR13_commercial_banking() != null) {
						cell7.setCellValue(record.getR13_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR13_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR13_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR13_agency_services() != null) {
						cell9.setCellValue(record.getR13_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR13_asset_management() != null) {
						cell10.setCellValue(record.getR13_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR13_retail_brokerage() != null) {
						cell11.setCellValue(record.getR13_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					
					
					//row14
					row = sheet.getRow(13);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR14_corporate_finance() != null) {
						cell4.setCellValue(record.getR14_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR14_trading_and_sales() != null) {
						cell5.setCellValue(record.getR14_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR14_retail_banking()  != null) {
						cell6.setCellValue(record.getR14_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR14_commercial_banking() != null) {
						cell7.setCellValue(record.getR14_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR14_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR14_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR14_agency_services() != null) {
						cell9.setCellValue(record.getR14_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR14_asset_management() != null) {
						cell10.setCellValue(record.getR14_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR14_retail_brokerage() != null) {
						cell11.setCellValue(record.getR14_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row15
					row = sheet.getRow(14);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR15_corporate_finance() != null) {
						cell4.setCellValue(record.getR15_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR15_trading_and_sales() != null) {
						cell5.setCellValue(record.getR15_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR15_retail_banking()  != null) {
						cell6.setCellValue(record.getR15_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR15_commercial_banking() != null) {
						cell7.setCellValue(record.getR15_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR15_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR15_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR15_agency_services() != null) {
						cell9.setCellValue(record.getR15_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR15_asset_management() != null) {
						cell10.setCellValue(record.getR15_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR15_retail_brokerage() != null) {
						cell11.setCellValue(record.getR15_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row16
					row = sheet.getRow(15);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR16_corporate_finance() != null) {
						cell4.setCellValue(record.getR16_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR16_trading_and_sales() != null) {
						cell5.setCellValue(record.getR16_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR16_retail_banking()  != null) {
						cell6.setCellValue(record.getR16_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR16_commercial_banking() != null) {
						cell7.setCellValue(record.getR16_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR16_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR16_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR16_agency_services() != null) {
						cell9.setCellValue(record.getR16_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR16_asset_management() != null) {
						cell10.setCellValue(record.getR16_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR16_retail_brokerage() != null) {
						cell11.setCellValue(record.getR16_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row17
					row = sheet.getRow(16);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR17_corporate_finance() != null) {
						cell4.setCellValue(record.getR17_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR17_trading_and_sales() != null) {
						cell5.setCellValue(record.getR17_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR17_retail_banking()  != null) {
						cell6.setCellValue(record.getR17_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR17_commercial_banking() != null) {
						cell7.setCellValue(record.getR17_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR17_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR17_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR17_agency_services() != null) {
						cell9.setCellValue(record.getR17_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR17_asset_management() != null) {
						cell10.setCellValue(record.getR17_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR17_retail_brokerage() != null) {
						cell11.setCellValue(record.getR17_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row18
					row = sheet.getRow(17);
					
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR18_corporate_finance() != null) {
						cell4.setCellValue(record.getR18_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR18_trading_and_sales() != null) {
						cell5.setCellValue(record.getR18_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR18_retail_banking()  != null) {
						cell6.setCellValue(record.getR18_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR18_commercial_banking() != null) {
						cell7.setCellValue(record.getR18_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR18_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR18_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR18_agency_services() != null) {
						cell9.setCellValue(record.getR18_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR18_asset_management() != null) {
						cell10.setCellValue(record.getR18_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR18_retail_brokerage() != null) {
						cell11.setCellValue(record.getR18_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row19
					row = sheet.getRow(18);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR19_corporate_finance() != null) {
						cell4.setCellValue(record.getR19_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR19_trading_and_sales() != null) {
						cell5.setCellValue(record.getR19_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR19_retail_banking()  != null) {
						cell6.setCellValue(record.getR19_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR19_commercial_banking() != null) {
						cell7.setCellValue(record.getR19_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR19_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR19_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR19_agency_services() != null) {
						cell9.setCellValue(record.getR19_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR19_asset_management() != null) {
						cell10.setCellValue(record.getR19_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR19_retail_brokerage() != null) {
						cell11.setCellValue(record.getR19_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row20
					row = sheet.getRow(19);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR20_corporate_finance() != null) {
						cell4.setCellValue(record.getR20_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR20_trading_and_sales() != null) {
						cell5.setCellValue(record.getR20_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR20_retail_banking()  != null) {
						cell6.setCellValue(record.getR20_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR20_commercial_banking() != null) {
						cell7.setCellValue(record.getR20_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR20_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR20_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR20_agency_services() != null) {
						cell9.setCellValue(record.getR20_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR20_asset_management() != null) {
						cell10.setCellValue(record.getR20_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR20_retail_brokerage() != null) {
						cell11.setCellValue(record.getR20_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row21
					row = sheet.getRow(20);
					
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR21_corporate_finance() != null) {
						cell4.setCellValue(record.getR21_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR21_trading_and_sales() != null) {
						cell5.setCellValue(record.getR21_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR21_retail_banking()  != null) {
						cell6.setCellValue(record.getR21_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR21_commercial_banking() != null) {
						cell7.setCellValue(record.getR21_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR21_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR21_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR21_agency_services() != null) {
						cell9.setCellValue(record.getR21_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR21_asset_management() != null) {
						cell10.setCellValue(record.getR21_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR21_retail_brokerage() != null) {
						cell11.setCellValue(record.getR21_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row22
					row = sheet.getRow(21);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR22_corporate_finance() != null) {
						cell4.setCellValue(record.getR22_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR22_trading_and_sales() != null) {
						cell5.setCellValue(record.getR22_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR22_retail_banking()  != null) {
						cell6.setCellValue(record.getR22_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR22_commercial_banking() != null) {
						cell7.setCellValue(record.getR22_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR22_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR22_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR22_agency_services() != null) {
						cell9.setCellValue(record.getR22_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR22_asset_management() != null) {
						cell10.setCellValue(record.getR22_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR22_retail_brokerage() != null) {
						cell11.setCellValue(record.getR22_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row23
					row = sheet.getRow(22);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR23_corporate_finance() != null) {
						cell4.setCellValue(record.getR23_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR23_trading_and_sales() != null) {
						cell5.setCellValue(record.getR23_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR23_retail_banking()  != null) {
						cell6.setCellValue(record.getR23_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR23_commercial_banking() != null) {
						cell7.setCellValue(record.getR23_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR23_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR23_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR23_agency_services() != null) {
						cell9.setCellValue(record.getR23_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR23_asset_management() != null) {
						cell10.setCellValue(record.getR23_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR23_retail_brokerage() != null) {
						cell11.setCellValue(record.getR23_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row28
					row = sheet.getRow(27);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR28_corporate_finance() != null) {
						cell4.setCellValue(record.getR28_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR28_trading_and_sales() != null) {
						cell5.setCellValue(record.getR28_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR28_retail_banking()  != null) {
						cell6.setCellValue(record.getR28_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR28_commercial_banking() != null) {
						cell7.setCellValue(record.getR28_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR28_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR28_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR28_agency_services() != null) {
						cell9.setCellValue(record.getR28_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR28_asset_management() != null) {
						cell10.setCellValue(record.getR28_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR28_retail_brokerage() != null) {
						cell11.setCellValue(record.getR28_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row29
					row = sheet.getRow(28);
					
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR29_corporate_finance() != null) {
						cell4.setCellValue(record.getR29_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR29_trading_and_sales() != null) {
						cell5.setCellValue(record.getR29_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR29_retail_banking()  != null) {
						cell6.setCellValue(record.getR29_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR29_commercial_banking() != null) {
						cell7.setCellValue(record.getR29_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR29_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR29_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR29_agency_services() != null) {
						cell9.setCellValue(record.getR29_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR29_asset_management() != null) {
						cell10.setCellValue(record.getR29_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR29_retail_brokerage() != null) {
						cell11.setCellValue(record.getR29_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row30
					row = sheet.getRow(29);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR30_corporate_finance() != null) {
						cell4.setCellValue(record.getR30_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR30_trading_and_sales() != null) {
						cell5.setCellValue(record.getR30_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR30_retail_banking()  != null) {
						cell6.setCellValue(record.getR30_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR30_commercial_banking() != null) {
						cell7.setCellValue(record.getR30_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR30_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR30_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR30_agency_services() != null) {
						cell9.setCellValue(record.getR30_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR30_asset_management() != null) {
						cell10.setCellValue(record.getR30_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR30_retail_brokerage() != null) {
						cell11.setCellValue(record.getR30_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row31
					row = sheet.getRow(30);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR31_corporate_finance() != null) {
						cell4.setCellValue(record.getR31_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR31_trading_and_sales() != null) {
						cell5.setCellValue(record.getR31_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR31_retail_banking()  != null) {
						cell6.setCellValue(record.getR31_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR31_commercial_banking() != null) {
						cell7.setCellValue(record.getR31_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR31_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR31_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR31_agency_services() != null) {
						cell9.setCellValue(record.getR31_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR31_asset_management() != null) {
						cell10.setCellValue(record.getR31_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR31_retail_brokerage() != null) {
						cell11.setCellValue(record.getR31_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row32
					row = sheet.getRow(31);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR32_corporate_finance() != null) {
						cell4.setCellValue(record.getR32_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR32_trading_and_sales() != null) {
						cell5.setCellValue(record.getR32_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR32_retail_banking()  != null) {
						cell6.setCellValue(record.getR32_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR32_commercial_banking() != null) {
						cell7.setCellValue(record.getR32_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR32_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR32_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR32_agency_services() != null) {
						cell9.setCellValue(record.getR32_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR32_asset_management() != null) {
						cell10.setCellValue(record.getR32_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR32_retail_brokerage() != null) {
						cell11.setCellValue(record.getR32_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row33
					row = sheet.getRow(32);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR33_corporate_finance() != null) {
						cell4.setCellValue(record.getR33_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR33_trading_and_sales() != null) {
						cell5.setCellValue(record.getR33_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR33_retail_banking()  != null) {
						cell6.setCellValue(record.getR33_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR33_commercial_banking() != null) {
						cell7.setCellValue(record.getR33_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR33_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR33_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR33_agency_services() != null) {
						cell9.setCellValue(record.getR33_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR33_asset_management() != null) {
						cell10.setCellValue(record.getR33_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR33_retail_brokerage() != null) {
						cell11.setCellValue(record.getR33_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row34
					row = sheet.getRow(33);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR34_corporate_finance() != null) {
						cell4.setCellValue(record.getR34_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR34_trading_and_sales() != null) {
						cell5.setCellValue(record.getR34_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR34_retail_banking()  != null) {
						cell6.setCellValue(record.getR34_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR34_commercial_banking() != null) {
						cell7.setCellValue(record.getR34_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR34_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR34_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR34_agency_services() != null) {
						cell9.setCellValue(record.getR34_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR34_asset_management() != null) {
						cell10.setCellValue(record.getR34_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR34_retail_brokerage() != null) {
						cell11.setCellValue(record.getR34_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row35
					row = sheet.getRow(34);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR35_corporate_finance() != null) {
						cell4.setCellValue(record.getR35_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR35_trading_and_sales() != null) {
						cell5.setCellValue(record.getR35_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR35_retail_banking()  != null) {
						cell6.setCellValue(record.getR35_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR35_commercial_banking() != null) {
						cell7.setCellValue(record.getR35_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR35_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR35_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR35_agency_services() != null) {
						cell9.setCellValue(record.getR35_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR35_asset_management() != null) {
						cell10.setCellValue(record.getR35_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR35_retail_brokerage() != null) {
						cell11.setCellValue(record.getR35_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row 36
					row = sheet.getRow(35);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR36_corporate_finance() != null) {
						cell4.setCellValue(record.getR36_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR36_trading_and_sales() != null) {
						cell5.setCellValue(record.getR36_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR36_retail_banking()  != null) {
						cell6.setCellValue(record.getR36_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR36_commercial_banking() != null) {
						cell7.setCellValue(record.getR36_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR36_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR36_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR36_agency_services() != null) {
						cell9.setCellValue(record.getR36_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR36_asset_management() != null) {
						cell10.setCellValue(record.getR36_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR36_retail_brokerage() != null) {
						cell11.setCellValue(record.getR36_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row37
					row = sheet.getRow(36);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR37_corporate_finance() != null) {
						cell4.setCellValue(record.getR37_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR37_trading_and_sales() != null) {
						cell5.setCellValue(record.getR37_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR37_retail_banking()  != null) {
						cell6.setCellValue(record.getR37_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR37_commercial_banking() != null) {
						cell7.setCellValue(record.getR37_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR37_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR37_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR37_agency_services() != null) {
						cell9.setCellValue(record.getR37_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR37_asset_management() != null) {
						cell10.setCellValue(record.getR37_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR37_retail_brokerage() != null) {
						cell11.setCellValue(record.getR37_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row38
					row = sheet.getRow(37);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR38_corporate_finance() != null) {
						cell4.setCellValue(record.getR38_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR38_trading_and_sales() != null) {
						cell5.setCellValue(record.getR38_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR38_retail_banking()  != null) {
						cell6.setCellValue(record.getR38_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR38_commercial_banking() != null) {
						cell7.setCellValue(record.getR38_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR38_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR38_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR38_agency_services() != null) {
						cell9.setCellValue(record.getR38_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR38_asset_management() != null) {
						cell10.setCellValue(record.getR38_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR38_retail_brokerage() != null) {
						cell11.setCellValue(record.getR38_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row 39
					row = sheet.getRow(38);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR39_corporate_finance() != null) {
						cell4.setCellValue(record.getR39_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR39_trading_and_sales() != null) {
						cell5.setCellValue(record.getR39_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR39_retail_banking()  != null) {
						cell6.setCellValue(record.getR39_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR39_commercial_banking() != null) {
						cell7.setCellValue(record.getR39_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR39_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR39_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR39_agency_services() != null) {
						cell9.setCellValue(record.getR39_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR39_asset_management() != null) {
						cell10.setCellValue(record.getR39_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR39_retail_brokerage() != null) {
						cell11.setCellValue(record.getR39_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row44
					row = sheet.getRow(43);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR44_corporate_finance() != null) {
						cell4.setCellValue(record.getR44_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR44_trading_and_sales() != null) {
						cell5.setCellValue(record.getR44_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR44_retail_banking()  != null) {
						cell6.setCellValue(record.getR44_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR44_commercial_banking() != null) {
						cell7.setCellValue(record.getR44_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR44_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR44_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR44_agency_services() != null) {
						cell9.setCellValue(record.getR44_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR44_asset_management() != null) {
						cell10.setCellValue(record.getR44_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR44_retail_brokerage() != null) {
						cell11.setCellValue(record.getR44_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row45
					row = sheet.getRow(44);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR45_corporate_finance() != null) {
						cell4.setCellValue(record.getR45_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR45_trading_and_sales() != null) {
						cell5.setCellValue(record.getR45_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR45_retail_banking()  != null) {
						cell6.setCellValue(record.getR45_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR45_commercial_banking() != null) {
						cell7.setCellValue(record.getR45_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR45_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR45_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR45_agency_services() != null) {
						cell9.setCellValue(record.getR45_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR45_asset_management() != null) {
						cell10.setCellValue(record.getR45_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR45_retail_brokerage() != null) {
						cell11.setCellValue(record.getR45_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row46
					row = sheet.getRow(45);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR46_corporate_finance() != null) {
						cell4.setCellValue(record.getR46_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR46_trading_and_sales() != null) {
						cell5.setCellValue(record.getR46_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR46_retail_banking()  != null) {
						cell6.setCellValue(record.getR46_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR46_commercial_banking() != null) {
						cell7.setCellValue(record.getR46_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR46_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR46_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR46_agency_services() != null) {
						cell9.setCellValue(record.getR46_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR46_asset_management() != null) {
						cell10.setCellValue(record.getR46_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR46_retail_brokerage() != null) {
						cell11.setCellValue(record.getR46_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row47
					row = sheet.getRow(46);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR47_corporate_finance() != null) {
						cell4.setCellValue(record.getR47_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR47_trading_and_sales() != null) {
						cell5.setCellValue(record.getR47_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR47_retail_banking()  != null) {
						cell6.setCellValue(record.getR47_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR47_commercial_banking() != null) {
						cell7.setCellValue(record.getR47_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR47_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR47_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR47_agency_services() != null) {
						cell9.setCellValue(record.getR47_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR47_asset_management() != null) {
						cell10.setCellValue(record.getR47_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR47_retail_brokerage() != null) {
						cell11.setCellValue(record.getR47_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row48
					row = sheet.getRow(47);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR48_corporate_finance() != null) {
						cell4.setCellValue(record.getR48_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR48_trading_and_sales() != null) {
						cell5.setCellValue(record.getR48_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR48_retail_banking()  != null) {
						cell6.setCellValue(record.getR48_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR48_commercial_banking() != null) {
						cell7.setCellValue(record.getR48_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR48_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR48_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR48_agency_services() != null) {
						cell9.setCellValue(record.getR48_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR48_asset_management() != null) {
						cell10.setCellValue(record.getR48_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR48_retail_brokerage() != null) {
						cell11.setCellValue(record.getR48_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					
					//row49
					row = sheet.getRow(48);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR49_corporate_finance() != null) {
						cell4.setCellValue(record.getR49_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR49_trading_and_sales() != null) {
						cell5.setCellValue(record.getR49_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR49_retail_banking()  != null) {
						cell6.setCellValue(record.getR49_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR49_commercial_banking() != null) {
						cell7.setCellValue(record.getR49_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR49_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR49_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR49_agency_services() != null) {
						cell9.setCellValue(record.getR49_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR49_asset_management() != null) {
						cell10.setCellValue(record.getR49_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR49_retail_brokerage() != null) {
						cell11.setCellValue(record.getR49_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row50
					row = sheet.getRow(49);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR50_corporate_finance() != null) {
						cell4.setCellValue(record.getR50_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR50_trading_and_sales() != null) {
						cell5.setCellValue(record.getR50_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR50_retail_banking()  != null) {
						cell6.setCellValue(record.getR50_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR50_commercial_banking() != null) {
						cell7.setCellValue(record.getR50_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR50_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR50_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR50_agency_services() != null) {
						cell9.setCellValue(record.getR50_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR50_asset_management() != null) {
						cell10.setCellValue(record.getR50_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR50_retail_brokerage() != null) {
						cell11.setCellValue(record.getR50_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row51
					row = sheet.getRow(50);
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR51_corporate_finance() != null) {
						cell4.setCellValue(record.getR51_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR51_trading_and_sales() != null) {
						cell5.setCellValue(record.getR51_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR51_retail_banking()  != null) {
						cell6.setCellValue(record.getR51_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR51_commercial_banking() != null) {
						cell7.setCellValue(record.getR51_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR51_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR51_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR51_agency_services() != null) {
						cell9.setCellValue(record.getR51_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR51_asset_management() != null) {
						cell10.setCellValue(record.getR51_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR51_retail_brokerage() != null) {
						cell11.setCellValue(record.getR51_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row52
					row = sheet.getRow(51);
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR52_corporate_finance() != null) {
						cell4.setCellValue(record.getR52_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR52_trading_and_sales() != null) {
						cell5.setCellValue(record.getR52_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR52_retail_banking()  != null) {
						cell6.setCellValue(record.getR52_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR52_commercial_banking() != null) {
						cell7.setCellValue(record.getR52_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR52_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR52_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR52_agency_services() != null) {
						cell9.setCellValue(record.getR52_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR52_asset_management() != null) {
						cell10.setCellValue(record.getR52_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR52_retail_brokerage() != null) {
						cell11.setCellValue(record.getR52_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row53
					row = sheet.getRow(52);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR53_corporate_finance() != null) {
						cell4.setCellValue(record.getR53_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR53_trading_and_sales() != null) {
						cell5.setCellValue(record.getR53_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR53_retail_banking()  != null) {
						cell6.setCellValue(record.getR53_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR53_commercial_banking() != null) {
						cell7.setCellValue(record.getR53_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR53_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR53_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR53_agency_services() != null) {
						cell9.setCellValue(record.getR53_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR53_asset_management() != null) {
						cell10.setCellValue(record.getR53_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR53_retail_brokerage() != null) {
						cell11.setCellValue(record.getR53_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row54
					row = sheet.getRow(53);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR54_corporate_finance() != null) {
						cell4.setCellValue(record.getR54_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR54_trading_and_sales() != null) {
						cell5.setCellValue(record.getR54_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR54_retail_banking()  != null) {
						cell6.setCellValue(record.getR54_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR54_commercial_banking() != null) {
						cell7.setCellValue(record.getR54_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR54_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR54_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR54_agency_services() != null) {
						cell9.setCellValue(record.getR54_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR54_asset_management() != null) {
						cell10.setCellValue(record.getR54_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR54_retail_brokerage() != null) {
						cell11.setCellValue(record.getR54_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row55
					row = sheet.getRow(54);
					
					 // Column D
					 cell4 = row.createCell(3);
					if (record.getR55_corporate_finance() != null) {
						cell4.setCellValue(record.getR55_corporate_finance().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					// Column E
					 cell5 = row.createCell(4);
					if (record.getR55_trading_and_sales() != null) {
						cell5.setCellValue(record.getR55_trading_and_sales().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					
					
					// Column F
					 cell6 = row.createCell(5);
					if (record.getR55_retail_banking()  != null) {
						cell6.setCellValue(record.getR55_retail_banking() .doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// Column G
					 cell7 = row.createCell(6);
					if (record.getR55_commercial_banking() != null) {
						cell7.setCellValue(record.getR55_commercial_banking().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
				
					// Column H
					 cell8 = row.createCell(7);
					if (record.getR55_payments_and_settlements() != null) {
						cell8.setCellValue(record.getR55_payments_and_settlements().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// Column I
					 cell9 = row.createCell(8);
					if (record.getR55_agency_services() != null) {
						cell9.setCellValue(record.getR55_agency_services().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					
					
					
					
					
					
				
					// Column J
					 cell10 = row.createCell(9);
					if (record.getR55_asset_management() != null) {
						cell10.setCellValue(record.getR55_asset_management().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
				
					// Column K
					 cell11 = row.createCell(10);
					if (record.getR55_retail_brokerage() != null) {
						cell11.setCellValue(record.getR55_retail_brokerage().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
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
	
/////////////////////////////////////////RESUBMISSION///////////////////////////////////////////////////////////////////	
/// Report Date | Report Version | Domain
/// RESUB VIEW
public List<Object[]> getM_OR2Resub() {
List<Object[]> resubList = new ArrayList<>();
try {
List<M_OR2_Archival_Summary_Entity> latestArchivalList = 
M_OR2_Archival_Summary_Repo.getdatabydateListWithVersionAll();

if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
for (M_OR2_Archival_Summary_Entity entity : latestArchivalList) {
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
public List<Object[]> getM_OR2Archival() {
List<Object[]> archivalList = new ArrayList<>();

try {
List<M_OR2_Archival_Summary_Entity> repoData = M_OR2_Archival_Summary_Repo
.getdatabydateListWithVersionAll();

if (repoData != null && !repoData.isEmpty()) {
for (M_OR2_Archival_Summary_Entity entity : repoData) {
Object[] row = new Object[] {
entity.getReportDate(), 
entity.getReportVersion() 
};
archivalList.add(row);
}

System.out.println("Fetched " + archivalList.size() + " archival records");
M_OR2_Archival_Summary_Entity first = repoData.get(0);
System.out.println("Latest archival version: " + first.getReportVersion());
} else {
System.out.println("No archival data found.");
}

} catch (Exception e) {
System.err.println("Error fetching M_OR2 Archival data: " + e.getMessage());
e.printStackTrace();
}

return archivalList;
}


//Resubmit the values , latest version and Resub Date
public void updateReportReSub(M_OR2_Summary_Entity updatedEntity) {
System.out.println("Came to Resub Service");
System.out.println("Report Date: " + updatedEntity.getReportDate());

Date reportDate = updatedEntity.getReportDate();
int newVersion = 1;

try {
//Fetch the latest archival version for this report date
Optional<M_OR2_Archival_Summary_Entity> latestArchivalOpt = M_OR2_Archival_Summary_Repo
.getLatestArchivalVersionByDate(reportDate);

//Determine next version number
if (latestArchivalOpt.isPresent()) {
M_OR2_Archival_Summary_Entity latestArchival = latestArchivalOpt.get();
try {
newVersion = Integer.parseInt(latestArchival.getReportVersion()) + 1;
} catch (NumberFormatException e) {
System.err.println("Invalid version format. Defaulting to version 1");
newVersion = 1;
}
} else {
System.out.println("No previous archival found for date: " + reportDate);
}

//Prevent duplicate version number
boolean exists = M_OR2_Archival_Summary_Repo
.findByReportDateAndReportVersion(reportDate, String.valueOf(newVersion))
.isPresent();

if (exists) {
throw new RuntimeException("Version " + newVersion + " already exists for report date " + reportDate);
}

//Copy summary entity to archival entity
M_OR2_Archival_Summary_Entity archivalEntity = new M_OR2_Archival_Summary_Entity();
org.springframework.beans.BeanUtils.copyProperties(updatedEntity, archivalEntity);

archivalEntity.setReportDate(reportDate);
archivalEntity.setReportVersion(String.valueOf(newVersion));
archivalEntity.setReportResubDate(new Date());

System.out.println("Saving new archival version: " + newVersion);

//Save new version to repository
M_OR2_Archival_Summary_Repo.save(archivalEntity);

System.out.println(" Saved archival version successfully: " + newVersion);

} catch (Exception e) {
e.printStackTrace();
throw new RuntimeException("Error while creating archival resubmission record", e);
}
}

/// Downloaded for Archival & Resub
public byte[] BRRS_M_OR2ResubExcel(String filename, String reportId, String fromdate,
String todate, String currency, String dtltype,
String type, String version) throws Exception {

logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

if (type.equals("RESUB") & version != null) {

}

List<M_OR2_Archival_Summary_Entity> dataList1 =
M_OR2_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

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
//This specific exception will be caught by the controller.
throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
}
if (!Files.isReadable(templatePath)) {
//A specific exception for permission errors.
throw new SecurityException(
"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
}

//This try-with-resources block is perfect. It guarantees all resources are
//closed automatically.
try (InputStream templateInputStream = Files.newInputStream(templatePath);
Workbook workbook = WorkbookFactory.create(templateInputStream);
ByteArrayOutputStream out = new ByteArrayOutputStream()) {

Sheet sheet = workbook.getSheetAt(0);

//--- Style Definitions ---
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

//Create the font
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
//--- End of Style Definitions ---

int startRow = 11;

if (!dataList1.isEmpty()) {
	for (int i = 0; i < dataList1.size(); i++) {
		M_OR2_Archival_Summary_Entity record = dataList1.get(i);  
		System.out.println("rownumber="+startRow + i);
		Row row = sheet.getRow(startRow + i);
		if (row == null) {
			row = sheet.createRow(startRow + i);
		}


		//row12
		// Column D 
		Cell cell4 = row.createCell(3);
		if (record.getR12_corporate_finance() != null) {
			cell4.setCellValue(record.getR12_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		//row12
		// Column E 
		Cell cell5 = row.createCell(4);
		if (record.getR12_trading_and_sales() != null) {
			cell5.setCellValue(record.getR12_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		} 
		
		
		//row12
		// Column F
		Cell cell6 = row.createCell(5);
		if (record.getR12_retail_banking() != null) {
			cell6.setCellValue(record.getR12_retail_banking().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		//row12
		// Column G
		Cell cell7 = row.createCell(6);
		if (record.getR12_commercial_banking()  != null) {
			cell7.setCellValue(record.getR12_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
		//row12
		// Column H
		Cell cell8 = row.createCell(7);
		if (record.getR12_payments_and_settlements()  != null) {
			cell8.setCellValue(record.getR12_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		//row12
		// Column I
		Cell cell9 = row.createCell(8);
		if (record.getR12_agency_services() != null) {
			cell9.setCellValue(record.getR12_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		//row12
		// Column J
		Cell cell10 = row.createCell(9);
		if (record.getR12_asset_management()  != null) {
			cell10.setCellValue(record.getR12_asset_management() .doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
		//row12
		// Column K
		Cell cell11 = row.createCell(10);
		if (record.getR12_retail_brokerage() != null) {
			cell11.setCellValue(record.getR12_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		
		//row13
		row = sheet.getRow(12);
		
		// Column D
		 cell4 = row.createCell(3);
		if (record.getR13_corporate_finance() != null) {
			cell4.setCellValue(record.getR13_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR13_trading_and_sales() != null) {
			cell5.setCellValue(record.getR13_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR13_retail_banking()  != null) {
			cell6.setCellValue(record.getR13_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR13_commercial_banking() != null) {
			cell7.setCellValue(record.getR13_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR13_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR13_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR13_agency_services() != null) {
			cell9.setCellValue(record.getR13_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR13_asset_management() != null) {
			cell10.setCellValue(record.getR13_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR13_retail_brokerage() != null) {
			cell11.setCellValue(record.getR13_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		
		
		
		//row14
		row = sheet.getRow(13);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR14_corporate_finance() != null) {
			cell4.setCellValue(record.getR14_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR14_trading_and_sales() != null) {
			cell5.setCellValue(record.getR14_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR14_retail_banking()  != null) {
			cell6.setCellValue(record.getR14_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR14_commercial_banking() != null) {
			cell7.setCellValue(record.getR14_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR14_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR14_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR14_agency_services() != null) {
			cell9.setCellValue(record.getR14_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR14_asset_management() != null) {
			cell10.setCellValue(record.getR14_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR14_retail_brokerage() != null) {
			cell11.setCellValue(record.getR14_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		//row15
		row = sheet.getRow(14);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR15_corporate_finance() != null) {
			cell4.setCellValue(record.getR15_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR15_trading_and_sales() != null) {
			cell5.setCellValue(record.getR15_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR15_retail_banking()  != null) {
			cell6.setCellValue(record.getR15_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR15_commercial_banking() != null) {
			cell7.setCellValue(record.getR15_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR15_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR15_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR15_agency_services() != null) {
			cell9.setCellValue(record.getR15_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR15_asset_management() != null) {
			cell10.setCellValue(record.getR15_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR15_retail_brokerage() != null) {
			cell11.setCellValue(record.getR15_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		//row16
		row = sheet.getRow(15);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR16_corporate_finance() != null) {
			cell4.setCellValue(record.getR16_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR16_trading_and_sales() != null) {
			cell5.setCellValue(record.getR16_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR16_retail_banking()  != null) {
			cell6.setCellValue(record.getR16_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR16_commercial_banking() != null) {
			cell7.setCellValue(record.getR16_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR16_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR16_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR16_agency_services() != null) {
			cell9.setCellValue(record.getR16_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR16_asset_management() != null) {
			cell10.setCellValue(record.getR16_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR16_retail_brokerage() != null) {
			cell11.setCellValue(record.getR16_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		//row17
		row = sheet.getRow(16);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR17_corporate_finance() != null) {
			cell4.setCellValue(record.getR17_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR17_trading_and_sales() != null) {
			cell5.setCellValue(record.getR17_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR17_retail_banking()  != null) {
			cell6.setCellValue(record.getR17_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR17_commercial_banking() != null) {
			cell7.setCellValue(record.getR17_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR17_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR17_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR17_agency_services() != null) {
			cell9.setCellValue(record.getR17_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR17_asset_management() != null) {
			cell10.setCellValue(record.getR17_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR17_retail_brokerage() != null) {
			cell11.setCellValue(record.getR17_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		
		//row18
		row = sheet.getRow(17);
		
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR18_corporate_finance() != null) {
			cell4.setCellValue(record.getR18_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR18_trading_and_sales() != null) {
			cell5.setCellValue(record.getR18_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR18_retail_banking()  != null) {
			cell6.setCellValue(record.getR18_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR18_commercial_banking() != null) {
			cell7.setCellValue(record.getR18_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR18_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR18_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR18_agency_services() != null) {
			cell9.setCellValue(record.getR18_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR18_asset_management() != null) {
			cell10.setCellValue(record.getR18_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR18_retail_brokerage() != null) {
			cell11.setCellValue(record.getR18_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		//row19
		row = sheet.getRow(18);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR19_corporate_finance() != null) {
			cell4.setCellValue(record.getR19_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR19_trading_and_sales() != null) {
			cell5.setCellValue(record.getR19_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR19_retail_banking()  != null) {
			cell6.setCellValue(record.getR19_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR19_commercial_banking() != null) {
			cell7.setCellValue(record.getR19_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR19_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR19_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR19_agency_services() != null) {
			cell9.setCellValue(record.getR19_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR19_asset_management() != null) {
			cell10.setCellValue(record.getR19_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR19_retail_brokerage() != null) {
			cell11.setCellValue(record.getR19_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		//row20
		row = sheet.getRow(19);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR20_corporate_finance() != null) {
			cell4.setCellValue(record.getR20_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR20_trading_and_sales() != null) {
			cell5.setCellValue(record.getR20_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR20_retail_banking()  != null) {
			cell6.setCellValue(record.getR20_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR20_commercial_banking() != null) {
			cell7.setCellValue(record.getR20_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR20_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR20_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR20_agency_services() != null) {
			cell9.setCellValue(record.getR20_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR20_asset_management() != null) {
			cell10.setCellValue(record.getR20_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR20_retail_brokerage() != null) {
			cell11.setCellValue(record.getR20_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		
		//row21
		row = sheet.getRow(20);
		
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR21_corporate_finance() != null) {
			cell4.setCellValue(record.getR21_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR21_trading_and_sales() != null) {
			cell5.setCellValue(record.getR21_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR21_retail_banking()  != null) {
			cell6.setCellValue(record.getR21_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR21_commercial_banking() != null) {
			cell7.setCellValue(record.getR21_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR21_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR21_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR21_agency_services() != null) {
			cell9.setCellValue(record.getR21_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR21_asset_management() != null) {
			cell10.setCellValue(record.getR21_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR21_retail_brokerage() != null) {
			cell11.setCellValue(record.getR21_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		
		//row22
		row = sheet.getRow(21);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR22_corporate_finance() != null) {
			cell4.setCellValue(record.getR22_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR22_trading_and_sales() != null) {
			cell5.setCellValue(record.getR22_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR22_retail_banking()  != null) {
			cell6.setCellValue(record.getR22_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR22_commercial_banking() != null) {
			cell7.setCellValue(record.getR22_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR22_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR22_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR22_agency_services() != null) {
			cell9.setCellValue(record.getR22_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR22_asset_management() != null) {
			cell10.setCellValue(record.getR22_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR22_retail_brokerage() != null) {
			cell11.setCellValue(record.getR22_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		//row23
		row = sheet.getRow(22);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR23_corporate_finance() != null) {
			cell4.setCellValue(record.getR23_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR23_trading_and_sales() != null) {
			cell5.setCellValue(record.getR23_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR23_retail_banking()  != null) {
			cell6.setCellValue(record.getR23_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR23_commercial_banking() != null) {
			cell7.setCellValue(record.getR23_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR23_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR23_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR23_agency_services() != null) {
			cell9.setCellValue(record.getR23_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR23_asset_management() != null) {
			cell10.setCellValue(record.getR23_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR23_retail_brokerage() != null) {
			cell11.setCellValue(record.getR23_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		
		//row28
		row = sheet.getRow(27);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR28_corporate_finance() != null) {
			cell4.setCellValue(record.getR28_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR28_trading_and_sales() != null) {
			cell5.setCellValue(record.getR28_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR28_retail_banking()  != null) {
			cell6.setCellValue(record.getR28_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR28_commercial_banking() != null) {
			cell7.setCellValue(record.getR28_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR28_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR28_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR28_agency_services() != null) {
			cell9.setCellValue(record.getR28_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR28_asset_management() != null) {
			cell10.setCellValue(record.getR28_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR28_retail_brokerage() != null) {
			cell11.setCellValue(record.getR28_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		
		//row29
		row = sheet.getRow(28);
		
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR29_corporate_finance() != null) {
			cell4.setCellValue(record.getR29_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR29_trading_and_sales() != null) {
			cell5.setCellValue(record.getR29_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR29_retail_banking()  != null) {
			cell6.setCellValue(record.getR29_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR29_commercial_banking() != null) {
			cell7.setCellValue(record.getR29_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR29_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR29_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR29_agency_services() != null) {
			cell9.setCellValue(record.getR29_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR29_asset_management() != null) {
			cell10.setCellValue(record.getR29_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR29_retail_brokerage() != null) {
			cell11.setCellValue(record.getR29_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		//row30
		row = sheet.getRow(29);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR30_corporate_finance() != null) {
			cell4.setCellValue(record.getR30_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR30_trading_and_sales() != null) {
			cell5.setCellValue(record.getR30_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR30_retail_banking()  != null) {
			cell6.setCellValue(record.getR30_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR30_commercial_banking() != null) {
			cell7.setCellValue(record.getR30_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR30_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR30_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR30_agency_services() != null) {
			cell9.setCellValue(record.getR30_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR30_asset_management() != null) {
			cell10.setCellValue(record.getR30_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR30_retail_brokerage() != null) {
			cell11.setCellValue(record.getR30_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		//row31
		row = sheet.getRow(30);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR31_corporate_finance() != null) {
			cell4.setCellValue(record.getR31_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR31_trading_and_sales() != null) {
			cell5.setCellValue(record.getR31_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR31_retail_banking()  != null) {
			cell6.setCellValue(record.getR31_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR31_commercial_banking() != null) {
			cell7.setCellValue(record.getR31_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR31_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR31_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR31_agency_services() != null) {
			cell9.setCellValue(record.getR31_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR31_asset_management() != null) {
			cell10.setCellValue(record.getR31_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR31_retail_brokerage() != null) {
			cell11.setCellValue(record.getR31_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		
		//row32
		row = sheet.getRow(31);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR32_corporate_finance() != null) {
			cell4.setCellValue(record.getR32_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR32_trading_and_sales() != null) {
			cell5.setCellValue(record.getR32_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR32_retail_banking()  != null) {
			cell6.setCellValue(record.getR32_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR32_commercial_banking() != null) {
			cell7.setCellValue(record.getR32_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR32_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR32_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR32_agency_services() != null) {
			cell9.setCellValue(record.getR32_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR32_asset_management() != null) {
			cell10.setCellValue(record.getR32_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR32_retail_brokerage() != null) {
			cell11.setCellValue(record.getR32_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		//row33
		row = sheet.getRow(32);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR33_corporate_finance() != null) {
			cell4.setCellValue(record.getR33_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR33_trading_and_sales() != null) {
			cell5.setCellValue(record.getR33_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR33_retail_banking()  != null) {
			cell6.setCellValue(record.getR33_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR33_commercial_banking() != null) {
			cell7.setCellValue(record.getR33_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR33_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR33_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR33_agency_services() != null) {
			cell9.setCellValue(record.getR33_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR33_asset_management() != null) {
			cell10.setCellValue(record.getR33_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR33_retail_brokerage() != null) {
			cell11.setCellValue(record.getR33_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		//row34
		row = sheet.getRow(33);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR34_corporate_finance() != null) {
			cell4.setCellValue(record.getR34_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR34_trading_and_sales() != null) {
			cell5.setCellValue(record.getR34_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR34_retail_banking()  != null) {
			cell6.setCellValue(record.getR34_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR34_commercial_banking() != null) {
			cell7.setCellValue(record.getR34_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR34_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR34_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR34_agency_services() != null) {
			cell9.setCellValue(record.getR34_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR34_asset_management() != null) {
			cell10.setCellValue(record.getR34_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR34_retail_brokerage() != null) {
			cell11.setCellValue(record.getR34_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		
		//row35
		row = sheet.getRow(34);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR35_corporate_finance() != null) {
			cell4.setCellValue(record.getR35_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR35_trading_and_sales() != null) {
			cell5.setCellValue(record.getR35_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR35_retail_banking()  != null) {
			cell6.setCellValue(record.getR35_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR35_commercial_banking() != null) {
			cell7.setCellValue(record.getR35_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR35_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR35_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR35_agency_services() != null) {
			cell9.setCellValue(record.getR35_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR35_asset_management() != null) {
			cell10.setCellValue(record.getR35_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR35_retail_brokerage() != null) {
			cell11.setCellValue(record.getR35_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		//row 36
		row = sheet.getRow(35);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR36_corporate_finance() != null) {
			cell4.setCellValue(record.getR36_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR36_trading_and_sales() != null) {
			cell5.setCellValue(record.getR36_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR36_retail_banking()  != null) {
			cell6.setCellValue(record.getR36_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR36_commercial_banking() != null) {
			cell7.setCellValue(record.getR36_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR36_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR36_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR36_agency_services() != null) {
			cell9.setCellValue(record.getR36_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR36_asset_management() != null) {
			cell10.setCellValue(record.getR36_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR36_retail_brokerage() != null) {
			cell11.setCellValue(record.getR36_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		
		//row37
		row = sheet.getRow(36);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR37_corporate_finance() != null) {
			cell4.setCellValue(record.getR37_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR37_trading_and_sales() != null) {
			cell5.setCellValue(record.getR37_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR37_retail_banking()  != null) {
			cell6.setCellValue(record.getR37_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR37_commercial_banking() != null) {
			cell7.setCellValue(record.getR37_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR37_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR37_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR37_agency_services() != null) {
			cell9.setCellValue(record.getR37_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR37_asset_management() != null) {
			cell10.setCellValue(record.getR37_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR37_retail_brokerage() != null) {
			cell11.setCellValue(record.getR37_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		
		//row38
		row = sheet.getRow(37);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR38_corporate_finance() != null) {
			cell4.setCellValue(record.getR38_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR38_trading_and_sales() != null) {
			cell5.setCellValue(record.getR38_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR38_retail_banking()  != null) {
			cell6.setCellValue(record.getR38_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR38_commercial_banking() != null) {
			cell7.setCellValue(record.getR38_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR38_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR38_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR38_agency_services() != null) {
			cell9.setCellValue(record.getR38_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR38_asset_management() != null) {
			cell10.setCellValue(record.getR38_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR38_retail_brokerage() != null) {
			cell11.setCellValue(record.getR38_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		
		//row 39
		row = sheet.getRow(38);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR39_corporate_finance() != null) {
			cell4.setCellValue(record.getR39_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR39_trading_and_sales() != null) {
			cell5.setCellValue(record.getR39_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR39_retail_banking()  != null) {
			cell6.setCellValue(record.getR39_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR39_commercial_banking() != null) {
			cell7.setCellValue(record.getR39_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR39_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR39_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR39_agency_services() != null) {
			cell9.setCellValue(record.getR39_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR39_asset_management() != null) {
			cell10.setCellValue(record.getR39_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR39_retail_brokerage() != null) {
			cell11.setCellValue(record.getR39_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		//row44
		row = sheet.getRow(43);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR44_corporate_finance() != null) {
			cell4.setCellValue(record.getR44_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR44_trading_and_sales() != null) {
			cell5.setCellValue(record.getR44_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR44_retail_banking()  != null) {
			cell6.setCellValue(record.getR44_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR44_commercial_banking() != null) {
			cell7.setCellValue(record.getR44_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR44_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR44_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR44_agency_services() != null) {
			cell9.setCellValue(record.getR44_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR44_asset_management() != null) {
			cell10.setCellValue(record.getR44_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR44_retail_brokerage() != null) {
			cell11.setCellValue(record.getR44_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		//row45
		row = sheet.getRow(44);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR45_corporate_finance() != null) {
			cell4.setCellValue(record.getR45_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR45_trading_and_sales() != null) {
			cell5.setCellValue(record.getR45_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR45_retail_banking()  != null) {
			cell6.setCellValue(record.getR45_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR45_commercial_banking() != null) {
			cell7.setCellValue(record.getR45_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR45_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR45_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR45_agency_services() != null) {
			cell9.setCellValue(record.getR45_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR45_asset_management() != null) {
			cell10.setCellValue(record.getR45_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR45_retail_brokerage() != null) {
			cell11.setCellValue(record.getR45_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		//row46
		row = sheet.getRow(45);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR46_corporate_finance() != null) {
			cell4.setCellValue(record.getR46_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR46_trading_and_sales() != null) {
			cell5.setCellValue(record.getR46_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR46_retail_banking()  != null) {
			cell6.setCellValue(record.getR46_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR46_commercial_banking() != null) {
			cell7.setCellValue(record.getR46_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR46_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR46_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR46_agency_services() != null) {
			cell9.setCellValue(record.getR46_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR46_asset_management() != null) {
			cell10.setCellValue(record.getR46_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR46_retail_brokerage() != null) {
			cell11.setCellValue(record.getR46_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		//row47
		row = sheet.getRow(46);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR47_corporate_finance() != null) {
			cell4.setCellValue(record.getR47_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR47_trading_and_sales() != null) {
			cell5.setCellValue(record.getR47_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR47_retail_banking()  != null) {
			cell6.setCellValue(record.getR47_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR47_commercial_banking() != null) {
			cell7.setCellValue(record.getR47_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR47_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR47_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR47_agency_services() != null) {
			cell9.setCellValue(record.getR47_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR47_asset_management() != null) {
			cell10.setCellValue(record.getR47_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR47_retail_brokerage() != null) {
			cell11.setCellValue(record.getR47_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		//row48
		row = sheet.getRow(47);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR48_corporate_finance() != null) {
			cell4.setCellValue(record.getR48_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR48_trading_and_sales() != null) {
			cell5.setCellValue(record.getR48_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR48_retail_banking()  != null) {
			cell6.setCellValue(record.getR48_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR48_commercial_banking() != null) {
			cell7.setCellValue(record.getR48_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR48_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR48_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR48_agency_services() != null) {
			cell9.setCellValue(record.getR48_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR48_asset_management() != null) {
			cell10.setCellValue(record.getR48_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR48_retail_brokerage() != null) {
			cell11.setCellValue(record.getR48_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		
		//row49
		row = sheet.getRow(48);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR49_corporate_finance() != null) {
			cell4.setCellValue(record.getR49_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR49_trading_and_sales() != null) {
			cell5.setCellValue(record.getR49_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR49_retail_banking()  != null) {
			cell6.setCellValue(record.getR49_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR49_commercial_banking() != null) {
			cell7.setCellValue(record.getR49_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR49_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR49_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR49_agency_services() != null) {
			cell9.setCellValue(record.getR49_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR49_asset_management() != null) {
			cell10.setCellValue(record.getR49_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR49_retail_brokerage() != null) {
			cell11.setCellValue(record.getR49_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		//row50
		row = sheet.getRow(49);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR50_corporate_finance() != null) {
			cell4.setCellValue(record.getR50_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR50_trading_and_sales() != null) {
			cell5.setCellValue(record.getR50_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR50_retail_banking()  != null) {
			cell6.setCellValue(record.getR50_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR50_commercial_banking() != null) {
			cell7.setCellValue(record.getR50_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR50_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR50_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR50_agency_services() != null) {
			cell9.setCellValue(record.getR50_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR50_asset_management() != null) {
			cell10.setCellValue(record.getR50_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR50_retail_brokerage() != null) {
			cell11.setCellValue(record.getR50_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		//row51
		row = sheet.getRow(50);
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR51_corporate_finance() != null) {
			cell4.setCellValue(record.getR51_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR51_trading_and_sales() != null) {
			cell5.setCellValue(record.getR51_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR51_retail_banking()  != null) {
			cell6.setCellValue(record.getR51_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR51_commercial_banking() != null) {
			cell7.setCellValue(record.getR51_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR51_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR51_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR51_agency_services() != null) {
			cell9.setCellValue(record.getR51_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR51_asset_management() != null) {
			cell10.setCellValue(record.getR51_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR51_retail_brokerage() != null) {
			cell11.setCellValue(record.getR51_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		//row52
		row = sheet.getRow(51);
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR52_corporate_finance() != null) {
			cell4.setCellValue(record.getR52_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR52_trading_and_sales() != null) {
			cell5.setCellValue(record.getR52_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR52_retail_banking()  != null) {
			cell6.setCellValue(record.getR52_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR52_commercial_banking() != null) {
			cell7.setCellValue(record.getR52_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR52_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR52_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR52_agency_services() != null) {
			cell9.setCellValue(record.getR52_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR52_asset_management() != null) {
			cell10.setCellValue(record.getR52_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR52_retail_brokerage() != null) {
			cell11.setCellValue(record.getR52_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		//row53
		row = sheet.getRow(52);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR53_corporate_finance() != null) {
			cell4.setCellValue(record.getR53_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR53_trading_and_sales() != null) {
			cell5.setCellValue(record.getR53_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR53_retail_banking()  != null) {
			cell6.setCellValue(record.getR53_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR53_commercial_banking() != null) {
			cell7.setCellValue(record.getR53_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR53_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR53_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR53_agency_services() != null) {
			cell9.setCellValue(record.getR53_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR53_asset_management() != null) {
			cell10.setCellValue(record.getR53_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR53_retail_brokerage() != null) {
			cell11.setCellValue(record.getR53_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		//row54
		row = sheet.getRow(53);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR54_corporate_finance() != null) {
			cell4.setCellValue(record.getR54_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR54_trading_and_sales() != null) {
			cell5.setCellValue(record.getR54_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR54_retail_banking()  != null) {
			cell6.setCellValue(record.getR54_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR54_commercial_banking() != null) {
			cell7.setCellValue(record.getR54_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR54_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR54_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR54_agency_services() != null) {
			cell9.setCellValue(record.getR54_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
		
		
		
		
		
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR54_asset_management() != null) {
			cell10.setCellValue(record.getR54_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR54_retail_brokerage() != null) {
			cell11.setCellValue(record.getR54_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
		}
		
		//row55
		row = sheet.getRow(54);
		
		 // Column D
		 cell4 = row.createCell(3);
		if (record.getR55_corporate_finance() != null) {
			cell4.setCellValue(record.getR55_corporate_finance().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}
		
		
		
		// Column E
		 cell5 = row.createCell(4);
		if (record.getR55_trading_and_sales() != null) {
			cell5.setCellValue(record.getR55_trading_and_sales().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}
		
		
		
		// Column F
		 cell6 = row.createCell(5);
		if (record.getR55_retail_banking()  != null) {
			cell6.setCellValue(record.getR55_retail_banking() .doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}
		
		
		// Column G
		 cell7 = row.createCell(6);
		if (record.getR55_commercial_banking() != null) {
			cell7.setCellValue(record.getR55_commercial_banking().doubleValue());
			cell7.setCellStyle(numberStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}
		
	
		// Column H
		 cell8 = row.createCell(7);
		if (record.getR55_payments_and_settlements() != null) {
			cell8.setCellValue(record.getR55_payments_and_settlements().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}
		
		
		// Column I
		 cell9 = row.createCell(8);
		if (record.getR55_agency_services() != null) {
			cell9.setCellValue(record.getR55_agency_services().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}
		
	
		
	
		// Column J
		 cell10 = row.createCell(9);
		if (record.getR55_asset_management() != null) {
			cell10.setCellValue(record.getR55_asset_management().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}
		
	
		// Column K
		 cell11 = row.createCell(10);
		if (record.getR55_retail_brokerage() != null) {
			cell11.setCellValue(record.getR55_retail_brokerage().doubleValue());
			cell11.setCellStyle(numberStyle);
		} else {
			cell11.setCellValue("");
			cell11.setCellStyle(textStyle);
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
	
	













