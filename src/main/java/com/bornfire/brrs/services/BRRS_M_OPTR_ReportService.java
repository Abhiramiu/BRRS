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



import com.bornfire.brrs.entities.M_OPTR_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_OPTR_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_OPTR_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_OPTR_Archival_Summary_Repo;



import java.math.BigDecimal;

@Component
@Service
public class BRRS_M_OPTR_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_OPTR_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;




	@Autowired
	BRRS_M_OPTR_Summary_Repo BRRS_M_OPTR_Summary_Repo;

	@Autowired
	BRRS_M_OPTR_Archival_Summary_Repo BRRS_M_OPTR_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_OPTRView(String reportId, String fromdate, String todate, String currency,
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
			List<M_OPTR_Archival_Summary_Entity> T1Master = new ArrayList<M_OPTR_Archival_Summary_Entity>();
			System.out.println(version);
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_OPTR_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} else {
			List<M_OPTR_Summary_Entity> T1Master = new ArrayList<M_OPTR_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_OPTR_Summary_Repo.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/M_OPTR");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}



	public void updateReport1(M_OPTR_Summary_Entity updatedEntity) {
	    System.out.println("Came to services1");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    M_OPTR_Summary_Entity existing = BRRS_M_OPTR_Summary_Repo.findById(updatedEntity.getReport_date())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

	    try {
	        // 1. Loop from R15 to R50 and copy fields
	        for (int i = 10; i <= 14; i++) {
		if (i == 12) {
        continue;
    }
	            String prefix = "R" + i + "_";

	            String[] fields = {"INTEREST_RATES", "EQUITIES", "FOREIGN_EXC_GOLD", "COMMODITIES"};

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_OPTR_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_OPTR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }
	        
// Loop through rows for formula fields 
int[] targetRows = {12};

for (int i : targetRows) {
    String prefix = "R" + i + "_";

    String[] fields = {"INTEREST_RATES", "EQUITIES", "FOREIGN_EXC_GOLD", "COMMODITIES"};

    for (String field : fields) {
        String getterName = "get" + prefix + field;
        String setterName = "set" + prefix + field;

        try {
            Method getter = M_OPTR_Summary_Entity.class.getMethod(getterName);
            Method setter = M_OPTR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

            Object newValue = getter.invoke(updatedEntity);
            setter.invoke(existing, newValue);

        } catch (NoSuchMethodException e) {
            // Skip missing fields
            continue;
        }
    }
}
//Loop through rows for formula fields 
int[] target = {10,11,12,13,14};

for (int i : target) {
 String prefix = "R" + i + "_";

 String[] fields = {"TOTAL"};

 for (String field : fields) {
     String getterName = "get" + prefix + field;
     String setterName = "set" + prefix + field;

     try {
         Method getter = M_OPTR_Summary_Entity.class.getMethod(getterName);
         Method setter = M_OPTR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

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
	    BRRS_M_OPTR_Summary_Repo.save(existing);
	}
	

	
	
//	public ModelAndView getM_OPTRcurrentDtl(String reportId, String fromdate, String todate, String currency,
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
//				List<BRRS_M_OPTR_Archival_Detail_Entity> T1Dt1;
//				if (rowId != null && columnId != null) {
//					T1Dt1 = BRRS_M_OPTR_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
//				} else {
//					T1Dt1 = BRRS_M_OPTR_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
//				}
//
//				mv.addObject("reportdetails", T1Dt1);
//				mv.addObject("reportmaster12", T1Dt1);
//				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
//
//			} else {
//				// 🔹 Current branch
//				List<BRRS_M_OPTR_Detail_Entity> T1Dt1;
//				if (rowId != null && columnId != null) {
//					T1Dt1 = BRRS_M_OPTR_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
//				} else {
//					T1Dt1 = BRRS_M_OPTR_Detail_Repo.getdatabydateList(parsedDate, currentPage, pageSize);
//					totalPages = BRRS_M_OPTR_Detail_Repo.getdatacount(parsedDate);
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
//		mv.setViewName("BRRS/M_OPTR");
//		mv.addObject("displaymode", "Details");
//		mv.addObject("currentPage", currentPage);
//		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
//		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
//		mv.addObject("reportsflag", "reportsflag");
//		mv.addObject("menu", reportId);
//
//		return mv;
//	}


	public byte[] getM_OPTRExcel(String filename, String reportId, String fromdate, String todate, String currency,
			 String dtltype, String type, String version) throws Exception {
logger.info("Service: Starting Excel generation process in memory.");

 //ARCHIVAL check
if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
logger.info("Service: Generating ARCHIVAL report for version {}", version);
return getExcelM_OPTRARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
}

// Fetch data
List<M_OPTR_Summary_Entity> dataList = BRRS_M_OPTR_Summary_Repo.getdatabydateList(dateformat.parse(todate));

if (dataList.isEmpty()) {
logger.warn("Service: No data found for M_OPTR report. Returning empty result.");
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
int startRow = 9;

if (!dataList.isEmpty()) {
for (int i = 0; i < dataList.size(); i++) {
M_OPTR_Summary_Entity record = dataList.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);	

if (row == null) {
row = sheet.createRow(startRow + i);
}



Cell cell2 = row.createCell(4);
if (record.getR10_INTEREST_RATES() != null) {
cell2.setCellValue(record.getR10_INTEREST_RATES().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

Cell cell3 = row.createCell(5);
if (record.getR10_EQUITIES() != null) {
cell3.setCellValue(record.getR10_EQUITIES().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

Cell cell4 = row.createCell(6);
if (record.getR10_FOREIGN_EXC_GOLD() != null) {
cell4.setCellValue(record.getR10_FOREIGN_EXC_GOLD().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

Cell cell5 = row.createCell(7);
if (record.getR10_COMMODITIES() != null) {
cell5.setCellValue(record.getR10_COMMODITIES().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

row = sheet.getRow(10);

 cell2 = row.createCell(4);
if (record.getR11_INTEREST_RATES() != null) {
cell2.setCellValue(record.getR11_INTEREST_RATES().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

 cell3 = row.createCell(5);
if (record.getR11_EQUITIES() != null) {
cell3.setCellValue(record.getR11_EQUITIES().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

 cell4 = row.createCell(6);
if (record.getR11_FOREIGN_EXC_GOLD() != null) {
cell4.setCellValue(record.getR11_FOREIGN_EXC_GOLD().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

 cell5 = row.createCell(7);
if (record.getR11_COMMODITIES() != null) {
cell5.setCellValue(record.getR11_COMMODITIES().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}
				
row = sheet.getRow(12);
 cell2 = row.createCell(4);
if (record.getR13_INTEREST_RATES() != null) {
cell2.setCellValue(record.getR13_INTEREST_RATES().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

 cell3 = row.createCell(5);
if (record.getR13_EQUITIES() != null) {
cell3.setCellValue(record.getR13_EQUITIES().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

 cell4 = row.createCell(6);
if (record.getR13_FOREIGN_EXC_GOLD() != null) {
cell4.setCellValue(record.getR13_FOREIGN_EXC_GOLD().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(7);
if (record.getR13_COMMODITIES() != null) {
cell5.setCellValue(record.getR13_COMMODITIES().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}


row = sheet.getRow(13);
				cell2 = row.createCell(4);
if (record.getR14_INTEREST_RATES() != null) {
cell2.setCellValue(record.getR14_INTEREST_RATES().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

 cell3 = row.createCell(5);
if (record.getR14_EQUITIES() != null) {
cell3.setCellValue(record.getR14_EQUITIES().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(6);
if (record.getR14_FOREIGN_EXC_GOLD() != null) {
cell4.setCellValue(record.getR14_FOREIGN_EXC_GOLD().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

 cell5 = row.createCell(7);
if (record.getR14_COMMODITIES() != null) {
cell5.setCellValue(record.getR14_COMMODITIES().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
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


//
////	public byte[] BRRS_M_OPTRDetailExcel(String filename, String fromdate, String todate, String currency,
////										   String dtltype, String type, String version) {
////
////		try {
////			logger.info("Generating Excel for BRRS_M_OPTR Details...");
////			System.out.println("came to Detail download service");
////			if (type.equals("ARCHIVAL") & version != null) {
////				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
////						version);
////				return ARCHIVALreport;
////			}
////			XSSFWorkbook workbook = new XSSFWorkbook();
////			XSSFSheet sheet = workbook.createSheet("BRRS_M_OPTRDetails");
////
////			// Common border style
////			BorderStyle border = BorderStyle.THIN;
////			// Header style (left aligned)
////			CellStyle headerStyle = workbook.createCellStyle();
////			Font headerFont = workbook.createFont();
////			headerFont.setBold(true);
////			headerFont.setFontHeightInPoints((short) 10);
////			headerStyle.setFont(headerFont);
////			headerStyle.setAlignment(HorizontalAlignment.LEFT);
////			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
////			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
////			headerStyle.setBorderTop(border);
////			headerStyle.setBorderBottom(border);
////			headerStyle.setBorderLeft(border);
////			headerStyle.setBorderRight(border);
////
////			// Right-aligned header style for ACCT BALANCE
////			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
////			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
////			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);
////
////			// Default data style (left aligned)
////			CellStyle dataStyle = workbook.createCellStyle();
////			dataStyle.setAlignment(HorizontalAlignment.LEFT);
////			dataStyle.setBorderTop(border);
////			dataStyle.setBorderBottom(border);
////			dataStyle.setBorderLeft(border);
////			dataStyle.setBorderRight(border);
////
////			// ACCT BALANCE style (right aligned with 3 decimals)
////			CellStyle balanceStyle = workbook.createCellStyle();
////			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
////			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
////			balanceStyle.setBorderTop(border);
////			balanceStyle.setBorderBottom(border);
////			balanceStyle.setBorderLeft(border);
////			balanceStyle.setBorderRight(border);
////			// Header row
////			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "ROWID", "COLUMNID",
////					"REPORT_DATE" };
////			XSSFRow headerRow = sheet.createRow(0);
////			for (int i = 0; i < headers.length; i++) {
////				Cell cell = headerRow.createCell(i);
////				cell.setCellValue(headers[i]);
////				if (i == 3) { // ACCT BALANCE
////					cell.setCellStyle(rightAlignedHeaderStyle);
////				} else {
////					cell.setCellStyle(headerStyle);
////				}
////				sheet.setColumnWidth(i, 5000);
////			}
////			// Get data
////			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
////			List<BRRS_M_OPTR_Detail_Entity> reportData = BRRS_M_OPTR_Detail_Repo.getdatabydateList(parsedToDate);
////			if (reportData != null && !reportData.isEmpty()) {
////				int rowIndex = 1;
////				for (BRRS_M_OPTR_Detail_Entity item : reportData) {
////					XSSFRow row = sheet.createRow(rowIndex++);
////					row.createCell(0).setCellValue(item.getCUST_ID());
////					row.createCell(1).setCellValue(item.getACCT_NUMBER());
////					row.createCell(2).setCellValue(item.getACCT_NAME());
////					// ACCT BALANCE (right aligned, 3 decimal places)
////					Cell balanceCell = row.createCell(3);
////					if (item.getACCT_BALANCE_IN_PULA() != null) {
////						balanceCell.setCellValue(item.getACCT_BALANCE_IN_PULA().doubleValue());
////					} else {
////						balanceCell.setCellValue(0.000);
////					}
////					balanceCell.setCellStyle(balanceStyle);
////					row.createCell(4).setCellValue(item.getROW_ID());
////					row.createCell(5).setCellValue(item.getCOLUMN_ID());
////					row.createCell(6)
////							.setCellValue(item.getREPORT_DATE() != null
////									? new SimpleDateFormat("dd-MM-yyyy").format(item.getREPORT_DATE())
////									: "");
////					// Apply data style for all other cells
////					for (int j = 0; j < 7; j++) {
////						if (j != 3) {
////							row.getCell(j).setCellStyle(dataStyle);
////						}
////					}
////				}
////			} else {
////				logger.info("No data found for BRRS_M_OPTR — only header will be written.");
////			}
////			// Write to byte[]
////			ByteArrayOutputStream bos = new ByteArrayOutputStream();
////			workbook.write(bos);
////			workbook.close();
////			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
////			return bos.toByteArray();
////		} catch (Exception e) {
////			logger.error("Error generating BRRS_M_OPTR Excel", e);
////			return new byte[0];
////		}
////	}
//
	public List<Object> getM_OPTRArchival() {
		List<Object> M_OPTRArchivallist = new ArrayList<>();
		try {
			M_OPTRArchivallist = BRRS_M_OPTR_Archival_Summary_Repo.getM_OPTRarchival();
			System.out.println("countser" + M_OPTRArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_OPTR Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_OPTRArchivallist;
	}
	

	public byte[] getExcelM_OPTRARCHIVAL(String filename, String reportId, String fromdate, String todate,
										   String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_OPTR_Archival_Summary_Entity> dataList = BRRS_M_OPTR_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_OPTR report. Returning empty result.");
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
			M_OPTR_Archival_Summary_Entity record = dataList.get(i);
			System.out.println("rownumber=" + startRow + i);
			Row row = sheet.getRow(startRow + i);	

			if (row == null) {
			row = sheet.createRow(startRow + i);
			}



			Cell cell2 = row.createCell(4);
			if (record.getR10_INTEREST_RATES() != null) {
			cell2.setCellValue(record.getR10_INTEREST_RATES().doubleValue());
			cell2.setCellStyle(numberStyle);
			} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
			}

			Cell cell3 = row.createCell(5);
			if (record.getR10_EQUITIES() != null) {
			cell3.setCellValue(record.getR10_EQUITIES().doubleValue());
			cell3.setCellStyle(numberStyle);
			} else {
			cell3.setCellValue("");
			cell3.setCellStyle(textStyle);
			}

			Cell cell4 = row.createCell(6);
			if (record.getR10_FOREIGN_EXC_GOLD() != null) {
			cell4.setCellValue(record.getR10_FOREIGN_EXC_GOLD().doubleValue());
			cell4.setCellStyle(numberStyle);
			} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
			}

			Cell cell5 = row.createCell(7);
			if (record.getR10_COMMODITIES() != null) {
			cell5.setCellValue(record.getR10_COMMODITIES().doubleValue());
			cell5.setCellStyle(numberStyle);
			} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
			}

			row = sheet.getRow(10);

			 cell2 = row.createCell(4);
			if (record.getR11_INTEREST_RATES() != null) {
			cell2.setCellValue(record.getR11_INTEREST_RATES().doubleValue());
			cell2.setCellStyle(numberStyle);
			} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
			}

			 cell3 = row.createCell(5);
			if (record.getR11_EQUITIES() != null) {
			cell3.setCellValue(record.getR11_EQUITIES().doubleValue());
			cell3.setCellStyle(numberStyle);
			} else {
			cell3.setCellValue("");
			cell3.setCellStyle(textStyle);
			}

			 cell4 = row.createCell(6);
			if (record.getR11_FOREIGN_EXC_GOLD() != null) {
			cell4.setCellValue(record.getR11_FOREIGN_EXC_GOLD().doubleValue());
			cell4.setCellStyle(numberStyle);
			} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
			}

			 cell5 = row.createCell(7);
			if (record.getR11_COMMODITIES() != null) {
			cell5.setCellValue(record.getR11_COMMODITIES().doubleValue());
			cell5.setCellStyle(numberStyle);
			} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
			}
							
			row = sheet.getRow(12);
			 cell2 = row.createCell(4);
			if (record.getR13_INTEREST_RATES() != null) {
			cell2.setCellValue(record.getR13_INTEREST_RATES().doubleValue());
			cell2.setCellStyle(numberStyle);
			} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
			}

			 cell3 = row.createCell(5);
			if (record.getR13_EQUITIES() != null) {
			cell3.setCellValue(record.getR13_EQUITIES().doubleValue());
			cell3.setCellStyle(numberStyle);
			} else {
			cell3.setCellValue("");
			cell3.setCellStyle(textStyle);
			}

			 cell4 = row.createCell(6);
			if (record.getR13_FOREIGN_EXC_GOLD() != null) {
			cell4.setCellValue(record.getR13_FOREIGN_EXC_GOLD().doubleValue());
			cell4.setCellStyle(numberStyle);
			} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
			}

			cell5 = row.createCell(7);
			if (record.getR13_COMMODITIES() != null) {
			cell5.setCellValue(record.getR13_COMMODITIES().doubleValue());
			cell5.setCellStyle(numberStyle);
			} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
			}


			row = sheet.getRow(13);
							cell2 = row.createCell(4);
			if (record.getR14_INTEREST_RATES() != null) {
			cell2.setCellValue(record.getR14_INTEREST_RATES().doubleValue());
			cell2.setCellStyle(numberStyle);
			} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
			}

			 cell3 = row.createCell(5);
			if (record.getR14_EQUITIES() != null) {
			cell3.setCellValue(record.getR14_EQUITIES().doubleValue());
			cell3.setCellStyle(numberStyle);
			} else {
			cell3.setCellValue("");
			cell3.setCellStyle(textStyle);
			}

			cell4 = row.createCell(6);
			if (record.getR14_FOREIGN_EXC_GOLD() != null) {
			cell4.setCellValue(record.getR14_FOREIGN_EXC_GOLD().doubleValue());
			cell4.setCellStyle(numberStyle);
			} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
			}

			 cell5 = row.createCell(7);
			if (record.getR14_COMMODITIES() != null) {
			cell5.setCellValue(record.getR14_COMMODITIES().doubleValue());
			cell5.setCellStyle(numberStyle);
			} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
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
//
////	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
////										 String dtltype, String type, String version) {
////		try {
////			logger.info("Generating Excel for BRRS_M_OPTR ARCHIVAL Details...");
////			System.out.println("came to Detail download service");
////			if (type.equals("ARCHIVAL") & version != null) {
////
////			}
////			XSSFWorkbook workbook = new XSSFWorkbook();
////			XSSFSheet sheet = workbook.createSheet("M_OPTRDetail");
////
////			// Common border style
////			BorderStyle border = BorderStyle.THIN;
////
////			// Header style (left aligned)
////			CellStyle headerStyle = workbook.createCellStyle();
////			Font headerFont = workbook.createFont();
////			headerFont.setBold(true);
////			headerFont.setFontHeightInPoints((short) 10);
////			headerStyle.setFont(headerFont);
////			headerStyle.setAlignment(HorizontalAlignment.LEFT);
////			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
////			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
////			headerStyle.setBorderTop(border);
////			headerStyle.setBorderBottom(border);
////			headerStyle.setBorderLeft(border);
////			headerStyle.setBorderRight(border);
////
////			// Right-aligned header style for ACCT BALANCE
////			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
////			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
////			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);
////
////			// Default data style (left aligned)
////			CellStyle dataStyle = workbook.createCellStyle();
////			dataStyle.setAlignment(HorizontalAlignment.LEFT);
////			dataStyle.setBorderTop(border);
////			dataStyle.setBorderBottom(border);
////			dataStyle.setBorderLeft(border);
////			dataStyle.setBorderRight(border);
////
////			// ACCT BALANCE style (right aligned with 3 decimals)
////			CellStyle balanceStyle = workbook.createCellStyle();
////			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
////			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
////			balanceStyle.setBorderTop(border);
////			balanceStyle.setBorderBottom(border);
////			balanceStyle.setBorderLeft(border);
////			balanceStyle.setBorderRight(border);
////
////			// Header row
////			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "ROWID", "COLUMNID",
////					"REPORT_DATE" };
////
////			XSSFRow headerRow = sheet.createRow(0);
////			for (int i = 0; i < headers.length; i++) {
////				Cell cell = headerRow.createCell(i);
////				cell.setCellValue(headers[i]);
////
////				if (i == 3) { // ACCT BALANCE
////					cell.setCellStyle(rightAlignedHeaderStyle);
////				} else {
////					cell.setCellStyle(headerStyle);
////				}
////
////				sheet.setColumnWidth(i, 5000);
////			}
////
////			// Get data
////			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
////			List<BRRS_M_OPTR_Archival_Detail_Entity> reportData = BRRS_M_OPTR_Archival_Detail_Repo
////					.getdatabydateList(parsedToDate, version);
////
////			if (reportData != null && !reportData.isEmpty()) {
////				int rowIndex = 1;
////				for (BRRS_M_OPTR_Archival_Detail_Entity item : reportData) {
////					XSSFRow row = sheet.createRow(rowIndex++);
////
////					row.createCell(0).setCellValue(item.getCUST_ID());
////					row.createCell(1).setCellValue(item.getACCT_NUMBER());
////					row.createCell(2).setCellValue(item.getACCT_NAME());
////
////					// ACCT BALANCE (right aligned, 3 decimal places)
////					Cell balanceCell = row.createCell(3);
////					if (item.getACCT_BALANCE_IN_PULA() != null) {
////						balanceCell.setCellValue(item.getACCT_BALANCE_IN_PULA().doubleValue());
////					} else {
////						balanceCell.setCellValue(0.000);
////					}
////					balanceCell.setCellStyle(balanceStyle);
////
////					row.createCell(4).setCellValue(item.getROW_ID());
////					row.createCell(5).setCellValue(item.getCOLUMN_ID());
////					row.createCell(6)
////							.setCellValue(item.getREPORT_DATE() != null
////									? new SimpleDateFormat("dd-MM-yyyy").format(item.getREPORT_DATE())
////									: "");
////
////					// Apply data style for all other cells
////					for (int j = 0; j < 7; j++) {
////						if (j != 3) {
////							row.getCell(j).setCellStyle(dataStyle);
////						}
////					}
////				}
////			} else {
////				logger.info("No data found for BRRS_M_OPTR — only header will be written.");
////			}
////
////			// Write to byte[]
////			ByteArrayOutputStream bos = new ByteArrayOutputStream();
////			workbook.write(bos);
////			workbook.close();
////
////			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
////			return bos.toByteArray();
////
////		} catch (Exception e) {
////			logger.error("Error generating BRRS_M_OPTRExcel", e);
////			return new byte[0];
////		}
////	}
//
	//}
