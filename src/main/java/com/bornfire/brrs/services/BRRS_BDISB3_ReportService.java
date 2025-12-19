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
import java.util.Optional;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BDISB3_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BDISB3_Detail_Entity;
import com.bornfire.brrs.entities.BDISB3_Detail_Entity_Archival;
import com.bornfire.brrs.entities.BDISB3_Detail_Repo;
import com.bornfire.brrs.entities.BDISB3_Detail_Repo_Archival;
import com.bornfire.brrs.entities.BDISB3_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_BDISB3_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_BDISB3_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_CA2_Archival_Detail_Repo;
import com.bornfire.brrs.entities.M_CA2_Archival_Detail_Entity;

@Component
@Service

public class BRRS_BDISB3_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_BDISB3_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_BDISB3_Summary_Repo BDISB3_Summary_Repo;

	@Autowired
    private BDISB3_Detail_Repo repo;
	
	@Autowired
	BRRS_BDISB3_Archival_Summary_Repo BDISB3_Archival_Summary_Repo;

	@Autowired
	BDISB3_Detail_Repo_Archival BDISB3_Detail_Repo_Archival;

	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_BDISB3View(String reportId, String fromdate, String todate, 
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
            List<BDISB3_Archival_Summary_Entity> T1Master = 
                BDISB3_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 2: RESUB ----------
        else if ("RESUB".equalsIgnoreCase(type) && version != null) {
            List<BDISB3_Archival_Summary_Entity> T1Master =
            		BDISB3_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 3: NORMAL ----------
        else {
            List<BDISB3_Summary_Entity> T1Master = 
                BDISB3_Summary_Repo.getdatabydateListWithVersion(todate);
            System.out.println("T1Master Size "+T1Master.size());
            mv.addObject("reportsummary", T1Master);
        }

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/BDISB3");
		mv.addObject("displaymode", "summary");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}
	
	
	
	
	
	public ModelAndView getBDISB3currentDtl(String reportId, String fromdate, String todate, String currency,
			  String dtltype, Pageable pageable, String Filter, String type, String version) {

	int pageSize = pageable != null ? pageable.getPageSize() : 10;
	int currentPage = pageable != null ? pageable.getPageNumber() : 0;
	int totalPages = 0;

	ModelAndView mv = new ModelAndView();
	//Session hs = sessionFactory.getCurrentSession();

	try {
		Date parsedDate = null;
		if (todate != null && !todate.isEmpty()) {
			parsedDate = dateformat.parse(todate);
		}

		String rowId = null;
		String columnId = null;

		// ✅ Split filter string into rowId & columnId
		if (Filter != null && Filter.contains(",")) {
			String[] parts = Filter.split(",");
			if (parts.length >= 2) {
				rowId = parts[0];
				columnId = parts[1];
			}
		}
		System.out.println(type);
		if ("ARCHIVAL".equals(type) && version != null) {
			System.out.println(type);
			// 🔹 Archival branch
			List<BDISB3_Detail_Entity_Archival> T1Dt1;
			if (rowId != null && columnId != null) {
				T1Dt1 = BDISB3_Detail_Repo_Archival.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
			} else {
				T1Dt1 = BDISB3_Detail_Repo_Archival.getdatabydateList(parsedDate, version);
			}

			mv.addObject("reportdetails", T1Dt1);
			mv.addObject("reportmaster12", T1Dt1);
			System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

		} else {
			System.out.println("row id is: "+rowId +" column id is : "+columnId+" date parsed is : "+parsedDate);
			// 🔹 Current branch
			List<BDISB3_Detail_Entity> T1Dt1;
			if (rowId != null && columnId != null) {
				T1Dt1 = repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
			} else {
				T1Dt1 = repo.getdatabydateList(parsedDate, currentPage, pageSize);
				totalPages = repo.getdatacount(parsedDate);
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
	mv.setViewName("BRRS/BDISB3");
	mv.addObject("displaymode", "Details");
	mv.addObject("currentPage", currentPage);
	System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
	mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
	mv.addObject("reportsflag", "reportsflag");
	mv.addObject("menu", reportId);

	return mv;
}

	public void updateDetailFromForm(Date reportDate, Map<String, String> params) {

	    System.out.println("came to service for update ");

	    for (Map.Entry<String, String> entry : params.entrySet()) {

	        String key = entry.getKey();
	        String value = entry.getValue();

	        if (!key.matches("R\\d+_C\\d+_(AGGREGATE_BALANCE|COMPENSATABLE_AMOUNT)")) {
	            continue;
	        }

	        String[] parts = key.split("_");
	        String reportLabel = parts[0];
	        String addlCriteria = parts[1];
	        String column = String.join("_",
	                Arrays.copyOfRange(parts, 2, parts.length));

	        BigDecimal amount = new BigDecimal(value);

	        List<BDISB3_Detail_Entity> rows =
	            repo.findByReportDateAndReportLableAndReportAddlCriteria1(
	                reportDate, reportLabel, addlCriteria
	            );

	        for (BDISB3_Detail_Entity row : rows) {
	            if ("AGGREGATE_BALANCE".equals(column)) {
	                row.setAGGREGATE_BALANCE(amount);
	            } else if ("COMPENSATABLE_AMOUNT".equals(column)) {
	                row.setCOMPENSATABLE_AMOUNT(amount);
	            }
	        }

	        repo.saveAll(rows);
	    }

	    // ✅ CALL ORACLE PROCEDURE AFTER ALL UPDATES
	    callSummaryProcedure(reportDate);
	}


	private void callSummaryProcedure(Date reportDate) {

	    String sql = "{ call BRRS_BDISB3_SUMMARY_PROCEDURE(?) }";

	    jdbcTemplate.update(connection -> {
	        CallableStatement cs = connection.prepareCall(sql);

	        // Force exact format expected by procedure
	        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	        sdf.setLenient(false);

	        String formattedDate = sdf.format(reportDate);

	        cs.setString(1, formattedDate);  // 🔥 THIS IS MANDATORY
	        return cs;
	    });

	    System.out.println("✅ Summary procedure executed for date: " +
	            new SimpleDateFormat("dd-MM-yyyy").format(reportDate));
	}


    
	public void updateReport(BDISB3_Summary_Entity updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReportDate());

	    BDISB3_Summary_Entity existing =
	            BDISB3_Summary_Repo.findTopByReportDateOrderByReportVersionDesc(updatedEntity.getReportDate())
	                    .orElseThrow(() ->
	                            new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

	    try {
	        // 1️⃣ Update R14 to R36 (based on your loop)
	        for (int i = 5; i <= 10; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = {
	            		"AGGREGATE_BALANCE",
	        	        "COMPENSATABLE_AMOUNT"
	            };

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = BDISB3_Summary_Entity.class.getMethod(getterName);
	                    Method setter = BDISB3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);

	                    // ✔ Prevent null overwriting if updatedEntity does not send the field
	                    if (newValue != null) {
	                        setter.invoke(existing, newValue);
	                    }

	                } catch (NoSuchMethodException e) {
	                    System.out.println("Skipping missing field: " + setterName);
	                }
	            }
	        }

	        // 2️⃣ Handle R37 TOTAL
	        String prefix = "R10_";
	        String[] totalFields = {
	        		"AGGREGATE_BALANCE",
        	        "COMPENSATABLE_AMOUNT"
	        };

	        for (String field : totalFields) {

	            String getterName = "get" + prefix + field;
	            String setterName = "set" + prefix + field;

	            try {
	                Method getter = BDISB3_Summary_Entity.class.getMethod(getterName);
	                Method setter = BDISB3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                Object newValue = getter.invoke(updatedEntity);

	                if (newValue != null) {
	                    setter.invoke(existing, newValue);
	                }

	            } catch (NoSuchMethodException e) {
	                System.out.println("Skipping missing total field: " + setterName);
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    System.out.println("Saving updated entity");
	    BDISB3_Summary_Repo.save(existing);
	}
	
	
	public byte[] getBDISB3Excel(String filename, String reportId, String fromdate, String todate, String currency,
			 String dtltype, String type, String version) throws Exception {
logger.info("Service: Starting Excel generation process in memory.");
logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

// Convert string to Date
Date reportDate = dateformat.parse(todate);

// ARCHIVAL check
if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
logger.info("Service: Generating ARCHIVAL report for version {}", version);
return getExcelBDISB3ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
}
// RESUB check
else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
logger.info("Service: Generating RESUB report for version {}", version);


List<BDISB3_Archival_Summary_Entity> T1Master =
BDISB3_Archival_Summary_Repo.getdatabydateListarchival(reportDate, version);

// Generate Excel for RESUB
return BRRS_BDISB3ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
}




// Default (LIVE) case
List<BDISB3_Summary_Entity> dataList1 = BDISB3_Summary_Repo.getdatabydateList(reportDate);

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

int startRow = 4;

if (!dataList1.isEmpty()) {
for (int i = 0; i < dataList1.size(); i++) {

BDISB3_Summary_Entity record = dataList1.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}


//Cell0 - R5_RECORD_NUMBER
Cell cell1 = row.createCell(1);
if (record.getR5_AGGREGATE_BALANCE() != null) {
 cell1.setCellValue(record.getR5_AGGREGATE_BALANCE().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

Cell cell2 = row.createCell(2);
if (record.getR5_COMPENSATABLE_AMOUNT() != null) {
 cell2.setCellValue(record.getR5_COMPENSATABLE_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

row = sheet.getRow(5);
//------------------------- R6 -------------------------
cell1 = row.createCell(1);
if (record.getR6_AGGREGATE_BALANCE() != null) {
 cell1.setCellValue(record.getR6_AGGREGATE_BALANCE().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

cell2 = row.createCell(2);
if (record.getR6_COMPENSATABLE_AMOUNT() != null) {
 cell2.setCellValue(record.getR6_COMPENSATABLE_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

row = sheet.getRow(6);
//------------------------- R7 -------------------------
cell1 = row.createCell(1);
if (record.getR7_AGGREGATE_BALANCE() != null) {
 cell1.setCellValue(record.getR7_AGGREGATE_BALANCE().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

cell2 = row.createCell(2);
if (record.getR7_COMPENSATABLE_AMOUNT() != null) {
 cell2.setCellValue(record.getR7_COMPENSATABLE_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

row = sheet.getRow(7);
//------------------------- R8 -------------------------
cell1 = row.createCell(1);
if (record.getR8_AGGREGATE_BALANCE() != null) {
 cell1.setCellValue(record.getR8_AGGREGATE_BALANCE().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

cell2 = row.createCell(2);
if (record.getR8_COMPENSATABLE_AMOUNT() != null) {
 cell2.setCellValue(record.getR8_COMPENSATABLE_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

row = sheet.getRow(8);
//------------------------- R9 -------------------------
cell1 = row.createCell(1);
if (record.getR9_AGGREGATE_BALANCE() != null) {
 cell1.setCellValue(record.getR9_AGGREGATE_BALANCE().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

cell2 = row.createCell(2);
if (record.getR9_COMPENSATABLE_AMOUNT() != null) {
 cell2.setCellValue(record.getR9_COMPENSATABLE_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

row = sheet.getRow(9);
//------------------------- R10 -------------------------
cell1 = row.createCell(1);
if (record.getR10_AGGREGATE_BALANCE() != null) {
 cell1.setCellValue(record.getR10_AGGREGATE_BALANCE().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

cell2 = row.createCell(2);
if (record.getR10_COMPENSATABLE_AMOUNT() != null) {
 cell2.setCellValue(record.getR10_COMPENSATABLE_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
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
	
	public byte[] getExcelBDISB3ARCHIVAL(String filename, String reportId, String fromdate,
			String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if ("ARCHIVAL".equals(type) && version != null) {
		}
			List<BDISB3_Archival_Summary_Entity> dataList1 = BDISB3_Archival_Summary_Repo
					.getdatabydateListarchival(dateformat.parse(todate), version);
		

			
		
		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for BDISB3 report. Returning empty result.");
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

			BDISB3_Archival_Summary_Entity record1 = dataList1.get(i);
			System.out.println("rownumber=" + startRow + i);
			Row row = sheet.getRow(startRow + i);
			if (row == null) {
			row = sheet.createRow(startRow + i);
			}
			
			//Cell0 - R5_record1_NUMBER
			Cell cell1 = row.createCell(1);
			if (record1.getR5_AGGREGATE_BALANCE() != null) {
			 cell1.setCellValue(record1.getR5_AGGREGATE_BALANCE().doubleValue());
			 cell1.setCellStyle(numberStyle);
			} else {
			 cell1.setCellValue("");
			 cell1.setCellStyle(textStyle);
			}

			Cell cell2 = row.createCell(2);
			if (record1.getR5_COMPENSATABLE_AMOUNT() != null) {
			 cell2.setCellValue(record1.getR5_COMPENSATABLE_AMOUNT().doubleValue());
			 cell2.setCellStyle(numberStyle);
			} else {
			 cell2.setCellValue("");
			 cell2.setCellStyle(textStyle);
			}

			row = sheet.getRow(5);
			//------------------------- R6 -------------------------
			cell1 = row.createCell(1);
			if (record1.getR6_AGGREGATE_BALANCE() != null) {
			 cell1.setCellValue(record1.getR6_AGGREGATE_BALANCE().doubleValue());
			 cell1.setCellStyle(numberStyle);
			} else {
			 cell1.setCellValue("");
			 cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record1.getR6_COMPENSATABLE_AMOUNT() != null) {
			 cell2.setCellValue(record1.getR6_COMPENSATABLE_AMOUNT().doubleValue());
			 cell2.setCellStyle(numberStyle);
			} else {
			 cell2.setCellValue("");
			 cell2.setCellStyle(textStyle);
			}

			row = sheet.getRow(6);
			//------------------------- R7 -------------------------
			cell1 = row.createCell(1);
			if (record1.getR7_AGGREGATE_BALANCE() != null) {
			 cell1.setCellValue(record1.getR7_AGGREGATE_BALANCE().doubleValue());
			 cell1.setCellStyle(numberStyle);
			} else {
			 cell1.setCellValue("");
			 cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record1.getR7_COMPENSATABLE_AMOUNT() != null) {
			 cell2.setCellValue(record1.getR7_COMPENSATABLE_AMOUNT().doubleValue());
			 cell2.setCellStyle(numberStyle);
			} else {
			 cell2.setCellValue("");
			 cell2.setCellStyle(textStyle);
			}

			row = sheet.getRow(7);
			//------------------------- R8 -------------------------
			cell1 = row.createCell(1);
			if (record1.getR8_AGGREGATE_BALANCE() != null) {
			 cell1.setCellValue(record1.getR8_AGGREGATE_BALANCE().doubleValue());
			 cell1.setCellStyle(numberStyle);
			} else {
			 cell1.setCellValue("");
			 cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record1.getR8_COMPENSATABLE_AMOUNT() != null) {
			 cell2.setCellValue(record1.getR8_COMPENSATABLE_AMOUNT().doubleValue());
			 cell2.setCellStyle(numberStyle);
			} else {
			 cell2.setCellValue("");
			 cell2.setCellStyle(textStyle);
			}

			row = sheet.getRow(8);
			//------------------------- R9 -------------------------
			cell1 = row.createCell(1);
			if (record1.getR9_AGGREGATE_BALANCE() != null) {
			 cell1.setCellValue(record1.getR9_AGGREGATE_BALANCE().doubleValue());
			 cell1.setCellStyle(numberStyle);
			} else {
			 cell1.setCellValue("");
			 cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record1.getR9_COMPENSATABLE_AMOUNT() != null) {
			 cell2.setCellValue(record1.getR9_COMPENSATABLE_AMOUNT().doubleValue());
			 cell2.setCellStyle(numberStyle);
			} else {
			 cell2.setCellValue("");
			 cell2.setCellStyle(textStyle);
			}

			row = sheet.getRow(9);
			//------------------------- R10 -------------------------
			cell1 = row.createCell(1);
			if (record1.getR10_AGGREGATE_BALANCE() != null) {
			 cell1.setCellValue(record1.getR10_AGGREGATE_BALANCE().doubleValue());
			 cell1.setCellStyle(numberStyle);
			} else {
			 cell1.setCellValue("");
			 cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record1.getR10_COMPENSATABLE_AMOUNT() != null) {
			 cell2.setCellValue(record1.getR10_COMPENSATABLE_AMOUNT().doubleValue());
			 cell2.setCellStyle(numberStyle);
			} else {
			 cell2.setCellValue("");
			 cell2.setCellStyle(textStyle);
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

//////////////////////////////////////////RESUBMISSION///////////////////////////////////////////////////////////////////	
/// Report Date | Report Version | Domain
/// RESUB VIEW
public List<Object[]> getBDISB3Resub() {
List<Object[]> resubList = new ArrayList<>();
try {
List<BDISB3_Archival_Summary_Entity> latestArchivalList = 
BDISB3_Archival_Summary_Repo.getdatabydateListWithVersionAll();

if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
for (BDISB3_Archival_Summary_Entity entity : latestArchivalList) {
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
System.err.println("Error fetching BDISB3 Resub data: " + e.getMessage());
e.printStackTrace();
}
return resubList;
}


//Archival View
public List<Object[]> getBDISB3Archival() {
List<Object[]> archivalList = new ArrayList<>();

try {
List<BDISB3_Archival_Summary_Entity> repoData = BDISB3_Archival_Summary_Repo
.getdatabydateListWithVersionAll();

if (repoData != null && !repoData.isEmpty()) {
for (BDISB3_Archival_Summary_Entity entity : repoData) {
Object[] row = new Object[] {
entity.getReportDate(), 
entity.getReportVersion() 
};
archivalList.add(row);
}

System.out.println("Fetched " + archivalList.size() + " archival records");
BDISB3_Archival_Summary_Entity first = repoData.get(0);
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
public void updateReportReSub(BDISB3_Summary_Entity updatedEntity) {
System.out.println("Came to Resub Service");
System.out.println("Report Date: " + updatedEntity.getReportDate());

Date reportDate = updatedEntity.getReportDate();
int newVersion = 1;

try {
// Fetch the latest archival version for this report date
Optional<BDISB3_Archival_Summary_Entity> latestArchivalOpt = BDISB3_Archival_Summary_Repo
.getLatestArchivalVersionByDate(reportDate);

// Determine next version number
if (latestArchivalOpt.isPresent()) {
BDISB3_Archival_Summary_Entity latestArchival = latestArchivalOpt.get();
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
boolean exists = BDISB3_Archival_Summary_Repo
.findByReportDateAndReportVersion(reportDate, String.valueOf(newVersion))
.isPresent();

if (exists) {
throw new RuntimeException("Version " + newVersion + " already exists for report date " + reportDate);
}

// Copy summary entity to archival entity
BDISB3_Archival_Summary_Entity archivalEntity = new BDISB3_Archival_Summary_Entity();
org.springframework.beans.BeanUtils.copyProperties(updatedEntity, archivalEntity);

archivalEntity.setReportDate(reportDate);
archivalEntity.setReportVersion(String.valueOf(newVersion));
archivalEntity.setReportResubDate(new Date());

System.out.println("Saving new archival version: " + newVersion);

// Save new version to repository
BDISB3_Archival_Summary_Repo.save(archivalEntity);

System.out.println(" Saved archival version successfully: " + newVersion);

} catch (Exception e) {
e.printStackTrace();
throw new RuntimeException("Error while creating archival resubmission record", e);
}
}

/// Downloaded for Archival & Resub
public byte[] BRRS_BDISB3ResubExcel(String filename, String reportId, String fromdate,
String todate, String currency, String dtltype,
String type, String version) throws Exception {

logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

if (type.equals("RESUB") & version != null) {

}

List<BDISB3_Archival_Summary_Entity> dataList1 =
BDISB3_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

if (dataList1.isEmpty()) {
logger.warn("Service: No data found for BDISB3 report. Returning empty result.");
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

BDISB3_Archival_Summary_Entity record1 = dataList1.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}

//Cell0 - R5_record1_NUMBER
Cell cell1 = row.createCell(1);
if (record1.getR5_AGGREGATE_BALANCE() != null) {
 cell1.setCellValue(record1.getR5_AGGREGATE_BALANCE().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

Cell cell2 = row.createCell(2);
if (record1.getR5_COMPENSATABLE_AMOUNT() != null) {
 cell2.setCellValue(record1.getR5_COMPENSATABLE_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

row = sheet.getRow(5);
//------------------------- R6 -------------------------
cell1 = row.createCell(1);
if (record1.getR6_AGGREGATE_BALANCE() != null) {
 cell1.setCellValue(record1.getR6_AGGREGATE_BALANCE().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

cell2 = row.createCell(2);
if (record1.getR6_COMPENSATABLE_AMOUNT() != null) {
 cell2.setCellValue(record1.getR6_COMPENSATABLE_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

row = sheet.getRow(6);
//------------------------- R7 -------------------------
cell1 = row.createCell(1);
if (record1.getR7_AGGREGATE_BALANCE() != null) {
 cell1.setCellValue(record1.getR7_AGGREGATE_BALANCE().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

cell2 = row.createCell(2);
if (record1.getR7_COMPENSATABLE_AMOUNT() != null) {
 cell2.setCellValue(record1.getR7_COMPENSATABLE_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

row = sheet.getRow(7);
//------------------------- R8 -------------------------
cell1 = row.createCell(1);
if (record1.getR8_AGGREGATE_BALANCE() != null) {
 cell1.setCellValue(record1.getR8_AGGREGATE_BALANCE().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

cell2 = row.createCell(2);
if (record1.getR8_COMPENSATABLE_AMOUNT() != null) {
 cell2.setCellValue(record1.getR8_COMPENSATABLE_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

row = sheet.getRow(8);
//------------------------- R9 -------------------------
cell1 = row.createCell(1);
if (record1.getR9_AGGREGATE_BALANCE() != null) {
 cell1.setCellValue(record1.getR9_AGGREGATE_BALANCE().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

cell2 = row.createCell(2);
if (record1.getR9_COMPENSATABLE_AMOUNT() != null) {
 cell2.setCellValue(record1.getR9_COMPENSATABLE_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

row = sheet.getRow(9);
//------------------------- R10 -------------------------
cell1 = row.createCell(1);
if (record1.getR10_AGGREGATE_BALANCE() != null) {
 cell1.setCellValue(record1.getR10_AGGREGATE_BALANCE().doubleValue());
 cell1.setCellStyle(numberStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

cell2 = row.createCell(2);
if (record1.getR10_COMPENSATABLE_AMOUNT() != null) {
 cell2.setCellValue(record1.getR10_COMPENSATABLE_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
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
