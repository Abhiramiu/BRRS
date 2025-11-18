package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
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

import com.bornfire.brrs.dto.ReportLineItemDTO;
import com.bornfire.brrs.entities.BRRS_M_LA1_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_LA1_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_LA1_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_LA1_Summary_Repo;
import com.bornfire.brrs.entities.M_LA1_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_LA1_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_LA1_Detail_Entity;
import com.bornfire.brrs.entities.M_LA1_Summary_Entity;

@Component
@Service

public class BRRS_M_LA1_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_LA1_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_LA1_Summary_Repo BRRS_M_LA1_Summary_Repo;

	@Autowired
	BRRS_M_LA1_Detail_Repo M_LA1_Detail_Repo;

	@Autowired
	BRRS_M_LA1_Archival_Detail_Repo M_LA1_Archival_Detail_Repo;

	@Autowired
	BRRS_M_LA1_Archival_Summary_Repo M_LA1_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_LA1View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, String version) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<M_LA1_Archival_Summary_Entity> T1Master = new ArrayList<M_LA1_Archival_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = M_LA1_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);

		} else {

			List<M_LA1_Summary_Entity> T1Master = new ArrayList<M_LA1_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_LA1_Summary_Repo.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

		mv.setViewName("BRRS/M_LA1");

		// mv.addObject("reportmaster", T1Master);
		mv.addObject("displaymode", "summary");
		// mv.addObject("reportsflag", "reportsflag");
		// mv.addObject("menu", reportId);
		System.out.println("scv" + mv.getViewName());

		return mv;

	}

	public ModelAndView getM_LA1currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {

		ModelAndView mv = new ModelAndView("BRRS/M_LA1");
		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalRecords = 0;

		try {
// ✅ Parse toDate
			Date parsedDate = null;
			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

// ✅ Parse filter (rowId, columnIds)
			String rowId = null, columnId = null, columnId1 = null, columnId2 = null;
			if (filter != null && !filter.isEmpty()) {
				String[] parts = filter.split(",", -1);
				rowId = parts.length > 0 ? parts[0] : null;
				columnId = parts.length > 1 ? parts[1] : null;
				columnId1 = parts.length > 2 ? parts[2] : null;
				columnId2 = parts.length > 3 ? parts[3] : null;
			}

// ✅ ARCHIVAL DATA BRANCH
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.isEmpty()) {
				logger.info("Fetching ARCHIVAL data for version {}", version);

				List<M_LA1_Archival_Detail_Entity> detailList;

// 🔹 Filtered (ROWID + COLUMNID)
				if (rowId != null && !rowId.isEmpty()
						&& (isNotEmpty(columnId) || isNotEmpty(columnId1) || isNotEmpty(columnId2))) {

					logger.info("➡ ARCHIVAL DETAIL QUERY TRIGGERED (with filters)");
					detailList = M_LA1_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, columnId1,
							columnId2, parsedDate);

				} else {
					logger.info("➡ ARCHIVAL LIST QUERY TRIGGERED (with pagination)");
					detailList = M_LA1_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
					totalRecords = M_LA1_Archival_Detail_Repo.getdatacount(parsedDate);
					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", detailList);
				mv.addObject("reportmaster12", detailList);
				logger.info("ARCHIVAL COUNT: {}", (detailList != null ? detailList.size() : 0));

			} else {
// ✅ CURRENT DATA BRANCH
				logger.info("Fetching CURRENT data for M_LA1");

				List<M_LA1_Detail_Entity> detailList;

				if (rowId != null && !rowId.isEmpty()
						&& (isNotEmpty(columnId) || isNotEmpty(columnId1) || isNotEmpty(columnId2))) {

					logger.info("➡ CURRENT DETAIL QUERY TRIGGERED (with filters)");
					detailList = M_LA1_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, columnId1, columnId2,
							parsedDate);

				} else {
					logger.info("➡ CURRENT LIST QUERY TRIGGERED (with pagination)");
					detailList = M_LA1_Detail_Repo.getdatabydateList(parsedDate);
					totalRecords = M_LA1_Detail_Repo.getdatacount(parsedDate);
					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", detailList);
				mv.addObject("reportmaster12", detailList);
				logger.info("CURRENT COUNT: {}", (detailList != null ? detailList.size() : 0));
			}

		} catch (ParseException e) {
			logger.error("Invalid date format: {}", todate, e);
			mv.addObject("errorMessage", "Invalid date format: " + todate);
		} catch (Exception e) {
			logger.error("Unexpected error in getM_LA1currentDtl", e);
			mv.addObject("errorMessage", "Unexpected error: " + e.getMessage());
		}

// ✅ Common model attributes
		int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		mv.addObject("totalPages", totalPages);
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		logger.info("Total pages calculated: {}", totalPages);
		return mv;
	}

