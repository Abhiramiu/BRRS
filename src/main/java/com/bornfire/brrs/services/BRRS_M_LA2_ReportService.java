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
import java.sql.CallableStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.bornfire.brrs.entities.BRRS_M_LA2_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_LA2_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_LA2_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_LA2_Summary_Repo;
import com.bornfire.brrs.entities.MDISB1_Detail_Entity;
import com.bornfire.brrs.entities.M_LA2_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_LA2_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_LA2_Detail_Entity;
import com.bornfire.brrs.entities.M_LA2_Summary_Entity;

@Component
@Service

public class BRRS_M_LA2_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_LA2_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_LA2_Summary_Repo BRRS_M_LA2_Summary_Repo;

	@Autowired
	BRRS_M_LA2_Detail_Repo M_LA2_Detail_Repo;

	@Autowired
	BRRS_M_LA2_Archival_Detail_Repo M_LA2_Archival_Detail_Repo;

	@Autowired
	BRRS_M_LA2_Archival_Summary_Repo M_LA2_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_LA2View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<M_LA2_Archival_Summary_Entity> T1Master = new ArrayList<M_LA2_Archival_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = M_LA2_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);

		} else {

			List<M_LA2_Summary_Entity> T1Master = new ArrayList<M_LA2_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_LA2_Summary_Repo.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

		mv.setViewName("BRRS/M_LA2");

		// mv.addObject("reportmaster", T1Master);
		mv.addObject("displaymode", "summary");
		// mv.addObject("reportsflag", "reportsflag");
		// mv.addObject("menu", reportId);
		System.out.println("scv" + mv.getViewName());

		return mv;

	}

	public ModelAndView getM_LA2currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {

		ModelAndView mv = new ModelAndView("BRRS/M_LA2");
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

				List<M_LA2_Archival_Detail_Entity> detailList;

// 🔹 Filtered (ROWID + COLUMNID)
				if (rowId != null && !rowId.isEmpty()
						&& (isNotEmpty(columnId) || isNotEmpty(columnId1) || isNotEmpty(columnId2))) {

					logger.info("➡ ARCHIVAL DETAIL QUERY TRIGGERED (with filters)");
					detailList = M_LA2_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate,version);

				} else {
					logger.info("➡ ARCHIVAL LIST QUERY TRIGGERED (with pagination)");
					detailList = M_LA2_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
					//totalRecords = M_LA2_Archival_Detail_Repo.getdatacount(parsedDate);
					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", detailList);
				mv.addObject("reportmaster12", detailList);
				logger.info("ARCHIVAL COUNT: {}", (detailList != null ? detailList.size() : 0));

			} else {
// ✅ CURRENT DATA BRANCH
				logger.info("Fetching CURRENT data for M_LA2");

				List<M_LA2_Detail_Entity> detailList;

				if (rowId != null && !rowId.isEmpty()
						&& (isNotEmpty(columnId) || isNotEmpty(columnId1) || isNotEmpty(columnId2))) {

					logger.info("➡ CURRENT DETAIL QUERY TRIGGERED (with filters)");
					detailList = M_LA2_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId,
							parsedDate);

				} else {
					logger.info("➡ CURRENT LIST QUERY TRIGGERED (with pagination)");
					detailList = M_LA2_Detail_Repo.getdatabydateList(parsedDate);
					totalRecords = M_LA2_Detail_Repo.getdatacount(parsedDate);
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
			logger.error("Unexpected error in getM_LA2currentDtl", e);
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

	public byte[] BRRS_M_LA2Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		// ARCHIVAL check
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version != null) {
					logger.info("Service: Generating ARCHIVAL report for version {}", version);
					return getExcelM_LA2ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
				}

		List<M_LA2_Summary_Entity> dataList = BRRS_M_LA2_Summary_Repo.getdatabydateList(dateformat.parse(todate));

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
					M_LA2_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}


					
							// row12
							// Column B
							Cell cell2 = row.createCell(1);
							if (record.getR12_TOTAL() != null) {
								cell2.setCellValue(record.getR12_TOTAL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}
							
							// row13
							row = sheet.getRow(12);
							// Column B
							Cell R13cell2 = row.getCell(1);
							if (record.getR13_TOTAL() != null) {
								R13cell2.setCellValue(record.getR13_TOTAL().doubleValue());
								R13cell2.setCellStyle(numberStyle);
							} else {
								R13cell2.setCellValue("");
								R13cell2.setCellStyle(textStyle);

							}
							
							// row14
							row = sheet.getRow(13);
							// Column B
							Cell R14cell2 = row.getCell(1);
							if (record.getR14_TOTAL() != null) {
								R14cell2.setCellValue(record.getR14_TOTAL().doubleValue());
								R14cell2.setCellStyle(numberStyle);
							} else {
								R14cell2.setCellValue("");
								R14cell2.setCellStyle(textStyle);

							}
							
							// row15
							row = sheet.getRow(14);
							// Column B
							Cell R15cell2 = row.getCell(1);
							if (record.getR15_TOTAL() != null) {
								R15cell2.setCellValue(record.getR15_TOTAL().doubleValue());
								R15cell2.setCellStyle(numberStyle);
							} else {
								R15cell2.setCellValue("");
								R15cell2.setCellStyle(textStyle);

							}
							
							// row16
							row = sheet.getRow(15);
							// Column B
							Cell R16cell2 = row.getCell(1);
							if (record.getR16_TOTAL() != null) {
								R16cell2.setCellValue(record.getR16_TOTAL().doubleValue());
								R16cell2.setCellStyle(numberStyle);
							} else {
								R16cell2.setCellValue("");
								R16cell2.setCellStyle(textStyle);

							}
							
							// row17
							row = sheet.getRow(16);
							// Column B
							Cell R17cell2 = row.getCell(1);
							if (record.getR17_TOTAL() != null) {
								R17cell2.setCellValue(record.getR17_TOTAL().doubleValue());
								R17cell2.setCellStyle(numberStyle);
							} else {
								R17cell2.setCellValue("");
								R17cell2.setCellStyle(textStyle);

							}
							
							// row18
							row = sheet.getRow(17);
							// Column B
							Cell R18cell2 = row.getCell(1);
							if (record.getR18_TOTAL() != null) {
								R18cell2.setCellValue(record.getR18_TOTAL().doubleValue());
								R18cell2.setCellStyle(numberStyle);
							} else {
								R18cell2.setCellValue("");
								R18cell2.setCellStyle(textStyle);

							}
							
							// row19
							row = sheet.getRow(18);
							// Column B
							Cell R19cell2 = row.getCell(1);
							if (record.getR19_TOTAL() != null) {
								R19cell2.setCellValue(record.getR19_TOTAL().doubleValue());
								R19cell2.setCellStyle(numberStyle);
							} else {
								R19cell2.setCellValue("");
								R19cell2.setCellStyle(textStyle);

							}
							
							// row20
							row = sheet.getRow(19);
							// Column B
							Cell R20cell2 = row.getCell(1);
							if (record.getR20_TOTAL() != null) {
								R20cell2.setCellValue(record.getR20_TOTAL().doubleValue());
								R20cell2.setCellStyle(numberStyle);
							} else {
								R20cell2.setCellValue("");
								R20cell2.setCellStyle(textStyle);

							}
							
							// row21
							row = sheet.getRow(20);
							// Column B
							Cell R21cell2 = row.getCell(1);
							if (record.getR21_TOTAL() != null) {
								R21cell2.setCellValue(record.getR21_TOTAL().doubleValue());
								R21cell2.setCellStyle(numberStyle);
							} else {
								R21cell2.setCellValue("");
								R21cell2.setCellStyle(textStyle);

							}
							
							// row22
							row = sheet.getRow(21);
							// Column B
							Cell R22cell2 = row.getCell(1);
							if (record.getR22_TOTAL() != null) {
								R22cell2.setCellValue(record.getR22_TOTAL().doubleValue());
								R22cell2.setCellStyle(numberStyle);
							} else {
								R22cell2.setCellValue("");
								R22cell2.setCellStyle(textStyle);

							}
							
							// row23
							row = sheet.getRow(22);
							// Column B
							Cell R23cell2 = row.getCell(1);
							if (record.getR23_TOTAL() != null) {
								R23cell2.setCellValue(record.getR23_TOTAL().doubleValue());
								R23cell2.setCellStyle(numberStyle);
							} else {
								R23cell2.setCellValue("");
								R23cell2.setCellStyle(textStyle);

							}
							
							// row24
							row = sheet.getRow(23);
							// Column B
							Cell R24cell2 = row.getCell(1);
							if (record.getR24_TOTAL() != null) {
								R24cell2.setCellValue(record.getR24_TOTAL().doubleValue());
								R24cell2.setCellStyle(numberStyle);
							} else {
								R24cell2.setCellValue("");
								R24cell2.setCellStyle(textStyle);

							}
							
							
							// row25
							row = sheet.getRow(24);
							// Column B
							Cell R25cell2 = row.getCell(1);
							if (record.getR25_TOTAL() != null) {
								R25cell2.setCellValue(record.getR25_TOTAL().doubleValue());
								R25cell2.setCellStyle(numberStyle);
							} else {
								R25cell2.setCellValue("");
								R25cell2.setCellStyle(textStyle);

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
	public byte[] BRRS_M_LA2DetailExcel(String filename, String fromdate, String todate,
	        String currency, String dtltype, String type, String version) {

	    try {
	        logger.info("Generating Excel for M_LA2 Details...");
	        System.out.println("came to Detail download service");

	        // ✅ FIX 1: use logical AND
	        if ("ARCHIVAL".equals(type) && version != null) {
	            return getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type, version);
	        }

	        try (XSSFWorkbook workbook = new XSSFWorkbook();
	             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

	            XSSFSheet sheet = workbook.createSheet("M_LA2Detail");

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

	            CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
	            rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
	            rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

	            CellStyle dataStyle = workbook.createCellStyle();
	            dataStyle.setAlignment(HorizontalAlignment.LEFT);
	            dataStyle.setBorderTop(border);
	            dataStyle.setBorderBottom(border);
	            dataStyle.setBorderLeft(border);
	            dataStyle.setBorderRight(border);

	            // ✅ FIX 2: correct decimal format
	            CellStyle balanceStyle = workbook.createCellStyle();
	            balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
	            balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.000"));
	            balanceStyle.setBorderTop(border);
	            balanceStyle.setBorderBottom(border);
	            balanceStyle.setBorderLeft(border);
	            balanceStyle.setBorderRight(border);

	            String[] headers = {
	                "CUST ID", "ACCT NO", "ACCT NAME",
	                "ACCT BALANCE PULA", "REPORT LABEL",
	                "REPORT_ADDL_CRITERIA_1", "REPORT_DATE"
	            };

	            XSSFRow headerRow = sheet.createRow(0);
	            for (int i = 0; i < headers.length; i++) {
	                Cell cell = headerRow.createCell(i);
	                cell.setCellValue(headers[i]);
	                cell.setCellStyle(i == 3 ? rightAlignedHeaderStyle : headerStyle);
	                sheet.setColumnWidth(i, 5000);
	            }

	            Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
	            List<M_LA2_Detail_Entity> reportData =
	                    M_LA2_Detail_Repo.getdatabydateList(parsedToDate);

	            int rowIndex = 1;
	            if (reportData != null) {
	                for (M_LA2_Detail_Entity item : reportData) {
	                    XSSFRow row = sheet.createRow(rowIndex++);

	                    row.createCell(0).setCellValue(item.getCustId());
	                    row.createCell(1).setCellValue(item.getAcctNumber());
	                    row.createCell(2).setCellValue(item.getAcctName());

	                    Cell balCell = row.createCell(3);
	                    balCell.setCellValue(
	                            item.getAcctBalanceInPula() != null
	                                    ? item.getAcctBalanceInPula().doubleValue()
	                                    : 0.0);
	                    balCell.setCellStyle(balanceStyle);

	                    row.createCell(4).setCellValue(item.getReportLabel());
	                    row.createCell(5).setCellValue(item.getReportAddlCriteria1());
	                    row.createCell(6).setCellValue(
	                            item.getReportDate() != null
	                                    ? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
	                                    : "");

	                    for (int j = 0; j < 7; j++) {
	                        if (j != 3) {
	                            row.getCell(j).setCellStyle(dataStyle);
	                        }
	                    }
	                }
	            }

	            workbook.write(bos);
	            logger.info("Excel generation completed with {} row(s).",
	                    reportData != null ? reportData.size() : 0);

	            return bos.toByteArray();
	        }

	    } catch (Exception e) {
	        logger.error("Error generating M_LA2 Excel", e);
	        return new byte[0];
	    }
	}

	public List<Object> getM_LA2Archival() {
		List<Object> M_LA2Archivallist = new ArrayList<>();
		try {
			M_LA2Archivallist = M_LA2_Archival_Summary_Repo.getM_LA2archival();
			System.out.println("countser" + M_LA2Archivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_LA2 Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_LA2Archivallist;
	}

	public byte[] getExcelM_LA2ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_LA2_Archival_Summary_Entity> dataList = M_LA2_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LA2 report. Returning empty result.");
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
					M_LA2_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}



							// row12
							// Column B
							Cell cell2 = row.createCell(1);
							if (record.getR12_TOTAL() != null) {
								cell2.setCellValue(record.getR12_TOTAL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}
							
							// row13
							row = sheet.getRow(12);
							// Column B
							Cell R13cell2 = row.getCell(1);
							if (record.getR13_TOTAL() != null) {
								R13cell2.setCellValue(record.getR13_TOTAL().doubleValue());
								R13cell2.setCellStyle(numberStyle);
							} else {
								R13cell2.setCellValue("");
								R13cell2.setCellStyle(textStyle);

							}
							
							// row14
							row = sheet.getRow(13);
							// Column B
							Cell R14cell2 = row.getCell(1);
							if (record.getR14_TOTAL() != null) {
								R14cell2.setCellValue(record.getR14_TOTAL().doubleValue());
								R14cell2.setCellStyle(numberStyle);
							} else {
								R14cell2.setCellValue("");
								R14cell2.setCellStyle(textStyle);

							}
							
							// row15
							row = sheet.getRow(14);
							// Column B
							Cell R15cell2 = row.getCell(1);
							if (record.getR15_TOTAL() != null) {
								R15cell2.setCellValue(record.getR15_TOTAL().doubleValue());
								R15cell2.setCellStyle(numberStyle);
							} else {
								R15cell2.setCellValue("");
								R15cell2.setCellStyle(textStyle);

							}
							
							// row16
							row = sheet.getRow(15);
							// Column B
							Cell R16cell2 = row.getCell(1);
							if (record.getR16_TOTAL() != null) {
								R16cell2.setCellValue(record.getR16_TOTAL().doubleValue());
								R16cell2.setCellStyle(numberStyle);
							} else {
								R16cell2.setCellValue("");
								R16cell2.setCellStyle(textStyle);

							}
							
							// row17
							row = sheet.getRow(16);
							// Column B
							Cell R17cell2 = row.getCell(1);
							if (record.getR17_TOTAL() != null) {
								R17cell2.setCellValue(record.getR17_TOTAL().doubleValue());
								R17cell2.setCellStyle(numberStyle);
							} else {
								R17cell2.setCellValue("");
								R17cell2.setCellStyle(textStyle);

							}
							
							// row18
							row = sheet.getRow(17);
							// Column B
							Cell R18cell2 = row.getCell(1);
							if (record.getR18_TOTAL() != null) {
								R18cell2.setCellValue(record.getR18_TOTAL().doubleValue());
								R18cell2.setCellStyle(numberStyle);
							} else {
								R18cell2.setCellValue("");
								R18cell2.setCellStyle(textStyle);

							}
							
							// row19
							row = sheet.getRow(18);
							// Column B
							Cell R19cell2 = row.getCell(1);
							if (record.getR19_TOTAL() != null) {
								R19cell2.setCellValue(record.getR19_TOTAL().doubleValue());
								R19cell2.setCellStyle(numberStyle);
							} else {
								R19cell2.setCellValue("");
								R19cell2.setCellStyle(textStyle);

							}
							
							// row20
							row = sheet.getRow(19);
							// Column B
							Cell R20cell2 = row.getCell(1);
							if (record.getR20_TOTAL() != null) {
								R20cell2.setCellValue(record.getR20_TOTAL().doubleValue());
								R20cell2.setCellStyle(numberStyle);
							} else {
								R20cell2.setCellValue("");
								R20cell2.setCellStyle(textStyle);

							}
							
							// row21
							row = sheet.getRow(20);
							// Column B
							Cell R21cell2 = row.getCell(1);
							if (record.getR21_TOTAL() != null) {
								R21cell2.setCellValue(record.getR21_TOTAL().doubleValue());
								R21cell2.setCellStyle(numberStyle);
							} else {
								R21cell2.setCellValue("");
								R21cell2.setCellStyle(textStyle);

							}
							
							// row22
							row = sheet.getRow(21);
							// Column B
							Cell R22cell2 = row.getCell(1);
							if (record.getR22_TOTAL() != null) {
								R22cell2.setCellValue(record.getR22_TOTAL().doubleValue());
								R22cell2.setCellStyle(numberStyle);
							} else {
								R22cell2.setCellValue("");
								R22cell2.setCellStyle(textStyle);

							}
							
							// row23
							row = sheet.getRow(22);
							// Column B
							Cell R23cell2 = row.getCell(1);
							if (record.getR23_TOTAL() != null) {
								R23cell2.setCellValue(record.getR23_TOTAL().doubleValue());
								R23cell2.setCellStyle(numberStyle);
							} else {
								R23cell2.setCellValue("");
								R23cell2.setCellStyle(textStyle);

							}
							
							// row24
							row = sheet.getRow(23);
							// Column B
							Cell R24cell2 = row.getCell(1);
							if (record.getR24_TOTAL() != null) {
								R24cell2.setCellValue(record.getR24_TOTAL().doubleValue());
								R24cell2.setCellStyle(numberStyle);
							} else {
								R24cell2.setCellValue("");
								R24cell2.setCellStyle(textStyle);

							}
							
							
							// row25
							row = sheet.getRow(24);
							// Column B
							Cell R25cell2 = row.getCell(1);
							if (record.getR25_TOTAL() != null) {
								R25cell2.setCellValue(record.getR25_TOTAL().doubleValue());
								R25cell2.setCellStyle(numberStyle);
							} else {
								R25cell2.setCellValue("");
								R25cell2.setCellStyle(textStyle);

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

	    logger.info("Generating Excel for BRRS_M_LA2 ARCHIVAL Details...");

	    // Validate ARCHIVAL request
	    if (!"ARCHIVAL".equalsIgnoreCase(type) || version == null || version.trim().isEmpty()) {
	        logger.warn("Invalid request: type={} version={}", type, version);
	        return new byte[0];
	    }

	    try (XSSFWorkbook workbook = new XSSFWorkbook();
	         ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

	        XSSFSheet sheet = workbook.createSheet("M_LA2Details");

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

	        // Right aligned header
	        CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
	        rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
	        rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

	        // Data style
	        CellStyle dataStyle = workbook.createCellStyle();
	        dataStyle.setAlignment(HorizontalAlignment.LEFT);
	        dataStyle.setBorderTop(border);
	        dataStyle.setBorderBottom(border);
	        dataStyle.setBorderLeft(border);
	        dataStyle.setBorderRight(border);

	        // Balance style
	        CellStyle balanceStyle = workbook.createCellStyle();
	        balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
	        balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
	        balanceStyle.setBorderTop(border);
	        balanceStyle.setBorderBottom(border);
	        balanceStyle.setBorderLeft(border);
	        balanceStyle.setBorderRight(border);

	        // Headers
	        String[] headers = {
	                "CUST ID", "ACCT NO", "ACCT NAME",
	                "ACCT BALANCE PULA", "REPORT LABEL",
	                "REPORT_ADDL_CRITERIA_1", "REPORT_DATE"
	        };

	        XSSFRow headerRow = sheet.createRow(0);
	        for (int i = 0; i < headers.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(headers[i]);
	            cell.setCellStyle(i == 3 ? rightAlignedHeaderStyle : headerStyle);
	            sheet.setColumnWidth(i, 5000);
	        }

	        Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);

	        List<M_LA2_Archival_Detail_Entity> reportData =
	                M_LA2_Archival_Detail_Repo.getdatabydateList(parsedToDate, version);

	        int rowIndex = 1;
	        if (reportData != null && !reportData.isEmpty()) {
	            for (M_LA2_Archival_Detail_Entity item : reportData) {

	                XSSFRow row = sheet.createRow(rowIndex++);

	                row.createCell(0).setCellValue(item.getCustId());
	                row.createCell(1).setCellValue(item.getAcctNumber());
	                row.createCell(2).setCellValue(item.getAcctName());

	                Cell balCell = row.createCell(3);
	                balCell.setCellValue(
	                        item.getAcctBalanceInPula() != null
	                                ? item.getAcctBalanceInPula().doubleValue()
	                                : 0.0);
	                balCell.setCellStyle(balanceStyle);

	                row.createCell(4).setCellValue(item.getReportLabel());
	                row.createCell(5).setCellValue(item.getReportAddlCriteria1());
	                row.createCell(6).setCellValue(
	                        item.getReportDate() != null
	                                ? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
	                                : "");

	                for (int j = 0; j < 7; j++) {
	                    if (j != 3) {
	                        row.getCell(j).setCellStyle(dataStyle);
	                    }
	                }
	            }
	        } else {
	            logger.info("No archival data found for M_LA2.");
	        }

	        workbook.write(bos);

	        logger.info("Excel generation completed with {} row(s).",
	                reportData != null ? reportData.size() : 0);

	        return bos.toByteArray();

	    } catch (Exception e) {
	        logger.error("Error generating M_LA2 Excel", e);
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

			if (fieldDescCell == null || fieldDescCell.getCellType() == Cell.CELL_TYPE_BLANK) {
				continue;
			}

			String fieldDesc = "";
			try {
				fieldDesc = fieldDescCell.getStringCellValue();
			} catch (IllegalStateException e) {

				FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
				CellValue cellValue = evaluator.evaluate(fieldDescCell);
				if (cellValue != null) {
					if (cellValue.getCellType() == Cell.CELL_TYPE_STRING) {
						fieldDesc = cellValue.getStringValue();
					} else if (cellValue.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						fieldDesc = String.valueOf(cellValue.getNumberValue());
					} else if (cellValue.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
						fieldDesc = String.valueOf(cellValue.getBooleanValue());
					}

				}
				if (fieldDesc.isEmpty() && fieldDescCell.getCellType() == Cell.CELL_TYPE_FORMULA) {

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
				if (cell != null && cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
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

		System.out.println("✅ M_LA2 Report data processed (Excel Row " + (START_ROW_INDEX + 1) + " to "
				+ (END_ROW_INDEX + 1) + "). Total items: " + reportData.size());
		return reportData;
	}

//	public boolean updateProvision(M_LA2_Detail_Entity la1Data) {
//		try {
//			System.out.println("Came to LA1 Service");
//
//			// ✅ Must match your entity field name exactly
//			M_LA2_Detail_Entity existing = M_LA2_Detail_Repo.findByAcctnumber(la1Data.getAcct_number());
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
//				M_LA2_Detail_Repo.save(existing);
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
	    ModelAndView mv = new ModelAndView("BRRS/M_LA2"); // ✅ match the report name
	    System.out.println("Hello");
	    if (acctNo != null) {
	        M_LA2_Detail_Entity la2Entity = M_LA2_Detail_Repo.findByAcctnumber(acctNo);
	        if (la2Entity != null && la2Entity.getReportDate() != null) {
	            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(la2Entity.getReportDate());
	            mv.addObject("asondate", formattedDate);
	        }
	        mv.addObject("Data", la2Entity);
	    }

	    mv.addObject("displaymode", "edit");
	    mv.addObject("formmode", formMode != null ? formMode : "edit");
	    return mv;
	}
	


	public void updateDetailFromForm(Date reportDate, Map<String, String> params) {

		System.out.println("Updating la2 detail table");

		for (Map.Entry<String, String> entry : params.entrySet()) {

			String key = entry.getKey();
			String value = entry.getValue();

			// Expected: R10_C2_r10_31_3_25_amt
			if (!key.matches("R\\d+_C\\d+_.*")) {
				continue;
			}

			String[] parts = key.split("_");
			String reportLabel = parts[0]; // R10
			String addlCriteria = parts[1]; // C2 or C3

			BigDecimal amount = (value == null || value.isEmpty()) ? BigDecimal.ZERO : new BigDecimal(value);

			List<M_LA2_Detail_Entity> rows = M_LA2_Detail_Repo
					.findByReportDateAndReportLableAndReportAddlCriteria1(reportDate, reportLabel, addlCriteria);

			System.out.println("Rows fetching for Reportdate is : " + reportDate + " Report label is : " + reportLabel
					+ " Column is : " + addlCriteria);
			System.out.println("data size is : " + rows.size());

			for (M_LA2_Detail_Entity row : rows) {

				// System.out.println("Row PK = " + row.getId());
				System.out.println("Before update acct = " + row.getAcctBalanceInPula());
				System.out.println("Before update modifyFlg = " + row.getModifyFlg());

				row.setAcctBalanceInPula(amount);
				row.setModifyFlg('Y');
			}

			M_LA2_Detail_Repo.saveAll(rows);
		}

		callSummaryProcedure(reportDate);
	}

	private void callSummaryProcedure(Date reportDate) {

		String sql = "{ call BRRS_M_LA2_SUMMARY_PROCEDURE(?) }";

		jdbcTemplate.update(connection -> {
			CallableStatement cs = connection.prepareCall(sql);

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			sdf.setLenient(false);

			cs.setString(1, sdf.format(reportDate));
			return cs;
		});

		System.out.println("✅ M_LA2 Summary procedure executed");
	}

	

	

}
