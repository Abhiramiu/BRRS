package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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

import com.bornfire.brrs.entities.BRRS_M_INT_RATES_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_INT_RATES_FCA_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_INT_RATES_FCA_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_INT_RATES_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12F_Archival_Summary_Repo;

import com.bornfire.brrs.entities.BRRS_M_SRWA_12F_Summary_Repo;
import com.bornfire.brrs.entities.M_INT_RATES_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_FCA_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_FCA_Summary_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_Summary_Entity;
import com.bornfire.brrs.entities.M_SECL_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SECL_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12F_Summary_Entity;

import java.math.BigDecimal;

@Component
@Service

public class BRRS_M_INT_RATES_FCA_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_INT_RATES_FCA_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_SRWA_12F_Summary_Repo M_SRWA_12F_Summary_Repo;

	@Autowired
	BRRS_M_SRWA_12F_Archival_Summary_Repo M_SRWA_12F_Archival_Summary_Repo;
	
	@Autowired
	BRRS_M_INT_RATES_Summary_Repo M_INT_RATES_Summary_Repo;

	@Autowired
	BRRS_M_INT_RATES_Archival_Summary_Repo M_INT_RATES_Archival_Summary_Repo;
	
	@Autowired
	BRRS_M_INT_RATES_FCA_Summary_Repo M_INT_RATES_FCA_Summary_Repo;

	@Autowired
	BRRS_M_INT_RATES_FCA_Archival_Summary_Repo M_INT_RATES_FCA_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_INTRATESFCAView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, String version) {
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		System.out.println("testing");
		System.out.println(version);

		if (type.equals("ARCHIVAL") & version != null) {
			System.out.println(type);
			List<M_INT_RATES_FCA_Archival_Summary_Entity> T1Master = new ArrayList<M_INT_RATES_FCA_Archival_Summary_Entity>();
			System.out.println(version);
			try {
				Date d1 = dateformat.parse(todate);

// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
// ", BRF1_REPORT_ENTITY.class)
// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = M_INT_RATES_FCA_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate),
						version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} else {
			List<M_INT_RATES_FCA_Summary_Entity> T1Master = new ArrayList<M_INT_RATES_FCA_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);

// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
// ", BRF1_REPORT_ENTITY.class)
// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = M_INT_RATES_FCA_Summary_Repo.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/M_INT_RATES_FCA");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}
	
	public List<Object> getM_INTRATESFCAArchival() {
		List<Object> M_INTRATESFCAArchivallist = new ArrayList<>();
//		List<Object> M_SRWA_12GArchivallist2 = new ArrayList<>();
//		List<Object> M_SRWA_12GArchivallist3 = new ArrayList<>();
		try {
			M_INTRATESFCAArchivallist = M_INT_RATES_FCA_Archival_Summary_Repo.getM_INTRATESFCAarchival();
			System.out.println("countser" + M_INTRATESFCAArchivallist.size());
//			System.out.println("countser" + M_SRWA_12GArchivallist.size());
//			System.out.println("countser" + M_SRWA_12GArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_SRWA_12F Archival data: " + e.getMessage());
			e.printStackTrace();
			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_INTRATESFCAArchivallist;
	}
	
	
	public void updateReport(M_INT_RATES_FCA_Summary_Entity updatedEntity) {
		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		M_INT_RATES_FCA_Summary_Entity existing = M_INT_RATES_FCA_Summary_Repo.findById(updatedEntity.getReport_date())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

		try {
			// 1️⃣ Loop through R14 to R100
			for (int i = 10; i <= 14; i++) {
				String prefix = "R" + i + "_";

				String[] fields = {"CURRENT",
				        "CALL",
				        "SAVINGS",
				        "NOTICE_0_31_DAYS",
				        "NOTICE_32_88_DAYS",
				        "91_DEPOSIT_DAY",
				        "FD_1_6_MONTHS",
				        "FD_7_12_MONTHS",
				        "FD_13_18_MONTHS",
				        "FD_19_24_MONTHS",
				        "FD_OVER_24_MONTHS"};

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_INT_RATES_FCA_Summary_Entity.class.getMethod(getterName);
						Method setter = M_INT_RATES_FCA_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

			// 2️⃣ Handle R100 total fields using same structure
			String prefix = "R15_";
			String[] totalFields = { "CURRENCY","CURRENT",
			        "CALL",
			        "SAVINGS",
			        "NOTICE_0_31_DAYS",
			        "NOTICE_32_88_DAYS",
			        "91_DEPOSIT_DAY",
			        "FD_1_6_MONTHS",
			        "FD_7_12_MONTHS",
			        "FD_13_18_MONTHS",
			        "FD_19_24_MONTHS",
			        "FD_OVER_24_MONTHS",
			        "TOTAL"};

			for (String field : totalFields) {
				String getterName = "get" + prefix + field;
				String setterName = "set" + prefix + field;

				try {
					Method getter = M_INT_RATES_FCA_Summary_Entity.class.getMethod(getterName);
					Method setter = M_INT_RATES_FCA_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

					Object newValue = getter.invoke(updatedEntity);
					setter.invoke(existing, newValue);

				} catch (NoSuchMethodException e) {
					// Skip missing total fields
					continue;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// Save updated entity
		System.out.println("abc");
		M_INT_RATES_FCA_Summary_Repo.save(existing);
	}
	
	
	public byte[] getM_INTRATESFCAExcel(String filename,String reportId, String fromdate, String todate, String currency, String dtltype , String type ,
			String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		
		// ARCHIVAL check
				if ("ARCHIVAL".equals(type) && version != null) {
					byte[] ARCHIVALreport = getExcelM_INTRATESFCAARCHIVAL(filename, reportId, fromdate, 
							todate, currency, dtltype, type,
							version);
					return ARCHIVALreport;
				}

		List<M_INT_RATES_FCA_Summary_Entity> dataList =M_INT_RATES_FCA_Summary_Repo.getdatabydateList(dateformat.parse(todate)) ;

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS report. Returning empty result.");
			return new byte[0];
		}
		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(templateDir);
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
			font.setFontHeightInPoints((short)8); // size 8
			font.setFontName("Arial");    

			CellStyle numberStyle = workbook.createCellStyle();
			//numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
			// --- End of Style Definitions ---
			
	          int startRow = 9;
			
			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_INT_RATES_FCA_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber="+startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					
					Cell cell1 = row.createCell(1);
					if (record.getR10_CURRENT() != null) {
						cell1.setCellValue(record.getR10_CURRENT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					Cell cell2 = row.createCell(2);
					if (record.getR10_CALL() != null) {
						cell2.setCellValue(record.getR10_CALL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					Cell cell3 = row.createCell(3);
					if (record.getR10_SAVINGS() != null) {
						cell3.setCellValue(record.getR10_SAVINGS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					Cell cell4 = row.createCell(4);
					if (record.getR10_NOTICE_0_31_DAYS() != null) {
						cell4.setCellValue(record.getR10_NOTICE_0_31_DAYS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					Cell cell5 = row.createCell(5);
					if (record.getR10_NOTICE_32_88_DAYS() != null) {
						cell5.setCellValue(record.getR10_NOTICE_32_88_DAYS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					Cell cell6 = row.createCell(6);
					if (record.getR10_91_DEPOSIT_DAY() != null) {
						cell6.setCellValue(record.getR10_91_DEPOSIT_DAY().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					Cell cell7 = row.createCell(7);
					if (record.getR10_FD_1_6_MONTHS() != null) {
						cell7.setCellValue(record.getR10_FD_1_6_MONTHS().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					Cell cell8 = row.createCell(8);
					if (record.getR10_FD_7_12_MONTHS() != null) {
						cell8.setCellValue(record.getR10_FD_7_12_MONTHS().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					Cell cell9 = row.createCell(9);
					if (record.getR10_FD_13_18_MONTHS() != null) {
						cell9.setCellValue(record.getR10_FD_13_18_MONTHS().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					Cell cell10 = row.createCell(10);
					if (record.getR10_FD_19_24_MONTHS() != null) {
						cell10.setCellValue(record.getR10_FD_19_24_MONTHS().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					Cell cell11 = row.createCell(11);
					if (record.getR10_FD_OVER_24_MONTHS() != null) {
						cell11.setCellValue(record.getR10_FD_OVER_24_MONTHS().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					

					// ---------- R11 ----------
					row = sheet.getRow(10);
					if (row == null) {
					    row = sheet.createRow(10);
					}

					cell1 = row.createCell(1);
					if (record.getR11_CURRENT() != null) {
					    cell1.setCellValue(record.getR11_CURRENT().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR11_CALL() != null) {
					    cell2.setCellValue(record.getR11_CALL().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR11_SAVINGS() != null) {
					    cell3.setCellValue(record.getR11_SAVINGS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR11_NOTICE_0_31_DAYS() != null) {
					    cell4.setCellValue(record.getR11_NOTICE_0_31_DAYS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR11_NOTICE_32_88_DAYS() != null) {
					    cell5.setCellValue(record.getR11_NOTICE_32_88_DAYS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR11_91_DEPOSIT_DAY() != null) {
					    cell6.setCellValue(record.getR11_91_DEPOSIT_DAY().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR11_FD_1_6_MONTHS() != null) {
					    cell7.setCellValue(record.getR11_FD_1_6_MONTHS().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR11_FD_7_12_MONTHS() != null) {
					    cell8.setCellValue(record.getR11_FD_7_12_MONTHS().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR11_FD_13_18_MONTHS() != null) {
					    cell9.setCellValue(record.getR11_FD_13_18_MONTHS().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(10);
					if (record.getR11_FD_19_24_MONTHS() != null) {
					    cell10.setCellValue(record.getR11_FD_19_24_MONTHS().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(11);
					if (record.getR11_FD_OVER_24_MONTHS() != null) {
					    cell11.setCellValue(record.getR11_FD_OVER_24_MONTHS().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// ---------- R12 ----------
					row = sheet.getRow(11);
					if (row == null) {
					    row = sheet.createRow(11);
					}

					cell1 = row.createCell(1);
					if (record.getR12_CURRENT() != null) {
					    cell1.setCellValue(record.getR12_CURRENT().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR12_CALL() != null) {
					    cell2.setCellValue(record.getR12_CALL().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR12_SAVINGS() != null) {
					    cell3.setCellValue(record.getR12_SAVINGS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR12_NOTICE_0_31_DAYS() != null) {
					    cell4.setCellValue(record.getR12_NOTICE_0_31_DAYS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR12_NOTICE_32_88_DAYS() != null) {
					    cell5.setCellValue(record.getR12_NOTICE_32_88_DAYS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR12_91_DEPOSIT_DAY() != null) {
					    cell6.setCellValue(record.getR12_91_DEPOSIT_DAY().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR12_FD_1_6_MONTHS() != null) {
					    cell7.setCellValue(record.getR12_FD_1_6_MONTHS().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR12_FD_7_12_MONTHS() != null) {
					    cell8.setCellValue(record.getR12_FD_7_12_MONTHS().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR12_FD_13_18_MONTHS() != null) {
					    cell9.setCellValue(record.getR12_FD_13_18_MONTHS().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(10);
					if (record.getR12_FD_19_24_MONTHS() != null) {
					    cell10.setCellValue(record.getR12_FD_19_24_MONTHS().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(11);
					if (record.getR12_FD_OVER_24_MONTHS() != null) {
					    cell11.setCellValue(record.getR12_FD_OVER_24_MONTHS().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// ---------- R13 ----------
					row = sheet.getRow(12);
					if (row == null) {
					    row = sheet.createRow(12);
					}

					cell1 = row.createCell(1);
					if (record.getR13_CURRENT() != null) {
					    cell1.setCellValue(record.getR13_CURRENT().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR13_CALL() != null) {
					    cell2.setCellValue(record.getR13_CALL().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR13_SAVINGS() != null) {
					    cell3.setCellValue(record.getR13_SAVINGS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR13_NOTICE_0_31_DAYS() != null) {
					    cell4.setCellValue(record.getR13_NOTICE_0_31_DAYS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR13_NOTICE_32_88_DAYS() != null) {
					    cell5.setCellValue(record.getR13_NOTICE_32_88_DAYS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR13_91_DEPOSIT_DAY() != null) {
					    cell6.setCellValue(record.getR13_91_DEPOSIT_DAY().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR13_FD_1_6_MONTHS() != null) {
					    cell7.setCellValue(record.getR13_FD_1_6_MONTHS().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR13_FD_7_12_MONTHS() != null) {
					    cell8.setCellValue(record.getR13_FD_7_12_MONTHS().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR13_FD_13_18_MONTHS() != null) {
					    cell9.setCellValue(record.getR13_FD_13_18_MONTHS().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(10);
					if (record.getR13_FD_19_24_MONTHS() != null) {
					    cell10.setCellValue(record.getR13_FD_19_24_MONTHS().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(11);
					if (record.getR13_FD_OVER_24_MONTHS() != null) {
					    cell11.setCellValue(record.getR13_FD_OVER_24_MONTHS().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
					}

					// ---------- R14 ----------
					row = sheet.getRow(13);
					if (row == null) {
					    row = sheet.createRow(13);
					}

					cell1 = row.createCell(1);
					if (record.getR14_CURRENT() != null) {
					    cell1.setCellValue(record.getR14_CURRENT().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR14_CALL() != null) {
					    cell2.setCellValue(record.getR14_CALL().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_SAVINGS() != null) {
					    cell3.setCellValue(record.getR14_SAVINGS().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR14_NOTICE_0_31_DAYS() != null) {
					    cell4.setCellValue(record.getR14_NOTICE_0_31_DAYS().doubleValue());
					    cell4.setCellStyle(numberStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR14_NOTICE_32_88_DAYS() != null) {
					    cell5.setCellValue(record.getR14_NOTICE_32_88_DAYS().doubleValue());
					    cell5.setCellStyle(numberStyle);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR14_91_DEPOSIT_DAY() != null) {
					    cell6.setCellValue(record.getR14_91_DEPOSIT_DAY().doubleValue());
					    cell6.setCellStyle(numberStyle);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR14_FD_1_6_MONTHS() != null) {
					    cell7.setCellValue(record.getR14_FD_1_6_MONTHS().doubleValue());
					    cell7.setCellStyle(numberStyle);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR14_FD_7_12_MONTHS() != null) {
					    cell8.setCellValue(record.getR14_FD_7_12_MONTHS().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR14_FD_13_18_MONTHS() != null) {
					    cell9.setCellValue(record.getR14_FD_13_18_MONTHS().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					cell10 = row.createCell(10);
					if (record.getR14_FD_19_24_MONTHS() != null) {
					    cell10.setCellValue(record.getR14_FD_19_24_MONTHS().doubleValue());
					    cell10.setCellStyle(numberStyle);
					} else {
					    cell10.setCellValue("");
					    cell10.setCellStyle(textStyle);
					}

					cell11 = row.createCell(11);
					if (record.getR14_FD_OVER_24_MONTHS() != null) {
					    cell11.setCellValue(record.getR14_FD_OVER_24_MONTHS().doubleValue());
					    cell11.setCellStyle(numberStyle);
					} else {
					    cell11.setCellValue("");
					    cell11.setCellStyle(textStyle);
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
	
	public List<Object> getM_INTRATESFCAarchival() {
		List<Object> M_INTRATESFCAArchivallist = new ArrayList<>();
//		List<Object> M_FXRArchivallist2 = new ArrayList<>();
//		List<Object> M_FXRArchivallist3 = new ArrayList<>();
		try {
			M_INTRATESFCAArchivallist = M_INT_RATES_FCA_Archival_Summary_Repo.getM_INTRATESFCAarchival();
			
			
			System.out.println("countser" + M_INTRATESFCAArchivallist.size());
//			System.out.println("countser" + M_FXRArchivallist.size());
//			System.out.println("countser" + M_FXRArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_SECL Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_INTRATESFCAArchivallist;
	}


	public byte[] getExcelM_INTRATESFCAARCHIVAL(String filename, String reportId, String fromdate,
			String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if ("ARCHIVAL".equals(type) && version != null) {
		}
			List<M_INT_RATES_FCA_Archival_Summary_Entity> dataList1 = M_INT_RATES_FCA_Archival_Summary_Repo
					.getdatabydateListarchival(dateformat.parse(todate), version);
			
			
		
		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for M_SECL report. Returning empty result.");
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
			
			int startRow = 9;
			
			 if (!dataList1.isEmpty()) {
					for (int i = 0; i < dataList1.size(); i++) {
						M_INT_RATES_FCA_Archival_Summary_Entity record = dataList1.get(i);
						System.out.println("rownumber="+startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						
						Cell cell1 = row.createCell(1);
						if (record.getR10_CURRENT() != null) {
							cell1.setCellValue(record.getR10_CURRENT().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						
						Cell cell2 = row.createCell(2);
						if (record.getR10_CALL() != null) {
							cell2.setCellValue(record.getR10_CALL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}
						
						Cell cell3 = row.createCell(3);
						if (record.getR10_SAVINGS() != null) {
							cell3.setCellValue(record.getR10_SAVINGS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}
						
						Cell cell4 = row.createCell(4);
						if (record.getR10_NOTICE_0_31_DAYS() != null) {
							cell4.setCellValue(record.getR10_NOTICE_0_31_DAYS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}
						
						Cell cell5 = row.createCell(5);
						if (record.getR10_NOTICE_32_88_DAYS() != null) {
							cell5.setCellValue(record.getR10_NOTICE_32_88_DAYS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}
						
						Cell cell6 = row.createCell(6);
						if (record.getR10_91_DEPOSIT_DAY() != null) {
							cell6.setCellValue(record.getR10_91_DEPOSIT_DAY().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}
						
						Cell cell7 = row.createCell(7);
						if (record.getR10_FD_1_6_MONTHS() != null) {
							cell7.setCellValue(record.getR10_FD_1_6_MONTHS().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}
						
						Cell cell8 = row.createCell(8);
						if (record.getR10_FD_7_12_MONTHS() != null) {
							cell8.setCellValue(record.getR10_FD_7_12_MONTHS().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}
						
						Cell cell9 = row.createCell(9);
						if (record.getR10_FD_13_18_MONTHS() != null) {
							cell9.setCellValue(record.getR10_FD_13_18_MONTHS().doubleValue());
							cell9.setCellStyle(numberStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}
						
						Cell cell10 = row.createCell(10);
						if (record.getR10_FD_19_24_MONTHS() != null) {
							cell10.setCellValue(record.getR10_FD_19_24_MONTHS().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}
						
						Cell cell11 = row.createCell(11);
						if (record.getR10_FD_OVER_24_MONTHS() != null) {
							cell11.setCellValue(record.getR10_FD_OVER_24_MONTHS().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}
						

						// ---------- R11 ----------
						row = sheet.getRow(10);
						if (row == null) {
						    row = sheet.createRow(10);
						}

						cell1 = row.createCell(1);
						if (record.getR11_CURRENT() != null) {
						    cell1.setCellValue(record.getR11_CURRENT().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR11_CALL() != null) {
						    cell2.setCellValue(record.getR11_CALL().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR11_SAVINGS() != null) {
						    cell3.setCellValue(record.getR11_SAVINGS().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR11_NOTICE_0_31_DAYS() != null) {
						    cell4.setCellValue(record.getR11_NOTICE_0_31_DAYS().doubleValue());
						    cell4.setCellStyle(numberStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR11_NOTICE_32_88_DAYS() != null) {
						    cell5.setCellValue(record.getR11_NOTICE_32_88_DAYS().doubleValue());
						    cell5.setCellStyle(numberStyle);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR11_91_DEPOSIT_DAY() != null) {
						    cell6.setCellValue(record.getR11_91_DEPOSIT_DAY().doubleValue());
						    cell6.setCellStyle(numberStyle);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR11_FD_1_6_MONTHS() != null) {
						    cell7.setCellValue(record.getR11_FD_1_6_MONTHS().doubleValue());
						    cell7.setCellStyle(numberStyle);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR11_FD_7_12_MONTHS() != null) {
						    cell8.setCellValue(record.getR11_FD_7_12_MONTHS().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR11_FD_13_18_MONTHS() != null) {
						    cell9.setCellValue(record.getR11_FD_13_18_MONTHS().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR11_FD_19_24_MONTHS() != null) {
						    cell10.setCellValue(record.getR11_FD_19_24_MONTHS().doubleValue());
						    cell10.setCellStyle(numberStyle);
						} else {
						    cell10.setCellValue("");
						    cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR11_FD_OVER_24_MONTHS() != null) {
						    cell11.setCellValue(record.getR11_FD_OVER_24_MONTHS().doubleValue());
						    cell11.setCellStyle(numberStyle);
						} else {
						    cell11.setCellValue("");
						    cell11.setCellStyle(textStyle);
						}

						// ---------- R12 ----------
						row = sheet.getRow(11);
						if (row == null) {
						    row = sheet.createRow(11);
						}

						cell1 = row.createCell(1);
						if (record.getR12_CURRENT() != null) {
						    cell1.setCellValue(record.getR12_CURRENT().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR12_CALL() != null) {
						    cell2.setCellValue(record.getR12_CALL().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR12_SAVINGS() != null) {
						    cell3.setCellValue(record.getR12_SAVINGS().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR12_NOTICE_0_31_DAYS() != null) {
						    cell4.setCellValue(record.getR12_NOTICE_0_31_DAYS().doubleValue());
						    cell4.setCellStyle(numberStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR12_NOTICE_32_88_DAYS() != null) {
						    cell5.setCellValue(record.getR12_NOTICE_32_88_DAYS().doubleValue());
						    cell5.setCellStyle(numberStyle);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR12_91_DEPOSIT_DAY() != null) {
						    cell6.setCellValue(record.getR12_91_DEPOSIT_DAY().doubleValue());
						    cell6.setCellStyle(numberStyle);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR12_FD_1_6_MONTHS() != null) {
						    cell7.setCellValue(record.getR12_FD_1_6_MONTHS().doubleValue());
						    cell7.setCellStyle(numberStyle);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR12_FD_7_12_MONTHS() != null) {
						    cell8.setCellValue(record.getR12_FD_7_12_MONTHS().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR12_FD_13_18_MONTHS() != null) {
						    cell9.setCellValue(record.getR12_FD_13_18_MONTHS().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR12_FD_19_24_MONTHS() != null) {
						    cell10.setCellValue(record.getR12_FD_19_24_MONTHS().doubleValue());
						    cell10.setCellStyle(numberStyle);
						} else {
						    cell10.setCellValue("");
						    cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR12_FD_OVER_24_MONTHS() != null) {
						    cell11.setCellValue(record.getR12_FD_OVER_24_MONTHS().doubleValue());
						    cell11.setCellStyle(numberStyle);
						} else {
						    cell11.setCellValue("");
						    cell11.setCellStyle(textStyle);
						}

						// ---------- R13 ----------
						row = sheet.getRow(12);
						if (row == null) {
						    row = sheet.createRow(12);
						}

						cell1 = row.createCell(1);
						if (record.getR13_CURRENT() != null) {
						    cell1.setCellValue(record.getR13_CURRENT().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR13_CALL() != null) {
						    cell2.setCellValue(record.getR13_CALL().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR13_SAVINGS() != null) {
						    cell3.setCellValue(record.getR13_SAVINGS().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR13_NOTICE_0_31_DAYS() != null) {
						    cell4.setCellValue(record.getR13_NOTICE_0_31_DAYS().doubleValue());
						    cell4.setCellStyle(numberStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR13_NOTICE_32_88_DAYS() != null) {
						    cell5.setCellValue(record.getR13_NOTICE_32_88_DAYS().doubleValue());
						    cell5.setCellStyle(numberStyle);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR13_91_DEPOSIT_DAY() != null) {
						    cell6.setCellValue(record.getR13_91_DEPOSIT_DAY().doubleValue());
						    cell6.setCellStyle(numberStyle);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR13_FD_1_6_MONTHS() != null) {
						    cell7.setCellValue(record.getR13_FD_1_6_MONTHS().doubleValue());
						    cell7.setCellStyle(numberStyle);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR13_FD_7_12_MONTHS() != null) {
						    cell8.setCellValue(record.getR13_FD_7_12_MONTHS().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR13_FD_13_18_MONTHS() != null) {
						    cell9.setCellValue(record.getR13_FD_13_18_MONTHS().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR13_FD_19_24_MONTHS() != null) {
						    cell10.setCellValue(record.getR13_FD_19_24_MONTHS().doubleValue());
						    cell10.setCellStyle(numberStyle);
						} else {
						    cell10.setCellValue("");
						    cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR13_FD_OVER_24_MONTHS() != null) {
						    cell11.setCellValue(record.getR13_FD_OVER_24_MONTHS().doubleValue());
						    cell11.setCellStyle(numberStyle);
						} else {
						    cell11.setCellValue("");
						    cell11.setCellStyle(textStyle);
						}

						// ---------- R14 ----------
						row = sheet.getRow(13);
						if (row == null) {
						    row = sheet.createRow(13);
						}

						cell1 = row.createCell(1);
						if (record.getR14_CURRENT() != null) {
						    cell1.setCellValue(record.getR14_CURRENT().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR14_CALL() != null) {
						    cell2.setCellValue(record.getR14_CALL().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR14_SAVINGS() != null) {
						    cell3.setCellValue(record.getR14_SAVINGS().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR14_NOTICE_0_31_DAYS() != null) {
						    cell4.setCellValue(record.getR14_NOTICE_0_31_DAYS().doubleValue());
						    cell4.setCellStyle(numberStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR14_NOTICE_32_88_DAYS() != null) {
						    cell5.setCellValue(record.getR14_NOTICE_32_88_DAYS().doubleValue());
						    cell5.setCellStyle(numberStyle);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR14_91_DEPOSIT_DAY() != null) {
						    cell6.setCellValue(record.getR14_91_DEPOSIT_DAY().doubleValue());
						    cell6.setCellStyle(numberStyle);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record.getR14_FD_1_6_MONTHS() != null) {
						    cell7.setCellValue(record.getR14_FD_1_6_MONTHS().doubleValue());
						    cell7.setCellStyle(numberStyle);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record.getR14_FD_7_12_MONTHS() != null) {
						    cell8.setCellValue(record.getR14_FD_7_12_MONTHS().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record.getR14_FD_13_18_MONTHS() != null) {
						    cell9.setCellValue(record.getR14_FD_13_18_MONTHS().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						cell10 = row.createCell(10);
						if (record.getR14_FD_19_24_MONTHS() != null) {
						    cell10.setCellValue(record.getR14_FD_19_24_MONTHS().doubleValue());
						    cell10.setCellStyle(numberStyle);
						} else {
						    cell10.setCellValue("");
						    cell10.setCellStyle(textStyle);
						}

						cell11 = row.createCell(11);
						if (record.getR14_FD_OVER_24_MONTHS() != null) {
						    cell11.setCellValue(record.getR14_FD_OVER_24_MONTHS().doubleValue());
						    cell11.setCellStyle(numberStyle);
						} else {
						    cell11.setCellValue("");
						    cell11.setCellStyle(textStyle);
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
			
					
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
