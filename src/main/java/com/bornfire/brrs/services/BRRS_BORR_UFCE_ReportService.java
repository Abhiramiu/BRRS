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

import com.bornfire.brrs.entities.ADISB2_Archival_Summary_Entity;
import com.bornfire.brrs.entities.ADISB2_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_BORR_UFCE_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_BORR_UFCE_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_BORR_UFCE_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_BORR_UFCE_Summary_Repo;
import com.bornfire.brrs.entities.BORR_UFCE_Archival_Detail_Entity;
import com.bornfire.brrs.entities.BORR_UFCE_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BORR_UFCE_Detail_Entity;
import com.bornfire.brrs.entities.BORR_UFCE_Summary_Entity;
//import com.bornfire.brrs.entities.BORR_UFCE_Manual_Summary_Entity;
//import com.bornfire.brrs.entities.BORR_UFCE_Manual_Archival_Summary_Entity;
//import com.bornfire.brrs.entities.BRRS_BORR_UFCE_Manual_Summary_Repo;
//import com.bornfire.brrs.entities.BRRS_BORR_UFCE_Manual_Archival_Summary_Repo;

@Component
@Service
public class BRRS_BORR_UFCE_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_BORR_UFCE_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_BORR_UFCE_Detail_Repo BRRS_BORR_UFCE_Detail_Repo;

	@Autowired
	BRRS_BORR_UFCE_Summary_Repo BRRS_BORR_UFCE_Summary_Repo;

//	@Autowired
//	BRRS_BORR_UFCE_Manual_Summary_Repo BRRS_BORR_UFCE_Manual_Summary_Repo;
//

	@Autowired
	BRRS_BORR_UFCE_Archival_Detail_Repo BRRS_BORR_UFCE_Archival_Detail_Repo;

	@Autowired
	BRRS_BORR_UFCE_Archival_Summary_Repo BRRS_BORR_UFCE_Archival_Summary_Repo;

//	@Autowired
//	BRRS_BORR_UFCE_Manual_Archival_Summary_Repo BRRS_BORR_UFCE_Manual_Archival_Summary_Repo;
//


	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBORR_UFCEView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type,  String version) {
		ModelAndView mv = new ModelAndView();
//		Session hs = sessionFactory.getCurrentSession();
//		int pageSize = pageable.getPageSize();
//		int currentPage = pageable.getPageNumber();
//		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<BORR_UFCE_Archival_Summary_Entity> T1Master = new ArrayList<BORR_UFCE_Archival_Summary_Entity>();
//			List<BORR_UFCE_Manual_Archival_Summary_Entity> T2Master = new ArrayList<BORR_UFCE_Manual_Archival_Summary_Entity>();

			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_BORR_UFCE_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
//				T2Master = BRRS_BORR_UFCE_Manual_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
				

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
//			mv.addObject("reportsummary1", T2Master);
            
		} else {
			List<BORR_UFCE_Summary_Entity> T1Master = new ArrayList<BORR_UFCE_Summary_Entity>();
//			List<BORR_UFCE_Manual_Summary_Entity> T2Master = new ArrayList<BORR_UFCE_Manual_Summary_Entity>();
			
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_BORR_UFCE_Summary_Repo.getdatabydateList(dateformat.parse(todate));
//				T2Master = BRRS_BORR_UFCE_Manual_Summary_Repo.getdatabydateList(dateformat.parse(todate));
				

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
//			mv.addObject("reportsummary1", T2Master);
            
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/BORR_UFCE");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

//	public ModelAndView getBORR_UFCEcurrentDtl(String reportId, String fromdate, String todate, String currency,
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
//			List<BORR_UFCE_Archival_Detail_Entity> T1Dt1;
//			if (rowId != null && columnId != null) {
//				T1Dt1 = BRRS_BORR_UFCE_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
//			} else {
//				T1Dt1 = BRRS_BORR_UFCE_Archival_Detail_Repo.getdatabydateList(parsedDate, version);					
//			}
//
//			mv.addObject("reportdetails", T1Dt1);
//			mv.addObject("reportmaster12", T1Dt1);
//			System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
//
//		} else {
//			// 🔹 Current branch
//			List<BORR_UFCE_Detail_Entity> T1Dt1;
//			if (rowId != null && columnId != null) {
//				T1Dt1 = BRRS_BORR_UFCE_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
//			} else {
//				T1Dt1 = BRRS_BORR_UFCE_Detail_Repo.getdatabydateList(parsedDate);
//				totalPages = BRRS_BORR_UFCE_Detail_Repo.getdatacount(parsedDate);
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
//	mv.setViewName("BRRS/BORR_UFCE");
//	mv.addObject("displaymode", "Details");
//	mv.addObject("currentPage", currentPage);
//	System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
//	mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
//	mv.addObject("reportsflag", "reportsflag");
//	mv.addObject("menu", reportId);
//
//	return mv;
//}

	public ModelAndView getBORR_UFCEcurrentDtl(String reportId, String fromdate, String todate, String currency,
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
			List<BORR_UFCE_Archival_Detail_Entity> T1Dt1;
			if (rowId != null && columnId != null) {
				T1Dt1 = BRRS_BORR_UFCE_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
			} else {
				T1Dt1 = BRRS_BORR_UFCE_Archival_Detail_Repo.getdatabydateList(parsedDate, version);					
			}

			mv.addObject("reportdetails", T1Dt1);
			mv.addObject("reportmaster12", T1Dt1);
			System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

		} else {
			// 🔹 Current branch
			List<BORR_UFCE_Detail_Entity> T1Dt1;
			if (rowId != null && columnId != null) {
				T1Dt1 = BRRS_BORR_UFCE_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
			} else {
				T1Dt1 = BRRS_BORR_UFCE_Detail_Repo.getdatabydateList(parsedDate);
				totalPages = BRRS_BORR_UFCE_Detail_Repo.getdatacount(parsedDate);
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
	mv.setViewName("BRRS/BORR_UFCE");
	mv.addObject("displaymode", "Details");
	mv.addObject("currentPage", currentPage);
	System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
	mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
	mv.addObject("reportsflag", "reportsflag");
	mv.addObject("menu", reportId);

	return mv;
}

	
//	public void updateReport(BORR_UFCE_Manual_Summary_Entity updatedEntity) {
//	    System.out.println("Came to services1");
//	    System.out.println("Report Date: " + updatedEntity.getReport_date());
//
//	    BORR_UFCE_Manual_Summary_Entity existing = BRRS_BORR_UFCE_Manual_Summary_Repo.findById(updatedEntity.getReport_date())
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
//	                    Method getter = BORR_UFCE_Manual_Summary_Entity.class.getMethod(getterName);
//	                    Method setter = BORR_UFCE_Manual_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
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
//	        BRRS_BORR_UFCE_Manual_Summary_Repo.save(existing);
//
//	    } catch (Exception e) {
//	        throw new RuntimeException("Error while updating report fields", e);
//	    }
//	}

	
	
	
	public byte[] getBORR_UFCEDetailExcel(String filename, String fromdate, String todate, String currency,
			   String dtltype, String type, String version) {
try {
logger.info("Generating Excel for BORR_UFCE Details...");
System.out.println("came to Detail download service");


if (type.equals("ARCHIVAL") & version != null) {
byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
version);
return ARCHIVALreport;
}

XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("BORR_UFCEDetail");

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
List<BORR_UFCE_Detail_Entity> reportData = BRRS_BORR_UFCE_Detail_Repo.getdatabydateList(parsedToDate);

if (reportData != null && !reportData.isEmpty()) {
int rowIndex = 1;
for (BORR_UFCE_Detail_Entity item : reportData) {
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
row.createCell(4).setCellValue(item.getSchmCode());
row.createCell(5).setCellValue(item.getSchmDesc());
row.createCell(6)
.setCellValue(item.getAcctOpnDate() != null
		? new SimpleDateFormat("dd-MM-yyyy").format(item.getAcctOpnDate())
		: "");
row.createCell(7).setCellValue(item.getCcy());
Cell sanctionCell = row.createCell(8);
if (item.getSanctionAmount() != null) {
	sanctionCell.setCellValue(item.getSanctionAmount().doubleValue());
} else {
	sanctionCell.setCellValue(0);
}
Cell intRateCell = row.createCell(9);
if (item.getIntRate() != null) {
	intRateCell.setCellValue(item.getIntRate().doubleValue());
} else {
	intRateCell.setCellValue(0);
}

		row.createCell(10).setCellValue(item.getReportLable());
		row.createCell(11).setCellValue(item.getReportAddlCriteria1());
		row.createCell(12)
				.setCellValue(item.getReportDate() != null
						? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
						: "");

		// Apply data style for all other cells
		for (int j = 0; j < 13; j++) {
			if (j != 3) {
				row.getCell(j).setCellStyle(dataStyle);
			}
		}
	}
} else {
	logger.info("No data found for BORR_UFCE — only header will be written.");
}

//Write to byte[]
ByteArrayOutputStream bos = new ByteArrayOutputStream();
workbook.write(bos);
workbook.close();

logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
return bos.toByteArray();

} catch (Exception e) {
logger.error("Error generating BORR_UFCE Excel", e);
return new byte[0];
}
}

	public byte[] getBORR_UFCEExcel(String filename, String reportId, String fromdate, String todate, String currency,
			 String dtltype, String type, String version) throws Exception {
logger.info("Service: Starting Excel generation process in memory.");
logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

//Convert string to Date
Date reportDate = dateformat.parse(todate);

//ARCHIVAL check
if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
logger.info("Service: Generating ARCHIVAL report for version {}", version);
return getExcelBORR_UFCEARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
}
//RESUB check
else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
logger.info("Service: Generating RESUB report for version {}", version);


List<BORR_UFCE_Archival_Summary_Entity> T1Master =
BRRS_BORR_UFCE_Archival_Summary_Repo.getdatabydateListarchival(reportDate, version);


}


