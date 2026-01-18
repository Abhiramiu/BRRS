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

import com.bornfire.brrs.entities.BRRS_M_DEP3_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_DEP3_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_DEP3_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_DEP3_Summary_Repo;
import com.bornfire.brrs.entities.M_DEP3_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_DEP3_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_DEP3_Detail_Entity;
import com.bornfire.brrs.entities.M_DEP3_Summary_Entity;
import com.bornfire.brrs.entities.M_DEP3_Manual_Summary_Entity;
import com.bornfire.brrs.entities.M_DEP3_Manual_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_DEP3_Manual_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_DEP3_Manual_Archival_Summary_Repo;

@Component
@Service
public class BRRS_M_DEP3_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_DEP3_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_DEP3_Detail_Repo BRRS_M_DEP3_Detail_Repo;

	@Autowired
	BRRS_M_DEP3_Summary_Repo BRRS_M_DEP3_Summary_Repo;

	@Autowired
	BRRS_M_DEP3_Manual_Summary_Repo BRRS_M_DEP3_Manual_Summary_Repo;


	@Autowired
	BRRS_M_DEP3_Archival_Detail_Repo BRRS_M_DEP3_Archival_Detail_Repo;

	@Autowired
	BRRS_M_DEP3_Archival_Summary_Repo BRRS_M_DEP3_Archival_Summary_Repo;

	@Autowired
	BRRS_M_DEP3_Manual_Archival_Summary_Repo BRRS_M_DEP3_Manual_Archival_Summary_Repo;



	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_DEP3View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type,  String version) {
		ModelAndView mv = new ModelAndView();
//		Session hs = sessionFactory.getCurrentSession();
//		int pageSize = pageable.getPageSize();
//		int currentPage = pageable.getPageNumber();
//		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<M_DEP3_Archival_Summary_Entity> T1Master = new ArrayList<M_DEP3_Archival_Summary_Entity>();
			List<M_DEP3_Manual_Archival_Summary_Entity> T2Master = new ArrayList<M_DEP3_Manual_Archival_Summary_Entity>();

			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_DEP3_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
				T2Master = BRRS_M_DEP3_Manual_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
				

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);
            
		} else {
			List<M_DEP3_Summary_Entity> T1Master = new ArrayList<M_DEP3_Summary_Entity>();
			List<M_DEP3_Manual_Summary_Entity> T2Master = new ArrayList<M_DEP3_Manual_Summary_Entity>();
			
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_DEP3_Summary_Repo.getdatabydateList(dateformat.parse(todate));
				T2Master = BRRS_M_DEP3_Manual_Summary_Repo.getdatabydateList(dateformat.parse(todate));
				

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);
            
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/M_DEP3");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

