package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_MASTER_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_MASTER_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_MASTER_Detail_Repo;
import com.bornfire.brrs.entities.MASTER_Archival_Detail_Entity;
import com.bornfire.brrs.entities.MASTER_Archival_Summary_Entity;
import com.bornfire.brrs.entities.MASTER_Detail_Entity;
import com.bornfire.brrs.entities.MASTER_Summary_Entity;
import com.bornfire.brrs.entities.MASTER_Summary_Repo;

@Component
@Service

public class BRRS_MASTER_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_MASTER_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_MASTER_Detail_Repo BRRS_MASTER_Detail_Repo;

	@Autowired
	MASTER_Summary_Repo MASTER_Summary_repo;


	@Autowired
	BRRS_MASTER_Archival_Summary_Repo brrs_MASTER_Archival_Summary_Repo;

	
	@Autowired
	BRRS_MASTER_Archival_Detail_Repo brrs_MASTER_Archival_Detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getMASTERView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, String version) {
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		System.out.println("testing");
		System.out.println(version);

		if (type.equals("ARCHIVAL") & version != null) {
			System.out.println(type);
			List<MASTER_Archival_Summary_Entity> T1Master = new ArrayList<MASTER_Archival_Summary_Entity>();
			
			System.out.println(version);
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = brrs_MASTER_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate),
						version);


			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		
		} else {

			List<MASTER_Summary_Entity> T1Master = new ArrayList<MASTER_Summary_Entity>();
			
			try {
				Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = MASTER_Summary_repo.getdatabydateList(dateformat.parse(todate));
				

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

		mv.setViewName("BRRS/MASTER");

		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

	public ModelAndView getMASTERcurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String Filter, String type, String version) {

		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

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
			System.out.println(type);
			if ("ARCHIVAL".equals(type) && version != null) {
				System.out.println(type);
				// 🔹 Archival branch
				List<MASTER_Archival_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = brrs_MASTER_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate,
							version);
				} else {
					T1Dt1 = brrs_MASTER_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<MASTER_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BRRS_MASTER_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
				} else {
					T1Dt1 = BRRS_MASTER_Detail_Repo.getdatabydateList(parsedDate);
					totalPages = BRRS_MASTER_Detail_Repo.getdatacount(parsedDate);
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
		mv.setViewName("BRRS/MASTER");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}

	public byte[] getMASTERExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelMASTERARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data
		List<MASTER_Summary_Entity> dataList = MASTER_Summary_repo.getdatabydateList(dateformat.parse(todate));
		
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for MASTER report. Returning empty result.");
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
					MASTER_Summary_Entity record = dataList.get(i);
					
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R11
					// Column C
					Cell R11cell1 = row.createCell(2);
					if (record.getR11_no_of_accounts() != null) {
						R11cell1.setCellValue(record.getR11_no_of_accounts().doubleValue());
						R11cell1.setCellStyle(numberStyle);
					} else {
						R11cell1.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}
					
					// R11
					// Column D
					Cell R11cell2 = row.createCell(3);
					if (record.getR11_total_deposits() != null) {
						R11cell2.setCellValue(record.getR11_total_deposits().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}
					
					
					// R12
					// Column C
					row = sheet.getRow(11);
					Cell R12cell1 = row.createCell(2);
					if (record.getR12_no_of_accounts() != null) {
					R12cell1.setCellValue(record.getR12_no_of_accounts().doubleValue());
					R12cell1.setCellStyle(numberStyle);
					} else {
					R12cell1.setCellValue("");
					R12cell1.setCellStyle(textStyle);
					}
										
					// R12
					// Column D
					Cell R12cell2 = row.createCell(3);
					if (record.getR12_total_deposits() != null) {
					R12cell2.setCellValue(record.getR12_total_deposits().doubleValue());
					R12cell2.setCellStyle(numberStyle);
					} else {
					R12cell2.setCellValue("");
					R12cell2.setCellStyle(textStyle);
					}
					
					
					// R13
					// Column C
					row = sheet.getRow(12);
					Cell R13cell1 = row.createCell(2);
					if (record.getR13_no_of_accounts() != null) {
					R13cell1.setCellValue(record.getR13_no_of_accounts().doubleValue());
					R13cell1.setCellStyle(numberStyle);
					} else {
					R13cell1.setCellValue("");
					R13cell1.setCellStyle(textStyle);
					}
										
					// R13
					// Column D
					Cell R13cell2 = row.createCell(3);
					if (record.getR13_total_deposits() != null) {
					R13cell2.setCellValue(record.getR13_total_deposits().doubleValue());
					R13cell2.setCellStyle(numberStyle);
					} else {
					R13cell2.setCellValue("");
					R13cell2.setCellStyle(textStyle);
					}
					
					// R14
					// Column C
					row = sheet.getRow(13);
					Cell R14cell1 = row.createCell(2);
					if (record.getR14_no_of_accounts() != null) {
					R14cell1.setCellValue(record.getR14_no_of_accounts().doubleValue());
					R14cell1.setCellStyle(numberStyle);
					} else {
					R14cell1.setCellValue("");
					R14cell1.setCellStyle(textStyle);
					}
										
					// R14
					// Column D
					Cell R14cell2 = row.createCell(3);
					if (record.getR14_total_deposits() != null) {
					R14cell2.setCellValue(record.getR14_total_deposits().doubleValue());
					R14cell2.setCellStyle(numberStyle);
					} else {
					R14cell2.setCellValue("");
					R14cell2.setCellStyle(textStyle);
					}
					
					///////////////
					// R21
					// Column C
					row = sheet.getRow(20);
					Cell R21cell1 = row.createCell(2);
					if (record.getR21_no_of_customer() != null) {
					R21cell1.setCellValue(record.getR21_no_of_customer().doubleValue());
					R21cell1.setCellStyle(numberStyle);
					} else {
					R21cell1.setCellValue("");
					R21cell1.setCellStyle(textStyle);
					}
										
					// R21
					// Column D
					Cell R21cell2 = row.createCell(3);
					if (record.getR21_value() != null) {
					R21cell2.setCellValue(record.getR21_value().doubleValue());
					R21cell2.setCellStyle(numberStyle);
					} else {
					R21cell2.setCellValue("");
					R21cell2.setCellStyle(textStyle);
					}
					
		///////////////
					// R22
					// Column C
					row = sheet.getRow(21);
					Cell R22cell1 = row.createCell(2);
					if (record.getR22_no_of_customer() != null) {
					R22cell1.setCellValue(record.getR22_no_of_customer().doubleValue());
					R22cell1.setCellStyle(numberStyle);
					} else {
					R22cell1.setCellValue("");
					R22cell1.setCellStyle(textStyle);
					}
															
					// R22
					// Column D
					Cell R22cell2 = row.createCell(3);
					if (record.getR22_value() != null) {
					R22cell2.setCellValue(record.getR22_value().doubleValue());
					R22cell2.setCellStyle(numberStyle);
					} else {
					R22cell2.setCellValue("");
					R22cell2.setCellStyle(textStyle);
					}
					
		///////////////
					// R23
					// Column C
					row = sheet.getRow(22);
					Cell R23cell1 = row.createCell(2);
					if (record.getR23_no_of_customer() != null) {
					R23cell1.setCellValue(record.getR23_no_of_customer().doubleValue());
					R23cell1.setCellStyle(numberStyle);
					} else {
					R23cell1.setCellValue("");
					R23cell1.setCellStyle(textStyle);
					}
															
					// R23
					// Column D
					Cell R23cell2 = row.createCell(3);
					if (record.getR23_value() != null) {
					R23cell2.setCellValue(record.getR23_value().doubleValue());
					R23cell2.setCellStyle(numberStyle);
					} else {
					R23cell2.setCellValue("");
					R23cell2.setCellStyle(textStyle);
					}
				
		///////////////
					// R30
					// Column C
					row = sheet.getRow(29);
					Cell R30cell1 = row.createCell(2);
					if (record.getR30_low_customer() != null) {
					R30cell1.setCellValue(record.getR30_low_customer().doubleValue());
					R30cell1.setCellStyle(numberStyle);
					} else {
					R30cell1.setCellValue("");
					R30cell1.setCellStyle(textStyle);
					}
															
					// R30
					// Column D
					Cell R30cell2 = row.createCell(3);
					if (record.getR30_low_deposit() != null) {
					R30cell2.setCellValue(record.getR30_low_deposit().doubleValue());
					R30cell2.setCellStyle(numberStyle);
					} else {
					R30cell2.setCellValue("");
					R30cell2.setCellStyle(textStyle);
					}

					// R30
					// Column E
					Cell R30cell3 = row.createCell(4);
					if (record.getR30_medium_customer() != null) {
					R30cell3.setCellValue(record.getR30_medium_customer().doubleValue());
					R30cell3.setCellStyle(numberStyle);
					} else {
					R30cell3.setCellValue("");
					R30cell3.setCellStyle(textStyle);
					}

					// R30
					// Column F
					Cell R30cell4 = row.createCell(5);
					if (record.getR30_medium_deposit() != null) {
					R30cell4.setCellValue(record.getR30_medium_deposit().doubleValue());
					R30cell4.setCellStyle(numberStyle);
					} else {
					R30cell4.setCellValue("");
					R30cell4.setCellStyle(textStyle);
					}

					// R30
					// Column G
					Cell R30cell5 = row.createCell(6);
					if (record.getR30_high_customer() != null) {
					R30cell5.setCellValue(record.getR30_high_customer().doubleValue());
					R30cell5.setCellStyle(numberStyle);
					} else {
					R30cell5.setCellValue("");
					R30cell5.setCellStyle(textStyle);
					}

					// R30
					// Column H
					Cell R30cell6 = row.createCell(7);
					if (record.getR30_high_deposit() != null) {
					R30cell6.setCellValue(record.getR30_high_deposit().doubleValue());
					R30cell6.setCellStyle(numberStyle);
					} else {
					R30cell6.setCellValue("");
					R30cell6.setCellStyle(textStyle);
					}

		///////////////
					// R31
					// Column C
					row = sheet.getRow(30);
					Cell R31cell1 = row.createCell(2);
					if (record.getR31_low_customer() != null) {
					R31cell1.setCellValue(record.getR31_low_customer().doubleValue());
					R31cell1.setCellStyle(numberStyle);
					} else {
					R31cell1.setCellValue("");
					R31cell1.setCellStyle(textStyle);
					}
															
					// R31
					// Column D
					Cell R31cell2 = row.createCell(3);
					if (record.getR31_low_deposit() != null) {
					R31cell2.setCellValue(record.getR31_low_deposit().doubleValue());
					R31cell2.setCellStyle(numberStyle);
					} else {
					R31cell2.setCellValue("");
					R31cell2.setCellStyle(textStyle);
					}

					// R31
					// Column E
					Cell R31cell3 = row.createCell(4);
					if (record.getR31_medium_customer() != null) {
					R31cell3.setCellValue(record.getR31_medium_customer().doubleValue());
					R31cell3.setCellStyle(numberStyle);
					} else {
					R31cell3.setCellValue("");
					R31cell3.setCellStyle(textStyle);
					}

					// R31
					// Column F
					Cell R31cell4 = row.createCell(5);
					if (record.getR31_medium_deposit() != null) {
					R31cell4.setCellValue(record.getR31_medium_deposit().doubleValue());
					R31cell4.setCellStyle(numberStyle);
					} else {
					R31cell4.setCellValue("");
					R31cell4.setCellStyle(textStyle);
					}

					// R31
					// Column G
					Cell R31cell5 = row.createCell(6);
					if (record.getR31_high_customer() != null) {
					R31cell5.setCellValue(record.getR31_high_customer().doubleValue());
					R31cell5.setCellStyle(numberStyle);
					} else {
					R31cell5.setCellValue("");
					R31cell5.setCellStyle(textStyle);
					}

					// R31
					// Column H
					Cell R31cell6 = row.createCell(7);
					if (record.getR31_high_deposit() != null) {
					R31cell6.setCellValue(record.getR31_high_deposit().doubleValue());
					R31cell6.setCellStyle(numberStyle);
					} else {
					R31cell6.setCellValue("");
					R31cell6.setCellStyle(textStyle);
					}


		///////////////
					// R32
					// Column C
					row = sheet.getRow(31);
					Cell R32cell1 = row.createCell(2);
					if (record.getR32_low_customer() != null) {
					R32cell1.setCellValue(record.getR32_low_customer().doubleValue());
					R32cell1.setCellStyle(numberStyle);
					} else {
					R32cell1.setCellValue("");
					R32cell1.setCellStyle(textStyle);
					}
															
					// R32
					// Column D
					Cell R32cell2 = row.createCell(3);
					if (record.getR32_low_deposit() != null) {
					R32cell2.setCellValue(record.getR32_low_deposit().doubleValue());
					R32cell2.setCellStyle(numberStyle);
					} else {
					R32cell2.setCellValue("");
					R32cell2.setCellStyle(textStyle);
					}

					// R32
					// Column E
					Cell R32cell3 = row.createCell(4);
					if (record.getR32_medium_customer() != null) {
					R32cell3.setCellValue(record.getR32_medium_customer().doubleValue());
					R32cell3.setCellStyle(numberStyle);
					} else {
					R32cell3.setCellValue("");
					R32cell3.setCellStyle(textStyle);
					}

					// R32
					// Column F
					Cell R32cell4 = row.createCell(5);
					if (record.getR32_medium_deposit() != null) {
					R32cell4.setCellValue(record.getR32_medium_deposit().doubleValue());
					R32cell4.setCellStyle(numberStyle);
					} else {
					R32cell4.setCellValue("");
					R32cell4.setCellStyle(textStyle);
					}

					// R32
					// Column G
					Cell R32cell5 = row.createCell(6);
					if (record.getR32_high_customer() != null) {
					R32cell5.setCellValue(record.getR32_high_customer().doubleValue());
					R32cell5.setCellStyle(numberStyle);
					} else {
					R32cell5.setCellValue("");
					R32cell5.setCellStyle(textStyle);
					}

					// R32
					// Column H
					Cell R32cell6 = row.createCell(7);
					if (record.getR32_high_deposit() != null) {
					R32cell6.setCellValue(record.getR32_high_deposit().doubleValue());
					R32cell6.setCellStyle(numberStyle);
					} else {
					R32cell6.setCellValue("");
					R32cell6.setCellStyle(textStyle);
					}

		///////////////
					// R39
					// Column C
					row = sheet.getRow(38);
					Cell R39cell1 = row.createCell(2);
					if (record.getR39_no_of_accounts() != null) {
					R39cell1.setCellValue(record.getR39_no_of_accounts().doubleValue());
					R39cell1.setCellStyle(numberStyle);
					} else {
					R39cell1.setCellValue("");
					R39cell1.setCellStyle(textStyle);
					}
															
					// R39
					// Column D
					Cell R39cell2 = row.createCell(3);
					if (record.getR39_deposits() != null) {
					R39cell2.setCellValue(record.getR39_deposits().doubleValue());
					R39cell2.setCellStyle(numberStyle);
					} else {
					R39cell2.setCellValue("");
					R39cell2.setCellStyle(textStyle);
					}
					
		///////////////
					// R40
					// Column C
					row = sheet.getRow(39);
					Cell R40cell1 = row.createCell(2);
					if (record.getR40_no_of_accounts() != null) {
					R40cell1.setCellValue(record.getR40_no_of_accounts().doubleValue());
					R40cell1.setCellStyle(numberStyle);
					} else {
					R40cell1.setCellValue("");
					R40cell1.setCellStyle(textStyle);
					}
															
					// R40
					// Column D
					Cell R40cell2 = row.createCell(3);
					if (record.getR40_deposits() != null) {
					R40cell2.setCellValue(record.getR40_deposits().doubleValue());
					R40cell2.setCellStyle(numberStyle);
					} else {
					R40cell2.setCellValue("");
					R40cell2.setCellStyle(textStyle);
					}
					
		///////////////
					// R51
					// Column C
					row = sheet.getRow(50);
					Cell R51cell1 = row.createCell(3);
					if (record.getR51_no_of_accounts() != null) {
					R51cell1.setCellValue(record.getR51_no_of_accounts().doubleValue());
					R51cell1.setCellStyle(numberStyle);
					} else {
					R51cell1.setCellValue("");
					R51cell1.setCellStyle(textStyle);
					}
															
					// R51
					// Column D
					Cell R51cell2 = row.createCell(4);
					if (record.getR51_deposits() != null) {
					R51cell2.setCellValue(record.getR51_deposits().doubleValue());
					R51cell2.setCellStyle(numberStyle);
					} else {
					R51cell2.setCellValue("");
					R51cell2.setCellStyle(textStyle);
					}
					
		///////////////
					// R52
					// Column C
					row = sheet.getRow(51);
					Cell R52cell1 = row.createCell(3);
					if (record.getR52_no_of_accounts() != null) {
					R52cell1.setCellValue(record.getR52_no_of_accounts().doubleValue());
					R52cell1.setCellStyle(numberStyle);
					} else {
					R52cell1.setCellValue("");
					R52cell1.setCellStyle(textStyle);
					}
															
					// R52
					// Column D
					Cell R52cell2 = row.createCell(4);
					if (record.getR52_deposits() != null) {
					R52cell2.setCellValue(record.getR52_deposits().doubleValue());
					R52cell2.setCellStyle(numberStyle);
					} else {
					R52cell2.setCellValue("");
					R52cell2.setCellStyle(textStyle);
					}
					
		///////////////
					// R53
					// Column C
					row = sheet.getRow(52);
					Cell R53cell1 = row.createCell(3);
					if (record.getR53_no_of_accounts() != null) {
					R53cell1.setCellValue(record.getR53_no_of_accounts().doubleValue());
					R53cell1.setCellStyle(numberStyle);
					} else {
					R53cell1.setCellValue("");
					R53cell1.setCellStyle(textStyle);
					}
															
					// R53
					// Column D
					Cell R53cell2 = row.createCell(4);
					if (record.getR53_deposits() != null) {
					R53cell2.setCellValue(record.getR53_deposits().doubleValue());
					R53cell2.setCellStyle(numberStyle);
					} else {
					R53cell2.setCellValue("");
					R53cell2.setCellStyle(textStyle);
					}
					
		///////////////
					// R54
					// Column C
					row = sheet.getRow(53);
					Cell R54cell1 = row.createCell(3);
					if (record.getR54_no_of_accounts() != null) {
					R54cell1.setCellValue(record.getR54_no_of_accounts().doubleValue());
					R54cell1.setCellStyle(numberStyle);
					} else {
					R54cell1.setCellValue("");
					R54cell1.setCellStyle(textStyle);
					}
															
					// R54
					// Column D
					Cell R54cell2 = row.createCell(4);
					if (record.getR54_deposits() != null) {
					R54cell2.setCellValue(record.getR54_deposits().doubleValue());
					R54cell2.setCellStyle(numberStyle);
					} else {
					R54cell2.setCellValue("");
					R54cell2.setCellStyle(textStyle);
					}
					
		///////////////
					// R57
					// Column C
					row = sheet.getRow(56);
					Cell R57cell1 = row.createCell(3);
					if (record.getR57_no_of_accounts() != null) {
					R57cell1.setCellValue(record.getR57_no_of_accounts().doubleValue());
					R57cell1.setCellStyle(numberStyle);
					} else {
					R57cell1.setCellValue("");
					R57cell1.setCellStyle(textStyle);
					}
															
					// R57
					// Column D
					Cell R57cell2 = row.createCell(4);
					if (record.getR57_deposits() != null) {
					R57cell2.setCellValue(record.getR57_deposits().doubleValue());
					R57cell2.setCellStyle(numberStyle);
					} else {
					R57cell2.setCellValue("");
					R57cell2.setCellStyle(textStyle);
					}
					
		///////////////
					// R58
					// Column C
					row = sheet.getRow(57);
					Cell R58cell1 = row.createCell(3);
					if (record.getR58_no_of_accounts() != null) {
					R58cell1.setCellValue(record.getR58_no_of_accounts().doubleValue());
					R58cell1.setCellStyle(numberStyle);
					} else {
					R58cell1.setCellValue("");
					R58cell1.setCellStyle(textStyle);
					}
															
					// R58
					// Column D
					Cell R58cell2 = row.createCell(4);
					if (record.getR58_deposits() != null) {
					R58cell2.setCellValue(record.getR58_deposits().doubleValue());
					R58cell2.setCellStyle(numberStyle);
					} else {
					R58cell2.setCellValue("");
					R58cell2.setCellStyle(textStyle);
					}
					
		///////////////
					// R59
					// Column C
					row = sheet.getRow(58);
					Cell R59cell1 = row.createCell(3);
					if (record.getR59_no_of_accounts() != null) {
					R59cell1.setCellValue(record.getR59_no_of_accounts().doubleValue());
					R59cell1.setCellStyle(numberStyle);
					} else {
					R59cell1.setCellValue("");
					R59cell1.setCellStyle(textStyle);
					}
															
					// R59
					// Column D
					Cell R59cell2 = row.createCell(4);
					if (record.getR59_deposits() != null) {
					R59cell2.setCellValue(record.getR59_deposits().doubleValue());
					R59cell2.setCellStyle(numberStyle);
					} else {
					R59cell2.setCellValue("");
					R59cell2.setCellStyle(textStyle);
					}
					
		///////////////
					// R60
					// Column C
					row = sheet.getRow(59);
					Cell R60cell1 = row.createCell(3);
					if (record.getR60_no_of_accounts() != null) {
					R60cell1.setCellValue(record.getR60_no_of_accounts().doubleValue());
					R60cell1.setCellStyle(numberStyle);
					} else {
					R60cell1.setCellValue("");
					R60cell1.setCellStyle(textStyle);
					}
															
					// R60
					// Column D
					Cell R60cell2 = row.createCell(4);
					if (record.getR60_deposits() != null) {
					R60cell2.setCellValue(record.getR60_deposits().doubleValue());
					R60cell2.setCellStyle(numberStyle);
					} else {
					R60cell2.setCellValue("");
					R60cell2.setCellStyle(textStyle);
					}
					
		///////////////
					// R61
					// Column C
					row = sheet.getRow(60);
					Cell R61cell1 = row.createCell(3);
					if (record.getR61_no_of_accounts() != null) {
					R61cell1.setCellValue(record.getR61_no_of_accounts().doubleValue());
					R61cell1.setCellStyle(numberStyle);
					} else {
					R61cell1.setCellValue("");
					R61cell1.setCellStyle(textStyle);
					}
															
					// R61
					// Column D
					Cell R61cell2 = row.createCell(4);
					if (record.getR61_deposits() != null) {
					R61cell2.setCellValue(record.getR61_deposits().doubleValue());
					R61cell2.setCellStyle(numberStyle);
					} else {
					R61cell2.setCellValue("");
					R61cell2.setCellStyle(textStyle);
					}
					
		///////////////
					// R62
					// Column C
					row = sheet.getRow(61);
					Cell R62cell1 = row.createCell(3);
					if (record.getR62_no_of_accounts() != null) {
					R62cell1.setCellValue(record.getR62_no_of_accounts().doubleValue());
					R62cell1.setCellStyle(numberStyle);
					} else {
					R62cell1.setCellValue("");
					R62cell1.setCellStyle(textStyle);
					}
															
					// R62
					// Column D
					Cell R62cell2 = row.createCell(4);
					if (record.getR62_deposits() != null) {
					R62cell2.setCellValue(record.getR62_deposits().doubleValue());
					R62cell2.setCellStyle(numberStyle);
					} else {
					R62cell2.setCellValue("");
					R62cell2.setCellStyle(textStyle);
					}
					
		///////////////
					// R63
					// Column C
					row = sheet.getRow(62);
					Cell R63cell1 = row.createCell(3);
					if (record.getR63_no_of_accounts() != null) {
					R63cell1.setCellValue(record.getR63_no_of_accounts().doubleValue());
					R63cell1.setCellStyle(numberStyle);
					} else {
					R63cell1.setCellValue("");
					R63cell1.setCellStyle(textStyle);
					}
															
					// R63
					// Column D
					Cell R63cell2 = row.createCell(4);
					if (record.getR63_deposits() != null) {
					R63cell2.setCellValue(record.getR63_deposits().doubleValue());
					R63cell2.setCellStyle(numberStyle);
					} else {
					R63cell2.setCellValue("");
					R63cell2.setCellStyle(textStyle);
					}
					
		///////////////
					// R64
					// Column C
					row = sheet.getRow(63);
					Cell R64cell1 = row.createCell(3);
					if (record.getR64_no_of_accounts() != null) {
					R64cell1.setCellValue(record.getR64_no_of_accounts().doubleValue());
					R64cell1.setCellStyle(numberStyle);
					} else {
					R64cell1.setCellValue("");
					R64cell1.setCellStyle(textStyle);
					}
															
					// R64
					// Column D
					Cell R64cell2 = row.createCell(4);
					if (record.getR64_deposits() != null) {
					R64cell2.setCellValue(record.getR64_deposits().doubleValue());
					R64cell2.setCellStyle(numberStyle);
					} else {
					R64cell2.setCellValue("");
					R64cell2.setCellStyle(textStyle);
					}
					
		///////////////
					// R65
					// Column C
					row = sheet.getRow(64);
					Cell R65cell1 = row.createCell(3);
					if (record.getR65_no_of_accounts() != null) {
					R65cell1.setCellValue(record.getR65_no_of_accounts().doubleValue());
					R65cell1.setCellStyle(numberStyle);
					} else {
					R65cell1.setCellValue("");
					R65cell1.setCellStyle(textStyle);
					}
															
					// R65
					// Column D
					Cell R65cell2 = row.createCell(4);
					if (record.getR65_deposits() != null) {
					R65cell2.setCellValue(record.getR65_deposits().doubleValue());
					R65cell2.setCellStyle(numberStyle);
					} else {
					R65cell2.setCellValue("");
					R65cell2.setCellStyle(textStyle);
					}
					
		///////////////
					// R66
					// Column C
					row = sheet.getRow(65);
					Cell R66cell1 = row.createCell(3);
					if (record.getR66_no_of_accounts() != null) {
					R66cell1.setCellValue(record.getR66_no_of_accounts().doubleValue());
					R66cell1.setCellStyle(numberStyle);
					} else {
					R66cell1.setCellValue("");
					R66cell1.setCellStyle(textStyle);
					}
															
					// R66
					// Column D
					Cell R66cell2 = row.createCell(4);
					if (record.getR66_deposits() != null) {
					R66cell2.setCellValue(record.getR66_deposits().doubleValue());
					R66cell2.setCellStyle(numberStyle);
					} else {
					R66cell2.setCellValue("");
					R66cell2.setCellStyle(textStyle);
					}
					
		///////////////
					// R67
					// Column C
					row = sheet.getRow(66);
					Cell R67cell1 = row.createCell(3);
					if (record.getR67_no_of_accounts() != null) {
					R67cell1.setCellValue(record.getR67_no_of_accounts().doubleValue());
					R67cell1.setCellStyle(numberStyle);
					} else {
					R67cell1.setCellValue("");
					R67cell1.setCellStyle(textStyle);
					}
															
					// R67
					// Column D
					Cell R67cell2 = row.createCell(4);
					if (record.getR67_deposits() != null) {
					R67cell2.setCellValue(record.getR67_deposits().doubleValue());
					R67cell2.setCellStyle(numberStyle);
					} else {
					R67cell2.setCellValue("");
					R67cell2.setCellStyle(textStyle);
					}
					
		///////////////
					// R68
					// Column C
					row = sheet.getRow(67);
					Cell R68cell1 = row.createCell(3);
					if (record.getR68_no_of_accounts() != null) {
					R68cell1.setCellValue(record.getR68_no_of_accounts().doubleValue());
					R68cell1.setCellStyle(numberStyle);
					} else {
					R68cell1.setCellValue("");
					R68cell1.setCellStyle(textStyle);
					}
															
					// R68
					// Column D
					Cell R68cell2 = row.createCell(4);
					if (record.getR68_deposits() != null) {
					R68cell2.setCellValue(record.getR68_deposits().doubleValue());
					R68cell2.setCellStyle(numberStyle);
					} else {
					R68cell2.setCellValue("");
					R68cell2.setCellStyle(textStyle);
					}
					
		///////////////
					// R69
					// Column C
					row = sheet.getRow(68);
					Cell R69cell1 = row.createCell(3);
					if (record.getR69_no_of_accounts() != null) {
					R69cell1.setCellValue(record.getR69_no_of_accounts().doubleValue());
					R69cell1.setCellStyle(numberStyle);
					} else {
					R69cell1.setCellValue("");
					R69cell1.setCellStyle(textStyle);
					}
															
					// R69
					// Column D
					Cell R69cell2 = row.createCell(4);
					if (record.getR69_deposits() != null) {
					R69cell2.setCellValue(record.getR69_deposits().doubleValue());
					R69cell2.setCellStyle(numberStyle);
					} else {
					R69cell2.setCellValue("");
					R69cell2.setCellStyle(textStyle);
					}
					
		///////////////
					// R72
					// Column C
					row = sheet.getRow(71);
					Cell R72cell1 = row.createCell(3);
					if (record.getR72_no_of_accounts() != null) {
					R72cell1.setCellValue(record.getR72_no_of_accounts().doubleValue());
					R72cell1.setCellStyle(numberStyle);
					} else {
					R72cell1.setCellValue("");
					R72cell1.setCellStyle(textStyle);
					}
															
					// R72
					// Column D
					Cell R72cell2 = row.createCell(4);
					if (record.getR72_deposits() != null) {
					R72cell2.setCellValue(record.getR72_deposits().doubleValue());
					R72cell2.setCellStyle(numberStyle);
					} else {
					R72cell2.setCellValue("");
					R72cell2.setCellStyle(textStyle);
					}
					
		///////////////
					// R73
					// Column C
					row = sheet.getRow(72);
					Cell R73cell1 = row.createCell(3);
					if (record.getR73_no_of_accounts() != null) {
					R73cell1.setCellValue(record.getR73_no_of_accounts().doubleValue());
					R73cell1.setCellStyle(numberStyle);
					} else {
					R73cell1.setCellValue("");
					R73cell1.setCellStyle(textStyle);
					}
															
					// R73
					// Column D
					Cell R73cell2 = row.createCell(4);
					if (record.getR73_deposits() != null) {
					R73cell2.setCellValue(record.getR73_deposits().doubleValue());
					R73cell2.setCellStyle(numberStyle);
					} else {
					R73cell2.setCellValue("");
					R73cell2.setCellStyle(textStyle);
					}


		///////////////
					// R74
					// Column C
					row = sheet.getRow(73);
					Cell R74cell1 = row.createCell(3);
					if (record.getR74_no_of_accounts() != null) {
					R74cell1.setCellValue(record.getR74_no_of_accounts().doubleValue());
					R74cell1.setCellStyle(numberStyle);
					} else {
					R74cell1.setCellValue("");
					R74cell1.setCellStyle(textStyle);
					}
															
					// R74
					// Column D
					Cell R74cell2 = row.createCell(4);
					if (record.getR74_deposits() != null) {
					R74cell2.setCellValue(record.getR74_deposits().doubleValue());
					R74cell2.setCellStyle(numberStyle);
					} else {
					R74cell2.setCellValue("");
					R74cell2.setCellStyle(textStyle);
					}

					
		///////////////
					// R82
					// Column B
					row = sheet.getRow(81);
					Cell R82cell1 = row.createCell(1);
					if (record.getR82_customer() != null) {
					R82cell1.setCellValue(record.getR82_customer().doubleValue());
					R82cell1.setCellStyle(numberStyle);
					} else {
					R82cell1.setCellValue("");
					R82cell1.setCellStyle(textStyle);
					}
															
					// R82
					// Column C
					Cell R82cell2 = row.createCell(2);
					if (record.getR82_loan() != null) {
					R82cell2.setCellValue(record.getR82_loan().doubleValue());
					R82cell2.setCellStyle(numberStyle);
					} else {
					R82cell2.setCellValue("");
					R82cell2.setCellStyle(textStyle);
					}

					// R82
					// Column D
					Cell R82cell3 = row.createCell(3);
					if (record.getR82_deposits() != null) {
					R82cell3.setCellValue(record.getR82_deposits().doubleValue());
					R82cell3.setCellStyle(numberStyle);
					} else {
					R82cell3.setCellValue("");
					R82cell3.setCellStyle(textStyle);
					}

					// R82
					// Column E
					Cell R82cell4 = row.createCell(4);
					if (record.getR82_funds() != null) {
					R82cell4.setCellValue(record.getR82_funds().doubleValue());
					R82cell4.setCellStyle(numberStyle);
					} else {
					R82cell4.setCellValue("");
					R82cell4.setCellStyle(textStyle);
					}

					// R82
					// Column F
					Cell R82cell5 = row.createCell(5);
					if (record.getR82_turnover() != null) {
					R82cell5.setCellValue(record.getR82_turnover().doubleValue());
					R82cell5.setCellStyle(numberStyle);
					} else {
					R82cell5.setCellValue("");
					R82cell5.setCellStyle(textStyle);
					}


		///////////////
					// R89
					// Column B
					row = sheet.getRow(88);
					Cell R89cell1 = row.createCell(1);
					if (record.getR89_customer() != null) {
					R89cell1.setCellValue(record.getR89_customer().doubleValue());
					R89cell1.setCellStyle(numberStyle);
					} else {
					R89cell1.setCellValue("");
					R89cell1.setCellStyle(textStyle);
					}
															
					// R89
					// Column C
					Cell R89cell2 = row.createCell(2);
					if (record.getR89_loan() != null) {
					R89cell2.setCellValue(record.getR89_loan().doubleValue());
					R89cell2.setCellStyle(numberStyle);
					} else {
					R89cell2.setCellValue("");
					R89cell2.setCellStyle(textStyle);
					}

					// R89
					// Column D
					Cell R89cell3 = row.createCell(3);
					if (record.getR89_deposits() != null) {
					R89cell3.setCellValue(record.getR89_deposits().doubleValue());
					R89cell3.setCellStyle(numberStyle);
					} else {
					R89cell3.setCellValue("");
					R89cell3.setCellStyle(textStyle);
					}

					// R89
					// Column E
					Cell R89cell4 = row.createCell(4);
					if (record.getR89_funds() != null) {
					R89cell4.setCellValue(record.getR89_funds().doubleValue());
					R89cell4.setCellStyle(numberStyle);
					} else {
					R89cell4.setCellValue("");
					R89cell4.setCellStyle(textStyle);
					}

					// R89
					// Column F
					Cell R89cell5 = row.createCell(5);
					if (record.getR89_turnover() != null) {
					R89cell5.setCellValue(record.getR89_turnover().doubleValue());
					R89cell5.setCellStyle(numberStyle);
					} else {
					R89cell5.setCellValue("");
					R89cell5.setCellStyle(textStyle);
					}


		///////////////
					// R96
					// Column B
					row = sheet.getRow(95);
					Cell R96cell1 = row.createCell(1);
					if (record.getR96_customer() != null) {
					R96cell1.setCellValue(record.getR96_customer().doubleValue());
					R96cell1.setCellStyle(numberStyle);
					} else {
					R96cell1.setCellValue("");
					R96cell1.setCellStyle(textStyle);
					}
															
					// R96
					// Column C
					Cell R96cell2 = row.createCell(2);
					if (record.getR96_loan() != null) {
					R96cell2.setCellValue(record.getR96_loan().doubleValue());
					R96cell2.setCellStyle(numberStyle);
					} else {
					R96cell2.setCellValue("");
					R96cell2.setCellStyle(textStyle);
					}

					// R96
					// Column D
					Cell R96cell3 = row.createCell(3);
					if (record.getR96_deposits() != null) {
					R96cell3.setCellValue(record.getR96_deposits().doubleValue());
					R96cell3.setCellStyle(numberStyle);
					} else {
					R96cell3.setCellValue("");
					R96cell3.setCellStyle(textStyle);
					}

					// R96
					// Column E
					Cell R96cell4 = row.createCell(4);
					if (record.getR96_funds() != null) {
					R96cell4.setCellValue(record.getR96_funds().doubleValue());
					R96cell4.setCellStyle(numberStyle);
					} else {
					R96cell4.setCellValue("");
					R96cell4.setCellStyle(textStyle);
					}

					// R96
					// Column F
					Cell R96cell5 = row.createCell(5);
					if (record.getR96_turnover() != null) {
					R96cell5.setCellValue(record.getR96_turnover().doubleValue());
					R96cell5.setCellStyle(numberStyle);
					} else {
					R96cell5.setCellValue("");
					R96cell5.setCellStyle(textStyle);
					}


		///////////////
					// R104
					// Column B
					row = sheet.getRow(103);
					Cell R104cell1 = row.createCell(1);
					if (record.getR104_customer() != null) {
					R104cell1.setCellValue(record.getR104_customer().doubleValue());
					R104cell1.setCellStyle(numberStyle);
					} else {
					R104cell1.setCellValue("");
					R104cell1.setCellStyle(textStyle);
					}
															
					// R104
					// Column C
					Cell R104cell2 = row.createCell(2);
					if (record.getR104_loan() != null) {
					R104cell2.setCellValue(record.getR104_loan().doubleValue());
					R104cell2.setCellStyle(numberStyle);
					} else {
					R104cell2.setCellValue("");
					R104cell2.setCellStyle(textStyle);
					}

					// R104
					// Column D
					Cell R104cell3 = row.createCell(3);
					if (record.getR104_deposits() != null) {
					R104cell3.setCellValue(record.getR104_deposits().doubleValue());
					R104cell3.setCellStyle(numberStyle);
					} else {
					R104cell3.setCellValue("");
					R104cell3.setCellStyle(textStyle);
					}

					// R104
					// Column E
					Cell R104cell4 = row.createCell(4);
					if (record.getR104_funds() != null) {
					R104cell4.setCellValue(record.getR104_funds().doubleValue());
					R104cell4.setCellStyle(numberStyle);
					} else {
					R104cell4.setCellValue("");
					R104cell4.setCellStyle(textStyle);
					}

					// R104
					// Column F
					Cell R104cell5 = row.createCell(5);
					if (record.getR104_turnover() != null) {
					R104cell5.setCellValue(record.getR104_turnover().doubleValue());
					R104cell5.setCellStyle(numberStyle);
					} else {
					R104cell5.setCellValue("");
					R104cell5.setCellStyle(textStyle);
					}

		///////////////
					// R111
					// Column C
					row = sheet.getRow(110);
					Cell R111cell1 = row.createCell(3);
					if (record.getR111_no_of_transfer() != null) {
					R111cell1.setCellValue(record.getR111_no_of_transfer().doubleValue());
					R111cell1.setCellStyle(numberStyle);
					} else {
					R111cell1.setCellValue("");
					R111cell1.setCellStyle(textStyle);
					}
															
					// R111
					// Column D
					Cell R111cell2 = row.createCell(4);
					if (record.getR111_values_of_transfer() != null) {
					R111cell2.setCellValue(record.getR111_values_of_transfer().doubleValue());
					R111cell2.setCellStyle(numberStyle);
					} else {
					R111cell2.setCellValue("");
					R111cell2.setCellStyle(textStyle);
					}


					
		///////////////
					// R112
					// Column C
					row = sheet.getRow(111);
					Cell R112cell1 = row.createCell(3);
					if (record.getR112_no_of_transfer() != null) {
					R112cell1.setCellValue(record.getR112_no_of_transfer().doubleValue());
					R112cell1.setCellStyle(numberStyle);
					} else {
					R112cell1.setCellValue("");
					R112cell1.setCellStyle(textStyle);
					}
															
					// R112
					// Column D
					Cell R112cell2 = row.createCell(4);
					if (record.getR112_values_of_transfer() != null) {
					R112cell2.setCellValue(record.getR112_values_of_transfer().doubleValue());
					R112cell2.setCellStyle(numberStyle);
					} else {
					R112cell2.setCellValue("");
					R112cell2.setCellStyle(textStyle);
					}

		///////////////
					// R114
					// Column C
					row = sheet.getRow(113);
					Cell R114cell1 = row.createCell(3);
					if (record.getR114_no_of_transfer() != null) {
					R114cell1.setCellValue(record.getR114_no_of_transfer().doubleValue());
					R114cell1.setCellStyle(numberStyle);
					} else {
					R114cell1.setCellValue("");
					R114cell1.setCellStyle(textStyle);
					}
															
					// R114
					// Column D
					Cell R114cell2 = row.createCell(4);
					if (record.getR114_values_of_transfer() != null) {
					R114cell2.setCellValue(record.getR114_values_of_transfer().doubleValue());
					R114cell2.setCellStyle(numberStyle);
					} else {
					R114cell2.setCellValue("");
					R114cell2.setCellStyle(textStyle);
					}

		///////////////
					// R115
					// Column C
					row = sheet.getRow(114);
					Cell R115cell1 = row.createCell(3);
					if (record.getR115_no_of_transfer() != null) {
					R115cell1.setCellValue(record.getR115_no_of_transfer().doubleValue());
					R115cell1.setCellStyle(numberStyle);
					} else {
					R115cell1.setCellValue("");
					R115cell1.setCellStyle(textStyle);
					}
															
					// R115
					// Column D
					Cell R115cell2 = row.createCell(4);
					if (record.getR115_values_of_transfer() != null) {
					R115cell2.setCellValue(record.getR115_values_of_transfer().doubleValue());
					R115cell2.setCellStyle(numberStyle);
					} else {
					R115cell2.setCellValue("");
					R115cell2.setCellStyle(textStyle);
					}

		///////////////
					// R117
					// Column C
					row = sheet.getRow(116);
					Cell R117cell1 = row.createCell(3);
					if (record.getR117_no_of_transfer() != null) {
					R117cell1.setCellValue(record.getR117_no_of_transfer().doubleValue());
					R117cell1.setCellStyle(numberStyle);
					} else {
					R117cell1.setCellValue("");
					R117cell1.setCellStyle(textStyle);
					}
															
					// R117
					// Column D
					Cell R117cell2 = row.createCell(4);
					if (record.getR117_values_of_transfer() != null) {
					R117cell2.setCellValue(record.getR117_values_of_transfer().doubleValue());
					R117cell2.setCellStyle(numberStyle);
					} else {
					R117cell2.setCellValue("");
					R117cell2.setCellStyle(textStyle);
					}

		///////////////
					// R118
					// Column C
					row = sheet.getRow(117);
					Cell R118cell1 = row.createCell(3);
					if (record.getR118_no_of_transfer() != null) {
					R118cell1.setCellValue(record.getR118_no_of_transfer().doubleValue());
					R118cell1.setCellStyle(numberStyle);
					} else {
					R118cell1.setCellValue("");
					R118cell1.setCellStyle(textStyle);
					}
															
					// R118
					// Column D
					Cell R118cell2 = row.createCell(4);
					if (record.getR118_values_of_transfer() != null) {
					R118cell2.setCellValue(record.getR118_values_of_transfer().doubleValue());
					R118cell2.setCellStyle(numberStyle);
					} else {
					R118cell2.setCellValue("");
					R118cell2.setCellStyle(textStyle);
					}

		///////////////
					// R120
					// Column C
					row = sheet.getRow(119);
					Cell R120cell1 = row.createCell(3);
					if (record.getR120_no_of_transfer() != null) {
					R120cell1.setCellValue(record.getR120_no_of_transfer().doubleValue());
					R120cell1.setCellStyle(numberStyle);
					} else {
					R120cell1.setCellValue("");
					R120cell1.setCellStyle(textStyle);
					}
															
					// R120
					// Column D
					Cell R120cell2 = row.createCell(4);
					if (record.getR120_values_of_transfer() != null) {
					R120cell2.setCellValue(record.getR120_values_of_transfer().doubleValue());
					R120cell2.setCellStyle(numberStyle);
					} else {
					R120cell2.setCellValue("");
					R120cell2.setCellStyle(textStyle);
					}


		///////////////
					// R121
					// Column C
					row = sheet.getRow(120);
					Cell R121cell1 = row.createCell(3);
					if (record.getR121_no_of_transfer() != null) {
					R121cell1.setCellValue(record.getR121_no_of_transfer().doubleValue());
					R121cell1.setCellStyle(numberStyle);
					} else {
					R121cell1.setCellValue("");
					R121cell1.setCellStyle(textStyle);
					}
															
					// R121
					// Column D
					Cell R121cell2 = row.createCell(4);
					if (record.getR121_values_of_transfer() != null) {
					R121cell2.setCellValue(record.getR121_values_of_transfer().doubleValue());
					R121cell2.setCellStyle(numberStyle);
					} else {
					R121cell2.setCellValue("");
					R121cell2.setCellStyle(textStyle);
					}


		///////////////
					// R123
					// Column C
					row = sheet.getRow(122);
					Cell R123cell1 = row.createCell(3);
					if (record.getR123_no_of_transfer() != null) {
					R123cell1.setCellValue(record.getR123_no_of_transfer().doubleValue());
					R123cell1.setCellStyle(numberStyle);
					} else {
					R123cell1.setCellValue("");
					R123cell1.setCellStyle(textStyle);
					}
															
					// R123
					// Column D
					Cell R123cell2 = row.createCell(4);
					if (record.getR123_values_of_transfer() != null) {
					R123cell2.setCellValue(record.getR123_values_of_transfer().doubleValue());
					R123cell2.setCellStyle(numberStyle);
					} else {
					R123cell2.setCellValue("");
					R123cell2.setCellStyle(textStyle);
					}


		///////////////
					// R124
					// Column C
					row = sheet.getRow(123);
					Cell R124cell1 = row.createCell(3);
					if (record.getR124_no_of_transfer() != null) {
					R124cell1.setCellValue(record.getR124_no_of_transfer().doubleValue());
					R124cell1.setCellStyle(numberStyle);
					} else {
					R124cell1.setCellValue("");
					R124cell1.setCellStyle(textStyle);
					}
															
					// R124
					// Column D
					Cell R124cell2 = row.createCell(4);
					if (record.getR124_values_of_transfer() != null) {
					R124cell2.setCellValue(record.getR124_values_of_transfer().doubleValue());
					R124cell2.setCellStyle(numberStyle);
					} else {
					R124cell2.setCellValue("");
					R124cell2.setCellStyle(textStyle);
					}


		///////////////
					// R126
					// Column C
					row = sheet.getRow(125);
					Cell R126cell1 = row.createCell(3);
					if (record.getR126_no_of_transfer() != null) {
					R126cell1.setCellValue(record.getR126_no_of_transfer().doubleValue());
					R126cell1.setCellStyle(numberStyle);
					} else {
					R126cell1.setCellValue("");
					R126cell1.setCellStyle(textStyle);
					}
															
					// R126
					// Column D
					Cell R126cell2 = row.createCell(4);
					if (record.getR126_values_of_transfer() != null) {
					R126cell2.setCellValue(record.getR126_values_of_transfer().doubleValue());
					R126cell2.setCellStyle(numberStyle);
					} else {
					R126cell2.setCellValue("");
					R126cell2.setCellStyle(textStyle);
					}

		///////////////
					// R127
					// Column C
					row = sheet.getRow(126);
					Cell R127cell1 = row.createCell(3);
					if (record.getR127_no_of_transfer() != null) {
					R127cell1.setCellValue(record.getR127_no_of_transfer().doubleValue());
					R127cell1.setCellStyle(numberStyle);
					} else {
					R127cell1.setCellValue("");
					R127cell1.setCellStyle(textStyle);
					}
															
					// R127
					// Column D
					Cell R127cell2 = row.createCell(4);
					if (record.getR127_values_of_transfer() != null) {
					R127cell2.setCellValue(record.getR127_values_of_transfer().doubleValue());
					R127cell2.setCellStyle(numberStyle);
					} else {
					R127cell2.setCellValue("");
					R127cell2.setCellStyle(textStyle);
					}

		///////////////
					// R128
					// Column C
					row = sheet.getRow(127);
					Cell R128cell1 = row.createCell(3);
					if (record.getR128_no_of_transfer() != null) {
					R128cell1.setCellValue(record.getR128_no_of_transfer().doubleValue());
					R128cell1.setCellStyle(numberStyle);
					} else {
					R128cell1.setCellValue("");
					R128cell1.setCellStyle(textStyle);
					}
															
					// R128
					// Column D
					Cell R128cell2 = row.createCell(4);
					if (record.getR128_values_of_transfer() != null) {
					R128cell2.setCellValue(record.getR128_values_of_transfer().doubleValue());
					R128cell2.setCellStyle(numberStyle);
					} else {
					R128cell2.setCellValue("");
					R128cell2.setCellStyle(textStyle);
					}


		///////////////
					// R135
					// Column E
					row = sheet.getRow(134);
					Cell R135cell1 = row.createCell(4);
					if (record.getR135_transactions() != null) {
					R135cell1.setCellValue(record.getR135_transactions().doubleValue());
					R135cell1.setCellStyle(numberStyle);
					} else {
					R135cell1.setCellValue("");
					R135cell1.setCellStyle(textStyle);
					}
					
		///////////////
					// R136
					// Column E
					row = sheet.getRow(135);
					Cell R136cell1 = row.createCell(4);
					if (record.getR136_transactions() != null) {
					R136cell1.setCellValue(record.getR136_transactions().doubleValue());
					R136cell1.setCellStyle(numberStyle);
					} else {
					R136cell1.setCellValue("");
					R136cell1.setCellStyle(textStyle);
					}
					
		///////////////
					// R138
					// Column E
					row = sheet.getRow(137);
					Cell R138cell1 = row.createCell(4);
					if (record.getR138_transactions() != null) {
					R138cell1.setCellValue(record.getR138_transactions().doubleValue());
					R138cell1.setCellStyle(numberStyle);
					} else {
					R138cell1.setCellValue("");
					R138cell1.setCellStyle(textStyle);
					}
					
		///////////////
					// R139
					// Column E
					row = sheet.getRow(138);
					Cell R139cell1 = row.createCell(4);
					if (record.getR139_transactions() != null) {
					R139cell1.setCellValue(record.getR139_transactions().doubleValue());
					R139cell1.setCellStyle(numberStyle);
					} else {
					R139cell1.setCellValue("");
					R139cell1.setCellStyle(textStyle);
					}
					
		///////////////
					// R144
					// Column F
					row = sheet.getRow(143);
					Cell R144cell1 = row.createCell(5);
					if (record.getR144_transactions() != null) {
					R144cell1.setCellValue(record.getR144_transactions().doubleValue());
					R144cell1.setCellStyle(numberStyle);
					} else {
					R144cell1.setCellValue("");
					R144cell1.setCellStyle(textStyle);
					}
					
		///////////////
					// R145
					// Column F
					row = sheet.getRow(144);
					Cell R145cell1 = row.createCell(5);
					if (record.getR145_transactions() != null) {
					R145cell1.setCellValue(record.getR145_transactions().doubleValue());
					R145cell1.setCellStyle(numberStyle);
					} else {
					R145cell1.setCellValue("");
					R145cell1.setCellStyle(textStyle);
					}
					
		///////////////
					// R146
					// Column F
					row = sheet.getRow(145);
					Cell R146cell1 = row.createCell(5);
					if (record.getR146_transactions() != null) {
					R146cell1.setCellValue(record.getR146_transactions().doubleValue());
					R146cell1.setCellStyle(numberStyle);
					} else {
					R146cell1.setCellValue("");
					R146cell1.setCellStyle(textStyle);
					}
					
		///////////////
					// R147
					// Column F
					row = sheet.getRow(146);
					Cell R147cell1 = row.createCell(5);
					if (record.getR147_transactions() != null) {
					R147cell1.setCellValue(record.getR147_transactions().doubleValue());
					R147cell1.setCellStyle(numberStyle);
					} else {
					R147cell1.setCellValue("");
					R147cell1.setCellStyle(textStyle);
					}
					
		///////////////
					// R148
					// Column F
					row = sheet.getRow(147);
					Cell R148cell1 = row.createCell(5);
					if (record.getR148_transactions() != null) {
					R148cell1.setCellValue(record.getR148_transactions().doubleValue());
					R148cell1.setCellStyle(numberStyle);
					} else {
					R148cell1.setCellValue("");
					R148cell1.setCellStyle(textStyle);
					}
					
					
		///////////////
					// R153
					// Column C
					row = sheet.getRow(152);
					Cell R153cell1 = row.createCell(2);
					if (record.getR153_no_of_customer() != null) {
					R153cell1.setCellValue(record.getR153_no_of_customer().doubleValue());
					R153cell1.setCellStyle(numberStyle);
					} else {
					R153cell1.setCellValue("");
					R153cell1.setCellStyle(textStyle);
					}
															
					// R153
					// Column D
					Cell R153cell2 = row.createCell(3);
					if (record.getR153_outstanding() != null) {
					R153cell2.setCellValue(record.getR153_outstanding().doubleValue());
					R153cell2.setCellStyle(numberStyle);
					} else {
					R153cell2.setCellValue("");
					R153cell2.setCellStyle(textStyle);
					}

					// R153
					// Column E
					Cell R153cell3 = row.createCell(4);
					if (record.getR153_turnover() != null) {
					R153cell3.setCellValue(record.getR153_turnover().doubleValue());
					R153cell3.setCellStyle(numberStyle);
					} else {
					R153cell3.setCellValue("");
					R153cell3.setCellStyle(textStyle);
					}

					
		///////////////
					// R154
					// Column C
					row = sheet.getRow(153);
					Cell R154cell1 = row.createCell(2);
					if (record.getR154_no_of_customer() != null) {
					R154cell1.setCellValue(record.getR154_no_of_customer().doubleValue());
					R154cell1.setCellStyle(numberStyle);
					} else {
					R154cell1.setCellValue("");
					R154cell1.setCellStyle(textStyle);
					}
															
					// R154
					// Column D
					Cell R154cell2 = row.createCell(3);
					if (record.getR154_outstanding() != null) {
					R154cell2.setCellValue(record.getR154_outstanding().doubleValue());
					R154cell2.setCellStyle(numberStyle);
					} else {
					R154cell2.setCellValue("");
					R154cell2.setCellStyle(textStyle);
					}

					// R154
					// Column E
					Cell R154cell3 = row.createCell(4);
					if (record.getR154_turnover() != null) {
					R154cell3.setCellValue(record.getR154_turnover().doubleValue());
					R154cell3.setCellStyle(numberStyle);
					} else {
					R154cell3.setCellValue("");
					R154cell3.setCellStyle(textStyle);
					}

		///////////////
					// R155
					// Column C
					row = sheet.getRow(154);
					Cell R155cell1 = row.createCell(2);
					if (record.getR155_no_of_customer() != null) {
					R155cell1.setCellValue(record.getR155_no_of_customer().doubleValue());
					R155cell1.setCellStyle(numberStyle);
					} else {
					R155cell1.setCellValue("");
					R155cell1.setCellStyle(textStyle);
					}
															
					// R155
					// Column D
					Cell R155cell2 = row.createCell(3);
					if (record.getR155_outstanding() != null) {
					R155cell2.setCellValue(record.getR155_outstanding().doubleValue());
					R155cell2.setCellStyle(numberStyle);
					} else {
					R155cell2.setCellValue("");
					R155cell2.setCellStyle(textStyle);
					}

					// R155
					// Column E
					Cell R155cell3 = row.createCell(4);
					if (record.getR155_turnover() != null) {
					R155cell3.setCellValue(record.getR155_turnover().doubleValue());
					R155cell3.setCellStyle(numberStyle);
					} else {
					R155cell3.setCellValue("");
					R155cell3.setCellStyle(textStyle);
					}

					
		///////////////
					// R161
					// Column C
					row = sheet.getRow(160);
					Cell R161cell1 = row.createCell(2);
					if (record.getR161_no_of_customer() != null) {
					R161cell1.setCellValue(record.getR161_no_of_customer().doubleValue());
					R161cell1.setCellStyle(numberStyle);
					} else {
					R161cell1.setCellValue("");
					R161cell1.setCellStyle(textStyle);
					}
															


					// R161
					// Column E
					Cell R161cell3 = row.createCell(4);
					if (record.getR161_commitment() != null) {
					R161cell3.setCellValue(record.getR161_commitment().doubleValue());
					R161cell3.setCellStyle(numberStyle);
					} else {
					R161cell3.setCellValue("");
					R161cell3.setCellStyle(textStyle);
					}


		///////////////
					// R162
					// Column C
					row = sheet.getRow(161);
					Cell R162cell1 = row.createCell(2);
					if (record.getR162_no_of_customer() != null) {
					R162cell1.setCellValue(record.getR162_no_of_customer().doubleValue());
					R162cell1.setCellStyle(numberStyle);
					} else {
					R162cell1.setCellValue("");
					R162cell1.setCellStyle(textStyle);
					}
															


					// R162
					// Column E
					Cell R162cell3 = row.createCell(4);
					if (record.getR162_commitment() != null) {
					R162cell3.setCellValue(record.getR162_commitment().doubleValue());
					R162cell3.setCellStyle(numberStyle);
					} else {
					R162cell3.setCellValue("");
					R162cell3.setCellStyle(textStyle);
					}


		///////////////
					// R163
					// Column C
					row = sheet.getRow(162);
					Cell R163cell1 = row.createCell(2);
					if (record.getR163_no_of_customer() != null) {
					R163cell1.setCellValue(record.getR163_no_of_customer().doubleValue());
					R163cell1.setCellStyle(numberStyle);
					} else {
					R163cell1.setCellValue("");
					R163cell1.setCellStyle(textStyle);
					}
															


					// R163
					// Column E
					Cell R163cell3 = row.createCell(4);
					if (record.getR163_commitment() != null) {
					R163cell3.setCellValue(record.getR163_commitment().doubleValue());
					R163cell3.setCellStyle(numberStyle);
					} else {
					R163cell3.setCellValue("");
					R163cell3.setCellStyle(textStyle);
					}


		///////////////
					// R170
					// Column E
					row = sheet.getRow(169);
					Cell R170cell1 = row.createCell(4);
					if (record.getR170_no_of_transfer() != null) {
					R170cell1.setCellValue(record.getR170_no_of_transfer().doubleValue());
					R170cell1.setCellStyle(numberStyle);
					} else {
					R170cell1.setCellValue("");
					R170cell1.setCellStyle(textStyle);
					}
															
					// R170
					// Column F
					Cell R170cell2 = row.createCell(5);
					if (record.getR170_value_of_transfer() != null) {
					R170cell2.setCellValue(record.getR170_value_of_transfer().doubleValue());
					R170cell2.setCellStyle(numberStyle);
					} else {
					R170cell2.setCellValue("");
					R170cell2.setCellStyle(textStyle);
					}
					
		///////////////
					// R171
					// Column E
					row = sheet.getRow(170);
					Cell R171cell1 = row.createCell(4);
					if (record.getR171_no_of_transfer() != null) {
					R171cell1.setCellValue(record.getR171_no_of_transfer().doubleValue());
					R171cell1.setCellStyle(numberStyle);
					} else {
					R171cell1.setCellValue("");
					R171cell1.setCellStyle(textStyle);
					}
															
					// R171
					// Column F
					Cell R171cell2 = row.createCell(5);
					if (record.getR171_value_of_transfer() != null) {
					R171cell2.setCellValue(record.getR171_value_of_transfer().doubleValue());
					R171cell2.setCellStyle(numberStyle);
					} else {
					R171cell2.setCellValue("");
					R171cell2.setCellStyle(textStyle);
					}
					
					
		///////////////
					// R172
					// Column E
					row = sheet.getRow(171);
					Cell R172cell1 = row.createCell(4);
					if (record.getR172_no_of_transfer() != null) {
					R172cell1.setCellValue(record.getR172_no_of_transfer().doubleValue());
					R172cell1.setCellStyle(numberStyle);
					} else {
					R172cell1.setCellValue("");
					R172cell1.setCellStyle(textStyle);
					}
															
					// R172
					// Column F
					Cell R172cell2 = row.createCell(5);
					if (record.getR172_value_of_transfer() != null) {
					R172cell2.setCellValue(record.getR172_value_of_transfer().doubleValue());
					R172cell2.setCellStyle(numberStyle);
					} else {
					R172cell2.setCellValue("");
					R172cell2.setCellStyle(textStyle);
					}


		///////////////
					// R179
					// Column F
					row = sheet.getRow(178);
					Cell R179cell1 = row.createCell(5);
					if (record.getR179_no_of_transaction() != null) {
					R179cell1.setCellValue(record.getR179_no_of_transaction().doubleValue());
					R179cell1.setCellStyle(numberStyle);
					} else {
					R179cell1.setCellValue("");
					R179cell1.setCellStyle(textStyle);
					}
															


					
		///////////////
					// R180
					// Column F
					row = sheet.getRow(179);
					Cell R180cell1 = row.createCell(5);
					if (record.getR180_no_of_transaction() != null) {
					R180cell1.setCellValue(record.getR180_no_of_transaction().doubleValue());
					R180cell1.setCellStyle(numberStyle);
					} else {
					R180cell1.setCellValue("");
					R180cell1.setCellStyle(textStyle);
					}
															


		///////////////
					// R181
					// Column F
					row = sheet.getRow(180);
					Cell R181cell1 = row.createCell(5);
					if (record.getR181_no_of_transaction() != null) {
					R181cell1.setCellValue(record.getR181_no_of_transaction().doubleValue());
					R181cell1.setCellStyle(numberStyle);
					} else {
					R181cell1.setCellValue("");
					R181cell1.setCellStyle(textStyle);
					}
															

		///////////////
					// R187
					// Column E
					row = sheet.getRow(186);
					Cell R187cell1 = row.createCell(4);
					if (record.getR187_no_of_transaction() != null) {
					R187cell1.setCellValue(record.getR187_no_of_transaction().doubleValue());
					R187cell1.setCellStyle(numberStyle);
					} else {
					R187cell1.setCellValue("");
					R187cell1.setCellStyle(textStyle);
					}
															
					// R187
					// Column F
					Cell R187cell2 = row.createCell(5);
					if (record.getR187_value_of_transaction() != null) {
					R187cell2.setCellValue(record.getR187_value_of_transaction().doubleValue());
					R187cell2.setCellStyle(numberStyle);
					} else {
					R187cell2.setCellValue("");
					R187cell2.setCellStyle(textStyle);
					}


		///////////////
					// R192
					// Column E
					row = sheet.getRow(191);
					Cell R192cell1 = row.createCell(4);
					if (record.getR192_no_of_transaction() != null) {
					R192cell1.setCellValue(record.getR192_no_of_transaction().doubleValue());
					R192cell1.setCellStyle(numberStyle);
					} else {
					R192cell1.setCellValue("");
					R192cell1.setCellStyle(textStyle);
					}
															
					// R192
					// Column F
					Cell R192cell2 = row.createCell(5);
					if (record.getR192_value_of_transaction() != null) {
					R192cell2.setCellValue(record.getR192_value_of_transaction().doubleValue());
					R192cell2.setCellStyle(numberStyle);
					} else {
					R192cell2.setCellValue("");
					R192cell2.setCellStyle(textStyle);
					}

		///////////////
					// R197
					// Column E
					row = sheet.getRow(196);
					Cell R197cell1 = row.createCell(4);
					if (record.getR196_no_of_transaction() != null) {
					R197cell1.setCellValue(record.getR196_no_of_transaction().doubleValue());
					R197cell1.setCellStyle(numberStyle);
					} else {
					R197cell1.setCellValue("");
					R197cell1.setCellStyle(textStyle);
					}
															
					// R197
					// Column F
					Cell R197cell2 = row.createCell(5);
					if (record.getR196_value_of_transaction() != null) {
					R197cell2.setCellValue(record.getR196_value_of_transaction().doubleValue());
					R197cell2.setCellStyle(numberStyle);
					} else {
					R197cell2.setCellValue("");
					R197cell2.setCellStyle(textStyle);
					}

					
		///////////////
					// R201
					// Column E
					row = sheet.getRow(200);
					Cell R201cell1 = row.createCell(4);
					if (record.getR201_no_of_transaction() != null) {
					R201cell1.setCellValue(record.getR201_no_of_transaction().doubleValue());
					R201cell1.setCellStyle(numberStyle);
					} else {
					R201cell1.setCellValue("");
					R201cell1.setCellStyle(textStyle);
					}
															
					// R201
					// Column F
					Cell R201cell2 = row.createCell(5);
					if (record.getR201_value_of_transaction() != null) {
					R201cell2.setCellValue(record.getR201_value_of_transaction().doubleValue());
					R201cell2.setCellStyle(numberStyle);
					} else {
					R201cell2.setCellValue("");
					R201cell2.setCellStyle(textStyle);
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

	public byte[] getMASTERDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for MASTER Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("MASTERDetails");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "PROVISION AMOUNT", "ROWID", "COLUMNID",
					"REPORT_DATE" };

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
			List<MASTER_Detail_Entity> reportData = BRRS_MASTER_Detail_Repo.getdatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (MASTER_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCust_id());
					row.createCell(1).setCellValue(item.getAcct_number());
					row.createCell(2).setCellValue(item.getAcct_name());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcct_balance_in_pula() != null) {
						balanceCell.setCellValue(item.getAcct_balance_in_pula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReport_label());
					row.createCell(5).setCellValue(item.getReport_addl_criteria_1());
					row.createCell(6)
							.setCellValue(item.getReport_date() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReport_date())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for MASTER — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating MASTER Excel", e);
			return new byte[0];
		}
	}

	public List<Object> getMASTERArchival() {
		List<Object> MASTERArchivallist = new ArrayList<>();
		//List<Object> MASTERArchivallist1 = new ArrayList<>();
		try {
			MASTERArchivallist = brrs_MASTER_Archival_Summary_Repo.getMASTERarchival();
			System.out.println("countser" + MASTERArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching MASTER Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return MASTERArchivallist;
	}

	public byte[] getExcelMASTERARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<MASTER_Archival_Summary_Entity> dataList = brrs_MASTER_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for MASTER report. Returning empty result.");
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
			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					MASTER_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
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

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_MASTER ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("MASTERDetails");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "PROVISION AMOUNT", "ROWID", "COLUMNID",
					"REPORT_DATE" };

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
			List<MASTER_Archival_Detail_Entity> reportData = brrs_MASTER_Archival_Detail_Repo
					.getdatabydateList(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (MASTER_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCust_id());
					row.createCell(1).setCellValue(item.getAcct_number());
					row.createCell(2).setCellValue(item.getAcct_name());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcct_balance_in_pula() != null) {
						balanceCell.setCellValue(item.getAcct_balance_in_pula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReport_label());
					row.createCell(5).setCellValue(item.getReport_addl_criteria_1());
					row.createCell(6)
							.setCellValue(item.getReport_date() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReport_date())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for MASTER — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating MASTER Excel", e);
			return new byte[0];
		}
	}



	@Autowired
	private BRRS_MASTER_Detail_Repo MASTER_Detail_Repo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/MASTER");
		
		System.out.println("Came to view method");

		if (acctNo != null) {
			MASTER_Detail_Entity Entity = MASTER_Detail_Repo.findByAcctnumber(acctNo);
			if (Entity != null && Entity.getReport_date() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(Entity.getReport_date());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("Data", Entity);
		}
		
		else {
			System.out.println(acctNo);
		}


		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}
	
	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {

			String acctNo = request.getParameter("acct_number");
			String provisionStr = request.getParameter("acct_balance_in_pula");
			String acctName = request.getParameter("acct_name");
			String reportDateStr = request.getParameter("report_date");
			
			System.out.println(reportDateStr);


			logger.info("Received update for ACCT_NO: {}", acctNo);

			MASTER_Detail_Entity existing = MASTER_Detail_Repo.findByAcctnumber(acctNo);
			if (existing == null) {
				logger.warn("No record found for ACCT_NO: {}", acctNo);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}

			boolean isChanged = false;

			if (acctName != null && !acctName.isEmpty()) {
				if (existing.getAcct_name() == null || !existing.getAcct_name().equals(acctName)) {
					existing.setAcct_name(acctName);
					isChanged = true;
					logger.info("Account name updated to {}", acctName);
				}
			}

			if (provisionStr != null && !provisionStr.isEmpty()) {
				BigDecimal newProvision = new BigDecimal(provisionStr);
				if (existing.getAcct_balance_in_pula() == null
						|| existing.getAcct_balance_in_pula().compareTo(newProvision) != 0) {
					existing.setAcct_balance_in_pula(newProvision);
					isChanged = true;
					logger.info("Provision updated to {}", newProvision);
				}
			}
			if (isChanged) {
				MASTER_Detail_Repo.save(existing);
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_MASTER_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_MASTER_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating MASTER record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

}
