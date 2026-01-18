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

import com.bornfire.brrs.entities.BRRS_M_DEP2_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_DEP2_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_DEP2_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_DEP2_Summary_Repo;
import com.bornfire.brrs.entities.M_DEP2_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_DEP2_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_DEP2_Detail_Entity;
import com.bornfire.brrs.entities.M_DEP2_Summary_Entity;

@Component
@Service

public class BRRS_M_DEP2_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_DEP2_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	
	@Autowired
	BRRS_M_DEP2_Summary_Repo BRRS_M_DEP2_Summary_Repo;

	@Autowired
	BRRS_M_DEP2_Detail_Repo M_DEP2_Detail_Repo;

	@Autowired
	BRRS_M_DEP2_Archival_Detail_Repo M_DEP2_Archival_Detail_Repo;

	@Autowired
	BRRS_M_DEP2_Archival_Summary_Repo M_DEP2_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_DEP2View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, String version) {

		ModelAndView mv = new ModelAndView();
//		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<M_DEP2_Archival_Summary_Entity> T1Master = new ArrayList<M_DEP2_Archival_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = M_DEP2_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);

		} else {

			List<M_DEP2_Summary_Entity> T1Master = new ArrayList<M_DEP2_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_DEP2_Summary_Repo.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

		mv.setViewName("BRRS/M_DEP2");

		// mv.addObject("reportmaster", T1Master);
		mv.addObject("displaymode", "summary");
		// mv.addObject("reportsflag", "reportsflag");
		// mv.addObject("menu", reportId);
		System.out.println("scv" + mv.getViewName());

		return mv;

	}

	public ModelAndView getM_DEP2currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {

		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();
//		Session hs = sessionFactory.getCurrentSession();

		try {
			Date parsedDate = null;
			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

			String reportLabel = null;
			String reportAddlCriteria1 = null;

			// ✅ Split the filter string here
			if (filter != null && filter.contains(",")) {
				String[] parts = filter.split(",");
				if (parts.length >= 2) {
					reportLabel = parts[0];
					reportAddlCriteria1 = parts[1];
				}
			}

			if ("ARCHIVAL".equals(type) && version != null) {
				System.out.println(type);
				System.out.println(version);
				// 🔹 Archival branch
				List<M_DEP2_Archival_Detail_Entity> T1Dt1;
				if (reportLabel != null && reportAddlCriteria1 != null) {
					T1Dt1 = M_DEP2_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1, parsedDate, version);
				} else {
					T1Dt1 = M_DEP2_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
					totalPages = M_DEP2_Detail_Repo.getdatacount(parsedDate);
					System.out.println(T1Dt1.size());
					mv.addObject("pagination", "YES");

				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				System.out.println("Praveen");
				// 🔹 Current branch
				List<M_DEP2_Detail_Entity> T1Dt1;
				if (reportLabel != null && reportAddlCriteria1 != null) {
					T1Dt1 = M_DEP2_Detail_Repo.GetDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1, parsedDate);
				} else {
					T1Dt1 = M_DEP2_Detail_Repo.getdatabydateList(parsedDate);
					totalPages = M_DEP2_Detail_Repo.getdatacount(parsedDate);
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
		mv.setViewName("BRRS/M_DEP2");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}

	public byte[] BRRS_M_DEP2Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		System.out.println(type);
		System.out.println(version);
		if (type.equals("ARCHIVAL") & version != null) {
			byte[] ARCHIVALreport = getExcelM_DEP2ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
					version);
			return ARCHIVALreport;
		}

		List<M_DEP2_Summary_Entity> dataList = BRRS_M_DEP2_Summary_Repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRF2.4 report. Returning empty result.");
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

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			// --- End of Style Definitions ---

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_DEP2_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column B
					Cell cell1 = row.getCell(1);
					if (record.getR12_current() != null) {
						cell1.setCellValue(record.getR12_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row12
					// Column C
					Cell cell2 = row.createCell(2);
					if (record.getR12_call() != null) {
						cell2.setCellValue(record.getR12_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR12_savings() != null) {
						cell3.setCellValue(record.getR12_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row12
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR12_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR12_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row12
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR12_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR12_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row12
					// Column G
					Cell cell6 = row.createCell(6);
					if (record.getR12_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR12_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row12
					// Column H
					Cell cell7 = row.createCell(7);
					if (record.getR12_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR12_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row12
					// Column I
					Cell cell8 = row.createCell(8);
					if (record.getR12_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR12_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row12
					// Column J
					Cell cell9 = row.createCell(9);
					if (record.getR12_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR12_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					// row12
					// Column K
					Cell cell10 = row.createCell(10);
					if (record.getR12_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR12_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					// row12
					// Column L
					Cell cell11 = row.createCell(11);
					if (record.getR12_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR12_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					// row12
					// Column M
					Cell cell12 = row.createCell(12);
					if (record.getR12_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR12_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					
					// row12
					// Column N
					Cell cell13 = row.createCell(13);
					if (record.getR12_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR12_certificates_of_deposit().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR13_current() != null) {
						cell1.setCellValue(record.getR13_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row13
					// Column C
					cell2 = row.createCell(2);
					if (record.getR13_call() != null) {
						cell2.setCellValue(record.getR13_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// row13
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_savings() != null) {
						cell3.setCellValue(record.getR13_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row13
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR13_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR13_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR13_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row13
					// Column G
					cell6 = row.createCell(6);
					if (record.getR13_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR13_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row13
					// Column H
					cell7 = row.createCell(7);
					if (record.getR13_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR13_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row13
					// Column I
					cell8 = row.createCell(8);
					if (record.getR13_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR13_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row13
					// Column J
					cell9 = row.createCell(9);
					if (record.getR13_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR13_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					// row13
					// Column K
					cell10 = row.createCell(10);
					if (record.getR13_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR13_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					// row13
					// Column L
					cell11 = row.createCell(11);
					if (record.getR13_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR13_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					// row13
					// Column M
					cell12 = row.createCell(12);
					if (record.getR13_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR13_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					
					// row13
					// Column N
					cell13 = row.createCell(13);
					if (record.getR13_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR13_certificates_of_deposit().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// row14
					row = sheet.getRow(13);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR14_current() != null) {
						cell1.setCellValue(record.getR14_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row14
					// Column C
					cell2 = row.createCell(2);
					if (record.getR14_call() != null) {
						cell2.setCellValue(record.getR14_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// row14
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_savings() != null) {
						cell3.setCellValue(record.getR14_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row14
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR14_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row14
					// Column F
					cell5 = row.createCell(5);
					if (record.getR14_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR14_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row14
					// Column G
					cell6 = row.createCell(6);
					if (record.getR14_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR14_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row14
					// Column H
					cell7 = row.createCell(7);
					if (record.getR14_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR14_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row14
					// Column I
					cell8 = row.createCell(8);
					if (record.getR14_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR14_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row14
					// Column J
					cell9 = row.createCell(9);
					if (record.getR14_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR14_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					// row14
					// Column K
					cell10 = row.createCell(10);
					if (record.getR14_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR14_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					// row14
					// Column L
					cell11 = row.createCell(11);
					if (record.getR14_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR14_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					// row14
					// Column M
					cell12 = row.createCell(12);
					if (record.getR14_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR14_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					
					// row14
					// Column N
					cell13 = row.createCell(13);
					if (record.getR14_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR14_certificates_of_deposit().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					
				
					// Row 16
					row = sheet.getRow(15);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR16_current() != null) {
						cell1.setCellValue(record.getR16_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Row 16
					// Column C
					cell2 = row.createCell(2);
					if (record.getR16_call() != null) {
						cell2.setCellValue(record.getR16_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 16
					// Column D
					cell3 = row.createCell(3);
					if (record.getR16_savings() != null) {
						cell3.setCellValue(record.getR16_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 16
					// Column E
					cell4 = row.createCell(4);
					if (record.getR16_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR16_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 16
					// Column F
					cell5 = row.createCell(5);
					if (record.getR16_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR16_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 16
					// Column G
					cell6 = row.createCell(6);
					if (record.getR16_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR16_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 16
					// Column H
					cell7 = row.createCell(7);
					if (record.getR16_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR16_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 16
					// Column I
					cell8 = row.createCell(8);
					if (record.getR16_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR16_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 16
					// Column J
					cell9 = row.createCell(9);
					if (record.getR16_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR16_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 16
					// Column K
					cell10 = row.createCell(10);
					if (record.getR16_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR16_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 16
					// Column L
					cell11 = row.createCell(11);
					if (record.getR16_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR16_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 16
					// Column M
					cell12 = row.createCell(12);
					if (record.getR16_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR16_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 16
					// Column N
					cell13 = row.createCell(13);
					if (record.getR16_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR16_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 17
					row = sheet.getRow(16);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR17_current() != null) {
						cell1.setCellValue(record.getR17_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 17
					// Column C
					cell2 = row.createCell(2);
					if (record.getR17_call() != null) {
						cell2.setCellValue(record.getR17_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 17
					// Column D
					cell3 = row.createCell(3);
					if (record.getR17_savings() != null) {
						cell3.setCellValue(record.getR17_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 17
					// Column E
					cell4 = row.createCell(4);
					if (record.getR17_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR17_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 17
					// Column F
					cell5 = row.createCell(5);
					if (record.getR17_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR17_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 17
					// Column G
					cell6 = row.createCell(6);
					if (record.getR17_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR17_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 17
					// Column H
					cell7 = row.createCell(7);
					if (record.getR17_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR17_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 17
					// Column I
					cell8 = row.createCell(8);
					if (record.getR17_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR17_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 17
					// Column J
					cell9 = row.createCell(9);
					if (record.getR17_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR17_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 17
					// Column K
					cell10 = row.createCell(10);
					if (record.getR17_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR17_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 17
					// Column L
					cell11 = row.createCell(11);
					if (record.getR17_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR17_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 17
					// Column M
					cell12 = row.createCell(12);
					if (record.getR17_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR17_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 17
					// Column N
					cell13 = row.createCell(13);
					if (record.getR17_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR17_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 18
					row = sheet.getRow(17);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR18_current() != null) {
						cell1.setCellValue(record.getR18_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 18
					// Column C
					cell2 = row.createCell(2);
					if (record.getR18_call() != null) {
						cell2.setCellValue(record.getR18_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 18
					// Column D
					cell3 = row.createCell(3);
					if (record.getR18_savings() != null) {
						cell3.setCellValue(record.getR18_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 18
					// Column E
					cell4 = row.createCell(4);
					if (record.getR18_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR18_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 18
					// Column F
					cell5 = row.createCell(5);
					if (record.getR18_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR18_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 18
					// Column G
					cell6 = row.createCell(6);
					if (record.getR18_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR18_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 18
					// Column H
					cell7 = row.createCell(7);
					if (record.getR18_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR18_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 18
					// Column I
					cell8 = row.createCell(8);
					if (record.getR18_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR18_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 18
					// Column J
					cell9 = row.createCell(9);
					if (record.getR18_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR18_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 18
					// Column K
					cell10 = row.createCell(10);
					if (record.getR18_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR18_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 18
					// Column L
					cell11 = row.createCell(11);
					if (record.getR18_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR18_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 18
					// Column M
					cell12 = row.createCell(12);
					if (record.getR18_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR18_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 18
					// Column N
					cell13 = row.createCell(13);
					if (record.getR18_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR18_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 19
					row = sheet.getRow(18);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR19_current() != null) {
						cell1.setCellValue(record.getR19_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 19
					// Column C
					cell2 = row.createCell(2);
					if (record.getR19_call() != null) {
						cell2.setCellValue(record.getR19_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 19
					// Column D
					cell3 = row.createCell(3);
					if (record.getR19_savings() != null) {
						cell3.setCellValue(record.getR19_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 19
					// Column E
					cell4 = row.createCell(4);
					if (record.getR19_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR19_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 19
					// Column F
					cell5 = row.createCell(5);
					if (record.getR19_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR19_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 19
					// Column G
					cell6 = row.createCell(6);
					if (record.getR19_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR19_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 19
					// Column H
					cell7 = row.createCell(7);
					if (record.getR19_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR19_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 19
					// Column I
					cell8 = row.createCell(8);
					if (record.getR19_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR19_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 19
					// Column J
					cell9 = row.createCell(9);
					if (record.getR19_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR19_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 19
					// Column K
					cell10 = row.createCell(10);
					if (record.getR19_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR19_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 19
					// Column L
					cell11 = row.createCell(11);
					if (record.getR19_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR19_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 19
					// Column M
					cell12 = row.createCell(12);
					if (record.getR19_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR19_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 19
					// Column N
					cell13 = row.createCell(13);
					if (record.getR19_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR19_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 20
					row = sheet.getRow(19);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR20_current() != null) {
						cell1.setCellValue(record.getR20_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 20
					// Column C
					cell2 = row.createCell(2);
					if (record.getR20_call() != null) {
						cell2.setCellValue(record.getR20_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 20
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_savings() != null) {
						cell3.setCellValue(record.getR20_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 20
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR20_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 20
					// Column F
					cell5 = row.createCell(5);
					if (record.getR20_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR20_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 20
					// Column G
					cell6 = row.createCell(6);
					if (record.getR20_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR20_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 20
					// Column H
					cell7 = row.createCell(7);
					if (record.getR20_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR20_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 20
					// Column I
					cell8 = row.createCell(8);
					if (record.getR20_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR20_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 20
					// Column J
					cell9 = row.createCell(9);
					if (record.getR20_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR20_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 20
					// Column K
					cell10 = row.createCell(10);
					if (record.getR20_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR20_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 20
					// Column L
					cell11 = row.createCell(11);
					if (record.getR20_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR20_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 20
					// Column M
					cell12 = row.createCell(12);
					if (record.getR20_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR20_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 20
					// Column N
					cell13 = row.createCell(13);
					if (record.getR20_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR20_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 21
					row = sheet.getRow(20);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR21_current() != null) {
						cell1.setCellValue(record.getR21_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 21
					// Column C
					cell2 = row.createCell(2);
					if (record.getR21_call() != null) {
						cell2.setCellValue(record.getR21_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 21
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_savings() != null) {
						cell3.setCellValue(record.getR21_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 21
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR21_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 21
					// Column F
					cell5 = row.createCell(5);
					if (record.getR21_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR21_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 21
					// Column G
					cell6 = row.createCell(6);
					if (record.getR21_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR21_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 21
					// Column H
					cell7 = row.createCell(7);
					if (record.getR21_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR21_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 21
					// Column I
					cell8 = row.createCell(8);
					if (record.getR21_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR21_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 21
					// Column J
					cell9 = row.createCell(9);
					if (record.getR21_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR21_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 21
					// Column K
					cell10 = row.createCell(10);
					if (record.getR21_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR21_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 21
					// Column L
					cell11 = row.createCell(11);
					if (record.getR21_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR21_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 21
					// Column M
					cell12 = row.createCell(12);
					if (record.getR21_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR21_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 21
					// Column N
					cell13 = row.createCell(13);
					if (record.getR21_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR21_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 22
					row = sheet.getRow(21);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR22_current() != null) {
						cell1.setCellValue(record.getR22_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 22
					// Column C
					cell2 = row.createCell(2);
					if (record.getR22_call() != null) {
						cell2.setCellValue(record.getR22_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 22
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_savings() != null) {
						cell3.setCellValue(record.getR22_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 22
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR22_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 22
					// Column F
					cell5 = row.createCell(5);
					if (record.getR22_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR22_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 22
					// Column G
					cell6 = row.createCell(6);
					if (record.getR22_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR22_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 22
					// Column H
					cell7 = row.createCell(7);
					if (record.getR22_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR22_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 22
					// Column I
					cell8 = row.createCell(8);
					if (record.getR22_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR22_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 22
					// Column J
					cell9 = row.createCell(9);
					if (record.getR22_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR22_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 22
					// Column K
					cell10 = row.createCell(10);
					if (record.getR22_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR22_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 22
					// Column L
					cell11 = row.createCell(11);
					if (record.getR22_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR22_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 22
					// Column M
					cell12 = row.createCell(12);
					if (record.getR22_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR22_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 22
					// Column N
					cell13 = row.createCell(13);
					if (record.getR22_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR22_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 23
					row = sheet.getRow(22);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR23_current() != null) {
						cell1.setCellValue(record.getR23_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 23
					// Column C
					cell2 = row.createCell(2);
					if (record.getR23_call() != null) {
						cell2.setCellValue(record.getR23_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 23
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_savings() != null) {
						cell3.setCellValue(record.getR23_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 23
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR23_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 23
					// Column F
					cell5 = row.createCell(5);
					if (record.getR23_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR23_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 23
					// Column G
					cell6 = row.createCell(6);
					if (record.getR23_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR23_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 23
					// Column H
					cell7 = row.createCell(7);
					if (record.getR23_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR23_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 23
					// Column I
					cell8 = row.createCell(8);
					if (record.getR23_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR23_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 23
					// Column J
					cell9 = row.createCell(9);
					if (record.getR23_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR23_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 23
					// Column K
					cell10 = row.createCell(10);
					if (record.getR23_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR23_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 23
					// Column L
					cell11 = row.createCell(11);
					if (record.getR23_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR23_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 23
					// Column M
					cell12 = row.createCell(12);
					if (record.getR23_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR23_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 23
					// Column N
					cell13 = row.createCell(13);
					if (record.getR23_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR23_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
				
					// Row 24
					row = sheet.getRow(23);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR24_current() != null) {
						cell1.setCellValue(record.getR24_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 24
					// Column C
					cell2 = row.createCell(2);
					if (record.getR24_call() != null) {
						cell2.setCellValue(record.getR24_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 24
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_savings() != null) {
						cell3.setCellValue(record.getR24_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 24
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR24_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 24
					// Column F
					cell5 = row.createCell(5);
					if (record.getR24_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR24_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 24
					// Column G
					cell6 = row.createCell(6);
					if (record.getR24_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR24_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 24
					// Column H
					cell7 = row.createCell(7);
					if (record.getR24_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR24_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 24
					// Column I
					cell8 = row.createCell(8);
					if (record.getR24_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR24_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 24
					// Column J
					cell9 = row.createCell(9);
					if (record.getR24_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR24_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 24
					// Column K
					cell10 = row.createCell(10);
					if (record.getR24_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR24_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 24
					// Column L
					cell11 = row.createCell(11);
					if (record.getR24_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR24_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 24
					// Column M
					cell12 = row.createCell(12);
					if (record.getR24_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR24_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 24
					// Column N
					cell13 = row.createCell(13);
					if (record.getR24_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR24_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 25
					row = sheet.getRow(24);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR25_current() != null) {
						cell1.setCellValue(record.getR25_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 25
					// Column C
					cell2 = row.createCell(2);
					if (record.getR25_call() != null) {
						cell2.setCellValue(record.getR25_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 25
					// Column D
					cell3 = row.createCell(3);
					if (record.getR25_savings() != null) {
						cell3.setCellValue(record.getR25_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 25
					// Column E
					cell4 = row.createCell(4);
					if (record.getR25_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR25_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 25
					// Column F
					cell5 = row.createCell(5);
					if (record.getR25_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR25_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 25
					// Column G
					cell6 = row.createCell(6);
					if (record.getR25_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR25_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 25
					// Column H
					cell7 = row.createCell(7);
					if (record.getR25_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR25_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 25
					// Column I
					cell8 = row.createCell(8);
					if (record.getR25_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR25_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 25
					// Column J
					cell9 = row.createCell(9);
					if (record.getR25_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR25_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 25
					// Column K
					cell10 = row.createCell(10);
					if (record.getR25_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR25_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 25
					// Column L
					cell11 = row.createCell(11);
					if (record.getR25_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR25_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 25
					// Column M
					cell12 = row.createCell(12);
					if (record.getR25_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR25_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 25
					// Column N
					cell13 = row.createCell(13);
					if (record.getR25_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR25_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
				
					// Row 26
					row = sheet.getRow(25);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR26_current() != null) {
						cell1.setCellValue(record.getR26_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 26
					// Column C
					cell2 = row.createCell(2);
					if (record.getR26_call() != null) {
						cell2.setCellValue(record.getR26_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 26
					// Column D
					cell3 = row.createCell(3);
					if (record.getR26_savings() != null) {
						cell3.setCellValue(record.getR26_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 26
					// Column E
					cell4 = row.createCell(4);
					if (record.getR26_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR26_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 26
					// Column F
					cell5 = row.createCell(5);
					if (record.getR26_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR26_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 26
					// Column G
					cell6 = row.createCell(6);
					if (record.getR26_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR26_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 26
					// Column H
					cell7 = row.createCell(7);
					if (record.getR26_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR26_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 26
					// Column I
					cell8 = row.createCell(8);
					if (record.getR26_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR26_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 26
					// Column J
					cell9 = row.createCell(9);
					if (record.getR26_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR26_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 26
					// Column K
					cell10 = row.createCell(10);
					if (record.getR26_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR26_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 26
					// Column L
					cell11 = row.createCell(11);
					if (record.getR26_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR26_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 26
					// Column M
					cell12 = row.createCell(12);
					if (record.getR26_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR26_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 26
					// Column N
					cell13 = row.createCell(13);
					if (record.getR26_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR26_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 27
					row = sheet.getRow(26);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR27_current() != null) {
						cell1.setCellValue(record.getR27_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 27
					// Column C
					cell2 = row.createCell(2);
					if (record.getR27_call() != null) {
						cell2.setCellValue(record.getR27_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 27
					// Column D
					cell3 = row.createCell(3);
					if (record.getR27_savings() != null) {
						cell3.setCellValue(record.getR27_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 27
					// Column E
					cell4 = row.createCell(4);
					if (record.getR27_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR27_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 27
					// Column F
					cell5 = row.createCell(5);
					if (record.getR27_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR27_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 27
					// Column G
					cell6 = row.createCell(6);
					if (record.getR27_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR27_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 27
					// Column H
					cell7 = row.createCell(7);
					if (record.getR27_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR27_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 27
					// Column I
					cell8 = row.createCell(8);
					if (record.getR27_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR27_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 27
					// Column J
					cell9 = row.createCell(9);
					if (record.getR27_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR27_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 27
					// Column K
					cell10 = row.createCell(10);
					if (record.getR27_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR27_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 27
					// Column L
					cell11 = row.createCell(11);
					if (record.getR27_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR27_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 27
					// Column M
					cell12 = row.createCell(12);
					if (record.getR27_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR27_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 27
					// Column N
					cell13 = row.createCell(13);
					if (record.getR27_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR27_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 28
					row = sheet.getRow(27);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR28_current() != null) {
						cell1.setCellValue(record.getR28_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 28
					// Column C
					cell2 = row.createCell(2);
					if (record.getR28_call() != null) {
						cell2.setCellValue(record.getR28_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 28
					// Column D
					cell3 = row.createCell(3);
					if (record.getR28_savings() != null) {
						cell3.setCellValue(record.getR28_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 28
					// Column E
					cell4 = row.createCell(4);
					if (record.getR28_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR28_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 28
					// Column F
					cell5 = row.createCell(5);
					if (record.getR28_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR28_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 28
					// Column G
					cell6 = row.createCell(6);
					if (record.getR28_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR28_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 28
					// Column H
					cell7 = row.createCell(7);
					if (record.getR28_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR28_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 28
					// Column I
					cell8 = row.createCell(8);
					if (record.getR28_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR28_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 28
					// Column J
					cell9 = row.createCell(9);
					if (record.getR28_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR28_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 28
					// Column K
					cell10 = row.createCell(10);
					if (record.getR28_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR28_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 28
					// Column L
					cell11 = row.createCell(11);
					if (record.getR28_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR28_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 28
					// Column M
					cell12 = row.createCell(12);
					if (record.getR28_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR28_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 28
					// Column N
					cell13 = row.createCell(13);
					if (record.getR28_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR28_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 29
					row = sheet.getRow(28);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR29_current() != null) {
						cell1.setCellValue(record.getR29_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 29
					// Column C
					cell2 = row.createCell(2);
					if (record.getR29_call() != null) {
						cell2.setCellValue(record.getR29_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 29
					// Column D
					cell3 = row.createCell(3);
					if (record.getR29_savings() != null) {
						cell3.setCellValue(record.getR29_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 29
					// Column E
					cell4 = row.createCell(4);
					if (record.getR29_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR29_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 29
					// Column F
					cell5 = row.createCell(5);
					if (record.getR29_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR29_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 29
					// Column G
					cell6 = row.createCell(6);
					if (record.getR29_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR29_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 29
					// Column H
					cell7 = row.createCell(7);
					if (record.getR29_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR29_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 29
					// Column I
					cell8 = row.createCell(8);
					if (record.getR29_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR29_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 29
					// Column J
					cell9 = row.createCell(9);
					if (record.getR29_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR29_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 29
					// Column K
					cell10 = row.createCell(10);
					if (record.getR29_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR29_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 29
					// Column L
					cell11 = row.createCell(11);
					if (record.getR29_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR29_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 29
					// Column M
					cell12 = row.createCell(12);
					if (record.getR29_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR29_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 29
					// Column N
					cell13 = row.createCell(13);
					if (record.getR29_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR29_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 31
					row = sheet.getRow(30);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR31_current() != null) {
						cell1.setCellValue(record.getR31_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 31
					// Column C
					cell2 = row.createCell(2);
					if (record.getR31_call() != null) {
						cell2.setCellValue(record.getR31_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 31
					// Column D
					cell3 = row.createCell(3);
					if (record.getR31_savings() != null) {
						cell3.setCellValue(record.getR31_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 31
					// Column E
					cell4 = row.createCell(4);
					if (record.getR31_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR31_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 31
					// Column F
					cell5 = row.createCell(5);
					if (record.getR31_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR31_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 31
					// Column G
					cell6 = row.createCell(6);
					if (record.getR31_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR31_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 31
					// Column H
					cell7 = row.createCell(7);
					if (record.getR31_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR31_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 31
					// Column I
					cell8 = row.createCell(8);
					if (record.getR31_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR31_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 31
					// Column J
					cell9 = row.createCell(9);
					if (record.getR31_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR31_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 31
					// Column K
					cell10 = row.createCell(10);
					if (record.getR31_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR31_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 31
					// Column L
					cell11 = row.createCell(11);
					if (record.getR31_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR31_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 31
					// Column M
					cell12 = row.createCell(12);
					if (record.getR31_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR31_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 31
					// Column N
					cell13 = row.createCell(13);
					if (record.getR31_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR31_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 32
					row = sheet.getRow(31);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR32_current() != null) {
						cell1.setCellValue(record.getR32_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 32
					// Column C
					cell2 = row.createCell(2);
					if (record.getR32_call() != null) {
						cell2.setCellValue(record.getR32_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 32
					// Column D
					cell3 = row.createCell(3);
					if (record.getR32_savings() != null) {
						cell3.setCellValue(record.getR32_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 32
					// Column E
					cell4 = row.createCell(4);
					if (record.getR32_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR32_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 32
					// Column F
					cell5 = row.createCell(5);
					if (record.getR32_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR32_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 32
					// Column G
					cell6 = row.createCell(6);
					if (record.getR32_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR32_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 32
					// Column H
					cell7 = row.createCell(7);
					if (record.getR32_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR32_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 32
					// Column I
					cell8 = row.createCell(8);
					if (record.getR32_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR32_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 32
					// Column J
					cell9 = row.createCell(9);
					if (record.getR32_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR32_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 32
					// Column K
					cell10 = row.createCell(10);
					if (record.getR32_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR32_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 32
					// Column L
					cell11 = row.createCell(11);
					if (record.getR32_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR32_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 32
					// Column M
					cell12 = row.createCell(12);
					if (record.getR32_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR32_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 32
					// Column N
					cell13 = row.createCell(13);
					if (record.getR32_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR32_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 34
					row = sheet.getRow(33);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR34_current() != null) {
						cell1.setCellValue(record.getR34_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 34
					// Column C
					cell2 = row.createCell(2);
					if (record.getR34_call() != null) {
						cell2.setCellValue(record.getR34_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 34
					// Column D
					cell3 = row.createCell(3);
					if (record.getR34_savings() != null) {
						cell3.setCellValue(record.getR34_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 34
					// Column E
					cell4 = row.createCell(4);
					if (record.getR34_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR34_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 34
					// Column F
					cell5 = row.createCell(5);
					if (record.getR34_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR34_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 34
					// Column G
					cell6 = row.createCell(6);
					if (record.getR34_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR34_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 34
					// Column H
					cell7 = row.createCell(7);
					if (record.getR34_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR34_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 34
					// Column I
					cell8 = row.createCell(8);
					if (record.getR34_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR34_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 34
					// Column J
					cell9 = row.createCell(9);
					if (record.getR34_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR34_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 34
					// Column K
					cell10 = row.createCell(10);
					if (record.getR34_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR34_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 34
					// Column L
					cell11 = row.createCell(11);
					if (record.getR34_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR34_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 34
					// Column M
					cell12 = row.createCell(12);
					if (record.getR34_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR34_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 34
					// Column N
					cell13 = row.createCell(13);
					if (record.getR34_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR34_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 35
					row = sheet.getRow(34);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR35_current() != null) {
						cell1.setCellValue(record.getR35_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 35
					// Column C
					cell2 = row.createCell(2);
					if (record.getR35_call() != null) {
						cell2.setCellValue(record.getR35_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 35
					// Column D
					cell3 = row.createCell(3);
					if (record.getR35_savings() != null) {
						cell3.setCellValue(record.getR35_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 35
					// Column E
					cell4 = row.createCell(4);
					if (record.getR35_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR35_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 35
					// Column F
					cell5 = row.createCell(5);
					if (record.getR35_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR35_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 35
					// Column G
					cell6 = row.createCell(6);
					if (record.getR35_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR35_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 35
					// Column H
					cell7 = row.createCell(7);
					if (record.getR35_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR35_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 35
					// Column I
					cell8 = row.createCell(8);
					if (record.getR35_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR35_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 35
					// Column J
					cell9 = row.createCell(9);
					if (record.getR35_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR35_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 35
					// Column K
					cell10 = row.createCell(10);
					if (record.getR35_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR35_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 35
					// Column L
					cell11 = row.createCell(11);
					if (record.getR35_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR35_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 35
					// Column M
					cell12 = row.createCell(12);
					if (record.getR35_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR35_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 35
					// Column N
					cell13 = row.createCell(13);
					if (record.getR35_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR35_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 37
					row = sheet.getRow(36);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR37_current() != null) {
						cell1.setCellValue(record.getR37_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 37
					// Column C
					cell2 = row.createCell(2);
					if (record.getR37_call() != null) {
						cell2.setCellValue(record.getR37_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 37
					// Column D
					cell3 = row.createCell(3);
					if (record.getR37_savings() != null) {
						cell3.setCellValue(record.getR37_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 37
					// Column E
					cell4 = row.createCell(4);
					if (record.getR37_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR37_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 37
					// Column F
					cell5 = row.createCell(5);
					if (record.getR37_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR37_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 37
					// Column G
					cell6 = row.createCell(6);
					if (record.getR37_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR37_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 37
					// Column H
					cell7 = row.createCell(7);
					if (record.getR37_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR37_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 37
					// Column I
					cell8 = row.createCell(8);
					if (record.getR37_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR37_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 37
					// Column J
					cell9 = row.createCell(9);
					if (record.getR37_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR37_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 37
					// Column K
					cell10 = row.createCell(10);
					if (record.getR37_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR37_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 37
					// Column L
					cell11 = row.createCell(11);
					if (record.getR37_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR37_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 37
					// Column M
					cell12 = row.createCell(12);
					if (record.getR37_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR37_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 37
					// Column N
					cell13 = row.createCell(13);
					if (record.getR37_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR37_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 38
					row = sheet.getRow(37);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR38_current() != null) {
						cell1.setCellValue(record.getR38_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 38
					// Column C
					cell2 = row.createCell(2);
					if (record.getR38_call() != null) {
						cell2.setCellValue(record.getR38_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 38
					// Column D
					cell3 = row.createCell(3);
					if (record.getR38_savings() != null) {
						cell3.setCellValue(record.getR38_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 38
					// Column E
					cell4 = row.createCell(4);
					if (record.getR38_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR38_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 38
					// Column F
					cell5 = row.createCell(5);
					if (record.getR38_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR38_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 38
					// Column G
					cell6 = row.createCell(6);
					if (record.getR38_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR38_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 38
					// Column H
					cell7 = row.createCell(7);
					if (record.getR38_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR38_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 38
					// Column I
					cell8 = row.createCell(8);
					if (record.getR38_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR38_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 38
					// Column J
					cell9 = row.createCell(9);
					if (record.getR38_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR38_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 38
					// Column K
					cell10 = row.createCell(10);
					if (record.getR38_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR38_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 38
					// Column L
					cell11 = row.createCell(11);
					if (record.getR38_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR38_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 38
					// Column M
					cell12 = row.createCell(12);
					if (record.getR38_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR38_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 38
					// Column N
					cell13 = row.createCell(13);
					if (record.getR38_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR38_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 39
					row = sheet.getRow(38);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR39_current() != null) {
						cell1.setCellValue(record.getR39_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 39
					// Column C
					cell2 = row.createCell(2);
					if (record.getR39_call() != null) {
						cell2.setCellValue(record.getR39_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 39
					// Column D
					cell3 = row.createCell(3);
					if (record.getR39_savings() != null) {
						cell3.setCellValue(record.getR39_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 39
					// Column E
					cell4 = row.createCell(4);
					if (record.getR39_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR39_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 39
					// Column F
					cell5 = row.createCell(5);
					if (record.getR39_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR39_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 39
					// Column G
					cell6 = row.createCell(6);
					if (record.getR39_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR39_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 39
					// Column H
					cell7 = row.createCell(7);
					if (record.getR39_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR39_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 39
					// Column I
					cell8 = row.createCell(8);
					if (record.getR39_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR39_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 39
					// Column J
					cell9 = row.createCell(9);
					if (record.getR39_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR39_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 39
					// Column K
					cell10 = row.createCell(10);
					if (record.getR39_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR39_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 39
					// Column L
					cell11 = row.createCell(11);
					if (record.getR39_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR39_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 39
					// Column M
					cell12 = row.createCell(12);
					if (record.getR39_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR39_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 39
					// Column N
					cell13 = row.createCell(13);
					if (record.getR39_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR39_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 40
					row = sheet.getRow(39);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR40_current() != null) {
						cell1.setCellValue(record.getR40_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 40
					// Column C
					cell2 = row.createCell(2);
					if (record.getR40_call() != null) {
						cell2.setCellValue(record.getR40_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 40
					// Column D
					cell3 = row.createCell(3);
					if (record.getR40_savings() != null) {
						cell3.setCellValue(record.getR40_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 40
					// Column E
					cell4 = row.createCell(4);
					if (record.getR40_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR40_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 40
					// Column F
					cell5 = row.createCell(5);
					if (record.getR40_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR40_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 40
					// Column G
					cell6 = row.createCell(6);
					if (record.getR40_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR40_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 40
					// Column H
					cell7 = row.createCell(7);
					if (record.getR40_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR40_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 40
					// Column I
					cell8 = row.createCell(8);
					if (record.getR40_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR40_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 40
					// Column J
					cell9 = row.createCell(9);
					if (record.getR40_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR40_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 40
					// Column K
					cell10 = row.createCell(10);
					if (record.getR40_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR40_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 40
					// Column L
					cell11 = row.createCell(11);
					if (record.getR40_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR40_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 40
					// Column M
					cell12 = row.createCell(12);
					if (record.getR40_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR40_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 40
					// Column N
					cell13 = row.createCell(13);
					if (record.getR40_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR40_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 42
					row = sheet.getRow(41);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR42_current() != null) {
						cell1.setCellValue(record.getR42_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 42
					// Column C
					cell2 = row.createCell(2);
					if (record.getR42_call() != null) {
						cell2.setCellValue(record.getR42_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 42
					// Column D
					cell3 = row.createCell(3);
					if (record.getR42_savings() != null) {
						cell3.setCellValue(record.getR42_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 42
					// Column E
					cell4 = row.createCell(4);
					if (record.getR42_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR42_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 42
					// Column F
					cell5 = row.createCell(5);
					if (record.getR42_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR42_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 42
					// Column G
					cell6 = row.createCell(6);
					if (record.getR42_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR42_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 42
					// Column H
					cell7 = row.createCell(7);
					if (record.getR42_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR42_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 42
					// Column I
					cell8 = row.createCell(8);
					if (record.getR42_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR42_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 42
					// Column J
					cell9 = row.createCell(9);
					if (record.getR42_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR42_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 42
					// Column K
					cell10 = row.createCell(10);
					if (record.getR42_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR42_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 42
					// Column L
					cell11 = row.createCell(11);
					if (record.getR42_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR42_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 42
					// Column M
					cell12 = row.createCell(12);
					if (record.getR42_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR42_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 42
					// Column N
					cell13 = row.createCell(13);
					if (record.getR42_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR42_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 43
					row = sheet.getRow(42);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR43_current() != null) {
						cell1.setCellValue(record.getR43_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 43
					// Column C
					cell2 = row.createCell(2);
					if (record.getR43_call() != null) {
						cell2.setCellValue(record.getR43_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 43
					// Column D
					cell3 = row.createCell(3);
					if (record.getR43_savings() != null) {
						cell3.setCellValue(record.getR43_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 43
					// Column E
					cell4 = row.createCell(4);
					if (record.getR43_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR43_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 43
					// Column F
					cell5 = row.createCell(5);
					if (record.getR43_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR43_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 43
					// Column G
					cell6 = row.createCell(6);
					if (record.getR43_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR43_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 43
					// Column H
					cell7 = row.createCell(7);
					if (record.getR43_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR43_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 43
					// Column I
					cell8 = row.createCell(8);
					if (record.getR43_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR43_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 43
					// Column J
					cell9 = row.createCell(9);
					if (record.getR43_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR43_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 43
					// Column K
					cell10 = row.createCell(10);
					if (record.getR43_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR43_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 43
					// Column L
					cell11 = row.createCell(11);
					if (record.getR43_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR43_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 43
					// Column M
					cell12 = row.createCell(12);
					if (record.getR43_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR43_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 43
					// Column N
					cell13 = row.createCell(13);
					if (record.getR43_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR43_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 44
					row = sheet.getRow(43);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR44_current() != null) {
						cell1.setCellValue(record.getR44_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 44
					// Column C
					cell2 = row.createCell(2);
					if (record.getR44_call() != null) {
						cell2.setCellValue(record.getR44_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 44
					// Column D
					cell3 = row.createCell(3);
					if (record.getR44_savings() != null) {
						cell3.setCellValue(record.getR44_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 44
					// Column E
					cell4 = row.createCell(4);
					if (record.getR44_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR44_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 44
					// Column F
					cell5 = row.createCell(5);
					if (record.getR44_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR44_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 44
					// Column G
					cell6 = row.createCell(6);
					if (record.getR44_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR44_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 44
					// Column H
					cell7 = row.createCell(7);
					if (record.getR44_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR44_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 44
					// Column I
					cell8 = row.createCell(8);
					if (record.getR44_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR44_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 44
					// Column J
					cell9 = row.createCell(9);
					if (record.getR44_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR44_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 44
					// Column K
					cell10 = row.createCell(10);
					if (record.getR44_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR44_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 44
					// Column L
					cell11 = row.createCell(11);
					if (record.getR44_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR44_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 44
					// Column M
					cell12 = row.createCell(12);
					if (record.getR44_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR44_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 44
					// Column N
					cell13 = row.createCell(13);
					if (record.getR44_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR44_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 46
					row = sheet.getRow(45);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR46_current() != null) {
						cell1.setCellValue(record.getR46_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 46
					// Column C
					cell2 = row.createCell(2);
					if (record.getR46_call() != null) {
						cell2.setCellValue(record.getR46_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 46
					// Column D
					cell3 = row.createCell(3);
					if (record.getR46_savings() != null) {
						cell3.setCellValue(record.getR46_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 46
					// Column E
					cell4 = row.createCell(4);
					if (record.getR46_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR46_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 46
					// Column F
					cell5 = row.createCell(5);
					if (record.getR46_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR46_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 46
					// Column G
					cell6 = row.createCell(6);
					if (record.getR46_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR46_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 46
					// Column H
					cell7 = row.createCell(7);
					if (record.getR46_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR46_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 46
					// Column I
					cell8 = row.createCell(8);
					if (record.getR46_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR46_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 46
					// Column J
					cell9 = row.createCell(9);
					if (record.getR46_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR46_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 46
					// Column K
					cell10 = row.createCell(10);
					if (record.getR46_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR46_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 46
					// Column L
					cell11 = row.createCell(11);
					if (record.getR46_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR46_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 46
					// Column M
					cell12 = row.createCell(12);
					if (record.getR46_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR46_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 46
					// Column N
					cell13 = row.createCell(13);
					if (record.getR46_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR46_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 47
					row = sheet.getRow(46);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR47_current() != null) {
						cell1.setCellValue(record.getR47_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 47
					// Column C
					cell2 = row.createCell(2);
					if (record.getR47_call() != null) {
						cell2.setCellValue(record.getR47_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 47
					// Column D
					cell3 = row.createCell(3);
					if (record.getR47_savings() != null) {
						cell3.setCellValue(record.getR47_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 47
					// Column E
					cell4 = row.createCell(4);
					if (record.getR47_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR47_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 47
					// Column F
					cell5 = row.createCell(5);
					if (record.getR47_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR47_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 47
					// Column G
					cell6 = row.createCell(6);
					if (record.getR47_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR47_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 47
					// Column H
					cell7 = row.createCell(7);
					if (record.getR47_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR47_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 47
					// Column I
					cell8 = row.createCell(8);
					if (record.getR47_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR47_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 47
					// Column J
					cell9 = row.createCell(9);
					if (record.getR47_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR47_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 47
					// Column K
					cell10 = row.createCell(10);
					if (record.getR47_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR47_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 47
					// Column L
					cell11 = row.createCell(11);
					if (record.getR47_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR47_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 47
					// Column M
					cell12 = row.createCell(12);
					if (record.getR47_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR47_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 47
					// Column N
					cell13 = row.createCell(13);
					if (record.getR47_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR47_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 48
					row = sheet.getRow(47);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR48_current() != null) {
						cell1.setCellValue(record.getR48_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 48
					// Column C
					cell2 = row.createCell(2);
					if (record.getR48_call() != null) {
						cell2.setCellValue(record.getR48_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 48
					// Column D
					cell3 = row.createCell(3);
					if (record.getR48_savings() != null) {
						cell3.setCellValue(record.getR48_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 48
					// Column E
					cell4 = row.createCell(4);
					if (record.getR48_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR48_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 48
					// Column F
					cell5 = row.createCell(5);
					if (record.getR48_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR48_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 48
					// Column G
					cell6 = row.createCell(6);
					if (record.getR48_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR48_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 48
					// Column H
					cell7 = row.createCell(7);
					if (record.getR48_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR48_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 48
					// Column I
					cell8 = row.createCell(8);
					if (record.getR48_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR48_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 48
					// Column J
					cell9 = row.createCell(9);
					if (record.getR48_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR48_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 48
					// Column K
					cell10 = row.createCell(10);
					if (record.getR48_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR48_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 48
					// Column L
					cell11 = row.createCell(11);
					if (record.getR48_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR48_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 48
					// Column M
					cell12 = row.createCell(12);
					if (record.getR48_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR48_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 48
					// Column N
					cell13 = row.createCell(13);
					if (record.getR48_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR48_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 50
					row = sheet.getRow(49);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR50_current() != null) {
						cell1.setCellValue(record.getR50_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 50
					// Column C
					cell2 = row.createCell(2);
					if (record.getR50_call() != null) {
						cell2.setCellValue(record.getR50_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 50
					// Column D
					cell3 = row.createCell(3);
					if (record.getR50_savings() != null) {
						cell3.setCellValue(record.getR50_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 50
					// Column E
					cell4 = row.createCell(4);
					if (record.getR50_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR50_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 50
					// Column F
					cell5 = row.createCell(5);
					if (record.getR50_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR50_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 50
					// Column G
					cell6 = row.createCell(6);
					if (record.getR50_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR50_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 50
					// Column H
					cell7 = row.createCell(7);
					if (record.getR50_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR50_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 50
					// Column I
					cell8 = row.createCell(8);
					if (record.getR50_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR50_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 50
					// Column J
					cell9 = row.createCell(9);
					if (record.getR50_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR50_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 50
					// Column K
					cell10 = row.createCell(10);
					if (record.getR50_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR50_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 50
					// Column L
					cell11 = row.createCell(11);
					if (record.getR50_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR50_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 50
					// Column M
					cell12 = row.createCell(12);
					if (record.getR50_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR50_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 50
					// Column N
					cell13 = row.createCell(13);
					if (record.getR50_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR50_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 51
					row = sheet.getRow(50);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR51_current() != null) {
						cell1.setCellValue(record.getR51_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 51
					// Column C
					cell2 = row.createCell(2);
					if (record.getR51_call() != null) {
						cell2.setCellValue(record.getR51_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 51
					// Column D
					cell3 = row.createCell(3);
					if (record.getR51_savings() != null) {
						cell3.setCellValue(record.getR51_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 51
					// Column E
					cell4 = row.createCell(4);
					if (record.getR51_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR51_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 51
					// Column F
					cell5 = row.createCell(5);
					if (record.getR51_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR51_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 51
					// Column G
					cell6 = row.createCell(6);
					if (record.getR51_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR51_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 51
					// Column H
					cell7 = row.createCell(7);
					if (record.getR51_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR51_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 51
					// Column I
					cell8 = row.createCell(8);
					if (record.getR51_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR51_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 51
					// Column J
					cell9 = row.createCell(9);
					if (record.getR51_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR51_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 51
					// Column K
					cell10 = row.createCell(10);
					if (record.getR51_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR51_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 51
					// Column L
					cell11 = row.createCell(11);
					if (record.getR51_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR51_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 51
					// Column M
					cell12 = row.createCell(12);
					if (record.getR51_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR51_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 51
					// Column N
					cell13 = row.createCell(13);
					if (record.getR51_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR51_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 52
					row = sheet.getRow(51);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR52_current() != null) {
						cell1.setCellValue(record.getR52_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 52
					// Column C
					cell2 = row.createCell(2);
					if (record.getR52_call() != null) {
						cell2.setCellValue(record.getR52_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 52
					// Column D
					cell3 = row.createCell(3);
					if (record.getR52_savings() != null) {
						cell3.setCellValue(record.getR52_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 52
					// Column E
					cell4 = row.createCell(4);
					if (record.getR52_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR52_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 52
					// Column F
					cell5 = row.createCell(5);
					if (record.getR52_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR52_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 52
					// Column G
					cell6 = row.createCell(6);
					if (record.getR52_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR52_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 52
					// Column H
					cell7 = row.createCell(7);
					if (record.getR52_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR52_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 52
					// Column I
					cell8 = row.createCell(8);
					if (record.getR52_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR52_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 52
					// Column J
					cell9 = row.createCell(9);
					if (record.getR52_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR52_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 52
					// Column K
					cell10 = row.createCell(10);
					if (record.getR52_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR52_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 52
					// Column L
					cell11 = row.createCell(11);
					if (record.getR52_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR52_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 52
					// Column M
					cell12 = row.createCell(12);
					if (record.getR52_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR52_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 52
					// Column N
					cell13 = row.createCell(13);
					if (record.getR52_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR52_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 53
					row = sheet.getRow(52);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR53_current() != null) {
						cell1.setCellValue(record.getR53_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 53
					// Column C
					cell2 = row.createCell(2);
					if (record.getR53_call() != null) {
						cell2.setCellValue(record.getR53_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 53
					// Column D
					cell3 = row.createCell(3);
					if (record.getR53_savings() != null) {
						cell3.setCellValue(record.getR53_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 53
					// Column E
					cell4 = row.createCell(4);
					if (record.getR53_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR53_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 53
					// Column F
					cell5 = row.createCell(5);
					if (record.getR53_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR53_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 53
					// Column G
					cell6 = row.createCell(6);
					if (record.getR53_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR53_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 53
					// Column H
					cell7 = row.createCell(7);
					if (record.getR53_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR53_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 53
					// Column I
					cell8 = row.createCell(8);
					if (record.getR53_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR53_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 53
					// Column J
					cell9 = row.createCell(9);
					if (record.getR53_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR53_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 53
					// Column K
					cell10 = row.createCell(10);
					if (record.getR53_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR53_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 53
					// Column L
					cell11 = row.createCell(11);
					if (record.getR53_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR53_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 53
					// Column M
					cell12 = row.createCell(12);
					if (record.getR53_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR53_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 53
					// Column N
					cell13 = row.createCell(13);
					if (record.getR53_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR53_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 54
					row = sheet.getRow(53);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR54_current() != null) {
						cell1.setCellValue(record.getR54_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 54
					// Column C
					cell2 = row.createCell(2);
					if (record.getR54_call() != null) {
						cell2.setCellValue(record.getR54_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 54
					// Column D
					cell3 = row.createCell(3);
					if (record.getR54_savings() != null) {
						cell3.setCellValue(record.getR54_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 54
					// Column E
					cell4 = row.createCell(4);
					if (record.getR54_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR54_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 54
					// Column F
					cell5 = row.createCell(5);
					if (record.getR54_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR54_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 54
					// Column G
					cell6 = row.createCell(6);
					if (record.getR54_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR54_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 54
					// Column H
					cell7 = row.createCell(7);
					if (record.getR54_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR54_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 54
					// Column I
					cell8 = row.createCell(8);
					if (record.getR54_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR54_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 54
					// Column J
					cell9 = row.createCell(9);
					if (record.getR54_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR54_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 54
					// Column K
					cell10 = row.createCell(10);
					if (record.getR54_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR54_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 54
					// Column L
					cell11 = row.createCell(11);
					if (record.getR54_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR54_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 54
					// Column M
					cell12 = row.createCell(12);
					if (record.getR54_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR54_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 54
					// Column N
					cell13 = row.createCell(13);
					if (record.getR54_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR54_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 55
					row = sheet.getRow(54);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR55_current() != null) {
						cell1.setCellValue(record.getR55_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 55
					// Column C
					cell2 = row.createCell(2);
					if (record.getR55_call() != null) {
						cell2.setCellValue(record.getR55_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 55
					// Column D
					cell3 = row.createCell(3);
					if (record.getR55_savings() != null) {
						cell3.setCellValue(record.getR55_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 55
					// Column E
					cell4 = row.createCell(4);
					if (record.getR55_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR55_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 55
					// Column F
					cell5 = row.createCell(5);
					if (record.getR55_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR55_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 55
					// Column G
					cell6 = row.createCell(6);
					if (record.getR55_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR55_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 55
					// Column H
					cell7 = row.createCell(7);
					if (record.getR55_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR55_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 55
					// Column I
					cell8 = row.createCell(8);
					if (record.getR55_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR55_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 55
					// Column J
					cell9 = row.createCell(9);
					if (record.getR55_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR55_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 55
					// Column K
					cell10 = row.createCell(10);
					if (record.getR55_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR55_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 55
					// Column L
					cell11 = row.createCell(11);
					if (record.getR55_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR55_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 55
					// Column M
					cell12 = row.createCell(12);
					if (record.getR55_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR55_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 55
					// Column N
					cell13 = row.createCell(13);
					if (record.getR55_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR55_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 57
					row = sheet.getRow(56);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR57_current() != null) {
						cell1.setCellValue(record.getR57_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 57
					// Column C
					cell2 = row.createCell(2);
					if (record.getR57_call() != null) {
						cell2.setCellValue(record.getR57_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 57
					// Column D
					cell3 = row.createCell(3);
					if (record.getR57_savings() != null) {
						cell3.setCellValue(record.getR57_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 57
					// Column E
					cell4 = row.createCell(4);
					if (record.getR57_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR57_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 57
					// Column F
					cell5 = row.createCell(5);
					if (record.getR57_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR57_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 57
					// Column G
					cell6 = row.createCell(6);
					if (record.getR57_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR57_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 57
					// Column H
					cell7 = row.createCell(7);
					if (record.getR57_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR57_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 57
					// Column I
					cell8 = row.createCell(8);
					if (record.getR57_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR57_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 57
					// Column J
					cell9 = row.createCell(9);
					if (record.getR57_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR57_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 57
					// Column K
					cell10 = row.createCell(10);
					if (record.getR57_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR57_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 57
					// Column L
					cell11 = row.createCell(11);
					if (record.getR57_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR57_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 57
					// Column M
					cell12 = row.createCell(12);
					if (record.getR57_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR57_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 57
					// Column N
					cell13 = row.createCell(13);
					if (record.getR57_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR57_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
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

	public byte[] BRRS_M_DEP2DetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRS_M_DEP2 Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRRS_M_DEP2Details");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);
// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT_LABEL", "REPORT_ADDL_CRITERIA_1",
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
			List<M_DEP2_Detail_Entity> reportData = M_DEP2_Detail_Repo.getdatabydateList(parsedToDate);
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_DEP2_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());
// ACCT BALANCE (right aligned)
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
				logger.info("No data found for BRRS_M_DEP2 — only header will be written.");
			}
// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();
		} catch (Exception e) {
			logger.error("Error generating BRRS_M_DEP2 Excel", e);
			return new byte[0];
		}
	}

	public List<Object> getM_DEP2Archival() {
		List<Object> M_DEP2Archivallist = new ArrayList<>();
		try {
			M_DEP2Archivallist = M_DEP2_Archival_Summary_Repo.getM_DEP2archival();
			System.out.println("countser" + M_DEP2Archivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_DEP2 Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_DEP2Archivallist;
	}

	public byte[] getExcelM_DEP2ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_DEP2_Archival_Summary_Entity> dataList = M_DEP2_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_DEP2 report. Returning empty result.");
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

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			// --- End of Style Definitions ---
			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_DEP2_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column B
					Cell cell1 = row.getCell(1);
					if (record.getR12_current() != null) {
						cell1.setCellValue(record.getR12_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row12
					// Column C
					Cell cell2 = row.createCell(2);
					if (record.getR12_call() != null) {
						cell2.setCellValue(record.getR12_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR12_savings() != null) {
						cell3.setCellValue(record.getR12_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row12
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR12_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR12_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row12
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR12_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR12_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row12
					// Column G
					Cell cell6 = row.createCell(6);
					if (record.getR12_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR12_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row12
					// Column H
					Cell cell7 = row.createCell(7);
					if (record.getR12_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR12_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row12
					// Column I
					Cell cell8 = row.createCell(8);
					if (record.getR12_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR12_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row12
					// Column J
					Cell cell9 = row.createCell(9);
					if (record.getR12_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR12_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					// row12
					// Column K
					Cell cell10 = row.createCell(10);
					if (record.getR12_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR12_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					// row12
					// Column L
					Cell cell11 = row.createCell(11);
					if (record.getR12_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR12_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					// row12
					// Column M
					Cell cell12 = row.createCell(12);
					if (record.getR12_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR12_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					
					// row12
					// Column N
					Cell cell13 = row.createCell(13);
					if (record.getR12_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR12_certificates_of_deposit().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR13_current() != null) {
						cell1.setCellValue(record.getR13_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row13
					// Column C
					cell2 = row.createCell(2);
					if (record.getR13_call() != null) {
						cell2.setCellValue(record.getR13_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// row13
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_savings() != null) {
						cell3.setCellValue(record.getR13_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row13
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR13_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR13_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR13_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row13
					// Column G
					cell6 = row.createCell(6);
					if (record.getR13_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR13_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row13
					// Column H
					cell7 = row.createCell(7);
					if (record.getR13_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR13_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row13
					// Column I
					cell8 = row.createCell(8);
					if (record.getR13_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR13_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row13
					// Column J
					cell9 = row.createCell(9);
					if (record.getR13_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR13_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					// row13
					// Column K
					cell10 = row.createCell(10);
					if (record.getR13_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR13_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					// row13
					// Column L
					cell11 = row.createCell(11);
					if (record.getR13_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR13_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					// row13
					// Column M
					cell12 = row.createCell(12);
					if (record.getR13_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR13_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					
					// row13
					// Column N
					cell13 = row.createCell(13);
					if (record.getR13_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR13_certificates_of_deposit().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// row14
					row = sheet.getRow(13);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR14_current() != null) {
						cell1.setCellValue(record.getR14_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row14
					// Column C
					cell2 = row.createCell(2);
					if (record.getR14_call() != null) {
						cell2.setCellValue(record.getR14_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// row14
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_savings() != null) {
						cell3.setCellValue(record.getR14_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row14
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR14_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row14
					// Column F
					cell5 = row.createCell(5);
					if (record.getR14_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR14_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row14
					// Column G
					cell6 = row.createCell(6);
					if (record.getR14_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR14_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row14
					// Column H
					cell7 = row.createCell(7);
					if (record.getR14_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR14_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row14
					// Column I
					cell8 = row.createCell(8);
					if (record.getR14_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR14_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row14
					// Column J
					cell9 = row.createCell(9);
					if (record.getR14_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR14_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					
					// row14
					// Column K
					cell10 = row.createCell(10);
					if (record.getR14_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR14_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					
					// row14
					// Column L
					cell11 = row.createCell(11);
					if (record.getR14_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR14_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					
					// row14
					// Column M
					cell12 = row.createCell(12);
					if (record.getR14_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR14_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					
					// row14
					// Column N
					cell13 = row.createCell(13);
					if (record.getR14_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR14_certificates_of_deposit().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					
				
					// Row 16
					row = sheet.getRow(15);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR16_current() != null) {
						cell1.setCellValue(record.getR16_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Row 16
					// Column C
					cell2 = row.createCell(2);
					if (record.getR16_call() != null) {
						cell2.setCellValue(record.getR16_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 16
					// Column D
					cell3 = row.createCell(3);
					if (record.getR16_savings() != null) {
						cell3.setCellValue(record.getR16_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 16
					// Column E
					cell4 = row.createCell(4);
					if (record.getR16_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR16_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 16
					// Column F
					cell5 = row.createCell(5);
					if (record.getR16_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR16_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 16
					// Column G
					cell6 = row.createCell(6);
					if (record.getR16_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR16_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 16
					// Column H
					cell7 = row.createCell(7);
					if (record.getR16_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR16_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 16
					// Column I
					cell8 = row.createCell(8);
					if (record.getR16_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR16_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 16
					// Column J
					cell9 = row.createCell(9);
					if (record.getR16_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR16_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 16
					// Column K
					cell10 = row.createCell(10);
					if (record.getR16_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR16_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 16
					// Column L
					cell11 = row.createCell(11);
					if (record.getR16_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR16_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 16
					// Column M
					cell12 = row.createCell(12);
					if (record.getR16_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR16_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 16
					// Column N
					cell13 = row.createCell(13);
					if (record.getR16_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR16_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 17
					row = sheet.getRow(16);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR17_current() != null) {
						cell1.setCellValue(record.getR17_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 17
					// Column C
					cell2 = row.createCell(2);
					if (record.getR17_call() != null) {
						cell2.setCellValue(record.getR17_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 17
					// Column D
					cell3 = row.createCell(3);
					if (record.getR17_savings() != null) {
						cell3.setCellValue(record.getR17_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 17
					// Column E
					cell4 = row.createCell(4);
					if (record.getR17_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR17_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 17
					// Column F
					cell5 = row.createCell(5);
					if (record.getR17_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR17_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 17
					// Column G
					cell6 = row.createCell(6);
					if (record.getR17_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR17_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 17
					// Column H
					cell7 = row.createCell(7);
					if (record.getR17_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR17_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 17
					// Column I
					cell8 = row.createCell(8);
					if (record.getR17_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR17_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 17
					// Column J
					cell9 = row.createCell(9);
					if (record.getR17_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR17_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 17
					// Column K
					cell10 = row.createCell(10);
					if (record.getR17_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR17_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 17
					// Column L
					cell11 = row.createCell(11);
					if (record.getR17_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR17_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 17
					// Column M
					cell12 = row.createCell(12);
					if (record.getR17_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR17_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 17
					// Column N
					cell13 = row.createCell(13);
					if (record.getR17_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR17_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 18
					row = sheet.getRow(17);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR18_current() != null) {
						cell1.setCellValue(record.getR18_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 18
					// Column C
					cell2 = row.createCell(2);
					if (record.getR18_call() != null) {
						cell2.setCellValue(record.getR18_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 18
					// Column D
					cell3 = row.createCell(3);
					if (record.getR18_savings() != null) {
						cell3.setCellValue(record.getR18_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 18
					// Column E
					cell4 = row.createCell(4);
					if (record.getR18_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR18_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 18
					// Column F
					cell5 = row.createCell(5);
					if (record.getR18_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR18_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 18
					// Column G
					cell6 = row.createCell(6);
					if (record.getR18_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR18_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 18
					// Column H
					cell7 = row.createCell(7);
					if (record.getR18_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR18_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 18
					// Column I
					cell8 = row.createCell(8);
					if (record.getR18_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR18_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 18
					// Column J
					cell9 = row.createCell(9);
					if (record.getR18_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR18_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 18
					// Column K
					cell10 = row.createCell(10);
					if (record.getR18_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR18_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 18
					// Column L
					cell11 = row.createCell(11);
					if (record.getR18_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR18_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 18
					// Column M
					cell12 = row.createCell(12);
					if (record.getR18_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR18_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 18
					// Column N
					cell13 = row.createCell(13);
					if (record.getR18_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR18_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 19
					row = sheet.getRow(18);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR19_current() != null) {
						cell1.setCellValue(record.getR19_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 19
					// Column C
					cell2 = row.createCell(2);
					if (record.getR19_call() != null) {
						cell2.setCellValue(record.getR19_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 19
					// Column D
					cell3 = row.createCell(3);
					if (record.getR19_savings() != null) {
						cell3.setCellValue(record.getR19_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 19
					// Column E
					cell4 = row.createCell(4);
					if (record.getR19_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR19_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 19
					// Column F
					cell5 = row.createCell(5);
					if (record.getR19_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR19_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 19
					// Column G
					cell6 = row.createCell(6);
					if (record.getR19_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR19_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 19
					// Column H
					cell7 = row.createCell(7);
					if (record.getR19_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR19_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 19
					// Column I
					cell8 = row.createCell(8);
					if (record.getR19_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR19_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 19
					// Column J
					cell9 = row.createCell(9);
					if (record.getR19_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR19_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 19
					// Column K
					cell10 = row.createCell(10);
					if (record.getR19_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR19_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 19
					// Column L
					cell11 = row.createCell(11);
					if (record.getR19_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR19_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 19
					// Column M
					cell12 = row.createCell(12);
					if (record.getR19_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR19_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 19
					// Column N
					cell13 = row.createCell(13);
					if (record.getR19_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR19_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 20
					row = sheet.getRow(19);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR20_current() != null) {
						cell1.setCellValue(record.getR20_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 20
					// Column C
					cell2 = row.createCell(2);
					if (record.getR20_call() != null) {
						cell2.setCellValue(record.getR20_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 20
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_savings() != null) {
						cell3.setCellValue(record.getR20_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 20
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR20_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 20
					// Column F
					cell5 = row.createCell(5);
					if (record.getR20_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR20_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 20
					// Column G
					cell6 = row.createCell(6);
					if (record.getR20_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR20_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 20
					// Column H
					cell7 = row.createCell(7);
					if (record.getR20_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR20_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 20
					// Column I
					cell8 = row.createCell(8);
					if (record.getR20_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR20_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 20
					// Column J
					cell9 = row.createCell(9);
					if (record.getR20_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR20_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 20
					// Column K
					cell10 = row.createCell(10);
					if (record.getR20_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR20_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 20
					// Column L
					cell11 = row.createCell(11);
					if (record.getR20_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR20_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 20
					// Column M
					cell12 = row.createCell(12);
					if (record.getR20_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR20_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 20
					// Column N
					cell13 = row.createCell(13);
					if (record.getR20_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR20_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 21
					row = sheet.getRow(20);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR21_current() != null) {
						cell1.setCellValue(record.getR21_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 21
					// Column C
					cell2 = row.createCell(2);
					if (record.getR21_call() != null) {
						cell2.setCellValue(record.getR21_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 21
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_savings() != null) {
						cell3.setCellValue(record.getR21_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 21
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR21_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 21
					// Column F
					cell5 = row.createCell(5);
					if (record.getR21_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR21_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 21
					// Column G
					cell6 = row.createCell(6);
					if (record.getR21_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR21_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 21
					// Column H
					cell7 = row.createCell(7);
					if (record.getR21_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR21_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 21
					// Column I
					cell8 = row.createCell(8);
					if (record.getR21_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR21_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 21
					// Column J
					cell9 = row.createCell(9);
					if (record.getR21_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR21_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 21
					// Column K
					cell10 = row.createCell(10);
					if (record.getR21_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR21_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 21
					// Column L
					cell11 = row.createCell(11);
					if (record.getR21_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR21_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 21
					// Column M
					cell12 = row.createCell(12);
					if (record.getR21_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR21_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 21
					// Column N
					cell13 = row.createCell(13);
					if (record.getR21_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR21_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 22
					row = sheet.getRow(21);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR22_current() != null) {
						cell1.setCellValue(record.getR22_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 22
					// Column C
					cell2 = row.createCell(2);
					if (record.getR22_call() != null) {
						cell2.setCellValue(record.getR22_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 22
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_savings() != null) {
						cell3.setCellValue(record.getR22_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 22
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR22_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 22
					// Column F
					cell5 = row.createCell(5);
					if (record.getR22_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR22_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 22
					// Column G
					cell6 = row.createCell(6);
					if (record.getR22_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR22_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 22
					// Column H
					cell7 = row.createCell(7);
					if (record.getR22_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR22_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 22
					// Column I
					cell8 = row.createCell(8);
					if (record.getR22_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR22_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 22
					// Column J
					cell9 = row.createCell(9);
					if (record.getR22_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR22_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 22
					// Column K
					cell10 = row.createCell(10);
					if (record.getR22_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR22_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 22
					// Column L
					cell11 = row.createCell(11);
					if (record.getR22_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR22_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 22
					// Column M
					cell12 = row.createCell(12);
					if (record.getR22_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR22_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 22
					// Column N
					cell13 = row.createCell(13);
					if (record.getR22_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR22_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 23
					row = sheet.getRow(22);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR23_current() != null) {
						cell1.setCellValue(record.getR23_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 23
					// Column C
					cell2 = row.createCell(2);
					if (record.getR23_call() != null) {
						cell2.setCellValue(record.getR23_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 23
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_savings() != null) {
						cell3.setCellValue(record.getR23_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 23
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR23_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 23
					// Column F
					cell5 = row.createCell(5);
					if (record.getR23_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR23_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 23
					// Column G
					cell6 = row.createCell(6);
					if (record.getR23_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR23_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 23
					// Column H
					cell7 = row.createCell(7);
					if (record.getR23_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR23_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 23
					// Column I
					cell8 = row.createCell(8);
					if (record.getR23_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR23_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 23
					// Column J
					cell9 = row.createCell(9);
					if (record.getR23_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR23_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 23
					// Column K
					cell10 = row.createCell(10);
					if (record.getR23_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR23_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 23
					// Column L
					cell11 = row.createCell(11);
					if (record.getR23_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR23_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 23
					// Column M
					cell12 = row.createCell(12);
					if (record.getR23_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR23_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 23
					// Column N
					cell13 = row.createCell(13);
					if (record.getR23_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR23_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
				
					// Row 24
					row = sheet.getRow(23);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR24_current() != null) {
						cell1.setCellValue(record.getR24_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 24
					// Column C
					cell2 = row.createCell(2);
					if (record.getR24_call() != null) {
						cell2.setCellValue(record.getR24_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 24
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_savings() != null) {
						cell3.setCellValue(record.getR24_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 24
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR24_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 24
					// Column F
					cell5 = row.createCell(5);
					if (record.getR24_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR24_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 24
					// Column G
					cell6 = row.createCell(6);
					if (record.getR24_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR24_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 24
					// Column H
					cell7 = row.createCell(7);
					if (record.getR24_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR24_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 24
					// Column I
					cell8 = row.createCell(8);
					if (record.getR24_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR24_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 24
					// Column J
					cell9 = row.createCell(9);
					if (record.getR24_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR24_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 24
					// Column K
					cell10 = row.createCell(10);
					if (record.getR24_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR24_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 24
					// Column L
					cell11 = row.createCell(11);
					if (record.getR24_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR24_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 24
					// Column M
					cell12 = row.createCell(12);
					if (record.getR24_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR24_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 24
					// Column N
					cell13 = row.createCell(13);
					if (record.getR24_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR24_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 25
					row = sheet.getRow(24);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR25_current() != null) {
						cell1.setCellValue(record.getR25_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 25
					// Column C
					cell2 = row.createCell(2);
					if (record.getR25_call() != null) {
						cell2.setCellValue(record.getR25_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 25
					// Column D
					cell3 = row.createCell(3);
					if (record.getR25_savings() != null) {
						cell3.setCellValue(record.getR25_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 25
					// Column E
					cell4 = row.createCell(4);
					if (record.getR25_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR25_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 25
					// Column F
					cell5 = row.createCell(5);
					if (record.getR25_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR25_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 25
					// Column G
					cell6 = row.createCell(6);
					if (record.getR25_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR25_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 25
					// Column H
					cell7 = row.createCell(7);
					if (record.getR25_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR25_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 25
					// Column I
					cell8 = row.createCell(8);
					if (record.getR25_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR25_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 25
					// Column J
					cell9 = row.createCell(9);
					if (record.getR25_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR25_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 25
					// Column K
					cell10 = row.createCell(10);
					if (record.getR25_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR25_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 25
					// Column L
					cell11 = row.createCell(11);
					if (record.getR25_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR25_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 25
					// Column M
					cell12 = row.createCell(12);
					if (record.getR25_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR25_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 25
					// Column N
					cell13 = row.createCell(13);
					if (record.getR25_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR25_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
				
					// Row 26
					row = sheet.getRow(25);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR26_current() != null) {
						cell1.setCellValue(record.getR26_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 26
					// Column C
					cell2 = row.createCell(2);
					if (record.getR26_call() != null) {
						cell2.setCellValue(record.getR26_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 26
					// Column D
					cell3 = row.createCell(3);
					if (record.getR26_savings() != null) {
						cell3.setCellValue(record.getR26_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 26
					// Column E
					cell4 = row.createCell(4);
					if (record.getR26_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR26_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 26
					// Column F
					cell5 = row.createCell(5);
					if (record.getR26_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR26_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 26
					// Column G
					cell6 = row.createCell(6);
					if (record.getR26_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR26_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 26
					// Column H
					cell7 = row.createCell(7);
					if (record.getR26_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR26_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 26
					// Column I
					cell8 = row.createCell(8);
					if (record.getR26_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR26_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 26
					// Column J
					cell9 = row.createCell(9);
					if (record.getR26_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR26_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 26
					// Column K
					cell10 = row.createCell(10);
					if (record.getR26_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR26_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 26
					// Column L
					cell11 = row.createCell(11);
					if (record.getR26_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR26_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 26
					// Column M
					cell12 = row.createCell(12);
					if (record.getR26_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR26_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 26
					// Column N
					cell13 = row.createCell(13);
					if (record.getR26_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR26_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 27
					row = sheet.getRow(26);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR27_current() != null) {
						cell1.setCellValue(record.getR27_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 27
					// Column C
					cell2 = row.createCell(2);
					if (record.getR27_call() != null) {
						cell2.setCellValue(record.getR27_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 27
					// Column D
					cell3 = row.createCell(3);
					if (record.getR27_savings() != null) {
						cell3.setCellValue(record.getR27_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 27
					// Column E
					cell4 = row.createCell(4);
					if (record.getR27_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR27_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 27
					// Column F
					cell5 = row.createCell(5);
					if (record.getR27_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR27_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 27
					// Column G
					cell6 = row.createCell(6);
					if (record.getR27_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR27_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 27
					// Column H
					cell7 = row.createCell(7);
					if (record.getR27_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR27_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 27
					// Column I
					cell8 = row.createCell(8);
					if (record.getR27_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR27_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 27
					// Column J
					cell9 = row.createCell(9);
					if (record.getR27_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR27_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 27
					// Column K
					cell10 = row.createCell(10);
					if (record.getR27_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR27_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 27
					// Column L
					cell11 = row.createCell(11);
					if (record.getR27_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR27_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 27
					// Column M
					cell12 = row.createCell(12);
					if (record.getR27_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR27_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 27
					// Column N
					cell13 = row.createCell(13);
					if (record.getR27_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR27_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 28
					row = sheet.getRow(27);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR28_current() != null) {
						cell1.setCellValue(record.getR28_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 28
					// Column C
					cell2 = row.createCell(2);
					if (record.getR28_call() != null) {
						cell2.setCellValue(record.getR28_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 28
					// Column D
					cell3 = row.createCell(3);
					if (record.getR28_savings() != null) {
						cell3.setCellValue(record.getR28_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 28
					// Column E
					cell4 = row.createCell(4);
					if (record.getR28_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR28_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 28
					// Column F
					cell5 = row.createCell(5);
					if (record.getR28_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR28_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 28
					// Column G
					cell6 = row.createCell(6);
					if (record.getR28_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR28_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 28
					// Column H
					cell7 = row.createCell(7);
					if (record.getR28_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR28_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 28
					// Column I
					cell8 = row.createCell(8);
					if (record.getR28_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR28_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 28
					// Column J
					cell9 = row.createCell(9);
					if (record.getR28_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR28_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 28
					// Column K
					cell10 = row.createCell(10);
					if (record.getR28_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR28_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 28
					// Column L
					cell11 = row.createCell(11);
					if (record.getR28_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR28_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 28
					// Column M
					cell12 = row.createCell(12);
					if (record.getR28_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR28_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 28
					// Column N
					cell13 = row.createCell(13);
					if (record.getR28_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR28_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 29
					row = sheet.getRow(28);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR29_current() != null) {
						cell1.setCellValue(record.getR29_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 29
					// Column C
					cell2 = row.createCell(2);
					if (record.getR29_call() != null) {
						cell2.setCellValue(record.getR29_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 29
					// Column D
					cell3 = row.createCell(3);
					if (record.getR29_savings() != null) {
						cell3.setCellValue(record.getR29_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 29
					// Column E
					cell4 = row.createCell(4);
					if (record.getR29_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR29_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 29
					// Column F
					cell5 = row.createCell(5);
					if (record.getR29_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR29_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 29
					// Column G
					cell6 = row.createCell(6);
					if (record.getR29_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR29_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 29
					// Column H
					cell7 = row.createCell(7);
					if (record.getR29_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR29_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 29
					// Column I
					cell8 = row.createCell(8);
					if (record.getR29_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR29_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 29
					// Column J
					cell9 = row.createCell(9);
					if (record.getR29_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR29_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 29
					// Column K
					cell10 = row.createCell(10);
					if (record.getR29_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR29_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 29
					// Column L
					cell11 = row.createCell(11);
					if (record.getR29_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR29_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 29
					// Column M
					cell12 = row.createCell(12);
					if (record.getR29_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR29_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 29
					// Column N
					cell13 = row.createCell(13);
					if (record.getR29_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR29_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 31
					row = sheet.getRow(30);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR31_current() != null) {
						cell1.setCellValue(record.getR31_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 31
					// Column C
					cell2 = row.createCell(2);
					if (record.getR31_call() != null) {
						cell2.setCellValue(record.getR31_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 31
					// Column D
					cell3 = row.createCell(3);
					if (record.getR31_savings() != null) {
						cell3.setCellValue(record.getR31_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 31
					// Column E
					cell4 = row.createCell(4);
					if (record.getR31_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR31_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 31
					// Column F
					cell5 = row.createCell(5);
					if (record.getR31_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR31_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 31
					// Column G
					cell6 = row.createCell(6);
					if (record.getR31_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR31_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 31
					// Column H
					cell7 = row.createCell(7);
					if (record.getR31_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR31_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 31
					// Column I
					cell8 = row.createCell(8);
					if (record.getR31_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR31_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 31
					// Column J
					cell9 = row.createCell(9);
					if (record.getR31_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR31_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 31
					// Column K
					cell10 = row.createCell(10);
					if (record.getR31_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR31_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 31
					// Column L
					cell11 = row.createCell(11);
					if (record.getR31_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR31_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 31
					// Column M
					cell12 = row.createCell(12);
					if (record.getR31_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR31_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 31
					// Column N
					cell13 = row.createCell(13);
					if (record.getR31_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR31_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					
					// Row 32
					row = sheet.getRow(31);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR32_current() != null) {
						cell1.setCellValue(record.getR32_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 32
					// Column C
					cell2 = row.createCell(2);
					if (record.getR32_call() != null) {
						cell2.setCellValue(record.getR32_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 32
					// Column D
					cell3 = row.createCell(3);
					if (record.getR32_savings() != null) {
						cell3.setCellValue(record.getR32_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 32
					// Column E
					cell4 = row.createCell(4);
					if (record.getR32_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR32_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 32
					// Column F
					cell5 = row.createCell(5);
					if (record.getR32_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR32_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 32
					// Column G
					cell6 = row.createCell(6);
					if (record.getR32_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR32_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 32
					// Column H
					cell7 = row.createCell(7);
					if (record.getR32_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR32_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 32
					// Column I
					cell8 = row.createCell(8);
					if (record.getR32_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR32_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 32
					// Column J
					cell9 = row.createCell(9);
					if (record.getR32_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR32_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 32
					// Column K
					cell10 = row.createCell(10);
					if (record.getR32_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR32_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 32
					// Column L
					cell11 = row.createCell(11);
					if (record.getR32_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR32_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 32
					// Column M
					cell12 = row.createCell(12);
					if (record.getR32_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR32_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 32
					// Column N
					cell13 = row.createCell(13);
					if (record.getR32_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR32_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 34
					row = sheet.getRow(33);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR34_current() != null) {
						cell1.setCellValue(record.getR34_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 34
					// Column C
					cell2 = row.createCell(2);
					if (record.getR34_call() != null) {
						cell2.setCellValue(record.getR34_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 34
					// Column D
					cell3 = row.createCell(3);
					if (record.getR34_savings() != null) {
						cell3.setCellValue(record.getR34_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 34
					// Column E
					cell4 = row.createCell(4);
					if (record.getR34_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR34_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 34
					// Column F
					cell5 = row.createCell(5);
					if (record.getR34_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR34_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 34
					// Column G
					cell6 = row.createCell(6);
					if (record.getR34_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR34_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 34
					// Column H
					cell7 = row.createCell(7);
					if (record.getR34_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR34_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 34
					// Column I
					cell8 = row.createCell(8);
					if (record.getR34_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR34_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 34
					// Column J
					cell9 = row.createCell(9);
					if (record.getR34_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR34_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 34
					// Column K
					cell10 = row.createCell(10);
					if (record.getR34_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR34_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 34
					// Column L
					cell11 = row.createCell(11);
					if (record.getR34_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR34_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 34
					// Column M
					cell12 = row.createCell(12);
					if (record.getR34_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR34_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 34
					// Column N
					cell13 = row.createCell(13);
					if (record.getR34_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR34_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 35
					row = sheet.getRow(34);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR35_current() != null) {
						cell1.setCellValue(record.getR35_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 35
					// Column C
					cell2 = row.createCell(2);
					if (record.getR35_call() != null) {
						cell2.setCellValue(record.getR35_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 35
					// Column D
					cell3 = row.createCell(3);
					if (record.getR35_savings() != null) {
						cell3.setCellValue(record.getR35_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 35
					// Column E
					cell4 = row.createCell(4);
					if (record.getR35_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR35_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 35
					// Column F
					cell5 = row.createCell(5);
					if (record.getR35_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR35_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 35
					// Column G
					cell6 = row.createCell(6);
					if (record.getR35_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR35_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 35
					// Column H
					cell7 = row.createCell(7);
					if (record.getR35_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR35_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 35
					// Column I
					cell8 = row.createCell(8);
					if (record.getR35_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR35_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 35
					// Column J
					cell9 = row.createCell(9);
					if (record.getR35_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR35_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 35
					// Column K
					cell10 = row.createCell(10);
					if (record.getR35_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR35_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 35
					// Column L
					cell11 = row.createCell(11);
					if (record.getR35_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR35_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 35
					// Column M
					cell12 = row.createCell(12);
					if (record.getR35_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR35_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 35
					// Column N
					cell13 = row.createCell(13);
					if (record.getR35_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR35_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 37
					row = sheet.getRow(36);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR37_current() != null) {
						cell1.setCellValue(record.getR37_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 37
					// Column C
					cell2 = row.createCell(2);
					if (record.getR37_call() != null) {
						cell2.setCellValue(record.getR37_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 37
					// Column D
					cell3 = row.createCell(3);
					if (record.getR37_savings() != null) {
						cell3.setCellValue(record.getR37_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 37
					// Column E
					cell4 = row.createCell(4);
					if (record.getR37_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR37_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 37
					// Column F
					cell5 = row.createCell(5);
					if (record.getR37_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR37_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 37
					// Column G
					cell6 = row.createCell(6);
					if (record.getR37_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR37_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 37
					// Column H
					cell7 = row.createCell(7);
					if (record.getR37_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR37_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 37
					// Column I
					cell8 = row.createCell(8);
					if (record.getR37_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR37_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 37
					// Column J
					cell9 = row.createCell(9);
					if (record.getR37_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR37_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 37
					// Column K
					cell10 = row.createCell(10);
					if (record.getR37_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR37_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 37
					// Column L
					cell11 = row.createCell(11);
					if (record.getR37_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR37_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 37
					// Column M
					cell12 = row.createCell(12);
					if (record.getR37_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR37_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 37
					// Column N
					cell13 = row.createCell(13);
					if (record.getR37_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR37_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 38
					row = sheet.getRow(37);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR38_current() != null) {
						cell1.setCellValue(record.getR38_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 38
					// Column C
					cell2 = row.createCell(2);
					if (record.getR38_call() != null) {
						cell2.setCellValue(record.getR38_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 38
					// Column D
					cell3 = row.createCell(3);
					if (record.getR38_savings() != null) {
						cell3.setCellValue(record.getR38_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 38
					// Column E
					cell4 = row.createCell(4);
					if (record.getR38_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR38_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 38
					// Column F
					cell5 = row.createCell(5);
					if (record.getR38_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR38_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 38
					// Column G
					cell6 = row.createCell(6);
					if (record.getR38_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR38_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 38
					// Column H
					cell7 = row.createCell(7);
					if (record.getR38_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR38_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 38
					// Column I
					cell8 = row.createCell(8);
					if (record.getR38_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR38_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 38
					// Column J
					cell9 = row.createCell(9);
					if (record.getR38_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR38_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 38
					// Column K
					cell10 = row.createCell(10);
					if (record.getR38_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR38_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 38
					// Column L
					cell11 = row.createCell(11);
					if (record.getR38_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR38_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 38
					// Column M
					cell12 = row.createCell(12);
					if (record.getR38_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR38_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 38
					// Column N
					cell13 = row.createCell(13);
					if (record.getR38_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR38_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 39
					row = sheet.getRow(38);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR39_current() != null) {
						cell1.setCellValue(record.getR39_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 39
					// Column C
					cell2 = row.createCell(2);
					if (record.getR39_call() != null) {
						cell2.setCellValue(record.getR39_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 39
					// Column D
					cell3 = row.createCell(3);
					if (record.getR39_savings() != null) {
						cell3.setCellValue(record.getR39_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 39
					// Column E
					cell4 = row.createCell(4);
					if (record.getR39_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR39_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 39
					// Column F
					cell5 = row.createCell(5);
					if (record.getR39_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR39_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 39
					// Column G
					cell6 = row.createCell(6);
					if (record.getR39_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR39_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 39
					// Column H
					cell7 = row.createCell(7);
					if (record.getR39_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR39_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 39
					// Column I
					cell8 = row.createCell(8);
					if (record.getR39_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR39_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 39
					// Column J
					cell9 = row.createCell(9);
					if (record.getR39_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR39_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 39
					// Column K
					cell10 = row.createCell(10);
					if (record.getR39_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR39_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 39
					// Column L
					cell11 = row.createCell(11);
					if (record.getR39_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR39_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 39
					// Column M
					cell12 = row.createCell(12);
					if (record.getR39_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR39_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 39
					// Column N
					cell13 = row.createCell(13);
					if (record.getR39_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR39_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 40
					row = sheet.getRow(39);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR40_current() != null) {
						cell1.setCellValue(record.getR40_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 40
					// Column C
					cell2 = row.createCell(2);
					if (record.getR40_call() != null) {
						cell2.setCellValue(record.getR40_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 40
					// Column D
					cell3 = row.createCell(3);
					if (record.getR40_savings() != null) {
						cell3.setCellValue(record.getR40_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 40
					// Column E
					cell4 = row.createCell(4);
					if (record.getR40_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR40_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 40
					// Column F
					cell5 = row.createCell(5);
					if (record.getR40_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR40_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 40
					// Column G
					cell6 = row.createCell(6);
					if (record.getR40_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR40_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 40
					// Column H
					cell7 = row.createCell(7);
					if (record.getR40_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR40_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 40
					// Column I
					cell8 = row.createCell(8);
					if (record.getR40_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR40_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 40
					// Column J
					cell9 = row.createCell(9);
					if (record.getR40_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR40_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 40
					// Column K
					cell10 = row.createCell(10);
					if (record.getR40_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR40_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 40
					// Column L
					cell11 = row.createCell(11);
					if (record.getR40_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR40_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 40
					// Column M
					cell12 = row.createCell(12);
					if (record.getR40_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR40_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 40
					// Column N
					cell13 = row.createCell(13);
					if (record.getR40_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR40_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 42
					row = sheet.getRow(41);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR42_current() != null) {
						cell1.setCellValue(record.getR42_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 42
					// Column C
					cell2 = row.createCell(2);
					if (record.getR42_call() != null) {
						cell2.setCellValue(record.getR42_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 42
					// Column D
					cell3 = row.createCell(3);
					if (record.getR42_savings() != null) {
						cell3.setCellValue(record.getR42_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 42
					// Column E
					cell4 = row.createCell(4);
					if (record.getR42_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR42_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 42
					// Column F
					cell5 = row.createCell(5);
					if (record.getR42_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR42_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 42
					// Column G
					cell6 = row.createCell(6);
					if (record.getR42_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR42_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 42
					// Column H
					cell7 = row.createCell(7);
					if (record.getR42_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR42_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 42
					// Column I
					cell8 = row.createCell(8);
					if (record.getR42_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR42_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 42
					// Column J
					cell9 = row.createCell(9);
					if (record.getR42_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR42_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 42
					// Column K
					cell10 = row.createCell(10);
					if (record.getR42_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR42_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 42
					// Column L
					cell11 = row.createCell(11);
					if (record.getR42_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR42_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 42
					// Column M
					cell12 = row.createCell(12);
					if (record.getR42_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR42_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 42
					// Column N
					cell13 = row.createCell(13);
					if (record.getR42_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR42_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 43
					row = sheet.getRow(42);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR43_current() != null) {
						cell1.setCellValue(record.getR43_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 43
					// Column C
					cell2 = row.createCell(2);
					if (record.getR43_call() != null) {
						cell2.setCellValue(record.getR43_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 43
					// Column D
					cell3 = row.createCell(3);
					if (record.getR43_savings() != null) {
						cell3.setCellValue(record.getR43_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 43
					// Column E
					cell4 = row.createCell(4);
					if (record.getR43_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR43_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 43
					// Column F
					cell5 = row.createCell(5);
					if (record.getR43_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR43_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 43
					// Column G
					cell6 = row.createCell(6);
					if (record.getR43_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR43_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 43
					// Column H
					cell7 = row.createCell(7);
					if (record.getR43_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR43_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 43
					// Column I
					cell8 = row.createCell(8);
					if (record.getR43_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR43_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 43
					// Column J
					cell9 = row.createCell(9);
					if (record.getR43_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR43_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 43
					// Column K
					cell10 = row.createCell(10);
					if (record.getR43_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR43_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 43
					// Column L
					cell11 = row.createCell(11);
					if (record.getR43_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR43_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 43
					// Column M
					cell12 = row.createCell(12);
					if (record.getR43_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR43_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 43
					// Column N
					cell13 = row.createCell(13);
					if (record.getR43_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR43_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 44
					row = sheet.getRow(43);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR44_current() != null) {
						cell1.setCellValue(record.getR44_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 44
					// Column C
					cell2 = row.createCell(2);
					if (record.getR44_call() != null) {
						cell2.setCellValue(record.getR44_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 44
					// Column D
					cell3 = row.createCell(3);
					if (record.getR44_savings() != null) {
						cell3.setCellValue(record.getR44_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 44
					// Column E
					cell4 = row.createCell(4);
					if (record.getR44_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR44_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 44
					// Column F
					cell5 = row.createCell(5);
					if (record.getR44_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR44_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 44
					// Column G
					cell6 = row.createCell(6);
					if (record.getR44_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR44_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 44
					// Column H
					cell7 = row.createCell(7);
					if (record.getR44_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR44_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 44
					// Column I
					cell8 = row.createCell(8);
					if (record.getR44_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR44_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 44
					// Column J
					cell9 = row.createCell(9);
					if (record.getR44_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR44_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 44
					// Column K
					cell10 = row.createCell(10);
					if (record.getR44_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR44_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 44
					// Column L
					cell11 = row.createCell(11);
					if (record.getR44_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR44_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 44
					// Column M
					cell12 = row.createCell(12);
					if (record.getR44_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR44_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 44
					// Column N
					cell13 = row.createCell(13);
					if (record.getR44_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR44_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 46
					row = sheet.getRow(45);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR46_current() != null) {
						cell1.setCellValue(record.getR46_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 46
					// Column C
					cell2 = row.createCell(2);
					if (record.getR46_call() != null) {
						cell2.setCellValue(record.getR46_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 46
					// Column D
					cell3 = row.createCell(3);
					if (record.getR46_savings() != null) {
						cell3.setCellValue(record.getR46_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 46
					// Column E
					cell4 = row.createCell(4);
					if (record.getR46_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR46_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 46
					// Column F
					cell5 = row.createCell(5);
					if (record.getR46_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR46_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 46
					// Column G
					cell6 = row.createCell(6);
					if (record.getR46_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR46_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 46
					// Column H
					cell7 = row.createCell(7);
					if (record.getR46_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR46_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 46
					// Column I
					cell8 = row.createCell(8);
					if (record.getR46_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR46_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 46
					// Column J
					cell9 = row.createCell(9);
					if (record.getR46_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR46_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 46
					// Column K
					cell10 = row.createCell(10);
					if (record.getR46_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR46_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 46
					// Column L
					cell11 = row.createCell(11);
					if (record.getR46_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR46_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 46
					// Column M
					cell12 = row.createCell(12);
					if (record.getR46_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR46_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 46
					// Column N
					cell13 = row.createCell(13);
					if (record.getR46_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR46_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 47
					row = sheet.getRow(46);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR47_current() != null) {
						cell1.setCellValue(record.getR47_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 47
					// Column C
					cell2 = row.createCell(2);
					if (record.getR47_call() != null) {
						cell2.setCellValue(record.getR47_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 47
					// Column D
					cell3 = row.createCell(3);
					if (record.getR47_savings() != null) {
						cell3.setCellValue(record.getR47_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 47
					// Column E
					cell4 = row.createCell(4);
					if (record.getR47_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR47_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 47
					// Column F
					cell5 = row.createCell(5);
					if (record.getR47_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR47_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 47
					// Column G
					cell6 = row.createCell(6);
					if (record.getR47_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR47_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 47
					// Column H
					cell7 = row.createCell(7);
					if (record.getR47_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR47_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 47
					// Column I
					cell8 = row.createCell(8);
					if (record.getR47_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR47_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 47
					// Column J
					cell9 = row.createCell(9);
					if (record.getR47_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR47_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 47
					// Column K
					cell10 = row.createCell(10);
					if (record.getR47_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR47_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 47
					// Column L
					cell11 = row.createCell(11);
					if (record.getR47_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR47_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 47
					// Column M
					cell12 = row.createCell(12);
					if (record.getR47_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR47_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 47
					// Column N
					cell13 = row.createCell(13);
					if (record.getR47_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR47_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 48
					row = sheet.getRow(47);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR48_current() != null) {
						cell1.setCellValue(record.getR48_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 48
					// Column C
					cell2 = row.createCell(2);
					if (record.getR48_call() != null) {
						cell2.setCellValue(record.getR48_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 48
					// Column D
					cell3 = row.createCell(3);
					if (record.getR48_savings() != null) {
						cell3.setCellValue(record.getR48_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 48
					// Column E
					cell4 = row.createCell(4);
					if (record.getR48_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR48_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 48
					// Column F
					cell5 = row.createCell(5);
					if (record.getR48_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR48_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 48
					// Column G
					cell6 = row.createCell(6);
					if (record.getR48_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR48_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 48
					// Column H
					cell7 = row.createCell(7);
					if (record.getR48_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR48_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 48
					// Column I
					cell8 = row.createCell(8);
					if (record.getR48_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR48_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 48
					// Column J
					cell9 = row.createCell(9);
					if (record.getR48_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR48_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 48
					// Column K
					cell10 = row.createCell(10);
					if (record.getR48_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR48_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 48
					// Column L
					cell11 = row.createCell(11);
					if (record.getR48_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR48_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 48
					// Column M
					cell12 = row.createCell(12);
					if (record.getR48_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR48_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 48
					// Column N
					cell13 = row.createCell(13);
					if (record.getR48_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR48_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 50
					row = sheet.getRow(49);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR50_current() != null) {
						cell1.setCellValue(record.getR50_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 50
					// Column C
					cell2 = row.createCell(2);
					if (record.getR50_call() != null) {
						cell2.setCellValue(record.getR50_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 50
					// Column D
					cell3 = row.createCell(3);
					if (record.getR50_savings() != null) {
						cell3.setCellValue(record.getR50_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 50
					// Column E
					cell4 = row.createCell(4);
					if (record.getR50_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR50_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 50
					// Column F
					cell5 = row.createCell(5);
					if (record.getR50_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR50_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 50
					// Column G
					cell6 = row.createCell(6);
					if (record.getR50_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR50_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 50
					// Column H
					cell7 = row.createCell(7);
					if (record.getR50_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR50_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 50
					// Column I
					cell8 = row.createCell(8);
					if (record.getR50_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR50_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 50
					// Column J
					cell9 = row.createCell(9);
					if (record.getR50_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR50_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 50
					// Column K
					cell10 = row.createCell(10);
					if (record.getR50_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR50_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 50
					// Column L
					cell11 = row.createCell(11);
					if (record.getR50_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR50_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 50
					// Column M
					cell12 = row.createCell(12);
					if (record.getR50_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR50_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 50
					// Column N
					cell13 = row.createCell(13);
					if (record.getR50_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR50_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 51
					row = sheet.getRow(50);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR51_current() != null) {
						cell1.setCellValue(record.getR51_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 51
					// Column C
					cell2 = row.createCell(2);
					if (record.getR51_call() != null) {
						cell2.setCellValue(record.getR51_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 51
					// Column D
					cell3 = row.createCell(3);
					if (record.getR51_savings() != null) {
						cell3.setCellValue(record.getR51_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 51
					// Column E
					cell4 = row.createCell(4);
					if (record.getR51_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR51_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 51
					// Column F
					cell5 = row.createCell(5);
					if (record.getR51_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR51_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 51
					// Column G
					cell6 = row.createCell(6);
					if (record.getR51_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR51_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 51
					// Column H
					cell7 = row.createCell(7);
					if (record.getR51_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR51_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 51
					// Column I
					cell8 = row.createCell(8);
					if (record.getR51_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR51_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 51
					// Column J
					cell9 = row.createCell(9);
					if (record.getR51_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR51_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 51
					// Column K
					cell10 = row.createCell(10);
					if (record.getR51_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR51_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 51
					// Column L
					cell11 = row.createCell(11);
					if (record.getR51_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR51_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 51
					// Column M
					cell12 = row.createCell(12);
					if (record.getR51_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR51_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 51
					// Column N
					cell13 = row.createCell(13);
					if (record.getR51_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR51_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 52
					row = sheet.getRow(51);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR52_current() != null) {
						cell1.setCellValue(record.getR52_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 52
					// Column C
					cell2 = row.createCell(2);
					if (record.getR52_call() != null) {
						cell2.setCellValue(record.getR52_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 52
					// Column D
					cell3 = row.createCell(3);
					if (record.getR52_savings() != null) {
						cell3.setCellValue(record.getR52_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 52
					// Column E
					cell4 = row.createCell(4);
					if (record.getR52_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR52_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 52
					// Column F
					cell5 = row.createCell(5);
					if (record.getR52_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR52_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 52
					// Column G
					cell6 = row.createCell(6);
					if (record.getR52_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR52_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 52
					// Column H
					cell7 = row.createCell(7);
					if (record.getR52_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR52_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 52
					// Column I
					cell8 = row.createCell(8);
					if (record.getR52_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR52_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 52
					// Column J
					cell9 = row.createCell(9);
					if (record.getR52_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR52_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 52
					// Column K
					cell10 = row.createCell(10);
					if (record.getR52_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR52_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 52
					// Column L
					cell11 = row.createCell(11);
					if (record.getR52_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR52_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 52
					// Column M
					cell12 = row.createCell(12);
					if (record.getR52_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR52_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 52
					// Column N
					cell13 = row.createCell(13);
					if (record.getR52_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR52_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 53
					row = sheet.getRow(52);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR53_current() != null) {
						cell1.setCellValue(record.getR53_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 53
					// Column C
					cell2 = row.createCell(2);
					if (record.getR53_call() != null) {
						cell2.setCellValue(record.getR53_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 53
					// Column D
					cell3 = row.createCell(3);
					if (record.getR53_savings() != null) {
						cell3.setCellValue(record.getR53_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 53
					// Column E
					cell4 = row.createCell(4);
					if (record.getR53_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR53_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 53
					// Column F
					cell5 = row.createCell(5);
					if (record.getR53_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR53_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 53
					// Column G
					cell6 = row.createCell(6);
					if (record.getR53_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR53_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 53
					// Column H
					cell7 = row.createCell(7);
					if (record.getR53_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR53_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 53
					// Column I
					cell8 = row.createCell(8);
					if (record.getR53_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR53_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 53
					// Column J
					cell9 = row.createCell(9);
					if (record.getR53_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR53_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 53
					// Column K
					cell10 = row.createCell(10);
					if (record.getR53_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR53_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 53
					// Column L
					cell11 = row.createCell(11);
					if (record.getR53_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR53_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 53
					// Column M
					cell12 = row.createCell(12);
					if (record.getR53_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR53_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 53
					// Column N
					cell13 = row.createCell(13);
					if (record.getR53_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR53_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 54
					row = sheet.getRow(53);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR54_current() != null) {
						cell1.setCellValue(record.getR54_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 54
					// Column C
					cell2 = row.createCell(2);
					if (record.getR54_call() != null) {
						cell2.setCellValue(record.getR54_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 54
					// Column D
					cell3 = row.createCell(3);
					if (record.getR54_savings() != null) {
						cell3.setCellValue(record.getR54_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 54
					// Column E
					cell4 = row.createCell(4);
					if (record.getR54_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR54_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 54
					// Column F
					cell5 = row.createCell(5);
					if (record.getR54_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR54_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 54
					// Column G
					cell6 = row.createCell(6);
					if (record.getR54_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR54_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 54
					// Column H
					cell7 = row.createCell(7);
					if (record.getR54_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR54_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 54
					// Column I
					cell8 = row.createCell(8);
					if (record.getR54_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR54_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 54
					// Column J
					cell9 = row.createCell(9);
					if (record.getR54_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR54_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 54
					// Column K
					cell10 = row.createCell(10);
					if (record.getR54_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR54_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 54
					// Column L
					cell11 = row.createCell(11);
					if (record.getR54_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR54_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 54
					// Column M
					cell12 = row.createCell(12);
					if (record.getR54_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR54_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 54
					// Column N
					cell13 = row.createCell(13);
					if (record.getR54_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR54_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 55
					row = sheet.getRow(54);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR55_current() != null) {
						cell1.setCellValue(record.getR55_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 55
					// Column C
					cell2 = row.createCell(2);
					if (record.getR55_call() != null) {
						cell2.setCellValue(record.getR55_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 55
					// Column D
					cell3 = row.createCell(3);
					if (record.getR55_savings() != null) {
						cell3.setCellValue(record.getR55_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 55
					// Column E
					cell4 = row.createCell(4);
					if (record.getR55_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR55_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 55
					// Column F
					cell5 = row.createCell(5);
					if (record.getR55_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR55_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 55
					// Column G
					cell6 = row.createCell(6);
					if (record.getR55_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR55_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 55
					// Column H
					cell7 = row.createCell(7);
					if (record.getR55_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR55_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 55
					// Column I
					cell8 = row.createCell(8);
					if (record.getR55_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR55_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 55
					// Column J
					cell9 = row.createCell(9);
					if (record.getR55_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR55_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 55
					// Column K
					cell10 = row.createCell(10);
					if (record.getR55_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR55_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 55
					// Column L
					cell11 = row.createCell(11);
					if (record.getR55_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR55_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 55
					// Column M
					cell12 = row.createCell(12);
					if (record.getR55_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR55_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 55
					// Column N
					cell13 = row.createCell(13);
					if (record.getR55_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR55_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					//Row 57
					row = sheet.getRow(56);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR57_current() != null) {
						cell1.setCellValue(record.getR57_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					//Row 57
					// Column C
					cell2 = row.createCell(2);
					if (record.getR57_call() != null) {
						cell2.setCellValue(record.getR57_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//Row 57
					// Column D
					cell3 = row.createCell(3);
					if (record.getR57_savings() != null) {
						cell3.setCellValue(record.getR57_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//Row 57
					// Column E
					cell4 = row.createCell(4);
					if (record.getR57_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR57_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//Row 57
					// Column F
					cell5 = row.createCell(5);
					if (record.getR57_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR57_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					//Row 57
					// Column G
					cell6 = row.createCell(6);
					if (record.getR57_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR57_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					//Row 57
					// Column H
					cell7 = row.createCell(7);
					if (record.getR57_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR57_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					//Row 57
					// Column I
					cell8 = row.createCell(8);
					if (record.getR57_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR57_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					//Row 57
					// Column J
					cell9 = row.createCell(9);
					if (record.getR57_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR57_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					//Row 57
					// Column K
					cell10 = row.createCell(10);
					if (record.getR57_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR57_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					//Row 57
					// Column L
					cell11 = row.createCell(11);
					if (record.getR57_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR57_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					//Row 57
					// Column M
					cell12 = row.createCell(12);
					if (record.getR57_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR57_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					//Row 57
					// Column N
					cell13 = row.createCell(13);
					if (record.getR57_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR57_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
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
			logger.info("Generating Excel for BRRS_M_DEP2 ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("MDEP2Detail");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT_LABEL", "REPORT_ADDL_CRITERIA_1",
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
			List<M_DEP2_Archival_Detail_Entity> reportData = M_DEP2_Archival_Detail_Repo.getdatabydateList(parsedToDate,
					version);
			System.out.println("Size");
			System.out.println(reportData.size());
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_DEP2_Archival_Detail_Entity item : reportData) {
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
				logger.info("No data found for BRRS_M_DEP2 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_M_DEP2Excel", e);
			return new byte[0];
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/M_DEP2"); // ✅ match the report name
	    System.out.println("Hello");
	    if (acctNo != null) {
	    	M_DEP2_Detail_Entity la1Entity = M_DEP2_Detail_Repo.findByAcctnumber(acctNo);
	        if (la1Entity != null && la1Entity.getReportDate() != null) {
	            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(la1Entity.getReportDate());
	            mv.addObject("asondate", formattedDate);
	        }
	        mv.addObject("Data", la1Entity);
	    }

	    mv.addObject("displaymode", "edit");
	    mv.addObject("formmode", formMode != null ? formMode : "edit");
	    return mv;
	}


	
public ModelAndView updateDetailEdit(String acctNo, String formMode) {
    ModelAndView mv = new ModelAndView("BRRS/M_DEP2"); // ✅ match the report name

    if (acctNo != null) {
        M_DEP2_Detail_Entity la1Entity = M_DEP2_Detail_Repo.findByAcctnumber(acctNo);
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

        M_DEP2_Detail_Entity existing = M_DEP2_Detail_Repo.findByAcctnumber(acctNo);
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
        	M_DEP2_Detail_Repo.save(existing);
            logger.info("Record updated successfully for account {}", acctNo);

            // Format date for procedure
            String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
                    .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

            // Run summary procedure after commit
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    try {
                        logger.info("Transaction committed — calling BRRS_M_DEP2_SUMMARY_PROCEDURE({})",
                                formattedDate);
                        jdbcTemplate.update("BEGIN BRRS_M_DEP2_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
        logger.error("Error updating M_DEP2 record", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error updating record: " + e.getMessage());
    }
}


}
