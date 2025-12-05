package com.bornfire.brrs.services;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

//=== Apache POI Excel ===
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
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

import com.bornfire.brrs.entities.BRRS_M_SFINP1_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SFINP1_Archival_Summary_Manual_Repo;
import com.bornfire.brrs.entities.BRRS_M_SFINP1_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SFINP1_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SFINP1_Summary_Manual_Repo;
import com.bornfire.brrs.entities.BRRS_M_SFINP1_Summary_Repo;
import com.bornfire.brrs.entities.M_CA2_Detail_Entity;
//import com.bornfire.brrs.controllers.CBUAE_BRF_ReportsController;
import com.bornfire.brrs.entities.M_SFINP1_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_SFINP1_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SFINP1_Archival_Summary_Manual_Entity;
import com.bornfire.brrs.entities.M_SFINP1_Detail_Entity;
import com.bornfire.brrs.entities.M_SFINP1_Summary_Entity;
import com.bornfire.brrs.entities.M_SFINP1_Summary_Manual_Entity;
//=== iText PDF ===
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Component
@Service
public class BRRS_M_SFINP1_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SFINP1_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_SFINP1_Detail_Repo BRRS_M_SFINP1_Detail_Repo;

	@Autowired
	BRRS_M_SFINP1_Summary_Repo BRRS_M_SFINP1_Summary_Repo;

	@Autowired
	BRRS_M_SFINP1_Summary_Manual_Repo BRRS_M_SFINP1_Summary_Manual_Repo;

	@Autowired
	BRRS_M_SFINP1_Archival_Detail_Repo BRRS_M_SFINP1_Archival_Detail_Repo;
	@Autowired
	BRRS_M_SFINP1_Archival_Summary_Repo BRRS_M_SFINP1_Archival_Summary_Repo;

	@Autowired
	BRRS_M_SFINP1_Archival_Summary_Manual_Repo BRRS_M_SFINP1_Archival_Summary_Manual_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_SFINP1View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, String version) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		System.out.println("🟢 getM_SFINP1View() called");

		try {
			Date reportDate = dateformat.parse(todate);

// ======= CASE 1: ARCHIVAL =======
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
				System.out.println("📦 Fetching ARCHIVAL data for version: " + version);

				List<M_SFINP1_Archival_Summary_Entity> mainList = BRRS_M_SFINP1_Archival_Summary_Repo.getdatabydateListarchival(reportDate,version);
				List<M_SFINP1_Archival_Summary_Manual_Entity> archivalList = BRRS_M_SFINP1_Archival_Summary_Manual_Repo
						.getdatabydateListarchival(reportDate);
				
				mv.addObject("reportsummary", mainList);
				mv.addObject("reportsummary", archivalList);
				
				

			} else {
// ======= CASE 2: MANUAL =======
				System.out.println("🧾 Fetching MANUAL data for date: " + todate);

				List<M_SFINP1_Summary_Entity> mainList = BRRS_M_SFINP1_Summary_Repo.getdatabydateList(reportDate);
				List<M_SFINP1_Summary_Manual_Entity> manualList = BRRS_M_SFINP1_Summary_Manual_Repo
						.getdatabydateList(reportDate);

// ======= Fields to calculate averages =======
				String[] fields = { "R14", "R34", "R37", "R39", "R43", "R50", "R51", "R52", "R57", "R59" };

				for (String field : fields) {
					try {
						BigDecimal currentVal = (BigDecimal) hs.createNativeQuery("SELECT " + field
								+ "_MONTH_END FROM BRRS_M_SFINP1_SUMMARYTABLE_MANUAL WHERE REPORT_DATE = :reportDate")
								.setParameter("reportDate", reportDate).getSingleResult();

						BigDecimal previousVal = (BigDecimal) hs.createNativeQuery("SELECT " + field
								+ "_MONTH_END FROM BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY_MANUAL WHERE REPORT_DATE = ADD_MONTHS(:reportDate,-1)")
								.setParameter("reportDate", reportDate).getSingleResult();

						if (currentVal != null && previousVal != null) {
							BigDecimal averageVal = currentVal.add(previousVal).divide(BigDecimal.valueOf(2), 2,
									RoundingMode.HALF_UP);

// Update the average in main summary table
							hs.createNativeQuery("UPDATE BRRS_M_SFINP1_SUMMARYTABLE SET " + field
									+ "_AVERAGE = :avg WHERE REPORT_DATE = :reportDate").setParameter("avg", averageVal)
									.setParameter("reportDate", reportDate).executeUpdate();

							System.out.println("✅ " + field + "_AVERAGE updated: " + averageVal);
						}

					} catch (Exception e) {
						System.err.println("❌ Error processing " + field + ": " + e.getMessage());
					}
				}

// ======= Add to ModelAndView =======
				mv.addObject("reportsummary", mainList);
				mv.addObject("reportsummary1", manualList);
			}

