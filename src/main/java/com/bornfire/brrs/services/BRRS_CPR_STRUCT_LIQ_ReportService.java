package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
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
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_CPR_STRUCT_LIQ_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_CPR_STRUCT_LIQ_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_CPR_STRUCT_LIQ_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_CPR_STRUCT_LIQ_Summary_Repo;
import com.bornfire.brrs.entities.CPR_STRUCT_LIQ_Archival_Detail_Entity;
import com.bornfire.brrs.entities.CPR_STRUCT_LIQ_Archival_Summary_Entity;
import com.bornfire.brrs.entities.CPR_STRUCT_LIQ_Detail_Entity;
import com.bornfire.brrs.entities.CPR_STRUCT_LIQ_Summary_Entity;
import com.bornfire.brrs.entities.FSI_Archival_Summary_Entity;
import com.bornfire.brrs.entities.FSI_Summary_Entity;


@Component
@Service
public class BRRS_CPR_STRUCT_LIQ_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_CPR_STRUCT_LIQ_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_CPR_STRUCT_LIQ_Detail_Repo BRRS_CPR_STRUCT_LIQ_Detail_Repo;

	@Autowired
	BRRS_CPR_STRUCT_LIQ_Summary_Repo BRRS_CPR_STRUCT_LIQ_Summary_Repo;

	@Autowired
	BRRS_CPR_STRUCT_LIQ_Archival_Detail_Repo BRRS_CPR_STRUCT_LIQ_Archival_Detail_Repo;

	@Autowired
	BRRS_CPR_STRUCT_LIQ_Archival_Summary_Repo BRRS_CPR_STRUCT_LIQ_Archival_Summary_Repo;


	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getCPR_STRUCT_LIQView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type,  String version) {
		ModelAndView mv = new ModelAndView();
//		Session hs = sessionFactory.getCurrentSession();
//		int pageSize = pageable.getPageSize();
//		int currentPage = pageable.getPageNumber();
//		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<CPR_STRUCT_LIQ_Archival_Summary_Entity> T1Master = new ArrayList<CPR_STRUCT_LIQ_Archival_Summary_Entity>();
//			List<CPR_STRUCT_LIQ_Manual_Archival_Summary_Entity> T2Master = new ArrayList<CPR_STRUCT_LIQ_Manual_Archival_Summary_Entity>();

			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_CPR_STRUCT_LIQ_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
//				T2Master = BRRS_CPR_STRUCT_LIQ_Manual_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
				

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
//			mv.addObject("reportsummary1", T2Master);
            
		} else {
			List<CPR_STRUCT_LIQ_Summary_Entity> T1Master = new ArrayList<CPR_STRUCT_LIQ_Summary_Entity>();
//			List<CPR_STRUCT_LIQ_Manual_Summary_Entity> T2Master = new ArrayList<CPR_STRUCT_LIQ_Manual_Summary_Entity>();
			
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_CPR_STRUCT_LIQ_Summary_Repo.getdatabydateList(dateformat.parse(todate));
//				T2Master = BRRS_CPR_STRUCT_LIQ_Manual_Summary_Repo.getdatabydateList(dateformat.parse(todate));
				

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
//			mv.addObject("reportsummary1", T2Master);
            
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/CPR_STRUCT_LIQ");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

//	public ModelAndView getCPR_STRUCT_LIQcurrentDtl(String reportId, String fromdate, String todate, String currency,
//			  String dtltype, Pageable pageable, String Filter, String type, String version) {
//
//	int pageSize = pageable != null ? pageable.getPageSize() : 10;
//	int currentPage = pageable != null ? pageable.getPageNumber() : 0;
//	int totalPages = 0;
//
//	ModelAndView mv = new ModelAndView();
//	Session hs = sessionFactory.getCurrentSession();
//
//	try {
//		Date parsedDate = null;
//		if (todate != null && !todate.isEmpty()) {
//			parsedDate = dateformat.parse(todate);
//		}
//
//		String rowId = null;
//		String columnId = null;
//
//		// ✅ Split filter string into rowId & columnId
//		if (Filter != null && Filter.contains(",")) {
//			String[] parts = Filter.split(",");
//			if (parts.length >= 2) {
//				rowId = parts[0];
//				columnId = parts[1];
//			}
//		}
//	
//		if ("ARCHIVAL".equals(type) && version != null) {
//			// 🔹 Archival branch
//			List<CPR_STRUCT_LIQ_Archival_Detail_Entity> T1Dt1;
//			if (rowId != null && columnId != null) {
//				T1Dt1 = BRRS_CPR_STRUCT_LIQ_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
//			} else {
//				T1Dt1 = BRRS_CPR_STRUCT_LIQ_Archival_Detail_Repo.getdatabydateList(parsedDate, version);					
//			}
//
//			mv.addObject("reportdetails", T1Dt1);
//			mv.addObject("reportmaster12", T1Dt1);
//			System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
//
//		} else {
//			// 🔹 Current branch
//			List<CPR_STRUCT_LIQ_Detail_Entity> T1Dt1;
//			if (rowId != null && columnId != null) {
//				T1Dt1 = BRRS_CPR_STRUCT_LIQ_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
//			} else {
//				T1Dt1 = BRRS_CPR_STRUCT_LIQ_Detail_Repo.getdatabydateList(parsedDate);
//				totalPages = BRRS_CPR_STRUCT_LIQ_Detail_Repo.getdatacount(parsedDate);
//				mv.addObject("pagination", "YES");
//			}
//
//			mv.addObject("reportdetails", T1Dt1);
//			mv.addObject("reportmaster12", T1Dt1);
//			System.out.println("LISTCOUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
//		}
//
//	} catch (ParseException e) {
//		e.printStackTrace();
//		mv.addObject("errorMessage", "Invalid date format: " + todate);
//	} catch (Exception e) {
//		e.printStackTrace();
//		mv.addObject("errorMessage", "Unexpected error: " + e.getMessage());
//	}
//
//	// ✅ Common attributes
//	mv.setViewName("BRRS/CPR_STRUCT_LIQ");
//	mv.addObject("displaymode", "Details");
//	mv.addObject("currentPage", currentPage);
//	System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
//	mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
//	mv.addObject("reportsflag", "reportsflag");
//	mv.addObject("menu", reportId);
//
//	return mv;
//}

	public ModelAndView getCPR_STRUCT_LIQcurrentDtl(String reportId, String fromdate, String todate, String currency,
			  String dtltype, Pageable pageable, String Filter, String type, String version) {

	int pageSize = pageable != null ? pageable.getPageSize() : 10;
	int currentPage = pageable != null ? pageable.getPageNumber() : 0;
	int totalPages = 0;

	ModelAndView mv = new ModelAndView();
//	Session hs = sessionFactory.getCurrentSession();

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
	
		if ("ARCHIVAL".equals(type) && version != null) {
			// 🔹 Archival branch
			List<CPR_STRUCT_LIQ_Archival_Detail_Entity> T1Dt1;
			if (rowId != null && columnId != null) {
				T1Dt1 = BRRS_CPR_STRUCT_LIQ_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
			} else {
				T1Dt1 = BRRS_CPR_STRUCT_LIQ_Archival_Detail_Repo.getdatabydateList(parsedDate, version);					
			}

			mv.addObject("reportdetails", T1Dt1);
			mv.addObject("reportmaster12", T1Dt1);
			System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

		} else {
			// 🔹 Current branch
			List<CPR_STRUCT_LIQ_Detail_Entity> T1Dt1;
			if (rowId != null && columnId != null) {
				T1Dt1 = BRRS_CPR_STRUCT_LIQ_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
			} else {
				T1Dt1 = BRRS_CPR_STRUCT_LIQ_Detail_Repo.getdatabydateList(parsedDate);
				totalPages = BRRS_CPR_STRUCT_LIQ_Detail_Repo.getdatacount(parsedDate);
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
	mv.setViewName("BRRS/CPR_STRUCT_LIQ");
	mv.addObject("displaymode", "Details");
	mv.addObject("currentPage", currentPage);
	System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
	mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
	mv.addObject("reportsflag", "reportsflag");
	mv.addObject("menu", reportId);

	return mv;
}

	
//	public void updateReport(CPR_STRUCT_LIQ_Manual_Summary_Entity updatedEntity) {
//	    System.out.println("Came to services1");
//	    System.out.println("Report Date: " + updatedEntity.getReport_date());
//
//	    CPR_STRUCT_LIQ_Manual_Summary_Entity existing = BRRS_CPR_STRUCT_LIQ_Manual_Summary_Repo.findById(updatedEntity.getReport_date())
//	            .orElseThrow(() -> new RuntimeException(
//	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));
//
//	    try {
//	        // ✅ Loop for fields
//	        int[] Rows = {23,25,26,30,34,35,42,43,44,46};
//	        for (int i : Rows) {
//	            String prefix = "R" + i + "_";
//	            String[] fields = {"total_no_of_acct", "total_value"};
//
//	            for (String field : fields) {
//	                try {
//	                    String getterName = "get" + prefix + field;
//	                    String setterName = "set" + prefix + field;
//
//	                    Method getter = CPR_STRUCT_LIQ_Manual_Summary_Entity.class.getMethod(getterName);
//	                    Method setter = CPR_STRUCT_LIQ_Manual_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//
//	                    Object newValue = getter.invoke(updatedEntity);
//	                    setter.invoke(existing, newValue);
//
//	                } catch (NoSuchMethodException e) {
//	                    // Skip missing getter/setter gracefully
//	                    continue;
//	                }
//	            }
//	        }
//
//
//	        // ✅ Save after all updates
//	        BRRS_CPR_STRUCT_LIQ_Manual_Summary_Repo.save(existing);
//
//	    } catch (Exception e) {
//	        throw new RuntimeException("Error while updating report fields", e);
//	    }
//	}

	
	
	
	public byte[] getCPR_STRUCT_LIQDetailExcel(String filename, String fromdate, String todate, String currency,
			   String dtltype, String type, String version) {
try {
logger.info("Generating Excel for CPR_STRUCT_LIQ Details...");
System.out.println("came to Detail download service");


if (type.equals("ARCHIVAL") & version != null) {
byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
version);
return ARCHIVALreport;
}

XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("CPR_STRUCT_LIQDetail");

//Common border style
BorderStyle border = BorderStyle.THIN;

//Header style (left aligned)
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

//Right-aligned header style for ACCT BALANCE
CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

//Default data style (left aligned)
CellStyle dataStyle = workbook.createCellStyle();
dataStyle.setAlignment(HorizontalAlignment.LEFT);
dataStyle.setBorderTop(border);
dataStyle.setBorderBottom(border);
dataStyle.setBorderLeft(border);
dataStyle.setBorderRight(border);

//ACCT BALANCE style (right aligned with thousand separator)
CellStyle balanceStyle = workbook.createCellStyle();
balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
balanceStyle.setBorderTop(border);
balanceStyle.setBorderBottom(border);
balanceStyle.setBorderLeft(border);
balanceStyle.setBorderRight(border);






//Header row
String[] headers = {
"CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABLE", "REPORT ADDL CRITERIA1",
"REPORT_DATE"
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

//Get data
Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
List<CPR_STRUCT_LIQ_Detail_Entity> reportData = BRRS_CPR_STRUCT_LIQ_Detail_Repo.getdatabydateList(parsedToDate);

if (reportData != null && !reportData.isEmpty()) {
int rowIndex = 1;
for (CPR_STRUCT_LIQ_Detail_Entity item : reportData) {
XSSFRow row = sheet.createRow(rowIndex++);

row.createCell(0).setCellValue(item.getCustId());
row.createCell(1).setCellValue(item.getAcctNumber());
row.createCell(2).setCellValue(item.getAcctName());

//ACCT BALANCE (right aligned, 3 decimal places)
Cell balanceCell = row.createCell(3);
if (item.getAcctBalanceInpula() != null) {
balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
} else {
balanceCell.setCellValue(0);
}
balanceCell.setCellStyle(balanceStyle);

		row.createCell(4).setCellValue(item.getReportLable());
		row.createCell(5).setCellValue(item.getReportAddlCriteria1());
		row.createCell(6)
				.setCellValue(item.getReportDate() != null
						? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
						: "");

		// Apply data style for all other cells
		for (int j = 0; j < 7; j++) {
			if (j != 3) {
				row.getCell(j).setCellStyle(dataStyle);
			}
		}
	}
} else {
	logger.info("No data found for CPR_STRUCT_LIQ — only header will be written.");
}

//Write to byte[]
ByteArrayOutputStream bos = new ByteArrayOutputStream();
workbook.write(bos);
workbook.close();

logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
return bos.toByteArray();

} catch (Exception e) {
logger.error("Error generating CPR_STRUCT_LIQ Excel", e);
return new byte[0];
}
}

	public byte[] getCPR_STRUCT_LIQExcel(String filename, String reportId, String fromdate, String todate, String currency,
			 String dtltype, String type, String version) throws Exception {
logger.info("Service: Starting Excel generation process in memory.");
logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

//Convert string to Date
Date reportDate = dateformat.parse(todate);

//ARCHIVAL check
if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
logger.info("Service: Generating ARCHIVAL report for version {}", version);
return getExcelCPR_STRUCT_LIQARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
}
//RESUB check
else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
logger.info("Service: Generating RESUB report for version {}", version);


List<CPR_STRUCT_LIQ_Archival_Summary_Entity> T1Master =
BRRS_CPR_STRUCT_LIQ_Archival_Summary_Repo.getdatabydateListarchival(reportDate, version);


}


//Default (LIVE) case
List<CPR_STRUCT_LIQ_Summary_Entity> dataList1 = BRRS_CPR_STRUCT_LIQ_Summary_Repo.getdatabydateList(reportDate);

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
font.setFontHeightInPoints((short)8); // size 8
font.setFontName("Arial");    

CellStyle numberStyle = workbook.createCellStyle();
//numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
numberStyle.setBorderBottom(BorderStyle.THIN);
numberStyle.setBorderTop(BorderStyle.THIN);
numberStyle.setBorderLeft(BorderStyle.THIN);
numberStyle.setBorderRight(BorderStyle.THIN);
numberStyle.setFont(font);
//--- End of Style Definitions ---

int startRow = 7;

if (!dataList1.isEmpty()) {
for (int i = 0; i < dataList1.size(); i++) {

CPR_STRUCT_LIQ_Summary_Entity record = dataList1.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}


Cell cell2 = row.createCell(2);
if (record.getR8_1_DAY() != null) {
   cell2.setCellValue(record.getR8_1_DAY().doubleValue());
   cell2.setCellStyle(numberStyle);
} else {
   cell2.setCellValue("");
   cell2.setCellStyle(textStyle);
}

Cell cell3 = row.createCell(3);
if (record.getR8_2TO7_DAYS() != null) {
   cell3.setCellValue(record.getR8_2TO7_DAYS().doubleValue());
   cell3.setCellStyle(numberStyle);
} else {
   cell3.setCellValue("");
   cell3.setCellStyle(textStyle);
}

Cell cell4 = row.createCell(4);
if (record.getR8_8TO14_DAYS() != null) {
   cell4.setCellValue(record.getR8_8TO14_DAYS().doubleValue());
   cell4.setCellStyle(numberStyle);
} else {
   cell4.setCellValue("");
   cell4.setCellStyle(textStyle);
}

Cell cell5 = row.createCell(5);
if (record.getR8_15TO30_DAYS() != null) {
   cell5.setCellValue(record.getR8_15TO30_DAYS().doubleValue());
   cell5.setCellStyle(numberStyle);
} else {
   cell5.setCellValue("");
   cell5.setCellStyle(textStyle);
}

Cell cell6 = row.createCell(6);
if (record.getR8_31DAYS_UPTO_2MONTHS() != null) {
   cell6.setCellValue(record.getR8_31DAYS_UPTO_2MONTHS().doubleValue());
   cell6.setCellStyle(numberStyle);
} else {
   cell6.setCellValue("");
   cell6.setCellStyle(textStyle);
}

Cell cell7 = row.createCell(7);
if (record.getR8_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
   cell7.setCellValue(record.getR8_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
   cell7.setCellStyle(numberStyle);
} else {
   cell7.setCellValue("");
   cell7.setCellStyle(textStyle);
}

Cell cell8 = row.createCell(8);
if (record.getR8_OVER_3MONTHS_UPTO_6MONTHS() != null) {
   cell8.setCellValue(record.getR8_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
   cell8.setCellStyle(numberStyle);
} else {
   cell8.setCellValue("");
   cell8.setCellStyle(textStyle);
}

Cell cell9 = row.createCell(9);
if (record.getR8_OVER_6MONTHS_UPTO_1YEAR() != null) {
   cell9.setCellValue(record.getR8_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
   cell9.setCellStyle(numberStyle);
} else {
   cell9.setCellValue("");
   cell9.setCellStyle(textStyle);
}

Cell cell10 = row.createCell(10);
if (record.getR8_OVER_1YEAR_UPTO_3YEARS() != null) {
   cell10.setCellValue(record.getR8_OVER_1YEAR_UPTO_3YEARS().doubleValue());
   cell10.setCellStyle(numberStyle);
} else {
   cell10.setCellValue("");
   cell10.setCellStyle(textStyle);
}

Cell cell11 = row.createCell(11);
if (record.getR8_OVER_3YEARS_UPTO_5YEARS() != null) {
   cell11.setCellValue(record.getR8_OVER_3YEARS_UPTO_5YEARS().doubleValue());
   cell11.setCellStyle(numberStyle);
} else {
   cell11.setCellValue("");
   cell2.setCellStyle(textStyle);
}

Cell cell12 = row.createCell(12);
if (record.getR8_OVER_3YEARS_UPTO_5YEARS() != null) {
   cell12.setCellValue(record.getR8_OVER_3YEARS_UPTO_5YEARS().doubleValue());
   cell12.setCellStyle(numberStyle);
} else {
   cell12.setCellValue("");
   cell12.setCellStyle(textStyle);
}

//-------- R9 --------
row = sheet.getRow(8);
cell2 = row.createCell(2);
if (record.getR9_1_DAY() != null) {
cell2.setCellValue(record.getR9_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR9_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR9_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR9_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR9_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR9_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR9_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR9_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR9_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR9_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR9_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR9_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR9_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR9_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR9_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR9_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR9_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR9_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR9_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR9_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR9_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R10 --------
row = sheet.getRow(9);
cell2 = row.createCell(2);
if (record.getR10_1_DAY() != null) {
cell2.setCellValue(record.getR10_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR10_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR10_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR10_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR10_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR10_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR10_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR10_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR10_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR10_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR10_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR10_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR10_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR10_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR10_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR10_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR10_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR10_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR10_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR10_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR10_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R11 --------
row = sheet.getRow(10);
cell2 = row.createCell(2);
if (record.getR11_1_DAY() != null) {
cell2.setCellValue(record.getR11_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR11_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR11_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR11_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR11_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR11_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR11_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR11_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR11_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR11_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR11_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR11_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR11_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR11_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR11_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR11_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR11_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR11_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR11_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR11_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR11_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R12 --------
row = sheet.getRow(11);
cell2 = row.createCell(2);
if (record.getR12_1_DAY() != null) {
cell2.setCellValue(record.getR12_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR12_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR12_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR12_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR12_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR12_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR12_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR12_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR12_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR12_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR12_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR12_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR12_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR12_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR12_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR12_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR12_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR12_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR12_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR12_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR12_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R13 --------
row = sheet.getRow(12);
cell2 = row.createCell(2);
if (record.getR13_1_DAY() != null) {
cell2.setCellValue(record.getR13_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR13_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR13_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR13_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR13_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR13_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR13_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR13_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR13_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR13_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR13_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR13_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR13_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR13_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR13_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR13_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR13_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR13_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR13_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR13_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR13_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R14 --------
row = sheet.getRow(13);
cell2 = row.createCell(2);
if (record.getR14_1_DAY() != null) {
cell2.setCellValue(record.getR14_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR14_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR14_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR14_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR14_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR14_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR14_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR14_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR14_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR14_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR14_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR14_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR14_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR14_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR14_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR14_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR14_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR14_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR14_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR14_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR14_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R15 --------
row = sheet.getRow(14);
cell2 = row.createCell(2);
if (record.getR15_1_DAY() != null) {
cell2.setCellValue(record.getR15_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR15_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR15_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR15_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR15_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR15_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR15_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR15_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR15_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR15_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR15_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR15_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR15_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR15_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR15_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR15_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR15_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR15_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR15_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR15_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR15_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R16 --------
row = sheet.getRow(15);
cell2 = row.createCell(2);
if (record.getR16_1_DAY() != null) {
cell2.setCellValue(record.getR16_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR16_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR16_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR16_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR16_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR16_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR16_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR16_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR16_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR16_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR16_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR16_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR16_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR16_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR16_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR16_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR16_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR16_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR16_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR16_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR16_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R17 --------
row = sheet.getRow(16);
cell2 = row.createCell(2);
if (record.getR17_1_DAY() != null) {
cell2.setCellValue(record.getR17_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR17_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR17_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR17_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR17_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR17_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR17_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR17_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR17_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR17_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR17_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR17_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR17_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR17_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR17_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR17_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR17_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR17_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR17_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR17_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR17_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R18 --------
row = sheet.getRow(17);
cell2 = row.createCell(2);
if (record.getR18_1_DAY() != null) {
cell2.setCellValue(record.getR18_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR18_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR18_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR18_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR18_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR18_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR18_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR18_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR18_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR18_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR18_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR18_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR18_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR18_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR18_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR18_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR18_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR18_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR18_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR18_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR18_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R19 --------
row = sheet.getRow(18);
cell2 = row.createCell(2);
if (record.getR19_1_DAY() != null) {
cell2.setCellValue(record.getR19_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR19_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR19_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR19_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR19_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR19_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR19_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR19_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR19_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR19_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR19_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR19_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR19_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR19_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR19_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR19_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR19_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR19_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR19_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR19_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR19_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R20 --------
row = sheet.getRow(19);
cell2 = row.createCell(2);
if (record.getR20_1_DAY() != null) {
cell2.setCellValue(record.getR20_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR20_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR20_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR20_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR20_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR20_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR20_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR20_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR20_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR20_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR20_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR20_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR20_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR20_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR20_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR20_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR20_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR20_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR20_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR20_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR20_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R21 --------
row = sheet.getRow(20);
cell2 = row.createCell(2);
if (record.getR21_1_DAY() != null) {
cell2.setCellValue(record.getR21_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR21_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR21_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR21_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR21_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR21_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR21_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR21_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR21_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR21_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR21_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR21_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR21_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR21_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR21_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR21_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR21_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR21_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR21_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR21_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR21_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R22 --------
row = sheet.getRow(21);
cell2 = row.createCell(2);
if (record.getR22_1_DAY() != null) {
cell2.setCellValue(record.getR22_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR22_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR22_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR22_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR22_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR22_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR22_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR22_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR22_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR22_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR22_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR22_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR22_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR22_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR22_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR22_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR22_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR22_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR22_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR22_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR22_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R23 --------
row = sheet.getRow(22);
cell2 = row.createCell(2);
if (record.getR23_1_DAY() != null) {
cell2.setCellValue(record.getR23_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR23_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR23_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR23_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR23_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR23_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR23_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR23_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR23_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR23_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR23_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR23_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR23_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR23_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR23_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR23_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR23_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR23_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR23_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR23_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR23_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R24 --------
row = sheet.getRow(23);
cell2 = row.createCell(2);
if (record.getR24_1_DAY() != null) {
cell2.setCellValue(record.getR24_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR24_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR24_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR24_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR24_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR24_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR24_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR24_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR24_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR24_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR24_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR24_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR24_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR24_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR24_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR24_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR24_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR24_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR24_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR24_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR24_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R25 --------
row = sheet.getRow(24);
cell2 = row.createCell(2);
if (record.getR25_1_DAY() != null) {
cell2.setCellValue(record.getR25_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR25_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR25_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR25_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR25_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR25_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR25_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR25_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR25_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR25_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR25_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR25_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR25_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR25_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR25_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR25_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR25_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR25_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR25_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR25_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR25_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R26 --------
row = sheet.getRow(25);
cell2 = row.createCell(2);
if (record.getR26_1_DAY() != null) {
cell2.setCellValue(record.getR26_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR26_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR26_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR26_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR26_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR26_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR26_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR26_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR26_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR26_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR26_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR26_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR26_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR26_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR26_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR26_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR26_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR26_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR26_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR26_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR26_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R27 --------
row = sheet.getRow(26);
cell2 = row.createCell(2);
if (record.getR27_1_DAY() != null) {
cell2.setCellValue(record.getR27_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR27_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR27_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR27_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR27_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR27_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR27_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR27_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR27_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR27_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR27_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR27_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR27_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR27_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR27_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR27_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR27_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR27_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR27_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR27_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR27_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R28 --------
row = sheet.getRow(27);
cell2 = row.createCell(2);
if (record.getR28_1_DAY() != null) {
cell2.setCellValue(record.getR28_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR28_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR28_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR28_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR28_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR28_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR28_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR28_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR28_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR28_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR28_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR28_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR28_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR28_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR28_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR28_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR28_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR28_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR28_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR28_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR28_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R29 --------
row = sheet.getRow(28);
cell2 = row.createCell(2);
if (record.getR29_1_DAY() != null) {
cell2.setCellValue(record.getR29_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR29_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR29_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR29_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR29_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR29_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR29_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR29_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR29_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR29_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR29_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR29_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR29_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR29_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR29_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR29_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR29_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR29_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR29_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR29_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR29_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R30 --------
row = sheet.getRow(29);
cell2 = row.createCell(2);
if (record.getR30_1_DAY() != null) {
cell2.setCellValue(record.getR30_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR30_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR30_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR30_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR30_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR30_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR30_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR30_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR30_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR30_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR30_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR30_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR30_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR30_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR30_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR30_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR30_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR30_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR30_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR30_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR30_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R31 --------
row = sheet.getRow(30);
cell2 = row.createCell(2);
if (record.getR31_1_DAY() != null) {
cell2.setCellValue(record.getR31_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR31_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR31_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR31_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR31_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR31_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR31_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR31_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR31_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR31_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR31_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR31_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR31_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR31_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR31_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR31_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR31_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR31_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR31_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR31_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR31_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R32 --------
row = sheet.getRow(31);
cell2 = row.createCell(2);
if (record.getR32_1_DAY() != null) {
cell2.setCellValue(record.getR32_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR32_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR32_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR32_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR32_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR32_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR32_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR32_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR32_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR32_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR32_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR32_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR32_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR32_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR32_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR32_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR32_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR32_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR32_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR32_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR32_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R33 --------
row = sheet.getRow(32);
cell2 = row.createCell(2);
if (record.getR33_1_DAY() != null) {
cell2.setCellValue(record.getR33_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR33_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR33_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR33_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR33_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR33_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR33_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR33_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR33_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR33_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR33_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR33_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR33_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR33_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR33_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR33_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR33_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR33_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR33_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR33_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR33_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R34 --------
row = sheet.getRow(33);
cell2 = row.createCell(2);
if (record.getR34_1_DAY() != null) {
cell2.setCellValue(record.getR34_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR34_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR34_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR34_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR34_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR34_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR34_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR34_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR34_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR34_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR34_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR34_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR34_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR34_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR34_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR34_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR34_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR34_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR34_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR34_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR34_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R35 --------
row = sheet.getRow(34);
cell2 = row.createCell(2);
if (record.getR35_1_DAY() != null) {
cell2.setCellValue(record.getR35_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR35_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR35_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR35_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR35_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR35_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR35_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR35_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR35_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR35_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR35_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR35_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR35_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR35_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR35_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR35_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR35_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR35_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR35_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR35_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR35_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R36 --------
row = sheet.getRow(35);
cell2 = row.createCell(2);
if (record.getR36_1_DAY() != null) {
cell2.setCellValue(record.getR36_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR36_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR36_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR36_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR36_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR36_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR36_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR36_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR36_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR36_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR36_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR36_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR36_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR36_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR36_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR36_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR36_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR36_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR36_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR36_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR36_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}

//-------- R46 --------
row = sheet.getRow(45);
cell2 = row.createCell(2);
if (record.getR46_1_DAY() != null) {
cell2.setCellValue(record.getR46_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR46_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR46_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR46_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR46_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR46_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR46_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR46_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR46_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR46_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR46_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR46_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR46_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR46_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR46_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR46_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR46_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR46_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR46_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR46_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR46_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R47 --------
row = sheet.getRow(46);
cell2 = row.createCell(2);
if (record.getR47_1_DAY() != null) {
cell2.setCellValue(record.getR47_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR47_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR47_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR47_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR47_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR47_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR47_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR47_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR47_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR47_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR47_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR47_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR47_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR47_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR47_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR47_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR47_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR47_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR47_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR47_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR47_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R48 --------
row = sheet.getRow(47);
cell2 = row.createCell(2);
if (record.getR48_1_DAY() != null) {
cell2.setCellValue(record.getR48_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR48_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR48_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR48_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR48_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR48_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR48_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR48_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR48_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR48_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR48_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR48_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR48_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR48_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR48_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR48_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR48_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR48_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR48_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR48_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR48_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R49 --------
row = sheet.getRow(48);
cell2 = row.createCell(2);
if (record.getR49_1_DAY() != null) {
cell2.setCellValue(record.getR49_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR49_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR49_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR49_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR49_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR49_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR49_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR49_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR49_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR49_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR49_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR49_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR49_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR49_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR49_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR49_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR49_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR49_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR49_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR49_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR49_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R50 --------
row = sheet.getRow(49);
cell2 = row.createCell(2);
if (record.getR50_1_DAY() != null) {
cell2.setCellValue(record.getR50_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR50_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR50_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR50_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR50_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR50_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR50_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR50_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR50_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR50_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR50_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR50_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR50_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR50_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR50_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR50_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR50_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR50_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR50_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR50_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR50_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R51 --------
row = sheet.getRow(50);
cell2 = row.createCell(2);
if (record.getR51_1_DAY() != null) {
cell2.setCellValue(record.getR51_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR51_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR51_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR51_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR51_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR51_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR51_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR51_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR51_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR51_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR51_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR51_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR51_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR51_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR51_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR51_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR51_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR51_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR51_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR51_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR51_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R52 --------
row = sheet.getRow(51);
cell2 = row.createCell(2);
if (record.getR52_1_DAY() != null) {
cell2.setCellValue(record.getR52_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR52_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR52_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR52_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR52_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR52_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR52_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR52_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR52_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR52_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR52_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR52_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR52_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR52_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR52_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR52_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR52_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR52_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR52_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR52_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR52_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R53 --------
row = sheet.getRow(52);
cell2 = row.createCell(2);
if (record.getR53_1_DAY() != null) {
cell2.setCellValue(record.getR53_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR53_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR53_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR53_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR53_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR53_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR53_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR53_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR53_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR53_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR53_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR53_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR53_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR53_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR53_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR53_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR53_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR53_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR53_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR53_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR53_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R54 --------
row = sheet.getRow(53);
cell2 = row.createCell(2);
if (record.getR54_1_DAY() != null) {
cell2.setCellValue(record.getR54_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR54_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR54_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR54_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR54_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR54_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR54_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR54_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR54_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR54_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR54_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR54_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR54_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR54_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR54_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR54_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR54_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR54_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR54_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR54_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR54_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R55 --------
row = sheet.getRow(54);
cell2 = row.createCell(2);
if (record.getR55_1_DAY() != null) {
cell2.setCellValue(record.getR55_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR55_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR55_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR55_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR55_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR55_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR55_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR55_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR55_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR55_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR55_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR55_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR55_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR55_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR55_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR55_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR55_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR55_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR55_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR55_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR55_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R56 --------
row = sheet.getRow(55);
cell2 = row.createCell(2);
if (record.getR56_1_DAY() != null) {
cell2.setCellValue(record.getR56_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR56_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR56_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR56_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR56_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR56_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR56_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR56_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR56_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR56_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR56_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR56_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR56_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR56_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR56_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR56_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR56_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR56_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR56_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR56_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR56_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R57 --------
row = sheet.getRow(56);
cell2 = row.createCell(2);
if (record.getR57_1_DAY() != null) {
cell2.setCellValue(record.getR57_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR57_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR57_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR57_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR57_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR57_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR57_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR57_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR57_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR57_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR57_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR57_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR57_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR57_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR57_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR57_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR57_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR57_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR57_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR57_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR57_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R58 --------
row = sheet.getRow(57);
cell2 = row.createCell(2);
if (record.getR58_1_DAY() != null) {
cell2.setCellValue(record.getR58_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR58_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR58_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR58_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR58_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR58_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR58_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR58_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR58_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR58_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR58_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR58_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR58_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR58_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR58_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR58_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR58_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR58_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR58_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR58_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR58_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R59 --------
row = sheet.getRow(58);
cell2 = row.createCell(2);
if (record.getR59_1_DAY() != null) {
cell2.setCellValue(record.getR59_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR59_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR59_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR59_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR59_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR59_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR59_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR59_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR59_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR59_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR59_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR59_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR59_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR59_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR59_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR59_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR59_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR59_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR59_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR59_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR59_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R60 --------
row = sheet.getRow(59);
cell2 = row.createCell(2);
if (record.getR60_1_DAY() != null) {
cell2.setCellValue(record.getR60_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR60_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR60_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR60_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR60_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR60_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR60_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR60_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR60_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR60_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR60_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR60_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR60_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR60_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR60_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR60_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR60_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR60_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR60_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR60_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR60_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R61 --------
row = sheet.getRow(60);
cell2 = row.createCell(2);
if (record.getR61_1_DAY() != null) {
cell2.setCellValue(record.getR61_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR61_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR61_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR61_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR61_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR61_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR61_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR61_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR61_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR61_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR61_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR61_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR61_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR61_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR61_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR61_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR61_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR61_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR61_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR61_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR61_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R62 --------
row = sheet.getRow(61);
cell2 = row.createCell(2);
if (record.getR62_1_DAY() != null) {
cell2.setCellValue(record.getR62_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR62_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR62_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR62_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR62_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR62_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR62_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR62_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR62_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR62_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR62_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR62_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR62_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR62_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR62_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR62_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR62_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR62_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR62_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR62_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR62_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R63 --------
row = sheet.getRow(62);
cell2 = row.createCell(2);
if (record.getR63_1_DAY() != null) {
cell2.setCellValue(record.getR63_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR63_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR63_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR63_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR63_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR63_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR63_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR63_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR63_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR63_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR63_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR63_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR63_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR63_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR63_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR63_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR63_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR63_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR63_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR63_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR63_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R64 --------
row = sheet.getRow(63);
cell2 = row.createCell(2);
if (record.getR64_1_DAY() != null) {
cell2.setCellValue(record.getR64_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR64_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR64_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR64_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR64_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR64_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR64_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR64_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR64_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR64_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR64_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR64_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR64_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR64_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR64_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR64_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR64_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR64_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR64_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR64_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR64_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R65 --------
row = sheet.getRow(64);
cell2 = row.createCell(2);
if (record.getR65_1_DAY() != null) {
cell2.setCellValue(record.getR65_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR65_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR65_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR65_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR65_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR65_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR65_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR65_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR65_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR65_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR65_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR65_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR65_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR65_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR65_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR65_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR65_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR65_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR65_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR65_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR65_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R66 --------
row = sheet.getRow(65);
cell2 = row.createCell(2);
if (record.getR66_1_DAY() != null) {
cell2.setCellValue(record.getR66_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR66_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR66_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR66_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR66_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR66_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR66_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR66_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR66_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR66_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR66_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR66_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR66_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR66_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR66_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR66_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR66_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR66_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR66_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR66_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR66_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R67 --------
row = sheet.getRow(66);
cell2 = row.createCell(2);
if (record.getR67_1_DAY() != null) {
cell2.setCellValue(record.getR67_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR67_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR67_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR67_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR67_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR67_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR67_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR67_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR67_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR67_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR67_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR67_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR67_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR67_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR67_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR67_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR67_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR67_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR67_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR67_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR67_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R68 --------
row = sheet.getRow(67);
cell2 = row.createCell(2);
if (record.getR68_1_DAY() != null) {
cell2.setCellValue(record.getR68_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR68_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR68_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR68_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR68_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR68_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR68_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR68_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR68_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR68_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR68_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR68_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR68_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR68_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR68_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR68_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR68_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR68_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR68_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR68_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR68_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R69 --------
row = sheet.getRow(68);
cell2 = row.createCell(2);
if (record.getR69_1_DAY() != null) {
cell2.setCellValue(record.getR69_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR69_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR69_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR69_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR69_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR69_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR69_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR69_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR69_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR69_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR69_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR69_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR69_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR69_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR69_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR69_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR69_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR69_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR69_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR69_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR69_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R70 --------
row = sheet.getRow(69);
cell2 = row.createCell(2);
if (record.getR70_1_DAY() != null) {
cell2.setCellValue(record.getR70_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR70_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR70_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR70_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR70_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR70_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR70_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR70_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR70_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR70_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR70_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR70_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR70_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR70_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR70_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR70_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR70_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR70_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR70_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR70_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR70_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R71 --------
row = sheet.getRow(70);
cell2 = row.createCell(2);
if (record.getR71_1_DAY() != null) {
cell2.setCellValue(record.getR71_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR71_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR71_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR71_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR71_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR71_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR71_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR71_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR71_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR71_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR71_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR71_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR71_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR71_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR71_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR71_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR71_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR71_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR71_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR71_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR71_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}


//-------- R72 --------
row = sheet.getRow(71);
cell2 = row.createCell(2);
if (record.getR72_1_DAY() != null) {
cell2.setCellValue(record.getR72_1_DAY().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR72_2TO7_DAYS() != null) {
cell3.setCellValue(record.getR72_2TO7_DAYS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR72_8TO14_DAYS() != null) {
cell4.setCellValue(record.getR72_8TO14_DAYS().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR72_15TO30_DAYS() != null) {
cell5.setCellValue(record.getR72_15TO30_DAYS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR72_31DAYS_UPTO_2MONTHS() != null) {
cell6.setCellValue(record.getR72_31DAYS_UPTO_2MONTHS().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

cell7 = row.createCell(7);
if (record.getR72_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
cell7.setCellValue(record.getR72_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
cell7.setCellStyle(numberStyle);
} else {
cell7.setCellValue("");
cell7.setCellStyle(textStyle);
}

cell8 = row.createCell(8);
if (record.getR72_OVER_3MONTHS_UPTO_6MONTHS() != null) {
cell8.setCellValue(record.getR72_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
cell8.setCellStyle(numberStyle);
} else {
cell8.setCellValue("");
cell8.setCellStyle(textStyle);
}

cell9 = row.createCell(9);
if (record.getR72_OVER_6MONTHS_UPTO_1YEAR() != null) {
cell9.setCellValue(record.getR72_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
cell9.setCellStyle(numberStyle);
} else {
cell9.setCellValue("");
cell9.setCellStyle(textStyle);
}

cell10 = row.createCell(10);
if (record.getR72_OVER_1YEAR_UPTO_3YEARS() != null) {
cell10.setCellValue(record.getR72_OVER_1YEAR_UPTO_3YEARS().doubleValue());
cell10.setCellStyle(numberStyle);
} else {
cell10.setCellValue("");
cell10.setCellStyle(textStyle);
}

cell11 = row.createCell(11);
if (record.getR72_OVER_3YEARS_UPTO_5YEARS() != null) {
cell11.setCellValue(record.getR72_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell11.setCellStyle(numberStyle);
} else {
cell11.setCellValue("");
cell11.setCellStyle(textStyle);
}

cell12 = row.createCell(12);
if (record.getR72_OVER_3YEARS_UPTO_5YEARS() != null) {
cell12.setCellValue(record.getR72_OVER_3YEARS_UPTO_5YEARS().doubleValue());
cell12.setCellStyle(numberStyle);
} else {
cell12.setCellValue("");
cell12.setCellStyle(textStyle);
}
}
workbook.setForceFormulaRecalculation(true);
} else {

}
// Write the final workbook content to the in-memory stream.
workbook.write(out);
logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
return out.toByteArray();
}
}

	public List<Object> getCPR_STRUCT_LIQArchival() {
		List<Object> CPR_STRUCT_LIQArchivallist = new ArrayList<>();
		try {
			CPR_STRUCT_LIQArchivallist = BRRS_CPR_STRUCT_LIQ_Archival_Summary_Repo.getCPR_STRUCT_LIQarchival();
//			CPR_STRUCT_LIQArchivallist = BRRS_CPR_STRUCT_LIQ_Manual_Archival_Summary_Repo.getCPR_STRUCT_LIQarchival();
			System.out.println("countser" + CPR_STRUCT_LIQArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching CPR_STRUCT_LIQ Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return CPR_STRUCT_LIQArchivallist;
	}
	
	
	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			 String dtltype, String type, String version) {
try {
logger.info("Generating Excel for BRRS_CPR_STRUCT_LIQ ARCHIVAL Details...");
System.out.println("came to Detail download service");
if (type.equals("ARCHIVAL") & version != null) {

}
XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("CPR_STRUCT_LIQDetail");

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
balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
balanceStyle.setBorderTop(border);
balanceStyle.setBorderBottom(border);
balanceStyle.setBorderLeft(border);
balanceStyle.setBorderRight(border);


// Header row
String[] headers = {
"CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABLE", "REPORT ADDL CRITERIA", "REPORT_DATE"
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
List<CPR_STRUCT_LIQ_Archival_Detail_Entity> reportData = BRRS_CPR_STRUCT_LIQ_Archival_Detail_Repo.getdatabydateList(parsedToDate,version);

if (reportData != null && !reportData.isEmpty()) {
int rowIndex = 1;
for (CPR_STRUCT_LIQ_Archival_Detail_Entity item : reportData) {
XSSFRow row = sheet.createRow(rowIndex++);

row.createCell(0).setCellValue(item.getCustId());
row.createCell(1).setCellValue(item.getAcctNumber());
row.createCell(2).setCellValue(item.getAcctName());

// ACCT BALANCE (right aligned, 3 decimal places with comma separator)
Cell balanceCell = row.createCell(3);

if (item.getAcctBalanceInpula() != null) {
balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
} else {
balanceCell.setCellValue(0);
}

// Create style with thousand separator and decimal point
DataFormat format = workbook.createDataFormat();

// Format: 1,234,567
balanceStyle.setDataFormat(format.getFormat("#,##0"));

// Right alignment (optional)
balanceStyle.setAlignment(HorizontalAlignment.RIGHT);

balanceCell.setCellStyle(balanceStyle);

row.createCell(4).setCellValue(item.getReportLable());
row.createCell(5).setCellValue(item.getReportAddlCriteria1());
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
logger.info("No data found for CPR_STRUCT_LIQ — only header will be written.");
}
// Write to byte[]
ByteArrayOutputStream bos = new ByteArrayOutputStream();
workbook.write(bos);
workbook.close();

logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
return bos.toByteArray();

} catch (Exception e) {
logger.error("Error generating CPR_STRUCT_LIQ Excel", e);
return new byte[0];
}
}

	public byte[] getExcelCPR_STRUCT_LIQARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<CPR_STRUCT_LIQ_Archival_Summary_Entity> dataList = BRRS_CPR_STRUCT_LIQ_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for CPR_STRUCT_LIQ report. Returning empty result.");
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
			// --- End of Style Definitions --
			int startRow = 7;
			
			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

				CPR_STRUCT_LIQ_Archival_Summary_Entity record1 = dataList.get(i);
				System.out.println("rownumber=" + startRow + i);
				Row row = sheet.getRow(startRow + i);
				if (row == null) {
				row = sheet.createRow(startRow + i);
				}

				Cell cell2 = row.createCell(2);
				if (record1.getR8_1_DAY() != null) {
				   cell2.setCellValue(record1.getR8_1_DAY().doubleValue());
				   cell2.setCellStyle(numberStyle);
				} else {
				   cell2.setCellValue("");
				   cell2.setCellStyle(textStyle);
				}

				Cell cell3 = row.createCell(3);
				if (record1.getR8_2TO7_DAYS() != null) {
				   cell3.setCellValue(record1.getR8_2TO7_DAYS().doubleValue());
				   cell3.setCellStyle(numberStyle);
				} else {
				   cell3.setCellValue("");
				   cell3.setCellStyle(textStyle);
				}

				Cell cell4 = row.createCell(4);
				if (record1.getR8_8TO14_DAYS() != null) {
				   cell4.setCellValue(record1.getR8_8TO14_DAYS().doubleValue());
				   cell4.setCellStyle(numberStyle);
				} else {
				   cell4.setCellValue("");
				   cell4.setCellStyle(textStyle);
				}

				Cell cell5 = row.createCell(5);
				if (record1.getR8_15TO30_DAYS() != null) {
				   cell5.setCellValue(record1.getR8_15TO30_DAYS().doubleValue());
				   cell5.setCellStyle(numberStyle);
				} else {
				   cell5.setCellValue("");
				   cell5.setCellStyle(textStyle);
				}

				Cell cell6 = row.createCell(6);
				if (record1.getR8_31DAYS_UPTO_2MONTHS() != null) {
				   cell6.setCellValue(record1.getR8_31DAYS_UPTO_2MONTHS().doubleValue());
				   cell6.setCellStyle(numberStyle);
				} else {
				   cell6.setCellValue("");
				   cell6.setCellStyle(textStyle);
				}

				Cell cell7 = row.createCell(7);
				if (record1.getR8_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				   cell7.setCellValue(record1.getR8_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				   cell7.setCellStyle(numberStyle);
				} else {
				   cell7.setCellValue("");
				   cell7.setCellStyle(textStyle);
				}

				Cell cell8 = row.createCell(8);
				if (record1.getR8_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				   cell8.setCellValue(record1.getR8_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				   cell8.setCellStyle(numberStyle);
				} else {
				   cell8.setCellValue("");
				   cell8.setCellStyle(textStyle);
				}

				Cell cell9 = row.createCell(9);
				if (record1.getR8_OVER_6MONTHS_UPTO_1YEAR() != null) {
				   cell9.setCellValue(record1.getR8_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				   cell9.setCellStyle(numberStyle);
				} else {
				   cell9.setCellValue("");
				   cell9.setCellStyle(textStyle);
				}

				Cell cell10 = row.createCell(10);
				if (record1.getR8_OVER_1YEAR_UPTO_3YEARS() != null) {
				   cell10.setCellValue(record1.getR8_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				   cell10.setCellStyle(numberStyle);
				} else {
				   cell10.setCellValue("");
				   cell10.setCellStyle(textStyle);
				}

				Cell cell11 = row.createCell(11);
				if (record1.getR8_OVER_3YEARS_UPTO_5YEARS() != null) {
				   cell11.setCellValue(record1.getR8_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				   cell11.setCellStyle(numberStyle);
				} else {
				   cell11.setCellValue("");
				   cell2.setCellStyle(textStyle);
				}

				Cell cell12 = row.createCell(12);
				if (record1.getR8_OVER_3YEARS_UPTO_5YEARS() != null) {
				   cell12.setCellValue(record1.getR8_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				   cell12.setCellStyle(numberStyle);
				} else {
				   cell12.setCellValue("");
				   cell12.setCellStyle(textStyle);
				}

				//-------- R9 --------
				row = sheet.getRow(8);
				cell2 = row.createCell(2);
				if (record1.getR9_1_DAY() != null) {
				cell2.setCellValue(record1.getR9_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR9_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR9_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR9_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR9_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR9_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR9_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR9_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR9_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR9_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR9_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR9_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR9_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR9_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR9_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR9_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR9_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR9_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR9_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR9_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR9_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R10 --------
				row = sheet.getRow(9);
				cell2 = row.createCell(2);
				if (record1.getR10_1_DAY() != null) {
				cell2.setCellValue(record1.getR10_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR10_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR10_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR10_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR10_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR10_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR10_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR10_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR10_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR10_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR10_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR10_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR10_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR10_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR10_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR10_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR10_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR10_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR10_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR10_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR10_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R11 --------
				row = sheet.getRow(10);
				cell2 = row.createCell(2);
				if (record1.getR11_1_DAY() != null) {
				cell2.setCellValue(record1.getR11_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR11_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR11_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR11_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR11_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR11_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR11_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR11_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR11_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR11_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR11_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR11_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR11_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR11_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR11_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR11_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR11_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR11_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR11_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR11_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR11_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R12 --------
				row = sheet.getRow(11);
				cell2 = row.createCell(2);
				if (record1.getR12_1_DAY() != null) {
				cell2.setCellValue(record1.getR12_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR12_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR12_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR12_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR12_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR12_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR12_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR12_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR12_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR12_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR12_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR12_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR12_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR12_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR12_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR12_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR12_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR12_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR12_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR12_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR12_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R13 --------
				row = sheet.getRow(12);
				cell2 = row.createCell(2);
				if (record1.getR13_1_DAY() != null) {
				cell2.setCellValue(record1.getR13_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR13_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR13_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR13_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR13_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR13_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR13_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR13_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR13_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR13_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR13_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR13_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR13_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR13_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR13_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR13_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR13_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR13_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR13_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR13_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR13_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R14 --------
				row = sheet.getRow(13);
				cell2 = row.createCell(2);
				if (record1.getR14_1_DAY() != null) {
				cell2.setCellValue(record1.getR14_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR14_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR14_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR14_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR14_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR14_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR14_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR14_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR14_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR14_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR14_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR14_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR14_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR14_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR14_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR14_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR14_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR14_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR14_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR14_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR14_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R15 --------
				row = sheet.getRow(14);
				cell2 = row.createCell(2);
				if (record1.getR15_1_DAY() != null) {
				cell2.setCellValue(record1.getR15_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR15_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR15_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR15_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR15_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR15_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR15_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR15_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR15_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR15_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR15_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR15_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR15_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR15_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR15_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR15_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR15_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR15_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR15_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR15_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR15_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R16 --------
				row = sheet.getRow(15);
				cell2 = row.createCell(2);
				if (record1.getR16_1_DAY() != null) {
				cell2.setCellValue(record1.getR16_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR16_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR16_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR16_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR16_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR16_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR16_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR16_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR16_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR16_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR16_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR16_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR16_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR16_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR16_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR16_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR16_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR16_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR16_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR16_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR16_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R17 --------
				row = sheet.getRow(16);
				cell2 = row.createCell(2);
				if (record1.getR17_1_DAY() != null) {
				cell2.setCellValue(record1.getR17_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR17_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR17_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR17_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR17_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR17_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR17_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR17_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR17_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR17_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR17_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR17_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR17_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR17_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR17_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR17_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR17_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR17_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR17_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR17_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR17_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R18 --------
				row = sheet.getRow(17);
				cell2 = row.createCell(2);
				if (record1.getR18_1_DAY() != null) {
				cell2.setCellValue(record1.getR18_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR18_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR18_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR18_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR18_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR18_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR18_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR18_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR18_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR18_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR18_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR18_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR18_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR18_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR18_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR18_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR18_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR18_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR18_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR18_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR18_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R19 --------
				row = sheet.getRow(18);
				cell2 = row.createCell(2);
				if (record1.getR19_1_DAY() != null) {
				cell2.setCellValue(record1.getR19_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR19_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR19_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR19_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR19_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR19_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR19_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR19_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR19_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR19_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR19_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR19_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR19_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR19_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR19_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR19_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR19_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR19_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR19_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR19_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR19_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R20 --------
				row = sheet.getRow(19);
				cell2 = row.createCell(2);
				if (record1.getR20_1_DAY() != null) {
				cell2.setCellValue(record1.getR20_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR20_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR20_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR20_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR20_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR20_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR20_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR20_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR20_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR20_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR20_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR20_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR20_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR20_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR20_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR20_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR20_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR20_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR20_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR20_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR20_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R21 --------
				row = sheet.getRow(20);
				cell2 = row.createCell(2);
				if (record1.getR21_1_DAY() != null) {
				cell2.setCellValue(record1.getR21_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR21_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR21_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR21_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR21_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR21_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR21_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR21_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR21_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR21_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR21_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR21_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR21_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR21_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR21_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR21_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR21_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR21_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR21_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR21_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR21_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R22 --------
				row = sheet.getRow(21);
				cell2 = row.createCell(2);
				if (record1.getR22_1_DAY() != null) {
				cell2.setCellValue(record1.getR22_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR22_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR22_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR22_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR22_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR22_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR22_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR22_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR22_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR22_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR22_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR22_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR22_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR22_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR22_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR22_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR22_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR22_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR22_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR22_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR22_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R23 --------
				row = sheet.getRow(22);
				cell2 = row.createCell(2);
				if (record1.getR23_1_DAY() != null) {
				cell2.setCellValue(record1.getR23_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR23_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR23_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR23_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR23_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR23_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR23_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR23_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR23_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR23_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR23_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR23_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR23_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR23_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR23_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR23_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR23_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR23_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR23_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR23_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR23_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R24 --------
				row = sheet.getRow(23);
				cell2 = row.createCell(2);
				if (record1.getR24_1_DAY() != null) {
				cell2.setCellValue(record1.getR24_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR24_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR24_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR24_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR24_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR24_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR24_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR24_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR24_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR24_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR24_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR24_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR24_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR24_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR24_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR24_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR24_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR24_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR24_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR24_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR24_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R25 --------
				row = sheet.getRow(24);
				cell2 = row.createCell(2);
				if (record1.getR25_1_DAY() != null) {
				cell2.setCellValue(record1.getR25_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR25_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR25_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR25_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR25_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR25_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR25_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR25_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR25_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR25_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR25_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR25_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR25_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR25_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR25_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR25_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR25_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR25_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR25_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR25_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR25_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R26 --------
				row = sheet.getRow(25);
				cell2 = row.createCell(2);
				if (record1.getR26_1_DAY() != null) {
				cell2.setCellValue(record1.getR26_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR26_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR26_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR26_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR26_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR26_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR26_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR26_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR26_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR26_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR26_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR26_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR26_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR26_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR26_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR26_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR26_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR26_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR26_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR26_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR26_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R27 --------
				row = sheet.getRow(26);
				cell2 = row.createCell(2);
				if (record1.getR27_1_DAY() != null) {
				cell2.setCellValue(record1.getR27_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR27_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR27_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR27_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR27_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR27_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR27_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR27_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR27_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR27_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR27_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR27_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR27_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR27_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR27_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR27_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR27_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR27_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR27_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR27_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR27_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R28 --------
				row = sheet.getRow(27);
				cell2 = row.createCell(2);
				if (record1.getR28_1_DAY() != null) {
				cell2.setCellValue(record1.getR28_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR28_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR28_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR28_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR28_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR28_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR28_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR28_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR28_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR28_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR28_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR28_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR28_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR28_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR28_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR28_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR28_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR28_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR28_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR28_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR28_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R29 --------
				row = sheet.getRow(28);
				cell2 = row.createCell(2);
				if (record1.getR29_1_DAY() != null) {
				cell2.setCellValue(record1.getR29_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR29_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR29_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR29_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR29_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR29_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR29_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR29_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR29_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR29_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR29_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR29_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR29_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR29_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR29_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR29_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR29_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR29_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR29_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR29_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR29_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R30 --------
				row = sheet.getRow(29);
				cell2 = row.createCell(2);
				if (record1.getR30_1_DAY() != null) {
				cell2.setCellValue(record1.getR30_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR30_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR30_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR30_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR30_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR30_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR30_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR30_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR30_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR30_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR30_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR30_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR30_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR30_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR30_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR30_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR30_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR30_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR30_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR30_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR30_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R31 --------
				row = sheet.getRow(30);
				cell2 = row.createCell(2);
				if (record1.getR31_1_DAY() != null) {
				cell2.setCellValue(record1.getR31_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR31_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR31_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR31_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR31_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR31_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR31_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR31_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR31_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR31_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR31_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR31_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR31_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR31_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR31_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR31_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR31_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR31_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR31_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR31_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR31_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R32 --------
				row = sheet.getRow(31);
				cell2 = row.createCell(2);
				if (record1.getR32_1_DAY() != null) {
				cell2.setCellValue(record1.getR32_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR32_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR32_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR32_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR32_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR32_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR32_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR32_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR32_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR32_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR32_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR32_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR32_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR32_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR32_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR32_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR32_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR32_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR32_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR32_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR32_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R33 --------
				row = sheet.getRow(32);
				cell2 = row.createCell(2);
				if (record1.getR33_1_DAY() != null) {
				cell2.setCellValue(record1.getR33_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR33_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR33_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR33_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR33_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR33_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR33_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR33_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR33_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR33_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR33_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR33_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR33_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR33_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR33_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR33_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR33_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR33_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR33_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR33_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR33_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R34 --------
				row = sheet.getRow(33);
				cell2 = row.createCell(2);
				if (record1.getR34_1_DAY() != null) {
				cell2.setCellValue(record1.getR34_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR34_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR34_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR34_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR34_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR34_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR34_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR34_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR34_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR34_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR34_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR34_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR34_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR34_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR34_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR34_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR34_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR34_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR34_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR34_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR34_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R35 --------
				row = sheet.getRow(34);
				cell2 = row.createCell(2);
				if (record1.getR35_1_DAY() != null) {
				cell2.setCellValue(record1.getR35_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR35_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR35_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR35_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR35_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR35_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR35_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR35_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR35_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR35_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR35_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR35_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR35_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR35_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR35_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR35_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR35_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR35_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR35_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR35_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR35_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R36 --------
				row = sheet.getRow(35);
				cell2 = row.createCell(2);
				if (record1.getR36_1_DAY() != null) {
				cell2.setCellValue(record1.getR36_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR36_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR36_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR36_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR36_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR36_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR36_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR36_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR36_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR36_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR36_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR36_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR36_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR36_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR36_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR36_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR36_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR36_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR36_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR36_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR36_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}

				//-------- R46 --------
				row = sheet.getRow(45);
				cell2 = row.createCell(2);
				if (record1.getR46_1_DAY() != null) {
				cell2.setCellValue(record1.getR46_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR46_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR46_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR46_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR46_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR46_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR46_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR46_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR46_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR46_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR46_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR46_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR46_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR46_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR46_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR46_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR46_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR46_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR46_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR46_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR46_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R47 --------
				row = sheet.getRow(46);
				cell2 = row.createCell(2);
				if (record1.getR47_1_DAY() != null) {
				cell2.setCellValue(record1.getR47_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR47_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR47_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR47_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR47_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR47_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR47_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR47_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR47_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR47_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR47_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR47_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR47_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR47_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR47_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR47_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR47_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR47_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR47_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR47_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR47_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R48 --------
				row = sheet.getRow(47);
				cell2 = row.createCell(2);
				if (record1.getR48_1_DAY() != null) {
				cell2.setCellValue(record1.getR48_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR48_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR48_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR48_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR48_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR48_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR48_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR48_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR48_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR48_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR48_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR48_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR48_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR48_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR48_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR48_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR48_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR48_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR48_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR48_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR48_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R49 --------
				row = sheet.getRow(48);
				cell2 = row.createCell(2);
				if (record1.getR49_1_DAY() != null) {
				cell2.setCellValue(record1.getR49_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR49_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR49_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR49_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR49_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR49_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR49_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR49_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR49_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR49_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR49_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR49_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR49_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR49_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR49_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR49_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR49_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR49_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR49_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR49_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR49_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R50 --------
				row = sheet.getRow(49);
				cell2 = row.createCell(2);
				if (record1.getR50_1_DAY() != null) {
				cell2.setCellValue(record1.getR50_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR50_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR50_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR50_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR50_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR50_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR50_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR50_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR50_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR50_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR50_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR50_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR50_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR50_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR50_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR50_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR50_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR50_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR50_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR50_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR50_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R51 --------
				row = sheet.getRow(50);
				cell2 = row.createCell(2);
				if (record1.getR51_1_DAY() != null) {
				cell2.setCellValue(record1.getR51_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR51_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR51_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR51_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR51_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR51_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR51_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR51_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR51_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR51_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR51_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR51_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR51_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR51_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR51_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR51_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR51_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR51_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR51_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR51_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR51_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R52 --------
				row = sheet.getRow(51);
				cell2 = row.createCell(2);
				if (record1.getR52_1_DAY() != null) {
				cell2.setCellValue(record1.getR52_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR52_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR52_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR52_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR52_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR52_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR52_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR52_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR52_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR52_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR52_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR52_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR52_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR52_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR52_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR52_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR52_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR52_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR52_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR52_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR52_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R53 --------
				row = sheet.getRow(52);
				cell2 = row.createCell(2);
				if (record1.getR53_1_DAY() != null) {
				cell2.setCellValue(record1.getR53_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR53_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR53_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR53_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR53_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR53_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR53_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR53_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR53_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR53_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR53_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR53_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR53_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR53_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR53_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR53_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR53_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR53_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR53_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR53_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR53_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R54 --------
				row = sheet.getRow(53);
				cell2 = row.createCell(2);
				if (record1.getR54_1_DAY() != null) {
				cell2.setCellValue(record1.getR54_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR54_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR54_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR54_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR54_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR54_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR54_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR54_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR54_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR54_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR54_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR54_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR54_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR54_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR54_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR54_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR54_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR54_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR54_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR54_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR54_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R55 --------
				row = sheet.getRow(54);
				cell2 = row.createCell(2);
				if (record1.getR55_1_DAY() != null) {
				cell2.setCellValue(record1.getR55_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR55_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR55_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR55_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR55_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR55_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR55_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR55_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR55_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR55_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR55_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR55_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR55_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR55_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR55_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR55_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR55_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR55_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR55_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR55_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR55_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R56 --------
				row = sheet.getRow(55);
				cell2 = row.createCell(2);
				if (record1.getR56_1_DAY() != null) {
				cell2.setCellValue(record1.getR56_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR56_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR56_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR56_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR56_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR56_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR56_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR56_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR56_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR56_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR56_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR56_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR56_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR56_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR56_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR56_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR56_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR56_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR56_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR56_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR56_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R57 --------
				row = sheet.getRow(56);
				cell2 = row.createCell(2);
				if (record1.getR57_1_DAY() != null) {
				cell2.setCellValue(record1.getR57_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR57_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR57_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR57_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR57_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR57_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR57_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR57_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR57_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR57_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR57_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR57_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR57_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR57_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR57_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR57_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR57_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR57_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR57_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR57_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR57_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R58 --------
				row = sheet.getRow(57);
				cell2 = row.createCell(2);
				if (record1.getR58_1_DAY() != null) {
				cell2.setCellValue(record1.getR58_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR58_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR58_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR58_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR58_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR58_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR58_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR58_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR58_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR58_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR58_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR58_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR58_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR58_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR58_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR58_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR58_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR58_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR58_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR58_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR58_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R59 --------
				row = sheet.getRow(58);
				cell2 = row.createCell(2);
				if (record1.getR59_1_DAY() != null) {
				cell2.setCellValue(record1.getR59_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR59_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR59_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR59_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR59_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR59_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR59_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR59_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR59_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR59_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR59_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR59_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR59_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR59_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR59_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR59_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR59_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR59_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR59_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR59_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR59_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R60 --------
				row = sheet.getRow(59);
				cell2 = row.createCell(2);
				if (record1.getR60_1_DAY() != null) {
				cell2.setCellValue(record1.getR60_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR60_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR60_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR60_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR60_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR60_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR60_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR60_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR60_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR60_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR60_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR60_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR60_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR60_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR60_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR60_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR60_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR60_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR60_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR60_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR60_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R61 --------
				row = sheet.getRow(60);
				cell2 = row.createCell(2);
				if (record1.getR61_1_DAY() != null) {
				cell2.setCellValue(record1.getR61_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR61_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR61_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR61_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR61_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR61_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR61_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR61_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR61_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR61_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR61_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR61_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR61_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR61_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR61_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR61_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR61_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR61_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR61_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR61_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR61_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R62 --------
				row = sheet.getRow(61);
				cell2 = row.createCell(2);
				if (record1.getR62_1_DAY() != null) {
				cell2.setCellValue(record1.getR62_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR62_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR62_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR62_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR62_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR62_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR62_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR62_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR62_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR62_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR62_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR62_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR62_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR62_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR62_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR62_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR62_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR62_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR62_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR62_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR62_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R63 --------
				row = sheet.getRow(62);
				cell2 = row.createCell(2);
				if (record1.getR63_1_DAY() != null) {
				cell2.setCellValue(record1.getR63_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR63_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR63_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR63_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR63_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR63_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR63_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR63_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR63_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR63_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR63_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR63_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR63_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR63_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR63_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR63_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR63_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR63_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR63_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR63_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR63_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R64 --------
				row = sheet.getRow(63);
				cell2 = row.createCell(2);
				if (record1.getR64_1_DAY() != null) {
				cell2.setCellValue(record1.getR64_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR64_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR64_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR64_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR64_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR64_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR64_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR64_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR64_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR64_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR64_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR64_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR64_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR64_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR64_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR64_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR64_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR64_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR64_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR64_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR64_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R65 --------
				row = sheet.getRow(64);
				cell2 = row.createCell(2);
				if (record1.getR65_1_DAY() != null) {
				cell2.setCellValue(record1.getR65_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR65_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR65_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR65_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR65_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR65_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR65_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR65_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR65_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR65_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR65_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR65_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR65_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR65_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR65_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR65_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR65_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR65_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR65_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR65_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR65_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R66 --------
				row = sheet.getRow(65);
				cell2 = row.createCell(2);
				if (record1.getR66_1_DAY() != null) {
				cell2.setCellValue(record1.getR66_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR66_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR66_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR66_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR66_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR66_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR66_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR66_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR66_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR66_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR66_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR66_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR66_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR66_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR66_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR66_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR66_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR66_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR66_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR66_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR66_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R67 --------
				row = sheet.getRow(66);
				cell2 = row.createCell(2);
				if (record1.getR67_1_DAY() != null) {
				cell2.setCellValue(record1.getR67_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR67_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR67_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR67_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR67_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR67_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR67_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR67_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR67_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR67_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR67_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR67_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR67_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR67_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR67_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR67_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR67_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR67_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR67_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR67_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR67_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R68 --------
				row = sheet.getRow(67);
				cell2 = row.createCell(2);
				if (record1.getR68_1_DAY() != null) {
				cell2.setCellValue(record1.getR68_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR68_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR68_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR68_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR68_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR68_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR68_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR68_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR68_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR68_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR68_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR68_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR68_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR68_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR68_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR68_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR68_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR68_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR68_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR68_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR68_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R69 --------
				row = sheet.getRow(68);
				cell2 = row.createCell(2);
				if (record1.getR69_1_DAY() != null) {
				cell2.setCellValue(record1.getR69_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR69_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR69_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR69_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR69_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR69_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR69_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR69_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR69_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR69_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR69_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR69_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR69_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR69_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR69_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR69_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR69_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR69_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR69_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR69_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR69_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R70 --------
				row = sheet.getRow(69);
				cell2 = row.createCell(2);
				if (record1.getR70_1_DAY() != null) {
				cell2.setCellValue(record1.getR70_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR70_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR70_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR70_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR70_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR70_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR70_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR70_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR70_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR70_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR70_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR70_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR70_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR70_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR70_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR70_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR70_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR70_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR70_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR70_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR70_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R71 --------
				row = sheet.getRow(70);
				cell2 = row.createCell(2);
				if (record1.getR71_1_DAY() != null) {
				cell2.setCellValue(record1.getR71_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR71_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR71_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR71_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR71_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR71_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR71_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR71_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR71_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR71_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR71_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR71_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR71_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR71_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR71_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR71_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR71_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR71_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR71_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR71_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR71_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}


				//-------- R72 --------
				row = sheet.getRow(71);
				cell2 = row.createCell(2);
				if (record1.getR72_1_DAY() != null) {
				cell2.setCellValue(record1.getR72_1_DAY().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR72_2TO7_DAYS() != null) {
				cell3.setCellValue(record1.getR72_2TO7_DAYS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR72_8TO14_DAYS() != null) {
				cell4.setCellValue(record1.getR72_8TO14_DAYS().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR72_15TO30_DAYS() != null) {
				cell5.setCellValue(record1.getR72_15TO30_DAYS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR72_31DAYS_UPTO_2MONTHS() != null) {
				cell6.setCellValue(record1.getR72_31DAYS_UPTO_2MONTHS().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record1.getR72_MORETHAN_2MONTHS_UPTO_3MONHTS() != null) {
				cell7.setCellValue(record1.getR72_MORETHAN_2MONTHS_UPTO_3MONHTS().doubleValue());
				cell7.setCellStyle(numberStyle);
				} else {
				cell7.setCellValue("");
				cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record1.getR72_OVER_3MONTHS_UPTO_6MONTHS() != null) {
				cell8.setCellValue(record1.getR72_OVER_3MONTHS_UPTO_6MONTHS().doubleValue());
				cell8.setCellStyle(numberStyle);
				} else {
				cell8.setCellValue("");
				cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record1.getR72_OVER_6MONTHS_UPTO_1YEAR() != null) {
				cell9.setCellValue(record1.getR72_OVER_6MONTHS_UPTO_1YEAR().doubleValue());
				cell9.setCellStyle(numberStyle);
				} else {
				cell9.setCellValue("");
				cell9.setCellStyle(textStyle);
				}

				cell10 = row.createCell(10);
				if (record1.getR72_OVER_1YEAR_UPTO_3YEARS() != null) {
				cell10.setCellValue(record1.getR72_OVER_1YEAR_UPTO_3YEARS().doubleValue());
				cell10.setCellStyle(numberStyle);
				} else {
				cell10.setCellValue("");
				cell10.setCellStyle(textStyle);
				}

				cell11 = row.createCell(11);
				if (record1.getR72_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell11.setCellValue(record1.getR72_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell11.setCellStyle(numberStyle);
				} else {
				cell11.setCellValue("");
				cell11.setCellStyle(textStyle);
				}

				cell12 = row.createCell(12);
				if (record1.getR72_OVER_3YEARS_UPTO_5YEARS() != null) {
				cell12.setCellValue(record1.getR72_OVER_3YEARS_UPTO_5YEARS().doubleValue());
				cell12.setCellStyle(numberStyle);
				} else {
				cell12.setCellValue("");
				cell12.setCellStyle(textStyle);
				}
				}
				workbook.setForceFormulaRecalculation(true);
				} else {

				}
				// Write the final workbook content to the in-memory stream.
				workbook.write(out);
				logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
				return out.toByteArray();
				}
				}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/CPR_STRUCT_LIQ"); // ✅ match the report name
	    System.out.println("Hello");
	    if (acctNo != null) {
	    	CPR_STRUCT_LIQ_Detail_Entity fsiEntity = BRRS_CPR_STRUCT_LIQ_Detail_Repo.findByAcctnumber(acctNo);
	        if (fsiEntity != null && fsiEntity.getReportDate() != null) {
	            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(fsiEntity.getReportDate());
	            mv.addObject("asondate", formattedDate);
	        }
	        mv.addObject("Data", fsiEntity);
	    }

	    mv.addObject("displaymode", "edit");
	    mv.addObject("formmode", formMode != null ? formMode : "edit");
	    return mv;
	}





	public ModelAndView updateDetailEdit(String acctNo, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/CPR_STRUCT_LIQ"); // ✅ match the report name

	    if (acctNo != null) {
	        CPR_STRUCT_LIQ_Detail_Entity la1Entity = BRRS_CPR_STRUCT_LIQ_Detail_Repo.findByAcctnumber(acctNo);
	        if (la1Entity != null && la1Entity.getReportDate() != null) {
	            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(la1Entity.getReportDate());
	            mv.addObject("asondate", formattedDate);
	            System.out.println(formattedDate);
	        }
	        mv.addObject("Data", la1Entity);
	    }

	    mv.addObject("displaymode", "edit");
	    mv.addObject("formmode", formMode != null ? formMode : "edit");
	    return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
	    try {
	        String acctNo = request.getParameter("acctNumber");
	        String provisionStr = request.getParameter("acctBalanceInpula");
	        String acctName = request.getParameter("acctName");
	        String reportDateStr = request.getParameter("reportDate");

	        logger.info("Received update for ACCT_NO: {}", acctNo);

	        CPR_STRUCT_LIQ_Detail_Entity existing = BRRS_CPR_STRUCT_LIQ_Detail_Repo.findByAcctnumber(acctNo);
	        if (existing == null) {
	            logger.warn("No record found for ACCT_NO: {}", acctNo);
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
	        }

	        boolean isChanged = false;

	        if (acctName != null && !acctName.isEmpty()) {
	            if (existing.getAcctName() == null || !existing.getAcctName().equals(acctName)) {
	                existing.setAcctName(acctName);
	                isChanged = true;
	                logger.info("Account name updated to {}", acctName);
	            }
	        }

	        if (provisionStr != null && !provisionStr.isEmpty()) {
	            BigDecimal newProvision = new BigDecimal(provisionStr);
	            if (existing.getAcctBalanceInpula() == null ||
	                existing.getAcctBalanceInpula().compareTo(newProvision) != 0) {
	                existing.setAcctBalanceInpula(newProvision);
	                isChanged = true;
	                logger.info("Balance updated to {}", newProvision);
	            }
	        }
	        
	        

	        if (isChanged) {
	        	BRRS_CPR_STRUCT_LIQ_Detail_Repo.save(existing);
	            logger.info("Record updated successfully for account {}", acctNo);

	            // Format date for procedure
	            String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
	                    .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

	            // Run summary procedure after commit
	            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
	                @Override
	                public void afterCommit() {
	                    try {
	                        logger.info("Transaction committed — calling BRRS_CPR_STRUCT_LIQ_SUMMARY_PROCEDURE({})",
	                                formattedDate);
	                        jdbcTemplate.update("BEGIN BRRS_CPR_STRUCT_LIQ_SUMMARY_PROCEDURE(?); END;", formattedDate);
	                        logger.info("Procedure executed successfully after commit.");
	                    } catch (Exception e) {
	                        logger.error("Error executing procedure after commit", e);
	                    }
	                }
	            });

	            return ResponseEntity.ok("Record updated successfully!");
	        } else {
	            logger.info("No changes detected for ACCT_NO: {}", acctNo);
	            return ResponseEntity.ok("No changes were made.");
	        }

	    } catch (Exception e) {
	        logger.error("Error updating CPR_STRUCT_LIQ record", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error updating record: " + e.getMessage());
	    }
	}
	
	
	
	
}