//	public ModelAndView getM_DEP3currentDtl(String reportId, String fromdate, String todate, String currency,
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
//			List<M_DEP3_Archival_Detail_Entity> T1Dt1;
//			if (rowId != null && columnId != null) {
//				T1Dt1 = BRRS_M_DEP3_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
//			} else {
//				T1Dt1 = BRRS_M_DEP3_Archival_Detail_Repo.getdatabydateList(parsedDate, version);					
//			}
//
//			mv.addObject("reportdetails", T1Dt1);
//			mv.addObject("reportmaster12", T1Dt1);
//			System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
//
//		} else {
//			// 🔹 Current branch
//			List<M_DEP3_Detail_Entity> T1Dt1;
//			if (rowId != null && columnId != null) {
//				T1Dt1 = BRRS_M_DEP3_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
//			} else {
//				T1Dt1 = BRRS_M_DEP3_Detail_Repo.getdatabydateList(parsedDate);
//				totalPages = BRRS_M_DEP3_Detail_Repo.getdatacount(parsedDate);
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
//	mv.setViewName("BRRS/M_DEP3");
//	mv.addObject("displaymode", "Details");
//	mv.addObject("currentPage", currentPage);
//	System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
//	mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
//	mv.addObject("reportsflag", "reportsflag");
//	mv.addObject("menu", reportId);
//
//	return mv;
//}

	public ModelAndView getM_DEP3currentDtl(String reportId, String fromdate, String todate, String currency,
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
			List<M_DEP3_Archival_Detail_Entity> T1Dt1;
			if (rowId != null && columnId != null) {
				T1Dt1 = BRRS_M_DEP3_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
			} else {
				T1Dt1 = BRRS_M_DEP3_Archival_Detail_Repo.getdatabydateList(parsedDate, version);					
			}

			mv.addObject("reportdetails", T1Dt1);
			mv.addObject("reportmaster12", T1Dt1);
			System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

		} else {
			// 🔹 Current branch
			List<M_DEP3_Detail_Entity> T1Dt1;
			if (rowId != null && columnId != null) {
				T1Dt1 = BRRS_M_DEP3_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
			} else {
				T1Dt1 = BRRS_M_DEP3_Detail_Repo.getdatabydateList(parsedDate);
				totalPages = BRRS_M_DEP3_Detail_Repo.getdatacount(parsedDate);
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
	mv.setViewName("BRRS/M_DEP3");
	mv.addObject("displaymode", "Details");
	mv.addObject("currentPage", currentPage);
	System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
	mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
	mv.addObject("reportsflag", "reportsflag");
	mv.addObject("menu", reportId);

	return mv;
}

	
	public void updateReport(M_DEP3_Manual_Summary_Entity updatedEntity) {
	    System.out.println("Came to services1");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    M_DEP3_Manual_Summary_Entity existing = BRRS_M_DEP3_Manual_Summary_Repo.findById(updatedEntity.getReport_date())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

	    try {
	        // ✅ Loop for table 2 fields
	        int[] Rows = {28,29,30,31,32,33,34};
	        for (int i : Rows) {
	            String prefix = "R" + i + "_";
	            String[] fields = {"import", "investment", "other", "residents","non_residents" };

	            for (String field : fields) {
	                try {
	                    String getterName = "get" + prefix + field;
	                    String setterName = "set" + prefix + field;

	                    Method getter = M_DEP3_Manual_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_DEP3_Manual_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing getter/setter gracefully
	                    continue;
	                }
	            }
	        }

	        // ✅ Loop for amount_1 fields
	        int[] Rows1 = {11,12,13,14,15,16};
	        for (int i : Rows1) {
	            String prefix = "R" + i + "_";
	            String[] fields = {"ex_rate_buy", "ex_rate_mid", "ex_rate_sell"};

	            for (String field : fields) {
	                try {
	                    String getterName = "get" + prefix + field;
	                    String setterName = "set" + prefix + field;

	                    Method getter = M_DEP3_Manual_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_DEP3_Manual_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    continue;
	                }
	            }
	        }
	        
	        // ✅ Loop for amount_1 fields
	        int[] Rows2 = {11,12,13,14,15,16,18};
	        for (int i : Rows2) {
	            String prefix = "R" + i + "_";
	            String[] fields = {"notice_0to31", "notice_32to88", "cer_of_depo"};

	            for (String field : fields) {
	                try {
	                    String getterName = "get" + prefix + field;
	                    String setterName = "set" + prefix + field;

	                    Method getter = M_DEP3_Manual_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_DEP3_Manual_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    continue;
	                }
	            }
	        }

	        // ✅ Save after all updates
	        BRRS_M_DEP3_Manual_Summary_Repo.save(existing);

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }
	}

	
	
	
	public byte[] getM_DEP3DetailExcel(String filename, String fromdate, String todate, String currency,
			   String dtltype, String type, String version) {
try {
logger.info("Generating Excel for M_DEP3 Details...");
System.out.println("came to Detail download service");


if (type.equals("ARCHIVAL") & version != null) {
byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
version);
return ARCHIVALreport;
}

XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("M_DEP3Detail");

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
balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
balanceStyle.setBorderTop(border);
balanceStyle.setBorderBottom(border);
balanceStyle.setBorderLeft(border);
balanceStyle.setBorderRight(border);






//Header row
String[] headers = {
"CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABEL", "REPORT ADDL CRITERIA1",
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
List<M_DEP3_Detail_Entity> reportData = BRRS_M_DEP3_Detail_Repo.getdatabydateList(parsedToDate);

if (reportData != null && !reportData.isEmpty()) {
int rowIndex = 1;
for (M_DEP3_Detail_Entity item : reportData) {
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

		row.createCell(4).setCellValue(item.getReportLabel());
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
	logger.info("No data found for M_DEP3 — only header will be written.");
}

//Write to byte[]
ByteArrayOutputStream bos = new ByteArrayOutputStream();
workbook.write(bos);
workbook.close();

logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
return bos.toByteArray();

} catch (Exception e) {
logger.error("Error generating M_DEP3 Excel", e);
return new byte[0];
}
}
//	public byte[] BRRS_M_DEP3DetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
//			String type, String version) {
//
//		try {
//			logger.info("Generating Excel for BRRS_M_DEP3Details...");
//			System.out.println("came to Detail download service");
//			if (type.equals("ARCHIVAL") & version != null) {
//				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
//						version);
//				return ARCHIVALreport;
//			}
//			XSSFWorkbook workbook = new XSSFWorkbook();
//			XSSFSheet sheet = workbook.createSheet("BRRS_M_DEP3Details");
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
//			List<M_DEP3_Detail_Entity> reportData = BRRS_M_DEP3_Detail_Repo.getdatabydateList(parsedToDate);
//			if (reportData != null && !reportData.isEmpty()) {
//				int rowIndex = 1;
//				for (M_DEP3_Detail_Entity item : reportData) {
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
//				logger.info("No data found for BRRS_M_DEP3— only header will be written.");
//			}
//			// Write to byte[]
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			workbook.write(bos);
//			workbook.close();
//			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
//			return bos.toByteArray();
//		} catch (Exception e) {
//			logger.error("Error generating BRRS_M_DEP3Excel", e);
//			return new byte[0];
//		}
//	}

	public byte[] BRRS_M_DEP3Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_DEP3ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data
		List<M_DEP3_Summary_Entity> dataList = BRRS_M_DEP3_Summary_Repo.getdatabydateList(dateformat.parse(todate));
		List<M_DEP3_Manual_Summary_Entity> dataList1 = BRRS_M_DEP3_Manual_Summary_Repo.getdatabydateList(dateformat.parse(todate));


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
			int startRow = 10;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_DEP3_Summary_Entity record = dataList.get(i);
					M_DEP3_Manual_Summary_Entity record1 = dataList1.get(i);
					
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// R11 Col B
            
Cell R11cell1 = row.createCell(1);
if (record1.getR11_ex_rate_buy() != null) {
    R11cell1.setCellValue(record1.getR11_ex_rate_buy().doubleValue());
    R11cell1.setCellStyle(numberStyle);
} else {
    R11cell1.setCellValue("");
    R11cell1.setCellStyle(textStyle);
}

// R11 Col C
Cell R11cell2 = row.createCell(2);
if (record1.getR11_ex_rate_mid() != null) {
    R11cell2.setCellValue(record1.getR11_ex_rate_mid().doubleValue());
    R11cell2.setCellStyle(numberStyle);
} else {
    R11cell2.setCellValue("");
    R11cell2.setCellStyle(textStyle);
}

// R11 Col D
Cell R11cell3 = row.createCell(3);
if (record1.getR11_ex_rate_sell() != null) {
    R11cell3.setCellValue(record1.getR11_ex_rate_sell().doubleValue());
    R11cell3.setCellStyle(numberStyle);
} else {
    R11cell3.setCellValue("");
    R11cell3.setCellStyle(textStyle);
}

// R11 Col E
Cell R11cell4 = row.createCell(4);
if (record.getR11_current()!= null) {
    R11cell4.setCellValue(record.getR11_current().doubleValue());
    R11cell4.setCellStyle(numberStyle);
} else {
    R11cell4.setCellValue("");
    R11cell1.setCellStyle(textStyle);
}

// R11 Col F
Cell R11cell5 = row.createCell(5);
if (record.getR11_call() != null) {
    R11cell5.setCellValue(record.getR11_call().doubleValue());
    R11cell5.setCellStyle(numberStyle);
} else {
    R11cell5.setCellValue("");
    R11cell5.setCellStyle(textStyle);
}

// R11 Col G
Cell R11cell6 = row.createCell(6);
if (record.getR11_savings() != null) {
    R11cell6.setCellValue(record.getR11_savings().doubleValue());
    R11cell6.setCellStyle(numberStyle);
} else {
    R11cell6.setCellValue("");
    R11cell6.setCellStyle(textStyle);
	
}

// R11 Col H
Cell R11cell7 = row.createCell(7);
if (record1.getR11_notice_0to31()!= null) {
    R11cell7.setCellValue(record1.getR11_notice_0to31().doubleValue());
    R11cell7.setCellStyle(numberStyle);
} else {
    R11cell7.setCellValue("");
    R11cell7.setCellStyle(textStyle);
	
}

// R11 Col I
Cell R11cell8= row.createCell(8);
if (record1.getR11_notice_32to88() != null) {
    R11cell8.setCellValue(record1.getR11_notice_32to88().doubleValue());
    R11cell8.setCellStyle(numberStyle);
} else {
    R11cell8.setCellValue("");
    R11cell8.setCellStyle(textStyle);
}

// R11 Col J
Cell R11cell9 = row.createCell(9);
if (record.getR11_fix_depo_91_day_depo() != null) {
    R11cell9.setCellValue(record.getR11_fix_depo_91_day_depo().doubleValue());
    R11cell9.setCellStyle(numberStyle);
} else {
    R11cell9.setCellValue("");
    R11cell9.setCellStyle(textStyle);
}

// R11 Col K
Cell R11cell10 = row.createCell(10);
if (record.getR11_fix_depo_1to2() != null) {
    R11cell10.setCellValue(record.getR11_fix_depo_1to2().doubleValue());
    R11cell10.setCellStyle(numberStyle);
} else {
    R11cell10.setCellValue("");
    R11cell10.setCellStyle(textStyle);
}

// R11 Col L
Cell R11cell11 = row.createCell(11);
if (record.getR11_fix_depo_4to6() != null) {
    R11cell11.setCellValue(record.getR11_fix_depo_4to6().doubleValue());
    R11cell11.setCellStyle(numberStyle);
} else {
    R11cell11.setCellValue("");
    R11cell11.setCellStyle(textStyle);
}

// R11 Col M
Cell R11cell12 = row.createCell(12);
if (record.getR11_fix_depo_7to12() != null) {
    R11cell12.setCellValue(record.getR11_fix_depo_7to12().doubleValue());
    R11cell12.setCellStyle(numberStyle);
} else {
    R11cell12.setCellValue("");
    R11cell12.setCellStyle(textStyle);
}

// R11 Col N
Cell R11cell13 = row.createCell(13);
if (record.getR11_fix_depo_13to18()!= null) {
    R11cell13.setCellValue(record.getR11_fix_depo_13to18().doubleValue());
    R11cell13.setCellStyle(numberStyle);
} else {
    R11cell13.setCellValue("");
    R11cell13.setCellStyle(textStyle);
}

// R11 Col O
Cell R11cell14 = row.createCell(14);
if (record.getR11_fix_depo_19to24()!= null) {
    R11cell14.setCellValue(record.getR11_fix_depo_19to24().doubleValue());
    R11cell14.setCellStyle(numberStyle);
} else {
    R11cell14.setCellValue("");
    R11cell14.setCellStyle(textStyle);
}

// R11 Col P
Cell R11cell15 = row.createCell(15);
if (record.getR11_fix_depo_over24()!= null) {
    R11cell15.setCellValue(record.getR11_fix_depo_over24().doubleValue());
    R11cell15.setCellStyle(numberStyle);
} else {
    R11cell15.setCellValue("");
    R11cell15.setCellStyle(textStyle);
}


// R11 Col Q
Cell R11cell16 = row.createCell(16);
if (record1.getR11_cer_of_depo()!= null) {
    R11cell16.setCellValue(record1.getR11_cer_of_depo().doubleValue());
    R11cell16.setCellStyle(numberStyle);
} else {
    R11cell16.setCellValue("");
    R11cell16.setCellStyle(textStyle);
 
}

// R11 Col S
Cell R11cell18 = row.createCell(18);
if (record.getR11_pula_equivalent () != null) {
    R11cell18.setCellValue(record.getR11_pula_equivalent().doubleValue());
    R11cell18.setCellStyle(numberStyle);
} else {
    R11cell18.setCellValue("");
    R11cell18.setCellStyle(textStyle);
}
 

// R11 Col T
Cell R11cell19 = row.createCell(19);
if (record.getR11_avg_pula_equivalent ()!= null) {
    R11cell19.setCellValue(record.getR11_avg_pula_equivalent().doubleValue());
    R11cell19.setCellStyle(numberStyle);
} else {
    R11cell19.setCellValue("");
    R11cell19.setCellStyle(textStyle);
}
row = sheet.getRow(11);
// R12 Col B
Cell R12cell1 = row.createCell(1);
if (record1.getR12_ex_rate_buy ()!= null) {
    R12cell1.setCellValue(record1.getR12_ex_rate_buy().doubleValue());
    R12cell1.setCellStyle(numberStyle);
} else {
    R12cell1.setCellValue("");
    R12cell1.setCellStyle(textStyle);
}

// R12 Col C
Cell R12cell2 = row.createCell(2);
if (record1.getR12_ex_rate_mid() != null) {
    R12cell2.setCellValue(record1.getR12_ex_rate_mid().doubleValue());
    R12cell2.setCellStyle(numberStyle);
} else {
    R12cell2.setCellValue("");
    R12cell2.setCellStyle(textStyle);
}

// R12 Col D
Cell R12cell3 = row.createCell(3);
if (record1.getR12_ex_rate_sell() != null) {
    R12cell3.setCellValue(record1.getR12_ex_rate_sell().doubleValue());
    R12cell3.setCellStyle(numberStyle);
} else {
    R12cell3.setCellValue("");
    R12cell3.setCellStyle(textStyle);
}

// R12 Col E
Cell R12cell4 = row.createCell(4);
if (record.getR12_current() != null) {
    R12cell4.setCellValue(record.getR12_current().doubleValue());
    R12cell4.setCellStyle(numberStyle);
} else {
    R12cell4.setCellValue("");
    R12cell1.setCellStyle(textStyle);
}

// R12 Col F
Cell R12cell5 = row.createCell(5);
if (record.getR12_call() != null) {
    R12cell5.setCellValue(record.getR12_call().doubleValue());
    R12cell5.setCellStyle(numberStyle);
} else {
    R12cell5.setCellValue("");
    R12cell5.setCellStyle(textStyle);
}

// R12 Col G
Cell R12cell6 = row.createCell(6);
if (record.getR12_savings() != null) {
    R12cell6.setCellValue(record.getR12_savings().doubleValue());
    R12cell6.setCellStyle(numberStyle);
} else {
    R12cell6.setCellValue("");
    R12cell6.setCellStyle(textStyle);
	
}

// R12 Col H
Cell R12cell7 = row.createCell(7);
if (record1.getR12_notice_0to31()!= null) {
    R12cell7.setCellValue(record1.getR12_notice_0to31().doubleValue());
    R12cell7.setCellStyle(numberStyle);
} else {
    R12cell7.setCellValue("");
    R12cell7.setCellStyle(textStyle);
	
}

// R12 Col I
Cell R12cell8= row.createCell(8);
if (record1.getR12_notice_32to88() != null) {
    R12cell8.setCellValue(record1.getR12_notice_32to88().doubleValue());
    R12cell8.setCellStyle(numberStyle);
} else {
    R12cell8.setCellValue("");
    R12cell8.setCellStyle(textStyle);
}

// R12 Col J
Cell R12cell9 = row.createCell(9);
if (record.getR12_fix_depo_91_day_depo() != null) {
    R12cell9.setCellValue(record.getR12_fix_depo_91_day_depo().doubleValue());
    R12cell9.setCellStyle(numberStyle);
} else {
    R12cell9.setCellValue("");
    R12cell9.setCellStyle(textStyle);
}

// R12 Col K
Cell R12cell10 = row.createCell(10);
if (record.getR12_fix_depo_1to2() != null) {
    R12cell10.setCellValue(record.getR12_fix_depo_1to2().doubleValue());
    R12cell10.setCellStyle(numberStyle);
} else {
    R12cell10.setCellValue("");
    R12cell10.setCellStyle(textStyle);
}

// R12 Col L
Cell R12cell11 = row.createCell(11);
if (record.getR12_fix_depo_4to6() != null) {
    R12cell11.setCellValue(record.getR12_fix_depo_4to6().doubleValue());
    R12cell11.setCellStyle(numberStyle);
} else {
    R12cell11.setCellValue("");
    R12cell11.setCellStyle(textStyle);
}

// R12 Col M
Cell R12cell12 = row.createCell(12);
if (record.getR12_fix_depo_7to12() != null) {
    R12cell12.setCellValue(record.getR12_fix_depo_7to12().doubleValue());
    R12cell12.setCellStyle(numberStyle);
} else {
    R12cell12.setCellValue("");
    R12cell12.setCellStyle(textStyle);
}

// R12 Col N
Cell R12cell13 = row.createCell(13);
if (record.getR12_fix_depo_13to18()!= null) {
    R12cell13.setCellValue(record.getR12_fix_depo_13to18().doubleValue());
    R12cell13.setCellStyle(numberStyle);
} else {
    R12cell13.setCellValue("");
    R12cell13.setCellStyle(textStyle);
}

// R12 Col O
Cell R12cell14 = row.createCell(14);
if (record.getR12_fix_depo_19to24()!= null) {
    R12cell14.setCellValue(record.getR12_fix_depo_19to24().doubleValue());
    R12cell14.setCellStyle(numberStyle);
} else {
    R12cell14.setCellValue("");
    R12cell14.setCellStyle(textStyle);
}

// R12 Col P
Cell R12cell15 = row.createCell(15);
if (record.getR12_fix_depo_over24() != null) {
    R12cell15.setCellValue(record.getR12_fix_depo_over24().doubleValue());
    R12cell15.setCellStyle(numberStyle);
} else {
    R12cell15.setCellValue("");
    R12cell15.setCellStyle(textStyle);
}


// R12 Col Q
Cell R12cell16 = row.createCell(16);
if (record1.getR12_cer_of_depo() != null) {
    R12cell16.setCellValue(record1.getR12_cer_of_depo().doubleValue());
    R12cell16.setCellStyle(numberStyle);
} else {
    R12cell16.setCellValue("");
    R12cell16.setCellStyle(textStyle);
}

// R12 Col S
Cell R12cell18 = row.createCell(18);
if (record.getR12_pula_equivalent() != null) {
    R12cell18.setCellValue(record.getR12_pula_equivalent().doubleValue());
    R12cell18.setCellStyle(numberStyle);
} else {
    R12cell18.setCellValue("");
    R12cell18.setCellStyle(textStyle);
}
 

// R12 Col T
Cell R12cell19 = row.createCell(19);
if (record.getR12_avg_pula_equivalent() != null) {
    R12cell19.setCellValue(record.getR12_avg_pula_equivalent().doubleValue());
    R12cell19.setCellStyle(numberStyle);
} else {
    R12cell19.setCellValue("");
    R12cell19.setCellStyle(textStyle);
}

// R13 Col B
row = sheet.getRow(12);
Cell R13cell1 = row.createCell(1);
if (record1.getR13_ex_rate_buy() != null) {
    R13cell1.setCellValue(record1.getR13_ex_rate_buy().doubleValue());
    R13cell1.setCellStyle(numberStyle);
} else {
    R13cell1.setCellValue("");
    R13cell1.setCellStyle(textStyle);
}

// R13 Col C
Cell R13cell2 = row.createCell(2);
if (record1.getR13_ex_rate_mid() != null) {
    R13cell2.setCellValue(record1.getR13_ex_rate_mid().doubleValue());
    R13cell2.setCellStyle(numberStyle);
} else {
    R13cell2.setCellValue("");
    R13cell2.setCellStyle(textStyle);
}

// R13 Col D
Cell R13cell3 = row.createCell(3);
if (record1.getR13_ex_rate_sell() != null) {
    R13cell3.setCellValue(record1.getR13_ex_rate_sell().doubleValue());
    R13cell3.setCellStyle(numberStyle);
} else {
    R13cell3.setCellValue("");
    R13cell3.setCellStyle(textStyle);
}

// R13 Col E
Cell R13cell4 = row.createCell(4);
if (record.getR13_current() != null) {
    R13cell4.setCellValue(record.getR13_current().doubleValue());
    R13cell4.setCellStyle(numberStyle);
} else {
    R13cell4.setCellValue("");
    R13cell1.setCellStyle(textStyle);
}

// R13 Col F
Cell R13cell5 = row.createCell(5);
if (record.getR13_call() != null) {
    R13cell5.setCellValue(record.getR13_call().doubleValue());
    R13cell5.setCellStyle(numberStyle);
} else {
    R13cell5.setCellValue("");
    R13cell5.setCellStyle(textStyle);
}

// R13 Col G
Cell R13cell6 = row.createCell(6);
if (record.getR13_savings() != null) {
    R13cell6.setCellValue(record.getR13_savings().doubleValue());
    R13cell6.setCellStyle(numberStyle);
} else {
    R13cell6.setCellValue("");
    R13cell6.setCellStyle(textStyle);
	
}

// R13 Col H
Cell R13cell7 = row.createCell(7);
if (record1.getR13_notice_0to31()!= null) {
    R13cell7.setCellValue(record1.getR13_notice_0to31().doubleValue());
    R13cell7.setCellStyle(numberStyle);
} else {
    R13cell7.setCellValue("");
    R13cell7.setCellStyle(textStyle);
	
}

// R13 Col I
Cell R13cell8= row.createCell(8);
if (record1.getR13_notice_32to88() != null) {
    R13cell8.setCellValue(record1.getR13_notice_32to88().doubleValue());
    R13cell8.setCellStyle(numberStyle);
} else {
    R13cell8.setCellValue("");
    R13cell8.setCellStyle(textStyle);
}

// R13 Col J
Cell R13cell9 = row.createCell(9);
if (record.getR13_fix_depo_91_day_depo() != null) {
    R13cell9.setCellValue(record.getR13_fix_depo_91_day_depo().doubleValue());
    R13cell9.setCellStyle(numberStyle);
} else {
    R13cell9.setCellValue("");
    R13cell9.setCellStyle(textStyle);
}

// R13 Col K
Cell R13cell10 = row.createCell(10);
if (record.getR13_fix_depo_1to2() != null) {
    R13cell10.setCellValue(record.getR13_fix_depo_1to2().doubleValue());
    R13cell10.setCellStyle(numberStyle);
} else {
    R13cell10.setCellValue("");
    R13cell10.setCellStyle(textStyle);
}

// R13 Col L
Cell R13cell11 = row.createCell(11);
if (record.getR13_fix_depo_4to6() != null) {
    R13cell11.setCellValue(record.getR13_fix_depo_4to6().doubleValue());
    R13cell11.setCellStyle(numberStyle);
} else {
    R13cell11.setCellValue("");
    R13cell11.setCellStyle(textStyle);
}

// R13 Col M
Cell R13cell12 = row.createCell(12);
if (record.getR13_fix_depo_7to12() != null) {
    R13cell12.setCellValue(record.getR13_fix_depo_7to12().doubleValue());
    R13cell12.setCellStyle(numberStyle);
} else {
    R13cell12.setCellValue("");
    R13cell12.setCellStyle(textStyle);
}

// R13 Col N
Cell R13cell13 = row.createCell(13);
if (record.getR13_fix_depo_13to18()!= null) {
    R13cell13.setCellValue(record.getR13_fix_depo_13to18().doubleValue());
    R13cell13.setCellStyle(numberStyle);
} else {
    R13cell13.setCellValue("");
    R13cell13.setCellStyle(textStyle);
}

// R13 Col O
Cell R13cell14 = row.createCell(14);
if (record.getR13_fix_depo_19to24()!= null) {
    R13cell14.setCellValue(record.getR13_fix_depo_19to24().doubleValue());
    R13cell14.setCellStyle(numberStyle);
} else {
    R13cell14.setCellValue("");
    R13cell14.setCellStyle(textStyle);
}

// R13 Col P
Cell R13cell15 = row.createCell(15);
if (record.getR13_fix_depo_over24() != null) {
    R13cell15.setCellValue(record.getR13_fix_depo_over24().doubleValue());
    R13cell15.setCellStyle(numberStyle);
} else {
    R13cell15.setCellValue("");
    R13cell15.setCellStyle(textStyle);
}


// R13 Col Q
Cell R13cell16 = row.createCell(16);
if (record1.getR13_cer_of_depo() != null) {
    R13cell16.setCellValue(record1.getR13_cer_of_depo().doubleValue());
    R13cell16.setCellStyle(numberStyle);
} else {
    R13cell16.setCellValue("");
    R13cell16.setCellStyle(textStyle);
}

// R13 Col S
Cell R13cell18 = row.createCell(18);
if (record.getR13_pula_equivalent() != null) {
    R13cell18.setCellValue(record.getR13_pula_equivalent().doubleValue());
    R13cell18.setCellStyle(numberStyle);
} else {
    R13cell18.setCellValue("");
    R13cell18.setCellStyle(textStyle);
}
 

// R13 Col T
Cell R13cell19 = row.createCell(19);
if (record.getR13_avg_pula_equivalent() != null) {
    R13cell19.setCellValue(record.getR13_avg_pula_equivalent().doubleValue());
    R13cell19.setCellStyle(numberStyle);
} else {
    R13cell19.setCellValue("");
    R13cell19.setCellStyle(textStyle);
}

// R14 Col B
row = sheet.getRow(13);
Cell R14cell1 = row.createCell(1);
if (record1.getR14_ex_rate_buy() != null) {
    R14cell1.setCellValue(record1.getR14_ex_rate_buy().doubleValue());
    R14cell1.setCellStyle(numberStyle);
} else {
    R14cell1.setCellValue("");
    R14cell1.setCellStyle(textStyle);
}

// R14 Col C
Cell R14cell2 = row.createCell(2);
if (record1.getR14_ex_rate_mid() != null) {
    R14cell2.setCellValue(record1.getR14_ex_rate_mid().doubleValue());
    R14cell2.setCellStyle(numberStyle);
} else {
    R14cell2.setCellValue("");
    R14cell2.setCellStyle(textStyle);
}

// R14 Col D
Cell R14cell3 = row.createCell(3);
if (record1.getR14_ex_rate_sell() != null) {
    R14cell3.setCellValue(record1.getR14_ex_rate_sell().doubleValue());
    R14cell3.setCellStyle(numberStyle);
} else {
    R14cell3.setCellValue("");
    R14cell3.setCellStyle(textStyle);
}

// R14 Col E
Cell R14cell4 = row.createCell(4);
if (record.getR14_current() != null) {
    R14cell4.setCellValue(record.getR14_current().doubleValue());
    R14cell4.setCellStyle(numberStyle);
} else {
    R14cell4.setCellValue("");
    R14cell1.setCellStyle(textStyle);
}

// R14 Col F
Cell R14cell5 = row.createCell(5);
if (record.getR14_call() != null) {
    R14cell5.setCellValue(record.getR14_call().doubleValue());
    R14cell5.setCellStyle(numberStyle);
} else {
    R14cell5.setCellValue("");
    R14cell5.setCellStyle(textStyle);
}

// R14 Col G
Cell R14cell6 = row.createCell(6);
if (record.getR14_savings() != null) {
    R14cell6.setCellValue(record.getR14_savings().doubleValue());
    R14cell6.setCellStyle(numberStyle);
} else {
    R14cell6.setCellValue("");
    R14cell6.setCellStyle(textStyle);
	
}

// R14 Col H
Cell R14cell7 = row.createCell(7);
if (record1.getR14_notice_0to31()!= null) {
    R14cell7.setCellValue(record1.getR14_notice_0to31().doubleValue());
    R14cell7.setCellStyle(numberStyle);
} else {
    R14cell7.setCellValue("");
    R14cell7.setCellStyle(textStyle);
	
}

// R14 Col I
Cell R14cell8= row.createCell(8);
if (record1.getR14_notice_32to88() != null) {
    R14cell8.setCellValue(record1.getR14_notice_32to88().doubleValue());
    R14cell8.setCellStyle(numberStyle);
} else {
    R14cell8.setCellValue("");
    R14cell8.setCellStyle(textStyle);
}

// R14 Col J
Cell R14cell9 = row.createCell(9);
if (record.getR14_fix_depo_91_day_depo() != null) {
    R14cell9.setCellValue(record.getR14_fix_depo_91_day_depo().doubleValue());
    R14cell9.setCellStyle(numberStyle);
} else {
    R14cell9.setCellValue("");
    R14cell9.setCellStyle(textStyle);
}

// R14 Col K
Cell R14cell10 = row.createCell(10);
if (record.getR14_fix_depo_1to2() != null) {
    R14cell10.setCellValue(record.getR14_fix_depo_1to2().doubleValue());
    R14cell10.setCellStyle(numberStyle);
} else {
    R14cell10.setCellValue("");
    R14cell10.setCellStyle(textStyle);
}

// R14 Col L
Cell R14cell11 = row.createCell(11);
if (record.getR14_fix_depo_4to6() != null) {
    R14cell11.setCellValue(record.getR14_fix_depo_4to6().doubleValue());
    R14cell11.setCellStyle(numberStyle);
} else {
    R14cell11.setCellValue("");
    R14cell11.setCellStyle(textStyle);
}

// R14 Col M
Cell R14cell12 = row.createCell(12);
if (record.getR14_fix_depo_7to12() != null) {
    R14cell12.setCellValue(record.getR14_fix_depo_7to12().doubleValue());
    R14cell12.setCellStyle(numberStyle);
} else {
    R14cell12.setCellValue("");
    R14cell12.setCellStyle(textStyle);
}

// R14 Col N
Cell R14cell13 = row.createCell(13);
if (record.getR14_fix_depo_13to18()!= null) {
    R14cell13.setCellValue(record.getR14_fix_depo_13to18().doubleValue());
    R14cell13.setCellStyle(numberStyle);
} else {
    R14cell13.setCellValue("");
    R14cell13.setCellStyle(textStyle);
}

// R14 Col O
Cell R14cell14 = row.createCell(14);
if (record.getR14_fix_depo_19to24()!= null) {
    R14cell14.setCellValue(record.getR14_fix_depo_19to24().doubleValue());
    R14cell14.setCellStyle(numberStyle);
} else {
    R14cell14.setCellValue("");
    R14cell14.setCellStyle(textStyle);
}

// R14 Col P
Cell R14cell15 = row.createCell(15);
if (record.getR14_fix_depo_over24() != null) {
    R14cell15.setCellValue(record.getR14_fix_depo_over24().doubleValue());
    R14cell15.setCellStyle(numberStyle);
} else {
    R14cell15.setCellValue("");
    R14cell15.setCellStyle(textStyle);
}


// R14 Col Q
Cell R14cell16 = row.createCell(16);
if (record1.getR14_cer_of_depo() != null) {
    R14cell16.setCellValue(record1.getR14_cer_of_depo().doubleValue());
    R14cell16.setCellStyle(numberStyle);
} else {
    R14cell16.setCellValue("");
    R14cell16.setCellStyle(textStyle);
}

// R14 Col S
Cell R14cell18 = row.createCell(18);
if (record.getR14_pula_equivalent() != null) {
    R14cell18.setCellValue(record.getR14_pula_equivalent().doubleValue());
    R14cell18.setCellStyle(numberStyle);
} else {
    R14cell18.setCellValue("");
    R14cell18.setCellStyle(textStyle);
}
 

// R14 Col T
Cell R14cell19 = row.createCell(19);
if (record.getR14_avg_pula_equivalent() != null) {
    R14cell19.setCellValue(record.getR14_avg_pula_equivalent().doubleValue());
    R14cell19.setCellStyle(numberStyle);
} else {
    R14cell19.setCellValue("");
    R14cell19.setCellStyle(textStyle);
}

// R15 Col B
row = sheet.getRow(14);
Cell R15cell1 = row.createCell(1);
if (record1.getR15_ex_rate_buy() != null) {
    R15cell1.setCellValue(record1.getR15_ex_rate_buy().doubleValue());
    R15cell1.setCellStyle(numberStyle);
} else {
    R15cell1.setCellValue("");
    R15cell1.setCellStyle(textStyle);
}

// R15 Col C
Cell R15cell2 = row.createCell(2);
if (record1.getR15_ex_rate_mid() != null) {
    R15cell2.setCellValue(record1.getR15_ex_rate_mid().doubleValue());
    R15cell2.setCellStyle(numberStyle);
} else {
    R15cell2.setCellValue("");
    R15cell2.setCellStyle(textStyle);
}

// R15 Col D
Cell R15cell3 = row.createCell(3);
if (record1.getR15_ex_rate_sell() != null) {
    R15cell3.setCellValue(record1.getR15_ex_rate_sell().doubleValue());
    R15cell3.setCellStyle(numberStyle);
} else {
    R15cell3.setCellValue("");
    R15cell3.setCellStyle(textStyle);
}

// R15 Col E
Cell R15cell4 = row.createCell(4);
if (record.getR15_current() != null) {
    R15cell4.setCellValue(record.getR15_current().doubleValue());
    R15cell4.setCellStyle(numberStyle);
} else {
    R15cell4.setCellValue("");
    R15cell1.setCellStyle(textStyle);
}

// R15 Col F
Cell R15cell5 = row.createCell(5);
if (record.getR15_call() != null) {
    R15cell5.setCellValue(record.getR15_call().doubleValue());
    R15cell5.setCellStyle(numberStyle);
} else {
    R15cell5.setCellValue("");
    R15cell5.setCellStyle(textStyle);
}

// R15 Col G
Cell R15cell6 = row.createCell(6);
if (record.getR15_savings() != null) {
    R15cell6.setCellValue(record.getR15_savings().doubleValue());
    R15cell6.setCellStyle(numberStyle);
} else {
    R15cell6.setCellValue("");
    R15cell6.setCellStyle(textStyle);
	
}

// R15 Col H
Cell R15cell7 = row.createCell(7);
if (record1.getR15_notice_0to31()!= null) {
    R15cell7.setCellValue(record1.getR15_notice_0to31().doubleValue());
    R15cell7.setCellStyle(numberStyle);
} else {
    R15cell7.setCellValue("");
    R15cell7.setCellStyle(textStyle);
	
}

// R15 Col I
Cell R15cell8= row.createCell(8);
if (record1.getR15_notice_32to88() != null) {
    R15cell8.setCellValue(record1.getR15_notice_32to88().doubleValue());
    R15cell8.setCellStyle(numberStyle);
} else {
    R15cell8.setCellValue("");
    R15cell8.setCellStyle(textStyle);
}

// R15 Col J
Cell R15cell9 = row.createCell(9);
if (record.getR15_fix_depo_91_day_depo() != null) {
    R15cell9.setCellValue(record.getR15_fix_depo_91_day_depo().doubleValue());
    R15cell9.setCellStyle(numberStyle);
} else {
    R15cell9.setCellValue("");
    R15cell9.setCellStyle(textStyle);
}

// R15 Col K
Cell R15cell10 = row.createCell(10);
if (record.getR15_fix_depo_1to2() != null) {
    R15cell10.setCellValue(record.getR15_fix_depo_1to2().doubleValue());
    R15cell10.setCellStyle(numberStyle);
} else {
    R15cell10.setCellValue("");
    R15cell10.setCellStyle(textStyle);
}

// R15 Col L
Cell R15cell11 = row.createCell(11);
if (record.getR15_fix_depo_4to6() != null) {
    R15cell11.setCellValue(record.getR15_fix_depo_4to6().doubleValue());
    R15cell11.setCellStyle(numberStyle);
} else {
    R15cell11.setCellValue("");
    R15cell11.setCellStyle(textStyle);
}

// R15 Col M
Cell R15cell12 = row.createCell(12);
if (record.getR15_fix_depo_7to12() != null) {
    R15cell12.setCellValue(record.getR15_fix_depo_7to12().doubleValue());
    R15cell12.setCellStyle(numberStyle);
} else {
    R15cell12.setCellValue("");
    R15cell12.setCellStyle(textStyle);
}

// R15 Col N
Cell R15cell13 = row.createCell(13);
if (record.getR15_fix_depo_13to18()!= null) {
    R15cell13.setCellValue(record.getR15_fix_depo_13to18().doubleValue());
    R15cell13.setCellStyle(numberStyle);
} else {
    R15cell13.setCellValue("");
    R15cell13.setCellStyle(textStyle);
}

// R15 Col O
Cell R15cell14 = row.createCell(14);
if (record.getR15_fix_depo_19to24()!= null) {
    R15cell14.setCellValue(record.getR15_fix_depo_19to24().doubleValue());
    R15cell14.setCellStyle(numberStyle);
} else {
    R15cell14.setCellValue("");
    R15cell14.setCellStyle(textStyle);
}

// R15 Col P
Cell R15cell15 = row.createCell(15);
if (record.getR15_fix_depo_over24() != null) {
    R15cell15.setCellValue(record.getR15_fix_depo_over24().doubleValue());
    R15cell15.setCellStyle(numberStyle);
} else {
    R15cell15.setCellValue("");
    R15cell15.setCellStyle(textStyle);
}


// R15 Col Q
Cell R15cell16 = row.createCell(16);
if (record1.getR15_cer_of_depo() != null) {
    R15cell16.setCellValue(record1.getR15_cer_of_depo().doubleValue());
    R15cell16.setCellStyle(numberStyle);
} else {
    R15cell16.setCellValue("");
    R15cell16.setCellStyle(textStyle);
}

// R15 Col S
Cell R15cell18 = row.createCell(18);
if (record.getR15_pula_equivalent() != null) {
    R15cell18.setCellValue(record.getR15_pula_equivalent().doubleValue());
    R15cell18.setCellStyle(numberStyle);
} else {
    R15cell18.setCellValue("");
    R15cell18.setCellStyle(textStyle);
}
 

// R15 Col T
Cell R15cell19 = row.createCell(19);
if (record.getR15_avg_pula_equivalent() != null) {
    R15cell19.setCellValue(record.getR15_avg_pula_equivalent().doubleValue());
    R15cell19.setCellStyle(numberStyle);
} else {
    R15cell19.setCellValue("");
    R15cell19.setCellStyle(textStyle);
}

// R16 Col B
row = sheet.getRow(15);
Cell R16cell1 = row.createCell(1);
if (record1.getR16_ex_rate_buy() != null) {
    R16cell1.setCellValue(record1.getR16_ex_rate_buy().doubleValue());
    R16cell1.setCellStyle(numberStyle);
} else {
    R16cell1.setCellValue("");
    R16cell1.setCellStyle(textStyle);
}

// R16 Col C
Cell R16cell2 = row.createCell(2);
if (record1.getR16_ex_rate_mid() != null) {
    R16cell2.setCellValue(record1.getR16_ex_rate_mid().doubleValue());
    R16cell2.setCellStyle(numberStyle);
} else {
    R16cell2.setCellValue("");
    R16cell2.setCellStyle(textStyle);
}

// R16 Col D
Cell R16cell3 = row.createCell(3);
if (record1.getR16_ex_rate_sell() != null) {
    R16cell3.setCellValue(record1.getR16_ex_rate_sell().doubleValue());
    R16cell3.setCellStyle(numberStyle);
} else {
    R16cell3.setCellValue("");
    R16cell3.setCellStyle(textStyle);
}

// R16 Col E
Cell R16cell4 = row.createCell(4);
if (record.getR16_current() != null) {
    R16cell4.setCellValue(record.getR16_current().doubleValue());
    R16cell4.setCellStyle(numberStyle);
} else {
    R16cell4.setCellValue("");
    R16cell1.setCellStyle(textStyle);
}

// R16 Col F
Cell R16cell5 = row.createCell(5);
if (record.getR16_call() != null) {
    R16cell5.setCellValue(record.getR16_call().doubleValue());
    R16cell5.setCellStyle(numberStyle);
} else {
    R16cell5.setCellValue("");
    R16cell5.setCellStyle(textStyle);
}

// R16 Col G
Cell R16cell6 = row.createCell(6);
if (record.getR16_savings() != null) {
    R16cell6.setCellValue(record.getR16_savings().doubleValue());
    R16cell6.setCellStyle(numberStyle);
} else {
    R16cell6.setCellValue("");
    R16cell6.setCellStyle(textStyle);
	
}

// R16 Col H
Cell R16cell7 = row.createCell(7);
if (record1.getR16_notice_0to31()!= null) {
    R16cell7.setCellValue(record1.getR16_notice_0to31().doubleValue());
    R16cell7.setCellStyle(numberStyle);
} else {
    R16cell7.setCellValue("");
    R16cell7.setCellStyle(textStyle);
	
}

// R16 Col I
Cell R16cell8= row.createCell(8);
if (record1.getR16_notice_32to88() != null) {
    R16cell8.setCellValue(record1.getR16_notice_32to88().doubleValue());
    R16cell8.setCellStyle(numberStyle);
} else {
    R16cell8.setCellValue("");
    R16cell8.setCellStyle(textStyle);
}

// R16 Col J
Cell R16cell9 = row.createCell(9);
if (record.getR16_fix_depo_91_day_depo() != null) {
    R16cell9.setCellValue(record.getR16_fix_depo_91_day_depo().doubleValue());
    R16cell9.setCellStyle(numberStyle);
} else {
    R16cell9.setCellValue("");
    R16cell9.setCellStyle(textStyle);
}

// R16 Col K
Cell R16cell10 = row.createCell(10);
if (record.getR16_fix_depo_1to2() != null) {
    R16cell10.setCellValue(record.getR16_fix_depo_1to2().doubleValue());
    R16cell10.setCellStyle(numberStyle);
} else {
    R16cell10.setCellValue("");
    R16cell10.setCellStyle(textStyle);
}

// R16 Col L
Cell R16cell11 = row.createCell(11);
if (record.getR16_fix_depo_4to6() != null) {
    R16cell11.setCellValue(record.getR16_fix_depo_4to6().doubleValue());
    R16cell11.setCellStyle(numberStyle);
} else {
    R16cell11.setCellValue("");
    R16cell11.setCellStyle(textStyle);
}

// R16 Col M
Cell R16cell12 = row.createCell(12);
if (record.getR16_fix_depo_7to12() != null) {
    R16cell12.setCellValue(record.getR16_fix_depo_7to12().doubleValue());
    R16cell12.setCellStyle(numberStyle);
} else {
    R16cell12.setCellValue("");
    R16cell12.setCellStyle(textStyle);
}

// R16 Col N
Cell R16cell13 = row.createCell(13);
if (record.getR16_fix_depo_13to18()!= null) {
    R16cell13.setCellValue(record.getR16_fix_depo_13to18().doubleValue());
    R16cell13.setCellStyle(numberStyle);
} else {
    R16cell13.setCellValue("");
    R16cell13.setCellStyle(textStyle);
}

// R16 Col O
Cell R16cell14 = row.createCell(14);
if (record.getR16_fix_depo_19to24()!= null) {
    R16cell14.setCellValue(record.getR16_fix_depo_19to24().doubleValue());
    R16cell14.setCellStyle(numberStyle);
} else {
    R16cell14.setCellValue("");
    R16cell14.setCellStyle(textStyle);
}

// R16 Col P
Cell R16cell15 = row.createCell(15);
if (record.getR16_fix_depo_over24() != null) {
    R16cell15.setCellValue(record.getR16_fix_depo_over24().doubleValue());
    R16cell15.setCellStyle(numberStyle);
} else {
    R16cell15.setCellValue("");
    R16cell15.setCellStyle(textStyle);

}

// R16 Col Q
Cell R16cell16 = row.createCell(16);
if (record1.getR16_cer_of_depo() != null) {
    R16cell16.setCellValue(record1.getR16_cer_of_depo().doubleValue());
    R16cell16.setCellStyle(numberStyle);
} else {
    R16cell16.setCellValue("");
    R16cell16.setCellStyle(textStyle);
}

// R16 Col S
Cell R16cell18 = row.createCell(18);
if (record.getR16_pula_equivalent() != null) {
    R16cell18.setCellValue(record.getR16_pula_equivalent().doubleValue());
    R16cell18.setCellStyle(numberStyle);
} else {
    R16cell18.setCellValue("");
    R16cell18.setCellStyle(textStyle);
}
 

// R16 Col T
Cell R16cell19 = row.createCell(19);
if (record.getR16_avg_pula_equivalent() != null) {
    R16cell19.setCellValue(record.getR16_avg_pula_equivalent().doubleValue());
    R16cell19.setCellStyle(numberStyle);
} else {
    R16cell19.setCellValue("");
    R16cell19.setCellStyle(textStyle);
}

// R17 Col B
row = sheet.getRow(16);
Cell R17cell1 = row.createCell(1);
if (record.getR17_ex_rate_buy() != null) {
    R17cell1.setCellValue(record.getR17_ex_rate_buy().doubleValue());
    R17cell1.setCellStyle(numberStyle);
} else {
    R17cell1.setCellValue("");
    R17cell1.setCellStyle(textStyle);
}

// R17 Col C
Cell R17cell2 = row.createCell(2);
if (record.getR17_ex_rate_mid() != null) {
    R17cell2.setCellValue(record.getR17_ex_rate_mid().doubleValue());
    R17cell2.setCellStyle(numberStyle);
} else {
    R17cell2.setCellValue("");
    R17cell2.setCellStyle(textStyle);
}

// R17 Col D
Cell R17cell3 = row.createCell(3);
if (record.getR17_ex_rate_sell() != null) {
    R17cell3.setCellValue(record.getR17_ex_rate_sell().doubleValue());
    R17cell3.setCellStyle(numberStyle);
} else {
    R17cell3.setCellValue("");
    R17cell3.setCellStyle(textStyle);
}

// R18 Col B
row = sheet.getRow(17);
Cell R18cell1 = row.createCell(1);
if (record.getR18_ex_rate_buy() != null) {
    R18cell1.setCellValue(record.getR18_ex_rate_buy().doubleValue());
    R18cell1.setCellStyle(numberStyle);
} else {
    R18cell1.setCellValue("");
    R18cell1.setCellStyle(textStyle);
}

// R18 Col C
Cell R18cell2 = row.createCell(2);
if (record.getR18_ex_rate_mid() != null) {
    R18cell2.setCellValue(record.getR18_ex_rate_mid().doubleValue());
    R18cell2.setCellStyle(numberStyle);
} else {
    R18cell2.setCellValue("");
    R18cell2.setCellStyle(textStyle);
}

// R18 Col D
Cell R18cell3 = row.createCell(3);
if (record.getR18_ex_rate_sell() != null) {
    R18cell3.setCellValue(record.getR18_ex_rate_sell().doubleValue());
    R18cell3.setCellStyle(numberStyle);
} else {
    R18cell3.setCellValue("");
    R18cell3.setCellStyle(textStyle);
}

// R18 Col E
Cell R18cell4 = row.createCell(4);
if (record.getR18_current() != null) {
    R18cell4.setCellValue(record.getR18_current().doubleValue());
    R18cell4.setCellStyle(numberStyle);
} else {
    R18cell4.setCellValue("");
    R18cell1.setCellStyle(textStyle);
}

// R18 Col F
Cell R18cell5 = row.createCell(5);
if (record.getR18_call() != null) {
    R18cell5.setCellValue(record.getR18_call().doubleValue());
    R18cell5.setCellStyle(numberStyle);
} else {
    R18cell5.setCellValue("");
    R18cell5.setCellStyle(textStyle);
}

// R18 Col G
Cell R18cell6 = row.createCell(6);
if (record.getR18_savings() != null) {
    R18cell6.setCellValue(record.getR18_savings().doubleValue());
    R18cell6.setCellStyle(numberStyle);
} else {
    R18cell6.setCellValue("");
    R18cell6.setCellStyle(textStyle);
	
}

// R18 Col H
Cell R18cell7 = row.createCell(7);
if (record1.getR18_notice_0to31()!= null) {
    R18cell7.setCellValue(record1.getR18_notice_0to31().doubleValue());
    R18cell7.setCellStyle(numberStyle);
} else {
    R18cell7.setCellValue("");
    R18cell7.setCellStyle(textStyle);
	
}

// R18 Col I
Cell R18cell8= row.createCell(8);
if (record1.getR18_notice_32to88() != null) {
    R18cell8.setCellValue(record1.getR18_notice_32to88().doubleValue());
    R18cell8.setCellStyle(numberStyle);
} else {
    R18cell8.setCellValue("");
    R18cell8.setCellStyle(textStyle);
}

// R18 Col J
Cell R18cell9 = row.createCell(9);
if (record.getR18_fix_depo_91_day_depo() != null) {
    R18cell9.setCellValue(record.getR18_fix_depo_91_day_depo().doubleValue());
    R18cell9.setCellStyle(numberStyle);
} else {
    R18cell9.setCellValue("");
    R18cell9.setCellStyle(textStyle);
}

// R18 Col K
Cell R18cell10 = row.createCell(10);
if (record.getR18_fix_depo_1to2() != null) {
    R18cell10.setCellValue(record.getR18_fix_depo_1to2().doubleValue());
    R18cell10.setCellStyle(numberStyle);
} else {
    R18cell10.setCellValue("");
    R18cell10.setCellStyle(textStyle);
}

// R18 Col L
Cell R18cell11 = row.createCell(11);
if (record.getR18_fix_depo_4to6() != null) {
    R18cell11.setCellValue(record.getR18_fix_depo_4to6().doubleValue());
    R18cell11.setCellStyle(numberStyle);
} else {
    R18cell11.setCellValue("");
    R18cell11.setCellStyle(textStyle);
}

// R18 Col M
Cell R18cell12 = row.createCell(12);
if (record.getR18_fix_depo_7to12() != null) {
    R18cell12.setCellValue(record.getR18_fix_depo_7to12().doubleValue());
    R18cell12.setCellStyle(numberStyle);
} else {
    R18cell12.setCellValue("");
    R18cell12.setCellStyle(textStyle);
}

// R18 Col N
Cell R18cell13 = row.createCell(13);
if (record.getR18_fix_depo_13to18()!= null) {
    R18cell13.setCellValue(record.getR18_fix_depo_13to18().doubleValue());
    R18cell13.setCellStyle(numberStyle);
} else {
    R18cell13.setCellValue("");
    R18cell13.setCellStyle(textStyle);
}

// R18 Col O
Cell R18cell14 = row.createCell(14);
if (record.getR18_fix_depo_19to24()!= null) {
    R18cell14.setCellValue(record.getR18_fix_depo_19to24().doubleValue());
    R18cell14.setCellStyle(numberStyle);
} else {
    R18cell14.setCellValue("");
    R18cell14.setCellStyle(textStyle);
}

// R18 Col P
Cell R18cell15 = row.createCell(15);
if (record.getR18_fix_depo_over24() != null) {
    R18cell15.setCellValue(record.getR18_fix_depo_over24().doubleValue());
    R18cell15.setCellStyle(numberStyle);
} else {
    R18cell15.setCellValue("");
    R18cell15.setCellStyle(textStyle);
}


// R18 Col Q
Cell R18cell16 = row.createCell(16);
if (record1.getR18_cer_of_depo() != null) {
    R18cell16.setCellValue(record1.getR18_cer_of_depo().doubleValue());
    R18cell16.setCellStyle(numberStyle);
} else {
    R18cell16.setCellValue("");
    R18cell16.setCellStyle(textStyle);
}
// R18 Col R
Cell R18cell17 = row.createCell(17);
if (record.getR18_total() != null) {
    R18cell17.setCellValue(record.getR18_total().doubleValue());
    R18cell17.setCellStyle(numberStyle);
} else {
    R18cell17.setCellValue("");
    R18cell17.setCellStyle(textStyle);
}
// R18 Col S
Cell R18cell18 = row.createCell(18);
if (record.getR18_pula_equivalent() != null) {
    R18cell18.setCellValue(record.getR18_pula_equivalent().doubleValue());
    R18cell18.setCellStyle(numberStyle);
} else {
    R18cell18.setCellValue("");
    R18cell18.setCellStyle(textStyle);
}
// R18 Col S
Cell R18cell19 = row.createCell(19);
if (record.getR18_avg_pula_equivalent() != null) {
    R18cell19.setCellValue(record.getR18_avg_pula_equivalent().doubleValue());
    R18cell18.setCellStyle(numberStyle);
} else {
    R18cell19.setCellValue("");
    R18cell19.setCellStyle(textStyle);
}

//Entity 2
// R28 Col B
row = sheet.getRow(27);
Cell R28cell1 = row.createCell(1);
if (record1.getR28_import() != null) {
    R28cell1.setCellValue(record1.getR28_import().doubleValue());
    R28cell1.setCellStyle(numberStyle);
} else {
    R28cell1.setCellValue("");
    R28cell1.setCellStyle(textStyle);
}
// R28 Col C
Cell R28cell2 = row.createCell(2);
if (record1.getR28_investment () != null) {
    R28cell2.setCellValue(record1.getR28_investment().doubleValue());
    R28cell2.setCellStyle(numberStyle);
} else {
    R28cell2.setCellValue("");
    R28cell2.setCellStyle(textStyle);
}
// R28 Col D
Cell R28cell3 = row.createCell(3);
if (record1.getR28_other () != null) {
    R28cell3.setCellValue(record1.getR28_other().doubleValue());
    R28cell3.setCellStyle(numberStyle);
} else {
    R28cell3.setCellValue("");
    R28cell3.setCellStyle(textStyle);
}
// R29 Col B
row = sheet.getRow(28);
Cell R29cell1 = row.createCell(1);
if (record1.getR29_import() != null) {
    R29cell1.setCellValue(record1.getR29_import().doubleValue());
    R29cell1.setCellStyle(numberStyle);
} else {
    R29cell1.setCellValue("");
    R29cell1.setCellStyle(textStyle);
}
// R29 Col C
Cell R29cell2 = row.createCell(2);
if (record1.getR29_investment () != null) {
    R29cell2.setCellValue(record1.getR29_investment().doubleValue());
    R29cell2.setCellStyle(numberStyle);
} else {
    R29cell2.setCellValue("");
    R29cell2.setCellStyle(textStyle);
}
// R29 Col D
Cell R29cell3 = row.createCell(3);
if (record1.getR29_other () != null) {
    R29cell3.setCellValue(record1.getR29_other().doubleValue());
    R29cell3.setCellStyle(numberStyle);
} else {
    R29cell3.setCellValue("");
    R29cell3.setCellStyle(textStyle);
}

// R30 Col B
row = sheet.getRow(29);
Cell R30cell1 = row.createCell(1);
if (record1.getR30_import() != null) {
    R30cell1.setCellValue(record1.getR30_import().doubleValue());
    R30cell1.setCellStyle(numberStyle);
} else {
    R30cell1.setCellValue("");
    R30cell1.setCellStyle(textStyle);
}
// R30 Col C
Cell R30cell2 = row.createCell(2);
if (record1.getR30_investment () != null) {
    R30cell2.setCellValue(record1.getR30_investment().doubleValue());
    R30cell2.setCellStyle(numberStyle);
} else {
    R30cell2.setCellValue("");
    R30cell2.setCellStyle(textStyle);
}
// R30 Col D
Cell R30cell3 = row.createCell(3);
if (record1.getR30_other () != null) {
    R30cell3.setCellValue(record1.getR30_other().doubleValue());
    R30cell3.setCellStyle(numberStyle);
} else {
    R30cell3.setCellValue("");
    R30cell3.setCellStyle(textStyle);
}
// R31 Col B
row = sheet.getRow(30);
Cell R31cell1 = row.createCell(1);
if (record1.getR31_import() != null) {
    R31cell1.setCellValue(record1.getR31_import().doubleValue());
    R31cell1.setCellStyle(numberStyle);
} else {
    R31cell1.setCellValue("");
    R31cell1.setCellStyle(textStyle);
}
// R31 Col C
Cell R31cell2 = row.createCell(2);
if (record1.getR31_investment () != null) {
    R31cell2.setCellValue(record1.getR31_investment().doubleValue());
    R31cell2.setCellStyle(numberStyle);
} else {
    R31cell2.setCellValue("");
    R31cell2.setCellStyle(textStyle);
}
// R31 Col D
Cell R31cell3 = row.createCell(3);
if (record1.getR31_other () != null) {
    R31cell3.setCellValue(record1.getR31_other().doubleValue());
    R31cell3.setCellStyle(numberStyle);
} else {
    R31cell3.setCellValue("");
    R31cell3.setCellStyle(textStyle);
}
// R32 Col B
row = sheet.getRow(31);
Cell R32cell1 = row.createCell(1);
if (record1.getR32_import() != null) {
    R32cell1.setCellValue(record1.getR32_import().doubleValue());
    R32cell1.setCellStyle(numberStyle);
} else {
    R32cell1.setCellValue("");
    R32cell1.setCellStyle(textStyle);
}
// R32 Col C
Cell R32cell2 = row.createCell(2);
if (record1.getR32_investment () != null) {
    R32cell2.setCellValue(record1.getR32_investment().doubleValue());
    R32cell2.setCellStyle(numberStyle);
} else {
    R32cell2.setCellValue("");
    R32cell2.setCellStyle(textStyle);
}
// R32 Col D
Cell R32cell3 = row.createCell(3);
if (record1.getR32_other () != null) {
    R32cell3.setCellValue(record1.getR32_other().doubleValue());
    R32cell3.setCellStyle(numberStyle);
} else {
    R32cell3.setCellValue("");
    R32cell3.setCellStyle(textStyle);
}
// R33 Col B
row = sheet.getRow(32);
Cell R33cell1 = row.createCell(1);
if (record1.getR33_import() != null) {
    R33cell1.setCellValue(record1.getR33_import().doubleValue());
    R33cell1.setCellStyle(numberStyle);
} else {
    R33cell1.setCellValue("");
    R33cell1.setCellStyle(textStyle);
}
// R33 Col C
Cell R33cell2 = row.createCell(2);
if (record1.getR33_investment () != null) {
    R33cell2.setCellValue(record1.getR33_investment().doubleValue());
    R33cell2.setCellStyle(numberStyle);
} else {
    R33cell2.setCellValue("");
    R33cell2.setCellStyle(textStyle);
}
// R33 Col D
Cell R33cell3 = row.createCell(3);
if (record1.getR33_other () != null) {
    R33cell3.setCellValue(record1.getR33_other().doubleValue());
    R33cell3.setCellStyle(numberStyle);
} else {
    R33cell3.setCellValue("");
    R33cell3.setCellStyle(textStyle);
}

//Entity 3

row = sheet.getRow(27);
Cell R28cell1e3 = row.createCell(8);
if (record1.getR28_residents () != null) {
    R28cell1e3.setCellValue(record1.getR28_residents().doubleValue());
    R28cell1e3.setCellStyle(numberStyle);
} else {
    R28cell1e3.setCellValue("");
    R28cell1e3.setCellStyle(textStyle);
}
// R28 Col C
Cell R28cell2e3 = row.createCell(9);
if (record1.getR28_non_residents () != null) {
    R28cell2e3.setCellValue(record1.getR28_non_residents().doubleValue());
    R28cell2e3.setCellStyle(numberStyle);
} else {
    R28cell2e3.setCellValue("");
    R28cell2e3.setCellStyle(textStyle);
}
// R29 Col B
row = sheet.getRow(28);
Cell R29cell1e3 = row.createCell(8);
if (record1.getR29_residents () != null) {
    R29cell1e3.setCellValue(record1.getR29_residents().doubleValue());
    R29cell1e3.setCellStyle(numberStyle);
} else {
    R29cell1e3.setCellValue("");
    R29cell1e3.setCellStyle(textStyle);
}
// R29 Col C
Cell R29cell2e3 = row.createCell(9);
if (record1.getR29_non_residents () != null) {
    R29cell2e3.setCellValue(record1.getR29_non_residents().doubleValue());
    R29cell2e3.setCellStyle(numberStyle);
} else {
    R29cell2e3.setCellValue("");
    R29cell2e3.setCellStyle(textStyle);
}
// R30 Col B
row = sheet.getRow(29);
Cell R30cell1e3 = row.createCell(8);
if (record1.getR30_residents () != null) {
    R30cell1e3.setCellValue(record1.getR30_residents().doubleValue());
    R30cell1e3.setCellStyle(numberStyle);
} else {
    R30cell1e3.setCellValue("");
    R30cell1e3.setCellStyle(textStyle);
}
// R30 Col C
Cell R30cell2e3 = row.createCell(9);
if (record1.getR30_non_residents () != null) {
    R30cell2e3.setCellValue(record1.getR30_non_residents().doubleValue());
    R30cell2e3.setCellStyle(numberStyle);
} else {
    R30cell2e3.setCellValue("");
    R30cell2e3.setCellStyle(textStyle);
}
// R31 Col B
row = sheet.getRow(30);
Cell R31cell1e3 = row.createCell(8);
if (record1.getR31_residents () != null) {
    R31cell1e3.setCellValue(record1.getR31_residents().doubleValue());
    R31cell1e3.setCellStyle(numberStyle);
} else {
    R31cell1e3.setCellValue("");
    R31cell1e3.setCellStyle(textStyle);
}
// R31 Col C
Cell R31cell2e3 = row.createCell(9);
if (record1.getR31_non_residents () != null) {
    R31cell2e3.setCellValue(record1.getR31_non_residents().doubleValue());
    R31cell2e3.setCellStyle(numberStyle);
} else {
    R31cell2e3.setCellValue("");
    R31cell2e3.setCellStyle(textStyle);
}

// R32 Col B
row = sheet.getRow(31);
Cell R32cell1e3 = row.createCell(8);
if (record1.getR32_residents () != null) {
    R32cell1e3.setCellValue(record1.getR32_residents().doubleValue());
    R32cell1e3.setCellStyle(numberStyle);
} else {
    R32cell1e3.setCellValue("");
    R32cell1e3.setCellStyle(textStyle);
}
// R32 Col C
Cell R32cell2e3 = row.createCell(9);
if (record1.getR32_non_residents () != null) {
    R32cell2e3.setCellValue(record1.getR32_non_residents().doubleValue());
    R32cell2e3.setCellStyle(numberStyle);
} else {
    R32cell2e3.setCellValue("");
    R32cell2e3.setCellStyle(textStyle);
}
// R33 Col B
row = sheet.getRow(32);
Cell R33cell1e3 = row.createCell(8);
if (record1.getR33_residents () != null) {
    R33cell1e3.setCellValue(record1.getR33_residents().doubleValue());
    R33cell1e3.setCellStyle(numberStyle);
} else {
    R33cell1e3.setCellValue("");
    R33cell1e3.setCellStyle(textStyle);
}
// R33 Col C
Cell R33cell2e3 = row.createCell(9);
if (record1.getR33_non_residents () != null) {
    R33cell2e3.setCellValue(record1.getR33_non_residents().doubleValue());
    R33cell2e3.setCellStyle(numberStyle);
} else {
    R33cell2e3.setCellValue("");
    R33cell2e3.setCellStyle(textStyle);
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

	public List<Object> getM_DEP3Archival() {
		List<Object> M_DEP3Archivallist = new ArrayList<>();
		try {
			M_DEP3Archivallist = BRRS_M_DEP3_Archival_Summary_Repo.getM_DEP3archival();
			M_DEP3Archivallist = BRRS_M_DEP3_Manual_Archival_Summary_Repo.getM_DEP3archival();
			System.out.println("countser" + M_DEP3Archivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_DEP3 Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_DEP3Archivallist;
	}
	
	
	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			 String dtltype, String type, String version) {
try {
logger.info("Generating Excel for BRRS_M_DEP3 ARCHIVAL Details...");
System.out.println("came to Detail download service");
if (type.equals("ARCHIVAL") & version != null) {

}
XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("M_DEP3Detail");

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
balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
balanceStyle.setBorderTop(border);
balanceStyle.setBorderBottom(border);
balanceStyle.setBorderLeft(border);
balanceStyle.setBorderRight(border);


// Header row
String[] headers = {
"CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABEL", "REPORT ADDL CRITERIA", "REPORT_DATE"
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
List<M_DEP3_Archival_Detail_Entity> reportData = BRRS_M_DEP3_Archival_Detail_Repo.getdatabydateList(parsedToDate,version);

if (reportData != null && !reportData.isEmpty()) {
int rowIndex = 1;
for (M_DEP3_Archival_Detail_Entity item : reportData) {
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

row.createCell(4).setCellValue(item.getReportLabel());
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
logger.info("No data found for M_DEP3 — only header will be written.");
}
// Write to byte[]
ByteArrayOutputStream bos = new ByteArrayOutputStream();
workbook.write(bos);
workbook.close();

logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
return bos.toByteArray();

} catch (Exception e) {
logger.error("Error generating M_DEP3 Excel", e);
return new byte[0];
}
}




	public byte[] getExcelM_DEP3ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_DEP3_Archival_Summary_Entity> dataList = BRRS_M_DEP3_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		List<M_DEP3_Manual_Archival_Summary_Entity> dataList1 = BRRS_M_DEP3_Manual_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_DEP3 report. Returning empty result.");
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

					M_DEP3_Archival_Summary_Entity record = dataList.get(i);
					M_DEP3_Manual_Archival_Summary_Entity record1 = dataList1.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// R11 Col B
		            
Cell R11cell1 = row.createCell(1);
if (record1.getR11_ex_rate_buy() != null) {
    R11cell1.setCellValue(record1.getR11_ex_rate_buy().doubleValue());
    R11cell1.setCellStyle(numberStyle);
} else {
    R11cell1.setCellValue("");
    R11cell1.setCellStyle(textStyle);
}

// R11 Col C
Cell R11cell2 = row.createCell(2);
if (record1.getR11_ex_rate_mid() != null) {
    R11cell2.setCellValue(record1.getR11_ex_rate_mid().doubleValue());
    R11cell2.setCellStyle(numberStyle);
} else {
    R11cell2.setCellValue("");
    R11cell2.setCellStyle(textStyle);
}

// R11 Col D
Cell R11cell3 = row.createCell(3);
if (record1.getR11_ex_rate_sell() != null) {
    R11cell3.setCellValue(record1.getR11_ex_rate_sell().doubleValue());
    R11cell3.setCellStyle(numberStyle);
} else {
    R11cell3.setCellValue("");
    R11cell3.setCellStyle(textStyle);
}

// R11 Col E
Cell R11cell4 = row.createCell(4);
if (record.getR11_current()!= null) {
    R11cell4.setCellValue(record.getR11_current().doubleValue());
    R11cell4.setCellStyle(numberStyle);
} else {
    R11cell4.setCellValue("");
    R11cell1.setCellStyle(textStyle);
}

// R11 Col F
Cell R11cell5 = row.createCell(5);
if (record.getR11_call() != null) {
    R11cell5.setCellValue(record.getR11_call().doubleValue());
    R11cell5.setCellStyle(numberStyle);
} else {
    R11cell5.setCellValue("");
    R11cell5.setCellStyle(textStyle);
}

// R11 Col G
Cell R11cell6 = row.createCell(6);
if (record.getR11_savings() != null) {
    R11cell6.setCellValue(record.getR11_savings().doubleValue());
    R11cell6.setCellStyle(numberStyle);
} else {
    R11cell6.setCellValue("");
    R11cell6.setCellStyle(textStyle);
	
}

// R11 Col H
Cell R11cell7 = row.createCell(7);
if (record1.getR11_notice_0to31()!= null) {
    R11cell7.setCellValue(record1.getR11_notice_0to31().doubleValue());
    R11cell7.setCellStyle(numberStyle);
} else {
    R11cell7.setCellValue("");
    R11cell7.setCellStyle(textStyle);
	
}

// R11 Col I
Cell R11cell8= row.createCell(8);
if (record1.getR11_notice_32to88() != null) {
    R11cell8.setCellValue(record1.getR11_notice_32to88().doubleValue());
    R11cell8.setCellStyle(numberStyle);
} else {
    R11cell8.setCellValue("");
    R11cell8.setCellStyle(textStyle);
}

// R11 Col J
Cell R11cell9 = row.createCell(9);
if (record.getR11_fix_depo_91_day_depo() != null) {
    R11cell9.setCellValue(record.getR11_fix_depo_91_day_depo().doubleValue());
    R11cell9.setCellStyle(numberStyle);
} else {
    R11cell9.setCellValue("");
    R11cell9.setCellStyle(textStyle);
}

// R11 Col K
Cell R11cell10 = row.createCell(10);
if (record.getR11_fix_depo_1to2() != null) {
    R11cell10.setCellValue(record.getR11_fix_depo_1to2().doubleValue());
    R11cell10.setCellStyle(numberStyle);
} else {
    R11cell10.setCellValue("");
    R11cell10.setCellStyle(textStyle);
}

// R11 Col L
Cell R11cell11 = row.createCell(11);
if (record.getR11_fix_depo_4to6() != null) {
    R11cell11.setCellValue(record.getR11_fix_depo_4to6().doubleValue());
    R11cell11.setCellStyle(numberStyle);
} else {
    R11cell11.setCellValue("");
    R11cell11.setCellStyle(textStyle);
}

// R11 Col M
Cell R11cell12 = row.createCell(12);
if (record.getR11_fix_depo_7to12() != null) {
    R11cell12.setCellValue(record.getR11_fix_depo_7to12().doubleValue());
    R11cell12.setCellStyle(numberStyle);
} else {
    R11cell12.setCellValue("");
    R11cell12.setCellStyle(textStyle);
}

// R11 Col N
Cell R11cell13 = row.createCell(13);
if (record.getR11_fix_depo_13to18()!= null) {
    R11cell13.setCellValue(record.getR11_fix_depo_13to18().doubleValue());
    R11cell13.setCellStyle(numberStyle);
} else {
    R11cell13.setCellValue("");
    R11cell13.setCellStyle(textStyle);
}

// R11 Col O
Cell R11cell14 = row.createCell(14);
if (record.getR11_fix_depo_19to24()!= null) {
    R11cell14.setCellValue(record.getR11_fix_depo_19to24().doubleValue());
    R11cell14.setCellStyle(numberStyle);
} else {
    R11cell14.setCellValue("");
    R11cell14.setCellStyle(textStyle);
}

// R11 Col P
Cell R11cell15 = row.createCell(15);
if (record.getR11_fix_depo_over24()!= null) {
    R11cell15.setCellValue(record.getR11_fix_depo_over24().doubleValue());
    R11cell15.setCellStyle(numberStyle);
} else {
    R11cell15.setCellValue("");
    R11cell15.setCellStyle(textStyle);
}


// R11 Col Q
Cell R11cell16 = row.createCell(16);
if (record1.getR11_cer_of_depo()!= null) {
    R11cell16.setCellValue(record1.getR11_cer_of_depo().doubleValue());
    R11cell16.setCellStyle(numberStyle);
} else {
    R11cell16.setCellValue("");
    R11cell16.setCellStyle(textStyle);
 
}

// R11 Col S
Cell R11cell18 = row.createCell(18);
if (record.getR11_pula_equivalent () != null) {
    R11cell18.setCellValue(record.getR11_pula_equivalent().doubleValue());
    R11cell18.setCellStyle(numberStyle);
} else {
    R11cell18.setCellValue("");
    R11cell18.setCellStyle(textStyle);
}
 

// R11 Col T
Cell R11cell19 = row.createCell(19);
if (record.getR11_avg_pula_equivalent ()!= null) {
    R11cell19.setCellValue(record.getR11_avg_pula_equivalent().doubleValue());
    R11cell19.setCellStyle(numberStyle);
} else {
    R11cell19.setCellValue("");
    R11cell19.setCellStyle(textStyle);
}
row = sheet.getRow(11);
// R12 Col B
Cell R12cell1 = row.createCell(1);
if (record1.getR12_ex_rate_buy ()!= null) {
    R12cell1.setCellValue(record1.getR12_ex_rate_buy().doubleValue());
    R12cell1.setCellStyle(numberStyle);
} else {
    R12cell1.setCellValue("");
    R12cell1.setCellStyle(textStyle);
}

// R12 Col C
Cell R12cell2 = row.createCell(2);
if (record1.getR12_ex_rate_mid() != null) {
    R12cell2.setCellValue(record1.getR12_ex_rate_mid().doubleValue());
    R12cell2.setCellStyle(numberStyle);
} else {
    R12cell2.setCellValue("");
    R12cell2.setCellStyle(textStyle);
}

// R12 Col D
Cell R12cell3 = row.createCell(3);
if (record1.getR12_ex_rate_sell() != null) {
    R12cell3.setCellValue(record1.getR12_ex_rate_sell().doubleValue());
    R12cell3.setCellStyle(numberStyle);
} else {
    R12cell3.setCellValue("");
    R12cell3.setCellStyle(textStyle);
}

// R12 Col E
Cell R12cell4 = row.createCell(4);
if (record.getR12_current() != null) {
    R12cell4.setCellValue(record.getR12_current().doubleValue());
    R12cell4.setCellStyle(numberStyle);
} else {
    R12cell4.setCellValue("");
    R12cell1.setCellStyle(textStyle);
}

// R12 Col F
Cell R12cell5 = row.createCell(5);
if (record.getR12_call() != null) {
    R12cell5.setCellValue(record.getR12_call().doubleValue());
    R12cell5.setCellStyle(numberStyle);
} else {
    R12cell5.setCellValue("");
    R12cell5.setCellStyle(textStyle);
}

// R12 Col G
Cell R12cell6 = row.createCell(6);
if (record.getR12_savings() != null) {
    R12cell6.setCellValue(record.getR12_savings().doubleValue());
    R12cell6.setCellStyle(numberStyle);
} else {
    R12cell6.setCellValue("");
    R12cell6.setCellStyle(textStyle);
	
}

// R12 Col H
Cell R12cell7 = row.createCell(7);
if (record1.getR12_notice_0to31()!= null) {
    R12cell7.setCellValue(record1.getR12_notice_0to31().doubleValue());
    R12cell7.setCellStyle(numberStyle);
} else {
    R12cell7.setCellValue("");
    R12cell7.setCellStyle(textStyle);
	
}

// R12 Col I
Cell R12cell8= row.createCell(8);
if (record1.getR12_notice_32to88() != null) {
    R12cell8.setCellValue(record1.getR12_notice_32to88().doubleValue());
    R12cell8.setCellStyle(numberStyle);
} else {
    R12cell8.setCellValue("");
    R12cell8.setCellStyle(textStyle);
}

// R12 Col J
Cell R12cell9 = row.createCell(9);
if (record.getR12_fix_depo_91_day_depo() != null) {
    R12cell9.setCellValue(record.getR12_fix_depo_91_day_depo().doubleValue());
    R12cell9.setCellStyle(numberStyle);
} else {
    R12cell9.setCellValue("");
    R12cell9.setCellStyle(textStyle);
}

// R12 Col K
Cell R12cell10 = row.createCell(10);
if (record.getR12_fix_depo_1to2() != null) {
    R12cell10.setCellValue(record.getR12_fix_depo_1to2().doubleValue());
    R12cell10.setCellStyle(numberStyle);
} else {
    R12cell10.setCellValue("");
    R12cell10.setCellStyle(textStyle);
}

// R12 Col L
Cell R12cell11 = row.createCell(11);
if (record.getR12_fix_depo_4to6() != null) {
    R12cell11.setCellValue(record.getR12_fix_depo_4to6().doubleValue());
    R12cell11.setCellStyle(numberStyle);
} else {
    R12cell11.setCellValue("");
    R12cell11.setCellStyle(textStyle);
}

// R12 Col M
Cell R12cell12 = row.createCell(12);
if (record.getR12_fix_depo_7to12() != null) {
    R12cell12.setCellValue(record.getR12_fix_depo_7to12().doubleValue());
    R12cell12.setCellStyle(numberStyle);
} else {
    R12cell12.setCellValue("");
    R12cell12.setCellStyle(textStyle);
}

// R12 Col N
Cell R12cell13 = row.createCell(13);
if (record.getR12_fix_depo_13to18()!= null) {
    R12cell13.setCellValue(record.getR12_fix_depo_13to18().doubleValue());
    R12cell13.setCellStyle(numberStyle);
} else {
    R12cell13.setCellValue("");
    R12cell13.setCellStyle(textStyle);
}

// R12 Col O
Cell R12cell14 = row.createCell(14);
if (record.getR12_fix_depo_19to24()!= null) {
    R12cell14.setCellValue(record.getR12_fix_depo_19to24().doubleValue());
    R12cell14.setCellStyle(numberStyle);
} else {
    R12cell14.setCellValue("");
    R12cell14.setCellStyle(textStyle);
}

// R12 Col P
Cell R12cell15 = row.createCell(15);
if (record.getR12_fix_depo_over24() != null) {
    R12cell15.setCellValue(record.getR12_fix_depo_over24().doubleValue());
    R12cell15.setCellStyle(numberStyle);
} else {
    R12cell15.setCellValue("");
    R12cell15.setCellStyle(textStyle);
}


// R12 Col Q
Cell R12cell16 = row.createCell(16);
if (record1.getR12_cer_of_depo() != null) {
    R12cell16.setCellValue(record1.getR12_cer_of_depo().doubleValue());
    R12cell16.setCellStyle(numberStyle);
} else {
    R12cell16.setCellValue("");
    R12cell16.setCellStyle(textStyle);
}

// R12 Col S
Cell R12cell18 = row.createCell(18);
if (record.getR12_pula_equivalent() != null) {
    R12cell18.setCellValue(record.getR12_pula_equivalent().doubleValue());
    R12cell18.setCellStyle(numberStyle);
} else {
    R12cell18.setCellValue("");
    R12cell18.setCellStyle(textStyle);
}
 

// R12 Col T
Cell R12cell19 = row.createCell(19);
if (record.getR12_avg_pula_equivalent() != null) {
    R12cell19.setCellValue(record.getR12_avg_pula_equivalent().doubleValue());
    R12cell19.setCellStyle(numberStyle);
} else {
    R12cell19.setCellValue("");
    R12cell19.setCellStyle(textStyle);
}

// R13 Col B
row = sheet.getRow(12);
Cell R13cell1 = row.createCell(1);
if (record1.getR13_ex_rate_buy() != null) {
    R13cell1.setCellValue(record1.getR13_ex_rate_buy().doubleValue());
    R13cell1.setCellStyle(numberStyle);
} else {
    R13cell1.setCellValue("");
    R13cell1.setCellStyle(textStyle);
}

// R13 Col C
Cell R13cell2 = row.createCell(2);
if (record1.getR13_ex_rate_mid() != null) {
    R13cell2.setCellValue(record1.getR13_ex_rate_mid().doubleValue());
    R13cell2.setCellStyle(numberStyle);
} else {
    R13cell2.setCellValue("");
    R13cell2.setCellStyle(textStyle);
}

// R13 Col D
Cell R13cell3 = row.createCell(3);
if (record1.getR13_ex_rate_sell() != null) {
    R13cell3.setCellValue(record1.getR13_ex_rate_sell().doubleValue());
    R13cell3.setCellStyle(numberStyle);
} else {
    R13cell3.setCellValue("");
    R13cell3.setCellStyle(textStyle);
}

// R13 Col E
Cell R13cell4 = row.createCell(4);
if (record.getR13_current() != null) {
    R13cell4.setCellValue(record.getR13_current().doubleValue());
    R13cell4.setCellStyle(numberStyle);
} else {
    R13cell4.setCellValue("");
    R13cell1.setCellStyle(textStyle);
}

// R13 Col F
Cell R13cell5 = row.createCell(5);
if (record.getR13_call() != null) {
    R13cell5.setCellValue(record.getR13_call().doubleValue());
    R13cell5.setCellStyle(numberStyle);
} else {
    R13cell5.setCellValue("");
    R13cell5.setCellStyle(textStyle);
}

// R13 Col G
Cell R13cell6 = row.createCell(6);
if (record.getR13_savings() != null) {
    R13cell6.setCellValue(record.getR13_savings().doubleValue());
    R13cell6.setCellStyle(numberStyle);
} else {
    R13cell6.setCellValue("");
    R13cell6.setCellStyle(textStyle);
	
}

// R13 Col H
Cell R13cell7 = row.createCell(7);
if (record1.getR13_notice_0to31()!= null) {
    R13cell7.setCellValue(record1.getR13_notice_0to31().doubleValue());
    R13cell7.setCellStyle(numberStyle);
} else {
    R13cell7.setCellValue("");
    R13cell7.setCellStyle(textStyle);
	
}

// R13 Col I
Cell R13cell8= row.createCell(8);
if (record1.getR13_notice_32to88() != null) {
    R13cell8.setCellValue(record1.getR13_notice_32to88().doubleValue());
    R13cell8.setCellStyle(numberStyle);
} else {
    R13cell8.setCellValue("");
    R13cell8.setCellStyle(textStyle);
}

// R13 Col J
Cell R13cell9 = row.createCell(9);
if (record.getR13_fix_depo_91_day_depo() != null) {
    R13cell9.setCellValue(record.getR13_fix_depo_91_day_depo().doubleValue());
    R13cell9.setCellStyle(numberStyle);
} else {
    R13cell9.setCellValue("");
    R13cell9.setCellStyle(textStyle);
}

// R13 Col K
Cell R13cell10 = row.createCell(10);
if (record.getR13_fix_depo_1to2() != null) {
    R13cell10.setCellValue(record.getR13_fix_depo_1to2().doubleValue());
    R13cell10.setCellStyle(numberStyle);
} else {
    R13cell10.setCellValue("");
    R13cell10.setCellStyle(textStyle);
}

// R13 Col L
Cell R13cell11 = row.createCell(11);
if (record.getR13_fix_depo_4to6() != null) {
    R13cell11.setCellValue(record.getR13_fix_depo_4to6().doubleValue());
    R13cell11.setCellStyle(numberStyle);
} else {
    R13cell11.setCellValue("");
    R13cell11.setCellStyle(textStyle);
}

// R13 Col M
Cell R13cell12 = row.createCell(12);
if (record.getR13_fix_depo_7to12() != null) {
    R13cell12.setCellValue(record.getR13_fix_depo_7to12().doubleValue());
    R13cell12.setCellStyle(numberStyle);
} else {
    R13cell12.setCellValue("");
    R13cell12.setCellStyle(textStyle);
}

// R13 Col N
Cell R13cell13 = row.createCell(13);
if (record.getR13_fix_depo_13to18()!= null) {
    R13cell13.setCellValue(record.getR13_fix_depo_13to18().doubleValue());
    R13cell13.setCellStyle(numberStyle);
} else {
    R13cell13.setCellValue("");
    R13cell13.setCellStyle(textStyle);
}

// R13 Col O
Cell R13cell14 = row.createCell(14);
if (record.getR13_fix_depo_19to24()!= null) {
    R13cell14.setCellValue(record.getR13_fix_depo_19to24().doubleValue());
    R13cell14.setCellStyle(numberStyle);
} else {
    R13cell14.setCellValue("");
    R13cell14.setCellStyle(textStyle);
}

// R13 Col P
Cell R13cell15 = row.createCell(15);
if (record.getR13_fix_depo_over24() != null) {
    R13cell15.setCellValue(record.getR13_fix_depo_over24().doubleValue());
    R13cell15.setCellStyle(numberStyle);
} else {
    R13cell15.setCellValue("");
    R13cell15.setCellStyle(textStyle);
}


// R13 Col Q
Cell R13cell16 = row.createCell(16);
if (record1.getR13_cer_of_depo() != null) {
    R13cell16.setCellValue(record1.getR13_cer_of_depo().doubleValue());
    R13cell16.setCellStyle(numberStyle);
} else {
    R13cell16.setCellValue("");
    R13cell16.setCellStyle(textStyle);
}

// R13 Col S
Cell R13cell18 = row.createCell(18);
if (record.getR13_pula_equivalent() != null) {
    R13cell18.setCellValue(record.getR13_pula_equivalent().doubleValue());
    R13cell18.setCellStyle(numberStyle);
} else {
    R13cell18.setCellValue("");
    R13cell18.setCellStyle(textStyle);
}
 

// R13 Col T
Cell R13cell19 = row.createCell(19);
if (record.getR13_avg_pula_equivalent() != null) {
    R13cell19.setCellValue(record.getR13_avg_pula_equivalent().doubleValue());
    R13cell19.setCellStyle(numberStyle);
} else {
    R13cell19.setCellValue("");
    R13cell19.setCellStyle(textStyle);
}

// R14 Col B
row = sheet.getRow(13);
Cell R14cell1 = row.createCell(1);
if (record1.getR14_ex_rate_buy() != null) {
    R14cell1.setCellValue(record1.getR14_ex_rate_buy().doubleValue());
    R14cell1.setCellStyle(numberStyle);
} else {
    R14cell1.setCellValue("");
    R14cell1.setCellStyle(textStyle);
}

// R14 Col C
Cell R14cell2 = row.createCell(2);
if (record1.getR14_ex_rate_mid() != null) {
    R14cell2.setCellValue(record1.getR14_ex_rate_mid().doubleValue());
    R14cell2.setCellStyle(numberStyle);
} else {
    R14cell2.setCellValue("");
    R14cell2.setCellStyle(textStyle);
}

// R14 Col D
Cell R14cell3 = row.createCell(3);
if (record1.getR14_ex_rate_sell() != null) {
    R14cell3.setCellValue(record1.getR14_ex_rate_sell().doubleValue());
    R14cell3.setCellStyle(numberStyle);
} else {
    R14cell3.setCellValue("");
    R14cell3.setCellStyle(textStyle);
}

// R14 Col E
Cell R14cell4 = row.createCell(4);
if (record.getR14_current() != null) {
    R14cell4.setCellValue(record.getR14_current().doubleValue());
    R14cell4.setCellStyle(numberStyle);
} else {
    R14cell4.setCellValue("");
    R14cell1.setCellStyle(textStyle);
}

// R14 Col F
Cell R14cell5 = row.createCell(5);
if (record.getR14_call() != null) {
    R14cell5.setCellValue(record.getR14_call().doubleValue());
    R14cell5.setCellStyle(numberStyle);
} else {
    R14cell5.setCellValue("");
    R14cell5.setCellStyle(textStyle);
}

// R14 Col G
Cell R14cell6 = row.createCell(6);
if (record.getR14_savings() != null) {
    R14cell6.setCellValue(record.getR14_savings().doubleValue());
    R14cell6.setCellStyle(numberStyle);
} else {
    R14cell6.setCellValue("");
    R14cell6.setCellStyle(textStyle);
	
}

// R14 Col H
Cell R14cell7 = row.createCell(7);
if (record1.getR14_notice_0to31()!= null) {
    R14cell7.setCellValue(record1.getR14_notice_0to31().doubleValue());
    R14cell7.setCellStyle(numberStyle);
} else {
    R14cell7.setCellValue("");
    R14cell7.setCellStyle(textStyle);
	
}

// R14 Col I
Cell R14cell8= row.createCell(8);
if (record1.getR14_notice_32to88() != null) {
    R14cell8.setCellValue(record1.getR14_notice_32to88().doubleValue());
    R14cell8.setCellStyle(numberStyle);
} else {
    R14cell8.setCellValue("");
    R14cell8.setCellStyle(textStyle);
}

// R14 Col J
Cell R14cell9 = row.createCell(9);
if (record.getR14_fix_depo_91_day_depo() != null) {
    R14cell9.setCellValue(record.getR14_fix_depo_91_day_depo().doubleValue());
    R14cell9.setCellStyle(numberStyle);
} else {
    R14cell9.setCellValue("");
    R14cell9.setCellStyle(textStyle);
}

// R14 Col K
Cell R14cell10 = row.createCell(10);
if (record.getR14_fix_depo_1to2() != null) {
    R14cell10.setCellValue(record.getR14_fix_depo_1to2().doubleValue());
    R14cell10.setCellStyle(numberStyle);
} else {
    R14cell10.setCellValue("");
    R14cell10.setCellStyle(textStyle);
}

// R14 Col L
Cell R14cell11 = row.createCell(11);
if (record.getR14_fix_depo_4to6() != null) {
    R14cell11.setCellValue(record.getR14_fix_depo_4to6().doubleValue());
    R14cell11.setCellStyle(numberStyle);
} else {
    R14cell11.setCellValue("");
    R14cell11.setCellStyle(textStyle);
}

// R14 Col M
Cell R14cell12 = row.createCell(12);
if (record.getR14_fix_depo_7to12() != null) {
    R14cell12.setCellValue(record.getR14_fix_depo_7to12().doubleValue());
    R14cell12.setCellStyle(numberStyle);
} else {
    R14cell12.setCellValue("");
    R14cell12.setCellStyle(textStyle);
}

// R14 Col N
Cell R14cell13 = row.createCell(13);
if (record.getR14_fix_depo_13to18()!= null) {
    R14cell13.setCellValue(record.getR14_fix_depo_13to18().doubleValue());
    R14cell13.setCellStyle(numberStyle);
} else {
    R14cell13.setCellValue("");
    R14cell13.setCellStyle(textStyle);
}

// R14 Col O
Cell R14cell14 = row.createCell(14);
if (record.getR14_fix_depo_19to24()!= null) {
    R14cell14.setCellValue(record.getR14_fix_depo_19to24().doubleValue());
    R14cell14.setCellStyle(numberStyle);
} else {
    R14cell14.setCellValue("");
    R14cell14.setCellStyle(textStyle);
}

// R14 Col P
Cell R14cell15 = row.createCell(15);
if (record.getR14_fix_depo_over24() != null) {
    R14cell15.setCellValue(record.getR14_fix_depo_over24().doubleValue());
    R14cell15.setCellStyle(numberStyle);
} else {
    R14cell15.setCellValue("");
    R14cell15.setCellStyle(textStyle);
}


// R14 Col Q
Cell R14cell16 = row.createCell(16);
if (record1.getR14_cer_of_depo() != null) {
    R14cell16.setCellValue(record1.getR14_cer_of_depo().doubleValue());
    R14cell16.setCellStyle(numberStyle);
} else {
    R14cell16.setCellValue("");
    R14cell16.setCellStyle(textStyle);
}

// R14 Col S
Cell R14cell18 = row.createCell(18);
if (record.getR14_pula_equivalent() != null) {
    R14cell18.setCellValue(record.getR14_pula_equivalent().doubleValue());
    R14cell18.setCellStyle(numberStyle);
} else {
    R14cell18.setCellValue("");
    R14cell18.setCellStyle(textStyle);
}
 

// R14 Col T
Cell R14cell19 = row.createCell(19);
if (record.getR14_avg_pula_equivalent() != null) {
    R14cell19.setCellValue(record.getR14_avg_pula_equivalent().doubleValue());
    R14cell19.setCellStyle(numberStyle);
} else {
    R14cell19.setCellValue("");
    R14cell19.setCellStyle(textStyle);
}

// R15 Col B
row = sheet.getRow(14);
Cell R15cell1 = row.createCell(1);
if (record1.getR15_ex_rate_buy() != null) {
    R15cell1.setCellValue(record1.getR15_ex_rate_buy().doubleValue());
    R15cell1.setCellStyle(numberStyle);
} else {
    R15cell1.setCellValue("");
    R15cell1.setCellStyle(textStyle);
}

// R15 Col C
Cell R15cell2 = row.createCell(2);
if (record1.getR15_ex_rate_mid() != null) {
    R15cell2.setCellValue(record1.getR15_ex_rate_mid().doubleValue());
    R15cell2.setCellStyle(numberStyle);
} else {
    R15cell2.setCellValue("");
    R15cell2.setCellStyle(textStyle);
}

// R15 Col D
Cell R15cell3 = row.createCell(3);
if (record1.getR15_ex_rate_sell() != null) {
    R15cell3.setCellValue(record1.getR15_ex_rate_sell().doubleValue());
    R15cell3.setCellStyle(numberStyle);
} else {
    R15cell3.setCellValue("");
    R15cell3.setCellStyle(textStyle);
}

// R15 Col E
Cell R15cell4 = row.createCell(4);
if (record.getR15_current() != null) {
    R15cell4.setCellValue(record.getR15_current().doubleValue());
    R15cell4.setCellStyle(numberStyle);
} else {
    R15cell4.setCellValue("");
    R15cell1.setCellStyle(textStyle);
}

// R15 Col F
Cell R15cell5 = row.createCell(5);
if (record.getR15_call() != null) {
    R15cell5.setCellValue(record.getR15_call().doubleValue());
    R15cell5.setCellStyle(numberStyle);
} else {
    R15cell5.setCellValue("");
    R15cell5.setCellStyle(textStyle);
}

// R15 Col G
Cell R15cell6 = row.createCell(6);
if (record.getR15_savings() != null) {
    R15cell6.setCellValue(record.getR15_savings().doubleValue());
    R15cell6.setCellStyle(numberStyle);
} else {
    R15cell6.setCellValue("");
    R15cell6.setCellStyle(textStyle);
	
}

// R15 Col H
Cell R15cell7 = row.createCell(7);
if (record1.getR15_notice_0to31()!= null) {
    R15cell7.setCellValue(record1.getR15_notice_0to31().doubleValue());
    R15cell7.setCellStyle(numberStyle);
} else {
    R15cell7.setCellValue("");
    R15cell7.setCellStyle(textStyle);
	
}

// R15 Col I
Cell R15cell8= row.createCell(8);
if (record1.getR15_notice_32to88() != null) {
    R15cell8.setCellValue(record1.getR15_notice_32to88().doubleValue());
    R15cell8.setCellStyle(numberStyle);
} else {
    R15cell8.setCellValue("");
    R15cell8.setCellStyle(textStyle);
}

// R15 Col J
Cell R15cell9 = row.createCell(9);
if (record.getR15_fix_depo_91_day_depo() != null) {
    R15cell9.setCellValue(record.getR15_fix_depo_91_day_depo().doubleValue());
    R15cell9.setCellStyle(numberStyle);
} else {
    R15cell9.setCellValue("");
    R15cell9.setCellStyle(textStyle);
}

// R15 Col K
Cell R15cell10 = row.createCell(10);
if (record.getR15_fix_depo_1to2() != null) {
    R15cell10.setCellValue(record.getR15_fix_depo_1to2().doubleValue());
    R15cell10.setCellStyle(numberStyle);
} else {
    R15cell10.setCellValue("");
    R15cell10.setCellStyle(textStyle);
}

// R15 Col L
Cell R15cell11 = row.createCell(11);
if (record.getR15_fix_depo_4to6() != null) {
    R15cell11.setCellValue(record.getR15_fix_depo_4to6().doubleValue());
    R15cell11.setCellStyle(numberStyle);
} else {
    R15cell11.setCellValue("");
    R15cell11.setCellStyle(textStyle);
}

// R15 Col M
Cell R15cell12 = row.createCell(12);
if (record.getR15_fix_depo_7to12() != null) {
    R15cell12.setCellValue(record.getR15_fix_depo_7to12().doubleValue());
    R15cell12.setCellStyle(numberStyle);
} else {
    R15cell12.setCellValue("");
    R15cell12.setCellStyle(textStyle);
}

// R15 Col N
Cell R15cell13 = row.createCell(13);
if (record.getR15_fix_depo_13to18()!= null) {
    R15cell13.setCellValue(record.getR15_fix_depo_13to18().doubleValue());
    R15cell13.setCellStyle(numberStyle);
} else {
    R15cell13.setCellValue("");
    R15cell13.setCellStyle(textStyle);
}

// R15 Col O
Cell R15cell14 = row.createCell(14);
if (record.getR15_fix_depo_19to24()!= null) {
    R15cell14.setCellValue(record.getR15_fix_depo_19to24().doubleValue());
    R15cell14.setCellStyle(numberStyle);
} else {
    R15cell14.setCellValue("");
    R15cell14.setCellStyle(textStyle);
}

// R15 Col P
Cell R15cell15 = row.createCell(15);
if (record.getR15_fix_depo_over24() != null) {
    R15cell15.setCellValue(record.getR15_fix_depo_over24().doubleValue());
    R15cell15.setCellStyle(numberStyle);
} else {
    R15cell15.setCellValue("");
    R15cell15.setCellStyle(textStyle);
}


// R15 Col Q
Cell R15cell16 = row.createCell(16);
if (record1.getR15_cer_of_depo() != null) {
    R15cell16.setCellValue(record1.getR15_cer_of_depo().doubleValue());
    R15cell16.setCellStyle(numberStyle);
} else {
    R15cell16.setCellValue("");
    R15cell16.setCellStyle(textStyle);
}

// R15 Col S
Cell R15cell18 = row.createCell(18);
if (record.getR15_pula_equivalent() != null) {
    R15cell18.setCellValue(record.getR15_pula_equivalent().doubleValue());
    R15cell18.setCellStyle(numberStyle);
} else {
    R15cell18.setCellValue("");
    R15cell18.setCellStyle(textStyle);
}
 

// R15 Col T
Cell R15cell19 = row.createCell(19);
if (record.getR15_avg_pula_equivalent() != null) {
    R15cell19.setCellValue(record.getR15_avg_pula_equivalent().doubleValue());
    R15cell19.setCellStyle(numberStyle);
} else {
    R15cell19.setCellValue("");
    R15cell19.setCellStyle(textStyle);
}

// R16 Col B
row = sheet.getRow(15);
Cell R16cell1 = row.createCell(1);
if (record1.getR16_ex_rate_buy() != null) {
    R16cell1.setCellValue(record1.getR16_ex_rate_buy().doubleValue());
    R16cell1.setCellStyle(numberStyle);
} else {
    R16cell1.setCellValue("");
    R16cell1.setCellStyle(textStyle);
}

// R16 Col C
Cell R16cell2 = row.createCell(2);
if (record1.getR16_ex_rate_mid() != null) {
    R16cell2.setCellValue(record1.getR16_ex_rate_mid().doubleValue());
    R16cell2.setCellStyle(numberStyle);
} else {
    R16cell2.setCellValue("");
    R16cell2.setCellStyle(textStyle);
}

// R16 Col D
Cell R16cell3 = row.createCell(3);
if (record1.getR16_ex_rate_sell() != null) {
    R16cell3.setCellValue(record1.getR16_ex_rate_sell().doubleValue());
    R16cell3.setCellStyle(numberStyle);
} else {
    R16cell3.setCellValue("");
    R16cell3.setCellStyle(textStyle);
}

// R16 Col E
Cell R16cell4 = row.createCell(4);
if (record.getR16_current() != null) {
    R16cell4.setCellValue(record.getR16_current().doubleValue());
    R16cell4.setCellStyle(numberStyle);
} else {
    R16cell4.setCellValue("");
    R16cell1.setCellStyle(textStyle);
}

// R16 Col F
Cell R16cell5 = row.createCell(5);
if (record.getR16_call() != null) {
    R16cell5.setCellValue(record.getR16_call().doubleValue());
    R16cell5.setCellStyle(numberStyle);
} else {
    R16cell5.setCellValue("");
    R16cell5.setCellStyle(textStyle);
}

// R16 Col G
Cell R16cell6 = row.createCell(6);
if (record.getR16_savings() != null) {
    R16cell6.setCellValue(record.getR16_savings().doubleValue());
    R16cell6.setCellStyle(numberStyle);
} else {
    R16cell6.setCellValue("");
    R16cell6.setCellStyle(textStyle);
	
}

// R16 Col H
Cell R16cell7 = row.createCell(7);
if (record1.getR16_notice_0to31()!= null) {
    R16cell7.setCellValue(record1.getR16_notice_0to31().doubleValue());
    R16cell7.setCellStyle(numberStyle);
} else {
    R16cell7.setCellValue("");
    R16cell7.setCellStyle(textStyle);
	
}

// R16 Col I
Cell R16cell8= row.createCell(8);
if (record1.getR16_notice_32to88() != null) {
    R16cell8.setCellValue(record1.getR16_notice_32to88().doubleValue());
    R16cell8.setCellStyle(numberStyle);
} else {
    R16cell8.setCellValue("");
    R16cell8.setCellStyle(textStyle);
}

// R16 Col J
Cell R16cell9 = row.createCell(9);
if (record.getR16_fix_depo_91_day_depo() != null) {
    R16cell9.setCellValue(record.getR16_fix_depo_91_day_depo().doubleValue());
    R16cell9.setCellStyle(numberStyle);
} else {
    R16cell9.setCellValue("");
    R16cell9.setCellStyle(textStyle);
}

// R16 Col K
Cell R16cell10 = row.createCell(10);
if (record.getR16_fix_depo_1to2() != null) {
    R16cell10.setCellValue(record.getR16_fix_depo_1to2().doubleValue());
    R16cell10.setCellStyle(numberStyle);
} else {
    R16cell10.setCellValue("");
    R16cell10.setCellStyle(textStyle);
}

// R16 Col L
Cell R16cell11 = row.createCell(11);
if (record.getR16_fix_depo_4to6() != null) {
    R16cell11.setCellValue(record.getR16_fix_depo_4to6().doubleValue());
    R16cell11.setCellStyle(numberStyle);
} else {
    R16cell11.setCellValue("");
    R16cell11.setCellStyle(textStyle);
}

// R16 Col M
Cell R16cell12 = row.createCell(12);
if (record.getR16_fix_depo_7to12() != null) {
    R16cell12.setCellValue(record.getR16_fix_depo_7to12().doubleValue());
    R16cell12.setCellStyle(numberStyle);
} else {
    R16cell12.setCellValue("");
    R16cell12.setCellStyle(textStyle);
}

// R16 Col N
Cell R16cell13 = row.createCell(13);
if (record.getR16_fix_depo_13to18()!= null) {
    R16cell13.setCellValue(record.getR16_fix_depo_13to18().doubleValue());
    R16cell13.setCellStyle(numberStyle);
} else {
    R16cell13.setCellValue("");
    R16cell13.setCellStyle(textStyle);
}

// R16 Col O
Cell R16cell14 = row.createCell(14);
if (record.getR16_fix_depo_19to24()!= null) {
    R16cell14.setCellValue(record.getR16_fix_depo_19to24().doubleValue());
    R16cell14.setCellStyle(numberStyle);
} else {
    R16cell14.setCellValue("");
    R16cell14.setCellStyle(textStyle);
}

// R16 Col P
Cell R16cell15 = row.createCell(15);
if (record.getR16_fix_depo_over24() != null) {
    R16cell15.setCellValue(record.getR16_fix_depo_over24().doubleValue());
    R16cell15.setCellStyle(numberStyle);
} else {
    R16cell15.setCellValue("");
    R16cell15.setCellStyle(textStyle);

}

// R16 Col Q
Cell R16cell16 = row.createCell(16);
if (record1.getR16_cer_of_depo() != null) {
    R16cell16.setCellValue(record1.getR16_cer_of_depo().doubleValue());
    R16cell16.setCellStyle(numberStyle);
} else {
    R16cell16.setCellValue("");
    R16cell16.setCellStyle(textStyle);
}

// R16 Col S
Cell R16cell18 = row.createCell(18);
if (record.getR16_pula_equivalent() != null) {
    R16cell18.setCellValue(record.getR16_pula_equivalent().doubleValue());
    R16cell18.setCellStyle(numberStyle);
} else {
    R16cell18.setCellValue("");
    R16cell18.setCellStyle(textStyle);
}
 

// R16 Col T
Cell R16cell19 = row.createCell(19);
if (record.getR16_avg_pula_equivalent() != null) {
    R16cell19.setCellValue(record.getR16_avg_pula_equivalent().doubleValue());
    R16cell19.setCellStyle(numberStyle);
} else {
    R16cell19.setCellValue("");
    R16cell19.setCellStyle(textStyle);
}

// R17 Col B
row = sheet.getRow(16);
Cell R17cell1 = row.createCell(1);
if (record.getR17_ex_rate_buy() != null) {
    R17cell1.setCellValue(record.getR17_ex_rate_buy().doubleValue());
    R17cell1.setCellStyle(numberStyle);
} else {
    R17cell1.setCellValue("");
    R17cell1.setCellStyle(textStyle);
}

// R17 Col C
Cell R17cell2 = row.createCell(2);
if (record.getR17_ex_rate_mid() != null) {
    R17cell2.setCellValue(record.getR17_ex_rate_mid().doubleValue());
    R17cell2.setCellStyle(numberStyle);
} else {
    R17cell2.setCellValue("");
    R17cell2.setCellStyle(textStyle);
}

// R17 Col D
Cell R17cell3 = row.createCell(3);
if (record.getR17_ex_rate_sell() != null) {
    R17cell3.setCellValue(record.getR17_ex_rate_sell().doubleValue());
    R17cell3.setCellStyle(numberStyle);
} else {
    R17cell3.setCellValue("");
    R17cell3.setCellStyle(textStyle);
}

// R18 Col B
row = sheet.getRow(17);
Cell R18cell1 = row.createCell(1);
if (record.getR18_ex_rate_buy() != null) {
    R18cell1.setCellValue(record.getR18_ex_rate_buy().doubleValue());
    R18cell1.setCellStyle(numberStyle);
} else {
    R18cell1.setCellValue("");
    R18cell1.setCellStyle(textStyle);
}

// R18 Col C
Cell R18cell2 = row.createCell(2);
if (record.getR18_ex_rate_mid() != null) {
    R18cell2.setCellValue(record.getR18_ex_rate_mid().doubleValue());
    R18cell2.setCellStyle(numberStyle);
} else {
    R18cell2.setCellValue("");
    R18cell2.setCellStyle(textStyle);
}

// R18 Col D
Cell R18cell3 = row.createCell(3);
if (record.getR18_ex_rate_sell() != null) {
    R18cell3.setCellValue(record.getR18_ex_rate_sell().doubleValue());
    R18cell3.setCellStyle(numberStyle);
} else {
    R18cell3.setCellValue("");
    R18cell3.setCellStyle(textStyle);
}

// R18 Col E
Cell R18cell4 = row.createCell(4);
if (record.getR18_current() != null) {
    R18cell4.setCellValue(record.getR18_current().doubleValue());
    R18cell4.setCellStyle(numberStyle);
} else {
    R18cell4.setCellValue("");
    R18cell1.setCellStyle(textStyle);
}

// R18 Col F
Cell R18cell5 = row.createCell(5);
if (record.getR18_call() != null) {
    R18cell5.setCellValue(record.getR18_call().doubleValue());
    R18cell5.setCellStyle(numberStyle);
} else {
    R18cell5.setCellValue("");
    R18cell5.setCellStyle(textStyle);
}

// R18 Col G
Cell R18cell6 = row.createCell(6);
if (record.getR18_savings() != null) {
    R18cell6.setCellValue(record.getR18_savings().doubleValue());
    R18cell6.setCellStyle(numberStyle);
} else {
    R18cell6.setCellValue("");
    R18cell6.setCellStyle(textStyle);
	
}

// R18 Col H
Cell R18cell7 = row.createCell(7);
if (record1.getR18_notice_0to31()!= null) {
    R18cell7.setCellValue(record1.getR18_notice_0to31().doubleValue());
    R18cell7.setCellStyle(numberStyle);
} else {
    R18cell7.setCellValue("");
    R18cell7.setCellStyle(textStyle);
	
}

// R18 Col I
Cell R18cell8= row.createCell(8);
if (record1.getR18_notice_32to88() != null) {
    R18cell8.setCellValue(record1.getR18_notice_32to88().doubleValue());
    R18cell8.setCellStyle(numberStyle);
} else {
    R18cell8.setCellValue("");
    R18cell8.setCellStyle(textStyle);
}

// R18 Col J
Cell R18cell9 = row.createCell(9);
if (record.getR18_fix_depo_91_day_depo() != null) {
    R18cell9.setCellValue(record.getR18_fix_depo_91_day_depo().doubleValue());
    R18cell9.setCellStyle(numberStyle);
} else {
    R18cell9.setCellValue("");
    R18cell9.setCellStyle(textStyle);
}

// R18 Col K
Cell R18cell10 = row.createCell(10);
if (record.getR18_fix_depo_1to2() != null) {
    R18cell10.setCellValue(record.getR18_fix_depo_1to2().doubleValue());
    R18cell10.setCellStyle(numberStyle);
} else {
    R18cell10.setCellValue("");
    R18cell10.setCellStyle(textStyle);
}

// R18 Col L
Cell R18cell11 = row.createCell(11);
if (record.getR18_fix_depo_4to6() != null) {
    R18cell11.setCellValue(record.getR18_fix_depo_4to6().doubleValue());
    R18cell11.setCellStyle(numberStyle);
} else {
    R18cell11.setCellValue("");
    R18cell11.setCellStyle(textStyle);
}

// R18 Col M
Cell R18cell12 = row.createCell(12);
if (record.getR18_fix_depo_7to12() != null) {
    R18cell12.setCellValue(record.getR18_fix_depo_7to12().doubleValue());
    R18cell12.setCellStyle(numberStyle);
} else {
    R18cell12.setCellValue("");
    R18cell12.setCellStyle(textStyle);
}

// R18 Col N
Cell R18cell13 = row.createCell(13);
if (record.getR18_fix_depo_13to18()!= null) {
    R18cell13.setCellValue(record.getR18_fix_depo_13to18().doubleValue());
    R18cell13.setCellStyle(numberStyle);
} else {
    R18cell13.setCellValue("");
    R18cell13.setCellStyle(textStyle);
}

// R18 Col O
Cell R18cell14 = row.createCell(14);
if (record.getR18_fix_depo_19to24()!= null) {
    R18cell14.setCellValue(record.getR18_fix_depo_19to24().doubleValue());
    R18cell14.setCellStyle(numberStyle);
} else {
    R18cell14.setCellValue("");
    R18cell14.setCellStyle(textStyle);
}

// R18 Col P
Cell R18cell15 = row.createCell(15);
if (record.getR18_fix_depo_over24() != null) {
    R18cell15.setCellValue(record.getR18_fix_depo_over24().doubleValue());
    R18cell15.setCellStyle(numberStyle);
} else {
    R18cell15.setCellValue("");
    R18cell15.setCellStyle(textStyle);
}


// R18 Col Q
Cell R18cell16 = row.createCell(16);
if (record1.getR18_cer_of_depo() != null) {
    R18cell16.setCellValue(record1.getR18_cer_of_depo().doubleValue());
    R18cell16.setCellStyle(numberStyle);
} else {
    R18cell16.setCellValue("");
    R18cell16.setCellStyle(textStyle);
}
// R18 Col R
Cell R18cell17 = row.createCell(17);
if (record.getR18_total() != null) {
    R18cell17.setCellValue(record.getR18_total().doubleValue());
    R18cell17.setCellStyle(numberStyle);
} else {
    R18cell17.setCellValue("");
    R18cell17.setCellStyle(textStyle);
}
// R18 Col S
Cell R18cell18 = row.createCell(18);
if (record.getR18_pula_equivalent() != null) {
    R18cell18.setCellValue(record.getR18_pula_equivalent().doubleValue());
    R18cell18.setCellStyle(numberStyle);
} else {
    R18cell18.setCellValue("");
    R18cell18.setCellStyle(textStyle);
}
// R18 Col S
Cell R18cell19 = row.createCell(19);
if (record.getR18_avg_pula_equivalent() != null) {
    R18cell19.setCellValue(record.getR18_avg_pula_equivalent().doubleValue());
    R18cell18.setCellStyle(numberStyle);
} else {
    R18cell19.setCellValue("");
    R18cell19.setCellStyle(textStyle);
}

//Entity 2
// R28 Col B
row = sheet.getRow(27);
Cell R28cell1 = row.createCell(1);
if (record1.getR28_import() != null) {
    R28cell1.setCellValue(record1.getR28_import().doubleValue());
    R28cell1.setCellStyle(numberStyle);
} else {
    R28cell1.setCellValue("");
    R28cell1.setCellStyle(textStyle);
}
// R28 Col C
Cell R28cell2 = row.createCell(2);
if (record1.getR28_investment () != null) {
    R28cell2.setCellValue(record1.getR28_investment().doubleValue());
    R28cell2.setCellStyle(numberStyle);
} else {
    R28cell2.setCellValue("");
    R28cell2.setCellStyle(textStyle);
}
// R28 Col D
Cell R28cell3 = row.createCell(3);
if (record1.getR28_other () != null) {
    R28cell3.setCellValue(record1.getR28_other().doubleValue());
    R28cell3.setCellStyle(numberStyle);
} else {
    R28cell3.setCellValue("");
    R28cell3.setCellStyle(textStyle);
}
// R29 Col B
row = sheet.getRow(28);
Cell R29cell1 = row.createCell(1);
if (record1.getR29_import() != null) {
    R29cell1.setCellValue(record1.getR29_import().doubleValue());
    R29cell1.setCellStyle(numberStyle);
} else {
    R29cell1.setCellValue("");
    R29cell1.setCellStyle(textStyle);
}
// R29 Col C
Cell R29cell2 = row.createCell(2);
if (record1.getR29_investment () != null) {
    R29cell2.setCellValue(record1.getR29_investment().doubleValue());
    R29cell2.setCellStyle(numberStyle);
} else {
    R29cell2.setCellValue("");
    R29cell2.setCellStyle(textStyle);
}
// R29 Col D
Cell R29cell3 = row.createCell(3);
if (record1.getR29_other () != null) {
    R29cell3.setCellValue(record1.getR29_other().doubleValue());
    R29cell3.setCellStyle(numberStyle);
} else {
    R29cell3.setCellValue("");
    R29cell3.setCellStyle(textStyle);
}

// R30 Col B
row = sheet.getRow(29);
Cell R30cell1 = row.createCell(1);
if (record1.getR30_import() != null) {
    R30cell1.setCellValue(record1.getR30_import().doubleValue());
    R30cell1.setCellStyle(numberStyle);
} else {
    R30cell1.setCellValue("");
    R30cell1.setCellStyle(textStyle);
}
// R30 Col C
Cell R30cell2 = row.createCell(2);
if (record1.getR30_investment () != null) {
    R30cell2.setCellValue(record1.getR30_investment().doubleValue());
    R30cell2.setCellStyle(numberStyle);
} else {
    R30cell2.setCellValue("");
    R30cell2.setCellStyle(textStyle);
}
// R30 Col D
Cell R30cell3 = row.createCell(3);
if (record1.getR30_other () != null) {
    R30cell3.setCellValue(record1.getR30_other().doubleValue());
    R30cell3.setCellStyle(numberStyle);
} else {
    R30cell3.setCellValue("");
    R30cell3.setCellStyle(textStyle);
}
// R31 Col B
row = sheet.getRow(30);
Cell R31cell1 = row.createCell(1);
if (record1.getR31_import() != null) {
    R31cell1.setCellValue(record1.getR31_import().doubleValue());
    R31cell1.setCellStyle(numberStyle);
} else {
    R31cell1.setCellValue("");
    R31cell1.setCellStyle(textStyle);
}
// R31 Col C
Cell R31cell2 = row.createCell(2);
if (record1.getR31_investment () != null) {
    R31cell2.setCellValue(record1.getR31_investment().doubleValue());
    R31cell2.setCellStyle(numberStyle);
} else {
    R31cell2.setCellValue("");
    R31cell2.setCellStyle(textStyle);
}
// R31 Col D
Cell R31cell3 = row.createCell(3);
if (record1.getR31_other () != null) {
    R31cell3.setCellValue(record1.getR31_other().doubleValue());
    R31cell3.setCellStyle(numberStyle);
} else {
    R31cell3.setCellValue("");
    R31cell3.setCellStyle(textStyle);
}
// R32 Col B
row = sheet.getRow(31);
Cell R32cell1 = row.createCell(1);
if (record1.getR32_import() != null) {
    R32cell1.setCellValue(record1.getR32_import().doubleValue());
    R32cell1.setCellStyle(numberStyle);
} else {
    R32cell1.setCellValue("");
    R32cell1.setCellStyle(textStyle);
}
// R32 Col C
Cell R32cell2 = row.createCell(2);
if (record1.getR32_investment () != null) {
    R32cell2.setCellValue(record1.getR32_investment().doubleValue());
    R32cell2.setCellStyle(numberStyle);
} else {
    R32cell2.setCellValue("");
    R32cell2.setCellStyle(textStyle);
}
// R32 Col D
Cell R32cell3 = row.createCell(3);
if (record1.getR32_other () != null) {
    R32cell3.setCellValue(record1.getR32_other().doubleValue());
    R32cell3.setCellStyle(numberStyle);
} else {
    R32cell3.setCellValue("");
    R32cell3.setCellStyle(textStyle);
}
// R33 Col B
row = sheet.getRow(32);
Cell R33cell1 = row.createCell(1);
if (record1.getR33_import() != null) {
    R33cell1.setCellValue(record1.getR33_import().doubleValue());
    R33cell1.setCellStyle(numberStyle);
} else {
    R33cell1.setCellValue("");
    R33cell1.setCellStyle(textStyle);
}
// R33 Col C
Cell R33cell2 = row.createCell(2);
if (record1.getR33_investment () != null) {
    R33cell2.setCellValue(record1.getR33_investment().doubleValue());
    R33cell2.setCellStyle(numberStyle);
} else {
    R33cell2.setCellValue("");
    R33cell2.setCellStyle(textStyle);
}
// R33 Col D
Cell R33cell3 = row.createCell(3);
if (record1.getR33_other () != null) {
    R33cell3.setCellValue(record1.getR33_other().doubleValue());
    R33cell3.setCellStyle(numberStyle);
} else {
    R33cell3.setCellValue("");
    R33cell3.setCellStyle(textStyle);
}

//Entity 3

row = sheet.getRow(27);
Cell R28cell1e3 = row.createCell(8);
if (record1.getR28_residents () != null) {
    R28cell1e3.setCellValue(record1.getR28_residents().doubleValue());
    R28cell1e3.setCellStyle(numberStyle);
} else {
    R28cell1e3.setCellValue("");
    R28cell1e3.setCellStyle(textStyle);
}
// R28 Col C
Cell R28cell2e3 = row.createCell(9);
if (record1.getR28_non_residents () != null) {
    R28cell2e3.setCellValue(record1.getR28_non_residents().doubleValue());
    R28cell2e3.setCellStyle(numberStyle);
} else {
    R28cell2e3.setCellValue("");
    R28cell2e3.setCellStyle(textStyle);
}
// R29 Col B
row = sheet.getRow(28);
Cell R29cell1e3 = row.createCell(8);
if (record1.getR29_residents () != null) {
    R29cell1e3.setCellValue(record1.getR29_residents().doubleValue());
    R29cell1e3.setCellStyle(numberStyle);
} else {
    R29cell1e3.setCellValue("");
    R29cell1e3.setCellStyle(textStyle);
}
// R29 Col C
Cell R29cell2e3 = row.createCell(9);
if (record1.getR29_non_residents () != null) {
    R29cell2e3.setCellValue(record1.getR29_non_residents().doubleValue());
    R29cell2e3.setCellStyle(numberStyle);
} else {
    R29cell2e3.setCellValue("");
    R29cell2e3.setCellStyle(textStyle);
}
// R30 Col B
row = sheet.getRow(29);
Cell R30cell1e3 = row.createCell(8);
if (record1.getR30_residents () != null) {
    R30cell1e3.setCellValue(record1.getR30_residents().doubleValue());
    R30cell1e3.setCellStyle(numberStyle);
} else {
    R30cell1e3.setCellValue("");
    R30cell1e3.setCellStyle(textStyle);
}
// R30 Col C
Cell R30cell2e3 = row.createCell(9);
if (record1.getR30_non_residents () != null) {
    R30cell2e3.setCellValue(record1.getR30_non_residents().doubleValue());
    R30cell2e3.setCellStyle(numberStyle);
} else {
    R30cell2e3.setCellValue("");
    R30cell2e3.setCellStyle(textStyle);
}
// R31 Col B
row = sheet.getRow(30);
Cell R31cell1e3 = row.createCell(8);
if (record1.getR31_residents () != null) {
    R31cell1e3.setCellValue(record1.getR31_residents().doubleValue());
    R31cell1e3.setCellStyle(numberStyle);
} else {
    R31cell1e3.setCellValue("");
    R31cell1e3.setCellStyle(textStyle);
}
// R31 Col C
Cell R31cell2e3 = row.createCell(9);
if (record1.getR31_non_residents () != null) {
    R31cell2e3.setCellValue(record1.getR31_non_residents().doubleValue());
    R31cell2e3.setCellStyle(numberStyle);
} else {
    R31cell2e3.setCellValue("");
    R31cell2e3.setCellStyle(textStyle);
}

// R32 Col B
row = sheet.getRow(31);
Cell R32cell1e3 = row.createCell(8);
if (record1.getR32_residents () != null) {
    R32cell1e3.setCellValue(record1.getR32_residents().doubleValue());
    R32cell1e3.setCellStyle(numberStyle);
} else {
    R32cell1e3.setCellValue("");
    R32cell1e3.setCellStyle(textStyle);
}
// R32 Col C
Cell R32cell2e3 = row.createCell(9);
if (record1.getR32_non_residents () != null) {
    R32cell2e3.setCellValue(record1.getR32_non_residents().doubleValue());
    R32cell2e3.setCellStyle(numberStyle);
} else {
    R32cell2e3.setCellValue("");
    R32cell2e3.setCellStyle(textStyle);
}
// R33 Col B
row = sheet.getRow(32);
Cell R33cell1e3 = row.createCell(8);
if (record1.getR33_residents () != null) {
    R33cell1e3.setCellValue(record1.getR33_residents().doubleValue());
    R33cell1e3.setCellStyle(numberStyle);
} else {
    R33cell1e3.setCellValue("");
    R33cell1e3.setCellStyle(textStyle);
}
// R33 Col C
Cell R33cell2e3 = row.createCell(9);
if (record1.getR33_non_residents () != null) {
    R33cell2e3.setCellValue(record1.getR33_non_residents().doubleValue());
    R33cell2e3.setCellStyle(numberStyle);
} else {
    R33cell2e3.setCellValue("");
    R33cell2e3.setCellStyle(textStyle);
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
	    ModelAndView mv = new ModelAndView("BRRS/M_DEP3"); // ✅ match the report name
	    System.out.println("Hello");
	    if (acctNo != null) {
	    	M_DEP3_Detail_Entity dep3Entity = BRRS_M_DEP3_Detail_Repo.findByAcctnumber(acctNo);
	        if (dep3Entity != null && dep3Entity.getReportDate() != null) {
	            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(dep3Entity.getReportDate());
	            mv.addObject("asondate", formattedDate);
	        }
	        mv.addObject("Data", dep3Entity);
	    }

	    mv.addObject("displaymode", "edit");
	    mv.addObject("formmode", formMode != null ? formMode : "edit");
	    return mv;
	}





	public ModelAndView updateDetailEdit(String acctNo, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/M_DEP3"); // ✅ match the report name

	    if (acctNo != null) {
	        M_DEP3_Detail_Entity la1Entity = BRRS_M_DEP3_Detail_Repo.findByAcctnumber(acctNo);
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

	        M_DEP3_Detail_Entity existing = BRRS_M_DEP3_Detail_Repo.findByAcctnumber(acctNo);
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
	        	BRRS_M_DEP3_Detail_Repo.save(existing);
	            logger.info("Record updated successfully for account {}", acctNo);

	            // Format date for procedure
	            String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
	                    .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

	            // Run summary procedure after commit
	            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
	                @Override
	                public void afterCommit() {
	                    try {
	                        logger.info("Transaction committed — calling BRRS_M_DEP3_SUMMARY_PROCEDURE({})",
	                                formattedDate);
	                        jdbcTemplate.update("BEGIN BRRS_M_DEP3_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
	        logger.error("Error updating M_DEP3 record", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error updating record: " + e.getMessage());
	    }
	}
	
	
	
	
}