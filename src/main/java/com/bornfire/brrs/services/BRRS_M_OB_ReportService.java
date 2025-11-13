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

//import com.bornfire.brrs.entities.BRRS_M_OB_Archival_Detail_Entity;
//import com.bornfire.brrs.entities.BRRS_M_OB_Archival_Detail_Repo;
import com.bornfire.brrs.entities.M_OB_Archival_Summary_Entity;
//import com.bornfire.brrs.entities.BRRS_M_OB_Detail_Repo;
import com.bornfire.brrs.entities.M_OB_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12B_Summary_Entity1;
import com.bornfire.brrs.entities.M_OB_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_OB_Summary_Entity;
import com.bornfire.brrs.entities.M_OB_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_OB_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_OB_Summary_Entity;
import com.bornfire.brrs.entities.M_OB_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_OB_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_OB_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_OB_Archival_Summary_Repo;
//import com.bornfire.brrs.entities.BRRS_M_OB_Detail_Entity;


import java.math.BigDecimal;

@Component
@Service
public class BRRS_M_OB_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_OB_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;


//
//	@Autowired
//	BRRS_M_OB_Detail_Repo BRRS_M_OB_Detail_Repo;

	@Autowired
	BRRS_M_OB_Summary_Repo BRRS_M_OB_Summary_Repo;

