package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
/*import org.apache.poi.ss.usermodel.FillPatternType;*/
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
/*import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;*/
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

import com.bornfire.brrs.entities.BRRS_SCH_17_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_SCH_17_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_SCH_17_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_SCH_17_Manual_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_SCH_17_Manual_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_SCH_17_Summary_Repo;
import com.bornfire.brrs.entities.SCH_17_Archival_Detail_Entity;
import com.bornfire.brrs.entities.SCH_17_Archival_Manual_Summary_Entity;
import com.bornfire.brrs.entities.SCH_17_Archival_Summary_Entity;
import com.bornfire.brrs.entities.SCH_17_Detail_Entity;
import com.bornfire.brrs.entities.SCH_17_Manual_Summary_Entity;
import com.bornfire.brrs.entities.SCH_17_Summary_Entity;

@Component
@Service

public class BRRS_SCH_17_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_SCH_17_ReportService.class);

	

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	 @Autowired
	    private BRRS_SCH_17_Detail_Repo repo;

	    @Autowired 
	    BRRS_SCH_17_Summary_Repo                     SCH_17_summary_repo;
	 
		@Autowired
		BRRS_SCH_17_Archival_Summary_Repo            SCH_17_Archival_Summary_Repo;
		
		@Autowired
		BRRS_SCH_17_Detail_Repo                      SCH_17_detail_repo;
		
	    @Autowired 
	    BRRS_SCH_17_Archival_Detail_Repo             SCH_17_Archival_Detail_Repo;
	    
	    
	    @Autowired 
	    BRRS_SCH_17_Manual_Summary_Repo                     SCH_17_Manual_summary_repo;
	 
		@Autowired
		BRRS_SCH_17_Manual_Archival_Summary_Repo            SCH_17_Manual_Archival_Summary_Repo;
		
	  
	 

	
	
	
	

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	
	public ModelAndView getSCH_17View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, String version) {

		ModelAndView mv = new ModelAndView();
		

		System.out.println("testing");
		System.out.println(version);

		if ("ARCHIVAL".equals(type) && version != null && !version.isEmpty()) {

		    System.out.println("ARCHIVAL MODE");
		    System.out.println("version = " + version);

		    List<SCH_17_Archival_Summary_Entity> T1Master = new ArrayList<>();
		    List<SCH_17_Archival_Manual_Summary_Entity> T2Master = new ArrayList<>();
		 
		    try {
		        Date dt = dateformat.parse(todate);

		        T1Master = SCH_17_Archival_Summary_Repo.getdatabydateListarchival(dt, version);

		        System.out.println("T1Master size = " + T1Master.size());
		      
		        
		        T2Master = SCH_17_Manual_Archival_Summary_Repo.getdatabydateListarchival(dt, version);

		        System.out.println("T2Master size = " + T2Master.size());

		    } catch (ParseException e) {
		        e.printStackTrace();
		    }

		    mv.addObject("reportsummary", T1Master);
		    mv.addObject("reportsummary1", T2Master);
		 
		} else {

			List<SCH_17_Summary_Entity> T1Master = new ArrayList<SCH_17_Summary_Entity>();
			List<SCH_17_Manual_Summary_Entity> T2Master = new ArrayList<SCH_17_Manual_Summary_Entity>();
		
			try {
				Date d1 = dateformat.parse(todate);
				
				T1Master = SCH_17_summary_repo.getdatabydateList(dateformat.parse(todate));
			
				System.out.println("T1Master size " + T1Master.size());
				T2Master = SCH_17_Manual_summary_repo.getdatabydateList(dateformat.parse(todate));
				
				System.out.println("T2Master size " + T2Master.size());
				
				mv.addObject("report_date", dateformat.format(d1));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			 mv.addObject("reportsummary1", T2Master);
			
		}

	

		mv.setViewName("BRRS/SCH_17");

		mv.addObject("displaymode", "summary");

		System.out.println("scv" + mv.getViewName());

		return mv;

	}
	 
	
	
	public ModelAndView getSCH_17currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {

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

			String reportLable = null;
			String reportAddlCriteria_1 = null;
			// ✅ Split filter string into rowId & columnId
			if (filter != null && filter.contains(",")) {
				String[] parts = filter.split(",");
				if (parts.length >= 2) {
					reportLable = parts[0];
					reportAddlCriteria_1 = parts[1];
				}
			}

			System.out.println(type);
			if ("ARCHIVAL".equals(type) && version != null) {
				System.out.println(type);
				// 🔹 Archival branch
				List<SCH_17_Archival_Detail_Entity> T1Dt1;
				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = SCH_17_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1, parsedDate, version);
				} else {
					T1Dt1 = SCH_17_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<SCH_17_Detail_Entity> T1Dt1;

				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = SCH_17_detail_repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1, parsedDate);
				} else {
					T1Dt1 = SCH_17_detail_repo.getdatabydateList(parsedDate);
					totalPages = SCH_17_detail_repo.getdatacount(parsedDate);
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

	

	
		mv.setViewName("BRRS/SCH_17");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}
	
	
	
	

	public byte[] getSCH_17Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.sch17");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelSCH_17ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data

		List<SCH_17_Summary_Entity> dataList = SCH_17_summary_repo.getdatabydateList(dateformat.parse(todate));
	

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for  SCH_17 report. Returning empty result.");
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

			int startRow = 8;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					SCH_17_Summary_Entity record = dataList.get(i);
				
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

  
					

			
					// Column C
					Cell cellC = row.createCell(2);
					if (record.getR9_31_3_25_amt() != null) {
					    cellC.setCellValue(record.getR9_31_3_25_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue(0);   // IMPORTANT
					    cellC.setCellStyle(numberStyle);
					}

					// Column D
					Cell cellD = row.createCell(3);
					if (record.getR9_30_9_25_amt() != null) {
					    cellD.setCellValue(record.getR9_30_9_25_amt().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue(0);   // IMPORTANT
					    cellD.setCellStyle(numberStyle);
					}


					// R10
					row = sheet.getRow(9);
				   cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR10_31_3_25_amt() != null ? record.getR10_31_3_25_amt().doubleValue() : 0);
					
					// R10
				
				   cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					cellD.setCellValue(record.getR10_30_9_25_amt() != null ? record.getR10_30_9_25_amt().doubleValue() : 0);


				
					// R11
					row = sheet.getRow(10);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR11_31_3_25_amt() != null ? record.getR11_31_3_25_amt().doubleValue() : 0);

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					cellD.setCellValue(record.getR11_30_9_25_amt() != null ? record.getR11_30_9_25_amt().doubleValue() : 0);


					// R12
					row = sheet.getRow(11);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR12_31_3_25_amt() != null ? record.getR12_31_3_25_amt().doubleValue() : 0);

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					cellD.setCellValue(record.getR12_30_9_25_amt() != null ? record.getR12_30_9_25_amt().doubleValue() : 0);


					// R13
					row = sheet.getRow(12);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR13_31_3_25_amt() != null ? record.getR13_31_3_25_amt().doubleValue() : 0);

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					cellD.setCellValue(record.getR13_30_9_25_amt() != null ? record.getR13_30_9_25_amt().doubleValue() : 0);


					// R14
					row = sheet.getRow(13);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR14_31_3_25_amt() != null ? record.getR14_31_3_25_amt().doubleValue() : 0);

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					cellD.setCellValue(record.getR14_30_9_25_amt() != null ? record.getR14_30_9_25_amt().doubleValue() : 0);


					// R15
					row = sheet.getRow(14);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR15_31_3_25_amt() != null ? record.getR15_31_3_25_amt().doubleValue() : 0);

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					cellD.setCellValue(record.getR15_30_9_25_amt() != null ? record.getR15_30_9_25_amt().doubleValue() : 0);


					// R16
					row = sheet.getRow(15);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR16_31_3_25_amt() != null ? record.getR16_31_3_25_amt().doubleValue() : 0);

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					cellD.setCellValue(record.getR16_30_9_25_amt() != null ? record.getR16_30_9_25_amt().doubleValue() : 0);

					// R17
					row = sheet.getRow(16);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR17_31_3_25_amt() != null ? record.getR17_31_3_25_amt().doubleValue() : 0);

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					cellD.setCellValue(record.getR17_30_9_25_amt() != null ? record.getR17_30_9_25_amt().doubleValue() : 0);
				

					// R18
					row = sheet.getRow(17);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR18_31_3_25_amt() != null ? record.getR18_31_3_25_amt().doubleValue() : 0);

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					cellD.setCellValue(record.getR18_30_9_25_amt() != null ? record.getR18_30_9_25_amt().doubleValue() : 0);


					// R19
					row = sheet.getRow(18);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR19_31_3_25_amt() != null ? record.getR19_31_3_25_amt().doubleValue() : 0);

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					cellD.setCellValue(record.getR19_30_9_25_amt() != null ? record.getR19_30_9_25_amt().doubleValue() : 0);
					
					
					// R20
					row = sheet.getRow(19);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR20_31_3_25_amt() != null ? record.getR20_31_3_25_amt().doubleValue() : 0);

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					cellD.setCellValue(record.getR20_30_9_25_amt() != null ? record.getR20_30_9_25_amt().doubleValue() : 0);
					
				
				
				}
				
				/* workbook.getCreationHelper().createFormulaEvaluator().evaluateAll(); */

				
			} else {

			}

// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}

	
	public byte[] getExcelSCH_17ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}
		
		List<SCH_17_Archival_Summary_Entity> dataList = SCH_17_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
	
       List<SCH_17_Archival_Manual_Summary_Entity> dataList1 = SCH_17_Manual_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for SCH_17 report. Returning empty result.");
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

			int startRow = 8;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					SCH_17_Archival_Summary_Entity record = dataList.get(i);
				    SCH_17_Archival_Manual_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}


						// Column C
					Cell cellC = row.createCell(2);
					if (record.getR9_31_3_25_amt() != null) {
					    cellC.setCellValue(record.getR9_31_3_25_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue(0);   // IMPORTANT
					    cellC.setCellStyle(numberStyle);
					}

					// Column D
					Cell cellD = row.createCell(3);
					if (record.getR9_30_9_25_amt() != null) {
					    cellD.setCellValue(record.getR9_30_9_25_amt().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue(0);   // IMPORTANT
					    cellD.setCellStyle(numberStyle);
					}


					// R10
					row = sheet.getRow(9);
				   cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record1.getR10_31_3_25_amt() != null ? record1.getR10_31_3_25_amt().doubleValue() : 0);
					
					// R10
				
				   cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					cellD.setCellValue(record1.getR10_30_9_25_amt() != null ? record1.getR10_30_9_25_amt().doubleValue() : 0);


				
					// R11
					row = sheet.getRow(10);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record1.getR11_31_3_25_amt() != null ? record1.getR11_31_3_25_amt().doubleValue() : 0);

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					cellD.setCellValue(record1.getR11_30_9_25_amt() != null ? record1.getR11_30_9_25_amt().doubleValue() : 0);


					// R12
					row = sheet.getRow(11);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record1.getR12_31_3_25_amt() != null ? record1.getR12_31_3_25_amt().doubleValue() : 0);

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					cellD.setCellValue(record1.getR12_30_9_25_amt() != null ? record1.getR12_30_9_25_amt().doubleValue() : 0);


					// R13
					row = sheet.getRow(12);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record.getR13_31_3_25_amt() != null ? record.getR13_31_3_25_amt().doubleValue() : 0);

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					cellD.setCellValue(record.getR13_30_9_25_amt() != null ? record.getR13_30_9_25_amt().doubleValue() : 0);


					// R14
					row = sheet.getRow(13);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record1.getR14_31_3_25_amt() != null ? record1.getR14_31_3_25_amt().doubleValue() : 0);

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					cellD.setCellValue(record1.getR14_30_9_25_amt() != null ? record1.getR14_30_9_25_amt().doubleValue() : 0);


					// R15
					row = sheet.getRow(14);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record1.getR15_31_3_25_amt() != null ? record1.getR15_31_3_25_amt().doubleValue() : 0);

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					cellD.setCellValue(record1.getR15_30_9_25_amt() != null ? record1.getR15_30_9_25_amt().doubleValue() : 0);


					// R16
					row = sheet.getRow(15);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record1.getR16_31_3_25_amt() != null ? record1.getR16_31_3_25_amt().doubleValue() : 0);

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					cellD.setCellValue(record1.getR16_30_9_25_amt() != null ? record1.getR16_30_9_25_amt().doubleValue() : 0);

					// R17
					row = sheet.getRow(16);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record1.getR17_31_3_25_amt() != null ? record1.getR17_31_3_25_amt().doubleValue() : 0);

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					cellD.setCellValue(record1.getR17_30_9_25_amt() != null ? record1.getR17_30_9_25_amt().doubleValue() : 0);
				

					// R18
					row = sheet.getRow(17);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record1.getR18_31_3_25_amt() != null ? record1.getR18_31_3_25_amt().doubleValue() : 0);

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					cellD.setCellValue(record1.getR18_30_9_25_amt() != null ? record1.getR18_30_9_25_amt().doubleValue() : 0);


					// R19
					row = sheet.getRow(18);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record1.getR19_31_3_25_amt() != null ? record1.getR19_31_3_25_amt().doubleValue() : 0);

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					cellD.setCellValue(record1.getR19_30_9_25_amt() != null ? record1.getR19_30_9_25_amt().doubleValue() : 0);
					
					
					// R20
					row = sheet.getRow(19);
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					cellC.setCellValue(record1.getR20_31_3_25_amt() != null ? record1.getR20_31_3_25_amt().doubleValue() : 0);

					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					cellD.setCellValue(record1.getR20_30_9_25_amt() != null ? record1.getR20_30_9_25_amt().doubleValue() : 0);
					
					
				}

				
			} else {

			}

// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}
	
	

	
	
	
	
	
	
	
	
	
	
	public List<Object> getSCH_17Archival() {
		List<Object>SCH_17Archivallist = new ArrayList<>();
		try {
			SCH_17Archivallist = SCH_17_Archival_Summary_Repo.getSCH_17archival();
		
			System.out.println("countser" + SCH_17Archivallist.size());
			
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching SCH_17Archivallist Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return SCH_17Archivallist;
	}
	
	
	
	public byte[] getSCH_17DetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for  SCH_17 Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getSCH_17DetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("SCH_17 Details");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT LABEL", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

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
			List<SCH_17_Detail_Entity> reportData = SCH_17_detail_repo.getdatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (SCH_17_Detail_Entity item : reportData) { 
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());
					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					

					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria_1());
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
				logger.info("No data found for SCH_17 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating SCH_17 Excel", e);
			return new byte[0];
		}
	}
	
	
	
	
	
	
	
	public byte[] getSCH_17DetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for SCH_17 ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("SCH_17 Detail");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

// Header row
			String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE",  "REPORT LABEL", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

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
			List<SCH_17_Archival_Detail_Entity> reportData = SCH_17_Archival_Detail_Repo.getdatabydateList(parsedToDate,
					version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (SCH_17_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					 row.createCell(2).setCellValue(item.getAcctName()); 

// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					
					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria_1());
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
				logger.info("No data found for SCH_17 — only header will be written.");
			}

// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating SCH_17 Excel", e);
			return new byte[0];
		}
	}
	
	
	
	
	 @Autowired BRRS_SCH_17_Detail_Repo BRRS_SCH_17_detail_repo;
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String SNO, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/SCH_17"); 

		System.out.println("sno is : "+ SNO);
		if (SNO != null) {
			SCH_17_Detail_Entity sch_17Entity = BRRS_SCH_17_detail_repo.findBySno(SNO);
			if (sch_17Entity != null && sch_17Entity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(sch_17Entity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("sch_17Data", sch_17Entity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String Sno = request.getParameter("sno");
			String acctBalanceInpula = request.getParameter("acctBalanceInpula");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			System.out.println("Sno is : "+ Sno);
			
			SCH_17_Detail_Entity existing = BRRS_SCH_17_detail_repo.findBySno(Sno);
			if (existing == null) {
				logger.warn("No record found for Sno: {}", Sno);
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

			 if (acctBalanceInpula != null && !acctBalanceInpula.isEmpty()) {
		            BigDecimal newacctBalanceInpula = new BigDecimal(acctBalanceInpula);
		            if (existing.getAcctBalanceInpula()  == null ||
		                existing.getAcctBalanceInpula().compareTo(newacctBalanceInpula) != 0) {
		            	 existing.setAcctBalanceInpula(newacctBalanceInpula);
		                isChanged = true;
		                logger.info("Balance updated to {}", newacctBalanceInpula);
		            }
		        }
		        
			if (isChanged) {
				BRRS_SCH_17_detail_repo.save(existing);
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_SCH_17_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_SCH_17_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating SCH_17 record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}
	
/*	public void updateReport(SCH_17_Manual_Summary_Entity updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    //  Use your query to fetch by date
	    List<SCH_17_Manual_Summary_Entity> list = SCH_17_Manual_summary_repo
	        .getdatabydateList(updatedEntity.getReport_date());

	    SCH_17_Manual_Summary_Entity existing;
	    if (list.isEmpty()) {
	        // Record not found — optionally create it
	        System.out.println("No record found for REPORT_DATE: " + updatedEntity.getReport_date());
	        existing = new SCH_17_Manual_Summary_Entity();
	        existing.setReport_date(updatedEntity.getReport_date());
	    } else {
	        existing = list.get(0);
	    }

	    try {
	        //  Only for specific row numbers
	        int[] rows = {10, 11, 12, /*13 excluded 14, 15, 16, 17, 18, 19, 20};

	        for (int row : rows) {
	            String prefix = "R" + row + "_";

	            // Fields to update
	            String[] fields = { "product",
	            	    "31_3_25_amt",
	            	    "30_9_25_amt"};

	            for (String field : fields) {
	                String getterName = "get" + prefix + field; 
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = SCH_17_Manual_Summary_Entity.class.getMethod(getterName);
	                    Method setter = SCH_17_Manual_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields gracefully
	                    continue;
	                }
	            }
	        }

	        // Metadata
	        existing.setReport_version(updatedEntity.getReport_version());
	        existing.setReport_frequency(updatedEntity.getReport_frequency());
	        existing.setReport_code(updatedEntity.getReport_code());
	        existing.setReport_desc(updatedEntity.getReport_desc());
	        existing.setEntity_flg(updatedEntity.getEntity_flg());
	        existing.setModify_flg(updatedEntity.getModify_flg());
	        existing.setDel_flg(updatedEntity.getDel_flg());

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating SCH_17 Summary fields", e);
	    }

	    //  FIRST COMMIT — forces immediate commit
	    SCH_17_Manual_summary_repo.saveAndFlush(existing);
	    System.out.println("SCH_17 Summary updated and COMMITTED");

	    //  Execute procedure with updated data
	    String oracleDate = new SimpleDateFormat("dd-MM-yyyy")
	            .format(updatedEntity.getReport_date())
	            .toUpperCase();

	    //String sql = "BEGIN BRRS.BRRS_SCH_17_SUMMARY_PROCEDURE ('" + oracleDate + "'); END;";
	    //jdbcTemplate.execute(sql);

	    System.out.println("Procedure executed for date: " + oracleDate);
	}
*/

	    public void updateDetailFromForm(Date reportDate, Map<String, String> params) {

	        System.out.println("Updating SCH-17 detail table");

	        for (Map.Entry<String, String> entry : params.entrySet()) {

	            String key = entry.getKey();
	            String value = entry.getValue();

	            // Expected: R10_C2_r10_31_3_25_amt
	            if (!key.matches("R\\d+_C\\d+_.*")) {
	                continue;
	            }

	            String[] parts = key.split("_");
	            String reportLabel = parts[0];       // R10
	            String addlCriteria = parts[1];      // C2 or C3

	            BigDecimal amount =
	                    (value == null || value.isEmpty())
	                            ? BigDecimal.ZERO
	                            : new BigDecimal(value);

	            List<SCH_17_Detail_Entity> rows =
	                    repo.findByReportDateAndReportLableAndReportAddlCriteria1(
	                            reportDate, reportLabel, addlCriteria
	                    );

	            System.out.println("Rows fetching for Reportdate is : "+reportDate +" Report label is : " +reportLabel +" Column is : "+addlCriteria);
	           System.out.println("data size is : "+rows.size());
	          
	           for (SCH_17_Detail_Entity row : rows) {

	        	    //System.out.println("Row PK = " + row.getId());
	        	    System.out.println("Before update acct = " + row.getAcctBalanceInpula());
	        	    System.out.println("Before update modifyFlg = " + row.getModifyFlg());

	        	    row.setAcctBalanceInpula(amount);
	        	    row.setModifyFlg('Y');
	        	}

	            

	            repo.saveAll(rows);
	        }

	        callSummaryProcedure(reportDate);
	    }

	    private void callSummaryProcedure(Date reportDate) {

	        String sql = "{ call BRRS_SCH_17_SUMMARY_PROCEDURE(?) }";

	        jdbcTemplate.update(connection -> {
	            CallableStatement cs = connection.prepareCall(sql);

	            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	            sdf.setLenient(false);

	            cs.setString(1, sdf.format(reportDate));
	            return cs;
	        });

	        System.out.println("✅ SCH-17 Summary procedure executed");
	    }
	    
	    
	}

	

