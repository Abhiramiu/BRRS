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
import com.bornfire.brrs.entities.ADISB1_Archival_Detail_Entity;
import com.bornfire.brrs.entities.ADISB1_Archival_Summary_Entity;
import com.bornfire.brrs.entities.ADISB1_Detail_Entity;
import com.bornfire.brrs.entities.ADISB1_Summary_Entity;
import com.bornfire.brrs.entities.ADISB1_Manual_Summary_Entity;
import com.bornfire.brrs.entities.ADISB1_Manual_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_ADISB1_Manual_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_ADISB1_Manual_Archival_Summary_Repo;

@Component
@Service
public class BRRS_ADISB1_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_ADISB1_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_ADISB1_Detail_Repo BRRS_ADISB1_Detail_Repo;

	@Autowired
	BRRS_ADISB1_Summary_Repo BRRS_ADISB1_Summary_Repo;

	@Autowired
	BRRS_ADISB1_Manual_Summary_Repo BRRS_ADISB1_Manual_Summary_Repo;


	@Autowired
	BRRS_ADISB1_Archival_Detail_Repo BRRS_ADISB1_Archival_Detail_Repo;

	@Autowired
	BRRS_ADISB1_Archival_Summary_Repo BRRS_ADISB1_Archival_Summary_Repo;

	@Autowired
	BRRS_ADISB1_Manual_Archival_Summary_Repo BRRS_ADISB1_Manual_Archival_Summary_Repo;



	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getADISB1View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type,  String version) {
		ModelAndView mv = new ModelAndView();
//		Session hs = sessionFactory.getCurrentSession();
//		int pageSize = pageable.getPageSize();
//		int currentPage = pageable.getPageNumber();
//		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<ADISB1_Archival_Summary_Entity> T1Master = new ArrayList<ADISB1_Archival_Summary_Entity>();
			List<ADISB1_Manual_Archival_Summary_Entity> T2Master = new ArrayList<ADISB1_Manual_Archival_Summary_Entity>();

			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_ADISB1_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
				T2Master = BRRS_ADISB1_Manual_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
				

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);
            
		} else {
			List<ADISB1_Summary_Entity> T1Master = new ArrayList<ADISB1_Summary_Entity>();
			List<ADISB1_Manual_Summary_Entity> T2Master = new ArrayList<ADISB1_Manual_Summary_Entity>();
			
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_ADISB1_Summary_Repo.getdatabydateList(dateformat.parse(todate));
				T2Master = BRRS_ADISB1_Manual_Summary_Repo.getdatabydateList(dateformat.parse(todate));
				

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);
            
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/ADISB1");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