//	@Autowired
//	BRRS_M_OB_Archival_Detail_Repo BRRS_M_OB_Archival_Detail_Repo;

	@Autowired
	BRRS_M_OB_Archival_Summary_Repo BRRS_M_OB_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_OBview(String reportId, String fromdate, String todate, String currency,
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
            List<M_OB_Archival_Summary_Entity> T1Master = 
                BRRS_M_OB_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 2: RESUB ----------
        else if ("RESUB".equalsIgnoreCase(type) && version != null) {
            List<M_OB_Archival_Summary_Entity> T1Master =
                BRRS_M_OB_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 3: NORMAL ----------
        else {
            List<M_OB_Summary_Entity> T1Master = 
                BRRS_M_OB_Summary_Repo.getdatabydateListWithVersion(todate);
            System.out.println("T1Master Size "+T1Master.size());
            mv.addObject("reportsummary", T1Master);
        }

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_OB");
		mv.addObject("displaymode", "summary");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	public void updateReport1(M_OB_Summary_Entity updatedEntity) {
	    System.out.println("Came to services1");
	    System.out.println("Report Date: " + updatedEntity.getReportDate());

	    M_OB_Summary_Entity existing = BRRS_M_OB_Summary_Repo.findById(updatedEntity.getReportDate())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

	    try {
	        // 1. Loop from R15 to R50 and copy fields
	        for (int i = 12; i <= 63; i++) {
		if (i == 15 || i == 29 || i == 38 || i == 41 || i == 44 || i == 49 || i == 52 || i == 58 || i == 64) {
        continue;
    }
	            String prefix = "R" + i + "_";

	            String[] fields = {"OTHER_BORROW"};

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_OB_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_OB_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }
	        
// Loop through rows for formula fields 
int[] targetRows = {11, 15, 29, 38, 41, 44, 49, 52, 58, 64};

for (int i : targetRows) {
    String prefix = "R" + i + "_";

    String[] fields = {
        "OTHER_BORROW"
    };

    for (String field : fields) {
        String getterName = "get" + prefix + field;
        String setterName = "set" + prefix + field;

        try {
            Method getter = M_OB_Summary_Entity.class.getMethod(getterName);
            Method setter = M_OB_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

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
	    BRRS_M_OB_Summary_Repo.save(existing);
	}
	
	
	
	
//	public ModelAndView getM_OBcurrentDtl(String reportId, String fromdate, String todate, String currency,
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
//				List<BRRS_M_OB_Archival_Detail_Entity> T1Dt1;
//				if (rowId != null && columnId != null) {
//					T1Dt1 = BRRS_M_OB_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
//				} else {
//					T1Dt1 = BRRS_M_OB_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
//				}
//
//				mv.addObject("reportdetails", T1Dt1);
//				mv.addObject("reportmaster12", T1Dt1);
//				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
//
//			} else {
//				// 🔹 Current branch
//				List<BRRS_M_OB_Detail_Entity> T1Dt1;
//				if (rowId != null && columnId != null) {
//					T1Dt1 = BRRS_M_OB_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
//				} else {
//					T1Dt1 = BRRS_M_OB_Detail_Repo.getdatabydateList(parsedDate, currentPage, pageSize);
//					totalPages = BRRS_M_OB_Detail_Repo.getdatacount(parsedDate);
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
//		mv.setViewName("BRRS/M_OB");
//		mv.addObject("displaymode", "Details");
//		mv.addObject("currentPage", currentPage);
//		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
//		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
//		mv.addObject("reportsflag", "reportsflag");
//		mv.addObject("menu", reportId);
//
//		return mv;
//	}



	public byte[] getM_OBExcel(String filename, String reportId, String fromdate, String todate, String currency,
									 String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

		// Convert string to Date
		Date reportDate = dateformat.parse(todate);

    // ARCHIVAL check
    if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
        logger.info("Service: Generating ARCHIVAL report for version {}", version);
        return getExcelM_OBARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
    }
    // RESUB check
    else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
        logger.info("Service: Generating RESUB report for version {}", version);

       
        List<M_OB_Archival_Summary_Entity> T1Master =
                BRRS_M_OB_Archival_Summary_Repo.getdatabydateListarchival(reportDate, version);

        // Generate Excel for RESUB
        return BRRS_M_OBResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
    }




		// Default (LIVE) case
		List<M_OB_Summary_Entity> dataList = BRRS_M_OB_Summary_Repo.getdatabydateList(reportDate);

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
					M_OB_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);	
					
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
			

				
//					Cell cell2 = row.createCell(1);
//					if (record.getR11_OTHER_BORROW() != null) {
//						cell2.setCellValue(record.getR11_OTHER_BORROW().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
					
					row = sheet.getRow(11);
					
					Cell cell2 = row.createCell(1);
					if (record.getR12_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR12_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(12);
					
					cell2 = row.createCell(1);
					if (record.getR13_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR13_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(13);
					cell2 = row.createCell(1);
					if (record.getR14_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR14_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(14);
//					cell2 = row.createCell(1);
//					if (record.getR15_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR15_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(15);
					cell2 = row.createCell(1);
					if (record.getR16_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR16_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell2 = row.createCell(1);
					if (record.getR17_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR17_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell2 = row.createCell(1);
					if (record.getR18_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR18_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell2 = row.createCell(1);
					if (record.getR19_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR19_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell2 = row.createCell(1);
					if (record.getR20_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR20_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					cell2 = row.createCell(1);
					if (record.getR21_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR21_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					cell2 = row.createCell(1);
					if (record.getR22_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR22_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					cell2 = row.createCell(1);
					if (record.getR23_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR23_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell2 = row.createCell(1);
					if (record.getR24_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR24_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					cell2 = row.createCell(1);
					if (record.getR25_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR25_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell2 = row.createCell(1);
					if (record.getR26_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR26_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(26);
					cell2 = row.createCell(1);
					if (record.getR27_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR27_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					cell2 = row.createCell(1);
					if (record.getR28_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR28_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(28);
//					cell2 = row.createCell(1);
//					if (record.getR29_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR29_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(29);
					cell2 = row.createCell(1);
					if (record.getR30_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR30_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					cell2 = row.createCell(1);
					if (record.getR31_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR31_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell2 = row.createCell(1);
					if (record.getR32_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR32_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					cell2 = row.createCell(1);
					if (record.getR33_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR33_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					cell2 = row.createCell(1);
					if (record.getR34_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR34_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
					cell2 = row.createCell(1);
					if (record.getR35_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR35_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(35);
					cell2 = row.createCell(1);
					if (record.getR36_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR36_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					cell2 = row.createCell(1);
					if (record.getR37_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR37_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(37);
//					cell2 = row.createCell(1);
//					if (record.getR38_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR38_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(38);
					cell2 = row.createCell(1);
					if (record.getR39_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR39_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					cell2 = row.createCell(1);
					if (record.getR40_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR40_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(40);
//					cell2 = row.createCell(1);
//					if (record.getR41_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR41_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(41);
					cell2 = row.createCell(1);
					if (record.getR42_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR42_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					cell2 = row.createCell(1);
					if (record.getR43_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR43_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(43);
//					cell2 = row.createCell(1);
//					if (record.getR44_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR44_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(44);
					cell2 = row.createCell(1);
					if (record.getR45_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR45_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					cell2 = row.createCell(1);
					if (record.getR46_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR46_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					cell2 = row.createCell(1);
					if (record.getR47_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR47_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					cell2 = row.createCell(1);
					if (record.getR48_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR48_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(48);
//					cell2 = row.createCell(1);
//					if (record.getR49_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR49_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(49);
					cell2 = row.createCell(1);
					if (record.getR50_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR50_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(50);
					cell2 = row.createCell(1);
					if (record.getR51_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR51_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(51);
//					cell2 = row.createCell(1);
//					if (record.getR52_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR52_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(52);
					cell2 = row.createCell(1);
					if (record.getR53_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR53_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(53);
					cell2 = row.createCell(1);
					if (record.getR54_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR54_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(54);
					cell2 = row.createCell(1);
					if (record.getR55_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR55_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(55);
					cell2 = row.createCell(1);
					if (record.getR56_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR56_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(56);
					cell2 = row.createCell(1);
					if (record.getR57_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR57_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(57);
//					cell2 = row.createCell(1);
//					if (record.getR58_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR58_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(58);
					cell2 = row.createCell(1);
					if (record.getR59_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR59_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(59);
					cell2 = row.createCell(1);
					if (record.getR60_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR60_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(60);
					cell2 = row.createCell(1);
					if (record.getR61_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR61_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(61);
					cell2 = row.createCell(1);
					if (record.getR62_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR62_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(62);
					cell2 = row.createCell(1);
					if (record.getR63_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR63_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					row = sheet.getRow(63);
//					
//					cell2 = row.createCell(1);
//					if (record.getR64_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR64_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
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

//	public byte[] BRRS_M_OBDetailExcel(String filename, String fromdate, String todate, String currency,
//										   String dtltype, String type, String version) {
//
//		try {
//			logger.info("Generating Excel for BRRS_M_OB Details...");
//			System.out.println("came to Detail download service");
//			if (type.equals("ARCHIVAL") & version != null) {
//				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
//						version);
//				return ARCHIVALreport;
//			}
//			XSSFWorkbook workbook = new XSSFWorkbook();
//			XSSFSheet sheet = workbook.createSheet("BRRS_M_OBDetails");
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
//			List<BRRS_M_OB_Detail_Entity> reportData = BRRS_M_OB_Detail_Repo.getdatabydateList(parsedToDate);
//			if (reportData != null && !reportData.isEmpty()) {
//				int rowIndex = 1;
//				for (BRRS_M_OB_Detail_Entity item : reportData) {
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
//				logger.info("No data found for BRRS_M_OB — only header will be written.");
//			}
//			// Write to byte[]
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			workbook.write(bos);
//			workbook.close();
//			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
//			return bos.toByteArray();
//		} catch (Exception e) {
//			logger.error("Error generating BRRS_M_OB Excel", e);
//			return new byte[0];
//		}
//	}

//	public List<Object> getM_OBArchival() {
//		List<Object> M_OBArchivallist = new ArrayList<>();
//		try {
//			M_OBArchivallist = BRRS_M_OB_Archival_Summary_Repo.getM_OBarchival();
//			System.out.println("countser" + M_OBArchivallist.size());
//		} catch (Exception e) {
//			// Log the exception
//			System.err.println("Error fetching M_OB Archival data: " + e.getMessage());
//			e.printStackTrace();
//
//			// Optionally, you can rethrow it or return empty list
//			// throw new RuntimeException("Failed to fetch data", e);
//		}
//		return M_OBArchivallist;
//	}

	public byte[] getExcelM_OBARCHIVAL(String filename, String reportId, String fromdate,
			String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if ("ARCHIVAL".equals(type) && version != null) {
		}
			List<M_OB_Archival_Summary_Entity> dataList = BRRS_M_OB_Archival_Summary_Repo
					.getdatabydateListarchival(dateformat.parse(todate), version);
		

			
		
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_OB report. Returning empty result.");
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
					M_OB_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}



//					Cell cell2 = row.createCell(1);
//					if (record.getR11_OTHER_BORROW() != null) {
//						cell2.setCellValue(record.getR11_OTHER_BORROW().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
					
					row = sheet.getRow(11);
					
					Cell cell2 = row.createCell(1);
					if (record.getR12_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR12_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(12);
					
					cell2 = row.createCell(1);
					if (record.getR13_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR13_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					row = sheet.getRow(13);
					cell2 = row.createCell(1);
					if (record.getR14_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR14_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(14);
//					cell2 = row.createCell(1);
//					if (record.getR15_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR15_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(15);
					cell2 = row.createCell(1);
					if (record.getR16_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR16_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell2 = row.createCell(1);
					if (record.getR17_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR17_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell2 = row.createCell(1);
					if (record.getR18_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR18_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell2 = row.createCell(1);
					if (record.getR19_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR19_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell2 = row.createCell(1);
					if (record.getR20_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR20_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					cell2 = row.createCell(1);
					if (record.getR21_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR21_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					cell2 = row.createCell(1);
					if (record.getR22_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR22_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					cell2 = row.createCell(1);
					if (record.getR23_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR23_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell2 = row.createCell(1);
					if (record.getR24_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR24_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					cell2 = row.createCell(1);
					if (record.getR25_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR25_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell2 = row.createCell(1);
					if (record.getR26_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR26_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(26);
					cell2 = row.createCell(1);
					if (record.getR27_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR27_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					cell2 = row.createCell(1);
					if (record.getR28_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR28_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(28);
//					cell2 = row.createCell(1);
//					if (record.getR29_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR29_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(29);
					cell2 = row.createCell(1);
					if (record.getR30_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR30_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					cell2 = row.createCell(1);
					if (record.getR31_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR31_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell2 = row.createCell(1);
					if (record.getR32_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR32_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					cell2 = row.createCell(1);
					if (record.getR33_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR33_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					cell2 = row.createCell(1);
					if (record.getR34_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR34_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
					cell2 = row.createCell(1);
					if (record.getR35_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR35_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(35);
					cell2 = row.createCell(1);
					if (record.getR36_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR36_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					cell2 = row.createCell(1);
					if (record.getR37_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR37_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(37);
//					cell2 = row.createCell(1);
//					if (record.getR38_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR38_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(38);
					cell2 = row.createCell(1);
					if (record.getR39_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR39_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					cell2 = row.createCell(1);
					if (record.getR40_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR40_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(40);
//					cell2 = row.createCell(1);
//					if (record.getR41_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR41_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(41);
					cell2 = row.createCell(1);
					if (record.getR42_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR42_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					cell2 = row.createCell(1);
					if (record.getR43_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR43_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(43);
//					cell2 = row.createCell(1);
//					if (record.getR44_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR44_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(44);
					cell2 = row.createCell(1);
					if (record.getR45_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR45_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					cell2 = row.createCell(1);
					if (record.getR46_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR46_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					cell2 = row.createCell(1);
					if (record.getR47_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR47_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					cell2 = row.createCell(1);
					if (record.getR48_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR48_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(48);
//					cell2 = row.createCell(1);
//					if (record.getR49_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR49_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(49);
					cell2 = row.createCell(1);
					if (record.getR50_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR50_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(50);
					cell2 = row.createCell(1);
					if (record.getR51_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR51_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(51);
//					cell2 = row.createCell(1);
//					if (record.getR52_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR52_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(52);
					cell2 = row.createCell(1);
					if (record.getR53_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR53_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(53);
					cell2 = row.createCell(1);
					if (record.getR54_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR54_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(54);
					cell2 = row.createCell(1);
					if (record.getR55_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR55_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(55);
					cell2 = row.createCell(1);
					if (record.getR56_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR56_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(56);
					cell2 = row.createCell(1);
					if (record.getR57_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR57_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(57);
//					cell2 = row.createCell(1);
//					if (record.getR58_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR58_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(58);
					cell2 = row.createCell(1);
					if (record.getR59_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR59_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(59);
					cell2 = row.createCell(1);
					if (record.getR60_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR60_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(60);
					cell2 = row.createCell(1);
					if (record.getR61_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR61_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(61);
					cell2 = row.createCell(1);
					if (record.getR62_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR62_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(62);
					cell2 = row.createCell(1);
					if (record.getR63_OTHER_BORROW() != null) {
					    cell2.setCellValue(record.getR63_OTHER_BORROW().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}
					row = sheet.getRow(63);
//					
//					cell2 = row.createCell(1);
//					if (record.getR64_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR64_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
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
//			logger.info("Generating Excel for BRRS_M_OB ARCHIVAL Details...");
//			System.out.println("came to Detail download service");
//			if (type.equals("ARCHIVAL") & version != null) {
//
//			}
//			XSSFWorkbook workbook = new XSSFWorkbook();
//			XSSFSheet sheet = workbook.createSheet("M_OBDetail");
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
//			List<BRRS_M_OB_Archival_Detail_Entity> reportData = BRRS_M_OB_Archival_Detail_Repo
//					.getdatabydateList(parsedToDate, version);
//
//			if (reportData != null && !reportData.isEmpty()) {
//				int rowIndex = 1;
//				for (BRRS_M_OB_Archival_Detail_Entity item : reportData) {
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
//				logger.info("No data found for BRRS_M_OB — only header will be written.");
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
//			logger.error("Error generating BRRS_M_OBExcel", e);
//			return new byte[0];
//		}
//	}

//////////////////////////////////////////RESUBMISSION///////////////////////////////////////////////////////////////////	
/// Report Date | Report Version | Domain
/// RESUB VIEW
public List<Object[]> getM_OBResub() {
List<Object[]> resubList = new ArrayList<>();
try {
List<M_OB_Archival_Summary_Entity> latestArchivalList = 
BRRS_M_OB_Archival_Summary_Repo.getdatabydateListWithVersionAll();

if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
for (M_OB_Archival_Summary_Entity entity : latestArchivalList) {
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
System.err.println("Error fetching M_OB Resub data: " + e.getMessage());
e.printStackTrace();
}
return resubList;
}


//Archival View
public List<Object[]> getM_OBArchival() {
List<Object[]> archivalList = new ArrayList<>();

try {
List<M_OB_Archival_Summary_Entity> repoData = BRRS_M_OB_Archival_Summary_Repo
.getdatabydateListWithVersionAll();

if (repoData != null && !repoData.isEmpty()) {
for (M_OB_Archival_Summary_Entity entity : repoData) {
Object[] row = new Object[] {
entity.getReportDate(), 
entity.getReportVersion() 
};
archivalList.add(row);
}

System.out.println("Fetched " + archivalList.size() + " archival records");
M_OB_Archival_Summary_Entity first = repoData.get(0);
System.out.println("Latest archival version: " + first.getReportVersion());
} else {
System.out.println("No archival data found.");
}

} catch (Exception e) {
System.err.println("Error fetching M_OB Archival data: " + e.getMessage());
e.printStackTrace();
}

return archivalList;
}


// Resubmit the values , latest version and Resub Date
public void updateReportReSub(M_OB_Summary_Entity updatedEntity) {
System.out.println("Came to Resub Service");
System.out.println("Report Date: " + updatedEntity.getReportDate());

Date reportDate = updatedEntity.getReportDate();
int newVersion = 1;

try {
// Fetch the latest archival version for this report date
Optional<M_OB_Archival_Summary_Entity> latestArchivalOpt = BRRS_M_OB_Archival_Summary_Repo
.getLatestArchivalVersionByDate(reportDate);

// Determine next version number
if (latestArchivalOpt.isPresent()) {
M_OB_Archival_Summary_Entity latestArchival = latestArchivalOpt.get();
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
boolean exists = BRRS_M_OB_Archival_Summary_Repo
.findByReportDateAndReportVersion(reportDate, String.valueOf(newVersion))
.isPresent();

if (exists) {
throw new RuntimeException("Version " + newVersion + " already exists for report date " + reportDate);
}

// Copy summary entity to archival entity
M_OB_Archival_Summary_Entity archivalEntity = new M_OB_Archival_Summary_Entity();
org.springframework.beans.BeanUtils.copyProperties(updatedEntity, archivalEntity);

archivalEntity.setReportDate(reportDate);
archivalEntity.setReportVersion(String.valueOf(newVersion));
archivalEntity.setReportResubDate(new Date());

System.out.println("Saving new archival version: " + newVersion);

// Save new version to repository
BRRS_M_OB_Archival_Summary_Repo.save(archivalEntity);

System.out.println(" Saved archival version successfully: " + newVersion);

} catch (Exception e) {
e.printStackTrace();
throw new RuntimeException("Error while creating archival resubmission record", e);
}
}

/// Downloaded for Archival & Resub
public byte[] BRRS_M_OBResubExcel(String filename, String reportId, String fromdate,
String todate, String currency, String dtltype,
String type, String version) throws Exception {

logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

if (type.equals("RESUB") & version != null) {

}

List<M_OB_Archival_Summary_Entity> dataList =
BRRS_M_OB_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

if (dataList.isEmpty()) {
logger.warn("Service: No data found for M_OB report. Returning empty result.");
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
		M_OB_Archival_Summary_Entity record = dataList.get(i);
		System.out.println("rownumber=" + startRow + i);
		Row row = sheet.getRow(startRow + i);
		if (row == null) {
			row = sheet.createRow(startRow + i);
		}



//		Cell cell2 = row.createCell(1);
//		if (record.getR11_OTHER_BORROW() != null) {
//			cell2.setCellValue(record.getR11_OTHER_BORROW().doubleValue());
//			cell2.setCellStyle(numberStyle);
//		} else {
//			cell2.setCellValue("");
//			cell2.setCellStyle(textStyle);
//		}
		
		row = sheet.getRow(11);
		
		Cell cell2 = row.createCell(1);
		if (record.getR12_OTHER_BORROW() != null) {
			cell2.setCellValue(record.getR12_OTHER_BORROW().doubleValue());
			cell2.setCellStyle(numberStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}
		
		row = sheet.getRow(12);
		
		cell2 = row.createCell(1);
		if (record.getR13_OTHER_BORROW() != null) {
			cell2.setCellValue(record.getR13_OTHER_BORROW().doubleValue());
			cell2.setCellStyle(numberStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}
		
		row = sheet.getRow(13);
		cell2 = row.createCell(1);
		if (record.getR14_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR14_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

//		row = sheet.getRow(14);
//		cell2 = row.createCell(1);
//		if (record.getR15_OTHER_BORROW() != null) {
//		    cell2.setCellValue(record.getR15_OTHER_BORROW().doubleValue());
//		    cell2.setCellStyle(numberStyle);
//		} else {
//		    cell2.setCellValue("");
//		    cell2.setCellStyle(textStyle);
//		}

		row = sheet.getRow(15);
		cell2 = row.createCell(1);
		if (record.getR16_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR16_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(16);
		cell2 = row.createCell(1);
		if (record.getR17_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR17_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(17);
		cell2 = row.createCell(1);
		if (record.getR18_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR18_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(18);
		cell2 = row.createCell(1);
		if (record.getR19_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR19_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(19);
		cell2 = row.createCell(1);
		if (record.getR20_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR20_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(20);
		cell2 = row.createCell(1);
		if (record.getR21_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR21_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(21);
		cell2 = row.createCell(1);
		if (record.getR22_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR22_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(22);
		cell2 = row.createCell(1);
		if (record.getR23_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR23_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(23);
		cell2 = row.createCell(1);
		if (record.getR24_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR24_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(24);
		cell2 = row.createCell(1);
		if (record.getR25_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR25_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(25);
		cell2 = row.createCell(1);
		if (record.getR26_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR26_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(26);
		cell2 = row.createCell(1);
		if (record.getR27_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR27_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(27);
		cell2 = row.createCell(1);
		if (record.getR28_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR28_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

//		row = sheet.getRow(28);
//		cell2 = row.createCell(1);
//		if (record.getR29_OTHER_BORROW() != null) {
//		    cell2.setCellValue(record.getR29_OTHER_BORROW().doubleValue());
//		    cell2.setCellStyle(numberStyle);
//		} else {
//		    cell2.setCellValue("");
//		    cell2.setCellStyle(textStyle);
//		}

		row = sheet.getRow(29);
		cell2 = row.createCell(1);
		if (record.getR30_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR30_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(30);
		cell2 = row.createCell(1);
		if (record.getR31_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR31_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(31);
		cell2 = row.createCell(1);
		if (record.getR32_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR32_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(32);
		cell2 = row.createCell(1);
		if (record.getR33_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR33_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(33);
		cell2 = row.createCell(1);
		if (record.getR34_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR34_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(34);
		cell2 = row.createCell(1);
		if (record.getR35_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR35_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(35);
		cell2 = row.createCell(1);
		if (record.getR36_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR36_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(36);
		cell2 = row.createCell(1);
		if (record.getR37_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR37_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

//		row = sheet.getRow(37);
//		cell2 = row.createCell(1);
//		if (record.getR38_OTHER_BORROW() != null) {
//		    cell2.setCellValue(record.getR38_OTHER_BORROW().doubleValue());
//		    cell2.setCellStyle(numberStyle);
//		} else {
//		    cell2.setCellValue("");
//		    cell2.setCellStyle(textStyle);
//		}

		row = sheet.getRow(38);
		cell2 = row.createCell(1);
		if (record.getR39_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR39_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(39);
		cell2 = row.createCell(1);
		if (record.getR40_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR40_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

//		row = sheet.getRow(40);
//		cell2 = row.createCell(1);
//		if (record.getR41_OTHER_BORROW() != null) {
//		    cell2.setCellValue(record.getR41_OTHER_BORROW().doubleValue());
//		    cell2.setCellStyle(numberStyle);
//		} else {
//		    cell2.setCellValue("");
//		    cell2.setCellStyle(textStyle);
//		}

		row = sheet.getRow(41);
		cell2 = row.createCell(1);
		if (record.getR42_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR42_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(42);
		cell2 = row.createCell(1);
		if (record.getR43_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR43_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

//		row = sheet.getRow(43);
//		cell2 = row.createCell(1);
//		if (record.getR44_OTHER_BORROW() != null) {
//		    cell2.setCellValue(record.getR44_OTHER_BORROW().doubleValue());
//		    cell2.setCellStyle(numberStyle);
//		} else {
//		    cell2.setCellValue("");
//		    cell2.setCellStyle(textStyle);
//		}

		row = sheet.getRow(44);
		cell2 = row.createCell(1);
		if (record.getR45_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR45_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(45);
		cell2 = row.createCell(1);
		if (record.getR46_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR46_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(46);
		cell2 = row.createCell(1);
		if (record.getR47_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR47_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(47);
		cell2 = row.createCell(1);
		if (record.getR48_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR48_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

//		row = sheet.getRow(48);
//		cell2 = row.createCell(1);
//		if (record.getR49_OTHER_BORROW() != null) {
//		    cell2.setCellValue(record.getR49_OTHER_BORROW().doubleValue());
//		    cell2.setCellStyle(numberStyle);
//		} else {
//		    cell2.setCellValue("");
//		    cell2.setCellStyle(textStyle);
//		}

		row = sheet.getRow(49);
		cell2 = row.createCell(1);
		if (record.getR50_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR50_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(50);
		cell2 = row.createCell(1);
		if (record.getR51_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR51_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

//		row = sheet.getRow(51);
//		cell2 = row.createCell(1);
//		if (record.getR52_OTHER_BORROW() != null) {
//		    cell2.setCellValue(record.getR52_OTHER_BORROW().doubleValue());
//		    cell2.setCellStyle(numberStyle);
//		} else {
//		    cell2.setCellValue("");
//		    cell2.setCellStyle(textStyle);
//		}

		row = sheet.getRow(52);
		cell2 = row.createCell(1);
		if (record.getR53_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR53_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(53);
		cell2 = row.createCell(1);
		if (record.getR54_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR54_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(54);
		cell2 = row.createCell(1);
		if (record.getR55_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR55_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(55);
		cell2 = row.createCell(1);
		if (record.getR56_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR56_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(56);
		cell2 = row.createCell(1);
		if (record.getR57_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR57_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

//		row = sheet.getRow(57);
//		cell2 = row.createCell(1);
//		if (record.getR58_OTHER_BORROW() != null) {
//		    cell2.setCellValue(record.getR58_OTHER_BORROW().doubleValue());
//		    cell2.setCellStyle(numberStyle);
//		} else {
//		    cell2.setCellValue("");
//		    cell2.setCellStyle(textStyle);
//		}

		row = sheet.getRow(58);
		cell2 = row.createCell(1);
		if (record.getR59_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR59_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(59);
		cell2 = row.createCell(1);
		if (record.getR60_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR60_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(60);
		cell2 = row.createCell(1);
		if (record.getR61_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR61_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(61);
		cell2 = row.createCell(1);
		if (record.getR62_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR62_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		row = sheet.getRow(62);
		cell2 = row.createCell(1);
		if (record.getR63_OTHER_BORROW() != null) {
		    cell2.setCellValue(record.getR63_OTHER_BORROW().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}
		row = sheet.getRow(63);
//		
//		cell2 = row.createCell(1);
//		if (record.getR64_OTHER_BORROW() != null) {
//		    cell2.setCellValue(record.getR64_OTHER_BORROW().doubleValue());
//		    cell2.setCellStyle(numberStyle);
//		} else {
//		    cell2.setCellValue("");
//		    cell2.setCellStyle(textStyle);
//		}


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
