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
import com.bornfire.brrs.entities.BRRS_EXPOSURES_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_EXPOSURES_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_EXPOSURES_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_EXPOSURES_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_FSI_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_FSI_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_FSI_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_FSI_Summary_Repo;
import com.bornfire.brrs.entities.EXPOSURES_Archival_Detail_Entity;
import com.bornfire.brrs.entities.EXPOSURES_Archival_Summary_Entity;
import com.bornfire.brrs.entities.EXPOSURES_Detail_Entity;
import com.bornfire.brrs.entities.EXPOSURES_Summary_Entity;
import com.bornfire.brrs.entities.FORMAT_NEW_CPR_Detail_Entity;
import com.bornfire.brrs.entities.FSI_Archival_Detail_Entity;
import com.bornfire.brrs.entities.FSI_Archival_Summary_Entity;
import com.bornfire.brrs.entities.FSI_Detail_Entity;
import com.bornfire.brrs.entities.FSI_Summary_Entity;
//import com.bornfire.brrs.entities.FSI_Manual_Summary_Entity;
//import com.bornfire.brrs.entities.FSI_Manual_Archival_Summary_Entity;
//import com.bornfire.brrs.entities.BRRS_FSI_Manual_Summary_Repo;
//import com.bornfire.brrs.entities.BRRS_FSI_Manual_Archival_Summary_Repo;

@Component
@Service
public class BRRS_EXPOSURES_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_EXPOSURES_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_EXPOSURES_Detail_Repo EXPOSURES_Detail_Repo;

	@Autowired
	BRRS_EXPOSURES_Summary_Repo EXPOSURES_Summary_Repo;

	
	@Autowired
	BRRS_EXPOSURES_Archival_Detail_Repo EXPOSURES_Archival_Detail_Repo;

	@Autowired
	BRRS_EXPOSURES_Archival_Summary_Repo EXPOSURES_Archival_Summary_Repo;



	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getEXPOSURESView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type,  String version) {
		ModelAndView mv = new ModelAndView();
//		Session hs = sessionFactory.getCurrentSession();
//		int pageSize = pageable.getPageSize();
//		int currentPage = pageable.getPageNumber();
//		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<EXPOSURES_Archival_Summary_Entity> T1Master = new ArrayList<EXPOSURES_Archival_Summary_Entity>();
//			List<FSI_Manual_Archival_Summary_Entity> T2Master = new ArrayList<FSI_Manual_Archival_Summary_Entity>();

			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = EXPOSURES_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
//				T2Master = BRRS_FSI_Manual_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
				

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
//			mv.addObject("reportsummary1", T2Master);
            
		} else {
			List<EXPOSURES_Summary_Entity> T1Master = new ArrayList<EXPOSURES_Summary_Entity>();
//			List<FSI_Manual_Summary_Entity> T2Master = new ArrayList<FSI_Manual_Summary_Entity>();
			
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = EXPOSURES_Summary_Repo.getdatabydateList(dateformat.parse(todate));
//				T2Master = BRRS_FSI_Manual_Summary_Repo.getdatabydateList(dateformat.parse(todate));
				

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
//			mv.addObject("reportsummary1", T2Master);
            
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/EXPOSURES");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

	public ModelAndView getEXPOSUREScurrentDtl(String reportId, String fromdate, String todate, String currency,
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
			List<EXPOSURES_Archival_Detail_Entity> T1Dt1;
			if (rowId != null && columnId != null) {
				T1Dt1 = EXPOSURES_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
			} else {
				T1Dt1 = EXPOSURES_Archival_Detail_Repo.getdatabydateList(parsedDate, version);					
			}

			mv.addObject("reportdetails", T1Dt1);
			mv.addObject("reportmaster12", T1Dt1);
			System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

		} else {
			// 🔹 Current branch
			List<EXPOSURES_Detail_Entity> T1Dt1;
			if (rowId != null && columnId != null) {
				T1Dt1 = EXPOSURES_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
			} else {
				T1Dt1 = EXPOSURES_Detail_Repo.getdatabydateList(parsedDate);
				totalPages = EXPOSURES_Detail_Repo.getdatacount(parsedDate);
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
	mv.setViewName("BRRS/EXPOSURES");
	mv.addObject("displaymode", "Details");
	mv.addObject("currentPage", currentPage);
	System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
	mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
	mv.addObject("reportsflag", "reportsflag");
	mv.addObject("menu", reportId);

	return mv;
}
	
	public byte[] getEXPOSURESDetailExcel(String filename, String fromdate, String todate, String currency,
			   String dtltype, String type, String version) {
try {
logger.info("Generating Excel for EXPOSURES Details...");
System.out.println("came to Detail download service");


if (type.equals("ARCHIVAL") & version != null) {
byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
version);
return ARCHIVALreport;
}

XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("FSIDetail");

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
List<EXPOSURES_Detail_Entity> reportData = EXPOSURES_Detail_Repo.getdatabydateList(parsedToDate);

if (reportData != null && !reportData.isEmpty()) {
int rowIndex = 1;
for (EXPOSURES_Detail_Entity item : reportData) {
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
	logger.info("No data found for EXPOSURES — only header will be written.");
}

//Write to byte[]
ByteArrayOutputStream bos = new ByteArrayOutputStream();
workbook.write(bos);
workbook.close();

logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
return bos.toByteArray();

} catch (Exception e) {
logger.error("Error generating EXPOSURES Excel", e);
return new byte[0];
}
}

	public byte[] getEXPOSURESExcel(String filename, String reportId, String fromdate, String todate, String currency,
			 String dtltype, String type, String version) throws Exception {
logger.info("Service: Starting Excel generation process in memory.");
logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

//Convert string to Date
Date reportDate = dateformat.parse(todate);

//ARCHIVAL check
if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
logger.info("Service: Generating ARCHIVAL report for version {}", version);
return getExcelEXPOSURESARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
}
//RESUB check
else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
logger.info("Service: Generating RESUB report for version {}", version);


List<EXPOSURES_Archival_Summary_Entity> T1Master =
EXPOSURES_Archival_Summary_Repo.getdatabydateListarchival(reportDate, version);


}


//Default (LIVE) case
List<EXPOSURES_Summary_Entity> dataList1 = EXPOSURES_Summary_Repo.getdatabydateList(reportDate);

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

EXPOSURES_Summary_Entity record = dataList1.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}

