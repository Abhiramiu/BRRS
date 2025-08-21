package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.bornfire.brrs.entities.CBUAE_BRF1_2_Detail_Entity;
import com.bornfire.brrs.entities.CBUAE_BRF1_2_Detail_Repo;
import com.bornfire.brrs.entities.CBUAE_BRF1_2_Summary_Entity1;
import com.bornfire.brrs.entities.CBUAE_BRF1_2_Summary_Entity2;
import com.bornfire.brrs.entities.CBUAE_BRF1_2_Summary_Repo1;
import com.bornfire.brrs.entities.CBUAE_BRF1_2_Summary_Repo2;

@Component
@Service

public class CBUAE_BRF1_2_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(CBUAE_BRF1_2_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	CBUAE_BRF1_2_Detail_Repo CBUAE_BRF1_2_DETAIL_REPO;

	@Autowired
	CBUAE_BRF1_2_Summary_Repo1 CBUAE_BRF1_2_REPORT_REPO1;

	@Autowired
	CBUAE_BRF1_2_Summary_Repo2 CBUAE_BRF1_2_REPORT_REPO2;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRF1_2View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		List<CBUAE_BRF1_2_Summary_Entity1> T1Master = new ArrayList<CBUAE_BRF1_2_Summary_Entity1>();
		List<CBUAE_BRF1_2_Summary_Entity2> T1Master1 = new ArrayList<CBUAE_BRF1_2_Summary_Entity2>();
		try {
			Date d1 = dateformat.parse(todate);

			T1Master = CBUAE_BRF1_2_REPORT_REPO1.getdatabydateList(dateformat.parse(todate));
			T1Master1 = CBUAE_BRF1_2_REPORT_REPO2.getdatabydateList(dateformat.parse(todate));

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRF/BRF1_2");
		mv.addObject("reportsummary", T1Master);
		mv.addObject("reportsummary1", T1Master1);
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());

		return mv;

	}

	public ModelAndView getBRF1_2currentDtl(
	        String reportId, String fromdate, String todate, String currency,
	        String dtltype, Pageable pageable, String filter) {

	    int pageSize = pageable.getPageSize();
	    int currentPage = pageable.getPageNumber();

	    ModelAndView mv = new ModelAndView();
	    List<CBUAE_BRF1_2_Detail_Entity> T1Dt1 = new ArrayList<>();

	    try {
	        Date d1 = dateformat.parse(todate);

	        String rowId = null;
	        String columnId = null;

	        // ✅ Split the filter string here
	        if (filter != null && filter.contains(",")) {
	            String[] parts = filter.split(",");
	            if (parts.length >= 2) {
	                rowId = parts[0];
	                columnId = parts[1];
	            }
	        }

	        if (rowId != null && columnId != null) {
	        	T1Dt1 = CBUAE_BRF1_2_DETAIL_REPO.GetDataByRowIdAndColumnId(rowId, columnId, dateformat.parse(todate));
	        } else {
	            T1Dt1 = CBUAE_BRF1_2_DETAIL_REPO.getdatabydateList(d1);
	        }

	        System.out.println("LISTCOUNT: " + T1Dt1.size());

	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	    mv.setViewName("BRF/BRF1_2");
	    mv.addObject("displaymode", "Details");
	    mv.addObject("reportdetails", T1Dt1);
	    mv.addObject("reportmaster12", T1Dt1);
	    mv.addObject("reportsflag", "reportsflag");
	    mv.addObject("menu", reportId);
	    return mv;
	}

	public byte[] getBRF1_2Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		List<CBUAE_BRF1_2_Summary_Entity1> dataList = CBUAE_BRF1_2_REPORT_REPO1
				.getdatabydateList(dateformat.parse(todate));
		List<CBUAE_BRF1_2_Summary_Entity2> dataList1 = CBUAE_BRF1_2_REPORT_REPO2
				.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRF1.2 report. Returning empty result.");
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

					CBUAE_BRF1_2_Summary_Entity1 record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					///// ROW13///////////
					// row13
					// Column E:
					Cell cell4 = row.createCell(4);
					if (record.getR0020_no_acct_aed_resident() != null) {
						cell4.setCellValue(record.getR0020_no_acct_aed_resident().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row13
					// Column F:
					Cell cell5 = row.createCell(5);
					if (record.getR0020_amount_aed_resident() != null) {
						cell5.setCellValue(record.getR0020_amount_aed_resident().doubleValue());

						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row13
					// Column G:
					Cell cell6 = row.createCell(6);
					if (record.getR0020_no_acct_fcy_resident() != null) {
						cell6.setCellValue(record.getR0020_no_acct_fcy_resident().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row13
					// Column H:
					Cell cell7 = row.createCell(7);
					if (record.getR0020_amount_fcy_resident() != null) {
						cell7.setCellValue(record.getR0020_amount_fcy_resident().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row13
					// Column I:
					Cell cell8 = row.createCell(8);
					if (record.getR0020_no_acct_aed_non_resident() != null) {
						cell8.setCellValue(record.getR0020_no_acct_aed_non_resident().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row13
					// Column J:
					Cell cell9 = row.createCell(9);
					if (record.getR0020_amount_aed_non_resident() != null) {
						cell9.setCellValue(record.getR0020_amount_aed_non_resident().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// row13
					// Column K:
					Cell cell10 = row.createCell(10);
					if (record.getR0020_no_acct_fcy_non_resident() != null) {
						cell10.setCellValue(record.getR0020_no_acct_fcy_non_resident().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// row13
					// Column L:
					Cell cell11 = row.createCell(11);
					if (record.getR0020_amount_fcy_non_resident() != null) {
						cell11.setCellValue(record.getR0020_amount_fcy_non_resident().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					///// ROW14///////////
					// row14
					// Column E:

					row = sheet.getRow(13);
					Cell R1cell4 = row.createCell(4);
					if (record.getR0030_no_acct_aed_resident() != null) {
						R1cell4.setCellValue(record.getR0030_no_acct_aed_resident().doubleValue());
						R1cell4.setCellStyle(numberStyle);
					} else {
						R1cell4.setCellValue("");
						R1cell4.setCellStyle(textStyle);
					}

					// row14
					// Column F:
					Cell R1cell5 = row.createCell(5);
					if (record.getR0030_amount_aed_resident() != null) {
						R1cell5.setCellValue(record.getR0030_amount_aed_resident().doubleValue());
						R1cell5.setCellStyle(numberStyle);
					} else {
						R1cell5.setCellValue("");
						R1cell5.setCellStyle(textStyle);
					}

					// row14
					// Column G:
					Cell R1cell6 = row.createCell(6);
					if (record.getR0030_no_acct_fcy_resident() != null) {
						R1cell6.setCellValue(record.getR0030_no_acct_fcy_resident().doubleValue());
						R1cell6.setCellStyle(numberStyle);
					} else {
						R1cell6.setCellValue("");
						R1cell6.setCellStyle(textStyle);
					}

					// row14
					// Column H:
					Cell R1cell7 = row.createCell(7);
					if (record.getR0030_amount_fcy_resident() != null) {
						R1cell7.setCellValue(record.getR0030_amount_fcy_resident().doubleValue());
						R1cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row14
					// Column I:
					Cell R1cell8 = row.createCell(8);
					if (record.getR0030_no_acct_aed_non_resident() != null) {
						R1cell8.setCellValue(record.getR0030_no_acct_aed_non_resident().doubleValue());
						R1cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row14
					// Column J:
					Cell R1cell9 = row.createCell(9);
					if (record.getR0030_amount_aed_non_resident() != null) {
						R1cell9.setCellValue(record.getR0030_amount_aed_non_resident().doubleValue());
						R1cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// row14
					// Column K:
					Cell R1cell10 = row.createCell(10);
					if (record.getR0030_no_acct_fcy_non_resident() != null) {
						R1cell10.setCellValue(record.getR0030_no_acct_fcy_non_resident().doubleValue());
						R1cell10.setCellStyle(numberStyle);
					} else {
						R1cell10.setCellValue("");
						R1cell10.setCellStyle(textStyle);
					}

					// row14
					// Column L:
					Cell R1cell11 = row.createCell(11);
					if (record.getR0030_amount_fcy_non_resident() != null) {
						R1cell11.setCellValue(record.getR0030_amount_fcy_non_resident().doubleValue());
						R1cell11.setCellStyle(numberStyle);
					} else {
						R1cell11.setCellValue("");
						R1cell11.setCellStyle(textStyle);
					}

					///// ROW15///////////
					// row15
					// Column E:

					row = sheet.getRow(14);
					Cell R2cell4 = row.createCell(4);
					if (record.getR0040_no_acct_aed_resident() != null) {
						R2cell4.setCellValue(record.getR0040_no_acct_aed_resident().doubleValue());
						R2cell4.setCellStyle(numberStyle);
					} else {
						R2cell4.setCellValue("");
						R2cell4.setCellStyle(textStyle);
					}

					// row15
					// Column F:
					Cell R2cell5 = row.createCell(5);
					if (record.getR0040_amount_aed_resident() != null) {
						R2cell5.setCellValue(record.getR0040_amount_aed_resident().doubleValue());
						R2cell5.setCellStyle(numberStyle);
					} else {
						R2cell5.setCellValue("");
						R2cell5.setCellStyle(textStyle);
					}

					// row15
					// Column G:
					Cell R2cell6 = row.createCell(6);
					if (record.getR0040_no_acct_fcy_resident() != null) {
						R2cell6.setCellValue(record.getR0040_no_acct_fcy_resident().doubleValue());
						R2cell6.setCellStyle(numberStyle);
					} else {
						R2cell6.setCellValue("");
						R2cell6.setCellStyle(textStyle);
					}

					// row15
					// Column H:
					Cell R2cell7 = row.createCell(7);
					if (record.getR0040_amount_fcy_resident() != null) {
						R2cell7.setCellValue(record.getR0040_amount_fcy_resident().doubleValue());
						R2cell7.setCellStyle(numberStyle);
					} else {
						R2cell7.setCellValue("");
						R2cell7.setCellStyle(textStyle);
					}

					// row15
					// Column I:
					Cell R2cell8 = row.createCell(8);
					if (record.getR0040_no_acct_aed_non_resident() != null) {
						R2cell8.setCellValue(record.getR0040_no_acct_aed_non_resident().doubleValue());
						R2cell8.setCellStyle(numberStyle);
					} else {
						R2cell8.setCellValue("");
						R2cell8.setCellStyle(textStyle);
					}

					// row15
					// Column J:
					Cell R2cell9 = row.createCell(9);
					if (record.getR0040_amount_aed_non_resident() != null) {
						R2cell9.setCellValue(record.getR0040_amount_aed_non_resident().doubleValue());
						R2cell9.setCellStyle(numberStyle);
					} else {
						R2cell9.setCellValue("");
						R2cell9.setCellStyle(textStyle);
					}

					// row15
					// Column K:
					Cell R2cell10 = row.createCell(10);
					if (record.getR0040_no_acct_fcy_non_resident() != null) {
						R2cell10.setCellValue(record.getR0040_no_acct_fcy_non_resident().doubleValue());
						R2cell10.setCellStyle(numberStyle);
					} else {
						R2cell10.setCellValue("");
						R2cell10.setCellStyle(textStyle);
					}

					// row15
					// Column L:
					Cell R2cell11 = row.createCell(11);
					if (record.getR0040_amount_fcy_non_resident() != null) {
						R2cell11.setCellValue(record.getR0040_amount_fcy_non_resident().doubleValue());
						R2cell11.setCellStyle(numberStyle);
					} else {
						R2cell11.setCellValue("");
						R2cell11.setCellStyle(textStyle);
					}

					///// ROW16///////////
					// row16
					// Column E:

					row = sheet.getRow(15);
					Cell R3cell4 = row.createCell(4);
					if (record.getR0050_no_acct_aed_resident() != null) {
						R3cell4.setCellValue(record.getR0050_no_acct_aed_resident().doubleValue());
						R3cell4.setCellStyle(numberStyle);
					} else {
						R3cell4.setCellValue("");
						R3cell4.setCellStyle(textStyle);
					}

					// row16
					// Column F:
					Cell R3cell5 = row.createCell(5);
					if (record.getR0050_amount_aed_resident() != null) {
						R3cell5.setCellValue(record.getR0050_amount_aed_resident().doubleValue());
						R3cell5.setCellStyle(numberStyle);
					} else {
						R3cell5.setCellValue("");
						R3cell5.setCellStyle(textStyle);
					}

					// row16
					// Column G:
					Cell R3cell6 = row.createCell(6);
					if (record.getR0050_no_acct_fcy_resident() != null) {
						R3cell6.setCellValue(record.getR0050_no_acct_fcy_resident().doubleValue());
						R3cell6.setCellStyle(numberStyle);
					} else {
						R2cell6.setCellValue("");
						R2cell6.setCellStyle(textStyle);
					}

					// row16
					// Column H:
					Cell R3cell7 = row.createCell(7);
					if (record.getR0050_amount_fcy_resident() != null) {
						R3cell7.setCellValue(record.getR0050_amount_fcy_resident().doubleValue());
						R3cell7.setCellStyle(numberStyle);
					} else {
						R3cell7.setCellValue("");
						R3cell7.setCellStyle(textStyle);
					}

					// row16
					// Column I:
					Cell R3cell8 = row.createCell(8);
					if (record.getR0050_no_acct_aed_non_resident() != null) {
						R3cell8.setCellValue(record.getR0050_no_acct_aed_non_resident().doubleValue());
						R3cell8.setCellStyle(numberStyle);
					} else {
						R3cell8.setCellValue("");
						R3cell8.setCellStyle(textStyle);
					}

					// row16
					// Column J:
					Cell R3cell9 = row.createCell(9);
					if (record.getR0050_amount_aed_non_resident() != null) {
						R3cell9.setCellValue(record.getR0050_amount_aed_non_resident().doubleValue());
						R3cell9.setCellStyle(numberStyle);
					} else {
						R3cell9.setCellValue("");
						R3cell9.setCellStyle(textStyle);
					}

					// row16
					// Column K:
					Cell R3cell10 = row.createCell(10);
					if (record.getR0050_no_acct_fcy_non_resident() != null) {
						R3cell10.setCellValue(record.getR0050_no_acct_fcy_non_resident().doubleValue());
						R3cell10.setCellStyle(numberStyle);
					} else {
						R3cell10.setCellValue("");
						R3cell10.setCellStyle(textStyle);
					}

					// row16
					// Column L:
					Cell R3cell11 = row.createCell(11);
					if (record.getR0050_amount_fcy_non_resident() != null) {
						R3cell11.setCellValue(record.getR0050_amount_fcy_non_resident().doubleValue());
						R3cell11.setCellStyle(numberStyle);
					} else {
						R3cell11.setCellValue("");
						R3cell11.setCellStyle(textStyle);
					}

					///// ROW18///////////
					// ROW18
					// Column E:

					row = sheet.getRow(17);
					Cell R4cell4 = row.createCell(4);
					if (record.getR0070_no_acct_aed_resident() != null) {
						R4cell4.setCellValue(record.getR0070_no_acct_aed_resident().doubleValue());
						R4cell4.setCellStyle(numberStyle);
					} else {
						R4cell4.setCellValue("");
						R4cell4.setCellStyle(textStyle);
					}

					// ROW18
					// Column F:
					Cell R4cell5 = row.createCell(5);
					if (record.getR0070_amount_aed_resident() != null) {
						R4cell5.setCellValue(record.getR0070_amount_aed_resident().doubleValue());
						R4cell5.setCellStyle(numberStyle);
					} else {
						R4cell5.setCellValue("");
						R4cell5.setCellStyle(textStyle);
					}

					// ROW18
					// Column G:
					Cell R4cell6 = row.createCell(6);
					if (record.getR0070_no_acct_fcy_resident() != null) {
						R4cell6.setCellValue(record.getR0070_no_acct_fcy_resident().doubleValue());
						R4cell6.setCellStyle(numberStyle);
					} else {
						R4cell6.setCellValue("");
						R4cell6.setCellStyle(textStyle);
					}

					// ROW18
					// Column H:
					Cell R4cell7 = row.createCell(7);
					if (record.getR0070_amount_fcy_resident() != null) {
						R4cell7.setCellValue(record.getR0070_amount_fcy_resident().doubleValue());
						R4cell7.setCellStyle(numberStyle);
					} else {
						R4cell7.setCellValue("");
						R4cell7.setCellStyle(textStyle);
					}

					// ROW18
					// Column I:
					Cell R4cell8 = row.createCell(8);
					if (record.getR0070_no_acct_aed_non_resident() != null) {
						R4cell8.setCellValue(record.getR0070_no_acct_aed_non_resident().doubleValue());
						R4cell8.setCellStyle(numberStyle);
					} else {
						R4cell8.setCellValue("");
						R4cell8.setCellStyle(textStyle);
					}

					// ROW18
					// Column J:
					Cell R4cell9 = row.createCell(9);
					if (record.getR0070_amount_aed_non_resident() != null) {
						R4cell9.setCellValue(record.getR0070_amount_aed_non_resident().doubleValue());
						R4cell9.setCellStyle(numberStyle);
					} else {
						R4cell9.setCellValue("");
						R4cell9.setCellStyle(textStyle);
					}

					// ROW18
					// Column K:
					Cell R4cell10 = row.createCell(10);
					if (record.getR0070_no_acct_fcy_non_resident() != null) {
						R4cell10.setCellValue(record.getR0070_no_acct_fcy_non_resident().doubleValue());
						R4cell10.setCellStyle(numberStyle);
					} else {
						R4cell10.setCellValue("");
						R4cell10.setCellStyle(textStyle);
					}

					// ROW18
					// Column L:
					Cell R4cell11 = row.createCell(11);
					if (record.getR0070_amount_fcy_non_resident() != null) {
						R4cell11.setCellValue(record.getR0070_amount_fcy_non_resident().doubleValue());
						R4cell11.setCellStyle(numberStyle);
					} else {
						R4cell11.setCellValue("");
						R4cell11.setCellStyle(textStyle);
					}

					///// ROW19///////////
					// ROW19
					// Column E:

					row = sheet.getRow(18);
					Cell R5cell4 = row.createCell(4);
					if (record.getR0080_no_acct_aed_resident() != null) {
						R5cell4.setCellValue(record.getR0080_no_acct_aed_resident().doubleValue());
						R5cell4.setCellStyle(numberStyle);
					} else {
						R5cell4.setCellValue("");
						R5cell4.setCellStyle(textStyle);
					}

					// ROW19
					// Column F:
					Cell R5cell5 = row.createCell(5);
					if (record.getR0080_amount_aed_resident() != null) {
						R5cell5.setCellValue(record.getR0080_amount_aed_resident().doubleValue());
						R5cell5.setCellStyle(numberStyle);
					} else {
						R5cell5.setCellValue("");
						R5cell5.setCellStyle(textStyle);
					}

					// ROW19
					// Column G:
					Cell R5cell6 = row.createCell(6);
					if (record.getR0080_no_acct_fcy_resident() != null) {
						R5cell6.setCellValue(record.getR0080_no_acct_fcy_resident().doubleValue());
						R5cell6.setCellStyle(numberStyle);
					} else {
						R5cell6.setCellValue("");
						R5cell6.setCellStyle(textStyle);
					}

					// ROW19
					// Column H:
					Cell R5cell7 = row.createCell(7);
					if (record.getR0080_amount_fcy_resident() != null) {
						R5cell7.setCellValue(record.getR0080_amount_fcy_resident().doubleValue());
						R5cell7.setCellStyle(numberStyle);
					} else {
						R5cell7.setCellValue("");
						R5cell7.setCellStyle(textStyle);
					}

					// ROW19
					// Column I:
					Cell R5cell8 = row.createCell(8);
					if (record.getR0080_no_acct_aed_non_resident() != null) {
						R5cell8.setCellValue(record.getR0080_no_acct_aed_non_resident().doubleValue());
						R5cell8.setCellStyle(numberStyle);
					} else {
						R5cell8.setCellValue("");
						R5cell8.setCellStyle(textStyle);
					}

					// ROW19
					// Column J:
					Cell R5cell9 = row.createCell(9);
					if (record.getR0080_amount_aed_non_resident() != null) {
						R5cell9.setCellValue(record.getR0080_amount_aed_non_resident().doubleValue());
						R5cell9.setCellStyle(numberStyle);
					} else {
						R5cell9.setCellValue("");
						R5cell9.setCellStyle(textStyle);
					}

					// ROW19
					// Column K:
					Cell R5cell10 = row.createCell(10);
					if (record.getR0080_no_acct_fcy_non_resident() != null) {
						R5cell10.setCellValue(record.getR0080_no_acct_fcy_non_resident().doubleValue());
						R5cell10.setCellStyle(numberStyle);
					} else {
						R5cell10.setCellValue("");
						R5cell10.setCellStyle(textStyle);
					}

					// ROW19
					// Column L:
					Cell R5cell11 = row.createCell(11);
					if (record.getR0080_amount_fcy_non_resident() != null) {
						R5cell11.setCellValue(record.getR0080_amount_fcy_non_resident().doubleValue());
						R5cell11.setCellStyle(numberStyle);
					} else {
						R5cell11.setCellValue("");
						R5cell11.setCellStyle(textStyle);
					}

					///// ROW20///////////
					// ROW20
					// Column E:

					row = sheet.getRow(19);
					Cell R6cell4 = row.createCell(4);
					if (record.getR0090_no_acct_aed_resident() != null) {
						R6cell4.setCellValue(record.getR0090_no_acct_aed_resident().doubleValue());
						R6cell4.setCellStyle(numberStyle);
					} else {
						R6cell4.setCellValue("");
						R6cell4.setCellStyle(textStyle);
					}

					// ROW20
					// Column F:
					Cell R6cell5 = row.createCell(5);
					if (record.getR0090_amount_aed_resident() != null) {
						R6cell5.setCellValue(record.getR0090_amount_aed_resident().doubleValue());
						R6cell5.setCellStyle(numberStyle);
					} else {
						R6cell5.setCellValue("");
						R6cell5.setCellStyle(textStyle);
					}

					// ROW20
					// Column G:
					Cell R6cell6 = row.createCell(6);
					if (record.getR0090_no_acct_fcy_resident() != null) {
						R6cell6.setCellValue(record.getR0090_no_acct_fcy_resident().doubleValue());
						R6cell6.setCellStyle(numberStyle);
					} else {
						R6cell6.setCellValue("");
						R6cell6.setCellStyle(textStyle);
					}

					// ROW20
					// Column H:
					Cell R6cell7 = row.createCell(7);
					if (record.getR0090_amount_fcy_resident() != null) {
						R6cell7.setCellValue(record.getR0090_amount_fcy_resident().doubleValue());
						R6cell7.setCellStyle(numberStyle);
					} else {
						R6cell7.setCellValue("");
						R6cell7.setCellStyle(textStyle);
					}

					// ROW20
					// Column I:
					Cell R6cell8 = row.createCell(8);
					if (record.getR0090_no_acct_aed_non_resident() != null) {
						R6cell8.setCellValue(record.getR0090_no_acct_aed_non_resident().doubleValue());
						R6cell8.setCellStyle(numberStyle);
					} else {
						R6cell8.setCellValue("");
						R6cell8.setCellStyle(textStyle);
					}

					// ROW20
					// Column J:
					Cell R6cell9 = row.createCell(9);
					if (record.getR0090_amount_aed_non_resident() != null) {
						R6cell9.setCellValue(record.getR0090_amount_aed_non_resident().doubleValue());
						R6cell9.setCellStyle(numberStyle);
					} else {
						R6cell9.setCellValue("");
						R6cell9.setCellStyle(textStyle);
					}

					// ROW20
					// Column K:
					Cell R6cell10 = row.createCell(10);
					if (record.getR0090_no_acct_fcy_non_resident() != null) {
						R6cell10.setCellValue(record.getR0090_no_acct_fcy_non_resident().doubleValue());
						R6cell10.setCellStyle(numberStyle);
					} else {
						R6cell10.setCellValue("");
						R6cell10.setCellStyle(textStyle);
					}

					// ROW20
					// Column L:
					Cell R6cell11 = row.createCell(11);
					if (record.getR0090_amount_fcy_non_resident() != null) {
						R6cell11.setCellValue(record.getR0090_amount_fcy_non_resident().doubleValue());
						R6cell11.setCellStyle(numberStyle);
					} else {
						R6cell11.setCellValue("");
						R6cell11.setCellStyle(textStyle);
					}

					///// ROW21///////////
					// ROW21
					// Column E:

					row = sheet.getRow(20);
					Cell R7cell4 = row.createCell(4);
					if (record.getR0100_no_acct_aed_resident() != null) {
						R7cell4.setCellValue(record.getR0100_no_acct_aed_resident().doubleValue());
						R7cell4.setCellStyle(numberStyle);
					} else {
						R7cell4.setCellValue("");
						R7cell4.setCellStyle(textStyle);
					}

					// ROW21
					// Column F:
					Cell R7cell5 = row.createCell(5);
					if (record.getR0100_amount_aed_resident() != null) {
						R7cell5.setCellValue(record.getR0100_amount_aed_resident().doubleValue());
						R7cell5.setCellStyle(numberStyle);
					} else {
						R7cell5.setCellValue("");
						R7cell5.setCellStyle(textStyle);
					}

					// ROW21
					// Column G:
					Cell R7cell6 = row.createCell(6);
					if (record.getR0100_no_acct_fcy_resident() != null) {
						R7cell6.setCellValue(record.getR0100_no_acct_fcy_resident().doubleValue());
						R7cell6.setCellStyle(numberStyle);
					} else {
						R7cell6.setCellValue("");
						R7cell6.setCellStyle(textStyle);
					}

					// ROW21
					// Column H:
					Cell R7cell7 = row.createCell(7);
					if (record.getR0100_amount_fcy_resident() != null) {
						R7cell7.setCellValue(record.getR0100_amount_fcy_resident().doubleValue());
						R7cell7.setCellStyle(numberStyle);
					} else {
						R7cell7.setCellValue("");
						R7cell7.setCellStyle(textStyle);
					}

					// ROW21
					// Column I:
					Cell R7cell8 = row.createCell(8);
					if (record.getR0100_no_acct_aed_non_resident() != null) {
						R7cell8.setCellValue(record.getR0100_no_acct_aed_non_resident().doubleValue());
						R7cell8.setCellStyle(numberStyle);
					} else {
						R7cell8.setCellValue("");
						R7cell8.setCellStyle(textStyle);
					}

					// ROW21
					// Column J:
					Cell R7cell9 = row.createCell(9);
					if (record.getR0100_amount_aed_non_resident() != null) {
						R7cell9.setCellValue(record.getR0100_amount_aed_non_resident().doubleValue());
						R7cell9.setCellStyle(numberStyle);
					} else {
						R7cell9.setCellValue("");
						R7cell9.setCellStyle(textStyle);
					}

					// ROW21
					// Column K:
					Cell R7cell10 = row.createCell(10);
					if (record.getR0100_no_acct_fcy_non_resident() != null) {
						R7cell10.setCellValue(record.getR0100_no_acct_fcy_non_resident().doubleValue());
						R7cell10.setCellStyle(numberStyle);
					} else {
						R7cell10.setCellValue("");
						R7cell10.setCellStyle(textStyle);
					}

					// ROW21
					// Column L:
					Cell R7cell11 = row.createCell(11);
					if (record.getR0100_amount_fcy_non_resident() != null) {
						R7cell11.setCellValue(record.getR0100_amount_fcy_non_resident().doubleValue());
						R7cell11.setCellStyle(numberStyle);
					} else {
						R7cell11.setCellValue("");
						R7cell11.setCellStyle(textStyle);
					}

					///// ROW24///////////
					// ROW24
					// Column E:

					row = sheet.getRow(23);
					Cell R8cell4 = row.createCell(4);
					if (record.getR0130_no_acct_aed_resident() != null) {
						R8cell4.setCellValue(record.getR0130_no_acct_aed_resident().doubleValue());
						R8cell4.setCellStyle(numberStyle);
					} else {
						R8cell4.setCellValue("");
						R8cell4.setCellStyle(textStyle);
					}

					// ROW24
					// Column F:
					Cell R8cell5 = row.createCell(5);
					if (record.getR0130_amount_aed_resident() != null) {
						R8cell5.setCellValue(record.getR0130_amount_aed_resident().doubleValue());
						R8cell5.setCellStyle(numberStyle);
					} else {
						R8cell5.setCellValue("");
						R8cell5.setCellStyle(textStyle);
					}

					// ROW24
					// Column G:
					Cell R8cell6 = row.createCell(6);
					if (record.getR0130_no_acct_fcy_resident() != null) {
						R8cell6.setCellValue(record.getR0130_no_acct_fcy_resident().doubleValue());
						R8cell6.setCellStyle(numberStyle);
					} else {
						R8cell6.setCellValue("");
						R8cell6.setCellStyle(textStyle);
					}

					// ROW24
					// Column H:
					Cell R8cell7 = row.createCell(7);
					if (record.getR0130_amount_fcy_resident() != null) {
						R8cell7.setCellValue(record.getR0130_amount_fcy_resident().doubleValue());
						R8cell7.setCellStyle(numberStyle);
					} else {
						R8cell7.setCellValue("");
						R8cell7.setCellStyle(textStyle);
					}

					// ROW24
					// Column I:
					Cell R8cell8 = row.createCell(8);
					if (record.getR0130_no_acct_aed_non_resident() != null) {
						R8cell8.setCellValue(record.getR0130_no_acct_aed_non_resident().doubleValue());
						R8cell8.setCellStyle(numberStyle);
					} else {
						R8cell8.setCellValue("");
						R8cell8.setCellStyle(textStyle);
					}

					// ROW24
					// Column J:
					Cell R8cell9 = row.createCell(9);
					if (record.getR0130_amount_aed_non_resident() != null) {
						R8cell9.setCellValue(record.getR0130_amount_aed_non_resident().doubleValue());
						R8cell9.setCellStyle(numberStyle);
					} else {
						R8cell9.setCellValue("");
						R8cell9.setCellStyle(textStyle);
					}

					// ROW24
					// Column K:
					Cell R8cell13 = row.createCell(10);
					if (record.getR0130_no_acct_fcy_non_resident() != null) {
						R8cell13.setCellValue(record.getR0130_no_acct_fcy_non_resident().doubleValue());
						R8cell13.setCellStyle(numberStyle);
					} else {
						R8cell13.setCellValue("");
						R8cell13.setCellStyle(textStyle);
					}

					// ROW24
					// Column L:
					Cell R8cell11 = row.createCell(11);
					if (record.getR0130_amount_fcy_non_resident() != null) {
						R8cell11.setCellValue(record.getR0130_amount_fcy_non_resident().doubleValue());
						R8cell11.setCellStyle(numberStyle);
					} else {
						R8cell11.setCellValue("");
						R8cell11.setCellStyle(textStyle);
					}

					///// ROW25///////////
					// ROW25
					// Column E:

					row = sheet.getRow(24);
					Cell R9cell4 = row.createCell(4);
					if (record.getR0140_no_acct_aed_resident() != null) {
						R9cell4.setCellValue(record.getR0140_no_acct_aed_resident().doubleValue());
						R9cell4.setCellStyle(numberStyle);
					} else {
						R9cell4.setCellValue("");
						R9cell4.setCellStyle(textStyle);
					}

					// ROW25
					// Column F:
					Cell R9cell5 = row.createCell(5);
					if (record.getR0140_amount_aed_resident() != null) {
						R9cell5.setCellValue(record.getR0140_amount_aed_resident().doubleValue());
						R9cell5.setCellStyle(numberStyle);
					} else {
						R9cell5.setCellValue("");
						R9cell5.setCellStyle(textStyle);
					}

					// ROW25
					// Column G:
					Cell R9cell6 = row.createCell(6);
					if (record.getR0140_no_acct_fcy_resident() != null) {
						R9cell6.setCellValue(record.getR0140_no_acct_fcy_resident().doubleValue());
						R9cell6.setCellStyle(numberStyle);
					} else {
						R9cell6.setCellValue("");
						R9cell6.setCellStyle(textStyle);
					}

					// ROW25
					// Column H:
					Cell R9cell7 = row.createCell(7);
					if (record.getR0140_amount_fcy_resident() != null) {
						R9cell7.setCellValue(record.getR0140_amount_fcy_resident().doubleValue());
						R9cell7.setCellStyle(numberStyle);
					} else {
						R9cell7.setCellValue("");
						R9cell7.setCellStyle(textStyle);
					}

					// ROW25
					// Column I:
					Cell R9cell8 = row.createCell(8);
					if (record.getR0140_no_acct_aed_non_resident() != null) {
						R9cell8.setCellValue(record.getR0140_no_acct_aed_non_resident().doubleValue());
						R9cell8.setCellStyle(numberStyle);
					} else {
						R9cell8.setCellValue("");
						R9cell8.setCellStyle(textStyle);
					}

					// ROW25
					// Column J:
					Cell R9cell9 = row.createCell(9);
					if (record.getR0140_amount_aed_non_resident() != null) {
						R9cell9.setCellValue(record.getR0140_amount_aed_non_resident().doubleValue());
						R9cell9.setCellStyle(numberStyle);
					} else {
						R9cell9.setCellValue("");
						R9cell9.setCellStyle(textStyle);
					}

					// ROW25
					// Column K:
					Cell R9cell14 = row.createCell(10);
					if (record.getR0140_no_acct_fcy_non_resident() != null) {
						R9cell14.setCellValue(record.getR0140_no_acct_fcy_non_resident().doubleValue());
						R9cell14.setCellStyle(numberStyle);
					} else {
						R9cell14.setCellValue("");
						R9cell14.setCellStyle(textStyle);
					}

					// ROW25
					// Column L:
					Cell R9cell11 = row.createCell(11);
					if (record.getR0140_amount_fcy_non_resident() != null) {
						R9cell11.setCellValue(record.getR0140_amount_fcy_non_resident().doubleValue());
						R9cell11.setCellStyle(numberStyle);
					} else {
						R9cell11.setCellValue("");
						R9cell11.setCellStyle(textStyle);
					}

					///// ROW26///////////
					// ROW26
					// Column E:

					row = sheet.getRow(25);
					Cell R10cell4 = row.createCell(4);
					if (record.getR0150_no_acct_aed_resident() != null) {
						R10cell4.setCellValue(record.getR0150_no_acct_aed_resident().doubleValue());
						R10cell4.setCellStyle(numberStyle);
					} else {
						R10cell4.setCellValue("");
						R10cell4.setCellStyle(textStyle);
					}

					// ROW26
					// Column F:
					Cell R10cell5 = row.createCell(5);
					if (record.getR0150_amount_aed_resident() != null) {
						R10cell5.setCellValue(record.getR0150_amount_aed_resident().doubleValue());
						R10cell5.setCellStyle(numberStyle);
					} else {
						R10cell5.setCellValue("");
						R10cell5.setCellStyle(textStyle);
					}

					// ROW26
					// Column G:
					Cell R10cell6 = row.createCell(6);
					if (record.getR0150_no_acct_fcy_resident() != null) {
						R10cell6.setCellValue(record.getR0150_no_acct_fcy_resident().doubleValue());
						R10cell6.setCellStyle(numberStyle);
					} else {
						R10cell6.setCellValue("");
						R10cell6.setCellStyle(textStyle);
					}

					// ROW26
					// Column H:
					Cell R10cell7 = row.createCell(7);
					if (record.getR0150_amount_fcy_resident() != null) {
						R10cell7.setCellValue(record.getR0150_amount_fcy_resident().doubleValue());
						R10cell7.setCellStyle(numberStyle);
					} else {
						R10cell7.setCellValue("");
						R10cell7.setCellStyle(textStyle);
					}

					// ROW26
					// Column I:
					Cell R10cell8 = row.createCell(8);
					if (record.getR0150_no_acct_aed_non_resident() != null) {
						R10cell8.setCellValue(record.getR0150_no_acct_aed_non_resident().doubleValue());
						R10cell8.setCellStyle(numberStyle);
					} else {
						R10cell8.setCellValue("");
						R10cell8.setCellStyle(textStyle);
					}

					// ROW26
					// Column J:
					Cell R10cell9 = row.createCell(9);
					if (record.getR0150_amount_aed_non_resident() != null) {
						R10cell9.setCellValue(record.getR0150_amount_aed_non_resident().doubleValue());
						R10cell9.setCellStyle(numberStyle);
					} else {
						R10cell9.setCellValue("");
						R10cell9.setCellStyle(textStyle);
					}

					// ROW26
					// Column K:
					Cell R10cell15 = row.createCell(10);
					if (record.getR0150_no_acct_fcy_non_resident() != null) {
						R10cell15.setCellValue(record.getR0150_no_acct_fcy_non_resident().doubleValue());
						R10cell15.setCellStyle(numberStyle);
					} else {
						R10cell15.setCellValue("");
						R10cell15.setCellStyle(textStyle);
					}

					// ROW26
					// Column L:
					Cell R10cell11 = row.createCell(11);
					if (record.getR0150_amount_fcy_non_resident() != null) {
						R10cell11.setCellValue(record.getR0150_amount_fcy_non_resident().doubleValue());
						R10cell11.setCellStyle(numberStyle);
					} else {
						R10cell11.setCellValue("");
						R10cell11.setCellStyle(textStyle);
					}

					///// ROW27///////////
					// ROW27
					// Column E:

					row = sheet.getRow(26);
					Cell R11cell4 = row.createCell(4);
					if (record.getR0160_no_acct_aed_resident() != null) {
						R11cell4.setCellValue(record.getR0160_no_acct_aed_resident().doubleValue());
						R11cell4.setCellStyle(numberStyle);
					} else {
						R11cell4.setCellValue("");
						R11cell4.setCellStyle(textStyle);
					}

					// ROW27
					// Column F:
					Cell R11cell5 = row.createCell(5);
					if (record.getR0160_amount_aed_resident() != null) {
						R11cell5.setCellValue(record.getR0160_amount_aed_resident().doubleValue());
						R11cell5.setCellStyle(numberStyle);
					} else {
						R11cell5.setCellValue("");
						R11cell5.setCellStyle(textStyle);
					}

					// ROW27
					// Column G:
					Cell R11cell6 = row.createCell(6);
					if (record.getR0160_no_acct_fcy_resident() != null) {
						R11cell6.setCellValue(record.getR0160_no_acct_fcy_resident().doubleValue());
						R11cell6.setCellStyle(numberStyle);
					} else {
						R11cell6.setCellValue("");
						R11cell6.setCellStyle(textStyle);
					}

					// ROW27
					// Column H:
					Cell R11cell7 = row.createCell(7);
					if (record.getR0160_amount_fcy_resident() != null) {
						R11cell7.setCellValue(record.getR0160_amount_fcy_resident().doubleValue());
						R11cell7.setCellStyle(numberStyle);
					} else {
						R11cell7.setCellValue("");
						R11cell7.setCellStyle(textStyle);
					}

					// ROW27
					// Column I:
					Cell R11cell8 = row.createCell(8);
					if (record.getR0160_no_acct_aed_non_resident() != null) {
						R11cell8.setCellValue(record.getR0160_no_acct_aed_non_resident().doubleValue());
						R11cell8.setCellStyle(numberStyle);
					} else {
						R11cell8.setCellValue("");
						R11cell8.setCellStyle(textStyle);
					}

					// ROW27
					// Column J:
					Cell R11cell9 = row.createCell(9);
					if (record.getR0160_amount_aed_non_resident() != null) {
						R11cell9.setCellValue(record.getR0160_amount_aed_non_resident().doubleValue());
						R11cell9.setCellStyle(numberStyle);
					} else {
						R11cell9.setCellValue("");
						R11cell9.setCellStyle(textStyle);
					}

					// ROW27
					// Column K:
					Cell R11cell16 = row.createCell(10);
					if (record.getR0160_no_acct_fcy_non_resident() != null) {
						R11cell16.setCellValue(record.getR0160_no_acct_fcy_non_resident().doubleValue());
						R11cell16.setCellStyle(numberStyle);
					} else {
						R11cell16.setCellValue("");
						R11cell16.setCellStyle(textStyle);
					}

					// ROW27
					// Column L:
					Cell R11cell11 = row.createCell(11);
					if (record.getR0160_amount_fcy_non_resident() != null) {
						R11cell11.setCellValue(record.getR0160_amount_fcy_non_resident().doubleValue());
						R11cell11.setCellStyle(numberStyle);
					} else {
						R11cell11.setCellValue("");
						R11cell11.setCellStyle(textStyle);
					}

					///// ROW29///////////
					// ROW29
					// Column E:

					row = sheet.getRow(28);
					Cell R13cell4 = row.createCell(4);
					if (record.getR0180_no_acct_aed_resident() != null) {
						R13cell4.setCellValue(record.getR0180_no_acct_aed_resident().doubleValue());
						R13cell4.setCellStyle(numberStyle);
					} else {
						R13cell4.setCellValue("");
						R13cell4.setCellStyle(textStyle);
					}

//ROW29
// Column F: 
					Cell R13cell5 = row.createCell(5);
					if (record.getR0180_amount_aed_resident() != null) {
						R13cell5.setCellValue(record.getR0180_amount_aed_resident().doubleValue());
						R13cell5.setCellStyle(numberStyle);
					} else {
						R13cell5.setCellValue("");
						R13cell5.setCellStyle(textStyle);
					}

//ROW29
// Column G: 
					Cell R13cell6 = row.createCell(6);
					if (record.getR0180_no_acct_fcy_resident() != null) {
						R13cell6.setCellValue(record.getR0180_no_acct_fcy_resident().doubleValue());
						R13cell6.setCellStyle(numberStyle);
					} else {
						R13cell6.setCellValue("");
						R13cell6.setCellStyle(textStyle);
					}

//ROW29
// Column H: 
					Cell R13cell7 = row.createCell(7);
					if (record.getR0180_amount_fcy_resident() != null) {
						R13cell7.setCellValue(record.getR0180_amount_fcy_resident().doubleValue());
						R13cell7.setCellStyle(numberStyle);
					} else {
						R13cell7.setCellValue("");
						R13cell7.setCellStyle(textStyle);
					}

//ROW29
// Column I: 
					Cell R13cell8 = row.createCell(8);
					if (record.getR0180_no_acct_aed_non_resident() != null) {
						R13cell8.setCellValue(record.getR0180_no_acct_aed_non_resident().doubleValue());
						R13cell8.setCellStyle(numberStyle);
					} else {
						R13cell8.setCellValue("");
						R13cell8.setCellStyle(textStyle);
					}

//ROW29
// Column J: 
					Cell R13cell9 = row.createCell(9);
					if (record.getR0180_amount_aed_non_resident() != null) {
						R13cell9.setCellValue(record.getR0180_amount_aed_non_resident().doubleValue());
						R13cell9.setCellStyle(numberStyle);
					} else {
						R13cell9.setCellValue("");
						R13cell9.setCellStyle(textStyle);
					}

//ROW29
// Column K: 
					Cell R13cell18 = row.createCell(10);
					if (record.getR0180_no_acct_fcy_non_resident() != null) {
						R13cell18.setCellValue(record.getR0180_no_acct_fcy_non_resident().doubleValue());
						R13cell18.setCellStyle(numberStyle);
					} else {
						R13cell18.setCellValue("");
						R13cell18.setCellStyle(textStyle);
					}

//ROW29
// Column L: 
					Cell R13cell11 = row.createCell(11);
					if (record.getR0180_amount_fcy_non_resident() != null) {
						R13cell11.setCellValue(record.getR0180_amount_fcy_non_resident().doubleValue());
						R13cell11.setCellStyle(numberStyle);
					} else {
						R13cell11.setCellValue("");
						R13cell11.setCellStyle(textStyle);
					}

/////ROW30///////////
//ROW30
// Column E: 

					row = sheet.getRow(29);
					Cell R14cell4 = row.createCell(4);
					if (record.getR0190_no_acct_aed_resident() != null) {
						R14cell4.setCellValue(record.getR0190_no_acct_aed_resident().doubleValue());
						R14cell4.setCellStyle(numberStyle);
					} else {
						R14cell4.setCellValue("");
						R14cell4.setCellStyle(textStyle);
					}

//ROW30
//Column F: 
					Cell R14cell5 = row.createCell(5);
					if (record.getR0190_amount_aed_resident() != null) {
						R14cell5.setCellValue(record.getR0190_amount_aed_resident().doubleValue());
						R14cell5.setCellStyle(numberStyle);
					} else {
						R14cell5.setCellValue("");
						R14cell5.setCellStyle(textStyle);
					}

//ROW30
//Column G: 
					Cell R14cell6 = row.createCell(6);
					if (record.getR0190_no_acct_fcy_resident() != null) {
						R14cell6.setCellValue(record.getR0190_no_acct_fcy_resident().doubleValue());
						R14cell6.setCellStyle(numberStyle);
					} else {
						R14cell6.setCellValue("");
						R14cell6.setCellStyle(textStyle);
					}

//ROW30
//Column H: 
					Cell R14cell7 = row.createCell(7);
					if (record.getR0190_amount_fcy_resident() != null) {
						R14cell7.setCellValue(record.getR0190_amount_fcy_resident().doubleValue());
						R14cell7.setCellStyle(numberStyle);
					} else {
						R14cell7.setCellValue("");
						R14cell7.setCellStyle(textStyle);
					}

//ROW30
//Column I: 
					Cell R14cell8 = row.createCell(8);
					if (record.getR0190_no_acct_aed_non_resident() != null) {
						R14cell8.setCellValue(record.getR0190_no_acct_aed_non_resident().doubleValue());
						R14cell8.setCellStyle(numberStyle);
					} else {
						R14cell8.setCellValue("");
						R14cell8.setCellStyle(textStyle);
					}

//ROW30
//Column J: 
					Cell R14cell9 = row.createCell(9);
					if (record.getR0190_amount_aed_non_resident() != null) {
						R14cell9.setCellValue(record.getR0190_amount_aed_non_resident().doubleValue());
						R14cell9.setCellStyle(numberStyle);
					} else {
						R14cell9.setCellValue("");
						R14cell9.setCellStyle(textStyle);
					}

//ROW30
//Column K: 
					Cell R14cell18 = row.createCell(10);
					if (record.getR0190_no_acct_fcy_non_resident() != null) {
						R14cell18.setCellValue(record.getR0190_no_acct_fcy_non_resident().doubleValue());
						R14cell18.setCellStyle(numberStyle);
					} else {
						R14cell18.setCellValue("");
						R14cell18.setCellStyle(textStyle);
					}

//ROW30
//Column L: 
					Cell R14cell11 = row.createCell(11);
					if (record.getR0190_amount_fcy_non_resident() != null) {
						R14cell11.setCellValue(record.getR0190_amount_fcy_non_resident().doubleValue());
						R14cell11.setCellStyle(numberStyle);
					} else {
						R14cell11.setCellValue("");
						R14cell11.setCellStyle(textStyle);
					}

/////ROW31///////////
//ROW31
// Column E: 

					row = sheet.getRow(30);
					Cell R15cell4 = row.createCell(4);
					if (record.getR0200_no_acct_aed_resident() != null) {
						R15cell4.setCellValue(record.getR0200_no_acct_aed_resident().doubleValue());
						R15cell4.setCellStyle(numberStyle);
					} else {
						R15cell4.setCellValue("");
						R15cell4.setCellStyle(textStyle);
					}

//ROW31
//Column F: 
					Cell R15cell5 = row.createCell(5);
					if (record.getR0200_amount_aed_resident() != null) {
						R15cell5.setCellValue(record.getR0200_amount_aed_resident().doubleValue());
						R15cell5.setCellStyle(numberStyle);
					} else {
						R15cell5.setCellValue("");
						R15cell5.setCellStyle(textStyle);
					}

//ROW31
//Column G: 
					Cell R15cell6 = row.createCell(6);
					if (record.getR0200_no_acct_fcy_resident() != null) {
						R15cell6.setCellValue(record.getR0200_no_acct_fcy_resident().doubleValue());
						R15cell6.setCellStyle(numberStyle);
					} else {
						R15cell6.setCellValue("");
						R15cell6.setCellStyle(textStyle);
					}

//ROW31
//Column H: 
					Cell R15cell7 = row.createCell(7);
					if (record.getR0200_amount_fcy_resident() != null) {
						R15cell7.setCellValue(record.getR0200_amount_fcy_resident().doubleValue());
						R15cell7.setCellStyle(numberStyle);
					} else {
						R15cell7.setCellValue("");
						R15cell7.setCellStyle(textStyle);
					}

//ROW31
//Column I: 
					Cell R15cell8 = row.createCell(8);
					if (record.getR0200_no_acct_aed_non_resident() != null) {
						R15cell8.setCellValue(record.getR0200_no_acct_aed_non_resident().doubleValue());
						R15cell8.setCellStyle(numberStyle);
					} else {
						R15cell8.setCellValue("");
						R15cell8.setCellStyle(textStyle);
					}

//ROW31
//Column J: 
					Cell R15cell9 = row.createCell(9);
					if (record.getR0200_amount_aed_non_resident() != null) {
						R15cell9.setCellValue(record.getR0200_amount_aed_non_resident().doubleValue());
						R15cell9.setCellStyle(numberStyle);
					} else {
						R15cell9.setCellValue("");
						R15cell9.setCellStyle(textStyle);
					}

//ROW31
//Column K: 
					Cell R15cell18 = row.createCell(10);
					if (record.getR0200_no_acct_fcy_non_resident() != null) {
						R15cell18.setCellValue(record.getR0200_no_acct_fcy_non_resident().doubleValue());
						R15cell18.setCellStyle(numberStyle);
					} else {
						R15cell18.setCellValue("");
						R15cell18.setCellStyle(textStyle);
					}

//ROW31
//Column L: 
					Cell R15cell11 = row.createCell(11);
					if (record.getR0200_amount_fcy_non_resident() != null) {
						R15cell11.setCellValue(record.getR0200_amount_fcy_non_resident().doubleValue());
						R15cell11.setCellStyle(numberStyle);
					} else {
						R15cell11.setCellValue("");
						R15cell11.setCellStyle(textStyle);
					}

/////ROW32///////////
//ROW32
// Column E: 

					row = sheet.getRow(31);
					Cell R16cell4 = row.createCell(4);
					if (record.getR0210_no_acct_aed_resident() != null) {
						R16cell4.setCellValue(record.getR0210_no_acct_aed_resident().doubleValue());
						R16cell4.setCellStyle(numberStyle);
					} else {
						R16cell4.setCellValue("");
						R16cell4.setCellStyle(textStyle);
					}

//ROW32
//Column F: 
					Cell R16cell5 = row.createCell(5);
					if (record.getR0210_amount_aed_resident() != null) {
						R16cell5.setCellValue(record.getR0210_amount_aed_resident().doubleValue());
						R16cell5.setCellStyle(numberStyle);
					} else {
						R16cell5.setCellValue("");
						R16cell5.setCellStyle(textStyle);
					}

//ROW32
//Column G: 
					Cell R16cell6 = row.createCell(6);
					if (record.getR0210_no_acct_fcy_resident() != null) {
						R16cell6.setCellValue(record.getR0210_no_acct_fcy_resident().doubleValue());
						R16cell6.setCellStyle(numberStyle);
					} else {
						R16cell6.setCellValue("");
						R16cell6.setCellStyle(textStyle);
					}

//ROW32
//Column H: 
					Cell R16cell7 = row.createCell(7);
					if (record.getR0210_amount_fcy_resident() != null) {
						R16cell7.setCellValue(record.getR0210_amount_fcy_resident().doubleValue());
						R16cell7.setCellStyle(numberStyle);
					} else {
						R16cell7.setCellValue("");
						R16cell7.setCellStyle(textStyle);
					}

//ROW32
//Column I: 
					Cell R16cell8 = row.createCell(8);
					if (record.getR0210_no_acct_aed_non_resident() != null) {
						R16cell8.setCellValue(record.getR0210_no_acct_aed_non_resident().doubleValue());
						R16cell8.setCellStyle(numberStyle);
					} else {
						R16cell8.setCellValue("");
						R16cell8.setCellStyle(textStyle);
					}

//ROW32
//Column J: 
					Cell R16cell9 = row.createCell(9);
					if (record.getR0210_amount_aed_non_resident() != null) {
						R16cell9.setCellValue(record.getR0210_amount_aed_non_resident().doubleValue());
						R16cell9.setCellStyle(numberStyle);
					} else {
						R16cell9.setCellValue("");
						R16cell9.setCellStyle(textStyle);
					}

//ROW32
//Column K: 
					Cell R16cell18 = row.createCell(10);
					if (record.getR0210_no_acct_fcy_non_resident() != null) {
						R16cell18.setCellValue(record.getR0210_no_acct_fcy_non_resident().doubleValue());
						R16cell18.setCellStyle(numberStyle);
					} else {
						R16cell18.setCellValue("");
						R16cell18.setCellStyle(textStyle);
					}

//ROW32
//Column L: 
					Cell R16cell11 = row.createCell(11);
					if (record.getR0210_amount_fcy_non_resident() != null) {
						R16cell11.setCellValue(record.getR0210_amount_fcy_non_resident().doubleValue());
						R16cell11.setCellStyle(numberStyle);
					} else {
						R16cell11.setCellValue("");
						R16cell11.setCellStyle(textStyle);
					}

/////ROW34///////////
//ROW34
// Column E: 

					row = sheet.getRow(33);
					Cell R18cell4 = row.createCell(4);
					if (record.getR0230_no_acct_aed_resident() != null) {
						R18cell4.setCellValue(record.getR0230_no_acct_aed_resident().doubleValue());
						R18cell4.setCellStyle(numberStyle);
					} else {
						R18cell4.setCellValue("");
						R18cell4.setCellStyle(textStyle);
					}

//ROW34
//Column F: 
					Cell R18cell5 = row.createCell(5);
					if (record.getR0230_amount_aed_resident() != null) {
						R18cell5.setCellValue(record.getR0230_amount_aed_resident().doubleValue());
						R18cell5.setCellStyle(numberStyle);
					} else {
						R18cell5.setCellValue("");
						R18cell5.setCellStyle(textStyle);
					}

//ROW34
//Column G: 
					Cell R18cell6 = row.createCell(6);
					if (record.getR0230_no_acct_fcy_resident() != null) {
						R18cell6.setCellValue(record.getR0230_no_acct_fcy_resident().doubleValue());
						R18cell6.setCellStyle(numberStyle);
					} else {
						R18cell6.setCellValue("");
						R18cell6.setCellStyle(textStyle);
					}

//ROW34
//Column H: 
					Cell R18cell7 = row.createCell(7);
					if (record.getR0230_amount_fcy_resident() != null) {
						R18cell7.setCellValue(record.getR0230_amount_fcy_resident().doubleValue());
						R18cell7.setCellStyle(numberStyle);
					} else {
						R18cell7.setCellValue("");
						R18cell7.setCellStyle(textStyle);
					}

//ROW34
//Column I: 
					Cell R18cell8 = row.createCell(8);
					if (record.getR0230_no_acct_aed_non_resident() != null) {
						R18cell8.setCellValue(record.getR0230_no_acct_aed_non_resident().doubleValue());
						R18cell8.setCellStyle(numberStyle);
					} else {
						R18cell8.setCellValue("");
						R18cell8.setCellStyle(textStyle);
					}

//ROW34
//Column J: 
					Cell R18cell9 = row.createCell(9);
					if (record.getR0230_amount_aed_non_resident() != null) {
						R18cell9.setCellValue(record.getR0230_amount_aed_non_resident().doubleValue());
						R18cell9.setCellStyle(numberStyle);
					} else {
						R18cell9.setCellValue("");
						R18cell9.setCellStyle(textStyle);
					}

//ROW34
//Column K: 
					Cell R18cell18 = row.createCell(10);
					if (record.getR0230_no_acct_fcy_non_resident() != null) {
						R18cell18.setCellValue(record.getR0230_no_acct_fcy_non_resident().doubleValue());
						R18cell18.setCellStyle(numberStyle);
					} else {
						R18cell18.setCellValue("");
						R18cell18.setCellStyle(textStyle);
					}

//ROW34
//Column L: 
					Cell R18cell11 = row.createCell(11);
					if (record.getR0230_amount_fcy_non_resident() != null) {
						R18cell11.setCellValue(record.getR0230_amount_fcy_non_resident().doubleValue());
						R18cell11.setCellStyle(numberStyle);
					} else {
						R18cell11.setCellValue("");
						R18cell11.setCellStyle(textStyle);
					}

/////ROW35///////////
//ROW35
// Column E: 

					row = sheet.getRow(34);
					Cell R19cell4 = row.createCell(4);
					if (record.getR0240_no_acct_aed_resident() != null) {
						R19cell4.setCellValue(record.getR0240_no_acct_aed_resident().doubleValue());
						R19cell4.setCellStyle(numberStyle);
					} else {
						R19cell4.setCellValue("");
						R19cell4.setCellStyle(textStyle);
					}

//ROW35
//Column F: 
					Cell R19cell5 = row.createCell(5);
					if (record.getR0240_amount_aed_resident() != null) {
						R19cell5.setCellValue(record.getR0240_amount_aed_resident().doubleValue());
						R19cell5.setCellStyle(numberStyle);
					} else {
						R19cell5.setCellValue("");
						R19cell5.setCellStyle(textStyle);
					}

//ROW35
//Column G: 
					Cell R19cell6 = row.createCell(6);
					if (record.getR0240_no_acct_fcy_resident() != null) {
						R19cell6.setCellValue(record.getR0240_no_acct_fcy_resident().doubleValue());
						R19cell6.setCellStyle(numberStyle);
					} else {
						R19cell6.setCellValue("");
						R19cell6.setCellStyle(textStyle);
					}

//ROW35
//Column H: 
					Cell R19cell7 = row.createCell(7);
					if (record.getR0240_amount_fcy_resident() != null) {
						R19cell7.setCellValue(record.getR0240_amount_fcy_resident().doubleValue());
						R19cell7.setCellStyle(numberStyle);
					} else {
						R19cell7.setCellValue("");
						R19cell7.setCellStyle(textStyle);
					}

//ROW35
//Column I: 
					Cell R19cell8 = row.createCell(8);
					if (record.getR0240_no_acct_aed_non_resident() != null) {
						R19cell8.setCellValue(record.getR0240_no_acct_aed_non_resident().doubleValue());
						R19cell8.setCellStyle(numberStyle);
					} else {
						R19cell8.setCellValue("");
						R19cell8.setCellStyle(textStyle);
					}

//ROW35
//Column J: 
					Cell R19cell9 = row.createCell(9);
					if (record.getR0240_amount_aed_non_resident() != null) {
						R19cell9.setCellValue(record.getR0240_amount_aed_non_resident().doubleValue());
						R19cell9.setCellStyle(numberStyle);
					} else {
						R19cell9.setCellValue("");
						R19cell9.setCellStyle(textStyle);
					}

//ROW35
//Column K: 
					Cell R19cell18 = row.createCell(10);
					if (record.getR0240_no_acct_fcy_non_resident() != null) {
						R19cell18.setCellValue(record.getR0240_no_acct_fcy_non_resident().doubleValue());
						R19cell18.setCellStyle(numberStyle);
					} else {
						R19cell18.setCellValue("");
						R19cell18.setCellStyle(textStyle);
					}

//ROW35
//Column L: 
					Cell R19cell11 = row.createCell(11);
					if (record.getR0240_amount_fcy_non_resident() != null) {
						R19cell11.setCellValue(record.getR0240_amount_fcy_non_resident().doubleValue());
						R19cell11.setCellStyle(numberStyle);
					} else {
						R19cell11.setCellValue("");
						R19cell11.setCellStyle(textStyle);
					}

/////ROW36///////////
//ROW36
// Column E: 

					row = sheet.getRow(35);
					Cell R20cell4 = row.createCell(4);
					if (record.getR0250_no_acct_aed_resident() != null) {
						R20cell4.setCellValue(record.getR0250_no_acct_aed_resident().doubleValue());
						R20cell4.setCellStyle(numberStyle);
					} else {
						R20cell4.setCellValue("");
						R20cell4.setCellStyle(textStyle);
					}

//ROW36
//Column F: 
					Cell R20cell5 = row.createCell(5);
					if (record.getR0250_amount_aed_resident() != null) {
						R20cell5.setCellValue(record.getR0250_amount_aed_resident().doubleValue());
						R20cell5.setCellStyle(numberStyle);
					} else {
						R20cell5.setCellValue("");
						R20cell5.setCellStyle(textStyle);
					}

//ROW36
//Column G: 
					Cell R20cell6 = row.createCell(6);
					if (record.getR0250_no_acct_fcy_resident() != null) {
						R20cell6.setCellValue(record.getR0250_no_acct_fcy_resident().doubleValue());
						R20cell6.setCellStyle(numberStyle);
					} else {
						R20cell6.setCellValue("");
						R20cell6.setCellStyle(textStyle);
					}

//ROW36
//Column H: 
					Cell R20cell7 = row.createCell(7);
					if (record.getR0250_amount_fcy_resident() != null) {
						R20cell7.setCellValue(record.getR0250_amount_fcy_resident().doubleValue());
						R20cell7.setCellStyle(numberStyle);
					} else {
						R20cell7.setCellValue("");
						R20cell7.setCellStyle(textStyle);
					}

//ROW36
//Column I: 
					Cell R20cell8 = row.createCell(8);
					if (record.getR0250_no_acct_aed_non_resident() != null) {
						R20cell8.setCellValue(record.getR0250_no_acct_aed_non_resident().doubleValue());
						R20cell8.setCellStyle(numberStyle);
					} else {
						R20cell8.setCellValue("");
						R20cell8.setCellStyle(textStyle);
					}

//ROW36
//Column J: 
					Cell R20cell9 = row.createCell(9);
					if (record.getR0250_amount_aed_non_resident() != null) {
						R20cell9.setCellValue(record.getR0250_amount_aed_non_resident().doubleValue());
						R20cell9.setCellStyle(numberStyle);
					} else {
						R20cell9.setCellValue("");
						R20cell9.setCellStyle(textStyle);
					}

//ROW36
//Column K: 
					Cell R20cell18 = row.createCell(10);
					if (record.getR0250_no_acct_fcy_non_resident() != null) {
						R20cell18.setCellValue(record.getR0250_no_acct_fcy_non_resident().doubleValue());
						R20cell18.setCellStyle(numberStyle);
					} else {
						R20cell18.setCellValue("");
						R20cell18.setCellStyle(textStyle);
					}

//ROW36
//Column L: 
					Cell R20cell11 = row.createCell(11);
					if (record.getR0250_amount_fcy_non_resident() != null) {
						R20cell11.setCellValue(record.getR0250_amount_fcy_non_resident().doubleValue());
						R20cell11.setCellStyle(numberStyle);
					} else {
						R20cell11.setCellValue("");
						R20cell11.setCellStyle(textStyle);
					}

/////ROW37///////////
//ROW37
// Column E: 

					row = sheet.getRow(36);
					Cell R21cell4 = row.createCell(4);
					if (record.getR0260_no_acct_aed_resident() != null) {
						R21cell4.setCellValue(record.getR0260_no_acct_aed_resident().doubleValue());
						R21cell4.setCellStyle(numberStyle);
					} else {
						R21cell4.setCellValue("");
						R21cell4.setCellStyle(textStyle);
					}

//ROW37
//Column F: 
					Cell R21cell5 = row.createCell(5);
					if (record.getR0260_amount_aed_resident() != null) {
						R21cell5.setCellValue(record.getR0260_amount_aed_resident().doubleValue());
						R21cell5.setCellStyle(numberStyle);
					} else {
						R21cell5.setCellValue("");
						R21cell5.setCellStyle(textStyle);
					}

//ROW37
//Column G: 
					Cell R21cell6 = row.createCell(6);
					if (record.getR0260_no_acct_fcy_resident() != null) {
						R21cell6.setCellValue(record.getR0260_no_acct_fcy_resident().doubleValue());
						R21cell6.setCellStyle(numberStyle);
					} else {
						R21cell6.setCellValue("");
						R21cell6.setCellStyle(textStyle);
					}

//ROW37
//Column H: 
					Cell R21cell7 = row.createCell(7);
					if (record.getR0260_amount_fcy_resident() != null) {
						R21cell7.setCellValue(record.getR0260_amount_fcy_resident().doubleValue());
						R21cell7.setCellStyle(numberStyle);
					} else {
						R21cell7.setCellValue("");
						R21cell7.setCellStyle(textStyle);
					}

//ROW37
//Column I: 
					Cell R21cell8 = row.createCell(8);
					if (record.getR0260_no_acct_aed_non_resident() != null) {
						R21cell8.setCellValue(record.getR0260_no_acct_aed_non_resident().doubleValue());
						R21cell8.setCellStyle(numberStyle);
					} else {
						R21cell8.setCellValue("");
						R21cell8.setCellStyle(textStyle);
					}

//ROW37
//Column J: 
					Cell R21cell9 = row.createCell(9);
					if (record.getR0260_amount_aed_non_resident() != null) {
						R21cell9.setCellValue(record.getR0260_amount_aed_non_resident().doubleValue());
						R21cell9.setCellStyle(numberStyle);
					} else {
						R21cell9.setCellValue("");
						R21cell9.setCellStyle(textStyle);
					}

//ROW37
//Column K: 
					Cell R21cell18 = row.createCell(10);
					if (record.getR0260_no_acct_fcy_non_resident() != null) {
						R21cell18.setCellValue(record.getR0260_no_acct_fcy_non_resident().doubleValue());
						R21cell18.setCellStyle(numberStyle);
					} else {
						R21cell18.setCellValue("");
						R21cell18.setCellStyle(textStyle);
					}

//ROW37
//Column L: 
					Cell R21cell11 = row.createCell(11);
					if (record.getR0260_amount_fcy_non_resident() != null) {
						R21cell11.setCellValue(record.getR0260_amount_fcy_non_resident().doubleValue());
						R21cell11.setCellStyle(numberStyle);
					} else {
						R21cell11.setCellValue("");
						R21cell11.setCellStyle(textStyle);
					}

/////ROW39///////////
//ROW39
// Column E: 

					row = sheet.getRow(38);
					Cell R22cell4 = row.createCell(4);
					if (record.getR0280_no_acct_aed_resident() != null) {
						R22cell4.setCellValue(record.getR0280_no_acct_aed_resident().doubleValue());
						R22cell4.setCellStyle(numberStyle);
					} else {
						R22cell4.setCellValue("");
						R22cell4.setCellStyle(textStyle);
					}

//ROW39
//Column F: 
					Cell R22cell5 = row.createCell(5);
					if (record.getR0280_amount_aed_resident() != null) {
						R22cell5.setCellValue(record.getR0280_amount_aed_resident().doubleValue());
						R22cell5.setCellStyle(numberStyle);
					} else {
						R22cell5.setCellValue("");
						R22cell5.setCellStyle(textStyle);
					}

//ROW39
//Column G: 
					Cell R22cell6 = row.createCell(6);
					if (record.getR0280_no_acct_fcy_resident() != null) {
						R22cell6.setCellValue(record.getR0280_no_acct_fcy_resident().doubleValue());
						R22cell6.setCellStyle(numberStyle);
					} else {
						R22cell6.setCellValue("");
						R22cell6.setCellStyle(textStyle);
					}

//ROW39
//Column H: 
					Cell R22cell7 = row.createCell(7);
					if (record.getR0280_amount_fcy_resident() != null) {
						R22cell7.setCellValue(record.getR0280_amount_fcy_resident().doubleValue());
						R22cell7.setCellStyle(numberStyle);
					} else {
						R22cell7.setCellValue("");
						R22cell7.setCellStyle(textStyle);
					}

//ROW39
//Column I: 
					Cell R22cell8 = row.createCell(8);
					if (record.getR0280_no_acct_aed_non_resident() != null) {
						R22cell8.setCellValue(record.getR0280_no_acct_aed_non_resident().doubleValue());
						R22cell8.setCellStyle(numberStyle);
					} else {
						R22cell8.setCellValue("");
						R22cell8.setCellStyle(textStyle);
					}

//ROW39
//Column J: 
					Cell R22cell9 = row.createCell(9);
					if (record.getR0280_amount_aed_non_resident() != null) {
						R22cell9.setCellValue(record.getR0280_amount_aed_non_resident().doubleValue());
						R22cell9.setCellStyle(numberStyle);
					} else {
						R22cell9.setCellValue("");
						R22cell9.setCellStyle(textStyle);
					}

//ROW39
//Column K: 
					Cell R22cell18 = row.createCell(10);
					if (record.getR0280_no_acct_fcy_non_resident() != null) {
						R22cell18.setCellValue(record.getR0280_no_acct_fcy_non_resident().doubleValue());
						R22cell18.setCellStyle(numberStyle);
					} else {
						R22cell18.setCellValue("");
						R22cell18.setCellStyle(textStyle);
					}

//ROW39
//Column L: 
					Cell R22cell11 = row.createCell(11);
					if (record.getR0280_amount_fcy_non_resident() != null) {
						R22cell11.setCellValue(record.getR0280_amount_fcy_non_resident().doubleValue());
						R22cell11.setCellStyle(numberStyle);
					} else {
						R22cell11.setCellValue("");
						R22cell11.setCellStyle(textStyle);
					}

/////ROW40///////////
//ROW40
// Column E: 

					row = sheet.getRow(39);

//ROW40
//Column F: 
					Cell R23cell5 = row.createCell(5);
					if (record.getR0290_amount_aed_resident() != null) {
						R23cell5.setCellValue(record.getR0290_amount_aed_resident().doubleValue());
						R23cell5.setCellStyle(numberStyle);
					} else {
						R23cell5.setCellValue("");
						R23cell5.setCellStyle(textStyle);
					}

//ROW40
//Column H: 
					Cell R23cell7 = row.createCell(7);
					if (record.getR0290_amount_fcy_resident() != null) {
						R23cell7.setCellValue(record.getR0290_amount_fcy_resident().doubleValue());
						R23cell7.setCellStyle(numberStyle);
					} else {
						R23cell7.setCellValue("");
						R23cell7.setCellStyle(textStyle);
					}

//ROW40
//Column J: 
					Cell R23cell9 = row.createCell(9);
					if (record.getR0290_amount_aed_non_resident() != null) {
						R23cell9.setCellValue(record.getR0290_amount_aed_non_resident().doubleValue());
						R23cell9.setCellStyle(numberStyle);
					} else {
						R23cell9.setCellValue("");
						R23cell9.setCellStyle(textStyle);
					}

//ROW40
//Column L: 
					Cell R23cell11 = row.createCell(11);
					if (record.getR0290_amount_fcy_non_resident() != null) {
						R23cell11.setCellValue(record.getR0290_amount_fcy_non_resident().doubleValue());
						R23cell11.setCellStyle(numberStyle);
					} else {
						R23cell11.setCellValue("");
						R23cell11.setCellStyle(textStyle);
					}

/////ROW42///////////
//ROW42
// Column E: 

					row = sheet.getRow(41);

//ROW42
//Column F: 
					Cell R24cell5 = row.createCell(5);
					if (record.getR0310_amount_aed_resident() != null) {
						R24cell5.setCellValue(record.getR0310_amount_aed_resident().doubleValue());
						R24cell5.setCellStyle(numberStyle);
					} else {
						R24cell5.setCellValue("");
						R24cell5.setCellStyle(textStyle);
					}

//ROW42
//Column H: 
					Cell R24cell7 = row.createCell(7);
					if (record.getR0310_amount_fcy_resident() != null) {
						R24cell7.setCellValue(record.getR0310_amount_fcy_resident().doubleValue());
						R24cell7.setCellStyle(numberStyle);
					} else {
						R24cell7.setCellValue("");
						R24cell7.setCellStyle(textStyle);
					}

//ROW42
//Column J: 
					Cell R24cell9 = row.createCell(9);
					if (record.getR0310_amount_aed_non_resident() != null) {
						R24cell9.setCellValue(record.getR0310_amount_aed_non_resident().doubleValue());
						R24cell9.setCellStyle(numberStyle);
					} else {
						R24cell9.setCellValue("");
						R24cell9.setCellStyle(textStyle);
					}

//ROW42
//Column L: 
					Cell R24cell11 = row.createCell(11);
					if (record.getR0310_amount_fcy_non_resident() != null) {
						R24cell11.setCellValue(record.getR0310_amount_fcy_non_resident().doubleValue());
						R24cell11.setCellStyle(numberStyle);
					} else {
						R24cell11.setCellValue("");
						R24cell11.setCellStyle(textStyle);
					}

/////ROW43///////////
//ROW43
// Column E: 

					row = sheet.getRow(42);

//ROW43
//Column F: 
					Cell R25cell5 = row.createCell(5);
					if (record.getR0320_amount_aed_resident() != null) {
						R25cell5.setCellValue(record.getR0320_amount_aed_resident().doubleValue());
						R25cell5.setCellStyle(numberStyle);
					} else {
						R25cell5.setCellValue("");
						R25cell5.setCellStyle(textStyle);
					}

//ROW43
//Column H: 
					Cell R25cell7 = row.createCell(7);
					if (record.getR0320_amount_fcy_resident() != null) {
						R25cell7.setCellValue(record.getR0320_amount_fcy_resident().doubleValue());
						R25cell7.setCellStyle(numberStyle);
					} else {
						R25cell7.setCellValue("");
						R25cell7.setCellStyle(textStyle);
					}

//ROW43
//Column J: 
					Cell R25cell9 = row.createCell(9);
					if (record.getR0320_amount_aed_non_resident() != null) {
						R25cell9.setCellValue(record.getR0320_amount_aed_non_resident().doubleValue());
						R25cell9.setCellStyle(numberStyle);
					} else {
						R25cell9.setCellValue("");
						R25cell9.setCellStyle(textStyle);
					}

//ROW43
//Column L: 
					Cell R25cell11 = row.createCell(11);
					if (record.getR0320_amount_fcy_non_resident() != null) {
						R25cell11.setCellValue(record.getR0320_amount_fcy_non_resident().doubleValue());
						R25cell11.setCellStyle(numberStyle);
					} else {
						R25cell11.setCellValue("");
						R25cell11.setCellStyle(textStyle);
					}

/////ROW44///////////
//ROW44
// Column E: 

					row = sheet.getRow(43);

//ROW44
//Column F: 
					Cell R26cell5 = row.createCell(5);
					if (record.getR0330_amount_aed_resident() != null) {
						R26cell5.setCellValue(record.getR0330_amount_aed_resident().doubleValue());
						R26cell5.setCellStyle(numberStyle);
					} else {
						R26cell5.setCellValue("");
						R26cell5.setCellStyle(textStyle);
					}

//ROW44
//Column H: 
					Cell R26cell7 = row.createCell(7);
					if (record.getR0330_amount_fcy_resident() != null) {
						R26cell7.setCellValue(record.getR0330_amount_fcy_resident().doubleValue());
						R26cell7.setCellStyle(numberStyle);
					} else {
						R26cell7.setCellValue("");
						R26cell7.setCellStyle(textStyle);
					}

//ROW44
//Column J: 
					Cell R26cell9 = row.createCell(9);
					if (record.getR0330_amount_aed_non_resident() != null) {
						R26cell9.setCellValue(record.getR0330_amount_aed_non_resident().doubleValue());
						R26cell9.setCellStyle(numberStyle);
					} else {
						R26cell9.setCellValue("");
						R26cell9.setCellStyle(textStyle);
					}

//ROW44
//Column L: 
					Cell R26cell11 = row.createCell(11);
					if (record.getR0330_amount_fcy_non_resident() != null) {
						R26cell11.setCellValue(record.getR0330_amount_fcy_non_resident().doubleValue());
						R26cell11.setCellStyle(numberStyle);
					} else {
						R26cell11.setCellValue("");
						R26cell11.setCellStyle(textStyle);
					}

					
					//ROW46
					//Column F: 
					row = sheet.getRow(45);
										Cell R81cell5 = row.createCell(5);
										if (record.getR0350_amount_aed_resident() != null) {
											R81cell5.setCellValue(record.getR0350_amount_aed_resident().doubleValue());
											R81cell5.setCellStyle(numberStyle);
										} else {
											R81cell5.setCellValue("");
											R81cell5.setCellStyle(textStyle);
										}

					//ROW46
					//Column H: 
										Cell R81cell7 = row.createCell(7);
										if (record.getR0350_amount_fcy_resident() != null) {
											R81cell7.setCellValue(record.getR0350_amount_fcy_resident().doubleValue());
											R81cell7.setCellStyle(numberStyle);
										} else {
											R81cell7.setCellValue("");
											R81cell7.setCellStyle(textStyle);
										}

					//ROW46
					//Column J: 
										Cell R81cell9 = row.createCell(9);
										if (record.getR0350_amount_aed_non_resident() != null) {
											R81cell9.setCellValue(record.getR0350_amount_aed_non_resident().doubleValue());
											R81cell9.setCellStyle(numberStyle);
										} else {
											R81cell9.setCellValue("");
											R81cell9.setCellStyle(textStyle);
										}

					//ROW46
					//Column L: 
										Cell R81cell11 = row.createCell(11);
										if (record.getR0350_amount_fcy_non_resident() != null) {
											R81cell11.setCellValue(record.getR0350_amount_fcy_non_resident().doubleValue());
											R81cell11.setCellStyle(numberStyle);
										} else {
											R81cell11.setCellValue("");
											R81cell11.setCellStyle(textStyle);
										}

										
										//ROW47
										//Column F:
										row = sheet.getRow(46);
															Cell R82cell5 = row.createCell(5);
															if (record.getR0360_amount_aed_resident() != null) {
																R82cell5.setCellValue(record.getR0360_amount_aed_resident().doubleValue());
																R82cell5.setCellStyle(numberStyle);
															} else {
																R82cell5.setCellValue("");
																R82cell5.setCellStyle(textStyle);
															}

										//ROW47
										//Column H: 
															Cell R82cell7 = row.createCell(7);
															if (record.getR0360_amount_fcy_resident() != null) {
																R82cell7.setCellValue(record.getR0360_amount_fcy_resident().doubleValue());
																R82cell7.setCellStyle(numberStyle);
															} else {
																R82cell7.setCellValue("");
																R82cell7.setCellStyle(textStyle);
															}

										//ROW47
										//Column J: 
															Cell R82cell9 = row.createCell(9);
															if (record.getR0360_amount_aed_non_resident() != null) {
																R82cell9.setCellValue(record.getR0360_amount_aed_non_resident().doubleValue());
																R82cell9.setCellStyle(numberStyle);
															} else {
																R82cell9.setCellValue("");
																R82cell9.setCellStyle(textStyle);
															}

										//ROW47
										//Column L: 
															Cell R82cell11 = row.createCell(11);
															if (record.getR0360_amount_fcy_non_resident() != null) {
																R82cell11.setCellValue(record.getR0360_amount_fcy_non_resident().doubleValue());
																R82cell11.setCellStyle(numberStyle);
															} else {
																R82cell11.setCellValue("");
																R82cell11.setCellStyle(textStyle);
															}


					
					
/////ROW49///////////
//ROW49
// Column E: 

					row = sheet.getRow(48);

//ROW49
//Column F: 
					Cell R27cell5 = row.createCell(5);
					if (record.getR0380_amount_aed_resident() != null) {
						R27cell5.setCellValue(record.getR0380_amount_aed_resident().doubleValue());
						R27cell5.setCellStyle(numberStyle);
					} else {
						R27cell5.setCellValue("");
						R27cell5.setCellStyle(textStyle);
					}

//ROW49
//Column H: 
					Cell R27cell7 = row.createCell(7);
					if (record.getR0380_amount_fcy_resident() != null) {
						R27cell7.setCellValue(record.getR0380_amount_fcy_resident().doubleValue());
						R27cell7.setCellStyle(numberStyle);
					} else {
						R27cell7.setCellValue("");
						R27cell7.setCellStyle(textStyle);
					}

//ROW49
//Column J: 
					Cell R27cell9 = row.createCell(9);
					if (record.getR0380_amount_aed_non_resident() != null) {
						R27cell9.setCellValue(record.getR0380_amount_aed_non_resident().doubleValue());
						R27cell9.setCellStyle(numberStyle);
					} else {
						R27cell9.setCellValue("");
						R27cell9.setCellStyle(textStyle);
					}

//ROW49
//Column L: 
					Cell R27cell11 = row.createCell(11);
					if (record.getR0380_amount_fcy_non_resident() != null) {
						R27cell11.setCellValue(record.getR0380_amount_fcy_non_resident().doubleValue());
						R27cell11.setCellStyle(numberStyle);
					} else {
						R27cell11.setCellValue("");
						R27cell11.setCellStyle(textStyle);
					}

/////ROW50///////////
//ROW50
// Column E: 

					row = sheet.getRow(49);

//ROW50
//Column F: 
					Cell R28cell5 = row.createCell(5);
					if (record.getR0390_amount_aed_resident() != null) {
						R28cell5.setCellValue(record.getR0390_amount_aed_resident().doubleValue());
						R28cell5.setCellStyle(numberStyle);
					} else {
						R28cell5.setCellValue("");
						R28cell5.setCellStyle(textStyle);
					}

//ROW50
//Column H: 
					Cell R28cell7 = row.createCell(7);
					if (record.getR0390_amount_fcy_resident() != null) {
						R28cell7.setCellValue(record.getR0390_amount_fcy_resident().doubleValue());
						R28cell7.setCellStyle(numberStyle);
					} else {
						R28cell7.setCellValue("");
						R28cell7.setCellStyle(textStyle);
					}

//ROW50
//Column J: 
					Cell R28cell9 = row.createCell(9);
					if (record.getR0390_amount_aed_non_resident() != null) {
						R28cell9.setCellValue(record.getR0390_amount_aed_non_resident().doubleValue());
						R28cell9.setCellStyle(numberStyle);
					} else {
						R28cell9.setCellValue("");
						R28cell9.setCellStyle(textStyle);
					}

//ROW50
//Column L: 
					Cell R28cell11 = row.createCell(11);
					if (record.getR0390_amount_fcy_non_resident() != null) {
						R28cell11.setCellValue(record.getR0390_amount_fcy_non_resident().doubleValue());
						R28cell11.setCellStyle(numberStyle);
					} else {
						R28cell11.setCellValue("");
						R28cell11.setCellStyle(textStyle);
					}

/////ROW51///////////
//ROW51
// Column E: 

					row = sheet.getRow(50);

//ROW51
//Column F: 
					Cell R29cell5 = row.createCell(5);
					if (record.getR0400_amount_aed_resident() != null) {
						R29cell5.setCellValue(record.getR0400_amount_aed_resident().doubleValue());
						R29cell5.setCellStyle(numberStyle);
					} else {
						R29cell5.setCellValue("");
						R29cell5.setCellStyle(textStyle);
					}

//ROW51
//Column H: 
					Cell R29cell7 = row.createCell(7);
					if (record.getR0400_amount_fcy_resident() != null) {
						R29cell7.setCellValue(record.getR0400_amount_fcy_resident().doubleValue());
						R29cell7.setCellStyle(numberStyle);
					} else {
						R29cell7.setCellValue("");
						R29cell7.setCellStyle(textStyle);
					}

//ROW51
//Column J: 
					Cell R29cell9 = row.createCell(9);
					if (record.getR0400_amount_aed_non_resident() != null) {
						R29cell9.setCellValue(record.getR0400_amount_aed_non_resident().doubleValue());
						R29cell9.setCellStyle(numberStyle);
					} else {
						R29cell9.setCellValue("");
						R29cell9.setCellStyle(textStyle);
					}

//ROW51
//Column L: 
					Cell R29cell11 = row.createCell(11);
					if (record.getR0400_amount_fcy_non_resident() != null) {
						R29cell11.setCellValue(record.getR0400_amount_fcy_non_resident().doubleValue());
						R29cell11.setCellStyle(numberStyle);
					} else {
						R29cell11.setCellValue("");
						R29cell11.setCellStyle(textStyle);
					}

/////ROW52///////////
//ROW52
// Column E: 

					row = sheet.getRow(51);

//ROW52
//Column F: 
					Cell R30cell5 = row.createCell(5);
					if (record.getR0410_amount_aed_resident() != null) {
						R30cell5.setCellValue(record.getR0410_amount_aed_resident().doubleValue());
						R30cell5.setCellStyle(numberStyle);
					} else {
						R30cell5.setCellValue("");
						R30cell5.setCellStyle(textStyle);
					}

//ROW52
//Column H: 
					Cell R30cell7 = row.createCell(7);
					if (record.getR0410_amount_fcy_resident() != null) {
						R30cell7.setCellValue(record.getR0410_amount_fcy_resident().doubleValue());
						R30cell7.setCellStyle(numberStyle);
					} else {
						R30cell7.setCellValue("");
						R30cell7.setCellStyle(textStyle);
					}

//ROW52
//Column J: 
					Cell R30cell9 = row.createCell(9);
					if (record.getR0410_amount_aed_non_resident() != null) {
						R30cell9.setCellValue(record.getR0410_amount_aed_non_resident().doubleValue());
						R30cell9.setCellStyle(numberStyle);
					} else {
						R30cell9.setCellValue("");
						R30cell9.setCellStyle(textStyle);
					}

//ROW52
//Column L: 
					Cell R30cell11 = row.createCell(11);
					if (record.getR0410_amount_fcy_non_resident() != null) {
						R30cell11.setCellValue(record.getR0410_amount_fcy_non_resident().doubleValue());
						R30cell11.setCellStyle(numberStyle);
					} else {
						R30cell11.setCellValue("");
						R30cell11.setCellStyle(textStyle);
					}

/////ROW53///////////
//ROW53
// Column E: 

					row = sheet.getRow(52);

//ROW53
//Column F: 
					Cell R31cell5 = row.createCell(5);
					if (record.getR0420_amount_aed_resident() != null) {
						R31cell5.setCellValue(record.getR0420_amount_aed_resident().doubleValue());
						R31cell5.setCellStyle(numberStyle);
					} else {
						R31cell5.setCellValue("");
						R31cell5.setCellStyle(textStyle);
					}

//ROW53
//Column H: 
					Cell R31cell7 = row.createCell(7);
					if (record.getR0420_amount_fcy_resident() != null) {
						R31cell7.setCellValue(record.getR0420_amount_fcy_resident().doubleValue());
						R31cell7.setCellStyle(numberStyle);
					} else {
						R31cell7.setCellValue("");
						R31cell7.setCellStyle(textStyle);
					}

//ROW53
//Column J: 
					Cell R31cell9 = row.createCell(9);
					if (record.getR0420_amount_aed_non_resident() != null) {
						R31cell9.setCellValue(record.getR0420_amount_aed_non_resident().doubleValue());
						R31cell9.setCellStyle(numberStyle);
					} else {
						R31cell9.setCellValue("");
						R31cell9.setCellStyle(textStyle);
					}

//ROW53
//Column L: 
					Cell R31cell11 = row.createCell(11);
					if (record.getR0420_amount_fcy_non_resident() != null) {
						R31cell11.setCellValue(record.getR0420_amount_fcy_non_resident().doubleValue());
						R31cell11.setCellStyle(numberStyle);
					} else {
						R31cell11.setCellValue("");
						R31cell11.setCellStyle(textStyle);
					}

/////ROW54///////////
//ROW54
// Column E: 

					row = sheet.getRow(53);

//ROW54
//Column F: 
					Cell R32cell5 = row.createCell(5);
					if (record.getR0430_amount_aed_resident() != null) {
						R32cell5.setCellValue(record.getR0430_amount_aed_resident().doubleValue());
						R32cell5.setCellStyle(numberStyle);
					} else {
						R32cell5.setCellValue("");
						R32cell5.setCellStyle(textStyle);
					}

//ROW54
//Column H: 
					Cell R32cell7 = row.createCell(7);
					if (record.getR0430_amount_fcy_resident() != null) {
						R32cell7.setCellValue(record.getR0430_amount_fcy_resident().doubleValue());
						R32cell7.setCellStyle(numberStyle);
					} else {
						R32cell7.setCellValue("");
						R32cell7.setCellStyle(textStyle);
					}

//ROW54
//Column J: 
					Cell R32cell9 = row.createCell(9);
					if (record.getR0430_amount_aed_non_resident() != null) {
						R32cell9.setCellValue(record.getR0430_amount_aed_non_resident().doubleValue());
						R32cell9.setCellStyle(numberStyle);
					} else {
						R32cell9.setCellValue("");
						R32cell9.setCellStyle(textStyle);
					}

//ROW54
//Column L: 
					Cell R32cell11 = row.createCell(11);
					if (record.getR0430_amount_fcy_non_resident() != null) {
						R32cell11.setCellValue(record.getR0430_amount_fcy_non_resident().doubleValue());
						R32cell11.setCellStyle(numberStyle);
					} else {
						R32cell11.setCellValue("");
						R32cell11.setCellStyle(textStyle);
					}

/////ROW55///////////
//ROW55
// Column E: 

					row = sheet.getRow(54);

//ROW55
//Column F: 
					Cell R33cell5 = row.createCell(5);
					if (record.getR0440_amount_aed_resident() != null) {
						R33cell5.setCellValue(record.getR0440_amount_aed_resident().doubleValue());
						R33cell5.setCellStyle(numberStyle);
					} else {
						R33cell5.setCellValue("");
						R33cell5.setCellStyle(textStyle);
					}

//ROW55
//Column H: 
					Cell R33cell7 = row.createCell(7);
					if (record.getR0440_amount_fcy_resident() != null) {
						R33cell7.setCellValue(record.getR0440_amount_fcy_resident().doubleValue());
						R33cell7.setCellStyle(numberStyle);
					} else {
						R33cell7.setCellValue("");
						R33cell7.setCellStyle(textStyle);
					}

//ROW55
//Column J: 
					Cell R33cell9 = row.createCell(9);
					if (record.getR0440_amount_aed_non_resident() != null) {
						R33cell9.setCellValue(record.getR0440_amount_aed_non_resident().doubleValue());
						R33cell9.setCellStyle(numberStyle);
					} else {
						R33cell9.setCellValue("");
						R33cell9.setCellStyle(textStyle);
					}

//ROW55
//Column L: 
					Cell R33cell11 = row.createCell(11);
					if (record.getR0440_amount_fcy_non_resident() != null) {
						R33cell11.setCellValue(record.getR0440_amount_fcy_non_resident().doubleValue());
						R33cell11.setCellStyle(numberStyle);
					} else {
						R33cell11.setCellValue("");
						R33cell11.setCellStyle(textStyle);
					}

/////ROW56///////////
//ROW56
// Column E: 

					row = sheet.getRow(55);

//ROW56
//Column F: 
					Cell R36cell5 = row.createCell(5);
					if (record.getR0450_amount_aed_resident() != null) {
						R36cell5.setCellValue(record.getR0450_amount_aed_resident().doubleValue());
						R36cell5.setCellStyle(numberStyle);
					} else {
						R36cell5.setCellValue("");
						R36cell5.setCellStyle(textStyle);
					}

//ROW56
//Column H: 
					Cell R36cell7 = row.createCell(7);
					if (record.getR0450_amount_fcy_resident() != null) {
						R36cell7.setCellValue(record.getR0450_amount_fcy_resident().doubleValue());
						R36cell7.setCellStyle(numberStyle);
					} else {
						R36cell7.setCellValue("");
						R36cell7.setCellStyle(textStyle);
					}

//ROW56
//Column J: 
					Cell R36cell9 = row.createCell(9);
					if (record.getR0450_amount_aed_non_resident() != null) {
						R36cell9.setCellValue(record.getR0450_amount_aed_non_resident().doubleValue());
						R36cell9.setCellStyle(numberStyle);
					} else {
						R36cell9.setCellValue("");
						R36cell9.setCellStyle(textStyle);
					}

//ROW56
//Column L: 
					Cell R36cell11 = row.createCell(11);
					if (record.getR0450_amount_fcy_non_resident() != null) {
						R36cell11.setCellValue(record.getR0450_amount_fcy_non_resident().doubleValue());
						R36cell11.setCellStyle(numberStyle);
					} else {
						R36cell11.setCellValue("");
						R36cell11.setCellStyle(textStyle);
					}

/////ROW58///////////
//ROW58
// Column E: 

					row = sheet.getRow(57);

//ROW58
//Column F: 
					Cell R35cell5 = row.createCell(5);
					if (record.getR0470_amount_aed_resident() != null) {
						R35cell5.setCellValue(record.getR0470_amount_aed_resident().doubleValue());
						R35cell5.setCellStyle(numberStyle);
					} else {
						R35cell5.setCellValue("");
						R35cell5.setCellStyle(textStyle);
					}

//ROW58
//Column H: 
					Cell R35cell7 = row.createCell(7);
					if (record.getR0470_amount_fcy_resident() != null) {
						R35cell7.setCellValue(record.getR0470_amount_fcy_resident().doubleValue());
						R35cell7.setCellStyle(numberStyle);
					} else {
						R35cell7.setCellValue("");
						R35cell7.setCellStyle(textStyle);
					}

//ROW58
//Column J: 
					Cell R35cell9 = row.createCell(9);
					if (record.getR0470_amount_aed_non_resident() != null) {
						R35cell9.setCellValue(record.getR0470_amount_aed_non_resident().doubleValue());
						R35cell9.setCellStyle(numberStyle);
					} else {
						R35cell9.setCellValue("");
						R35cell9.setCellStyle(textStyle);
					}

//ROW58
//Column L: 
					Cell R35cell11 = row.createCell(11);
					if (record.getR0470_amount_fcy_non_resident() != null) {
						R35cell11.setCellValue(record.getR0470_amount_fcy_non_resident().doubleValue());
						R35cell11.setCellStyle(numberStyle);
					} else {
						R35cell11.setCellValue("");
						R35cell11.setCellStyle(textStyle);
					}

/////ROW59///////////
//ROW59
// Column E: 

					row = sheet.getRow(58);

//ROW59
//Column F: 
					Cell R37cell5 = row.createCell(5);
					if (record.getR0480_amount_aed_resident() != null) {
						R37cell5.setCellValue(record.getR0480_amount_aed_resident().doubleValue());
						R37cell5.setCellStyle(numberStyle);
					} else {
						R37cell5.setCellValue("");
						R37cell5.setCellStyle(textStyle);
					}

//ROW59
//Column H: 
					Cell R37cell7 = row.createCell(7);
					if (record.getR0480_amount_fcy_resident() != null) {
						R37cell7.setCellValue(record.getR0480_amount_fcy_resident().doubleValue());
						R37cell7.setCellStyle(numberStyle);
					} else {
						R37cell7.setCellValue("");
						R37cell7.setCellStyle(textStyle);
					}

//ROW59
//Column J: 
					Cell R37cell9 = row.createCell(9);
					if (record.getR0480_amount_aed_non_resident() != null) {
						R37cell9.setCellValue(record.getR0480_amount_aed_non_resident().doubleValue());
						R37cell9.setCellStyle(numberStyle);
					} else {
						R37cell9.setCellValue("");
						R37cell9.setCellStyle(textStyle);
					}

//ROW59
//Column L: 
					Cell R37cell11 = row.createCell(11);
					if (record.getR0480_amount_fcy_non_resident() != null) {
						R37cell11.setCellValue(record.getR0480_amount_fcy_non_resident().doubleValue());
						R37cell11.setCellStyle(numberStyle);
					} else {
						R37cell11.setCellValue("");
						R37cell11.setCellStyle(textStyle);
					}

/////ROW60///////////
//ROW60
// Column E: 

					row = sheet.getRow(59);

//ROW60
//Column F: 
					Cell R38cell5 = row.createCell(5);
					if (record.getR0490_amount_aed_resident() != null) {
						R38cell5.setCellValue(record.getR0490_amount_aed_resident().doubleValue());
						R38cell5.setCellStyle(numberStyle);
					} else {
						R38cell5.setCellValue("");
						R38cell5.setCellStyle(textStyle);
					}

//ROW60
//Column H: 
					Cell R38cell7 = row.createCell(7);
					if (record.getR0490_amount_fcy_resident() != null) {
						R38cell7.setCellValue(record.getR0490_amount_fcy_resident().doubleValue());
						R38cell7.setCellStyle(numberStyle);
					} else {
						R38cell7.setCellValue("");
						R38cell7.setCellStyle(textStyle);
					}

//ROW60
//Column J: 
					Cell R38cell9 = row.createCell(9);
					if (record.getR0490_amount_aed_non_resident() != null) {
						R38cell9.setCellValue(record.getR0490_amount_aed_non_resident().doubleValue());
						R38cell9.setCellStyle(numberStyle);
					} else {
						R38cell9.setCellValue("");
						R38cell9.setCellStyle(textStyle);
					}

//ROW60
//Column L: 
					Cell R38cell11 = row.createCell(11);
					if (record.getR0490_amount_fcy_non_resident() != null) {
						R38cell11.setCellValue(record.getR0490_amount_fcy_non_resident().doubleValue());
						R38cell11.setCellStyle(numberStyle);
					} else {
						R38cell11.setCellValue("");
						R38cell11.setCellStyle(textStyle);
					}

/////ROW61///////////
//ROW61
// Column E: 

					row = sheet.getRow(60);

//ROW61
//Column F: 
					Cell R39cell5 = row.createCell(5);
					if (record.getR0500_amount_aed_resident() != null) {
						R39cell5.setCellValue(record.getR0500_amount_aed_resident().doubleValue());
						R39cell5.setCellStyle(numberStyle);
					} else {
						R39cell5.setCellValue("");
						R39cell5.setCellStyle(textStyle);
					}

//ROW61
//Column H: 
					Cell R39cell7 = row.createCell(7);
					if (record.getR0500_amount_fcy_resident() != null) {
						R39cell7.setCellValue(record.getR0500_amount_fcy_resident().doubleValue());
						R39cell7.setCellStyle(numberStyle);
					} else {
						R39cell7.setCellValue("");
						R39cell7.setCellStyle(textStyle);
					}

//ROW61
//Column J: 
					Cell R39cell9 = row.createCell(9);
					if (record.getR0500_amount_aed_non_resident() != null) {
						R39cell9.setCellValue(record.getR0500_amount_aed_non_resident().doubleValue());
						R39cell9.setCellStyle(numberStyle);
					} else {
						R39cell9.setCellValue("");
						R39cell9.setCellStyle(textStyle);
					}

//ROW61
//Column L: 
					Cell R39cell11 = row.createCell(11);
					if (record.getR0500_amount_fcy_non_resident() != null) {
						R39cell11.setCellValue(record.getR0500_amount_fcy_non_resident().doubleValue());
						R39cell11.setCellStyle(numberStyle);
					} else {
						R39cell11.setCellValue("");
						R39cell11.setCellStyle(textStyle);
					}

/////ROW62///////////
//ROW62
// Column E: 

					row = sheet.getRow(61);

//ROW62
//Column F: 
					Cell R40cell5 = row.createCell(5);
					if (record.getR0510_amount_aed_resident() != null) {
						R40cell5.setCellValue(record.getR0510_amount_aed_resident().doubleValue());
						R40cell5.setCellStyle(numberStyle);
					} else {
						R40cell5.setCellValue("");
						R40cell5.setCellStyle(textStyle);
					}

//ROW62
//Column H: 
					Cell R40cell7 = row.createCell(7);
					if (record.getR0510_amount_fcy_resident() != null) {
						R40cell7.setCellValue(record.getR0510_amount_fcy_resident().doubleValue());
						R40cell7.setCellStyle(numberStyle);
					} else {
						R40cell7.setCellValue("");
						R40cell7.setCellStyle(textStyle);
					}

//ROW62
//Column J: 
					Cell R40cell9 = row.createCell(9);
					if (record.getR0510_amount_aed_non_resident() != null) {
						R40cell9.setCellValue(record.getR0510_amount_aed_non_resident().doubleValue());
						R40cell9.setCellStyle(numberStyle);
					} else {
						R40cell9.setCellValue("");
						R40cell9.setCellStyle(textStyle);
					}

//ROW62
//Column L: 
					Cell R40cell11 = row.createCell(11);
					if (record.getR0510_amount_fcy_non_resident() != null) {
						R40cell11.setCellValue(record.getR0510_amount_fcy_non_resident().doubleValue());
						R40cell11.setCellStyle(numberStyle);
					} else {
						R40cell11.setCellValue("");
						R40cell11.setCellStyle(textStyle);
					}

/////ROW63///////////
//ROW63
// Column E: 

					row = sheet.getRow(62);

//ROW63
//Column F: 
					Cell R41cell5 = row.createCell(5);
					if (record.getR0520_amount_aed_resident() != null) {
						R41cell5.setCellValue(record.getR0520_amount_aed_resident().doubleValue());
						R41cell5.setCellStyle(numberStyle);
					} else {
						R41cell5.setCellValue("");
						R41cell5.setCellStyle(textStyle);
					}

//ROW63
//Column H: 
					Cell R41cell7 = row.createCell(7);
					if (record.getR0520_amount_fcy_resident() != null) {
						R41cell7.setCellValue(record.getR0520_amount_fcy_resident().doubleValue());
						R41cell7.setCellStyle(numberStyle);
					} else {
						R41cell7.setCellValue("");
						R41cell7.setCellStyle(textStyle);
					}

//ROW63
//Column J: 
					Cell R41cell9 = row.createCell(9);
					if (record.getR0520_amount_aed_non_resident() != null) {
						R41cell9.setCellValue(record.getR0520_amount_aed_non_resident().doubleValue());
						R41cell9.setCellStyle(numberStyle);
					} else {
						R41cell9.setCellValue("");
						R41cell9.setCellStyle(textStyle);
					}

//ROW63
//Column L: 
					Cell R41cell11 = row.createCell(11);
					if (record.getR0520_amount_fcy_non_resident() != null) {
						R41cell11.setCellValue(record.getR0520_amount_fcy_non_resident().doubleValue());
						R41cell11.setCellStyle(numberStyle);
					} else {
						R41cell11.setCellValue("");
						R41cell11.setCellStyle(textStyle);
					}

/////ROW64///////////
//ROW64
// Column E: 

					row = sheet.getRow(63);

//ROW64
//Column F: 
					Cell R42cell5 = row.createCell(5);
					if (record.getR0530_amount_aed_resident() != null) {
						R42cell5.setCellValue(record.getR0530_amount_aed_resident().doubleValue());
						R42cell5.setCellStyle(numberStyle);
					} else {
						R42cell5.setCellValue("");
						R42cell5.setCellStyle(textStyle);
					}

//ROW64
//Column H: 
					Cell R42cell7 = row.createCell(7);
					if (record.getR0530_amount_fcy_resident() != null) {
						R42cell7.setCellValue(record.getR0530_amount_fcy_resident().doubleValue());
						R42cell7.setCellStyle(numberStyle);
					} else {
						R42cell7.setCellValue("");
						R42cell7.setCellStyle(textStyle);
					}

//ROW64
//Column J: 
					Cell R42cell9 = row.createCell(9);
					if (record.getR0530_amount_aed_non_resident() != null) {
						R42cell9.setCellValue(record.getR0530_amount_aed_non_resident().doubleValue());
						R42cell9.setCellStyle(numberStyle);
					} else {
						R42cell9.setCellValue("");
						R42cell9.setCellStyle(textStyle);
					}

//ROW64
//Column L: 
					Cell R42cell11 = row.createCell(11);
					if (record.getR0530_amount_fcy_non_resident() != null) {
						R42cell11.setCellValue(record.getR0530_amount_fcy_non_resident().doubleValue());
						R42cell11.setCellStyle(numberStyle);
					} else {
						R42cell11.setCellValue("");
						R42cell11.setCellStyle(textStyle);
					}

/////ROW65///////////
//ROW65
// Column E: 

					row = sheet.getRow(64);

//ROW65
//Column F: 
					Cell R43cell5 = row.createCell(5);
					if (record.getR0540_amount_aed_resident() != null) {
						R43cell5.setCellValue(record.getR0540_amount_aed_resident().doubleValue());
						R43cell5.setCellStyle(numberStyle);
					} else {
						R43cell5.setCellValue("");
						R43cell5.setCellStyle(textStyle);
					}

//ROW65
//Column H: 
					Cell R43cell7 = row.createCell(7);
					if (record.getR0540_amount_fcy_resident() != null) {
						R43cell7.setCellValue(record.getR0540_amount_fcy_resident().doubleValue());
						R43cell7.setCellStyle(numberStyle);
					} else {
						R43cell7.setCellValue("");
						R43cell7.setCellStyle(textStyle);
					}

//ROW65
//Column J: 
					Cell R43cell9 = row.createCell(9);
					if (record.getR0540_amount_aed_non_resident() != null) {
						R43cell9.setCellValue(record.getR0540_amount_aed_non_resident().doubleValue());
						R43cell9.setCellStyle(numberStyle);
					} else {
						R43cell9.setCellValue("");
						R43cell9.setCellStyle(textStyle);
					}

//ROW65
//Column L: 
					Cell R43cell11 = row.createCell(11);
					if (record.getR0540_amount_fcy_non_resident() != null) {
						R43cell11.setCellValue(record.getR0540_amount_fcy_non_resident().doubleValue());
						R43cell11.setCellStyle(numberStyle);
					} else {
						R43cell11.setCellValue("");
						R43cell11.setCellStyle(textStyle);
					}

/////ROW66///////////
//ROW66
// Column E: 

					row = sheet.getRow(65);

//ROW66
//Column F: 
					Cell R44cell5 = row.createCell(5);
					if (record.getR0550_amount_aed_resident() != null) {
						R44cell5.setCellValue(record.getR0550_amount_aed_resident().doubleValue());
						R44cell5.setCellStyle(numberStyle);
					} else {
						R44cell5.setCellValue("");
						R44cell5.setCellStyle(textStyle);
					}

//ROW66
//Column H: 
					Cell R44cell7 = row.createCell(7);
					if (record.getR0550_amount_fcy_resident() != null) {
						R44cell7.setCellValue(record.getR0550_amount_fcy_resident().doubleValue());
						R44cell7.setCellStyle(numberStyle);
					} else {
						R44cell7.setCellValue("");
						R44cell7.setCellStyle(textStyle);
					}

//ROW66
//Column J: 
					Cell R44cell9 = row.createCell(9);
					if (record.getR0550_amount_aed_non_resident() != null) {
						R44cell9.setCellValue(record.getR0550_amount_aed_non_resident().doubleValue());
						R44cell9.setCellStyle(numberStyle);
					} else {
						R44cell9.setCellValue("");
						R44cell9.setCellStyle(textStyle);
					}

//ROW66
//Column L: 
					Cell R44cell11 = row.createCell(11);
					if (record.getR0550_amount_fcy_non_resident() != null) {
						R44cell11.setCellValue(record.getR0550_amount_fcy_non_resident().doubleValue());
						R44cell11.setCellStyle(numberStyle);
					} else {
						R44cell11.setCellValue("");
						R44cell11.setCellStyle(textStyle);
					}

/////ROW67///////////
//ROW67
// Column E: 

					row = sheet.getRow(66);

//ROW67
//Column F: 
					Cell R45cell5 = row.createCell(5);
					if (record.getR0560_amount_aed_resident() != null) {
						R45cell5.setCellValue(record.getR0560_amount_aed_resident().doubleValue());
						R45cell5.setCellStyle(numberStyle);
					} else {
						R45cell5.setCellValue("");
						R45cell5.setCellStyle(textStyle);
					}

//ROW67
//Column H: 
					Cell R45cell7 = row.createCell(7);
					if (record.getR0560_amount_fcy_resident() != null) {
						R45cell7.setCellValue(record.getR0560_amount_fcy_resident().doubleValue());
						R45cell7.setCellStyle(numberStyle);
					} else {
						R45cell7.setCellValue("");
						R45cell7.setCellStyle(textStyle);
					}

//ROW67
//Column J: 
					Cell R45cell9 = row.createCell(9);
					if (record.getR0560_amount_aed_non_resident() != null) {
						R45cell9.setCellValue(record.getR0560_amount_aed_non_resident().doubleValue());
						R45cell9.setCellStyle(numberStyle);
					} else {
						R45cell9.setCellValue("");
						R45cell9.setCellStyle(textStyle);
					}

//ROW67
//Column L: 
					Cell R45cell11 = row.createCell(11);
					if (record.getR0560_amount_fcy_non_resident() != null) {
						R45cell11.setCellValue(record.getR0560_amount_fcy_non_resident().doubleValue());
						R45cell11.setCellStyle(numberStyle);
					} else {
						R45cell11.setCellValue("");
						R45cell11.setCellStyle(textStyle);
					}

/////ROW68///////////
//ROW68
// Column E: 

					row = sheet.getRow(67);

//ROW68
//Column F: 
					Cell R46cell5 = row.createCell(5);
					if (record.getR0570_amount_aed_resident() != null) {
						R46cell5.setCellValue(record.getR0570_amount_aed_resident().doubleValue());
						R46cell5.setCellStyle(numberStyle);
					} else {
						R46cell5.setCellValue("");
						R46cell5.setCellStyle(textStyle);
					}

//ROW68
//Column H: 
					Cell R46cell7 = row.createCell(7);
					if (record.getR0570_amount_fcy_resident() != null) {
						R46cell7.setCellValue(record.getR0570_amount_fcy_resident().doubleValue());
						R46cell7.setCellStyle(numberStyle);
					} else {
						R46cell7.setCellValue("");
						R46cell7.setCellStyle(textStyle);
					}

//ROW68
//Column J: 
					Cell R46cell9 = row.createCell(9);
					if (record.getR0570_amount_aed_non_resident() != null) {
						R46cell9.setCellValue(record.getR0570_amount_aed_non_resident().doubleValue());
						R46cell9.setCellStyle(numberStyle);
					} else {
						R46cell9.setCellValue("");
						R46cell9.setCellStyle(textStyle);
					}

//ROW68
//Column L: 
					Cell R46cell11 = row.createCell(11);
					if (record.getR0570_amount_fcy_non_resident() != null) {
						R46cell11.setCellValue(record.getR0570_amount_fcy_non_resident().doubleValue());
						R46cell11.setCellStyle(numberStyle);
					} else {
						R46cell11.setCellValue("");
						R46cell11.setCellStyle(textStyle);
					}

/////ROW69///////////
//ROW69
// Column E: 

					row = sheet.getRow(68);

//ROW69
//Column F: 
					Cell R47cell5 = row.createCell(5);
					if (record.getR0580_amount_aed_resident() != null) {
						R47cell5.setCellValue(record.getR0580_amount_aed_resident().doubleValue());
						R47cell5.setCellStyle(numberStyle);
					} else {
						R47cell5.setCellValue("");
						R47cell5.setCellStyle(textStyle);
					}

//ROW69
//Column H: 
					Cell R47cell7 = row.createCell(7);
					if (record.getR0580_amount_fcy_resident() != null) {
						R47cell7.setCellValue(record.getR0580_amount_fcy_resident().doubleValue());
						R47cell7.setCellStyle(numberStyle);
					} else {
						R47cell7.setCellValue("");
						R47cell7.setCellStyle(textStyle);
					}

//ROW69
//Column J: 
					Cell R47cell9 = row.createCell(9);
					if (record.getR0580_amount_aed_non_resident() != null) {
						R47cell9.setCellValue(record.getR0580_amount_aed_non_resident().doubleValue());
						R47cell9.setCellStyle(numberStyle);
					} else {
						R47cell9.setCellValue("");
						R47cell9.setCellStyle(textStyle);
					}

//ROW69
//Column L: 
					Cell R47cell11 = row.createCell(11);
					if (record.getR0580_amount_fcy_non_resident() != null) {
						R47cell11.setCellValue(record.getR0580_amount_fcy_non_resident().doubleValue());
						R47cell11.setCellStyle(numberStyle);
					} else {
						R47cell11.setCellValue("");
						R47cell11.setCellStyle(textStyle);
					}

/////ROW71///////////
//ROW71
// Column E: 

					row = sheet.getRow(70);

//ROW71
//Column F: 
					Cell R49cell5 = row.createCell(5);
					if (record.getR0600_amount_aed_resident() != null) {
						R49cell5.setCellValue(record.getR0600_amount_aed_resident().doubleValue());
						R49cell5.setCellStyle(numberStyle);
					} else {
						R49cell5.setCellValue("");
						R49cell5.setCellStyle(textStyle);
					}

//ROW71
//Column H: 
					Cell R49cell7 = row.createCell(7);
					if (record.getR0600_amount_fcy_resident() != null) {
						R49cell7.setCellValue(record.getR0600_amount_fcy_resident().doubleValue());
						R49cell7.setCellStyle(numberStyle);
					} else {
						R49cell7.setCellValue("");
						R49cell7.setCellStyle(textStyle);
					}

//ROW71
//Column J: 
					Cell R49cell9 = row.createCell(9);
					if (record.getR0600_amount_aed_non_resident() != null) {
						R49cell9.setCellValue(record.getR0600_amount_aed_non_resident().doubleValue());
						R49cell9.setCellStyle(numberStyle);
					} else {
						R49cell9.setCellValue("");
						R49cell9.setCellStyle(textStyle);
					}

//ROW71
//Column L: 
					Cell R49cell11 = row.createCell(11);
					if (record.getR0600_amount_fcy_non_resident() != null) {
						R49cell11.setCellValue(record.getR0600_amount_fcy_non_resident().doubleValue());
						R49cell11.setCellStyle(numberStyle);
					} else {
						R49cell11.setCellValue("");
						R49cell11.setCellStyle(textStyle);
					}

/////ROW73///////////
//ROW73
// Column E: 

					row = sheet.getRow(72);

//ROW73
//Column F: 
					Cell R50cell5 = row.createCell(5);
					if (record.getR0620_amount_aed_resident() != null) {
						R50cell5.setCellValue(record.getR0620_amount_aed_resident().doubleValue());
						R50cell5.setCellStyle(numberStyle);
					} else {
						R50cell5.setCellValue("");
						R50cell5.setCellStyle(textStyle);
					}

//ROW73
//Column H: 
					Cell R50cell7 = row.createCell(7);
					if (record.getR0620_amount_fcy_resident() != null) {
						R50cell7.setCellValue(record.getR0620_amount_fcy_resident().doubleValue());
						R50cell7.setCellStyle(numberStyle);
					} else {
						R50cell7.setCellValue("");
						R50cell7.setCellStyle(textStyle);
					}

//ROW73
//Column J: 
					Cell R50cell9 = row.createCell(9);
					if (record.getR0620_amount_aed_non_resident() != null) {
						R50cell9.setCellValue(record.getR0620_amount_aed_non_resident().doubleValue());
						R50cell9.setCellStyle(numberStyle);
					} else {
						R50cell9.setCellValue("");
						R50cell9.setCellStyle(textStyle);
					}

//ROW73
//Column L: 
					Cell R50cell11 = row.createCell(11);
					if (record.getR0620_amount_fcy_non_resident() != null) {
						R50cell11.setCellValue(record.getR0620_amount_fcy_non_resident().doubleValue());
						R50cell11.setCellStyle(numberStyle);
					} else {
						R50cell11.setCellValue("");
						R50cell11.setCellStyle(textStyle);
					}

/////ROW74///////////
//ROW74
// Column E: 

					row = sheet.getRow(73);

//ROW74
//Column F: 
					Cell R51cell5 = row.createCell(5);
					if (record.getR0630_amount_aed_resident() != null) {
						R51cell5.setCellValue(record.getR0630_amount_aed_resident().doubleValue());
						R51cell5.setCellStyle(numberStyle);
					} else {
						R51cell5.setCellValue("");
						R51cell5.setCellStyle(textStyle);
					}

//ROW74
//Column H: 
					Cell R51cell7 = row.createCell(7);
					if (record.getR0630_amount_fcy_resident() != null) {
						R51cell7.setCellValue(record.getR0630_amount_fcy_resident().doubleValue());
						R51cell7.setCellStyle(numberStyle);
					} else {
						R51cell7.setCellValue("");
						R51cell7.setCellStyle(textStyle);
					}

//ROW74
//Column J: 
					Cell R51cell9 = row.createCell(9);
					if (record.getR0630_amount_aed_non_resident() != null) {
						R51cell9.setCellValue(record.getR0630_amount_aed_non_resident().doubleValue());
						R51cell9.setCellStyle(numberStyle);
					} else {
						R51cell9.setCellValue("");
						R51cell9.setCellStyle(textStyle);
					}

//ROW74
//Column L: 
					Cell R51cell11 = row.createCell(11);
					if (record.getR0630_amount_fcy_non_resident() != null) {
						R51cell11.setCellValue(record.getR0630_amount_fcy_non_resident().doubleValue());
						R51cell11.setCellStyle(numberStyle);
					} else {
						R51cell11.setCellValue("");
						R51cell11.setCellStyle(textStyle);
					}

/////ROW75///////////
//ROW75
// Column E: 

					row = sheet.getRow(74);

//ROW75
//Column F: 
					Cell R52cell5 = row.createCell(5);
					if (record.getR0640_amount_aed_resident() != null) {
						R52cell5.setCellValue(record.getR0640_amount_aed_resident().doubleValue());
						R52cell5.setCellStyle(numberStyle);
					} else {
						R52cell5.setCellValue("");
						R52cell5.setCellStyle(textStyle);
					}

//ROW75
//Column H: 
					Cell R52cell7 = row.createCell(7);
					if (record.getR0640_amount_fcy_resident() != null) {
						R52cell7.setCellValue(record.getR0640_amount_fcy_resident().doubleValue());
						R52cell7.setCellStyle(numberStyle);
					} else {
						R52cell7.setCellValue("");
						R52cell7.setCellStyle(textStyle);
					}

//ROW75
//Column J: 
					Cell R52cell9 = row.createCell(9);
					if (record.getR0640_amount_aed_non_resident() != null) {
						R52cell9.setCellValue(record.getR0640_amount_aed_non_resident().doubleValue());
						R52cell9.setCellStyle(numberStyle);
					} else {
						R52cell9.setCellValue("");
						R52cell9.setCellStyle(textStyle);
					}

//ROW75
//Column L: 
					Cell R52cell11 = row.createCell(11);
					if (record.getR0640_amount_fcy_non_resident() != null) {
						R52cell11.setCellValue(record.getR0640_amount_fcy_non_resident().doubleValue());
						R52cell11.setCellStyle(numberStyle);
					} else {
						R52cell11.setCellValue("");
						R52cell11.setCellStyle(textStyle);
					}

/////ROW76///////////
//ROW76
// Column E: 

					row = sheet.getRow(75);

//ROW76
//Column F: 
					Cell R53cell5 = row.createCell(5);
					if (record.getR0650_amount_aed_resident() != null) {
						R53cell5.setCellValue(record.getR0650_amount_aed_resident().doubleValue());
						R53cell5.setCellStyle(numberStyle);
					} else {
						R53cell5.setCellValue("");
						R53cell5.setCellStyle(textStyle);
					}

//ROW76
//Column H: 
					Cell R53cell7 = row.createCell(7);
					if (record.getR0650_amount_fcy_resident() != null) {
						R53cell7.setCellValue(record.getR0650_amount_fcy_resident().doubleValue());
						R53cell7.setCellStyle(numberStyle);
					} else {
						R53cell7.setCellValue("");
						R53cell7.setCellStyle(textStyle);
					}

//ROW76
//Column J: 
					Cell R53cell9 = row.createCell(9);
					if (record.getR0650_amount_aed_non_resident() != null) {
						R53cell9.setCellValue(record.getR0650_amount_aed_non_resident().doubleValue());
						R53cell9.setCellStyle(numberStyle);
					} else {
						R53cell9.setCellValue("");
						R53cell9.setCellStyle(textStyle);
					}

//ROW76
//Column L: 
					Cell R53cell11 = row.createCell(11);
					if (record.getR0650_amount_fcy_non_resident() != null) {
						R53cell11.setCellValue(record.getR0650_amount_fcy_non_resident().doubleValue());
						R53cell11.setCellStyle(numberStyle);
					} else {
						R53cell11.setCellValue("");
						R53cell11.setCellStyle(textStyle);
					}

/////ROW77///////////
//ROW77
// Column E: 

					row = sheet.getRow(76);

//ROW77
//Column F: 
					Cell R54cell5 = row.createCell(5);
					if (record.getR0660_amount_aed_resident() != null) {
						R54cell5.setCellValue(record.getR0660_amount_aed_resident().doubleValue());
						R54cell5.setCellStyle(numberStyle);
					} else {
						R54cell5.setCellValue("");
						R54cell5.setCellStyle(textStyle);
					}

//ROW77
//Column H: 
					Cell R54cell7 = row.createCell(7);
					if (record.getR0660_amount_fcy_resident() != null) {
						R54cell7.setCellValue(record.getR0660_amount_fcy_resident().doubleValue());
						R54cell7.setCellStyle(numberStyle);
					} else {
						R54cell7.setCellValue("");
						R54cell7.setCellStyle(textStyle);
					}

//ROW77
//Column J: 
					Cell R54cell9 = row.createCell(9);
					if (record.getR0660_amount_aed_non_resident() != null) {
						R54cell9.setCellValue(record.getR0660_amount_aed_non_resident().doubleValue());
						R54cell9.setCellStyle(numberStyle);
					} else {
						R54cell9.setCellValue("");
						R54cell9.setCellStyle(textStyle);
					}

//ROW77
//Column L: 
					Cell R54cell11 = row.createCell(11);
					if (record.getR0660_amount_fcy_non_resident() != null) {
						R54cell11.setCellValue(record.getR0660_amount_fcy_non_resident().doubleValue());
						R54cell11.setCellStyle(numberStyle);
					} else {
						R54cell11.setCellValue("");
						R54cell11.setCellStyle(textStyle);
					}

/////ROW78///////////
//ROW78
// Column E: 

					row = sheet.getRow(77);

//ROW78
//Column F: 
					Cell R55cell5 = row.createCell(5);
					if (record.getR0670_amount_aed_resident() != null) {
						R55cell5.setCellValue(record.getR0670_amount_aed_resident().doubleValue());
						R55cell5.setCellStyle(numberStyle);
					} else {
						R55cell5.setCellValue("");
						R55cell5.setCellStyle(textStyle);
					}

//ROW78
//Column H: 
					Cell R55cell7 = row.createCell(7);
					if (record.getR0670_amount_fcy_resident() != null) {
						R55cell7.setCellValue(record.getR0670_amount_fcy_resident().doubleValue());
						R55cell7.setCellStyle(numberStyle);
					} else {
						R55cell7.setCellValue("");
						R55cell7.setCellStyle(textStyle);
					}

//ROW78
//Column J: 
					Cell R55cell9 = row.createCell(9);
					if (record.getR0670_amount_aed_non_resident() != null) {
						R55cell9.setCellValue(record.getR0670_amount_aed_non_resident().doubleValue());
						R55cell9.setCellStyle(numberStyle);
					} else {
						R55cell9.setCellValue("");
						R55cell9.setCellStyle(textStyle);
					}

//ROW78
//Column L: 
					Cell R55cell11 = row.createCell(11);
					if (record.getR0670_amount_fcy_non_resident() != null) {
						R55cell11.setCellValue(record.getR0670_amount_fcy_non_resident().doubleValue());
						R55cell11.setCellStyle(numberStyle);
					} else {
						R55cell11.setCellValue("");
						R55cell11.setCellStyle(textStyle);
					}

/////ROW79///////////
//ROW79
// Column E: 

					row = sheet.getRow(78);

//ROW79
//Column F: 
					Cell R56cell5 = row.createCell(5);
					if (record.getR0680_amount_aed_resident() != null) {
						R56cell5.setCellValue(record.getR0680_amount_aed_resident().doubleValue());
						R56cell5.setCellStyle(numberStyle);
					} else {
						R56cell5.setCellValue("");
						R56cell5.setCellStyle(textStyle);
					}

//ROW79
//Column H: 
					Cell R56cell7 = row.createCell(7);
					if (record.getR0680_amount_fcy_resident() != null) {
						R56cell7.setCellValue(record.getR0680_amount_fcy_resident().doubleValue());
						R56cell7.setCellStyle(numberStyle);
					} else {
						R56cell7.setCellValue("");
						R56cell7.setCellStyle(textStyle);
					}

//ROW79
//Column J: 
					Cell R56cell9 = row.createCell(9);
					if (record.getR0680_amount_aed_non_resident() != null) {
						R56cell9.setCellValue(record.getR0680_amount_aed_non_resident().doubleValue());
						R56cell9.setCellStyle(numberStyle);
					} else {
						R56cell9.setCellValue("");
						R56cell9.setCellStyle(textStyle);
					}

//ROW79
//Column L: 
					Cell R56cell11 = row.createCell(11);
					if (record.getR0680_amount_fcy_non_resident() != null) {
						R56cell11.setCellValue(record.getR0680_amount_fcy_non_resident().doubleValue());
						R56cell11.setCellStyle(numberStyle);
					} else {
						R56cell11.setCellValue("");
						R56cell11.setCellStyle(textStyle);
					}

/////ROW80///////////
//ROW80
// Column E: 

					row = sheet.getRow(79);

//ROW80
//Column F: 
					Cell R57cell5 = row.createCell(5);
					if (record.getR0690_amount_aed_resident() != null) {
						R57cell5.setCellValue(record.getR0690_amount_aed_resident().doubleValue());
						R57cell5.setCellStyle(numberStyle);
					} else {
						R57cell5.setCellValue("");
						R57cell5.setCellStyle(textStyle);
					}

//ROW80
//Column H: 
					Cell R57cell7 = row.createCell(7);
					if (record.getR0690_amount_fcy_resident() != null) {
						R57cell7.setCellValue(record.getR0690_amount_fcy_resident().doubleValue());
						R57cell7.setCellStyle(numberStyle);
					} else {
						R57cell7.setCellValue("");
						R57cell7.setCellStyle(textStyle);
					}

//ROW80
//Column J: 
					Cell R57cell9 = row.createCell(9);
					if (record.getR0690_amount_aed_non_resident() != null) {
						R57cell9.setCellValue(record.getR0690_amount_aed_non_resident().doubleValue());
						R57cell9.setCellStyle(numberStyle);
					} else {
						R57cell9.setCellValue("");
						R57cell9.setCellStyle(textStyle);
					}

//ROW80
//Column L: 
					Cell R57cell11 = row.createCell(11);
					if (record.getR0690_amount_fcy_non_resident() != null) {
						R57cell11.setCellValue(record.getR0690_amount_fcy_non_resident().doubleValue());
						R57cell11.setCellStyle(numberStyle);
					} else {
						R57cell11.setCellValue("");
						R57cell11.setCellStyle(textStyle);
					}

/////ROW81///////////
//ROW81
// Column E: 

					row = sheet.getRow(80);

//ROW81
//Column F: 
					Cell R58cell5 = row.createCell(5);
					if (record.getR0700_amount_aed_resident() != null) {
						R58cell5.setCellValue(record.getR0700_amount_aed_resident().doubleValue());
						R58cell5.setCellStyle(numberStyle);
					} else {
						R58cell5.setCellValue("");
						R58cell5.setCellStyle(textStyle);
					}

//ROW81
//Column H: 
					Cell R58cell7 = row.createCell(7);
					if (record.getR0700_amount_fcy_resident() != null) {
						R58cell7.setCellValue(record.getR0700_amount_fcy_resident().doubleValue());
						R58cell7.setCellStyle(numberStyle);
					} else {
						R58cell7.setCellValue("");
						R58cell7.setCellStyle(textStyle);
					}

//ROW81
//Column J: 
					Cell R58cell9 = row.createCell(9);
					if (record.getR0700_amount_aed_non_resident() != null) {
						R58cell9.setCellValue(record.getR0700_amount_aed_non_resident().doubleValue());
						R58cell9.setCellStyle(numberStyle);
					} else {
						R58cell9.setCellValue("");
						R58cell9.setCellStyle(textStyle);
					}

//ROW81
//Column L: 
					Cell R58cell11 = row.createCell(11);
					if (record.getR0700_amount_fcy_non_resident() != null) {
						R58cell11.setCellValue(record.getR0700_amount_fcy_non_resident().doubleValue());
						R58cell11.setCellStyle(numberStyle);
					} else {
						R58cell11.setCellValue("");
						R58cell11.setCellStyle(textStyle);
					}

/////ROW82///////////
//ROW82
// Column E: 

					row = sheet.getRow(81);

//ROW82
//Column F: 
					Cell R59cell5 = row.createCell(5);
					if (record.getR0710_amount_aed_resident() != null) {
						R59cell5.setCellValue(record.getR0710_amount_aed_resident().doubleValue());
						R59cell5.setCellStyle(numberStyle);
					} else {
						R59cell5.setCellValue("");
						R59cell5.setCellStyle(textStyle);
					}

//ROW82
//Column H: 
					Cell R59cell7 = row.createCell(7);
					if (record.getR0710_amount_fcy_resident() != null) {
						R59cell7.setCellValue(record.getR0710_amount_fcy_resident().doubleValue());
						R59cell7.setCellStyle(numberStyle);
					} else {
						R59cell7.setCellValue("");
						R59cell7.setCellStyle(textStyle);
					}

//ROW82
//Column J: 
					Cell R59cell9 = row.createCell(9);
					if (record.getR0710_amount_aed_non_resident() != null) {
						R59cell9.setCellValue(record.getR0710_amount_aed_non_resident().doubleValue());
						R59cell9.setCellStyle(numberStyle);
					} else {
						R59cell9.setCellValue("");
						R59cell9.setCellStyle(textStyle);
					}

//ROW82
//Column L: 
					Cell R59cell11 = row.createCell(11);
					if (record.getR0710_amount_fcy_non_resident() != null) {
						R59cell11.setCellValue(record.getR0710_amount_fcy_non_resident().doubleValue());
						R59cell11.setCellStyle(numberStyle);
					} else {
						R59cell11.setCellValue("");
						R59cell11.setCellStyle(textStyle);
					}

/////ROW84///////////
//ROW84
// Column E: 

					row = sheet.getRow(83);

//ROW84
//Column F: 
					Cell R60cell5 = row.createCell(5);
					if (record.getR0730_amount_aed_resident() != null) {
						R60cell5.setCellValue(record.getR0730_amount_aed_resident().doubleValue());
						R60cell5.setCellStyle(numberStyle);
					} else {
						R60cell5.setCellValue("");
						R60cell5.setCellStyle(textStyle);
					}

//ROW84
//Column H: 
					Cell R60cell7 = row.createCell(7);
					if (record.getR0730_amount_fcy_resident() != null) {
						R60cell7.setCellValue(record.getR0730_amount_fcy_resident().doubleValue());
						R60cell7.setCellStyle(numberStyle);
					} else {
						R60cell7.setCellValue("");
						R60cell7.setCellStyle(textStyle);
					}

//ROW84
//Column J: 
					Cell R60cell9 = row.createCell(9);
					if (record.getR0730_amount_aed_non_resident() != null) {
						R60cell9.setCellValue(record.getR0730_amount_aed_non_resident().doubleValue());
						R60cell9.setCellStyle(numberStyle);
					} else {
						R60cell9.setCellValue("");
						R60cell9.setCellStyle(textStyle);
					}

//ROW84
//Column L: 
					Cell R60cell11 = row.createCell(11);
					if (record.getR0730_amount_fcy_non_resident() != null) {
						R60cell11.setCellValue(record.getR0730_amount_fcy_non_resident().doubleValue());
						R60cell11.setCellStyle(numberStyle);
					} else {
						R60cell11.setCellValue("");
						R60cell11.setCellStyle(textStyle);
					}

/////ROW85///////////
//ROW85
// Column E: 

					row = sheet.getRow(84);

//ROW85
//Column F: 
					Cell R61cell5 = row.createCell(5);
					if (record.getR0740_amount_aed_resident() != null) {
						R61cell5.setCellValue(record.getR0740_amount_aed_resident().doubleValue());
						R61cell5.setCellStyle(numberStyle);
					} else {
						R61cell5.setCellValue("");
						R61cell5.setCellStyle(textStyle);
					}

//ROW85
//Column H: 
					Cell R61cell7 = row.createCell(7);
					if (record.getR0740_amount_fcy_resident() != null) {
						R61cell7.setCellValue(record.getR0740_amount_fcy_resident().doubleValue());
						R61cell7.setCellStyle(numberStyle);
					} else {
						R61cell7.setCellValue("");
						R61cell7.setCellStyle(textStyle);
					}

//ROW85
//Column J: 
					Cell R61cell9 = row.createCell(9);
					if (record.getR0740_amount_aed_non_resident() != null) {
						R61cell9.setCellValue(record.getR0740_amount_aed_non_resident().doubleValue());
						R61cell9.setCellStyle(numberStyle);
					} else {
						R61cell9.setCellValue("");
						R61cell9.setCellStyle(textStyle);
					}

//ROW85
//Column L: 
					Cell R61cell11 = row.createCell(11);
					if (record.getR0740_amount_fcy_non_resident() != null) {
						R61cell11.setCellValue(record.getR0740_amount_fcy_non_resident().doubleValue());
						R61cell11.setCellStyle(numberStyle);
					} else {
						R61cell11.setCellValue("");
						R61cell11.setCellStyle(textStyle);
					}

/////ROW86///////////
//ROW86
// Column E: 

					row = sheet.getRow(85);

//ROW86
//Column F: 
					Cell R62cell5 = row.createCell(5);
					if (record.getR0750_amount_aed_resident() != null) {
						R62cell5.setCellValue(record.getR0750_amount_aed_resident().doubleValue());
						R62cell5.setCellStyle(numberStyle);
					} else {
						R62cell5.setCellValue("");
						R62cell5.setCellStyle(textStyle);
					}

//ROW86
//Column H: 
					Cell R62cell7 = row.createCell(7);
					if (record.getR0750_amount_fcy_resident() != null) {
						R62cell7.setCellValue(record.getR0750_amount_fcy_resident().doubleValue());
						R62cell7.setCellStyle(numberStyle);
					} else {
						R62cell7.setCellValue("");
						R62cell7.setCellStyle(textStyle);
					}

//ROW86
//Column J: 
					Cell R62cell9 = row.createCell(9);
					if (record.getR0750_amount_aed_non_resident() != null) {
						R62cell9.setCellValue(record.getR0750_amount_aed_non_resident().doubleValue());
						R62cell9.setCellStyle(numberStyle);
					} else {
						R62cell9.setCellValue("");
						R62cell9.setCellStyle(textStyle);
					}

//ROW86
//Column L: 
					Cell R62cell11 = row.createCell(11);
					if (record.getR0750_amount_fcy_non_resident() != null) {
						R62cell11.setCellValue(record.getR0750_amount_fcy_non_resident().doubleValue());
						R62cell11.setCellStyle(numberStyle);
					} else {
						R62cell11.setCellValue("");
						R62cell11.setCellStyle(textStyle);
					}

/////ROW87///////////
//ROW87
// Column E: 

					row = sheet.getRow(86);

//ROW87
//Column F: 
					Cell R63cell5 = row.createCell(5);
					if (record.getR0760_amount_aed_resident() != null) {
						R63cell5.setCellValue(record.getR0760_amount_aed_resident().doubleValue());
						R63cell5.setCellStyle(numberStyle);
					} else {
						R63cell5.setCellValue("");
						R63cell5.setCellStyle(textStyle);
					}

//ROW87
//Column H: 
					Cell R63cell7 = row.createCell(7);
					if (record.getR0760_amount_fcy_resident() != null) {
						R63cell7.setCellValue(record.getR0760_amount_fcy_resident().doubleValue());
						R63cell7.setCellStyle(numberStyle);
					} else {
						R63cell7.setCellValue("");
						R63cell7.setCellStyle(textStyle);
					}

//ROW87
//Column J: 
					Cell R63cell9 = row.createCell(9);
					if (record.getR0760_amount_aed_non_resident() != null) {
						R63cell9.setCellValue(record.getR0760_amount_aed_non_resident().doubleValue());
						R63cell9.setCellStyle(numberStyle);
					} else {
						R63cell9.setCellValue("");
						R63cell9.setCellStyle(textStyle);
					}

//ROW87
//Column L: 
					Cell R63cell11 = row.createCell(11);
					if (record.getR0760_amount_fcy_non_resident() != null) {
						R63cell11.setCellValue(record.getR0760_amount_fcy_non_resident().doubleValue());
						R63cell11.setCellStyle(numberStyle);
					} else {
						R63cell11.setCellValue("");
						R63cell11.setCellStyle(textStyle);
					}

/////ROW88///////////
//ROW88
// Column E: 

					row = sheet.getRow(87);

//ROW88
//Column F: 
					Cell R64cell5 = row.createCell(5);
					if (record.getR0770_amount_aed_resident() != null) {
						R64cell5.setCellValue(record.getR0770_amount_aed_resident().doubleValue());
						R64cell5.setCellStyle(numberStyle);
					} else {
						R64cell5.setCellValue("");
						R64cell5.setCellStyle(textStyle);
					}

//ROW88
//Column H: 
					Cell R64cell7 = row.createCell(7);
					if (record.getR0770_amount_fcy_resident() != null) {
						R64cell7.setCellValue(record.getR0770_amount_fcy_resident().doubleValue());
						R64cell7.setCellStyle(numberStyle);
					} else {
						R64cell7.setCellValue("");
						R64cell7.setCellStyle(textStyle);
					}

//ROW88
//Column J: 
					Cell R64cell9 = row.createCell(9);
					if (record.getR0770_amount_aed_non_resident() != null) {
						R64cell9.setCellValue(record.getR0770_amount_aed_non_resident().doubleValue());
						R64cell9.setCellStyle(numberStyle);
					} else {
						R64cell9.setCellValue("");
						R64cell9.setCellStyle(textStyle);
					}

//ROW88
//Column L: 
					Cell R64cell11 = row.createCell(11);
					if (record.getR0770_amount_fcy_non_resident() != null) {
						R64cell11.setCellValue(record.getR0770_amount_fcy_non_resident().doubleValue());
						R64cell11.setCellStyle(numberStyle);
					} else {
						R64cell11.setCellValue("");
						R64cell11.setCellStyle(textStyle);
					}

/////ROW89///////////
//ROW89
// Column E: 

					row = sheet.getRow(88);

//ROW89
//Column F: 
					Cell R65cell5 = row.createCell(5);
					if (record.getR0780_amount_aed_resident() != null) {
						R65cell5.setCellValue(record.getR0780_amount_aed_resident().doubleValue());
						R65cell5.setCellStyle(numberStyle);
					} else {
						R65cell5.setCellValue("");
						R65cell5.setCellStyle(textStyle);
					}

//ROW89
//Column H: 
					Cell R65cell7 = row.createCell(7);
					if (record.getR0780_amount_fcy_resident() != null) {
						R65cell7.setCellValue(record.getR0780_amount_fcy_resident().doubleValue());
						R65cell7.setCellStyle(numberStyle);
					} else {
						R65cell7.setCellValue("");
						R65cell7.setCellStyle(textStyle);
					}

//ROW89
//Column J: 
					Cell R65cell9 = row.createCell(9);
					if (record.getR0780_amount_aed_non_resident() != null) {
						R65cell9.setCellValue(record.getR0780_amount_aed_non_resident().doubleValue());
						R65cell9.setCellStyle(numberStyle);
					} else {
						R65cell9.setCellValue("");
						R65cell9.setCellStyle(textStyle);
					}

//ROW89
//Column L: 
					Cell R65cell11 = row.createCell(11);
					if (record.getR0780_amount_fcy_non_resident() != null) {
						R65cell11.setCellValue(record.getR0780_amount_fcy_non_resident().doubleValue());
						R65cell11.setCellStyle(numberStyle);
					} else {
						R65cell11.setCellValue("");
						R65cell11.setCellStyle(textStyle);
					}

/////ROW90///////////
//ROW90
// Column E: 

					row = sheet.getRow(89);

//ROW90
//Column F: 
					Cell R66cell5 = row.createCell(5);
					if (record.getR0790_amount_aed_resident() != null) {
						R66cell5.setCellValue(record.getR0790_amount_aed_resident().doubleValue());
						R66cell5.setCellStyle(numberStyle);
					} else {
						R66cell5.setCellValue("");
						R66cell5.setCellStyle(textStyle);
					}

//ROW90
//Column H: 
					Cell R66cell7 = row.createCell(7);
					if (record.getR0790_amount_fcy_resident() != null) {
						R66cell7.setCellValue(record.getR0790_amount_fcy_resident().doubleValue());
						R66cell7.setCellStyle(numberStyle);
					} else {
						R66cell7.setCellValue("");
						R66cell7.setCellStyle(textStyle);
					}

//ROW90
//Column J: 
					Cell R66cell9 = row.createCell(9);
					if (record.getR0790_amount_aed_non_resident() != null) {
						R66cell9.setCellValue(record.getR0790_amount_aed_non_resident().doubleValue());
						R66cell9.setCellStyle(numberStyle);
					} else {
						R66cell9.setCellValue("");
						R66cell9.setCellStyle(textStyle);
					}

//ROW90
//Column L: 
					Cell R66cell11 = row.createCell(11);
					if (record.getR0790_amount_fcy_non_resident() != null) {
						R66cell11.setCellValue(record.getR0790_amount_fcy_non_resident().doubleValue());
						R66cell11.setCellStyle(numberStyle);
					} else {
						R66cell11.setCellValue("");
						R66cell11.setCellStyle(textStyle);
					}

/////ROW93///////////
//ROW93
// Column E: 

					row = sheet.getRow(92);

//ROW93
//Column F: 
					Cell R67cell5 = row.createCell(5);
					if (record.getR0820_amount_aed_resident() != null) {
						R67cell5.setCellValue(record.getR0820_amount_aed_resident().doubleValue());
						R67cell5.setCellStyle(numberStyle);
					} else {
						R67cell5.setCellValue("");
						R67cell5.setCellStyle(textStyle);
					}

//ROW93
//Column H: 
					Cell R67cell7 = row.createCell(7);
					if (record.getR0820_amount_fcy_resident() != null) {
						R67cell7.setCellValue(record.getR0820_amount_fcy_resident().doubleValue());
						R67cell7.setCellStyle(numberStyle);
					} else {
						R67cell7.setCellValue("");
						R67cell7.setCellStyle(textStyle);
					}

//ROW93
//Column J: 
					Cell R67cell9 = row.createCell(9);
					if (record.getR0820_amount_aed_non_resident() != null) {
						R67cell9.setCellValue(record.getR0820_amount_aed_non_resident().doubleValue());
						R67cell9.setCellStyle(numberStyle);
					} else {
						R67cell9.setCellValue("");
						R67cell9.setCellStyle(textStyle);
					}

//ROW93
//Column L: 
					Cell R67cell11 = row.createCell(11);
					if (record.getR0820_amount_fcy_non_resident() != null) {
						R67cell11.setCellValue(record.getR0820_amount_fcy_non_resident().doubleValue());
						R67cell11.setCellStyle(numberStyle);
					} else {
						R67cell11.setCellValue("");
						R67cell11.setCellStyle(textStyle);
					}

/////ROW94///////////
//ROW94
// Column E: 

					row = sheet.getRow(93);

//ROW94
//Column F: 
					Cell R68cell5 = row.createCell(5);
					if (record.getR0830_amount_aed_resident() != null) {
						R68cell5.setCellValue(record.getR0830_amount_aed_resident().doubleValue());
						R68cell5.setCellStyle(numberStyle);
					} else {
						R68cell5.setCellValue("");
						R68cell5.setCellStyle(textStyle);
					}

//ROW94
//Column H: 
					Cell R68cell7 = row.createCell(7);
					if (record.getR0830_amount_fcy_resident() != null) {
						R68cell7.setCellValue(record.getR0830_amount_fcy_resident().doubleValue());
						R68cell7.setCellStyle(numberStyle);
					} else {
						R68cell7.setCellValue("");
						R68cell7.setCellStyle(textStyle);
					}

//ROW94
//Column J: 
					Cell R68cell9 = row.createCell(9);
					if (record.getR0830_amount_aed_non_resident() != null) {
						R68cell9.setCellValue(record.getR0830_amount_aed_non_resident().doubleValue());
						R68cell9.setCellStyle(numberStyle);
					} else {
						R68cell9.setCellValue("");
						R68cell9.setCellStyle(textStyle);
					}

//ROW94
//Column L: 
					Cell R68cell11 = row.createCell(11);
					if (record.getR0830_amount_fcy_non_resident() != null) {
						R68cell11.setCellValue(record.getR0830_amount_fcy_non_resident().doubleValue());
						R68cell11.setCellStyle(numberStyle);
					} else {
						R68cell11.setCellValue("");
						R68cell11.setCellStyle(textStyle);
					}

/////ROW95///////////
//ROW95
// Column E: 

					row = sheet.getRow(94);

//ROW95
//Column F: 
					Cell R69cell5 = row.createCell(5);
					if (record.getR0840_amount_aed_resident() != null) {
						R69cell5.setCellValue(record.getR0840_amount_aed_resident().doubleValue());
						R69cell5.setCellStyle(numberStyle);
					} else {
						R69cell5.setCellValue("");
						R69cell5.setCellStyle(textStyle);
					}

//ROW95
//Column H: 
					Cell R69cell7 = row.createCell(7);
					if (record.getR0840_amount_fcy_resident() != null) {
						R69cell7.setCellValue(record.getR0840_amount_fcy_resident().doubleValue());
						R69cell7.setCellStyle(numberStyle);
					} else {
						R69cell7.setCellValue("");
						R69cell7.setCellStyle(textStyle);
					}

//ROW95
//Column J: 
					Cell R69cell9 = row.createCell(9);
					if (record.getR0840_amount_aed_non_resident() != null) {
						R69cell9.setCellValue(record.getR0840_amount_aed_non_resident().doubleValue());
						R69cell9.setCellStyle(numberStyle);
					} else {
						R69cell9.setCellValue("");
						R69cell9.setCellStyle(textStyle);
					}

//ROW95
//Column L: 
					Cell R69cell11 = row.createCell(11);
					if (record.getR0840_amount_fcy_non_resident() != null) {
						R69cell11.setCellValue(record.getR0840_amount_fcy_non_resident().doubleValue());
						R69cell11.setCellStyle(numberStyle);
					} else {
						R69cell11.setCellValue("");
						R69cell11.setCellStyle(textStyle);
					}

/////ROW97///////////
//ROW97
// Column E: 

					row = sheet.getRow(96);

//ROW97
//Column F: 
					Cell R70cell5 = row.createCell(5);
					if (record.getR0860_amount_aed_resident() != null) {
						R70cell5.setCellValue(record.getR0860_amount_aed_resident().doubleValue());
						R70cell5.setCellStyle(numberStyle);
					} else {
						R70cell5.setCellValue("");
						R70cell5.setCellStyle(textStyle);
					}

//ROW97
//Column H: 
					Cell R70cell7 = row.createCell(7);
					if (record.getR0860_amount_fcy_resident() != null) {
						R70cell7.setCellValue(record.getR0860_amount_fcy_resident().doubleValue());
						R70cell7.setCellStyle(numberStyle);
					} else {
						R70cell7.setCellValue("");
						R70cell7.setCellStyle(textStyle);
					}

//ROW97
//Column J: 
					Cell R70cell9 = row.createCell(9);
					if (record.getR0860_amount_aed_non_resident() != null) {
						R70cell9.setCellValue(record.getR0860_amount_aed_non_resident().doubleValue());
						R70cell9.setCellStyle(numberStyle);
					} else {
						R70cell9.setCellValue("");
						R70cell9.setCellStyle(textStyle);
					}

//ROW97
//Column L: 
					Cell R70cell11 = row.createCell(11);
					if (record.getR0860_amount_fcy_non_resident() != null) {
						R70cell11.setCellValue(record.getR0860_amount_fcy_non_resident().doubleValue());
						R70cell11.setCellStyle(numberStyle);
					} else {
						R70cell11.setCellValue("");
						R70cell11.setCellStyle(textStyle);
					}

/////ROW98///////////
//ROW98
// Column E: 

					row = sheet.getRow(97);

//ROW98
//Column F: 
					Cell R71cell5 = row.createCell(5);
					if (record.getR0870_amount_aed_resident() != null) {
						R71cell5.setCellValue(record.getR0870_amount_aed_resident().doubleValue());
						R71cell5.setCellStyle(numberStyle);
					} else {
						R71cell5.setCellValue("");
						R71cell5.setCellStyle(textStyle);
					}

//ROW98
//Column H: 
					Cell R71cell7 = row.createCell(7);
					if (record.getR0870_amount_fcy_resident() != null) {
						R71cell7.setCellValue(record.getR0870_amount_fcy_resident().doubleValue());
						R71cell7.setCellStyle(numberStyle);
					} else {
						R71cell7.setCellValue("");
						R71cell7.setCellStyle(textStyle);
					}

//ROW98
//Column J: 
					Cell R71cell9 = row.createCell(9);
					if (record.getR0870_amount_aed_non_resident() != null) {
						R71cell9.setCellValue(record.getR0870_amount_aed_non_resident().doubleValue());
						R71cell9.setCellStyle(numberStyle);
					} else {
						R71cell9.setCellValue("");
						R71cell9.setCellStyle(textStyle);
					}

//ROW98
//Column L: 
					Cell R71cell11 = row.createCell(11);
					if (record.getR0870_amount_fcy_non_resident() != null) {
						R71cell11.setCellValue(record.getR0870_amount_fcy_non_resident().doubleValue());
						R71cell11.setCellStyle(numberStyle);
					} else {
						R71cell11.setCellValue("");
						R71cell11.setCellStyle(textStyle);
					}

/////ROW99///////////
//ROW99
// Column E: 

					row = sheet.getRow(98);

//ROW99
//Column F: 
					Cell R72cell5 = row.createCell(5);
					if (record.getR0880_amount_aed_resident() != null) {
						R72cell5.setCellValue(record.getR0880_amount_aed_resident().doubleValue());
						R72cell5.setCellStyle(numberStyle);
					} else {
						R72cell5.setCellValue("");
						R72cell5.setCellStyle(textStyle);
					}

//ROW99
//Column H: 
					Cell R72cell7 = row.createCell(7);
					if (record.getR0880_amount_fcy_resident() != null) {
						R72cell7.setCellValue(record.getR0880_amount_fcy_resident().doubleValue());
						R72cell7.setCellStyle(numberStyle);
					} else {
						R72cell7.setCellValue("");
						R72cell7.setCellStyle(textStyle);
					}

//ROW99
//Column J: 
					Cell R72cell9 = row.createCell(9);
					if (record.getR0880_amount_aed_non_resident() != null) {
						R72cell9.setCellValue(record.getR0880_amount_aed_non_resident().doubleValue());
						R72cell9.setCellStyle(numberStyle);
					} else {
						R72cell9.setCellValue("");
						R72cell9.setCellStyle(textStyle);
					}

//ROW99
//Column L: 
					Cell R72cell11 = row.createCell(11);
					if (record.getR0880_amount_fcy_non_resident() != null) {
						R72cell11.setCellValue(record.getR0880_amount_fcy_non_resident().doubleValue());
						R72cell11.setCellStyle(numberStyle);
					} else {
						R72cell11.setCellValue("");
						R72cell11.setCellStyle(textStyle);
					}

/////ROW100///////////
//ROW100
// Column E: 

					row = sheet.getRow(99);

//ROW100
//Column F: 
					Cell R73cell5 = row.createCell(5);
					if (record.getR0890_amount_aed_resident() != null) {
						R73cell5.setCellValue(record.getR0890_amount_aed_resident().doubleValue());
						R73cell5.setCellStyle(numberStyle);
					} else {
						R73cell5.setCellValue("");
						R73cell5.setCellStyle(textStyle);
					}

//ROW100
//Column H: 
					Cell R73cell7 = row.createCell(7);
					if (record.getR0890_amount_fcy_resident() != null) {
						R73cell7.setCellValue(record.getR0890_amount_fcy_resident().doubleValue());
						R73cell7.setCellStyle(numberStyle);
					} else {
						R73cell7.setCellValue("");
						R73cell7.setCellStyle(textStyle);
					}

//ROW100
//Column J: 
					Cell R73cell9 = row.createCell(9);
					if (record.getR0890_amount_aed_non_resident() != null) {
						R73cell9.setCellValue(record.getR0890_amount_aed_non_resident().doubleValue());
						R73cell9.setCellStyle(numberStyle);
					} else {
						R73cell9.setCellValue("");
						R73cell9.setCellStyle(textStyle);
					}

//ROW100
//Column L: 
					Cell R73cell11 = row.createCell(11);
					if (record.getR0890_amount_fcy_non_resident() != null) {
						R73cell11.setCellValue(record.getR0890_amount_fcy_non_resident().doubleValue());
						R73cell11.setCellStyle(numberStyle);
					} else {
						R73cell11.setCellValue("");
						R73cell11.setCellStyle(textStyle);
					}

/////ROW101///////////
//ROW101
// Column E: 

					row = sheet.getRow(100);

//ROW101
//Column F: 
					Cell R74cell5 = row.createCell(5);
					if (record.getR0900_amount_aed_resident() != null) {
						R74cell5.setCellValue(record.getR0900_amount_aed_resident().doubleValue());
						R74cell5.setCellStyle(numberStyle);
					} else {
						R74cell5.setCellValue("");
						R74cell5.setCellStyle(textStyle);
					}

//ROW101
//Column H: 
					Cell R74cell7 = row.createCell(7);
					if (record.getR0900_amount_fcy_resident() != null) {
						R74cell7.setCellValue(record.getR0900_amount_fcy_resident().doubleValue());
						R74cell7.setCellStyle(numberStyle);
					} else {
						R74cell7.setCellValue("");
						R74cell7.setCellStyle(textStyle);
					}

//ROW101
//Column J: 
					Cell R74cell9 = row.createCell(9);
					if (record.getR0900_amount_aed_non_resident() != null) {
						R74cell9.setCellValue(record.getR0900_amount_aed_non_resident().doubleValue());
						R74cell9.setCellStyle(numberStyle);
					} else {
						R74cell9.setCellValue("");
						R74cell9.setCellStyle(textStyle);
					}

//ROW101
//Column L: 
					Cell R74cell11 = row.createCell(11);
					if (record.getR0900_amount_fcy_non_resident() != null) {
						R74cell11.setCellValue(record.getR0900_amount_fcy_non_resident().doubleValue());
						R74cell11.setCellStyle(numberStyle);
					} else {
						R74cell11.setCellValue("");
						R74cell11.setCellStyle(textStyle);
					}

				}

				int startRow1 = 102;

// dataList1 for entity 2
				if (!dataList1.isEmpty()) {
					for (int i = 0; i < dataList1.size(); i++) {
						CBUAE_BRF1_2_Summary_Entity2 record1 = dataList1.get(i);

						System.out.println("rownumber = " + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						///// ROW102///////////
						// ROW102
						// Column E:

						row = sheet.getRow(101);

						// ROW102
						// Column F:
						Cell R75cell5 = row.createCell(5);
						if (record1.getR0910_amount_aed_resident() != null) {
							R75cell5.setCellValue(record1.getR0910_amount_aed_resident().doubleValue());
							R75cell5.setCellStyle(numberStyle);
						} else {
							
							
							R75cell5.setCellValue("");
							R75cell5.setCellStyle(textStyle);
						}

						// ROW102
						// Column H:
						Cell R75cell7 = row.createCell(7);
						if (record1.getR0910_amount_fcy_resident() != null) {
							R75cell7.setCellValue(record1.getR0910_amount_fcy_resident().doubleValue());
							R75cell7.setCellStyle(numberStyle);
						} else {
							R75cell7.setCellValue("");
							R75cell7.setCellStyle(textStyle);
						}

						// ROW102
						// Column J:
						Cell R75cell9 = row.createCell(9);
						if (record1.getR0910_amount_aed_non_resident() != null) {
							R75cell9.setCellValue(record1.getR0910_amount_aed_non_resident().doubleValue());
							R75cell9.setCellStyle(numberStyle);
						} else {
							R75cell9.setCellValue("");
							R75cell9.setCellStyle(textStyle);
						}

						// ROW102
						// Column L:
						Cell R75cell11 = row.createCell(11);
						if (record1.getR0910_amount_fcy_non_resident() != null) {
							R75cell11.setCellValue(record1.getR0910_amount_fcy_non_resident().doubleValue());
							R75cell11.setCellStyle(numberStyle);
						} else {
							R75cell11.setCellValue("");
							R75cell11.setCellStyle(textStyle);
						}
/////ROW103///////////
						// ROW103
						// Column E:

						row = sheet.getRow(102);

//ROW103
//Column F: 
						Cell R76cell5 = row.createCell(5);
						if (record1.getR0920_amount_aed_resident() != null) {
							R76cell5.setCellValue(record1.getR0920_amount_aed_resident().doubleValue());
							R76cell5.setCellStyle(numberStyle);
						} else {
							R76cell5.setCellValue("");
							R76cell5.setCellStyle(textStyle);
						}

//ROW103
//Column H: 
						Cell R76cell7 = row.createCell(7);
						if (record1.getR0920_amount_fcy_resident() != null) {
							R76cell7.setCellValue(record1.getR0920_amount_fcy_resident().doubleValue());
							R76cell7.setCellStyle(numberStyle);
						} else {
							R76cell7.setCellValue("");
							R76cell7.setCellStyle(textStyle);
						}

//ROW103
//Column J: 
						Cell R76cell9 = row.createCell(9);
						if (record1.getR0920_amount_aed_non_resident() != null) {
							R76cell9.setCellValue(record1.getR0920_amount_aed_non_resident().doubleValue());
							R76cell9.setCellStyle(numberStyle);
						} else {
							R76cell9.setCellValue("");
							R76cell9.setCellStyle(textStyle);
						}

//ROW103
//Column L: 
						Cell R76cell11 = row.createCell(11);
						if (record1.getR0920_amount_fcy_non_resident() != null) {
							R76cell11.setCellValue(record1.getR0920_amount_fcy_non_resident().doubleValue());
							R76cell11.setCellStyle(numberStyle);
						} else {
							R76cell11.setCellValue("");
							R76cell11.setCellStyle(textStyle);
						}

/////ROW110///////////
//ROW110
// Column E: 

						row = sheet.getRow(109);

//ROW110
//Column E: 
						Cell R77cell5 = row.createCell(4);
						if (record1.getR0950_stage1_loans() != null) {
							R77cell5.setCellValue(record1.getR0950_stage1_loans().doubleValue());
							R77cell5.setCellStyle(numberStyle);
						} else {
							R77cell5.setCellValue("");
							R77cell5.setCellStyle(textStyle);
						}

//ROW110
//Column F: 
						Cell R77cell6 = row.createCell(5);
						if (record1.getR0950_stage1_investments() != null) {
							R77cell6.setCellValue(record1.getR0950_stage1_investments().doubleValue());
							R77cell6.setCellStyle(numberStyle);
						} else {
							R77cell6.setCellValue("");
							R77cell6.setCellStyle(textStyle);
						}

//ROW110
//Column G: 
						Cell R77cell7 = row.createCell(6);
						if (record1.getR0950_stage1_all_others() != null) {
							R77cell7.setCellValue(record1.getR0950_stage1_all_others().doubleValue());
							R77cell7.setCellStyle(numberStyle);
						} else {
							R77cell7.setCellValue("");
							R77cell7.setCellStyle(textStyle);
						}

//ROW110
//Column H: 
						Cell R77cell8 = row.createCell(7);
						if (record1.getR0950_stage2_loans() != null) {
							R77cell8.setCellValue(record1.getR0950_stage2_loans().doubleValue());
							R77cell8.setCellStyle(numberStyle);
						} else {
							R77cell8.setCellValue("");
							R77cell8.setCellStyle(textStyle);
						}

//ROW110
//Column I: 
						Cell R77cell9 = row.createCell(8);
						if (record1.getR0950_stage2_investments() != null) {
							R77cell9.setCellValue(record1.getR0950_stage2_investments().doubleValue());
							R77cell9.setCellStyle(numberStyle);
						} else {
							R77cell9.setCellValue("");
							R77cell9.setCellStyle(textStyle);
						}

//ROW110
//Column J: 
						Cell R77cell10 = row.createCell(9);
						if (record1.getR0950_stage2_all_others() != null) {
							R77cell10.setCellValue(record1.getR0950_stage2_all_others().doubleValue());
							R77cell10.setCellStyle(numberStyle);
						} else {
							R77cell10.setCellValue("");
							R77cell10.setCellStyle(textStyle);
						}

//ROW110
//Column K: 
						Cell R77cell11 = row.createCell(10);
						if (record1.getR0950_stage3_loans() != null) {
							R77cell11.setCellValue(record1.getR0950_stage3_loans().doubleValue());
							R77cell11.setCellStyle(numberStyle);
						} else {
							R77cell11.setCellValue("");
							R77cell11.setCellStyle(textStyle);
						}

//ROW110
//Column L: 
						Cell R77cell12 = row.createCell(11);
						if (record1.getR0950_stage3_investments() != null) {
							R77cell12.setCellValue(record1.getR0950_stage3_investments().doubleValue());
							R77cell12.setCellStyle(numberStyle);
						} else {
							R77cell12.setCellValue("");
							R77cell12.setCellStyle(textStyle);
						}

//ROW110
//Column M: 
						Cell R77cell13 = row.createCell(12);
						if (record1.getR0950_stage3_all_others() != null) {
							R77cell13.setCellValue(record1.getR0950_stage3_all_others().doubleValue());
							R77cell13.setCellStyle(numberStyle);
						} else {
							R77cell13.setCellValue("");
							R77cell13.setCellStyle(textStyle);
						}

//ROW110
//Column N: 
						Cell R77cell14 = row.createCell(13);
						if (record1.getR0950_legacy_interest() != null) {
							R77cell14.setCellValue(record1.getR0950_legacy_interest().doubleValue());
							R77cell14.setCellStyle(numberStyle);
						} else {
							R77cell14.setCellValue("");
							R77cell14.setCellStyle(textStyle);
						}

//ROW110
//Column O: 
						Cell R77cell15 = row.createCell(14);
						if (record1.getR0950_provision() != null) {
							R77cell15.setCellValue(record1.getR0950_provision().doubleValue());
							R77cell15.setCellStyle(numberStyle);
						} else {
							R77cell15.setCellValue("");
							R77cell15.setCellStyle(textStyle);
						}

//ROW111
//Column E: 

						row = sheet.getRow(110);

//ROW111
//Column E: 
						Cell R78cell5 = row.createCell(4);
						if (record1.getR0960_stage1_loans() != null) {
							R78cell5.setCellValue(record1.getR0960_stage1_loans().doubleValue());
							R78cell5.setCellStyle(numberStyle);
						} else {
							R78cell5.setCellValue("");
							R78cell5.setCellStyle(textStyle);
						}

//ROW111
//Column F: 
						Cell R78cell6 = row.createCell(5);
						if (record1.getR0960_stage1_investments() != null) {
							R78cell6.setCellValue(record1.getR0960_stage1_investments().doubleValue());
							R78cell6.setCellStyle(numberStyle);
						} else {
							R78cell6.setCellValue("");
							R78cell6.setCellStyle(textStyle);
						}

//ROW111
//Column G: 
						Cell R78cell7 = row.createCell(6);
						if (record1.getR0960_stage1_all_others() != null) {
							R78cell7.setCellValue(record1.getR0960_stage1_all_others().doubleValue());
							R78cell7.setCellStyle(numberStyle);
						} else {
							R78cell7.setCellValue("");
							R78cell7.setCellStyle(textStyle);
						}

//ROW111
//Column H: 
						Cell R78cell8 = row.createCell(7);
						if (record1.getR0960_stage2_loans() != null) {
							R78cell8.setCellValue(record1.getR0960_stage2_loans().doubleValue());
							R78cell8.setCellStyle(numberStyle);
						} else {
							R78cell8.setCellValue("");
							R78cell8.setCellStyle(textStyle);
						}

//ROW111
//Column I: 
						Cell R78cell9 = row.createCell(8);
						if (record1.getR0960_stage2_investments() != null) {
							R78cell9.setCellValue(record1.getR0960_stage2_investments().doubleValue());
							R78cell9.setCellStyle(numberStyle);
						} else {
							R78cell9.setCellValue("");
							R78cell9.setCellStyle(textStyle);
						}

//ROW111
//Column J: 
						Cell R78cell10 = row.createCell(9);
						if (record1.getR0960_stage2_all_others() != null) {
							R78cell10.setCellValue(record1.getR0960_stage2_all_others().doubleValue());
							R78cell10.setCellStyle(numberStyle);
						} else {
							R78cell10.setCellValue("");
							R78cell10.setCellStyle(textStyle);
						}

//ROW111
//Column K: 
						Cell R78cell11 = row.createCell(10);
						if (record1.getR0960_stage3_loans() != null) {
							R78cell11.setCellValue(record1.getR0960_stage3_loans().doubleValue());
							R78cell11.setCellStyle(numberStyle);
						} else {
							R78cell11.setCellValue("");
							R78cell11.setCellStyle(textStyle);
						}

//ROW111
//Column L: 
						Cell R78cell12 = row.createCell(11);
						if (record1.getR0960_stage3_investments() != null) {
							R78cell12.setCellValue(record1.getR0960_stage3_investments().doubleValue());
							R78cell12.setCellStyle(numberStyle);
						} else {
							R78cell12.setCellValue("");
							R78cell12.setCellStyle(textStyle);
						}

//ROW111
//Column M: 
						Cell R78cell13 = row.createCell(12);
						if (record1.getR0960_stage3_all_others() != null) {
							R78cell13.setCellValue(record1.getR0960_stage3_all_others().doubleValue());
							R78cell13.setCellStyle(numberStyle);
						} else {
							R78cell13.setCellValue("");
							R78cell13.setCellStyle(textStyle);
						}

//ROW111
//Column N: 
						Cell R78cell14 = row.createCell(13);
						if (record1.getR0960_legacy_interest() != null) {
							R78cell14.setCellValue(record1.getR0960_legacy_interest().doubleValue());
							R78cell14.setCellStyle(numberStyle);
						} else {
							R78cell14.setCellValue("");
							R78cell14.setCellStyle(textStyle);
						}

//ROW111
//Column O: 
						Cell R78cell15 = row.createCell(14);
						if (record1.getR0960_provision() != null) {
							R78cell15.setCellValue(record1.getR0960_provision().doubleValue());
							R78cell15.setCellStyle(numberStyle);
						} else {
							R78cell15.setCellValue("");
							R78cell15.setCellStyle(textStyle);
						}

//ROW112
//Column E: 

						row = sheet.getRow(111);

//ROW112
//Column E: 
						Cell R79cell5 = row.createCell(4);
						if (record1.getR0970_stage1_loans() != null) {
							R79cell5.setCellValue(record1.getR0970_stage1_loans().doubleValue());
							R79cell5.setCellStyle(numberStyle);
						} else {
							R79cell5.setCellValue("");
							R79cell5.setCellStyle(textStyle);
						}

//ROW112
//Column F: 
						Cell R79cell6 = row.createCell(5);
						if (record1.getR0970_stage1_investments() != null) {
							R79cell6.setCellValue(record1.getR0970_stage1_investments().doubleValue());
							R79cell6.setCellStyle(numberStyle);
						} else {
							R79cell6.setCellValue("");
							R79cell6.setCellStyle(textStyle);
						}

//ROW112
//Column G: 
						Cell R79cell7 = row.createCell(6);
						if (record1.getR0970_stage1_all_others() != null) {
							R79cell7.setCellValue(record1.getR0970_stage1_all_others().doubleValue());
							R79cell7.setCellStyle(numberStyle);
						} else {
							R79cell7.setCellValue("");
							R79cell7.setCellStyle(textStyle);
						}

//ROW112
//Column H: 
						Cell R79cell8 = row.createCell(7);
						if (record1.getR0970_stage2_loans() != null) {
							R79cell8.setCellValue(record1.getR0970_stage2_loans().doubleValue());
							R79cell8.setCellStyle(numberStyle);
						} else {
							R79cell8.setCellValue("");
							R79cell8.setCellStyle(textStyle);
						}

//ROW112
//Column I: 
						Cell R79cell9 = row.createCell(8);
						if (record1.getR0970_stage2_investments() != null) {
							R79cell9.setCellValue(record1.getR0970_stage2_investments().doubleValue());
							R79cell9.setCellStyle(numberStyle);
						} else {
							R79cell9.setCellValue("");
							R79cell9.setCellStyle(textStyle);
						}

//ROW112
//Column J: 
						Cell R79cell10 = row.createCell(9);
						if (record1.getR0970_stage2_all_others() != null) {
							R79cell10.setCellValue(record1.getR0970_stage2_all_others().doubleValue());
							R79cell10.setCellStyle(numberStyle);
						} else {
							R79cell10.setCellValue("");
							R79cell10.setCellStyle(textStyle);
						}

//ROW112
//Column K: 
						Cell R79cell11 = row.createCell(10);
						if (record1.getR0970_stage3_loans() != null) {
							R79cell11.setCellValue(record1.getR0970_stage3_loans().doubleValue());
							R79cell11.setCellStyle(numberStyle);
						} else {
							R79cell11.setCellValue("");
							R79cell11.setCellStyle(textStyle);
						}

//ROW112
//Column L: 
						Cell R79cell12 = row.createCell(11);
						if (record1.getR0970_stage3_investments() != null) {
							R79cell12.setCellValue(record1.getR0970_stage3_investments().doubleValue());
							R79cell12.setCellStyle(numberStyle);
						} else {
							R79cell12.setCellValue("");
							R79cell12.setCellStyle(textStyle);
						}

//ROW112
//Column M: 
						Cell R79cell13 = row.createCell(12);
						if (record1.getR0970_stage3_all_others() != null) {
							R79cell13.setCellValue(record1.getR0970_stage3_all_others().doubleValue());
							R79cell13.setCellStyle(numberStyle);
						} else {
							R79cell13.setCellValue("");
							R79cell13.setCellStyle(textStyle);
						}

//ROW112
//Column N: 
						Cell R79cell14 = row.createCell(13);
						if (record1.getR0970_legacy_interest() != null) {
							R79cell14.setCellValue(record1.getR0970_legacy_interest().doubleValue());
							R79cell14.setCellStyle(numberStyle);
						} else {
							R79cell14.setCellValue("");
							R79cell14.setCellStyle(textStyle);
						}

//ROW112
//Column O: 
						Cell R79cell15 = row.createCell(14);
						if (record1.getR0970_provision() != null) {
							R79cell15.setCellValue(record1.getR0970_provision().doubleValue());
							R79cell15.setCellStyle(numberStyle);
						} else {
							R79cell15.setCellValue("");
							R79cell15.setCellStyle(textStyle);
						}

//ROW113
//Column E: 

						row = sheet.getRow(112);

//ROW113
//Column E: 
						Cell R80cell5 = row.createCell(4);
						if (record1.getR0980_stage1_loans() != null) {
							R80cell5.setCellValue(record1.getR0980_stage1_loans().doubleValue());
							R80cell5.setCellStyle(numberStyle);
						} else {
							R80cell5.setCellValue("");
							R80cell5.setCellStyle(textStyle);
						}

//ROW113
//Column F: 
						Cell R80cell6 = row.createCell(5);
						if (record1.getR0980_stage1_investments() != null) {
							R80cell6.setCellValue(record1.getR0980_stage1_investments().doubleValue());
							R80cell6.setCellStyle(numberStyle);
						} else {
							R80cell6.setCellValue("");
							R80cell6.setCellStyle(textStyle);
						}

//ROW113
//Column G: 
						Cell R80cell7 = row.createCell(6);
						if (record1.getR0980_stage1_all_others() != null) {
							R80cell7.setCellValue(record1.getR0980_stage1_all_others().doubleValue());
							R80cell7.setCellStyle(numberStyle);
						} else {
							R80cell7.setCellValue("");
							R80cell7.setCellStyle(textStyle);
						}

//ROW113
//Column H: 
						Cell R80cell8 = row.createCell(7);
						if (record1.getR0980_stage2_loans() != null) {
							R80cell8.setCellValue(record1.getR0980_stage2_loans().doubleValue());
							R80cell8.setCellStyle(numberStyle);
						} else {
							R80cell8.setCellValue("");
							R80cell8.setCellStyle(textStyle);
						}

//ROW113
//Column I: 
						Cell R80cell9 = row.createCell(8);
						if (record1.getR0980_stage2_investments() != null) {
							R80cell9.setCellValue(record1.getR0980_stage2_investments().doubleValue());
							R80cell9.setCellStyle(numberStyle);
						} else {
							R80cell9.setCellValue("");
							R80cell9.setCellStyle(textStyle);
						}

//ROW113
//Column J: 
						Cell R80cell10 = row.createCell(9);
						if (record1.getR0980_stage2_all_others() != null) {
							R80cell10.setCellValue(record1.getR0980_stage2_all_others().doubleValue());
							R80cell10.setCellStyle(numberStyle);
						} else {
							R80cell10.setCellValue("");
							R80cell10.setCellStyle(textStyle);
						}

//ROW113
//Column K: 
						Cell R80cell11 = row.createCell(10);
						if (record1.getR0980_stage3_loans() != null) {
							R80cell11.setCellValue(record1.getR0980_stage3_loans().doubleValue());
							R80cell11.setCellStyle(numberStyle);
						} else {
							R80cell11.setCellValue("");
							R80cell11.setCellStyle(textStyle);
						}

//ROW113
//Column L: 
						Cell R80cell12 = row.createCell(11);
						if (record1.getR0980_stage3_investments() != null) {
							R80cell12.setCellValue(record1.getR0980_stage3_investments().doubleValue());
							R80cell12.setCellStyle(numberStyle);
						} else {
							R80cell12.setCellValue("");
							R80cell12.setCellStyle(textStyle);
						}

//ROW113
//Column M: 
						Cell R80cell13 = row.createCell(12);
						if (record1.getR0980_stage3_all_others() != null) {
							R80cell13.setCellValue(record1.getR0980_stage3_all_others().doubleValue());
							R80cell13.setCellStyle(numberStyle);
						} else {
							R80cell13.setCellValue("");
							R80cell13.setCellStyle(textStyle);
						}

//ROW113
//Column N: 
						Cell R80cell14 = row.createCell(13);
						if (record1.getR0980_legacy_interest() != null) {
							R80cell14.setCellValue(record1.getR0980_legacy_interest().doubleValue());
							R80cell14.setCellStyle(numberStyle);
						} else {
							R80cell14.setCellValue("");
							R80cell14.setCellStyle(textStyle);
						}

//ROW113
//Column O: 
						Cell R80cell15 = row.createCell(14);
						if (record1.getR0980_provision() != null) {
							R80cell15.setCellValue(record1.getR0980_provision().doubleValue());
							R80cell15.setCellStyle(numberStyle);
						} else {
							R80cell15.setCellValue("");
							R80cell15.setCellStyle(textStyle);
						}

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

	// DetailExcel
	public byte[] getBRF1_2DetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for BRF1_2 Details...");
			System.out.println("came to Detail download service");

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRF1_2Details");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "ROWID", "COLUMNID",
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
			List<CBUAE_BRF1_2_Detail_Entity> reportData = CBUAE_BRF1_2_DETAIL_REPO.getdatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (CBUAE_BRF1_2_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInAed() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInAed().doubleValue());
					} else {
						balanceCell.setCellValue(0.000);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getRowId());
					row.createCell(5).setCellValue(item.getColumnId());
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
				logger.info("No data found for BRF1_2 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRF1_2 Excel", e);
			return new byte[0];
		}
	}

}
