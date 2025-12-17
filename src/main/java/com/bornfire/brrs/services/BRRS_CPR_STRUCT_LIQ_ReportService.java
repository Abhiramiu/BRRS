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

//	public byte[] getCPR_STRUCT_LIQExcel(String filename, String reportId, String fromdate, String todate, String currency,
//			 String dtltype, String type, String version) throws Exception {
//logger.info("Service: Starting Excel generation process in memory.");
//logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);
//
////Convert string to Date
//Date reportDate = dateformat.parse(todate);
//
////ARCHIVAL check
//if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
//logger.info("Service: Generating ARCHIVAL report for version {}", version);
//return getExcelCPR_STRUCT_LIQARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
//}
////RESUB check
//else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
//logger.info("Service: Generating RESUB report for version {}", version);
//
//
//List<CPR_STRUCT_LIQ_Archival_Summary_Entity> T1Master =
//BRRS_CPR_STRUCT_LIQ_Archival_Summary_Repo.getdatabydateListarchival(reportDate, version);
//
//
//}
//
//
////Default (LIVE) case
//List<CPR_STRUCT_LIQ_Summary_Entity> dataList1 = BRRS_CPR_STRUCT_LIQ_Summary_Repo.getdatabydateList(reportDate);
//
//String templateDir = env.getProperty("output.exportpathtemp");
//String templateFileName = filename;
//System.out.println(filename);
//Path templatePath = Paths.get(templateDir, templateFileName);
//System.out.println(templatePath);
//
//logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());
//
//if (!Files.exists(templatePath)) {
////This specific exception will be caught by the controller.
//throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
//}
//if (!Files.isReadable(templatePath)) {
////A specific exception for permission errors.
//throw new SecurityException(
//"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
//}
//
////This try-with-resources block is perfect. It guarantees all resources are
////closed automatically.
//try (InputStream templateInputStream = Files.newInputStream(templatePath);
//Workbook workbook = WorkbookFactory.create(templateInputStream);
//ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//
//Sheet sheet = workbook.getSheetAt(0);
//
////--- Style Definitions ---
//CreationHelper createHelper = workbook.getCreationHelper();
//
//CellStyle dateStyle = workbook.createCellStyle();
//dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
//dateStyle.setBorderBottom(BorderStyle.THIN);
//dateStyle.setBorderTop(BorderStyle.THIN);
//dateStyle.setBorderLeft(BorderStyle.THIN);
//dateStyle.setBorderRight(BorderStyle.THIN);
//
//CellStyle textStyle = workbook.createCellStyle();
//textStyle.setBorderBottom(BorderStyle.THIN);
//textStyle.setBorderTop(BorderStyle.THIN);
//textStyle.setBorderLeft(BorderStyle.THIN);
//textStyle.setBorderRight(BorderStyle.THIN);
//
////Create the font
//Font font = workbook.createFont();
//font.setFontHeightInPoints((short)8); // size 8
//font.setFontName("Arial");    
//
//CellStyle numberStyle = workbook.createCellStyle();
////numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
//numberStyle.setBorderBottom(BorderStyle.THIN);
//numberStyle.setBorderTop(BorderStyle.THIN);
//numberStyle.setBorderLeft(BorderStyle.THIN);
//numberStyle.setBorderRight(BorderStyle.THIN);
//numberStyle.setFont(font);
////--- End of Style Definitions ---
//
//int startRow = 11;
//
//if (!dataList1.isEmpty()) {
//for (int i = 0; i < dataList1.size(); i++) {
//
//CPR_STRUCT_LIQ_Summary_Entity record = dataList1.get(i);
//System.out.println("rownumber=" + startRow + i);
//Row row = sheet.getRow(startRow + i);
//if (row == null) {
//row = sheet.createRow(startRow + i);
//}
//
//
//Cell cell1 = row.createCell(1);
//if (record.getR12_AMOUNT() != null) {
//    cell1.setCellValue(record.getR12_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(12);
// cell1 = row.createCell(1);
//if (record.getR13_AMOUNT() != null) {
//    cell1.setCellValue(record.getR13_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(13);
//cell1 = row.createCell(1);
//if (record.getR14_AMOUNT() != null) {
//    cell1.setCellValue(record.getR14_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(16);
//cell1 = row.createCell(1);
//if (record.getR17_AMOUNT() != null) {
//    cell1.setCellValue(record.getR17_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(17);
//cell1 = row.createCell(1);
//if (record.getR18_AMOUNT() != null) {
//    cell1.setCellValue(record.getR18_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(18);
//cell1 = row.createCell(1);
//if (record.getR19_AMOUNT() != null) {
//    cell1.setCellValue(record.getR19_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(19);
//cell1 = row.createCell(1);
//if (record.getR20_AMOUNT() != null) {
//    cell1.setCellValue(record.getR20_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//
//row = sheet.getRow(22);
//cell1 = row.createCell(1);
//if (record.getR23_AMOUNT() != null) {
//    cell1.setCellValue(record.getR23_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(23);
//cell1 = row.createCell(1);
//if (record.getR24_AMOUNT() != null) {
//    cell1.setCellValue(record.getR24_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//
//row = sheet.getRow(25);
//cell1 = row.createCell(1);
//if (record.getR26_AMOUNT() != null) {
//    cell1.setCellValue(record.getR26_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(26);
//cell1 = row.createCell(1);
//if (record.getR27_AMOUNT() != null) {
//    cell1.setCellValue(record.getR27_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//
//row = sheet.getRow(28);
//cell1 = row.createCell(1);
//if (record.getR29_AMOUNT() != null) {
//    cell1.setCellValue(record.getR29_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(29);
//cell1 = row.createCell(1);
//if (record.getR30_AMOUNT() != null) {
//    cell1.setCellValue(record.getR30_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//
//row = sheet.getRow(31);
//cell1 = row.createCell(1);
//if (record.getR32_AMOUNT() != null) {
//    cell1.setCellValue(record.getR32_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//
//row = sheet.getRow(41);
//cell1 = row.createCell(1);
//if (record.getR42_AMOUNT() != null) {
//    cell1.setCellValue(record.getR42_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(43);
//cell1 = row.createCell(1);
//if (record.getR44_AMOUNT() != null) {
//    cell1.setCellValue(record.getR44_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//
//row = sheet.getRow(47);
//cell1 = row.createCell(1);
//if (record.getR48_AMOUNT() != null) {
//    cell1.setCellValue(record.getR48_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(48);
//cell1 = row.createCell(1);
//if (record.getR49_AMOUNT() != null) {
//    cell1.setCellValue(record.getR49_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//
//row = sheet.getRow(51);
//cell1 = row.createCell(1);
//if (record.getR52_AMOUNT() != null) {
//    cell1.setCellValue(record.getR52_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(52);
//cell1 = row.createCell(1);
//if (record.getR53_AMOUNT() != null) {
//    cell1.setCellValue(record.getR53_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(53);
//cell1 = row.createCell(1);
//if (record.getR54_AMOUNT() != null) {
//    cell1.setCellValue(record.getR54_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(54);
//cell1 = row.createCell(1);
//if (record.getR55_AMOUNT() != null) {
//    cell1.setCellValue(record.getR55_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(55);
//cell1 = row.createCell(1);
//if (record.getR56_AMOUNT() != null) {
//    cell1.setCellValue(record.getR56_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(56);
//cell1 = row.createCell(1);
//if (record.getR57_AMOUNT() != null) {
//    cell1.setCellValue(record.getR57_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(57);
//cell1 = row.createCell(1);
//if (record.getR58_AMOUNT() != null) {
//    cell1.setCellValue(record.getR58_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(58);
//cell1 = row.createCell(1);
//if (record.getR59_AMOUNT() != null) {
//    cell1.setCellValue(record.getR59_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(59);
//cell1 = row.createCell(1);
//if (record.getR60_AMOUNT() != null) {
//    cell1.setCellValue(record.getR60_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(60);
//cell1 = row.createCell(1);
//if (record.getR61_AMOUNT() != null) {
//    cell1.setCellValue(record.getR61_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(61);
//cell1 = row.createCell(1);
//if (record.getR62_AMOUNT() != null) {
//    cell1.setCellValue(record.getR62_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(64);
//cell1 = row.createCell(1);
//if (record.getR65_AMOUNT() != null) {
//    cell1.setCellValue(record.getR65_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(66);
//cell1 = row.createCell(1);
//if (record.getR67_AMOUNT() != null) {
//    cell1.setCellValue(record.getR67_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(67);
//cell1 = row.createCell(1);
//if (record.getR68_AMOUNT() != null) {
//    cell1.setCellValue(record.getR68_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(68);
//cell1 = row.createCell(1);
//if (record.getR69_AMOUNT() != null) {
//    cell1.setCellValue(record.getR69_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(69);
//cell1 = row.createCell(1);
//if (record.getR70_AMOUNT() != null) {
//    cell1.setCellValue(record.getR70_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(70);
//cell1 = row.createCell(1);
//if (record.getR71_AMOUNT() != null) {
//    cell1.setCellValue(record.getR71_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(71);
//cell1 = row.createCell(1);
//if (record.getR72_AMOUNT() != null) {
//    cell1.setCellValue(record.getR72_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//
//row = sheet.getRow(73);
//cell1 = row.createCell(1);
//if (record.getR74_AMOUNT() != null) {
//    cell1.setCellValue(record.getR74_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(74);
//cell1 = row.createCell(1);
//if (record.getR75_AMOUNT() != null) {
//    cell1.setCellValue(record.getR75_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(75);
//cell1 = row.createCell(1);
//if (record.getR76_AMOUNT() != null) {
//    cell1.setCellValue(record.getR76_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(85);
//cell1 = row.createCell(1);
//if (record.getR86_AMOUNT() != null) {
//    cell1.setCellValue(record.getR86_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(86);
//cell1 = row.createCell(1);
//if (record.getR87_AMOUNT() != null) {
//    cell1.setCellValue(record.getR87_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(87);
//cell1 = row.createCell(1);
//if (record.getR88_AMOUNT() != null) {
//    cell1.setCellValue(record.getR88_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(88);
//cell1 = row.createCell(1);
//if (record.getR89_AMOUNT() != null) {
//    cell1.setCellValue(record.getR89_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(89);
//cell1 = row.createCell(1);
//if (record.getR90_AMOUNT() != null) {
//    cell1.setCellValue(record.getR90_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(90);
//cell1 = row.createCell(1);
//if (record.getR91_AMOUNT() != null) {
//    cell1.setCellValue(record.getR91_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(92);
//cell1 = row.createCell(1);
//if (record.getR93_AMOUNT() != null) {
//    cell1.setCellValue(record.getR93_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(93);
//cell1 = row.createCell(1);
//if (record.getR94_AMOUNT() != null) {
//    cell1.setCellValue(record.getR94_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(94);
//cell1 = row.createCell(1);
//if (record.getR95_AMOUNT() != null) {
//    cell1.setCellValue(record.getR95_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(95);
//cell1 = row.createCell(1);
//if (record.getR96_AMOUNT() != null) {
//    cell1.setCellValue(record.getR96_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(96);
//cell1 = row.createCell(1);
//if (record.getR97_AMOUNT() != null) {
//    cell1.setCellValue(record.getR97_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(97);
//cell1 = row.createCell(1);
//if (record.getR98_AMOUNT() != null) {
//    cell1.setCellValue(record.getR98_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(98);
//cell1 = row.createCell(1);
//if (record.getR99_AMOUNT() != null) {
//    cell1.setCellValue(record.getR99_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//
//row = sheet.getRow(100);
//cell1 = row.createCell(1);
//if (record.getR101_AMOUNT() != null) {
//    cell1.setCellValue(record.getR101_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(101);
//cell1 = row.createCell(1);
//if (record.getR102_AMOUNT() != null) {
//    cell1.setCellValue(record.getR102_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(102);
//cell1 = row.createCell(1);
//if (record.getR103_AMOUNT() != null) {
//    cell1.setCellValue(record.getR103_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(103);
//cell1 = row.createCell(1);
//if (record.getR104_AMOUNT() != null) {
//    cell1.setCellValue(record.getR104_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(104);
//cell1 = row.createCell(1);
//if (record.getR105_AMOUNT() != null) {
//    cell1.setCellValue(record.getR105_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//
//row = sheet.getRow(106);
//cell1 = row.createCell(1);
//if (record.getR107_AMOUNT() != null) {
//    cell1.setCellValue(record.getR107_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(107);
//cell1 = row.createCell(1);
//if (record.getR108_AMOUNT() != null) {
//    cell1.setCellValue(record.getR108_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(109);
//cell1 = row.createCell(1);
//if (record.getR110_AMOUNT() != null) {
//    cell1.setCellValue(record.getR110_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(110);
//cell1 = row.createCell(1);
//if (record.getR111_AMOUNT() != null) {
//    cell1.setCellValue(record.getR111_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(111);
//cell1 = row.createCell(1);
//if (record.getR112_AMOUNT() != null) {
//    cell1.setCellValue(record.getR112_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(112);
//cell1 = row.createCell(1);
//if (record.getR113_AMOUNT() != null) {
//    cell1.setCellValue(record.getR113_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(113);
//cell1 = row.createCell(1);
//if (record.getR114_AMOUNT() != null) {
//    cell1.setCellValue(record.getR114_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(114);
//cell1 = row.createCell(1);
//if (record.getR115_AMOUNT() != null) {
//    cell1.setCellValue(record.getR115_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(115);
//cell1 = row.createCell(1);
//if (record.getR116_AMOUNT() != null) {
//    cell1.setCellValue(record.getR116_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(116);
//cell1 = row.createCell(1);
//if (record.getR117_AMOUNT() != null) {
//    cell1.setCellValue(record.getR117_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(117);
//cell1 = row.createCell(1);
//if (record.getR118_AMOUNT() != null) {
//    cell1.setCellValue(record.getR118_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(118);
//cell1 = row.createCell(1);
//if (record.getR119_AMOUNT() != null) {
//    cell1.setCellValue(record.getR119_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(119);
//cell1 = row.createCell(1);
//if (record.getR120_AMOUNT() != null) {
//    cell1.setCellValue(record.getR120_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(120);
//cell1 = row.createCell(1);
//if (record.getR121_AMOUNT() != null) {
//    cell1.setCellValue(record.getR121_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(121);
//cell1 = row.createCell(1);
//if (record.getR122_AMOUNT() != null) {
//    cell1.setCellValue(record.getR122_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(122);
//cell1 = row.createCell(1);
//if (record.getR123_AMOUNT() != null) {
//    cell1.setCellValue(record.getR123_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(123);
//cell1 = row.createCell(1);
//if (record.getR124_AMOUNT() != null) {
//    cell1.setCellValue(record.getR124_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(124);
//cell1 = row.createCell(1);
//if (record.getR125_AMOUNT() != null) {
//    cell1.setCellValue(record.getR125_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(125);
//cell1 = row.createCell(1);
//if (record.getR126_AMOUNT() != null) {
//    cell1.setCellValue(record.getR126_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(126);
//cell1 = row.createCell(1);
//if (record.getR127_AMOUNT() != null) {
//    cell1.setCellValue(record.getR127_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(127);
//cell1 = row.createCell(1);
//if (record.getR128_AMOUNT() != null) {
//    cell1.setCellValue(record.getR128_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//row = sheet.getRow(128);
//cell1 = row.createCell(1);
//if (record.getR129_AMOUNT() != null) {
//    cell1.setCellValue(record.getR129_AMOUNT().doubleValue());
//    cell1.setCellStyle(numberStyle);
//} else {
//    cell1.setCellValue("");
//    cell1.setCellStyle(textStyle);
//}
//
//}
//workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
//} else {
//
//}
//// Write the final workbook content to the in-memory stream.
//workbook.write(out);
//logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
//return out.toByteArray();
//}
//}

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

