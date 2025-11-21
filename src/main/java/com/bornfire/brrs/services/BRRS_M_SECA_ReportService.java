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

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

//import com.bornfire.brrs.entities.BRRS_M_SECA_Archival_Detail_Entity;
//import com.bornfire.brrs.entities.BRRS_M_SECA_Archival_Detail_Repo;
import com.bornfire.brrs.entities.M_SECA_Manual_Archival_Summary_Entity;
//import com.bornfire.brrs.entities.BRRS_M_SECA_Detail_Repo;
import com.bornfire.brrs.entities.M_SECA_Manual_Summary_Entity;

import com.bornfire.brrs.entities.BRRS_M_SECA_Manual_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SECA_Manual_Archival_Summary_Repo;
//import com.bornfire.brrs.entities.BRRS_M_SECA_Detail_Entity;


import java.math.BigDecimal;

@Component
@Service
public class BRRS_M_SECA_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SECA_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;



//	@Autowired
//	BRRS_M_SECA_Detail_Repo BRRS_M_SECA_Detail_Repo;

	@Autowired
	BRRS_M_SECA_Manual_Summary_Repo BRRS_M_SECA_Summary_Repo;

//	@Autowired
//	BRRS_M_SECA_Archival_Detail_Repo BRRS_M_SECA_Archival_Detail_Repo;

	@Autowired
	BRRS_M_SECA_Manual_Archival_Summary_Repo BRRS_M_SECA_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_SECAview(String reportId, String fromdate, String todate, String currency,
										String dtltype, Pageable pageable, String type, String version) {
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		System.out.println("testing");
		System.out.println(version);

		if (type.equals("ARCHIVAL") & version != null) {
			System.out.println(type);
			List<M_SECA_Manual_Archival_Summary_Entity> T1Master = new ArrayList<M_SECA_Manual_Archival_Summary_Entity>();
			System.out.println(version);
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_SECA_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} else {
			List<M_SECA_Manual_Summary_Entity> T1Master = new ArrayList<M_SECA_Manual_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_SECA_Summary_Repo.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/M_SECA");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}
	
	
	public void updateReport1(M_SECA_Manual_Summary_Entity updatedEntity) {
	    System.out.println("Came to services1");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    M_SECA_Manual_Summary_Entity existing = BRRS_M_SECA_Summary_Repo.findById(updatedEntity.getReport_date())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

	    try {
	        // 1. Loop from R15 to R50 and copy fields
	        for (int i = 14; i <= 57; i++) {
		if (i == 17 || i == 32 || i == 35 || i == 36 || i == 38 || i == 43 || i == 47 || i == 51) {
        continue;
    }
	            String prefix = "R" + i + "_";

	            String[] fields = {"EQUITY", "BONDS", "BOBC", "TRES_BILLS", "REPURCHASE_AGREE", 
	    	     		"COM_PAPER", "CERT_OF_DEP", "PLEDGED_ASSET", "OTHER"};
	           

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_SECA_Manual_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_SECA_Manual_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }
	        for (int i = 36; i <= 36; i++) {
//	    		if (i == 17 || i == 32 || i == 35 || i == 36 || i == 38 || i == 43 || i == 47 || i == 51) {
//	            continue;}
	        
	    	            String prefix = "R" + i + "_";

	    	            String[] fields = {"EQUITY", "TRES_BILLS", "REPURCHASE_AGREE", 
	    	    	     		"COM_PAPER", "CERT_OF_DEP", "PLEDGED_ASSET", "OTHER"};
	    	           

	    	            for (String field : fields) {
	    	                String getterName = "get" + prefix + field;
	    	                String setterName = "set" + prefix + field;

	    	                try {
	    	                    Method getter = M_SECA_Manual_Summary_Entity.class.getMethod(getterName);
	    	                    Method setter = M_SECA_Manual_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	    	                    Object newValue = getter.invoke(updatedEntity);
	    	                    setter.invoke(existing, newValue);

	    	                } catch (NoSuchMethodException e) {
	    	                    // Skip missing fields
	    	                    continue;
	    	                }
	    	            }
	    	        }
	        
// Loop through rows for formula fields 
int[] targetRows = {13, 17, 32, 35, 38, 43, 47, 51, 58};

for (int i : targetRows) {
    String prefix = "R" + i + "_";

    String[] fields = {
    		"EQUITY", "BONDS", "BOBC", "TRES_BILLS", "REPURCHASE_AGREE", 
     		"COM_PAPER", "CERT_OF_DEP", "PLEDGED_ASSET", "OTHER", "TOTAL"
    };

    for (String field : fields) {
        String getterName = "get" + prefix + field;
        String setterName = "set" + prefix + field;

        try {
            Method getter = M_SECA_Manual_Summary_Entity.class.getMethod(getterName);
            Method setter = M_SECA_Manual_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

            Object newValue = getter.invoke(updatedEntity);
            setter.invoke(existing, newValue);

        } catch (NoSuchMethodException e) {
            // Skip missing fields
            continue;
        }
    }
}

for (int i = 14; i <= 57; i++) {
	if (i == 17 || i == 32 || i == 35 || i == 38 || i == 43 || i == 47 || i == 51) {
    continue;
}
            String prefix = "R" + i + "_";

            String[] fields = {"TOTAL"};
           

            for (String field : fields) {
                String getterName = "get" + prefix + field;
                String setterName = "set" + prefix + field;

                try {
                    Method getter = M_SECA_Manual_Summary_Entity.class.getMethod(getterName);
                    Method setter = M_SECA_Manual_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

                    Object newValue = getter.invoke(updatedEntity);
                    setter.invoke(existing, newValue);

                } catch (NoSuchMethodException e) {
                    // Skip missing fields
                    continue;
                }
            }
        }

}			

	catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // 3️⃣ Save updated entity
	    BRRS_M_SECA_Summary_Repo.save(existing);
	}

//	public ModelAndView getM_SECAcurrentDtl(String reportId, String fromdate, String todate, String currency,
//											  String dtltype, Pageable pageable, String Filter, String type, String version) {
//
//		int pageSize = pageable != null ? pageable.getPageSize() : 10;
//		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
//		int totalPages = 0;
//
//		ModelAndView mv = new ModelAndView();
//		Session hs = sessionFactory.getCurrentSession();
//
//		try {
//			Date parsedDate = null;
//			if (todate != null && !todate.isEmpty()) {
//				parsedDate = dateformat.parse(todate);
//			}
//
//			String rowId = null;
//			String columnId = null;
//
//			// ✅ Split filter string into rowId & columnId
//			if (Filter != null && Filter.contains(",")) {
//				String[] parts = Filter.split(",");
//				if (parts.length >= 2) {
//					rowId = parts[0];
//					columnId = parts[1];
//				}
//			}
//			System.out.println(type);
//			if ("ARCHIVAL".equals(type) && version != null) {
//				System.out.println(type);
//				// 🔹 Archival branch
//				List<BRRS_M_SECA_Archival_Detail_Entity> T1Dt1;
//				if (rowId != null && columnId != null) {
//					T1Dt1 = BRRS_M_SECA_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
//				} else {
//					T1Dt1 = BRRS_M_SECA_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
//				}
//
//				mv.addObject("reportdetails", T1Dt1);
//				mv.addObject("reportmaster12", T1Dt1);
//				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
//
//			} else {
//				// 🔹 Current branch
//				List<BRRS_M_SECA_Detail_Entity> T1Dt1;
//				if (rowId != null && columnId != null) {
//					T1Dt1 = BRRS_M_SECA_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
//				} else {
//					T1Dt1 = BRRS_M_SECA_Detail_Repo.getdatabydateList(parsedDate, currentPage, pageSize);
//					totalPages = BRRS_M_SECA_Detail_Repo.getdatacount(parsedDate);
//					mv.addObject("pagination", "YES");
//				}
//
//				mv.addObject("reportdetails", T1Dt1);
//				mv.addObject("reportmaster12", T1Dt1);
//				System.out.println("LISTCOUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
//			}
//
//		} catch (ParseException e) {
//			e.printStackTrace();
//			mv.addObject("errorMessage", "Invalid date format: " + todate);
//		} catch (Exception e) {
//			e.printStackTrace();
//			mv.addObject("errorMessage", "Unexpected error: " + e.getMessage());
//		}
//
//		// ✅ Common attributes
//		mv.setViewName("BRRS/M_SECA");
//		mv.addObject("displaymode", "Details");
//		mv.addObject("currentPage", currentPage);
//		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
//		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
//		mv.addObject("reportsflag", "reportsflag");
//		mv.addObject("menu", reportId);
//
//		return mv;
//	}


	public byte[] BRRS_M_SECAExcel(String filename, String reportId, String fromdate, String todate, String currency,
									 String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_SECAARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data
		List<M_SECA_Manual_Summary_Entity> dataList = BRRS_M_SECA_Summary_Repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SECA report. Returning empty result.");
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
			int startRow = 12;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SECA_Manual_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);	
					
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
			

					
					row = sheet.getRow(13);
				

					
					//ROW14
					//COLUMN2
					Cell cell2 = row.createCell(1);
					if (record.getR14_EQUITY() != null) {
						cell2.setCellValue(record.getR14_EQUITY().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					//row14
					// Column3
					Cell cell3 = row.createCell(2);
					if (record.getR14_BONDS() != null) {
						cell3.setCellValue(record.getR14_BONDS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row14
					// Column4
					Cell cell4 = row.createCell(3);
					if (record.getR14_BOBC() != null) {
						cell4.setCellValue(record.getR14_BOBC().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row14
					// Column5
					Cell cell5 = row.createCell(4);
					if (record.getR14_TRES_BILLS() != null) {
						cell5.setCellValue(record.getR14_TRES_BILLS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					//row14
					// Column6
					Cell cell6 = row.createCell(5);
					if (record.getR14_REPURCHASE_AGREE() != null) {
						cell6.setCellValue(record.getR14_REPURCHASE_AGREE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					//row14
					// Column7
					Cell cell7 = row.createCell(6);
					if (record.getR14_COM_PAPER() != null) {
						cell7.setCellValue(record.getR14_COM_PAPER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					//row14
					// Column8
					Cell cell8 = row.createCell(7);
					if (record.getR14_CERT_OF_DEP() != null) {
						cell8.setCellValue(record.getR14_CERT_OF_DEP().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					//row14
					// Column9
					Cell cell9 = row.createCell(8);
					if (record.getR14_PLEDGED_ASSET() != null) {
						cell9.setCellValue(record.getR14_PLEDGED_ASSET().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					//row14
					// Column10
					Cell cell10 = row.createCell(9);
					if (record.getR14_OTHER() != null) {
						cell10.setCellValue(record.getR14_OTHER().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					//row14
					// Column11
//					Cell cell11 = row.createCell(10);
//					if (record.getR14_TOTAL() != null) {
//						cell11.setCellValue(record.getR14_TOTAL().doubleValue());
//						cell11.setCellStyle(numberStyle);
//					} else {
//						cell11.setCellValue("");
//						cell11.setCellStyle(textStyle);
//					}
					
					row = sheet.getRow(14);
					
					// ROW15
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR15_EQUITY() != null) {
					    cell2.setCellValue(record.getR15_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR15_BONDS() != null) {
					    cell3.setCellValue(record.getR15_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR15_BOBC() != null) {
					    cell4.setCellValue(record.getR15_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR15_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR15_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR15_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR15_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR15_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR15_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR15_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR15_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR15_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR15_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR15_OTHER() != null) {
					    cell10.setCellValue(record.getR15_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR15_TOTAL() != null) {
//					    cell11.setCellValue(record.getR15_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(15);
					
					
					// ROW16
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR16_EQUITY() != null) {
					    cell2.setCellValue(record.getR16_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR16_BONDS() != null) {
					    cell3.setCellValue(record.getR16_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR16_BOBC() != null) {
					    cell4.setCellValue(record.getR16_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR16_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR16_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR16_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR16_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR16_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR16_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR16_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR16_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR16_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR16_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR16_OTHER() != null) {
					    cell10.setCellValue(record.getR16_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR16_TOTAL() != null) {
//					    cell11.setCellValue(record.getR16_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(16);
					
					
					// ROW17
					// COLUMN2
//					cell2 = row.createCell(1);
//					if (record.getR17_EQUITY() != null) {
//					    cell2.setCellValue(record.getR17_EQUITY().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}
//					// COLUMN3
//					cell3 = row.createCell(2);
//					if (record.getR17_BONDS() != null) {
//					    cell3.setCellValue(record.getR17_BONDS().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//					// COLUMN4
//					cell4 = row.createCell(3);
//					if (record.getR17_BOBC() != null) {
//					    cell4.setCellValue(record.getR17_BOBC().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
//					// COLUMN5
//					cell5 = row.createCell(4);
//					if (record.getR17_TRES_BILLS() != null) {
//					    cell5.setCellValue(record.getR17_TRES_BILLS().doubleValue());
//					    cell5.setCellStyle(numberStyle);
//					} else {
//					    cell5.setCellValue("");
//					    cell5.setCellStyle(textStyle);
//					}
//					// COLUMN6
//					cell6 = row.createCell(5);
//					if (record.getR17_REPURCHASE_AGREE() != null) {
//					    cell6.setCellValue(record.getR17_REPURCHASE_AGREE().doubleValue());
//					    cell6.setCellStyle(numberStyle);
//					} else {
//					    cell6.setCellValue("");
//					    cell6.setCellStyle(textStyle);
//					}
//					// COLUMN7
//					cell7 = row.createCell(6);
//					if (record.getR17_COM_PAPER() != null) {
//					    cell7.setCellValue(record.getR17_COM_PAPER().doubleValue());
//					    cell7.setCellStyle(numberStyle);
//					} else {
//					    cell7.setCellValue("");
//					    cell7.setCellStyle(textStyle);
//					}
//					// COLUMN8
//					cell8 = row.createCell(7);
//					if (record.getR17_CERT_OF_DEP() != null) {
//					    cell8.setCellValue(record.getR17_CERT_OF_DEP().doubleValue());
//					    cell8.setCellStyle(numberStyle);
//					} else {
//					    cell8.setCellValue("");
//					    cell8.setCellStyle(textStyle);
//					}
//					// COLUMN9
//					cell9 = row.createCell(8);
//					if (record.getR17_PLEDGED_ASSET() != null) {
//					    cell9.setCellValue(record.getR17_PLEDGED_ASSET().doubleValue());
//					    cell9.setCellStyle(numberStyle);
//					} else {
//					    cell9.setCellValue("");
//					    cell9.setCellStyle(textStyle);
//					}
//					// COLUMN10
//					cell10 = row.createCell(9);
//					if (record.getR17_OTHER() != null) {
//					    cell10.setCellValue(record.getR17_OTHER().doubleValue());
//					    cell10.setCellStyle(numberStyle);
//					} else {
//					    cell10.setCellValue("");
//					    cell10.setCellStyle(textStyle);
//					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR17_TOTAL() != null) {
//					    cell11.setCellValue(record.getR17_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(17);

					// ROW18
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR18_EQUITY() != null) {
					    cell2.setCellValue(record.getR18_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR18_BONDS() != null) {
					    cell3.setCellValue(record.getR18_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR18_BOBC() != null) {
					    cell4.setCellValue(record.getR18_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR18_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR18_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR18_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR18_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR18_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR18_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR18_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR18_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR18_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR18_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR18_OTHER() != null) {
					    cell10.setCellValue(record.getR18_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR18_TOTAL() != null) {
//					    cell11.setCellValue(record.getR18_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(18);

					// ROW19
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR19_EQUITY() != null) {
					    cell2.setCellValue(record.getR19_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR19_BONDS() != null) {
					    cell3.setCellValue(record.getR19_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR19_BOBC() != null) {
					    cell4.setCellValue(record.getR19_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR19_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR19_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR19_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR19_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR19_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR19_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR19_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR19_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR19_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR19_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR19_OTHER() != null) {
					    cell10.setCellValue(record.getR19_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR19_TOTAL() != null) {
//					    cell11.setCellValue(record.getR19_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(19);

					// ROW20
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR20_EQUITY() != null) {
					    cell2.setCellValue(record.getR20_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR20_BONDS() != null) {
					    cell3.setCellValue(record.getR20_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR20_BOBC() != null) {
					    cell4.setCellValue(record.getR20_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR20_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR20_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR20_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR20_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR20_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR20_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR20_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR20_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR20_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR20_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR20_OTHER() != null) {
					    cell10.setCellValue(record.getR20_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR20_TOTAL() != null) {
//					    cell11.setCellValue(record.getR20_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(20);
					
					
					// ROW21
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR21_EQUITY() != null) {
					    cell2.setCellValue(record.getR21_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR21_BONDS() != null) {
					    cell3.setCellValue(record.getR21_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR21_BOBC() != null) {
					    cell4.setCellValue(record.getR21_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR21_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR21_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR21_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR21_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR21_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR21_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR21_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR21_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR21_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR21_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR21_OTHER() != null) {
					    cell10.setCellValue(record.getR21_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR21_TOTAL() != null) {
//					    cell11.setCellValue(record.getR21_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(21);

					// ROW22
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR22_EQUITY() != null) {
					    cell2.setCellValue(record.getR22_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR22_BONDS() != null) {
					    cell3.setCellValue(record.getR22_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR22_BOBC() != null) {
					    cell4.setCellValue(record.getR22_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR22_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR22_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR22_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR22_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR22_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR22_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR22_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR22_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR22_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR22_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR22_OTHER() != null) {
					    cell10.setCellValue(record.getR22_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR22_TOTAL() != null) {
//					    cell11.setCellValue(record.getR22_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(22);

					
					
					// ROW23
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR23_EQUITY() != null) {
					    cell2.setCellValue(record.getR23_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR23_BONDS() != null) {
					    cell3.setCellValue(record.getR23_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR23_BOBC() != null) {
					    cell4.setCellValue(record.getR23_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR23_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR23_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR23_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR23_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR23_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR23_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR23_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR23_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR23_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR23_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR23_OTHER() != null) {
					    cell10.setCellValue(record.getR23_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR23_TOTAL() != null) {
//					    cell11.setCellValue(record.getR23_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(23);

					// ROW24
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR24_EQUITY() != null) {
					    cell2.setCellValue(record.getR24_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR24_BONDS() != null) {
					    cell3.setCellValue(record.getR24_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR24_BOBC() != null) {
					    cell4.setCellValue(record.getR24_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR24_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR24_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR24_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR24_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR24_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR24_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR24_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR24_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR24_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR24_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR24_OTHER() != null) {
					    cell10.setCellValue(record.getR24_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR24_TOTAL() != null) {
//					    cell11.setCellValue(record.getR24_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(24);

					
					// ROW25
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR25_EQUITY() != null) {
					    cell2.setCellValue(record.getR25_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR25_BONDS() != null) {
					    cell3.setCellValue(record.getR25_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR25_BOBC() != null) {
					    cell4.setCellValue(record.getR25_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR25_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR25_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR25_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR25_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR25_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR25_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR25_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR25_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR25_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR25_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR25_OTHER() != null) {
					    cell10.setCellValue(record.getR25_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR25_TOTAL() != null) {
//					    cell11.setCellValue(record.getR25_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(25);

					// ROW26
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR26_EQUITY() != null) {
					    cell2.setCellValue(record.getR26_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR26_BONDS() != null) {
					    cell3.setCellValue(record.getR26_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR26_BOBC() != null) {
					    cell4.setCellValue(record.getR26_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR26_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR26_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR26_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR26_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR26_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR26_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR26_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR26_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR26_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR26_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR26_OTHER() != null) {
					    cell10.setCellValue(record.getR26_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR26_TOTAL() != null) {
//					    cell11.setCellValue(record.getR26_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(26);

					
					// ROW27
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR27_EQUITY() != null) {
					    cell2.setCellValue(record.getR27_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR27_BONDS() != null) {
					    cell3.setCellValue(record.getR27_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR27_BOBC() != null) {
					    cell4.setCellValue(record.getR27_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR27_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR27_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR27_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR27_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR27_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR27_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR27_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR27_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR27_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR27_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR27_OTHER() != null) {
					    cell10.setCellValue(record.getR27_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR27_TOTAL() != null) {
//					    cell11.setCellValue(record.getR27_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(27);

					// ROW28
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR28_EQUITY() != null) {
					    cell2.setCellValue(record.getR28_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR28_BONDS() != null) {
					    cell3.setCellValue(record.getR28_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR28_BOBC() != null) {
					    cell4.setCellValue(record.getR28_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR28_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR28_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR28_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR28_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR28_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR28_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR28_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR28_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR28_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR28_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR28_OTHER() != null) {
					    cell10.setCellValue(record.getR28_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR28_TOTAL() != null) {
//					    cell11.setCellValue(record.getR28_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(28);

					// ROW29
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR29_EQUITY() != null) {
					    cell2.setCellValue(record.getR29_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR29_BONDS() != null) {
					    cell3.setCellValue(record.getR29_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR29_BOBC() != null) {
					    cell4.setCellValue(record.getR29_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR29_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR29_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR29_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR29_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR29_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR29_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR29_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR29_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR29_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR29_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR29_OTHER() != null) {
					    cell10.setCellValue(record.getR29_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR29_TOTAL() != null) {
//					    cell11.setCellValue(record.getR29_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(29);

					// ROW30
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR30_EQUITY() != null) {
					    cell2.setCellValue(record.getR30_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR30_BONDS() != null) {
					    cell3.setCellValue(record.getR30_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR30_BOBC() != null) {
					    cell4.setCellValue(record.getR30_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR30_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR30_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR30_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR30_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR30_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR30_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR30_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR30_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR30_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR30_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR30_OTHER() != null) {
					    cell10.setCellValue(record.getR30_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR30_TOTAL() != null) {
//					    cell11.setCellValue(record.getR30_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(30);
					
					// ROW31
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR31_EQUITY() != null) {
					    cell2.setCellValue(record.getR31_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR31_BONDS() != null) {
					    cell3.setCellValue(record.getR31_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR31_BOBC() != null) {
					    cell4.setCellValue(record.getR31_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR31_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR31_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR31_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR31_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR31_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR31_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR31_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR31_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR31_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR31_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR31_OTHER() != null) {
					    cell10.setCellValue(record.getR31_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR31_TOTAL() != null) {
//					    cell11.setCellValue(record.getR31_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(31);

					// ROW32
					// COLUMN2
//					cell2 = row.createCell(1);
//					if (record.getR32_EQUITY() != null) {
//					    cell2.setCellValue(record.getR32_EQUITY().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}
//					// COLUMN3
//					cell3 = row.createCell(2);
//					if (record.getR32_BONDS() != null) {
//					    cell3.setCellValue(record.getR32_BONDS().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//					// COLUMN4
//					cell4 = row.createCell(3);
//					if (record.getR32_BOBC() != null) {
//					    cell4.setCellValue(record.getR32_BOBC().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
//					// COLUMN5
//					cell5 = row.createCell(4);
//					if (record.getR32_TRES_BILLS() != null) {
//					    cell5.setCellValue(record.getR32_TRES_BILLS().doubleValue());
//					    cell5.setCellStyle(numberStyle);
//					} else {
//					    cell5.setCellValue("");
//					    cell5.setCellStyle(textStyle);
//					}
//					// COLUMN6
//					cell6 = row.createCell(5);
//					if (record.getR32_REPURCHASE_AGREE() != null) {
//					    cell6.setCellValue(record.getR32_REPURCHASE_AGREE().doubleValue());
//					    cell6.setCellStyle(numberStyle);
//					} else {
//					    cell6.setCellValue("");
//					    cell6.setCellStyle(textStyle);
//					}
//					// COLUMN7
//					cell7 = row.createCell(6);
//					if (record.getR32_COM_PAPER() != null) {
//					    cell7.setCellValue(record.getR32_COM_PAPER().doubleValue());
//					    cell7.setCellStyle(numberStyle);
//					} else {
//					    cell7.setCellValue("");
//					    cell7.setCellStyle(textStyle);
//					}
//					// COLUMN8
//					cell8 = row.createCell(7);
//					if (record.getR32_CERT_OF_DEP() != null) {
//					    cell8.setCellValue(record.getR32_CERT_OF_DEP().doubleValue());
//					    cell8.setCellStyle(numberStyle);
//					} else {
//					    cell8.setCellValue("");
//					    cell8.setCellStyle(textStyle);
//					}
//					// COLUMN9
//					cell9 = row.createCell(8);
//					if (record.getR32_PLEDGED_ASSET() != null) {
//					    cell9.setCellValue(record.getR32_PLEDGED_ASSET().doubleValue());
//					    cell9.setCellStyle(numberStyle);
//					} else {
//					    cell9.setCellValue("");
//					    cell9.setCellStyle(textStyle);
//					}
//					// COLUMN10
//					cell10 = row.createCell(9);
//					if (record.getR32_OTHER() != null) {
//					    cell10.setCellValue(record.getR32_OTHER().doubleValue());
//					    cell10.setCellStyle(numberStyle);
//					} else {
//					    cell10.setCellValue("");
//					    cell10.setCellStyle(textStyle);
//					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR32_TOTAL() != null) {
//					    cell11.setCellValue(record.getR32_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(32);

					
					
					// ROW33
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR33_EQUITY() != null) {
					    cell2.setCellValue(record.getR33_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR33_BONDS() != null) {
					    cell3.setCellValue(record.getR33_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR33_BOBC() != null) {
					    cell4.setCellValue(record.getR33_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR33_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR33_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR33_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR33_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR33_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR33_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR33_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR33_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR33_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR33_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR33_OTHER() != null) {
					    cell10.setCellValue(record.getR33_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR33_TOTAL() != null) {
//					    cell11.setCellValue(record.getR33_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(33);

					// ROW34
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR34_EQUITY() != null) {
					    cell2.setCellValue(record.getR34_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR34_BONDS() != null) {
					    cell3.setCellValue(record.getR34_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR34_BOBC() != null) {
					    cell4.setCellValue(record.getR34_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR34_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR34_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR34_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR34_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR34_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR34_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR34_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR34_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR34_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR34_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR34_OTHER() != null) {
					    cell10.setCellValue(record.getR34_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR34_TOTAL() != null) {
//					    cell11.setCellValue(record.getR34_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(34);

					
					
					// ROW35
					// COLUMN2
//					cell2 = row.createCell(1);
//					if (record.getR35_EQUITY() != null) {
//					    cell2.setCellValue(record.getR35_EQUITY().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}
//					// COLUMN3
//					cell3 = row.createCell(2);
//					if (record.getR35_BONDS() != null) {
//					    cell3.setCellValue(record.getR35_BONDS().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//					// COLUMN4
//					cell4 = row.createCell(3);
//					if (record.getR35_BOBC() != null) {
//					    cell4.setCellValue(record.getR35_BOBC().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
//					// COLUMN5
//					cell5 = row.createCell(4);
//					if (record.getR35_TRES_BILLS() != null) {
//					    cell5.setCellValue(record.getR35_TRES_BILLS().doubleValue());
//					    cell5.setCellStyle(numberStyle);
//					} else {
//					    cell5.setCellValue("");
//					    cell5.setCellStyle(textStyle);
//					}
//					// COLUMN6
//					cell6 = row.createCell(5);
//					if (record.getR35_REPURCHASE_AGREE() != null) {
//					    cell6.setCellValue(record.getR35_REPURCHASE_AGREE().doubleValue());
//					    cell6.setCellStyle(numberStyle);
//					} else {
//					    cell6.setCellValue("");
//					    cell6.setCellStyle(textStyle);
//					}
//					// COLUMN7
//					cell7 = row.createCell(6);
//					if (record.getR35_COM_PAPER() != null) {
//					    cell7.setCellValue(record.getR35_COM_PAPER().doubleValue());
//					    cell7.setCellStyle(numberStyle);
//					} else {
//					    cell7.setCellValue("");
//					    cell7.setCellStyle(textStyle);
//					}
//					// COLUMN8
//					cell8 = row.createCell(7);
//					if (record.getR35_CERT_OF_DEP() != null) {
//					    cell8.setCellValue(record.getR35_CERT_OF_DEP().doubleValue());
//					    cell8.setCellStyle(numberStyle);
//					} else {
//					    cell8.setCellValue("");
//					    cell8.setCellStyle(textStyle);
//					}
//					// COLUMN9
//					cell9 = row.createCell(8);
//					if (record.getR35_PLEDGED_ASSET() != null) {
//					    cell9.setCellValue(record.getR35_PLEDGED_ASSET().doubleValue());
//					    cell9.setCellStyle(numberStyle);
//					} else {
//					    cell9.setCellValue("");
//					    cell9.setCellStyle(textStyle);
//					}
//					// COLUMN10
//					cell10 = row.createCell(9);
//					if (record.getR35_OTHER() != null) {
//					    cell10.setCellValue(record.getR35_OTHER().doubleValue());
//					    cell10.setCellStyle(numberStyle);
//					} else {
//					    cell10.setCellValue("");
//					    cell10.setCellStyle(textStyle);
//					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR35_TOTAL() != null) {
//					    cell11.setCellValue(record.getR35_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(35);

					// ROW36
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR36_EQUITY() != null) {
					    cell2.setCellValue(record.getR36_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
//					// COLUMN3
//					cell3 = row.createCell(2);
//					if (record.getR36_BONDS() != null) {
//					    cell3.setCellValue(record.getR36_BONDS().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//					// COLUMN4
//					cell4 = row.createCell(3);
//					if (record.getR36_BOBC() != null) {
//					    cell4.setCellValue(record.getR36_BOBC().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR36_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR36_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR36_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR36_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR36_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR36_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR36_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR36_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR36_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR36_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR36_OTHER() != null) {
					    cell10.setCellValue(record.getR36_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR36_TOTAL() != null) {
//					    cell11.setCellValue(record.getR36_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(36);

					
					
					// ROW37
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR37_EQUITY() != null) {
					    cell2.setCellValue(record.getR37_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR37_BONDS() != null) {
					    cell3.setCellValue(record.getR37_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR37_BOBC() != null) {
					    cell4.setCellValue(record.getR37_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR37_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR37_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR37_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR37_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR37_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR37_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR37_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR37_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR37_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR37_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR37_OTHER() != null) {
					    cell10.setCellValue(record.getR37_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR37_TOTAL() != null) {
//					    cell11.setCellValue(record.getR37_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(37);

					// ROW38
					// COLUMN2
//					cell2 = row.createCell(1);
//					if (record.getR38_EQUITY() != null) {
//					    cell2.setCellValue(record.getR38_EQUITY().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}
//					// COLUMN3
//					cell3 = row.createCell(2);
//					if (record.getR38_BONDS() != null) {
//					    cell3.setCellValue(record.getR38_BONDS().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//					// COLUMN4
//					cell4 = row.createCell(3);
//					if (record.getR38_BOBC() != null) {
//					    cell4.setCellValue(record.getR38_BOBC().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
//					// COLUMN5
//					cell5 = row.createCell(4);
//					if (record.getR38_TRES_BILLS() != null) {
//					    cell5.setCellValue(record.getR38_TRES_BILLS().doubleValue());
//					    cell5.setCellStyle(numberStyle);
//					} else {
//					    cell5.setCellValue("");
//					    cell5.setCellStyle(textStyle);
//					}
//					// COLUMN6
//					cell6 = row.createCell(5);
//					if (record.getR38_REPURCHASE_AGREE() != null) {
//					    cell6.setCellValue(record.getR38_REPURCHASE_AGREE().doubleValue());
//					    cell6.setCellStyle(numberStyle);
//					} else {
//					    cell6.setCellValue("");
//					    cell6.setCellStyle(textStyle);
//					}
//					// COLUMN7
//					cell7 = row.createCell(6);
//					if (record.getR38_COM_PAPER() != null) {
//					    cell7.setCellValue(record.getR38_COM_PAPER().doubleValue());
//					    cell7.setCellStyle(numberStyle);
//					} else {
//					    cell7.setCellValue("");
//					    cell7.setCellStyle(textStyle);
//					}
//					// COLUMN8
//					cell8 = row.createCell(7);
//					if (record.getR38_CERT_OF_DEP() != null) {
//					    cell8.setCellValue(record.getR38_CERT_OF_DEP().doubleValue());
//					    cell8.setCellStyle(numberStyle);
//					} else {
//					    cell8.setCellValue("");
//					    cell8.setCellStyle(textStyle);
//					}
//					// COLUMN9
//					cell9 = row.createCell(8);
//					if (record.getR38_PLEDGED_ASSET() != null) {
//					    cell9.setCellValue(record.getR38_PLEDGED_ASSET().doubleValue());
//					    cell9.setCellStyle(numberStyle);
//					} else {
//					    cell9.setCellValue("");
//					    cell9.setCellStyle(textStyle);
//					}
//					// COLUMN10
//					cell10 = row.createCell(9);
//					if (record.getR38_OTHER() != null) {
//					    cell10.setCellValue(record.getR38_OTHER().doubleValue());
//					    cell10.setCellStyle(numberStyle);
//					} else {
//					    cell10.setCellValue("");
//					    cell10.setCellStyle(textStyle);
//					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR38_TOTAL() != null) {
//					    cell11.setCellValue(record.getR38_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(38);

					
					
					// ROW39
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR39_EQUITY() != null) {
					    cell2.setCellValue(record.getR39_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR39_BONDS() != null) {
					    cell3.setCellValue(record.getR39_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR39_BOBC() != null) {
					    cell4.setCellValue(record.getR39_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR39_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR39_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR39_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR39_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR39_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR39_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR39_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR39_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR39_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR39_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR39_OTHER() != null) {
					    cell10.setCellValue(record.getR39_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR39_TOTAL() != null) {
//					    cell11.setCellValue(record.getR39_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(39);

					// ROW40
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR40_EQUITY() != null) {
					    cell2.setCellValue(record.getR40_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR40_BONDS() != null) {
					    cell3.setCellValue(record.getR40_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR40_BOBC() != null) {
					    cell4.setCellValue(record.getR40_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR40_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR40_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR40_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR40_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR40_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR40_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR40_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR40_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR40_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR40_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR40_OTHER() != null) {
					    cell10.setCellValue(record.getR40_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR40_TOTAL() != null) {
//					    cell11.setCellValue(record.getR40_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(40);

					
					
					// ROW41
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR41_EQUITY() != null) {
					    cell2.setCellValue(record.getR41_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR41_BONDS() != null) {
					    cell3.setCellValue(record.getR41_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR41_BOBC() != null) {
					    cell4.setCellValue(record.getR41_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR41_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR41_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR41_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR41_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR41_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR41_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR41_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR41_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR41_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR41_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR41_OTHER() != null) {
					    cell10.setCellValue(record.getR41_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR41_TOTAL() != null) {
//					    cell11.setCellValue(record.getR41_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(41);

					// ROW42
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR42_EQUITY() != null) {
					    cell2.setCellValue(record.getR42_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR42_BONDS() != null) {
					    cell3.setCellValue(record.getR42_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR42_BOBC() != null) {
					    cell4.setCellValue(record.getR42_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR42_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR42_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR42_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR42_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR42_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR42_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR42_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR42_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR42_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR42_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR42_OTHER() != null) {
					    cell10.setCellValue(record.getR42_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR42_TOTAL() != null) {
//					    cell11.setCellValue(record.getR42_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(42);
					
					
					// ROW43
					// COLUMN2
//					cell2 = row.createCell(1);
//					if (record.getR43_EQUITY() != null) {
//					    cell2.setCellValue(record.getR43_EQUITY().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}
//					// COLUMN3
//					cell3 = row.createCell(2);
//					if (record.getR43_BONDS() != null) {
//					    cell3.setCellValue(record.getR43_BONDS().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//					// COLUMN4
//					cell4 = row.createCell(3);
//					if (record.getR43_BOBC() != null) {
//					    cell4.setCellValue(record.getR43_BOBC().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
//					// COLUMN5
//					cell5 = row.createCell(4);
//					if (record.getR43_TRES_BILLS() != null) {
//					    cell5.setCellValue(record.getR43_TRES_BILLS().doubleValue());
//					    cell5.setCellStyle(numberStyle);
//					} else {
//					    cell5.setCellValue("");
//					    cell5.setCellStyle(textStyle);
//					}
//					// COLUMN6
//					cell6 = row.createCell(5);
//					if (record.getR43_REPURCHASE_AGREE() != null) {
//					    cell6.setCellValue(record.getR43_REPURCHASE_AGREE().doubleValue());
//					    cell6.setCellStyle(numberStyle);
//					} else {
//					    cell6.setCellValue("");
//					    cell6.setCellStyle(textStyle);
//					}
//					// COLUMN7
//					cell7 = row.createCell(6);
//					if (record.getR43_COM_PAPER() != null) {
//					    cell7.setCellValue(record.getR43_COM_PAPER().doubleValue());
//					    cell7.setCellStyle(numberStyle);
//					} else {
//					    cell7.setCellValue("");
//					    cell7.setCellStyle(textStyle);
//					}
//					// COLUMN8
//					cell8 = row.createCell(7);
//					if (record.getR43_CERT_OF_DEP() != null) {
//					    cell8.setCellValue(record.getR43_CERT_OF_DEP().doubleValue());
//					    cell8.setCellStyle(numberStyle);
//					} else {
//					    cell8.setCellValue("");
//					    cell8.setCellStyle(textStyle);
//					}
//					// COLUMN9
//					cell9 = row.createCell(8);
//					if (record.getR43_PLEDGED_ASSET() != null) {
//					    cell9.setCellValue(record.getR43_PLEDGED_ASSET().doubleValue());
//					    cell9.setCellStyle(numberStyle);
//					} else {
//					    cell9.setCellValue("");
//					    cell9.setCellStyle(textStyle);
//					}
//					// COLUMN10
//					cell10 = row.createCell(9);
//					if (record.getR43_OTHER() != null) {
//					    cell10.setCellValue(record.getR43_OTHER().doubleValue());
//					    cell10.setCellStyle(numberStyle);
//					} else {
//					    cell10.setCellValue("");
//					    cell10.setCellStyle(textStyle);
//					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR43_TOTAL() != null) {
//					    cell11.setCellValue(record.getR43_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(43);

					// ROW44
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR44_EQUITY() != null) {
					    cell2.setCellValue(record.getR44_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR44_BONDS() != null) {
					    cell3.setCellValue(record.getR44_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR44_BOBC() != null) {
					    cell4.setCellValue(record.getR44_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR44_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR44_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR44_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR44_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR44_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR44_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR44_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR44_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR44_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR44_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR44_OTHER() != null) {
					    cell10.setCellValue(record.getR44_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR44_TOTAL() != null) {
//					    cell11.setCellValue(record.getR44_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(44);

					
					// ROW45
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR45_EQUITY() != null) {
					    cell2.setCellValue(record.getR45_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR45_BONDS() != null) {
					    cell3.setCellValue(record.getR45_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR45_BOBC() != null) {
					    cell4.setCellValue(record.getR45_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR45_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR45_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR45_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR45_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR45_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR45_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR45_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR45_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR45_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR45_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR45_OTHER() != null) {
					    cell10.setCellValue(record.getR45_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR45_TOTAL() != null) {
//					    cell11.setCellValue(record.getR45_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(45);

					// ROW46
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR46_EQUITY() != null) {
					    cell2.setCellValue(record.getR46_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR46_BONDS() != null) {
					    cell3.setCellValue(record.getR46_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR46_BOBC() != null) {
					    cell4.setCellValue(record.getR46_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR46_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR46_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR46_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR46_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR46_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR46_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR46_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR46_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR46_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR46_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR46_OTHER() != null) {
					    cell10.setCellValue(record.getR46_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR46_TOTAL() != null) {
//					    cell11.setCellValue(record.getR46_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(46);

					
					
					// ROW47
					// COLUMN2
//					cell2 = row.createCell(1);
//					if (record.getR47_EQUITY() != null) {
//					    cell2.setCellValue(record.getR47_EQUITY().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}
//					// COLUMN3
//					cell3 = row.createCell(2);
//					if (record.getR47_BONDS() != null) {
//					    cell3.setCellValue(record.getR47_BONDS().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//					// COLUMN4
//					cell4 = row.createCell(3);
//					if (record.getR47_BOBC() != null) {
//					    cell4.setCellValue(record.getR47_BOBC().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
//					// COLUMN5
//					cell5 = row.createCell(4);
//					if (record.getR47_TRES_BILLS() != null) {
//					    cell5.setCellValue(record.getR47_TRES_BILLS().doubleValue());
//					    cell5.setCellStyle(numberStyle);
//					} else {
//					    cell5.setCellValue("");
//					    cell5.setCellStyle(textStyle);
//					}
//					// COLUMN6
//					cell6 = row.createCell(5);
//					if (record.getR47_REPURCHASE_AGREE() != null) {
//					    cell6.setCellValue(record.getR47_REPURCHASE_AGREE().doubleValue());
//					    cell6.setCellStyle(numberStyle);
//					} else {
//					    cell6.setCellValue("");
//					    cell6.setCellStyle(textStyle);
//					}
//					// COLUMN7
//					cell7 = row.createCell(6);
//					if (record.getR47_COM_PAPER() != null) {
//					    cell7.setCellValue(record.getR47_COM_PAPER().doubleValue());
//					    cell7.setCellStyle(numberStyle);
//					} else {
//					    cell7.setCellValue("");
//					    cell7.setCellStyle(textStyle);
//					}
//					// COLUMN8
//					cell8 = row.createCell(7);
//					if (record.getR47_CERT_OF_DEP() != null) {
//					    cell8.setCellValue(record.getR47_CERT_OF_DEP().doubleValue());
//					    cell8.setCellStyle(numberStyle);
//					} else {
//					    cell8.setCellValue("");
//					    cell8.setCellStyle(textStyle);
//					}
//					// COLUMN9
//					cell9 = row.createCell(8);
//					if (record.getR47_PLEDGED_ASSET() != null) {
//					    cell9.setCellValue(record.getR47_PLEDGED_ASSET().doubleValue());
//					    cell9.setCellStyle(numberStyle);
//					} else {
//					    cell9.setCellValue("");
//					    cell9.setCellStyle(textStyle);
//					}
//					// COLUMN10
//					cell10 = row.createCell(9);
//					if (record.getR47_OTHER() != null) {
//					    cell10.setCellValue(record.getR47_OTHER().doubleValue());
//					    cell10.setCellStyle(numberStyle);
//					} else {
//					    cell10.setCellValue("");
//					    cell10.setCellStyle(textStyle);
//					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR47_TOTAL() != null) {
//					    cell11.setCellValue(record.getR47_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(47);

					// ROW48
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR48_EQUITY() != null) {
					    cell2.setCellValue(record.getR48_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR48_BONDS() != null) {
					    cell3.setCellValue(record.getR48_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR48_BOBC() != null) {
					    cell4.setCellValue(record.getR48_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR48_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR48_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR48_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR48_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR48_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR48_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR48_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR48_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR48_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR48_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR48_OTHER() != null) {
					    cell10.setCellValue(record.getR48_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR48_TOTAL() != null) {
//					    cell11.setCellValue(record.getR48_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(48);

					
					
					// ROW49
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR49_EQUITY() != null) {
					    cell2.setCellValue(record.getR49_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR49_BONDS() != null) {
					    cell3.setCellValue(record.getR49_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR49_BOBC() != null) {
					    cell4.setCellValue(record.getR49_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR49_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR49_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR49_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR49_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR49_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR49_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR49_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR49_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR49_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR49_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR49_OTHER() != null) {
					    cell10.setCellValue(record.getR49_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR49_TOTAL() != null) {
//					    cell11.setCellValue(record.getR49_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(49);

					// ROW50
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR50_EQUITY() != null) {
					    cell2.setCellValue(record.getR50_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR50_BONDS() != null) {
					    cell3.setCellValue(record.getR50_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR50_BOBC() != null) {
					    cell4.setCellValue(record.getR50_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR50_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR50_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR50_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR50_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR50_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR50_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR50_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR50_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR50_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR50_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR50_OTHER() != null) {
					    cell10.setCellValue(record.getR50_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR50_TOTAL() != null) {
//					    cell11.setCellValue(record.getR50_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(50);

					
					
					// ROW51
					// COLUMN2
//					cell2 = row.createCell(1);
//					if (record.getR51_EQUITY() != null) {
//					    cell2.setCellValue(record.getR51_EQUITY().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}
//					// COLUMN3
//					cell3 = row.createCell(2);
//					if (record.getR51_BONDS() != null) {
//					    cell3.setCellValue(record.getR51_BONDS().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//					// COLUMN4
//					cell4 = row.createCell(3);
//					if (record.getR51_BOBC() != null) {
//					    cell4.setCellValue(record.getR51_BOBC().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
//					// COLUMN5
//					cell5 = row.createCell(4);
//					if (record.getR51_TRES_BILLS() != null) {
//					    cell5.setCellValue(record.getR51_TRES_BILLS().doubleValue());
//					    cell5.setCellStyle(numberStyle);
//					} else {
//					    cell5.setCellValue("");
//					    cell5.setCellStyle(textStyle);
//					}
//					// COLUMN6
//					cell6 = row.createCell(5);
//					if (record.getR51_REPURCHASE_AGREE() != null) {
//					    cell6.setCellValue(record.getR51_REPURCHASE_AGREE().doubleValue());
//					    cell6.setCellStyle(numberStyle);
//					} else {
//					    cell6.setCellValue("");
//					    cell6.setCellStyle(textStyle);
//					}
//					// COLUMN7
//					cell7 = row.createCell(6);
//					if (record.getR51_COM_PAPER() != null) {
//					    cell7.setCellValue(record.getR51_COM_PAPER().doubleValue());
//					    cell7.setCellStyle(numberStyle);
//					} else {
//					    cell7.setCellValue("");
//					    cell7.setCellStyle(textStyle);
//					}
//					// COLUMN8
//					cell8 = row.createCell(7);
//					if (record.getR51_CERT_OF_DEP() != null) {
//					    cell8.setCellValue(record.getR51_CERT_OF_DEP().doubleValue());
//					    cell8.setCellStyle(numberStyle);
//					} else {
//					    cell8.setCellValue("");
//					    cell8.setCellStyle(textStyle);
//					}
//					// COLUMN9
//					cell9 = row.createCell(8);
//					if (record.getR51_PLEDGED_ASSET() != null) {
//					    cell9.setCellValue(record.getR51_PLEDGED_ASSET().doubleValue());
//					    cell9.setCellStyle(numberStyle);
//					} else {
//					    cell9.setCellValue("");
//					    cell9.setCellStyle(textStyle);
//					}
//					// COLUMN10
//					cell10 = row.createCell(9);
//					if (record.getR51_OTHER() != null) {
//					    cell10.setCellValue(record.getR51_OTHER().doubleValue());
//					    cell10.setCellStyle(numberStyle);
//					} else {
//					    cell10.setCellValue("");
//					    cell10.setCellStyle(textStyle);
//					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR51_TOTAL() != null) {
//					    cell11.setCellValue(record.getR51_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(51);

					// ROW52
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR52_EQUITY() != null) {
					    cell2.setCellValue(record.getR52_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR52_BONDS() != null) {
					    cell3.setCellValue(record.getR52_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR52_BOBC() != null) {
					    cell4.setCellValue(record.getR52_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR52_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR52_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR52_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR52_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR52_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR52_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR52_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR52_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR52_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR52_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR52_OTHER() != null) {
					    cell10.setCellValue(record.getR52_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR52_TOTAL() != null) {
//					    cell11.setCellValue(record.getR52_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(52);

					
					
					// ROW53
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR53_EQUITY() != null) {
					    cell2.setCellValue(record.getR53_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR53_BONDS() != null) {
					    cell3.setCellValue(record.getR53_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR53_BOBC() != null) {
					    cell4.setCellValue(record.getR53_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR53_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR53_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR53_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR53_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR53_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR53_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR53_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR53_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR53_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR53_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR53_OTHER() != null) {
					    cell10.setCellValue(record.getR53_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR53_TOTAL() != null) {
//					    cell11.setCellValue(record.getR53_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(53);

					// ROW54
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR54_EQUITY() != null) {
					    cell2.setCellValue(record.getR54_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR54_BONDS() != null) {
					    cell3.setCellValue(record.getR54_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR54_BOBC() != null) {
					    cell4.setCellValue(record.getR54_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR54_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR54_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR54_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR54_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR54_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR54_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR54_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR54_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR54_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR54_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR54_OTHER() != null) {
					    cell10.setCellValue(record.getR54_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR54_TOTAL() != null) {
//					    cell11.setCellValue(record.getR54_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(54);

					
					
					// ROW55
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR55_EQUITY() != null) {
					    cell2.setCellValue(record.getR55_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR55_BONDS() != null) {
					    cell3.setCellValue(record.getR55_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR55_BOBC() != null) {
					    cell4.setCellValue(record.getR55_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR55_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR55_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR55_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR55_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR55_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR55_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR55_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR55_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR55_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR55_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR55_OTHER() != null) {
					    cell10.setCellValue(record.getR55_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR55_TOTAL() != null) {
//					    cell11.setCellValue(record.getR55_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(55);

					// ROW56
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR56_EQUITY() != null) {
					    cell2.setCellValue(record.getR56_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR56_BONDS() != null) {
					    cell3.setCellValue(record.getR56_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR56_BOBC() != null) {
					    cell4.setCellValue(record.getR56_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR56_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR56_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR56_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR56_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR56_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR56_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR56_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR56_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR56_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR56_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR56_OTHER() != null) {
					    cell10.setCellValue(record.getR56_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR56_TOTAL() != null) {
//					    cell11.setCellValue(record.getR56_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(56);

					
					
					// ROW57
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR57_EQUITY() != null) {
					    cell2.setCellValue(record.getR57_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR57_BONDS() != null) {
					    cell3.setCellValue(record.getR57_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR57_BOBC() != null) {
					    cell4.setCellValue(record.getR57_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR57_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR57_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR57_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR57_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR57_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR57_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR57_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR57_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR57_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR57_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR57_OTHER() != null) {
					    cell10.setCellValue(record.getR57_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR57_TOTAL() != null) {
//					    cell11.setCellValue(record.getR57_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(57);

					// ROW58
					// COLUMN2
//					cell2 = row.createCell(1);
//					if (record.getR58_EQUITY() != null) {
//					    cell2.setCellValue(record.getR58_EQUITY().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}
//					// COLUMN3
//					cell3 = row.createCell(2);
//					if (record.getR58_BONDS() != null) {
//					    cell3.setCellValue(record.getR58_BONDS().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//					// COLUMN4
//					cell4 = row.createCell(3);
//					if (record.getR58_BOBC() != null) {
//					    cell4.setCellValue(record.getR58_BOBC().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
//					// COLUMN5
//					cell5 = row.createCell(4);
//					if (record.getR58_TRES_BILLS() != null) {
//					    cell5.setCellValue(record.getR58_TRES_BILLS().doubleValue());
//					    cell5.setCellStyle(numberStyle);
//					} else {
//					    cell5.setCellValue("");
//					    cell5.setCellStyle(textStyle);
//					}
//					// COLUMN6
//					cell6 = row.createCell(5);
//					if (record.getR58_REPURCHASE_AGREE() != null) {
//					    cell6.setCellValue(record.getR58_REPURCHASE_AGREE().doubleValue());
//					    cell6.setCellStyle(numberStyle);
//					} else {
//					    cell6.setCellValue("");
//					    cell6.setCellStyle(textStyle);
//					}
//					// COLUMN7
//					cell7 = row.createCell(6);
//					if (record.getR58_COM_PAPER() != null) {
//					    cell7.setCellValue(record.getR58_COM_PAPER().doubleValue());
//					    cell7.setCellStyle(numberStyle);
//					} else {
//					    cell7.setCellValue("");
//					    cell7.setCellStyle(textStyle);
//					}
//					// COLUMN8
//					cell8 = row.createCell(7);
//					if (record.getR58_CERT_OF_DEP() != null) {
//					    cell8.setCellValue(record.getR58_CERT_OF_DEP().doubleValue());
//					    cell8.setCellStyle(numberStyle);
//					} else {
//					    cell8.setCellValue("");
//					    cell8.setCellStyle(textStyle);
//					}
//					// COLUMN9
//					cell9 = row.createCell(8);
//					if (record.getR58_PLEDGED_ASSET() != null) {
//					    cell9.setCellValue(record.getR58_PLEDGED_ASSET().doubleValue());
//					    cell9.setCellStyle(numberStyle);
//					} else {
//					    cell9.setCellValue("");
//					    cell9.setCellStyle(textStyle);
//					}
//					// COLUMN10
//					cell10 = row.createCell(9);
//					if (record.getR58_OTHER() != null) {
//					    cell10.setCellValue(record.getR58_OTHER().doubleValue());
//					    cell10.setCellStyle(numberStyle);
//					} else {
//					    cell10.setCellValue("");
//					    cell10.setCellStyle(textStyle);
//					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR58_TOTAL() != null) {
//					    cell11.setCellValue(record.getR58_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(58);

					
					
					// ROW59
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR59_EQUITY() != null) {
					    cell2.setCellValue(record.getR59_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR59_BONDS() != null) {
					    cell3.setCellValue(record.getR59_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR59_BOBC() != null) {
					    cell4.setCellValue(record.getR59_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR59_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR59_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR59_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR59_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR59_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR59_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR59_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR59_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR59_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR59_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR59_OTHER() != null) {
					    cell10.setCellValue(record.getR59_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR59_TOTAL() != null) {
//					    cell11.setCellValue(record.getR59_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

			


					

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

//	public byte[] BRRS_M_SECADetailExcel(String filename, String fromdate, String todate, String currency,
//										   String dtltype, String type, String version) {
//
//		try {
//			logger.info("Generating Excel for BRRS_M_SECA Details...");
//			System.out.println("came to Detail download service");
//			if (type.equals("ARCHIVAL") & version != null) {
//				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
//						version);
//				return ARCHIVALreport;
//			}
//			XSSFWorkbook workbook = new XSSFWorkbook();
//			XSSFSheet sheet = workbook.createSheet("BRRS_M_SECADetails");
//
//			// Common border style
//			BorderStyle border = BorderStyle.THIN;
//			// Header style (left aligned)
//			CellStyle headerStyle = workbook.createCellStyle();
//			Font headerFont = workbook.createFont();
//			headerFont.setBold(true);
//			headerFont.setFontHeightInPoints((short) 10);
//			headerStyle.setFont(headerFont);
//			headerStyle.setAlignment(HorizontalAlignment.LEFT);
//			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//			headerStyle.setBorderTop(border);
//			headerStyle.setBorderBottom(border);
//			headerStyle.setBorderLeft(border);
//			headerStyle.setBorderRight(border);
//
//			// Right-aligned header style for ACCT BALANCE
//			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
//			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
//			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);
//
//			// Default data style (left aligned)
//			CellStyle dataStyle = workbook.createCellStyle();
//			dataStyle.setAlignment(HorizontalAlignment.LEFT);
//			dataStyle.setBorderTop(border);
//			dataStyle.setBorderBottom(border);
//			dataStyle.setBorderLeft(border);
//			dataStyle.setBorderRight(border);
//
//			// ACCT BALANCE style (right aligned with 3 decimals)
//			CellStyle balanceStyle = workbook.createCellStyle();
//			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
//			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
//			balanceStyle.setBorderTop(border);
//			balanceStyle.setBorderBottom(border);
//			balanceStyle.setBorderLeft(border);
//			balanceStyle.setBorderRight(border);
//			// Header row
//			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "ROWID", "COLUMNID",
//					"REPORT_DATE" };
//			XSSFRow headerRow = sheet.createRow(0);
//			for (int i = 0; i < headers.length; i++) {
//				Cell cell = headerRow.createCell(i);
//				cell.setCellValue(headers[i]);
//				if (i == 3) { // ACCT BALANCE
//					cell.setCellStyle(rightAlignedHeaderStyle);
//				} else {
//					cell.setCellStyle(headerStyle);
//				}
//				sheet.setColumnWidth(i, 5000);
//			}
//			// Get data
//			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
//			List<BRRS_M_SECA_Detail_Entity> reportData = BRRS_M_SECA_Detail_Repo.getdatabydateList(parsedToDate);
//			if (reportData != null && !reportData.isEmpty()) {
//				int rowIndex = 1;
//				for (BRRS_M_SECA_Detail_Entity item : reportData) {
//					XSSFRow row = sheet.createRow(rowIndex++);
//					row.createCell(0).setCellValue(item.getCUST_ID());
//					row.createCell(1).setCellValue(item.getACCT_NUMBER());
//					row.createCell(2).setCellValue(item.getACCT_NAME());
//					// ACCT BALANCE (right aligned, 3 decimal places)
//					Cell balanceCell = row.createCell(3);
//					if (item.getACCT_BALANCE_IN_PULA() != null) {
//						balanceCell.setCellValue(item.getACCT_BALANCE_IN_PULA().doubleValue());
//					} else {
//						balanceCell.setCellValue(0.000);
//					}
//					balanceCell.setCellStyle(balanceStyle);
//					row.createCell(4).setCellValue(item.getROW_ID());
//					row.createCell(5).setCellValue(item.getCOLUMN_ID());
//					row.createCell(6)
//							.setCellValue(item.getREPORT_DATE() != null
//									? new SimpleDateFormat("dd-MM-yyyy").format(item.getREPORT_DATE())
//									: "");
//					// Apply data style for all other cells
//					for (int j = 0; j < 7; j++) {
//						if (j != 3) {
//							row.getCell(j).setCellStyle(dataStyle);
//						}
//					}
//				}
//			} else {
//				logger.info("No data found for BRRS_M_SECA — only header will be written.");
//			}
//			// Write to byte[]
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			workbook.write(bos);
//			workbook.close();
//			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
//			return bos.toByteArray();
//		} catch (Exception e) {
//			logger.error("Error generating BRRS_M_SECA Excel", e);
//			return new byte[0];
//		}
//	}

	public List<Object> getM_SECAArchival() {
		List<Object> M_SECAArchivallist = new ArrayList<>();
		try {
			M_SECAArchivallist = BRRS_M_SECA_Archival_Summary_Repo.getM_SECAarchival();
			System.out.println("countser" + M_SECAArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_SECA Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_SECAArchivallist;
	}

	public byte[] getExcelM_SECAARCHIVAL(String filename, String reportId, String fromdate, String todate,
										   String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_SECA_Manual_Archival_Summary_Entity> dataList = BRRS_M_SECA_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SECA report. Returning empty result.");
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

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SECA_Manual_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}


					row = sheet.getRow(13);
				

					
					//ROW14
					//COLUMN2
					Cell cell2 = row.createCell(1);
					if (record.getR14_EQUITY() != null) {
						cell2.setCellValue(record.getR14_EQUITY().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					//row14
					// Column3
					Cell cell3 = row.createCell(2);
					if (record.getR14_BONDS() != null) {
						cell3.setCellValue(record.getR14_BONDS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row14
					// Column4
					Cell cell4 = row.createCell(3);
					if (record.getR14_BOBC() != null) {
						cell4.setCellValue(record.getR14_BOBC().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row14
					// Column5
					Cell cell5 = row.createCell(4);
					if (record.getR14_TRES_BILLS() != null) {
						cell5.setCellValue(record.getR14_TRES_BILLS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					//row14
					// Column6
					Cell cell6 = row.createCell(5);
					if (record.getR14_REPURCHASE_AGREE() != null) {
						cell6.setCellValue(record.getR14_REPURCHASE_AGREE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					//row14
					// Column7
					Cell cell7 = row.createCell(6);
					if (record.getR14_COM_PAPER() != null) {
						cell7.setCellValue(record.getR14_COM_PAPER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					//row14
					// Column8
					Cell cell8 = row.createCell(7);
					if (record.getR14_CERT_OF_DEP() != null) {
						cell8.setCellValue(record.getR14_CERT_OF_DEP().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					//row14
					// Column9
					Cell cell9 = row.createCell(8);
					if (record.getR14_PLEDGED_ASSET() != null) {
						cell9.setCellValue(record.getR14_PLEDGED_ASSET().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					//row14
					// Column10
					Cell cell10 = row.createCell(9);
					if (record.getR14_OTHER() != null) {
						cell10.setCellValue(record.getR14_OTHER().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					//row14
					// Column11
//					Cell cell11 = row.createCell(10);
//					if (record.getR14_TOTAL() != null) {
//						cell11.setCellValue(record.getR14_TOTAL().doubleValue());
//						cell11.setCellStyle(numberStyle);
//					} else {
//						cell11.setCellValue("");
//						cell11.setCellStyle(textStyle);
//					}
					
					row = sheet.getRow(14);
					
					// ROW15
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR15_EQUITY() != null) {
					    cell2.setCellValue(record.getR15_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR15_BONDS() != null) {
					    cell3.setCellValue(record.getR15_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR15_BOBC() != null) {
					    cell4.setCellValue(record.getR15_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR15_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR15_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR15_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR15_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR15_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR15_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR15_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR15_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR15_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR15_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR15_OTHER() != null) {
					    cell10.setCellValue(record.getR15_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR15_TOTAL() != null) {
//					    cell11.setCellValue(record.getR15_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(15);
					
					
					// ROW16
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR16_EQUITY() != null) {
					    cell2.setCellValue(record.getR16_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR16_BONDS() != null) {
					    cell3.setCellValue(record.getR16_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR16_BOBC() != null) {
					    cell4.setCellValue(record.getR16_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR16_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR16_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR16_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR16_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR16_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR16_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR16_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR16_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR16_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR16_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR16_OTHER() != null) {
					    cell10.setCellValue(record.getR16_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR16_TOTAL() != null) {
//					    cell11.setCellValue(record.getR16_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(16);
					
					
					// ROW17
					// COLUMN2
//					cell2 = row.createCell(1);
//					if (record.getR17_EQUITY() != null) {
//					    cell2.setCellValue(record.getR17_EQUITY().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}
//					// COLUMN3
//					cell3 = row.createCell(2);
//					if (record.getR17_BONDS() != null) {
//					    cell3.setCellValue(record.getR17_BONDS().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//					// COLUMN4
//					cell4 = row.createCell(3);
//					if (record.getR17_BOBC() != null) {
//					    cell4.setCellValue(record.getR17_BOBC().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
//					// COLUMN5
//					cell5 = row.createCell(4);
//					if (record.getR17_TRES_BILLS() != null) {
//					    cell5.setCellValue(record.getR17_TRES_BILLS().doubleValue());
//					    cell5.setCellStyle(numberStyle);
//					} else {
//					    cell5.setCellValue("");
//					    cell5.setCellStyle(textStyle);
//					}
//					// COLUMN6
//					cell6 = row.createCell(5);
//					if (record.getR17_REPURCHASE_AGREE() != null) {
//					    cell6.setCellValue(record.getR17_REPURCHASE_AGREE().doubleValue());
//					    cell6.setCellStyle(numberStyle);
//					} else {
//					    cell6.setCellValue("");
//					    cell6.setCellStyle(textStyle);
//					}
//					// COLUMN7
//					cell7 = row.createCell(6);
//					if (record.getR17_COM_PAPER() != null) {
//					    cell7.setCellValue(record.getR17_COM_PAPER().doubleValue());
//					    cell7.setCellStyle(numberStyle);
//					} else {
//					    cell7.setCellValue("");
//					    cell7.setCellStyle(textStyle);
//					}
//					// COLUMN8
//					cell8 = row.createCell(7);
//					if (record.getR17_CERT_OF_DEP() != null) {
//					    cell8.setCellValue(record.getR17_CERT_OF_DEP().doubleValue());
//					    cell8.setCellStyle(numberStyle);
//					} else {
//					    cell8.setCellValue("");
//					    cell8.setCellStyle(textStyle);
//					}
//					// COLUMN9
//					cell9 = row.createCell(8);
//					if (record.getR17_PLEDGED_ASSET() != null) {
//					    cell9.setCellValue(record.getR17_PLEDGED_ASSET().doubleValue());
//					    cell9.setCellStyle(numberStyle);
//					} else {
//					    cell9.setCellValue("");
//					    cell9.setCellStyle(textStyle);
//					}
//					// COLUMN10
//					cell10 = row.createCell(9);
//					if (record.getR17_OTHER() != null) {
//					    cell10.setCellValue(record.getR17_OTHER().doubleValue());
//					    cell10.setCellStyle(numberStyle);
//					} else {
//					    cell10.setCellValue("");
//					    cell10.setCellStyle(textStyle);
//					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR17_TOTAL() != null) {
//					    cell11.setCellValue(record.getR17_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(17);

					// ROW18
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR18_EQUITY() != null) {
					    cell2.setCellValue(record.getR18_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR18_BONDS() != null) {
					    cell3.setCellValue(record.getR18_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR18_BOBC() != null) {
					    cell4.setCellValue(record.getR18_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR18_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR18_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR18_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR18_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR18_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR18_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR18_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR18_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR18_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR18_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR18_OTHER() != null) {
					    cell10.setCellValue(record.getR18_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR18_TOTAL() != null) {
//					    cell11.setCellValue(record.getR18_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(18);

					// ROW19
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR19_EQUITY() != null) {
					    cell2.setCellValue(record.getR19_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR19_BONDS() != null) {
					    cell3.setCellValue(record.getR19_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR19_BOBC() != null) {
					    cell4.setCellValue(record.getR19_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR19_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR19_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR19_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR19_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR19_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR19_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR19_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR19_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR19_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR19_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR19_OTHER() != null) {
					    cell10.setCellValue(record.getR19_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR19_TOTAL() != null) {
//					    cell11.setCellValue(record.getR19_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(19);

					// ROW20
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR20_EQUITY() != null) {
					    cell2.setCellValue(record.getR20_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR20_BONDS() != null) {
					    cell3.setCellValue(record.getR20_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR20_BOBC() != null) {
					    cell4.setCellValue(record.getR20_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR20_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR20_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR20_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR20_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR20_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR20_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR20_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR20_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR20_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR20_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR20_OTHER() != null) {
					    cell10.setCellValue(record.getR20_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR20_TOTAL() != null) {
//					    cell11.setCellValue(record.getR20_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(20);
					
					
					// ROW21
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR21_EQUITY() != null) {
					    cell2.setCellValue(record.getR21_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR21_BONDS() != null) {
					    cell3.setCellValue(record.getR21_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR21_BOBC() != null) {
					    cell4.setCellValue(record.getR21_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR21_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR21_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR21_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR21_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR21_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR21_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR21_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR21_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR21_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR21_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR21_OTHER() != null) {
					    cell10.setCellValue(record.getR21_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR21_TOTAL() != null) {
//					    cell11.setCellValue(record.getR21_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(21);

					// ROW22
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR22_EQUITY() != null) {
					    cell2.setCellValue(record.getR22_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR22_BONDS() != null) {
					    cell3.setCellValue(record.getR22_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR22_BOBC() != null) {
					    cell4.setCellValue(record.getR22_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR22_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR22_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR22_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR22_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR22_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR22_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR22_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR22_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR22_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR22_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR22_OTHER() != null) {
					    cell10.setCellValue(record.getR22_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR22_TOTAL() != null) {
//					    cell11.setCellValue(record.getR22_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(22);

					
					
					// ROW23
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR23_EQUITY() != null) {
					    cell2.setCellValue(record.getR23_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR23_BONDS() != null) {
					    cell3.setCellValue(record.getR23_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR23_BOBC() != null) {
					    cell4.setCellValue(record.getR23_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR23_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR23_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR23_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR23_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR23_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR23_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR23_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR23_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR23_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR23_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR23_OTHER() != null) {
					    cell10.setCellValue(record.getR23_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR23_TOTAL() != null) {
//					    cell11.setCellValue(record.getR23_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(23);

					// ROW24
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR24_EQUITY() != null) {
					    cell2.setCellValue(record.getR24_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR24_BONDS() != null) {
					    cell3.setCellValue(record.getR24_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR24_BOBC() != null) {
					    cell4.setCellValue(record.getR24_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR24_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR24_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR24_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR24_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR24_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR24_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR24_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR24_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR24_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR24_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR24_OTHER() != null) {
					    cell10.setCellValue(record.getR24_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR24_TOTAL() != null) {
//					    cell11.setCellValue(record.getR24_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(24);

					
					// ROW25
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR25_EQUITY() != null) {
					    cell2.setCellValue(record.getR25_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR25_BONDS() != null) {
					    cell3.setCellValue(record.getR25_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR25_BOBC() != null) {
					    cell4.setCellValue(record.getR25_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR25_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR25_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR25_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR25_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR25_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR25_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR25_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR25_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR25_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR25_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR25_OTHER() != null) {
					    cell10.setCellValue(record.getR25_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR25_TOTAL() != null) {
//					    cell11.setCellValue(record.getR25_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(25);

					// ROW26
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR26_EQUITY() != null) {
					    cell2.setCellValue(record.getR26_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR26_BONDS() != null) {
					    cell3.setCellValue(record.getR26_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR26_BOBC() != null) {
					    cell4.setCellValue(record.getR26_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR26_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR26_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR26_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR26_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR26_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR26_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR26_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR26_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR26_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR26_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR26_OTHER() != null) {
					    cell10.setCellValue(record.getR26_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR26_TOTAL() != null) {
//					    cell11.setCellValue(record.getR26_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(26);

					
					// ROW27
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR27_EQUITY() != null) {
					    cell2.setCellValue(record.getR27_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR27_BONDS() != null) {
					    cell3.setCellValue(record.getR27_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR27_BOBC() != null) {
					    cell4.setCellValue(record.getR27_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR27_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR27_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR27_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR27_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR27_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR27_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR27_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR27_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR27_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR27_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR27_OTHER() != null) {
					    cell10.setCellValue(record.getR27_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR27_TOTAL() != null) {
//					    cell11.setCellValue(record.getR27_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(27);

					// ROW28
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR28_EQUITY() != null) {
					    cell2.setCellValue(record.getR28_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR28_BONDS() != null) {
					    cell3.setCellValue(record.getR28_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR28_BOBC() != null) {
					    cell4.setCellValue(record.getR28_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR28_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR28_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR28_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR28_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR28_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR28_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR28_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR28_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR28_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR28_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR28_OTHER() != null) {
					    cell10.setCellValue(record.getR28_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR28_TOTAL() != null) {
//					    cell11.setCellValue(record.getR28_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(28);

					// ROW29
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR29_EQUITY() != null) {
					    cell2.setCellValue(record.getR29_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR29_BONDS() != null) {
					    cell3.setCellValue(record.getR29_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR29_BOBC() != null) {
					    cell4.setCellValue(record.getR29_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR29_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR29_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR29_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR29_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR29_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR29_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR29_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR29_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR29_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR29_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR29_OTHER() != null) {
					    cell10.setCellValue(record.getR29_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR29_TOTAL() != null) {
//					    cell11.setCellValue(record.getR29_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(29);

					// ROW30
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR30_EQUITY() != null) {
					    cell2.setCellValue(record.getR30_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR30_BONDS() != null) {
					    cell3.setCellValue(record.getR30_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR30_BOBC() != null) {
					    cell4.setCellValue(record.getR30_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR30_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR30_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR30_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR30_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR30_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR30_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR30_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR30_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR30_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR30_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR30_OTHER() != null) {
					    cell10.setCellValue(record.getR30_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR30_TOTAL() != null) {
//					    cell11.setCellValue(record.getR30_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(30);
					
					// ROW31
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR31_EQUITY() != null) {
					    cell2.setCellValue(record.getR31_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR31_BONDS() != null) {
					    cell3.setCellValue(record.getR31_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR31_BOBC() != null) {
					    cell4.setCellValue(record.getR31_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR31_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR31_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR31_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR31_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR31_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR31_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR31_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR31_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR31_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR31_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR31_OTHER() != null) {
					    cell10.setCellValue(record.getR31_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR31_TOTAL() != null) {
//					    cell11.setCellValue(record.getR31_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(31);

					// ROW32
					// COLUMN2
//					cell2 = row.createCell(1);
//					if (record.getR32_EQUITY() != null) {
//					    cell2.setCellValue(record.getR32_EQUITY().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}
//					// COLUMN3
//					cell3 = row.createCell(2);
//					if (record.getR32_BONDS() != null) {
//					    cell3.setCellValue(record.getR32_BONDS().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//					// COLUMN4
//					cell4 = row.createCell(3);
//					if (record.getR32_BOBC() != null) {
//					    cell4.setCellValue(record.getR32_BOBC().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
//					// COLUMN5
//					cell5 = row.createCell(4);
//					if (record.getR32_TRES_BILLS() != null) {
//					    cell5.setCellValue(record.getR32_TRES_BILLS().doubleValue());
//					    cell5.setCellStyle(numberStyle);
//					} else {
//					    cell5.setCellValue("");
//					    cell5.setCellStyle(textStyle);
//					}
//					// COLUMN6
//					cell6 = row.createCell(5);
//					if (record.getR32_REPURCHASE_AGREE() != null) {
//					    cell6.setCellValue(record.getR32_REPURCHASE_AGREE().doubleValue());
//					    cell6.setCellStyle(numberStyle);
//					} else {
//					    cell6.setCellValue("");
//					    cell6.setCellStyle(textStyle);
//					}
//					// COLUMN7
//					cell7 = row.createCell(6);
//					if (record.getR32_COM_PAPER() != null) {
//					    cell7.setCellValue(record.getR32_COM_PAPER().doubleValue());
//					    cell7.setCellStyle(numberStyle);
//					} else {
//					    cell7.setCellValue("");
//					    cell7.setCellStyle(textStyle);
//					}
//					// COLUMN8
//					cell8 = row.createCell(7);
//					if (record.getR32_CERT_OF_DEP() != null) {
//					    cell8.setCellValue(record.getR32_CERT_OF_DEP().doubleValue());
//					    cell8.setCellStyle(numberStyle);
//					} else {
//					    cell8.setCellValue("");
//					    cell8.setCellStyle(textStyle);
//					}
//					// COLUMN9
//					cell9 = row.createCell(8);
//					if (record.getR32_PLEDGED_ASSET() != null) {
//					    cell9.setCellValue(record.getR32_PLEDGED_ASSET().doubleValue());
//					    cell9.setCellStyle(numberStyle);
//					} else {
//					    cell9.setCellValue("");
//					    cell9.setCellStyle(textStyle);
//					}
//					// COLUMN10
//					cell10 = row.createCell(9);
//					if (record.getR32_OTHER() != null) {
//					    cell10.setCellValue(record.getR32_OTHER().doubleValue());
//					    cell10.setCellStyle(numberStyle);
//					} else {
//					    cell10.setCellValue("");
//					    cell10.setCellStyle(textStyle);
//					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR32_TOTAL() != null) {
//					    cell11.setCellValue(record.getR32_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(32);

					
					
					// ROW33
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR33_EQUITY() != null) {
					    cell2.setCellValue(record.getR33_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR33_BONDS() != null) {
					    cell3.setCellValue(record.getR33_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR33_BOBC() != null) {
					    cell4.setCellValue(record.getR33_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR33_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR33_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR33_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR33_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR33_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR33_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR33_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR33_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR33_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR33_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR33_OTHER() != null) {
					    cell10.setCellValue(record.getR33_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR33_TOTAL() != null) {
//					    cell11.setCellValue(record.getR33_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(33);

					// ROW34
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR34_EQUITY() != null) {
					    cell2.setCellValue(record.getR34_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR34_BONDS() != null) {
					    cell3.setCellValue(record.getR34_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR34_BOBC() != null) {
					    cell4.setCellValue(record.getR34_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR34_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR34_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR34_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR34_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR34_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR34_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR34_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR34_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR34_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR34_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR34_OTHER() != null) {
					    cell10.setCellValue(record.getR34_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR34_TOTAL() != null) {
//					    cell11.setCellValue(record.getR34_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(34);

					
					
					// ROW35
					// COLUMN2
//					cell2 = row.createCell(1);
//					if (record.getR35_EQUITY() != null) {
//					    cell2.setCellValue(record.getR35_EQUITY().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}
//					// COLUMN3
//					cell3 = row.createCell(2);
//					if (record.getR35_BONDS() != null) {
//					    cell3.setCellValue(record.getR35_BONDS().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//					// COLUMN4
//					cell4 = row.createCell(3);
//					if (record.getR35_BOBC() != null) {
//					    cell4.setCellValue(record.getR35_BOBC().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
//					// COLUMN5
//					cell5 = row.createCell(4);
//					if (record.getR35_TRES_BILLS() != null) {
//					    cell5.setCellValue(record.getR35_TRES_BILLS().doubleValue());
//					    cell5.setCellStyle(numberStyle);
//					} else {
//					    cell5.setCellValue("");
//					    cell5.setCellStyle(textStyle);
//					}
//					// COLUMN6
//					cell6 = row.createCell(5);
//					if (record.getR35_REPURCHASE_AGREE() != null) {
//					    cell6.setCellValue(record.getR35_REPURCHASE_AGREE().doubleValue());
//					    cell6.setCellStyle(numberStyle);
//					} else {
//					    cell6.setCellValue("");
//					    cell6.setCellStyle(textStyle);
//					}
//					// COLUMN7
//					cell7 = row.createCell(6);
//					if (record.getR35_COM_PAPER() != null) {
//					    cell7.setCellValue(record.getR35_COM_PAPER().doubleValue());
//					    cell7.setCellStyle(numberStyle);
//					} else {
//					    cell7.setCellValue("");
//					    cell7.setCellStyle(textStyle);
//					}
//					// COLUMN8
//					cell8 = row.createCell(7);
//					if (record.getR35_CERT_OF_DEP() != null) {
//					    cell8.setCellValue(record.getR35_CERT_OF_DEP().doubleValue());
//					    cell8.setCellStyle(numberStyle);
//					} else {
//					    cell8.setCellValue("");
//					    cell8.setCellStyle(textStyle);
//					}
//					// COLUMN9
//					cell9 = row.createCell(8);
//					if (record.getR35_PLEDGED_ASSET() != null) {
//					    cell9.setCellValue(record.getR35_PLEDGED_ASSET().doubleValue());
//					    cell9.setCellStyle(numberStyle);
//					} else {
//					    cell9.setCellValue("");
//					    cell9.setCellStyle(textStyle);
//					}
//					// COLUMN10
//					cell10 = row.createCell(9);
//					if (record.getR35_OTHER() != null) {
//					    cell10.setCellValue(record.getR35_OTHER().doubleValue());
//					    cell10.setCellStyle(numberStyle);
//					} else {
//					    cell10.setCellValue("");
//					    cell10.setCellStyle(textStyle);
//					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR35_TOTAL() != null) {
//					    cell11.setCellValue(record.getR35_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(35);

					// ROW36
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR36_EQUITY() != null) {
					    cell2.setCellValue(record.getR36_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
//					// COLUMN3
//					cell3 = row.createCell(2);
//					if (record.getR36_BONDS() != null) {
//					    cell3.setCellValue(record.getR36_BONDS().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//					// COLUMN4
//					cell4 = row.createCell(3);
//					if (record.getR36_BOBC() != null) {
//					    cell4.setCellValue(record.getR36_BOBC().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR36_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR36_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR36_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR36_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR36_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR36_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR36_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR36_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR36_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR36_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR36_OTHER() != null) {
					    cell10.setCellValue(record.getR36_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR36_TOTAL() != null) {
//					    cell11.setCellValue(record.getR36_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(36);

					
					
					// ROW37
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR37_EQUITY() != null) {
					    cell2.setCellValue(record.getR37_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR37_BONDS() != null) {
					    cell3.setCellValue(record.getR37_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR37_BOBC() != null) {
					    cell4.setCellValue(record.getR37_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR37_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR37_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR37_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR37_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR37_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR37_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR37_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR37_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR37_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR37_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR37_OTHER() != null) {
					    cell10.setCellValue(record.getR37_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR37_TOTAL() != null) {
//					    cell11.setCellValue(record.getR37_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(37);

					// ROW38
					// COLUMN2
//					cell2 = row.createCell(1);
//					if (record.getR38_EQUITY() != null) {
//					    cell2.setCellValue(record.getR38_EQUITY().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}
//					// COLUMN3
//					cell3 = row.createCell(2);
//					if (record.getR38_BONDS() != null) {
//					    cell3.setCellValue(record.getR38_BONDS().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//					// COLUMN4
//					cell4 = row.createCell(3);
//					if (record.getR38_BOBC() != null) {
//					    cell4.setCellValue(record.getR38_BOBC().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
//					// COLUMN5
//					cell5 = row.createCell(4);
//					if (record.getR38_TRES_BILLS() != null) {
//					    cell5.setCellValue(record.getR38_TRES_BILLS().doubleValue());
//					    cell5.setCellStyle(numberStyle);
//					} else {
//					    cell5.setCellValue("");
//					    cell5.setCellStyle(textStyle);
//					}
//					// COLUMN6
//					cell6 = row.createCell(5);
//					if (record.getR38_REPURCHASE_AGREE() != null) {
//					    cell6.setCellValue(record.getR38_REPURCHASE_AGREE().doubleValue());
//					    cell6.setCellStyle(numberStyle);
//					} else {
//					    cell6.setCellValue("");
//					    cell6.setCellStyle(textStyle);
//					}
//					// COLUMN7
//					cell7 = row.createCell(6);
//					if (record.getR38_COM_PAPER() != null) {
//					    cell7.setCellValue(record.getR38_COM_PAPER().doubleValue());
//					    cell7.setCellStyle(numberStyle);
//					} else {
//					    cell7.setCellValue("");
//					    cell7.setCellStyle(textStyle);
//					}
//					// COLUMN8
//					cell8 = row.createCell(7);
//					if (record.getR38_CERT_OF_DEP() != null) {
//					    cell8.setCellValue(record.getR38_CERT_OF_DEP().doubleValue());
//					    cell8.setCellStyle(numberStyle);
//					} else {
//					    cell8.setCellValue("");
//					    cell8.setCellStyle(textStyle);
//					}
//					// COLUMN9
//					cell9 = row.createCell(8);
//					if (record.getR38_PLEDGED_ASSET() != null) {
//					    cell9.setCellValue(record.getR38_PLEDGED_ASSET().doubleValue());
//					    cell9.setCellStyle(numberStyle);
//					} else {
//					    cell9.setCellValue("");
//					    cell9.setCellStyle(textStyle);
//					}
//					// COLUMN10
//					cell10 = row.createCell(9);
//					if (record.getR38_OTHER() != null) {
//					    cell10.setCellValue(record.getR38_OTHER().doubleValue());
//					    cell10.setCellStyle(numberStyle);
//					} else {
//					    cell10.setCellValue("");
//					    cell10.setCellStyle(textStyle);
//					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR38_TOTAL() != null) {
//					    cell11.setCellValue(record.getR38_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(38);

					
					
					// ROW39
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR39_EQUITY() != null) {
					    cell2.setCellValue(record.getR39_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR39_BONDS() != null) {
					    cell3.setCellValue(record.getR39_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR39_BOBC() != null) {
					    cell4.setCellValue(record.getR39_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR39_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR39_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR39_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR39_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR39_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR39_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR39_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR39_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR39_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR39_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR39_OTHER() != null) {
					    cell10.setCellValue(record.getR39_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR39_TOTAL() != null) {
//					    cell11.setCellValue(record.getR39_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(39);

					// ROW40
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR40_EQUITY() != null) {
					    cell2.setCellValue(record.getR40_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR40_BONDS() != null) {
					    cell3.setCellValue(record.getR40_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR40_BOBC() != null) {
					    cell4.setCellValue(record.getR40_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR40_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR40_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR40_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR40_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR40_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR40_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR40_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR40_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR40_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR40_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR40_OTHER() != null) {
					    cell10.setCellValue(record.getR40_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR40_TOTAL() != null) {
//					    cell11.setCellValue(record.getR40_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(40);

					
					
					// ROW41
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR41_EQUITY() != null) {
					    cell2.setCellValue(record.getR41_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR41_BONDS() != null) {
					    cell3.setCellValue(record.getR41_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR41_BOBC() != null) {
					    cell4.setCellValue(record.getR41_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR41_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR41_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR41_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR41_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR41_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR41_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR41_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR41_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR41_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR41_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR41_OTHER() != null) {
					    cell10.setCellValue(record.getR41_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR41_TOTAL() != null) {
//					    cell11.setCellValue(record.getR41_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(41);

					// ROW42
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR42_EQUITY() != null) {
					    cell2.setCellValue(record.getR42_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR42_BONDS() != null) {
					    cell3.setCellValue(record.getR42_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR42_BOBC() != null) {
					    cell4.setCellValue(record.getR42_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR42_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR42_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR42_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR42_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR42_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR42_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR42_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR42_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR42_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR42_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR42_OTHER() != null) {
					    cell10.setCellValue(record.getR42_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR42_TOTAL() != null) {
//					    cell11.setCellValue(record.getR42_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(42);
					
					
					// ROW43
					// COLUMN2
//					cell2 = row.createCell(1);
//					if (record.getR43_EQUITY() != null) {
//					    cell2.setCellValue(record.getR43_EQUITY().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}
//					// COLUMN3
//					cell3 = row.createCell(2);
//					if (record.getR43_BONDS() != null) {
//					    cell3.setCellValue(record.getR43_BONDS().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//					// COLUMN4
//					cell4 = row.createCell(3);
//					if (record.getR43_BOBC() != null) {
//					    cell4.setCellValue(record.getR43_BOBC().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
//					// COLUMN5
//					cell5 = row.createCell(4);
//					if (record.getR43_TRES_BILLS() != null) {
//					    cell5.setCellValue(record.getR43_TRES_BILLS().doubleValue());
//					    cell5.setCellStyle(numberStyle);
//					} else {
//					    cell5.setCellValue("");
//					    cell5.setCellStyle(textStyle);
//					}
//					// COLUMN6
//					cell6 = row.createCell(5);
//					if (record.getR43_REPURCHASE_AGREE() != null) {
//					    cell6.setCellValue(record.getR43_REPURCHASE_AGREE().doubleValue());
//					    cell6.setCellStyle(numberStyle);
//					} else {
//					    cell6.setCellValue("");
//					    cell6.setCellStyle(textStyle);
//					}
//					// COLUMN7
//					cell7 = row.createCell(6);
//					if (record.getR43_COM_PAPER() != null) {
//					    cell7.setCellValue(record.getR43_COM_PAPER().doubleValue());
//					    cell7.setCellStyle(numberStyle);
//					} else {
//					    cell7.setCellValue("");
//					    cell7.setCellStyle(textStyle);
//					}
//					// COLUMN8
//					cell8 = row.createCell(7);
//					if (record.getR43_CERT_OF_DEP() != null) {
//					    cell8.setCellValue(record.getR43_CERT_OF_DEP().doubleValue());
//					    cell8.setCellStyle(numberStyle);
//					} else {
//					    cell8.setCellValue("");
//					    cell8.setCellStyle(textStyle);
//					}
//					// COLUMN9
//					cell9 = row.createCell(8);
//					if (record.getR43_PLEDGED_ASSET() != null) {
//					    cell9.setCellValue(record.getR43_PLEDGED_ASSET().doubleValue());
//					    cell9.setCellStyle(numberStyle);
//					} else {
//					    cell9.setCellValue("");
//					    cell9.setCellStyle(textStyle);
//					}
//					// COLUMN10
//					cell10 = row.createCell(9);
//					if (record.getR43_OTHER() != null) {
//					    cell10.setCellValue(record.getR43_OTHER().doubleValue());
//					    cell10.setCellStyle(numberStyle);
//					} else {
//					    cell10.setCellValue("");
//					    cell10.setCellStyle(textStyle);
//					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR43_TOTAL() != null) {
//					    cell11.setCellValue(record.getR43_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(43);

					// ROW44
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR44_EQUITY() != null) {
					    cell2.setCellValue(record.getR44_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR44_BONDS() != null) {
					    cell3.setCellValue(record.getR44_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR44_BOBC() != null) {
					    cell4.setCellValue(record.getR44_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR44_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR44_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR44_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR44_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR44_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR44_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR44_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR44_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR44_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR44_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR44_OTHER() != null) {
					    cell10.setCellValue(record.getR44_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR44_TOTAL() != null) {
//					    cell11.setCellValue(record.getR44_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(44);

					
					// ROW45
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR45_EQUITY() != null) {
					    cell2.setCellValue(record.getR45_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR45_BONDS() != null) {
					    cell3.setCellValue(record.getR45_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR45_BOBC() != null) {
					    cell4.setCellValue(record.getR45_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR45_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR45_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR45_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR45_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR45_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR45_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR45_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR45_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR45_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR45_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR45_OTHER() != null) {
					    cell10.setCellValue(record.getR45_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR45_TOTAL() != null) {
//					    cell11.setCellValue(record.getR45_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(45);

					// ROW46
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR46_EQUITY() != null) {
					    cell2.setCellValue(record.getR46_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR46_BONDS() != null) {
					    cell3.setCellValue(record.getR46_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR46_BOBC() != null) {
					    cell4.setCellValue(record.getR46_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR46_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR46_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR46_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR46_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR46_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR46_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR46_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR46_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR46_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR46_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR46_OTHER() != null) {
					    cell10.setCellValue(record.getR46_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR46_TOTAL() != null) {
//					    cell11.setCellValue(record.getR46_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(46);

					
					
					// ROW47
					// COLUMN2
//					cell2 = row.createCell(1);
//					if (record.getR47_EQUITY() != null) {
//					    cell2.setCellValue(record.getR47_EQUITY().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}
//					// COLUMN3
//					cell3 = row.createCell(2);
//					if (record.getR47_BONDS() != null) {
//					    cell3.setCellValue(record.getR47_BONDS().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//					// COLUMN4
//					cell4 = row.createCell(3);
//					if (record.getR47_BOBC() != null) {
//					    cell4.setCellValue(record.getR47_BOBC().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
//					// COLUMN5
//					cell5 = row.createCell(4);
//					if (record.getR47_TRES_BILLS() != null) {
//					    cell5.setCellValue(record.getR47_TRES_BILLS().doubleValue());
//					    cell5.setCellStyle(numberStyle);
//					} else {
//					    cell5.setCellValue("");
//					    cell5.setCellStyle(textStyle);
//					}
//					// COLUMN6
//					cell6 = row.createCell(5);
//					if (record.getR47_REPURCHASE_AGREE() != null) {
//					    cell6.setCellValue(record.getR47_REPURCHASE_AGREE().doubleValue());
//					    cell6.setCellStyle(numberStyle);
//					} else {
//					    cell6.setCellValue("");
//					    cell6.setCellStyle(textStyle);
//					}
//					// COLUMN7
//					cell7 = row.createCell(6);
//					if (record.getR47_COM_PAPER() != null) {
//					    cell7.setCellValue(record.getR47_COM_PAPER().doubleValue());
//					    cell7.setCellStyle(numberStyle);
//					} else {
//					    cell7.setCellValue("");
//					    cell7.setCellStyle(textStyle);
//					}
//					// COLUMN8
//					cell8 = row.createCell(7);
//					if (record.getR47_CERT_OF_DEP() != null) {
//					    cell8.setCellValue(record.getR47_CERT_OF_DEP().doubleValue());
//					    cell8.setCellStyle(numberStyle);
//					} else {
//					    cell8.setCellValue("");
//					    cell8.setCellStyle(textStyle);
//					}
//					// COLUMN9
//					cell9 = row.createCell(8);
//					if (record.getR47_PLEDGED_ASSET() != null) {
//					    cell9.setCellValue(record.getR47_PLEDGED_ASSET().doubleValue());
//					    cell9.setCellStyle(numberStyle);
//					} else {
//					    cell9.setCellValue("");
//					    cell9.setCellStyle(textStyle);
//					}
//					// COLUMN10
//					cell10 = row.createCell(9);
//					if (record.getR47_OTHER() != null) {
//					    cell10.setCellValue(record.getR47_OTHER().doubleValue());
//					    cell10.setCellStyle(numberStyle);
//					} else {
//					    cell10.setCellValue("");
//					    cell10.setCellStyle(textStyle);
//					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR47_TOTAL() != null) {
//					    cell11.setCellValue(record.getR47_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(47);

					// ROW48
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR48_EQUITY() != null) {
					    cell2.setCellValue(record.getR48_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR48_BONDS() != null) {
					    cell3.setCellValue(record.getR48_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR48_BOBC() != null) {
					    cell4.setCellValue(record.getR48_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR48_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR48_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR48_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR48_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR48_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR48_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR48_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR48_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR48_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR48_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR48_OTHER() != null) {
					    cell10.setCellValue(record.getR48_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR48_TOTAL() != null) {
//					    cell11.setCellValue(record.getR48_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(48);

					
					
					// ROW49
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR49_EQUITY() != null) {
					    cell2.setCellValue(record.getR49_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR49_BONDS() != null) {
					    cell3.setCellValue(record.getR49_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR49_BOBC() != null) {
					    cell4.setCellValue(record.getR49_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR49_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR49_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR49_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR49_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR49_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR49_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR49_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR49_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR49_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR49_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR49_OTHER() != null) {
					    cell10.setCellValue(record.getR49_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR49_TOTAL() != null) {
//					    cell11.setCellValue(record.getR49_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(49);

					// ROW50
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR50_EQUITY() != null) {
					    cell2.setCellValue(record.getR50_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR50_BONDS() != null) {
					    cell3.setCellValue(record.getR50_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR50_BOBC() != null) {
					    cell4.setCellValue(record.getR50_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR50_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR50_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR50_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR50_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR50_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR50_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR50_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR50_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR50_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR50_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR50_OTHER() != null) {
					    cell10.setCellValue(record.getR50_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR50_TOTAL() != null) {
//					    cell11.setCellValue(record.getR50_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(50);

					
					
					// ROW51
					// COLUMN2
//					cell2 = row.createCell(1);
//					if (record.getR51_EQUITY() != null) {
//					    cell2.setCellValue(record.getR51_EQUITY().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}
//					// COLUMN3
//					cell3 = row.createCell(2);
//					if (record.getR51_BONDS() != null) {
//					    cell3.setCellValue(record.getR51_BONDS().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//					// COLUMN4
//					cell4 = row.createCell(3);
//					if (record.getR51_BOBC() != null) {
//					    cell4.setCellValue(record.getR51_BOBC().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
//					// COLUMN5
//					cell5 = row.createCell(4);
//					if (record.getR51_TRES_BILLS() != null) {
//					    cell5.setCellValue(record.getR51_TRES_BILLS().doubleValue());
//					    cell5.setCellStyle(numberStyle);
//					} else {
//					    cell5.setCellValue("");
//					    cell5.setCellStyle(textStyle);
//					}
//					// COLUMN6
//					cell6 = row.createCell(5);
//					if (record.getR51_REPURCHASE_AGREE() != null) {
//					    cell6.setCellValue(record.getR51_REPURCHASE_AGREE().doubleValue());
//					    cell6.setCellStyle(numberStyle);
//					} else {
//					    cell6.setCellValue("");
//					    cell6.setCellStyle(textStyle);
//					}
//					// COLUMN7
//					cell7 = row.createCell(6);
//					if (record.getR51_COM_PAPER() != null) {
//					    cell7.setCellValue(record.getR51_COM_PAPER().doubleValue());
//					    cell7.setCellStyle(numberStyle);
//					} else {
//					    cell7.setCellValue("");
//					    cell7.setCellStyle(textStyle);
//					}
//					// COLUMN8
//					cell8 = row.createCell(7);
//					if (record.getR51_CERT_OF_DEP() != null) {
//					    cell8.setCellValue(record.getR51_CERT_OF_DEP().doubleValue());
//					    cell8.setCellStyle(numberStyle);
//					} else {
//					    cell8.setCellValue("");
//					    cell8.setCellStyle(textStyle);
//					}
//					// COLUMN9
//					cell9 = row.createCell(8);
//					if (record.getR51_PLEDGED_ASSET() != null) {
//					    cell9.setCellValue(record.getR51_PLEDGED_ASSET().doubleValue());
//					    cell9.setCellStyle(numberStyle);
//					} else {
//					    cell9.setCellValue("");
//					    cell9.setCellStyle(textStyle);
//					}
//					// COLUMN10
//					cell10 = row.createCell(9);
//					if (record.getR51_OTHER() != null) {
//					    cell10.setCellValue(record.getR51_OTHER().doubleValue());
//					    cell10.setCellStyle(numberStyle);
//					} else {
//					    cell10.setCellValue("");
//					    cell10.setCellStyle(textStyle);
//					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR51_TOTAL() != null) {
//					    cell11.setCellValue(record.getR51_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(51);

					// ROW52
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR52_EQUITY() != null) {
					    cell2.setCellValue(record.getR52_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR52_BONDS() != null) {
					    cell3.setCellValue(record.getR52_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR52_BOBC() != null) {
					    cell4.setCellValue(record.getR52_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR52_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR52_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR52_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR52_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR52_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR52_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR52_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR52_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR52_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR52_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR52_OTHER() != null) {
					    cell10.setCellValue(record.getR52_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR52_TOTAL() != null) {
//					    cell11.setCellValue(record.getR52_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(52);

					
					
					// ROW53
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR53_EQUITY() != null) {
					    cell2.setCellValue(record.getR53_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR53_BONDS() != null) {
					    cell3.setCellValue(record.getR53_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR53_BOBC() != null) {
					    cell4.setCellValue(record.getR53_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR53_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR53_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR53_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR53_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR53_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR53_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR53_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR53_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR53_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR53_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR53_OTHER() != null) {
					    cell10.setCellValue(record.getR53_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR53_TOTAL() != null) {
//					    cell11.setCellValue(record.getR53_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(53);

					// ROW54
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR54_EQUITY() != null) {
					    cell2.setCellValue(record.getR54_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR54_BONDS() != null) {
					    cell3.setCellValue(record.getR54_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR54_BOBC() != null) {
					    cell4.setCellValue(record.getR54_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR54_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR54_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR54_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR54_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR54_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR54_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR54_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR54_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR54_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR54_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR54_OTHER() != null) {
					    cell10.setCellValue(record.getR54_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR54_TOTAL() != null) {
//					    cell11.setCellValue(record.getR54_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(54);

					
					
					// ROW55
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR55_EQUITY() != null) {
					    cell2.setCellValue(record.getR55_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR55_BONDS() != null) {
					    cell3.setCellValue(record.getR55_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR55_BOBC() != null) {
					    cell4.setCellValue(record.getR55_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR55_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR55_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR55_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR55_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR55_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR55_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR55_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR55_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR55_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR55_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR55_OTHER() != null) {
					    cell10.setCellValue(record.getR55_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR55_TOTAL() != null) {
//					    cell11.setCellValue(record.getR55_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(55);

					// ROW56
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR56_EQUITY() != null) {
					    cell2.setCellValue(record.getR56_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR56_BONDS() != null) {
					    cell3.setCellValue(record.getR56_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR56_BOBC() != null) {
					    cell4.setCellValue(record.getR56_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR56_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR56_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR56_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR56_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR56_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR56_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR56_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR56_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR56_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR56_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR56_OTHER() != null) {
					    cell10.setCellValue(record.getR56_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR56_TOTAL() != null) {
//					    cell11.setCellValue(record.getR56_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(56);

					
					
					// ROW57
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR57_EQUITY() != null) {
					    cell2.setCellValue(record.getR57_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR57_BONDS() != null) {
					    cell3.setCellValue(record.getR57_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR57_BOBC() != null) {
					    cell4.setCellValue(record.getR57_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR57_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR57_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR57_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR57_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR57_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR57_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR57_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR57_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR57_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR57_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR57_OTHER() != null) {
					    cell10.setCellValue(record.getR57_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR57_TOTAL() != null) {
//					    cell11.setCellValue(record.getR57_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(57);

					// ROW58
					// COLUMN2
//					cell2 = row.createCell(1);
//					if (record.getR58_EQUITY() != null) {
//					    cell2.setCellValue(record.getR58_EQUITY().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}
//					// COLUMN3
//					cell3 = row.createCell(2);
//					if (record.getR58_BONDS() != null) {
//					    cell3.setCellValue(record.getR58_BONDS().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//					// COLUMN4
//					cell4 = row.createCell(3);
//					if (record.getR58_BOBC() != null) {
//					    cell4.setCellValue(record.getR58_BOBC().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
//					// COLUMN5
//					cell5 = row.createCell(4);
//					if (record.getR58_TRES_BILLS() != null) {
//					    cell5.setCellValue(record.getR58_TRES_BILLS().doubleValue());
//					    cell5.setCellStyle(numberStyle);
//					} else {
//					    cell5.setCellValue("");
//					    cell5.setCellStyle(textStyle);
//					}
//					// COLUMN6
//					cell6 = row.createCell(5);
//					if (record.getR58_REPURCHASE_AGREE() != null) {
//					    cell6.setCellValue(record.getR58_REPURCHASE_AGREE().doubleValue());
//					    cell6.setCellStyle(numberStyle);
//					} else {
//					    cell6.setCellValue("");
//					    cell6.setCellStyle(textStyle);
//					}
//					// COLUMN7
//					cell7 = row.createCell(6);
//					if (record.getR58_COM_PAPER() != null) {
//					    cell7.setCellValue(record.getR58_COM_PAPER().doubleValue());
//					    cell7.setCellStyle(numberStyle);
//					} else {
//					    cell7.setCellValue("");
//					    cell7.setCellStyle(textStyle);
//					}
//					// COLUMN8
//					cell8 = row.createCell(7);
//					if (record.getR58_CERT_OF_DEP() != null) {
//					    cell8.setCellValue(record.getR58_CERT_OF_DEP().doubleValue());
//					    cell8.setCellStyle(numberStyle);
//					} else {
//					    cell8.setCellValue("");
//					    cell8.setCellStyle(textStyle);
//					}
//					// COLUMN9
//					cell9 = row.createCell(8);
//					if (record.getR58_PLEDGED_ASSET() != null) {
//					    cell9.setCellValue(record.getR58_PLEDGED_ASSET().doubleValue());
//					    cell9.setCellStyle(numberStyle);
//					} else {
//					    cell9.setCellValue("");
//					    cell9.setCellStyle(textStyle);
//					}
//					// COLUMN10
//					cell10 = row.createCell(9);
//					if (record.getR58_OTHER() != null) {
//					    cell10.setCellValue(record.getR58_OTHER().doubleValue());
//					    cell10.setCellStyle(numberStyle);
//					} else {
//					    cell10.setCellValue("");
//					    cell10.setCellStyle(textStyle);
//					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR58_TOTAL() != null) {
//					    cell11.setCellValue(record.getR58_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}

					row = sheet.getRow(58);

					
					
					// ROW59
					// COLUMN2
					cell2 = row.createCell(1);
					if (record.getR59_EQUITY() != null) {
					    cell2.setCellValue(record.getR59_EQUITY().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					// COLUMN3
					cell3 = row.createCell(2);
					if (record.getR59_BONDS() != null) {
					    cell3.setCellValue(record.getR59_BONDS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					// COLUMN4
					cell4 = row.createCell(3);
					if (record.getR59_BOBC() != null) {
					    cell4.setCellValue(record.getR59_BOBC().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}
					// COLUMN5
					cell5 = row.createCell(4);
					if (record.getR59_TRES_BILLS() != null) {
					    cell5.setCellValue(record.getR59_TRES_BILLS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}
					// COLUMN6
					cell6 = row.createCell(5);
					if (record.getR59_REPURCHASE_AGREE() != null) {
					    cell6.setCellValue(record.getR59_REPURCHASE_AGREE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}
					// COLUMN7
					cell7 = row.createCell(6);
					if (record.getR59_COM_PAPER() != null) {
					    cell7.setCellValue(record.getR59_COM_PAPER().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}
					// COLUMN8
					cell8 = row.createCell(7);
					if (record.getR59_CERT_OF_DEP() != null) {
					    cell8.setCellValue(record.getR59_CERT_OF_DEP().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					// COLUMN9
					cell9 = row.createCell(8);
					if (record.getR59_PLEDGED_ASSET() != null) {
					    cell9.setCellValue(record.getR59_PLEDGED_ASSET().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
					// COLUMN10
					cell10 = row.createCell(9);
					if (record.getR59_OTHER() != null) {
					    cell10.setCellValue(record.getR59_OTHER().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}
					// COLUMN11
//					cell11 = row.createCell(10);
//					if (record.getR59_TOTAL() != null) {
//					    cell11.setCellValue(record.getR59_TOTAL().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}
					
					
			
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

//	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
//										 String dtltype, String type, String version) {
//		try {
//			logger.info("Generating Excel for BRRS_M_SECA ARCHIVAL Details...");
//			System.out.println("came to Detail download service");
//			if (type.equals("ARCHIVAL") & version != null) {
//
//			}
//			XSSFWorkbook workbook = new XSSFWorkbook();
//			XSSFSheet sheet = workbook.createSheet("M_SECADetail");
//
//			// Common border style
//			BorderStyle border = BorderStyle.THIN;
//
//			// Header style (left aligned)
//			CellStyle headerStyle = workbook.createCellStyle();
//			Font headerFont = workbook.createFont();
//			headerFont.setBold(true);
//			headerFont.setFontHeightInPoints((short) 10);
//			headerStyle.setFont(headerFont);
//			headerStyle.setAlignment(HorizontalAlignment.LEFT);
//			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//			headerStyle.setBorderTop(border);
//			headerStyle.setBorderBottom(border);
//			headerStyle.setBorderLeft(border);
//			headerStyle.setBorderRight(border);
//
//			// Right-aligned header style for ACCT BALANCE
//			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
//			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
//			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);
//
//			// Default data style (left aligned)
//			CellStyle dataStyle = workbook.createCellStyle();
//			dataStyle.setAlignment(HorizontalAlignment.LEFT);
//			dataStyle.setBorderTop(border);
//			dataStyle.setBorderBottom(border);
//			dataStyle.setBorderLeft(border);
//			dataStyle.setBorderRight(border);
//
//			// ACCT BALANCE style (right aligned with 3 decimals)
//			CellStyle balanceStyle = workbook.createCellStyle();
//			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
//			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
//			balanceStyle.setBorderTop(border);
//			balanceStyle.setBorderBottom(border);
//			balanceStyle.setBorderLeft(border);
//			balanceStyle.setBorderRight(border);
//
//			// Header row
//			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "ROWID", "COLUMNID",
//					"REPORT_DATE" };
//
//			XSSFRow headerRow = sheet.createRow(0);
//			for (int i = 0; i < headers.length; i++) {
//				Cell cell = headerRow.createCell(i);
//				cell.setCellValue(headers[i]);
//
//				if (i == 3) { // ACCT BALANCE
//					cell.setCellStyle(rightAlignedHeaderStyle);
//				} else {
//					cell.setCellStyle(headerStyle);
//				}
//
//				sheet.setColumnWidth(i, 5000);
//			}
//
//			// Get data
//			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
//			List<BRRS_M_SECA_Archival_Detail_Entity> reportData = BRRS_M_SECA_Archival_Detail_Repo
//					.getdatabydateList(parsedToDate, version);
//
//			if (reportData != null && !reportData.isEmpty()) {
//				int rowIndex = 1;
//				for (BRRS_M_SECA_Archival_Detail_Entity item : reportData) {
//					XSSFRow row = sheet.createRow(rowIndex++);
//
//					row.createCell(0).setCellValue(item.getCUST_ID());
//					row.createCell(1).setCellValue(item.getACCT_NUMBER());
//					row.createCell(2).setCellValue(item.getACCT_NAME());
//
//					// ACCT BALANCE (right aligned, 3 decimal places)
//					Cell balanceCell = row.createCell(3);
//					if (item.getACCT_BALANCE_IN_PULA() != null) {
//						balanceCell.setCellValue(item.getACCT_BALANCE_IN_PULA().doubleValue());
//					} else {
//						balanceCell.setCellValue(0.000);
//					}
//					balanceCell.setCellStyle(balanceStyle);
//
//					row.createCell(4).setCellValue(item.getROW_ID());
//					row.createCell(5).setCellValue(item.getCOLUMN_ID());
//					row.createCell(6)
//							.setCellValue(item.getREPORT_DATE() != null
//									? new SimpleDateFormat("dd-MM-yyyy").format(item.getREPORT_DATE())
//									: "");
//
//					// Apply data style for all other cells
//					for (int j = 0; j < 7; j++) {
//						if (j != 3) {
//							row.getCell(j).setCellStyle(dataStyle);
//						}
//					}
//				}
//			} else {
//				logger.info("No data found for BRRS_M_SECA — only header will be written.");
//			}
//
//			// Write to byte[]
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			workbook.write(bos);
//			workbook.close();
//
//			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
//			return bos.toByteArray();
//
//		} catch (Exception e) {
//			logger.error("Error generating BRRS_M_SECAExcel", e);
//			return new byte[0];
//		}
//	}

}
