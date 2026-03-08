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
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_M_IRB_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_IRB_Detail_Archival_Repo;
import com.bornfire.brrs.entities.BRRS_M_IRB_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_IRB_Summary_Repo;
import com.bornfire.brrs.entities.M_IRB_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_IRB_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_IRB_Detail_Entity;
import com.bornfire.brrs.entities.M_IRB_Summary_Entity;


@Component
@Service

public class BRRS_M_IRB_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_LIQ_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_IRB_Summary_Repo brrs_m_irb_Summary_Repo;

	@Autowired
	BRRS_M_IRB_Archival_Summary_Repo m_irb_Archival_Summary_Repo;

	@Autowired
	BRRS_M_IRB_Detail_Repo brrs_m_irb_detail_Repo;

	@Autowired
	BRRS_M_IRB_Detail_Archival_Repo brrs_m_irb_archival_detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_IRBView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();

		if (type.equals("ARCHIVAL") & version != null) {
			System.out.println(type);
			List<M_IRB_Archival_Summary_Entity> T1Master = new ArrayList<M_IRB_Archival_Summary_Entity>();
			System.out.println(version);
			try {

				T1Master = m_irb_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} else {

			List<M_IRB_Summary_Entity> T1Master = new ArrayList<M_IRB_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);

				T1Master = brrs_m_irb_Summary_Repo.getdatabydateList(dateformat.parse(todate));

				System.out.println("t1 master for IRB is :" + T1Master.size());
				mv.addObject("report_date", dateformat.format(d1));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		mv.setViewName("BRRS/M_IRB");

		mv.addObject("displaymode", "summary");

		System.out.println("scv" + mv.getViewName());

		return mv;

	}

	public ModelAndView getM_IRBcurrentDtl(String reportId, String fromdate, String todate, String currency,
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
				List<M_IRB_Archival_Detail_Entity> T1Dt1;
				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = brrs_m_irb_archival_detail_Repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1,
							parsedDate, version);
				} else {
					T1Dt1 = brrs_m_irb_archival_detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<M_IRB_Detail_Entity> T1Dt1;

				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = brrs_m_irb_detail_Repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1,
							parsedDate);
				} else {
					T1Dt1 = brrs_m_irb_detail_Repo.getdatabydateList(parsedDate, currentPage, pageSize);
					totalPages = brrs_m_irb_detail_Repo.getdatacount(parsedDate);
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
		mv.setViewName("BRRS/M_IRB");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}

	
	public List<Object[]> getM_IRBArchival() {
		List<Object[]> archivalList = new ArrayList<>();
		try {
			List<M_IRB_Archival_Summary_Entity> latestArchivalList = m_irb_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_IRB_Archival_Summary_Entity entity : latestArchivalList) {
					archivalList.add(new Object[] {
							entity.getReportDate(),
							entity.getReportVersion(),
							entity.getReportResubDate()
					});
				}
				System.out.println("Fetched " + archivalList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_LIQ Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return archivalList;
	}

	public byte[] BRRS_M_IRBDetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRS_M_IRB Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRRS_IRBDetails");

//Common border style
			BorderStyle border = BorderStyle.THIN;
//Header style (left aligned)
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

//Right-aligned header style for ACCT BALANCE
			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

//Default data style (left aligned)
			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setAlignment(HorizontalAlignment.LEFT);
			dataStyle.setBorderTop(border);
			dataStyle.setBorderBottom(border);
			dataStyle.setBorderLeft(border);
			dataStyle.setBorderRight(border);

//ACCT BALANCE style (right aligned with 3 decimals)
			CellStyle balanceStyle = workbook.createCellStyle();
			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);
//Header row
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
//Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_IRB_Detail_Entity> reportData = brrs_m_irb_detail_Repo.getdatabydateList(parsedToDate);
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_IRB_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(item.getCust_id());
					row.createCell(1).setCellValue(item.getAcct_number());
					row.createCell(2).setCellValue(item.getAcct_name());
//ACCT BALANCE (right aligned, 3 decimal places)
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
//Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for BRRS_M_PI — only header will be written.");
			}
//Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();
		} catch (Exception e) {
			logger.error("Error generating BRRS_M_PI Excel", e);
			return new byte[0];
		}
	}

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_M_PI ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("MSFinP2Detail");

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
			List<M_IRB_Archival_Detail_Entity> reportData = brrs_m_irb_archival_detail_Repo
					.getdatabydateList(parsedToDate, version);
			System.out.println("Size");
			System.out.println(reportData.size());
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_IRB_Archival_Detail_Entity item : reportData) {
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
				logger.info("No data found for BRRS_M_PI — only header will be written.");
			}

// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_M_PIExcel", e);
			return new byte[0];
		}
	}

	public void MIRBUpdate(M_IRB_Summary_Entity updatedEntity) {
		System.out.println("Came to MIS UPDATE services");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		M_IRB_Summary_Entity existing = brrs_m_irb_Summary_Repo.findById(updatedEntity.getReportDate()).orElseThrow(
				() -> new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

		try {
			// 1️⃣ Loop from R10 to R16 and copy fields
			for (int i = 96; i <= 106; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "upTo1Month", "moreThan1MonthTo3Months", "moreThan3MonthTo6Months",
						"moreThan6MonthTo12Months", "moreThan12MonthTo3Years", "moreThan13YearsTo5Years",
						"moreThan5YearsTo10Years", "moreThan10Years", "nonRatioSensativeItems", "total" };

				for (String field : fields) {
					String getterName = "getR" + i + "_" + field;
					String setterName = "setR" + i + "_" + field;

					try {
						Method getter = M_IRB_Summary_Entity.class.getMethod(getterName);
						Method setter = M_IRB_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

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
		brrs_m_irb_Summary_Repo.save(existing);
	}

	// SUMMARY FORMAT EXCEL
	public byte[] BRRS_M_IRBExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type,String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		System.out.println(type);
		System.out.println(version);
		if (type.equals("ARCHIVAL") & version != null) {
			byte[] ARCHIVALreport = getExcelM_IRBARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
					version);
			return ARCHIVALreport;
		}

		List<M_IRB_Summary_Entity> dataList = brrs_m_irb_Summary_Repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_IRB report. Returning empty result.");
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

			if (!dataList.isEmpty()) {
				populateEntity1Data(sheet, dataList.get(0), textStyle, numberStyle);
			}


			int startRow = 7;


			workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}
	}

	//SUMMARY FORMAT VALUES
   private void populateEntity1Data(Sheet sheet, M_IRB_Summary_Entity record, CellStyle textStyle, CellStyle numberStyle) {

	        Row row = sheet.getRow(11) != null ? sheet.getRow(11) : sheet.createRow(11);
	        Cell R12cell1 = row.createCell(2);
	        if (record.getR12_upTo1Month() != null) {
	            R12cell1.setCellValue(record.getR12_upTo1Month().doubleValue());
	            R12cell1.setCellStyle(numberStyle);
	        } else {
	            R12cell1.setCellValue("");
	            R12cell1.setCellStyle(textStyle);
	        }

	        Cell R12cell2 = row.createCell(3);
	        if (record.getR12_moreThan1MonthTo3Months() != null) {
	            R12cell2.setCellValue(record.getR12_moreThan1MonthTo3Months().doubleValue());
	            R12cell2.setCellStyle(numberStyle);
	        } else {
	            R12cell2.setCellValue("");
	            R12cell2.setCellStyle(textStyle);
	        }

	        Cell R12cell3 = row.createCell(4);
	        if (record.getR12_moreThan3MonthTo6Months() != null) {
	            R12cell3.setCellValue(record.getR12_moreThan3MonthTo6Months().doubleValue());
	            R12cell3.setCellStyle(numberStyle);
	        } else {
	            R12cell3.setCellValue("");
	            R12cell3.setCellStyle(textStyle);
	        }

	        Cell R12cell4 = row.createCell(5);
	        if (record.getR12_moreThan6MonthTo12Months() != null) {
	            R12cell4.setCellValue(record.getR12_moreThan6MonthTo12Months().doubleValue());
	            R12cell4.setCellStyle(numberStyle);
	        } else {
	            R12cell4.setCellValue("");
	            R12cell4.setCellStyle(textStyle);
	        }

	        Cell R12cell5 = row.createCell(6);
	        if (record.getR12_moreThan12MonthTo3Years() != null) {
	            R12cell5.setCellValue(record.getR12_moreThan12MonthTo3Years().doubleValue());
	            R12cell5.setCellStyle(numberStyle);
	        } else {
	            R12cell5.setCellValue("");
	            R12cell5.setCellStyle(textStyle);
	        }

	        Cell R12cell6 = row.createCell(7);
	        if (record.getR12_moreThan3YearsTo5Years() != null) {
	            R12cell6.setCellValue(record.getR12_moreThan3YearsTo5Years().doubleValue());
	            R12cell6.setCellStyle(numberStyle);
	        } else {
	            R12cell6.setCellValue("");
	            R12cell6.setCellStyle(textStyle);
	        }

	        Cell R12cell7 = row.createCell(8);
	        if (record.getR12_moreThan5YearsTo10Years() != null) {
	            R12cell7.setCellValue(record.getR12_moreThan5YearsTo10Years().doubleValue());
	            R12cell7.setCellStyle(numberStyle);
	        } else {
	            R12cell7.setCellValue("");
	            R12cell7.setCellStyle(textStyle);
	        }

	        Cell R12cell8 = row.createCell(9);
	        if (record.getR12_moreThan10Years() != null) {
	            R12cell8.setCellValue(record.getR12_moreThan10Years().doubleValue());
	            R12cell8.setCellStyle(numberStyle);
	        } else {
	            R12cell8.setCellValue("");
	            R12cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(12);
	         Cell R13cell1 = row.createCell(2);
	        if (record.getR13_upTo1Month() != null) {
	            R13cell1.setCellValue(record.getR13_upTo1Month().doubleValue());
	            R13cell1.setCellStyle(numberStyle);
	        } else {
	            R13cell1.setCellValue("");
	            R13cell1.setCellStyle(textStyle);
	        }

	        Cell R13cell2 = row.createCell(3);
	        if (record.getR13_moreThan1MonthTo3Months() != null) {
	            R13cell2.setCellValue(record.getR13_moreThan1MonthTo3Months().doubleValue());
	            R13cell2.setCellStyle(numberStyle);
	        } else {
	            R13cell2.setCellValue("");
	            R13cell2.setCellStyle(textStyle);
	        }

	        Cell R13cell3 = row.createCell(4);
	        if (record.getR13_moreThan3MonthTo6Months() != null) {
	            R13cell3.setCellValue(record.getR13_moreThan3MonthTo6Months().doubleValue());
	            R13cell3.setCellStyle(numberStyle);
	        } else {
	            R13cell3.setCellValue("");
	            R13cell3.setCellStyle(textStyle);
	        }

	        Cell R13cell4 = row.createCell(5);
	        if (record.getR13_moreThan6MonthTo12Months() != null) {
	            R13cell4.setCellValue(record.getR13_moreThan6MonthTo12Months().doubleValue());
	            R13cell4.setCellStyle(numberStyle);
	        } else {
	            R13cell4.setCellValue("");
	            R13cell4.setCellStyle(textStyle);
	        }

	        Cell R13cell5 = row.createCell(6);
	        if (record.getR13_moreThan12MonthTo3Years() != null) {
	            R13cell5.setCellValue(record.getR13_moreThan12MonthTo3Years().doubleValue());
	            R13cell5.setCellStyle(numberStyle);
	        } else {
	            R13cell5.setCellValue("");
	            R13cell5.setCellStyle(textStyle);
	        }

	        Cell R13cell6 = row.createCell(7);
	        if (record.getR13_moreThan3YearsTo5Years() != null) {
	            R13cell6.setCellValue(record.getR13_moreThan3YearsTo5Years().doubleValue());
	            R13cell6.setCellStyle(numberStyle);
	        } else {
	            R13cell6.setCellValue("");
	            R13cell6.setCellStyle(textStyle);
	        }

	        Cell R13cell7 = row.createCell(8);
	        if (record.getR13_moreThan5YearsTo10Years() != null) {
	            R13cell7.setCellValue(record.getR13_moreThan5YearsTo10Years().doubleValue());
	            R13cell7.setCellStyle(numberStyle);
	        } else {
	            R13cell7.setCellValue("");
	            R13cell7.setCellStyle(textStyle);
	        }

	        Cell R13cell8 = row.createCell(9);
	        if (record.getR13_moreThan10Years() != null) {
	            R13cell8.setCellValue(record.getR13_moreThan10Years().doubleValue());
	            R13cell8.setCellStyle(numberStyle);
	        } else {
	            R13cell8.setCellValue("");
	            R13cell8.setCellStyle(textStyle);
	        }


	        row = sheet.getRow(13);
	        Cell R14cell1 = row.createCell(2);
	        if (record.getR14_upTo1Month() != null) {
	            R14cell1.setCellValue(record.getR14_upTo1Month().doubleValue());
	            R14cell1.setCellStyle(numberStyle);
	        } else {
	            R14cell1.setCellValue("");
	            R14cell1.setCellStyle(textStyle);
	        }

	        Cell R14cell2 = row.createCell(3);
	        if (record.getR14_moreThan1MonthTo3Months() != null) {
	            R14cell2.setCellValue(record.getR14_moreThan1MonthTo3Months().doubleValue());
	            R14cell2.setCellStyle(numberStyle);
	        } else {
	            R14cell2.setCellValue("");
	            R14cell2.setCellStyle(textStyle);
	        }

	        Cell R14cell3 = row.createCell(4);
	        if (record.getR14_moreThan3MonthTo6Months() != null) {
	            R14cell3.setCellValue(record.getR14_moreThan3MonthTo6Months().doubleValue());
	            R14cell3.setCellStyle(numberStyle);
	        } else {
	            R14cell3.setCellValue("");
	            R14cell3.setCellStyle(textStyle);
	        }

	        Cell R14cell4 = row.createCell(5);
	        if (record.getR14_moreThan6MonthTo12Months() != null) {
	            R14cell4.setCellValue(record.getR14_moreThan6MonthTo12Months().doubleValue());
	            R14cell4.setCellStyle(numberStyle);
	        } else {
	            R14cell4.setCellValue("");
	            R14cell4.setCellStyle(textStyle);
	        }

	        Cell R14cell5 = row.createCell(6);
	        if (record.getR14_moreThan12MonthTo3Years() != null) {
	            R14cell5.setCellValue(record.getR14_moreThan12MonthTo3Years().doubleValue());
	            R14cell5.setCellStyle(numberStyle);
	        } else {
	            R14cell5.setCellValue("");
	            R14cell5.setCellStyle(textStyle);
	        }

	        Cell R14cell6 = row.createCell(7);
	        if (record.getR14_moreThan3YearsTo5Years() != null) {
	            R14cell6.setCellValue(record.getR14_moreThan3YearsTo5Years().doubleValue());
	            R14cell6.setCellStyle(numberStyle);
	        } else {
	            R14cell6.setCellValue("");
	            R14cell6.setCellStyle(textStyle);
	        }

	        Cell R14cell7 = row.createCell(8);
	        if (record.getR14_moreThan5YearsTo10Years() != null) {
	            R14cell7.setCellValue(record.getR14_moreThan5YearsTo10Years().doubleValue());
	            R14cell7.setCellStyle(numberStyle);
	        } else {
	            R14cell7.setCellValue("");
	            R14cell7.setCellStyle(textStyle);
	        }

	        Cell R14cell8 = row.createCell(9);
	        if (record.getR14_moreThan10Years() != null) {
	            R14cell8.setCellValue(record.getR14_moreThan10Years().doubleValue());
	            R14cell8.setCellStyle(numberStyle);
	        } else {
	            R14cell8.setCellValue("");
	            R14cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(14);
	        Cell R15cell1 = row.createCell(2);
	        if (record.getR15_upTo1Month() != null) {
	            R15cell1.setCellValue(record.getR15_upTo1Month().doubleValue());
	            R15cell1.setCellStyle(numberStyle);
	        } else {
	            R15cell1.setCellValue("");
	            R15cell1.setCellStyle(textStyle);
	        }

	        Cell R15cell2 = row.createCell(3);
	        if (record.getR15_moreThan1MonthTo3Months() != null) {
	            R15cell2.setCellValue(record.getR15_moreThan1MonthTo3Months().doubleValue());
	            R15cell2.setCellStyle(numberStyle);
	        } else {
	            R15cell2.setCellValue("");
	            R15cell2.setCellStyle(textStyle);
	        }

	        Cell R15cell3 = row.createCell(4);
	        if (record.getR15_moreThan3MonthTo6Months() != null) {
	            R15cell3.setCellValue(record.getR15_moreThan3MonthTo6Months().doubleValue());
	            R15cell3.setCellStyle(numberStyle);
	        } else {
	            R15cell3.setCellValue("");
	            R15cell3.setCellStyle(textStyle);
	        }

	        Cell R15cell4 = row.createCell(5);
	        if (record.getR15_moreThan6MonthTo12Months() != null) {
	            R15cell4.setCellValue(record.getR15_moreThan6MonthTo12Months().doubleValue());
	            R15cell4.setCellStyle(numberStyle);
	        } else {
	            R15cell4.setCellValue("");
	            R15cell4.setCellStyle(textStyle);
	        }

	        Cell R15cell5 = row.createCell(6);
	        if (record.getR15_moreThan12MonthTo3Years() != null) {
	            R15cell5.setCellValue(record.getR15_moreThan12MonthTo3Years().doubleValue());
	            R15cell5.setCellStyle(numberStyle);
	        } else {
	            R15cell5.setCellValue("");
	            R15cell5.setCellStyle(textStyle);
	        }

	        Cell R15cell6 = row.createCell(7);
	        if (record.getR15_moreThan3YearsTo5Years() != null) {
	            R15cell6.setCellValue(record.getR15_moreThan3YearsTo5Years().doubleValue());
	            R15cell6.setCellStyle(numberStyle);
	        } else {
	            R15cell6.setCellValue("");
	            R15cell6.setCellStyle(textStyle);
	        }

	        Cell R15cell7 = row.createCell(8);
	        if (record.getR15_moreThan5YearsTo10Years() != null) {
	            R15cell7.setCellValue(record.getR15_moreThan5YearsTo10Years().doubleValue());
	            R15cell7.setCellStyle(numberStyle);
	        } else {
	            R15cell7.setCellValue("");
	            R15cell7.setCellStyle(textStyle);
	        }

	        Cell R15cell8 = row.createCell(9);
	        if (record.getR15_moreThan10Years() != null) {
	            R15cell8.setCellValue(record.getR15_moreThan10Years().doubleValue());
	            R15cell8.setCellStyle(numberStyle);
	        } else {
	            R15cell8.setCellValue("");
	            R15cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(15);
	        Cell R16cell1 = row.createCell(2);
	        if (record.getR16_upTo1Month() != null) {
	            R16cell1.setCellValue(record.getR16_upTo1Month().doubleValue());
	            R16cell1.setCellStyle(numberStyle);
	        } else {
	            R16cell1.setCellValue("");
	            R16cell1.setCellStyle(textStyle);
	        }

	        Cell R16cell2 = row.createCell(3);
	        if (record.getR16_moreThan1MonthTo3Months() != null) {
	            R16cell2.setCellValue(record.getR16_moreThan1MonthTo3Months().doubleValue());
	            R16cell2.setCellStyle(numberStyle);
	        } else {
	            R16cell2.setCellValue("");
	            R16cell2.setCellStyle(textStyle);
	        }

	        Cell R16cell3 = row.createCell(4);
	        if (record.getR16_moreThan3MonthTo6Months() != null) {
	            R16cell3.setCellValue(record.getR16_moreThan3MonthTo6Months().doubleValue());
	            R16cell3.setCellStyle(numberStyle);
	        } else {
	            R16cell3.setCellValue("");
	            R16cell3.setCellStyle(textStyle);
	        }

	        Cell R16cell4 = row.createCell(5);
	        if (record.getR16_moreThan6MonthTo12Months() != null) {
	            R16cell4.setCellValue(record.getR16_moreThan6MonthTo12Months().doubleValue());
	            R16cell4.setCellStyle(numberStyle);
	        } else {
	            R16cell4.setCellValue("");
	            R16cell4.setCellStyle(textStyle);
	        }

	        Cell R16cell5 = row.createCell(6);
	        if (record.getR16_moreThan12MonthTo3Years() != null) {
	            R16cell5.setCellValue(record.getR16_moreThan12MonthTo3Years().doubleValue());
	            R16cell5.setCellStyle(numberStyle);
	        } else {
	            R16cell5.setCellValue("");
	            R16cell5.setCellStyle(textStyle);
	        }

	        Cell R16cell6 = row.createCell(7);
	        if (record.getR16_moreThan3YearsTo5Years() != null) {
	            R16cell6.setCellValue(record.getR16_moreThan3YearsTo5Years().doubleValue());
	            R16cell6.setCellStyle(numberStyle);
	        } else {
	            R16cell6.setCellValue("");
	            R16cell6.setCellStyle(textStyle);
	        }

	        Cell R16cell7 = row.createCell(8);
	        if (record.getR16_moreThan5YearsTo10Years() != null) {
	            R16cell7.setCellValue(record.getR16_moreThan5YearsTo10Years().doubleValue());
	            R16cell7.setCellStyle(numberStyle);
	        } else {
	            R16cell7.setCellValue("");
	            R16cell7.setCellStyle(textStyle);
	        }

	        Cell R16cell8 = row.createCell(9);
	        if (record.getR16_moreThan10Years() != null) {
	            R16cell8.setCellValue(record.getR16_moreThan10Years().doubleValue());
	            R16cell8.setCellStyle(numberStyle);
	        } else {
	            R16cell8.setCellValue("");
	            R16cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(16);
	        Cell R17cell1 = row.createCell(2);
	        if (record.getR17_upTo1Month() != null) {
	            R17cell1.setCellValue(record.getR17_upTo1Month().doubleValue());
	            R17cell1.setCellStyle(numberStyle);
	        } else {
	            R17cell1.setCellValue("");
	            R17cell1.setCellStyle(textStyle);
	        }

	        Cell R17cell2 = row.createCell(3);
	        if (record.getR17_moreThan1MonthTo3Months() != null) {
	            R17cell2.setCellValue(record.getR17_moreThan1MonthTo3Months().doubleValue());
	            R17cell2.setCellStyle(numberStyle);
	        } else {
	            R17cell2.setCellValue("");
	            R17cell2.setCellStyle(textStyle);
	        }

	        Cell R17cell3 = row.createCell(4);
	        if (record.getR17_moreThan3MonthTo6Months() != null) {
	            R17cell3.setCellValue(record.getR17_moreThan3MonthTo6Months().doubleValue());
	            R17cell3.setCellStyle(numberStyle);
	        } else {
	            R17cell3.setCellValue("");
	            R17cell3.setCellStyle(textStyle);
	        }

	        Cell R17cell4 = row.createCell(5);
	        if (record.getR17_moreThan6MonthTo12Months() != null) {
	            R17cell4.setCellValue(record.getR17_moreThan6MonthTo12Months().doubleValue());
	            R17cell4.setCellStyle(numberStyle);
	        } else {
	            R17cell4.setCellValue("");
	            R17cell4.setCellStyle(textStyle);
	        }

	        Cell R17cell5 = row.createCell(6);
	        if (record.getR17_moreThan12MonthTo3Years() != null) {
	            R17cell5.setCellValue(record.getR17_moreThan12MonthTo3Years().doubleValue());
	            R17cell5.setCellStyle(numberStyle);
	        } else {
	            R17cell5.setCellValue("");
	            R17cell5.setCellStyle(textStyle);
	        }

	        Cell R17cell6 = row.createCell(7);
	        if (record.getR17_moreThan3YearsTo5Years() != null) {
	            R17cell6.setCellValue(record.getR17_moreThan3YearsTo5Years().doubleValue());
	            R17cell6.setCellStyle(numberStyle);
	        } else {
	            R17cell6.setCellValue("");
	            R17cell6.setCellStyle(textStyle);
	        }

	        Cell R17cell7 = row.createCell(8);
	        if (record.getR17_moreThan5YearsTo10Years() != null) {
	            R17cell7.setCellValue(record.getR17_moreThan5YearsTo10Years().doubleValue());
	            R17cell7.setCellStyle(numberStyle);
	        } else {
	            R17cell7.setCellValue("");
	            R17cell7.setCellStyle(textStyle);
	        }

	        Cell R17cell8 = row.createCell(9);
	        if (record.getR17_moreThan10Years() != null) {
	            R17cell8.setCellValue(record.getR17_moreThan10Years().doubleValue());
	            R17cell8.setCellStyle(numberStyle);
	        } else {
	            R17cell8.setCellValue("");
	            R17cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(17);
	        Cell R18cell1 = row.createCell(2);
	        if (record.getR18_upTo1Month() != null) {
	            R18cell1.setCellValue(record.getR18_upTo1Month().doubleValue());
	            R18cell1.setCellStyle(numberStyle);
	        } else {
	            R18cell1.setCellValue("");
	            R18cell1.setCellStyle(textStyle);
	        }

	        Cell R18cell2 = row.createCell(3);
	        if (record.getR18_moreThan1MonthTo3Months() != null) {
	            R18cell2.setCellValue(record.getR18_moreThan1MonthTo3Months().doubleValue());
	            R18cell2.setCellStyle(numberStyle);
	        } else {
	            R18cell2.setCellValue("");
	            R18cell2.setCellStyle(textStyle);
	        }

	        Cell R18cell3 = row.createCell(4);
	        if (record.getR18_moreThan3MonthTo6Months() != null) {
	            R18cell3.setCellValue(record.getR18_moreThan3MonthTo6Months().doubleValue());
	            R18cell3.setCellStyle(numberStyle);
	        } else {
	            R18cell3.setCellValue("");
	            R18cell3.setCellStyle(textStyle);
	        }

	        Cell R18cell4 = row.createCell(5);
	        if (record.getR18_moreThan6MonthTo12Months() != null) {
	            R18cell4.setCellValue(record.getR18_moreThan6MonthTo12Months().doubleValue());
	            R18cell4.setCellStyle(numberStyle);
	        } else {
	            R18cell4.setCellValue("");
	            R18cell4.setCellStyle(textStyle);
	        }

	        Cell R18cell5 = row.createCell(6);
	        if (record.getR18_moreThan12MonthTo3Years() != null) {
	            R18cell5.setCellValue(record.getR18_moreThan12MonthTo3Years().doubleValue());
	            R18cell5.setCellStyle(numberStyle);
	        } else {
	            R18cell5.setCellValue("");
	            R18cell5.setCellStyle(textStyle);
	        }

	        Cell R18cell6 = row.createCell(7);
	        if (record.getR18_moreThan3YearsTo5Years() != null) {
	            R18cell6.setCellValue(record.getR18_moreThan3YearsTo5Years().doubleValue());
	            R18cell6.setCellStyle(numberStyle);
	        } else {
	            R18cell6.setCellValue("");
	            R18cell6.setCellStyle(textStyle);
	        }

	        Cell R18cell7 = row.createCell(8);
	        if (record.getR18_moreThan5YearsTo10Years() != null) {
	            R18cell7.setCellValue(record.getR18_moreThan5YearsTo10Years().doubleValue());
	            R18cell7.setCellStyle(numberStyle);
	        } else {
	            R18cell7.setCellValue("");
	            R18cell7.setCellStyle(textStyle);
	        }

	        Cell R18cell8 = row.createCell(9);
	        if (record.getR18_moreThan10Years() != null) {
	            R18cell8.setCellValue(record.getR18_moreThan10Years().doubleValue());
	            R18cell8.setCellStyle(numberStyle);
	        } else {
	            R18cell8.setCellValue("");
	            R18cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(18);
	        Cell R19cell1 = row.createCell(2);
	        if (record.getR19_upTo1Month() != null) {
	            R19cell1.setCellValue(record.getR19_upTo1Month().doubleValue());
	            R19cell1.setCellStyle(numberStyle);
	        } else {
	            R19cell1.setCellValue("");
	            R19cell1.setCellStyle(textStyle);
	        }

	        Cell R19cell2 = row.createCell(3);
	        if (record.getR19_moreThan1MonthTo3Months() != null) {
	            R19cell2.setCellValue(record.getR19_moreThan1MonthTo3Months().doubleValue());
	            R19cell2.setCellStyle(numberStyle);
	        } else {
	            R19cell2.setCellValue("");
	            R19cell2.setCellStyle(textStyle);
	        }

	        Cell R19cell3 = row.createCell(4);
	        if (record.getR19_moreThan3MonthTo6Months() != null) {
	            R19cell3.setCellValue(record.getR19_moreThan3MonthTo6Months().doubleValue());
	            R19cell3.setCellStyle(numberStyle);
	        } else {
	            R19cell3.setCellValue("");
	            R19cell3.setCellStyle(textStyle);
	        }

	        Cell R19cell4 = row.createCell(5);
	        if (record.getR19_moreThan6MonthTo12Months() != null) {
	            R19cell4.setCellValue(record.getR19_moreThan6MonthTo12Months().doubleValue());
	            R19cell4.setCellStyle(numberStyle);
	        } else {
	            R19cell4.setCellValue("");
	            R19cell4.setCellStyle(textStyle);
	        }

	        Cell R19cell5 = row.createCell(6);
	        if (record.getR19_moreThan12MonthTo3Years() != null) {
	            R19cell5.setCellValue(record.getR19_moreThan12MonthTo3Years().doubleValue());
	            R19cell5.setCellStyle(numberStyle);
	        } else {
	            R19cell5.setCellValue("");
	            R19cell5.setCellStyle(textStyle);
	        }

	        Cell R19cell6 = row.createCell(7);
	        if (record.getR19_moreThan3YearsTo5Years() != null) {
	            R19cell6.setCellValue(record.getR19_moreThan3YearsTo5Years().doubleValue());
	            R19cell6.setCellStyle(numberStyle);
	        } else {
	            R19cell6.setCellValue("");
	            R19cell6.setCellStyle(textStyle);
	        }

	        Cell R19cell7 = row.createCell(8);
	        if (record.getR19_moreThan5YearsTo10Years() != null) {
	            R19cell7.setCellValue(record.getR19_moreThan5YearsTo10Years().doubleValue());
	            R19cell7.setCellStyle(numberStyle);
	        } else {
	            R19cell7.setCellValue("");
	            R19cell7.setCellStyle(textStyle);
	        }

	        Cell R19cell8 = row.createCell(9);
	        if (record.getR19_moreThan10Years() != null) {
	            R19cell8.setCellValue(record.getR19_moreThan10Years().doubleValue());
	            R19cell8.setCellStyle(numberStyle);
	        } else {
	            R19cell8.setCellValue("");
	            R19cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(19);
	        Cell R20cell1 = row.createCell(2);
	        if (record.getR20_upTo1Month() != null) {
	            R20cell1.setCellValue(record.getR20_upTo1Month().doubleValue());
	            R20cell1.setCellStyle(numberStyle);
	        } else {
	            R20cell1.setCellValue("");
	            R20cell1.setCellStyle(textStyle);
	        }

	        Cell R20cell2 = row.createCell(3);
	        if (record.getR20_moreThan1MonthTo3Months() != null) {
	            R20cell2.setCellValue(record.getR20_moreThan1MonthTo3Months().doubleValue());
	            R20cell2.setCellStyle(numberStyle);
	        } else {
	            R20cell2.setCellValue("");
	            R20cell2.setCellStyle(textStyle);
	        }

	        Cell R20cell3 = row.createCell(4);
	        if (record.getR20_moreThan3MonthTo6Months() != null) {
	            R20cell3.setCellValue(record.getR20_moreThan3MonthTo6Months().doubleValue());
	            R20cell3.setCellStyle(numberStyle);
	        } else {
	            R20cell3.setCellValue("");
	            R20cell3.setCellStyle(textStyle);
	        }

	        Cell R20cell4 = row.createCell(5);
	        if (record.getR20_moreThan6MonthTo12Months() != null) {
	            R20cell4.setCellValue(record.getR20_moreThan6MonthTo12Months().doubleValue());
	            R20cell4.setCellStyle(numberStyle);
	        } else {
	            R20cell4.setCellValue("");
	            R20cell4.setCellStyle(textStyle);
	        }

	        Cell R20cell5 = row.createCell(6);
	        if (record.getR20_moreThan12MonthTo3Years() != null) {
	            R20cell5.setCellValue(record.getR20_moreThan12MonthTo3Years().doubleValue());
	            R20cell5.setCellStyle(numberStyle);
	        } else {
	            R20cell5.setCellValue("");
	            R20cell5.setCellStyle(textStyle);
	        }

	        Cell R20cell6 = row.createCell(7);
	        if (record.getR20_moreThan3YearsTo5Years() != null) {
	            R20cell6.setCellValue(record.getR20_moreThan3YearsTo5Years().doubleValue());
	            R20cell6.setCellStyle(numberStyle);
	        } else {
	            R20cell6.setCellValue("");
	            R20cell6.setCellStyle(textStyle);
	        }

	        Cell R20cell7 = row.createCell(8);
	        if (record.getR20_moreThan5YearsTo10Years() != null) {
	            R20cell7.setCellValue(record.getR20_moreThan5YearsTo10Years().doubleValue());
	            R20cell7.setCellStyle(numberStyle);
	        } else {
	            R20cell7.setCellValue("");
	            R20cell7.setCellStyle(textStyle);
	        }

	        Cell R20cell8 = row.createCell(9);
	        if (record.getR20_moreThan10Years() != null) {
	            R20cell8.setCellValue(record.getR20_moreThan10Years().doubleValue());
	            R20cell8.setCellStyle(numberStyle);
	        } else {
	            R20cell8.setCellValue("");
	            R20cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(20);
	        Cell R21cell1 = row.createCell(2);
	        if (record.getR21_upTo1Month() != null) {
	            R21cell1.setCellValue(record.getR21_upTo1Month().doubleValue());
	            R21cell1.setCellStyle(numberStyle);
	        } else {
	            R21cell1.setCellValue("");
	            R21cell1.setCellStyle(textStyle);
	        }

	        Cell R21cell2 = row.createCell(3);
	        if (record.getR21_moreThan1MonthTo3Months() != null) {
	            R21cell2.setCellValue(record.getR21_moreThan1MonthTo3Months().doubleValue());
	            R21cell2.setCellStyle(numberStyle);
	        } else {
	            R21cell2.setCellValue("");
	            R21cell2.setCellStyle(textStyle);
	        }

	        Cell R21cell3 = row.createCell(4);
	        if (record.getR21_moreThan3MonthTo6Months() != null) {
	            R21cell3.setCellValue(record.getR21_moreThan3MonthTo6Months().doubleValue());
	            R21cell3.setCellStyle(numberStyle);
	        } else {
	            R21cell3.setCellValue("");
	            R21cell3.setCellStyle(textStyle);
	        }

	        Cell R21cell4 = row.createCell(5);
	        if (record.getR21_moreThan6MonthTo12Months() != null) {
	            R21cell4.setCellValue(record.getR21_moreThan6MonthTo12Months().doubleValue());
	            R21cell4.setCellStyle(numberStyle);
	        } else {
	            R21cell4.setCellValue("");
	            R21cell4.setCellStyle(textStyle);
	        }

	        Cell R21cell5 = row.createCell(6);
	        if (record.getR21_moreThan12MonthTo3Years() != null) {
	            R21cell5.setCellValue(record.getR21_moreThan12MonthTo3Years().doubleValue());
	            R21cell5.setCellStyle(numberStyle);
	        } else {
	            R21cell5.setCellValue("");
	            R21cell5.setCellStyle(textStyle);
	        }

	        Cell R21cell6 = row.createCell(7);
	        if (record.getR21_moreThan3YearsTo5Years() != null) {
	            R21cell6.setCellValue(record.getR21_moreThan3YearsTo5Years().doubleValue());
	            R21cell6.setCellStyle(numberStyle);
	        } else {
	            R21cell6.setCellValue("");
	            R21cell6.setCellStyle(textStyle);
	        }

	        Cell R21cell7 = row.createCell(8);
	        if (record.getR21_moreThan5YearsTo10Years() != null) {
	            R21cell7.setCellValue(record.getR21_moreThan5YearsTo10Years().doubleValue());
	            R21cell7.setCellStyle(numberStyle);
	        } else {
	            R21cell7.setCellValue("");
	            R21cell7.setCellStyle(textStyle);
	        }

	        Cell R21cell8 = row.createCell(9);
	        if (record.getR21_moreThan10Years() != null) {
	            R21cell8.setCellValue(record.getR21_moreThan10Years().doubleValue());
	            R21cell8.setCellStyle(numberStyle);
	        } else {
	            R21cell8.setCellValue("");
	            R21cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(21);
	        Cell R22cell1 = row.createCell(2);
	        if (record.getR22_upTo1Month() != null) {
	            R22cell1.setCellValue(record.getR22_upTo1Month().doubleValue());
	            R22cell1.setCellStyle(numberStyle);
	        } else {
	            R22cell1.setCellValue("");
	            R22cell1.setCellStyle(textStyle);
	        }

	        Cell R22cell2 = row.createCell(3);
	        if (record.getR22_moreThan1MonthTo3Months() != null) {
	            R22cell2.setCellValue(record.getR22_moreThan1MonthTo3Months().doubleValue());
	            R22cell2.setCellStyle(numberStyle);
	        } else {
	            R22cell2.setCellValue("");
	            R22cell2.setCellStyle(textStyle);
	        }

	        Cell R22cell3 = row.createCell(4);
	        if (record.getR22_moreThan3MonthTo6Months() != null) {
	            R22cell3.setCellValue(record.getR22_moreThan3MonthTo6Months().doubleValue());
	            R22cell3.setCellStyle(numberStyle);
	        } else {
	            R22cell3.setCellValue("");
	            R22cell3.setCellStyle(textStyle);
	        }

	        Cell R22cell4 = row.createCell(5);
	        if (record.getR22_moreThan6MonthTo12Months() != null) {
	            R22cell4.setCellValue(record.getR22_moreThan6MonthTo12Months().doubleValue());
	            R22cell4.setCellStyle(numberStyle);
	        } else {
	            R22cell4.setCellValue("");
	            R22cell4.setCellStyle(textStyle);
	        }

	        Cell R22cell5 = row.createCell(6);
	        if (record.getR22_moreThan12MonthTo3Years() != null) {
	            R22cell5.setCellValue(record.getR22_moreThan12MonthTo3Years().doubleValue());
	            R22cell5.setCellStyle(numberStyle);
	        } else {
	            R22cell5.setCellValue("");
	            R22cell5.setCellStyle(textStyle);
	        }

	        Cell R22cell6 = row.createCell(7);
	        if (record.getR22_moreThan3YearsTo5Years() != null) {
	            R22cell6.setCellValue(record.getR22_moreThan3YearsTo5Years().doubleValue());
	            R22cell6.setCellStyle(numberStyle);
	        } else {
	            R22cell6.setCellValue("");
	            R22cell6.setCellStyle(textStyle);
	        }

	        Cell R22cell7 = row.createCell(8);
	        if (record.getR22_moreThan5YearsTo10Years() != null) {
	            R22cell7.setCellValue(record.getR22_moreThan5YearsTo10Years().doubleValue());
	            R22cell7.setCellStyle(numberStyle);
	        } else {
	            R22cell7.setCellValue("");
	            R22cell7.setCellStyle(textStyle);
	        }

	        Cell R22cell8 = row.createCell(9);
	        if (record.getR22_moreThan10Years() != null) {
	            R22cell8.setCellValue(record.getR22_moreThan10Years().doubleValue());
	            R22cell8.setCellStyle(numberStyle);
	        } else {
	            R22cell8.setCellValue("");
	            R22cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(23);
	        Cell R24cell1 = row.createCell(2);
	        if (record.getR24_upTo1Month() != null) {
	            R24cell1.setCellValue(record.getR24_upTo1Month().doubleValue());
	            R24cell1.setCellStyle(numberStyle);
	        } else {
	            R24cell1.setCellValue("");
	            R24cell1.setCellStyle(textStyle);
	        }

	        Cell R24cell2 = row.createCell(3);
	        if (record.getR24_moreThan1MonthTo3Months() != null) {
	            R24cell2.setCellValue(record.getR24_moreThan1MonthTo3Months().doubleValue());
	            R24cell2.setCellStyle(numberStyle);
	        } else {
	            R24cell2.setCellValue("");
	            R24cell2.setCellStyle(textStyle);
	        }

	        Cell R24cell3 = row.createCell(4);
	        if (record.getR24_moreThan3MonthTo6Months() != null) {
	            R24cell3.setCellValue(record.getR24_moreThan3MonthTo6Months().doubleValue());
	            R24cell3.setCellStyle(numberStyle);
	        } else {
	            R24cell3.setCellValue("");
	            R24cell3.setCellStyle(textStyle);
	        }

	        Cell R24cell4 = row.createCell(5);
	        if (record.getR24_moreThan6MonthTo12Months() != null) {
	            R24cell4.setCellValue(record.getR24_moreThan6MonthTo12Months().doubleValue());
	            R24cell4.setCellStyle(numberStyle);
	        } else {
	            R24cell4.setCellValue("");
	            R24cell4.setCellStyle(textStyle);
	        }

	        Cell R24cell5 = row.createCell(6);
	        if (record.getR24_moreThan12MonthTo3Years() != null) {
	            R24cell5.setCellValue(record.getR24_moreThan12MonthTo3Years().doubleValue());
	            R24cell5.setCellStyle(numberStyle);
	        } else {
	            R24cell5.setCellValue("");
	            R24cell5.setCellStyle(textStyle);
	        }

	        Cell R24cell6 = row.createCell(7);
	        if (record.getR24_moreThan3YearsTo5Years() != null) {
	            R24cell6.setCellValue(record.getR24_moreThan3YearsTo5Years().doubleValue());
	            R24cell6.setCellStyle(numberStyle);
	        } else {
	            R24cell6.setCellValue("");
	            R24cell6.setCellStyle(textStyle);
	        }

	        Cell R24cell7 = row.createCell(8);
	        if (record.getR24_moreThan5YearsTo10Years() != null) {
	            R24cell7.setCellValue(record.getR24_moreThan5YearsTo10Years().doubleValue());
	            R24cell7.setCellStyle(numberStyle);
	        } else {
	            R24cell7.setCellValue("");
	            R24cell7.setCellStyle(textStyle);
	        }

	        Cell R24cell8 = row.createCell(9);
	        if (record.getR24_moreThan10Years() != null) {
	            R24cell8.setCellValue(record.getR24_moreThan10Years().doubleValue());
	            R24cell8.setCellStyle(numberStyle);
	        } else {
	            R24cell8.setCellValue("");
	            R24cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(24);
	        Cell R25cell1 = row.createCell(2);
	        if (record.getR25_upTo1Month() != null) {
	            R25cell1.setCellValue(record.getR25_upTo1Month().doubleValue());
	            R25cell1.setCellStyle(numberStyle);
	        } else {
	            R25cell1.setCellValue("");
	            R25cell1.setCellStyle(textStyle);
	        }

	        Cell R25cell2 = row.createCell(3);
	        if (record.getR25_moreThan1MonthTo3Months() != null) {
	            R25cell2.setCellValue(record.getR25_moreThan1MonthTo3Months().doubleValue());
	            R25cell2.setCellStyle(numberStyle);
	        } else {
	            R25cell2.setCellValue("");
	            R25cell2.setCellStyle(textStyle);
	        }

	        Cell R25cell3 = row.createCell(4);
	        if (record.getR25_moreThan3MonthTo6Months() != null) {
	            R25cell3.setCellValue(record.getR25_moreThan3MonthTo6Months().doubleValue());
	            R25cell3.setCellStyle(numberStyle);
	        } else {
	            R25cell3.setCellValue("");
	            R25cell3.setCellStyle(textStyle);
	        }

	        Cell R25cell4 = row.createCell(5);
	        if (record.getR25_moreThan6MonthTo12Months() != null) {
	            R25cell4.setCellValue(record.getR25_moreThan6MonthTo12Months().doubleValue());
	            R25cell4.setCellStyle(numberStyle);
	        } else {
	            R25cell4.setCellValue("");
	            R25cell4.setCellStyle(textStyle);
	        }

	        Cell R25cell5 = row.createCell(6);
	        if (record.getR25_moreThan12MonthTo3Years() != null) {
	            R25cell5.setCellValue(record.getR25_moreThan12MonthTo3Years().doubleValue());
	            R25cell5.setCellStyle(numberStyle);
	        } else {
	            R25cell5.setCellValue("");
	            R25cell5.setCellStyle(textStyle);
	        }

	        Cell R25cell6 = row.createCell(7);
	        if (record.getR25_moreThan3YearsTo5Years() != null) {
	            R25cell6.setCellValue(record.getR25_moreThan3YearsTo5Years().doubleValue());
	            R25cell6.setCellStyle(numberStyle);
	        } else {
	            R25cell6.setCellValue("");
	            R25cell6.setCellStyle(textStyle);
	        }

	        Cell R25cell7 = row.createCell(8);
	        if (record.getR25_moreThan5YearsTo10Years() != null) {
	            R25cell7.setCellValue(record.getR25_moreThan5YearsTo10Years().doubleValue());
	            R25cell7.setCellStyle(numberStyle);
	        } else {
	            R25cell7.setCellValue("");
	            R25cell7.setCellStyle(textStyle);
	        }

	        Cell R25cell8 = row.createCell(9);
	        if (record.getR25_moreThan10Years() != null) {
	            R25cell8.setCellValue(record.getR25_moreThan10Years().doubleValue());
	            R25cell8.setCellStyle(numberStyle);
	        } else {
	            R25cell8.setCellValue("");
	            R25cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(25);
	        Cell R26cell1 = row.createCell(2);
	        if (record.getR26_upTo1Month() != null) {
	            R26cell1.setCellValue(record.getR26_upTo1Month().doubleValue());
	            R26cell1.setCellStyle(numberStyle);
	        } else {
	            R26cell1.setCellValue("");
	            R26cell1.setCellStyle(textStyle);
	        }

	        Cell R26cell2 = row.createCell(3);
	        if (record.getR26_moreThan1MonthTo3Months() != null) {
	            R26cell2.setCellValue(record.getR26_moreThan1MonthTo3Months().doubleValue());
	            R26cell2.setCellStyle(numberStyle);
	        } else {
	            R26cell2.setCellValue("");
	            R26cell2.setCellStyle(textStyle);
	        }

	        Cell R26cell3 = row.createCell(4);
	        if (record.getR26_moreThan3MonthTo6Months() != null) {
	            R26cell3.setCellValue(record.getR26_moreThan3MonthTo6Months().doubleValue());
	            R26cell3.setCellStyle(numberStyle);
	        } else {
	            R26cell3.setCellValue("");
	            R26cell3.setCellStyle(textStyle);
	        }

	        Cell R26cell4 = row.createCell(5);
	        if (record.getR26_moreThan6MonthTo12Months() != null) {
	            R26cell4.setCellValue(record.getR26_moreThan6MonthTo12Months().doubleValue());
	            R26cell4.setCellStyle(numberStyle);
	        } else {
	            R26cell4.setCellValue("");
	            R26cell4.setCellStyle(textStyle);
	        }

	        Cell R26cell5 = row.createCell(6);
	        if (record.getR26_moreThan12MonthTo3Years() != null) {
	            R26cell5.setCellValue(record.getR26_moreThan12MonthTo3Years().doubleValue());
	            R26cell5.setCellStyle(numberStyle);
	        } else {
	            R26cell5.setCellValue("");
	            R26cell5.setCellStyle(textStyle);
	        }

	        Cell R26cell6 = row.createCell(7);
	        if (record.getR26_moreThan3YearsTo5Years() != null) {
	            R26cell6.setCellValue(record.getR26_moreThan3YearsTo5Years().doubleValue());
	            R26cell6.setCellStyle(numberStyle);
	        } else {
	            R26cell6.setCellValue("");
	            R26cell6.setCellStyle(textStyle);
	        }

	        Cell R26cell7 = row.createCell(8);
	        if (record.getR26_moreThan5YearsTo10Years() != null) {
	            R26cell7.setCellValue(record.getR26_moreThan5YearsTo10Years().doubleValue());
	            R26cell7.setCellStyle(numberStyle);
	        } else {
	            R26cell7.setCellValue("");
	            R26cell7.setCellStyle(textStyle);
	        }

	        Cell R26cell8 = row.createCell(9);
	        if (record.getR26_moreThan10Years() != null) {
	            R26cell8.setCellValue(record.getR26_moreThan10Years().doubleValue());
	            R26cell8.setCellStyle(numberStyle);
	        } else {
	            R26cell8.setCellValue("");
	            R26cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(26);
	        Cell R27cell1 = row.createCell(2);
	        if (record.getR27_upTo1Month() != null) {
	            R27cell1.setCellValue(record.getR27_upTo1Month().doubleValue());
	            R27cell1.setCellStyle(numberStyle);
	        } else {
	            R27cell1.setCellValue("");
	            R27cell1.setCellStyle(textStyle);
	        }

	        Cell R27cell2 = row.createCell(3);
	        if (record.getR27_moreThan1MonthTo3Months() != null) {
	            R27cell2.setCellValue(record.getR27_moreThan1MonthTo3Months().doubleValue());
	            R27cell2.setCellStyle(numberStyle);
	        } else {
	            R27cell2.setCellValue("");
	            R27cell2.setCellStyle(textStyle);
	        }

	        Cell R27cell3 = row.createCell(4);
	        if (record.getR27_moreThan3MonthTo6Months() != null) {
	            R27cell3.setCellValue(record.getR27_moreThan3MonthTo6Months().doubleValue());
	            R27cell3.setCellStyle(numberStyle);
	        } else {
	            R27cell3.setCellValue("");
	            R27cell3.setCellStyle(textStyle);
	        }

	        Cell R27cell4 = row.createCell(5);
	        if (record.getR27_moreThan6MonthTo12Months() != null) {
	            R27cell4.setCellValue(record.getR27_moreThan6MonthTo12Months().doubleValue());
	            R27cell4.setCellStyle(numberStyle);
	        } else {
	            R27cell4.setCellValue("");
	            R27cell4.setCellStyle(textStyle);
	        }

	        Cell R27cell5 = row.createCell(6);
	        if (record.getR27_moreThan12MonthTo3Years() != null) {
	            R27cell5.setCellValue(record.getR27_moreThan12MonthTo3Years().doubleValue());
	            R27cell5.setCellStyle(numberStyle);
	        } else {
	            R27cell5.setCellValue("");
	            R27cell5.setCellStyle(textStyle);
	        }

	        Cell R27cell6 = row.createCell(7);
	        if (record.getR27_moreThan3YearsTo5Years() != null) {
	            R27cell6.setCellValue(record.getR27_moreThan3YearsTo5Years().doubleValue());
	            R27cell6.setCellStyle(numberStyle);
	        } else {
	            R27cell6.setCellValue("");
	            R27cell6.setCellStyle(textStyle);
	        }

	        Cell R27cell7 = row.createCell(8);
	        if (record.getR27_moreThan5YearsTo10Years() != null) {
	            R27cell7.setCellValue(record.getR27_moreThan5YearsTo10Years().doubleValue());
	            R27cell7.setCellStyle(numberStyle);
	        } else {
	            R27cell7.setCellValue("");
	            R27cell7.setCellStyle(textStyle);
	        }

	        Cell R27cell8 = row.createCell(9);
	        if (record.getR27_moreThan10Years() != null) {
	            R27cell8.setCellValue(record.getR27_moreThan10Years().doubleValue());
	            R27cell8.setCellStyle(numberStyle);
	        } else {
	            R27cell8.setCellValue("");
	            R27cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(27);
	        Cell R28cell1 = row.createCell(2);
	        if (record.getR28_upTo1Month() != null) {
	            R28cell1.setCellValue(record.getR28_upTo1Month().doubleValue());
	            R28cell1.setCellStyle(numberStyle);
	        } else {
	            R28cell1.setCellValue("");
	            R28cell1.setCellStyle(textStyle);
	        }

	        Cell R28cell2 = row.createCell(3);
	        if (record.getR28_moreThan1MonthTo3Months() != null) {
	            R28cell2.setCellValue(record.getR28_moreThan1MonthTo3Months().doubleValue());
	            R28cell2.setCellStyle(numberStyle);
	        } else {
	            R28cell2.setCellValue("");
	            R28cell2.setCellStyle(textStyle);
	        }

	        Cell R28cell3 = row.createCell(4);
	        if (record.getR28_moreThan3MonthTo6Months() != null) {
	            R28cell3.setCellValue(record.getR28_moreThan3MonthTo6Months().doubleValue());
	            R28cell3.setCellStyle(numberStyle);
	        } else {
	            R28cell3.setCellValue("");
	            R28cell3.setCellStyle(textStyle);
	        }

	        Cell R28cell4 = row.createCell(5);
	        if (record.getR28_moreThan6MonthTo12Months() != null) {
	            R28cell4.setCellValue(record.getR28_moreThan6MonthTo12Months().doubleValue());
	            R28cell4.setCellStyle(numberStyle);
	        } else {
	            R28cell4.setCellValue("");
	            R28cell4.setCellStyle(textStyle);
	        }

	        Cell R28cell5 = row.createCell(6);
	        if (record.getR28_moreThan12MonthTo3Years() != null) {
	            R28cell5.setCellValue(record.getR28_moreThan12MonthTo3Years().doubleValue());
	            R28cell5.setCellStyle(numberStyle);
	        } else {
	            R28cell5.setCellValue("");
	            R28cell5.setCellStyle(textStyle);
	        }

	        Cell R28cell6 = row.createCell(7);
	        if (record.getR28_moreThan3YearsTo5Years() != null) {
	            R28cell6.setCellValue(record.getR28_moreThan3YearsTo5Years().doubleValue());
	            R28cell6.setCellStyle(numberStyle);
	        } else {
	            R28cell6.setCellValue("");
	            R28cell6.setCellStyle(textStyle);
	        }

	        Cell R28cell7 = row.createCell(8);
	        if (record.getR28_moreThan5YearsTo10Years() != null) {
	            R28cell7.setCellValue(record.getR28_moreThan5YearsTo10Years().doubleValue());
	            R28cell7.setCellStyle(numberStyle);
	        } else {
	            R28cell7.setCellValue("");
	            R28cell7.setCellStyle(textStyle);
	        }

	        Cell R28cell8 = row.createCell(9);
	        if (record.getR28_moreThan10Years() != null) {
	            R28cell8.setCellValue(record.getR28_moreThan10Years().doubleValue());
	            R28cell8.setCellStyle(numberStyle);
	        } else {
	            R28cell8.setCellValue("");
	            R28cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(28);
	        Cell R29cell1 = row.createCell(2);
	        if (record.getR29_upTo1Month() != null) {
	            R29cell1.setCellValue(record.getR29_upTo1Month().doubleValue());
	            R29cell1.setCellStyle(numberStyle);
	        } else {
	            R29cell1.setCellValue("");
	            R29cell1.setCellStyle(textStyle);
	        }

	        Cell R29cell2 = row.createCell(3);
	        if (record.getR29_moreThan1MonthTo3Months() != null) {
	            R29cell2.setCellValue(record.getR29_moreThan1MonthTo3Months().doubleValue());
	            R29cell2.setCellStyle(numberStyle);
	        } else {
	            R29cell2.setCellValue("");
	            R29cell2.setCellStyle(textStyle);
	        }

	        Cell R29cell3 = row.createCell(4);
	        if (record.getR29_moreThan3MonthTo6Months() != null) {
	            R29cell3.setCellValue(record.getR29_moreThan3MonthTo6Months().doubleValue());
	            R29cell3.setCellStyle(numberStyle);
	        } else {
	            R29cell3.setCellValue("");
	            R29cell3.setCellStyle(textStyle);
	        }

	        Cell R29cell4 = row.createCell(5);
	        if (record.getR29_moreThan6MonthTo12Months() != null) {
	            R29cell4.setCellValue(record.getR29_moreThan6MonthTo12Months().doubleValue());
	            R29cell4.setCellStyle(numberStyle);
	        } else {
	            R29cell4.setCellValue("");
	            R29cell4.setCellStyle(textStyle);
	        }

	        Cell R29cell5 = row.createCell(6);
	        if (record.getR29_moreThan12MonthTo3Years() != null) {
	            R29cell5.setCellValue(record.getR29_moreThan12MonthTo3Years().doubleValue());
	            R29cell5.setCellStyle(numberStyle);
	        } else {
	            R29cell5.setCellValue("");
	            R29cell5.setCellStyle(textStyle);
	        }

	        Cell R29cell6 = row.createCell(7);
	        if (record.getR29_moreThan3YearsTo5Years() != null) {
	            R29cell6.setCellValue(record.getR29_moreThan3YearsTo5Years().doubleValue());
	            R29cell6.setCellStyle(numberStyle);
	        } else {
	            R29cell6.setCellValue("");
	            R29cell6.setCellStyle(textStyle);
	        }

	        Cell R29cell7 = row.createCell(8);
	        if (record.getR29_moreThan5YearsTo10Years() != null) {
	            R29cell7.setCellValue(record.getR29_moreThan5YearsTo10Years().doubleValue());
	            R29cell7.setCellStyle(numberStyle);
	        } else {
	            R29cell7.setCellValue("");
	            R29cell7.setCellStyle(textStyle);
	        }

	        Cell R29cell8 = row.createCell(9);
	        if (record.getR29_moreThan10Years() != null) {
	            R29cell8.setCellValue(record.getR29_moreThan10Years().doubleValue());
	            R29cell8.setCellStyle(numberStyle);
	        } else {
	            R29cell8.setCellValue("");
	            R29cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(29);
	        Cell R30cell1 = row.createCell(2);
	        if (record.getR30_upTo1Month() != null) {
	            R30cell1.setCellValue(record.getR30_upTo1Month().doubleValue());
	            R30cell1.setCellStyle(numberStyle);
	        } else {
	            R30cell1.setCellValue("");
	            R30cell1.setCellStyle(textStyle);
	        }

	        Cell R30cell2 = row.createCell(3);
	        if (record.getR30_moreThan1MonthTo3Months() != null) {
	            R30cell2.setCellValue(record.getR30_moreThan1MonthTo3Months().doubleValue());
	            R30cell2.setCellStyle(numberStyle);
	        } else {
	            R30cell2.setCellValue("");
	            R30cell2.setCellStyle(textStyle);
	        }

	        Cell R30cell3 = row.createCell(4);
	        if (record.getR30_moreThan3MonthTo6Months() != null) {
	            R30cell3.setCellValue(record.getR30_moreThan3MonthTo6Months().doubleValue());
	            R30cell3.setCellStyle(numberStyle);
	        } else {
	            R30cell3.setCellValue("");
	            R30cell3.setCellStyle(textStyle);
	        }

	        Cell R30cell4 = row.createCell(5);
	        if (record.getR30_moreThan6MonthTo12Months() != null) {
	            R30cell4.setCellValue(record.getR30_moreThan6MonthTo12Months().doubleValue());
	            R30cell4.setCellStyle(numberStyle);
	        } else {
	            R30cell4.setCellValue("");
	            R30cell4.setCellStyle(textStyle);
	        }

	        Cell R30cell5 = row.createCell(6);
	        if (record.getR30_moreThan12MonthTo3Years() != null) {
	            R30cell5.setCellValue(record.getR30_moreThan12MonthTo3Years().doubleValue());
	            R30cell5.setCellStyle(numberStyle);
	        } else {
	            R30cell5.setCellValue("");
	            R30cell5.setCellStyle(textStyle);
	        }

	        Cell R30cell6 = row.createCell(7);
	        if (record.getR30_moreThan3YearsTo5Years() != null) {
	            R30cell6.setCellValue(record.getR30_moreThan3YearsTo5Years().doubleValue());
	            R30cell6.setCellStyle(numberStyle);
	        } else {
	            R30cell6.setCellValue("");
	            R30cell6.setCellStyle(textStyle);
	        }

	        Cell R30cell7 = row.createCell(8);
	        if (record.getR30_moreThan5YearsTo10Years() != null) {
	            R30cell7.setCellValue(record.getR30_moreThan5YearsTo10Years().doubleValue());
	            R30cell7.setCellStyle(numberStyle);
	        } else {
	            R30cell7.setCellValue("");
	            R30cell7.setCellStyle(textStyle);
	        }

	        Cell R30cell8 = row.createCell(9);
	        if (record.getR30_moreThan10Years() != null) {
	            R30cell8.setCellValue(record.getR30_moreThan10Years().doubleValue());
	            R30cell8.setCellStyle(numberStyle);
	        } else {
	            R30cell8.setCellValue("");
	            R30cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(30);
	        Cell R31cell1 = row.createCell(2);
	        if (record.getR31_upTo1Month() != null) {
	            R31cell1.setCellValue(record.getR31_upTo1Month().doubleValue());
	            R31cell1.setCellStyle(numberStyle);
	        } else {
	            R31cell1.setCellValue("");
	            R31cell1.setCellStyle(textStyle);
	        }

	        Cell R31cell2 = row.createCell(3);
	        if (record.getR31_moreThan1MonthTo3Months() != null) {
	            R31cell2.setCellValue(record.getR31_moreThan1MonthTo3Months().doubleValue());
	            R31cell2.setCellStyle(numberStyle);
	        } else {
	            R31cell2.setCellValue("");
	            R31cell2.setCellStyle(textStyle);
	        }

	        Cell R31cell3 = row.createCell(4);
	        if (record.getR31_moreThan3MonthTo6Months() != null) {
	            R31cell3.setCellValue(record.getR31_moreThan3MonthTo6Months().doubleValue());
	            R31cell3.setCellStyle(numberStyle);
	        } else {
	            R31cell3.setCellValue("");
	            R31cell3.setCellStyle(textStyle);
	        }

	        Cell R31cell4 = row.createCell(5);
	        if (record.getR31_moreThan6MonthTo12Months() != null) {
	            R31cell4.setCellValue(record.getR31_moreThan6MonthTo12Months().doubleValue());
	            R31cell4.setCellStyle(numberStyle);
	        } else {
	            R31cell4.setCellValue("");
	            R31cell4.setCellStyle(textStyle);
	        }

	        Cell R31cell5 = row.createCell(6);
	        if (record.getR31_moreThan12MonthTo3Years() != null) {
	            R31cell5.setCellValue(record.getR31_moreThan12MonthTo3Years().doubleValue());
	            R31cell5.setCellStyle(numberStyle);
	        } else {
	            R31cell5.setCellValue("");
	            R31cell5.setCellStyle(textStyle);
	        }

	        Cell R31cell6 = row.createCell(7);
	        if (record.getR31_moreThan3YearsTo5Years() != null) {
	            R31cell6.setCellValue(record.getR31_moreThan3YearsTo5Years().doubleValue());
	            R31cell6.setCellStyle(numberStyle);
	        } else {
	            R31cell6.setCellValue("");
	            R31cell6.setCellStyle(textStyle);
	        }

	        Cell R31cell7 = row.createCell(8);
	        if (record.getR31_moreThan5YearsTo10Years() != null) {
	            R31cell7.setCellValue(record.getR31_moreThan5YearsTo10Years().doubleValue());
	            R31cell7.setCellStyle(numberStyle);
	        } else {
	            R31cell7.setCellValue("");
	            R31cell7.setCellStyle(textStyle);
	        }

	        Cell R31cell8 = row.createCell(9);
	        if (record.getR31_moreThan10Years() != null) {
	            R31cell8.setCellValue(record.getR31_moreThan10Years().doubleValue());
	            R31cell8.setCellStyle(numberStyle);
	        } else {
	            R31cell8.setCellValue("");
	            R31cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(31);
	        Cell R32cell1 = row.createCell(2);
	        if (record.getR32_upTo1Month() != null) {
	            R32cell1.setCellValue(record.getR32_upTo1Month().doubleValue());
	            R32cell1.setCellStyle(numberStyle);
	        } else {
	            R32cell1.setCellValue("");
	            R32cell1.setCellStyle(textStyle);
	        }

	        Cell R32cell2 = row.createCell(3);
	        if (record.getR32_moreThan1MonthTo3Months() != null) {
	            R32cell2.setCellValue(record.getR32_moreThan1MonthTo3Months().doubleValue());
	            R32cell2.setCellStyle(numberStyle);
	        } else {
	            R32cell2.setCellValue("");
	            R32cell2.setCellStyle(textStyle);
	        }

	        Cell R32cell3 = row.createCell(4);
	        if (record.getR32_moreThan3MonthTo6Months() != null) {
	            R32cell3.setCellValue(record.getR32_moreThan3MonthTo6Months().doubleValue());
	            R32cell3.setCellStyle(numberStyle);
	        } else {
	            R32cell3.setCellValue("");
	            R32cell3.setCellStyle(textStyle);
	        }

	        Cell R32cell4 = row.createCell(5);
	        if (record.getR32_moreThan6MonthTo12Months() != null) {
	            R32cell4.setCellValue(record.getR32_moreThan6MonthTo12Months().doubleValue());
	            R32cell4.setCellStyle(numberStyle);
	        } else {
	            R32cell4.setCellValue("");
	            R32cell4.setCellStyle(textStyle);
	        }

	        Cell R32cell5 = row.createCell(6);
	        if (record.getR32_moreThan12MonthTo3Years() != null) {
	            R32cell5.setCellValue(record.getR32_moreThan12MonthTo3Years().doubleValue());
	            R32cell5.setCellStyle(numberStyle);
	        } else {
	            R32cell5.setCellValue("");
	            R32cell5.setCellStyle(textStyle);
	        }

	        Cell R32cell6 = row.createCell(7);
	        if (record.getR32_moreThan3YearsTo5Years() != null) {
	            R32cell6.setCellValue(record.getR32_moreThan3YearsTo5Years().doubleValue());
	            R32cell6.setCellStyle(numberStyle);
	        } else {
	            R32cell6.setCellValue("");
	            R32cell6.setCellStyle(textStyle);
	        }

	        Cell R32cell7 = row.createCell(8);
	        if (record.getR32_moreThan5YearsTo10Years() != null) {
	            R32cell7.setCellValue(record.getR32_moreThan5YearsTo10Years().doubleValue());
	            R32cell7.setCellStyle(numberStyle);
	        } else {
	            R32cell7.setCellValue("");
	            R32cell7.setCellStyle(textStyle);
	        }

	        Cell R32cell8 = row.createCell(9);
	        if (record.getR32_moreThan10Years() != null) {
	            R32cell8.setCellValue(record.getR32_moreThan10Years().doubleValue());
	            R32cell8.setCellStyle(numberStyle);
	        } else {
	            R32cell8.setCellValue("");
	            R32cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(32);
	        Cell R33cell1 = row.createCell(2);
	        if (record.getR33_upTo1Month() != null) {
	            R33cell1.setCellValue(record.getR33_upTo1Month().doubleValue());
	            R33cell1.setCellStyle(numberStyle);
	        } else {
	            R33cell1.setCellValue("");
	            R33cell1.setCellStyle(textStyle);
	        }

	        Cell R33cell2 = row.createCell(3);
	        if (record.getR33_moreThan1MonthTo3Months() != null) {
	            R33cell2.setCellValue(record.getR33_moreThan1MonthTo3Months().doubleValue());
	            R33cell2.setCellStyle(numberStyle);
	        } else {
	            R33cell2.setCellValue("");
	            R33cell2.setCellStyle(textStyle);
	        }

	        Cell R33cell3 = row.createCell(4);
	        if (record.getR33_moreThan3MonthTo6Months() != null) {
	            R33cell3.setCellValue(record.getR33_moreThan3MonthTo6Months().doubleValue());
	            R33cell3.setCellStyle(numberStyle);
	        } else {
	            R33cell3.setCellValue("");
	            R33cell3.setCellStyle(textStyle);
	        }

	        Cell R33cell4 = row.createCell(5);
	        if (record.getR33_moreThan6MonthTo12Months() != null) {
	            R33cell4.setCellValue(record.getR33_moreThan6MonthTo12Months().doubleValue());
	            R33cell4.setCellStyle(numberStyle);
	        } else {
	            R33cell4.setCellValue("");
	            R33cell4.setCellStyle(textStyle);
	        }

	        Cell R33cell5 = row.createCell(6);
	        if (record.getR33_moreThan12MonthTo3Years() != null) {
	            R33cell5.setCellValue(record.getR33_moreThan12MonthTo3Years().doubleValue());
	            R33cell5.setCellStyle(numberStyle);
	        } else {
	            R33cell5.setCellValue("");
	            R33cell5.setCellStyle(textStyle);
	        }

	        Cell R33cell6 = row.createCell(7);
	        if (record.getR33_moreThan3YearsTo5Years() != null) {
	            R33cell6.setCellValue(record.getR33_moreThan3YearsTo5Years().doubleValue());
	            R33cell6.setCellStyle(numberStyle);
	        } else {
	            R33cell6.setCellValue("");
	            R33cell6.setCellStyle(textStyle);
	        }

	        Cell R33cell7 = row.createCell(8);
	        if (record.getR33_moreThan5YearsTo10Years() != null) {
	            R33cell7.setCellValue(record.getR33_moreThan5YearsTo10Years().doubleValue());
	            R33cell7.setCellStyle(numberStyle);
	        } else {
	            R33cell7.setCellValue("");
	            R33cell7.setCellStyle(textStyle);
	        }

	        Cell R33cell8 = row.createCell(9);
	        if (record.getR33_moreThan10Years() != null) {
	            R33cell8.setCellValue(record.getR33_moreThan10Years().doubleValue());
	            R33cell8.setCellStyle(numberStyle);
	        } else {
	            R33cell8.setCellValue("");
	            R33cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(33);
	        Cell R34cell1 = row.createCell(2);
	        if (record.getR34_upTo1Month() != null) {
	            R34cell1.setCellValue(record.getR34_upTo1Month().doubleValue());
	            R34cell1.setCellStyle(numberStyle);
	        } else {
	            R34cell1.setCellValue("");
	            R34cell1.setCellStyle(textStyle);
	        }

	        Cell R34cell2 = row.createCell(3);
	        if (record.getR34_moreThan1MonthTo3Months() != null) {
	            R34cell2.setCellValue(record.getR34_moreThan1MonthTo3Months().doubleValue());
	            R34cell2.setCellStyle(numberStyle);
	        } else {
	            R34cell2.setCellValue("");
	            R34cell2.setCellStyle(textStyle);
	        }

	        Cell R34cell3 = row.createCell(4);
	        if (record.getR34_moreThan3MonthTo6Months() != null) {
	            R34cell3.setCellValue(record.getR34_moreThan3MonthTo6Months().doubleValue());
	            R34cell3.setCellStyle(numberStyle);
	        } else {
	            R34cell3.setCellValue("");
	            R34cell3.setCellStyle(textStyle);
	        }

	        Cell R34cell4 = row.createCell(5);
	        if (record.getR34_moreThan6MonthTo12Months() != null) {
	            R34cell4.setCellValue(record.getR34_moreThan6MonthTo12Months().doubleValue());
	            R34cell4.setCellStyle(numberStyle);
	        } else {
	            R34cell4.setCellValue("");
	            R34cell4.setCellStyle(textStyle);
	        }

	        Cell R34cell5 = row.createCell(6);
	        if (record.getR34_moreThan12MonthTo3Years() != null) {
	            R34cell5.setCellValue(record.getR34_moreThan12MonthTo3Years().doubleValue());
	            R34cell5.setCellStyle(numberStyle);
	        } else {
	            R34cell5.setCellValue("");
	            R34cell5.setCellStyle(textStyle);
	        }

	        Cell R34cell6 = row.createCell(7);
	        if (record.getR34_moreThan3YearsTo5Years() != null) {
	            R34cell6.setCellValue(record.getR34_moreThan3YearsTo5Years().doubleValue());
	            R34cell6.setCellStyle(numberStyle);
	        } else {
	            R34cell6.setCellValue("");
	            R34cell6.setCellStyle(textStyle);
	        }

	        Cell R34cell7 = row.createCell(8);
	        if (record.getR34_moreThan5YearsTo10Years() != null) {
	            R34cell7.setCellValue(record.getR34_moreThan5YearsTo10Years().doubleValue());
	            R34cell7.setCellStyle(numberStyle);
	        } else {
	            R34cell7.setCellValue("");
	            R34cell7.setCellStyle(textStyle);
	        }

	        Cell R34cell8 = row.createCell(9);
	        if (record.getR34_moreThan10Years() != null) {
	            R34cell8.setCellValue(record.getR34_moreThan10Years().doubleValue());
	            R34cell8.setCellStyle(numberStyle);
	        } else {
	            R34cell8.setCellValue("");
	            R34cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(35);
	        Cell R36cell1 = row.createCell(2);
	        if (record.getR36_upTo1Month() != null) {
	            R36cell1.setCellValue(record.getR36_upTo1Month().doubleValue());
	            R36cell1.setCellStyle(numberStyle);
	        } else {
	            R36cell1.setCellValue("");
	            R36cell1.setCellStyle(textStyle);
	        }

	        Cell R36cell2 = row.createCell(3);
	        if (record.getR36_moreThan1MonthTo3Months() != null) {
	            R36cell2.setCellValue(record.getR36_moreThan1MonthTo3Months().doubleValue());
	            R36cell2.setCellStyle(numberStyle);
	        } else {
	            R36cell2.setCellValue("");
	            R36cell2.setCellStyle(textStyle);
	        }

	        Cell R36cell3 = row.createCell(4);
	        if (record.getR36_moreThan3MonthTo6Months() != null) {
	            R36cell3.setCellValue(record.getR36_moreThan3MonthTo6Months().doubleValue());
	            R36cell3.setCellStyle(numberStyle);
	        } else {
	            R36cell3.setCellValue("");
	            R36cell3.setCellStyle(textStyle);
	        }

	        Cell R36cell4 = row.createCell(5);
	        if (record.getR36_moreThan6MonthTo12Months() != null) {
	            R36cell4.setCellValue(record.getR36_moreThan6MonthTo12Months().doubleValue());
	            R36cell4.setCellStyle(numberStyle);
	        } else {
	            R36cell4.setCellValue("");
	            R36cell4.setCellStyle(textStyle);
	        }

	        Cell R36cell5 = row.createCell(6);
	        if (record.getR36_moreThan12MonthTo3Years() != null) {
	            R36cell5.setCellValue(record.getR36_moreThan12MonthTo3Years().doubleValue());
	            R36cell5.setCellStyle(numberStyle);
	        } else {
	            R36cell5.setCellValue("");
	            R36cell5.setCellStyle(textStyle);
	        }

	        Cell R36cell6 = row.createCell(7);
	        if (record.getR36_moreThan3YearsTo5Years() != null) {
	            R36cell6.setCellValue(record.getR36_moreThan3YearsTo5Years().doubleValue());
	            R36cell6.setCellStyle(numberStyle);
	        } else {
	            R36cell6.setCellValue("");
	            R36cell6.setCellStyle(textStyle);
	        }

	        Cell R36cell7 = row.createCell(8);
	        if (record.getR36_moreThan5YearsTo10Years() != null) {
	            R36cell7.setCellValue(record.getR36_moreThan5YearsTo10Years().doubleValue());
	            R36cell7.setCellStyle(numberStyle);
	        } else {
	            R36cell7.setCellValue("");
	            R36cell7.setCellStyle(textStyle);
	        }

	        Cell R36cell8 = row.createCell(9);
	        if (record.getR36_moreThan10Years() != null) {
	            R36cell8.setCellValue(record.getR36_moreThan10Years().doubleValue());
	            R36cell8.setCellStyle(numberStyle);
	        } else {
	            R36cell8.setCellValue("");
	            R36cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(36);
	        Cell R37cell1 = row.createCell(2);
	        if (record.getR37_upTo1Month() != null) {
	            R37cell1.setCellValue(record.getR37_upTo1Month().doubleValue());
	            R37cell1.setCellStyle(numberStyle);
	        } else {
	            R37cell1.setCellValue("");
	            R37cell1.setCellStyle(textStyle);
	        }

	        Cell R37cell2 = row.createCell(3);
	        if (record.getR37_moreThan1MonthTo3Months() != null) {
	            R37cell2.setCellValue(record.getR37_moreThan1MonthTo3Months().doubleValue());
	            R37cell2.setCellStyle(numberStyle);
	        } else {
	            R37cell2.setCellValue("");
	            R37cell2.setCellStyle(textStyle);
	        }

	        Cell R37cell3 = row.createCell(4);
	        if (record.getR37_moreThan3MonthTo6Months() != null) {
	            R37cell3.setCellValue(record.getR37_moreThan3MonthTo6Months().doubleValue());
	            R37cell3.setCellStyle(numberStyle);
	        } else {
	            R37cell3.setCellValue("");
	            R37cell3.setCellStyle(textStyle);
	        }

	        Cell R37cell4 = row.createCell(5);
	        if (record.getR37_moreThan6MonthTo12Months() != null) {
	            R37cell4.setCellValue(record.getR37_moreThan6MonthTo12Months().doubleValue());
	            R37cell4.setCellStyle(numberStyle);
	        } else {
	            R37cell4.setCellValue("");
	            R37cell4.setCellStyle(textStyle);
	        }

	        Cell R37cell5 = row.createCell(6);
	        if (record.getR37_moreThan12MonthTo3Years() != null) {
	            R37cell5.setCellValue(record.getR37_moreThan12MonthTo3Years().doubleValue());
	            R37cell5.setCellStyle(numberStyle);
	        } else {
	            R37cell5.setCellValue("");
	            R37cell5.setCellStyle(textStyle);
	        }

	        Cell R37cell6 = row.createCell(7);
	        if (record.getR37_moreThan3YearsTo5Years() != null) {
	            R37cell6.setCellValue(record.getR37_moreThan3YearsTo5Years().doubleValue());
	            R37cell6.setCellStyle(numberStyle);
	        } else {
	            R37cell6.setCellValue("");
	            R37cell6.setCellStyle(textStyle);
	        }

	        Cell R37cell7 = row.createCell(8);
	        if (record.getR37_moreThan5YearsTo10Years() != null) {
	            R37cell7.setCellValue(record.getR37_moreThan5YearsTo10Years().doubleValue());
	            R37cell7.setCellStyle(numberStyle);
	        } else {
	            R37cell7.setCellValue("");
	            R37cell7.setCellStyle(textStyle);
	        }

	        Cell R37cell8 = row.createCell(9);
	        if (record.getR37_moreThan10Years() != null) {
	            R37cell8.setCellValue(record.getR37_moreThan10Years().doubleValue());
	            R37cell8.setCellStyle(numberStyle);
	        } else {
	            R37cell8.setCellValue("");
	            R37cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(37);
	        Cell R38cell1 = row.createCell(2);
	        if (record.getR38_upTo1Month() != null) {
	            R38cell1.setCellValue(record.getR38_upTo1Month().doubleValue());
	            R38cell1.setCellStyle(numberStyle);
	        } else {
	            R38cell1.setCellValue("");
	            R38cell1.setCellStyle(textStyle);
	        }

	        Cell R38cell2 = row.createCell(3);
	        if (record.getR38_moreThan1MonthTo3Months() != null) {
	            R38cell2.setCellValue(record.getR38_moreThan1MonthTo3Months().doubleValue());
	            R38cell2.setCellStyle(numberStyle);
	        } else {
	            R38cell2.setCellValue("");
	            R38cell2.setCellStyle(textStyle);
	        }

	        Cell R38cell3 = row.createCell(4);
	        if (record.getR38_moreThan3MonthTo6Months() != null) {
	            R38cell3.setCellValue(record.getR38_moreThan3MonthTo6Months().doubleValue());
	            R38cell3.setCellStyle(numberStyle);
	        } else {
	            R38cell3.setCellValue("");
	            R38cell3.setCellStyle(textStyle);
	        }

	        Cell R38cell4 = row.createCell(5);
	        if (record.getR38_moreThan6MonthTo12Months() != null) {
	            R38cell4.setCellValue(record.getR38_moreThan6MonthTo12Months().doubleValue());
	            R38cell4.setCellStyle(numberStyle);
	        } else {
	            R38cell4.setCellValue("");
	            R38cell4.setCellStyle(textStyle);
	        }

	        Cell R38cell5 = row.createCell(6);
	        if (record.getR38_moreThan12MonthTo3Years() != null) {
	            R38cell5.setCellValue(record.getR38_moreThan12MonthTo3Years().doubleValue());
	            R38cell5.setCellStyle(numberStyle);
	        } else {
	            R38cell5.setCellValue("");
	            R38cell5.setCellStyle(textStyle);
	        }

	        Cell R38cell6 = row.createCell(7);
	        if (record.getR38_moreThan3YearsTo5Years() != null) {
	            R38cell6.setCellValue(record.getR38_moreThan3YearsTo5Years().doubleValue());
	            R38cell6.setCellStyle(numberStyle);
	        } else {
	            R38cell6.setCellValue("");
	            R38cell6.setCellStyle(textStyle);
	        }

	        Cell R38cell7 = row.createCell(8);
	        if (record.getR38_moreThan5YearsTo10Years() != null) {
	            R38cell7.setCellValue(record.getR38_moreThan5YearsTo10Years().doubleValue());
	            R38cell7.setCellStyle(numberStyle);
	        } else {
	            R38cell7.setCellValue("");
	            R38cell7.setCellStyle(textStyle);
	        }

	        Cell R38cell8 = row.createCell(9);
	        if (record.getR38_moreThan10Years() != null) {
	            R38cell8.setCellValue(record.getR38_moreThan10Years().doubleValue());
	            R38cell8.setCellStyle(numberStyle);
	        } else {
	            R38cell8.setCellValue("");
	            R38cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(38);
	        Cell R39cell1 = row.createCell(2);
	        if (record.getR39_upTo1Month() != null) {
	            R39cell1.setCellValue(record.getR39_upTo1Month().doubleValue());
	            R39cell1.setCellStyle(numberStyle);
	        } else {
	            R39cell1.setCellValue("");
	            R39cell1.setCellStyle(textStyle);
	        }

	        Cell R39cell2 = row.createCell(3);
	        if (record.getR39_moreThan1MonthTo3Months() != null) {
	            R39cell2.setCellValue(record.getR39_moreThan1MonthTo3Months().doubleValue());
	            R39cell2.setCellStyle(numberStyle);
	        } else {
	            R39cell2.setCellValue("");
	            R39cell2.setCellStyle(textStyle);
	        }

	        Cell R39cell3 = row.createCell(4);
	        if (record.getR39_moreThan3MonthTo6Months() != null) {
	            R39cell3.setCellValue(record.getR39_moreThan3MonthTo6Months().doubleValue());
	            R39cell3.setCellStyle(numberStyle);
	        } else {
	            R39cell3.setCellValue("");
	            R39cell3.setCellStyle(textStyle);
	        }

	        Cell R39cell4 = row.createCell(5);
	        if (record.getR39_moreThan6MonthTo12Months() != null) {
	            R39cell4.setCellValue(record.getR39_moreThan6MonthTo12Months().doubleValue());
	            R39cell4.setCellStyle(numberStyle);
	        } else {
	            R39cell4.setCellValue("");
	            R39cell4.setCellStyle(textStyle);
	        }

	        Cell R39cell5 = row.createCell(6);
	        if (record.getR39_moreThan12MonthTo3Years() != null) {
	            R39cell5.setCellValue(record.getR39_moreThan12MonthTo3Years().doubleValue());
	            R39cell5.setCellStyle(numberStyle);
	        } else {
	            R39cell5.setCellValue("");
	            R39cell5.setCellStyle(textStyle);
	        }

	        Cell R39cell6 = row.createCell(7);
	        if (record.getR39_moreThan3YearsTo5Years() != null) {
	            R39cell6.setCellValue(record.getR39_moreThan3YearsTo5Years().doubleValue());
	            R39cell6.setCellStyle(numberStyle);
	        } else {
	            R39cell6.setCellValue("");
	            R39cell6.setCellStyle(textStyle);
	        }

	        Cell R39cell7 = row.createCell(8);
	        if (record.getR39_moreThan5YearsTo10Years() != null) {
	            R39cell7.setCellValue(record.getR39_moreThan5YearsTo10Years().doubleValue());
	            R39cell7.setCellStyle(numberStyle);
	        } else {
	            R39cell7.setCellValue("");
	            R39cell7.setCellStyle(textStyle);
	        }

	        Cell R39cell8 = row.createCell(9);
	        if (record.getR39_moreThan10Years() != null) {
	            R39cell8.setCellValue(record.getR39_moreThan10Years().doubleValue());
	            R39cell8.setCellStyle(numberStyle);
	        } else {
	            R39cell8.setCellValue("");
	            R39cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(39);
	        Cell R40cell1 = row.createCell(2);
	        if (record.getR40_upTo1Month() != null) {
	            R40cell1.setCellValue(record.getR40_upTo1Month().doubleValue());
	            R40cell1.setCellStyle(numberStyle);
	        } else {
	            R40cell1.setCellValue("");
	            R40cell1.setCellStyle(textStyle);
	        }

	        Cell R40cell2 = row.createCell(3);
	        if (record.getR40_moreThan1MonthTo3Months() != null) {
	            R40cell2.setCellValue(record.getR40_moreThan1MonthTo3Months().doubleValue());
	            R40cell2.setCellStyle(numberStyle);
	        } else {
	            R40cell2.setCellValue("");
	            R40cell2.setCellStyle(textStyle);
	        }

	        Cell R40cell3 = row.createCell(4);
	        if (record.getR40_moreThan3MonthTo6Months() != null) {
	            R40cell3.setCellValue(record.getR40_moreThan3MonthTo6Months().doubleValue());
	            R40cell3.setCellStyle(numberStyle);
	        } else {
	            R40cell3.setCellValue("");
	            R40cell3.setCellStyle(textStyle);
	        }

	        Cell R40cell4 = row.createCell(5);
	        if (record.getR40_moreThan6MonthTo12Months() != null) {
	            R40cell4.setCellValue(record.getR40_moreThan6MonthTo12Months().doubleValue());
	            R40cell4.setCellStyle(numberStyle);
	        } else {
	            R40cell4.setCellValue("");
	            R40cell4.setCellStyle(textStyle);
	        }

	        Cell R40cell5 = row.createCell(6);
	        if (record.getR40_moreThan12MonthTo3Years() != null) {
	            R40cell5.setCellValue(record.getR40_moreThan12MonthTo3Years().doubleValue());
	            R40cell5.setCellStyle(numberStyle);
	        } else {
	            R40cell5.setCellValue("");
	            R40cell5.setCellStyle(textStyle);
	        }

	        Cell R40cell6 = row.createCell(7);
	        if (record.getR40_moreThan3YearsTo5Years() != null) {
	            R40cell6.setCellValue(record.getR40_moreThan3YearsTo5Years().doubleValue());
	            R40cell6.setCellStyle(numberStyle);
	        } else {
	            R40cell6.setCellValue("");
	            R40cell6.setCellStyle(textStyle);
	        }

	        Cell R40cell7 = row.createCell(8);
	        if (record.getR40_moreThan5YearsTo10Years() != null) {
	            R40cell7.setCellValue(record.getR40_moreThan5YearsTo10Years().doubleValue());
	            R40cell7.setCellStyle(numberStyle);
	        } else {
	            R40cell7.setCellValue("");
	            R40cell7.setCellStyle(textStyle);
	        }

	        Cell R40cell8 = row.createCell(9);
	        if (record.getR40_moreThan10Years() != null) {
	            R40cell8.setCellValue(record.getR40_moreThan10Years().doubleValue());
	            R40cell8.setCellStyle(numberStyle);
	        } else {
	            R40cell8.setCellValue("");
	            R40cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(40);
	        Cell R41cell1 = row.createCell(2);
	        if (record.getR41_upTo1Month() != null) {
	            R41cell1.setCellValue(record.getR41_upTo1Month().doubleValue());
	            R41cell1.setCellStyle(numberStyle);
	        } else {
	            R41cell1.setCellValue("");
	            R41cell1.setCellStyle(textStyle);
	        }

	        Cell R41cell2 = row.createCell(3);
	        if (record.getR41_moreThan1MonthTo3Months() != null) {
	            R41cell2.setCellValue(record.getR41_moreThan1MonthTo3Months().doubleValue());
	            R41cell2.setCellStyle(numberStyle);
	        } else {
	            R41cell2.setCellValue("");
	            R41cell2.setCellStyle(textStyle);
	        }

	        Cell R41cell3 = row.createCell(4);
	        if (record.getR41_moreThan3MonthTo6Months() != null) {
	            R41cell3.setCellValue(record.getR41_moreThan3MonthTo6Months().doubleValue());
	            R41cell3.setCellStyle(numberStyle);
	        } else {
	            R41cell3.setCellValue("");
	            R41cell3.setCellStyle(textStyle);
	        }

	        Cell R41cell4 = row.createCell(5);
	        if (record.getR41_moreThan6MonthTo12Months() != null) {
	            R41cell4.setCellValue(record.getR41_moreThan6MonthTo12Months().doubleValue());
	            R41cell4.setCellStyle(numberStyle);
	        } else {
	            R41cell4.setCellValue("");
	            R41cell4.setCellStyle(textStyle);
	        }

	        Cell R41cell5 = row.createCell(6);
	        if (record.getR41_moreThan12MonthTo3Years() != null) {
	            R41cell5.setCellValue(record.getR41_moreThan12MonthTo3Years().doubleValue());
	            R41cell5.setCellStyle(numberStyle);
	        } else {
	            R41cell5.setCellValue("");
	            R41cell5.setCellStyle(textStyle);
	        }

	        Cell R41cell6 = row.createCell(7);
	        if (record.getR41_moreThan3YearsTo5Years() != null) {
	            R41cell6.setCellValue(record.getR41_moreThan3YearsTo5Years().doubleValue());
	            R41cell6.setCellStyle(numberStyle);
	        } else {
	            R41cell6.setCellValue("");
	            R41cell6.setCellStyle(textStyle);
	        }

	        Cell R41cell7 = row.createCell(8);
	        if (record.getR41_moreThan5YearsTo10Years() != null) {
	            R41cell7.setCellValue(record.getR41_moreThan5YearsTo10Years().doubleValue());
	            R41cell7.setCellStyle(numberStyle);
	        } else {
	            R41cell7.setCellValue("");
	            R41cell7.setCellStyle(textStyle);
	        }

	        Cell R41cell8 = row.createCell(9);
	        if (record.getR41_moreThan10Years() != null) {
	            R41cell8.setCellValue(record.getR41_moreThan10Years().doubleValue());
	            R41cell8.setCellStyle(numberStyle);
	        } else {
	            R41cell8.setCellValue("");
	            R41cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(41);
	        Cell R42cell1 = row.createCell(2);
	        if (record.getR42_upTo1Month() != null) {
	            R42cell1.setCellValue(record.getR42_upTo1Month().doubleValue());
	            R42cell1.setCellStyle(numberStyle);
	        } else {
	            R42cell1.setCellValue("");
	            R42cell1.setCellStyle(textStyle);
	        }

	        Cell R42cell2 = row.createCell(3);
	        if (record.getR42_moreThan1MonthTo3Months() != null) {
	            R42cell2.setCellValue(record.getR42_moreThan1MonthTo3Months().doubleValue());
	            R42cell2.setCellStyle(numberStyle);
	        } else {
	            R42cell2.setCellValue("");
	            R42cell2.setCellStyle(textStyle);
	        }

	        Cell R42cell3 = row.createCell(4);
	        if (record.getR42_moreThan3MonthTo6Months() != null) {
	            R42cell3.setCellValue(record.getR42_moreThan3MonthTo6Months().doubleValue());
	            R42cell3.setCellStyle(numberStyle);
	        } else {
	            R42cell3.setCellValue("");
	            R42cell3.setCellStyle(textStyle);
	        }

	        Cell R42cell4 = row.createCell(5);
	        if (record.getR42_moreThan6MonthTo12Months() != null) {
	            R42cell4.setCellValue(record.getR42_moreThan6MonthTo12Months().doubleValue());
	            R42cell4.setCellStyle(numberStyle);
	        } else {
	            R42cell4.setCellValue("");
	            R42cell4.setCellStyle(textStyle);
	        }

	        Cell R42cell5 = row.createCell(6);
	        if (record.getR42_moreThan12MonthTo3Years() != null) {
	            R42cell5.setCellValue(record.getR42_moreThan12MonthTo3Years().doubleValue());
	            R42cell5.setCellStyle(numberStyle);
	        } else {
	            R42cell5.setCellValue("");
	            R42cell5.setCellStyle(textStyle);
	        }

	        Cell R42cell6 = row.createCell(7);
	        if (record.getR42_moreThan3YearsTo5Years() != null) {
	            R42cell6.setCellValue(record.getR42_moreThan3YearsTo5Years().doubleValue());
	            R42cell6.setCellStyle(numberStyle);
	        } else {
	            R42cell6.setCellValue("");
	            R42cell6.setCellStyle(textStyle);
	        }

	        Cell R42cell7 = row.createCell(8);
	        if (record.getR42_moreThan5YearsTo10Years() != null) {
	            R42cell7.setCellValue(record.getR42_moreThan5YearsTo10Years().doubleValue());
	            R42cell7.setCellStyle(numberStyle);
	        } else {
	            R42cell7.setCellValue("");
	            R42cell7.setCellStyle(textStyle);
	        }

	        Cell R42cell8 = row.createCell(9);
	        if (record.getR42_moreThan10Years() != null) {
	            R42cell8.setCellValue(record.getR42_moreThan10Years().doubleValue());
	            R42cell8.setCellStyle(numberStyle);
	        } else {
	            R42cell8.setCellValue("");
	            R42cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(42);
	        Cell R43cell1 = row.createCell(2);
	        if (record.getR43_upTo1Month() != null) {
	            R43cell1.setCellValue(record.getR43_upTo1Month().doubleValue());
	            R43cell1.setCellStyle(numberStyle);
	        } else {
	            R43cell1.setCellValue("");
	            R43cell1.setCellStyle(textStyle);
	        }

	        Cell R43cell2 = row.createCell(3);
	        if (record.getR43_moreThan1MonthTo3Months() != null) {
	            R43cell2.setCellValue(record.getR43_moreThan1MonthTo3Months().doubleValue());
	            R43cell2.setCellStyle(numberStyle);
	        } else {
	            R43cell2.setCellValue("");
	            R43cell2.setCellStyle(textStyle);
	        }

	        Cell R43cell3 = row.createCell(4);
	        if (record.getR43_moreThan3MonthTo6Months() != null) {
	            R43cell3.setCellValue(record.getR43_moreThan3MonthTo6Months().doubleValue());
	            R43cell3.setCellStyle(numberStyle);
	        } else {
	            R43cell3.setCellValue("");
	            R43cell3.setCellStyle(textStyle);
	        }

	        Cell R43cell4 = row.createCell(5);
	        if (record.getR43_moreThan6MonthTo12Months() != null) {
	            R43cell4.setCellValue(record.getR43_moreThan6MonthTo12Months().doubleValue());
	            R43cell4.setCellStyle(numberStyle);
	        } else {
	            R43cell4.setCellValue("");
	            R43cell4.setCellStyle(textStyle);
	        }

	        Cell R43cell5 = row.createCell(6);
	        if (record.getR43_moreThan12MonthTo3Years() != null) {
	            R43cell5.setCellValue(record.getR43_moreThan12MonthTo3Years().doubleValue());
	            R43cell5.setCellStyle(numberStyle);
	        } else {
	            R43cell5.setCellValue("");
	            R43cell5.setCellStyle(textStyle);
	        }

	        Cell R43cell6 = row.createCell(7);
	        if (record.getR43_moreThan3YearsTo5Years() != null) {
	            R43cell6.setCellValue(record.getR43_moreThan3YearsTo5Years().doubleValue());
	            R43cell6.setCellStyle(numberStyle);
	        } else {
	            R43cell6.setCellValue("");
	            R43cell6.setCellStyle(textStyle);
	        }

	        Cell R43cell7 = row.createCell(8);
	        if (record.getR43_moreThan5YearsTo10Years() != null) {
	            R43cell7.setCellValue(record.getR43_moreThan5YearsTo10Years().doubleValue());
	            R43cell7.setCellStyle(numberStyle);
	        } else {
	            R43cell7.setCellValue("");
	            R43cell7.setCellStyle(textStyle);
	        }

	        Cell R43cell8 = row.createCell(9);
	        if (record.getR43_moreThan10Years() != null) {
	            R43cell8.setCellValue(record.getR43_moreThan10Years().doubleValue());
	            R43cell8.setCellStyle(numberStyle);
	        } else {
	            R43cell8.setCellValue("");
	            R43cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(43);
	        Cell R44cell1 = row.createCell(2);
	        if (record.getR44_upTo1Month() != null) {
	            R44cell1.setCellValue(record.getR44_upTo1Month().doubleValue());
	            R44cell1.setCellStyle(numberStyle);
	        } else {
	            R44cell1.setCellValue("");
	            R44cell1.setCellStyle(textStyle);
	        }

	        Cell R44cell2 = row.createCell(3);
	        if (record.getR44_moreThan1MonthTo3Months() != null) {
	            R44cell2.setCellValue(record.getR44_moreThan1MonthTo3Months().doubleValue());
	            R44cell2.setCellStyle(numberStyle);
	        } else {
	            R44cell2.setCellValue("");
	            R44cell2.setCellStyle(textStyle);
	        }

	        Cell R44cell3 = row.createCell(4);
	        if (record.getR44_moreThan3MonthTo6Months() != null) {
	            R44cell3.setCellValue(record.getR44_moreThan3MonthTo6Months().doubleValue());
	            R44cell3.setCellStyle(numberStyle);
	        } else {
	            R44cell3.setCellValue("");
	            R44cell3.setCellStyle(textStyle);
	        }

	        Cell R44cell4 = row.createCell(5);
	        if (record.getR44_moreThan6MonthTo12Months() != null) {
	            R44cell4.setCellValue(record.getR44_moreThan6MonthTo12Months().doubleValue());
	            R44cell4.setCellStyle(numberStyle);
	        } else {
	            R44cell4.setCellValue("");
	            R44cell4.setCellStyle(textStyle);
	        }

	        Cell R44cell5 = row.createCell(6);
	        if (record.getR44_moreThan12MonthTo3Years() != null) {
	            R44cell5.setCellValue(record.getR44_moreThan12MonthTo3Years().doubleValue());
	            R44cell5.setCellStyle(numberStyle);
	        } else {
	            R44cell5.setCellValue("");
	            R44cell5.setCellStyle(textStyle);
	        }

	        Cell R44cell6 = row.createCell(7);
	        if (record.getR44_moreThan3YearsTo5Years() != null) {
	            R44cell6.setCellValue(record.getR44_moreThan3YearsTo5Years().doubleValue());
	            R44cell6.setCellStyle(numberStyle);
	        } else {
	            R44cell6.setCellValue("");
	            R44cell6.setCellStyle(textStyle);
	        }

	        Cell R44cell7 = row.createCell(8);
	        if (record.getR44_moreThan5YearsTo10Years() != null) {
	            R44cell7.setCellValue(record.getR44_moreThan5YearsTo10Years().doubleValue());
	            R44cell7.setCellStyle(numberStyle);
	        } else {
	            R44cell7.setCellValue("");
	            R44cell7.setCellStyle(textStyle);
	        }

	        Cell R44cell8 = row.createCell(9);
	        if (record.getR44_moreThan10Years() != null) {
	            R44cell8.setCellValue(record.getR44_moreThan10Years().doubleValue());
	            R44cell8.setCellStyle(numberStyle);
	        } else {
	            R44cell8.setCellValue("");
	            R44cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(44);
	        Cell R45cell1 = row.createCell(2);
	        if (record.getR45_upTo1Month() != null) {
	            R45cell1.setCellValue(record.getR45_upTo1Month().doubleValue());
	            R45cell1.setCellStyle(numberStyle);
	        } else {
	            R45cell1.setCellValue("");
	            R45cell1.setCellStyle(textStyle);
	        }

	        Cell R45cell2 = row.createCell(3);
	        if (record.getR45_moreThan1MonthTo3Months() != null) {
	            R45cell2.setCellValue(record.getR45_moreThan1MonthTo3Months().doubleValue());
	            R45cell2.setCellStyle(numberStyle);
	        } else {
	            R45cell2.setCellValue("");
	            R45cell2.setCellStyle(textStyle);
	        }

	        Cell R45cell3 = row.createCell(4);
	        if (record.getR45_moreThan3MonthTo6Months() != null) {
	            R45cell3.setCellValue(record.getR45_moreThan3MonthTo6Months().doubleValue());
	            R45cell3.setCellStyle(numberStyle);
	        } else {
	            R45cell3.setCellValue("");
	            R45cell3.setCellStyle(textStyle);
	        }

	        Cell R45cell4 = row.createCell(5);
	        if (record.getR45_moreThan6MonthTo12Months() != null) {
	            R45cell4.setCellValue(record.getR45_moreThan6MonthTo12Months().doubleValue());
	            R45cell4.setCellStyle(numberStyle);
	        } else {
	            R45cell4.setCellValue("");
	            R45cell4.setCellStyle(textStyle);
	        }

	        Cell R45cell5 = row.createCell(6);
	        if (record.getR45_moreThan12MonthTo3Years() != null) {
	            R45cell5.setCellValue(record.getR45_moreThan12MonthTo3Years().doubleValue());
	            R45cell5.setCellStyle(numberStyle);
	        } else {
	            R45cell5.setCellValue("");
	            R45cell5.setCellStyle(textStyle);
	        }

	        Cell R45cell6 = row.createCell(7);
	        if (record.getR45_moreThan3YearsTo5Years() != null) {
	            R45cell6.setCellValue(record.getR45_moreThan3YearsTo5Years().doubleValue());
	            R45cell6.setCellStyle(numberStyle);
	        } else {
	            R45cell6.setCellValue("");
	            R45cell6.setCellStyle(textStyle);
	        }

	        Cell R45cell7 = row.createCell(8);
	        if (record.getR45_moreThan5YearsTo10Years() != null) {
	            R45cell7.setCellValue(record.getR45_moreThan5YearsTo10Years().doubleValue());
	            R45cell7.setCellStyle(numberStyle);
	        } else {
	            R45cell7.setCellValue("");
	            R45cell7.setCellStyle(textStyle);
	        }

	        Cell R45cell8 = row.createCell(9);
	        if (record.getR45_moreThan10Years() != null) {
	            R45cell8.setCellValue(record.getR45_moreThan10Years().doubleValue());
	            R45cell8.setCellStyle(numberStyle);
	        } else {
	            R45cell8.setCellValue("");
	            R45cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(45);
	        Cell R46cell1 = row.createCell(2);
	        if (record.getR46_upTo1Month() != null) {
	            R46cell1.setCellValue(record.getR46_upTo1Month().doubleValue());
	            R46cell1.setCellStyle(numberStyle);
	        } else {
	            R46cell1.setCellValue("");
	            R46cell1.setCellStyle(textStyle);
	        }

	        Cell R46cell2 = row.createCell(3);
	        if (record.getR46_moreThan1MonthTo3Months() != null) {
	            R46cell2.setCellValue(record.getR46_moreThan1MonthTo3Months().doubleValue());
	            R46cell2.setCellStyle(numberStyle);
	        } else {
	            R46cell2.setCellValue("");
	            R46cell2.setCellStyle(textStyle);
	        }

	        Cell R46cell3 = row.createCell(4);
	        if (record.getR46_moreThan3MonthTo6Months() != null) {
	            R46cell3.setCellValue(record.getR46_moreThan3MonthTo6Months().doubleValue());
	            R46cell3.setCellStyle(numberStyle);
	        } else {
	            R46cell3.setCellValue("");
	            R46cell3.setCellStyle(textStyle);
	        }

	        Cell R46cell4 = row.createCell(5);
	        if (record.getR46_moreThan6MonthTo12Months() != null) {
	            R46cell4.setCellValue(record.getR46_moreThan6MonthTo12Months().doubleValue());
	            R46cell4.setCellStyle(numberStyle);
	        } else {
	            R46cell4.setCellValue("");
	            R46cell4.setCellStyle(textStyle);
	        }

	        Cell R46cell5 = row.createCell(6);
	        if (record.getR46_moreThan12MonthTo3Years() != null) {
	            R46cell5.setCellValue(record.getR46_moreThan12MonthTo3Years().doubleValue());
	            R46cell5.setCellStyle(numberStyle);
	        } else {
	            R46cell5.setCellValue("");
	            R46cell5.setCellStyle(textStyle);
	        }

	        Cell R46cell6 = row.createCell(7);
	        if (record.getR46_moreThan3YearsTo5Years() != null) {
	            R46cell6.setCellValue(record.getR46_moreThan3YearsTo5Years().doubleValue());
	            R46cell6.setCellStyle(numberStyle);
	        } else {
	            R46cell6.setCellValue("");
	            R46cell6.setCellStyle(textStyle);
	        }

	        Cell R46cell7 = row.createCell(8);
	        if (record.getR46_moreThan5YearsTo10Years() != null) {
	            R46cell7.setCellValue(record.getR46_moreThan5YearsTo10Years().doubleValue());
	            R46cell7.setCellStyle(numberStyle);
	        } else {
	            R46cell7.setCellValue("");
	            R46cell7.setCellStyle(textStyle);
	        }

	        Cell R46cell8 = row.createCell(9);
	        if (record.getR46_moreThan10Years() != null) {
	            R46cell8.setCellValue(record.getR46_moreThan10Years().doubleValue());
	            R46cell8.setCellStyle(numberStyle);
	        } else {
	            R46cell8.setCellValue("");
	            R46cell8.setCellStyle(textStyle);
	        }
	        
	        
		    
	        row = sheet.getRow(47);
	        Cell R48cell9 = row.createCell(10);
	        if (record.getR48_nonRatioSensativeItems() != null) {
	            R48cell9.setCellValue(record.getR48_nonRatioSensativeItems().doubleValue());
	            R48cell9.setCellStyle(numberStyle);
	        } else {
	            R48cell9.setCellValue("");
	            R48cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(48);
	        Cell R49cell9 = row.createCell(10);
	        if (record.getR49_nonRatioSensativeItems() != null) {
	            R49cell9.setCellValue(record.getR49_nonRatioSensativeItems().doubleValue());
	            R49cell9.setCellStyle(numberStyle);
	        } else {
	            R49cell9.setCellValue("");
	            R49cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(49);
	        Cell R50cell9 = row.createCell(10);
	        if (record.getR50_nonRatioSensativeItems() != null) {
	            R50cell9.setCellValue(record.getR50_nonRatioSensativeItems().doubleValue());
	            R50cell9.setCellStyle(numberStyle);
	        } else {
	            R50cell9.setCellValue("");
	            R50cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(50);
	        Cell R51cell9 = row.createCell(10);
	        if (record.getR51_nonRatioSensativeItems() != null) {
	            R51cell9.setCellValue(record.getR51_nonRatioSensativeItems().doubleValue());
	            R51cell9.setCellStyle(numberStyle);
	        } else {
	            R51cell9.setCellValue("");
	            R51cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(51);
	        Cell R52cell9 = row.createCell(10);
	        if (record.getR52_nonRatioSensativeItems() != null) {
	            R52cell9.setCellValue(record.getR52_nonRatioSensativeItems().doubleValue());
	            R52cell9.setCellStyle(numberStyle);
	        } else {
	            R52cell9.setCellValue("");
	            R52cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(52);
	        Cell R53cell9 = row.createCell(10);
	        if (record.getR53_nonRatioSensativeItems() != null) {
	            R53cell9.setCellValue(record.getR53_nonRatioSensativeItems().doubleValue());
	            R53cell9.setCellStyle(numberStyle);
	        } else {
	            R53cell9.setCellValue("");
	            R53cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(53);
	        Cell R54cell9 = row.createCell(10);
	        if (record.getR54_nonRatioSensativeItems() != null) {
	            R54cell9.setCellValue(record.getR54_nonRatioSensativeItems().doubleValue());
	            R54cell9.setCellStyle(numberStyle);
	        } else {
	            R54cell9.setCellValue("");
	            R54cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(54);
	        Cell R55cell9 = row.createCell(10);
	        if (record.getR55_nonRatioSensativeItems() != null) {
	            R55cell9.setCellValue(record.getR55_nonRatioSensativeItems().doubleValue());
	            R55cell9.setCellStyle(numberStyle);
	        } else {
	            R55cell9.setCellValue("");
	            R55cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(55);
	        Cell R56cell9 = row.createCell(10);
	        if (record.getR56_nonRatioSensativeItems() != null) {
	            R56cell9.setCellValue(record.getR56_nonRatioSensativeItems().doubleValue());
	            R56cell9.setCellStyle(numberStyle);
	        } else {
	            R56cell9.setCellValue("");
	            R56cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(56);
	        Cell R57cell9 = row.createCell(10);
	        if (record.getR57_nonRatioSensativeItems() != null) {
	            R57cell9.setCellValue(record.getR57_nonRatioSensativeItems().doubleValue());
	            R57cell9.setCellStyle(numberStyle);
	        } else {
	            R57cell9.setCellValue("");
	            R57cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(57);
	        Cell R58cell9 = row.createCell(10);
	        if (record.getR58_nonRatioSensativeItems() != null) {
	            R58cell9.setCellValue(record.getR58_nonRatioSensativeItems().doubleValue());
	            R58cell9.setCellStyle(numberStyle);
	        } else {
	            R58cell9.setCellValue("");
	            R58cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(60);
	        Cell R61cell1 = row.createCell(2);
	        if (record.getR61_upTo1Month() != null) {
	            R61cell1.setCellValue(record.getR61_upTo1Month().doubleValue());
	            R61cell1.setCellStyle(numberStyle);
	        } else {
	            R61cell1.setCellValue("");
	            R61cell1.setCellStyle(textStyle);
	        }

	        Cell R61cell2 = row.createCell(3);
	        if (record.getR61_moreThan1MonthTo3Months() != null) {
	            R61cell2.setCellValue(record.getR61_moreThan1MonthTo3Months().doubleValue());
	            R61cell2.setCellStyle(numberStyle);
	        } else {
	            R61cell2.setCellValue("");
	            R61cell2.setCellStyle(textStyle);
	        }

	        Cell R61cell3 = row.createCell(4);
	        if (record.getR61_moreThan3MonthTo6Months() != null) {
	            R61cell3.setCellValue(record.getR61_moreThan3MonthTo6Months().doubleValue());
	            R61cell3.setCellStyle(numberStyle);
	        } else {
	            R61cell3.setCellValue("");
	            R61cell3.setCellStyle(textStyle);
	        }

	        Cell R61cell4 = row.createCell(5);
	        if (record.getR61_moreThan6MonthTo12Months() != null) {
	            R61cell4.setCellValue(record.getR61_moreThan6MonthTo12Months().doubleValue());
	            R61cell4.setCellStyle(numberStyle);
	        } else {
	            R61cell4.setCellValue("");
	            R61cell4.setCellStyle(textStyle);
	        }

	        Cell R61cell5 = row.createCell(6);
	        if (record.getR61_moreThan12MonthTo3Years() != null) {
	            R61cell5.setCellValue(record.getR61_moreThan12MonthTo3Years().doubleValue());
	            R61cell5.setCellStyle(numberStyle);
	        } else {
	            R61cell5.setCellValue("");
	            R61cell5.setCellStyle(textStyle);
	        }

	        Cell R61cell6 = row.createCell(7);
	        if (record.getR61_moreThan3YearsTo5Years() != null) {
	            R61cell6.setCellValue(record.getR61_moreThan3YearsTo5Years().doubleValue());
	            R61cell6.setCellStyle(numberStyle);
	        } else {
	            R61cell6.setCellValue("");
	            R61cell6.setCellStyle(textStyle);
	        }

	        Cell R61cell7 = row.createCell(8);
	        if (record.getR61_moreThan5YearsTo10Years() != null) {
	            R61cell7.setCellValue(record.getR61_moreThan5YearsTo10Years().doubleValue());
	            R61cell7.setCellStyle(numberStyle);
	        } else {
	            R61cell7.setCellValue("");
	            R61cell7.setCellStyle(textStyle);
	        }

	        Cell R61cell8 = row.createCell(9);
	        if (record.getR61_moreThan10Years() != null) {
	            R61cell8.setCellValue(record.getR61_moreThan10Years().doubleValue());
	            R61cell8.setCellStyle(numberStyle);
	        } else {
	            R61cell8.setCellValue("");
	            R61cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(61);
	        Cell R62cell1 = row.createCell(2);
	        if (record.getR62_upTo1Month() != null) {
	            R62cell1.setCellValue(record.getR62_upTo1Month().doubleValue());
	            R62cell1.setCellStyle(numberStyle);
	        } else {
	            R62cell1.setCellValue("");
	            R62cell1.setCellStyle(textStyle);
	        }

	        Cell R62cell2 = row.createCell(3);
	        if (record.getR62_moreThan1MonthTo3Months() != null) {
	            R62cell2.setCellValue(record.getR62_moreThan1MonthTo3Months().doubleValue());
	            R62cell2.setCellStyle(numberStyle);
	        } else {
	            R62cell2.setCellValue("");
	            R62cell2.setCellStyle(textStyle);
	        }

	        Cell R62cell3 = row.createCell(4);
	        if (record.getR62_moreThan3MonthTo6Months() != null) {
	            R62cell3.setCellValue(record.getR62_moreThan3MonthTo6Months().doubleValue());
	            R62cell3.setCellStyle(numberStyle);
	        } else {
	            R62cell3.setCellValue("");
	            R62cell3.setCellStyle(textStyle);
	        }

	        Cell R62cell4 = row.createCell(5);
	        if (record.getR62_moreThan6MonthTo12Months() != null) {
	            R62cell4.setCellValue(record.getR62_moreThan6MonthTo12Months().doubleValue());
	            R62cell4.setCellStyle(numberStyle);
	        } else {
	            R62cell4.setCellValue("");
	            R62cell4.setCellStyle(textStyle);
	        }

	        Cell R62cell5 = row.createCell(6);
	        if (record.getR62_moreThan12MonthTo3Years() != null) {
	            R62cell5.setCellValue(record.getR62_moreThan12MonthTo3Years().doubleValue());
	            R62cell5.setCellStyle(numberStyle);
	        } else {
	            R62cell5.setCellValue("");
	            R62cell5.setCellStyle(textStyle);
	        }

	        Cell R62cell6 = row.createCell(7);
	        if (record.getR62_moreThan3YearsTo5Years() != null) {
	            R62cell6.setCellValue(record.getR62_moreThan3YearsTo5Years().doubleValue());
	            R62cell6.setCellStyle(numberStyle);
	        } else {
	            R62cell6.setCellValue("");
	            R62cell6.setCellStyle(textStyle);
	        }

	        Cell R62cell7 = row.createCell(8);
	        if (record.getR62_moreThan5YearsTo10Years() != null) {
	            R62cell7.setCellValue(record.getR62_moreThan5YearsTo10Years().doubleValue());
	            R62cell7.setCellStyle(numberStyle);
	        } else {
	            R62cell7.setCellValue("");
	            R62cell7.setCellStyle(textStyle);
	        }

	        Cell R62cell8 = row.createCell(9);
	        if (record.getR62_moreThan10Years() != null) {
	            R62cell8.setCellValue(record.getR62_moreThan10Years().doubleValue());
	            R62cell8.setCellStyle(numberStyle);
	        } else {
	            R62cell8.setCellValue("");
	            R62cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(62);
	        Cell R63cell1 = row.createCell(2);
	        if (record.getR63_upTo1Month() != null) {
	            R63cell1.setCellValue(record.getR63_upTo1Month().doubleValue());
	            R63cell1.setCellStyle(numberStyle);
	        } else {
	            R63cell1.setCellValue("");
	            R63cell1.setCellStyle(textStyle);
	        }

	        Cell R63cell2 = row.createCell(3);
	        if (record.getR63_moreThan1MonthTo3Months() != null) {
	            R63cell2.setCellValue(record.getR63_moreThan1MonthTo3Months().doubleValue());
	            R63cell2.setCellStyle(numberStyle);
	        } else {
	            R63cell2.setCellValue("");
	            R63cell2.setCellStyle(textStyle);
	        }

	        Cell R63cell3 = row.createCell(4);
	        if (record.getR63_moreThan3MonthTo6Months() != null) {
	            R63cell3.setCellValue(record.getR63_moreThan3MonthTo6Months().doubleValue());
	            R63cell3.setCellStyle(numberStyle);
	        } else {
	            R63cell3.setCellValue("");
	            R63cell3.setCellStyle(textStyle);
	        }

	        Cell R63cell4 = row.createCell(5);
	        if (record.getR63_moreThan6MonthTo12Months() != null) {
	            R63cell4.setCellValue(record.getR63_moreThan6MonthTo12Months().doubleValue());
	            R63cell4.setCellStyle(numberStyle);
	        } else {
	            R63cell4.setCellValue("");
	            R63cell4.setCellStyle(textStyle);
	        }

	        Cell R63cell5 = row.createCell(6);
	        if (record.getR63_moreThan12MonthTo3Years() != null) {
	            R63cell5.setCellValue(record.getR63_moreThan12MonthTo3Years().doubleValue());
	            R63cell5.setCellStyle(numberStyle);
	        } else {
	            R63cell5.setCellValue("");
	            R63cell5.setCellStyle(textStyle);
	        }

	        Cell R63cell6 = row.createCell(7);
	        if (record.getR63_moreThan3YearsTo5Years() != null) {
	            R63cell6.setCellValue(record.getR63_moreThan3YearsTo5Years().doubleValue());
	            R63cell6.setCellStyle(numberStyle);
	        } else {
	            R63cell6.setCellValue("");
	            R63cell6.setCellStyle(textStyle);
	        }

	        Cell R63cell7 = row.createCell(8);
	        if (record.getR63_moreThan5YearsTo10Years() != null) {
	            R63cell7.setCellValue(record.getR63_moreThan5YearsTo10Years().doubleValue());
	            R63cell7.setCellStyle(numberStyle);
	        } else {
	            R63cell7.setCellValue("");
	            R63cell7.setCellStyle(textStyle);
	        }

	        Cell R63cell8 = row.createCell(9);
	        if (record.getR63_moreThan10Years() != null) {
	            R63cell8.setCellValue(record.getR63_moreThan10Years().doubleValue());
	            R63cell8.setCellStyle(numberStyle);
	        } else {
	            R63cell8.setCellValue("");
	            R63cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(63);
	        Cell R64cell1 = row.createCell(2);
	        if (record.getR64_upTo1Month() != null) {
	            R64cell1.setCellValue(record.getR64_upTo1Month().doubleValue());
	            R64cell1.setCellStyle(numberStyle);
	        } else {
	            R64cell1.setCellValue("");
	            R64cell1.setCellStyle(textStyle);
	        }

	        Cell R64cell2 = row.createCell(3);
	        if (record.getR64_moreThan1MonthTo3Months() != null) {
	            R64cell2.setCellValue(record.getR64_moreThan1MonthTo3Months().doubleValue());
	            R64cell2.setCellStyle(numberStyle);
	        } else {
	            R64cell2.setCellValue("");
	            R64cell2.setCellStyle(textStyle);
	        }

	        Cell R64cell3 = row.createCell(4);
	        if (record.getR64_moreThan3MonthTo6Months() != null) {
	            R64cell3.setCellValue(record.getR64_moreThan3MonthTo6Months().doubleValue());
	            R64cell3.setCellStyle(numberStyle);
	        } else {
	            R64cell3.setCellValue("");
	            R64cell3.setCellStyle(textStyle);
	        }

	        Cell R64cell4 = row.createCell(5);
	        if (record.getR64_moreThan6MonthTo12Months() != null) {
	            R64cell4.setCellValue(record.getR64_moreThan6MonthTo12Months().doubleValue());
	            R64cell4.setCellStyle(numberStyle);
	        } else {
	            R64cell4.setCellValue("");
	            R64cell4.setCellStyle(textStyle);
	        }

	        Cell R64cell5 = row.createCell(6);
	        if (record.getR64_moreThan12MonthTo3Years() != null) {
	            R64cell5.setCellValue(record.getR64_moreThan12MonthTo3Years().doubleValue());
	            R64cell5.setCellStyle(numberStyle);
	        } else {
	            R64cell5.setCellValue("");
	            R64cell5.setCellStyle(textStyle);
	        }

	        Cell R64cell6 = row.createCell(7);
	        if (record.getR64_moreThan3YearsTo5Years() != null) {
	            R64cell6.setCellValue(record.getR64_moreThan3YearsTo5Years().doubleValue());
	            R64cell6.setCellStyle(numberStyle);
	        } else {
	            R64cell6.setCellValue("");
	            R64cell6.setCellStyle(textStyle);
	        }

	        Cell R64cell7 = row.createCell(8);
	        if (record.getR64_moreThan5YearsTo10Years() != null) {
	            R64cell7.setCellValue(record.getR64_moreThan5YearsTo10Years().doubleValue());
	            R64cell7.setCellStyle(numberStyle);
	        } else {
	            R64cell7.setCellValue("");
	            R64cell7.setCellStyle(textStyle);
	        }

	        Cell R64cell8 = row.createCell(9);
	        if (record.getR64_moreThan10Years() != null) {
	            R64cell8.setCellValue(record.getR64_moreThan10Years().doubleValue());
	            R64cell8.setCellStyle(numberStyle);
	        } else {
	            R64cell8.setCellValue("");
	            R64cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(64);
	        Cell R65cell1 = row.createCell(2);
	        if (record.getR65_upTo1Month() != null) {
	            R65cell1.setCellValue(record.getR65_upTo1Month().doubleValue());
	            R65cell1.setCellStyle(numberStyle);
	        } else {
	            R65cell1.setCellValue("");
	            R65cell1.setCellStyle(textStyle);
	        }

	        Cell R65cell2 = row.createCell(3);
	        if (record.getR65_moreThan1MonthTo3Months() != null) {
	            R65cell2.setCellValue(record.getR65_moreThan1MonthTo3Months().doubleValue());
	            R65cell2.setCellStyle(numberStyle);
	        } else {
	            R65cell2.setCellValue("");
	            R65cell2.setCellStyle(textStyle);
	        }

	        Cell R65cell3 = row.createCell(4);
	        if (record.getR65_moreThan3MonthTo6Months() != null) {
	            R65cell3.setCellValue(record.getR65_moreThan3MonthTo6Months().doubleValue());
	            R65cell3.setCellStyle(numberStyle);
	        } else {
	            R65cell3.setCellValue("");
	            R65cell3.setCellStyle(textStyle);
	        }

	        Cell R65cell4 = row.createCell(5);
	        if (record.getR65_moreThan6MonthTo12Months() != null) {
	            R65cell4.setCellValue(record.getR65_moreThan6MonthTo12Months().doubleValue());
	            R65cell4.setCellStyle(numberStyle);
	        } else {
	            R65cell4.setCellValue("");
	            R65cell4.setCellStyle(textStyle);
	        }

	        Cell R65cell5 = row.createCell(6);
	        if (record.getR65_moreThan12MonthTo3Years() != null) {
	            R65cell5.setCellValue(record.getR65_moreThan12MonthTo3Years().doubleValue());
	            R65cell5.setCellStyle(numberStyle);
	        } else {
	            R65cell5.setCellValue("");
	            R65cell5.setCellStyle(textStyle);
	        }

	        Cell R65cell6 = row.createCell(7);
	        if (record.getR65_moreThan3YearsTo5Years() != null) {
	            R65cell6.setCellValue(record.getR65_moreThan3YearsTo5Years().doubleValue());
	            R65cell6.setCellStyle(numberStyle);
	        } else {
	            R65cell6.setCellValue("");
	            R65cell6.setCellStyle(textStyle);
	        }

	        Cell R65cell7 = row.createCell(8);
	        if (record.getR65_moreThan5YearsTo10Years() != null) {
	            R65cell7.setCellValue(record.getR65_moreThan5YearsTo10Years().doubleValue());
	            R65cell7.setCellStyle(numberStyle);
	        } else {
	            R65cell7.setCellValue("");
	            R65cell7.setCellStyle(textStyle);
	        }

	        Cell R65cell8 = row.createCell(9);
	        if (record.getR65_moreThan10Years() != null) {
	            R65cell8.setCellValue(record.getR65_moreThan10Years().doubleValue());
	            R65cell8.setCellStyle(numberStyle);
	        } else {
	            R65cell8.setCellValue("");
	            R65cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(65);
	        Cell R66cell1 = row.createCell(2);
	        if (record.getR66_upTo1Month() != null) {
	            R66cell1.setCellValue(record.getR66_upTo1Month().doubleValue());
	            R66cell1.setCellStyle(numberStyle);
	        } else {
	            R66cell1.setCellValue("");
	            R66cell1.setCellStyle(textStyle);
	        }

	        Cell R66cell2 = row.createCell(3);
	        if (record.getR66_moreThan1MonthTo3Months() != null) {
	            R66cell2.setCellValue(record.getR66_moreThan1MonthTo3Months().doubleValue());
	            R66cell2.setCellStyle(numberStyle);
	        } else {
	            R66cell2.setCellValue("");
	            R66cell2.setCellStyle(textStyle);
	        }

	        Cell R66cell3 = row.createCell(4);
	        if (record.getR66_moreThan3MonthTo6Months() != null) {
	            R66cell3.setCellValue(record.getR66_moreThan3MonthTo6Months().doubleValue());
	            R66cell3.setCellStyle(numberStyle);
	        } else {
	            R66cell3.setCellValue("");
	            R66cell3.setCellStyle(textStyle);
	        }

	        Cell R66cell4 = row.createCell(5);
	        if (record.getR66_moreThan6MonthTo12Months() != null) {
	            R66cell4.setCellValue(record.getR66_moreThan6MonthTo12Months().doubleValue());
	            R66cell4.setCellStyle(numberStyle);
	        } else {
	            R66cell4.setCellValue("");
	            R66cell4.setCellStyle(textStyle);
	        }

	        Cell R66cell5 = row.createCell(6);
	        if (record.getR66_moreThan12MonthTo3Years() != null) {
	            R66cell5.setCellValue(record.getR66_moreThan12MonthTo3Years().doubleValue());
	            R66cell5.setCellStyle(numberStyle);
	        } else {
	            R66cell5.setCellValue("");
	            R66cell5.setCellStyle(textStyle);
	        }

	        Cell R66cell6 = row.createCell(7);
	        if (record.getR66_moreThan3YearsTo5Years() != null) {
	            R66cell6.setCellValue(record.getR66_moreThan3YearsTo5Years().doubleValue());
	            R66cell6.setCellStyle(numberStyle);
	        } else {
	            R66cell6.setCellValue("");
	            R66cell6.setCellStyle(textStyle);
	        }

	        Cell R66cell7 = row.createCell(8);
	        if (record.getR66_moreThan5YearsTo10Years() != null) {
	            R66cell7.setCellValue(record.getR66_moreThan5YearsTo10Years().doubleValue());
	            R66cell7.setCellStyle(numberStyle);
	        } else {
	            R66cell7.setCellValue("");
	            R66cell7.setCellStyle(textStyle);
	        }

	        Cell R66cell8 = row.createCell(9);
	        if (record.getR66_moreThan10Years() != null) {
	            R66cell8.setCellValue(record.getR66_moreThan10Years().doubleValue());
	            R66cell8.setCellStyle(numberStyle);
	        } else {
	            R66cell8.setCellValue("");
	            R66cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(66);
	        Cell R67cell1 = row.createCell(2);
	        if (record.getR67_upTo1Month() != null) {
	            R67cell1.setCellValue(record.getR67_upTo1Month().doubleValue());
	            R67cell1.setCellStyle(numberStyle);
	        } else {
	            R67cell1.setCellValue("");
	            R67cell1.setCellStyle(textStyle);
	        }

	        Cell R67cell2 = row.createCell(3);
	        if (record.getR67_moreThan1MonthTo3Months() != null) {
	            R67cell2.setCellValue(record.getR67_moreThan1MonthTo3Months().doubleValue());
	            R67cell2.setCellStyle(numberStyle);
	        } else {
	            R67cell2.setCellValue("");
	            R67cell2.setCellStyle(textStyle);
	        }

	        Cell R67cell3 = row.createCell(4);
	        if (record.getR67_moreThan3MonthTo6Months() != null) {
	            R67cell3.setCellValue(record.getR67_moreThan3MonthTo6Months().doubleValue());
	            R67cell3.setCellStyle(numberStyle);
	        } else {
	            R67cell3.setCellValue("");
	            R67cell3.setCellStyle(textStyle);
	        }

	        Cell R67cell4 = row.createCell(5);
	        if (record.getR67_moreThan6MonthTo12Months() != null) {
	            R67cell4.setCellValue(record.getR67_moreThan6MonthTo12Months().doubleValue());
	            R67cell4.setCellStyle(numberStyle);
	        } else {
	            R67cell4.setCellValue("");
	            R67cell4.setCellStyle(textStyle);
	        }

	        Cell R67cell5 = row.createCell(6);
	        if (record.getR67_moreThan12MonthTo3Years() != null) {
	            R67cell5.setCellValue(record.getR67_moreThan12MonthTo3Years().doubleValue());
	            R67cell5.setCellStyle(numberStyle);
	        } else {
	            R67cell5.setCellValue("");
	            R67cell5.setCellStyle(textStyle);
	        }

	        Cell R67cell6 = row.createCell(7);
	        if (record.getR67_moreThan3YearsTo5Years() != null) {
	            R67cell6.setCellValue(record.getR67_moreThan3YearsTo5Years().doubleValue());
	            R67cell6.setCellStyle(numberStyle);
	        } else {
	            R67cell6.setCellValue("");
	            R67cell6.setCellStyle(textStyle);
	        }

	        Cell R67cell7 = row.createCell(8);
	        if (record.getR67_moreThan5YearsTo10Years() != null) {
	            R67cell7.setCellValue(record.getR67_moreThan5YearsTo10Years().doubleValue());
	            R67cell7.setCellStyle(numberStyle);
	        } else {
	            R67cell7.setCellValue("");
	            R67cell7.setCellStyle(textStyle);
	        }

	        Cell R67cell8 = row.createCell(9);
	        if (record.getR67_moreThan10Years() != null) {
	            R67cell8.setCellValue(record.getR67_moreThan10Years().doubleValue());
	            R67cell8.setCellStyle(numberStyle);
	        } else {
	            R67cell8.setCellValue("");
	            R67cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(68);
	        Cell R69cell1 = row.createCell(2);
	        if (record.getR69_upTo1Month() != null) {
	            R69cell1.setCellValue(record.getR69_upTo1Month().doubleValue());
	            R69cell1.setCellStyle(numberStyle);
	        } else {
	            R69cell1.setCellValue("");
	            R69cell1.setCellStyle(textStyle);
	        }

	        Cell R69cell2 = row.createCell(3);
	        if (record.getR69_moreThan1MonthTo3Months() != null) {
	            R69cell2.setCellValue(record.getR69_moreThan1MonthTo3Months().doubleValue());
	            R69cell2.setCellStyle(numberStyle);
	        } else {
	            R69cell2.setCellValue("");
	            R69cell2.setCellStyle(textStyle);
	        }

	        Cell R69cell3 = row.createCell(4);
	        if (record.getR69_moreThan3MonthTo6Months() != null) {
	            R69cell3.setCellValue(record.getR69_moreThan3MonthTo6Months().doubleValue());
	            R69cell3.setCellStyle(numberStyle);
	        } else {
	            R69cell3.setCellValue("");
	            R69cell3.setCellStyle(textStyle);
	        }

	        Cell R69cell4 = row.createCell(5);
	        if (record.getR69_moreThan6MonthTo12Months() != null) {
	            R69cell4.setCellValue(record.getR69_moreThan6MonthTo12Months().doubleValue());
	            R69cell4.setCellStyle(numberStyle);
	        } else {
	            R69cell4.setCellValue("");
	            R69cell4.setCellStyle(textStyle);
	        }

	        Cell R69cell5 = row.createCell(6);
	        if (record.getR69_moreThan12MonthTo3Years() != null) {
	            R69cell5.setCellValue(record.getR69_moreThan12MonthTo3Years().doubleValue());
	            R69cell5.setCellStyle(numberStyle);
	        } else {
	            R69cell5.setCellValue("");
	            R69cell5.setCellStyle(textStyle);
	        }

	        Cell R69cell6 = row.createCell(7);
	        if (record.getR69_moreThan3YearsTo5Years() != null) {
	            R69cell6.setCellValue(record.getR69_moreThan3YearsTo5Years().doubleValue());
	            R69cell6.setCellStyle(numberStyle);
	        } else {
	            R69cell6.setCellValue("");
	            R69cell6.setCellStyle(textStyle);
	        }

	        Cell R69cell7 = row.createCell(8);
	        if (record.getR69_moreThan5YearsTo10Years() != null) {
	            R69cell7.setCellValue(record.getR69_moreThan5YearsTo10Years().doubleValue());
	            R69cell7.setCellStyle(numberStyle);
	        } else {
	            R69cell7.setCellValue("");
	            R69cell7.setCellStyle(textStyle);
	        }

	        Cell R69cell8 = row.createCell(9);
	        if (record.getR69_moreThan10Years() != null) {
	            R69cell8.setCellValue(record.getR69_moreThan10Years().doubleValue());
	            R69cell8.setCellStyle(numberStyle);
	        } else {
	            R69cell8.setCellValue("");
	            R69cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(69);
	        Cell R70cell1 = row.createCell(2);
	        if (record.getR70_upTo1Month() != null) {
	            R70cell1.setCellValue(record.getR70_upTo1Month().doubleValue());
	            R70cell1.setCellStyle(numberStyle);
	        } else {
	            R70cell1.setCellValue("");
	            R70cell1.setCellStyle(textStyle);
	        }

	        Cell R70cell2 = row.createCell(3);
	        if (record.getR70_moreThan1MonthTo3Months() != null) {
	            R70cell2.setCellValue(record.getR70_moreThan1MonthTo3Months().doubleValue());
	            R70cell2.setCellStyle(numberStyle);
	        } else {
	            R70cell2.setCellValue("");
	            R70cell2.setCellStyle(textStyle);
	        }

	        Cell R70cell3 = row.createCell(4);
	        if (record.getR70_moreThan3MonthTo6Months() != null) {
	            R70cell3.setCellValue(record.getR70_moreThan3MonthTo6Months().doubleValue());
	            R70cell3.setCellStyle(numberStyle);
	        } else {
	            R70cell3.setCellValue("");
	            R70cell3.setCellStyle(textStyle);
	        }

	        Cell R70cell4 = row.createCell(5);
	        if (record.getR70_moreThan6MonthTo12Months() != null) {
	            R70cell4.setCellValue(record.getR70_moreThan6MonthTo12Months().doubleValue());
	            R70cell4.setCellStyle(numberStyle);
	        } else {
	            R70cell4.setCellValue("");
	            R70cell4.setCellStyle(textStyle);
	        }

	        Cell R70cell5 = row.createCell(6);
	        if (record.getR70_moreThan12MonthTo3Years() != null) {
	            R70cell5.setCellValue(record.getR70_moreThan12MonthTo3Years().doubleValue());
	            R70cell5.setCellStyle(numberStyle);
	        } else {
	            R70cell5.setCellValue("");
	            R70cell5.setCellStyle(textStyle);
	        }

	        Cell R70cell6 = row.createCell(7);
	        if (record.getR70_moreThan3YearsTo5Years() != null) {
	            R70cell6.setCellValue(record.getR70_moreThan3YearsTo5Years().doubleValue());
	            R70cell6.setCellStyle(numberStyle);
	        } else {
	            R70cell6.setCellValue("");
	            R70cell6.setCellStyle(textStyle);
	        }

	        Cell R70cell7 = row.createCell(8);
	        if (record.getR70_moreThan5YearsTo10Years() != null) {
	            R70cell7.setCellValue(record.getR70_moreThan5YearsTo10Years().doubleValue());
	            R70cell7.setCellStyle(numberStyle);
	        } else {
	            R70cell7.setCellValue("");
	            R70cell7.setCellStyle(textStyle);
	        }

	        Cell R70cell8 = row.createCell(9);
	        if (record.getR70_moreThan10Years() != null) {
	            R70cell8.setCellValue(record.getR70_moreThan10Years().doubleValue());
	            R70cell8.setCellStyle(numberStyle);
	        } else {
	            R70cell8.setCellValue("");
	            R70cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(70);
	        Cell R71cell1 = row.createCell(2);
	        if (record.getR71_upTo1Month() != null) {
	            R71cell1.setCellValue(record.getR71_upTo1Month().doubleValue());
	            R71cell1.setCellStyle(numberStyle);
	        } else {
	            R71cell1.setCellValue("");
	            R71cell1.setCellStyle(textStyle);
	        }

	        Cell R71cell2 = row.createCell(3);
	        if (record.getR71_moreThan1MonthTo3Months() != null) {
	            R71cell2.setCellValue(record.getR71_moreThan1MonthTo3Months().doubleValue());
	            R71cell2.setCellStyle(numberStyle);
	        } else {
	            R71cell2.setCellValue("");
	            R71cell2.setCellStyle(textStyle);
	        }

	        Cell R71cell3 = row.createCell(4);
	        if (record.getR71_moreThan3MonthTo6Months() != null) {
	            R71cell3.setCellValue(record.getR71_moreThan3MonthTo6Months().doubleValue());
	            R71cell3.setCellStyle(numberStyle);
	        } else {
	            R71cell3.setCellValue("");
	            R71cell3.setCellStyle(textStyle);
	        }

	        Cell R71cell4 = row.createCell(5);
	        if (record.getR71_moreThan6MonthTo12Months() != null) {
	            R71cell4.setCellValue(record.getR71_moreThan6MonthTo12Months().doubleValue());
	            R71cell4.setCellStyle(numberStyle);
	        } else {
	            R71cell4.setCellValue("");
	            R71cell4.setCellStyle(textStyle);
	        }

	        Cell R71cell5 = row.createCell(6);
	        if (record.getR71_moreThan12MonthTo3Years() != null) {
	            R71cell5.setCellValue(record.getR71_moreThan12MonthTo3Years().doubleValue());
	            R71cell5.setCellStyle(numberStyle);
	        } else {
	            R71cell5.setCellValue("");
	            R71cell5.setCellStyle(textStyle);
	        }

	        Cell R71cell6 = row.createCell(7);
	        if (record.getR71_moreThan3YearsTo5Years() != null) {
	            R71cell6.setCellValue(record.getR71_moreThan3YearsTo5Years().doubleValue());
	            R71cell6.setCellStyle(numberStyle);
	        } else {
	            R71cell6.setCellValue("");
	            R71cell6.setCellStyle(textStyle);
	        }

	        Cell R71cell7 = row.createCell(8);
	        if (record.getR71_moreThan5YearsTo10Years() != null) {
	            R71cell7.setCellValue(record.getR71_moreThan5YearsTo10Years().doubleValue());
	            R71cell7.setCellStyle(numberStyle);
	        } else {
	            R71cell7.setCellValue("");
	            R71cell7.setCellStyle(textStyle);
	        }

	        Cell R71cell8 = row.createCell(9);
	        if (record.getR71_moreThan10Years() != null) {
	            R71cell8.setCellValue(record.getR71_moreThan10Years().doubleValue());
	            R71cell8.setCellStyle(numberStyle);
	        } else {
	            R71cell8.setCellValue("");
	            R71cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(71);
	        Cell R72cell1 = row.createCell(2);
	        if (record.getR72_upTo1Month() != null) {
	            R72cell1.setCellValue(record.getR72_upTo1Month().doubleValue());
	            R72cell1.setCellStyle(numberStyle);
	        } else {
	            R72cell1.setCellValue("");
	            R72cell1.setCellStyle(textStyle);
	        }

	        Cell R72cell2 = row.createCell(3);
	        if (record.getR72_moreThan1MonthTo3Months() != null) {
	            R72cell2.setCellValue(record.getR72_moreThan1MonthTo3Months().doubleValue());
	            R72cell2.setCellStyle(numberStyle);
	        } else {
	            R72cell2.setCellValue("");
	            R72cell2.setCellStyle(textStyle);
	        }

	        Cell R72cell3 = row.createCell(4);
	        if (record.getR72_moreThan3MonthTo6Months() != null) {
	            R72cell3.setCellValue(record.getR72_moreThan3MonthTo6Months().doubleValue());
	            R72cell3.setCellStyle(numberStyle);
	        } else {
	            R72cell3.setCellValue("");
	            R72cell3.setCellStyle(textStyle);
	        }

	        Cell R72cell4 = row.createCell(5);
	        if (record.getR72_moreThan6MonthTo12Months() != null) {
	            R72cell4.setCellValue(record.getR72_moreThan6MonthTo12Months().doubleValue());
	            R72cell4.setCellStyle(numberStyle);
	        } else {
	            R72cell4.setCellValue("");
	            R72cell4.setCellStyle(textStyle);
	        }

	        Cell R72cell5 = row.createCell(6);
	        if (record.getR72_moreThan12MonthTo3Years() != null) {
	            R72cell5.setCellValue(record.getR72_moreThan12MonthTo3Years().doubleValue());
	            R72cell5.setCellStyle(numberStyle);
	        } else {
	            R72cell5.setCellValue("");
	            R72cell5.setCellStyle(textStyle);
	        }

	        Cell R72cell6 = row.createCell(7);
	        if (record.getR72_moreThan3YearsTo5Years() != null) {
	            R72cell6.setCellValue(record.getR72_moreThan3YearsTo5Years().doubleValue());
	            R72cell6.setCellStyle(numberStyle);
	        } else {
	            R72cell6.setCellValue("");
	            R72cell6.setCellStyle(textStyle);
	        }

	        Cell R72cell7 = row.createCell(8);
	        if (record.getR72_moreThan5YearsTo10Years() != null) {
	            R72cell7.setCellValue(record.getR72_moreThan5YearsTo10Years().doubleValue());
	            R72cell7.setCellStyle(numberStyle);
	        } else {
	            R72cell7.setCellValue("");
	            R72cell7.setCellStyle(textStyle);
	        }

	        Cell R72cell8 = row.createCell(9);
	        if (record.getR72_moreThan10Years() != null) {
	            R72cell8.setCellValue(record.getR72_moreThan10Years().doubleValue());
	            R72cell8.setCellStyle(numberStyle);
	        } else {
	            R72cell8.setCellValue("");
	            R72cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(72);
	        Cell R73cell1 = row.createCell(2);
	        if (record.getR73_upTo1Month() != null) {
	            R73cell1.setCellValue(record.getR73_upTo1Month().doubleValue());
	            R73cell1.setCellStyle(numberStyle);
	        } else {
	            R73cell1.setCellValue("");
	            R73cell1.setCellStyle(textStyle);
	        }

	        Cell R73cell2 = row.createCell(3);
	        if (record.getR73_moreThan1MonthTo3Months() != null) {
	            R73cell2.setCellValue(record.getR73_moreThan1MonthTo3Months().doubleValue());
	            R73cell2.setCellStyle(numberStyle);
	        } else {
	            R73cell2.setCellValue("");
	            R73cell2.setCellStyle(textStyle);
	        }

	        Cell R73cell3 = row.createCell(4);
	        if (record.getR73_moreThan3MonthTo6Months() != null) {
	            R73cell3.setCellValue(record.getR73_moreThan3MonthTo6Months().doubleValue());
	            R73cell3.setCellStyle(numberStyle);
	        } else {
	            R73cell3.setCellValue("");
	            R73cell3.setCellStyle(textStyle);
	        }

	        Cell R73cell4 = row.createCell(5);
	        if (record.getR73_moreThan6MonthTo12Months() != null) {
	            R73cell4.setCellValue(record.getR73_moreThan6MonthTo12Months().doubleValue());
	            R73cell4.setCellStyle(numberStyle);
	        } else {
	            R73cell4.setCellValue("");
	            R73cell4.setCellStyle(textStyle);
	        }

	        Cell R73cell5 = row.createCell(6);
	        if (record.getR73_moreThan12MonthTo3Years() != null) {
	            R73cell5.setCellValue(record.getR73_moreThan12MonthTo3Years().doubleValue());
	            R73cell5.setCellStyle(numberStyle);
	        } else {
	            R73cell5.setCellValue("");
	            R73cell5.setCellStyle(textStyle);
	        }

	        Cell R73cell6 = row.createCell(7);
	        if (record.getR73_moreThan3YearsTo5Years() != null) {
	            R73cell6.setCellValue(record.getR73_moreThan3YearsTo5Years().doubleValue());
	            R73cell6.setCellStyle(numberStyle);
	        } else {
	            R73cell6.setCellValue("");
	            R73cell6.setCellStyle(textStyle);
	        }

	        Cell R73cell7 = row.createCell(8);
	        if (record.getR73_moreThan5YearsTo10Years() != null) {
	            R73cell7.setCellValue(record.getR73_moreThan5YearsTo10Years().doubleValue());
	            R73cell7.setCellStyle(numberStyle);
	        } else {
	            R73cell7.setCellValue("");
	            R73cell7.setCellStyle(textStyle);
	        }

	        Cell R73cell8 = row.createCell(9);
	        if (record.getR73_moreThan10Years() != null) {
	            R73cell8.setCellValue(record.getR73_moreThan10Years().doubleValue());
	            R73cell8.setCellStyle(numberStyle);
	        } else {
	            R73cell8.setCellValue("");
	            R73cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(73);
	        Cell R74cell1 = row.createCell(2);
	        if (record.getR74_upTo1Month() != null) {
	            R74cell1.setCellValue(record.getR74_upTo1Month().doubleValue());
	            R74cell1.setCellStyle(numberStyle);
	        } else {
	            R74cell1.setCellValue("");
	            R74cell1.setCellStyle(textStyle);
	        }

	        Cell R74cell2 = row.createCell(3);
	        if (record.getR74_moreThan1MonthTo3Months() != null) {
	            R74cell2.setCellValue(record.getR74_moreThan1MonthTo3Months().doubleValue());
	            R74cell2.setCellStyle(numberStyle);
	        } else {
	            R74cell2.setCellValue("");
	            R74cell2.setCellStyle(textStyle);
	        }

	        Cell R74cell3 = row.createCell(4);
	        if (record.getR74_moreThan3MonthTo6Months() != null) {
	            R74cell3.setCellValue(record.getR74_moreThan3MonthTo6Months().doubleValue());
	            R74cell3.setCellStyle(numberStyle);
	        } else {
	            R74cell3.setCellValue("");
	            R74cell3.setCellStyle(textStyle);
	        }

	        Cell R74cell4 = row.createCell(5);
	        if (record.getR74_moreThan6MonthTo12Months() != null) {
	            R74cell4.setCellValue(record.getR74_moreThan6MonthTo12Months().doubleValue());
	            R74cell4.setCellStyle(numberStyle);
	        } else {
	            R74cell4.setCellValue("");
	            R74cell4.setCellStyle(textStyle);
	        }

	        Cell R74cell5 = row.createCell(6);
	        if (record.getR74_moreThan12MonthTo3Years() != null) {
	            R74cell5.setCellValue(record.getR74_moreThan12MonthTo3Years().doubleValue());
	            R74cell5.setCellStyle(numberStyle);
	        } else {
	            R74cell5.setCellValue("");
	            R74cell5.setCellStyle(textStyle);
	        }

	        Cell R74cell6 = row.createCell(7);
	        if (record.getR74_moreThan3YearsTo5Years() != null) {
	            R74cell6.setCellValue(record.getR74_moreThan3YearsTo5Years().doubleValue());
	            R74cell6.setCellStyle(numberStyle);
	        } else {
	            R74cell6.setCellValue("");
	            R74cell6.setCellStyle(textStyle);
	        }

	        Cell R74cell7 = row.createCell(8);
	        if (record.getR74_moreThan5YearsTo10Years() != null) {
	            R74cell7.setCellValue(record.getR74_moreThan5YearsTo10Years().doubleValue());
	            R74cell7.setCellStyle(numberStyle);
	        } else {
	            R74cell7.setCellValue("");
	            R74cell7.setCellStyle(textStyle);
	        }

	        Cell R74cell8 = row.createCell(9);
	        if (record.getR74_moreThan10Years() != null) {
	            R74cell8.setCellValue(record.getR74_moreThan10Years().doubleValue());
	            R74cell8.setCellStyle(numberStyle);
	        } else {
	            R74cell8.setCellValue("");
	            R74cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(74);
	        Cell R75cell1 = row.createCell(2);
	        if (record.getR75_upTo1Month() != null) {
	            R75cell1.setCellValue(record.getR75_upTo1Month().doubleValue());
	            R75cell1.setCellStyle(numberStyle);
	        } else {
	            R75cell1.setCellValue("");
	            R75cell1.setCellStyle(textStyle);
	        }

	        Cell R75cell2 = row.createCell(3);
	        if (record.getR75_moreThan1MonthTo3Months() != null) {
	            R75cell2.setCellValue(record.getR75_moreThan1MonthTo3Months().doubleValue());
	            R75cell2.setCellStyle(numberStyle);
	        } else {
	            R75cell2.setCellValue("");
	            R75cell2.setCellStyle(textStyle);
	        }

	        Cell R75cell3 = row.createCell(4);
	        if (record.getR75_moreThan3MonthTo6Months() != null) {
	            R75cell3.setCellValue(record.getR75_moreThan3MonthTo6Months().doubleValue());
	            R75cell3.setCellStyle(numberStyle);
	        } else {
	            R75cell3.setCellValue("");
	            R75cell3.setCellStyle(textStyle);
	        }

	        Cell R75cell4 = row.createCell(5);
	        if (record.getR75_moreThan6MonthTo12Months() != null) {
	            R75cell4.setCellValue(record.getR75_moreThan6MonthTo12Months().doubleValue());
	            R75cell4.setCellStyle(numberStyle);
	        } else {
	            R75cell4.setCellValue("");
	            R75cell4.setCellStyle(textStyle);
	        }

	        Cell R75cell5 = row.createCell(6);
	        if (record.getR75_moreThan12MonthTo3Years() != null) {
	            R75cell5.setCellValue(record.getR75_moreThan12MonthTo3Years().doubleValue());
	            R75cell5.setCellStyle(numberStyle);
	        } else {
	            R75cell5.setCellValue("");
	            R75cell5.setCellStyle(textStyle);
	        }

	        Cell R75cell6 = row.createCell(7);
	        if (record.getR75_moreThan3YearsTo5Years() != null) {
	            R75cell6.setCellValue(record.getR75_moreThan3YearsTo5Years().doubleValue());
	            R75cell6.setCellStyle(numberStyle);
	        } else {
	            R75cell6.setCellValue("");
	            R75cell6.setCellStyle(textStyle);
	        }

	        Cell R75cell7 = row.createCell(8);
	        if (record.getR75_moreThan5YearsTo10Years() != null) {
	            R75cell7.setCellValue(record.getR75_moreThan5YearsTo10Years().doubleValue());
	            R75cell7.setCellStyle(numberStyle);
	        } else {
	            R75cell7.setCellValue("");
	            R75cell7.setCellStyle(textStyle);
	        }

	        Cell R75cell8 = row.createCell(9);
	        if (record.getR75_moreThan10Years() != null) {
	            R75cell8.setCellValue(record.getR75_moreThan10Years().doubleValue());
	            R75cell8.setCellStyle(numberStyle);
	        } else {
	            R75cell8.setCellValue("");
	            R75cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(76);
	        Cell R77cell1 = row.createCell(2);
	        if (record.getR77_upTo1Month() != null) {
	            R77cell1.setCellValue(record.getR77_upTo1Month().doubleValue());
	            R77cell1.setCellStyle(numberStyle);
	        } else {
	            R77cell1.setCellValue("");
	            R77cell1.setCellStyle(textStyle);
	        }

	        Cell R77cell2 = row.createCell(3);
	        if (record.getR77_moreThan1MonthTo3Months() != null) {
	            R77cell2.setCellValue(record.getR77_moreThan1MonthTo3Months().doubleValue());
	            R77cell2.setCellStyle(numberStyle);
	        } else {
	            R77cell2.setCellValue("");
	            R77cell2.setCellStyle(textStyle);
	        }

	        Cell R77cell3 = row.createCell(4);
	        if (record.getR77_moreThan3MonthTo6Months() != null) {
	            R77cell3.setCellValue(record.getR77_moreThan3MonthTo6Months().doubleValue());
	            R77cell3.setCellStyle(numberStyle);
	        } else {
	            R77cell3.setCellValue("");
	            R77cell3.setCellStyle(textStyle);
	        }

	        Cell R77cell4 = row.createCell(5);
	        if (record.getR77_moreThan6MonthTo12Months() != null) {
	            R77cell4.setCellValue(record.getR77_moreThan6MonthTo12Months().doubleValue());
	            R77cell4.setCellStyle(numberStyle);
	        } else {
	            R77cell4.setCellValue("");
	            R77cell4.setCellStyle(textStyle);
	        }

	        Cell R77cell5 = row.createCell(6);
	        if (record.getR77_moreThan12MonthTo3Years() != null) {
	            R77cell5.setCellValue(record.getR77_moreThan12MonthTo3Years().doubleValue());
	            R77cell5.setCellStyle(numberStyle);
	        } else {
	            R77cell5.setCellValue("");
	            R77cell5.setCellStyle(textStyle);
	        }

	        Cell R77cell6 = row.createCell(7);
	        if (record.getR77_moreThan3YearsTo5Years() != null) {
	            R77cell6.setCellValue(record.getR77_moreThan3YearsTo5Years().doubleValue());
	            R77cell6.setCellStyle(numberStyle);
	        } else {
	            R77cell6.setCellValue("");
	            R77cell6.setCellStyle(textStyle);
	        }

	        Cell R77cell7 = row.createCell(8);
	        if (record.getR77_moreThan5YearsTo10Years() != null) {
	            R77cell7.setCellValue(record.getR77_moreThan5YearsTo10Years().doubleValue());
	            R77cell7.setCellStyle(numberStyle);
	        } else {
	            R77cell7.setCellValue("");
	            R77cell7.setCellStyle(textStyle);
	        }

	        Cell R77cell8 = row.createCell(9);
	        if (record.getR77_moreThan10Years() != null) {
	            R77cell8.setCellValue(record.getR77_moreThan10Years().doubleValue());
	            R77cell8.setCellStyle(numberStyle);
	        } else {
	            R77cell8.setCellValue("");
	            R77cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(77);
	        Cell R78cell1 = row.createCell(2);
	        if (record.getR78_upTo1Month() != null) {
	            R78cell1.setCellValue(record.getR78_upTo1Month().doubleValue());
	            R78cell1.setCellStyle(numberStyle);
	        } else {
	            R78cell1.setCellValue("");
	            R78cell1.setCellStyle(textStyle);
	        }

	        Cell R78cell2 = row.createCell(3);
	        if (record.getR78_moreThan1MonthTo3Months() != null) {
	            R78cell2.setCellValue(record.getR78_moreThan1MonthTo3Months().doubleValue());
	            R78cell2.setCellStyle(numberStyle);
	        } else {
	            R78cell2.setCellValue("");
	            R78cell2.setCellStyle(textStyle);
	        }

	        Cell R78cell3 = row.createCell(4);
	        if (record.getR78_moreThan3MonthTo6Months() != null) {
	            R78cell3.setCellValue(record.getR78_moreThan3MonthTo6Months().doubleValue());
	            R78cell3.setCellStyle(numberStyle);
	        } else {
	            R78cell3.setCellValue("");
	            R78cell3.setCellStyle(textStyle);
	        }

	        Cell R78cell4 = row.createCell(5);
	        if (record.getR78_moreThan6MonthTo12Months() != null) {
	            R78cell4.setCellValue(record.getR78_moreThan6MonthTo12Months().doubleValue());
	            R78cell4.setCellStyle(numberStyle);
	        } else {
	            R78cell4.setCellValue("");
	            R78cell4.setCellStyle(textStyle);
	        }

	        Cell R78cell5 = row.createCell(6);
	        if (record.getR78_moreThan12MonthTo3Years() != null) {
	            R78cell5.setCellValue(record.getR78_moreThan12MonthTo3Years().doubleValue());
	            R78cell5.setCellStyle(numberStyle);
	        } else {
	            R78cell5.setCellValue("");
	            R78cell5.setCellStyle(textStyle);
	        }

	        Cell R78cell6 = row.createCell(7);
	        if (record.getR78_moreThan3YearsTo5Years() != null) {
	            R78cell6.setCellValue(record.getR78_moreThan3YearsTo5Years().doubleValue());
	            R78cell6.setCellStyle(numberStyle);
	        } else {
	            R78cell6.setCellValue("");
	            R78cell6.setCellStyle(textStyle);
	        }

	        Cell R78cell7 = row.createCell(8);
	        if (record.getR78_moreThan5YearsTo10Years() != null) {
	            R78cell7.setCellValue(record.getR78_moreThan5YearsTo10Years().doubleValue());
	            R78cell7.setCellStyle(numberStyle);
	        } else {
	            R78cell7.setCellValue("");
	            R78cell7.setCellStyle(textStyle);
	        }

	        Cell R78cell8 = row.createCell(9);
	        if (record.getR78_moreThan10Years() != null) {
	            R78cell8.setCellValue(record.getR78_moreThan10Years().doubleValue());
	            R78cell8.setCellStyle(numberStyle);
	        } else {
	            R78cell8.setCellValue("");
	            R78cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(78);
	        Cell R79cell1 = row.createCell(2);
	        if (record.getR79_upTo1Month() != null) {
	            R79cell1.setCellValue(record.getR79_upTo1Month().doubleValue());
	            R79cell1.setCellStyle(numberStyle);
	        } else {
	            R79cell1.setCellValue("");
	            R79cell1.setCellStyle(textStyle);
	        }

	        Cell R79cell2 = row.createCell(3);
	        if (record.getR79_moreThan1MonthTo3Months() != null) {
	            R79cell2.setCellValue(record.getR79_moreThan1MonthTo3Months().doubleValue());
	            R79cell2.setCellStyle(numberStyle);
	        } else {
	            R79cell2.setCellValue("");
	            R79cell2.setCellStyle(textStyle);
	        }

	        Cell R79cell3 = row.createCell(4);
	        if (record.getR79_moreThan3MonthTo6Months() != null) {
	            R79cell3.setCellValue(record.getR79_moreThan3MonthTo6Months().doubleValue());
	            R79cell3.setCellStyle(numberStyle);
	        } else {
	            R79cell3.setCellValue("");
	            R79cell3.setCellStyle(textStyle);
	        }

	        Cell R79cell4 = row.createCell(5);
	        if (record.getR79_moreThan6MonthTo12Months() != null) {
	            R79cell4.setCellValue(record.getR79_moreThan6MonthTo12Months().doubleValue());
	            R79cell4.setCellStyle(numberStyle);
	        } else {
	            R79cell4.setCellValue("");
	            R79cell4.setCellStyle(textStyle);
	        }

	        Cell R79cell5 = row.createCell(6);
	        if (record.getR79_moreThan12MonthTo3Years() != null) {
	            R79cell5.setCellValue(record.getR79_moreThan12MonthTo3Years().doubleValue());
	            R79cell5.setCellStyle(numberStyle);
	        } else {
	            R79cell5.setCellValue("");
	            R79cell5.setCellStyle(textStyle);
	        }

	        Cell R79cell6 = row.createCell(7);
	        if (record.getR79_moreThan3YearsTo5Years() != null) {
	            R79cell6.setCellValue(record.getR79_moreThan3YearsTo5Years().doubleValue());
	            R79cell6.setCellStyle(numberStyle);
	        } else {
	            R79cell6.setCellValue("");
	            R79cell6.setCellStyle(textStyle);
	        }

	        Cell R79cell7 = row.createCell(8);
	        if (record.getR79_moreThan5YearsTo10Years() != null) {
	            R79cell7.setCellValue(record.getR79_moreThan5YearsTo10Years().doubleValue());
	            R79cell7.setCellStyle(numberStyle);
	        } else {
	            R79cell7.setCellValue("");
	            R79cell7.setCellStyle(textStyle);
	        }

	        Cell R79cell8 = row.createCell(9);
	        if (record.getR79_moreThan10Years() != null) {
	            R79cell8.setCellValue(record.getR79_moreThan10Years().doubleValue());
	            R79cell8.setCellStyle(numberStyle);
	        } else {
	            R79cell8.setCellValue("");
	            R79cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(79);
	        Cell R80cell1 = row.createCell(2);
	        if (record.getR80_upTo1Month() != null) {
	            R80cell1.setCellValue(record.getR80_upTo1Month().doubleValue());
	            R80cell1.setCellStyle(numberStyle);
	        } else {
	            R80cell1.setCellValue("");
	            R80cell1.setCellStyle(textStyle);
	        }

	        Cell R80cell2 = row.createCell(3);
	        if (record.getR80_moreThan1MonthTo3Months() != null) {
	            R80cell2.setCellValue(record.getR80_moreThan1MonthTo3Months().doubleValue());
	            R80cell2.setCellStyle(numberStyle);
	        } else {
	            R80cell2.setCellValue("");
	            R80cell2.setCellStyle(textStyle);
	        }

	        Cell R80cell3 = row.createCell(4);
	        if (record.getR80_moreThan3MonthTo6Months() != null) {
	            R80cell3.setCellValue(record.getR80_moreThan3MonthTo6Months().doubleValue());
	            R80cell3.setCellStyle(numberStyle);
	        } else {
	            R80cell3.setCellValue("");
	            R80cell3.setCellStyle(textStyle);
	        }

	        Cell R80cell4 = row.createCell(5);
	        if (record.getR80_moreThan6MonthTo12Months() != null) {
	            R80cell4.setCellValue(record.getR80_moreThan6MonthTo12Months().doubleValue());
	            R80cell4.setCellStyle(numberStyle);
	        } else {
	            R80cell4.setCellValue("");
	            R80cell4.setCellStyle(textStyle);
	        }

	        Cell R80cell5 = row.createCell(6);
	        if (record.getR80_moreThan12MonthTo3Years() != null) {
	            R80cell5.setCellValue(record.getR80_moreThan12MonthTo3Years().doubleValue());
	            R80cell5.setCellStyle(numberStyle);
	        } else {
	            R80cell5.setCellValue("");
	            R80cell5.setCellStyle(textStyle);
	        }

	        Cell R80cell6 = row.createCell(7);
	        if (record.getR80_moreThan3YearsTo5Years() != null) {
	            R80cell6.setCellValue(record.getR80_moreThan3YearsTo5Years().doubleValue());
	            R80cell6.setCellStyle(numberStyle);
	        } else {
	            R80cell6.setCellValue("");
	            R80cell6.setCellStyle(textStyle);
	        }

	        Cell R80cell7 = row.createCell(8);
	        if (record.getR80_moreThan5YearsTo10Years() != null) {
	            R80cell7.setCellValue(record.getR80_moreThan5YearsTo10Years().doubleValue());
	            R80cell7.setCellStyle(numberStyle);
	        } else {
	            R80cell7.setCellValue("");
	            R80cell7.setCellStyle(textStyle);
	        }

	        Cell R80cell8 = row.createCell(9);
	        if (record.getR80_moreThan10Years() != null) {
	            R80cell8.setCellValue(record.getR80_moreThan10Years().doubleValue());
	            R80cell8.setCellStyle(numberStyle);
	        } else {
	            R80cell8.setCellValue("");
	            R80cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(80);
	        Cell R81cell1 = row.createCell(2);
	        if (record.getR81_upTo1Month() != null) {
	            R81cell1.setCellValue(record.getR81_upTo1Month().doubleValue());
	            R81cell1.setCellStyle(numberStyle);
	        } else {
	            R81cell1.setCellValue("");
	            R81cell1.setCellStyle(textStyle);
	        }

	        Cell R81cell2 = row.createCell(3);
	        if (record.getR81_moreThan1MonthTo3Months() != null) {
	            R81cell2.setCellValue(record.getR81_moreThan1MonthTo3Months().doubleValue());
	            R81cell2.setCellStyle(numberStyle);
	        } else {
	            R81cell2.setCellValue("");
	            R81cell2.setCellStyle(textStyle);
	        }

	        Cell R81cell3 = row.createCell(4);
	        if (record.getR81_moreThan3MonthTo6Months() != null) {
	            R81cell3.setCellValue(record.getR81_moreThan3MonthTo6Months().doubleValue());
	            R81cell3.setCellStyle(numberStyle);
	        } else {
	            R81cell3.setCellValue("");
	            R81cell3.setCellStyle(textStyle);
	        }

	        Cell R81cell4 = row.createCell(5);
	        if (record.getR81_moreThan6MonthTo12Months() != null) {
	            R81cell4.setCellValue(record.getR81_moreThan6MonthTo12Months().doubleValue());
	            R81cell4.setCellStyle(numberStyle);
	        } else {
	            R81cell4.setCellValue("");
	            R81cell4.setCellStyle(textStyle);
	        }

	        Cell R81cell5 = row.createCell(6);
	        if (record.getR81_moreThan12MonthTo3Years() != null) {
	            R81cell5.setCellValue(record.getR81_moreThan12MonthTo3Years().doubleValue());
	            R81cell5.setCellStyle(numberStyle);
	        } else {
	            R81cell5.setCellValue("");
	            R81cell5.setCellStyle(textStyle);
	        }

	        Cell R81cell6 = row.createCell(7);
	        if (record.getR81_moreThan3YearsTo5Years() != null) {
	            R81cell6.setCellValue(record.getR81_moreThan3YearsTo5Years().doubleValue());
	            R81cell6.setCellStyle(numberStyle);
	        } else {
	            R81cell6.setCellValue("");
	            R81cell6.setCellStyle(textStyle);
	        }

	        Cell R81cell7 = row.createCell(8);
	        if (record.getR81_moreThan5YearsTo10Years() != null) {
	            R81cell7.setCellValue(record.getR81_moreThan5YearsTo10Years().doubleValue());
	            R81cell7.setCellStyle(numberStyle);
	        } else {
	            R81cell7.setCellValue("");
	            R81cell7.setCellStyle(textStyle);
	        }

	        Cell R81cell8 = row.createCell(9);
	        if (record.getR81_moreThan10Years() != null) {
	            R81cell8.setCellValue(record.getR81_moreThan10Years().doubleValue());
	            R81cell8.setCellStyle(numberStyle);
	        } else {
	            R81cell8.setCellValue("");
	            R81cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(81);
	        Cell R82cell1 = row.createCell(2);
	        if (record.getR82_upTo1Month() != null) {
	            R82cell1.setCellValue(record.getR82_upTo1Month().doubleValue());
	            R82cell1.setCellStyle(numberStyle);
	        } else {
	            R82cell1.setCellValue("");
	            R82cell1.setCellStyle(textStyle);
	        }

	        Cell R82cell2 = row.createCell(3);
	        if (record.getR82_moreThan1MonthTo3Months() != null) {
	            R82cell2.setCellValue(record.getR82_moreThan1MonthTo3Months().doubleValue());
	            R82cell2.setCellStyle(numberStyle);
	        } else {
	            R82cell2.setCellValue("");
	            R82cell2.setCellStyle(textStyle);
	        }

	        Cell R82cell3 = row.createCell(4);
	        if (record.getR82_moreThan3MonthTo6Months() != null) {
	            R82cell3.setCellValue(record.getR82_moreThan3MonthTo6Months().doubleValue());
	            R82cell3.setCellStyle(numberStyle);
	        } else {
	            R82cell3.setCellValue("");
	            R82cell3.setCellStyle(textStyle);
	        }

	        Cell R82cell4 = row.createCell(5);
	        if (record.getR82_moreThan6MonthTo12Months() != null) {
	            R82cell4.setCellValue(record.getR82_moreThan6MonthTo12Months().doubleValue());
	            R82cell4.setCellStyle(numberStyle);
	        } else {
	            R82cell4.setCellValue("");
	            R82cell4.setCellStyle(textStyle);
	        }

	        Cell R82cell5 = row.createCell(6);
	        if (record.getR82_moreThan12MonthTo3Years() != null) {
	            R82cell5.setCellValue(record.getR82_moreThan12MonthTo3Years().doubleValue());
	            R82cell5.setCellStyle(numberStyle);
	        } else {
	            R82cell5.setCellValue("");
	            R82cell5.setCellStyle(textStyle);
	        }

	        Cell R82cell6 = row.createCell(7);
	        if (record.getR82_moreThan3YearsTo5Years() != null) {
	            R82cell6.setCellValue(record.getR82_moreThan3YearsTo5Years().doubleValue());
	            R82cell6.setCellStyle(numberStyle);
	        } else {
	            R82cell6.setCellValue("");
	            R82cell6.setCellStyle(textStyle);
	        }

	        Cell R82cell7 = row.createCell(8);
	        if (record.getR82_moreThan5YearsTo10Years() != null) {
	            R82cell7.setCellValue(record.getR82_moreThan5YearsTo10Years().doubleValue());
	            R82cell7.setCellStyle(numberStyle);
	        } else {
	            R82cell7.setCellValue("");
	            R82cell7.setCellStyle(textStyle);
	        }

	        Cell R82cell8 = row.createCell(9);
	        if (record.getR82_moreThan10Years() != null) {
	            R82cell8.setCellValue(record.getR82_moreThan10Years().doubleValue());
	            R82cell8.setCellStyle(numberStyle);
	        } else {
	            R82cell8.setCellValue("");
	            R82cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(82);
	        Cell R83cell1 = row.createCell(2);
	        if (record.getR83_upTo1Month() != null) {
	            R83cell1.setCellValue(record.getR83_upTo1Month().doubleValue());
	            R83cell1.setCellStyle(numberStyle);
	        } else {
	            R83cell1.setCellValue("");
	            R83cell1.setCellStyle(textStyle);
	        }

	        Cell R83cell2 = row.createCell(3);
	        if (record.getR83_moreThan1MonthTo3Months() != null) {
	            R83cell2.setCellValue(record.getR83_moreThan1MonthTo3Months().doubleValue());
	            R83cell2.setCellStyle(numberStyle);
	        } else {
	            R83cell2.setCellValue("");
	            R83cell2.setCellStyle(textStyle);
	        }

	        Cell R83cell3 = row.createCell(4);
	        if (record.getR83_moreThan3MonthTo6Months() != null) {
	            R83cell3.setCellValue(record.getR83_moreThan3MonthTo6Months().doubleValue());
	            R83cell3.setCellStyle(numberStyle);
	        } else {
	            R83cell3.setCellValue("");
	            R83cell3.setCellStyle(textStyle);
	        }

	        Cell R83cell4 = row.createCell(5);
	        if (record.getR83_moreThan6MonthTo12Months() != null) {
	            R83cell4.setCellValue(record.getR83_moreThan6MonthTo12Months().doubleValue());
	            R83cell4.setCellStyle(numberStyle);
	        } else {
	            R83cell4.setCellValue("");
	            R83cell4.setCellStyle(textStyle);
	        }

	        Cell R83cell5 = row.createCell(6);
	        if (record.getR83_moreThan12MonthTo3Years() != null) {
	            R83cell5.setCellValue(record.getR83_moreThan12MonthTo3Years().doubleValue());
	            R83cell5.setCellStyle(numberStyle);
	        } else {
	            R83cell5.setCellValue("");
	            R83cell5.setCellStyle(textStyle);
	        }

	        Cell R83cell6 = row.createCell(7);
	        if (record.getR83_moreThan3YearsTo5Years() != null) {
	            R83cell6.setCellValue(record.getR83_moreThan3YearsTo5Years().doubleValue());
	            R83cell6.setCellStyle(numberStyle);
	        } else {
	            R83cell6.setCellValue("");
	            R83cell6.setCellStyle(textStyle);
	        }

	        Cell R83cell7 = row.createCell(8);
	        if (record.getR83_moreThan5YearsTo10Years() != null) {
	            R83cell7.setCellValue(record.getR83_moreThan5YearsTo10Years().doubleValue());
	            R83cell7.setCellStyle(numberStyle);
	        } else {
	            R83cell7.setCellValue("");
	            R83cell7.setCellStyle(textStyle);
	        }

	        Cell R83cell8 = row.createCell(9);
	        if (record.getR83_moreThan10Years() != null) {
	            R83cell8.setCellValue(record.getR83_moreThan10Years().doubleValue());
	            R83cell8.setCellStyle(numberStyle);
	        } else {
	            R83cell8.setCellValue("");
	            R83cell8.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(84);
	        Cell R85cell9 = row.createCell(10);
	        if (record.getR85_nonRatioSensativeItems() != null) {
	            R85cell9.setCellValue(record.getR85_nonRatioSensativeItems().doubleValue());
	            R85cell9.setCellStyle(numberStyle);
	        } else {
	            R85cell9.setCellValue("");
	            R85cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(85);
	        Cell R86cell9 = row.createCell(10);
	        if (record.getR86_nonRatioSensativeItems() != null) {
	            R86cell9.setCellValue(record.getR86_nonRatioSensativeItems().doubleValue());
	            R86cell9.setCellStyle(numberStyle);
	        } else {
	            R86cell9.setCellValue("");
	            R86cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(86);
	        Cell R87cell9 = row.createCell(10);
	        if (record.getR87_nonRatioSensativeItems() != null) {
	            R87cell9.setCellValue(record.getR87_nonRatioSensativeItems().doubleValue());
	            R87cell9.setCellStyle(numberStyle);
	        } else {
	            R87cell9.setCellValue("");
	            R87cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(87);
	        Cell R88cell9 = row.createCell(10);
	        if (record.getR88_nonRatioSensativeItems() != null) {
	            R88cell9.setCellValue(record.getR88_nonRatioSensativeItems().doubleValue());
	            R88cell9.setCellStyle(numberStyle);
	        } else {
	            R88cell9.setCellValue("");
	            R88cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(88);
	        Cell R89cell9 = row.createCell(10);
	        if (record.getR89_nonRatioSensativeItems() != null) {
	            R89cell9.setCellValue(record.getR89_nonRatioSensativeItems().doubleValue());
	            R89cell9.setCellStyle(numberStyle);
	        } else {
	            R89cell9.setCellValue("");
	            R89cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(89);
	        Cell R90cell9 = row.createCell(10);
	        if (record.getR90_nonRatioSensativeItems() != null) {
	            R90cell9.setCellValue(record.getR90_nonRatioSensativeItems().doubleValue());
	            R90cell9.setCellStyle(numberStyle);
	        } else {
	            R90cell9.setCellValue("");
	            R90cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(90);
	        Cell R91cell9 = row.createCell(10);
	        if (record.getR91_nonRatioSensativeItems() != null) {
	            R91cell9.setCellValue(record.getR91_nonRatioSensativeItems().doubleValue());
	            R91cell9.setCellStyle(numberStyle);
	        } else {
	            R91cell9.setCellValue("");
	            R91cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(95);
	        Cell R96cell1 = row.createCell(2);
	        if (record.getR96_upTo1Month() != null) {
	            R96cell1.setCellValue(record.getR96_upTo1Month().doubleValue());
	            R96cell1.setCellStyle(numberStyle);
	        } else {
	            R96cell1.setCellValue("");
	            R96cell1.setCellStyle(textStyle);
	        }

	        Cell R96cell2 = row.createCell(3);
	        if (record.getR96_moreThan1MonthTo3Months() != null) {
	            R96cell2.setCellValue(record.getR96_moreThan1MonthTo3Months().doubleValue());
	            R96cell2.setCellStyle(numberStyle);
	        } else {
	            R96cell2.setCellValue("");
	            R96cell2.setCellStyle(textStyle);
	        }

	        Cell R96cell3 = row.createCell(4);
	        if (record.getR96_moreThan3MonthTo6Months() != null) {
	            R96cell3.setCellValue(record.getR96_moreThan3MonthTo6Months().doubleValue());
	            R96cell3.setCellStyle(numberStyle);
	        } else {
	            R96cell3.setCellValue("");
	            R96cell3.setCellStyle(textStyle);
	        }

	        Cell R96cell4 = row.createCell(5);
	        if (record.getR96_moreThan6MonthTo12Months() != null) {
	            R96cell4.setCellValue(record.getR96_moreThan6MonthTo12Months().doubleValue());
	            R96cell4.setCellStyle(numberStyle);
	        } else {
	            R96cell4.setCellValue("");
	            R96cell4.setCellStyle(textStyle);
	        }

	        Cell R96cell5 = row.createCell(6);
	        if (record.getR96_moreThan12MonthTo3Years() != null) {
	            R96cell5.setCellValue(record.getR96_moreThan12MonthTo3Years().doubleValue());
	            R96cell5.setCellStyle(numberStyle);
	        } else {
	            R96cell5.setCellValue("");
	            R96cell5.setCellStyle(textStyle);
	        }

	        Cell R96cell6 = row.createCell(7);
	        if (record.getR96_moreThan3YearsTo5Years() != null) {
	            R96cell6.setCellValue(record.getR96_moreThan3YearsTo5Years().doubleValue());
	            R96cell6.setCellStyle(numberStyle);
	        } else {
	            R96cell6.setCellValue("");
	            R96cell6.setCellStyle(textStyle);
	        }

	        Cell R96cell7 = row.createCell(8);
	        if (record.getR96_moreThan5YearsTo10Years() != null) {
	            R96cell7.setCellValue(record.getR96_moreThan5YearsTo10Years().doubleValue());
	            R96cell7.setCellStyle(numberStyle);
	        } else {
	            R96cell7.setCellValue("");
	            R96cell7.setCellStyle(textStyle);
	        }

	        Cell R96cell8 = row.createCell(9);
	        if (record.getR96_moreThan10Years() != null) {
	            R96cell8.setCellValue(record.getR96_moreThan10Years().doubleValue());
	            R96cell8.setCellStyle(numberStyle);
	        } else {
	            R96cell8.setCellValue("");
	            R96cell8.setCellStyle(textStyle);
	        }

	        Cell R96cell9 = row.createCell(10);
	        if (record.getR96_nonRatioSensativeItems() != null) {
	            R96cell9.setCellValue(record.getR96_nonRatioSensativeItems().doubleValue());
	            R96cell9.setCellStyle(numberStyle);
	        } else {
	            R96cell9.setCellValue("");
	            R96cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(99);
	        Cell R100cell1 = row.createCell(2);
	        if (record.getR100_upTo1Month() != null) {
	            R100cell1.setCellValue(record.getR100_upTo1Month().doubleValue());
	            R100cell1.setCellStyle(numberStyle);
	        } else {
	            R100cell1.setCellValue("");
	            R100cell1.setCellStyle(textStyle);
	        }

	        Cell R100cell2 = row.createCell(3);
	        if (record.getR100_moreThan1MonthTo3Months() != null) {
	            R100cell2.setCellValue(record.getR100_moreThan1MonthTo3Months().doubleValue());
	            R100cell2.setCellStyle(numberStyle);
	        } else {
	            R100cell2.setCellValue("");
	            R100cell2.setCellStyle(textStyle);
	        }

	        Cell R100cell3 = row.createCell(4);
	        if (record.getR100_moreThan3MonthTo6Months() != null) {
	            R100cell3.setCellValue(record.getR100_moreThan3MonthTo6Months().doubleValue());
	            R100cell3.setCellStyle(numberStyle);
	        } else {
	            R100cell3.setCellValue("");
	            R100cell3.setCellStyle(textStyle);
	        }

	        Cell R100cell4 = row.createCell(5);
	        if (record.getR100_moreThan6MonthTo12Months() != null) {
	            R100cell4.setCellValue(record.getR100_moreThan6MonthTo12Months().doubleValue());
	            R100cell4.setCellStyle(numberStyle);
	        } else {
	            R100cell4.setCellValue("");
	            R100cell4.setCellStyle(textStyle);
	        }

	        Cell R100cell5 = row.createCell(6);
	        if (record.getR100_moreThan12MonthTo3Years() != null) {
	            R100cell5.setCellValue(record.getR100_moreThan12MonthTo3Years().doubleValue());
	            R100cell5.setCellStyle(numberStyle);
	        } else {
	            R100cell5.setCellValue("");
	            R100cell5.setCellStyle(textStyle);
	        }

	        Cell R100cell6 = row.createCell(7);
	        if (record.getR100_moreThan3YearsTo5Years() != null) {
	            R100cell6.setCellValue(record.getR100_moreThan3YearsTo5Years().doubleValue());
	            R100cell6.setCellStyle(numberStyle);
	        } else {
	            R100cell6.setCellValue("");
	            R100cell6.setCellStyle(textStyle);
	        }

	        Cell R100cell7 = row.createCell(8);
	        if (record.getR100_moreThan5YearsTo10Years() != null) {
	            R100cell7.setCellValue(record.getR100_moreThan5YearsTo10Years().doubleValue());
	            R100cell7.setCellStyle(numberStyle);
	        } else {
	            R100cell7.setCellValue("");
	            R100cell7.setCellStyle(textStyle);
	        }

	        Cell R100cell8 = row.createCell(9);
	        if (record.getR100_moreThan10Years() != null) {
	            R100cell8.setCellValue(record.getR100_moreThan10Years().doubleValue());
	            R100cell8.setCellStyle(numberStyle);
	        } else {
	            R100cell8.setCellValue("");
	            R100cell8.setCellStyle(textStyle);
	        }

	        Cell R100cell9 = row.createCell(10);
	        if (record.getR100_nonRatioSensativeItems() != null) {
	            R100cell9.setCellValue(record.getR100_nonRatioSensativeItems().doubleValue());
	            R100cell9.setCellStyle(numberStyle);
	        } else {
	            R100cell9.setCellValue("");
	            R100cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(100);
	        Cell R101cell1 = row.createCell(2);
	        if (record.getR101_upTo1Month() != null) {
	            R101cell1.setCellValue(record.getR101_upTo1Month().doubleValue());
	            R101cell1.setCellStyle(numberStyle);
	        } else {
	            R101cell1.setCellValue("");
	            R101cell1.setCellStyle(textStyle);
	        }

	        Cell R101cell2 = row.createCell(3);
	        if (record.getR101_moreThan1MonthTo3Months() != null) {
	            R101cell2.setCellValue(record.getR101_moreThan1MonthTo3Months().doubleValue());
	            R101cell2.setCellStyle(numberStyle);
	        } else {
	            R101cell2.setCellValue("");
	            R101cell2.setCellStyle(textStyle);
	        }

	        Cell R101cell3 = row.createCell(4);
	        if (record.getR101_moreThan3MonthTo6Months() != null) {
	            R101cell3.setCellValue(record.getR101_moreThan3MonthTo6Months().doubleValue());
	            R101cell3.setCellStyle(numberStyle);
	        } else {
	            R101cell3.setCellValue("");
	            R101cell3.setCellStyle(textStyle);
	        }

	        Cell R101cell4 = row.createCell(5);
	        if (record.getR101_moreThan6MonthTo12Months() != null) {
	            R101cell4.setCellValue(record.getR101_moreThan6MonthTo12Months().doubleValue());
	            R101cell4.setCellStyle(numberStyle);
	        } else {
	            R101cell4.setCellValue("");
	            R101cell4.setCellStyle(textStyle);
	        }

	        Cell R101cell5 = row.createCell(6);
	        if (record.getR101_moreThan12MonthTo3Years() != null) {
	            R101cell5.setCellValue(record.getR101_moreThan12MonthTo3Years().doubleValue());
	            R101cell5.setCellStyle(numberStyle);
	        } else {
	            R101cell5.setCellValue("");
	            R101cell5.setCellStyle(textStyle);
	        }

	        Cell R101cell6 = row.createCell(7);
	        if (record.getR101_moreThan3YearsTo5Years() != null) {
	            R101cell6.setCellValue(record.getR101_moreThan3YearsTo5Years().doubleValue());
	            R101cell6.setCellStyle(numberStyle);
	        } else {
	            R101cell6.setCellValue("");
	            R101cell6.setCellStyle(textStyle);
	        }

	        Cell R101cell7 = row.createCell(8);
	        if (record.getR101_moreThan5YearsTo10Years() != null) {
	            R101cell7.setCellValue(record.getR101_moreThan5YearsTo10Years().doubleValue());
	            R101cell7.setCellStyle(numberStyle);
	        } else {
	            R101cell7.setCellValue("");
	            R101cell7.setCellStyle(textStyle);
	        }

	        Cell R101cell8 = row.createCell(9);
	        if (record.getR101_moreThan10Years() != null) {
	            R101cell8.setCellValue(record.getR101_moreThan10Years().doubleValue());
	            R101cell8.setCellStyle(numberStyle);
	        } else {
	            R101cell8.setCellValue("");
	            R101cell8.setCellStyle(textStyle);
	        }

	        Cell R101cell9 = row.createCell(10);
	        if (record.getR101_nonRatioSensativeItems() != null) {
	            R101cell9.setCellValue(record.getR101_nonRatioSensativeItems().doubleValue());
	            R101cell9.setCellStyle(numberStyle);
	        } else {
	            R101cell9.setCellValue("");
	            R101cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(101);
	        Cell R102cell1 = row.createCell(2);
	        if (record.getR102_upTo1Month() != null) {
	            R102cell1.setCellValue(record.getR102_upTo1Month().doubleValue());
	            R102cell1.setCellStyle(numberStyle);
	        } else {
	            R102cell1.setCellValue("");
	            R102cell1.setCellStyle(textStyle);
	        }

	        Cell R102cell2 = row.createCell(3);
	        if (record.getR102_moreThan1MonthTo3Months() != null) {
	            R102cell2.setCellValue(record.getR102_moreThan1MonthTo3Months().doubleValue());
	            R102cell2.setCellStyle(numberStyle);
	        } else {
	            R102cell2.setCellValue("");
	            R102cell2.setCellStyle(textStyle);
	        }

	        Cell R102cell3 = row.createCell(4);
	        if (record.getR102_moreThan3MonthTo6Months() != null) {
	            R102cell3.setCellValue(record.getR102_moreThan3MonthTo6Months().doubleValue());
	            R102cell3.setCellStyle(numberStyle);
	        } else {
	            R102cell3.setCellValue("");
	            R102cell3.setCellStyle(textStyle);
	        }

	        Cell R102cell4 = row.createCell(5);
	        if (record.getR102_moreThan6MonthTo12Months() != null) {
	            R102cell4.setCellValue(record.getR102_moreThan6MonthTo12Months().doubleValue());
	            R102cell4.setCellStyle(numberStyle);
	        } else {
	            R102cell4.setCellValue("");
	            R102cell4.setCellStyle(textStyle);
	        }

	        Cell R102cell5 = row.createCell(6);
	        if (record.getR102_moreThan12MonthTo3Years() != null) {
	            R102cell5.setCellValue(record.getR102_moreThan12MonthTo3Years().doubleValue());
	            R102cell5.setCellStyle(numberStyle);
	        } else {
	            R102cell5.setCellValue("");
	            R102cell5.setCellStyle(textStyle);
	        }

	        Cell R102cell6 = row.createCell(7);
	        if (record.getR102_moreThan3YearsTo5Years() != null) {
	            R102cell6.setCellValue(record.getR102_moreThan3YearsTo5Years().doubleValue());
	            R102cell6.setCellStyle(numberStyle);
	        } else {
	            R102cell6.setCellValue("");
	            R102cell6.setCellStyle(textStyle);
	        }

	        Cell R102cell7 = row.createCell(8);
	        if (record.getR102_moreThan5YearsTo10Years() != null) {
	            R102cell7.setCellValue(record.getR102_moreThan5YearsTo10Years().doubleValue());
	            R102cell7.setCellStyle(numberStyle);
	        } else {
	            R102cell7.setCellValue("");
	            R102cell7.setCellStyle(textStyle);
	        }

	        Cell R102cell8 = row.createCell(9);
	        if (record.getR102_moreThan10Years() != null) {
	            R102cell8.setCellValue(record.getR102_moreThan10Years().doubleValue());
	            R102cell8.setCellStyle(numberStyle);
	        } else {
	            R102cell8.setCellValue("");
	            R102cell8.setCellStyle(textStyle);
	        }

	        Cell R102cell9 = row.createCell(10);
	        if (record.getR102_nonRatioSensativeItems() != null) {
	            R102cell9.setCellValue(record.getR102_nonRatioSensativeItems().doubleValue());
	            R102cell9.setCellStyle(numberStyle);
	        } else {
	            R102cell9.setCellValue("");
	            R102cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(102);
	        Cell R103cell1 = row.createCell(2);
	        if (record.getR103_upTo1Month() != null) {
	            R103cell1.setCellValue(record.getR103_upTo1Month().doubleValue());
	            R103cell1.setCellStyle(numberStyle);
	        } else {
	            R103cell1.setCellValue("");
	            R103cell1.setCellStyle(textStyle);
	        }

	        Cell R103cell2 = row.createCell(3);
	        if (record.getR103_moreThan1MonthTo3Months() != null) {
	            R103cell2.setCellValue(record.getR103_moreThan1MonthTo3Months().doubleValue());
	            R103cell2.setCellStyle(numberStyle);
	        } else {
	            R103cell2.setCellValue("");
	            R103cell2.setCellStyle(textStyle);
	        }

	        Cell R103cell3 = row.createCell(4);
	        if (record.getR103_moreThan3MonthTo6Months() != null) {
	            R103cell3.setCellValue(record.getR103_moreThan3MonthTo6Months().doubleValue());
	            R103cell3.setCellStyle(numberStyle);
	        } else {
	            R103cell3.setCellValue("");
	            R103cell3.setCellStyle(textStyle);
	        }

	        Cell R103cell4 = row.createCell(5);
	        if (record.getR103_moreThan6MonthTo12Months() != null) {
	            R103cell4.setCellValue(record.getR103_moreThan6MonthTo12Months().doubleValue());
	            R103cell4.setCellStyle(numberStyle);
	        } else {
	            R103cell4.setCellValue("");
	            R103cell4.setCellStyle(textStyle);
	        }

	        Cell R103cell5 = row.createCell(6);
	        if (record.getR103_moreThan12MonthTo3Years() != null) {
	            R103cell5.setCellValue(record.getR103_moreThan12MonthTo3Years().doubleValue());
	            R103cell5.setCellStyle(numberStyle);
	        } else {
	            R103cell5.setCellValue("");
	            R103cell5.setCellStyle(textStyle);
	        }

	        Cell R103cell6 = row.createCell(7);
	        if (record.getR103_moreThan3YearsTo5Years() != null) {
	            R103cell6.setCellValue(record.getR103_moreThan3YearsTo5Years().doubleValue());
	            R103cell6.setCellStyle(numberStyle);
	        } else {
	            R103cell6.setCellValue("");
	            R103cell6.setCellStyle(textStyle);
	        }

	        Cell R103cell7 = row.createCell(8);
	        if (record.getR103_moreThan5YearsTo10Years() != null) {
	            R103cell7.setCellValue(record.getR103_moreThan5YearsTo10Years().doubleValue());
	            R103cell7.setCellStyle(numberStyle);
	        } else {
	            R103cell7.setCellValue("");
	            R103cell7.setCellStyle(textStyle);
	        }

	        Cell R103cell8 = row.createCell(9);
	        if (record.getR103_moreThan10Years() != null) {
	            R103cell8.setCellValue(record.getR103_moreThan10Years().doubleValue());
	            R103cell8.setCellStyle(numberStyle);
	        } else {
	            R103cell8.setCellValue("");
	            R103cell8.setCellStyle(textStyle);
	        }

	        Cell R103cell9 = row.createCell(10);
	        if (record.getR103_nonRatioSensativeItems() != null) {
	            R103cell9.setCellValue(record.getR103_nonRatioSensativeItems().doubleValue());
	            R103cell9.setCellStyle(numberStyle);
	        } else {
	            R103cell9.setCellValue("");
	            R103cell9.setCellStyle(textStyle);
	        }

	        row = sheet.getRow(103);
	        Cell R104cell1 = row.createCell(2);
	        if (record.getR104_upTo1Month() != null) {
	            R104cell1.setCellValue(record.getR104_upTo1Month().doubleValue());
	            R104cell1.setCellStyle(numberStyle);
	        } else {
	            R104cell1.setCellValue("");
	            R104cell1.setCellStyle(textStyle);
	        }

	        Cell R104cell2 = row.createCell(3);
	        if (record.getR104_moreThan1MonthTo3Months() != null) {
	            R104cell2.setCellValue(record.getR104_moreThan1MonthTo3Months().doubleValue());
	            R104cell2.setCellStyle(numberStyle);
	        } else {
	            R104cell2.setCellValue("");
	            R104cell2.setCellStyle(textStyle);
	        }

	        Cell R104cell3 = row.createCell(4);
	        if (record.getR104_moreThan3MonthTo6Months() != null) {
	            R104cell3.setCellValue(record.getR104_moreThan3MonthTo6Months().doubleValue());
	            R104cell3.setCellStyle(numberStyle);
	        } else {
	            R104cell3.setCellValue("");
	            R104cell3.setCellStyle(textStyle);
	        }

	        Cell R104cell4 = row.createCell(5);
	        if (record.getR104_moreThan6MonthTo12Months() != null) {
	            R104cell4.setCellValue(record.getR104_moreThan6MonthTo12Months().doubleValue());
	            R104cell4.setCellStyle(numberStyle);
	        } else {
	            R104cell4.setCellValue("");
	            R104cell4.setCellStyle(textStyle);
	        }

	        Cell R104cell5 = row.createCell(6);
	        if (record.getR104_moreThan12MonthTo3Years() != null) {
	            R104cell5.setCellValue(record.getR104_moreThan12MonthTo3Years().doubleValue());
	            R104cell5.setCellStyle(numberStyle);
	        } else {
	            R104cell5.setCellValue("");
	            R104cell5.setCellStyle(textStyle);
	        }

	        Cell R104cell6 = row.createCell(7);
	        if (record.getR104_moreThan3YearsTo5Years() != null) {
	            R104cell6.setCellValue(record.getR104_moreThan3YearsTo5Years().doubleValue());
	            R104cell6.setCellStyle(numberStyle);
	        } else {
	            R104cell6.setCellValue("");
	            R104cell6.setCellStyle(textStyle);
	        }

	        Cell R104cell7 = row.createCell(8);
	        if (record.getR104_moreThan5YearsTo10Years() != null) {
	            R104cell7.setCellValue(record.getR104_moreThan5YearsTo10Years().doubleValue());
	            R104cell7.setCellStyle(numberStyle);
	        } else {
	            R104cell7.setCellValue("");
	            R104cell7.setCellStyle(textStyle);
	        }

	        Cell R104cell8 = row.createCell(9);
	        if (record.getR104_moreThan10Years() != null) {
	            R104cell8.setCellValue(record.getR104_moreThan10Years().doubleValue());
	            R104cell8.setCellStyle(numberStyle);
	        } else {
	            R104cell8.setCellValue("");
	            R104cell8.setCellStyle(textStyle);
	        }

	        Cell R104cell9 = row.createCell(10);
	        if (record.getR104_nonRatioSensativeItems() != null) {
	            R104cell9.setCellValue(record.getR104_nonRatioSensativeItems().doubleValue());
	            R104cell9.setCellStyle(numberStyle);
	        } else {
	            R104cell9.setCellValue("");
	            R104cell9.setCellStyle(textStyle);
	        }

	   
	   }

   //ARCHIVAL FROMAT EXCEL
	public byte[] getExcelM_IRBARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_IRB_Archival_Summary_Entity> dataList = m_irb_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_PI report. Returning empty result.");
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

//Create the font
			Font font = workbook.createFont();
			font.setFontHeightInPoints((short) 8); // size 8
			font.setFontName("Arial");

			CellStyle numberStyle = workbook.createCellStyle();
//numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
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
			int startRow = 7;

			if (!dataList.isEmpty()) {
				populateArchivalEntity1Data(sheet, dataList.get(0), textStyle, numberStyle);
			}else {

			}

//Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}
	}

	//ARCHIVAL FORMAT VALUES
	private void populateArchivalEntity1Data(Sheet sheet, M_IRB_Archival_Summary_Entity record, CellStyle textStyle, CellStyle numberStyle) {

	       Row row = sheet.getRow(11) != null ? sheet.getRow(11) : sheet.createRow(11);
	       Cell R12cell1 = row.createCell(2);
	       if (record.getR12_upTo1Month() != null) {
	           R12cell1.setCellValue(record.getR12_upTo1Month().doubleValue());
	           R12cell1.setCellStyle(numberStyle);
	       } else {
	           R12cell1.setCellValue("");
	           R12cell1.setCellStyle(textStyle);
	       }

	       Cell R12cell2 = row.createCell(3);
	       if (record.getR12_moreThan1MonthTo3Months() != null) {
	           R12cell2.setCellValue(record.getR12_moreThan1MonthTo3Months().doubleValue());
	           R12cell2.setCellStyle(numberStyle);
	       } else {
	           R12cell2.setCellValue("");
	           R12cell2.setCellStyle(textStyle);
	       }

	       Cell R12cell3 = row.createCell(4);
	       if (record.getR12_moreThan3MonthTo6Months() != null) {
	           R12cell3.setCellValue(record.getR12_moreThan3MonthTo6Months().doubleValue());
	           R12cell3.setCellStyle(numberStyle);
	       } else {
	           R12cell3.setCellValue("");
	           R12cell3.setCellStyle(textStyle);
	       }

	       Cell R12cell4 = row.createCell(5);
	       if (record.getR12_moreThan6MonthTo12Months() != null) {
	           R12cell4.setCellValue(record.getR12_moreThan6MonthTo12Months().doubleValue());
	           R12cell4.setCellStyle(numberStyle);
	       } else {
	           R12cell4.setCellValue("");
	           R12cell4.setCellStyle(textStyle);
	       }

	       Cell R12cell5 = row.createCell(6);
	       if (record.getR12_moreThan12MonthTo3Years() != null) {
	           R12cell5.setCellValue(record.getR12_moreThan12MonthTo3Years().doubleValue());
	           R12cell5.setCellStyle(numberStyle);
	       } else {
	           R12cell5.setCellValue("");
	           R12cell5.setCellStyle(textStyle);
	       }

	       Cell R12cell6 = row.createCell(7);
	       if (record.getR12_moreThan3YearsTo5Years() != null) {
	           R12cell6.setCellValue(record.getR12_moreThan3YearsTo5Years().doubleValue());
	           R12cell6.setCellStyle(numberStyle);
	       } else {
	           R12cell6.setCellValue("");
	           R12cell6.setCellStyle(textStyle);
	       }

	       Cell R12cell7 = row.createCell(8);
	       if (record.getR12_moreThan5YearsTo10Years() != null) {
	           R12cell7.setCellValue(record.getR12_moreThan5YearsTo10Years().doubleValue());
	           R12cell7.setCellStyle(numberStyle);
	       } else {
	           R12cell7.setCellValue("");
	           R12cell7.setCellStyle(textStyle);
	       }

	       Cell R12cell8 = row.createCell(9);
	       if (record.getR12_moreThan10Years() != null) {
	           R12cell8.setCellValue(record.getR12_moreThan10Years().doubleValue());
	           R12cell8.setCellStyle(numberStyle);
	       } else {
	           R12cell8.setCellValue("");
	           R12cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(12);
	        Cell R13cell1 = row.createCell(2);
	       if (record.getR13_upTo1Month() != null) {
	           R13cell1.setCellValue(record.getR13_upTo1Month().doubleValue());
	           R13cell1.setCellStyle(numberStyle);
	       } else {
	           R13cell1.setCellValue("");
	           R13cell1.setCellStyle(textStyle);
	       }

	       Cell R13cell2 = row.createCell(3);
	       if (record.getR13_moreThan1MonthTo3Months() != null) {
	           R13cell2.setCellValue(record.getR13_moreThan1MonthTo3Months().doubleValue());
	           R13cell2.setCellStyle(numberStyle);
	       } else {
	           R13cell2.setCellValue("");
	           R13cell2.setCellStyle(textStyle);
	       }

	       Cell R13cell3 = row.createCell(4);
	       if (record.getR13_moreThan3MonthTo6Months() != null) {
	           R13cell3.setCellValue(record.getR13_moreThan3MonthTo6Months().doubleValue());
	           R13cell3.setCellStyle(numberStyle);
	       } else {
	           R13cell3.setCellValue("");
	           R13cell3.setCellStyle(textStyle);
	       }

	       Cell R13cell4 = row.createCell(5);
	       if (record.getR13_moreThan6MonthTo12Months() != null) {
	           R13cell4.setCellValue(record.getR13_moreThan6MonthTo12Months().doubleValue());
	           R13cell4.setCellStyle(numberStyle);
	       } else {
	           R13cell4.setCellValue("");
	           R13cell4.setCellStyle(textStyle);
	       }

	       Cell R13cell5 = row.createCell(6);
	       if (record.getR13_moreThan12MonthTo3Years() != null) {
	           R13cell5.setCellValue(record.getR13_moreThan12MonthTo3Years().doubleValue());
	           R13cell5.setCellStyle(numberStyle);
	       } else {
	           R13cell5.setCellValue("");
	           R13cell5.setCellStyle(textStyle);
	       }

	       Cell R13cell6 = row.createCell(7);
	       if (record.getR13_moreThan3YearsTo5Years() != null) {
	           R13cell6.setCellValue(record.getR13_moreThan3YearsTo5Years().doubleValue());
	           R13cell6.setCellStyle(numberStyle);
	       } else {
	           R13cell6.setCellValue("");
	           R13cell6.setCellStyle(textStyle);
	       }

	       Cell R13cell7 = row.createCell(8);
	       if (record.getR13_moreThan5YearsTo10Years() != null) {
	           R13cell7.setCellValue(record.getR13_moreThan5YearsTo10Years().doubleValue());
	           R13cell7.setCellStyle(numberStyle);
	       } else {
	           R13cell7.setCellValue("");
	           R13cell7.setCellStyle(textStyle);
	       }

	       Cell R13cell8 = row.createCell(9);
	       if (record.getR13_moreThan10Years() != null) {
	           R13cell8.setCellValue(record.getR13_moreThan10Years().doubleValue());
	           R13cell8.setCellStyle(numberStyle);
	       } else {
	           R13cell8.setCellValue("");
	           R13cell8.setCellStyle(textStyle);
	       }


	       row = sheet.getRow(13);
	       Cell R14cell1 = row.createCell(2);
	       if (record.getR14_upTo1Month() != null) {
	           R14cell1.setCellValue(record.getR14_upTo1Month().doubleValue());
	           R14cell1.setCellStyle(numberStyle);
	       } else {
	           R14cell1.setCellValue("");
	           R14cell1.setCellStyle(textStyle);
	       }

	       Cell R14cell2 = row.createCell(3);
	       if (record.getR14_moreThan1MonthTo3Months() != null) {
	           R14cell2.setCellValue(record.getR14_moreThan1MonthTo3Months().doubleValue());
	           R14cell2.setCellStyle(numberStyle);
	       } else {
	           R14cell2.setCellValue("");
	           R14cell2.setCellStyle(textStyle);
	       }

	       Cell R14cell3 = row.createCell(4);
	       if (record.getR14_moreThan3MonthTo6Months() != null) {
	           R14cell3.setCellValue(record.getR14_moreThan3MonthTo6Months().doubleValue());
	           R14cell3.setCellStyle(numberStyle);
	       } else {
	           R14cell3.setCellValue("");
	           R14cell3.setCellStyle(textStyle);
	       }

	       Cell R14cell4 = row.createCell(5);
	       if (record.getR14_moreThan6MonthTo12Months() != null) {
	           R14cell4.setCellValue(record.getR14_moreThan6MonthTo12Months().doubleValue());
	           R14cell4.setCellStyle(numberStyle);
	       } else {
	           R14cell4.setCellValue("");
	           R14cell4.setCellStyle(textStyle);
	       }

	       Cell R14cell5 = row.createCell(6);
	       if (record.getR14_moreThan12MonthTo3Years() != null) {
	           R14cell5.setCellValue(record.getR14_moreThan12MonthTo3Years().doubleValue());
	           R14cell5.setCellStyle(numberStyle);
	       } else {
	           R14cell5.setCellValue("");
	           R14cell5.setCellStyle(textStyle);
	       }

	       Cell R14cell6 = row.createCell(7);
	       if (record.getR14_moreThan3YearsTo5Years() != null) {
	           R14cell6.setCellValue(record.getR14_moreThan3YearsTo5Years().doubleValue());
	           R14cell6.setCellStyle(numberStyle);
	       } else {
	           R14cell6.setCellValue("");
	           R14cell6.setCellStyle(textStyle);
	       }

	       Cell R14cell7 = row.createCell(8);
	       if (record.getR14_moreThan5YearsTo10Years() != null) {
	           R14cell7.setCellValue(record.getR14_moreThan5YearsTo10Years().doubleValue());
	           R14cell7.setCellStyle(numberStyle);
	       } else {
	           R14cell7.setCellValue("");
	           R14cell7.setCellStyle(textStyle);
	       }

	       Cell R14cell8 = row.createCell(9);
	       if (record.getR14_moreThan10Years() != null) {
	           R14cell8.setCellValue(record.getR14_moreThan10Years().doubleValue());
	           R14cell8.setCellStyle(numberStyle);
	       } else {
	           R14cell8.setCellValue("");
	           R14cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(14);
	       Cell R15cell1 = row.createCell(2);
	       if (record.getR15_upTo1Month() != null) {
	           R15cell1.setCellValue(record.getR15_upTo1Month().doubleValue());
	           R15cell1.setCellStyle(numberStyle);
	       } else {
	           R15cell1.setCellValue("");
	           R15cell1.setCellStyle(textStyle);
	       }

	       Cell R15cell2 = row.createCell(3);
	       if (record.getR15_moreThan1MonthTo3Months() != null) {
	           R15cell2.setCellValue(record.getR15_moreThan1MonthTo3Months().doubleValue());
	           R15cell2.setCellStyle(numberStyle);
	       } else {
	           R15cell2.setCellValue("");
	           R15cell2.setCellStyle(textStyle);
	       }

	       Cell R15cell3 = row.createCell(4);
	       if (record.getR15_moreThan3MonthTo6Months() != null) {
	           R15cell3.setCellValue(record.getR15_moreThan3MonthTo6Months().doubleValue());
	           R15cell3.setCellStyle(numberStyle);
	       } else {
	           R15cell3.setCellValue("");
	           R15cell3.setCellStyle(textStyle);
	       }

	       Cell R15cell4 = row.createCell(5);
	       if (record.getR15_moreThan6MonthTo12Months() != null) {
	           R15cell4.setCellValue(record.getR15_moreThan6MonthTo12Months().doubleValue());
	           R15cell4.setCellStyle(numberStyle);
	       } else {
	           R15cell4.setCellValue("");
	           R15cell4.setCellStyle(textStyle);
	       }

	       Cell R15cell5 = row.createCell(6);
	       if (record.getR15_moreThan12MonthTo3Years() != null) {
	           R15cell5.setCellValue(record.getR15_moreThan12MonthTo3Years().doubleValue());
	           R15cell5.setCellStyle(numberStyle);
	       } else {
	           R15cell5.setCellValue("");
	           R15cell5.setCellStyle(textStyle);
	       }

	       Cell R15cell6 = row.createCell(7);
	       if (record.getR15_moreThan3YearsTo5Years() != null) {
	           R15cell6.setCellValue(record.getR15_moreThan3YearsTo5Years().doubleValue());
	           R15cell6.setCellStyle(numberStyle);
	       } else {
	           R15cell6.setCellValue("");
	           R15cell6.setCellStyle(textStyle);
	       }

	       Cell R15cell7 = row.createCell(8);
	       if (record.getR15_moreThan5YearsTo10Years() != null) {
	           R15cell7.setCellValue(record.getR15_moreThan5YearsTo10Years().doubleValue());
	           R15cell7.setCellStyle(numberStyle);
	       } else {
	           R15cell7.setCellValue("");
	           R15cell7.setCellStyle(textStyle);
	       }

	       Cell R15cell8 = row.createCell(9);
	       if (record.getR15_moreThan10Years() != null) {
	           R15cell8.setCellValue(record.getR15_moreThan10Years().doubleValue());
	           R15cell8.setCellStyle(numberStyle);
	       } else {
	           R15cell8.setCellValue("");
	           R15cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(15);
	       Cell R16cell1 = row.createCell(2);
	       if (record.getR16_upTo1Month() != null) {
	           R16cell1.setCellValue(record.getR16_upTo1Month().doubleValue());
	           R16cell1.setCellStyle(numberStyle);
	       } else {
	           R16cell1.setCellValue("");
	           R16cell1.setCellStyle(textStyle);
	       }

	       Cell R16cell2 = row.createCell(3);
	       if (record.getR16_moreThan1MonthTo3Months() != null) {
	           R16cell2.setCellValue(record.getR16_moreThan1MonthTo3Months().doubleValue());
	           R16cell2.setCellStyle(numberStyle);
	       } else {
	           R16cell2.setCellValue("");
	           R16cell2.setCellStyle(textStyle);
	       }

	       Cell R16cell3 = row.createCell(4);
	       if (record.getR16_moreThan3MonthTo6Months() != null) {
	           R16cell3.setCellValue(record.getR16_moreThan3MonthTo6Months().doubleValue());
	           R16cell3.setCellStyle(numberStyle);
	       } else {
	           R16cell3.setCellValue("");
	           R16cell3.setCellStyle(textStyle);
	       }

	       Cell R16cell4 = row.createCell(5);
	       if (record.getR16_moreThan6MonthTo12Months() != null) {
	           R16cell4.setCellValue(record.getR16_moreThan6MonthTo12Months().doubleValue());
	           R16cell4.setCellStyle(numberStyle);
	       } else {
	           R16cell4.setCellValue("");
	           R16cell4.setCellStyle(textStyle);
	       }

	       Cell R16cell5 = row.createCell(6);
	       if (record.getR16_moreThan12MonthTo3Years() != null) {
	           R16cell5.setCellValue(record.getR16_moreThan12MonthTo3Years().doubleValue());
	           R16cell5.setCellStyle(numberStyle);
	       } else {
	           R16cell5.setCellValue("");
	           R16cell5.setCellStyle(textStyle);
	       }

	       Cell R16cell6 = row.createCell(7);
	       if (record.getR16_moreThan3YearsTo5Years() != null) {
	           R16cell6.setCellValue(record.getR16_moreThan3YearsTo5Years().doubleValue());
	           R16cell6.setCellStyle(numberStyle);
	       } else {
	           R16cell6.setCellValue("");
	           R16cell6.setCellStyle(textStyle);
	       }

	       Cell R16cell7 = row.createCell(8);
	       if (record.getR16_moreThan5YearsTo10Years() != null) {
	           R16cell7.setCellValue(record.getR16_moreThan5YearsTo10Years().doubleValue());
	           R16cell7.setCellStyle(numberStyle);
	       } else {
	           R16cell7.setCellValue("");
	           R16cell7.setCellStyle(textStyle);
	       }

	       Cell R16cell8 = row.createCell(9);
	       if (record.getR16_moreThan10Years() != null) {
	           R16cell8.setCellValue(record.getR16_moreThan10Years().doubleValue());
	           R16cell8.setCellStyle(numberStyle);
	       } else {
	           R16cell8.setCellValue("");
	           R16cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(16);
	       Cell R17cell1 = row.createCell(2);
	       if (record.getR17_upTo1Month() != null) {
	           R17cell1.setCellValue(record.getR17_upTo1Month().doubleValue());
	           R17cell1.setCellStyle(numberStyle);
	       } else {
	           R17cell1.setCellValue("");
	           R17cell1.setCellStyle(textStyle);
	       }

	       Cell R17cell2 = row.createCell(3);
	       if (record.getR17_moreThan1MonthTo3Months() != null) {
	           R17cell2.setCellValue(record.getR17_moreThan1MonthTo3Months().doubleValue());
	           R17cell2.setCellStyle(numberStyle);
	       } else {
	           R17cell2.setCellValue("");
	           R17cell2.setCellStyle(textStyle);
	       }

	       Cell R17cell3 = row.createCell(4);
	       if (record.getR17_moreThan3MonthTo6Months() != null) {
	           R17cell3.setCellValue(record.getR17_moreThan3MonthTo6Months().doubleValue());
	           R17cell3.setCellStyle(numberStyle);
	       } else {
	           R17cell3.setCellValue("");
	           R17cell3.setCellStyle(textStyle);
	       }

	       Cell R17cell4 = row.createCell(5);
	       if (record.getR17_moreThan6MonthTo12Months() != null) {
	           R17cell4.setCellValue(record.getR17_moreThan6MonthTo12Months().doubleValue());
	           R17cell4.setCellStyle(numberStyle);
	       } else {
	           R17cell4.setCellValue("");
	           R17cell4.setCellStyle(textStyle);
	       }

	       Cell R17cell5 = row.createCell(6);
	       if (record.getR17_moreThan12MonthTo3Years() != null) {
	           R17cell5.setCellValue(record.getR17_moreThan12MonthTo3Years().doubleValue());
	           R17cell5.setCellStyle(numberStyle);
	       } else {
	           R17cell5.setCellValue("");
	           R17cell5.setCellStyle(textStyle);
	       }

	       Cell R17cell6 = row.createCell(7);
	       if (record.getR17_moreThan3YearsTo5Years() != null) {
	           R17cell6.setCellValue(record.getR17_moreThan3YearsTo5Years().doubleValue());
	           R17cell6.setCellStyle(numberStyle);
	       } else {
	           R17cell6.setCellValue("");
	           R17cell6.setCellStyle(textStyle);
	       }

	       Cell R17cell7 = row.createCell(8);
	       if (record.getR17_moreThan5YearsTo10Years() != null) {
	           R17cell7.setCellValue(record.getR17_moreThan5YearsTo10Years().doubleValue());
	           R17cell7.setCellStyle(numberStyle);
	       } else {
	           R17cell7.setCellValue("");
	           R17cell7.setCellStyle(textStyle);
	       }

	       Cell R17cell8 = row.createCell(9);
	       if (record.getR17_moreThan10Years() != null) {
	           R17cell8.setCellValue(record.getR17_moreThan10Years().doubleValue());
	           R17cell8.setCellStyle(numberStyle);
	       } else {
	           R17cell8.setCellValue("");
	           R17cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(17);
	       Cell R18cell1 = row.createCell(2);
	       if (record.getR18_upTo1Month() != null) {
	           R18cell1.setCellValue(record.getR18_upTo1Month().doubleValue());
	           R18cell1.setCellStyle(numberStyle);
	       } else {
	           R18cell1.setCellValue("");
	           R18cell1.setCellStyle(textStyle);
	       }

	       Cell R18cell2 = row.createCell(3);
	       if (record.getR18_moreThan1MonthTo3Months() != null) {
	           R18cell2.setCellValue(record.getR18_moreThan1MonthTo3Months().doubleValue());
	           R18cell2.setCellStyle(numberStyle);
	       } else {
	           R18cell2.setCellValue("");
	           R18cell2.setCellStyle(textStyle);
	       }

	       Cell R18cell3 = row.createCell(4);
	       if (record.getR18_moreThan3MonthTo6Months() != null) {
	           R18cell3.setCellValue(record.getR18_moreThan3MonthTo6Months().doubleValue());
	           R18cell3.setCellStyle(numberStyle);
	       } else {
	           R18cell3.setCellValue("");
	           R18cell3.setCellStyle(textStyle);
	       }

	       Cell R18cell4 = row.createCell(5);
	       if (record.getR18_moreThan6MonthTo12Months() != null) {
	           R18cell4.setCellValue(record.getR18_moreThan6MonthTo12Months().doubleValue());
	           R18cell4.setCellStyle(numberStyle);
	       } else {
	           R18cell4.setCellValue("");
	           R18cell4.setCellStyle(textStyle);
	       }

	       Cell R18cell5 = row.createCell(6);
	       if (record.getR18_moreThan12MonthTo3Years() != null) {
	           R18cell5.setCellValue(record.getR18_moreThan12MonthTo3Years().doubleValue());
	           R18cell5.setCellStyle(numberStyle);
	       } else {
	           R18cell5.setCellValue("");
	           R18cell5.setCellStyle(textStyle);
	       }

	       Cell R18cell6 = row.createCell(7);
	       if (record.getR18_moreThan3YearsTo5Years() != null) {
	           R18cell6.setCellValue(record.getR18_moreThan3YearsTo5Years().doubleValue());
	           R18cell6.setCellStyle(numberStyle);
	       } else {
	           R18cell6.setCellValue("");
	           R18cell6.setCellStyle(textStyle);
	       }

	       Cell R18cell7 = row.createCell(8);
	       if (record.getR18_moreThan5YearsTo10Years() != null) {
	           R18cell7.setCellValue(record.getR18_moreThan5YearsTo10Years().doubleValue());
	           R18cell7.setCellStyle(numberStyle);
	       } else {
	           R18cell7.setCellValue("");
	           R18cell7.setCellStyle(textStyle);
	       }

	       Cell R18cell8 = row.createCell(9);
	       if (record.getR18_moreThan10Years() != null) {
	           R18cell8.setCellValue(record.getR18_moreThan10Years().doubleValue());
	           R18cell8.setCellStyle(numberStyle);
	       } else {
	           R18cell8.setCellValue("");
	           R18cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(18);
	       Cell R19cell1 = row.createCell(2);
	       if (record.getR19_upTo1Month() != null) {
	           R19cell1.setCellValue(record.getR19_upTo1Month().doubleValue());
	           R19cell1.setCellStyle(numberStyle);
	       } else {
	           R19cell1.setCellValue("");
	           R19cell1.setCellStyle(textStyle);
	       }

	       Cell R19cell2 = row.createCell(3);
	       if (record.getR19_moreThan1MonthTo3Months() != null) {
	           R19cell2.setCellValue(record.getR19_moreThan1MonthTo3Months().doubleValue());
	           R19cell2.setCellStyle(numberStyle);
	       } else {
	           R19cell2.setCellValue("");
	           R19cell2.setCellStyle(textStyle);
	       }

	       Cell R19cell3 = row.createCell(4);
	       if (record.getR19_moreThan3MonthTo6Months() != null) {
	           R19cell3.setCellValue(record.getR19_moreThan3MonthTo6Months().doubleValue());
	           R19cell3.setCellStyle(numberStyle);
	       } else {
	           R19cell3.setCellValue("");
	           R19cell3.setCellStyle(textStyle);
	       }

	       Cell R19cell4 = row.createCell(5);
	       if (record.getR19_moreThan6MonthTo12Months() != null) {
	           R19cell4.setCellValue(record.getR19_moreThan6MonthTo12Months().doubleValue());
	           R19cell4.setCellStyle(numberStyle);
	       } else {
	           R19cell4.setCellValue("");
	           R19cell4.setCellStyle(textStyle);
	       }

	       Cell R19cell5 = row.createCell(6);
	       if (record.getR19_moreThan12MonthTo3Years() != null) {
	           R19cell5.setCellValue(record.getR19_moreThan12MonthTo3Years().doubleValue());
	           R19cell5.setCellStyle(numberStyle);
	       } else {
	           R19cell5.setCellValue("");
	           R19cell5.setCellStyle(textStyle);
	       }

	       Cell R19cell6 = row.createCell(7);
	       if (record.getR19_moreThan3YearsTo5Years() != null) {
	           R19cell6.setCellValue(record.getR19_moreThan3YearsTo5Years().doubleValue());
	           R19cell6.setCellStyle(numberStyle);
	       } else {
	           R19cell6.setCellValue("");
	           R19cell6.setCellStyle(textStyle);
	       }

	       Cell R19cell7 = row.createCell(8);
	       if (record.getR19_moreThan5YearsTo10Years() != null) {
	           R19cell7.setCellValue(record.getR19_moreThan5YearsTo10Years().doubleValue());
	           R19cell7.setCellStyle(numberStyle);
	       } else {
	           R19cell7.setCellValue("");
	           R19cell7.setCellStyle(textStyle);
	       }

	       Cell R19cell8 = row.createCell(9);
	       if (record.getR19_moreThan10Years() != null) {
	           R19cell8.setCellValue(record.getR19_moreThan10Years().doubleValue());
	           R19cell8.setCellStyle(numberStyle);
	       } else {
	           R19cell8.setCellValue("");
	           R19cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(19);
	       Cell R20cell1 = row.createCell(2);
	       if (record.getR20_upTo1Month() != null) {
	           R20cell1.setCellValue(record.getR20_upTo1Month().doubleValue());
	           R20cell1.setCellStyle(numberStyle);
	       } else {
	           R20cell1.setCellValue("");
	           R20cell1.setCellStyle(textStyle);
	       }

	       Cell R20cell2 = row.createCell(3);
	       if (record.getR20_moreThan1MonthTo3Months() != null) {
	           R20cell2.setCellValue(record.getR20_moreThan1MonthTo3Months().doubleValue());
	           R20cell2.setCellStyle(numberStyle);
	       } else {
	           R20cell2.setCellValue("");
	           R20cell2.setCellStyle(textStyle);
	       }

	       Cell R20cell3 = row.createCell(4);
	       if (record.getR20_moreThan3MonthTo6Months() != null) {
	           R20cell3.setCellValue(record.getR20_moreThan3MonthTo6Months().doubleValue());
	           R20cell3.setCellStyle(numberStyle);
	       } else {
	           R20cell3.setCellValue("");
	           R20cell3.setCellStyle(textStyle);
	       }

	       Cell R20cell4 = row.createCell(5);
	       if (record.getR20_moreThan6MonthTo12Months() != null) {
	           R20cell4.setCellValue(record.getR20_moreThan6MonthTo12Months().doubleValue());
	           R20cell4.setCellStyle(numberStyle);
	       } else {
	           R20cell4.setCellValue("");
	           R20cell4.setCellStyle(textStyle);
	       }

	       Cell R20cell5 = row.createCell(6);
	       if (record.getR20_moreThan12MonthTo3Years() != null) {
	           R20cell5.setCellValue(record.getR20_moreThan12MonthTo3Years().doubleValue());
	           R20cell5.setCellStyle(numberStyle);
	       } else {
	           R20cell5.setCellValue("");
	           R20cell5.setCellStyle(textStyle);
	       }

	       Cell R20cell6 = row.createCell(7);
	       if (record.getR20_moreThan3YearsTo5Years() != null) {
	           R20cell6.setCellValue(record.getR20_moreThan3YearsTo5Years().doubleValue());
	           R20cell6.setCellStyle(numberStyle);
	       } else {
	           R20cell6.setCellValue("");
	           R20cell6.setCellStyle(textStyle);
	       }

	       Cell R20cell7 = row.createCell(8);
	       if (record.getR20_moreThan5YearsTo10Years() != null) {
	           R20cell7.setCellValue(record.getR20_moreThan5YearsTo10Years().doubleValue());
	           R20cell7.setCellStyle(numberStyle);
	       } else {
	           R20cell7.setCellValue("");
	           R20cell7.setCellStyle(textStyle);
	       }

	       Cell R20cell8 = row.createCell(9);
	       if (record.getR20_moreThan10Years() != null) {
	           R20cell8.setCellValue(record.getR20_moreThan10Years().doubleValue());
	           R20cell8.setCellStyle(numberStyle);
	       } else {
	           R20cell8.setCellValue("");
	           R20cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(20);
	       Cell R21cell1 = row.createCell(2);
	       if (record.getR21_upTo1Month() != null) {
	           R21cell1.setCellValue(record.getR21_upTo1Month().doubleValue());
	           R21cell1.setCellStyle(numberStyle);
	       } else {
	           R21cell1.setCellValue("");
	           R21cell1.setCellStyle(textStyle);
	       }

	       Cell R21cell2 = row.createCell(3);
	       if (record.getR21_moreThan1MonthTo3Months() != null) {
	           R21cell2.setCellValue(record.getR21_moreThan1MonthTo3Months().doubleValue());
	           R21cell2.setCellStyle(numberStyle);
	       } else {
	           R21cell2.setCellValue("");
	           R21cell2.setCellStyle(textStyle);
	       }

	       Cell R21cell3 = row.createCell(4);
	       if (record.getR21_moreThan3MonthTo6Months() != null) {
	           R21cell3.setCellValue(record.getR21_moreThan3MonthTo6Months().doubleValue());
	           R21cell3.setCellStyle(numberStyle);
	       } else {
	           R21cell3.setCellValue("");
	           R21cell3.setCellStyle(textStyle);
	       }

	       Cell R21cell4 = row.createCell(5);
	       if (record.getR21_moreThan6MonthTo12Months() != null) {
	           R21cell4.setCellValue(record.getR21_moreThan6MonthTo12Months().doubleValue());
	           R21cell4.setCellStyle(numberStyle);
	       } else {
	           R21cell4.setCellValue("");
	           R21cell4.setCellStyle(textStyle);
	       }

	       Cell R21cell5 = row.createCell(6);
	       if (record.getR21_moreThan12MonthTo3Years() != null) {
	           R21cell5.setCellValue(record.getR21_moreThan12MonthTo3Years().doubleValue());
	           R21cell5.setCellStyle(numberStyle);
	       } else {
	           R21cell5.setCellValue("");
	           R21cell5.setCellStyle(textStyle);
	       }

	       Cell R21cell6 = row.createCell(7);
	       if (record.getR21_moreThan3YearsTo5Years() != null) {
	           R21cell6.setCellValue(record.getR21_moreThan3YearsTo5Years().doubleValue());
	           R21cell6.setCellStyle(numberStyle);
	       } else {
	           R21cell6.setCellValue("");
	           R21cell6.setCellStyle(textStyle);
	       }

	       Cell R21cell7 = row.createCell(8);
	       if (record.getR21_moreThan5YearsTo10Years() != null) {
	           R21cell7.setCellValue(record.getR21_moreThan5YearsTo10Years().doubleValue());
	           R21cell7.setCellStyle(numberStyle);
	       } else {
	           R21cell7.setCellValue("");
	           R21cell7.setCellStyle(textStyle);
	       }

	       Cell R21cell8 = row.createCell(9);
	       if (record.getR21_moreThan10Years() != null) {
	           R21cell8.setCellValue(record.getR21_moreThan10Years().doubleValue());
	           R21cell8.setCellStyle(numberStyle);
	       } else {
	           R21cell8.setCellValue("");
	           R21cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(21);
	       Cell R22cell1 = row.createCell(2);
	       if (record.getR22_upTo1Month() != null) {
	           R22cell1.setCellValue(record.getR22_upTo1Month().doubleValue());
	           R22cell1.setCellStyle(numberStyle);
	       } else {
	           R22cell1.setCellValue("");
	           R22cell1.setCellStyle(textStyle);
	       }

	       Cell R22cell2 = row.createCell(3);
	       if (record.getR22_moreThan1MonthTo3Months() != null) {
	           R22cell2.setCellValue(record.getR22_moreThan1MonthTo3Months().doubleValue());
	           R22cell2.setCellStyle(numberStyle);
	       } else {
	           R22cell2.setCellValue("");
	           R22cell2.setCellStyle(textStyle);
	       }

	       Cell R22cell3 = row.createCell(4);
	       if (record.getR22_moreThan3MonthTo6Months() != null) {
	           R22cell3.setCellValue(record.getR22_moreThan3MonthTo6Months().doubleValue());
	           R22cell3.setCellStyle(numberStyle);
	       } else {
	           R22cell3.setCellValue("");
	           R22cell3.setCellStyle(textStyle);
	       }

	       Cell R22cell4 = row.createCell(5);
	       if (record.getR22_moreThan6MonthTo12Months() != null) {
	           R22cell4.setCellValue(record.getR22_moreThan6MonthTo12Months().doubleValue());
	           R22cell4.setCellStyle(numberStyle);
	       } else {
	           R22cell4.setCellValue("");
	           R22cell4.setCellStyle(textStyle);
	       }

	       Cell R22cell5 = row.createCell(6);
	       if (record.getR22_moreThan12MonthTo3Years() != null) {
	           R22cell5.setCellValue(record.getR22_moreThan12MonthTo3Years().doubleValue());
	           R22cell5.setCellStyle(numberStyle);
	       } else {
	           R22cell5.setCellValue("");
	           R22cell5.setCellStyle(textStyle);
	       }

	       Cell R22cell6 = row.createCell(7);
	       if (record.getR22_moreThan3YearsTo5Years() != null) {
	           R22cell6.setCellValue(record.getR22_moreThan3YearsTo5Years().doubleValue());
	           R22cell6.setCellStyle(numberStyle);
	       } else {
	           R22cell6.setCellValue("");
	           R22cell6.setCellStyle(textStyle);
	       }

	       Cell R22cell7 = row.createCell(8);
	       if (record.getR22_moreThan5YearsTo10Years() != null) {
	           R22cell7.setCellValue(record.getR22_moreThan5YearsTo10Years().doubleValue());
	           R22cell7.setCellStyle(numberStyle);
	       } else {
	           R22cell7.setCellValue("");
	           R22cell7.setCellStyle(textStyle);
	       }

	       Cell R22cell8 = row.createCell(9);
	       if (record.getR22_moreThan10Years() != null) {
	           R22cell8.setCellValue(record.getR22_moreThan10Years().doubleValue());
	           R22cell8.setCellStyle(numberStyle);
	       } else {
	           R22cell8.setCellValue("");
	           R22cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(23);
	       Cell R24cell1 = row.createCell(2);
	       if (record.getR24_upTo1Month() != null) {
	           R24cell1.setCellValue(record.getR24_upTo1Month().doubleValue());
	           R24cell1.setCellStyle(numberStyle);
	       } else {
	           R24cell1.setCellValue("");
	           R24cell1.setCellStyle(textStyle);
	       }

	       Cell R24cell2 = row.createCell(3);
	       if (record.getR24_moreThan1MonthTo3Months() != null) {
	           R24cell2.setCellValue(record.getR24_moreThan1MonthTo3Months().doubleValue());
	           R24cell2.setCellStyle(numberStyle);
	       } else {
	           R24cell2.setCellValue("");
	           R24cell2.setCellStyle(textStyle);
	       }

	       Cell R24cell3 = row.createCell(4);
	       if (record.getR24_moreThan3MonthTo6Months() != null) {
	           R24cell3.setCellValue(record.getR24_moreThan3MonthTo6Months().doubleValue());
	           R24cell3.setCellStyle(numberStyle);
	       } else {
	           R24cell3.setCellValue("");
	           R24cell3.setCellStyle(textStyle);
	       }

	       Cell R24cell4 = row.createCell(5);
	       if (record.getR24_moreThan6MonthTo12Months() != null) {
	           R24cell4.setCellValue(record.getR24_moreThan6MonthTo12Months().doubleValue());
	           R24cell4.setCellStyle(numberStyle);
	       } else {
	           R24cell4.setCellValue("");
	           R24cell4.setCellStyle(textStyle);
	       }

	       Cell R24cell5 = row.createCell(6);
	       if (record.getR24_moreThan12MonthTo3Years() != null) {
	           R24cell5.setCellValue(record.getR24_moreThan12MonthTo3Years().doubleValue());
	           R24cell5.setCellStyle(numberStyle);
	       } else {
	           R24cell5.setCellValue("");
	           R24cell5.setCellStyle(textStyle);
	       }

	       Cell R24cell6 = row.createCell(7);
	       if (record.getR24_moreThan3YearsTo5Years() != null) {
	           R24cell6.setCellValue(record.getR24_moreThan3YearsTo5Years().doubleValue());
	           R24cell6.setCellStyle(numberStyle);
	       } else {
	           R24cell6.setCellValue("");
	           R24cell6.setCellStyle(textStyle);
	       }

	       Cell R24cell7 = row.createCell(8);
	       if (record.getR24_moreThan5YearsTo10Years() != null) {
	           R24cell7.setCellValue(record.getR24_moreThan5YearsTo10Years().doubleValue());
	           R24cell7.setCellStyle(numberStyle);
	       } else {
	           R24cell7.setCellValue("");
	           R24cell7.setCellStyle(textStyle);
	       }

	       Cell R24cell8 = row.createCell(9);
	       if (record.getR24_moreThan10Years() != null) {
	           R24cell8.setCellValue(record.getR24_moreThan10Years().doubleValue());
	           R24cell8.setCellStyle(numberStyle);
	       } else {
	           R24cell8.setCellValue("");
	           R24cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(24);
	       Cell R25cell1 = row.createCell(2);
	       if (record.getR25_upTo1Month() != null) {
	           R25cell1.setCellValue(record.getR25_upTo1Month().doubleValue());
	           R25cell1.setCellStyle(numberStyle);
	       } else {
	           R25cell1.setCellValue("");
	           R25cell1.setCellStyle(textStyle);
	       }

	       Cell R25cell2 = row.createCell(3);
	       if (record.getR25_moreThan1MonthTo3Months() != null) {
	           R25cell2.setCellValue(record.getR25_moreThan1MonthTo3Months().doubleValue());
	           R25cell2.setCellStyle(numberStyle);
	       } else {
	           R25cell2.setCellValue("");
	           R25cell2.setCellStyle(textStyle);
	       }

	       Cell R25cell3 = row.createCell(4);
	       if (record.getR25_moreThan3MonthTo6Months() != null) {
	           R25cell3.setCellValue(record.getR25_moreThan3MonthTo6Months().doubleValue());
	           R25cell3.setCellStyle(numberStyle);
	       } else {
	           R25cell3.setCellValue("");
	           R25cell3.setCellStyle(textStyle);
	       }

	       Cell R25cell4 = row.createCell(5);
	       if (record.getR25_moreThan6MonthTo12Months() != null) {
	           R25cell4.setCellValue(record.getR25_moreThan6MonthTo12Months().doubleValue());
	           R25cell4.setCellStyle(numberStyle);
	       } else {
	           R25cell4.setCellValue("");
	           R25cell4.setCellStyle(textStyle);
	       }

	       Cell R25cell5 = row.createCell(6);
	       if (record.getR25_moreThan12MonthTo3Years() != null) {
	           R25cell5.setCellValue(record.getR25_moreThan12MonthTo3Years().doubleValue());
	           R25cell5.setCellStyle(numberStyle);
	       } else {
	           R25cell5.setCellValue("");
	           R25cell5.setCellStyle(textStyle);
	       }

	       Cell R25cell6 = row.createCell(7);
	       if (record.getR25_moreThan3YearsTo5Years() != null) {
	           R25cell6.setCellValue(record.getR25_moreThan3YearsTo5Years().doubleValue());
	           R25cell6.setCellStyle(numberStyle);
	       } else {
	           R25cell6.setCellValue("");
	           R25cell6.setCellStyle(textStyle);
	       }

	       Cell R25cell7 = row.createCell(8);
	       if (record.getR25_moreThan5YearsTo10Years() != null) {
	           R25cell7.setCellValue(record.getR25_moreThan5YearsTo10Years().doubleValue());
	           R25cell7.setCellStyle(numberStyle);
	       } else {
	           R25cell7.setCellValue("");
	           R25cell7.setCellStyle(textStyle);
	       }

	       Cell R25cell8 = row.createCell(9);
	       if (record.getR25_moreThan10Years() != null) {
	           R25cell8.setCellValue(record.getR25_moreThan10Years().doubleValue());
	           R25cell8.setCellStyle(numberStyle);
	       } else {
	           R25cell8.setCellValue("");
	           R25cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(25);
	       Cell R26cell1 = row.createCell(2);
	       if (record.getR26_upTo1Month() != null) {
	           R26cell1.setCellValue(record.getR26_upTo1Month().doubleValue());
	           R26cell1.setCellStyle(numberStyle);
	       } else {
	           R26cell1.setCellValue("");
	           R26cell1.setCellStyle(textStyle);
	       }

	       Cell R26cell2 = row.createCell(3);
	       if (record.getR26_moreThan1MonthTo3Months() != null) {
	           R26cell2.setCellValue(record.getR26_moreThan1MonthTo3Months().doubleValue());
	           R26cell2.setCellStyle(numberStyle);
	       } else {
	           R26cell2.setCellValue("");
	           R26cell2.setCellStyle(textStyle);
	       }

	       Cell R26cell3 = row.createCell(4);
	       if (record.getR26_moreThan3MonthTo6Months() != null) {
	           R26cell3.setCellValue(record.getR26_moreThan3MonthTo6Months().doubleValue());
	           R26cell3.setCellStyle(numberStyle);
	       } else {
	           R26cell3.setCellValue("");
	           R26cell3.setCellStyle(textStyle);
	       }

	       Cell R26cell4 = row.createCell(5);
	       if (record.getR26_moreThan6MonthTo12Months() != null) {
	           R26cell4.setCellValue(record.getR26_moreThan6MonthTo12Months().doubleValue());
	           R26cell4.setCellStyle(numberStyle);
	       } else {
	           R26cell4.setCellValue("");
	           R26cell4.setCellStyle(textStyle);
	       }

	       Cell R26cell5 = row.createCell(6);
	       if (record.getR26_moreThan12MonthTo3Years() != null) {
	           R26cell5.setCellValue(record.getR26_moreThan12MonthTo3Years().doubleValue());
	           R26cell5.setCellStyle(numberStyle);
	       } else {
	           R26cell5.setCellValue("");
	           R26cell5.setCellStyle(textStyle);
	       }

	       Cell R26cell6 = row.createCell(7);
	       if (record.getR26_moreThan3YearsTo5Years() != null) {
	           R26cell6.setCellValue(record.getR26_moreThan3YearsTo5Years().doubleValue());
	           R26cell6.setCellStyle(numberStyle);
	       } else {
	           R26cell6.setCellValue("");
	           R26cell6.setCellStyle(textStyle);
	       }

	       Cell R26cell7 = row.createCell(8);
	       if (record.getR26_moreThan5YearsTo10Years() != null) {
	           R26cell7.setCellValue(record.getR26_moreThan5YearsTo10Years().doubleValue());
	           R26cell7.setCellStyle(numberStyle);
	       } else {
	           R26cell7.setCellValue("");
	           R26cell7.setCellStyle(textStyle);
	       }

	       Cell R26cell8 = row.createCell(9);
	       if (record.getR26_moreThan10Years() != null) {
	           R26cell8.setCellValue(record.getR26_moreThan10Years().doubleValue());
	           R26cell8.setCellStyle(numberStyle);
	       } else {
	           R26cell8.setCellValue("");
	           R26cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(26);
	       Cell R27cell1 = row.createCell(2);
	       if (record.getR27_upTo1Month() != null) {
	           R27cell1.setCellValue(record.getR27_upTo1Month().doubleValue());
	           R27cell1.setCellStyle(numberStyle);
	       } else {
	           R27cell1.setCellValue("");
	           R27cell1.setCellStyle(textStyle);
	       }

	       Cell R27cell2 = row.createCell(3);
	       if (record.getR27_moreThan1MonthTo3Months() != null) {
	           R27cell2.setCellValue(record.getR27_moreThan1MonthTo3Months().doubleValue());
	           R27cell2.setCellStyle(numberStyle);
	       } else {
	           R27cell2.setCellValue("");
	           R27cell2.setCellStyle(textStyle);
	       }

	       Cell R27cell3 = row.createCell(4);
	       if (record.getR27_moreThan3MonthTo6Months() != null) {
	           R27cell3.setCellValue(record.getR27_moreThan3MonthTo6Months().doubleValue());
	           R27cell3.setCellStyle(numberStyle);
	       } else {
	           R27cell3.setCellValue("");
	           R27cell3.setCellStyle(textStyle);
	       }

	       Cell R27cell4 = row.createCell(5);
	       if (record.getR27_moreThan6MonthTo12Months() != null) {
	           R27cell4.setCellValue(record.getR27_moreThan6MonthTo12Months().doubleValue());
	           R27cell4.setCellStyle(numberStyle);
	       } else {
	           R27cell4.setCellValue("");
	           R27cell4.setCellStyle(textStyle);
	       }

	       Cell R27cell5 = row.createCell(6);
	       if (record.getR27_moreThan12MonthTo3Years() != null) {
	           R27cell5.setCellValue(record.getR27_moreThan12MonthTo3Years().doubleValue());
	           R27cell5.setCellStyle(numberStyle);
	       } else {
	           R27cell5.setCellValue("");
	           R27cell5.setCellStyle(textStyle);
	       }

	       Cell R27cell6 = row.createCell(7);
	       if (record.getR27_moreThan3YearsTo5Years() != null) {
	           R27cell6.setCellValue(record.getR27_moreThan3YearsTo5Years().doubleValue());
	           R27cell6.setCellStyle(numberStyle);
	       } else {
	           R27cell6.setCellValue("");
	           R27cell6.setCellStyle(textStyle);
	       }

	       Cell R27cell7 = row.createCell(8);
	       if (record.getR27_moreThan5YearsTo10Years() != null) {
	           R27cell7.setCellValue(record.getR27_moreThan5YearsTo10Years().doubleValue());
	           R27cell7.setCellStyle(numberStyle);
	       } else {
	           R27cell7.setCellValue("");
	           R27cell7.setCellStyle(textStyle);
	       }

	       Cell R27cell8 = row.createCell(9);
	       if (record.getR27_moreThan10Years() != null) {
	           R27cell8.setCellValue(record.getR27_moreThan10Years().doubleValue());
	           R27cell8.setCellStyle(numberStyle);
	       } else {
	           R27cell8.setCellValue("");
	           R27cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(27);
	       Cell R28cell1 = row.createCell(2);
	       if (record.getR28_upTo1Month() != null) {
	           R28cell1.setCellValue(record.getR28_upTo1Month().doubleValue());
	           R28cell1.setCellStyle(numberStyle);
	       } else {
	           R28cell1.setCellValue("");
	           R28cell1.setCellStyle(textStyle);
	       }

	       Cell R28cell2 = row.createCell(3);
	       if (record.getR28_moreThan1MonthTo3Months() != null) {
	           R28cell2.setCellValue(record.getR28_moreThan1MonthTo3Months().doubleValue());
	           R28cell2.setCellStyle(numberStyle);
	       } else {
	           R28cell2.setCellValue("");
	           R28cell2.setCellStyle(textStyle);
	       }

	       Cell R28cell3 = row.createCell(4);
	       if (record.getR28_moreThan3MonthTo6Months() != null) {
	           R28cell3.setCellValue(record.getR28_moreThan3MonthTo6Months().doubleValue());
	           R28cell3.setCellStyle(numberStyle);
	       } else {
	           R28cell3.setCellValue("");
	           R28cell3.setCellStyle(textStyle);
	       }

	       Cell R28cell4 = row.createCell(5);
	       if (record.getR28_moreThan6MonthTo12Months() != null) {
	           R28cell4.setCellValue(record.getR28_moreThan6MonthTo12Months().doubleValue());
	           R28cell4.setCellStyle(numberStyle);
	       } else {
	           R28cell4.setCellValue("");
	           R28cell4.setCellStyle(textStyle);
	       }

	       Cell R28cell5 = row.createCell(6);
	       if (record.getR28_moreThan12MonthTo3Years() != null) {
	           R28cell5.setCellValue(record.getR28_moreThan12MonthTo3Years().doubleValue());
	           R28cell5.setCellStyle(numberStyle);
	       } else {
	           R28cell5.setCellValue("");
	           R28cell5.setCellStyle(textStyle);
	       }

	       Cell R28cell6 = row.createCell(7);
	       if (record.getR28_moreThan3YearsTo5Years() != null) {
	           R28cell6.setCellValue(record.getR28_moreThan3YearsTo5Years().doubleValue());
	           R28cell6.setCellStyle(numberStyle);
	       } else {
	           R28cell6.setCellValue("");
	           R28cell6.setCellStyle(textStyle);
	       }

	       Cell R28cell7 = row.createCell(8);
	       if (record.getR28_moreThan5YearsTo10Years() != null) {
	           R28cell7.setCellValue(record.getR28_moreThan5YearsTo10Years().doubleValue());
	           R28cell7.setCellStyle(numberStyle);
	       } else {
	           R28cell7.setCellValue("");
	           R28cell7.setCellStyle(textStyle);
	       }

	       Cell R28cell8 = row.createCell(9);
	       if (record.getR28_moreThan10Years() != null) {
	           R28cell8.setCellValue(record.getR28_moreThan10Years().doubleValue());
	           R28cell8.setCellStyle(numberStyle);
	       } else {
	           R28cell8.setCellValue("");
	           R28cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(28);
	       Cell R29cell1 = row.createCell(2);
	       if (record.getR29_upTo1Month() != null) {
	           R29cell1.setCellValue(record.getR29_upTo1Month().doubleValue());
	           R29cell1.setCellStyle(numberStyle);
	       } else {
	           R29cell1.setCellValue("");
	           R29cell1.setCellStyle(textStyle);
	       }

	       Cell R29cell2 = row.createCell(3);
	       if (record.getR29_moreThan1MonthTo3Months() != null) {
	           R29cell2.setCellValue(record.getR29_moreThan1MonthTo3Months().doubleValue());
	           R29cell2.setCellStyle(numberStyle);
	       } else {
	           R29cell2.setCellValue("");
	           R29cell2.setCellStyle(textStyle);
	       }

	       Cell R29cell3 = row.createCell(4);
	       if (record.getR29_moreThan3MonthTo6Months() != null) {
	           R29cell3.setCellValue(record.getR29_moreThan3MonthTo6Months().doubleValue());
	           R29cell3.setCellStyle(numberStyle);
	       } else {
	           R29cell3.setCellValue("");
	           R29cell3.setCellStyle(textStyle);
	       }

	       Cell R29cell4 = row.createCell(5);
	       if (record.getR29_moreThan6MonthTo12Months() != null) {
	           R29cell4.setCellValue(record.getR29_moreThan6MonthTo12Months().doubleValue());
	           R29cell4.setCellStyle(numberStyle);
	       } else {
	           R29cell4.setCellValue("");
	           R29cell4.setCellStyle(textStyle);
	       }

	       Cell R29cell5 = row.createCell(6);
	       if (record.getR29_moreThan12MonthTo3Years() != null) {
	           R29cell5.setCellValue(record.getR29_moreThan12MonthTo3Years().doubleValue());
	           R29cell5.setCellStyle(numberStyle);
	       } else {
	           R29cell5.setCellValue("");
	           R29cell5.setCellStyle(textStyle);
	       }

	       Cell R29cell6 = row.createCell(7);
	       if (record.getR29_moreThan3YearsTo5Years() != null) {
	           R29cell6.setCellValue(record.getR29_moreThan3YearsTo5Years().doubleValue());
	           R29cell6.setCellStyle(numberStyle);
	       } else {
	           R29cell6.setCellValue("");
	           R29cell6.setCellStyle(textStyle);
	       }

	       Cell R29cell7 = row.createCell(8);
	       if (record.getR29_moreThan5YearsTo10Years() != null) {
	           R29cell7.setCellValue(record.getR29_moreThan5YearsTo10Years().doubleValue());
	           R29cell7.setCellStyle(numberStyle);
	       } else {
	           R29cell7.setCellValue("");
	           R29cell7.setCellStyle(textStyle);
	       }

	       Cell R29cell8 = row.createCell(9);
	       if (record.getR29_moreThan10Years() != null) {
	           R29cell8.setCellValue(record.getR29_moreThan10Years().doubleValue());
	           R29cell8.setCellStyle(numberStyle);
	       } else {
	           R29cell8.setCellValue("");
	           R29cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(29);
	       Cell R30cell1 = row.createCell(2);
	       if (record.getR30_upTo1Month() != null) {
	           R30cell1.setCellValue(record.getR30_upTo1Month().doubleValue());
	           R30cell1.setCellStyle(numberStyle);
	       } else {
	           R30cell1.setCellValue("");
	           R30cell1.setCellStyle(textStyle);
	       }

	       Cell R30cell2 = row.createCell(3);
	       if (record.getR30_moreThan1MonthTo3Months() != null) {
	           R30cell2.setCellValue(record.getR30_moreThan1MonthTo3Months().doubleValue());
	           R30cell2.setCellStyle(numberStyle);
	       } else {
	           R30cell2.setCellValue("");
	           R30cell2.setCellStyle(textStyle);
	       }

	       Cell R30cell3 = row.createCell(4);
	       if (record.getR30_moreThan3MonthTo6Months() != null) {
	           R30cell3.setCellValue(record.getR30_moreThan3MonthTo6Months().doubleValue());
	           R30cell3.setCellStyle(numberStyle);
	       } else {
	           R30cell3.setCellValue("");
	           R30cell3.setCellStyle(textStyle);
	       }

	       Cell R30cell4 = row.createCell(5);
	       if (record.getR30_moreThan6MonthTo12Months() != null) {
	           R30cell4.setCellValue(record.getR30_moreThan6MonthTo12Months().doubleValue());
	           R30cell4.setCellStyle(numberStyle);
	       } else {
	           R30cell4.setCellValue("");
	           R30cell4.setCellStyle(textStyle);
	       }

	       Cell R30cell5 = row.createCell(6);
	       if (record.getR30_moreThan12MonthTo3Years() != null) {
	           R30cell5.setCellValue(record.getR30_moreThan12MonthTo3Years().doubleValue());
	           R30cell5.setCellStyle(numberStyle);
	       } else {
	           R30cell5.setCellValue("");
	           R30cell5.setCellStyle(textStyle);
	       }

	       Cell R30cell6 = row.createCell(7);
	       if (record.getR30_moreThan3YearsTo5Years() != null) {
	           R30cell6.setCellValue(record.getR30_moreThan3YearsTo5Years().doubleValue());
	           R30cell6.setCellStyle(numberStyle);
	       } else {
	           R30cell6.setCellValue("");
	           R30cell6.setCellStyle(textStyle);
	       }

	       Cell R30cell7 = row.createCell(8);
	       if (record.getR30_moreThan5YearsTo10Years() != null) {
	           R30cell7.setCellValue(record.getR30_moreThan5YearsTo10Years().doubleValue());
	           R30cell7.setCellStyle(numberStyle);
	       } else {
	           R30cell7.setCellValue("");
	           R30cell7.setCellStyle(textStyle);
	       }

	       Cell R30cell8 = row.createCell(9);
	       if (record.getR30_moreThan10Years() != null) {
	           R30cell8.setCellValue(record.getR30_moreThan10Years().doubleValue());
	           R30cell8.setCellStyle(numberStyle);
	       } else {
	           R30cell8.setCellValue("");
	           R30cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(30);
	       Cell R31cell1 = row.createCell(2);
	       if (record.getR31_upTo1Month() != null) {
	           R31cell1.setCellValue(record.getR31_upTo1Month().doubleValue());
	           R31cell1.setCellStyle(numberStyle);
	       } else {
	           R31cell1.setCellValue("");
	           R31cell1.setCellStyle(textStyle);
	       }

	       Cell R31cell2 = row.createCell(3);
	       if (record.getR31_moreThan1MonthTo3Months() != null) {
	           R31cell2.setCellValue(record.getR31_moreThan1MonthTo3Months().doubleValue());
	           R31cell2.setCellStyle(numberStyle);
	       } else {
	           R31cell2.setCellValue("");
	           R31cell2.setCellStyle(textStyle);
	       }

	       Cell R31cell3 = row.createCell(4);
	       if (record.getR31_moreThan3MonthTo6Months() != null) {
	           R31cell3.setCellValue(record.getR31_moreThan3MonthTo6Months().doubleValue());
	           R31cell3.setCellStyle(numberStyle);
	       } else {
	           R31cell3.setCellValue("");
	           R31cell3.setCellStyle(textStyle);
	       }

	       Cell R31cell4 = row.createCell(5);
	       if (record.getR31_moreThan6MonthTo12Months() != null) {
	           R31cell4.setCellValue(record.getR31_moreThan6MonthTo12Months().doubleValue());
	           R31cell4.setCellStyle(numberStyle);
	       } else {
	           R31cell4.setCellValue("");
	           R31cell4.setCellStyle(textStyle);
	       }

	       Cell R31cell5 = row.createCell(6);
	       if (record.getR31_moreThan12MonthTo3Years() != null) {
	           R31cell5.setCellValue(record.getR31_moreThan12MonthTo3Years().doubleValue());
	           R31cell5.setCellStyle(numberStyle);
	       } else {
	           R31cell5.setCellValue("");
	           R31cell5.setCellStyle(textStyle);
	       }

	       Cell R31cell6 = row.createCell(7);
	       if (record.getR31_moreThan3YearsTo5Years() != null) {
	           R31cell6.setCellValue(record.getR31_moreThan3YearsTo5Years().doubleValue());
	           R31cell6.setCellStyle(numberStyle);
	       } else {
	           R31cell6.setCellValue("");
	           R31cell6.setCellStyle(textStyle);
	       }

	       Cell R31cell7 = row.createCell(8);
	       if (record.getR31_moreThan5YearsTo10Years() != null) {
	           R31cell7.setCellValue(record.getR31_moreThan5YearsTo10Years().doubleValue());
	           R31cell7.setCellStyle(numberStyle);
	       } else {
	           R31cell7.setCellValue("");
	           R31cell7.setCellStyle(textStyle);
	       }

	       Cell R31cell8 = row.createCell(9);
	       if (record.getR31_moreThan10Years() != null) {
	           R31cell8.setCellValue(record.getR31_moreThan10Years().doubleValue());
	           R31cell8.setCellStyle(numberStyle);
	       } else {
	           R31cell8.setCellValue("");
	           R31cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(31);
	       Cell R32cell1 = row.createCell(2);
	       if (record.getR32_upTo1Month() != null) {
	           R32cell1.setCellValue(record.getR32_upTo1Month().doubleValue());
	           R32cell1.setCellStyle(numberStyle);
	       } else {
	           R32cell1.setCellValue("");
	           R32cell1.setCellStyle(textStyle);
	       }

	       Cell R32cell2 = row.createCell(3);
	       if (record.getR32_moreThan1MonthTo3Months() != null) {
	           R32cell2.setCellValue(record.getR32_moreThan1MonthTo3Months().doubleValue());
	           R32cell2.setCellStyle(numberStyle);
	       } else {
	           R32cell2.setCellValue("");
	           R32cell2.setCellStyle(textStyle);
	       }

	       Cell R32cell3 = row.createCell(4);
	       if (record.getR32_moreThan3MonthTo6Months() != null) {
	           R32cell3.setCellValue(record.getR32_moreThan3MonthTo6Months().doubleValue());
	           R32cell3.setCellStyle(numberStyle);
	       } else {
	           R32cell3.setCellValue("");
	           R32cell3.setCellStyle(textStyle);
	       }

	       Cell R32cell4 = row.createCell(5);
	       if (record.getR32_moreThan6MonthTo12Months() != null) {
	           R32cell4.setCellValue(record.getR32_moreThan6MonthTo12Months().doubleValue());
	           R32cell4.setCellStyle(numberStyle);
	       } else {
	           R32cell4.setCellValue("");
	           R32cell4.setCellStyle(textStyle);
	       }

	       Cell R32cell5 = row.createCell(6);
	       if (record.getR32_moreThan12MonthTo3Years() != null) {
	           R32cell5.setCellValue(record.getR32_moreThan12MonthTo3Years().doubleValue());
	           R32cell5.setCellStyle(numberStyle);
	       } else {
	           R32cell5.setCellValue("");
	           R32cell5.setCellStyle(textStyle);
	       }

	       Cell R32cell6 = row.createCell(7);
	       if (record.getR32_moreThan3YearsTo5Years() != null) {
	           R32cell6.setCellValue(record.getR32_moreThan3YearsTo5Years().doubleValue());
	           R32cell6.setCellStyle(numberStyle);
	       } else {
	           R32cell6.setCellValue("");
	           R32cell6.setCellStyle(textStyle);
	       }

	       Cell R32cell7 = row.createCell(8);
	       if (record.getR32_moreThan5YearsTo10Years() != null) {
	           R32cell7.setCellValue(record.getR32_moreThan5YearsTo10Years().doubleValue());
	           R32cell7.setCellStyle(numberStyle);
	       } else {
	           R32cell7.setCellValue("");
	           R32cell7.setCellStyle(textStyle);
	       }

	       Cell R32cell8 = row.createCell(9);
	       if (record.getR32_moreThan10Years() != null) {
	           R32cell8.setCellValue(record.getR32_moreThan10Years().doubleValue());
	           R32cell8.setCellStyle(numberStyle);
	       } else {
	           R32cell8.setCellValue("");
	           R32cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(32);
	       Cell R33cell1 = row.createCell(2);
	       if (record.getR33_upTo1Month() != null) {
	           R33cell1.setCellValue(record.getR33_upTo1Month().doubleValue());
	           R33cell1.setCellStyle(numberStyle);
	       } else {
	           R33cell1.setCellValue("");
	           R33cell1.setCellStyle(textStyle);
	       }

	       Cell R33cell2 = row.createCell(3);
	       if (record.getR33_moreThan1MonthTo3Months() != null) {
	           R33cell2.setCellValue(record.getR33_moreThan1MonthTo3Months().doubleValue());
	           R33cell2.setCellStyle(numberStyle);
	       } else {
	           R33cell2.setCellValue("");
	           R33cell2.setCellStyle(textStyle);
	       }

	       Cell R33cell3 = row.createCell(4);
	       if (record.getR33_moreThan3MonthTo6Months() != null) {
	           R33cell3.setCellValue(record.getR33_moreThan3MonthTo6Months().doubleValue());
	           R33cell3.setCellStyle(numberStyle);
	       } else {
	           R33cell3.setCellValue("");
	           R33cell3.setCellStyle(textStyle);
	       }

	       Cell R33cell4 = row.createCell(5);
	       if (record.getR33_moreThan6MonthTo12Months() != null) {
	           R33cell4.setCellValue(record.getR33_moreThan6MonthTo12Months().doubleValue());
	           R33cell4.setCellStyle(numberStyle);
	       } else {
	           R33cell4.setCellValue("");
	           R33cell4.setCellStyle(textStyle);
	       }

	       Cell R33cell5 = row.createCell(6);
	       if (record.getR33_moreThan12MonthTo3Years() != null) {
	           R33cell5.setCellValue(record.getR33_moreThan12MonthTo3Years().doubleValue());
	           R33cell5.setCellStyle(numberStyle);
	       } else {
	           R33cell5.setCellValue("");
	           R33cell5.setCellStyle(textStyle);
	       }

	       Cell R33cell6 = row.createCell(7);
	       if (record.getR33_moreThan3YearsTo5Years() != null) {
	           R33cell6.setCellValue(record.getR33_moreThan3YearsTo5Years().doubleValue());
	           R33cell6.setCellStyle(numberStyle);
	       } else {
	           R33cell6.setCellValue("");
	           R33cell6.setCellStyle(textStyle);
	       }

	       Cell R33cell7 = row.createCell(8);
	       if (record.getR33_moreThan5YearsTo10Years() != null) {
	           R33cell7.setCellValue(record.getR33_moreThan5YearsTo10Years().doubleValue());
	           R33cell7.setCellStyle(numberStyle);
	       } else {
	           R33cell7.setCellValue("");
	           R33cell7.setCellStyle(textStyle);
	       }

	       Cell R33cell8 = row.createCell(9);
	       if (record.getR33_moreThan10Years() != null) {
	           R33cell8.setCellValue(record.getR33_moreThan10Years().doubleValue());
	           R33cell8.setCellStyle(numberStyle);
	       } else {
	           R33cell8.setCellValue("");
	           R33cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(33);
	       Cell R34cell1 = row.createCell(2);
	       if (record.getR34_upTo1Month() != null) {
	           R34cell1.setCellValue(record.getR34_upTo1Month().doubleValue());
	           R34cell1.setCellStyle(numberStyle);
	       } else {
	           R34cell1.setCellValue("");
	           R34cell1.setCellStyle(textStyle);
	       }

	       Cell R34cell2 = row.createCell(3);
	       if (record.getR34_moreThan1MonthTo3Months() != null) {
	           R34cell2.setCellValue(record.getR34_moreThan1MonthTo3Months().doubleValue());
	           R34cell2.setCellStyle(numberStyle);
	       } else {
	           R34cell2.setCellValue("");
	           R34cell2.setCellStyle(textStyle);
	       }

	       Cell R34cell3 = row.createCell(4);
	       if (record.getR34_moreThan3MonthTo6Months() != null) {
	           R34cell3.setCellValue(record.getR34_moreThan3MonthTo6Months().doubleValue());
	           R34cell3.setCellStyle(numberStyle);
	       } else {
	           R34cell3.setCellValue("");
	           R34cell3.setCellStyle(textStyle);
	       }

	       Cell R34cell4 = row.createCell(5);
	       if (record.getR34_moreThan6MonthTo12Months() != null) {
	           R34cell4.setCellValue(record.getR34_moreThan6MonthTo12Months().doubleValue());
	           R34cell4.setCellStyle(numberStyle);
	       } else {
	           R34cell4.setCellValue("");
	           R34cell4.setCellStyle(textStyle);
	       }

	       Cell R34cell5 = row.createCell(6);
	       if (record.getR34_moreThan12MonthTo3Years() != null) {
	           R34cell5.setCellValue(record.getR34_moreThan12MonthTo3Years().doubleValue());
	           R34cell5.setCellStyle(numberStyle);
	       } else {
	           R34cell5.setCellValue("");
	           R34cell5.setCellStyle(textStyle);
	       }

	       Cell R34cell6 = row.createCell(7);
	       if (record.getR34_moreThan3YearsTo5Years() != null) {
	           R34cell6.setCellValue(record.getR34_moreThan3YearsTo5Years().doubleValue());
	           R34cell6.setCellStyle(numberStyle);
	       } else {
	           R34cell6.setCellValue("");
	           R34cell6.setCellStyle(textStyle);
	       }

	       Cell R34cell7 = row.createCell(8);
	       if (record.getR34_moreThan5YearsTo10Years() != null) {
	           R34cell7.setCellValue(record.getR34_moreThan5YearsTo10Years().doubleValue());
	           R34cell7.setCellStyle(numberStyle);
	       } else {
	           R34cell7.setCellValue("");
	           R34cell7.setCellStyle(textStyle);
	       }

	       Cell R34cell8 = row.createCell(9);
	       if (record.getR34_moreThan10Years() != null) {
	           R34cell8.setCellValue(record.getR34_moreThan10Years().doubleValue());
	           R34cell8.setCellStyle(numberStyle);
	       } else {
	           R34cell8.setCellValue("");
	           R34cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(35);
	       Cell R36cell1 = row.createCell(2);
	       if (record.getR36_upTo1Month() != null) {
	           R36cell1.setCellValue(record.getR36_upTo1Month().doubleValue());
	           R36cell1.setCellStyle(numberStyle);
	       } else {
	           R36cell1.setCellValue("");
	           R36cell1.setCellStyle(textStyle);
	       }

	       Cell R36cell2 = row.createCell(3);
	       if (record.getR36_moreThan1MonthTo3Months() != null) {
	           R36cell2.setCellValue(record.getR36_moreThan1MonthTo3Months().doubleValue());
	           R36cell2.setCellStyle(numberStyle);
	       } else {
	           R36cell2.setCellValue("");
	           R36cell2.setCellStyle(textStyle);
	       }

	       Cell R36cell3 = row.createCell(4);
	       if (record.getR36_moreThan3MonthTo6Months() != null) {
	           R36cell3.setCellValue(record.getR36_moreThan3MonthTo6Months().doubleValue());
	           R36cell3.setCellStyle(numberStyle);
	       } else {
	           R36cell3.setCellValue("");
	           R36cell3.setCellStyle(textStyle);
	       }

	       Cell R36cell4 = row.createCell(5);
	       if (record.getR36_moreThan6MonthTo12Months() != null) {
	           R36cell4.setCellValue(record.getR36_moreThan6MonthTo12Months().doubleValue());
	           R36cell4.setCellStyle(numberStyle);
	       } else {
	           R36cell4.setCellValue("");
	           R36cell4.setCellStyle(textStyle);
	       }

	       Cell R36cell5 = row.createCell(6);
	       if (record.getR36_moreThan12MonthTo3Years() != null) {
	           R36cell5.setCellValue(record.getR36_moreThan12MonthTo3Years().doubleValue());
	           R36cell5.setCellStyle(numberStyle);
	       } else {
	           R36cell5.setCellValue("");
	           R36cell5.setCellStyle(textStyle);
	       }

	       Cell R36cell6 = row.createCell(7);
	       if (record.getR36_moreThan3YearsTo5Years() != null) {
	           R36cell6.setCellValue(record.getR36_moreThan3YearsTo5Years().doubleValue());
	           R36cell6.setCellStyle(numberStyle);
	       } else {
	           R36cell6.setCellValue("");
	           R36cell6.setCellStyle(textStyle);
	       }

	       Cell R36cell7 = row.createCell(8);
	       if (record.getR36_moreThan5YearsTo10Years() != null) {
	           R36cell7.setCellValue(record.getR36_moreThan5YearsTo10Years().doubleValue());
	           R36cell7.setCellStyle(numberStyle);
	       } else {
	           R36cell7.setCellValue("");
	           R36cell7.setCellStyle(textStyle);
	       }

	       Cell R36cell8 = row.createCell(9);
	       if (record.getR36_moreThan10Years() != null) {
	           R36cell8.setCellValue(record.getR36_moreThan10Years().doubleValue());
	           R36cell8.setCellStyle(numberStyle);
	       } else {
	           R36cell8.setCellValue("");
	           R36cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(36);
	       Cell R37cell1 = row.createCell(2);
	       if (record.getR37_upTo1Month() != null) {
	           R37cell1.setCellValue(record.getR37_upTo1Month().doubleValue());
	           R37cell1.setCellStyle(numberStyle);
	       } else {
	           R37cell1.setCellValue("");
	           R37cell1.setCellStyle(textStyle);
	       }

	       Cell R37cell2 = row.createCell(3);
	       if (record.getR37_moreThan1MonthTo3Months() != null) {
	           R37cell2.setCellValue(record.getR37_moreThan1MonthTo3Months().doubleValue());
	           R37cell2.setCellStyle(numberStyle);
	       } else {
	           R37cell2.setCellValue("");
	           R37cell2.setCellStyle(textStyle);
	       }

	       Cell R37cell3 = row.createCell(4);
	       if (record.getR37_moreThan3MonthTo6Months() != null) {
	           R37cell3.setCellValue(record.getR37_moreThan3MonthTo6Months().doubleValue());
	           R37cell3.setCellStyle(numberStyle);
	       } else {
	           R37cell3.setCellValue("");
	           R37cell3.setCellStyle(textStyle);
	       }

	       Cell R37cell4 = row.createCell(5);
	       if (record.getR37_moreThan6MonthTo12Months() != null) {
	           R37cell4.setCellValue(record.getR37_moreThan6MonthTo12Months().doubleValue());
	           R37cell4.setCellStyle(numberStyle);
	       } else {
	           R37cell4.setCellValue("");
	           R37cell4.setCellStyle(textStyle);
	       }

	       Cell R37cell5 = row.createCell(6);
	       if (record.getR37_moreThan12MonthTo3Years() != null) {
	           R37cell5.setCellValue(record.getR37_moreThan12MonthTo3Years().doubleValue());
	           R37cell5.setCellStyle(numberStyle);
	       } else {
	           R37cell5.setCellValue("");
	           R37cell5.setCellStyle(textStyle);
	       }

	       Cell R37cell6 = row.createCell(7);
	       if (record.getR37_moreThan3YearsTo5Years() != null) {
	           R37cell6.setCellValue(record.getR37_moreThan3YearsTo5Years().doubleValue());
	           R37cell6.setCellStyle(numberStyle);
	       } else {
	           R37cell6.setCellValue("");
	           R37cell6.setCellStyle(textStyle);
	       }

	       Cell R37cell7 = row.createCell(8);
	       if (record.getR37_moreThan5YearsTo10Years() != null) {
	           R37cell7.setCellValue(record.getR37_moreThan5YearsTo10Years().doubleValue());
	           R37cell7.setCellStyle(numberStyle);
	       } else {
	           R37cell7.setCellValue("");
	           R37cell7.setCellStyle(textStyle);
	       }

	       Cell R37cell8 = row.createCell(9);
	       if (record.getR37_moreThan10Years() != null) {
	           R37cell8.setCellValue(record.getR37_moreThan10Years().doubleValue());
	           R37cell8.setCellStyle(numberStyle);
	       } else {
	           R37cell8.setCellValue("");
	           R37cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(37);
	       Cell R38cell1 = row.createCell(2);
	       if (record.getR38_upTo1Month() != null) {
	           R38cell1.setCellValue(record.getR38_upTo1Month().doubleValue());
	           R38cell1.setCellStyle(numberStyle);
	       } else {
	           R38cell1.setCellValue("");
	           R38cell1.setCellStyle(textStyle);
	       }

	       Cell R38cell2 = row.createCell(3);
	       if (record.getR38_moreThan1MonthTo3Months() != null) {
	           R38cell2.setCellValue(record.getR38_moreThan1MonthTo3Months().doubleValue());
	           R38cell2.setCellStyle(numberStyle);
	       } else {
	           R38cell2.setCellValue("");
	           R38cell2.setCellStyle(textStyle);
	       }

	       Cell R38cell3 = row.createCell(4);
	       if (record.getR38_moreThan3MonthTo6Months() != null) {
	           R38cell3.setCellValue(record.getR38_moreThan3MonthTo6Months().doubleValue());
	           R38cell3.setCellStyle(numberStyle);
	       } else {
	           R38cell3.setCellValue("");
	           R38cell3.setCellStyle(textStyle);
	       }

	       Cell R38cell4 = row.createCell(5);
	       if (record.getR38_moreThan6MonthTo12Months() != null) {
	           R38cell4.setCellValue(record.getR38_moreThan6MonthTo12Months().doubleValue());
	           R38cell4.setCellStyle(numberStyle);
	       } else {
	           R38cell4.setCellValue("");
	           R38cell4.setCellStyle(textStyle);
	       }

	       Cell R38cell5 = row.createCell(6);
	       if (record.getR38_moreThan12MonthTo3Years() != null) {
	           R38cell5.setCellValue(record.getR38_moreThan12MonthTo3Years().doubleValue());
	           R38cell5.setCellStyle(numberStyle);
	       } else {
	           R38cell5.setCellValue("");
	           R38cell5.setCellStyle(textStyle);
	       }

	       Cell R38cell6 = row.createCell(7);
	       if (record.getR38_moreThan3YearsTo5Years() != null) {
	           R38cell6.setCellValue(record.getR38_moreThan3YearsTo5Years().doubleValue());
	           R38cell6.setCellStyle(numberStyle);
	       } else {
	           R38cell6.setCellValue("");
	           R38cell6.setCellStyle(textStyle);
	       }

	       Cell R38cell7 = row.createCell(8);
	       if (record.getR38_moreThan5YearsTo10Years() != null) {
	           R38cell7.setCellValue(record.getR38_moreThan5YearsTo10Years().doubleValue());
	           R38cell7.setCellStyle(numberStyle);
	       } else {
	           R38cell7.setCellValue("");
	           R38cell7.setCellStyle(textStyle);
	       }

	       Cell R38cell8 = row.createCell(9);
	       if (record.getR38_moreThan10Years() != null) {
	           R38cell8.setCellValue(record.getR38_moreThan10Years().doubleValue());
	           R38cell8.setCellStyle(numberStyle);
	       } else {
	           R38cell8.setCellValue("");
	           R38cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(38);
	       Cell R39cell1 = row.createCell(2);
	       if (record.getR39_upTo1Month() != null) {
	           R39cell1.setCellValue(record.getR39_upTo1Month().doubleValue());
	           R39cell1.setCellStyle(numberStyle);
	       } else {
	           R39cell1.setCellValue("");
	           R39cell1.setCellStyle(textStyle);
	       }

	       Cell R39cell2 = row.createCell(3);
	       if (record.getR39_moreThan1MonthTo3Months() != null) {
	           R39cell2.setCellValue(record.getR39_moreThan1MonthTo3Months().doubleValue());
	           R39cell2.setCellStyle(numberStyle);
	       } else {
	           R39cell2.setCellValue("");
	           R39cell2.setCellStyle(textStyle);
	       }

	       Cell R39cell3 = row.createCell(4);
	       if (record.getR39_moreThan3MonthTo6Months() != null) {
	           R39cell3.setCellValue(record.getR39_moreThan3MonthTo6Months().doubleValue());
	           R39cell3.setCellStyle(numberStyle);
	       } else {
	           R39cell3.setCellValue("");
	           R39cell3.setCellStyle(textStyle);
	       }

	       Cell R39cell4 = row.createCell(5);
	       if (record.getR39_moreThan6MonthTo12Months() != null) {
	           R39cell4.setCellValue(record.getR39_moreThan6MonthTo12Months().doubleValue());
	           R39cell4.setCellStyle(numberStyle);
	       } else {
	           R39cell4.setCellValue("");
	           R39cell4.setCellStyle(textStyle);
	       }

	       Cell R39cell5 = row.createCell(6);
	       if (record.getR39_moreThan12MonthTo3Years() != null) {
	           R39cell5.setCellValue(record.getR39_moreThan12MonthTo3Years().doubleValue());
	           R39cell5.setCellStyle(numberStyle);
	       } else {
	           R39cell5.setCellValue("");
	           R39cell5.setCellStyle(textStyle);
	       }

	       Cell R39cell6 = row.createCell(7);
	       if (record.getR39_moreThan3YearsTo5Years() != null) {
	           R39cell6.setCellValue(record.getR39_moreThan3YearsTo5Years().doubleValue());
	           R39cell6.setCellStyle(numberStyle);
	       } else {
	           R39cell6.setCellValue("");
	           R39cell6.setCellStyle(textStyle);
	       }

	       Cell R39cell7 = row.createCell(8);
	       if (record.getR39_moreThan5YearsTo10Years() != null) {
	           R39cell7.setCellValue(record.getR39_moreThan5YearsTo10Years().doubleValue());
	           R39cell7.setCellStyle(numberStyle);
	       } else {
	           R39cell7.setCellValue("");
	           R39cell7.setCellStyle(textStyle);
	       }

	       Cell R39cell8 = row.createCell(9);
	       if (record.getR39_moreThan10Years() != null) {
	           R39cell8.setCellValue(record.getR39_moreThan10Years().doubleValue());
	           R39cell8.setCellStyle(numberStyle);
	       } else {
	           R39cell8.setCellValue("");
	           R39cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(39);
	       Cell R40cell1 = row.createCell(2);
	       if (record.getR40_upTo1Month() != null) {
	           R40cell1.setCellValue(record.getR40_upTo1Month().doubleValue());
	           R40cell1.setCellStyle(numberStyle);
	       } else {
	           R40cell1.setCellValue("");
	           R40cell1.setCellStyle(textStyle);
	       }

	       Cell R40cell2 = row.createCell(3);
	       if (record.getR40_moreThan1MonthTo3Months() != null) {
	           R40cell2.setCellValue(record.getR40_moreThan1MonthTo3Months().doubleValue());
	           R40cell2.setCellStyle(numberStyle);
	       } else {
	           R40cell2.setCellValue("");
	           R40cell2.setCellStyle(textStyle);
	       }

	       Cell R40cell3 = row.createCell(4);
	       if (record.getR40_moreThan3MonthTo6Months() != null) {
	           R40cell3.setCellValue(record.getR40_moreThan3MonthTo6Months().doubleValue());
	           R40cell3.setCellStyle(numberStyle);
	       } else {
	           R40cell3.setCellValue("");
	           R40cell3.setCellStyle(textStyle);
	       }

	       Cell R40cell4 = row.createCell(5);
	       if (record.getR40_moreThan6MonthTo12Months() != null) {
	           R40cell4.setCellValue(record.getR40_moreThan6MonthTo12Months().doubleValue());
	           R40cell4.setCellStyle(numberStyle);
	       } else {
	           R40cell4.setCellValue("");
	           R40cell4.setCellStyle(textStyle);
	       }

	       Cell R40cell5 = row.createCell(6);
	       if (record.getR40_moreThan12MonthTo3Years() != null) {
	           R40cell5.setCellValue(record.getR40_moreThan12MonthTo3Years().doubleValue());
	           R40cell5.setCellStyle(numberStyle);
	       } else {
	           R40cell5.setCellValue("");
	           R40cell5.setCellStyle(textStyle);
	       }

	       Cell R40cell6 = row.createCell(7);
	       if (record.getR40_moreThan3YearsTo5Years() != null) {
	           R40cell6.setCellValue(record.getR40_moreThan3YearsTo5Years().doubleValue());
	           R40cell6.setCellStyle(numberStyle);
	       } else {
	           R40cell6.setCellValue("");
	           R40cell6.setCellStyle(textStyle);
	       }

	       Cell R40cell7 = row.createCell(8);
	       if (record.getR40_moreThan5YearsTo10Years() != null) {
	           R40cell7.setCellValue(record.getR40_moreThan5YearsTo10Years().doubleValue());
	           R40cell7.setCellStyle(numberStyle);
	       } else {
	           R40cell7.setCellValue("");
	           R40cell7.setCellStyle(textStyle);
	       }

	       Cell R40cell8 = row.createCell(9);
	       if (record.getR40_moreThan10Years() != null) {
	           R40cell8.setCellValue(record.getR40_moreThan10Years().doubleValue());
	           R40cell8.setCellStyle(numberStyle);
	       } else {
	           R40cell8.setCellValue("");
	           R40cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(40);
	       Cell R41cell1 = row.createCell(2);
	       if (record.getR41_upTo1Month() != null) {
	           R41cell1.setCellValue(record.getR41_upTo1Month().doubleValue());
	           R41cell1.setCellStyle(numberStyle);
	       } else {
	           R41cell1.setCellValue("");
	           R41cell1.setCellStyle(textStyle);
	       }

	       Cell R41cell2 = row.createCell(3);
	       if (record.getR41_moreThan1MonthTo3Months() != null) {
	           R41cell2.setCellValue(record.getR41_moreThan1MonthTo3Months().doubleValue());
	           R41cell2.setCellStyle(numberStyle);
	       } else {
	           R41cell2.setCellValue("");
	           R41cell2.setCellStyle(textStyle);
	       }

	       Cell R41cell3 = row.createCell(4);
	       if (record.getR41_moreThan3MonthTo6Months() != null) {
	           R41cell3.setCellValue(record.getR41_moreThan3MonthTo6Months().doubleValue());
	           R41cell3.setCellStyle(numberStyle);
	       } else {
	           R41cell3.setCellValue("");
	           R41cell3.setCellStyle(textStyle);
	       }

	       Cell R41cell4 = row.createCell(5);
	       if (record.getR41_moreThan6MonthTo12Months() != null) {
	           R41cell4.setCellValue(record.getR41_moreThan6MonthTo12Months().doubleValue());
	           R41cell4.setCellStyle(numberStyle);
	       } else {
	           R41cell4.setCellValue("");
	           R41cell4.setCellStyle(textStyle);
	       }

	       Cell R41cell5 = row.createCell(6);
	       if (record.getR41_moreThan12MonthTo3Years() != null) {
	           R41cell5.setCellValue(record.getR41_moreThan12MonthTo3Years().doubleValue());
	           R41cell5.setCellStyle(numberStyle);
	       } else {
	           R41cell5.setCellValue("");
	           R41cell5.setCellStyle(textStyle);
	       }

	       Cell R41cell6 = row.createCell(7);
	       if (record.getR41_moreThan3YearsTo5Years() != null) {
	           R41cell6.setCellValue(record.getR41_moreThan3YearsTo5Years().doubleValue());
	           R41cell6.setCellStyle(numberStyle);
	       } else {
	           R41cell6.setCellValue("");
	           R41cell6.setCellStyle(textStyle);
	       }

	       Cell R41cell7 = row.createCell(8);
	       if (record.getR41_moreThan5YearsTo10Years() != null) {
	           R41cell7.setCellValue(record.getR41_moreThan5YearsTo10Years().doubleValue());
	           R41cell7.setCellStyle(numberStyle);
	       } else {
	           R41cell7.setCellValue("");
	           R41cell7.setCellStyle(textStyle);
	       }

	       Cell R41cell8 = row.createCell(9);
	       if (record.getR41_moreThan10Years() != null) {
	           R41cell8.setCellValue(record.getR41_moreThan10Years().doubleValue());
	           R41cell8.setCellStyle(numberStyle);
	       } else {
	           R41cell8.setCellValue("");
	           R41cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(41);
	       Cell R42cell1 = row.createCell(2);
	       if (record.getR42_upTo1Month() != null) {
	           R42cell1.setCellValue(record.getR42_upTo1Month().doubleValue());
	           R42cell1.setCellStyle(numberStyle);
	       } else {
	           R42cell1.setCellValue("");
	           R42cell1.setCellStyle(textStyle);
	       }

	       Cell R42cell2 = row.createCell(3);
	       if (record.getR42_moreThan1MonthTo3Months() != null) {
	           R42cell2.setCellValue(record.getR42_moreThan1MonthTo3Months().doubleValue());
	           R42cell2.setCellStyle(numberStyle);
	       } else {
	           R42cell2.setCellValue("");
	           R42cell2.setCellStyle(textStyle);
	       }

	       Cell R42cell3 = row.createCell(4);
	       if (record.getR42_moreThan3MonthTo6Months() != null) {
	           R42cell3.setCellValue(record.getR42_moreThan3MonthTo6Months().doubleValue());
	           R42cell3.setCellStyle(numberStyle);
	       } else {
	           R42cell3.setCellValue("");
	           R42cell3.setCellStyle(textStyle);
	       }

	       Cell R42cell4 = row.createCell(5);
	       if (record.getR42_moreThan6MonthTo12Months() != null) {
	           R42cell4.setCellValue(record.getR42_moreThan6MonthTo12Months().doubleValue());
	           R42cell4.setCellStyle(numberStyle);
	       } else {
	           R42cell4.setCellValue("");
	           R42cell4.setCellStyle(textStyle);
	       }

	       Cell R42cell5 = row.createCell(6);
	       if (record.getR42_moreThan12MonthTo3Years() != null) {
	           R42cell5.setCellValue(record.getR42_moreThan12MonthTo3Years().doubleValue());
	           R42cell5.setCellStyle(numberStyle);
	       } else {
	           R42cell5.setCellValue("");
	           R42cell5.setCellStyle(textStyle);
	       }

	       Cell R42cell6 = row.createCell(7);
	       if (record.getR42_moreThan3YearsTo5Years() != null) {
	           R42cell6.setCellValue(record.getR42_moreThan3YearsTo5Years().doubleValue());
	           R42cell6.setCellStyle(numberStyle);
	       } else {
	           R42cell6.setCellValue("");
	           R42cell6.setCellStyle(textStyle);
	       }

	       Cell R42cell7 = row.createCell(8);
	       if (record.getR42_moreThan5YearsTo10Years() != null) {
	           R42cell7.setCellValue(record.getR42_moreThan5YearsTo10Years().doubleValue());
	           R42cell7.setCellStyle(numberStyle);
	       } else {
	           R42cell7.setCellValue("");
	           R42cell7.setCellStyle(textStyle);
	       }

	       Cell R42cell8 = row.createCell(9);
	       if (record.getR42_moreThan10Years() != null) {
	           R42cell8.setCellValue(record.getR42_moreThan10Years().doubleValue());
	           R42cell8.setCellStyle(numberStyle);
	       } else {
	           R42cell8.setCellValue("");
	           R42cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(42);
	       Cell R43cell1 = row.createCell(2);
	       if (record.getR43_upTo1Month() != null) {
	           R43cell1.setCellValue(record.getR43_upTo1Month().doubleValue());
	           R43cell1.setCellStyle(numberStyle);
	       } else {
	           R43cell1.setCellValue("");
	           R43cell1.setCellStyle(textStyle);
	       }

	       Cell R43cell2 = row.createCell(3);
	       if (record.getR43_moreThan1MonthTo3Months() != null) {
	           R43cell2.setCellValue(record.getR43_moreThan1MonthTo3Months().doubleValue());
	           R43cell2.setCellStyle(numberStyle);
	       } else {
	           R43cell2.setCellValue("");
	           R43cell2.setCellStyle(textStyle);
	       }

	       Cell R43cell3 = row.createCell(4);
	       if (record.getR43_moreThan3MonthTo6Months() != null) {
	           R43cell3.setCellValue(record.getR43_moreThan3MonthTo6Months().doubleValue());
	           R43cell3.setCellStyle(numberStyle);
	       } else {
	           R43cell3.setCellValue("");
	           R43cell3.setCellStyle(textStyle);
	       }

	       Cell R43cell4 = row.createCell(5);
	       if (record.getR43_moreThan6MonthTo12Months() != null) {
	           R43cell4.setCellValue(record.getR43_moreThan6MonthTo12Months().doubleValue());
	           R43cell4.setCellStyle(numberStyle);
	       } else {
	           R43cell4.setCellValue("");
	           R43cell4.setCellStyle(textStyle);
	       }

	       Cell R43cell5 = row.createCell(6);
	       if (record.getR43_moreThan12MonthTo3Years() != null) {
	           R43cell5.setCellValue(record.getR43_moreThan12MonthTo3Years().doubleValue());
	           R43cell5.setCellStyle(numberStyle);
	       } else {
	           R43cell5.setCellValue("");
	           R43cell5.setCellStyle(textStyle);
	       }

	       Cell R43cell6 = row.createCell(7);
	       if (record.getR43_moreThan3YearsTo5Years() != null) {
	           R43cell6.setCellValue(record.getR43_moreThan3YearsTo5Years().doubleValue());
	           R43cell6.setCellStyle(numberStyle);
	       } else {
	           R43cell6.setCellValue("");
	           R43cell6.setCellStyle(textStyle);
	       }

	       Cell R43cell7 = row.createCell(8);
	       if (record.getR43_moreThan5YearsTo10Years() != null) {
	           R43cell7.setCellValue(record.getR43_moreThan5YearsTo10Years().doubleValue());
	           R43cell7.setCellStyle(numberStyle);
	       } else {
	           R43cell7.setCellValue("");
	           R43cell7.setCellStyle(textStyle);
	       }

	       Cell R43cell8 = row.createCell(9);
	       if (record.getR43_moreThan10Years() != null) {
	           R43cell8.setCellValue(record.getR43_moreThan10Years().doubleValue());
	           R43cell8.setCellStyle(numberStyle);
	       } else {
	           R43cell8.setCellValue("");
	           R43cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(43);
	       Cell R44cell1 = row.createCell(2);
	       if (record.getR44_upTo1Month() != null) {
	           R44cell1.setCellValue(record.getR44_upTo1Month().doubleValue());
	           R44cell1.setCellStyle(numberStyle);
	       } else {
	           R44cell1.setCellValue("");
	           R44cell1.setCellStyle(textStyle);
	       }

	       Cell R44cell2 = row.createCell(3);
	       if (record.getR44_moreThan1MonthTo3Months() != null) {
	           R44cell2.setCellValue(record.getR44_moreThan1MonthTo3Months().doubleValue());
	           R44cell2.setCellStyle(numberStyle);
	       } else {
	           R44cell2.setCellValue("");
	           R44cell2.setCellStyle(textStyle);
	       }

	       Cell R44cell3 = row.createCell(4);
	       if (record.getR44_moreThan3MonthTo6Months() != null) {
	           R44cell3.setCellValue(record.getR44_moreThan3MonthTo6Months().doubleValue());
	           R44cell3.setCellStyle(numberStyle);
	       } else {
	           R44cell3.setCellValue("");
	           R44cell3.setCellStyle(textStyle);
	       }

	       Cell R44cell4 = row.createCell(5);
	       if (record.getR44_moreThan6MonthTo12Months() != null) {
	           R44cell4.setCellValue(record.getR44_moreThan6MonthTo12Months().doubleValue());
	           R44cell4.setCellStyle(numberStyle);
	       } else {
	           R44cell4.setCellValue("");
	           R44cell4.setCellStyle(textStyle);
	       }

	       Cell R44cell5 = row.createCell(6);
	       if (record.getR44_moreThan12MonthTo3Years() != null) {
	           R44cell5.setCellValue(record.getR44_moreThan12MonthTo3Years().doubleValue());
	           R44cell5.setCellStyle(numberStyle);
	       } else {
	           R44cell5.setCellValue("");
	           R44cell5.setCellStyle(textStyle);
	       }

	       Cell R44cell6 = row.createCell(7);
	       if (record.getR44_moreThan3YearsTo5Years() != null) {
	           R44cell6.setCellValue(record.getR44_moreThan3YearsTo5Years().doubleValue());
	           R44cell6.setCellStyle(numberStyle);
	       } else {
	           R44cell6.setCellValue("");
	           R44cell6.setCellStyle(textStyle);
	       }

	       Cell R44cell7 = row.createCell(8);
	       if (record.getR44_moreThan5YearsTo10Years() != null) {
	           R44cell7.setCellValue(record.getR44_moreThan5YearsTo10Years().doubleValue());
	           R44cell7.setCellStyle(numberStyle);
	       } else {
	           R44cell7.setCellValue("");
	           R44cell7.setCellStyle(textStyle);
	       }

	       Cell R44cell8 = row.createCell(9);
	       if (record.getR44_moreThan10Years() != null) {
	           R44cell8.setCellValue(record.getR44_moreThan10Years().doubleValue());
	           R44cell8.setCellStyle(numberStyle);
	       } else {
	           R44cell8.setCellValue("");
	           R44cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(44);
	       Cell R45cell1 = row.createCell(2);
	       if (record.getR45_upTo1Month() != null) {
	           R45cell1.setCellValue(record.getR45_upTo1Month().doubleValue());
	           R45cell1.setCellStyle(numberStyle);
	       } else {
	           R45cell1.setCellValue("");
	           R45cell1.setCellStyle(textStyle);
	       }

	       Cell R45cell2 = row.createCell(3);
	       if (record.getR45_moreThan1MonthTo3Months() != null) {
	           R45cell2.setCellValue(record.getR45_moreThan1MonthTo3Months().doubleValue());
	           R45cell2.setCellStyle(numberStyle);
	       } else {
	           R45cell2.setCellValue("");
	           R45cell2.setCellStyle(textStyle);
	       }

	       Cell R45cell3 = row.createCell(4);
	       if (record.getR45_moreThan3MonthTo6Months() != null) {
	           R45cell3.setCellValue(record.getR45_moreThan3MonthTo6Months().doubleValue());
	           R45cell3.setCellStyle(numberStyle);
	       } else {
	           R45cell3.setCellValue("");
	           R45cell3.setCellStyle(textStyle);
	       }

	       Cell R45cell4 = row.createCell(5);
	       if (record.getR45_moreThan6MonthTo12Months() != null) {
	           R45cell4.setCellValue(record.getR45_moreThan6MonthTo12Months().doubleValue());
	           R45cell4.setCellStyle(numberStyle);
	       } else {
	           R45cell4.setCellValue("");
	           R45cell4.setCellStyle(textStyle);
	       }

	       Cell R45cell5 = row.createCell(6);
	       if (record.getR45_moreThan12MonthTo3Years() != null) {
	           R45cell5.setCellValue(record.getR45_moreThan12MonthTo3Years().doubleValue());
	           R45cell5.setCellStyle(numberStyle);
	       } else {
	           R45cell5.setCellValue("");
	           R45cell5.setCellStyle(textStyle);
	       }

	       Cell R45cell6 = row.createCell(7);
	       if (record.getR45_moreThan3YearsTo5Years() != null) {
	           R45cell6.setCellValue(record.getR45_moreThan3YearsTo5Years().doubleValue());
	           R45cell6.setCellStyle(numberStyle);
	       } else {
	           R45cell6.setCellValue("");
	           R45cell6.setCellStyle(textStyle);
	       }

	       Cell R45cell7 = row.createCell(8);
	       if (record.getR45_moreThan5YearsTo10Years() != null) {
	           R45cell7.setCellValue(record.getR45_moreThan5YearsTo10Years().doubleValue());
	           R45cell7.setCellStyle(numberStyle);
	       } else {
	           R45cell7.setCellValue("");
	           R45cell7.setCellStyle(textStyle);
	       }

	       Cell R45cell8 = row.createCell(9);
	       if (record.getR45_moreThan10Years() != null) {
	           R45cell8.setCellValue(record.getR45_moreThan10Years().doubleValue());
	           R45cell8.setCellStyle(numberStyle);
	       } else {
	           R45cell8.setCellValue("");
	           R45cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(45);
	       Cell R46cell1 = row.createCell(2);
	       if (record.getR46_upTo1Month() != null) {
	           R46cell1.setCellValue(record.getR46_upTo1Month().doubleValue());
	           R46cell1.setCellStyle(numberStyle);
	       } else {
	           R46cell1.setCellValue("");
	           R46cell1.setCellStyle(textStyle);
	       }

	       Cell R46cell2 = row.createCell(3);
	       if (record.getR46_moreThan1MonthTo3Months() != null) {
	           R46cell2.setCellValue(record.getR46_moreThan1MonthTo3Months().doubleValue());
	           R46cell2.setCellStyle(numberStyle);
	       } else {
	           R46cell2.setCellValue("");
	           R46cell2.setCellStyle(textStyle);
	       }

	       Cell R46cell3 = row.createCell(4);
	       if (record.getR46_moreThan3MonthTo6Months() != null) {
	           R46cell3.setCellValue(record.getR46_moreThan3MonthTo6Months().doubleValue());
	           R46cell3.setCellStyle(numberStyle);
	       } else {
	           R46cell3.setCellValue("");
	           R46cell3.setCellStyle(textStyle);
	       }

	       Cell R46cell4 = row.createCell(5);
	       if (record.getR46_moreThan6MonthTo12Months() != null) {
	           R46cell4.setCellValue(record.getR46_moreThan6MonthTo12Months().doubleValue());
	           R46cell4.setCellStyle(numberStyle);
	       } else {
	           R46cell4.setCellValue("");
	           R46cell4.setCellStyle(textStyle);
	       }

	       Cell R46cell5 = row.createCell(6);
	       if (record.getR46_moreThan12MonthTo3Years() != null) {
	           R46cell5.setCellValue(record.getR46_moreThan12MonthTo3Years().doubleValue());
	           R46cell5.setCellStyle(numberStyle);
	       } else {
	           R46cell5.setCellValue("");
	           R46cell5.setCellStyle(textStyle);
	       }

	       Cell R46cell6 = row.createCell(7);
	       if (record.getR46_moreThan3YearsTo5Years() != null) {
	           R46cell6.setCellValue(record.getR46_moreThan3YearsTo5Years().doubleValue());
	           R46cell6.setCellStyle(numberStyle);
	       } else {
	           R46cell6.setCellValue("");
	           R46cell6.setCellStyle(textStyle);
	       }

	       Cell R46cell7 = row.createCell(8);
	       if (record.getR46_moreThan5YearsTo10Years() != null) {
	           R46cell7.setCellValue(record.getR46_moreThan5YearsTo10Years().doubleValue());
	           R46cell7.setCellStyle(numberStyle);
	       } else {
	           R46cell7.setCellValue("");
	           R46cell7.setCellStyle(textStyle);
	       }

	       Cell R46cell8 = row.createCell(9);
	       if (record.getR46_moreThan10Years() != null) {
	           R46cell8.setCellValue(record.getR46_moreThan10Years().doubleValue());
	           R46cell8.setCellStyle(numberStyle);
	       } else {
	           R46cell8.setCellValue("");
	           R46cell8.setCellStyle(textStyle);
	       }
	       
	       
		    
	       row = sheet.getRow(47);
	       Cell R48cell9 = row.createCell(10);
	       if (record.getR48_nonRatioSensativeItems() != null) {
	           R48cell9.setCellValue(record.getR48_nonRatioSensativeItems().doubleValue());
	           R48cell9.setCellStyle(numberStyle);
	       } else {
	           R48cell9.setCellValue("");
	           R48cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(48);
	       Cell R49cell9 = row.createCell(10);
	       if (record.getR49_nonRatioSensativeItems() != null) {
	           R49cell9.setCellValue(record.getR49_nonRatioSensativeItems().doubleValue());
	           R49cell9.setCellStyle(numberStyle);
	       } else {
	           R49cell9.setCellValue("");
	           R49cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(49);
	       Cell R50cell9 = row.createCell(10);
	       if (record.getR50_nonRatioSensativeItems() != null) {
	           R50cell9.setCellValue(record.getR50_nonRatioSensativeItems().doubleValue());
	           R50cell9.setCellStyle(numberStyle);
	       } else {
	           R50cell9.setCellValue("");
	           R50cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(50);
	       Cell R51cell9 = row.createCell(10);
	       if (record.getR51_nonRatioSensativeItems() != null) {
	           R51cell9.setCellValue(record.getR51_nonRatioSensativeItems().doubleValue());
	           R51cell9.setCellStyle(numberStyle);
	       } else {
	           R51cell9.setCellValue("");
	           R51cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(51);
	       Cell R52cell9 = row.createCell(10);
	       if (record.getR52_nonRatioSensativeItems() != null) {
	           R52cell9.setCellValue(record.getR52_nonRatioSensativeItems().doubleValue());
	           R52cell9.setCellStyle(numberStyle);
	       } else {
	           R52cell9.setCellValue("");
	           R52cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(52);
	       Cell R53cell9 = row.createCell(10);
	       if (record.getR53_nonRatioSensativeItems() != null) {
	           R53cell9.setCellValue(record.getR53_nonRatioSensativeItems().doubleValue());
	           R53cell9.setCellStyle(numberStyle);
	       } else {
	           R53cell9.setCellValue("");
	           R53cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(53);
	       Cell R54cell9 = row.createCell(10);
	       if (record.getR54_nonRatioSensativeItems() != null) {
	           R54cell9.setCellValue(record.getR54_nonRatioSensativeItems().doubleValue());
	           R54cell9.setCellStyle(numberStyle);
	       } else {
	           R54cell9.setCellValue("");
	           R54cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(54);
	       Cell R55cell9 = row.createCell(10);
	       if (record.getR55_nonRatioSensativeItems() != null) {
	           R55cell9.setCellValue(record.getR55_nonRatioSensativeItems().doubleValue());
	           R55cell9.setCellStyle(numberStyle);
	       } else {
	           R55cell9.setCellValue("");
	           R55cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(55);
	       Cell R56cell9 = row.createCell(10);
	       if (record.getR56_nonRatioSensativeItems() != null) {
	           R56cell9.setCellValue(record.getR56_nonRatioSensativeItems().doubleValue());
	           R56cell9.setCellStyle(numberStyle);
	       } else {
	           R56cell9.setCellValue("");
	           R56cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(56);
	       Cell R57cell9 = row.createCell(10);
	       if (record.getR57_nonRatioSensativeItems() != null) {
	           R57cell9.setCellValue(record.getR57_nonRatioSensativeItems().doubleValue());
	           R57cell9.setCellStyle(numberStyle);
	       } else {
	           R57cell9.setCellValue("");
	           R57cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(57);
	       Cell R58cell9 = row.createCell(10);
	       if (record.getR58_nonRatioSensativeItems() != null) {
	           R58cell9.setCellValue(record.getR58_nonRatioSensativeItems().doubleValue());
	           R58cell9.setCellStyle(numberStyle);
	       } else {
	           R58cell9.setCellValue("");
	           R58cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(60);
	       Cell R61cell1 = row.createCell(2);
	       if (record.getR61_upTo1Month() != null) {
	           R61cell1.setCellValue(record.getR61_upTo1Month().doubleValue());
	           R61cell1.setCellStyle(numberStyle);
	       } else {
	           R61cell1.setCellValue("");
	           R61cell1.setCellStyle(textStyle);
	       }

	       Cell R61cell2 = row.createCell(3);
	       if (record.getR61_moreThan1MonthTo3Months() != null) {
	           R61cell2.setCellValue(record.getR61_moreThan1MonthTo3Months().doubleValue());
	           R61cell2.setCellStyle(numberStyle);
	       } else {
	           R61cell2.setCellValue("");
	           R61cell2.setCellStyle(textStyle);
	       }

	       Cell R61cell3 = row.createCell(4);
	       if (record.getR61_moreThan3MonthTo6Months() != null) {
	           R61cell3.setCellValue(record.getR61_moreThan3MonthTo6Months().doubleValue());
	           R61cell3.setCellStyle(numberStyle);
	       } else {
	           R61cell3.setCellValue("");
	           R61cell3.setCellStyle(textStyle);
	       }

	       Cell R61cell4 = row.createCell(5);
	       if (record.getR61_moreThan6MonthTo12Months() != null) {
	           R61cell4.setCellValue(record.getR61_moreThan6MonthTo12Months().doubleValue());
	           R61cell4.setCellStyle(numberStyle);
	       } else {
	           R61cell4.setCellValue("");
	           R61cell4.setCellStyle(textStyle);
	       }

	       Cell R61cell5 = row.createCell(6);
	       if (record.getR61_moreThan12MonthTo3Years() != null) {
	           R61cell5.setCellValue(record.getR61_moreThan12MonthTo3Years().doubleValue());
	           R61cell5.setCellStyle(numberStyle);
	       } else {
	           R61cell5.setCellValue("");
	           R61cell5.setCellStyle(textStyle);
	       }

	       Cell R61cell6 = row.createCell(7);
	       if (record.getR61_moreThan3YearsTo5Years() != null) {
	           R61cell6.setCellValue(record.getR61_moreThan3YearsTo5Years().doubleValue());
	           R61cell6.setCellStyle(numberStyle);
	       } else {
	           R61cell6.setCellValue("");
	           R61cell6.setCellStyle(textStyle);
	       }

	       Cell R61cell7 = row.createCell(8);
	       if (record.getR61_moreThan5YearsTo10Years() != null) {
	           R61cell7.setCellValue(record.getR61_moreThan5YearsTo10Years().doubleValue());
	           R61cell7.setCellStyle(numberStyle);
	       } else {
	           R61cell7.setCellValue("");
	           R61cell7.setCellStyle(textStyle);
	       }

	       Cell R61cell8 = row.createCell(9);
	       if (record.getR61_moreThan10Years() != null) {
	           R61cell8.setCellValue(record.getR61_moreThan10Years().doubleValue());
	           R61cell8.setCellStyle(numberStyle);
	       } else {
	           R61cell8.setCellValue("");
	           R61cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(61);
	       Cell R62cell1 = row.createCell(2);
	       if (record.getR62_upTo1Month() != null) {
	           R62cell1.setCellValue(record.getR62_upTo1Month().doubleValue());
	           R62cell1.setCellStyle(numberStyle);
	       } else {
	           R62cell1.setCellValue("");
	           R62cell1.setCellStyle(textStyle);
	       }

	       Cell R62cell2 = row.createCell(3);
	       if (record.getR62_moreThan1MonthTo3Months() != null) {
	           R62cell2.setCellValue(record.getR62_moreThan1MonthTo3Months().doubleValue());
	           R62cell2.setCellStyle(numberStyle);
	       } else {
	           R62cell2.setCellValue("");
	           R62cell2.setCellStyle(textStyle);
	       }

	       Cell R62cell3 = row.createCell(4);
	       if (record.getR62_moreThan3MonthTo6Months() != null) {
	           R62cell3.setCellValue(record.getR62_moreThan3MonthTo6Months().doubleValue());
	           R62cell3.setCellStyle(numberStyle);
	       } else {
	           R62cell3.setCellValue("");
	           R62cell3.setCellStyle(textStyle);
	       }

	       Cell R62cell4 = row.createCell(5);
	       if (record.getR62_moreThan6MonthTo12Months() != null) {
	           R62cell4.setCellValue(record.getR62_moreThan6MonthTo12Months().doubleValue());
	           R62cell4.setCellStyle(numberStyle);
	       } else {
	           R62cell4.setCellValue("");
	           R62cell4.setCellStyle(textStyle);
	       }

	       Cell R62cell5 = row.createCell(6);
	       if (record.getR62_moreThan12MonthTo3Years() != null) {
	           R62cell5.setCellValue(record.getR62_moreThan12MonthTo3Years().doubleValue());
	           R62cell5.setCellStyle(numberStyle);
	       } else {
	           R62cell5.setCellValue("");
	           R62cell5.setCellStyle(textStyle);
	       }

	       Cell R62cell6 = row.createCell(7);
	       if (record.getR62_moreThan3YearsTo5Years() != null) {
	           R62cell6.setCellValue(record.getR62_moreThan3YearsTo5Years().doubleValue());
	           R62cell6.setCellStyle(numberStyle);
	       } else {
	           R62cell6.setCellValue("");
	           R62cell6.setCellStyle(textStyle);
	       }

	       Cell R62cell7 = row.createCell(8);
	       if (record.getR62_moreThan5YearsTo10Years() != null) {
	           R62cell7.setCellValue(record.getR62_moreThan5YearsTo10Years().doubleValue());
	           R62cell7.setCellStyle(numberStyle);
	       } else {
	           R62cell7.setCellValue("");
	           R62cell7.setCellStyle(textStyle);
	       }

	       Cell R62cell8 = row.createCell(9);
	       if (record.getR62_moreThan10Years() != null) {
	           R62cell8.setCellValue(record.getR62_moreThan10Years().doubleValue());
	           R62cell8.setCellStyle(numberStyle);
	       } else {
	           R62cell8.setCellValue("");
	           R62cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(62);
	       Cell R63cell1 = row.createCell(2);
	       if (record.getR63_upTo1Month() != null) {
	           R63cell1.setCellValue(record.getR63_upTo1Month().doubleValue());
	           R63cell1.setCellStyle(numberStyle);
	       } else {
	           R63cell1.setCellValue("");
	           R63cell1.setCellStyle(textStyle);
	       }

	       Cell R63cell2 = row.createCell(3);
	       if (record.getR63_moreThan1MonthTo3Months() != null) {
	           R63cell2.setCellValue(record.getR63_moreThan1MonthTo3Months().doubleValue());
	           R63cell2.setCellStyle(numberStyle);
	       } else {
	           R63cell2.setCellValue("");
	           R63cell2.setCellStyle(textStyle);
	       }

	       Cell R63cell3 = row.createCell(4);
	       if (record.getR63_moreThan3MonthTo6Months() != null) {
	           R63cell3.setCellValue(record.getR63_moreThan3MonthTo6Months().doubleValue());
	           R63cell3.setCellStyle(numberStyle);
	       } else {
	           R63cell3.setCellValue("");
	           R63cell3.setCellStyle(textStyle);
	       }

	       Cell R63cell4 = row.createCell(5);
	       if (record.getR63_moreThan6MonthTo12Months() != null) {
	           R63cell4.setCellValue(record.getR63_moreThan6MonthTo12Months().doubleValue());
	           R63cell4.setCellStyle(numberStyle);
	       } else {
	           R63cell4.setCellValue("");
	           R63cell4.setCellStyle(textStyle);
	       }

	       Cell R63cell5 = row.createCell(6);
	       if (record.getR63_moreThan12MonthTo3Years() != null) {
	           R63cell5.setCellValue(record.getR63_moreThan12MonthTo3Years().doubleValue());
	           R63cell5.setCellStyle(numberStyle);
	       } else {
	           R63cell5.setCellValue("");
	           R63cell5.setCellStyle(textStyle);
	       }

	       Cell R63cell6 = row.createCell(7);
	       if (record.getR63_moreThan3YearsTo5Years() != null) {
	           R63cell6.setCellValue(record.getR63_moreThan3YearsTo5Years().doubleValue());
	           R63cell6.setCellStyle(numberStyle);
	       } else {
	           R63cell6.setCellValue("");
	           R63cell6.setCellStyle(textStyle);
	       }

	       Cell R63cell7 = row.createCell(8);
	       if (record.getR63_moreThan5YearsTo10Years() != null) {
	           R63cell7.setCellValue(record.getR63_moreThan5YearsTo10Years().doubleValue());
	           R63cell7.setCellStyle(numberStyle);
	       } else {
	           R63cell7.setCellValue("");
	           R63cell7.setCellStyle(textStyle);
	       }

	       Cell R63cell8 = row.createCell(9);
	       if (record.getR63_moreThan10Years() != null) {
	           R63cell8.setCellValue(record.getR63_moreThan10Years().doubleValue());
	           R63cell8.setCellStyle(numberStyle);
	       } else {
	           R63cell8.setCellValue("");
	           R63cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(63);
	       Cell R64cell1 = row.createCell(2);
	       if (record.getR64_upTo1Month() != null) {
	           R64cell1.setCellValue(record.getR64_upTo1Month().doubleValue());
	           R64cell1.setCellStyle(numberStyle);
	       } else {
	           R64cell1.setCellValue("");
	           R64cell1.setCellStyle(textStyle);
	       }

	       Cell R64cell2 = row.createCell(3);
	       if (record.getR64_moreThan1MonthTo3Months() != null) {
	           R64cell2.setCellValue(record.getR64_moreThan1MonthTo3Months().doubleValue());
	           R64cell2.setCellStyle(numberStyle);
	       } else {
	           R64cell2.setCellValue("");
	           R64cell2.setCellStyle(textStyle);
	       }

	       Cell R64cell3 = row.createCell(4);
	       if (record.getR64_moreThan3MonthTo6Months() != null) {
	           R64cell3.setCellValue(record.getR64_moreThan3MonthTo6Months().doubleValue());
	           R64cell3.setCellStyle(numberStyle);
	       } else {
	           R64cell3.setCellValue("");
	           R64cell3.setCellStyle(textStyle);
	       }

	       Cell R64cell4 = row.createCell(5);
	       if (record.getR64_moreThan6MonthTo12Months() != null) {
	           R64cell4.setCellValue(record.getR64_moreThan6MonthTo12Months().doubleValue());
	           R64cell4.setCellStyle(numberStyle);
	       } else {
	           R64cell4.setCellValue("");
	           R64cell4.setCellStyle(textStyle);
	       }

	       Cell R64cell5 = row.createCell(6);
	       if (record.getR64_moreThan12MonthTo3Years() != null) {
	           R64cell5.setCellValue(record.getR64_moreThan12MonthTo3Years().doubleValue());
	           R64cell5.setCellStyle(numberStyle);
	       } else {
	           R64cell5.setCellValue("");
	           R64cell5.setCellStyle(textStyle);
	       }

	       Cell R64cell6 = row.createCell(7);
	       if (record.getR64_moreThan3YearsTo5Years() != null) {
	           R64cell6.setCellValue(record.getR64_moreThan3YearsTo5Years().doubleValue());
	           R64cell6.setCellStyle(numberStyle);
	       } else {
	           R64cell6.setCellValue("");
	           R64cell6.setCellStyle(textStyle);
	       }

	       Cell R64cell7 = row.createCell(8);
	       if (record.getR64_moreThan5YearsTo10Years() != null) {
	           R64cell7.setCellValue(record.getR64_moreThan5YearsTo10Years().doubleValue());
	           R64cell7.setCellStyle(numberStyle);
	       } else {
	           R64cell7.setCellValue("");
	           R64cell7.setCellStyle(textStyle);
	       }

	       Cell R64cell8 = row.createCell(9);
	       if (record.getR64_moreThan10Years() != null) {
	           R64cell8.setCellValue(record.getR64_moreThan10Years().doubleValue());
	           R64cell8.setCellStyle(numberStyle);
	       } else {
	           R64cell8.setCellValue("");
	           R64cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(64);
	       Cell R65cell1 = row.createCell(2);
	       if (record.getR65_upTo1Month() != null) {
	           R65cell1.setCellValue(record.getR65_upTo1Month().doubleValue());
	           R65cell1.setCellStyle(numberStyle);
	       } else {
	           R65cell1.setCellValue("");
	           R65cell1.setCellStyle(textStyle);
	       }

	       Cell R65cell2 = row.createCell(3);
	       if (record.getR65_moreThan1MonthTo3Months() != null) {
	           R65cell2.setCellValue(record.getR65_moreThan1MonthTo3Months().doubleValue());
	           R65cell2.setCellStyle(numberStyle);
	       } else {
	           R65cell2.setCellValue("");
	           R65cell2.setCellStyle(textStyle);
	       }

	       Cell R65cell3 = row.createCell(4);
	       if (record.getR65_moreThan3MonthTo6Months() != null) {
	           R65cell3.setCellValue(record.getR65_moreThan3MonthTo6Months().doubleValue());
	           R65cell3.setCellStyle(numberStyle);
	       } else {
	           R65cell3.setCellValue("");
	           R65cell3.setCellStyle(textStyle);
	       }

	       Cell R65cell4 = row.createCell(5);
	       if (record.getR65_moreThan6MonthTo12Months() != null) {
	           R65cell4.setCellValue(record.getR65_moreThan6MonthTo12Months().doubleValue());
	           R65cell4.setCellStyle(numberStyle);
	       } else {
	           R65cell4.setCellValue("");
	           R65cell4.setCellStyle(textStyle);
	       }

	       Cell R65cell5 = row.createCell(6);
	       if (record.getR65_moreThan12MonthTo3Years() != null) {
	           R65cell5.setCellValue(record.getR65_moreThan12MonthTo3Years().doubleValue());
	           R65cell5.setCellStyle(numberStyle);
	       } else {
	           R65cell5.setCellValue("");
	           R65cell5.setCellStyle(textStyle);
	       }

	       Cell R65cell6 = row.createCell(7);
	       if (record.getR65_moreThan3YearsTo5Years() != null) {
	           R65cell6.setCellValue(record.getR65_moreThan3YearsTo5Years().doubleValue());
	           R65cell6.setCellStyle(numberStyle);
	       } else {
	           R65cell6.setCellValue("");
	           R65cell6.setCellStyle(textStyle);
	       }

	       Cell R65cell7 = row.createCell(8);
	       if (record.getR65_moreThan5YearsTo10Years() != null) {
	           R65cell7.setCellValue(record.getR65_moreThan5YearsTo10Years().doubleValue());
	           R65cell7.setCellStyle(numberStyle);
	       } else {
	           R65cell7.setCellValue("");
	           R65cell7.setCellStyle(textStyle);
	       }

	       Cell R65cell8 = row.createCell(9);
	       if (record.getR65_moreThan10Years() != null) {
	           R65cell8.setCellValue(record.getR65_moreThan10Years().doubleValue());
	           R65cell8.setCellStyle(numberStyle);
	       } else {
	           R65cell8.setCellValue("");
	           R65cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(65);
	       Cell R66cell1 = row.createCell(2);
	       if (record.getR66_upTo1Month() != null) {
	           R66cell1.setCellValue(record.getR66_upTo1Month().doubleValue());
	           R66cell1.setCellStyle(numberStyle);
	       } else {
	           R66cell1.setCellValue("");
	           R66cell1.setCellStyle(textStyle);
	       }

	       Cell R66cell2 = row.createCell(3);
	       if (record.getR66_moreThan1MonthTo3Months() != null) {
	           R66cell2.setCellValue(record.getR66_moreThan1MonthTo3Months().doubleValue());
	           R66cell2.setCellStyle(numberStyle);
	       } else {
	           R66cell2.setCellValue("");
	           R66cell2.setCellStyle(textStyle);
	       }

	       Cell R66cell3 = row.createCell(4);
	       if (record.getR66_moreThan3MonthTo6Months() != null) {
	           R66cell3.setCellValue(record.getR66_moreThan3MonthTo6Months().doubleValue());
	           R66cell3.setCellStyle(numberStyle);
	       } else {
	           R66cell3.setCellValue("");
	           R66cell3.setCellStyle(textStyle);
	       }

	       Cell R66cell4 = row.createCell(5);
	       if (record.getR66_moreThan6MonthTo12Months() != null) {
	           R66cell4.setCellValue(record.getR66_moreThan6MonthTo12Months().doubleValue());
	           R66cell4.setCellStyle(numberStyle);
	       } else {
	           R66cell4.setCellValue("");
	           R66cell4.setCellStyle(textStyle);
	       }

	       Cell R66cell5 = row.createCell(6);
	       if (record.getR66_moreThan12MonthTo3Years() != null) {
	           R66cell5.setCellValue(record.getR66_moreThan12MonthTo3Years().doubleValue());
	           R66cell5.setCellStyle(numberStyle);
	       } else {
	           R66cell5.setCellValue("");
	           R66cell5.setCellStyle(textStyle);
	       }

	       Cell R66cell6 = row.createCell(7);
	       if (record.getR66_moreThan3YearsTo5Years() != null) {
	           R66cell6.setCellValue(record.getR66_moreThan3YearsTo5Years().doubleValue());
	           R66cell6.setCellStyle(numberStyle);
	       } else {
	           R66cell6.setCellValue("");
	           R66cell6.setCellStyle(textStyle);
	       }

	       Cell R66cell7 = row.createCell(8);
	       if (record.getR66_moreThan5YearsTo10Years() != null) {
	           R66cell7.setCellValue(record.getR66_moreThan5YearsTo10Years().doubleValue());
	           R66cell7.setCellStyle(numberStyle);
	       } else {
	           R66cell7.setCellValue("");
	           R66cell7.setCellStyle(textStyle);
	       }

	       Cell R66cell8 = row.createCell(9);
	       if (record.getR66_moreThan10Years() != null) {
	           R66cell8.setCellValue(record.getR66_moreThan10Years().doubleValue());
	           R66cell8.setCellStyle(numberStyle);
	       } else {
	           R66cell8.setCellValue("");
	           R66cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(66);
	       Cell R67cell1 = row.createCell(2);
	       if (record.getR67_upTo1Month() != null) {
	           R67cell1.setCellValue(record.getR67_upTo1Month().doubleValue());
	           R67cell1.setCellStyle(numberStyle);
	       } else {
	           R67cell1.setCellValue("");
	           R67cell1.setCellStyle(textStyle);
	       }

	       Cell R67cell2 = row.createCell(3);
	       if (record.getR67_moreThan1MonthTo3Months() != null) {
	           R67cell2.setCellValue(record.getR67_moreThan1MonthTo3Months().doubleValue());
	           R67cell2.setCellStyle(numberStyle);
	       } else {
	           R67cell2.setCellValue("");
	           R67cell2.setCellStyle(textStyle);
	       }

	       Cell R67cell3 = row.createCell(4);
	       if (record.getR67_moreThan3MonthTo6Months() != null) {
	           R67cell3.setCellValue(record.getR67_moreThan3MonthTo6Months().doubleValue());
	           R67cell3.setCellStyle(numberStyle);
	       } else {
	           R67cell3.setCellValue("");
	           R67cell3.setCellStyle(textStyle);
	       }

	       Cell R67cell4 = row.createCell(5);
	       if (record.getR67_moreThan6MonthTo12Months() != null) {
	           R67cell4.setCellValue(record.getR67_moreThan6MonthTo12Months().doubleValue());
	           R67cell4.setCellStyle(numberStyle);
	       } else {
	           R67cell4.setCellValue("");
	           R67cell4.setCellStyle(textStyle);
	       }

	       Cell R67cell5 = row.createCell(6);
	       if (record.getR67_moreThan12MonthTo3Years() != null) {
	           R67cell5.setCellValue(record.getR67_moreThan12MonthTo3Years().doubleValue());
	           R67cell5.setCellStyle(numberStyle);
	       } else {
	           R67cell5.setCellValue("");
	           R67cell5.setCellStyle(textStyle);
	       }

	       Cell R67cell6 = row.createCell(7);
	       if (record.getR67_moreThan3YearsTo5Years() != null) {
	           R67cell6.setCellValue(record.getR67_moreThan3YearsTo5Years().doubleValue());
	           R67cell6.setCellStyle(numberStyle);
	       } else {
	           R67cell6.setCellValue("");
	           R67cell6.setCellStyle(textStyle);
	       }

	       Cell R67cell7 = row.createCell(8);
	       if (record.getR67_moreThan5YearsTo10Years() != null) {
	           R67cell7.setCellValue(record.getR67_moreThan5YearsTo10Years().doubleValue());
	           R67cell7.setCellStyle(numberStyle);
	       } else {
	           R67cell7.setCellValue("");
	           R67cell7.setCellStyle(textStyle);
	       }

	       Cell R67cell8 = row.createCell(9);
	       if (record.getR67_moreThan10Years() != null) {
	           R67cell8.setCellValue(record.getR67_moreThan10Years().doubleValue());
	           R67cell8.setCellStyle(numberStyle);
	       } else {
	           R67cell8.setCellValue("");
	           R67cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(68);
	       Cell R69cell1 = row.createCell(2);
	       if (record.getR69_upTo1Month() != null) {
	           R69cell1.setCellValue(record.getR69_upTo1Month().doubleValue());
	           R69cell1.setCellStyle(numberStyle);
	       } else {
	           R69cell1.setCellValue("");
	           R69cell1.setCellStyle(textStyle);
	       }

	       Cell R69cell2 = row.createCell(3);
	       if (record.getR69_moreThan1MonthTo3Months() != null) {
	           R69cell2.setCellValue(record.getR69_moreThan1MonthTo3Months().doubleValue());
	           R69cell2.setCellStyle(numberStyle);
	       } else {
	           R69cell2.setCellValue("");
	           R69cell2.setCellStyle(textStyle);
	       }

	       Cell R69cell3 = row.createCell(4);
	       if (record.getR69_moreThan3MonthTo6Months() != null) {
	           R69cell3.setCellValue(record.getR69_moreThan3MonthTo6Months().doubleValue());
	           R69cell3.setCellStyle(numberStyle);
	       } else {
	           R69cell3.setCellValue("");
	           R69cell3.setCellStyle(textStyle);
	       }

	       Cell R69cell4 = row.createCell(5);
	       if (record.getR69_moreThan6MonthTo12Months() != null) {
	           R69cell4.setCellValue(record.getR69_moreThan6MonthTo12Months().doubleValue());
	           R69cell4.setCellStyle(numberStyle);
	       } else {
	           R69cell4.setCellValue("");
	           R69cell4.setCellStyle(textStyle);
	       }

	       Cell R69cell5 = row.createCell(6);
	       if (record.getR69_moreThan12MonthTo3Years() != null) {
	           R69cell5.setCellValue(record.getR69_moreThan12MonthTo3Years().doubleValue());
	           R69cell5.setCellStyle(numberStyle);
	       } else {
	           R69cell5.setCellValue("");
	           R69cell5.setCellStyle(textStyle);
	       }

	       Cell R69cell6 = row.createCell(7);
	       if (record.getR69_moreThan3YearsTo5Years() != null) {
	           R69cell6.setCellValue(record.getR69_moreThan3YearsTo5Years().doubleValue());
	           R69cell6.setCellStyle(numberStyle);
	       } else {
	           R69cell6.setCellValue("");
	           R69cell6.setCellStyle(textStyle);
	       }

	       Cell R69cell7 = row.createCell(8);
	       if (record.getR69_moreThan5YearsTo10Years() != null) {
	           R69cell7.setCellValue(record.getR69_moreThan5YearsTo10Years().doubleValue());
	           R69cell7.setCellStyle(numberStyle);
	       } else {
	           R69cell7.setCellValue("");
	           R69cell7.setCellStyle(textStyle);
	       }

	       Cell R69cell8 = row.createCell(9);
	       if (record.getR69_moreThan10Years() != null) {
	           R69cell8.setCellValue(record.getR69_moreThan10Years().doubleValue());
	           R69cell8.setCellStyle(numberStyle);
	       } else {
	           R69cell8.setCellValue("");
	           R69cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(69);
	       Cell R70cell1 = row.createCell(2);
	       if (record.getR70_upTo1Month() != null) {
	           R70cell1.setCellValue(record.getR70_upTo1Month().doubleValue());
	           R70cell1.setCellStyle(numberStyle);
	       } else {
	           R70cell1.setCellValue("");
	           R70cell1.setCellStyle(textStyle);
	       }

	       Cell R70cell2 = row.createCell(3);
	       if (record.getR70_moreThan1MonthTo3Months() != null) {
	           R70cell2.setCellValue(record.getR70_moreThan1MonthTo3Months().doubleValue());
	           R70cell2.setCellStyle(numberStyle);
	       } else {
	           R70cell2.setCellValue("");
	           R70cell2.setCellStyle(textStyle);
	       }

	       Cell R70cell3 = row.createCell(4);
	       if (record.getR70_moreThan3MonthTo6Months() != null) {
	           R70cell3.setCellValue(record.getR70_moreThan3MonthTo6Months().doubleValue());
	           R70cell3.setCellStyle(numberStyle);
	       } else {
	           R70cell3.setCellValue("");
	           R70cell3.setCellStyle(textStyle);
	       }

	       Cell R70cell4 = row.createCell(5);
	       if (record.getR70_moreThan6MonthTo12Months() != null) {
	           R70cell4.setCellValue(record.getR70_moreThan6MonthTo12Months().doubleValue());
	           R70cell4.setCellStyle(numberStyle);
	       } else {
	           R70cell4.setCellValue("");
	           R70cell4.setCellStyle(textStyle);
	       }

	       Cell R70cell5 = row.createCell(6);
	       if (record.getR70_moreThan12MonthTo3Years() != null) {
	           R70cell5.setCellValue(record.getR70_moreThan12MonthTo3Years().doubleValue());
	           R70cell5.setCellStyle(numberStyle);
	       } else {
	           R70cell5.setCellValue("");
	           R70cell5.setCellStyle(textStyle);
	       }

	       Cell R70cell6 = row.createCell(7);
	       if (record.getR70_moreThan3YearsTo5Years() != null) {
	           R70cell6.setCellValue(record.getR70_moreThan3YearsTo5Years().doubleValue());
	           R70cell6.setCellStyle(numberStyle);
	       } else {
	           R70cell6.setCellValue("");
	           R70cell6.setCellStyle(textStyle);
	       }

	       Cell R70cell7 = row.createCell(8);
	       if (record.getR70_moreThan5YearsTo10Years() != null) {
	           R70cell7.setCellValue(record.getR70_moreThan5YearsTo10Years().doubleValue());
	           R70cell7.setCellStyle(numberStyle);
	       } else {
	           R70cell7.setCellValue("");
	           R70cell7.setCellStyle(textStyle);
	       }

	       Cell R70cell8 = row.createCell(9);
	       if (record.getR70_moreThan10Years() != null) {
	           R70cell8.setCellValue(record.getR70_moreThan10Years().doubleValue());
	           R70cell8.setCellStyle(numberStyle);
	       } else {
	           R70cell8.setCellValue("");
	           R70cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(70);
	       Cell R71cell1 = row.createCell(2);
	       if (record.getR71_upTo1Month() != null) {
	           R71cell1.setCellValue(record.getR71_upTo1Month().doubleValue());
	           R71cell1.setCellStyle(numberStyle);
	       } else {
	           R71cell1.setCellValue("");
	           R71cell1.setCellStyle(textStyle);
	       }

	       Cell R71cell2 = row.createCell(3);
	       if (record.getR71_moreThan1MonthTo3Months() != null) {
	           R71cell2.setCellValue(record.getR71_moreThan1MonthTo3Months().doubleValue());
	           R71cell2.setCellStyle(numberStyle);
	       } else {
	           R71cell2.setCellValue("");
	           R71cell2.setCellStyle(textStyle);
	       }

	       Cell R71cell3 = row.createCell(4);
	       if (record.getR71_moreThan3MonthTo6Months() != null) {
	           R71cell3.setCellValue(record.getR71_moreThan3MonthTo6Months().doubleValue());
	           R71cell3.setCellStyle(numberStyle);
	       } else {
	           R71cell3.setCellValue("");
	           R71cell3.setCellStyle(textStyle);
	       }

	       Cell R71cell4 = row.createCell(5);
	       if (record.getR71_moreThan6MonthTo12Months() != null) {
	           R71cell4.setCellValue(record.getR71_moreThan6MonthTo12Months().doubleValue());
	           R71cell4.setCellStyle(numberStyle);
	       } else {
	           R71cell4.setCellValue("");
	           R71cell4.setCellStyle(textStyle);
	       }

	       Cell R71cell5 = row.createCell(6);
	       if (record.getR71_moreThan12MonthTo3Years() != null) {
	           R71cell5.setCellValue(record.getR71_moreThan12MonthTo3Years().doubleValue());
	           R71cell5.setCellStyle(numberStyle);
	       } else {
	           R71cell5.setCellValue("");
	           R71cell5.setCellStyle(textStyle);
	       }

	       Cell R71cell6 = row.createCell(7);
	       if (record.getR71_moreThan3YearsTo5Years() != null) {
	           R71cell6.setCellValue(record.getR71_moreThan3YearsTo5Years().doubleValue());
	           R71cell6.setCellStyle(numberStyle);
	       } else {
	           R71cell6.setCellValue("");
	           R71cell6.setCellStyle(textStyle);
	       }

	       Cell R71cell7 = row.createCell(8);
	       if (record.getR71_moreThan5YearsTo10Years() != null) {
	           R71cell7.setCellValue(record.getR71_moreThan5YearsTo10Years().doubleValue());
	           R71cell7.setCellStyle(numberStyle);
	       } else {
	           R71cell7.setCellValue("");
	           R71cell7.setCellStyle(textStyle);
	       }

	       Cell R71cell8 = row.createCell(9);
	       if (record.getR71_moreThan10Years() != null) {
	           R71cell8.setCellValue(record.getR71_moreThan10Years().doubleValue());
	           R71cell8.setCellStyle(numberStyle);
	       } else {
	           R71cell8.setCellValue("");
	           R71cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(71);
	       Cell R72cell1 = row.createCell(2);
	       if (record.getR72_upTo1Month() != null) {
	           R72cell1.setCellValue(record.getR72_upTo1Month().doubleValue());
	           R72cell1.setCellStyle(numberStyle);
	       } else {
	           R72cell1.setCellValue("");
	           R72cell1.setCellStyle(textStyle);
	       }

	       Cell R72cell2 = row.createCell(3);
	       if (record.getR72_moreThan1MonthTo3Months() != null) {
	           R72cell2.setCellValue(record.getR72_moreThan1MonthTo3Months().doubleValue());
	           R72cell2.setCellStyle(numberStyle);
	       } else {
	           R72cell2.setCellValue("");
	           R72cell2.setCellStyle(textStyle);
	       }

	       Cell R72cell3 = row.createCell(4);
	       if (record.getR72_moreThan3MonthTo6Months() != null) {
	           R72cell3.setCellValue(record.getR72_moreThan3MonthTo6Months().doubleValue());
	           R72cell3.setCellStyle(numberStyle);
	       } else {
	           R72cell3.setCellValue("");
	           R72cell3.setCellStyle(textStyle);
	       }

	       Cell R72cell4 = row.createCell(5);
	       if (record.getR72_moreThan6MonthTo12Months() != null) {
	           R72cell4.setCellValue(record.getR72_moreThan6MonthTo12Months().doubleValue());
	           R72cell4.setCellStyle(numberStyle);
	       } else {
	           R72cell4.setCellValue("");
	           R72cell4.setCellStyle(textStyle);
	       }

	       Cell R72cell5 = row.createCell(6);
	       if (record.getR72_moreThan12MonthTo3Years() != null) {
	           R72cell5.setCellValue(record.getR72_moreThan12MonthTo3Years().doubleValue());
	           R72cell5.setCellStyle(numberStyle);
	       } else {
	           R72cell5.setCellValue("");
	           R72cell5.setCellStyle(textStyle);
	       }

	       Cell R72cell6 = row.createCell(7);
	       if (record.getR72_moreThan3YearsTo5Years() != null) {
	           R72cell6.setCellValue(record.getR72_moreThan3YearsTo5Years().doubleValue());
	           R72cell6.setCellStyle(numberStyle);
	       } else {
	           R72cell6.setCellValue("");
	           R72cell6.setCellStyle(textStyle);
	       }

	       Cell R72cell7 = row.createCell(8);
	       if (record.getR72_moreThan5YearsTo10Years() != null) {
	           R72cell7.setCellValue(record.getR72_moreThan5YearsTo10Years().doubleValue());
	           R72cell7.setCellStyle(numberStyle);
	       } else {
	           R72cell7.setCellValue("");
	           R72cell7.setCellStyle(textStyle);
	       }

	       Cell R72cell8 = row.createCell(9);
	       if (record.getR72_moreThan10Years() != null) {
	           R72cell8.setCellValue(record.getR72_moreThan10Years().doubleValue());
	           R72cell8.setCellStyle(numberStyle);
	       } else {
	           R72cell8.setCellValue("");
	           R72cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(72);
	       Cell R73cell1 = row.createCell(2);
	       if (record.getR73_upTo1Month() != null) {
	           R73cell1.setCellValue(record.getR73_upTo1Month().doubleValue());
	           R73cell1.setCellStyle(numberStyle);
	       } else {
	           R73cell1.setCellValue("");
	           R73cell1.setCellStyle(textStyle);
	       }

	       Cell R73cell2 = row.createCell(3);
	       if (record.getR73_moreThan1MonthTo3Months() != null) {
	           R73cell2.setCellValue(record.getR73_moreThan1MonthTo3Months().doubleValue());
	           R73cell2.setCellStyle(numberStyle);
	       } else {
	           R73cell2.setCellValue("");
	           R73cell2.setCellStyle(textStyle);
	       }

	       Cell R73cell3 = row.createCell(4);
	       if (record.getR73_moreThan3MonthTo6Months() != null) {
	           R73cell3.setCellValue(record.getR73_moreThan3MonthTo6Months().doubleValue());
	           R73cell3.setCellStyle(numberStyle);
	       } else {
	           R73cell3.setCellValue("");
	           R73cell3.setCellStyle(textStyle);
	       }

	       Cell R73cell4 = row.createCell(5);
	       if (record.getR73_moreThan6MonthTo12Months() != null) {
	           R73cell4.setCellValue(record.getR73_moreThan6MonthTo12Months().doubleValue());
	           R73cell4.setCellStyle(numberStyle);
	       } else {
	           R73cell4.setCellValue("");
	           R73cell4.setCellStyle(textStyle);
	       }

	       Cell R73cell5 = row.createCell(6);
	       if (record.getR73_moreThan12MonthTo3Years() != null) {
	           R73cell5.setCellValue(record.getR73_moreThan12MonthTo3Years().doubleValue());
	           R73cell5.setCellStyle(numberStyle);
	       } else {
	           R73cell5.setCellValue("");
	           R73cell5.setCellStyle(textStyle);
	       }

	       Cell R73cell6 = row.createCell(7);
	       if (record.getR73_moreThan3YearsTo5Years() != null) {
	           R73cell6.setCellValue(record.getR73_moreThan3YearsTo5Years().doubleValue());
	           R73cell6.setCellStyle(numberStyle);
	       } else {
	           R73cell6.setCellValue("");
	           R73cell6.setCellStyle(textStyle);
	       }

	       Cell R73cell7 = row.createCell(8);
	       if (record.getR73_moreThan5YearsTo10Years() != null) {
	           R73cell7.setCellValue(record.getR73_moreThan5YearsTo10Years().doubleValue());
	           R73cell7.setCellStyle(numberStyle);
	       } else {
	           R73cell7.setCellValue("");
	           R73cell7.setCellStyle(textStyle);
	       }

	       Cell R73cell8 = row.createCell(9);
	       if (record.getR73_moreThan10Years() != null) {
	           R73cell8.setCellValue(record.getR73_moreThan10Years().doubleValue());
	           R73cell8.setCellStyle(numberStyle);
	       } else {
	           R73cell8.setCellValue("");
	           R73cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(73);
	       Cell R74cell1 = row.createCell(2);
	       if (record.getR74_upTo1Month() != null) {
	           R74cell1.setCellValue(record.getR74_upTo1Month().doubleValue());
	           R74cell1.setCellStyle(numberStyle);
	       } else {
	           R74cell1.setCellValue("");
	           R74cell1.setCellStyle(textStyle);
	       }

	       Cell R74cell2 = row.createCell(3);
	       if (record.getR74_moreThan1MonthTo3Months() != null) {
	           R74cell2.setCellValue(record.getR74_moreThan1MonthTo3Months().doubleValue());
	           R74cell2.setCellStyle(numberStyle);
	       } else {
	           R74cell2.setCellValue("");
	           R74cell2.setCellStyle(textStyle);
	       }

	       Cell R74cell3 = row.createCell(4);
	       if (record.getR74_moreThan3MonthTo6Months() != null) {
	           R74cell3.setCellValue(record.getR74_moreThan3MonthTo6Months().doubleValue());
	           R74cell3.setCellStyle(numberStyle);
	       } else {
	           R74cell3.setCellValue("");
	           R74cell3.setCellStyle(textStyle);
	       }

	       Cell R74cell4 = row.createCell(5);
	       if (record.getR74_moreThan6MonthTo12Months() != null) {
	           R74cell4.setCellValue(record.getR74_moreThan6MonthTo12Months().doubleValue());
	           R74cell4.setCellStyle(numberStyle);
	       } else {
	           R74cell4.setCellValue("");
	           R74cell4.setCellStyle(textStyle);
	       }

	       Cell R74cell5 = row.createCell(6);
	       if (record.getR74_moreThan12MonthTo3Years() != null) {
	           R74cell5.setCellValue(record.getR74_moreThan12MonthTo3Years().doubleValue());
	           R74cell5.setCellStyle(numberStyle);
	       } else {
	           R74cell5.setCellValue("");
	           R74cell5.setCellStyle(textStyle);
	       }

	       Cell R74cell6 = row.createCell(7);
	       if (record.getR74_moreThan3YearsTo5Years() != null) {
	           R74cell6.setCellValue(record.getR74_moreThan3YearsTo5Years().doubleValue());
	           R74cell6.setCellStyle(numberStyle);
	       } else {
	           R74cell6.setCellValue("");
	           R74cell6.setCellStyle(textStyle);
	       }

	       Cell R74cell7 = row.createCell(8);
	       if (record.getR74_moreThan5YearsTo10Years() != null) {
	           R74cell7.setCellValue(record.getR74_moreThan5YearsTo10Years().doubleValue());
	           R74cell7.setCellStyle(numberStyle);
	       } else {
	           R74cell7.setCellValue("");
	           R74cell7.setCellStyle(textStyle);
	       }

	       Cell R74cell8 = row.createCell(9);
	       if (record.getR74_moreThan10Years() != null) {
	           R74cell8.setCellValue(record.getR74_moreThan10Years().doubleValue());
	           R74cell8.setCellStyle(numberStyle);
	       } else {
	           R74cell8.setCellValue("");
	           R74cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(74);
	       Cell R75cell1 = row.createCell(2);
	       if (record.getR75_upTo1Month() != null) {
	           R75cell1.setCellValue(record.getR75_upTo1Month().doubleValue());
	           R75cell1.setCellStyle(numberStyle);
	       } else {
	           R75cell1.setCellValue("");
	           R75cell1.setCellStyle(textStyle);
	       }

	       Cell R75cell2 = row.createCell(3);
	       if (record.getR75_moreThan1MonthTo3Months() != null) {
	           R75cell2.setCellValue(record.getR75_moreThan1MonthTo3Months().doubleValue());
	           R75cell2.setCellStyle(numberStyle);
	       } else {
	           R75cell2.setCellValue("");
	           R75cell2.setCellStyle(textStyle);
	       }

	       Cell R75cell3 = row.createCell(4);
	       if (record.getR75_moreThan3MonthTo6Months() != null) {
	           R75cell3.setCellValue(record.getR75_moreThan3MonthTo6Months().doubleValue());
	           R75cell3.setCellStyle(numberStyle);
	       } else {
	           R75cell3.setCellValue("");
	           R75cell3.setCellStyle(textStyle);
	       }

	       Cell R75cell4 = row.createCell(5);
	       if (record.getR75_moreThan6MonthTo12Months() != null) {
	           R75cell4.setCellValue(record.getR75_moreThan6MonthTo12Months().doubleValue());
	           R75cell4.setCellStyle(numberStyle);
	       } else {
	           R75cell4.setCellValue("");
	           R75cell4.setCellStyle(textStyle);
	       }

	       Cell R75cell5 = row.createCell(6);
	       if (record.getR75_moreThan12MonthTo3Years() != null) {
	           R75cell5.setCellValue(record.getR75_moreThan12MonthTo3Years().doubleValue());
	           R75cell5.setCellStyle(numberStyle);
	       } else {
	           R75cell5.setCellValue("");
	           R75cell5.setCellStyle(textStyle);
	       }

	       Cell R75cell6 = row.createCell(7);
	       if (record.getR75_moreThan3YearsTo5Years() != null) {
	           R75cell6.setCellValue(record.getR75_moreThan3YearsTo5Years().doubleValue());
	           R75cell6.setCellStyle(numberStyle);
	       } else {
	           R75cell6.setCellValue("");
	           R75cell6.setCellStyle(textStyle);
	       }

	       Cell R75cell7 = row.createCell(8);
	       if (record.getR75_moreThan5YearsTo10Years() != null) {
	           R75cell7.setCellValue(record.getR75_moreThan5YearsTo10Years().doubleValue());
	           R75cell7.setCellStyle(numberStyle);
	       } else {
	           R75cell7.setCellValue("");
	           R75cell7.setCellStyle(textStyle);
	       }

	       Cell R75cell8 = row.createCell(9);
	       if (record.getR75_moreThan10Years() != null) {
	           R75cell8.setCellValue(record.getR75_moreThan10Years().doubleValue());
	           R75cell8.setCellStyle(numberStyle);
	       } else {
	           R75cell8.setCellValue("");
	           R75cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(76);
	       Cell R77cell1 = row.createCell(2);
	       if (record.getR77_upTo1Month() != null) {
	           R77cell1.setCellValue(record.getR77_upTo1Month().doubleValue());
	           R77cell1.setCellStyle(numberStyle);
	       } else {
	           R77cell1.setCellValue("");
	           R77cell1.setCellStyle(textStyle);
	       }

	       Cell R77cell2 = row.createCell(3);
	       if (record.getR77_moreThan1MonthTo3Months() != null) {
	           R77cell2.setCellValue(record.getR77_moreThan1MonthTo3Months().doubleValue());
	           R77cell2.setCellStyle(numberStyle);
	       } else {
	           R77cell2.setCellValue("");
	           R77cell2.setCellStyle(textStyle);
	       }

	       Cell R77cell3 = row.createCell(4);
	       if (record.getR77_moreThan3MonthTo6Months() != null) {
	           R77cell3.setCellValue(record.getR77_moreThan3MonthTo6Months().doubleValue());
	           R77cell3.setCellStyle(numberStyle);
	       } else {
	           R77cell3.setCellValue("");
	           R77cell3.setCellStyle(textStyle);
	       }

	       Cell R77cell4 = row.createCell(5);
	       if (record.getR77_moreThan6MonthTo12Months() != null) {
	           R77cell4.setCellValue(record.getR77_moreThan6MonthTo12Months().doubleValue());
	           R77cell4.setCellStyle(numberStyle);
	       } else {
	           R77cell4.setCellValue("");
	           R77cell4.setCellStyle(textStyle);
	       }

	       Cell R77cell5 = row.createCell(6);
	       if (record.getR77_moreThan12MonthTo3Years() != null) {
	           R77cell5.setCellValue(record.getR77_moreThan12MonthTo3Years().doubleValue());
	           R77cell5.setCellStyle(numberStyle);
	       } else {
	           R77cell5.setCellValue("");
	           R77cell5.setCellStyle(textStyle);
	       }

	       Cell R77cell6 = row.createCell(7);
	       if (record.getR77_moreThan3YearsTo5Years() != null) {
	           R77cell6.setCellValue(record.getR77_moreThan3YearsTo5Years().doubleValue());
	           R77cell6.setCellStyle(numberStyle);
	       } else {
	           R77cell6.setCellValue("");
	           R77cell6.setCellStyle(textStyle);
	       }

	       Cell R77cell7 = row.createCell(8);
	       if (record.getR77_moreThan5YearsTo10Years() != null) {
	           R77cell7.setCellValue(record.getR77_moreThan5YearsTo10Years().doubleValue());
	           R77cell7.setCellStyle(numberStyle);
	       } else {
	           R77cell7.setCellValue("");
	           R77cell7.setCellStyle(textStyle);
	       }

	       Cell R77cell8 = row.createCell(9);
	       if (record.getR77_moreThan10Years() != null) {
	           R77cell8.setCellValue(record.getR77_moreThan10Years().doubleValue());
	           R77cell8.setCellStyle(numberStyle);
	       } else {
	           R77cell8.setCellValue("");
	           R77cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(77);
	       Cell R78cell1 = row.createCell(2);
	       if (record.getR78_upTo1Month() != null) {
	           R78cell1.setCellValue(record.getR78_upTo1Month().doubleValue());
	           R78cell1.setCellStyle(numberStyle);
	       } else {
	           R78cell1.setCellValue("");
	           R78cell1.setCellStyle(textStyle);
	       }

	       Cell R78cell2 = row.createCell(3);
	       if (record.getR78_moreThan1MonthTo3Months() != null) {
	           R78cell2.setCellValue(record.getR78_moreThan1MonthTo3Months().doubleValue());
	           R78cell2.setCellStyle(numberStyle);
	       } else {
	           R78cell2.setCellValue("");
	           R78cell2.setCellStyle(textStyle);
	       }

	       Cell R78cell3 = row.createCell(4);
	       if (record.getR78_moreThan3MonthTo6Months() != null) {
	           R78cell3.setCellValue(record.getR78_moreThan3MonthTo6Months().doubleValue());
	           R78cell3.setCellStyle(numberStyle);
	       } else {
	           R78cell3.setCellValue("");
	           R78cell3.setCellStyle(textStyle);
	       }

	       Cell R78cell4 = row.createCell(5);
	       if (record.getR78_moreThan6MonthTo12Months() != null) {
	           R78cell4.setCellValue(record.getR78_moreThan6MonthTo12Months().doubleValue());
	           R78cell4.setCellStyle(numberStyle);
	       } else {
	           R78cell4.setCellValue("");
	           R78cell4.setCellStyle(textStyle);
	       }

	       Cell R78cell5 = row.createCell(6);
	       if (record.getR78_moreThan12MonthTo3Years() != null) {
	           R78cell5.setCellValue(record.getR78_moreThan12MonthTo3Years().doubleValue());
	           R78cell5.setCellStyle(numberStyle);
	       } else {
	           R78cell5.setCellValue("");
	           R78cell5.setCellStyle(textStyle);
	       }

	       Cell R78cell6 = row.createCell(7);
	       if (record.getR78_moreThan3YearsTo5Years() != null) {
	           R78cell6.setCellValue(record.getR78_moreThan3YearsTo5Years().doubleValue());
	           R78cell6.setCellStyle(numberStyle);
	       } else {
	           R78cell6.setCellValue("");
	           R78cell6.setCellStyle(textStyle);
	       }

	       Cell R78cell7 = row.createCell(8);
	       if (record.getR78_moreThan5YearsTo10Years() != null) {
	           R78cell7.setCellValue(record.getR78_moreThan5YearsTo10Years().doubleValue());
	           R78cell7.setCellStyle(numberStyle);
	       } else {
	           R78cell7.setCellValue("");
	           R78cell7.setCellStyle(textStyle);
	       }

	       Cell R78cell8 = row.createCell(9);
	       if (record.getR78_moreThan10Years() != null) {
	           R78cell8.setCellValue(record.getR78_moreThan10Years().doubleValue());
	           R78cell8.setCellStyle(numberStyle);
	       } else {
	           R78cell8.setCellValue("");
	           R78cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(78);
	       Cell R79cell1 = row.createCell(2);
	       if (record.getR79_upTo1Month() != null) {
	           R79cell1.setCellValue(record.getR79_upTo1Month().doubleValue());
	           R79cell1.setCellStyle(numberStyle);
	       } else {
	           R79cell1.setCellValue("");
	           R79cell1.setCellStyle(textStyle);
	       }

	       Cell R79cell2 = row.createCell(3);
	       if (record.getR79_moreThan1MonthTo3Months() != null) {
	           R79cell2.setCellValue(record.getR79_moreThan1MonthTo3Months().doubleValue());
	           R79cell2.setCellStyle(numberStyle);
	       } else {
	           R79cell2.setCellValue("");
	           R79cell2.setCellStyle(textStyle);
	       }

	       Cell R79cell3 = row.createCell(4);
	       if (record.getR79_moreThan3MonthTo6Months() != null) {
	           R79cell3.setCellValue(record.getR79_moreThan3MonthTo6Months().doubleValue());
	           R79cell3.setCellStyle(numberStyle);
	       } else {
	           R79cell3.setCellValue("");
	           R79cell3.setCellStyle(textStyle);
	       }

	       Cell R79cell4 = row.createCell(5);
	       if (record.getR79_moreThan6MonthTo12Months() != null) {
	           R79cell4.setCellValue(record.getR79_moreThan6MonthTo12Months().doubleValue());
	           R79cell4.setCellStyle(numberStyle);
	       } else {
	           R79cell4.setCellValue("");
	           R79cell4.setCellStyle(textStyle);
	       }

	       Cell R79cell5 = row.createCell(6);
	       if (record.getR79_moreThan12MonthTo3Years() != null) {
	           R79cell5.setCellValue(record.getR79_moreThan12MonthTo3Years().doubleValue());
	           R79cell5.setCellStyle(numberStyle);
	       } else {
	           R79cell5.setCellValue("");
	           R79cell5.setCellStyle(textStyle);
	       }

	       Cell R79cell6 = row.createCell(7);
	       if (record.getR79_moreThan3YearsTo5Years() != null) {
	           R79cell6.setCellValue(record.getR79_moreThan3YearsTo5Years().doubleValue());
	           R79cell6.setCellStyle(numberStyle);
	       } else {
	           R79cell6.setCellValue("");
	           R79cell6.setCellStyle(textStyle);
	       }

	       Cell R79cell7 = row.createCell(8);
	       if (record.getR79_moreThan5YearsTo10Years() != null) {
	           R79cell7.setCellValue(record.getR79_moreThan5YearsTo10Years().doubleValue());
	           R79cell7.setCellStyle(numberStyle);
	       } else {
	           R79cell7.setCellValue("");
	           R79cell7.setCellStyle(textStyle);
	       }

	       Cell R79cell8 = row.createCell(9);
	       if (record.getR79_moreThan10Years() != null) {
	           R79cell8.setCellValue(record.getR79_moreThan10Years().doubleValue());
	           R79cell8.setCellStyle(numberStyle);
	       } else {
	           R79cell8.setCellValue("");
	           R79cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(79);
	       Cell R80cell1 = row.createCell(2);
	       if (record.getR80_upTo1Month() != null) {
	           R80cell1.setCellValue(record.getR80_upTo1Month().doubleValue());
	           R80cell1.setCellStyle(numberStyle);
	       } else {
	           R80cell1.setCellValue("");
	           R80cell1.setCellStyle(textStyle);
	       }

	       Cell R80cell2 = row.createCell(3);
	       if (record.getR80_moreThan1MonthTo3Months() != null) {
	           R80cell2.setCellValue(record.getR80_moreThan1MonthTo3Months().doubleValue());
	           R80cell2.setCellStyle(numberStyle);
	       } else {
	           R80cell2.setCellValue("");
	           R80cell2.setCellStyle(textStyle);
	       }

	       Cell R80cell3 = row.createCell(4);
	       if (record.getR80_moreThan3MonthTo6Months() != null) {
	           R80cell3.setCellValue(record.getR80_moreThan3MonthTo6Months().doubleValue());
	           R80cell3.setCellStyle(numberStyle);
	       } else {
	           R80cell3.setCellValue("");
	           R80cell3.setCellStyle(textStyle);
	       }

	       Cell R80cell4 = row.createCell(5);
	       if (record.getR80_moreThan6MonthTo12Months() != null) {
	           R80cell4.setCellValue(record.getR80_moreThan6MonthTo12Months().doubleValue());
	           R80cell4.setCellStyle(numberStyle);
	       } else {
	           R80cell4.setCellValue("");
	           R80cell4.setCellStyle(textStyle);
	       }

	       Cell R80cell5 = row.createCell(6);
	       if (record.getR80_moreThan12MonthTo3Years() != null) {
	           R80cell5.setCellValue(record.getR80_moreThan12MonthTo3Years().doubleValue());
	           R80cell5.setCellStyle(numberStyle);
	       } else {
	           R80cell5.setCellValue("");
	           R80cell5.setCellStyle(textStyle);
	       }

	       Cell R80cell6 = row.createCell(7);
	       if (record.getR80_moreThan3YearsTo5Years() != null) {
	           R80cell6.setCellValue(record.getR80_moreThan3YearsTo5Years().doubleValue());
	           R80cell6.setCellStyle(numberStyle);
	       } else {
	           R80cell6.setCellValue("");
	           R80cell6.setCellStyle(textStyle);
	       }

	       Cell R80cell7 = row.createCell(8);
	       if (record.getR80_moreThan5YearsTo10Years() != null) {
	           R80cell7.setCellValue(record.getR80_moreThan5YearsTo10Years().doubleValue());
	           R80cell7.setCellStyle(numberStyle);
	       } else {
	           R80cell7.setCellValue("");
	           R80cell7.setCellStyle(textStyle);
	       }

	       Cell R80cell8 = row.createCell(9);
	       if (record.getR80_moreThan10Years() != null) {
	           R80cell8.setCellValue(record.getR80_moreThan10Years().doubleValue());
	           R80cell8.setCellStyle(numberStyle);
	       } else {
	           R80cell8.setCellValue("");
	           R80cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(80);
	       Cell R81cell1 = row.createCell(2);
	       if (record.getR81_upTo1Month() != null) {
	           R81cell1.setCellValue(record.getR81_upTo1Month().doubleValue());
	           R81cell1.setCellStyle(numberStyle);
	       } else {
	           R81cell1.setCellValue("");
	           R81cell1.setCellStyle(textStyle);
	       }

	       Cell R81cell2 = row.createCell(3);
	       if (record.getR81_moreThan1MonthTo3Months() != null) {
	           R81cell2.setCellValue(record.getR81_moreThan1MonthTo3Months().doubleValue());
	           R81cell2.setCellStyle(numberStyle);
	       } else {
	           R81cell2.setCellValue("");
	           R81cell2.setCellStyle(textStyle);
	       }

	       Cell R81cell3 = row.createCell(4);
	       if (record.getR81_moreThan3MonthTo6Months() != null) {
	           R81cell3.setCellValue(record.getR81_moreThan3MonthTo6Months().doubleValue());
	           R81cell3.setCellStyle(numberStyle);
	       } else {
	           R81cell3.setCellValue("");
	           R81cell3.setCellStyle(textStyle);
	       }

	       Cell R81cell4 = row.createCell(5);
	       if (record.getR81_moreThan6MonthTo12Months() != null) {
	           R81cell4.setCellValue(record.getR81_moreThan6MonthTo12Months().doubleValue());
	           R81cell4.setCellStyle(numberStyle);
	       } else {
	           R81cell4.setCellValue("");
	           R81cell4.setCellStyle(textStyle);
	       }

	       Cell R81cell5 = row.createCell(6);
	       if (record.getR81_moreThan12MonthTo3Years() != null) {
	           R81cell5.setCellValue(record.getR81_moreThan12MonthTo3Years().doubleValue());
	           R81cell5.setCellStyle(numberStyle);
	       } else {
	           R81cell5.setCellValue("");
	           R81cell5.setCellStyle(textStyle);
	       }

	       Cell R81cell6 = row.createCell(7);
	       if (record.getR81_moreThan3YearsTo5Years() != null) {
	           R81cell6.setCellValue(record.getR81_moreThan3YearsTo5Years().doubleValue());
	           R81cell6.setCellStyle(numberStyle);
	       } else {
	           R81cell6.setCellValue("");
	           R81cell6.setCellStyle(textStyle);
	       }

	       Cell R81cell7 = row.createCell(8);
	       if (record.getR81_moreThan5YearsTo10Years() != null) {
	           R81cell7.setCellValue(record.getR81_moreThan5YearsTo10Years().doubleValue());
	           R81cell7.setCellStyle(numberStyle);
	       } else {
	           R81cell7.setCellValue("");
	           R81cell7.setCellStyle(textStyle);
	       }

	       Cell R81cell8 = row.createCell(9);
	       if (record.getR81_moreThan10Years() != null) {
	           R81cell8.setCellValue(record.getR81_moreThan10Years().doubleValue());
	           R81cell8.setCellStyle(numberStyle);
	       } else {
	           R81cell8.setCellValue("");
	           R81cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(81);
	       Cell R82cell1 = row.createCell(2);
	       if (record.getR82_upTo1Month() != null) {
	           R82cell1.setCellValue(record.getR82_upTo1Month().doubleValue());
	           R82cell1.setCellStyle(numberStyle);
	       } else {
	           R82cell1.setCellValue("");
	           R82cell1.setCellStyle(textStyle);
	       }

	       Cell R82cell2 = row.createCell(3);
	       if (record.getR82_moreThan1MonthTo3Months() != null) {
	           R82cell2.setCellValue(record.getR82_moreThan1MonthTo3Months().doubleValue());
	           R82cell2.setCellStyle(numberStyle);
	       } else {
	           R82cell2.setCellValue("");
	           R82cell2.setCellStyle(textStyle);
	       }

	       Cell R82cell3 = row.createCell(4);
	       if (record.getR82_moreThan3MonthTo6Months() != null) {
	           R82cell3.setCellValue(record.getR82_moreThan3MonthTo6Months().doubleValue());
	           R82cell3.setCellStyle(numberStyle);
	       } else {
	           R82cell3.setCellValue("");
	           R82cell3.setCellStyle(textStyle);
	       }

	       Cell R82cell4 = row.createCell(5);
	       if (record.getR82_moreThan6MonthTo12Months() != null) {
	           R82cell4.setCellValue(record.getR82_moreThan6MonthTo12Months().doubleValue());
	           R82cell4.setCellStyle(numberStyle);
	       } else {
	           R82cell4.setCellValue("");
	           R82cell4.setCellStyle(textStyle);
	       }

	       Cell R82cell5 = row.createCell(6);
	       if (record.getR82_moreThan12MonthTo3Years() != null) {
	           R82cell5.setCellValue(record.getR82_moreThan12MonthTo3Years().doubleValue());
	           R82cell5.setCellStyle(numberStyle);
	       } else {
	           R82cell5.setCellValue("");
	           R82cell5.setCellStyle(textStyle);
	       }

	       Cell R82cell6 = row.createCell(7);
	       if (record.getR82_moreThan3YearsTo5Years() != null) {
	           R82cell6.setCellValue(record.getR82_moreThan3YearsTo5Years().doubleValue());
	           R82cell6.setCellStyle(numberStyle);
	       } else {
	           R82cell6.setCellValue("");
	           R82cell6.setCellStyle(textStyle);
	       }

	       Cell R82cell7 = row.createCell(8);
	       if (record.getR82_moreThan5YearsTo10Years() != null) {
	           R82cell7.setCellValue(record.getR82_moreThan5YearsTo10Years().doubleValue());
	           R82cell7.setCellStyle(numberStyle);
	       } else {
	           R82cell7.setCellValue("");
	           R82cell7.setCellStyle(textStyle);
	       }

	       Cell R82cell8 = row.createCell(9);
	       if (record.getR82_moreThan10Years() != null) {
	           R82cell8.setCellValue(record.getR82_moreThan10Years().doubleValue());
	           R82cell8.setCellStyle(numberStyle);
	       } else {
	           R82cell8.setCellValue("");
	           R82cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(82);
	       Cell R83cell1 = row.createCell(2);
	       if (record.getR83_upTo1Month() != null) {
	           R83cell1.setCellValue(record.getR83_upTo1Month().doubleValue());
	           R83cell1.setCellStyle(numberStyle);
	       } else {
	           R83cell1.setCellValue("");
	           R83cell1.setCellStyle(textStyle);
	       }

	       Cell R83cell2 = row.createCell(3);
	       if (record.getR83_moreThan1MonthTo3Months() != null) {
	           R83cell2.setCellValue(record.getR83_moreThan1MonthTo3Months().doubleValue());
	           R83cell2.setCellStyle(numberStyle);
	       } else {
	           R83cell2.setCellValue("");
	           R83cell2.setCellStyle(textStyle);
	       }

	       Cell R83cell3 = row.createCell(4);
	       if (record.getR83_moreThan3MonthTo6Months() != null) {
	           R83cell3.setCellValue(record.getR83_moreThan3MonthTo6Months().doubleValue());
	           R83cell3.setCellStyle(numberStyle);
	       } else {
	           R83cell3.setCellValue("");
	           R83cell3.setCellStyle(textStyle);
	       }

	       Cell R83cell4 = row.createCell(5);
	       if (record.getR83_moreThan6MonthTo12Months() != null) {
	           R83cell4.setCellValue(record.getR83_moreThan6MonthTo12Months().doubleValue());
	           R83cell4.setCellStyle(numberStyle);
	       } else {
	           R83cell4.setCellValue("");
	           R83cell4.setCellStyle(textStyle);
	       }

	       Cell R83cell5 = row.createCell(6);
	       if (record.getR83_moreThan12MonthTo3Years() != null) {
	           R83cell5.setCellValue(record.getR83_moreThan12MonthTo3Years().doubleValue());
	           R83cell5.setCellStyle(numberStyle);
	       } else {
	           R83cell5.setCellValue("");
	           R83cell5.setCellStyle(textStyle);
	       }

	       Cell R83cell6 = row.createCell(7);
	       if (record.getR83_moreThan3YearsTo5Years() != null) {
	           R83cell6.setCellValue(record.getR83_moreThan3YearsTo5Years().doubleValue());
	           R83cell6.setCellStyle(numberStyle);
	       } else {
	           R83cell6.setCellValue("");
	           R83cell6.setCellStyle(textStyle);
	       }

	       Cell R83cell7 = row.createCell(8);
	       if (record.getR83_moreThan5YearsTo10Years() != null) {
	           R83cell7.setCellValue(record.getR83_moreThan5YearsTo10Years().doubleValue());
	           R83cell7.setCellStyle(numberStyle);
	       } else {
	           R83cell7.setCellValue("");
	           R83cell7.setCellStyle(textStyle);
	       }

	       Cell R83cell8 = row.createCell(9);
	       if (record.getR83_moreThan10Years() != null) {
	           R83cell8.setCellValue(record.getR83_moreThan10Years().doubleValue());
	           R83cell8.setCellStyle(numberStyle);
	       } else {
	           R83cell8.setCellValue("");
	           R83cell8.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(84);
	       Cell R85cell9 = row.createCell(10);
	       if (record.getR85_nonRatioSensativeItems() != null) {
	           R85cell9.setCellValue(record.getR85_nonRatioSensativeItems().doubleValue());
	           R85cell9.setCellStyle(numberStyle);
	       } else {
	           R85cell9.setCellValue("");
	           R85cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(85);
	       Cell R86cell9 = row.createCell(10);
	       if (record.getR86_nonRatioSensativeItems() != null) {
	           R86cell9.setCellValue(record.getR86_nonRatioSensativeItems().doubleValue());
	           R86cell9.setCellStyle(numberStyle);
	       } else {
	           R86cell9.setCellValue("");
	           R86cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(86);
	       Cell R87cell9 = row.createCell(10);
	       if (record.getR87_nonRatioSensativeItems() != null) {
	           R87cell9.setCellValue(record.getR87_nonRatioSensativeItems().doubleValue());
	           R87cell9.setCellStyle(numberStyle);
	       } else {
	           R87cell9.setCellValue("");
	           R87cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(87);
	       Cell R88cell9 = row.createCell(10);
	       if (record.getR88_nonRatioSensativeItems() != null) {
	           R88cell9.setCellValue(record.getR88_nonRatioSensativeItems().doubleValue());
	           R88cell9.setCellStyle(numberStyle);
	       } else {
	           R88cell9.setCellValue("");
	           R88cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(88);
	       Cell R89cell9 = row.createCell(10);
	       if (record.getR89_nonRatioSensativeItems() != null) {
	           R89cell9.setCellValue(record.getR89_nonRatioSensativeItems().doubleValue());
	           R89cell9.setCellStyle(numberStyle);
	       } else {
	           R89cell9.setCellValue("");
	           R89cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(89);
	       Cell R90cell9 = row.createCell(10);
	       if (record.getR90_nonRatioSensativeItems() != null) {
	           R90cell9.setCellValue(record.getR90_nonRatioSensativeItems().doubleValue());
	           R90cell9.setCellStyle(numberStyle);
	       } else {
	           R90cell9.setCellValue("");
	           R90cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(90);
	       Cell R91cell9 = row.createCell(10);
	       if (record.getR91_nonRatioSensativeItems() != null) {
	           R91cell9.setCellValue(record.getR91_nonRatioSensativeItems().doubleValue());
	           R91cell9.setCellStyle(numberStyle);
	       } else {
	           R91cell9.setCellValue("");
	           R91cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(95);
	       Cell R96cell1 = row.createCell(2);
	       if (record.getR96_upTo1Month() != null) {
	           R96cell1.setCellValue(record.getR96_upTo1Month().doubleValue());
	           R96cell1.setCellStyle(numberStyle);
	       } else {
	           R96cell1.setCellValue("");
	           R96cell1.setCellStyle(textStyle);
	       }

	       Cell R96cell2 = row.createCell(3);
	       if (record.getR96_moreThan1MonthTo3Months() != null) {
	           R96cell2.setCellValue(record.getR96_moreThan1MonthTo3Months().doubleValue());
	           R96cell2.setCellStyle(numberStyle);
	       } else {
	           R96cell2.setCellValue("");
	           R96cell2.setCellStyle(textStyle);
	       }

	       Cell R96cell3 = row.createCell(4);
	       if (record.getR96_moreThan3MonthTo6Months() != null) {
	           R96cell3.setCellValue(record.getR96_moreThan3MonthTo6Months().doubleValue());
	           R96cell3.setCellStyle(numberStyle);
	       } else {
	           R96cell3.setCellValue("");
	           R96cell3.setCellStyle(textStyle);
	       }

	       Cell R96cell4 = row.createCell(5);
	       if (record.getR96_moreThan6MonthTo12Months() != null) {
	           R96cell4.setCellValue(record.getR96_moreThan6MonthTo12Months().doubleValue());
	           R96cell4.setCellStyle(numberStyle);
	       } else {
	           R96cell4.setCellValue("");
	           R96cell4.setCellStyle(textStyle);
	       }

	       Cell R96cell5 = row.createCell(6);
	       if (record.getR96_moreThan12MonthTo3Years() != null) {
	           R96cell5.setCellValue(record.getR96_moreThan12MonthTo3Years().doubleValue());
	           R96cell5.setCellStyle(numberStyle);
	       } else {
	           R96cell5.setCellValue("");
	           R96cell5.setCellStyle(textStyle);
	       }

	       Cell R96cell6 = row.createCell(7);
	       if (record.getR96_moreThan3YearsTo5Years() != null) {
	           R96cell6.setCellValue(record.getR96_moreThan3YearsTo5Years().doubleValue());
	           R96cell6.setCellStyle(numberStyle);
	       } else {
	           R96cell6.setCellValue("");
	           R96cell6.setCellStyle(textStyle);
	       }

	       Cell R96cell7 = row.createCell(8);
	       if (record.getR96_moreThan5YearsTo10Years() != null) {
	           R96cell7.setCellValue(record.getR96_moreThan5YearsTo10Years().doubleValue());
	           R96cell7.setCellStyle(numberStyle);
	       } else {
	           R96cell7.setCellValue("");
	           R96cell7.setCellStyle(textStyle);
	       }

	       Cell R96cell8 = row.createCell(9);
	       if (record.getR96_moreThan10Years() != null) {
	           R96cell8.setCellValue(record.getR96_moreThan10Years().doubleValue());
	           R96cell8.setCellStyle(numberStyle);
	       } else {
	           R96cell8.setCellValue("");
	           R96cell8.setCellStyle(textStyle);
	       }

	       Cell R96cell9 = row.createCell(10);
	       if (record.getR96_nonRatioSensativeItems() != null) {
	           R96cell9.setCellValue(record.getR96_nonRatioSensativeItems().doubleValue());
	           R96cell9.setCellStyle(numberStyle);
	       } else {
	           R96cell9.setCellValue("");
	           R96cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(99);
	       Cell R100cell1 = row.createCell(2);
	       if (record.getR100_upTo1Month() != null) {
	           R100cell1.setCellValue(record.getR100_upTo1Month().doubleValue());
	           R100cell1.setCellStyle(numberStyle);
	       } else {
	           R100cell1.setCellValue("");
	           R100cell1.setCellStyle(textStyle);
	       }

	       Cell R100cell2 = row.createCell(3);
	       if (record.getR100_moreThan1MonthTo3Months() != null) {
	           R100cell2.setCellValue(record.getR100_moreThan1MonthTo3Months().doubleValue());
	           R100cell2.setCellStyle(numberStyle);
	       } else {
	           R100cell2.setCellValue("");
	           R100cell2.setCellStyle(textStyle);
	       }

	       Cell R100cell3 = row.createCell(4);
	       if (record.getR100_moreThan3MonthTo6Months() != null) {
	           R100cell3.setCellValue(record.getR100_moreThan3MonthTo6Months().doubleValue());
	           R100cell3.setCellStyle(numberStyle);
	       } else {
	           R100cell3.setCellValue("");
	           R100cell3.setCellStyle(textStyle);
	       }

	       Cell R100cell4 = row.createCell(5);
	       if (record.getR100_moreThan6MonthTo12Months() != null) {
	           R100cell4.setCellValue(record.getR100_moreThan6MonthTo12Months().doubleValue());
	           R100cell4.setCellStyle(numberStyle);
	       } else {
	           R100cell4.setCellValue("");
	           R100cell4.setCellStyle(textStyle);
	       }

	       Cell R100cell5 = row.createCell(6);
	       if (record.getR100_moreThan12MonthTo3Years() != null) {
	           R100cell5.setCellValue(record.getR100_moreThan12MonthTo3Years().doubleValue());
	           R100cell5.setCellStyle(numberStyle);
	       } else {
	           R100cell5.setCellValue("");
	           R100cell5.setCellStyle(textStyle);
	       }

	       Cell R100cell6 = row.createCell(7);
	       if (record.getR100_moreThan3YearsTo5Years() != null) {
	           R100cell6.setCellValue(record.getR100_moreThan3YearsTo5Years().doubleValue());
	           R100cell6.setCellStyle(numberStyle);
	       } else {
	           R100cell6.setCellValue("");
	           R100cell6.setCellStyle(textStyle);
	       }

	       Cell R100cell7 = row.createCell(8);
	       if (record.getR100_moreThan5YearsTo10Years() != null) {
	           R100cell7.setCellValue(record.getR100_moreThan5YearsTo10Years().doubleValue());
	           R100cell7.setCellStyle(numberStyle);
	       } else {
	           R100cell7.setCellValue("");
	           R100cell7.setCellStyle(textStyle);
	       }

	       Cell R100cell8 = row.createCell(9);
	       if (record.getR100_moreThan10Years() != null) {
	           R100cell8.setCellValue(record.getR100_moreThan10Years().doubleValue());
	           R100cell8.setCellStyle(numberStyle);
	       } else {
	           R100cell8.setCellValue("");
	           R100cell8.setCellStyle(textStyle);
	       }

	       Cell R100cell9 = row.createCell(10);
	       if (record.getR100_nonRatioSensativeItems() != null) {
	           R100cell9.setCellValue(record.getR100_nonRatioSensativeItems().doubleValue());
	           R100cell9.setCellStyle(numberStyle);
	       } else {
	           R100cell9.setCellValue("");
	           R100cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(100);
	       Cell R101cell1 = row.createCell(2);
	       if (record.getR101_upTo1Month() != null) {
	           R101cell1.setCellValue(record.getR101_upTo1Month().doubleValue());
	           R101cell1.setCellStyle(numberStyle);
	       } else {
	           R101cell1.setCellValue("");
	           R101cell1.setCellStyle(textStyle);
	       }

	       Cell R101cell2 = row.createCell(3);
	       if (record.getR101_moreThan1MonthTo3Months() != null) {
	           R101cell2.setCellValue(record.getR101_moreThan1MonthTo3Months().doubleValue());
	           R101cell2.setCellStyle(numberStyle);
	       } else {
	           R101cell2.setCellValue("");
	           R101cell2.setCellStyle(textStyle);
	       }

	       Cell R101cell3 = row.createCell(4);
	       if (record.getR101_moreThan3MonthTo6Months() != null) {
	           R101cell3.setCellValue(record.getR101_moreThan3MonthTo6Months().doubleValue());
	           R101cell3.setCellStyle(numberStyle);
	       } else {
	           R101cell3.setCellValue("");
	           R101cell3.setCellStyle(textStyle);
	       }

	       Cell R101cell4 = row.createCell(5);
	       if (record.getR101_moreThan6MonthTo12Months() != null) {
	           R101cell4.setCellValue(record.getR101_moreThan6MonthTo12Months().doubleValue());
	           R101cell4.setCellStyle(numberStyle);
	       } else {
	           R101cell4.setCellValue("");
	           R101cell4.setCellStyle(textStyle);
	       }

	       Cell R101cell5 = row.createCell(6);
	       if (record.getR101_moreThan12MonthTo3Years() != null) {
	           R101cell5.setCellValue(record.getR101_moreThan12MonthTo3Years().doubleValue());
	           R101cell5.setCellStyle(numberStyle);
	       } else {
	           R101cell5.setCellValue("");
	           R101cell5.setCellStyle(textStyle);
	       }

	       Cell R101cell6 = row.createCell(7);
	       if (record.getR101_moreThan3YearsTo5Years() != null) {
	           R101cell6.setCellValue(record.getR101_moreThan3YearsTo5Years().doubleValue());
	           R101cell6.setCellStyle(numberStyle);
	       } else {
	           R101cell6.setCellValue("");
	           R101cell6.setCellStyle(textStyle);
	       }

	       Cell R101cell7 = row.createCell(8);
	       if (record.getR101_moreThan5YearsTo10Years() != null) {
	           R101cell7.setCellValue(record.getR101_moreThan5YearsTo10Years().doubleValue());
	           R101cell7.setCellStyle(numberStyle);
	       } else {
	           R101cell7.setCellValue("");
	           R101cell7.setCellStyle(textStyle);
	       }

	       Cell R101cell8 = row.createCell(9);
	       if (record.getR101_moreThan10Years() != null) {
	           R101cell8.setCellValue(record.getR101_moreThan10Years().doubleValue());
	           R101cell8.setCellStyle(numberStyle);
	       } else {
	           R101cell8.setCellValue("");
	           R101cell8.setCellStyle(textStyle);
	       }

	       Cell R101cell9 = row.createCell(10);
	       if (record.getR101_nonRatioSensativeItems() != null) {
	           R101cell9.setCellValue(record.getR101_nonRatioSensativeItems().doubleValue());
	           R101cell9.setCellStyle(numberStyle);
	       } else {
	           R101cell9.setCellValue("");
	           R101cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(101);
	       Cell R102cell1 = row.createCell(2);
	       if (record.getR102_upTo1Month() != null) {
	           R102cell1.setCellValue(record.getR102_upTo1Month().doubleValue());
	           R102cell1.setCellStyle(numberStyle);
	       } else {
	           R102cell1.setCellValue("");
	           R102cell1.setCellStyle(textStyle);
	       }

	       Cell R102cell2 = row.createCell(3);
	       if (record.getR102_moreThan1MonthTo3Months() != null) {
	           R102cell2.setCellValue(record.getR102_moreThan1MonthTo3Months().doubleValue());
	           R102cell2.setCellStyle(numberStyle);
	       } else {
	           R102cell2.setCellValue("");
	           R102cell2.setCellStyle(textStyle);
	       }

	       Cell R102cell3 = row.createCell(4);
	       if (record.getR102_moreThan3MonthTo6Months() != null) {
	           R102cell3.setCellValue(record.getR102_moreThan3MonthTo6Months().doubleValue());
	           R102cell3.setCellStyle(numberStyle);
	       } else {
	           R102cell3.setCellValue("");
	           R102cell3.setCellStyle(textStyle);
	       }

	       Cell R102cell4 = row.createCell(5);
	       if (record.getR102_moreThan6MonthTo12Months() != null) {
	           R102cell4.setCellValue(record.getR102_moreThan6MonthTo12Months().doubleValue());
	           R102cell4.setCellStyle(numberStyle);
	       } else {
	           R102cell4.setCellValue("");
	           R102cell4.setCellStyle(textStyle);
	       }

	       Cell R102cell5 = row.createCell(6);
	       if (record.getR102_moreThan12MonthTo3Years() != null) {
	           R102cell5.setCellValue(record.getR102_moreThan12MonthTo3Years().doubleValue());
	           R102cell5.setCellStyle(numberStyle);
	       } else {
	           R102cell5.setCellValue("");
	           R102cell5.setCellStyle(textStyle);
	       }

	       Cell R102cell6 = row.createCell(7);
	       if (record.getR102_moreThan3YearsTo5Years() != null) {
	           R102cell6.setCellValue(record.getR102_moreThan3YearsTo5Years().doubleValue());
	           R102cell6.setCellStyle(numberStyle);
	       } else {
	           R102cell6.setCellValue("");
	           R102cell6.setCellStyle(textStyle);
	       }

	       Cell R102cell7 = row.createCell(8);
	       if (record.getR102_moreThan5YearsTo10Years() != null) {
	           R102cell7.setCellValue(record.getR102_moreThan5YearsTo10Years().doubleValue());
	           R102cell7.setCellStyle(numberStyle);
	       } else {
	           R102cell7.setCellValue("");
	           R102cell7.setCellStyle(textStyle);
	       }

	       Cell R102cell8 = row.createCell(9);
	       if (record.getR102_moreThan10Years() != null) {
	           R102cell8.setCellValue(record.getR102_moreThan10Years().doubleValue());
	           R102cell8.setCellStyle(numberStyle);
	       } else {
	           R102cell8.setCellValue("");
	           R102cell8.setCellStyle(textStyle);
	       }

	       Cell R102cell9 = row.createCell(10);
	       if (record.getR102_nonRatioSensativeItems() != null) {
	           R102cell9.setCellValue(record.getR102_nonRatioSensativeItems().doubleValue());
	           R102cell9.setCellStyle(numberStyle);
	       } else {
	           R102cell9.setCellValue("");
	           R102cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(102);
	       Cell R103cell1 = row.createCell(2);
	       if (record.getR103_upTo1Month() != null) {
	           R103cell1.setCellValue(record.getR103_upTo1Month().doubleValue());
	           R103cell1.setCellStyle(numberStyle);
	       } else {
	           R103cell1.setCellValue("");
	           R103cell1.setCellStyle(textStyle);
	       }

	       Cell R103cell2 = row.createCell(3);
	       if (record.getR103_moreThan1MonthTo3Months() != null) {
	           R103cell2.setCellValue(record.getR103_moreThan1MonthTo3Months().doubleValue());
	           R103cell2.setCellStyle(numberStyle);
	       } else {
	           R103cell2.setCellValue("");
	           R103cell2.setCellStyle(textStyle);
	       }

	       Cell R103cell3 = row.createCell(4);
	       if (record.getR103_moreThan3MonthTo6Months() != null) {
	           R103cell3.setCellValue(record.getR103_moreThan3MonthTo6Months().doubleValue());
	           R103cell3.setCellStyle(numberStyle);
	       } else {
	           R103cell3.setCellValue("");
	           R103cell3.setCellStyle(textStyle);
	       }

	       Cell R103cell4 = row.createCell(5);
	       if (record.getR103_moreThan6MonthTo12Months() != null) {
	           R103cell4.setCellValue(record.getR103_moreThan6MonthTo12Months().doubleValue());
	           R103cell4.setCellStyle(numberStyle);
	       } else {
	           R103cell4.setCellValue("");
	           R103cell4.setCellStyle(textStyle);
	       }

	       Cell R103cell5 = row.createCell(6);
	       if (record.getR103_moreThan12MonthTo3Years() != null) {
	           R103cell5.setCellValue(record.getR103_moreThan12MonthTo3Years().doubleValue());
	           R103cell5.setCellStyle(numberStyle);
	       } else {
	           R103cell5.setCellValue("");
	           R103cell5.setCellStyle(textStyle);
	       }

	       Cell R103cell6 = row.createCell(7);
	       if (record.getR103_moreThan3YearsTo5Years() != null) {
	           R103cell6.setCellValue(record.getR103_moreThan3YearsTo5Years().doubleValue());
	           R103cell6.setCellStyle(numberStyle);
	       } else {
	           R103cell6.setCellValue("");
	           R103cell6.setCellStyle(textStyle);
	       }

	       Cell R103cell7 = row.createCell(8);
	       if (record.getR103_moreThan5YearsTo10Years() != null) {
	           R103cell7.setCellValue(record.getR103_moreThan5YearsTo10Years().doubleValue());
	           R103cell7.setCellStyle(numberStyle);
	       } else {
	           R103cell7.setCellValue("");
	           R103cell7.setCellStyle(textStyle);
	       }

	       Cell R103cell8 = row.createCell(9);
	       if (record.getR103_moreThan10Years() != null) {
	           R103cell8.setCellValue(record.getR103_moreThan10Years().doubleValue());
	           R103cell8.setCellStyle(numberStyle);
	       } else {
	           R103cell8.setCellValue("");
	           R103cell8.setCellStyle(textStyle);
	       }

	       Cell R103cell9 = row.createCell(10);
	       if (record.getR103_nonRatioSensativeItems() != null) {
	           R103cell9.setCellValue(record.getR103_nonRatioSensativeItems().doubleValue());
	           R103cell9.setCellStyle(numberStyle);
	       } else {
	           R103cell9.setCellValue("");
	           R103cell9.setCellStyle(textStyle);
	       }

	       row = sheet.getRow(103);
	       Cell R104cell1 = row.createCell(2);
	       if (record.getR104_upTo1Month() != null) {
	           R104cell1.setCellValue(record.getR104_upTo1Month().doubleValue());
	           R104cell1.setCellStyle(numberStyle);
	       } else {
	           R104cell1.setCellValue("");
	           R104cell1.setCellStyle(textStyle);
	       }

	       Cell R104cell2 = row.createCell(3);
	       if (record.getR104_moreThan1MonthTo3Months() != null) {
	           R104cell2.setCellValue(record.getR104_moreThan1MonthTo3Months().doubleValue());
	           R104cell2.setCellStyle(numberStyle);
	       } else {
	           R104cell2.setCellValue("");
	           R104cell2.setCellStyle(textStyle);
	       }

	       Cell R104cell3 = row.createCell(4);
	       if (record.getR104_moreThan3MonthTo6Months() != null) {
	           R104cell3.setCellValue(record.getR104_moreThan3MonthTo6Months().doubleValue());
	           R104cell3.setCellStyle(numberStyle);
	       } else {
	           R104cell3.setCellValue("");
	           R104cell3.setCellStyle(textStyle);
	       }

	       Cell R104cell4 = row.createCell(5);
	       if (record.getR104_moreThan6MonthTo12Months() != null) {
	           R104cell4.setCellValue(record.getR104_moreThan6MonthTo12Months().doubleValue());
	           R104cell4.setCellStyle(numberStyle);
	       } else {
	           R104cell4.setCellValue("");
	           R104cell4.setCellStyle(textStyle);
	       }

	       Cell R104cell5 = row.createCell(6);
	       if (record.getR104_moreThan12MonthTo3Years() != null) {
	           R104cell5.setCellValue(record.getR104_moreThan12MonthTo3Years().doubleValue());
	           R104cell5.setCellStyle(numberStyle);
	       } else {
	           R104cell5.setCellValue("");
	           R104cell5.setCellStyle(textStyle);
	       }

	       Cell R104cell6 = row.createCell(7);
	       if (record.getR104_moreThan3YearsTo5Years() != null) {
	           R104cell6.setCellValue(record.getR104_moreThan3YearsTo5Years().doubleValue());
	           R104cell6.setCellStyle(numberStyle);
	       } else {
	           R104cell6.setCellValue("");
	           R104cell6.setCellStyle(textStyle);
	       }

	       Cell R104cell7 = row.createCell(8);
	       if (record.getR104_moreThan5YearsTo10Years() != null) {
	           R104cell7.setCellValue(record.getR104_moreThan5YearsTo10Years().doubleValue());
	           R104cell7.setCellStyle(numberStyle);
	       } else {
	           R104cell7.setCellValue("");
	           R104cell7.setCellStyle(textStyle);
	       }

	       Cell R104cell8 = row.createCell(9);
	       if (record.getR104_moreThan10Years() != null) {
	           R104cell8.setCellValue(record.getR104_moreThan10Years().doubleValue());
	           R104cell8.setCellStyle(numberStyle);
	       } else {
	           R104cell8.setCellValue("");
	           R104cell8.setCellStyle(textStyle);
	       }

	       Cell R104cell9 = row.createCell(10);
	       if (record.getR104_nonRatioSensativeItems() != null) {
	           R104cell9.setCellValue(record.getR104_nonRatioSensativeItems().doubleValue());
	           R104cell9.setCellStyle(numberStyle);
	       } else {
	           R104cell9.setCellValue("");
	           R104cell9.setCellStyle(textStyle);
	       }

	  
	  }



}
