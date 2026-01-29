package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_M_INT_RATES_NEW_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_INT_RATES_NEW_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_INT_RATES_NEW_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_INT_RATES_NEW_Summary_Repo;
import com.bornfire.brrs.entities.M_EPR_Summary_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_Detail_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_NEW_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_NEW_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_NEW_Detail_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_NEW_Summary_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_Summary_Entity;
import com.bornfire.brrs.entities.M_SCI_E_Manual_Summary_Entity;

@Component
@Service

public class BRRS_M_INT_RATES_NEW_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_INT_RATES_NEW_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	BRRS_M_INT_RATES_NEW_Detail_Repo M_INT_RATES_NEW_Detail_Repo;

	@Autowired
	BRRS_M_INT_RATES_NEW_Archival_Detail_Repo  M_INT_RATES_NEW_Archival_Detail_Repo;
	
	@Autowired
	BRRS_M_INT_RATES_NEW_Summary_Repo M_INT_RATES_NEW_Summary_Repo;

	@Autowired
	BRRS_M_INT_RATES_NEW_Archival_Summary_Repo M_INT_RATES_NEW_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_INTRATESNEWView(String reportId, String fromdate, String todate, 
			String currency, String dtltype, Pageable pageable, String type, BigDecimal version) 
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
            List<M_INT_RATES_NEW_Archival_Summary_Entity> T1Master = 
                M_INT_RATES_NEW_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 2: RESUB ----------
        else if ("RESUB".equalsIgnoreCase(type) && version != null) {
            List<M_INT_RATES_NEW_Archival_Summary_Entity> T1Master =
            		M_INT_RATES_NEW_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 3: NORMAL ----------
        else {
        	List<M_INT_RATES_NEW_Summary_Entity> T1Master = M_INT_RATES_NEW_Summary_Repo.getdatabydateList(dateformat.parse(todate));
    		System.out.println("T1Master Size " + T1Master.size());
    		mv.addObject("reportsummary", T1Master);
        }
        
        

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_INT_RATES_NEW");
		mv.addObject("displaymode", "summary");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}
		

	
	public ModelAndView getM_INT_RATESNEWcurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String Filter, String type, String version) {

		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

		try {
			Date parsedDate = null;
			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

			String reportLable = null;
			String reportAddlCriteria_1 = null;

			// ✅ Split filter string into rowId & columnId
			if (Filter != null && Filter.contains(",")) {
				String[] parts = Filter.split(",");
				if (parts.length >= 2) {
					reportLable = parts[0];
					reportAddlCriteria_1 = parts[1];
				}
			}
			System.out.println(type);
			if ("ARCHIVAL".equals(type) && version != null) {
				System.out.println(type);
				// 🔹 Archival branch
				List<M_INT_RATES_NEW_Archival_Detail_Entity> T1Dt1;
				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = M_INT_RATES_NEW_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1, parsedDate, version);
				} else {
					T1Dt1 = M_INT_RATES_NEW_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<M_INT_RATES_NEW_Detail_Entity> T1Dt1;
				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = M_INT_RATES_NEW_Detail_Repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1, parsedDate);
				} else {
					T1Dt1 = M_INT_RATES_NEW_Detail_Repo.getdatabydateList(parsedDate);
					System.out.println("M_INT_RATES size is : " + T1Dt1.size());
					totalPages = M_INT_RATES_NEW_Detail_Repo.getdatacount(parsedDate);
					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("LISTCOUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
			}

		} catch (ParseException e) {
			e.printStackTrace();
			mv.addObject("errorMessage", "Invalid date format: " + todate);
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("errorMessage", "Unexpected error: " + e.getMessage());
		}

		// ✅ Common attributes
		mv.setViewName("BRRS/M_INT_RATES_NEW");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}
	
	
	public void updateReport(M_INT_RATES_NEW_Summary_Entity updatedEntity) {
		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		 //  Use your query to fetch by date
	    List<M_INT_RATES_NEW_Summary_Entity> list = M_INT_RATES_NEW_Summary_Repo
	        .getdatabydateList(updatedEntity.getReport_date());

	    M_INT_RATES_NEW_Summary_Entity existing;
	    if (list.isEmpty()) {
	        // Record not found — optionally create it
	        System.out.println("No record found for REPORT_DATE: " + updatedEntity.getReport_date());
	        existing = new M_INT_RATES_NEW_Summary_Entity();
	        existing.setReport_date(updatedEntity.getReport_date());
	    } else {
	        existing = list.get(0);
	    }
		
		

		try {
			// 1️⃣ Loop through R14 to R100
			for (int i = 11; i <= 35; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "nominal_interest_rate", "avg_effective_rate"};

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_INT_RATES_NEW_Summary_Entity.class.getMethod(getterName);
						Method setter = M_INT_RATES_NEW_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

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

		// Save updated entity
		System.out.println("abc");
		M_INT_RATES_NEW_Summary_Repo.save(existing);
	}
	
	
	public byte[] getM_INTRATESNEWExcel(String filename, String reportId, String fromdate, String todate, String currency,
			 String dtltype, String type, BigDecimal version) throws Exception {
logger.info("Service: Starting Excel generation process in memory.");
logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

// Convert string to Date
Date reportDate = dateformat.parse(todate);

// ARCHIVAL check
if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version != null) {
logger.info("Service: Generating ARCHIVAL report for version {}", version);
return getExcelM_INTRATESNEWARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
}
// RESUB check
else if ("RESUB".equalsIgnoreCase(type) && version != null && version != null) {
logger.info("Service: Generating RESUB report for version {}", version);


List<M_INT_RATES_NEW_Archival_Summary_Entity> T1Master =
M_INT_RATES_NEW_Archival_Summary_Repo.getdatabydateListarchival(reportDate, version);

// Generate Excel for RESUB
return BRRS_M_INTRATESResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
}




// Default (LIVE) case
List<M_INT_RATES_NEW_Summary_Entity> dataList1 = M_INT_RATES_NEW_Summary_Repo.getdatabydateList(reportDate);

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

		M_INT_RATES_NEW_Summary_Entity record = dataList1.get(i);
		System.out.println("rownumber=" + startRow + i);
		Row row = sheet.getRow(startRow + i);
		if (row == null) {
			row = sheet.createRow(startRow + i);
		}
					
					Cell cell1 = row.createCell(1);
					if (record.getR11_nominal_interest_rate() != null) {
						cell1.setCellValue(record.getR11_nominal_interest_rate().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					Cell cell2 = row.createCell(2);
					if (record.getR11_avg_effective_rate() != null) {
						cell2.setCellValue(record.getR11_avg_effective_rate().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					
					
					
					row = sheet.getRow(11);
					if (row == null) {
					    row = sheet.createRow(11);
					}

					cell1 = row.createCell(1);
					if (record.getR12_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR12_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR12_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR12_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

				


					row = sheet.getRow(12);
					if (row == null) {
					    row = sheet.createRow(12);
					}

					cell1 = row.createCell(1);
					if (record.getR13_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR13_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR13_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR13_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					



					row = sheet.getRow(13);
					if (row == null) {
					    row = sheet.createRow(13);
					}

					cell1 = row.createCell(1);
					if (record.getR14_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR14_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR14_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR14_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					



					row = sheet.getRow(14);
					if (row == null) {
					    row = sheet.createRow(14);
					}

					cell1 = row.createCell(1);
					if (record.getR15_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR15_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR15_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR15_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					


					row = sheet.getRow(15);
					if (row == null) {
					    row = sheet.createRow(15);
					}

					cell1 = row.createCell(1);
					if (record.getR16_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR16_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR16_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR16_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					



					row = sheet.getRow(16);
					if (row == null) {
					    row = sheet.createRow(16);
					}

					cell1 = row.createCell(1);
					if (record.getR17_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR17_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR17_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR17_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

				



					row = sheet.getRow(17);
					if (row == null) {
					    row = sheet.createRow(17);
					}

					cell1 = row.createCell(1);
					if (record.getR18_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR18_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR18_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR18_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

				


					row = sheet.getRow(18);
					if (row == null) {
					    row = sheet.createRow(18);
					}

					cell1 = row.createCell(1);
					if (record.getR19_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR19_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR19_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR19_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

				

					row = sheet.getRow(19);
					if (row == null) {
					    row = sheet.createRow(19);
					}

					cell1 = row.createCell(1);
					if (record.getR20_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR20_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR20_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR20_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					



					row = sheet.getRow(20);
					if (row == null) {
					    row = sheet.createRow(20);
					}

					cell1 = row.createCell(1);
					if (record.getR21_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR21_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR21_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR21_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					

					row = sheet.getRow(21);
					if (row == null) {
					    row = sheet.createRow(21);
					}

					cell1 = row.createCell(1);
					if (record.getR22_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR22_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR22_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR22_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

				


					row = sheet.getRow(22);
					if (row == null) {
					    row = sheet.createRow(22);
					}

					cell1 = row.createCell(1);
					if (record.getR23_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR23_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR23_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR23_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					
					row = sheet.getRow(23);
					if (row == null) {
					    row = sheet.createRow(23);
					}

					cell1 = row.createCell(1);
					if (record.getR24_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR24_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR24_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR24_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					

					row = sheet.getRow(24);
					if (row == null) {
					    row = sheet.createRow(24);
					}

					cell1 = row.createCell(1);
					if (record.getR25_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR25_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR25_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR25_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					



					row = sheet.getRow(25);
					if (row == null) {
					    row = sheet.createRow(25);
					}

					cell1 = row.createCell(1);
					if (record.getR26_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR26_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR26_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR26_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

				



					row = sheet.getRow(26);
					if (row == null) {
					    row = sheet.createRow(26);
					}

					cell1 = row.createCell(1);
					if (record.getR27_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR27_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR27_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR27_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

				



					row = sheet.getRow(27);
					if (row == null) {
					    row = sheet.createRow(27);
					}

					cell1 = row.createCell(1);
					if (record.getR28_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR28_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR28_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR28_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					


					row = sheet.getRow(28);
					if (row == null) {
					    row = sheet.createRow(28);
					}

					cell1 = row.createCell(1);
					if (record.getR29_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR29_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR29_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR29_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

				


					row = sheet.getRow(29);
					if (row == null) {
					    row = sheet.createRow(29);
					}

					cell1 = row.createCell(1);
					if (record.getR30_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR30_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR30_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR30_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

				



					row = sheet.getRow(30);
					if (row == null) {
					    row = sheet.createRow(30);
					}

					cell1 = row.createCell(1);
					if (record.getR31_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR31_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR31_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR31_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

				


					row = sheet.getRow(31);
					if (row == null) {
					    row = sheet.createRow(31);
					}

					cell1 = row.createCell(1);
					if (record.getR32_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR32_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR32_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR32_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

				



					row = sheet.getRow(32);
					if (row == null) {
					    row = sheet.createRow(32);
					}

					cell1 = row.createCell(1);
					if (record.getR33_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR33_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR33_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR33_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

				

					row = sheet.getRow(33);
					if (row == null) {
					    row = sheet.createRow(33);
					}

					cell1 = row.createCell(1);
					if (record.getR34_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR34_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR34_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR34_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					

					row = sheet.getRow(34);
					if (row == null) {
					    row = sheet.createRow(34);
					}

					cell1 = row.createCell(1);
					if (record.getR35_nominal_interest_rate() != null) {
					    cell1.setCellValue(record.getR35_nominal_interest_rate().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR35_avg_effective_rate() != null) {
					    cell2.setCellValue(record.getR35_avg_effective_rate().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

				

					
					
				}
				
			} else {
				
			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}
	}
	
//	public List<Object> getM_INTRATESarchival() {
//		List<Object> M_INTRATESArchivallist = new ArrayList<>();
//		List<Object> M_FXRArchivallist2 = new ArrayList<>();
//		List<Object> M_FXRArchivallist3 = new ArrayList<>();
//		try {
//			M_INTRATESArchivallist = M_INT_RATES_Archival_Summary_Repo.getM_INTRATESarchival();
			
			
//			System.out.println("countser" + M_INTRATESArchivallist.size());
//			System.out.println("countser" + M_FXRArchivallist.size());
//			System.out.println("countser" + M_FXRArchivallist.size());
//		} catch (Exception e) {
			// Log the exception
//			System.err.println("Error fetching M_SECL Archival data: " + e.getMessage());
//			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
//		}
//		return M_INTRATESArchivallist;
//	}


	public byte[] getExcelM_INTRATESNEWARCHIVAL(String filename, String reportId, String fromdate,
			String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if ("ARCHIVAL".equals(type) && version != null) {
		}
			List<M_INT_RATES_NEW_Archival_Summary_Entity> dataList1 = M_INT_RATES_NEW_Archival_Summary_Repo
					.getdatabydateListarchival(dateformat.parse(todate), version);
		

			
		
		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for M_INT_RATES_NEW report. Returning empty result.");
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
						M_INT_RATES_NEW_Archival_Summary_Entity record = dataList1.get(i);
						System.out.println("rownumber="+startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						
						Cell cell1 = row.createCell(1);
						if (record.getR11_nominal_interest_rate() != null) {
							cell1.setCellValue(record.getR11_nominal_interest_rate().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						
						Cell cell2 = row.createCell(2);
						if (record.getR11_avg_effective_rate() != null) {
							cell2.setCellValue(record.getR11_avg_effective_rate().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}
						
					
						row = sheet.getRow(11);
						if (row == null) {
						    row = sheet.createRow(11);
						}

						cell1 = row.createCell(1);
						if (record.getR12_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR12_nominal_interest_rate().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR12_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR12_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}


						row = sheet.getRow(12);
						if (row == null) {
						    row = sheet.createRow(12);
						}

						cell1 = row.createCell(1);
						if (record.getR13_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR13_nominal_interest_rate().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR13_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR13_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

					


						row = sheet.getRow(13);
						if (row == null) {
						    row = sheet.createRow(13);
						}

						cell1 = row.createCell(1);
						if (record.getR14_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR14_nominal_interest_rate().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR14_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR14_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

					


						row = sheet.getRow(14);
						if (row == null) {
						    row = sheet.createRow(14);
						}

						cell1 = row.createCell(1);
						if (record.getR15_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR15_nominal_interest_rate().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR15_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR15_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

					


						row = sheet.getRow(15);
						if (row == null) {
						    row = sheet.createRow(15);
						}

						cell1 = row.createCell(1);
						if (record.getR16_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR16_nominal_interest_rate().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR16_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR16_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

					


						row = sheet.getRow(16);
						if (row == null) {
						    row = sheet.createRow(16);
						}

						cell1 = row.createCell(1);
						if (record.getR17_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR17_nominal_interest_rate().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR17_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR17_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						


						row = sheet.getRow(17);
						if (row == null) {
						    row = sheet.createRow(17);
						}

						cell1 = row.createCell(1);
						if (record.getR18_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR18_nominal_interest_rate().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR18_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR18_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

					

						row = sheet.getRow(18);
						if (row == null) {
						    row = sheet.createRow(18);
						}

						cell1 = row.createCell(1);
						if (record.getR19_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR19_nominal_interest_rate().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR19_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR19_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

					


						row = sheet.getRow(19);
						if (row == null) {
						    row = sheet.createRow(19);
						}

						cell1 = row.createCell(1);
						if (record.getR20_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR20_nominal_interest_rate().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR20_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR20_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

					


						row = sheet.getRow(20);
						if (row == null) {
						    row = sheet.createRow(20);
						}

						cell1 = row.createCell(1);
						if (record.getR21_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR21_nominal_interest_rate().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR21_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR21_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

					


						row = sheet.getRow(21);
						if (row == null) {
						    row = sheet.createRow(21);
						}

						cell1 = row.createCell(1);
						if (record.getR22_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR22_nominal_interest_rate().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR22_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR22_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

					


						row = sheet.getRow(22);
						if (row == null) {
						    row = sheet.createRow(22);
						}

						cell1 = row.createCell(1);
						if (record.getR23_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR23_nominal_interest_rate().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR23_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR23_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

					
						
						
						// ----------------- R24 -----------------
						row = sheet.getRow(23);  // R24 = Excel row 24 (index 23)
						if (row == null) {
						    row = sheet.createRow(23);
						}

						cell1 = row.createCell(1);
						if (record.getR24_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR24_nominal_interest_rate().doubleValue());  // 
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR24_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR24_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}


						row = sheet.getRow(24);
						if (row == null) {
						    row = sheet.createRow(24);
						}

						cell1 = row.createCell(1);
						if (record.getR25_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR25_nominal_interest_rate().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR25_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR25_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						


						row = sheet.getRow(25);
						if (row == null) {
						    row = sheet.createRow(25);
						}

						cell1 = row.createCell(1);
						if (record.getR26_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR26_nominal_interest_rate().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR26_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR26_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

					


						row = sheet.getRow(26);
						if (row == null) {
						    row = sheet.createRow(26);
						}

						cell1 = row.createCell(1);
						if (record.getR27_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR27_nominal_interest_rate().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR27_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR27_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

					



						row = sheet.getRow(27);
						if (row == null) {
						    row = sheet.createRow(27);
						}

						cell1 = row.createCell(1);
						if (record.getR28_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR28_nominal_interest_rate().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR28_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR28_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						



						row = sheet.getRow(28);
						if (row == null) {
						    row = sheet.createRow(28);
						}

						cell1 = row.createCell(1);
						if (record.getR29_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR29_nominal_interest_rate().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR29_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR29_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

					

						row = sheet.getRow(29);
						if (row == null) {
						    row = sheet.createRow(29);
						}

						cell1 = row.createCell(1);
						if (record.getR30_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR30_nominal_interest_rate().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR30_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR30_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

					


						row = sheet.getRow(30);
						if (row == null) {
						    row = sheet.createRow(30);
						}

						cell1 = row.createCell(1);
						if (record.getR31_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR31_nominal_interest_rate().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR31_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR31_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

					


						row = sheet.getRow(31);
						if (row == null) {
						    row = sheet.createRow(31);
						}

						cell1 = row.createCell(1);
						if (record.getR32_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR32_nominal_interest_rate().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR32_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR32_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

					


						row = sheet.getRow(32);
						if (row == null) {
						    row = sheet.createRow(32);
						}

						cell1 = row.createCell(1);
						if (record.getR33_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR33_nominal_interest_rate().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR33_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR33_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

					


						row = sheet.getRow(33);
						if (row == null) {
						    row = sheet.createRow(33);
						}

						cell1 = row.createCell(1);
						if (record.getR34_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR34_nominal_interest_rate().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR34_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR34_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						


						row = sheet.getRow(34);
						if (row == null) {
						    row = sheet.createRow(34);
						}

						cell1 = row.createCell(1);
						if (record.getR35_nominal_interest_rate() != null) {
						    cell1.setCellValue(record.getR35_nominal_interest_rate().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR35_avg_effective_rate() != null) {
						    cell2.setCellValue(record.getR35_avg_effective_rate().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

					


						
					}
					
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
					/*
					 * public List<Object[]> getM_INTRATESResub() { List<Object[]> resubList = new
					 * ArrayList<>(); try { List<M_INT_RATES_NEW_Archival_Summary_Entity>
					 * latestArchivalList =
					 * M_INT_RATES_NEW_Archival_Summary_Repo.getdatabydateListWithVersionAll();
					 * 
					 * if (latestArchivalList != null && !latestArchivalList.isEmpty()) { for
					 * (M_INT_RATES_Archival_Summary_Entity entity : latestArchivalList) { Object[]
					 * row = new Object[] { entity.getReportDate(), entity.getReportVersion() };
					 * resubList.add(row); } System.out.println("Fetched " + resubList.size() +
					 * " record(s)"); } else { System.out.println("No archival data found."); } }
					 * catch (Exception e) {
					 * System.err.println("Error fetching M_SRWA_12F Resub data: " +
					 * e.getMessage()); e.printStackTrace(); } return resubList; }
					 */


/*
 * //Archival View public List<Object[]> getM_INTRATESArchival() {
 * List<Object[]> archivalList = new ArrayList<>();
 * 
 * try { List<M_INT_RATES_Archival_Summary_Entity> repoData =
 * M_INT_RATES_Archival_Summary_Repo .getdatabydateListWithVersionAll();
 * 
 * if (repoData != null && !repoData.isEmpty()) { for
 * (M_INT_RATES_Archival_Summary_Entity entity : repoData) { Object[] row = new
 * Object[] { entity.getReportDate(), entity.getReportVersion() };
 * archivalList.add(row); }
 * 
 * System.out.println("Fetched " + archivalList.size() + " archival records");
 * M_INT_RATES_Archival_Summary_Entity first = repoData.get(0);
 * System.out.println("Latest archival version: " + first.getReportVersion()); }
 * else { System.out.println("No archival data found."); }
 * 
 * } catch (Exception e) {
 * System.err.println("Error fetching M_INT_RATES Archival data: " +
 * e.getMessage()); e.printStackTrace(); }
 * 
 * return archivalList; }
 */

public List<Object> getM_INT_RATESNEWArchival() {
	List<Object> M_INT_RATESNEWArchivallist = new ArrayList<>();
	try {
		M_INT_RATESNEWArchivallist = M_INT_RATES_NEW_Archival_Summary_Repo.getM_INT_RATES_NEWarchival();
	
	
		System.out.println("countser" + M_INT_RATESNEWArchivallist.size());
		
	} catch (Exception e) {
		// Log the exception
		System.err.println("Error fetching M_INT_RATESNEWArchivallist Archival data: " + e.getMessage());
		e.printStackTrace();

		// Optionally, you can rethrow it or return empty list
		// throw new RuntimeException("Failed to fetch data", e);
	}
	return M_INT_RATESNEWArchivallist;
}










/*
 * //Resubmit the values , latest version and Resub Date public void
 * updateReportReSub(M_INT_RATES_Summary_Entity updatedEntity) {
 * System.out.println("Came to Resub Service");
 * System.out.println("Report Date: " + updatedEntity.getReportDate());
 * 
 * Date reportDate = updatedEntity.getReportDate(); int newVersion = 1;
 * 
 * try { //Fetch the latest archival version for this report date
 * Optional<M_INT_RATES_Archival_Summary_Entity> latestArchivalOpt =
 * M_INT_RATES_Archival_Summary_Repo
 * .getLatestArchivalVersionByDate(reportDate);
 * 
 * //Determine next version number if (latestArchivalOpt.isPresent()) {
 * M_INT_RATES_Archival_Summary_Entity latestArchival = latestArchivalOpt.get();
 * try { newVersion = Integer.parseInt(latestArchival.getReportVersion()) + 1; }
 * catch (NumberFormatException e) {
 * System.err.println("Invalid version format. Defaulting to version 1");
 * newVersion = 1; } } else {
 * System.out.println("No previous archival found for date: " + reportDate); }
 * 
 * //Prevent duplicate version number boolean exists =
 * M_INT_RATES_Archival_Summary_Repo
 * .findByReportDateAndReportVersion(reportDate, BigDecimal.valueOf(newVersion))
 * .isPresent();
 * 
 * if (exists) { throw new RuntimeException("Version " + newVersion +
 * " already exists for report date " + reportDate); }
 * 
 * //Copy summary entity to archival entity M_INT_RATES_Archival_Summary_Entity
 * archivalEntity = new M_INT_RATES_Archival_Summary_Entity();
 * org.springframework.beans.BeanUtils.copyProperties(updatedEntity,
 * archivalEntity);
 * 
 * archivalEntity.setReportDate(reportDate);
 * archivalEntity.setReportVersion(BigDecimal.valueOf(newVersion));
 * archivalEntity.setReportResubDate(new Date());
 * 
 * System.out.println("Saving new archival version: " + newVersion);
 * 
 * //Save new version to repository
 * M_INT_RATES_Archival_Summary_Repo.save(archivalEntity);
 * 
 * System.out.println(" Saved archival version successfully: " + newVersion);
 * 
 * } catch (Exception e) { e.printStackTrace(); throw new
 * RuntimeException("Error while creating archival resubmission record", e); } }
 */

/// Downloaded for Archival & Resub
public byte[] BRRS_M_INTRATESResubExcel(String filename, String reportId, String fromdate,
String todate, String currency, String dtltype,
String type, BigDecimal version) throws Exception {

logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

if (type.equals("RESUB") & version != null) {

}

List<M_INT_RATES_NEW_Archival_Summary_Entity> dataList1 =
M_INT_RATES_NEW_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

if (dataList1.isEmpty()) {
logger.warn("Service: No data found for M_INT_RATES_NEW report. Returning empty result.");
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

int startRow = 10;

if (!dataList1.isEmpty()) {
		for (int i = 0; i < dataList1.size(); i++) {
			M_INT_RATES_NEW_Archival_Summary_Entity record = dataList1.get(i);
			System.out.println("rownumber="+startRow + i);
			Row row = sheet.getRow(startRow + i);
			if (row == null) {
				row = sheet.createRow(startRow + i);
			}
			
			Cell cell1 = row.createCell(1);
			if (record.getR11_nominal_interest_rate() != null) {
				cell1.setCellValue(record.getR11_nominal_interest_rate().doubleValue());
				cell1.setCellStyle(numberStyle);
			} else {
				cell1.setCellValue("");
				cell1.setCellStyle(textStyle);
			}
			
			Cell cell2 = row.createCell(2);
			if (record.getR11_avg_effective_rate() != null) {
				cell2.setCellValue(record.getR11_avg_effective_rate().doubleValue());
				cell2.setCellStyle(numberStyle);
			} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
			}
			
			
			
			
			row = sheet.getRow(11);
			if (row == null) {
			    row = sheet.createRow(11);
			}

			cell1 = row.createCell(1);
			if (record.getR12_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR12_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR12_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR12_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(12);
			if (row == null) {
			    row = sheet.createRow(12);
			}

			cell1 = row.createCell(1);
			if (record.getR13_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR13_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR13_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR13_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(13);
			if (row == null) {
			    row = sheet.createRow(13);
			}

			cell1 = row.createCell(1);
			if (record.getR14_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR14_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR14_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR14_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		


			row = sheet.getRow(14);
			if (row == null) {
			    row = sheet.createRow(14);
			}

			cell1 = row.createCell(1);
			if (record.getR15_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR15_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR15_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR15_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(15);
			if (row == null) {
			    row = sheet.createRow(15);
			}

			cell1 = row.createCell(1);
			if (record.getR16_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR16_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR16_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR16_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(16);
			if (row == null) {
			    row = sheet.createRow(16);
			}

			cell1 = row.createCell(1);
			if (record.getR17_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR17_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR17_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR17_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		


			row = sheet.getRow(17);
			if (row == null) {
			    row = sheet.createRow(17);
			}

			cell1 = row.createCell(1);
			if (record.getR18_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR18_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR18_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR18_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		


			row = sheet.getRow(18);
			if (row == null) {
			    row = sheet.createRow(18);
			}

			cell1 = row.createCell(1);
			if (record.getR19_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR19_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR19_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR19_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(19);
			if (row == null) {
			    row = sheet.createRow(19);
			}

			cell1 = row.createCell(1);
			if (record.getR20_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR20_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR20_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR20_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(20);
			if (row == null) {
			    row = sheet.createRow(20);
			}

			cell1 = row.createCell(1);
			if (record.getR21_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR21_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR21_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR21_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		



			row = sheet.getRow(21);
			if (row == null) {
			    row = sheet.createRow(21);
			}

			cell1 = row.createCell(1);
			if (record.getR22_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR22_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR22_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR22_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(22);
			if (row == null) {
			    row = sheet.createRow(22);
			}

			cell1 = row.createCell(1);
			if (record.getR23_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR23_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR23_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR23_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			
			row = sheet.getRow(23);
			if (row == null) {
			    row = sheet.createRow(23);
			}

			cell1 = row.createCell(1);
			if (record.getR24_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR24_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR24_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR24_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		

			row = sheet.getRow(24);
			if (row == null) {
			    row = sheet.createRow(24);
			}

			cell1 = row.createCell(1);
			if (record.getR25_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR25_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR25_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR25_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		


			row = sheet.getRow(25);
			if (row == null) {
			    row = sheet.createRow(25);
			}

			cell1 = row.createCell(1);
			if (record.getR26_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR26_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR26_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR26_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		


			row = sheet.getRow(26);
			if (row == null) {
			    row = sheet.createRow(26);
			}

			cell1 = row.createCell(1);
			if (record.getR27_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR27_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR27_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR27_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		



			row = sheet.getRow(27);
			if (row == null) {
			    row = sheet.createRow(27);
			}

			cell1 = row.createCell(1);
			if (record.getR28_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR28_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR28_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR28_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}



			row = sheet.getRow(28);
			if (row == null) {
			    row = sheet.createRow(28);
			}

			cell1 = row.createCell(1);
			if (record.getR29_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR29_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR29_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR29_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(29);
			if (row == null) {
			    row = sheet.createRow(29);
			}

			cell1 = row.createCell(1);
			if (record.getR30_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR30_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR30_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR30_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}



			row = sheet.getRow(30);
			if (row == null) {
			    row = sheet.createRow(30);
			}

			cell1 = row.createCell(1);
			if (record.getR31_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR31_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR31_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR31_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		



			row = sheet.getRow(31);
			if (row == null) {
			    row = sheet.createRow(31);
			}

			cell1 = row.createCell(1);
			if (record.getR32_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR32_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR32_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR32_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		


			row = sheet.getRow(32);
			if (row == null) {
			    row = sheet.createRow(32);
			}

			cell1 = row.createCell(1);
			if (record.getR33_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR33_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR33_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR33_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		


			row = sheet.getRow(33);
			if (row == null) {
			    row = sheet.createRow(33);
			}

			cell1 = row.createCell(1);
			if (record.getR34_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR34_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR34_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR34_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		



			row = sheet.getRow(34);
			if (row == null) {
			    row = sheet.createRow(34);
			}

			cell1 = row.createCell(1);
			if (record.getR35_nominal_interest_rate() != null) {
			    cell1.setCellValue(record.getR35_nominal_interest_rate().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR35_avg_effective_rate() != null) {
			    cell2.setCellValue(record.getR35_avg_effective_rate().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		

			
		}
		
	} else {
		
	}

	// Write the final workbook content to the in-memory stream.
	workbook.write(out);

	logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

	return out.toByteArray();
			}
		}



public void updateDetailFromForm(Date reportDate, Map<String, String> params) {

	System.out.println("came to service for update ");

	for (Map.Entry<String, String> entry : params.entrySet()) {

		String key = entry.getKey();
		String value = entry.getValue();

		// ✅ Allow only valid keys for required columns
		if (!key.matches("R\\d+_C\\d+_(" + "NOMINAL_INTEREST_RATE|" + "AVG_EFFECTIVE_RATE|" 	
				+ ")")) {
			continue;
		}

		if (value == null || value.trim().isEmpty()) {
			value = "0";
		}

		String[] parts = key.split("_");
		String reportLabel = parts[0]; // R1, R2, etc.
		String addlCriteria = parts[1]; // C1, C2, etc.
		String column = String.join("_", Arrays.copyOfRange(parts, 2, parts.length));

		BigDecimal amount = new BigDecimal(value);

		List<M_INT_RATES_NEW_Detail_Entity> rows = M_INT_RATES_NEW_Detail_Repo
				.findByReportDateAndReportLableAndReportAddlCriteria1(reportDate, reportLabel, addlCriteria);

		for (M_INT_RATES_NEW_Detail_Entity row : rows) {

			if ("NOMINAL_INTEREST_RATE".equals(column)) {
				row.setNOMINAL_INTEREST_RATE(amount);

			} else if ("AVG_EFFECTIVE_RATE".equals(column)) {
				row.setAVG_EFFECTIVE_RATE(amount);

			} 
		}

		M_INT_RATES_NEW_Detail_Repo.saveAll(rows);
	}

	// ✅ CALL ORACLE PROCEDURE AFTER ALL UPDATES
	callSummaryProcedure(reportDate);
}

private void callSummaryProcedure(Date reportDate) {

	String sql = "{ call BRRS_M_INT_RATES_NEW_SUMMARY_PROCEDURE(?) }";

	jdbcTemplate.update(connection -> {
		CallableStatement cs = connection.prepareCall(sql);

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		sdf.setLenient(false);

		String formattedDate = sdf.format(reportDate);

		cs.setString(1, formattedDate);
		return cs;
	});

	System.out.println(
			"✅ Summary procedure executed for date: " + new SimpleDateFormat("dd-MM-yyyy").format(reportDate));
}




public byte[] getM_INT_RATESNEWDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
		String type, String version) {

	try {
		logger.info("Generating Excel for M_INT_RATESNEW Details...");
		System.out.println("came to Detail download service");

		// ================= ARCHIVAL HANDLING =================
		if ("ARCHIVAL".equals(type) && version != null) {
			return getM_INT_RATESNEWDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type, version);
		}

		// ================= WORKBOOK & SHEET =================
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("M_INT_RATESNEW Detail");

		BorderStyle border = BorderStyle.THIN;

		// ================= HEADER STYLE =================
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

		CellStyle rightHeaderStyle = workbook.createCellStyle();
		rightHeaderStyle.cloneStyleFrom(headerStyle);
		rightHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

		// ================= DATA STYLES =================
		CellStyle textStyle = workbook.createCellStyle();
		textStyle.setAlignment(HorizontalAlignment.LEFT);
		textStyle.setBorderTop(border);
		textStyle.setBorderBottom(border);
		textStyle.setBorderLeft(border);
		textStyle.setBorderRight(border);

		CellStyle amountStyle = workbook.createCellStyle();
		amountStyle.setAlignment(HorizontalAlignment.RIGHT);
		amountStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
		amountStyle.setBorderTop(border);
		amountStyle.setBorderBottom(border);
		amountStyle.setBorderLeft(border);
		amountStyle.setBorderRight(border);

		// ================= HEADER ROW =================
		String[] headers = { "NOMINAL_INTEREST_RATE", "AVG_EFFECTIVE_RATE",
				"REPORT LABEL", "REPORT ADDL CRITERIA1", "REPORT DATE" };

		XSSFRow headerRow = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle((i == 4) ? rightHeaderStyle : headerStyle);
			sheet.setColumnWidth(i, 6000);
		}

		// ================= DATA FETCH =================
		Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
		List<M_INT_RATES_NEW_Detail_Entity> reportData = M_INT_RATES_NEW_Detail_Repo.getdatabydateList(parsedToDate);

		// ================= DATA ROWS =================
		int rowIndex = 1;

		if (reportData != null && !reportData.isEmpty()) {
			for (M_INT_RATES_NEW_Detail_Entity item : reportData) {

				XSSFRow row = sheet.createRow(rowIndex++);

				Cell c0 = row.createCell(0);
				c0.setCellValue(item.getNOMINAL_INTEREST_RATE() != null ? item.getNOMINAL_INTEREST_RATE().doubleValue() : 0);
				c0.setCellStyle(amountStyle);

				
				Cell c1 = row.createCell(1);
				c1.setCellValue(item.getAVG_EFFECTIVE_RATE() != null ? item.getAVG_EFFECTIVE_RATE().doubleValue() : 0);
				c1.setCellStyle(amountStyle);

			

			
				// Column 5 - REPORT LABEL
				Cell	 c2 = row.createCell(2);
				c2.setCellValue(item.getReportLable());
				c2.setCellStyle(textStyle);

				// Column 6 - REPORT ADDL CRITERIA 1
				Cell c3 = row.createCell(3);
				c3.setCellValue(item.getReportAddlCriteria1());
				c3.setCellStyle(textStyle);

				// Column 7 - REPORT DATE
				Cell c4 = row.createCell(4);
				c4.setCellValue(item.getReportDate() != null
						? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
						: "");
				c4.setCellStyle(textStyle);
			}
		} else {
			logger.info("No data found for M_INT_RATES_NEW — only header written.");
		}

		// ================= WRITE FILE =================
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		workbook.close();

		logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);

		return bos.toByteArray();

	} catch (Exception e) {
		logger.error("Error generating M_INT_RATES_NEW Excel", e);
		return new byte[0];
	}
}

public byte[] getM_INT_RATESNEWDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
		String dtltype, String type, String version) {

	try {
		logger.info("Generating Excel for BRRS_M_INT_RATESNEW ARCHIVAL Details...");
		System.out.println("came to Detail download service");

		// ================= WORKBOOK & SHEET =================
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("M_INT_RATESDetail");

		BorderStyle border = BorderStyle.THIN;

		// ================= HEADER STYLE =================
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

		CellStyle rightHeaderStyle = workbook.createCellStyle();
		rightHeaderStyle.cloneStyleFrom(headerStyle);
		rightHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

		// ================= DATA STYLES =================
		CellStyle textStyle = workbook.createCellStyle();
		textStyle.setAlignment(HorizontalAlignment.LEFT);
		textStyle.setBorderTop(border);
		textStyle.setBorderBottom(border);
		textStyle.setBorderLeft(border);
		textStyle.setBorderRight(border);

		CellStyle amountStyle = workbook.createCellStyle();
		amountStyle.setAlignment(HorizontalAlignment.RIGHT);
		amountStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
		amountStyle.setBorderTop(border);
		amountStyle.setBorderBottom(border);
		amountStyle.setBorderLeft(border);
		amountStyle.setBorderRight(border);

		// ================= HEADER ROW =================
		String[] headers = { "NOMINAL_INTEREST_RATE", "AVG_EFFECTIVE_RATE", 
				"REPORT LABEL", "REPORT ADDL CRITERIA1", "REPORT DATE" };

		XSSFRow headerRow = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle((i == 4) ? rightHeaderStyle : headerStyle);
			sheet.setColumnWidth(i, 6000);
		}

		// ================= DATA FETCH =================
		Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
		List<M_INT_RATES_NEW_Archival_Detail_Entity> reportData = M_INT_RATES_NEW_Archival_Detail_Repo.getdatabydateList(parsedToDate,
				version);

		// ================= DATA ROWS =================
		int rowIndex = 1;

		if (reportData != null && !reportData.isEmpty()) {
			for (M_INT_RATES_NEW_Archival_Detail_Entity item : reportData) {

				XSSFRow row = sheet.createRow(rowIndex++);

				Cell c0 = row.createCell(0);
				c0.setCellValue(item.getNOMINAL_INTEREST_RATE() != null ? item.getNOMINAL_INTEREST_RATE().doubleValue() : 0);
				c0.setCellStyle(amountStyle);

				
				Cell c1 = row.createCell(1);
				c1.setCellValue(item.getAVG_EFFECTIVE_RATE() != null ? item.getAVG_EFFECTIVE_RATE().doubleValue() : 0);
				c1.setCellStyle(amountStyle);

			

			

			
				// Column 5 - REPORT LABEL
				Cell	 c2 = row.createCell(2);
				c2.setCellValue(item.getReportLable());
				c2.setCellStyle(textStyle);

				// Column 6 - REPORT ADDL CRITERIA 1
				Cell c3 = row.createCell(3);
				c3.setCellValue(item.getReportAddlCriteria1());
				c3.setCellStyle(textStyle);

				// Column 7 - REPORT DATE
				Cell c4 = row.createCell(4);
				c4.setCellValue(item.getReportDate() != null
						? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
						: "");
				c4.setCellStyle(textStyle);
			}
		} else {
			logger.info("No archival data found for M_INT_RATESNEW — only header written.");
		}

		// ================= WRITE FILE =================
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		workbook.close();

		logger.info("ARCHIVAL Excel generation completed with {} row(s).",
				reportData != null ? reportData.size() : 0);

		return bos.toByteArray();

	} catch (Exception e) {
		logger.error("Error generating M_INT_RATESNEW ARCHIVAL Excel", e);
		return new byte[0];
	}
}














}

	
	
	
	
	
	
	

	


					



	
	
	
	
	