//	public ModelAndView getADISB1currentDtl(String reportId, String fromdate, String todate, String currency,
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
//			List<ADISB1_Archival_Detail_Entity> T1Dt1;
//			if (rowId != null && columnId != null) {
//				T1Dt1 = BRRS_ADISB1_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
//			} else {
//				T1Dt1 = BRRS_ADISB1_Archival_Detail_Repo.getdatabydateList(parsedDate, version);					
//			}
//
//			mv.addObject("reportdetails", T1Dt1);
//			mv.addObject("reportmaster12", T1Dt1);
//			System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
//
//		} else {
//			// 🔹 Current branch
//			List<ADISB1_Detail_Entity> T1Dt1;
//			if (rowId != null && columnId != null) {
//				T1Dt1 = BRRS_ADISB1_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
//			} else {
//				T1Dt1 = BRRS_ADISB1_Detail_Repo.getdatabydateList(parsedDate);
//				totalPages = BRRS_ADISB1_Detail_Repo.getdatacount(parsedDate);
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
//	mv.setViewName("BRRS/ADISB1");
//	mv.addObject("displaymode", "Details");
//	mv.addObject("currentPage", currentPage);
//	System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
//	mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
//	mv.addObject("reportsflag", "reportsflag");
//	mv.addObject("menu", reportId);
//
//	return mv;
//}

	public ModelAndView getADISB1currentDtl(String reportId, String fromdate, String todate, String currency,
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
			List<ADISB1_Archival_Detail_Entity> T1Dt1;
			if (rowId != null && columnId != null) {
				T1Dt1 = BRRS_ADISB1_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
			} else {
				T1Dt1 = BRRS_ADISB1_Archival_Detail_Repo.getdatabydateList(parsedDate, version);					
			}

			mv.addObject("reportdetails", T1Dt1);
			mv.addObject("reportmaster12", T1Dt1);
			System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

		} else {
			// 🔹 Current branch
			List<ADISB1_Detail_Entity> T1Dt1;
			if (rowId != null && columnId != null) {
				T1Dt1 = BRRS_ADISB1_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
			} else {
				T1Dt1 = BRRS_ADISB1_Detail_Repo.getdatabydateList(parsedDate);
				totalPages = BRRS_ADISB1_Detail_Repo.getdatacount(parsedDate);
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
	mv.setViewName("BRRS/ADISB1");
	mv.addObject("displaymode", "Details");
	mv.addObject("currentPage", currentPage);
	System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
	mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
	mv.addObject("reportsflag", "reportsflag");
	mv.addObject("menu", reportId);

	return mv;
}

	
	public void updateReport(ADISB1_Manual_Summary_Entity updatedEntity) {
	    System.out.println("Came to services1");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    ADISB1_Manual_Summary_Entity existing = BRRS_ADISB1_Manual_Summary_Repo.findById(updatedEntity.getReport_date())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

	    try {
	        // ✅ Loop for fields
	        int[] Rows = {23,25,26,30,34,35,42,43,44,46};
	        for (int i : Rows) {
	            String prefix = "R" + i + "_";
	            String[] fields = {"total_no_of_acct", "total_value"};

	            for (String field : fields) {
	                try {
	                    String getterName = "get" + prefix + field;
	                    String setterName = "set" + prefix + field;

	                    Method getter = ADISB1_Manual_Summary_Entity.class.getMethod(getterName);
	                    Method setter = ADISB1_Manual_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing getter/setter gracefully
	                    continue;
	                }
	            }
	        }


	        // ✅ Save after all updates
	        BRRS_ADISB1_Manual_Summary_Repo.save(existing);

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }
	}

	
	
	
	public byte[] getADISB1DetailExcel(String filename, String fromdate, String todate, String currency,
			   String dtltype, String type, String version) {
try {
logger.info("Generating Excel for ADISB1 Details...");
System.out.println("came to Detail download service");


if (type.equals("ARCHIVAL") & version != null) {
byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
version);
return ARCHIVALreport;
}

XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("ADISB1Detail");

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
List<ADISB1_Detail_Entity> reportData = BRRS_ADISB1_Detail_Repo.getdatabydateList(parsedToDate);

if (reportData != null && !reportData.isEmpty()) {
int rowIndex = 1;
for (ADISB1_Detail_Entity item : reportData) {
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
	logger.info("No data found for ADISB1 — only header will be written.");
}

//Write to byte[]
ByteArrayOutputStream bos = new ByteArrayOutputStream();
workbook.write(bos);
workbook.close();

logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
return bos.toByteArray();

} catch (Exception e) {
logger.error("Error generating ADISB1 Excel", e);
return new byte[0];
}
}
//	public byte[] BRRS_ADISB1DetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
//			String type, String version) {
//
//		try {
//			logger.info("Generating Excel for BRRS_ADISB1Details...");
//			System.out.println("came to Detail download service");
//			if (type.equals("ARCHIVAL") & version != null) {
//				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
//						version);
//				return ARCHIVALreport;
//			}
//			XSSFWorkbook workbook = new XSSFWorkbook();
//			XSSFSheet sheet = workbook.createSheet("BRRS_ADISB1Details");
//
//			// Common border style
//			BorderStyle border = BorderStyle.THIN;
//			// Header style (left aligned)
//			CellStyle headerStyle = workbook.createCellStyle();
//			Font headerFont = workbook.createFont();
//			headerFont.setBold(true);
//			headerFont.setFontHeightInPoints((short) 10);
//			headerStyle.setFont(headerFont);
//			headerStyle.setAlignment(HorizontalAlignment.LEFT);
//			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//			headerStyle.setBorderTop(border);
//			headerStyle.setBorderBottom(border);
//			headerStyle.setBorderLeft(border);
//			headerStyle.setBorderRight(border);
//
//			// Right-aligned header style for ACCT BALANCE
//			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
//			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
//			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);
//
//			// Default data style (left aligned)
//			CellStyle dataStyle = workbook.createCellStyle();
//			dataStyle.setAlignment(HorizontalAlignment.LEFT);
//			dataStyle.setBorderTop(border);
//			dataStyle.setBorderBottom(border);
//			dataStyle.setBorderLeft(border);
//			dataStyle.setBorderRight(border);
//
//			// ACCT BALANCE style (right aligned with 3 decimals)
//			CellStyle balanceStyle = workbook.createCellStyle();
//			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
//			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
//			balanceStyle.setBorderTop(border);
//			balanceStyle.setBorderBottom(border);
//			balanceStyle.setBorderLeft(border);
//			balanceStyle.setBorderRight(border);
//			// Header row
//			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "ROWID", "COLUMNID",
//					"REPORT_DATE" };
//			XSSFRow headerRow = sheet.createRow(0);
//			for (int i = 0; i < headers.length; i++) {
//				Cell cell = headerRow.createCell(i);
//				cell.setCellValue(headers[i]);
//				if (i == 3) { // ACCT BALANCE
//					cell.setCellStyle(rightAlignedHeaderStyle);
//				} else {
//					cell.setCellStyle(headerStyle);
//				}
//				sheet.setColumnWidth(i, 5000);
//			}
//			// Get data
//			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
//			List<ADISB1_Detail_Entity> reportData = BRRS_ADISB1_Detail_Repo.getdatabydateList(parsedToDate);
//			if (reportData != null && !reportData.isEmpty()) {
//				int rowIndex = 1;
//				for (ADISB1_Detail_Entity item : reportData) {
//					XSSFRow row = sheet.createRow(rowIndex++);
//					row.createCell(0).setCellValue(item.getCustId());
//					row.createCell(1).setCellValue(item.getAcctNumber());
//					row.createCell(2).setCellValue(item.getAcctName());
//					// ACCT BALANCE (right aligned, 3 decimal places)
//					Cell balanceCell = row.createCell(3);
//					if (item.getAcctBalanceInpula() != null) {
//						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
//					} else {
//						balanceCell.setCellValue(0.000);
//					}
//					balanceCell.setCellStyle(balanceStyle);
//					row.createCell(4).setCellValue(item.getRowId());
//					row.createCell(5).setCellValue(item.getColumnId());
//					row.createCell(6)
//							.setCellValue(item.getReportDate() != null
//									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
//									: "");
//					// Apply data style for all other cells
//					for (int j = 0; j < 7; j++) {
//						if (j != 3) {
//							row.getCell(j).setCellStyle(dataStyle);
//						}
//					}
//				}
//			} else {
//				logger.info("No data found for BRRS_ADISB1— only header will be written.");
//			}
//			// Write to byte[]
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			workbook.write(bos);
//			workbook.close();
//			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
//			return bos.toByteArray();
//		} catch (Exception e) {
//			logger.error("Error generating BRRS_ADISB1Excel", e);
//			return new byte[0];
//		}
//	}

	public byte[] BRRS_ADISB1Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelADISB1ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data
		List<ADISB1_Summary_Entity> dataList = BRRS_ADISB1_Summary_Repo.getdatabydateList(dateformat.parse(todate));
		List<ADISB1_Manual_Summary_Entity> dataList1 = BRRS_ADISB1_Manual_Summary_Repo.getdatabydateList(dateformat.parse(todate));


		if (dataList.isEmpty() || dataList1.isEmpty()) {
			logger.warn("Service: No data found for BRRS report. Returning empty result.");
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
			int startRow = 6;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					ADISB1_Summary_Entity record = dataList.get(i);
					ADISB1_Manual_Summary_Entity record1 = dataList1.get(i);
					
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
// R7 Col B            
Cell R7cell1 = row.createCell(1);
if (record.getR7_total_no_of_acct() != null) {
    R7cell1.setCellValue(record.getR7_total_no_of_acct().doubleValue());
    R7cell1.setCellStyle(numberStyle);
} else {
    R7cell1.setCellValue("");
    R7cell1.setCellStyle(textStyle);
}

// R7 Col C
Cell R7cell2 = row.createCell(2);
if (record.getR7_total_value() != null) {
    R7cell2.setCellValue(record.getR7_total_value().doubleValue());
    R7cell2.setCellStyle(numberStyle);
} else {
    R7cell2.setCellValue("");
    R7cell2.setCellStyle(textStyle);
}
row = sheet.getRow(7);
//R8 Col B            
Cell R8cell1 = row.createCell(1);
if (record.getR8_total_no_of_acct() != null) {
 R8cell1.setCellValue(record.getR8_total_no_of_acct().doubleValue());
 R8cell1.setCellStyle(numberStyle);
} else {
 R8cell1.setCellValue("");
 R8cell1.setCellStyle(textStyle);
}

//R8 Col C
Cell R8cell2 = row.createCell(2);
if (record.getR8_total_value() != null) {
 R8cell2.setCellValue(record.getR8_total_value().doubleValue());
 R8cell2.setCellStyle(numberStyle);
} else {
 R8cell2.setCellValue("");
 R8cell2.setCellStyle(textStyle);
}
row = sheet.getRow(8);
//R9 Col B            
Cell R9cell1 = row.createCell(1);
if (record.getR9_total_no_of_acct() != null) {
 R9cell1.setCellValue(record.getR9_total_no_of_acct().doubleValue());
 R9cell1.setCellStyle(numberStyle);
} else {
 R9cell1.setCellValue("");
 R9cell1.setCellStyle(textStyle);
}

//R9 Col C
Cell R9cell2 = row.createCell(2);
if (record.getR9_total_value() != null) {
 R9cell2.setCellValue(record.getR9_total_value().doubleValue());
 R9cell2.setCellStyle(numberStyle);
} else {
 R9cell2.setCellValue("");
 R9cell2.setCellStyle(textStyle);
}
row = sheet.getRow(11);
//R12 Col B            
Cell R12cell1 = row.createCell(1);
if (record.getR12_total_no_of_acct() != null) {
 R12cell1.setCellValue(record.getR12_total_no_of_acct().doubleValue());
 R12cell1.setCellStyle(numberStyle);
} else {
 R12cell1.setCellValue("");
 R12cell1.setCellStyle(textStyle);
}

//R12 Col C
Cell R12cell2 = row.createCell(2);
if (record.getR12_total_value() != null) {
 R12cell2.setCellValue(record.getR12_total_value().doubleValue());
 R12cell2.setCellStyle(numberStyle);
} else {
 R12cell2.setCellValue("");
 R12cell2.setCellStyle(textStyle);
}
row = sheet.getRow(12);
//R13 Col B            
Cell R13cell1 = row.createCell(1);
if (record.getR13_total_no_of_acct() != null) {
 R13cell1.setCellValue(record.getR13_total_no_of_acct().doubleValue());
 R13cell1.setCellStyle(numberStyle);
} else {
 R13cell1.setCellValue("");
 R13cell1.setCellStyle(textStyle);
}

//R13 Col C
Cell R13cell2 = row.createCell(2);
if (record.getR13_total_value() != null) {
 R13cell2.setCellValue(record.getR13_total_value().doubleValue());
 R13cell2.setCellStyle(numberStyle);
} else {
 R13cell2.setCellValue("");
 R13cell2.setCellStyle(textStyle);
}

row = sheet.getRow(13);
//R14 Col B            
Cell R14cell1 = row.createCell(1);
if (record.getR14_total_no_of_acct() != null) {
R14cell1.setCellValue(record.getR14_total_no_of_acct().doubleValue());
R14cell1.setCellStyle(numberStyle);
} else {
R14cell1.setCellValue("");
R14cell1.setCellStyle(textStyle);
}

//R14 Col C
Cell R14cell2 = row.createCell(2);
if (record.getR14_total_value() != null) {
R14cell2.setCellValue(record.getR14_total_value().doubleValue());
R14cell2.setCellStyle(numberStyle);
} else {
R14cell2.setCellValue("");
R14cell2.setCellStyle(textStyle);
}

row = sheet.getRow(22);
//R23 Col B            
Cell R23cell1 = row.createCell(1);
if (record1.getR23_total_no_of_acct() != null) {
R23cell1.setCellValue(record1.getR23_total_no_of_acct().doubleValue());
R23cell1.setCellStyle(numberStyle);
} else {
R23cell1.setCellValue("");
R23cell1.setCellStyle(textStyle);
}

//R23 Col C
Cell R23cell2 = row.createCell(2);
if (record1.getR23_total_value() != null) {
R23cell2.setCellValue(record1.getR23_total_value().doubleValue());
R23cell2.setCellStyle(numberStyle);
} else {
R23cell2.setCellValue("");
R23cell2.setCellStyle(textStyle);
}

row = sheet.getRow(24);
//R25 Col B            
Cell R25cell1 = row.createCell(1);
if (record1.getR25_total_no_of_acct() != null) {
R25cell1.setCellValue(record1.getR25_total_no_of_acct().doubleValue());
R25cell1.setCellStyle(numberStyle);
} else {
R25cell1.setCellValue("");
R25cell1.setCellStyle(textStyle);
}

//R25 Col C
Cell R25cell2 = row.createCell(2);
if (record1.getR25_total_value() != null) {
R25cell2.setCellValue(record1.getR25_total_value().doubleValue());
R25cell2.setCellStyle(numberStyle);
} else {
R25cell2.setCellValue("");
R25cell2.setCellStyle(textStyle);
}

row = sheet.getRow(25);
//R26 Col B            
Cell R26cell1 = row.createCell(1);
if (record1.getR26_total_no_of_acct() != null) {
R26cell1.setCellValue(record1.getR26_total_no_of_acct().doubleValue());
R26cell1.setCellStyle(numberStyle);
} else {
R26cell1.setCellValue("");
R26cell1.setCellStyle(textStyle);
}

//R26 Col C
Cell R26cell2 = row.createCell(2);
if (record1.getR26_total_value() != null) {
R26cell2.setCellValue(record1.getR26_total_value().doubleValue());
R26cell2.setCellStyle(numberStyle);
} else {
R26cell2.setCellValue("");
R26cell2.setCellStyle(textStyle);
}

row = sheet.getRow(29);
//R30 Col B            
Cell R30cell1 = row.createCell(1);
if (record1.getR30_total_no_of_acct() != null) {
R30cell1.setCellValue(record1.getR30_total_no_of_acct().doubleValue());
R30cell1.setCellStyle(numberStyle);
} else {
R30cell1.setCellValue("");
R30cell1.setCellStyle(textStyle);
}

//R30 Col C
Cell R30cell2 = row.createCell(2);
if (record1.getR30_total_value() != null) {
R30cell2.setCellValue(record1.getR30_total_value().doubleValue());
R30cell2.setCellStyle(numberStyle);
} else {
R30cell2.setCellValue("");
R30cell2.setCellStyle(textStyle);
}

row = sheet.getRow(33);
//R34 Col B            
Cell R34cell1 = row.createCell(1);
if (record.getR34_total_no_of_acct() != null) {
R34cell1.setCellValue(record.getR34_total_no_of_acct().doubleValue());
R34cell1.setCellStyle(numberStyle);
} else {
R34cell1.setCellValue("");
R34cell1.setCellStyle(textStyle);
}

//R34 Col C
Cell R34cell2 = row.createCell(2);
if (record.getR34_total_value() != null) {
R34cell2.setCellValue(record.getR34_total_no_of_acct().doubleValue());
R34cell2.setCellStyle(numberStyle);
} else {
R34cell2.setCellValue("");
R34cell2.setCellStyle(textStyle);
}

row = sheet.getRow(34);
//R35 Col B            
Cell R35cell1 = row.createCell(1);
if (record1.getR35_total_no_of_acct() != null) {
R35cell1.setCellValue(record1.getR35_total_no_of_acct().doubleValue());
R35cell1.setCellStyle(numberStyle);
} else {
R35cell1.setCellValue("");
R35cell1.setCellStyle(textStyle);
}

//R35 Col C
Cell R35cell2 = row.createCell(2);
if (record1.getR35_total_value() != null) {
R35cell2.setCellValue(record1.getR35_total_value().doubleValue());
R35cell2.setCellStyle(numberStyle);
} else {
R35cell2.setCellValue("");
R35cell2.setCellStyle(textStyle);
}

row = sheet.getRow(37);
//R38 Col B            
Cell R38cell1 = row.createCell(1);
if (record.getR38_total_no_of_acct() != null) {
R38cell1.setCellValue(record.getR38_total_no_of_acct().doubleValue());
R38cell1.setCellStyle(numberStyle);
} else {
R38cell1.setCellValue("");
R38cell1.setCellStyle(textStyle);
}

//R38 Col C
Cell R38cell2 = row.createCell(2);
if (record.getR38_total_value() != null) {
R38cell2.setCellValue(record.getR38_total_value().doubleValue());
R38cell2.setCellStyle(numberStyle);
} else {
R38cell2.setCellValue("");
R38cell2.setCellStyle(textStyle);
}

row = sheet.getRow(38);
//R39 Col B            
Cell R39cell1 = row.createCell(1);
if (record.getR39_total_no_of_acct() != null) {
R39cell1.setCellValue(record.getR39_total_no_of_acct().doubleValue());
R39cell1.setCellStyle(numberStyle);
} else {
R39cell1.setCellValue("");
R39cell1.setCellStyle(textStyle);
}

//R39 Col C
Cell R39cell2 = row.createCell(2);
if (record.getR39_total_value() != null) {
R39cell2.setCellValue(record.getR39_total_value().doubleValue());
R39cell2.setCellStyle(numberStyle);
} else {
R39cell2.setCellValue("");
R39cell2.setCellStyle(textStyle);
}

row = sheet.getRow(41);
//R42 Col B            
Cell R42cell1 = row.createCell(1);
if (record1.getR42_total_no_of_acct() != null) {
R42cell1.setCellValue(record1.getR42_total_no_of_acct().doubleValue());
R42cell1.setCellStyle(numberStyle);
} else {
R42cell1.setCellValue("");
R42cell1.setCellStyle(textStyle);
}

//R42 Col C
Cell R42cell2 = row.createCell(2);
if (record1.getR42_total_value() != null) {
R42cell2.setCellValue(record1.getR42_total_value().doubleValue());
R42cell2.setCellStyle(numberStyle);
} else {
R42cell2.setCellValue("");
R42cell2.setCellStyle(textStyle);
}

row = sheet.getRow(42);
//R43 Col B            
Cell R43cell1 = row.createCell(1);
if (record1.getR43_total_no_of_acct() != null) {
R43cell1.setCellValue(record1.getR43_total_no_of_acct().doubleValue());
R43cell1.setCellStyle(numberStyle);
} else {
R43cell1.setCellValue("");
R43cell1.setCellStyle(textStyle);
}

//R43 Col C
Cell R43cell2 = row.createCell(2);
if (record1.getR43_total_value() != null) {
R43cell2.setCellValue(record1.getR43_total_value().doubleValue());
R43cell2.setCellStyle(numberStyle);
} else {
R43cell2.setCellValue("");
R43cell2.setCellStyle(textStyle);
}

row = sheet.getRow(43);
//R44 Col B            
Cell R44cell1 = row.createCell(1);
if (record1.getR44_total_no_of_acct() != null) {
R44cell1.setCellValue(record1.getR44_total_no_of_acct().doubleValue());
R44cell1.setCellStyle(numberStyle);
} else {
R44cell1.setCellValue("");
R44cell1.setCellStyle(textStyle);
}

//R44 Col C
Cell R44cell2 = row.createCell(2);
if (record1.getR44_total_value() != null) {
R44cell2.setCellValue(record1.getR44_total_value().doubleValue());
R44cell2.setCellStyle(numberStyle);
} else {
R44cell2.setCellValue("");
R44cell2.setCellStyle(textStyle);
}

row = sheet.getRow(45);
//R46 Col B            
Cell R46cell1 = row.createCell(1);
if (record1.getR46_total_no_of_acct() != null) {
R46cell1.setCellValue(record1.getR46_total_no_of_acct().doubleValue());
R46cell1.setCellStyle(numberStyle);
} else {
R46cell1.setCellValue("");
R46cell1.setCellStyle(textStyle);
}

//R46 Col C
Cell R46cell2 = row.createCell(2);
if (record1.getR46_total_value() != null) {
R46cell2.setCellValue(record1.getR46_total_value().doubleValue());
R46cell2.setCellStyle(numberStyle);
} else {
R46cell2.setCellValue("");
R46cell2.setCellStyle(textStyle);
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

	public List<Object> getADISB1Archival() {
		List<Object> ADISB1Archivallist = new ArrayList<>();
		try {
			ADISB1Archivallist = BRRS_ADISB1_Archival_Summary_Repo.getADISB1archival();
			ADISB1Archivallist = BRRS_ADISB1_Manual_Archival_Summary_Repo.getADISB1archival();
			System.out.println("countser" + ADISB1Archivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching ADISB1 Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return ADISB1Archivallist;
	}
	
	
	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			 String dtltype, String type, String version) {
try {
logger.info("Generating Excel for BRRS_ADISB1 ARCHIVAL Details...");
System.out.println("came to Detail download service");
if (type.equals("ARCHIVAL") & version != null) {

}
XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("ADISB1Detail");

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
List<ADISB1_Archival_Detail_Entity> reportData = BRRS_ADISB1_Archival_Detail_Repo.getdatabydateList(parsedToDate,version);

if (reportData != null && !reportData.isEmpty()) {
int rowIndex = 1;
for (ADISB1_Archival_Detail_Entity item : reportData) {
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
logger.info("No data found for ADISB1 — only header will be written.");
}
// Write to byte[]
ByteArrayOutputStream bos = new ByteArrayOutputStream();
workbook.write(bos);
workbook.close();

logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
return bos.toByteArray();

} catch (Exception e) {
logger.error("Error generating ADISB1 Excel", e);
return new byte[0];
}
}




	public byte[] getExcelADISB1ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<ADISB1_Archival_Summary_Entity> dataList = BRRS_ADISB1_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		List<ADISB1_Manual_Archival_Summary_Entity> dataList1 = BRRS_ADISB1_Manual_Archival_Summary_Repo
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
			// --- End of Style Definitions ---
			int startRow = 10;

			if (!dataList.isEmpty() || !dataList1.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					ADISB1_Archival_Summary_Entity record = dataList.get(i);
					ADISB1_Manual_Archival_Summary_Entity record1 = dataList1.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// R7 Col B            
					Cell R7cell1 = row.createCell(1);
					if (record.getR7_total_no_of_acct() != null) {
					    R7cell1.setCellValue(record.getR7_total_no_of_acct().doubleValue());
					    R7cell1.setCellStyle(numberStyle);
					} else {
					    R7cell1.setCellValue("");
					    R7cell1.setCellStyle(textStyle);
					}

					// R7 Col C
					Cell R7cell2 = row.createCell(2);
					if (record.getR7_total_value() != null) {
					    R7cell2.setCellValue(record.getR7_total_value().doubleValue());
					    R7cell2.setCellStyle(numberStyle);
					} else {
					    R7cell2.setCellValue("");
					    R7cell2.setCellStyle(textStyle);
					}
					row = sheet.getRow(7);
					//R8 Col B            
					Cell R8cell1 = row.createCell(1);
					if (record.getR8_total_no_of_acct() != null) {
					 R8cell1.setCellValue(record.getR8_total_no_of_acct().doubleValue());
					 R8cell1.setCellStyle(numberStyle);
					} else {
					 R8cell1.setCellValue("");
					 R8cell1.setCellStyle(textStyle);
					}

					//R8 Col C
					Cell R8cell2 = row.createCell(2);
					if (record.getR8_total_value() != null) {
					 R8cell2.setCellValue(record.getR8_total_value().doubleValue());
					 R8cell2.setCellStyle(numberStyle);
					} else {
					 R8cell2.setCellValue("");
					 R8cell2.setCellStyle(textStyle);
					}
					row = sheet.getRow(8);
					//R9 Col B            
					Cell R9cell1 = row.createCell(1);
					if (record.getR9_total_no_of_acct() != null) {
					 R9cell1.setCellValue(record.getR9_total_no_of_acct().doubleValue());
					 R9cell1.setCellStyle(numberStyle);
					} else {
					 R9cell1.setCellValue("");
					 R9cell1.setCellStyle(textStyle);
					}

					//R9 Col C
					Cell R9cell2 = row.createCell(2);
					if (record.getR9_total_value() != null) {
					 R9cell2.setCellValue(record.getR9_total_value().doubleValue());
					 R9cell2.setCellStyle(numberStyle);
					} else {
					 R9cell2.setCellValue("");
					 R9cell2.setCellStyle(textStyle);
					}
					row = sheet.getRow(11);
					//R12 Col B            
					Cell R12cell1 = row.createCell(1);
					if (record.getR12_total_no_of_acct() != null) {
					 R12cell1.setCellValue(record.getR12_total_no_of_acct().doubleValue());
					 R12cell1.setCellStyle(numberStyle);
					} else {
					 R12cell1.setCellValue("");
					 R12cell1.setCellStyle(textStyle);
					}

					//R12 Col C
					Cell R12cell2 = row.createCell(2);
					if (record.getR12_total_value() != null) {
					 R12cell2.setCellValue(record.getR12_total_value().doubleValue());
					 R12cell2.setCellStyle(numberStyle);
					} else {
					 R12cell2.setCellValue("");
					 R12cell2.setCellStyle(textStyle);
					}
					row = sheet.getRow(12);
					//R13 Col B            
					Cell R13cell1 = row.createCell(1);
					if (record.getR13_total_no_of_acct() != null) {
					 R13cell1.setCellValue(record.getR13_total_no_of_acct().doubleValue());
					 R13cell1.setCellStyle(numberStyle);
					} else {
					 R13cell1.setCellValue("");
					 R13cell1.setCellStyle(textStyle);
					}

					//R13 Col C
					Cell R13cell2 = row.createCell(2);
					if (record.getR13_total_value() != null) {
					 R13cell2.setCellValue(record.getR13_total_value().doubleValue());
					 R13cell2.setCellStyle(numberStyle);
					} else {
					 R13cell2.setCellValue("");
					 R13cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					//R14 Col B            
					Cell R14cell1 = row.createCell(1);
					if (record.getR14_total_no_of_acct() != null) {
					R14cell1.setCellValue(record.getR14_total_no_of_acct().doubleValue());
					R14cell1.setCellStyle(numberStyle);
					} else {
					R14cell1.setCellValue("");
					R14cell1.setCellStyle(textStyle);
					}

					//R14 Col C
					Cell R14cell2 = row.createCell(2);
					if (record.getR14_total_value() != null) {
					R14cell2.setCellValue(record.getR14_total_value().doubleValue());
					R14cell2.setCellStyle(numberStyle);
					} else {
					R14cell2.setCellValue("");
					R14cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					//R23 Col B            
					Cell R23cell1 = row.createCell(1);
					if (record1.getR23_total_no_of_acct() != null) {
					R23cell1.setCellValue(record1.getR23_total_no_of_acct().doubleValue());
					R23cell1.setCellStyle(numberStyle);
					} else {
					R23cell1.setCellValue("");
					R23cell1.setCellStyle(textStyle);
					}

					//R23 Col C
					Cell R23cell2 = row.createCell(2);
					if (record1.getR23_total_value() != null) {
					R23cell2.setCellValue(record1.getR23_total_value().doubleValue());
					R23cell2.setCellStyle(numberStyle);
					} else {
					R23cell2.setCellValue("");
					R23cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					//R25 Col B            
					Cell R25cell1 = row.createCell(1);
					if (record1.getR25_total_no_of_acct() != null) {
					R25cell1.setCellValue(record1.getR25_total_no_of_acct().doubleValue());
					R25cell1.setCellStyle(numberStyle);
					} else {
					R25cell1.setCellValue("");
					R25cell1.setCellStyle(textStyle);
					}

					//R25 Col C
					Cell R25cell2 = row.createCell(2);
					if (record1.getR25_total_value() != null) {
					R25cell2.setCellValue(record1.getR25_total_value().doubleValue());
					R25cell2.setCellStyle(numberStyle);
					} else {
					R25cell2.setCellValue("");
					R25cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					//R26 Col B            
					Cell R26cell1 = row.createCell(1);
					if (record1.getR26_total_no_of_acct() != null) {
					R26cell1.setCellValue(record1.getR26_total_no_of_acct().doubleValue());
					R26cell1.setCellStyle(numberStyle);
					} else {
					R26cell1.setCellValue("");
					R26cell1.setCellStyle(textStyle);
					}

					//R26 Col C
					Cell R26cell2 = row.createCell(2);
					if (record1.getR26_total_value() != null) {
					R26cell2.setCellValue(record1.getR26_total_value().doubleValue());
					R26cell2.setCellStyle(numberStyle);
					} else {
					R26cell2.setCellValue("");
					R26cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(29);
					//R30 Col B            
					Cell R30cell1 = row.createCell(1);
					if (record1.getR30_total_no_of_acct() != null) {
					R30cell1.setCellValue(record1.getR30_total_no_of_acct().doubleValue());
					R30cell1.setCellStyle(numberStyle);
					} else {
					R30cell1.setCellValue("");
					R30cell1.setCellStyle(textStyle);
					}

					//R30 Col C
					Cell R30cell2 = row.createCell(2);
					if (record1.getR30_total_value() != null) {
					R30cell2.setCellValue(record1.getR30_total_value().doubleValue());
					R30cell2.setCellStyle(numberStyle);
					} else {
					R30cell2.setCellValue("");
					R30cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					//R34 Col B            
					Cell R34cell1 = row.createCell(1);
					if (record.getR34_total_no_of_acct() != null) {
					R34cell1.setCellValue(record.getR34_total_no_of_acct().doubleValue());
					R34cell1.setCellStyle(numberStyle);
					} else {
					R34cell1.setCellValue("");
					R34cell1.setCellStyle(textStyle);
					}

					//R34 Col C
					Cell R34cell2 = row.createCell(2);
					if (record.getR34_total_value() != null) {
					R34cell2.setCellValue(record.getR34_total_no_of_acct().doubleValue());
					R34cell2.setCellStyle(numberStyle);
					} else {
					R34cell2.setCellValue("");
					R34cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
					//R35 Col B            
					Cell R35cell1 = row.createCell(1);
					if (record1.getR35_total_no_of_acct() != null) {
					R35cell1.setCellValue(record1.getR35_total_no_of_acct().doubleValue());
					R35cell1.setCellStyle(numberStyle);
					} else {
					R35cell1.setCellValue("");
					R35cell1.setCellStyle(textStyle);
					}

					//R35 Col C
					Cell R35cell2 = row.createCell(2);
					if (record1.getR35_total_value() != null) {
					R35cell2.setCellValue(record1.getR35_total_value().doubleValue());
					R35cell2.setCellStyle(numberStyle);
					} else {
					R35cell2.setCellValue("");
					R35cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(37);
					//R38 Col B            
					Cell R38cell1 = row.createCell(1);
					if (record.getR38_total_no_of_acct() != null) {
					R38cell1.setCellValue(record.getR38_total_no_of_acct().doubleValue());
					R38cell1.setCellStyle(numberStyle);
					} else {
					R38cell1.setCellValue("");
					R38cell1.setCellStyle(textStyle);
					}

					//R38 Col C
					Cell R38cell2 = row.createCell(2);
					if (record.getR38_total_value() != null) {
					R38cell2.setCellValue(record.getR38_total_value().doubleValue());
					R38cell2.setCellStyle(numberStyle);
					} else {
					R38cell2.setCellValue("");
					R38cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(38);
					//R39 Col B            
					Cell R39cell1 = row.createCell(1);
					if (record.getR39_total_no_of_acct() != null) {
					R39cell1.setCellValue(record.getR39_total_no_of_acct().doubleValue());
					R39cell1.setCellStyle(numberStyle);
					} else {
					R39cell1.setCellValue("");
					R39cell1.setCellStyle(textStyle);
					}

					//R39 Col C
					Cell R39cell2 = row.createCell(2);
					if (record.getR39_total_value() != null) {
					R39cell2.setCellValue(record.getR39_total_value().doubleValue());
					R39cell2.setCellStyle(numberStyle);
					} else {
					R39cell2.setCellValue("");
					R39cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(41);
					//R42 Col B            
					Cell R42cell1 = row.createCell(1);
					if (record1.getR42_total_no_of_acct() != null) {
					R42cell1.setCellValue(record1.getR42_total_no_of_acct().doubleValue());
					R42cell1.setCellStyle(numberStyle);
					} else {
					R42cell1.setCellValue("");
					R42cell1.setCellStyle(textStyle);
					}

					//R42 Col C
					Cell R42cell2 = row.createCell(2);
					if (record1.getR42_total_value() != null) {
					R42cell2.setCellValue(record1.getR42_total_value().doubleValue());
					R42cell2.setCellStyle(numberStyle);
					} else {
					R42cell2.setCellValue("");
					R42cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					//R43 Col B            
					Cell R43cell1 = row.createCell(1);
					if (record1.getR43_total_no_of_acct() != null) {
					R43cell1.setCellValue(record1.getR43_total_no_of_acct().doubleValue());
					R43cell1.setCellStyle(numberStyle);
					} else {
					R43cell1.setCellValue("");
					R43cell1.setCellStyle(textStyle);
					}

					//R43 Col C
					Cell R43cell2 = row.createCell(2);
					if (record1.getR43_total_value() != null) {
					R43cell2.setCellValue(record1.getR43_total_value().doubleValue());
					R43cell2.setCellStyle(numberStyle);
					} else {
					R43cell2.setCellValue("");
					R43cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(43);
					//R44 Col B            
					Cell R44cell1 = row.createCell(1);
					if (record1.getR44_total_no_of_acct() != null) {
					R44cell1.setCellValue(record1.getR44_total_no_of_acct().doubleValue());
					R44cell1.setCellStyle(numberStyle);
					} else {
					R44cell1.setCellValue("");
					R44cell1.setCellStyle(textStyle);
					}

					//R44 Col C
					Cell R44cell2 = row.createCell(2);
					if (record1.getR44_total_value() != null) {
					R44cell2.setCellValue(record1.getR44_total_value().doubleValue());
					R44cell2.setCellStyle(numberStyle);
					} else {
					R44cell2.setCellValue("");
					R44cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					//R46 Col B            
					Cell R46cell1 = row.createCell(1);
					if (record1.getR46_total_no_of_acct() != null) {
					R46cell1.setCellValue(record1.getR46_total_no_of_acct().doubleValue());
					R46cell1.setCellStyle(numberStyle);
					} else {
					R46cell1.setCellValue("");
					R46cell1.setCellStyle(textStyle);
					}

					//R46 Col C
					Cell R46cell2 = row.createCell(2);
					if (record1.getR46_total_value() != null) {
					R46cell2.setCellValue(record1.getR46_total_value().doubleValue());
					R46cell2.setCellStyle(numberStyle);
					} else {
					R46cell2.setCellValue("");
					R46cell2.setCellStyle(textStyle);
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
	    ModelAndView mv = new ModelAndView("BRRS/ADISB1"); // ✅ match the report name
	    System.out.println("Hello");
	    if (acctNo != null) {
	    	ADISB1_Detail_Entity adisb1Entity = BRRS_ADISB1_Detail_Repo.findByAcctnumber(acctNo);
	        if (adisb1Entity != null && adisb1Entity.getReportDate() != null) {
	            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(adisb1Entity.getReportDate());
	            mv.addObject("asondate", formattedDate);
	        }
	        mv.addObject("Data", adisb1Entity);
	    }

	    mv.addObject("displaymode", "edit");
	    mv.addObject("formmode", formMode != null ? formMode : "edit");
	    return mv;
	}





	public ModelAndView updateDetailEdit(String acctNo, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/ADISB1"); // ✅ match the report name

	    if (acctNo != null) {
	        ADISB1_Detail_Entity la1Entity = BRRS_ADISB1_Detail_Repo.findByAcctnumber(acctNo);
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

	        ADISB1_Detail_Entity existing = BRRS_ADISB1_Detail_Repo.findByAcctnumber(acctNo);
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
	        	BRRS_ADISB1_Detail_Repo.save(existing);
	            logger.info("Record updated successfully for account {}", acctNo);

	            // Format date for procedure
	            String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
	                    .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

	            // Run summary procedure after commit
	            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
	                @Override
	                public void afterCommit() {
	                    try {
	                        logger.info("Transaction committed — calling BRRS_ADISB1_SUMMARY_PROCEDURE({})",
	                                formattedDate);
	                        jdbcTemplate.update("BEGIN BRRS_ADISB1_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
	        logger.error("Error updating ADISB1 record", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error updating record: " + e.getMessage());
	    }
	}
	
	
	
	
}