// ======= Common view settings =======
			mv.setViewName("BRRS/M_SFINP1");
			mv.addObject("displaymode", "summary");
			System.out.println("✅ View set to: " + mv.getViewName());

		} catch (ParseException e) {
			System.err.println("❌ Error parsing todate: " + todate);
			e.printStackTrace();
		} catch (Exception ex) {
			System.err.println("❌ Unexpected error in getM_SFINP1View()");
			ex.printStackTrace();
		}

		return mv;
	}

	public ModelAndView getM_SFINP1currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String Filter, String type, String version) {
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
				List<M_SFINP1_Archival_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BRRS_M_SFINP1_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate,
							version);
				} else {
					T1Dt1 = BRRS_M_SFINP1_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}
				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
			} else {
				// 🔹 Current branch
				List<M_SFINP1_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BRRS_M_SFINP1_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
				} else {
					T1Dt1 = BRRS_M_SFINP1_Detail_Repo.getdatabydateList(parsedDate, currentPage, pageSize);
					totalPages = BRRS_M_SFINP1_Detail_Repo.getdatacount(parsedDate);
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
		mv.setViewName("BRRS/M_SFINP1");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}

	public void updateReport(M_SFINP1_Summary_Manual_Entity updatedEntity) {
		System.out.println("Came to services 1");
		System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

		M_SFINP1_Summary_Manual_Entity existing = BRRS_M_SFINP1_Summary_Manual_Repo
				.findById(updatedEntity.getREPORT_DATE()).orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

		try {

			existing.setR14_MONTH_END(updatedEntity.getR14_MONTH_END());
			existing.setR34_MONTH_END(updatedEntity.getR34_MONTH_END());
			existing.setR37_MONTH_END(updatedEntity.getR37_MONTH_END());
			existing.setR39_MONTH_END(updatedEntity.getR39_MONTH_END());
			existing.setR43_MONTH_END(updatedEntity.getR43_MONTH_END());
			existing.setR50_MONTH_END(updatedEntity.getR50_MONTH_END());
			existing.setR51_MONTH_END(updatedEntity.getR51_MONTH_END());
			existing.setR52_MONTH_END(updatedEntity.getR52_MONTH_END());
			existing.setR57_MONTH_END(updatedEntity.getR57_MONTH_END());
			existing.setR59_MONTH_END(updatedEntity.getR59_MONTH_END());

			// Save back
			BRRS_M_SFINP1_Summary_Manual_Repo.save(existing);

		} catch (Exception e) {
			throw new RuntimeException("Error updating report: " + e.getMessage(), e);
		}
	}

	public byte[] getM_SFINP1Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
		DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);

		LocalDate date = LocalDate.parse(todate, inputFormat);
		String formattedDate = date.format(outputFormat);


		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_SFINP1ARCHIVAL(filename, reportId, fromdate, formattedDate, currency, dtltype, type,
					version);
		}

		// Fetch data
		List<M_SFINP1_Summary_Entity> dataList = BRRS_M_SFINP1_Summary_Repo.getdatabydateList1(formattedDate);
		List<M_SFINP1_Summary_Manual_Entity> dataList1 = BRRS_M_SFINP1_Summary_Manual_Repo.getdatabydateList1(formattedDate);
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SFINP1 report. Returning empty result.");
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
			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SFINP1_Summary_Entity record = dataList.get(i);
					M_SFINP1_Summary_Manual_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cellC, cellD;
					CellStyle originalStyle;
					// ===== Row 10 / Col C =====
					row = sheet.getRow(9);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR10_month_end() != null)
						cellC.setCellValue(record.getR10_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 10 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR10_average() != null)
						cellD.setCellValue(record.getR10_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 11 / Col C =====
					row = sheet.getRow(10);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR11_month_end() != null)
						cellC.setCellValue(record.getR11_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 11 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR11_average() != null)
						cellD.setCellValue(record.getR11_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 13 / Col C =====
					row = sheet.getRow(12);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR13_month_end() != null)
						cellC.setCellValue(record.getR13_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 13 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR13_average() != null)
						cellD.setCellValue(record.getR13_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 14 / Col C =====
					row = sheet.getRow(13);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR14_MONTH_END() != null)
						cellC.setCellValue(record1.getR14_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 14 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR14_average() != null)
						cellD.setCellValue(record.getR14_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 15 / Col C =====
					row = sheet.getRow(14);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR15_month_end() != null)
						cellC.setCellValue(record.getR15_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 15 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR15_average() != null)
						cellD.setCellValue(record.getR15_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 16 / Col C =====
					row = sheet.getRow(15);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR16_month_end() != null)
						cellC.setCellValue(record.getR16_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 16 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR16_average() != null)
						cellD.setCellValue(record.getR16_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 17 / Col C =====
					row = sheet.getRow(16);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR17_month_end() != null)
						cellC.setCellValue(record.getR17_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 17 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR17_average() != null)
						cellD.setCellValue(record.getR17_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 18 / Col C =====
					row = sheet.getRow(17);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR18_month_end() != null)
						cellC.setCellValue(record.getR18_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 18 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR18_average() != null)
						cellD.setCellValue(record.getR18_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 19 / Col C =====
					row = sheet.getRow(18);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR19_month_end() != null)
						cellC.setCellValue(record.getR19_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 19 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR19_average() != null)
						cellD.setCellValue(record.getR19_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 22 / Col C =====
					row = sheet.getRow(21);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR22_month_end() != null)
						cellC.setCellValue(record.getR22_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 22 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR22_average() != null)
						cellD.setCellValue(record.getR22_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 23 / Col C =====
					row = sheet.getRow(22);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR23_month_end() != null)
						cellC.setCellValue(record.getR23_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 23 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR23_average() != null)
						cellD.setCellValue(record.getR23_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 25 / Col C =====
					row = sheet.getRow(24);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR25_month_end() != null)
						cellC.setCellValue(record.getR25_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 25 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR25_average() != null)
						cellD.setCellValue(record.getR25_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 26 / Col C =====
					row = sheet.getRow(25);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR26_month_end() != null)
						cellC.setCellValue(record.getR26_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 26 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR26_average() != null)
						cellD.setCellValue(record.getR26_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 28 / Col C =====
					row = sheet.getRow(27);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR28_month_end() != null)
						cellC.setCellValue(record.getR28_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 28 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR28_average() != null)
						cellD.setCellValue(record.getR28_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 29 / Col C =====
					row = sheet.getRow(28);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR29_month_end() != null)
						cellC.setCellValue(record.getR29_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 29 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR29_average() != null)
						cellD.setCellValue(record.getR29_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 30 / Col C =====
					row = sheet.getRow(29);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR30_month_end() != null)
						cellC.setCellValue(record.getR30_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 30 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR30_average() != null)
						cellD.setCellValue(record.getR30_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 32 / Col C =====
					row = sheet.getRow(31);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR32_month_end() != null)
						cellC.setCellValue(record.getR32_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 32 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR32_average() != null)
						cellD.setCellValue(record.getR32_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 33 / Col C =====
					row = sheet.getRow(32);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR33_month_end() != null)
						cellC.setCellValue(record.getR33_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 33 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR33_average() != null)
						cellD.setCellValue(record.getR33_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 34 / Col C =====
					row = sheet.getRow(33);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR34_MONTH_END() != null)
						cellC.setCellValue(record1.getR34_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 34 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR34_average() != null)
						cellD.setCellValue(record.getR34_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 36 / Col C =====
					row = sheet.getRow(35);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR36_month_end() != null)
						cellC.setCellValue(record.getR36_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 36 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR36_average() != null)
						cellD.setCellValue(record.getR36_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 37 / Col C =====
					row = sheet.getRow(36);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR37_MONTH_END() != null)
						cellC.setCellValue(record1.getR37_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 37 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR37_average() != null)
						cellD.setCellValue(record.getR37_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 39 / Col C =====
					row = sheet.getRow(38);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR39_month_end() != null)
						cellC.setCellValue(record.getR39_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 39 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR39_average() != null)
						cellD.setCellValue(record.getR39_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 41 / Col C =====
					row = sheet.getRow(40);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR41_month_end() != null)
						cellC.setCellValue(record.getR41_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 41 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR41_average() != null)
						cellD.setCellValue(record.getR41_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 42 / Col C =====
					row = sheet.getRow(41);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR42_month_end() != null)
						cellC.setCellValue(record.getR42_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 42 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR42_average() != null)
						cellD.setCellValue(record.getR42_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 43 / Col C =====
					row = sheet.getRow(42);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR43_MONTH_END() != null)
						cellC.setCellValue(record1.getR43_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 43 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR43_average() != null)
						cellD.setCellValue(record.getR43_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 45 / Col C =====
					row = sheet.getRow(44);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR45_month_end() != null)
						cellC.setCellValue(record.getR45_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 45 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR45_average() != null)
						cellD.setCellValue(record.getR45_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 46 / Col C =====
					row = sheet.getRow(45);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR46_month_end() != null)
						cellC.setCellValue(record.getR46_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 46 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR46_average() != null)
						cellD.setCellValue(record.getR46_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 47 / Col C =====
					row = sheet.getRow(46);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR47_month_end() != null)
						cellC.setCellValue(record.getR47_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 47 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR47_average() != null)
						cellD.setCellValue(record.getR47_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 48 / Col C =====
					row = sheet.getRow(47);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR48_month_end() != null)
						cellC.setCellValue(record.getR48_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 48 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR48_average() != null)
						cellD.setCellValue(record.getR48_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 50 / Col C =====
					row = sheet.getRow(49);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR50_month_end() != null)
						cellC.setCellValue(record.getR50_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 50 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR50_MONTH_END() != null)
						cellD.setCellValue(record1.getR50_MONTH_END().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 51 / Col C =====
					row = sheet.getRow(50);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR51_MONTH_END() != null)
						cellC.setCellValue(record1.getR51_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 51 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR51_average() != null)
						cellD.setCellValue(record.getR51_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 52 / Col C =====
					row = sheet.getRow(51);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR52_MONTH_END() != null)
						cellC.setCellValue(record1.getR52_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 52 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR52_average() != null)
						cellD.setCellValue(record.getR52_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 53 / Col C =====
					row = sheet.getRow(52);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR53_month_end() != null)
						cellC.setCellValue(record.getR53_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 53 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR53_average() != null)
						cellD.setCellValue(record.getR53_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 54 / Col C =====
					row = sheet.getRow(53);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR54_month_end() != null)
						cellC.setCellValue(record.getR54_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 54 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR54_average() != null)
						cellD.setCellValue(record.getR54_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 55 / Col C =====
					row = sheet.getRow(54);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR55_month_end() != null)
						cellC.setCellValue(record.getR55_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 55 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR55_average() != null)
						cellD.setCellValue(record.getR55_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 57 / Col C =====
					row = sheet.getRow(56);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR57_MONTH_END() != null)
						cellC.setCellValue(record1.getR57_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 57 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR57_average() != null)
						cellD.setCellValue(record.getR57_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 58 / Col C =====
					row = sheet.getRow(57);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR58_month_end() != null)
						cellC.setCellValue(record.getR58_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 58 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR58_average() != null)
						cellD.setCellValue(record.getR58_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 59 / Col C =====
					row = sheet.getRow(58);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR59_MONTH_END() != null)
						cellC.setCellValue(record1.getR59_MONTH_END().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 59 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR59_average() != null)
						cellD.setCellValue(record.getR59_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 60 / Col C =====
					row = sheet.getRow(59);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR60_month_end() != null)
						cellC.setCellValue(record.getR60_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 60 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR60_average() != null)
						cellD.setCellValue(record.getR60_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
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

	public byte[] getM_SFINP1DetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for M_SFINP1 Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_SFINP1Details");
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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA ", "ROWID", "COLUMNID",
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
			List<M_SFINP1_Detail_Entity> reportData = BRRS_M_SFINP1_Detail_Repo.getdatabydateList(parsedToDate);
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_SFINP1_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());
					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInPula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
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
				logger.info("No data found for M_SFINP1 — only header will be written.");
			}
			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();
		} catch (Exception e) {
			logger.error("Error generating M_SFINP1 Excel", e);
			return new byte[0];
		}
	}

	public List<Object> getM_SFINP1Archival() {
		List<Object> M_SFINP1Archivallist = new ArrayList<>();
		try {
			M_SFINP1Archivallist = BRRS_M_SFINP1_Archival_Summary_Repo.getM_SFINP1archival();
			System.out.println("countser" + M_SFINP1Archivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_SFINP1 Archival data: " + e.getMessage());
			e.printStackTrace();
			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_SFINP1Archivallist;
	}

	public byte[] getExcelM_SFINP1ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {
		}
		List<M_SFINP1_Archival_Summary_Entity> dataList = BRRS_M_SFINP1_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SFINP1 report. Returning empty result.");
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
			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SFINP1_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cellC, cellD;
					CellStyle originalStyle;
					// ===== Row 10 / Col C =====
					row = sheet.getRow(9);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR10_month_end() != null)
						cellC.setCellValue(record.getR10_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 10 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR10_average() != null)
						cellD.setCellValue(record.getR10_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 11 / Col C =====
					row = sheet.getRow(10);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR11_month_end() != null)
						cellC.setCellValue(record.getR11_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 11 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR11_average() != null)
						cellD.setCellValue(record.getR11_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 13 / Col C =====
					row = sheet.getRow(12);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR13_month_end() != null)
						cellC.setCellValue(record.getR13_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 13 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR13_average() != null)
						cellD.setCellValue(record.getR13_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 14 / Col C =====
					row = sheet.getRow(13);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR14_month_end() != null)
						cellC.setCellValue(record.getR14_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 14 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR14_average() != null)
						cellD.setCellValue(record.getR14_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 15 / Col C =====
					row = sheet.getRow(14);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR15_month_end() != null)
						cellC.setCellValue(record.getR15_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 15 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR15_average() != null)
						cellD.setCellValue(record.getR15_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 16 / Col C =====
					row = sheet.getRow(15);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR16_month_end() != null)
						cellC.setCellValue(record.getR16_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 16 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR16_average() != null)
						cellD.setCellValue(record.getR16_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 17 / Col C =====
					row = sheet.getRow(16);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR17_month_end() != null)
						cellC.setCellValue(record.getR17_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 17 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR17_average() != null)
						cellD.setCellValue(record.getR17_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 18 / Col C =====
					row = sheet.getRow(17);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR18_month_end() != null)
						cellC.setCellValue(record.getR18_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 18 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR18_average() != null)
						cellD.setCellValue(record.getR18_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 19 / Col C =====
					row = sheet.getRow(18);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR19_month_end() != null)
						cellC.setCellValue(record.getR19_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 19 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR19_average() != null)
						cellD.setCellValue(record.getR19_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 22 / Col C =====
					row = sheet.getRow(21);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR22_month_end() != null)
						cellC.setCellValue(record.getR22_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 22 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR22_average() != null)
						cellD.setCellValue(record.getR22_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 23 / Col C =====
					row = sheet.getRow(22);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR23_month_end() != null)
						cellC.setCellValue(record.getR23_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 23 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR23_average() != null)
						cellD.setCellValue(record.getR23_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 25 / Col C =====
					row = sheet.getRow(24);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR25_month_end() != null)
						cellC.setCellValue(record.getR25_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 25 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR25_average() != null)
						cellD.setCellValue(record.getR25_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 26 / Col C =====
					row = sheet.getRow(25);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR26_month_end() != null)
						cellC.setCellValue(record.getR26_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 26 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR26_average() != null)
						cellD.setCellValue(record.getR26_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 28 / Col C =====
					row = sheet.getRow(27);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR28_month_end() != null)
						cellC.setCellValue(record.getR28_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 28 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR28_average() != null)
						cellD.setCellValue(record.getR28_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 29 / Col C =====
					row = sheet.getRow(28);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR29_month_end() != null)
						cellC.setCellValue(record.getR29_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 29 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR29_average() != null)
						cellD.setCellValue(record.getR29_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 30 / Col C =====
					row = sheet.getRow(29);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR30_month_end() != null)
						cellC.setCellValue(record.getR30_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 30 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR30_average() != null)
						cellD.setCellValue(record.getR30_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 32 / Col C =====
					row = sheet.getRow(31);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR32_month_end() != null)
						cellC.setCellValue(record.getR32_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 32 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR32_average() != null)
						cellD.setCellValue(record.getR32_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 33 / Col C =====
					row = sheet.getRow(32);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR33_month_end() != null)
						cellC.setCellValue(record.getR33_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 33 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR33_average() != null)
						cellD.setCellValue(record.getR33_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 34 / Col C =====
					row = sheet.getRow(33);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR34_month_end() != null)
						cellC.setCellValue(record.getR34_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 34 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR34_average() != null)
						cellD.setCellValue(record.getR34_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 36 / Col C =====
					row = sheet.getRow(35);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR36_month_end() != null)
						cellC.setCellValue(record.getR36_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 36 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR36_average() != null)
						cellD.setCellValue(record.getR36_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 37 / Col C =====
					row = sheet.getRow(36);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR37_month_end() != null)
						cellC.setCellValue(record.getR37_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 37 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR37_average() != null)
						cellD.setCellValue(record.getR37_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 39 / Col C =====
					row = sheet.getRow(38);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR39_month_end() != null)
						cellC.setCellValue(record.getR39_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 39 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR39_average() != null)
						cellD.setCellValue(record.getR39_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 41 / Col C =====
					row = sheet.getRow(40);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR41_month_end() != null)
						cellC.setCellValue(record.getR41_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 41 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR41_average() != null)
						cellD.setCellValue(record.getR41_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 42 / Col C =====
					row = sheet.getRow(41);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR42_month_end() != null)
						cellC.setCellValue(record.getR42_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 42 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR42_average() != null)
						cellD.setCellValue(record.getR42_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 43 / Col C =====
					row = sheet.getRow(42);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR43_month_end() != null)
						cellC.setCellValue(record.getR43_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 43 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR43_average() != null)
						cellD.setCellValue(record.getR43_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 45 / Col C =====
					row = sheet.getRow(44);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR45_month_end() != null)
						cellC.setCellValue(record.getR45_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 45 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR45_average() != null)
						cellD.setCellValue(record.getR45_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 46 / Col C =====
					row = sheet.getRow(45);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR46_month_end() != null)
						cellC.setCellValue(record.getR46_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 46 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR46_average() != null)
						cellD.setCellValue(record.getR46_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 47 / Col C =====
					row = sheet.getRow(46);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR47_month_end() != null)
						cellC.setCellValue(record.getR47_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 47 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR47_average() != null)
						cellD.setCellValue(record.getR47_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 48 / Col C =====
					row = sheet.getRow(47);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR48_month_end() != null)
						cellC.setCellValue(record.getR48_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 48 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR48_average() != null)
						cellD.setCellValue(record.getR48_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 50 / Col C =====
					row = sheet.getRow(49);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR50_month_end() != null)
						cellC.setCellValue(record.getR50_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 50 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR50_average() != null)
						cellD.setCellValue(record.getR50_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 51 / Col C =====
					row = sheet.getRow(50);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR51_month_end() != null)
						cellC.setCellValue(record.getR51_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 51 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR51_average() != null)
						cellD.setCellValue(record.getR51_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 52 / Col C =====
					row = sheet.getRow(51);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR52_month_end() != null)
						cellC.setCellValue(record.getR52_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 52 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR52_average() != null)
						cellD.setCellValue(record.getR52_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 53 / Col C =====
					row = sheet.getRow(52);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR53_month_end() != null)
						cellC.setCellValue(record.getR53_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 53 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR53_average() != null)
						cellD.setCellValue(record.getR53_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 54 / Col C =====
					row = sheet.getRow(53);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR54_month_end() != null)
						cellC.setCellValue(record.getR54_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 54 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR54_average() != null)
						cellD.setCellValue(record.getR54_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 55 / Col C =====
					row = sheet.getRow(54);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR55_month_end() != null)
						cellC.setCellValue(record.getR55_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 55 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR55_average() != null)
						cellD.setCellValue(record.getR55_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 57 / Col C =====
					row = sheet.getRow(56);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR57_month_end() != null)
						cellC.setCellValue(record.getR57_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 57 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR57_average() != null)
						cellD.setCellValue(record.getR57_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 58 / Col C =====
					row = sheet.getRow(57);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR58_month_end() != null)
						cellC.setCellValue(record.getR58_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 58 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR58_average() != null)
						cellD.setCellValue(record.getR58_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 59 / Col C =====
					row = sheet.getRow(58);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR59_month_end() != null)
						cellC.setCellValue(record.getR59_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 59 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR59_average() != null)
						cellD.setCellValue(record.getR59_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					// ===== Row 60 / Col C =====
					row = sheet.getRow(59);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record.getR60_month_end() != null)
						cellC.setCellValue(record.getR60_month_end().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					// ===== Row 60 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record.getR60_average() != null)
						cellD.setCellValue(record.getR60_average().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

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
			logger.info("Generating Excel for BRRS_M_SFINP1 ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_SFINP1Details");
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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "ROWID", "COLUMNID",
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
			List<M_SFINP1_Archival_Detail_Entity> reportData = BRRS_M_SFINP1_Archival_Detail_Repo
					.getdatabydateList(parsedToDate, version);
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_SFINP1_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());
					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInPula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
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
				logger.info("No data found for M_SFINP1 — only header will be written.");
			}
			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();
		} catch (Exception e) {
			logger.error("Error generating M_SFINP1 Excel", e);
			return new byte[0];
		}
	}

	private Rectangle getPageSizeForColumns(int columnCount) {

		// Approx width per column (10 pts per column)
		float requiredWidth = columnCount * 55;

		if (requiredWidth <= PageSize.A4.getWidth())
			return PageSize.A4.rotate();

		if (requiredWidth <= PageSize.A3.getWidth())
			return PageSize.A3.rotate();

		if (requiredWidth <= PageSize.A2.getWidth())
			return PageSize.A2.rotate();

		if (requiredWidth <= PageSize.A1.getWidth())
			return PageSize.A1.rotate();

		if (requiredWidth <= PageSize.A0.getWidth())
			return PageSize.A0.rotate();

		// If still bigger → custom large canvas
		return new Rectangle(requiredWidth + 100, PageSize.A0.getHeight()).rotate();
	}

	private int findLastUsedColumn(Sheet sheet) {
		int lastCol = 0;
		for (int r = 0; r <= sheet.getLastRowNum(); r++) {
			Row row = sheet.getRow(r);
			if (row == null)
				continue;

			short lastCell = row.getLastCellNum();
			if (lastCell > lastCol) {
				// Check if there is at least one non-empty cell
				for (int c = lastCol; c < lastCell; c++) {
					Cell cell = row.getCell(c);
					if (cell != null && cell.getCellType() != CellType.BLANK && !cell.toString().trim().isEmpty()) {
						lastCol = lastCell;
						break;
					}
				}
			}
		}
		return lastCol;
	}

	private int getUsedColumnCount(Sheet sheet) {
		int maxCol = 0;

		for (Row row : sheet) {
			if (row == null)
				continue;
			if (row.getLastCellNum() > maxCol) {
				maxCol = row.getLastCellNum();
			}
		}

		// Now remove trailing blank columns
		boolean columnUsed;

		for (int col = maxCol - 1; col >= 0; col--) {
			columnUsed = false;
			for (Row row : sheet) {
				Cell cell = (row == null) ? null : row.getCell(col);
				if (cell != null && cell.getCellType() != CellType.BLANK
						&& !new DataFormatter().formatCellValue(cell).trim().isEmpty()) {
					columnUsed = true;
					break;
				}
			}
			if (columnUsed) {
				return col + 1; // This is last actual column
			}
		}

		return 1; // Fallback
	}

	private BaseColor toBaseColor(Color excelColor) {
		if (excelColor == null)
			return BaseColor.WHITE;
		return new BaseColor(excelColor.getRed(), excelColor.getGreen(), excelColor.getBlue());
	}

	/**
	 * Compatibility-safe applyCellStyleToPdf. Works with POI 3.x (old constants)
	 * and POI 4.x/5.x (enums) via reflection.
	 */
	private void applyCellStyleToPdf(Cell excelCell, PdfPCell pdfCell, Workbook workbook) {
		if (excelCell == null)
			return;
		CellStyle style = excelCell.getCellStyle();
		if (style == null)
			return;

		// ===== Background color (XSSF) =====
		try {
			if (workbook instanceof XSSFWorkbook) {
				XSSFCellStyle xssfStyle = (XSSFCellStyle) style;
				XSSFColor bg = xssfStyle.getFillForegroundXSSFColor();
				if (bg != null && bg.getRGB() != null) {
					byte[] rgb = bg.getRGB();
					pdfCell.setBackgroundColor(new BaseColor(rgb[0] & 0xFF, rgb[1] & 0xFF, rgb[2] & 0xFF));
				}
			}
		} catch (Throwable t) {
			// ignore background color extraction errors
		}

		// ===== Alignment (use reflection to support both old and new POI) =====
		int pdfAlignment = Element.ALIGN_LEFT;
		try {
			java.lang.reflect.Method m = style.getClass().getMethod("getAlignment");
			Object alignVal = m.invoke(style);
			if (alignVal instanceof Short) {
				short s = ((Short) alignVal).shortValue();
				// Old POI short codes: 1 = LEFT, 2 = CENTER, 3 = RIGHT (common mapping)
				if (s == 2)
					pdfAlignment = Element.ALIGN_CENTER;
				else if (s == 3)
					pdfAlignment = Element.ALIGN_RIGHT;
				else
					pdfAlignment = Element.ALIGN_LEFT;
			} else if (alignVal != null) {
				// New POI: enum HorizontalAlignment (toString -> "CENTER"/"RIGHT"/"LEFT")
				String name = alignVal.toString();
				if ("CENTER".equalsIgnoreCase(name))
					pdfAlignment = Element.ALIGN_CENTER;
				else if ("RIGHT".equalsIgnoreCase(name))
					pdfAlignment = Element.ALIGN_RIGHT;
				else
					pdfAlignment = Element.ALIGN_LEFT;
			}
		} catch (Throwable t) {
			// fallback to left alignment
			pdfAlignment = Element.ALIGN_LEFT;
		}
		pdfCell.setHorizontalAlignment(pdfAlignment);

		// ===== Borders (reflection: support short or enum) =====
		boolean leftBorder = false, rightBorder = false, topBorder = false, bottomBorder = false;
		try {
			leftBorder = borderPresent(style, "Left");
			rightBorder = borderPresent(style, "Right");
			topBorder = borderPresent(style, "Top");
			bottomBorder = borderPresent(style, "Bottom");
		} catch (Throwable t) {
			// ignore -> keep false defaults
		}
		pdfCell.setBorderWidthLeft(leftBorder ? 0.8f : 0f);
		pdfCell.setBorderWidthRight(rightBorder ? 0.8f : 0f);
		pdfCell.setBorderWidthTop(topBorder ? 0.8f : 0f);
		pdfCell.setBorderWidthBottom(bottomBorder ? 0.8f : 0f);

		// ===== Font extraction =====
		org.apache.poi.ss.usermodel.Font excelFont = null;
		try {
			excelFont = workbook.getFontAt(style.getFontIndex());
		} catch (Throwable t) {
			// fallback: create a default workbook font
			try {
				excelFont = workbook.createFont();
			} catch (Throwable ignored) {
			}
		}

		BaseColor fontColor = BaseColor.BLACK;
		int fontSize = 9;
		boolean isBold = false;

		if (excelFont != null) {
			try {
				fontSize = excelFont.getFontHeightInPoints();
				if (fontSize <= 0)
					fontSize = 9;
			} catch (Throwable ignored) {
				fontSize = 9;
			}
			try {
				isBold = excelFont.getBold();
			} catch (Throwable ignored) {
				isBold = false;
			}

			// try to extract XSSF font color (if available)
			try {
				if (excelFont instanceof XSSFFont) {
					XSSFFont xssfFont = (XSSFFont) excelFont;
					XSSFColor xColor = xssfFont.getXSSFColor();
					if (xColor != null && xColor.getRGB() != null) {
						byte[] rgb = xColor.getRGB();
						fontColor = new BaseColor(rgb[0] & 0xFF, rgb[1] & 0xFF, rgb[2] & 0xFF);
					}
				}
			} catch (Throwable ignored) {
			}
		}

		// ===== Create iText font (iText 5-style) =====
		com.itextpdf.text.Font pdfFont;
		try {
			pdfFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, fontSize,
					isBold ? com.itextpdf.text.Font.BOLD : com.itextpdf.text.Font.NORMAL, fontColor);
		} catch (Throwable tt) {
			// fallback font if HELEVETICA constant not available
			pdfFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, fontSize,
					isBold ? com.itextpdf.text.Font.BOLD : com.itextpdf.text.Font.NORMAL, fontColor);
		}

		// ===== Phrase safe set (old iText constructors compatibility) =====
		String text = "";
		Phrase existing = pdfCell.getPhrase();
		if (existing != null) {
			try {
				text = existing.getContent();
			} catch (Throwable ignored) {
				text = "";
			}
		}
		Phrase phrase = new Phrase(text, pdfFont);
		phrase.setLeading(pdfFont.getSize() + 2); // proper line spacing
		pdfCell.setPhrase(phrase);
		pdfCell.setNoWrap(false); // always wrap
		pdfCell.setPadding(4f); // spacing so text doesn’t collide

		// ===== Wrap text =====
		try {
			pdfCell.setNoWrap(false);
		} catch (Throwable t) {
			// ignore
		}
	}

	/**
	 * Helper used by applyCellStyleToPdf to discover whether a border exists
	 * (Left/Right/Top/Bottom).
	 */
	private boolean borderPresent(CellStyle style, String which) {
		// which should be "Left", "Right", "Top", or "Bottom"
		try {
			java.lang.reflect.Method m = style.getClass().getMethod("getBorder" + which);
			Object val = m.invoke(style);
			if (val == null)
				return false;
			if (val instanceof Short) {
				short s = ((Short) val).shortValue();
				return s != 0; // old POI: 0 means BORDER_NONE
			} else {
				// new POI: enum BorderStyle, e.g., NONE, THIN, etc.
				String name = val.toString();
				return !"NONE".equalsIgnoreCase(name);
			}
		} catch (NoSuchMethodException nsme) {
			// try alternative new-style method names (getBorderLeftEnum) for some versions
			try {
				java.lang.reflect.Method m2 = style.getClass().getMethod("getBorder" + which + "Enum");
				Object val2 = m2.invoke(style);
				if (val2 == null)
					return false;
				return !"NONE".equalsIgnoreCase(val2.toString());
			} catch (Throwable t) {
				return false;
			}
		} catch (Throwable t) {
			return false;
		}
	}

	public byte[] convertExcelBytesToPdf(byte[] excelBytes) throws Exception {

		try (InputStream inputStream = new ByteArrayInputStream(excelBytes);
				Workbook workbook = WorkbookFactory.create(inputStream);
				ByteArrayOutputStream pdfOut = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

			// Determine number of columns
			int colCount = getUsedColumnCount(sheet);
			System.out.println("Final usable column count = " + colCount);

			// Get dynamic page size
			Rectangle pageSize = getPageSizeForColumns(colCount);

			Document document = new Document(pageSize, 20, 20, 20, 20);
			PdfWriter.getInstance(document, pdfOut);
			document.open();

			PdfPTable table = new PdfPTable(colCount);
			table.setWidthPercentage(100);

			// Auto column width
			float[] widths = new float[colCount];
			for (int i = 0; i < colCount; i++) {
				int excelWidth = sheet.getColumnWidth(i);

				// Prevent ultra-small or ultra-big widths
				widths[i] = Math.max(50f, Math.min(excelWidth / 30f, 300f));
			}
			table.setWidths(widths);

			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			DataFormatter formatter = new DataFormatter();

			for (Row row : sheet) {
				if (row == null)
					continue;

				for (int i = 0; i < colCount; i++) {
					Cell cell = row.getCell(i);
					String value = formatter.formatCellValue(cell, evaluator);
					PdfPCell pdfCell = new PdfPCell(new Phrase(value));
					applyCellStyleToPdf(cell, pdfCell, workbook);
					pdfCell.setPadding(4);
					table.addCell(pdfCell);

				}
			}

			document.add(table);
			document.close();
			return pdfOut.toByteArray();
		}
	}
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/M_SFINP1"); // ✅ match the report name
	    System.out.println("Hello");
	    if (acctNo != null) {
	    	M_SFINP1_Detail_Entity la1Entity = BRRS_M_SFINP1_Detail_Repo.findByAcctnumber(acctNo);
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
	    ModelAndView mv = new ModelAndView("BRRS/M_SFINP1"); // ✅ match the report name

	    if (acctNo != null) {
	        M_SFINP1_Detail_Entity la1Entity = BRRS_M_SFINP1_Detail_Repo.findByAcctnumber(acctNo);
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
	        String provisionStr = request.getParameter("acctBalanceInPula");
	        String acctName = request.getParameter("acctName");
	        String reportDateStr = request.getParameter("reportDate");

	        logger.info("Received update for ACCT_NO: {}", acctNo);

	        M_SFINP1_Detail_Entity existing = BRRS_M_SFINP1_Detail_Repo.findByAcctnumber(acctNo);
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
	            if (existing.getAcctBalanceInPula() == null ||
	                existing.getAcctBalanceInPula().compareTo(newProvision) != 0) {
	                existing.setAcctBalanceInPula(newProvision);
	                isChanged = true;
	                logger.info("Balance updated to {}", newProvision);
	            }
	        }
	        
	        

	        if (isChanged) {
	        	BRRS_M_SFINP1_Detail_Repo.save(existing);
	            logger.info("Record updated successfully for account {}", acctNo);

	            // Format date for procedure
	            String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
	                    .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

	            // Run summary procedure after commit
	            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
	                @Override
	                public void afterCommit() {
	                    try {
	                        logger.info("Transaction committed — calling BRRS_M_SFINP1_SUMMARY_PROCEDURE({})",
	                                formattedDate);
	                        jdbcTemplate.update("BEGIN BRRS_M_SFINP1_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
	        logger.error("Error updating M_CA2 record", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error updating record: " + e.getMessage());
	    }
	}

}
