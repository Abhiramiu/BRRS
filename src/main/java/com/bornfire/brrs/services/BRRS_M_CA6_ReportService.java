
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

import com.bornfire.brrs.entities.M_CA6_Detail_Entity;
import com.bornfire.brrs.entities.BRRS_M_CA6_Detail_Repo;
import com.bornfire.brrs.entities.M_CA6_Summary_Entity1;
import com.bornfire.brrs.entities.M_CA6_Summary_Entity2;
import com.bornfire.brrs.entities.M_CA6_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.M_CA6_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.BRRS_M_CA6_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_CA6_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_M_CA6_Archival_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_M_CA6_Archival_Summary_Repo1;

@Component
@Service

public class BRRS_M_CA6_ReportService {
private static final Logger logger = LoggerFactory.getLogger(BRRS_M_CA6_ReportService.class);
	


	@Autowired
	private Environment env;
	
	
	@Autowired
	SessionFactory sessionFactory;
	

	@Autowired
	BRRS_M_CA6_Detail_Repo M_CA6_DETAIL_Repo;
	

	
	@Autowired
	BRRS_M_CA6_Summary_Repo1 M_CA6_Summary_Repo1;
	
	@Autowired
	BRRS_M_CA6_Summary_Repo2 M_CA6_Summary_Repo2;
	@Autowired
	BRRS_M_CA6_Archival_Summary_Repo1 BRRS_M_CA6_Archival_Summary_Repo1;
	@Autowired
	BRRS_M_CA6_Archival_Summary_Repo2 BRRS_M_CA6_Archival_Summary_Repo2;
	
	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
	public ModelAndView getM_CA6View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, String version) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;	

		 try {
	            Date d1 = dateformat.parse(todate);

	            // ---------- CASE 1: ARCHIVAL ----------
	            if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

	                List<M_CA6_Archival_Summary_Entity1> T1Master = BRRS_M_CA6_Archival_Summary_Repo1
	                        .getdatabydateListarchival(d1, version);
	                List<M_CA6_Archival_Summary_Entity2> T2Master = BRRS_M_CA6_Archival_Summary_Repo2
	                        .getdatabydateListarchival(d1, version);

	                mv.addObject("reportsummary", T1Master);
	                mv.addObject("reportsummary1", T2Master);
	            }
	            // ---------- CASE 2: RESUB ----------
	            else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	                List<M_CA6_Archival_Summary_Entity1> T1Master = BRRS_M_CA6_Archival_Summary_Repo1
	                        .getdatabydateListarchival(d1, version);
	                List<M_CA6_Archival_Summary_Entity2> T2Master = BRRS_M_CA6_Archival_Summary_Repo2
	                        .getdatabydateListarchival(d1, version);

	                mv.addObject("reportsummary", T1Master);
	                mv.addObject("reportsummary1", T2Master);
	            }
	            // ---------- CASE 3: NORMAL ----------
	            else {

	                List<M_CA6_Summary_Entity1> T1Master = M_CA6_Summary_Repo1.getdatabydateList(d1);
	                List<M_CA6_Summary_Entity2> T2Master = M_CA6_Summary_Repo2.getdatabydateList(d1);

	                System.out.println("T1Master Size: " + T1Master.size());
	                System.out.println("T2Master Size: " + T2Master.size());

	                mv.addObject("reportsummary", T1Master);
	                mv.addObject("reportsummary1", T2Master);
	            }
		
		
		
		mv.setViewName("BRRS/M_CA6");		
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		
		 } catch (ParseException e) {
	            e.printStackTrace();
	            mv.addObject("error", "Invalid date format for: " + todate);
	        } catch (Exception e) {
	            e.printStackTrace();
	            mv.addObject("error", "An error occurred while fetching M_CA6 data.");
	        }

	        return mv;
	    }
	
	public void updateReport1(M_CA6_Summary_Entity1 entity1) {
	    System.out.println("Report Date: " + entity1.getReportDate());

	    M_CA6_Summary_Entity1 existing = M_CA6_Summary_Repo1.findById(entity1.getReportDate())
	        .orElseThrow(() -> new RuntimeException(
	            "Record not found for REPORT_DATE: " + entity1.getReportDate()));

	    // --------------------------
	    // Update R28–R34 amounts
	    // --------------------------
	    
	    try {
	        for (int i = 12; i <= 16; i++) {
	            String prefix = "R" + i + "_";
	            // You can add more fields here if needed later
	            String[] fields = {
	                "CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT",
	                "AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT"
	            };

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_CA6_Summary_Entity1.class.getMethod(getterName);
	                    Method setter = M_CA6_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(entity1);
	                    setter.invoke(existing, newValue);
	                } catch (NoSuchMethodException e) {
	                    // if any field is missing in entity class, skip it
	                    continue;
	                }
	            }
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating R12–R16 fields", e);
	    }  
	    
	    try {
	        for (int i = 20; i <= 24; i++) {
	            String prefix = "R" + i + "_";
	            // You can add more fields here if needed later
	            String[] fields = {
	                "CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT",
	                "AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT"
	            };

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_CA6_Summary_Entity1.class.getMethod(getterName);
	                    Method setter = M_CA6_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(entity1);
	                    setter.invoke(existing, newValue);
	                } catch (NoSuchMethodException e) {
	                    // if any field is missing in entity class, skip it
	                    continue;
	                }
	            }
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating R20-24 fields", e);
	    }
	    M_CA6_Summary_Repo1.save(existing);
	}	
	
	public ModelAndView getM_CA6currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter) {

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int totalPages=0;
		ModelAndView mv = new ModelAndView();

		Session hs = sessionFactory.getCurrentSession();
		List<M_CA6_Detail_Entity> T1Dt1 = new ArrayList<M_CA6_Detail_Entity>();
		
		try {
			Date d1 = dateformat.parse(todate);
			String rowId = null;
	        String columnId = null;
	        if (filter != null && filter.contains(",")) {
	            String[] parts = filter.split(",");
	            if (parts.length >= 2) {
	                rowId = parts[0];
	                columnId = parts[1];
	            }
	        }

	        if (rowId != null && columnId != null) { 
	        	System.out.println("Testing");
	        	T1Dt1=M_CA6_DETAIL_Repo.getdatabydateListrow(dateformat.parse(todate),columnId,rowId);

	        	
	        } else {
	        		
				T1Dt1 = M_CA6_DETAIL_Repo.getdatabydateList(d1,currentPage,pageSize);
				totalPages=M_CA6_DETAIL_Repo.getdatacount(dateformat.parse(todate));
				mv.addObject("pagination","YES");
				
	        }

		
		} catch (ParseException e) {
			e.printStackTrace();
		}
		

		//Page<Object> T1Dt1Page = new PageImpl<Object>(pagedlist, PageRequest.of(currentPage, pageSize), T1Dt1.size());

		mv.setViewName("BRRS/M_CA6");
		mv.addObject("currentPage", currentPage);
		mv.addObject("totalPages",(int)Math.ceil((double)totalPages / 100));
		mv.addObject("displaymode", "Details");
		//mv.addObject("reportdetails", T1Dt1Page.getContent());
		mv.addObject("reportdetails",T1Dt1 );
		mv.addObject("reportmaster12", T1Dt1);
		//mv.addObject("reportmaster1", qr);
		//mv.addObject("singledetail", new T1CurProdDetail());
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}
	

