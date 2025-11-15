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

import com.bornfire.brrs.entities.BRRS_Q_RLFA1_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Q_RLFA1_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_Q_RLFA1_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Q_RLFA1_Summary_Repo;
import com.bornfire.brrs.entities.M_EPR_Summary_Entity;
import com.bornfire.brrs.entities.M_LIQ_Archival_Summary_Entity;
import com.bornfire.brrs.entities.Q_RLFA1_Archival_Summary_Entity;
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
	BRRS_Q_RLFA1_Archival_Detail_Repo q_rlfa1_Archival_Detail_Repo;

	@Autowired
	BRRS_Q_RLFA1_Archival_Summary_Repo q_rlfa1_Archival_Summary_Repo;

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
			  String version) {
			  
			  ModelAndView mv = new ModelAndView(); Session hs =
			  sessionFactory.getCurrentSession();
			  
			  int pageSize = pageable.getPageSize(); int currentPage =
			  pageable.getPageNumber(); int startItem = currentPage * pageSize;
			  
			  try { Date d1 = dateformat.parse(todate);
			  
			  // ---------- CASE 1: ARCHIVAL ---------- 
			  if
			  ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			  List<Q_RLFA1_Archival_Summary_Entity> T1Master =
					  q_rlfa1_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
			  
			  mv.addObject("reportsummary", T1Master); }
			  
			  // ---------- CASE 2: RESUB ---------- 
			  else if
			  ("RESUB".equalsIgnoreCase(type) && version != null) {
			  List<Q_RLFA1_Archival_Summary_Entity> T1Master =
					  q_rlfa1_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
			  
			  mv.addObject("reportsummary", T1Master); }
			  
			  // ---------- CASE 3: NORMAL ---------- 
			  else { List<Q_RLFA1_Summary_Entity>
			  T1Master =
					  brrs_q_rlfa1_Summary_Repo.getdatabydateListWithVersion(todate);
			  System.out.println("T1Master Size "+T1Master.size());
			  mv.addObject("reportsummary", T1Master); }
			  
			  } catch (ParseException e) { e.printStackTrace(); }
			  
			  mv.setViewName("BRRS/Q_RLFA1"); mv.addObject("displaymode", "summary");
			  System.out.println("View set to: " + mv.getViewName()); return mv; }

	 
	


	public byte[] getQ_RLFA1Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelQ_RLFA1ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}
		
		
		else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
		    logger.info("Service: Generating RESUB report for version {}", version);

		    try {
		        // ✅ Use pattern matching "31-Jul-2025"
		        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
		        Date report_date = sdf.parse(fromdate);  // or use asondate if that's your date source

		        List<Q_RLFA1_Archival_Summary_Entity> T1Master =
		        		q_rlfa1_Archival_Summary_Repo.getdatabydateListarchival(report_date, version);

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
							entity.getReport_version() 
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
			String currency, String dtltype, String type, String version) throws Exception {

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

		// 3️⃣ Save updated entity
		brrs_q_rlfa1_Summary_Repo.save(existing);
	}
	
	
	public List<Object[]> getQ_RLFA1Resub() {
	    List<Object[]> resubList = new ArrayList<>();
	    try {
	        List<Q_RLFA1_Archival_Summary_Entity> latestArchivalList = 
	        		q_rlfa1_Archival_Summary_Repo.getdatabydateListWithVersionAll();

	        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
	            for (Q_RLFA1_Archival_Summary_Entity entity : latestArchivalList) {
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
	    int newVersion = 1;

	    try {
	        // ✅ use the same variable name as in repo method
	        Optional<Q_RLFA1_Archival_Summary_Entity> latestArchivalOpt =
	        		q_rlfa1_Archival_Summary_Repo.getLatestArchivalVersionByDate(report_date);

	        // Determine next version
	        if (latestArchivalOpt.isPresent()) {
	        	Q_RLFA1_Archival_Summary_Entity latestArchival = latestArchivalOpt.get();
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
	        boolean exists = q_rlfa1_Archival_Summary_Repo
	                .findByReport_dateAndReport_version(report_date, String.valueOf(newVersion))
	                .isPresent();

	        if (exists) {
	            throw new RuntimeException("Version " + newVersion + " already exists for report date " + report_date);
	        }

	        // Copy summary entity to archival entity
	        Q_RLFA1_Archival_Summary_Entity archivalEntity = new Q_RLFA1_Archival_Summary_Entity();
	        org.springframework.beans.BeanUtils.copyProperties(updatedEntity, archivalEntity);

	        archivalEntity.setReport_date(report_date);
	        archivalEntity.setReport_version(String.valueOf(newVersion));
	        archivalEntity.setReportResubDate(new Date());

	        System.out.println("Saving new archival version: " + newVersion);
	        q_rlfa1_Archival_Summary_Repo.save(archivalEntity);

	        System.out.println("Saved archival version successfully: " + newVersion);

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error while creating archival resubmission record", e);
	    }
	}
	


	/// Downloaded for Archival & Resub
	public byte[] BRRS_Q_RLFA1ResubExcel(String filename, String reportId, String fromdate,
        String todate, String currency, String dtltype,
        String type, String version) throws Exception {

    logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

    if (type.equals("RESUB") & version != null) {
       
    }

    List<Q_RLFA1_Archival_Summary_Entity> dataList =
    		q_rlfa1_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

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
