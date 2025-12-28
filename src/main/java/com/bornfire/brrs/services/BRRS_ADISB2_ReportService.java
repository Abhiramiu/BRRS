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

import com.bornfire.brrs.entities.BRRS_ADISB1_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_ADISB1_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_ADISB1_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_ADISB1_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_ADISB2_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_ADISB2_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_ADISB2_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_ADISB2_Summary_Repo;

import com.bornfire.brrs.entities.ADISB1_Archival_Detail_Entity;
import com.bornfire.brrs.entities.ADISB1_Archival_Summary_Entity;
import com.bornfire.brrs.entities.ADISB1_Detail_Entity;
import com.bornfire.brrs.entities.ADISB1_Summary_Entity;
import com.bornfire.brrs.entities.ADISB2_Archival_Detail_Entity;
import com.bornfire.brrs.entities.ADISB2_Archival_Summary_Entity;
import com.bornfire.brrs.entities.ADISB2_Detail_Entity;
import com.bornfire.brrs.entities.ADISB2_Summary_Entity;
import com.bornfire.brrs.entities.ADISB1_Manual_Summary_Entity;
import com.bornfire.brrs.entities.ADISB1_Manual_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_ADISB1_Manual_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_ADISB1_Manual_Archival_Summary_Repo;

@Component
@Service
public class BRRS_ADISB2_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_ADISB2_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_ADISB2_Detail_Repo  ADISB2_Detail_Repo;

	@Autowired
	BRRS_ADISB2_Summary_Repo  ADISB2_Summary_Repo;

	@Autowired
	BRRS_ADISB2_Archival_Detail_Repo ADISB2_Archival_Detail_Repo;

	@Autowired
	BRRS_ADISB2_Archival_Summary_Repo ADISB2_Archival_Summary_Repo;

	



	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getADISB2View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type,  String version) {
		ModelAndView mv = new ModelAndView();
//		Session hs = sessionFactory.getCurrentSession();
//		int pageSize = pageable.getPageSize();
//		int currentPage = pageable.getPageNumber();
//		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<ADISB2_Archival_Summary_Entity> T1Master = new ArrayList<ADISB2_Archival_Summary_Entity>();
			
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = ADISB2_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			
            
		} else {
			List<ADISB2_Summary_Entity> T1Master = new ArrayList<ADISB2_Summary_Entity>();
			
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = ADISB2_Summary_Repo.getdatabydateList(dateformat.parse(todate));
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
            
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/ADISB2");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}
	
	public ModelAndView getADISB2currentDtl(String reportId, String fromdate, String todate, String currency,
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
			List<ADISB2_Archival_Detail_Entity> T1Dt1;
			if (rowId != null && columnId != null) {
				T1Dt1 = ADISB2_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
			} else {
				T1Dt1 = ADISB2_Archival_Detail_Repo.getdatabydateList(parsedDate, version);					
			}

			mv.addObject("reportdetails", T1Dt1);
			mv.addObject("reportmaster12", T1Dt1);
			System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

		} else {
			// 🔹 Current branch
			List<ADISB2_Detail_Entity> T1Dt1;
			if (rowId != null && columnId != null) {
				T1Dt1 = ADISB2_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
			} else {
				T1Dt1 = ADISB2_Detail_Repo.getdatabydateList(parsedDate);
				totalPages = ADISB2_Detail_Repo.getdatacount(parsedDate);
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
	mv.setViewName("BRRS/ADISB2");
	mv.addObject("displaymode", "Details");
	mv.addObject("currentPage", currentPage);
	System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
	mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
	mv.addObject("reportsflag", "reportsflag");
	mv.addObject("menu", reportId);

	return mv;
}
	
	public byte[] getADISB2DetailExcel(String filename, String fromdate, String todate, String currency,
			   String dtltype, String type, String version) {
try {
logger.info("Generating Excel for ADISB2 Details...");
System.out.println("came to Detail download service");


if (type.equals("ARCHIVAL") & version != null) {
byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
version);
return ARCHIVALreport;
}

XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("ADISB2Detail");

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
List<ADISB2_Detail_Entity> reportData = ADISB2_Detail_Repo.getdatabydateList(parsedToDate);

if (reportData != null && !reportData.isEmpty()) {
int rowIndex = 1;
for (ADISB2_Detail_Entity item : reportData) {
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
	logger.info("No data found for ADISB2 — only header will be written.");
}

//Write to byte[]
ByteArrayOutputStream bos = new ByteArrayOutputStream();
workbook.write(bos);
workbook.close();

logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
return bos.toByteArray();

} catch (Exception e) {
logger.error("Error generating ADISB2 Excel", e);
return new byte[0];
}
}
	
	public byte[] getM_ADISB2Excel(String filename, String reportId, String fromdate, String todate, String currency,
			 String dtltype, String type, String version) throws Exception {
logger.info("Service: Starting Excel generation process in memory.");
logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

//Convert string to Date
Date reportDate = dateformat.parse(todate);

//ARCHIVAL check
if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
logger.info("Service: Generating ARCHIVAL report for version {}", version);
return getExcelADISB2ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
}
//RESUB check
else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
logger.info("Service: Generating RESUB report for version {}", version);


List<ADISB2_Archival_Summary_Entity> T1Master =
ADISB2_Archival_Summary_Repo.getdatabydateListarchival(reportDate, version);


}




//Default (LIVE) case
List<ADISB2_Summary_Entity> dataList1 = ADISB2_Summary_Repo.getdatabydateList(reportDate);

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

int startRow = 5;

if (!dataList1.isEmpty()) {
for (int i = 0; i < dataList1.size(); i++) {

ADISB2_Summary_Entity record = dataList1.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}

//Cell2 - R5_FIRST_NAME


//Cell26 - R5_EXCHANGE_RATE
Cell cell2 = row.createCell(2);
if (record.getR6_TOTAL_DEPOSIT_AMOUNT() != null) {
cell2.setCellValue(record.getR6_TOTAL_DEPOSIT_AMOUNT().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}


Cell cell3 = row.createCell(3);
if (record.getR6_COVERED_DEPOSITORS() != null) {
cell3.setCellValue(record.getR6_COVERED_DEPOSITORS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

Cell cell4 = row.createCell(4);
if (record.getR6_COVERED_DEPOSITORS_PCT() != null) {
cell4.setCellValue(record.getR6_COVERED_DEPOSITORS_PCT().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

Cell cell5 = row.createCell(5);
if (record.getR6_EXCEEDING_DEPOSITORS() != null) {
cell5.setCellValue(record.getR6_EXCEEDING_DEPOSITORS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

Cell cell6 = row.createCell(6);
if (record.getR6_COVERED_AMOUNT_PCT() != null) {
cell6.setCellValue(record.getR6_COVERED_AMOUNT_PCT().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}


row = sheet.getRow(6);
cell2 = row.createCell(2);
if (record.getR7_TOTAL_DEPOSIT_AMOUNT() != null) {
cell2.setCellValue(record.getR7_TOTAL_DEPOSIT_AMOUNT().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}


cell3 = row.createCell(3);
if (record.getR7_COVERED_DEPOSITORS() != null) {
cell3.setCellValue(record.getR7_COVERED_DEPOSITORS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR7_COVERED_DEPOSITORS_PCT() != null) {
cell4.setCellValue(record.getR7_COVERED_DEPOSITORS_PCT().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR7_EXCEEDING_DEPOSITORS() != null) {
cell5.setCellValue(record.getR7_EXCEEDING_DEPOSITORS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR7_COVERED_AMOUNT_PCT() != null) {
cell6.setCellValue(record.getR7_COVERED_AMOUNT_PCT().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

row = sheet.getRow(7);
cell2 = row.createCell(2);
if (record.getR8_TOTAL_DEPOSIT_AMOUNT() != null) {
cell2.setCellValue(record.getR8_TOTAL_DEPOSIT_AMOUNT().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}


cell3 = row.createCell(3);
if (record.getR8_COVERED_DEPOSITORS() != null) {
cell3.setCellValue(record.getR8_COVERED_DEPOSITORS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR8_COVERED_DEPOSITORS_PCT() != null) {
cell4.setCellValue(record.getR8_COVERED_DEPOSITORS_PCT().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR8_EXCEEDING_DEPOSITORS() != null) {
cell5.setCellValue(record.getR8_EXCEEDING_DEPOSITORS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR8_COVERED_AMOUNT_PCT() != null) {
cell6.setCellValue(record.getR8_COVERED_AMOUNT_PCT().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

row = sheet.getRow(8);
cell2 = row.createCell(2);
if (record.getR9_TOTAL_DEPOSIT_AMOUNT() != null) {
cell2.setCellValue(record.getR9_TOTAL_DEPOSIT_AMOUNT().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}


cell3 = row.createCell(3);
if (record.getR9_COVERED_DEPOSITORS() != null) {
cell3.setCellValue(record.getR9_COVERED_DEPOSITORS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR9_COVERED_DEPOSITORS_PCT() != null) {
cell4.setCellValue(record.getR9_COVERED_DEPOSITORS_PCT().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR9_EXCEEDING_DEPOSITORS() != null) {
cell5.setCellValue(record.getR9_EXCEEDING_DEPOSITORS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR9_COVERED_AMOUNT_PCT() != null) {
cell6.setCellValue(record.getR9_COVERED_AMOUNT_PCT().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

row = sheet.getRow(9);
cell2 = row.createCell(2);
if (record.getR10_TOTAL_DEPOSIT_AMOUNT() != null) {
cell2.setCellValue(record.getR10_TOTAL_DEPOSIT_AMOUNT().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}


cell3 = row.createCell(3);
if (record.getR10_COVERED_DEPOSITORS() != null) {
cell3.setCellValue(record.getR10_COVERED_DEPOSITORS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR10_COVERED_DEPOSITORS_PCT() != null) {
cell4.setCellValue(record.getR10_COVERED_DEPOSITORS_PCT().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR10_EXCEEDING_DEPOSITORS() != null) {
cell5.setCellValue(record.getR10_EXCEEDING_DEPOSITORS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR10_COVERED_AMOUNT_PCT() != null) {
cell6.setCellValue(record.getR10_COVERED_AMOUNT_PCT().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

row = sheet.getRow(10);
cell2 = row.createCell(2);
if (record.getR11_TOTAL_DEPOSIT_AMOUNT() != null) {
cell2.setCellValue(record.getR11_TOTAL_DEPOSIT_AMOUNT().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}


cell3 = row.createCell(3);
if (record.getR11_COVERED_DEPOSITORS() != null) {
cell3.setCellValue(record.getR11_COVERED_DEPOSITORS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR11_COVERED_DEPOSITORS_PCT() != null) {
cell4.setCellValue(record.getR11_COVERED_DEPOSITORS_PCT().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR11_EXCEEDING_DEPOSITORS() != null) {
cell5.setCellValue(record.getR11_EXCEEDING_DEPOSITORS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR11_COVERED_AMOUNT_PCT() != null) {
cell6.setCellValue(record.getR11_COVERED_AMOUNT_PCT().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

row = sheet.getRow(11);
cell2 = row.createCell(2);
if (record.getR12_TOTAL_DEPOSIT_AMOUNT() != null) {
cell2.setCellValue(record.getR12_TOTAL_DEPOSIT_AMOUNT().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}


cell3 = row.createCell(3);
if (record.getR12_COVERED_DEPOSITORS() != null) {
cell3.setCellValue(record.getR12_COVERED_DEPOSITORS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR12_COVERED_DEPOSITORS_PCT() != null) {
cell4.setCellValue(record.getR12_COVERED_DEPOSITORS_PCT().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR12_EXCEEDING_DEPOSITORS() != null) {
cell5.setCellValue(record.getR12_EXCEEDING_DEPOSITORS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR12_COVERED_AMOUNT_PCT() != null) {
cell6.setCellValue(record.getR12_COVERED_AMOUNT_PCT().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

row = sheet.getRow(12);
cell2 = row.createCell(2);
if (record.getR13_TOTAL_DEPOSIT_AMOUNT() != null) {
cell2.setCellValue(record.getR13_TOTAL_DEPOSIT_AMOUNT().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}


cell3 = row.createCell(3);
if (record.getR13_COVERED_DEPOSITORS() != null) {
cell3.setCellValue(record.getR13_COVERED_DEPOSITORS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR13_COVERED_DEPOSITORS_PCT() != null) {
cell4.setCellValue(record.getR13_COVERED_DEPOSITORS_PCT().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR13_EXCEEDING_DEPOSITORS() != null) {
cell5.setCellValue(record.getR13_EXCEEDING_DEPOSITORS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR13_COVERED_AMOUNT_PCT() != null) {
cell6.setCellValue(record.getR13_COVERED_AMOUNT_PCT().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

row = sheet.getRow(13);
cell2 = row.createCell(2);
if (record.getR14_TOTAL_DEPOSIT_AMOUNT() != null) {
cell2.setCellValue(record.getR14_TOTAL_DEPOSIT_AMOUNT().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}


cell3 = row.createCell(3);
if (record.getR14_COVERED_DEPOSITORS() != null) {
cell3.setCellValue(record.getR14_COVERED_DEPOSITORS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR14_COVERED_DEPOSITORS_PCT() != null) {
cell4.setCellValue(record.getR14_COVERED_DEPOSITORS_PCT().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR14_EXCEEDING_DEPOSITORS() != null) {
cell5.setCellValue(record.getR14_EXCEEDING_DEPOSITORS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR14_COVERED_AMOUNT_PCT() != null) {
cell6.setCellValue(record.getR14_COVERED_AMOUNT_PCT().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
}

row = sheet.getRow(14);
cell2 = row.createCell(2);
if (record.getR15_TOTAL_DEPOSIT_AMOUNT() != null) {
cell2.setCellValue(record.getR15_TOTAL_DEPOSIT_AMOUNT().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}


cell3 = row.createCell(3);
if (record.getR15_COVERED_DEPOSITORS() != null) {
cell3.setCellValue(record.getR15_COVERED_DEPOSITORS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

cell4 = row.createCell(4);
if (record.getR15_COVERED_DEPOSITORS_PCT() != null) {
cell4.setCellValue(record.getR15_COVERED_DEPOSITORS_PCT().doubleValue());
cell4.setCellStyle(numberStyle);
} else {
cell4.setCellValue("");
cell4.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR15_EXCEEDING_DEPOSITORS() != null) {
cell5.setCellValue(record.getR15_EXCEEDING_DEPOSITORS().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}

cell6 = row.createCell(6);
if (record.getR15_COVERED_AMOUNT_PCT() != null) {
cell6.setCellValue(record.getR15_COVERED_AMOUNT_PCT().doubleValue());
cell6.setCellStyle(numberStyle);
} else {
cell6.setCellValue("");
cell6.setCellStyle(textStyle);
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
	
	public List<Object> getADISB2Archival() {
		List<Object> ADISB2Archivallist = new ArrayList<>();
		try {
			ADISB2Archivallist = ADISB2_Archival_Summary_Repo.getADISB2archival();
			
			System.out.println("countser" + ADISB2Archivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching ADISB2 Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return ADISB2Archivallist;
	}
	
	
	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			 String dtltype, String type, String version) {
try {
logger.info("Generating Excel for BRRS_ADISB2 ARCHIVAL Details...");
System.out.println("came to Detail download service");
if (type.equals("ARCHIVAL") & version != null) {

}
XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("ADISB2Detail");

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
List<ADISB2_Archival_Detail_Entity> reportData = ADISB2_Archival_Detail_Repo.getdatabydateList(parsedToDate,version);

if (reportData != null && !reportData.isEmpty()) {
int rowIndex = 1;
for (ADISB2_Archival_Detail_Entity item : reportData) {
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
logger.info("No data found for ADISB2 — only header will be written.");
}
// Write to byte[]
ByteArrayOutputStream bos = new ByteArrayOutputStream();
workbook.write(bos);
workbook.close();

logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
return bos.toByteArray();

} catch (Exception e) {
logger.error("Error generating ADISB2 Excel", e);
return new byte[0];
}
}
	
	public byte[] getExcelADISB2ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<ADISB2_Archival_Summary_Entity> dataList = ADISB2_Archival_Summary_Repo
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
			int startRow = 5;
			
			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

				ADISB2_Archival_Summary_Entity record1 = dataList.get(i);
				System.out.println("rownumber=" + startRow + i);
				Row row = sheet.getRow(startRow + i);
				if (row == null) {
				row = sheet.createRow(startRow + i);
				}
				
				//Cell26 - R5_EXCHANGE_RATE
				Cell cell2 = row.createCell(2);
				if (record1.getR6_TOTAL_DEPOSIT_AMOUNT() != null) {
				cell2.setCellValue(record1.getR6_TOTAL_DEPOSIT_AMOUNT().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}


				Cell cell3 = row.createCell(3);
				if (record1.getR6_COVERED_DEPOSITORS() != null) {
				cell3.setCellValue(record1.getR6_COVERED_DEPOSITORS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				Cell cell4 = row.createCell(4);
				if (record1.getR6_COVERED_DEPOSITORS_PCT() != null) {
				cell4.setCellValue(record1.getR6_COVERED_DEPOSITORS_PCT().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				Cell cell5 = row.createCell(5);
				if (record1.getR6_EXCEEDING_DEPOSITORS() != null) {
				cell5.setCellValue(record1.getR6_EXCEEDING_DEPOSITORS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				Cell cell6 = row.createCell(6);
				if (record1.getR6_COVERED_AMOUNT_PCT() != null) {
				cell6.setCellValue(record1.getR6_COVERED_AMOUNT_PCT().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}
				


				row = sheet.getRow(6);
				cell2 = row.createCell(2);
				if (record1.getR7_TOTAL_DEPOSIT_AMOUNT() != null) {
				cell2.setCellValue(record1.getR7_TOTAL_DEPOSIT_AMOUNT().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}


				cell3 = row.createCell(3);
				if (record1.getR7_COVERED_DEPOSITORS() != null) {
				cell3.setCellValue(record1.getR7_COVERED_DEPOSITORS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR7_COVERED_DEPOSITORS_PCT() != null) {
				cell4.setCellValue(record1.getR7_COVERED_DEPOSITORS_PCT().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR7_EXCEEDING_DEPOSITORS() != null) {
				cell5.setCellValue(record1.getR7_EXCEEDING_DEPOSITORS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR7_COVERED_AMOUNT_PCT() != null) {
				cell6.setCellValue(record1.getR7_COVERED_AMOUNT_PCT().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				row = sheet.getRow(7);
				cell2 = row.createCell(2);
				if (record1.getR8_TOTAL_DEPOSIT_AMOUNT() != null) {
				cell2.setCellValue(record1.getR8_TOTAL_DEPOSIT_AMOUNT().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}


				cell3 = row.createCell(3);
				if (record1.getR8_COVERED_DEPOSITORS() != null) {
				cell3.setCellValue(record1.getR8_COVERED_DEPOSITORS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR8_COVERED_DEPOSITORS_PCT() != null) {
				cell4.setCellValue(record1.getR8_COVERED_DEPOSITORS_PCT().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR8_EXCEEDING_DEPOSITORS() != null) {
				cell5.setCellValue(record1.getR8_EXCEEDING_DEPOSITORS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR8_COVERED_AMOUNT_PCT() != null) {
				cell6.setCellValue(record1.getR8_COVERED_AMOUNT_PCT().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				row = sheet.getRow(8);
				cell2 = row.createCell(2);
				if (record1.getR9_TOTAL_DEPOSIT_AMOUNT() != null) {
				cell2.setCellValue(record1.getR9_TOTAL_DEPOSIT_AMOUNT().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}


				cell3 = row.createCell(3);
				if (record1.getR9_COVERED_DEPOSITORS() != null) {
				cell3.setCellValue(record1.getR9_COVERED_DEPOSITORS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR9_COVERED_DEPOSITORS_PCT() != null) {
				cell4.setCellValue(record1.getR9_COVERED_DEPOSITORS_PCT().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR9_EXCEEDING_DEPOSITORS() != null) {
				cell5.setCellValue(record1.getR9_EXCEEDING_DEPOSITORS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR9_COVERED_AMOUNT_PCT() != null) {
				cell6.setCellValue(record1.getR9_COVERED_AMOUNT_PCT().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				row = sheet.getRow(9);
				cell2 = row.createCell(2);
				if (record1.getR10_TOTAL_DEPOSIT_AMOUNT() != null) {
				cell2.setCellValue(record1.getR10_TOTAL_DEPOSIT_AMOUNT().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}


				cell3 = row.createCell(3);
				if (record1.getR10_COVERED_DEPOSITORS() != null) {
				cell3.setCellValue(record1.getR10_COVERED_DEPOSITORS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR10_COVERED_DEPOSITORS_PCT() != null) {
				cell4.setCellValue(record1.getR10_COVERED_DEPOSITORS_PCT().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR10_EXCEEDING_DEPOSITORS() != null) {
				cell5.setCellValue(record1.getR10_EXCEEDING_DEPOSITORS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR10_COVERED_AMOUNT_PCT() != null) {
				cell6.setCellValue(record1.getR10_COVERED_AMOUNT_PCT().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				row = sheet.getRow(10);
				cell2 = row.createCell(2);
				if (record1.getR11_TOTAL_DEPOSIT_AMOUNT() != null) {
				cell2.setCellValue(record1.getR11_TOTAL_DEPOSIT_AMOUNT().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}


				cell3 = row.createCell(3);
				if (record1.getR11_COVERED_DEPOSITORS() != null) {
				cell3.setCellValue(record1.getR11_COVERED_DEPOSITORS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR11_COVERED_DEPOSITORS_PCT() != null) {
				cell4.setCellValue(record1.getR11_COVERED_DEPOSITORS_PCT().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR11_EXCEEDING_DEPOSITORS() != null) {
				cell5.setCellValue(record1.getR11_EXCEEDING_DEPOSITORS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR11_COVERED_AMOUNT_PCT() != null) {
				cell6.setCellValue(record1.getR11_COVERED_AMOUNT_PCT().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				row = sheet.getRow(11);
				cell2 = row.createCell(2);
				if (record1.getR12_TOTAL_DEPOSIT_AMOUNT() != null) {
				cell2.setCellValue(record1.getR12_TOTAL_DEPOSIT_AMOUNT().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}


				cell3 = row.createCell(3);
				if (record1.getR12_COVERED_DEPOSITORS() != null) {
				cell3.setCellValue(record1.getR12_COVERED_DEPOSITORS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR12_COVERED_DEPOSITORS_PCT() != null) {
				cell4.setCellValue(record1.getR12_COVERED_DEPOSITORS_PCT().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR12_EXCEEDING_DEPOSITORS() != null) {
				cell5.setCellValue(record1.getR12_EXCEEDING_DEPOSITORS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR12_COVERED_AMOUNT_PCT() != null) {
				cell6.setCellValue(record1.getR12_COVERED_AMOUNT_PCT().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				row = sheet.getRow(12);
				cell2 = row.createCell(2);
				if (record1.getR13_TOTAL_DEPOSIT_AMOUNT() != null) {
				cell2.setCellValue(record1.getR13_TOTAL_DEPOSIT_AMOUNT().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}


				cell3 = row.createCell(3);
				if (record1.getR13_COVERED_DEPOSITORS() != null) {
				cell3.setCellValue(record1.getR13_COVERED_DEPOSITORS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR13_COVERED_DEPOSITORS_PCT() != null) {
				cell4.setCellValue(record1.getR13_COVERED_DEPOSITORS_PCT().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR13_EXCEEDING_DEPOSITORS() != null) {
				cell5.setCellValue(record1.getR13_EXCEEDING_DEPOSITORS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR13_COVERED_AMOUNT_PCT() != null) {
				cell6.setCellValue(record1.getR13_COVERED_AMOUNT_PCT().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				row = sheet.getRow(13);
				cell2 = row.createCell(2);
				if (record1.getR14_TOTAL_DEPOSIT_AMOUNT() != null) {
				cell2.setCellValue(record1.getR14_TOTAL_DEPOSIT_AMOUNT().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}


				cell3 = row.createCell(3);
				if (record1.getR14_COVERED_DEPOSITORS() != null) {
				cell3.setCellValue(record1.getR14_COVERED_DEPOSITORS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR14_COVERED_DEPOSITORS_PCT() != null) {
				cell4.setCellValue(record1.getR14_COVERED_DEPOSITORS_PCT().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR14_EXCEEDING_DEPOSITORS() != null) {
				cell5.setCellValue(record1.getR14_EXCEEDING_DEPOSITORS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR14_COVERED_AMOUNT_PCT() != null) {
				cell6.setCellValue(record1.getR14_COVERED_AMOUNT_PCT().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
				}

				row = sheet.getRow(14);
				cell2 = row.createCell(2);
				if (record1.getR15_TOTAL_DEPOSIT_AMOUNT() != null) {
				cell2.setCellValue(record1.getR15_TOTAL_DEPOSIT_AMOUNT().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}


				cell3 = row.createCell(3);
				if (record1.getR15_COVERED_DEPOSITORS() != null) {
				cell3.setCellValue(record1.getR15_COVERED_DEPOSITORS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record1.getR15_COVERED_DEPOSITORS_PCT() != null) {
				cell4.setCellValue(record1.getR15_COVERED_DEPOSITORS_PCT().doubleValue());
				cell4.setCellStyle(numberStyle);
				} else {
				cell4.setCellValue("");
				cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR15_EXCEEDING_DEPOSITORS() != null) {
				cell5.setCellValue(record1.getR15_EXCEEDING_DEPOSITORS().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record1.getR15_COVERED_AMOUNT_PCT() != null) {
				cell6.setCellValue(record1.getR15_COVERED_AMOUNT_PCT().doubleValue());
				cell6.setCellStyle(numberStyle);
				} else {
				cell6.setCellValue("");
				cell6.setCellStyle(textStyle);
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
	    ModelAndView mv = new ModelAndView("BRRS/ADISB2"); // ✅ match the report name
	    System.out.println("Hello");
	    if (acctNo != null) {
	    	ADISB2_Detail_Entity adisb2Entity = ADISB2_Detail_Repo.findByAcctnumber(acctNo);
	        if (adisb2Entity != null && adisb2Entity.getReportDate() != null) {
	            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(adisb2Entity.getReportDate());
	            mv.addObject("asondate", formattedDate);
	        }
	        mv.addObject("Data", adisb2Entity);
	    }

	    mv.addObject("displaymode", "edit");
	    mv.addObject("formmode", formMode != null ? formMode : "edit");
	    return mv;
	}





	public ModelAndView updateDetailEdit(String acctNo, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/ADISB2"); // ✅ match the report name

	    if (acctNo != null) {
	        ADISB2_Detail_Entity la1Entity = ADISB2_Detail_Repo.findByAcctnumber(acctNo);
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

	        ADISB2_Detail_Entity existing = ADISB2_Detail_Repo.findByAcctnumber(acctNo);
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
	        	ADISB2_Detail_Repo.save(existing);
	            logger.info("Record updated successfully for account {}", acctNo);

	            // Format date for procedure
	            String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
	                    .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

	            // Run summary procedure after commit
	            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
	                @Override
	                public void afterCommit() {
	                    try {
	                        logger.info("Transaction committed — calling BRRS_ADISB2_SUMMARY_PROCEDURE({})",
	                                formattedDate);
	                        jdbcTemplate.update("BEGIN BRRS_ADISB2_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
	        logger.error("Error updating ADISB2 record", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error updating record: " + e.getMessage());
	    }
	}
	
	
	
	
}