//Default (LIVE) case
List<BORR_UFCE_Summary_Entity> dataList1 = BRRS_BORR_UFCE_Summary_Repo.getdatabydateList(reportDate);

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

int startRow = 3;

if (!dataList1.isEmpty()) {
for (int i = 0; i < dataList1.size(); i++) {

BORR_UFCE_Summary_Entity record = dataList1.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}


Cell cell1 = row.createCell(0);
if (record.getR4_CUST_ID() != null) {
    cell1.setCellValue(record.getR4_CUST_ID());
    cell1.setCellStyle(textStyle);
} else {
    cell1.setCellValue("");
    cell1.setCellStyle(textStyle);
}


Cell cell2 = row.createCell(1);
if (record.getR4_ACCT_NO() != null) {
    cell2.setCellValue(record.getR4_ACCT_NO().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}
Cell cell3 = row.createCell(2);
if (record.getR4_ACCT_NAME() != null) {
    cell3.setCellValue(record.getR4_ACCT_NAME());
    cell3.setCellStyle(textStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}
Cell cell4 = row.createCell(3);
if (record.getR4_SCHM_CODE() != null) {
    cell4.setCellValue(record.getR4_SCHM_CODE());
    cell4.setCellStyle(textStyle);
} else {
    cell4.setCellValue("");
    cell4.setCellStyle(textStyle);
}
Cell cell5 = row.createCell(4);
if (record.getR4_SCHM_DESC() != null) {
    cell5.setCellValue(record.getR4_SCHM_DESC());
    cell5.setCellStyle(textStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}
Cell cell6 = row.createCell(5);
if (record.getR4_ACCT_OPN_DATE() != null) {
    cell6.setCellValue(record.getR4_ACCT_OPN_DATE());
    cell6.setCellStyle(dateStyle);
} else {
    cell6.setCellValue("");
    cell6.setCellStyle(textStyle);
}
Cell cell7 = row.createCell(6);
if (record.getR4_CCY() != null) {
    cell7.setCellValue(record.getR4_CCY());
    cell7.setCellStyle(textStyle);
} else {
    cell7.setCellValue("");
    cell7.setCellStyle(textStyle);
}
Cell cell8 = row.createCell(7);
if (record.getR4_BAL_EQUI_TO_BWP() != null) {
    cell8.setCellValue(record.getR4_BAL_EQUI_TO_BWP().doubleValue());
    cell8.setCellStyle(numberStyle);
} else {
    cell8.setCellValue("");
    cell8.setCellStyle(textStyle);
}
Cell cell9 = row.createCell(8);
if (record.getR4_SANCTION_AMT_BWP() != null) {
    cell9.setCellValue(record.getR4_SANCTION_AMT_BWP().doubleValue());
    cell9.setCellStyle(numberStyle);
} else {
    cell9.setCellValue("");
    cell9.setCellStyle(textStyle);
}
Cell cell10 = row.createCell(9);
if (record.getR4_INT_RATE() != null) {
    cell10.setCellValue(record.getR4_INT_RATE().doubleValue());
    cell10.setCellStyle(numberStyle);
} else {
    cell10.setCellValue("");
    cell10.setCellStyle(textStyle);
}
Cell cell12 = row.createCell(11);
if (record.getR4_VALUE_1() != null) {
    cell12.setCellValue(record.getR4_VALUE_1().doubleValue());
    cell12.setCellStyle(numberStyle);
} else {
    cell12.setCellValue("");
    cell12.setCellStyle(textStyle);
}


row = sheet.getRow(4);
cell1 = row.createCell(0);
if (record.getR5_CUST_ID() != null) {
    cell1.setCellValue(record.getR5_CUST_ID());
    cell1.setCellStyle(textStyle);
} else {
    cell1.setCellValue("");
    cell1.setCellStyle(textStyle);
}


cell2 = row.createCell(1);
if (record.getR5_ACCT_NO() != null) {
    cell2.setCellValue(record.getR5_ACCT_NO().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}
cell3 = row.createCell(2);
if (record.getR5_ACCT_NAME() != null) {
    cell3.setCellValue(record.getR5_ACCT_NAME());
    cell3.setCellStyle(textStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}
cell4 = row.createCell(3);
if (record.getR5_SCHM_CODE() != null) {
    cell4.setCellValue(record.getR5_SCHM_CODE());
    cell4.setCellStyle(textStyle);
} else {
    cell4.setCellValue("");
    cell4.setCellStyle(textStyle);
}
cell5 = row.createCell(4);
if (record.getR5_SCHM_DESC() != null) {
    cell5.setCellValue(record.getR5_SCHM_DESC());
    cell5.setCellStyle(textStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}
cell6 = row.createCell(5);
if (record.getR5_ACCT_OPN_DATE() != null) {
    cell6.setCellValue(record.getR5_ACCT_OPN_DATE());
    cell6.setCellStyle(dateStyle);
} else {
    cell6.setCellValue("");
    cell6.setCellStyle(textStyle);
}
cell7 = row.createCell(6);
if (record.getR5_CCY() != null) {
    cell7.setCellValue(record.getR5_CCY());
    cell7.setCellStyle(textStyle);
} else {
    cell7.setCellValue("");
    cell7.setCellStyle(textStyle);
}
cell8 = row.createCell(7);
if (record.getR5_BAL_EQUI_TO_BWP() != null) {
    cell8.setCellValue(record.getR5_BAL_EQUI_TO_BWP().doubleValue());
    cell8.setCellStyle(numberStyle);
} else {
    cell8.setCellValue("");
    cell8.setCellStyle(textStyle);
}
cell9 = row.createCell(8);
if (record.getR5_SANCTION_AMT_BWP() != null) {
    cell9.setCellValue(record.getR5_SANCTION_AMT_BWP().doubleValue());
    cell9.setCellStyle(numberStyle);
} else {
    cell9.setCellValue("");
    cell9.setCellStyle(textStyle);
}
cell10 = row.createCell(9);
if (record.getR5_INT_RATE() != null) {
    cell10.setCellValue(record.getR5_INT_RATE().doubleValue());
    cell10.setCellStyle(numberStyle);
} else {
    cell10.setCellValue("");
    cell10.setCellStyle(textStyle);
}
cell12 = row.createCell(11);
if (record.getR5_VALUE_1() != null) {
    cell12.setCellValue(record.getR5_VALUE_1().doubleValue());
    cell12.setCellStyle(numberStyle);
} else {
    cell12.setCellValue("");
    cell12.setCellStyle(textStyle);
}


row = sheet.getRow(5);
cell1 = row.createCell(0);
if (record.getR6_CUST_ID() != null) {
    cell1.setCellValue(record.getR6_CUST_ID());
    cell1.setCellStyle(textStyle);
} else {
    cell1.setCellValue("");
    cell1.setCellStyle(textStyle);
}


cell2 = row.createCell(1);
if (record.getR6_ACCT_NO() != null) {
    cell2.setCellValue(record.getR6_ACCT_NO().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}
cell3 = row.createCell(2);
if (record.getR6_ACCT_NAME() != null) {
    cell3.setCellValue(record.getR6_ACCT_NAME());
    cell3.setCellStyle(textStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}
cell4 = row.createCell(3);
if (record.getR6_SCHM_CODE() != null) {
    cell4.setCellValue(record.getR6_SCHM_CODE());
    cell4.setCellStyle(textStyle);
} else {
    cell4.setCellValue("");
    cell4.setCellStyle(textStyle);
}
cell5 = row.createCell(4);
if (record.getR6_SCHM_DESC() != null) {
    cell5.setCellValue(record.getR6_SCHM_DESC());
    cell5.setCellStyle(textStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}
cell6 = row.createCell(5);
if (record.getR6_ACCT_OPN_DATE() != null) {
    cell6.setCellValue(record.getR6_ACCT_OPN_DATE());
    cell6.setCellStyle(dateStyle);
} else {
    cell6.setCellValue("");
    cell6.setCellStyle(textStyle);
}
cell7 = row.createCell(6);
if (record.getR6_CCY() != null) {
    cell7.setCellValue(record.getR6_CCY());
    cell7.setCellStyle(textStyle);
} else {
    cell7.setCellValue("");
    cell7.setCellStyle(textStyle);
}
cell8 = row.createCell(7);
if (record.getR6_BAL_EQUI_TO_BWP() != null) {
    cell8.setCellValue(record.getR6_BAL_EQUI_TO_BWP().doubleValue());
    cell8.setCellStyle(numberStyle);
} else {
    cell8.setCellValue("");
    cell8.setCellStyle(textStyle);
}
cell9 = row.createCell(8);
if (record.getR6_SANCTION_AMT_BWP() != null) {
    cell9.setCellValue(record.getR6_SANCTION_AMT_BWP().doubleValue());
    cell9.setCellStyle(numberStyle);
} else {
    cell9.setCellValue("");
    cell9.setCellStyle(textStyle);
}
cell10 = row.createCell(9);
if (record.getR6_INT_RATE() != null) {
    cell10.setCellValue(record.getR6_INT_RATE().doubleValue());
    cell10.setCellStyle(numberStyle);
} else {
    cell10.setCellValue("");
    cell10.setCellStyle(textStyle);
}
cell12 = row.createCell(11);
if (record.getR6_VALUE_1() != null) {
    cell12.setCellValue(record.getR6_VALUE_1().doubleValue());
    cell12.setCellStyle(numberStyle);
} else {
    cell12.setCellValue("");
    cell12.setCellStyle(textStyle);
}


row = sheet.getRow(6);
cell1 = row.createCell(0);
if (record.getR7_CUST_ID() != null) {
    cell1.setCellValue(record.getR7_CUST_ID());
    cell1.setCellStyle(textStyle);
} else {
    cell1.setCellValue("");
    cell1.setCellStyle(textStyle);
}


cell2 = row.createCell(1);
if (record.getR7_ACCT_NO() != null) {
    cell2.setCellValue(record.getR7_ACCT_NO().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}
cell3 = row.createCell(2);
if (record.getR7_ACCT_NAME() != null) {
    cell3.setCellValue(record.getR7_ACCT_NAME());
    cell3.setCellStyle(textStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}
cell4 = row.createCell(3);
if (record.getR7_SCHM_CODE() != null) {
    cell4.setCellValue(record.getR7_SCHM_CODE());
    cell4.setCellStyle(textStyle);
} else {
    cell4.setCellValue("");
    cell4.setCellStyle(textStyle);
}
cell5 = row.createCell(4);
if (record.getR7_SCHM_DESC() != null) {
    cell5.setCellValue(record.getR7_SCHM_DESC());
    cell5.setCellStyle(textStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}
cell6 = row.createCell(5);
if (record.getR7_ACCT_OPN_DATE() != null) {
    cell6.setCellValue(record.getR7_ACCT_OPN_DATE());
    cell6.setCellStyle(dateStyle);
} else {
    cell6.setCellValue("");
    cell6.setCellStyle(textStyle);
}
cell7 = row.createCell(6);
if (record.getR7_CCY() != null) {
    cell7.setCellValue(record.getR7_CCY());
    cell7.setCellStyle(textStyle);
} else {
    cell7.setCellValue("");
    cell7.setCellStyle(textStyle);
}
cell8 = row.createCell(7);
if (record.getR7_BAL_EQUI_TO_BWP() != null) {
    cell8.setCellValue(record.getR7_BAL_EQUI_TO_BWP().doubleValue());
    cell8.setCellStyle(numberStyle);
} else {
    cell8.setCellValue("");
    cell8.setCellStyle(textStyle);
}
cell9 = row.createCell(8);
if (record.getR7_SANCTION_AMT_BWP() != null) {
    cell9.setCellValue(record.getR7_SANCTION_AMT_BWP().doubleValue());
    cell9.setCellStyle(numberStyle);
} else {
    cell9.setCellValue("");
    cell9.setCellStyle(textStyle);
}
cell10 = row.createCell(9);
if (record.getR7_INT_RATE() != null) {
    cell10.setCellValue(record.getR7_INT_RATE().doubleValue());
    cell10.setCellStyle(numberStyle);
} else {
    cell10.setCellValue("");
    cell10.setCellStyle(textStyle);
}
cell12 = row.createCell(11);
if (record.getR7_VALUE_1() != null) {
    cell12.setCellValue(record.getR7_VALUE_1().doubleValue());
    cell12.setCellStyle(numberStyle);
} else {
    cell12.setCellValue("");
    cell12.setCellStyle(textStyle);
}


row = sheet.getRow(7);
cell1 = row.createCell(0);
if (record.getR8_CUST_ID() != null) {
    cell1.setCellValue(record.getR8_CUST_ID());
    cell1.setCellStyle(textStyle);
} else {
    cell1.setCellValue("");
    cell1.setCellStyle(textStyle);
}


cell2 = row.createCell(1);
if (record.getR8_ACCT_NO() != null) {
    cell2.setCellValue(record.getR8_ACCT_NO().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}
cell3 = row.createCell(2);
if (record.getR8_ACCT_NAME() != null) {
    cell3.setCellValue(record.getR8_ACCT_NAME());
    cell3.setCellStyle(textStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}
cell4 = row.createCell(3);
if (record.getR8_SCHM_CODE() != null) {
    cell4.setCellValue(record.getR8_SCHM_CODE());
    cell4.setCellStyle(textStyle);
} else {
    cell4.setCellValue("");
    cell4.setCellStyle(textStyle);
}
cell5 = row.createCell(4);
if (record.getR8_SCHM_DESC() != null) {
    cell5.setCellValue(record.getR8_SCHM_DESC());
    cell5.setCellStyle(textStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}
cell6 = row.createCell(5);
if (record.getR8_ACCT_OPN_DATE() != null) {
    cell6.setCellValue(record.getR8_ACCT_OPN_DATE());
    cell6.setCellStyle(dateStyle);
} else {
    cell6.setCellValue("");
    cell6.setCellStyle(textStyle);
}
cell7 = row.createCell(6);
if (record.getR8_CCY() != null) {
    cell7.setCellValue(record.getR8_CCY());
    cell7.setCellStyle(textStyle);
} else {
    cell7.setCellValue("");
    cell7.setCellStyle(textStyle);
}
cell8 = row.createCell(7);
if (record.getR8_BAL_EQUI_TO_BWP() != null) {
    cell8.setCellValue(record.getR8_BAL_EQUI_TO_BWP().doubleValue());
    cell8.setCellStyle(numberStyle);
} else {
    cell8.setCellValue("");
    cell8.setCellStyle(textStyle);
}
cell9 = row.createCell(8);
if (record.getR8_SANCTION_AMT_BWP() != null) {
    cell9.setCellValue(record.getR8_SANCTION_AMT_BWP().doubleValue());
    cell9.setCellStyle(numberStyle);
} else {
    cell9.setCellValue("");
    cell9.setCellStyle(textStyle);
}
cell10 = row.createCell(9);
if (record.getR8_INT_RATE() != null) {
    cell10.setCellValue(record.getR8_INT_RATE().doubleValue());
    cell10.setCellStyle(numberStyle);
} else {
    cell10.setCellValue("");
    cell10.setCellStyle(textStyle);
}
cell12 = row.createCell(11);
if (record.getR8_VALUE_1() != null) {
    cell12.setCellValue(record.getR8_VALUE_1().doubleValue());
    cell12.setCellStyle(numberStyle);
} else {
    cell12.setCellValue("");
    cell12.setCellStyle(textStyle);
}


row = sheet.getRow(8);
cell1 = row.createCell(0);
if (record.getR9_CUST_ID() != null) {
    cell1.setCellValue(record.getR9_CUST_ID());
    cell1.setCellStyle(textStyle);
} else {
    cell1.setCellValue("");
    cell1.setCellStyle(textStyle);
}


cell2 = row.createCell(1);
if (record.getR9_ACCT_NO() != null) {
    cell2.setCellValue(record.getR9_ACCT_NO().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}
cell3 = row.createCell(2);
if (record.getR9_ACCT_NAME() != null) {
    cell3.setCellValue(record.getR9_ACCT_NAME());
    cell3.setCellStyle(textStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}
cell4 = row.createCell(3);
if (record.getR9_SCHM_CODE() != null) {
    cell4.setCellValue(record.getR9_SCHM_CODE());
    cell4.setCellStyle(textStyle);
} else {
    cell4.setCellValue("");
    cell4.setCellStyle(textStyle);
}
cell5 = row.createCell(4);
if (record.getR9_SCHM_DESC() != null) {
    cell5.setCellValue(record.getR9_SCHM_DESC());
    cell5.setCellStyle(textStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}
cell6 = row.createCell(5);
if (record.getR9_ACCT_OPN_DATE() != null) {
    cell6.setCellValue(record.getR9_ACCT_OPN_DATE());
    cell6.setCellStyle(dateStyle);
} else {
    cell6.setCellValue("");
    cell6.setCellStyle(textStyle);
}
cell7 = row.createCell(6);
if (record.getR9_CCY() != null) {
    cell7.setCellValue(record.getR9_CCY());
    cell7.setCellStyle(textStyle);
} else {
    cell7.setCellValue("");
    cell7.setCellStyle(textStyle);
}
cell8 = row.createCell(7);
if (record.getR9_BAL_EQUI_TO_BWP() != null) {
    cell8.setCellValue(record.getR9_BAL_EQUI_TO_BWP().doubleValue());
    cell8.setCellStyle(numberStyle);
} else {
    cell8.setCellValue("");
    cell8.setCellStyle(textStyle);
}
cell9 = row.createCell(8);
if (record.getR9_SANCTION_AMT_BWP() != null) {
    cell9.setCellValue(record.getR9_SANCTION_AMT_BWP().doubleValue());
    cell9.setCellStyle(numberStyle);
} else {
    cell9.setCellValue("");
    cell9.setCellStyle(textStyle);
}
cell10 = row.createCell(9);
if (record.getR9_INT_RATE() != null) {
    cell10.setCellValue(record.getR9_INT_RATE().doubleValue());
    cell10.setCellStyle(numberStyle);
} else {
    cell10.setCellValue("");
    cell10.setCellStyle(textStyle);
}
cell12 = row.createCell(11);
if (record.getR9_VALUE_1() != null) {
    cell12.setCellValue(record.getR9_VALUE_1().doubleValue());
    cell12.setCellStyle(numberStyle);
} else {
    cell12.setCellValue("");
    cell12.setCellStyle(textStyle);
}


row = sheet.getRow(9);
cell1 = row.createCell(0);
if (record.getR10_CUST_ID() != null) {
    cell1.setCellValue(record.getR10_CUST_ID());
    cell1.setCellStyle(textStyle);
} else {
    cell1.setCellValue("");
    cell1.setCellStyle(textStyle);
}


cell2 = row.createCell(1);
if (record.getR10_ACCT_NO() != null) {
    cell2.setCellValue(record.getR10_ACCT_NO().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}
cell3 = row.createCell(2);
if (record.getR10_ACCT_NAME() != null) {
    cell3.setCellValue(record.getR10_ACCT_NAME());
    cell3.setCellStyle(textStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}
cell4 = row.createCell(3);
if (record.getR10_SCHM_CODE() != null) {
    cell4.setCellValue(record.getR10_SCHM_CODE());
    cell4.setCellStyle(textStyle);
} else {
    cell4.setCellValue("");
    cell4.setCellStyle(textStyle);
}
cell5 = row.createCell(4);
if (record.getR10_SCHM_DESC() != null) {
    cell5.setCellValue(record.getR10_SCHM_DESC());
    cell5.setCellStyle(textStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}
cell6 = row.createCell(5);
if (record.getR10_ACCT_OPN_DATE() != null) {
    cell6.setCellValue(record.getR10_ACCT_OPN_DATE());
    cell6.setCellStyle(dateStyle);
} else {
    cell6.setCellValue("");
    cell6.setCellStyle(textStyle);
}
cell7 = row.createCell(6);
if (record.getR10_CCY() != null) {
    cell7.setCellValue(record.getR10_CCY());
    cell7.setCellStyle(textStyle);
} else {
    cell7.setCellValue("");
    cell7.setCellStyle(textStyle);
}
cell8 = row.createCell(7);
if (record.getR10_BAL_EQUI_TO_BWP() != null) {
    cell8.setCellValue(record.getR10_BAL_EQUI_TO_BWP().doubleValue());
    cell8.setCellStyle(numberStyle);
} else {
    cell8.setCellValue("");
    cell8.setCellStyle(textStyle);
}
cell9 = row.createCell(8);
if (record.getR10_SANCTION_AMT_BWP() != null) {
    cell9.setCellValue(record.getR10_SANCTION_AMT_BWP().doubleValue());
    cell9.setCellStyle(numberStyle);
} else {
    cell9.setCellValue("");
    cell9.setCellStyle(textStyle);
}
cell10 = row.createCell(9);
if (record.getR10_INT_RATE() != null) {
    cell10.setCellValue(record.getR10_INT_RATE().doubleValue());
    cell10.setCellStyle(numberStyle);
} else {
    cell10.setCellValue("");
    cell10.setCellStyle(textStyle);
}
cell12 = row.createCell(11);
if (record.getR10_VALUE_1() != null) {
    cell12.setCellValue(record.getR10_VALUE_1().doubleValue());
    cell12.setCellStyle(numberStyle);
} else {
    cell12.setCellValue("");
    cell12.setCellStyle(textStyle);
}


row = sheet.getRow(10);
cell1 = row.createCell(0);
if (record.getR11_CUST_ID() != null) {
    cell1.setCellValue(record.getR11_CUST_ID());
    cell1.setCellStyle(textStyle);
} else {
    cell1.setCellValue("");
    cell1.setCellStyle(textStyle);
}


cell2 = row.createCell(1);
if (record.getR11_ACCT_NO() != null) {
    cell2.setCellValue(record.getR11_ACCT_NO().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}
cell3 = row.createCell(2);
if (record.getR11_ACCT_NAME() != null) {
    cell3.setCellValue(record.getR11_ACCT_NAME());
    cell3.setCellStyle(textStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}
cell4 = row.createCell(3);
if (record.getR11_SCHM_CODE() != null) {
    cell4.setCellValue(record.getR11_SCHM_CODE());
    cell4.setCellStyle(textStyle);
} else {
    cell4.setCellValue("");
    cell4.setCellStyle(textStyle);
}
cell5 = row.createCell(4);
if (record.getR11_SCHM_DESC() != null) {
    cell5.setCellValue(record.getR11_SCHM_DESC());
    cell5.setCellStyle(textStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}
cell6 = row.createCell(5);
if (record.getR11_ACCT_OPN_DATE() != null) {
    cell6.setCellValue(record.getR11_ACCT_OPN_DATE());
    cell6.setCellStyle(dateStyle);
} else {
    cell6.setCellValue("");
    cell6.setCellStyle(textStyle);
}
cell7 = row.createCell(6);
if (record.getR11_CCY() != null) {
    cell7.setCellValue(record.getR11_CCY());
    cell7.setCellStyle(textStyle);
} else {
    cell7.setCellValue("");
    cell7.setCellStyle(textStyle);
}
cell8 = row.createCell(7);
if (record.getR11_BAL_EQUI_TO_BWP() != null) {
    cell8.setCellValue(record.getR11_BAL_EQUI_TO_BWP().doubleValue());
    cell8.setCellStyle(numberStyle);
} else {
    cell8.setCellValue("");
    cell8.setCellStyle(textStyle);
}
cell9 = row.createCell(8);
if (record.getR11_SANCTION_AMT_BWP() != null) {
    cell9.setCellValue(record.getR11_SANCTION_AMT_BWP().doubleValue());
    cell9.setCellStyle(numberStyle);
} else {
    cell9.setCellValue("");
    cell9.setCellStyle(textStyle);
}
cell10 = row.createCell(9);
if (record.getR11_INT_RATE() != null) {
    cell10.setCellValue(record.getR11_INT_RATE().doubleValue());
    cell10.setCellStyle(numberStyle);
} else {
    cell10.setCellValue("");
    cell10.setCellStyle(textStyle);
}
cell12 = row.createCell(11);
if (record.getR11_VALUE_1() != null) {
    cell12.setCellValue(record.getR11_VALUE_1().doubleValue());
    cell12.setCellStyle(numberStyle);
} else {
    cell12.setCellValue("");
    cell12.setCellStyle(textStyle);
}


row = sheet.getRow(11);
cell1 = row.createCell(0);
if (record.getR12_CUST_ID() != null) {
    cell1.setCellValue(record.getR12_CUST_ID());
    cell1.setCellStyle(textStyle);
} else {
    cell1.setCellValue("");
    cell1.setCellStyle(textStyle);
}


cell2 = row.createCell(1);
if (record.getR12_ACCT_NO() != null) {
    cell2.setCellValue(record.getR12_ACCT_NO().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}
cell3 = row.createCell(2);
if (record.getR12_ACCT_NAME() != null) {
    cell3.setCellValue(record.getR12_ACCT_NAME());
    cell3.setCellStyle(textStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}
cell4 = row.createCell(3);
if (record.getR12_SCHM_CODE() != null) {
    cell4.setCellValue(record.getR12_SCHM_CODE());
    cell4.setCellStyle(textStyle);
} else {
    cell4.setCellValue("");
    cell4.setCellStyle(textStyle);
}
cell5 = row.createCell(4);
if (record.getR12_SCHM_DESC() != null) {
    cell5.setCellValue(record.getR12_SCHM_DESC());
    cell5.setCellStyle(textStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}
cell6 = row.createCell(5);
if (record.getR12_ACCT_OPN_DATE() != null) {
    cell6.setCellValue(record.getR12_ACCT_OPN_DATE());
    cell6.setCellStyle(dateStyle);
} else {
    cell6.setCellValue("");
    cell6.setCellStyle(textStyle);
}
cell7 = row.createCell(6);
if (record.getR12_CCY() != null) {
    cell7.setCellValue(record.getR12_CCY());
    cell7.setCellStyle(textStyle);
} else {
    cell7.setCellValue("");
    cell7.setCellStyle(textStyle);
}
cell8 = row.createCell(7);
if (record.getR12_BAL_EQUI_TO_BWP() != null) {
    cell8.setCellValue(record.getR12_BAL_EQUI_TO_BWP().doubleValue());
    cell8.setCellStyle(numberStyle);
} else {
    cell8.setCellValue("");
    cell8.setCellStyle(textStyle);
}
cell9 = row.createCell(8);
if (record.getR12_SANCTION_AMT_BWP() != null) {
    cell9.setCellValue(record.getR12_SANCTION_AMT_BWP().doubleValue());
    cell9.setCellStyle(numberStyle);
} else {
    cell9.setCellValue("");
    cell9.setCellStyle(textStyle);
}
cell10 = row.createCell(9);
if (record.getR12_INT_RATE() != null) {
    cell10.setCellValue(record.getR12_INT_RATE().doubleValue());
    cell10.setCellStyle(numberStyle);
} else {
    cell10.setCellValue("");
    cell10.setCellStyle(textStyle);
}
cell12 = row.createCell(11);
if (record.getR12_VALUE_1() != null) {
    cell12.setCellValue(record.getR12_VALUE_1().doubleValue());
    cell12.setCellStyle(numberStyle);
} else {
    cell12.setCellValue("");
    cell12.setCellStyle(textStyle);
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

	public List<Object> getBORR_UFCEArchival() {
		List<Object> BORR_UFCEArchivallist = new ArrayList<>();
		try {
			BORR_UFCEArchivallist = BRRS_BORR_UFCE_Archival_Summary_Repo.getBORR_UFCEarchival();
//			BORR_UFCEArchivallist = BRRS_BORR_UFCE_Manual_Archival_Summary_Repo.getBORR_UFCEarchival();
			System.out.println("countser" + BORR_UFCEArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching BORR_UFCE Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return BORR_UFCEArchivallist;
	}
	
	
	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			 String dtltype, String type, String version) {
try {
logger.info("Generating Excel for BRRS_BORR_UFCE ARCHIVAL Details...");
System.out.println("came to Detail download service");
if (type.equals("ARCHIVAL") & version != null) {

}
XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("BORR_UFCEDetail");

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
List<BORR_UFCE_Archival_Detail_Entity> reportData = BRRS_BORR_UFCE_Archival_Detail_Repo.getdatabydateList(parsedToDate,version);

if (reportData != null && !reportData.isEmpty()) {
int rowIndex = 1;
for (BORR_UFCE_Archival_Detail_Entity item : reportData) {
XSSFRow row = sheet.createRow(rowIndex++);

//row.createCell(0).setCellValue(item.getCustId());
//row.createCell(1).setCellValue(item.getAcctNumber());
//row.createCell(2).setCellValue(item.getAcctName());
//
//// ACCT BALANCE (right aligned, 3 decimal places with comma separator)
//Cell balanceCell = row.createCell(3);
//
//if (item.getAcctBalanceInpula() != null) {
//balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
//} else {
//balanceCell.setCellValue(0);
//}
//
// Create style with thousand separator and decimal point
DataFormat format = workbook.createDataFormat();

// Format: 1,234,567
balanceStyle.setDataFormat(format.getFormat("#,##0"));

// Right alignment (optional)
balanceStyle.setAlignment(HorizontalAlignment.RIGHT);

//balanceCell.setCellStyle(balanceStyle);

//row.createCell(4).setCellValue(item.getReportLable());
//row.createCell(5).setCellValue(item.getReportAddlCriteria1());
//row.createCell(6).setCellValue(
//item.getReportDate() != null ?
//new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate()) : ""
//);
//
//// Apply data style for all other cells
//for (int j = 0; j < 7; j++) {
//if (j != 3) {
//row.getCell(j).setCellStyle(dataStyle);
//}
//}
//}
//}
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
row.createCell(4).setCellValue(item.getSchmCode());
row.createCell(5).setCellValue(item.getSchmDesc());
row.createCell(6)
.setCellValue(item.getAcctOpnDate() != null
		? new SimpleDateFormat("dd-MM-yyyy").format(item.getAcctOpnDate())
		: "");
row.createCell(7).setCellValue(item.getCcy());
Cell sanctionCell = row.createCell(8);
if (item.getSanctionAmount() != null) {
	sanctionCell.setCellValue(item.getSanctionAmount().doubleValue());
} else {
	sanctionCell.setCellValue(0);
}
sanctionCell.setCellStyle(balanceStyle);
Cell intRateCell = row.createCell(9);
if (item.getIntRate() != null) {
	intRateCell.setCellValue(item.getIntRate().doubleValue());
} else {
	intRateCell.setCellValue(0);
}

		row.createCell(10).setCellValue(item.getReportLable());
		row.createCell(11).setCellValue(item.getReportAddlCriteria1());
		row.createCell(12)
				.setCellValue(item.getReportDate() != null
						? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
						: "");

		// Apply data style for all other cells
		for (int j = 0; j < 13; j++) {
			if (j != 3) {
				row.getCell(j).setCellStyle(dataStyle);
			}
		}
	}
}
else {
logger.info("No data found for BORR_UFCE — only header will be written.");
}
// Write to byte[]
ByteArrayOutputStream bos = new ByteArrayOutputStream();
workbook.write(bos);
workbook.close();

logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
return bos.toByteArray();

} catch (Exception e) {
logger.error("Error generating BORR_UFCE Excel", e);
return new byte[0];
}
}

	public byte[] getExcelBORR_UFCEARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<BORR_UFCE_Archival_Summary_Entity> dataList = BRRS_BORR_UFCE_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for ADISB1 report. Returning empty result.");
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
			int startRow = 3;
			
			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

				BORR_UFCE_Archival_Summary_Entity record = dataList.get(i);
				System.out.println("rownumber=" + startRow + i);
				Row row = sheet.getRow(startRow + i);
				if (row == null) {
				row = sheet.createRow(startRow + i);
				}
				
				Cell cell1 = row.createCell(0);
				if (record.getR4_CUST_ID() != null) {
				    cell1.setCellValue(record.getR4_CUST_ID());
				    cell1.setCellStyle(textStyle);
				} else {
				    cell1.setCellValue("");
				    cell1.setCellStyle(textStyle);
				}


				Cell cell2 = row.createCell(1);
				if (record.getR4_ACCT_NO() != null) {
				    cell2.setCellValue(record.getR4_ACCT_NO().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}
				Cell cell3 = row.createCell(2);
				if (record.getR4_ACCT_NAME() != null) {
				    cell3.setCellValue(record.getR4_ACCT_NAME());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				Cell cell4 = row.createCell(3);
				if (record.getR4_SCHM_CODE() != null) {
				    cell4.setCellValue(record.getR4_SCHM_CODE());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}
				Cell cell5 = row.createCell(4);
				if (record.getR4_SCHM_DESC() != null) {
				    cell5.setCellValue(record.getR4_SCHM_DESC());
				    cell5.setCellStyle(textStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}
				Cell cell6 = row.createCell(5);
				if (record.getR4_ACCT_OPN_DATE() != null) {
				    cell6.setCellValue(record.getR4_ACCT_OPN_DATE());
				    cell6.setCellStyle(dateStyle);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}
				Cell cell7 = row.createCell(6);
				if (record.getR4_CCY() != null) {
				    cell7.setCellValue(record.getR4_CCY());
				    cell7.setCellStyle(textStyle);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}
				Cell cell8 = row.createCell(7);
				if (record.getR4_BAL_EQUI_TO_BWP() != null) {
				    cell8.setCellValue(record.getR4_BAL_EQUI_TO_BWP().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}
				Cell cell9 = row.createCell(8);
				if (record.getR4_SANCTION_AMT_BWP() != null) {
				    cell9.setCellValue(record.getR4_SANCTION_AMT_BWP().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}
				Cell cell10 = row.createCell(9);
				if (record.getR4_INT_RATE() != null) {
				    cell10.setCellValue(record.getR4_INT_RATE().doubleValue());
				    cell10.setCellStyle(numberStyle);
				} else {
				    cell10.setCellValue("");
				    cell10.setCellStyle(textStyle);
				}
				Cell cell12 = row.createCell(11);
				if (record.getR4_VALUE_1() != null) {
				    cell12.setCellValue(record.getR4_VALUE_1().doubleValue());
				    cell12.setCellStyle(numberStyle);
				} else {
				    cell12.setCellValue("");
				    cell12.setCellStyle(textStyle);
				}


				row = sheet.getRow(4);
				cell1 = row.createCell(0);
				if (record.getR5_CUST_ID() != null) {
				    cell1.setCellValue(record.getR5_CUST_ID());
				    cell1.setCellStyle(textStyle);
				} else {
				    cell1.setCellValue("");
				    cell1.setCellStyle(textStyle);
				}


				cell2 = row.createCell(1);
				if (record.getR5_ACCT_NO() != null) {
				    cell2.setCellValue(record.getR5_ACCT_NO().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}
				cell3 = row.createCell(2);
				if (record.getR5_ACCT_NAME() != null) {
				    cell3.setCellValue(record.getR5_ACCT_NAME());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell4 = row.createCell(3);
				if (record.getR5_SCHM_CODE() != null) {
				    cell4.setCellValue(record.getR5_SCHM_CODE());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}
				cell5 = row.createCell(4);
				if (record.getR5_SCHM_DESC() != null) {
				    cell5.setCellValue(record.getR5_SCHM_DESC());
				    cell5.setCellStyle(textStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}
				cell6 = row.createCell(5);
				if (record.getR5_ACCT_OPN_DATE() != null) {
				    cell6.setCellValue(record.getR5_ACCT_OPN_DATE());
				    cell6.setCellStyle(dateStyle);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}
				cell7 = row.createCell(6);
				if (record.getR5_CCY() != null) {
				    cell7.setCellValue(record.getR5_CCY());
				    cell7.setCellStyle(textStyle);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}
				cell8 = row.createCell(7);
				if (record.getR5_BAL_EQUI_TO_BWP() != null) {
				    cell8.setCellValue(record.getR5_BAL_EQUI_TO_BWP().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}
				cell9 = row.createCell(8);
				if (record.getR5_SANCTION_AMT_BWP() != null) {
				    cell9.setCellValue(record.getR5_SANCTION_AMT_BWP().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}
				cell10 = row.createCell(9);
				if (record.getR5_INT_RATE() != null) {
				    cell10.setCellValue(record.getR5_INT_RATE().doubleValue());
				    cell10.setCellStyle(numberStyle);
				} else {
				    cell10.setCellValue("");
				    cell10.setCellStyle(textStyle);
				}
				cell12 = row.createCell(11);
				if (record.getR5_VALUE_1() != null) {
				    cell12.setCellValue(record.getR5_VALUE_1().doubleValue());
				    cell12.setCellStyle(numberStyle);
				} else {
				    cell12.setCellValue("");
				    cell12.setCellStyle(textStyle);
				}


				row = sheet.getRow(5);
				cell1 = row.createCell(0);
				if (record.getR6_CUST_ID() != null) {
				    cell1.setCellValue(record.getR6_CUST_ID());
				    cell1.setCellStyle(textStyle);
				} else {
				    cell1.setCellValue("");
				    cell1.setCellStyle(textStyle);
				}


				cell2 = row.createCell(1);
				if (record.getR6_ACCT_NO() != null) {
				    cell2.setCellValue(record.getR6_ACCT_NO().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}
				cell3 = row.createCell(2);
				if (record.getR6_ACCT_NAME() != null) {
				    cell3.setCellValue(record.getR6_ACCT_NAME());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell4 = row.createCell(3);
				if (record.getR6_SCHM_CODE() != null) {
				    cell4.setCellValue(record.getR6_SCHM_CODE());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}
				cell5 = row.createCell(4);
				if (record.getR6_SCHM_DESC() != null) {
				    cell5.setCellValue(record.getR6_SCHM_DESC());
				    cell5.setCellStyle(textStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}
				cell6 = row.createCell(5);
				if (record.getR6_ACCT_OPN_DATE() != null) {
				    cell6.setCellValue(record.getR6_ACCT_OPN_DATE());
				    cell6.setCellStyle(dateStyle);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}
				cell7 = row.createCell(6);
				if (record.getR6_CCY() != null) {
				    cell7.setCellValue(record.getR6_CCY());
				    cell7.setCellStyle(textStyle);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}
				cell8 = row.createCell(7);
				if (record.getR6_BAL_EQUI_TO_BWP() != null) {
				    cell8.setCellValue(record.getR6_BAL_EQUI_TO_BWP().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}
				cell9 = row.createCell(8);
				if (record.getR6_SANCTION_AMT_BWP() != null) {
				    cell9.setCellValue(record.getR6_SANCTION_AMT_BWP().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}
				cell10 = row.createCell(9);
				if (record.getR6_INT_RATE() != null) {
				    cell10.setCellValue(record.getR6_INT_RATE().doubleValue());
				    cell10.setCellStyle(numberStyle);
				} else {
				    cell10.setCellValue("");
				    cell10.setCellStyle(textStyle);
				}
				cell12 = row.createCell(11);
				if (record.getR6_VALUE_1() != null) {
				    cell12.setCellValue(record.getR6_VALUE_1().doubleValue());
				    cell12.setCellStyle(numberStyle);
				} else {
				    cell12.setCellValue("");
				    cell12.setCellStyle(textStyle);
				}


				row = sheet.getRow(6);
				cell1 = row.createCell(0);
				if (record.getR7_CUST_ID() != null) {
				    cell1.setCellValue(record.getR7_CUST_ID());
				    cell1.setCellStyle(textStyle);
				} else {
				    cell1.setCellValue("");
				    cell1.setCellStyle(textStyle);
				}


				cell2 = row.createCell(1);
				if (record.getR7_ACCT_NO() != null) {
				    cell2.setCellValue(record.getR7_ACCT_NO().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}
				cell3 = row.createCell(2);
				if (record.getR7_ACCT_NAME() != null) {
				    cell3.setCellValue(record.getR7_ACCT_NAME());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell4 = row.createCell(3);
				if (record.getR7_SCHM_CODE() != null) {
				    cell4.setCellValue(record.getR7_SCHM_CODE());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}
				cell5 = row.createCell(4);
				if (record.getR7_SCHM_DESC() != null) {
				    cell5.setCellValue(record.getR7_SCHM_DESC());
				    cell5.setCellStyle(textStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}
				cell6 = row.createCell(5);
				if (record.getR7_ACCT_OPN_DATE() != null) {
				    cell6.setCellValue(record.getR7_ACCT_OPN_DATE());
				    cell6.setCellStyle(dateStyle);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}
				cell7 = row.createCell(6);
				if (record.getR7_CCY() != null) {
				    cell7.setCellValue(record.getR7_CCY());
				    cell7.setCellStyle(textStyle);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}
				cell8 = row.createCell(7);
				if (record.getR7_BAL_EQUI_TO_BWP() != null) {
				    cell8.setCellValue(record.getR7_BAL_EQUI_TO_BWP().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}
				cell9 = row.createCell(8);
				if (record.getR7_SANCTION_AMT_BWP() != null) {
				    cell9.setCellValue(record.getR7_SANCTION_AMT_BWP().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}
				cell10 = row.createCell(9);
				if (record.getR7_INT_RATE() != null) {
				    cell10.setCellValue(record.getR7_INT_RATE().doubleValue());
				    cell10.setCellStyle(numberStyle);
				} else {
				    cell10.setCellValue("");
				    cell10.setCellStyle(textStyle);
				}
				cell12 = row.createCell(11);
				if (record.getR7_VALUE_1() != null) {
				    cell12.setCellValue(record.getR7_VALUE_1().doubleValue());
				    cell12.setCellStyle(numberStyle);
				} else {
				    cell12.setCellValue("");
				    cell12.setCellStyle(textStyle);
				}


				row = sheet.getRow(7);
				cell1 = row.createCell(0);
				if (record.getR8_CUST_ID() != null) {
				    cell1.setCellValue(record.getR8_CUST_ID());
				    cell1.setCellStyle(textStyle);
				} else {
				    cell1.setCellValue("");
				    cell1.setCellStyle(textStyle);
				}


				cell2 = row.createCell(1);
				if (record.getR8_ACCT_NO() != null) {
				    cell2.setCellValue(record.getR8_ACCT_NO().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}
				cell3 = row.createCell(2);
				if (record.getR8_ACCT_NAME() != null) {
				    cell3.setCellValue(record.getR8_ACCT_NAME());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell4 = row.createCell(3);
				if (record.getR8_SCHM_CODE() != null) {
				    cell4.setCellValue(record.getR8_SCHM_CODE());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}
				cell5 = row.createCell(4);
				if (record.getR8_SCHM_DESC() != null) {
				    cell5.setCellValue(record.getR8_SCHM_DESC());
				    cell5.setCellStyle(textStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}
				cell6 = row.createCell(5);
				if (record.getR8_ACCT_OPN_DATE() != null) {
				    cell6.setCellValue(record.getR8_ACCT_OPN_DATE());
				    cell6.setCellStyle(dateStyle);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}
				cell7 = row.createCell(6);
				if (record.getR8_CCY() != null) {
				    cell7.setCellValue(record.getR8_CCY());
				    cell7.setCellStyle(textStyle);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}
				cell8 = row.createCell(7);
				if (record.getR8_BAL_EQUI_TO_BWP() != null) {
				    cell8.setCellValue(record.getR8_BAL_EQUI_TO_BWP().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}
				cell9 = row.createCell(8);
				if (record.getR8_SANCTION_AMT_BWP() != null) {
				    cell9.setCellValue(record.getR8_SANCTION_AMT_BWP().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}
				cell10 = row.createCell(9);
				if (record.getR8_INT_RATE() != null) {
				    cell10.setCellValue(record.getR8_INT_RATE().doubleValue());
				    cell10.setCellStyle(numberStyle);
				} else {
				    cell10.setCellValue("");
				    cell10.setCellStyle(textStyle);
				}
				cell12 = row.createCell(11);
				if (record.getR8_VALUE_1() != null) {
				    cell12.setCellValue(record.getR8_VALUE_1().doubleValue());
				    cell12.setCellStyle(numberStyle);
				} else {
				    cell12.setCellValue("");
				    cell12.setCellStyle(textStyle);
				}


				row = sheet.getRow(8);
				cell1 = row.createCell(0);
				if (record.getR9_CUST_ID() != null) {
				    cell1.setCellValue(record.getR9_CUST_ID());
				    cell1.setCellStyle(textStyle);
				} else {
				    cell1.setCellValue("");
				    cell1.setCellStyle(textStyle);
				}


				cell2 = row.createCell(1);
				if (record.getR9_ACCT_NO() != null) {
				    cell2.setCellValue(record.getR9_ACCT_NO().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}
				cell3 = row.createCell(2);
				if (record.getR9_ACCT_NAME() != null) {
				    cell3.setCellValue(record.getR9_ACCT_NAME());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell4 = row.createCell(3);
				if (record.getR9_SCHM_CODE() != null) {
				    cell4.setCellValue(record.getR9_SCHM_CODE());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}
				cell5 = row.createCell(4);
				if (record.getR9_SCHM_DESC() != null) {
				    cell5.setCellValue(record.getR9_SCHM_DESC());
				    cell5.setCellStyle(textStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}
				cell6 = row.createCell(5);
				if (record.getR9_ACCT_OPN_DATE() != null) {
				    cell6.setCellValue(record.getR9_ACCT_OPN_DATE());
				    cell6.setCellStyle(dateStyle);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}
				cell7 = row.createCell(6);
				if (record.getR9_CCY() != null) {
				    cell7.setCellValue(record.getR9_CCY());
				    cell7.setCellStyle(textStyle);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}
				cell8 = row.createCell(7);
				if (record.getR9_BAL_EQUI_TO_BWP() != null) {
				    cell8.setCellValue(record.getR9_BAL_EQUI_TO_BWP().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}
				cell9 = row.createCell(8);
				if (record.getR9_SANCTION_AMT_BWP() != null) {
				    cell9.setCellValue(record.getR9_SANCTION_AMT_BWP().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}
				cell10 = row.createCell(9);
				if (record.getR9_INT_RATE() != null) {
				    cell10.setCellValue(record.getR9_INT_RATE().doubleValue());
				    cell10.setCellStyle(numberStyle);
				} else {
				    cell10.setCellValue("");
				    cell10.setCellStyle(textStyle);
				}
				cell12 = row.createCell(11);
				if (record.getR9_VALUE_1() != null) {
				    cell12.setCellValue(record.getR9_VALUE_1().doubleValue());
				    cell12.setCellStyle(numberStyle);
				} else {
				    cell12.setCellValue("");
				    cell12.setCellStyle(textStyle);
				}


				row = sheet.getRow(9);
				cell1 = row.createCell(0);
				if (record.getR10_CUST_ID() != null) {
				    cell1.setCellValue(record.getR10_CUST_ID());
				    cell1.setCellStyle(textStyle);
				} else {
				    cell1.setCellValue("");
				    cell1.setCellStyle(textStyle);
				}


				cell2 = row.createCell(1);
				if (record.getR10_ACCT_NO() != null) {
				    cell2.setCellValue(record.getR10_ACCT_NO().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}
				cell3 = row.createCell(2);
				if (record.getR10_ACCT_NAME() != null) {
				    cell3.setCellValue(record.getR10_ACCT_NAME());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell4 = row.createCell(3);
				if (record.getR10_SCHM_CODE() != null) {
				    cell4.setCellValue(record.getR10_SCHM_CODE());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}
				cell5 = row.createCell(4);
				if (record.getR10_SCHM_DESC() != null) {
				    cell5.setCellValue(record.getR10_SCHM_DESC());
				    cell5.setCellStyle(textStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}
				cell6 = row.createCell(5);
				if (record.getR10_ACCT_OPN_DATE() != null) {
				    cell6.setCellValue(record.getR10_ACCT_OPN_DATE());
				    cell6.setCellStyle(dateStyle);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}
				cell7 = row.createCell(6);
				if (record.getR10_CCY() != null) {
				    cell7.setCellValue(record.getR10_CCY());
				    cell7.setCellStyle(textStyle);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}
				cell8 = row.createCell(7);
				if (record.getR10_BAL_EQUI_TO_BWP() != null) {
				    cell8.setCellValue(record.getR10_BAL_EQUI_TO_BWP().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}
				cell9 = row.createCell(8);
				if (record.getR10_SANCTION_AMT_BWP() != null) {
				    cell9.setCellValue(record.getR10_SANCTION_AMT_BWP().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}
				cell10 = row.createCell(9);
				if (record.getR10_INT_RATE() != null) {
				    cell10.setCellValue(record.getR10_INT_RATE().doubleValue());
				    cell10.setCellStyle(numberStyle);
				} else {
				    cell10.setCellValue("");
				    cell10.setCellStyle(textStyle);
				}
				cell12 = row.createCell(11);
				if (record.getR10_VALUE_1() != null) {
				    cell12.setCellValue(record.getR10_VALUE_1().doubleValue());
				    cell12.setCellStyle(numberStyle);
				} else {
				    cell12.setCellValue("");
				    cell12.setCellStyle(textStyle);
				}


				row = sheet.getRow(10);
				cell1 = row.createCell(0);
				if (record.getR11_CUST_ID() != null) {
				    cell1.setCellValue(record.getR11_CUST_ID());
				    cell1.setCellStyle(textStyle);
				} else {
				    cell1.setCellValue("");
				    cell1.setCellStyle(textStyle);
				}


				cell2 = row.createCell(1);
				if (record.getR11_ACCT_NO() != null) {
				    cell2.setCellValue(record.getR11_ACCT_NO().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}
				cell3 = row.createCell(2);
				if (record.getR11_ACCT_NAME() != null) {
				    cell3.setCellValue(record.getR11_ACCT_NAME());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell4 = row.createCell(3);
				if (record.getR11_SCHM_CODE() != null) {
				    cell4.setCellValue(record.getR11_SCHM_CODE());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}
				cell5 = row.createCell(4);
				if (record.getR11_SCHM_DESC() != null) {
				    cell5.setCellValue(record.getR11_SCHM_DESC());
				    cell5.setCellStyle(textStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}
				cell6 = row.createCell(5);
				if (record.getR11_ACCT_OPN_DATE() != null) {
				    cell6.setCellValue(record.getR11_ACCT_OPN_DATE());
				    cell6.setCellStyle(dateStyle);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}
				cell7 = row.createCell(6);
				if (record.getR11_CCY() != null) {
				    cell7.setCellValue(record.getR11_CCY());
				    cell7.setCellStyle(textStyle);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}
				cell8 = row.createCell(7);
				if (record.getR11_BAL_EQUI_TO_BWP() != null) {
				    cell8.setCellValue(record.getR11_BAL_EQUI_TO_BWP().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}
				cell9 = row.createCell(8);
				if (record.getR11_SANCTION_AMT_BWP() != null) {
				    cell9.setCellValue(record.getR11_SANCTION_AMT_BWP().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}
				cell10 = row.createCell(9);
				if (record.getR11_INT_RATE() != null) {
				    cell10.setCellValue(record.getR11_INT_RATE().doubleValue());
				    cell10.setCellStyle(numberStyle);
				} else {
				    cell10.setCellValue("");
				    cell10.setCellStyle(textStyle);
				}
				cell12 = row.createCell(11);
				if (record.getR11_VALUE_1() != null) {
				    cell12.setCellValue(record.getR11_VALUE_1().doubleValue());
				    cell12.setCellStyle(numberStyle);
				} else {
				    cell12.setCellValue("");
				    cell12.setCellStyle(textStyle);
				}


				row = sheet.getRow(11);
				cell1 = row.createCell(0);
				if (record.getR12_CUST_ID() != null) {
				    cell1.setCellValue(record.getR12_CUST_ID());
				    cell1.setCellStyle(textStyle);
				} else {
				    cell1.setCellValue("");
				    cell1.setCellStyle(textStyle);
				}


				cell2 = row.createCell(1);
				if (record.getR12_ACCT_NO() != null) {
				    cell2.setCellValue(record.getR12_ACCT_NO().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}
				cell3 = row.createCell(2);
				if (record.getR12_ACCT_NAME() != null) {
				    cell3.setCellValue(record.getR12_ACCT_NAME());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}
				cell4 = row.createCell(3);
				if (record.getR12_SCHM_CODE() != null) {
				    cell4.setCellValue(record.getR12_SCHM_CODE());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}
				cell5 = row.createCell(4);
				if (record.getR12_SCHM_DESC() != null) {
				    cell5.setCellValue(record.getR12_SCHM_DESC());
				    cell5.setCellStyle(textStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}
				cell6 = row.createCell(5);
				if (record.getR12_ACCT_OPN_DATE() != null) {
				    cell6.setCellValue(record.getR12_ACCT_OPN_DATE());
				    cell6.setCellStyle(dateStyle);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}
				cell7 = row.createCell(6);
				if (record.getR12_CCY() != null) {
				    cell7.setCellValue(record.getR12_CCY());
				    cell7.setCellStyle(textStyle);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}
				cell8 = row.createCell(7);
				if (record.getR12_BAL_EQUI_TO_BWP() != null) {
				    cell8.setCellValue(record.getR12_BAL_EQUI_TO_BWP().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}
				cell9 = row.createCell(8);
				if (record.getR12_SANCTION_AMT_BWP() != null) {
				    cell9.setCellValue(record.getR12_SANCTION_AMT_BWP().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}
				cell10 = row.createCell(9);
				if (record.getR12_INT_RATE() != null) {
				    cell10.setCellValue(record.getR12_INT_RATE().doubleValue());
				    cell10.setCellStyle(numberStyle);
				} else {
				    cell10.setCellValue("");
				    cell10.setCellStyle(textStyle);
				}
				cell12 = row.createCell(11);
				if (record.getR12_VALUE_1() != null) {
				    cell12.setCellValue(record.getR12_VALUE_1().doubleValue());
				    cell12.setCellStyle(numberStyle);
				} else {
				    cell12.setCellValue("");
				    cell12.setCellStyle(textStyle);
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

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/BORR_UFCE"); // ✅ match the report name
	    System.out.println("Hello");
	    if (acctNo != null) {
	    	BORR_UFCE_Detail_Entity BORR_UFCEEntity = BRRS_BORR_UFCE_Detail_Repo.findByAcctnumber(acctNo);
	        if (BORR_UFCEEntity != null && BORR_UFCEEntity.getReportDate() != null) {
	            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(BORR_UFCEEntity.getReportDate());
	            mv.addObject("asondate", formattedDate);
	        }
	        mv.addObject("Data", BORR_UFCEEntity);
	    }

	    mv.addObject("displaymode", "edit");
	    mv.addObject("formmode", formMode != null ? formMode : "edit");
	    return mv;
	}





	public ModelAndView updateDetailEdit(String acctNo, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/BORR_UFCE"); // ✅ match the report name

	    if (acctNo != null) {
	        BORR_UFCE_Detail_Entity la1Entity = BRRS_BORR_UFCE_Detail_Repo.findByAcctnumber(acctNo);
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

	        BORR_UFCE_Detail_Entity existing = BRRS_BORR_UFCE_Detail_Repo.findByAcctnumber(acctNo);
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
	        	BRRS_BORR_UFCE_Detail_Repo.save(existing);
	            logger.info("Record updated successfully for account {}", acctNo);

	            // Format date for procedure
	            String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
	                    .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

	            // Run summary procedure after commit
	            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
	                @Override
	                public void afterCommit() {
	                    try {
	                        logger.info("Transaction committed — calling BRRS_BORR_UFCE_SUMMARY_PROCEDURE({})",
	                                formattedDate);
	                        jdbcTemplate.update("BEGIN BRRS_BORR_UFCE_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
	        logger.error("Error updating BORR_UFCE record", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error updating record: " + e.getMessage());
	    }
	}
	
	
	
	
}