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

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.FillPatternType;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.springframework.jdbc.core.JdbcTemplate;
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

import com.bornfire.brrs.entities.BDISB1_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_INT_RATES_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_INT_RATES_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_INT_RATES_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_INT_RATES_Summary_Repo;
import com.bornfire.brrs.entities.M_INT_RATES_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_Detail_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_Summary_Entity;

@Component
@Service

public class BRRS_M_INT_RATES_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_INT_RATES_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	BRRS_M_INT_RATES_Detail_Repo M_INT_RATES_Detail_Repo;

	@Autowired
	BRRS_M_INT_RATES_Archival_Detail_Repo  M_INT_RATES_Archival_Detail_Repo;
	
	@Autowired
	BRRS_M_INT_RATES_Summary_Repo M_INT_RATES_Summary_Repo;

	@Autowired
	BRRS_M_INT_RATES_Archival_Summary_Repo M_INT_RATES_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_INTRATESView(String reportId, String fromdate, String todate, 
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
            List<M_INT_RATES_Archival_Summary_Entity> T1Master = 
                M_INT_RATES_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 2: RESUB ----------
        else if ("RESUB".equalsIgnoreCase(type) && version != null) {
            List<M_INT_RATES_Archival_Summary_Entity> T1Master =
            		M_INT_RATES_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 3: NORMAL ----------
        else {
        	List<M_INT_RATES_Summary_Entity> T1Master = M_INT_RATES_Summary_Repo.getdatabydateList(dateformat.parse(todate));
    		System.out.println("T1Master Size " + T1Master.size());
    		mv.addObject("reportsummary", T1Master);
        }
        
        

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_INT_RATES");
		mv.addObject("displaymode", "summary");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}
		

	
	public ModelAndView getM_INT_RATEScurrentDtl(String reportId, String fromdate, String todate, String currency,
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
				List<M_INT_RATES_Archival_Detail_Entity> T1Dt1;
				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = M_INT_RATES_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1, parsedDate, version);
				} else {
					T1Dt1 = M_INT_RATES_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<M_INT_RATES_Detail_Entity> T1Dt1;
				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = M_INT_RATES_Detail_Repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1, parsedDate);
				} else {
					T1Dt1 = M_INT_RATES_Detail_Repo.getdatabydateList(parsedDate);
					System.out.println("M_INT_RATES size is : " + T1Dt1.size());
					totalPages = M_INT_RATES_Detail_Repo.getdatacount(parsedDate);
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
		mv.setViewName("BRRS/M_INT_RATES");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}
	
	
	public void updateReport(M_INT_RATES_Summary_Entity updatedEntity) {
		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		M_INT_RATES_Summary_Entity existing = M_INT_RATES_Summary_Repo
				.findTopByReportDateOrderByReportVersionDesc(updatedEntity.getReportDate())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

		try {
			// 1️⃣ Loop through R14 to R100
			for (int i = 11; i <= 41; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "LENDING", "NOMINAL_INTEREST_RATE", "AVG_EFFECTIVE_RATE", "VOLUME"};

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_INT_RATES_Summary_Entity.class.getMethod(getterName);
						Method setter = M_INT_RATES_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

			// 2️⃣ Handle R100 total fields using same structure
			String prefix = "R41_";
			String[] totalFields = { "LENDING", "NOMINAL_INTEREST_RATE", "AVG_EFFECTIVE_RATE", "VOLUME" };

			for (String field : totalFields) {
				String getterName = "get" + prefix + field;
				String setterName = "set" + prefix + field;

				try {
					Method getter = M_INT_RATES_Summary_Entity.class.getMethod(getterName);
					Method setter = M_INT_RATES_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

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
		M_INT_RATES_Summary_Repo.save(existing);
	}
	
	
	public byte[] getM_INTRATESExcel(String filename, String reportId, String fromdate, String todate, String currency,
			 String dtltype, String type, BigDecimal version) throws Exception {
logger.info("Service: Starting Excel generation process in memory.");
logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

// Convert string to Date
Date reportDate = dateformat.parse(todate);

// ARCHIVAL check
if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version != null) {
logger.info("Service: Generating ARCHIVAL report for version {}", version);
return getExcelM_INTRATESARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
}
// RESUB check
else if ("RESUB".equalsIgnoreCase(type) && version != null && version != null) {
logger.info("Service: Generating RESUB report for version {}", version);


List<M_INT_RATES_Archival_Summary_Entity> T1Master =
M_INT_RATES_Archival_Summary_Repo.getdatabydateListarchival(reportDate, version);

// Generate Excel for RESUB
return BRRS_M_INTRATESResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
}




// Default (LIVE) case
List<M_INT_RATES_Summary_Entity> dataList1 = M_INT_RATES_Summary_Repo.getdatabydateList(reportDate);

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

		M_INT_RATES_Summary_Entity record = dataList1.get(i);
		System.out.println("rownumber=" + startRow + i);
		Row row = sheet.getRow(startRow + i);
		if (row == null) {
			row = sheet.createRow(startRow + i);
		}
					
					Cell cell1 = row.createCell(1);
					if (record.getR11_NOMINAL_INTEREST_RATE() != null) {
						cell1.setCellValue(record.getR11_NOMINAL_INTEREST_RATE().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					Cell cell2 = row.createCell(2);
					if (record.getR11_AVG_EFFECTIVE_RATE() != null) {
						cell2.setCellValue(record.getR11_AVG_EFFECTIVE_RATE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					Cell cell3 = row.createCell(3);
					if (record.getR11_VOLUME() != null) {
						cell3.setCellValue(record.getR11_VOLUME().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					row = sheet.getRow(11);
					if (row == null) {
					    row = sheet.createRow(11);
					}

					cell1 = row.createCell(1);
					if (record.getR12_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR12_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR12_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR12_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR12_VOLUME() != null) {
					    cell3.setCellValue(record.getR12_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(12);
					if (row == null) {
					    row = sheet.createRow(12);
					}

					cell1 = row.createCell(1);
					if (record.getR13_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR13_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR13_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR13_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR13_VOLUME() != null) {
					    cell3.setCellValue(record.getR13_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(13);
					if (row == null) {
					    row = sheet.createRow(13);
					}

					cell1 = row.createCell(1);
					if (record.getR14_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR14_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR14_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR14_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_VOLUME() != null) {
					    cell3.setCellValue(record.getR14_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(14);
					if (row == null) {
					    row = sheet.createRow(14);
					}

					cell1 = row.createCell(1);
					if (record.getR15_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR15_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR15_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR15_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_VOLUME() != null) {
					    cell3.setCellValue(record.getR15_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(15);
					if (row == null) {
					    row = sheet.createRow(15);
					}

					cell1 = row.createCell(1);
					if (record.getR16_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR16_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR16_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR16_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR16_VOLUME() != null) {
					    cell3.setCellValue(record.getR16_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(16);
					if (row == null) {
					    row = sheet.createRow(16);
					}

					cell1 = row.createCell(1);
					if (record.getR17_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR17_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR17_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR17_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR17_VOLUME() != null) {
					    cell3.setCellValue(record.getR17_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(17);
					if (row == null) {
					    row = sheet.createRow(17);
					}

					cell1 = row.createCell(1);
					if (record.getR18_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR18_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR18_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR18_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR18_VOLUME() != null) {
					    cell3.setCellValue(record.getR18_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(18);
					if (row == null) {
					    row = sheet.createRow(18);
					}

					cell1 = row.createCell(1);
					if (record.getR19_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR19_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR19_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR19_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR19_VOLUME() != null) {
					    cell3.setCellValue(record.getR19_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(19);
					if (row == null) {
					    row = sheet.createRow(19);
					}

					cell1 = row.createCell(1);
					if (record.getR20_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR20_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR20_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR20_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR20_VOLUME() != null) {
					    cell3.setCellValue(record.getR20_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(20);
					if (row == null) {
					    row = sheet.createRow(20);
					}

					cell1 = row.createCell(1);
					if (record.getR21_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR21_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR21_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR21_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR21_VOLUME() != null) {
					    cell3.setCellValue(record.getR21_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(21);
					if (row == null) {
					    row = sheet.createRow(21);
					}

					cell1 = row.createCell(1);
					if (record.getR22_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR22_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR22_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR22_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR22_VOLUME() != null) {
					    cell3.setCellValue(record.getR22_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(22);
					if (row == null) {
					    row = sheet.createRow(22);
					}

					cell1 = row.createCell(1);
					if (record.getR23_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR23_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR23_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR23_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR23_VOLUME() != null) {
					    cell3.setCellValue(record.getR23_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}


					row = sheet.getRow(24);
					if (row == null) {
					    row = sheet.createRow(24);
					}

					cell1 = row.createCell(1);
					if (record.getR25_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR25_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR25_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR25_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR25_VOLUME() != null) {
					    cell3.setCellValue(record.getR25_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(25);
					if (row == null) {
					    row = sheet.createRow(25);
					}

					cell1 = row.createCell(1);
					if (record.getR26_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR26_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR26_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR26_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR26_VOLUME() != null) {
					    cell3.setCellValue(record.getR26_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(26);
					if (row == null) {
					    row = sheet.createRow(26);
					}

					cell1 = row.createCell(1);
					if (record.getR27_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR27_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR27_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR27_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR27_VOLUME() != null) {
					    cell3.setCellValue(record.getR27_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(27);
					if (row == null) {
					    row = sheet.createRow(27);
					}

					cell1 = row.createCell(1);
					if (record.getR28_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR28_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR28_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR28_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR28_VOLUME() != null) {
					    cell3.setCellValue(record.getR28_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(28);
					if (row == null) {
					    row = sheet.createRow(28);
					}

					cell1 = row.createCell(1);
					if (record.getR29_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR29_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR29_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR29_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR29_VOLUME() != null) {
					    cell3.setCellValue(record.getR29_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(29);
					if (row == null) {
					    row = sheet.createRow(29);
					}

					cell1 = row.createCell(1);
					if (record.getR30_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR30_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR30_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR30_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR30_VOLUME() != null) {
					    cell3.setCellValue(record.getR30_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(30);
					if (row == null) {
					    row = sheet.createRow(30);
					}

					cell1 = row.createCell(1);
					if (record.getR31_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR31_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR31_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR31_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR31_VOLUME() != null) {
					    cell3.setCellValue(record.getR31_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(31);
					if (row == null) {
					    row = sheet.createRow(31);
					}

					cell1 = row.createCell(1);
					if (record.getR32_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR32_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR32_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR32_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR32_VOLUME() != null) {
					    cell3.setCellValue(record.getR32_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(32);
					if (row == null) {
					    row = sheet.createRow(32);
					}

					cell1 = row.createCell(1);
					if (record.getR33_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR33_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR33_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR33_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR33_VOLUME() != null) {
					    cell3.setCellValue(record.getR33_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(33);
					if (row == null) {
					    row = sheet.createRow(33);
					}

					cell1 = row.createCell(1);
					if (record.getR34_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR34_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR34_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR34_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR34_VOLUME() != null) {
					    cell3.setCellValue(record.getR34_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(34);
					if (row == null) {
					    row = sheet.createRow(34);
					}

					cell1 = row.createCell(1);
					if (record.getR35_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR35_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR35_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR35_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR35_VOLUME() != null) {
					    cell3.setCellValue(record.getR35_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(35);
					if (row == null) {
					    row = sheet.createRow(35);
					}

					cell1 = row.createCell(1);
					if (record.getR36_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR36_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR36_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR36_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR36_VOLUME() != null) {
					    cell3.setCellValue(record.getR36_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(36);
					if (row == null) {
					    row = sheet.createRow(36);
					}

					cell1 = row.createCell(1);
					if (record.getR37_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR37_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR37_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR37_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR37_VOLUME() != null) {
					    cell3.setCellValue(record.getR37_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(37);
					if (row == null) {
					    row = sheet.createRow(37);
					}

					cell1 = row.createCell(1);
					if (record.getR38_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR38_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR38_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR38_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR38_VOLUME() != null) {
					    cell3.setCellValue(record.getR38_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(38);
					if (row == null) {
					    row = sheet.createRow(38);
					}

					cell1 = row.createCell(1);
					if (record.getR39_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR39_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR39_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR39_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR39_VOLUME() != null) {
					    cell3.setCellValue(record.getR39_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(39);
					if (row == null) {
					    row = sheet.createRow(39);
					}

					cell1 = row.createCell(1);
					if (record.getR40_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR40_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR40_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR40_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR40_VOLUME() != null) {
					    cell3.setCellValue(record.getR40_VOLUME().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}



					row = sheet.getRow(40);
					if (row == null) {
					    row = sheet.createRow(40);
					}

					cell1 = row.createCell(1);
					if (record.getR41_NOMINAL_INTEREST_RATE() != null) {
					    cell1.setCellValue(record.getR41_NOMINAL_INTEREST_RATE().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR41_AVG_EFFECTIVE_RATE() != null) {
					    cell2.setCellValue(record.getR41_AVG_EFFECTIVE_RATE().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR41_VOLUME() != null) {
					    cell3.setCellValue(record.getR41_VOLUME().doubleValue());
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


	public byte[] getExcelM_INTRATESARCHIVAL(String filename, String reportId, String fromdate,
			String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if ("ARCHIVAL".equals(type) && version != null) {
		}
			List<M_INT_RATES_Archival_Summary_Entity> dataList1 = M_INT_RATES_Archival_Summary_Repo
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
						M_INT_RATES_Archival_Summary_Entity record = dataList1.get(i);
						System.out.println("rownumber="+startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						
						Cell cell1 = row.createCell(1);
						if (record.getR11_NOMINAL_INTEREST_RATE() != null) {
							cell1.setCellValue(record.getR11_NOMINAL_INTEREST_RATE().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						
						Cell cell2 = row.createCell(2);
						if (record.getR11_AVG_EFFECTIVE_RATE() != null) {
							cell2.setCellValue(record.getR11_AVG_EFFECTIVE_RATE().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}
						
						Cell cell3 = row.createCell(3);
						if (record.getR11_VOLUME() != null) {
							cell3.setCellValue(record.getR11_VOLUME().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}
						
						
						row = sheet.getRow(11);
						if (row == null) {
						    row = sheet.createRow(11);
						}

						cell1 = row.createCell(1);
						if (record.getR12_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR12_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR12_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR12_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR12_VOLUME() != null) {
						    cell3.setCellValue(record.getR12_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(12);
						if (row == null) {
						    row = sheet.createRow(12);
						}

						cell1 = row.createCell(1);
						if (record.getR13_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR13_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR13_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR13_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR13_VOLUME() != null) {
						    cell3.setCellValue(record.getR13_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(13);
						if (row == null) {
						    row = sheet.createRow(13);
						}

						cell1 = row.createCell(1);
						if (record.getR14_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR14_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR14_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR14_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR14_VOLUME() != null) {
						    cell3.setCellValue(record.getR14_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(14);
						if (row == null) {
						    row = sheet.createRow(14);
						}

						cell1 = row.createCell(1);
						if (record.getR15_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR15_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR15_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR15_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR15_VOLUME() != null) {
						    cell3.setCellValue(record.getR15_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(15);
						if (row == null) {
						    row = sheet.createRow(15);
						}

						cell1 = row.createCell(1);
						if (record.getR16_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR16_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR16_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR16_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR16_VOLUME() != null) {
						    cell3.setCellValue(record.getR16_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(16);
						if (row == null) {
						    row = sheet.createRow(16);
						}

						cell1 = row.createCell(1);
						if (record.getR17_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR17_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR17_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR17_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR17_VOLUME() != null) {
						    cell3.setCellValue(record.getR17_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(17);
						if (row == null) {
						    row = sheet.createRow(17);
						}

						cell1 = row.createCell(1);
						if (record.getR18_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR18_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR18_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR18_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR18_VOLUME() != null) {
						    cell3.setCellValue(record.getR18_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(18);
						if (row == null) {
						    row = sheet.createRow(18);
						}

						cell1 = row.createCell(1);
						if (record.getR19_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR19_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR19_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR19_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR19_VOLUME() != null) {
						    cell3.setCellValue(record.getR19_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(19);
						if (row == null) {
						    row = sheet.createRow(19);
						}

						cell1 = row.createCell(1);
						if (record.getR20_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR20_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR20_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR20_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR20_VOLUME() != null) {
						    cell3.setCellValue(record.getR20_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(20);
						if (row == null) {
						    row = sheet.createRow(20);
						}

						cell1 = row.createCell(1);
						if (record.getR21_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR21_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR21_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR21_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR21_VOLUME() != null) {
						    cell3.setCellValue(record.getR21_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(21);
						if (row == null) {
						    row = sheet.createRow(21);
						}

						cell1 = row.createCell(1);
						if (record.getR22_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR22_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR22_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR22_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR22_VOLUME() != null) {
						    cell3.setCellValue(record.getR22_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(22);
						if (row == null) {
						    row = sheet.createRow(22);
						}

						cell1 = row.createCell(1);
						if (record.getR23_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR23_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR23_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR23_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR23_VOLUME() != null) {
						    cell3.setCellValue(record.getR23_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}
						
						
						/*
						 * // ----------------- R24 ----------------- row = sheet.getRow(23); // R24 =
						 * Excel row 24 (index 23) if (row == null) { row = sheet.createRow(23); }
						 * 
						 * cell1 = row.createCell(1); if (record.getR24_NOMINAL_INTEREST_RATE() != null)
						 * { cell1.setCellValue(record.getR24_NOMINAL_INTEREST_RATE()); // <-- use text
						 * if 200-200 cell1.setCellStyle(textStyle); } else { cell1.setCellValue("");
						 * cell1.setCellStyle(textStyle); }
						 * 
						 * cell2 = row.createCell(2); if (record.getR24_AVG_EFFECTIVE_RATE() != null) {
						 * cell2.setCellValue(record.getR24_AVG_EFFECTIVE_RATE());
						 * cell2.setCellStyle(textStyle); } else { cell2.setCellValue("");
						 * cell2.setCellStyle(textStyle); }
						 */

						row = sheet.getRow(24);
						if (row == null) {
						    row = sheet.createRow(24);
						}

						cell1 = row.createCell(1);
						if (record.getR25_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR25_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR25_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR25_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR25_VOLUME() != null) {
						    cell3.setCellValue(record.getR25_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(25);
						if (row == null) {
						    row = sheet.createRow(25);
						}

						cell1 = row.createCell(1);
						if (record.getR26_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR26_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR26_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR26_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR26_VOLUME() != null) {
						    cell3.setCellValue(record.getR26_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(26);
						if (row == null) {
						    row = sheet.createRow(26);
						}

						cell1 = row.createCell(1);
						if (record.getR27_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR27_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR27_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR27_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR27_VOLUME() != null) {
						    cell3.setCellValue(record.getR27_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(27);
						if (row == null) {
						    row = sheet.createRow(27);
						}

						cell1 = row.createCell(1);
						if (record.getR28_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR28_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR28_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR28_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR28_VOLUME() != null) {
						    cell3.setCellValue(record.getR28_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(28);
						if (row == null) {
						    row = sheet.createRow(28);
						}

						cell1 = row.createCell(1);
						if (record.getR29_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR29_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR29_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR29_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR29_VOLUME() != null) {
						    cell3.setCellValue(record.getR29_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(29);
						if (row == null) {
						    row = sheet.createRow(29);
						}

						cell1 = row.createCell(1);
						if (record.getR30_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR30_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR30_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR30_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR30_VOLUME() != null) {
						    cell3.setCellValue(record.getR30_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(30);
						if (row == null) {
						    row = sheet.createRow(30);
						}

						cell1 = row.createCell(1);
						if (record.getR31_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR31_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR31_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR31_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR31_VOLUME() != null) {
						    cell3.setCellValue(record.getR31_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(31);
						if (row == null) {
						    row = sheet.createRow(31);
						}

						cell1 = row.createCell(1);
						if (record.getR32_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR32_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR32_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR32_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR32_VOLUME() != null) {
						    cell3.setCellValue(record.getR32_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(32);
						if (row == null) {
						    row = sheet.createRow(32);
						}

						cell1 = row.createCell(1);
						if (record.getR33_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR33_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR33_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR33_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR33_VOLUME() != null) {
						    cell3.setCellValue(record.getR33_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(33);
						if (row == null) {
						    row = sheet.createRow(33);
						}

						cell1 = row.createCell(1);
						if (record.getR34_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR34_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR34_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR34_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR34_VOLUME() != null) {
						    cell3.setCellValue(record.getR34_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(34);
						if (row == null) {
						    row = sheet.createRow(34);
						}

						cell1 = row.createCell(1);
						if (record.getR35_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR35_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR35_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR35_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR35_VOLUME() != null) {
						    cell3.setCellValue(record.getR35_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(35);
						if (row == null) {
						    row = sheet.createRow(35);
						}

						cell1 = row.createCell(1);
						if (record.getR36_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR36_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR36_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR36_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR36_VOLUME() != null) {
						    cell3.setCellValue(record.getR36_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(36);
						if (row == null) {
						    row = sheet.createRow(36);
						}

						cell1 = row.createCell(1);
						if (record.getR37_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR37_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR37_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR37_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR37_VOLUME() != null) {
						    cell3.setCellValue(record.getR37_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(37);
						if (row == null) {
						    row = sheet.createRow(37);
						}

						cell1 = row.createCell(1);
						if (record.getR38_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR38_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR38_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR38_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR38_VOLUME() != null) {
						    cell3.setCellValue(record.getR38_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(38);
						if (row == null) {
						    row = sheet.createRow(38);
						}

						cell1 = row.createCell(1);
						if (record.getR39_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR39_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR39_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR39_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR39_VOLUME() != null) {
						    cell3.setCellValue(record.getR39_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(39);
						if (row == null) {
						    row = sheet.createRow(39);
						}

						cell1 = row.createCell(1);
						if (record.getR40_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR40_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR40_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR40_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR40_VOLUME() != null) {
						    cell3.setCellValue(record.getR40_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(40);
						if (row == null) {
						    row = sheet.createRow(40);
						}

						cell1 = row.createCell(1);
						if (record.getR41_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR41_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR41_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR41_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR41_VOLUME() != null) {
						    cell3.setCellValue(record.getR41_VOLUME().doubleValue());
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
	
/////////////////////////////////////////RESUBMISSION///////////////////////////////////////////////////////////////////	
/// Report Date | Report Version | Domain
/// RESUB VIEW
public List<Object[]> getM_INTRATESResub() {
List<Object[]> resubList = new ArrayList<>();
try {
List<M_INT_RATES_Archival_Summary_Entity> latestArchivalList = 
M_INT_RATES_Archival_Summary_Repo.getdatabydateListWithVersionAll();

if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
for (M_INT_RATES_Archival_Summary_Entity entity : latestArchivalList) {
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

public List<Object> getM_INT_RATESArchival() {
	List<Object> M_INT_RATESArchivallist = new ArrayList<>();
	try {
		M_INT_RATESArchivallist = M_INT_RATES_Archival_Summary_Repo.getM_INTRATESarchival();
	
	
		System.out.println("countser" + M_INT_RATESArchivallist.size());
		
	} catch (Exception e) {
		// Log the exception
		System.err.println("Error fetching M_INT_RATESArchivallist Archival data: " + e.getMessage());
		e.printStackTrace();

		// Optionally, you can rethrow it or return empty list
		// throw new RuntimeException("Failed to fetch data", e);
	}
	return M_INT_RATESArchivallist;
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

List<M_INT_RATES_Archival_Summary_Entity> dataList1 =
M_INT_RATES_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

if (dataList1.isEmpty()) {
logger.warn("Service: No data found for M_INT_RATES report. Returning empty result.");
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
			M_INT_RATES_Archival_Summary_Entity record = dataList1.get(i);
			System.out.println("rownumber="+startRow + i);
			Row row = sheet.getRow(startRow + i);
			if (row == null) {
				row = sheet.createRow(startRow + i);
			}
			
			Cell cell1 = row.createCell(1);
			if (record.getR11_NOMINAL_INTEREST_RATE() != null) {
				cell1.setCellValue(record.getR11_NOMINAL_INTEREST_RATE().doubleValue());
				cell1.setCellStyle(numberStyle);
			} else {
				cell1.setCellValue("");
				cell1.setCellStyle(textStyle);
			}
			
			Cell cell2 = row.createCell(2);
			if (record.getR11_AVG_EFFECTIVE_RATE() != null) {
				cell2.setCellValue(record.getR11_AVG_EFFECTIVE_RATE().doubleValue());
				cell2.setCellStyle(numberStyle);
			} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
			}
			
			Cell cell3 = row.createCell(3);
			if (record.getR11_VOLUME() != null) {
				cell3.setCellValue(record.getR11_VOLUME().doubleValue());
				cell3.setCellStyle(numberStyle);
			} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
			}
			
			
			row = sheet.getRow(11);
			if (row == null) {
			    row = sheet.createRow(11);
			}

			cell1 = row.createCell(1);
			if (record.getR12_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR12_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR12_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR12_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR12_VOLUME() != null) {
			    cell3.setCellValue(record.getR12_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(12);
			if (row == null) {
			    row = sheet.createRow(12);
			}

			cell1 = row.createCell(1);
			if (record.getR13_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR13_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR13_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR13_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR13_VOLUME() != null) {
			    cell3.setCellValue(record.getR13_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(13);
			if (row == null) {
			    row = sheet.createRow(13);
			}

			cell1 = row.createCell(1);
			if (record.getR14_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR14_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR14_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR14_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR14_VOLUME() != null) {
			    cell3.setCellValue(record.getR14_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(14);
			if (row == null) {
			    row = sheet.createRow(14);
			}

			cell1 = row.createCell(1);
			if (record.getR15_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR15_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR15_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR15_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR15_VOLUME() != null) {
			    cell3.setCellValue(record.getR15_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(15);
			if (row == null) {
			    row = sheet.createRow(15);
			}

			cell1 = row.createCell(1);
			if (record.getR16_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR16_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR16_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR16_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR16_VOLUME() != null) {
			    cell3.setCellValue(record.getR16_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(16);
			if (row == null) {
			    row = sheet.createRow(16);
			}

			cell1 = row.createCell(1);
			if (record.getR17_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR17_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR17_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR17_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR17_VOLUME() != null) {
			    cell3.setCellValue(record.getR17_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(17);
			if (row == null) {
			    row = sheet.createRow(17);
			}

			cell1 = row.createCell(1);
			if (record.getR18_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR18_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR18_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR18_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR18_VOLUME() != null) {
			    cell3.setCellValue(record.getR18_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(18);
			if (row == null) {
			    row = sheet.createRow(18);
			}

			cell1 = row.createCell(1);
			if (record.getR19_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR19_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR19_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR19_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR19_VOLUME() != null) {
			    cell3.setCellValue(record.getR19_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(19);
			if (row == null) {
			    row = sheet.createRow(19);
			}

			cell1 = row.createCell(1);
			if (record.getR20_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR20_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR20_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR20_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR20_VOLUME() != null) {
			    cell3.setCellValue(record.getR20_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(20);
			if (row == null) {
			    row = sheet.createRow(20);
			}

			cell1 = row.createCell(1);
			if (record.getR21_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR21_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR21_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR21_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR21_VOLUME() != null) {
			    cell3.setCellValue(record.getR21_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(21);
			if (row == null) {
			    row = sheet.createRow(21);
			}

			cell1 = row.createCell(1);
			if (record.getR22_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR22_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR22_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR22_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR22_VOLUME() != null) {
			    cell3.setCellValue(record.getR22_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(22);
			if (row == null) {
			    row = sheet.createRow(22);
			}

			cell1 = row.createCell(1);
			if (record.getR23_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR23_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR23_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR23_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR23_VOLUME() != null) {
			    cell3.setCellValue(record.getR23_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}


			row = sheet.getRow(24);
			if (row == null) {
			    row = sheet.createRow(24);
			}

			cell1 = row.createCell(1);
			if (record.getR25_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR25_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR25_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR25_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR25_VOLUME() != null) {
			    cell3.setCellValue(record.getR25_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(25);
			if (row == null) {
			    row = sheet.createRow(25);
			}

			cell1 = row.createCell(1);
			if (record.getR26_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR26_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR26_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR26_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR26_VOLUME() != null) {
			    cell3.setCellValue(record.getR26_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(26);
			if (row == null) {
			    row = sheet.createRow(26);
			}

			cell1 = row.createCell(1);
			if (record.getR27_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR27_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR27_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR27_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR27_VOLUME() != null) {
			    cell3.setCellValue(record.getR27_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(27);
			if (row == null) {
			    row = sheet.createRow(27);
			}

			cell1 = row.createCell(1);
			if (record.getR28_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR28_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR28_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR28_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR28_VOLUME() != null) {
			    cell3.setCellValue(record.getR28_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(28);
			if (row == null) {
			    row = sheet.createRow(28);
			}

			cell1 = row.createCell(1);
			if (record.getR29_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR29_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR29_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR29_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR29_VOLUME() != null) {
			    cell3.setCellValue(record.getR29_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(29);
			if (row == null) {
			    row = sheet.createRow(29);
			}

			cell1 = row.createCell(1);
			if (record.getR30_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR30_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR30_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR30_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR30_VOLUME() != null) {
			    cell3.setCellValue(record.getR30_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(30);
			if (row == null) {
			    row = sheet.createRow(30);
			}

			cell1 = row.createCell(1);
			if (record.getR31_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR31_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR31_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR31_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR31_VOLUME() != null) {
			    cell3.setCellValue(record.getR31_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(31);
			if (row == null) {
			    row = sheet.createRow(31);
			}

			cell1 = row.createCell(1);
			if (record.getR32_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR32_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR32_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR32_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR32_VOLUME() != null) {
			    cell3.setCellValue(record.getR32_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(32);
			if (row == null) {
			    row = sheet.createRow(32);
			}

			cell1 = row.createCell(1);
			if (record.getR33_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR33_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR33_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR33_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR33_VOLUME() != null) {
			    cell3.setCellValue(record.getR33_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(33);
			if (row == null) {
			    row = sheet.createRow(33);
			}

			cell1 = row.createCell(1);
			if (record.getR34_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR34_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR34_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR34_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR34_VOLUME() != null) {
			    cell3.setCellValue(record.getR34_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(34);
			if (row == null) {
			    row = sheet.createRow(34);
			}

			cell1 = row.createCell(1);
			if (record.getR35_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR35_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR35_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR35_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR35_VOLUME() != null) {
			    cell3.setCellValue(record.getR35_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(35);
			if (row == null) {
			    row = sheet.createRow(35);
			}

			cell1 = row.createCell(1);
			if (record.getR36_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR36_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR36_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR36_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR36_VOLUME() != null) {
			    cell3.setCellValue(record.getR36_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(36);
			if (row == null) {
			    row = sheet.createRow(36);
			}

			cell1 = row.createCell(1);
			if (record.getR37_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR37_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR37_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR37_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR37_VOLUME() != null) {
			    cell3.setCellValue(record.getR37_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(37);
			if (row == null) {
			    row = sheet.createRow(37);
			}

			cell1 = row.createCell(1);
			if (record.getR38_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR38_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR38_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR38_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR38_VOLUME() != null) {
			    cell3.setCellValue(record.getR38_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(38);
			if (row == null) {
			    row = sheet.createRow(38);
			}

			cell1 = row.createCell(1);
			if (record.getR39_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR39_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR39_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR39_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR39_VOLUME() != null) {
			    cell3.setCellValue(record.getR39_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(39);
			if (row == null) {
			    row = sheet.createRow(39);
			}

			cell1 = row.createCell(1);
			if (record.getR40_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR40_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR40_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR40_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR40_VOLUME() != null) {
			    cell3.setCellValue(record.getR40_VOLUME().doubleValue());
			    cell3.setCellStyle(numberStyle);
			} else {
			    cell3.setCellValue("");
			    cell3.setCellStyle(textStyle);
			}



			row = sheet.getRow(40);
			if (row == null) {
			    row = sheet.createRow(40);
			}

			cell1 = row.createCell(1);
			if (record.getR41_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR41_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR41_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR41_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			cell3 = row.createCell(3);
			if (record.getR41_VOLUME() != null) {
			    cell3.setCellValue(record.getR41_VOLUME().doubleValue());
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



public void updateDetailFromForm(Date reportDate, Map<String, String> params) {

	System.out.println("came to service for update ");

	for (Map.Entry<String, String> entry : params.entrySet()) {

		String key = entry.getKey();
		String value = entry.getValue();

		// ✅ Allow only valid keys for required columns
		if (!key.matches("R\\d+_C\\d+_(" + "NOMINAL_INTEREST_RATE|" + "AVG_EFFECTIVE_RATE|" + "VOLUME|" 	
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

		List<M_INT_RATES_Detail_Entity> rows = M_INT_RATES_Detail_Repo
				.findByReportDateAndReportLableAndReportAddlCriteria1(reportDate, reportLabel, addlCriteria);

		for (M_INT_RATES_Detail_Entity row : rows) {

			if ("NOMINAL_INTEREST_RATE".equals(column)) {
				row.setNOMINAL_INTEREST_RATE(amount);

			} else if ("AVG_EFFECTIVE_RATE".equals(column)) {
				row.setAVG_EFFECTIVE_RATE(amount);

			} else if ("VOLUME_AMT".equals(column)) {
				row.setVOLUME_AMT(amount);

			} 
		}

		M_INT_RATES_Detail_Repo.saveAll(rows);
	}

	// ✅ CALL ORACLE PROCEDURE AFTER ALL UPDATES
	callSummaryProcedure(reportDate);
}

private void callSummaryProcedure(Date reportDate) {

	String sql = "{ call BRRS_M_INT_RATES_SUMMARY_PROCEDURE(?) }";

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




public byte[] getM_INT_RATESDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
		String type, String version) {

	try {
		logger.info("Generating Excel for BDISB1 Details...");
		System.out.println("came to Detail download service");

		// ================= ARCHIVAL HANDLING =================
		if ("ARCHIVAL".equals(type) && version != null) {
			return getM_INT_RATESDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type, version);
		}

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
		String[] headers = { "NOMINAL_INTEREST_RATE", "AVG_EFFECTIVE_RATE", "VOLUME", 
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
		List<M_INT_RATES_Detail_Entity> reportData = M_INT_RATES_Detail_Repo.getdatabydateList(parsedToDate);

		// ================= DATA ROWS =================
		int rowIndex = 1;

		if (reportData != null && !reportData.isEmpty()) {
			for (M_INT_RATES_Detail_Entity item : reportData) {

				XSSFRow row = sheet.createRow(rowIndex++);

				Cell c0 = row.createCell(0);
				c0.setCellValue(item.getNOMINAL_INTEREST_RATE() != null ? item.getNOMINAL_INTEREST_RATE().doubleValue() : 0);
				c0.setCellStyle(amountStyle);

				
				Cell c1 = row.createCell(1);
				c1.setCellValue(item.getAVG_EFFECTIVE_RATE() != null ? item.getAVG_EFFECTIVE_RATE().doubleValue() : 0);
				c1.setCellStyle(amountStyle);

				Cell c2 = row.createCell(2);
				c2.setCellValue(item.getVOLUME_AMT() != null ? item.getVOLUME_AMT().doubleValue() : 0);
				c2.setCellStyle(amountStyle);

			

			
				// Column 5 - REPORT LABEL
				 c2 = row.createCell(3);
				c2.setCellValue(item.getReportLable());
				c2.setCellStyle(textStyle);

				// Column 6 - REPORT ADDL CRITERIA 1
				Cell c3 = row.createCell(4);
				c3.setCellValue(item.getReportAddlCriteria1());
				c3.setCellStyle(textStyle);

				// Column 7 - REPORT DATE
				Cell c4 = row.createCell(5);
				c4.setCellValue(item.getReportDate() != null
						? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
						: "");
				c4.setCellStyle(textStyle);
			}
		} else {
			logger.info("No data found for M_INT_RATES — only header written.");
		}

		// ================= WRITE FILE =================
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		workbook.close();

		logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);

		return bos.toByteArray();

	} catch (Exception e) {
		logger.error("Error generating M_INT_RATES Excel", e);
		return new byte[0];
	}
}

public byte[] getM_INT_RATESDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
		String dtltype, String type, String version) {

	try {
		logger.info("Generating Excel for BRRS_M_INT_RATES ARCHIVAL Details...");
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
		String[] headers = { "NOMINAL_INTEREST_RATE", "AVG_EFFECTIVE_RATE", "VOLUME", 
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
		List<M_INT_RATES_Archival_Detail_Entity> reportData = M_INT_RATES_Archival_Detail_Repo.getdatabydateList(parsedToDate,
				version);

		// ================= DATA ROWS =================
		int rowIndex = 1;

		if (reportData != null && !reportData.isEmpty()) {
			for (M_INT_RATES_Archival_Detail_Entity item : reportData) {

				XSSFRow row = sheet.createRow(rowIndex++);

				Cell c0 = row.createCell(0);
				c0.setCellValue(item.getNOMINAL_INTEREST_RATE() != null ? item.getNOMINAL_INTEREST_RATE().doubleValue() : 0);
				c0.setCellStyle(amountStyle);

				
				Cell c1 = row.createCell(1);
				c1.setCellValue(item.getAVG_EFFECTIVE_RATE() != null ? item.getAVG_EFFECTIVE_RATE().doubleValue() : 0);
				c1.setCellStyle(amountStyle);

				Cell c2 = row.createCell(2);
				c2.setCellValue(item.getVOLUME_AMT() != null ? item.getVOLUME_AMT().doubleValue() : 0);
				c2.setCellStyle(amountStyle);

			

			
				// Column 5 - REPORT LABEL
				 c2 = row.createCell(3);
				c2.setCellValue(item.getReportLable());
				c2.setCellStyle(textStyle);

				// Column 6 - REPORT ADDL CRITERIA 1
				Cell c3 = row.createCell(4);
				c3.setCellValue(item.getReportAddlCriteria1());
				c3.setCellStyle(textStyle);

				// Column 7 - REPORT DATE
				Cell c4 = row.createCell(5);
				c4.setCellValue(item.getReportDate() != null
						? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
						: "");
				c4.setCellStyle(textStyle);
			}
		} else {
			logger.info("No archival data found for M_INT_RATES — only header written.");
		}

		// ================= WRITE FILE =================
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		workbook.close();

		logger.info("ARCHIVAL Excel generation completed with {} row(s).",
				reportData != null ? reportData.size() : 0);

		return bos.toByteArray();

	} catch (Exception e) {
		logger.error("Error generating M_INT_RATES ARCHIVAL Excel", e);
		return new byte[0];
	}
}














}

	
	
	
	
	
	
	

	


					



	
	
	
	
	
