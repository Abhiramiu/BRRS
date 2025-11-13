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

import com.bornfire.brrs.entities.BRRS_M_LIQ_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_LIQ_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_LIQ_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_LIQ_Manual_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_LIQ_Manual_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_LIQ_Summary_Repo;
import com.bornfire.brrs.entities.M_CA2_Detail_Entity;
import com.bornfire.brrs.entities.M_LIQ_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_LIQ_Archival_Manual_Summary_Entity;
import com.bornfire.brrs.entities.M_LIQ_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_LIQ_Detail_Entity;
import com.bornfire.brrs.entities.M_LIQ_Manual_Summary_Entity;
import com.bornfire.brrs.entities.M_LIQ_Summary_Entity;

@Component
@Service

public class BRRS_M_LIQ_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_LIQ_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_LIQ_Detail_Repo brrs_m_liq_detail_Repo;

	@Autowired
	BRRS_M_LIQ_Summary_Repo brrs_m_liq_Summary_Repo;

	@Autowired
	BRRS_M_LIQ_Archival_Detail_Repo m_liq_Archival_Detail_Repo;

	@Autowired
	BRRS_M_LIQ_Archival_Summary_Repo m_liq_Archival_Summary_Repo;

	@Autowired
	BRRS_M_LIQ_Manual_Summary_Repo m_liq_Manual_Summary_Repo;

	@Autowired
	BRRS_M_LIQ_Manual_Archival_Summary_Repo m_liq_Manual_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_LIQView(String reportId, String fromdate, String todate, String currency, String dtltype,
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
			List<M_LIQ_Archival_Summary_Entity> T1Master = new ArrayList<M_LIQ_Archival_Summary_Entity>();
			List<M_LIQ_Archival_Manual_Summary_Entity> T2Master = new ArrayList<M_LIQ_Archival_Manual_Summary_Entity>();
			System.out.println(version);
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();

				T1Master = m_liq_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
				T2Master = m_liq_Manual_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate),
						version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);
		} else {

			List<M_LIQ_Summary_Entity> T1Master = new ArrayList<M_LIQ_Summary_Entity>();
			List<M_LIQ_Manual_Summary_Entity> T2Master = new ArrayList<M_LIQ_Manual_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = brrs_m_liq_Summary_Repo.getdatabydateList(dateformat.parse(todate));
				T2Master = m_liq_Manual_Summary_Repo.getdatabydateList(dateformat.parse(todate));
				System.out.println("T2Master size " + T2Master.size());
				mv.addObject("report_date", dateformat.format(d1));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		// mv.addObject("reportmaster", T1Master);
		// mv.addObject("reportsflag", "reportsflag");
		// mv.addObject("menu", reportId);

		mv.setViewName("BRRS/M_LIQ");

		mv.addObject("displaymode", "summary");

		System.out.println("scv" + mv.getViewName());

		return mv;

	}

	public ModelAndView getM_LIQcurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {

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

			String reportLable = null;
			String reportAddlCriteria_1 = null;
			// ✅ Split filter string into rowId & columnId
			if (filter != null && filter.contains(",")) {
				String[] parts = filter.split(",");
				if (parts.length >= 2) {
					reportLable = parts[0];
					reportAddlCriteria_1 = parts[1];
				}
			}

			System.out.println(type);
			if ("ARCHIVAL".equals(type) && version != null) {
				System.out.println(type);
				// 🔹 Archival branch
				List<M_LIQ_Archival_Detail_Entity> T1Dt1;
				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = m_liq_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1, parsedDate, version);
				} else {
					T1Dt1 = m_liq_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<M_LIQ_Detail_Entity> T1Dt1;

				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = brrs_m_liq_detail_Repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1, parsedDate);
				} else {
					T1Dt1 = brrs_m_liq_detail_Repo.getdatabydateList(parsedDate, currentPage, pageSize);
					totalPages = brrs_m_liq_detail_Repo.getdatacount(parsedDate);
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

		// Page<Object> T1Dt1Page = new PageImpl<Object>(pagedlist,
		// PageRequest.of(currentPage, pageSize), T1Dt1.size());
		// mv.addObject("reportdetails", T1Dt1Page.getContent());
		// mv.addObject("reportmaster1", qr);
		// mv.addObject("singledetail", new T1CurProdDetail());

		// ✅ Common attributes
		mv.setViewName("BRRS/M_LIQ");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}

	public byte[] getM_LIQExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_LIQARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data

		List<M_LIQ_Summary_Entity> dataList = brrs_m_liq_Summary_Repo.getdatabydateList(dateformat.parse(todate));
		List<M_LIQ_Manual_Summary_Entity> dataList1 = m_liq_Manual_Summary_Repo
				.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LIQ report. Returning empty result.");
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
					M_LIQ_Summary_Entity record = dataList.get(i);
					M_LIQ_Manual_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					row = sheet.getRow(11);

					// Column 2 - total
					Cell cellE = row.createCell(4);
					if (record.getR12_total() != null) {
						cellE.setCellValue(record.getR12_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR13_total() != null) {
						cellE.setCellValue(record.getR13_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR15_total() != null) {
						cellE.setCellValue(record.getR15_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR16_total() != null) {
						cellE.setCellValue(record1.getR16_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR17_total() != null) {
						cellE.setCellValue(record.getR17_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR18_total() != null) {
						cellE.setCellValue(record.getR18_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR19_total() != null) {
						cellE.setCellValue(record1.getR19_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR20_total() != null) {
						cellE.setCellValue(record1.getR20_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row21
					
					row = sheet.getRow(20);
					if (row == null) row = sheet.createRow(20);

					// Reuse existing cell if it exists, otherwise create new
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);

					if (record.getR21_total() != null) {
					    cellE.setCellValue(record.getR21_total().doubleValue());
					} else {
					    cellE.setCellValue(0); // or leave previous value if preferred
					}
					/*
					 * row = sheet.getRow(20);
					 * 
					 * // Column 2 - total cellE = row.createCell(4); if (record.getR21_total() !=
					 * null) { cellE.setCellValue(record.getR21_total().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); }
					 */

					// row22
					row = sheet.getRow(21);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR22_total() != null) {
						cellE.setCellValue(record1.getR22_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR23_total() != null) {
						cellE.setCellValue(record1.getR23_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					
					
					
					// row25
					row = sheet.getRow(24);
					if (row == null) row = sheet.createRow(24);

					// Reuse existing cell if it exists, otherwise create new
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);

					if (record.getR25_total() != null) {
					    cellE.setCellValue(record.getR25_total().doubleValue());
					} else {
					    cellE.setCellValue(0); // or leave as is if you prefer
					}
					/*
					 * // row25 row = sheet.getRow(24);
					 * 
					 * // Column 2 - total cellE = row.createCell(4); if (record.getR25_total() !=
					 * null) { cellE.setCellValue(record.getR25_total().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); }
					 */

					// row26
					row = sheet.getRow(25);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR26_total() != null) {
						cellE.setCellValue(record1.getR26_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);
					// Column 1 - product name

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR30_total() != null) {
						cellE.setCellValue(record.getR30_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR31_total() != null) {
						cellE.setCellValue(record.getR31_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row32
					
					row = sheet.getRow(31);
					if (row == null) row = sheet.createRow(31);

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);

					if (record.getR32_total() != null) {
					    cellE.setCellValue(record.getR32_total().doubleValue());
					} else {
					    cellE.setCellValue(0); // or leave previous value
					}
					
					
					/*
					 * row = sheet.getRow(31);
					 * 
					 * // Column 2 - total cellE = row.createCell(4); if (record.getR32_total() !=
					 * null) { cellE.setCellValue(record.getR32_total().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); }
					 */

					
					// row34
					row = sheet.getRow(33);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR34_total() != null) {
						cellE.setCellValue(record.getR34_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					
					// row35
					row = sheet.getRow(34);
					if (row == null) row = sheet.createRow(34);

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);

					if (record.getR35_total() != null) {
					    cellE.setCellValue(record.getR35_total().doubleValue());
					} else {
					    cellE.setCellValue(0); // or leave previous value
					}
					/*
					 * // row35 row = sheet.getRow(34);
					 * 
					 * // Column 2 - total cellE = row.createCell(4); if (record.getR35_total() !=
					 * null) { cellE.setCellValue(record.getR35_total().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); }
					 */

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

	public byte[] getM_LIQDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for M_LIQ Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getM_LIQDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_LIQDetails");

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
			String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

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
			System.out.println("Parsed Date: " + parsedToDate);
			List<M_LIQ_Detail_Entity> reportData = brrs_m_liq_detail_Repo.getdatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LIQ_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCust_id());
					row.createCell(1).setCellValue(item.getAcct_number());
					row.createCell(2).setCellValue(item.getAcct_name());
					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcct_balance_in_pula() != null) {
						balanceCell.setCellValue(item.getAcct_balance_in_pula().doubleValue());
					} else {
						balanceCell.setCellValue(0.000);
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
				logger.info("No data found for M_LIQ — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_LIQ Excel", e);
			return new byte[0];
		}
	}

	public List<Object> getM_LIQArchival() {
		List<Object> M_LIQArchivallist = new ArrayList<>();
		try {
			M_LIQArchivallist = m_liq_Archival_Summary_Repo.getM_LIQarchival();
			M_LIQArchivallist = m_liq_Manual_Archival_Summary_Repo.getM_LIQmanualarchival();
			System.out.println("countser" + M_LIQArchivallist.size());
			System.out.println("countser" + M_LIQArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_LIQ Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_LIQArchivallist;
	}

	public byte[] getExcelM_LIQARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<M_LIQ_Archival_Summary_Entity> dataList = m_liq_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		List<M_LIQ_Archival_Manual_Summary_Entity> dataList1 = m_liq_Manual_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LIQ report. Returning empty result.");
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
					M_LIQ_Archival_Summary_Entity record = dataList.get(i);
					M_LIQ_Archival_Manual_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					row = sheet.getRow(11);

					// Column 2 - total
					Cell cellE = row.createCell(4);
					if (record.getR12_total() != null) {
						cellE.setCellValue(record.getR12_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR13_total() != null) {
						cellE.setCellValue(record.getR13_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR15_total() != null) {
						cellE.setCellValue(record.getR15_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR16_total() != null) {
						cellE.setCellValue(record1.getR16_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR17_total() != null) {
						cellE.setCellValue(record.getR17_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR18_total() != null) {
						cellE.setCellValue(record.getR18_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR19_total() != null) {
						cellE.setCellValue(record1.getR19_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR20_total() != null) {
						cellE.setCellValue(record1.getR20_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row21
					
					row = sheet.getRow(20);
					if (row == null) row = sheet.createRow(20);

					// Reuse existing cell if it exists, otherwise create new
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);

					if (record.getR21_total() != null) {
					    cellE.setCellValue(record.getR21_total().doubleValue());
					} else {
					    cellE.setCellValue(0); // or leave previous value if preferred
					}
					/*
					 * row = sheet.getRow(20);
					 * 
					 * // Column 2 - total cellE = row.createCell(4); if (record.getR21_total() !=
					 * null) { cellE.setCellValue(record.getR21_total().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); }
					 */

					// row22
					row = sheet.getRow(21);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR22_total() != null) {
						cellE.setCellValue(record1.getR22_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR23_total() != null) {
						cellE.setCellValue(record1.getR23_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					
					
					
					// row25
					row = sheet.getRow(24);
					if (row == null) row = sheet.createRow(24);

					// Reuse existing cell if it exists, otherwise create new
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);

					if (record.getR25_total() != null) {
					    cellE.setCellValue(record.getR25_total().doubleValue());
					} else {
					    cellE.setCellValue(0); // or leave as is if you prefer
					}
					/*
					 * // row25 row = sheet.getRow(24);
					 * 
					 * // Column 2 - total cellE = row.createCell(4); if (record.getR25_total() !=
					 * null) { cellE.setCellValue(record.getR25_total().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); }
					 */

					// row26
					row = sheet.getRow(25);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record1.getR26_total() != null) {
						cellE.setCellValue(record1.getR26_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);
					// Column 1 - product name

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR30_total() != null) {
						cellE.setCellValue(record.getR30_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR31_total() != null) {
						cellE.setCellValue(record.getR31_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row32
					
					row = sheet.getRow(31);
					if (row == null) row = sheet.createRow(31);

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);

					if (record.getR32_total() != null) {
					    cellE.setCellValue(record.getR32_total().doubleValue());
					} else {
					    cellE.setCellValue(0); // or leave previous value
					}
					
					
					/*
					 * row = sheet.getRow(31);
					 * 
					 * // Column 2 - total cellE = row.createCell(4); if (record.getR32_total() !=
					 * null) { cellE.setCellValue(record.getR32_total().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); }
					 */

					
					// row34
					row = sheet.getRow(33);

					// Column 2 - total
					cellE = row.createCell(4);
					if (record.getR34_total() != null) {
						cellE.setCellValue(record.getR34_total().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					
					// row35
					row = sheet.getRow(34);
					if (row == null) row = sheet.createRow(34);

					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);

					if (record.getR35_total() != null) {
					    cellE.setCellValue(record.getR35_total().doubleValue());
					} else {
					    cellE.setCellValue(0); // or leave previous value
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

	public byte[] getM_LIQDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for M_LIQ ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("MLIQDetail");

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
			String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE",  "REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

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
			List<M_LIQ_Archival_Detail_Entity> reportData = m_liq_Archival_Detail_Repo.getdatabydateList(parsedToDate,
					version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LIQ_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCust_id());
					row.createCell(1).setCellValue(item.getAcct_number());
					 row.createCell(2).setCellValue(item.getAcct_name()); 

// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcct_balance_in_pula() != null) {
						balanceCell.setCellValue(item.getAcct_balance_in_pula().doubleValue());
					} else {
						balanceCell.setCellValue(0.000);
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
				logger.info("No data found for M_LIQ — only header will be written.");
			}

// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_LIQExcel", e);
			return new byte[0];
		}
	}

	public void updateReport1(M_LIQ_Manual_Summary_Entity updatedEntity) {
		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		M_LIQ_Manual_Summary_Entity existing = m_liq_Manual_Summary_Repo.findById(updatedEntity.getReport_date())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

		try {
			// 🔹 Only these rows
			int[] specialRows = { 16, 19, 20, 22, 23, 26 };

			for (int i : specialRows) {
				String prefix = "R" + i + "_";
				String[] fields = { "product", "total" };

				for (String field : fields) {
					String getterName = "get" + prefix + field; // e.g., getR16_product
					String setterName = "set" + prefix + field; // e.g., setR16_product

					try {
						Method getter = M_LIQ_Manual_Summary_Entity.class.getMethod(getterName);
						Method setter = M_LIQ_Manual_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// If getter/setter is missing, just skip
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 3️⃣ Save updated entity
		m_liq_Manual_Summary_Repo.save(existing);
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/M_LIQ"); // ✅ match the report name
	    System.out.println("Hello");
	    if (acctNo != null) {
	    	M_LIQ_Detail_Entity liqEntity = brrs_m_liq_detail_Repo.findByAcctnumber(acctNo);
	        if (liqEntity != null && liqEntity.getReport_date() != null) {
	            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(liqEntity.getReport_date());
	            mv.addObject("asondate", formattedDate);
	        }
	        mv.addObject("Data", liqEntity);
	    }

	    mv.addObject("displaymode", "edit");
	    mv.addObject("formmode", formMode != null ? formMode : "edit");
	    return mv;
	}





	public ModelAndView updateDetailEdit(String acctNo, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/M_LIQ"); // ✅ match the report name

	    if (acctNo != null) {
	    	M_LIQ_Detail_Entity liqEntity = brrs_m_liq_detail_Repo.findByAcctnumber(acctNo);
	        if (liqEntity != null && liqEntity.getReport_date() != null) {
	            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(liqEntity.getReport_date());
	            mv.addObject("asondate", formattedDate);
	            System.out.println(formattedDate);
	        }
	        mv.addObject("Data", liqEntity);
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

	        logger.info("Received update for ACCT_NO: {}", acctNo);

	        M_LIQ_Detail_Entity existing = brrs_m_liq_detail_Repo.findByAcctnumber(acctNo);
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
	                logger.info("Balance updated to {}", newProvision);
	            }
	        }
	        
	        

	        if (isChanged) {
	        	brrs_m_liq_detail_Repo.save(existing);
	            logger.info("Record updated successfully for account {}", acctNo);

	            // Format date for procedure
	            String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
	                    .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

	            // Run summary procedure after commit
	            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
	                @Override
	                public void afterCommit() {
	                    try {
	                        logger.info("Transaction committed — calling BRRS_M_LIQ_SUMMARY_PROCEDURE({})",
	                                formattedDate);
	                        jdbcTemplate.update("BEGIN BRRS_M_LIQ_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
	        logger.error("Error updating M_LIQ record", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error updating record: " + e.getMessage());
	    }
	}

	
}