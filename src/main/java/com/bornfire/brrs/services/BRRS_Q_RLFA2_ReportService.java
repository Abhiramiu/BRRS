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
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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

import com.bornfire.brrs.entities.BRRS_Q_RLFA2_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_Q_RLFA2_Summary_Repo;
import com.bornfire.brrs.entities.M_CA4_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_CA4_Summary_Entity;
import com.bornfire.brrs.entities.Q_RLFA2_Archival_Summary_Entity;
import com.bornfire.brrs.entities.Q_RLFA2_Summary_Entity;

@Component
@Service

public class BRRS_Q_RLFA2_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_Q_RLFA2_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;


	@Autowired
	BRRS_Q_RLFA2_Summary_Repo brrs_q_rlfa2_summary_repo;

	@Autowired
	BRRS_Q_RLFA2_Archival_Summary_Repo brrs_q_rlfa2_archival_summary_repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	

	
	  
	/*
	 * public ModelAndView getQ_RLFA2View(String reportId, String fromdate, String
	 * todate, String currency, String dtltype, Pageable pageable, String type,
	 * String version) {
	 * 
	 * ModelAndView mv = new ModelAndView(); Session hs =
	 * sessionFactory.getCurrentSession();
	 * 
	 * int pageSize = pageable.getPageSize(); int currentPage =
	 * pageable.getPageNumber(); int startItem = currentPage * pageSize;
	 * 
	 * if (type.equals("ARCHIVAL") & version != null) {
	 * 
	 * List<Q_RLFA2_Archival_Summary_Entity> T1Master = new
	 * ArrayList<Q_RLFA2_Archival_Summary_Entity>(); try { Date d1 =
	 * dateformat.parse(todate);
	 * 
	 * 
	 * T1Master =
	 * brrs_q_rlfa2_archival_summary_repo.getdatabydateListarchival(dateformat.parse
	 * (todate), version);
	 * 
	 * } catch (ParseException e) { e.printStackTrace(); }
	 * 
	 * mv.addObject("reportsummary", T1Master); } else {
	 * List<Q_RLFA2_Summary_Entity> T1Master = new
	 * ArrayList<Q_RLFA2_Summary_Entity>();
	 * 
	 * try { Date d1 = dateformat.parse(todate);
	 * 
	 * T1Master =
	 * brrs_q_rlfa2_summary_repo.getdatabydateList(dateformat.parse(todate));
	 * 
	 * 
	 * 
	 * 
	 * System.out.println("Size of t1master is :"+T1Master.size());
	 * 
	 * 
	 * } catch (ParseException e) { e.printStackTrace(); }
	 * mv.addObject("reportsummary", T1Master); }
	 * 
	 * 
	 * 
	 * mv.setViewName("BRRS/Q_RLFA2"); mv.addObject("displaymode", "summary");
	 * System.out.println("scv" + mv.getViewName());
	 * 
	 * 
	 * return mv;
	 * 
	 * }
	 */
	  
	  public ModelAndView getQ_RLFA2View(String reportId, String fromdate, String
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
			  List<Q_RLFA2_Archival_Summary_Entity> T1Master =
					  brrs_q_rlfa2_archival_summary_repo.getdatabydateListarchival(d1, version);
			  
			  mv.addObject("reportsummary", T1Master); }
			  
			  // ---------- CASE 2: RESUB ---------- 
			  else if
			  ("RESUB".equalsIgnoreCase(type) && version != null) {
			  List<Q_RLFA2_Archival_Summary_Entity> T1Master =
					  brrs_q_rlfa2_archival_summary_repo.getdatabydateListarchival(d1, version);
			  
			  mv.addObject("reportsummary", T1Master); }
			  
			  // ---------- CASE 3: NORMAL ---------- 
			  else { List<Q_RLFA2_Summary_Entity>
			  T1Master =
					  brrs_q_rlfa2_summary_repo.getdatabydateListWithVersion(todate);
			  System.out.println("T1Master Size "+T1Master.size());
			  mv.addObject("reportsummary", T1Master); }
			  
			  } catch (ParseException e) { e.printStackTrace(); }
			  
			  mv.setViewName("BRRS/Q_RLFA2"); mv.addObject("displaymode", "summary");
			  System.out.println("View set to: " + mv.getViewName()); return mv; }

	 
	

	  
	  
	  
	  
		/*
		 * public void updateReport(Q_RLFA2_Summary_Entity updatedEntity) {
		 * System.out.println("Came to services"); System.out.println("Report Date: " +
		 * updatedEntity.getReport_date());
		 * 
		 * Q_RLFA2_Summary_Entity existing =
		 * brrs_q_rlfa2_summary_repo.findById(updatedEntity.getReport_date())
		 * .orElseThrow(() -> new RuntimeException( "Record not found for REPORT_DATE: "
		 * + updatedEntity.getReport_date()));
		 * 
		 * try { // 1️⃣ Loop from R10 to R63 and copy fields
		 * 
		 * for (int i = 10; i <= 63; i++) {
		 * 
		 * String prefix = "R" + i + "_";
		 * 
		 * String[] fields = { "sche_fore_ass", "orig_amt", "fore_amt", "no_of_acc" };
		 * 
		 * for (String field : fields) { String getterName = "get" + prefix + field; //
		 * e.g., getR10_orig_amt String setterName = "set" + prefix + field; // e.g.,
		 * setR10_orig_amt
		 * 
		 * try { Method getter = Q_RLFA2_Summary_Entity.class.getMethod(getterName);
		 * Method setter = Q_RLFA2_Summary_Entity.class.getMethod(setterName,
		 * getter.getReturnType());
		 * 
		 * Object newValue = getter.invoke(updatedEntity); setter.invoke(existing,
		 * newValue);
		 * 
		 * } catch (NoSuchMethodException e) { // Skip missing fields continue; } } }
		 * 
		 * 
		 * 
		 * } catch (Exception e) { throw new
		 * RuntimeException("Error while updating report fields", e); }
		 * 
		 * // 3️⃣ Save updated entity brrs_q_rlfa2_summary_repo.save(existing); }
		 */
	  
	  
	  
	  public byte[] getQ_RLFA2Excel(String filename, String reportId, String fromdate, String todate, String currency,
				String dtltype, String type, String version) throws Exception {
			logger.info("Service: Starting Excel generation process in memory.");

			// ARCHIVAL check
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
				logger.info("Service: Generating ARCHIVAL report for version {}", version);
				return getExcelQ_RLFA2ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			}

			else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			    logger.info("Service: Generating RESUB report for version {}", version);

			    try {
			        // ✅ Use pattern matching "31-Jul-2025"
			        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
			        Date report_date = sdf.parse(fromdate);  // or use asondate if that's your date source

			        List<Q_RLFA2_Archival_Summary_Entity> T1Master =
			        		brrs_q_rlfa2_archival_summary_repo.getdatabydateListarchival(report_date, version);

			        // ✅ Generate Excel
			        return BRRS_Q_RLFA2ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

			    } catch (ParseException e) {
			        logger.error("Invalid report date format: {}", fromdate, e);
			        throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			    }
			}
			
			
			
			// Fetch data

			List<Q_RLFA2_Summary_Entity> dataList = brrs_q_rlfa2_summary_repo.getdatabydateList(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for Q_RLFA2 report. Returning empty result.");
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
						Q_RLFA2_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						// row11
						// Column B2 - Original Amount 
						Cell cellB = row.createCell(1);
						if (record.getR11_orig_amt() != null) {
							cellB.setCellValue(record.getR11_orig_amt().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// row11
					    // Column C3 - Foreclosure Amount
						Cell cellC = row.createCell(2);
						if (record.getR11_fore_amt() != null) {
							cellC.setCellValue(record.getR11_fore_amt().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// row11
			            // Column D4 - No. of Accounts
						Cell cellE = row.createCell(3);
						if (record.getR11_no_of_acc() != null) {
							cellE.setCellValue(record.getR11_no_of_acc().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// row12
						row = sheet.getRow(11);
						
						// row12
						// Column B2 - Original Amount 
						 cellB = row.createCell(1);
						if (record.getR12_orig_amt() != null) {
						    cellB.setCellValue(record.getR12_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row12
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR12_fore_amt() != null) {
						    cellC.setCellValue(record.getR12_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row12
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR12_no_of_acc() != null) {
						    cellE.setCellValue(record.getR12_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						

						// row13
						row = sheet.getRow(12);

						// row13
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR13_orig_amt() != null) {
						    cellB.setCellValue(record.getR13_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row13
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR13_fore_amt() != null) {
						    cellC.setCellValue(record.getR13_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row13
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR13_no_of_acc() != null) {
						    cellE.setCellValue(record.getR13_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						
						// row15
						row = sheet.getRow(14);

						// row15
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR15_orig_amt() != null) {
						    cellB.setCellValue(record.getR15_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row15
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR15_fore_amt() != null) {
						    cellC.setCellValue(record.getR15_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row15
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR15_no_of_acc() != null) {
						    cellE.setCellValue(record.getR15_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row16
						row = sheet.getRow(15);

						// row16
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR16_orig_amt() != null) {
						    cellB.setCellValue(record.getR16_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row16
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR16_fore_amt() != null) {
						    cellC.setCellValue(record.getR16_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row16
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR16_no_of_acc() != null) {
						    cellE.setCellValue(record.getR16_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row17
						row = sheet.getRow(16);

						// row17
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR17_orig_amt() != null) {
						    cellB.setCellValue(record.getR17_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row17
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR17_fore_amt() != null) {
						    cellC.setCellValue(record.getR17_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row17
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR17_no_of_acc() != null) {
						    cellE.setCellValue(record.getR17_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row18
						row = sheet.getRow(17);

						// row18
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR18_orig_amt() != null) {
						    cellB.setCellValue(record.getR18_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row18
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR18_fore_amt() != null) {
						    cellC.setCellValue(record.getR18_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row18
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR18_no_of_acc() != null) {
						    cellE.setCellValue(record.getR18_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row19
						row = sheet.getRow(18);

						// row19
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR19_orig_amt() != null) {
						    cellB.setCellValue(record.getR19_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row19
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR19_fore_amt() != null) {
						    cellC.setCellValue(record.getR19_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row19
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR19_no_of_acc() != null) {
						    cellE.setCellValue(record.getR19_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row20
						row = sheet.getRow(19);

						// row20
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR20_orig_amt() != null) {
						    cellB.setCellValue(record.getR20_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row20
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR20_fore_amt() != null) {
						    cellC.setCellValue(record.getR20_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row20
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR20_no_of_acc() != null) {
						    cellE.setCellValue(record.getR20_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row21
						row = sheet.getRow(20);

						// row21
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR21_orig_amt() != null) {
						    cellB.setCellValue(record.getR21_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row21
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR21_fore_amt() != null) {
						    cellC.setCellValue(record.getR21_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row21
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR21_no_of_acc() != null) {
						    cellE.setCellValue(record.getR21_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row22
						row = sheet.getRow(21);

						// row22
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR22_orig_amt() != null) {
						    cellB.setCellValue(record.getR22_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row22
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR22_fore_amt() != null) {
						    cellC.setCellValue(record.getR22_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row22
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR22_no_of_acc() != null) {
						    cellE.setCellValue(record.getR22_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row23
						row = sheet.getRow(22);

						// row23
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR23_orig_amt() != null) {
						    cellB.setCellValue(record.getR23_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row23
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR23_fore_amt() != null) {
						    cellC.setCellValue(record.getR23_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row23
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR23_no_of_acc() != null) {
						    cellE.setCellValue(record.getR23_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row24
						row = sheet.getRow(23);

						// row24
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR24_orig_amt() != null) {
						    cellB.setCellValue(record.getR24_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row24
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR24_fore_amt() != null) {
						    cellC.setCellValue(record.getR24_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row24
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR24_no_of_acc() != null) {
						    cellE.setCellValue(record.getR24_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row25
						row = sheet.getRow(24);

						// row25
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR25_orig_amt() != null) {
						    cellB.setCellValue(record.getR25_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row25
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR25_fore_amt() != null) {
						    cellC.setCellValue(record.getR25_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row25
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR25_no_of_acc() != null) {
						    cellE.setCellValue(record.getR25_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row26
						row = sheet.getRow(25);

						// row26
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR26_orig_amt() != null) {
						    cellB.setCellValue(record.getR26_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row26
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR26_fore_amt() != null) {
						    cellC.setCellValue(record.getR26_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row26
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR26_no_of_acc() != null) {
						    cellE.setCellValue(record.getR26_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row27
						row = sheet.getRow(26);

						// row27
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR27_orig_amt() != null) {
						    cellB.setCellValue(record.getR27_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row27
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR27_fore_amt() != null) {
						    cellC.setCellValue(record.getR27_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row27
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR27_no_of_acc() != null) {
						    cellE.setCellValue(record.getR27_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						// row29
						row = sheet.getRow(28);

						// row29
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR29_orig_amt() != null) {
						    cellB.setCellValue(record.getR29_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row29
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR29_fore_amt() != null) {
						    cellC.setCellValue(record.getR29_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row29
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR29_no_of_acc() != null) {
						    cellE.setCellValue(record.getR29_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row30
						row = sheet.getRow(29);

						// row30
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR30_orig_amt() != null) {
						    cellB.setCellValue(record.getR30_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row30
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR30_fore_amt() != null) {
						    cellC.setCellValue(record.getR30_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row30
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR30_no_of_acc() != null) {
						    cellE.setCellValue(record.getR30_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row31
						row = sheet.getRow(30);

						// row31
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR31_orig_amt() != null) {
						    cellB.setCellValue(record.getR31_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row31
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR31_fore_amt() != null) {
						    cellC.setCellValue(record.getR31_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row31
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR31_no_of_acc() != null) {
						    cellE.setCellValue(record.getR31_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row32
						row = sheet.getRow(31);

						// row32
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR32_orig_amt() != null) {
						    cellB.setCellValue(record.getR32_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row32
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR32_fore_amt() != null) {
						    cellC.setCellValue(record.getR32_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row32
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR32_no_of_acc() != null) {
						    cellE.setCellValue(record.getR32_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row33
						row = sheet.getRow(32);

						// row33
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR33_orig_amt() != null) {
						    cellB.setCellValue(record.getR33_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row33
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR33_fore_amt() != null) {
						    cellC.setCellValue(record.getR33_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row33
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR33_no_of_acc() != null) {
						    cellE.setCellValue(record.getR33_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row34
						row = sheet.getRow(33);

						// row34
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR34_orig_amt() != null) {
						    cellB.setCellValue(record.getR34_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row34
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR34_fore_amt() != null) {
						    cellC.setCellValue(record.getR34_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row34
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR34_no_of_acc() != null) {
						    cellE.setCellValue(record.getR34_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row35
						row = sheet.getRow(34);

						// row35
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR35_orig_amt() != null) {
						    cellB.setCellValue(record.getR35_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row35
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR35_fore_amt() != null) {
						    cellC.setCellValue(record.getR35_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row35
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR35_no_of_acc() != null) {
						    cellE.setCellValue(record.getR35_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row36
						row = sheet.getRow(35);

						// row36
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR36_orig_amt() != null) {
						    cellB.setCellValue(record.getR36_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row36
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR36_fore_amt() != null) {
						    cellC.setCellValue(record.getR36_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row36
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR36_no_of_acc() != null) {
						    cellE.setCellValue(record.getR36_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						// row38
						row = sheet.getRow(37);

						// row38
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR38_orig_amt() != null) {
						    cellB.setCellValue(record.getR38_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row38
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR38_fore_amt() != null) {
						    cellC.setCellValue(record.getR38_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row38
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR38_no_of_acc() != null) {
						    cellE.setCellValue(record.getR38_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row39
						row = sheet.getRow(38);

						// row39
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR39_orig_amt() != null) {
						    cellB.setCellValue(record.getR39_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row39
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR39_fore_amt() != null) {
						    cellC.setCellValue(record.getR39_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row39
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR39_no_of_acc() != null) {
						    cellE.setCellValue(record.getR39_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						// row41
						row = sheet.getRow(40);

						// row41
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR41_orig_amt() != null) {
						    cellB.setCellValue(record.getR41_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row41
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR41_fore_amt() != null) {
						    cellC.setCellValue(record.getR41_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row41
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR41_no_of_acc() != null) {
						    cellE.setCellValue(record.getR41_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row42
						row = sheet.getRow(41);

						// row42
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR42_orig_amt() != null) {
						    cellB.setCellValue(record.getR42_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row42
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR42_fore_amt() != null) {
						    cellC.setCellValue(record.getR42_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row42
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR42_no_of_acc() != null) {
						    cellE.setCellValue(record.getR42_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						// row44
						row = sheet.getRow(43);

						// row44
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR44_orig_amt() != null) {
						    cellB.setCellValue(record.getR44_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row44
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR44_fore_amt() != null) {
						    cellC.setCellValue(record.getR44_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row44
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR44_no_of_acc() != null) {
						    cellE.setCellValue(record.getR44_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row45
						row = sheet.getRow(44);

						// row45
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR45_orig_amt() != null) {
						    cellB.setCellValue(record.getR45_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row45
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR45_fore_amt() != null) {
						    cellC.setCellValue(record.getR45_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row45
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR45_no_of_acc() != null) {
						    cellE.setCellValue(record.getR45_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row46
						row = sheet.getRow(45);

						// row46
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR46_orig_amt() != null) {
						    cellB.setCellValue(record.getR46_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row46
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR46_fore_amt() != null) {
						    cellC.setCellValue(record.getR46_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row46
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR46_no_of_acc() != null) {
						    cellE.setCellValue(record.getR46_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row47
						row = sheet.getRow(46);

						// row47
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR47_orig_amt() != null) {
						    cellB.setCellValue(record.getR47_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row47
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR47_fore_amt() != null) {
						    cellC.setCellValue(record.getR47_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row47
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR47_no_of_acc() != null) {
						    cellE.setCellValue(record.getR47_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						// row49
						row = sheet.getRow(48);

						// row49
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR49_orig_amt() != null) {
						    cellB.setCellValue(record.getR49_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row49
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR49_fore_amt() != null) {
						    cellC.setCellValue(record.getR49_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row49
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR49_no_of_acc() != null) {
						    cellE.setCellValue(record.getR49_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row50
						row = sheet.getRow(49);

						// row50
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR50_orig_amt() != null) {
						    cellB.setCellValue(record.getR50_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row50
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR50_fore_amt() != null) {
						    cellC.setCellValue(record.getR50_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row50
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR50_no_of_acc() != null) {
						    cellE.setCellValue(record.getR50_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row51
						row = sheet.getRow(50);

						// row51
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR51_orig_amt() != null) {
						    cellB.setCellValue(record.getR51_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row51
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR51_fore_amt() != null) {
						    cellC.setCellValue(record.getR51_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row51
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR51_no_of_acc() != null) {
						    cellE.setCellValue(record.getR51_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						// row53
						row = sheet.getRow(52);

						// row53
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR53_orig_amt() != null) {
						    cellB.setCellValue(record.getR53_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row53
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR53_fore_amt() != null) {
						    cellC.setCellValue(record.getR53_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row53
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR53_no_of_acc() != null) {
						    cellE.setCellValue(record.getR53_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row54
						row = sheet.getRow(53);

						// row54
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR54_orig_amt() != null) {
						    cellB.setCellValue(record.getR54_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row54
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR54_fore_amt() != null) {
						    cellC.setCellValue(record.getR54_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row54
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR54_no_of_acc() != null) {
						    cellE.setCellValue(record.getR54_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row55
						row = sheet.getRow(54);

						// row55
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR55_orig_amt() != null) {
						    cellB.setCellValue(record.getR55_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row55
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR55_fore_amt() != null) {
						    cellC.setCellValue(record.getR55_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row55
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR55_no_of_acc() != null) {
						    cellE.setCellValue(record.getR55_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						
						// row57
						row = sheet.getRow(56);

						// row57
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR57_orig_amt() != null) {
						    cellB.setCellValue(record.getR57_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row57
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR57_fore_amt() != null) {
						    cellC.setCellValue(record.getR57_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row57
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR57_no_of_acc() != null) {
						    cellE.setCellValue(record.getR57_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row58
						row = sheet.getRow(57);

						// row58
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR58_orig_amt() != null) {
						    cellB.setCellValue(record.getR58_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row58
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR58_fore_amt() != null) {
						    cellC.setCellValue(record.getR58_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row58
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR58_no_of_acc() != null) {
						    cellE.setCellValue(record.getR58_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row59
						row = sheet.getRow(58);

						// row59
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR59_orig_amt() != null) {
						    cellB.setCellValue(record.getR59_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row59
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR59_fore_amt() != null) {
						    cellC.setCellValue(record.getR59_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row59
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR59_no_of_acc() != null) {
						    cellE.setCellValue(record.getR59_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row60
						row = sheet.getRow(59);

						// row60
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR60_orig_amt() != null) {
						    cellB.setCellValue(record.getR60_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row60
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR60_fore_amt() != null) {
						    cellC.setCellValue(record.getR60_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row60
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR60_no_of_acc() != null) {
						    cellE.setCellValue(record.getR60_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row61
						row = sheet.getRow(60);

						// row61
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR61_orig_amt() != null) {
						    cellB.setCellValue(record.getR61_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row61
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR61_fore_amt() != null) {
						    cellC.setCellValue(record.getR61_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row61
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR61_no_of_acc() != null) {
						    cellE.setCellValue(record.getR61_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row62
						row = sheet.getRow(61);

						// row62
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR62_orig_amt() != null) {
						    cellB.setCellValue(record.getR62_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row62
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR62_fore_amt() != null) {
						    cellC.setCellValue(record.getR62_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row62
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR62_no_of_acc() != null) {
						    cellE.setCellValue(record.getR62_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
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
	  
	  
	  
	  public byte[] getExcelQ_RLFA2ARCHIVAL(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, String version) throws Exception {

			logger.info("Service: Starting Excel generation process in memory.");

			if (type.equals("ARCHIVAL") & version != null) {

			}

			List<Q_RLFA2_Archival_Summary_Entity> dataList = brrs_q_rlfa2_archival_summary_repo
					.getdatabydateListarchival(dateformat.parse(todate), version);

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found forQ_RLFA2 report. Returning empty result.");
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
						Q_RLFA2_Archival_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						
						
						// row11
						// Column B2 - Original Amount 
						Cell cellB = row.createCell(1);
						if (record.getR11_orig_amt() != null) {
							cellB.setCellValue(record.getR11_orig_amt().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// row11
					    // Column C3 - Foreclosure Amount
						Cell cellC = row.createCell(2);
						if (record.getR11_fore_amt() != null) {
							cellC.setCellValue(record.getR11_fore_amt().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// row11
			            // Column D4 - No. of Accounts
						Cell cellE = row.createCell(3);
						if (record.getR11_no_of_acc() != null) {
							cellE.setCellValue(record.getR11_no_of_acc().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// row12
						row = sheet.getRow(11);
						
						// row12
						// Column B2 - Original Amount 
						 cellB = row.createCell(1);
						if (record.getR12_orig_amt() != null) {
						    cellB.setCellValue(record.getR12_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row12
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR12_fore_amt() != null) {
						    cellC.setCellValue(record.getR12_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row12
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR12_no_of_acc() != null) {
						    cellE.setCellValue(record.getR12_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						

						// row13
						row = sheet.getRow(12);

						// row13
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR13_orig_amt() != null) {
						    cellB.setCellValue(record.getR13_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row13
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR13_fore_amt() != null) {
						    cellC.setCellValue(record.getR13_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row13
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR13_no_of_acc() != null) {
						    cellE.setCellValue(record.getR13_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						
						// row15
						row = sheet.getRow(14);

						// row15
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR15_orig_amt() != null) {
						    cellB.setCellValue(record.getR15_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row15
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR15_fore_amt() != null) {
						    cellC.setCellValue(record.getR15_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row15
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR15_no_of_acc() != null) {
						    cellE.setCellValue(record.getR15_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row16
						row = sheet.getRow(15);

						// row16
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR16_orig_amt() != null) {
						    cellB.setCellValue(record.getR16_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row16
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR16_fore_amt() != null) {
						    cellC.setCellValue(record.getR16_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row16
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR16_no_of_acc() != null) {
						    cellE.setCellValue(record.getR16_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row17
						row = sheet.getRow(16);

						// row17
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR17_orig_amt() != null) {
						    cellB.setCellValue(record.getR17_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row17
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR17_fore_amt() != null) {
						    cellC.setCellValue(record.getR17_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row17
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR17_no_of_acc() != null) {
						    cellE.setCellValue(record.getR17_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row18
						row = sheet.getRow(17);

						// row18
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR18_orig_amt() != null) {
						    cellB.setCellValue(record.getR18_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row18
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR18_fore_amt() != null) {
						    cellC.setCellValue(record.getR18_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row18
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR18_no_of_acc() != null) {
						    cellE.setCellValue(record.getR18_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row19
						row = sheet.getRow(18);

						// row19
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR19_orig_amt() != null) {
						    cellB.setCellValue(record.getR19_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row19
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR19_fore_amt() != null) {
						    cellC.setCellValue(record.getR19_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row19
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR19_no_of_acc() != null) {
						    cellE.setCellValue(record.getR19_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row20
						row = sheet.getRow(19);

						// row20
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR20_orig_amt() != null) {
						    cellB.setCellValue(record.getR20_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row20
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR20_fore_amt() != null) {
						    cellC.setCellValue(record.getR20_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row20
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR20_no_of_acc() != null) {
						    cellE.setCellValue(record.getR20_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row21
						row = sheet.getRow(20);

						// row21
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR21_orig_amt() != null) {
						    cellB.setCellValue(record.getR21_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row21
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR21_fore_amt() != null) {
						    cellC.setCellValue(record.getR21_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row21
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR21_no_of_acc() != null) {
						    cellE.setCellValue(record.getR21_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row22
						row = sheet.getRow(21);

						// row22
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR22_orig_amt() != null) {
						    cellB.setCellValue(record.getR22_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row22
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR22_fore_amt() != null) {
						    cellC.setCellValue(record.getR22_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row22
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR22_no_of_acc() != null) {
						    cellE.setCellValue(record.getR22_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row23
						row = sheet.getRow(22);

						// row23
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR23_orig_amt() != null) {
						    cellB.setCellValue(record.getR23_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row23
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR23_fore_amt() != null) {
						    cellC.setCellValue(record.getR23_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row23
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR23_no_of_acc() != null) {
						    cellE.setCellValue(record.getR23_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row24
						row = sheet.getRow(23);

						// row24
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR24_orig_amt() != null) {
						    cellB.setCellValue(record.getR24_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row24
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR24_fore_amt() != null) {
						    cellC.setCellValue(record.getR24_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row24
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR24_no_of_acc() != null) {
						    cellE.setCellValue(record.getR24_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row25
						row = sheet.getRow(24);

						// row25
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR25_orig_amt() != null) {
						    cellB.setCellValue(record.getR25_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row25
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR25_fore_amt() != null) {
						    cellC.setCellValue(record.getR25_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row25
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR25_no_of_acc() != null) {
						    cellE.setCellValue(record.getR25_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row26
						row = sheet.getRow(25);

						// row26
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR26_orig_amt() != null) {
						    cellB.setCellValue(record.getR26_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row26
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR26_fore_amt() != null) {
						    cellC.setCellValue(record.getR26_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row26
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR26_no_of_acc() != null) {
						    cellE.setCellValue(record.getR26_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row27
						row = sheet.getRow(26);

						// row27
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR27_orig_amt() != null) {
						    cellB.setCellValue(record.getR27_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row27
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR27_fore_amt() != null) {
						    cellC.setCellValue(record.getR27_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row27
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR27_no_of_acc() != null) {
						    cellE.setCellValue(record.getR27_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						// row29
						row = sheet.getRow(28);

						// row29
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR29_orig_amt() != null) {
						    cellB.setCellValue(record.getR29_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row29
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR29_fore_amt() != null) {
						    cellC.setCellValue(record.getR29_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row29
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR29_no_of_acc() != null) {
						    cellE.setCellValue(record.getR29_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row30
						row = sheet.getRow(29);

						// row30
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR30_orig_amt() != null) {
						    cellB.setCellValue(record.getR30_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row30
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR30_fore_amt() != null) {
						    cellC.setCellValue(record.getR30_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row30
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR30_no_of_acc() != null) {
						    cellE.setCellValue(record.getR30_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row31
						row = sheet.getRow(30);

						// row31
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR31_orig_amt() != null) {
						    cellB.setCellValue(record.getR31_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row31
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR31_fore_amt() != null) {
						    cellC.setCellValue(record.getR31_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row31
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR31_no_of_acc() != null) {
						    cellE.setCellValue(record.getR31_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row32
						row = sheet.getRow(31);

						// row32
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR32_orig_amt() != null) {
						    cellB.setCellValue(record.getR32_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row32
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR32_fore_amt() != null) {
						    cellC.setCellValue(record.getR32_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row32
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR32_no_of_acc() != null) {
						    cellE.setCellValue(record.getR32_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row33
						row = sheet.getRow(32);

						// row33
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR33_orig_amt() != null) {
						    cellB.setCellValue(record.getR33_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row33
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR33_fore_amt() != null) {
						    cellC.setCellValue(record.getR33_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row33
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR33_no_of_acc() != null) {
						    cellE.setCellValue(record.getR33_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row34
						row = sheet.getRow(33);

						// row34
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR34_orig_amt() != null) {
						    cellB.setCellValue(record.getR34_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row34
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR34_fore_amt() != null) {
						    cellC.setCellValue(record.getR34_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row34
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR34_no_of_acc() != null) {
						    cellE.setCellValue(record.getR34_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row35
						row = sheet.getRow(34);

						// row35
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR35_orig_amt() != null) {
						    cellB.setCellValue(record.getR35_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row35
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR35_fore_amt() != null) {
						    cellC.setCellValue(record.getR35_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row35
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR35_no_of_acc() != null) {
						    cellE.setCellValue(record.getR35_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row36
						row = sheet.getRow(35);

						// row36
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR36_orig_amt() != null) {
						    cellB.setCellValue(record.getR36_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row36
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR36_fore_amt() != null) {
						    cellC.setCellValue(record.getR36_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row36
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR36_no_of_acc() != null) {
						    cellE.setCellValue(record.getR36_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						// row38
						row = sheet.getRow(37);

						// row38
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR38_orig_amt() != null) {
						    cellB.setCellValue(record.getR38_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row38
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR38_fore_amt() != null) {
						    cellC.setCellValue(record.getR38_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row38
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR38_no_of_acc() != null) {
						    cellE.setCellValue(record.getR38_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row39
						row = sheet.getRow(38);

						// row39
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR39_orig_amt() != null) {
						    cellB.setCellValue(record.getR39_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row39
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR39_fore_amt() != null) {
						    cellC.setCellValue(record.getR39_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row39
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR39_no_of_acc() != null) {
						    cellE.setCellValue(record.getR39_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						// row41
						row = sheet.getRow(40);

						// row41
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR41_orig_amt() != null) {
						    cellB.setCellValue(record.getR41_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row41
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR41_fore_amt() != null) {
						    cellC.setCellValue(record.getR41_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row41
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR41_no_of_acc() != null) {
						    cellE.setCellValue(record.getR41_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row42
						row = sheet.getRow(41);

						// row42
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR42_orig_amt() != null) {
						    cellB.setCellValue(record.getR42_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row42
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR42_fore_amt() != null) {
						    cellC.setCellValue(record.getR42_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row42
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR42_no_of_acc() != null) {
						    cellE.setCellValue(record.getR42_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						// row44
						row = sheet.getRow(43);

						// row44
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR44_orig_amt() != null) {
						    cellB.setCellValue(record.getR44_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row44
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR44_fore_amt() != null) {
						    cellC.setCellValue(record.getR44_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row44
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR44_no_of_acc() != null) {
						    cellE.setCellValue(record.getR44_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row45
						row = sheet.getRow(44);

						// row45
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR45_orig_amt() != null) {
						    cellB.setCellValue(record.getR45_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row45
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR45_fore_amt() != null) {
						    cellC.setCellValue(record.getR45_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row45
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR45_no_of_acc() != null) {
						    cellE.setCellValue(record.getR45_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row46
						row = sheet.getRow(45);

						// row46
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR46_orig_amt() != null) {
						    cellB.setCellValue(record.getR46_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row46
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR46_fore_amt() != null) {
						    cellC.setCellValue(record.getR46_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row46
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR46_no_of_acc() != null) {
						    cellE.setCellValue(record.getR46_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row47
						row = sheet.getRow(46);

						// row47
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR47_orig_amt() != null) {
						    cellB.setCellValue(record.getR47_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row47
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR47_fore_amt() != null) {
						    cellC.setCellValue(record.getR47_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row47
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR47_no_of_acc() != null) {
						    cellE.setCellValue(record.getR47_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						// row49
						row = sheet.getRow(48);

						// row49
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR49_orig_amt() != null) {
						    cellB.setCellValue(record.getR49_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row49
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR49_fore_amt() != null) {
						    cellC.setCellValue(record.getR49_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row49
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR49_no_of_acc() != null) {
						    cellE.setCellValue(record.getR49_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row50
						row = sheet.getRow(49);

						// row50
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR50_orig_amt() != null) {
						    cellB.setCellValue(record.getR50_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row50
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR50_fore_amt() != null) {
						    cellC.setCellValue(record.getR50_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row50
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR50_no_of_acc() != null) {
						    cellE.setCellValue(record.getR50_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row51
						row = sheet.getRow(50);

						// row51
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR51_orig_amt() != null) {
						    cellB.setCellValue(record.getR51_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row51
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR51_fore_amt() != null) {
						    cellC.setCellValue(record.getR51_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row51
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR51_no_of_acc() != null) {
						    cellE.setCellValue(record.getR51_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						// row53
						row = sheet.getRow(52);

						// row53
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR53_orig_amt() != null) {
						    cellB.setCellValue(record.getR53_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row53
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR53_fore_amt() != null) {
						    cellC.setCellValue(record.getR53_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row53
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR53_no_of_acc() != null) {
						    cellE.setCellValue(record.getR53_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row54
						row = sheet.getRow(53);

						// row54
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR54_orig_amt() != null) {
						    cellB.setCellValue(record.getR54_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row54
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR54_fore_amt() != null) {
						    cellC.setCellValue(record.getR54_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row54
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR54_no_of_acc() != null) {
						    cellE.setCellValue(record.getR54_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row55
						row = sheet.getRow(54);

						// row55
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR55_orig_amt() != null) {
						    cellB.setCellValue(record.getR55_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row55
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR55_fore_amt() != null) {
						    cellC.setCellValue(record.getR55_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row55
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR55_no_of_acc() != null) {
						    cellE.setCellValue(record.getR55_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						
						// row57
						row = sheet.getRow(56);

						// row57
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR57_orig_amt() != null) {
						    cellB.setCellValue(record.getR57_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row57
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR57_fore_amt() != null) {
						    cellC.setCellValue(record.getR57_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row57
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR57_no_of_acc() != null) {
						    cellE.setCellValue(record.getR57_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row58
						row = sheet.getRow(57);

						// row58
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR58_orig_amt() != null) {
						    cellB.setCellValue(record.getR58_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row58
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR58_fore_amt() != null) {
						    cellC.setCellValue(record.getR58_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row58
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR58_no_of_acc() != null) {
						    cellE.setCellValue(record.getR58_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row59
						row = sheet.getRow(58);

						// row59
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR59_orig_amt() != null) {
						    cellB.setCellValue(record.getR59_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row59
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR59_fore_amt() != null) {
						    cellC.setCellValue(record.getR59_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row59
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR59_no_of_acc() != null) {
						    cellE.setCellValue(record.getR59_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row60
						row = sheet.getRow(59);

						// row60
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR60_orig_amt() != null) {
						    cellB.setCellValue(record.getR60_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row60
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR60_fore_amt() != null) {
						    cellC.setCellValue(record.getR60_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row60
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR60_no_of_acc() != null) {
						    cellE.setCellValue(record.getR60_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row61
						row = sheet.getRow(60);

						// row61
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR61_orig_amt() != null) {
						    cellB.setCellValue(record.getR61_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row61
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR61_fore_amt() != null) {
						    cellC.setCellValue(record.getR61_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row61
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR61_no_of_acc() != null) {
						    cellE.setCellValue(record.getR61_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row62
						row = sheet.getRow(61);

						// row62
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR62_orig_amt() != null) {
						    cellB.setCellValue(record.getR62_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row62
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR62_fore_amt() != null) {
						    cellC.setCellValue(record.getR62_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row62
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR62_no_of_acc() != null) {
						    cellE.setCellValue(record.getR62_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
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
	  
	  
	  
		/*
		 * public List<Object> getQ_RLFA2Archival() { List<Object> Q_RLFA2Archivallist =
		 * new ArrayList<>(); try { Q_RLFA2Archivallist =
		 * brrs_q_rlfa2_archival_summary_repo.getQ_RLFA2archival();
		 * System.out.println("countser" + Q_RLFA2Archivallist.size()); } catch
		 * (Exception e) { // Log the exception
		 * System.err.println("Error fetching Q_RLFA2 Archival data: " +
		 * e.getMessage()); e.printStackTrace();
		 * 
		 * // Optionally, you can rethrow it or return empty list // throw new
		 * RuntimeException("Failed to fetch data", e); } return Q_RLFA2Archivallist; }
		 */
	
	
		public List<Object[]> getQ_RLFA2Archival() {
			List<Object[]> archivalList = new ArrayList<>();

			try {
				List<Q_RLFA2_Archival_Summary_Entity> repoData = brrs_q_rlfa2_archival_summary_repo
						.getdatabydateListWithVersionAll();

				if (repoData != null && !repoData.isEmpty()) {
					for (Q_RLFA2_Archival_Summary_Entity entity : repoData) {
						Object[] row = new Object[] {
								entity.getReport_date(), 
								entity.getReport_version() 
						};
						archivalList.add(row);
					}

					System.out.println("Fetched " + archivalList.size() + " archival records");
					Q_RLFA2_Archival_Summary_Entity first = repoData.get(0);
					System.out.println("Latest archival version: " + first.getReport_version());
				} else {
					System.out.println("No archival data found.");
				}

			} catch (Exception e) {
				System.err.println("Error fetching Q_RLFA2 Archival data: " + e.getMessage());
				e.printStackTrace();
			}

			return archivalList;
		}
		
	
	
	
		public void updateReport(Q_RLFA2_Summary_Entity updatedEntity) {
		    System.out.println("Came to services");
		    System.out.println("Report Date: " + updatedEntity.getReport_date());

		    Q_RLFA2_Summary_Entity existing = brrs_q_rlfa2_summary_repo.findById(updatedEntity.getReport_date())
		            .orElseThrow(() -> new RuntimeException(
		                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

		    try {
		        // 1️⃣ Loop from R10 to R63 and copy fields
		    	
		        for (int i = 10; i <= 63; i++) {
					
		        	 String prefix = "R" + i + "_";

		            String[] fields = { "sche_fore_ass", "orig_amt", "fore_amt", "no_of_acc" };

		            for (String field : fields) {
		            	   String getterName = "get" + prefix + field; // e.g., getR10_orig_amt
		                   String setterName = "set" + prefix + field; // e.g., setR10_orig_amt

		                try {
		                    Method getter = Q_RLFA2_Summary_Entity.class.getMethod(getterName);
		                    Method setter = Q_RLFA2_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

		                    Object newValue = getter.invoke(updatedEntity);
		                    setter.invoke(existing, newValue);

		                } catch (NoSuchMethodException e) {
		                    // Skip missing fields
		                    continue;
		                }
		            }
		        }

		       

		    } catch (Exception e) {
		        throw new RuntimeException("Error while updating report fields", e);
		    }

		    // 3️⃣ Save updated entity
		    brrs_q_rlfa2_summary_repo.save(existing);
		}
	
	
	
		public List<Object[]> getQ_RLFA2Resub() {
		    List<Object[]> resubList = new ArrayList<>();
		    try {
		        List<Q_RLFA2_Archival_Summary_Entity> latestArchivalList = 
		        		brrs_q_rlfa2_archival_summary_repo.getdatabydateListWithVersionAll();

		        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
		            for (Q_RLFA2_Archival_Summary_Entity entity : latestArchivalList) {
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
		        System.err.println("Error fetching M_CA4 Resub data: " + e.getMessage());
		        e.printStackTrace();
		    }
		    return resubList;
		}
		
		
		
		
		
		public void updateReportResub(Q_RLFA2_Summary_Entity updatedEntity) {
		    System.out.println("Came to Resub Service");
		    System.out.println("Report Date: " + updatedEntity.getReport_date());

		    // Use entity field directly (same name as in entity)
		    Date report_date = updatedEntity.getReport_date();
		    int newVersion = 1;

		    try {
		        // ✅ use the same variable name as in repo method
		        Optional<Q_RLFA2_Archival_Summary_Entity> latestArchivalOpt =
		        		brrs_q_rlfa2_archival_summary_repo.getLatestArchivalVersionByDate(report_date);

		        // Determine next version
		        if (latestArchivalOpt.isPresent()) {
		        	Q_RLFA2_Archival_Summary_Entity latestArchival = latestArchivalOpt.get();
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
		        boolean exists = brrs_q_rlfa2_archival_summary_repo
		                .findByReport_dateAndReport_version(report_date, String.valueOf(newVersion))
		                .isPresent();

		        if (exists) {
		            throw new RuntimeException("Version " + newVersion + " already exists for report date " + report_date);
		        }

		        // Copy summary entity to archival entity
		        Q_RLFA2_Archival_Summary_Entity archivalEntity = new Q_RLFA2_Archival_Summary_Entity();
		        org.springframework.beans.BeanUtils.copyProperties(updatedEntity, archivalEntity);

		        archivalEntity.setReport_date(report_date);
		        archivalEntity.setReport_version(String.valueOf(newVersion));
		        archivalEntity.setReportResubDate(new Date());

		        System.out.println("Saving new archival version: " + newVersion);
		        brrs_q_rlfa2_archival_summary_repo.save(archivalEntity);

		        System.out.println("Saved archival version successfully: " + newVersion);

		    } catch (Exception e) {
		        e.printStackTrace();
		        throw new RuntimeException("Error while creating archival resubmission record", e);
		    }
		}
		
	
	
		/// Downloaded for Archival & Resub
		public byte[] BRRS_Q_RLFA2ResubExcel(String filename, String reportId, String fromdate,
	        String todate, String currency, String dtltype,
	        String type, String version) throws Exception {

	    logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

	    if (type.equals("RESUB") & version != null) {
	       
	    }

	    List<Q_RLFA2_Archival_Summary_Entity> dataList =
	    		brrs_q_rlfa2_archival_summary_repo.getdatabydateListarchival(dateformat.parse(todate), version);

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

						Q_RLFA2_Archival_Summary_Entity  record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						
						
						// row11
						// Column B2 - Original Amount 
						Cell cellB = row.createCell(1);
						if (record.getR11_orig_amt() != null) {
							cellB.setCellValue(record.getR11_orig_amt().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// row11
					    // Column C3 - Foreclosure Amount
						Cell cellC = row.createCell(2);
						if (record.getR11_fore_amt() != null) {
							cellC.setCellValue(record.getR11_fore_amt().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// row11
			            // Column D4 - No. of Accounts
						Cell cellE = row.createCell(3);
						if (record.getR11_no_of_acc() != null) {
							cellE.setCellValue(record.getR11_no_of_acc().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// row12
						row = sheet.getRow(11);
						
						// row12
						// Column B2 - Original Amount 
						 cellB = row.createCell(1);
						if (record.getR12_orig_amt() != null) {
						    cellB.setCellValue(record.getR12_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row12
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR12_fore_amt() != null) {
						    cellC.setCellValue(record.getR12_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row12
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR12_no_of_acc() != null) {
						    cellE.setCellValue(record.getR12_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						

						// row13
						row = sheet.getRow(12);

						// row13
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR13_orig_amt() != null) {
						    cellB.setCellValue(record.getR13_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row13
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR13_fore_amt() != null) {
						    cellC.setCellValue(record.getR13_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row13
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR13_no_of_acc() != null) {
						    cellE.setCellValue(record.getR13_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						
						// row15
						row = sheet.getRow(14);

						// row15
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR15_orig_amt() != null) {
						    cellB.setCellValue(record.getR15_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row15
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR15_fore_amt() != null) {
						    cellC.setCellValue(record.getR15_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row15
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR15_no_of_acc() != null) {
						    cellE.setCellValue(record.getR15_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row16
						row = sheet.getRow(15);

						// row16
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR16_orig_amt() != null) {
						    cellB.setCellValue(record.getR16_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row16
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR16_fore_amt() != null) {
						    cellC.setCellValue(record.getR16_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row16
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR16_no_of_acc() != null) {
						    cellE.setCellValue(record.getR16_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row17
						row = sheet.getRow(16);

						// row17
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR17_orig_amt() != null) {
						    cellB.setCellValue(record.getR17_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row17
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR17_fore_amt() != null) {
						    cellC.setCellValue(record.getR17_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row17
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR17_no_of_acc() != null) {
						    cellE.setCellValue(record.getR17_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row18
						row = sheet.getRow(17);

						// row18
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR18_orig_amt() != null) {
						    cellB.setCellValue(record.getR18_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row18
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR18_fore_amt() != null) {
						    cellC.setCellValue(record.getR18_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row18
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR18_no_of_acc() != null) {
						    cellE.setCellValue(record.getR18_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row19
						row = sheet.getRow(18);

						// row19
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR19_orig_amt() != null) {
						    cellB.setCellValue(record.getR19_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row19
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR19_fore_amt() != null) {
						    cellC.setCellValue(record.getR19_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row19
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR19_no_of_acc() != null) {
						    cellE.setCellValue(record.getR19_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row20
						row = sheet.getRow(19);

						// row20
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR20_orig_amt() != null) {
						    cellB.setCellValue(record.getR20_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row20
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR20_fore_amt() != null) {
						    cellC.setCellValue(record.getR20_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row20
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR20_no_of_acc() != null) {
						    cellE.setCellValue(record.getR20_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row21
						row = sheet.getRow(20);

						// row21
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR21_orig_amt() != null) {
						    cellB.setCellValue(record.getR21_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row21
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR21_fore_amt() != null) {
						    cellC.setCellValue(record.getR21_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row21
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR21_no_of_acc() != null) {
						    cellE.setCellValue(record.getR21_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row22
						row = sheet.getRow(21);

						// row22
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR22_orig_amt() != null) {
						    cellB.setCellValue(record.getR22_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row22
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR22_fore_amt() != null) {
						    cellC.setCellValue(record.getR22_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row22
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR22_no_of_acc() != null) {
						    cellE.setCellValue(record.getR22_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row23
						row = sheet.getRow(22);

						// row23
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR23_orig_amt() != null) {
						    cellB.setCellValue(record.getR23_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row23
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR23_fore_amt() != null) {
						    cellC.setCellValue(record.getR23_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row23
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR23_no_of_acc() != null) {
						    cellE.setCellValue(record.getR23_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row24
						row = sheet.getRow(23);

						// row24
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR24_orig_amt() != null) {
						    cellB.setCellValue(record.getR24_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row24
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR24_fore_amt() != null) {
						    cellC.setCellValue(record.getR24_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row24
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR24_no_of_acc() != null) {
						    cellE.setCellValue(record.getR24_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row25
						row = sheet.getRow(24);

						// row25
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR25_orig_amt() != null) {
						    cellB.setCellValue(record.getR25_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row25
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR25_fore_amt() != null) {
						    cellC.setCellValue(record.getR25_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row25
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR25_no_of_acc() != null) {
						    cellE.setCellValue(record.getR25_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row26
						row = sheet.getRow(25);

						// row26
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR26_orig_amt() != null) {
						    cellB.setCellValue(record.getR26_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row26
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR26_fore_amt() != null) {
						    cellC.setCellValue(record.getR26_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row26
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR26_no_of_acc() != null) {
						    cellE.setCellValue(record.getR26_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row27
						row = sheet.getRow(26);

						// row27
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR27_orig_amt() != null) {
						    cellB.setCellValue(record.getR27_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row27
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR27_fore_amt() != null) {
						    cellC.setCellValue(record.getR27_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row27
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR27_no_of_acc() != null) {
						    cellE.setCellValue(record.getR27_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						// row29
						row = sheet.getRow(28);

						// row29
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR29_orig_amt() != null) {
						    cellB.setCellValue(record.getR29_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row29
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR29_fore_amt() != null) {
						    cellC.setCellValue(record.getR29_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row29
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR29_no_of_acc() != null) {
						    cellE.setCellValue(record.getR29_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row30
						row = sheet.getRow(29);

						// row30
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR30_orig_amt() != null) {
						    cellB.setCellValue(record.getR30_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row30
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR30_fore_amt() != null) {
						    cellC.setCellValue(record.getR30_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row30
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR30_no_of_acc() != null) {
						    cellE.setCellValue(record.getR30_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row31
						row = sheet.getRow(30);

						// row31
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR31_orig_amt() != null) {
						    cellB.setCellValue(record.getR31_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row31
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR31_fore_amt() != null) {
						    cellC.setCellValue(record.getR31_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row31
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR31_no_of_acc() != null) {
						    cellE.setCellValue(record.getR31_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row32
						row = sheet.getRow(31);

						// row32
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR32_orig_amt() != null) {
						    cellB.setCellValue(record.getR32_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row32
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR32_fore_amt() != null) {
						    cellC.setCellValue(record.getR32_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row32
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR32_no_of_acc() != null) {
						    cellE.setCellValue(record.getR32_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row33
						row = sheet.getRow(32);

						// row33
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR33_orig_amt() != null) {
						    cellB.setCellValue(record.getR33_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row33
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR33_fore_amt() != null) {
						    cellC.setCellValue(record.getR33_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row33
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR33_no_of_acc() != null) {
						    cellE.setCellValue(record.getR33_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row34
						row = sheet.getRow(33);

						// row34
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR34_orig_amt() != null) {
						    cellB.setCellValue(record.getR34_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row34
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR34_fore_amt() != null) {
						    cellC.setCellValue(record.getR34_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row34
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR34_no_of_acc() != null) {
						    cellE.setCellValue(record.getR34_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row35
						row = sheet.getRow(34);

						// row35
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR35_orig_amt() != null) {
						    cellB.setCellValue(record.getR35_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row35
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR35_fore_amt() != null) {
						    cellC.setCellValue(record.getR35_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row35
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR35_no_of_acc() != null) {
						    cellE.setCellValue(record.getR35_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row36
						row = sheet.getRow(35);

						// row36
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR36_orig_amt() != null) {
						    cellB.setCellValue(record.getR36_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row36
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR36_fore_amt() != null) {
						    cellC.setCellValue(record.getR36_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row36
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR36_no_of_acc() != null) {
						    cellE.setCellValue(record.getR36_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						// row38
						row = sheet.getRow(37);

						// row38
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR38_orig_amt() != null) {
						    cellB.setCellValue(record.getR38_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row38
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR38_fore_amt() != null) {
						    cellC.setCellValue(record.getR38_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row38
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR38_no_of_acc() != null) {
						    cellE.setCellValue(record.getR38_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row39
						row = sheet.getRow(38);

						// row39
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR39_orig_amt() != null) {
						    cellB.setCellValue(record.getR39_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row39
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR39_fore_amt() != null) {
						    cellC.setCellValue(record.getR39_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row39
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR39_no_of_acc() != null) {
						    cellE.setCellValue(record.getR39_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						// row41
						row = sheet.getRow(40);

						// row41
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR41_orig_amt() != null) {
						    cellB.setCellValue(record.getR41_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row41
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR41_fore_amt() != null) {
						    cellC.setCellValue(record.getR41_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row41
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR41_no_of_acc() != null) {
						    cellE.setCellValue(record.getR41_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row42
						row = sheet.getRow(41);

						// row42
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR42_orig_amt() != null) {
						    cellB.setCellValue(record.getR42_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row42
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR42_fore_amt() != null) {
						    cellC.setCellValue(record.getR42_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row42
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR42_no_of_acc() != null) {
						    cellE.setCellValue(record.getR42_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						// row44
						row = sheet.getRow(43);

						// row44
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR44_orig_amt() != null) {
						    cellB.setCellValue(record.getR44_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row44
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR44_fore_amt() != null) {
						    cellC.setCellValue(record.getR44_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row44
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR44_no_of_acc() != null) {
						    cellE.setCellValue(record.getR44_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row45
						row = sheet.getRow(44);

						// row45
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR45_orig_amt() != null) {
						    cellB.setCellValue(record.getR45_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row45
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR45_fore_amt() != null) {
						    cellC.setCellValue(record.getR45_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row45
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR45_no_of_acc() != null) {
						    cellE.setCellValue(record.getR45_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row46
						row = sheet.getRow(45);

						// row46
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR46_orig_amt() != null) {
						    cellB.setCellValue(record.getR46_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row46
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR46_fore_amt() != null) {
						    cellC.setCellValue(record.getR46_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row46
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR46_no_of_acc() != null) {
						    cellE.setCellValue(record.getR46_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row47
						row = sheet.getRow(46);

						// row47
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR47_orig_amt() != null) {
						    cellB.setCellValue(record.getR47_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row47
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR47_fore_amt() != null) {
						    cellC.setCellValue(record.getR47_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row47
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR47_no_of_acc() != null) {
						    cellE.setCellValue(record.getR47_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						// row49
						row = sheet.getRow(48);

						// row49
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR49_orig_amt() != null) {
						    cellB.setCellValue(record.getR49_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row49
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR49_fore_amt() != null) {
						    cellC.setCellValue(record.getR49_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row49
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR49_no_of_acc() != null) {
						    cellE.setCellValue(record.getR49_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row50
						row = sheet.getRow(49);

						// row50
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR50_orig_amt() != null) {
						    cellB.setCellValue(record.getR50_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row50
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR50_fore_amt() != null) {
						    cellC.setCellValue(record.getR50_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row50
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR50_no_of_acc() != null) {
						    cellE.setCellValue(record.getR50_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row51
						row = sheet.getRow(50);

						// row51
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR51_orig_amt() != null) {
						    cellB.setCellValue(record.getR51_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row51
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR51_fore_amt() != null) {
						    cellC.setCellValue(record.getR51_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row51
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR51_no_of_acc() != null) {
						    cellE.setCellValue(record.getR51_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						// row53
						row = sheet.getRow(52);

						// row53
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR53_orig_amt() != null) {
						    cellB.setCellValue(record.getR53_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row53
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR53_fore_amt() != null) {
						    cellC.setCellValue(record.getR53_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row53
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR53_no_of_acc() != null) {
						    cellE.setCellValue(record.getR53_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row54
						row = sheet.getRow(53);

						// row54
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR54_orig_amt() != null) {
						    cellB.setCellValue(record.getR54_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row54
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR54_fore_amt() != null) {
						    cellC.setCellValue(record.getR54_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row54
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR54_no_of_acc() != null) {
						    cellE.setCellValue(record.getR54_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row55
						row = sheet.getRow(54);

						// row55
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR55_orig_amt() != null) {
						    cellB.setCellValue(record.getR55_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row55
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR55_fore_amt() != null) {
						    cellC.setCellValue(record.getR55_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row55
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR55_no_of_acc() != null) {
						    cellE.setCellValue(record.getR55_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}
						
						
						
						// row57
						row = sheet.getRow(56);

						// row57
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR57_orig_amt() != null) {
						    cellB.setCellValue(record.getR57_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row57
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR57_fore_amt() != null) {
						    cellC.setCellValue(record.getR57_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row57
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR57_no_of_acc() != null) {
						    cellE.setCellValue(record.getR57_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row58
						row = sheet.getRow(57);

						// row58
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR58_orig_amt() != null) {
						    cellB.setCellValue(record.getR58_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row58
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR58_fore_amt() != null) {
						    cellC.setCellValue(record.getR58_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row58
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR58_no_of_acc() != null) {
						    cellE.setCellValue(record.getR58_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row59
						row = sheet.getRow(58);

						// row59
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR59_orig_amt() != null) {
						    cellB.setCellValue(record.getR59_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row59
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR59_fore_amt() != null) {
						    cellC.setCellValue(record.getR59_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row59
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR59_no_of_acc() != null) {
						    cellE.setCellValue(record.getR59_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row60
						row = sheet.getRow(59);

						// row60
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR60_orig_amt() != null) {
						    cellB.setCellValue(record.getR60_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row60
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR60_fore_amt() != null) {
						    cellC.setCellValue(record.getR60_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row60
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR60_no_of_acc() != null) {
						    cellE.setCellValue(record.getR60_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row61
						row = sheet.getRow(60);

						// row61
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR61_orig_amt() != null) {
						    cellB.setCellValue(record.getR61_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row61
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR61_fore_amt() != null) {
						    cellC.setCellValue(record.getR61_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row61
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR61_no_of_acc() != null) {
						    cellE.setCellValue(record.getR61_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
						}

						// row62
						row = sheet.getRow(61);

						// row62
						// Column B2 - Original Amount 
						cellB = row.createCell(1);
						if (record.getR62_orig_amt() != null) {
						    cellB.setCellValue(record.getR62_orig_amt().doubleValue());
						    cellB.setCellStyle(numberStyle);
						} else {
						    cellB.setCellValue("");
						    cellB.setCellStyle(textStyle);
						}

						// row62
						// Column C3 - Foreclosure Amount
						cellC = row.createCell(2);
						if (record.getR62_fore_amt() != null) {
						    cellC.setCellValue(record.getR62_fore_amt().doubleValue());
						    cellC.setCellStyle(numberStyle);
						} else {
						    cellC.setCellValue("");
						    cellC.setCellStyle(textStyle);
						}

						// row62
						// Column D4 - No. of Accounts
						cellE = row.createCell(3);
						if (record.getR62_no_of_acc() != null) {
						    cellE.setCellValue(record.getR62_no_of_acc().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
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