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
import com.bornfire.brrs.entities.BRRS_RWA_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_RWA_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_RWA_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_RWA_Summary_Repo;
import com.bornfire.brrs.entities.EXPOSURES_Archival_Detail_Entity;
import com.bornfire.brrs.entities.EXPOSURES_Archival_Summary_Entity;
import com.bornfire.brrs.entities.EXPOSURES_Detail_Entity;
import com.bornfire.brrs.entities.EXPOSURES_Summary_Entity;
import com.bornfire.brrs.entities.FORMAT_NEW_CPR_Detail_Entity;
import com.bornfire.brrs.entities.FSI_Archival_Detail_Entity;
import com.bornfire.brrs.entities.FSI_Archival_Summary_Entity;
import com.bornfire.brrs.entities.FSI_Detail_Entity;
import com.bornfire.brrs.entities.FSI_Summary_Entity;
import com.bornfire.brrs.entities.RWA_Archival_Detail_Entity;
//import com.bornfire.brrs.entities.FSI_Manual_Summary_Entity;
//import com.bornfire.brrs.entities.FSI_Manual_Archival_Summary_Entity;
//import com.bornfire.brrs.entities.BRRS_FSI_Manual_Summary_Repo;
//import com.bornfire.brrs.entities.BRRS_FSI_Manual_Archival_Summary_Repo;
import com.bornfire.brrs.entities.RWA_Archival_Summary_Entity;
import com.bornfire.brrs.entities.RWA_Detail_Entity;
import com.bornfire.brrs.entities.RWA_Summary_Entity;

@Component
@Service
public class BRRS_RWA_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_RWA_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_RWA_Detail_Repo RWA_Detail_Repo;

	@Autowired
	BRRS_RWA_Summary_Repo RWA_Summary_Repo;

	
	@Autowired
	BRRS_RWA_Archival_Detail_Repo RWA_Archival_Detail_Repo;

	@Autowired
	BRRS_RWA_Archival_Summary_Repo RWA_Archival_Summary_Repo;



	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getRWAView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type,  String version) {
		ModelAndView mv = new ModelAndView();
//		Session hs = sessionFactory.getCurrentSession();
//		int pageSize = pageable.getPageSize();
//		int currentPage = pageable.getPageNumber();
//		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<RWA_Archival_Summary_Entity> T1Master = new ArrayList<RWA_Archival_Summary_Entity>();
//			List<FSI_Manual_Archival_Summary_Entity> T2Master = new ArrayList<FSI_Manual_Archival_Summary_Entity>();

			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = RWA_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
//				T2Master = BRRS_FSI_Manual_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
				

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
//			mv.addObject("reportsummary1", T2Master);
            
		} else {
			List<RWA_Summary_Entity> T1Master = new ArrayList<RWA_Summary_Entity>();
//			List<FSI_Manual_Summary_Entity> T2Master = new ArrayList<FSI_Manual_Summary_Entity>();
			
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = RWA_Summary_Repo.getdatabydateList(dateformat.parse(todate));
//				T2Master = BRRS_FSI_Manual_Summary_Repo.getdatabydateList(dateformat.parse(todate));
				

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
//			mv.addObject("reportsummary1", T2Master);
            
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/RWA");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

	public ModelAndView getRWAcurrentDtl(String reportId, String fromdate, String todate, String currency,
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
			List<RWA_Archival_Detail_Entity> T1Dt1;
			if (rowId != null && columnId != null) {
				T1Dt1 = RWA_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
			} else {
				T1Dt1 = RWA_Archival_Detail_Repo.getdatabydateList(parsedDate, version);					
			}

			mv.addObject("reportdetails", T1Dt1);
			mv.addObject("reportmaster12", T1Dt1);
			System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

		} else {
			// 🔹 Current branch
			List<RWA_Detail_Entity> T1Dt1;
			if (rowId != null && columnId != null) {
				T1Dt1 = RWA_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
			} else {
				T1Dt1 = RWA_Detail_Repo.getdatabydateList(parsedDate);
				totalPages = RWA_Detail_Repo.getdatacount(parsedDate);
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
	mv.setViewName("BRRS/RWA");
	mv.addObject("displaymode", "Details");
	mv.addObject("currentPage", currentPage);
	System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
	mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
	mv.addObject("reportsflag", "reportsflag");
	mv.addObject("menu", reportId);

	return mv;
}
	
	
	public byte[] getRWADetailExcel(String filename, String fromdate, String todate, String currency,
			   String dtltype, String type, String version) {
try {
logger.info("Generating Excel for RWA Details...");
System.out.println("came to Detail download service");


if (type.equals("ARCHIVAL") & version != null) {
byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
version);
return ARCHIVALreport;
}

XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("RWADetail");

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
List<RWA_Detail_Entity> reportData = RWA_Detail_Repo.getdatabydateList(parsedToDate);

if (reportData != null && !reportData.isEmpty()) {
int rowIndex = 1;
for (RWA_Detail_Entity item : reportData) {
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
	logger.info("No data found for RWA — only header will be written.");
}

//Write to byte[]
ByteArrayOutputStream bos = new ByteArrayOutputStream();
workbook.write(bos);
workbook.close();

logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
return bos.toByteArray();

} catch (Exception e) {
logger.error("Error generating RWA Excel", e);
return new byte[0];
}
}

	public byte[] getRWAExcel(String filename, String reportId, String fromdate, String todate, String currency,
			 String dtltype, String type, String version) throws Exception {
logger.info("Service: Starting Excel generation process in memory.");
logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

//Convert string to Date
Date reportDate = dateformat.parse(todate);

//ARCHIVAL check
if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
logger.info("Service: Generating ARCHIVAL report for version {}", version);
return getExcelRWAARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
}
//RESUB check
else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
logger.info("Service: Generating RESUB report for version {}", version);


List<RWA_Archival_Summary_Entity> T1Master =
RWA_Archival_Summary_Repo.getdatabydateListarchival(reportDate, version);


}


//Default (LIVE) case
List<RWA_Summary_Entity> dataList1 = RWA_Summary_Repo.getdatabydateList(reportDate);

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

RWA_Summary_Entity record = dataList1.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}

