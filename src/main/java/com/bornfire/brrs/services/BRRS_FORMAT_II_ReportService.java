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

import com.bornfire.brrs.entities.BRRS_FORMAT_II_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_FORMAT_II_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_FORMAT_II_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_FORMAT_II_Manual_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_FORMAT_II_Manual_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_FORMAT_II_Summary_Repo;
import com.bornfire.brrs.entities.FORMAT_II_Archival_Detail_Entity;
import com.bornfire.brrs.entities.FORMAT_II_Archival_Manual_Summary_Entity;
import com.bornfire.brrs.entities.FORMAT_II_Archival_Summary_Entity;
import com.bornfire.brrs.entities.FORMAT_II_Detail_Entity;
import com.bornfire.brrs.entities.FORMAT_II_Manual_Summary_Entity;
import com.bornfire.brrs.entities.FORMAT_II_Summary_Entity;
import com.bornfire.brrs.entities.SCH_17_Manual_Summary_Entity;

@Component
@Service

public class BRRS_FORMAT_II_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_FORMAT_II_ReportService.class);

	

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	
	     @Autowired 
	    BRRS_FORMAT_II_Summary_Repo             FORMAT_II_summary_repo;
	 
		@Autowired
		BRRS_FORMAT_II_Archival_Summary_Repo    FORMAT_II_Archival_Summary_Repo;
		
		@Autowired
		BRRS_FORMAT_II_Detail_Repo              FORMAT_II_detail_repo;
		
	    @Autowired 
	    BRRS_FORMAT_II_Archival_Detail_Repo     FORMAT_II_Archival_Detail_Repo;
	 
	    @Autowired 
	    BRRS_FORMAT_II_Manual_Summary_Repo                     FORMAT_II_Manual_summary_repo;
	 
		@Autowired
		BRRS_FORMAT_II_Manual_Archival_Summary_Repo            FORMAT_II_Manual_Archival_Summary_Repo;
		
	
	
	
	

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	
	public ModelAndView getFORMAT_IIView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, String version) {

		ModelAndView mv = new ModelAndView();
		

		System.out.println("testing");
		System.out.println(version);

		if ("ARCHIVAL".equals(type) && version != null && !version.isEmpty()) {

		    System.out.println("ARCHIVAL MODE");
		    System.out.println("version = " + version);

		    List<FORMAT_II_Archival_Summary_Entity> T1Master = new ArrayList<>();
		    List<FORMAT_II_Archival_Manual_Summary_Entity> T2Master = new ArrayList<>();
		 
		    try {
		        Date dt = dateformat.parse(todate);

		        T1Master = FORMAT_II_Archival_Summary_Repo.getdatabydateListarchival(dt, version);

		        System.out.println("T1Master size = " + T1Master.size());
		        
		        T2Master = FORMAT_II_Manual_Archival_Summary_Repo.getdatabydateListarchival(dt, version);

		        System.out.println("T2Master size = " + T2Master.size());
		      

		    } catch (ParseException e) {
		        e.printStackTrace();
		    }

		    mv.addObject("reportsummary", T1Master);
		    mv.addObject("reportsummary1", T2Master);
		 
		} else {

			List<FORMAT_II_Summary_Entity> T1Master = new ArrayList<FORMAT_II_Summary_Entity>();
			List<FORMAT_II_Manual_Summary_Entity> T2Master = new ArrayList<FORMAT_II_Manual_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				
				T1Master = FORMAT_II_summary_repo.getdatabydateList(dateformat.parse(todate));
				
			
				System.out.println("T1Master size " + T1Master.size());
           T2Master = FORMAT_II_Manual_summary_repo.getdatabydateList(dateformat.parse(todate));
				
				System.out.println("T2Master size " + T2Master.size());
				mv.addObject("report_date", dateformat.format(d1));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			
			mv.addObject("reportsummary1", T2Master);
			
			
		}

	

		mv.setViewName("BRRS/FORMAT_II");

		mv.addObject("displaymode", "summary");

		System.out.println("scv" + mv.getViewName());

		return mv;

	}
	 
	
	
	public ModelAndView getFORMAT_IIcurrentDtl(String reportId, String fromdate, String todate, String currency,
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
				List<FORMAT_II_Archival_Detail_Entity> T1Dt1;
				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = FORMAT_II_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1, parsedDate, version);
				} else {
					T1Dt1 = FORMAT_II_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<FORMAT_II_Detail_Entity> T1Dt1;

				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = FORMAT_II_detail_repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1, parsedDate);
				} else {
					T1Dt1 = FORMAT_II_detail_repo.getdatabydateList(parsedDate);
					totalPages = FORMAT_II_detail_repo.getdatacount(parsedDate);
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

	

	
		mv.setViewName("BRRS/FORMAT_II");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}
	
	
	
	

	public byte[] getFORMAT_IIExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelFORMAT_IIARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data

		List<FORMAT_II_Summary_Entity> dataList = FORMAT_II_summary_repo.getdatabydateList(dateformat.parse(todate));
	

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for  FORMAT_II report. Returning empty result.");
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

			int startRow = 12;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					FORMAT_II_Summary_Entity record = dataList.get(i);
				
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

                   // ROW 13
					// Column E - 
					Cell cellE = row.createCell(4);
					if (record.getR13_amt() != null) {
					    cellE.setCellValue(record.getR13_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}
					
					
					
					// Column F - 01.04.2025
					Cell cellF = row.createCell(5);
					if (record.getR13_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR13_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}
					
					
					// Column G - Addition As on  30.06.2025
					Cell cellG = row.createCell(6);
					if (record.getR13_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR13_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}
					
					
					// Column H - Total as at 30.06.2025
					Cell cellH = row.createCell(7);
					if (record.getR13_amt_total() != null) {
						cellH.setCellValue(record.getR13_amt_total().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}
					
					/* ===================== ROW 14 ===================== */
					// Column E
					row = sheet.getRow(13);
					 cellE = row.createCell(4);
					if (record.getR14_amt() != null) {
					    cellE.setCellValue(record.getR14_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// Column F
					 cellF = row.createCell(5);
					if (record.getR14_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR14_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// Column G
					 cellG = row.createCell(6);
					if (record.getR14_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR14_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// Column H
					 cellH = row.createCell(7);
					if (record.getR14_amt_total() != null) {
					    cellH.setCellValue(record.getR14_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 15 ===================== */
					row = sheet.getRow(14);
					Cell cellE15 = row.createCell(4);
					if (record.getR15_amt() != null) {
					    cellE15.setCellValue(record.getR15_amt().doubleValue());
					    cellE15.setCellStyle(numberStyle);
					} else {
					    cellE15.setCellValue("");
					    cellE15.setCellStyle(textStyle);
					}

					Cell cellF15 = row.createCell(5);
					if (record.getR15_amt_sub_add() != null) {
					    cellF15.setCellValue(record.getR15_amt_sub_add().doubleValue());
					    cellF15.setCellStyle(numberStyle);
					} else {
					    cellF15.setCellValue("");
					    cellF15.setCellStyle(textStyle);
					}

					Cell cellG15 = row.createCell(6);
					if (record.getR15_amt_sub_del() != null) {
					    cellG15.setCellValue(record.getR15_amt_sub_del().doubleValue());
					    cellG15.setCellStyle(numberStyle);
					} else {
					    cellG15.setCellValue("");
					    cellG15.setCellStyle(textStyle);
					}

					Cell cellH15 = row.createCell(7);
					if (record.getR15_amt_total() != null) {
					    cellH15.setCellValue(record.getR15_amt_total().doubleValue());
					    cellH15.setCellStyle(numberStyle);
					} else {
					    cellH15.setCellValue("");
					    cellH15.setCellStyle(textStyle);
					}
					
					/* ===================== ROW 16 ===================== */
					row = sheet.getRow(15);
					cellE = row.createCell(4);
					if (record.getR16_amt() != null) {
					    cellE.setCellValue(record.getR16_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR16_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR16_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR16_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR16_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR16_amt_total() != null) {
					    cellH.setCellValue(record.getR16_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 17 ===================== */
					row = sheet.getRow(16);
					cellE = row.createCell(4);
					if (record.getR17_amt() != null) {
					    cellE.setCellValue(record.getR17_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR17_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR17_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR17_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR17_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR17_amt_total() != null) {
					    cellH.setCellValue(record.getR17_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 18 ===================== */
					row = sheet.getRow(17);
					cellE = row.createCell(4);
					if (record.getR18_amt() != null) {
					    cellE.setCellValue(record.getR18_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR18_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR18_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR18_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR18_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR18_amt_total() != null) {
					    cellH.setCellValue(record.getR18_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 19 ===================== */
					row = sheet.getRow(18);
					cellE = row.createCell(4);
					if (record.getR19_amt() != null) {
					    cellE.setCellValue(record.getR19_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR19_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR19_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR19_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR19_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR19_amt_total() != null) {
					    cellH.setCellValue(record.getR19_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 20 ===================== */
					row = sheet.getRow(19);
					cellE = row.createCell(4);
					if (record.getR20_amt() != null) {
					    cellE.setCellValue(record.getR20_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR20_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR20_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR20_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR20_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR20_amt_total() != null) {
					    cellH.setCellValue(record.getR20_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 21 ===================== */
					row = sheet.getRow(20);
					cellE = row.createCell(4);
					if (record.getR21_amt() != null) {
					    cellE.setCellValue(record.getR21_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR21_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR21_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR21_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR21_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR21_amt_total() != null) {
					    cellH.setCellValue(record.getR21_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 22 ===================== */
					row = sheet.getRow(21);
					cellE = row.createCell(4);
					if (record.getR22_amt() != null) {
					    cellE.setCellValue(record.getR22_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR22_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR22_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR22_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR22_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR22_amt_total() != null) {
					    cellH.setCellValue(record.getR22_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 23 ===================== */
					row = sheet.getRow(22);
					cellE = row.createCell(4);
					if (record.getR23_amt() != null) {
					    cellE.setCellValue(record.getR23_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR23_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR23_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR23_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR23_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR23_amt_total() != null) {
					    cellH.setCellValue(record.getR23_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 24 ===================== */
					row = sheet.getRow(23);
					cellE = row.createCell(4);
					if (record.getR24_amt() != null) {
					    cellE.setCellValue(record.getR24_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR24_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR24_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR24_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR24_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR24_amt_total() != null) {
					    cellH.setCellValue(record.getR24_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 25 ===================== */
					row = sheet.getRow(24);
					cellE = row.createCell(4);
					if (record.getR25_amt() != null) {
					    cellE.setCellValue(record.getR25_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR25_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR25_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR25_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR25_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR25_amt_total() != null) {
					    cellH.setCellValue(record.getR25_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 26 ===================== */
					row = sheet.getRow(25);
					cellE = row.createCell(4);
					if (record.getR26_amt() != null) {
					    cellE.setCellValue(record.getR26_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR26_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR26_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR26_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR26_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR26_amt_total() != null) {
					    cellH.setCellValue(record.getR26_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 27 ===================== */
					row = sheet.getRow(26);
					cellE = row.createCell(4);
					if (record.getR27_amt() != null) {
					    cellE.setCellValue(record.getR27_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR27_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR27_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR27_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR27_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR27_amt_total() != null) {
					    cellH.setCellValue(record.getR27_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


					/* ===================== ROW 28 ===================== */
					row = sheet.getRow(27);
					cellE = row.createCell(4);
					if (record.getR28_amt() != null) {
					    cellE.setCellValue(record.getR28_amt().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					cellF = row.createCell(5);
					if (record.getR28_amt_sub_add() != null) {
					    cellF.setCellValue(record.getR28_amt_sub_add().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					cellG = row.createCell(6);
					if (record.getR28_amt_sub_del() != null) {
					    cellG.setCellValue(record.getR28_amt_sub_del().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR28_amt_total() != null) {
					    cellH.setCellValue(record.getR28_amt_total().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}


				

					
				
				}

				
			} else {

			}

// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}

	
	public byte[] getExcelFORMAT_IIARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}
		
		List<FORMAT_II_Archival_Summary_Entity> dataList = FORMAT_II_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
	

		

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for FORMAT_II report. Returning empty result.");
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

			int startRow = 12;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					FORMAT_II_Archival_Summary_Entity record = dataList.get(i);
				
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}


				     // ROW 13
									// Column E - 
									Cell cellE = row.createCell(4);
									if (record.getR13_amt() != null) {
									    cellE.setCellValue(record.getR13_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}
									
									
									
									// Column F - 01.04.2025
									Cell cellF = row.createCell(5);
									if (record.getR13_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR13_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}
									
									
									// Column G - Addition As on  30.06.2025
									Cell cellG = row.createCell(6);
									if (record.getR13_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR13_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}
									
									
									// Column H - Total as at 30.06.2025
									Cell cellH = row.createCell(7);
									if (record.getR13_amt_total() != null) {
										cellH.setCellValue(record.getR13_amt_total().doubleValue());
										cellH.setCellStyle(numberStyle);
									} else {
										cellH.setCellValue("");
										cellH.setCellStyle(textStyle);
									}
									
									/* ===================== ROW 14 ===================== */
									// Column E
									row = sheet.getRow(13);
									 cellE = row.createCell(4);
									if (record.getR14_amt() != null) {
									    cellE.setCellValue(record.getR14_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									// Column F
									 cellF = row.createCell(5);
									if (record.getR14_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR14_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									// Column G
									 cellG = row.createCell(6);
									if (record.getR14_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR14_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									// Column H
									 cellH = row.createCell(7);
									if (record.getR14_amt_total() != null) {
									    cellH.setCellValue(record.getR14_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 15 ===================== */
									row = sheet.getRow(14);
									Cell cellE15 = row.createCell(4);
									if (record.getR15_amt() != null) {
									    cellE15.setCellValue(record.getR15_amt().doubleValue());
									    cellE15.setCellStyle(numberStyle);
									} else {
									    cellE15.setCellValue("");
									    cellE15.setCellStyle(textStyle);
									}

									Cell cellF15 = row.createCell(5);
									if (record.getR15_amt_sub_add() != null) {
									    cellF15.setCellValue(record.getR15_amt_sub_add().doubleValue());
									    cellF15.setCellStyle(numberStyle);
									} else {
									    cellF15.setCellValue("");
									    cellF15.setCellStyle(textStyle);
									}

									Cell cellG15 = row.createCell(6);
									if (record.getR15_amt_sub_del() != null) {
									    cellG15.setCellValue(record.getR15_amt_sub_del().doubleValue());
									    cellG15.setCellStyle(numberStyle);
									} else {
									    cellG15.setCellValue("");
									    cellG15.setCellStyle(textStyle);
									}

									Cell cellH15 = row.createCell(7);
									if (record.getR15_amt_total() != null) {
									    cellH15.setCellValue(record.getR15_amt_total().doubleValue());
									    cellH15.setCellStyle(numberStyle);
									} else {
									    cellH15.setCellValue("");
									    cellH15.setCellStyle(textStyle);
									}
									
									/* ===================== ROW 16 ===================== */
									row = sheet.getRow(15);
									cellE = row.createCell(4);
									if (record.getR16_amt() != null) {
									    cellE.setCellValue(record.getR16_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR16_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR16_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR16_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR16_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR16_amt_total() != null) {
									    cellH.setCellValue(record.getR16_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 17 ===================== */
									row = sheet.getRow(16);
									cellE = row.createCell(4);
									if (record.getR17_amt() != null) {
									    cellE.setCellValue(record.getR17_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR17_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR17_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR17_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR17_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR17_amt_total() != null) {
									    cellH.setCellValue(record.getR17_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 18 ===================== */
									row = sheet.getRow(17);
									cellE = row.createCell(4);
									if (record.getR18_amt() != null) {
									    cellE.setCellValue(record.getR18_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR18_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR18_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR18_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR18_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR18_amt_total() != null) {
									    cellH.setCellValue(record.getR18_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 19 ===================== */
									row = sheet.getRow(18);
									cellE = row.createCell(4);
									if (record.getR19_amt() != null) {
									    cellE.setCellValue(record.getR19_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR19_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR19_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR19_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR19_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR19_amt_total() != null) {
									    cellH.setCellValue(record.getR19_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 20 ===================== */
									row = sheet.getRow(19);
									cellE = row.createCell(4);
									if (record.getR20_amt() != null) {
									    cellE.setCellValue(record.getR20_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR20_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR20_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR20_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR20_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR20_amt_total() != null) {
									    cellH.setCellValue(record.getR20_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 21 ===================== */
									row = sheet.getRow(20);
									cellE = row.createCell(4);
									if (record.getR21_amt() != null) {
									    cellE.setCellValue(record.getR21_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR21_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR21_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR21_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR21_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR21_amt_total() != null) {
									    cellH.setCellValue(record.getR21_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 22 ===================== */
									row = sheet.getRow(21);
									cellE = row.createCell(4);
									if (record.getR22_amt() != null) {
									    cellE.setCellValue(record.getR22_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR22_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR22_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR22_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR22_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR22_amt_total() != null) {
									    cellH.setCellValue(record.getR22_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 23 ===================== */
									row = sheet.getRow(22);
									cellE = row.createCell(4);
									if (record.getR23_amt() != null) {
									    cellE.setCellValue(record.getR23_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR23_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR23_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR23_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR23_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR23_amt_total() != null) {
									    cellH.setCellValue(record.getR23_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 24 ===================== */
									row = sheet.getRow(23);
									cellE = row.createCell(4);
									if (record.getR24_amt() != null) {
									    cellE.setCellValue(record.getR24_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR24_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR24_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR24_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR24_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR24_amt_total() != null) {
									    cellH.setCellValue(record.getR24_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 25 ===================== */
									row = sheet.getRow(24);
									cellE = row.createCell(4);
									if (record.getR25_amt() != null) {
									    cellE.setCellValue(record.getR25_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR25_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR25_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR25_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR25_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR25_amt_total() != null) {
									    cellH.setCellValue(record.getR25_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 26 ===================== */
									row = sheet.getRow(25);
									cellE = row.createCell(4);
									if (record.getR26_amt() != null) {
									    cellE.setCellValue(record.getR26_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR26_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR26_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR26_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR26_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR26_amt_total() != null) {
									    cellH.setCellValue(record.getR26_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 27 ===================== */
									row = sheet.getRow(26);
									cellE = row.createCell(4);
									if (record.getR27_amt() != null) {
									    cellE.setCellValue(record.getR27_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR27_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR27_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR27_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR27_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR27_amt_total() != null) {
									    cellH.setCellValue(record.getR27_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


									/* ===================== ROW 28 ===================== */
									row = sheet.getRow(27);
									cellE = row.createCell(4);
									if (record.getR28_amt() != null) {
									    cellE.setCellValue(record.getR28_amt().doubleValue());
									    cellE.setCellStyle(numberStyle);
									} else {
									    cellE.setCellValue("");
									    cellE.setCellStyle(textStyle);
									}

									cellF = row.createCell(5);
									if (record.getR28_amt_sub_add() != null) {
									    cellF.setCellValue(record.getR28_amt_sub_add().doubleValue());
									    cellF.setCellStyle(numberStyle);
									} else {
									    cellF.setCellValue("");
									    cellF.setCellStyle(textStyle);
									}

									cellG = row.createCell(6);
									if (record.getR28_amt_sub_del() != null) {
									    cellG.setCellValue(record.getR28_amt_sub_del().doubleValue());
									    cellG.setCellStyle(numberStyle);
									} else {
									    cellG.setCellValue("");
									    cellG.setCellStyle(textStyle);
									}

									cellH = row.createCell(7);
									if (record.getR28_amt_total() != null) {
									    cellH.setCellValue(record.getR28_amt_total().doubleValue());
									    cellH.setCellStyle(numberStyle);
									} else {
									    cellH.setCellValue("");
									    cellH.setCellStyle(textStyle);
									}


   
					
					
				}

				
			} else {

			}

// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}
	
	

	
	
	
	
	
	
	
	
	
	
	public List<Object> getFORMAT_IIArchival() {
		List<Object>FORMAT_IIArchivallist = new ArrayList<>();
		try {
			FORMAT_IIArchivallist = FORMAT_II_Archival_Summary_Repo.getFORMAT_IIarchival();
		
			System.out.println("countser" + FORMAT_IIArchivallist.size());
			
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching FORMAT_IIArchivallist Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return FORMAT_IIArchivallist;
	}
	
	
	
	public byte[] getFORMAT_IIDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for  FORMAT_II Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getFORMAT_IIDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("FORMAT_II Details");

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
			String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

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
			List<FORMAT_II_Detail_Entity> reportData = FORMAT_II_detail_repo.getdatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (FORMAT_II_Detail_Entity item : reportData) {
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
				logger.info("No data found for FORMAT_II — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating FORMAT_II Excel", e);
			return new byte[0];
		}
	}
	
	
	
	
	
	
	
	public byte[] getFORMAT_IIDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for FORMAT_II ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("FORMAT_II Detail");

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
			String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE",  "REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

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
			List<FORMAT_II_Archival_Detail_Entity> reportData =FORMAT_II_Archival_Detail_Repo.getdatabydateList(parsedToDate,
					version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (FORMAT_II_Archival_Detail_Entity item : reportData) {
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
				logger.info("No data found for FORMAT_II — only header will be written.");
			}

// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating FORMAT_II Excel", e);
			return new byte[0];
		}
	}
	
	
	
	
	 @Autowired BRRS_FORMAT_II_Detail_Repo BRRS_FORMAT_II_detail_repo;
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/FORMAT_II"); 

		if (acctNo != null) {
			FORMAT_II_Detail_Entity formate_IIEntity = BRRS_FORMAT_II_detail_repo.findByAcctnumber(acctNo);
			if (formate_IIEntity != null && formate_IIEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(formate_IIEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("formate_IIData", formate_IIEntity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String acctBalanceInpula = request.getParameter("acctBalanceInpula");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			FORMAT_II_Detail_Entity existing = BRRS_FORMAT_II_detail_repo.findByAcctnumber(acctNo);
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
				BRRS_FORMAT_II_detail_repo.save(existing);
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_FORMAT_II_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_FORMAT_II_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating FORMAT_II record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}
	
	
	public void updateReport(FORMAT_II_Manual_Summary_Entity updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    //  Use your query to fetch by date
	    List<FORMAT_II_Manual_Summary_Entity> list = FORMAT_II_Manual_summary_repo
	        .getdatabydateList(updatedEntity.getReport_date());

	    FORMAT_II_Manual_Summary_Entity existing;
	    if (list.isEmpty()) {
	        // Record not found — optionally create it
	        System.out.println("No record found for REPORT_DATE: " + updatedEntity.getReport_date());
	        existing = new FORMAT_II_Manual_Summary_Entity();
	        existing.setReport_date(updatedEntity.getReport_date());
	    } else {
	        existing = list.get(0);
	    }

	    try {
	        //  Only for specific row numbers
	    	int[] rows = {21, 25};

	    	for (int row : rows) {
	    	    String prefix = "R" + row + "_";

	    	    String[] fields;

	    	    if (row == 21) {
	    	        fields = new String[] { "amt" };             // R21_amt only
	    	    } else if (row == 25) {
	    	        fields = new String[] { "amt_sub_del" };     // R25_amt_sub_del only
	    	    } else {
	    	        continue;
	    	    }

	            for (String field : fields) {
	                String getterName = "get" + prefix + field; 
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = FORMAT_II_Manual_Summary_Entity.class.getMethod(getterName);
	                    Method setter = FORMAT_II_Manual_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

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
	        throw new RuntimeException("Error while updating FORMAT_II Summary fields", e);
	    }

	    //  FIRST COMMIT — forces immediate commit
	    FORMAT_II_Manual_summary_repo.saveAndFlush(existing);
	    System.out.println("FORMAT_II Summary updated and COMMITTED");

	    //  Execute procedure with updated data
	    String oracleDate = new SimpleDateFormat("dd-MM-yyyy")
	            .format(updatedEntity.getReport_date())
	            .toUpperCase();

	    String sql = "BEGIN BRRS.BRRS_FORMAT_II_SUMMARY_PROCEDURE ('" + oracleDate + "'); END;";
	    jdbcTemplate.execute(sql);

	    System.out.println("Procedure executed for date: " + oracleDate);
	}

	
	

}