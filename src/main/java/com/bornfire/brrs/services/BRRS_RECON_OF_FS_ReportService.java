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

import com.bornfire.brrs.entities.BRRS_Recon_Of_FS_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Recon_Of_FS_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_DEP3_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_Recon_Of_FS_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Recon_Of_FS_Summary_Repo;
import com.bornfire.brrs.entities.Recon_Of_FS_Archival_Detail_Entity;
import com.bornfire.brrs.entities.Recon_Of_FS_Achival_Summary_Entity;
import com.bornfire.brrs.entities.Recon_Of_FS_Detail_Entity;
import com.bornfire.brrs.entities.Recon_Of_FS_Summary_Entity;

@Component
@Service

public class BRRS_RECON_OF_FS_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_RECON_OF_FS_ReportService.class);

	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	private Environment env;
	@Autowired
	BRRS_Recon_Of_FS_Detail_Repo Recon_Of_FS_Detail_Repo;

	@Autowired
	BRRS_Recon_Of_FS_Summary_Repo Recon_Of_FS_Summary_Repo;

	@Autowired
	BRRS_Recon_Of_FS_Archival_Detail_Repo Recon_Of_FS_Archival_Detail_Repo;

	@Autowired
	BRRS_Recon_Of_FS_Archival_Summary_Repo Recon_Of_FS_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRRS_Recon_Of_FS_View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, String version) {
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		System.out.println("testing");
		System.out.println(version);

		if (type.equals("ARCHIVAL") & version != null) {
			System.out.println(type);
			List<Recon_Of_FS_Achival_Summary_Entity> T1Master = new ArrayList<Recon_Of_FS_Achival_Summary_Entity>();
			System.out.println(version);
			try {
				Date d1 = dateformat.parse(todate);
				T1Master = Recon_Of_FS_Archival_Summary_Repo.getdatabydateListarchival(todate, version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} else {
			List<Recon_Of_FS_Summary_Entity> T1Master = new ArrayList<Recon_Of_FS_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);

				T1Master = Recon_Of_FS_Summary_Repo.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		mv.setViewName("BRRS/Recon_Of_FS");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

	public ModelAndView getBRRS_Recon_Of_FScurrentDtl(String reportId, String fromdate, String todate, String currency,
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
				List<Recon_Of_FS_Archival_Detail_Entity> T1Dt1;
				if (reportLabel != null && reportAddlCriteria1 != null) {
					T1Dt1 = Recon_Of_FS_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1,
							parsedDate, version);
				} else {
					T1Dt1 = Recon_Of_FS_Archival_Detail_Repo.getdatabydateList(todate, version);
					totalPages = Recon_Of_FS_Detail_Repo.getdatacount(parsedDate);
					System.out.println(T1Dt1.size());
					mv.addObject("pagination", "YES");
				}
				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
			} else {

				// 🔹 Current branch
				List<Recon_Of_FS_Detail_Entity> T1Dt1;
				if (reportLabel != null && reportAddlCriteria1 != null) {
					T1Dt1 = Recon_Of_FS_Detail_Repo.GetDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1,
							parsedDate);
					System.out.println(T1Dt1.size());
				} else {
					T1Dt1 = Recon_Of_FS_Detail_Repo.getdatabydateList(parsedDate, currentPage, pageSize);
					System.out.println(T1Dt1.size());
					totalPages = Recon_Of_FS_Detail_Repo.getdatacount(parsedDate);
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
		mv.setViewName("BRRS/Recon_Of_FS");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}

	public byte[] getRecon_Of_FSExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		System.out.println(type + "   " + version);
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getSummaryExcelARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);

		}

		// Fetch data
		List<Recon_Of_FS_Summary_Entity> dataList = Recon_Of_FS_Summary_Repo
				.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Recon_Of_FS report. Returning empty result.");
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
					Recon_Of_FS_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					row = sheet.getRow(6);
					// ================= R7 =================

					Cell R7cell2 = row.createCell(1);
					if (record.getR7_BAL_SHEET_PUB_FS() != null) {
						R7cell2.setCellValue(record.getR7_BAL_SHEET_PUB_FS().doubleValue());
						R7cell2.setCellStyle(numberStyle);
					} else {
						R7cell2.setCellValue("");
						R7cell2.setCellStyle(textStyle);
					}
					Cell R7cell3 = row.createCell(2);
					if (record.getR7_UNDER_REG_SOC() != null) {
						R7cell3.setCellValue(record.getR7_UNDER_REG_SOC().doubleValue());
						R7cell3.setCellStyle(numberStyle);
					} else {
						R7cell3.setCellValue("");
						R7cell3.setCellStyle(textStyle);
					}

					// ================= R8 =================
					row = sheet.getRow(7);
					Cell R8cell2 = row.createCell(1);
					if (record.getR8_BAL_SHEET_PUB_FS() != null) {
						R8cell2.setCellValue(record.getR8_BAL_SHEET_PUB_FS().doubleValue());
						R8cell2.setCellStyle(numberStyle);
					} else {
						R8cell2.setCellValue("");
						R8cell2.setCellStyle(textStyle);
					}
					Cell R8cell3 = row.createCell(2);
					if (record.getR8_UNDER_REG_SOC() != null) {
						R8cell3.setCellValue(record.getR8_UNDER_REG_SOC().doubleValue());
						R8cell3.setCellStyle(numberStyle);
					} else {
						R8cell3.setCellValue("");
						R8cell3.setCellStyle(textStyle);
					}

					// ================= R9 =================
					row = sheet.getRow(8);
					Cell R9cell2 = row.createCell(1);
					if (record.getR9_BAL_SHEET_PUB_FS() != null) {
						R9cell2.setCellValue(record.getR9_BAL_SHEET_PUB_FS().doubleValue());
						R9cell2.setCellStyle(numberStyle);
					} else {
						R9cell2.setCellValue("");
						R9cell2.setCellStyle(textStyle);
					}
					Cell R9cell3 = row.createCell(2);
					if (record.getR9_UNDER_REG_SOC() != null) {
						R9cell3.setCellValue(record.getR9_UNDER_REG_SOC().doubleValue());
						R9cell3.setCellStyle(numberStyle);
					} else {
						R9cell3.setCellValue("");
						R9cell3.setCellStyle(textStyle);
					}

					// ================= R10 =================
					row = sheet.getRow(9);
					Cell R10cell2 = row.createCell(1);
					if (record.getR10_BAL_SHEET_PUB_FS() != null) {
						R10cell2.setCellValue(record.getR10_BAL_SHEET_PUB_FS().doubleValue());
						R10cell2.setCellStyle(numberStyle);
					} else {
						R10cell2.setCellValue("");
						R10cell2.setCellStyle(textStyle);
					}
					Cell R10cell3 = row.createCell(2);
					if (record.getR10_UNDER_REG_SOC() != null) {
						R10cell3.setCellValue(record.getR10_UNDER_REG_SOC().doubleValue());
						R10cell3.setCellStyle(numberStyle);
					} else {
						R10cell3.setCellValue("");
						R10cell3.setCellStyle(textStyle);
					}

					// ================= R11 =================
					row = sheet.getRow(10);
					Cell R11cell2 = row.createCell(1);
					if (record.getR11_BAL_SHEET_PUB_FS() != null) {
						R11cell2.setCellValue(record.getR11_BAL_SHEET_PUB_FS().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}
					Cell R11cell3 = row.createCell(2);
					if (record.getR11_UNDER_REG_SOC() != null) {
						R11cell3.setCellValue(record.getR11_UNDER_REG_SOC().doubleValue());
						R11cell3.setCellStyle(numberStyle);
					} else {
						R11cell3.setCellValue("");
						R11cell3.setCellStyle(textStyle);
					}

					// ================= R12 =================
					row = sheet.getRow(11);
					Cell R12cell2 = row.createCell(1);
					if (record.getR12_BAL_SHEET_PUB_FS() != null) {
						R12cell2.setCellValue(record.getR12_BAL_SHEET_PUB_FS().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}
					Cell R12cell3 = row.createCell(2);
					if (record.getR12_UNDER_REG_SOC() != null) {
						R12cell3.setCellValue(record.getR12_UNDER_REG_SOC().doubleValue());
						R12cell3.setCellStyle(numberStyle);
					} else {
						R12cell3.setCellValue("");
						R12cell3.setCellStyle(textStyle);
					}

					// ================= R13 =================
					row = sheet.getRow(12);
					Cell R13cell2 = row.createCell(1);
					if (record.getR13_BAL_SHEET_PUB_FS() != null) {
						R13cell2.setCellValue(record.getR13_BAL_SHEET_PUB_FS().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}
					Cell R13cell3 = row.createCell(2);
					if (record.getR13_UNDER_REG_SOC() != null) {
						R13cell3.setCellValue(record.getR13_UNDER_REG_SOC().doubleValue());
						R13cell3.setCellStyle(numberStyle);
					} else {
						R13cell3.setCellValue("");
						R13cell3.setCellStyle(textStyle);
					}

					// ================= R14 =================
					row = sheet.getRow(13);
					Cell R14cell2 = row.createCell(1);
					if (record.getR14_BAL_SHEET_PUB_FS() != null) {
						R14cell2.setCellValue(record.getR14_BAL_SHEET_PUB_FS().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}
					Cell R14cell3 = row.createCell(2);
					if (record.getR14_UNDER_REG_SOC() != null) {
						R14cell3.setCellValue(record.getR14_UNDER_REG_SOC().doubleValue());
						R14cell3.setCellStyle(numberStyle);
					} else {
						R14cell3.setCellValue("");
						R14cell3.setCellStyle(textStyle);
					}

					// ================= R15 =================
					row = sheet.getRow(14);
					Cell R15cell2 = row.createCell(1);
					if (record.getR15_BAL_SHEET_PUB_FS() != null) {
						R15cell2.setCellValue(record.getR15_BAL_SHEET_PUB_FS().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);
					}
					Cell R15cell3 = row.createCell(2);
					if (record.getR15_UNDER_REG_SOC() != null) {
						R15cell3.setCellValue(record.getR15_UNDER_REG_SOC().doubleValue());
						R15cell3.setCellStyle(numberStyle);
					} else {
						R15cell3.setCellValue("");
						R15cell3.setCellStyle(textStyle);
					}

					// ================= R16 =================
					row = sheet.getRow(15);
					Cell R16cell2 = row.createCell(1);
					if (record.getR16_BAL_SHEET_PUB_FS() != null) {
						R16cell2.setCellValue(record.getR16_BAL_SHEET_PUB_FS().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);
					}
					Cell R16cell3 = row.createCell(2);
					if (record.getR16_UNDER_REG_SOC() != null) {
						R16cell3.setCellValue(record.getR16_UNDER_REG_SOC().doubleValue());
						R16cell3.setCellStyle(numberStyle);
					} else {
						R16cell3.setCellValue("");
						R16cell3.setCellStyle(textStyle);
					}

					// ================= R17 =================
					row = sheet.getRow(16);
					Cell R17cell2 = row.createCell(1);
					if (record.getR17_BAL_SHEET_PUB_FS() != null) {
						R17cell2.setCellValue(record.getR17_BAL_SHEET_PUB_FS().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);
					}
					Cell R17cell3 = row.createCell(2);
					if (record.getR17_UNDER_REG_SOC() != null) {
						R17cell3.setCellValue(record.getR17_UNDER_REG_SOC().doubleValue());
						R17cell3.setCellStyle(numberStyle);
					} else {
						R17cell3.setCellValue("");
						R17cell3.setCellStyle(textStyle);
					}

					// ================= R18 =================
					row = sheet.getRow(17);
					Cell R18cell2 = row.createCell(1);
					if (record.getR18_BAL_SHEET_PUB_FS() != null) {
						R18cell2.setCellValue(record.getR18_BAL_SHEET_PUB_FS().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);
					}
					Cell R18cell3 = row.createCell(2);
					if (record.getR18_UNDER_REG_SOC() != null) {
						R18cell3.setCellValue(record.getR18_UNDER_REG_SOC().doubleValue());
						R18cell3.setCellStyle(numberStyle);
					} else {
						R18cell3.setCellValue("");
						R18cell3.setCellStyle(textStyle);
					}

					// ================= R19 =================
					row = sheet.getRow(18);
					Cell R19cell2 = row.createCell(1);
					if (record.getR19_BAL_SHEET_PUB_FS() != null) {
						R19cell2.setCellValue(record.getR19_BAL_SHEET_PUB_FS().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);
					}
					Cell R19cell3 = row.createCell(2);
					if (record.getR19_UNDER_REG_SOC() != null) {
						R19cell3.setCellValue(record.getR19_UNDER_REG_SOC().doubleValue());
						R19cell3.setCellStyle(numberStyle);
					} else {
						R19cell3.setCellValue("");
						R19cell3.setCellStyle(textStyle);
					}

					// ================= R20 =================
					row = sheet.getRow(19);
					Cell R20cell2 = row.createCell(1);
					if (record.getR20_BAL_SHEET_PUB_FS() != null) {
						R20cell2.setCellValue(record.getR20_BAL_SHEET_PUB_FS().doubleValue());
						R20cell2.setCellStyle(numberStyle);
					} else {
						R20cell2.setCellValue("");
						R20cell2.setCellStyle(textStyle);
					}
					Cell R20cell3 = row.createCell(2);
					if (record.getR20_UNDER_REG_SOC() != null) {
						R20cell3.setCellValue(record.getR20_UNDER_REG_SOC().doubleValue());
						R20cell3.setCellStyle(numberStyle);
					} else {
						R20cell3.setCellValue("");
						R20cell3.setCellStyle(textStyle);
					}

					// ================= R21 =================
					row = sheet.getRow(20);
					Cell R21cell2 = row.createCell(1);
					if (record.getR21_BAL_SHEET_PUB_FS() != null) {
						R21cell2.setCellValue(record.getR21_BAL_SHEET_PUB_FS().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);
					}
					Cell R21cell3 = row.createCell(2);
					if (record.getR21_UNDER_REG_SOC() != null) {
						R21cell3.setCellValue(record.getR21_UNDER_REG_SOC().doubleValue());
						R21cell3.setCellStyle(numberStyle);
					} else {
						R21cell3.setCellValue("");
						R21cell3.setCellStyle(textStyle);
					}

					// ================= R23 =================
					row = sheet.getRow(22);
					Cell R23cell2 = row.createCell(1);
					if (record.getR23_BAL_SHEET_PUB_FS() != null) {
						R23cell2.setCellValue(record.getR23_BAL_SHEET_PUB_FS().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}
					Cell R23cell3 = row.createCell(2);
					if (record.getR23_UNDER_REG_SOC() != null) {
						R23cell3.setCellValue(record.getR23_UNDER_REG_SOC().doubleValue());
						R23cell3.setCellStyle(numberStyle);
					} else {
						R23cell3.setCellValue("");
						R23cell3.setCellStyle(textStyle);
					}

					// ================= R24 =================
					row = sheet.getRow(23);
					Cell R24cell2 = row.createCell(1);
					if (record.getR24_BAL_SHEET_PUB_FS() != null) {
						R24cell2.setCellValue(record.getR24_BAL_SHEET_PUB_FS().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}
					Cell R24cell3 = row.createCell(2);
					if (record.getR24_UNDER_REG_SOC() != null) {
						R24cell3.setCellValue(record.getR24_UNDER_REG_SOC().doubleValue());
						R24cell3.setCellStyle(numberStyle);
					} else {
						R24cell3.setCellValue("");
						R24cell3.setCellStyle(textStyle);
					}

					// ================= R25 =================
					row = sheet.getRow(24);
					Cell R25cell2 = row.createCell(1);
					if (record.getR25_BAL_SHEET_PUB_FS() != null) {
						R25cell2.setCellValue(record.getR25_BAL_SHEET_PUB_FS().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}
					Cell R25cell3 = row.createCell(2);
					if (record.getR25_UNDER_REG_SOC() != null) {
						R25cell3.setCellValue(record.getR25_UNDER_REG_SOC().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}

					// ================= R26 =================
					row = sheet.getRow(25);
					Cell R26cell2 = row.createCell(1);
					if (record.getR26_BAL_SHEET_PUB_FS() != null) {
						R26cell2.setCellValue(record.getR26_BAL_SHEET_PUB_FS().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}
					Cell R26cell3 = row.createCell(2);
					if (record.getR26_UNDER_REG_SOC() != null) {
						R26cell3.setCellValue(record.getR26_UNDER_REG_SOC().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}

					// ================= R27 =================
					row = sheet.getRow(26);
					Cell R27cell2 = row.createCell(1);
					if (record.getR27_BAL_SHEET_PUB_FS() != null) {
						R27cell2.setCellValue(record.getR27_BAL_SHEET_PUB_FS().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}
					Cell R27cell3 = row.createCell(2);
					if (record.getR27_UNDER_REG_SOC() != null) {
						R27cell3.setCellValue(record.getR27_UNDER_REG_SOC().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);
					}

					// ================= R28 =================
					row = sheet.getRow(27);
					Cell R28cell2 = row.createCell(1);
					if (record.getR28_BAL_SHEET_PUB_FS() != null) {
						R28cell2.setCellValue(record.getR28_BAL_SHEET_PUB_FS().doubleValue());
						R28cell2.setCellStyle(numberStyle);
					} else {
						R28cell2.setCellValue("");
						R28cell2.setCellStyle(textStyle);
					}
					Cell R28cell3 = row.createCell(2);
					if (record.getR28_UNDER_REG_SOC() != null) {
						R28cell3.setCellValue(record.getR28_UNDER_REG_SOC().doubleValue());
						R28cell3.setCellStyle(numberStyle);
					} else {
						R28cell3.setCellValue("");
						R28cell3.setCellStyle(textStyle);
					}

					// ================= R29 =================
					row = sheet.getRow(28);
					Cell R29cell2 = row.createCell(1);
					if (record.getR29_BAL_SHEET_PUB_FS() != null) {
						R29cell2.setCellValue(record.getR29_BAL_SHEET_PUB_FS().doubleValue());
						R29cell2.setCellStyle(numberStyle);
					} else {
						R29cell2.setCellValue("");
						R29cell2.setCellStyle(textStyle);
					}
					Cell R29cell3 = row.createCell(2);
					if (record.getR29_UNDER_REG_SOC() != null) {
						R29cell3.setCellValue(record.getR29_UNDER_REG_SOC().doubleValue());
						R29cell3.setCellStyle(numberStyle);
					} else {
						R29cell3.setCellValue("");
						R29cell3.setCellStyle(textStyle);
					}

					// ================= R30 =================
					row = sheet.getRow(29);
					Cell R30cell2 = row.createCell(1);
					if (record.getR30_BAL_SHEET_PUB_FS() != null) {
						R30cell2.setCellValue(record.getR30_BAL_SHEET_PUB_FS().doubleValue());
						R30cell2.setCellStyle(numberStyle);
					} else {
						R30cell2.setCellValue("");
						R30cell2.setCellStyle(textStyle);
					}
					Cell R30cell3 = row.createCell(2);
					if (record.getR30_UNDER_REG_SOC() != null) {
						R30cell3.setCellValue(record.getR30_UNDER_REG_SOC().doubleValue());
						R30cell3.setCellStyle(numberStyle);
					} else {
						R30cell3.setCellValue("");
						R30cell3.setCellStyle(textStyle);
					}

					// ================= R31 =================
					row = sheet.getRow(30);
					Cell R31cell2 = row.createCell(1);
					if (record.getR31_BAL_SHEET_PUB_FS() != null) {
						R31cell2.setCellValue(record.getR31_BAL_SHEET_PUB_FS().doubleValue());
						R31cell2.setCellStyle(numberStyle);
					} else {
						R31cell2.setCellValue("");
						R31cell2.setCellStyle(textStyle);
					}
					Cell R31cell3 = row.createCell(2);
					if (record.getR31_UNDER_REG_SOC() != null) {
						R31cell3.setCellValue(record.getR31_UNDER_REG_SOC().doubleValue());
						R31cell3.setCellStyle(numberStyle);
					} else {
						R31cell3.setCellValue("");
						R31cell3.setCellStyle(textStyle);
					}

					// ================= R32 =================
					row = sheet.getRow(31);
					Cell R32cell2 = row.createCell(1);
					if (record.getR32_BAL_SHEET_PUB_FS() != null) {
						R32cell2.setCellValue(record.getR32_BAL_SHEET_PUB_FS().doubleValue());
						R32cell2.setCellStyle(numberStyle);
					} else {
						R32cell2.setCellValue("");
						R32cell2.setCellStyle(textStyle);
					}
					Cell R32cell3 = row.createCell(2);
					if (record.getR32_UNDER_REG_SOC() != null) {
						R32cell3.setCellValue(record.getR32_UNDER_REG_SOC().doubleValue());
						R32cell3.setCellStyle(numberStyle);
					} else {
						R32cell3.setCellValue("");
						R32cell3.setCellStyle(textStyle);
					}

					// ================= R33 =================
					row = sheet.getRow(32);
					Cell R33cell2 = row.createCell(1);
					if (record.getR33_BAL_SHEET_PUB_FS() != null) {
						R33cell2.setCellValue(record.getR33_BAL_SHEET_PUB_FS().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}
					Cell R33cell3 = row.createCell(2);
					if (record.getR33_UNDER_REG_SOC() != null) {
						R33cell3.setCellValue(record.getR33_UNDER_REG_SOC().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

					// ================= R34 =================
					row = sheet.getRow(33);
					Cell R34cell2 = row.createCell(1);
					if (record.getR34_BAL_SHEET_PUB_FS() != null) {
						R34cell2.setCellValue(record.getR34_BAL_SHEET_PUB_FS().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}
					Cell R34cell3 = row.createCell(2);
					if (record.getR34_UNDER_REG_SOC() != null) {
						R34cell3.setCellValue(record.getR34_UNDER_REG_SOC().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}

					// ================= R35 =================
					row = sheet.getRow(34);
					Cell R35cell2 = row.createCell(1);
					if (record.getR35_BAL_SHEET_PUB_FS() != null) {
						R35cell2.setCellValue(record.getR35_BAL_SHEET_PUB_FS().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}
					Cell R35cell3 = row.createCell(2);
					if (record.getR35_UNDER_REG_SOC() != null) {
						R35cell3.setCellValue(record.getR35_UNDER_REG_SOC().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}

					// ================= R36 =================
					row = sheet.getRow(35);
					Cell R36cell2 = row.createCell(1);
					if (record.getR36_BAL_SHEET_PUB_FS() != null) {
						R36cell2.setCellValue(record.getR36_BAL_SHEET_PUB_FS().doubleValue());
						R36cell2.setCellStyle(numberStyle);
					} else {
						R36cell2.setCellValue("");
						R36cell2.setCellStyle(textStyle);
					}
					Cell R36cell3 = row.createCell(2);
					if (record.getR36_UNDER_REG_SOC() != null) {
						R36cell3.setCellValue(record.getR36_UNDER_REG_SOC().doubleValue());
						R36cell3.setCellStyle(numberStyle);
					} else {
						R36cell3.setCellValue("");
						R36cell3.setCellStyle(textStyle);
					}

					// ================= R38 =================
					row = sheet.getRow(37);
					Cell R38cell2 = row.createCell(1);
					if (record.getR38_BAL_SHEET_PUB_FS() != null) {
						R38cell2.setCellValue(record.getR38_BAL_SHEET_PUB_FS().doubleValue());
						R38cell2.setCellStyle(numberStyle);
					} else {
						R38cell2.setCellValue("");
						R38cell2.setCellStyle(textStyle);
					}
					Cell R38cell3 = row.createCell(2);
					if (record.getR38_UNDER_REG_SOC() != null) {
						R38cell3.setCellValue(record.getR38_UNDER_REG_SOC().doubleValue());
						R38cell3.setCellStyle(numberStyle);
					} else {
						R38cell3.setCellValue("");
						R38cell3.setCellStyle(textStyle);
					}

					// ================= R39 =================
					row = sheet.getRow(38);
					Cell R39cell2 = row.createCell(1);
					if (record.getR39_BAL_SHEET_PUB_FS() != null) {
						R39cell2.setCellValue(record.getR39_BAL_SHEET_PUB_FS().doubleValue());
						R39cell2.setCellStyle(numberStyle);
					} else {
						R39cell2.setCellValue("");
						R39cell2.setCellStyle(textStyle);
					}
					Cell R39cell3 = row.createCell(2);
					if (record.getR39_UNDER_REG_SOC() != null) {
						R39cell3.setCellValue(record.getR39_UNDER_REG_SOC().doubleValue());
						R39cell3.setCellStyle(numberStyle);
					} else {
						R39cell3.setCellValue("");
						R39cell3.setCellStyle(textStyle);
					}

					// ================= R40 =================
					row = sheet.getRow(39);
					Cell R40cell2 = row.createCell(1);
					if (record.getR40_BAL_SHEET_PUB_FS() != null) {
						R40cell2.setCellValue(record.getR40_BAL_SHEET_PUB_FS().doubleValue());
						R40cell2.setCellStyle(numberStyle);
					} else {
						R40cell2.setCellValue("");
						R40cell2.setCellStyle(textStyle);
					}
					Cell R40cell3 = row.createCell(2);
					if (record.getR40_UNDER_REG_SOC() != null) {
						R40cell3.setCellValue(record.getR40_UNDER_REG_SOC().doubleValue());
						R40cell3.setCellStyle(numberStyle);
					} else {
						R40cell3.setCellValue("");
						R40cell3.setCellStyle(textStyle);
					}

					// ================= R41 =================
					row = sheet.getRow(40);
					Cell R41cell2 = row.createCell(1);
					if (record.getR41_BAL_SHEET_PUB_FS() != null) {
						R41cell2.setCellValue(record.getR41_BAL_SHEET_PUB_FS().doubleValue());
						R41cell2.setCellStyle(numberStyle);
					} else {
						R41cell2.setCellValue("");
						R41cell2.setCellStyle(textStyle);
					}
					Cell R41cell3 = row.createCell(2);
					if (record.getR41_UNDER_REG_SOC() != null) {
						R41cell3.setCellValue(record.getR41_UNDER_REG_SOC().doubleValue());
						R41cell3.setCellStyle(numberStyle);
					} else {
						R41cell3.setCellValue("");
						R41cell3.setCellStyle(textStyle);
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

	public byte[] BRRS_Recon_Of_FSDetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRSRecon_Of_FS Details...");
			System.out.println("came to Detail download service");
			System.out.println("Gopika");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRRS_Recon_Of_FSDetails");

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
			List<Recon_Of_FS_Detail_Entity> reportData = Recon_Of_FS_Detail_Repo.getdatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (Recon_Of_FS_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());
					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInPula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);
					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria1());
					row.createCell(6)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd/MM/yyyy").format(item.getReportDate())
									: "");
					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for BRRS_Recon_Of_FS — only header will be written.");
			}
			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();
		} catch (Exception e) {
			logger.error("Error generating BRRS_Q_Recon_Of_FS Excel", e);
			return new byte[0];
		}
	}

	public List<Object> getRecon_Of_FSArchival() {
		List<Object> Recon_Of_FSArchivallist = new ArrayList<>();
		try {
			Recon_Of_FSArchivallist = Recon_Of_FS_Archival_Summary_Repo.getRecon_Of_FSarchival();
			System.out.println("countser" + Recon_Of_FSArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching Recon_Of_FS Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return Recon_Of_FSArchivallist;
	}

	public byte[] getSummaryExcelARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<Recon_Of_FS_Achival_Summary_Entity> dataList = Recon_Of_FS_Archival_Summary_Repo
				.getdatabydateListarchival(todate, version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Recon_Of_FS report. Returning empty result.");
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
					Recon_Of_FS_Achival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					row = sheet.getRow(6);
					// ================= R7 =================

					Cell R7cell2 = row.createCell(1);
					if (record.getR7_BAL_SHEET_PUB_FS() != null) {
						R7cell2.setCellValue(record.getR7_BAL_SHEET_PUB_FS().doubleValue());
						R7cell2.setCellStyle(numberStyle);
					} else {
						R7cell2.setCellValue("");
						R7cell2.setCellStyle(textStyle);
					}
					Cell R7cell3 = row.createCell(2);
					if (record.getR7_UNDER_REG_SOC() != null) {
						R7cell3.setCellValue(record.getR7_UNDER_REG_SOC().doubleValue());
						R7cell3.setCellStyle(numberStyle);
					} else {
						R7cell3.setCellValue("");
						R7cell3.setCellStyle(textStyle);
					}

					// ================= R8 =================
					row = sheet.getRow(7);
					Cell R8cell2 = row.createCell(1);
					if (record.getR8_BAL_SHEET_PUB_FS() != null) {
						R8cell2.setCellValue(record.getR8_BAL_SHEET_PUB_FS().doubleValue());
						R8cell2.setCellStyle(numberStyle);
					} else {
						R8cell2.setCellValue("");
						R8cell2.setCellStyle(textStyle);
					}
					Cell R8cell3 = row.createCell(2);
					if (record.getR8_UNDER_REG_SOC() != null) {
						R8cell3.setCellValue(record.getR8_UNDER_REG_SOC().doubleValue());
						R8cell3.setCellStyle(numberStyle);
					} else {
						R8cell3.setCellValue("");
						R8cell3.setCellStyle(textStyle);
					}

					// ================= R9 =================
					row = sheet.getRow(8);
					Cell R9cell2 = row.createCell(1);
					if (record.getR9_BAL_SHEET_PUB_FS() != null) {
						R9cell2.setCellValue(record.getR9_BAL_SHEET_PUB_FS().doubleValue());
						R9cell2.setCellStyle(numberStyle);
					} else {
						R9cell2.setCellValue("");
						R9cell2.setCellStyle(textStyle);
					}
					Cell R9cell3 = row.createCell(2);
					if (record.getR9_UNDER_REG_SOC() != null) {
						R9cell3.setCellValue(record.getR9_UNDER_REG_SOC().doubleValue());
						R9cell3.setCellStyle(numberStyle);
					} else {
						R9cell3.setCellValue("");
						R9cell3.setCellStyle(textStyle);
					}

					// ================= R10 =================
					row = sheet.getRow(9);
					Cell R10cell2 = row.createCell(1);
					if (record.getR10_BAL_SHEET_PUB_FS() != null) {
						R10cell2.setCellValue(record.getR10_BAL_SHEET_PUB_FS().doubleValue());
						R10cell2.setCellStyle(numberStyle);
					} else {
						R10cell2.setCellValue("");
						R10cell2.setCellStyle(textStyle);
					}
					Cell R10cell3 = row.createCell(2);
					if (record.getR10_UNDER_REG_SOC() != null) {
						R10cell3.setCellValue(record.getR10_UNDER_REG_SOC().doubleValue());
						R10cell3.setCellStyle(numberStyle);
					} else {
						R10cell3.setCellValue("");
						R10cell3.setCellStyle(textStyle);
					}

					// ================= R11 =================
					row = sheet.getRow(10);
					Cell R11cell2 = row.createCell(1);
					if (record.getR11_BAL_SHEET_PUB_FS() != null) {
						R11cell2.setCellValue(record.getR11_BAL_SHEET_PUB_FS().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}
					Cell R11cell3 = row.createCell(2);
					if (record.getR11_UNDER_REG_SOC() != null) {
						R11cell3.setCellValue(record.getR11_UNDER_REG_SOC().doubleValue());
						R11cell3.setCellStyle(numberStyle);
					} else {
						R11cell3.setCellValue("");
						R11cell3.setCellStyle(textStyle);
					}

					// ================= R12 =================
					row = sheet.getRow(11);
					Cell R12cell2 = row.createCell(1);
					if (record.getR12_BAL_SHEET_PUB_FS() != null) {
						R12cell2.setCellValue(record.getR12_BAL_SHEET_PUB_FS().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}
					Cell R12cell3 = row.createCell(2);
					if (record.getR12_UNDER_REG_SOC() != null) {
						R12cell3.setCellValue(record.getR12_UNDER_REG_SOC().doubleValue());
						R12cell3.setCellStyle(numberStyle);
					} else {
						R12cell3.setCellValue("");
						R12cell3.setCellStyle(textStyle);
					}

					// ================= R13 =================
					row = sheet.getRow(12);
					Cell R13cell2 = row.createCell(1);
					if (record.getR13_BAL_SHEET_PUB_FS() != null) {
						R13cell2.setCellValue(record.getR13_BAL_SHEET_PUB_FS().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}
					Cell R13cell3 = row.createCell(2);
					if (record.getR13_UNDER_REG_SOC() != null) {
						R13cell3.setCellValue(record.getR13_UNDER_REG_SOC().doubleValue());
						R13cell3.setCellStyle(numberStyle);
					} else {
						R13cell3.setCellValue("");
						R13cell3.setCellStyle(textStyle);
					}

					// ================= R14 =================
					row = sheet.getRow(13);
					Cell R14cell2 = row.createCell(1);
					if (record.getR14_BAL_SHEET_PUB_FS() != null) {
						R14cell2.setCellValue(record.getR14_BAL_SHEET_PUB_FS().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}
					Cell R14cell3 = row.createCell(2);
					if (record.getR14_UNDER_REG_SOC() != null) {
						R14cell3.setCellValue(record.getR14_UNDER_REG_SOC().doubleValue());
						R14cell3.setCellStyle(numberStyle);
					} else {
						R14cell3.setCellValue("");
						R14cell3.setCellStyle(textStyle);
					}

					// ================= R15 =================
					row = sheet.getRow(14);
					Cell R15cell2 = row.createCell(1);
					if (record.getR15_BAL_SHEET_PUB_FS() != null) {
						R15cell2.setCellValue(record.getR15_BAL_SHEET_PUB_FS().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);
					}
					Cell R15cell3 = row.createCell(2);
					if (record.getR15_UNDER_REG_SOC() != null) {
						R15cell3.setCellValue(record.getR15_UNDER_REG_SOC().doubleValue());
						R15cell3.setCellStyle(numberStyle);
					} else {
						R15cell3.setCellValue("");
						R15cell3.setCellStyle(textStyle);
					}

					// ================= R16 =================
					row = sheet.getRow(15);
					Cell R16cell2 = row.createCell(1);
					if (record.getR16_BAL_SHEET_PUB_FS() != null) {
						R16cell2.setCellValue(record.getR16_BAL_SHEET_PUB_FS().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);
					}
					Cell R16cell3 = row.createCell(2);
					if (record.getR16_UNDER_REG_SOC() != null) {
						R16cell3.setCellValue(record.getR16_UNDER_REG_SOC().doubleValue());
						R16cell3.setCellStyle(numberStyle);
					} else {
						R16cell3.setCellValue("");
						R16cell3.setCellStyle(textStyle);
					}

					// ================= R17 =================
					row = sheet.getRow(16);
					Cell R17cell2 = row.createCell(1);
					if (record.getR17_BAL_SHEET_PUB_FS() != null) {
						R17cell2.setCellValue(record.getR17_BAL_SHEET_PUB_FS().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);
					}
					Cell R17cell3 = row.createCell(2);
					if (record.getR17_UNDER_REG_SOC() != null) {
						R17cell3.setCellValue(record.getR17_UNDER_REG_SOC().doubleValue());
						R17cell3.setCellStyle(numberStyle);
					} else {
						R17cell3.setCellValue("");
						R17cell3.setCellStyle(textStyle);
					}

					// ================= R18 =================
					row = sheet.getRow(17);
					Cell R18cell2 = row.createCell(1);
					if (record.getR18_BAL_SHEET_PUB_FS() != null) {
						R18cell2.setCellValue(record.getR18_BAL_SHEET_PUB_FS().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);
					}
					Cell R18cell3 = row.createCell(2);
					if (record.getR18_UNDER_REG_SOC() != null) {
						R18cell3.setCellValue(record.getR18_UNDER_REG_SOC().doubleValue());
						R18cell3.setCellStyle(numberStyle);
					} else {
						R18cell3.setCellValue("");
						R18cell3.setCellStyle(textStyle);
					}

					// ================= R19 =================
					row = sheet.getRow(18);
					Cell R19cell2 = row.createCell(1);
					if (record.getR19_BAL_SHEET_PUB_FS() != null) {
						R19cell2.setCellValue(record.getR19_BAL_SHEET_PUB_FS().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);
					}
					Cell R19cell3 = row.createCell(2);
					if (record.getR19_UNDER_REG_SOC() != null) {
						R19cell3.setCellValue(record.getR19_UNDER_REG_SOC().doubleValue());
						R19cell3.setCellStyle(numberStyle);
					} else {
						R19cell3.setCellValue("");
						R19cell3.setCellStyle(textStyle);
					}

					// ================= R20 =================
					row = sheet.getRow(19);
					Cell R20cell2 = row.createCell(1);
					if (record.getR20_BAL_SHEET_PUB_FS() != null) {
						R20cell2.setCellValue(record.getR20_BAL_SHEET_PUB_FS().doubleValue());
						R20cell2.setCellStyle(numberStyle);
					} else {
						R20cell2.setCellValue("");
						R20cell2.setCellStyle(textStyle);
					}
					Cell R20cell3 = row.createCell(2);
					if (record.getR20_UNDER_REG_SOC() != null) {
						R20cell3.setCellValue(record.getR20_UNDER_REG_SOC().doubleValue());
						R20cell3.setCellStyle(numberStyle);
					} else {
						R20cell3.setCellValue("");
						R20cell3.setCellStyle(textStyle);
					}

					// ================= R21 =================
					row = sheet.getRow(20);
					Cell R21cell2 = row.createCell(1);
					if (record.getR21_BAL_SHEET_PUB_FS() != null) {
						R21cell2.setCellValue(record.getR21_BAL_SHEET_PUB_FS().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);
					}
					Cell R21cell3 = row.createCell(2);
					if (record.getR21_UNDER_REG_SOC() != null) {
						R21cell3.setCellValue(record.getR21_UNDER_REG_SOC().doubleValue());
						R21cell3.setCellStyle(numberStyle);
					} else {
						R21cell3.setCellValue("");
						R21cell3.setCellStyle(textStyle);
					}

					// ================= R23 =================
					row = sheet.getRow(22);
					Cell R23cell2 = row.createCell(1);
					if (record.getR23_BAL_SHEET_PUB_FS() != null) {
						R23cell2.setCellValue(record.getR23_BAL_SHEET_PUB_FS().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}
					Cell R23cell3 = row.createCell(2);
					if (record.getR23_UNDER_REG_SOC() != null) {
						R23cell3.setCellValue(record.getR23_UNDER_REG_SOC().doubleValue());
						R23cell3.setCellStyle(numberStyle);
					} else {
						R23cell3.setCellValue("");
						R23cell3.setCellStyle(textStyle);
					}

					// ================= R24 =================
					row = sheet.getRow(23);
					Cell R24cell2 = row.createCell(1);
					if (record.getR24_BAL_SHEET_PUB_FS() != null) {
						R24cell2.setCellValue(record.getR24_BAL_SHEET_PUB_FS().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}
					Cell R24cell3 = row.createCell(2);
					if (record.getR24_UNDER_REG_SOC() != null) {
						R24cell3.setCellValue(record.getR24_UNDER_REG_SOC().doubleValue());
						R24cell3.setCellStyle(numberStyle);
					} else {
						R24cell3.setCellValue("");
						R24cell3.setCellStyle(textStyle);
					}

					// ================= R25 =================
					row = sheet.getRow(24);
					Cell R25cell2 = row.createCell(1);
					if (record.getR25_BAL_SHEET_PUB_FS() != null) {
						R25cell2.setCellValue(record.getR25_BAL_SHEET_PUB_FS().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}
					Cell R25cell3 = row.createCell(2);
					if (record.getR25_UNDER_REG_SOC() != null) {
						R25cell3.setCellValue(record.getR25_UNDER_REG_SOC().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}

					// ================= R26 =================
					row = sheet.getRow(25);
					Cell R26cell2 = row.createCell(1);
					if (record.getR26_BAL_SHEET_PUB_FS() != null) {
						R26cell2.setCellValue(record.getR26_BAL_SHEET_PUB_FS().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}
					Cell R26cell3 = row.createCell(2);
					if (record.getR26_UNDER_REG_SOC() != null) {
						R26cell3.setCellValue(record.getR26_UNDER_REG_SOC().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}

					// ================= R27 =================
					row = sheet.getRow(26);
					Cell R27cell2 = row.createCell(1);
					if (record.getR27_BAL_SHEET_PUB_FS() != null) {
						R27cell2.setCellValue(record.getR27_BAL_SHEET_PUB_FS().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}
					Cell R27cell3 = row.createCell(2);
					if (record.getR27_UNDER_REG_SOC() != null) {
						R27cell3.setCellValue(record.getR27_UNDER_REG_SOC().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);
					}

					// ================= R28 =================
					row = sheet.getRow(27);
					Cell R28cell2 = row.createCell(1);
					if (record.getR28_BAL_SHEET_PUB_FS() != null) {
						R28cell2.setCellValue(record.getR28_BAL_SHEET_PUB_FS().doubleValue());
						R28cell2.setCellStyle(numberStyle);
					} else {
						R28cell2.setCellValue("");
						R28cell2.setCellStyle(textStyle);
					}
					Cell R28cell3 = row.createCell(2);
					if (record.getR28_UNDER_REG_SOC() != null) {
						R28cell3.setCellValue(record.getR28_UNDER_REG_SOC().doubleValue());
						R28cell3.setCellStyle(numberStyle);
					} else {
						R28cell3.setCellValue("");
						R28cell3.setCellStyle(textStyle);
					}

					// ================= R29 =================
					row = sheet.getRow(28);
					Cell R29cell2 = row.createCell(1);
					if (record.getR29_BAL_SHEET_PUB_FS() != null) {
						R29cell2.setCellValue(record.getR29_BAL_SHEET_PUB_FS().doubleValue());
						R29cell2.setCellStyle(numberStyle);
					} else {
						R29cell2.setCellValue("");
						R29cell2.setCellStyle(textStyle);
					}
					Cell R29cell3 = row.createCell(2);
					if (record.getR29_UNDER_REG_SOC() != null) {
						R29cell3.setCellValue(record.getR29_UNDER_REG_SOC().doubleValue());
						R29cell3.setCellStyle(numberStyle);
					} else {
						R29cell3.setCellValue("");
						R29cell3.setCellStyle(textStyle);
					}

					// ================= R30 =================
					row = sheet.getRow(29);
					Cell R30cell2 = row.createCell(1);
					if (record.getR30_BAL_SHEET_PUB_FS() != null) {
						R30cell2.setCellValue(record.getR30_BAL_SHEET_PUB_FS().doubleValue());
						R30cell2.setCellStyle(numberStyle);
					} else {
						R30cell2.setCellValue("");
						R30cell2.setCellStyle(textStyle);
					}
					Cell R30cell3 = row.createCell(2);
					if (record.getR30_UNDER_REG_SOC() != null) {
						R30cell3.setCellValue(record.getR30_UNDER_REG_SOC().doubleValue());
						R30cell3.setCellStyle(numberStyle);
					} else {
						R30cell3.setCellValue("");
						R30cell3.setCellStyle(textStyle);
					}

					// ================= R31 =================
					row = sheet.getRow(30);
					Cell R31cell2 = row.createCell(1);
					if (record.getR31_BAL_SHEET_PUB_FS() != null) {
						R31cell2.setCellValue(record.getR31_BAL_SHEET_PUB_FS().doubleValue());
						R31cell2.setCellStyle(numberStyle);
					} else {
						R31cell2.setCellValue("");
						R31cell2.setCellStyle(textStyle);
					}
					Cell R31cell3 = row.createCell(2);
					if (record.getR31_UNDER_REG_SOC() != null) {
						R31cell3.setCellValue(record.getR31_UNDER_REG_SOC().doubleValue());
						R31cell3.setCellStyle(numberStyle);
					} else {
						R31cell3.setCellValue("");
						R31cell3.setCellStyle(textStyle);
					}

					// ================= R32 =================
					row = sheet.getRow(31);
					Cell R32cell2 = row.createCell(1);
					if (record.getR32_BAL_SHEET_PUB_FS() != null) {
						R32cell2.setCellValue(record.getR32_BAL_SHEET_PUB_FS().doubleValue());
						R32cell2.setCellStyle(numberStyle);
					} else {
						R32cell2.setCellValue("");
						R32cell2.setCellStyle(textStyle);
					}
					Cell R32cell3 = row.createCell(2);
					if (record.getR32_UNDER_REG_SOC() != null) {
						R32cell3.setCellValue(record.getR32_UNDER_REG_SOC().doubleValue());
						R32cell3.setCellStyle(numberStyle);
					} else {
						R32cell3.setCellValue("");
						R32cell3.setCellStyle(textStyle);
					}

					// ================= R33 =================
					row = sheet.getRow(32);
					Cell R33cell2 = row.createCell(1);
					if (record.getR33_BAL_SHEET_PUB_FS() != null) {
						R33cell2.setCellValue(record.getR33_BAL_SHEET_PUB_FS().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}
					Cell R33cell3 = row.createCell(2);
					if (record.getR33_UNDER_REG_SOC() != null) {
						R33cell3.setCellValue(record.getR33_UNDER_REG_SOC().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

					// ================= R34 =================
					row = sheet.getRow(33);
					Cell R34cell2 = row.createCell(1);
					if (record.getR34_BAL_SHEET_PUB_FS() != null) {
						R34cell2.setCellValue(record.getR34_BAL_SHEET_PUB_FS().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}
					Cell R34cell3 = row.createCell(2);
					if (record.getR34_UNDER_REG_SOC() != null) {
						R34cell3.setCellValue(record.getR34_UNDER_REG_SOC().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}

					// ================= R35 =================
					row = sheet.getRow(34);
					Cell R35cell2 = row.createCell(1);
					if (record.getR35_BAL_SHEET_PUB_FS() != null) {
						R35cell2.setCellValue(record.getR35_BAL_SHEET_PUB_FS().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}
					Cell R35cell3 = row.createCell(2);
					if (record.getR35_UNDER_REG_SOC() != null) {
						R35cell3.setCellValue(record.getR35_UNDER_REG_SOC().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}

					// ================= R36 =================
					row = sheet.getRow(35);
					Cell R36cell2 = row.createCell(1);
					if (record.getR36_BAL_SHEET_PUB_FS() != null) {
						R36cell2.setCellValue(record.getR36_BAL_SHEET_PUB_FS().doubleValue());
						R36cell2.setCellStyle(numberStyle);
					} else {
						R36cell2.setCellValue("");
						R36cell2.setCellStyle(textStyle);
					}
					Cell R36cell3 = row.createCell(2);
					if (record.getR36_UNDER_REG_SOC() != null) {
						R36cell3.setCellValue(record.getR36_UNDER_REG_SOC().doubleValue());
						R36cell3.setCellStyle(numberStyle);
					} else {
						R36cell3.setCellValue("");
						R36cell3.setCellStyle(textStyle);
					}

					// ================= R38 =================
					row = sheet.getRow(37);
					Cell R38cell2 = row.createCell(1);
					if (record.getR38_BAL_SHEET_PUB_FS() != null) {
						R38cell2.setCellValue(record.getR38_BAL_SHEET_PUB_FS().doubleValue());
						R38cell2.setCellStyle(numberStyle);
					} else {
						R38cell2.setCellValue("");
						R38cell2.setCellStyle(textStyle);
					}
					Cell R38cell3 = row.createCell(2);
					if (record.getR38_UNDER_REG_SOC() != null) {
						R38cell3.setCellValue(record.getR38_UNDER_REG_SOC().doubleValue());
						R38cell3.setCellStyle(numberStyle);
					} else {
						R38cell3.setCellValue("");
						R38cell3.setCellStyle(textStyle);
					}

					// ================= R39 =================
					row = sheet.getRow(38);
					Cell R39cell2 = row.createCell(1);
					if (record.getR39_BAL_SHEET_PUB_FS() != null) {
						R39cell2.setCellValue(record.getR39_BAL_SHEET_PUB_FS().doubleValue());
						R39cell2.setCellStyle(numberStyle);
					} else {
						R39cell2.setCellValue("");
						R39cell2.setCellStyle(textStyle);
					}
					Cell R39cell3 = row.createCell(2);
					if (record.getR39_UNDER_REG_SOC() != null) {
						R39cell3.setCellValue(record.getR39_UNDER_REG_SOC().doubleValue());
						R39cell3.setCellStyle(numberStyle);
					} else {
						R39cell3.setCellValue("");
						R39cell3.setCellStyle(textStyle);
					}

					// ================= R40 =================
					row = sheet.getRow(39);
					Cell R40cell2 = row.createCell(1);
					if (record.getR40_BAL_SHEET_PUB_FS() != null) {
						R40cell2.setCellValue(record.getR40_BAL_SHEET_PUB_FS().doubleValue());
						R40cell2.setCellStyle(numberStyle);
					} else {
						R40cell2.setCellValue("");
						R40cell2.setCellStyle(textStyle);
					}
					Cell R40cell3 = row.createCell(2);
					if (record.getR40_UNDER_REG_SOC() != null) {
						R40cell3.setCellValue(record.getR40_UNDER_REG_SOC().doubleValue());
						R40cell3.setCellStyle(numberStyle);
					} else {
						R40cell3.setCellValue("");
						R40cell3.setCellStyle(textStyle);
					}

					// ================= R41 =================
					row = sheet.getRow(40);
					Cell R41cell2 = row.createCell(1);
					if (record.getR41_BAL_SHEET_PUB_FS() != null) {
						R41cell2.setCellValue(record.getR41_BAL_SHEET_PUB_FS().doubleValue());
						R41cell2.setCellStyle(numberStyle);
					} else {
						R41cell2.setCellValue("");
						R41cell2.setCellStyle(textStyle);
					}
					Cell R41cell3 = row.createCell(2);
					if (record.getR41_UNDER_REG_SOC() != null) {
						R41cell3.setCellValue(record.getR41_UNDER_REG_SOC().doubleValue());
						R41cell3.setCellStyle(numberStyle);
					} else {
						R41cell3.setCellValue("");
						R41cell3.setCellStyle(textStyle);
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

	public byte[] getDetailExcelARCHIVAL(String filename,
			String fromdate,
			String todate,
			String currency,
			String dtltype,
			String type,
			String version) {
		try {
			logger.info("Generating Excel for BRRS_Recon_Of_FS ARCHIVAL Details...");
			System.out.println("came to Detail download service");

			// --- Create workbook and sheet ---
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Recon_Of_FSDetail");

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

			// Right-aligned header style for ACCT BALANCE
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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// --- Header row ---
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "ROWID", "COLUMNID",
					"REPORT_DATE" };
			XSSFRow headerRow = sheet.createRow(0);

			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				if (i == 3) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}
				sheet.setColumnWidth(i, 5000);
			}

			// --- Fetch data from DB ---
			// Date parsedToDate = new SimpleDateFormat("dd-MM-yyyy").parse(todate); // ✅
			// match with controller
			List<Recon_Of_FS_Archival_Detail_Entity> reportData = Recon_Of_FS_Archival_Detail_Repo
					.getdatabydateList(todate, version);

			logger.info("Fetched {} rows from DB for ARCHIVAL", reportData != null ? reportData.size() : 0);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (Recon_Of_FS_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// Balance
					Cell balanceCell = row.createCell(3);
					balanceCell.setCellValue(item.getAcctBalanceInPula() != null
							? item.getAcctBalanceInPula().doubleValue()
							: 0.000);
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria1());
					row.createCell(6).setCellValue(item.getReportDate() != null
							? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
							: "");

					// Apply data style except balance column
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("⚠️ No data found for BRRS_Recon_Of_FS ARCHIVAL — only header will be written.");
			}

			// --- Write to byte[] ---
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_Recon_Of_FS Excel", e);
			return new byte[0];
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/Recon_Of_FS");

		if (acctNo != null) {
			Recon_Of_FS_Detail_Entity ReconFs = Recon_Of_FS_Detail_Repo.findByAcctnumber(acctNo);
			if (ReconFs != null && ReconFs.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(ReconFs.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("Recon_Of_FSData", ReconFs);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String acctBalanceInpula = request.getParameter("acctBalanceInPula");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			Recon_Of_FS_Detail_Entity existing = Recon_Of_FS_Detail_Repo.findByAcctnumber(acctNo);
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
				if (existing.getAcctBalanceInPula() == null ||
						existing.getAcctBalanceInPula().compareTo(newacctBalanceInpula) != 0) {
					existing.setAcctBalanceInPula(newacctBalanceInpula);
					isChanged = true;
					logger.info("Balance updated to {}", newacctBalanceInpula);
				}
			}

			if (isChanged) {
				Recon_Of_FS_Detail_Repo.save(existing);
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_RECON_OF_FS_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_RECON_OF_FS_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating ReconOfFS record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

}
