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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

import com.bornfire.brrs.entities.BRRS_MDISB1_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_MDISB1_Archival_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_MDISB1_Archival_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_MDISB1_Archival_Summary_Repo3;
import com.bornfire.brrs.entities.BRRS_MDISB1_Detail_Repo;
import com.bornfire.brrs.entities.MDISB1_Archival_Detail_Entity;
import com.bornfire.brrs.entities.MDISB1_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.MDISB1_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.MDISB1_Archival_Summary_Manual;
import com.bornfire.brrs.entities.MDISB1_Detail_Entity;
import com.bornfire.brrs.entities.MDISB1_Summary_Entity1;
import com.bornfire.brrs.entities.MDISB1_Summary_Entity2;
import com.bornfire.brrs.entities.MDISB1_Summary_Entity_Manual;
import com.bornfire.brrs.entities.MDISB1_Summary_Repo1;
import com.bornfire.brrs.entities.MDISB1_Summary_Repo2;
import com.bornfire.brrs.entities.MDISB1_Summary_Repo3;
import com.bornfire.brrs.entities.M_LA4_Summary_Entity2;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Summary_Entity1;

@Component
@Service

public class BRRS_MDISB1_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_MDISB1_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_MDISB1_Detail_Repo BRRS_MDISB1_Detail_Repo;

	@Autowired
	MDISB1_Summary_Repo1 MDISB1_Summary_repo1;

	@Autowired
	MDISB1_Summary_Repo2 MDISB1_Summary_repo2;

	@Autowired
	MDISB1_Summary_Repo3 MDISB1_Summary_repo3;

	@Autowired
	BRRS_MDISB1_Archival_Summary_Repo1 brrs_MDISB1_Archival_Summary_Repo1;

	@Autowired
	BRRS_MDISB1_Archival_Summary_Repo2 brrs_MDISB1_Archival_Summary_Repo2;

	@Autowired
	BRRS_MDISB1_Archival_Summary_Repo3 brrs_MDISB1_Archival_Summary_Repo3;

	@Autowired
	BRRS_MDISB1_Archival_Detail_Repo brrs_MDISB1_Archival_Detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getMDISB1View(String reportId, String fromdate, String todate, String currency, String dtltype,
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
			List<MDISB1_Archival_Summary_Entity1> T1Master = new ArrayList<MDISB1_Archival_Summary_Entity1>();
			List<MDISB1_Archival_Summary_Entity2> T1Master1 = new ArrayList<MDISB1_Archival_Summary_Entity2>();
			List<MDISB1_Archival_Summary_Manual> T1Master2 = new ArrayList<MDISB1_Archival_Summary_Manual>();
			System.out.println(version);
			try {
				Date d1 = dateformat.parse(todate);

				T1Master = brrs_MDISB1_Archival_Summary_Repo1.getdatabydateListarchival(dateformat.parse(todate),
						version);

				T1Master1 = brrs_MDISB1_Archival_Summary_Repo2.getdatabydateListarchival(dateformat.parse(todate),
						version);

				T1Master2 = brrs_MDISB1_Archival_Summary_Repo3.getdatabydateListarchival(dateformat.parse(todate),
						version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T1Master1);
			mv.addObject("reportsummary2", T1Master2);
		} else {

			List<MDISB1_Summary_Entity1> T1Master = new ArrayList<MDISB1_Summary_Entity1>();
			List<MDISB1_Summary_Entity2> T1Master1 = new ArrayList<MDISB1_Summary_Entity2>();
			List<MDISB1_Summary_Entity_Manual> T1Master2 = new ArrayList<MDISB1_Summary_Entity_Manual>();
			try {
				Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = MDISB1_Summary_repo1.getdatabydateList(dateformat.parse(todate));
				T1Master1 = MDISB1_Summary_repo2.getdatabydateList(dateformat.parse(todate));
				T1Master2 = MDISB1_Summary_repo3.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T1Master1);
			mv.addObject("reportsummary2", T1Master2);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

		mv.setViewName("BRRS/MDISB1");

		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

	public ModelAndView getMDISB1currentDtl(String reportId, String fromdate, String todate, String currency,
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
				List<MDISB1_Archival_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = brrs_MDISB1_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate,
							version);
				} else {
					T1Dt1 = brrs_MDISB1_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<MDISB1_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BRRS_MDISB1_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
				} else {
					T1Dt1 = BRRS_MDISB1_Detail_Repo.getdatabydateList(parsedDate);
					totalPages = BRRS_MDISB1_Detail_Repo.getdatacount(parsedDate);
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
		mv.setViewName("BRRS/MDISB1");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}

	public byte[] getMDISB1Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelMDISB1ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data
		List<MDISB1_Summary_Entity1> dataList = MDISB1_Summary_repo1.getdatabydateList(dateformat.parse(todate));
		List<MDISB1_Summary_Entity2> dataList1 = MDISB1_Summary_repo2.getdatabydateList(dateformat.parse(todate));
		List<MDISB1_Summary_Entity_Manual> dataList2 = MDISB1_Summary_repo3.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for MDISB1 report. Returning empty result.");
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

			int startRow = 6;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					MDISB1_Summary_Entity1 record = dataList.get(i);
					MDISB1_Summary_Entity2 record1 = dataList1.get(i);
					MDISB1_Summary_Entity_Manual record2 = dataList2.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R7
					// Column C
					Cell cell1 = row.createCell(2);
					if (record.getR7_deposit_excluding_number() != null) {
						cell1.setCellValue(record.getR7_deposit_excluding_number().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell cell2 = row.createCell(3);
					if (record.getR7_deposit_excluding_amount() != null) {
						cell2.setCellValue(record.getR7_deposit_excluding_amount().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell cell3 = row.createCell(4);
					if (record.getR7_deposit_foreign_number() != null) {
						cell3.setCellValue(record.getR7_deposit_foreign_number().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column F
					Cell cell4 = row.createCell(5);
					if (record.getR7_deposit_foreign_amount() != null) {
						cell4.setCellValue(record.getR7_deposit_foreign_amount().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell cell5 = row.createCell(8);
					if (record2.getR7_TOTAL_DEPOSIT_EXCEED() != null) {
						cell5.setCellValue(record2.getR7_TOTAL_DEPOSIT_EXCEED().doubleValue());
						} else {
						cell5.setCellValue("");
						
					}
					
					// Column J
					Cell cell6 = row.createCell(9);
					if (record.getR7_total_deposit_bank() != null) {
						cell6.setCellValue(record.getR7_total_deposit_bank().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// R8
					row = sheet.getRow(7);
					// Column C
					Cell R8cell1 = row.createCell(2);
					if (record.getR8_deposit_excluding_number() != null) {
						R8cell1.setCellValue(record.getR8_deposit_excluding_number().doubleValue());
						R8cell1.setCellStyle(numberStyle);
					} else {
						R8cell1.setCellValue("");
						R8cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R8cell2 = row.createCell(3);
					if (record.getR8_deposit_excluding_amount() != null) {
						R8cell2.setCellValue(record.getR8_deposit_excluding_amount().doubleValue());
						R8cell2.setCellStyle(numberStyle);
					} else {
						R8cell2.setCellValue("");
						R8cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R8cell3 = row.createCell(4);
					if (record.getR8_deposit_foreign_number() != null) {
						R8cell3.setCellValue(record.getR8_deposit_foreign_number().doubleValue());
						R8cell3.setCellStyle(numberStyle);
					} else {
						R8cell3.setCellValue("");
						R8cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R8cell4 = row.createCell(5);
					if (record.getR8_deposit_foreign_amount() != null) {
						R8cell4.setCellValue(record.getR8_deposit_foreign_amount().doubleValue());
						R8cell4.setCellStyle(numberStyle);
					} else {
						R8cell4.setCellValue("");
						R8cell4.setCellStyle(textStyle);
					}
					// Column I
					Cell R8cell5 = row.createCell(8);
					if (record2.getR8_TOTAL_DEPOSIT_EXCEED() != null) {
					R8cell5.setCellValue(record2.getR8_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R8cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R8cell6 = row.createCell(9);
					if (record.getR8_total_deposit_bank() != null) {
					R8cell6.setCellValue(record.getR8_total_deposit_bank().doubleValue());
					R8cell6.setCellStyle(numberStyle);
					} else {
					R8cell6.setCellValue("");
					R8cell6.setCellStyle(textStyle);
					}

					// R9
					row = sheet.getRow(8);
					// Column C
					Cell R9cell1 = row.createCell(2);
					if (record.getR9_deposit_excluding_number() != null) {
						R9cell1.setCellValue(record.getR9_deposit_excluding_number().doubleValue());
						R9cell1.setCellStyle(numberStyle);
					} else {
						R9cell1.setCellValue("");
						R9cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R9cell2 = row.createCell(3);
					if (record.getR9_deposit_excluding_amount() != null) {
						R9cell2.setCellValue(record.getR9_deposit_excluding_amount().doubleValue());
						R9cell2.setCellStyle(numberStyle);
					} else {
						R9cell2.setCellValue("");
						R9cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R9cell3 = row.createCell(4);
					if (record.getR9_deposit_foreign_number() != null) {
						R9cell3.setCellValue(record.getR9_deposit_foreign_number().doubleValue());
						R9cell3.setCellStyle(numberStyle);
					} else {
						R9cell3.setCellValue("");
						R9cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R9cell4 = row.createCell(5);
					if (record.getR9_deposit_foreign_amount() != null) {
						R9cell4.setCellValue(record.getR9_deposit_foreign_amount().doubleValue());
						R9cell4.setCellStyle(numberStyle);
					} else {
						R9cell4.setCellValue("");
						R9cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R9cell5 = row.createCell(8);
					if (record2.getR9_TOTAL_DEPOSIT_EXCEED() != null) {
					R9cell5.setCellValue(record2.getR9_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R9cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R9cell6 = row.createCell(9);
					if (record.getR9_total_deposit_bank() != null) {
					R9cell6.setCellValue(record.getR9_total_deposit_bank().doubleValue());
					R9cell6.setCellStyle(numberStyle);
					} else {
					R9cell6.setCellValue("");
					R9cell6.setCellStyle(textStyle);
					}

					// R10
					row = sheet.getRow(9);
					// Column C
					Cell R10cell1 = row.createCell(2);
					if (record.getR10_deposit_excluding_number() != null) {
						R10cell1.setCellValue(record.getR10_deposit_excluding_number().doubleValue());
						R10cell1.setCellStyle(numberStyle);
					} else {
						R10cell1.setCellValue("");
						R10cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R10cell2 = row.createCell(3);
					if (record.getR10_deposit_excluding_amount() != null) {
						R10cell2.setCellValue(record.getR10_deposit_excluding_amount().doubleValue());
						R10cell2.setCellStyle(numberStyle);
					} else {
						R10cell2.setCellValue("");
						R10cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R10cell3 = row.createCell(4);
					if (record.getR10_deposit_foreign_number() != null) {
						R10cell3.setCellValue(record.getR10_deposit_foreign_number().doubleValue());
						R10cell3.setCellStyle(numberStyle);
					} else {
						R10cell3.setCellValue("");
						R10cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R10cell4 = row.createCell(5);
					if (record.getR10_deposit_foreign_amount() != null) {
						R10cell4.setCellValue(record.getR10_deposit_foreign_amount().doubleValue());
						R10cell4.setCellStyle(numberStyle);
					} else {
						R10cell4.setCellValue("");
						R10cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R10cell5 = row.createCell(8);
					if (record2.getR10_TOTAL_DEPOSIT_EXCEED() != null) {
					R10cell5.setCellValue(record2.getR10_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R10cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R10cell6 = row.createCell(9);
					if (record.getR10_total_deposit_bank() != null) {
					R10cell6.setCellValue(record.getR10_total_deposit_bank().doubleValue());
					R10cell6.setCellStyle(numberStyle);
					} else {
					R10cell6.setCellValue("");
					R10cell6.setCellStyle(textStyle);
					}

					
					// R11
					row = sheet.getRow(10);
					// Column C
					Cell R11cell1 = row.createCell(2);
					if (record.getR11_deposit_excluding_number() != null) {
						R11cell1.setCellValue(record.getR11_deposit_excluding_number().doubleValue());
						R11cell1.setCellStyle(numberStyle);
					} else {
						R11cell1.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R11cell2 = row.createCell(3);
					if (record.getR11_deposit_excluding_amount() != null) {
						R11cell2.setCellValue(record.getR11_deposit_excluding_amount().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R11cell3 = row.createCell(4);
					if (record.getR11_deposit_foreign_number() != null) {
						R11cell3.setCellValue(record.getR11_deposit_foreign_number().doubleValue());
						R11cell3.setCellStyle(numberStyle);
					} else {
						R11cell3.setCellValue("");
						R11cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R11cell4 = row.createCell(5);
					if (record.getR11_deposit_foreign_amount() != null) {
						R11cell4.setCellValue(record.getR11_deposit_foreign_amount().doubleValue());
						R11cell4.setCellStyle(numberStyle);
					} else {
						R11cell4.setCellValue("");
						R11cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R11cell5 = row.createCell(8);
					if (record2.getR11_TOTAL_DEPOSIT_EXCEED() != null) {
					R11cell5.setCellValue(record2.getR11_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R11cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R11cell6 = row.createCell(9);
					if (record.getR11_total_deposit_bank() != null) {
					R11cell6.setCellValue(record.getR11_total_deposit_bank().doubleValue());
					R11cell6.setCellStyle(numberStyle);
					} else {
					R11cell6.setCellValue("");
					R11cell6.setCellStyle(textStyle);
					}


					// R12
					row = sheet.getRow(11);
					// Column C
					Cell R12cell1 = row.createCell(2);
					if (record.getR12_deposit_excluding_number() != null) {
						R12cell1.setCellValue(record.getR12_deposit_excluding_number().doubleValue());
						R12cell1.setCellStyle(numberStyle);
					} else {
						R12cell1.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R12cell2 = row.createCell(3);
					if (record.getR12_deposit_excluding_amount() != null) {
						R12cell2.setCellValue(record.getR12_deposit_excluding_amount().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R12cell3 = row.createCell(4);
					if (record.getR12_deposit_foreign_number() != null) {
						R12cell3.setCellValue(record.getR12_deposit_foreign_number().doubleValue());
						R12cell3.setCellStyle(numberStyle);
					} else {
						R12cell3.setCellValue("");
						R12cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R12cell4 = row.createCell(5);
					if (record.getR12_deposit_foreign_amount() != null) {
						R12cell4.setCellValue(record.getR12_deposit_foreign_amount().doubleValue());
						R12cell4.setCellStyle(numberStyle);
					} else {
						R12cell4.setCellValue("");
						R12cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R12cell5 = row.createCell(8);
					if (record2.getR12_TOTAL_DEPOSIT_EXCEED() != null) {
					R12cell5.setCellValue(record2.getR12_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R12cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R12cell6 = row.createCell(9);
					if (record.getR12_total_deposit_bank() != null) {
					R12cell6.setCellValue(record.getR12_total_deposit_bank().doubleValue());
					R12cell6.setCellStyle(numberStyle);
					} else {
					R12cell6.setCellValue("");
					R12cell6.setCellStyle(textStyle);
					}

					// R15
					row = sheet.getRow(14);
					// Column C
					Cell R15cell1 = row.createCell(2);
					if (record.getR15_deposit_excluding_number() != null) {
						R15cell1.setCellValue(record.getR15_deposit_excluding_number().doubleValue());
						R15cell1.setCellStyle(numberStyle);
					} else {
						R15cell1.setCellValue("");
						R15cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R15cell2 = row.createCell(3);
					if (record.getR15_deposit_excluding_amount() != null) {
						R15cell2.setCellValue(record.getR15_deposit_excluding_amount().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R15cell3 = row.createCell(4);
					if (record.getR15_deposit_foreign_number() != null) {
						R15cell3.setCellValue(record.getR15_deposit_foreign_number().doubleValue());
						R15cell3.setCellStyle(numberStyle);
					} else {
						R15cell3.setCellValue("");
						R15cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R15cell4 = row.createCell(5);
					if (record.getR15_deposit_foreign_amount() != null) {
						R15cell4.setCellValue(record.getR15_deposit_foreign_amount().doubleValue());
						R15cell4.setCellStyle(numberStyle);
					} else {
						R15cell4.setCellValue("");
						R15cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R15cell5 = row.createCell(8);
					if (record2.getR15_TOTAL_DEPOSIT_EXCEED() != null) {
					R15cell5.setCellValue(record2.getR15_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R15cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R15cell6 = row.createCell(9);
					if (record.getR15_total_deposit_bank() != null) {
					R15cell6.setCellValue(record.getR15_total_deposit_bank().doubleValue());
					R15cell6.setCellStyle(numberStyle);
					} else {
					R15cell6.setCellValue("");
					R15cell6.setCellStyle(textStyle);
					}

					// R16
					row = sheet.getRow(15);
					// Column C
					Cell R16cell1 = row.createCell(2);
					if (record.getR16_deposit_excluding_number() != null) {
						R16cell1.setCellValue(record.getR16_deposit_excluding_number().doubleValue());
						R16cell1.setCellStyle(numberStyle);
					} else {
						R16cell1.setCellValue("");
						R16cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R16cell2 = row.createCell(3);
					if (record.getR16_deposit_excluding_amount() != null) {
						R16cell2.setCellValue(record.getR16_deposit_excluding_amount().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R16cell3 = row.createCell(4);
					if (record.getR16_deposit_foreign_number() != null) {
						R16cell3.setCellValue(record.getR16_deposit_foreign_number().doubleValue());
						R16cell3.setCellStyle(numberStyle);
					} else {
						R16cell3.setCellValue("");
						R16cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R16cell4 = row.createCell(5);
					if (record.getR16_deposit_foreign_amount() != null) {
						R16cell4.setCellValue(record.getR16_deposit_foreign_amount().doubleValue());
						R16cell4.setCellStyle(numberStyle);
					} else {
						R16cell4.setCellValue("");
						R16cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R16cell5 = row.createCell(8);
					if (record2.getR16_TOTAL_DEPOSIT_EXCEED() != null) {
					R16cell5.setCellValue(record2.getR16_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R16cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R16cell6 = row.createCell(9);
					if (record.getR16_total_deposit_bank() != null) {
					R16cell6.setCellValue(record.getR16_total_deposit_bank().doubleValue());
					R16cell6.setCellStyle(numberStyle);
					} else {
					R16cell6.setCellValue("");
					R16cell6.setCellStyle(textStyle);
					}


					// R17
					row = sheet.getRow(16);
					// Column C
					Cell R17cell1 = row.createCell(2);
					if (record.getR17_deposit_excluding_number() != null) {
						R17cell1.setCellValue(record.getR17_deposit_excluding_number().doubleValue());
						R17cell1.setCellStyle(numberStyle);
					} else {
						R17cell1.setCellValue("");
						R17cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R17cell2 = row.createCell(3);
					if (record.getR17_deposit_excluding_amount() != null) {
						R17cell2.setCellValue(record.getR17_deposit_excluding_amount().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R17cell3 = row.createCell(4);
					if (record.getR17_deposit_foreign_number() != null) {
						R17cell3.setCellValue(record.getR17_deposit_foreign_number().doubleValue());
						R17cell3.setCellStyle(numberStyle);
					} else {
						R17cell3.setCellValue("");
						R17cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R17cell4 = row.createCell(5);
					if (record.getR17_deposit_foreign_amount() != null) {
						R17cell4.setCellValue(record.getR17_deposit_foreign_amount().doubleValue());
						R17cell4.setCellStyle(numberStyle);
					} else {
						R17cell4.setCellValue("");
						R17cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R17cell5 = row.createCell(8);
					if (record2.getR17_TOTAL_DEPOSIT_EXCEED() != null) {
					R17cell5.setCellValue(record2.getR17_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R17cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R17cell6 = row.createCell(9);
					if (record.getR17_total_deposit_bank() != null) {
					R17cell6.setCellValue(record.getR17_total_deposit_bank().doubleValue());
					R17cell6.setCellStyle(numberStyle);
					} else {
					R17cell6.setCellValue("");
					R17cell6.setCellStyle(textStyle);
					}


					// R18
					row = sheet.getRow(17);
					// Column C
					Cell R18cell1 = row.createCell(2);
					if (record.getR18_deposit_excluding_number() != null) {
						R18cell1.setCellValue(record.getR18_deposit_excluding_number().doubleValue());
						R18cell1.setCellStyle(numberStyle);
					} else {
						R18cell1.setCellValue("");
						R18cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R18cell2 = row.createCell(3);
					if (record.getR18_deposit_excluding_amount() != null) {
						R18cell2.setCellValue(record.getR18_deposit_excluding_amount().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R18cell3 = row.createCell(4);
					if (record.getR18_deposit_foreign_number() != null) {
						R18cell3.setCellValue(record.getR18_deposit_foreign_number().doubleValue());
						R18cell3.setCellStyle(numberStyle);
					} else {
						R18cell3.setCellValue("");
						R18cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R18cell4 = row.createCell(5);
					if (record.getR18_deposit_foreign_amount() != null) {
						R18cell4.setCellValue(record.getR18_deposit_foreign_amount().doubleValue());
						R18cell4.setCellStyle(numberStyle);
					} else {
						R18cell4.setCellValue("");
						R18cell4.setCellStyle(textStyle);
					}
					// Column I
					Cell R18cell5 = row.createCell(8);
					if (record2.getR18_TOTAL_DEPOSIT_EXCEED() != null) {
					R18cell5.setCellValue(record2.getR18_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R18cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R18cell6 = row.createCell(9);
					if (record.getR18_total_deposit_bank() != null) {
					R18cell6.setCellValue(record.getR18_total_deposit_bank().doubleValue());
					R18cell6.setCellStyle(numberStyle);
					} else {
					R18cell6.setCellValue("");
					R18cell6.setCellStyle(textStyle);
					}


					// R19
					row = sheet.getRow(18);
					// Column C
					Cell R19cell1 = row.createCell(2);
					if (record.getR19_deposit_excluding_number() != null) {
						R19cell1.setCellValue(record.getR19_deposit_excluding_number().doubleValue());
						R19cell1.setCellStyle(numberStyle);
					} else {
						R19cell1.setCellValue("");
						R19cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R19cell2 = row.createCell(3);
					if (record.getR19_deposit_excluding_amount() != null) {
						R19cell2.setCellValue(record.getR19_deposit_excluding_amount().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R19cell3 = row.createCell(4);
					if (record.getR19_deposit_foreign_number() != null) {
						R19cell3.setCellValue(record.getR19_deposit_foreign_number().doubleValue());
						R19cell3.setCellStyle(numberStyle);
					} else {
						R19cell3.setCellValue("");
						R19cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R19cell4 = row.createCell(5);
					if (record.getR19_deposit_foreign_amount() != null) {
						R19cell4.setCellValue(record.getR19_deposit_foreign_amount().doubleValue());
						R19cell4.setCellStyle(numberStyle);
					} else {
						R19cell4.setCellValue("");
						R19cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R19cell5 = row.createCell(8);
					if (record2.getR19_TOTAL_DEPOSIT_EXCEED() != null) {
					R19cell5.setCellValue(record2.getR19_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R19cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R19cell6 = row.createCell(9);
					if (record.getR19_total_deposit_bank() != null) {
					R19cell6.setCellValue(record.getR19_total_deposit_bank().doubleValue());
					R19cell6.setCellStyle(numberStyle);
					} else {
					R19cell6.setCellValue("");
					R19cell6.setCellStyle(textStyle);
					}


					// R20
					row = sheet.getRow(19);
					// Column C
					Cell R20cell1 = row.createCell(2);
					if (record.getR20_deposit_excluding_number() != null) {
						R20cell1.setCellValue(record.getR20_deposit_excluding_number().doubleValue());
						R20cell1.setCellStyle(numberStyle);
					} else {
						R20cell1.setCellValue("");
						R20cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R20cell2 = row.createCell(3);
					if (record.getR20_deposit_excluding_amount() != null) {
						R20cell2.setCellValue(record.getR20_deposit_excluding_amount().doubleValue());
						R20cell2.setCellStyle(numberStyle);
					} else {
						R20cell2.setCellValue("");
						R20cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R20cell3 = row.createCell(4);
					if (record.getR20_deposit_foreign_number() != null) {
						R20cell3.setCellValue(record.getR20_deposit_foreign_number().doubleValue());
						R20cell3.setCellStyle(numberStyle);
					} else {
						R20cell3.setCellValue("");
						R20cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R20cell4 = row.createCell(5);
					if (record.getR20_deposit_foreign_amount() != null) {
						R20cell4.setCellValue(record.getR20_deposit_foreign_amount().doubleValue());
						R20cell4.setCellStyle(numberStyle);
					} else {
						R20cell4.setCellValue("");
						R20cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R20cell5 = row.createCell(8);
					if (record2.getR20_TOTAL_DEPOSIT_EXCEED() != null) {
					R20cell5.setCellValue(record2.getR20_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R20cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R20cell6 = row.createCell(9);
					if (record.getR20_total_deposit_bank() != null) {
					R20cell6.setCellValue(record.getR20_total_deposit_bank().doubleValue());
					R20cell6.setCellStyle(numberStyle);
					} else {
					R20cell6.setCellValue("");
					R20cell6.setCellStyle(textStyle);
					}

					// R23
					row = sheet.getRow(22);
					// Column C
					Cell R23cell1 = row.createCell(2);
					if (record.getR23_deposit_excluding_number() != null) {
						R23cell1.setCellValue(record.getR23_deposit_excluding_number().doubleValue());
						R23cell1.setCellStyle(numberStyle);
					} else {
						R23cell1.setCellValue("");
						R23cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R23cell2 = row.createCell(3);
					if (record.getR23_deposit_excluding_amount() != null) {
						R23cell2.setCellValue(record.getR23_deposit_excluding_amount().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R23cell3 = row.createCell(4);
					if (record.getR23_deposit_foreign_number() != null) {
						R23cell3.setCellValue(record.getR23_deposit_foreign_number().doubleValue());
						R23cell3.setCellStyle(numberStyle);
					} else {
						R23cell3.setCellValue("");
						R23cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R23cell4 = row.createCell(5);
					if (record.getR23_deposit_foreign_amount() != null) {
						R23cell4.setCellValue(record.getR23_deposit_foreign_amount().doubleValue());
						R23cell4.setCellStyle(numberStyle);
					} else {
						R23cell4.setCellValue("");
						R23cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R23cell5 = row.createCell(8);
					if (record2.getR23_TOTAL_DEPOSIT_EXCEED() != null) {
					R23cell5.setCellValue(record2.getR23_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R23cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R23cell6 = row.createCell(9);
					if (record.getR23_total_deposit_bank() != null) {
					R23cell6.setCellValue(record.getR23_total_deposit_bank().doubleValue());
					R23cell6.setCellStyle(numberStyle);
					} else {
					R23cell6.setCellValue("");
					R23cell6.setCellStyle(textStyle);
					}


					// R24
					row = sheet.getRow(23);
					// Column C
					Cell R24cell1 = row.createCell(2);
					if (record.getR24_deposit_excluding_number() != null) {
						R24cell1.setCellValue(record.getR24_deposit_excluding_number().doubleValue());
						R24cell1.setCellStyle(numberStyle);
					} else {
						R24cell1.setCellValue("");
						R24cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R24cell2 = row.createCell(3);
					if (record.getR24_deposit_excluding_amount() != null) {
						R24cell2.setCellValue(record.getR24_deposit_excluding_amount().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R24cell3 = row.createCell(4);
					if (record.getR24_deposit_foreign_number() != null) {
						R24cell3.setCellValue(record.getR24_deposit_foreign_number().doubleValue());
						R24cell3.setCellStyle(numberStyle);
					} else {
						R24cell3.setCellValue("");
						R24cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R24cell4 = row.createCell(5);
					if (record.getR24_deposit_foreign_amount() != null) {
						R24cell4.setCellValue(record.getR24_deposit_foreign_amount().doubleValue());
						R24cell4.setCellStyle(numberStyle);
					} else {
						R24cell4.setCellValue("");
						R24cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R24cell5 = row.createCell(8);
					if (record2.getR24_TOTAL_DEPOSIT_EXCEED() != null) {
					R24cell5.setCellValue(record2.getR24_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R24cell5.setCellValue("");
					}
										
					// Column J
					Cell R24cell6 = row.createCell(9);
					if (record.getR24_total_deposit_bank() != null) {
					R24cell6.setCellValue(record.getR24_total_deposit_bank().doubleValue());
					R24cell6.setCellStyle(numberStyle);
					} else {
					R24cell6.setCellValue("");
					R24cell6.setCellStyle(textStyle);
					}

					// R25
					row = sheet.getRow(24);
					// Column C
					Cell R25cell1 = row.createCell(2);
					if (record.getR25_deposit_excluding_number() != null) {
						R25cell1.setCellValue(record.getR25_deposit_excluding_number().doubleValue());
						R25cell1.setCellStyle(numberStyle);
					} else {
						R25cell1.setCellValue("");
						R25cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R25cell2 = row.createCell(3);
					if (record.getR25_deposit_excluding_amount() != null) {
						R25cell2.setCellValue(record.getR25_deposit_excluding_amount().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R25cell3 = row.createCell(4);
					if (record.getR25_deposit_foreign_number() != null) {
						R25cell3.setCellValue(record.getR25_deposit_foreign_number().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R25cell4 = row.createCell(5);
					if (record.getR25_deposit_foreign_amount() != null) {
						R25cell4.setCellValue(record.getR25_deposit_foreign_amount().doubleValue());
						R25cell4.setCellStyle(numberStyle);
					} else {
						R25cell4.setCellValue("");
						R25cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R25cell5 = row.createCell(8);
					if (record2.getR25_TOTAL_DEPOSIT_EXCEED() != null) {
					R25cell5.setCellValue(record2.getR25_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R25cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R25cell6 = row.createCell(9);
					if (record.getR25_total_deposit_bank() != null) {
					R25cell6.setCellValue(record.getR25_total_deposit_bank().doubleValue());
					R25cell6.setCellStyle(numberStyle);
					} else {
					R25cell6.setCellValue("");
					R25cell6.setCellStyle(textStyle);
					}

					// R26
					row = sheet.getRow(25);
					// Column C
					Cell R26cell1 = row.createCell(2);
					if (record.getR26_deposit_excluding_number() != null) {
						R26cell1.setCellValue(record.getR26_deposit_excluding_number().doubleValue());
						R26cell1.setCellStyle(numberStyle);
					} else {
						R26cell1.setCellValue("");
						R26cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R26cell2 = row.createCell(3);
					if (record.getR26_deposit_excluding_amount() != null) {
						R26cell2.setCellValue(record.getR26_deposit_excluding_amount().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R26cell3 = row.createCell(4);
					if (record.getR26_deposit_foreign_number() != null) {
						R26cell3.setCellValue(record.getR26_deposit_foreign_number().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R26cell4 = row.createCell(5);
					if (record.getR26_deposit_foreign_amount() != null) {
						R26cell4.setCellValue(record.getR26_deposit_foreign_amount().doubleValue());
						R26cell4.setCellStyle(numberStyle);
					} else {
						R26cell4.setCellValue("");
						R26cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R26cell5 = row.createCell(8);
					if (record2.getR26_TOTAL_DEPOSIT_EXCEED() != null) {
					R26cell5.setCellValue(record2.getR26_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R26cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R26cell6 = row.createCell(9);
					if (record.getR26_total_deposit_bank() != null) {
					R26cell6.setCellValue(record.getR26_total_deposit_bank().doubleValue());
					R26cell6.setCellStyle(numberStyle);
					} else {
					R26cell6.setCellValue("");
					R26cell6.setCellStyle(textStyle);
					}

					// R27
					row = sheet.getRow(26);
					// Column C
					Cell R27cell1 = row.createCell(2);
					if (record.getR27_deposit_excluding_number() != null) {
						R27cell1.setCellValue(record.getR27_deposit_excluding_number().doubleValue());
						R27cell1.setCellStyle(numberStyle);
					} else {
						R27cell1.setCellValue("");
						R27cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R27cell2 = row.createCell(3);
					if (record.getR27_deposit_excluding_amount() != null) {
						R27cell2.setCellValue(record.getR27_deposit_excluding_amount().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R27cell3 = row.createCell(4);
					if (record.getR27_deposit_foreign_number() != null) {
						R27cell3.setCellValue(record.getR27_deposit_foreign_number().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R27cell4 = row.createCell(5);
					if (record.getR27_deposit_foreign_amount() != null) {
						R27cell4.setCellValue(record.getR27_deposit_foreign_amount().doubleValue());
						R27cell4.setCellStyle(numberStyle);
					} else {
						R27cell4.setCellValue("");
						R27cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R27cell5 = row.createCell(8);
					if (record2.getR27_TOTAL_DEPOSIT_EXCEED() != null) {
					R27cell5.setCellValue(record2.getR27_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R27cell5.setCellValue("");
					
					
					}
										
					// Column J
					Cell R27cell6 = row.createCell(9);
					if (record.getR27_total_deposit_bank() != null) {
					R27cell6.setCellValue(record.getR27_total_deposit_bank().doubleValue());
					R27cell6.setCellStyle(numberStyle);
					} else {
					R27cell6.setCellValue("");
					R27cell6.setCellStyle(textStyle);
					}

					// R28
					row = sheet.getRow(27);
					// Column C
					Cell R28cell1 = row.createCell(2);
					if (record.getR28_deposit_excluding_number() != null) {
						R28cell1.setCellValue(record.getR28_deposit_excluding_number().doubleValue());
						R28cell1.setCellStyle(numberStyle);
					} else {
						R28cell1.setCellValue("");
						R28cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R28cell2 = row.createCell(3);
					if (record.getR28_deposit_excluding_amount() != null) {
						R28cell2.setCellValue(record.getR28_deposit_excluding_amount().doubleValue());
						R28cell2.setCellStyle(numberStyle);
					} else {
						R28cell2.setCellValue("");
						R28cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R28cell3 = row.createCell(4);
					if (record.getR28_deposit_foreign_number() != null) {
						R28cell3.setCellValue(record.getR28_deposit_foreign_number().doubleValue());
						R28cell3.setCellStyle(numberStyle);
					} else {
						R28cell3.setCellValue("");
						R28cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R28cell4 = row.createCell(5);
					if (record.getR28_deposit_foreign_amount() != null) {
						R28cell4.setCellValue(record.getR28_deposit_foreign_amount().doubleValue());
						R28cell4.setCellStyle(numberStyle);
					} else {
						R28cell4.setCellValue("");
						R28cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R28cell5 = row.createCell(8);
					if (record2.getR28_TOTAL_DEPOSIT_EXCEED() != null) {
					R28cell5.setCellValue(record2.getR28_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R28cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R28cell6 = row.createCell(9);
					if (record.getR28_total_deposit_bank() != null) {
					R28cell6.setCellValue(record.getR28_total_deposit_bank().doubleValue());
					R28cell6.setCellStyle(numberStyle);
					} else {
					R28cell6.setCellValue("");
					R28cell6.setCellStyle(textStyle);
					}

					// R31
					row = sheet.getRow(30);
					// Column C
					Cell R31cell1 = row.createCell(2);
					if (record.getR31_deposit_excluding_number() != null) {
						R31cell1.setCellValue(record.getR31_deposit_excluding_number().doubleValue());
						R31cell1.setCellStyle(numberStyle);
					} else {
						R31cell1.setCellValue("");
						R31cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R31cell2 = row.createCell(3);
					if (record.getR31_deposit_excluding_amount() != null) {
						R31cell2.setCellValue(record.getR31_deposit_excluding_amount().doubleValue());
						R31cell2.setCellStyle(numberStyle);
					} else {
						R31cell2.setCellValue("");
						R31cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R31cell3 = row.createCell(4);
					if (record.getR31_deposit_foreign_number() != null) {
						R31cell3.setCellValue(record.getR31_deposit_foreign_number().doubleValue());
						R31cell3.setCellStyle(numberStyle);
					} else {
						R31cell3.setCellValue("");
						R31cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R31cell4 = row.createCell(5);
					if (record.getR31_deposit_foreign_amount() != null) {
						R31cell4.setCellValue(record.getR31_deposit_foreign_amount().doubleValue());
						R31cell4.setCellStyle(numberStyle);
					} else {
						R31cell4.setCellValue("");
						R31cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R31cell5 = row.createCell(8);
					if (record2.getR31_TOTAL_DEPOSIT_EXCEED() != null) {
					R31cell5.setCellValue(record2.getR31_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R31cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R31cell6 = row.createCell(9);
					if (record.getR31_total_deposit_bank() != null) {
					R31cell6.setCellValue(record.getR31_total_deposit_bank().doubleValue());
					R31cell6.setCellStyle(numberStyle);
					} else {
					R31cell6.setCellValue("");
					R31cell6.setCellStyle(textStyle);
					}

					// R32
					row = sheet.getRow(31);
					// Column C
					Cell R32cell1 = row.createCell(2);
					if (record.getR32_deposit_excluding_number() != null) {
						R32cell1.setCellValue(record.getR32_deposit_excluding_number().doubleValue());
						R32cell1.setCellStyle(numberStyle);
					} else {
						R32cell1.setCellValue("");
						R32cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R32cell2 = row.createCell(3);
					if (record.getR32_deposit_excluding_amount() != null) {
						R32cell2.setCellValue(record.getR32_deposit_excluding_amount().doubleValue());
						R32cell2.setCellStyle(numberStyle);
					} else {
						R32cell2.setCellValue("");
						R32cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R32cell3 = row.createCell(4);
					if (record.getR32_deposit_foreign_number() != null) {
						R32cell3.setCellValue(record.getR32_deposit_foreign_number().doubleValue());
						R32cell3.setCellStyle(numberStyle);
					} else {
						R32cell3.setCellValue("");
						R32cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R32cell4 = row.createCell(5);
					if (record.getR32_deposit_foreign_amount() != null) {
						R32cell4.setCellValue(record.getR32_deposit_foreign_amount().doubleValue());
						R32cell4.setCellStyle(numberStyle);
					} else {
						R32cell4.setCellValue("");
						R32cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R32cell5 = row.createCell(8);
					if (record2.getR32_TOTAL_DEPOSIT_EXCEED() != null) {
					R32cell5.setCellValue(record2.getR32_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R32cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R32cell6 = row.createCell(9);
					if (record.getR32_total_deposit_bank() != null) {
					R32cell6.setCellValue(record.getR32_total_deposit_bank().doubleValue());
					R32cell6.setCellStyle(numberStyle);
					} else {
					R32cell6.setCellValue("");
					R32cell6.setCellStyle(textStyle);
					}

					// R33
					row = sheet.getRow(32);
					// Column C
					Cell R33cell1 = row.createCell(2);
					if (record.getR33_deposit_excluding_number() != null) {
						R33cell1.setCellValue(record.getR33_deposit_excluding_number().doubleValue());
						R33cell1.setCellStyle(numberStyle);
					} else {
						R33cell1.setCellValue("");
						R33cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R33cell2 = row.createCell(3);
					if (record.getR33_deposit_excluding_amount() != null) {
						R33cell2.setCellValue(record.getR33_deposit_excluding_amount().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R33cell3 = row.createCell(4);
					if (record.getR33_deposit_foreign_number() != null) {
						R33cell3.setCellValue(record.getR33_deposit_foreign_number().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R33cell4 = row.createCell(5);
					if (record.getR33_deposit_foreign_amount() != null) {
						R33cell4.setCellValue(record.getR33_deposit_foreign_amount().doubleValue());
						R33cell4.setCellStyle(numberStyle);
					} else {
						R33cell4.setCellValue("");
						R33cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R33cell5 = row.createCell(8);
					if (record2.getR33_TOTAL_DEPOSIT_EXCEED() != null) {
					R33cell5.setCellValue(record2.getR33_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R33cell5.setCellValue("");
					}
										
					// Column J
					Cell R33cell6 = row.createCell(9);
					if (record.getR33_total_deposit_bank() != null) {
					R33cell6.setCellValue(record.getR33_total_deposit_bank().doubleValue());
					R33cell6.setCellStyle(numberStyle);
					} else {
					R33cell6.setCellValue("");
					R33cell6.setCellStyle(textStyle);
					}

					// R34
					row = sheet.getRow(33);
					// Column C
					Cell R34cell1 = row.createCell(2);
					if (record.getR34_deposit_excluding_number() != null) {
						R34cell1.setCellValue(record.getR34_deposit_excluding_number().doubleValue());
						R34cell1.setCellStyle(numberStyle);
					} else {
						R34cell1.setCellValue("");
						R34cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R34cell2 = row.createCell(3);
					if (record.getR34_deposit_excluding_amount() != null) {
						R34cell2.setCellValue(record.getR34_deposit_excluding_amount().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R34cell3 = row.createCell(4);
					if (record.getR34_deposit_foreign_number() != null) {
						R34cell3.setCellValue(record.getR34_deposit_foreign_number().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R34cell4 = row.createCell(5);
					if (record.getR34_deposit_foreign_amount() != null) {
						R34cell4.setCellValue(record.getR34_deposit_foreign_amount().doubleValue());
						R34cell4.setCellStyle(numberStyle);
					} else {
						R34cell4.setCellValue("");
						R34cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R34cell5 = row.createCell(8);
					if (record2.getR34_TOTAL_DEPOSIT_EXCEED() != null) {
					R34cell5.setCellValue(record2.getR34_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R34cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R34cell6 = row.createCell(9);
					if (record.getR34_total_deposit_bank() != null) {
					R34cell6.setCellValue(record.getR34_total_deposit_bank().doubleValue());
					R34cell6.setCellStyle(numberStyle);
					} else {
					R34cell6.setCellValue("");
					R34cell6.setCellStyle(textStyle);
					}

					// R35
					row = sheet.getRow(34);
					// Column C
					Cell R35cell1 = row.createCell(2);
					if (record.getR35_deposit_excluding_number() != null) {
						R35cell1.setCellValue(record.getR35_deposit_excluding_number().doubleValue());
						R35cell1.setCellStyle(numberStyle);
					} else {
						R35cell1.setCellValue("");
						R35cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R35cell2 = row.createCell(3);
					if (record.getR35_deposit_excluding_amount() != null) {
						R35cell2.setCellValue(record.getR35_deposit_excluding_amount().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R35cell3 = row.createCell(4);
					if (record.getR35_deposit_foreign_number() != null) {
						R35cell3.setCellValue(record.getR35_deposit_foreign_number().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R35cell4 = row.createCell(5);
					if (record.getR35_deposit_foreign_amount() != null) {
						R35cell4.setCellValue(record.getR35_deposit_foreign_amount().doubleValue());
						R35cell4.setCellStyle(numberStyle);
					} else {
						R35cell4.setCellValue("");
						R35cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R35cell5 = row.createCell(8);
					if (record2.getR35_TOTAL_DEPOSIT_EXCEED() != null) {
					R35cell5.setCellValue(record2.getR35_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R35cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R35cell6 = row.createCell(9);
					if (record.getR35_total_deposit_bank() != null) {
					R35cell6.setCellValue(record.getR35_total_deposit_bank().doubleValue());
					R35cell6.setCellStyle(numberStyle);
					} else {
					R35cell6.setCellValue("");
					R35cell6.setCellStyle(textStyle);
					}

					// R36
					row = sheet.getRow(35);
					// Column C
					Cell R36cell1 = row.createCell(2);
					if (record.getR36_deposit_excluding_number() != null) {
						R36cell1.setCellValue(record.getR36_deposit_excluding_number().doubleValue());
						R36cell1.setCellStyle(numberStyle);
					} else {
						R36cell1.setCellValue("");
						R36cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R36cell2 = row.createCell(3);
					if (record.getR36_deposit_excluding_amount() != null) {
						R36cell2.setCellValue(record.getR36_deposit_excluding_amount().doubleValue());
						R36cell2.setCellStyle(numberStyle);
					} else {
						R36cell2.setCellValue("");
						R36cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R36cell3 = row.createCell(4);
					if (record.getR36_deposit_foreign_number() != null) {
						R36cell3.setCellValue(record.getR36_deposit_foreign_number().doubleValue());
						R36cell3.setCellStyle(numberStyle);
					} else {
						R36cell3.setCellValue("");
						R36cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R36cell4 = row.createCell(5);
					if (record.getR36_deposit_foreign_amount() != null) {
						R36cell4.setCellValue(record.getR36_deposit_foreign_amount().doubleValue());
						R36cell4.setCellStyle(numberStyle);
					} else {
						R36cell4.setCellValue("");
						R36cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R36cell5 = row.createCell(8);
					if (record2.getR36_TOTAL_DEPOSIT_EXCEED() != null) {
					R36cell5.setCellValue(record2.getR36_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R36cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R36cell6 = row.createCell(9);
					if (record.getR36_total_deposit_bank() != null) {
					R36cell6.setCellValue(record.getR36_total_deposit_bank().doubleValue());
					R36cell6.setCellStyle(numberStyle);
					} else {
					R36cell6.setCellValue("");
					R36cell6.setCellStyle(textStyle);
					}


					// R39
					row = sheet.getRow(38);
					// Column C
					Cell R39cell1 = row.createCell(2);
					if (record.getR39_deposit_excluding_number() != null) {
						R39cell1.setCellValue(record.getR39_deposit_excluding_number().doubleValue());
						R39cell1.setCellStyle(numberStyle);
					} else {
						R39cell1.setCellValue("");
						R39cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R39cell2 = row.createCell(3);
					if (record.getR39_deposit_excluding_amount() != null) {
						R39cell2.setCellValue(record.getR39_deposit_excluding_amount().doubleValue());
						R39cell2.setCellStyle(numberStyle);
					} else {
						R39cell2.setCellValue("");
						R39cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R39cell3 = row.createCell(4);
					if (record.getR39_deposit_foreign_number() != null) {
						R39cell3.setCellValue(record.getR39_deposit_foreign_number().doubleValue());
						R39cell3.setCellStyle(numberStyle);
					} else {
						R39cell3.setCellValue("");
						R39cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R39cell4 = row.createCell(5);
					if (record.getR39_deposit_foreign_amount() != null) {
						R39cell4.setCellValue(record.getR39_deposit_foreign_amount().doubleValue());
						R39cell4.setCellStyle(numberStyle);
					} else {
						R39cell4.setCellValue("");
						R39cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R39cell5 = row.createCell(8);
					if (record2.getR39_TOTAL_DEPOSIT_EXCEED() != null) {
					R39cell5.setCellValue(record2.getR39_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R39cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R39cell6 = row.createCell(9);
					if (record.getR39_total_deposit_bank() != null) {
					R39cell6.setCellValue(record.getR39_total_deposit_bank().doubleValue());
					R39cell6.setCellStyle(numberStyle);
					} else {
					R39cell6.setCellValue("");
					R39cell6.setCellStyle(textStyle);
					}

					
					// R40
					row = sheet.getRow(39);
					// Column C
					Cell R40cell1 = row.createCell(2);
					if (record.getR40_deposit_excluding_number() != null) {
						R40cell1.setCellValue(record.getR40_deposit_excluding_number().doubleValue());
						R40cell1.setCellStyle(numberStyle);
					} else {
						R40cell1.setCellValue("");
						R40cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R40cell2 = row.createCell(3);
					if (record.getR40_deposit_excluding_amount() != null) {
						R40cell2.setCellValue(record.getR40_deposit_excluding_amount().doubleValue());
						R40cell2.setCellStyle(numberStyle);
					} else {
						R40cell2.setCellValue("");
						R40cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R40cell3 = row.createCell(4);
					if (record.getR40_deposit_foreign_number() != null) {
						R40cell3.setCellValue(record.getR40_deposit_foreign_number().doubleValue());
						R40cell3.setCellStyle(numberStyle);
					} else {
						R40cell3.setCellValue("");
						R40cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R40cell4 = row.createCell(5);
					if (record.getR40_deposit_foreign_amount() != null) {
						R40cell4.setCellValue(record.getR40_deposit_foreign_amount().doubleValue());
						R40cell4.setCellStyle(numberStyle);
					} else {
						R40cell4.setCellValue("");
						R40cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R40cell5 = row.createCell(8);
					if (record2.getR40_TOTAL_DEPOSIT_EXCEED() != null) {
					R40cell5.setCellValue(record2.getR40_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R40cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R40cell6 = row.createCell(9);
					if (record.getR40_total_deposit_bank() != null) {
					R40cell6.setCellValue(record.getR40_total_deposit_bank().doubleValue());
					R40cell6.setCellStyle(numberStyle);
					} else {
					R40cell6.setCellValue("");
					R40cell6.setCellStyle(textStyle);
					}

					// R41
					row = sheet.getRow(40);
					// Column C
					Cell R41cell1 = row.createCell(2);
					if (record.getR41_deposit_excluding_number() != null) {
						R41cell1.setCellValue(record.getR41_deposit_excluding_number().doubleValue());
						R41cell1.setCellStyle(numberStyle);
					} else {
						R41cell1.setCellValue("");
						R41cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R41cell2 = row.createCell(3);
					if (record.getR41_deposit_excluding_amount() != null) {
						R41cell2.setCellValue(record.getR41_deposit_excluding_amount().doubleValue());
						R41cell2.setCellStyle(numberStyle);
					} else {
						R41cell2.setCellValue("");
						R41cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R41cell3 = row.createCell(4);
					if (record.getR41_deposit_foreign_number() != null) {
						R41cell3.setCellValue(record.getR41_deposit_foreign_number().doubleValue());
						R41cell3.setCellStyle(numberStyle);
					} else {
						R41cell3.setCellValue("");
						R41cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R41cell4 = row.createCell(5);
					if (record.getR41_deposit_foreign_amount() != null) {
						R41cell4.setCellValue(record.getR41_deposit_foreign_amount().doubleValue());
						R41cell4.setCellStyle(numberStyle);
					} else {
						R41cell4.setCellValue("");
						R41cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R41cell5 = row.createCell(8);
					if (record2.getR41_TOTAL_DEPOSIT_EXCEED() != null) {
					R41cell5.setCellValue(record2.getR41_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R41cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R41cell6 = row.createCell(9);
					if (record.getR41_total_deposit_bank() != null) {
					R41cell6.setCellValue(record.getR41_total_deposit_bank().doubleValue());
					R41cell6.setCellStyle(numberStyle);
					} else {
					R41cell6.setCellValue("");
					R41cell6.setCellStyle(textStyle);
					}


					// R42
					row = sheet.getRow(41);
					// Column C
					Cell R42cell1 = row.createCell(2);
					if (record.getR42_deposit_excluding_number() != null) {
						R42cell1.setCellValue(record.getR42_deposit_excluding_number().doubleValue());
						R42cell1.setCellStyle(numberStyle);
					} else {
						R42cell1.setCellValue("");
						R42cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R42cell2 = row.createCell(3);
					if (record.getR42_deposit_excluding_amount() != null) {
						R42cell2.setCellValue(record.getR42_deposit_excluding_amount().doubleValue());
						R42cell2.setCellStyle(numberStyle);
					} else {
						R42cell2.setCellValue("");
						R42cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R42cell3 = row.createCell(4);
					if (record.getR42_deposit_foreign_number() != null) {
						R42cell3.setCellValue(record.getR42_deposit_foreign_number().doubleValue());
						R42cell3.setCellStyle(numberStyle);
					} else {
						R42cell3.setCellValue("");
						R42cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R42cell4 = row.createCell(5);
					if (record.getR42_deposit_foreign_amount() != null) {
						R42cell4.setCellValue(record.getR42_deposit_foreign_amount().doubleValue());
						R42cell4.setCellStyle(numberStyle);
					} else {
						R42cell4.setCellValue("");
						R42cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R42cell5 = row.createCell(8);
					if (record2.getR42_TOTAL_DEPOSIT_EXCEED() != null) {
					R42cell5.setCellValue(record2.getR42_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R42cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R42cell6 = row.createCell(9);
					if (record.getR42_total_deposit_bank() != null) {
					R42cell6.setCellValue(record.getR42_total_deposit_bank().doubleValue());
					R42cell6.setCellStyle(numberStyle);
					} else {
					R42cell6.setCellValue("");
					R42cell6.setCellStyle(textStyle);
					}


					// R43
					row = sheet.getRow(42);
					// Column C
					Cell R43cell1 = row.createCell(2);
					if (record.getR43_deposit_excluding_number() != null) {
						R43cell1.setCellValue(record.getR43_deposit_excluding_number().doubleValue());
						R43cell1.setCellStyle(numberStyle);
					} else {
						R43cell1.setCellValue("");
						R43cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R43cell2 = row.createCell(3);
					if (record.getR43_deposit_excluding_amount() != null) {
						R43cell2.setCellValue(record.getR43_deposit_excluding_amount().doubleValue());
						R43cell2.setCellStyle(numberStyle);
					} else {
						R43cell2.setCellValue("");
						R43cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R43cell3 = row.createCell(4);
					if (record.getR43_deposit_foreign_number() != null) {
						R43cell3.setCellValue(record.getR43_deposit_foreign_number().doubleValue());
						R43cell3.setCellStyle(numberStyle);
					} else {
						R43cell3.setCellValue("");
						R43cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R43cell4 = row.createCell(5);
					if (record.getR43_deposit_foreign_amount() != null) {
						R43cell4.setCellValue(record.getR43_deposit_foreign_amount().doubleValue());
						R43cell4.setCellStyle(numberStyle);
					} else {
						R43cell4.setCellValue("");
						R43cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R43cell5 = row.createCell(8);
					if (record2.getR43_TOTAL_DEPOSIT_EXCEED() != null) {
					R43cell5.setCellValue(record2.getR43_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R43cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R43cell6 = row.createCell(9);
					if (record.getR43_total_deposit_bank() != null) {
					R43cell6.setCellValue(record.getR43_total_deposit_bank().doubleValue());
					R43cell6.setCellStyle(numberStyle);
					} else {
					R43cell6.setCellValue("");
					R43cell6.setCellStyle(textStyle);
					}

					// R44
					row = sheet.getRow(43);
					// Column C
					Cell R44cell1 = row.createCell(2);
					if (record.getR44_deposit_excluding_number() != null) {
						R44cell1.setCellValue(record.getR44_deposit_excluding_number().doubleValue());
						R44cell1.setCellStyle(numberStyle);
					} else {
						R44cell1.setCellValue("");
						R44cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R44cell2 = row.createCell(3);
					if (record.getR44_deposit_excluding_amount() != null) {
						R44cell2.setCellValue(record.getR44_deposit_excluding_amount().doubleValue());
						R44cell2.setCellStyle(numberStyle);
					} else {
						R44cell2.setCellValue("");
						R44cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R44cell3 = row.createCell(4);
					if (record.getR44_deposit_foreign_number() != null) {
						R44cell3.setCellValue(record.getR44_deposit_foreign_number().doubleValue());
						R44cell3.setCellStyle(numberStyle);
					} else {
						R44cell3.setCellValue("");
						R44cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R44cell4 = row.createCell(5);
					if (record.getR44_deposit_foreign_amount() != null) {
						R44cell4.setCellValue(record.getR44_deposit_foreign_amount().doubleValue());
						R44cell4.setCellStyle(numberStyle);
					} else {
						R44cell4.setCellValue("");
						R44cell4.setCellStyle(textStyle);
					}
					// Column I
					Cell R44cell5 = row.createCell(8);
					if (record2.getR44_TOTAL_DEPOSIT_EXCEED() != null) {
					R44cell5.setCellValue(record2.getR44_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R44cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R44cell6 = row.createCell(9);
					if (record.getR44_total_deposit_bank() != null) {
					R44cell6.setCellValue(record.getR44_total_deposit_bank().doubleValue());
					R44cell6.setCellStyle(numberStyle);
					} else {
					R44cell6.setCellValue("");
					R44cell6.setCellStyle(textStyle);
					}

					// R47
					row = sheet.getRow(46);
					// Column C
					Cell R47cell1 = row.createCell(2);
					if (record.getR47_deposit_excluding_number() != null) {
						R47cell1.setCellValue(record.getR47_deposit_excluding_number().doubleValue());
						R47cell1.setCellStyle(numberStyle);
					} else {
						R47cell1.setCellValue("");
						R47cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R47cell2 = row.createCell(3);
					if (record.getR47_deposit_excluding_amount() != null) {
						R47cell2.setCellValue(record.getR47_deposit_excluding_amount().doubleValue());
						R47cell2.setCellStyle(numberStyle);
					} else {
						R47cell2.setCellValue("");
						R47cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R47cell3 = row.createCell(4);
					if (record.getR47_deposit_foreign_number() != null) {
						R47cell3.setCellValue(record.getR47_deposit_foreign_number().doubleValue());
						R47cell3.setCellStyle(numberStyle);
					} else {
						R47cell3.setCellValue("");
						R47cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R47cell4 = row.createCell(5);
					if (record.getR47_deposit_foreign_amount() != null) {
						R47cell4.setCellValue(record.getR47_deposit_foreign_amount().doubleValue());
						R47cell4.setCellStyle(numberStyle);
					} else {
						R47cell4.setCellValue("");
						R47cell4.setCellStyle(textStyle);
					}
					// Column I
					Cell R47cell5 = row.createCell(8);
					if (record2.getR47_TOTAL_DEPOSIT_EXCEED() != null) {
					R47cell5.setCellValue(record2.getR47_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R47cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R47cell6 = row.createCell(9);
					if (record.getR47_total_deposit_bank() != null) {
					R47cell6.setCellValue(record.getR47_total_deposit_bank().doubleValue());
					R47cell6.setCellStyle(numberStyle);
					} else {
					R47cell6.setCellValue("");
					R47cell6.setCellStyle(textStyle);
					}

					// R48
					row = sheet.getRow(47);
					// Column C
					Cell R48cell1 = row.createCell(2);
					if (record.getR48_deposit_excluding_number() != null) {
						R48cell1.setCellValue(record.getR48_deposit_excluding_number().doubleValue());
						R48cell1.setCellStyle(numberStyle);
					} else {
						R48cell1.setCellValue("");
						R48cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R48cell2 = row.createCell(3);
					if (record.getR48_deposit_excluding_amount() != null) {
						R48cell2.setCellValue(record.getR48_deposit_excluding_amount().doubleValue());
						R48cell2.setCellStyle(numberStyle);
					} else {
						R48cell2.setCellValue("");
						R48cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R48cell3 = row.createCell(4);
					if (record.getR48_deposit_foreign_number() != null) {
						R48cell3.setCellValue(record.getR48_deposit_foreign_number().doubleValue());
						R48cell3.setCellStyle(numberStyle);
					} else {
						R48cell3.setCellValue("");
						R48cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R48cell4 = row.createCell(5);
					if (record.getR48_deposit_foreign_amount() != null) {
						R48cell4.setCellValue(record.getR48_deposit_foreign_amount().doubleValue());
						R48cell4.setCellStyle(numberStyle);
					} else {
						R48cell4.setCellValue("");
						R48cell4.setCellStyle(textStyle);
					}
					// Column I
					Cell R48cell5 = row.createCell(8);
					if (record2.getR48_TOTAL_DEPOSIT_EXCEED() != null) {
					R48cell5.setCellValue(record2.getR48_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R48cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R48cell6 = row.createCell(9);
					if (record.getR48_total_deposit_bank() != null) {
					R48cell6.setCellValue(record.getR48_total_deposit_bank().doubleValue());
					R48cell6.setCellStyle(numberStyle);
					} else {
					R48cell6.setCellValue("");
					R48cell6.setCellStyle(textStyle);
					}


					// R49
					row = sheet.getRow(48);
					// Column C
					Cell R49cell1 = row.createCell(2);
					if (record.getR49_deposit_excluding_number() != null) {
						R49cell1.setCellValue(record.getR49_deposit_excluding_number().doubleValue());
						R49cell1.setCellStyle(numberStyle);
					} else {
						R49cell1.setCellValue("");
						R49cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R49cell2 = row.createCell(3);
					if (record.getR49_deposit_excluding_amount() != null) {
						R49cell2.setCellValue(record.getR49_deposit_excluding_amount().doubleValue());
						R49cell2.setCellStyle(numberStyle);
					} else {
						R49cell2.setCellValue("");
						R49cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R49cell3 = row.createCell(4);
					if (record.getR49_deposit_foreign_number() != null) {
						R49cell3.setCellValue(record.getR49_deposit_foreign_number().doubleValue());
						R49cell3.setCellStyle(numberStyle);
					} else {
						R49cell3.setCellValue("");
						R49cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R49cell4 = row.createCell(5);
					if (record.getR49_deposit_foreign_amount() != null) {
						R49cell4.setCellValue(record.getR49_deposit_foreign_amount().doubleValue());
						R49cell4.setCellStyle(numberStyle);
					} else {
						R49cell4.setCellValue("");
						R49cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R49cell5 = row.createCell(8);
					if (record2.getR49_TOTAL_DEPOSIT_EXCEED() != null) {
					R49cell5.setCellValue(record2.getR49_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R49cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R49cell6 = row.createCell(9);
					if (record.getR49_total_deposit_bank() != null) {
					R49cell6.setCellValue(record.getR49_total_deposit_bank().doubleValue());
					R49cell6.setCellStyle(numberStyle);
					} else {
					R49cell6.setCellValue("");
					R49cell6.setCellStyle(textStyle);
					}

					// R50
					row = sheet.getRow(49);
					// Column C
					Cell R50cell1 = row.createCell(2);
					if (record.getR50_deposit_excluding_number() != null) {
						R50cell1.setCellValue(record.getR50_deposit_excluding_number().doubleValue());
						R50cell1.setCellStyle(numberStyle);
					} else {
						R50cell1.setCellValue("");
						R50cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R50cell2 = row.createCell(3);
					if (record.getR50_deposit_excluding_amount() != null) {
						R50cell2.setCellValue(record.getR50_deposit_excluding_amount().doubleValue());
						R50cell2.setCellStyle(numberStyle);
					} else {
						R50cell2.setCellValue("");
						R50cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R50cell3 = row.createCell(4);
					if (record.getR50_deposit_foreign_number() != null) {
						R50cell3.setCellValue(record.getR50_deposit_foreign_number().doubleValue());
						R50cell3.setCellStyle(numberStyle);
					} else {
						R50cell3.setCellValue("");
						R50cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R50cell4 = row.createCell(5);
					if (record.getR50_deposit_foreign_amount() != null) {
						R50cell4.setCellValue(record.getR50_deposit_foreign_amount().doubleValue());
						R50cell4.setCellStyle(numberStyle);
					} else {
						R50cell4.setCellValue("");
						R50cell4.setCellStyle(textStyle);
					}
					// Column I
					Cell R50cell5 = row.createCell(8);
					if (record2.getR50_TOTAL_DEPOSIT_EXCEED() != null) {
					R50cell5.setCellValue(record2.getR50_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R50cell5.setCellValue("");
				
					}
										
					// Column J
					Cell R50cell6 = row.createCell(9);
					if (record.getR50_total_deposit_bank() != null) {
					R50cell6.setCellValue(record.getR50_total_deposit_bank().doubleValue());
					R50cell6.setCellStyle(numberStyle);
					} else {
					R50cell6.setCellValue("");
					R50cell6.setCellStyle(textStyle);
					}

					// R51
					row = sheet.getRow(50);
					// Column C
					Cell R51cell1 = row.createCell(2);
					if (record.getR51_deposit_excluding_number() != null) {
						R51cell1.setCellValue(record.getR51_deposit_excluding_number().doubleValue());
						R51cell1.setCellStyle(numberStyle);
					} else {
						R51cell1.setCellValue("");
						R51cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R51cell2 = row.createCell(3);
					if (record.getR51_deposit_excluding_amount() != null) {
						R51cell2.setCellValue(record.getR51_deposit_excluding_amount().doubleValue());
						R51cell2.setCellStyle(numberStyle);
					} else {
						R51cell2.setCellValue("");
						R51cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R51cell3 = row.createCell(4);
					if (record.getR51_deposit_foreign_number() != null) {
						R51cell3.setCellValue(record.getR51_deposit_foreign_number().doubleValue());
						R51cell3.setCellStyle(numberStyle);
					} else {
						R51cell3.setCellValue("");
						R51cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R51cell4 = row.createCell(5);
					if (record.getR51_deposit_foreign_amount() != null) {
						R51cell4.setCellValue(record.getR51_deposit_foreign_amount().doubleValue());
						R51cell4.setCellStyle(numberStyle);
					} else {
						R51cell4.setCellValue("");
						R51cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R51cell5 = row.createCell(8);
					if (record2.getR51_TOTAL_DEPOSIT_EXCEED() != null) {
					R51cell5.setCellValue(record2.getR51_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R51cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R51cell6 = row.createCell(9);
					if (record.getR51_total_deposit_bank() != null) {
					R51cell6.setCellValue(record.getR51_total_deposit_bank().doubleValue());
					R51cell6.setCellStyle(numberStyle);
					} else {
					R51cell6.setCellValue("");
					R51cell6.setCellStyle(textStyle);
					}

					// R52
					row = sheet.getRow(51);
					// Column C
					Cell R52cell1 = row.createCell(2);
					if (record.getR52_deposit_excluding_number() != null) {
						R52cell1.setCellValue(record.getR52_deposit_excluding_number().doubleValue());
						R52cell1.setCellStyle(numberStyle);
					} else {
						R52cell1.setCellValue("");
						R52cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R52cell2 = row.createCell(3);
					if (record.getR52_deposit_excluding_amount() != null) {
						R52cell2.setCellValue(record.getR52_deposit_excluding_amount().doubleValue());
						R52cell2.setCellStyle(numberStyle);
					} else {
						R52cell2.setCellValue("");
						R52cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R52cell3 = row.createCell(4);
					if (record.getR52_deposit_foreign_number() != null) {
						R52cell3.setCellValue(record.getR52_deposit_foreign_number().doubleValue());
						R52cell3.setCellStyle(numberStyle);
					} else {
						R52cell3.setCellValue("");
						R52cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R52cell4 = row.createCell(5);
					if (record.getR52_deposit_foreign_amount() != null) {
						R52cell4.setCellValue(record.getR52_deposit_foreign_amount().doubleValue());
						R52cell4.setCellStyle(numberStyle);
					} else {
						R52cell4.setCellValue("");
						R52cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R52cell5 = row.createCell(8);
					if (record2.getR52_TOTAL_DEPOSIT_EXCEED() != null) {
					R52cell5.setCellValue(record2.getR52_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R52cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R52cell6 = row.createCell(9);
					if (record.getR52_total_deposit_bank() != null) {
					R52cell6.setCellValue(record.getR52_total_deposit_bank().doubleValue());
					R52cell6.setCellStyle(numberStyle);
					} else {
					R52cell6.setCellValue("");
					R52cell6.setCellStyle(textStyle);
					}


					// R55
					row = sheet.getRow(54);
					// Column C
					Cell R55cell1 = row.createCell(2);
					if (record.getR55_deposit_excluding_number() != null) {
						R55cell1.setCellValue(record.getR55_deposit_excluding_number().doubleValue());
						R55cell1.setCellStyle(numberStyle);
					} else {
						R55cell1.setCellValue("");
						R55cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R55cell2 = row.createCell(3);
					if (record.getR55_deposit_excluding_amount() != null) {
						R55cell2.setCellValue(record.getR55_deposit_excluding_amount().doubleValue());
						R55cell2.setCellStyle(numberStyle);
					} else {
						R55cell2.setCellValue("");
						R55cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R55cell3 = row.createCell(4);
					if (record.getR55_deposit_foreign_number() != null) {
						R55cell3.setCellValue(record.getR55_deposit_foreign_number().doubleValue());
						R55cell3.setCellStyle(numberStyle);
					} else {
						R55cell3.setCellValue("");
						R55cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R55cell4 = row.createCell(5);
					if (record.getR55_deposit_foreign_amount() != null) {
						R55cell4.setCellValue(record.getR55_deposit_foreign_amount().doubleValue());
						R55cell4.setCellStyle(numberStyle);
					} else {
						R55cell4.setCellValue("");
						R55cell4.setCellStyle(textStyle);
					}
					// Column I
					Cell R55cell5 = row.createCell(8);
					if (record2.getR55_TOTAL_DEPOSIT_EXCEED() != null) {
					R55cell5.setCellValue(record2.getR55_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R55cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R55cell6 = row.createCell(9);
					if (record.getR55_total_deposit_bank() != null) {
					R55cell6.setCellValue(record.getR55_total_deposit_bank().doubleValue());
					R55cell6.setCellStyle(numberStyle);
					} else {
					R55cell6.setCellValue("");
					R55cell6.setCellStyle(textStyle);
					}


					// R56
					row = sheet.getRow(55);
					// Column C
					Cell R56cell1 = row.createCell(2);
					if (record.getR56_deposit_excluding_number() != null) {
						R56cell1.setCellValue(record.getR56_deposit_excluding_number().doubleValue());
						R56cell1.setCellStyle(numberStyle);
					} else {
						R56cell1.setCellValue("");
						R56cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R56cell2 = row.createCell(3);
					if (record.getR56_deposit_excluding_amount() != null) {
						R56cell2.setCellValue(record.getR56_deposit_excluding_amount().doubleValue());
						R56cell2.setCellStyle(numberStyle);
					} else {
						R56cell2.setCellValue("");
						R56cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R56cell3 = row.createCell(4);
					if (record.getR56_deposit_foreign_number() != null) {
						R56cell3.setCellValue(record.getR56_deposit_foreign_number().doubleValue());
						R56cell3.setCellStyle(numberStyle);
					} else {
						R56cell3.setCellValue("");
						R56cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R56cell4 = row.createCell(5);
					if (record.getR56_deposit_foreign_amount() != null) {
						R56cell4.setCellValue(record.getR56_deposit_foreign_amount().doubleValue());
						R56cell4.setCellStyle(numberStyle);
					} else {
						R56cell4.setCellValue("");
						R56cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R56cell5 = row.createCell(8);
					if (record2.getR56_TOTAL_DEPOSIT_EXCEED() != null) {
					R56cell5.setCellValue(record2.getR56_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R56cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R56cell6 = row.createCell(9);
					if (record.getR56_total_deposit_bank() != null) {
					R56cell6.setCellValue(record.getR56_total_deposit_bank().doubleValue());
					R56cell6.setCellStyle(numberStyle);
					} else {
					R56cell6.setCellValue("");
					R56cell6.setCellStyle(textStyle);
					}


					// R57
					row = sheet.getRow(56);
					// Column C
					Cell R57cell1 = row.createCell(2);
					if (record.getR57_deposit_excluding_number() != null) {
						R57cell1.setCellValue(record.getR57_deposit_excluding_number().doubleValue());
						R57cell1.setCellStyle(numberStyle);
					} else {
						R57cell1.setCellValue("");
						R57cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R57cell2 = row.createCell(3);
					if (record.getR57_deposit_excluding_amount() != null) {
						R57cell2.setCellValue(record.getR57_deposit_excluding_amount().doubleValue());
						R57cell2.setCellStyle(numberStyle);
					} else {
						R57cell2.setCellValue("");
						R57cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R57cell3 = row.createCell(4);
					if (record.getR57_deposit_foreign_number() != null) {
						R57cell3.setCellValue(record.getR57_deposit_foreign_number().doubleValue());
						R57cell3.setCellStyle(numberStyle);
					} else {
						R57cell3.setCellValue("");
						R57cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R57cell4 = row.createCell(5);
					if (record.getR57_deposit_foreign_amount() != null) {
						R57cell4.setCellValue(record.getR57_deposit_foreign_amount().doubleValue());
						R57cell4.setCellStyle(numberStyle);
					} else {
						R57cell4.setCellValue("");
						R57cell4.setCellStyle(textStyle);
					}
					// Column I
					Cell R57cell5 = row.createCell(8);
					if (record2.getR57_TOTAL_DEPOSIT_EXCEED() != null) {
					R57cell5.setCellValue(record2.getR57_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R57cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R57cell6 = row.createCell(9);
					if (record.getR57_total_deposit_bank() != null) {
					R57cell6.setCellValue(record.getR57_total_deposit_bank().doubleValue());
					R57cell6.setCellStyle(numberStyle);
					} else {
					R57cell6.setCellValue("");
					R57cell6.setCellStyle(textStyle);
					}

					// R58
					row = sheet.getRow(57);
					// Column C
					Cell R58cell1 = row.createCell(2);
					if (record.getR58_deposit_excluding_number() != null) {
						R58cell1.setCellValue(record.getR58_deposit_excluding_number().doubleValue());
						R58cell1.setCellStyle(numberStyle);
					} else {
						R58cell1.setCellValue("");
						R58cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R58cell2 = row.createCell(3);
					if (record.getR58_deposit_excluding_amount() != null) {
						R58cell2.setCellValue(record.getR58_deposit_excluding_amount().doubleValue());
						R58cell2.setCellStyle(numberStyle);
					} else {
						R58cell2.setCellValue("");
						R58cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R58cell3 = row.createCell(4);
					if (record.getR58_deposit_foreign_number() != null) {
						R58cell3.setCellValue(record.getR58_deposit_foreign_number().doubleValue());
						R58cell3.setCellStyle(numberStyle);
					} else {
						R58cell3.setCellValue("");
						R58cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R58cell4 = row.createCell(5);
					if (record.getR58_deposit_foreign_amount() != null) {
						R58cell4.setCellValue(record.getR58_deposit_foreign_amount().doubleValue());
						R58cell4.setCellStyle(numberStyle);
					} else {
						R58cell4.setCellValue("");
						R58cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R58cell5 = row.createCell(8);
					if (record2.getR58_TOTAL_DEPOSIT_EXCEED() != null) {
					R58cell5.setCellValue(record2.getR58_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R58cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R58cell6 = row.createCell(9);
					if (record.getR58_total_deposit_bank() != null) {
					R58cell6.setCellValue(record.getR58_total_deposit_bank().doubleValue());
					R58cell6.setCellStyle(numberStyle);
					} else {
					R58cell6.setCellValue("");
					R58cell6.setCellStyle(textStyle);
					}

					// R59
					row = sheet.getRow(58);
					// Column C
					Cell R59cell1 = row.createCell(2);
					if (record.getR59_deposit_excluding_number() != null) {
						R59cell1.setCellValue(record.getR59_deposit_excluding_number().doubleValue());
						R59cell1.setCellStyle(numberStyle);
					} else {
						R59cell1.setCellValue("");
						R59cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R59cell2 = row.createCell(3);
					if (record.getR59_deposit_excluding_amount() != null) {
						R59cell2.setCellValue(record.getR59_deposit_excluding_amount().doubleValue());
						R59cell2.setCellStyle(numberStyle);
					} else {
						R59cell2.setCellValue("");
						R59cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R59cell3 = row.createCell(4);
					if (record.getR59_deposit_foreign_number() != null) {
						R59cell3.setCellValue(record.getR59_deposit_foreign_number().doubleValue());
						R59cell3.setCellStyle(numberStyle);
					} else {
						R59cell3.setCellValue("");
						R59cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R59cell4 = row.createCell(5);
					if (record.getR59_deposit_foreign_amount() != null) {
						R59cell4.setCellValue(record.getR59_deposit_foreign_amount().doubleValue());
						R59cell4.setCellStyle(numberStyle);
					} else {
						R59cell4.setCellValue("");
						R59cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R59cell5 = row.createCell(8);
					if (record2.getR59_TOTAL_DEPOSIT_EXCEED() != null) {
					R59cell5.setCellValue(record2.getR59_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R59cell5.setCellValue("");
					}
										
					// Column J
					Cell R59cell6 = row.createCell(9);
					if (record.getR59_total_deposit_bank() != null) {
					R59cell6.setCellValue(record.getR59_total_deposit_bank().doubleValue());
					R59cell6.setCellStyle(numberStyle);
					} else {
					R59cell6.setCellValue("");
					R59cell6.setCellStyle(textStyle);
					}

					// R60
					row = sheet.getRow(59);
					// Column C
					Cell R60cell1 = row.createCell(2);
					if (record.getR60_deposit_excluding_number() != null) {
						R60cell1.setCellValue(record.getR60_deposit_excluding_number().doubleValue());
						R60cell1.setCellStyle(numberStyle);
					} else {
						R60cell1.setCellValue("");
						R60cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R60cell2 = row.createCell(3);
					if (record.getR60_deposit_excluding_amount() != null) {
						R60cell2.setCellValue(record.getR60_deposit_excluding_amount().doubleValue());
						R60cell2.setCellStyle(numberStyle);
					} else {
						R60cell2.setCellValue("");
						R60cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R60cell3 = row.createCell(4);
					if (record.getR60_deposit_foreign_number() != null) {
						R60cell3.setCellValue(record.getR60_deposit_foreign_number().doubleValue());
						R60cell3.setCellStyle(numberStyle);
					} else {
						R60cell3.setCellValue("");
						R60cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R60cell4 = row.createCell(5);
					if (record.getR60_deposit_foreign_amount() != null) {
						R60cell4.setCellValue(record.getR60_deposit_foreign_amount().doubleValue());
						R60cell4.setCellStyle(numberStyle);
					} else {
						R60cell4.setCellValue("");
						R60cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R60cell5 = row.createCell(8);
					if (record2.getR60_TOTAL_DEPOSIT_EXCEED() != null) {
					R60cell5.setCellValue(record2.getR60_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R60cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R60cell6 = row.createCell(9);
					if (record.getR60_total_deposit_bank() != null) {
					R60cell6.setCellValue(record.getR60_total_deposit_bank().doubleValue());
					R60cell6.setCellStyle(numberStyle);
					} else {
					R60cell6.setCellValue("");
					R60cell6.setCellStyle(textStyle);
					}

					// R63
					row = sheet.getRow(62);
					// Column C
					Cell R63cell1 = row.createCell(2);
					if (record.getR63_deposit_excluding_number() != null) {
						R63cell1.setCellValue(record.getR63_deposit_excluding_number().doubleValue());
						R63cell1.setCellStyle(numberStyle);
					} else {
						R63cell1.setCellValue("");
						R63cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R63cell2 = row.createCell(3);
					if (record.getR63_deposit_excluding_amount() != null) {
						R63cell2.setCellValue(record.getR63_deposit_excluding_amount().doubleValue());
						R63cell2.setCellStyle(numberStyle);
					} else {
						R63cell2.setCellValue("");
						R63cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R63cell3 = row.createCell(4);
					if (record.getR63_deposit_foreign_number() != null) {
						R63cell3.setCellValue(record.getR63_deposit_foreign_number().doubleValue());
						R63cell3.setCellStyle(numberStyle);
					} else {
						R63cell3.setCellValue("");
						R63cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R63cell4 = row.createCell(5);
					if (record.getR63_deposit_foreign_amount() != null) {
						R63cell4.setCellValue(record.getR63_deposit_foreign_amount().doubleValue());
						R63cell4.setCellStyle(numberStyle);
					} else {
						R63cell4.setCellValue("");
						R63cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R63cell5 = row.createCell(8);
					if (record2.getR63_TOTAL_DEPOSIT_EXCEED() != null) {
					R63cell5.setCellValue(record2.getR63_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R63cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R63cell6 = row.createCell(9);
					if (record.getR63_total_deposit_bank() != null) {
					R63cell6.setCellValue(record.getR63_total_deposit_bank().doubleValue());
					R63cell6.setCellStyle(numberStyle);
					} else {
					R63cell6.setCellValue("");
					R63cell6.setCellStyle(textStyle);
					}


					// R64
					row = sheet.getRow(63);
					// Column C
					Cell R64cell1 = row.createCell(2);
					if (record.getR64_deposit_excluding_number() != null) {
						R64cell1.setCellValue(record.getR64_deposit_excluding_number().doubleValue());
						R64cell1.setCellStyle(numberStyle);
					} else {
						R64cell1.setCellValue("");
						R64cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R64cell2 = row.createCell(3);
					if (record.getR64_deposit_excluding_amount() != null) {
						R64cell2.setCellValue(record.getR64_deposit_excluding_amount().doubleValue());
						R64cell2.setCellStyle(numberStyle);
					} else {
						R64cell2.setCellValue("");
						R64cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R64cell3 = row.createCell(4);
					if (record.getR64_deposit_foreign_number() != null) {
						R64cell3.setCellValue(record.getR64_deposit_foreign_number().doubleValue());
						R64cell3.setCellStyle(numberStyle);
					} else {
						R64cell3.setCellValue("");
						R64cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R64cell4 = row.createCell(5);
					if (record.getR64_deposit_foreign_amount() != null) {
						R64cell4.setCellValue(record.getR64_deposit_foreign_amount().doubleValue());
						R64cell4.setCellStyle(numberStyle);
					} else {
						R64cell4.setCellValue("");
						R64cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R64cell5 = row.createCell(8);
					if (record2.getR64_TOTAL_DEPOSIT_EXCEED() != null) {
					R64cell5.setCellValue(record2.getR64_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R64cell5.setCellValue("");
					
										
					// Column J
					Cell R64cell6 = row.createCell(9);
					if (record.getR64_total_deposit_bank() != null) {
					R64cell6.setCellValue(record.getR64_total_deposit_bank().doubleValue());
					R64cell6.setCellStyle(numberStyle);
					} else {
					R64cell6.setCellValue("");
					R64cell6.setCellStyle(textStyle);
					}


					// R65
					row = sheet.getRow(64);
					// Column C
					Cell R65cell1 = row.createCell(2);
					if (record.getR65_deposit_excluding_number() != null) {
						R65cell1.setCellValue(record.getR65_deposit_excluding_number().doubleValue());
						R65cell1.setCellStyle(numberStyle);
					} else {
						R65cell1.setCellValue("");
						R65cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R65cell2 = row.createCell(3);
					if (record.getR65_deposit_excluding_amount() != null) {
						R65cell2.setCellValue(record.getR65_deposit_excluding_amount().doubleValue());
						R65cell2.setCellStyle(numberStyle);
					} else {
						R65cell2.setCellValue("");
						R65cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R65cell3 = row.createCell(4);
					if (record.getR65_deposit_foreign_number() != null) {
						R65cell3.setCellValue(record.getR65_deposit_foreign_number().doubleValue());
						R65cell3.setCellStyle(numberStyle);
					} else {
						R65cell3.setCellValue("");
						R65cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R65cell4 = row.createCell(5);
					if (record.getR65_deposit_foreign_amount() != null) {
						R65cell4.setCellValue(record.getR65_deposit_foreign_amount().doubleValue());
						R65cell4.setCellStyle(numberStyle);
					} else {
						R65cell4.setCellValue("");
						R65cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R65cell5 = row.createCell(8);
					if (record2.getR65_TOTAL_DEPOSIT_EXCEED() != null) {
					R65cell5.setCellValue(record2.getR65_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R65cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R65cell6 = row.createCell(9);
					if (record.getR65_total_deposit_bank() != null) {
					R65cell6.setCellValue(record.getR65_total_deposit_bank().doubleValue());
					R65cell6.setCellStyle(numberStyle);
					} else {
					R65cell6.setCellValue("");
					R65cell6.setCellStyle(textStyle);
					}

					// R66
					row = sheet.getRow(65);
					// Column C
					Cell R66cell1 = row.createCell(2);
					if (record.getR66_deposit_excluding_number() != null) {
						R66cell1.setCellValue(record.getR66_deposit_excluding_number().doubleValue());
						R66cell1.setCellStyle(numberStyle);
					} else {
						R66cell1.setCellValue("");
						R66cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R66cell2 = row.createCell(3);
					if (record.getR66_deposit_excluding_amount() != null) {
						R66cell2.setCellValue(record.getR66_deposit_excluding_amount().doubleValue());
						R66cell2.setCellStyle(numberStyle);
					} else {
						R66cell2.setCellValue("");
						R66cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R66cell3 = row.createCell(4);
					if (record.getR66_deposit_foreign_number() != null) {
						R66cell3.setCellValue(record.getR66_deposit_foreign_number().doubleValue());
						R66cell3.setCellStyle(numberStyle);
					} else {
						R66cell3.setCellValue("");
						R66cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R66cell4 = row.createCell(5);
					if (record.getR66_deposit_foreign_amount() != null) {
						R66cell4.setCellValue(record.getR66_deposit_foreign_amount().doubleValue());
						R66cell4.setCellStyle(numberStyle);
					} else {
						R66cell4.setCellValue("");
						R66cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R66cell5 = row.createCell(8);
					if (record2.getR66_TOTAL_DEPOSIT_EXCEED() != null) {
					R66cell5.setCellValue(record2.getR66_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R66cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R66cell6 = row.createCell(9);
					if (record.getR66_total_deposit_bank() != null) {
					R66cell6.setCellValue(record.getR66_total_deposit_bank().doubleValue());
					R66cell6.setCellStyle(numberStyle);
					} else {
					R66cell6.setCellValue("");
					R66cell6.setCellStyle(textStyle);
					}


					// R67
					row = sheet.getRow(66);
					// Column C
					Cell R67cell1 = row.createCell(2);
					if (record.getR67_deposit_excluding_number() != null) {
						R67cell1.setCellValue(record.getR67_deposit_excluding_number().doubleValue());
						R67cell1.setCellStyle(numberStyle);
					} else {
						R67cell1.setCellValue("");
						R67cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R67cell2 = row.createCell(3);
					if (record.getR67_deposit_excluding_amount() != null) {
						R67cell2.setCellValue(record.getR67_deposit_excluding_amount().doubleValue());
						R67cell2.setCellStyle(numberStyle);
					} else {
						R67cell2.setCellValue("");
						R67cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R67cell3 = row.createCell(4);
					if (record.getR67_deposit_foreign_number() != null) {
						R67cell3.setCellValue(record.getR67_deposit_foreign_number().doubleValue());
						R67cell3.setCellStyle(numberStyle);
					} else {
						R67cell3.setCellValue("");
						R67cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R67cell4 = row.createCell(5);
					if (record.getR67_deposit_foreign_amount() != null) {
						R67cell4.setCellValue(record.getR67_deposit_foreign_amount().doubleValue());
						R67cell4.setCellStyle(numberStyle);
					} else {
						R67cell4.setCellValue("");
						R67cell4.setCellStyle(textStyle);
					}

					
					// Column I
					Cell R67cell5 = row.createCell(8);
					if (record2.getR67_TOTAL_DEPOSIT_EXCEED() != null) {
					R67cell5.setCellValue(record2.getR67_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R67cell5.setCellValue("");
					}
										
					// Column J
					Cell R67cell6 = row.createCell(9);
					if (record.getR67_total_deposit_bank() != null) {
					R67cell6.setCellValue(record.getR67_total_deposit_bank().doubleValue());
					R67cell6.setCellStyle(numberStyle);
					} else {
					R67cell6.setCellValue("");
					R67cell6.setCellStyle(textStyle);
					}

					// R68
					row = sheet.getRow(67);
					// Column C
					Cell R68cell1 = row.createCell(2);
					if (record.getR68_deposit_excluding_number() != null) {
						R68cell1.setCellValue(record.getR68_deposit_excluding_number().doubleValue());
						R68cell1.setCellStyle(numberStyle);
					} else {
						R68cell1.setCellValue("");
						R68cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R68cell2 = row.createCell(3);
					if (record.getR68_deposit_excluding_amount() != null) {
						R68cell2.setCellValue(record.getR68_deposit_excluding_amount().doubleValue());
						R68cell2.setCellStyle(numberStyle);
					} else {
						R68cell2.setCellValue("");
						R68cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R68cell3 = row.createCell(4);
					if (record.getR68_deposit_foreign_number() != null) {
						R68cell3.setCellValue(record.getR68_deposit_foreign_number().doubleValue());
						R68cell3.setCellStyle(numberStyle);
					} else {
						R68cell3.setCellValue("");
						R68cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R68cell4 = row.createCell(5);
					if (record.getR68_deposit_foreign_amount() != null) {
						R68cell4.setCellValue(record.getR68_deposit_foreign_amount().doubleValue());
						R68cell4.setCellStyle(numberStyle);
					} else {
						R68cell4.setCellValue("");
						R68cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R68cell5 = row.createCell(8);
					if (record2.getR68_TOTAL_DEPOSIT_EXCEED() != null) {
					R68cell5.setCellValue(record2.getR68_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R68cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R68cell6 = row.createCell(9);
					if (record.getR68_total_deposit_bank() != null) {
					R68cell6.setCellValue(record.getR68_total_deposit_bank().doubleValue());
					R68cell6.setCellStyle(numberStyle);
					} else {
					R68cell6.setCellValue("");
					R68cell6.setCellStyle(textStyle);
					}

					// R71
					row = sheet.getRow(70);
					// Column C
					Cell R71cell1 = row.createCell(2);
					if (record.getR71_deposit_excluding_number() != null) {
						R71cell1.setCellValue(record.getR71_deposit_excluding_number().doubleValue());
						R71cell1.setCellStyle(numberStyle);
					} else {
						R71cell1.setCellValue("");
						R71cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R71cell2 = row.createCell(3);
					if (record.getR71_deposit_excluding_amount() != null) {
						R71cell2.setCellValue(record.getR71_deposit_excluding_amount().doubleValue());
						R71cell2.setCellStyle(numberStyle);
					} else {
						R71cell2.setCellValue("");
						R71cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R71cell3 = row.createCell(4);
					if (record.getR71_deposit_foreign_number() != null) {
						R71cell3.setCellValue(record.getR71_deposit_foreign_number().doubleValue());
						R71cell3.setCellStyle(numberStyle);
					} else {
						R71cell3.setCellValue("");
						R71cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R71cell4 = row.createCell(5);
					if (record.getR71_deposit_foreign_amount() != null) {
						R71cell4.setCellValue(record.getR71_deposit_foreign_amount().doubleValue());
						R71cell4.setCellStyle(numberStyle);
					} else {
						R71cell4.setCellValue("");
						R71cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R71cell5 = row.createCell(8);
					if (record2.getR71_TOTAL_DEPOSIT_EXCEED() != null) {
					R71cell5.setCellValue(record2.getR71_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R71cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R71cell6 = row.createCell(9);
					if (record.getR71_total_deposit_bank() != null) {
					R71cell6.setCellValue(record.getR71_total_deposit_bank().doubleValue());
					R71cell6.setCellStyle(numberStyle);
					} else {
					R71cell6.setCellValue("");
					R71cell6.setCellStyle(textStyle);
					}

					// R72
					row = sheet.getRow(71);
					// Column C
					Cell R72cell1 = row.createCell(2);
					if (record.getR72_deposit_excluding_number() != null) {
						R72cell1.setCellValue(record.getR72_deposit_excluding_number().doubleValue());
						R72cell1.setCellStyle(numberStyle);
					} else {
						R72cell1.setCellValue("");
						R72cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R72cell2 = row.createCell(3);
					if (record.getR72_deposit_excluding_amount() != null) {
						R72cell2.setCellValue(record.getR72_deposit_excluding_amount().doubleValue());
						R72cell2.setCellStyle(numberStyle);
					} else {
						R72cell2.setCellValue("");
						R72cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R72cell3 = row.createCell(4);
					if (record.getR72_deposit_foreign_number() != null) {
						R72cell3.setCellValue(record.getR72_deposit_foreign_number().doubleValue());
						R72cell3.setCellStyle(numberStyle);
					} else {
						R72cell3.setCellValue("");
						R72cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R72cell4 = row.createCell(5);
					if (record.getR72_deposit_foreign_amount() != null) {
						R72cell4.setCellValue(record.getR72_deposit_foreign_amount().doubleValue());
						R72cell4.setCellStyle(numberStyle);
					} else {
						R72cell4.setCellValue("");
						R72cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R72cell5 = row.createCell(8);
					if (record2.getR72_TOTAL_DEPOSIT_EXCEED() != null) {
					R72cell5.setCellValue(record2.getR72_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R72cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R72cell6 = row.createCell(9);
					if (record.getR72_total_deposit_bank() != null) {
					R72cell6.setCellValue(record.getR72_total_deposit_bank().doubleValue());
					R72cell6.setCellStyle(numberStyle);
					} else {
					R72cell6.setCellValue("");
					R72cell6.setCellStyle(textStyle);
					}


					// R73
					row = sheet.getRow(72);
					// Column C
					Cell R73cell1 = row.createCell(2);
					if (record.getR73_deposit_excluding_number() != null) {
						R73cell1.setCellValue(record.getR73_deposit_excluding_number().doubleValue());
						R73cell1.setCellStyle(numberStyle);
					} else {
						R73cell1.setCellValue("");
						R73cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R73cell2 = row.createCell(3);
					if (record.getR73_deposit_excluding_amount() != null) {
						R73cell2.setCellValue(record.getR73_deposit_excluding_amount().doubleValue());
						R73cell2.setCellStyle(numberStyle);
					} else {
						R73cell2.setCellValue("");
						R73cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R73cell3 = row.createCell(4);
					if (record.getR73_deposit_foreign_number() != null) {
						R73cell3.setCellValue(record.getR73_deposit_foreign_number().doubleValue());
						R73cell3.setCellStyle(numberStyle);
					} else {
						R73cell3.setCellValue("");
						R73cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R73cell4 = row.createCell(5);
					if (record.getR73_deposit_foreign_amount() != null) {
						R73cell4.setCellValue(record.getR73_deposit_foreign_amount().doubleValue());
						R73cell4.setCellStyle(numberStyle);
					} else {
						R73cell4.setCellValue("");
						R73cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R73cell5 = row.createCell(8);
					if (record2.getR73_TOTAL_DEPOSIT_EXCEED() != null) {
					R73cell5.setCellValue(record2.getR73_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R73cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R73cell6 = row.createCell(9);
					if (record.getR73_total_deposit_bank() != null) {
					R73cell6.setCellValue(record.getR73_total_deposit_bank().doubleValue());
					R73cell6.setCellStyle(numberStyle);
					} else {
					R73cell6.setCellValue("");
					R73cell6.setCellStyle(textStyle);
					}


					// R74
					row = sheet.getRow(73);
					// Column C
					Cell R74cell1 = row.createCell(2);
					if (record.getR74_deposit_excluding_number() != null) {
						R74cell1.setCellValue(record.getR74_deposit_excluding_number().doubleValue());
						R74cell1.setCellStyle(numberStyle);
					} else {
						R74cell1.setCellValue("");
						R74cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R74cell2 = row.createCell(3);
					if (record.getR74_deposit_excluding_amount() != null) {
						R74cell2.setCellValue(record.getR74_deposit_excluding_amount().doubleValue());
						R74cell2.setCellStyle(numberStyle);
					} else {
						R74cell2.setCellValue("");
						R74cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R74cell3 = row.createCell(4);
					if (record.getR74_deposit_foreign_number() != null) {
						R74cell3.setCellValue(record.getR74_deposit_foreign_number().doubleValue());
						R74cell3.setCellStyle(numberStyle);
					} else {
						R74cell3.setCellValue("");
						R74cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R74cell4 = row.createCell(5);
					if (record.getR74_deposit_foreign_amount() != null) {
						R74cell4.setCellValue(record.getR74_deposit_foreign_amount().doubleValue());
						R74cell4.setCellStyle(numberStyle);
					} else {
						R74cell4.setCellValue("");
						R74cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R74cell5 = row.createCell(8);
					if (record2.getR74_TOTAL_DEPOSIT_EXCEED() != null) {
					R74cell5.setCellValue(record2.getR74_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R74cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R74cell6 = row.createCell(9);
					if (record.getR74_total_deposit_bank() != null) {
					R74cell6.setCellValue(record.getR74_total_deposit_bank().doubleValue());
					R74cell6.setCellStyle(numberStyle);
					} else {
					R74cell6.setCellValue("");
					R74cell6.setCellStyle(textStyle);
					}



					// R75
					row = sheet.getRow(74);
					// Column C
					Cell R75cell1 = row.createCell(2);
					if (record.getR75_deposit_excluding_number() != null) {
						R75cell1.setCellValue(record.getR75_deposit_excluding_number().doubleValue());
						R75cell1.setCellStyle(numberStyle);
					} else {
						R75cell1.setCellValue("");
						R75cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R75cell2 = row.createCell(3);
					if (record.getR75_deposit_excluding_amount() != null) {
						R75cell2.setCellValue(record.getR75_deposit_excluding_amount().doubleValue());
						R75cell2.setCellStyle(numberStyle);
					} else {
						R75cell2.setCellValue("");
						R75cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R75cell3 = row.createCell(4);
					if (record.getR75_deposit_foreign_number() != null) {
						R75cell3.setCellValue(record.getR75_deposit_foreign_number().doubleValue());
						R75cell3.setCellStyle(numberStyle);
					} else {
						R75cell3.setCellValue("");
						R75cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R75cell4 = row.createCell(5);
					if (record.getR75_deposit_foreign_amount() != null) {
						R75cell4.setCellValue(record.getR75_deposit_foreign_amount().doubleValue());
						R75cell4.setCellStyle(numberStyle);
					} else {
						R75cell4.setCellValue("");
						R75cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R75cell5 = row.createCell(8);
					if (record2.getR75_TOTAL_DEPOSIT_EXCEED() != null) {
					R75cell5.setCellValue(record2.getR75_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R75cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R75cell6 = row.createCell(9);
					if (record.getR75_total_deposit_bank() != null) {
					R75cell6.setCellValue(record.getR75_total_deposit_bank().doubleValue());
					R75cell6.setCellStyle(numberStyle);
					} else {
					R75cell6.setCellValue("");
					R75cell6.setCellStyle(textStyle);
					}


					// R76
					row = sheet.getRow(75);
					// Column C
					Cell R76cell1 = row.createCell(2);
					if (record.getR76_deposit_excluding_number() != null) {
						R76cell1.setCellValue(record.getR76_deposit_excluding_number().doubleValue());
						R76cell1.setCellStyle(numberStyle);
					} else {
						R76cell1.setCellValue("");
						R76cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R76cell2 = row.createCell(3);
					if (record.getR76_deposit_excluding_amount() != null) {
						R76cell2.setCellValue(record.getR76_deposit_excluding_amount().doubleValue());
						R76cell2.setCellStyle(numberStyle);
					} else {
						R76cell2.setCellValue("");
						R76cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R76cell3 = row.createCell(4);
					if (record.getR76_deposit_foreign_number() != null) {
						R76cell3.setCellValue(record.getR76_deposit_foreign_number().doubleValue());
						R76cell3.setCellStyle(numberStyle);
					} else {
						R76cell3.setCellValue("");
						R76cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R76cell4 = row.createCell(5);
					if (record.getR76_deposit_foreign_amount() != null) {
						R76cell4.setCellValue(record.getR76_deposit_foreign_amount().doubleValue());
						R76cell4.setCellStyle(numberStyle);
					} else {
						R76cell4.setCellValue("");
						R76cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R76cell5 = row.createCell(8);
					if (record2.getR76_TOTAL_DEPOSIT_EXCEED() != null) {
					R76cell5.setCellValue(record2.getR76_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R76cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R76cell6 = row.createCell(9);
					if (record.getR76_total_deposit_bank() != null) {
					R76cell6.setCellValue(record.getR76_total_deposit_bank().doubleValue());
					R76cell6.setCellStyle(numberStyle);
					} else {
					R76cell6.setCellValue("");
					R76cell6.setCellStyle(textStyle);
					}


					// R79
					row = sheet.getRow(78);
					// Column C
					Cell R79cell1 = row.createCell(2);
					if (record.getR79_deposit_excluding_number() != null) {
						R79cell1.setCellValue(record.getR79_deposit_excluding_number().doubleValue());
						R79cell1.setCellStyle(numberStyle);
					} else {
						R79cell1.setCellValue("");
						R79cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R79cell2 = row.createCell(3);
					if (record.getR79_deposit_excluding_amount() != null) {
						R79cell2.setCellValue(record.getR79_deposit_excluding_amount().doubleValue());
						R79cell2.setCellStyle(numberStyle);
					} else {
						R79cell2.setCellValue("");
						R79cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R79cell3 = row.createCell(4);
					if (record.getR79_deposit_foreign_number() != null) {
						R79cell3.setCellValue(record.getR79_deposit_foreign_number().doubleValue());
						R79cell3.setCellStyle(numberStyle);
					} else {
						R79cell3.setCellValue("");
						R79cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R79cell4 = row.createCell(5);
					if (record.getR79_deposit_foreign_amount() != null) {
						R79cell4.setCellValue(record.getR79_deposit_foreign_amount().doubleValue());
						R79cell4.setCellStyle(numberStyle);
					} else {
						R79cell4.setCellValue("");
						R79cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R79cell5 = row.createCell(8);
					if (record2.getR79_TOTAL_DEPOSIT_EXCEED() != null) {
					R79cell5.setCellValue(record2.getR79_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R79cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R79cell6 = row.createCell(9);
					if (record.getR79_total_deposit_bank() != null) {
					R79cell6.setCellValue(record.getR79_total_deposit_bank().doubleValue());
					R79cell6.setCellStyle(numberStyle);
					} else {
					R79cell6.setCellValue("");
					R79cell6.setCellStyle(textStyle);
					}

					// R80
					row = sheet.getRow(79);
					// Column C
					Cell R80cell1 = row.createCell(2);
					if (record.getR80_deposit_excluding_number() != null) {
						R80cell1.setCellValue(record.getR80_deposit_excluding_number().doubleValue());
						R80cell1.setCellStyle(numberStyle);
					} else {
						R80cell1.setCellValue("");
						R80cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R80cell2 = row.createCell(3);
					if (record.getR80_deposit_excluding_amount() != null) {
						R80cell2.setCellValue(record.getR80_deposit_excluding_amount().doubleValue());
						R80cell2.setCellStyle(numberStyle);
					} else {
						R80cell2.setCellValue("");
						R80cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R80cell3 = row.createCell(4);
					if (record.getR80_deposit_foreign_number() != null) {
						R80cell3.setCellValue(record.getR80_deposit_foreign_number().doubleValue());
						R80cell3.setCellStyle(numberStyle);
					} else {
						R80cell3.setCellValue("");
						R80cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R80cell4 = row.createCell(5);
					if (record.getR80_deposit_foreign_amount() != null) {
						R80cell4.setCellValue(record.getR80_deposit_foreign_amount().doubleValue());
						R80cell4.setCellStyle(numberStyle);
					} else {
						R80cell4.setCellValue("");
						R80cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R80cell5 = row.createCell(8);
					if (record2.getR80_TOTAL_DEPOSIT_EXCEED() != null) {
					R80cell5.setCellValue(record2.getR80_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R80cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R80cell6 = row.createCell(9);
					if (record.getR80_total_deposit_bank() != null) {
					R80cell6.setCellValue(record.getR80_total_deposit_bank().doubleValue());
					R80cell6.setCellStyle(numberStyle);
					} else {
					R80cell6.setCellValue("");
					R80cell6.setCellStyle(textStyle);
					}


					// R81
					row = sheet.getRow(80);
					// Column C
					Cell R81cell1 = row.createCell(2);
					if (record.getR81_deposit_excluding_number() != null) {
						R81cell1.setCellValue(record.getR81_deposit_excluding_number().doubleValue());
						R81cell1.setCellStyle(numberStyle);
					} else {
						R81cell1.setCellValue("");
						R81cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R81cell2 = row.createCell(3);
					if (record.getR81_deposit_excluding_amount() != null) {
						R81cell2.setCellValue(record.getR81_deposit_excluding_amount().doubleValue());
						R81cell2.setCellStyle(numberStyle);
					} else {
						R81cell2.setCellValue("");
						R81cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R81cell3 = row.createCell(4);
					if (record.getR81_deposit_foreign_number() != null) {
						R81cell3.setCellValue(record.getR81_deposit_foreign_number().doubleValue());
						R81cell3.setCellStyle(numberStyle);
					} else {
						R81cell3.setCellValue("");
						R81cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R81cell4 = row.createCell(5);
					if (record.getR81_deposit_foreign_amount() != null) {
						R81cell4.setCellValue(record.getR81_deposit_foreign_amount().doubleValue());
						R81cell4.setCellStyle(numberStyle);
					} else {
						R81cell4.setCellValue("");
						R81cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R81cell5 = row.createCell(8);
					if (record2.getR81_TOTAL_DEPOSIT_EXCEED() != null) {
					R81cell5.setCellValue(record2.getR81_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R81cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R81cell6 = row.createCell(9);
					if (record.getR81_total_deposit_bank() != null) {
					R81cell6.setCellValue(record.getR81_total_deposit_bank().doubleValue());
					R81cell6.setCellStyle(numberStyle);
					} else {
					R81cell6.setCellValue("");
					R81cell6.setCellStyle(textStyle);
					}


					// R82
					row = sheet.getRow(81);
					// Column C
					Cell R82cell1 = row.createCell(2);
					if (record.getR82_deposit_excluding_number() != null) {
						R82cell1.setCellValue(record.getR82_deposit_excluding_number().doubleValue());
						R82cell1.setCellStyle(numberStyle);
					} else {
						R82cell1.setCellValue("");
						R82cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R82cell2 = row.createCell(3);
					if (record.getR82_deposit_excluding_amount() != null) {
						R82cell2.setCellValue(record.getR82_deposit_excluding_amount().doubleValue());
						R82cell2.setCellStyle(numberStyle);
					} else {
						R82cell2.setCellValue("");
						R82cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R82cell3 = row.createCell(4);
					if (record.getR82_deposit_foreign_number() != null) {
						R82cell3.setCellValue(record.getR82_deposit_foreign_number().doubleValue());
						R82cell3.setCellStyle(numberStyle);
					} else {
						R82cell3.setCellValue("");
						R82cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R82cell4 = row.createCell(5);
					if (record.getR82_deposit_foreign_amount() != null) {
						R82cell4.setCellValue(record.getR82_deposit_foreign_amount().doubleValue());
						R82cell4.setCellStyle(numberStyle);
					} else {
						R82cell4.setCellValue("");
						R82cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R82cell5 = row.createCell(8);
					if (record2.getR82_TOTAL_DEPOSIT_EXCEED() != null) {
					R82cell5.setCellValue(record2.getR82_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R82cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R82cell6 = row.createCell(9);
					if (record.getR82_total_deposit_bank() != null) {
					R82cell6.setCellValue(record.getR82_total_deposit_bank().doubleValue());
					R82cell6.setCellStyle(numberStyle);
					} else {
					R82cell6.setCellValue("");
					R82cell6.setCellStyle(textStyle);
					}


					// R83
					row = sheet.getRow(82);
					// Column C
					Cell R83cell1 = row.createCell(2);
					if (record.getR83_deposit_excluding_number() != null) {
						R83cell1.setCellValue(record.getR83_deposit_excluding_number().doubleValue());
						R83cell1.setCellStyle(numberStyle);
					} else {
						R83cell1.setCellValue("");
						R83cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R83cell2 = row.createCell(3);
					if (record.getR83_deposit_excluding_amount() != null) {
						R83cell2.setCellValue(record.getR83_deposit_excluding_amount().doubleValue());
						R83cell2.setCellStyle(numberStyle);
					} else {
						R83cell2.setCellValue("");
						R83cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R83cell3 = row.createCell(4);
					if (record.getR83_deposit_foreign_number() != null) {
						R83cell3.setCellValue(record.getR83_deposit_foreign_number().doubleValue());
						R83cell3.setCellStyle(numberStyle);
					} else {
						R83cell3.setCellValue("");
						R83cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R83cell4 = row.createCell(5);
					if (record.getR83_deposit_foreign_amount() != null) {
						R83cell4.setCellValue(record.getR83_deposit_foreign_amount().doubleValue());
						R83cell4.setCellStyle(numberStyle);
					} else {
						R83cell4.setCellValue("");
						R83cell4.setCellStyle(textStyle);
					}
					// Column I
					Cell R83cell5 = row.createCell(8);
					if (record2.getR83_TOTAL_DEPOSIT_EXCEED() != null) {
					R83cell5.setCellValue(record2.getR83_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R83cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R83cell6 = row.createCell(9);
					if (record.getR83_total_deposit_bank() != null) {
					R83cell6.setCellValue(record.getR83_total_deposit_bank().doubleValue());
					R83cell6.setCellStyle(numberStyle);
					} else {
					R83cell6.setCellValue("");
					R83cell6.setCellStyle(textStyle);
					}

					

					// R84
					row = sheet.getRow(83);
					// Column C
					Cell R84cell1 = row.createCell(2);
					if (record.getR84_deposit_excluding_number() != null) {
						R84cell1.setCellValue(record.getR84_deposit_excluding_number().doubleValue());
						R84cell1.setCellStyle(numberStyle);
					} else {
						R84cell1.setCellValue("");
						R84cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R84cell2 = row.createCell(3);
					if (record.getR84_deposit_excluding_amount() != null) {
						R84cell2.setCellValue(record.getR84_deposit_excluding_amount().doubleValue());
						R84cell2.setCellStyle(numberStyle);
					} else {
						R84cell2.setCellValue("");
						R84cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R84cell3 = row.createCell(4);
					if (record.getR84_deposit_foreign_number() != null) {
						R84cell3.setCellValue(record.getR84_deposit_foreign_number().doubleValue());
						R84cell3.setCellStyle(numberStyle);
					} else {
						R84cell3.setCellValue("");
						R84cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R84cell4 = row.createCell(5);
					if (record.getR84_deposit_foreign_amount() != null) {
						R84cell4.setCellValue(record.getR84_deposit_foreign_amount().doubleValue());
						R84cell4.setCellStyle(numberStyle);
					} else {
						R84cell4.setCellValue("");
						R84cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R84cell5 = row.createCell(8);
					if (record2.getR84_TOTAL_DEPOSIT_EXCEED() != null) {
					R84cell5.setCellValue(record2.getR84_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R84cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R84cell6 = row.createCell(9);
					if (record.getR84_total_deposit_bank() != null) {
					R84cell6.setCellValue(record.getR84_total_deposit_bank().doubleValue());
					R84cell6.setCellStyle(numberStyle);
					} else {
					R84cell6.setCellValue("");
					R84cell6.setCellStyle(textStyle);
					}


					// R87
					row = sheet.getRow(86);
					// Column C
					Cell R87cell1 = row.createCell(2);
					if (record1.getR87_deposit_excluding_number() != null) {
						R87cell1.setCellValue(record1.getR87_deposit_excluding_number().doubleValue());
						R87cell1.setCellStyle(numberStyle);
					} else {
						R87cell1.setCellValue("");
						R87cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R87cell2 = row.createCell(3);
					if (record1.getR87_deposit_excluding_amount() != null) {
						R87cell2.setCellValue(record1.getR87_deposit_excluding_amount().doubleValue());
						R87cell2.setCellStyle(numberStyle);
					} else {
						R87cell2.setCellValue("");
						R87cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R87cell3 = row.createCell(4);
					if (record1.getR87_deposit_foreign_number() != null) {
						R87cell3.setCellValue(record1.getR87_deposit_foreign_number().doubleValue());
						R87cell3.setCellStyle(numberStyle);
					} else {
						R87cell3.setCellValue("");
						R87cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R87cell4 = row.createCell(5);
					if (record1.getR87_deposit_foreign_amount() != null) {
						R87cell4.setCellValue(record1.getR87_deposit_foreign_amount().doubleValue());
						R87cell4.setCellStyle(numberStyle);
					} else {
						R87cell4.setCellValue("");
						R87cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R87cell5 = row.createCell(8);
					if (record2.getR87_TOTAL_DEPOSIT_EXCEED() != null) {
					R87cell5.setCellValue(record2.getR87_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R87cell5.setCellValue("");
					}
										
					// Column J
					Cell R87cell6 = row.createCell(9);
					if (record1.getR87_total_deposit_bank() != null) {
					R87cell6.setCellValue(record1.getR87_total_deposit_bank().doubleValue());
					R87cell6.setCellStyle(numberStyle);
					} else {
					R87cell6.setCellValue("");
					R87cell6.setCellStyle(textStyle);
					}

					// R88
					row = sheet.getRow(87);
					// Column C
					Cell R88cell1 = row.createCell(2);
					if (record1.getR88_deposit_excluding_number() != null) {
						R88cell1.setCellValue(record1.getR88_deposit_excluding_number().doubleValue());
						R88cell1.setCellStyle(numberStyle);
					} else {
						R88cell1.setCellValue("");
						R88cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R88cell2 = row.createCell(3);
					if (record1.getR88_deposit_excluding_amount() != null) {
						R88cell2.setCellValue(record1.getR88_deposit_excluding_amount().doubleValue());
						R88cell2.setCellStyle(numberStyle);
					} else {
						R88cell2.setCellValue("");
						R88cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R88cell3 = row.createCell(4);
					if (record1.getR88_deposit_foreign_number() != null) {
						R88cell3.setCellValue(record1.getR88_deposit_foreign_number().doubleValue());
						R88cell3.setCellStyle(numberStyle);
					} else {
						R88cell3.setCellValue("");
						R88cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R88cell4 = row.createCell(5);
					if (record1.getR88_deposit_foreign_amount() != null) {
						R88cell4.setCellValue(record1.getR88_deposit_foreign_amount().doubleValue());
						R88cell4.setCellStyle(numberStyle);
					} else {
						R88cell4.setCellValue("");
						R88cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R88cell5 = row.createCell(8);
					if (record2.getR88_TOTAL_DEPOSIT_EXCEED() != null) {
					R88cell5.setCellValue(record2.getR88_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R88cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R88cell6 = row.createCell(9);
					if (record1.getR88_total_deposit_bank() != null) {
					R88cell6.setCellValue(record1.getR88_total_deposit_bank().doubleValue());
					R88cell6.setCellStyle(numberStyle);
					} else {
					R88cell6.setCellValue("");
					R88cell6.setCellStyle(textStyle);
					}

					// R89
					row = sheet.getRow(88);
					// Column C
					Cell R89cell1 = row.createCell(2);
					if (record1.getR89_deposit_excluding_number() != null) {
						R89cell1.setCellValue(record1.getR89_deposit_excluding_number().doubleValue());
						R89cell1.setCellStyle(numberStyle);
					} else {
						R89cell1.setCellValue("");
						R89cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R89cell2 = row.createCell(3);
					if (record1.getR89_deposit_excluding_amount() != null) {
						R89cell2.setCellValue(record1.getR89_deposit_excluding_amount().doubleValue());
						R89cell2.setCellStyle(numberStyle);
					} else {
						R89cell2.setCellValue("");
						R89cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R89cell3 = row.createCell(4);
					if (record1.getR89_deposit_foreign_number() != null) {
						R89cell3.setCellValue(record1.getR89_deposit_foreign_number().doubleValue());
						R89cell3.setCellStyle(numberStyle);
					} else {
						R89cell3.setCellValue("");
						R89cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R89cell4 = row.createCell(5);
					if (record1.getR89_deposit_foreign_amount() != null) {
						R89cell4.setCellValue(record1.getR89_deposit_foreign_amount().doubleValue());
						R89cell4.setCellStyle(numberStyle);
					} else {
						R89cell4.setCellValue("");
						R89cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R89cell5 = row.createCell(8);
					if (record2.getR89_TOTAL_DEPOSIT_EXCEED() != null) {
					R89cell5.setCellValue(record2.getR89_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R89cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R89cell6 = row.createCell(9);
					if (record1.getR89_total_deposit_bank() != null) {
					R89cell6.setCellValue(record1.getR89_total_deposit_bank().doubleValue());
					R89cell6.setCellStyle(numberStyle);
					} else {
					R89cell6.setCellValue("");
					R89cell6.setCellStyle(textStyle);
					}


					// R90
					row = sheet.getRow(89);
					// Column C
					Cell R90cell1 = row.createCell(2);
					if (record1.getR90_deposit_excluding_number() != null) {
						R90cell1.setCellValue(record1.getR90_deposit_excluding_number().doubleValue());
						R90cell1.setCellStyle(numberStyle);
					} else {
						R90cell1.setCellValue("");
						R90cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R90cell2 = row.createCell(3);
					if (record1.getR90_deposit_excluding_amount() != null) {
						R90cell2.setCellValue(record1.getR90_deposit_excluding_amount().doubleValue());
						R90cell2.setCellStyle(numberStyle);
					} else {
						R90cell2.setCellValue("");
						R90cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R90cell3 = row.createCell(4);
					if (record1.getR90_deposit_foreign_number() != null) {
						R90cell3.setCellValue(record1.getR90_deposit_foreign_number().doubleValue());
						R90cell3.setCellStyle(numberStyle);
					} else {
						R90cell3.setCellValue("");
						R90cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R90cell4 = row.createCell(5);
					if (record1.getR90_deposit_foreign_amount() != null) {
						R90cell4.setCellValue(record1.getR90_deposit_foreign_amount().doubleValue());
						R90cell4.setCellStyle(numberStyle);
					} else {
						R90cell4.setCellValue("");
						R90cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R90cell5 = row.createCell(8);
					if (record2.getR90_TOTAL_DEPOSIT_EXCEED() != null) {
					R90cell5.setCellValue(record2.getR90_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R90cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R90cell6 = row.createCell(9);
					if (record1.getR90_total_deposit_bank() != null) {
					R90cell6.setCellValue(record1.getR90_total_deposit_bank().doubleValue());
					R90cell6.setCellStyle(numberStyle);
					} else {
					R90cell6.setCellValue("");
					R90cell6.setCellStyle(textStyle);
					}

					// R91
					row = sheet.getRow(90);
					// Column C
					Cell R91cell1 = row.createCell(2);
					if (record1.getR91_deposit_excluding_number() != null) {
						R91cell1.setCellValue(record1.getR91_deposit_excluding_number().doubleValue());
						R91cell1.setCellStyle(numberStyle);
					} else {
						R91cell1.setCellValue("");
						R91cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R91cell2 = row.createCell(3);
					if (record1.getR91_deposit_excluding_amount() != null) {
						R91cell2.setCellValue(record1.getR91_deposit_excluding_amount().doubleValue());
						R91cell2.setCellStyle(numberStyle);
					} else {
						R91cell2.setCellValue("");
						R91cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R91cell3 = row.createCell(4);
					if (record1.getR91_deposit_foreign_number() != null) {
						R91cell3.setCellValue(record1.getR91_deposit_foreign_number().doubleValue());
						R91cell3.setCellStyle(numberStyle);
					} else {
						R91cell3.setCellValue("");
						R91cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R91cell4 = row.createCell(5);
					if (record1.getR91_deposit_foreign_amount() != null) {
						R91cell4.setCellValue(record1.getR91_deposit_foreign_amount().doubleValue());
						R91cell4.setCellStyle(numberStyle);
					} else {
						R91cell4.setCellValue("");
						R91cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R91cell5 = row.createCell(8);
					if (record2.getR91_TOTAL_DEPOSIT_EXCEED() != null) {
					R91cell5.setCellValue(record2.getR91_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R91cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R91cell6 = row.createCell(9);
					if (record1.getR91_total_deposit_bank() != null) {
					R91cell6.setCellValue(record1.getR91_total_deposit_bank().doubleValue());
					R91cell6.setCellStyle(numberStyle);
					} else {
					R91cell6.setCellValue("");
					R91cell6.setCellStyle(textStyle);
					}

					// R92
					row = sheet.getRow(91);
					// Column C
					Cell R92cell1 = row.createCell(2);
					if (record1.getR92_deposit_excluding_number() != null) {
						R92cell1.setCellValue(record1.getR92_deposit_excluding_number().doubleValue());
						R92cell1.setCellStyle(numberStyle);
					} else {
						R92cell1.setCellValue("");
						R92cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R92cell2 = row.createCell(3);
					if (record1.getR92_deposit_excluding_amount() != null) {
						R92cell2.setCellValue(record1.getR92_deposit_excluding_amount().doubleValue());
						R92cell2.setCellStyle(numberStyle);
					} else {
						R92cell2.setCellValue("");
						R92cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R92cell3 = row.createCell(4);
					if (record1.getR92_deposit_foreign_number() != null) {
						R92cell3.setCellValue(record1.getR92_deposit_foreign_number().doubleValue());
						R92cell3.setCellStyle(numberStyle);
					} else {
						R92cell3.setCellValue("");
						R92cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R92cell4 = row.createCell(5);
					if (record1.getR92_deposit_foreign_amount() != null) {
						R92cell4.setCellValue(record1.getR92_deposit_foreign_amount().doubleValue());
						R92cell4.setCellStyle(numberStyle);
					} else {
						R92cell4.setCellValue("");
						R92cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R92cell5 = row.createCell(8);
					if (record2.getR92_TOTAL_DEPOSIT_EXCEED() != null) {
					R92cell5.setCellValue(record2.getR92_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R92cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R92cell6 = row.createCell(9);
					if (record1.getR92_total_deposit_bank() != null) {
					R92cell6.setCellValue(record1.getR92_total_deposit_bank().doubleValue());
					R92cell6.setCellStyle(numberStyle);
					} else {
					R92cell6.setCellValue("");
					R92cell6.setCellStyle(textStyle);
					}

					// R95
					row = sheet.getRow(94);
					// Column C
					Cell R95cell1 = row.createCell(2);
					if (record1.getR95_deposit_excluding_number() != null) {
						R95cell1.setCellValue(record1.getR95_deposit_excluding_number().doubleValue());
						R95cell1.setCellStyle(numberStyle);
					} else {
						R95cell1.setCellValue("");
						R95cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R95cell2 = row.createCell(3);
					if (record1.getR95_deposit_excluding_amount() != null) {
						R95cell2.setCellValue(record1.getR95_deposit_excluding_amount().doubleValue());
						R95cell2.setCellStyle(numberStyle);
					} else {
						R95cell2.setCellValue("");
						R95cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R95cell3 = row.createCell(4);
					if (record1.getR95_deposit_foreign_number() != null) {
						R95cell3.setCellValue(record1.getR95_deposit_foreign_number().doubleValue());
						R95cell3.setCellStyle(numberStyle);
					} else {
						R95cell3.setCellValue("");
						R95cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R95cell4 = row.createCell(5);
					if (record1.getR95_deposit_foreign_amount() != null) {
						R95cell4.setCellValue(record1.getR95_deposit_foreign_amount().doubleValue());
						R95cell4.setCellStyle(numberStyle);
					} else {
						R95cell4.setCellValue("");
						R95cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R95cell5 = row.createCell(8);
					if (record2.getR95_TOTAL_DEPOSIT_EXCEED() != null) {
					R95cell5.setCellValue(record2.getR95_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R95cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R95cell6 = row.createCell(9);
					if (record1.getR95_total_deposit_bank() != null) {
					R95cell6.setCellValue(record1.getR95_total_deposit_bank().doubleValue());
					R95cell6.setCellStyle(numberStyle);
					} else {
					R95cell6.setCellValue("");
					R95cell6.setCellStyle(textStyle);
					}

					// R96
					row = sheet.getRow(95);
					// Column C
					Cell R96cell1 = row.createCell(2);
					if (record1.getR96_deposit_excluding_number() != null) {
						R96cell1.setCellValue(record1.getR96_deposit_excluding_number().doubleValue());
						R96cell1.setCellStyle(numberStyle);
					} else {
						R96cell1.setCellValue("");
						R96cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R96cell2 = row.createCell(3);
					if (record1.getR96_deposit_excluding_amount() != null) {
						R96cell2.setCellValue(record1.getR96_deposit_excluding_amount().doubleValue());
						R96cell2.setCellStyle(numberStyle);
					} else {
						R96cell2.setCellValue("");
						R96cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R96cell3 = row.createCell(4);
					if (record1.getR96_deposit_foreign_number() != null) {
						R96cell3.setCellValue(record1.getR96_deposit_foreign_number().doubleValue());
						R96cell3.setCellStyle(numberStyle);
					} else {
						R96cell3.setCellValue("");
						R96cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R96cell4 = row.createCell(5);
					if (record1.getR96_deposit_foreign_amount() != null) {
						R96cell4.setCellValue(record1.getR96_deposit_foreign_amount().doubleValue());
						R96cell4.setCellStyle(numberStyle);
					} else {
						R96cell4.setCellValue("");
						R96cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R96cell5 = row.createCell(8);
					if (record2.getR96_TOTAL_DEPOSIT_EXCEED() != null) {
					R96cell5.setCellValue(record2.getR96_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R96cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R96cell6 = row.createCell(9);
					if (record1.getR96_total_deposit_bank() != null) {
					R96cell6.setCellValue(record1.getR96_total_deposit_bank().doubleValue());
					R96cell6.setCellStyle(numberStyle);
					} else {
					R96cell6.setCellValue("");
					R96cell6.setCellStyle(textStyle);
					}

					// R97
					row = sheet.getRow(96);
					// Column C
					Cell R97cell1 = row.createCell(2);
					if (record1.getR97_deposit_excluding_number() != null) {
						R97cell1.setCellValue(record1.getR97_deposit_excluding_number().doubleValue());
						R97cell1.setCellStyle(numberStyle);
					} else {
						R97cell1.setCellValue("");
						R97cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R97cell2 = row.createCell(3);
					if (record1.getR97_deposit_excluding_amount() != null) {
						R97cell2.setCellValue(record1.getR97_deposit_excluding_amount().doubleValue());
						R97cell2.setCellStyle(numberStyle);
					} else {
						R97cell2.setCellValue("");
						R97cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R97cell3 = row.createCell(4);
					if (record1.getR97_deposit_foreign_number() != null) {
						R97cell3.setCellValue(record1.getR97_deposit_foreign_number().doubleValue());
						R97cell3.setCellStyle(numberStyle);
					} else {
						R97cell3.setCellValue("");
						R97cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R97cell4 = row.createCell(5);
					if (record1.getR97_deposit_foreign_amount() != null) {
						R97cell4.setCellValue(record1.getR97_deposit_foreign_amount().doubleValue());
						R97cell4.setCellStyle(numberStyle);
					} else {
						R97cell4.setCellValue("");
						R97cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R97cell5 = row.createCell(8);
					if (record2.getR97_TOTAL_DEPOSIT_EXCEED() != null) {
					R97cell5.setCellValue(record2.getR97_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R97cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R97cell6 = row.createCell(9);
					if (record.getR7_total_deposit_bank() != null) {
					R97cell6.setCellValue(record.getR7_total_deposit_bank().doubleValue());
					R97cell6.setCellStyle(numberStyle);
					} else {
					R97cell6.setCellValue("");
					R97cell6.setCellStyle(textStyle);
					}

					// R98
					row = sheet.getRow(97);
					// Column C
					Cell R98cell1 = row.createCell(2);
					if (record1.getR98_deposit_excluding_number() != null) {
						R98cell1.setCellValue(record1.getR98_deposit_excluding_number().doubleValue());
						R98cell1.setCellStyle(numberStyle);
					} else {
						R98cell1.setCellValue("");
						R98cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R98cell2 = row.createCell(3);
					if (record1.getR98_deposit_excluding_amount() != null) {
						R98cell2.setCellValue(record1.getR98_deposit_excluding_amount().doubleValue());
						R98cell2.setCellStyle(numberStyle);
					} else {
						R98cell2.setCellValue("");
						R98cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R98cell3 = row.createCell(4);
					if (record1.getR98_deposit_foreign_number() != null) {
						R98cell3.setCellValue(record1.getR98_deposit_foreign_number().doubleValue());
						R98cell3.setCellStyle(numberStyle);
					} else {
						R98cell3.setCellValue("");
						R98cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R98cell4 = row.createCell(5);
					if (record1.getR98_deposit_foreign_amount() != null) {
						R98cell4.setCellValue(record1.getR98_deposit_foreign_amount().doubleValue());
						R98cell4.setCellStyle(numberStyle);
					} else {
						R98cell4.setCellValue("");
						R98cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R98cell5 = row.createCell(8);
					if (record2.getR98_TOTAL_DEPOSIT_EXCEED() != null) {
					R98cell5.setCellValue(record2.getR98_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R98cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R98cell6 = row.createCell(9);
					if (record1.getR98_total_deposit_bank() != null) {
					R98cell6.setCellValue(record1.getR98_total_deposit_bank().doubleValue());
					R98cell6.setCellStyle(numberStyle);
					} else {
					R98cell6.setCellValue("");
					R98cell6.setCellStyle(textStyle);
					}

					// R99
					row = sheet.getRow(98);
					// Column C
					Cell R99cell1 = row.createCell(2);
					if (record1.getR99_deposit_excluding_number() != null) {
						R99cell1.setCellValue(record1.getR99_deposit_excluding_number().doubleValue());
						R99cell1.setCellStyle(numberStyle);
					} else {
						R99cell1.setCellValue("");
						R99cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R99cell2 = row.createCell(3);
					if (record1.getR99_deposit_excluding_amount() != null) {
						R99cell2.setCellValue(record1.getR99_deposit_excluding_amount().doubleValue());
						R99cell2.setCellStyle(numberStyle);
					} else {
						R99cell2.setCellValue("");
						R99cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R99cell3 = row.createCell(4);
					if (record1.getR99_deposit_foreign_number() != null) {
						R99cell3.setCellValue(record1.getR99_deposit_foreign_number().doubleValue());
						R99cell3.setCellStyle(numberStyle);
					} else {
						R99cell3.setCellValue("");
						R99cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R99cell4 = row.createCell(5);
					if (record1.getR99_deposit_foreign_amount() != null) {
						R99cell4.setCellValue(record1.getR99_deposit_foreign_amount().doubleValue());
						R99cell4.setCellStyle(numberStyle);
					} else {
						R99cell4.setCellValue("");
						R99cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R99cell5 = row.createCell(8);
					if (record2.getR99_TOTAL_DEPOSIT_EXCEED() != null) {
					R99cell5.setCellValue(record2.getR99_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R99cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R99cell6 = row.createCell(9);
					if (record1.getR99_total_deposit_bank() != null) {
					R99cell6.setCellValue(record1.getR99_total_deposit_bank().doubleValue());
					R99cell6.setCellStyle(numberStyle);
					} else {
					R99cell6.setCellValue("");
					R99cell6.setCellStyle(textStyle);
					}

					// R100
					row = sheet.getRow(99);
					// Column C
					Cell R100cell1 = row.createCell(2);
					if (record1.getR100_deposit_excluding_number() != null) {
						R100cell1.setCellValue(record1.getR100_deposit_excluding_number().doubleValue());
						R100cell1.setCellStyle(numberStyle);
					} else {
						R100cell1.setCellValue("");
						R100cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R100cell2 = row.createCell(3);
					if (record1.getR100_deposit_excluding_amount() != null) {
						R100cell2.setCellValue(record1.getR100_deposit_excluding_amount().doubleValue());
						R100cell2.setCellStyle(numberStyle);
					} else {
						R100cell2.setCellValue("");
						R100cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R100cell3 = row.createCell(4);
					if (record1.getR100_deposit_foreign_number() != null) {
						R100cell3.setCellValue(record1.getR100_deposit_foreign_number().doubleValue());
						R100cell3.setCellStyle(numberStyle);
					} else {
						R100cell3.setCellValue("");
						R100cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R100cell4 = row.createCell(5);
					if (record1.getR100_deposit_foreign_amount() != null) {
						R100cell4.setCellValue(record1.getR100_deposit_foreign_amount().doubleValue());
						R100cell4.setCellStyle(numberStyle);
					} else {
						R100cell4.setCellValue("");
						R100cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R100cell5 = row.createCell(8);
					if (record2.getR100_TOTAL_DEPOSIT_EXCEED() != null) {
					R100cell5.setCellValue(record2.getR100_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R100cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R100cell6 = row.createCell(9);
					if (record1.getR100_total_deposit_bank() != null) {
					R100cell6.setCellValue(record1.getR100_total_deposit_bank().doubleValue());
					R100cell6.setCellStyle(numberStyle);
					} else {
					R100cell6.setCellValue("");
					R100cell6.setCellStyle(textStyle);
					}

					// R103
					row = sheet.getRow(102);
					// Column C
					Cell R103cell1 = row.createCell(2);
					if (record1.getR103_deposit_excluding_number() != null) {
						R103cell1.setCellValue(record1.getR103_deposit_excluding_number().doubleValue());
						R103cell1.setCellStyle(numberStyle);
					} else {
						R103cell1.setCellValue("");
						R103cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R103cell2 = row.createCell(3);
					if (record1.getR103_deposit_excluding_amount() != null) {
						R103cell2.setCellValue(record1.getR103_deposit_excluding_amount().doubleValue());
						R103cell2.setCellStyle(numberStyle);
					} else {
						R103cell2.setCellValue("");
						R103cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R103cell3 = row.createCell(4);
					if (record1.getR103_deposit_foreign_number() != null) {
						R103cell3.setCellValue(record1.getR103_deposit_foreign_number().doubleValue());
						R103cell3.setCellStyle(numberStyle);
					} else {
						R103cell3.setCellValue("");
						R103cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R103cell4 = row.createCell(5);
					if (record1.getR103_deposit_foreign_amount() != null) {
						R103cell4.setCellValue(record1.getR103_deposit_foreign_amount().doubleValue());
						R103cell4.setCellStyle(numberStyle);
					} else {
						R103cell4.setCellValue("");
						R103cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R103cell5 = row.createCell(8);
					if (record2.getR103_TOTAL_DEPOSIT_EXCEED() != null) {
					R103cell5.setCellValue(record2.getR103_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R103cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R103cell6 = row.createCell(9);
					if (record1.getR103_total_deposit_bank() != null) {
					R103cell6.setCellValue(record1.getR103_total_deposit_bank().doubleValue());
					R103cell6.setCellStyle(numberStyle);
					} else {
					R103cell6.setCellValue("");
					R103cell6.setCellStyle(textStyle);
					}

					// R104
					row = sheet.getRow(103);
					// Column C
					Cell R104cell1 = row.createCell(2);
					if (record1.getR104_deposit_excluding_number() != null) {
						R104cell1.setCellValue(record1.getR104_deposit_excluding_number().doubleValue());
						R104cell1.setCellStyle(numberStyle);
					} else {
						R104cell1.setCellValue("");
						R104cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R104cell2 = row.createCell(3);
					if (record1.getR104_deposit_excluding_amount() != null) {
						R104cell2.setCellValue(record1.getR104_deposit_excluding_amount().doubleValue());
						R104cell2.setCellStyle(numberStyle);
					} else {
						R104cell2.setCellValue("");
						R104cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R104cell3 = row.createCell(4);
					if (record1.getR104_deposit_foreign_number() != null) {
						R104cell3.setCellValue(record1.getR104_deposit_foreign_number().doubleValue());
						R104cell3.setCellStyle(numberStyle);
					} else {
						R104cell3.setCellValue("");
						R104cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R104cell4 = row.createCell(5);
					if (record1.getR104_deposit_foreign_amount() != null) {
						R104cell4.setCellValue(record1.getR104_deposit_foreign_amount().doubleValue());
						R104cell4.setCellStyle(numberStyle);
					} else {
						R104cell4.setCellValue("");
						R104cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R104cell5 = row.createCell(8);
					if (record2.getR104_TOTAL_DEPOSIT_EXCEED() != null) {
					R104cell5.setCellValue(record2.getR104_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R104cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R104cell6 = row.createCell(9);
					if (record1.getR104_total_deposit_bank() != null) {
					R104cell6.setCellValue(record1.getR104_total_deposit_bank().doubleValue());
					R104cell6.setCellStyle(numberStyle);
					} else {
					R104cell6.setCellValue("");
					R104cell6.setCellStyle(textStyle);
					}


					// R105
					row = sheet.getRow(104);
					// Column C
					Cell R105cell1 = row.createCell(2);
					if (record1.getR105_deposit_excluding_number() != null) {
						R105cell1.setCellValue(record1.getR105_deposit_excluding_number().doubleValue());
						R105cell1.setCellStyle(numberStyle);
					} else {
						R105cell1.setCellValue("");
						R105cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R105cell2 = row.createCell(3);
					if (record1.getR105_deposit_excluding_amount() != null) {
						R105cell2.setCellValue(record1.getR105_deposit_excluding_amount().doubleValue());
						R105cell2.setCellStyle(numberStyle);
					} else {
						R105cell2.setCellValue("");
						R105cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R105cell3 = row.createCell(4);
					if (record1.getR105_deposit_foreign_number() != null) {
						R105cell3.setCellValue(record1.getR105_deposit_foreign_number().doubleValue());
						R105cell3.setCellStyle(numberStyle);
					} else {
						R105cell3.setCellValue("");
						R105cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R105cell4 = row.createCell(5);
					if (record1.getR105_deposit_foreign_amount() != null) {
						R105cell4.setCellValue(record1.getR105_deposit_foreign_amount().doubleValue());
						R105cell4.setCellStyle(numberStyle);
					} else {
						R105cell4.setCellValue("");
						R105cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R105cell5 = row.createCell(8);
					if (record2.getR105_TOTAL_DEPOSIT_EXCEED() != null) {
					R105cell5.setCellValue(record2.getR105_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R105cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R105cell6 = row.createCell(9);
					if (record1.getR105_total_deposit_bank() != null) {
					R105cell6.setCellValue(record1.getR105_total_deposit_bank().doubleValue());
					R105cell6.setCellStyle(numberStyle);
					} else {
					R105cell6.setCellValue("");
					R105cell6.setCellStyle(textStyle);
					}

					// R106
					row = sheet.getRow(105);
					// Column C
					Cell R106cell1 = row.createCell(2);
					if (record1.getR106_deposit_excluding_number() != null) {
						R106cell1.setCellValue(record1.getR106_deposit_excluding_number().doubleValue());
						R106cell1.setCellStyle(numberStyle);
					} else {
						R106cell1.setCellValue("");
						R106cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R106cell2 = row.createCell(3);
					if (record1.getR106_deposit_excluding_amount() != null) {
						R106cell2.setCellValue(record1.getR106_deposit_excluding_amount().doubleValue());
						R106cell2.setCellStyle(numberStyle);
					} else {
						R106cell2.setCellValue("");
						R106cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R106cell3 = row.createCell(4);
					if (record1.getR106_deposit_foreign_number() != null) {
						R106cell3.setCellValue(record1.getR106_deposit_foreign_number().doubleValue());
						R106cell3.setCellStyle(numberStyle);
					} else {
						R106cell3.setCellValue("");
						R106cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R106cell4 = row.createCell(5);
					if (record1.getR106_deposit_foreign_amount() != null) {
						R106cell4.setCellValue(record1.getR106_deposit_foreign_amount().doubleValue());
						R106cell4.setCellStyle(numberStyle);
					} else {
						R106cell4.setCellValue("");
						R106cell4.setCellStyle(textStyle);
					}
					
					// Column I
					Cell R106cell5 = row.createCell(8);
					if (record2.getR106_TOTAL_DEPOSIT_EXCEED() != null) {
					R106cell5.setCellValue(record2.getR106_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R106cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R106cell6 = row.createCell(9);
					if (record1.getR106_total_deposit_bank() != null) {
					R106cell6.setCellValue(record1.getR106_total_deposit_bank().doubleValue());
					R106cell6.setCellStyle(numberStyle);
					} else {
					R106cell6.setCellValue("");
					R106cell6.setCellStyle(textStyle);
					}


					// R107
					row = sheet.getRow(106);
					// Column C
					Cell R107cell1 = row.createCell(2);
					if (record1.getR107_deposit_excluding_number() != null) {
						R107cell1.setCellValue(record1.getR107_deposit_excluding_number().doubleValue());
						R107cell1.setCellStyle(numberStyle);
					} else {
						R107cell1.setCellValue("");
						R107cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R107cell2 = row.createCell(3);
					if (record1.getR107_deposit_excluding_amount() != null) {
						R107cell2.setCellValue(record1.getR107_deposit_excluding_amount().doubleValue());
						R107cell2.setCellStyle(numberStyle);
					} else {
						R107cell2.setCellValue("");
						R107cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R107cell3 = row.createCell(4);
					if (record1.getR107_deposit_foreign_number() != null) {
						R107cell3.setCellValue(record1.getR107_deposit_foreign_number().doubleValue());
						R107cell3.setCellStyle(numberStyle);
					} else {
						R107cell3.setCellValue("");
						R107cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R107cell4 = row.createCell(5);
					if (record1.getR107_deposit_foreign_amount() != null) {
						R107cell4.setCellValue(record1.getR107_deposit_foreign_amount().doubleValue());
						R107cell4.setCellStyle(numberStyle);
					} else {
						R107cell4.setCellValue("");
						R107cell4.setCellStyle(textStyle);
					}

					// Column I
					Cell R107cell5 = row.createCell(8);
					if (record2.getR107_TOTAL_DEPOSIT_EXCEED() != null) {
					R107cell5.setCellValue(record2.getR107_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R107cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R107cell6 = row.createCell(9);
					if (record1.getR107_total_deposit_bank() != null) {
					R107cell6.setCellValue(record1.getR107_total_deposit_bank().doubleValue());
					R107cell6.setCellStyle(numberStyle);
					} else {
					R107cell6.setCellValue("");
					R107cell6.setCellStyle(textStyle);
					}

					// R108
					row = sheet.getRow(107);
					// Column C
					Cell R108cell1 = row.createCell(2);
					if (record1.getR108_deposit_excluding_number() != null) {
						R108cell1.setCellValue(record1.getR108_deposit_excluding_number().doubleValue());
						R108cell1.setCellStyle(numberStyle);
					} else {
						R108cell1.setCellValue("");
						R108cell1.setCellStyle(textStyle);
					}

					// Column D
					Cell R108cell2 = row.createCell(3);
					if (record1.getR108_deposit_excluding_amount() != null) {
						R108cell2.setCellValue(record1.getR108_deposit_excluding_amount().doubleValue());
						R108cell2.setCellStyle(numberStyle);
					} else {
						R108cell2.setCellValue("");
						R108cell2.setCellStyle(textStyle);
					}

					// Column E
					Cell R108cell3 = row.createCell(4);
					if (record1.getR108_deposit_foreign_number() != null) {
						R108cell3.setCellValue(record1.getR108_deposit_foreign_number().doubleValue());
						R108cell3.setCellStyle(numberStyle);
					} else {
						R108cell3.setCellValue("");
						R108cell3.setCellStyle(textStyle);
					}

					// Column F
					Cell R108cell4 = row.createCell(5);
					if (record1.getR108_deposit_foreign_amount() != null) {
						R108cell4.setCellValue(record1.getR108_deposit_foreign_amount().doubleValue());
						R108cell4.setCellStyle(numberStyle);
					} else {
						R108cell4.setCellValue("");
						R108cell4.setCellStyle(textStyle);
					}
					// Column I
					Cell R108cell5 = row.createCell(8);
					if (record2.getR108_TOTAL_DEPOSIT_EXCEED() != null) {
					R108cell5.setCellValue(record2.getR108_TOTAL_DEPOSIT_EXCEED().doubleValue());
					} else {
					R108cell5.setCellValue("");
					
					}
										
					// Column J
					Cell R108cell6 = row.createCell(9);
					if (record1.getR108_total_deposit_bank() != null) {
					R108cell6.setCellValue(record1.getR108_total_deposit_bank().doubleValue());
					R108cell6.setCellStyle(numberStyle);
					} else {
					R108cell6.setCellValue("");
					R108cell6.setCellStyle(textStyle);
					}
					}

					
					workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
				} }else {

				}
				// Write the final workbook content to the in-memory stream.
				workbook.write(out);
				logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
				return out.toByteArray();
			}
		}

	public byte[] getMDISB1DetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for MDISB1 Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("MDISB1Details");

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
			List<MDISB1_Detail_Entity> reportData = BRRS_MDISB1_Detail_Repo.getdatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (MDISB1_Detail_Entity item : reportData) {
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
				logger.info("No data found for MDISB1 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating MDISB1 Excel", e);
			return new byte[0];
		}
	}

	public List<Object> getMDISB1Archival() {
		List<Object> MDISB1Archivallist = new ArrayList<>();
		// List<Object> MDISB1Archivallist1 = new ArrayList<>();
		try {
			MDISB1Archivallist = brrs_MDISB1_Archival_Summary_Repo1.getMDISB1archival();
			System.out.println("countser" + MDISB1Archivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching MDISB1 Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return MDISB1Archivallist;
	}

	public byte[] getMDISB1Archival(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<MDISB1_Archival_Summary_Entity1> dataList = brrs_MDISB1_Archival_Summary_Repo1
				.getdatabydateListarchival(dateformat.parse(todate), version);

		List<MDISB1_Archival_Summary_Entity2> dataList1 = brrs_MDISB1_Archival_Summary_Repo2
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for MDISB1 report. Returning empty result.");
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
					MDISB1_Archival_Summary_Entity1 record = dataList.get(i);
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
			logger.info("Generating Excel for BRRS_MDISB1 ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("MDISB1Details");

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
			List<MDISB1_Archival_Detail_Entity> reportData = brrs_MDISB1_Archival_Detail_Repo
					.getdatabydateList(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (MDISB1_Archival_Detail_Entity item : reportData) {
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
				logger.info("No data found for MDISB1 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating MDISB1 Excel", e);
			return new byte[0];
		}
	}

	@Autowired
	private BRRS_MDISB1_Detail_Repo MDISB1_Detail_Repo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/MDISB1");

		System.out.println("Came to view method");

		if (acctNo != null) {
			MDISB1_Detail_Entity Entity = MDISB1_Detail_Repo.findByAcctnumber(acctNo);
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

			MDISB1_Detail_Entity existing = MDISB1_Detail_Repo.findByAcctnumber(acctNo);
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
				MDISB1_Detail_Repo.save(existing);
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_MDISB1_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_MDISB1_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating MDISB1 record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	public byte[] getExcelMDISB1ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<MDISB1_Archival_Summary_Entity1> dataList = brrs_MDISB1_Archival_Summary_Repo1
				.getdatabydateListarchival(dateformat.parse(todate), version);
		List<MDISB1_Archival_Summary_Entity2> dataList1 = brrs_MDISB1_Archival_Summary_Repo2
				.getdatabydateListarchival(dateformat.parse(todate), version);
		List<MDISB1_Archival_Summary_Manual> dataList2 = brrs_MDISB1_Archival_Summary_Repo3
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_PLL report. Returning empty result.");
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
					MDISB1_Archival_Summary_Entity1 record = dataList.get(i);
					MDISB1_Archival_Summary_Entity2 record1 = dataList1.get(i);
					MDISB1_Archival_Summary_Manual record2 = dataList2.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					
							// R7
							// Column C
							Cell cell1 = row.createCell(2);
							if (record.getR7_deposit_excluding_number() != null) {
								cell1.setCellValue(record.getR7_deposit_excluding_number().doubleValue());
								cell1.setCellStyle(numberStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell cell2 = row.createCell(3);
							if (record.getR7_deposit_excluding_amount() != null) {
								cell2.setCellValue(record.getR7_deposit_excluding_amount().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell cell3 = row.createCell(4);
							if (record.getR7_deposit_foreign_number() != null) {
								cell3.setCellValue(record.getR7_deposit_foreign_number().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column F
							Cell cell4 = row.createCell(5);
							if (record.getR7_deposit_foreign_amount() != null) {
								cell4.setCellValue(record.getR7_deposit_foreign_amount().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell cell5 = row.createCell(8);
							if (record2.getR7_TOTAL_DEPOSIT_EXCEED() != null) {
								cell5.setCellValue(record2.getR7_TOTAL_DEPOSIT_EXCEED().doubleValue());
								} else {
								cell5.setCellValue("");
								
							}
							
							// Column J
							Cell cell6 = row.createCell(9);
							if (record.getR7_total_deposit_bank() != null) {
								cell6.setCellValue(record.getR7_total_deposit_bank().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// R8
							row = sheet.getRow(7);
							// Column C
							Cell R8cell1 = row.createCell(2);
							if (record.getR8_deposit_excluding_number() != null) {
								R8cell1.setCellValue(record.getR8_deposit_excluding_number().doubleValue());
								R8cell1.setCellStyle(numberStyle);
							} else {
								R8cell1.setCellValue("");
								R8cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R8cell2 = row.createCell(3);
							if (record.getR8_deposit_excluding_amount() != null) {
								R8cell2.setCellValue(record.getR8_deposit_excluding_amount().doubleValue());
								R8cell2.setCellStyle(numberStyle);
							} else {
								R8cell2.setCellValue("");
								R8cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R8cell3 = row.createCell(4);
							if (record.getR8_deposit_foreign_number() != null) {
								R8cell3.setCellValue(record.getR8_deposit_foreign_number().doubleValue());
								R8cell3.setCellStyle(numberStyle);
							} else {
								R8cell3.setCellValue("");
								R8cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R8cell4 = row.createCell(5);
							if (record.getR8_deposit_foreign_amount() != null) {
								R8cell4.setCellValue(record.getR8_deposit_foreign_amount().doubleValue());
								R8cell4.setCellStyle(numberStyle);
							} else {
								R8cell4.setCellValue("");
								R8cell4.setCellStyle(textStyle);
							}
							// Column I
							Cell R8cell5 = row.createCell(8);
							if (record2.getR8_TOTAL_DEPOSIT_EXCEED() != null) {
							R8cell5.setCellValue(record2.getR8_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R8cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R8cell6 = row.createCell(9);
							if (record.getR8_total_deposit_bank() != null) {
							R8cell6.setCellValue(record.getR8_total_deposit_bank().doubleValue());
							R8cell6.setCellStyle(numberStyle);
							} else {
							R8cell6.setCellValue("");
							R8cell6.setCellStyle(textStyle);
							}

							// R9
							row = sheet.getRow(8);
							// Column C
							Cell R9cell1 = row.createCell(2);
							if (record.getR9_deposit_excluding_number() != null) {
								R9cell1.setCellValue(record.getR9_deposit_excluding_number().doubleValue());
								R9cell1.setCellStyle(numberStyle);
							} else {
								R9cell1.setCellValue("");
								R9cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R9cell2 = row.createCell(3);
							if (record.getR9_deposit_excluding_amount() != null) {
								R9cell2.setCellValue(record.getR9_deposit_excluding_amount().doubleValue());
								R9cell2.setCellStyle(numberStyle);
							} else {
								R9cell2.setCellValue("");
								R9cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R9cell3 = row.createCell(4);
							if (record.getR9_deposit_foreign_number() != null) {
								R9cell3.setCellValue(record.getR9_deposit_foreign_number().doubleValue());
								R9cell3.setCellStyle(numberStyle);
							} else {
								R9cell3.setCellValue("");
								R9cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R9cell4 = row.createCell(5);
							if (record.getR9_deposit_foreign_amount() != null) {
								R9cell4.setCellValue(record.getR9_deposit_foreign_amount().doubleValue());
								R9cell4.setCellStyle(numberStyle);
							} else {
								R9cell4.setCellValue("");
								R9cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R9cell5 = row.createCell(8);
							if (record2.getR9_TOTAL_DEPOSIT_EXCEED() != null) {
							R9cell5.setCellValue(record2.getR9_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R9cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R9cell6 = row.createCell(9);
							if (record.getR9_total_deposit_bank() != null) {
							R9cell6.setCellValue(record.getR9_total_deposit_bank().doubleValue());
							R9cell6.setCellStyle(numberStyle);
							} else {
							R9cell6.setCellValue("");
							R9cell6.setCellStyle(textStyle);
							}

							// R10
							row = sheet.getRow(9);
							// Column C
							Cell R10cell1 = row.createCell(2);
							if (record.getR10_deposit_excluding_number() != null) {
								R10cell1.setCellValue(record.getR10_deposit_excluding_number().doubleValue());
								R10cell1.setCellStyle(numberStyle);
							} else {
								R10cell1.setCellValue("");
								R10cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R10cell2 = row.createCell(3);
							if (record.getR10_deposit_excluding_amount() != null) {
								R10cell2.setCellValue(record.getR10_deposit_excluding_amount().doubleValue());
								R10cell2.setCellStyle(numberStyle);
							} else {
								R10cell2.setCellValue("");
								R10cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R10cell3 = row.createCell(4);
							if (record.getR10_deposit_foreign_number() != null) {
								R10cell3.setCellValue(record.getR10_deposit_foreign_number().doubleValue());
								R10cell3.setCellStyle(numberStyle);
							} else {
								R10cell3.setCellValue("");
								R10cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R10cell4 = row.createCell(5);
							if (record.getR10_deposit_foreign_amount() != null) {
								R10cell4.setCellValue(record.getR10_deposit_foreign_amount().doubleValue());
								R10cell4.setCellStyle(numberStyle);
							} else {
								R10cell4.setCellValue("");
								R10cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R10cell5 = row.createCell(8);
							if (record2.getR10_TOTAL_DEPOSIT_EXCEED() != null) {
							R10cell5.setCellValue(record2.getR10_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R10cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R10cell6 = row.createCell(9);
							if (record.getR10_total_deposit_bank() != null) {
							R10cell6.setCellValue(record.getR10_total_deposit_bank().doubleValue());
							R10cell6.setCellStyle(numberStyle);
							} else {
							R10cell6.setCellValue("");
							R10cell6.setCellStyle(textStyle);
							}

							
							// R11
							row = sheet.getRow(10);
							// Column C
							Cell R11cell1 = row.createCell(2);
							if (record.getR11_deposit_excluding_number() != null) {
								R11cell1.setCellValue(record.getR11_deposit_excluding_number().doubleValue());
								R11cell1.setCellStyle(numberStyle);
							} else {
								R11cell1.setCellValue("");
								R11cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R11cell2 = row.createCell(3);
							if (record.getR11_deposit_excluding_amount() != null) {
								R11cell2.setCellValue(record.getR11_deposit_excluding_amount().doubleValue());
								R11cell2.setCellStyle(numberStyle);
							} else {
								R11cell2.setCellValue("");
								R11cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R11cell3 = row.createCell(4);
							if (record.getR11_deposit_foreign_number() != null) {
								R11cell3.setCellValue(record.getR11_deposit_foreign_number().doubleValue());
								R11cell3.setCellStyle(numberStyle);
							} else {
								R11cell3.setCellValue("");
								R11cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R11cell4 = row.createCell(5);
							if (record.getR11_deposit_foreign_amount() != null) {
								R11cell4.setCellValue(record.getR11_deposit_foreign_amount().doubleValue());
								R11cell4.setCellStyle(numberStyle);
							} else {
								R11cell4.setCellValue("");
								R11cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R11cell5 = row.createCell(8);
							if (record2.getR11_TOTAL_DEPOSIT_EXCEED() != null) {
							R11cell5.setCellValue(record2.getR11_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R11cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R11cell6 = row.createCell(9);
							if (record.getR11_total_deposit_bank() != null) {
							R11cell6.setCellValue(record.getR11_total_deposit_bank().doubleValue());
							R11cell6.setCellStyle(numberStyle);
							} else {
							R11cell6.setCellValue("");
							R11cell6.setCellStyle(textStyle);
							}


							// R12
							row = sheet.getRow(11);
							// Column C
							Cell R12cell1 = row.createCell(2);
							if (record.getR12_deposit_excluding_number() != null) {
								R12cell1.setCellValue(record.getR12_deposit_excluding_number().doubleValue());
								R12cell1.setCellStyle(numberStyle);
							} else {
								R12cell1.setCellValue("");
								R12cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R12cell2 = row.createCell(3);
							if (record.getR12_deposit_excluding_amount() != null) {
								R12cell2.setCellValue(record.getR12_deposit_excluding_amount().doubleValue());
								R12cell2.setCellStyle(numberStyle);
							} else {
								R12cell2.setCellValue("");
								R12cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R12cell3 = row.createCell(4);
							if (record.getR12_deposit_foreign_number() != null) {
								R12cell3.setCellValue(record.getR12_deposit_foreign_number().doubleValue());
								R12cell3.setCellStyle(numberStyle);
							} else {
								R12cell3.setCellValue("");
								R12cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R12cell4 = row.createCell(5);
							if (record.getR12_deposit_foreign_amount() != null) {
								R12cell4.setCellValue(record.getR12_deposit_foreign_amount().doubleValue());
								R12cell4.setCellStyle(numberStyle);
							} else {
								R12cell4.setCellValue("");
								R12cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R12cell5 = row.createCell(8);
							if (record2.getR12_TOTAL_DEPOSIT_EXCEED() != null) {
							R12cell5.setCellValue(record2.getR12_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R12cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R12cell6 = row.createCell(9);
							if (record.getR12_total_deposit_bank() != null) {
							R12cell6.setCellValue(record.getR12_total_deposit_bank().doubleValue());
							R12cell6.setCellStyle(numberStyle);
							} else {
							R12cell6.setCellValue("");
							R12cell6.setCellStyle(textStyle);
							}

							// R15
							row = sheet.getRow(14);
							// Column C
							Cell R15cell1 = row.createCell(2);
							if (record.getR15_deposit_excluding_number() != null) {
								R15cell1.setCellValue(record.getR15_deposit_excluding_number().doubleValue());
								R15cell1.setCellStyle(numberStyle);
							} else {
								R15cell1.setCellValue("");
								R15cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R15cell2 = row.createCell(3);
							if (record.getR15_deposit_excluding_amount() != null) {
								R15cell2.setCellValue(record.getR15_deposit_excluding_amount().doubleValue());
								R15cell2.setCellStyle(numberStyle);
							} else {
								R15cell2.setCellValue("");
								R15cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R15cell3 = row.createCell(4);
							if (record.getR15_deposit_foreign_number() != null) {
								R15cell3.setCellValue(record.getR15_deposit_foreign_number().doubleValue());
								R15cell3.setCellStyle(numberStyle);
							} else {
								R15cell3.setCellValue("");
								R15cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R15cell4 = row.createCell(5);
							if (record.getR15_deposit_foreign_amount() != null) {
								R15cell4.setCellValue(record.getR15_deposit_foreign_amount().doubleValue());
								R15cell4.setCellStyle(numberStyle);
							} else {
								R15cell4.setCellValue("");
								R15cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R15cell5 = row.createCell(8);
							if (record2.getR15_TOTAL_DEPOSIT_EXCEED() != null) {
							R15cell5.setCellValue(record2.getR15_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R15cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R15cell6 = row.createCell(9);
							if (record.getR15_total_deposit_bank() != null) {
							R15cell6.setCellValue(record.getR15_total_deposit_bank().doubleValue());
							R15cell6.setCellStyle(numberStyle);
							} else {
							R15cell6.setCellValue("");
							R15cell6.setCellStyle(textStyle);
							}

							// R16
							row = sheet.getRow(15);
							// Column C
							Cell R16cell1 = row.createCell(2);
							if (record.getR16_deposit_excluding_number() != null) {
								R16cell1.setCellValue(record.getR16_deposit_excluding_number().doubleValue());
								R16cell1.setCellStyle(numberStyle);
							} else {
								R16cell1.setCellValue("");
								R16cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R16cell2 = row.createCell(3);
							if (record.getR16_deposit_excluding_amount() != null) {
								R16cell2.setCellValue(record.getR16_deposit_excluding_amount().doubleValue());
								R16cell2.setCellStyle(numberStyle);
							} else {
								R16cell2.setCellValue("");
								R16cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R16cell3 = row.createCell(4);
							if (record.getR16_deposit_foreign_number() != null) {
								R16cell3.setCellValue(record.getR16_deposit_foreign_number().doubleValue());
								R16cell3.setCellStyle(numberStyle);
							} else {
								R16cell3.setCellValue("");
								R16cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R16cell4 = row.createCell(5);
							if (record.getR16_deposit_foreign_amount() != null) {
								R16cell4.setCellValue(record.getR16_deposit_foreign_amount().doubleValue());
								R16cell4.setCellStyle(numberStyle);
							} else {
								R16cell4.setCellValue("");
								R16cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R16cell5 = row.createCell(8);
							if (record2.getR16_TOTAL_DEPOSIT_EXCEED() != null) {
							R16cell5.setCellValue(record2.getR16_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R16cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R16cell6 = row.createCell(9);
							if (record.getR16_total_deposit_bank() != null) {
							R16cell6.setCellValue(record.getR16_total_deposit_bank().doubleValue());
							R16cell6.setCellStyle(numberStyle);
							} else {
							R16cell6.setCellValue("");
							R16cell6.setCellStyle(textStyle);
							}


							// R17
							row = sheet.getRow(16);
							// Column C
							Cell R17cell1 = row.createCell(2);
							if (record.getR17_deposit_excluding_number() != null) {
								R17cell1.setCellValue(record.getR17_deposit_excluding_number().doubleValue());
								R17cell1.setCellStyle(numberStyle);
							} else {
								R17cell1.setCellValue("");
								R17cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R17cell2 = row.createCell(3);
							if (record.getR17_deposit_excluding_amount() != null) {
								R17cell2.setCellValue(record.getR17_deposit_excluding_amount().doubleValue());
								R17cell2.setCellStyle(numberStyle);
							} else {
								R17cell2.setCellValue("");
								R17cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R17cell3 = row.createCell(4);
							if (record.getR17_deposit_foreign_number() != null) {
								R17cell3.setCellValue(record.getR17_deposit_foreign_number().doubleValue());
								R17cell3.setCellStyle(numberStyle);
							} else {
								R17cell3.setCellValue("");
								R17cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R17cell4 = row.createCell(5);
							if (record.getR17_deposit_foreign_amount() != null) {
								R17cell4.setCellValue(record.getR17_deposit_foreign_amount().doubleValue());
								R17cell4.setCellStyle(numberStyle);
							} else {
								R17cell4.setCellValue("");
								R17cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R17cell5 = row.createCell(8);
							if (record2.getR17_TOTAL_DEPOSIT_EXCEED() != null) {
							R17cell5.setCellValue(record2.getR17_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R17cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R17cell6 = row.createCell(9);
							if (record.getR17_total_deposit_bank() != null) {
							R17cell6.setCellValue(record.getR17_total_deposit_bank().doubleValue());
							R17cell6.setCellStyle(numberStyle);
							} else {
							R17cell6.setCellValue("");
							R17cell6.setCellStyle(textStyle);
							}


							// R18
							row = sheet.getRow(17);
							// Column C
							Cell R18cell1 = row.createCell(2);
							if (record.getR18_deposit_excluding_number() != null) {
								R18cell1.setCellValue(record.getR18_deposit_excluding_number().doubleValue());
								R18cell1.setCellStyle(numberStyle);
							} else {
								R18cell1.setCellValue("");
								R18cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R18cell2 = row.createCell(3);
							if (record.getR18_deposit_excluding_amount() != null) {
								R18cell2.setCellValue(record.getR18_deposit_excluding_amount().doubleValue());
								R18cell2.setCellStyle(numberStyle);
							} else {
								R18cell2.setCellValue("");
								R18cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R18cell3 = row.createCell(4);
							if (record.getR18_deposit_foreign_number() != null) {
								R18cell3.setCellValue(record.getR18_deposit_foreign_number().doubleValue());
								R18cell3.setCellStyle(numberStyle);
							} else {
								R18cell3.setCellValue("");
								R18cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R18cell4 = row.createCell(5);
							if (record.getR18_deposit_foreign_amount() != null) {
								R18cell4.setCellValue(record.getR18_deposit_foreign_amount().doubleValue());
								R18cell4.setCellStyle(numberStyle);
							} else {
								R18cell4.setCellValue("");
								R18cell4.setCellStyle(textStyle);
							}
							// Column I
							Cell R18cell5 = row.createCell(8);
							if (record2.getR18_TOTAL_DEPOSIT_EXCEED() != null) {
							R18cell5.setCellValue(record2.getR18_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R18cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R18cell6 = row.createCell(9);
							if (record.getR18_total_deposit_bank() != null) {
							R18cell6.setCellValue(record.getR18_total_deposit_bank().doubleValue());
							R18cell6.setCellStyle(numberStyle);
							} else {
							R18cell6.setCellValue("");
							R18cell6.setCellStyle(textStyle);
							}


							// R19
							row = sheet.getRow(18);
							// Column C
							Cell R19cell1 = row.createCell(2);
							if (record.getR19_deposit_excluding_number() != null) {
								R19cell1.setCellValue(record.getR19_deposit_excluding_number().doubleValue());
								R19cell1.setCellStyle(numberStyle);
							} else {
								R19cell1.setCellValue("");
								R19cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R19cell2 = row.createCell(3);
							if (record.getR19_deposit_excluding_amount() != null) {
								R19cell2.setCellValue(record.getR19_deposit_excluding_amount().doubleValue());
								R19cell2.setCellStyle(numberStyle);
							} else {
								R19cell2.setCellValue("");
								R19cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R19cell3 = row.createCell(4);
							if (record.getR19_deposit_foreign_number() != null) {
								R19cell3.setCellValue(record.getR19_deposit_foreign_number().doubleValue());
								R19cell3.setCellStyle(numberStyle);
							} else {
								R19cell3.setCellValue("");
								R19cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R19cell4 = row.createCell(5);
							if (record.getR19_deposit_foreign_amount() != null) {
								R19cell4.setCellValue(record.getR19_deposit_foreign_amount().doubleValue());
								R19cell4.setCellStyle(numberStyle);
							} else {
								R19cell4.setCellValue("");
								R19cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R19cell5 = row.createCell(8);
							if (record2.getR19_TOTAL_DEPOSIT_EXCEED() != null) {
							R19cell5.setCellValue(record2.getR19_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R19cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R19cell6 = row.createCell(9);
							if (record.getR19_total_deposit_bank() != null) {
							R19cell6.setCellValue(record.getR19_total_deposit_bank().doubleValue());
							R19cell6.setCellStyle(numberStyle);
							} else {
							R19cell6.setCellValue("");
							R19cell6.setCellStyle(textStyle);
							}


							// R20
							row = sheet.getRow(19);
							// Column C
							Cell R20cell1 = row.createCell(2);
							if (record.getR20_deposit_excluding_number() != null) {
								R20cell1.setCellValue(record.getR20_deposit_excluding_number().doubleValue());
								R20cell1.setCellStyle(numberStyle);
							} else {
								R20cell1.setCellValue("");
								R20cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R20cell2 = row.createCell(3);
							if (record.getR20_deposit_excluding_amount() != null) {
								R20cell2.setCellValue(record.getR20_deposit_excluding_amount().doubleValue());
								R20cell2.setCellStyle(numberStyle);
							} else {
								R20cell2.setCellValue("");
								R20cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R20cell3 = row.createCell(4);
							if (record.getR20_deposit_foreign_number() != null) {
								R20cell3.setCellValue(record.getR20_deposit_foreign_number().doubleValue());
								R20cell3.setCellStyle(numberStyle);
							} else {
								R20cell3.setCellValue("");
								R20cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R20cell4 = row.createCell(5);
							if (record.getR20_deposit_foreign_amount() != null) {
								R20cell4.setCellValue(record.getR20_deposit_foreign_amount().doubleValue());
								R20cell4.setCellStyle(numberStyle);
							} else {
								R20cell4.setCellValue("");
								R20cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R20cell5 = row.createCell(8);
							if (record2.getR20_TOTAL_DEPOSIT_EXCEED() != null) {
							R20cell5.setCellValue(record2.getR20_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R20cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R20cell6 = row.createCell(9);
							if (record.getR20_total_deposit_bank() != null) {
							R20cell6.setCellValue(record.getR20_total_deposit_bank().doubleValue());
							R20cell6.setCellStyle(numberStyle);
							} else {
							R20cell6.setCellValue("");
							R20cell6.setCellStyle(textStyle);
							}

							// R23
							row = sheet.getRow(22);
							// Column C
							Cell R23cell1 = row.createCell(2);
							if (record.getR23_deposit_excluding_number() != null) {
								R23cell1.setCellValue(record.getR23_deposit_excluding_number().doubleValue());
								R23cell1.setCellStyle(numberStyle);
							} else {
								R23cell1.setCellValue("");
								R23cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R23cell2 = row.createCell(3);
							if (record.getR23_deposit_excluding_amount() != null) {
								R23cell2.setCellValue(record.getR23_deposit_excluding_amount().doubleValue());
								R23cell2.setCellStyle(numberStyle);
							} else {
								R23cell2.setCellValue("");
								R23cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R23cell3 = row.createCell(4);
							if (record.getR23_deposit_foreign_number() != null) {
								R23cell3.setCellValue(record.getR23_deposit_foreign_number().doubleValue());
								R23cell3.setCellStyle(numberStyle);
							} else {
								R23cell3.setCellValue("");
								R23cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R23cell4 = row.createCell(5);
							if (record.getR23_deposit_foreign_amount() != null) {
								R23cell4.setCellValue(record.getR23_deposit_foreign_amount().doubleValue());
								R23cell4.setCellStyle(numberStyle);
							} else {
								R23cell4.setCellValue("");
								R23cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R23cell5 = row.createCell(8);
							if (record2.getR23_TOTAL_DEPOSIT_EXCEED() != null) {
							R23cell5.setCellValue(record2.getR23_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R23cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R23cell6 = row.createCell(9);
							if (record.getR23_total_deposit_bank() != null) {
							R23cell6.setCellValue(record.getR23_total_deposit_bank().doubleValue());
							R23cell6.setCellStyle(numberStyle);
							} else {
							R23cell6.setCellValue("");
							R23cell6.setCellStyle(textStyle);
							}


							// R24
							row = sheet.getRow(23);
							// Column C
							Cell R24cell1 = row.createCell(2);
							if (record.getR24_deposit_excluding_number() != null) {
								R24cell1.setCellValue(record.getR24_deposit_excluding_number().doubleValue());
								R24cell1.setCellStyle(numberStyle);
							} else {
								R24cell1.setCellValue("");
								R24cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R24cell2 = row.createCell(3);
							if (record.getR24_deposit_excluding_amount() != null) {
								R24cell2.setCellValue(record.getR24_deposit_excluding_amount().doubleValue());
								R24cell2.setCellStyle(numberStyle);
							} else {
								R24cell2.setCellValue("");
								R24cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R24cell3 = row.createCell(4);
							if (record.getR24_deposit_foreign_number() != null) {
								R24cell3.setCellValue(record.getR24_deposit_foreign_number().doubleValue());
								R24cell3.setCellStyle(numberStyle);
							} else {
								R24cell3.setCellValue("");
								R24cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R24cell4 = row.createCell(5);
							if (record.getR24_deposit_foreign_amount() != null) {
								R24cell4.setCellValue(record.getR24_deposit_foreign_amount().doubleValue());
								R24cell4.setCellStyle(numberStyle);
							} else {
								R24cell4.setCellValue("");
								R24cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R24cell5 = row.createCell(8);
							if (record2.getR24_TOTAL_DEPOSIT_EXCEED() != null) {
							R24cell5.setCellValue(record2.getR24_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R24cell5.setCellValue("");
							}
												
							// Column J
							Cell R24cell6 = row.createCell(9);
							if (record.getR24_total_deposit_bank() != null) {
							R24cell6.setCellValue(record.getR24_total_deposit_bank().doubleValue());
							R24cell6.setCellStyle(numberStyle);
							} else {
							R24cell6.setCellValue("");
							R24cell6.setCellStyle(textStyle);
							}

							// R25
							row = sheet.getRow(24);
							// Column C
							Cell R25cell1 = row.createCell(2);
							if (record.getR25_deposit_excluding_number() != null) {
								R25cell1.setCellValue(record.getR25_deposit_excluding_number().doubleValue());
								R25cell1.setCellStyle(numberStyle);
							} else {
								R25cell1.setCellValue("");
								R25cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R25cell2 = row.createCell(3);
							if (record.getR25_deposit_excluding_amount() != null) {
								R25cell2.setCellValue(record.getR25_deposit_excluding_amount().doubleValue());
								R25cell2.setCellStyle(numberStyle);
							} else {
								R25cell2.setCellValue("");
								R25cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R25cell3 = row.createCell(4);
							if (record.getR25_deposit_foreign_number() != null) {
								R25cell3.setCellValue(record.getR25_deposit_foreign_number().doubleValue());
								R25cell3.setCellStyle(numberStyle);
							} else {
								R25cell3.setCellValue("");
								R25cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R25cell4 = row.createCell(5);
							if (record.getR25_deposit_foreign_amount() != null) {
								R25cell4.setCellValue(record.getR25_deposit_foreign_amount().doubleValue());
								R25cell4.setCellStyle(numberStyle);
							} else {
								R25cell4.setCellValue("");
								R25cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R25cell5 = row.createCell(8);
							if (record2.getR25_TOTAL_DEPOSIT_EXCEED() != null) {
							R25cell5.setCellValue(record2.getR25_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R25cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R25cell6 = row.createCell(9);
							if (record.getR25_total_deposit_bank() != null) {
							R25cell6.setCellValue(record.getR25_total_deposit_bank().doubleValue());
							R25cell6.setCellStyle(numberStyle);
							} else {
							R25cell6.setCellValue("");
							R25cell6.setCellStyle(textStyle);
							}

							// R26
							row = sheet.getRow(25);
							// Column C
							Cell R26cell1 = row.createCell(2);
							if (record.getR26_deposit_excluding_number() != null) {
								R26cell1.setCellValue(record.getR26_deposit_excluding_number().doubleValue());
								R26cell1.setCellStyle(numberStyle);
							} else {
								R26cell1.setCellValue("");
								R26cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R26cell2 = row.createCell(3);
							if (record.getR26_deposit_excluding_amount() != null) {
								R26cell2.setCellValue(record.getR26_deposit_excluding_amount().doubleValue());
								R26cell2.setCellStyle(numberStyle);
							} else {
								R26cell2.setCellValue("");
								R26cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R26cell3 = row.createCell(4);
							if (record.getR26_deposit_foreign_number() != null) {
								R26cell3.setCellValue(record.getR26_deposit_foreign_number().doubleValue());
								R26cell3.setCellStyle(numberStyle);
							} else {
								R26cell3.setCellValue("");
								R26cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R26cell4 = row.createCell(5);
							if (record.getR26_deposit_foreign_amount() != null) {
								R26cell4.setCellValue(record.getR26_deposit_foreign_amount().doubleValue());
								R26cell4.setCellStyle(numberStyle);
							} else {
								R26cell4.setCellValue("");
								R26cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R26cell5 = row.createCell(8);
							if (record2.getR26_TOTAL_DEPOSIT_EXCEED() != null) {
							R26cell5.setCellValue(record2.getR26_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R26cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R26cell6 = row.createCell(9);
							if (record.getR26_total_deposit_bank() != null) {
							R26cell6.setCellValue(record.getR26_total_deposit_bank().doubleValue());
							R26cell6.setCellStyle(numberStyle);
							} else {
							R26cell6.setCellValue("");
							R26cell6.setCellStyle(textStyle);
							}

							// R27
							row = sheet.getRow(26);
							// Column C
							Cell R27cell1 = row.createCell(2);
							if (record.getR27_deposit_excluding_number() != null) {
								R27cell1.setCellValue(record.getR27_deposit_excluding_number().doubleValue());
								R27cell1.setCellStyle(numberStyle);
							} else {
								R27cell1.setCellValue("");
								R27cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R27cell2 = row.createCell(3);
							if (record.getR27_deposit_excluding_amount() != null) {
								R27cell2.setCellValue(record.getR27_deposit_excluding_amount().doubleValue());
								R27cell2.setCellStyle(numberStyle);
							} else {
								R27cell2.setCellValue("");
								R27cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R27cell3 = row.createCell(4);
							if (record.getR27_deposit_foreign_number() != null) {
								R27cell3.setCellValue(record.getR27_deposit_foreign_number().doubleValue());
								R27cell3.setCellStyle(numberStyle);
							} else {
								R27cell3.setCellValue("");
								R27cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R27cell4 = row.createCell(5);
							if (record.getR27_deposit_foreign_amount() != null) {
								R27cell4.setCellValue(record.getR27_deposit_foreign_amount().doubleValue());
								R27cell4.setCellStyle(numberStyle);
							} else {
								R27cell4.setCellValue("");
								R27cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R27cell5 = row.createCell(8);
							if (record2.getR27_TOTAL_DEPOSIT_EXCEED() != null) {
							R27cell5.setCellValue(record2.getR27_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R27cell5.setCellValue("");
							
							
							}
												
							// Column J
							Cell R27cell6 = row.createCell(9);
							if (record.getR27_total_deposit_bank() != null) {
							R27cell6.setCellValue(record.getR27_total_deposit_bank().doubleValue());
							R27cell6.setCellStyle(numberStyle);
							} else {
							R27cell6.setCellValue("");
							R27cell6.setCellStyle(textStyle);
							}

							// R28
							row = sheet.getRow(27);
							// Column C
							Cell R28cell1 = row.createCell(2);
							if (record.getR28_deposit_excluding_number() != null) {
								R28cell1.setCellValue(record.getR28_deposit_excluding_number().doubleValue());
								R28cell1.setCellStyle(numberStyle);
							} else {
								R28cell1.setCellValue("");
								R28cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R28cell2 = row.createCell(3);
							if (record.getR28_deposit_excluding_amount() != null) {
								R28cell2.setCellValue(record.getR28_deposit_excluding_amount().doubleValue());
								R28cell2.setCellStyle(numberStyle);
							} else {
								R28cell2.setCellValue("");
								R28cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R28cell3 = row.createCell(4);
							if (record.getR28_deposit_foreign_number() != null) {
								R28cell3.setCellValue(record.getR28_deposit_foreign_number().doubleValue());
								R28cell3.setCellStyle(numberStyle);
							} else {
								R28cell3.setCellValue("");
								R28cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R28cell4 = row.createCell(5);
							if (record.getR28_deposit_foreign_amount() != null) {
								R28cell4.setCellValue(record.getR28_deposit_foreign_amount().doubleValue());
								R28cell4.setCellStyle(numberStyle);
							} else {
								R28cell4.setCellValue("");
								R28cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R28cell5 = row.createCell(8);
							if (record2.getR28_TOTAL_DEPOSIT_EXCEED() != null) {
							R28cell5.setCellValue(record2.getR28_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R28cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R28cell6 = row.createCell(9);
							if (record.getR28_total_deposit_bank() != null) {
							R28cell6.setCellValue(record.getR28_total_deposit_bank().doubleValue());
							R28cell6.setCellStyle(numberStyle);
							} else {
							R28cell6.setCellValue("");
							R28cell6.setCellStyle(textStyle);
							}

							// R31
							row = sheet.getRow(30);
							// Column C
							Cell R31cell1 = row.createCell(2);
							if (record.getR31_deposit_excluding_number() != null) {
								R31cell1.setCellValue(record.getR31_deposit_excluding_number().doubleValue());
								R31cell1.setCellStyle(numberStyle);
							} else {
								R31cell1.setCellValue("");
								R31cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R31cell2 = row.createCell(3);
							if (record.getR31_deposit_excluding_amount() != null) {
								R31cell2.setCellValue(record.getR31_deposit_excluding_amount().doubleValue());
								R31cell2.setCellStyle(numberStyle);
							} else {
								R31cell2.setCellValue("");
								R31cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R31cell3 = row.createCell(4);
							if (record.getR31_deposit_foreign_number() != null) {
								R31cell3.setCellValue(record.getR31_deposit_foreign_number().doubleValue());
								R31cell3.setCellStyle(numberStyle);
							} else {
								R31cell3.setCellValue("");
								R31cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R31cell4 = row.createCell(5);
							if (record.getR31_deposit_foreign_amount() != null) {
								R31cell4.setCellValue(record.getR31_deposit_foreign_amount().doubleValue());
								R31cell4.setCellStyle(numberStyle);
							} else {
								R31cell4.setCellValue("");
								R31cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R31cell5 = row.createCell(8);
							if (record2.getR31_TOTAL_DEPOSIT_EXCEED() != null) {
							R31cell5.setCellValue(record2.getR31_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R31cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R31cell6 = row.createCell(9);
							if (record.getR31_total_deposit_bank() != null) {
							R31cell6.setCellValue(record.getR31_total_deposit_bank().doubleValue());
							R31cell6.setCellStyle(numberStyle);
							} else {
							R31cell6.setCellValue("");
							R31cell6.setCellStyle(textStyle);
							}

							// R32
							row = sheet.getRow(31);
							// Column C
							Cell R32cell1 = row.createCell(2);
							if (record.getR32_deposit_excluding_number() != null) {
								R32cell1.setCellValue(record.getR32_deposit_excluding_number().doubleValue());
								R32cell1.setCellStyle(numberStyle);
							} else {
								R32cell1.setCellValue("");
								R32cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R32cell2 = row.createCell(3);
							if (record.getR32_deposit_excluding_amount() != null) {
								R32cell2.setCellValue(record.getR32_deposit_excluding_amount().doubleValue());
								R32cell2.setCellStyle(numberStyle);
							} else {
								R32cell2.setCellValue("");
								R32cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R32cell3 = row.createCell(4);
							if (record.getR32_deposit_foreign_number() != null) {
								R32cell3.setCellValue(record.getR32_deposit_foreign_number().doubleValue());
								R32cell3.setCellStyle(numberStyle);
							} else {
								R32cell3.setCellValue("");
								R32cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R32cell4 = row.createCell(5);
							if (record.getR32_deposit_foreign_amount() != null) {
								R32cell4.setCellValue(record.getR32_deposit_foreign_amount().doubleValue());
								R32cell4.setCellStyle(numberStyle);
							} else {
								R32cell4.setCellValue("");
								R32cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R32cell5 = row.createCell(8);
							if (record2.getR32_TOTAL_DEPOSIT_EXCEED() != null) {
							R32cell5.setCellValue(record2.getR32_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R32cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R32cell6 = row.createCell(9);
							if (record.getR32_total_deposit_bank() != null) {
							R32cell6.setCellValue(record.getR32_total_deposit_bank().doubleValue());
							R32cell6.setCellStyle(numberStyle);
							} else {
							R32cell6.setCellValue("");
							R32cell6.setCellStyle(textStyle);
							}

							// R33
							row = sheet.getRow(32);
							// Column C
							Cell R33cell1 = row.createCell(2);
							if (record.getR33_deposit_excluding_number() != null) {
								R33cell1.setCellValue(record.getR33_deposit_excluding_number().doubleValue());
								R33cell1.setCellStyle(numberStyle);
							} else {
								R33cell1.setCellValue("");
								R33cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R33cell2 = row.createCell(3);
							if (record.getR33_deposit_excluding_amount() != null) {
								R33cell2.setCellValue(record.getR33_deposit_excluding_amount().doubleValue());
								R33cell2.setCellStyle(numberStyle);
							} else {
								R33cell2.setCellValue("");
								R33cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R33cell3 = row.createCell(4);
							if (record.getR33_deposit_foreign_number() != null) {
								R33cell3.setCellValue(record.getR33_deposit_foreign_number().doubleValue());
								R33cell3.setCellStyle(numberStyle);
							} else {
								R33cell3.setCellValue("");
								R33cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R33cell4 = row.createCell(5);
							if (record.getR33_deposit_foreign_amount() != null) {
								R33cell4.setCellValue(record.getR33_deposit_foreign_amount().doubleValue());
								R33cell4.setCellStyle(numberStyle);
							} else {
								R33cell4.setCellValue("");
								R33cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R33cell5 = row.createCell(8);
							if (record2.getR33_TOTAL_DEPOSIT_EXCEED() != null) {
							R33cell5.setCellValue(record2.getR33_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R33cell5.setCellValue("");
							}
												
							// Column J
							Cell R33cell6 = row.createCell(9);
							if (record.getR33_total_deposit_bank() != null) {
							R33cell6.setCellValue(record.getR33_total_deposit_bank().doubleValue());
							R33cell6.setCellStyle(numberStyle);
							} else {
							R33cell6.setCellValue("");
							R33cell6.setCellStyle(textStyle);
							}

							// R34
							row = sheet.getRow(33);
							// Column C
							Cell R34cell1 = row.createCell(2);
							if (record.getR34_deposit_excluding_number() != null) {
								R34cell1.setCellValue(record.getR34_deposit_excluding_number().doubleValue());
								R34cell1.setCellStyle(numberStyle);
							} else {
								R34cell1.setCellValue("");
								R34cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R34cell2 = row.createCell(3);
							if (record.getR34_deposit_excluding_amount() != null) {
								R34cell2.setCellValue(record.getR34_deposit_excluding_amount().doubleValue());
								R34cell2.setCellStyle(numberStyle);
							} else {
								R34cell2.setCellValue("");
								R34cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R34cell3 = row.createCell(4);
							if (record.getR34_deposit_foreign_number() != null) {
								R34cell3.setCellValue(record.getR34_deposit_foreign_number().doubleValue());
								R34cell3.setCellStyle(numberStyle);
							} else {
								R34cell3.setCellValue("");
								R34cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R34cell4 = row.createCell(5);
							if (record.getR34_deposit_foreign_amount() != null) {
								R34cell4.setCellValue(record.getR34_deposit_foreign_amount().doubleValue());
								R34cell4.setCellStyle(numberStyle);
							} else {
								R34cell4.setCellValue("");
								R34cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R34cell5 = row.createCell(8);
							if (record2.getR34_TOTAL_DEPOSIT_EXCEED() != null) {
							R34cell5.setCellValue(record2.getR34_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R34cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R34cell6 = row.createCell(9);
							if (record.getR34_total_deposit_bank() != null) {
							R34cell6.setCellValue(record.getR34_total_deposit_bank().doubleValue());
							R34cell6.setCellStyle(numberStyle);
							} else {
							R34cell6.setCellValue("");
							R34cell6.setCellStyle(textStyle);
							}

							// R35
							row = sheet.getRow(34);
							// Column C
							Cell R35cell1 = row.createCell(2);
							if (record.getR35_deposit_excluding_number() != null) {
								R35cell1.setCellValue(record.getR35_deposit_excluding_number().doubleValue());
								R35cell1.setCellStyle(numberStyle);
							} else {
								R35cell1.setCellValue("");
								R35cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R35cell2 = row.createCell(3);
							if (record.getR35_deposit_excluding_amount() != null) {
								R35cell2.setCellValue(record.getR35_deposit_excluding_amount().doubleValue());
								R35cell2.setCellStyle(numberStyle);
							} else {
								R35cell2.setCellValue("");
								R35cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R35cell3 = row.createCell(4);
							if (record.getR35_deposit_foreign_number() != null) {
								R35cell3.setCellValue(record.getR35_deposit_foreign_number().doubleValue());
								R35cell3.setCellStyle(numberStyle);
							} else {
								R35cell3.setCellValue("");
								R35cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R35cell4 = row.createCell(5);
							if (record.getR35_deposit_foreign_amount() != null) {
								R35cell4.setCellValue(record.getR35_deposit_foreign_amount().doubleValue());
								R35cell4.setCellStyle(numberStyle);
							} else {
								R35cell4.setCellValue("");
								R35cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R35cell5 = row.createCell(8);
							if (record2.getR35_TOTAL_DEPOSIT_EXCEED() != null) {
							R35cell5.setCellValue(record2.getR35_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R35cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R35cell6 = row.createCell(9);
							if (record.getR35_total_deposit_bank() != null) {
							R35cell6.setCellValue(record.getR35_total_deposit_bank().doubleValue());
							R35cell6.setCellStyle(numberStyle);
							} else {
							R35cell6.setCellValue("");
							R35cell6.setCellStyle(textStyle);
							}

							// R36
							row = sheet.getRow(35);
							// Column C
							Cell R36cell1 = row.createCell(2);
							if (record.getR36_deposit_excluding_number() != null) {
								R36cell1.setCellValue(record.getR36_deposit_excluding_number().doubleValue());
								R36cell1.setCellStyle(numberStyle);
							} else {
								R36cell1.setCellValue("");
								R36cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R36cell2 = row.createCell(3);
							if (record.getR36_deposit_excluding_amount() != null) {
								R36cell2.setCellValue(record.getR36_deposit_excluding_amount().doubleValue());
								R36cell2.setCellStyle(numberStyle);
							} else {
								R36cell2.setCellValue("");
								R36cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R36cell3 = row.createCell(4);
							if (record.getR36_deposit_foreign_number() != null) {
								R36cell3.setCellValue(record.getR36_deposit_foreign_number().doubleValue());
								R36cell3.setCellStyle(numberStyle);
							} else {
								R36cell3.setCellValue("");
								R36cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R36cell4 = row.createCell(5);
							if (record.getR36_deposit_foreign_amount() != null) {
								R36cell4.setCellValue(record.getR36_deposit_foreign_amount().doubleValue());
								R36cell4.setCellStyle(numberStyle);
							} else {
								R36cell4.setCellValue("");
								R36cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R36cell5 = row.createCell(8);
							if (record2.getR36_TOTAL_DEPOSIT_EXCEED() != null) {
							R36cell5.setCellValue(record2.getR36_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R36cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R36cell6 = row.createCell(9);
							if (record.getR36_total_deposit_bank() != null) {
							R36cell6.setCellValue(record.getR36_total_deposit_bank().doubleValue());
							R36cell6.setCellStyle(numberStyle);
							} else {
							R36cell6.setCellValue("");
							R36cell6.setCellStyle(textStyle);
							}


							// R39
							row = sheet.getRow(38);
							// Column C
							Cell R39cell1 = row.createCell(2);
							if (record.getR39_deposit_excluding_number() != null) {
								R39cell1.setCellValue(record.getR39_deposit_excluding_number().doubleValue());
								R39cell1.setCellStyle(numberStyle);
							} else {
								R39cell1.setCellValue("");
								R39cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R39cell2 = row.createCell(3);
							if (record.getR39_deposit_excluding_amount() != null) {
								R39cell2.setCellValue(record.getR39_deposit_excluding_amount().doubleValue());
								R39cell2.setCellStyle(numberStyle);
							} else {
								R39cell2.setCellValue("");
								R39cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R39cell3 = row.createCell(4);
							if (record.getR39_deposit_foreign_number() != null) {
								R39cell3.setCellValue(record.getR39_deposit_foreign_number().doubleValue());
								R39cell3.setCellStyle(numberStyle);
							} else {
								R39cell3.setCellValue("");
								R39cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R39cell4 = row.createCell(5);
							if (record.getR39_deposit_foreign_amount() != null) {
								R39cell4.setCellValue(record.getR39_deposit_foreign_amount().doubleValue());
								R39cell4.setCellStyle(numberStyle);
							} else {
								R39cell4.setCellValue("");
								R39cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R39cell5 = row.createCell(8);
							if (record2.getR39_TOTAL_DEPOSIT_EXCEED() != null) {
							R39cell5.setCellValue(record2.getR39_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R39cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R39cell6 = row.createCell(9);
							if (record.getR39_total_deposit_bank() != null) {
							R39cell6.setCellValue(record.getR39_total_deposit_bank().doubleValue());
							R39cell6.setCellStyle(numberStyle);
							} else {
							R39cell6.setCellValue("");
							R39cell6.setCellStyle(textStyle);
							}

							
							// R40
							row = sheet.getRow(39);
							// Column C
							Cell R40cell1 = row.createCell(2);
							if (record.getR40_deposit_excluding_number() != null) {
								R40cell1.setCellValue(record.getR40_deposit_excluding_number().doubleValue());
								R40cell1.setCellStyle(numberStyle);
							} else {
								R40cell1.setCellValue("");
								R40cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R40cell2 = row.createCell(3);
							if (record.getR40_deposit_excluding_amount() != null) {
								R40cell2.setCellValue(record.getR40_deposit_excluding_amount().doubleValue());
								R40cell2.setCellStyle(numberStyle);
							} else {
								R40cell2.setCellValue("");
								R40cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R40cell3 = row.createCell(4);
							if (record.getR40_deposit_foreign_number() != null) {
								R40cell3.setCellValue(record.getR40_deposit_foreign_number().doubleValue());
								R40cell3.setCellStyle(numberStyle);
							} else {
								R40cell3.setCellValue("");
								R40cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R40cell4 = row.createCell(5);
							if (record.getR40_deposit_foreign_amount() != null) {
								R40cell4.setCellValue(record.getR40_deposit_foreign_amount().doubleValue());
								R40cell4.setCellStyle(numberStyle);
							} else {
								R40cell4.setCellValue("");
								R40cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R40cell5 = row.createCell(8);
							if (record2.getR40_TOTAL_DEPOSIT_EXCEED() != null) {
							R40cell5.setCellValue(record2.getR40_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R40cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R40cell6 = row.createCell(9);
							if (record.getR40_total_deposit_bank() != null) {
							R40cell6.setCellValue(record.getR40_total_deposit_bank().doubleValue());
							R40cell6.setCellStyle(numberStyle);
							} else {
							R40cell6.setCellValue("");
							R40cell6.setCellStyle(textStyle);
							}

							// R41
							row = sheet.getRow(40);
							// Column C
							Cell R41cell1 = row.createCell(2);
							if (record.getR41_deposit_excluding_number() != null) {
								R41cell1.setCellValue(record.getR41_deposit_excluding_number().doubleValue());
								R41cell1.setCellStyle(numberStyle);
							} else {
								R41cell1.setCellValue("");
								R41cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R41cell2 = row.createCell(3);
							if (record.getR41_deposit_excluding_amount() != null) {
								R41cell2.setCellValue(record.getR41_deposit_excluding_amount().doubleValue());
								R41cell2.setCellStyle(numberStyle);
							} else {
								R41cell2.setCellValue("");
								R41cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R41cell3 = row.createCell(4);
							if (record.getR41_deposit_foreign_number() != null) {
								R41cell3.setCellValue(record.getR41_deposit_foreign_number().doubleValue());
								R41cell3.setCellStyle(numberStyle);
							} else {
								R41cell3.setCellValue("");
								R41cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R41cell4 = row.createCell(5);
							if (record.getR41_deposit_foreign_amount() != null) {
								R41cell4.setCellValue(record.getR41_deposit_foreign_amount().doubleValue());
								R41cell4.setCellStyle(numberStyle);
							} else {
								R41cell4.setCellValue("");
								R41cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R41cell5 = row.createCell(8);
							if (record2.getR41_TOTAL_DEPOSIT_EXCEED() != null) {
							R41cell5.setCellValue(record2.getR41_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R41cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R41cell6 = row.createCell(9);
							if (record.getR41_total_deposit_bank() != null) {
							R41cell6.setCellValue(record.getR41_total_deposit_bank().doubleValue());
							R41cell6.setCellStyle(numberStyle);
							} else {
							R41cell6.setCellValue("");
							R41cell6.setCellStyle(textStyle);
							}


							// R42
							row = sheet.getRow(41);
							// Column C
							Cell R42cell1 = row.createCell(2);
							if (record.getR42_deposit_excluding_number() != null) {
								R42cell1.setCellValue(record.getR42_deposit_excluding_number().doubleValue());
								R42cell1.setCellStyle(numberStyle);
							} else {
								R42cell1.setCellValue("");
								R42cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R42cell2 = row.createCell(3);
							if (record.getR42_deposit_excluding_amount() != null) {
								R42cell2.setCellValue(record.getR42_deposit_excluding_amount().doubleValue());
								R42cell2.setCellStyle(numberStyle);
							} else {
								R42cell2.setCellValue("");
								R42cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R42cell3 = row.createCell(4);
							if (record.getR42_deposit_foreign_number() != null) {
								R42cell3.setCellValue(record.getR42_deposit_foreign_number().doubleValue());
								R42cell3.setCellStyle(numberStyle);
							} else {
								R42cell3.setCellValue("");
								R42cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R42cell4 = row.createCell(5);
							if (record.getR42_deposit_foreign_amount() != null) {
								R42cell4.setCellValue(record.getR42_deposit_foreign_amount().doubleValue());
								R42cell4.setCellStyle(numberStyle);
							} else {
								R42cell4.setCellValue("");
								R42cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R42cell5 = row.createCell(8);
							if (record2.getR42_TOTAL_DEPOSIT_EXCEED() != null) {
							R42cell5.setCellValue(record2.getR42_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R42cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R42cell6 = row.createCell(9);
							if (record.getR42_total_deposit_bank() != null) {
							R42cell6.setCellValue(record.getR42_total_deposit_bank().doubleValue());
							R42cell6.setCellStyle(numberStyle);
							} else {
							R42cell6.setCellValue("");
							R42cell6.setCellStyle(textStyle);
							}


							// R43
							row = sheet.getRow(42);
							// Column C
							Cell R43cell1 = row.createCell(2);
							if (record.getR43_deposit_excluding_number() != null) {
								R43cell1.setCellValue(record.getR43_deposit_excluding_number().doubleValue());
								R43cell1.setCellStyle(numberStyle);
							} else {
								R43cell1.setCellValue("");
								R43cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R43cell2 = row.createCell(3);
							if (record.getR43_deposit_excluding_amount() != null) {
								R43cell2.setCellValue(record.getR43_deposit_excluding_amount().doubleValue());
								R43cell2.setCellStyle(numberStyle);
							} else {
								R43cell2.setCellValue("");
								R43cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R43cell3 = row.createCell(4);
							if (record.getR43_deposit_foreign_number() != null) {
								R43cell3.setCellValue(record.getR43_deposit_foreign_number().doubleValue());
								R43cell3.setCellStyle(numberStyle);
							} else {
								R43cell3.setCellValue("");
								R43cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R43cell4 = row.createCell(5);
							if (record.getR43_deposit_foreign_amount() != null) {
								R43cell4.setCellValue(record.getR43_deposit_foreign_amount().doubleValue());
								R43cell4.setCellStyle(numberStyle);
							} else {
								R43cell4.setCellValue("");
								R43cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R43cell5 = row.createCell(8);
							if (record2.getR43_TOTAL_DEPOSIT_EXCEED() != null) {
							R43cell5.setCellValue(record2.getR43_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R43cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R43cell6 = row.createCell(9);
							if (record.getR43_total_deposit_bank() != null) {
							R43cell6.setCellValue(record.getR43_total_deposit_bank().doubleValue());
							R43cell6.setCellStyle(numberStyle);
							} else {
							R43cell6.setCellValue("");
							R43cell6.setCellStyle(textStyle);
							}

							// R44
							row = sheet.getRow(43);
							// Column C
							Cell R44cell1 = row.createCell(2);
							if (record.getR44_deposit_excluding_number() != null) {
								R44cell1.setCellValue(record.getR44_deposit_excluding_number().doubleValue());
								R44cell1.setCellStyle(numberStyle);
							} else {
								R44cell1.setCellValue("");
								R44cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R44cell2 = row.createCell(3);
							if (record.getR44_deposit_excluding_amount() != null) {
								R44cell2.setCellValue(record.getR44_deposit_excluding_amount().doubleValue());
								R44cell2.setCellStyle(numberStyle);
							} else {
								R44cell2.setCellValue("");
								R44cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R44cell3 = row.createCell(4);
							if (record.getR44_deposit_foreign_number() != null) {
								R44cell3.setCellValue(record.getR44_deposit_foreign_number().doubleValue());
								R44cell3.setCellStyle(numberStyle);
							} else {
								R44cell3.setCellValue("");
								R44cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R44cell4 = row.createCell(5);
							if (record.getR44_deposit_foreign_amount() != null) {
								R44cell4.setCellValue(record.getR44_deposit_foreign_amount().doubleValue());
								R44cell4.setCellStyle(numberStyle);
							} else {
								R44cell4.setCellValue("");
								R44cell4.setCellStyle(textStyle);
							}
							// Column I
							Cell R44cell5 = row.createCell(8);
							if (record2.getR44_TOTAL_DEPOSIT_EXCEED() != null) {
							R44cell5.setCellValue(record2.getR44_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R44cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R44cell6 = row.createCell(9);
							if (record.getR44_total_deposit_bank() != null) {
							R44cell6.setCellValue(record.getR44_total_deposit_bank().doubleValue());
							R44cell6.setCellStyle(numberStyle);
							} else {
							R44cell6.setCellValue("");
							R44cell6.setCellStyle(textStyle);
							}

							// R47
							row = sheet.getRow(46);
							// Column C
							Cell R47cell1 = row.createCell(2);
							if (record.getR47_deposit_excluding_number() != null) {
								R47cell1.setCellValue(record.getR47_deposit_excluding_number().doubleValue());
								R47cell1.setCellStyle(numberStyle);
							} else {
								R47cell1.setCellValue("");
								R47cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R47cell2 = row.createCell(3);
							if (record.getR47_deposit_excluding_amount() != null) {
								R47cell2.setCellValue(record.getR47_deposit_excluding_amount().doubleValue());
								R47cell2.setCellStyle(numberStyle);
							} else {
								R47cell2.setCellValue("");
								R47cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R47cell3 = row.createCell(4);
							if (record.getR47_deposit_foreign_number() != null) {
								R47cell3.setCellValue(record.getR47_deposit_foreign_number().doubleValue());
								R47cell3.setCellStyle(numberStyle);
							} else {
								R47cell3.setCellValue("");
								R47cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R47cell4 = row.createCell(5);
							if (record.getR47_deposit_foreign_amount() != null) {
								R47cell4.setCellValue(record.getR47_deposit_foreign_amount().doubleValue());
								R47cell4.setCellStyle(numberStyle);
							} else {
								R47cell4.setCellValue("");
								R47cell4.setCellStyle(textStyle);
							}
							// Column I
							Cell R47cell5 = row.createCell(8);
							if (record2.getR47_TOTAL_DEPOSIT_EXCEED() != null) {
							R47cell5.setCellValue(record2.getR47_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R47cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R47cell6 = row.createCell(9);
							if (record.getR47_total_deposit_bank() != null) {
							R47cell6.setCellValue(record.getR47_total_deposit_bank().doubleValue());
							R47cell6.setCellStyle(numberStyle);
							} else {
							R47cell6.setCellValue("");
							R47cell6.setCellStyle(textStyle);
							}

							// R48
							row = sheet.getRow(47);
							// Column C
							Cell R48cell1 = row.createCell(2);
							if (record.getR48_deposit_excluding_number() != null) {
								R48cell1.setCellValue(record.getR48_deposit_excluding_number().doubleValue());
								R48cell1.setCellStyle(numberStyle);
							} else {
								R48cell1.setCellValue("");
								R48cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R48cell2 = row.createCell(3);
							if (record.getR48_deposit_excluding_amount() != null) {
								R48cell2.setCellValue(record.getR48_deposit_excluding_amount().doubleValue());
								R48cell2.setCellStyle(numberStyle);
							} else {
								R48cell2.setCellValue("");
								R48cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R48cell3 = row.createCell(4);
							if (record.getR48_deposit_foreign_number() != null) {
								R48cell3.setCellValue(record.getR48_deposit_foreign_number().doubleValue());
								R48cell3.setCellStyle(numberStyle);
							} else {
								R48cell3.setCellValue("");
								R48cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R48cell4 = row.createCell(5);
							if (record.getR48_deposit_foreign_amount() != null) {
								R48cell4.setCellValue(record.getR48_deposit_foreign_amount().doubleValue());
								R48cell4.setCellStyle(numberStyle);
							} else {
								R48cell4.setCellValue("");
								R48cell4.setCellStyle(textStyle);
							}
							// Column I
							Cell R48cell5 = row.createCell(8);
							if (record2.getR48_TOTAL_DEPOSIT_EXCEED() != null) {
							R48cell5.setCellValue(record2.getR48_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R48cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R48cell6 = row.createCell(9);
							if (record.getR48_total_deposit_bank() != null) {
							R48cell6.setCellValue(record.getR48_total_deposit_bank().doubleValue());
							R48cell6.setCellStyle(numberStyle);
							} else {
							R48cell6.setCellValue("");
							R48cell6.setCellStyle(textStyle);
							}


							// R49
							row = sheet.getRow(48);
							// Column C
							Cell R49cell1 = row.createCell(2);
							if (record.getR49_deposit_excluding_number() != null) {
								R49cell1.setCellValue(record.getR49_deposit_excluding_number().doubleValue());
								R49cell1.setCellStyle(numberStyle);
							} else {
								R49cell1.setCellValue("");
								R49cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R49cell2 = row.createCell(3);
							if (record.getR49_deposit_excluding_amount() != null) {
								R49cell2.setCellValue(record.getR49_deposit_excluding_amount().doubleValue());
								R49cell2.setCellStyle(numberStyle);
							} else {
								R49cell2.setCellValue("");
								R49cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R49cell3 = row.createCell(4);
							if (record.getR49_deposit_foreign_number() != null) {
								R49cell3.setCellValue(record.getR49_deposit_foreign_number().doubleValue());
								R49cell3.setCellStyle(numberStyle);
							} else {
								R49cell3.setCellValue("");
								R49cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R49cell4 = row.createCell(5);
							if (record.getR49_deposit_foreign_amount() != null) {
								R49cell4.setCellValue(record.getR49_deposit_foreign_amount().doubleValue());
								R49cell4.setCellStyle(numberStyle);
							} else {
								R49cell4.setCellValue("");
								R49cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R49cell5 = row.createCell(8);
							if (record2.getR49_TOTAL_DEPOSIT_EXCEED() != null) {
							R49cell5.setCellValue(record2.getR49_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R49cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R49cell6 = row.createCell(9);
							if (record.getR49_total_deposit_bank() != null) {
							R49cell6.setCellValue(record.getR49_total_deposit_bank().doubleValue());
							R49cell6.setCellStyle(numberStyle);
							} else {
							R49cell6.setCellValue("");
							R49cell6.setCellStyle(textStyle);
							}

							// R50
							row = sheet.getRow(49);
							// Column C
							Cell R50cell1 = row.createCell(2);
							if (record.getR50_deposit_excluding_number() != null) {
								R50cell1.setCellValue(record.getR50_deposit_excluding_number().doubleValue());
								R50cell1.setCellStyle(numberStyle);
							} else {
								R50cell1.setCellValue("");
								R50cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R50cell2 = row.createCell(3);
							if (record.getR50_deposit_excluding_amount() != null) {
								R50cell2.setCellValue(record.getR50_deposit_excluding_amount().doubleValue());
								R50cell2.setCellStyle(numberStyle);
							} else {
								R50cell2.setCellValue("");
								R50cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R50cell3 = row.createCell(4);
							if (record.getR50_deposit_foreign_number() != null) {
								R50cell3.setCellValue(record.getR50_deposit_foreign_number().doubleValue());
								R50cell3.setCellStyle(numberStyle);
							} else {
								R50cell3.setCellValue("");
								R50cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R50cell4 = row.createCell(5);
							if (record.getR50_deposit_foreign_amount() != null) {
								R50cell4.setCellValue(record.getR50_deposit_foreign_amount().doubleValue());
								R50cell4.setCellStyle(numberStyle);
							} else {
								R50cell4.setCellValue("");
								R50cell4.setCellStyle(textStyle);
							}
							// Column I
							Cell R50cell5 = row.createCell(8);
							if (record2.getR50_TOTAL_DEPOSIT_EXCEED() != null) {
							R50cell5.setCellValue(record2.getR50_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R50cell5.setCellValue("");
						
							}
												
							// Column J
							Cell R50cell6 = row.createCell(9);
							if (record.getR50_total_deposit_bank() != null) {
							R50cell6.setCellValue(record.getR50_total_deposit_bank().doubleValue());
							R50cell6.setCellStyle(numberStyle);
							} else {
							R50cell6.setCellValue("");
							R50cell6.setCellStyle(textStyle);
							}

							// R51
							row = sheet.getRow(50);
							// Column C
							Cell R51cell1 = row.createCell(2);
							if (record.getR51_deposit_excluding_number() != null) {
								R51cell1.setCellValue(record.getR51_deposit_excluding_number().doubleValue());
								R51cell1.setCellStyle(numberStyle);
							} else {
								R51cell1.setCellValue("");
								R51cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R51cell2 = row.createCell(3);
							if (record.getR51_deposit_excluding_amount() != null) {
								R51cell2.setCellValue(record.getR51_deposit_excluding_amount().doubleValue());
								R51cell2.setCellStyle(numberStyle);
							} else {
								R51cell2.setCellValue("");
								R51cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R51cell3 = row.createCell(4);
							if (record.getR51_deposit_foreign_number() != null) {
								R51cell3.setCellValue(record.getR51_deposit_foreign_number().doubleValue());
								R51cell3.setCellStyle(numberStyle);
							} else {
								R51cell3.setCellValue("");
								R51cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R51cell4 = row.createCell(5);
							if (record.getR51_deposit_foreign_amount() != null) {
								R51cell4.setCellValue(record.getR51_deposit_foreign_amount().doubleValue());
								R51cell4.setCellStyle(numberStyle);
							} else {
								R51cell4.setCellValue("");
								R51cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R51cell5 = row.createCell(8);
							if (record2.getR51_TOTAL_DEPOSIT_EXCEED() != null) {
							R51cell5.setCellValue(record2.getR51_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R51cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R51cell6 = row.createCell(9);
							if (record.getR51_total_deposit_bank() != null) {
							R51cell6.setCellValue(record.getR51_total_deposit_bank().doubleValue());
							R51cell6.setCellStyle(numberStyle);
							} else {
							R51cell6.setCellValue("");
							R51cell6.setCellStyle(textStyle);
							}

							// R52
							row = sheet.getRow(51);
							// Column C
							Cell R52cell1 = row.createCell(2);
							if (record.getR52_deposit_excluding_number() != null) {
								R52cell1.setCellValue(record.getR52_deposit_excluding_number().doubleValue());
								R52cell1.setCellStyle(numberStyle);
							} else {
								R52cell1.setCellValue("");
								R52cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R52cell2 = row.createCell(3);
							if (record.getR52_deposit_excluding_amount() != null) {
								R52cell2.setCellValue(record.getR52_deposit_excluding_amount().doubleValue());
								R52cell2.setCellStyle(numberStyle);
							} else {
								R52cell2.setCellValue("");
								R52cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R52cell3 = row.createCell(4);
							if (record.getR52_deposit_foreign_number() != null) {
								R52cell3.setCellValue(record.getR52_deposit_foreign_number().doubleValue());
								R52cell3.setCellStyle(numberStyle);
							} else {
								R52cell3.setCellValue("");
								R52cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R52cell4 = row.createCell(5);
							if (record.getR52_deposit_foreign_amount() != null) {
								R52cell4.setCellValue(record.getR52_deposit_foreign_amount().doubleValue());
								R52cell4.setCellStyle(numberStyle);
							} else {
								R52cell4.setCellValue("");
								R52cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R52cell5 = row.createCell(8);
							if (record2.getR52_TOTAL_DEPOSIT_EXCEED() != null) {
							R52cell5.setCellValue(record2.getR52_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R52cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R52cell6 = row.createCell(9);
							if (record.getR52_total_deposit_bank() != null) {
							R52cell6.setCellValue(record.getR52_total_deposit_bank().doubleValue());
							R52cell6.setCellStyle(numberStyle);
							} else {
							R52cell6.setCellValue("");
							R52cell6.setCellStyle(textStyle);
							}


							// R55
							row = sheet.getRow(54);
							// Column C
							Cell R55cell1 = row.createCell(2);
							if (record.getR55_deposit_excluding_number() != null) {
								R55cell1.setCellValue(record.getR55_deposit_excluding_number().doubleValue());
								R55cell1.setCellStyle(numberStyle);
							} else {
								R55cell1.setCellValue("");
								R55cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R55cell2 = row.createCell(3);
							if (record.getR55_deposit_excluding_amount() != null) {
								R55cell2.setCellValue(record.getR55_deposit_excluding_amount().doubleValue());
								R55cell2.setCellStyle(numberStyle);
							} else {
								R55cell2.setCellValue("");
								R55cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R55cell3 = row.createCell(4);
							if (record.getR55_deposit_foreign_number() != null) {
								R55cell3.setCellValue(record.getR55_deposit_foreign_number().doubleValue());
								R55cell3.setCellStyle(numberStyle);
							} else {
								R55cell3.setCellValue("");
								R55cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R55cell4 = row.createCell(5);
							if (record.getR55_deposit_foreign_amount() != null) {
								R55cell4.setCellValue(record.getR55_deposit_foreign_amount().doubleValue());
								R55cell4.setCellStyle(numberStyle);
							} else {
								R55cell4.setCellValue("");
								R55cell4.setCellStyle(textStyle);
							}
							// Column I
							Cell R55cell5 = row.createCell(8);
							if (record2.getR55_TOTAL_DEPOSIT_EXCEED() != null) {
							R55cell5.setCellValue(record2.getR55_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R55cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R55cell6 = row.createCell(9);
							if (record.getR55_total_deposit_bank() != null) {
							R55cell6.setCellValue(record.getR55_total_deposit_bank().doubleValue());
							R55cell6.setCellStyle(numberStyle);
							} else {
							R55cell6.setCellValue("");
							R55cell6.setCellStyle(textStyle);
							}


							// R56
							row = sheet.getRow(55);
							// Column C
							Cell R56cell1 = row.createCell(2);
							if (record.getR56_deposit_excluding_number() != null) {
								R56cell1.setCellValue(record.getR56_deposit_excluding_number().doubleValue());
								R56cell1.setCellStyle(numberStyle);
							} else {
								R56cell1.setCellValue("");
								R56cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R56cell2 = row.createCell(3);
							if (record.getR56_deposit_excluding_amount() != null) {
								R56cell2.setCellValue(record.getR56_deposit_excluding_amount().doubleValue());
								R56cell2.setCellStyle(numberStyle);
							} else {
								R56cell2.setCellValue("");
								R56cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R56cell3 = row.createCell(4);
							if (record.getR56_deposit_foreign_number() != null) {
								R56cell3.setCellValue(record.getR56_deposit_foreign_number().doubleValue());
								R56cell3.setCellStyle(numberStyle);
							} else {
								R56cell3.setCellValue("");
								R56cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R56cell4 = row.createCell(5);
							if (record.getR56_deposit_foreign_amount() != null) {
								R56cell4.setCellValue(record.getR56_deposit_foreign_amount().doubleValue());
								R56cell4.setCellStyle(numberStyle);
							} else {
								R56cell4.setCellValue("");
								R56cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R56cell5 = row.createCell(8);
							if (record2.getR56_TOTAL_DEPOSIT_EXCEED() != null) {
							R56cell5.setCellValue(record2.getR56_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R56cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R56cell6 = row.createCell(9);
							if (record.getR56_total_deposit_bank() != null) {
							R56cell6.setCellValue(record.getR56_total_deposit_bank().doubleValue());
							R56cell6.setCellStyle(numberStyle);
							} else {
							R56cell6.setCellValue("");
							R56cell6.setCellStyle(textStyle);
							}


							// R57
							row = sheet.getRow(56);
							// Column C
							Cell R57cell1 = row.createCell(2);
							if (record.getR57_deposit_excluding_number() != null) {
								R57cell1.setCellValue(record.getR57_deposit_excluding_number().doubleValue());
								R57cell1.setCellStyle(numberStyle);
							} else {
								R57cell1.setCellValue("");
								R57cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R57cell2 = row.createCell(3);
							if (record.getR57_deposit_excluding_amount() != null) {
								R57cell2.setCellValue(record.getR57_deposit_excluding_amount().doubleValue());
								R57cell2.setCellStyle(numberStyle);
							} else {
								R57cell2.setCellValue("");
								R57cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R57cell3 = row.createCell(4);
							if (record.getR57_deposit_foreign_number() != null) {
								R57cell3.setCellValue(record.getR57_deposit_foreign_number().doubleValue());
								R57cell3.setCellStyle(numberStyle);
							} else {
								R57cell3.setCellValue("");
								R57cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R57cell4 = row.createCell(5);
							if (record.getR57_deposit_foreign_amount() != null) {
								R57cell4.setCellValue(record.getR57_deposit_foreign_amount().doubleValue());
								R57cell4.setCellStyle(numberStyle);
							} else {
								R57cell4.setCellValue("");
								R57cell4.setCellStyle(textStyle);
							}
							// Column I
							Cell R57cell5 = row.createCell(8);
							if (record2.getR57_TOTAL_DEPOSIT_EXCEED() != null) {
							R57cell5.setCellValue(record2.getR57_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R57cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R57cell6 = row.createCell(9);
							if (record.getR57_total_deposit_bank() != null) {
							R57cell6.setCellValue(record.getR57_total_deposit_bank().doubleValue());
							R57cell6.setCellStyle(numberStyle);
							} else {
							R57cell6.setCellValue("");
							R57cell6.setCellStyle(textStyle);
							}

							// R58
							row = sheet.getRow(57);
							// Column C
							Cell R58cell1 = row.createCell(2);
							if (record.getR58_deposit_excluding_number() != null) {
								R58cell1.setCellValue(record.getR58_deposit_excluding_number().doubleValue());
								R58cell1.setCellStyle(numberStyle);
							} else {
								R58cell1.setCellValue("");
								R58cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R58cell2 = row.createCell(3);
							if (record.getR58_deposit_excluding_amount() != null) {
								R58cell2.setCellValue(record.getR58_deposit_excluding_amount().doubleValue());
								R58cell2.setCellStyle(numberStyle);
							} else {
								R58cell2.setCellValue("");
								R58cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R58cell3 = row.createCell(4);
							if (record.getR58_deposit_foreign_number() != null) {
								R58cell3.setCellValue(record.getR58_deposit_foreign_number().doubleValue());
								R58cell3.setCellStyle(numberStyle);
							} else {
								R58cell3.setCellValue("");
								R58cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R58cell4 = row.createCell(5);
							if (record.getR58_deposit_foreign_amount() != null) {
								R58cell4.setCellValue(record.getR58_deposit_foreign_amount().doubleValue());
								R58cell4.setCellStyle(numberStyle);
							} else {
								R58cell4.setCellValue("");
								R58cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R58cell5 = row.createCell(8);
							if (record2.getR58_TOTAL_DEPOSIT_EXCEED() != null) {
							R58cell5.setCellValue(record2.getR58_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R58cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R58cell6 = row.createCell(9);
							if (record.getR58_total_deposit_bank() != null) {
							R58cell6.setCellValue(record.getR58_total_deposit_bank().doubleValue());
							R58cell6.setCellStyle(numberStyle);
							} else {
							R58cell6.setCellValue("");
							R58cell6.setCellStyle(textStyle);
							}

							// R59
							row = sheet.getRow(58);
							// Column C
							Cell R59cell1 = row.createCell(2);
							if (record.getR59_deposit_excluding_number() != null) {
								R59cell1.setCellValue(record.getR59_deposit_excluding_number().doubleValue());
								R59cell1.setCellStyle(numberStyle);
							} else {
								R59cell1.setCellValue("");
								R59cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R59cell2 = row.createCell(3);
							if (record.getR59_deposit_excluding_amount() != null) {
								R59cell2.setCellValue(record.getR59_deposit_excluding_amount().doubleValue());
								R59cell2.setCellStyle(numberStyle);
							} else {
								R59cell2.setCellValue("");
								R59cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R59cell3 = row.createCell(4);
							if (record.getR59_deposit_foreign_number() != null) {
								R59cell3.setCellValue(record.getR59_deposit_foreign_number().doubleValue());
								R59cell3.setCellStyle(numberStyle);
							} else {
								R59cell3.setCellValue("");
								R59cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R59cell4 = row.createCell(5);
							if (record.getR59_deposit_foreign_amount() != null) {
								R59cell4.setCellValue(record.getR59_deposit_foreign_amount().doubleValue());
								R59cell4.setCellStyle(numberStyle);
							} else {
								R59cell4.setCellValue("");
								R59cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R59cell5 = row.createCell(8);
							if (record2.getR59_TOTAL_DEPOSIT_EXCEED() != null) {
							R59cell5.setCellValue(record2.getR59_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R59cell5.setCellValue("");
							}
												
							// Column J
							Cell R59cell6 = row.createCell(9);
							if (record.getR59_total_deposit_bank() != null) {
							R59cell6.setCellValue(record.getR59_total_deposit_bank().doubleValue());
							R59cell6.setCellStyle(numberStyle);
							} else {
							R59cell6.setCellValue("");
							R59cell6.setCellStyle(textStyle);
							}

							// R60
							row = sheet.getRow(59);
							// Column C
							Cell R60cell1 = row.createCell(2);
							if (record.getR60_deposit_excluding_number() != null) {
								R60cell1.setCellValue(record.getR60_deposit_excluding_number().doubleValue());
								R60cell1.setCellStyle(numberStyle);
							} else {
								R60cell1.setCellValue("");
								R60cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R60cell2 = row.createCell(3);
							if (record.getR60_deposit_excluding_amount() != null) {
								R60cell2.setCellValue(record.getR60_deposit_excluding_amount().doubleValue());
								R60cell2.setCellStyle(numberStyle);
							} else {
								R60cell2.setCellValue("");
								R60cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R60cell3 = row.createCell(4);
							if (record.getR60_deposit_foreign_number() != null) {
								R60cell3.setCellValue(record.getR60_deposit_foreign_number().doubleValue());
								R60cell3.setCellStyle(numberStyle);
							} else {
								R60cell3.setCellValue("");
								R60cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R60cell4 = row.createCell(5);
							if (record.getR60_deposit_foreign_amount() != null) {
								R60cell4.setCellValue(record.getR60_deposit_foreign_amount().doubleValue());
								R60cell4.setCellStyle(numberStyle);
							} else {
								R60cell4.setCellValue("");
								R60cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R60cell5 = row.createCell(8);
							if (record2.getR60_TOTAL_DEPOSIT_EXCEED() != null) {
							R60cell5.setCellValue(record2.getR60_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R60cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R60cell6 = row.createCell(9);
							if (record.getR60_total_deposit_bank() != null) {
							R60cell6.setCellValue(record.getR60_total_deposit_bank().doubleValue());
							R60cell6.setCellStyle(numberStyle);
							} else {
							R60cell6.setCellValue("");
							R60cell6.setCellStyle(textStyle);
							}

							// R63
							row = sheet.getRow(62);
							// Column C
							Cell R63cell1 = row.createCell(2);
							if (record.getR63_deposit_excluding_number() != null) {
								R63cell1.setCellValue(record.getR63_deposit_excluding_number().doubleValue());
								R63cell1.setCellStyle(numberStyle);
							} else {
								R63cell1.setCellValue("");
								R63cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R63cell2 = row.createCell(3);
							if (record.getR63_deposit_excluding_amount() != null) {
								R63cell2.setCellValue(record.getR63_deposit_excluding_amount().doubleValue());
								R63cell2.setCellStyle(numberStyle);
							} else {
								R63cell2.setCellValue("");
								R63cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R63cell3 = row.createCell(4);
							if (record.getR63_deposit_foreign_number() != null) {
								R63cell3.setCellValue(record.getR63_deposit_foreign_number().doubleValue());
								R63cell3.setCellStyle(numberStyle);
							} else {
								R63cell3.setCellValue("");
								R63cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R63cell4 = row.createCell(5);
							if (record.getR63_deposit_foreign_amount() != null) {
								R63cell4.setCellValue(record.getR63_deposit_foreign_amount().doubleValue());
								R63cell4.setCellStyle(numberStyle);
							} else {
								R63cell4.setCellValue("");
								R63cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R63cell5 = row.createCell(8);
							if (record2.getR63_TOTAL_DEPOSIT_EXCEED() != null) {
							R63cell5.setCellValue(record2.getR63_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R63cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R63cell6 = row.createCell(9);
							if (record.getR63_total_deposit_bank() != null) {
							R63cell6.setCellValue(record.getR63_total_deposit_bank().doubleValue());
							R63cell6.setCellStyle(numberStyle);
							} else {
							R63cell6.setCellValue("");
							R63cell6.setCellStyle(textStyle);
							}


							// R64
							row = sheet.getRow(63);
							// Column C
							Cell R64cell1 = row.createCell(2);
							if (record.getR64_deposit_excluding_number() != null) {
								R64cell1.setCellValue(record.getR64_deposit_excluding_number().doubleValue());
								R64cell1.setCellStyle(numberStyle);
							} else {
								R64cell1.setCellValue("");
								R64cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R64cell2 = row.createCell(3);
							if (record.getR64_deposit_excluding_amount() != null) {
								R64cell2.setCellValue(record.getR64_deposit_excluding_amount().doubleValue());
								R64cell2.setCellStyle(numberStyle);
							} else {
								R64cell2.setCellValue("");
								R64cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R64cell3 = row.createCell(4);
							if (record.getR64_deposit_foreign_number() != null) {
								R64cell3.setCellValue(record.getR64_deposit_foreign_number().doubleValue());
								R64cell3.setCellStyle(numberStyle);
							} else {
								R64cell3.setCellValue("");
								R64cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R64cell4 = row.createCell(5);
							if (record.getR64_deposit_foreign_amount() != null) {
								R64cell4.setCellValue(record.getR64_deposit_foreign_amount().doubleValue());
								R64cell4.setCellStyle(numberStyle);
							} else {
								R64cell4.setCellValue("");
								R64cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R64cell5 = row.createCell(8);
							if (record2.getR64_TOTAL_DEPOSIT_EXCEED() != null) {
							R64cell5.setCellValue(record2.getR64_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R64cell5.setCellValue("");
							
												
							// Column J
							Cell R64cell6 = row.createCell(9);
							if (record.getR64_total_deposit_bank() != null) {
							R64cell6.setCellValue(record.getR64_total_deposit_bank().doubleValue());
							R64cell6.setCellStyle(numberStyle);
							} else {
							R64cell6.setCellValue("");
							R64cell6.setCellStyle(textStyle);
							}


							// R65
							row = sheet.getRow(64);
							// Column C
							Cell R65cell1 = row.createCell(2);
							if (record.getR65_deposit_excluding_number() != null) {
								R65cell1.setCellValue(record.getR65_deposit_excluding_number().doubleValue());
								R65cell1.setCellStyle(numberStyle);
							} else {
								R65cell1.setCellValue("");
								R65cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R65cell2 = row.createCell(3);
							if (record.getR65_deposit_excluding_amount() != null) {
								R65cell2.setCellValue(record.getR65_deposit_excluding_amount().doubleValue());
								R65cell2.setCellStyle(numberStyle);
							} else {
								R65cell2.setCellValue("");
								R65cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R65cell3 = row.createCell(4);
							if (record.getR65_deposit_foreign_number() != null) {
								R65cell3.setCellValue(record.getR65_deposit_foreign_number().doubleValue());
								R65cell3.setCellStyle(numberStyle);
							} else {
								R65cell3.setCellValue("");
								R65cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R65cell4 = row.createCell(5);
							if (record.getR65_deposit_foreign_amount() != null) {
								R65cell4.setCellValue(record.getR65_deposit_foreign_amount().doubleValue());
								R65cell4.setCellStyle(numberStyle);
							} else {
								R65cell4.setCellValue("");
								R65cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R65cell5 = row.createCell(8);
							if (record2.getR65_TOTAL_DEPOSIT_EXCEED() != null) {
							R65cell5.setCellValue(record2.getR65_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R65cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R65cell6 = row.createCell(9);
							if (record.getR65_total_deposit_bank() != null) {
							R65cell6.setCellValue(record.getR65_total_deposit_bank().doubleValue());
							R65cell6.setCellStyle(numberStyle);
							} else {
							R65cell6.setCellValue("");
							R65cell6.setCellStyle(textStyle);
							}

							// R66
							row = sheet.getRow(65);
							// Column C
							Cell R66cell1 = row.createCell(2);
							if (record.getR66_deposit_excluding_number() != null) {
								R66cell1.setCellValue(record.getR66_deposit_excluding_number().doubleValue());
								R66cell1.setCellStyle(numberStyle);
							} else {
								R66cell1.setCellValue("");
								R66cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R66cell2 = row.createCell(3);
							if (record.getR66_deposit_excluding_amount() != null) {
								R66cell2.setCellValue(record.getR66_deposit_excluding_amount().doubleValue());
								R66cell2.setCellStyle(numberStyle);
							} else {
								R66cell2.setCellValue("");
								R66cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R66cell3 = row.createCell(4);
							if (record.getR66_deposit_foreign_number() != null) {
								R66cell3.setCellValue(record.getR66_deposit_foreign_number().doubleValue());
								R66cell3.setCellStyle(numberStyle);
							} else {
								R66cell3.setCellValue("");
								R66cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R66cell4 = row.createCell(5);
							if (record.getR66_deposit_foreign_amount() != null) {
								R66cell4.setCellValue(record.getR66_deposit_foreign_amount().doubleValue());
								R66cell4.setCellStyle(numberStyle);
							} else {
								R66cell4.setCellValue("");
								R66cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R66cell5 = row.createCell(8);
							if (record2.getR66_TOTAL_DEPOSIT_EXCEED() != null) {
							R66cell5.setCellValue(record2.getR66_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R66cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R66cell6 = row.createCell(9);
							if (record.getR66_total_deposit_bank() != null) {
							R66cell6.setCellValue(record.getR66_total_deposit_bank().doubleValue());
							R66cell6.setCellStyle(numberStyle);
							} else {
							R66cell6.setCellValue("");
							R66cell6.setCellStyle(textStyle);
							}


							// R67
							row = sheet.getRow(66);
							// Column C
							Cell R67cell1 = row.createCell(2);
							if (record.getR67_deposit_excluding_number() != null) {
								R67cell1.setCellValue(record.getR67_deposit_excluding_number().doubleValue());
								R67cell1.setCellStyle(numberStyle);
							} else {
								R67cell1.setCellValue("");
								R67cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R67cell2 = row.createCell(3);
							if (record.getR67_deposit_excluding_amount() != null) {
								R67cell2.setCellValue(record.getR67_deposit_excluding_amount().doubleValue());
								R67cell2.setCellStyle(numberStyle);
							} else {
								R67cell2.setCellValue("");
								R67cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R67cell3 = row.createCell(4);
							if (record.getR67_deposit_foreign_number() != null) {
								R67cell3.setCellValue(record.getR67_deposit_foreign_number().doubleValue());
								R67cell3.setCellStyle(numberStyle);
							} else {
								R67cell3.setCellValue("");
								R67cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R67cell4 = row.createCell(5);
							if (record.getR67_deposit_foreign_amount() != null) {
								R67cell4.setCellValue(record.getR67_deposit_foreign_amount().doubleValue());
								R67cell4.setCellStyle(numberStyle);
							} else {
								R67cell4.setCellValue("");
								R67cell4.setCellStyle(textStyle);
							}

							
							// Column I
							Cell R67cell5 = row.createCell(8);
							if (record2.getR67_TOTAL_DEPOSIT_EXCEED() != null) {
							R67cell5.setCellValue(record2.getR67_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R67cell5.setCellValue("");
							}
												
							// Column J
							Cell R67cell6 = row.createCell(9);
							if (record.getR67_total_deposit_bank() != null) {
							R67cell6.setCellValue(record.getR67_total_deposit_bank().doubleValue());
							R67cell6.setCellStyle(numberStyle);
							} else {
							R67cell6.setCellValue("");
							R67cell6.setCellStyle(textStyle);
							}

							// R68
							row = sheet.getRow(67);
							// Column C
							Cell R68cell1 = row.createCell(2);
							if (record.getR68_deposit_excluding_number() != null) {
								R68cell1.setCellValue(record.getR68_deposit_excluding_number().doubleValue());
								R68cell1.setCellStyle(numberStyle);
							} else {
								R68cell1.setCellValue("");
								R68cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R68cell2 = row.createCell(3);
							if (record.getR68_deposit_excluding_amount() != null) {
								R68cell2.setCellValue(record.getR68_deposit_excluding_amount().doubleValue());
								R68cell2.setCellStyle(numberStyle);
							} else {
								R68cell2.setCellValue("");
								R68cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R68cell3 = row.createCell(4);
							if (record.getR68_deposit_foreign_number() != null) {
								R68cell3.setCellValue(record.getR68_deposit_foreign_number().doubleValue());
								R68cell3.setCellStyle(numberStyle);
							} else {
								R68cell3.setCellValue("");
								R68cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R68cell4 = row.createCell(5);
							if (record.getR68_deposit_foreign_amount() != null) {
								R68cell4.setCellValue(record.getR68_deposit_foreign_amount().doubleValue());
								R68cell4.setCellStyle(numberStyle);
							} else {
								R68cell4.setCellValue("");
								R68cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R68cell5 = row.createCell(8);
							if (record2.getR68_TOTAL_DEPOSIT_EXCEED() != null) {
							R68cell5.setCellValue(record2.getR68_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R68cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R68cell6 = row.createCell(9);
							if (record.getR68_total_deposit_bank() != null) {
							R68cell6.setCellValue(record.getR68_total_deposit_bank().doubleValue());
							R68cell6.setCellStyle(numberStyle);
							} else {
							R68cell6.setCellValue("");
							R68cell6.setCellStyle(textStyle);
							}

							// R71
							row = sheet.getRow(70);
							// Column C
							Cell R71cell1 = row.createCell(2);
							if (record.getR71_deposit_excluding_number() != null) {
								R71cell1.setCellValue(record.getR71_deposit_excluding_number().doubleValue());
								R71cell1.setCellStyle(numberStyle);
							} else {
								R71cell1.setCellValue("");
								R71cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R71cell2 = row.createCell(3);
							if (record.getR71_deposit_excluding_amount() != null) {
								R71cell2.setCellValue(record.getR71_deposit_excluding_amount().doubleValue());
								R71cell2.setCellStyle(numberStyle);
							} else {
								R71cell2.setCellValue("");
								R71cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R71cell3 = row.createCell(4);
							if (record.getR71_deposit_foreign_number() != null) {
								R71cell3.setCellValue(record.getR71_deposit_foreign_number().doubleValue());
								R71cell3.setCellStyle(numberStyle);
							} else {
								R71cell3.setCellValue("");
								R71cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R71cell4 = row.createCell(5);
							if (record.getR71_deposit_foreign_amount() != null) {
								R71cell4.setCellValue(record.getR71_deposit_foreign_amount().doubleValue());
								R71cell4.setCellStyle(numberStyle);
							} else {
								R71cell4.setCellValue("");
								R71cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R71cell5 = row.createCell(8);
							if (record2.getR71_TOTAL_DEPOSIT_EXCEED() != null) {
							R71cell5.setCellValue(record2.getR71_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R71cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R71cell6 = row.createCell(9);
							if (record.getR71_total_deposit_bank() != null) {
							R71cell6.setCellValue(record.getR71_total_deposit_bank().doubleValue());
							R71cell6.setCellStyle(numberStyle);
							} else {
							R71cell6.setCellValue("");
							R71cell6.setCellStyle(textStyle);
							}

							// R72
							row = sheet.getRow(71);
							// Column C
							Cell R72cell1 = row.createCell(2);
							if (record.getR72_deposit_excluding_number() != null) {
								R72cell1.setCellValue(record.getR72_deposit_excluding_number().doubleValue());
								R72cell1.setCellStyle(numberStyle);
							} else {
								R72cell1.setCellValue("");
								R72cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R72cell2 = row.createCell(3);
							if (record.getR72_deposit_excluding_amount() != null) {
								R72cell2.setCellValue(record.getR72_deposit_excluding_amount().doubleValue());
								R72cell2.setCellStyle(numberStyle);
							} else {
								R72cell2.setCellValue("");
								R72cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R72cell3 = row.createCell(4);
							if (record.getR72_deposit_foreign_number() != null) {
								R72cell3.setCellValue(record.getR72_deposit_foreign_number().doubleValue());
								R72cell3.setCellStyle(numberStyle);
							} else {
								R72cell3.setCellValue("");
								R72cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R72cell4 = row.createCell(5);
							if (record.getR72_deposit_foreign_amount() != null) {
								R72cell4.setCellValue(record.getR72_deposit_foreign_amount().doubleValue());
								R72cell4.setCellStyle(numberStyle);
							} else {
								R72cell4.setCellValue("");
								R72cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R72cell5 = row.createCell(8);
							if (record2.getR72_TOTAL_DEPOSIT_EXCEED() != null) {
							R72cell5.setCellValue(record2.getR72_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R72cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R72cell6 = row.createCell(9);
							if (record.getR72_total_deposit_bank() != null) {
							R72cell6.setCellValue(record.getR72_total_deposit_bank().doubleValue());
							R72cell6.setCellStyle(numberStyle);
							} else {
							R72cell6.setCellValue("");
							R72cell6.setCellStyle(textStyle);
							}


							// R73
							row = sheet.getRow(72);
							// Column C
							Cell R73cell1 = row.createCell(2);
							if (record.getR73_deposit_excluding_number() != null) {
								R73cell1.setCellValue(record.getR73_deposit_excluding_number().doubleValue());
								R73cell1.setCellStyle(numberStyle);
							} else {
								R73cell1.setCellValue("");
								R73cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R73cell2 = row.createCell(3);
							if (record.getR73_deposit_excluding_amount() != null) {
								R73cell2.setCellValue(record.getR73_deposit_excluding_amount().doubleValue());
								R73cell2.setCellStyle(numberStyle);
							} else {
								R73cell2.setCellValue("");
								R73cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R73cell3 = row.createCell(4);
							if (record.getR73_deposit_foreign_number() != null) {
								R73cell3.setCellValue(record.getR73_deposit_foreign_number().doubleValue());
								R73cell3.setCellStyle(numberStyle);
							} else {
								R73cell3.setCellValue("");
								R73cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R73cell4 = row.createCell(5);
							if (record.getR73_deposit_foreign_amount() != null) {
								R73cell4.setCellValue(record.getR73_deposit_foreign_amount().doubleValue());
								R73cell4.setCellStyle(numberStyle);
							} else {
								R73cell4.setCellValue("");
								R73cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R73cell5 = row.createCell(8);
							if (record2.getR73_TOTAL_DEPOSIT_EXCEED() != null) {
							R73cell5.setCellValue(record2.getR73_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R73cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R73cell6 = row.createCell(9);
							if (record.getR73_total_deposit_bank() != null) {
							R73cell6.setCellValue(record.getR73_total_deposit_bank().doubleValue());
							R73cell6.setCellStyle(numberStyle);
							} else {
							R73cell6.setCellValue("");
							R73cell6.setCellStyle(textStyle);
							}


							// R74
							row = sheet.getRow(73);
							// Column C
							Cell R74cell1 = row.createCell(2);
							if (record.getR74_deposit_excluding_number() != null) {
								R74cell1.setCellValue(record.getR74_deposit_excluding_number().doubleValue());
								R74cell1.setCellStyle(numberStyle);
							} else {
								R74cell1.setCellValue("");
								R74cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R74cell2 = row.createCell(3);
							if (record.getR74_deposit_excluding_amount() != null) {
								R74cell2.setCellValue(record.getR74_deposit_excluding_amount().doubleValue());
								R74cell2.setCellStyle(numberStyle);
							} else {
								R74cell2.setCellValue("");
								R74cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R74cell3 = row.createCell(4);
							if (record.getR74_deposit_foreign_number() != null) {
								R74cell3.setCellValue(record.getR74_deposit_foreign_number().doubleValue());
								R74cell3.setCellStyle(numberStyle);
							} else {
								R74cell3.setCellValue("");
								R74cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R74cell4 = row.createCell(5);
							if (record.getR74_deposit_foreign_amount() != null) {
								R74cell4.setCellValue(record.getR74_deposit_foreign_amount().doubleValue());
								R74cell4.setCellStyle(numberStyle);
							} else {
								R74cell4.setCellValue("");
								R74cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R74cell5 = row.createCell(8);
							if (record2.getR74_TOTAL_DEPOSIT_EXCEED() != null) {
							R74cell5.setCellValue(record2.getR74_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R74cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R74cell6 = row.createCell(9);
							if (record.getR74_total_deposit_bank() != null) {
							R74cell6.setCellValue(record.getR74_total_deposit_bank().doubleValue());
							R74cell6.setCellStyle(numberStyle);
							} else {
							R74cell6.setCellValue("");
							R74cell6.setCellStyle(textStyle);
							}



							// R75
							row = sheet.getRow(74);
							// Column C
							Cell R75cell1 = row.createCell(2);
							if (record.getR75_deposit_excluding_number() != null) {
								R75cell1.setCellValue(record.getR75_deposit_excluding_number().doubleValue());
								R75cell1.setCellStyle(numberStyle);
							} else {
								R75cell1.setCellValue("");
								R75cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R75cell2 = row.createCell(3);
							if (record.getR75_deposit_excluding_amount() != null) {
								R75cell2.setCellValue(record.getR75_deposit_excluding_amount().doubleValue());
								R75cell2.setCellStyle(numberStyle);
							} else {
								R75cell2.setCellValue("");
								R75cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R75cell3 = row.createCell(4);
							if (record.getR75_deposit_foreign_number() != null) {
								R75cell3.setCellValue(record.getR75_deposit_foreign_number().doubleValue());
								R75cell3.setCellStyle(numberStyle);
							} else {
								R75cell3.setCellValue("");
								R75cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R75cell4 = row.createCell(5);
							if (record.getR75_deposit_foreign_amount() != null) {
								R75cell4.setCellValue(record.getR75_deposit_foreign_amount().doubleValue());
								R75cell4.setCellStyle(numberStyle);
							} else {
								R75cell4.setCellValue("");
								R75cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R75cell5 = row.createCell(8);
							if (record2.getR75_TOTAL_DEPOSIT_EXCEED() != null) {
							R75cell5.setCellValue(record2.getR75_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R75cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R75cell6 = row.createCell(9);
							if (record.getR75_total_deposit_bank() != null) {
							R75cell6.setCellValue(record.getR75_total_deposit_bank().doubleValue());
							R75cell6.setCellStyle(numberStyle);
							} else {
							R75cell6.setCellValue("");
							R75cell6.setCellStyle(textStyle);
							}


							// R76
							row = sheet.getRow(75);
							// Column C
							Cell R76cell1 = row.createCell(2);
							if (record.getR76_deposit_excluding_number() != null) {
								R76cell1.setCellValue(record.getR76_deposit_excluding_number().doubleValue());
								R76cell1.setCellStyle(numberStyle);
							} else {
								R76cell1.setCellValue("");
								R76cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R76cell2 = row.createCell(3);
							if (record.getR76_deposit_excluding_amount() != null) {
								R76cell2.setCellValue(record.getR76_deposit_excluding_amount().doubleValue());
								R76cell2.setCellStyle(numberStyle);
							} else {
								R76cell2.setCellValue("");
								R76cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R76cell3 = row.createCell(4);
							if (record.getR76_deposit_foreign_number() != null) {
								R76cell3.setCellValue(record.getR76_deposit_foreign_number().doubleValue());
								R76cell3.setCellStyle(numberStyle);
							} else {
								R76cell3.setCellValue("");
								R76cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R76cell4 = row.createCell(5);
							if (record.getR76_deposit_foreign_amount() != null) {
								R76cell4.setCellValue(record.getR76_deposit_foreign_amount().doubleValue());
								R76cell4.setCellStyle(numberStyle);
							} else {
								R76cell4.setCellValue("");
								R76cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R76cell5 = row.createCell(8);
							if (record2.getR76_TOTAL_DEPOSIT_EXCEED() != null) {
							R76cell5.setCellValue(record2.getR76_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R76cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R76cell6 = row.createCell(9);
							if (record.getR76_total_deposit_bank() != null) {
							R76cell6.setCellValue(record.getR76_total_deposit_bank().doubleValue());
							R76cell6.setCellStyle(numberStyle);
							} else {
							R76cell6.setCellValue("");
							R76cell6.setCellStyle(textStyle);
							}


							// R79
							row = sheet.getRow(78);
							// Column C
							Cell R79cell1 = row.createCell(2);
							if (record.getR79_deposit_excluding_number() != null) {
								R79cell1.setCellValue(record.getR79_deposit_excluding_number().doubleValue());
								R79cell1.setCellStyle(numberStyle);
							} else {
								R79cell1.setCellValue("");
								R79cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R79cell2 = row.createCell(3);
							if (record.getR79_deposit_excluding_amount() != null) {
								R79cell2.setCellValue(record.getR79_deposit_excluding_amount().doubleValue());
								R79cell2.setCellStyle(numberStyle);
							} else {
								R79cell2.setCellValue("");
								R79cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R79cell3 = row.createCell(4);
							if (record.getR79_deposit_foreign_number() != null) {
								R79cell3.setCellValue(record.getR79_deposit_foreign_number().doubleValue());
								R79cell3.setCellStyle(numberStyle);
							} else {
								R79cell3.setCellValue("");
								R79cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R79cell4 = row.createCell(5);
							if (record.getR79_deposit_foreign_amount() != null) {
								R79cell4.setCellValue(record.getR79_deposit_foreign_amount().doubleValue());
								R79cell4.setCellStyle(numberStyle);
							} else {
								R79cell4.setCellValue("");
								R79cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R79cell5 = row.createCell(8);
							if (record2.getR79_TOTAL_DEPOSIT_EXCEED() != null) {
							R79cell5.setCellValue(record2.getR79_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R79cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R79cell6 = row.createCell(9);
							if (record.getR79_total_deposit_bank() != null) {
							R79cell6.setCellValue(record.getR79_total_deposit_bank().doubleValue());
							R79cell6.setCellStyle(numberStyle);
							} else {
							R79cell6.setCellValue("");
							R79cell6.setCellStyle(textStyle);
							}

							// R80
							row = sheet.getRow(79);
							// Column C
							Cell R80cell1 = row.createCell(2);
							if (record.getR80_deposit_excluding_number() != null) {
								R80cell1.setCellValue(record.getR80_deposit_excluding_number().doubleValue());
								R80cell1.setCellStyle(numberStyle);
							} else {
								R80cell1.setCellValue("");
								R80cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R80cell2 = row.createCell(3);
							if (record.getR80_deposit_excluding_amount() != null) {
								R80cell2.setCellValue(record.getR80_deposit_excluding_amount().doubleValue());
								R80cell2.setCellStyle(numberStyle);
							} else {
								R80cell2.setCellValue("");
								R80cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R80cell3 = row.createCell(4);
							if (record.getR80_deposit_foreign_number() != null) {
								R80cell3.setCellValue(record.getR80_deposit_foreign_number().doubleValue());
								R80cell3.setCellStyle(numberStyle);
							} else {
								R80cell3.setCellValue("");
								R80cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R80cell4 = row.createCell(5);
							if (record.getR80_deposit_foreign_amount() != null) {
								R80cell4.setCellValue(record.getR80_deposit_foreign_amount().doubleValue());
								R80cell4.setCellStyle(numberStyle);
							} else {
								R80cell4.setCellValue("");
								R80cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R80cell5 = row.createCell(8);
							if (record2.getR80_TOTAL_DEPOSIT_EXCEED() != null) {
							R80cell5.setCellValue(record2.getR80_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R80cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R80cell6 = row.createCell(9);
							if (record.getR80_total_deposit_bank() != null) {
							R80cell6.setCellValue(record.getR80_total_deposit_bank().doubleValue());
							R80cell6.setCellStyle(numberStyle);
							} else {
							R80cell6.setCellValue("");
							R80cell6.setCellStyle(textStyle);
							}


							// R81
							row = sheet.getRow(80);
							// Column C
							Cell R81cell1 = row.createCell(2);
							if (record.getR81_deposit_excluding_number() != null) {
								R81cell1.setCellValue(record.getR81_deposit_excluding_number().doubleValue());
								R81cell1.setCellStyle(numberStyle);
							} else {
								R81cell1.setCellValue("");
								R81cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R81cell2 = row.createCell(3);
							if (record.getR81_deposit_excluding_amount() != null) {
								R81cell2.setCellValue(record.getR81_deposit_excluding_amount().doubleValue());
								R81cell2.setCellStyle(numberStyle);
							} else {
								R81cell2.setCellValue("");
								R81cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R81cell3 = row.createCell(4);
							if (record.getR81_deposit_foreign_number() != null) {
								R81cell3.setCellValue(record.getR81_deposit_foreign_number().doubleValue());
								R81cell3.setCellStyle(numberStyle);
							} else {
								R81cell3.setCellValue("");
								R81cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R81cell4 = row.createCell(5);
							if (record.getR81_deposit_foreign_amount() != null) {
								R81cell4.setCellValue(record.getR81_deposit_foreign_amount().doubleValue());
								R81cell4.setCellStyle(numberStyle);
							} else {
								R81cell4.setCellValue("");
								R81cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R81cell5 = row.createCell(8);
							if (record2.getR81_TOTAL_DEPOSIT_EXCEED() != null) {
							R81cell5.setCellValue(record2.getR81_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R81cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R81cell6 = row.createCell(9);
							if (record.getR81_total_deposit_bank() != null) {
							R81cell6.setCellValue(record.getR81_total_deposit_bank().doubleValue());
							R81cell6.setCellStyle(numberStyle);
							} else {
							R81cell6.setCellValue("");
							R81cell6.setCellStyle(textStyle);
							}


							// R82
							row = sheet.getRow(81);
							// Column C
							Cell R82cell1 = row.createCell(2);
							if (record.getR82_deposit_excluding_number() != null) {
								R82cell1.setCellValue(record.getR82_deposit_excluding_number().doubleValue());
								R82cell1.setCellStyle(numberStyle);
							} else {
								R82cell1.setCellValue("");
								R82cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R82cell2 = row.createCell(3);
							if (record.getR82_deposit_excluding_amount() != null) {
								R82cell2.setCellValue(record.getR82_deposit_excluding_amount().doubleValue());
								R82cell2.setCellStyle(numberStyle);
							} else {
								R82cell2.setCellValue("");
								R82cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R82cell3 = row.createCell(4);
							if (record.getR82_deposit_foreign_number() != null) {
								R82cell3.setCellValue(record.getR82_deposit_foreign_number().doubleValue());
								R82cell3.setCellStyle(numberStyle);
							} else {
								R82cell3.setCellValue("");
								R82cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R82cell4 = row.createCell(5);
							if (record.getR82_deposit_foreign_amount() != null) {
								R82cell4.setCellValue(record.getR82_deposit_foreign_amount().doubleValue());
								R82cell4.setCellStyle(numberStyle);
							} else {
								R82cell4.setCellValue("");
								R82cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R82cell5 = row.createCell(8);
							if (record2.getR82_TOTAL_DEPOSIT_EXCEED() != null) {
							R82cell5.setCellValue(record2.getR82_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R82cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R82cell6 = row.createCell(9);
							if (record.getR82_total_deposit_bank() != null) {
							R82cell6.setCellValue(record.getR82_total_deposit_bank().doubleValue());
							R82cell6.setCellStyle(numberStyle);
							} else {
							R82cell6.setCellValue("");
							R82cell6.setCellStyle(textStyle);
							}


							// R83
							row = sheet.getRow(82);
							// Column C
							Cell R83cell1 = row.createCell(2);
							if (record.getR83_deposit_excluding_number() != null) {
								R83cell1.setCellValue(record.getR83_deposit_excluding_number().doubleValue());
								R83cell1.setCellStyle(numberStyle);
							} else {
								R83cell1.setCellValue("");
								R83cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R83cell2 = row.createCell(3);
							if (record.getR83_deposit_excluding_amount() != null) {
								R83cell2.setCellValue(record.getR83_deposit_excluding_amount().doubleValue());
								R83cell2.setCellStyle(numberStyle);
							} else {
								R83cell2.setCellValue("");
								R83cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R83cell3 = row.createCell(4);
							if (record.getR83_deposit_foreign_number() != null) {
								R83cell3.setCellValue(record.getR83_deposit_foreign_number().doubleValue());
								R83cell3.setCellStyle(numberStyle);
							} else {
								R83cell3.setCellValue("");
								R83cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R83cell4 = row.createCell(5);
							if (record.getR83_deposit_foreign_amount() != null) {
								R83cell4.setCellValue(record.getR83_deposit_foreign_amount().doubleValue());
								R83cell4.setCellStyle(numberStyle);
							} else {
								R83cell4.setCellValue("");
								R83cell4.setCellStyle(textStyle);
							}
							// Column I
							Cell R83cell5 = row.createCell(8);
							if (record2.getR83_TOTAL_DEPOSIT_EXCEED() != null) {
							R83cell5.setCellValue(record2.getR83_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R83cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R83cell6 = row.createCell(9);
							if (record.getR83_total_deposit_bank() != null) {
							R83cell6.setCellValue(record.getR83_total_deposit_bank().doubleValue());
							R83cell6.setCellStyle(numberStyle);
							} else {
							R83cell6.setCellValue("");
							R83cell6.setCellStyle(textStyle);
							}

							

							// R84
							row = sheet.getRow(83);
							// Column C
							Cell R84cell1 = row.createCell(2);
							if (record.getR84_deposit_excluding_number() != null) {
								R84cell1.setCellValue(record.getR84_deposit_excluding_number().doubleValue());
								R84cell1.setCellStyle(numberStyle);
							} else {
								R84cell1.setCellValue("");
								R84cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R84cell2 = row.createCell(3);
							if (record.getR84_deposit_excluding_amount() != null) {
								R84cell2.setCellValue(record.getR84_deposit_excluding_amount().doubleValue());
								R84cell2.setCellStyle(numberStyle);
							} else {
								R84cell2.setCellValue("");
								R84cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R84cell3 = row.createCell(4);
							if (record.getR84_deposit_foreign_number() != null) {
								R84cell3.setCellValue(record.getR84_deposit_foreign_number().doubleValue());
								R84cell3.setCellStyle(numberStyle);
							} else {
								R84cell3.setCellValue("");
								R84cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R84cell4 = row.createCell(5);
							if (record.getR84_deposit_foreign_amount() != null) {
								R84cell4.setCellValue(record.getR84_deposit_foreign_amount().doubleValue());
								R84cell4.setCellStyle(numberStyle);
							} else {
								R84cell4.setCellValue("");
								R84cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R84cell5 = row.createCell(8);
							if (record2.getR84_TOTAL_DEPOSIT_EXCEED() != null) {
							R84cell5.setCellValue(record2.getR84_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R84cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R84cell6 = row.createCell(9);
							if (record.getR84_total_deposit_bank() != null) {
							R84cell6.setCellValue(record.getR84_total_deposit_bank().doubleValue());
							R84cell6.setCellStyle(numberStyle);
							} else {
							R84cell6.setCellValue("");
							R84cell6.setCellStyle(textStyle);
							}


							// R87
							row = sheet.getRow(86);
							// Column C
							Cell R87cell1 = row.createCell(2);
							if (record1.getR87_deposit_excluding_number() != null) {
								R87cell1.setCellValue(record1.getR87_deposit_excluding_number().doubleValue());
								R87cell1.setCellStyle(numberStyle);
							} else {
								R87cell1.setCellValue("");
								R87cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R87cell2 = row.createCell(3);
							if (record1.getR87_deposit_excluding_amount() != null) {
								R87cell2.setCellValue(record1.getR87_deposit_excluding_amount().doubleValue());
								R87cell2.setCellStyle(numberStyle);
							} else {
								R87cell2.setCellValue("");
								R87cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R87cell3 = row.createCell(4);
							if (record1.getR87_deposit_foreign_number() != null) {
								R87cell3.setCellValue(record1.getR87_deposit_foreign_number().doubleValue());
								R87cell3.setCellStyle(numberStyle);
							} else {
								R87cell3.setCellValue("");
								R87cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R87cell4 = row.createCell(5);
							if (record1.getR87_deposit_foreign_amount() != null) {
								R87cell4.setCellValue(record1.getR87_deposit_foreign_amount().doubleValue());
								R87cell4.setCellStyle(numberStyle);
							} else {
								R87cell4.setCellValue("");
								R87cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R87cell5 = row.createCell(8);
							if (record2.getR87_TOTAL_DEPOSIT_EXCEED() != null) {
							R87cell5.setCellValue(record2.getR87_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R87cell5.setCellValue("");
							}
												
							// Column J
							Cell R87cell6 = row.createCell(9);
							if (record1.getR87_total_deposit_bank() != null) {
							R87cell6.setCellValue(record1.getR87_total_deposit_bank().doubleValue());
							R87cell6.setCellStyle(numberStyle);
							} else {
							R87cell6.setCellValue("");
							R87cell6.setCellStyle(textStyle);
							}

							// R88
							row = sheet.getRow(87);
							// Column C
							Cell R88cell1 = row.createCell(2);
							if (record1.getR88_deposit_excluding_number() != null) {
								R88cell1.setCellValue(record1.getR88_deposit_excluding_number().doubleValue());
								R88cell1.setCellStyle(numberStyle);
							} else {
								R88cell1.setCellValue("");
								R88cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R88cell2 = row.createCell(3);
							if (record1.getR88_deposit_excluding_amount() != null) {
								R88cell2.setCellValue(record1.getR88_deposit_excluding_amount().doubleValue());
								R88cell2.setCellStyle(numberStyle);
							} else {
								R88cell2.setCellValue("");
								R88cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R88cell3 = row.createCell(4);
							if (record1.getR88_deposit_foreign_number() != null) {
								R88cell3.setCellValue(record1.getR88_deposit_foreign_number().doubleValue());
								R88cell3.setCellStyle(numberStyle);
							} else {
								R88cell3.setCellValue("");
								R88cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R88cell4 = row.createCell(5);
							if (record1.getR88_deposit_foreign_amount() != null) {
								R88cell4.setCellValue(record1.getR88_deposit_foreign_amount().doubleValue());
								R88cell4.setCellStyle(numberStyle);
							} else {
								R88cell4.setCellValue("");
								R88cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R88cell5 = row.createCell(8);
							if (record2.getR88_TOTAL_DEPOSIT_EXCEED() != null) {
							R88cell5.setCellValue(record2.getR88_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R88cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R88cell6 = row.createCell(9);
							if (record1.getR88_total_deposit_bank() != null) {
							R88cell6.setCellValue(record1.getR88_total_deposit_bank().doubleValue());
							R88cell6.setCellStyle(numberStyle);
							} else {
							R88cell6.setCellValue("");
							R88cell6.setCellStyle(textStyle);
							}

							// R89
							row = sheet.getRow(88);
							// Column C
							Cell R89cell1 = row.createCell(2);
							if (record1.getR89_deposit_excluding_number() != null) {
								R89cell1.setCellValue(record1.getR89_deposit_excluding_number().doubleValue());
								R89cell1.setCellStyle(numberStyle);
							} else {
								R89cell1.setCellValue("");
								R89cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R89cell2 = row.createCell(3);
							if (record1.getR89_deposit_excluding_amount() != null) {
								R89cell2.setCellValue(record1.getR89_deposit_excluding_amount().doubleValue());
								R89cell2.setCellStyle(numberStyle);
							} else {
								R89cell2.setCellValue("");
								R89cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R89cell3 = row.createCell(4);
							if (record1.getR89_deposit_foreign_number() != null) {
								R89cell3.setCellValue(record1.getR89_deposit_foreign_number().doubleValue());
								R89cell3.setCellStyle(numberStyle);
							} else {
								R89cell3.setCellValue("");
								R89cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R89cell4 = row.createCell(5);
							if (record1.getR89_deposit_foreign_amount() != null) {
								R89cell4.setCellValue(record1.getR89_deposit_foreign_amount().doubleValue());
								R89cell4.setCellStyle(numberStyle);
							} else {
								R89cell4.setCellValue("");
								R89cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R89cell5 = row.createCell(8);
							if (record2.getR89_TOTAL_DEPOSIT_EXCEED() != null) {
							R89cell5.setCellValue(record2.getR89_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R89cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R89cell6 = row.createCell(9);
							if (record1.getR89_total_deposit_bank() != null) {
							R89cell6.setCellValue(record1.getR89_total_deposit_bank().doubleValue());
							R89cell6.setCellStyle(numberStyle);
							} else {
							R89cell6.setCellValue("");
							R89cell6.setCellStyle(textStyle);
							}


							// R90
							row = sheet.getRow(89);
							// Column C
							Cell R90cell1 = row.createCell(2);
							if (record1.getR90_deposit_excluding_number() != null) {
								R90cell1.setCellValue(record1.getR90_deposit_excluding_number().doubleValue());
								R90cell1.setCellStyle(numberStyle);
							} else {
								R90cell1.setCellValue("");
								R90cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R90cell2 = row.createCell(3);
							if (record1.getR90_deposit_excluding_amount() != null) {
								R90cell2.setCellValue(record1.getR90_deposit_excluding_amount().doubleValue());
								R90cell2.setCellStyle(numberStyle);
							} else {
								R90cell2.setCellValue("");
								R90cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R90cell3 = row.createCell(4);
							if (record1.getR90_deposit_foreign_number() != null) {
								R90cell3.setCellValue(record1.getR90_deposit_foreign_number().doubleValue());
								R90cell3.setCellStyle(numberStyle);
							} else {
								R90cell3.setCellValue("");
								R90cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R90cell4 = row.createCell(5);
							if (record1.getR90_deposit_foreign_amount() != null) {
								R90cell4.setCellValue(record1.getR90_deposit_foreign_amount().doubleValue());
								R90cell4.setCellStyle(numberStyle);
							} else {
								R90cell4.setCellValue("");
								R90cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R90cell5 = row.createCell(8);
							if (record2.getR90_TOTAL_DEPOSIT_EXCEED() != null) {
							R90cell5.setCellValue(record2.getR90_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R90cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R90cell6 = row.createCell(9);
							if (record1.getR90_total_deposit_bank() != null) {
							R90cell6.setCellValue(record1.getR90_total_deposit_bank().doubleValue());
							R90cell6.setCellStyle(numberStyle);
							} else {
							R90cell6.setCellValue("");
							R90cell6.setCellStyle(textStyle);
							}

							// R91
							row = sheet.getRow(90);
							// Column C
							Cell R91cell1 = row.createCell(2);
							if (record1.getR91_deposit_excluding_number() != null) {
								R91cell1.setCellValue(record1.getR91_deposit_excluding_number().doubleValue());
								R91cell1.setCellStyle(numberStyle);
							} else {
								R91cell1.setCellValue("");
								R91cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R91cell2 = row.createCell(3);
							if (record1.getR91_deposit_excluding_amount() != null) {
								R91cell2.setCellValue(record1.getR91_deposit_excluding_amount().doubleValue());
								R91cell2.setCellStyle(numberStyle);
							} else {
								R91cell2.setCellValue("");
								R91cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R91cell3 = row.createCell(4);
							if (record1.getR91_deposit_foreign_number() != null) {
								R91cell3.setCellValue(record1.getR91_deposit_foreign_number().doubleValue());
								R91cell3.setCellStyle(numberStyle);
							} else {
								R91cell3.setCellValue("");
								R91cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R91cell4 = row.createCell(5);
							if (record1.getR91_deposit_foreign_amount() != null) {
								R91cell4.setCellValue(record1.getR91_deposit_foreign_amount().doubleValue());
								R91cell4.setCellStyle(numberStyle);
							} else {
								R91cell4.setCellValue("");
								R91cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R91cell5 = row.createCell(8);
							if (record2.getR91_TOTAL_DEPOSIT_EXCEED() != null) {
							R91cell5.setCellValue(record2.getR91_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R91cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R91cell6 = row.createCell(9);
							if (record1.getR91_total_deposit_bank() != null) {
							R91cell6.setCellValue(record1.getR91_total_deposit_bank().doubleValue());
							R91cell6.setCellStyle(numberStyle);
							} else {
							R91cell6.setCellValue("");
							R91cell6.setCellStyle(textStyle);
							}

							// R92
							row = sheet.getRow(91);
							// Column C
							Cell R92cell1 = row.createCell(2);
							if (record1.getR92_deposit_excluding_number() != null) {
								R92cell1.setCellValue(record1.getR92_deposit_excluding_number().doubleValue());
								R92cell1.setCellStyle(numberStyle);
							} else {
								R92cell1.setCellValue("");
								R92cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R92cell2 = row.createCell(3);
							if (record1.getR92_deposit_excluding_amount() != null) {
								R92cell2.setCellValue(record1.getR92_deposit_excluding_amount().doubleValue());
								R92cell2.setCellStyle(numberStyle);
							} else {
								R92cell2.setCellValue("");
								R92cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R92cell3 = row.createCell(4);
							if (record1.getR92_deposit_foreign_number() != null) {
								R92cell3.setCellValue(record1.getR92_deposit_foreign_number().doubleValue());
								R92cell3.setCellStyle(numberStyle);
							} else {
								R92cell3.setCellValue("");
								R92cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R92cell4 = row.createCell(5);
							if (record1.getR92_deposit_foreign_amount() != null) {
								R92cell4.setCellValue(record1.getR92_deposit_foreign_amount().doubleValue());
								R92cell4.setCellStyle(numberStyle);
							} else {
								R92cell4.setCellValue("");
								R92cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R92cell5 = row.createCell(8);
							if (record2.getR92_TOTAL_DEPOSIT_EXCEED() != null) {
							R92cell5.setCellValue(record2.getR92_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R92cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R92cell6 = row.createCell(9);
							if (record1.getR92_total_deposit_bank() != null) {
							R92cell6.setCellValue(record1.getR92_total_deposit_bank().doubleValue());
							R92cell6.setCellStyle(numberStyle);
							} else {
							R92cell6.setCellValue("");
							R92cell6.setCellStyle(textStyle);
							}

							// R95
							row = sheet.getRow(94);
							// Column C
							Cell R95cell1 = row.createCell(2);
							if (record1.getR95_deposit_excluding_number() != null) {
								R95cell1.setCellValue(record1.getR95_deposit_excluding_number().doubleValue());
								R95cell1.setCellStyle(numberStyle);
							} else {
								R95cell1.setCellValue("");
								R95cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R95cell2 = row.createCell(3);
							if (record1.getR95_deposit_excluding_amount() != null) {
								R95cell2.setCellValue(record1.getR95_deposit_excluding_amount().doubleValue());
								R95cell2.setCellStyle(numberStyle);
							} else {
								R95cell2.setCellValue("");
								R95cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R95cell3 = row.createCell(4);
							if (record1.getR95_deposit_foreign_number() != null) {
								R95cell3.setCellValue(record1.getR95_deposit_foreign_number().doubleValue());
								R95cell3.setCellStyle(numberStyle);
							} else {
								R95cell3.setCellValue("");
								R95cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R95cell4 = row.createCell(5);
							if (record1.getR95_deposit_foreign_amount() != null) {
								R95cell4.setCellValue(record1.getR95_deposit_foreign_amount().doubleValue());
								R95cell4.setCellStyle(numberStyle);
							} else {
								R95cell4.setCellValue("");
								R95cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R95cell5 = row.createCell(8);
							if (record2.getR95_TOTAL_DEPOSIT_EXCEED() != null) {
							R95cell5.setCellValue(record2.getR95_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R95cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R95cell6 = row.createCell(9);
							if (record1.getR95_total_deposit_bank() != null) {
							R95cell6.setCellValue(record1.getR95_total_deposit_bank().doubleValue());
							R95cell6.setCellStyle(numberStyle);
							} else {
							R95cell6.setCellValue("");
							R95cell6.setCellStyle(textStyle);
							}

							// R96
							row = sheet.getRow(95);
							// Column C
							Cell R96cell1 = row.createCell(2);
							if (record1.getR96_deposit_excluding_number() != null) {
								R96cell1.setCellValue(record1.getR96_deposit_excluding_number().doubleValue());
								R96cell1.setCellStyle(numberStyle);
							} else {
								R96cell1.setCellValue("");
								R96cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R96cell2 = row.createCell(3);
							if (record1.getR96_deposit_excluding_amount() != null) {
								R96cell2.setCellValue(record1.getR96_deposit_excluding_amount().doubleValue());
								R96cell2.setCellStyle(numberStyle);
							} else {
								R96cell2.setCellValue("");
								R96cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R96cell3 = row.createCell(4);
							if (record1.getR96_deposit_foreign_number() != null) {
								R96cell3.setCellValue(record1.getR96_deposit_foreign_number().doubleValue());
								R96cell3.setCellStyle(numberStyle);
							} else {
								R96cell3.setCellValue("");
								R96cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R96cell4 = row.createCell(5);
							if (record1.getR96_deposit_foreign_amount() != null) {
								R96cell4.setCellValue(record1.getR96_deposit_foreign_amount().doubleValue());
								R96cell4.setCellStyle(numberStyle);
							} else {
								R96cell4.setCellValue("");
								R96cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R96cell5 = row.createCell(8);
							if (record2.getR96_TOTAL_DEPOSIT_EXCEED() != null) {
							R96cell5.setCellValue(record2.getR96_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R96cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R96cell6 = row.createCell(9);
							if (record1.getR96_total_deposit_bank() != null) {
							R96cell6.setCellValue(record1.getR96_total_deposit_bank().doubleValue());
							R96cell6.setCellStyle(numberStyle);
							} else {
							R96cell6.setCellValue("");
							R96cell6.setCellStyle(textStyle);
							}

							// R97
							row = sheet.getRow(96);
							// Column C
							Cell R97cell1 = row.createCell(2);
							if (record1.getR97_deposit_excluding_number() != null) {
								R97cell1.setCellValue(record1.getR97_deposit_excluding_number().doubleValue());
								R97cell1.setCellStyle(numberStyle);
							} else {
								R97cell1.setCellValue("");
								R97cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R97cell2 = row.createCell(3);
							if (record1.getR97_deposit_excluding_amount() != null) {
								R97cell2.setCellValue(record1.getR97_deposit_excluding_amount().doubleValue());
								R97cell2.setCellStyle(numberStyle);
							} else {
								R97cell2.setCellValue("");
								R97cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R97cell3 = row.createCell(4);
							if (record1.getR97_deposit_foreign_number() != null) {
								R97cell3.setCellValue(record1.getR97_deposit_foreign_number().doubleValue());
								R97cell3.setCellStyle(numberStyle);
							} else {
								R97cell3.setCellValue("");
								R97cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R97cell4 = row.createCell(5);
							if (record1.getR97_deposit_foreign_amount() != null) {
								R97cell4.setCellValue(record1.getR97_deposit_foreign_amount().doubleValue());
								R97cell4.setCellStyle(numberStyle);
							} else {
								R97cell4.setCellValue("");
								R97cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R97cell5 = row.createCell(8);
							if (record2.getR97_TOTAL_DEPOSIT_EXCEED() != null) {
							R97cell5.setCellValue(record2.getR97_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R97cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R97cell6 = row.createCell(9);
							if (record.getR7_total_deposit_bank() != null) {
							R97cell6.setCellValue(record.getR7_total_deposit_bank().doubleValue());
							R97cell6.setCellStyle(numberStyle);
							} else {
							R97cell6.setCellValue("");
							R97cell6.setCellStyle(textStyle);
							}

							// R98
							row = sheet.getRow(97);
							// Column C
							Cell R98cell1 = row.createCell(2);
							if (record1.getR98_deposit_excluding_number() != null) {
								R98cell1.setCellValue(record1.getR98_deposit_excluding_number().doubleValue());
								R98cell1.setCellStyle(numberStyle);
							} else {
								R98cell1.setCellValue("");
								R98cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R98cell2 = row.createCell(3);
							if (record1.getR98_deposit_excluding_amount() != null) {
								R98cell2.setCellValue(record1.getR98_deposit_excluding_amount().doubleValue());
								R98cell2.setCellStyle(numberStyle);
							} else {
								R98cell2.setCellValue("");
								R98cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R98cell3 = row.createCell(4);
							if (record1.getR98_deposit_foreign_number() != null) {
								R98cell3.setCellValue(record1.getR98_deposit_foreign_number().doubleValue());
								R98cell3.setCellStyle(numberStyle);
							} else {
								R98cell3.setCellValue("");
								R98cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R98cell4 = row.createCell(5);
							if (record1.getR98_deposit_foreign_amount() != null) {
								R98cell4.setCellValue(record1.getR98_deposit_foreign_amount().doubleValue());
								R98cell4.setCellStyle(numberStyle);
							} else {
								R98cell4.setCellValue("");
								R98cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R98cell5 = row.createCell(8);
							if (record2.getR98_TOTAL_DEPOSIT_EXCEED() != null) {
							R98cell5.setCellValue(record2.getR98_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R98cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R98cell6 = row.createCell(9);
							if (record1.getR98_total_deposit_bank() != null) {
							R98cell6.setCellValue(record1.getR98_total_deposit_bank().doubleValue());
							R98cell6.setCellStyle(numberStyle);
							} else {
							R98cell6.setCellValue("");
							R98cell6.setCellStyle(textStyle);
							}

							// R99
							row = sheet.getRow(98);
							// Column C
							Cell R99cell1 = row.createCell(2);
							if (record1.getR99_deposit_excluding_number() != null) {
								R99cell1.setCellValue(record1.getR99_deposit_excluding_number().doubleValue());
								R99cell1.setCellStyle(numberStyle);
							} else {
								R99cell1.setCellValue("");
								R99cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R99cell2 = row.createCell(3);
							if (record1.getR99_deposit_excluding_amount() != null) {
								R99cell2.setCellValue(record1.getR99_deposit_excluding_amount().doubleValue());
								R99cell2.setCellStyle(numberStyle);
							} else {
								R99cell2.setCellValue("");
								R99cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R99cell3 = row.createCell(4);
							if (record1.getR99_deposit_foreign_number() != null) {
								R99cell3.setCellValue(record1.getR99_deposit_foreign_number().doubleValue());
								R99cell3.setCellStyle(numberStyle);
							} else {
								R99cell3.setCellValue("");
								R99cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R99cell4 = row.createCell(5);
							if (record1.getR99_deposit_foreign_amount() != null) {
								R99cell4.setCellValue(record1.getR99_deposit_foreign_amount().doubleValue());
								R99cell4.setCellStyle(numberStyle);
							} else {
								R99cell4.setCellValue("");
								R99cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R99cell5 = row.createCell(8);
							if (record2.getR99_TOTAL_DEPOSIT_EXCEED() != null) {
							R99cell5.setCellValue(record2.getR99_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R99cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R99cell6 = row.createCell(9);
							if (record1.getR99_total_deposit_bank() != null) {
							R99cell6.setCellValue(record1.getR99_total_deposit_bank().doubleValue());
							R99cell6.setCellStyle(numberStyle);
							} else {
							R99cell6.setCellValue("");
							R99cell6.setCellStyle(textStyle);
							}

							// R100
							row = sheet.getRow(99);
							// Column C
							Cell R100cell1 = row.createCell(2);
							if (record1.getR100_deposit_excluding_number() != null) {
								R100cell1.setCellValue(record1.getR100_deposit_excluding_number().doubleValue());
								R100cell1.setCellStyle(numberStyle);
							} else {
								R100cell1.setCellValue("");
								R100cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R100cell2 = row.createCell(3);
							if (record1.getR100_deposit_excluding_amount() != null) {
								R100cell2.setCellValue(record1.getR100_deposit_excluding_amount().doubleValue());
								R100cell2.setCellStyle(numberStyle);
							} else {
								R100cell2.setCellValue("");
								R100cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R100cell3 = row.createCell(4);
							if (record1.getR100_deposit_foreign_number() != null) {
								R100cell3.setCellValue(record1.getR100_deposit_foreign_number().doubleValue());
								R100cell3.setCellStyle(numberStyle);
							} else {
								R100cell3.setCellValue("");
								R100cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R100cell4 = row.createCell(5);
							if (record1.getR100_deposit_foreign_amount() != null) {
								R100cell4.setCellValue(record1.getR100_deposit_foreign_amount().doubleValue());
								R100cell4.setCellStyle(numberStyle);
							} else {
								R100cell4.setCellValue("");
								R100cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R100cell5 = row.createCell(8);
							if (record2.getR100_TOTAL_DEPOSIT_EXCEED() != null) {
							R100cell5.setCellValue(record2.getR100_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R100cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R100cell6 = row.createCell(9);
							if (record1.getR100_total_deposit_bank() != null) {
							R100cell6.setCellValue(record1.getR100_total_deposit_bank().doubleValue());
							R100cell6.setCellStyle(numberStyle);
							} else {
							R100cell6.setCellValue("");
							R100cell6.setCellStyle(textStyle);
							}

							// R103
							row = sheet.getRow(102);
							// Column C
							Cell R103cell1 = row.createCell(2);
							if (record1.getR103_deposit_excluding_number() != null) {
								R103cell1.setCellValue(record1.getR103_deposit_excluding_number().doubleValue());
								R103cell1.setCellStyle(numberStyle);
							} else {
								R103cell1.setCellValue("");
								R103cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R103cell2 = row.createCell(3);
							if (record1.getR103_deposit_excluding_amount() != null) {
								R103cell2.setCellValue(record1.getR103_deposit_excluding_amount().doubleValue());
								R103cell2.setCellStyle(numberStyle);
							} else {
								R103cell2.setCellValue("");
								R103cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R103cell3 = row.createCell(4);
							if (record1.getR103_deposit_foreign_number() != null) {
								R103cell3.setCellValue(record1.getR103_deposit_foreign_number().doubleValue());
								R103cell3.setCellStyle(numberStyle);
							} else {
								R103cell3.setCellValue("");
								R103cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R103cell4 = row.createCell(5);
							if (record1.getR103_deposit_foreign_amount() != null) {
								R103cell4.setCellValue(record1.getR103_deposit_foreign_amount().doubleValue());
								R103cell4.setCellStyle(numberStyle);
							} else {
								R103cell4.setCellValue("");
								R103cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R103cell5 = row.createCell(8);
							if (record2.getR103_TOTAL_DEPOSIT_EXCEED() != null) {
							R103cell5.setCellValue(record2.getR103_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R103cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R103cell6 = row.createCell(9);
							if (record1.getR103_total_deposit_bank() != null) {
							R103cell6.setCellValue(record1.getR103_total_deposit_bank().doubleValue());
							R103cell6.setCellStyle(numberStyle);
							} else {
							R103cell6.setCellValue("");
							R103cell6.setCellStyle(textStyle);
							}

							// R104
							row = sheet.getRow(103);
							// Column C
							Cell R104cell1 = row.createCell(2);
							if (record1.getR104_deposit_excluding_number() != null) {
								R104cell1.setCellValue(record1.getR104_deposit_excluding_number().doubleValue());
								R104cell1.setCellStyle(numberStyle);
							} else {
								R104cell1.setCellValue("");
								R104cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R104cell2 = row.createCell(3);
							if (record1.getR104_deposit_excluding_amount() != null) {
								R104cell2.setCellValue(record1.getR104_deposit_excluding_amount().doubleValue());
								R104cell2.setCellStyle(numberStyle);
							} else {
								R104cell2.setCellValue("");
								R104cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R104cell3 = row.createCell(4);
							if (record1.getR104_deposit_foreign_number() != null) {
								R104cell3.setCellValue(record1.getR104_deposit_foreign_number().doubleValue());
								R104cell3.setCellStyle(numberStyle);
							} else {
								R104cell3.setCellValue("");
								R104cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R104cell4 = row.createCell(5);
							if (record1.getR104_deposit_foreign_amount() != null) {
								R104cell4.setCellValue(record1.getR104_deposit_foreign_amount().doubleValue());
								R104cell4.setCellStyle(numberStyle);
							} else {
								R104cell4.setCellValue("");
								R104cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R104cell5 = row.createCell(8);
							if (record2.getR104_TOTAL_DEPOSIT_EXCEED() != null) {
							R104cell5.setCellValue(record2.getR104_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R104cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R104cell6 = row.createCell(9);
							if (record1.getR104_total_deposit_bank() != null) {
							R104cell6.setCellValue(record1.getR104_total_deposit_bank().doubleValue());
							R104cell6.setCellStyle(numberStyle);
							} else {
							R104cell6.setCellValue("");
							R104cell6.setCellStyle(textStyle);
							}


							// R105
							row = sheet.getRow(104);
							// Column C
							Cell R105cell1 = row.createCell(2);
							if (record1.getR105_deposit_excluding_number() != null) {
								R105cell1.setCellValue(record1.getR105_deposit_excluding_number().doubleValue());
								R105cell1.setCellStyle(numberStyle);
							} else {
								R105cell1.setCellValue("");
								R105cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R105cell2 = row.createCell(3);
							if (record1.getR105_deposit_excluding_amount() != null) {
								R105cell2.setCellValue(record1.getR105_deposit_excluding_amount().doubleValue());
								R105cell2.setCellStyle(numberStyle);
							} else {
								R105cell2.setCellValue("");
								R105cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R105cell3 = row.createCell(4);
							if (record1.getR105_deposit_foreign_number() != null) {
								R105cell3.setCellValue(record1.getR105_deposit_foreign_number().doubleValue());
								R105cell3.setCellStyle(numberStyle);
							} else {
								R105cell3.setCellValue("");
								R105cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R105cell4 = row.createCell(5);
							if (record1.getR105_deposit_foreign_amount() != null) {
								R105cell4.setCellValue(record1.getR105_deposit_foreign_amount().doubleValue());
								R105cell4.setCellStyle(numberStyle);
							} else {
								R105cell4.setCellValue("");
								R105cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R105cell5 = row.createCell(8);
							if (record2.getR105_TOTAL_DEPOSIT_EXCEED() != null) {
							R105cell5.setCellValue(record2.getR105_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R105cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R105cell6 = row.createCell(9);
							if (record1.getR105_total_deposit_bank() != null) {
							R105cell6.setCellValue(record1.getR105_total_deposit_bank().doubleValue());
							R105cell6.setCellStyle(numberStyle);
							} else {
							R105cell6.setCellValue("");
							R105cell6.setCellStyle(textStyle);
							}

							// R106
							row = sheet.getRow(105);
							// Column C
							Cell R106cell1 = row.createCell(2);
							if (record1.getR106_deposit_excluding_number() != null) {
								R106cell1.setCellValue(record1.getR106_deposit_excluding_number().doubleValue());
								R106cell1.setCellStyle(numberStyle);
							} else {
								R106cell1.setCellValue("");
								R106cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R106cell2 = row.createCell(3);
							if (record1.getR106_deposit_excluding_amount() != null) {
								R106cell2.setCellValue(record1.getR106_deposit_excluding_amount().doubleValue());
								R106cell2.setCellStyle(numberStyle);
							} else {
								R106cell2.setCellValue("");
								R106cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R106cell3 = row.createCell(4);
							if (record1.getR106_deposit_foreign_number() != null) {
								R106cell3.setCellValue(record1.getR106_deposit_foreign_number().doubleValue());
								R106cell3.setCellStyle(numberStyle);
							} else {
								R106cell3.setCellValue("");
								R106cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R106cell4 = row.createCell(5);
							if (record1.getR106_deposit_foreign_amount() != null) {
								R106cell4.setCellValue(record1.getR106_deposit_foreign_amount().doubleValue());
								R106cell4.setCellStyle(numberStyle);
							} else {
								R106cell4.setCellValue("");
								R106cell4.setCellStyle(textStyle);
							}
							
							// Column I
							Cell R106cell5 = row.createCell(8);
							if (record2.getR106_TOTAL_DEPOSIT_EXCEED() != null) {
							R106cell5.setCellValue(record2.getR106_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R106cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R106cell6 = row.createCell(9);
							if (record1.getR106_total_deposit_bank() != null) {
							R106cell6.setCellValue(record1.getR106_total_deposit_bank().doubleValue());
							R106cell6.setCellStyle(numberStyle);
							} else {
							R106cell6.setCellValue("");
							R106cell6.setCellStyle(textStyle);
							}


							// R107
							row = sheet.getRow(106);
							// Column C
							Cell R107cell1 = row.createCell(2);
							if (record1.getR107_deposit_excluding_number() != null) {
								R107cell1.setCellValue(record1.getR107_deposit_excluding_number().doubleValue());
								R107cell1.setCellStyle(numberStyle);
							} else {
								R107cell1.setCellValue("");
								R107cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R107cell2 = row.createCell(3);
							if (record1.getR107_deposit_excluding_amount() != null) {
								R107cell2.setCellValue(record1.getR107_deposit_excluding_amount().doubleValue());
								R107cell2.setCellStyle(numberStyle);
							} else {
								R107cell2.setCellValue("");
								R107cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R107cell3 = row.createCell(4);
							if (record1.getR107_deposit_foreign_number() != null) {
								R107cell3.setCellValue(record1.getR107_deposit_foreign_number().doubleValue());
								R107cell3.setCellStyle(numberStyle);
							} else {
								R107cell3.setCellValue("");
								R107cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R107cell4 = row.createCell(5);
							if (record1.getR107_deposit_foreign_amount() != null) {
								R107cell4.setCellValue(record1.getR107_deposit_foreign_amount().doubleValue());
								R107cell4.setCellStyle(numberStyle);
							} else {
								R107cell4.setCellValue("");
								R107cell4.setCellStyle(textStyle);
							}

							// Column I
							Cell R107cell5 = row.createCell(8);
							if (record2.getR107_TOTAL_DEPOSIT_EXCEED() != null) {
							R107cell5.setCellValue(record2.getR107_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R107cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R107cell6 = row.createCell(9);
							if (record1.getR107_total_deposit_bank() != null) {
							R107cell6.setCellValue(record1.getR107_total_deposit_bank().doubleValue());
							R107cell6.setCellStyle(numberStyle);
							} else {
							R107cell6.setCellValue("");
							R107cell6.setCellStyle(textStyle);
							}

							// R108
							row = sheet.getRow(107);
							// Column C
							Cell R108cell1 = row.createCell(2);
							if (record1.getR108_deposit_excluding_number() != null) {
								R108cell1.setCellValue(record1.getR108_deposit_excluding_number().doubleValue());
								R108cell1.setCellStyle(numberStyle);
							} else {
								R108cell1.setCellValue("");
								R108cell1.setCellStyle(textStyle);
							}

							// Column D
							Cell R108cell2 = row.createCell(3);
							if (record1.getR108_deposit_excluding_amount() != null) {
								R108cell2.setCellValue(record1.getR108_deposit_excluding_amount().doubleValue());
								R108cell2.setCellStyle(numberStyle);
							} else {
								R108cell2.setCellValue("");
								R108cell2.setCellStyle(textStyle);
							}

							// Column E
							Cell R108cell3 = row.createCell(4);
							if (record1.getR108_deposit_foreign_number() != null) {
								R108cell3.setCellValue(record1.getR108_deposit_foreign_number().doubleValue());
								R108cell3.setCellStyle(numberStyle);
							} else {
								R108cell3.setCellValue("");
								R108cell3.setCellStyle(textStyle);
							}

							// Column F
							Cell R108cell4 = row.createCell(5);
							if (record1.getR108_deposit_foreign_amount() != null) {
								R108cell4.setCellValue(record1.getR108_deposit_foreign_amount().doubleValue());
								R108cell4.setCellStyle(numberStyle);
							} else {
								R108cell4.setCellValue("");
								R108cell4.setCellStyle(textStyle);
							}
							// Column I
							Cell R108cell5 = row.createCell(8);
							if (record2.getR108_TOTAL_DEPOSIT_EXCEED() != null) {
							R108cell5.setCellValue(record2.getR108_TOTAL_DEPOSIT_EXCEED().doubleValue());
							} else {
							R108cell5.setCellValue("");
							
							}
												
							// Column J
							Cell R108cell6 = row.createCell(9);
							if (record1.getR108_total_deposit_bank() != null) {
							R108cell6.setCellValue(record1.getR108_total_deposit_bank().doubleValue());
							R108cell6.setCellStyle(numberStyle);
							} else {
							R108cell6.setCellValue("");
							R108cell6.setCellStyle(textStyle);
							}
							}

							
							workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
						} }else {

						}
						// Write the final workbook content to the in-memory stream.
						workbook.write(out);
						logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
						return out.toByteArray();
					}
				}


	public void updateReport(MDISB1_Summary_Entity_Manual updatedEntity) {

		System.out.println("Came to services 1");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		MDISB1_Summary_Entity_Manual existing = MDISB1_Summary_repo3.findById(updatedEntity.getReport_date())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

		try {
			String[] fields = { "TOTAL_DEPOSIT_EXCEED" };

			// 🔹 R7 to R12
			for (int i = 7; i <= 108; i++) {
				String prefix = "R" + i + "_";

				for (String field : fields) {
					try {
						String getterName = "get" + prefix + field;
						String setterName = "set" + prefix + field;

						Method getter = MDISB1_Summary_Entity_Manual.class.getMethod(getterName);
						Method setter = MDISB1_Summary_Entity_Manual.class.getMethod(setterName,
								getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
					}
				}
			}

			/*
			 * // 🔹 R15 to R20 for (int i = 15; i <= 20; i++) { String prefix = "R" + i +
			 * "_";
			 * 
			 * for (String field : fields) { try { String getterName = "get" + prefix +
			 * field; String setterName = "set" + prefix + field;
			 * 
			 * Method getter = MDISB1_Summary_Entity_Manual.class.getMethod(getterName);
			 * Method setter = MDISB1_Summary_Entity_Manual.class .getMethod(setterName,
			 * getter.getReturnType());
			 * 
			 * Object newValue = getter.invoke(updatedEntity); setter.invoke(existing,
			 * newValue);
			 * 
			 * } catch (NoSuchMethodException e) { // Skip missing fields } } }
			 */

			// 🔹 R13
			for (String field : fields) {
				try {
					String getterName = "getR13_" + field;
					String setterName = "setR13_" + field;

					Method getter = MDISB1_Summary_Entity_Manual.class.getMethod(getterName);
					Method setter = MDISB1_Summary_Entity_Manual.class.getMethod(setterName, getter.getReturnType());

					Object newValue = getter.invoke(updatedEntity);
					setter.invoke(existing, newValue);

				} catch (NoSuchMethodException e) {
					// Skip missing fields
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		System.out.println("Testing 1");

		// 🔹 Save
		MDISB1_Summary_repo3.save(existing);
	}

}
