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
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_M_LA2_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_LA2_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_LA2_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_LA2_Resub_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_LA2_Resub_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_LA2_Summary_Repo;
import com.bornfire.brrs.entities.M_CR_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_LA2_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_LA2_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_LA2_Detail_Entity;
import com.bornfire.brrs.entities.M_LA2_Resub_Detail_Entity;
import com.bornfire.brrs.entities.M_LA2_Resub_Summary_Entity;
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
	BRRS_M_LA2_Summary_Repo brrs_M_LA2_summary_repo;

	@Autowired
	BRRS_M_LA2_Detail_Repo brrs_M_LA2_detail_repo;

	@Autowired
	BRRS_M_LA2_Archival_Summary_Repo M_LA2_Archival_Summary_Repo;

	@Autowired
	BRRS_M_LA2_Detail_Repo BRRS_M_LA2_Detail_Repo;

	@Autowired
	BRRS_M_LA2_Archival_Detail_Repo BRRS_M_LA2_Archival_Detail_Repo;
	
	@Autowired
	BRRS_M_LA2_Resub_Summary_Repo M_LA2_Resub_Summary_Repo;

	@Autowired
	BRRS_M_LA2_Resub_Detail_Repo M_LA2_Resub_Detail_Repo;

	

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRRS_M_LA2View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		System.out.println("dtltype...." + dtltype);
		System.out.println("type...." + type);

		try {

			// Parse only once
			Date d1 = dateformat.parse(todate);

			System.out.println("======= VIEW SCREEN =======");
			System.out.println("TYPE      : " + type);
			System.out.println("DTLTYPE   : " + dtltype);
			System.out.println("DATE      : " + d1);
			System.out.println("VERSION   : " + version);
			System.out.println("==========================");

			// ===========================================================
			// SUMMARY SECTION
			// ===========================================================

			// ---------- CASE 1: ARCHIVAL ----------
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				List<M_LA2_Archival_Summary_Entity> T1Master = M_LA2_Archival_Summary_Repo.getdatabydateListarchival(d1,
						version);
				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_LA2_Resub_Summary_Entity> T1Master = M_LA2_Resub_Summary_Repo.getdatabydateListarchival(d1,
						version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<M_LA2_Summary_Entity> T1Master = brrs_M_LA2_summary_repo.getdatabydateList(dateformat.parse(todate));
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_LA2_Archival_Detail_Entity> T1Master = BRRS_M_LA2_Archival_Detail_Repo
							.getdatabydateListarchival(d1, version);
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_LA2_Resub_Detail_Entity> T1Master = M_LA2_Resub_Detail_Repo
							.getdatabydateListarchival(d1, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_LA2_Detail_Entity> T1Master = BRRS_M_LA2_Detail_Repo
							.getdatabydateList(dateformat.parse(todate));
					System.out.println("Details......T1Master Size " + T1Master.size());
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_LA2");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	
	public void updateReport(M_LA2_Summary_Entity updatedEntity) {

		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

		// 1️⃣ Fetch existing SUMMARY
		M_LA2_Summary_Entity existingSummary = brrs_M_LA2_summary_repo.findById(updatedEntity.getREPORT_DATE())
				.orElseThrow(() -> new RuntimeException(
						"Summary record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

		// 2️⃣ Fetch or create DETAIL
		M_LA2_Detail_Entity existingDetail = brrs_M_LA2_detail_repo.findById(updatedEntity.getREPORT_DATE())
				.orElseGet(() -> {
					M_LA2_Detail_Entity d = new M_LA2_Detail_Entity();
					d.setREPORT_DATE(updatedEntity.getREPORT_DATE());
					return d;
				});

		try {

			// 🔁 Loop R11 → R23
			for (int i = 11; i <= 25; i++) {

				String prefix = "R" + i + "_";

				String[] fields = { "TOTAL" };

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_LA2_Summary_Entity.class.getMethod(getterName);

						Method summarySetter = M_LA2_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Method detailSetter = M_LA2_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);

						// ✅ set into SUMMARY
						summarySetter.invoke(existingSummary, newValue);

						// ✅ set into DETAIL
						detailSetter.invoke(existingDetail, newValue);

					} catch (NoSuchMethodException e) {
						// skip missing fields safely
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 3️⃣ Save BOTH (same transaction)
		brrs_M_LA2_summary_repo.save(existingSummary);
		brrs_M_LA2_detail_repo.save(existingDetail);
	}



	public void updateResubReport(M_LA2_Resub_Summary_Entity updatedEntity) {

	    Date reportDate = updatedEntity.getReportDate();

	    BigDecimal maxResubVer = M_LA2_Resub_Summary_Repo.findMaxVersion(reportDate);
	    if (maxResubVer == null) {
	        throw new RuntimeException("No record for report date: " + reportDate);
	    }

	    BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);
	    Date now = new Date();

	    M_LA2_Resub_Summary_Entity resubSummary = new M_LA2_Resub_Summary_Entity();
	    BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "reportResubDate");
	    resubSummary.setReportDate(reportDate);
	    resubSummary.setReportVersion(newVersion);
	    resubSummary.setREPORT_RESUBDATE(now);

	    M_LA2_Resub_Detail_Entity resubDetail = new M_LA2_Resub_Detail_Entity();
	    BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "reportResubDate");
	    resubDetail.setReportDate(reportDate);
	    resubDetail.setReportVersion(newVersion);
	    resubDetail.setREPORT_RESUBDATE(now);

	    M_LA2_Archival_Summary_Entity archSummary = new M_LA2_Archival_Summary_Entity();
	    BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "reportResubDate");
	    archSummary.setReportDate(reportDate);
	    archSummary.setReportVersion(newVersion);
	    archSummary.setReportResubDate(now);

	    M_LA2_Archival_Detail_Entity archDetail = new M_LA2_Archival_Detail_Entity();
	    BeanUtils.copyProperties(updatedEntity, archDetail, "reportDate", "reportVersion", "reportResubDate");
	    archDetail.setReportDate(reportDate);
	    archDetail.setReportVersion(newVersion);
	    archDetail.setReportResubDate(now);

	    M_LA2_Resub_Summary_Repo.save(resubSummary);
	    M_LA2_Resub_Detail_Repo.save(resubDetail);
	    M_LA2_Archival_Summary_Repo.save(archSummary);
	    BRRS_M_LA2_Archival_Detail_Repo.save(archDetail);
	}


	public List<Object[]> getM_LA2Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_LA2_Archival_Summary_Entity> latestArchivalList = M_LA2_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_LA2_Archival_Summary_Entity entity : latestArchivalList) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					resubList.add(row);
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}
		} catch (Exception e) {
			System.err.println("Error fetching M_LA2 Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	public List<Object[]> getM_LA2Archival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_LA2_Archival_Summary_Entity> repoData = M_LA2_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_LA2_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(), entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_LA2_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_LA2 Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Normal format Excel

	public byte[] getBRRS_M_LA2Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		System.out.println("======= VIEW SCREEN =======");
		System.out.println("TYPE      : " + type);
		System.out.println("FORMAT      : " + format);
		System.out.println("DTLTYPE   : " + dtltype);
		System.out.println("DATE      : " + dateformat.parse(todate));
		System.out.println("VERSION   : " + version);
		System.out.println("==========================");
		
		
		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return getExcelM_LA2ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_LA2ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_LA2EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				List<M_LA2_Summary_Entity> dataList = brrs_M_LA2_summary_repo
						.getdatabydateList(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_LA2 report. Returning empty result.");
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
					throw new SecurityException("Template file exists but is not readable (check permissions): "
							+ templatePath.toAbsolutePath());
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
		}
	}

	// Normal Email Excel
	public byte[] BRRS_M_LA2EmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");
		
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_LA2ArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_LA2ResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} 
		else {
		List<M_LA2_Summary_Entity> dataList = brrs_M_LA2_summary_repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_LA2 report. Returning empty result.");
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
	}

	// Archival format excel
	public byte[] getExcelM_LA2ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_LA2ArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
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

	// Archival Email Excel
	public byte[] BRRS_M_LA2ArchivalEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_LA2_Archival_Summary_Entity> dataList = M_LA2_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_LA2 report. Returning empty result.");
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

	// Resub Format excel
	public byte[] BRRS_M_LA2ResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_LA2ResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
		}
	}

		List<M_LA2_Resub_Summary_Entity> dataList = M_LA2_Resub_Summary_Repo
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
			// --- End of Style Definitions ---

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_LA2_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
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

	// Resub Email Excel
	public byte[] BRRS_M_LA2ResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_LA2_Resub_Summary_Entity> dataList = M_LA2_Resub_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_LA2 report. Returning empty result.");
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
					M_LA2_Resub_Summary_Entity record = dataList.get(i);
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

}