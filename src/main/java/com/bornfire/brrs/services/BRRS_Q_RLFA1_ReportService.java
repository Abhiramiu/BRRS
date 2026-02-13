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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_Q_RLFA1_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Q_RLFA1_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_Q_RLFA1_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Q_RLFA1_Detail_Repo_New;
import com.bornfire.brrs.entities.BRRS_Q_RLFA1_Resub_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Q_RLFA1_Resub_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_Q_RLFA1_Summary_Repo;
import com.bornfire.brrs.entities.BrrsMNosvosP1Detail;
import com.bornfire.brrs.entities.M_EPR_Summary_Entity;
import com.bornfire.brrs.entities.M_LIQ_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12B_Detail_Entity1;
import com.bornfire.brrs.entities.Q_RLFA1_Archival_Detail_Entity;
import com.bornfire.brrs.entities.Q_RLFA1_Archival_Summary_Entity;
import com.bornfire.brrs.entities.Q_RLFA1_Detail_Entity_New;
import com.bornfire.brrs.entities.Q_RLFA1_Resub_Detail_Entity;
import com.bornfire.brrs.entities.Q_RLFA1_Resub_Summary_Entity;
import com.bornfire.brrs.entities.Q_RLFA1_Summary_Entity;
import com.bornfire.brrs.entities.Q_RLFA2_Archival_Summary_Entity;
import com.bornfire.brrs.entities.Q_RLFA2_Summary_Entity;

@Component
@Service