//	public byte[] getExcelCPR_STRUCT_LIQARCHIVAL(String filename, String reportId, String fromdate, String todate,
//			String currency, String dtltype, String type, String version) throws Exception {
//		logger.info("Service: Starting Excel generation process in memory.");
//		if (type.equals("ARCHIVAL") & version != null) {
//
//		}
//		List<CPR_STRUCT_LIQ_Archival_Summary_Entity> dataList = BRRS_CPR_STRUCT_LIQ_Archival_Summary_Repo
//				.getdatabydateListarchival(dateformat.parse(todate), version);
//		
//
//		if (dataList.isEmpty()) {
//			logger.warn("Service: No data found for ADISB1 report. Returning empty result.");
//			return new byte[0];
//		}
//
//		String templateDir = env.getProperty("output.exportpathtemp");
//		String templateFileName = filename;
//		System.out.println(filename);
//		Path templatePath = Paths.get(templateDir, templateFileName);
//		System.out.println(templatePath);
//
//		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());
//
//		if (!Files.exists(templatePath)) {
//			// This specific exception will be caught by the controller.
//			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
//		}
//		if (!Files.isReadable(templatePath)) {
//			// A specific exception for permission errors.
//			throw new SecurityException(
//					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
//		}
//
//		// This try-with-resources block is perfect. It guarantees all resources are
//		// closed automatically.
//		try (InputStream templateInputStream = Files.newInputStream(templatePath);
//				Workbook workbook = WorkbookFactory.create(templateInputStream);
//				ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//			Sheet sheet = workbook.getSheetAt(0);
//
//			// --- Style Definitions ---
//			CreationHelper createHelper = workbook.getCreationHelper();
//
//			CellStyle dateStyle = workbook.createCellStyle();
//			dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
//			dateStyle.setBorderBottom(BorderStyle.THIN);
//			dateStyle.setBorderTop(BorderStyle.THIN);
//			dateStyle.setBorderLeft(BorderStyle.THIN);
//			dateStyle.setBorderRight(BorderStyle.THIN);
//			CellStyle textStyle = workbook.createCellStyle();
//			textStyle.setBorderBottom(BorderStyle.THIN);
//			textStyle.setBorderTop(BorderStyle.THIN);
//			textStyle.setBorderLeft(BorderStyle.THIN);
//			textStyle.setBorderRight(BorderStyle.THIN);
//
//			// Create the font
//			Font font = workbook.createFont();
//			font.setFontHeightInPoints((short) 8); // size 8
//			font.setFontName("Arial");
//			CellStyle numberStyle = workbook.createCellStyle();
//			// numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
//			numberStyle.setBorderBottom(BorderStyle.THIN);
//			numberStyle.setBorderTop(BorderStyle.THIN);
//			numberStyle.setBorderLeft(BorderStyle.THIN);
//			numberStyle.setBorderRight(BorderStyle.THIN);
//			numberStyle.setFont(font);
//			// --- End of Style Definitions --
//			int startRow = 11;
//			
//			if (!dataList.isEmpty()) {
//				for (int i = 0; i < dataList.size(); i++) {
//
//				CPR_STRUCT_LIQ_Archival_Summary_Entity record1 = dataList.get(i);
//				System.out.println("rownumber=" + startRow + i);
//				Row row = sheet.getRow(startRow + i);
//				if (row == null) {
//				row = sheet.createRow(startRow + i);
//				}
//				
//				Cell cell1 = row.createCell(1);
//				if (record1.getR12_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR12_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(12);
//				 cell1 = row.createCell(1);
//				if (record1.getR13_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR13_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(13);
//				cell1 = row.createCell(1);
//				if (record1.getR14_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR14_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(16);
//				cell1 = row.createCell(1);
//				if (record1.getR17_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR17_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(17);
//				cell1 = row.createCell(1);
//				if (record1.getR18_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR18_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(18);
//				cell1 = row.createCell(1);
//				if (record1.getR19_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR19_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(19);
//				cell1 = row.createCell(1);
//				if (record1.getR20_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR20_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//
//				row = sheet.getRow(22);
//				cell1 = row.createCell(1);
//				if (record1.getR23_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR23_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(23);
//				cell1 = row.createCell(1);
//				if (record1.getR24_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR24_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//
//				row = sheet.getRow(25);
//				cell1 = row.createCell(1);
//				if (record1.getR26_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR26_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(26);
//				cell1 = row.createCell(1);
//				if (record1.getR27_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR27_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//
//				row = sheet.getRow(28);
//				cell1 = row.createCell(1);
//				if (record1.getR29_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR29_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(29);
//				cell1 = row.createCell(1);
//				if (record1.getR30_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR30_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//
//				row = sheet.getRow(31);
//				cell1 = row.createCell(1);
//				if (record1.getR32_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR32_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//
//				row = sheet.getRow(41);
//				cell1 = row.createCell(1);
//				if (record1.getR42_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR42_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(43);
//				cell1 = row.createCell(1);
//				if (record1.getR44_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR44_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//
//				row = sheet.getRow(47);
//				cell1 = row.createCell(1);
//				if (record1.getR48_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR48_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(48);
//				cell1 = row.createCell(1);
//				if (record1.getR49_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR49_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//
//				row = sheet.getRow(51);
//				cell1 = row.createCell(1);
//				if (record1.getR52_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR52_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(52);
//				cell1 = row.createCell(1);
//				if (record1.getR53_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR53_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(53);
//				cell1 = row.createCell(1);
//				if (record1.getR54_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR54_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(54);
//				cell1 = row.createCell(1);
//				if (record1.getR55_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR55_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(55);
//				cell1 = row.createCell(1);
//				if (record1.getR56_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR56_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(56);
//				cell1 = row.createCell(1);
//				if (record1.getR57_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR57_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(57);
//				cell1 = row.createCell(1);
//				if (record1.getR58_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR58_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(58);
//				cell1 = row.createCell(1);
//				if (record1.getR59_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR59_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(59);
//				cell1 = row.createCell(1);
//				if (record1.getR60_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR60_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(60);
//				cell1 = row.createCell(1);
//				if (record1.getR61_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR61_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(61);
//				cell1 = row.createCell(1);
//				if (record1.getR62_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR62_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(64);
//				cell1 = row.createCell(1);
//				if (record1.getR65_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR65_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(66);
//				cell1 = row.createCell(1);
//				if (record1.getR67_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR67_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(67);
//				cell1 = row.createCell(1);
//				if (record1.getR68_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR68_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(68);
//				cell1 = row.createCell(1);
//				if (record1.getR69_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR69_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(69);
//				cell1 = row.createCell(1);
//				if (record1.getR70_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR70_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(70);
//				cell1 = row.createCell(1);
//				if (record1.getR71_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR71_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(71);
//				cell1 = row.createCell(1);
//				if (record1.getR72_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR72_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//
//				row = sheet.getRow(73);
//				cell1 = row.createCell(1);
//				if (record1.getR74_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR74_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(74);
//				cell1 = row.createCell(1);
//				if (record1.getR75_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR75_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(75);
//				cell1 = row.createCell(1);
//				if (record1.getR76_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR76_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(85);
//				cell1 = row.createCell(1);
//				if (record1.getR86_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR86_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(86);
//				cell1 = row.createCell(1);
//				if (record1.getR87_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR87_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(87);
//				cell1 = row.createCell(1);
//				if (record1.getR88_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR88_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(88);
//				cell1 = row.createCell(1);
//				if (record1.getR89_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR89_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(89);
//				cell1 = row.createCell(1);
//				if (record1.getR90_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR90_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(90);
//				cell1 = row.createCell(1);
//				if (record1.getR91_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR91_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(92);
//				cell1 = row.createCell(1);
//				if (record1.getR93_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR93_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(93);
//				cell1 = row.createCell(1);
//				if (record1.getR94_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR94_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(94);
//				cell1 = row.createCell(1);
//				if (record1.getR95_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR95_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(95);
//				cell1 = row.createCell(1);
//				if (record1.getR96_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR96_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(96);
//				cell1 = row.createCell(1);
//				if (record1.getR97_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR97_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(97);
//				cell1 = row.createCell(1);
//				if (record1.getR98_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR98_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(98);
//				cell1 = row.createCell(1);
//				if (record1.getR99_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR99_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//
//				row = sheet.getRow(100);
//				cell1 = row.createCell(1);
//				if (record1.getR101_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR101_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(101);
//				cell1 = row.createCell(1);
//				if (record1.getR102_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR102_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(102);
//				cell1 = row.createCell(1);
//				if (record1.getR103_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR103_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(103);
//				cell1 = row.createCell(1);
//				if (record1.getR104_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR104_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(104);
//				cell1 = row.createCell(1);
//				if (record1.getR105_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR105_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//
//				row = sheet.getRow(106);
//				cell1 = row.createCell(1);
//				if (record1.getR107_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR107_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(107);
//				cell1 = row.createCell(1);
//				if (record1.getR108_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR108_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(109);
//				cell1 = row.createCell(1);
//				if (record1.getR110_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR110_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(110);
//				cell1 = row.createCell(1);
//				if (record1.getR111_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR111_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(111);
//				cell1 = row.createCell(1);
//				if (record1.getR112_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR112_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(112);
//				cell1 = row.createCell(1);
//				if (record1.getR113_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR113_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(113);
//				cell1 = row.createCell(1);
//				if (record1.getR114_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR114_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(114);
//				cell1 = row.createCell(1);
//				if (record1.getR115_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR115_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(115);
//				cell1 = row.createCell(1);
//				if (record1.getR116_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR116_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(116);
//				cell1 = row.createCell(1);
//				if (record1.getR117_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR117_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(117);
//				cell1 = row.createCell(1);
//				if (record1.getR118_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR118_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(118);
//				cell1 = row.createCell(1);
//				if (record1.getR119_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR119_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(119);
//				cell1 = row.createCell(1);
//				if (record1.getR120_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR120_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(120);
//				cell1 = row.createCell(1);
//				if (record1.getR121_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR121_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(121);
//				cell1 = row.createCell(1);
//				if (record1.getR122_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR122_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(122);
//				cell1 = row.createCell(1);
//				if (record1.getR123_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR123_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(123);
//				cell1 = row.createCell(1);
//				if (record1.getR124_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR124_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(124);
//				cell1 = row.createCell(1);
//				if (record1.getR125_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR125_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(125);
//				cell1 = row.createCell(1);
//				if (record1.getR126_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR126_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(126);
//				cell1 = row.createCell(1);
//				if (record1.getR127_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR127_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(127);
//				cell1 = row.createCell(1);
//				if (record1.getR128_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR128_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				row = sheet.getRow(128);
//				cell1 = row.createCell(1);
//				if (record1.getR129_AMOUNT() != null) {
//				    cell1.setCellValue(record1.getR129_AMOUNT().doubleValue());
//				    cell1.setCellStyle(numberStyle);
//				} else {
//				    cell1.setCellValue("");
//				    cell1.setCellStyle(textStyle);
//				}
//
//				}
//				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
//				} else {
//
//				}
//				// Write the final workbook content to the in-memory stream.
//				workbook.write(out);
//				logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
//				return out.toByteArray();
//				}
//				}

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