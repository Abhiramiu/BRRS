package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;


import com.bornfire.brrs.entities.BRRS_Q_SMME_DEP_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_Q_SMME_DEP_Summary_Repo;
import com.bornfire.brrs.entities.Q_RLFA2_Archival_Summary_Entity;
import com.bornfire.brrs.entities.Q_RLFA2_Summary_Entity;
import com.bornfire.brrs.entities.Q_SMME_DEP_Archival_Summary_Entity;
import com.bornfire.brrs.entities.Q_SMME_DEP_Summary_Entity;



@Component
@Service
public class BRRS_Q_SMME_DEP_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_Q_SMME_DEP_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;



	

	@Autowired
	BRRS_Q_SMME_DEP_Summary_Repo BRRS_Q_SMME_DEP_Summary_Repo;

	

	@Autowired
	BRRS_Q_SMME_DEP_Archival_Summary_Repo BRRS_Q_SMME_DEP_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	/*
	 * public ModelAndView getQ_SMME_DEPview(String reportId, String fromdate,
	 * String todate, String currency, String dtltype, Pageable pageable, String
	 * type, String version) { ModelAndView mv = new ModelAndView(); Session hs =
	 * sessionFactory.getCurrentSession(); int pageSize = pageable.getPageSize();
	 * int currentPage = pageable.getPageNumber(); int startItem = currentPage *
	 * pageSize;
	 * 
	 * System.out.println("testing"); System.out.println(version);
	 * 
	 * if (type.equals("ARCHIVAL") & version != null) { System.out.println(type);
	 * List<Q_SMME_DEP_Archival_Summary_Entity> T1Master = new
	 * ArrayList<Q_SMME_DEP_Archival_Summary_Entity>(); System.out.println(version);
	 * try { Date d1 = dateformat.parse(todate);
	 * 
	 * // T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date =
	 * ?1 // ", BRF1_REPORT_ENTITY.class) // .setParameter(1,
	 * df.parse(todate)).getResultList(); T1Master =
	 * BRRS_Q_SMME_DEP_Archival_Summary_Repo.getdatabydateListarchival(dateformat.
	 * parse(todate), version);
	 * 
	 * } catch (ParseException e) { e.printStackTrace(); }
	 * 
	 * mv.addObject("reportsummary", T1Master); } else {
	 * List<Q_SMME_DEP_Summary_Entity> T1Master = new
	 * ArrayList<Q_SMME_DEP_Summary_Entity>(); try { Date d1 =
	 * dateformat.parse(todate);
	 * 
	 * // T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date =
	 * ?1 // ", BRF1_REPORT_ENTITY.class) // .setParameter(1,
	 * df.parse(todate)).getResultList(); T1Master =
	 * BRRS_Q_SMME_DEP_Summary_Repo.getdatabydateList(dateformat.parse(todate));
	 * 
	 * } catch (ParseException e) { e.printStackTrace(); }
	 * mv.addObject("reportsummary", T1Master); }
	 * 
	 * // T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
	 * mv.setViewName("BRRS/Q_SMME_DEP"); mv.addObject("displaymode", "summary");
	 * System.out.println("scv" + mv.getViewName()); return mv; }
	 */

	
	 public ModelAndView getQ_SMME_DEPview(String reportId, String fromdate, String
			  todate, String currency, String dtltype, Pageable pageable, String type,
			  String version) {
			  
			  ModelAndView mv = new ModelAndView(); Session hs =
			  sessionFactory.getCurrentSession();
			  
			  int pageSize = pageable.getPageSize(); int currentPage =
			  pageable.getPageNumber(); int startItem = currentPage * pageSize;
			  
			  try { Date d1 = dateformat.parse(todate);
			  
			  // ---------- CASE 1: ARCHIVAL ---------- 
			  if
			  ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			  List<Q_SMME_DEP_Archival_Summary_Entity> T1Master =
					  BRRS_Q_SMME_DEP_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
			  
			  mv.addObject("reportsummary", T1Master); }
			  
			  // ---------- CASE 2: RESUB ---------- 
			  else if
			  ("RESUB".equalsIgnoreCase(type) && version != null) {
			  List<Q_SMME_DEP_Archival_Summary_Entity> T1Master =
					  BRRS_Q_SMME_DEP_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
			  
			  mv.addObject("reportsummary", T1Master); }
			  
			  // ---------- CASE 3: NORMAL ---------- 
			  else { List<Q_SMME_DEP_Summary_Entity>
			  T1Master =
					  BRRS_Q_SMME_DEP_Summary_Repo.getdatabydateListWithVersion(todate);
			  System.out.println("T1Master Size "+T1Master.size());
			  mv.addObject("reportsummary", T1Master); }
			  
			  } catch (ParseException e) { e.printStackTrace(); }
			  
			  mv.setViewName("BRRS/Q_SMME_DEP"); mv.addObject("displaymode", "summary");
			  System.out.println("View set to: " + mv.getViewName()); return mv; }

	 
	

	
	
	
	
	
	
	
	
	/*
	 * public ModelAndView getQ_SMME_DEPcurrentDtl(String reportId, String fromdate,
	 * String todate, String currency, String dtltype, Pageable pageable, String
	 * Filter, String type, String version) {
	 * 
	 * int pageSize = pageable != null ? pageable.getPageSize() : 10; int
	 * currentPage = pageable != null ? pageable.getPageNumber() : 0; int totalPages
	 * = 0;
	 * 
	 * ModelAndView mv = new ModelAndView(); Session hs =
	 * sessionFactory.getCurrentSession();
	 * 
	 * try { Date parsedDate = null; if (todate != null && !todate.isEmpty()) {
	 * parsedDate = dateformat.parse(todate); }
	 * 
	 * String rowId = null; String columnId = null;
	 * 
	 * // ✅ Split filter string into rowId & columnId if (Filter != null &&
	 * Filter.contains(",")) { String[] parts = Filter.split(","); if (parts.length
	 * >= 2) { rowId = parts[0]; columnId = parts[1]; } } System.out.println(type);
	 * if ("ARCHIVAL".equals(type) && version != null) { System.out.println(type);
	 * // 🔹 Archival branch List<BRRS_Q_SMME_DEP_Archival_Detail_Entity> T1Dt1; if
	 * (rowId != null && columnId != null) { T1Dt1 =
	 * BRRS_Q_SMME_DEP_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId,
	 * columnId, parsedDate, version); } else { T1Dt1 =
	 * BRRS_Q_SMME_DEP_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
	 * }
	 * 
	 * mv.addObject("reportdetails", T1Dt1); mv.addObject("reportmaster12", T1Dt1);
	 * System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
	 * 
	 * } else { // 🔹 Current branch List<BRRS_Q_SMME_DEP_DETAIL_Entity> T1Dt1; if
	 * (rowId != null && columnId != null) { T1Dt1 =
	 * BRRS_Q_SMME_DEP_DETAIL_Repo.GetDataByRowIdAndColumnId(rowId, columnId,
	 * parsedDate); } else { T1Dt1 =
	 * BRRS_Q_SMME_DEP_DETAIL_Repo.getdatabydateList(parsedDate, currentPage,
	 * pageSize); totalPages = BRRS_Q_SMME_DEP_DETAIL_Repo.getdatacount(parsedDate);
	 * mv.addObject("pagination", "YES"); }
	 * 
	 * mv.addObject("reportdetails", T1Dt1); mv.addObject("reportmaster12", T1Dt1);
	 * System.out.println("LISTCOUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0)); }
	 * 
	 * } catch (ParseException e) { e.printStackTrace();
	 * mv.addObject("errorMessage", "Invalid date format: " + todate); } catch
	 * (Exception e) { e.printStackTrace(); mv.addObject("errorMessage",
	 * "Unexpected error: " + e.getMessage()); }
	 * 
	 * // ✅ Common attributes mv.setViewName("BRRS/Q_SMME_DEP");
	 * mv.addObject("displaymode", "Details"); mv.addObject("currentPage",
	 * currentPage); System.out.println("totalPages: " + (int) Math.ceil((double)
	 * totalPages / 100)); mv.addObject("totalPages", (int) Math.ceil((double)
	 * totalPages / 100)); mv.addObject("reportsflag", "reportsflag");
	 * mv.addObject("menu", reportId);
	 * 
	 * return mv; }
	 */


	public byte[] getQ_SMME_DEPExcel(String filename, String reportId, String fromdate, String todate, String currency,
									 String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelQ_SMME_DEPARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}
		
		else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
		    logger.info("Service: Generating RESUB report for version {}", version);

		    try {
		        // ✅ Use pattern matching "31-Jul-2025"
		        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
		        Date report_date = sdf.parse(fromdate);  // or use asondate if that's your date source

		        List<Q_SMME_DEP_Archival_Summary_Entity> T1Master =
		        		BRRS_Q_SMME_DEP_Archival_Summary_Repo.getdatabydateListarchival(report_date, version);

		        // ✅ Generate Excel
		        return BRRS_Q_SMME_DEPResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

		    } catch (ParseException e) {
		        logger.error("Invalid report date format: {}", fromdate, e);
		        throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
		    }
		}
		
		

		// Fetch data
		List<Q_SMME_DEP_Summary_Entity> dataList = BRRS_Q_SMME_DEP_Summary_Repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Q_SMME_DEP report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(filename);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}

		if (!Files.isReadable(templatePath)) {
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
					Q_SMME_DEP_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					
					
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
			
					

					//ROW11
					//COLUMN2
					Cell cell2 = row.createCell(1);
					if (record.getR11_CURRENT() != null) {
						cell2.setCellValue(record.getR11_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					//row11
					// Column3
					Cell cell3 = row.createCell(2);
					if (record.getR11_CALL() != null) {
						cell3.setCellValue(record.getR11_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row11
					// Column4
					Cell cell4 = row.createCell(3);
					if (record.getR11_SAVINGS() != null) {
						cell4.setCellValue(record.getR11_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					//row11
					// Column5
					Cell cell5 = row.createCell(4);
					if (record.getR11_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR11_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					//row11
					// Column6
					Cell cell6 = row.createCell(5);
					if (record.getR11_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR11_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					//row11
					// Column7
					Cell cell7 = row.createCell(6);
					if (record.getR11_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR11_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					//row11
					// Column8
					Cell cell8 = row.createCell(7);
					if (record.getR11_1_2M_FD() != null) {
						cell8.setCellValue(record.getR11_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					//row11
					// Column9
					Cell cell9 = row.createCell(8);
					if (record.getR11_4_6M_FD() != null) {
						cell9.setCellValue(record.getR11_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					//row11
					// Column10
					Cell cell10 = row.createCell(9);
					if (record.getR11_7_12M_FD() != null) {
						cell10.setCellValue(record.getR11_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					//row11
					// Column11
					Cell cell11 = row.createCell(10);
					if (record.getR11_13_18M_FD() != null) {
						cell11.setCellValue(record.getR11_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row11
					// Column12
					Cell cell12 = row.createCell(11);
					if (record.getR11_19_24M_FD() != null) {
						cell12.setCellValue(record.getR11_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					
					//row11
					// Column13
					Cell cell13 = row.createCell(12);
					if (record.getR11_OVER24_FD() != null) {
						cell13.setCellValue(record.getR11_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					//row11
					// Column14
//					Cell cell14 = row.createCell(13);
//					if (record.getR11_TOTAL() != null) {
//						cell14.setCellValue(record.getR11_TOTAL().doubleValue());
//						cell14.setCellStyle(numberStyle);
//					} else {
//						cell14.setCellValue("");
//						cell14.setCellStyle(textStyle);
//					}
					
					//row11
					// Column15
					Cell cell15 = row.createCell(14);
					if (record.getR11_NOACC() != null) {
						cell15.setCellValue(record.getR11_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}
		
					row = sheet.getRow(11);
				
					
					
					// Row 12
					// Column2
					cell2 = row.createCell(1);
					if (record.getR12_CURRENT() != null) {
					    cell2.setCellValue(record.getR12_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR12_CALL() != null) {
					    cell3.setCellValue(record.getR12_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR12_SAVINGS() != null) {
					    cell4.setCellValue(record.getR12_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR12_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR12_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR12_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR12_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR12_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR12_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR12_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR12_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR12_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR12_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR12_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR12_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR12_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR12_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR12_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR12_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR12_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR12_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR12_TOTAL() != null) {
//					    cell14.setCellValue(record.getR12_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR12_NOACC() != null) {
					    cell15.setCellValue(record.getR12_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					
					
					// Row 13
					// Column2
					cell2 = row.createCell(1);
					if (record.getR13_CURRENT() != null) {
					    cell2.setCellValue(record.getR13_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR13_CALL() != null) {
					    cell3.setCellValue(record.getR13_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR13_SAVINGS() != null) {
					    cell4.setCellValue(record.getR13_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR13_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR13_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR13_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR13_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR13_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR13_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR13_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR13_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR13_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR13_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR13_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR13_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR13_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR13_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR13_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR13_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR13_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR13_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR13_TOTAL() != null) {
//					    cell14.setCellValue(record.getR13_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR13_NOACC() != null) {
					    cell15.setCellValue(record.getR13_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);

					
					// Row 14
					// Column2
					cell2 = row.createCell(1);
					if (record.getR14_CURRENT() != null) {
					    cell2.setCellValue(record.getR14_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR14_CALL() != null) {
					    cell3.setCellValue(record.getR14_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR14_SAVINGS() != null) {
					    cell4.setCellValue(record.getR14_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR14_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR14_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR14_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR14_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR14_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR14_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR14_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR14_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR14_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR14_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR14_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR14_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR14_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR14_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR14_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR14_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR14_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR14_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR14_TOTAL() != null) {
//					    cell14.setCellValue(record.getR14_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR14_NOACC() != null) {
					    cell15.setCellValue(record.getR14_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 14
					row = sheet.getRow(14);

					
					// Row 15
					// Column2
					cell2 = row.createCell(1);
					if (record.getR15_CURRENT() != null) {
					    cell2.setCellValue(record.getR15_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR15_CALL() != null) {
					    cell3.setCellValue(record.getR15_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR15_SAVINGS() != null) {
					    cell4.setCellValue(record.getR15_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR15_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR15_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR15_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR15_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR15_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR15_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR15_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR15_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR15_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR15_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR15_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR15_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR15_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR15_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR15_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR15_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR15_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR15_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR15_TOTAL() != null) {
//					    cell14.setCellValue(record.getR15_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR15_NOACC() != null) {
					    cell15.setCellValue(record.getR15_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 15
					row = sheet.getRow(15);
					
					
					
					// Row 16
					// Column2
					cell2 = row.createCell(1);
					if (record.getR16_CURRENT() != null) {
					    cell2.setCellValue(record.getR16_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR16_CALL() != null) {
					    cell3.setCellValue(record.getR16_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR16_SAVINGS() != null) {
					    cell4.setCellValue(record.getR16_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR16_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR16_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR16_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR16_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR16_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR16_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR16_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR16_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR16_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR16_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR16_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR16_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR16_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR16_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR16_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR16_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR16_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR16_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR16_TOTAL() != null) {
//					    cell14.setCellValue(record.getR16_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR16_NOACC() != null) {
					    cell15.setCellValue(record.getR16_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 16
					row = sheet.getRow(16);

					
					// Row 17
					// Column2
					cell2 = row.createCell(1);
					if (record.getR17_CURRENT() != null) {
					    cell2.setCellValue(record.getR17_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR17_CALL() != null) {
					    cell3.setCellValue(record.getR17_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR17_SAVINGS() != null) {
					    cell4.setCellValue(record.getR17_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR17_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR17_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR17_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR17_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR17_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR17_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR17_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR17_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR17_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR17_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR17_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR17_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR17_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR17_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR17_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR17_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR17_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR17_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR17_TOTAL() != null) {
//					    cell14.setCellValue(record.getR17_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR17_NOACC() != null) {
					    cell15.setCellValue(record.getR17_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 17
					row = sheet.getRow(17);

					
					// Row 18
					// Column2
					cell2 = row.createCell(1);
					if (record.getR18_CURRENT() != null) {
					    cell2.setCellValue(record.getR18_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR18_CALL() != null) {
					    cell3.setCellValue(record.getR18_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR18_SAVINGS() != null) {
					    cell4.setCellValue(record.getR18_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR18_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR18_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR18_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR18_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR18_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR18_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR18_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR18_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR18_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR18_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR18_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR18_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR18_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR18_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR18_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR18_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR18_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR18_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR18_TOTAL() != null) {
//					    cell14.setCellValue(record.getR18_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR18_NOACC() != null) {
					    cell15.setCellValue(record.getR18_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 18
					row = sheet.getRow(18);

					
					
					// Row 19
					// Column2
					cell2 = row.createCell(1);
					if (record.getR19_CURRENT() != null) {
					    cell2.setCellValue(record.getR19_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR19_CALL() != null) {
					    cell3.setCellValue(record.getR19_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR19_SAVINGS() != null) {
					    cell4.setCellValue(record.getR19_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR19_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR19_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR19_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR19_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR19_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR19_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR19_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR19_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR19_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR19_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR19_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR19_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR19_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR19_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR19_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR19_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR19_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR19_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR19_TOTAL() != null) {
//					    cell14.setCellValue(record.getR19_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR19_NOACC() != null) {
					    cell15.setCellValue(record.getR19_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 19
					row = sheet.getRow(19);

					
					// Row 20
					// Column2
					cell2 = row.createCell(1);
					if (record.getR20_CURRENT() != null) {
					    cell2.setCellValue(record.getR20_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR20_CALL() != null) {
					    cell3.setCellValue(record.getR20_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR20_SAVINGS() != null) {
					    cell4.setCellValue(record.getR20_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR20_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR20_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR20_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR20_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR20_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR20_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR20_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR20_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR20_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR20_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR20_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR20_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR20_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR20_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR20_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR20_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR20_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR20_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR20_TOTAL() != null) {
//					    cell14.setCellValue(record.getR20_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR20_NOACC() != null) {
					    cell15.setCellValue(record.getR20_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 20
					row = sheet.getRow(20);

					
					// Row 21
					// Column2
					cell2 = row.createCell(1);
					if (record.getR21_CURRENT() != null) {
					    cell2.setCellValue(record.getR21_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR21_CALL() != null) {
					    cell3.setCellValue(record.getR21_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR21_SAVINGS() != null) {
					    cell4.setCellValue(record.getR21_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR21_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR21_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR21_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR21_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR21_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR21_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR21_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR21_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR21_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR21_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR21_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR21_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR21_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR21_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR21_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR21_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR21_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR21_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR21_TOTAL() != null) {
//					    cell14.setCellValue(record.getR21_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR21_NOACC() != null) {
					    cell15.setCellValue(record.getR21_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 21
					row = sheet.getRow(21);

					// Row 22
					// Column2
					cell2 = row.createCell(1);
					if (record.getR22_CURRENT() != null) {
					    cell2.setCellValue(record.getR22_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR22_CALL() != null) {
					    cell3.setCellValue(record.getR22_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR22_SAVINGS() != null) {
					    cell4.setCellValue(record.getR22_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR22_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR22_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR22_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR22_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR22_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR22_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR22_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR22_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR22_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR22_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR22_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR22_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR22_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR22_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR22_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR22_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR22_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR22_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR22_TOTAL() != null) {
//					    cell14.setCellValue(record.getR22_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR22_NOACC() != null) {
					    cell15.setCellValue(record.getR22_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 22
					row = sheet.getRow(22);
					
					
					// Row 23
					// Column2
					cell2 = row.createCell(1);
					if (record.getR23_CURRENT() != null) {
					    cell2.setCellValue(record.getR23_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR23_CALL() != null) {
					    cell3.setCellValue(record.getR23_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR23_SAVINGS() != null) {
					    cell4.setCellValue(record.getR23_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR23_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR23_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR23_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR23_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR23_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR23_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR23_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR23_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR23_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR23_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR23_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR23_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR23_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR23_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR23_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR23_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR23_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR23_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR23_TOTAL() != null) {
//					    cell14.setCellValue(record.getR23_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR23_NOACC() != null) {
					    cell15.setCellValue(record.getR23_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 23
					row = sheet.getRow(23);


					// Row 24
					// Column2
					cell2 = row.createCell(1);
					if (record.getR24_CURRENT() != null) {
					    cell2.setCellValue(record.getR24_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR24_CALL() != null) {
					    cell3.setCellValue(record.getR24_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR24_SAVINGS() != null) {
					    cell4.setCellValue(record.getR24_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR24_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR24_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR24_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR24_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR24_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR24_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR24_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR24_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR24_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR24_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR24_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR24_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR24_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR24_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR24_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR24_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR24_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR24_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR24_TOTAL() != null) {
//					    cell14.setCellValue(record.getR24_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR24_NOACC() != null) {
					    cell15.setCellValue(record.getR24_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 24
					row = sheet.getRow(24);


					// Row 25
					// Column2
//					cell2 = row.createCell(1);
//					if (record.getR25_CURRENT() != null) {
//					    cell2.setCellValue(record.getR25_CURRENT().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}
//
//					// Column3
//					cell3 = row.createCell(2);
//					if (record.getR25_CALL() != null) {
//					    cell3.setCellValue(record.getR25_CALL().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//
//					// Column4
//					cell4 = row.createCell(3);
//					if (record.getR25_SAVINGS() != null) {
//					    cell4.setCellValue(record.getR25_SAVINGS().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
//
//					// Column5
//					cell5 = row.createCell(4);
//					if (record.getR25_0_31D_NOTICE() != null) {
//					    cell5.setCellValue(record.getR25_0_31D_NOTICE().doubleValue());
//					    cell5.setCellStyle(numberStyle);
//					} else {
//					    cell5.setCellValue("");
//					    cell5.setCellStyle(textStyle);
//					}
//
//					// Column6
//					cell6 = row.createCell(5);
//					if (record.getR25_32_88D_NOTICE() != null) {
//					    cell6.setCellValue(record.getR25_32_88D_NOTICE().doubleValue());
//					    cell6.setCellStyle(numberStyle);
//					} else {
//					    cell6.setCellValue("");
//					    cell6.setCellStyle(textStyle);
//					}
//
//					// Column7
//					cell7 = row.createCell(6);
//					if (record.getR25_91D_DEPOSIT() != null) {
//					    cell7.setCellValue(record.getR25_91D_DEPOSIT().doubleValue());
//					    cell7.setCellStyle(numberStyle);
//					} else {
//					    cell7.setCellValue("");
//					    cell7.setCellStyle(textStyle);
//					}
//
//					// Column8
//					cell8 = row.createCell(7);
//					if (record.getR25_1_2M_FD() != null) {
//					    cell8.setCellValue(record.getR25_1_2M_FD().doubleValue());
//					    cell8.setCellStyle(numberStyle);
//					} else {
//					    cell8.setCellValue("");
//					    cell8.setCellStyle(textStyle);
//					}
//
//					// Column9
//					cell9 = row.createCell(8);
//					if (record.getR25_4_6M_FD() != null) {
//					    cell9.setCellValue(record.getR25_4_6M_FD().doubleValue());
//					    cell9.setCellStyle(numberStyle);
//					} else {
//					    cell9.setCellValue("");
//					    cell9.setCellStyle(textStyle);
//					}
//
//					// Column10
//					cell10 = row.createCell(9);
//					if (record.getR25_7_12M_FD() != null) {
//					    cell10.setCellValue(record.getR25_7_12M_FD().doubleValue());
//					    cell10.setCellStyle(numberStyle);
//					} else {
//					    cell10.setCellValue("");
//					    cell10.setCellStyle(textStyle);
//					}
//
//					// Column11
//					cell11 = row.createCell(10);
//					if (record.getR25_13_18M_FD() != null) {
//					    cell11.setCellValue(record.getR25_13_18M_FD().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}
//
//					// Column12
//					cell12 = row.createCell(11);
//					if (record.getR25_19_24M_FD() != null) {
//					    cell12.setCellValue(record.getR25_19_24M_FD().doubleValue());
//					    cell12.setCellStyle(numberStyle);
//					} else {
//					    cell12.setCellValue("");
//					    cell12.setCellStyle(textStyle);
//					}
//
//					// Column13
//					cell13 = row.createCell(12);
//					if (record.getR25_OVER24_FD() != null) {
//					    cell13.setCellValue(record.getR25_OVER24_FD().doubleValue());
//					    cell13.setCellStyle(numberStyle);
//					} else {
//					    cell13.setCellValue("");
//					    cell13.setCellStyle(textStyle);
//					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR25_TOTAL() != null) {
//					    cell14.setCellValue(record.getR25_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
//					cell15 = row.createCell(14);
//					if (record.getR25_NOACC() != null) {
//					    cell15.setCellValue(record.getR25_NOACC().doubleValue());
//					    cell15.setCellStyle(numberStyle);
//					} else {
//					    cell15.setCellValue("");
//					    cell15.setCellStyle(textStyle);
//					}

					// Assign Row 25
					row = sheet.getRow(25);



					// Row 26
					// Column2
					cell2 = row.createCell(1);
					if (record.getR26_CURRENT() != null) {
					    cell2.setCellValue(record.getR26_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR26_CALL() != null) {
					    cell3.setCellValue(record.getR26_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR26_SAVINGS() != null) {
					    cell4.setCellValue(record.getR26_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR26_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR26_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR26_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR26_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR26_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR26_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR26_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR26_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR26_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR26_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR26_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR26_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR26_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR26_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR26_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR26_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR26_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR26_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR26_TOTAL() != null) {
//					    cell14.setCellValue(record.getR26_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR26_NOACC() != null) {
					    cell15.setCellValue(record.getR26_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 26
					row = sheet.getRow(31);

					
					
					// Row 32
					// Column2
					cell2 = row.createCell(1);
					if (record.getR32_CURRENT() != null) {
						System.out.println(record.getR32_CURRENT());
					    cell2.setCellValue(record.getR32_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR32_CALL() != null) {
					    cell3.setCellValue(record.getR32_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR32_SAVINGS() != null) {
					    cell4.setCellValue(record.getR32_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR32_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR32_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR32_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR32_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR32_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR32_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR32_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR32_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR32_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR32_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR32_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR32_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR32_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR32_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR32_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR32_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR32_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR32_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR32_TOTAL() != null) {
//					    cell14.setCellValue(record.getR32_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR32_NOACC() != null) {
					    cell15.setCellValue(record.getR32_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 32
					row = sheet.getRow(32);


					// Row 33
					// Column2
					cell2 = row.createCell(1);
					if (record.getR33_CURRENT() != null) {
					    cell2.setCellValue(record.getR33_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR33_CALL() != null) {
					    cell3.setCellValue(record.getR33_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR33_SAVINGS() != null) {
					    cell4.setCellValue(record.getR33_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR33_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR33_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR33_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR33_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR33_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR33_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR33_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR33_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR33_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR33_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR33_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR33_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR33_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR33_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR33_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR33_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR33_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR33_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR33_TOTAL() != null) {
//					    cell14.setCellValue(record.getR33_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR33_NOACC() != null) {
					    cell15.setCellValue(record.getR33_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 33
					row = sheet.getRow(33);


					// Row 34
					// Column2
					cell2 = row.createCell(1);
					if (record.getR34_CURRENT() != null) {
					    cell2.setCellValue(record.getR34_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR34_CALL() != null) {
					    cell3.setCellValue(record.getR34_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR34_SAVINGS() != null) {
					    cell4.setCellValue(record.getR34_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR34_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR34_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR34_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR34_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR34_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR34_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR34_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR34_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR34_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR34_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR34_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR34_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR34_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR34_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR34_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR34_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR34_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR34_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR34_TOTAL() != null) {
//					    cell14.setCellValue(record.getR34_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR34_NOACC() != null) {
					    cell15.setCellValue(record.getR34_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 34
					row = sheet.getRow(34);

					
					// Row 35
					// Column2
					cell2 = row.createCell(1);
					if (record.getR35_CURRENT() != null) {
					    cell2.setCellValue(record.getR35_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR35_CALL() != null) {
					    cell3.setCellValue(record.getR35_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR35_SAVINGS() != null) {
					    cell4.setCellValue(record.getR35_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR35_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR35_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR35_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR35_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR35_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR35_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR35_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR35_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR35_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR35_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR35_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR35_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR35_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR35_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR35_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR35_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR35_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR35_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR35_TOTAL() != null) {
//					    cell14.setCellValue(record.getR35_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR35_NOACC() != null) {
					    cell15.setCellValue(record.getR35_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 35
					row = sheet.getRow(35);


					// Row 36
					// Column2
					cell2 = row.createCell(1);
					if (record.getR36_CURRENT() != null) {
					    cell2.setCellValue(record.getR36_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR36_CALL() != null) {
					    cell3.setCellValue(record.getR36_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR36_SAVINGS() != null) {
					    cell4.setCellValue(record.getR36_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR36_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR36_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR36_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR36_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR36_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR36_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR36_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR36_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR36_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR36_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR36_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR36_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR36_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR36_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR36_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR36_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR36_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR36_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR36_TOTAL() != null) {
//					    cell14.setCellValue(record.getR36_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR36_NOACC() != null) {
					    cell15.setCellValue(record.getR36_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 36
					row = sheet.getRow(36);


					// Row 37
					// Column2
					cell2 = row.createCell(1);
					if (record.getR37_CURRENT() != null) {
					    cell2.setCellValue(record.getR37_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR37_CALL() != null) {
					    cell3.setCellValue(record.getR37_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR37_SAVINGS() != null) {
					    cell4.setCellValue(record.getR37_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR37_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR37_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR37_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR37_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR37_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR37_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR37_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR37_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR37_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR37_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR37_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR37_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR37_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR37_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR37_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR37_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR37_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR37_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR37_TOTAL() != null) {
//					    cell14.setCellValue(record.getR37_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR37_NOACC() != null) {
					    cell15.setCellValue(record.getR37_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 37
					row = sheet.getRow(37);



					// Row 38
					// Column2
					cell2 = row.createCell(1);
					if (record.getR38_CURRENT() != null) {
					    cell2.setCellValue(record.getR38_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR38_CALL() != null) {
					    cell3.setCellValue(record.getR38_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR38_SAVINGS() != null) {
					    cell4.setCellValue(record.getR38_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR38_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR38_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR38_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR38_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR38_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR38_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR38_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR38_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR38_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR38_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR38_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR38_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR38_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR38_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR38_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR38_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR38_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR38_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR38_TOTAL() != null) {
//					    cell14.setCellValue(record.getR38_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR38_NOACC() != null) {
					    cell15.setCellValue(record.getR38_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 38
					row = sheet.getRow(38);


					// Row 39
					// Column2
					cell2 = row.createCell(1);
					if (record.getR39_CURRENT() != null) {
					    cell2.setCellValue(record.getR39_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR39_CALL() != null) {
					    cell3.setCellValue(record.getR39_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR39_SAVINGS() != null) {
					    cell4.setCellValue(record.getR39_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR39_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR39_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR39_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR39_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR39_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR39_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR39_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR39_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR39_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR39_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR39_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR39_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR39_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR39_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR39_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR39_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR39_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR39_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR39_TOTAL() != null) {
//					    cell14.setCellValue(record.getR39_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR39_NOACC() != null) {
					    cell15.setCellValue(record.getR39_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 39
					row = sheet.getRow(39);


					// Row 40
					// Column2
					cell2 = row.createCell(1);
					if (record.getR40_CURRENT() != null) {
					    cell2.setCellValue(record.getR40_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR40_CALL() != null) {
					    cell3.setCellValue(record.getR40_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR40_SAVINGS() != null) {
					    cell4.setCellValue(record.getR40_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR40_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR40_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR40_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR40_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR40_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR40_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR40_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR40_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR40_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR40_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR40_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR40_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR40_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR40_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR40_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR40_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR40_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR40_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR40_TOTAL() != null) {
//					    cell14.setCellValue(record.getR40_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR40_NOACC() != null) {
					    cell15.setCellValue(record.getR40_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 40
					row = sheet.getRow(40);

					
					// Row 41
					// Column2
					cell2 = row.createCell(1);
					if (record.getR41_CURRENT() != null) {
					    cell2.setCellValue(record.getR41_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR41_CALL() != null) {
					    cell3.setCellValue(record.getR41_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR41_SAVINGS() != null) {
					    cell4.setCellValue(record.getR41_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR41_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR41_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR41_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR41_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR41_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR41_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR41_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR41_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR41_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR41_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR41_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR41_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR41_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR41_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR41_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR41_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR41_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR41_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR41_TOTAL() != null) {
//					    cell14.setCellValue(record.getR41_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR41_NOACC() != null) {
					    cell15.setCellValue(record.getR41_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 41
					row = sheet.getRow(41);


					// Row 42
					// Column2
					cell2 = row.createCell(1);
					if (record.getR42_CURRENT() != null) {
					    cell2.setCellValue(record.getR42_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR42_CALL() != null) {
					    cell3.setCellValue(record.getR42_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR42_SAVINGS() != null) {
					    cell4.setCellValue(record.getR42_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR42_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR42_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR42_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR42_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR42_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR42_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR42_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR42_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR42_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR42_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR42_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR42_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR42_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR42_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR42_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR42_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR42_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR42_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR42_TOTAL() != null) {
//					    cell14.setCellValue(record.getR42_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR42_NOACC() != null) {
					    cell15.setCellValue(record.getR42_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 42
					row = sheet.getRow(42);


					// Row 43
					// Column2
					cell2 = row.createCell(1);
					if (record.getR43_CURRENT() != null) {
					    cell2.setCellValue(record.getR43_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR43_CALL() != null) {
					    cell3.setCellValue(record.getR43_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR43_SAVINGS() != null) {
					    cell4.setCellValue(record.getR43_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR43_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR43_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR43_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR43_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR43_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR43_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR43_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR43_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR43_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR43_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR43_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR43_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR43_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR43_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR43_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR43_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR43_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR43_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR43_TOTAL() != null) {
//					    cell14.setCellValue(record.getR43_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR43_NOACC() != null) {
					    cell15.setCellValue(record.getR43_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 43
					row = sheet.getRow(43);


					// Row 44
					// Column2
					cell2 = row.createCell(1);
					if (record.getR44_CURRENT() != null) {
					    cell2.setCellValue(record.getR44_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR44_CALL() != null) {
					    cell3.setCellValue(record.getR44_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR44_SAVINGS() != null) {
					    cell4.setCellValue(record.getR44_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR44_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR44_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR44_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR44_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR44_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR44_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR44_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR44_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR44_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR44_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR44_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR44_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR44_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR44_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR44_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR44_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR44_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR44_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR44_TOTAL() != null) {
//					    cell14.setCellValue(record.getR44_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR44_NOACC() != null) {
					    cell15.setCellValue(record.getR44_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 44
					row = sheet.getRow(44);


					// Row 45
					// Column2
					cell2 = row.createCell(1);
					if (record.getR45_CURRENT() != null) {
					    cell2.setCellValue(record.getR45_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR45_CALL() != null) {
					    cell3.setCellValue(record.getR45_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR45_SAVINGS() != null) {
					    cell4.setCellValue(record.getR45_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR45_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR45_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR45_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR45_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR45_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR45_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR45_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR45_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR45_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR45_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR45_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR45_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR45_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR45_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR45_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR45_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR45_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR45_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR45_TOTAL() != null) {
//					    cell14.setCellValue(record.getR45_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR45_NOACC() != null) {
					    cell15.setCellValue(record.getR45_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 45
					row = sheet.getRow(45);


					// Row 46
					// Column2
//					cell2 = row.createCell(1);
//					if (record.getR46_CURRENT() != null) {
//					    cell2.setCellValue(record.getR46_CURRENT().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}
//
//					// Column3
//					cell3 = row.createCell(2);
//					if (record.getR46_CALL() != null) {
//					    cell3.setCellValue(record.getR46_CALL().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//
//					// Column4
//					cell4 = row.createCell(3);
//					if (record.getR46_SAVINGS() != null) {
//					    cell4.setCellValue(record.getR46_SAVINGS().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
//
//					// Column5
//					cell5 = row.createCell(4);
//					if (record.getR46_0_31D_NOTICE() != null) {
//					    cell5.setCellValue(record.getR46_0_31D_NOTICE().doubleValue());
//					    cell5.setCellStyle(numberStyle);
//					} else {
//					    cell5.setCellValue("");
//					    cell5.setCellStyle(textStyle);
//					}
//
//					// Column6
//					cell6 = row.createCell(5);
//					if (record.getR46_32_88D_NOTICE() != null) {
//					    cell6.setCellValue(record.getR46_32_88D_NOTICE().doubleValue());
//					    cell6.setCellStyle(numberStyle);
//					} else {
//					    cell6.setCellValue("");
//					    cell6.setCellStyle(textStyle);
//					}
//
//					// Column7
//					cell7 = row.createCell(6);
//					if (record.getR46_91D_DEPOSIT() != null) {
//					    cell7.setCellValue(record.getR46_91D_DEPOSIT().doubleValue());
//					    cell7.setCellStyle(numberStyle);
//					} else {
//					    cell7.setCellValue("");
//					    cell7.setCellStyle(textStyle);
//					}
//
//					// Column8
//					cell8 = row.createCell(7);
//					if (record.getR46_1_2M_FD() != null) {
//					    cell8.setCellValue(record.getR46_1_2M_FD().doubleValue());
//					    cell8.setCellStyle(numberStyle);
//					} else {
//					    cell8.setCellValue("");
//					    cell8.setCellStyle(textStyle);
//					}
//
//					// Column9
//					cell9 = row.createCell(8);
//					if (record.getR46_4_6M_FD() != null) {
//					    cell9.setCellValue(record.getR46_4_6M_FD().doubleValue());
//					    cell9.setCellStyle(numberStyle);
//					} else {
//					    cell9.setCellValue("");
//					    cell9.setCellStyle(textStyle);
//					}
//
//					// Column10
//					cell10 = row.createCell(9);
//					if (record.getR46_7_12M_FD() != null) {
//					    cell10.setCellValue(record.getR46_7_12M_FD().doubleValue());
//					    cell10.setCellStyle(numberStyle);
//					} else {
//					    cell10.setCellValue("");
//					    cell10.setCellStyle(textStyle);
//					}
//
//					// Column11
//					cell11 = row.createCell(10);
//					if (record.getR46_13_18M_FD() != null) {
//					    cell11.setCellValue(record.getR46_13_18M_FD().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}
//
//					// Column12
//					cell12 = row.createCell(11);
//					if (record.getR46_19_24M_FD() != null) {
//					    cell12.setCellValue(record.getR46_19_24M_FD().doubleValue());
//					    cell12.setCellStyle(numberStyle);
//					} else {
//					    cell12.setCellValue("");
//					    cell12.setCellStyle(textStyle);
//					}
//
//					// Column13
//					cell13 = row.createCell(12);
//					if (record.getR46_OVER24_FD() != null) {
//					    cell13.setCellValue(record.getR46_OVER24_FD().doubleValue());
//					    cell13.setCellStyle(numberStyle);
//					} else {
//					    cell13.setCellValue("");
//					    cell13.setCellStyle(textStyle);
//					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR46_TOTAL() != null) {
//					    cell14.setCellValue(record.getR46_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
//					cell15 = row.createCell(14);
//					if (record.getR46_NOACC() != null) {
//					    cell15.setCellValue(record.getR46_NOACC().doubleValue());
//					    cell15.setCellStyle(numberStyle);
//					} else {
//					    cell15.setCellValue("");
//					    cell15.setCellStyle(textStyle);
//					}

					// Assign Row 46
					row = sheet.getRow(46);

					
					// Row 47
					// Column2
					cell2 = row.createCell(1);
					if (record.getR47_CURRENT() != null) {
					    cell2.setCellValue(record.getR47_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR47_CALL() != null) {
					    cell3.setCellValue(record.getR47_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR47_SAVINGS() != null) {
					    cell4.setCellValue(record.getR47_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR47_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR47_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR47_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR47_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR47_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR47_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR47_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR47_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR47_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR47_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR47_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR47_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR47_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR47_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR47_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR47_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR47_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR47_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR47_TOTAL() != null) {
//					    cell14.setCellValue(record.getR47_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR47_NOACC() != null) {
					    cell15.setCellValue(record.getR47_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 47
//					row = sheet.getRow(47);
					

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

	/*
	 * public byte[] BRRS_Q_SMME_DEPDetailExcel(String filename, String fromdate,
	 * String todate, String currency, String dtltype, String type, String version)
	 * {
	 * 
	 * try { logger.info("Generating Excel for BRRS_Q_SMME_DEP Details...");
	 * System.out.println("came to Detail download service"); if
	 * (type.equals("ARCHIVAL") & version != null) { byte[] ARCHIVALreport =
	 * getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
	 * version); return ARCHIVALreport; } XSSFWorkbook workbook = new
	 * XSSFWorkbook(); XSSFSheet sheet =
	 * workbook.createSheet("BRRS_Q_SMME_DEPDetails");
	 * 
	 * // Common border style BorderStyle border = BorderStyle.THIN; // Header style
	 * (left aligned) CellStyle headerStyle = workbook.createCellStyle(); Font
	 * headerFont = workbook.createFont(); headerFont.setBold(true);
	 * headerFont.setFontHeightInPoints((short) 10);
	 * headerStyle.setFont(headerFont);
	 * headerStyle.setAlignment(HorizontalAlignment.LEFT);
	 * headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	 * headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	 * headerStyle.setBorderTop(border); headerStyle.setBorderBottom(border);
	 * headerStyle.setBorderLeft(border); headerStyle.setBorderRight(border);
	 * 
	 * // Right-aligned header style for ACCT BALANCE CellStyle
	 * rightAlignedHeaderStyle = workbook.createCellStyle();
	 * rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
	 * rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);
	 * 
	 * // Default data style (left aligned) CellStyle dataStyle =
	 * workbook.createCellStyle(); dataStyle.setAlignment(HorizontalAlignment.LEFT);
	 * dataStyle.setBorderTop(border); dataStyle.setBorderBottom(border);
	 * dataStyle.setBorderLeft(border); dataStyle.setBorderRight(border);
	 * 
	 * // ACCT BALANCE style (right aligned with 3 decimals) CellStyle balanceStyle
	 * = workbook.createCellStyle();
	 * balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
	 * balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
	 * balanceStyle.setBorderTop(border); balanceStyle.setBorderBottom(border);
	 * balanceStyle.setBorderLeft(border); balanceStyle.setBorderRight(border); //
	 * Header row String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME",
	 * "ACCT BALANCE", "ROWID", "COLUMNID", "REPORT_DATE" }; XSSFRow headerRow =
	 * sheet.createRow(0); for (int i = 0; i < headers.length; i++) { Cell cell =
	 * headerRow.createCell(i); cell.setCellValue(headers[i]); if (i == 3) { // ACCT
	 * BALANCE cell.setCellStyle(rightAlignedHeaderStyle); } else {
	 * cell.setCellStyle(headerStyle); } sheet.setColumnWidth(i, 5000); } // Get
	 * data Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
	 * List<BRRS_Q_SMME_DEP_DETAIL_Entity> reportData =
	 * BRRS_Q_SMME_DEP_DETAIL_Repo.getdatabydateList(parsedToDate); if (reportData
	 * != null && !reportData.isEmpty()) { int rowIndex = 1; for
	 * (BRRS_Q_SMME_DEP_DETAIL_Entity item : reportData) { XSSFRow row =
	 * sheet.createRow(rowIndex++);
	 * row.createCell(0).setCellValue(item.getCUST_ID());
	 * row.createCell(1).setCellValue(item.getACCT_NUMBER());
	 * row.createCell(2).setCellValue(item.getACCT_NAME()); // ACCT BALANCE (right
	 * aligned, 3 decimal places) Cell balanceCell = row.createCell(3); if
	 * (item.getACCT_BALANCE_IN_PULA() != null) {
	 * balanceCell.setCellValue(item.getACCT_BALANCE_IN_PULA().doubleValue()); }
	 * else { balanceCell.setCellValue(0.000); }
	 * balanceCell.setCellStyle(balanceStyle);
	 * row.createCell(4).setCellValue(item.getROW_ID());
	 * row.createCell(5).setCellValue(item.getCOLUMN_ID()); row.createCell(6)
	 * .setCellValue(item.getREPORT_DATE() != null ? new
	 * SimpleDateFormat("dd-MM-yyyy").format(item.getREPORT_DATE()) : ""); // Apply
	 * data style for all other cells for (int j = 0; j < 7; j++) { if (j != 3) {
	 * row.getCell(j).setCellStyle(dataStyle); } } } } else { logger.
	 * info("No data found for BRRS_Q_SMME_DEP — only header will be written."); }
	 * // Write to byte[] ByteArrayOutputStream bos = new ByteArrayOutputStream();
	 * workbook.write(bos); workbook.close();
	 * logger.info("Excel generation completed with {} row(s).", reportData != null
	 * ? reportData.size() : 0); return bos.toByteArray(); } catch (Exception e) {
	 * logger.error("Error generating BRRS_Q_SMME_DEP Excel", e); return new
	 * byte[0]; } }
	 */

	/*
	 * public List<Object> getQ_SMME_DEPArchival() { List<Object>
	 * Q_SMME_DEPArchivallist = new ArrayList<>(); try { Q_SMME_DEPArchivallist =
	 * BRRS_Q_SMME_DEP_Archival_Summary_Repo.getQ_SMME_DEParchival();
	 * System.out.println("countser" + Q_SMME_DEPArchivallist.size()); } catch
	 * (Exception e) { // Log the exception
	 * System.err.println("Error fetching Q_SMME_DEP Archival data: " +
	 * e.getMessage()); e.printStackTrace();
	 * 
	 * // Optionally, you can rethrow it or return empty list // throw new
	 * RuntimeException("Failed to fetch data", e); } return Q_SMME_DEPArchivallist;
	 * }
	 */
	
	public List<Object[]> getQ_SMME_DEPArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<Q_SMME_DEP_Archival_Summary_Entity> repoData = BRRS_Q_SMME_DEP_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (repoData != null && !repoData.isEmpty()) {
				for (Q_SMME_DEP_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] {
							entity.getReport_date(), 
							entity.getReport_version() 
					};
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				Q_SMME_DEP_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReport_version());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching Q_SMME_DEP Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	public byte[] getExcelQ_SMME_DEPARCHIVAL(String filename, String reportId, String fromdate, String todate,
										   String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<Q_SMME_DEP_Archival_Summary_Entity> dataList = BRRS_Q_SMME_DEP_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Q_SMME_DEP report. Returning empty result.");
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
					Q_SMME_DEP_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}



					//ROW11
					//COLUMN2
					Cell cell2 = row.createCell(1);
					if (record.getR11_CURRENT() != null) {
						cell2.setCellValue(record.getR11_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					//row11
					// Column3
					Cell cell3 = row.createCell(2);
					if (record.getR11_CALL() != null) {
						cell3.setCellValue(record.getR11_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row11
					// Column4
					Cell cell4 = row.createCell(3);
					if (record.getR11_SAVINGS() != null) {
						cell4.setCellValue(record.getR11_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row11
					// Column5
					Cell cell5 = row.createCell(4);
					if (record.getR11_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR11_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					//row11
					// Column6
					Cell cell6 = row.createCell(5);
					if (record.getR11_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR11_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					//row11
					// Column7
					Cell cell7 = row.createCell(6);
					if (record.getR11_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR11_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					//row11
					// Column8
					Cell cell8 = row.createCell(7);
					if (record.getR11_1_2M_FD() != null) {
						cell8.setCellValue(record.getR11_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					//row11
					// Column9
					Cell cell9 = row.createCell(8);
					if (record.getR11_4_6M_FD() != null) {
						cell9.setCellValue(record.getR11_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					//row11
					// Column10
					Cell cell10 = row.createCell(9);
					if (record.getR11_7_12M_FD() != null) {
						cell10.setCellValue(record.getR11_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					//row11
					// Column11
					Cell cell11 = row.createCell(10);
					if (record.getR11_13_18M_FD() != null) {
						cell11.setCellValue(record.getR11_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row11
					// Column12
					Cell cell12 = row.createCell(11);
					if (record.getR11_19_24M_FD() != null) {
						cell12.setCellValue(record.getR11_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					
					//row11
					// Column13
					Cell cell13 = row.createCell(12);
					if (record.getR11_OVER24_FD() != null) {
						cell13.setCellValue(record.getR11_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					//row11
					// Column14
//					Cell cell14 = row.createCell(13);
//					if (record.getR11_TOTAL() != null) {
//						cell14.setCellValue(record.getR11_TOTAL().doubleValue());
//						cell14.setCellStyle(numberStyle);
//					} else {
//						cell14.setCellValue("");
//						cell14.setCellStyle(textStyle);
//					}
					
					//row11
					// Column15
					Cell cell15 = row.createCell(14);
					if (record.getR11_NOACC() != null) {
						cell15.setCellValue(record.getR11_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}
		
					row = sheet.getRow(11);
				
					
					
					// Row 12
					// Column2
					cell2 = row.createCell(1);
					if (record.getR12_CURRENT() != null) {
					    cell2.setCellValue(record.getR12_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR12_CALL() != null) {
					    cell3.setCellValue(record.getR12_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR12_SAVINGS() != null) {
					    cell4.setCellValue(record.getR12_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR12_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR12_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR12_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR12_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR12_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR12_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR12_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR12_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR12_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR12_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR12_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR12_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR12_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR12_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR12_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR12_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR12_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR12_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR12_TOTAL() != null) {
//					    cell14.setCellValue(record.getR12_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR12_NOACC() != null) {
					    cell15.setCellValue(record.getR12_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					
					
					// Row 13
					// Column2
					cell2 = row.createCell(1);
					if (record.getR13_CURRENT() != null) {
					    cell2.setCellValue(record.getR13_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR13_CALL() != null) {
					    cell3.setCellValue(record.getR13_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR13_SAVINGS() != null) {
					    cell4.setCellValue(record.getR13_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR13_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR13_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR13_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR13_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR13_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR13_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR13_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR13_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR13_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR13_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR13_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR13_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR13_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR13_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR13_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR13_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR13_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR13_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR13_TOTAL() != null) {
//					    cell14.setCellValue(record.getR13_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR13_NOACC() != null) {
					    cell15.setCellValue(record.getR13_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);

					
					// Row 14
					// Column2
					cell2 = row.createCell(1);
					if (record.getR14_CURRENT() != null) {
					    cell2.setCellValue(record.getR14_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR14_CALL() != null) {
					    cell3.setCellValue(record.getR14_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR14_SAVINGS() != null) {
					    cell4.setCellValue(record.getR14_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR14_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR14_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR14_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR14_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR14_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR14_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR14_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR14_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR14_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR14_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR14_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR14_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR14_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR14_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR14_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR14_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR14_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR14_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR14_TOTAL() != null) {
//					    cell14.setCellValue(record.getR14_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR14_NOACC() != null) {
					    cell15.setCellValue(record.getR14_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 14
					row = sheet.getRow(14);

					
					// Row 15
					// Column2
					cell2 = row.createCell(1);
					if (record.getR15_CURRENT() != null) {
					    cell2.setCellValue(record.getR15_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR15_CALL() != null) {
					    cell3.setCellValue(record.getR15_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR15_SAVINGS() != null) {
					    cell4.setCellValue(record.getR15_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR15_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR15_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR15_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR15_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR15_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR15_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR15_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR15_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR15_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR15_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR15_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR15_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR15_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR15_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR15_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR15_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR15_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR15_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR15_TOTAL() != null) {
//					    cell14.setCellValue(record.getR15_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR15_NOACC() != null) {
					    cell15.setCellValue(record.getR15_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 15
					row = sheet.getRow(15);
					
					
					
					// Row 16
					// Column2
					cell2 = row.createCell(1);
					if (record.getR16_CURRENT() != null) {
					    cell2.setCellValue(record.getR16_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR16_CALL() != null) {
					    cell3.setCellValue(record.getR16_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR16_SAVINGS() != null) {
					    cell4.setCellValue(record.getR16_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR16_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR16_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR16_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR16_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR16_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR16_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR16_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR16_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR16_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR16_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR16_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR16_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR16_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR16_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR16_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR16_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR16_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR16_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR16_TOTAL() != null) {
//					    cell14.setCellValue(record.getR16_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR16_NOACC() != null) {
					    cell15.setCellValue(record.getR16_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 16
					row = sheet.getRow(16);

					
					// Row 17
					// Column2
					cell2 = row.createCell(1);
					if (record.getR17_CURRENT() != null) {
					    cell2.setCellValue(record.getR17_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR17_CALL() != null) {
					    cell3.setCellValue(record.getR17_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR17_SAVINGS() != null) {
					    cell4.setCellValue(record.getR17_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR17_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR17_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR17_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR17_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR17_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR17_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR17_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR17_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR17_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR17_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR17_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR17_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR17_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR17_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR17_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR17_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR17_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR17_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR17_TOTAL() != null) {
//					    cell14.setCellValue(record.getR17_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR17_NOACC() != null) {
					    cell15.setCellValue(record.getR17_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 17
					row = sheet.getRow(17);

					
					// Row 18
					// Column2
					cell2 = row.createCell(1);
					if (record.getR18_CURRENT() != null) {
					    cell2.setCellValue(record.getR18_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR18_CALL() != null) {
					    cell3.setCellValue(record.getR18_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR18_SAVINGS() != null) {
					    cell4.setCellValue(record.getR18_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR18_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR18_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR18_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR18_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR18_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR18_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR18_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR18_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR18_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR18_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR18_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR18_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR18_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR18_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR18_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR18_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR18_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR18_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR18_TOTAL() != null) {
//					    cell14.setCellValue(record.getR18_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR18_NOACC() != null) {
					    cell15.setCellValue(record.getR18_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 18
					row = sheet.getRow(18);

					
					
					// Row 19
					// Column2
					cell2 = row.createCell(1);
					if (record.getR19_CURRENT() != null) {
					    cell2.setCellValue(record.getR19_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR19_CALL() != null) {
					    cell3.setCellValue(record.getR19_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR19_SAVINGS() != null) {
					    cell4.setCellValue(record.getR19_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR19_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR19_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR19_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR19_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR19_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR19_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR19_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR19_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR19_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR19_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR19_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR19_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR19_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR19_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR19_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR19_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR19_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR19_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR19_TOTAL() != null) {
//					    cell14.setCellValue(record.getR19_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR19_NOACC() != null) {
					    cell15.setCellValue(record.getR19_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 19
					row = sheet.getRow(19);

					
					// Row 20
					// Column2
					cell2 = row.createCell(1);
					if (record.getR20_CURRENT() != null) {
					    cell2.setCellValue(record.getR20_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR20_CALL() != null) {
					    cell3.setCellValue(record.getR20_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR20_SAVINGS() != null) {
					    cell4.setCellValue(record.getR20_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR20_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR20_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR20_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR20_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR20_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR20_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR20_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR20_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR20_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR20_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR20_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR20_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR20_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR20_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR20_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR20_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR20_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR20_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR20_TOTAL() != null) {
//					    cell14.setCellValue(record.getR20_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR20_NOACC() != null) {
					    cell15.setCellValue(record.getR20_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 20
					row = sheet.getRow(20);

					
					// Row 21
					// Column2
					cell2 = row.createCell(1);
					if (record.getR21_CURRENT() != null) {
					    cell2.setCellValue(record.getR21_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR21_CALL() != null) {
					    cell3.setCellValue(record.getR21_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR21_SAVINGS() != null) {
					    cell4.setCellValue(record.getR21_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR21_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR21_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR21_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR21_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR21_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR21_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR21_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR21_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR21_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR21_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR21_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR21_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR21_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR21_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR21_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR21_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR21_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR21_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR21_TOTAL() != null) {
//					    cell14.setCellValue(record.getR21_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR21_NOACC() != null) {
					    cell15.setCellValue(record.getR21_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 21
					row = sheet.getRow(21);

					// Row 22
					// Column2
					cell2 = row.createCell(1);
					if (record.getR22_CURRENT() != null) {
					    cell2.setCellValue(record.getR22_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR22_CALL() != null) {
					    cell3.setCellValue(record.getR22_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR22_SAVINGS() != null) {
					    cell4.setCellValue(record.getR22_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR22_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR22_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR22_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR22_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR22_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR22_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR22_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR22_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR22_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR22_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR22_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR22_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR22_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR22_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR22_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR22_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR22_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR22_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR22_TOTAL() != null) {
//					    cell14.setCellValue(record.getR22_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR22_NOACC() != null) {
					    cell15.setCellValue(record.getR22_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 22
					row = sheet.getRow(22);
					
					
					// Row 23
					// Column2
					cell2 = row.createCell(1);
					if (record.getR23_CURRENT() != null) {
					    cell2.setCellValue(record.getR23_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR23_CALL() != null) {
					    cell3.setCellValue(record.getR23_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR23_SAVINGS() != null) {
					    cell4.setCellValue(record.getR23_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR23_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR23_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR23_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR23_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR23_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR23_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR23_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR23_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR23_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR23_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR23_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR23_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR23_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR23_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR23_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR23_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR23_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR23_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR23_TOTAL() != null) {
//					    cell14.setCellValue(record.getR23_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR23_NOACC() != null) {
					    cell15.setCellValue(record.getR23_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 23
					row = sheet.getRow(23);


					// Row 24
					// Column2
					cell2 = row.createCell(1);
					if (record.getR24_CURRENT() != null) {
					    cell2.setCellValue(record.getR24_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR24_CALL() != null) {
					    cell3.setCellValue(record.getR24_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR24_SAVINGS() != null) {
					    cell4.setCellValue(record.getR24_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR24_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR24_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR24_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR24_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR24_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR24_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR24_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR24_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR24_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR24_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR24_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR24_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR24_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR24_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR24_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR24_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR24_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR24_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR24_TOTAL() != null) {
//					    cell14.setCellValue(record.getR24_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR24_NOACC() != null) {
					    cell15.setCellValue(record.getR24_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 24
					row = sheet.getRow(24);


					// Row 25
					// Column2
//					cell2 = row.createCell(1);
//					if (record.getR25_CURRENT() != null) {
//					    cell2.setCellValue(record.getR25_CURRENT().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}
//
//					// Column3
//					cell3 = row.createCell(2);
//					if (record.getR25_CALL() != null) {
//					    cell3.setCellValue(record.getR25_CALL().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//
//					// Column4
//					cell4 = row.createCell(3);
//					if (record.getR25_SAVINGS() != null) {
//					    cell4.setCellValue(record.getR25_SAVINGS().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
//
//					// Column5
//					cell5 = row.createCell(4);
//					if (record.getR25_0_31D_NOTICE() != null) {
//					    cell5.setCellValue(record.getR25_0_31D_NOTICE().doubleValue());
//					    cell5.setCellStyle(numberStyle);
//					} else {
//					    cell5.setCellValue("");
//					    cell5.setCellStyle(textStyle);
//					}
//
//					// Column6
//					cell6 = row.createCell(5);
//					if (record.getR25_32_88D_NOTICE() != null) {
//					    cell6.setCellValue(record.getR25_32_88D_NOTICE().doubleValue());
//					    cell6.setCellStyle(numberStyle);
//					} else {
//					    cell6.setCellValue("");
//					    cell6.setCellStyle(textStyle);
//					}
//
//					// Column7
//					cell7 = row.createCell(6);
//					if (record.getR25_91D_DEPOSIT() != null) {
//					    cell7.setCellValue(record.getR25_91D_DEPOSIT().doubleValue());
//					    cell7.setCellStyle(numberStyle);
//					} else {
//					    cell7.setCellValue("");
//					    cell7.setCellStyle(textStyle);
//					}
//
//					// Column8
//					cell8 = row.createCell(7);
//					if (record.getR25_1_2M_FD() != null) {
//					    cell8.setCellValue(record.getR25_1_2M_FD().doubleValue());
//					    cell8.setCellStyle(numberStyle);
//					} else {
//					    cell8.setCellValue("");
//					    cell8.setCellStyle(textStyle);
//					}
//
//					// Column9
//					cell9 = row.createCell(8);
//					if (record.getR25_4_6M_FD() != null) {
//					    cell9.setCellValue(record.getR25_4_6M_FD().doubleValue());
//					    cell9.setCellStyle(numberStyle);
//					} else {
//					    cell9.setCellValue("");
//					    cell9.setCellStyle(textStyle);
//					}
//
//					// Column10
//					cell10 = row.createCell(9);
//					if (record.getR25_7_12M_FD() != null) {
//					    cell10.setCellValue(record.getR25_7_12M_FD().doubleValue());
//					    cell10.setCellStyle(numberStyle);
//					} else {
//					    cell10.setCellValue("");
//					    cell10.setCellStyle(textStyle);
//					}
//
//					// Column11
//					cell11 = row.createCell(10);
//					if (record.getR25_13_18M_FD() != null) {
//					    cell11.setCellValue(record.getR25_13_18M_FD().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}
//
//					// Column12
//					cell12 = row.createCell(11);
//					if (record.getR25_19_24M_FD() != null) {
//					    cell12.setCellValue(record.getR25_19_24M_FD().doubleValue());
//					    cell12.setCellStyle(numberStyle);
//					} else {
//					    cell12.setCellValue("");
//					    cell12.setCellStyle(textStyle);
//					}
//
//					// Column13
//					cell13 = row.createCell(12);
//					if (record.getR25_OVER24_FD() != null) {
//					    cell13.setCellValue(record.getR25_OVER24_FD().doubleValue());
//					    cell13.setCellStyle(numberStyle);
//					} else {
//					    cell13.setCellValue("");
//					    cell13.setCellStyle(textStyle);
//					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR25_TOTAL() != null) {
//					    cell14.setCellValue(record.getR25_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
//					cell15 = row.createCell(14);
//					if (record.getR25_NOACC() != null) {
//					    cell15.setCellValue(record.getR25_NOACC().doubleValue());
//					    cell15.setCellStyle(numberStyle);
//					} else {
//					    cell15.setCellValue("");
//					    cell15.setCellStyle(textStyle);
//					}

					// Assign Row 25
					row = sheet.getRow(25);



					// Row 26
					// Column2
					cell2 = row.createCell(1);
					if (record.getR26_CURRENT() != null) {
					    cell2.setCellValue(record.getR26_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR26_CALL() != null) {
					    cell3.setCellValue(record.getR26_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR26_SAVINGS() != null) {
					    cell4.setCellValue(record.getR26_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR26_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR26_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR26_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR26_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR26_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR26_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR26_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR26_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR26_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR26_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR26_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR26_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR26_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR26_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR26_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR26_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR26_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR26_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR26_TOTAL() != null) {
//					    cell14.setCellValue(record.getR26_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR26_NOACC() != null) {
					    cell15.setCellValue(record.getR26_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 26
					row = sheet.getRow(31);

					
					
					// Row 32
					// Column2
					cell2 = row.createCell(1);
					if (record.getR32_CURRENT() != null) {
						System.out.println(record.getR32_CURRENT());
					    cell2.setCellValue(record.getR32_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR32_CALL() != null) {
					    cell3.setCellValue(record.getR32_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR32_SAVINGS() != null) {
					    cell4.setCellValue(record.getR32_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR32_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR32_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR32_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR32_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR32_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR32_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR32_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR32_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR32_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR32_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR32_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR32_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR32_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR32_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR32_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR32_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR32_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR32_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR32_TOTAL() != null) {
//					    cell14.setCellValue(record.getR32_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR32_NOACC() != null) {
					    cell15.setCellValue(record.getR32_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 32
					row = sheet.getRow(32);


					// Row 33
					// Column2
					cell2 = row.createCell(1);
					if (record.getR33_CURRENT() != null) {
					    cell2.setCellValue(record.getR33_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR33_CALL() != null) {
					    cell3.setCellValue(record.getR33_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR33_SAVINGS() != null) {
					    cell4.setCellValue(record.getR33_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR33_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR33_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR33_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR33_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR33_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR33_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR33_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR33_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR33_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR33_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR33_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR33_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR33_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR33_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR33_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR33_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR33_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR33_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR33_TOTAL() != null) {
//					    cell14.setCellValue(record.getR33_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR33_NOACC() != null) {
					    cell15.setCellValue(record.getR33_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 33
					row = sheet.getRow(33);


					// Row 34
					// Column2
					cell2 = row.createCell(1);
					if (record.getR34_CURRENT() != null) {
					    cell2.setCellValue(record.getR34_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR34_CALL() != null) {
					    cell3.setCellValue(record.getR34_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR34_SAVINGS() != null) {
					    cell4.setCellValue(record.getR34_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR34_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR34_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR34_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR34_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR34_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR34_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR34_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR34_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR34_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR34_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR34_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR34_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR34_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR34_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR34_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR34_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR34_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR34_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR34_TOTAL() != null) {
//					    cell14.setCellValue(record.getR34_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR34_NOACC() != null) {
					    cell15.setCellValue(record.getR34_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 34
					row = sheet.getRow(34);

					
					// Row 35
					// Column2
					cell2 = row.createCell(1);
					if (record.getR35_CURRENT() != null) {
					    cell2.setCellValue(record.getR35_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR35_CALL() != null) {
					    cell3.setCellValue(record.getR35_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR35_SAVINGS() != null) {
					    cell4.setCellValue(record.getR35_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR35_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR35_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR35_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR35_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR35_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR35_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR35_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR35_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR35_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR35_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR35_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR35_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR35_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR35_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR35_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR35_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR35_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR35_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR35_TOTAL() != null) {
//					    cell14.setCellValue(record.getR35_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR35_NOACC() != null) {
					    cell15.setCellValue(record.getR35_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 35
					row = sheet.getRow(35);


					// Row 36
					// Column2
					cell2 = row.createCell(1);
					if (record.getR36_CURRENT() != null) {
					    cell2.setCellValue(record.getR36_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR36_CALL() != null) {
					    cell3.setCellValue(record.getR36_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR36_SAVINGS() != null) {
					    cell4.setCellValue(record.getR36_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR36_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR36_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR36_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR36_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR36_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR36_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR36_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR36_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR36_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR36_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR36_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR36_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR36_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR36_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR36_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR36_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR36_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR36_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR36_TOTAL() != null) {
//					    cell14.setCellValue(record.getR36_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR36_NOACC() != null) {
					    cell15.setCellValue(record.getR36_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 36
					row = sheet.getRow(36);


					// Row 37
					// Column2
					cell2 = row.createCell(1);
					if (record.getR37_CURRENT() != null) {
					    cell2.setCellValue(record.getR37_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR37_CALL() != null) {
					    cell3.setCellValue(record.getR37_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR37_SAVINGS() != null) {
					    cell4.setCellValue(record.getR37_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR37_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR37_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR37_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR37_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR37_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR37_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR37_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR37_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR37_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR37_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR37_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR37_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR37_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR37_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR37_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR37_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR37_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR37_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR37_TOTAL() != null) {
//					    cell14.setCellValue(record.getR37_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR37_NOACC() != null) {
					    cell15.setCellValue(record.getR37_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 37
					row = sheet.getRow(37);



					// Row 38
					// Column2
					cell2 = row.createCell(1);
					if (record.getR38_CURRENT() != null) {
					    cell2.setCellValue(record.getR38_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR38_CALL() != null) {
					    cell3.setCellValue(record.getR38_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR38_SAVINGS() != null) {
					    cell4.setCellValue(record.getR38_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR38_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR38_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR38_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR38_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR38_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR38_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR38_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR38_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR38_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR38_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR38_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR38_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR38_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR38_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR38_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR38_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR38_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR38_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR38_TOTAL() != null) {
//					    cell14.setCellValue(record.getR38_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR38_NOACC() != null) {
					    cell15.setCellValue(record.getR38_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 38
					row = sheet.getRow(38);


					// Row 39
					// Column2
					cell2 = row.createCell(1);
					if (record.getR39_CURRENT() != null) {
					    cell2.setCellValue(record.getR39_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR39_CALL() != null) {
					    cell3.setCellValue(record.getR39_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR39_SAVINGS() != null) {
					    cell4.setCellValue(record.getR39_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR39_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR39_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR39_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR39_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR39_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR39_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR39_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR39_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR39_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR39_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR39_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR39_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR39_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR39_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR39_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR39_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR39_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR39_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR39_TOTAL() != null) {
//					    cell14.setCellValue(record.getR39_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR39_NOACC() != null) {
					    cell15.setCellValue(record.getR39_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 39
					row = sheet.getRow(39);


					// Row 40
					// Column2
					cell2 = row.createCell(1);
					if (record.getR40_CURRENT() != null) {
					    cell2.setCellValue(record.getR40_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR40_CALL() != null) {
					    cell3.setCellValue(record.getR40_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR40_SAVINGS() != null) {
					    cell4.setCellValue(record.getR40_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR40_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR40_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR40_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR40_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR40_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR40_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR40_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR40_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR40_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR40_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR40_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR40_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR40_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR40_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR40_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR40_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR40_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR40_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR40_TOTAL() != null) {
//					    cell14.setCellValue(record.getR40_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR40_NOACC() != null) {
					    cell15.setCellValue(record.getR40_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 40
					row = sheet.getRow(40);

					
					// Row 41
					// Column2
					cell2 = row.createCell(1);
					if (record.getR41_CURRENT() != null) {
					    cell2.setCellValue(record.getR41_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR41_CALL() != null) {
					    cell3.setCellValue(record.getR41_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR41_SAVINGS() != null) {
					    cell4.setCellValue(record.getR41_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR41_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR41_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR41_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR41_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR41_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR41_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR41_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR41_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR41_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR41_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR41_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR41_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR41_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR41_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR41_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR41_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR41_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR41_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR41_TOTAL() != null) {
//					    cell14.setCellValue(record.getR41_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR41_NOACC() != null) {
					    cell15.setCellValue(record.getR41_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 41
					row = sheet.getRow(41);


					// Row 42
					// Column2
					cell2 = row.createCell(1);
					if (record.getR42_CURRENT() != null) {
					    cell2.setCellValue(record.getR42_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR42_CALL() != null) {
					    cell3.setCellValue(record.getR42_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR42_SAVINGS() != null) {
					    cell4.setCellValue(record.getR42_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR42_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR42_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR42_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR42_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR42_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR42_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR42_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR42_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR42_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR42_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR42_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR42_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR42_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR42_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR42_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR42_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR42_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR42_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR42_TOTAL() != null) {
//					    cell14.setCellValue(record.getR42_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR42_NOACC() != null) {
					    cell15.setCellValue(record.getR42_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 42
					row = sheet.getRow(42);


					// Row 43
					// Column2
					cell2 = row.createCell(1);
					if (record.getR43_CURRENT() != null) {
					    cell2.setCellValue(record.getR43_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR43_CALL() != null) {
					    cell3.setCellValue(record.getR43_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR43_SAVINGS() != null) {
					    cell4.setCellValue(record.getR43_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR43_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR43_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR43_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR43_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR43_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR43_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR43_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR43_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR43_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR43_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR43_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR43_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR43_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR43_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR43_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR43_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR43_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR43_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR43_TOTAL() != null) {
//					    cell14.setCellValue(record.getR43_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR43_NOACC() != null) {
					    cell15.setCellValue(record.getR43_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 43
					row = sheet.getRow(43);


					// Row 44
					// Column2
					cell2 = row.createCell(1);
					if (record.getR44_CURRENT() != null) {
					    cell2.setCellValue(record.getR44_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR44_CALL() != null) {
					    cell3.setCellValue(record.getR44_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR44_SAVINGS() != null) {
					    cell4.setCellValue(record.getR44_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR44_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR44_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR44_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR44_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR44_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR44_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR44_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR44_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR44_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR44_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR44_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR44_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR44_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR44_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR44_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR44_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR44_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR44_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR44_TOTAL() != null) {
//					    cell14.setCellValue(record.getR44_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR44_NOACC() != null) {
					    cell15.setCellValue(record.getR44_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 44
					row = sheet.getRow(44);


					// Row 45
					// Column2
					cell2 = row.createCell(1);
					if (record.getR45_CURRENT() != null) {
					    cell2.setCellValue(record.getR45_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR45_CALL() != null) {
					    cell3.setCellValue(record.getR45_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR45_SAVINGS() != null) {
					    cell4.setCellValue(record.getR45_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR45_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR45_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR45_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR45_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR45_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR45_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR45_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR45_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR45_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR45_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR45_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR45_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR45_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR45_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR45_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR45_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR45_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR45_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR45_TOTAL() != null) {
//					    cell14.setCellValue(record.getR45_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR45_NOACC() != null) {
					    cell15.setCellValue(record.getR45_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 45
					row = sheet.getRow(45);


					// Row 46
					// Column2
//					cell2 = row.createCell(1);
//					if (record.getR46_CURRENT() != null) {
//					    cell2.setCellValue(record.getR46_CURRENT().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}
//
//					// Column3
//					cell3 = row.createCell(2);
//					if (record.getR46_CALL() != null) {
//					    cell3.setCellValue(record.getR46_CALL().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//
//					// Column4
//					cell4 = row.createCell(3);
//					if (record.getR46_SAVINGS() != null) {
//					    cell4.setCellValue(record.getR46_SAVINGS().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
//
//					// Column5
//					cell5 = row.createCell(4);
//					if (record.getR46_0_31D_NOTICE() != null) {
//					    cell5.setCellValue(record.getR46_0_31D_NOTICE().doubleValue());
//					    cell5.setCellStyle(numberStyle);
//					} else {
//					    cell5.setCellValue("");
//					    cell5.setCellStyle(textStyle);
//					}
//
//					// Column6
//					cell6 = row.createCell(5);
//					if (record.getR46_32_88D_NOTICE() != null) {
//					    cell6.setCellValue(record.getR46_32_88D_NOTICE().doubleValue());
//					    cell6.setCellStyle(numberStyle);
//					} else {
//					    cell6.setCellValue("");
//					    cell6.setCellStyle(textStyle);
//					}
//
//					// Column7
//					cell7 = row.createCell(6);
//					if (record.getR46_91D_DEPOSIT() != null) {
//					    cell7.setCellValue(record.getR46_91D_DEPOSIT().doubleValue());
//					    cell7.setCellStyle(numberStyle);
//					} else {
//					    cell7.setCellValue("");
//					    cell7.setCellStyle(textStyle);
//					}
//
//					// Column8
//					cell8 = row.createCell(7);
//					if (record.getR46_1_2M_FD() != null) {
//					    cell8.setCellValue(record.getR46_1_2M_FD().doubleValue());
//					    cell8.setCellStyle(numberStyle);
//					} else {
//					    cell8.setCellValue("");
//					    cell8.setCellStyle(textStyle);
//					}
//
//					// Column9
//					cell9 = row.createCell(8);
//					if (record.getR46_4_6M_FD() != null) {
//					    cell9.setCellValue(record.getR46_4_6M_FD().doubleValue());
//					    cell9.setCellStyle(numberStyle);
//					} else {
//					    cell9.setCellValue("");
//					    cell9.setCellStyle(textStyle);
//					}
//
//					// Column10
//					cell10 = row.createCell(9);
//					if (record.getR46_7_12M_FD() != null) {
//					    cell10.setCellValue(record.getR46_7_12M_FD().doubleValue());
//					    cell10.setCellStyle(numberStyle);
//					} else {
//					    cell10.setCellValue("");
//					    cell10.setCellStyle(textStyle);
//					}
//
//					// Column11
//					cell11 = row.createCell(10);
//					if (record.getR46_13_18M_FD() != null) {
//					    cell11.setCellValue(record.getR46_13_18M_FD().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}
//
//					// Column12
//					cell12 = row.createCell(11);
//					if (record.getR46_19_24M_FD() != null) {
//					    cell12.setCellValue(record.getR46_19_24M_FD().doubleValue());
//					    cell12.setCellStyle(numberStyle);
//					} else {
//					    cell12.setCellValue("");
//					    cell12.setCellStyle(textStyle);
//					}
//
//					// Column13
//					cell13 = row.createCell(12);
//					if (record.getR46_OVER24_FD() != null) {
//					    cell13.setCellValue(record.getR46_OVER24_FD().doubleValue());
//					    cell13.setCellStyle(numberStyle);
//					} else {
//					    cell13.setCellValue("");
//					    cell13.setCellStyle(textStyle);
//					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR46_TOTAL() != null) {
//					    cell14.setCellValue(record.getR46_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
//					cell15 = row.createCell(14);
//					if (record.getR46_NOACC() != null) {
//					    cell15.setCellValue(record.getR46_NOACC().doubleValue());
//					    cell15.setCellStyle(numberStyle);
//					} else {
//					    cell15.setCellValue("");
//					    cell15.setCellStyle(textStyle);
//					}

					// Assign Row 46
					row = sheet.getRow(46);

					
					// Row 47
					// Column2
					cell2 = row.createCell(1);
					if (record.getR47_CURRENT() != null) {
					    cell2.setCellValue(record.getR47_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR47_CALL() != null) {
					    cell3.setCellValue(record.getR47_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR47_SAVINGS() != null) {
					    cell4.setCellValue(record.getR47_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR47_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR47_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR47_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR47_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR47_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR47_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR47_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR47_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR47_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR47_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR47_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR47_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR47_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR47_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR47_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR47_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR47_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR47_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR47_TOTAL() != null) {
//					    cell14.setCellValue(record.getR47_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR47_NOACC() != null) {
					    cell15.setCellValue(record.getR47_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 47
//					row = sheet.getRow(47);
					
					
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
	
	
	
	public void updateReport(Q_SMME_DEP_Summary_Entity updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

		/*
		 * Q_SMME_DEP_Summary_Entity existing =
		 * BRRS_Q_SMME_DEP_Summary_Repo.findById(updatedEntity.getREPORT_DATE())
		 * .orElseThrow(() -> new RuntimeException( "Record not found for REPORT_DATE: "
		 * + updatedEntity.getREPORT_DATE()));
		 */
	    
	    Date reportDate = updatedEntity.getReport_date();
	    List<Q_SMME_DEP_Summary_Entity> existingList =
	    		BRRS_Q_SMME_DEP_Summary_Repo.getdatabydateList(reportDate);

	    Q_SMME_DEP_Summary_Entity existing;

	    if (!existingList.isEmpty()) {
	        existing = existingList.get(0);
	        System.out.println(" Existing record found for date: " + reportDate);
	    } else {
	        existing = new Q_SMME_DEP_Summary_Entity();
	        existing.setReport_date(reportDate);
	        System.out.println("⚠️ No record found — creating new entry for date: " + reportDate);
	    }

	    try {
	        //  Loop from R11 to R47, skipping R27–R31
	        for (int i = 11; i <= 47; i++) {
	            if (i >= 27 && i <= 31) {
	                continue; // skip rows 27, 28, 29, 30, 31
	            }

	            String prefix = "R" + i + "_";

	            String[] fields = {
	                "PRODUCT", "CURRENT", "CALL", "SAVINGS", "0_31D_NOTICE", "32_88D_NOTICE",
	                "91D_DEPOSIT", "1_2M_FD", "4_6M_FD", "7_12M_FD", "13_18M_FD",
	                "19_24M_FD", "OVER24_FD", "TOTAL", "NOACC"
	            };

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = Q_SMME_DEP_Summary_Entity.class.getMethod(getterName);
	                    Method setter = Q_SMME_DEP_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);
	                } catch (NoSuchMethodException e) {
	                    // Skip fields that don't exist
	                    continue;
	                }
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    //  Save updated entity
	    BRRS_Q_SMME_DEP_Summary_Repo.save(existing);
	}

	

	/*
	 * public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String
	 * todate, String currency, String dtltype, String type, String version) { try {
	 * logger.info("Generating Excel for BRRS_Q_SMME_DEP ARCHIVAL Details...");
	 * System.out.println("came to Detail download service"); if
	 * (type.equals("ARCHIVAL") & version != null) {
	 * 
	 * } XSSFWorkbook workbook = new XSSFWorkbook(); XSSFSheet sheet =
	 * workbook.createSheet("Q_SMME_DEPDetail");
	 * 
	 * // Common border style BorderStyle border = BorderStyle.THIN;
	 * 
	 * // Header style (left aligned) CellStyle headerStyle =
	 * workbook.createCellStyle(); Font headerFont = workbook.createFont();
	 * headerFont.setBold(true); headerFont.setFontHeightInPoints((short) 10);
	 * headerStyle.setFont(headerFont);
	 * headerStyle.setAlignment(HorizontalAlignment.LEFT);
	 * headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	 * headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	 * headerStyle.setBorderTop(border); headerStyle.setBorderBottom(border);
	 * headerStyle.setBorderLeft(border); headerStyle.setBorderRight(border);
	 * 
	 * // Right-aligned header style for ACCT BALANCE CellStyle
	 * rightAlignedHeaderStyle = workbook.createCellStyle();
	 * rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
	 * rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);
	 * 
	 * // Default data style (left aligned) CellStyle dataStyle =
	 * workbook.createCellStyle(); dataStyle.setAlignment(HorizontalAlignment.LEFT);
	 * dataStyle.setBorderTop(border); dataStyle.setBorderBottom(border);
	 * dataStyle.setBorderLeft(border); dataStyle.setBorderRight(border);
	 * 
	 * // ACCT BALANCE style (right aligned with 3 decimals) CellStyle balanceStyle
	 * = workbook.createCellStyle();
	 * balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
	 * balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
	 * balanceStyle.setBorderTop(border); balanceStyle.setBorderBottom(border);
	 * balanceStyle.setBorderLeft(border); balanceStyle.setBorderRight(border);
	 * 
	 * // Header row String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME",
	 * "ACCT BALANCE", "ROWID", "COLUMNID", "REPORT_DATE" };
	 * 
	 * XSSFRow headerRow = sheet.createRow(0); for (int i = 0; i < headers.length;
	 * i++) { Cell cell = headerRow.createCell(i); cell.setCellValue(headers[i]);
	 * 
	 * if (i == 3) { // ACCT BALANCE cell.setCellStyle(rightAlignedHeaderStyle); }
	 * else { cell.setCellStyle(headerStyle); }
	 * 
	 * sheet.setColumnWidth(i, 5000); }
	 * 
	 * // Get data Date parsedToDate = new
	 * SimpleDateFormat("dd/MM/yyyy").parse(todate);
	 * List<BRRS_Q_SMME_DEP_Archival_Detail_Entity> reportData =
	 * BRRS_Q_SMME_DEP_Archival_Detail_Repo .getdatabydateList(parsedToDate,
	 * version);
	 * 
	 * if (reportData != null && !reportData.isEmpty()) { int rowIndex = 1; for
	 * (BRRS_Q_SMME_DEP_Archival_Detail_Entity item : reportData) { XSSFRow row =
	 * sheet.createRow(rowIndex++);
	 * 
	 * row.createCell(0).setCellValue(item.getCUST_ID());
	 * row.createCell(1).setCellValue(item.getACCT_NUMBER());
	 * row.createCell(2).setCellValue(item.getACCT_NAME());
	 * 
	 * // ACCT BALANCE (right aligned, 3 decimal places) Cell balanceCell =
	 * row.createCell(3); if (item.getACCT_BALANCE_IN_PULA() != null) {
	 * balanceCell.setCellValue(item.getACCT_BALANCE_IN_PULA().doubleValue()); }
	 * else { balanceCell.setCellValue(0.000); }
	 * balanceCell.setCellStyle(balanceStyle);
	 * 
	 * row.createCell(4).setCellValue(item.getROW_ID());
	 * row.createCell(5).setCellValue(item.getCOLUMN_ID()); row.createCell(6)
	 * .setCellValue(item.getREPORT_DATE() != null ? new
	 * SimpleDateFormat("dd-MM-yyyy").format(item.getREPORT_DATE()) : "");
	 * 
	 * // Apply data style for all other cells for (int j = 0; j < 7; j++) { if (j
	 * != 3) { row.getCell(j).setCellStyle(dataStyle); } } } } else { logger.
	 * info("No data found for BRRS_Q_SMME_DEP — only header will be written."); }
	 * 
	 * // Write to byte[] ByteArrayOutputStream bos = new ByteArrayOutputStream();
	 * workbook.write(bos); workbook.close();
	 * 
	 * logger.info("Excel generation completed with {} row(s).", reportData != null
	 * ? reportData.size() : 0); return bos.toByteArray();
	 * 
	 * } catch (Exception e) { logger.error("Error generating BRRS_Q_SMME_DEPExcel",
	 * e); return new byte[0]; } }
	 */
	
	
	
	public List<Object[]> getQ_SMME_DEPResub() {
	    List<Object[]> resubList = new ArrayList<>();
	    try {
	        List<Q_SMME_DEP_Archival_Summary_Entity> latestArchivalList = 
	        		BRRS_Q_SMME_DEP_Archival_Summary_Repo.getdatabydateListWithVersionAll();

	        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
	            for (Q_SMME_DEP_Archival_Summary_Entity entity : latestArchivalList) {
	                Object[] row = new Object[] {
	                    entity.getReport_date(),
	                    entity.getReport_version()
	                };
	                resubList.add(row);
	            }
	            System.out.println("Fetched " + resubList.size() + " record(s)");
	        } else {
	            System.out.println("No archival data found.");
	        }
	    } catch (Exception e) {
	        System.err.println("Error fetching Q_SMME_DEP Resub data: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return resubList;
	}
	
	
	public void updateReportResub(Q_SMME_DEP_Summary_Entity updatedEntity) {
	    System.out.println("Came to Resub Service");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    // Use entity field directly (same name as in entity)
	    Date report_date = updatedEntity.getReport_date();
	    int newVersion = 1;

	    try {
	        // ✅ use the same variable name as in repo method
	        Optional<Q_SMME_DEP_Archival_Summary_Entity> latestArchivalOpt =
	        		BRRS_Q_SMME_DEP_Archival_Summary_Repo.getLatestArchivalVersionByDate(report_date);

	        // Determine next version
	        if (latestArchivalOpt.isPresent()) {
	        	Q_SMME_DEP_Archival_Summary_Entity latestArchival = latestArchivalOpt.get();
	            try {
	                newVersion = Integer.parseInt(latestArchival.getReport_version()) + 1;
	            } catch (NumberFormatException e) {
	                System.err.println("Invalid version format. Defaulting to version 1");
	                newVersion = 1;
	            }
	        } else {
	            System.out.println("No previous archival found for date: " + report_date);
	        }

	        // Prevent duplicate version
	        boolean exists = BRRS_Q_SMME_DEP_Archival_Summary_Repo
	                .findByReport_dateAndReport_version(report_date, String.valueOf(newVersion))
	                .isPresent();

	        if (exists) {
	            throw new RuntimeException("Version " + newVersion + " already exists for report date " + report_date);
	        }

	        // Copy summary entity to archival entity
	        Q_SMME_DEP_Archival_Summary_Entity archivalEntity = new Q_SMME_DEP_Archival_Summary_Entity();
	        org.springframework.beans.BeanUtils.copyProperties(updatedEntity, archivalEntity);

	        archivalEntity.setReport_date(report_date);
	        archivalEntity.setReport_version(String.valueOf(newVersion));
	        archivalEntity.setReportResubDate(new Date());

	        System.out.println("Saving new archival version: " + newVersion);
	        BRRS_Q_SMME_DEP_Archival_Summary_Repo.save(archivalEntity);

	        System.out.println("Saved archival version successfully: " + newVersion);

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error while creating archival resubmission record", e);
	    }
	}
	
	
	
	


	/// Downloaded for Archival & Resub
	public byte[] BRRS_Q_SMME_DEPResubExcel(String filename, String reportId, String fromdate,
        String todate, String currency, String dtltype,
        String type, String version) throws Exception {

    logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

    if (type.equals("RESUB") & version != null) {
       
    }

    List<Q_SMME_DEP_Archival_Summary_Entity> dataList =
    		BRRS_Q_SMME_DEP_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

    if (dataList.isEmpty()) {
        logger.warn("Service: No data found for M_CA4 report. Returning empty result.");
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

			int startRow =  10;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					Q_SMME_DEP_Archival_Summary_Entity  record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					
					
					//ROW11
					//COLUMN2
					Cell cell2 = row.createCell(1);
					if (record.getR11_CURRENT() != null) {
						cell2.setCellValue(record.getR11_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					//row11
					// Column3
					Cell cell3 = row.createCell(2);
					if (record.getR11_CALL() != null) {
						cell3.setCellValue(record.getR11_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row11
					// Column4
					Cell cell4 = row.createCell(3);
					if (record.getR11_SAVINGS() != null) {
						cell4.setCellValue(record.getR11_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					
					//row11
					// Column5
					Cell cell5 = row.createCell(4);
					if (record.getR11_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR11_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					//row11
					// Column6
					Cell cell6 = row.createCell(5);
					if (record.getR11_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR11_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					//row11
					// Column7
					Cell cell7 = row.createCell(6);
					if (record.getR11_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR11_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					//row11
					// Column8
					Cell cell8 = row.createCell(7);
					if (record.getR11_1_2M_FD() != null) {
						cell8.setCellValue(record.getR11_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					//row11
					// Column9
					Cell cell9 = row.createCell(8);
					if (record.getR11_4_6M_FD() != null) {
						cell9.setCellValue(record.getR11_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					//row11
					// Column10
					Cell cell10 = row.createCell(9);
					if (record.getR11_7_12M_FD() != null) {
						cell10.setCellValue(record.getR11_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					//row11
					// Column11
					Cell cell11 = row.createCell(10);
					if (record.getR11_13_18M_FD() != null) {
						cell11.setCellValue(record.getR11_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					//row11
					// Column12
					Cell cell12 = row.createCell(11);
					if (record.getR11_19_24M_FD() != null) {
						cell12.setCellValue(record.getR11_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					
					//row11
					// Column13
					Cell cell13 = row.createCell(12);
					if (record.getR11_OVER24_FD() != null) {
						cell13.setCellValue(record.getR11_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					//row11
					// Column14
//					Cell cell14 = row.createCell(13);
//					if (record.getR11_TOTAL() != null) {
//						cell14.setCellValue(record.getR11_TOTAL().doubleValue());
//						cell14.setCellStyle(numberStyle);
//					} else {
//						cell14.setCellValue("");
//						cell14.setCellStyle(textStyle);
//					}
					
					//row11
					// Column15
					Cell cell15 = row.createCell(14);
					if (record.getR11_NOACC() != null) {
						cell15.setCellValue(record.getR11_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}
		
					row = sheet.getRow(11);
				
					
					
					// Row 12
					// Column2
					cell2 = row.createCell(1);
					if (record.getR12_CURRENT() != null) {
					    cell2.setCellValue(record.getR12_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR12_CALL() != null) {
					    cell3.setCellValue(record.getR12_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR12_SAVINGS() != null) {
					    cell4.setCellValue(record.getR12_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR12_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR12_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR12_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR12_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR12_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR12_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR12_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR12_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR12_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR12_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR12_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR12_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR12_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR12_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR12_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR12_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR12_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR12_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR12_TOTAL() != null) {
//					    cell14.setCellValue(record.getR12_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR12_NOACC() != null) {
					    cell15.setCellValue(record.getR12_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					
					
					// Row 13
					// Column2
					cell2 = row.createCell(1);
					if (record.getR13_CURRENT() != null) {
					    cell2.setCellValue(record.getR13_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR13_CALL() != null) {
					    cell3.setCellValue(record.getR13_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR13_SAVINGS() != null) {
					    cell4.setCellValue(record.getR13_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR13_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR13_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR13_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR13_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR13_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR13_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR13_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR13_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR13_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR13_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR13_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR13_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR13_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR13_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR13_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR13_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR13_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR13_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR13_TOTAL() != null) {
//					    cell14.setCellValue(record.getR13_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR13_NOACC() != null) {
					    cell15.setCellValue(record.getR13_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);

					
					// Row 14
					// Column2
					cell2 = row.createCell(1);
					if (record.getR14_CURRENT() != null) {
					    cell2.setCellValue(record.getR14_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR14_CALL() != null) {
					    cell3.setCellValue(record.getR14_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR14_SAVINGS() != null) {
					    cell4.setCellValue(record.getR14_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR14_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR14_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR14_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR14_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR14_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR14_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR14_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR14_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR14_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR14_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR14_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR14_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR14_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR14_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR14_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR14_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR14_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR14_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR14_TOTAL() != null) {
//					    cell14.setCellValue(record.getR14_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR14_NOACC() != null) {
					    cell15.setCellValue(record.getR14_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 14
					row = sheet.getRow(14);

					
					// Row 15
					// Column2
					cell2 = row.createCell(1);
					if (record.getR15_CURRENT() != null) {
					    cell2.setCellValue(record.getR15_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR15_CALL() != null) {
					    cell3.setCellValue(record.getR15_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR15_SAVINGS() != null) {
					    cell4.setCellValue(record.getR15_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR15_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR15_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR15_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR15_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR15_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR15_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR15_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR15_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR15_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR15_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR15_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR15_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR15_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR15_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR15_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR15_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR15_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR15_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR15_TOTAL() != null) {
//					    cell14.setCellValue(record.getR15_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR15_NOACC() != null) {
					    cell15.setCellValue(record.getR15_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 15
					row = sheet.getRow(15);
					
					
					
					// Row 16
					// Column2
					cell2 = row.createCell(1);
					if (record.getR16_CURRENT() != null) {
					    cell2.setCellValue(record.getR16_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR16_CALL() != null) {
					    cell3.setCellValue(record.getR16_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR16_SAVINGS() != null) {
					    cell4.setCellValue(record.getR16_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR16_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR16_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR16_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR16_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR16_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR16_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR16_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR16_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR16_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR16_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR16_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR16_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR16_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR16_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR16_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR16_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR16_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR16_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR16_TOTAL() != null) {
//					    cell14.setCellValue(record.getR16_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR16_NOACC() != null) {
					    cell15.setCellValue(record.getR16_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 16
					row = sheet.getRow(16);

					
					// Row 17
					// Column2
					cell2 = row.createCell(1);
					if (record.getR17_CURRENT() != null) {
					    cell2.setCellValue(record.getR17_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR17_CALL() != null) {
					    cell3.setCellValue(record.getR17_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR17_SAVINGS() != null) {
					    cell4.setCellValue(record.getR17_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR17_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR17_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR17_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR17_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR17_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR17_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR17_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR17_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR17_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR17_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR17_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR17_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR17_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR17_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR17_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR17_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR17_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR17_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR17_TOTAL() != null) {
//					    cell14.setCellValue(record.getR17_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR17_NOACC() != null) {
					    cell15.setCellValue(record.getR17_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 17
					row = sheet.getRow(17);

					
					// Row 18
					// Column2
					cell2 = row.createCell(1);
					if (record.getR18_CURRENT() != null) {
					    cell2.setCellValue(record.getR18_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR18_CALL() != null) {
					    cell3.setCellValue(record.getR18_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR18_SAVINGS() != null) {
					    cell4.setCellValue(record.getR18_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR18_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR18_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR18_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR18_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR18_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR18_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR18_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR18_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR18_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR18_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR18_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR18_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR18_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR18_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR18_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR18_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR18_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR18_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR18_TOTAL() != null) {
//					    cell14.setCellValue(record.getR18_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR18_NOACC() != null) {
					    cell15.setCellValue(record.getR18_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 18
					row = sheet.getRow(18);

					
					
					// Row 19
					// Column2
					cell2 = row.createCell(1);
					if (record.getR19_CURRENT() != null) {
					    cell2.setCellValue(record.getR19_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR19_CALL() != null) {
					    cell3.setCellValue(record.getR19_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR19_SAVINGS() != null) {
					    cell4.setCellValue(record.getR19_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR19_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR19_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR19_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR19_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR19_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR19_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR19_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR19_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR19_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR19_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR19_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR19_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR19_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR19_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR19_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR19_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR19_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR19_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR19_TOTAL() != null) {
//					    cell14.setCellValue(record.getR19_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR19_NOACC() != null) {
					    cell15.setCellValue(record.getR19_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 19
					row = sheet.getRow(19);

					
					// Row 20
					// Column2
					cell2 = row.createCell(1);
					if (record.getR20_CURRENT() != null) {
					    cell2.setCellValue(record.getR20_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR20_CALL() != null) {
					    cell3.setCellValue(record.getR20_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR20_SAVINGS() != null) {
					    cell4.setCellValue(record.getR20_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR20_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR20_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR20_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR20_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR20_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR20_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR20_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR20_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR20_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR20_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR20_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR20_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR20_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR20_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR20_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR20_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR20_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR20_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR20_TOTAL() != null) {
//					    cell14.setCellValue(record.getR20_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR20_NOACC() != null) {
					    cell15.setCellValue(record.getR20_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 20
					row = sheet.getRow(20);

					
					// Row 21
					// Column2
					cell2 = row.createCell(1);
					if (record.getR21_CURRENT() != null) {
					    cell2.setCellValue(record.getR21_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR21_CALL() != null) {
					    cell3.setCellValue(record.getR21_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR21_SAVINGS() != null) {
					    cell4.setCellValue(record.getR21_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR21_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR21_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR21_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR21_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR21_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR21_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR21_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR21_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR21_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR21_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR21_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR21_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR21_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR21_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR21_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR21_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR21_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR21_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR21_TOTAL() != null) {
//					    cell14.setCellValue(record.getR21_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR21_NOACC() != null) {
					    cell15.setCellValue(record.getR21_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 21
					row = sheet.getRow(21);

					// Row 22
					// Column2
					cell2 = row.createCell(1);
					if (record.getR22_CURRENT() != null) {
					    cell2.setCellValue(record.getR22_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR22_CALL() != null) {
					    cell3.setCellValue(record.getR22_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR22_SAVINGS() != null) {
					    cell4.setCellValue(record.getR22_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR22_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR22_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR22_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR22_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR22_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR22_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR22_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR22_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR22_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR22_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR22_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR22_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR22_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR22_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR22_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR22_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR22_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR22_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR22_TOTAL() != null) {
//					    cell14.setCellValue(record.getR22_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR22_NOACC() != null) {
					    cell15.setCellValue(record.getR22_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 22
					row = sheet.getRow(22);
					
					
					// Row 23
					// Column2
					cell2 = row.createCell(1);
					if (record.getR23_CURRENT() != null) {
					    cell2.setCellValue(record.getR23_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR23_CALL() != null) {
					    cell3.setCellValue(record.getR23_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR23_SAVINGS() != null) {
					    cell4.setCellValue(record.getR23_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR23_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR23_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR23_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR23_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR23_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR23_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR23_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR23_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR23_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR23_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR23_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR23_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR23_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR23_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR23_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR23_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR23_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR23_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR23_TOTAL() != null) {
//					    cell14.setCellValue(record.getR23_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR23_NOACC() != null) {
					    cell15.setCellValue(record.getR23_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 23
					row = sheet.getRow(23);


					// Row 24
					// Column2
					cell2 = row.createCell(1);
					if (record.getR24_CURRENT() != null) {
					    cell2.setCellValue(record.getR24_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR24_CALL() != null) {
					    cell3.setCellValue(record.getR24_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR24_SAVINGS() != null) {
					    cell4.setCellValue(record.getR24_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR24_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR24_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR24_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR24_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR24_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR24_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR24_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR24_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR24_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR24_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR24_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR24_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR24_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR24_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR24_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR24_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR24_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR24_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR24_TOTAL() != null) {
//					    cell14.setCellValue(record.getR24_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR24_NOACC() != null) {
					    cell15.setCellValue(record.getR24_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 24
					row = sheet.getRow(24);


					// Row 25
					// Column2
//					cell2 = row.createCell(1);
//					if (record.getR25_CURRENT() != null) {
//					    cell2.setCellValue(record.getR25_CURRENT().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}
//
//					// Column3
//					cell3 = row.createCell(2);
//					if (record.getR25_CALL() != null) {
//					    cell3.setCellValue(record.getR25_CALL().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//
//					// Column4
//					cell4 = row.createCell(3);
//					if (record.getR25_SAVINGS() != null) {
//					    cell4.setCellValue(record.getR25_SAVINGS().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
//
//					// Column5
//					cell5 = row.createCell(4);
//					if (record.getR25_0_31D_NOTICE() != null) {
//					    cell5.setCellValue(record.getR25_0_31D_NOTICE().doubleValue());
//					    cell5.setCellStyle(numberStyle);
//					} else {
//					    cell5.setCellValue("");
//					    cell5.setCellStyle(textStyle);
//					}
//
//					// Column6
//					cell6 = row.createCell(5);
//					if (record.getR25_32_88D_NOTICE() != null) {
//					    cell6.setCellValue(record.getR25_32_88D_NOTICE().doubleValue());
//					    cell6.setCellStyle(numberStyle);
//					} else {
//					    cell6.setCellValue("");
//					    cell6.setCellStyle(textStyle);
//					}
//
//					// Column7
//					cell7 = row.createCell(6);
//					if (record.getR25_91D_DEPOSIT() != null) {
//					    cell7.setCellValue(record.getR25_91D_DEPOSIT().doubleValue());
//					    cell7.setCellStyle(numberStyle);
//					} else {
//					    cell7.setCellValue("");
//					    cell7.setCellStyle(textStyle);
//					}
//
//					// Column8
//					cell8 = row.createCell(7);
//					if (record.getR25_1_2M_FD() != null) {
//					    cell8.setCellValue(record.getR25_1_2M_FD().doubleValue());
//					    cell8.setCellStyle(numberStyle);
//					} else {
//					    cell8.setCellValue("");
//					    cell8.setCellStyle(textStyle);
//					}
//
//					// Column9
//					cell9 = row.createCell(8);
//					if (record.getR25_4_6M_FD() != null) {
//					    cell9.setCellValue(record.getR25_4_6M_FD().doubleValue());
//					    cell9.setCellStyle(numberStyle);
//					} else {
//					    cell9.setCellValue("");
//					    cell9.setCellStyle(textStyle);
//					}
//
//					// Column10
//					cell10 = row.createCell(9);
//					if (record.getR25_7_12M_FD() != null) {
//					    cell10.setCellValue(record.getR25_7_12M_FD().doubleValue());
//					    cell10.setCellStyle(numberStyle);
//					} else {
//					    cell10.setCellValue("");
//					    cell10.setCellStyle(textStyle);
//					}
//
//					// Column11
//					cell11 = row.createCell(10);
//					if (record.getR25_13_18M_FD() != null) {
//					    cell11.setCellValue(record.getR25_13_18M_FD().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}
//
//					// Column12
//					cell12 = row.createCell(11);
//					if (record.getR25_19_24M_FD() != null) {
//					    cell12.setCellValue(record.getR25_19_24M_FD().doubleValue());
//					    cell12.setCellStyle(numberStyle);
//					} else {
//					    cell12.setCellValue("");
//					    cell12.setCellStyle(textStyle);
//					}
//
//					// Column13
//					cell13 = row.createCell(12);
//					if (record.getR25_OVER24_FD() != null) {
//					    cell13.setCellValue(record.getR25_OVER24_FD().doubleValue());
//					    cell13.setCellStyle(numberStyle);
//					} else {
//					    cell13.setCellValue("");
//					    cell13.setCellStyle(textStyle);
//					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR25_TOTAL() != null) {
//					    cell14.setCellValue(record.getR25_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
//					cell15 = row.createCell(14);
//					if (record.getR25_NOACC() != null) {
//					    cell15.setCellValue(record.getR25_NOACC().doubleValue());
//					    cell15.setCellStyle(numberStyle);
//					} else {
//					    cell15.setCellValue("");
//					    cell15.setCellStyle(textStyle);
//					}

					// Assign Row 25
					row = sheet.getRow(25);



					// Row 26
					// Column2
					cell2 = row.createCell(1);
					if (record.getR26_CURRENT() != null) {
					    cell2.setCellValue(record.getR26_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR26_CALL() != null) {
					    cell3.setCellValue(record.getR26_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR26_SAVINGS() != null) {
					    cell4.setCellValue(record.getR26_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR26_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR26_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR26_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR26_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR26_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR26_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR26_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR26_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR26_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR26_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR26_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR26_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR26_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR26_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR26_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR26_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR26_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR26_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR26_TOTAL() != null) {
//					    cell14.setCellValue(record.getR26_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR26_NOACC() != null) {
					    cell15.setCellValue(record.getR26_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 26
					row = sheet.getRow(31);

					
					
					// Row 32
					// Column2
					cell2 = row.createCell(1);
					if (record.getR32_CURRENT() != null) {
						System.out.println(record.getR32_CURRENT());
					    cell2.setCellValue(record.getR32_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR32_CALL() != null) {
					    cell3.setCellValue(record.getR32_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR32_SAVINGS() != null) {
					    cell4.setCellValue(record.getR32_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR32_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR32_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR32_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR32_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR32_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR32_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR32_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR32_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR32_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR32_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR32_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR32_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR32_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR32_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR32_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR32_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR32_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR32_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR32_TOTAL() != null) {
//					    cell14.setCellValue(record.getR32_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR32_NOACC() != null) {
					    cell15.setCellValue(record.getR32_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 32
					row = sheet.getRow(32);


					// Row 33
					// Column2
					cell2 = row.createCell(1);
					if (record.getR33_CURRENT() != null) {
					    cell2.setCellValue(record.getR33_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR33_CALL() != null) {
					    cell3.setCellValue(record.getR33_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR33_SAVINGS() != null) {
					    cell4.setCellValue(record.getR33_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR33_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR33_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR33_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR33_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR33_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR33_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR33_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR33_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR33_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR33_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR33_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR33_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR33_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR33_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR33_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR33_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR33_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR33_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR33_TOTAL() != null) {
//					    cell14.setCellValue(record.getR33_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR33_NOACC() != null) {
					    cell15.setCellValue(record.getR33_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 33
					row = sheet.getRow(33);


					// Row 34
					// Column2
					cell2 = row.createCell(1);
					if (record.getR34_CURRENT() != null) {
					    cell2.setCellValue(record.getR34_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR34_CALL() != null) {
					    cell3.setCellValue(record.getR34_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR34_SAVINGS() != null) {
					    cell4.setCellValue(record.getR34_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR34_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR34_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR34_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR34_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR34_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR34_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR34_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR34_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR34_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR34_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR34_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR34_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR34_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR34_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR34_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR34_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR34_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR34_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR34_TOTAL() != null) {
//					    cell14.setCellValue(record.getR34_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR34_NOACC() != null) {
					    cell15.setCellValue(record.getR34_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 34
					row = sheet.getRow(34);

					
					// Row 35
					// Column2
					cell2 = row.createCell(1);
					if (record.getR35_CURRENT() != null) {
					    cell2.setCellValue(record.getR35_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR35_CALL() != null) {
					    cell3.setCellValue(record.getR35_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR35_SAVINGS() != null) {
					    cell4.setCellValue(record.getR35_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR35_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR35_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR35_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR35_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR35_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR35_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR35_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR35_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR35_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR35_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR35_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR35_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR35_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR35_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR35_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR35_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR35_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR35_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR35_TOTAL() != null) {
//					    cell14.setCellValue(record.getR35_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR35_NOACC() != null) {
					    cell15.setCellValue(record.getR35_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 35
					row = sheet.getRow(35);


					// Row 36
					// Column2
					cell2 = row.createCell(1);
					if (record.getR36_CURRENT() != null) {
					    cell2.setCellValue(record.getR36_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR36_CALL() != null) {
					    cell3.setCellValue(record.getR36_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR36_SAVINGS() != null) {
					    cell4.setCellValue(record.getR36_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR36_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR36_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR36_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR36_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR36_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR36_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR36_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR36_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR36_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR36_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR36_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR36_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR36_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR36_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR36_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR36_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR36_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR36_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR36_TOTAL() != null) {
//					    cell14.setCellValue(record.getR36_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR36_NOACC() != null) {
					    cell15.setCellValue(record.getR36_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 36
					row = sheet.getRow(36);


					// Row 37
					// Column2
					cell2 = row.createCell(1);
					if (record.getR37_CURRENT() != null) {
					    cell2.setCellValue(record.getR37_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR37_CALL() != null) {
					    cell3.setCellValue(record.getR37_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR37_SAVINGS() != null) {
					    cell4.setCellValue(record.getR37_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR37_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR37_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR37_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR37_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR37_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR37_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR37_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR37_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR37_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR37_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR37_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR37_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR37_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR37_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR37_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR37_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR37_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR37_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR37_TOTAL() != null) {
//					    cell14.setCellValue(record.getR37_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR37_NOACC() != null) {
					    cell15.setCellValue(record.getR37_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 37
					row = sheet.getRow(37);



					// Row 38
					// Column2
					cell2 = row.createCell(1);
					if (record.getR38_CURRENT() != null) {
					    cell2.setCellValue(record.getR38_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR38_CALL() != null) {
					    cell3.setCellValue(record.getR38_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR38_SAVINGS() != null) {
					    cell4.setCellValue(record.getR38_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR38_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR38_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR38_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR38_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR38_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR38_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR38_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR38_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR38_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR38_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR38_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR38_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR38_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR38_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR38_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR38_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR38_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR38_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR38_TOTAL() != null) {
//					    cell14.setCellValue(record.getR38_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR38_NOACC() != null) {
					    cell15.setCellValue(record.getR38_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 38
					row = sheet.getRow(38);


					// Row 39
					// Column2
					cell2 = row.createCell(1);
					if (record.getR39_CURRENT() != null) {
					    cell2.setCellValue(record.getR39_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR39_CALL() != null) {
					    cell3.setCellValue(record.getR39_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR39_SAVINGS() != null) {
					    cell4.setCellValue(record.getR39_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR39_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR39_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR39_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR39_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR39_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR39_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR39_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR39_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR39_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR39_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR39_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR39_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR39_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR39_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR39_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR39_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR39_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR39_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR39_TOTAL() != null) {
//					    cell14.setCellValue(record.getR39_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR39_NOACC() != null) {
					    cell15.setCellValue(record.getR39_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 39
					row = sheet.getRow(39);


					// Row 40
					// Column2
					cell2 = row.createCell(1);
					if (record.getR40_CURRENT() != null) {
					    cell2.setCellValue(record.getR40_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR40_CALL() != null) {
					    cell3.setCellValue(record.getR40_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR40_SAVINGS() != null) {
					    cell4.setCellValue(record.getR40_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR40_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR40_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR40_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR40_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR40_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR40_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR40_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR40_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR40_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR40_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR40_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR40_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR40_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR40_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR40_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR40_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR40_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR40_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR40_TOTAL() != null) {
//					    cell14.setCellValue(record.getR40_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR40_NOACC() != null) {
					    cell15.setCellValue(record.getR40_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 40
					row = sheet.getRow(40);

					
					// Row 41
					// Column2
					cell2 = row.createCell(1);
					if (record.getR41_CURRENT() != null) {
					    cell2.setCellValue(record.getR41_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR41_CALL() != null) {
					    cell3.setCellValue(record.getR41_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR41_SAVINGS() != null) {
					    cell4.setCellValue(record.getR41_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR41_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR41_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR41_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR41_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR41_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR41_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR41_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR41_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR41_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR41_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR41_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR41_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR41_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR41_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR41_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR41_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR41_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR41_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR41_TOTAL() != null) {
//					    cell14.setCellValue(record.getR41_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR41_NOACC() != null) {
					    cell15.setCellValue(record.getR41_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 41
					row = sheet.getRow(41);


					// Row 42
					// Column2
					cell2 = row.createCell(1);
					if (record.getR42_CURRENT() != null) {
					    cell2.setCellValue(record.getR42_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR42_CALL() != null) {
					    cell3.setCellValue(record.getR42_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR42_SAVINGS() != null) {
					    cell4.setCellValue(record.getR42_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR42_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR42_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR42_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR42_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR42_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR42_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR42_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR42_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR42_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR42_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR42_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR42_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR42_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR42_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR42_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR42_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR42_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR42_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR42_TOTAL() != null) {
//					    cell14.setCellValue(record.getR42_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR42_NOACC() != null) {
					    cell15.setCellValue(record.getR42_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 42
					row = sheet.getRow(42);


					// Row 43
					// Column2
					cell2 = row.createCell(1);
					if (record.getR43_CURRENT() != null) {
					    cell2.setCellValue(record.getR43_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR43_CALL() != null) {
					    cell3.setCellValue(record.getR43_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR43_SAVINGS() != null) {
					    cell4.setCellValue(record.getR43_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR43_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR43_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR43_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR43_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR43_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR43_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR43_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR43_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR43_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR43_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR43_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR43_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR43_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR43_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR43_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR43_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR43_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR43_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR43_TOTAL() != null) {
//					    cell14.setCellValue(record.getR43_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR43_NOACC() != null) {
					    cell15.setCellValue(record.getR43_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 43
					row = sheet.getRow(43);


					// Row 44
					// Column2
					cell2 = row.createCell(1);
					if (record.getR44_CURRENT() != null) {
					    cell2.setCellValue(record.getR44_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR44_CALL() != null) {
					    cell3.setCellValue(record.getR44_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR44_SAVINGS() != null) {
					    cell4.setCellValue(record.getR44_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR44_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR44_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR44_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR44_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR44_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR44_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR44_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR44_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR44_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR44_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR44_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR44_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR44_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR44_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR44_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR44_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR44_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR44_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR44_TOTAL() != null) {
//					    cell14.setCellValue(record.getR44_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR44_NOACC() != null) {
					    cell15.setCellValue(record.getR44_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 44
					row = sheet.getRow(44);


					// Row 45
					// Column2
					cell2 = row.createCell(1);
					if (record.getR45_CURRENT() != null) {
					    cell2.setCellValue(record.getR45_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR45_CALL() != null) {
					    cell3.setCellValue(record.getR45_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR45_SAVINGS() != null) {
					    cell4.setCellValue(record.getR45_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR45_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR45_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR45_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR45_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR45_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR45_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR45_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR45_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR45_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR45_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR45_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR45_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR45_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR45_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR45_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR45_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR45_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR45_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR45_TOTAL() != null) {
//					    cell14.setCellValue(record.getR45_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR45_NOACC() != null) {
					    cell15.setCellValue(record.getR45_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
					}

					// Assign Row 45
					row = sheet.getRow(45);


					// Row 46
					// Column2
//					cell2 = row.createCell(1);
//					if (record.getR46_CURRENT() != null) {
//					    cell2.setCellValue(record.getR46_CURRENT().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}
//
//					// Column3
//					cell3 = row.createCell(2);
//					if (record.getR46_CALL() != null) {
//					    cell3.setCellValue(record.getR46_CALL().doubleValue());
//					    cell3.setCellStyle(numberStyle);
//					} else {
//					    cell3.setCellValue("");
//					    cell3.setCellStyle(textStyle);
//					}
//
//					// Column4
//					cell4 = row.createCell(3);
//					if (record.getR46_SAVINGS() != null) {
//					    cell4.setCellValue(record.getR46_SAVINGS().doubleValue());
//					    cell4.setCellStyle(numberStyle);
//					} else {
//					    cell4.setCellValue("");
//					    cell4.setCellStyle(textStyle);
//					}
//
//					// Column5
//					cell5 = row.createCell(4);
//					if (record.getR46_0_31D_NOTICE() != null) {
//					    cell5.setCellValue(record.getR46_0_31D_NOTICE().doubleValue());
//					    cell5.setCellStyle(numberStyle);
//					} else {
//					    cell5.setCellValue("");
//					    cell5.setCellStyle(textStyle);
//					}
//
//					// Column6
//					cell6 = row.createCell(5);
//					if (record.getR46_32_88D_NOTICE() != null) {
//					    cell6.setCellValue(record.getR46_32_88D_NOTICE().doubleValue());
//					    cell6.setCellStyle(numberStyle);
//					} else {
//					    cell6.setCellValue("");
//					    cell6.setCellStyle(textStyle);
//					}
//
//					// Column7
//					cell7 = row.createCell(6);
//					if (record.getR46_91D_DEPOSIT() != null) {
//					    cell7.setCellValue(record.getR46_91D_DEPOSIT().doubleValue());
//					    cell7.setCellStyle(numberStyle);
//					} else {
//					    cell7.setCellValue("");
//					    cell7.setCellStyle(textStyle);
//					}
//
//					// Column8
//					cell8 = row.createCell(7);
//					if (record.getR46_1_2M_FD() != null) {
//					    cell8.setCellValue(record.getR46_1_2M_FD().doubleValue());
//					    cell8.setCellStyle(numberStyle);
//					} else {
//					    cell8.setCellValue("");
//					    cell8.setCellStyle(textStyle);
//					}
//
//					// Column9
//					cell9 = row.createCell(8);
//					if (record.getR46_4_6M_FD() != null) {
//					    cell9.setCellValue(record.getR46_4_6M_FD().doubleValue());
//					    cell9.setCellStyle(numberStyle);
//					} else {
//					    cell9.setCellValue("");
//					    cell9.setCellStyle(textStyle);
//					}
//
//					// Column10
//					cell10 = row.createCell(9);
//					if (record.getR46_7_12M_FD() != null) {
//					    cell10.setCellValue(record.getR46_7_12M_FD().doubleValue());
//					    cell10.setCellStyle(numberStyle);
//					} else {
//					    cell10.setCellValue("");
//					    cell10.setCellStyle(textStyle);
//					}
//
//					// Column11
//					cell11 = row.createCell(10);
//					if (record.getR46_13_18M_FD() != null) {
//					    cell11.setCellValue(record.getR46_13_18M_FD().doubleValue());
//					    cell11.setCellStyle(numberStyle);
//					} else {
//					    cell11.setCellValue("");
//					    cell11.setCellStyle(textStyle);
//					}
//
//					// Column12
//					cell12 = row.createCell(11);
//					if (record.getR46_19_24M_FD() != null) {
//					    cell12.setCellValue(record.getR46_19_24M_FD().doubleValue());
//					    cell12.setCellStyle(numberStyle);
//					} else {
//					    cell12.setCellValue("");
//					    cell12.setCellStyle(textStyle);
//					}
//
//					// Column13
//					cell13 = row.createCell(12);
//					if (record.getR46_OVER24_FD() != null) {
//					    cell13.setCellValue(record.getR46_OVER24_FD().doubleValue());
//					    cell13.setCellStyle(numberStyle);
//					} else {
//					    cell13.setCellValue("");
//					    cell13.setCellStyle(textStyle);
//					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR46_TOTAL() != null) {
//					    cell14.setCellValue(record.getR46_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
//					cell15 = row.createCell(14);
//					if (record.getR46_NOACC() != null) {
//					    cell15.setCellValue(record.getR46_NOACC().doubleValue());
//					    cell15.setCellStyle(numberStyle);
//					} else {
//					    cell15.setCellValue("");
//					    cell15.setCellStyle(textStyle);
//					}

					// Assign Row 46
					row = sheet.getRow(46);

					
					// Row 47
					// Column2
					cell2 = row.createCell(1);
					if (record.getR47_CURRENT() != null) {
					    cell2.setCellValue(record.getR47_CURRENT().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR47_CALL() != null) {
					    cell3.setCellValue(record.getR47_CALL().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR47_SAVINGS() != null) {
					    cell4.setCellValue(record.getR47_SAVINGS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR47_0_31D_NOTICE() != null) {
					    cell5.setCellValue(record.getR47_0_31D_NOTICE().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR47_32_88D_NOTICE() != null) {
					    cell6.setCellValue(record.getR47_32_88D_NOTICE().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR47_91D_DEPOSIT() != null) {
					    cell7.setCellValue(record.getR47_91D_DEPOSIT().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR47_1_2M_FD() != null) {
					    cell8.setCellValue(record.getR47_1_2M_FD().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR47_4_6M_FD() != null) {
					    cell9.setCellValue(record.getR47_4_6M_FD().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR47_7_12M_FD() != null) {
					    cell10.setCellValue(record.getR47_7_12M_FD().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR47_13_18M_FD() != null) {
					    cell11.setCellValue(record.getR47_13_18M_FD().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR47_19_24M_FD() != null) {
					    cell12.setCellValue(record.getR47_19_24M_FD().doubleValue());
					    cell12.setCellStyle(numberStyle);
					} else {
					    cell12.setCellValue("");
					    cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR47_OVER24_FD() != null) {
					    cell13.setCellValue(record.getR47_OVER24_FD().doubleValue());
					    cell13.setCellStyle(numberStyle);
					} else {
					    cell13.setCellValue("");
					    cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR47_TOTAL() != null) {
//					    cell14.setCellValue(record.getR47_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR47_NOACC() != null) {
					    cell15.setCellValue(record.getR47_NOACC().doubleValue());
					    cell15.setCellStyle(numberStyle);
					} else {
					    cell15.setCellValue("");
					    cell15.setCellStyle(textStyle);
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



}