public class BRRS_Q_RLFA1_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_Q_RLFA1_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_Q_RLFA1_Detail_Repo brrs_q_rlfa1_detail_Repo;

	@Autowired
	BRRS_Q_RLFA1_Summary_Repo brrs_q_rlfa1_Summary_Repo;
	
	@Autowired
	BRRS_Q_RLFA1_Detail_Repo_New new_brrs_q_rlfa1_Summary_Repo;

	@Autowired
	BRRS_Q_RLFA1_Archival_Detail_Repo q_rlfa1_Archival_Detail_Repo;

	@Autowired
	BRRS_Q_RLFA1_Archival_Summary_Repo q_rlfa1_Archival_Summary_Repo;
	
	@Autowired
	BRRS_Q_RLFA1_Resub_Summary_Repo brrs_Q_RLFA1_Resub_Summary_Repo;
	
	@Autowired
	BRRS_Q_RLFA1_Resub_Detail_Repo brrs_Q_RLFA1_Resub_Detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	/*
	 * public ModelAndView getQ_RLFA1View(String reportId, String fromdate, String
	 * todate, String currency, String dtltype, Pageable pageable, String type,
	 * String version) {
	 * 
	 * ModelAndView mv = new ModelAndView(); Session hs =
	 * sessionFactory.getCurrentSession();
	 * 
	 * int pageSize = pageable.getPageSize(); int currentPage =
	 * pageable.getPageNumber(); int startItem = currentPage * pageSize;
	 * 
	 * System.out.println("testing"); System.out.println(version);
	 * 
	 * if (type.equals("ARCHIVAL") & version != null) { System.out.println(type);
	 * List<Q_RLFA1_Archival_Summary_Entity> T1Master = new
	 * ArrayList<Q_RLFA1_Archival_Summary_Entity>();
	 * 
	 * System.out.println(version); try { Date d1 = dateformat.parse(todate);
	 * 
	 * 
	 * 
	 * T1Master =
	 * q_rlfa1_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(
	 * todate), version);
	 * 
	 * } catch (ParseException e) { e.printStackTrace(); }
	 * 
	 * mv.addObject("reportsummary", T1Master);
	 * 
	 * } else {
	 * 
	 * List<Q_RLFA1_Summary_Entity> T1Master = new
	 * ArrayList<Q_RLFA1_Summary_Entity>();
	 * 
	 * try { Date d1 = dateformat.parse(todate);
	 * 
	 * T1Master =
	 * brrs_q_rlfa1_Summary_Repo.getdatabydateList(dateformat.parse(todate));
	 * 
	 * System.out.println("T1Master size " + T1Master.size());
	 * mv.addObject("report_date", dateformat.format(d1));
	 * 
	 * } catch (ParseException e) { e.printStackTrace(); }
	 * mv.addObject("reportsummary", T1Master);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * mv.setViewName("BRRS/Q_RLFA1");
	 * 
	 * mv.addObject("displaymode", "summary");
	 * 
	 * System.out.println("scv" + mv.getViewName());
	 * 
	 * return mv;
	 * 
	 * }
	 */
	
	
	
	 public ModelAndView getQ_RLFA1View(String reportId, String fromdate, String
			  todate, String currency, String dtltype, Pageable pageable, String type,
			  BigDecimal version) {
			  
			  ModelAndView mv = new ModelAndView(); Session hs =
			  sessionFactory.getCurrentSession();
			  
			  int pageSize = pageable.getPageSize(); int currentPage =
			  pageable.getPageNumber(); int startItem = currentPage * pageSize;
			  
			  String displayMode = "summary";
			  
			  try { Date d1 = dateformat.parse(todate);
			  
			  // ---------- CASE 1: ARCHIVAL ---------- 
			  if ( ( "ARCHIVAL".equalsIgnoreCase(type) && "summary".equalsIgnoreCase(dtltype) && version != null ) || ( "ARCHIVAL".equalsIgnoreCase(type) && !"detail".equalsIgnoreCase(dtltype) && version != null) ) {
				  List<Q_RLFA1_Archival_Summary_Entity> T1Master = q_rlfa1_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
				  displayMode = "summary";
				  mv.addObject("reportsummary", T1Master);
			  }
			  else if ("ARCHIVAL".equalsIgnoreCase(type) && "detail".equalsIgnoreCase(dtltype) && version != null) {
				  List<Q_RLFA1_Archival_Detail_Entity> T1Master = q_rlfa1_Archival_Detail_Repo.getdatabydateListarchival(d1, version);
				  displayMode = "detail";
				  mv.addObject("reportsummary", T1Master);
			  }
			  // ---------- CASE 2: RESUB ---------- 
			  else if  (( "RESUB".equalsIgnoreCase(type) && "summary".equalsIgnoreCase(dtltype) && version != null) ||  ( "RESUB".equalsIgnoreCase(type) && !"detail".equalsIgnoreCase(dtltype) && version != null ) ) {
				  List<Q_RLFA1_Resub_Summary_Entity> T1Master =
						  brrs_Q_RLFA1_Resub_Summary_Repo.getdatabydateListarchival(d1, version);
				  displayMode = "summary";
				  mv.addObject("reportsummary", T1Master); 
			  }
			  else if  ( "RESUB".equalsIgnoreCase(type) && "detail".equalsIgnoreCase(dtltype) && version != null) {
				  List<Q_RLFA1_Resub_Detail_Entity> T1Master =
						  brrs_Q_RLFA1_Resub_Detail_Repo.getdatabydateListarchival(d1, version);
				  displayMode = "detail";
				  mv.addObject("reportsummary", T1Master); 
			  }
			  else if( !"ARCHIVAL".equalsIgnoreCase(type) && "detail".equalsIgnoreCase(dtltype)){
				  List<Q_RLFA1_Detail_Entity_New>
			  		T1Master =
			  				new_brrs_q_rlfa1_Summary_Repo.getdatabydateListWithVersion(todate);
			  		System.out.println("Detail");
			  		mv.addObject("reportsummary", T1Master); 
			  		displayMode = "detail";
			  }
			  
			  // ---------- CASE 3: NORMAL ---------- 
			  else {
				  List<Q_RLFA1_Summary_Entity>
			  		T1Master =
					  brrs_q_rlfa1_Summary_Repo.getdatabydateListWithVersion(todate);
			  		System.out.println("Summary");
			  		mv.addObject("reportsummary", T1Master); 
			  		displayMode = "summary";
			  }
			  
			  } catch (ParseException e) { e.printStackTrace(); }
			  
			  mv.setViewName("BRRS/Q_RLFA1"); mv.addObject("displaymode", displayMode);
			  System.out.println("View set to: " + mv.getViewName()); return mv; }

	 
	


	public byte[] getQ_RLFA1Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelQ_RLFA1ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}
		
		
		else if ("RESUB".equalsIgnoreCase(type) && version != null ) {
		    logger.info("Service: Generating RESUB report for version {}", version);

		    try {
		        // ✅ Use pattern matching "31-Jul-2025"
		        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
		        Date report_date = sdf.parse(fromdate);  // or use asondate if that's your date source

		        List<Q_RLFA1_Archival_Summary_Entity> T1Master = q_rlfa1_Archival_Summary_Repo.getdatabydateListarchival(report_date, version);

		        // ✅ Generate Excel
		        return BRRS_Q_RLFA1ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

		    } catch (ParseException e) {
		        logger.error("Invalid report date format: {}", fromdate, e);
		        throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
		    }
		}
		
		
		

		// Fetch data

		List<Q_RLFA1_Summary_Entity> dataList = brrs_q_rlfa1_Summary_Repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Q_RLFA1 report. Returning empty result.");
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

			if("email_report".equalsIgnoreCase(dtltype)) {
				
				Q_RLFA1_Summary_Entity record1 = dataList.get(0);
				
				Row row = sheet.getRow(7) != null ? sheet.getRow(7) : sheet.createRow(7);
				 Cell cell2 = row.createCell(2);

				 if (record1.getR10_collateral_amount() != null) {
				     cell2.setCellValue(record1.getR10_collateral_amount().doubleValue());
				     cell2.setCellStyle(numberStyle);
				 } else {
				     cell2.setCellValue("");
				     cell2.setCellStyle(textStyle);
				 }

				  Cell cell3 = row.createCell(3);

				 if (record1.getR10_carrying_amount() != null) {
				     cell3.setCellValue(record1.getR10_carrying_amount().doubleValue());
				     cell3.setCellStyle(numberStyle);
				 } else {
				     cell3.setCellValue("");
				     cell3.setCellStyle(textStyle);
				 }

				  Cell cell4 = row.createCell(5);

				 if (record1.getR10_no_of_accts() != null) {
				     cell4.setCellValue(record1.getR10_no_of_accts().doubleValue());
				     cell4.setCellStyle(numberStyle);
				 } else {
				     cell4.setCellValue("");
				     cell4.setCellStyle(textStyle);
				 }


				 row = sheet.getRow(8) != null ? sheet.getRow(8) : sheet.createRow(8);
				  Cell cell5 = row.createCell(2);

				 if (record1.getR11_collateral_amount() != null) {
				     cell5.setCellValue(record1.getR11_collateral_amount().doubleValue());
				     cell5.setCellStyle(numberStyle);
				 } else {
				     cell5.setCellValue("");
				     cell5.setCellStyle(textStyle);
				 }

				  Cell cell6 = row.createCell(3);

				 if (record1.getR11_carrying_amount() != null) {
				     cell6.setCellValue(record1.getR11_carrying_amount().doubleValue());
				     cell6.setCellStyle(numberStyle);
				 } else {
				     cell6.setCellValue("");
				     cell6.setCellStyle(textStyle);
				 }

				  Cell cell7 = row.createCell(5);

				 if (record1.getR11_no_of_accts() != null) {
				     cell7.setCellValue(record1.getR11_no_of_accts().doubleValue());
				     cell7.setCellStyle(numberStyle);
				 } else {
				     cell7.setCellValue("");
				     cell7.setCellStyle(textStyle);
				 }


				 row = sheet.getRow(9) != null ? sheet.getRow(9) : sheet.createRow(9);
				  Cell cell8 = row.createCell(2);

				 if (record1.getR12_collateral_amount() != null) {
				     cell8.setCellValue(record1.getR12_collateral_amount().doubleValue());
				     cell8.setCellStyle(numberStyle);
				 } else {
				     cell8.setCellValue("");
				     cell8.setCellStyle(textStyle);
				 }

				  Cell cell9 = row.createCell(3);

				 if (record1.getR12_carrying_amount() != null) {
				     cell9.setCellValue(record1.getR12_carrying_amount().doubleValue());
				     cell9.setCellStyle(numberStyle);
				 } else {
				     cell9.setCellValue("");
				     cell9.setCellStyle(textStyle);
				 }

				  Cell cell10 = row.createCell(5);

				 if (record1.getR12_no_of_accts() != null) {
				     cell10.setCellValue(record1.getR12_no_of_accts().doubleValue());
				     cell10.setCellStyle(numberStyle);
				 } else {
				     cell10.setCellValue("");
				     cell10.setCellStyle(textStyle);
				 }


				 row = sheet.getRow(10) != null ? sheet.getRow(10) : sheet.createRow(10);
				  Cell cell11 = row.createCell(2);

				 if (record1.getR13_collateral_amount() != null) {
				     cell11.setCellValue(record1.getR13_collateral_amount().doubleValue());
				     cell11.setCellStyle(numberStyle);
				 } else {
				     cell11.setCellValue("");
				     cell11.setCellStyle(textStyle);
				 }

				  Cell cell12 = row.createCell(3);

				 if (record1.getR13_carrying_amount() != null) {
				     cell12.setCellValue(record1.getR13_carrying_amount().doubleValue());
				     cell12.setCellStyle(numberStyle);
				 } else {
				     cell12.setCellValue("");
				     cell12.setCellStyle(textStyle);
				 }

				  Cell cell13 = row.createCell(5);

				 if (record1.getR13_no_of_accts() != null) {
				     cell13.setCellValue(record1.getR13_no_of_accts().doubleValue());
				     cell13.setCellStyle(numberStyle);
				 } else {
				     cell13.setCellValue("");
				     cell13.setCellStyle(textStyle);
				 }


				 row = sheet.getRow(11) != null ? sheet.getRow(11) : sheet.createRow(11);
				  Cell cell14 = row.createCell(2);

				 if (record1.getR14_collateral_amount() != null) {
				     cell14.setCellValue(record1.getR14_collateral_amount().doubleValue());
				     cell14.setCellStyle(numberStyle);
				 } else {
				     cell14.setCellValue("");
				     cell14.setCellStyle(textStyle);
				 }

				  Cell cell15 = row.createCell(3);

				 if (record1.getR14_carrying_amount() != null) {
				     cell15.setCellValue(record1.getR14_carrying_amount().doubleValue());
				     cell15.setCellStyle(numberStyle);
				 } else {
				     cell15.setCellValue("");
				     cell15.setCellStyle(textStyle);
				 }

				  Cell cell16 = row.createCell(5);

				 if (record1.getR14_no_of_accts() != null) {
				     cell16.setCellValue(record1.getR14_no_of_accts().doubleValue());
				     cell16.setCellStyle(numberStyle);
				 } else {
				     cell16.setCellValue("");
				     cell16.setCellStyle(textStyle);
				 }


				 row = sheet.getRow(12) != null ? sheet.getRow(12) : sheet.createRow(12);
				  Cell cell17 = row.createCell(2);

				 if (record1.getR15_collateral_amount() != null) {
				     cell17.setCellValue(record1.getR15_collateral_amount().doubleValue());
				     cell17.setCellStyle(numberStyle);
				 } else {
				     cell17.setCellValue("");
				     cell17.setCellStyle(textStyle);
				 }

				  Cell cell18 = row.createCell(3);

				 if (record1.getR15_carrying_amount() != null) {
				     cell18.setCellValue(record1.getR15_carrying_amount().doubleValue());
				     cell18.setCellStyle(numberStyle);
				 } else {
				     cell18.setCellValue("");
				     cell18.setCellStyle(textStyle);
				 }

				  Cell cell19 = row.createCell(5);

				 if (record1.getR15_no_of_accts() != null) {
				     cell19.setCellValue(record1.getR15_no_of_accts().doubleValue());
				     cell19.setCellStyle(numberStyle);
				 } else {
				     cell19.setCellValue("");
				     cell19.setCellStyle(textStyle);
				 }


				 row = sheet.getRow(13) != null ? sheet.getRow(13) : sheet.createRow(13);
				  Cell cell20 = row.createCell(2);

				 if (record1.getR16_collateral_amount() != null) {
				     cell20.setCellValue(record1.getR16_collateral_amount().doubleValue());
				     cell20.setCellStyle(numberStyle);
				 } else {
				     cell20.setCellValue("");
				     cell20.setCellStyle(textStyle);
				 }

				  Cell cell21 = row.createCell(3);

				 if (record1.getR16_carrying_amount() != null) {
				     cell21.setCellValue(record1.getR16_carrying_amount().doubleValue());
				     cell21.setCellStyle(numberStyle);
				 } else {
				     cell21.setCellValue("");
				     cell21.setCellStyle(textStyle);
				 }

				  Cell cell22 = row.createCell(5);

				 if (record1.getR16_no_of_accts() != null) {
				     cell22.setCellValue(record1.getR16_no_of_accts().doubleValue());
				     cell22.setCellStyle(numberStyle);
				 } else {
				     cell22.setCellValue("");
				     cell22.setCellStyle(textStyle);
				 }


				 row = sheet.getRow(14) != null ? sheet.getRow(14) : sheet.createRow(14);
				  Cell cell23 = row.createCell(2);

				 if (record1.getR17_collateral_amount() != null) {
				     cell23.setCellValue(record1.getR17_collateral_amount().doubleValue());
				     cell23.setCellStyle(numberStyle);
				 } else {
				     cell23.setCellValue("");
				     cell23.setCellStyle(textStyle);
				 }

				  Cell cell24 = row.createCell(3);

				 if (record1.getR17_carrying_amount() != null) {
				     cell24.setCellValue(record1.getR17_carrying_amount().doubleValue());
				     cell24.setCellStyle(numberStyle);
				 } else {
				     cell24.setCellValue("");
				     cell24.setCellStyle(textStyle);
				 }

				  Cell cell25 = row.createCell(5);

				 if (record1.getR17_no_of_accts() != null) {
				     cell25.setCellValue(record1.getR17_no_of_accts().doubleValue());
				     cell25.setCellStyle(numberStyle);
				 } else {
				     cell25.setCellValue("");
				     cell25.setCellStyle(textStyle);
				 }


				 row = sheet.getRow(15) != null ? sheet.getRow(15) : sheet.createRow(15);
				  Cell cell26 = row.createCell(2);

				 if (record1.getR18_collateral_amount() != null) {
				     cell26.setCellValue(record1.getR18_collateral_amount().doubleValue());
				     cell26.setCellStyle(numberStyle);
				 } else {
				     cell26.setCellValue("");
				     cell26.setCellStyle(textStyle);
				 }

				  Cell cell27 = row.createCell(3);

				 if (record1.getR18_carrying_amount() != null) {
				     cell27.setCellValue(record1.getR18_carrying_amount().doubleValue());
				     cell27.setCellStyle(numberStyle);
				 } else {
				     cell27.setCellValue("");
				     cell27.setCellStyle(textStyle);
				 }

				  Cell cell28 = row.createCell(5);

				 if (record1.getR18_no_of_accts() != null) {
				     cell28.setCellValue(record1.getR18_no_of_accts().doubleValue());
				     cell28.setCellStyle(numberStyle);
				 } else {
				     cell28.setCellValue("");
				     cell28.setCellStyle(textStyle);
				 }


//				 row = sheet.getRow(16) != null ? sheet.getRow(16) : sheet.createRow(16);
//				  Cell cell29 = row.createCell(2);
//
//				 if (record1.getR19_collateral_amount() != null) {
//				     cell29.setCellValue(record1.getR19_collateral_amount().doubleValue());
//				     cell29.setCellStyle(numberStyle);
//				 } else {
//				     cell29.setCellValue("");
//				     cell29.setCellStyle(textStyle);
//				 }
//
//				  Cell cell30 = row.createCell(3);
//
//				 if (record1.getR19_carrying_amount() != null) {
//				     cell30.setCellValue(record1.getR19_carrying_amount().doubleValue());
//				     cell30.setCellStyle(numberStyle);
//				 } else {
//				     cell30.setCellValue("");
//				     cell30.setCellStyle(textStyle);
//				 }
//
//				  Cell cell31 = row.createCell(4);
//
//				 if (record1.getR19_no_of_accts() != null) {
//				     cell31.setCellValue(record1.getR19_no_of_accts().doubleValue());
//				     cell31.setCellStyle(numberStyle);
//				 } else {
//				     cell31.setCellValue("");
//				     cell31.setCellStyle(textStyle);
//				 }
//
//
//				 row = sheet.getRow(17) != null ? sheet.getRow(17) : sheet.createRow(17);
//				  Cell cell32 = row.createCell(2);
//
//				 if (record1.getR20_collateral_amount() != null) {
//				     cell32.setCellValue(record1.getR20_collateral_amount().doubleValue());
//				     cell32.setCellStyle(numberStyle);
//				 } else {
//				     cell32.setCellValue("");
//				     cell32.setCellStyle(textStyle);
//				 }
//
//				  Cell cell33 = row.createCell(3);
//
//				 if (record1.getR20_carrying_amount() != null) {
//				     cell33.setCellValue(record1.getR20_carrying_amount().doubleValue());
//				     cell33.setCellStyle(numberStyle);
//				 } else {
//				     cell33.setCellValue("");
//				     cell33.setCellStyle(textStyle);
//				 }
//
//				  Cell cell34 = row.createCell(4);
//
//				 if (record1.getR20_no_of_accts() != null) {
//				     cell34.setCellValue(record1.getR20_no_of_accts().doubleValue());
//				     cell34.setCellStyle(numberStyle);
//				 } else {
//				     cell34.setCellValue("");
//				     cell34.setCellStyle(textStyle);
//				 }
//
//
//				 row = sheet.getRow(18) != null ? sheet.getRow(18) : sheet.createRow(18);
//				  Cell cell35 = row.createCell(2);
//
//				 if (record1.getR21_collateral_amount() != null) {
//				     cell35.setCellValue(record1.getR21_collateral_amount().doubleValue());
//				     cell35.setCellStyle(numberStyle);
//				 } else {
//				     cell35.setCellValue("");
//				     cell35.setCellStyle(textStyle);
//				 }
//
//				  Cell cell36 = row.createCell(3);
//
//				 if (record1.getR21_carrying_amount() != null) {
//				     cell36.setCellValue(record1.getR21_carrying_amount().doubleValue());
//				     cell36.setCellStyle(numberStyle);
//				 } else {
//				     cell36.setCellValue("");
//				     cell36.setCellStyle(textStyle);
//				 }
//
//				  Cell cell37 = row.createCell(4);
//
//				 if (record1.getR21_no_of_accts() != null) {
//				     cell37.setCellValue(record1.getR21_no_of_accts().doubleValue());
//				     cell37.setCellStyle(numberStyle);
//				 } else {
//				     cell37.setCellValue("");
//				     cell37.setCellStyle(textStyle);
//				 }
//
//
//				 row = sheet.getRow(19) != null ? sheet.getRow(19) : sheet.createRow(19);
//				  Cell cell38 = row.createCell(2);
//
//				 if (record1.getR22_collateral_amount() != null) {
//				     cell38.setCellValue(record1.getR22_collateral_amount().doubleValue());
//				     cell38.setCellStyle(numberStyle);
//				 } else {
//				     cell38.setCellValue("");
//				     cell38.setCellStyle(textStyle);
//				 }
//
//				  Cell cell39 = row.createCell(3);
//
//				 if (record1.getR22_carrying_amount() != null) {
//				     cell39.setCellValue(record1.getR22_carrying_amount().doubleValue());
//				     cell39.setCellStyle(numberStyle);
//				 } else {
//				     cell39.setCellValue("");
//				     cell39.setCellStyle(textStyle);
//				 }
//
//				  Cell cell40 = row.createCell(4);
//
//				 if (record1.getR22_no_of_accts() != null) {
//				     cell40.setCellValue(record1.getR22_no_of_accts().doubleValue());
//				     cell40.setCellStyle(numberStyle);
//				 } else {
//				     cell40.setCellValue("");
//				     cell40.setCellStyle(textStyle);
//				 }
//
//
//				 row = sheet.getRow(20) != null ? sheet.getRow(20) : sheet.createRow(20);
//				  Cell cell41 = row.createCell(2);
//
//				 if (record1.getR23_collateral_amount() != null) {
//				     cell41.setCellValue(record1.getR23_collateral_amount().doubleValue());
//				     cell41.setCellStyle(numberStyle);
//				 } else {
//				     cell41.setCellValue("");
//				     cell41.setCellStyle(textStyle);
//				 }
//
//				  Cell cell42 = row.createCell(3);
//
//				 if (record1.getR23_carrying_amount() != null) {
//				     cell42.setCellValue(record1.getR23_carrying_amount().doubleValue());
//				     cell42.setCellStyle(numberStyle);
//				 } else {
//				     cell42.setCellValue("");
//				     cell42.setCellStyle(textStyle);
//				 }
//
//				  Cell cell43 = row.createCell(4);
//
//				 if (record1.getR23_no_of_accts() != null) {
//				     cell43.setCellValue(record1.getR23_no_of_accts().doubleValue());
//				     cell43.setCellStyle(numberStyle);
//				 } else {
//				     cell43.setCellValue("");
//				     cell43.setCellStyle(textStyle);
//				 }
//
//
//				 row = sheet.getRow(21) != null ? sheet.getRow(21) : sheet.createRow(21);
//				  Cell cell44 = row.createCell(2);
//
//				 if (record1.getR24_collateral_amount() != null) {
//				     cell44.setCellValue(record1.getR24_collateral_amount().doubleValue());
//				     cell44.setCellStyle(numberStyle);
//				 } else {
//				     cell44.setCellValue("");
//				     cell44.setCellStyle(textStyle);
//				 }
//
//				  Cell cell45 = row.createCell(3);
//
//				 if (record1.getR24_carrying_amount() != null) {
//				     cell45.setCellValue(record1.getR24_carrying_amount().doubleValue());
//				     cell45.setCellStyle(numberStyle);
//				 } else {
//				     cell45.setCellValue("");
//				     cell45.setCellStyle(textStyle);
//				 }
//
//				  Cell cell46 = row.createCell(4);
//
//				 if (record1.getR24_no_of_accts() != null) {
//				     cell46.setCellValue(record1.getR24_no_of_accts().doubleValue());
//				     cell46.setCellStyle(numberStyle);
//				 } else {
//				     cell46.setCellValue("");
//				     cell46.setCellStyle(textStyle);
//				 }
//
//
//				 row = sheet.getRow(22) != null ? sheet.getRow(22) : sheet.createRow(22);
//				  Cell cell47 = row.createCell(2);
//
//				 if (record1.getR25_collateral_amount() != null) {
//				     cell47.setCellValue(record1.getR25_collateral_amount().doubleValue());
//				     cell47.setCellStyle(numberStyle);
//				 } else {
//				     cell47.setCellValue("");
//				     cell47.setCellStyle(textStyle);
//				 }
//
//				  Cell cell48 = row.createCell(3);
//
//				 if (record1.getR25_carrying_amount() != null) {
//				     cell48.setCellValue(record1.getR25_carrying_amount().doubleValue());
//				     cell48.setCellStyle(numberStyle);
//				 } else {
//				     cell48.setCellValue("");
//				     cell48.setCellStyle(textStyle);
//				 }
//
//				  Cell cell49 = row.createCell(4);
//
//				 if (record1.getR25_no_of_accts() != null) {
//				     cell49.setCellValue(record1.getR25_no_of_accts().doubleValue());
//				     cell49.setCellStyle(numberStyle);
//				 } else {
//				     cell49.setCellValue("");
//				     cell49.setCellStyle(textStyle);
//				 }
//
//
//				 row = sheet.getRow(23) != null ? sheet.getRow(23) : sheet.createRow(23);
//				  Cell cell50 = row.createCell(2);
//
//				 if (record1.getR28_collateral_amount() != null) {
//				     cell50.setCellValue(record1.getR28_collateral_amount().doubleValue());
//				     cell50.setCellStyle(numberStyle);
//				 } else {
//				     cell50.setCellValue("");
//				     cell50.setCellStyle(textStyle);
//				 }
//
//				  Cell cell51 = row.createCell(3);
//
//				 if (record1.getR28_carrying_amount() != null) {
//				     cell51.setCellValue(record1.getR28_carrying_amount().doubleValue());
//				     cell51.setCellStyle(numberStyle);
//				 } else {
//				     cell51.setCellValue("");
//				     cell51.setCellStyle(textStyle);
//				 }
//
//				  Cell cell52 = row.createCell(4);
//
//				 if (record1.getR28_no_of_accts() != null) {
//				     cell52.setCellValue(record1.getR28_no_of_accts().doubleValue());
//				     cell52.setCellStyle(numberStyle);
//				 } else {
//				     cell52.setCellValue("");
//				     cell52.setCellStyle(textStyle);
//				 }
//
//
//				 row = sheet.getRow(24) != null ? sheet.getRow(24) : sheet.createRow(24);
//				  Cell cell53 = row.createCell(2);
//
//				 if (record1.getR29_collateral_amount() != null) {
//				     cell53.setCellValue(record1.getR29_collateral_amount().doubleValue());
//				     cell53.setCellStyle(numberStyle);
//				 } else {
//				     cell53.setCellValue("");
//				     cell53.setCellStyle(textStyle);
//				 }
//
//				  Cell cell54 = row.createCell(3);
//
//				 if (record1.getR29_carrying_amount() != null) {
//				     cell54.setCellValue(record1.getR29_carrying_amount().doubleValue());
//				     cell54.setCellStyle(numberStyle);
//				 } else {
//				     cell54.setCellValue("");
//				     cell54.setCellStyle(textStyle);
//				 }
//
//				  Cell cell55 = row.createCell(4);
//
//				 if (record1.getR29_no_of_accts() != null) {
//				     cell55.setCellValue(record1.getR29_no_of_accts().doubleValue());
//				     cell55.setCellStyle(numberStyle);
//				 } else {
//				     cell55.setCellValue("");
//				     cell55.setCellStyle(textStyle);
//				 }
//
//
//				 row = sheet.getRow(25) != null ? sheet.getRow(25) : sheet.createRow(25);
//				  Cell cell56 = row.createCell(2);
//
//				 if (record1.getR30_collateral_amount() != null) {
//				     cell56.setCellValue(record1.getR30_collateral_amount().doubleValue());
//				     cell56.setCellStyle(numberStyle);
//				 } else {
//				     cell56.setCellValue("");
//				     cell56.setCellStyle(textStyle);
//				 }
//
//				  Cell cell57 = row.createCell(3);
//
//				 if (record1.getR30_carrying_amount() != null) {
//				     cell57.setCellValue(record1.getR30_carrying_amount().doubleValue());
//				     cell57.setCellStyle(numberStyle);
//				 } else {
//				     cell57.setCellValue("");
//				     cell57.setCellStyle(textStyle);
//				 }
//
//				  Cell cell58 = row.createCell(4);
//
//				 if (record1.getR30_no_of_accts() != null) {
//				     cell58.setCellValue(record1.getR30_no_of_accts().doubleValue());
//				     cell58.setCellStyle(numberStyle);
//				 } else {
//				     cell58.setCellValue("");
//				     cell58.setCellStyle(textStyle);
//				 }
//
//				 row = sheet.getRow(27) != null ? sheet.getRow(27) : sheet.createRow(27);
//				 Cell cell59 = row.createCell(2);
//
//				if (record1.getR32_collateral_amount() != null) {
//				    cell59.setCellValue(record1.getR32_collateral_amount().doubleValue());
//				    cell59.setCellStyle(numberStyle);
//				} else {
//				    cell59.setCellValue("");
//				    cell59.setCellStyle(textStyle);
//				}
//
//				 Cell cell60 = row.createCell(3);
//
//				if (record1.getR32_carrying_amount() != null) {
//				    cell60.setCellValue(record1.getR32_carrying_amount().doubleValue());
//				    cell60.setCellStyle(numberStyle);
//				} else {
//				    cell60.setCellValue("");
//				    cell60.setCellStyle(textStyle);
//				}
//
//				 Cell cell61 = row.createCell(4);
//
//				if (record1.getR32_no_of_accts() != null) {
//				    cell61.setCellValue(record1.getR32_no_of_accts().doubleValue());
//				    cell61.setCellStyle(numberStyle);
//				} else {
//				    cell61.setCellValue("");
//				    cell61.setCellStyle(textStyle);
//				}
//
//
//				 row = sheet.getRow(28) != null ? sheet.getRow(28) : sheet.createRow(28);
//				 Cell cell62 = row.createCell(2);
//
//				if (record1.getR33_collateral_amount() != null) {
//				    cell62.setCellValue(record1.getR33_collateral_amount().doubleValue());
//				    cell62.setCellStyle(numberStyle);
//				} else {
//				    cell62.setCellValue("");
//				    cell62.setCellStyle(textStyle);
//				}
//
//				 Cell cell63 = row.createCell(3);
//
//				if (record1.getR33_carrying_amount() != null) {
//				    cell63.setCellValue(record1.getR33_carrying_amount().doubleValue());
//				    cell63.setCellStyle(numberStyle);
//				} else {
//				    cell63.setCellValue("");
//				    cell63.setCellStyle(textStyle);
//				}
//
//				 Cell cell64 = row.createCell(4);
//
//				if (record1.getR33_no_of_accts() != null) {
//				    cell64.setCellValue(record1.getR33_no_of_accts().doubleValue());
//				    cell64.setCellStyle(numberStyle);
//				} else {
//				    cell64.setCellValue("");
//				    cell64.setCellStyle(textStyle);
//				}
//
//
//				 row = sheet.getRow(29) != null ? sheet.getRow(29) : sheet.createRow(29);
//				 Cell cell65 = row.createCell(2);
//
//				if (record1.getR31_collateral_amount() != null) {
//				    cell65.setCellValue(record1.getR31_collateral_amount().doubleValue());
//				    cell65.setCellStyle(numberStyle);
//				} else {
//				    cell65.setCellValue("");
//				    cell65.setCellStyle(textStyle);
//				}
//
//				 Cell cell66 = row.createCell(3);
//
//				if (record1.getR31_carrying_amount() != null) {
//				    cell66.setCellValue(record1.getR31_carrying_amount().doubleValue());
//				    cell66.setCellStyle(numberStyle);
//				} else {
//				    cell66.setCellValue("");
//				    cell66.setCellStyle(textStyle);
//				}
//
//				 Cell cell67 = row.createCell(4);
//
//				if (record1.getR31_no_of_accts() != null) {
//				    cell67.setCellValue(record1.getR31_no_of_accts().doubleValue());
//				    cell67.setCellStyle(numberStyle);
//				} else {
//				    cell67.setCellValue("");
//				    cell67.setCellStyle(textStyle);
//				}
//
//
//				 row = sheet.getRow(30) != null ? sheet.getRow(30) : sheet.createRow(30);
//				 Cell cell68 = row.createCell(2);
//
//				if (record1.getR34_collateral_amount() != null) {
//				    cell68.setCellValue(record1.getR34_collateral_amount().doubleValue());
//				    cell68.setCellStyle(numberStyle);
//				} else {
//				    cell68.setCellValue("");
//				    cell68.setCellStyle(textStyle);
//				}
//
//				 Cell cell69 = row.createCell(3);
//
//				if (record1.getR34_carrying_amount() != null) {
//				    cell69.setCellValue(record1.getR34_carrying_amount().doubleValue());
//				    cell69.setCellStyle(numberStyle);
//				} else {
//				    cell69.setCellValue("");
//				    cell69.setCellStyle(textStyle);
//				}
//
//				 Cell cell70 = row.createCell(4);
//
//				if (record1.getR34_no_of_accts() != null) {
//				    cell70.setCellValue(record1.getR34_no_of_accts().doubleValue());
//				    cell70.setCellStyle(numberStyle);
//				} else {
//				    cell70.setCellValue("");
//				    cell70.setCellStyle(textStyle);
//				}
//
//
//				 row = sheet.getRow(31) != null ? sheet.getRow(31) : sheet.createRow(31);
//				 Cell cell71 = row.createCell(2);
//
//				if (record1.getR36_collateral_amount() != null) {
//				    cell71.setCellValue(record1.getR36_collateral_amount().doubleValue());
//				    cell71.setCellStyle(numberStyle);
//				} else {
//				    cell71.setCellValue("");
//				    cell71.setCellStyle(textStyle);
//				}
//
//				 Cell cell72 = row.createCell(3);
//
//				if (record1.getR36_carrying_amount() != null) {
//				    cell72.setCellValue(record1.getR36_carrying_amount().doubleValue());
//				    cell72.setCellStyle(numberStyle);
//				} else {
//				    cell72.setCellValue("");
//				    cell72.setCellStyle(textStyle);
//				}
//
//				 Cell cell73 = row.createCell(4);
//
//				if (record1.getR36_no_of_accts() != null) {
//				    cell73.setCellValue(record1.getR36_no_of_accts().doubleValue());
//				    cell73.setCellStyle(numberStyle);
//				} else {
//				    cell73.setCellValue("");
//				    cell73.setCellStyle(textStyle);
//				}
//
//
//				 row = sheet.getRow(32) != null ? sheet.getRow(32) : sheet.createRow(32);
//				 Cell cell74 = row.createCell(2);
//
//				if (record1.getR35_collateral_amount() != null) {
//				    cell74.setCellValue(record1.getR35_collateral_amount().doubleValue());
//				    cell74.setCellStyle(numberStyle);
//				} else {
//				    cell74.setCellValue("");
//				    cell74.setCellStyle(textStyle);
//				}
//
//				 Cell cell75 = row.createCell(3);
//
//				if (record1.getR35_carrying_amount() != null) {
//				    cell75.setCellValue(record1.getR35_carrying_amount().doubleValue());
//				    cell75.setCellStyle(numberStyle);
//				} else {
//				    cell75.setCellValue("");
//				    cell75.setCellStyle(textStyle);
//				}
//
//				 Cell cell76 = row.createCell(4);
//
//				if (record1.getR35_no_of_accts() != null) {
//				    cell76.setCellValue(record1.getR35_no_of_accts().doubleValue());
//				    cell76.setCellStyle(numberStyle);
//				} else {
//				    cell76.setCellValue("");
//				    cell76.setCellStyle(textStyle);
//				}
//
//
//				 row = sheet.getRow(33) != null ? sheet.getRow(33) : sheet.createRow(33);
//				 Cell cell77 = row.createCell(2);
//
//				if (record1.getR37_collateral_amount() != null) {
//				    cell77.setCellValue(record1.getR37_collateral_amount().doubleValue());
//				    cell77.setCellStyle(numberStyle);
//				} else {
//				    cell77.setCellValue("");
//				    cell77.setCellStyle(textStyle);
//				}
//
//				 Cell cell78 = row.createCell(3);
//
//				if (record1.getR37_carrying_amount() != null) {
//				    cell78.setCellValue(record1.getR37_carrying_amount().doubleValue());
//				    cell78.setCellStyle(numberStyle);
//				} else {
//				    cell78.setCellValue("");
//				    cell78.setCellStyle(textStyle);
//				}
//
//				 Cell cell79 = row.createCell(4);
//
//				if (record1.getR37_no_of_accts() != null) {
//				    cell79.setCellValue(record1.getR37_no_of_accts().doubleValue());
//				    cell79.setCellStyle(numberStyle);
//				} else {
//				    cell79.setCellValue("");
//				    cell79.setCellStyle(textStyle);
//				}
//
//
//				 row = sheet.getRow(34) != null ? sheet.getRow(34) : sheet.createRow(34);
//				 Cell cell80 = row.createCell(2);
//
//				if (record1.getR38_collateral_amount() != null) {
//				    cell80.setCellValue(record1.getR38_collateral_amount().doubleValue());
//				    cell80.setCellStyle(numberStyle);
//				} else {
//				    cell80.setCellValue("");
//				    cell80.setCellStyle(textStyle);
//				}
//
//				 Cell cell81 = row.createCell(3);
//
//				if (record1.getR38_carrying_amount() != null) {
//				    cell81.setCellValue(record1.getR38_carrying_amount().doubleValue());
//				    cell81.setCellStyle(numberStyle);
//				} else {
//				    cell81.setCellValue("");
//				    cell81.setCellStyle(textStyle);
//				}
//
//				 Cell cell82 = row.createCell(4);
//
//				if (record1.getR38_no_of_accts() != null) {
//				    cell82.setCellValue(record1.getR38_no_of_accts().doubleValue());
//				    cell82.setCellStyle(numberStyle);
//				} else {
//				    cell82.setCellValue("");
//				    cell82.setCellStyle(textStyle);
//				}
//
//
//				 row = sheet.getRow(35) != null ? sheet.getRow(35) : sheet.createRow(35);
//				 Cell cell83 = row.createCell(2);
//
//				if (record1.getR39_collateral_amount() != null) {
//				    cell83.setCellValue(record1.getR39_collateral_amount().doubleValue());
//				    cell83.setCellStyle(numberStyle);
//				} else {
//				    cell83.setCellValue("");
//				    cell83.setCellStyle(textStyle);
//				}
//
//				 Cell cell84 = row.createCell(3);
//
//				if (record1.getR39_carrying_amount() != null) {
//				    cell84.setCellValue(record1.getR39_carrying_amount().doubleValue());
//				    cell84.setCellStyle(numberStyle);
//				} else {
//				    cell84.setCellValue("");
//				    cell84.setCellStyle(textStyle);
//				}
//
//				 Cell cell85 = row.createCell(4);
//
//				if (record1.getR39_no_of_accts() != null) {
//				    cell85.setCellValue(record1.getR39_no_of_accts().doubleValue());
//				    cell85.setCellStyle(numberStyle);
//				} else {
//				    cell85.setCellValue("");
//				    cell85.setCellStyle(textStyle);
//				}
//
//
//				 row = sheet.getRow(36) != null ? sheet.getRow(36) : sheet.createRow(36);
//				 Cell cell86 = row.createCell(2);
//
//				if (record1.getR40_collateral_amount() != null) {
//				    cell86.setCellValue(record1.getR40_collateral_amount().doubleValue());
//				    cell86.setCellStyle(numberStyle);
//				} else {
//				    cell86.setCellValue("");
//				    cell86.setCellStyle(textStyle);
//				}
//
//				 Cell cell87 = row.createCell(3);
//
//				if (record1.getR40_carrying_amount() != null) {
//				    cell87.setCellValue(record1.getR40_carrying_amount().doubleValue());
//				    cell87.setCellStyle(numberStyle);
//				} else {
//				    cell87.setCellValue("");
//				    cell87.setCellStyle(textStyle);
//				}
//
//				 Cell cell88 = row.createCell(4);
//
//				if (record1.getR40_no_of_accts() != null) {
//				    cell88.setCellValue(record1.getR40_no_of_accts().doubleValue());
//				    cell88.setCellStyle(numberStyle);
//				} else {
//				    cell88.setCellValue("");
//				    cell88.setCellStyle(textStyle);
//				}
//
//
//				 row = sheet.getRow(37) != null ? sheet.getRow(37) : sheet.createRow(37);
//				 Cell cell89 = row.createCell(2);
//
//				if (record1.getR41_collateral_amount() != null) {
//				    cell89.setCellValue(record1.getR41_collateral_amount().doubleValue());
//				    cell89.setCellStyle(numberStyle);
//				} else {
//				    cell89.setCellValue("");
//				    cell89.setCellStyle(textStyle);
//				}
//
//				 Cell cell90 = row.createCell(3);
//
//				if (record1.getR41_carrying_amount() != null) {
//				    cell90.setCellValue(record1.getR41_carrying_amount().doubleValue());
//				    cell90.setCellStyle(numberStyle);
//				} else {
//				    cell90.setCellValue("");
//				    cell90.setCellStyle(textStyle);
//				}
//
//				 Cell cell91 = row.createCell(4);
//
//				if (record1.getR41_no_of_accts() != null) {
//				    cell91.setCellValue(record1.getR41_no_of_accts().doubleValue());
//				    cell91.setCellStyle(numberStyle);
//				} else {
//				    cell91.setCellValue("");
//				    cell91.setCellStyle(textStyle);
//				}
//
//
//				 row = sheet.getRow(38) != null ? sheet.getRow(38) : sheet.createRow(38);
//				 Cell cell92 = row.createCell(2);
//
//				if (record1.getR42_collateral_amount() != null) {
//				    cell92.setCellValue(record1.getR42_collateral_amount().doubleValue());
//				    cell92.setCellStyle(numberStyle);
//				} else {
//				    cell92.setCellValue("");
//				    cell92.setCellStyle(textStyle);
//				}
//
//				 Cell cell93 = row.createCell(3);
//
//				if (record1.getR42_carrying_amount() != null) {
//				    cell93.setCellValue(record1.getR42_carrying_amount().doubleValue());
//				    cell93.setCellStyle(numberStyle);
//				} else {
//				    cell93.setCellValue("");
//				    cell93.setCellStyle(textStyle);
//				}
//
//				 Cell cell94 = row.createCell(4);
//
//				if (record1.getR42_no_of_accts() != null) {
//				    cell94.setCellValue(record1.getR42_no_of_accts().doubleValue());
//				    cell94.setCellStyle(numberStyle);
//				} else {
//				    cell94.setCellValue("");
//				    cell94.setCellStyle(textStyle);
//				}


				 row = sheet.getRow(39) != null ? sheet.getRow(39) : sheet.createRow(39);
				 Cell cell95 = row.createCell(2);

				if (record1.getR43_collateral_amount() != null) {
				    cell95.setCellValue(record1.getR43_collateral_amount().doubleValue());
				    cell95.setCellStyle(numberStyle);
				} else {
				    cell95.setCellValue("");
				    cell95.setCellStyle(textStyle);
				}

				 Cell cell96 = row.createCell(3);

				if (record1.getR43_carrying_amount() != null) {
				    cell96.setCellValue(record1.getR43_carrying_amount().doubleValue());
				    cell96.setCellStyle(numberStyle);
				} else {
				    cell96.setCellValue("");
				    cell96.setCellStyle(textStyle);
				}

				 Cell cell97 = row.createCell(5);

				if (record1.getR43_no_of_accts() != null) {
				    cell97.setCellValue(record1.getR43_no_of_accts().doubleValue());
				    cell97.setCellStyle(numberStyle);
				} else {
				    cell97.setCellValue("");
				    cell97.setCellStyle(textStyle);
				}


				 row = sheet.getRow(40) != null ? sheet.getRow(40) : sheet.createRow(40);
				 Cell cell98 = row.createCell(2);

				if (record1.getR44_collateral_amount() != null) {
				    cell98.setCellValue(record1.getR44_collateral_amount().doubleValue());
				    cell98.setCellStyle(numberStyle);
				} else {
				    cell98.setCellValue("");
				    cell98.setCellStyle(textStyle);
				}

				 Cell cell99 = row.createCell(3);

				if (record1.getR44_carrying_amount() != null) {
				    cell99.setCellValue(record1.getR44_carrying_amount().doubleValue());
				    cell99.setCellStyle(numberStyle);
				} else {
				    cell99.setCellValue("");
				    cell99.setCellStyle(textStyle);
				}

				 Cell cell100 = row.createCell(5);

				if (record1.getR44_no_of_accts() != null) {
				    cell100.setCellValue(record1.getR44_no_of_accts().doubleValue());
				    cell100.setCellStyle(numberStyle);
				} else {
				    cell100.setCellValue("");
				    cell100.setCellStyle(textStyle);
				}


				 row = sheet.getRow(41) != null ? sheet.getRow(41) : sheet.createRow(41);
				 Cell cell101 = row.createCell(2);

				if (record1.getR45_collateral_amount() != null) {
				    cell101.setCellValue(record1.getR45_collateral_amount().doubleValue());
				    cell101.setCellStyle(numberStyle);
				} else {
				    cell101.setCellValue("");
				    cell101.setCellStyle(textStyle);
				}

				 Cell cell102 = row.createCell(3);

				if (record1.getR45_carrying_amount() != null) {
				    cell102.setCellValue(record1.getR45_carrying_amount().doubleValue());
				    cell102.setCellStyle(numberStyle);
				} else {
				    cell102.setCellValue("");
				    cell102.setCellStyle(textStyle);
				}

				 Cell cell103 = row.createCell(5);

				if (record1.getR45_no_of_accts() != null) {
				    cell103.setCellValue(record1.getR45_no_of_accts().doubleValue());
				    cell103.setCellStyle(numberStyle);
				} else {
				    cell103.setCellValue("");
				    cell103.setCellStyle(textStyle);
				}

				 row = sheet.getRow(43) != null ? sheet.getRow(43) : sheet.createRow(43);
				 Cell cell104 = row.createCell(2);

				if (record1.getR47_collateral_amount() != null) {
				    cell104.setCellValue(record1.getR47_collateral_amount().doubleValue());
				    cell104.setCellStyle(numberStyle);
				} else {
				    cell104.setCellValue("");
				    cell104.setCellStyle(textStyle);
				}

				 Cell cell105 = row.createCell(3);

				if (record1.getR47_carrying_amount() != null) {
				    cell105.setCellValue(record1.getR47_carrying_amount().doubleValue());
				    cell105.setCellStyle(numberStyle);
				} else {
				    cell105.setCellValue("");
				    cell105.setCellStyle(textStyle);
				}

				 Cell cell106 = row.createCell(5);

				if (record1.getR47_no_of_accts() != null) {
				    cell106.setCellValue(record1.getR47_no_of_accts().doubleValue());
				    cell106.setCellStyle(numberStyle);
				} else {
				    cell106.setCellValue("");
				    cell106.setCellStyle(textStyle);
				}


				 row = sheet.getRow(44) != null ? sheet.getRow(44) : sheet.createRow(44);
				 Cell cell107 = row.createCell(2);

				if (record1.getR48_collateral_amount() != null) {
				    cell107.setCellValue(record1.getR48_collateral_amount().doubleValue());
				    cell107.setCellStyle(numberStyle);
				} else {
				    cell107.setCellValue("");
				    cell107.setCellStyle(textStyle);
				}

				 Cell cell108 = row.createCell(3);

				if (record1.getR48_carrying_amount() != null) {
				    cell108.setCellValue(record1.getR48_carrying_amount().doubleValue());
				    cell108.setCellStyle(numberStyle);
				} else {
				    cell108.setCellValue("");
				    cell108.setCellStyle(textStyle);
				}

				 Cell cell109 = row.createCell(5);

				if (record1.getR48_no_of_accts() != null) {
				    cell109.setCellValue(record1.getR48_no_of_accts().doubleValue());
				    cell109.setCellStyle(numberStyle);
				} else {
				    cell109.setCellValue("");
				    cell109.setCellStyle(textStyle);
				}


				 row = sheet.getRow(45) != null ? sheet.getRow(45) : sheet.createRow(45);
				 Cell cell110 = row.createCell(2);

				if (record1.getR49_collateral_amount() != null) {
				    cell110.setCellValue(record1.getR49_collateral_amount().doubleValue());
				    cell110.setCellStyle(numberStyle);
				} else {
				    cell110.setCellValue("");
				    cell110.setCellStyle(textStyle);
				}

				 Cell cell111 = row.createCell(3);

				if (record1.getR49_carrying_amount() != null) {
				    cell111.setCellValue(record1.getR49_carrying_amount().doubleValue());
				    cell111.setCellStyle(numberStyle);
				} else {
				    cell111.setCellValue("");
				    cell111.setCellStyle(textStyle);
				}

				 Cell cell112 = row.createCell(5);

				if (record1.getR49_no_of_accts() != null) {
				    cell112.setCellValue(record1.getR49_no_of_accts().doubleValue());
				    cell112.setCellStyle(numberStyle);
				} else {
				    cell112.setCellValue("");
				    cell112.setCellStyle(textStyle);
				}


				 row = sheet.getRow(46) != null ? sheet.getRow(46) : sheet.createRow(46);
				 Cell cell113 = row.createCell(2);

				if (record1.getR50_collateral_amount() != null) {
				    cell113.setCellValue(record1.getR50_collateral_amount().doubleValue());
				    cell113.setCellStyle(numberStyle);
				} else {
				    cell113.setCellValue("");
				    cell113.setCellStyle(textStyle);
				}

				 Cell cell114 = row.createCell(3);

				if (record1.getR50_carrying_amount() != null) {
				    cell114.setCellValue(record1.getR50_carrying_amount().doubleValue());
				    cell114.setCellStyle(numberStyle);
				} else {
				    cell114.setCellValue("");
				    cell114.setCellStyle(textStyle);
				}

				 Cell cell115 = row.createCell(5);

				if (record1.getR50_no_of_accts() != null) {
				    cell115.setCellValue(record1.getR50_no_of_accts().doubleValue());
				    cell115.setCellStyle(numberStyle);
				} else {
				    cell115.setCellValue("");
				    cell115.setCellStyle(textStyle);
				}


				 row = sheet.getRow(47) != null ? sheet.getRow(47) : sheet.createRow(47);
				 Cell cell116 = row.createCell(2);

				if (record1.getR52_collateral_amount() != null) {
				    cell116.setCellValue(record1.getR52_collateral_amount().doubleValue());
				    cell116.setCellStyle(numberStyle);
				} else {
				    cell116.setCellValue("");
				    cell116.setCellStyle(textStyle);
				}

				 Cell cell117 = row.createCell(3);

				if (record1.getR52_carrying_amount() != null) {
				    cell117.setCellValue(record1.getR52_carrying_amount().doubleValue());
				    cell117.setCellStyle(numberStyle);
				} else {
				    cell117.setCellValue("");
				    cell117.setCellStyle(textStyle);
				}

				 Cell cell118 = row.createCell(5);

				if (record1.getR52_no_of_accts() != null) {
				    cell118.setCellValue(record1.getR52_no_of_accts().doubleValue());
				    cell118.setCellStyle(numberStyle);
				} else {
				    cell118.setCellValue("");
				    cell118.setCellStyle(textStyle);
				}

				 row = sheet.getRow(49) != null ? sheet.getRow(49) : sheet.createRow(49);
				 Cell cell119 = row.createCell(2);

				if (record1.getR53_collateral_amount() != null) {
				    cell119.setCellValue(record1.getR53_collateral_amount().doubleValue());
				    cell119.setCellStyle(numberStyle);
				} else {
				    cell119.setCellValue("");
				    cell119.setCellStyle(textStyle);
				}

				 Cell cell120 = row.createCell(3);

				if (record1.getR53_carrying_amount() != null) {
				    cell120.setCellValue(record1.getR53_carrying_amount().doubleValue());
				    cell120.setCellStyle(numberStyle);
				} else {
				    cell120.setCellValue("");
				    cell120.setCellStyle(textStyle);
				}

				 Cell cell121 = row.createCell(5);

				if (record1.getR53_no_of_accts() != null) {
				    cell121.setCellValue(record1.getR53_no_of_accts().doubleValue());
				    cell121.setCellStyle(numberStyle);
				} else {
				    cell121.setCellValue("");
				    cell121.setCellStyle(textStyle);
				}


				 row = sheet.getRow(50) != null ? sheet.getRow(50) : sheet.createRow(50);
				 Cell cell122 = row.createCell(2);

				if (record1.getR54_collateral_amount() != null) {
				    cell122.setCellValue(record1.getR54_collateral_amount().doubleValue());
				    cell122.setCellStyle(numberStyle);
				} else {
				    cell122.setCellValue("");
				    cell122.setCellStyle(textStyle);
				}

				 Cell cell123 = row.createCell(3);

				if (record1.getR54_carrying_amount() != null) {
				    cell123.setCellValue(record1.getR54_carrying_amount().doubleValue());
				    cell123.setCellStyle(numberStyle);
				} else {
				    cell123.setCellValue("");
				    cell123.setCellStyle(textStyle);
				}

				 Cell cell124 = row.createCell(5);

				if (record1.getR54_no_of_accts() != null) {
				    cell124.setCellValue(record1.getR54_no_of_accts().doubleValue());
				    cell124.setCellStyle(numberStyle);
				} else {
				    cell124.setCellValue("");
				    cell124.setCellStyle(textStyle);
				}


				 row = sheet.getRow(51) != null ? sheet.getRow(51) : sheet.createRow(51);
				 Cell cell125 = row.createCell(2);

				if (record1.getR55_collateral_amount() != null) {
				    cell125.setCellValue(record1.getR55_collateral_amount().doubleValue());
				    cell125.setCellStyle(numberStyle);
				} else {
				    cell125.setCellValue("");
				    cell125.setCellStyle(textStyle);
				}

				 Cell cell126 = row.createCell(3);

				if (record1.getR55_carrying_amount() != null) {
				    cell126.setCellValue(record1.getR55_carrying_amount().doubleValue());
				    cell126.setCellStyle(numberStyle);
				} else {
				    cell126.setCellValue("");
				    cell126.setCellStyle(textStyle);
				}

				 Cell cell127 = row.createCell(5);

				if (record1.getR55_no_of_accts() != null) {
				    cell127.setCellValue(record1.getR55_no_of_accts().doubleValue());
				    cell127.setCellStyle(numberStyle);
				} else {
				    cell127.setCellValue("");
				    cell127.setCellStyle(textStyle);
				}


				 row = sheet.getRow(52) != null ? sheet.getRow(52) : sheet.createRow(52);
				 Cell cell128 = row.createCell(2);

				if (record1.getR56_collateral_amount() != null) {
				    cell128.setCellValue(record1.getR56_collateral_amount().doubleValue());
				    cell128.setCellStyle(numberStyle);
				} else {
				    cell128.setCellValue("");
				    cell128.setCellStyle(textStyle);
				}

				 Cell cell129 = row.createCell(3);

				if (record1.getR56_carrying_amount() != null) {
				    cell129.setCellValue(record1.getR56_carrying_amount().doubleValue());
				    cell129.setCellStyle(numberStyle);
				} else {
				    cell129.setCellValue("");
				    cell129.setCellStyle(textStyle);
				}

				 Cell cell130 = row.createCell(5);

				if (record1.getR56_no_of_accts() != null) {
				    cell130.setCellValue(record1.getR56_no_of_accts().doubleValue());
				    cell130.setCellStyle(numberStyle);
				} else {
				    cell130.setCellValue("");
				    cell130.setCellStyle(textStyle);
				}


				 row = sheet.getRow(53) != null ? sheet.getRow(53) : sheet.createRow(53);
				 Cell cell131 = row.createCell(2);

				if (record1.getR58_collateral_amount() != null) {
				    cell131.setCellValue(record1.getR58_collateral_amount().doubleValue());
				    cell131.setCellStyle(numberStyle);
				} else {
				    cell131.setCellValue("");
				    cell131.setCellStyle(textStyle);
				}

				 Cell cell132 = row.createCell(3);

				if (record1.getR58_carrying_amount() != null) {
				    cell132.setCellValue(record1.getR58_carrying_amount().doubleValue());
				    cell132.setCellStyle(numberStyle);
				} else {
				    cell132.setCellValue("");
				    cell132.setCellStyle(textStyle);
				}

				 Cell cell133 = row.createCell(5);

				if (record1.getR58_no_of_accts() != null) {
				    cell133.setCellValue(record1.getR58_no_of_accts().doubleValue());
				    cell133.setCellStyle(numberStyle);
				} else {
				    cell133.setCellValue("");
				    cell133.setCellStyle(textStyle);
				}


				 row = sheet.getRow(54) != null ? sheet.getRow(54) : sheet.createRow(54);
				 Cell cell134 = row.createCell(2);

				if (record1.getR59_collateral_amount() != null) {
				    cell134.setCellValue(record1.getR59_collateral_amount().doubleValue());
				    cell134.setCellStyle(numberStyle);
				} else {
				    cell134.setCellValue("");
				    cell134.setCellStyle(textStyle);
				}

				 Cell cell135 = row.createCell(3);

				if (record1.getR59_carrying_amount() != null) {
				    cell135.setCellValue(record1.getR59_carrying_amount().doubleValue());
				    cell135.setCellStyle(numberStyle);
				} else {
				    cell135.setCellValue("");
				    cell135.setCellStyle(textStyle);
				}

				 Cell cell136 = row.createCell(5);

				if (record1.getR59_no_of_accts() != null) {
				    cell136.setCellValue(record1.getR59_no_of_accts().doubleValue());
				    cell136.setCellStyle(numberStyle);
				} else {
				    cell136.setCellValue("");
				    cell136.setCellStyle(textStyle);
				}


				 row = sheet.getRow(55) != null ? sheet.getRow(55) : sheet.createRow(55);
				 Cell cell137 = row.createCell(2);

				if (record1.getR60_collateral_amount() != null) {
				    cell137.setCellValue(record1.getR60_collateral_amount().doubleValue());
				    cell137.setCellStyle(numberStyle);
				} else {
				    cell137.setCellValue("");
				    cell137.setCellStyle(textStyle);
				}

				 Cell cell138 = row.createCell(3);

				if (record1.getR60_carrying_amount() != null) {
				    cell138.setCellValue(record1.getR60_carrying_amount().doubleValue());
				    cell138.setCellStyle(numberStyle);
				} else {
				    cell138.setCellValue("");
				    cell138.setCellStyle(textStyle);
				}

				 Cell cell139 = row.createCell(5);

				if (record1.getR60_no_of_accts() != null) {
				    cell139.setCellValue(record1.getR60_no_of_accts().doubleValue());
				    cell139.setCellStyle(numberStyle);
				} else {
				    cell139.setCellValue("");
				    cell139.setCellStyle(textStyle);
				}


				 row = sheet.getRow(56) != null ? sheet.getRow(56) : sheet.createRow(56);
				 Cell cell140 = row.createCell(2);

				if (record1.getR61_collateral_amount() != null) {
				    cell140.setCellValue(record1.getR61_collateral_amount().doubleValue());
				    cell140.setCellStyle(numberStyle);
				} else {
				    cell140.setCellValue("");
				    cell140.setCellStyle(textStyle);
				}

				 Cell cell141 = row.createCell(3);

				if (record1.getR61_carrying_amount() != null) {
				    cell141.setCellValue(record1.getR61_carrying_amount().doubleValue());
				    cell141.setCellStyle(numberStyle);
				} else {
				    cell141.setCellValue("");
				    cell141.setCellStyle(textStyle);
				}

				 Cell cell142 = row.createCell(5);

				if (record1.getR61_no_of_accts() != null) {
				    cell142.setCellValue(record1.getR61_no_of_accts().doubleValue());
				    cell142.setCellStyle(numberStyle);
				} else {
				    cell142.setCellValue("");
				    cell142.setCellStyle(textStyle);
				}


				 row = sheet.getRow(57) != null ? sheet.getRow(57) : sheet.createRow(57);
				 Cell cell143 = row.createCell(2);

				if (record1.getR62_collateral_amount() != null) {
				    cell143.setCellValue(record1.getR62_collateral_amount().doubleValue());
				    cell143.setCellStyle(numberStyle);
				} else {
				    cell143.setCellValue("");
				    cell143.setCellStyle(textStyle);
				}

				 Cell cell144 = row.createCell(3);

				if (record1.getR62_carrying_amount() != null) {
				    cell144.setCellValue(record1.getR62_carrying_amount().doubleValue());
				    cell144.setCellStyle(numberStyle);
				} else {
				    cell144.setCellValue("");
				    cell144.setCellStyle(textStyle);
				}

				 Cell cell145 = row.createCell(5);

				if (record1.getR62_no_of_accts() != null) {
				    cell145.setCellValue(record1.getR62_no_of_accts().doubleValue());
				    cell145.setCellStyle(numberStyle);
				} else {
				    cell145.setCellValue("");
				    cell145.setCellStyle(textStyle);
				}


				 row = sheet.getRow(58) != null ? sheet.getRow(58) : sheet.createRow(58);
				 Cell cell146 = row.createCell(2);

				if (record1.getR63_collateral_amount() != null) {
				    cell146.setCellValue(record1.getR63_collateral_amount().doubleValue());
				    cell146.setCellStyle(numberStyle);
				} else {
				    cell146.setCellValue("");
				    cell146.setCellStyle(textStyle);
				}

				 Cell cell147 = row.createCell(3);

				if (record1.getR63_carrying_amount() != null) {
				    cell147.setCellValue(record1.getR63_carrying_amount().doubleValue());
				    cell147.setCellStyle(numberStyle);
				} else {
				    cell147.setCellValue("");
				    cell147.setCellStyle(textStyle);
				}

				 Cell cell148 = row.createCell(5);

				if (record1.getR63_no_of_accts() != null) {
				    cell148.setCellValue(record1.getR63_no_of_accts().doubleValue());
				    cell148.setCellStyle(numberStyle);
				} else {
				    cell148.setCellValue("");
				    cell148.setCellStyle(textStyle);
				}






				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();

				// Write the final workbook content to the in-memory stream.
				workbook.write(out);

				logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

				return out.toByteArray();
				
			}
			int startRow = 10;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					Q_RLFA1_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row11
					row = sheet.getRow(10);

					// Column 2 -B

					Cell cellB = row.createCell(1);
					if (record.getR11_collateral_amount() != null) {
						cellB.setCellValue(record.getR11_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					Cell cellC = row.createCell(2);
					if (record.getR11_carrying_amount() != null) {
						cellC.setCellValue(record.getR11_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}
					
					// Column 4-D
					Cell cellD = row.createCell(3);
					if (record.getR11_no_of_accts() != null) {
						cellD.setCellValue(record.getR11_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}
					
					// row12
					row = sheet.getRow(11);

					// Column 2 -B

					 cellB = row.createCell(1);
					if (record.getR12_collateral_amount() != null) {
						cellB.setCellValue(record.getR12_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					 cellC = row.createCell(2);
					if (record.getR12_carrying_amount() != null) {
						cellC.setCellValue(record.getR12_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}
					
					// Column 4-D
				 cellD = row.createCell(3);
					if (record.getR12_no_of_accts() != null) {
						cellD.setCellValue(record.getR12_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}
					
					
					// row13
					row = sheet.getRow(12);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR13_collateral_amount() != null) {
					    cellB.setCellValue(record.getR13_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR13_carrying_amount() != null) {
					    cellC.setCellValue(record.getR13_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR13_no_of_accts() != null) {
					    cellD.setCellValue(record.getR13_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row15
					row = sheet.getRow(14);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR15_collateral_amount() != null) {
					    cellB.setCellValue(record.getR15_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR15_carrying_amount() != null) {
					    cellC.setCellValue(record.getR15_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR15_no_of_accts() != null) {
					    cellD.setCellValue(record.getR15_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}


					// row16
					row = sheet.getRow(15);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR16_collateral_amount() != null) {
					    cellB.setCellValue(record.getR16_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR16_carrying_amount() != null) {
					    cellC.setCellValue(record.getR16_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR16_no_of_accts() != null) {
					    cellD.setCellValue(record.getR16_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR17_collateral_amount() != null) {
					    cellB.setCellValue(record.getR17_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR17_carrying_amount() != null) {
					    cellC.setCellValue(record.getR17_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR17_no_of_accts() != null) {
					    cellD.setCellValue(record.getR17_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row18
					row = sheet.getRow(17);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR18_collateral_amount() != null) {
					    cellB.setCellValue(record.getR18_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR18_carrying_amount() != null) {
					    cellC.setCellValue(record.getR18_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR18_no_of_accts() != null) {
					    cellD.setCellValue(record.getR18_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR19_collateral_amount() != null) {
					    cellB.setCellValue(record.getR19_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR19_carrying_amount() != null) {
					    cellC.setCellValue(record.getR19_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR19_no_of_accts() != null) {
					    cellD.setCellValue(record.getR19_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR20_collateral_amount() != null) {
					    cellB.setCellValue(record.getR20_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR20_carrying_amount() != null) {
					    cellC.setCellValue(record.getR20_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR20_no_of_accts() != null) {
					    cellD.setCellValue(record.getR20_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR21_collateral_amount() != null) {
					    cellB.setCellValue(record.getR21_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR21_carrying_amount() != null) {
					    cellC.setCellValue(record.getR21_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR21_no_of_accts() != null) {
					    cellD.setCellValue(record.getR21_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR22_collateral_amount() != null) {
					    cellB.setCellValue(record.getR22_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR22_carrying_amount() != null) {
					    cellC.setCellValue(record.getR22_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR22_no_of_accts() != null) {
					    cellD.setCellValue(record.getR22_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR23_collateral_amount() != null) {
					    cellB.setCellValue(record.getR23_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR23_carrying_amount() != null) {
					    cellC.setCellValue(record.getR23_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR23_no_of_accts() != null) {
					    cellD.setCellValue(record.getR23_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR24_collateral_amount() != null) {
					    cellB.setCellValue(record.getR24_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR24_carrying_amount() != null) {
					    cellC.setCellValue(record.getR24_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR24_no_of_accts() != null) {
					    cellD.setCellValue(record.getR24_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR25_collateral_amount() != null) {
					    cellB.setCellValue(record.getR25_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR25_carrying_amount() != null) {
					    cellC.setCellValue(record.getR25_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR25_no_of_accts() != null) {
					    cellD.setCellValue(record.getR25_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR26_collateral_amount() != null) {
					    cellB.setCellValue(record.getR26_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR26_carrying_amount() != null) {
					    cellC.setCellValue(record.getR26_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR26_no_of_accts() != null) {
					    cellD.setCellValue(record.getR26_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR27_collateral_amount() != null) {
					    cellB.setCellValue(record.getR27_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR27_carrying_amount() != null) {
					    cellC.setCellValue(record.getR27_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR27_no_of_accts() != null) {
					    cellD.setCellValue(record.getR27_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					
					// row29
					row = sheet.getRow(28);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR29_collateral_amount() != null) {
					    cellB.setCellValue(record.getR29_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR29_carrying_amount() != null) {
					    cellC.setCellValue(record.getR29_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR29_no_of_accts() != null) {
					    cellD.setCellValue(record.getR29_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR30_collateral_amount() != null) {
					    cellB.setCellValue(record.getR30_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR30_carrying_amount() != null) {
					    cellC.setCellValue(record.getR30_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR30_no_of_accts() != null) {
					    cellD.setCellValue(record.getR30_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR31_collateral_amount() != null) {
					    cellB.setCellValue(record.getR31_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR31_carrying_amount() != null) {
					    cellC.setCellValue(record.getR31_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR31_no_of_accts() != null) {
					    cellD.setCellValue(record.getR31_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR32_collateral_amount() != null) {
					    cellB.setCellValue(record.getR32_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR32_carrying_amount() != null) {
					    cellC.setCellValue(record.getR32_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR32_no_of_accts() != null) {
					    cellD.setCellValue(record.getR32_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR33_collateral_amount() != null) {
					    cellB.setCellValue(record.getR33_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR33_carrying_amount() != null) {
					    cellC.setCellValue(record.getR33_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR33_no_of_accts() != null) {
					    cellD.setCellValue(record.getR33_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR34_collateral_amount() != null) {
					    cellB.setCellValue(record.getR34_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR34_carrying_amount() != null) {
					    cellC.setCellValue(record.getR34_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR34_no_of_accts() != null) {
					    cellD.setCellValue(record.getR34_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR35_collateral_amount() != null) {
					    cellB.setCellValue(record.getR35_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR35_carrying_amount() != null) {
					    cellC.setCellValue(record.getR35_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR35_no_of_accts() != null) {
					    cellD.setCellValue(record.getR35_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR36_collateral_amount() != null) {
					    cellB.setCellValue(record.getR36_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR36_carrying_amount() != null) {
					    cellC.setCellValue(record.getR36_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR36_no_of_accts() != null) {
					    cellD.setCellValue(record.getR36_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row38
					row = sheet.getRow(37);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR38_collateral_amount() != null) {
					    cellB.setCellValue(record.getR38_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR38_carrying_amount() != null) {
					    cellC.setCellValue(record.getR38_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR38_no_of_accts() != null) {
					    cellD.setCellValue(record.getR38_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(38);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR39_collateral_amount() != null) {
					    cellB.setCellValue(record.getR39_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR39_carrying_amount() != null) {
					    cellC.setCellValue(record.getR39_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR39_no_of_accts() != null) {
					    cellD.setCellValue(record.getR39_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row41
					row = sheet.getRow(40);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR41_collateral_amount() != null) {
					    cellB.setCellValue(record.getR41_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR41_carrying_amount() != null) {
					    cellC.setCellValue(record.getR41_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR41_no_of_accts() != null) {
					    cellD.setCellValue(record.getR41_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row42
					row = sheet.getRow(41);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR42_collateral_amount() != null) {
					    cellB.setCellValue(record.getR42_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR42_carrying_amount() != null) {
					    cellC.setCellValue(record.getR42_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR42_no_of_accts() != null) {
					    cellD.setCellValue(record.getR42_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					
					// row44
					row = sheet.getRow(43);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR44_collateral_amount() != null) {
					    cellB.setCellValue(record.getR44_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR44_carrying_amount() != null) {
					    cellC.setCellValue(record.getR44_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR44_no_of_accts() != null) {
					    cellD.setCellValue(record.getR44_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row45
					row = sheet.getRow(44);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR45_collateral_amount() != null) {
					    cellB.setCellValue(record.getR45_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR45_carrying_amount() != null) {
					    cellC.setCellValue(record.getR45_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR45_no_of_accts() != null) {
					    cellD.setCellValue(record.getR45_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row46
					row = sheet.getRow(45);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR46_collateral_amount() != null) {
					    cellB.setCellValue(record.getR46_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR46_carrying_amount() != null) {
					    cellC.setCellValue(record.getR46_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR46_no_of_accts() != null) {
					    cellD.setCellValue(record.getR46_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row47
					row = sheet.getRow(46);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR47_collateral_amount() != null) {
					    cellB.setCellValue(record.getR47_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR47_carrying_amount() != null) {
					    cellC.setCellValue(record.getR47_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR47_no_of_accts() != null) {
					    cellD.setCellValue(record.getR47_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row49
					row = sheet.getRow(48);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR49_collateral_amount() != null) {
					    cellB.setCellValue(record.getR49_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR49_carrying_amount() != null) {
					    cellC.setCellValue(record.getR49_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR49_no_of_accts() != null) {
					    cellD.setCellValue(record.getR49_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row50
					row = sheet.getRow(49);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR50_collateral_amount() != null) {
					    cellB.setCellValue(record.getR50_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR50_carrying_amount() != null) {
					    cellC.setCellValue(record.getR50_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR50_no_of_accts() != null) {
					    cellD.setCellValue(record.getR50_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(50);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR51_collateral_amount() != null) {
					    cellB.setCellValue(record.getR51_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR51_carrying_amount() != null) {
					    cellC.setCellValue(record.getR51_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR51_no_of_accts() != null) {
					    cellD.setCellValue(record.getR51_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					
					// row53
					row = sheet.getRow(52);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR53_collateral_amount() != null) {
					    cellB.setCellValue(record.getR53_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR53_carrying_amount() != null) {
					    cellC.setCellValue(record.getR53_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR53_no_of_accts() != null) {
					    cellD.setCellValue(record.getR53_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR54_collateral_amount() != null) {
					    cellB.setCellValue(record.getR54_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR54_carrying_amount() != null) {
					    cellC.setCellValue(record.getR54_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR54_no_of_accts() != null) {
					    cellD.setCellValue(record.getR54_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(54);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR55_collateral_amount() != null) {
					    cellB.setCellValue(record.getR55_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR55_carrying_amount() != null) {
					    cellC.setCellValue(record.getR55_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR55_no_of_accts() != null) {
					    cellD.setCellValue(record.getR55_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}


					
					// row57
					row = sheet.getRow(56);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR57_collateral_amount() != null) {
					    cellB.setCellValue(record.getR57_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR57_carrying_amount() != null) {
					    cellC.setCellValue(record.getR57_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR57_no_of_accts() != null) {
					    cellD.setCellValue(record.getR57_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row58
					row = sheet.getRow(57);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR58_collateral_amount() != null) {
					    cellB.setCellValue(record.getR58_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR58_carrying_amount() != null) {
					    cellC.setCellValue(record.getR58_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR58_no_of_accts() != null) {
					    cellD.setCellValue(record.getR58_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row59
					row = sheet.getRow(58);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR59_collateral_amount() != null) {
					    cellB.setCellValue(record.getR59_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR59_carrying_amount() != null) {
					    cellC.setCellValue(record.getR59_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR59_no_of_accts() != null) {
					    cellD.setCellValue(record.getR59_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row60
					row = sheet.getRow(59);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR60_collateral_amount() != null) {
					    cellB.setCellValue(record.getR60_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR60_carrying_amount() != null) {
					    cellC.setCellValue(record.getR60_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR60_no_of_accts() != null) {
					    cellD.setCellValue(record.getR60_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row61
					row = sheet.getRow(60);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR61_collateral_amount() != null) {
					    cellB.setCellValue(record.getR61_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR61_carrying_amount() != null) {
					    cellC.setCellValue(record.getR61_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR61_no_of_accts() != null) {
					    cellD.setCellValue(record.getR61_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row62
					row = sheet.getRow(61);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR62_collateral_amount() != null) {
					    cellB.setCellValue(record.getR62_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR62_carrying_amount() != null) {
					    cellC.setCellValue(record.getR62_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR62_no_of_accts() != null) {
					    cellD.setCellValue(record.getR62_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
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
	 * public List<Object> getQ_RLFA1Archival() { List<Object> Q_RLFA1Archivallist =
	 * new ArrayList<>(); try { Q_RLFA1Archivallist =
	 * q_rlfa1_Archival_Summary_Repo.getQ_RLFA1archival();
	 * 
	 * System.out.println("countser" + Q_RLFA1Archivallist.size());
	 * 
	 * } catch (Exception e) { // Log the exception
	 * System.err.println("Error fetching Q_RLFA1 Archival data: " +
	 * e.getMessage()); e.printStackTrace();
	 * 
	 * // Optionally, you can rethrow it or return empty list // throw new
	 * RuntimeException("Failed to fetch data", e); } return Q_RLFA1Archivallist; }
	 * 
	 */
	
	
	public List<Object[]> getQ_RLFA1Archival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<Q_RLFA1_Archival_Summary_Entity> repoData = q_rlfa1_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (repoData != null && !repoData.isEmpty()) {
				for (Q_RLFA1_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] {
							entity.getReport_date(), 
							entity.getReport_version(),
							entity.getReportResubDate()
					};
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				Q_RLFA1_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReport_version());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching Q_RLFA1 Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}
	public byte[] getExcelQ_RLFA1ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<Q_RLFA1_Archival_Summary_Entity> dataList = q_rlfa1_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Q_RLFA1 report. Returning empty result.");
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

	if("email_report".equalsIgnoreCase(dtltype)) {
				
		Q_RLFA1_Archival_Summary_Entity record1 = dataList.get(0);
				
				Row row = sheet.getRow(7) != null ? sheet.getRow(7) : sheet.createRow(7);
				 Cell cell2 = row.createCell(2);

				 if (record1.getR10_collateral_amount() != null) {
				     cell2.setCellValue(record1.getR10_collateral_amount().doubleValue());
				     cell2.setCellStyle(numberStyle);
				 } else {
				     cell2.setCellValue("");
				     cell2.setCellStyle(textStyle);
				 }

				  Cell cell3 = row.createCell(3);

				 if (record1.getR10_carrying_amount() != null) {
				     cell3.setCellValue(record1.getR10_carrying_amount().doubleValue());
				     cell3.setCellStyle(numberStyle);
				 } else {
				     cell3.setCellValue("");
				     cell3.setCellStyle(textStyle);
				 }

				  Cell cell4 = row.createCell(5);

				 if (record1.getR10_no_of_accts() != null) {
				     cell4.setCellValue(record1.getR10_no_of_accts().doubleValue());
				     cell4.setCellStyle(numberStyle);
				 } else {
				     cell4.setCellValue("");
				     cell4.setCellStyle(textStyle);
				 }


				 row = sheet.getRow(8) != null ? sheet.getRow(8) : sheet.createRow(8);
				  Cell cell5 = row.createCell(2);

				 if (record1.getR11_collateral_amount() != null) {
				     cell5.setCellValue(record1.getR11_collateral_amount().doubleValue());
				     cell5.setCellStyle(numberStyle);
				 } else {
				     cell5.setCellValue("");
				     cell5.setCellStyle(textStyle);
				 }

				  Cell cell6 = row.createCell(3);

				 if (record1.getR11_carrying_amount() != null) {
				     cell6.setCellValue(record1.getR11_carrying_amount().doubleValue());
				     cell6.setCellStyle(numberStyle);
				 } else {
				     cell6.setCellValue("");
				     cell6.setCellStyle(textStyle);
				 }

				  Cell cell7 = row.createCell(5);

				 if (record1.getR11_no_of_accts() != null) {
				     cell7.setCellValue(record1.getR11_no_of_accts().doubleValue());
				     cell7.setCellStyle(numberStyle);
				 } else {
				     cell7.setCellValue("");
				     cell7.setCellStyle(textStyle);
				 }


				 row = sheet.getRow(9) != null ? sheet.getRow(9) : sheet.createRow(9);
				  Cell cell8 = row.createCell(2);

				 if (record1.getR12_collateral_amount() != null) {
				     cell8.setCellValue(record1.getR12_collateral_amount().doubleValue());
				     cell8.setCellStyle(numberStyle);
				 } else {
				     cell8.setCellValue("");
				     cell8.setCellStyle(textStyle);
				 }

				  Cell cell9 = row.createCell(3);

				 if (record1.getR12_carrying_amount() != null) {
				     cell9.setCellValue(record1.getR12_carrying_amount().doubleValue());
				     cell9.setCellStyle(numberStyle);
				 } else {
				     cell9.setCellValue("");
				     cell9.setCellStyle(textStyle);
				 }

				  Cell cell10 = row.createCell(5);

				 if (record1.getR12_no_of_accts() != null) {
				     cell10.setCellValue(record1.getR12_no_of_accts().doubleValue());
				     cell10.setCellStyle(numberStyle);
				 } else {
				     cell10.setCellValue("");
				     cell10.setCellStyle(textStyle);
				 }


				 row = sheet.getRow(10) != null ? sheet.getRow(10) : sheet.createRow(10);
				  Cell cell11 = row.createCell(2);

				 if (record1.getR13_collateral_amount() != null) {
				     cell11.setCellValue(record1.getR13_collateral_amount().doubleValue());
				     cell11.setCellStyle(numberStyle);
				 } else {
				     cell11.setCellValue("");
				     cell11.setCellStyle(textStyle);
				 }

				  Cell cell12 = row.createCell(3);

				 if (record1.getR13_carrying_amount() != null) {
				     cell12.setCellValue(record1.getR13_carrying_amount().doubleValue());
				     cell12.setCellStyle(numberStyle);
				 } else {
				     cell12.setCellValue("");
				     cell12.setCellStyle(textStyle);
				 }

				  Cell cell13 = row.createCell(5);

				 if (record1.getR13_no_of_accts() != null) {
				     cell13.setCellValue(record1.getR13_no_of_accts().doubleValue());
				     cell13.setCellStyle(numberStyle);
				 } else {
				     cell13.setCellValue("");
				     cell13.setCellStyle(textStyle);
				 }


				 row = sheet.getRow(11) != null ? sheet.getRow(11) : sheet.createRow(11);
				  Cell cell14 = row.createCell(2);

				 if (record1.getR14_collateral_amount() != null) {
				     cell14.setCellValue(record1.getR14_collateral_amount().doubleValue());
				     cell14.setCellStyle(numberStyle);
				 } else {
				     cell14.setCellValue("");
				     cell14.setCellStyle(textStyle);
				 }

				  Cell cell15 = row.createCell(3);

				 if (record1.getR14_carrying_amount() != null) {
				     cell15.setCellValue(record1.getR14_carrying_amount().doubleValue());
				     cell15.setCellStyle(numberStyle);
				 } else {
				     cell15.setCellValue("");
				     cell15.setCellStyle(textStyle);
				 }

				  Cell cell16 = row.createCell(5);

				 if (record1.getR14_no_of_accts() != null) {
				     cell16.setCellValue(record1.getR14_no_of_accts().doubleValue());
				     cell16.setCellStyle(numberStyle);
				 } else {
				     cell16.setCellValue("");
				     cell16.setCellStyle(textStyle);
				 }


				 row = sheet.getRow(12) != null ? sheet.getRow(12) : sheet.createRow(12);
				  Cell cell17 = row.createCell(2);

				 if (record1.getR15_collateral_amount() != null) {
				     cell17.setCellValue(record1.getR15_collateral_amount().doubleValue());
				     cell17.setCellStyle(numberStyle);
				 } else {
				     cell17.setCellValue("");
				     cell17.setCellStyle(textStyle);
				 }

				  Cell cell18 = row.createCell(3);

				 if (record1.getR15_carrying_amount() != null) {
				     cell18.setCellValue(record1.getR15_carrying_amount().doubleValue());
				     cell18.setCellStyle(numberStyle);
				 } else {
				     cell18.setCellValue("");
				     cell18.setCellStyle(textStyle);
				 }

				  Cell cell19 = row.createCell(5);

				 if (record1.getR15_no_of_accts() != null) {
				     cell19.setCellValue(record1.getR15_no_of_accts().doubleValue());
				     cell19.setCellStyle(numberStyle);
				 } else {
				     cell19.setCellValue("");
				     cell19.setCellStyle(textStyle);
				 }


				 row = sheet.getRow(13) != null ? sheet.getRow(13) : sheet.createRow(13);
				  Cell cell20 = row.createCell(2);

				 if (record1.getR16_collateral_amount() != null) {
				     cell20.setCellValue(record1.getR16_collateral_amount().doubleValue());
				     cell20.setCellStyle(numberStyle);
				 } else {
				     cell20.setCellValue("");
				     cell20.setCellStyle(textStyle);
				 }

				  Cell cell21 = row.createCell(3);

				 if (record1.getR16_carrying_amount() != null) {
				     cell21.setCellValue(record1.getR16_carrying_amount().doubleValue());
				     cell21.setCellStyle(numberStyle);
				 } else {
				     cell21.setCellValue("");
				     cell21.setCellStyle(textStyle);
				 }

				  Cell cell22 = row.createCell(5);

				 if (record1.getR16_no_of_accts() != null) {
				     cell22.setCellValue(record1.getR16_no_of_accts().doubleValue());
				     cell22.setCellStyle(numberStyle);
				 } else {
				     cell22.setCellValue("");
				     cell22.setCellStyle(textStyle);
				 }


				 row = sheet.getRow(14) != null ? sheet.getRow(14) : sheet.createRow(14);
				  Cell cell23 = row.createCell(2);

				 if (record1.getR17_collateral_amount() != null) {
				     cell23.setCellValue(record1.getR17_collateral_amount().doubleValue());
				     cell23.setCellStyle(numberStyle);
				 } else {
				     cell23.setCellValue("");
				     cell23.setCellStyle(textStyle);
				 }

				  Cell cell24 = row.createCell(3);

				 if (record1.getR17_carrying_amount() != null) {
				     cell24.setCellValue(record1.getR17_carrying_amount().doubleValue());
				     cell24.setCellStyle(numberStyle);
				 } else {
				     cell24.setCellValue("");
				     cell24.setCellStyle(textStyle);
				 }

				  Cell cell25 = row.createCell(5);

				 if (record1.getR17_no_of_accts() != null) {
				     cell25.setCellValue(record1.getR17_no_of_accts().doubleValue());
				     cell25.setCellStyle(numberStyle);
				 } else {
				     cell25.setCellValue("");
				     cell25.setCellStyle(textStyle);
				 }


				 row = sheet.getRow(15) != null ? sheet.getRow(15) : sheet.createRow(15);
				  Cell cell26 = row.createCell(2);

				 if (record1.getR18_collateral_amount() != null) {
				     cell26.setCellValue(record1.getR18_collateral_amount().doubleValue());
				     cell26.setCellStyle(numberStyle);
				 } else {
				     cell26.setCellValue("");
				     cell26.setCellStyle(textStyle);
				 }

				  Cell cell27 = row.createCell(3);

				 if (record1.getR18_carrying_amount() != null) {
				     cell27.setCellValue(record1.getR18_carrying_amount().doubleValue());
				     cell27.setCellStyle(numberStyle);
				 } else {
				     cell27.setCellValue("");
				     cell27.setCellStyle(textStyle);
				 }

				  Cell cell28 = row.createCell(5);

				 if (record1.getR18_no_of_accts() != null) {
				     cell28.setCellValue(record1.getR18_no_of_accts().doubleValue());
				     cell28.setCellStyle(numberStyle);
				 } else {
				     cell28.setCellValue("");
				     cell28.setCellStyle(textStyle);
				 }


//				 row = sheet.getRow(16) != null ? sheet.getRow(16) : sheet.createRow(16);
//				  Cell cell29 = row.createCell(2);
//
//				 if (record1.getR19_collateral_amount() != null) {
//				     cell29.setCellValue(record1.getR19_collateral_amount().doubleValue());
//				     cell29.setCellStyle(numberStyle);
//				 } else {
//				     cell29.setCellValue("");
//				     cell29.setCellStyle(textStyle);
//				 }
//
//				  Cell cell30 = row.createCell(3);
//
//				 if (record1.getR19_carrying_amount() != null) {
//				     cell30.setCellValue(record1.getR19_carrying_amount().doubleValue());
//				     cell30.setCellStyle(numberStyle);
//				 } else {
//				     cell30.setCellValue("");
//				     cell30.setCellStyle(textStyle);
//				 }
//
//				  Cell cell31 = row.createCell(4);
//
//				 if (record1.getR19_no_of_accts() != null) {
//				     cell31.setCellValue(record1.getR19_no_of_accts().doubleValue());
//				     cell31.setCellStyle(numberStyle);
//				 } else {
//				     cell31.setCellValue("");
//				     cell31.setCellStyle(textStyle);
//				 }
//
//
//				 row = sheet.getRow(17) != null ? sheet.getRow(17) : sheet.createRow(17);
//				  Cell cell32 = row.createCell(2);
//
//				 if (record1.getR20_collateral_amount() != null) {
//				     cell32.setCellValue(record1.getR20_collateral_amount().doubleValue());
//				     cell32.setCellStyle(numberStyle);
//				 } else {
//				     cell32.setCellValue("");
//				     cell32.setCellStyle(textStyle);
//				 }
//
//				  Cell cell33 = row.createCell(3);
//
//				 if (record1.getR20_carrying_amount() != null) {
//				     cell33.setCellValue(record1.getR20_carrying_amount().doubleValue());
//				     cell33.setCellStyle(numberStyle);
//				 } else {
//				     cell33.setCellValue("");
//				     cell33.setCellStyle(textStyle);
//				 }
//
//				  Cell cell34 = row.createCell(4);
//
//				 if (record1.getR20_no_of_accts() != null) {
//				     cell34.setCellValue(record1.getR20_no_of_accts().doubleValue());
//				     cell34.setCellStyle(numberStyle);
//				 } else {
//				     cell34.setCellValue("");
//				     cell34.setCellStyle(textStyle);
//				 }
//
//
//				 row = sheet.getRow(18) != null ? sheet.getRow(18) : sheet.createRow(18);
//				  Cell cell35 = row.createCell(2);
//
//				 if (record1.getR21_collateral_amount() != null) {
//				     cell35.setCellValue(record1.getR21_collateral_amount().doubleValue());
//				     cell35.setCellStyle(numberStyle);
//				 } else {
//				     cell35.setCellValue("");
//				     cell35.setCellStyle(textStyle);
//				 }
//
//				  Cell cell36 = row.createCell(3);
//
//				 if (record1.getR21_carrying_amount() != null) {
//				     cell36.setCellValue(record1.getR21_carrying_amount().doubleValue());
//				     cell36.setCellStyle(numberStyle);
//				 } else {
//				     cell36.setCellValue("");
//				     cell36.setCellStyle(textStyle);
//				 }
//
//				  Cell cell37 = row.createCell(4);
//
//				 if (record1.getR21_no_of_accts() != null) {
//				     cell37.setCellValue(record1.getR21_no_of_accts().doubleValue());
//				     cell37.setCellStyle(numberStyle);
//				 } else {
//				     cell37.setCellValue("");
//				     cell37.setCellStyle(textStyle);
//				 }
//
//
//				 row = sheet.getRow(19) != null ? sheet.getRow(19) : sheet.createRow(19);
//				  Cell cell38 = row.createCell(2);
//
//				 if (record1.getR22_collateral_amount() != null) {
//				     cell38.setCellValue(record1.getR22_collateral_amount().doubleValue());
//				     cell38.setCellStyle(numberStyle);
//				 } else {
//				     cell38.setCellValue("");
//				     cell38.setCellStyle(textStyle);
//				 }
//
//				  Cell cell39 = row.createCell(3);
//
//				 if (record1.getR22_carrying_amount() != null) {
//				     cell39.setCellValue(record1.getR22_carrying_amount().doubleValue());
//				     cell39.setCellStyle(numberStyle);
//				 } else {
//				     cell39.setCellValue("");
//				     cell39.setCellStyle(textStyle);
//				 }
//
//				  Cell cell40 = row.createCell(4);
//
//				 if (record1.getR22_no_of_accts() != null) {
//				     cell40.setCellValue(record1.getR22_no_of_accts().doubleValue());
//				     cell40.setCellStyle(numberStyle);
//				 } else {
//				     cell40.setCellValue("");
//				     cell40.setCellStyle(textStyle);
//				 }
//
//
//				 row = sheet.getRow(20) != null ? sheet.getRow(20) : sheet.createRow(20);
//				  Cell cell41 = row.createCell(2);
//
//				 if (record1.getR23_collateral_amount() != null) {
//				     cell41.setCellValue(record1.getR23_collateral_amount().doubleValue());
//				     cell41.setCellStyle(numberStyle);
//				 } else {
//				     cell41.setCellValue("");
//				     cell41.setCellStyle(textStyle);
//				 }
//
//				  Cell cell42 = row.createCell(3);
//
//				 if (record1.getR23_carrying_amount() != null) {
//				     cell42.setCellValue(record1.getR23_carrying_amount().doubleValue());
//				     cell42.setCellStyle(numberStyle);
//				 } else {
//				     cell42.setCellValue("");
//				     cell42.setCellStyle(textStyle);
//				 }
//
//				  Cell cell43 = row.createCell(4);
//
//				 if (record1.getR23_no_of_accts() != null) {
//				     cell43.setCellValue(record1.getR23_no_of_accts().doubleValue());
//				     cell43.setCellStyle(numberStyle);
//				 } else {
//				     cell43.setCellValue("");
//				     cell43.setCellStyle(textStyle);
//				 }
//
//
//				 row = sheet.getRow(21) != null ? sheet.getRow(21) : sheet.createRow(21);
//				  Cell cell44 = row.createCell(2);
//
//				 if (record1.getR24_collateral_amount() != null) {
//				     cell44.setCellValue(record1.getR24_collateral_amount().doubleValue());
//				     cell44.setCellStyle(numberStyle);
//				 } else {
//				     cell44.setCellValue("");
//				     cell44.setCellStyle(textStyle);
//				 }
//
//				  Cell cell45 = row.createCell(3);
//
//				 if (record1.getR24_carrying_amount() != null) {
//				     cell45.setCellValue(record1.getR24_carrying_amount().doubleValue());
//				     cell45.setCellStyle(numberStyle);
//				 } else {
//				     cell45.setCellValue("");
//				     cell45.setCellStyle(textStyle);
//				 }
//
//				  Cell cell46 = row.createCell(4);
//
//				 if (record1.getR24_no_of_accts() != null) {
//				     cell46.setCellValue(record1.getR24_no_of_accts().doubleValue());
//				     cell46.setCellStyle(numberStyle);
//				 } else {
//				     cell46.setCellValue("");
//				     cell46.setCellStyle(textStyle);
//				 }
//
//
//				 row = sheet.getRow(22) != null ? sheet.getRow(22) : sheet.createRow(22);
//				  Cell cell47 = row.createCell(2);
//
//				 if (record1.getR25_collateral_amount() != null) {
//				     cell47.setCellValue(record1.getR25_collateral_amount().doubleValue());
//				     cell47.setCellStyle(numberStyle);
//				 } else {
//				     cell47.setCellValue("");
//				     cell47.setCellStyle(textStyle);
//				 }
//
//				  Cell cell48 = row.createCell(3);
//
//				 if (record1.getR25_carrying_amount() != null) {
//				     cell48.setCellValue(record1.getR25_carrying_amount().doubleValue());
//				     cell48.setCellStyle(numberStyle);
//				 } else {
//				     cell48.setCellValue("");
//				     cell48.setCellStyle(textStyle);
//				 }
//
//				  Cell cell49 = row.createCell(4);
//
//				 if (record1.getR25_no_of_accts() != null) {
//				     cell49.setCellValue(record1.getR25_no_of_accts().doubleValue());
//				     cell49.setCellStyle(numberStyle);
//				 } else {
//				     cell49.setCellValue("");
//				     cell49.setCellStyle(textStyle);
//				 }
//
//
//				 row = sheet.getRow(23) != null ? sheet.getRow(23) : sheet.createRow(23);
//				  Cell cell50 = row.createCell(2);
//
//				 if (record1.getR28_collateral_amount() != null) {
//				     cell50.setCellValue(record1.getR28_collateral_amount().doubleValue());
//				     cell50.setCellStyle(numberStyle);
//				 } else {
//				     cell50.setCellValue("");
//				     cell50.setCellStyle(textStyle);
//				 }
//
//				  Cell cell51 = row.createCell(3);
//
//				 if (record1.getR28_carrying_amount() != null) {
//				     cell51.setCellValue(record1.getR28_carrying_amount().doubleValue());
//				     cell51.setCellStyle(numberStyle);
//				 } else {
//				     cell51.setCellValue("");
//				     cell51.setCellStyle(textStyle);
//				 }
//
//				  Cell cell52 = row.createCell(4);
//
//				 if (record1.getR28_no_of_accts() != null) {
//				     cell52.setCellValue(record1.getR28_no_of_accts().doubleValue());
//				     cell52.setCellStyle(numberStyle);
//				 } else {
//				     cell52.setCellValue("");
//				     cell52.setCellStyle(textStyle);
//				 }
//
//
//				 row = sheet.getRow(24) != null ? sheet.getRow(24) : sheet.createRow(24);
//				  Cell cell53 = row.createCell(2);
//
//				 if (record1.getR29_collateral_amount() != null) {
//				     cell53.setCellValue(record1.getR29_collateral_amount().doubleValue());
//				     cell53.setCellStyle(numberStyle);
//				 } else {
//				     cell53.setCellValue("");
//				     cell53.setCellStyle(textStyle);
//				 }
//
//				  Cell cell54 = row.createCell(3);
//
//				 if (record1.getR29_carrying_amount() != null) {
//				     cell54.setCellValue(record1.getR29_carrying_amount().doubleValue());
//				     cell54.setCellStyle(numberStyle);
//				 } else {
//				     cell54.setCellValue("");
//				     cell54.setCellStyle(textStyle);
//				 }
//
//				  Cell cell55 = row.createCell(4);
//
//				 if (record1.getR29_no_of_accts() != null) {
//				     cell55.setCellValue(record1.getR29_no_of_accts().doubleValue());
//				     cell55.setCellStyle(numberStyle);
//				 } else {
//				     cell55.setCellValue("");
//				     cell55.setCellStyle(textStyle);
//				 }
//
//
//				 row = sheet.getRow(25) != null ? sheet.getRow(25) : sheet.createRow(25);
//				  Cell cell56 = row.createCell(2);
//
//				 if (record1.getR30_collateral_amount() != null) {
//				     cell56.setCellValue(record1.getR30_collateral_amount().doubleValue());
//				     cell56.setCellStyle(numberStyle);
//				 } else {
//				     cell56.setCellValue("");
//				     cell56.setCellStyle(textStyle);
//				 }
//
//				  Cell cell57 = row.createCell(3);
//
//				 if (record1.getR30_carrying_amount() != null) {
//				     cell57.setCellValue(record1.getR30_carrying_amount().doubleValue());
//				     cell57.setCellStyle(numberStyle);
//				 } else {
//				     cell57.setCellValue("");
//				     cell57.setCellStyle(textStyle);
//				 }
//
//				  Cell cell58 = row.createCell(4);
//
//				 if (record1.getR30_no_of_accts() != null) {
//				     cell58.setCellValue(record1.getR30_no_of_accts().doubleValue());
//				     cell58.setCellStyle(numberStyle);
//				 } else {
//				     cell58.setCellValue("");
//				     cell58.setCellStyle(textStyle);
//				 }
//
//				 row = sheet.getRow(27) != null ? sheet.getRow(27) : sheet.createRow(27);
//				 Cell cell59 = row.createCell(2);
//
//				if (record1.getR32_collateral_amount() != null) {
//				    cell59.setCellValue(record1.getR32_collateral_amount().doubleValue());
//				    cell59.setCellStyle(numberStyle);
//				} else {
//				    cell59.setCellValue("");
//				    cell59.setCellStyle(textStyle);
//				}
//
//				 Cell cell60 = row.createCell(3);
//
//				if (record1.getR32_carrying_amount() != null) {
//				    cell60.setCellValue(record1.getR32_carrying_amount().doubleValue());
//				    cell60.setCellStyle(numberStyle);
//				} else {
//				    cell60.setCellValue("");
//				    cell60.setCellStyle(textStyle);
//				}
//
//				 Cell cell61 = row.createCell(4);
//
//				if (record1.getR32_no_of_accts() != null) {
//				    cell61.setCellValue(record1.getR32_no_of_accts().doubleValue());
//				    cell61.setCellStyle(numberStyle);
//				} else {
//				    cell61.setCellValue("");
//				    cell61.setCellStyle(textStyle);
//				}
//
//
//				 row = sheet.getRow(28) != null ? sheet.getRow(28) : sheet.createRow(28);
//				 Cell cell62 = row.createCell(2);
//
//				if (record1.getR33_collateral_amount() != null) {
//				    cell62.setCellValue(record1.getR33_collateral_amount().doubleValue());
//				    cell62.setCellStyle(numberStyle);
//				} else {
//				    cell62.setCellValue("");
//				    cell62.setCellStyle(textStyle);
//				}
//
//				 Cell cell63 = row.createCell(3);
//
//				if (record1.getR33_carrying_amount() != null) {
//				    cell63.setCellValue(record1.getR33_carrying_amount().doubleValue());
//				    cell63.setCellStyle(numberStyle);
//				} else {
//				    cell63.setCellValue("");
//				    cell63.setCellStyle(textStyle);
//				}
//
//				 Cell cell64 = row.createCell(4);
//
//				if (record1.getR33_no_of_accts() != null) {
//				    cell64.setCellValue(record1.getR33_no_of_accts().doubleValue());
//				    cell64.setCellStyle(numberStyle);
//				} else {
//				    cell64.setCellValue("");
//				    cell64.setCellStyle(textStyle);
//				}
//
//
//				 row = sheet.getRow(29) != null ? sheet.getRow(29) : sheet.createRow(29);
//				 Cell cell65 = row.createCell(2);
//
//				if (record1.getR31_collateral_amount() != null) {
//				    cell65.setCellValue(record1.getR31_collateral_amount().doubleValue());
//				    cell65.setCellStyle(numberStyle);
//				} else {
//				    cell65.setCellValue("");
//				    cell65.setCellStyle(textStyle);
//				}
//
//				 Cell cell66 = row.createCell(3);
//
//				if (record1.getR31_carrying_amount() != null) {
//				    cell66.setCellValue(record1.getR31_carrying_amount().doubleValue());
//				    cell66.setCellStyle(numberStyle);
//				} else {
//				    cell66.setCellValue("");
//				    cell66.setCellStyle(textStyle);
//				}
//
//				 Cell cell67 = row.createCell(4);
//
//				if (record1.getR31_no_of_accts() != null) {
//				    cell67.setCellValue(record1.getR31_no_of_accts().doubleValue());
//				    cell67.setCellStyle(numberStyle);
//				} else {
//				    cell67.setCellValue("");
//				    cell67.setCellStyle(textStyle);
//				}
//
//
//				 row = sheet.getRow(30) != null ? sheet.getRow(30) : sheet.createRow(30);
//				 Cell cell68 = row.createCell(2);
//
//				if (record1.getR34_collateral_amount() != null) {
//				    cell68.setCellValue(record1.getR34_collateral_amount().doubleValue());
//				    cell68.setCellStyle(numberStyle);
//				} else {
//				    cell68.setCellValue("");
//				    cell68.setCellStyle(textStyle);
//				}
//
//				 Cell cell69 = row.createCell(3);
//
//				if (record1.getR34_carrying_amount() != null) {
//				    cell69.setCellValue(record1.getR34_carrying_amount().doubleValue());
//				    cell69.setCellStyle(numberStyle);
//				} else {
//				    cell69.setCellValue("");
//				    cell69.setCellStyle(textStyle);
//				}
//
//				 Cell cell70 = row.createCell(4);
//
//				if (record1.getR34_no_of_accts() != null) {
//				    cell70.setCellValue(record1.getR34_no_of_accts().doubleValue());
//				    cell70.setCellStyle(numberStyle);
//				} else {
//				    cell70.setCellValue("");
//				    cell70.setCellStyle(textStyle);
//				}
//
//
//				 row = sheet.getRow(31) != null ? sheet.getRow(31) : sheet.createRow(31);
//				 Cell cell71 = row.createCell(2);
//
//				if (record1.getR36_collateral_amount() != null) {
//				    cell71.setCellValue(record1.getR36_collateral_amount().doubleValue());
//				    cell71.setCellStyle(numberStyle);
//				} else {
//				    cell71.setCellValue("");
//				    cell71.setCellStyle(textStyle);
//				}
//
//				 Cell cell72 = row.createCell(3);
//
//				if (record1.getR36_carrying_amount() != null) {
//				    cell72.setCellValue(record1.getR36_carrying_amount().doubleValue());
//				    cell72.setCellStyle(numberStyle);
//				} else {
//				    cell72.setCellValue("");
//				    cell72.setCellStyle(textStyle);
//				}
//
//				 Cell cell73 = row.createCell(4);
//
//				if (record1.getR36_no_of_accts() != null) {
//				    cell73.setCellValue(record1.getR36_no_of_accts().doubleValue());
//				    cell73.setCellStyle(numberStyle);
//				} else {
//				    cell73.setCellValue("");
//				    cell73.setCellStyle(textStyle);
//				}
//
//
//				 row = sheet.getRow(32) != null ? sheet.getRow(32) : sheet.createRow(32);
//				 Cell cell74 = row.createCell(2);
//
//				if (record1.getR35_collateral_amount() != null) {
//				    cell74.setCellValue(record1.getR35_collateral_amount().doubleValue());
//				    cell74.setCellStyle(numberStyle);
//				} else {
//				    cell74.setCellValue("");
//				    cell74.setCellStyle(textStyle);
//				}
//
//				 Cell cell75 = row.createCell(3);
//
//				if (record1.getR35_carrying_amount() != null) {
//				    cell75.setCellValue(record1.getR35_carrying_amount().doubleValue());
//				    cell75.setCellStyle(numberStyle);
//				} else {
//				    cell75.setCellValue("");
//				    cell75.setCellStyle(textStyle);
//				}
//
//				 Cell cell76 = row.createCell(4);
//
//				if (record1.getR35_no_of_accts() != null) {
//				    cell76.setCellValue(record1.getR35_no_of_accts().doubleValue());
//				    cell76.setCellStyle(numberStyle);
//				} else {
//				    cell76.setCellValue("");
//				    cell76.setCellStyle(textStyle);
//				}
//
//
//				 row = sheet.getRow(33) != null ? sheet.getRow(33) : sheet.createRow(33);
//				 Cell cell77 = row.createCell(2);
//
//				if (record1.getR37_collateral_amount() != null) {
//				    cell77.setCellValue(record1.getR37_collateral_amount().doubleValue());
//				    cell77.setCellStyle(numberStyle);
//				} else {
//				    cell77.setCellValue("");
//				    cell77.setCellStyle(textStyle);
//				}
//
//				 Cell cell78 = row.createCell(3);
//
//				if (record1.getR37_carrying_amount() != null) {
//				    cell78.setCellValue(record1.getR37_carrying_amount().doubleValue());
//				    cell78.setCellStyle(numberStyle);
//				} else {
//				    cell78.setCellValue("");
//				    cell78.setCellStyle(textStyle);
//				}
//
//				 Cell cell79 = row.createCell(4);
//
//				if (record1.getR37_no_of_accts() != null) {
//				    cell79.setCellValue(record1.getR37_no_of_accts().doubleValue());
//				    cell79.setCellStyle(numberStyle);
//				} else {
//				    cell79.setCellValue("");
//				    cell79.setCellStyle(textStyle);
//				}
//
//
//				 row = sheet.getRow(34) != null ? sheet.getRow(34) : sheet.createRow(34);
//				 Cell cell80 = row.createCell(2);
//
//				if (record1.getR38_collateral_amount() != null) {
//				    cell80.setCellValue(record1.getR38_collateral_amount().doubleValue());
//				    cell80.setCellStyle(numberStyle);
//				} else {
//				    cell80.setCellValue("");
//				    cell80.setCellStyle(textStyle);
//				}
//
//				 Cell cell81 = row.createCell(3);
//
//				if (record1.getR38_carrying_amount() != null) {
//				    cell81.setCellValue(record1.getR38_carrying_amount().doubleValue());
//				    cell81.setCellStyle(numberStyle);
//				} else {
//				    cell81.setCellValue("");
//				    cell81.setCellStyle(textStyle);
//				}
//
//				 Cell cell82 = row.createCell(4);
//
//				if (record1.getR38_no_of_accts() != null) {
//				    cell82.setCellValue(record1.getR38_no_of_accts().doubleValue());
//				    cell82.setCellStyle(numberStyle);
//				} else {
//				    cell82.setCellValue("");
//				    cell82.setCellStyle(textStyle);
//				}
//
//
//				 row = sheet.getRow(35) != null ? sheet.getRow(35) : sheet.createRow(35);
//				 Cell cell83 = row.createCell(2);
//
//				if (record1.getR39_collateral_amount() != null) {
//				    cell83.setCellValue(record1.getR39_collateral_amount().doubleValue());
//				    cell83.setCellStyle(numberStyle);
//				} else {
//				    cell83.setCellValue("");
//				    cell83.setCellStyle(textStyle);
//				}
//
//				 Cell cell84 = row.createCell(3);
//
//				if (record1.getR39_carrying_amount() != null) {
//				    cell84.setCellValue(record1.getR39_carrying_amount().doubleValue());
//				    cell84.setCellStyle(numberStyle);
//				} else {
//				    cell84.setCellValue("");
//				    cell84.setCellStyle(textStyle);
//				}
//
//				 Cell cell85 = row.createCell(4);
//
//				if (record1.getR39_no_of_accts() != null) {
//				    cell85.setCellValue(record1.getR39_no_of_accts().doubleValue());
//				    cell85.setCellStyle(numberStyle);
//				} else {
//				    cell85.setCellValue("");
//				    cell85.setCellStyle(textStyle);
//				}
//
//
//				 row = sheet.getRow(36) != null ? sheet.getRow(36) : sheet.createRow(36);
//				 Cell cell86 = row.createCell(2);
//
//				if (record1.getR40_collateral_amount() != null) {
//				    cell86.setCellValue(record1.getR40_collateral_amount().doubleValue());
//				    cell86.setCellStyle(numberStyle);
//				} else {
//				    cell86.setCellValue("");
//				    cell86.setCellStyle(textStyle);
//				}
//
//				 Cell cell87 = row.createCell(3);
//
//				if (record1.getR40_carrying_amount() != null) {
//				    cell87.setCellValue(record1.getR40_carrying_amount().doubleValue());
//				    cell87.setCellStyle(numberStyle);
//				} else {
//				    cell87.setCellValue("");
//				    cell87.setCellStyle(textStyle);
//				}
//
//				 Cell cell88 = row.createCell(4);
//
//				if (record1.getR40_no_of_accts() != null) {
//				    cell88.setCellValue(record1.getR40_no_of_accts().doubleValue());
//				    cell88.setCellStyle(numberStyle);
//				} else {
//				    cell88.setCellValue("");
//				    cell88.setCellStyle(textStyle);
//				}
//
//
//				 row = sheet.getRow(37) != null ? sheet.getRow(37) : sheet.createRow(37);
//				 Cell cell89 = row.createCell(2);
//
//				if (record1.getR41_collateral_amount() != null) {
//				    cell89.setCellValue(record1.getR41_collateral_amount().doubleValue());
//				    cell89.setCellStyle(numberStyle);
//				} else {
//				    cell89.setCellValue("");
//				    cell89.setCellStyle(textStyle);
//				}
//
//				 Cell cell90 = row.createCell(3);
//
//				if (record1.getR41_carrying_amount() != null) {
//				    cell90.setCellValue(record1.getR41_carrying_amount().doubleValue());
//				    cell90.setCellStyle(numberStyle);
//				} else {
//				    cell90.setCellValue("");
//				    cell90.setCellStyle(textStyle);
//				}
//
//				 Cell cell91 = row.createCell(4);
//
//				if (record1.getR41_no_of_accts() != null) {
//				    cell91.setCellValue(record1.getR41_no_of_accts().doubleValue());
//				    cell91.setCellStyle(numberStyle);
//				} else {
//				    cell91.setCellValue("");
//				    cell91.setCellStyle(textStyle);
//				}
//
//
//				 row = sheet.getRow(38) != null ? sheet.getRow(38) : sheet.createRow(38);
//				 Cell cell92 = row.createCell(2);
//
//				if (record1.getR42_collateral_amount() != null) {
//				    cell92.setCellValue(record1.getR42_collateral_amount().doubleValue());
//				    cell92.setCellStyle(numberStyle);
//				} else {
//				    cell92.setCellValue("");
//				    cell92.setCellStyle(textStyle);
//				}
//
//				 Cell cell93 = row.createCell(3);
//
//				if (record1.getR42_carrying_amount() != null) {
//				    cell93.setCellValue(record1.getR42_carrying_amount().doubleValue());
//				    cell93.setCellStyle(numberStyle);
//				} else {
//				    cell93.setCellValue("");
//				    cell93.setCellStyle(textStyle);
//				}
//
//				 Cell cell94 = row.createCell(4);
//
//				if (record1.getR42_no_of_accts() != null) {
//				    cell94.setCellValue(record1.getR42_no_of_accts().doubleValue());
//				    cell94.setCellStyle(numberStyle);
//				} else {
//				    cell94.setCellValue("");
//				    cell94.setCellStyle(textStyle);
//				}


				 row = sheet.getRow(39) != null ? sheet.getRow(39) : sheet.createRow(39);
				 Cell cell95 = row.createCell(2);

				if (record1.getR43_collateral_amount() != null) {
				    cell95.setCellValue(record1.getR43_collateral_amount().doubleValue());
				    cell95.setCellStyle(numberStyle);
				} else {
				    cell95.setCellValue("");
				    cell95.setCellStyle(textStyle);
				}

				 Cell cell96 = row.createCell(3);

				if (record1.getR43_carrying_amount() != null) {
				    cell96.setCellValue(record1.getR43_carrying_amount().doubleValue());
				    cell96.setCellStyle(numberStyle);
				} else {
				    cell96.setCellValue("");
				    cell96.setCellStyle(textStyle);
				}

				 Cell cell97 = row.createCell(5);

				if (record1.getR43_no_of_accts() != null) {
				    cell97.setCellValue(record1.getR43_no_of_accts().doubleValue());
				    cell97.setCellStyle(numberStyle);
				} else {
				    cell97.setCellValue("");
				    cell97.setCellStyle(textStyle);
				}


				 row = sheet.getRow(40) != null ? sheet.getRow(40) : sheet.createRow(40);
				 Cell cell98 = row.createCell(2);

				if (record1.getR44_collateral_amount() != null) {
				    cell98.setCellValue(record1.getR44_collateral_amount().doubleValue());
				    cell98.setCellStyle(numberStyle);
				} else {
				    cell98.setCellValue("");
				    cell98.setCellStyle(textStyle);
				}

				 Cell cell99 = row.createCell(3);

				if (record1.getR44_carrying_amount() != null) {
				    cell99.setCellValue(record1.getR44_carrying_amount().doubleValue());
				    cell99.setCellStyle(numberStyle);
				} else {
				    cell99.setCellValue("");
				    cell99.setCellStyle(textStyle);
				}

				 Cell cell100 = row.createCell(5);

				if (record1.getR44_no_of_accts() != null) {
				    cell100.setCellValue(record1.getR44_no_of_accts().doubleValue());
				    cell100.setCellStyle(numberStyle);
				} else {
				    cell100.setCellValue("");
				    cell100.setCellStyle(textStyle);
				}


				 row = sheet.getRow(41) != null ? sheet.getRow(41) : sheet.createRow(41);
				 Cell cell101 = row.createCell(2);

				if (record1.getR45_collateral_amount() != null) {
				    cell101.setCellValue(record1.getR45_collateral_amount().doubleValue());
				    cell101.setCellStyle(numberStyle);
				} else {
				    cell101.setCellValue("");
				    cell101.setCellStyle(textStyle);
				}

				 Cell cell102 = row.createCell(3);

				if (record1.getR45_carrying_amount() != null) {
				    cell102.setCellValue(record1.getR45_carrying_amount().doubleValue());
				    cell102.setCellStyle(numberStyle);
				} else {
				    cell102.setCellValue("");
				    cell102.setCellStyle(textStyle);
				}

				 Cell cell103 = row.createCell(5);

				if (record1.getR45_no_of_accts() != null) {
				    cell103.setCellValue(record1.getR45_no_of_accts().doubleValue());
				    cell103.setCellStyle(numberStyle);
				} else {
				    cell103.setCellValue("");
				    cell103.setCellStyle(textStyle);
				}

				 row = sheet.getRow(43) != null ? sheet.getRow(43) : sheet.createRow(43);
				 Cell cell104 = row.createCell(2);

				if (record1.getR47_collateral_amount() != null) {
				    cell104.setCellValue(record1.getR47_collateral_amount().doubleValue());
				    cell104.setCellStyle(numberStyle);
				} else {
				    cell104.setCellValue("");
				    cell104.setCellStyle(textStyle);
				}

				 Cell cell105 = row.createCell(3);

				if (record1.getR47_carrying_amount() != null) {
				    cell105.setCellValue(record1.getR47_carrying_amount().doubleValue());
				    cell105.setCellStyle(numberStyle);
				} else {
				    cell105.setCellValue("");
				    cell105.setCellStyle(textStyle);
				}

				 Cell cell106 = row.createCell(5);

				if (record1.getR47_no_of_accts() != null) {
				    cell106.setCellValue(record1.getR47_no_of_accts().doubleValue());
				    cell106.setCellStyle(numberStyle);
				} else {
				    cell106.setCellValue("");
				    cell106.setCellStyle(textStyle);
				}


				 row = sheet.getRow(44) != null ? sheet.getRow(44) : sheet.createRow(44);
				 Cell cell107 = row.createCell(2);

				if (record1.getR48_collateral_amount() != null) {
				    cell107.setCellValue(record1.getR48_collateral_amount().doubleValue());
				    cell107.setCellStyle(numberStyle);
				} else {
				    cell107.setCellValue("");
				    cell107.setCellStyle(textStyle);
				}

				 Cell cell108 = row.createCell(3);

				if (record1.getR48_carrying_amount() != null) {
				    cell108.setCellValue(record1.getR48_carrying_amount().doubleValue());
				    cell108.setCellStyle(numberStyle);
				} else {
				    cell108.setCellValue("");
				    cell108.setCellStyle(textStyle);
				}

				 Cell cell109 = row.createCell(5);

				if (record1.getR48_no_of_accts() != null) {
				    cell109.setCellValue(record1.getR48_no_of_accts().doubleValue());
				    cell109.setCellStyle(numberStyle);
				} else {
				    cell109.setCellValue("");
				    cell109.setCellStyle(textStyle);
				}


				 row = sheet.getRow(45) != null ? sheet.getRow(45) : sheet.createRow(45);
				 Cell cell110 = row.createCell(2);

				if (record1.getR49_collateral_amount() != null) {
				    cell110.setCellValue(record1.getR49_collateral_amount().doubleValue());
				    cell110.setCellStyle(numberStyle);
				} else {
				    cell110.setCellValue("");
				    cell110.setCellStyle(textStyle);
				}

				 Cell cell111 = row.createCell(3);

				if (record1.getR49_carrying_amount() != null) {
				    cell111.setCellValue(record1.getR49_carrying_amount().doubleValue());
				    cell111.setCellStyle(numberStyle);
				} else {
				    cell111.setCellValue("");
				    cell111.setCellStyle(textStyle);
				}

				 Cell cell112 = row.createCell(5);

				if (record1.getR49_no_of_accts() != null) {
				    cell112.setCellValue(record1.getR49_no_of_accts().doubleValue());
				    cell112.setCellStyle(numberStyle);
				} else {
				    cell112.setCellValue("");
				    cell112.setCellStyle(textStyle);
				}


				 row = sheet.getRow(46) != null ? sheet.getRow(46) : sheet.createRow(46);
				 Cell cell113 = row.createCell(2);

				if (record1.getR50_collateral_amount() != null) {
				    cell113.setCellValue(record1.getR50_collateral_amount().doubleValue());
				    cell113.setCellStyle(numberStyle);
				} else {
				    cell113.setCellValue("");
				    cell113.setCellStyle(textStyle);
				}

				 Cell cell114 = row.createCell(3);

				if (record1.getR50_carrying_amount() != null) {
				    cell114.setCellValue(record1.getR50_carrying_amount().doubleValue());
				    cell114.setCellStyle(numberStyle);
				} else {
				    cell114.setCellValue("");
				    cell114.setCellStyle(textStyle);
				}

				 Cell cell115 = row.createCell(5);

				if (record1.getR50_no_of_accts() != null) {
				    cell115.setCellValue(record1.getR50_no_of_accts().doubleValue());
				    cell115.setCellStyle(numberStyle);
				} else {
				    cell115.setCellValue("");
				    cell115.setCellStyle(textStyle);
				}


				 row = sheet.getRow(47) != null ? sheet.getRow(47) : sheet.createRow(47);
				 Cell cell116 = row.createCell(2);

				if (record1.getR52_collateral_amount() != null) {
				    cell116.setCellValue(record1.getR52_collateral_amount().doubleValue());
				    cell116.setCellStyle(numberStyle);
				} else {
				    cell116.setCellValue("");
				    cell116.setCellStyle(textStyle);
				}

				 Cell cell117 = row.createCell(3);

				if (record1.getR52_carrying_amount() != null) {
				    cell117.setCellValue(record1.getR52_carrying_amount().doubleValue());
				    cell117.setCellStyle(numberStyle);
				} else {
				    cell117.setCellValue("");
				    cell117.setCellStyle(textStyle);
				}

				 Cell cell118 = row.createCell(5);

				if (record1.getR52_no_of_accts() != null) {
				    cell118.setCellValue(record1.getR52_no_of_accts().doubleValue());
				    cell118.setCellStyle(numberStyle);
				} else {
				    cell118.setCellValue("");
				    cell118.setCellStyle(textStyle);
				}

				 row = sheet.getRow(49) != null ? sheet.getRow(49) : sheet.createRow(49);
				 Cell cell119 = row.createCell(2);

				if (record1.getR53_collateral_amount() != null) {
				    cell119.setCellValue(record1.getR53_collateral_amount().doubleValue());
				    cell119.setCellStyle(numberStyle);
				} else {
				    cell119.setCellValue("");
				    cell119.setCellStyle(textStyle);
				}

				 Cell cell120 = row.createCell(3);

				if (record1.getR53_carrying_amount() != null) {
				    cell120.setCellValue(record1.getR53_carrying_amount().doubleValue());
				    cell120.setCellStyle(numberStyle);
				} else {
				    cell120.setCellValue("");
				    cell120.setCellStyle(textStyle);
				}

				 Cell cell121 = row.createCell(5);

				if (record1.getR53_no_of_accts() != null) {
				    cell121.setCellValue(record1.getR53_no_of_accts().doubleValue());
				    cell121.setCellStyle(numberStyle);
				} else {
				    cell121.setCellValue("");
				    cell121.setCellStyle(textStyle);
				}


				 row = sheet.getRow(50) != null ? sheet.getRow(50) : sheet.createRow(50);
				 Cell cell122 = row.createCell(2);

				if (record1.getR54_collateral_amount() != null) {
				    cell122.setCellValue(record1.getR54_collateral_amount().doubleValue());
				    cell122.setCellStyle(numberStyle);
				} else {
				    cell122.setCellValue("");
				    cell122.setCellStyle(textStyle);
				}

				 Cell cell123 = row.createCell(3);

				if (record1.getR54_carrying_amount() != null) {
				    cell123.setCellValue(record1.getR54_carrying_amount().doubleValue());
				    cell123.setCellStyle(numberStyle);
				} else {
				    cell123.setCellValue("");
				    cell123.setCellStyle(textStyle);
				}

				 Cell cell124 = row.createCell(5);

				if (record1.getR54_no_of_accts() != null) {
				    cell124.setCellValue(record1.getR54_no_of_accts().doubleValue());
				    cell124.setCellStyle(numberStyle);
				} else {
				    cell124.setCellValue("");
				    cell124.setCellStyle(textStyle);
				}


				 row = sheet.getRow(51) != null ? sheet.getRow(51) : sheet.createRow(51);
				 Cell cell125 = row.createCell(2);

				if (record1.getR55_collateral_amount() != null) {
				    cell125.setCellValue(record1.getR55_collateral_amount().doubleValue());
				    cell125.setCellStyle(numberStyle);
				} else {
				    cell125.setCellValue("");
				    cell125.setCellStyle(textStyle);
				}

				 Cell cell126 = row.createCell(3);

				if (record1.getR55_carrying_amount() != null) {
				    cell126.setCellValue(record1.getR55_carrying_amount().doubleValue());
				    cell126.setCellStyle(numberStyle);
				} else {
				    cell126.setCellValue("");
				    cell126.setCellStyle(textStyle);
				}

				 Cell cell127 = row.createCell(5);

				if (record1.getR55_no_of_accts() != null) {
				    cell127.setCellValue(record1.getR55_no_of_accts().doubleValue());
				    cell127.setCellStyle(numberStyle);
				} else {
				    cell127.setCellValue("");
				    cell127.setCellStyle(textStyle);
				}


				 row = sheet.getRow(52) != null ? sheet.getRow(52) : sheet.createRow(52);
				 Cell cell128 = row.createCell(2);

				if (record1.getR56_collateral_amount() != null) {
				    cell128.setCellValue(record1.getR56_collateral_amount().doubleValue());
				    cell128.setCellStyle(numberStyle);
				} else {
				    cell128.setCellValue("");
				    cell128.setCellStyle(textStyle);
				}

				 Cell cell129 = row.createCell(3);

				if (record1.getR56_carrying_amount() != null) {
				    cell129.setCellValue(record1.getR56_carrying_amount().doubleValue());
				    cell129.setCellStyle(numberStyle);
				} else {
				    cell129.setCellValue("");
				    cell129.setCellStyle(textStyle);
				}

				 Cell cell130 = row.createCell(5);

				if (record1.getR56_no_of_accts() != null) {
				    cell130.setCellValue(record1.getR56_no_of_accts().doubleValue());
				    cell130.setCellStyle(numberStyle);
				} else {
				    cell130.setCellValue("");
				    cell130.setCellStyle(textStyle);
				}


				 row = sheet.getRow(53) != null ? sheet.getRow(53) : sheet.createRow(53);
				 Cell cell131 = row.createCell(2);

				if (record1.getR58_collateral_amount() != null) {
				    cell131.setCellValue(record1.getR58_collateral_amount().doubleValue());
				    cell131.setCellStyle(numberStyle);
				} else {
				    cell131.setCellValue("");
				    cell131.setCellStyle(textStyle);
				}

				 Cell cell132 = row.createCell(3);

				if (record1.getR58_carrying_amount() != null) {
				    cell132.setCellValue(record1.getR58_carrying_amount().doubleValue());
				    cell132.setCellStyle(numberStyle);
				} else {
				    cell132.setCellValue("");
				    cell132.setCellStyle(textStyle);
				}

				 Cell cell133 = row.createCell(5);

				if (record1.getR58_no_of_accts() != null) {
				    cell133.setCellValue(record1.getR58_no_of_accts().doubleValue());
				    cell133.setCellStyle(numberStyle);
				} else {
				    cell133.setCellValue("");
				    cell133.setCellStyle(textStyle);
				}


				 row = sheet.getRow(54) != null ? sheet.getRow(54) : sheet.createRow(54);
				 Cell cell134 = row.createCell(2);

				if (record1.getR59_collateral_amount() != null) {
				    cell134.setCellValue(record1.getR59_collateral_amount().doubleValue());
				    cell134.setCellStyle(numberStyle);
				} else {
				    cell134.setCellValue("");
				    cell134.setCellStyle(textStyle);
				}

				 Cell cell135 = row.createCell(3);

				if (record1.getR59_carrying_amount() != null) {
				    cell135.setCellValue(record1.getR59_carrying_amount().doubleValue());
				    cell135.setCellStyle(numberStyle);
				} else {
				    cell135.setCellValue("");
				    cell135.setCellStyle(textStyle);
				}

				 Cell cell136 = row.createCell(5);

				if (record1.getR59_no_of_accts() != null) {
				    cell136.setCellValue(record1.getR59_no_of_accts().doubleValue());
				    cell136.setCellStyle(numberStyle);
				} else {
				    cell136.setCellValue("");
				    cell136.setCellStyle(textStyle);
				}


				 row = sheet.getRow(55) != null ? sheet.getRow(55) : sheet.createRow(55);
				 Cell cell137 = row.createCell(2);

				if (record1.getR60_collateral_amount() != null) {
				    cell137.setCellValue(record1.getR60_collateral_amount().doubleValue());
				    cell137.setCellStyle(numberStyle);
				} else {
				    cell137.setCellValue("");
				    cell137.setCellStyle(textStyle);
				}

				 Cell cell138 = row.createCell(3);

				if (record1.getR60_carrying_amount() != null) {
				    cell138.setCellValue(record1.getR60_carrying_amount().doubleValue());
				    cell138.setCellStyle(numberStyle);
				} else {
				    cell138.setCellValue("");
				    cell138.setCellStyle(textStyle);
				}

				 Cell cell139 = row.createCell(5);

				if (record1.getR60_no_of_accts() != null) {
				    cell139.setCellValue(record1.getR60_no_of_accts().doubleValue());
				    cell139.setCellStyle(numberStyle);
				} else {
				    cell139.setCellValue("");
				    cell139.setCellStyle(textStyle);
				}


				 row = sheet.getRow(56) != null ? sheet.getRow(56) : sheet.createRow(56);
				 Cell cell140 = row.createCell(2);

				if (record1.getR61_collateral_amount() != null) {
				    cell140.setCellValue(record1.getR61_collateral_amount().doubleValue());
				    cell140.setCellStyle(numberStyle);
				} else {
				    cell140.setCellValue("");
				    cell140.setCellStyle(textStyle);
				}

				 Cell cell141 = row.createCell(3);

				if (record1.getR61_carrying_amount() != null) {
				    cell141.setCellValue(record1.getR61_carrying_amount().doubleValue());
				    cell141.setCellStyle(numberStyle);
				} else {
				    cell141.setCellValue("");
				    cell141.setCellStyle(textStyle);
				}

				 Cell cell142 = row.createCell(5);

				if (record1.getR61_no_of_accts() != null) {
				    cell142.setCellValue(record1.getR61_no_of_accts().doubleValue());
				    cell142.setCellStyle(numberStyle);
				} else {
				    cell142.setCellValue("");
				    cell142.setCellStyle(textStyle);
				}


				 row = sheet.getRow(57) != null ? sheet.getRow(57) : sheet.createRow(57);
				 Cell cell143 = row.createCell(2);

				if (record1.getR62_collateral_amount() != null) {
				    cell143.setCellValue(record1.getR62_collateral_amount().doubleValue());
				    cell143.setCellStyle(numberStyle);
				} else {
				    cell143.setCellValue("");
				    cell143.setCellStyle(textStyle);
				}

				 Cell cell144 = row.createCell(3);

				if (record1.getR62_carrying_amount() != null) {
				    cell144.setCellValue(record1.getR62_carrying_amount().doubleValue());
				    cell144.setCellStyle(numberStyle);
				} else {
				    cell144.setCellValue("");
				    cell144.setCellStyle(textStyle);
				}

				 Cell cell145 = row.createCell(5);

				if (record1.getR62_no_of_accts() != null) {
				    cell145.setCellValue(record1.getR62_no_of_accts().doubleValue());
				    cell145.setCellStyle(numberStyle);
				} else {
				    cell145.setCellValue("");
				    cell145.setCellStyle(textStyle);
				}


				 row = sheet.getRow(58) != null ? sheet.getRow(58) : sheet.createRow(58);
				 Cell cell146 = row.createCell(2);

				if (record1.getR63_collateral_amount() != null) {
				    cell146.setCellValue(record1.getR63_collateral_amount().doubleValue());
				    cell146.setCellStyle(numberStyle);
				} else {
				    cell146.setCellValue("");
				    cell146.setCellStyle(textStyle);
				}

				 Cell cell147 = row.createCell(3);

				if (record1.getR63_carrying_amount() != null) {
				    cell147.setCellValue(record1.getR63_carrying_amount().doubleValue());
				    cell147.setCellStyle(numberStyle);
				} else {
				    cell147.setCellValue("");
				    cell147.setCellStyle(textStyle);
				}

				 Cell cell148 = row.createCell(5);

				if (record1.getR63_no_of_accts() != null) {
				    cell148.setCellValue(record1.getR63_no_of_accts().doubleValue());
				    cell148.setCellStyle(numberStyle);
				} else {
				    cell148.setCellValue("");
				    cell148.setCellStyle(textStyle);
				}






				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();

				// Write the final workbook content to the in-memory stream.
				workbook.write(out);

				logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

				return out.toByteArray();
				
			}
			int startRow = 10;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					Q_RLFA1_Archival_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row11
					row = sheet.getRow(10);

					// Column 2 -B

					Cell cellB = row.createCell(1);
					if (record.getR11_collateral_amount() != null) {
						cellB.setCellValue(record.getR11_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					Cell cellC = row.createCell(2);
					if (record.getR11_carrying_amount() != null) {
						cellC.setCellValue(record.getR11_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}
					
					// Column 4-D
					Cell cellD = row.createCell(3);
					if (record.getR11_no_of_accts() != null) {
						cellD.setCellValue(record.getR11_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}
					
					// row12
					row = sheet.getRow(11);

					// Column 2 -B

					 cellB = row.createCell(1);
					if (record.getR12_collateral_amount() != null) {
						cellB.setCellValue(record.getR12_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					 cellC = row.createCell(2);
					if (record.getR12_carrying_amount() != null) {
						cellC.setCellValue(record.getR12_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}
					
					// Column 4-D
				 cellD = row.createCell(3);
					if (record.getR12_no_of_accts() != null) {
						cellD.setCellValue(record.getR12_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}
					
					
					// row13
					row = sheet.getRow(12);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR13_collateral_amount() != null) {
					    cellB.setCellValue(record.getR13_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR13_carrying_amount() != null) {
					    cellC.setCellValue(record.getR13_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR13_no_of_accts() != null) {
					    cellD.setCellValue(record.getR13_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row15
					row = sheet.getRow(14);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR15_collateral_amount() != null) {
					    cellB.setCellValue(record.getR15_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR15_carrying_amount() != null) {
					    cellC.setCellValue(record.getR15_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR15_no_of_accts() != null) {
					    cellD.setCellValue(record.getR15_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}


					// row16
					row = sheet.getRow(15);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR16_collateral_amount() != null) {
					    cellB.setCellValue(record.getR16_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR16_carrying_amount() != null) {
					    cellC.setCellValue(record.getR16_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR16_no_of_accts() != null) {
					    cellD.setCellValue(record.getR16_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR17_collateral_amount() != null) {
					    cellB.setCellValue(record.getR17_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR17_carrying_amount() != null) {
					    cellC.setCellValue(record.getR17_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR17_no_of_accts() != null) {
					    cellD.setCellValue(record.getR17_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row18
					row = sheet.getRow(17);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR18_collateral_amount() != null) {
					    cellB.setCellValue(record.getR18_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR18_carrying_amount() != null) {
					    cellC.setCellValue(record.getR18_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR18_no_of_accts() != null) {
					    cellD.setCellValue(record.getR18_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR19_collateral_amount() != null) {
					    cellB.setCellValue(record.getR19_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR19_carrying_amount() != null) {
					    cellC.setCellValue(record.getR19_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR19_no_of_accts() != null) {
					    cellD.setCellValue(record.getR19_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR20_collateral_amount() != null) {
					    cellB.setCellValue(record.getR20_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR20_carrying_amount() != null) {
					    cellC.setCellValue(record.getR20_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR20_no_of_accts() != null) {
					    cellD.setCellValue(record.getR20_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR21_collateral_amount() != null) {
					    cellB.setCellValue(record.getR21_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR21_carrying_amount() != null) {
					    cellC.setCellValue(record.getR21_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR21_no_of_accts() != null) {
					    cellD.setCellValue(record.getR21_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR22_collateral_amount() != null) {
					    cellB.setCellValue(record.getR22_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR22_carrying_amount() != null) {
					    cellC.setCellValue(record.getR22_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR22_no_of_accts() != null) {
					    cellD.setCellValue(record.getR22_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR23_collateral_amount() != null) {
					    cellB.setCellValue(record.getR23_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR23_carrying_amount() != null) {
					    cellC.setCellValue(record.getR23_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR23_no_of_accts() != null) {
					    cellD.setCellValue(record.getR23_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR24_collateral_amount() != null) {
					    cellB.setCellValue(record.getR24_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR24_carrying_amount() != null) {
					    cellC.setCellValue(record.getR24_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR24_no_of_accts() != null) {
					    cellD.setCellValue(record.getR24_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR25_collateral_amount() != null) {
					    cellB.setCellValue(record.getR25_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR25_carrying_amount() != null) {
					    cellC.setCellValue(record.getR25_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR25_no_of_accts() != null) {
					    cellD.setCellValue(record.getR25_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR26_collateral_amount() != null) {
					    cellB.setCellValue(record.getR26_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR26_carrying_amount() != null) {
					    cellC.setCellValue(record.getR26_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR26_no_of_accts() != null) {
					    cellD.setCellValue(record.getR26_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR27_collateral_amount() != null) {
					    cellB.setCellValue(record.getR27_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR27_carrying_amount() != null) {
					    cellC.setCellValue(record.getR27_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR27_no_of_accts() != null) {
					    cellD.setCellValue(record.getR27_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					
					// row29
					row = sheet.getRow(28);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR29_collateral_amount() != null) {
					    cellB.setCellValue(record.getR29_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR29_carrying_amount() != null) {
					    cellC.setCellValue(record.getR29_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR29_no_of_accts() != null) {
					    cellD.setCellValue(record.getR29_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR30_collateral_amount() != null) {
					    cellB.setCellValue(record.getR30_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR30_carrying_amount() != null) {
					    cellC.setCellValue(record.getR30_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR30_no_of_accts() != null) {
					    cellD.setCellValue(record.getR30_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR31_collateral_amount() != null) {
					    cellB.setCellValue(record.getR31_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR31_carrying_amount() != null) {
					    cellC.setCellValue(record.getR31_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR31_no_of_accts() != null) {
					    cellD.setCellValue(record.getR31_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR32_collateral_amount() != null) {
					    cellB.setCellValue(record.getR32_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR32_carrying_amount() != null) {
					    cellC.setCellValue(record.getR32_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR32_no_of_accts() != null) {
					    cellD.setCellValue(record.getR32_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR33_collateral_amount() != null) {
					    cellB.setCellValue(record.getR33_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR33_carrying_amount() != null) {
					    cellC.setCellValue(record.getR33_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR33_no_of_accts() != null) {
					    cellD.setCellValue(record.getR33_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR34_collateral_amount() != null) {
					    cellB.setCellValue(record.getR34_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR34_carrying_amount() != null) {
					    cellC.setCellValue(record.getR34_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR34_no_of_accts() != null) {
					    cellD.setCellValue(record.getR34_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR35_collateral_amount() != null) {
					    cellB.setCellValue(record.getR35_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR35_carrying_amount() != null) {
					    cellC.setCellValue(record.getR35_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR35_no_of_accts() != null) {
					    cellD.setCellValue(record.getR35_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR36_collateral_amount() != null) {
					    cellB.setCellValue(record.getR36_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR36_carrying_amount() != null) {
					    cellC.setCellValue(record.getR36_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR36_no_of_accts() != null) {
					    cellD.setCellValue(record.getR36_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row38
					row = sheet.getRow(37);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR38_collateral_amount() != null) {
					    cellB.setCellValue(record.getR38_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR38_carrying_amount() != null) {
					    cellC.setCellValue(record.getR38_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR38_no_of_accts() != null) {
					    cellD.setCellValue(record.getR38_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(38);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR39_collateral_amount() != null) {
					    cellB.setCellValue(record.getR39_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR39_carrying_amount() != null) {
					    cellC.setCellValue(record.getR39_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR39_no_of_accts() != null) {
					    cellD.setCellValue(record.getR39_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row41
					row = sheet.getRow(40);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR41_collateral_amount() != null) {
					    cellB.setCellValue(record.getR41_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR41_carrying_amount() != null) {
					    cellC.setCellValue(record.getR41_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR41_no_of_accts() != null) {
					    cellD.setCellValue(record.getR41_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row42
					row = sheet.getRow(41);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR42_collateral_amount() != null) {
					    cellB.setCellValue(record.getR42_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR42_carrying_amount() != null) {
					    cellC.setCellValue(record.getR42_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR42_no_of_accts() != null) {
					    cellD.setCellValue(record.getR42_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					
					// row44
					row = sheet.getRow(43);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR44_collateral_amount() != null) {
					    cellB.setCellValue(record.getR44_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR44_carrying_amount() != null) {
					    cellC.setCellValue(record.getR44_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR44_no_of_accts() != null) {
					    cellD.setCellValue(record.getR44_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row45
					row = sheet.getRow(44);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR45_collateral_amount() != null) {
					    cellB.setCellValue(record.getR45_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR45_carrying_amount() != null) {
					    cellC.setCellValue(record.getR45_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR45_no_of_accts() != null) {
					    cellD.setCellValue(record.getR45_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row46
					row = sheet.getRow(45);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR46_collateral_amount() != null) {
					    cellB.setCellValue(record.getR46_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR46_carrying_amount() != null) {
					    cellC.setCellValue(record.getR46_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR46_no_of_accts() != null) {
					    cellD.setCellValue(record.getR46_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row47
					row = sheet.getRow(46);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR47_collateral_amount() != null) {
					    cellB.setCellValue(record.getR47_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR47_carrying_amount() != null) {
					    cellC.setCellValue(record.getR47_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR47_no_of_accts() != null) {
					    cellD.setCellValue(record.getR47_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row49
					row = sheet.getRow(48);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR49_collateral_amount() != null) {
					    cellB.setCellValue(record.getR49_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR49_carrying_amount() != null) {
					    cellC.setCellValue(record.getR49_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR49_no_of_accts() != null) {
					    cellD.setCellValue(record.getR49_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row50
					row = sheet.getRow(49);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR50_collateral_amount() != null) {
					    cellB.setCellValue(record.getR50_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR50_carrying_amount() != null) {
					    cellC.setCellValue(record.getR50_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR50_no_of_accts() != null) {
					    cellD.setCellValue(record.getR50_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(50);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR51_collateral_amount() != null) {
					    cellB.setCellValue(record.getR51_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR51_carrying_amount() != null) {
					    cellC.setCellValue(record.getR51_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR51_no_of_accts() != null) {
					    cellD.setCellValue(record.getR51_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					
					// row53
					row = sheet.getRow(52);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR53_collateral_amount() != null) {
					    cellB.setCellValue(record.getR53_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR53_carrying_amount() != null) {
					    cellC.setCellValue(record.getR53_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR53_no_of_accts() != null) {
					    cellD.setCellValue(record.getR53_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR54_collateral_amount() != null) {
					    cellB.setCellValue(record.getR54_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR54_carrying_amount() != null) {
					    cellC.setCellValue(record.getR54_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR54_no_of_accts() != null) {
					    cellD.setCellValue(record.getR54_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(54);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR55_collateral_amount() != null) {
					    cellB.setCellValue(record.getR55_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR55_carrying_amount() != null) {
					    cellC.setCellValue(record.getR55_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR55_no_of_accts() != null) {
					    cellD.setCellValue(record.getR55_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}


					
					// row57
					row = sheet.getRow(56);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR57_collateral_amount() != null) {
					    cellB.setCellValue(record.getR57_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR57_carrying_amount() != null) {
					    cellC.setCellValue(record.getR57_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR57_no_of_accts() != null) {
					    cellD.setCellValue(record.getR57_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row58
					row = sheet.getRow(57);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR58_collateral_amount() != null) {
					    cellB.setCellValue(record.getR58_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR58_carrying_amount() != null) {
					    cellC.setCellValue(record.getR58_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR58_no_of_accts() != null) {
					    cellD.setCellValue(record.getR58_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row59
					row = sheet.getRow(58);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR59_collateral_amount() != null) {
					    cellB.setCellValue(record.getR59_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR59_carrying_amount() != null) {
					    cellC.setCellValue(record.getR59_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR59_no_of_accts() != null) {
					    cellD.setCellValue(record.getR59_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row60
					row = sheet.getRow(59);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR60_collateral_amount() != null) {
					    cellB.setCellValue(record.getR60_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR60_carrying_amount() != null) {
					    cellC.setCellValue(record.getR60_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR60_no_of_accts() != null) {
					    cellD.setCellValue(record.getR60_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row61
					row = sheet.getRow(60);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR61_collateral_amount() != null) {
					    cellB.setCellValue(record.getR61_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR61_carrying_amount() != null) {
					    cellC.setCellValue(record.getR61_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR61_no_of_accts() != null) {
					    cellD.setCellValue(record.getR61_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row62
					row = sheet.getRow(61);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR62_collateral_amount() != null) {
					    cellB.setCellValue(record.getR62_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR62_carrying_amount() != null) {
					    cellC.setCellValue(record.getR62_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR62_no_of_accts() != null) {
					    cellD.setCellValue(record.getR62_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
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

	public void updateReport(Q_RLFA1_Summary_Entity updatedEntity) {
		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		Q_RLFA1_Summary_Entity existing = brrs_q_rlfa1_Summary_Repo.findById(updatedEntity.getReport_date())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

		try {
			// 1️⃣ Loop from R11 to R23 and copy fields

			for (int i = 10; i <= 63; i++) {

				String prefix = "R" + i + "_";

				String[] fields = { "rene_loans", "collateral_amount", "carrying_amount", "no_of_accts" };

				for (String field : fields) {
					String getterName = "get" + prefix + field; // e.g., getR10
					String setterName = "set" + prefix + field; // e.g., setR10

					try {
						Method getter = Q_RLFA1_Summary_Entity.class.getMethod(getterName);
						Method setter = Q_RLFA1_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

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
		
		existing.setR27_new_column_carrying_amount(updatedEntity.getR27_new_column_carrying_amount());
		existing.setR27_new_column_collateral_amount(updatedEntity.getR27_new_column_collateral_amount());
		existing.setR27_new_column_no_of_accts(updatedEntity.getR27_new_column_no_of_accts());
		existing.setR27_new_column_rene_loans(updatedEntity.getR27_new_column_rene_loans());

		existing.setR42_new_column_carrying_amount(updatedEntity.getR42_new_column_carrying_amount());
		existing.setR42_new_column_collateral_amount(updatedEntity.getR42_new_column_collateral_amount());
		existing.setR42_new_column_no_of_accts(updatedEntity.getR42_new_column_no_of_accts());
		existing.setR42_new_column_rene_loans(updatedEntity.getR42_new_column_rene_loans());

		existing.setR48_new_column_carrying_amount(updatedEntity.getR48_new_column_carrying_amount());
		existing.setR48_new_column_collateral_amount(updatedEntity.getR48_new_column_collateral_amount());
		existing.setR48_new_column_no_of_accts(updatedEntity.getR48_new_column_no_of_accts());
		existing.setR48_new_column_rene_loans(updatedEntity.getR48_new_column_rene_loans());
		
		// 3️⃣ Save updated entity
		brrs_q_rlfa1_Summary_Repo.save(existing);
		
		Q_RLFA1_Detail_Entity_New detail = new Q_RLFA1_Detail_Entity_New();

	    BeanUtils.copyProperties(existing, detail);


	    new_brrs_q_rlfa1_Summary_Repo.save(detail);
	    
	    
		
	}
	
	
	public List<Object[]> getQ_RLFA1Resub() {
	    List<Object[]> resubList = new ArrayList<>();
	    try {
	        List<Q_RLFA1_Resub_Summary_Entity> latestArchivalList = 
	        		brrs_Q_RLFA1_Resub_Summary_Repo.getdatabydateListWithVersionAll();

	        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
	            for (Q_RLFA1_Resub_Summary_Entity entity : latestArchivalList) {
	                Object[] row = new Object[] {
	                    entity.getReport_date(),
	                    entity.getReport_version(),
	                    entity.getReportResubDate()
	                };
	                resubList.add(row);
	            }
	            System.out.println("Fetched " + resubList.size() + " record(s)");
	        } else {
	            System.out.println("No archival data found.");
	        }
	    } catch (Exception e) {
	        System.err.println("Error fetching Q_RLFA1 Resub data: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return resubList;
	}
	
	
	
	
	
	public void updateReportResub(Q_RLFA1_Summary_Entity updatedEntity) {
	    System.out.println("Came to Resub Service");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    // Use entity field directly (same name as in entity)
	    Date report_date = updatedEntity.getReport_date();
	    BigDecimal newVersion = BigDecimal.ONE;

	    try {
	        // ✅ use the same variable name as in repo method
	        Optional<Q_RLFA1_Archival_Summary_Entity> latestArchivalOpt =
	        		q_rlfa1_Archival_Summary_Repo.getLatestArchivalVersionByDate(report_date);

	        // Determine next version
	        if (latestArchivalOpt.isPresent()) {
	        	Q_RLFA1_Archival_Summary_Entity latestArchival = latestArchivalOpt.get();
	            try {
	                newVersion = latestArchival.getReport_version().add(BigDecimal.ONE);
	            } catch (NumberFormatException e) {
	                System.err.println("Invalid version format. Defaulting to version 1");
	                newVersion = BigDecimal.ONE;
	            }
	        } else {
	            System.out.println("No previous archival found for date: " + report_date);
	        }

	        // Prevent duplicate version
	        boolean exists = q_rlfa1_Archival_Summary_Repo
	                .findByReport_dateAndReport_version(report_date,newVersion)
	                .isPresent();

	        if (exists) {
	            throw new RuntimeException("Version " + newVersion + " already exists for report date " + report_date);
	        }

	        // Copy summary entity to archival entity
	        Q_RLFA1_Archival_Summary_Entity archivalEntity = new Q_RLFA1_Archival_Summary_Entity();
	        org.springframework.beans.BeanUtils.copyProperties(updatedEntity, archivalEntity);

	        archivalEntity.setReport_date(report_date);
	        archivalEntity.setReport_version(newVersion);
	        archivalEntity.setReportResubDate(new Date());

	        System.out.println("Saving new archival version: " + newVersion);
	        q_rlfa1_Archival_Summary_Repo.save(archivalEntity);
	        
	        Q_RLFA1_Archival_Detail_Entity detail = new Q_RLFA1_Archival_Detail_Entity();

		    BeanUtils.copyProperties(archivalEntity, detail);


		    q_rlfa1_Archival_Detail_Repo.save(detail);
		    
		    Q_RLFA1_Resub_Summary_Entity detailResub = new Q_RLFA1_Resub_Summary_Entity();

		    BeanUtils.copyProperties(archivalEntity, detailResub);


		    brrs_Q_RLFA1_Resub_Summary_Repo.save(detailResub);
		    
		    Q_RLFA1_Resub_Detail_Entity summaryResub = new Q_RLFA1_Resub_Detail_Entity();

		    BeanUtils.copyProperties(archivalEntity, summaryResub);


		    brrs_Q_RLFA1_Resub_Detail_Repo.save(summaryResub);

	        System.out.println("Saved archival version successfully: " + newVersion);

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error while creating archival resubmission record", e);
	    }
	}
	


	/// Downloaded for Archival & Resub
	public byte[] BRRS_Q_RLFA1ResubExcel(String filename, String reportId, String fromdate,
        String todate, String currency, String dtltype,
        String type, BigDecimal version) throws Exception {

    logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

    if (type.equals("RESUB") & version != null) {
       
    }

    List<Q_RLFA1_Archival_Summary_Entity> dataList = q_rlfa1_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

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
			
			if("email_report".equalsIgnoreCase(dtltype)) {
				
				Q_RLFA1_Archival_Summary_Entity record1 = dataList.get(0);
						
						Row row = sheet.getRow(7) != null ? sheet.getRow(7) : sheet.createRow(7);
						 Cell cell2 = row.createCell(2);

						 if (record1.getR10_collateral_amount() != null) {
						     cell2.setCellValue(record1.getR10_collateral_amount().doubleValue());
						     cell2.setCellStyle(numberStyle);
						 } else {
						     cell2.setCellValue("");
						     cell2.setCellStyle(textStyle);
						 }

						  Cell cell3 = row.createCell(3);

						 if (record1.getR10_carrying_amount() != null) {
						     cell3.setCellValue(record1.getR10_carrying_amount().doubleValue());
						     cell3.setCellStyle(numberStyle);
						 } else {
						     cell3.setCellValue("");
						     cell3.setCellStyle(textStyle);
						 }

						  Cell cell4 = row.createCell(5);

						 if (record1.getR10_no_of_accts() != null) {
						     cell4.setCellValue(record1.getR10_no_of_accts().doubleValue());
						     cell4.setCellStyle(numberStyle);
						 } else {
						     cell4.setCellValue("");
						     cell4.setCellStyle(textStyle);
						 }


						 row = sheet.getRow(8) != null ? sheet.getRow(8) : sheet.createRow(8);
						  Cell cell5 = row.createCell(2);

						 if (record1.getR11_collateral_amount() != null) {
						     cell5.setCellValue(record1.getR11_collateral_amount().doubleValue());
						     cell5.setCellStyle(numberStyle);
						 } else {
						     cell5.setCellValue("");
						     cell5.setCellStyle(textStyle);
						 }

						  Cell cell6 = row.createCell(3);

						 if (record1.getR11_carrying_amount() != null) {
						     cell6.setCellValue(record1.getR11_carrying_amount().doubleValue());
						     cell6.setCellStyle(numberStyle);
						 } else {
						     cell6.setCellValue("");
						     cell6.setCellStyle(textStyle);
						 }

						  Cell cell7 = row.createCell(5);

						 if (record1.getR11_no_of_accts() != null) {
						     cell7.setCellValue(record1.getR11_no_of_accts().doubleValue());
						     cell7.setCellStyle(numberStyle);
						 } else {
						     cell7.setCellValue("");
						     cell7.setCellStyle(textStyle);
						 }


						 row = sheet.getRow(9) != null ? sheet.getRow(9) : sheet.createRow(9);
						  Cell cell8 = row.createCell(2);

						 if (record1.getR12_collateral_amount() != null) {
						     cell8.setCellValue(record1.getR12_collateral_amount().doubleValue());
						     cell8.setCellStyle(numberStyle);
						 } else {
						     cell8.setCellValue("");
						     cell8.setCellStyle(textStyle);
						 }

						  Cell cell9 = row.createCell(3);

						 if (record1.getR12_carrying_amount() != null) {
						     cell9.setCellValue(record1.getR12_carrying_amount().doubleValue());
						     cell9.setCellStyle(numberStyle);
						 } else {
						     cell9.setCellValue("");
						     cell9.setCellStyle(textStyle);
						 }

						  Cell cell10 = row.createCell(5);

						 if (record1.getR12_no_of_accts() != null) {
						     cell10.setCellValue(record1.getR12_no_of_accts().doubleValue());
						     cell10.setCellStyle(numberStyle);
						 } else {
						     cell10.setCellValue("");
						     cell10.setCellStyle(textStyle);
						 }


						 row = sheet.getRow(10) != null ? sheet.getRow(10) : sheet.createRow(10);
						  Cell cell11 = row.createCell(2);

						 if (record1.getR13_collateral_amount() != null) {
						     cell11.setCellValue(record1.getR13_collateral_amount().doubleValue());
						     cell11.setCellStyle(numberStyle);
						 } else {
						     cell11.setCellValue("");
						     cell11.setCellStyle(textStyle);
						 }

						  Cell cell12 = row.createCell(3);

						 if (record1.getR13_carrying_amount() != null) {
						     cell12.setCellValue(record1.getR13_carrying_amount().doubleValue());
						     cell12.setCellStyle(numberStyle);
						 } else {
						     cell12.setCellValue("");
						     cell12.setCellStyle(textStyle);
						 }

						  Cell cell13 = row.createCell(5);

						 if (record1.getR13_no_of_accts() != null) {
						     cell13.setCellValue(record1.getR13_no_of_accts().doubleValue());
						     cell13.setCellStyle(numberStyle);
						 } else {
						     cell13.setCellValue("");
						     cell13.setCellStyle(textStyle);
						 }


						 row = sheet.getRow(11) != null ? sheet.getRow(11) : sheet.createRow(11);
						  Cell cell14 = row.createCell(2);

						 if (record1.getR14_collateral_amount() != null) {
						     cell14.setCellValue(record1.getR14_collateral_amount().doubleValue());
						     cell14.setCellStyle(numberStyle);
						 } else {
						     cell14.setCellValue("");
						     cell14.setCellStyle(textStyle);
						 }

						  Cell cell15 = row.createCell(3);

						 if (record1.getR14_carrying_amount() != null) {
						     cell15.setCellValue(record1.getR14_carrying_amount().doubleValue());
						     cell15.setCellStyle(numberStyle);
						 } else {
						     cell15.setCellValue("");
						     cell15.setCellStyle(textStyle);
						 }

						  Cell cell16 = row.createCell(5);

						 if (record1.getR14_no_of_accts() != null) {
						     cell16.setCellValue(record1.getR14_no_of_accts().doubleValue());
						     cell16.setCellStyle(numberStyle);
						 } else {
						     cell16.setCellValue("");
						     cell16.setCellStyle(textStyle);
						 }


						 row = sheet.getRow(12) != null ? sheet.getRow(12) : sheet.createRow(12);
						  Cell cell17 = row.createCell(2);

						 if (record1.getR15_collateral_amount() != null) {
						     cell17.setCellValue(record1.getR15_collateral_amount().doubleValue());
						     cell17.setCellStyle(numberStyle);
						 } else {
						     cell17.setCellValue("");
						     cell17.setCellStyle(textStyle);
						 }

						  Cell cell18 = row.createCell(3);

						 if (record1.getR15_carrying_amount() != null) {
						     cell18.setCellValue(record1.getR15_carrying_amount().doubleValue());
						     cell18.setCellStyle(numberStyle);
						 } else {
						     cell18.setCellValue("");
						     cell18.setCellStyle(textStyle);
						 }

						  Cell cell19 = row.createCell(5);

						 if (record1.getR15_no_of_accts() != null) {
						     cell19.setCellValue(record1.getR15_no_of_accts().doubleValue());
						     cell19.setCellStyle(numberStyle);
						 } else {
						     cell19.setCellValue("");
						     cell19.setCellStyle(textStyle);
						 }


						 row = sheet.getRow(13) != null ? sheet.getRow(13) : sheet.createRow(13);
						  Cell cell20 = row.createCell(2);

						 if (record1.getR16_collateral_amount() != null) {
						     cell20.setCellValue(record1.getR16_collateral_amount().doubleValue());
						     cell20.setCellStyle(numberStyle);
						 } else {
						     cell20.setCellValue("");
						     cell20.setCellStyle(textStyle);
						 }

						  Cell cell21 = row.createCell(3);

						 if (record1.getR16_carrying_amount() != null) {
						     cell21.setCellValue(record1.getR16_carrying_amount().doubleValue());
						     cell21.setCellStyle(numberStyle);
						 } else {
						     cell21.setCellValue("");
						     cell21.setCellStyle(textStyle);
						 }

						  Cell cell22 = row.createCell(5);

						 if (record1.getR16_no_of_accts() != null) {
						     cell22.setCellValue(record1.getR16_no_of_accts().doubleValue());
						     cell22.setCellStyle(numberStyle);
						 } else {
						     cell22.setCellValue("");
						     cell22.setCellStyle(textStyle);
						 }


						 row = sheet.getRow(14) != null ? sheet.getRow(14) : sheet.createRow(14);
						  Cell cell23 = row.createCell(2);

						 if (record1.getR17_collateral_amount() != null) {
						     cell23.setCellValue(record1.getR17_collateral_amount().doubleValue());
						     cell23.setCellStyle(numberStyle);
						 } else {
						     cell23.setCellValue("");
						     cell23.setCellStyle(textStyle);
						 }

						  Cell cell24 = row.createCell(3);

						 if (record1.getR17_carrying_amount() != null) {
						     cell24.setCellValue(record1.getR17_carrying_amount().doubleValue());
						     cell24.setCellStyle(numberStyle);
						 } else {
						     cell24.setCellValue("");
						     cell24.setCellStyle(textStyle);
						 }

						  Cell cell25 = row.createCell(5);

						 if (record1.getR17_no_of_accts() != null) {
						     cell25.setCellValue(record1.getR17_no_of_accts().doubleValue());
						     cell25.setCellStyle(numberStyle);
						 } else {
						     cell25.setCellValue("");
						     cell25.setCellStyle(textStyle);
						 }


						 row = sheet.getRow(15) != null ? sheet.getRow(15) : sheet.createRow(15);
						  Cell cell26 = row.createCell(2);

						 if (record1.getR18_collateral_amount() != null) {
						     cell26.setCellValue(record1.getR18_collateral_amount().doubleValue());
						     cell26.setCellStyle(numberStyle);
						 } else {
						     cell26.setCellValue("");
						     cell26.setCellStyle(textStyle);
						 }

						  Cell cell27 = row.createCell(3);

						 if (record1.getR18_carrying_amount() != null) {
						     cell27.setCellValue(record1.getR18_carrying_amount().doubleValue());
						     cell27.setCellStyle(numberStyle);
						 } else {
						     cell27.setCellValue("");
						     cell27.setCellStyle(textStyle);
						 }

						  Cell cell28 = row.createCell(5);

						 if (record1.getR18_no_of_accts() != null) {
						     cell28.setCellValue(record1.getR18_no_of_accts().doubleValue());
						     cell28.setCellStyle(numberStyle);
						 } else {
						     cell28.setCellValue("");
						     cell28.setCellStyle(textStyle);
						 }


//						 row = sheet.getRow(16) != null ? sheet.getRow(16) : sheet.createRow(16);
//						  Cell cell29 = row.createCell(2);
		//
//						 if (record1.getR19_collateral_amount() != null) {
//						     cell29.setCellValue(record1.getR19_collateral_amount().doubleValue());
//						     cell29.setCellStyle(numberStyle);
//						 } else {
//						     cell29.setCellValue("");
//						     cell29.setCellStyle(textStyle);
//						 }
		//
//						  Cell cell30 = row.createCell(3);
		//
//						 if (record1.getR19_carrying_amount() != null) {
//						     cell30.setCellValue(record1.getR19_carrying_amount().doubleValue());
//						     cell30.setCellStyle(numberStyle);
//						 } else {
//						     cell30.setCellValue("");
//						     cell30.setCellStyle(textStyle);
//						 }
		//
//						  Cell cell31 = row.createCell(4);
		//
//						 if (record1.getR19_no_of_accts() != null) {
//						     cell31.setCellValue(record1.getR19_no_of_accts().doubleValue());
//						     cell31.setCellStyle(numberStyle);
//						 } else {
//						     cell31.setCellValue("");
//						     cell31.setCellStyle(textStyle);
//						 }
		//
		//
//						 row = sheet.getRow(17) != null ? sheet.getRow(17) : sheet.createRow(17);
//						  Cell cell32 = row.createCell(2);
		//
//						 if (record1.getR20_collateral_amount() != null) {
//						     cell32.setCellValue(record1.getR20_collateral_amount().doubleValue());
//						     cell32.setCellStyle(numberStyle);
//						 } else {
//						     cell32.setCellValue("");
//						     cell32.setCellStyle(textStyle);
//						 }
		//
//						  Cell cell33 = row.createCell(3);
		//
//						 if (record1.getR20_carrying_amount() != null) {
//						     cell33.setCellValue(record1.getR20_carrying_amount().doubleValue());
//						     cell33.setCellStyle(numberStyle);
//						 } else {
//						     cell33.setCellValue("");
//						     cell33.setCellStyle(textStyle);
//						 }
		//
//						  Cell cell34 = row.createCell(4);
		//
//						 if (record1.getR20_no_of_accts() != null) {
//						     cell34.setCellValue(record1.getR20_no_of_accts().doubleValue());
//						     cell34.setCellStyle(numberStyle);
//						 } else {
//						     cell34.setCellValue("");
//						     cell34.setCellStyle(textStyle);
//						 }
		//
		//
//						 row = sheet.getRow(18) != null ? sheet.getRow(18) : sheet.createRow(18);
//						  Cell cell35 = row.createCell(2);
		//
//						 if (record1.getR21_collateral_amount() != null) {
//						     cell35.setCellValue(record1.getR21_collateral_amount().doubleValue());
//						     cell35.setCellStyle(numberStyle);
//						 } else {
//						     cell35.setCellValue("");
//						     cell35.setCellStyle(textStyle);
//						 }
		//
//						  Cell cell36 = row.createCell(3);
		//
//						 if (record1.getR21_carrying_amount() != null) {
//						     cell36.setCellValue(record1.getR21_carrying_amount().doubleValue());
//						     cell36.setCellStyle(numberStyle);
//						 } else {
//						     cell36.setCellValue("");
//						     cell36.setCellStyle(textStyle);
//						 }
		//
//						  Cell cell37 = row.createCell(4);
		//
//						 if (record1.getR21_no_of_accts() != null) {
//						     cell37.setCellValue(record1.getR21_no_of_accts().doubleValue());
//						     cell37.setCellStyle(numberStyle);
//						 } else {
//						     cell37.setCellValue("");
//						     cell37.setCellStyle(textStyle);
//						 }
		//
		//
//						 row = sheet.getRow(19) != null ? sheet.getRow(19) : sheet.createRow(19);
//						  Cell cell38 = row.createCell(2);
		//
//						 if (record1.getR22_collateral_amount() != null) {
//						     cell38.setCellValue(record1.getR22_collateral_amount().doubleValue());
//						     cell38.setCellStyle(numberStyle);
//						 } else {
//						     cell38.setCellValue("");
//						     cell38.setCellStyle(textStyle);
//						 }
		//
//						  Cell cell39 = row.createCell(3);
		//
//						 if (record1.getR22_carrying_amount() != null) {
//						     cell39.setCellValue(record1.getR22_carrying_amount().doubleValue());
//						     cell39.setCellStyle(numberStyle);
//						 } else {
//						     cell39.setCellValue("");
//						     cell39.setCellStyle(textStyle);
//						 }
		//
//						  Cell cell40 = row.createCell(4);
		//
//						 if (record1.getR22_no_of_accts() != null) {
//						     cell40.setCellValue(record1.getR22_no_of_accts().doubleValue());
//						     cell40.setCellStyle(numberStyle);
//						 } else {
//						     cell40.setCellValue("");
//						     cell40.setCellStyle(textStyle);
//						 }
		//
		//
//						 row = sheet.getRow(20) != null ? sheet.getRow(20) : sheet.createRow(20);
//						  Cell cell41 = row.createCell(2);
		//
//						 if (record1.getR23_collateral_amount() != null) {
//						     cell41.setCellValue(record1.getR23_collateral_amount().doubleValue());
//						     cell41.setCellStyle(numberStyle);
//						 } else {
//						     cell41.setCellValue("");
//						     cell41.setCellStyle(textStyle);
//						 }
		//
//						  Cell cell42 = row.createCell(3);
		//
//						 if (record1.getR23_carrying_amount() != null) {
//						     cell42.setCellValue(record1.getR23_carrying_amount().doubleValue());
//						     cell42.setCellStyle(numberStyle);
//						 } else {
//						     cell42.setCellValue("");
//						     cell42.setCellStyle(textStyle);
//						 }
		//
//						  Cell cell43 = row.createCell(4);
		//
//						 if (record1.getR23_no_of_accts() != null) {
//						     cell43.setCellValue(record1.getR23_no_of_accts().doubleValue());
//						     cell43.setCellStyle(numberStyle);
//						 } else {
//						     cell43.setCellValue("");
//						     cell43.setCellStyle(textStyle);
//						 }
		//
		//
//						 row = sheet.getRow(21) != null ? sheet.getRow(21) : sheet.createRow(21);
//						  Cell cell44 = row.createCell(2);
		//
//						 if (record1.getR24_collateral_amount() != null) {
//						     cell44.setCellValue(record1.getR24_collateral_amount().doubleValue());
//						     cell44.setCellStyle(numberStyle);
//						 } else {
//						     cell44.setCellValue("");
//						     cell44.setCellStyle(textStyle);
//						 }
		//
//						  Cell cell45 = row.createCell(3);
		//
//						 if (record1.getR24_carrying_amount() != null) {
//						     cell45.setCellValue(record1.getR24_carrying_amount().doubleValue());
//						     cell45.setCellStyle(numberStyle);
//						 } else {
//						     cell45.setCellValue("");
//						     cell45.setCellStyle(textStyle);
//						 }
		//
//						  Cell cell46 = row.createCell(4);
		//
//						 if (record1.getR24_no_of_accts() != null) {
//						     cell46.setCellValue(record1.getR24_no_of_accts().doubleValue());
//						     cell46.setCellStyle(numberStyle);
//						 } else {
//						     cell46.setCellValue("");
//						     cell46.setCellStyle(textStyle);
//						 }
		//
		//
//						 row = sheet.getRow(22) != null ? sheet.getRow(22) : sheet.createRow(22);
//						  Cell cell47 = row.createCell(2);
		//
//						 if (record1.getR25_collateral_amount() != null) {
//						     cell47.setCellValue(record1.getR25_collateral_amount().doubleValue());
//						     cell47.setCellStyle(numberStyle);
//						 } else {
//						     cell47.setCellValue("");
//						     cell47.setCellStyle(textStyle);
//						 }
		//
//						  Cell cell48 = row.createCell(3);
		//
//						 if (record1.getR25_carrying_amount() != null) {
//						     cell48.setCellValue(record1.getR25_carrying_amount().doubleValue());
//						     cell48.setCellStyle(numberStyle);
//						 } else {
//						     cell48.setCellValue("");
//						     cell48.setCellStyle(textStyle);
//						 }
		//
//						  Cell cell49 = row.createCell(4);
		//
//						 if (record1.getR25_no_of_accts() != null) {
//						     cell49.setCellValue(record1.getR25_no_of_accts().doubleValue());
//						     cell49.setCellStyle(numberStyle);
//						 } else {
//						     cell49.setCellValue("");
//						     cell49.setCellStyle(textStyle);
//						 }
		//
		//
//						 row = sheet.getRow(23) != null ? sheet.getRow(23) : sheet.createRow(23);
//						  Cell cell50 = row.createCell(2);
		//
//						 if (record1.getR28_collateral_amount() != null) {
//						     cell50.setCellValue(record1.getR28_collateral_amount().doubleValue());
//						     cell50.setCellStyle(numberStyle);
//						 } else {
//						     cell50.setCellValue("");
//						     cell50.setCellStyle(textStyle);
//						 }
		//
//						  Cell cell51 = row.createCell(3);
		//
//						 if (record1.getR28_carrying_amount() != null) {
//						     cell51.setCellValue(record1.getR28_carrying_amount().doubleValue());
//						     cell51.setCellStyle(numberStyle);
//						 } else {
//						     cell51.setCellValue("");
//						     cell51.setCellStyle(textStyle);
//						 }
		//
//						  Cell cell52 = row.createCell(4);
		//
//						 if (record1.getR28_no_of_accts() != null) {
//						     cell52.setCellValue(record1.getR28_no_of_accts().doubleValue());
//						     cell52.setCellStyle(numberStyle);
//						 } else {
//						     cell52.setCellValue("");
//						     cell52.setCellStyle(textStyle);
//						 }
		//
		//
//						 row = sheet.getRow(24) != null ? sheet.getRow(24) : sheet.createRow(24);
//						  Cell cell53 = row.createCell(2);
		//
//						 if (record1.getR29_collateral_amount() != null) {
//						     cell53.setCellValue(record1.getR29_collateral_amount().doubleValue());
//						     cell53.setCellStyle(numberStyle);
//						 } else {
//						     cell53.setCellValue("");
//						     cell53.setCellStyle(textStyle);
//						 }
		//
//						  Cell cell54 = row.createCell(3);
		//
//						 if (record1.getR29_carrying_amount() != null) {
//						     cell54.setCellValue(record1.getR29_carrying_amount().doubleValue());
//						     cell54.setCellStyle(numberStyle);
//						 } else {
//						     cell54.setCellValue("");
//						     cell54.setCellStyle(textStyle);
//						 }
		//
//						  Cell cell55 = row.createCell(4);
		//
//						 if (record1.getR29_no_of_accts() != null) {
//						     cell55.setCellValue(record1.getR29_no_of_accts().doubleValue());
//						     cell55.setCellStyle(numberStyle);
//						 } else {
//						     cell55.setCellValue("");
//						     cell55.setCellStyle(textStyle);
//						 }
		//
		//
//						 row = sheet.getRow(25) != null ? sheet.getRow(25) : sheet.createRow(25);
//						  Cell cell56 = row.createCell(2);
		//
//						 if (record1.getR30_collateral_amount() != null) {
//						     cell56.setCellValue(record1.getR30_collateral_amount().doubleValue());
//						     cell56.setCellStyle(numberStyle);
//						 } else {
//						     cell56.setCellValue("");
//						     cell56.setCellStyle(textStyle);
//						 }
		//
//						  Cell cell57 = row.createCell(3);
		//
//						 if (record1.getR30_carrying_amount() != null) {
//						     cell57.setCellValue(record1.getR30_carrying_amount().doubleValue());
//						     cell57.setCellStyle(numberStyle);
//						 } else {
//						     cell57.setCellValue("");
//						     cell57.setCellStyle(textStyle);
//						 }
		//
//						  Cell cell58 = row.createCell(4);
		//
//						 if (record1.getR30_no_of_accts() != null) {
//						     cell58.setCellValue(record1.getR30_no_of_accts().doubleValue());
//						     cell58.setCellStyle(numberStyle);
//						 } else {
//						     cell58.setCellValue("");
//						     cell58.setCellStyle(textStyle);
//						 }
		//
//						 row = sheet.getRow(27) != null ? sheet.getRow(27) : sheet.createRow(27);
//						 Cell cell59 = row.createCell(2);
		//
//						if (record1.getR32_collateral_amount() != null) {
//						    cell59.setCellValue(record1.getR32_collateral_amount().doubleValue());
//						    cell59.setCellStyle(numberStyle);
//						} else {
//						    cell59.setCellValue("");
//						    cell59.setCellStyle(textStyle);
//						}
		//
//						 Cell cell60 = row.createCell(3);
		//
//						if (record1.getR32_carrying_amount() != null) {
//						    cell60.setCellValue(record1.getR32_carrying_amount().doubleValue());
//						    cell60.setCellStyle(numberStyle);
//						} else {
//						    cell60.setCellValue("");
//						    cell60.setCellStyle(textStyle);
//						}
		//
//						 Cell cell61 = row.createCell(4);
		//
//						if (record1.getR32_no_of_accts() != null) {
//						    cell61.setCellValue(record1.getR32_no_of_accts().doubleValue());
//						    cell61.setCellStyle(numberStyle);
//						} else {
//						    cell61.setCellValue("");
//						    cell61.setCellStyle(textStyle);
//						}
		//
		//
//						 row = sheet.getRow(28) != null ? sheet.getRow(28) : sheet.createRow(28);
//						 Cell cell62 = row.createCell(2);
		//
//						if (record1.getR33_collateral_amount() != null) {
//						    cell62.setCellValue(record1.getR33_collateral_amount().doubleValue());
//						    cell62.setCellStyle(numberStyle);
//						} else {
//						    cell62.setCellValue("");
//						    cell62.setCellStyle(textStyle);
//						}
		//
//						 Cell cell63 = row.createCell(3);
		//
//						if (record1.getR33_carrying_amount() != null) {
//						    cell63.setCellValue(record1.getR33_carrying_amount().doubleValue());
//						    cell63.setCellStyle(numberStyle);
//						} else {
//						    cell63.setCellValue("");
//						    cell63.setCellStyle(textStyle);
//						}
		//
//						 Cell cell64 = row.createCell(4);
		//
//						if (record1.getR33_no_of_accts() != null) {
//						    cell64.setCellValue(record1.getR33_no_of_accts().doubleValue());
//						    cell64.setCellStyle(numberStyle);
//						} else {
//						    cell64.setCellValue("");
//						    cell64.setCellStyle(textStyle);
//						}
		//
		//
//						 row = sheet.getRow(29) != null ? sheet.getRow(29) : sheet.createRow(29);
//						 Cell cell65 = row.createCell(2);
		//
//						if (record1.getR31_collateral_amount() != null) {
//						    cell65.setCellValue(record1.getR31_collateral_amount().doubleValue());
//						    cell65.setCellStyle(numberStyle);
//						} else {
//						    cell65.setCellValue("");
//						    cell65.setCellStyle(textStyle);
//						}
		//
//						 Cell cell66 = row.createCell(3);
		//
//						if (record1.getR31_carrying_amount() != null) {
//						    cell66.setCellValue(record1.getR31_carrying_amount().doubleValue());
//						    cell66.setCellStyle(numberStyle);
//						} else {
//						    cell66.setCellValue("");
//						    cell66.setCellStyle(textStyle);
//						}
		//
//						 Cell cell67 = row.createCell(4);
		//
//						if (record1.getR31_no_of_accts() != null) {
//						    cell67.setCellValue(record1.getR31_no_of_accts().doubleValue());
//						    cell67.setCellStyle(numberStyle);
//						} else {
//						    cell67.setCellValue("");
//						    cell67.setCellStyle(textStyle);
//						}
		//
		//
//						 row = sheet.getRow(30) != null ? sheet.getRow(30) : sheet.createRow(30);
//						 Cell cell68 = row.createCell(2);
		//
//						if (record1.getR34_collateral_amount() != null) {
//						    cell68.setCellValue(record1.getR34_collateral_amount().doubleValue());
//						    cell68.setCellStyle(numberStyle);
//						} else {
//						    cell68.setCellValue("");
//						    cell68.setCellStyle(textStyle);
//						}
		//
//						 Cell cell69 = row.createCell(3);
		//
//						if (record1.getR34_carrying_amount() != null) {
//						    cell69.setCellValue(record1.getR34_carrying_amount().doubleValue());
//						    cell69.setCellStyle(numberStyle);
//						} else {
//						    cell69.setCellValue("");
//						    cell69.setCellStyle(textStyle);
//						}
		//
//						 Cell cell70 = row.createCell(4);
		//
//						if (record1.getR34_no_of_accts() != null) {
//						    cell70.setCellValue(record1.getR34_no_of_accts().doubleValue());
//						    cell70.setCellStyle(numberStyle);
//						} else {
//						    cell70.setCellValue("");
//						    cell70.setCellStyle(textStyle);
//						}
		//
		//
//						 row = sheet.getRow(31) != null ? sheet.getRow(31) : sheet.createRow(31);
//						 Cell cell71 = row.createCell(2);
		//
//						if (record1.getR36_collateral_amount() != null) {
//						    cell71.setCellValue(record1.getR36_collateral_amount().doubleValue());
//						    cell71.setCellStyle(numberStyle);
//						} else {
//						    cell71.setCellValue("");
//						    cell71.setCellStyle(textStyle);
//						}
		//
//						 Cell cell72 = row.createCell(3);
		//
//						if (record1.getR36_carrying_amount() != null) {
//						    cell72.setCellValue(record1.getR36_carrying_amount().doubleValue());
//						    cell72.setCellStyle(numberStyle);
//						} else {
//						    cell72.setCellValue("");
//						    cell72.setCellStyle(textStyle);
//						}
		//
//						 Cell cell73 = row.createCell(4);
		//
//						if (record1.getR36_no_of_accts() != null) {
//						    cell73.setCellValue(record1.getR36_no_of_accts().doubleValue());
//						    cell73.setCellStyle(numberStyle);
//						} else {
//						    cell73.setCellValue("");
//						    cell73.setCellStyle(textStyle);
//						}
		//
		//
//						 row = sheet.getRow(32) != null ? sheet.getRow(32) : sheet.createRow(32);
//						 Cell cell74 = row.createCell(2);
		//
//						if (record1.getR35_collateral_amount() != null) {
//						    cell74.setCellValue(record1.getR35_collateral_amount().doubleValue());
//						    cell74.setCellStyle(numberStyle);
//						} else {
//						    cell74.setCellValue("");
//						    cell74.setCellStyle(textStyle);
//						}
		//
//						 Cell cell75 = row.createCell(3);
		//
//						if (record1.getR35_carrying_amount() != null) {
//						    cell75.setCellValue(record1.getR35_carrying_amount().doubleValue());
//						    cell75.setCellStyle(numberStyle);
//						} else {
//						    cell75.setCellValue("");
//						    cell75.setCellStyle(textStyle);
//						}
		//
//						 Cell cell76 = row.createCell(4);
		//
//						if (record1.getR35_no_of_accts() != null) {
//						    cell76.setCellValue(record1.getR35_no_of_accts().doubleValue());
//						    cell76.setCellStyle(numberStyle);
//						} else {
//						    cell76.setCellValue("");
//						    cell76.setCellStyle(textStyle);
//						}
		//
		//
//						 row = sheet.getRow(33) != null ? sheet.getRow(33) : sheet.createRow(33);
//						 Cell cell77 = row.createCell(2);
		//
//						if (record1.getR37_collateral_amount() != null) {
//						    cell77.setCellValue(record1.getR37_collateral_amount().doubleValue());
//						    cell77.setCellStyle(numberStyle);
//						} else {
//						    cell77.setCellValue("");
//						    cell77.setCellStyle(textStyle);
//						}
		//
//						 Cell cell78 = row.createCell(3);
		//
//						if (record1.getR37_carrying_amount() != null) {
//						    cell78.setCellValue(record1.getR37_carrying_amount().doubleValue());
//						    cell78.setCellStyle(numberStyle);
//						} else {
//						    cell78.setCellValue("");
//						    cell78.setCellStyle(textStyle);
//						}
		//
//						 Cell cell79 = row.createCell(4);
		//
//						if (record1.getR37_no_of_accts() != null) {
//						    cell79.setCellValue(record1.getR37_no_of_accts().doubleValue());
//						    cell79.setCellStyle(numberStyle);
//						} else {
//						    cell79.setCellValue("");
//						    cell79.setCellStyle(textStyle);
//						}
		//
		//
//						 row = sheet.getRow(34) != null ? sheet.getRow(34) : sheet.createRow(34);
//						 Cell cell80 = row.createCell(2);
		//
//						if (record1.getR38_collateral_amount() != null) {
//						    cell80.setCellValue(record1.getR38_collateral_amount().doubleValue());
//						    cell80.setCellStyle(numberStyle);
//						} else {
//						    cell80.setCellValue("");
//						    cell80.setCellStyle(textStyle);
//						}
		//
//						 Cell cell81 = row.createCell(3);
		//
//						if (record1.getR38_carrying_amount() != null) {
//						    cell81.setCellValue(record1.getR38_carrying_amount().doubleValue());
//						    cell81.setCellStyle(numberStyle);
//						} else {
//						    cell81.setCellValue("");
//						    cell81.setCellStyle(textStyle);
//						}
		//
//						 Cell cell82 = row.createCell(4);
		//
//						if (record1.getR38_no_of_accts() != null) {
//						    cell82.setCellValue(record1.getR38_no_of_accts().doubleValue());
//						    cell82.setCellStyle(numberStyle);
//						} else {
//						    cell82.setCellValue("");
//						    cell82.setCellStyle(textStyle);
//						}
		//
		//
//						 row = sheet.getRow(35) != null ? sheet.getRow(35) : sheet.createRow(35);
//						 Cell cell83 = row.createCell(2);
		//
//						if (record1.getR39_collateral_amount() != null) {
//						    cell83.setCellValue(record1.getR39_collateral_amount().doubleValue());
//						    cell83.setCellStyle(numberStyle);
//						} else {
//						    cell83.setCellValue("");
//						    cell83.setCellStyle(textStyle);
//						}
		//
//						 Cell cell84 = row.createCell(3);
		//
//						if (record1.getR39_carrying_amount() != null) {
//						    cell84.setCellValue(record1.getR39_carrying_amount().doubleValue());
//						    cell84.setCellStyle(numberStyle);
//						} else {
//						    cell84.setCellValue("");
//						    cell84.setCellStyle(textStyle);
//						}
		//
//						 Cell cell85 = row.createCell(4);
		//
//						if (record1.getR39_no_of_accts() != null) {
//						    cell85.setCellValue(record1.getR39_no_of_accts().doubleValue());
//						    cell85.setCellStyle(numberStyle);
//						} else {
//						    cell85.setCellValue("");
//						    cell85.setCellStyle(textStyle);
//						}
		//
		//
//						 row = sheet.getRow(36) != null ? sheet.getRow(36) : sheet.createRow(36);
//						 Cell cell86 = row.createCell(2);
		//
//						if (record1.getR40_collateral_amount() != null) {
//						    cell86.setCellValue(record1.getR40_collateral_amount().doubleValue());
//						    cell86.setCellStyle(numberStyle);
//						} else {
//						    cell86.setCellValue("");
//						    cell86.setCellStyle(textStyle);
//						}
		//
//						 Cell cell87 = row.createCell(3);
		//
//						if (record1.getR40_carrying_amount() != null) {
//						    cell87.setCellValue(record1.getR40_carrying_amount().doubleValue());
//						    cell87.setCellStyle(numberStyle);
//						} else {
//						    cell87.setCellValue("");
//						    cell87.setCellStyle(textStyle);
//						}
		//
//						 Cell cell88 = row.createCell(4);
		//
//						if (record1.getR40_no_of_accts() != null) {
//						    cell88.setCellValue(record1.getR40_no_of_accts().doubleValue());
//						    cell88.setCellStyle(numberStyle);
//						} else {
//						    cell88.setCellValue("");
//						    cell88.setCellStyle(textStyle);
//						}
		//
		//
//						 row = sheet.getRow(37) != null ? sheet.getRow(37) : sheet.createRow(37);
//						 Cell cell89 = row.createCell(2);
		//
//						if (record1.getR41_collateral_amount() != null) {
//						    cell89.setCellValue(record1.getR41_collateral_amount().doubleValue());
//						    cell89.setCellStyle(numberStyle);
//						} else {
//						    cell89.setCellValue("");
//						    cell89.setCellStyle(textStyle);
//						}
		//
//						 Cell cell90 = row.createCell(3);
		//
//						if (record1.getR41_carrying_amount() != null) {
//						    cell90.setCellValue(record1.getR41_carrying_amount().doubleValue());
//						    cell90.setCellStyle(numberStyle);
//						} else {
//						    cell90.setCellValue("");
//						    cell90.setCellStyle(textStyle);
//						}
		//
//						 Cell cell91 = row.createCell(4);
		//
//						if (record1.getR41_no_of_accts() != null) {
//						    cell91.setCellValue(record1.getR41_no_of_accts().doubleValue());
//						    cell91.setCellStyle(numberStyle);
//						} else {
//						    cell91.setCellValue("");
//						    cell91.setCellStyle(textStyle);
//						}
		//
		//
//						 row = sheet.getRow(38) != null ? sheet.getRow(38) : sheet.createRow(38);
//						 Cell cell92 = row.createCell(2);
		//
//						if (record1.getR42_collateral_amount() != null) {
//						    cell92.setCellValue(record1.getR42_collateral_amount().doubleValue());
//						    cell92.setCellStyle(numberStyle);
//						} else {
//						    cell92.setCellValue("");
//						    cell92.setCellStyle(textStyle);
//						}
		//
//						 Cell cell93 = row.createCell(3);
		//
//						if (record1.getR42_carrying_amount() != null) {
//						    cell93.setCellValue(record1.getR42_carrying_amount().doubleValue());
//						    cell93.setCellStyle(numberStyle);
//						} else {
//						    cell93.setCellValue("");
//						    cell93.setCellStyle(textStyle);
//						}
		//
//						 Cell cell94 = row.createCell(4);
		//
//						if (record1.getR42_no_of_accts() != null) {
//						    cell94.setCellValue(record1.getR42_no_of_accts().doubleValue());
//						    cell94.setCellStyle(numberStyle);
//						} else {
//						    cell94.setCellValue("");
//						    cell94.setCellStyle(textStyle);
//						}


						 row = sheet.getRow(39) != null ? sheet.getRow(39) : sheet.createRow(39);
						 Cell cell95 = row.createCell(2);

						if (record1.getR43_collateral_amount() != null) {
						    cell95.setCellValue(record1.getR43_collateral_amount().doubleValue());
						    cell95.setCellStyle(numberStyle);
						} else {
						    cell95.setCellValue("");
						    cell95.setCellStyle(textStyle);
						}

						 Cell cell96 = row.createCell(3);

						if (record1.getR43_carrying_amount() != null) {
						    cell96.setCellValue(record1.getR43_carrying_amount().doubleValue());
						    cell96.setCellStyle(numberStyle);
						} else {
						    cell96.setCellValue("");
						    cell96.setCellStyle(textStyle);
						}

						 Cell cell97 = row.createCell(5);

						if (record1.getR43_no_of_accts() != null) {
						    cell97.setCellValue(record1.getR43_no_of_accts().doubleValue());
						    cell97.setCellStyle(numberStyle);
						} else {
						    cell97.setCellValue("");
						    cell97.setCellStyle(textStyle);
						}


						 row = sheet.getRow(40) != null ? sheet.getRow(40) : sheet.createRow(40);
						 Cell cell98 = row.createCell(2);

						if (record1.getR44_collateral_amount() != null) {
						    cell98.setCellValue(record1.getR44_collateral_amount().doubleValue());
						    cell98.setCellStyle(numberStyle);
						} else {
						    cell98.setCellValue("");
						    cell98.setCellStyle(textStyle);
						}

						 Cell cell99 = row.createCell(3);

						if (record1.getR44_carrying_amount() != null) {
						    cell99.setCellValue(record1.getR44_carrying_amount().doubleValue());
						    cell99.setCellStyle(numberStyle);
						} else {
						    cell99.setCellValue("");
						    cell99.setCellStyle(textStyle);
						}

						 Cell cell100 = row.createCell(5);

						if (record1.getR44_no_of_accts() != null) {
						    cell100.setCellValue(record1.getR44_no_of_accts().doubleValue());
						    cell100.setCellStyle(numberStyle);
						} else {
						    cell100.setCellValue("");
						    cell100.setCellStyle(textStyle);
						}


						 row = sheet.getRow(41) != null ? sheet.getRow(41) : sheet.createRow(41);
						 Cell cell101 = row.createCell(2);

						if (record1.getR45_collateral_amount() != null) {
						    cell101.setCellValue(record1.getR45_collateral_amount().doubleValue());
						    cell101.setCellStyle(numberStyle);
						} else {
						    cell101.setCellValue("");
						    cell101.setCellStyle(textStyle);
						}

						 Cell cell102 = row.createCell(3);

						if (record1.getR45_carrying_amount() != null) {
						    cell102.setCellValue(record1.getR45_carrying_amount().doubleValue());
						    cell102.setCellStyle(numberStyle);
						} else {
						    cell102.setCellValue("");
						    cell102.setCellStyle(textStyle);
						}

						 Cell cell103 = row.createCell(5);

						if (record1.getR45_no_of_accts() != null) {
						    cell103.setCellValue(record1.getR45_no_of_accts().doubleValue());
						    cell103.setCellStyle(numberStyle);
						} else {
						    cell103.setCellValue("");
						    cell103.setCellStyle(textStyle);
						}

						 row = sheet.getRow(43) != null ? sheet.getRow(43) : sheet.createRow(43);
						 Cell cell104 = row.createCell(2);

						if (record1.getR47_collateral_amount() != null) {
						    cell104.setCellValue(record1.getR47_collateral_amount().doubleValue());
						    cell104.setCellStyle(numberStyle);
						} else {
						    cell104.setCellValue("");
						    cell104.setCellStyle(textStyle);
						}

						 Cell cell105 = row.createCell(3);

						if (record1.getR47_carrying_amount() != null) {
						    cell105.setCellValue(record1.getR47_carrying_amount().doubleValue());
						    cell105.setCellStyle(numberStyle);
						} else {
						    cell105.setCellValue("");
						    cell105.setCellStyle(textStyle);
						}

						 Cell cell106 = row.createCell(5);

						if (record1.getR47_no_of_accts() != null) {
						    cell106.setCellValue(record1.getR47_no_of_accts().doubleValue());
						    cell106.setCellStyle(numberStyle);
						} else {
						    cell106.setCellValue("");
						    cell106.setCellStyle(textStyle);
						}


						 row = sheet.getRow(44) != null ? sheet.getRow(44) : sheet.createRow(44);
						 Cell cell107 = row.createCell(2);

						if (record1.getR48_collateral_amount() != null) {
						    cell107.setCellValue(record1.getR48_collateral_amount().doubleValue());
						    cell107.setCellStyle(numberStyle);
						} else {
						    cell107.setCellValue("");
						    cell107.setCellStyle(textStyle);
						}

						 Cell cell108 = row.createCell(3);

						if (record1.getR48_carrying_amount() != null) {
						    cell108.setCellValue(record1.getR48_carrying_amount().doubleValue());
						    cell108.setCellStyle(numberStyle);
						} else {
						    cell108.setCellValue("");
						    cell108.setCellStyle(textStyle);
						}

						 Cell cell109 = row.createCell(5);

						if (record1.getR48_no_of_accts() != null) {
						    cell109.setCellValue(record1.getR48_no_of_accts().doubleValue());
						    cell109.setCellStyle(numberStyle);
						} else {
						    cell109.setCellValue("");
						    cell109.setCellStyle(textStyle);
						}


						 row = sheet.getRow(45) != null ? sheet.getRow(45) : sheet.createRow(45);
						 Cell cell110 = row.createCell(2);

						if (record1.getR49_collateral_amount() != null) {
						    cell110.setCellValue(record1.getR49_collateral_amount().doubleValue());
						    cell110.setCellStyle(numberStyle);
						} else {
						    cell110.setCellValue("");
						    cell110.setCellStyle(textStyle);
						}

						 Cell cell111 = row.createCell(3);

						if (record1.getR49_carrying_amount() != null) {
						    cell111.setCellValue(record1.getR49_carrying_amount().doubleValue());
						    cell111.setCellStyle(numberStyle);
						} else {
						    cell111.setCellValue("");
						    cell111.setCellStyle(textStyle);
						}

						 Cell cell112 = row.createCell(5);

						if (record1.getR49_no_of_accts() != null) {
						    cell112.setCellValue(record1.getR49_no_of_accts().doubleValue());
						    cell112.setCellStyle(numberStyle);
						} else {
						    cell112.setCellValue("");
						    cell112.setCellStyle(textStyle);
						}


						 row = sheet.getRow(46) != null ? sheet.getRow(46) : sheet.createRow(46);
						 Cell cell113 = row.createCell(2);

						if (record1.getR50_collateral_amount() != null) {
						    cell113.setCellValue(record1.getR50_collateral_amount().doubleValue());
						    cell113.setCellStyle(numberStyle);
						} else {
						    cell113.setCellValue("");
						    cell113.setCellStyle(textStyle);
						}

						 Cell cell114 = row.createCell(3);

						if (record1.getR50_carrying_amount() != null) {
						    cell114.setCellValue(record1.getR50_carrying_amount().doubleValue());
						    cell114.setCellStyle(numberStyle);
						} else {
						    cell114.setCellValue("");
						    cell114.setCellStyle(textStyle);
						}

						 Cell cell115 = row.createCell(5);

						if (record1.getR50_no_of_accts() != null) {
						    cell115.setCellValue(record1.getR50_no_of_accts().doubleValue());
						    cell115.setCellStyle(numberStyle);
						} else {
						    cell115.setCellValue("");
						    cell115.setCellStyle(textStyle);
						}


						 row = sheet.getRow(47) != null ? sheet.getRow(47) : sheet.createRow(47);
						 Cell cell116 = row.createCell(2);

						if (record1.getR52_collateral_amount() != null) {
						    cell116.setCellValue(record1.getR52_collateral_amount().doubleValue());
						    cell116.setCellStyle(numberStyle);
						} else {
						    cell116.setCellValue("");
						    cell116.setCellStyle(textStyle);
						}

						 Cell cell117 = row.createCell(3);

						if (record1.getR52_carrying_amount() != null) {
						    cell117.setCellValue(record1.getR52_carrying_amount().doubleValue());
						    cell117.setCellStyle(numberStyle);
						} else {
						    cell117.setCellValue("");
						    cell117.setCellStyle(textStyle);
						}

						 Cell cell118 = row.createCell(5);

						if (record1.getR52_no_of_accts() != null) {
						    cell118.setCellValue(record1.getR52_no_of_accts().doubleValue());
						    cell118.setCellStyle(numberStyle);
						} else {
						    cell118.setCellValue("");
						    cell118.setCellStyle(textStyle);
						}

						 row = sheet.getRow(49) != null ? sheet.getRow(49) : sheet.createRow(49);
						 Cell cell119 = row.createCell(2);

						if (record1.getR53_collateral_amount() != null) {
						    cell119.setCellValue(record1.getR53_collateral_amount().doubleValue());
						    cell119.setCellStyle(numberStyle);
						} else {
						    cell119.setCellValue("");
						    cell119.setCellStyle(textStyle);
						}

						 Cell cell120 = row.createCell(3);

						if (record1.getR53_carrying_amount() != null) {
						    cell120.setCellValue(record1.getR53_carrying_amount().doubleValue());
						    cell120.setCellStyle(numberStyle);
						} else {
						    cell120.setCellValue("");
						    cell120.setCellStyle(textStyle);
						}

						 Cell cell121 = row.createCell(5);

						if (record1.getR53_no_of_accts() != null) {
						    cell121.setCellValue(record1.getR53_no_of_accts().doubleValue());
						    cell121.setCellStyle(numberStyle);
						} else {
						    cell121.setCellValue("");
						    cell121.setCellStyle(textStyle);
						}


						 row = sheet.getRow(50) != null ? sheet.getRow(50) : sheet.createRow(50);
						 Cell cell122 = row.createCell(2);

						if (record1.getR54_collateral_amount() != null) {
						    cell122.setCellValue(record1.getR54_collateral_amount().doubleValue());
						    cell122.setCellStyle(numberStyle);
						} else {
						    cell122.setCellValue("");
						    cell122.setCellStyle(textStyle);
						}

						 Cell cell123 = row.createCell(3);

						if (record1.getR54_carrying_amount() != null) {
						    cell123.setCellValue(record1.getR54_carrying_amount().doubleValue());
						    cell123.setCellStyle(numberStyle);
						} else {
						    cell123.setCellValue("");
						    cell123.setCellStyle(textStyle);
						}

						 Cell cell124 = row.createCell(5);

						if (record1.getR54_no_of_accts() != null) {
						    cell124.setCellValue(record1.getR54_no_of_accts().doubleValue());
						    cell124.setCellStyle(numberStyle);
						} else {
						    cell124.setCellValue("");
						    cell124.setCellStyle(textStyle);
						}


						 row = sheet.getRow(51) != null ? sheet.getRow(51) : sheet.createRow(51);
						 Cell cell125 = row.createCell(2);

						if (record1.getR55_collateral_amount() != null) {
						    cell125.setCellValue(record1.getR55_collateral_amount().doubleValue());
						    cell125.setCellStyle(numberStyle);
						} else {
						    cell125.setCellValue("");
						    cell125.setCellStyle(textStyle);
						}

						 Cell cell126 = row.createCell(3);

						if (record1.getR55_carrying_amount() != null) {
						    cell126.setCellValue(record1.getR55_carrying_amount().doubleValue());
						    cell126.setCellStyle(numberStyle);
						} else {
						    cell126.setCellValue("");
						    cell126.setCellStyle(textStyle);
						}

						 Cell cell127 = row.createCell(5);

						if (record1.getR55_no_of_accts() != null) {
						    cell127.setCellValue(record1.getR55_no_of_accts().doubleValue());
						    cell127.setCellStyle(numberStyle);
						} else {
						    cell127.setCellValue("");
						    cell127.setCellStyle(textStyle);
						}


						 row = sheet.getRow(52) != null ? sheet.getRow(52) : sheet.createRow(52);
						 Cell cell128 = row.createCell(2);

						if (record1.getR56_collateral_amount() != null) {
						    cell128.setCellValue(record1.getR56_collateral_amount().doubleValue());
						    cell128.setCellStyle(numberStyle);
						} else {
						    cell128.setCellValue("");
						    cell128.setCellStyle(textStyle);
						}

						 Cell cell129 = row.createCell(3);

						if (record1.getR56_carrying_amount() != null) {
						    cell129.setCellValue(record1.getR56_carrying_amount().doubleValue());
						    cell129.setCellStyle(numberStyle);
						} else {
						    cell129.setCellValue("");
						    cell129.setCellStyle(textStyle);
						}

						 Cell cell130 = row.createCell(5);

						if (record1.getR56_no_of_accts() != null) {
						    cell130.setCellValue(record1.getR56_no_of_accts().doubleValue());
						    cell130.setCellStyle(numberStyle);
						} else {
						    cell130.setCellValue("");
						    cell130.setCellStyle(textStyle);
						}


						 row = sheet.getRow(53) != null ? sheet.getRow(53) : sheet.createRow(53);
						 Cell cell131 = row.createCell(2);

						if (record1.getR58_collateral_amount() != null) {
						    cell131.setCellValue(record1.getR58_collateral_amount().doubleValue());
						    cell131.setCellStyle(numberStyle);
						} else {
						    cell131.setCellValue("");
						    cell131.setCellStyle(textStyle);
						}

						 Cell cell132 = row.createCell(3);

						if (record1.getR58_carrying_amount() != null) {
						    cell132.setCellValue(record1.getR58_carrying_amount().doubleValue());
						    cell132.setCellStyle(numberStyle);
						} else {
						    cell132.setCellValue("");
						    cell132.setCellStyle(textStyle);
						}

						 Cell cell133 = row.createCell(5);

						if (record1.getR58_no_of_accts() != null) {
						    cell133.setCellValue(record1.getR58_no_of_accts().doubleValue());
						    cell133.setCellStyle(numberStyle);
						} else {
						    cell133.setCellValue("");
						    cell133.setCellStyle(textStyle);
						}


						 row = sheet.getRow(54) != null ? sheet.getRow(54) : sheet.createRow(54);
						 Cell cell134 = row.createCell(2);

						if (record1.getR59_collateral_amount() != null) {
						    cell134.setCellValue(record1.getR59_collateral_amount().doubleValue());
						    cell134.setCellStyle(numberStyle);
						} else {
						    cell134.setCellValue("");
						    cell134.setCellStyle(textStyle);
						}

						 Cell cell135 = row.createCell(3);

						if (record1.getR59_carrying_amount() != null) {
						    cell135.setCellValue(record1.getR59_carrying_amount().doubleValue());
						    cell135.setCellStyle(numberStyle);
						} else {
						    cell135.setCellValue("");
						    cell135.setCellStyle(textStyle);
						}

						 Cell cell136 = row.createCell(5);

						if (record1.getR59_no_of_accts() != null) {
						    cell136.setCellValue(record1.getR59_no_of_accts().doubleValue());
						    cell136.setCellStyle(numberStyle);
						} else {
						    cell136.setCellValue("");
						    cell136.setCellStyle(textStyle);
						}


						 row = sheet.getRow(55) != null ? sheet.getRow(55) : sheet.createRow(55);
						 Cell cell137 = row.createCell(2);

						if (record1.getR60_collateral_amount() != null) {
						    cell137.setCellValue(record1.getR60_collateral_amount().doubleValue());
						    cell137.setCellStyle(numberStyle);
						} else {
						    cell137.setCellValue("");
						    cell137.setCellStyle(textStyle);
						}

						 Cell cell138 = row.createCell(3);

						if (record1.getR60_carrying_amount() != null) {
						    cell138.setCellValue(record1.getR60_carrying_amount().doubleValue());
						    cell138.setCellStyle(numberStyle);
						} else {
						    cell138.setCellValue("");
						    cell138.setCellStyle(textStyle);
						}

						 Cell cell139 = row.createCell(5);

						if (record1.getR60_no_of_accts() != null) {
						    cell139.setCellValue(record1.getR60_no_of_accts().doubleValue());
						    cell139.setCellStyle(numberStyle);
						} else {
						    cell139.setCellValue("");
						    cell139.setCellStyle(textStyle);
						}


						 row = sheet.getRow(56) != null ? sheet.getRow(56) : sheet.createRow(56);
						 Cell cell140 = row.createCell(2);

						if (record1.getR61_collateral_amount() != null) {
						    cell140.setCellValue(record1.getR61_collateral_amount().doubleValue());
						    cell140.setCellStyle(numberStyle);
						} else {
						    cell140.setCellValue("");
						    cell140.setCellStyle(textStyle);
						}

						 Cell cell141 = row.createCell(3);

						if (record1.getR61_carrying_amount() != null) {
						    cell141.setCellValue(record1.getR61_carrying_amount().doubleValue());
						    cell141.setCellStyle(numberStyle);
						} else {
						    cell141.setCellValue("");
						    cell141.setCellStyle(textStyle);
						}

						 Cell cell142 = row.createCell(5);

						if (record1.getR61_no_of_accts() != null) {
						    cell142.setCellValue(record1.getR61_no_of_accts().doubleValue());
						    cell142.setCellStyle(numberStyle);
						} else {
						    cell142.setCellValue("");
						    cell142.setCellStyle(textStyle);
						}


						 row = sheet.getRow(57) != null ? sheet.getRow(57) : sheet.createRow(57);
						 Cell cell143 = row.createCell(2);

						if (record1.getR62_collateral_amount() != null) {
						    cell143.setCellValue(record1.getR62_collateral_amount().doubleValue());
						    cell143.setCellStyle(numberStyle);
						} else {
						    cell143.setCellValue("");
						    cell143.setCellStyle(textStyle);
						}

						 Cell cell144 = row.createCell(3);

						if (record1.getR62_carrying_amount() != null) {
						    cell144.setCellValue(record1.getR62_carrying_amount().doubleValue());
						    cell144.setCellStyle(numberStyle);
						} else {
						    cell144.setCellValue("");
						    cell144.setCellStyle(textStyle);
						}

						 Cell cell145 = row.createCell(5);

						if (record1.getR62_no_of_accts() != null) {
						    cell145.setCellValue(record1.getR62_no_of_accts().doubleValue());
						    cell145.setCellStyle(numberStyle);
						} else {
						    cell145.setCellValue("");
						    cell145.setCellStyle(textStyle);
						}


						 row = sheet.getRow(58) != null ? sheet.getRow(58) : sheet.createRow(58);
						 Cell cell146 = row.createCell(2);

						if (record1.getR63_collateral_amount() != null) {
						    cell146.setCellValue(record1.getR63_collateral_amount().doubleValue());
						    cell146.setCellStyle(numberStyle);
						} else {
						    cell146.setCellValue("");
						    cell146.setCellStyle(textStyle);
						}

						 Cell cell147 = row.createCell(3);

						if (record1.getR63_carrying_amount() != null) {
						    cell147.setCellValue(record1.getR63_carrying_amount().doubleValue());
						    cell147.setCellStyle(numberStyle);
						} else {
						    cell147.setCellValue("");
						    cell147.setCellStyle(textStyle);
						}

						 Cell cell148 = row.createCell(5);

						if (record1.getR63_no_of_accts() != null) {
						    cell148.setCellValue(record1.getR63_no_of_accts().doubleValue());
						    cell148.setCellStyle(numberStyle);
						} else {
						    cell148.setCellValue("");
						    cell148.setCellStyle(textStyle);
						}






						workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();

						// Write the final workbook content to the in-memory stream.
						workbook.write(out);

						logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

						return out.toByteArray();
						
			}

			int startRow =  10;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					Q_RLFA1_Archival_Summary_Entity  record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					
					// row11
					row = sheet.getRow(10);

					// Column 2 -B

					Cell cellB = row.createCell(1);
					if (record.getR11_collateral_amount() != null) {
						cellB.setCellValue(record.getR11_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					Cell cellC = row.createCell(2);
					if (record.getR11_carrying_amount() != null) {
						cellC.setCellValue(record.getR11_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}
					
					// Column 4-D
					Cell cellD = row.createCell(3);
					if (record.getR11_no_of_accts() != null) {
						cellD.setCellValue(record.getR11_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}
					
					// row12
					row = sheet.getRow(11);

					// Column 2 -B

					 cellB = row.createCell(1);
					if (record.getR12_collateral_amount() != null) {
						cellB.setCellValue(record.getR12_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					 cellC = row.createCell(2);
					if (record.getR12_carrying_amount() != null) {
						cellC.setCellValue(record.getR12_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}
					
					// Column 4-D
				 cellD = row.createCell(3);
					if (record.getR12_no_of_accts() != null) {
						cellD.setCellValue(record.getR12_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}
					
					
					// row13
					row = sheet.getRow(12);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR13_collateral_amount() != null) {
					    cellB.setCellValue(record.getR13_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR13_carrying_amount() != null) {
					    cellC.setCellValue(record.getR13_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR13_no_of_accts() != null) {
					    cellD.setCellValue(record.getR13_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row15
					row = sheet.getRow(14);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR15_collateral_amount() != null) {
					    cellB.setCellValue(record.getR15_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR15_carrying_amount() != null) {
					    cellC.setCellValue(record.getR15_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR15_no_of_accts() != null) {
					    cellD.setCellValue(record.getR15_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}


					// row16
					row = sheet.getRow(15);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR16_collateral_amount() != null) {
					    cellB.setCellValue(record.getR16_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR16_carrying_amount() != null) {
					    cellC.setCellValue(record.getR16_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR16_no_of_accts() != null) {
					    cellD.setCellValue(record.getR16_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR17_collateral_amount() != null) {
					    cellB.setCellValue(record.getR17_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR17_carrying_amount() != null) {
					    cellC.setCellValue(record.getR17_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR17_no_of_accts() != null) {
					    cellD.setCellValue(record.getR17_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row18
					row = sheet.getRow(17);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR18_collateral_amount() != null) {
					    cellB.setCellValue(record.getR18_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR18_carrying_amount() != null) {
					    cellC.setCellValue(record.getR18_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR18_no_of_accts() != null) {
					    cellD.setCellValue(record.getR18_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR19_collateral_amount() != null) {
					    cellB.setCellValue(record.getR19_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR19_carrying_amount() != null) {
					    cellC.setCellValue(record.getR19_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR19_no_of_accts() != null) {
					    cellD.setCellValue(record.getR19_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR20_collateral_amount() != null) {
					    cellB.setCellValue(record.getR20_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR20_carrying_amount() != null) {
					    cellC.setCellValue(record.getR20_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR20_no_of_accts() != null) {
					    cellD.setCellValue(record.getR20_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR21_collateral_amount() != null) {
					    cellB.setCellValue(record.getR21_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR21_carrying_amount() != null) {
					    cellC.setCellValue(record.getR21_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR21_no_of_accts() != null) {
					    cellD.setCellValue(record.getR21_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR22_collateral_amount() != null) {
					    cellB.setCellValue(record.getR22_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR22_carrying_amount() != null) {
					    cellC.setCellValue(record.getR22_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR22_no_of_accts() != null) {
					    cellD.setCellValue(record.getR22_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR23_collateral_amount() != null) {
					    cellB.setCellValue(record.getR23_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR23_carrying_amount() != null) {
					    cellC.setCellValue(record.getR23_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR23_no_of_accts() != null) {
					    cellD.setCellValue(record.getR23_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR24_collateral_amount() != null) {
					    cellB.setCellValue(record.getR24_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR24_carrying_amount() != null) {
					    cellC.setCellValue(record.getR24_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR24_no_of_accts() != null) {
					    cellD.setCellValue(record.getR24_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR25_collateral_amount() != null) {
					    cellB.setCellValue(record.getR25_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR25_carrying_amount() != null) {
					    cellC.setCellValue(record.getR25_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR25_no_of_accts() != null) {
					    cellD.setCellValue(record.getR25_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR26_collateral_amount() != null) {
					    cellB.setCellValue(record.getR26_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR26_carrying_amount() != null) {
					    cellC.setCellValue(record.getR26_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR26_no_of_accts() != null) {
					    cellD.setCellValue(record.getR26_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR27_collateral_amount() != null) {
					    cellB.setCellValue(record.getR27_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR27_carrying_amount() != null) {
					    cellC.setCellValue(record.getR27_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR27_no_of_accts() != null) {
					    cellD.setCellValue(record.getR27_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					
					// row29
					row = sheet.getRow(28);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR29_collateral_amount() != null) {
					    cellB.setCellValue(record.getR29_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR29_carrying_amount() != null) {
					    cellC.setCellValue(record.getR29_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR29_no_of_accts() != null) {
					    cellD.setCellValue(record.getR29_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR30_collateral_amount() != null) {
					    cellB.setCellValue(record.getR30_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR30_carrying_amount() != null) {
					    cellC.setCellValue(record.getR30_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR30_no_of_accts() != null) {
					    cellD.setCellValue(record.getR30_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR31_collateral_amount() != null) {
					    cellB.setCellValue(record.getR31_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR31_carrying_amount() != null) {
					    cellC.setCellValue(record.getR31_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR31_no_of_accts() != null) {
					    cellD.setCellValue(record.getR31_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR32_collateral_amount() != null) {
					    cellB.setCellValue(record.getR32_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR32_carrying_amount() != null) {
					    cellC.setCellValue(record.getR32_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR32_no_of_accts() != null) {
					    cellD.setCellValue(record.getR32_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR33_collateral_amount() != null) {
					    cellB.setCellValue(record.getR33_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR33_carrying_amount() != null) {
					    cellC.setCellValue(record.getR33_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR33_no_of_accts() != null) {
					    cellD.setCellValue(record.getR33_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR34_collateral_amount() != null) {
					    cellB.setCellValue(record.getR34_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR34_carrying_amount() != null) {
					    cellC.setCellValue(record.getR34_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR34_no_of_accts() != null) {
					    cellD.setCellValue(record.getR34_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR35_collateral_amount() != null) {
					    cellB.setCellValue(record.getR35_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR35_carrying_amount() != null) {
					    cellC.setCellValue(record.getR35_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR35_no_of_accts() != null) {
					    cellD.setCellValue(record.getR35_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR36_collateral_amount() != null) {
					    cellB.setCellValue(record.getR36_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR36_carrying_amount() != null) {
					    cellC.setCellValue(record.getR36_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR36_no_of_accts() != null) {
					    cellD.setCellValue(record.getR36_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row38
					row = sheet.getRow(37);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR38_collateral_amount() != null) {
					    cellB.setCellValue(record.getR38_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR38_carrying_amount() != null) {
					    cellC.setCellValue(record.getR38_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR38_no_of_accts() != null) {
					    cellD.setCellValue(record.getR38_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(38);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR39_collateral_amount() != null) {
					    cellB.setCellValue(record.getR39_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR39_carrying_amount() != null) {
					    cellC.setCellValue(record.getR39_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR39_no_of_accts() != null) {
					    cellD.setCellValue(record.getR39_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row41
					row = sheet.getRow(40);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR41_collateral_amount() != null) {
					    cellB.setCellValue(record.getR41_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR41_carrying_amount() != null) {
					    cellC.setCellValue(record.getR41_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR41_no_of_accts() != null) {
					    cellD.setCellValue(record.getR41_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row42
					row = sheet.getRow(41);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR42_collateral_amount() != null) {
					    cellB.setCellValue(record.getR42_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR42_carrying_amount() != null) {
					    cellC.setCellValue(record.getR42_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR42_no_of_accts() != null) {
					    cellD.setCellValue(record.getR42_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					
					// row44
					row = sheet.getRow(43);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR44_collateral_amount() != null) {
					    cellB.setCellValue(record.getR44_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR44_carrying_amount() != null) {
					    cellC.setCellValue(record.getR44_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR44_no_of_accts() != null) {
					    cellD.setCellValue(record.getR44_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row45
					row = sheet.getRow(44);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR45_collateral_amount() != null) {
					    cellB.setCellValue(record.getR45_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR45_carrying_amount() != null) {
					    cellC.setCellValue(record.getR45_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR45_no_of_accts() != null) {
					    cellD.setCellValue(record.getR45_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row46
					row = sheet.getRow(45);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR46_collateral_amount() != null) {
					    cellB.setCellValue(record.getR46_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR46_carrying_amount() != null) {
					    cellC.setCellValue(record.getR46_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR46_no_of_accts() != null) {
					    cellD.setCellValue(record.getR46_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row47
					row = sheet.getRow(46);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR47_collateral_amount() != null) {
					    cellB.setCellValue(record.getR47_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR47_carrying_amount() != null) {
					    cellC.setCellValue(record.getR47_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR47_no_of_accts() != null) {
					    cellD.setCellValue(record.getR47_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row49
					row = sheet.getRow(48);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR49_collateral_amount() != null) {
					    cellB.setCellValue(record.getR49_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR49_carrying_amount() != null) {
					    cellC.setCellValue(record.getR49_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR49_no_of_accts() != null) {
					    cellD.setCellValue(record.getR49_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row50
					row = sheet.getRow(49);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR50_collateral_amount() != null) {
					    cellB.setCellValue(record.getR50_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR50_carrying_amount() != null) {
					    cellC.setCellValue(record.getR50_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR50_no_of_accts() != null) {
					    cellD.setCellValue(record.getR50_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(50);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR51_collateral_amount() != null) {
					    cellB.setCellValue(record.getR51_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR51_carrying_amount() != null) {
					    cellC.setCellValue(record.getR51_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR51_no_of_accts() != null) {
					    cellD.setCellValue(record.getR51_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					
					// row53
					row = sheet.getRow(52);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR53_collateral_amount() != null) {
					    cellB.setCellValue(record.getR53_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR53_carrying_amount() != null) {
					    cellC.setCellValue(record.getR53_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR53_no_of_accts() != null) {
					    cellD.setCellValue(record.getR53_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR54_collateral_amount() != null) {
					    cellB.setCellValue(record.getR54_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR54_carrying_amount() != null) {
					    cellC.setCellValue(record.getR54_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR54_no_of_accts() != null) {
					    cellD.setCellValue(record.getR54_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(54);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR55_collateral_amount() != null) {
					    cellB.setCellValue(record.getR55_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR55_carrying_amount() != null) {
					    cellC.setCellValue(record.getR55_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR55_no_of_accts() != null) {
					    cellD.setCellValue(record.getR55_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}


					
					// row57
					row = sheet.getRow(56);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR57_collateral_amount() != null) {
					    cellB.setCellValue(record.getR57_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR57_carrying_amount() != null) {
					    cellC.setCellValue(record.getR57_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR57_no_of_accts() != null) {
					    cellD.setCellValue(record.getR57_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row58
					row = sheet.getRow(57);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR58_collateral_amount() != null) {
					    cellB.setCellValue(record.getR58_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR58_carrying_amount() != null) {
					    cellC.setCellValue(record.getR58_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR58_no_of_accts() != null) {
					    cellD.setCellValue(record.getR58_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row59
					row = sheet.getRow(58);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR59_collateral_amount() != null) {
					    cellB.setCellValue(record.getR59_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR59_carrying_amount() != null) {
					    cellC.setCellValue(record.getR59_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR59_no_of_accts() != null) {
					    cellD.setCellValue(record.getR59_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row60
					row = sheet.getRow(59);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR60_collateral_amount() != null) {
					    cellB.setCellValue(record.getR60_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR60_carrying_amount() != null) {
					    cellC.setCellValue(record.getR60_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR60_no_of_accts() != null) {
					    cellD.setCellValue(record.getR60_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row61
					row = sheet.getRow(60);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR61_collateral_amount() != null) {
					    cellB.setCellValue(record.getR61_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR61_carrying_amount() != null) {
					    cellC.setCellValue(record.getR61_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR61_no_of_accts() != null) {
					    cellD.setCellValue(record.getR61_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row62
					row = sheet.getRow(61);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR62_collateral_amount() != null) {
					    cellB.setCellValue(record.getR62_collateral_amount().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR62_carrying_amount() != null) {
					    cellC.setCellValue(record.getR62_carrying_amount().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR62_no_of_accts() != null) {
					    cellD.setCellValue(record.getR62_no_of_accts().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
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