Cell cell2 = row.createCell(2);
if (record.getR8_BOOK_VALUE() != null) {
cell2.setCellValue(record.getR8_BOOK_VALUE().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

Cell cell3 = row.createCell(3);
if (record.getR8_MARGINS() != null) {
cell3.setCellValue(record.getR8_MARGINS().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

Cell cell5 = row.createCell(5);
if (record.getR8_RW() != null) {
cell5.setCellValue(record.getR8_RW().doubleValue());
cell5.setCellStyle(numberStyle);
} else {
cell5.setCellValue("");
cell5.setCellStyle(textStyle);
}


row = sheet.getRow(8);
/* ===================== R9 ===================== */
cell2 = row.createCell(2);
if (record.getR9_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR9_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR9_MARGINS() != null) {
    cell3.setCellValue(record.getR9_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR9_RW() != null) {
    cell5.setCellValue(record.getR9_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(9);
/* ===================== R10 ===================== */
cell2 = row.createCell(2);
if (record.getR10_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR10_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR10_MARGINS() != null) {
    cell3.setCellValue(record.getR10_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR10_RW() != null) {
    cell5.setCellValue(record.getR10_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(10);
/* ===================== R11 ===================== */
cell2 = row.createCell(2);
if (record.getR11_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR11_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR11_MARGINS() != null) {
    cell3.setCellValue(record.getR11_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR11_RW() != null) {
    cell5.setCellValue(record.getR11_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(11);
/* ===================== R12 ===================== */
cell2 = row.createCell(2);
if (record.getR12_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR12_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR12_MARGINS() != null) {
    cell3.setCellValue(record.getR12_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR12_RW() != null) {
    cell5.setCellValue(record.getR12_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(12);
/* ===================== R13 ===================== */
cell2 = row.createCell(2);
if (record.getR13_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR13_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR13_MARGINS() != null) {
    cell3.setCellValue(record.getR13_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR13_RW() != null) {
    cell5.setCellValue(record.getR13_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(13);
/* ===================== R14 ===================== */
cell2 = row.createCell(2);
if (record.getR14_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR14_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR14_MARGINS() != null) {
    cell3.setCellValue(record.getR14_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR14_RW() != null) {
    cell5.setCellValue(record.getR14_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(14);
/* ===================== R15 ===================== */
cell2 = row.createCell(2);
if (record.getR15_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR15_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR15_MARGINS() != null) {
    cell3.setCellValue(record.getR15_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR15_RW() != null) {
    cell5.setCellValue(record.getR15_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(15);
/* ===================== R16 ===================== */
cell2 = row.createCell(2);
if (record.getR16_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR16_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR16_MARGINS() != null) {
    cell3.setCellValue(record.getR16_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR16_RW() != null) {
    cell5.setCellValue(record.getR16_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(16);
/* ===================== R17 ===================== */
cell2 = row.createCell(2);
if (record.getR17_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR17_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR17_MARGINS() != null) {
    cell3.setCellValue(record.getR17_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR17_RW() != null) {
    cell5.setCellValue(record.getR17_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(17);
/* ===================== R18 ===================== */
cell2 = row.createCell(2);
if (record.getR18_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR18_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR18_MARGINS() != null) {
    cell3.setCellValue(record.getR18_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR18_RW() != null) {
    cell5.setCellValue(record.getR18_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(18);
/* ===================== R19 ===================== */
cell2 = row.createCell(2);
if (record.getR19_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR19_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR19_MARGINS() != null) {
    cell3.setCellValue(record.getR19_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR19_RW() != null) {
    cell5.setCellValue(record.getR19_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(19);
/* ===================== R20 ===================== */
cell2 = row.createCell(2);
if (record.getR20_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR20_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR20_MARGINS() != null) {
    cell3.setCellValue(record.getR20_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR20_RW() != null) {
    cell5.setCellValue(record.getR20_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(20);
/* ===================== R21 ===================== */
cell2 = row.createCell(2);
if (record.getR21_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR21_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR21_MARGINS() != null) {
    cell3.setCellValue(record.getR21_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR21_RW() != null) {
    cell5.setCellValue(record.getR21_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(21);
/* ===================== R22 ===================== */
cell2 = row.createCell(2);
if (record.getR22_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR22_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR22_MARGINS() != null) {
    cell3.setCellValue(record.getR22_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR22_RW() != null) {
    cell5.setCellValue(record.getR22_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(22);
/* ===================== R23 ===================== */
cell2 = row.createCell(2);
if (record.getR23_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR23_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR23_MARGINS() != null) {
    cell3.setCellValue(record.getR23_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR23_RW() != null) {
    cell5.setCellValue(record.getR23_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(23);
/* ===================== R24 ===================== */
cell2 = row.createCell(2);
if (record.getR24_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR24_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR24_MARGINS() != null) {
    cell3.setCellValue(record.getR24_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR24_RW() != null) {
    cell5.setCellValue(record.getR24_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(24);
/* ===================== R25 ===================== */
cell2 = row.createCell(2);
if (record.getR25_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR25_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR25_MARGINS() != null) {
    cell3.setCellValue(record.getR25_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR25_RW() != null) {
    cell5.setCellValue(record.getR25_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(25);
/* ===================== R26 ===================== */
cell2 = row.createCell(2);
if (record.getR26_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR26_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR26_MARGINS() != null) {
    cell3.setCellValue(record.getR26_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR26_RW() != null) {
    cell5.setCellValue(record.getR26_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(26);
/* ===================== R27 ===================== */
cell2 = row.createCell(2);
if (record.getR27_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR27_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR27_MARGINS() != null) {
    cell3.setCellValue(record.getR27_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR27_RW() != null) {
    cell5.setCellValue(record.getR27_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(27);
/* ===================== R28 ===================== */
cell2 = row.createCell(2);
if (record.getR28_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR28_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR28_MARGINS() != null) {
    cell3.setCellValue(record.getR28_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR28_RW() != null) {
    cell5.setCellValue(record.getR28_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(28);
/* ===================== R29 ===================== */
cell2 = row.createCell(2);
if (record.getR29_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR29_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR29_MARGINS() != null) {
    cell3.setCellValue(record.getR29_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR29_RW() != null) {
    cell5.setCellValue(record.getR29_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(29);
/* ===================== R30 ===================== */
cell2 = row.createCell(2);
if (record.getR30_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR30_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR30_MARGINS() != null) {
    cell3.setCellValue(record.getR30_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR30_RW() != null) {
    cell5.setCellValue(record.getR30_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(30);
/* ===================== R31 ===================== */
cell2 = row.createCell(2);
if (record.getR31_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR31_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR31_MARGINS() != null) {
    cell3.setCellValue(record.getR31_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR31_RW() != null) {
    cell5.setCellValue(record.getR31_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(31);
/* ===================== R32 ===================== */
cell2 = row.createCell(2);
if (record.getR32_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR32_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR32_MARGINS() != null) {
    cell3.setCellValue(record.getR32_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR32_RW() != null) {
    cell5.setCellValue(record.getR32_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(32);
/* ===================== R33 ===================== */
cell2 = row.createCell(2);
if (record.getR33_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR33_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR33_MARGINS() != null) {
    cell3.setCellValue(record.getR33_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR33_RW() != null) {
    cell5.setCellValue(record.getR33_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(33);
/* ===================== R34 ===================== */
cell2 = row.createCell(2);
if (record.getR34_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR34_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR34_MARGINS() != null) {
    cell3.setCellValue(record.getR34_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR34_RW() != null) {
    cell5.setCellValue(record.getR34_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(34);
/* ===================== R35 ===================== */
cell2 = row.createCell(2);
if (record.getR35_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR35_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR35_MARGINS() != null) {
    cell3.setCellValue(record.getR35_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR35_RW() != null) {
    cell5.setCellValue(record.getR35_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(35);
/* ===================== R36 ===================== */
cell2 = row.createCell(2);
if (record.getR36_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR36_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR36_MARGINS() != null) {
    cell3.setCellValue(record.getR36_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR36_RW() != null) {
    cell5.setCellValue(record.getR36_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(36);
/* ===================== R37 ===================== */
cell2 = row.createCell(2);
if (record.getR37_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR37_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR37_MARGINS() != null) {
    cell3.setCellValue(record.getR37_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR37_RW() != null) {
    cell5.setCellValue(record.getR37_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(37);
/* ===================== R38 ===================== */
cell2 = row.createCell(2);
if (record.getR38_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR38_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR38_MARGINS() != null) {
    cell3.setCellValue(record.getR38_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR38_RW() != null) {
    cell5.setCellValue(record.getR38_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(38);
/* ===================== R39 ===================== */
cell2 = row.createCell(2);
if (record.getR39_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR39_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR39_MARGINS() != null) {
    cell3.setCellValue(record.getR39_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR39_RW() != null) {
    cell5.setCellValue(record.getR39_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(39);
/* ===================== R40 ===================== */
cell2 = row.createCell(2);
if (record.getR40_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR40_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR40_MARGINS() != null) {
    cell3.setCellValue(record.getR40_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR40_RW() != null) {
    cell5.setCellValue(record.getR40_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(40);
/* ===================== R41 ===================== */
cell2 = row.createCell(2);
if (record.getR41_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR41_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR41_MARGINS() != null) {
    cell3.setCellValue(record.getR41_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR41_RW() != null) {
    cell5.setCellValue(record.getR41_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(41);
/* ===================== R42 ===================== */
cell2 = row.createCell(2);
if (record.getR42_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR42_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR42_MARGINS() != null) {
    cell3.setCellValue(record.getR42_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR42_RW() != null) {
    cell5.setCellValue(record.getR42_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(42);
/* ===================== R43 ===================== */
cell2 = row.createCell(2);
if (record.getR43_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR43_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR43_MARGINS() != null) {
    cell3.setCellValue(record.getR43_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR43_RW() != null) {
    cell5.setCellValue(record.getR43_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(43);
/* ===================== R44 ===================== */
cell2 = row.createCell(2);
if (record.getR44_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR44_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR44_MARGINS() != null) {
    cell3.setCellValue(record.getR44_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR44_RW() != null) {
    cell5.setCellValue(record.getR44_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(44);
/* ===================== R45 ===================== */
cell2 = row.createCell(2);
if (record.getR45_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR45_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR45_MARGINS() != null) {
    cell3.setCellValue(record.getR45_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR45_RW() != null) {
    cell5.setCellValue(record.getR45_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(45);
/* ===================== R46 ===================== */
cell2 = row.createCell(2);
if (record.getR46_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR46_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR46_MARGINS() != null) {
    cell3.setCellValue(record.getR46_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR46_RW() != null) {
    cell5.setCellValue(record.getR46_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}


row = sheet.getRow(47);
/* ===================== R48 ===================== */
cell2 = row.createCell(2);
if (record.getR48_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR48_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR48_MARGINS() != null) {
    cell3.setCellValue(record.getR48_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR48_RW() != null) {
    cell5.setCellValue(record.getR48_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}


row = sheet.getRow(60);
/* ===================== R61 ===================== */
cell2 = row.createCell(2);
if (record.getR61_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR61_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR61_MARGINS() != null) {
    cell3.setCellValue(record.getR61_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR61_RW() != null) {
    cell5.setCellValue(record.getR61_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}


row = sheet.getRow(62);
/* ===================== R63 ===================== */
cell2 = row.createCell(2);
if (record.getR63_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR63_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR63_MARGINS() != null) {
    cell3.setCellValue(record.getR63_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR63_RW() != null) {
    cell5.setCellValue(record.getR63_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(63);
/* ===================== R64 ===================== */
cell2 = row.createCell(2);
if (record.getR64_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR64_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR64_MARGINS() != null) {
    cell3.setCellValue(record.getR64_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR64_RW() != null) {
    cell5.setCellValue(record.getR64_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(64);
/* ===================== R65 ===================== */
cell2 = row.createCell(2);
if (record.getR65_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR65_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR65_MARGINS() != null) {
    cell3.setCellValue(record.getR65_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR65_RW() != null) {
    cell5.setCellValue(record.getR65_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(65);
/* ===================== R66 ===================== */
cell2 = row.createCell(2);
if (record.getR66_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR66_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR66_MARGINS() != null) {
    cell3.setCellValue(record.getR66_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR66_RW() != null) {
    cell5.setCellValue(record.getR66_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(66);
/* ===================== R67 ===================== */
cell2 = row.createCell(2);
if (record.getR67_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR67_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR67_MARGINS() != null) {
    cell3.setCellValue(record.getR67_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR67_RW() != null) {
    cell5.setCellValue(record.getR67_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(67);
/* ===================== R68 ===================== */
cell2 = row.createCell(2);
if (record.getR68_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR68_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR68_MARGINS() != null) {
    cell3.setCellValue(record.getR68_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR68_RW() != null) {
    cell5.setCellValue(record.getR68_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(68);
/* ===================== R69 ===================== */
cell2 = row.createCell(2);
if (record.getR69_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR69_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR69_MARGINS() != null) {
    cell3.setCellValue(record.getR69_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR69_RW() != null) {
    cell5.setCellValue(record.getR69_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(69);
/* ===================== R70 ===================== */
cell2 = row.createCell(2);
if (record.getR70_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR70_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR70_MARGINS() != null) {
    cell3.setCellValue(record.getR70_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR70_RW() != null) {
    cell5.setCellValue(record.getR70_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(70);
/* ===================== R71 ===================== */
cell2 = row.createCell(2);
if (record.getR71_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR71_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR71_MARGINS() != null) {
    cell3.setCellValue(record.getR71_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR71_RW() != null) {
    cell5.setCellValue(record.getR71_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(71);
/* ===================== R72 ===================== */
cell2 = row.createCell(2);
if (record.getR72_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR72_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR72_MARGINS() != null) {
    cell3.setCellValue(record.getR72_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR72_RW() != null) {
    cell5.setCellValue(record.getR72_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(72);
/* ===================== R73 ===================== */
cell2 = row.createCell(2);
if (record.getR73_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR73_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR73_MARGINS() != null) {
    cell3.setCellValue(record.getR73_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR73_RW() != null) {
    cell5.setCellValue(record.getR73_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(73);
/* ===================== R74 ===================== */
cell2 = row.createCell(2);
if (record.getR74_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR74_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR74_MARGINS() != null) {
    cell3.setCellValue(record.getR74_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR74_RW() != null) {
    cell5.setCellValue(record.getR74_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(74);
/* ===================== R75 ===================== */
cell2 = row.createCell(2);
if (record.getR75_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR75_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR75_MARGINS() != null) {
    cell3.setCellValue(record.getR75_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR75_RW() != null) {
    cell5.setCellValue(record.getR75_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(75);
/* ===================== R76 ===================== */
cell2 = row.createCell(2);
if (record.getR76_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR76_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR76_MARGINS() != null) {
    cell3.setCellValue(record.getR76_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR76_RW() != null) {
    cell5.setCellValue(record.getR76_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(76);
/* ===================== R77 ===================== */
cell2 = row.createCell(2);
if (record.getR77_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR77_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR77_MARGINS() != null) {
    cell3.setCellValue(record.getR77_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR77_RW() != null) {
    cell5.setCellValue(record.getR77_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(77);
/* ===================== R78 ===================== */
cell2 = row.createCell(2);
if (record.getR78_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR78_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR78_MARGINS() != null) {
    cell3.setCellValue(record.getR78_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR78_RW() != null) {
    cell5.setCellValue(record.getR78_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(78);
/* ===================== R79 ===================== */
cell2 = row.createCell(2);
if (record.getR79_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR79_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR79_MARGINS() != null) {
    cell3.setCellValue(record.getR79_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR79_RW() != null) {
    cell5.setCellValue(record.getR79_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(79);
/* ===================== R80 ===================== */
cell2 = row.createCell(2);
if (record.getR80_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR80_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR80_MARGINS() != null) {
    cell3.setCellValue(record.getR80_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR80_RW() != null) {
    cell5.setCellValue(record.getR80_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(80);
/* ===================== R81 ===================== */
cell2 = row.createCell(2);
if (record.getR81_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR81_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR81_MARGINS() != null) {
    cell3.setCellValue(record.getR81_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR81_RW() != null) {
    cell5.setCellValue(record.getR81_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(81);
/* ===================== R82 ===================== */
cell2 = row.createCell(2);
if (record.getR82_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR82_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR82_MARGINS() != null) {
    cell3.setCellValue(record.getR82_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR82_RW() != null) {
    cell5.setCellValue(record.getR82_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}


row = sheet.getRow(96);
/* ===================== R97 ===================== */
cell2 = row.createCell(2);
if (record.getR97_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR97_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR97_MARGINS() != null) {
    cell3.setCellValue(record.getR97_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR97_RW() != null) {
    cell5.setCellValue(record.getR97_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(97);
/* ===================== R98 ===================== */
cell2 = row.createCell(2);
if (record.getR98_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR98_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR98_MARGINS() != null) {
    cell3.setCellValue(record.getR98_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR98_RW() != null) {
    cell5.setCellValue(record.getR98_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(98);
/* ===================== R99 ===================== */
cell2 = row.createCell(2);
if (record.getR99_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR99_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR99_MARGINS() != null) {
    cell3.setCellValue(record.getR99_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR99_RW() != null) {
    cell5.setCellValue(record.getR99_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(99);
/* ===================== R100 ===================== */
cell2 = row.createCell(2);
if (record.getR100_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR100_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR100_MARGINS() != null) {
    cell3.setCellValue(record.getR100_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR100_RW() != null) {
    cell5.setCellValue(record.getR100_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(100);
/* ===================== R101 ===================== */
cell2 = row.createCell(2);
if (record.getR101_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR101_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR101_MARGINS() != null) {
    cell3.setCellValue(record.getR101_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR101_RW() != null) {
    cell5.setCellValue(record.getR101_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(101);
/* ===================== R102 ===================== */
cell2 = row.createCell(2);
if (record.getR102_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR102_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR102_MARGINS() != null) {
    cell3.setCellValue(record.getR102_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR102_RW() != null) {
    cell5.setCellValue(record.getR102_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(102);
/* ===================== R103 ===================== */
cell2 = row.createCell(2);
if (record.getR103_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR103_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR103_MARGINS() != null) {
    cell3.setCellValue(record.getR103_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR103_RW() != null) {
    cell5.setCellValue(record.getR103_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(103);
/* ===================== R104 ===================== */
cell2 = row.createCell(2);
if (record.getR104_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR104_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR104_MARGINS() != null) {
    cell3.setCellValue(record.getR104_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR104_RW() != null) {
    cell5.setCellValue(record.getR104_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(104);
/* ===================== R105 ===================== */
cell2 = row.createCell(2);
if (record.getR105_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR105_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR105_MARGINS() != null) {
    cell3.setCellValue(record.getR105_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR105_RW() != null) {
    cell5.setCellValue(record.getR105_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(105);
/* ===================== R106 ===================== */
cell2 = row.createCell(2);
if (record.getR106_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR106_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR106_MARGINS() != null) {
    cell3.setCellValue(record.getR106_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR106_RW() != null) {
    cell5.setCellValue(record.getR106_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(106);
/* ===================== R107 ===================== */
cell2 = row.createCell(2);
if (record.getR107_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR107_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR107_MARGINS() != null) {
    cell3.setCellValue(record.getR107_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR107_RW() != null) {
    cell5.setCellValue(record.getR107_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(107);
/* ===================== R108 ===================== */
cell2 = row.createCell(2);
if (record.getR108_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR108_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR108_MARGINS() != null) {
    cell3.setCellValue(record.getR108_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR108_RW() != null) {
    cell5.setCellValue(record.getR108_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(108);
/* ===================== R109 ===================== */
cell2 = row.createCell(2);
if (record.getR109_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR109_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR109_MARGINS() != null) {
    cell3.setCellValue(record.getR109_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR109_RW() != null) {
    cell5.setCellValue(record.getR109_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(109);
/* ===================== R110 ===================== */
cell2 = row.createCell(2);
if (record.getR110_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR110_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR110_MARGINS() != null) {
    cell3.setCellValue(record.getR110_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR110_RW() != null) {
    cell5.setCellValue(record.getR110_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(110);
/* ===================== R111 ===================== */
cell2 = row.createCell(2);
if (record.getR111_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR111_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR111_MARGINS() != null) {
    cell3.setCellValue(record.getR111_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR111_RW() != null) {
    cell5.setCellValue(record.getR111_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(111);
/* ===================== R112 ===================== */
cell2 = row.createCell(2);
if (record.getR112_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR112_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR112_MARGINS() != null) {
    cell3.setCellValue(record.getR112_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR112_RW() != null) {
    cell5.setCellValue(record.getR112_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(112);
/* ===================== R113 ===================== */
cell2 = row.createCell(2);
if (record.getR113_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR113_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR113_MARGINS() != null) {
    cell3.setCellValue(record.getR113_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR113_RW() != null) {
    cell5.setCellValue(record.getR113_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(113);
/* ===================== R114 ===================== */
cell2 = row.createCell(2);
if (record.getR114_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR114_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR114_MARGINS() != null) {
    cell3.setCellValue(record.getR114_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR114_RW() != null) {
    cell5.setCellValue(record.getR114_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(114);
/* ===================== R115 ===================== */
cell2 = row.createCell(2);
if (record.getR115_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR115_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR115_MARGINS() != null) {
    cell3.setCellValue(record.getR115_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR115_RW() != null) {
    cell5.setCellValue(record.getR115_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(115);
/* ===================== R116 ===================== */
cell2 = row.createCell(2);
if (record.getR116_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR116_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR116_MARGINS() != null) {
    cell3.setCellValue(record.getR116_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR116_RW() != null) {
    cell5.setCellValue(record.getR116_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(116);
/* ===================== R117 ===================== */
cell2 = row.createCell(2);
if (record.getR117_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR117_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR117_MARGINS() != null) {
    cell3.setCellValue(record.getR117_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR117_RW() != null) {
    cell5.setCellValue(record.getR117_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(117);
/* ===================== R118 ===================== */
cell2 = row.createCell(2);
if (record.getR118_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR118_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR118_MARGINS() != null) {
    cell3.setCellValue(record.getR118_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR118_RW() != null) {
    cell5.setCellValue(record.getR118_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(118);
/* ===================== R119 ===================== */
cell2 = row.createCell(2);
if (record.getR119_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR119_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR119_MARGINS() != null) {
    cell3.setCellValue(record.getR119_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR119_RW() != null) {
    cell5.setCellValue(record.getR119_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(119);
/* ===================== R120 ===================== */
cell2 = row.createCell(2);
if (record.getR120_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR120_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR120_MARGINS() != null) {
    cell3.setCellValue(record.getR120_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR120_RW() != null) {
    cell5.setCellValue(record.getR120_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(120);
/* ===================== R121 ===================== */
cell2 = row.createCell(2);
if (record.getR121_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR121_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR121_MARGINS() != null) {
    cell3.setCellValue(record.getR121_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR121_RW() != null) {
    cell5.setCellValue(record.getR121_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(121);
/* ===================== R122 ===================== */
cell2 = row.createCell(2);
if (record.getR122_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR122_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR122_MARGINS() != null) {
    cell3.setCellValue(record.getR122_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR122_RW() != null) {
    cell5.setCellValue(record.getR122_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(122);
/* ===================== R123 ===================== */
cell2 = row.createCell(2);
if (record.getR123_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR123_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR123_MARGINS() != null) {
    cell3.setCellValue(record.getR123_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR123_RW() != null) {
    cell5.setCellValue(record.getR123_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(123);
/* ===================== R124 ===================== */
cell2 = row.createCell(2);
if (record.getR124_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR124_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR124_MARGINS() != null) {
    cell3.setCellValue(record.getR124_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR124_RW() != null) {
    cell5.setCellValue(record.getR124_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(124);
/* ===================== R125 ===================== */
cell2 = row.createCell(2);
if (record.getR125_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR125_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR125_MARGINS() != null) {
    cell3.setCellValue(record.getR125_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR125_RW() != null) {
    cell5.setCellValue(record.getR125_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(125);
/* ===================== R126 ===================== */
cell2 = row.createCell(2);
if (record.getR126_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR126_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR126_MARGINS() != null) {
    cell3.setCellValue(record.getR126_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR126_RW() != null) {
    cell5.setCellValue(record.getR126_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(126);
/* ===================== R127 ===================== */
cell2 = row.createCell(2);
if (record.getR127_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR127_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR127_MARGINS() != null) {
    cell3.setCellValue(record.getR127_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR127_RW() != null) {
    cell5.setCellValue(record.getR127_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(127);
/* ===================== R128 ===================== */
cell2 = row.createCell(2);
if (record.getR128_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR128_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR128_MARGINS() != null) {
    cell3.setCellValue(record.getR128_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR128_RW() != null) {
    cell5.setCellValue(record.getR128_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(128);
/* ===================== R129 ===================== */
cell2 = row.createCell(2);
if (record.getR129_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR129_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR129_MARGINS() != null) {
    cell3.setCellValue(record.getR129_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR129_RW() != null) {
    cell5.setCellValue(record.getR129_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
}

row = sheet.getRow(129);
/* ===================== R130 ===================== */
cell2 = row.createCell(2);
if (record.getR130_BOOK_VALUE() != null) {
    cell2.setCellValue(record.getR130_BOOK_VALUE().doubleValue());
    cell2.setCellStyle(numberStyle);
} else {
    cell2.setCellValue("");
    cell2.setCellStyle(textStyle);
}

cell3 = row.createCell(3);
if (record.getR130_MARGINS() != null) {
    cell3.setCellValue(record.getR130_MARGINS().doubleValue());
    cell3.setCellStyle(numberStyle);
} else {
    cell3.setCellValue("");
    cell3.setCellStyle(textStyle);
}

cell5 = row.createCell(5);
if (record.getR130_RW() != null) {
    cell5.setCellValue(record.getR130_RW().doubleValue());
    cell5.setCellStyle(numberStyle);
} else {
    cell5.setCellValue("");
    cell5.setCellStyle(textStyle);
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
	
	public List<Object> getRWAArchival() {
		List<Object> RWAArchivallist = new ArrayList<>();
		try {
			RWAArchivallist = RWA_Archival_Summary_Repo.getRWAarchival();
//			FSIArchivallist = BRRS_FSI_Manual_Archival_Summary_Repo.getFSIarchival();
			System.out.println("countser" + RWAArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching EXPOSURES Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return RWAArchivallist;
	}
	
	
	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			 String dtltype, String type, String version) {
try {
logger.info("Generating Excel for BRRS_RWA ARCHIVAL Details...");
System.out.println("came to Detail download service");
if (type.equals("ARCHIVAL") & version != null) {

}
XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("RWADetail");

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
List<RWA_Archival_Detail_Entity> reportData = RWA_Archival_Detail_Repo.getdatabydateList(parsedToDate,version);

if (reportData != null && !reportData.isEmpty()) {
int rowIndex = 1;
for (RWA_Archival_Detail_Entity item : reportData) {
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
logger.info("No data found for RWA — only header will be written.");
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

	public byte[] getExcelRWAARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<RWA_Archival_Summary_Entity> dataList = RWA_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for RWA report. Returning empty result.");
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

				RWA_Archival_Summary_Entity record1 = dataList.get(i);
				System.out.println("rownumber=" + startRow + i);
				Row row = sheet.getRow(startRow + i);
				if (row == null) {
				row = sheet.createRow(startRow + i);
				}
				Cell cell2 = row.createCell(2);
				if (record1.getR8_BOOK_VALUE() != null) {
				cell2.setCellValue(record1.getR8_BOOK_VALUE().doubleValue());
				cell2.setCellStyle(numberStyle);
				} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
				}

				Cell cell3 = row.createCell(3);
				if (record1.getR8_MARGINS() != null) {
				cell3.setCellValue(record1.getR8_MARGINS().doubleValue());
				cell3.setCellStyle(numberStyle);
				} else {
				cell3.setCellValue("");
				cell3.setCellStyle(textStyle);
				}

				Cell cell5 = row.createCell(3);
				if (record1.getR8_RW() != null) {
				cell5.setCellValue(record1.getR8_RW().doubleValue());
				cell5.setCellStyle(numberStyle);
				} else {
				cell5.setCellValue("");
				cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(8);
				/* ===================== R9 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR9_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR9_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR9_MARGINS() != null) {
				    cell3.setCellValue(record1.getR9_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR9_RW() != null) {
				    cell5.setCellValue(record1.getR9_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(9);
				/* ===================== R10 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR10_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR10_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR10_MARGINS() != null) {
				    cell3.setCellValue(record1.getR10_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR10_RW() != null) {
				    cell5.setCellValue(record1.getR10_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(10);
				/* ===================== R11 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR11_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR11_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR11_MARGINS() != null) {
				    cell3.setCellValue(record1.getR11_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR11_RW() != null) {
				    cell5.setCellValue(record1.getR11_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(11);
				/* ===================== R12 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR12_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR12_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR12_MARGINS() != null) {
				    cell3.setCellValue(record1.getR12_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR12_RW() != null) {
				    cell5.setCellValue(record1.getR12_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(12);
				/* ===================== R13 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR13_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR13_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR13_MARGINS() != null) {
				    cell3.setCellValue(record1.getR13_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR13_RW() != null) {
				    cell5.setCellValue(record1.getR13_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(13);
				/* ===================== R14 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR14_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR14_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR14_MARGINS() != null) {
				    cell3.setCellValue(record1.getR14_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR14_RW() != null) {
				    cell5.setCellValue(record1.getR14_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(14);
				/* ===================== R15 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR15_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR15_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR15_MARGINS() != null) {
				    cell3.setCellValue(record1.getR15_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR15_RW() != null) {
				    cell5.setCellValue(record1.getR15_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(15);
				/* ===================== R16 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR16_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR16_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR16_MARGINS() != null) {
				    cell3.setCellValue(record1.getR16_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR16_RW() != null) {
				    cell5.setCellValue(record1.getR16_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(16);
				/* ===================== R17 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR17_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR17_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR17_MARGINS() != null) {
				    cell3.setCellValue(record1.getR17_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR17_RW() != null) {
				    cell5.setCellValue(record1.getR17_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(17);
				/* ===================== R18 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR18_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR18_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR18_MARGINS() != null) {
				    cell3.setCellValue(record1.getR18_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR18_RW() != null) {
				    cell5.setCellValue(record1.getR18_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(18);
				/* ===================== R19 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR19_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR19_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR19_MARGINS() != null) {
				    cell3.setCellValue(record1.getR19_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR19_RW() != null) {
				    cell5.setCellValue(record1.getR19_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(19);
				/* ===================== R20 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR20_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR20_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR20_MARGINS() != null) {
				    cell3.setCellValue(record1.getR20_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR20_RW() != null) {
				    cell5.setCellValue(record1.getR20_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(20);
				/* ===================== R21 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR21_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR21_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR21_MARGINS() != null) {
				    cell3.setCellValue(record1.getR21_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR21_RW() != null) {
				    cell5.setCellValue(record1.getR21_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(21);
				/* ===================== R22 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR22_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR22_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR22_MARGINS() != null) {
				    cell3.setCellValue(record1.getR22_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR22_RW() != null) {
				    cell5.setCellValue(record1.getR22_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(22);
				/* ===================== R23 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR23_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR23_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR23_MARGINS() != null) {
				    cell3.setCellValue(record1.getR23_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR23_RW() != null) {
				    cell5.setCellValue(record1.getR23_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(23);
				/* ===================== R24 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR24_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR24_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR24_MARGINS() != null) {
				    cell3.setCellValue(record1.getR24_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR24_RW() != null) {
				    cell5.setCellValue(record1.getR24_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(24);
				/* ===================== R25 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR25_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR25_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR25_MARGINS() != null) {
				    cell3.setCellValue(record1.getR25_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR25_RW() != null) {
				    cell5.setCellValue(record1.getR25_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(25);
				/* ===================== R26 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR26_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR26_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR26_MARGINS() != null) {
				    cell3.setCellValue(record1.getR26_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR26_RW() != null) {
				    cell5.setCellValue(record1.getR26_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(26);
				/* ===================== R27 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR27_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR27_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR27_MARGINS() != null) {
				    cell3.setCellValue(record1.getR27_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR27_RW() != null) {
				    cell5.setCellValue(record1.getR27_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(27);
				/* ===================== R28 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR28_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR28_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR28_MARGINS() != null) {
				    cell3.setCellValue(record1.getR28_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR28_RW() != null) {
				    cell5.setCellValue(record1.getR28_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(28);
				/* ===================== R29 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR29_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR29_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR29_MARGINS() != null) {
				    cell3.setCellValue(record1.getR29_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR29_RW() != null) {
				    cell5.setCellValue(record1.getR29_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(29);
				/* ===================== R30 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR30_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR30_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR30_MARGINS() != null) {
				    cell3.setCellValue(record1.getR30_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR30_RW() != null) {
				    cell5.setCellValue(record1.getR30_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(30);
				/* ===================== R31 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR31_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR31_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR31_MARGINS() != null) {
				    cell3.setCellValue(record1.getR31_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR31_RW() != null) {
				    cell5.setCellValue(record1.getR31_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(31);
				/* ===================== R32 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR32_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR32_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR32_MARGINS() != null) {
				    cell3.setCellValue(record1.getR32_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR32_RW() != null) {
				    cell5.setCellValue(record1.getR32_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(32);
				/* ===================== R33 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR33_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR33_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR33_MARGINS() != null) {
				    cell3.setCellValue(record1.getR33_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR33_RW() != null) {
				    cell5.setCellValue(record1.getR33_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(33);
				/* ===================== R34 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR34_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR34_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR34_MARGINS() != null) {
				    cell3.setCellValue(record1.getR34_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR34_RW() != null) {
				    cell5.setCellValue(record1.getR34_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(34);
				/* ===================== R35 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR35_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR35_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR35_MARGINS() != null) {
				    cell3.setCellValue(record1.getR35_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR35_RW() != null) {
				    cell5.setCellValue(record1.getR35_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(35);
				/* ===================== R36 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR36_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR36_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR36_MARGINS() != null) {
				    cell3.setCellValue(record1.getR36_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR36_RW() != null) {
				    cell5.setCellValue(record1.getR36_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(36);
				/* ===================== R37 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR37_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR37_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR37_MARGINS() != null) {
				    cell3.setCellValue(record1.getR37_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR37_RW() != null) {
				    cell5.setCellValue(record1.getR37_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(37);
				/* ===================== R38 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR38_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR38_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR38_MARGINS() != null) {
				    cell3.setCellValue(record1.getR38_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR38_RW() != null) {
				    cell5.setCellValue(record1.getR38_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(38);
				/* ===================== R39 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR39_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR39_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR39_MARGINS() != null) {
				    cell3.setCellValue(record1.getR39_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR39_RW() != null) {
				    cell5.setCellValue(record1.getR39_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(39);
				/* ===================== R40 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR40_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR40_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR40_MARGINS() != null) {
				    cell3.setCellValue(record1.getR40_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR40_RW() != null) {
				    cell5.setCellValue(record1.getR40_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(40);
				/* ===================== R41 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR41_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR41_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR41_MARGINS() != null) {
				    cell3.setCellValue(record1.getR41_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR41_RW() != null) {
				    cell5.setCellValue(record1.getR41_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(41);
				/* ===================== R42 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR42_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR42_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR42_MARGINS() != null) {
				    cell3.setCellValue(record1.getR42_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR42_RW() != null) {
				    cell5.setCellValue(record1.getR42_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(42);
				/* ===================== R43 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR43_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR43_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR43_MARGINS() != null) {
				    cell3.setCellValue(record1.getR43_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR43_RW() != null) {
				    cell5.setCellValue(record1.getR43_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(43);
				/* ===================== R44 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR44_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR44_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR44_MARGINS() != null) {
				    cell3.setCellValue(record1.getR44_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR44_RW() != null) {
				    cell5.setCellValue(record1.getR44_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(44);
				/* ===================== R45 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR45_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR45_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR45_MARGINS() != null) {
				    cell3.setCellValue(record1.getR45_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR45_RW() != null) {
				    cell5.setCellValue(record1.getR45_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(45);
				/* ===================== R46 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR46_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR46_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR46_MARGINS() != null) {
				    cell3.setCellValue(record1.getR46_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR46_RW() != null) {
				    cell5.setCellValue(record1.getR46_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}


				row = sheet.getRow(47);
				/* ===================== R48 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR48_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR48_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR48_MARGINS() != null) {
				    cell3.setCellValue(record1.getR48_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR48_RW() != null) {
				    cell5.setCellValue(record1.getR48_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}


				row = sheet.getRow(60);
				/* ===================== R61 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR61_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR61_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR61_MARGINS() != null) {
				    cell3.setCellValue(record1.getR61_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR61_RW() != null) {
				    cell5.setCellValue(record1.getR61_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}


				row = sheet.getRow(62);
				/* ===================== R63 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR63_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR63_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR63_MARGINS() != null) {
				    cell3.setCellValue(record1.getR63_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR63_RW() != null) {
				    cell5.setCellValue(record1.getR63_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(63);
				/* ===================== R64 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR64_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR64_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR64_MARGINS() != null) {
				    cell3.setCellValue(record1.getR64_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR64_RW() != null) {
				    cell5.setCellValue(record1.getR64_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(64);
				/* ===================== R65 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR65_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR65_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR65_MARGINS() != null) {
				    cell3.setCellValue(record1.getR65_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR65_RW() != null) {
				    cell5.setCellValue(record1.getR65_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(65);
				/* ===================== R66 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR66_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR66_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR66_MARGINS() != null) {
				    cell3.setCellValue(record1.getR66_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR66_RW() != null) {
				    cell5.setCellValue(record1.getR66_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(66);
				/* ===================== R67 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR67_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR67_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR67_MARGINS() != null) {
				    cell3.setCellValue(record1.getR67_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR67_RW() != null) {
				    cell5.setCellValue(record1.getR67_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(67);
				/* ===================== R68 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR68_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR68_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR68_MARGINS() != null) {
				    cell3.setCellValue(record1.getR68_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR68_RW() != null) {
				    cell5.setCellValue(record1.getR68_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(68);
				/* ===================== R69 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR69_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR69_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR69_MARGINS() != null) {
				    cell3.setCellValue(record1.getR69_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR69_RW() != null) {
				    cell5.setCellValue(record1.getR69_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(69);
				/* ===================== R70 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR70_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR70_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR70_MARGINS() != null) {
				    cell3.setCellValue(record1.getR70_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR70_RW() != null) {
				    cell5.setCellValue(record1.getR70_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(70);
				/* ===================== R71 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR71_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR71_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR71_MARGINS() != null) {
				    cell3.setCellValue(record1.getR71_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR71_RW() != null) {
				    cell5.setCellValue(record1.getR71_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(71);
				/* ===================== R72 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR72_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR72_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR72_MARGINS() != null) {
				    cell3.setCellValue(record1.getR72_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR72_RW() != null) {
				    cell5.setCellValue(record1.getR72_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(72);
				/* ===================== R73 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR73_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR73_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR73_MARGINS() != null) {
				    cell3.setCellValue(record1.getR73_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR73_RW() != null) {
				    cell5.setCellValue(record1.getR73_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(73);
				/* ===================== R74 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR74_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR74_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR74_MARGINS() != null) {
				    cell3.setCellValue(record1.getR74_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR74_RW() != null) {
				    cell5.setCellValue(record1.getR74_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(74);
				/* ===================== R75 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR75_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR75_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR75_MARGINS() != null) {
				    cell3.setCellValue(record1.getR75_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR75_RW() != null) {
				    cell5.setCellValue(record1.getR75_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(75);
				/* ===================== R76 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR76_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR76_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR76_MARGINS() != null) {
				    cell3.setCellValue(record1.getR76_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR76_RW() != null) {
				    cell5.setCellValue(record1.getR76_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(76);
				/* ===================== R77 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR77_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR77_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR77_MARGINS() != null) {
				    cell3.setCellValue(record1.getR77_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR77_RW() != null) {
				    cell5.setCellValue(record1.getR77_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(77);
				/* ===================== R78 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR78_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR78_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR78_MARGINS() != null) {
				    cell3.setCellValue(record1.getR78_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR78_RW() != null) {
				    cell5.setCellValue(record1.getR78_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(78);
				/* ===================== R79 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR79_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR79_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR79_MARGINS() != null) {
				    cell3.setCellValue(record1.getR79_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR79_RW() != null) {
				    cell5.setCellValue(record1.getR79_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(79);
				/* ===================== R80 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR80_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR80_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR80_MARGINS() != null) {
				    cell3.setCellValue(record1.getR80_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR80_RW() != null) {
				    cell5.setCellValue(record1.getR80_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(80);
				/* ===================== R81 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR81_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR81_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR81_MARGINS() != null) {
				    cell3.setCellValue(record1.getR81_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR81_RW() != null) {
				    cell5.setCellValue(record1.getR81_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(81);
				/* ===================== R82 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR82_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR82_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR82_MARGINS() != null) {
				    cell3.setCellValue(record1.getR82_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR82_RW() != null) {
				    cell5.setCellValue(record1.getR82_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}


				row = sheet.getRow(96);
				/* ===================== R97 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR97_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR97_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR97_MARGINS() != null) {
				    cell3.setCellValue(record1.getR97_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR97_RW() != null) {
				    cell5.setCellValue(record1.getR97_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(97);
				/* ===================== R98 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR98_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR98_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR98_MARGINS() != null) {
				    cell3.setCellValue(record1.getR98_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR98_RW() != null) {
				    cell5.setCellValue(record1.getR98_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(98);
				/* ===================== R99 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR99_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR99_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR99_MARGINS() != null) {
				    cell3.setCellValue(record1.getR99_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR99_RW() != null) {
				    cell5.setCellValue(record1.getR99_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(99);
				/* ===================== R100 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR100_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR100_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR100_MARGINS() != null) {
				    cell3.setCellValue(record1.getR100_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR100_RW() != null) {
				    cell5.setCellValue(record1.getR100_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(100);
				/* ===================== R101 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR101_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR101_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR101_MARGINS() != null) {
				    cell3.setCellValue(record1.getR101_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR101_RW() != null) {
				    cell5.setCellValue(record1.getR101_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(101);
				/* ===================== R102 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR102_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR102_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR102_MARGINS() != null) {
				    cell3.setCellValue(record1.getR102_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR102_RW() != null) {
				    cell5.setCellValue(record1.getR102_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(102);
				/* ===================== R103 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR103_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR103_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR103_MARGINS() != null) {
				    cell3.setCellValue(record1.getR103_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR103_RW() != null) {
				    cell5.setCellValue(record1.getR103_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(103);
				/* ===================== R104 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR104_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR104_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR104_MARGINS() != null) {
				    cell3.setCellValue(record1.getR104_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR104_RW() != null) {
				    cell5.setCellValue(record1.getR104_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(104);
				/* ===================== R105 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR105_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR105_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR105_MARGINS() != null) {
				    cell3.setCellValue(record1.getR105_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR105_RW() != null) {
				    cell5.setCellValue(record1.getR105_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(105);
				/* ===================== R106 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR106_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR106_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR106_MARGINS() != null) {
				    cell3.setCellValue(record1.getR106_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR106_RW() != null) {
				    cell5.setCellValue(record1.getR106_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(106);
				/* ===================== R107 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR107_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR107_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR107_MARGINS() != null) {
				    cell3.setCellValue(record1.getR107_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR107_RW() != null) {
				    cell5.setCellValue(record1.getR107_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(107);
				/* ===================== R108 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR108_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR108_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR108_MARGINS() != null) {
				    cell3.setCellValue(record1.getR108_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR108_RW() != null) {
				    cell5.setCellValue(record1.getR108_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(108);
				/* ===================== R109 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR109_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR109_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR109_MARGINS() != null) {
				    cell3.setCellValue(record1.getR109_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR109_RW() != null) {
				    cell5.setCellValue(record1.getR109_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(109);
				/* ===================== R110 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR110_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR110_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR110_MARGINS() != null) {
				    cell3.setCellValue(record1.getR110_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR110_RW() != null) {
				    cell5.setCellValue(record1.getR110_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(110);
				/* ===================== R111 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR111_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR111_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR111_MARGINS() != null) {
				    cell3.setCellValue(record1.getR111_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR111_RW() != null) {
				    cell5.setCellValue(record1.getR111_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(111);
				/* ===================== R112 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR112_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR112_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR112_MARGINS() != null) {
				    cell3.setCellValue(record1.getR112_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR112_RW() != null) {
				    cell5.setCellValue(record1.getR112_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(112);
				/* ===================== R113 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR113_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR113_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR113_MARGINS() != null) {
				    cell3.setCellValue(record1.getR113_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR113_RW() != null) {
				    cell5.setCellValue(record1.getR113_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(113);
				/* ===================== R114 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR114_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR114_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR114_MARGINS() != null) {
				    cell3.setCellValue(record1.getR114_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR114_RW() != null) {
				    cell5.setCellValue(record1.getR114_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(114);
				/* ===================== R115 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR115_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR115_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR115_MARGINS() != null) {
				    cell3.setCellValue(record1.getR115_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR115_RW() != null) {
				    cell5.setCellValue(record1.getR115_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(115);
				/* ===================== R116 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR116_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR116_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR116_MARGINS() != null) {
				    cell3.setCellValue(record1.getR116_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR116_RW() != null) {
				    cell5.setCellValue(record1.getR116_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(116);
				/* ===================== R117 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR117_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR117_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR117_MARGINS() != null) {
				    cell3.setCellValue(record1.getR117_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR117_RW() != null) {
				    cell5.setCellValue(record1.getR117_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(117);
				/* ===================== R118 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR118_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR118_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR118_MARGINS() != null) {
				    cell3.setCellValue(record1.getR118_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR118_RW() != null) {
				    cell5.setCellValue(record1.getR118_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(118);
				/* ===================== R119 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR119_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR119_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR119_MARGINS() != null) {
				    cell3.setCellValue(record1.getR119_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR119_RW() != null) {
				    cell5.setCellValue(record1.getR119_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(119);
				/* ===================== R120 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR120_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR120_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR120_MARGINS() != null) {
				    cell3.setCellValue(record1.getR120_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR120_RW() != null) {
				    cell5.setCellValue(record1.getR120_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(120);
				/* ===================== R121 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR121_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR121_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR121_MARGINS() != null) {
				    cell3.setCellValue(record1.getR121_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR121_RW() != null) {
				    cell5.setCellValue(record1.getR121_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(121);
				/* ===================== R122 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR122_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR122_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR122_MARGINS() != null) {
				    cell3.setCellValue(record1.getR122_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR122_RW() != null) {
				    cell5.setCellValue(record1.getR122_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(122);
				/* ===================== R123 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR123_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR123_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR123_MARGINS() != null) {
				    cell3.setCellValue(record1.getR123_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR123_RW() != null) {
				    cell5.setCellValue(record1.getR123_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(123);
				/* ===================== R124 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR124_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR124_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR124_MARGINS() != null) {
				    cell3.setCellValue(record1.getR124_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR124_RW() != null) {
				    cell5.setCellValue(record1.getR124_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(124);
				/* ===================== R125 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR125_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR125_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR125_MARGINS() != null) {
				    cell3.setCellValue(record1.getR125_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR125_RW() != null) {
				    cell5.setCellValue(record1.getR125_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(125);
				/* ===================== R126 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR126_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR126_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR126_MARGINS() != null) {
				    cell3.setCellValue(record1.getR126_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR126_RW() != null) {
				    cell5.setCellValue(record1.getR126_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(126);
				/* ===================== R127 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR127_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR127_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR127_MARGINS() != null) {
				    cell3.setCellValue(record1.getR127_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR127_RW() != null) {
				    cell5.setCellValue(record1.getR127_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(127);
				/* ===================== R128 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR128_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR128_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR128_MARGINS() != null) {
				    cell3.setCellValue(record1.getR128_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR128_RW() != null) {
				    cell5.setCellValue(record1.getR128_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(128);
				/* ===================== R129 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR129_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR129_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR129_MARGINS() != null) {
				    cell3.setCellValue(record1.getR129_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR129_RW() != null) {
				    cell5.setCellValue(record1.getR129_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				row = sheet.getRow(129);
				/* ===================== R130 ===================== */
				cell2 = row.createCell(2);
				if (record1.getR130_BOOK_VALUE() != null) {
				    cell2.setCellValue(record1.getR130_BOOK_VALUE().doubleValue());
				    cell2.setCellStyle(numberStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record1.getR130_MARGINS() != null) {
				    cell3.setCellValue(record1.getR130_MARGINS().doubleValue());
				    cell3.setCellStyle(numberStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record1.getR130_RW() != null) {
				    cell5.setCellValue(record1.getR130_RW().doubleValue());
				    cell5.setCellStyle(numberStyle);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
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
	    ModelAndView mv = new ModelAndView("BRRS/RWA"); // ✅ match the report name
	    System.out.println("Hello");
	    if (acctNo != null) {
	    	RWA_Detail_Entity fsiEntity = RWA_Detail_Repo.findByAcctnumber(acctNo);
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
	    ModelAndView mv = new ModelAndView("BRRS/RWA"); // ✅ match the report name

	    if (acctNo != null) {
	    	RWA_Detail_Entity la1Entity = RWA_Detail_Repo.findByAcctnumber(acctNo);
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

	        RWA_Detail_Entity existing = RWA_Detail_Repo.findByAcctnumber(acctNo);
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
	        	RWA_Detail_Repo.save(existing);
	            logger.info("Record updated successfully for account {}", acctNo);

	            // Format date for procedure
	            String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
	                    .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

	            // Run summary procedure after commit
	            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
	                @Override
	                public void afterCommit() {
	                    try {
	                        logger.info("Transaction committed — calling BRRS_RWA_SUMMARY_PROCEDURE({})",
	                                formattedDate);
	                        jdbcTemplate.update("BEGIN BRRS_RWA_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
	        logger.error("Error updating RWA record", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error updating record: " + e.getMessage());
	    }
	}
	
					
			
}

