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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.AS_11_Archival_Detail_Entity;
import com.bornfire.brrs.entities.AS_11_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.AS_11_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.AS_11_Detail_Entity;
import com.bornfire.brrs.entities.AS_11_Summary_Entity1;
import com.bornfire.brrs.entities.AS_11_Summary_Entity2;
import com.bornfire.brrs.entities.BRRS_AS_11_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_AS_11_Archival_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_AS_11_Archival_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_AS_11_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_AS_11_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_AS_11_Summary_Repo2;

@Component
@Service

public class BRRS_AS_11_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_AS_11_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_AS_11_Summary_Repo1 AS_11_summary_repo1;

	@Autowired
	BRRS_AS_11_Archival_Summary_Repo1 AS_11_Archival_Summary_Repo1;

	@Autowired
	BRRS_AS_11_Summary_Repo2 AS_11_summary_repo2;

	@Autowired
	BRRS_AS_11_Archival_Summary_Repo2 AS_11_Archival_Summary_Repo2;

	@Autowired
	BRRS_AS_11_Detail_Repo AS_11_detail_repo;

	@Autowired
	BRRS_AS_11_Archival_Detail_Repo AS_11_Archival_Detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getAS_11View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		try {
			Date d1 = dateformat.parse(todate);

			// ---------- CASE 1: ARCHIVAL ----------
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				List<AS_11_Archival_Summary_Entity1> T1Master = AS_11_Archival_Summary_Repo1
						.getdatabydateListarchival(d1, version);
				List<AS_11_Archival_Summary_Entity2> T2Master = AS_11_Archival_Summary_Repo2
						.getdatabydateListarchival(d1, version);

				mv.addObject("reportsummary", T1Master);
				mv.addObject("reportsummary1", T2Master);

				System.out.println("T1Master Size " + T1Master.size());
				System.out.println("T2Master Size " + T2Master.size());

			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<AS_11_Summary_Entity1> T1Master = AS_11_summary_repo1.getdatabydateList(dateformat.parse(todate));
				List<AS_11_Summary_Entity2> T2Master = AS_11_summary_repo2.getdatabydateList(dateformat.parse(todate));

				mv.addObject("reportsummary", T1Master);
				mv.addObject("reportsummary1", T2Master);

				System.out.println("T1Master Size " + T1Master.size());
				System.out.println("T2Master Size " + T2Master.size());
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/AS_11");
		mv.addObject("displaymode", "summary");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	public ModelAndView getAS_11currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {

		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();

		// Session hs = sessionFactory.getCurrentSession();

		try {
			Date parsedDate = null;

			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

			String reportLabel = null;
			String reportAddlCriteria1 = null;
			// ? Split filter string into rowId & columnId
			if (filter != null && filter.contains(",")) {
				String[] parts = filter.split(",");
				if (parts.length >= 2) {
					reportLabel = parts[0];
					reportAddlCriteria1 = parts[1];
				}
			}

			System.out.println(type);
			if ("ARCHIVAL".equals(type) && version != null) {
				System.out.println(type);
				// ?? Archival branch
				List<AS_11_Archival_Detail_Entity> T1Dt1;
				if (reportLabel != null && reportAddlCriteria1 != null) {
					T1Dt1 = AS_11_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1,
							parsedDate, version);
				} else {
					T1Dt1 = AS_11_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// ?? Current branch
				List<AS_11_Detail_Entity> T1Dt1;

				if (reportLabel != null && reportAddlCriteria1 != null) {
					T1Dt1 = AS_11_detail_repo.GetDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1, parsedDate);
				} else {
					T1Dt1 = AS_11_detail_repo.getdatabydateList(parsedDate);
					totalPages = AS_11_detail_repo.getdatacount(parsedDate);
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

		mv.setViewName("BRRS/AS_11");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}

	public byte[] getAS_11Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		System.out.println(type);
		System.out.println(version);
		Date reportDate = dateformat.parse(todate);

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelAS_11ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);

		}

		List<AS_11_Summary_Entity1> dataList = AS_11_summary_repo1.getdatabydateList(dateformat.parse(todate));
		List<AS_11_Summary_Entity2> dataList1 = AS_11_summary_repo2.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for brrs2.4 report. Returning empty result.");
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

			// --- End of Style Definitions ---
			int startRow = 17;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					AS_11_Summary_Entity1 record = dataList.get(i);
					AS_11_Summary_Entity2 record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R18Cell = row.createCell(2);
					if (record.getR18_qua_i_lc() != null) {
						R18Cell.setCellValue(record.getR18_qua_i_lc().doubleValue());
						R18Cell.setCellStyle(numberStyle);
					} else {
						R18Cell.setCellValue("");
						R18Cell.setCellStyle(textStyle);
					}

					Cell R18Cell1 = row.createCell(3);
					if (record.getR18_qua_i_qar() != null) {
						R18Cell1.setCellValue(record.getR18_qua_i_qar().doubleValue());
						R18Cell1.setCellStyle(numberStyle);
					} else {
						R18Cell1.setCellValue("");
						R18Cell1.setCellStyle(textStyle);
					}

					// R18 Col E
					Cell R18Cell2 = row.createCell(4);
					if (record.getR18_qua_i_inr() != null) {
						R18Cell2.setCellValue(record.getR18_qua_i_inr().doubleValue());
						R18Cell2.setCellStyle(numberStyle);
					} else {
						R18Cell2.setCellValue("");
						R18Cell2.setCellStyle(textStyle);
					}

					// R18 Col F
					Cell R18Cell3 = row.createCell(5);
					if (record.getR18_qua_ii_lc() != null) {
						R18Cell3.setCellValue(record.getR18_qua_ii_lc().doubleValue());
						R18Cell3.setCellStyle(numberStyle);
					} else {
						R18Cell3.setCellValue("");
						R18Cell3.setCellStyle(textStyle);
					}
					// R18 Col G
					Cell R18Cell4 = row.createCell(6);
					if (record.getR18_qua_ii_qar() != null) {
						R18Cell4.setCellValue(record.getR18_qua_ii_qar().doubleValue());
						R18Cell4.setCellStyle(numberStyle);
					} else {
						R18Cell4.setCellValue("");
						R18Cell4.setCellStyle(textStyle);
					}
					// R18 Col H
					Cell R18Cell5 = row.createCell(7);
					if (record.getR18_qua_ii_inr() != null) {
						R18Cell5.setCellValue(record.getR18_qua_ii_inr().doubleValue());
						R18Cell5.setCellStyle(numberStyle);
					} else {
						R18Cell5.setCellValue("");
						R18Cell5.setCellStyle(textStyle);
					}
					// R18 Col I
					Cell R18Cell6 = row.createCell(8);
					if (record.getR18_qua_iii_lc() != null) {
						R18Cell6.setCellValue(record.getR18_qua_iii_lc().doubleValue());
						R18Cell6.setCellStyle(numberStyle);
					} else {
						R18Cell6.setCellValue("");
						R18Cell6.setCellStyle(textStyle);
					}
					// R18 Col J
					Cell R18Cell7 = row.createCell(9);
					if (record.getR18_qua_iii_qar() != null) {
						R18Cell7.setCellValue(record.getR18_qua_iii_qar().doubleValue());
						R18Cell7.setCellStyle(numberStyle);
					} else {
						R18Cell7.setCellValue("");
						R18Cell7.setCellStyle(textStyle);
					}
					// R18 Col K
					Cell R18Cell8 = row.createCell(10);
					if (record.getR18_qua_iii_inr() != null) {
						R18Cell8.setCellValue(record.getR18_qua_iii_inr().doubleValue());
						R18Cell8.setCellStyle(numberStyle);
					} else {
						R18Cell8.setCellValue("");
						R18Cell8.setCellStyle(textStyle);
					}
					// R18 Col L
					Cell R18Cell9 = row.createCell(11);
					if (record.getR18_qua_iv_lc() != null) {
						R18Cell9.setCellValue(record.getR18_qua_iv_lc().doubleValue());
						R18Cell9.setCellStyle(numberStyle);
					} else {
						R18Cell9.setCellValue("");
						R18Cell9.setCellStyle(textStyle);
					}
					// R18 Col M
					Cell R18Cell10 = row.createCell(12);
					if (record.getR18_qua_iv_qar() != null) {
						R18Cell10.setCellValue(record.getR18_qua_iv_qar().doubleValue());
						R18Cell10.setCellStyle(numberStyle);
					} else {
						R18Cell10.setCellValue("");
						R18Cell10.setCellStyle(textStyle);
					}

					// R18 Col N
					Cell R18Cell11 = row.createCell(13);
					if (record.getR18_qua_iv_inr() != null) {
						R18Cell11.setCellValue(record.getR18_qua_iv_inr().doubleValue());
						R18Cell11.setCellStyle(numberStyle);
					} else {
						R18Cell11.setCellValue("");
						R18Cell11.setCellStyle(textStyle);
					}
					row = sheet.getRow(18);
					Cell R19Cell = row.createCell(2);
					if (record.getR19_qua_i_lc() != null) {
						R19Cell.setCellValue(record.getR19_qua_i_lc().doubleValue());
						R19Cell.setCellStyle(numberStyle);
					} else {
						R19Cell.setCellValue("");
						R19Cell.setCellStyle(textStyle);
					}

					Cell R19Cell1 = row.createCell(3);
					if (record.getR19_qua_i_qar() != null) {
						R19Cell1.setCellValue(record.getR19_qua_i_qar().doubleValue());
						R19Cell1.setCellStyle(numberStyle);
					} else {
						R19Cell1.setCellValue("");
						R19Cell1.setCellStyle(textStyle);
					}

					// R19 Col E
					Cell R19Cell2 = row.createCell(4);
					if (record.getR19_qua_i_inr() != null) {
						R19Cell2.setCellValue(record.getR19_qua_i_inr().doubleValue());
						R19Cell2.setCellStyle(numberStyle);
					} else {
						R19Cell2.setCellValue("");
						R19Cell2.setCellStyle(textStyle);
					}

					// R19 Col F
					Cell R19Cell3 = row.createCell(5);
					if (record.getR19_qua_ii_lc() != null) {
						R19Cell3.setCellValue(record.getR19_qua_ii_lc().doubleValue());
						R19Cell3.setCellStyle(numberStyle);
					} else {
						R19Cell3.setCellValue("");
						R19Cell3.setCellStyle(textStyle);
					}
					// R19 Col G
					Cell R19Cell4 = row.createCell(6);
					if (record.getR19_qua_ii_qar() != null) {
						R19Cell4.setCellValue(record.getR19_qua_ii_qar().doubleValue());
						R19Cell4.setCellStyle(numberStyle);
					} else {
						R19Cell4.setCellValue("");
						R19Cell4.setCellStyle(textStyle);
					}
					// R19 Col H
					Cell R19Cell5 = row.createCell(7);
					if (record.getR19_qua_ii_inr() != null) {
						R19Cell5.setCellValue(record.getR19_qua_ii_inr().doubleValue());
						R19Cell5.setCellStyle(numberStyle);
					} else {
						R19Cell5.setCellValue("");
						R19Cell5.setCellStyle(textStyle);
					}
					// R19 Col I
					Cell R19Cell6 = row.createCell(8);
					if (record.getR19_qua_iii_lc() != null) {
						R19Cell6.setCellValue(record.getR19_qua_iii_lc().doubleValue());
						R19Cell6.setCellStyle(numberStyle);
					} else {
						R19Cell6.setCellValue("");
						R19Cell6.setCellStyle(textStyle);
					}
					// R19 Col J
					Cell R19Cell7 = row.createCell(9);
					if (record.getR19_qua_iii_qar() != null) {
						R19Cell7.setCellValue(record.getR19_qua_iii_qar().doubleValue());
						R19Cell7.setCellStyle(numberStyle);
					} else {
						R19Cell7.setCellValue("");
						R19Cell7.setCellStyle(textStyle);
					}
					// R19 Col K
					Cell R19Cell8 = row.createCell(10);
					if (record.getR19_qua_iii_inr() != null) {
						R19Cell8.setCellValue(record.getR19_qua_iii_inr().doubleValue());
						R19Cell8.setCellStyle(numberStyle);
					} else {
						R19Cell8.setCellValue("");
						R19Cell8.setCellStyle(textStyle);
					}
					// R19 Col L
					Cell R19Cell9 = row.createCell(11);
					if (record.getR19_qua_iv_lc() != null) {
						R19Cell9.setCellValue(record.getR19_qua_iv_lc().doubleValue());
						R19Cell9.setCellStyle(numberStyle);
					} else {
						R19Cell9.setCellValue("");
						R19Cell9.setCellStyle(textStyle);
					}
					// R19 Col M
					Cell R19Cell10 = row.createCell(12);
					if (record.getR19_qua_iv_qar() != null) {
						R19Cell10.setCellValue(record.getR19_qua_iv_qar().doubleValue());
						R19Cell10.setCellStyle(numberStyle);
					} else {
						R19Cell10.setCellValue("");
						R19Cell10.setCellStyle(textStyle);
					}

					// R19 Col N
					Cell R19Cell11 = row.createCell(13);
					if (record.getR19_qua_iv_inr() != null) {
						R19Cell11.setCellValue(record.getR19_qua_iv_inr().doubleValue());
						R19Cell11.setCellStyle(numberStyle);
					} else {
						R19Cell11.setCellValue("");
						R19Cell11.setCellStyle(textStyle);
					}
					row = sheet.getRow(19);
					Cell R20Cell = row.createCell(2);
					if (record.getR20_qua_i_lc() != null) {
						R20Cell.setCellValue(record.getR20_qua_i_lc().doubleValue());
						R20Cell.setCellStyle(numberStyle);
					} else {
						R20Cell.setCellValue("");
						R20Cell.setCellStyle(textStyle);
					}

					Cell R20Cell1 = row.createCell(3);
					if (record.getR20_qua_i_qar() != null) {
						R20Cell1.setCellValue(record.getR20_qua_i_qar().doubleValue());
						R20Cell1.setCellStyle(numberStyle);
					} else {
						R20Cell1.setCellValue("");
						R20Cell1.setCellStyle(textStyle);
					}

					// R20 Col E
					Cell R20Cell2 = row.createCell(4);
					if (record.getR20_qua_i_inr() != null) {
						R20Cell2.setCellValue(record.getR20_qua_i_inr().doubleValue());
						R20Cell2.setCellStyle(numberStyle);
					} else {
						R20Cell2.setCellValue("");
						R20Cell2.setCellStyle(textStyle);
					}

					// R20 Col F
					Cell R20Cell3 = row.createCell(5);
					if (record.getR20_qua_ii_lc() != null) {
						R20Cell3.setCellValue(record.getR20_qua_ii_lc().doubleValue());
						R20Cell3.setCellStyle(numberStyle);
					} else {
						R20Cell3.setCellValue("");
						R20Cell3.setCellStyle(textStyle);
					}
					// R20 Col G
					Cell R20Cell4 = row.createCell(6);
					if (record.getR20_qua_ii_qar() != null) {
						R20Cell4.setCellValue(record.getR20_qua_ii_qar().doubleValue());
						R20Cell4.setCellStyle(numberStyle);
					} else {
						R20Cell4.setCellValue("");
						R20Cell4.setCellStyle(textStyle);
					}
					// R20 Col H
					Cell R20Cell5 = row.createCell(7);
					if (record.getR20_qua_ii_inr() != null) {
						R20Cell5.setCellValue(record.getR20_qua_ii_inr().doubleValue());
						R20Cell5.setCellStyle(numberStyle);
					} else {
						R20Cell5.setCellValue("");
						R20Cell5.setCellStyle(textStyle);
					}
					// R20 Col I
					Cell R20Cell6 = row.createCell(8);
					if (record.getR20_qua_iii_lc() != null) {
						R20Cell6.setCellValue(record.getR20_qua_iii_lc().doubleValue());
						R20Cell6.setCellStyle(numberStyle);
					} else {
						R20Cell6.setCellValue("");
						R20Cell6.setCellStyle(textStyle);
					}
					// R20 Col J
					Cell R20Cell7 = row.createCell(9);
					if (record.getR20_qua_iii_qar() != null) {
						R20Cell7.setCellValue(record.getR20_qua_iii_qar().doubleValue());
						R20Cell7.setCellStyle(numberStyle);
					} else {
						R20Cell7.setCellValue("");
						R20Cell7.setCellStyle(textStyle);
					}
					// R20 Col K
					Cell R20Cell8 = row.createCell(10);
					if (record.getR20_qua_iii_inr() != null) {
						R20Cell8.setCellValue(record.getR20_qua_iii_inr().doubleValue());
						R20Cell8.setCellStyle(numberStyle);
					} else {
						R20Cell8.setCellValue("");
						R20Cell8.setCellStyle(textStyle);
					}
					// R20 Col L
					Cell R20Cell9 = row.createCell(11);
					if (record.getR20_qua_iv_lc() != null) {
						R20Cell9.setCellValue(record.getR20_qua_iv_lc().doubleValue());
						R20Cell9.setCellStyle(numberStyle);
					} else {
						R20Cell9.setCellValue("");
						R20Cell9.setCellStyle(textStyle);
					}
					// R20 Col M
					Cell R20Cell10 = row.createCell(12);
					if (record.getR20_qua_iv_qar() != null) {
						R20Cell10.setCellValue(record.getR20_qua_iv_qar().doubleValue());
						R20Cell10.setCellStyle(numberStyle);
					} else {
						R20Cell10.setCellValue("");
						R20Cell10.setCellStyle(textStyle);
					}

					// R20 Col N
					Cell R20Cell11 = row.createCell(13);
					if (record.getR20_qua_iv_inr() != null) {
						R20Cell11.setCellValue(record.getR20_qua_iv_inr().doubleValue());
						R20Cell11.setCellStyle(numberStyle);
					} else {
						R20Cell11.setCellValue("");
						R20Cell11.setCellStyle(textStyle);
					}
					row = sheet.getRow(20);
					Cell R21Cell = row.createCell(2);
					if (record.getR21_qua_i_lc() != null) {
						R21Cell.setCellValue(record.getR21_qua_i_lc().doubleValue());
						R21Cell.setCellStyle(numberStyle);
					} else {
						R21Cell.setCellValue("");
						R21Cell.setCellStyle(textStyle);
					}

					Cell R21Cell1 = row.createCell(3);
					if (record.getR21_qua_i_qar() != null) {
						R21Cell1.setCellValue(record.getR21_qua_i_qar().doubleValue());
						R21Cell1.setCellStyle(numberStyle);
					} else {
						R21Cell1.setCellValue("");
						R21Cell1.setCellStyle(textStyle);
					}

					// R21 Col E
					Cell R21Cell2 = row.createCell(4);
					if (record.getR21_qua_i_inr() != null) {
						R21Cell2.setCellValue(record.getR21_qua_i_inr().doubleValue());
						R21Cell2.setCellStyle(numberStyle);
					} else {
						R21Cell2.setCellValue("");
						R21Cell2.setCellStyle(textStyle);
					}

					// R21 Col F
					Cell R21Cell3 = row.createCell(5);
					if (record.getR21_qua_ii_lc() != null) {
						R21Cell3.setCellValue(record.getR21_qua_ii_lc().doubleValue());
						R21Cell3.setCellStyle(numberStyle);
					} else {
						R21Cell3.setCellValue("");
						R21Cell3.setCellStyle(textStyle);
					}
					// R21 Col G
					Cell R21Cell4 = row.createCell(6);
					if (record.getR21_qua_ii_qar() != null) {
						R21Cell4.setCellValue(record.getR21_qua_ii_qar().doubleValue());
						R21Cell4.setCellStyle(numberStyle);
					} else {
						R21Cell4.setCellValue("");
						R21Cell4.setCellStyle(textStyle);
					}
					// R21 Col H
					Cell R21Cell5 = row.createCell(7);
					if (record.getR21_qua_ii_inr() != null) {
						R21Cell5.setCellValue(record.getR21_qua_ii_inr().doubleValue());
						R21Cell5.setCellStyle(numberStyle);
					} else {
						R21Cell5.setCellValue("");
						R21Cell5.setCellStyle(textStyle);
					}
					// R21 Col I
					Cell R21Cell6 = row.createCell(8);
					if (record.getR21_qua_iii_lc() != null) {
						R21Cell6.setCellValue(record.getR21_qua_iii_lc().doubleValue());
						R21Cell6.setCellStyle(numberStyle);
					} else {
						R21Cell6.setCellValue("");
						R21Cell6.setCellStyle(textStyle);
					}
					// R21 Col J
					Cell R21Cell7 = row.createCell(9);
					if (record.getR21_qua_iii_qar() != null) {
						R21Cell7.setCellValue(record.getR21_qua_iii_qar().doubleValue());
						R21Cell7.setCellStyle(numberStyle);
					} else {
						R21Cell7.setCellValue("");
						R21Cell7.setCellStyle(textStyle);
					}
					// R21 Col K
					Cell R21Cell8 = row.createCell(10);
					if (record.getR21_qua_iii_inr() != null) {
						R21Cell8.setCellValue(record.getR21_qua_iii_inr().doubleValue());
						R21Cell8.setCellStyle(numberStyle);
					} else {
						R21Cell8.setCellValue("");
						R21Cell8.setCellStyle(textStyle);
					}
					// R21 Col L
					Cell R21Cell9 = row.createCell(11);
					if (record.getR21_qua_iv_lc() != null) {
						R21Cell9.setCellValue(record.getR21_qua_iv_lc().doubleValue());
						R21Cell9.setCellStyle(numberStyle);
					} else {
						R21Cell9.setCellValue("");
						R21Cell9.setCellStyle(textStyle);
					}
					// R21 Col M
					Cell R21Cell10 = row.createCell(12);
					if (record.getR21_qua_iv_qar() != null) {
						R21Cell10.setCellValue(record.getR21_qua_iv_qar().doubleValue());
						R21Cell10.setCellStyle(numberStyle);
					} else {
						R21Cell10.setCellValue("");
						R21Cell10.setCellStyle(textStyle);
					}

					// R21 Col N
					Cell R21Cell11 = row.createCell(13);
					if (record.getR21_qua_iv_inr() != null) {
						R21Cell11.setCellValue(record.getR21_qua_iv_inr().doubleValue());
						R21Cell11.setCellStyle(numberStyle);
					} else {
						R21Cell11.setCellValue("");
						R21Cell11.setCellStyle(textStyle);
					}
					row = sheet.getRow(21);
					Cell R22Cell1 = row.createCell(3);

					if (record.getR22_qua_i_qar() != null) {
						R22Cell1.setCellValue(record.getR22_qua_i_qar().doubleValue());
					} else {
						// ✅ Default 0
						R22Cell1.setCellValue(0);
					}

					// Always apply number style
					R22Cell1.setCellStyle(numberStyle);
					row = sheet.getRow(22);
					Cell R23Cell = row.createCell(2);
					if (record.getR23_qua_i_lc() != null) {
						R23Cell.setCellValue(record.getR23_qua_i_lc().doubleValue());
						R23Cell.setCellStyle(numberStyle);
					} else {
						R23Cell.setCellValue("");
						R23Cell.setCellStyle(textStyle);
					}

					Cell R23Cell1 = row.createCell(3);
					if (record.getR23_qua_i_qar() != null) {
						R23Cell1.setCellValue(record.getR23_qua_i_qar().doubleValue());
						R23Cell1.setCellStyle(numberStyle);
					} else {
						R23Cell1.setCellValue("");
						R23Cell1.setCellStyle(textStyle);
					}

					// R23 Col E
					Cell R23Cell2 = row.createCell(4);
					if (record.getR23_qua_i_inr() != null) {
						R23Cell2.setCellValue(record.getR23_qua_i_inr().doubleValue());
						R23Cell2.setCellStyle(numberStyle);
					} else {
						R23Cell2.setCellValue("");
						R23Cell2.setCellStyle(textStyle);
					}

					// R23 Col F
					Cell R23Cell3 = row.createCell(5);
					if (record.getR23_qua_ii_lc() != null) {
						R23Cell3.setCellValue(record.getR23_qua_ii_lc().doubleValue());
						R23Cell3.setCellStyle(numberStyle);
					} else {
						R23Cell3.setCellValue("");
						R23Cell3.setCellStyle(textStyle);
					}
					// R23 Col G
					Cell R23Cell4 = row.createCell(6);
					if (record.getR23_qua_ii_qar() != null) {
						R23Cell4.setCellValue(record.getR23_qua_ii_qar().doubleValue());
						R23Cell4.setCellStyle(numberStyle);
					} else {
						R23Cell4.setCellValue("");
						R23Cell4.setCellStyle(textStyle);
					}
					// R23 Col H
					Cell R23Cell5 = row.createCell(7);
					if (record.getR23_qua_ii_inr() != null) {
						R23Cell5.setCellValue(record.getR23_qua_ii_inr().doubleValue());
						R23Cell5.setCellStyle(numberStyle);
					} else {
						R23Cell5.setCellValue("");
						R23Cell5.setCellStyle(textStyle);
					}
					// R23 Col I
					Cell R23Cell6 = row.createCell(8);
					if (record.getR23_qua_iii_lc() != null) {
						R23Cell6.setCellValue(record.getR23_qua_iii_lc().doubleValue());
						R23Cell6.setCellStyle(numberStyle);
					} else {
						R23Cell6.setCellValue("");
						R23Cell6.setCellStyle(textStyle);
					}
					// R23 Col J
					Cell R23Cell7 = row.createCell(9);
					if (record.getR23_qua_iii_qar() != null) {
						R23Cell7.setCellValue(record.getR23_qua_iii_qar().doubleValue());
						R23Cell7.setCellStyle(numberStyle);
					} else {
						R23Cell7.setCellValue("");
						R23Cell7.setCellStyle(textStyle);
					}
					// R23 Col K
					Cell R23Cell8 = row.createCell(10);
					if (record.getR23_qua_iii_inr() != null) {
						R23Cell8.setCellValue(record.getR23_qua_iii_inr().doubleValue());
						R23Cell8.setCellStyle(numberStyle);
					} else {
						R23Cell8.setCellValue("");
						R23Cell8.setCellStyle(textStyle);
					}
					// R23 Col L
					Cell R23Cell9 = row.createCell(11);
					if (record.getR23_qua_iv_lc() != null) {
						R23Cell9.setCellValue(record.getR23_qua_iv_lc().doubleValue());
						R23Cell9.setCellStyle(numberStyle);
					} else {
						R23Cell9.setCellValue("");
						R23Cell9.setCellStyle(textStyle);
					}
					// R23 Col M
					Cell R23Cell10 = row.createCell(12);
					if (record.getR23_qua_iv_qar() != null) {
						R23Cell10.setCellValue(record.getR23_qua_iv_qar().doubleValue());
						R23Cell10.setCellStyle(numberStyle);
					} else {
						R23Cell10.setCellValue("");
						R23Cell10.setCellStyle(textStyle);
					}

					// R23 Col N
					Cell R23Cell11 = row.createCell(13);
					if (record.getR23_qua_iv_inr() != null) {
						R23Cell11.setCellValue(record.getR23_qua_iv_inr().doubleValue());
						R23Cell11.setCellStyle(numberStyle);
					} else {
						R23Cell11.setCellValue("");
						R23Cell11.setCellStyle(textStyle);
					}
					// R23 Col O
					Cell R23Cell12 = row.createCell(14);
					if (record.getR23_cumm_inr() != null) {
						R23Cell12.setCellValue(record.getR23_cumm_inr().doubleValue());
						R23Cell12.setCellStyle(numberStyle);
					} else {
						R23Cell12.setCellValue("");
						R23Cell12.setCellStyle(textStyle);
					}
					row = sheet.getRow(23);
					Cell R24Cell = row.createCell(2);
					if (record.getR24_qua_i_lc() != null) {
						R24Cell.setCellValue(record.getR24_qua_i_lc().doubleValue());
						R24Cell.setCellStyle(numberStyle);
					} else {
						R24Cell.setCellValue("");
						R24Cell.setCellStyle(textStyle);
					}

					Cell R24Cell1 = row.createCell(3);
					if (record.getR24_qua_i_qar() != null) {
						R24Cell1.setCellValue(record.getR24_qua_i_qar().doubleValue());
						R24Cell1.setCellStyle(numberStyle);
					} else {
						R24Cell1.setCellValue("");
						R24Cell1.setCellStyle(textStyle);
					}

					// R24 Col E
					Cell R24Cell2 = row.createCell(4);
					if (record.getR24_qua_i_inr() != null) {
						R24Cell2.setCellValue(record.getR24_qua_i_inr().doubleValue());
						R24Cell2.setCellStyle(numberStyle);
					} else {
						R24Cell2.setCellValue("");
						R24Cell2.setCellStyle(textStyle);
					}

					// R24 Col F
					Cell R24Cell3 = row.createCell(5);
					if (record.getR24_qua_ii_lc() != null) {
						R24Cell3.setCellValue(record.getR24_qua_ii_lc().doubleValue());
						R24Cell3.setCellStyle(numberStyle);
					} else {
						R24Cell3.setCellValue("");
						R24Cell3.setCellStyle(textStyle);
					}
					// R24 Col G
					Cell R24Cell4 = row.createCell(6);
					if (record.getR24_qua_ii_qar() != null) {
						R24Cell4.setCellValue(record.getR24_qua_ii_qar().doubleValue());
						R24Cell4.setCellStyle(numberStyle);
					} else {
						R24Cell4.setCellValue("");
						R24Cell4.setCellStyle(textStyle);
					}
					// R24 Col H
					Cell R24Cell5 = row.createCell(7);
					if (record.getR24_qua_ii_inr() != null) {
						R24Cell5.setCellValue(record.getR24_qua_ii_inr().doubleValue());
						R24Cell5.setCellStyle(numberStyle);
					} else {
						R24Cell5.setCellValue("");
						R24Cell5.setCellStyle(textStyle);
					}
					// R24 Col I
					Cell R24Cell6 = row.createCell(8);
					if (record.getR24_qua_iii_lc() != null) {
						R24Cell6.setCellValue(record.getR24_qua_iii_lc().doubleValue());
						R24Cell6.setCellStyle(numberStyle);
					} else {
						R24Cell6.setCellValue("");
						R24Cell6.setCellStyle(textStyle);
					}
					// R24 Col J
					Cell R24Cell7 = row.createCell(9);
					if (record.getR24_qua_iii_qar() != null) {
						R24Cell7.setCellValue(record.getR24_qua_iii_qar().doubleValue());
						R24Cell7.setCellStyle(numberStyle);
					} else {
						R24Cell7.setCellValue("");
						R24Cell7.setCellStyle(textStyle);
					}
					// R24 Col K
					Cell R24Cell8 = row.createCell(10);
					if (record.getR24_qua_iii_inr() != null) {
						R24Cell8.setCellValue(record.getR24_qua_iii_inr().doubleValue());
						R24Cell8.setCellStyle(numberStyle);
					} else {
						R24Cell8.setCellValue("");
						R24Cell8.setCellStyle(textStyle);
					}
					// R24 Col L
					Cell R24Cell9 = row.createCell(11);
					if (record.getR24_qua_iv_lc() != null) {
						R24Cell9.setCellValue(record.getR24_qua_iv_lc().doubleValue());
						R24Cell9.setCellStyle(numberStyle);
					} else {
						R24Cell9.setCellValue("");
						R24Cell9.setCellStyle(textStyle);
					}
					// R24 Col M
					Cell R24Cell10 = row.createCell(12);
					if (record.getR24_qua_iv_qar() != null) {
						R24Cell10.setCellValue(record.getR24_qua_iv_qar().doubleValue());
						R24Cell10.setCellStyle(numberStyle);
					} else {
						R24Cell10.setCellValue("");
						R24Cell10.setCellStyle(textStyle);
					}

					// R24 Col N
					Cell R24Cell11 = row.createCell(13);
					if (record.getR24_qua_iv_inr() != null) {
						R24Cell11.setCellValue(record.getR24_qua_iv_inr().doubleValue());
						R24Cell11.setCellStyle(numberStyle);
					} else {
						R24Cell11.setCellValue("");
						R24Cell11.setCellStyle(textStyle);
					}
					// R24 Col O
					Cell R24Cell12 = row.createCell(14);
					if (record.getR24_cumm_inr() != null) {
						R24Cell12.setCellValue(record.getR24_cumm_inr().doubleValue());
						R24Cell12.setCellStyle(numberStyle);
					} else {
						R24Cell12.setCellValue("");
						R24Cell12.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					Cell R25Cell = row.createCell(2);
					if (record.getR25_qua_i_lc() != null) {
						R25Cell.setCellValue(record.getR25_qua_i_lc().doubleValue());
						R25Cell.setCellStyle(numberStyle);
					} else {
						R25Cell.setCellValue("");
						R25Cell.setCellStyle(textStyle);
					}

					Cell R25Cell1 = row.createCell(3);
					if (record.getR25_qua_i_qar() != null) {
						R25Cell1.setCellValue(record.getR25_qua_i_qar().doubleValue());
						R25Cell1.setCellStyle(numberStyle);
					} else {
						R25Cell1.setCellValue("");
						R25Cell1.setCellStyle(textStyle);
					}

					// R25 Col E
					Cell R25Cell2 = row.createCell(4);
					if (record.getR25_qua_i_inr() != null) {
						R25Cell2.setCellValue(record.getR25_qua_i_inr().doubleValue());
						R25Cell2.setCellStyle(numberStyle);
					} else {
						R25Cell2.setCellValue("");
						R25Cell2.setCellStyle(textStyle);
					}

					// R25 Col F
					Cell R25Cell3 = row.createCell(5);
					if (record.getR25_qua_ii_lc() != null) {
						R25Cell3.setCellValue(record.getR25_qua_ii_lc().doubleValue());
						R25Cell3.setCellStyle(numberStyle);
					} else {
						R25Cell3.setCellValue("");
						R25Cell3.setCellStyle(textStyle);
					}
					// R25 Col G
					Cell R25Cell4 = row.createCell(6);
					if (record.getR25_qua_ii_qar() != null) {
						R25Cell4.setCellValue(record.getR25_qua_ii_qar().doubleValue());
						R25Cell4.setCellStyle(numberStyle);
					} else {
						R25Cell4.setCellValue("");
						R25Cell4.setCellStyle(textStyle);
					}
					// R25 Col H
					Cell R25Cell5 = row.createCell(7);
					if (record.getR25_qua_ii_inr() != null) {
						R25Cell5.setCellValue(record.getR25_qua_ii_inr().doubleValue());
						R25Cell5.setCellStyle(numberStyle);
					} else {
						R25Cell5.setCellValue("");
						R25Cell5.setCellStyle(textStyle);
					}
					// R25 Col I
					Cell R25Cell6 = row.createCell(8);
					if (record.getR25_qua_iii_lc() != null) {
						R25Cell6.setCellValue(record.getR25_qua_iii_lc().doubleValue());
						R25Cell6.setCellStyle(numberStyle);
					} else {
						R25Cell6.setCellValue("");
						R25Cell6.setCellStyle(textStyle);
					}
					// R25 Col J
					Cell R25Cell7 = row.createCell(9);
					if (record.getR25_qua_iii_qar() != null) {
						R25Cell7.setCellValue(record.getR25_qua_iii_qar().doubleValue());
						R25Cell7.setCellStyle(numberStyle);
					} else {
						R25Cell7.setCellValue("");
						R25Cell7.setCellStyle(textStyle);
					}
					// R25 Col K
					Cell R25Cell8 = row.createCell(10);
					if (record.getR25_qua_iii_inr() != null) {
						R25Cell8.setCellValue(record.getR25_qua_iii_inr().doubleValue());
						R25Cell8.setCellStyle(numberStyle);
					} else {
						R25Cell8.setCellValue("");
						R25Cell8.setCellStyle(textStyle);
					}
					// R25 Col L
					Cell R25Cell9 = row.createCell(11);
					if (record.getR25_qua_iv_lc() != null) {
						R25Cell9.setCellValue(record.getR25_qua_iv_lc().doubleValue());
						R25Cell9.setCellStyle(numberStyle);
					} else {
						R25Cell9.setCellValue("");
						R25Cell9.setCellStyle(textStyle);
					}
					// R25 Col M
					Cell R25Cell10 = row.createCell(12);
					if (record.getR25_qua_iv_qar() != null) {
						R25Cell10.setCellValue(record.getR25_qua_iv_qar().doubleValue());
						R25Cell10.setCellStyle(numberStyle);
					} else {
						R25Cell10.setCellValue("");
						R25Cell10.setCellStyle(textStyle);
					}

					// R25 Col N
					Cell R25Cell11 = row.createCell(13);
					if (record.getR25_qua_iv_inr() != null) {
						R25Cell11.setCellValue(record.getR25_qua_iv_inr().doubleValue());
						R25Cell11.setCellStyle(numberStyle);
					} else {
						R25Cell11.setCellValue("");
						R25Cell11.setCellStyle(textStyle);
					}

					// R25 Col O
					Cell R25Cell12 = row.createCell(14);
					if (record.getR25_cumm_inr() != null) {
						R25Cell12.setCellValue(record.getR25_cumm_inr().doubleValue());
						R25Cell12.setCellStyle(numberStyle);
					} else {
						R25Cell12.setCellValue("");
						R25Cell12.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					Cell R26Cell = row.createCell(2);
					if (record.getR26_qua_i_lc() != null) {
						R26Cell.setCellValue(record.getR26_qua_i_lc().doubleValue());
						R26Cell.setCellStyle(numberStyle);
					} else {
						R26Cell.setCellValue("");
						R26Cell.setCellStyle(textStyle);
					}

					Cell R26Cell1 = row.createCell(3);
					if (record.getR26_qua_i_qar() != null) {
						R26Cell1.setCellValue(record.getR26_qua_i_qar().doubleValue());
						R26Cell1.setCellStyle(numberStyle);
					} else {
						R26Cell1.setCellValue("");
						R26Cell1.setCellStyle(textStyle);
					}

					// R26 Col E
					Cell R26Cell2 = row.createCell(4);
					if (record.getR26_qua_i_inr() != null) {
						R26Cell2.setCellValue(record.getR26_qua_i_inr().doubleValue());
						R26Cell2.setCellStyle(numberStyle);
					} else {
						R26Cell2.setCellValue("");
						R26Cell2.setCellStyle(textStyle);
					}

					// R26 Col F
					Cell R26Cell3 = row.createCell(5);
					if (record.getR26_qua_ii_lc() != null) {
						R26Cell3.setCellValue(record.getR26_qua_ii_lc().doubleValue());
						R26Cell3.setCellStyle(numberStyle);
					} else {
						R26Cell3.setCellValue("");
						R26Cell3.setCellStyle(textStyle);
					}
					// R26 Col G
					Cell R26Cell4 = row.createCell(6);
					if (record.getR26_qua_ii_qar() != null) {
						R26Cell4.setCellValue(record.getR26_qua_ii_qar().doubleValue());
						R26Cell4.setCellStyle(numberStyle);
					} else {
						R26Cell4.setCellValue("");
						R26Cell4.setCellStyle(textStyle);
					}
					// R26 Col H
					Cell R26Cell5 = row.createCell(7);
					if (record.getR26_qua_ii_inr() != null) {
						R26Cell5.setCellValue(record.getR26_qua_ii_inr().doubleValue());
						R26Cell5.setCellStyle(numberStyle);
					} else {
						R26Cell5.setCellValue("");
						R26Cell5.setCellStyle(textStyle);
					}
					// R26 Col I
					Cell R26Cell6 = row.createCell(8);
					if (record.getR26_qua_iii_lc() != null) {
						R26Cell6.setCellValue(record.getR26_qua_iii_lc().doubleValue());
						R26Cell6.setCellStyle(numberStyle);
					} else {
						R26Cell6.setCellValue("");
						R26Cell6.setCellStyle(textStyle);
					}
					// R26 Col J
					Cell R26Cell7 = row.createCell(9);
					if (record.getR26_qua_iii_qar() != null) {
						R26Cell7.setCellValue(record.getR26_qua_iii_qar().doubleValue());
						R26Cell7.setCellStyle(numberStyle);
					} else {
						R26Cell7.setCellValue("");
						R26Cell7.setCellStyle(textStyle);
					}
					// R26 Col K
					Cell R26Cell8 = row.createCell(10);
					if (record.getR26_qua_iii_inr() != null) {
						R26Cell8.setCellValue(record.getR26_qua_iii_inr().doubleValue());
						R26Cell8.setCellStyle(numberStyle);
					} else {
						R26Cell8.setCellValue("");
						R26Cell8.setCellStyle(textStyle);
					}
					// R26 Col L
					Cell R26Cell9 = row.createCell(11);
					if (record.getR26_qua_iv_lc() != null) {
						R26Cell9.setCellValue(record.getR26_qua_iv_lc().doubleValue());
						R26Cell9.setCellStyle(numberStyle);
					} else {
						R26Cell9.setCellValue("");
						R26Cell9.setCellStyle(textStyle);
					}
					// R26 Col M
					Cell R26Cell10 = row.createCell(12);
					if (record.getR26_qua_iv_qar() != null) {
						R26Cell10.setCellValue(record.getR26_qua_iv_qar().doubleValue());
						R26Cell10.setCellStyle(numberStyle);
					} else {
						R26Cell10.setCellValue("");
						R26Cell10.setCellStyle(textStyle);
					}

					// R26 Col N
					Cell R26Cell11 = row.createCell(13);
					if (record.getR26_qua_iv_inr() != null) {
						R26Cell11.setCellValue(record.getR26_qua_iv_inr().doubleValue());
						R26Cell11.setCellStyle(numberStyle);
					} else {
						R26Cell11.setCellValue("");
						R26Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(26);
					Cell R27Cell = row.createCell(2);
					if (record.getR27_qua_i_lc() != null) {
						R27Cell.setCellValue(record.getR27_qua_i_lc().doubleValue());
						R27Cell.setCellStyle(numberStyle);
					} else {
						R27Cell.setCellValue("");
						R27Cell.setCellStyle(textStyle);
					}

					Cell R27Cell1 = row.createCell(3);
					if (record.getR27_qua_i_qar() != null) {
						R27Cell1.setCellValue(record.getR27_qua_i_qar().doubleValue());
						R27Cell1.setCellStyle(numberStyle);
					} else {
						R27Cell1.setCellValue("");
						R27Cell1.setCellStyle(textStyle);
					}

					// R27 Col E
					Cell R27Cell2 = row.createCell(4);
					if (record.getR27_qua_i_inr() != null) {
						R27Cell2.setCellValue(record.getR27_qua_i_inr().doubleValue());
						R27Cell2.setCellStyle(numberStyle);
					} else {
						R27Cell2.setCellValue("");
						R27Cell2.setCellStyle(textStyle);
					}

					// R27 Col F
					Cell R27Cell3 = row.createCell(5);
					if (record.getR27_qua_ii_lc() != null) {
						R27Cell3.setCellValue(record.getR27_qua_ii_lc().doubleValue());
						R27Cell3.setCellStyle(numberStyle);
					} else {
						R27Cell3.setCellValue("");
						R27Cell3.setCellStyle(textStyle);
					}
					// R27 Col G
					Cell R27Cell4 = row.createCell(6);
					if (record.getR27_qua_ii_qar() != null) {
						R27Cell4.setCellValue(record.getR27_qua_ii_qar().doubleValue());
						R27Cell4.setCellStyle(numberStyle);
					} else {
						R27Cell4.setCellValue("");
						R27Cell4.setCellStyle(textStyle);
					}
					// R27 Col H
					Cell R27Cell5 = row.createCell(7);
					if (record.getR27_qua_ii_inr() != null) {
						R27Cell5.setCellValue(record.getR27_qua_ii_inr().doubleValue());
						R27Cell5.setCellStyle(numberStyle);
					} else {
						R27Cell5.setCellValue("");
						R27Cell5.setCellStyle(textStyle);
					}
					// R27 Col I
					Cell R27Cell6 = row.createCell(8);
					if (record.getR27_qua_iii_lc() != null) {
						R27Cell6.setCellValue(record.getR27_qua_iii_lc().doubleValue());
						R27Cell6.setCellStyle(numberStyle);
					} else {
						R27Cell6.setCellValue("");
						R27Cell6.setCellStyle(textStyle);
					}
					// R27 Col J
					Cell R27Cell7 = row.createCell(9);
					if (record.getR27_qua_iii_qar() != null) {
						R27Cell7.setCellValue(record.getR27_qua_iii_qar().doubleValue());
						R27Cell7.setCellStyle(numberStyle);
					} else {
						R27Cell7.setCellValue("");
						R27Cell7.setCellStyle(textStyle);
					}
					// R27 Col K
					Cell R27Cell8 = row.createCell(10);
					if (record.getR27_qua_iii_inr() != null) {
						R27Cell8.setCellValue(record.getR27_qua_iii_inr().doubleValue());
						R27Cell8.setCellStyle(numberStyle);
					} else {
						R27Cell8.setCellValue("");
						R27Cell8.setCellStyle(textStyle);
					}
					// R27 Col L
					Cell R27Cell9 = row.createCell(11);
					if (record.getR27_qua_iv_lc() != null) {
						R27Cell9.setCellValue(record.getR27_qua_iv_lc().doubleValue());
						R27Cell9.setCellStyle(numberStyle);
					} else {
						R27Cell9.setCellValue("");
						R27Cell9.setCellStyle(textStyle);
					}
					// R27 Col M
					Cell R27Cell10 = row.createCell(12);
					if (record.getR27_qua_iv_qar() != null) {
						R27Cell10.setCellValue(record.getR27_qua_iv_qar().doubleValue());
						R27Cell10.setCellStyle(numberStyle);
					} else {
						R27Cell10.setCellValue("");
						R27Cell10.setCellStyle(textStyle);
					}

					// R27 Col N
					Cell R27Cell11 = row.createCell(13);
					if (record.getR27_qua_iv_inr() != null) {
						R27Cell11.setCellValue(record.getR27_qua_iv_inr().doubleValue());
						R27Cell11.setCellStyle(numberStyle);
					} else {
						R27Cell11.setCellValue("");
						R27Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					Cell R28Cell = row.createCell(2);
					if (record.getR28_qua_i_lc() != null) {
						R28Cell.setCellValue(record.getR28_qua_i_lc().doubleValue());
						R28Cell.setCellStyle(numberStyle);
					} else {
						R28Cell.setCellValue("");
						R28Cell.setCellStyle(textStyle);
					}

					Cell R28Cell1 = row.createCell(3);
					if (record.getR28_qua_i_qar() != null) {
						R28Cell1.setCellValue(record.getR28_qua_i_qar().doubleValue());
						R28Cell1.setCellStyle(numberStyle);
					} else {
						R28Cell1.setCellValue("");
						R28Cell1.setCellStyle(textStyle);
					}

					// R28 Col E
					Cell R28Cell2 = row.createCell(4);
					if (record.getR28_qua_i_inr() != null) {
						R28Cell2.setCellValue(record.getR28_qua_i_inr().doubleValue());
						R28Cell2.setCellStyle(numberStyle);
					} else {
						R28Cell2.setCellValue("");
						R28Cell2.setCellStyle(textStyle);
					}

					// R28 Col F
					Cell R28Cell3 = row.createCell(5);
					if (record.getR28_qua_ii_lc() != null) {
						R28Cell3.setCellValue(record.getR28_qua_ii_lc().doubleValue());
						R28Cell3.setCellStyle(numberStyle);
					} else {
						R28Cell3.setCellValue("");
						R28Cell3.setCellStyle(textStyle);
					}
					// R28 Col G
					Cell R28Cell4 = row.createCell(6);
					if (record.getR28_qua_ii_qar() != null) {
						R28Cell4.setCellValue(record.getR28_qua_ii_qar().doubleValue());
						R28Cell4.setCellStyle(numberStyle);
					} else {
						R28Cell4.setCellValue("");
						R28Cell4.setCellStyle(textStyle);
					}
					// R28 Col H
					Cell R28Cell5 = row.createCell(7);
					if (record.getR28_qua_ii_inr() != null) {
						R28Cell5.setCellValue(record.getR28_qua_ii_inr().doubleValue());
						R28Cell5.setCellStyle(numberStyle);
					} else {
						R28Cell5.setCellValue("");
						R28Cell5.setCellStyle(textStyle);
					}
					// R28 Col I
					Cell R28Cell6 = row.createCell(8);
					if (record.getR28_qua_iii_lc() != null) {
						R28Cell6.setCellValue(record.getR28_qua_iii_lc().doubleValue());
						R28Cell6.setCellStyle(numberStyle);
					} else {
						R28Cell6.setCellValue("");
						R28Cell6.setCellStyle(textStyle);
					}
					// R28 Col J
					Cell R28Cell7 = row.createCell(9);
					if (record.getR28_qua_iii_qar() != null) {
						R28Cell7.setCellValue(record.getR28_qua_iii_qar().doubleValue());
						R28Cell7.setCellStyle(numberStyle);
					} else {
						R28Cell7.setCellValue("");
						R28Cell7.setCellStyle(textStyle);
					}
					// R28 Col K
					Cell R28Cell8 = row.createCell(10);
					if (record.getR28_qua_iii_inr() != null) {
						R28Cell8.setCellValue(record.getR28_qua_iii_inr().doubleValue());
						R28Cell8.setCellStyle(numberStyle);
					} else {
						R28Cell8.setCellValue("");
						R28Cell8.setCellStyle(textStyle);
					}
					// R28 Col L
					Cell R28Cell9 = row.createCell(11);
					if (record.getR28_qua_iv_lc() != null) {
						R28Cell9.setCellValue(record.getR28_qua_iv_lc().doubleValue());
						R28Cell9.setCellStyle(numberStyle);
					} else {
						R28Cell9.setCellValue("");
						R28Cell9.setCellStyle(textStyle);
					}
					// R28 Col M
					Cell R28Cell10 = row.createCell(12);
					if (record.getR28_qua_iv_qar() != null) {
						R28Cell10.setCellValue(record.getR28_qua_iv_qar().doubleValue());
						R28Cell10.setCellStyle(numberStyle);
					} else {
						R28Cell10.setCellValue("");
						R28Cell10.setCellStyle(textStyle);
					}

					// R28 Col N
					Cell R28Cell11 = row.createCell(13);
					if (record.getR28_qua_iv_inr() != null) {
						R28Cell11.setCellValue(record.getR28_qua_iv_inr().doubleValue());
						R28Cell11.setCellStyle(numberStyle);
					} else {
						R28Cell11.setCellValue("");
						R28Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					Cell R29Cell = row.createCell(2);
					if (record.getR29_qua_i_lc() != null) {
						R29Cell.setCellValue(record.getR29_qua_i_lc().doubleValue());
						R29Cell.setCellStyle(numberStyle);
					} else {
						R29Cell.setCellValue("");
						R29Cell.setCellStyle(textStyle);
					}

					Cell R29Cell1 = row.createCell(3);
					if (record.getR29_qua_i_qar() != null) {
						R29Cell1.setCellValue(record.getR29_qua_i_qar().doubleValue());
						R29Cell1.setCellStyle(numberStyle);
					} else {
						R29Cell1.setCellValue("");
						R29Cell1.setCellStyle(textStyle);
					}

					// R29 Col E
					Cell R29Cell2 = row.createCell(4);
					if (record.getR29_qua_i_inr() != null) {
						R29Cell2.setCellValue(record.getR29_qua_i_inr().doubleValue());
						R29Cell2.setCellStyle(numberStyle);
					} else {
						R29Cell2.setCellValue("");
						R29Cell2.setCellStyle(textStyle);
					}

					// R29 Col F
					Cell R29Cell3 = row.createCell(5);
					if (record.getR29_qua_ii_lc() != null) {
						R29Cell3.setCellValue(record.getR29_qua_ii_lc().doubleValue());
						R29Cell3.setCellStyle(numberStyle);
					} else {
						R29Cell3.setCellValue("");
						R29Cell3.setCellStyle(textStyle);
					}
					// R29 Col G
					Cell R29Cell4 = row.createCell(6);
					if (record.getR29_qua_ii_qar() != null) {
						R29Cell4.setCellValue(record.getR29_qua_ii_qar().doubleValue());
						R29Cell4.setCellStyle(numberStyle);
					} else {
						R29Cell4.setCellValue("");
						R29Cell4.setCellStyle(textStyle);
					}
					// R29 Col H
					Cell R29Cell5 = row.createCell(7);
					if (record.getR29_qua_ii_inr() != null) {
						R29Cell5.setCellValue(record.getR29_qua_ii_inr().doubleValue());
						R29Cell5.setCellStyle(numberStyle);
					} else {
						R29Cell5.setCellValue("");
						R29Cell5.setCellStyle(textStyle);
					}
					// R29 Col I
					Cell R29Cell6 = row.createCell(8);
					if (record.getR29_qua_iii_lc() != null) {
						R29Cell6.setCellValue(record.getR29_qua_iii_lc().doubleValue());
						R29Cell6.setCellStyle(numberStyle);
					} else {
						R29Cell6.setCellValue("");
						R29Cell6.setCellStyle(textStyle);
					}
					// R29 Col J
					Cell R29Cell7 = row.createCell(9);
					if (record.getR29_qua_iii_qar() != null) {
						R29Cell7.setCellValue(record.getR29_qua_iii_qar().doubleValue());
						R29Cell7.setCellStyle(numberStyle);
					} else {
						R29Cell7.setCellValue("");
						R29Cell7.setCellStyle(textStyle);
					}
					// R29 Col K
					Cell R29Cell8 = row.createCell(10);
					if (record.getR29_qua_iii_inr() != null) {
						R29Cell8.setCellValue(record.getR29_qua_iii_inr().doubleValue());
						R29Cell8.setCellStyle(numberStyle);
					} else {
						R29Cell8.setCellValue("");
						R29Cell8.setCellStyle(textStyle);
					}
					// R29 Col L
					Cell R29Cell9 = row.createCell(11);
					if (record.getR29_qua_iv_lc() != null) {
						R29Cell9.setCellValue(record.getR29_qua_iv_lc().doubleValue());
						R29Cell9.setCellStyle(numberStyle);
					} else {
						R29Cell9.setCellValue("");
						R29Cell9.setCellStyle(textStyle);
					}
					// R29 Col M
					Cell R29Cell10 = row.createCell(12);
					if (record.getR29_qua_iv_qar() != null) {
						R29Cell10.setCellValue(record.getR29_qua_iv_qar().doubleValue());
						R29Cell10.setCellStyle(numberStyle);
					} else {
						R29Cell10.setCellValue("");
						R29Cell10.setCellStyle(textStyle);
					}

					// R29 Col N
					Cell R29Cell11 = row.createCell(13);
					if (record.getR29_qua_iv_inr() != null) {
						R29Cell11.setCellValue(record.getR29_qua_iv_inr().doubleValue());
						R29Cell11.setCellStyle(numberStyle);
					} else {
						R29Cell11.setCellValue("");
						R29Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(29);
					// R30 Col C
					Cell R30Cell = row.createCell(2);
					if (record.getR30_qua_i_lc() != null) {
						R30Cell.setCellValue(record.getR30_qua_i_lc().doubleValue());
						R30Cell.setCellStyle(numberStyle);
					} else {
						R30Cell.setCellValue("");
						R30Cell.setCellStyle(textStyle);
					}

// R30 Col D
					Cell R30Cell1 = row.createCell(3);
					if (record.getR30_qua_i_qar() != null) {
						R30Cell1.setCellValue(record.getR30_qua_i_qar().doubleValue());
						R30Cell1.setCellStyle(numberStyle);
					} else {
						R30Cell1.setCellValue("");
						R30Cell1.setCellStyle(textStyle);
					}

// R30 Col E
					Cell R30Cell2 = row.createCell(4);
					if (record.getR30_qua_i_inr() != null) {
						R30Cell2.setCellValue(record.getR30_qua_i_inr().doubleValue());
						R30Cell2.setCellStyle(numberStyle);
					} else {
						R30Cell2.setCellValue("");
						R30Cell2.setCellStyle(textStyle);
					}

// R30 Col F
					Cell R30Cell3 = row.createCell(5);
					if (record.getR30_qua_ii_lc() != null) {
						R30Cell3.setCellValue(record.getR30_qua_ii_lc().doubleValue());
						R30Cell3.setCellStyle(numberStyle);
					} else {
						R30Cell3.setCellValue("");
						R30Cell3.setCellStyle(textStyle);
					}

// R30 Col G
					Cell R30Cell4 = row.createCell(6);
					if (record.getR30_qua_ii_qar() != null) {
						R30Cell4.setCellValue(record.getR30_qua_ii_qar().doubleValue());
						R30Cell4.setCellStyle(numberStyle);
					} else {
						R30Cell4.setCellValue("");
						R30Cell4.setCellStyle(textStyle);
					}

// R30 Col H
					Cell R30Cell5 = row.createCell(7);
					if (record.getR30_qua_ii_inr() != null) {
						R30Cell5.setCellValue(record.getR30_qua_ii_inr().doubleValue());
						R30Cell5.setCellStyle(numberStyle);
					} else {
						R30Cell5.setCellValue("");
						R30Cell5.setCellStyle(textStyle);
					}

// R30 Col I
					Cell R30Cell6 = row.createCell(8);
					if (record.getR30_qua_iii_lc() != null) {
						R30Cell6.setCellValue(record.getR30_qua_iii_lc().doubleValue());
						R30Cell6.setCellStyle(numberStyle);
					} else {
						R30Cell6.setCellValue("");
						R30Cell6.setCellStyle(textStyle);
					}

// R30 Col J
					Cell R30Cell7 = row.createCell(9);
					if (record.getR30_qua_iii_qar() != null) {
						R30Cell7.setCellValue(record.getR30_qua_iii_qar().doubleValue());
						R30Cell7.setCellStyle(numberStyle);
					} else {
						R30Cell7.setCellValue("");
						R30Cell7.setCellStyle(textStyle);
					}

// R30 Col K
					Cell R30Cell8 = row.createCell(10);
					if (record.getR30_qua_iii_inr() != null) {
						R30Cell8.setCellValue(record.getR30_qua_iii_inr().doubleValue());
						R30Cell8.setCellStyle(numberStyle);
					} else {
						R30Cell8.setCellValue("");
						R30Cell8.setCellStyle(textStyle);
					}

// R30 Col L
					Cell R30Cell9 = row.createCell(11);
					if (record.getR30_qua_iv_lc() != null) {
						R30Cell9.setCellValue(record.getR30_qua_iv_lc().doubleValue());
						R30Cell9.setCellStyle(numberStyle);
					} else {
						R30Cell9.setCellValue("");
						R30Cell9.setCellStyle(textStyle);
					}

// R30 Col M
					Cell R30Cell10 = row.createCell(12);
					if (record.getR30_qua_iv_qar() != null) {
						R30Cell10.setCellValue(record.getR30_qua_iv_qar().doubleValue());
						R30Cell10.setCellStyle(numberStyle);
					} else {
						R30Cell10.setCellValue("");
						R30Cell10.setCellStyle(textStyle);
					}

// R30 Col N
					Cell R30Cell11 = row.createCell(13);
					if (record.getR30_qua_iv_inr() != null) {
						R30Cell11.setCellValue(record.getR30_qua_iv_inr().doubleValue());
						R30Cell11.setCellStyle(numberStyle);
					} else {
						R30Cell11.setCellValue("");
						R30Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
// R31 Col C
					Cell R31Cell = row.createCell(2);
					if (record.getR31_qua_i_lc() != null) {
						R31Cell.setCellValue(record.getR31_qua_i_lc().doubleValue());
						R31Cell.setCellStyle(numberStyle);
					} else {
						R31Cell.setCellValue("");
						R31Cell.setCellStyle(textStyle);
					}

// R31 Col D
					Cell R31Cell1 = row.createCell(3);
					if (record.getR31_qua_i_qar() != null) {
						R31Cell1.setCellValue(record.getR31_qua_i_qar().doubleValue());
						R31Cell1.setCellStyle(numberStyle);
					} else {
						R31Cell1.setCellValue("");
						R31Cell1.setCellStyle(textStyle);
					}

// R31 Col E
					Cell R31Cell2 = row.createCell(4);
					if (record.getR31_qua_i_inr() != null) {
						R31Cell2.setCellValue(record.getR31_qua_i_inr().doubleValue());
						R31Cell2.setCellStyle(numberStyle);
					} else {
						R31Cell2.setCellValue("");
						R31Cell2.setCellStyle(textStyle);
					}

// R31 Col F
					Cell R31Cell3 = row.createCell(5);
					if (record.getR31_qua_ii_lc() != null) {
						R31Cell3.setCellValue(record.getR31_qua_ii_lc().doubleValue());
						R31Cell3.setCellStyle(numberStyle);
					} else {
						R31Cell3.setCellValue("");
						R31Cell3.setCellStyle(textStyle);
					}

// R31 Col G
					Cell R31Cell4 = row.createCell(6);
					if (record.getR31_qua_ii_qar() != null) {
						R31Cell4.setCellValue(record.getR31_qua_ii_qar().doubleValue());
						R31Cell4.setCellStyle(numberStyle);
					} else {
						R31Cell4.setCellValue("");
						R31Cell4.setCellStyle(textStyle);
					}

// R31 Col H
					Cell R31Cell5 = row.createCell(7);
					if (record.getR31_qua_ii_inr() != null) {
						R31Cell5.setCellValue(record.getR31_qua_ii_inr().doubleValue());
						R31Cell5.setCellStyle(numberStyle);
					} else {
						R31Cell5.setCellValue("");
						R31Cell5.setCellStyle(textStyle);
					}

// R31 Col I
					Cell R31Cell6 = row.createCell(8);
					if (record.getR31_qua_iii_lc() != null) {
						R31Cell6.setCellValue(record.getR31_qua_iii_lc().doubleValue());
						R31Cell6.setCellStyle(numberStyle);
					} else {
						R31Cell6.setCellValue("");
						R31Cell6.setCellStyle(textStyle);
					}

// R31 Col J
					Cell R31Cell7 = row.createCell(9);
					if (record.getR31_qua_iii_qar() != null) {
						R31Cell7.setCellValue(record.getR31_qua_iii_qar().doubleValue());
						R31Cell7.setCellStyle(numberStyle);
					} else {
						R31Cell7.setCellValue("");
						R31Cell7.setCellStyle(textStyle);
					}

// R31 Col K
					Cell R31Cell8 = row.createCell(10);
					if (record.getR31_qua_iii_inr() != null) {
						R31Cell8.setCellValue(record.getR31_qua_iii_inr().doubleValue());
						R31Cell8.setCellStyle(numberStyle);
					} else {
						R31Cell8.setCellValue("");
						R31Cell8.setCellStyle(textStyle);
					}

// R31 Col L
					Cell R31Cell9 = row.createCell(11);
					if (record.getR31_qua_iv_lc() != null) {
						R31Cell9.setCellValue(record.getR31_qua_iv_lc().doubleValue());
						R31Cell9.setCellStyle(numberStyle);
					} else {
						R31Cell9.setCellValue("");
						R31Cell9.setCellStyle(textStyle);
					}

// R31 Col M
					Cell R31Cell10 = row.createCell(12);
					if (record.getR31_qua_iv_qar() != null) {
						R31Cell10.setCellValue(record.getR31_qua_iv_qar().doubleValue());
						R31Cell10.setCellStyle(numberStyle);
					} else {
						R31Cell10.setCellValue("");
						R31Cell10.setCellStyle(textStyle);
					}

// R31 Col N
					Cell R31Cell11 = row.createCell(13);
					if (record.getR31_qua_iv_inr() != null) {
						R31Cell11.setCellValue(record.getR31_qua_iv_inr().doubleValue());
						R31Cell11.setCellStyle(numberStyle);
					} else {
						R31Cell11.setCellValue("");
						R31Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
// R32 Col C
					Cell R32Cell = row.createCell(2);
					if (record.getR32_qua_i_lc() != null) {
						R32Cell.setCellValue(record.getR32_qua_i_lc().doubleValue());
						R32Cell.setCellStyle(numberStyle);
					} else {
						R32Cell.setCellValue("");
						R32Cell.setCellStyle(textStyle);
					}

// R32 Col D
					Cell R32Cell1 = row.createCell(3);
					if (record.getR32_qua_i_qar() != null) {
						R32Cell1.setCellValue(record.getR32_qua_i_qar().doubleValue());
						R32Cell1.setCellStyle(numberStyle);
					} else {
						R32Cell1.setCellValue("");
						R32Cell1.setCellStyle(textStyle);
					}

// R32 Col E
					Cell R32Cell2 = row.createCell(4);
					if (record.getR32_qua_i_inr() != null) {
						R32Cell2.setCellValue(record.getR32_qua_i_inr().doubleValue());
						R32Cell2.setCellStyle(numberStyle);
					} else {
						R32Cell2.setCellValue("");
						R32Cell2.setCellStyle(textStyle);
					}

// R32 Col F
					Cell R32Cell3 = row.createCell(5);
					if (record.getR32_qua_ii_lc() != null) {
						R32Cell3.setCellValue(record.getR32_qua_ii_lc().doubleValue());
						R32Cell3.setCellStyle(numberStyle);
					} else {
						R32Cell3.setCellValue("");
						R32Cell3.setCellStyle(textStyle);
					}

// R32 Col G
					Cell R32Cell4 = row.createCell(6);
					if (record.getR32_qua_ii_qar() != null) {
						R32Cell4.setCellValue(record.getR32_qua_ii_qar().doubleValue());
						R32Cell4.setCellStyle(numberStyle);
					} else {
						R32Cell4.setCellValue("");
						R32Cell4.setCellStyle(textStyle);
					}

// R32 Col H
					Cell R32Cell5 = row.createCell(7);
					if (record.getR32_qua_ii_inr() != null) {
						R32Cell5.setCellValue(record.getR32_qua_ii_inr().doubleValue());
						R32Cell5.setCellStyle(numberStyle);
					} else {
						R32Cell5.setCellValue("");
						R32Cell5.setCellStyle(textStyle);
					}

// R32 Col I
					Cell R32Cell6 = row.createCell(8);
					if (record.getR32_qua_iii_lc() != null) {
						R32Cell6.setCellValue(record.getR32_qua_iii_lc().doubleValue());
						R32Cell6.setCellStyle(numberStyle);
					} else {
						R32Cell6.setCellValue("");
						R32Cell6.setCellStyle(textStyle);
					}

// R32 Col J
					Cell R32Cell7 = row.createCell(9);
					if (record.getR32_qua_iii_qar() != null) {
						R32Cell7.setCellValue(record.getR32_qua_iii_qar().doubleValue());
						R32Cell7.setCellStyle(numberStyle);
					} else {
						R32Cell7.setCellValue("");
						R32Cell7.setCellStyle(textStyle);
					}

// R32 Col K
					Cell R32Cell8 = row.createCell(10);
					if (record.getR32_qua_iii_inr() != null) {
						R32Cell8.setCellValue(record.getR32_qua_iii_inr().doubleValue());
						R32Cell8.setCellStyle(numberStyle);
					} else {
						R32Cell8.setCellValue("");
						R32Cell8.setCellStyle(textStyle);
					}

// R32 Col L
					Cell R32Cell9 = row.createCell(11);
					if (record.getR32_qua_iv_lc() != null) {
						R32Cell9.setCellValue(record.getR32_qua_iv_lc().doubleValue());
						R32Cell9.setCellStyle(numberStyle);
					} else {
						R32Cell9.setCellValue("");
						R32Cell9.setCellStyle(textStyle);
					}

// R32 Col M
					Cell R32Cell10 = row.createCell(12);
					if (record.getR32_qua_iv_qar() != null) {
						R32Cell10.setCellValue(record.getR32_qua_iv_qar().doubleValue());
						R32Cell10.setCellStyle(numberStyle);
					} else {
						R32Cell10.setCellValue("");
						R32Cell10.setCellStyle(textStyle);
					}

// R32 Col N
					Cell R32Cell11 = row.createCell(13);
					if (record.getR32_qua_iv_inr() != null) {
						R32Cell11.setCellValue(record.getR32_qua_iv_inr().doubleValue());
						R32Cell11.setCellStyle(numberStyle);
					} else {
						R32Cell11.setCellValue("");
						R32Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
// R33 Col C
					Cell R33Cell = row.createCell(2);
					if (record.getR33_qua_i_lc() != null) {
						R33Cell.setCellValue(record.getR33_qua_i_lc().doubleValue());
						R33Cell.setCellStyle(numberStyle);
					} else {
						R33Cell.setCellValue("");
						R33Cell.setCellStyle(textStyle);
					}

// R33 Col D
					Cell R33Cell1 = row.createCell(3);
					if (record.getR33_qua_i_qar() != null) {
						R33Cell1.setCellValue(record.getR33_qua_i_qar().doubleValue());
						R33Cell1.setCellStyle(numberStyle);
					} else {
						R33Cell1.setCellValue("");
						R33Cell1.setCellStyle(textStyle);
					}

// R33 Col E
					Cell R33Cell2 = row.createCell(4);
					if (record.getR33_qua_i_inr() != null) {
						R33Cell2.setCellValue(record.getR33_qua_i_inr().doubleValue());
						R33Cell2.setCellStyle(numberStyle);
					} else {
						R33Cell2.setCellValue("");
						R33Cell2.setCellStyle(textStyle);
					}

// R33 Col F
					Cell R33Cell3 = row.createCell(5);
					if (record.getR33_qua_ii_lc() != null) {
						R33Cell3.setCellValue(record.getR33_qua_ii_lc().doubleValue());
						R33Cell3.setCellStyle(numberStyle);
					} else {
						R33Cell3.setCellValue("");
						R33Cell3.setCellStyle(textStyle);
					}

// R33 Col G
					Cell R33Cell4 = row.createCell(6);
					if (record.getR33_qua_ii_qar() != null) {
						R33Cell4.setCellValue(record.getR33_qua_ii_qar().doubleValue());
						R33Cell4.setCellStyle(numberStyle);
					} else {
						R33Cell4.setCellValue("");
						R33Cell4.setCellStyle(textStyle);
					}

// R33 Col H
					Cell R33Cell5 = row.createCell(7);
					if (record.getR33_qua_ii_inr() != null) {
						R33Cell5.setCellValue(record.getR33_qua_ii_inr().doubleValue());
						R33Cell5.setCellStyle(numberStyle);
					} else {
						R33Cell5.setCellValue("");
						R33Cell5.setCellStyle(textStyle);
					}

// R33 Col I
					Cell R33Cell6 = row.createCell(8);
					if (record.getR33_qua_iii_lc() != null) {
						R33Cell6.setCellValue(record.getR33_qua_iii_lc().doubleValue());
						R33Cell6.setCellStyle(numberStyle);
					} else {
						R33Cell6.setCellValue("");
						R33Cell6.setCellStyle(textStyle);
					}

// R33 Col J
					Cell R33Cell7 = row.createCell(9);
					if (record.getR33_qua_iii_qar() != null) {
						R33Cell7.setCellValue(record.getR33_qua_iii_qar().doubleValue());
						R33Cell7.setCellStyle(numberStyle);
					} else {
						R33Cell7.setCellValue("");
						R33Cell7.setCellStyle(textStyle);
					}

// R33 Col K
					Cell R33Cell8 = row.createCell(10);
					if (record.getR33_qua_iii_inr() != null) {
						R33Cell8.setCellValue(record.getR33_qua_iii_inr().doubleValue());
						R33Cell8.setCellStyle(numberStyle);
					} else {
						R33Cell8.setCellValue("");
						R33Cell8.setCellStyle(textStyle);
					}

// R33 Col L
					Cell R33Cell9 = row.createCell(11);
					if (record.getR33_qua_iv_lc() != null) {
						R33Cell9.setCellValue(record.getR33_qua_iv_lc().doubleValue());
						R33Cell9.setCellStyle(numberStyle);
					} else {
						R33Cell9.setCellValue("");
						R33Cell9.setCellStyle(textStyle);
					}

// R33 Col M
					Cell R33Cell10 = row.createCell(12);
					if (record.getR33_qua_iv_qar() != null) {
						R33Cell10.setCellValue(record.getR33_qua_iv_qar().doubleValue());
						R33Cell10.setCellStyle(numberStyle);
					} else {
						R33Cell10.setCellValue("");
						R33Cell10.setCellStyle(textStyle);
					}

// R33 Col N
					Cell R33Cell11 = row.createCell(13);
					if (record.getR33_qua_iv_inr() != null) {
						R33Cell11.setCellValue(record.getR33_qua_iv_inr().doubleValue());
						R33Cell11.setCellStyle(numberStyle);
					} else {
						R33Cell11.setCellValue("");
						R33Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
// R34 Col C
					Cell R34Cell = row.createCell(2);
					if (record.getR34_qua_i_lc() != null) {
						R34Cell.setCellValue(record.getR34_qua_i_lc().doubleValue());
						R34Cell.setCellStyle(numberStyle);
					} else {
						R34Cell.setCellValue("");
						R34Cell.setCellStyle(textStyle);
					}

// R34 Col D
					Cell R34Cell1 = row.createCell(3);
					if (record.getR34_qua_i_qar() != null) {
						R34Cell1.setCellValue(record.getR34_qua_i_qar().doubleValue());
						R34Cell1.setCellStyle(numberStyle);
					} else {
						R34Cell1.setCellValue("");
						R34Cell1.setCellStyle(textStyle);
					}

// R34 Col E
					Cell R34Cell2 = row.createCell(4);
					if (record.getR34_qua_i_inr() != null) {
						R34Cell2.setCellValue(record.getR34_qua_i_inr().doubleValue());
						R34Cell2.setCellStyle(numberStyle);
					} else {
						R34Cell2.setCellValue("");
						R34Cell2.setCellStyle(textStyle);
					}

// R34 Col F
					Cell R34Cell3 = row.createCell(5);
					if (record.getR34_qua_ii_lc() != null) {
						R34Cell3.setCellValue(record.getR34_qua_ii_lc().doubleValue());
						R34Cell3.setCellStyle(numberStyle);
					} else {
						R34Cell3.setCellValue("");
						R34Cell3.setCellStyle(textStyle);
					}

// R34 Col G
					Cell R34Cell4 = row.createCell(6);
					if (record.getR34_qua_ii_qar() != null) {
						R34Cell4.setCellValue(record.getR34_qua_ii_qar().doubleValue());
						R34Cell4.setCellStyle(numberStyle);
					} else {
						R34Cell4.setCellValue("");
						R34Cell4.setCellStyle(textStyle);
					}

// R34 Col H
					Cell R34Cell5 = row.createCell(7);
					if (record.getR34_qua_ii_inr() != null) {
						R34Cell5.setCellValue(record.getR34_qua_ii_inr().doubleValue());
						R34Cell5.setCellStyle(numberStyle);
					} else {
						R34Cell5.setCellValue("");
						R34Cell5.setCellStyle(textStyle);
					}

// R34 Col I
					Cell R34Cell6 = row.createCell(8);
					if (record.getR34_qua_iii_lc() != null) {
						R34Cell6.setCellValue(record.getR34_qua_iii_lc().doubleValue());
						R34Cell6.setCellStyle(numberStyle);
					} else {
						R34Cell6.setCellValue("");
						R34Cell6.setCellStyle(textStyle);
					}

// R34 Col J
					Cell R34Cell7 = row.createCell(9);
					if (record.getR34_qua_iii_qar() != null) {
						R34Cell7.setCellValue(record.getR34_qua_iii_qar().doubleValue());
						R34Cell7.setCellStyle(numberStyle);
					} else {
						R34Cell7.setCellValue("");
						R34Cell7.setCellStyle(textStyle);
					}

// R34 Col K
					Cell R34Cell8 = row.createCell(10);
					if (record.getR34_qua_iii_inr() != null) {
						R34Cell8.setCellValue(record.getR34_qua_iii_inr().doubleValue());
						R34Cell8.setCellStyle(numberStyle);
					} else {
						R34Cell8.setCellValue("");
						R34Cell8.setCellStyle(textStyle);
					}

// R34 Col L
					Cell R34Cell9 = row.createCell(11);
					if (record.getR34_qua_iv_lc() != null) {
						R34Cell9.setCellValue(record.getR34_qua_iv_lc().doubleValue());
						R34Cell9.setCellStyle(numberStyle);
					} else {
						R34Cell9.setCellValue("");
						R34Cell9.setCellStyle(textStyle);
					}

// R34 Col M
					Cell R34Cell10 = row.createCell(12);
					if (record.getR34_qua_iv_qar() != null) {
						R34Cell10.setCellValue(record.getR34_qua_iv_qar().doubleValue());
						R34Cell10.setCellStyle(numberStyle);
					} else {
						R34Cell10.setCellValue("");
						R34Cell10.setCellStyle(textStyle);
					}

// R34 Col N
					Cell R34Cell11 = row.createCell(13);
					if (record.getR34_qua_iv_inr() != null) {
						R34Cell11.setCellValue(record.getR34_qua_iv_inr().doubleValue());
						R34Cell11.setCellStyle(numberStyle);
					} else {
						R34Cell11.setCellValue("");
						R34Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
// R35 Col C
					Cell R35Cell = row.createCell(2);
					if (record.getR35_qua_i_lc() != null) {
						R35Cell.setCellValue(record.getR35_qua_i_lc().doubleValue());
						R35Cell.setCellStyle(numberStyle);
					} else {
						R35Cell.setCellValue("");
						R35Cell.setCellStyle(textStyle);
					}

// R35 Col D
					Cell R35Cell1 = row.createCell(3);
					if (record.getR35_qua_i_qar() != null) {
						R35Cell1.setCellValue(record.getR35_qua_i_qar().doubleValue());
						R35Cell1.setCellStyle(numberStyle);
					} else {
						R35Cell1.setCellValue("");
						R35Cell1.setCellStyle(textStyle);
					}

// R35 Col E
					Cell R35Cell2 = row.createCell(4);
					if (record.getR35_qua_i_inr() != null) {
						R35Cell2.setCellValue(record.getR35_qua_i_inr().doubleValue());
						R35Cell2.setCellStyle(numberStyle);
					} else {
						R35Cell2.setCellValue("");
						R35Cell2.setCellStyle(textStyle);
					}

// R35 Col F
					Cell R35Cell3 = row.createCell(5);
					if (record.getR35_qua_ii_lc() != null) {
						R35Cell3.setCellValue(record.getR35_qua_ii_lc().doubleValue());
						R35Cell3.setCellStyle(numberStyle);
					} else {
						R35Cell3.setCellValue("");
						R35Cell3.setCellStyle(textStyle);
					}

// R35 Col G
					Cell R35Cell4 = row.createCell(6);
					if (record.getR35_qua_ii_qar() != null) {
						R35Cell4.setCellValue(record.getR35_qua_ii_qar().doubleValue());
						R35Cell4.setCellStyle(numberStyle);
					} else {
						R35Cell4.setCellValue("");
						R35Cell4.setCellStyle(textStyle);
					}

// R35 Col H
					Cell R35Cell5 = row.createCell(7);
					if (record.getR35_qua_ii_inr() != null) {
						R35Cell5.setCellValue(record.getR35_qua_ii_inr().doubleValue());
						R35Cell5.setCellStyle(numberStyle);
					} else {
						R35Cell5.setCellValue("");
						R35Cell5.setCellStyle(textStyle);
					}

// R35 Col I
					Cell R35Cell6 = row.createCell(8);
					if (record.getR35_qua_iii_lc() != null) {
						R35Cell6.setCellValue(record.getR35_qua_iii_lc().doubleValue());
						R35Cell6.setCellStyle(numberStyle);
					} else {
						R35Cell6.setCellValue("");
						R35Cell6.setCellStyle(textStyle);
					}

// R35 Col J
					Cell R35Cell7 = row.createCell(9);
					if (record.getR35_qua_iii_qar() != null) {
						R35Cell7.setCellValue(record.getR35_qua_iii_qar().doubleValue());
						R35Cell7.setCellStyle(numberStyle);
					} else {
						R35Cell7.setCellValue("");
						R35Cell7.setCellStyle(textStyle);
					}

// R35 Col K
					Cell R35Cell8 = row.createCell(10);
					if (record.getR35_qua_iii_inr() != null) {
						R35Cell8.setCellValue(record.getR35_qua_iii_inr().doubleValue());
						R35Cell8.setCellStyle(numberStyle);
					} else {
						R35Cell8.setCellValue("");
						R35Cell8.setCellStyle(textStyle);
					}

// R35 Col L
					Cell R35Cell9 = row.createCell(11);
					if (record.getR35_qua_iv_lc() != null) {
						R35Cell9.setCellValue(record.getR35_qua_iv_lc().doubleValue());
						R35Cell9.setCellStyle(numberStyle);
					} else {
						R35Cell9.setCellValue("");
						R35Cell9.setCellStyle(textStyle);
					}

// R35 Col M
					Cell R35Cell10 = row.createCell(12);
					if (record.getR35_qua_iv_qar() != null) {
						R35Cell10.setCellValue(record.getR35_qua_iv_qar().doubleValue());
						R35Cell10.setCellStyle(numberStyle);
					} else {
						R35Cell10.setCellValue("");
						R35Cell10.setCellStyle(textStyle);
					}

// R35 Col N
					Cell R35Cell11 = row.createCell(13);
					if (record.getR35_qua_iv_inr() != null) {
						R35Cell11.setCellValue(record.getR35_qua_iv_inr().doubleValue());
						R35Cell11.setCellStyle(numberStyle);
					} else {
						R35Cell11.setCellValue("");
						R35Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(35);
// R36 Col C
					Cell R36Cell = row.createCell(2);
					if (record.getR36_qua_i_lc() != null) {
						R36Cell.setCellValue(record.getR36_qua_i_lc().doubleValue());
						R36Cell.setCellStyle(numberStyle);
					} else {
						R36Cell.setCellValue("");
						R36Cell.setCellStyle(textStyle);
					}

// R36 Col D
					Cell R36Cell1 = row.createCell(3);
					if (record.getR36_qua_i_qar() != null) {
						R36Cell1.setCellValue(record.getR36_qua_i_qar().doubleValue());
						R36Cell1.setCellStyle(numberStyle);
					} else {
						R36Cell1.setCellValue("");
						R36Cell1.setCellStyle(textStyle);
					}

// R36 Col E
					Cell R36Cell2 = row.createCell(4);
					if (record.getR36_qua_i_inr() != null) {
						R36Cell2.setCellValue(record.getR36_qua_i_inr().doubleValue());
						R36Cell2.setCellStyle(numberStyle);
					} else {
						R36Cell2.setCellValue("");
						R36Cell2.setCellStyle(textStyle);
					}

// R36 Col F
					Cell R36Cell3 = row.createCell(5);
					if (record.getR36_qua_ii_lc() != null) {
						R36Cell3.setCellValue(record.getR36_qua_ii_lc().doubleValue());
						R36Cell3.setCellStyle(numberStyle);
					} else {
						R36Cell3.setCellValue("");
						R36Cell3.setCellStyle(textStyle);
					}

// R36 Col G
					Cell R36Cell4 = row.createCell(6);
					if (record.getR36_qua_ii_qar() != null) {
						R36Cell4.setCellValue(record.getR36_qua_ii_qar().doubleValue());
						R36Cell4.setCellStyle(numberStyle);
					} else {
						R36Cell4.setCellValue("");
						R36Cell4.setCellStyle(textStyle);
					}

// R36 Col H
					Cell R36Cell5 = row.createCell(7);
					if (record.getR36_qua_ii_inr() != null) {
						R36Cell5.setCellValue(record.getR36_qua_ii_inr().doubleValue());
						R36Cell5.setCellStyle(numberStyle);
					} else {
						R36Cell5.setCellValue("");
						R36Cell5.setCellStyle(textStyle);
					}

// R36 Col I
					Cell R36Cell6 = row.createCell(8);
					if (record.getR36_qua_iii_lc() != null) {
						R36Cell6.setCellValue(record.getR36_qua_iii_lc().doubleValue());
						R36Cell6.setCellStyle(numberStyle);
					} else {
						R36Cell6.setCellValue("");
						R36Cell6.setCellStyle(textStyle);
					}

// R36 Col J
					Cell R36Cell7 = row.createCell(9);
					if (record.getR36_qua_iii_qar() != null) {
						R36Cell7.setCellValue(record.getR36_qua_iii_qar().doubleValue());
						R36Cell7.setCellStyle(numberStyle);
					} else {
						R36Cell7.setCellValue("");
						R36Cell7.setCellStyle(textStyle);
					}

// R36 Col K
					Cell R36Cell8 = row.createCell(10);
					if (record.getR36_qua_iii_inr() != null) {
						R36Cell8.setCellValue(record.getR36_qua_iii_inr().doubleValue());
						R36Cell8.setCellStyle(numberStyle);
					} else {
						R36Cell8.setCellValue("");
						R36Cell8.setCellStyle(textStyle);
					}

// R36 Col L
					Cell R36Cell9 = row.createCell(11);
					if (record.getR36_qua_iv_lc() != null) {
						R36Cell9.setCellValue(record.getR36_qua_iv_lc().doubleValue());
						R36Cell9.setCellStyle(numberStyle);
					} else {
						R36Cell9.setCellValue("");
						R36Cell9.setCellStyle(textStyle);
					}

// R36 Col M
					Cell R36Cell10 = row.createCell(12);
					if (record.getR36_qua_iv_qar() != null) {
						R36Cell10.setCellValue(record.getR36_qua_iv_qar().doubleValue());
						R36Cell10.setCellStyle(numberStyle);
					} else {
						R36Cell10.setCellValue("");
						R36Cell10.setCellStyle(textStyle);
					}

// R36 Col N
					Cell R36Cell11 = row.createCell(13);
					if (record.getR36_qua_iv_inr() != null) {
						R36Cell11.setCellValue(record.getR36_qua_iv_inr().doubleValue());
						R36Cell11.setCellStyle(numberStyle);
					} else {
						R36Cell11.setCellValue("");
						R36Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
// R37 Col C
					Cell R37Cell = row.createCell(2);
					if (record.getR37_qua_i_lc() != null) {
						R37Cell.setCellValue(record.getR37_qua_i_lc().doubleValue());
						R37Cell.setCellStyle(numberStyle);
					} else {
						R37Cell.setCellValue("");
						R37Cell.setCellStyle(textStyle);
					}

// R37 Col D
					Cell R37Cell1 = row.createCell(3);
					if (record.getR37_qua_i_qar() != null) {
						R37Cell1.setCellValue(record.getR37_qua_i_qar().doubleValue());
						R37Cell1.setCellStyle(numberStyle);
					} else {
						R37Cell1.setCellValue("");
						R37Cell1.setCellStyle(textStyle);
					}

// R37 Col E
					Cell R37Cell2 = row.createCell(4);
					if (record.getR37_qua_i_inr() != null) {
						R37Cell2.setCellValue(record.getR37_qua_i_inr().doubleValue());
						R37Cell2.setCellStyle(numberStyle);
					} else {
						R37Cell2.setCellValue("");
						R37Cell2.setCellStyle(textStyle);
					}

// R37 Col F
					Cell R37Cell3 = row.createCell(5);
					if (record.getR37_qua_ii_lc() != null) {
						R37Cell3.setCellValue(record.getR37_qua_ii_lc().doubleValue());
						R37Cell3.setCellStyle(numberStyle);
					} else {
						R37Cell3.setCellValue("");
						R37Cell3.setCellStyle(textStyle);
					}

// R37 Col G
					Cell R37Cell4 = row.createCell(6);
					if (record.getR37_qua_ii_qar() != null) {
						R37Cell4.setCellValue(record.getR37_qua_ii_qar().doubleValue());
						R37Cell4.setCellStyle(numberStyle);
					} else {
						R37Cell4.setCellValue("");
						R37Cell4.setCellStyle(textStyle);
					}

// R37 Col H
					Cell R37Cell5 = row.createCell(7);
					if (record.getR37_qua_ii_inr() != null) {
						R37Cell5.setCellValue(record.getR37_qua_ii_inr().doubleValue());
						R37Cell5.setCellStyle(numberStyle);
					} else {
						R37Cell5.setCellValue("");
						R37Cell5.setCellStyle(textStyle);
					}

// R37 Col I
					Cell R37Cell6 = row.createCell(8);
					if (record.getR37_qua_iii_lc() != null) {
						R37Cell6.setCellValue(record.getR37_qua_iii_lc().doubleValue());
						R37Cell6.setCellStyle(numberStyle);
					} else {
						R37Cell6.setCellValue("");
						R37Cell6.setCellStyle(textStyle);
					}

// R37 Col J
					Cell R37Cell7 = row.createCell(9);
					if (record.getR37_qua_iii_qar() != null) {
						R37Cell7.setCellValue(record.getR37_qua_iii_qar().doubleValue());
						R37Cell7.setCellStyle(numberStyle);
					} else {
						R37Cell7.setCellValue("");
						R37Cell7.setCellStyle(textStyle);
					}

// R37 Col K
					Cell R37Cell8 = row.createCell(10);
					if (record.getR37_qua_iii_inr() != null) {
						R37Cell8.setCellValue(record.getR37_qua_iii_inr().doubleValue());
						R37Cell8.setCellStyle(numberStyle);
					} else {
						R37Cell8.setCellValue("");
						R37Cell8.setCellStyle(textStyle);
					}

// R37 Col L
					Cell R37Cell9 = row.createCell(11);
					if (record.getR37_qua_iv_lc() != null) {
						R37Cell9.setCellValue(record.getR37_qua_iv_lc().doubleValue());
						R37Cell9.setCellStyle(numberStyle);
					} else {
						R37Cell9.setCellValue("");
						R37Cell9.setCellStyle(textStyle);
					}

// R37 Col M
					Cell R37Cell10 = row.createCell(12);
					if (record.getR37_qua_iv_qar() != null) {
						R37Cell10.setCellValue(record.getR37_qua_iv_qar().doubleValue());
						R37Cell10.setCellStyle(numberStyle);
					} else {
						R37Cell10.setCellValue("");
						R37Cell10.setCellStyle(textStyle);
					}

// R37 Col N
					Cell R37Cell11 = row.createCell(13);
					if (record.getR37_qua_iv_inr() != null) {
						R37Cell11.setCellValue(record.getR37_qua_iv_inr().doubleValue());
						R37Cell11.setCellStyle(numberStyle);
					} else {
						R37Cell11.setCellValue("");
						R37Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(37);
// R38 Col C
					Cell R38Cell = row.createCell(2);
					if (record.getR38_qua_i_lc() != null) {
						R38Cell.setCellValue(record.getR38_qua_i_lc().doubleValue());
						R38Cell.setCellStyle(numberStyle);
					} else {
						R38Cell.setCellValue("");
						R38Cell.setCellStyle(textStyle);
					}

// R38 Col D
					Cell R38Cell1 = row.createCell(3);
					if (record.getR38_qua_i_qar() != null) {
						R38Cell1.setCellValue(record.getR38_qua_i_qar().doubleValue());
						R38Cell1.setCellStyle(numberStyle);
					} else {
						R38Cell1.setCellValue("");
						R38Cell1.setCellStyle(textStyle);
					}

// R38 Col E
					Cell R38Cell2 = row.createCell(4);
					if (record.getR38_qua_i_inr() != null) {
						R38Cell2.setCellValue(record.getR38_qua_i_inr().doubleValue());
						R38Cell2.setCellStyle(numberStyle);
					} else {
						R38Cell2.setCellValue("");
						R38Cell2.setCellStyle(textStyle);
					}

// R38 Col F
					Cell R38Cell3 = row.createCell(5);
					if (record.getR38_qua_ii_lc() != null) {
						R38Cell3.setCellValue(record.getR38_qua_ii_lc().doubleValue());
						R38Cell3.setCellStyle(numberStyle);
					} else {
						R38Cell3.setCellValue("");
						R38Cell3.setCellStyle(textStyle);
					}

// R38 Col G
					Cell R38Cell4 = row.createCell(6);
					if (record.getR38_qua_ii_qar() != null) {
						R38Cell4.setCellValue(record.getR38_qua_ii_qar().doubleValue());
						R38Cell4.setCellStyle(numberStyle);
					} else {
						R38Cell4.setCellValue("");
						R38Cell4.setCellStyle(textStyle);
					}

// R38 Col H
					Cell R38Cell5 = row.createCell(7);
					if (record.getR38_qua_ii_inr() != null) {
						R38Cell5.setCellValue(record.getR38_qua_ii_inr().doubleValue());
						R38Cell5.setCellStyle(numberStyle);
					} else {
						R38Cell5.setCellValue("");
						R38Cell5.setCellStyle(textStyle);
					}

// R38 Col I
					Cell R38Cell6 = row.createCell(8);
					if (record.getR38_qua_iii_lc() != null) {
						R38Cell6.setCellValue(record.getR38_qua_iii_lc().doubleValue());
						R38Cell6.setCellStyle(numberStyle);
					} else {
						R38Cell6.setCellValue("");
						R38Cell6.setCellStyle(textStyle);
					}

// R38 Col J
					Cell R38Cell7 = row.createCell(9);
					if (record.getR38_qua_iii_qar() != null) {
						R38Cell7.setCellValue(record.getR38_qua_iii_qar().doubleValue());
						R38Cell7.setCellStyle(numberStyle);
					} else {
						R38Cell7.setCellValue("");
						R38Cell7.setCellStyle(textStyle);
					}

// R38 Col K
					Cell R38Cell8 = row.createCell(10);
					if (record.getR38_qua_iii_inr() != null) {
						R38Cell8.setCellValue(record.getR38_qua_iii_inr().doubleValue());
						R38Cell8.setCellStyle(numberStyle);
					} else {
						R38Cell8.setCellValue("");
						R38Cell8.setCellStyle(textStyle);
					}

// R38 Col L
					Cell R38Cell9 = row.createCell(11);
					if (record.getR38_qua_iv_lc() != null) {
						R38Cell9.setCellValue(record.getR38_qua_iv_lc().doubleValue());
						R38Cell9.setCellStyle(numberStyle);
					} else {
						R38Cell9.setCellValue("");
						R38Cell9.setCellStyle(textStyle);
					}

// R38 Col M
					Cell R38Cell10 = row.createCell(12);
					if (record.getR38_qua_iv_qar() != null) {
						R38Cell10.setCellValue(record.getR38_qua_iv_qar().doubleValue());
						R38Cell10.setCellStyle(numberStyle);
					} else {
						R38Cell10.setCellValue("");
						R38Cell10.setCellStyle(textStyle);
					}

// R38 Col N
					Cell R38Cell11 = row.createCell(13);
					if (record.getR38_qua_iv_inr() != null) {
						R38Cell11.setCellValue(record.getR38_qua_iv_inr().doubleValue());
						R38Cell11.setCellStyle(numberStyle);
					} else {
						R38Cell11.setCellValue("");
						R38Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(38);
// R39 Col C
					Cell R39Cell = row.createCell(2);
					if (record.getR39_qua_i_lc() != null) {
						R39Cell.setCellValue(record.getR39_qua_i_lc().doubleValue());
						R39Cell.setCellStyle(numberStyle);
					} else {
						R39Cell.setCellValue("");
						R39Cell.setCellStyle(textStyle);
					}

// R39 Col D
					Cell R39Cell1 = row.createCell(3);
					if (record.getR39_qua_i_qar() != null) {
						R39Cell1.setCellValue(record.getR39_qua_i_qar().doubleValue());
						R39Cell1.setCellStyle(numberStyle);
					} else {
						R39Cell1.setCellValue("");
						R39Cell1.setCellStyle(textStyle);
					}

// R39 Col E
					Cell R39Cell2 = row.createCell(4);
					if (record.getR39_qua_i_inr() != null) {
						R39Cell2.setCellValue(record.getR39_qua_i_inr().doubleValue());
						R39Cell2.setCellStyle(numberStyle);
					} else {
						R39Cell2.setCellValue("");
						R39Cell2.setCellStyle(textStyle);
					}

// R39 Col F
					Cell R39Cell3 = row.createCell(5);
					if (record.getR39_qua_ii_lc() != null) {
						R39Cell3.setCellValue(record.getR39_qua_ii_lc().doubleValue());
						R39Cell3.setCellStyle(numberStyle);
					} else {
						R39Cell3.setCellValue("");
						R39Cell3.setCellStyle(textStyle);
					}

// R39 Col G
					Cell R39Cell4 = row.createCell(6);
					if (record.getR39_qua_ii_qar() != null) {
						R39Cell4.setCellValue(record.getR39_qua_ii_qar().doubleValue());
						R39Cell4.setCellStyle(numberStyle);
					} else {
						R39Cell4.setCellValue("");
						R39Cell4.setCellStyle(textStyle);
					}

// R39 Col H
					Cell R39Cell5 = row.createCell(7);
					if (record.getR39_qua_ii_inr() != null) {
						R39Cell5.setCellValue(record.getR39_qua_ii_inr().doubleValue());
						R39Cell5.setCellStyle(numberStyle);
					} else {
						R39Cell5.setCellValue("");
						R39Cell5.setCellStyle(textStyle);
					}

// R39 Col I
					Cell R39Cell6 = row.createCell(8);
					if (record.getR39_qua_iii_lc() != null) {
						R39Cell6.setCellValue(record.getR39_qua_iii_lc().doubleValue());
						R39Cell6.setCellStyle(numberStyle);
					} else {
						R39Cell6.setCellValue("");
						R39Cell6.setCellStyle(textStyle);
					}

// R39 Col J
					Cell R39Cell7 = row.createCell(9);
					if (record.getR39_qua_iii_qar() != null) {
						R39Cell7.setCellValue(record.getR39_qua_iii_qar().doubleValue());
						R39Cell7.setCellStyle(numberStyle);
					} else {
						R39Cell7.setCellValue("");
						R39Cell7.setCellStyle(textStyle);
					}

// R39 Col K
					Cell R39Cell8 = row.createCell(10);
					if (record.getR39_qua_iii_inr() != null) {
						R39Cell8.setCellValue(record.getR39_qua_iii_inr().doubleValue());
						R39Cell8.setCellStyle(numberStyle);
					} else {
						R39Cell8.setCellValue("");
						R39Cell8.setCellStyle(textStyle);
					}

// R39 Col L
					Cell R39Cell9 = row.createCell(11);
					if (record.getR39_qua_iv_lc() != null) {
						R39Cell9.setCellValue(record.getR39_qua_iv_lc().doubleValue());
						R39Cell9.setCellStyle(numberStyle);
					} else {
						R39Cell9.setCellValue("");
						R39Cell9.setCellStyle(textStyle);
					}

// R39 Col M
					Cell R39Cell10 = row.createCell(12);
					if (record.getR39_qua_iv_qar() != null) {
						R39Cell10.setCellValue(record.getR39_qua_iv_qar().doubleValue());
						R39Cell10.setCellStyle(numberStyle);
					} else {
						R39Cell10.setCellValue("");
						R39Cell10.setCellStyle(textStyle);
					}

// R39 Col N
					Cell R39Cell11 = row.createCell(13);
					if (record.getR39_qua_iv_inr() != null) {
						R39Cell11.setCellValue(record.getR39_qua_iv_inr().doubleValue());
						R39Cell11.setCellStyle(numberStyle);
					} else {
						R39Cell11.setCellValue("");
						R39Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(40);
					Cell R41Cell = row.createCell(2);
					if (record.getR41_qua_i_lc() != null) {
						R41Cell.setCellValue(record.getR41_qua_i_lc().doubleValue());
						R41Cell.setCellStyle(numberStyle);
					} else {
						R41Cell.setCellValue("");
						R41Cell.setCellStyle(textStyle);
					}

					Cell R41Cell1 = row.createCell(3);
					if (record.getR41_qua_i_qar() != null) {
						R41Cell1.setCellValue(record.getR41_qua_i_qar().doubleValue());
						R41Cell1.setCellStyle(numberStyle);
					} else {
						R41Cell1.setCellValue("");
						R41Cell1.setCellStyle(textStyle);
					}

					// R41 Col E
					Cell R41Cell2 = row.createCell(4);
					if (record.getR41_qua_i_inr() != null) {
						R41Cell2.setCellValue(record.getR41_qua_i_inr().doubleValue());
						R41Cell2.setCellStyle(numberStyle);
					} else {
						R41Cell2.setCellValue("");
						R41Cell2.setCellStyle(textStyle);
					}

					// R41 Col F
					Cell R41Cell3 = row.createCell(5);
					if (record.getR41_qua_ii_lc() != null) {
						R41Cell3.setCellValue(record.getR41_qua_ii_lc().doubleValue());
						R41Cell3.setCellStyle(numberStyle);
					} else {
						R41Cell3.setCellValue("");
						R41Cell3.setCellStyle(textStyle);
					}
					// R41 Col G
					Cell R41Cell4 = row.createCell(6);
					if (record.getR41_qua_ii_qar() != null) {
						R41Cell4.setCellValue(record.getR41_qua_ii_qar().doubleValue());
						R41Cell4.setCellStyle(numberStyle);
					} else {
						R41Cell4.setCellValue("");
						R41Cell4.setCellStyle(textStyle);
					}
					// R41 Col H
					Cell R41Cell5 = row.createCell(7);
					if (record.getR41_qua_ii_inr() != null) {
						R41Cell5.setCellValue(record.getR41_qua_ii_inr().doubleValue());
						R41Cell5.setCellStyle(numberStyle);
					} else {
						R41Cell5.setCellValue("");
						R41Cell5.setCellStyle(textStyle);
					}
					// R41 Col I
					Cell R41Cell6 = row.createCell(8);
					if (record.getR41_qua_iii_lc() != null) {
						R41Cell6.setCellValue(record.getR41_qua_iii_lc().doubleValue());
						R41Cell6.setCellStyle(numberStyle);
					} else {
						R41Cell6.setCellValue("");
						R41Cell6.setCellStyle(textStyle);
					}
					// R41 Col J
					Cell R41Cell7 = row.createCell(9);
					if (record.getR41_qua_iii_qar() != null) {
						R41Cell7.setCellValue(record.getR41_qua_iii_qar().doubleValue());
						R41Cell7.setCellStyle(numberStyle);
					} else {
						R41Cell7.setCellValue("");
						R41Cell7.setCellStyle(textStyle);
					}
					// R41 Col K
					Cell R41Cell8 = row.createCell(10);
					if (record.getR41_qua_iii_inr() != null) {
						R41Cell8.setCellValue(record.getR41_qua_iii_inr().doubleValue());
						R41Cell8.setCellStyle(numberStyle);
					} else {
						R41Cell8.setCellValue("");
						R41Cell8.setCellStyle(textStyle);
					}
					// R41 Col L
					Cell R41Cell9 = row.createCell(11);
					if (record.getR41_qua_iv_lc() != null) {
						R41Cell9.setCellValue(record.getR41_qua_iv_lc().doubleValue());
						R41Cell9.setCellStyle(numberStyle);
					} else {
						R41Cell9.setCellValue("");
						R41Cell9.setCellStyle(textStyle);
					}
					// R41 Col M
					Cell R41Cell10 = row.createCell(12);
					if (record.getR41_qua_iv_qar() != null) {
						R41Cell10.setCellValue(record.getR41_qua_iv_qar().doubleValue());
						R41Cell10.setCellStyle(numberStyle);
					} else {
						R41Cell10.setCellValue("");
						R41Cell10.setCellStyle(textStyle);
					}

					// R41 Col N
					Cell R41Cell11 = row.createCell(13);
					if (record.getR41_qua_iv_inr() != null) {
						R41Cell11.setCellValue(record.getR41_qua_iv_inr().doubleValue());
						R41Cell11.setCellStyle(numberStyle);
					} else {
						R41Cell11.setCellValue("");
						R41Cell11.setCellStyle(textStyle);
					}
					// R41 Col O
					Cell R41Cell12 = row.createCell(14);
					if (record.getR41_cumm_inr() != null) {
						R41Cell12.setCellValue(record.getR41_cumm_inr().doubleValue());
						R41Cell12.setCellStyle(numberStyle);
					} else {
						R41Cell12.setCellValue("");
						R41Cell12.setCellStyle(textStyle);
					}

					row = sheet.getRow(41);
					Cell R42Cell = row.createCell(2);
					if (record.getR42_qua_i_lc() != null) {
						R42Cell.setCellValue(record.getR42_qua_i_lc().doubleValue());
						R42Cell.setCellStyle(numberStyle);
					} else {
						R42Cell.setCellValue("");
						R42Cell.setCellStyle(textStyle);
					}

					Cell R42Cell1 = row.createCell(3);
					if (record.getR42_qua_i_qar() != null) {
						R42Cell1.setCellValue(record.getR42_qua_i_qar().doubleValue());
						R42Cell1.setCellStyle(numberStyle);
					} else {
						R42Cell1.setCellValue("");
						R42Cell1.setCellStyle(textStyle);
					}

					// R42 Col E
					Cell R42Cell2 = row.createCell(4);
					if (record.getR42_qua_i_inr() != null) {
						R42Cell2.setCellValue(record.getR42_qua_i_inr().doubleValue());
						R42Cell2.setCellStyle(numberStyle);
					} else {
						R42Cell2.setCellValue("");
						R42Cell2.setCellStyle(textStyle);
					}

					// R42 Col F
					Cell R42Cell3 = row.createCell(5);
					if (record.getR42_qua_ii_lc() != null) {
						R42Cell3.setCellValue(record.getR42_qua_ii_lc().doubleValue());
						R42Cell3.setCellStyle(numberStyle);
					} else {
						R42Cell3.setCellValue("");
						R42Cell3.setCellStyle(textStyle);
					}
					// R42 Col G
					Cell R42Cell4 = row.createCell(6);
					if (record.getR42_qua_ii_qar() != null) {
						R42Cell4.setCellValue(record.getR42_qua_ii_qar().doubleValue());
						R42Cell4.setCellStyle(numberStyle);
					} else {
						R42Cell4.setCellValue("");
						R42Cell4.setCellStyle(textStyle);
					}
					// R42 Col H
					Cell R42Cell5 = row.createCell(7);
					if (record.getR42_qua_ii_inr() != null) {
						R42Cell5.setCellValue(record.getR42_qua_ii_inr().doubleValue());
						R42Cell5.setCellStyle(numberStyle);
					} else {
						R42Cell5.setCellValue("");
						R42Cell5.setCellStyle(textStyle);
					}
					// R42 Col I
					Cell R42Cell6 = row.createCell(8);
					if (record.getR42_qua_iii_lc() != null) {
						R42Cell6.setCellValue(record.getR42_qua_iii_lc().doubleValue());
						R42Cell6.setCellStyle(numberStyle);
					} else {
						R42Cell6.setCellValue("");
						R42Cell6.setCellStyle(textStyle);
					}
					// R42 Col J
					Cell R42Cell7 = row.createCell(9);
					if (record.getR42_qua_iii_qar() != null) {
						R42Cell7.setCellValue(record.getR42_qua_iii_qar().doubleValue());
						R42Cell7.setCellStyle(numberStyle);
					} else {
						R42Cell7.setCellValue("");
						R42Cell7.setCellStyle(textStyle);
					}
					// R42 Col K
					Cell R42Cell8 = row.createCell(10);
					if (record.getR42_qua_iii_inr() != null) {
						R42Cell8.setCellValue(record.getR42_qua_iii_inr().doubleValue());
						R42Cell8.setCellStyle(numberStyle);
					} else {
						R42Cell8.setCellValue("");
						R42Cell8.setCellStyle(textStyle);
					}
					// R42 Col L
					Cell R42Cell9 = row.createCell(11);
					if (record.getR42_qua_iv_lc() != null) {
						R42Cell9.setCellValue(record.getR42_qua_iv_lc().doubleValue());
						R42Cell9.setCellStyle(numberStyle);
					} else {
						R42Cell9.setCellValue("");
						R42Cell9.setCellStyle(textStyle);
					}
					// R42 Col M
					Cell R42Cell10 = row.createCell(12);
					if (record.getR42_qua_iv_qar() != null) {
						R42Cell10.setCellValue(record.getR42_qua_iv_qar().doubleValue());
						R42Cell10.setCellStyle(numberStyle);
					} else {
						R42Cell10.setCellValue("");
						R42Cell10.setCellStyle(textStyle);
					}

					// R42 Col N
					Cell R42Cell11 = row.createCell(13);
					if (record.getR42_qua_iv_inr() != null) {
						R42Cell11.setCellValue(record.getR42_qua_iv_inr().doubleValue());
						R42Cell11.setCellStyle(numberStyle);
					} else {
						R42Cell11.setCellValue("");
						R42Cell11.setCellStyle(textStyle);
					}
					// R42 Col O
					Cell R42Cell12 = row.createCell(14);
					if (record.getR42_cumm_inr() != null) {
						R42Cell12.setCellValue(record.getR42_cumm_inr().doubleValue());
						R42Cell12.setCellStyle(numberStyle);
					} else {
						R42Cell12.setCellValue("");
						R42Cell12.setCellStyle(textStyle);
					}
					row = sheet.getRow(42);
					Cell R43Cell = row.createCell(2);
					if (record.getR43_qua_i_lc() != null) {
						R43Cell.setCellValue(record.getR43_qua_i_lc().doubleValue());
						R43Cell.setCellStyle(numberStyle);
					} else {
						R43Cell.setCellValue("");
						R43Cell.setCellStyle(textStyle);
					}

					Cell R43Cell1 = row.createCell(3);
					if (record.getR43_qua_i_qar() != null) {
						R43Cell1.setCellValue(record.getR43_qua_i_qar().doubleValue());
						R43Cell1.setCellStyle(numberStyle);
					} else {
						R43Cell1.setCellValue("");
						R43Cell1.setCellStyle(textStyle);
					}

					// R43 Col E
					Cell R43Cell2 = row.createCell(4);
					if (record.getR43_qua_i_inr() != null) {
						R43Cell2.setCellValue(record.getR43_qua_i_inr().doubleValue());
						R43Cell2.setCellStyle(numberStyle);
					} else {
						R43Cell2.setCellValue("");
						R43Cell2.setCellStyle(textStyle);
					}

					// R43 Col F
					Cell R43Cell3 = row.createCell(5);
					if (record.getR43_qua_ii_lc() != null) {
						R43Cell3.setCellValue(record.getR43_qua_ii_lc().doubleValue());
						R43Cell3.setCellStyle(numberStyle);
					} else {
						R43Cell3.setCellValue("");
						R43Cell3.setCellStyle(textStyle);
					}
					// R43 Col G
					Cell R43Cell4 = row.createCell(6);
					if (record.getR43_qua_ii_qar() != null) {
						R43Cell4.setCellValue(record.getR43_qua_ii_qar().doubleValue());
						R43Cell4.setCellStyle(numberStyle);
					} else {
						R43Cell4.setCellValue("");
						R43Cell4.setCellStyle(textStyle);
					}
					// R43 Col H
					Cell R43Cell5 = row.createCell(7);
					if (record.getR43_qua_ii_inr() != null) {
						R43Cell5.setCellValue(record.getR43_qua_ii_inr().doubleValue());
						R43Cell5.setCellStyle(numberStyle);
					} else {
						R43Cell5.setCellValue("");
						R43Cell5.setCellStyle(textStyle);
					}
					// R43 Col I
					Cell R43Cell6 = row.createCell(8);
					if (record.getR43_qua_iii_lc() != null) {
						R43Cell6.setCellValue(record.getR43_qua_iii_lc().doubleValue());
						R43Cell6.setCellStyle(numberStyle);
					} else {
						R43Cell6.setCellValue("");
						R43Cell6.setCellStyle(textStyle);
					}
					// R43 Col J
					Cell R43Cell7 = row.createCell(9);
					if (record.getR43_qua_iii_qar() != null) {
						R43Cell7.setCellValue(record.getR43_qua_iii_qar().doubleValue());
						R43Cell7.setCellStyle(numberStyle);
					} else {
						R43Cell7.setCellValue("");
						R43Cell7.setCellStyle(textStyle);
					}
					// R43 Col K
					Cell R43Cell8 = row.createCell(10);
					if (record.getR43_qua_iii_inr() != null) {
						R43Cell8.setCellValue(record.getR43_qua_iii_inr().doubleValue());
						R43Cell8.setCellStyle(numberStyle);
					} else {
						R43Cell8.setCellValue("");
						R43Cell8.setCellStyle(textStyle);
					}
					// R43 Col L
					Cell R43Cell9 = row.createCell(11);
					if (record.getR43_qua_iv_lc() != null) {
						R43Cell9.setCellValue(record.getR43_qua_iv_lc().doubleValue());
						R43Cell9.setCellStyle(numberStyle);
					} else {
						R43Cell9.setCellValue("");
						R43Cell9.setCellStyle(textStyle);
					}
					// R43 Col M
					Cell R43Cell10 = row.createCell(12);
					if (record.getR43_qua_iv_qar() != null) {
						R43Cell10.setCellValue(record.getR43_qua_iv_qar().doubleValue());
						R43Cell10.setCellStyle(numberStyle);
					} else {
						R43Cell10.setCellValue("");
						R43Cell10.setCellStyle(textStyle);
					}

					// R43 Col N
					Cell R43Cell11 = row.createCell(13);
					if (record.getR43_qua_iv_inr() != null) {
						R43Cell11.setCellValue(record.getR43_qua_iv_inr().doubleValue());
						R43Cell11.setCellStyle(numberStyle);
					} else {
						R43Cell11.setCellValue("");
						R43Cell11.setCellStyle(textStyle);
					}

					// R43 Col O
					Cell R43Cell12 = row.createCell(14);
					if (record.getR43_cumm_inr() != null) {
						R43Cell12.setCellValue(record.getR43_cumm_inr().doubleValue());
						R43Cell12.setCellStyle(numberStyle);
					} else {
						R43Cell12.setCellValue("");
						R43Cell12.setCellStyle(textStyle);
					}

					row = sheet.getRow(43);
					// R44 Col C
					Cell R44Cell = row.createCell(2);
					if (record.getR44_qua_i_lc() != null) {
						R44Cell.setCellValue(record.getR44_qua_i_lc().doubleValue());
						R44Cell.setCellStyle(numberStyle);
					} else {
						R44Cell.setCellValue("");
						R44Cell.setCellStyle(textStyle);
					}

// R44 Col D
					Cell R44Cell1 = row.createCell(3);
					if (record.getR44_qua_i_qar() != null) {
						R44Cell1.setCellValue(record.getR44_qua_i_qar().doubleValue());
						R44Cell1.setCellStyle(numberStyle);
					} else {
						R44Cell1.setCellValue("");
						R44Cell1.setCellStyle(textStyle);
					}

// R44 Col E
					Cell R44Cell2 = row.createCell(4);
					if (record.getR44_qua_i_inr() != null) {
						R44Cell2.setCellValue(record.getR44_qua_i_inr().doubleValue());
						R44Cell2.setCellStyle(numberStyle);
					} else {
						R44Cell2.setCellValue("");
						R44Cell2.setCellStyle(textStyle);
					}

// R44 Col F
					Cell R44Cell3 = row.createCell(5);
					if (record.getR44_qua_ii_lc() != null) {
						R44Cell3.setCellValue(record.getR44_qua_ii_lc().doubleValue());
						R44Cell3.setCellStyle(numberStyle);
					} else {
						R44Cell3.setCellValue("");
						R44Cell3.setCellStyle(textStyle);
					}

// R44 Col G
					Cell R44Cell4 = row.createCell(6);
					if (record.getR44_qua_ii_qar() != null) {
						R44Cell4.setCellValue(record.getR44_qua_ii_qar().doubleValue());
						R44Cell4.setCellStyle(numberStyle);
					} else {
						R44Cell4.setCellValue("");
						R44Cell4.setCellStyle(textStyle);
					}

// R44 Col H
					Cell R44Cell5 = row.createCell(7);
					if (record.getR44_qua_ii_inr() != null) {
						R44Cell5.setCellValue(record.getR44_qua_ii_inr().doubleValue());
						R44Cell5.setCellStyle(numberStyle);
					} else {
						R44Cell5.setCellValue("");
						R44Cell5.setCellStyle(textStyle);
					}

// R44 Col I
					Cell R44Cell6 = row.createCell(8);
					if (record.getR44_qua_iii_lc() != null) {
						R44Cell6.setCellValue(record.getR44_qua_iii_lc().doubleValue());
						R44Cell6.setCellStyle(numberStyle);
					} else {
						R44Cell6.setCellValue("");
						R44Cell6.setCellStyle(textStyle);
					}

// R44 Col J
					Cell R44Cell7 = row.createCell(9);
					if (record.getR44_qua_iii_qar() != null) {
						R44Cell7.setCellValue(record.getR44_qua_iii_qar().doubleValue());
						R44Cell7.setCellStyle(numberStyle);
					} else {
						R44Cell7.setCellValue("");
						R44Cell7.setCellStyle(textStyle);
					}

// R44 Col K
					Cell R44Cell8 = row.createCell(10);
					if (record.getR44_qua_iii_inr() != null) {
						R44Cell8.setCellValue(record.getR44_qua_iii_inr().doubleValue());
						R44Cell8.setCellStyle(numberStyle);
					} else {
						R44Cell8.setCellValue("");
						R44Cell8.setCellStyle(textStyle);
					}

// R44 Col L
					Cell R44Cell9 = row.createCell(11);
					if (record.getR44_qua_iv_lc() != null) {
						R44Cell9.setCellValue(record.getR44_qua_iv_lc().doubleValue());
						R44Cell9.setCellStyle(numberStyle);
					} else {
						R44Cell9.setCellValue("");
						R44Cell9.setCellStyle(textStyle);
					}

// R44 Col M
					Cell R44Cell10 = row.createCell(12);
					if (record.getR44_qua_iv_qar() != null) {
						R44Cell10.setCellValue(record.getR44_qua_iv_qar().doubleValue());
						R44Cell10.setCellStyle(numberStyle);
					} else {
						R44Cell10.setCellValue("");
						R44Cell10.setCellStyle(textStyle);
					}

// R44 Col N
					Cell R44Cell11 = row.createCell(13);
					if (record.getR44_qua_iv_inr() != null) {
						R44Cell11.setCellValue(record.getR44_qua_iv_inr().doubleValue());
						R44Cell11.setCellStyle(numberStyle);
					} else {
						R44Cell11.setCellValue("");
						R44Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(44);
// R45 Col C
					Cell R45Cell = row.createCell(2);
					if (record.getR45_qua_i_lc() != null) {
						R45Cell.setCellValue(record.getR45_qua_i_lc().doubleValue());
						R45Cell.setCellStyle(numberStyle);
					} else {
						R45Cell.setCellValue("");
						R45Cell.setCellStyle(textStyle);
					}

// R45 Col D
					Cell R45Cell1 = row.createCell(3);
					if (record.getR45_qua_i_qar() != null) {
						R45Cell1.setCellValue(record.getR45_qua_i_qar().doubleValue());
						R45Cell1.setCellStyle(numberStyle);
					} else {
						R45Cell1.setCellValue("");
						R45Cell1.setCellStyle(textStyle);
					}

// R45 Col E
					Cell R45Cell2 = row.createCell(4);
					if (record.getR45_qua_i_inr() != null) {
						R45Cell2.setCellValue(record.getR45_qua_i_inr().doubleValue());
						R45Cell2.setCellStyle(numberStyle);
					} else {
						R45Cell2.setCellValue("");
						R45Cell2.setCellStyle(textStyle);
					}

// R45 Col F
					Cell R45Cell3 = row.createCell(5);
					if (record.getR45_qua_ii_lc() != null) {
						R45Cell3.setCellValue(record.getR45_qua_ii_lc().doubleValue());
						R45Cell3.setCellStyle(numberStyle);
					} else {
						R45Cell3.setCellValue("");
						R45Cell3.setCellStyle(textStyle);
					}

// R45 Col G
					Cell R45Cell4 = row.createCell(6);
					if (record.getR45_qua_ii_qar() != null) {
						R45Cell4.setCellValue(record.getR45_qua_ii_qar().doubleValue());
						R45Cell4.setCellStyle(numberStyle);
					} else {
						R45Cell4.setCellValue("");
						R45Cell4.setCellStyle(textStyle);
					}

// R45 Col H
					Cell R45Cell5 = row.createCell(7);
					if (record.getR45_qua_ii_inr() != null) {
						R45Cell5.setCellValue(record.getR45_qua_ii_inr().doubleValue());
						R45Cell5.setCellStyle(numberStyle);
					} else {
						R45Cell5.setCellValue("");
						R45Cell5.setCellStyle(textStyle);
					}

// R45 Col I
					Cell R45Cell6 = row.createCell(8);
					if (record.getR45_qua_iii_lc() != null) {
						R45Cell6.setCellValue(record.getR45_qua_iii_lc().doubleValue());
						R45Cell6.setCellStyle(numberStyle);
					} else {
						R45Cell6.setCellValue("");
						R45Cell6.setCellStyle(textStyle);
					}

// R45 Col J
					Cell R45Cell7 = row.createCell(9);
					if (record.getR45_qua_iii_qar() != null) {
						R45Cell7.setCellValue(record.getR45_qua_iii_qar().doubleValue());
						R45Cell7.setCellStyle(numberStyle);
					} else {
						R45Cell7.setCellValue("");
						R45Cell7.setCellStyle(textStyle);
					}

// R45 Col K
					Cell R45Cell8 = row.createCell(10);
					if (record.getR45_qua_iii_inr() != null) {
						R45Cell8.setCellValue(record.getR45_qua_iii_inr().doubleValue());
						R45Cell8.setCellStyle(numberStyle);
					} else {
						R45Cell8.setCellValue("");
						R45Cell8.setCellStyle(textStyle);
					}

// R45 Col L
					Cell R45Cell9 = row.createCell(11);
					if (record.getR45_qua_iv_lc() != null) {
						R45Cell9.setCellValue(record.getR45_qua_iv_lc().doubleValue());
						R45Cell9.setCellStyle(numberStyle);
					} else {
						R45Cell9.setCellValue("");
						R45Cell9.setCellStyle(textStyle);
					}

// R45 Col M
					Cell R45Cell10 = row.createCell(12);
					if (record.getR45_qua_iv_qar() != null) {
						R45Cell10.setCellValue(record.getR45_qua_iv_qar().doubleValue());
						R45Cell10.setCellStyle(numberStyle);
					} else {
						R45Cell10.setCellValue("");
						R45Cell10.setCellStyle(textStyle);
					}

// R45 Col N
					Cell R45Cell11 = row.createCell(13);
					if (record.getR45_qua_iv_inr() != null) {
						R45Cell11.setCellValue(record.getR45_qua_iv_inr().doubleValue());
						R45Cell11.setCellStyle(numberStyle);
					} else {
						R45Cell11.setCellValue("");
						R45Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
// R46 Col C
					Cell R46Cell = row.createCell(2);
					if (record.getR46_qua_i_lc() != null) {
						R46Cell.setCellValue(record.getR46_qua_i_lc().doubleValue());
						R46Cell.setCellStyle(numberStyle);
					} else {
						R46Cell.setCellValue("");
						R46Cell.setCellStyle(textStyle);
					}

// R46 Col D
					Cell R46Cell1 = row.createCell(3);
					if (record.getR46_qua_i_qar() != null) {
						R46Cell1.setCellValue(record.getR46_qua_i_qar().doubleValue());
						R46Cell1.setCellStyle(numberStyle);
					} else {
						R46Cell1.setCellValue("");
						R46Cell1.setCellStyle(textStyle);
					}

// R46 Col E
					Cell R46Cell2 = row.createCell(4);
					if (record.getR46_qua_i_inr() != null) {
						R46Cell2.setCellValue(record.getR46_qua_i_inr().doubleValue());
						R46Cell2.setCellStyle(numberStyle);
					} else {
						R46Cell2.setCellValue("");
						R46Cell2.setCellStyle(textStyle);
					}

// R46 Col F
					Cell R46Cell3 = row.createCell(5);
					if (record.getR46_qua_ii_lc() != null) {
						R46Cell3.setCellValue(record.getR46_qua_ii_lc().doubleValue());
						R46Cell3.setCellStyle(numberStyle);
					} else {
						R46Cell3.setCellValue("");
						R46Cell3.setCellStyle(textStyle);
					}

// R46 Col G
					Cell R46Cell4 = row.createCell(6);
					if (record.getR46_qua_ii_qar() != null) {
						R46Cell4.setCellValue(record.getR46_qua_ii_qar().doubleValue());
						R46Cell4.setCellStyle(numberStyle);
					} else {
						R46Cell4.setCellValue("");
						R46Cell4.setCellStyle(textStyle);
					}

// R46 Col H
					Cell R46Cell5 = row.createCell(7);
					if (record.getR46_qua_ii_inr() != null) {
						R46Cell5.setCellValue(record.getR46_qua_ii_inr().doubleValue());
						R46Cell5.setCellStyle(numberStyle);
					} else {
						R46Cell5.setCellValue("");
						R46Cell5.setCellStyle(textStyle);
					}

// R46 Col I
					Cell R46Cell6 = row.createCell(8);
					if (record.getR46_qua_iii_lc() != null) {
						R46Cell6.setCellValue(record.getR46_qua_iii_lc().doubleValue());
						R46Cell6.setCellStyle(numberStyle);
					} else {
						R46Cell6.setCellValue("");
						R46Cell6.setCellStyle(textStyle);
					}

// R46 Col J
					Cell R46Cell7 = row.createCell(9);
					if (record.getR46_qua_iii_qar() != null) {
						R46Cell7.setCellValue(record.getR46_qua_iii_qar().doubleValue());
						R46Cell7.setCellStyle(numberStyle);
					} else {
						R46Cell7.setCellValue("");
						R46Cell7.setCellStyle(textStyle);
					}

// R46 Col K
					Cell R46Cell8 = row.createCell(10);
					if (record.getR46_qua_iii_inr() != null) {
						R46Cell8.setCellValue(record.getR46_qua_iii_inr().doubleValue());
						R46Cell8.setCellStyle(numberStyle);
					} else {
						R46Cell8.setCellValue("");
						R46Cell8.setCellStyle(textStyle);
					}

// R46 Col L
					Cell R46Cell9 = row.createCell(11);
					if (record.getR46_qua_iv_lc() != null) {
						R46Cell9.setCellValue(record.getR46_qua_iv_lc().doubleValue());
						R46Cell9.setCellStyle(numberStyle);
					} else {
						R46Cell9.setCellValue("");
						R46Cell9.setCellStyle(textStyle);
					}

// R46 Col M
					Cell R46Cell10 = row.createCell(12);
					if (record.getR46_qua_iv_qar() != null) {
						R46Cell10.setCellValue(record.getR46_qua_iv_qar().doubleValue());
						R46Cell10.setCellStyle(numberStyle);
					} else {
						R46Cell10.setCellValue("");
						R46Cell10.setCellStyle(textStyle);
					}

// R46 Col N
					Cell R46Cell11 = row.createCell(13);
					if (record.getR46_qua_iv_inr() != null) {
						R46Cell11.setCellValue(record.getR46_qua_iv_inr().doubleValue());
						R46Cell11.setCellStyle(numberStyle);
					} else {
						R46Cell11.setCellValue("");
						R46Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					Cell R48Cell = row.createCell(2);
					if (record.getR48_qua_i_lc() != null) {
						R48Cell.setCellValue(record.getR48_qua_i_lc().doubleValue());
						R48Cell.setCellStyle(numberStyle);
					} else {
						R48Cell.setCellValue("");
						R48Cell.setCellStyle(textStyle);
					}

					Cell R48Cell1 = row.createCell(3);
					if (record.getR48_qua_i_qar() != null) {
						R48Cell1.setCellValue(record.getR48_qua_i_qar().doubleValue());
						R48Cell1.setCellStyle(numberStyle);
					} else {
						R48Cell1.setCellValue("");
						R48Cell1.setCellStyle(textStyle);
					}

					// R48 Col E
					Cell R48Cell2 = row.createCell(4);
					if (record.getR48_qua_i_inr() != null) {
						R48Cell2.setCellValue(record.getR48_qua_i_inr().doubleValue());
						R48Cell2.setCellStyle(numberStyle);
					} else {
						R48Cell2.setCellValue("");
						R48Cell2.setCellStyle(textStyle);
					}

					// R48 Col F
					Cell R48Cell3 = row.createCell(5);
					if (record.getR48_qua_ii_lc() != null) {
						R48Cell3.setCellValue(record.getR48_qua_ii_lc().doubleValue());
						R48Cell3.setCellStyle(numberStyle);
					} else {
						R48Cell3.setCellValue("");
						R48Cell3.setCellStyle(textStyle);
					}
					// R48 Col G
					Cell R48Cell4 = row.createCell(6);
					if (record.getR48_qua_ii_qar() != null) {
						R48Cell4.setCellValue(record.getR48_qua_ii_qar().doubleValue());
						R48Cell4.setCellStyle(numberStyle);
					} else {
						R48Cell4.setCellValue("");
						R48Cell4.setCellStyle(textStyle);
					}
					// R48 Col H
					Cell R48Cell5 = row.createCell(7);
					if (record.getR48_qua_ii_inr() != null) {
						R48Cell5.setCellValue(record.getR48_qua_ii_inr().doubleValue());
						R48Cell5.setCellStyle(numberStyle);
					} else {
						R48Cell5.setCellValue("");
						R48Cell5.setCellStyle(textStyle);
					}
					// R48 Col I
					Cell R48Cell6 = row.createCell(8);
					if (record.getR48_qua_iii_lc() != null) {
						R48Cell6.setCellValue(record.getR48_qua_iii_lc().doubleValue());
						R48Cell6.setCellStyle(numberStyle);
					} else {
						R48Cell6.setCellValue("");
						R48Cell6.setCellStyle(textStyle);
					}
					// R48 Col J
					Cell R48Cell7 = row.createCell(9);
					if (record.getR48_qua_iii_qar() != null) {
						R48Cell7.setCellValue(record.getR48_qua_iii_qar().doubleValue());
						R48Cell7.setCellStyle(numberStyle);
					} else {
						R48Cell7.setCellValue("");
						R48Cell7.setCellStyle(textStyle);
					}
					// R48 Col K
					Cell R48Cell8 = row.createCell(10);
					if (record.getR48_qua_iii_inr() != null) {
						R48Cell8.setCellValue(record.getR48_qua_iii_inr().doubleValue());
						R48Cell8.setCellStyle(numberStyle);
					} else {
						R48Cell8.setCellValue("");
						R48Cell8.setCellStyle(textStyle);
					}
					// R48 Col L
					Cell R48Cell9 = row.createCell(11);
					if (record.getR48_qua_iv_lc() != null) {
						R48Cell9.setCellValue(record.getR48_qua_iv_lc().doubleValue());
						R48Cell9.setCellStyle(numberStyle);
					} else {
						R48Cell9.setCellValue("");
						R48Cell9.setCellStyle(textStyle);
					}
					// R48 Col M
					Cell R48Cell10 = row.createCell(12);
					if (record.getR48_qua_iv_qar() != null) {
						R48Cell10.setCellValue(record.getR48_qua_iv_qar().doubleValue());
						R48Cell10.setCellStyle(numberStyle);
					} else {
						R48Cell10.setCellValue("");
						R48Cell10.setCellStyle(textStyle);
					}

					// R48 Col N
					Cell R48Cell11 = row.createCell(13);
					if (record.getR48_qua_iv_inr() != null) {
						R48Cell11.setCellValue(record.getR48_qua_iv_inr().doubleValue());
						R48Cell11.setCellStyle(numberStyle);
					} else {
						R48Cell11.setCellValue("");
						R48Cell11.setCellStyle(textStyle);
					}
					// R48 Col O
					Cell R48Cell12 = row.createCell(14);
					if (record.getR48_cumm_inr() != null) {
						R48Cell12.setCellValue(record.getR48_cumm_inr().doubleValue());
						R48Cell12.setCellStyle(numberStyle);
					} else {
						R48Cell12.setCellValue("");
						R48Cell12.setCellStyle(textStyle);
					}

					row = sheet.getRow(48);
					Cell R49Cell = row.createCell(2);
					if (record.getR49_qua_i_lc() != null) {
						R49Cell.setCellValue(record.getR49_qua_i_lc().doubleValue());
						R49Cell.setCellStyle(numberStyle);
					} else {
						R49Cell.setCellValue("");
						R49Cell.setCellStyle(textStyle);
					}

					Cell R49Cell1 = row.createCell(3);
					if (record.getR49_qua_i_qar() != null) {
						R49Cell1.setCellValue(record.getR49_qua_i_qar().doubleValue());
						R49Cell1.setCellStyle(numberStyle);
					} else {
						R49Cell1.setCellValue("");
						R49Cell1.setCellStyle(textStyle);
					}

					// R49 Col E
					Cell R49Cell2 = row.createCell(4);
					if (record.getR49_qua_i_inr() != null) {
						R49Cell2.setCellValue(record.getR49_qua_i_inr().doubleValue());
						R49Cell2.setCellStyle(numberStyle);
					} else {
						R49Cell2.setCellValue("");
						R49Cell2.setCellStyle(textStyle);
					}

					// R49 Col F
					Cell R49Cell3 = row.createCell(5);
					if (record.getR49_qua_ii_lc() != null) {
						R49Cell3.setCellValue(record.getR49_qua_ii_lc().doubleValue());
						R49Cell3.setCellStyle(numberStyle);
					} else {
						R49Cell3.setCellValue("");
						R49Cell3.setCellStyle(textStyle);
					}
					// R49 Col G
					Cell R49Cell4 = row.createCell(6);
					if (record.getR49_qua_ii_qar() != null) {
						R49Cell4.setCellValue(record.getR49_qua_ii_qar().doubleValue());
						R49Cell4.setCellStyle(numberStyle);
					} else {
						R49Cell4.setCellValue("");
						R49Cell4.setCellStyle(textStyle);
					}
					// R49 Col H
					Cell R49Cell5 = row.createCell(7);
					if (record.getR49_qua_ii_inr() != null) {
						R49Cell5.setCellValue(record.getR49_qua_ii_inr().doubleValue());
						R49Cell5.setCellStyle(numberStyle);
					} else {
						R49Cell5.setCellValue("");
						R49Cell5.setCellStyle(textStyle);
					}
					// R49 Col I
					Cell R49Cell6 = row.createCell(8);
					if (record.getR49_qua_iii_lc() != null) {
						R49Cell6.setCellValue(record.getR49_qua_iii_lc().doubleValue());
						R49Cell6.setCellStyle(numberStyle);
					} else {
						R49Cell6.setCellValue("");
						R49Cell6.setCellStyle(textStyle);
					}
					// R49 Col J
					Cell R49Cell7 = row.createCell(9);
					if (record.getR49_qua_iii_qar() != null) {
						R49Cell7.setCellValue(record.getR49_qua_iii_qar().doubleValue());
						R49Cell7.setCellStyle(numberStyle);
					} else {
						R49Cell7.setCellValue("");
						R49Cell7.setCellStyle(textStyle);
					}
					// R49 Col K
					Cell R49Cell8 = row.createCell(10);
					if (record.getR49_qua_iii_inr() != null) {
						R49Cell8.setCellValue(record.getR49_qua_iii_inr().doubleValue());
						R49Cell8.setCellStyle(numberStyle);
					} else {
						R49Cell8.setCellValue("");
						R49Cell8.setCellStyle(textStyle);
					}
					// R49 Col L
					Cell R49Cell9 = row.createCell(11);
					if (record.getR49_qua_iv_lc() != null) {
						R49Cell9.setCellValue(record.getR49_qua_iv_lc().doubleValue());
						R49Cell9.setCellStyle(numberStyle);
					} else {
						R49Cell9.setCellValue("");
						R49Cell9.setCellStyle(textStyle);
					}
					// R49 Col M
					Cell R49Cell10 = row.createCell(12);
					if (record.getR49_qua_iv_qar() != null) {
						R49Cell10.setCellValue(record.getR49_qua_iv_qar().doubleValue());
						R49Cell10.setCellStyle(numberStyle);
					} else {
						R49Cell10.setCellValue("");
						R49Cell10.setCellStyle(textStyle);
					}

					// R49 Col N
					Cell R49Cell11 = row.createCell(13);
					if (record.getR49_qua_iv_inr() != null) {
						R49Cell11.setCellValue(record.getR49_qua_iv_inr().doubleValue());
						R49Cell11.setCellStyle(numberStyle);
					} else {
						R49Cell11.setCellValue("");
						R49Cell11.setCellStyle(textStyle);
					}
					// R49 Col O
					Cell R49Cell12 = row.createCell(14);
					if (record.getR49_cumm_inr() != null) {
						R49Cell12.setCellValue(record.getR49_cumm_inr().doubleValue());
						R49Cell12.setCellStyle(numberStyle);
					} else {
						R49Cell12.setCellValue("");
						R49Cell12.setCellStyle(textStyle);
					}
					row = sheet.getRow(49);
					Cell R50Cell = row.createCell(2);
					if (record.getR50_qua_i_lc() != null) {
						R50Cell.setCellValue(record.getR50_qua_i_lc().doubleValue());
						R50Cell.setCellStyle(numberStyle);
					} else {
						R50Cell.setCellValue("");
						R50Cell.setCellStyle(textStyle);
					}

					Cell R50Cell1 = row.createCell(3);
					if (record.getR50_qua_i_qar() != null) {
						R50Cell1.setCellValue(record.getR50_qua_i_qar().doubleValue());
						R50Cell1.setCellStyle(numberStyle);
					} else {
						R50Cell1.setCellValue("");
						R50Cell1.setCellStyle(textStyle);
					}

					// R50 Col E
					Cell R50Cell2 = row.createCell(4);
					if (record.getR50_qua_i_inr() != null) {
						R50Cell2.setCellValue(record.getR50_qua_i_inr().doubleValue());
						R50Cell2.setCellStyle(numberStyle);
					} else {
						R50Cell2.setCellValue("");
						R50Cell2.setCellStyle(textStyle);
					}

					// R50 Col F
					Cell R50Cell3 = row.createCell(5);
					if (record.getR50_qua_ii_lc() != null) {
						R50Cell3.setCellValue(record.getR50_qua_ii_lc().doubleValue());
						R50Cell3.setCellStyle(numberStyle);
					} else {
						R50Cell3.setCellValue("");
						R50Cell3.setCellStyle(textStyle);
					}
					// R50 Col G
					Cell R50Cell4 = row.createCell(6);
					if (record.getR50_qua_ii_qar() != null) {
						R50Cell4.setCellValue(record.getR50_qua_ii_qar().doubleValue());
						R50Cell4.setCellStyle(numberStyle);
					} else {
						R50Cell4.setCellValue("");
						R50Cell4.setCellStyle(textStyle);
					}
					// R50 Col H
					Cell R50Cell5 = row.createCell(7);
					if (record.getR50_qua_ii_inr() != null) {
						R50Cell5.setCellValue(record.getR50_qua_ii_inr().doubleValue());
						R50Cell5.setCellStyle(numberStyle);
					} else {
						R50Cell5.setCellValue("");
						R50Cell5.setCellStyle(textStyle);
					}
					// R50 Col I
					Cell R50Cell6 = row.createCell(8);
					if (record.getR50_qua_iii_lc() != null) {
						R50Cell6.setCellValue(record.getR50_qua_iii_lc().doubleValue());
						R50Cell6.setCellStyle(numberStyle);
					} else {
						R50Cell6.setCellValue("");
						R50Cell6.setCellStyle(textStyle);
					}
					// R50 Col J
					Cell R50Cell7 = row.createCell(9);
					if (record.getR50_qua_iii_qar() != null) {
						R50Cell7.setCellValue(record.getR50_qua_iii_qar().doubleValue());
						R50Cell7.setCellStyle(numberStyle);
					} else {
						R50Cell7.setCellValue("");
						R50Cell7.setCellStyle(textStyle);
					}
					// R50 Col K
					Cell R50Cell8 = row.createCell(10);
					if (record.getR50_qua_iii_inr() != null) {
						R50Cell8.setCellValue(record.getR50_qua_iii_inr().doubleValue());
						R50Cell8.setCellStyle(numberStyle);
					} else {
						R50Cell8.setCellValue("");
						R50Cell8.setCellStyle(textStyle);
					}
					// R50 Col L
					Cell R50Cell9 = row.createCell(11);
					if (record.getR50_qua_iv_lc() != null) {
						R50Cell9.setCellValue(record.getR50_qua_iv_lc().doubleValue());
						R50Cell9.setCellStyle(numberStyle);
					} else {
						R50Cell9.setCellValue("");
						R50Cell9.setCellStyle(textStyle);
					}
					// R50 Col M
					Cell R50Cell10 = row.createCell(12);
					if (record.getR50_qua_iv_qar() != null) {
						R50Cell10.setCellValue(record.getR50_qua_iv_qar().doubleValue());
						R50Cell10.setCellStyle(numberStyle);
					} else {
						R50Cell10.setCellValue("");
						R50Cell10.setCellStyle(textStyle);
					}

					// R50 Col N
					Cell R50Cell11 = row.createCell(13);
					if (record.getR50_qua_iv_inr() != null) {
						R50Cell11.setCellValue(record.getR50_qua_iv_inr().doubleValue());
						R50Cell11.setCellStyle(numberStyle);
					} else {
						R50Cell11.setCellValue("");
						R50Cell11.setCellStyle(textStyle);
					}

					// R50 Col O
					Cell R50Cell12 = row.createCell(14);
					if (record.getR50_cumm_inr() != null) {
						R50Cell12.setCellValue(record.getR50_cumm_inr().doubleValue());
						R50Cell12.setCellStyle(numberStyle);
					} else {
						R50Cell12.setCellValue("");
						R50Cell12.setCellStyle(textStyle);
					}

					row = sheet.getRow(50);
					Cell R51Cell = row.createCell(2);
					if (record.getR51_qua_i_lc() != null) {
						R51Cell.setCellValue(record.getR51_qua_i_lc().doubleValue());
						R51Cell.setCellStyle(numberStyle);
					} else {
						R51Cell.setCellValue("");
						R51Cell.setCellStyle(textStyle);
					}

					Cell R51Cell1 = row.createCell(3);
					if (record.getR51_qua_i_qar() != null) {
						R51Cell1.setCellValue(record.getR51_qua_i_qar().doubleValue());
						R51Cell1.setCellStyle(numberStyle);
					} else {
						R51Cell1.setCellValue("");
						R51Cell1.setCellStyle(textStyle);
					}

					// R51 Col E
					Cell R51Cell2 = row.createCell(4);
					if (record.getR51_qua_i_inr() != null) {
						R51Cell2.setCellValue(record.getR51_qua_i_inr().doubleValue());
						R51Cell2.setCellStyle(numberStyle);
					} else {
						R51Cell2.setCellValue("");
						R51Cell2.setCellStyle(textStyle);
					}

					// R51 Col F
					Cell R51Cell3 = row.createCell(5);
					if (record.getR51_qua_ii_lc() != null) {
						R51Cell3.setCellValue(record.getR51_qua_ii_lc().doubleValue());
						R51Cell3.setCellStyle(numberStyle);
					} else {
						R51Cell3.setCellValue("");
						R51Cell3.setCellStyle(textStyle);
					}
					// R51 Col G
					Cell R51Cell4 = row.createCell(6);
					if (record.getR51_qua_ii_qar() != null) {
						R51Cell4.setCellValue(record.getR51_qua_ii_qar().doubleValue());
						R51Cell4.setCellStyle(numberStyle);
					} else {
						R51Cell4.setCellValue("");
						R51Cell4.setCellStyle(textStyle);
					}
					// R51 Col H
					Cell R51Cell5 = row.createCell(7);
					if (record.getR51_qua_ii_inr() != null) {
						R51Cell5.setCellValue(record.getR51_qua_ii_inr().doubleValue());
						R51Cell5.setCellStyle(numberStyle);
					} else {
						R51Cell5.setCellValue("");
						R51Cell5.setCellStyle(textStyle);
					}
					// R51 Col I
					Cell R51Cell6 = row.createCell(8);
					if (record.getR51_qua_iii_lc() != null) {
						R51Cell6.setCellValue(record.getR51_qua_iii_lc().doubleValue());
						R51Cell6.setCellStyle(numberStyle);
					} else {
						R51Cell6.setCellValue("");
						R51Cell6.setCellStyle(textStyle);
					}
					// R51 Col J
					Cell R51Cell7 = row.createCell(9);
					if (record.getR51_qua_iii_qar() != null) {
						R51Cell7.setCellValue(record.getR51_qua_iii_qar().doubleValue());
						R51Cell7.setCellStyle(numberStyle);
					} else {
						R51Cell7.setCellValue("");
						R51Cell7.setCellStyle(textStyle);
					}
					// R51 Col K
					Cell R51Cell8 = row.createCell(10);
					if (record.getR51_qua_iii_inr() != null) {
						R51Cell8.setCellValue(record.getR51_qua_iii_inr().doubleValue());
						R51Cell8.setCellStyle(numberStyle);
					} else {
						R51Cell8.setCellValue("");
						R51Cell8.setCellStyle(textStyle);
					}
					// R51 Col L
					Cell R51Cell9 = row.createCell(11);
					if (record.getR51_qua_iv_lc() != null) {
						R51Cell9.setCellValue(record.getR51_qua_iv_lc().doubleValue());
						R51Cell9.setCellStyle(numberStyle);
					} else {
						R51Cell9.setCellValue("");
						R51Cell9.setCellStyle(textStyle);
					}
					// R51 Col M
					Cell R51Cell10 = row.createCell(12);
					if (record.getR51_qua_iv_qar() != null) {
						R51Cell10.setCellValue(record.getR51_qua_iv_qar().doubleValue());
						R51Cell10.setCellStyle(numberStyle);
					} else {
						R51Cell10.setCellValue("");
						R51Cell10.setCellStyle(textStyle);
					}

					// R51 Col N
					Cell R51Cell11 = row.createCell(13);
					if (record.getR51_qua_iv_inr() != null) {
						R51Cell11.setCellValue(record.getR51_qua_iv_inr().doubleValue());
						R51Cell11.setCellStyle(numberStyle);
					} else {
						R51Cell11.setCellValue("");
						R51Cell11.setCellStyle(textStyle);
					}

					// R51 Col O
					Cell R51Cell12 = row.createCell(14);
					if (record.getR51_cumm_inr() != null) {
						R51Cell12.setCellValue(record.getR51_cumm_inr().doubleValue());
						R51Cell12.setCellStyle(numberStyle);
					} else {
						R51Cell12.setCellValue("");
						R51Cell12.setCellStyle(textStyle);
					}
					row = sheet.getRow(51);
					// R52 Col C
					Cell R52Cell = row.createCell(2);
					if (record.getR52_qua_i_lc() != null) {
						R52Cell.setCellValue(record.getR52_qua_i_lc().doubleValue());
						R52Cell.setCellStyle(numberStyle);
					} else {
						R52Cell.setCellValue("");
						R52Cell.setCellStyle(textStyle);
					}

// R52 Col D
					Cell R52Cell1 = row.createCell(3);
					if (record.getR52_qua_i_qar() != null) {
						R52Cell1.setCellValue(record.getR52_qua_i_qar().doubleValue());
						R52Cell1.setCellStyle(numberStyle);
					} else {
						R52Cell1.setCellValue("");
						R52Cell1.setCellStyle(textStyle);
					}

// R52 Col E
					Cell R52Cell2 = row.createCell(4);
					if (record.getR52_qua_i_inr() != null) {
						R52Cell2.setCellValue(record.getR52_qua_i_inr().doubleValue());
						R52Cell2.setCellStyle(numberStyle);
					} else {
						R52Cell2.setCellValue("");
						R52Cell2.setCellStyle(textStyle);
					}

// R52 Col F
					Cell R52Cell3 = row.createCell(5);
					if (record.getR52_qua_ii_lc() != null) {
						R52Cell3.setCellValue(record.getR52_qua_ii_lc().doubleValue());
						R52Cell3.setCellStyle(numberStyle);
					} else {
						R52Cell3.setCellValue("");
						R52Cell3.setCellStyle(textStyle);
					}

// R52 Col G
					Cell R52Cell4 = row.createCell(6);
					if (record.getR52_qua_ii_qar() != null) {
						R52Cell4.setCellValue(record.getR52_qua_ii_qar().doubleValue());
						R52Cell4.setCellStyle(numberStyle);
					} else {
						R52Cell4.setCellValue("");
						R52Cell4.setCellStyle(textStyle);
					}

// R52 Col H
					Cell R52Cell5 = row.createCell(7);
					if (record.getR52_qua_ii_inr() != null) {
						R52Cell5.setCellValue(record.getR52_qua_ii_inr().doubleValue());
						R52Cell5.setCellStyle(numberStyle);
					} else {
						R52Cell5.setCellValue("");
						R52Cell5.setCellStyle(textStyle);
					}

// R52 Col I
					Cell R52Cell6 = row.createCell(8);
					if (record.getR52_qua_iii_lc() != null) {
						R52Cell6.setCellValue(record.getR52_qua_iii_lc().doubleValue());
						R52Cell6.setCellStyle(numberStyle);
					} else {
						R52Cell6.setCellValue("");
						R52Cell6.setCellStyle(textStyle);
					}

// R52 Col J
					Cell R52Cell7 = row.createCell(9);
					if (record.getR52_qua_iii_qar() != null) {
						R52Cell7.setCellValue(record.getR52_qua_iii_qar().doubleValue());
						R52Cell7.setCellStyle(numberStyle);
					} else {
						R52Cell7.setCellValue("");
						R52Cell7.setCellStyle(textStyle);
					}

// R52 Col K
					Cell R52Cell8 = row.createCell(10);
					if (record.getR52_qua_iii_inr() != null) {
						R52Cell8.setCellValue(record.getR52_qua_iii_inr().doubleValue());
						R52Cell8.setCellStyle(numberStyle);
					} else {
						R52Cell8.setCellValue("");
						R52Cell8.setCellStyle(textStyle);
					}

// R52 Col L
					Cell R52Cell9 = row.createCell(11);
					if (record.getR52_qua_iv_lc() != null) {
						R52Cell9.setCellValue(record.getR52_qua_iv_lc().doubleValue());
						R52Cell9.setCellStyle(numberStyle);
					} else {
						R52Cell9.setCellValue("");
						R52Cell9.setCellStyle(textStyle);
					}

// R52 Col M
					Cell R52Cell10 = row.createCell(12);
					if (record.getR52_qua_iv_qar() != null) {
						R52Cell10.setCellValue(record.getR52_qua_iv_qar().doubleValue());
						R52Cell10.setCellStyle(numberStyle);
					} else {
						R52Cell10.setCellValue("");
						R52Cell10.setCellStyle(textStyle);
					}

// R52 Col N
					Cell R52Cell11 = row.createCell(13);
					if (record.getR52_qua_iv_inr() != null) {
						R52Cell11.setCellValue(record.getR52_qua_iv_inr().doubleValue());
						R52Cell11.setCellStyle(numberStyle);
					} else {
						R52Cell11.setCellValue("");
						R52Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(52);
// R53 Col C
					Cell R53Cell = row.createCell(2);
					if (record.getR53_qua_i_lc() != null) {
						R53Cell.setCellValue(record.getR53_qua_i_lc().doubleValue());
						R53Cell.setCellStyle(numberStyle);
					} else {
						R53Cell.setCellValue("");
						R53Cell.setCellStyle(textStyle);
					}

// R53 Col D
					Cell R53Cell1 = row.createCell(3);
					if (record.getR53_qua_i_qar() != null) {
						R53Cell1.setCellValue(record.getR53_qua_i_qar().doubleValue());
						R53Cell1.setCellStyle(numberStyle);
					} else {
						R53Cell1.setCellValue("");
						R53Cell1.setCellStyle(textStyle);
					}

// R53 Col E
					Cell R53Cell2 = row.createCell(4);
					if (record.getR53_qua_i_inr() != null) {
						R53Cell2.setCellValue(record.getR53_qua_i_inr().doubleValue());
						R53Cell2.setCellStyle(numberStyle);
					} else {
						R53Cell2.setCellValue("");
						R53Cell2.setCellStyle(textStyle);
					}

// R53 Col F
					Cell R53Cell3 = row.createCell(5);
					if (record.getR53_qua_ii_lc() != null) {
						R53Cell3.setCellValue(record.getR53_qua_ii_lc().doubleValue());
						R53Cell3.setCellStyle(numberStyle);
					} else {
						R53Cell3.setCellValue("");
						R53Cell3.setCellStyle(textStyle);
					}

// R53 Col G
					Cell R53Cell4 = row.createCell(6);
					if (record.getR53_qua_ii_qar() != null) {
						R53Cell4.setCellValue(record.getR53_qua_ii_qar().doubleValue());
						R53Cell4.setCellStyle(numberStyle);
					} else {
						R53Cell4.setCellValue("");
						R53Cell4.setCellStyle(textStyle);
					}

// R53 Col H
					Cell R53Cell5 = row.createCell(7);
					if (record.getR53_qua_ii_inr() != null) {
						R53Cell5.setCellValue(record.getR53_qua_ii_inr().doubleValue());
						R53Cell5.setCellStyle(numberStyle);
					} else {
						R53Cell5.setCellValue("");
						R53Cell5.setCellStyle(textStyle);
					}

// R53 Col I
					Cell R53Cell6 = row.createCell(8);
					if (record.getR53_qua_iii_lc() != null) {
						R53Cell6.setCellValue(record.getR53_qua_iii_lc().doubleValue());
						R53Cell6.setCellStyle(numberStyle);
					} else {
						R53Cell6.setCellValue("");
						R53Cell6.setCellStyle(textStyle);
					}

// R53 Col J
					Cell R53Cell7 = row.createCell(9);
					if (record.getR53_qua_iii_qar() != null) {
						R53Cell7.setCellValue(record.getR53_qua_iii_qar().doubleValue());
						R53Cell7.setCellStyle(numberStyle);
					} else {
						R53Cell7.setCellValue("");
						R53Cell7.setCellStyle(textStyle);
					}

// R53 Col K
					Cell R53Cell8 = row.createCell(10);
					if (record.getR53_qua_iii_inr() != null) {
						R53Cell8.setCellValue(record.getR53_qua_iii_inr().doubleValue());
						R53Cell8.setCellStyle(numberStyle);
					} else {
						R53Cell8.setCellValue("");
						R53Cell8.setCellStyle(textStyle);
					}

// R53 Col L
					Cell R53Cell9 = row.createCell(11);
					if (record.getR53_qua_iv_lc() != null) {
						R53Cell9.setCellValue(record.getR53_qua_iv_lc().doubleValue());
						R53Cell9.setCellStyle(numberStyle);
					} else {
						R53Cell9.setCellValue("");
						R53Cell9.setCellStyle(textStyle);
					}

// R53 Col M
					Cell R53Cell10 = row.createCell(12);
					if (record.getR53_qua_iv_qar() != null) {
						R53Cell10.setCellValue(record.getR53_qua_iv_qar().doubleValue());
						R53Cell10.setCellStyle(numberStyle);
					} else {
						R53Cell10.setCellValue("");
						R53Cell10.setCellStyle(textStyle);
					}

// R53 Col N
					Cell R53Cell11 = row.createCell(13);
					if (record.getR53_qua_iv_inr() != null) {
						R53Cell11.setCellValue(record.getR53_qua_iv_inr().doubleValue());
						R53Cell11.setCellStyle(numberStyle);
					} else {
						R53Cell11.setCellValue("");
						R53Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(53);
// R54 Col C
					Cell R54Cell = row.createCell(2);
					if (record.getR54_qua_i_lc() != null) {
						R54Cell.setCellValue(record.getR54_qua_i_lc().doubleValue());
						R54Cell.setCellStyle(numberStyle);
					} else {
						R54Cell.setCellValue("");
						R54Cell.setCellStyle(textStyle);
					}

// R54 Col D
					Cell R54Cell1 = row.createCell(3);
					if (record.getR54_qua_i_qar() != null) {
						R54Cell1.setCellValue(record.getR54_qua_i_qar().doubleValue());
						R54Cell1.setCellStyle(numberStyle);
					} else {
						R54Cell1.setCellValue("");
						R54Cell1.setCellStyle(textStyle);
					}

// R54 Col E
					Cell R54Cell2 = row.createCell(4);
					if (record.getR54_qua_i_inr() != null) {
						R54Cell2.setCellValue(record.getR54_qua_i_inr().doubleValue());
						R54Cell2.setCellStyle(numberStyle);
					} else {
						R54Cell2.setCellValue("");
						R54Cell2.setCellStyle(textStyle);
					}

// R54 Col F
					Cell R54Cell3 = row.createCell(5);
					if (record.getR54_qua_ii_lc() != null) {
						R54Cell3.setCellValue(record.getR54_qua_ii_lc().doubleValue());
						R54Cell3.setCellStyle(numberStyle);
					} else {
						R54Cell3.setCellValue("");
						R54Cell3.setCellStyle(textStyle);
					}

// R54 Col G
					Cell R54Cell4 = row.createCell(6);
					if (record.getR54_qua_ii_qar() != null) {
						R54Cell4.setCellValue(record.getR54_qua_ii_qar().doubleValue());
						R54Cell4.setCellStyle(numberStyle);
					} else {
						R54Cell4.setCellValue("");
						R54Cell4.setCellStyle(textStyle);
					}

// R54 Col H
					Cell R54Cell5 = row.createCell(7);
					if (record.getR54_qua_ii_inr() != null) {
						R54Cell5.setCellValue(record.getR54_qua_ii_inr().doubleValue());
						R54Cell5.setCellStyle(numberStyle);
					} else {
						R54Cell5.setCellValue("");
						R54Cell5.setCellStyle(textStyle);
					}

// R54 Col I
					Cell R54Cell6 = row.createCell(8);
					if (record.getR54_qua_iii_lc() != null) {
						R54Cell6.setCellValue(record.getR54_qua_iii_lc().doubleValue());
						R54Cell6.setCellStyle(numberStyle);
					} else {
						R54Cell6.setCellValue("");
						R54Cell6.setCellStyle(textStyle);
					}

// R54 Col J
					Cell R54Cell7 = row.createCell(9);
					if (record.getR54_qua_iii_qar() != null) {
						R54Cell7.setCellValue(record.getR54_qua_iii_qar().doubleValue());
						R54Cell7.setCellStyle(numberStyle);
					} else {
						R54Cell7.setCellValue("");
						R54Cell7.setCellStyle(textStyle);
					}

// R54 Col K
					Cell R54Cell8 = row.createCell(10);
					if (record.getR54_qua_iii_inr() != null) {
						R54Cell8.setCellValue(record.getR54_qua_iii_inr().doubleValue());
						R54Cell8.setCellStyle(numberStyle);
					} else {
						R54Cell8.setCellValue("");
						R54Cell8.setCellStyle(textStyle);
					}

// R54 Col L
					Cell R54Cell9 = row.createCell(11);
					if (record.getR54_qua_iv_lc() != null) {
						R54Cell9.setCellValue(record.getR54_qua_iv_lc().doubleValue());
						R54Cell9.setCellStyle(numberStyle);
					} else {
						R54Cell9.setCellValue("");
						R54Cell9.setCellStyle(textStyle);
					}

// R54 Col M
					Cell R54Cell10 = row.createCell(12);
					if (record.getR54_qua_iv_qar() != null) {
						R54Cell10.setCellValue(record.getR54_qua_iv_qar().doubleValue());
						R54Cell10.setCellStyle(numberStyle);
					} else {
						R54Cell10.setCellValue("");
						R54Cell10.setCellStyle(textStyle);
					}

// R54 Col N
					Cell R54Cell11 = row.createCell(13);
					if (record.getR54_qua_iv_inr() != null) {
						R54Cell11.setCellValue(record.getR54_qua_iv_inr().doubleValue());
						R54Cell11.setCellStyle(numberStyle);
					} else {
						R54Cell11.setCellValue("");
						R54Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(54);
// R55 Col C
					Cell R55Cell = row.createCell(2);
					if (record.getR55_qua_i_lc() != null) {
						R55Cell.setCellValue(record.getR55_qua_i_lc().doubleValue());
						R55Cell.setCellStyle(numberStyle);
					} else {
						R55Cell.setCellValue("");
						R55Cell.setCellStyle(textStyle);
					}

// R55 Col D
					Cell R55Cell1 = row.createCell(3);
					if (record.getR55_qua_i_qar() != null) {
						R55Cell1.setCellValue(record.getR55_qua_i_qar().doubleValue());
						R55Cell1.setCellStyle(numberStyle);
					} else {
						R55Cell1.setCellValue("");
						R55Cell1.setCellStyle(textStyle);
					}

// R55 Col E
					Cell R55Cell2 = row.createCell(4);
					if (record.getR55_qua_i_inr() != null) {
						R55Cell2.setCellValue(record.getR55_qua_i_inr().doubleValue());
						R55Cell2.setCellStyle(numberStyle);
					} else {
						R55Cell2.setCellValue("");
						R55Cell2.setCellStyle(textStyle);
					}

// R55 Col F
					Cell R55Cell3 = row.createCell(5);
					if (record.getR55_qua_ii_lc() != null) {
						R55Cell3.setCellValue(record.getR55_qua_ii_lc().doubleValue());
						R55Cell3.setCellStyle(numberStyle);
					} else {
						R55Cell3.setCellValue("");
						R55Cell3.setCellStyle(textStyle);
					}

// R55 Col G
					Cell R55Cell4 = row.createCell(6);
					if (record.getR55_qua_ii_qar() != null) {
						R55Cell4.setCellValue(record.getR55_qua_ii_qar().doubleValue());
						R55Cell4.setCellStyle(numberStyle);
					} else {
						R55Cell4.setCellValue("");
						R55Cell4.setCellStyle(textStyle);
					}

// R55 Col H
					Cell R55Cell5 = row.createCell(7);
					if (record.getR55_qua_ii_inr() != null) {
						R55Cell5.setCellValue(record.getR55_qua_ii_inr().doubleValue());
						R55Cell5.setCellStyle(numberStyle);
					} else {
						R55Cell5.setCellValue("");
						R55Cell5.setCellStyle(textStyle);
					}

// R55 Col I
					Cell R55Cell6 = row.createCell(8);
					if (record.getR55_qua_iii_lc() != null) {
						R55Cell6.setCellValue(record.getR55_qua_iii_lc().doubleValue());
						R55Cell6.setCellStyle(numberStyle);
					} else {
						R55Cell6.setCellValue("");
						R55Cell6.setCellStyle(textStyle);
					}

// R55 Col J
					Cell R55Cell7 = row.createCell(9);
					if (record.getR55_qua_iii_qar() != null) {
						R55Cell7.setCellValue(record.getR55_qua_iii_qar().doubleValue());
						R55Cell7.setCellStyle(numberStyle);
					} else {
						R55Cell7.setCellValue("");
						R55Cell7.setCellStyle(textStyle);
					}

// R55 Col K
					Cell R55Cell8 = row.createCell(10);
					if (record.getR55_qua_iii_inr() != null) {
						R55Cell8.setCellValue(record.getR55_qua_iii_inr().doubleValue());
						R55Cell8.setCellStyle(numberStyle);
					} else {
						R55Cell8.setCellValue("");
						R55Cell8.setCellStyle(textStyle);
					}

// R55 Col L
					Cell R55Cell9 = row.createCell(11);
					if (record.getR55_qua_iv_lc() != null) {
						R55Cell9.setCellValue(record.getR55_qua_iv_lc().doubleValue());
						R55Cell9.setCellStyle(numberStyle);
					} else {
						R55Cell9.setCellValue("");
						R55Cell9.setCellStyle(textStyle);
					}

// R55 Col M
					Cell R55Cell10 = row.createCell(12);
					if (record.getR55_qua_iv_qar() != null) {
						R55Cell10.setCellValue(record.getR55_qua_iv_qar().doubleValue());
						R55Cell10.setCellStyle(numberStyle);
					} else {
						R55Cell10.setCellValue("");
						R55Cell10.setCellStyle(textStyle);
					}

// R55 Col N
					Cell R55Cell11 = row.createCell(13);
					if (record.getR55_qua_iv_inr() != null) {
						R55Cell11.setCellValue(record.getR55_qua_iv_inr().doubleValue());
						R55Cell11.setCellStyle(numberStyle);
					} else {
						R55Cell11.setCellValue("");
						R55Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(55);
// R56 Col C
					Cell R56Cell = row.createCell(2);
					if (record.getR56_qua_i_lc() != null) {
						R56Cell.setCellValue(record.getR56_qua_i_lc().doubleValue());
						R56Cell.setCellStyle(numberStyle);
					} else {
						R56Cell.setCellValue("");
						R56Cell.setCellStyle(textStyle);
					}

// R56 Col D
					Cell R56Cell1 = row.createCell(3);
					if (record.getR56_qua_i_qar() != null) {
						R56Cell1.setCellValue(record.getR56_qua_i_qar().doubleValue());
						R56Cell1.setCellStyle(numberStyle);
					} else {
						R56Cell1.setCellValue("");
						R56Cell1.setCellStyle(textStyle);
					}

// R56 Col E
					Cell R56Cell2 = row.createCell(4);
					if (record.getR56_qua_i_inr() != null) {
						R56Cell2.setCellValue(record.getR56_qua_i_inr().doubleValue());
						R56Cell2.setCellStyle(numberStyle);
					} else {
						R56Cell2.setCellValue("");
						R56Cell2.setCellStyle(textStyle);
					}

// R56 Col F
					Cell R56Cell3 = row.createCell(5);
					if (record.getR56_qua_ii_lc() != null) {
						R56Cell3.setCellValue(record.getR56_qua_ii_lc().doubleValue());
						R56Cell3.setCellStyle(numberStyle);
					} else {
						R56Cell3.setCellValue("");
						R56Cell3.setCellStyle(textStyle);
					}

// R56 Col G
					Cell R56Cell4 = row.createCell(6);
					if (record.getR56_qua_ii_qar() != null) {
						R56Cell4.setCellValue(record.getR56_qua_ii_qar().doubleValue());
						R56Cell4.setCellStyle(numberStyle);
					} else {
						R56Cell4.setCellValue("");
						R56Cell4.setCellStyle(textStyle);
					}

// R56 Col H
					Cell R56Cell5 = row.createCell(7);
					if (record.getR56_qua_ii_inr() != null) {
						R56Cell5.setCellValue(record.getR56_qua_ii_inr().doubleValue());
						R56Cell5.setCellStyle(numberStyle);
					} else {
						R56Cell5.setCellValue("");
						R56Cell5.setCellStyle(textStyle);
					}

// R56 Col I
					Cell R56Cell6 = row.createCell(8);
					if (record.getR56_qua_iii_lc() != null) {
						R56Cell6.setCellValue(record.getR56_qua_iii_lc().doubleValue());
						R56Cell6.setCellStyle(numberStyle);
					} else {
						R56Cell6.setCellValue("");
						R56Cell6.setCellStyle(textStyle);
					}

// R56 Col J
					Cell R56Cell7 = row.createCell(9);
					if (record.getR56_qua_iii_qar() != null) {
						R56Cell7.setCellValue(record.getR56_qua_iii_qar().doubleValue());
						R56Cell7.setCellStyle(numberStyle);
					} else {
						R56Cell7.setCellValue("");
						R56Cell7.setCellStyle(textStyle);
					}

// R56 Col K
					Cell R56Cell8 = row.createCell(10);
					if (record.getR56_qua_iii_inr() != null) {
						R56Cell8.setCellValue(record.getR56_qua_iii_inr().doubleValue());
						R56Cell8.setCellStyle(numberStyle);
					} else {
						R56Cell8.setCellValue("");
						R56Cell8.setCellStyle(textStyle);
					}

// R56 Col L
					Cell R56Cell9 = row.createCell(11);
					if (record.getR56_qua_iv_lc() != null) {
						R56Cell9.setCellValue(record.getR56_qua_iv_lc().doubleValue());
						R56Cell9.setCellStyle(numberStyle);
					} else {
						R56Cell9.setCellValue("");
						R56Cell9.setCellStyle(textStyle);
					}

// R56 Col M
					Cell R56Cell10 = row.createCell(12);
					if (record.getR56_qua_iv_qar() != null) {
						R56Cell10.setCellValue(record.getR56_qua_iv_qar().doubleValue());
						R56Cell10.setCellStyle(numberStyle);
					} else {
						R56Cell10.setCellValue("");
						R56Cell10.setCellStyle(textStyle);
					}

// R56 Col N
					Cell R56Cell11 = row.createCell(13);
					if (record.getR56_qua_iv_inr() != null) {
						R56Cell11.setCellValue(record.getR56_qua_iv_inr().doubleValue());
						R56Cell11.setCellStyle(numberStyle);
					} else {
						R56Cell11.setCellValue("");
						R56Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(56);
// R57 Col C
					Cell R57Cell = row.createCell(2);
					if (record.getR57_qua_i_lc() != null) {
						R57Cell.setCellValue(record.getR57_qua_i_lc().doubleValue());
						R57Cell.setCellStyle(numberStyle);
					} else {
						R57Cell.setCellValue("");
						R57Cell.setCellStyle(textStyle);
					}

// R57 Col D
					Cell R57Cell1 = row.createCell(3);
					if (record.getR57_qua_i_qar() != null) {
						R57Cell1.setCellValue(record.getR57_qua_i_qar().doubleValue());
						R57Cell1.setCellStyle(numberStyle);
					} else {
						R57Cell1.setCellValue("");
						R57Cell1.setCellStyle(textStyle);
					}

// R57 Col E
					Cell R57Cell2 = row.createCell(4);
					if (record.getR57_qua_i_inr() != null) {
						R57Cell2.setCellValue(record.getR57_qua_i_inr().doubleValue());
						R57Cell2.setCellStyle(numberStyle);
					} else {
						R57Cell2.setCellValue("");
						R57Cell2.setCellStyle(textStyle);
					}

// R57 Col F
					Cell R57Cell3 = row.createCell(5);
					if (record.getR57_qua_ii_lc() != null) {
						R57Cell3.setCellValue(record.getR57_qua_ii_lc().doubleValue());
						R57Cell3.setCellStyle(numberStyle);
					} else {
						R57Cell3.setCellValue("");
						R57Cell3.setCellStyle(textStyle);
					}

// R57 Col G
					Cell R57Cell4 = row.createCell(6);
					if (record.getR57_qua_ii_qar() != null) {
						R57Cell4.setCellValue(record.getR57_qua_ii_qar().doubleValue());
						R57Cell4.setCellStyle(numberStyle);
					} else {
						R57Cell4.setCellValue("");
						R57Cell4.setCellStyle(textStyle);
					}

// R57 Col H
					Cell R57Cell5 = row.createCell(7);
					if (record.getR57_qua_ii_inr() != null) {
						R57Cell5.setCellValue(record.getR57_qua_ii_inr().doubleValue());
						R57Cell5.setCellStyle(numberStyle);
					} else {
						R57Cell5.setCellValue("");
						R57Cell5.setCellStyle(textStyle);
					}

// R57 Col I
					Cell R57Cell6 = row.createCell(8);
					if (record.getR57_qua_iii_lc() != null) {
						R57Cell6.setCellValue(record.getR57_qua_iii_lc().doubleValue());
						R57Cell6.setCellStyle(numberStyle);
					} else {
						R57Cell6.setCellValue("");
						R57Cell6.setCellStyle(textStyle);
					}

// R57 Col J
					Cell R57Cell7 = row.createCell(9);
					if (record.getR57_qua_iii_qar() != null) {
						R57Cell7.setCellValue(record.getR57_qua_iii_qar().doubleValue());
						R57Cell7.setCellStyle(numberStyle);
					} else {
						R57Cell7.setCellValue("");
						R57Cell7.setCellStyle(textStyle);
					}

// R57 Col K
					Cell R57Cell8 = row.createCell(10);
					if (record.getR57_qua_iii_inr() != null) {
						R57Cell8.setCellValue(record.getR57_qua_iii_inr().doubleValue());
						R57Cell8.setCellStyle(numberStyle);
					} else {
						R57Cell8.setCellValue("");
						R57Cell8.setCellStyle(textStyle);
					}

// R57 Col L
					Cell R57Cell9 = row.createCell(11);
					if (record.getR57_qua_iv_lc() != null) {
						R57Cell9.setCellValue(record.getR57_qua_iv_lc().doubleValue());
						R57Cell9.setCellStyle(numberStyle);
					} else {
						R57Cell9.setCellValue("");
						R57Cell9.setCellStyle(textStyle);
					}

// R57 Col M
					Cell R57Cell10 = row.createCell(12);
					if (record.getR57_qua_iv_qar() != null) {
						R57Cell10.setCellValue(record.getR57_qua_iv_qar().doubleValue());
						R57Cell10.setCellStyle(numberStyle);
					} else {
						R57Cell10.setCellValue("");
						R57Cell10.setCellStyle(textStyle);
					}

// R57 Col N
					Cell R57Cell11 = row.createCell(13);
					if (record.getR57_qua_iv_inr() != null) {
						R57Cell11.setCellValue(record.getR57_qua_iv_inr().doubleValue());
						R57Cell11.setCellStyle(numberStyle);
					} else {
						R57Cell11.setCellValue("");
						R57Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(57);
// R58 Col C
					Cell R58Cell = row.createCell(2);
					if (record.getR58_qua_i_lc() != null) {
						R58Cell.setCellValue(record.getR58_qua_i_lc().doubleValue());
						R58Cell.setCellStyle(numberStyle);
					} else {
						R58Cell.setCellValue("");
						R58Cell.setCellStyle(textStyle);
					}

// R58 Col D
					Cell R58Cell1 = row.createCell(3);
					if (record.getR58_qua_i_qar() != null) {
						R58Cell1.setCellValue(record.getR58_qua_i_qar().doubleValue());
						R58Cell1.setCellStyle(numberStyle);
					} else {
						R58Cell1.setCellValue("");
						R58Cell1.setCellStyle(textStyle);
					}

// R58 Col E
					Cell R58Cell2 = row.createCell(4);
					if (record.getR58_qua_i_inr() != null) {
						R58Cell2.setCellValue(record.getR58_qua_i_inr().doubleValue());
						R58Cell2.setCellStyle(numberStyle);
					} else {
						R58Cell2.setCellValue("");
						R58Cell2.setCellStyle(textStyle);
					}

// R58 Col F
					Cell R58Cell3 = row.createCell(5);
					if (record.getR58_qua_ii_lc() != null) {
						R58Cell3.setCellValue(record.getR58_qua_ii_lc().doubleValue());
						R58Cell3.setCellStyle(numberStyle);
					} else {
						R58Cell3.setCellValue("");
						R58Cell3.setCellStyle(textStyle);
					}

// R58 Col G
					Cell R58Cell4 = row.createCell(6);
					if (record.getR58_qua_ii_qar() != null) {
						R58Cell4.setCellValue(record.getR58_qua_ii_qar().doubleValue());
						R58Cell4.setCellStyle(numberStyle);
					} else {
						R58Cell4.setCellValue("");
						R58Cell4.setCellStyle(textStyle);
					}

// R58 Col H
					Cell R58Cell5 = row.createCell(7);
					if (record.getR58_qua_ii_inr() != null) {
						R58Cell5.setCellValue(record.getR58_qua_ii_inr().doubleValue());
						R58Cell5.setCellStyle(numberStyle);
					} else {
						R58Cell5.setCellValue("");
						R58Cell5.setCellStyle(textStyle);
					}

// R58 Col I
					Cell R58Cell6 = row.createCell(8);
					if (record.getR58_qua_iii_lc() != null) {
						R58Cell6.setCellValue(record.getR58_qua_iii_lc().doubleValue());
						R58Cell6.setCellStyle(numberStyle);
					} else {
						R58Cell6.setCellValue("");
						R58Cell6.setCellStyle(textStyle);
					}

// R58 Col J
					Cell R58Cell7 = row.createCell(9);
					if (record.getR58_qua_iii_qar() != null) {
						R58Cell7.setCellValue(record.getR58_qua_iii_qar().doubleValue());
						R58Cell7.setCellStyle(numberStyle);
					} else {
						R58Cell7.setCellValue("");
						R58Cell7.setCellStyle(textStyle);
					}

// R58 Col K
					Cell R58Cell8 = row.createCell(10);
					if (record.getR58_qua_iii_inr() != null) {
						R58Cell8.setCellValue(record.getR58_qua_iii_inr().doubleValue());
						R58Cell8.setCellStyle(numberStyle);
					} else {
						R58Cell8.setCellValue("");
						R58Cell8.setCellStyle(textStyle);
					}

// R58 Col L
					Cell R58Cell9 = row.createCell(11);
					if (record.getR58_qua_iv_lc() != null) {
						R58Cell9.setCellValue(record.getR58_qua_iv_lc().doubleValue());
						R58Cell9.setCellStyle(numberStyle);
					} else {
						R58Cell9.setCellValue("");
						R58Cell9.setCellStyle(textStyle);
					}

// R58 Col M
					Cell R58Cell10 = row.createCell(12);
					if (record.getR58_qua_iv_qar() != null) {
						R58Cell10.setCellValue(record.getR58_qua_iv_qar().doubleValue());
						R58Cell10.setCellStyle(numberStyle);
					} else {
						R58Cell10.setCellValue("");
						R58Cell10.setCellStyle(textStyle);
					}

// R58 Col N
					Cell R58Cell11 = row.createCell(13);
					if (record.getR58_qua_iv_inr() != null) {
						R58Cell11.setCellValue(record.getR58_qua_iv_inr().doubleValue());
						R58Cell11.setCellStyle(numberStyle);
					} else {
						R58Cell11.setCellValue("");
						R58Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(58);
// R59 Col C
					Cell R59Cell = row.createCell(2);
					if (record.getR59_qua_i_lc() != null) {
						R59Cell.setCellValue(record.getR59_qua_i_lc().doubleValue());
						R59Cell.setCellStyle(numberStyle);
					} else {
						R59Cell.setCellValue("");
						R59Cell.setCellStyle(textStyle);
					}

// R59 Col D
					Cell R59Cell1 = row.createCell(3);
					if (record.getR59_qua_i_qar() != null) {
						R59Cell1.setCellValue(record.getR59_qua_i_qar().doubleValue());
						R59Cell1.setCellStyle(numberStyle);
					} else {
						R59Cell1.setCellValue("");
						R59Cell1.setCellStyle(textStyle);
					}

// R59 Col E
					Cell R59Cell2 = row.createCell(4);
					if (record.getR59_qua_i_inr() != null) {
						R59Cell2.setCellValue(record.getR59_qua_i_inr().doubleValue());
						R59Cell2.setCellStyle(numberStyle);
					} else {
						R59Cell2.setCellValue("");
						R59Cell2.setCellStyle(textStyle);
					}

// R59 Col F
					Cell R59Cell3 = row.createCell(5);
					if (record.getR59_qua_ii_lc() != null) {
						R59Cell3.setCellValue(record.getR59_qua_ii_lc().doubleValue());
						R59Cell3.setCellStyle(numberStyle);
					} else {
						R59Cell3.setCellValue("");
						R59Cell3.setCellStyle(textStyle);
					}

// R59 Col G
					Cell R59Cell4 = row.createCell(6);
					if (record.getR59_qua_ii_qar() != null) {
						R59Cell4.setCellValue(record.getR59_qua_ii_qar().doubleValue());
						R59Cell4.setCellStyle(numberStyle);
					} else {
						R59Cell4.setCellValue("");
						R59Cell4.setCellStyle(textStyle);
					}

// R59 Col H
					Cell R59Cell5 = row.createCell(7);
					if (record.getR59_qua_ii_inr() != null) {
						R59Cell5.setCellValue(record.getR59_qua_ii_inr().doubleValue());
						R59Cell5.setCellStyle(numberStyle);
					} else {
						R59Cell5.setCellValue("");
						R59Cell5.setCellStyle(textStyle);
					}

// R59 Col I
					Cell R59Cell6 = row.createCell(8);
					if (record.getR59_qua_iii_lc() != null) {
						R59Cell6.setCellValue(record.getR59_qua_iii_lc().doubleValue());
						R59Cell6.setCellStyle(numberStyle);
					} else {
						R59Cell6.setCellValue("");
						R59Cell6.setCellStyle(textStyle);
					}

// R59 Col J
					Cell R59Cell7 = row.createCell(9);
					if (record.getR59_qua_iii_qar() != null) {
						R59Cell7.setCellValue(record.getR59_qua_iii_qar().doubleValue());
						R59Cell7.setCellStyle(numberStyle);
					} else {
						R59Cell7.setCellValue("");
						R59Cell7.setCellStyle(textStyle);
					}

// R59 Col K
					Cell R59Cell8 = row.createCell(10);
					if (record.getR59_qua_iii_inr() != null) {
						R59Cell8.setCellValue(record.getR59_qua_iii_inr().doubleValue());
						R59Cell8.setCellStyle(numberStyle);
					} else {
						R59Cell8.setCellValue("");
						R59Cell8.setCellStyle(textStyle);
					}

// R59 Col L
					Cell R59Cell9 = row.createCell(11);
					if (record.getR59_qua_iv_lc() != null) {
						R59Cell9.setCellValue(record.getR59_qua_iv_lc().doubleValue());
						R59Cell9.setCellStyle(numberStyle);
					} else {
						R59Cell9.setCellValue("");
						R59Cell9.setCellStyle(textStyle);
					}

// R59 Col M
					Cell R59Cell10 = row.createCell(12);
					if (record.getR59_qua_iv_qar() != null) {
						R59Cell10.setCellValue(record.getR59_qua_iv_qar().doubleValue());
						R59Cell10.setCellStyle(numberStyle);
					} else {
						R59Cell10.setCellValue("");
						R59Cell10.setCellStyle(textStyle);
					}

// R59 Col N
					Cell R59Cell11 = row.createCell(13);
					if (record.getR59_qua_iv_inr() != null) {
						R59Cell11.setCellValue(record.getR59_qua_iv_inr().doubleValue());
						R59Cell11.setCellStyle(numberStyle);
					} else {
						R59Cell11.setCellValue("");
						R59Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(59);
// R60 Col C
					Cell R60Cell = row.createCell(2);
					if (record.getR60_qua_i_lc() != null) {
						R60Cell.setCellValue(record.getR60_qua_i_lc().doubleValue());
						R60Cell.setCellStyle(numberStyle);
					} else {
						R60Cell.setCellValue("");
						R60Cell.setCellStyle(textStyle);
					}

// R60 Col D
					Cell R60Cell1 = row.createCell(3);
					if (record.getR60_qua_i_qar() != null) {
						R60Cell1.setCellValue(record.getR60_qua_i_qar().doubleValue());
						R60Cell1.setCellStyle(numberStyle);
					} else {
						R60Cell1.setCellValue("");
						R60Cell1.setCellStyle(textStyle);
					}

// R60 Col E
					Cell R60Cell2 = row.createCell(4);
					if (record.getR60_qua_i_inr() != null) {
						R60Cell2.setCellValue(record.getR60_qua_i_inr().doubleValue());
						R60Cell2.setCellStyle(numberStyle);
					} else {
						R60Cell2.setCellValue("");
						R60Cell2.setCellStyle(textStyle);
					}

// R60 Col F
					Cell R60Cell3 = row.createCell(5);
					if (record.getR60_qua_ii_lc() != null) {
						R60Cell3.setCellValue(record.getR60_qua_ii_lc().doubleValue());
						R60Cell3.setCellStyle(numberStyle);
					} else {
						R60Cell3.setCellValue("");
						R60Cell3.setCellStyle(textStyle);
					}

// R60 Col G
					Cell R60Cell4 = row.createCell(6);
					if (record.getR60_qua_ii_qar() != null) {
						R60Cell4.setCellValue(record.getR60_qua_ii_qar().doubleValue());
						R60Cell4.setCellStyle(numberStyle);
					} else {
						R60Cell4.setCellValue("");
						R60Cell4.setCellStyle(textStyle);
					}

// R60 Col H
					Cell R60Cell5 = row.createCell(7);
					if (record.getR60_qua_ii_inr() != null) {
						R60Cell5.setCellValue(record.getR60_qua_ii_inr().doubleValue());
						R60Cell5.setCellStyle(numberStyle);
					} else {
						R60Cell5.setCellValue("");
						R60Cell5.setCellStyle(textStyle);
					}

// R60 Col I
					Cell R60Cell6 = row.createCell(8);
					if (record.getR60_qua_iii_lc() != null) {
						R60Cell6.setCellValue(record.getR60_qua_iii_lc().doubleValue());
						R60Cell6.setCellStyle(numberStyle);
					} else {
						R60Cell6.setCellValue("");
						R60Cell6.setCellStyle(textStyle);
					}

// R60 Col J
					Cell R60Cell7 = row.createCell(9);
					if (record.getR60_qua_iii_qar() != null) {
						R60Cell7.setCellValue(record.getR60_qua_iii_qar().doubleValue());
						R60Cell7.setCellStyle(numberStyle);
					} else {
						R60Cell7.setCellValue("");
						R60Cell7.setCellStyle(textStyle);
					}

// R60 Col K
					Cell R60Cell8 = row.createCell(10);
					if (record.getR60_qua_iii_inr() != null) {
						R60Cell8.setCellValue(record.getR60_qua_iii_inr().doubleValue());
						R60Cell8.setCellStyle(numberStyle);
					} else {
						R60Cell8.setCellValue("");
						R60Cell8.setCellStyle(textStyle);
					}

// R60 Col L
					Cell R60Cell9 = row.createCell(11);
					if (record.getR60_qua_iv_lc() != null) {
						R60Cell9.setCellValue(record.getR60_qua_iv_lc().doubleValue());
						R60Cell9.setCellStyle(numberStyle);
					} else {
						R60Cell9.setCellValue("");
						R60Cell9.setCellStyle(textStyle);
					}

// R60 Col M
					Cell R60Cell10 = row.createCell(12);
					if (record.getR60_qua_iv_qar() != null) {
						R60Cell10.setCellValue(record.getR60_qua_iv_qar().doubleValue());
						R60Cell10.setCellStyle(numberStyle);
					} else {
						R60Cell10.setCellValue("");
						R60Cell10.setCellStyle(textStyle);
					}

// R60 Col N
					Cell R60Cell11 = row.createCell(13);
					if (record.getR60_qua_iv_inr() != null) {
						R60Cell11.setCellValue(record.getR60_qua_iv_inr().doubleValue());
						R60Cell11.setCellStyle(numberStyle);
					} else {
						R60Cell11.setCellValue("");
						R60Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(60);
// R61 Col C
					Cell R61Cell = row.createCell(2);
					if (record.getR61_qua_i_lc() != null) {
						R61Cell.setCellValue(record.getR61_qua_i_lc().doubleValue());
						R61Cell.setCellStyle(numberStyle);
					} else {
						R61Cell.setCellValue("");
						R61Cell.setCellStyle(textStyle);
					}

// R61 Col D
					Cell R61Cell1 = row.createCell(3);
					if (record.getR61_qua_i_qar() != null) {
						R61Cell1.setCellValue(record.getR61_qua_i_qar().doubleValue());
						R61Cell1.setCellStyle(numberStyle);
					} else {
						R61Cell1.setCellValue("");
						R61Cell1.setCellStyle(textStyle);
					}

// R61 Col E
					Cell R61Cell2 = row.createCell(4);
					if (record.getR61_qua_i_inr() != null) {
						R61Cell2.setCellValue(record.getR61_qua_i_inr().doubleValue());
						R61Cell2.setCellStyle(numberStyle);
					} else {
						R61Cell2.setCellValue("");
						R61Cell2.setCellStyle(textStyle);
					}

// R61 Col F
					Cell R61Cell3 = row.createCell(5);
					if (record.getR61_qua_ii_lc() != null) {
						R61Cell3.setCellValue(record.getR61_qua_ii_lc().doubleValue());
						R61Cell3.setCellStyle(numberStyle);
					} else {
						R61Cell3.setCellValue("");
						R61Cell3.setCellStyle(textStyle);
					}

// R61 Col G
					Cell R61Cell4 = row.createCell(6);
					if (record.getR61_qua_ii_qar() != null) {
						R61Cell4.setCellValue(record.getR61_qua_ii_qar().doubleValue());
						R61Cell4.setCellStyle(numberStyle);
					} else {
						R61Cell4.setCellValue("");
						R61Cell4.setCellStyle(textStyle);
					}

// R61 Col H
					Cell R61Cell5 = row.createCell(7);
					if (record.getR61_qua_ii_inr() != null) {
						R61Cell5.setCellValue(record.getR61_qua_ii_inr().doubleValue());
						R61Cell5.setCellStyle(numberStyle);
					} else {
						R61Cell5.setCellValue("");
						R61Cell5.setCellStyle(textStyle);
					}

// R61 Col I
					Cell R61Cell6 = row.createCell(8);
					if (record.getR61_qua_iii_lc() != null) {
						R61Cell6.setCellValue(record.getR61_qua_iii_lc().doubleValue());
						R61Cell6.setCellStyle(numberStyle);
					} else {
						R61Cell6.setCellValue("");
						R61Cell6.setCellStyle(textStyle);
					}

// R61 Col J
					Cell R61Cell7 = row.createCell(9);
					if (record.getR61_qua_iii_qar() != null) {
						R61Cell7.setCellValue(record.getR61_qua_iii_qar().doubleValue());
						R61Cell7.setCellStyle(numberStyle);
					} else {
						R61Cell7.setCellValue("");
						R61Cell7.setCellStyle(textStyle);
					}

// R61 Col K
					Cell R61Cell8 = row.createCell(10);
					if (record.getR61_qua_iii_inr() != null) {
						R61Cell8.setCellValue(record.getR61_qua_iii_inr().doubleValue());
						R61Cell8.setCellStyle(numberStyle);
					} else {
						R61Cell8.setCellValue("");
						R61Cell8.setCellStyle(textStyle);
					}

// R61 Col L
					Cell R61Cell9 = row.createCell(11);
					if (record.getR61_qua_iv_lc() != null) {
						R61Cell9.setCellValue(record.getR61_qua_iv_lc().doubleValue());
						R61Cell9.setCellStyle(numberStyle);
					} else {
						R61Cell9.setCellValue("");
						R61Cell9.setCellStyle(textStyle);
					}

// R61 Col M
					Cell R61Cell10 = row.createCell(12);
					if (record.getR61_qua_iv_qar() != null) {
						R61Cell10.setCellValue(record.getR61_qua_iv_qar().doubleValue());
						R61Cell10.setCellStyle(numberStyle);
					} else {
						R61Cell10.setCellValue("");
						R61Cell10.setCellStyle(textStyle);
					}

// R61 Col N
					Cell R61Cell11 = row.createCell(13);
					if (record.getR61_qua_iv_inr() != null) {
						R61Cell11.setCellValue(record.getR61_qua_iv_inr().doubleValue());
						R61Cell11.setCellStyle(numberStyle);
					} else {
						R61Cell11.setCellValue("");
						R61Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(61);
// R62 Col C
					Cell R62Cell = row.createCell(2);
					if (record.getR62_qua_i_lc() != null) {
						R62Cell.setCellValue(record.getR62_qua_i_lc().doubleValue());
						R62Cell.setCellStyle(numberStyle);
					} else {
						R62Cell.setCellValue("");
						R62Cell.setCellStyle(textStyle);
					}

// R62 Col D
					Cell R62Cell1 = row.createCell(3);
					if (record.getR62_qua_i_qar() != null) {
						R62Cell1.setCellValue(record.getR62_qua_i_qar().doubleValue());
						R62Cell1.setCellStyle(numberStyle);
					} else {
						R62Cell1.setCellValue("");
						R62Cell1.setCellStyle(textStyle);
					}

// R62 Col E
					Cell R62Cell2 = row.createCell(4);
					if (record.getR62_qua_i_inr() != null) {
						R62Cell2.setCellValue(record.getR62_qua_i_inr().doubleValue());
						R62Cell2.setCellStyle(numberStyle);
					} else {
						R62Cell2.setCellValue("");
						R62Cell2.setCellStyle(textStyle);
					}

// R62 Col F
					Cell R62Cell3 = row.createCell(5);
					if (record.getR62_qua_ii_lc() != null) {
						R62Cell3.setCellValue(record.getR62_qua_ii_lc().doubleValue());
						R62Cell3.setCellStyle(numberStyle);
					} else {
						R62Cell3.setCellValue("");
						R62Cell3.setCellStyle(textStyle);
					}

// R62 Col G
					Cell R62Cell4 = row.createCell(6);
					if (record.getR62_qua_ii_qar() != null) {
						R62Cell4.setCellValue(record.getR62_qua_ii_qar().doubleValue());
						R62Cell4.setCellStyle(numberStyle);
					} else {
						R62Cell4.setCellValue("");
						R62Cell4.setCellStyle(textStyle);
					}

// R62 Col H
					Cell R62Cell5 = row.createCell(7);
					if (record.getR62_qua_ii_inr() != null) {
						R62Cell5.setCellValue(record.getR62_qua_ii_inr().doubleValue());
						R62Cell5.setCellStyle(numberStyle);
					} else {
						R62Cell5.setCellValue("");
						R62Cell5.setCellStyle(textStyle);
					}

// R62 Col I
					Cell R62Cell6 = row.createCell(8);
					if (record.getR62_qua_iii_lc() != null) {
						R62Cell6.setCellValue(record.getR62_qua_iii_lc().doubleValue());
						R62Cell6.setCellStyle(numberStyle);
					} else {
						R62Cell6.setCellValue("");
						R62Cell6.setCellStyle(textStyle);
					}

// R62 Col J
					Cell R62Cell7 = row.createCell(9);
					if (record.getR62_qua_iii_qar() != null) {
						R62Cell7.setCellValue(record.getR62_qua_iii_qar().doubleValue());
						R62Cell7.setCellStyle(numberStyle);
					} else {
						R62Cell7.setCellValue("");
						R62Cell7.setCellStyle(textStyle);
					}

// R62 Col K
					Cell R62Cell8 = row.createCell(10);
					if (record.getR62_qua_iii_inr() != null) {
						R62Cell8.setCellValue(record.getR62_qua_iii_inr().doubleValue());
						R62Cell8.setCellStyle(numberStyle);
					} else {
						R62Cell8.setCellValue("");
						R62Cell8.setCellStyle(textStyle);
					}

// R62 Col L
					Cell R62Cell9 = row.createCell(11);
					if (record.getR62_qua_iv_lc() != null) {
						R62Cell9.setCellValue(record.getR62_qua_iv_lc().doubleValue());
						R62Cell9.setCellStyle(numberStyle);
					} else {
						R62Cell9.setCellValue("");
						R62Cell9.setCellStyle(textStyle);
					}

// R62 Col M
					Cell R62Cell10 = row.createCell(12);
					if (record.getR62_qua_iv_qar() != null) {
						R62Cell10.setCellValue(record.getR62_qua_iv_qar().doubleValue());
						R62Cell10.setCellStyle(numberStyle);
					} else {
						R62Cell10.setCellValue("");
						R62Cell10.setCellStyle(textStyle);
					}

// R62 Col N
					Cell R62Cell11 = row.createCell(13);
					if (record.getR62_qua_iv_inr() != null) {
						R62Cell11.setCellValue(record.getR62_qua_iv_inr().doubleValue());
						R62Cell11.setCellStyle(numberStyle);
					} else {
						R62Cell11.setCellValue("");
						R62Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(62);
// R63 Col C
					Cell R63Cell = row.createCell(2);
					if (record.getR63_qua_i_lc() != null) {
						R63Cell.setCellValue(record.getR63_qua_i_lc().doubleValue());
						R63Cell.setCellStyle(numberStyle);
					} else {
						R63Cell.setCellValue("");
						R63Cell.setCellStyle(textStyle);
					}

// R63 Col D
					Cell R63Cell1 = row.createCell(3);
					if (record.getR63_qua_i_qar() != null) {
						R63Cell1.setCellValue(record.getR63_qua_i_qar().doubleValue());
						R63Cell1.setCellStyle(numberStyle);
					} else {
						R63Cell1.setCellValue("");
						R63Cell1.setCellStyle(textStyle);
					}

// R63 Col E
					Cell R63Cell2 = row.createCell(4);
					if (record.getR63_qua_i_inr() != null) {
						R63Cell2.setCellValue(record.getR63_qua_i_inr().doubleValue());
						R63Cell2.setCellStyle(numberStyle);
					} else {
						R63Cell2.setCellValue("");
						R63Cell2.setCellStyle(textStyle);
					}

// R63 Col F
					Cell R63Cell3 = row.createCell(5);
					if (record.getR63_qua_ii_lc() != null) {
						R63Cell3.setCellValue(record.getR63_qua_ii_lc().doubleValue());
						R63Cell3.setCellStyle(numberStyle);
					} else {
						R63Cell3.setCellValue("");
						R63Cell3.setCellStyle(textStyle);
					}

// R63 Col G
					Cell R63Cell4 = row.createCell(6);
					if (record.getR63_qua_ii_qar() != null) {
						R63Cell4.setCellValue(record.getR63_qua_ii_qar().doubleValue());
						R63Cell4.setCellStyle(numberStyle);
					} else {
						R63Cell4.setCellValue("");
						R63Cell4.setCellStyle(textStyle);
					}

// R63 Col H
					Cell R63Cell5 = row.createCell(7);
					if (record.getR63_qua_ii_inr() != null) {
						R63Cell5.setCellValue(record.getR63_qua_ii_inr().doubleValue());
						R63Cell5.setCellStyle(numberStyle);
					} else {
						R63Cell5.setCellValue("");
						R63Cell5.setCellStyle(textStyle);
					}

// R63 Col I
					Cell R63Cell6 = row.createCell(8);
					if (record.getR63_qua_iii_lc() != null) {
						R63Cell6.setCellValue(record.getR63_qua_iii_lc().doubleValue());
						R63Cell6.setCellStyle(numberStyle);
					} else {
						R63Cell6.setCellValue("");
						R63Cell6.setCellStyle(textStyle);
					}

// R63 Col J
					Cell R63Cell7 = row.createCell(9);
					if (record.getR63_qua_iii_qar() != null) {
						R63Cell7.setCellValue(record.getR63_qua_iii_qar().doubleValue());
						R63Cell7.setCellStyle(numberStyle);
					} else {
						R63Cell7.setCellValue("");
						R63Cell7.setCellStyle(textStyle);
					}

// R63 Col K
					Cell R63Cell8 = row.createCell(10);
					if (record.getR63_qua_iii_inr() != null) {
						R63Cell8.setCellValue(record.getR63_qua_iii_inr().doubleValue());
						R63Cell8.setCellStyle(numberStyle);
					} else {
						R63Cell8.setCellValue("");
						R63Cell8.setCellStyle(textStyle);
					}

// R63 Col L
					Cell R63Cell9 = row.createCell(11);
					if (record.getR63_qua_iv_lc() != null) {
						R63Cell9.setCellValue(record.getR63_qua_iv_lc().doubleValue());
						R63Cell9.setCellStyle(numberStyle);
					} else {
						R63Cell9.setCellValue("");
						R63Cell9.setCellStyle(textStyle);
					}

// R63 Col M
					Cell R63Cell10 = row.createCell(12);
					if (record.getR63_qua_iv_qar() != null) {
						R63Cell10.setCellValue(record.getR63_qua_iv_qar().doubleValue());
						R63Cell10.setCellStyle(numberStyle);
					} else {
						R63Cell10.setCellValue("");
						R63Cell10.setCellStyle(textStyle);
					}

// R63 Col N
					Cell R63Cell11 = row.createCell(13);
					if (record.getR63_qua_iv_inr() != null) {
						R63Cell11.setCellValue(record.getR63_qua_iv_inr().doubleValue());
						R63Cell11.setCellStyle(numberStyle);
					} else {
						R63Cell11.setCellValue("");
						R63Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(63);
// R64 Col C
					Cell R64Cell = row.createCell(2);
					if (record.getR64_qua_i_lc() != null) {
						R64Cell.setCellValue(record.getR64_qua_i_lc().doubleValue());
						R64Cell.setCellStyle(numberStyle);
					} else {
						R64Cell.setCellValue("");
						R64Cell.setCellStyle(textStyle);
					}

// R64 Col D
					Cell R64Cell1 = row.createCell(3);
					if (record.getR64_qua_i_qar() != null) {
						R64Cell1.setCellValue(record.getR64_qua_i_qar().doubleValue());
						R64Cell1.setCellStyle(numberStyle);
					} else {
						R64Cell1.setCellValue("");
						R64Cell1.setCellStyle(textStyle);
					}

// R64 Col E
					Cell R64Cell2 = row.createCell(4);
					if (record.getR64_qua_i_inr() != null) {
						R64Cell2.setCellValue(record.getR64_qua_i_inr().doubleValue());
						R64Cell2.setCellStyle(numberStyle);
					} else {
						R64Cell2.setCellValue("");
						R64Cell2.setCellStyle(textStyle);
					}

// R64 Col F
					Cell R64Cell3 = row.createCell(5);
					if (record.getR64_qua_ii_lc() != null) {
						R64Cell3.setCellValue(record.getR64_qua_ii_lc().doubleValue());
						R64Cell3.setCellStyle(numberStyle);
					} else {
						R64Cell3.setCellValue("");
						R64Cell3.setCellStyle(textStyle);
					}

// R64 Col G
					Cell R64Cell4 = row.createCell(6);
					if (record.getR64_qua_ii_qar() != null) {
						R64Cell4.setCellValue(record.getR64_qua_ii_qar().doubleValue());
						R64Cell4.setCellStyle(numberStyle);
					} else {
						R64Cell4.setCellValue("");
						R64Cell4.setCellStyle(textStyle);
					}

// R64 Col H
					Cell R64Cell5 = row.createCell(7);
					if (record.getR64_qua_ii_inr() != null) {
						R64Cell5.setCellValue(record.getR64_qua_ii_inr().doubleValue());
						R64Cell5.setCellStyle(numberStyle);
					} else {
						R64Cell5.setCellValue("");
						R64Cell5.setCellStyle(textStyle);
					}

// R64 Col I
					Cell R64Cell6 = row.createCell(8);
					if (record.getR64_qua_iii_lc() != null) {
						R64Cell6.setCellValue(record.getR64_qua_iii_lc().doubleValue());
						R64Cell6.setCellStyle(numberStyle);
					} else {
						R64Cell6.setCellValue("");
						R64Cell6.setCellStyle(textStyle);
					}

// R64 Col J
					Cell R64Cell7 = row.createCell(9);
					if (record.getR64_qua_iii_qar() != null) {
						R64Cell7.setCellValue(record.getR64_qua_iii_qar().doubleValue());
						R64Cell7.setCellStyle(numberStyle);
					} else {
						R64Cell7.setCellValue("");
						R64Cell7.setCellStyle(textStyle);
					}

// R64 Col K
					Cell R64Cell8 = row.createCell(10);
					if (record.getR64_qua_iii_inr() != null) {
						R64Cell8.setCellValue(record.getR64_qua_iii_inr().doubleValue());
						R64Cell8.setCellStyle(numberStyle);
					} else {
						R64Cell8.setCellValue("");
						R64Cell8.setCellStyle(textStyle);
					}

// R64 Col L
					Cell R64Cell9 = row.createCell(11);
					if (record.getR64_qua_iv_lc() != null) {
						R64Cell9.setCellValue(record.getR64_qua_iv_lc().doubleValue());
						R64Cell9.setCellStyle(numberStyle);
					} else {
						R64Cell9.setCellValue("");
						R64Cell9.setCellStyle(textStyle);
					}

// R64 Col M
					Cell R64Cell10 = row.createCell(12);
					if (record.getR64_qua_iv_qar() != null) {
						R64Cell10.setCellValue(record.getR64_qua_iv_qar().doubleValue());
						R64Cell10.setCellStyle(numberStyle);
					} else {
						R64Cell10.setCellValue("");
						R64Cell10.setCellStyle(textStyle);
					}

// R64 Col N
					Cell R64Cell11 = row.createCell(13);
					if (record.getR64_qua_iv_inr() != null) {
						R64Cell11.setCellValue(record.getR64_qua_iv_inr().doubleValue());
						R64Cell11.setCellStyle(numberStyle);
					} else {
						R64Cell11.setCellValue("");
						R64Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(64);
// R65 Col C
					Cell R65Cell = row.createCell(2);
					if (record.getR65_qua_i_lc() != null) {
						R65Cell.setCellValue(record.getR65_qua_i_lc().doubleValue());
						R65Cell.setCellStyle(numberStyle);
					} else {
						R65Cell.setCellValue("");
						R65Cell.setCellStyle(textStyle);
					}

// R65 Col D
					Cell R65Cell1 = row.createCell(3);
					if (record.getR65_qua_i_qar() != null) {
						R65Cell1.setCellValue(record.getR65_qua_i_qar().doubleValue());
						R65Cell1.setCellStyle(numberStyle);
					} else {
						R65Cell1.setCellValue("");
						R65Cell1.setCellStyle(textStyle);
					}

// R65 Col E
					Cell R65Cell2 = row.createCell(4);
					if (record.getR65_qua_i_inr() != null) {
						R65Cell2.setCellValue(record.getR65_qua_i_inr().doubleValue());
						R65Cell2.setCellStyle(numberStyle);
					} else {
						R65Cell2.setCellValue("");
						R65Cell2.setCellStyle(textStyle);
					}

// R65 Col F
					Cell R65Cell3 = row.createCell(5);
					if (record.getR65_qua_ii_lc() != null) {
						R65Cell3.setCellValue(record.getR65_qua_ii_lc().doubleValue());
						R65Cell3.setCellStyle(numberStyle);
					} else {
						R65Cell3.setCellValue("");
						R65Cell3.setCellStyle(textStyle);
					}

// R65 Col G
					Cell R65Cell4 = row.createCell(6);
					if (record.getR65_qua_ii_qar() != null) {
						R65Cell4.setCellValue(record.getR65_qua_ii_qar().doubleValue());
						R65Cell4.setCellStyle(numberStyle);
					} else {
						R65Cell4.setCellValue("");
						R65Cell4.setCellStyle(textStyle);
					}

// R65 Col H
					Cell R65Cell5 = row.createCell(7);
					if (record.getR65_qua_ii_inr() != null) {
						R65Cell5.setCellValue(record.getR65_qua_ii_inr().doubleValue());
						R65Cell5.setCellStyle(numberStyle);
					} else {
						R65Cell5.setCellValue("");
						R65Cell5.setCellStyle(textStyle);
					}

// R65 Col I
					Cell R65Cell6 = row.createCell(8);
					if (record.getR65_qua_iii_lc() != null) {
						R65Cell6.setCellValue(record.getR65_qua_iii_lc().doubleValue());
						R65Cell6.setCellStyle(numberStyle);
					} else {
						R65Cell6.setCellValue("");
						R65Cell6.setCellStyle(textStyle);
					}

// R65 Col J
					Cell R65Cell7 = row.createCell(9);
					if (record.getR65_qua_iii_qar() != null) {
						R65Cell7.setCellValue(record.getR65_qua_iii_qar().doubleValue());
						R65Cell7.setCellStyle(numberStyle);
					} else {
						R65Cell7.setCellValue("");
						R65Cell7.setCellStyle(textStyle);
					}

// R65 Col K
					Cell R65Cell8 = row.createCell(10);
					if (record.getR65_qua_iii_inr() != null) {
						R65Cell8.setCellValue(record.getR65_qua_iii_inr().doubleValue());
						R65Cell8.setCellStyle(numberStyle);
					} else {
						R65Cell8.setCellValue("");
						R65Cell8.setCellStyle(textStyle);
					}

// R65 Col L
					Cell R65Cell9 = row.createCell(11);
					if (record.getR65_qua_iv_lc() != null) {
						R65Cell9.setCellValue(record.getR65_qua_iv_lc().doubleValue());
						R65Cell9.setCellStyle(numberStyle);
					} else {
						R65Cell9.setCellValue("");
						R65Cell9.setCellStyle(textStyle);
					}

// R65 Col M
					Cell R65Cell10 = row.createCell(12);
					if (record.getR65_qua_iv_qar() != null) {
						R65Cell10.setCellValue(record.getR65_qua_iv_qar().doubleValue());
						R65Cell10.setCellStyle(numberStyle);
					} else {
						R65Cell10.setCellValue("");
						R65Cell10.setCellStyle(textStyle);
					}

// R65 Col N
					Cell R65Cell11 = row.createCell(13);
					if (record.getR65_qua_iv_inr() != null) {
						R65Cell11.setCellValue(record.getR65_qua_iv_inr().doubleValue());
						R65Cell11.setCellStyle(numberStyle);
					} else {
						R65Cell11.setCellValue("");
						R65Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(66);
					Cell R67Cell = row.createCell(2);
					if (record.getR67_qua_i_lc() != null) {
						R67Cell.setCellValue(record.getR67_qua_i_lc().doubleValue());
						R67Cell.setCellStyle(numberStyle);
					} else {
						R67Cell.setCellValue("");
						R67Cell.setCellStyle(textStyle);
					}

					Cell R67Cell1 = row.createCell(3);
					if (record.getR67_qua_i_qar() != null) {
						R67Cell1.setCellValue(record.getR67_qua_i_qar().doubleValue());
						R67Cell1.setCellStyle(numberStyle);
					} else {
						R67Cell1.setCellValue("");
						R67Cell1.setCellStyle(textStyle);
					}

					// R67 Col E
					Cell R67Cell2 = row.createCell(4);
					if (record.getR67_qua_i_inr() != null) {
						R67Cell2.setCellValue(record.getR67_qua_i_inr().doubleValue());
						R67Cell2.setCellStyle(numberStyle);
					} else {
						R67Cell2.setCellValue("");
						R67Cell2.setCellStyle(textStyle);
					}

					// R67 Col F
					Cell R67Cell3 = row.createCell(5);
					if (record.getR67_qua_ii_lc() != null) {
						R67Cell3.setCellValue(record.getR67_qua_ii_lc().doubleValue());
						R67Cell3.setCellStyle(numberStyle);
					} else {
						R67Cell3.setCellValue("");
						R67Cell3.setCellStyle(textStyle);
					}
					// R67 Col G
					Cell R67Cell4 = row.createCell(6);
					if (record.getR67_qua_ii_qar() != null) {
						R67Cell4.setCellValue(record.getR67_qua_ii_qar().doubleValue());
						R67Cell4.setCellStyle(numberStyle);
					} else {
						R67Cell4.setCellValue("");
						R67Cell4.setCellStyle(textStyle);
					}
					// R67 Col H
					Cell R67Cell5 = row.createCell(7);
					if (record.getR67_qua_ii_inr() != null) {
						R67Cell5.setCellValue(record.getR67_qua_ii_inr().doubleValue());
						R67Cell5.setCellStyle(numberStyle);
					} else {
						R67Cell5.setCellValue("");
						R67Cell5.setCellStyle(textStyle);
					}
					// R67 Col I
					Cell R67Cell6 = row.createCell(8);
					if (record.getR67_qua_iii_lc() != null) {
						R67Cell6.setCellValue(record.getR67_qua_iii_lc().doubleValue());
						R67Cell6.setCellStyle(numberStyle);
					} else {
						R67Cell6.setCellValue("");
						R67Cell6.setCellStyle(textStyle);
					}
					// R67 Col J
					Cell R67Cell7 = row.createCell(9);
					if (record.getR67_qua_iii_qar() != null) {
						R67Cell7.setCellValue(record.getR67_qua_iii_qar().doubleValue());
						R67Cell7.setCellStyle(numberStyle);
					} else {
						R67Cell7.setCellValue("");
						R67Cell7.setCellStyle(textStyle);
					}
					// R67 Col K
					Cell R67Cell8 = row.createCell(10);
					if (record.getR67_qua_iii_inr() != null) {
						R67Cell8.setCellValue(record.getR67_qua_iii_inr().doubleValue());
						R67Cell8.setCellStyle(numberStyle);
					} else {
						R67Cell8.setCellValue("");
						R67Cell8.setCellStyle(textStyle);
					}
					// R67 Col L
					Cell R67Cell9 = row.createCell(11);
					if (record.getR67_qua_iv_lc() != null) {
						R67Cell9.setCellValue(record.getR67_qua_iv_lc().doubleValue());
						R67Cell9.setCellStyle(numberStyle);
					} else {
						R67Cell9.setCellValue("");
						R67Cell9.setCellStyle(textStyle);
					}
					// R67 Col M
					Cell R67Cell10 = row.createCell(12);
					if (record.getR67_qua_iv_qar() != null) {
						R67Cell10.setCellValue(record.getR67_qua_iv_qar().doubleValue());
						R67Cell10.setCellStyle(numberStyle);
					} else {
						R67Cell10.setCellValue("");
						R67Cell10.setCellStyle(textStyle);
					}

					// R67 Col N
					Cell R67Cell11 = row.createCell(13);
					if (record.getR67_qua_iv_inr() != null) {
						R67Cell11.setCellValue(record.getR67_qua_iv_inr().doubleValue());
						R67Cell11.setCellStyle(numberStyle);
					} else {
						R67Cell11.setCellValue("");
						R67Cell11.setCellStyle(textStyle);
					}
					// R67 Col O
					Cell R67Cell12 = row.createCell(14);
					if (record.getR67_cumm_inr() != null) {
						R67Cell12.setCellValue(record.getR67_cumm_inr().doubleValue());
						R67Cell12.setCellStyle(numberStyle);
					} else {
						R67Cell12.setCellValue("");
						R67Cell12.setCellStyle(textStyle);
					}

					row = sheet.getRow(67);
					Cell R68Cell = row.createCell(2);
					if (record.getR68_qua_i_lc() != null) {
						R68Cell.setCellValue(record.getR68_qua_i_lc().doubleValue());
						R68Cell.setCellStyle(numberStyle);
					} else {
						R68Cell.setCellValue("");
						R68Cell.setCellStyle(textStyle);
					}

					Cell R68Cell1 = row.createCell(3);
					if (record.getR68_qua_i_qar() != null) {
						R68Cell1.setCellValue(record.getR68_qua_i_qar().doubleValue());
						R68Cell1.setCellStyle(numberStyle);
					} else {
						R68Cell1.setCellValue("");
						R68Cell1.setCellStyle(textStyle);
					}

					// R68 Col E
					Cell R68Cell2 = row.createCell(4);
					if (record.getR68_qua_i_inr() != null) {
						R68Cell2.setCellValue(record.getR68_qua_i_inr().doubleValue());
						R68Cell2.setCellStyle(numberStyle);
					} else {
						R68Cell2.setCellValue("");
						R68Cell2.setCellStyle(textStyle);
					}

					// R68 Col F
					Cell R68Cell3 = row.createCell(5);
					if (record.getR68_qua_ii_lc() != null) {
						R68Cell3.setCellValue(record.getR68_qua_ii_lc().doubleValue());
						R68Cell3.setCellStyle(numberStyle);
					} else {
						R68Cell3.setCellValue("");
						R68Cell3.setCellStyle(textStyle);
					}
					// R68 Col G
					Cell R68Cell4 = row.createCell(6);
					if (record.getR68_qua_ii_qar() != null) {
						R68Cell4.setCellValue(record.getR68_qua_ii_qar().doubleValue());
						R68Cell4.setCellStyle(numberStyle);
					} else {
						R68Cell4.setCellValue("");
						R68Cell4.setCellStyle(textStyle);
					}
					// R68 Col H
					Cell R68Cell5 = row.createCell(7);
					if (record.getR68_qua_ii_inr() != null) {
						R68Cell5.setCellValue(record.getR68_qua_ii_inr().doubleValue());
						R68Cell5.setCellStyle(numberStyle);
					} else {
						R68Cell5.setCellValue("");
						R68Cell5.setCellStyle(textStyle);
					}
					// R68 Col I
					Cell R68Cell6 = row.createCell(8);
					if (record.getR68_qua_iii_lc() != null) {
						R68Cell6.setCellValue(record.getR68_qua_iii_lc().doubleValue());
						R68Cell6.setCellStyle(numberStyle);
					} else {
						R68Cell6.setCellValue("");
						R68Cell6.setCellStyle(textStyle);
					}
					// R68 Col J
					Cell R68Cell7 = row.createCell(9);
					if (record.getR68_qua_iii_qar() != null) {
						R68Cell7.setCellValue(record.getR68_qua_iii_qar().doubleValue());
						R68Cell7.setCellStyle(numberStyle);
					} else {
						R68Cell7.setCellValue("");
						R68Cell7.setCellStyle(textStyle);
					}
					// R68 Col K
					Cell R68Cell8 = row.createCell(10);
					if (record.getR68_qua_iii_inr() != null) {
						R68Cell8.setCellValue(record.getR68_qua_iii_inr().doubleValue());
						R68Cell8.setCellStyle(numberStyle);
					} else {
						R68Cell8.setCellValue("");
						R68Cell8.setCellStyle(textStyle);
					}
					// R68 Col L
					Cell R68Cell9 = row.createCell(11);
					if (record.getR68_qua_iv_lc() != null) {
						R68Cell9.setCellValue(record.getR68_qua_iv_lc().doubleValue());
						R68Cell9.setCellStyle(numberStyle);
					} else {
						R68Cell9.setCellValue("");
						R68Cell9.setCellStyle(textStyle);
					}
					// R68 Col M
					Cell R68Cell10 = row.createCell(12);
					if (record.getR68_qua_iv_qar() != null) {
						R68Cell10.setCellValue(record.getR68_qua_iv_qar().doubleValue());
						R68Cell10.setCellStyle(numberStyle);
					} else {
						R68Cell10.setCellValue("");
						R68Cell10.setCellStyle(textStyle);
					}

					// R68 Col N
					Cell R68Cell11 = row.createCell(13);
					if (record.getR68_qua_iv_inr() != null) {
						R68Cell11.setCellValue(record.getR68_qua_iv_inr().doubleValue());
						R68Cell11.setCellStyle(numberStyle);
					} else {
						R68Cell11.setCellValue("");
						R68Cell11.setCellStyle(textStyle);
					}
					// R68 Col O
					Cell R68Cell12 = row.createCell(14);
					if (record.getR68_cumm_inr() != null) {
						R68Cell12.setCellValue(record.getR68_cumm_inr().doubleValue());
						R68Cell12.setCellStyle(numberStyle);
					} else {
						R68Cell12.setCellValue("");
						R68Cell12.setCellStyle(textStyle);
					}

					row = sheet.getRow(68);
					Cell R69Cell = row.createCell(2);
					if (record.getR69_qua_i_lc() != null) {
						R69Cell.setCellValue(record.getR69_qua_i_lc().doubleValue());
						R69Cell.setCellStyle(numberStyle);
					} else {
						R69Cell.setCellValue("");
						R69Cell.setCellStyle(textStyle);
					}

					Cell R69Cell1 = row.createCell(3);
					if (record.getR69_qua_i_qar() != null) {
						R69Cell1.setCellValue(record.getR69_qua_i_qar().doubleValue());
						R69Cell1.setCellStyle(numberStyle);
					} else {
						R69Cell1.setCellValue("");
						R69Cell1.setCellStyle(textStyle);
					}

					// R69 Col E
					Cell R69Cell2 = row.createCell(4);
					if (record.getR69_qua_i_inr() != null) {
						R69Cell2.setCellValue(record.getR69_qua_i_inr().doubleValue());
						R69Cell2.setCellStyle(numberStyle);
					} else {
						R69Cell2.setCellValue("");
						R69Cell2.setCellStyle(textStyle);
					}

					// R69 Col F
					Cell R69Cell3 = row.createCell(5);
					if (record.getR69_qua_ii_lc() != null) {
						R69Cell3.setCellValue(record.getR69_qua_ii_lc().doubleValue());
						R69Cell3.setCellStyle(numberStyle);
					} else {
						R69Cell3.setCellValue("");
						R69Cell3.setCellStyle(textStyle);
					}
					// R69 Col G
					Cell R69Cell4 = row.createCell(6);
					if (record.getR69_qua_ii_qar() != null) {
						R69Cell4.setCellValue(record.getR69_qua_ii_qar().doubleValue());
						R69Cell4.setCellStyle(numberStyle);
					} else {
						R69Cell4.setCellValue("");
						R69Cell4.setCellStyle(textStyle);
					}
					// R69 Col H
					Cell R69Cell5 = row.createCell(7);
					if (record.getR69_qua_ii_inr() != null) {
						R69Cell5.setCellValue(record.getR69_qua_ii_inr().doubleValue());
						R69Cell5.setCellStyle(numberStyle);
					} else {
						R69Cell5.setCellValue("");
						R69Cell5.setCellStyle(textStyle);
					}
					// R69 Col I
					Cell R69Cell6 = row.createCell(8);
					if (record.getR69_qua_iii_lc() != null) {
						R69Cell6.setCellValue(record.getR69_qua_iii_lc().doubleValue());
						R69Cell6.setCellStyle(numberStyle);
					} else {
						R69Cell6.setCellValue("");
						R69Cell6.setCellStyle(textStyle);
					}
					// R69 Col J
					Cell R69Cell7 = row.createCell(9);
					if (record.getR69_qua_iii_qar() != null) {
						R69Cell7.setCellValue(record.getR69_qua_iii_qar().doubleValue());
						R69Cell7.setCellStyle(numberStyle);
					} else {
						R69Cell7.setCellValue("");
						R69Cell7.setCellStyle(textStyle);
					}
					// R69 Col K
					Cell R69Cell8 = row.createCell(10);
					if (record.getR69_qua_iii_inr() != null) {
						R69Cell8.setCellValue(record.getR69_qua_iii_inr().doubleValue());
						R69Cell8.setCellStyle(numberStyle);
					} else {
						R69Cell8.setCellValue("");
						R69Cell8.setCellStyle(textStyle);
					}
					// R69 Col L
					Cell R69Cell9 = row.createCell(11);
					if (record.getR69_qua_iv_lc() != null) {
						R69Cell9.setCellValue(record.getR69_qua_iv_lc().doubleValue());
						R69Cell9.setCellStyle(numberStyle);
					} else {
						R69Cell9.setCellValue("");
						R69Cell9.setCellStyle(textStyle);
					}
					// R69 Col M
					Cell R69Cell10 = row.createCell(12);
					if (record.getR69_qua_iv_qar() != null) {
						R69Cell10.setCellValue(record.getR69_qua_iv_qar().doubleValue());
						R69Cell10.setCellStyle(numberStyle);
					} else {
						R69Cell10.setCellValue("");
						R69Cell10.setCellStyle(textStyle);
					}

					// R69 Col N
					Cell R69Cell11 = row.createCell(13);
					if (record.getR69_qua_iv_inr() != null) {
						R69Cell11.setCellValue(record.getR69_qua_iv_inr().doubleValue());
						R69Cell11.setCellStyle(numberStyle);
					} else {
						R69Cell11.setCellValue("");
						R69Cell11.setCellStyle(textStyle);
					}

					// R69 Col O
					Cell R69Cell12 = row.createCell(14);
					if (record.getR69_cumm_inr() != null) {
						R69Cell12.setCellValue(record.getR69_cumm_inr().doubleValue());
						R69Cell12.setCellStyle(numberStyle);
					} else {
						R69Cell12.setCellValue("");
						R69Cell12.setCellStyle(textStyle);
					}

					row = sheet.getRow(69);
					Cell R70Cell = row.createCell(2);
					if (record.getR70_qua_i_lc() != null) {
						R70Cell.setCellValue(record.getR70_qua_i_lc().doubleValue());
						R70Cell.setCellStyle(numberStyle);
					} else {
						R70Cell.setCellValue("");
						R70Cell.setCellStyle(textStyle);
					}

					Cell R70Cell1 = row.createCell(3);
					if (record.getR70_qua_i_qar() != null) {
						R70Cell1.setCellValue(record.getR70_qua_i_qar().doubleValue());
						R70Cell1.setCellStyle(numberStyle);
					} else {
						R70Cell1.setCellValue("");
						R70Cell1.setCellStyle(textStyle);
					}

					// R70 Col E
					Cell R70Cell2 = row.createCell(4);
					if (record.getR70_qua_i_inr() != null) {
						R70Cell2.setCellValue(record.getR70_qua_i_inr().doubleValue());
						R70Cell2.setCellStyle(numberStyle);
					} else {
						R70Cell2.setCellValue("");
						R70Cell2.setCellStyle(textStyle);
					}

					// R70 Col F
					Cell R70Cell3 = row.createCell(5);
					if (record.getR70_qua_ii_lc() != null) {
						R70Cell3.setCellValue(record.getR70_qua_ii_lc().doubleValue());
						R70Cell3.setCellStyle(numberStyle);
					} else {
						R70Cell3.setCellValue("");
						R70Cell3.setCellStyle(textStyle);
					}
					// R70 Col G
					Cell R70Cell4 = row.createCell(6);
					if (record.getR70_qua_ii_qar() != null) {
						R70Cell4.setCellValue(record.getR70_qua_ii_qar().doubleValue());
						R70Cell4.setCellStyle(numberStyle);
					} else {
						R70Cell4.setCellValue("");
						R70Cell4.setCellStyle(textStyle);
					}
					// R70 Col H
					Cell R70Cell5 = row.createCell(7);
					if (record.getR70_qua_ii_inr() != null) {
						R70Cell5.setCellValue(record.getR70_qua_ii_inr().doubleValue());
						R70Cell5.setCellStyle(numberStyle);
					} else {
						R70Cell5.setCellValue("");
						R70Cell5.setCellStyle(textStyle);
					}
					// R70 Col I
					Cell R70Cell6 = row.createCell(8);
					if (record.getR70_qua_iii_lc() != null) {
						R70Cell6.setCellValue(record.getR70_qua_iii_lc().doubleValue());
						R70Cell6.setCellStyle(numberStyle);
					} else {
						R70Cell6.setCellValue("");
						R70Cell6.setCellStyle(textStyle);
					}
					// R70 Col J
					Cell R70Cell7 = row.createCell(9);
					if (record.getR70_qua_iii_qar() != null) {
						R70Cell7.setCellValue(record.getR70_qua_iii_qar().doubleValue());
						R70Cell7.setCellStyle(numberStyle);
					} else {
						R70Cell7.setCellValue("");
						R70Cell7.setCellStyle(textStyle);
					}
					// R70 Col K
					Cell R70Cell8 = row.createCell(10);
					if (record.getR70_qua_iii_inr() != null) {
						R70Cell8.setCellValue(record.getR70_qua_iii_inr().doubleValue());
						R70Cell8.setCellStyle(numberStyle);
					} else {
						R70Cell8.setCellValue("");
						R70Cell8.setCellStyle(textStyle);
					}
					// R70 Col L
					Cell R70Cell9 = row.createCell(11);
					if (record.getR70_qua_iv_lc() != null) {
						R70Cell9.setCellValue(record.getR70_qua_iv_lc().doubleValue());
						R70Cell9.setCellStyle(numberStyle);
					} else {
						R70Cell9.setCellValue("");
						R70Cell9.setCellStyle(textStyle);
					}
					// R70 Col M
					Cell R70Cell10 = row.createCell(12);
					if (record.getR70_qua_iv_qar() != null) {
						R70Cell10.setCellValue(record.getR70_qua_iv_qar().doubleValue());
						R70Cell10.setCellStyle(numberStyle);
					} else {
						R70Cell10.setCellValue("");
						R70Cell10.setCellStyle(textStyle);
					}

					// R70 Col N
					Cell R70Cell11 = row.createCell(13);
					if (record.getR70_qua_iv_inr() != null) {
						R70Cell11.setCellValue(record.getR70_qua_iv_inr().doubleValue());
						R70Cell11.setCellStyle(numberStyle);
					} else {
						R70Cell11.setCellValue("");
						R70Cell11.setCellStyle(textStyle);
					}

					// R70 Col O
					Cell R70Cell12 = row.createCell(14);
					if (record.getR70_cumm_inr() != null) {
						R70Cell12.setCellValue(record.getR70_cumm_inr().doubleValue());
						R70Cell12.setCellStyle(numberStyle);
					} else {
						R70Cell12.setCellValue("");
						R70Cell12.setCellStyle(textStyle);
					}
					row = sheet.getRow(70);
					// R71 Col C
					Cell R71Cell = row.createCell(2);
					if (record.getR71_qua_i_lc() != null) {
						R71Cell.setCellValue(record.getR71_qua_i_lc().doubleValue());
						R71Cell.setCellStyle(numberStyle);
					} else {
						R71Cell.setCellValue("");
						R71Cell.setCellStyle(textStyle);
					}

// R71 Col D
					Cell R71Cell1 = row.createCell(3);
					if (record.getR71_qua_i_qar() != null) {
						R71Cell1.setCellValue(record.getR71_qua_i_qar().doubleValue());
						R71Cell1.setCellStyle(numberStyle);
					} else {
						R71Cell1.setCellValue("");
						R71Cell1.setCellStyle(textStyle);
					}

// R71 Col E
					Cell R71Cell2 = row.createCell(4);
					if (record.getR71_qua_i_inr() != null) {
						R71Cell2.setCellValue(record.getR71_qua_i_inr().doubleValue());
						R71Cell2.setCellStyle(numberStyle);
					} else {
						R71Cell2.setCellValue("");
						R71Cell2.setCellStyle(textStyle);
					}

// R71 Col F
					Cell R71Cell3 = row.createCell(5);
					if (record.getR71_qua_ii_lc() != null) {
						R71Cell3.setCellValue(record.getR71_qua_ii_lc().doubleValue());
						R71Cell3.setCellStyle(numberStyle);
					} else {
						R71Cell3.setCellValue("");
						R71Cell3.setCellStyle(textStyle);
					}

// R71 Col G
					Cell R71Cell4 = row.createCell(6);
					if (record.getR71_qua_ii_qar() != null) {
						R71Cell4.setCellValue(record.getR71_qua_ii_qar().doubleValue());
						R71Cell4.setCellStyle(numberStyle);
					} else {
						R71Cell4.setCellValue("");
						R71Cell4.setCellStyle(textStyle);
					}

// R71 Col H
					Cell R71Cell5 = row.createCell(7);
					if (record.getR71_qua_ii_inr() != null) {
						R71Cell5.setCellValue(record.getR71_qua_ii_inr().doubleValue());
						R71Cell5.setCellStyle(numberStyle);
					} else {
						R71Cell5.setCellValue("");
						R71Cell5.setCellStyle(textStyle);
					}

// R71 Col I
					Cell R71Cell6 = row.createCell(8);
					if (record.getR71_qua_iii_lc() != null) {
						R71Cell6.setCellValue(record.getR71_qua_iii_lc().doubleValue());
						R71Cell6.setCellStyle(numberStyle);
					} else {
						R71Cell6.setCellValue("");
						R71Cell6.setCellStyle(textStyle);
					}

// R71 Col J
					Cell R71Cell7 = row.createCell(9);
					if (record.getR71_qua_iii_qar() != null) {
						R71Cell7.setCellValue(record.getR71_qua_iii_qar().doubleValue());
						R71Cell7.setCellStyle(numberStyle);
					} else {
						R71Cell7.setCellValue("");
						R71Cell7.setCellStyle(textStyle);
					}

// R71 Col K
					Cell R71Cell8 = row.createCell(10);
					if (record.getR71_qua_iii_inr() != null) {
						R71Cell8.setCellValue(record.getR71_qua_iii_inr().doubleValue());
						R71Cell8.setCellStyle(numberStyle);
					} else {
						R71Cell8.setCellValue("");
						R71Cell8.setCellStyle(textStyle);
					}

// R71 Col L
					Cell R71Cell9 = row.createCell(11);
					if (record.getR71_qua_iv_lc() != null) {
						R71Cell9.setCellValue(record.getR71_qua_iv_lc().doubleValue());
						R71Cell9.setCellStyle(numberStyle);
					} else {
						R71Cell9.setCellValue("");
						R71Cell9.setCellStyle(textStyle);
					}

// R71 Col M
					Cell R71Cell10 = row.createCell(12);
					if (record.getR71_qua_iv_qar() != null) {
						R71Cell10.setCellValue(record.getR71_qua_iv_qar().doubleValue());
						R71Cell10.setCellStyle(numberStyle);
					} else {
						R71Cell10.setCellValue("");
						R71Cell10.setCellStyle(textStyle);
					}

// R71 Col N
					Cell R71Cell11 = row.createCell(13);
					if (record.getR71_qua_iv_inr() != null) {
						R71Cell11.setCellValue(record.getR71_qua_iv_inr().doubleValue());
						R71Cell11.setCellStyle(numberStyle);
					} else {
						R71Cell11.setCellValue("");
						R71Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(71);
// R72 Col C
					Cell R72Cell = row.createCell(2);
					if (record.getR72_qua_i_lc() != null) {
						R72Cell.setCellValue(record.getR72_qua_i_lc().doubleValue());
						R72Cell.setCellStyle(numberStyle);
					} else {
						R72Cell.setCellValue("");
						R72Cell.setCellStyle(textStyle);
					}

// R72 Col D
					Cell R72Cell1 = row.createCell(3);
					if (record.getR72_qua_i_qar() != null) {
						R72Cell1.setCellValue(record.getR72_qua_i_qar().doubleValue());
						R72Cell1.setCellStyle(numberStyle);
					} else {
						R72Cell1.setCellValue("");
						R72Cell1.setCellStyle(textStyle);
					}

// R72 Col E
					Cell R72Cell2 = row.createCell(4);
					if (record.getR72_qua_i_inr() != null) {
						R72Cell2.setCellValue(record.getR72_qua_i_inr().doubleValue());
						R72Cell2.setCellStyle(numberStyle);
					} else {
						R72Cell2.setCellValue("");
						R72Cell2.setCellStyle(textStyle);
					}

// R72 Col F
					Cell R72Cell3 = row.createCell(5);
					if (record.getR72_qua_ii_lc() != null) {
						R72Cell3.setCellValue(record.getR72_qua_ii_lc().doubleValue());
						R72Cell3.setCellStyle(numberStyle);
					} else {
						R72Cell3.setCellValue("");
						R72Cell3.setCellStyle(textStyle);
					}

// R72 Col G
					Cell R72Cell4 = row.createCell(6);
					if (record.getR72_qua_ii_qar() != null) {
						R72Cell4.setCellValue(record.getR72_qua_ii_qar().doubleValue());
						R72Cell4.setCellStyle(numberStyle);
					} else {
						R72Cell4.setCellValue("");
						R72Cell4.setCellStyle(textStyle);
					}

// R72 Col H
					Cell R72Cell5 = row.createCell(7);
					if (record.getR72_qua_ii_inr() != null) {
						R72Cell5.setCellValue(record.getR72_qua_ii_inr().doubleValue());
						R72Cell5.setCellStyle(numberStyle);
					} else {
						R72Cell5.setCellValue("");
						R72Cell5.setCellStyle(textStyle);
					}

// R72 Col I
					Cell R72Cell6 = row.createCell(8);
					if (record.getR72_qua_iii_lc() != null) {
						R72Cell6.setCellValue(record.getR72_qua_iii_lc().doubleValue());
						R72Cell6.setCellStyle(numberStyle);
					} else {
						R72Cell6.setCellValue("");
						R72Cell6.setCellStyle(textStyle);
					}

// R72 Col J
					Cell R72Cell7 = row.createCell(9);
					if (record.getR72_qua_iii_qar() != null) {
						R72Cell7.setCellValue(record.getR72_qua_iii_qar().doubleValue());
						R72Cell7.setCellStyle(numberStyle);
					} else {
						R72Cell7.setCellValue("");
						R72Cell7.setCellStyle(textStyle);
					}

// R72 Col K
					Cell R72Cell8 = row.createCell(10);
					if (record.getR72_qua_iii_inr() != null) {
						R72Cell8.setCellValue(record.getR72_qua_iii_inr().doubleValue());
						R72Cell8.setCellStyle(numberStyle);
					} else {
						R72Cell8.setCellValue("");
						R72Cell8.setCellStyle(textStyle);
					}

// R72 Col L
					Cell R72Cell9 = row.createCell(11);
					if (record.getR72_qua_iv_lc() != null) {
						R72Cell9.setCellValue(record.getR72_qua_iv_lc().doubleValue());
						R72Cell9.setCellStyle(numberStyle);
					} else {
						R72Cell9.setCellValue("");
						R72Cell9.setCellStyle(textStyle);
					}

// R72 Col M
					Cell R72Cell10 = row.createCell(12);
					if (record.getR72_qua_iv_qar() != null) {
						R72Cell10.setCellValue(record.getR72_qua_iv_qar().doubleValue());
						R72Cell10.setCellStyle(numberStyle);
					} else {
						R72Cell10.setCellValue("");
						R72Cell10.setCellStyle(textStyle);
					}

// R72 Col N
					Cell R72Cell11 = row.createCell(13);
					if (record.getR72_qua_iv_inr() != null) {
						R72Cell11.setCellValue(record.getR72_qua_iv_inr().doubleValue());
						R72Cell11.setCellStyle(numberStyle);
					} else {
						R72Cell11.setCellValue("");
						R72Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(72);
// R73 Col C
					Cell R73Cell = row.createCell(2);
					if (record.getR73_qua_i_lc() != null) {
						R73Cell.setCellValue(record.getR73_qua_i_lc().doubleValue());
						R73Cell.setCellStyle(numberStyle);
					} else {
						R73Cell.setCellValue("");
						R73Cell.setCellStyle(textStyle);
					}

// R73 Col D
					Cell R73Cell1 = row.createCell(3);
					if (record.getR73_qua_i_qar() != null) {
						R73Cell1.setCellValue(record.getR73_qua_i_qar().doubleValue());
						R73Cell1.setCellStyle(numberStyle);
					} else {
						R73Cell1.setCellValue("");
						R73Cell1.setCellStyle(textStyle);
					}

// R73 Col E
					Cell R73Cell2 = row.createCell(4);
					if (record.getR73_qua_i_inr() != null) {
						R73Cell2.setCellValue(record.getR73_qua_i_inr().doubleValue());
						R73Cell2.setCellStyle(numberStyle);
					} else {
						R73Cell2.setCellValue("");
						R73Cell2.setCellStyle(textStyle);
					}

// R73 Col F
					Cell R73Cell3 = row.createCell(5);
					if (record.getR73_qua_ii_lc() != null) {
						R73Cell3.setCellValue(record.getR73_qua_ii_lc().doubleValue());
						R73Cell3.setCellStyle(numberStyle);
					} else {
						R73Cell3.setCellValue("");
						R73Cell3.setCellStyle(textStyle);
					}

// R73 Col G
					Cell R73Cell4 = row.createCell(6);
					if (record.getR73_qua_ii_qar() != null) {
						R73Cell4.setCellValue(record.getR73_qua_ii_qar().doubleValue());
						R73Cell4.setCellStyle(numberStyle);
					} else {
						R73Cell4.setCellValue("");
						R73Cell4.setCellStyle(textStyle);
					}

// R73 Col H
					Cell R73Cell5 = row.createCell(7);
					if (record.getR73_qua_ii_inr() != null) {
						R73Cell5.setCellValue(record.getR73_qua_ii_inr().doubleValue());
						R73Cell5.setCellStyle(numberStyle);
					} else {
						R73Cell5.setCellValue("");
						R73Cell5.setCellStyle(textStyle);
					}

// R73 Col I
					Cell R73Cell6 = row.createCell(8);
					if (record.getR73_qua_iii_lc() != null) {
						R73Cell6.setCellValue(record.getR73_qua_iii_lc().doubleValue());
						R73Cell6.setCellStyle(numberStyle);
					} else {
						R73Cell6.setCellValue("");
						R73Cell6.setCellStyle(textStyle);
					}

// R73 Col J
					Cell R73Cell7 = row.createCell(9);
					if (record.getR73_qua_iii_qar() != null) {
						R73Cell7.setCellValue(record.getR73_qua_iii_qar().doubleValue());
						R73Cell7.setCellStyle(numberStyle);
					} else {
						R73Cell7.setCellValue("");
						R73Cell7.setCellStyle(textStyle);
					}

// R73 Col K
					Cell R73Cell8 = row.createCell(10);
					if (record.getR73_qua_iii_inr() != null) {
						R73Cell8.setCellValue(record.getR73_qua_iii_inr().doubleValue());
						R73Cell8.setCellStyle(numberStyle);
					} else {
						R73Cell8.setCellValue("");
						R73Cell8.setCellStyle(textStyle);
					}

// R73 Col L
					Cell R73Cell9 = row.createCell(11);
					if (record.getR73_qua_iv_lc() != null) {
						R73Cell9.setCellValue(record.getR73_qua_iv_lc().doubleValue());
						R73Cell9.setCellStyle(numberStyle);
					} else {
						R73Cell9.setCellValue("");
						R73Cell9.setCellStyle(textStyle);
					}

// R73 Col M
					Cell R73Cell10 = row.createCell(12);
					if (record.getR73_qua_iv_qar() != null) {
						R73Cell10.setCellValue(record.getR73_qua_iv_qar().doubleValue());
						R73Cell10.setCellStyle(numberStyle);
					} else {
						R73Cell10.setCellValue("");
						R73Cell10.setCellStyle(textStyle);
					}

// R73 Col N
					Cell R73Cell11 = row.createCell(13);
					if (record.getR73_qua_iv_inr() != null) {
						R73Cell11.setCellValue(record.getR73_qua_iv_inr().doubleValue());
						R73Cell11.setCellStyle(numberStyle);
					} else {
						R73Cell11.setCellValue("");
						R73Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(73);
// R74 Col C
					Cell R74Cell = row.createCell(2);
					if (record.getR74_qua_i_lc() != null) {
						R74Cell.setCellValue(record.getR74_qua_i_lc().doubleValue());
						R74Cell.setCellStyle(numberStyle);
					} else {
						R74Cell.setCellValue("");
						R74Cell.setCellStyle(textStyle);
					}

// R74 Col D
					Cell R74Cell1 = row.createCell(3);
					if (record.getR74_qua_i_qar() != null) {
						R74Cell1.setCellValue(record.getR74_qua_i_qar().doubleValue());
						R74Cell1.setCellStyle(numberStyle);
					} else {
						R74Cell1.setCellValue("");
						R74Cell1.setCellStyle(textStyle);
					}

// R74 Col E
					Cell R74Cell2 = row.createCell(4);
					if (record.getR74_qua_i_inr() != null) {
						R74Cell2.setCellValue(record.getR74_qua_i_inr().doubleValue());
						R74Cell2.setCellStyle(numberStyle);
					} else {
						R74Cell2.setCellValue("");
						R74Cell2.setCellStyle(textStyle);
					}

// R74 Col F
					Cell R74Cell3 = row.createCell(5);
					if (record.getR74_qua_ii_lc() != null) {
						R74Cell3.setCellValue(record.getR74_qua_ii_lc().doubleValue());
						R74Cell3.setCellStyle(numberStyle);
					} else {
						R74Cell3.setCellValue("");
						R74Cell3.setCellStyle(textStyle);
					}

// R74 Col G
					Cell R74Cell4 = row.createCell(6);
					if (record.getR74_qua_ii_qar() != null) {
						R74Cell4.setCellValue(record.getR74_qua_ii_qar().doubleValue());
						R74Cell4.setCellStyle(numberStyle);
					} else {
						R74Cell4.setCellValue("");
						R74Cell4.setCellStyle(textStyle);
					}

// R74 Col H
					Cell R74Cell5 = row.createCell(7);
					if (record.getR74_qua_ii_inr() != null) {
						R74Cell5.setCellValue(record.getR74_qua_ii_inr().doubleValue());
						R74Cell5.setCellStyle(numberStyle);
					} else {
						R74Cell5.setCellValue("");
						R74Cell5.setCellStyle(textStyle);
					}

// R74 Col I
					Cell R74Cell6 = row.createCell(8);
					if (record.getR74_qua_iii_lc() != null) {
						R74Cell6.setCellValue(record.getR74_qua_iii_lc().doubleValue());
						R74Cell6.setCellStyle(numberStyle);
					} else {
						R74Cell6.setCellValue("");
						R74Cell6.setCellStyle(textStyle);
					}

// R74 Col J
					Cell R74Cell7 = row.createCell(9);
					if (record.getR74_qua_iii_qar() != null) {
						R74Cell7.setCellValue(record.getR74_qua_iii_qar().doubleValue());
						R74Cell7.setCellStyle(numberStyle);
					} else {
						R74Cell7.setCellValue("");
						R74Cell7.setCellStyle(textStyle);
					}

// R74 Col K
					Cell R74Cell8 = row.createCell(10);
					if (record.getR74_qua_iii_inr() != null) {
						R74Cell8.setCellValue(record.getR74_qua_iii_inr().doubleValue());
						R74Cell8.setCellStyle(numberStyle);
					} else {
						R74Cell8.setCellValue("");
						R74Cell8.setCellStyle(textStyle);
					}

// R74 Col L
					Cell R74Cell9 = row.createCell(11);
					if (record.getR74_qua_iv_lc() != null) {
						R74Cell9.setCellValue(record.getR74_qua_iv_lc().doubleValue());
						R74Cell9.setCellStyle(numberStyle);
					} else {
						R74Cell9.setCellValue("");
						R74Cell9.setCellStyle(textStyle);
					}

// R74 Col M
					Cell R74Cell10 = row.createCell(12);
					if (record.getR74_qua_iv_qar() != null) {
						R74Cell10.setCellValue(record.getR74_qua_iv_qar().doubleValue());
						R74Cell10.setCellStyle(numberStyle);
					} else {
						R74Cell10.setCellValue("");
						R74Cell10.setCellStyle(textStyle);
					}

// R74 Col N
					Cell R74Cell11 = row.createCell(13);
					if (record.getR74_qua_iv_inr() != null) {
						R74Cell11.setCellValue(record.getR74_qua_iv_inr().doubleValue());
						R74Cell11.setCellStyle(numberStyle);
					} else {
						R74Cell11.setCellValue("");
						R74Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(74);
// R75 Col C
					Cell R75Cell = row.createCell(2);
					if (record.getR75_qua_i_lc() != null) {
						R75Cell.setCellValue(record.getR75_qua_i_lc().doubleValue());
						R75Cell.setCellStyle(numberStyle);
					} else {
						R75Cell.setCellValue("");
						R75Cell.setCellStyle(textStyle);
					}

// R75 Col D
					Cell R75Cell1 = row.createCell(3);
					if (record.getR75_qua_i_qar() != null) {
						R75Cell1.setCellValue(record.getR75_qua_i_qar().doubleValue());
						R75Cell1.setCellStyle(numberStyle);
					} else {
						R75Cell1.setCellValue("");
						R75Cell1.setCellStyle(textStyle);
					}

// R75 Col E
					Cell R75Cell2 = row.createCell(4);
					if (record.getR75_qua_i_inr() != null) {
						R75Cell2.setCellValue(record.getR75_qua_i_inr().doubleValue());
						R75Cell2.setCellStyle(numberStyle);
					} else {
						R75Cell2.setCellValue("");
						R75Cell2.setCellStyle(textStyle);
					}

// R75 Col F
					Cell R75Cell3 = row.createCell(5);
					if (record.getR75_qua_ii_lc() != null) {
						R75Cell3.setCellValue(record.getR75_qua_ii_lc().doubleValue());
						R75Cell3.setCellStyle(numberStyle);
					} else {
						R75Cell3.setCellValue("");
						R75Cell3.setCellStyle(textStyle);
					}

// R75 Col G
					Cell R75Cell4 = row.createCell(6);
					if (record.getR75_qua_ii_qar() != null) {
						R75Cell4.setCellValue(record.getR75_qua_ii_qar().doubleValue());
						R75Cell4.setCellStyle(numberStyle);
					} else {
						R75Cell4.setCellValue("");
						R75Cell4.setCellStyle(textStyle);
					}

// R75 Col H
					Cell R75Cell5 = row.createCell(7);
					if (record.getR75_qua_ii_inr() != null) {
						R75Cell5.setCellValue(record.getR75_qua_ii_inr().doubleValue());
						R75Cell5.setCellStyle(numberStyle);
					} else {
						R75Cell5.setCellValue("");
						R75Cell5.setCellStyle(textStyle);
					}

// R75 Col I
					Cell R75Cell6 = row.createCell(8);
					if (record.getR75_qua_iii_lc() != null) {
						R75Cell6.setCellValue(record.getR75_qua_iii_lc().doubleValue());
						R75Cell6.setCellStyle(numberStyle);
					} else {
						R75Cell6.setCellValue("");
						R75Cell6.setCellStyle(textStyle);
					}

// R75 Col J
					Cell R75Cell7 = row.createCell(9);
					if (record.getR75_qua_iii_qar() != null) {
						R75Cell7.setCellValue(record.getR75_qua_iii_qar().doubleValue());
						R75Cell7.setCellStyle(numberStyle);
					} else {
						R75Cell7.setCellValue("");
						R75Cell7.setCellStyle(textStyle);
					}

// R75 Col K
					Cell R75Cell8 = row.createCell(10);
					if (record.getR75_qua_iii_inr() != null) {
						R75Cell8.setCellValue(record.getR75_qua_iii_inr().doubleValue());
						R75Cell8.setCellStyle(numberStyle);
					} else {
						R75Cell8.setCellValue("");
						R75Cell8.setCellStyle(textStyle);
					}

// R75 Col L
					Cell R75Cell9 = row.createCell(11);
					if (record.getR75_qua_iv_lc() != null) {
						R75Cell9.setCellValue(record.getR75_qua_iv_lc().doubleValue());
						R75Cell9.setCellStyle(numberStyle);
					} else {
						R75Cell9.setCellValue("");
						R75Cell9.setCellStyle(textStyle);
					}

// R75 Col M
					Cell R75Cell10 = row.createCell(12);
					if (record.getR75_qua_iv_qar() != null) {
						R75Cell10.setCellValue(record.getR75_qua_iv_qar().doubleValue());
						R75Cell10.setCellStyle(numberStyle);
					} else {
						R75Cell10.setCellValue("");
						R75Cell10.setCellStyle(textStyle);
					}

// R75 Col N
					Cell R75Cell11 = row.createCell(13);
					if (record.getR75_qua_iv_inr() != null) {
						R75Cell11.setCellValue(record.getR75_qua_iv_inr().doubleValue());
						R75Cell11.setCellStyle(numberStyle);
					} else {
						R75Cell11.setCellValue("");
						R75Cell11.setCellStyle(textStyle);
					}
					row = sheet.getRow(75);
// R76 Col C
					Cell R76Cell = row.createCell(2);
					if (record.getR76_qua_i_lc() != null) {
						R76Cell.setCellValue(record.getR76_qua_i_lc().doubleValue());
						R76Cell.setCellStyle(numberStyle);
					} else {
						R76Cell.setCellValue("");
						R76Cell.setCellStyle(textStyle);
					}

// R76 Col D
					Cell R76Cell1 = row.createCell(3);
					if (record.getR76_qua_i_qar() != null) {
						R76Cell1.setCellValue(record.getR76_qua_i_qar().doubleValue());
						R76Cell1.setCellStyle(numberStyle);
					} else {
						R76Cell1.setCellValue("");
						R76Cell1.setCellStyle(textStyle);
					}

// R76 Col E
					Cell R76Cell2 = row.createCell(4);
					if (record.getR76_qua_i_inr() != null) {
						R76Cell2.setCellValue(record.getR76_qua_i_inr().doubleValue());
						R76Cell2.setCellStyle(numberStyle);
					} else {
						R76Cell2.setCellValue("");
						R76Cell2.setCellStyle(textStyle);
					}

// R76 Col F
					Cell R76Cell3 = row.createCell(5);
					if (record.getR76_qua_ii_lc() != null) {
						R76Cell3.setCellValue(record.getR76_qua_ii_lc().doubleValue());
						R76Cell3.setCellStyle(numberStyle);
					} else {
						R76Cell3.setCellValue("");
						R76Cell3.setCellStyle(textStyle);
					}

// R76 Col G
					Cell R76Cell4 = row.createCell(6);
					if (record.getR76_qua_ii_qar() != null) {
						R76Cell4.setCellValue(record.getR76_qua_ii_qar().doubleValue());
						R76Cell4.setCellStyle(numberStyle);
					} else {
						R76Cell4.setCellValue("");
						R76Cell4.setCellStyle(textStyle);
					}

// R76 Col H
					Cell R76Cell5 = row.createCell(7);
					if (record.getR76_qua_ii_inr() != null) {
						R76Cell5.setCellValue(record.getR76_qua_ii_inr().doubleValue());
						R76Cell5.setCellStyle(numberStyle);
					} else {
						R76Cell5.setCellValue("");
						R76Cell5.setCellStyle(textStyle);
					}

// R76 Col I
					Cell R76Cell6 = row.createCell(8);
					if (record.getR76_qua_iii_lc() != null) {
						R76Cell6.setCellValue(record.getR76_qua_iii_lc().doubleValue());
						R76Cell6.setCellStyle(numberStyle);
					} else {
						R76Cell6.setCellValue("");
						R76Cell6.setCellStyle(textStyle);
					}

// R76 Col J
					Cell R76Cell7 = row.createCell(9);
					if (record.getR76_qua_iii_qar() != null) {
						R76Cell7.setCellValue(record.getR76_qua_iii_qar().doubleValue());
						R76Cell7.setCellStyle(numberStyle);
					} else {
						R76Cell7.setCellValue("");
						R76Cell7.setCellStyle(textStyle);
					}

// R76 Col K
					Cell R76Cell8 = row.createCell(10);
					if (record.getR76_qua_iii_inr() != null) {
						R76Cell8.setCellValue(record.getR76_qua_iii_inr().doubleValue());
						R76Cell8.setCellStyle(numberStyle);
					} else {
						R76Cell8.setCellValue("");
						R76Cell8.setCellStyle(textStyle);
					}

// R76 Col L
					Cell R76Cell9 = row.createCell(11);
					if (record.getR76_qua_iv_lc() != null) {
						R76Cell9.setCellValue(record.getR76_qua_iv_lc().doubleValue());
						R76Cell9.setCellStyle(numberStyle);
					} else {
						R76Cell9.setCellValue("");
						R76Cell9.setCellStyle(textStyle);
					}

// R76 Col M
					Cell R76Cell10 = row.createCell(12);
					if (record.getR76_qua_iv_qar() != null) {
						R76Cell10.setCellValue(record.getR76_qua_iv_qar().doubleValue());
						R76Cell10.setCellStyle(numberStyle);
					} else {
						R76Cell10.setCellValue("");
						R76Cell10.setCellStyle(textStyle);
					}

// R76 Col N
					Cell R76Cell11 = row.createCell(13);
					if (record.getR76_qua_iv_inr() != null) {
						R76Cell11.setCellValue(record.getR76_qua_iv_inr().doubleValue());
						R76Cell11.setCellStyle(numberStyle);
					} else {
						R76Cell11.setCellValue("");
						R76Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(76);
// R77 Col C
					Cell R77Cell = row.createCell(2);
					if (record.getR77_qua_i_lc() != null) {
						R77Cell.setCellValue(record.getR77_qua_i_lc().doubleValue());
						R77Cell.setCellStyle(numberStyle);
					} else {
						R77Cell.setCellValue("");
						R77Cell.setCellStyle(textStyle);
					}

// R77 Col D
					Cell R77Cell1 = row.createCell(3);
					if (record.getR77_qua_i_qar() != null) {
						R77Cell1.setCellValue(record.getR77_qua_i_qar().doubleValue());
						R77Cell1.setCellStyle(numberStyle);
					} else {
						R77Cell1.setCellValue("");
						R77Cell1.setCellStyle(textStyle);
					}

// R77 Col E
					Cell R77Cell2 = row.createCell(4);
					if (record.getR77_qua_i_inr() != null) {
						R77Cell2.setCellValue(record.getR77_qua_i_inr().doubleValue());
						R77Cell2.setCellStyle(numberStyle);
					} else {
						R77Cell2.setCellValue("");
						R77Cell2.setCellStyle(textStyle);
					}

// R77 Col F
					Cell R77Cell3 = row.createCell(5);
					if (record.getR77_qua_ii_lc() != null) {
						R77Cell3.setCellValue(record.getR77_qua_ii_lc().doubleValue());
						R77Cell3.setCellStyle(numberStyle);
					} else {
						R77Cell3.setCellValue("");
						R77Cell3.setCellStyle(textStyle);
					}

// R77 Col G
					Cell R77Cell4 = row.createCell(6);
					if (record.getR77_qua_ii_qar() != null) {
						R77Cell4.setCellValue(record.getR77_qua_ii_qar().doubleValue());
						R77Cell4.setCellStyle(numberStyle);
					} else {
						R77Cell4.setCellValue("");
						R77Cell4.setCellStyle(textStyle);
					}

// R77 Col H
					Cell R77Cell5 = row.createCell(7);
					if (record.getR77_qua_ii_inr() != null) {
						R77Cell5.setCellValue(record.getR77_qua_ii_inr().doubleValue());
						R77Cell5.setCellStyle(numberStyle);
					} else {
						R77Cell5.setCellValue("");
						R77Cell5.setCellStyle(textStyle);
					}

// R77 Col I
					Cell R77Cell6 = row.createCell(8);
					if (record.getR77_qua_iii_lc() != null) {
						R77Cell6.setCellValue(record.getR77_qua_iii_lc().doubleValue());
						R77Cell6.setCellStyle(numberStyle);
					} else {
						R77Cell6.setCellValue("");
						R77Cell6.setCellStyle(textStyle);
					}

// R77 Col J
					Cell R77Cell7 = row.createCell(9);
					if (record.getR77_qua_iii_qar() != null) {
						R77Cell7.setCellValue(record.getR77_qua_iii_qar().doubleValue());
						R77Cell7.setCellStyle(numberStyle);
					} else {
						R77Cell7.setCellValue("");
						R77Cell7.setCellStyle(textStyle);
					}

// R77 Col K
					Cell R77Cell8 = row.createCell(10);
					if (record.getR77_qua_iii_inr() != null) {
						R77Cell8.setCellValue(record.getR77_qua_iii_inr().doubleValue());
						R77Cell8.setCellStyle(numberStyle);
					} else {
						R77Cell8.setCellValue("");
						R77Cell8.setCellStyle(textStyle);
					}

// R77 Col L
					Cell R77Cell9 = row.createCell(11);
					if (record.getR77_qua_iv_lc() != null) {
						R77Cell9.setCellValue(record.getR77_qua_iv_lc().doubleValue());
						R77Cell9.setCellStyle(numberStyle);
					} else {
						R77Cell9.setCellValue("");
						R77Cell9.setCellStyle(textStyle);
					}

// R77 Col M
					Cell R77Cell10 = row.createCell(12);
					if (record.getR77_qua_iv_qar() != null) {
						R77Cell10.setCellValue(record.getR77_qua_iv_qar().doubleValue());
						R77Cell10.setCellStyle(numberStyle);
					} else {
						R77Cell10.setCellValue("");
						R77Cell10.setCellStyle(textStyle);
					}

// R77 Col N
					Cell R77Cell11 = row.createCell(13);
					if (record.getR77_qua_iv_inr() != null) {
						R77Cell11.setCellValue(record.getR77_qua_iv_inr().doubleValue());
						R77Cell11.setCellStyle(numberStyle);
					} else {
						R77Cell11.setCellValue("");
						R77Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(77);
// R78 Col C
					Cell R78Cell = row.createCell(2);
					if (record.getR78_qua_i_lc() != null) {
						R78Cell.setCellValue(record.getR78_qua_i_lc().doubleValue());
						R78Cell.setCellStyle(numberStyle);
					} else {
						R78Cell.setCellValue("");
						R78Cell.setCellStyle(textStyle);
					}

// R78 Col D
					Cell R78Cell1 = row.createCell(3);
					if (record.getR78_qua_i_qar() != null) {
						R78Cell1.setCellValue(record.getR78_qua_i_qar().doubleValue());
						R78Cell1.setCellStyle(numberStyle);
					} else {
						R78Cell1.setCellValue("");
						R78Cell1.setCellStyle(textStyle);
					}

// R78 Col E
					Cell R78Cell2 = row.createCell(4);
					if (record.getR78_qua_i_inr() != null) {
						R78Cell2.setCellValue(record.getR78_qua_i_inr().doubleValue());
						R78Cell2.setCellStyle(numberStyle);
					} else {
						R78Cell2.setCellValue("");
						R78Cell2.setCellStyle(textStyle);
					}

// R78 Col F
					Cell R78Cell3 = row.createCell(5);
					if (record.getR78_qua_ii_lc() != null) {
						R78Cell3.setCellValue(record.getR78_qua_ii_lc().doubleValue());
						R78Cell3.setCellStyle(numberStyle);
					} else {
						R78Cell3.setCellValue("");
						R78Cell3.setCellStyle(textStyle);
					}

// R78 Col G
					Cell R78Cell4 = row.createCell(6);
					if (record.getR78_qua_ii_qar() != null) {
						R78Cell4.setCellValue(record.getR78_qua_ii_qar().doubleValue());
						R78Cell4.setCellStyle(numberStyle);
					} else {
						R78Cell4.setCellValue("");
						R78Cell4.setCellStyle(textStyle);
					}

// R78 Col H
					Cell R78Cell5 = row.createCell(7);
					if (record.getR78_qua_ii_inr() != null) {
						R78Cell5.setCellValue(record.getR78_qua_ii_inr().doubleValue());
						R78Cell5.setCellStyle(numberStyle);
					} else {
						R78Cell5.setCellValue("");
						R78Cell5.setCellStyle(textStyle);
					}

// R78 Col I
					Cell R78Cell6 = row.createCell(8);
					if (record.getR78_qua_iii_lc() != null) {
						R78Cell6.setCellValue(record.getR78_qua_iii_lc().doubleValue());
						R78Cell6.setCellStyle(numberStyle);
					} else {
						R78Cell6.setCellValue("");
						R78Cell6.setCellStyle(textStyle);
					}

// R78 Col J
					Cell R78Cell7 = row.createCell(9);
					if (record.getR78_qua_iii_qar() != null) {
						R78Cell7.setCellValue(record.getR78_qua_iii_qar().doubleValue());
						R78Cell7.setCellStyle(numberStyle);
					} else {
						R78Cell7.setCellValue("");
						R78Cell7.setCellStyle(textStyle);
					}

// R78 Col K
					Cell R78Cell8 = row.createCell(10);
					if (record.getR78_qua_iii_inr() != null) {
						R78Cell8.setCellValue(record.getR78_qua_iii_inr().doubleValue());
						R78Cell8.setCellStyle(numberStyle);
					} else {
						R78Cell8.setCellValue("");
						R78Cell8.setCellStyle(textStyle);
					}

// R78 Col L
					Cell R78Cell9 = row.createCell(11);
					if (record.getR78_qua_iv_lc() != null) {
						R78Cell9.setCellValue(record.getR78_qua_iv_lc().doubleValue());
						R78Cell9.setCellStyle(numberStyle);
					} else {
						R78Cell9.setCellValue("");
						R78Cell9.setCellStyle(textStyle);
					}

// R78 Col M
					Cell R78Cell10 = row.createCell(12);
					if (record.getR78_qua_iv_qar() != null) {
						R78Cell10.setCellValue(record.getR78_qua_iv_qar().doubleValue());
						R78Cell10.setCellStyle(numberStyle);
					} else {
						R78Cell10.setCellValue("");
						R78Cell10.setCellStyle(textStyle);
					}

// R78 Col N
					Cell R78Cell11 = row.createCell(13);
					if (record.getR78_qua_iv_inr() != null) {
						R78Cell11.setCellValue(record.getR78_qua_iv_inr().doubleValue());
						R78Cell11.setCellStyle(numberStyle);
					} else {
						R78Cell11.setCellValue("");
						R78Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(79);
// R80 Col C
					Cell R80Cell = row.createCell(2);
					if (record.getR80_qua_i_lc() != null) {
						R80Cell.setCellValue(record.getR80_qua_i_lc().doubleValue());
						R80Cell.setCellStyle(numberStyle);
					} else {
						R80Cell.setCellValue("");
						R80Cell.setCellStyle(textStyle);
					}

// R80 Col D
					Cell R80Cell1 = row.createCell(3);
					if (record.getR80_qua_i_qar() != null) {
						R80Cell1.setCellValue(record.getR80_qua_i_qar().doubleValue());
						R80Cell1.setCellStyle(numberStyle);
					} else {
						R80Cell1.setCellValue("");
						R80Cell1.setCellStyle(textStyle);
					}

// R80 Col E
					Cell R80Cell2 = row.createCell(4);
					if (record.getR80_qua_i_inr() != null) {
						R80Cell2.setCellValue(record.getR80_qua_i_inr().doubleValue());
						R80Cell2.setCellStyle(numberStyle);
					} else {
						R80Cell2.setCellValue("");
						R80Cell2.setCellStyle(textStyle);
					}

// R80 Col F
					Cell R80Cell3 = row.createCell(5);
					if (record.getR80_qua_ii_lc() != null) {
						R80Cell3.setCellValue(record.getR80_qua_ii_lc().doubleValue());
						R80Cell3.setCellStyle(numberStyle);
					} else {
						R80Cell3.setCellValue("");
						R80Cell3.setCellStyle(textStyle);
					}

// R80 Col G
					Cell R80Cell4 = row.createCell(6);
					if (record.getR80_qua_ii_qar() != null) {
						R80Cell4.setCellValue(record.getR80_qua_ii_qar().doubleValue());
						R80Cell4.setCellStyle(numberStyle);
					} else {
						R80Cell4.setCellValue("");
						R80Cell4.setCellStyle(textStyle);
					}

// R80 Col H
					Cell R80Cell5 = row.createCell(7);
					if (record.getR80_qua_ii_inr() != null) {
						R80Cell5.setCellValue(record.getR80_qua_ii_inr().doubleValue());
						R80Cell5.setCellStyle(numberStyle);
					} else {
						R80Cell5.setCellValue("");
						R80Cell5.setCellStyle(textStyle);
					}

// R80 Col I
					Cell R80Cell6 = row.createCell(8);
					if (record.getR80_qua_iii_lc() != null) {
						R80Cell6.setCellValue(record.getR80_qua_iii_lc().doubleValue());
						R80Cell6.setCellStyle(numberStyle);
					} else {
						R80Cell6.setCellValue("");
						R80Cell6.setCellStyle(textStyle);
					}

// R80 Col J
					Cell R80Cell7 = row.createCell(9);
					if (record.getR80_qua_iii_qar() != null) {
						R80Cell7.setCellValue(record.getR80_qua_iii_qar().doubleValue());
						R80Cell7.setCellStyle(numberStyle);
					} else {
						R80Cell7.setCellValue("");
						R80Cell7.setCellStyle(textStyle);
					}

// R80 Col K
					Cell R80Cell8 = row.createCell(10);
					if (record.getR80_qua_iii_inr() != null) {
						R80Cell8.setCellValue(record.getR80_qua_iii_inr().doubleValue());
						R80Cell8.setCellStyle(numberStyle);
					} else {
						R80Cell8.setCellValue("");
						R80Cell8.setCellStyle(textStyle);
					}

// R80 Col L
					Cell R80Cell9 = row.createCell(11);
					if (record.getR80_qua_iv_lc() != null) {
						R80Cell9.setCellValue(record.getR80_qua_iv_lc().doubleValue());
						R80Cell9.setCellStyle(numberStyle);
					} else {
						R80Cell9.setCellValue("");
						R80Cell9.setCellStyle(textStyle);
					}

// R80 Col M
					Cell R80Cell10 = row.createCell(12);
					if (record.getR80_qua_iv_qar() != null) {
						R80Cell10.setCellValue(record.getR80_qua_iv_qar().doubleValue());
						R80Cell10.setCellStyle(numberStyle);
					} else {
						R80Cell10.setCellValue("");
						R80Cell10.setCellStyle(textStyle);
					}

// R80 Col N
					Cell R80Cell11 = row.createCell(13);
					if (record.getR80_qua_iv_inr() != null) {
						R80Cell11.setCellValue(record.getR80_qua_iv_inr().doubleValue());
						R80Cell11.setCellStyle(numberStyle);
					} else {
						R80Cell11.setCellValue("");
						R80Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(80);
// R81 Col C
					Cell R81Cell = row.createCell(2);
					if (record.getR81_qua_i_lc() != null) {
						R81Cell.setCellValue(record.getR81_qua_i_lc().doubleValue());
						R81Cell.setCellStyle(numberStyle);
					} else {
						R81Cell.setCellValue("");
						R81Cell.setCellStyle(textStyle);
					}

// R81 Col D
					Cell R81Cell1 = row.createCell(3);
					if (record.getR81_qua_i_qar() != null) {
						R81Cell1.setCellValue(record.getR81_qua_i_qar().doubleValue());
						R81Cell1.setCellStyle(numberStyle);
					} else {
						R81Cell1.setCellValue("");
						R81Cell1.setCellStyle(textStyle);
					}

// R81 Col E
					Cell R81Cell2 = row.createCell(4);
					if (record.getR81_qua_i_inr() != null) {
						R81Cell2.setCellValue(record.getR81_qua_i_inr().doubleValue());
						R81Cell2.setCellStyle(numberStyle);
					} else {
						R81Cell2.setCellValue("");
						R81Cell2.setCellStyle(textStyle);
					}

// R81 Col F
					Cell R81Cell3 = row.createCell(5);
					if (record.getR81_qua_ii_lc() != null) {
						R81Cell3.setCellValue(record.getR81_qua_ii_lc().doubleValue());
						R81Cell3.setCellStyle(numberStyle);
					} else {
						R81Cell3.setCellValue("");
						R81Cell3.setCellStyle(textStyle);
					}

// R81 Col G
					Cell R81Cell4 = row.createCell(6);
					if (record.getR81_qua_ii_qar() != null) {
						R81Cell4.setCellValue(record.getR81_qua_ii_qar().doubleValue());
						R81Cell4.setCellStyle(numberStyle);
					} else {
						R81Cell4.setCellValue("");
						R81Cell4.setCellStyle(textStyle);
					}

// R81 Col H
					Cell R81Cell5 = row.createCell(7);
					if (record.getR81_qua_ii_inr() != null) {
						R81Cell5.setCellValue(record.getR81_qua_ii_inr().doubleValue());
						R81Cell5.setCellStyle(numberStyle);
					} else {
						R81Cell5.setCellValue("");
						R81Cell5.setCellStyle(textStyle);
					}

// R81 Col I
					Cell R81Cell6 = row.createCell(8);
					if (record.getR81_qua_iii_lc() != null) {
						R81Cell6.setCellValue(record.getR81_qua_iii_lc().doubleValue());
						R81Cell6.setCellStyle(numberStyle);
					} else {
						R81Cell6.setCellValue("");
						R81Cell6.setCellStyle(textStyle);
					}

// R81 Col J
					Cell R81Cell7 = row.createCell(9);
					if (record.getR81_qua_iii_qar() != null) {
						R81Cell7.setCellValue(record.getR81_qua_iii_qar().doubleValue());
						R81Cell7.setCellStyle(numberStyle);
					} else {
						R81Cell7.setCellValue("");
						R81Cell7.setCellStyle(textStyle);
					}

// R81 Col K
					Cell R81Cell8 = row.createCell(10);
					if (record.getR81_qua_iii_inr() != null) {
						R81Cell8.setCellValue(record.getR81_qua_iii_inr().doubleValue());
						R81Cell8.setCellStyle(numberStyle);
					} else {
						R81Cell8.setCellValue("");
						R81Cell8.setCellStyle(textStyle);
					}

// R81 Col L
					Cell R81Cell9 = row.createCell(11);
					if (record.getR81_qua_iv_lc() != null) {
						R81Cell9.setCellValue(record.getR81_qua_iv_lc().doubleValue());
						R81Cell9.setCellStyle(numberStyle);
					} else {
						R81Cell9.setCellValue("");
						R81Cell9.setCellStyle(textStyle);
					}

// R81 Col M
					Cell R81Cell10 = row.createCell(12);
					if (record.getR81_qua_iv_qar() != null) {
						R81Cell10.setCellValue(record.getR81_qua_iv_qar().doubleValue());
						R81Cell10.setCellStyle(numberStyle);
					} else {
						R81Cell10.setCellValue("");
						R81Cell10.setCellStyle(textStyle);
					}

// R81 Col N
					Cell R81Cell11 = row.createCell(13);
					if (record.getR81_qua_iv_inr() != null) {
						R81Cell11.setCellValue(record.getR81_qua_iv_inr().doubleValue());
						R81Cell11.setCellStyle(numberStyle);
					} else {
						R81Cell11.setCellValue("");
						R81Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(82);
// R83 Col C
					Cell R83Cell = row.createCell(2);
					if (record.getR83_qua_i_lc() != null) {
						R83Cell.setCellValue(record.getR83_qua_i_lc().doubleValue());
						R83Cell.setCellStyle(numberStyle);
					} else {
						R83Cell.setCellValue("");
						R83Cell.setCellStyle(textStyle);
					}

// R83 Col D
					Cell R83Cell1 = row.createCell(3);
					if (record.getR83_qua_i_qar() != null) {
						R83Cell1.setCellValue(record.getR83_qua_i_qar().doubleValue());
						R83Cell1.setCellStyle(numberStyle);
					} else {
						R83Cell1.setCellValue("");
						R83Cell1.setCellStyle(textStyle);
					}

// R83 Col E
					Cell R83Cell2 = row.createCell(4);
					if (record.getR83_qua_i_inr() != null) {
						R83Cell2.setCellValue(record.getR83_qua_i_inr().doubleValue());
						R83Cell2.setCellStyle(numberStyle);
					} else {
						R83Cell2.setCellValue("");
						R83Cell2.setCellStyle(textStyle);
					}

// R83 Col F
					Cell R83Cell3 = row.createCell(5);
					if (record.getR83_qua_ii_lc() != null) {
						R83Cell3.setCellValue(record.getR83_qua_ii_lc().doubleValue());
						R83Cell3.setCellStyle(numberStyle);
					} else {
						R83Cell3.setCellValue("");
						R83Cell3.setCellStyle(textStyle);
					}

// R83 Col G
					Cell R83Cell4 = row.createCell(6);
					if (record.getR83_qua_ii_qar() != null) {
						R83Cell4.setCellValue(record.getR83_qua_ii_qar().doubleValue());
						R83Cell4.setCellStyle(numberStyle);
					} else {
						R83Cell4.setCellValue("");
						R83Cell4.setCellStyle(textStyle);
					}

// R83 Col H
					Cell R83Cell5 = row.createCell(7);
					if (record.getR83_qua_ii_inr() != null) {
						R83Cell5.setCellValue(record.getR83_qua_ii_inr().doubleValue());
						R83Cell5.setCellStyle(numberStyle);
					} else {
						R83Cell5.setCellValue("");
						R83Cell5.setCellStyle(textStyle);
					}

// R83 Col I
					Cell R83Cell6 = row.createCell(8);
					if (record.getR83_qua_iii_lc() != null) {
						R83Cell6.setCellValue(record.getR83_qua_iii_lc().doubleValue());
						R83Cell6.setCellStyle(numberStyle);
					} else {
						R83Cell6.setCellValue("");
						R83Cell6.setCellStyle(textStyle);
					}

// R83 Col J
					Cell R83Cell7 = row.createCell(9);
					if (record.getR83_qua_iii_qar() != null) {
						R83Cell7.setCellValue(record.getR83_qua_iii_qar().doubleValue());
						R83Cell7.setCellStyle(numberStyle);
					} else {
						R83Cell7.setCellValue("");
						R83Cell7.setCellStyle(textStyle);
					}

// R83 Col K
					Cell R83Cell8 = row.createCell(10);
					if (record.getR83_qua_iii_inr() != null) {
						R83Cell8.setCellValue(record.getR83_qua_iii_inr().doubleValue());
						R83Cell8.setCellStyle(numberStyle);
					} else {
						R83Cell8.setCellValue("");
						R83Cell8.setCellStyle(textStyle);
					}

// R83 Col L
					Cell R83Cell9 = row.createCell(11);
					if (record.getR83_qua_iv_lc() != null) {
						R83Cell9.setCellValue(record.getR83_qua_iv_lc().doubleValue());
						R83Cell9.setCellStyle(numberStyle);
					} else {
						R83Cell9.setCellValue("");
						R83Cell9.setCellStyle(textStyle);
					}

// R83 Col M
					Cell R83Cell10 = row.createCell(12);
					if (record.getR83_qua_iv_qar() != null) {
						R83Cell10.setCellValue(record.getR83_qua_iv_qar().doubleValue());
						R83Cell10.setCellStyle(numberStyle);
					} else {
						R83Cell10.setCellValue("");
						R83Cell10.setCellStyle(textStyle);
					}

// R83 Col N
					Cell R83Cell11 = row.createCell(13);
					if (record.getR83_qua_iv_inr() != null) {
						R83Cell11.setCellValue(record.getR83_qua_iv_inr().doubleValue());
						R83Cell11.setCellStyle(numberStyle);
					} else {
						R83Cell11.setCellValue("");
						R83Cell11.setCellStyle(textStyle);
					}
					Cell R83Cell12 = row.createCell(14);
					if (record.getR83_cumm_inr() != null) {
						R83Cell12.setCellValue(record.getR83_cumm_inr().doubleValue());
						R83Cell12.setCellStyle(numberStyle);
					} else {
						R83Cell12.setCellValue("");
						R83Cell12.setCellStyle(textStyle);
					}
					row = sheet.getRow(83);
// R84 Col C
					Cell R84Cell = row.createCell(2);
					if (record1.getR84_qua_i_lc() != null) {
						R84Cell.setCellValue(record1.getR84_qua_i_lc().doubleValue());
						R84Cell.setCellStyle(numberStyle);
					} else {
						R84Cell.setCellValue("");
						R84Cell.setCellStyle(textStyle);
					}

// R84 Col D
					Cell R84Cell1 = row.createCell(3);
					if (record1.getR84_qua_i_qar() != null) {
						R84Cell1.setCellValue(record1.getR84_qua_i_qar().doubleValue());
						R84Cell1.setCellStyle(numberStyle);
					} else {
						R84Cell1.setCellValue("");
						R84Cell1.setCellStyle(textStyle);
					}

// R84 Col E
					Cell R84Cell2 = row.createCell(4);
					if (record1.getR84_qua_i_inr() != null) {
						R84Cell2.setCellValue(record1.getR84_qua_i_inr().doubleValue());
						R84Cell2.setCellStyle(numberStyle);
					} else {
						R84Cell2.setCellValue("");
						R84Cell2.setCellStyle(textStyle);
					}

// R84 Col F
					Cell R84Cell3 = row.createCell(5);
					if (record1.getR84_qua_ii_lc() != null) {
						R84Cell3.setCellValue(record1.getR84_qua_ii_lc().doubleValue());
						R84Cell3.setCellStyle(numberStyle);
					} else {
						R84Cell3.setCellValue("");
						R84Cell3.setCellStyle(textStyle);
					}

// R84 Col G
					Cell R84Cell4 = row.createCell(6);
					if (record1.getR84_qua_ii_qar() != null) {
						R84Cell4.setCellValue(record1.getR84_qua_ii_qar().doubleValue());
						R84Cell4.setCellStyle(numberStyle);
					} else {
						R84Cell4.setCellValue("");
						R84Cell4.setCellStyle(textStyle);
					}

// R84 Col H
					Cell R84Cell5 = row.createCell(7);
					if (record1.getR84_qua_ii_inr() != null) {
						R84Cell5.setCellValue(record1.getR84_qua_ii_inr().doubleValue());
						R84Cell5.setCellStyle(numberStyle);
					} else {
						R84Cell5.setCellValue("");
						R84Cell5.setCellStyle(textStyle);
					}

// R84 Col I
					Cell R84Cell6 = row.createCell(8);
					if (record1.getR84_qua_iii_lc() != null) {
						R84Cell6.setCellValue(record1.getR84_qua_iii_lc().doubleValue());
						R84Cell6.setCellStyle(numberStyle);
					} else {
						R84Cell6.setCellValue("");
						R84Cell6.setCellStyle(textStyle);
					}

// R84 Col J
					Cell R84Cell7 = row.createCell(9);
					if (record1.getR84_qua_iii_qar() != null) {
						R84Cell7.setCellValue(record1.getR84_qua_iii_qar().doubleValue());
						R84Cell7.setCellStyle(numberStyle);
					} else {
						R84Cell7.setCellValue("");
						R84Cell7.setCellStyle(textStyle);
					}

// R84 Col K
					Cell R84Cell8 = row.createCell(10);
					if (record1.getR84_qua_iii_inr() != null) {
						R84Cell8.setCellValue(record1.getR84_qua_iii_inr().doubleValue());
						R84Cell8.setCellStyle(numberStyle);
					} else {
						R84Cell8.setCellValue("");
						R84Cell8.setCellStyle(textStyle);
					}

// R84 Col L
					Cell R84Cell9 = row.createCell(11);
					if (record1.getR84_qua_iv_lc() != null) {
						R84Cell9.setCellValue(record1.getR84_qua_iv_lc().doubleValue());
						R84Cell9.setCellStyle(numberStyle);
					} else {
						R84Cell9.setCellValue("");
						R84Cell9.setCellStyle(textStyle);
					}

// R84 Col M
					Cell R84Cell10 = row.createCell(12);
					if (record1.getR84_qua_iv_qar() != null) {
						R84Cell10.setCellValue(record1.getR84_qua_iv_qar().doubleValue());
						R84Cell10.setCellStyle(numberStyle);
					} else {
						R84Cell10.setCellValue("");
						R84Cell10.setCellStyle(textStyle);
					}

// R84 Col N
					Cell R84Cell11 = row.createCell(13);
					if (record1.getR84_qua_iv_inr() != null) {
						R84Cell11.setCellValue(record1.getR84_qua_iv_inr().doubleValue());
						R84Cell11.setCellStyle(numberStyle);
					} else {
						R84Cell11.setCellValue("");
						R84Cell11.setCellStyle(textStyle);
					}
					Cell R84Cell12 = row.createCell(14);
					if (record1.getR84_cumm_inr() != null) {
						R84Cell12.setCellValue(record1.getR84_cumm_inr().doubleValue());
						R84Cell12.setCellStyle(numberStyle);
					} else {
						R84Cell12.setCellValue("");
						R84Cell12.setCellStyle(textStyle);
					}
					row = sheet.getRow(85);
// R86 Col C
					Cell R86Cell = row.createCell(2);
					if (record1.getR86_qua_i_lc() != null) {
						R86Cell.setCellValue(record1.getR86_qua_i_lc().doubleValue());
						R86Cell.setCellStyle(numberStyle);
					} else {
						R86Cell.setCellValue("");
						R86Cell.setCellStyle(textStyle);
					}

// R86 Col D
					Cell R86Cell1 = row.createCell(3);
					if (record1.getR86_qua_i_qar() != null) {
						R86Cell1.setCellValue(record1.getR86_qua_i_qar().doubleValue());
						R86Cell1.setCellStyle(numberStyle);
					} else {
						R86Cell1.setCellValue("");
						R86Cell1.setCellStyle(textStyle);
					}

// R86 Col E
					Cell R86Cell2 = row.createCell(4);
					if (record1.getR86_qua_i_inr() != null) {
						R86Cell2.setCellValue(record1.getR86_qua_i_inr().doubleValue());
						R86Cell2.setCellStyle(numberStyle);
					} else {
						R86Cell2.setCellValue("");
						R86Cell2.setCellStyle(textStyle);
					}

// R86 Col F
					Cell R86Cell3 = row.createCell(5);
					if (record1.getR86_qua_ii_lc() != null) {
						R86Cell3.setCellValue(record1.getR86_qua_ii_lc().doubleValue());
						R86Cell3.setCellStyle(numberStyle);
					} else {
						R86Cell3.setCellValue("");
						R86Cell3.setCellStyle(textStyle);
					}

// R86 Col G
					Cell R86Cell4 = row.createCell(6);
					if (record1.getR86_qua_ii_qar() != null) {
						R86Cell4.setCellValue(record1.getR86_qua_ii_qar().doubleValue());
						R86Cell4.setCellStyle(numberStyle);
					} else {
						R86Cell4.setCellValue("");
						R86Cell4.setCellStyle(textStyle);
					}

// R86 Col H
					Cell R86Cell5 = row.createCell(7);
					if (record1.getR86_qua_ii_inr() != null) {
						R86Cell5.setCellValue(record1.getR86_qua_ii_inr().doubleValue());
						R86Cell5.setCellStyle(numberStyle);
					} else {
						R86Cell5.setCellValue("");
						R86Cell5.setCellStyle(textStyle);
					}

// R86 Col I
					Cell R86Cell6 = row.createCell(8);
					if (record1.getR86_qua_iii_lc() != null) {
						R86Cell6.setCellValue(record1.getR86_qua_iii_lc().doubleValue());
						R86Cell6.setCellStyle(numberStyle);
					} else {
						R86Cell6.setCellValue("");
						R86Cell6.setCellStyle(textStyle);
					}

// R86 Col J
					Cell R86Cell7 = row.createCell(9);
					if (record1.getR86_qua_iii_qar() != null) {
						R86Cell7.setCellValue(record1.getR86_qua_iii_qar().doubleValue());
						R86Cell7.setCellStyle(numberStyle);
					} else {
						R86Cell7.setCellValue("");
						R86Cell7.setCellStyle(textStyle);
					}

// R86 Col K
					Cell R86Cell8 = row.createCell(10);
					if (record1.getR86_qua_iii_inr() != null) {
						R86Cell8.setCellValue(record1.getR86_qua_iii_inr().doubleValue());
						R86Cell8.setCellStyle(numberStyle);
					} else {
						R86Cell8.setCellValue("");
						R86Cell8.setCellStyle(textStyle);
					}

// R86 Col L
					Cell R86Cell9 = row.createCell(11);
					if (record1.getR86_qua_iv_lc() != null) {
						R86Cell9.setCellValue(record1.getR86_qua_iv_lc().doubleValue());
						R86Cell9.setCellStyle(numberStyle);
					} else {
						R86Cell9.setCellValue("");
						R86Cell9.setCellStyle(textStyle);
					}

// R86 Col M
					Cell R86Cell10 = row.createCell(12);
					if (record1.getR86_qua_iv_qar() != null) {
						R86Cell10.setCellValue(record1.getR86_qua_iv_qar().doubleValue());
						R86Cell10.setCellStyle(numberStyle);
					} else {
						R86Cell10.setCellValue("");
						R86Cell10.setCellStyle(textStyle);
					}

// R86 Col N
					Cell R86Cell11 = row.createCell(13);
					if (record1.getR86_qua_iv_inr() != null) {
						R86Cell11.setCellValue(record1.getR86_qua_iv_inr().doubleValue());
						R86Cell11.setCellStyle(numberStyle);
					} else {
						R86Cell11.setCellValue("");
						R86Cell11.setCellStyle(textStyle);
					}
					Cell R86Cell12 = row.createCell(14);
					if (record1.getR86_cumm_inr() != null) {
						R86Cell12.setCellValue(record1.getR86_cumm_inr().doubleValue());
						R86Cell12.setCellStyle(numberStyle);
					} else {
						R86Cell12.setCellValue("");
						R86Cell12.setCellStyle(textStyle);
					}
					row = sheet.getRow(86);
// R87 Col C
					Cell R87Cell = row.createCell(2);
					if (record1.getR87_qua_i_lc() != null) {
						R87Cell.setCellValue(record1.getR87_qua_i_lc().doubleValue());
						R87Cell.setCellStyle(numberStyle);
					} else {
						R87Cell.setCellValue("");
						R87Cell.setCellStyle(textStyle);
					}

// R87 Col D
					Cell R87Cell1 = row.createCell(3);
					if (record1.getR87_qua_i_qar() != null) {
						R87Cell1.setCellValue(record1.getR87_qua_i_qar().doubleValue());
						R87Cell1.setCellStyle(numberStyle);
					} else {
						R87Cell1.setCellValue("");
						R87Cell1.setCellStyle(textStyle);
					}

// R87 Col E
					Cell R87Cell2 = row.createCell(4);
					if (record1.getR87_qua_i_inr() != null) {
						R87Cell2.setCellValue(record1.getR87_qua_i_inr().doubleValue());
						R87Cell2.setCellStyle(numberStyle);
					} else {
						R87Cell2.setCellValue("");
						R87Cell2.setCellStyle(textStyle);
					}

// R87 Col F
					Cell R87Cell3 = row.createCell(5);
					if (record1.getR87_qua_ii_lc() != null) {
						R87Cell3.setCellValue(record1.getR87_qua_ii_lc().doubleValue());
						R87Cell3.setCellStyle(numberStyle);
					} else {
						R87Cell3.setCellValue("");
						R87Cell3.setCellStyle(textStyle);
					}

// R87 Col G
					Cell R87Cell4 = row.createCell(6);
					if (record1.getR87_qua_ii_qar() != null) {
						R87Cell4.setCellValue(record1.getR87_qua_ii_qar().doubleValue());
						R87Cell4.setCellStyle(numberStyle);
					} else {
						R87Cell4.setCellValue("");
						R87Cell4.setCellStyle(textStyle);
					}

// R87 Col H
					Cell R87Cell5 = row.createCell(7);
					if (record1.getR87_qua_ii_inr() != null) {
						R87Cell5.setCellValue(record1.getR87_qua_ii_inr().doubleValue());
						R87Cell5.setCellStyle(numberStyle);
					} else {
						R87Cell5.setCellValue("");
						R87Cell5.setCellStyle(textStyle);
					}

// R87 Col I
					Cell R87Cell6 = row.createCell(8);
					if (record1.getR87_qua_iii_lc() != null) {
						R87Cell6.setCellValue(record1.getR87_qua_iii_lc().doubleValue());
						R87Cell6.setCellStyle(numberStyle);
					} else {
						R87Cell6.setCellValue("");
						R87Cell6.setCellStyle(textStyle);
					}

// R87 Col J
					Cell R87Cell7 = row.createCell(9);
					if (record1.getR87_qua_iii_qar() != null) {
						R87Cell7.setCellValue(record1.getR87_qua_iii_qar().doubleValue());
						R87Cell7.setCellStyle(numberStyle);
					} else {
						R87Cell7.setCellValue("");
						R87Cell7.setCellStyle(textStyle);
					}

// R87 Col K
					Cell R87Cell8 = row.createCell(10);
					if (record1.getR87_qua_iii_inr() != null) {
						R87Cell8.setCellValue(record1.getR87_qua_iii_inr().doubleValue());
						R87Cell8.setCellStyle(numberStyle);
					} else {
						R87Cell8.setCellValue("");
						R87Cell8.setCellStyle(textStyle);
					}

// R87 Col L
					Cell R87Cell9 = row.createCell(11);
					if (record1.getR87_qua_iv_lc() != null) {
						R87Cell9.setCellValue(record1.getR87_qua_iv_lc().doubleValue());
						R87Cell9.setCellStyle(numberStyle);
					} else {
						R87Cell9.setCellValue("");
						R87Cell9.setCellStyle(textStyle);
					}

// R87 Col M
					Cell R87Cell10 = row.createCell(12);
					if (record1.getR87_qua_iv_qar() != null) {
						R87Cell10.setCellValue(record1.getR87_qua_iv_qar().doubleValue());
						R87Cell10.setCellStyle(numberStyle);
					} else {
						R87Cell10.setCellValue("");
						R87Cell10.setCellStyle(textStyle);
					}

// R87 Col N
					Cell R87Cell11 = row.createCell(13);
					if (record1.getR87_qua_iv_inr() != null) {
						R87Cell11.setCellValue(record1.getR87_qua_iv_inr().doubleValue());
						R87Cell11.setCellStyle(numberStyle);
					} else {
						R87Cell11.setCellValue("");
						R87Cell11.setCellStyle(textStyle);
					}
					Cell R87Cell12 = row.createCell(14);
					if (record1.getR87_cumm_inr() != null) {
						R87Cell12.setCellValue(record1.getR87_cumm_inr().doubleValue());
						R87Cell12.setCellStyle(numberStyle);
					} else {
						R87Cell12.setCellValue("");
						R87Cell12.setCellStyle(textStyle);
					}
					row = sheet.getRow(87);
// R88 Col C
					Cell R88Cell = row.createCell(2);
					if (record1.getR88_qua_i_lc() != null) {
						R88Cell.setCellValue(record1.getR88_qua_i_lc().doubleValue());
						R88Cell.setCellStyle(numberStyle);
					} else {
						R88Cell.setCellValue("");
						R88Cell.setCellStyle(textStyle);
					}

// R88 Col D
					Cell R88Cell1 = row.createCell(3);
					if (record1.getR88_qua_i_qar() != null) {
						R88Cell1.setCellValue(record1.getR88_qua_i_qar().doubleValue());
						R88Cell1.setCellStyle(numberStyle);
					} else {
						R88Cell1.setCellValue("");
						R88Cell1.setCellStyle(textStyle);
					}

// R88 Col E
					Cell R88Cell2 = row.createCell(4);
					if (record1.getR88_qua_i_inr() != null) {
						R88Cell2.setCellValue(record1.getR88_qua_i_inr().doubleValue());
						R88Cell2.setCellStyle(numberStyle);
					} else {
						R88Cell2.setCellValue("");
						R88Cell2.setCellStyle(textStyle);
					}

// R88 Col F
					Cell R88Cell3 = row.createCell(5);
					if (record1.getR88_qua_ii_lc() != null) {
						R88Cell3.setCellValue(record1.getR88_qua_ii_lc().doubleValue());
						R88Cell3.setCellStyle(numberStyle);
					} else {
						R88Cell3.setCellValue("");
						R88Cell3.setCellStyle(textStyle);
					}

// R88 Col G
					Cell R88Cell4 = row.createCell(6);
					if (record1.getR88_qua_ii_qar() != null) {
						R88Cell4.setCellValue(record1.getR88_qua_ii_qar().doubleValue());
						R88Cell4.setCellStyle(numberStyle);
					} else {
						R88Cell4.setCellValue("");
						R88Cell4.setCellStyle(textStyle);
					}

// R88 Col H
					Cell R88Cell5 = row.createCell(7);
					if (record1.getR88_qua_ii_inr() != null) {
						R88Cell5.setCellValue(record1.getR88_qua_ii_inr().doubleValue());
						R88Cell5.setCellStyle(numberStyle);
					} else {
						R88Cell5.setCellValue("");
						R88Cell5.setCellStyle(textStyle);
					}

// R88 Col I
					Cell R88Cell6 = row.createCell(8);
					if (record1.getR88_qua_iii_lc() != null) {
						R88Cell6.setCellValue(record1.getR88_qua_iii_lc().doubleValue());
						R88Cell6.setCellStyle(numberStyle);
					} else {
						R88Cell6.setCellValue("");
						R88Cell6.setCellStyle(textStyle);
					}

// R88 Col J
					Cell R88Cell7 = row.createCell(9);
					if (record1.getR88_qua_iii_qar() != null) {
						R88Cell7.setCellValue(record1.getR88_qua_iii_qar().doubleValue());
						R88Cell7.setCellStyle(numberStyle);
					} else {
						R88Cell7.setCellValue("");
						R88Cell7.setCellStyle(textStyle);
					}

// R88 Col K
					Cell R88Cell8 = row.createCell(10);
					if (record1.getR88_qua_iii_inr() != null) {
						R88Cell8.setCellValue(record1.getR88_qua_iii_inr().doubleValue());
						R88Cell8.setCellStyle(numberStyle);
					} else {
						R88Cell8.setCellValue("");
						R88Cell8.setCellStyle(textStyle);
					}

// R88 Col L
					Cell R88Cell9 = row.createCell(11);
					if (record1.getR88_qua_iv_lc() != null) {
						R88Cell9.setCellValue(record1.getR88_qua_iv_lc().doubleValue());
						R88Cell9.setCellStyle(numberStyle);
					} else {
						R88Cell9.setCellValue("");
						R88Cell9.setCellStyle(textStyle);
					}

// R88 Col M
					Cell R88Cell10 = row.createCell(12);
					if (record1.getR88_qua_iv_qar() != null) {
						R88Cell10.setCellValue(record1.getR88_qua_iv_qar().doubleValue());
						R88Cell10.setCellStyle(numberStyle);
					} else {
						R88Cell10.setCellValue("");
						R88Cell10.setCellStyle(textStyle);
					}

// R88 Col N
					Cell R88Cell11 = row.createCell(13);
					if (record1.getR88_qua_iv_inr() != null) {
						R88Cell11.setCellValue(record1.getR88_qua_iv_inr().doubleValue());
						R88Cell11.setCellStyle(numberStyle);
					} else {
						R88Cell11.setCellValue("");
						R88Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(88);
// R89 Col C
					Cell R89Cell = row.createCell(2);
					if (record1.getR89_qua_i_lc() != null) {
						R89Cell.setCellValue(record1.getR89_qua_i_lc().doubleValue());
						R89Cell.setCellStyle(numberStyle);
					} else {
						R89Cell.setCellValue("");
						R89Cell.setCellStyle(textStyle);
					}

// R89 Col D
					Cell R89Cell1 = row.createCell(3);
					if (record1.getR89_qua_i_qar() != null) {
						R89Cell1.setCellValue(record1.getR89_qua_i_qar().doubleValue());
						R89Cell1.setCellStyle(numberStyle);
					} else {
						R89Cell1.setCellValue("");
						R89Cell1.setCellStyle(textStyle);
					}

// R89 Col E
					Cell R89Cell2 = row.createCell(4);
					if (record1.getR89_qua_i_inr() != null) {
						R89Cell2.setCellValue(record1.getR89_qua_i_inr().doubleValue());
						R89Cell2.setCellStyle(numberStyle);
					} else {
						R89Cell2.setCellValue("");
						R89Cell2.setCellStyle(textStyle);
					}

// R89 Col F
					Cell R89Cell3 = row.createCell(5);
					if (record1.getR89_qua_ii_lc() != null) {
						R89Cell3.setCellValue(record1.getR89_qua_ii_lc().doubleValue());
						R89Cell3.setCellStyle(numberStyle);
					} else {
						R89Cell3.setCellValue("");
						R89Cell3.setCellStyle(textStyle);
					}

// R89 Col G
					Cell R89Cell4 = row.createCell(6);
					if (record1.getR89_qua_ii_qar() != null) {
						R89Cell4.setCellValue(record1.getR89_qua_ii_qar().doubleValue());
						R89Cell4.setCellStyle(numberStyle);
					} else {
						R89Cell4.setCellValue("");
						R89Cell4.setCellStyle(textStyle);
					}

// R89 Col H
					Cell R89Cell5 = row.createCell(7);
					if (record1.getR89_qua_ii_inr() != null) {
						R89Cell5.setCellValue(record1.getR89_qua_ii_inr().doubleValue());
						R89Cell5.setCellStyle(numberStyle);
					} else {
						R89Cell5.setCellValue("");
						R89Cell5.setCellStyle(textStyle);
					}

// R89 Col I
					Cell R89Cell6 = row.createCell(8);
					if (record1.getR89_qua_iii_lc() != null) {
						R89Cell6.setCellValue(record1.getR89_qua_iii_lc().doubleValue());
						R89Cell6.setCellStyle(numberStyle);
					} else {
						R89Cell6.setCellValue("");
						R89Cell6.setCellStyle(textStyle);
					}

// R89 Col J
					Cell R89Cell7 = row.createCell(9);
					if (record1.getR89_qua_iii_qar() != null) {
						R89Cell7.setCellValue(record1.getR89_qua_iii_qar().doubleValue());
						R89Cell7.setCellStyle(numberStyle);
					} else {
						R89Cell7.setCellValue("");
						R89Cell7.setCellStyle(textStyle);
					}

// R89 Col K
					Cell R89Cell8 = row.createCell(10);
					if (record1.getR89_qua_iii_inr() != null) {
						R89Cell8.setCellValue(record1.getR89_qua_iii_inr().doubleValue());
						R89Cell8.setCellStyle(numberStyle);
					} else {
						R89Cell8.setCellValue("");
						R89Cell8.setCellStyle(textStyle);
					}

// R89 Col L
					Cell R89Cell9 = row.createCell(11);
					if (record1.getR89_qua_iv_lc() != null) {
						R89Cell9.setCellValue(record1.getR89_qua_iv_lc().doubleValue());
						R89Cell9.setCellStyle(numberStyle);
					} else {
						R89Cell9.setCellValue("");
						R89Cell9.setCellStyle(textStyle);
					}

// R89 Col M
					Cell R89Cell10 = row.createCell(12);
					if (record1.getR89_qua_iv_qar() != null) {
						R89Cell10.setCellValue(record1.getR89_qua_iv_qar().doubleValue());
						R89Cell10.setCellStyle(numberStyle);
					} else {
						R89Cell10.setCellValue("");
						R89Cell10.setCellStyle(textStyle);
					}

// R89 Col N
					Cell R89Cell11 = row.createCell(13);
					if (record1.getR89_qua_iv_inr() != null) {
						R89Cell11.setCellValue(record1.getR89_qua_iv_inr().doubleValue());
						R89Cell11.setCellStyle(numberStyle);
					} else {
						R89Cell11.setCellValue("");
						R89Cell11.setCellStyle(textStyle);
					}

					row = sheet.getRow(89);
					Cell R90Cell1 = row.createCell(3);
					if (record1.getR90_qua_i_qar() != null) {
						R90Cell1.setCellValue(record1.getR90_qua_i_qar().doubleValue());
						R90Cell1.setCellStyle(numberStyle);
					} else {
						R90Cell1.setCellValue("");
						R90Cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(90);
					Cell R91Cell1 = row.createCell(3);
					if (record1.getR91_qua_i_qar() != null) {
						R91Cell1.setCellValue(record1.getR91_qua_i_qar().doubleValue());
						R91Cell1.setCellStyle(numberStyle);
					} else {
						R91Cell1.setCellValue("");
						R91Cell1.setCellStyle(textStyle);
					}

				}

				workbook.setForceFormulaRecalculation(true);
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}

	public byte[] getExcelAS_11ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<AS_11_Archival_Summary_Entity1> dataList = AS_11_Archival_Summary_Repo1
				.getdatabydateListarchival(dateformat.parse(todate), version);
		List<AS_11_Archival_Summary_Entity2> dataList1 = AS_11_Archival_Summary_Repo2
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for AS_11 report. Returning empty result.");
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

			int startRow = 8;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					AS_11_Archival_Summary_Entity1 record = dataList.get(i);
					AS_11_Archival_Summary_Entity2 record1 = dataList1.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// Cell R9Cell1 = row.createCell(3);
					// if (record.getR9_fig_bal_sheet() != null) {
					// R9Cell1.setCellValue(record.getR9_fig_bal_sheet().doubleValue());
					// R9Cell1.setCellStyle(numberStyle);
					// } else {
					// R9Cell1.setCellValue("");
					// R9Cell1.setCellStyle(textStyle);
					// }
				}

				workbook.setForceFormulaRecalculation(true);
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}

	public byte[] getAS_11DetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for AS_11 Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getAS_11DetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("AS_11Details");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "AVERAGE", "REPORT LABLE",
					"REPORT ADDL CRITERIA1", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3 || i == 4) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<AS_11_Detail_Entity> reportData = AS_11_detail_repo.getdatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (AS_11_Detail_Entity item : reportData) {
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

					// AVERAGE (right aligned, 3 decimal places)
					balanceCell = row.createCell(4);
					if (item.getAverage() != null) {
						balanceCell.setCellValue(item.getAverage().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(5).setCellValue(item.getReportLabel());
					row.createCell(6).setCellValue(item.getReportAddlCriteria1());
					row.createCell(7)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						if (j != 3 && j != 4) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for AS_11 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating AS_11 Excel", e);
			return new byte[0];
		}
	}

	public byte[] getAS_11DetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for AS_11 ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("AS_11Detail");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "AVERAGE", "REPORT LABLE",
					"REPORT ADDL CRITERIA1", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3 || i == 4) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<AS_11_Archival_Detail_Entity> reportData = AS_11_Archival_Detail_Repo.getdatabydateList(parsedToDate,
					version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (AS_11_Archival_Detail_Entity item : reportData) {
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

					// AVERAGE (right aligned, 3 decimal places)
					balanceCell = row.createCell(4);
					if (item.getAverage() != null) {
						balanceCell.setCellValue(item.getAverage().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(5).setCellValue(item.getReportLabel());
					row.createCell(6).setCellValue(item.getReportAddlCriteria1());
					row.createCell(7)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						if (j != 3 && j != 4) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for AS_11 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating  AS_11 Excel", e);
			return new byte[0];
		}
	}

	// Archival View
		public List<Object[]> getAS_11Archival() {
			List<Object[]> archivalList = new ArrayList<>();

			try {
				List<AS_11_Archival_Summary_Entity1> repoData = AS_11_Archival_Summary_Repo1
						.getdatabydateListWithVersion();

				if (repoData != null && !repoData.isEmpty()) {
					for (AS_11_Archival_Summary_Entity1 entity : repoData) {
						Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
								entity.getREPORT_RESUBDATE() };
						archivalList.add(row);
					}

					System.out.println("Fetched " + archivalList.size() + " archival records");
					AS_11_Archival_Summary_Entity1 first = repoData.get(0);
					System.out.println("Latest archival version: " + first.getReport_version());
				} else {
					System.out.println("No archival data found.");
				}

			} catch (Exception e) {
				System.err.println("Error fetching PL_SCHS Archival data: " + e.getMessage());
				e.printStackTrace();
			}

			return archivalList;
		}

	@Autowired
	BRRS_AS_11_Detail_Repo brrs_AS_11_detail_repo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/AS_11");

		if (acctNo != null) {
			AS_11_Detail_Entity AS_11Entity = brrs_AS_11_detail_repo.findByAcctnumber(acctNo);
			if (AS_11Entity != null && AS_11Entity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(AS_11Entity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("AS_11Data", AS_11Entity);
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
			String average = request.getParameter("average");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			AS_11_Detail_Entity existing = brrs_AS_11_detail_repo.findByAcctnumber(acctNo);
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
				if (existing.getAcctBalanceInpula() == null
						|| existing.getAcctBalanceInpula().compareTo(newacctBalanceInpula) != 0) {
					existing.setAcctBalanceInpula(newacctBalanceInpula);
					isChanged = true;
					logger.info("Balance updated to {}", newacctBalanceInpula);
				}
			}
			if (average != null && !average.isEmpty()) {
				BigDecimal newaverage = new BigDecimal(average);
				if (existing.getAverage() == null || existing.getAverage().compareTo(newaverage) != 0) {
					existing.setAverage(newaverage);
					isChanged = true;
					logger.info("Balance updated to {}", newaverage);
				}
			}
			if (isChanged) {
				brrs_AS_11_detail_repo.save(existing);
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_AS_11_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_AS_11_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating AS_11 record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

//	public void updateReport(AS_11_Summary_Entity1 updatedEntity) {
//
//		AS_11_Summary_Entity1 existing = AS_11_summary_repo1.findById(updatedEntity.getReport_date()).orElseThrow(
//				() -> new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));
//
//		int[] rows = { 21, 22, 27, 28, 30, 31, 32, 33, 34, 35, 36, 37, 38, 40, 46,47, 57, 64, 66, 72, 73, 74, 77,
//				78, 79, 80, 81, 82, 85, 90, 91 };
//
//		String[] fields = { "qua_i_lc", "qua_i_qar", "qua_i_inr", "qua_ii_lc", "qua_ii_qar", "qua_ii_inr", "qua_iii_lc",
//				"qua_iii_qar", "qua_iii_inr", "qua_iv_lc", "qua_iv_qar", "qua_iv_inr", "cumm_inr", "cumm_bwp" };
//
//		try {
//			for (int i : rows) {
//				for (String field : fields) {
//
//					String getterName = "getR" + i + "_" + field;
//					String setterName = "setR" + i + "_" + field;
//
//					try {
//						Method getter = AS_11_Summary_Entity1.class.getMethod(getterName);
//						Method setter = AS_11_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());
//
//						Object newValue = getter.invoke(updatedEntity);
//						setter.invoke(existing, newValue);
//
//					} catch (NoSuchMethodException e) {
//						// Field not applicable for this row → skip safely
//					}
//				}
//			}
//		} catch (Exception e) {
//			throw new RuntimeException("Error while updating report fields", e);
//		}
//
//		AS_11_summary_repo1.save(existing);
//	}
	public void updateReport(AS_11_Summary_Entity1 updatedEntity) {

	    AS_11_Summary_Entity1 existing = AS_11_summary_repo1
	            .findById(updatedEntity.getReport_date())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

	    int[] rows = { 21, 22, 27, 28, 30, 31, 32, 33, 34, 35, 36, 37, 38,
	                   40, 46, 47, 57, 64, 66, 72, 73, 74,76, 77, 78, 79, 80,
	                   81, 82, 85, 90, 91 };

	    String[] fields = {
	            "qua_i_lc", "qua_i_qar", "qua_i_inr",
	            "qua_ii_lc", "qua_ii_qar", "qua_ii_inr",
	            "qua_iii_lc", "qua_iii_qar", "qua_iii_inr",
	            "qua_iv_lc", "qua_iv_qar", "qua_iv_inr",
	            "cumm_inr", "cumm_bwp"
	    };

	    try {
	        for (int i : rows) {
	            for (String field : fields) {

	                String getterName = "getR" + i + "_" + field;
	                String setterName = "setR" + i + "_" + field;

	                try {
	                    Method getter = AS_11_Summary_Entity1.class.getMethod(getterName);
	                    Method setter = AS_11_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);

	                    // ✅ IMPORTANT FIX
	                    if (newValue != null) {
	                        setter.invoke(existing, newValue);
	                    }

	                } catch (NoSuchMethodException e) {
	                    // skip
	                }
	            }
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    AS_11_summary_repo1.save(existing);
	}
}