Cell cell2 = row.createCell(2);
if (record.getR4_AMOUNT() != null) {
 cell2.setCellValue(record.getR4_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

row = sheet.getRow(5);
cell2 = row.createCell(2);
if (record.getR6_AMOUNT() != null) {
 cell2.setCellValue(record.getR6_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

Cell cell3 = row.createCell(3);
if (record.getR6_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR6_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}

//===== R7 =====
row = sheet.getRow(6);
cell2 = row.createCell(2);
if (record.getR7_AMOUNT() != null) {
 cell2.setCellValue(record.getR7_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR7_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR7_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R8 =====
row = sheet.getRow(7);
cell2 = row.createCell(2);
if (record.getR8_AMOUNT() != null) {
 cell2.setCellValue(record.getR8_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR8_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR8_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R9 =====
row = sheet.getRow(8);
cell2 = row.createCell(2);
if (record.getR9_AMOUNT() != null) {
 cell2.setCellValue(record.getR9_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR9_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR9_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R10 =====
row = sheet.getRow(9);
cell2 = row.createCell(2);
if (record.getR10_AMOUNT() != null) {
 cell2.setCellValue(record.getR10_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR10_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR10_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R11 =====
row = sheet.getRow(10);
cell2 = row.createCell(2);
if (record.getR11_AMOUNT() != null) {
 cell2.setCellValue(record.getR11_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR11_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR11_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R12 =====
row = sheet.getRow(11);
cell2 = row.createCell(2);
if (record.getR12_AMOUNT() != null) {
 cell2.setCellValue(record.getR12_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR12_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR12_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R13 =====
row = sheet.getRow(12);
cell2 = row.createCell(2);
if (record.getR13_AMOUNT() != null) {
 cell2.setCellValue(record.getR13_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR13_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR13_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R14 =====
row = sheet.getRow(13);
cell2 = row.createCell(2);
if (record.getR14_AMOUNT() != null) {
 cell2.setCellValue(record.getR14_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR14_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR14_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R15 =====
row = sheet.getRow(14);
cell2 = row.createCell(2);
if (record.getR15_AMOUNT() != null) {
 cell2.setCellValue(record.getR15_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR15_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR15_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R16 =====
row = sheet.getRow(15);
cell2 = row.createCell(2);
if (record.getR16_AMOUNT() != null) {
 cell2.setCellValue(record.getR16_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR16_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR16_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R17 =====
row = sheet.getRow(16);
cell2 = row.createCell(2);
if (record.getR17_AMOUNT() != null) {
 cell2.setCellValue(record.getR17_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR17_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR17_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R18 =====
row = sheet.getRow(17);
cell2 = row.createCell(2);
if (record.getR18_AMOUNT() != null) {
 cell2.setCellValue(record.getR18_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR18_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR18_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R19 =====
row = sheet.getRow(18);
cell2 = row.createCell(2);
if (record.getR19_AMOUNT() != null) {
 cell2.setCellValue(record.getR19_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR19_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR19_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R20 =====
row = sheet.getRow(19);
cell2 = row.createCell(2);
if (record.getR20_AMOUNT() != null) {
 cell2.setCellValue(record.getR20_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR20_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR20_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R21 =====
row = sheet.getRow(20);
cell2 = row.createCell(2);
if (record.getR21_AMOUNT() != null) {
 cell2.setCellValue(record.getR21_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR21_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR21_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R22 =====
row = sheet.getRow(21);
cell2 = row.createCell(2);
if (record.getR22_AMOUNT() != null) {
 cell2.setCellValue(record.getR22_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR22_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR22_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R23 =====
row = sheet.getRow(22);
cell2 = row.createCell(2);
if (record.getR23_AMOUNT() != null) {
 cell2.setCellValue(record.getR23_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR23_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR23_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R24 =====
row = sheet.getRow(23);
cell2 = row.createCell(2);
if (record.getR24_AMOUNT() != null) {
 cell2.setCellValue(record.getR24_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR24_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR24_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R25 =====
row = sheet.getRow(24);
cell2 = row.createCell(2);
if (record.getR25_AMOUNT() != null) {
 cell2.setCellValue(record.getR25_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR25_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR25_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R31 =====
row = sheet.getRow(30);
cell2 = row.createCell(2);
if (record.getR31_AMOUNT() != null) {
 cell2.setCellValue(record.getR31_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR31_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR31_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R32 =====
row = sheet.getRow(31);
cell2 = row.createCell(2);
if (record.getR32_AMOUNT() != null) {
 cell2.setCellValue(record.getR32_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR32_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR32_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R33 =====
row = sheet.getRow(32);
cell2 = row.createCell(2);
if (record.getR33_AMOUNT() != null) {
 cell2.setCellValue(record.getR33_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR33_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR33_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R34 =====
row = sheet.getRow(33);
cell2 = row.createCell(2);
if (record.getR34_AMOUNT() != null) {
 cell2.setCellValue(record.getR34_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR34_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR34_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R35 =====
row = sheet.getRow(34);
cell2 = row.createCell(2);
if (record.getR35_AMOUNT() != null) {
 cell2.setCellValue(record.getR35_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR35_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR35_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R36 =====
row = sheet.getRow(35);
cell2 = row.createCell(2);
if (record.getR36_AMOUNT() != null) {
 cell2.setCellValue(record.getR36_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR36_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR36_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R37 =====
row = sheet.getRow(36);
cell2 = row.createCell(2);
if (record.getR37_AMOUNT() != null) {
 cell2.setCellValue(record.getR37_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR37_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR37_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R38 =====
row = sheet.getRow(37);
cell2 = row.createCell(2);
if (record.getR38_AMOUNT() != null) {
 cell2.setCellValue(record.getR38_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR38_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR38_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R39 =====
row = sheet.getRow(38);
cell2 = row.createCell(2);
if (record.getR39_AMOUNT() != null) {
 cell2.setCellValue(record.getR39_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR39_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR39_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R40 =====
row = sheet.getRow(39);
cell2 = row.createCell(2);
if (record.getR40_AMOUNT() != null) {
 cell2.setCellValue(record.getR40_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR40_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR40_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R41 =====
row = sheet.getRow(40);
cell2 = row.createCell(2);
if (record.getR41_AMOUNT() != null) {
 cell2.setCellValue(record.getR41_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR41_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR41_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R42 =====
row = sheet.getRow(41);
cell2 = row.createCell(2);
if (record.getR42_AMOUNT() != null) {
 cell2.setCellValue(record.getR42_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR42_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR42_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R43 =====
row = sheet.getRow(42);
cell2 = row.createCell(2);
if (record.getR43_AMOUNT() != null) {
 cell2.setCellValue(record.getR43_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR43_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR43_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R44 =====
row = sheet.getRow(43);
cell2 = row.createCell(2);
if (record.getR44_AMOUNT() != null) {
 cell2.setCellValue(record.getR44_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR44_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR44_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R45 =====
row = sheet.getRow(44);
cell2 = row.createCell(2);
if (record.getR45_AMOUNT() != null) {
 cell2.setCellValue(record.getR45_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR45_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR45_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R46 =====
row = sheet.getRow(45);
cell2 = row.createCell(2);
if (record.getR46_AMOUNT() != null) {
 cell2.setCellValue(record.getR46_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR46_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR46_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R47 =====
row = sheet.getRow(46);
cell2 = row.createCell(2);
if (record.getR47_AMOUNT() != null) {
 cell2.setCellValue(record.getR47_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR47_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR47_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R48 =====
row = sheet.getRow(47);
cell2 = row.createCell(2);
if (record.getR48_AMOUNT() != null) {
 cell2.setCellValue(record.getR48_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR48_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR48_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R49 =====
row = sheet.getRow(48);
cell2 = row.createCell(2);
if (record.getR49_AMOUNT() != null) {
 cell2.setCellValue(record.getR49_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR49_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR49_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R50 =====
row = sheet.getRow(49);
cell2 = row.createCell(2);
if (record.getR50_AMOUNT() != null) {
 cell2.setCellValue(record.getR50_AMOUNT().doubleValue());
 cell2.setCellStyle(numberStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR50_CAPITAL_FUNDS() != null) {
 cell3.setCellValue(record.getR50_CAPITAL_FUNDS().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}

//===== R58 =====
row = sheet.getRow(57);
cell3 = row.createCell(3);
if (record.getR58_AMOUNT() != null) {
 cell3.setCellValue(record.getR58_AMOUNT().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R59 =====
row = sheet.getRow(58);
cell3 = row.createCell(3);
if (record.getR59_AMOUNT() != null) {
 cell3.setCellValue(record.getR59_AMOUNT().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R60 =====
row = sheet.getRow(59);
cell3 = row.createCell(3);
if (record.getR60_AMOUNT() != null) {
 cell3.setCellValue(record.getR60_AMOUNT().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}


//===== R61 =====
row = sheet.getRow(61);
cell3 = row.createCell(3);
if (record.getR62_AMOUNT() != null) {
 cell3.setCellValue(record.getR62_AMOUNT().doubleValue());
 cell3.setCellStyle(numberStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
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
	
	public List<Object> getEXPOSURESArchival() {
		List<Object> EXPOSURESArchivallist = new ArrayList<>();
		try {
			EXPOSURESArchivallist = EXPOSURES_Archival_Summary_Repo.getEXPOSURESarchival();
//			FSIArchivallist = BRRS_FSI_Manual_Archival_Summary_Repo.getFSIarchival();
			System.out.println("countser" + EXPOSURESArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching EXPOSURES Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return EXPOSURESArchivallist;
	}
	
	
	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			 String dtltype, String type, String version) {
try {
logger.info("Generating Excel for BRRS_EXPOSURES ARCHIVAL Details...");
System.out.println("came to Detail download service");
if (type.equals("ARCHIVAL") & version != null) {

}
XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("EXPOSURESDetail");

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
List<EXPOSURES_Archival_Detail_Entity> reportData = EXPOSURES_Archival_Detail_Repo.getdatabydateList(parsedToDate,version);

if (reportData != null && !reportData.isEmpty()) {
int rowIndex = 1;
for (EXPOSURES_Archival_Detail_Entity item : reportData) {
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
logger.info("No data found for EXPOSURES — only header will be written.");
}
// Write to byte[]
ByteArrayOutputStream bos = new ByteArrayOutputStream();
workbook.write(bos);
workbook.close();

logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
return bos.toByteArray();

} catch (Exception e) {
logger.error("Error generating EXPOSURES Excel", e);
return new byte[0];
}
}

	public byte[] getExcelEXPOSURESARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<EXPOSURES_Archival_Summary_Entity> dataList = EXPOSURES_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for EXPOSURES report. Returning empty result.");
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

				EXPOSURES_Archival_Summary_Entity record1 = dataList.get(i);
				System.out.println("rownumber=" + startRow + i);
				Row row = sheet.getRow(startRow + i);
				if (row == null) {
				row = sheet.createRow(startRow + i);
				}
				
				Cell cell2 = row.createCell(2);
				if (record1.getR4_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR4_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				row = sheet.getRow(5);
				cell2 = row.createCell(2);
				if (record1.getR6_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR6_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				Cell cell3 = row.createCell(3);
				if (record1.getR6_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR6_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}

				//===== R7 =====
				row = sheet.getRow(6);
				cell2 = row.createCell(2);
				if (record1.getR7_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR7_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR7_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR7_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R8 =====
				row = sheet.getRow(7);
				cell2 = row.createCell(2);
				if (record1.getR8_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR8_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR8_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR8_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R9 =====
				row = sheet.getRow(8);
				cell2 = row.createCell(2);
				if (record1.getR9_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR9_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR9_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR9_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R10 =====
				row = sheet.getRow(9);
				cell2 = row.createCell(2);
				if (record1.getR10_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR10_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR10_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR10_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R11 =====
				row = sheet.getRow(10);
				cell2 = row.createCell(2);
				if (record1.getR11_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR11_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR11_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR11_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R12 =====
				row = sheet.getRow(11);
				cell2 = row.createCell(2);
				if (record1.getR12_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR12_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR12_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR12_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R13 =====
				row = sheet.getRow(12);
				cell2 = row.createCell(2);
				if (record1.getR13_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR13_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR13_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR13_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R14 =====
				row = sheet.getRow(13);
				cell2 = row.createCell(2);
				if (record1.getR14_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR14_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR14_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR14_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R15 =====
				row = sheet.getRow(14);
				cell2 = row.createCell(2);
				if (record1.getR15_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR15_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR15_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR15_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R16 =====
				row = sheet.getRow(15);
				cell2 = row.createCell(2);
				if (record1.getR16_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR16_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR16_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR16_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R17 =====
				row = sheet.getRow(16);
				cell2 = row.createCell(2);
				if (record1.getR17_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR17_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR17_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR17_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R18 =====
				row = sheet.getRow(17);
				cell2 = row.createCell(2);
				if (record1.getR18_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR18_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR18_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR18_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R19 =====
				row = sheet.getRow(18);
				cell2 = row.createCell(2);
				if (record1.getR19_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR19_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR19_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR19_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R20 =====
				row = sheet.getRow(19);
				cell2 = row.createCell(2);
				if (record1.getR20_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR20_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR20_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR20_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R21 =====
				row = sheet.getRow(20);
				cell2 = row.createCell(2);
				if (record1.getR21_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR21_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR21_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR21_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R22 =====
				row = sheet.getRow(21);
				cell2 = row.createCell(2);
				if (record1.getR22_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR22_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR22_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR22_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R23 =====
				row = sheet.getRow(22);
				cell2 = row.createCell(2);
				if (record1.getR23_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR23_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR23_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR23_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R24 =====
				row = sheet.getRow(23);
				cell2 = row.createCell(2);
				if (record1.getR24_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR24_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR24_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR24_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R25 =====
				row = sheet.getRow(24);
				cell2 = row.createCell(2);
				if (record1.getR25_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR25_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR25_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR25_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R31 =====
				row = sheet.getRow(30);
				cell2 = row.createCell(2);
				if (record1.getR31_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR31_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR31_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR31_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R32 =====
				row = sheet.getRow(31);
				cell2 = row.createCell(2);
				if (record1.getR32_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR32_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR32_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR32_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R33 =====
				row = sheet.getRow(32);
				cell2 = row.createCell(2);
				if (record1.getR33_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR33_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR33_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR33_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R34 =====
				row = sheet.getRow(33);
				cell2 = row.createCell(2);
				if (record1.getR34_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR34_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR34_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR34_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R35 =====
				row = sheet.getRow(34);
				cell2 = row.createCell(2);
				if (record1.getR35_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR35_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR35_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR35_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R36 =====
				row = sheet.getRow(35);
				cell2 = row.createCell(2);
				if (record1.getR36_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR36_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR36_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR36_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R37 =====
				row = sheet.getRow(36);
				cell2 = row.createCell(2);
				if (record1.getR37_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR37_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR37_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR37_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R38 =====
				row = sheet.getRow(37);
				cell2 = row.createCell(2);
				if (record1.getR38_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR38_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR38_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR38_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R39 =====
				row = sheet.getRow(38);
				cell2 = row.createCell(2);
				if (record1.getR39_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR39_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR39_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR39_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R40 =====
				row = sheet.getRow(39);
				cell2 = row.createCell(2);
				if (record1.getR40_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR40_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR40_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR40_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R41 =====
				row = sheet.getRow(40);
				cell2 = row.createCell(2);
				if (record1.getR41_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR41_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR41_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR41_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R42 =====
				row = sheet.getRow(41);
				cell2 = row.createCell(2);
				if (record1.getR42_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR42_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR42_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR42_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R43 =====
				row = sheet.getRow(42);
				cell2 = row.createCell(2);
				if (record1.getR43_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR43_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR43_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR43_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R44 =====
				row = sheet.getRow(43);
				cell2 = row.createCell(2);
				if (record1.getR44_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR44_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR44_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR44_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R45 =====
				row = sheet.getRow(44);
				cell2 = row.createCell(2);
				if (record1.getR45_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR45_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR45_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR45_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R46 =====
				row = sheet.getRow(45);
				cell2 = row.createCell(2);
				if (record1.getR46_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR46_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR46_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR46_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R47 =====
				row = sheet.getRow(46);
				cell2 = row.createCell(2);
				if (record1.getR47_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR47_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR47_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR47_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R48 =====
				row = sheet.getRow(47);
				cell2 = row.createCell(2);
				if (record1.getR48_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR48_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR48_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR48_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R49 =====
				row = sheet.getRow(48);
				cell2 = row.createCell(2);
				if (record1.getR49_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR49_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR49_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR49_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R50 =====
				row = sheet.getRow(49);
				cell2 = row.createCell(2);
				if (record1.getR50_AMOUNT() != null) {
				 cell2.setCellValue(record1.getR50_AMOUNT().doubleValue());
				 cell2.setCellStyle(numberStyle);
				} else {
				 cell2.setCellValue("");
				 cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR50_CAPITAL_FUNDS() != null) {
				 cell3.setCellValue(record1.getR50_CAPITAL_FUNDS().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}

				//===== R58 =====
				row = sheet.getRow(57);
				cell3 = row.createCell(3);
				if (record1.getR58_AMOUNT() != null) {
				 cell3.setCellValue(record1.getR58_AMOUNT().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R59 =====
				row = sheet.getRow(58);
				cell3 = row.createCell(3);
				if (record1.getR59_AMOUNT() != null) {
				 cell3.setCellValue(record1.getR59_AMOUNT().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R60 =====
				row = sheet.getRow(59);
				cell3 = row.createCell(3);
				if (record1.getR60_AMOUNT() != null) {
				 cell3.setCellValue(record1.getR60_AMOUNT().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
				}


				//===== R61 =====
				row = sheet.getRow(61);
				cell3 = row.createCell(3);
				if (record1.getR62_AMOUNT() != null) {
				 cell3.setCellValue(record1.getR62_AMOUNT().doubleValue());
				 cell3.setCellStyle(numberStyle);
				} else {
				 cell3.setCellValue("");
				 cell3.setCellStyle(textStyle);
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
	    ModelAndView mv = new ModelAndView("BRRS/EXPOSURES"); // ✅ match the report name
	    System.out.println("Hello");
	    if (acctNo != null) {
	    	EXPOSURES_Detail_Entity fsiEntity = EXPOSURES_Detail_Repo.findByAcctnumber(acctNo);
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
	    ModelAndView mv = new ModelAndView("BRRS/EXPOSURES"); // ✅ match the report name

	    if (acctNo != null) {
	    	EXPOSURES_Detail_Entity la1Entity = EXPOSURES_Detail_Repo.findByAcctnumber(acctNo);
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

	        EXPOSURES_Detail_Entity existing = EXPOSURES_Detail_Repo.findByAcctnumber(acctNo);
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
	        	EXPOSURES_Detail_Repo.save(existing);
	            logger.info("Record updated successfully for account {}", acctNo);

	            // Format date for procedure
	            String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
	                    .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

	            // Run summary procedure after commit
	            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
	                @Override
	                public void afterCommit() {
	                    try {
	                        logger.info("Transaction committed — calling BRRS_EXPOSURES_SUMMARY_PROCEDURE({})",
	                                formattedDate);
	                        jdbcTemplate.update("BEGIN BRRS_EXPOSURES_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
	        logger.error("Error updating EXPOSURES record", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error updating record: " + e.getMessage());
	    }
	}
	
					
			
}