public byte[] getM_CA6Excel(String filename,String reportId, String fromdate, String todate, String currency, String dtltype , String type ,
		String version) throws Exception {
	logger.info("Service: Starting Excel generation process in memory.");
	if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
		logger.info("Service: Generating ARCHIVAL report for version {}", version);
		return getExcelM_CA6ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
	}

	List<M_CA6_Summary_Entity1> dataList =M_CA6_Summary_Repo1.getdatabydateList(dateformat.parse(todate)) ;

	List<M_CA6_Summary_Entity2> dataList1 =M_CA6_Summary_Repo2.getdatabydateList(dateformat.parse(todate)) ;

	if (dataList.isEmpty()) {
		logger.warn("Service: No data found for BRRS report. Returning empty result.");
		return new byte[0];
	}
	String templateDir = env.getProperty("output.exportpathtemp");
	String templateFileName = filename;
	System.out.println(templateDir);
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
		// --- End of Style Definitions ---
		// --- End of Style Definitions ---

		int startRow = 11;
		
		if (!dataList.isEmpty()) {
			for (int i = 0; i < dataList.size(); i++) {
				
				M_CA6_Summary_Entity1 record = dataList.get(i);
				M_CA6_Summary_Entity2 record2 = dataList1.get(i);
				System.out.println("rownumber="+startRow + i);
				System.out.println("enterred serice method.....");
				Row row = sheet.getRow(startRow + i);
				if (row == null) {
					row = sheet.createRow(startRow + i);
				}


				//row12
				// Column D
				Cell cell3 = row.createCell(3);
				if (record.getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row12
				// Column E
				Cell cell4 = row.createCell(4);
				if (record.getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				//row13
				row = sheet.getRow(12);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row13
				// Column E
				cell4 = row.createCell(4);
				if (record.getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				//row14
				row = sheet.getRow(13);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row14
				// Column E
				cell4 = row.createCell(4);
				if (record.getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}

				//row15
				row = sheet.getRow(14);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row15
				// Column E
				cell4 = row.createCell(4);
				if (record.getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				//row16
				row = sheet.getRow(15);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row16
				// Column E
				cell4 = row.createCell(4);
				if (record.getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				//row20
				row = sheet.getRow(19);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row20
				// Column E
				cell4 = row.createCell(4);
				if (record.getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				//row21
				row = sheet.getRow(20);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row21
				// Column E
				cell4 = row.createCell(4);
				if (record.getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}

				//row22
				row = sheet.getRow(21);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row22
				// Column E
				cell4 = row.createCell(4);
				if (record.getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				//row23
				row = sheet.getRow(22);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row23
				// Column E
				cell4 = row.createCell(4);
				if (record.getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}

				//row24
				row = sheet.getRow(12);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row24
				// Column E
				cell4 = row.createCell(4);
				if (record.getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				//row28
				row = sheet.getRow(27);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR28_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR28_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR28_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR28_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR28_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR28_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR28_AMOUNT() != null) {
					cell3.setCellValue(record2.getR28_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row29
				row = sheet.getRow(28);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR29_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR29_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR29_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR29_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR29_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR29_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR29_AMOUNT() != null) {
					cell3.setCellValue(record2.getR29_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row30
				row = sheet.getRow(29);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR30_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR30_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR30_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR30_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR30_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR30_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR30_AMOUNT() != null) {
					cell3.setCellValue(record2.getR30_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row31
				row = sheet.getRow(30);
				// Column D
				
				cell3 = row.createCell(2); 
				if (record2.getR31_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR31_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR31_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR31_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR31_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR31_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR31_AMOUNT() != null) {
					cell3.setCellValue(record2.getR31_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row32
				row = sheet.getRow(31);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR32_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR32_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR32_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR32_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR32_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR32_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR32_AMOUNT() != null) {
					cell3.setCellValue(record2.getR32_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row33
				row = sheet.getRow(32);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR33_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR33_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR33_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR33_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR33_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR33_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR33_AMOUNT() != null) {
					cell3.setCellValue(record2.getR33_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row34
				row = sheet.getRow(33);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR34_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR34_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR34_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR34_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR34_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR34_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR34_AMOUNT() != null) {
					cell3.setCellValue(record2.getR34_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row40
				row = sheet.getRow(39);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR40_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR40_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR40_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR40_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR40_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR40_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR40_AMOUNT() != null) {
					cell3.setCellValue(record2.getR40_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row41
				row = sheet.getRow(40);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR41_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR41_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR41_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR41_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR41_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR41_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR41_AMOUNT() != null) {
					cell3.setCellValue(record2.getR41_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row42
				row = sheet.getRow(41);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR42_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR42_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR42_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR42_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR42_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR42_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR42_AMOUNT() != null) {
					cell3.setCellValue(record2.getR42_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row43
				row = sheet.getRow(42);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR43_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR43_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR43_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR43_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR43_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR43_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR43_AMOUNT() != null) {
					cell3.setCellValue(record2.getR43_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row44
				row = sheet.getRow(43);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR44_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR44_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR44_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR44_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR44_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR44_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR44_AMOUNT() != null) {
					cell3.setCellValue(record2.getR44_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row45
				row = sheet.getRow(44);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR45_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR45_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR45_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR45_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR45_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR45_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR45_AMOUNT() != null) {
					cell3.setCellValue(record2.getR45_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row46
				row = sheet.getRow(45);
				// Column D
				
				cell3 = row.createCell(2); 
				if (record2.getR46_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR46_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR46_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR46_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR46_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR46_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR46_AMOUNT() != null) {
					cell3.setCellValue(record2.getR46_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
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
/*
 * 
 * public List<Object> getM_CA6Archival() { List<Object> M_CA6Archivallist = new
 * ArrayList<>(); try { M_CA6Archivallist =
 * BRRS_M_CA6_Archival_Summary_Repo1.getM_CA6archival(); M_CA6Archivallist =
 * BRRS_M_CA6_Archival_Summary_Repo2.getM_CA6archival();
 * System.out.println("countser" + M_CA6Archivallist.size()); } catch (Exception
 * e) { // Log the exception
 * System.err.println("Error fetching M_CA7 Archival data: " + e.getMessage());
 * e.printStackTrace();
 * 
 * // Optionally, you can rethrow it or return empty list // throw new
 * RuntimeException("Failed to fetch data", e); } return M_CA6Archivallist; }
 */



public byte[] getExcelM_CA6ARCHIVAL(String filename,String reportId, String fromdate, String todate, String currency, String dtltype,String type,String version) throws Exception {
	logger.info("Service: Starting Excel generation process in memory.");


	List<M_CA6_Archival_Summary_Entity1> dataList = BRRS_M_CA6_Archival_Summary_Repo1
			.getdatabydateListarchival(dateformat.parse(todate), version);
	List<M_CA6_Archival_Summary_Entity2> dataList1 = BRRS_M_CA6_Archival_Summary_Repo2
			.getdatabydateListarchival(dateformat.parse(todate), version);

	
	if (dataList.isEmpty()) {
		logger.warn("Service: No data found for BWRBR report. Returning empty result.");
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
		
		if (!dataList.isEmpty()) {
			for (int i = 0; i < dataList.size(); i++) {
				
				M_CA6_Archival_Summary_Entity1 record = dataList.get(i);
				M_CA6_Archival_Summary_Entity2 record2 = dataList1.get(i);
				System.out.println("rownumber="+startRow + i);
				Row row = sheet.getRow(startRow + i);
				if (row == null) {
					row = sheet.createRow(startRow + i);
				}



				//row12
				// Column D
				Cell cell3 = row.createCell(3);
				if (record.getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row12
				// Column E
				Cell cell4 = row.createCell(4);
				if (record.getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				//row13
				row = sheet.getRow(12);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row13
				// Column E
				cell4 = row.createCell(4);
				if (record.getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				//row14
				row = sheet.getRow(13);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row14
				// Column E
				cell4 = row.createCell(4);
				if (record.getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}

				//row15
				row = sheet.getRow(14);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row15
				// Column E
				cell4 = row.createCell(4);
				if (record.getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				//row16
				row = sheet.getRow(15);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row16
				// Column E
				cell4 = row.createCell(4);
				if (record.getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				//row20
				row = sheet.getRow(19);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row20
				// Column E
				cell4 = row.createCell(4);
				if (record.getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				//row21
				row = sheet.getRow(20);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row21
				// Column E
				cell4 = row.createCell(4);
				if (record.getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}

				//row22
				row = sheet.getRow(21);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row22
				// Column E
				cell4 = row.createCell(4);
				if (record.getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				//row23
				row = sheet.getRow(22);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row23
				// Column E
				cell4 = row.createCell(4);
				if (record.getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}

				//row24
				row = sheet.getRow(12);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row24
				// Column E
				cell4 = row.createCell(4);
				if (record.getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				//row28
				row = sheet.getRow(27);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR28_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR28_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR28_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR28_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR28_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR28_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR28_AMOUNT() != null) {
					cell3.setCellValue(record2.getR28_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row29
				row = sheet.getRow(28);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR29_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR29_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR29_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR29_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR29_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR29_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR29_AMOUNT() != null) {
					cell3.setCellValue(record2.getR29_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row30
				row = sheet.getRow(29);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR30_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR30_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR30_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR30_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR30_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR30_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR30_AMOUNT() != null) {
					cell3.setCellValue(record2.getR30_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row31
				row = sheet.getRow(30);
				// Column D
				
				cell3 = row.createCell(2); 
				if (record2.getR31_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR31_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR31_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR31_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR31_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR31_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR31_AMOUNT() != null) {
					cell3.setCellValue(record2.getR31_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row32
				row = sheet.getRow(31);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR32_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR32_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR32_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR32_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR32_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR32_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR32_AMOUNT() != null) {
					cell3.setCellValue(record2.getR32_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row33
				row = sheet.getRow(32);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR33_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR33_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR33_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR33_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR33_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR33_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR33_AMOUNT() != null) {
					cell3.setCellValue(record2.getR33_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row34
				row = sheet.getRow(33);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR34_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR34_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR34_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR34_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR34_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR34_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR34_AMOUNT() != null) {
					cell3.setCellValue(record2.getR34_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row40
				row = sheet.getRow(39);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR40_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR40_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR40_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR40_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR40_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR40_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR40_AMOUNT() != null) {
					cell3.setCellValue(record2.getR40_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row41
				row = sheet.getRow(40);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR41_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR41_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR41_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR41_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR41_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR41_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR41_AMOUNT() != null) {
					cell3.setCellValue(record2.getR41_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row42
				row = sheet.getRow(41);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR42_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR42_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR42_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR42_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR42_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR42_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR42_AMOUNT() != null) {
					cell3.setCellValue(record2.getR42_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row43
				row = sheet.getRow(42);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR43_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR43_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR43_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR43_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR43_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR43_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR43_AMOUNT() != null) {
					cell3.setCellValue(record2.getR43_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row44
				row = sheet.getRow(43);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR44_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR44_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR44_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR44_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR44_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR44_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR44_AMOUNT() != null) {
					cell3.setCellValue(record2.getR44_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row45
				row = sheet.getRow(44);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR45_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR45_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR45_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR45_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR45_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR45_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR45_AMOUNT() != null) {
					cell3.setCellValue(record2.getR45_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row46
				row = sheet.getRow(45);
				// Column D
				
				cell3 = row.createCell(2); 
				if (record2.getR46_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR46_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR46_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR46_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR46_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR46_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR46_AMOUNT() != null) {
					cell3.setCellValue(record2.getR46_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
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

public byte[] BRRS_M_CA6DetailExcel(String filename, String fromdate, String todate) {
    try {
        logger.info("Generating Excel for BRRS_M_CA6 Details...");
        System.out.println("came to Detail download service");

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("BRRS_M_CA6Details");

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
        balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
        balanceStyle.setBorderTop(border);
        balanceStyle.setBorderBottom(border);
        balanceStyle.setBorderLeft(border);
        balanceStyle.setBorderRight(border);

        // Header row
        String[] headers = {
            "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "ROWID", "COLUMNID", "REPORT_DATE"
        };

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
        List<M_CA6_Detail_Entity> reportData = M_CA6_DETAIL_Repo.getdatabydateList(parsedToDate);

        if (reportData != null && !reportData.isEmpty()) {
            int rowIndex = 1;
            for (M_CA6_Detail_Entity item : reportData) {
                XSSFRow row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(item.getCustId());
                row.createCell(1).setCellValue(item.getAcctNumber());
                row.createCell(2).setCellValue(item.getAcctName());

                // ACCT BALANCE (right aligned, 3 decimal places)
                Cell balanceCell = row.createCell(3);
                if (item.getAcctBalanceInpula() != null) {
                    balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
                } else {
                    balanceCell.setCellValue(0.000);
                }
                balanceCell.setCellStyle(balanceStyle);

                row.createCell(4).setCellValue(item.getRowId());
                row.createCell(5).setCellValue(item.getColumnId());
                row.createCell(6).setCellValue(
                    item.getReportDate() != null ?
                    new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate()) : ""
                );

                // Apply data style for all other cells
                for (int j = 0; j < 7; j++) {
                    if (j != 3) {
                        row.getCell(j).setCellStyle(dataStyle);
                    }
                }
            }
        } else {
            logger.info("No data found for BWRBR_M_SFINP2 — only header will be written.");
        }

        // Write to byte[]
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();

        logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
        return bos.toByteArray();

    } catch (Exception e) {
        logger.error("Error generating BWRBR_M_CA6 Excel", e);
        return new byte[0];
    }
}

public void updateReport(M_CA6_Summary_Entity2 entity) {
    System.out.println("Report Date: " + entity.getReportDate());

    M_CA6_Summary_Entity2 existing = M_CA6_Summary_Repo2.findById(entity.getReportDate())
        .orElseThrow(() -> new RuntimeException(
            "Record not found for REPORT_DATE: " + entity.getReportDate()));

    // --------------------------
    // Update R28–R34 amounts
    // --------------------------
    try {
        for (int i = 28; i <= 34; i++) {
            String prefix = "R" + i + "_";
            String[] fields = { "AMOUNT" };

            for (String field : fields) {
                String getterName = "get" + prefix + field;
                String setterName = "set" + prefix + field;

                try {
                    Method getter = M_CA6_Summary_Entity2.class.getMethod(getterName);
                    Method setter = M_CA6_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());

                    Object newValue = getter.invoke(entity);
                    setter.invoke(existing, newValue);

                } catch (NoSuchMethodException e) {
                    continue;
                }
            }
        }

        // Update R35 total
        Method getter = M_CA6_Summary_Entity2.class.getMethod("getR35_AMOUNT");
        Method setter = M_CA6_Summary_Entity2.class.getMethod("setR35_AMOUNT", getter.getReturnType());
        setter.invoke(existing, getter.invoke(entity));

    } catch (Exception e) {
        throw new RuntimeException("Error while updating R28–R35 fields", e);
    }

    // --------------------------
    // Update R40–R46 + R47 amounts
    // --------------------------
    try {
        for (int i = 40; i <= 46; i++) {
            String prefix = "R" + i + "_";
            String[] fields = { "AMOUNT" };

            for (String field : fields) {
                String getterName = "get" + prefix + field;
                String setterName = "set" + prefix + field;

                try {
                    Method getter = M_CA6_Summary_Entity2.class.getMethod(getterName);
                    Method setter = M_CA6_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());

                    Object newValue = getter.invoke(entity);
                    setter.invoke(existing, newValue);

                } catch (NoSuchMethodException e) {
                    continue;
                }
            }
        }

        // Update R47
        Method getter = M_CA6_Summary_Entity2.class.getMethod("getR47_AMOUNT");
        Method setter = M_CA6_Summary_Entity2.class.getMethod("setR47_AMOUNT", getter.getReturnType());
        setter.invoke(existing, getter.invoke(entity));

    } catch (Exception e) {
        throw new RuntimeException("Error while updating R40–R47 fields", e);
    }

    // --------------------------
    // ✅ Update date fields
    // --------------------------
    try {
        existing.setR28_ISSUANCE_DATE(entity.getR28_ISSUANCE_DATE());
        existing.setR28_CONTRACTUAL_MATURITY_DATE(entity.getR28_CONTRACTUAL_MATURITY_DATE());
        existing.setR28_EFFECTIVE_MATURITY_DATE(entity.getR28_EFFECTIVE_MATURITY_DATE());
        
        

        existing.setR29_ISSUANCE_DATE(entity.getR29_ISSUANCE_DATE());
        existing.setR29_CONTRACTUAL_MATURITY_DATE(entity.getR29_CONTRACTUAL_MATURITY_DATE());
        existing.setR29_EFFECTIVE_MATURITY_DATE(entity.getR29_EFFECTIVE_MATURITY_DATE());

        existing.setR30_ISSUANCE_DATE(entity.getR30_ISSUANCE_DATE());
        existing.setR30_CONTRACTUAL_MATURITY_DATE(entity.getR30_CONTRACTUAL_MATURITY_DATE());
        existing.setR30_EFFECTIVE_MATURITY_DATE(entity.getR30_EFFECTIVE_MATURITY_DATE());

        existing.setR31_ISSUANCE_DATE(entity.getR31_ISSUANCE_DATE());
        existing.setR31_CONTRACTUAL_MATURITY_DATE(entity.getR31_CONTRACTUAL_MATURITY_DATE());
        existing.setR31_EFFECTIVE_MATURITY_DATE(entity.getR31_EFFECTIVE_MATURITY_DATE());

        existing.setR32_ISSUANCE_DATE(entity.getR32_ISSUANCE_DATE());
        existing.setR32_CONTRACTUAL_MATURITY_DATE(entity.getR32_CONTRACTUAL_MATURITY_DATE());
        existing.setR32_EFFECTIVE_MATURITY_DATE(entity.getR32_EFFECTIVE_MATURITY_DATE());

        existing.setR33_ISSUANCE_DATE(entity.getR33_ISSUANCE_DATE());
        existing.setR33_CONTRACTUAL_MATURITY_DATE(entity.getR33_CONTRACTUAL_MATURITY_DATE());
        existing.setR33_EFFECTIVE_MATURITY_DATE(entity.getR33_EFFECTIVE_MATURITY_DATE());

        existing.setR34_ISSUANCE_DATE(entity.getR34_ISSUANCE_DATE());
        existing.setR34_CONTRACTUAL_MATURITY_DATE(entity.getR34_CONTRACTUAL_MATURITY_DATE());
        existing.setR34_EFFECTIVE_MATURITY_DATE(entity.getR34_EFFECTIVE_MATURITY_DATE());

        // Update dates R40–R46
        existing.setR40_ISSUANCE_DATE(entity.getR40_ISSUANCE_DATE());
        existing.setR40_CONTRACTUAL_MATURITY_DATE(entity.getR40_CONTRACTUAL_MATURITY_DATE());
        existing.setR40_EFFECTIVE_MATURITY_DATE(entity.getR40_EFFECTIVE_MATURITY_DATE());

        existing.setR41_ISSUANCE_DATE(entity.getR41_ISSUANCE_DATE());
        existing.setR41_CONTRACTUAL_MATURITY_DATE(entity.getR41_CONTRACTUAL_MATURITY_DATE());
        existing.setR41_EFFECTIVE_MATURITY_DATE(entity.getR41_EFFECTIVE_MATURITY_DATE());

        existing.setR42_ISSUANCE_DATE(entity.getR42_ISSUANCE_DATE());
        existing.setR42_CONTRACTUAL_MATURITY_DATE(entity.getR42_CONTRACTUAL_MATURITY_DATE());
        existing.setR42_EFFECTIVE_MATURITY_DATE(entity.getR42_EFFECTIVE_MATURITY_DATE());

        existing.setR43_ISSUANCE_DATE(entity.getR43_ISSUANCE_DATE());
        existing.setR43_CONTRACTUAL_MATURITY_DATE(entity.getR43_CONTRACTUAL_MATURITY_DATE());
        existing.setR43_EFFECTIVE_MATURITY_DATE(entity.getR43_EFFECTIVE_MATURITY_DATE());

        existing.setR44_ISSUANCE_DATE(entity.getR44_ISSUANCE_DATE());
        existing.setR44_CONTRACTUAL_MATURITY_DATE(entity.getR44_CONTRACTUAL_MATURITY_DATE());
        existing.setR44_EFFECTIVE_MATURITY_DATE(entity.getR44_EFFECTIVE_MATURITY_DATE());

        existing.setR45_ISSUANCE_DATE(entity.getR45_ISSUANCE_DATE());
        existing.setR45_CONTRACTUAL_MATURITY_DATE(entity.getR45_CONTRACTUAL_MATURITY_DATE());
        existing.setR45_EFFECTIVE_MATURITY_DATE(entity.getR45_EFFECTIVE_MATURITY_DATE());

        existing.setR46_ISSUANCE_DATE(entity.getR46_ISSUANCE_DATE());
        existing.setR46_CONTRACTUAL_MATURITY_DATE(entity.getR46_CONTRACTUAL_MATURITY_DATE());
        existing.setR46_EFFECTIVE_MATURITY_DATE(entity.getR46_EFFECTIVE_MATURITY_DATE());

    } catch (Exception e) {
        throw new RuntimeException("Error while updating date fields", e);
    }

    // --------------------------
    // Save updated entity
    // --------------------------
    M_CA6_Summary_Repo2.save(existing);
}

////////////////////////////////////////// RESUBMISSION///////////////////////////////////////////////////////////////////
/// Report Date | Report Version | Domain
/// RESUB VIEW

public List<Object[]> getM_CA6Resub() {
	List<Object[]> resubList = new ArrayList<>();
	try {
		List<M_CA6_Archival_Summary_Entity1> latestArchivalList = BRRS_M_CA6_Archival_Summary_Repo1
				.getdatabydateListWithVersion();

		if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
			for (M_CA6_Archival_Summary_Entity1 entity : latestArchivalList) {
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
		System.err.println("Error fetching M_CA6 Resub data: " + e.getMessage());
		e.printStackTrace();
	}
	return resubList;
}

public List<Object[]> getM_CA6Archival() {
	List<Object[]> archivalList = new ArrayList<>();
	try {
		List<M_CA6_Archival_Summary_Entity1> latestArchivalList = BRRS_M_CA6_Archival_Summary_Repo1
				.getdatabydateListWithVersion();

		if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
			for (M_CA6_Archival_Summary_Entity1 entity : latestArchivalList) {
				archivalList.add(new Object[] {
						entity.getReportDate(),
						entity.getReportVersion()
				});
			}
			System.out.println("Fetched " + archivalList.size() + " record(s)");
		} else {
			System.out.println("No archival data found.");
		}

	} catch (Exception e) {
		System.err.println("Error fetching M_CA6 Resub data: " + e.getMessage());
		e.printStackTrace();
	}
	return archivalList;
}
public void updateReportReSub(
		M_CA6_Summary_Entity1 updatedEntity1,
		M_CA6_Summary_Entity2 updatedEntity2) {

	System.out.println("Came to M_CA6 Resub Service");
	System.out.println("Report Date: " + updatedEntity1.getReportDate());

	Date reportDate = updatedEntity1.getReportDate();
	int newVersion = 1;

	try {
		// 🔹 Fetch the latest archival version for this report date from Entity1
		Optional<M_CA6_Archival_Summary_Entity1> latestArchivalOpt1 = BRRS_M_CA6_Archival_Summary_Repo1
				.getLatestArchivalVersionByDate(reportDate);

		if (latestArchivalOpt1.isPresent()) {
			M_CA6_Archival_Summary_Entity1 latestArchival = latestArchivalOpt1.get();
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
		boolean exists = BRRS_M_CA6_Archival_Summary_Repo1
				.findByReportDateAndReportVersion(reportDate, String.valueOf(newVersion))
				.isPresent();

		if (exists) {
			throw new RuntimeException("⚠ Version " + newVersion + " already exists for report date " + reportDate);
		}

		// Copy data from summary to archival entities for all 3 entities
		M_CA6_Archival_Summary_Entity1 archivalEntity1 = new M_CA6_Archival_Summary_Entity1();
		M_CA6_Archival_Summary_Entity2 archivalEntity2 = new M_CA6_Archival_Summary_Entity2();

		org.springframework.beans.BeanUtils.copyProperties(updatedEntity1, archivalEntity1);
		org.springframework.beans.BeanUtils.copyProperties(updatedEntity2, archivalEntity2);

		// Set common fields
		Date now = new Date();
		archivalEntity1.setReportDate(reportDate);
		archivalEntity2.setReportDate(reportDate);

		archivalEntity1.setReportVersion(String.valueOf(newVersion));
		archivalEntity2.setReportVersion(String.valueOf(newVersion));

		archivalEntity1.setReportResubDate(now);
		archivalEntity2.setReportResubDate(now);

		System.out.println("Saving new archival version: " + newVersion);

		// Save to all three archival repositories
		BRRS_M_CA6_Archival_Summary_Repo1.save(archivalEntity1);
		BRRS_M_CA6_Archival_Summary_Repo2.save(archivalEntity2);

		System.out.println("Saved archival version successfully: " + newVersion);

	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Error while creating M_CA6 archival resubmission record", e);
	}
}

public byte[] BRRS_M_CA6ResubExcel(String filename, String reportId, String fromdate,
		String todate, String currency, String dtltype,
		String type, String version) throws Exception {

	logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

	if (type.equals("RESUB") & version != null) {

	}

	List<M_CA6_Archival_Summary_Entity1> dataList = BRRS_M_CA6_Archival_Summary_Repo1
			.getdatabydateListarchival(dateformat.parse(todate), version);
	List<M_CA6_Archival_Summary_Entity2> dataList1 = BRRS_M_CA6_Archival_Summary_Repo2
			.getdatabydateListarchival(dateformat.parse(todate), version);

	if (dataList.isEmpty()) {
		logger.warn("Service: No data found for M_CA6 report. Returning empty result.");
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
		
		if (!dataList.isEmpty()) {
			for (int i = 0; i < dataList.size(); i++) {
				
				M_CA6_Archival_Summary_Entity1 record = dataList.get(i);
				M_CA6_Archival_Summary_Entity2 record2 = dataList1.get(i);
				System.out.println("rownumber="+startRow + i);
				Row row = sheet.getRow(startRow + i);
				if (row == null) {
					row = sheet.createRow(startRow + i);
				}



				//row12
				// Column D
				Cell cell3 = row.createCell(3);
				if (record.getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row12
				// Column E
				Cell cell4 = row.createCell(4);
				if (record.getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				//row13
				row = sheet.getRow(12);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row13
				// Column E
				cell4 = row.createCell(4);
				if (record.getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				//row14
				row = sheet.getRow(13);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row14
				// Column E
				cell4 = row.createCell(4);
				if (record.getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}

				//row15
				row = sheet.getRow(14);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row15
				// Column E
				cell4 = row.createCell(4);
				if (record.getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				//row16
				row = sheet.getRow(15);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row16
				// Column E
				cell4 = row.createCell(4);
				if (record.getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				//row20
				row = sheet.getRow(19);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row20
				// Column E
				cell4 = row.createCell(4);
				if (record.getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				//row21
				row = sheet.getRow(20);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row21
				// Column E
				cell4 = row.createCell(4);
				if (record.getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}

				//row22
				row = sheet.getRow(21);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row22
				// Column E
				cell4 = row.createCell(4);
				if (record.getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				//row23
				row = sheet.getRow(22);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row23
				// Column E
				cell4 = row.createCell(4);
				if (record.getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}

				//row24
				row = sheet.getRow(12);
				// Column D
				cell3 = row.createCell(3);
				if (record.getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell3.setCellValue(record.getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				
				
				//row24
				// Column E
				cell4 = row.createCell(4);
				if (record.getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					cell4.setCellValue(record.getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
					cell4.setCellStyle(numberStyle);
				} else {
					cell4.setCellValue("");
					cell4.setCellStyle(textStyle);
				}
				
				
				//row28
				row = sheet.getRow(27);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR28_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR28_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR28_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR28_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR28_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR28_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR28_AMOUNT() != null) {
					cell3.setCellValue(record2.getR28_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row29
				row = sheet.getRow(28);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR29_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR29_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR29_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR29_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR29_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR29_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR29_AMOUNT() != null) {
					cell3.setCellValue(record2.getR29_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row30
				row = sheet.getRow(29);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR30_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR30_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR30_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR30_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR30_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR30_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR30_AMOUNT() != null) {
					cell3.setCellValue(record2.getR30_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row31
				row = sheet.getRow(30);
				// Column D
				
				cell3 = row.createCell(2); 
				if (record2.getR31_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR31_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR31_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR31_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR31_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR31_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR31_AMOUNT() != null) {
					cell3.setCellValue(record2.getR31_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row32
				row = sheet.getRow(31);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR32_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR32_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR32_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR32_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR32_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR32_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR32_AMOUNT() != null) {
					cell3.setCellValue(record2.getR32_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row33
				row = sheet.getRow(32);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR33_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR33_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR33_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR33_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR33_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR33_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR33_AMOUNT() != null) {
					cell3.setCellValue(record2.getR33_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row34
				row = sheet.getRow(33);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR34_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR34_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR34_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR34_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR34_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR34_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR34_AMOUNT() != null) {
					cell3.setCellValue(record2.getR34_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row40
				row = sheet.getRow(39);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR40_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR40_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR40_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR40_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR40_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR40_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR40_AMOUNT() != null) {
					cell3.setCellValue(record2.getR40_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row41
				row = sheet.getRow(40);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR41_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR41_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR41_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR41_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR41_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR41_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR41_AMOUNT() != null) {
					cell3.setCellValue(record2.getR41_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row42
				row = sheet.getRow(41);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR42_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR42_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR42_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR42_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR42_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR42_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR42_AMOUNT() != null) {
					cell3.setCellValue(record2.getR42_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row43
				row = sheet.getRow(42);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR43_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR43_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR43_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR43_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR43_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR43_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR43_AMOUNT() != null) {
					cell3.setCellValue(record2.getR43_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row44
				row = sheet.getRow(43);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR44_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR44_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR44_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR44_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR44_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR44_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR44_AMOUNT() != null) {
					cell3.setCellValue(record2.getR44_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row45
				row = sheet.getRow(44);
				// Column D
				cell3 = row.createCell(2); 
				if (record2.getR45_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR45_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR45_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR45_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR45_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR45_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR45_AMOUNT() != null) {
					cell3.setCellValue(record2.getR45_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
				}
				
				//row46
				row = sheet.getRow(45);
				// Column D
				
				cell3 = row.createCell(2); 
				if (record2.getR46_ISSUANCE_DATE() != null) {
				    cell3.setCellValue(record2.getR46_ISSUANCE_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(3);
				if (record2.getR46_CONTRACTUAL_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR46_CONTRACTUAL_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(4);
				if (record2.getR46_EFFECTIVE_MATURITY_DATE() != null) {
				    cell3.setCellValue(record2.getR46_EFFECTIVE_MATURITY_DATE());
				    cell3.setCellStyle(dateStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell3 = row.createCell(5);
				if (record2.getR46_AMOUNT() != null) {
					cell3.setCellValue(record2.getR46_AMOUNT().doubleValue());
					cell3.setCellStyle(numberStyle);
				} else {
					cell3.setCellValue("");
					cell3.setCellStyle(textStyle);
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

	