//Helper for null/empty check
	private boolean isNotEmpty(String value) {
		return value != null && !value.trim().isEmpty();
	}

	public byte[] BRRS_M_LA1Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		// ARCHIVAL check
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
					logger.info("Service: Generating ARCHIVAL report for version {}", version);
					return getExcelM_LA1ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
				}

		List<M_LA1_Summary_Entity> dataList = BRRS_M_LA1_Summary_Repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for LA1 report. Returning empty result.");
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
			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_LA1_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column B
					Cell cell1 = row.getCell(1);
					if (record.getR12_approved_limit() != null) {
						cell1.setCellValue(record.getR12_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row12
					// Column C
					Cell cell2 = row.createCell(2);
					if (record.getR12_balance_outstanding() != null) {
						cell2.setCellValue(record.getR12_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR12_no_of_acct() != null) {
						cell3.setCellValue(record.getR12_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR13_approved_limit() != null) {
						cell1.setCellValue(record.getR13_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row13
					// Column C
					cell2 = row.createCell(2);
					if (record.getR13_balance_outstanding() != null) {
						cell2.setCellValue(record.getR13_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_no_of_acct() != null) {
						cell3.setCellValue(record.getR13_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR14_approved_limit() != null) {
						cell1.setCellValue(record.getR14_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row14
					// Column C
					cell2 = row.getCell(2);
					if (record.getR14_balance_outstanding() != null) {
						cell2.setCellValue(record.getR14_balance_outstanding().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row14
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_no_of_acct() != null) {
						cell3.setCellValue(record.getR14_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR16_approved_limit() != null) {
						cell1.setCellValue(record.getR16_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row16
					// Column C
					cell2 = row.createCell(2);
					if (record.getR16_balance_outstanding() != null) {
						cell2.setCellValue(record.getR16_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row16
					// Column D
					cell3 = row.createCell(3);
					if (record.getR16_no_of_acct() != null) {
						cell3.setCellValue(record.getR16_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR17_approved_limit() != null) {
						cell1.setCellValue(record.getR17_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row17
					// Column C
					cell2 = row.createCell(2);
					if (record.getR17_balance_outstanding() != null) {
						cell2.setCellValue(record.getR17_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row17
					// Column D
					cell3 = row.createCell(3);
					if (record.getR17_no_of_acct() != null) {
						cell3.setCellValue(record.getR17_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR18_approved_limit() != null) {
						cell1.setCellValue(record.getR18_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row18
					// Column C
					cell2 = row.createCell(2);
					if (record.getR18_balance_outstanding() != null) {
						cell2.setCellValue(record.getR18_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row18
					// Column D
					cell3 = row.createCell(3);
					if (record.getR18_no_of_acct() != null) {
						cell3.setCellValue(record.getR18_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR19_approved_limit() != null) {
						cell1.setCellValue(record.getR19_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row19
					// Column C
					cell2 = row.createCell(2);
					if (record.getR19_balance_outstanding() != null) {
						cell2.setCellValue(record.getR19_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row19
					// Column D
					cell3 = row.createCell(3);
					if (record.getR19_no_of_acct() != null) {
						cell3.setCellValue(record.getR19_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR20_approved_limit() != null) {
						cell1.setCellValue(record.getR20_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row20
					// Column C
					cell2 = row.createCell(2);
					if (record.getR20_balance_outstanding() != null) {
						cell2.setCellValue(record.getR20_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row20
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_no_of_acct() != null) {
						cell3.setCellValue(record.getR20_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR21_approved_limit() != null) {
						cell1.setCellValue(record.getR21_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row21
					// Column C
					cell2 = row.createCell(2);
					if (record.getR21_balance_outstanding() != null) {
						cell2.setCellValue(record.getR21_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row21
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_no_of_acct() != null) {
						cell3.setCellValue(record.getR21_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR22_approved_limit() != null) {
						cell1.setCellValue(record.getR22_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row22
					// Column C
					cell2 = row.createCell(2);
					if (record.getR22_balance_outstanding() != null) {
						cell2.setCellValue(record.getR22_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row22
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_no_of_acct() != null) {
						cell3.setCellValue(record.getR22_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR23_approved_limit() != null) {
						cell1.setCellValue(record.getR23_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row23
					// Column C
					cell2 = row.createCell(2);
					if (record.getR23_balance_outstanding() != null) {
						cell2.setCellValue(record.getR23_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row23
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_no_of_acct() != null) {
						cell3.setCellValue(record.getR23_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR24_approved_limit() != null) {
						cell1.setCellValue(record.getR24_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row24
					// Column C
					cell2 = row.createCell(2);
					if (record.getR24_balance_outstanding() != null) {
						cell2.setCellValue(record.getR24_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row24
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_no_of_acct() != null) {
						cell3.setCellValue(record.getR24_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR25_approved_limit() != null) {
						cell1.setCellValue(record.getR25_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row25
					// Column C
					cell2 = row.createCell(2);
					if (record.getR25_balance_outstanding() != null) {
						cell2.setCellValue(record.getR25_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row25
					// Column D
					cell3 = row.createCell(3);
					if (record.getR25_no_of_acct() != null) {
						cell3.setCellValue(record.getR25_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR26_approved_limit() != null) {
						cell1.setCellValue(record.getR26_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row26
					// Column C
					cell2 = row.createCell(2);
					if (record.getR26_balance_outstanding() != null) {
						cell2.setCellValue(record.getR26_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row26
					// Column D
					cell3 = row.createCell(3);
					if (record.getR26_no_of_acct() != null) {
						cell3.setCellValue(record.getR26_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR27_approved_limit() != null) {
						cell1.setCellValue(record.getR27_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row27
					// Column C
					cell2 = row.createCell(2);
					if (record.getR27_balance_outstanding() != null) {
						cell2.setCellValue(record.getR27_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row27
					// Column D
					cell3 = row.createCell(3);
					if (record.getR27_no_of_acct() != null) {
						cell3.setCellValue(record.getR27_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR28_approved_limit() != null) {
						cell1.setCellValue(record.getR28_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row28
					// Column C
					cell2 = row.createCell(2);
					if (record.getR28_balance_outstanding() != null) {
						cell2.setCellValue(record.getR28_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row28
					// Column D
					cell3 = row.createCell(3);
					if (record.getR28_no_of_acct() != null) {
						cell3.setCellValue(record.getR28_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR30_approved_limit() != null) {
						cell1.setCellValue(record.getR30_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row30
					// Column C
					cell2 = row.createCell(2);
					if (record.getR30_balance_outstanding() != null) {
						cell2.setCellValue(record.getR30_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row30
					// Column D
					cell3 = row.createCell(3);
					if (record.getR30_no_of_acct() != null) {
						cell3.setCellValue(record.getR30_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR31_approved_limit() != null) {
						cell1.setCellValue(record.getR31_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row31
					// Column C
					cell2 = row.createCell(2);
					if (record.getR31_balance_outstanding() != null) {
						cell2.setCellValue(record.getR31_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row31
					// Column D
					cell3 = row.createCell(3);
					if (record.getR31_no_of_acct() != null) {
						cell3.setCellValue(record.getR31_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR32_approved_limit() != null) {
						cell1.setCellValue(record.getR32_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row32
					// Column C
					cell2 = row.createCell(2);
					if (record.getR32_balance_outstanding() != null) {
						cell2.setCellValue(record.getR32_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row32
					// Column D
					cell3 = row.createCell(3);
					if (record.getR32_no_of_acct() != null) {
						cell3.setCellValue(record.getR32_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR33_approved_limit() != null) {
						cell1.setCellValue(record.getR33_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row33
					// Column C
					cell2 = row.createCell(2);
					if (record.getR33_balance_outstanding() != null) {
						cell2.setCellValue(record.getR33_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR33_no_of_acct() != null) {
						cell3.setCellValue(record.getR33_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR34_approved_limit() != null) {
						cell1.setCellValue(record.getR34_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row34
					// Column C
					cell2 = row.createCell(2);
					if (record.getR34_balance_outstanding() != null) {
						cell2.setCellValue(record.getR34_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row34
					// Column D
					cell3 = row.createCell(3);
					if (record.getR34_no_of_acct() != null) {
						cell3.setCellValue(record.getR34_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR35_approved_limit() != null) {
						cell1.setCellValue(record.getR35_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row35
					// Column C
					cell2 = row.createCell(2);
					if (record.getR35_balance_outstanding() != null) {
						cell2.setCellValue(record.getR35_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row35
					// Column D
					cell3 = row.createCell(3);
					if (record.getR35_no_of_acct() != null) {
						cell3.setCellValue(record.getR35_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR36_approved_limit() != null) {
						cell1.setCellValue(record.getR36_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row36
					// Column C
					cell2 = row.createCell(2);
					if (record.getR36_balance_outstanding() != null) {
						cell2.setCellValue(record.getR36_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row36
					// Column D
					cell3 = row.createCell(3);
					if (record.getR36_no_of_acct() != null) {
						cell3.setCellValue(record.getR36_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row37
					row = sheet.getRow(36);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR37_approved_limit() != null) {
						cell1.setCellValue(record.getR37_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row37
					// Column C
					cell2 = row.createCell(2);
					if (record.getR37_balance_outstanding() != null) {
						cell2.setCellValue(record.getR37_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row37
					// Column D
					cell3 = row.createCell(3);
					if (record.getR37_no_of_acct() != null) {
						cell3.setCellValue(record.getR37_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(38);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR39_approved_limit() != null) {
						cell1.setCellValue(record.getR39_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row39
					// Column C
					cell2 = row.createCell(2);
					if (record.getR39_balance_outstanding() != null) {
						cell2.setCellValue(record.getR39_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row39
					// Column D
					cell3 = row.createCell(3);
					if (record.getR39_no_of_acct() != null) {
						cell3.setCellValue(record.getR39_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row40
					row = sheet.getRow(39);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR40_approved_limit() != null) {
						cell1.setCellValue(record.getR40_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row40
					// Column C
					cell2 = row.createCell(2);
					if (record.getR40_balance_outstanding() != null) {
						cell2.setCellValue(record.getR40_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row40
					// Column D
					cell3 = row.createCell(3);
					if (record.getR40_no_of_acct() != null) {
						cell3.setCellValue(record.getR40_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row42
					row = sheet.getRow(41);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR42_approved_limit() != null) {
						cell1.setCellValue(record.getR42_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row42
					// Column C
					cell2 = row.createCell(2);
					if (record.getR42_balance_outstanding() != null) {
						cell2.setCellValue(record.getR42_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row42
					// Column D
					cell3 = row.createCell(3);
					if (record.getR42_no_of_acct() != null) {
						cell3.setCellValue(record.getR42_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row43
					row = sheet.getRow(42);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR43_approved_limit() != null) {
						cell1.setCellValue(record.getR43_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row43
					// Column C
					cell2 = row.createCell(2);
					if (record.getR43_balance_outstanding() != null) {
						cell2.setCellValue(record.getR43_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row43
					// Column D
					cell3 = row.createCell(3);
					if (record.getR43_no_of_acct() != null) {
						cell3.setCellValue(record.getR43_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row45
					row = sheet.getRow(44);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR45_approved_limit() != null) {
						cell1.setCellValue(record.getR45_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row40
					// Column C
					cell2 = row.createCell(2);
					if (record.getR45_balance_outstanding() != null) {
						cell2.setCellValue(record.getR45_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row45
					// Column D
					cell3 = row.createCell(3);
					if (record.getR45_no_of_acct() != null) {
						cell3.setCellValue(record.getR45_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row46
					row = sheet.getRow(45);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR46_approved_limit() != null) {
						cell1.setCellValue(record.getR46_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row46
					// Column C
					cell2 = row.createCell(2);
					if (record.getR46_balance_outstanding() != null) {
						cell2.setCellValue(record.getR46_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row46
					// Column D
					cell3 = row.createCell(3);
					if (record.getR46_no_of_acct() != null) {
						cell3.setCellValue(record.getR46_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row47
					row = sheet.getRow(46);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR47_approved_limit() != null) {
						cell1.setCellValue(record.getR47_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row47
					// Column C
					cell2 = row.createCell(2);
					if (record.getR47_balance_outstanding() != null) {
						cell2.setCellValue(record.getR47_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row47
					// Column D
					cell3 = row.createCell(3);
					if (record.getR47_no_of_acct() != null) {
						cell3.setCellValue(record.getR47_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row48
					row = sheet.getRow(47);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR48_approved_limit() != null) {
						cell1.setCellValue(record.getR48_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row48
					// Column C
					cell2 = row.createCell(2);
					if (record.getR48_balance_outstanding() != null) {
						cell2.setCellValue(record.getR48_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row48
					// Column D
					cell3 = row.createCell(3);
					if (record.getR48_no_of_acct() != null) {
						cell3.setCellValue(record.getR48_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row50
					row = sheet.getRow(49);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR50_approved_limit() != null) {
						cell1.setCellValue(record.getR50_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row50
					// Column C
					cell2 = row.createCell(2);
					if (record.getR50_balance_outstanding() != null) {
						cell2.setCellValue(record.getR50_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row50
					// Column D
					cell3 = row.createCell(3);
					if (record.getR50_no_of_acct() != null) {
						cell3.setCellValue(record.getR50_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(50);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR51_approved_limit() != null) {
						cell1.setCellValue(record.getR51_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row51
					// Column C
					cell2 = row.createCell(2);
					if (record.getR51_balance_outstanding() != null) {
						cell2.setCellValue(record.getR51_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row51
					// Column D
					cell3 = row.createCell(3);
					if (record.getR51_no_of_acct() != null) {
						cell3.setCellValue(record.getR51_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row52
					row = sheet.getRow(51);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR52_approved_limit() != null) {
						cell1.setCellValue(record.getR52_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row52
					// Column C
					cell2 = row.createCell(2);
					if (record.getR52_balance_outstanding() != null) {
						cell2.setCellValue(record.getR52_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row52
					// Column D
					cell3 = row.createCell(3);
					if (record.getR52_no_of_acct() != null) {
						cell3.setCellValue(record.getR52_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR54_approved_limit() != null) {
						cell1.setCellValue(record.getR54_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row54
					// Column C
					cell2 = row.createCell(2);
					if (record.getR54_balance_outstanding() != null) {
						cell2.setCellValue(record.getR54_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row54
					// Column D
					cell3 = row.createCell(3);
					if (record.getR54_no_of_acct() != null) {
						cell3.setCellValue(record.getR54_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(54);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR55_approved_limit() != null) {
						cell1.setCellValue(record.getR55_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row55
					// Column C
					cell2 = row.createCell(2);
					if (record.getR55_balance_outstanding() != null) {
						cell2.setCellValue(record.getR55_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row55
					// Column D
					cell3 = row.createCell(3);
					if (record.getR55_no_of_acct() != null) {
						cell3.setCellValue(record.getR55_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row56
					row = sheet.getRow(55);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR56_approved_limit() != null) {
						cell1.setCellValue(record.getR56_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row56
					// Column C
					cell2 = row.createCell(2);
					if (record.getR56_balance_outstanding() != null) {
						cell2.setCellValue(record.getR56_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row56
					// Column D
					cell3 = row.createCell(3);
					if (record.getR56_no_of_acct() != null) {
						cell3.setCellValue(record.getR56_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row58
					row = sheet.getRow(57);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR58_approved_limit() != null) {
						cell1.setCellValue(record.getR58_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row58
					// Column C
					cell2 = row.createCell(2);
					if (record.getR58_balance_outstanding() != null) {
						cell2.setCellValue(record.getR58_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row58
					// Column D
					cell3 = row.createCell(3);
					if (record.getR58_no_of_acct() != null) {
						cell3.setCellValue(record.getR58_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row59
					row = sheet.getRow(58);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR59_approved_limit() != null) {
						cell1.setCellValue(record.getR59_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row59
					// Column C
					cell2 = row.createCell(2);
					if (record.getR59_balance_outstanding() != null) {
						cell2.setCellValue(record.getR59_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row59
					// Column D
					cell3 = row.createCell(3);
					if (record.getR59_no_of_acct() != null) {
						cell3.setCellValue(record.getR59_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row60
					row = sheet.getRow(59);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR60_approved_limit() != null) {
						cell1.setCellValue(record.getR60_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row60
					// Column C
					cell2 = row.createCell(2);
					if (record.getR60_balance_outstanding() != null) {
						cell2.setCellValue(record.getR60_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row60
					// Column D
					cell3 = row.createCell(3);
					if (record.getR60_no_of_acct() != null) {
						cell3.setCellValue(record.getR60_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row61
					row = sheet.getRow(60);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR61_approved_limit() != null) {
						cell1.setCellValue(record.getR61_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row61
					// Column C
					cell2 = row.createCell(2);
					if (record.getR61_balance_outstanding() != null) {
						cell2.setCellValue(record.getR61_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row61
					// Column D
					cell3 = row.createCell(3);
					if (record.getR61_no_of_acct() != null) {
						cell3.setCellValue(record.getR61_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row62
					row = sheet.getRow(61);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR62_approved_limit() != null) {
						cell1.setCellValue(record.getR62_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row62
					// Column C
					cell2 = row.createCell(2);
					if (record.getR62_balance_outstanding() != null) {
						cell2.setCellValue(record.getR62_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row62
					// Column D
					cell3 = row.createCell(3);
					if (record.getR62_no_of_acct() != null) {
						cell3.setCellValue(record.getR62_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row63
					row = sheet.getRow(62);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR63_approved_limit() != null) {
						cell1.setCellValue(record.getR63_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row63
					// Column C
					cell2 = row.createCell(2);
					if (record.getR63_balance_outstanding() != null) {
						cell2.setCellValue(record.getR63_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row63
					// Column D
					cell3 = row.createCell(3);
					if (record.getR63_no_of_acct() != null) {
						cell3.setCellValue(record.getR63_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
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

	public byte[] BRRS_M_LA1DetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRS_M_LA1 Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRRS_M_LA1Details");

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

			// sanction style (right aligned with 3 decimals)
			CellStyle sanctionStyle = workbook.createCellStyle();
			sanctionStyle.setAlignment(HorizontalAlignment.RIGHT);
			sanctionStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			sanctionStyle.setBorderTop(border);
			sanctionStyle.setBorderBottom(border);
			sanctionStyle.setBorderLeft(border);
			sanctionStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NUMBER", "SCHM DESC", "ACCT BALANCE IN PULA", "APPROVED LIMIT", "REPORT LABEL",
					"REPORT ADDL CRITERIA 1", "REPORT ADDL CRITERIA 2", "REPORT ADDL CRITERIA 3", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				if (i == 3|| i == 4) { // ACCT BALANCE
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}
				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_LA1_Detail_Entity> reportData = M_LA1_Detail_Repo.getdatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LA1_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(item.getCust_id());
					row.createCell(1).setCellValue(item.getAcct_number());
					row.createCell(2).setCellValue(item.getSchm_desc());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcct_balance_in_pula() != null) {
						balanceCell.setCellValue(item.getAcct_balance_in_pula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					// sanction (right aligned, 3 decimal places)
					Cell balanceCell1 = row.createCell(4);
					if (item.getSanction_limit() != null) {
						balanceCell1.setCellValue(item.getSanction_limit().doubleValue());
					} else {
						balanceCell1.setCellValue(0);
					}
					balanceCell1.setCellStyle(balanceStyle);

					row.createCell(5).setCellValue(item.getReport_label());
					row.createCell(6).setCellValue(item.getReport_addl_criteria_1());
					row.createCell(7).setCellValue(item.getReport_addl_criteria_2());
					row.createCell(8).setCellValue(item.getReport_addl_criteria_3());
					row.createCell(9)
							.setCellValue(item.getReport_date() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReport_date())
									: "");

					// Apply border style to all cells in the row
					for (int colIndex = 0; colIndex < headers.length; colIndex++) {
						Cell cell = row.getCell(colIndex);
						if (cell != null) {
							if (colIndex == 3) { // ACCT BALANCE
								cell.setCellStyle(balanceStyle);
							} else if (colIndex == 4) { // APPROVED LIMIT
								cell.setCellStyle(sanctionStyle);
							} else {
								cell.setCellStyle(dataStyle);
							}
						}
					}
				}
			} else {
				logger.info("No data found for BRRS_M_LA1 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_M_LA1 Excel", e);
			return new byte[0];
		}
	}

	public List<Object> getM_LA1Archival() {
		List<Object> M_LA1Archivallist = new ArrayList<>();
		try {
			M_LA1Archivallist = M_LA1_Archival_Summary_Repo.getM_LA1archival();
			System.out.println("countser" + M_LA1Archivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_LA1 Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_LA1Archivallist;
	}

	public byte[] getExcelM_LA1ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_LA1_Archival_Summary_Entity> dataList = M_LA1_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LA1 report. Returning empty result.");
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
					M_LA1_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column B
					Cell cell1 = row.getCell(1);
					if (record.getR12_approved_limit() != null) {
						cell1.setCellValue(record.getR12_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row12
					// Column C
					Cell cell2 = row.createCell(2);
					if (record.getR12_balance_outstanding() != null) {
						cell2.setCellValue(record.getR12_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR12_no_of_acct() != null) {
						cell3.setCellValue(record.getR12_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR13_approved_limit() != null) {
						cell1.setCellValue(record.getR13_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row13
					// Column C
					cell2 = row.createCell(2);
					if (record.getR13_balance_outstanding() != null) {
						cell2.setCellValue(record.getR13_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_no_of_acct() != null) {
						cell3.setCellValue(record.getR13_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR14_approved_limit() != null) {
						cell1.setCellValue(record.getR14_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row14
					// Column C
					cell2 = row.getCell(2);
					if (record.getR14_balance_outstanding() != null) {
						cell2.setCellValue(record.getR14_balance_outstanding().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row14
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_no_of_acct() != null) {
						cell3.setCellValue(record.getR14_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR16_approved_limit() != null) {
						cell1.setCellValue(record.getR16_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row16
					// Column C
					cell2 = row.createCell(2);
					if (record.getR16_balance_outstanding() != null) {
						cell2.setCellValue(record.getR16_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row16
					// Column D
					cell3 = row.createCell(3);
					if (record.getR16_no_of_acct() != null) {
						cell3.setCellValue(record.getR16_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR17_approved_limit() != null) {
						cell1.setCellValue(record.getR17_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row17
					// Column C
					cell2 = row.createCell(2);
					if (record.getR17_balance_outstanding() != null) {
						cell2.setCellValue(record.getR17_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row17
					// Column D
					cell3 = row.createCell(3);
					if (record.getR17_no_of_acct() != null) {
						cell3.setCellValue(record.getR17_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR18_approved_limit() != null) {
						cell1.setCellValue(record.getR18_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row18
					// Column C
					cell2 = row.createCell(2);
					if (record.getR18_balance_outstanding() != null) {
						cell2.setCellValue(record.getR18_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row18
					// Column D
					cell3 = row.createCell(3);
					if (record.getR18_no_of_acct() != null) {
						cell3.setCellValue(record.getR18_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR19_approved_limit() != null) {
						cell1.setCellValue(record.getR19_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row19
					// Column C
					cell2 = row.createCell(2);
					if (record.getR19_balance_outstanding() != null) {
						cell2.setCellValue(record.getR19_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row19
					// Column D
					cell3 = row.createCell(3);
					if (record.getR19_no_of_acct() != null) {
						cell3.setCellValue(record.getR19_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR20_approved_limit() != null) {
						cell1.setCellValue(record.getR20_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row20
					// Column C
					cell2 = row.createCell(2);
					if (record.getR20_balance_outstanding() != null) {
						cell2.setCellValue(record.getR20_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row20
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_no_of_acct() != null) {
						cell3.setCellValue(record.getR20_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR21_approved_limit() != null) {
						cell1.setCellValue(record.getR21_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row21
					// Column C
					cell2 = row.createCell(2);
					if (record.getR21_balance_outstanding() != null) {
						cell2.setCellValue(record.getR21_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row21
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_no_of_acct() != null) {
						cell3.setCellValue(record.getR21_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR22_approved_limit() != null) {
						cell1.setCellValue(record.getR22_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row22
					// Column C
					cell2 = row.createCell(2);
					if (record.getR22_balance_outstanding() != null) {
						cell2.setCellValue(record.getR22_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row22
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_no_of_acct() != null) {
						cell3.setCellValue(record.getR22_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR23_approved_limit() != null) {
						cell1.setCellValue(record.getR23_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row23
					// Column C
					cell2 = row.createCell(2);
					if (record.getR23_balance_outstanding() != null) {
						cell2.setCellValue(record.getR23_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row23
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_no_of_acct() != null) {
						cell3.setCellValue(record.getR23_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR24_approved_limit() != null) {
						cell1.setCellValue(record.getR24_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row24
					// Column C
					cell2 = row.createCell(2);
					if (record.getR24_balance_outstanding() != null) {
						cell2.setCellValue(record.getR24_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row24
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_no_of_acct() != null) {
						cell3.setCellValue(record.getR24_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR25_approved_limit() != null) {
						cell1.setCellValue(record.getR25_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row25
					// Column C
					cell2 = row.createCell(2);
					if (record.getR25_balance_outstanding() != null) {
						cell2.setCellValue(record.getR25_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row25
					// Column D
					cell3 = row.createCell(3);
					if (record.getR25_no_of_acct() != null) {
						cell3.setCellValue(record.getR25_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR26_approved_limit() != null) {
						cell1.setCellValue(record.getR26_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row26
					// Column C
					cell2 = row.createCell(2);
					if (record.getR26_balance_outstanding() != null) {
						cell2.setCellValue(record.getR26_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row26
					// Column D
					cell3 = row.createCell(3);
					if (record.getR26_no_of_acct() != null) {
						cell3.setCellValue(record.getR26_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR27_approved_limit() != null) {
						cell1.setCellValue(record.getR27_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row27
					// Column C
					cell2 = row.createCell(2);
					if (record.getR27_balance_outstanding() != null) {
						cell2.setCellValue(record.getR27_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row27
					// Column D
					cell3 = row.createCell(3);
					if (record.getR27_no_of_acct() != null) {
						cell3.setCellValue(record.getR27_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR28_approved_limit() != null) {
						cell1.setCellValue(record.getR28_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row28
					// Column C
					cell2 = row.createCell(2);
					if (record.getR28_balance_outstanding() != null) {
						cell2.setCellValue(record.getR28_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row28
					// Column D
					cell3 = row.createCell(3);
					if (record.getR28_no_of_acct() != null) {
						cell3.setCellValue(record.getR28_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR30_approved_limit() != null) {
						cell1.setCellValue(record.getR30_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row30
					// Column C
					cell2 = row.createCell(2);
					if (record.getR30_balance_outstanding() != null) {
						cell2.setCellValue(record.getR30_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row30
					// Column D
					cell3 = row.createCell(3);
					if (record.getR30_no_of_acct() != null) {
						cell3.setCellValue(record.getR30_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR31_approved_limit() != null) {
						cell1.setCellValue(record.getR31_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row31
					// Column C
					cell2 = row.createCell(2);
					if (record.getR31_balance_outstanding() != null) {
						cell2.setCellValue(record.getR31_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row31
					// Column D
					cell3 = row.createCell(3);
					if (record.getR31_no_of_acct() != null) {
						cell3.setCellValue(record.getR31_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR32_approved_limit() != null) {
						cell1.setCellValue(record.getR32_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row32
					// Column C
					cell2 = row.createCell(2);
					if (record.getR32_balance_outstanding() != null) {
						cell2.setCellValue(record.getR32_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row32
					// Column D
					cell3 = row.createCell(3);
					if (record.getR32_no_of_acct() != null) {
						cell3.setCellValue(record.getR32_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR33_approved_limit() != null) {
						cell1.setCellValue(record.getR33_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row33
					// Column C
					cell2 = row.createCell(2);
					if (record.getR33_balance_outstanding() != null) {
						cell2.setCellValue(record.getR33_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR33_no_of_acct() != null) {
						cell3.setCellValue(record.getR33_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR34_approved_limit() != null) {
						cell1.setCellValue(record.getR34_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row34
					// Column C
					cell2 = row.createCell(2);
					if (record.getR34_balance_outstanding() != null) {
						cell2.setCellValue(record.getR34_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row34
					// Column D
					cell3 = row.createCell(3);
					if (record.getR34_no_of_acct() != null) {
						cell3.setCellValue(record.getR34_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR35_approved_limit() != null) {
						cell1.setCellValue(record.getR35_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row35
					// Column C
					cell2 = row.createCell(2);
					if (record.getR35_balance_outstanding() != null) {
						cell2.setCellValue(record.getR35_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row35
					// Column D
					cell3 = row.createCell(3);
					if (record.getR35_no_of_acct() != null) {
						cell3.setCellValue(record.getR35_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR36_approved_limit() != null) {
						cell1.setCellValue(record.getR36_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row36
					// Column C
					cell2 = row.createCell(2);
					if (record.getR36_balance_outstanding() != null) {
						cell2.setCellValue(record.getR36_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row36
					// Column D
					cell3 = row.createCell(3);
					if (record.getR36_no_of_acct() != null) {
						cell3.setCellValue(record.getR36_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row37
					row = sheet.getRow(36);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR37_approved_limit() != null) {
						cell1.setCellValue(record.getR37_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row37
					// Column C
					cell2 = row.createCell(2);
					if (record.getR37_balance_outstanding() != null) {
						cell2.setCellValue(record.getR37_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row37
					// Column D
					cell3 = row.createCell(3);
					if (record.getR37_no_of_acct() != null) {
						cell3.setCellValue(record.getR37_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(38);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR39_approved_limit() != null) {
						cell1.setCellValue(record.getR39_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row39
					// Column C
					cell2 = row.createCell(2);
					if (record.getR39_balance_outstanding() != null) {
						cell2.setCellValue(record.getR39_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row39
					// Column D
					cell3 = row.createCell(3);
					if (record.getR39_no_of_acct() != null) {
						cell3.setCellValue(record.getR39_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row40
					row = sheet.getRow(39);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR40_approved_limit() != null) {
						cell1.setCellValue(record.getR40_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row40
					// Column C
					cell2 = row.createCell(2);
					if (record.getR40_balance_outstanding() != null) {
						cell2.setCellValue(record.getR40_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row40
					// Column D
					cell3 = row.createCell(3);
					if (record.getR40_no_of_acct() != null) {
						cell3.setCellValue(record.getR40_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row42
					row = sheet.getRow(41);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR42_approved_limit() != null) {
						cell1.setCellValue(record.getR42_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row42
					// Column C
					cell2 = row.createCell(2);
					if (record.getR42_balance_outstanding() != null) {
						cell2.setCellValue(record.getR42_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row42
					// Column D
					cell3 = row.createCell(3);
					if (record.getR42_no_of_acct() != null) {
						cell3.setCellValue(record.getR42_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row43
					row = sheet.getRow(42);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR43_approved_limit() != null) {
						cell1.setCellValue(record.getR43_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row43
					// Column C
					cell2 = row.createCell(2);
					if (record.getR43_balance_outstanding() != null) {
						cell2.setCellValue(record.getR43_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row43
					// Column D
					cell3 = row.createCell(3);
					if (record.getR43_no_of_acct() != null) {
						cell3.setCellValue(record.getR43_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row45
					row = sheet.getRow(44);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR45_approved_limit() != null) {
						cell1.setCellValue(record.getR45_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row40
					// Column C
					cell2 = row.createCell(2);
					if (record.getR45_balance_outstanding() != null) {
						cell2.setCellValue(record.getR45_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row45
					// Column D
					cell3 = row.createCell(3);
					if (record.getR45_no_of_acct() != null) {
						cell3.setCellValue(record.getR45_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row46
					row = sheet.getRow(45);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR46_approved_limit() != null) {
						cell1.setCellValue(record.getR46_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row46
					// Column C
					cell2 = row.createCell(2);
					if (record.getR46_balance_outstanding() != null) {
						cell2.setCellValue(record.getR46_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row46
					// Column D
					cell3 = row.createCell(3);
					if (record.getR46_no_of_acct() != null) {
						cell3.setCellValue(record.getR46_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row47
					row = sheet.getRow(46);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR47_approved_limit() != null) {
						cell1.setCellValue(record.getR47_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row47
					// Column C
					cell2 = row.createCell(2);
					if (record.getR47_balance_outstanding() != null) {
						cell2.setCellValue(record.getR47_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row47
					// Column D
					cell3 = row.createCell(3);
					if (record.getR47_no_of_acct() != null) {
						cell3.setCellValue(record.getR47_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row48
					row = sheet.getRow(47);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR48_approved_limit() != null) {
						cell1.setCellValue(record.getR48_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row48
					// Column C
					cell2 = row.createCell(2);
					if (record.getR48_balance_outstanding() != null) {
						cell2.setCellValue(record.getR48_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row48
					// Column D
					cell3 = row.createCell(3);
					if (record.getR48_no_of_acct() != null) {
						cell3.setCellValue(record.getR48_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row50
					row = sheet.getRow(49);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR50_approved_limit() != null) {
						cell1.setCellValue(record.getR50_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row50
					// Column C
					cell2 = row.createCell(2);
					if (record.getR50_balance_outstanding() != null) {
						cell2.setCellValue(record.getR50_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row50
					// Column D
					cell3 = row.createCell(3);
					if (record.getR50_no_of_acct() != null) {
						cell3.setCellValue(record.getR50_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(50);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR51_approved_limit() != null) {
						cell1.setCellValue(record.getR51_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row51
					// Column C
					cell2 = row.createCell(2);
					if (record.getR51_balance_outstanding() != null) {
						cell2.setCellValue(record.getR51_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row51
					// Column D
					cell3 = row.createCell(3);
					if (record.getR51_no_of_acct() != null) {
						cell3.setCellValue(record.getR51_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row52
					row = sheet.getRow(51);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR52_approved_limit() != null) {
						cell1.setCellValue(record.getR52_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row52
					// Column C
					cell2 = row.createCell(2);
					if (record.getR52_balance_outstanding() != null) {
						cell2.setCellValue(record.getR52_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row52
					// Column D
					cell3 = row.createCell(3);
					if (record.getR52_no_of_acct() != null) {
						cell3.setCellValue(record.getR52_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR54_approved_limit() != null) {
						cell1.setCellValue(record.getR54_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row54
					// Column C
					cell2 = row.createCell(2);
					if (record.getR54_balance_outstanding() != null) {
						cell2.setCellValue(record.getR54_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row54
					// Column D
					cell3 = row.createCell(3);
					if (record.getR54_no_of_acct() != null) {
						cell3.setCellValue(record.getR54_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(54);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR55_approved_limit() != null) {
						cell1.setCellValue(record.getR55_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row55
					// Column C
					cell2 = row.createCell(2);
					if (record.getR55_balance_outstanding() != null) {
						cell2.setCellValue(record.getR55_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row55
					// Column D
					cell3 = row.createCell(3);
					if (record.getR55_no_of_acct() != null) {
						cell3.setCellValue(record.getR55_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row56
					row = sheet.getRow(55);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR56_approved_limit() != null) {
						cell1.setCellValue(record.getR56_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row56
					// Column C
					cell2 = row.createCell(2);
					if (record.getR56_balance_outstanding() != null) {
						cell2.setCellValue(record.getR56_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row56
					// Column D
					cell3 = row.createCell(3);
					if (record.getR56_no_of_acct() != null) {
						cell3.setCellValue(record.getR56_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row58
					row = sheet.getRow(57);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR58_approved_limit() != null) {
						cell1.setCellValue(record.getR58_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row58
					// Column C
					cell2 = row.createCell(2);
					if (record.getR58_balance_outstanding() != null) {
						cell2.setCellValue(record.getR58_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row58
					// Column D
					cell3 = row.createCell(3);
					if (record.getR58_no_of_acct() != null) {
						cell3.setCellValue(record.getR58_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row59
					row = sheet.getRow(58);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR59_approved_limit() != null) {
						cell1.setCellValue(record.getR59_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row59
					// Column C
					cell2 = row.createCell(2);
					if (record.getR59_balance_outstanding() != null) {
						cell2.setCellValue(record.getR59_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row59
					// Column D
					cell3 = row.createCell(3);
					if (record.getR59_no_of_acct() != null) {
						cell3.setCellValue(record.getR59_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row60
					row = sheet.getRow(59);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR60_approved_limit() != null) {
						cell1.setCellValue(record.getR60_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row60
					// Column C
					cell2 = row.createCell(2);
					if (record.getR60_balance_outstanding() != null) {
						cell2.setCellValue(record.getR60_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row60
					// Column D
					cell3 = row.createCell(3);
					if (record.getR60_no_of_acct() != null) {
						cell3.setCellValue(record.getR60_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row61
					row = sheet.getRow(60);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR61_approved_limit() != null) {
						cell1.setCellValue(record.getR61_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row61
					// Column C
					cell2 = row.createCell(2);
					if (record.getR61_balance_outstanding() != null) {
						cell2.setCellValue(record.getR61_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row61
					// Column D
					cell3 = row.createCell(3);
					if (record.getR61_no_of_acct() != null) {
						cell3.setCellValue(record.getR61_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row62
					row = sheet.getRow(61);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR62_approved_limit() != null) {
						cell1.setCellValue(record.getR62_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row62
					// Column C
					cell2 = row.createCell(2);
					if (record.getR62_balance_outstanding() != null) {
						cell2.setCellValue(record.getR62_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row62
					// Column D
					cell3 = row.createCell(3);
					if (record.getR62_no_of_acct() != null) {
						cell3.setCellValue(record.getR62_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row63
					row = sheet.getRow(62);
					// Column H
					cell1 = row.createCell(1);
					if (record.getR63_approved_limit() != null) {
						cell1.setCellValue(record.getR63_approved_limit().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row63
					// Column C
					cell2 = row.createCell(2);
					if (record.getR63_balance_outstanding() != null) {
						cell2.setCellValue(record.getR63_balance_outstanding().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row63
					// Column D
					cell3 = row.createCell(3);
					if (record.getR63_no_of_acct() != null) {
						cell3.setCellValue(record.getR63_no_of_acct().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
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
	        logger.info("Generating Excel for BRRS_M_LA1 ARCHIVAL Details...");
	        System.out.println("Came to Detail download service");

	        // Only proceed if ARCHIVAL and version provided
	        if (!"ARCHIVAL".equalsIgnoreCase(type) || version == null || version.isEmpty()) {
	            logger.warn("Invalid type/version for archival download.");
	            return new byte[0];
	        }

	        // Create workbook and sheet
	        XSSFWorkbook workbook = new XSSFWorkbook();
	        XSSFSheet sheet = workbook.createSheet("BRRS_M_LA1_Archival_Detail");

	        // Border style
	        BorderStyle border = BorderStyle.THIN;

	        // Header style
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

	        // Right-aligned header (for numeric columns)
	        CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
	        rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
	        rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

	        // Data style (text)
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
	        String[] headers = { "CUST ID", "ACCT NUMBER", "SCHM DESC", "ACCT BALANCE IN PULA", "APPROVED LIMIT", "REPORT LABEL",
	                "REPORT ADDL CRITERIA 1", "REPORT ADDL CRITERIA 2", "REPORT ADDL CRITERIA 3", "REPORT_DATE" };

	        XSSFRow headerRow = sheet.createRow(0);
	        for (int i = 0; i < headers.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(headers[i]);

	            if (i == 3|| i == 4) { // ACCT BALANCE
	                cell.setCellStyle(rightAlignedHeaderStyle);
	            } else {
	                cell.setCellStyle(headerStyle);
	            }

	            sheet.setColumnWidth(i, 5000);
	        }

	        // Parse date
	        Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);

	        // Fetch data
	        List<M_LA1_Archival_Detail_Entity> reportData =
	                M_LA1_Archival_Detail_Repo.getdatabydateList(parsedToDate, version);

	        if (reportData != null && !reportData.isEmpty()) {
	            int rowIndex = 1;
	            for (M_LA1_Archival_Detail_Entity item : reportData) {
	                XSSFRow row = sheet.createRow(rowIndex++);

	                // Text columns
	                row.createCell(0).setCellValue(item.getCust_id());
	                row.createCell(1).setCellValue(item.getAcct_number());
	                row.createCell(2).setCellValue(item.getSchm_desc());

	             // ACCT BALANCE (right aligned, 3 decimal places)
	                Cell balanceCell = row.createCell(3);
	                if (item.getAcct_balance_in_pula() != null) {
	                    balanceCell.setCellValue(item.getAcct_balance_in_pula().doubleValue());
	                } else {
	                    balanceCell.setCellValue(0);
	                }
	                balanceCell.setCellStyle(balanceStyle);

	                
	                
	             // ACCT BALANCE (right aligned, 3 decimal places)
	                Cell sanctionCell = row.createCell(4);
	                if (item.getSanction_limit() != null) {
	                	sanctionCell.setCellValue(item.getSanction_limit().doubleValue());
	                } else {
	                	sanctionCell.setCellValue(0);
	                }
	                sanctionCell.setCellStyle(balanceStyle);

	                // Remaining text columns
	                row.createCell(5).setCellValue(item.getReport_label());
	                row.createCell(6).setCellValue(item.getReport_addl_criteria_1());
	                row.createCell(7).setCellValue(item.getReport_addl_criteria_2());
	                row.createCell(8).setCellValue(item.getReport_addl_criteria_3());
	                row.createCell(9).setCellValue(item.getReport_date() != null
	                        ? new SimpleDateFormat("dd-MM-yyyy").format(item.getReport_date())
	                        : "");

	                // Apply text style to non-numeric cells
	                for (int j = 0; j < headers.length; j++) {
	                    if (j != 3 && j != 4) {
	                        row.getCell(j).setCellStyle(dataStyle);
	                    }
	                }
	            }
	        }

	        // Write to byte array
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        workbook.write(bos);
	        workbook.close();

	        logger.info("Excel generation completed with {} row(s).",
	                reportData != null ? reportData.size() : 0);
	        return bos.toByteArray();

	    } catch (Exception e) {
	        logger.error("Error generating BRRS_M_LA1 ARCHIVAL Excel", e);
	        return new byte[0];
	    }
	}


	public List<ReportLineItemDTO> getReportData(String filename) throws Exception {
		List<ReportLineItemDTO> reportData = new ArrayList<>();

		File file = new File(filename);
		if (!file.exists()) {
			throw new Exception("File not found: " + filename);
		}

		FileInputStream fis = new FileInputStream(file);
		Workbook workbook = new XSSFWorkbook(fis);
		Sheet sheet = workbook.getSheetAt(0);

		final int START_ROW_INDEX = 10;
		final int END_ROW_INDEX = 63;

		Iterator<Row> rowIterator = sheet.iterator();
		int srlNo = 1;

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			int currentRowIndex = row.getRowNum();

			if (currentRowIndex < START_ROW_INDEX) {
				continue;
			}

			if (currentRowIndex > END_ROW_INDEX) {
				break;
			}

			Cell fieldDescCell = row.getCell(0);

			if (fieldDescCell == null || fieldDescCell.getCellType() == CellType.BLANK) {
				continue;
			}

			String fieldDesc = "";
			try {
				fieldDesc = fieldDescCell.getStringCellValue();
			} catch (IllegalStateException e) {

				FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
				CellValue cellValue = evaluator.evaluate(fieldDescCell);
				if (cellValue != null) {
					if (cellValue.getCellType() == CellType.STRING) {
						fieldDesc = cellValue.getStringValue();
					} else if (cellValue.getCellType() == CellType.NUMERIC) {
						fieldDesc = String.valueOf(cellValue.getNumberValue());
					} else if (cellValue.getCellType() == CellType.BOOLEAN) {
						fieldDesc = String.valueOf(cellValue.getBooleanValue());
					}

				}
				if (fieldDesc.isEmpty() && fieldDescCell.getCellType() == CellType.FORMULA) {

					fieldDesc = fieldDescCell.getCellFormula();
				}
			} catch (Exception e) {
				System.err.println("Error reading cell A" + (currentRowIndex + 1) + ": " + e.getMessage());
				continue;
			}

			if (fieldDesc == null || fieldDesc.trim().isEmpty()) {
				continue;
			}

			ReportLineItemDTO dto = new ReportLineItemDTO();
			dto.setSrlNo(srlNo++);
			dto.setFieldDescription(fieldDesc.trim());

			dto.setReportLabel("R" + (currentRowIndex + 1));

			boolean hasFormula = false;
			for (int i = 0; i < row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i);
				if (cell != null && cell.getCellType() == CellType.FORMULA) {
					hasFormula = true;
					break;
				}
			}
			dto.setHeader(hasFormula ? "Y" : " ");

			dto.setRemarks("");

			reportData.add(dto);
		}

		workbook.close();
		fis.close();

		System.out.println("✅ M_LA1 Report data processed (Excel Row " + (START_ROW_INDEX + 1) + " to "
				+ (END_ROW_INDEX + 1) + "). Total items: " + reportData.size());
		return reportData;
	}

//	public boolean updateProvision(M_LA1_Detail_Entity la1Data) {
//		try {
//			System.out.println("Came to LA1 Service");
//
//			// ✅ Must match your entity field name exactly
//			M_LA1_Detail_Entity existing = M_LA1_Detail_Repo.findByAcctnumber(la1Data.getAcct_number());
//
//			if (existing != null) {
//
//				existing.setAcct_name(la1Data.getAcct_name());
//
//				// existing.setAcct_name(la1Data.getAcct_name());
//
//				existing.setSanction_limit(la1Data.getSanction_limit());
//				existing.setAcct_balance_in_pula(la1Data.getAcct_balance_in_pula());
//
//				M_LA1_Detail_Repo.save(existing);
//
//				System.out.println("Updated successfully for ACCT_NO: " + la1Data.getAcct_number());
//				return true;
//			} else {
//				System.out.println("Record not found for Account No: " + la1Data.getAcct_number());
//				return false;
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
	
	

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/M_LA1"); // ✅ match the report name
	    System.out.println("Hello");
	    if (acctNo != null) {
	        M_LA1_Detail_Entity la1Entity = M_LA1_Detail_Repo.findByAcctnumber(acctNo);
	        if (la1Entity != null && la1Entity.getReport_date() != null) {
	            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(la1Entity.getReport_date());
	            mv.addObject("asondate", formattedDate);
	        }
	        mv.addObject("Data", la1Entity);
	    }

	    mv.addObject("displaymode", "edit");
	    mv.addObject("formmode", formMode != null ? formMode : "edit");
	    return mv;
	}
	




	public ModelAndView updateDetailEdit(String acctNo, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/M_LA1"); // ✅ match the report name

	    if (acctNo != null) {
	        M_LA1_Detail_Entity la1Entity = M_LA1_Detail_Repo.findByAcctnumber(acctNo);
	        if (la1Entity != null && la1Entity.getReport_date() != null) {
	            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(la1Entity.getReport_date());
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
	        String acctNo = request.getParameter("acct_number");
	        String provisionStr = request.getParameter("acct_balance_in_pula");
	        String sanction_limit = request.getParameter("sanction_limit");
	        String acctName = request.getParameter("acct_name");
	        String reportDateStr = request.getParameter("report_Date");

	        logger.info("Received update for ACCT_NO: {}", acctNo);

	        M_LA1_Detail_Entity existing = M_LA1_Detail_Repo.findByAcctnumber(acctNo);
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
	            if (existing.getAcct_balance_in_pula() == null ||
	                existing.getAcct_balance_in_pula().compareTo(newProvision) != 0) {
	                existing.setAcct_balance_in_pula(newProvision);
	                isChanged = true;
	                logger.info("Provision updated to {}", newProvision);
	            }
	        }
	        
	        if (sanction_limit != null && !sanction_limit.isEmpty()) {
	            BigDecimal newSanctionLimit = new BigDecimal(sanction_limit);
	            if (existing.getSanction_limit() == null ||
	                existing.getSanction_limit().compareTo(newSanctionLimit) != 0) {
	                existing.setSanction_limit(newSanctionLimit);
	                isChanged = true;
	                logger.info("Sanction limit updated to {}", newSanctionLimit);
	            }
	        }

	        if (isChanged) {
	            M_LA1_Detail_Repo.save(existing);
	            logger.info("Record updated successfully for account {}", acctNo);

	            // Format date for procedure
	            String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
	                    .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

	            // Run summary procedure after commit
	            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
	                @Override
	                public void afterCommit() {
	                    try {
	                        logger.info("Transaction committed — calling BRRS_M_LA1_SUMMARY_PROCEDURE({})",
	                                formattedDate);
	                        jdbcTemplate.update("BEGIN BRRS_M_LA1_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
	        logger.error("Error updating M_LA1 record", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error updating record: " + e.getMessage());
	    }
	}

}
