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

import javax.transaction.Transactional;

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

import com.bornfire.brrs.entities.BRRS_Q_STAFF_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Q_STAFF_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_Q_STAFF_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Q_STAFF_Resub_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Q_STAFF_Resub_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_Q_STAFF_Summary_Repo;
import com.bornfire.brrs.entities.Q_STAFF_Archival_Detail_Entity;
import com.bornfire.brrs.entities.Q_STAFF_Archival_Summary_Entity;
import com.bornfire.brrs.entities.Q_STAFF_Detail_Entity;
import com.bornfire.brrs.entities.Q_STAFF_Resub_Detail_Entity;
import com.bornfire.brrs.entities.Q_STAFF_Resub_Summary_Entity;
import com.bornfire.brrs.entities.Q_STAFF_Summary_Entity;

@Component
@Service

public class BRRS_Q_STAFF_Report_Service {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_Q_STAFF_Report_Service.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_Q_STAFF_Detail_Repo Q_STAFF_Detail_Repo;

	@Autowired
	BRRS_Q_STAFF_Summary_Repo Q_STAFF_Summary_Repo;

	@Autowired
	BRRS_Q_STAFF_Archival_Summary_Repo Q_STAFF_Archival_Summary_Repo;

	@Autowired
	BRRS_Q_STAFF_Archival_Detail_Repo Q_STAFF_Archival_Detail_Repo;

	@Autowired
	BRRS_Q_STAFF_Resub_Summary_Repo Q_STAFF_Resub_Summary_Repo;

	@Autowired
	BRRS_Q_STAFF_Resub_Detail_Repo Q_STAFF_Resub_Detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getQ_STAFFView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		try {

			// Parse only once
			Date d1 = dateformat.parse(todate);

			System.out.println("======= VIEW DEBUG =======");
			System.out.println("TYPE      : " + type);
			System.out.println("DTLTYPE   : " + dtltype);
			System.out.println("DATE      : " + d1);
			System.out.println("VERSION   : " + version);
			System.out.println("==========================");

			// ===========================================================
			// 1️⃣ SUMMARY SECTION
			// ===========================================================

			// ---------- ARCHIVAL SUMMARY ----------
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

				List<Q_STAFF_Archival_Summary_Entity> T1Master = Q_STAFF_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);

				System.out.println("Archival Summary Size : " + T1Master.size());

				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- RESUB SUMMARY ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {

				List<Q_STAFF_Resub_Summary_Entity> T1Master = Q_STAFF_Resub_Summary_Repo.getdatabydateListarchival(d1,
						version);

				System.out.println("Resub Summary Size : " + T1Master.size());

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- NORMAL SUMMARY ----------
			else {

				List<Q_STAFF_Summary_Entity> T1Master = Q_STAFF_Summary_Repo.getdatabydateList(d1);

				System.out.println("Normal Summary Size : " + T1Master.size());

				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ===========================================================
			// 2️⃣ DETAIL SECTION
			// ===========================================================

			if ("detail".equalsIgnoreCase(dtltype)) {

				// ---------- ARCHIVAL DETAIL ----------
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<Q_STAFF_Archival_Detail_Entity> T1Master = Q_STAFF_Archival_Detail_Repo
							.getdatabydateListarchival(d1, version);

					System.out.println("Archival Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}

				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<Q_STAFF_Resub_Detail_Entity> T1Master = Q_STAFF_Resub_Detail_Repo.getdatabydateListarchival(d1,
							version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}

				// ---------- NORMAL DETAIL ----------
				else {

					List<Q_STAFF_Detail_Entity> T1Master = Q_STAFF_Detail_Repo.getdatabydateList(d1);

					System.out.println("Normal Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/Q_STAFF");

		System.out.println("View set to: " + mv.getViewName());

		return mv;
	}

	@Transactional
	public void updateReport(Q_STAFF_Summary_Entity updatedEntity) {

		System.out.println("Came to services 1");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		// 🔹 Fetch existing SUMMARY
		Q_STAFF_Summary_Entity existingSummary = Q_STAFF_Summary_Repo.findById(updatedEntity.getReportDate())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

		// 🔹 Fetch or create DETAIL
		Q_STAFF_Detail_Entity detailEntity = Q_STAFF_Detail_Repo.findById(updatedEntity.getReportDate())
				.orElseGet(() -> {
					Q_STAFF_Detail_Entity d = new Q_STAFF_Detail_Entity();
					d.setReportDate(updatedEntity.getReportDate());
					return d;
				});

		try {
			// 🔁 Loop R9 to R15
			for (int i = 9; i <= 15; i++) {

				String prefix = "R" + i + "_";
				String[] fields = { "STAFF_COMPLEMENT", "LOCAL", "EXPARIATES", "TOTAL" };

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						// Getter from UPDATED entity
						Method getter = Q_STAFF_Summary_Entity.class.getMethod(getterName);

						Object newValue = getter.invoke(updatedEntity);

						// SUMMARY setter
						Method summarySetter = Q_STAFF_Summary_Entity.class.getMethod(setterName,
								getter.getReturnType());

						summarySetter.invoke(existingSummary, newValue);

						// DETAIL setter
						Method detailSetter = Q_STAFF_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						// Field not present in entity – skip safely
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		System.out.println("Saving Summary & Detail tables");

		// 💾 Save both tables
		Q_STAFF_Summary_Repo.save(existingSummary);
		Q_STAFF_Detail_Repo.save(detailEntity);

		System.out.println("Update completed successfully");
	}

	public void updateReport2(Q_STAFF_Summary_Entity updatedEntity) {
		System.out.println("Came to services 2");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		Q_STAFF_Summary_Entity existing = Q_STAFF_Summary_Repo.findById(updatedEntity.getReportDate()).orElse(null);

		// 🔹 Fetch existing SUMMARY
		Q_STAFF_Summary_Entity existingSummary = Q_STAFF_Summary_Repo.findById(updatedEntity.getReportDate())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

		// 🔹 Fetch or create DETAIL
		Q_STAFF_Detail_Entity detailEntity = Q_STAFF_Detail_Repo.findById(updatedEntity.getReportDate())
				.orElseGet(() -> {
					Q_STAFF_Detail_Entity d = new Q_STAFF_Detail_Entity();
					d.setReportDate(updatedEntity.getReportDate());
					return d;
				});

		try {
			for (int i = 21; i <= 28; i++) {
				String prefix = "R" + i + "_";
				String[] fields = { "SENIOR_MANAGEMENT_COMPENSATION", "LOCAL", "EXPARIATES", "TOTAL" };

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						// Getter from UPDATED entity
						Method getter = Q_STAFF_Summary_Entity.class.getMethod(getterName);

						Object newValue = getter.invoke(updatedEntity);

						// SUMMARY setter
						Method summarySetter = Q_STAFF_Summary_Entity.class.getMethod(setterName,
								getter.getReturnType());

						summarySetter.invoke(existingSummary, newValue);

						// DETAIL setter
						Method detailSetter = Q_STAFF_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						// Field not present in entity – skip safely
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		System.out.println("Saving Summary & Detail tables");

		// 💾 Save both tables
		Q_STAFF_Summary_Repo.save(existingSummary);
		Q_STAFF_Detail_Repo.save(detailEntity);

		System.out.println("Update completed successfully");
	}

	public void updateReport3(Q_STAFF_Summary_Entity updatedEntity) {
		System.out.println("Came to services 3");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		// 🔹 Fetch existing SUMMARY
		Q_STAFF_Summary_Entity existingSummary = Q_STAFF_Summary_Repo.findById(updatedEntity.getReportDate())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

		// 🔹 Fetch or create DETAIL
		Q_STAFF_Detail_Entity detailEntity = Q_STAFF_Detail_Repo.findById(updatedEntity.getReportDate())
				.orElseGet(() -> {
					Q_STAFF_Detail_Entity d = new Q_STAFF_Detail_Entity();
					d.setReportDate(updatedEntity.getReportDate());
					return d;
				});

		try {
			for (int i = 33; i <= 38; i++) {
				String prefix = "R" + i + "_";
				String[] fields = { "STAFF_LOANS", "ORIGINAL_AMT", "BALANCE_OUTSTANDING", "NO_OF_ACS",
						"INTEREST_RATE" };

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						// Getter from UPDATED entity
						Method getter = Q_STAFF_Summary_Entity.class.getMethod(getterName);

						Object newValue = getter.invoke(updatedEntity);

						// SUMMARY setter
						Method summarySetter = Q_STAFF_Summary_Entity.class.getMethod(setterName,
								getter.getReturnType());

						summarySetter.invoke(existingSummary, newValue);

						// DETAIL setter
						Method detailSetter = Q_STAFF_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						// Field not present in entity – skip safely
						continue;
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		System.out.println("Saving Summary & Detail tables");

		// 💾 Save both tables
		Q_STAFF_Summary_Repo.save(existingSummary);
		Q_STAFF_Detail_Repo.save(detailEntity);

		System.out.println("Update completed successfully");
	}

	@Transactional
	public void updateResubReport(Q_STAFF_Resub_Summary_Entity updatedEntity) {

		Date reportDate = updatedEntity.getReportDate();

		// ----------------------------------------------------
		// 1️⃣ GET CURRENT VERSION FROM RESUB TABLE
		// ----------------------------------------------------

		BigDecimal maxResubVer = Q_STAFF_Resub_Summary_Repo.findMaxVersion(reportDate);

		if (maxResubVer == null)
			throw new RuntimeException("No record for: " + reportDate);

		BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

		Date now = new Date();

		// ====================================================
		// 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
		// ====================================================

		Q_STAFF_Resub_Summary_Entity resubSummary = new Q_STAFF_Resub_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "reportResubDate");

		resubSummary.setReportDate(reportDate);
		resubSummary.setReportVersion(newVersion);
		resubSummary.setReportResubDate(now);

		// ====================================================
		// 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
		// ====================================================

		Q_STAFF_Resub_Detail_Entity resubDetail = new Q_STAFF_Resub_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "reportResubDate");

		resubDetail.setReportDate(reportDate);
		resubDetail.setReportVersion(newVersion);
		resubDetail.setReportResubDate(now);

		// ====================================================
		// 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
		// ====================================================

		Q_STAFF_Archival_Summary_Entity archSummary = new Q_STAFF_Archival_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "reportResubDate");

		archSummary.setReportDate(reportDate);
		archSummary.setReportVersion(newVersion); // SAME VERSION
		archSummary.setReportResubDate(now);

		// ====================================================
		// 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
		// ====================================================

		Q_STAFF_Archival_Detail_Entity archDetail = new Q_STAFF_Archival_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, archDetail, "reportDate", "reportVersion", "reportResubDate");

		archDetail.setReportDate(reportDate);
		archDetail.setReportVersion(newVersion); // SAME VERSION
		archDetail.setReportResubDate(now);

		// ====================================================
		// 6️⃣ SAVE ALL WITH SAME DATA
		// ====================================================

		Q_STAFF_Resub_Summary_Repo.save(resubSummary);
		Q_STAFF_Resub_Detail_Repo.save(resubDetail);

		Q_STAFF_Archival_Summary_Repo.save(archSummary);
		Q_STAFF_Archival_Detail_Repo.save(archDetail);
	}

	// Download For Summary
	// Download For Summary
	public byte[] BRRS_Q_STAFFExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// Convert string to Date
		Date reportDate = dateformat.parse(todate);

		// 1. ARCHIVAL – FORMAT
		if ("ARCHIVAL".equalsIgnoreCase(type) 
		        && version != null 
		        && "report".equalsIgnoreCase(dtltype)) {

		    logger.info("Service: Generating ARCHIVAL report for version {}", version);

		    return getSummaryExcelARCHIVAL(
		        filename, reportId, fromdate, todate,
		        currency, dtltype, type, version);
		}

		// 2. RESUB – FORMAT
		else if ("RESUB".equalsIgnoreCase(type) 
		        && version != null
		        && "report".equalsIgnoreCase(dtltype)) {

		    logger.info("Service: Generating RESUB report for version {}", version);

		    return BRRS_Q_STAFFResubExcel(
		        filename, reportId, fromdate, todate,
		        currency, dtltype, type, version);
		}

		// 3. RESUB – EMAIL
		else if ("RESUB".equalsIgnoreCase(type) 
		        && "email".equalsIgnoreCase(dtltype)) {

		    logger.info("Service: Generating RESUB Email for version {}", version);

		    return BRRS_Q_STAFFEmailResubExcel(
		        filename, reportId, fromdate, todate,
		        currency, dtltype, type, version);
		}

		// 4. NORMAL EMAIL (NO VERSION)
		else if ("email".equalsIgnoreCase(dtltype) 
		        && version == null) {

		    logger.info("Service: Generating Normal Email");

		    return BRRS_Q_STAFFEmailExcel(
		        filename, reportId, fromdate, todate,
		        currency, dtltype, type, null);
		}

		// 5. ARCHIVAL – EMAIL
		else if ("ARCHIVAL".equalsIgnoreCase(type) 
		        && "email".equalsIgnoreCase(dtltype)
		        && version != null) {

		    logger.info("Service: Generating ARCHIVAL Email for version {}", version);

		    return BRRS_Q_STAFFEmailArchivalExcel(
		        filename, reportId, fromdate, todate,
		        currency, dtltype, type, version);
		}

		// Default (LIVE) case
		List<Q_STAFF_Summary_Entity> dataList = Q_STAFF_Summary_Repo.getdatabydateList(reportDate);

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
					Q_STAFF_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R9 Col B
					Cell R9cell1 = row.createCell(1);
					if (record.getR9_LOCAL() != null) {
						R9cell1.setCellValue(record.getR9_LOCAL().doubleValue());
						R9cell1.setCellStyle(numberStyle);
					} else {
						R9cell1.setCellValue("");
						R9cell1.setCellStyle(textStyle);
					}

					// R9 Col C
					Cell R9cell2 = row.createCell(2);
					if (record.getR9_EXPARIATES() != null) {
						R9cell2.setCellValue(record.getR9_EXPARIATES().doubleValue());
						R9cell2.setCellStyle(numberStyle);
					} else {
						R9cell2.setCellValue("");
						R9cell2.setCellStyle(textStyle);
					}
					// R10 Col B
					row = sheet.getRow(9);
					// R10 Col B
					Cell R10cell1 = row.createCell(1);
					if (record.getR10_LOCAL() != null) {
						R10cell1.setCellValue(record.getR10_LOCAL().doubleValue());
						R10cell1.setCellStyle(numberStyle);
					} else {
						R10cell1.setCellValue("");
						R10cell1.setCellStyle(textStyle);
					}

					// R10 Col C
					Cell R10cell2 = row.createCell(2);
					if (record.getR10_EXPARIATES() != null) {
						R10cell2.setCellValue(record.getR10_EXPARIATES().doubleValue());
						R10cell2.setCellStyle(numberStyle);
					} else {
						R10cell2.setCellValue("");
						R10cell2.setCellStyle(textStyle);
					}
					// R11 Col B
					row = sheet.getRow(10);
					// R11 Col B
					Cell R11cell1 = row.createCell(1);
					if (record.getR11_LOCAL() != null) {
						R11cell1.setCellValue(record.getR11_LOCAL().doubleValue());
						R11cell1.setCellStyle(numberStyle);
					} else {
						R11cell1.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}

					// R11 Col C
					Cell R11cell2 = row.createCell(2);
					if (record.getR11_EXPARIATES() != null) {
						R11cell2.setCellValue(record.getR11_EXPARIATES().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}
					// R12 Col B
					row = sheet.getRow(11);

					Cell R12cell1 = row.createCell(1);
					if (record.getR12_LOCAL() != null) {
						R12cell1.setCellValue(record.getR12_LOCAL().doubleValue());
						R12cell1.setCellStyle(numberStyle);
					} else {
						R12cell1.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

					// R12 Col C
					Cell R12cell2 = row.createCell(2);
					if (record.getR12_EXPARIATES() != null) {
						R12cell2.setCellValue(record.getR12_EXPARIATES().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}
					// R13 Col B
					row = sheet.getRow(12);

					Cell R13cell1 = row.createCell(1);
					if (record.getR13_LOCAL() != null) {
						R13cell1.setCellValue(record.getR13_LOCAL().doubleValue());
						R13cell1.setCellStyle(numberStyle);
					} else {
						R13cell1.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

					// R13 Col C
					Cell R13cell2 = row.createCell(2);
					if (record.getR13_EXPARIATES() != null) {
						R13cell2.setCellValue(record.getR13_EXPARIATES().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}
					// R14 Col B
					row = sheet.getRow(13);
					Cell R14cell1 = row.createCell(1);
					if (record.getR14_LOCAL() != null) {
						R14cell1.setCellValue(record.getR14_LOCAL().doubleValue());
						R14cell1.setCellStyle(numberStyle);
					} else {
						R14cell1.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

					// R14 Col C
					Cell R14cell2 = row.createCell(2);
					if (record.getR14_EXPARIATES() != null) {
						R14cell2.setCellValue(record.getR14_EXPARIATES().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}

					// TABLE 2
					// R21 Col B
					row = sheet.getRow(20);
					Cell R21cell1 = row.createCell(1);
					if (record.getR21_LOCAL() != null) {
						R21cell1.setCellValue(record.getR21_LOCAL().doubleValue());
						R21cell1.setCellStyle(numberStyle);
					} else {
						R21cell1.setCellValue("");
						R21cell1.setCellStyle(textStyle);
					}
					// R21 COL C
					Cell R21cell2 = row.createCell(2);
					if (record.getR21_EXPARIATES() != null) {
						R21cell2.setCellValue(record.getR21_EXPARIATES().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);
					}
					// R22 Col B
					row = sheet.getRow(21);
					Cell R22cell1 = row.createCell(1);
					if (record.getR22_LOCAL() != null) {
						R22cell1.setCellValue(record.getR22_LOCAL().doubleValue());
						R22cell1.setCellStyle(numberStyle);
					} else {
						R22cell1.setCellValue("");
						R22cell1.setCellStyle(textStyle);
					}

					// R22 Col C
					Cell R22cell2 = row.createCell(2);
					if (record.getR22_EXPARIATES() != null) {
						R22cell2.setCellValue(record.getR22_EXPARIATES().doubleValue());
						R22cell2.setCellStyle(numberStyle);
					} else {
						R22cell2.setCellValue("");
						R22cell2.setCellStyle(textStyle);
					}
					// R23 Col B
					row = sheet.getRow(22);
					// R23 Col B
					Cell R23cell1 = row.createCell(1);
					if (record.getR23_LOCAL() != null) {
						R23cell1.setCellValue(record.getR23_LOCAL().doubleValue());
						R23cell1.setCellStyle(numberStyle);
					} else {
						R23cell1.setCellValue("");
						R23cell1.setCellStyle(textStyle);
					}

					// R23 Col C
					Cell R23cell2 = row.createCell(2);
					if (record.getR23_EXPARIATES() != null) {
						R23cell2.setCellValue(record.getR23_EXPARIATES().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}
					// R24 Col B
					row = sheet.getRow(23);
					// R24 Col B
					Cell R24cell1 = row.createCell(1);
					if (record.getR24_LOCAL() != null) {
						R24cell1.setCellValue(record.getR24_LOCAL().doubleValue());
						R24cell1.setCellStyle(numberStyle);
					} else {
						R24cell1.setCellValue("");
						R24cell1.setCellStyle(textStyle);
					}

					// R24 Col C
					Cell R24cell2 = row.createCell(2);
					if (record.getR24_EXPARIATES() != null) {
						R24cell2.setCellValue(record.getR24_EXPARIATES().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}
					// R25 Col B
					row = sheet.getRow(24);
					// R25 Col B
					Cell R25cell1 = row.createCell(1);
					if (record.getR25_LOCAL() != null) {
						R25cell1.setCellValue(record.getR25_LOCAL().doubleValue());
						R25cell1.setCellStyle(numberStyle);
					} else {
						R25cell1.setCellValue("");
						R25cell1.setCellStyle(textStyle);
					}

					// R25 Col C
					Cell R25cell2 = row.createCell(2);
					if (record.getR25_EXPARIATES() != null) {
						R25cell2.setCellValue(record.getR25_EXPARIATES().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}
					// R26 Col B
					row = sheet.getRow(25);
					// R26 Col B
					Cell R26cell1 = row.createCell(1);
					if (record.getR26_LOCAL() != null) {
						R26cell1.setCellValue(record.getR26_LOCAL().doubleValue());
						R26cell1.setCellStyle(numberStyle);
					} else {
						R26cell1.setCellValue("");
						R26cell1.setCellStyle(textStyle);
					}

					// R26 Col C
					Cell R26cell2 = row.createCell(2);
					if (record.getR26_EXPARIATES() != null) {
						R26cell2.setCellValue(record.getR26_EXPARIATES().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}
					// R27 Col B
					row = sheet.getRow(26);
					// R27 Col B
					Cell R27cell1 = row.createCell(1);
					if (record.getR27_LOCAL() != null) {
						R27cell1.setCellValue(record.getR27_LOCAL().doubleValue());
						R27cell1.setCellStyle(numberStyle);
					} else {
						R27cell1.setCellValue("");
						R27cell1.setCellStyle(textStyle);
					}

					// R27 Col C
					Cell R27cell2 = row.createCell(2);
					if (record.getR27_EXPARIATES() != null) {
						R27cell2.setCellValue(record.getR27_EXPARIATES().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}
					// TABLE 3
					// R33 Col B
					row = sheet.getRow(32);
					Cell R33cell1 = row.createCell(1);
					if (record.getR33_ORIGINAL_AMT() != null) {
						R33cell1.setCellValue(record.getR33_ORIGINAL_AMT().doubleValue());
						R33cell1.setCellStyle(numberStyle);
					} else {
						R33cell1.setCellValue("");
						R33cell1.setCellStyle(textStyle);
					}

					// R33 Col C
					Cell R33cell2 = row.createCell(2);
					if (record.getR33_BALANCE_OUTSTANDING() != null) {
						R33cell2.setCellValue(record.getR33_BALANCE_OUTSTANDING().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}
					// R33 Col D
					Cell R33cell3 = row.createCell(3);
					if (record.getR33_NO_OF_ACS() != null) {
						R33cell3.setCellValue(record.getR33_NO_OF_ACS().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

					// R33 Col E
					Cell R33cell4 = row.createCell(4);
					if (record.getR33_INTEREST_RATE() != null) {
						R33cell4.setCellValue(record.getR33_INTEREST_RATE().doubleValue());
						R33cell4.setCellStyle(numberStyle);
					} else {
						R33cell4.setCellValue("");
						R33cell4.setCellStyle(textStyle);
					}
					// R34 Col B
					row = sheet.getRow(33);
					Cell R34cell1 = row.createCell(1);
					if (record.getR34_ORIGINAL_AMT() != null) {
						R34cell1.setCellValue(record.getR34_ORIGINAL_AMT().doubleValue());
						R34cell1.setCellStyle(numberStyle);
					} else {
						R34cell1.setCellValue("");
						R34cell1.setCellStyle(textStyle);
					}

					// R34 Col C
					Cell R34cell2 = row.createCell(2);
					if (record.getR34_BALANCE_OUTSTANDING() != null) {
						R34cell2.setCellValue(record.getR34_BALANCE_OUTSTANDING().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}
					// R34 Col D
					Cell R34cell3 = row.createCell(3);
					if (record.getR34_NO_OF_ACS() != null) {
						R34cell3.setCellValue(record.getR34_NO_OF_ACS().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}

					// R34 Col E
					Cell R34cell4 = row.createCell(4);
					if (record.getR34_INTEREST_RATE() != null) {
						R34cell4.setCellValue(record.getR34_INTEREST_RATE().doubleValue());
						R34cell4.setCellStyle(numberStyle);
					} else {
						R34cell4.setCellValue("");
						R34cell4.setCellStyle(textStyle);
					}
					// R35 Col B
					row = sheet.getRow(34);

					Cell R35cell1 = row.createCell(1);
					if (record.getR35_ORIGINAL_AMT() != null) {
						R35cell1.setCellValue(record.getR35_ORIGINAL_AMT().doubleValue());
						R35cell1.setCellStyle(numberStyle);
					} else {
						R35cell1.setCellValue("");
						R35cell1.setCellStyle(textStyle);
					}

					// R35 Col C
					Cell R35cell2 = row.createCell(2);
					if (record.getR35_BALANCE_OUTSTANDING() != null) {
						R35cell2.setCellValue(record.getR35_BALANCE_OUTSTANDING().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}
					// R35 Col D
					Cell R35cell3 = row.createCell(3);
					if (record.getR35_NO_OF_ACS() != null) {
						R35cell3.setCellValue(record.getR35_NO_OF_ACS().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}

					// R35 Col E
					Cell R35cell4 = row.createCell(4);
					if (record.getR35_INTEREST_RATE() != null) {
						R35cell4.setCellValue(record.getR35_INTEREST_RATE().doubleValue());
						R35cell4.setCellStyle(numberStyle);
					} else {
						R35cell4.setCellValue("");
						R35cell4.setCellStyle(textStyle);
					}
					// R36 Col B
					row = sheet.getRow(35);
					Cell R36cell1 = row.createCell(1);
					if (record.getR36_ORIGINAL_AMT() != null) {
						R36cell1.setCellValue(record.getR36_ORIGINAL_AMT().doubleValue());
						R36cell1.setCellStyle(numberStyle);
					} else {
						R36cell1.setCellValue("");
						R36cell1.setCellStyle(textStyle);
					}

					// R36 Col C
					Cell R36cell2 = row.createCell(2);
					if (record.getR36_BALANCE_OUTSTANDING() != null) {
						R36cell2.setCellValue(record.getR36_BALANCE_OUTSTANDING().doubleValue());
						R36cell2.setCellStyle(numberStyle);
					} else {
						R36cell2.setCellValue("");
						R36cell2.setCellStyle(textStyle);
					}
					// R36 Col D
					Cell R36cell3 = row.createCell(3);
					if (record.getR36_NO_OF_ACS() != null) {
						R36cell3.setCellValue(record.getR36_NO_OF_ACS().doubleValue());
						R36cell3.setCellStyle(numberStyle);
					} else {
						R36cell3.setCellValue("");
						R36cell3.setCellStyle(textStyle);
					}

					// R36 Col E
					Cell R36cell4 = row.createCell(4);
					if (record.getR36_INTEREST_RATE() != null) {
						R36cell4.setCellValue(record.getR36_INTEREST_RATE().doubleValue());
						R36cell4.setCellStyle(numberStyle);
					} else {
						R36cell4.setCellValue("");
						R36cell4.setCellStyle(textStyle);
					}
					// R37 Col B
					row = sheet.getRow(36);
					Cell R37cell1 = row.createCell(1);
					if (record.getR37_ORIGINAL_AMT() != null) {
						R37cell1.setCellValue(record.getR37_ORIGINAL_AMT().doubleValue());
						R37cell1.setCellStyle(numberStyle);
					} else {
						R37cell1.setCellValue("");
						R37cell1.setCellStyle(textStyle);
					}

					// R37 Col C
					Cell R37cell2 = row.createCell(2);
					if (record.getR37_BALANCE_OUTSTANDING() != null) {
						R37cell2.setCellValue(record.getR37_BALANCE_OUTSTANDING().doubleValue());
						R37cell2.setCellStyle(numberStyle);
					} else {
						R37cell2.setCellValue("");
						R37cell2.setCellStyle(textStyle);
					}
					// R37 Col D
					Cell R37cell3 = row.createCell(3);
					if (record.getR37_NO_OF_ACS() != null) {
						R37cell3.setCellValue(record.getR37_NO_OF_ACS().doubleValue());
						R37cell3.setCellStyle(numberStyle);
					} else {
						R37cell3.setCellValue("");
						R37cell3.setCellStyle(textStyle);
					}

					// R37 Col E
					Cell R37cell4 = row.createCell(4);
					if (record.getR37_INTEREST_RATE() != null) {
						R37cell4.setCellValue(record.getR37_INTEREST_RATE().doubleValue());
						R37cell4.setCellStyle(numberStyle);
					} else {
						R37cell4.setCellValue("");
						R37cell4.setCellStyle(textStyle);
					}
					row = sheet.getRow(37);
					// R37 Col E
					Cell R38cell4 = row.createCell(4);
					if (record.getR38_INTEREST_RATE() != null) {
						R38cell4.setCellValue(record.getR38_INTEREST_RATE().doubleValue());
						R38cell4.setCellStyle(numberStyle);
					} else {
						R38cell4.setCellValue("");
						R38cell4.setCellStyle(textStyle);
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

	public byte[] getSummaryExcelARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<Q_STAFF_Archival_Summary_Entity> dataList = Q_STAFF_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Q_STAFF report. Returning empty result.");
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
//ARCHIVAL DOWNLOAD
			// --- End of Style Definitions ---

			int startRow = 8;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					Q_STAFF_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R9 Col B
					Cell R9cell1 = row.createCell(1);
					if (record.getR9_LOCAL() != null) {
						R9cell1.setCellValue(record.getR9_LOCAL().doubleValue());
						R9cell1.setCellStyle(numberStyle);
					} else {
						R9cell1.setCellValue("");
						R9cell1.setCellStyle(textStyle);
					}

					// R9 Col C
					Cell R9cell2 = row.createCell(2);
					if (record.getR9_EXPARIATES() != null) {
						R9cell2.setCellValue(record.getR9_EXPARIATES().doubleValue());
						R9cell2.setCellStyle(numberStyle);
					} else {
						R9cell2.setCellValue("");
						R9cell2.setCellStyle(textStyle);
					}
					// R10 Col B
					row = sheet.getRow(9);
					// R10 Col B
					Cell R10cell1 = row.createCell(1);
					if (record.getR10_LOCAL() != null) {
						R10cell1.setCellValue(record.getR10_LOCAL().doubleValue());
						R10cell1.setCellStyle(numberStyle);
					} else {
						R10cell1.setCellValue("");
						R10cell1.setCellStyle(textStyle);
					}

					// R10 Col C
					Cell R10cell2 = row.createCell(2);
					if (record.getR10_EXPARIATES() != null) {
						R10cell2.setCellValue(record.getR10_EXPARIATES().doubleValue());
						R10cell2.setCellStyle(numberStyle);
					} else {
						R10cell2.setCellValue("");
						R10cell2.setCellStyle(textStyle);
					}
					// R11 Col B
					row = sheet.getRow(10);
					// R11 Col B
					Cell R11cell1 = row.createCell(1);
					if (record.getR11_LOCAL() != null) {
						R11cell1.setCellValue(record.getR11_LOCAL().doubleValue());
						R11cell1.setCellStyle(numberStyle);
					} else {
						R11cell1.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}

					// R11 Col C
					Cell R11cell2 = row.createCell(2);
					if (record.getR11_EXPARIATES() != null) {
						R11cell2.setCellValue(record.getR11_EXPARIATES().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}
					// R12 Col B
					row = sheet.getRow(11);

					Cell R12cell1 = row.createCell(1);
					if (record.getR12_LOCAL() != null) {
						R12cell1.setCellValue(record.getR12_LOCAL().doubleValue());
						R12cell1.setCellStyle(numberStyle);
					} else {
						R12cell1.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

					// R12 Col C
					Cell R12cell2 = row.createCell(2);
					if (record.getR12_EXPARIATES() != null) {
						R12cell2.setCellValue(record.getR12_EXPARIATES().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}
					// R13 Col B
					row = sheet.getRow(12);

					Cell R13cell1 = row.createCell(1);
					if (record.getR13_LOCAL() != null) {
						R13cell1.setCellValue(record.getR13_LOCAL().doubleValue());
						R13cell1.setCellStyle(numberStyle);
					} else {
						R13cell1.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

					// R13 Col C
					Cell R13cell2 = row.createCell(2);
					if (record.getR13_EXPARIATES() != null) {
						R13cell2.setCellValue(record.getR13_EXPARIATES().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}
					// R14 Col B
					row = sheet.getRow(13);
					Cell R14cell1 = row.createCell(1);
					if (record.getR14_LOCAL() != null) {
						R14cell1.setCellValue(record.getR14_LOCAL().doubleValue());
						R14cell1.setCellStyle(numberStyle);
					} else {
						R14cell1.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

					// R14 Col C
					Cell R14cell2 = row.createCell(2);
					if (record.getR14_EXPARIATES() != null) {
						R14cell2.setCellValue(record.getR14_EXPARIATES().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}

					// TABLE 2
					// R21 Col B
					row = sheet.getRow(20);
					Cell R21cell1 = row.createCell(1);
					if (record.getR21_LOCAL() != null) {
						R21cell1.setCellValue(record.getR21_LOCAL().doubleValue());
						R21cell1.setCellStyle(numberStyle);
					} else {
						R21cell1.setCellValue("");
						R21cell1.setCellStyle(textStyle);
					}
					// R21 COL C
					Cell R21cell2 = row.createCell(2);
					if (record.getR21_EXPARIATES() != null) {
						R21cell2.setCellValue(record.getR21_EXPARIATES().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);
					}
					// R22 Col B
					row = sheet.getRow(21);
					Cell R22cell1 = row.createCell(1);
					if (record.getR22_LOCAL() != null) {
						R22cell1.setCellValue(record.getR22_LOCAL().doubleValue());
						R22cell1.setCellStyle(numberStyle);
					} else {
						R22cell1.setCellValue("");
						R22cell1.setCellStyle(textStyle);
					}

					// R22 Col C
					Cell R22cell2 = row.createCell(2);
					if (record.getR22_EXPARIATES() != null) {
						R22cell2.setCellValue(record.getR22_EXPARIATES().doubleValue());
						R22cell2.setCellStyle(numberStyle);
					} else {
						R22cell2.setCellValue("");
						R22cell2.setCellStyle(textStyle);
					}
					// R23 Col B
					row = sheet.getRow(22);
					// R23 Col B
					Cell R23cell1 = row.createCell(1);
					if (record.getR23_LOCAL() != null) {
						R23cell1.setCellValue(record.getR23_LOCAL().doubleValue());
						R23cell1.setCellStyle(numberStyle);
					} else {
						R23cell1.setCellValue("");
						R23cell1.setCellStyle(textStyle);
					}

					// R23 Col C
					Cell R23cell2 = row.createCell(2);
					if (record.getR23_EXPARIATES() != null) {
						R23cell2.setCellValue(record.getR23_EXPARIATES().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}
					// R24 Col B
					row = sheet.getRow(23);
					// R24 Col B
					Cell R24cell1 = row.createCell(1);
					if (record.getR24_LOCAL() != null) {
						R24cell1.setCellValue(record.getR24_LOCAL().doubleValue());
						R24cell1.setCellStyle(numberStyle);
					} else {
						R24cell1.setCellValue("");
						R24cell1.setCellStyle(textStyle);
					}

					// R24 Col C
					Cell R24cell2 = row.createCell(2);
					if (record.getR24_EXPARIATES() != null) {
						R24cell2.setCellValue(record.getR24_EXPARIATES().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}
					// R25 Col B
					row = sheet.getRow(24);
					// R25 Col B
					Cell R25cell1 = row.createCell(1);
					if (record.getR25_LOCAL() != null) {
						R25cell1.setCellValue(record.getR25_LOCAL().doubleValue());
						R25cell1.setCellStyle(numberStyle);
					} else {
						R25cell1.setCellValue("");
						R25cell1.setCellStyle(textStyle);
					}

					// R25 Col C
					Cell R25cell2 = row.createCell(2);
					if (record.getR25_EXPARIATES() != null) {
						R25cell2.setCellValue(record.getR25_EXPARIATES().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}
					// R26 Col B
					row = sheet.getRow(25);
					// R26 Col B
					Cell R26cell1 = row.createCell(1);
					if (record.getR26_LOCAL() != null) {
						R26cell1.setCellValue(record.getR26_LOCAL().doubleValue());
						R26cell1.setCellStyle(numberStyle);
					} else {
						R26cell1.setCellValue("");
						R26cell1.setCellStyle(textStyle);
					}

					// R26 Col C
					Cell R26cell2 = row.createCell(2);
					if (record.getR26_EXPARIATES() != null) {
						R26cell2.setCellValue(record.getR26_EXPARIATES().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}
					// R27 Col B
					row = sheet.getRow(26);
					// R27 Col B
					Cell R27cell1 = row.createCell(1);
					if (record.getR27_LOCAL() != null) {
						R27cell1.setCellValue(record.getR27_LOCAL().doubleValue());
						R27cell1.setCellStyle(numberStyle);
					} else {
						R27cell1.setCellValue("");
						R27cell1.setCellStyle(textStyle);
					}

					// R27 Col C
					Cell R27cell2 = row.createCell(2);
					if (record.getR27_EXPARIATES() != null) {
						R27cell2.setCellValue(record.getR27_EXPARIATES().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}
					// TABLE 3
					// R33 Col B
					row = sheet.getRow(32);
					Cell R33cell1 = row.createCell(1);
					if (record.getR33_ORIGINAL_AMT() != null) {
						R33cell1.setCellValue(record.getR33_ORIGINAL_AMT().doubleValue());
						R33cell1.setCellStyle(numberStyle);
					} else {
						R33cell1.setCellValue("");
						R33cell1.setCellStyle(textStyle);
					}

					// R33 Col C
					Cell R33cell2 = row.createCell(2);
					if (record.getR33_BALANCE_OUTSTANDING() != null) {
						R33cell2.setCellValue(record.getR33_BALANCE_OUTSTANDING().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}
					// R33 Col D
					Cell R33cell3 = row.createCell(3);
					if (record.getR33_NO_OF_ACS() != null) {
						R33cell3.setCellValue(record.getR33_NO_OF_ACS().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

					// R33 Col E
					Cell R33cell4 = row.createCell(4);
					if (record.getR33_INTEREST_RATE() != null) {
						R33cell4.setCellValue(record.getR33_INTEREST_RATE().doubleValue());
						R33cell4.setCellStyle(numberStyle);
					} else {
						R33cell4.setCellValue("");
						R33cell4.setCellStyle(textStyle);
					}
					// R34 Col B
					row = sheet.getRow(33);
					Cell R34cell1 = row.createCell(1);
					if (record.getR34_ORIGINAL_AMT() != null) {
						R34cell1.setCellValue(record.getR34_ORIGINAL_AMT().doubleValue());
						R34cell1.setCellStyle(numberStyle);
					} else {
						R34cell1.setCellValue("");
						R34cell1.setCellStyle(textStyle);
					}

					// R34 Col C
					Cell R34cell2 = row.createCell(2);
					if (record.getR34_BALANCE_OUTSTANDING() != null) {
						R34cell2.setCellValue(record.getR34_BALANCE_OUTSTANDING().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}
					// R34 Col D
					Cell R34cell3 = row.createCell(3);
					if (record.getR34_NO_OF_ACS() != null) {
						R34cell3.setCellValue(record.getR34_NO_OF_ACS().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}

					// R34 Col E
					Cell R34cell4 = row.createCell(4);
					if (record.getR34_INTEREST_RATE() != null) {
						R34cell4.setCellValue(record.getR34_INTEREST_RATE().doubleValue());
						R34cell4.setCellStyle(numberStyle);
					} else {
						R34cell4.setCellValue("");
						R34cell4.setCellStyle(textStyle);
					}
					// R35 Col B
					row = sheet.getRow(34);

					Cell R35cell1 = row.createCell(1);
					if (record.getR35_ORIGINAL_AMT() != null) {
						R35cell1.setCellValue(record.getR35_ORIGINAL_AMT().doubleValue());
						R35cell1.setCellStyle(numberStyle);
					} else {
						R35cell1.setCellValue("");
						R35cell1.setCellStyle(textStyle);
					}

					// R35 Col C
					Cell R35cell2 = row.createCell(2);
					if (record.getR35_BALANCE_OUTSTANDING() != null) {
						R35cell2.setCellValue(record.getR35_BALANCE_OUTSTANDING().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}
					// R35 Col D
					Cell R35cell3 = row.createCell(3);
					if (record.getR35_NO_OF_ACS() != null) {
						R35cell3.setCellValue(record.getR35_NO_OF_ACS().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}

					// R35 Col E
					Cell R35cell4 = row.createCell(4);
					if (record.getR35_INTEREST_RATE() != null) {
						R35cell4.setCellValue(record.getR35_INTEREST_RATE().doubleValue());
						R35cell4.setCellStyle(numberStyle);
					} else {
						R35cell4.setCellValue("");
						R35cell4.setCellStyle(textStyle);
					}
					// R36 Col B
					row = sheet.getRow(35);
					Cell R36cell1 = row.createCell(1);
					if (record.getR36_ORIGINAL_AMT() != null) {
						R36cell1.setCellValue(record.getR36_ORIGINAL_AMT().doubleValue());
						R36cell1.setCellStyle(numberStyle);
					} else {
						R36cell1.setCellValue("");
						R36cell1.setCellStyle(textStyle);
					}

					// R36 Col C
					Cell R36cell2 = row.createCell(2);
					if (record.getR36_BALANCE_OUTSTANDING() != null) {
						R36cell2.setCellValue(record.getR36_BALANCE_OUTSTANDING().doubleValue());
						R36cell2.setCellStyle(numberStyle);
					} else {
						R36cell2.setCellValue("");
						R36cell2.setCellStyle(textStyle);
					}
					// R36 Col D
					Cell R36cell3 = row.createCell(3);
					if (record.getR36_NO_OF_ACS() != null) {
						R36cell3.setCellValue(record.getR36_NO_OF_ACS().doubleValue());
						R36cell3.setCellStyle(numberStyle);
					} else {
						R36cell3.setCellValue("");
						R36cell3.setCellStyle(textStyle);
					}

					// R36 Col E
					Cell R36cell4 = row.createCell(4);
					if (record.getR36_INTEREST_RATE() != null) {
						R36cell4.setCellValue(record.getR36_INTEREST_RATE().doubleValue());
						R36cell4.setCellStyle(numberStyle);
					} else {
						R36cell4.setCellValue("");
						R36cell4.setCellStyle(textStyle);
					}
					// R37 Col B
					row = sheet.getRow(36);
					Cell R37cell1 = row.createCell(1);
					if (record.getR37_ORIGINAL_AMT() != null) {
						R37cell1.setCellValue(record.getR37_ORIGINAL_AMT().doubleValue());
						R37cell1.setCellStyle(numberStyle);
					} else {
						R37cell1.setCellValue("");
						R37cell1.setCellStyle(textStyle);
					}

					// R37 Col C
					Cell R37cell2 = row.createCell(2);
					if (record.getR37_BALANCE_OUTSTANDING() != null) {
						R37cell2.setCellValue(record.getR37_BALANCE_OUTSTANDING().doubleValue());
						R37cell2.setCellStyle(numberStyle);
					} else {
						R37cell2.setCellValue("");
						R37cell2.setCellStyle(textStyle);
					}
					// R37 Col D
					Cell R37cell3 = row.createCell(3);
					if (record.getR37_NO_OF_ACS() != null) {
						R37cell3.setCellValue(record.getR37_NO_OF_ACS().doubleValue());
						R37cell3.setCellStyle(numberStyle);
					} else {
						R37cell3.setCellValue("");
						R37cell3.setCellStyle(textStyle);
					}

					// R37 Col E
					Cell R37cell4 = row.createCell(4);
					if (record.getR37_INTEREST_RATE() != null) {
						R37cell4.setCellValue(record.getR37_INTEREST_RATE().doubleValue());
						R37cell4.setCellStyle(numberStyle);
					} else {
						R37cell4.setCellValue("");
						R37cell4.setCellStyle(textStyle);
					}
					row = sheet.getRow(37);
					// R37 Col E
					Cell R38cell4 = row.createCell(4);
					if (record.getR38_INTEREST_RATE() != null) {
						R38cell4.setCellValue(record.getR38_INTEREST_RATE().doubleValue());
						R38cell4.setCellStyle(numberStyle);
					} else {
						R38cell4.setCellValue("");
						R38cell4.setCellStyle(textStyle);
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

//////////////////////////////////////////RESUBMISSION///////////////////////////////////////////////////////////////////	
/// Report Date | Report Version | Domain
/// RESUB VIEW
	public List<Object[]> getQ_STAFFResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<Q_STAFF_Archival_Summary_Entity> latestArchivalList = Q_STAFF_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (Q_STAFF_Archival_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching Q_STAFF Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	// Archival View
	public List<Object[]> getQ_STAFFArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<Q_STAFF_Archival_Summary_Entity> repoData = Q_STAFF_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (Q_STAFF_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				Q_STAFF_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching Q_STAFF Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

//Email Download 
	public byte[] BRRS_Q_STAFFEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		Date reportDate = dateformat.parse(todate);
		if (type.equals("email") & version != null) {

		}
		List<Q_STAFF_Summary_Entity> dataList = Q_STAFF_Summary_Repo.getdatabydateList(reportDate);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found forQ_STAFF report. Returning empty result.");
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
//EMAIL EXCEL
			// --- End of Style Definitions ---

			int startRow = 8;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					Q_STAFF_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R9 Col B
					Cell R9cell1 = row.createCell(3);
					if (record.getR9_LOCAL() != null) {
						R9cell1.setCellValue(record.getR9_LOCAL().doubleValue());
						R9cell1.setCellStyle(numberStyle);
					} else {
						R9cell1.setCellValue("");
						R9cell1.setCellStyle(textStyle);
					}

					// R9 Col C
					Cell R9cell2 = row.createCell(4);
					if (record.getR9_EXPARIATES() != null) {
						R9cell2.setCellValue(record.getR9_EXPARIATES().doubleValue());
						R9cell2.setCellStyle(numberStyle);
					} else {
						R9cell2.setCellValue("");
						R9cell2.setCellStyle(textStyle);
					}

					// R9 Col C
					Cell R9cell3 = row.createCell(5);
					if (record.getR9_TOTAL() != null) {
						R9cell3.setCellValue(record.getR9_TOTAL().doubleValue());
						R9cell3.setCellStyle(numberStyle);
					} else {
						R9cell3.setCellValue("");
						R9cell3.setCellStyle(textStyle);
					}
					// R10 Col B
					row = sheet.getRow(9);
					// R10 Col B
					Cell R10cell1 = row.createCell(3);
					if (record.getR10_LOCAL() != null) {
						R10cell1.setCellValue(record.getR10_LOCAL().doubleValue());
						R10cell1.setCellStyle(numberStyle);
					} else {
						R10cell1.setCellValue("");
						R10cell1.setCellStyle(textStyle);
					}

					// R10 Col C
					Cell R10cell2 = row.createCell(4);
					if (record.getR10_EXPARIATES() != null) {
						R10cell2.setCellValue(record.getR10_EXPARIATES().doubleValue());
						R10cell2.setCellStyle(numberStyle);
					} else {
						R10cell2.setCellValue("");
						R10cell2.setCellStyle(textStyle);
					}
					Cell R10cell3 = row.createCell(5);
					if (record.getR10_TOTAL() != null) {
						R10cell3.setCellValue(record.getR10_TOTAL().doubleValue());
						R10cell3.setCellStyle(numberStyle);
					} else {
						R10cell3.setCellValue("");
						R10cell3.setCellStyle(textStyle);
					}
					// R11 Col B
					row = sheet.getRow(10);

					// R11 Col B
					Cell R11cell1 = row.createCell(3);
					if (record.getR11_LOCAL() != null) {
						R11cell1.setCellValue(record.getR11_LOCAL().doubleValue());
						R11cell1.setCellStyle(numberStyle);
					} else {
						R11cell1.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}

					// R11 Col C
					Cell R11cell2 = row.createCell(4);
					if (record.getR11_EXPARIATES() != null) {
						R11cell2.setCellValue(record.getR11_EXPARIATES().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}

					// R11 TOTAL
					Cell R11cell3 = row.createCell(5);
					if (record.getR11_TOTAL() != null) {
						R11cell3.setCellValue(record.getR11_TOTAL().doubleValue());
						R11cell3.setCellStyle(numberStyle);
					} else {
						R11cell3.setCellValue("");
						R11cell3.setCellStyle(textStyle);
					}
					// R12 Col B
					row = sheet.getRow(11);

					// R12 Col B
					Cell R12cell1 = row.createCell(3);
					if (record.getR12_LOCAL() != null) {
						R12cell1.setCellValue(record.getR12_LOCAL().doubleValue());
						R12cell1.setCellStyle(numberStyle);
					} else {
						R12cell1.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

					// R12 Col C
					Cell R12cell2 = row.createCell(4);
					if (record.getR12_EXPARIATES() != null) {
						R12cell2.setCellValue(record.getR12_EXPARIATES().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}

					// R12 TOTAL
					Cell R12cell3 = row.createCell(5);
					if (record.getR12_TOTAL() != null) {
						R12cell3.setCellValue(record.getR12_TOTAL().doubleValue());
						R12cell3.setCellStyle(numberStyle);
					} else {
						R12cell3.setCellValue("");
						R12cell3.setCellStyle(textStyle);
					}

					// R13 Col B
					row = sheet.getRow(12);

					// R13 Col B
					Cell R13cell1 = row.createCell(3);
					if (record.getR13_LOCAL() != null) {
						R13cell1.setCellValue(record.getR13_LOCAL().doubleValue());
						R13cell1.setCellStyle(numberStyle);
					} else {
						R13cell1.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

					// R13 Col C
					Cell R13cell2 = row.createCell(4);
					if (record.getR13_EXPARIATES() != null) {
						R13cell2.setCellValue(record.getR13_EXPARIATES().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}

					// R13 TOTAL
					Cell R13cell3 = row.createCell(5);
					if (record.getR13_TOTAL() != null) {
						R13cell3.setCellValue(record.getR13_TOTAL().doubleValue());
						R13cell3.setCellStyle(numberStyle);
					} else {
						R13cell3.setCellValue("");
						R13cell3.setCellStyle(textStyle);
					}

					// R14 Col B
					row = sheet.getRow(13);

					// R14 Col B
					Cell R14cell1 = row.createCell(3);
					if (record.getR14_LOCAL() != null) {
						R14cell1.setCellValue(record.getR14_LOCAL().doubleValue());
						R14cell1.setCellStyle(numberStyle);
					} else {
						R14cell1.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

					// R14 Col C
					Cell R14cell2 = row.createCell(4);
					if (record.getR14_EXPARIATES() != null) {
						R14cell2.setCellValue(record.getR14_EXPARIATES().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}

					// R14 TOTAL
					Cell R14cell3 = row.createCell(5);
					if (record.getR14_TOTAL() != null) {
						R14cell3.setCellValue(record.getR14_TOTAL().doubleValue());
						R14cell3.setCellStyle(numberStyle);
					} else {
						R14cell3.setCellValue("");
						R14cell3.setCellStyle(textStyle);
					}

					// ================= R21 =================
					row = sheet.getRow(20);

					Cell R21cell1 = row.createCell(3);
					if (record.getR21_LOCAL() != null) {
						R21cell1.setCellValue(record.getR21_LOCAL().doubleValue());
						R21cell1.setCellStyle(numberStyle);
					} else {
						R21cell1.setCellValue("");
						R21cell1.setCellStyle(textStyle);
					}

					Cell R21cell2 = row.createCell(4);
					if (record.getR21_EXPARIATES() != null) {
						R21cell2.setCellValue(record.getR21_EXPARIATES().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);
					}

					Cell R21cell3 = row.createCell(5);
					if (record.getR21_TOTAL() != null) {
						R21cell3.setCellValue(record.getR21_TOTAL().doubleValue());
						R21cell3.setCellStyle(numberStyle);
					} else {
						R21cell3.setCellValue("");
						R21cell3.setCellStyle(textStyle);
					}

					// ================= R22 =================
					row = sheet.getRow(21);

					Cell R22cell1 = row.createCell(3);
					if (record.getR22_LOCAL() != null) {
						R22cell1.setCellValue(record.getR22_LOCAL().doubleValue());
						R22cell1.setCellStyle(numberStyle);
					} else {
						R22cell1.setCellValue("");
						R22cell1.setCellStyle(textStyle);
					}

					Cell R22cell2 = row.createCell(4);
					if (record.getR22_EXPARIATES() != null) {
						R22cell2.setCellValue(record.getR22_EXPARIATES().doubleValue());
						R22cell2.setCellStyle(numberStyle);
					} else {
						R22cell2.setCellValue("");
						R22cell2.setCellStyle(textStyle);
					}

					Cell R22cell3 = row.createCell(5);
					if (record.getR22_TOTAL() != null) {
						R22cell3.setCellValue(record.getR22_TOTAL().doubleValue());
						R22cell3.setCellStyle(numberStyle);
					} else {
						R22cell3.setCellValue("");
						R22cell3.setCellStyle(textStyle);
					}

					// ================= R23 =================
					row = sheet.getRow(22);

					Cell R23cell1 = row.createCell(3);
					if (record.getR23_LOCAL() != null) {
						R23cell1.setCellValue(record.getR23_LOCAL().doubleValue());
						R23cell1.setCellStyle(numberStyle);
					} else {
						R23cell1.setCellValue("");
						R23cell1.setCellStyle(textStyle);
					}

					Cell R23cell2 = row.createCell(4);
					if (record.getR23_EXPARIATES() != null) {
						R23cell2.setCellValue(record.getR23_EXPARIATES().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}

					Cell R23cell3 = row.createCell(5);
					if (record.getR23_TOTAL() != null) {
						R23cell3.setCellValue(record.getR23_TOTAL().doubleValue());
						R23cell3.setCellStyle(numberStyle);
					} else {
						R23cell3.setCellValue("");
						R23cell3.setCellStyle(textStyle);
					}

					// ================= R24 =================
					row = sheet.getRow(23);

					Cell R24cell1 = row.createCell(3);
					if (record.getR24_LOCAL() != null) {
						R24cell1.setCellValue(record.getR24_LOCAL().doubleValue());
						R24cell1.setCellStyle(numberStyle);
					} else {
						R24cell1.setCellValue("");
						R24cell1.setCellStyle(textStyle);
					}

					Cell R24cell2 = row.createCell(4);
					if (record.getR24_EXPARIATES() != null) {
						R24cell2.setCellValue(record.getR24_EXPARIATES().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}

					Cell R24cell3 = row.createCell(5);
					if (record.getR24_TOTAL() != null) {
						R24cell3.setCellValue(record.getR24_TOTAL().doubleValue());
						R24cell3.setCellStyle(numberStyle);
					} else {
						R24cell3.setCellValue("");
						R24cell3.setCellStyle(textStyle);
					}

					// ================= R25 =================
					row = sheet.getRow(24);

					Cell R25cell1 = row.createCell(3);
					if (record.getR25_LOCAL() != null) {
						R25cell1.setCellValue(record.getR25_LOCAL().doubleValue());
						R25cell1.setCellStyle(numberStyle);
					} else {
						R25cell1.setCellValue("");
						R25cell1.setCellStyle(textStyle);
					}

					Cell R25cell2 = row.createCell(4);
					if (record.getR25_EXPARIATES() != null) {
						R25cell2.setCellValue(record.getR25_EXPARIATES().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					Cell R25cell3 = row.createCell(5);
					if (record.getR25_TOTAL() != null) {
						R25cell3.setCellValue(record.getR25_TOTAL().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}

					// ================= R26 =================
					row = sheet.getRow(25);

					Cell R26cell1 = row.createCell(3);
					if (record.getR26_LOCAL() != null) {
						R26cell1.setCellValue(record.getR26_LOCAL().doubleValue());
						R26cell1.setCellStyle(numberStyle);
					} else {
						R26cell1.setCellValue("");
						R26cell1.setCellStyle(textStyle);
					}

					Cell R26cell2 = row.createCell(4);
					if (record.getR26_EXPARIATES() != null) {
						R26cell2.setCellValue(record.getR26_EXPARIATES().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					Cell R26cell3 = row.createCell(5);
					if (record.getR26_TOTAL() != null) {
						R26cell3.setCellValue(record.getR26_TOTAL().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}

					// ================= R27 =================
					row = sheet.getRow(26);

					Cell R27cell1 = row.createCell(3);
					if (record.getR27_LOCAL() != null) {
						R27cell1.setCellValue(record.getR27_LOCAL().doubleValue());
						R27cell1.setCellStyle(numberStyle);
					} else {
						R27cell1.setCellValue("");
						R27cell1.setCellStyle(textStyle);
					}

					Cell R27cell2 = row.createCell(4);
					if (record.getR27_EXPARIATES() != null) {
						R27cell2.setCellValue(record.getR27_EXPARIATES().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}

					Cell R27cell3 = row.createCell(5);
					if (record.getR27_TOTAL() != null) {
						R27cell3.setCellValue(record.getR27_TOTAL().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);
					}

					// TABLE 3
					// R33 Col B
					row = sheet.getRow(32);
					Cell R33cell1 = row.createCell(3);
					if (record.getR33_ORIGINAL_AMT() != null) {
						R33cell1.setCellValue(record.getR33_ORIGINAL_AMT().doubleValue());
						R33cell1.setCellStyle(numberStyle);
					} else {
						R33cell1.setCellValue("");
						R33cell1.setCellStyle(textStyle);
					}

					// R33 Col C
					Cell R33cell2 = row.createCell(4);
					if (record.getR33_BALANCE_OUTSTANDING() != null) {
						R33cell2.setCellValue(record.getR33_BALANCE_OUTSTANDING().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}
					// R33 Col D
					Cell R33cell3 = row.createCell(5);
					if (record.getR33_NO_OF_ACS() != null) {
						R33cell3.setCellValue(record.getR33_NO_OF_ACS().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

					// R33 Col E
					Cell R33cell4 = row.createCell(6);
					if (record.getR33_INTEREST_RATE() != null) {
						R33cell4.setCellValue(record.getR33_INTEREST_RATE().doubleValue());
						R33cell4.setCellStyle(numberStyle);
					} else {
						R33cell4.setCellValue("");
						R33cell4.setCellStyle(textStyle);
					}
					// R34 Col B
					row = sheet.getRow(33);
					Cell R34cell1 = row.createCell(3);
					if (record.getR34_ORIGINAL_AMT() != null) {
						R34cell1.setCellValue(record.getR34_ORIGINAL_AMT().doubleValue());
						R34cell1.setCellStyle(numberStyle);
					} else {
						R34cell1.setCellValue("");
						R34cell1.setCellStyle(textStyle);
					}

					// R34 Col C
					Cell R34cell2 = row.createCell(4);
					if (record.getR34_BALANCE_OUTSTANDING() != null) {
						R34cell2.setCellValue(record.getR34_BALANCE_OUTSTANDING().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}
					// R34 Col D
					Cell R34cell3 = row.createCell(5);
					if (record.getR34_NO_OF_ACS() != null) {
						R34cell3.setCellValue(record.getR34_NO_OF_ACS().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}

					// R34 Col E
					Cell R34cell4 = row.createCell(6);
					if (record.getR34_INTEREST_RATE() != null) {
						R34cell4.setCellValue(record.getR34_INTEREST_RATE().doubleValue());
						R34cell4.setCellStyle(numberStyle);
					} else {
						R34cell4.setCellValue("");
						R34cell4.setCellStyle(textStyle);
					}
					// R35 Col B
					row = sheet.getRow(34);

					Cell R35cell1 = row.createCell(3);
					if (record.getR35_ORIGINAL_AMT() != null) {
						R35cell1.setCellValue(record.getR35_ORIGINAL_AMT().doubleValue());
						R35cell1.setCellStyle(numberStyle);
					} else {
						R35cell1.setCellValue("");
						R35cell1.setCellStyle(textStyle);
					}

					// R35 Col C
					Cell R35cell2 = row.createCell(4);
					if (record.getR35_BALANCE_OUTSTANDING() != null) {
						R35cell2.setCellValue(record.getR35_BALANCE_OUTSTANDING().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}
					// R35 Col D
					Cell R35cell3 = row.createCell(5);
					if (record.getR35_NO_OF_ACS() != null) {
						R35cell3.setCellValue(record.getR35_NO_OF_ACS().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}

					// R35 Col E
					Cell R35cell4 = row.createCell(6);
					if (record.getR35_INTEREST_RATE() != null) {
						R35cell4.setCellValue(record.getR35_INTEREST_RATE().doubleValue());
						R35cell4.setCellStyle(numberStyle);
					} else {
						R35cell4.setCellValue("");
						R35cell4.setCellStyle(textStyle);
					}
					// R36 Col B
					row = sheet.getRow(35);
					Cell R36cell1 = row.createCell(3);
					if (record.getR36_ORIGINAL_AMT() != null) {
						R36cell1.setCellValue(record.getR36_ORIGINAL_AMT().doubleValue());
						R36cell1.setCellStyle(numberStyle);
					} else {
						R36cell1.setCellValue("");
						R36cell1.setCellStyle(textStyle);
					}

					// R36 Col C
					Cell R36cell2 = row.createCell(4);
					if (record.getR36_BALANCE_OUTSTANDING() != null) {
						R36cell2.setCellValue(record.getR36_BALANCE_OUTSTANDING().doubleValue());
						R36cell2.setCellStyle(numberStyle);
					} else {
						R36cell2.setCellValue("");
						R36cell2.setCellStyle(textStyle);
					}
					// R36 Col D
					Cell R36cell3 = row.createCell(5);
					if (record.getR36_NO_OF_ACS() != null) {
						R36cell3.setCellValue(record.getR36_NO_OF_ACS().doubleValue());
						R36cell3.setCellStyle(numberStyle);
					} else {
						R36cell3.setCellValue("");
						R36cell3.setCellStyle(textStyle);
					}

					// R36 Col E
					Cell R36cell4 = row.createCell(6);
					if (record.getR36_INTEREST_RATE() != null) {
						R36cell4.setCellValue(record.getR36_INTEREST_RATE().doubleValue());
						R36cell4.setCellStyle(numberStyle);
					} else {
						R36cell4.setCellValue("");
						R36cell4.setCellStyle(textStyle);
					}
					// R37 Col B
					row = sheet.getRow(36);
					Cell R37cell1 = row.createCell(3);
					if (record.getR37_ORIGINAL_AMT() != null) {
						R37cell1.setCellValue(record.getR37_ORIGINAL_AMT().doubleValue());
						R37cell1.setCellStyle(numberStyle);
					} else {
						R37cell1.setCellValue("");
						R37cell1.setCellStyle(textStyle);
					}

					// R37 Col C
					Cell R37cell2 = row.createCell(4);
					if (record.getR37_BALANCE_OUTSTANDING() != null) {
						R37cell2.setCellValue(record.getR37_BALANCE_OUTSTANDING().doubleValue());
						R37cell2.setCellStyle(numberStyle);
					} else {
						R37cell2.setCellValue("");
						R37cell2.setCellStyle(textStyle);
					}
					// R37 Col D
					Cell R37cell3 = row.createCell(5);
					if (record.getR37_NO_OF_ACS() != null) {
						R37cell3.setCellValue(record.getR37_NO_OF_ACS().doubleValue());
						R37cell3.setCellStyle(numberStyle);
					} else {
						R37cell3.setCellValue("");
						R37cell3.setCellStyle(textStyle);
					}

					// R37 Col E
					Cell R37cell4 = row.createCell(6);
					if (record.getR37_INTEREST_RATE() != null) {
						R37cell4.setCellValue(record.getR37_INTEREST_RATE().doubleValue());
						R37cell4.setCellStyle(numberStyle);
					} else {
						R37cell4.setCellValue("");
						R37cell4.setCellStyle(textStyle);
					}
					row = sheet.getRow(37);
					// R37 Col E
					Cell R38cell4 = row.createCell(6);
					if (record.getR38_INTEREST_RATE() != null) {
						R38cell4.setCellValue(record.getR38_INTEREST_RATE().doubleValue());
						R38cell4.setCellStyle(numberStyle);
					} else {
						R38cell4.setCellValue("");
						R38cell4.setCellStyle(textStyle);
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

	// Archival download for email
	public byte[] BRRS_Q_STAFFEmailArchivalExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		Date reportDate = dateformat.parse(todate);
		if (type.equals("email") & version != null) {

		}
		List<Q_STAFF_Archival_Summary_Entity> dataList = Q_STAFF_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found forQ_STAFF report. Returning empty result.");
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
//EMAIL ARCHIVAL
			// --- End of Style Definitions ---

			int startRow = 8;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					Q_STAFF_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R9 Col B
					Cell R9cell1 = row.createCell(3);
					if (record.getR9_LOCAL() != null) {
						R9cell1.setCellValue(record.getR9_LOCAL().doubleValue());
						R9cell1.setCellStyle(numberStyle);
					} else {
						R9cell1.setCellValue("");
						R9cell1.setCellStyle(textStyle);
					}

					// R9 Col C
					Cell R9cell2 = row.createCell(4);
					if (record.getR9_EXPARIATES() != null) {
						R9cell2.setCellValue(record.getR9_EXPARIATES().doubleValue());
						R9cell2.setCellStyle(numberStyle);
					} else {
						R9cell2.setCellValue("");
						R9cell2.setCellStyle(textStyle);
					}

					// R9 Col C
					Cell R9cell3 = row.createCell(5);
					if (record.getR9_TOTAL() != null) {
						R9cell3.setCellValue(record.getR9_TOTAL().doubleValue());
						R9cell3.setCellStyle(numberStyle);
					} else {
						R9cell3.setCellValue("");
						R9cell3.setCellStyle(textStyle);
					}
					// R10 Col B
					row = sheet.getRow(9);
					// R10 Col B
					Cell R10cell1 = row.createCell(3);
					if (record.getR10_LOCAL() != null) {
						R10cell1.setCellValue(record.getR10_LOCAL().doubleValue());
						R10cell1.setCellStyle(numberStyle);
					} else {
						R10cell1.setCellValue("");
						R10cell1.setCellStyle(textStyle);
					}

					// R10 Col C
					Cell R10cell2 = row.createCell(4);
					if (record.getR10_EXPARIATES() != null) {
						R10cell2.setCellValue(record.getR10_EXPARIATES().doubleValue());
						R10cell2.setCellStyle(numberStyle);
					} else {
						R10cell2.setCellValue("");
						R10cell2.setCellStyle(textStyle);
					}
					Cell R10cell3 = row.createCell(5);
					if (record.getR10_TOTAL() != null) {
						R10cell3.setCellValue(record.getR10_TOTAL().doubleValue());
						R10cell3.setCellStyle(numberStyle);
					} else {
						R10cell3.setCellValue("");
						R10cell3.setCellStyle(textStyle);
					}
					// R11 Col B
					row = sheet.getRow(10);

					// R11 Col B
					Cell R11cell1 = row.createCell(3);
					if (record.getR11_LOCAL() != null) {
						R11cell1.setCellValue(record.getR11_LOCAL().doubleValue());
						R11cell1.setCellStyle(numberStyle);
					} else {
						R11cell1.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}

					// R11 Col C
					Cell R11cell2 = row.createCell(4);
					if (record.getR11_EXPARIATES() != null) {
						R11cell2.setCellValue(record.getR11_EXPARIATES().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}

					// R11 TOTAL
					Cell R11cell3 = row.createCell(5);
					if (record.getR11_TOTAL() != null) {
						R11cell3.setCellValue(record.getR11_TOTAL().doubleValue());
						R11cell3.setCellStyle(numberStyle);
					} else {
						R11cell3.setCellValue("");
						R11cell3.setCellStyle(textStyle);
					}
					// R12 Col B
					row = sheet.getRow(11);

					// R12 Col B
					Cell R12cell1 = row.createCell(3);
					if (record.getR12_LOCAL() != null) {
						R12cell1.setCellValue(record.getR12_LOCAL().doubleValue());
						R12cell1.setCellStyle(numberStyle);
					} else {
						R12cell1.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

					// R12 Col C
					Cell R12cell2 = row.createCell(4);
					if (record.getR12_EXPARIATES() != null) {
						R12cell2.setCellValue(record.getR12_EXPARIATES().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}

					// R12 TOTAL
					Cell R12cell3 = row.createCell(5);
					if (record.getR12_TOTAL() != null) {
						R12cell3.setCellValue(record.getR12_TOTAL().doubleValue());
						R12cell3.setCellStyle(numberStyle);
					} else {
						R12cell3.setCellValue("");
						R12cell3.setCellStyle(textStyle);
					}

					// R13 Col B
					row = sheet.getRow(12);

					// R13 Col B
					Cell R13cell1 = row.createCell(3);
					if (record.getR13_LOCAL() != null) {
						R13cell1.setCellValue(record.getR13_LOCAL().doubleValue());
						R13cell1.setCellStyle(numberStyle);
					} else {
						R13cell1.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

					// R13 Col C
					Cell R13cell2 = row.createCell(4);
					if (record.getR13_EXPARIATES() != null) {
						R13cell2.setCellValue(record.getR13_EXPARIATES().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}

					// R13 TOTAL
					Cell R13cell3 = row.createCell(5);
					if (record.getR13_TOTAL() != null) {
						R13cell3.setCellValue(record.getR13_TOTAL().doubleValue());
						R13cell3.setCellStyle(numberStyle);
					} else {
						R13cell3.setCellValue("");
						R13cell3.setCellStyle(textStyle);
					}

					// R14 Col B
					row = sheet.getRow(13);

					// R14 Col B
					Cell R14cell1 = row.createCell(3);
					if (record.getR14_LOCAL() != null) {
						R14cell1.setCellValue(record.getR14_LOCAL().doubleValue());
						R14cell1.setCellStyle(numberStyle);
					} else {
						R14cell1.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

					// R14 Col C
					Cell R14cell2 = row.createCell(4);
					if (record.getR14_EXPARIATES() != null) {
						R14cell2.setCellValue(record.getR14_EXPARIATES().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}

					// R14 TOTAL
					Cell R14cell3 = row.createCell(5);
					if (record.getR14_TOTAL() != null) {
						R14cell3.setCellValue(record.getR14_TOTAL().doubleValue());
						R14cell3.setCellStyle(numberStyle);
					} else {
						R14cell3.setCellValue("");
						R14cell3.setCellStyle(textStyle);
					}

					// ================= R21 =================
					row = sheet.getRow(20);

					Cell R21cell1 = row.createCell(3);
					if (record.getR21_LOCAL() != null) {
						R21cell1.setCellValue(record.getR21_LOCAL().doubleValue());
						R21cell1.setCellStyle(numberStyle);
					} else {
						R21cell1.setCellValue("");
						R21cell1.setCellStyle(textStyle);
					}

					Cell R21cell2 = row.createCell(4);
					if (record.getR21_EXPARIATES() != null) {
						R21cell2.setCellValue(record.getR21_EXPARIATES().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);
					}

					Cell R21cell3 = row.createCell(5);
					if (record.getR21_TOTAL() != null) {
						R21cell3.setCellValue(record.getR21_TOTAL().doubleValue());
						R21cell3.setCellStyle(numberStyle);
					} else {
						R21cell3.setCellValue("");
						R21cell3.setCellStyle(textStyle);
					}

					// ================= R22 =================
					row = sheet.getRow(21);

					Cell R22cell1 = row.createCell(3);
					if (record.getR22_LOCAL() != null) {
						R22cell1.setCellValue(record.getR22_LOCAL().doubleValue());
						R22cell1.setCellStyle(numberStyle);
					} else {
						R22cell1.setCellValue("");
						R22cell1.setCellStyle(textStyle);
					}

					Cell R22cell2 = row.createCell(4);
					if (record.getR22_EXPARIATES() != null) {
						R22cell2.setCellValue(record.getR22_EXPARIATES().doubleValue());
						R22cell2.setCellStyle(numberStyle);
					} else {
						R22cell2.setCellValue("");
						R22cell2.setCellStyle(textStyle);
					}

					Cell R22cell3 = row.createCell(5);
					if (record.getR22_TOTAL() != null) {
						R22cell3.setCellValue(record.getR22_TOTAL().doubleValue());
						R22cell3.setCellStyle(numberStyle);
					} else {
						R22cell3.setCellValue("");
						R22cell3.setCellStyle(textStyle);
					}

					// ================= R23 =================
					row = sheet.getRow(22);

					Cell R23cell1 = row.createCell(3);
					if (record.getR23_LOCAL() != null) {
						R23cell1.setCellValue(record.getR23_LOCAL().doubleValue());
						R23cell1.setCellStyle(numberStyle);
					} else {
						R23cell1.setCellValue("");
						R23cell1.setCellStyle(textStyle);
					}

					Cell R23cell2 = row.createCell(4);
					if (record.getR23_EXPARIATES() != null) {
						R23cell2.setCellValue(record.getR23_EXPARIATES().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}

					Cell R23cell3 = row.createCell(5);
					if (record.getR23_TOTAL() != null) {
						R23cell3.setCellValue(record.getR23_TOTAL().doubleValue());
						R23cell3.setCellStyle(numberStyle);
					} else {
						R23cell3.setCellValue("");
						R23cell3.setCellStyle(textStyle);
					}

					// ================= R24 =================
					row = sheet.getRow(23);

					Cell R24cell1 = row.createCell(3);
					if (record.getR24_LOCAL() != null) {
						R24cell1.setCellValue(record.getR24_LOCAL().doubleValue());
						R24cell1.setCellStyle(numberStyle);
					} else {
						R24cell1.setCellValue("");
						R24cell1.setCellStyle(textStyle);
					}

					Cell R24cell2 = row.createCell(4);
					if (record.getR24_EXPARIATES() != null) {
						R24cell2.setCellValue(record.getR24_EXPARIATES().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}

					Cell R24cell3 = row.createCell(5);
					if (record.getR24_TOTAL() != null) {
						R24cell3.setCellValue(record.getR24_TOTAL().doubleValue());
						R24cell3.setCellStyle(numberStyle);
					} else {
						R24cell3.setCellValue("");
						R24cell3.setCellStyle(textStyle);
					}

					// ================= R25 =================
					row = sheet.getRow(24);

					Cell R25cell1 = row.createCell(3);
					if (record.getR25_LOCAL() != null) {
						R25cell1.setCellValue(record.getR25_LOCAL().doubleValue());
						R25cell1.setCellStyle(numberStyle);
					} else {
						R25cell1.setCellValue("");
						R25cell1.setCellStyle(textStyle);
					}

					Cell R25cell2 = row.createCell(4);
					if (record.getR25_EXPARIATES() != null) {
						R25cell2.setCellValue(record.getR25_EXPARIATES().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					Cell R25cell3 = row.createCell(5);
					if (record.getR25_TOTAL() != null) {
						R25cell3.setCellValue(record.getR25_TOTAL().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}

					// ================= R26 =================
					row = sheet.getRow(25);

					Cell R26cell1 = row.createCell(3);
					if (record.getR26_LOCAL() != null) {
						R26cell1.setCellValue(record.getR26_LOCAL().doubleValue());
						R26cell1.setCellStyle(numberStyle);
					} else {
						R26cell1.setCellValue("");
						R26cell1.setCellStyle(textStyle);
					}

					Cell R26cell2 = row.createCell(4);
					if (record.getR26_EXPARIATES() != null) {
						R26cell2.setCellValue(record.getR26_EXPARIATES().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					Cell R26cell3 = row.createCell(5);
					if (record.getR26_TOTAL() != null) {
						R26cell3.setCellValue(record.getR26_TOTAL().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}

					// ================= R27 =================
					row = sheet.getRow(26);

					Cell R27cell1 = row.createCell(3);
					if (record.getR27_LOCAL() != null) {
						R27cell1.setCellValue(record.getR27_LOCAL().doubleValue());
						R27cell1.setCellStyle(numberStyle);
					} else {
						R27cell1.setCellValue("");
						R27cell1.setCellStyle(textStyle);
					}

					Cell R27cell2 = row.createCell(4);
					if (record.getR27_EXPARIATES() != null) {
						R27cell2.setCellValue(record.getR27_EXPARIATES().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}

					Cell R27cell3 = row.createCell(5);
					if (record.getR27_TOTAL() != null) {
						R27cell3.setCellValue(record.getR27_TOTAL().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);
					}

					// TABLE 3
					// R33 Col B
					row = sheet.getRow(32);
					Cell R33cell1 = row.createCell(3);
					if (record.getR33_ORIGINAL_AMT() != null) {
						R33cell1.setCellValue(record.getR33_ORIGINAL_AMT().doubleValue());
						R33cell1.setCellStyle(numberStyle);
					} else {
						R33cell1.setCellValue("");
						R33cell1.setCellStyle(textStyle);
					}

					// R33 Col C
					Cell R33cell2 = row.createCell(4);
					if (record.getR33_BALANCE_OUTSTANDING() != null) {
						R33cell2.setCellValue(record.getR33_BALANCE_OUTSTANDING().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}
					// R33 Col D
					Cell R33cell3 = row.createCell(5);
					if (record.getR33_NO_OF_ACS() != null) {
						R33cell3.setCellValue(record.getR33_NO_OF_ACS().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

					// R33 Col E
					Cell R33cell4 = row.createCell(6);
					if (record.getR33_INTEREST_RATE() != null) {
						R33cell4.setCellValue(record.getR33_INTEREST_RATE().doubleValue());
						R33cell4.setCellStyle(numberStyle);
					} else {
						R33cell4.setCellValue("");
						R33cell4.setCellStyle(textStyle);
					}
					// R34 Col B
					row = sheet.getRow(33);
					Cell R34cell1 = row.createCell(3);
					if (record.getR34_ORIGINAL_AMT() != null) {
						R34cell1.setCellValue(record.getR34_ORIGINAL_AMT().doubleValue());
						R34cell1.setCellStyle(numberStyle);
					} else {
						R34cell1.setCellValue("");
						R34cell1.setCellStyle(textStyle);
					}

					// R34 Col C
					Cell R34cell2 = row.createCell(4);
					if (record.getR34_BALANCE_OUTSTANDING() != null) {
						R34cell2.setCellValue(record.getR34_BALANCE_OUTSTANDING().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}
					// R34 Col D
					Cell R34cell3 = row.createCell(5);
					if (record.getR34_NO_OF_ACS() != null) {
						R34cell3.setCellValue(record.getR34_NO_OF_ACS().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}

					// R34 Col E
					Cell R34cell4 = row.createCell(6);
					if (record.getR34_INTEREST_RATE() != null) {
						R34cell4.setCellValue(record.getR34_INTEREST_RATE().doubleValue());
						R34cell4.setCellStyle(numberStyle);
					} else {
						R34cell4.setCellValue("");
						R34cell4.setCellStyle(textStyle);
					}
					// R35 Col B
					row = sheet.getRow(34);

					Cell R35cell1 = row.createCell(3);
					if (record.getR35_ORIGINAL_AMT() != null) {
						R35cell1.setCellValue(record.getR35_ORIGINAL_AMT().doubleValue());
						R35cell1.setCellStyle(numberStyle);
					} else {
						R35cell1.setCellValue("");
						R35cell1.setCellStyle(textStyle);
					}

					// R35 Col C
					Cell R35cell2 = row.createCell(4);
					if (record.getR35_BALANCE_OUTSTANDING() != null) {
						R35cell2.setCellValue(record.getR35_BALANCE_OUTSTANDING().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}
					// R35 Col D
					Cell R35cell3 = row.createCell(5);
					if (record.getR35_NO_OF_ACS() != null) {
						R35cell3.setCellValue(record.getR35_NO_OF_ACS().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}

					// R35 Col E
					Cell R35cell4 = row.createCell(6);
					if (record.getR35_INTEREST_RATE() != null) {
						R35cell4.setCellValue(record.getR35_INTEREST_RATE().doubleValue());
						R35cell4.setCellStyle(numberStyle);
					} else {
						R35cell4.setCellValue("");
						R35cell4.setCellStyle(textStyle);
					}
					// R36 Col B
					row = sheet.getRow(35);
					Cell R36cell1 = row.createCell(3);
					if (record.getR36_ORIGINAL_AMT() != null) {
						R36cell1.setCellValue(record.getR36_ORIGINAL_AMT().doubleValue());
						R36cell1.setCellStyle(numberStyle);
					} else {
						R36cell1.setCellValue("");
						R36cell1.setCellStyle(textStyle);
					}

					// R36 Col C
					Cell R36cell2 = row.createCell(4);
					if (record.getR36_BALANCE_OUTSTANDING() != null) {
						R36cell2.setCellValue(record.getR36_BALANCE_OUTSTANDING().doubleValue());
						R36cell2.setCellStyle(numberStyle);
					} else {
						R36cell2.setCellValue("");
						R36cell2.setCellStyle(textStyle);
					}
					// R36 Col D
					Cell R36cell3 = row.createCell(5);
					if (record.getR36_NO_OF_ACS() != null) {
						R36cell3.setCellValue(record.getR36_NO_OF_ACS().doubleValue());
						R36cell3.setCellStyle(numberStyle);
					} else {
						R36cell3.setCellValue("");
						R36cell3.setCellStyle(textStyle);
					}

					// R36 Col E
					Cell R36cell4 = row.createCell(6);
					if (record.getR36_INTEREST_RATE() != null) {
						R36cell4.setCellValue(record.getR36_INTEREST_RATE().doubleValue());
						R36cell4.setCellStyle(numberStyle);
					} else {
						R36cell4.setCellValue("");
						R36cell4.setCellStyle(textStyle);
					}
					// R37 Col B
					row = sheet.getRow(36);
					Cell R37cell1 = row.createCell(3);
					if (record.getR37_ORIGINAL_AMT() != null) {
						R37cell1.setCellValue(record.getR37_ORIGINAL_AMT().doubleValue());
						R37cell1.setCellStyle(numberStyle);
					} else {
						R37cell1.setCellValue("");
						R37cell1.setCellStyle(textStyle);
					}

					// R37 Col C
					Cell R37cell2 = row.createCell(4);
					if (record.getR37_BALANCE_OUTSTANDING() != null) {
						R37cell2.setCellValue(record.getR37_BALANCE_OUTSTANDING().doubleValue());
						R37cell2.setCellStyle(numberStyle);
					} else {
						R37cell2.setCellValue("");
						R37cell2.setCellStyle(textStyle);
					}
					// R37 Col D
					Cell R37cell3 = row.createCell(5);
					if (record.getR37_NO_OF_ACS() != null) {
						R37cell3.setCellValue(record.getR37_NO_OF_ACS().doubleValue());
						R37cell3.setCellStyle(numberStyle);
					} else {
						R37cell3.setCellValue("");
						R37cell3.setCellStyle(textStyle);
					}

					// R37 Col E
					Cell R37cell4 = row.createCell(6);
					if (record.getR37_INTEREST_RATE() != null) {
						R37cell4.setCellValue(record.getR37_INTEREST_RATE().doubleValue());
						R37cell4.setCellStyle(numberStyle);
					} else {
						R37cell4.setCellValue("");
						R37cell4.setCellStyle(textStyle);
					}
					row = sheet.getRow(37);
					// R37 Col E
					Cell R38cell4 = row.createCell(6);
					if (record.getR38_INTEREST_RATE() != null) {
						R38cell4.setCellValue(record.getR38_INTEREST_RATE().doubleValue());
						R38cell4.setCellStyle(numberStyle);
					} else {
						R38cell4.setCellValue("");
						R38cell4.setCellStyle(textStyle);
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

	// Resub Download
	// Resub Download
	public byte[] BRRS_Q_STAFFResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		Date reportDate = dateformat.parse(todate);

		if (type.equals("RESUB") & version != null) {

		}
		List<Q_STAFF_Resub_Summary_Entity> dataList = Q_STAFF_Resub_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found forQ_STAFF report. Returning empty result.");
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
					Q_STAFF_Resub_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R9 Col B
					Cell R9cell1 = row.createCell(1);
					if (record.getR9_LOCAL() != null) {
						R9cell1.setCellValue(record.getR9_LOCAL().doubleValue());
						R9cell1.setCellStyle(numberStyle);
					} else {
						R9cell1.setCellValue("");
						R9cell1.setCellStyle(textStyle);
					}

					// R9 Col C
					Cell R9cell2 = row.createCell(2);
					if (record.getR9_EXPARIATES() != null) {
						R9cell2.setCellValue(record.getR9_EXPARIATES().doubleValue());
						R9cell2.setCellStyle(numberStyle);
					} else {
						R9cell2.setCellValue("");
						R9cell2.setCellStyle(textStyle);
					}
					// R10 Col B
					row = sheet.getRow(9);
					// R10 Col B
					Cell R10cell1 = row.createCell(1);
					if (record.getR10_LOCAL() != null) {
						R10cell1.setCellValue(record.getR10_LOCAL().doubleValue());
						R10cell1.setCellStyle(numberStyle);
					} else {
						R10cell1.setCellValue("");
						R10cell1.setCellStyle(textStyle);
					}

					// R10 Col C
					Cell R10cell2 = row.createCell(2);
					if (record.getR10_EXPARIATES() != null) {
						R10cell2.setCellValue(record.getR10_EXPARIATES().doubleValue());
						R10cell2.setCellStyle(numberStyle);
					} else {
						R10cell2.setCellValue("");
						R10cell2.setCellStyle(textStyle);
					}
					// R11 Col B
					row = sheet.getRow(10);
					// R11 Col B
					Cell R11cell1 = row.createCell(1);
					if (record.getR11_LOCAL() != null) {
						R11cell1.setCellValue(record.getR11_LOCAL().doubleValue());
						R11cell1.setCellStyle(numberStyle);
					} else {
						R11cell1.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}

					// R11 Col C
					Cell R11cell2 = row.createCell(2);
					if (record.getR11_EXPARIATES() != null) {
						R11cell2.setCellValue(record.getR11_EXPARIATES().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}
					// R12 Col B
					row = sheet.getRow(11);

					Cell R12cell1 = row.createCell(1);
					if (record.getR12_LOCAL() != null) {
						R12cell1.setCellValue(record.getR12_LOCAL().doubleValue());
						R12cell1.setCellStyle(numberStyle);
					} else {
						R12cell1.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

					// R12 Col C
					Cell R12cell2 = row.createCell(2);
					if (record.getR12_EXPARIATES() != null) {
						R12cell2.setCellValue(record.getR12_EXPARIATES().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}
					// R13 Col B
					row = sheet.getRow(12);

					Cell R13cell1 = row.createCell(1);
					if (record.getR13_LOCAL() != null) {
						R13cell1.setCellValue(record.getR13_LOCAL().doubleValue());
						R13cell1.setCellStyle(numberStyle);
					} else {
						R13cell1.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

					// R13 Col C
					Cell R13cell2 = row.createCell(2);
					if (record.getR13_EXPARIATES() != null) {
						R13cell2.setCellValue(record.getR13_EXPARIATES().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}
					// R14 Col B
					row = sheet.getRow(13);
					Cell R14cell1 = row.createCell(1);
					if (record.getR14_LOCAL() != null) {
						R14cell1.setCellValue(record.getR14_LOCAL().doubleValue());
						R14cell1.setCellStyle(numberStyle);
					} else {
						R14cell1.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

					// R14 Col C
					Cell R14cell2 = row.createCell(2);
					if (record.getR14_EXPARIATES() != null) {
						R14cell2.setCellValue(record.getR14_EXPARIATES().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}

					// TABLE 2
					// R21 Col B
					row = sheet.getRow(20);
					Cell R21cell1 = row.createCell(1);
					if (record.getR21_LOCAL() != null) {
						R21cell1.setCellValue(record.getR21_LOCAL().doubleValue());
						R21cell1.setCellStyle(numberStyle);
					} else {
						R21cell1.setCellValue("");
						R21cell1.setCellStyle(textStyle);
					}
					// R21 COL C
					Cell R21cell2 = row.createCell(2);
					if (record.getR21_EXPARIATES() != null) {
						R21cell2.setCellValue(record.getR21_EXPARIATES().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);
					}
					// R22 Col B
					row = sheet.getRow(21);
					Cell R22cell1 = row.createCell(1);
					if (record.getR22_LOCAL() != null) {
						R22cell1.setCellValue(record.getR22_LOCAL().doubleValue());
						R22cell1.setCellStyle(numberStyle);
					} else {
						R22cell1.setCellValue("");
						R22cell1.setCellStyle(textStyle);
					}

					// R22 Col C
					Cell R22cell2 = row.createCell(2);
					if (record.getR22_EXPARIATES() != null) {
						R22cell2.setCellValue(record.getR22_EXPARIATES().doubleValue());
						R22cell2.setCellStyle(numberStyle);
					} else {
						R22cell2.setCellValue("");
						R22cell2.setCellStyle(textStyle);
					}
					// R23 Col B
					row = sheet.getRow(22);
					// R23 Col B
					Cell R23cell1 = row.createCell(1);
					if (record.getR23_LOCAL() != null) {
						R23cell1.setCellValue(record.getR23_LOCAL().doubleValue());
						R23cell1.setCellStyle(numberStyle);
					} else {
						R23cell1.setCellValue("");
						R23cell1.setCellStyle(textStyle);
					}

					// R23 Col C
					Cell R23cell2 = row.createCell(2);
					if (record.getR23_EXPARIATES() != null) {
						R23cell2.setCellValue(record.getR23_EXPARIATES().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}
					// R24 Col B
					row = sheet.getRow(23);
					// R24 Col B
					Cell R24cell1 = row.createCell(1);
					if (record.getR24_LOCAL() != null) {
						R24cell1.setCellValue(record.getR24_LOCAL().doubleValue());
						R24cell1.setCellStyle(numberStyle);
					} else {
						R24cell1.setCellValue("");
						R24cell1.setCellStyle(textStyle);
					}

					// R24 Col C
					Cell R24cell2 = row.createCell(2);
					if (record.getR24_EXPARIATES() != null) {
						R24cell2.setCellValue(record.getR24_EXPARIATES().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}
					// R25 Col B
					row = sheet.getRow(24);
					// R25 Col B
					Cell R25cell1 = row.createCell(1);
					if (record.getR25_LOCAL() != null) {
						R25cell1.setCellValue(record.getR25_LOCAL().doubleValue());
						R25cell1.setCellStyle(numberStyle);
					} else {
						R25cell1.setCellValue("");
						R25cell1.setCellStyle(textStyle);
					}

					// R25 Col C
					Cell R25cell2 = row.createCell(2);
					if (record.getR25_EXPARIATES() != null) {
						R25cell2.setCellValue(record.getR25_EXPARIATES().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}
					// R26 Col B
					row = sheet.getRow(25);
					// R26 Col B
					Cell R26cell1 = row.createCell(1);
					if (record.getR26_LOCAL() != null) {
						R26cell1.setCellValue(record.getR26_LOCAL().doubleValue());
						R26cell1.setCellStyle(numberStyle);
					} else {
						R26cell1.setCellValue("");
						R26cell1.setCellStyle(textStyle);
					}

					// R26 Col C
					Cell R26cell2 = row.createCell(2);
					if (record.getR26_EXPARIATES() != null) {
						R26cell2.setCellValue(record.getR26_EXPARIATES().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}
					// R27 Col B
					row = sheet.getRow(26);
					// R27 Col B
					Cell R27cell1 = row.createCell(1);
					if (record.getR27_LOCAL() != null) {
						R27cell1.setCellValue(record.getR27_LOCAL().doubleValue());
						R27cell1.setCellStyle(numberStyle);
					} else {
						R27cell1.setCellValue("");
						R27cell1.setCellStyle(textStyle);
					}

					// R27 Col C
					Cell R27cell2 = row.createCell(2);
					if (record.getR27_EXPARIATES() != null) {
						R27cell2.setCellValue(record.getR27_EXPARIATES().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}
					// TABLE 3
					// R33 Col B
					row = sheet.getRow(32);
					Cell R33cell1 = row.createCell(1);
					if (record.getR33_ORIGINAL_AMT() != null) {
						R33cell1.setCellValue(record.getR33_ORIGINAL_AMT().doubleValue());
						R33cell1.setCellStyle(numberStyle);
					} else {
						R33cell1.setCellValue("");
						R33cell1.setCellStyle(textStyle);
					}

					// R33 Col C
					Cell R33cell2 = row.createCell(2);
					if (record.getR33_BALANCE_OUTSTANDING() != null) {
						R33cell2.setCellValue(record.getR33_BALANCE_OUTSTANDING().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}
					// R33 Col D
					Cell R33cell3 = row.createCell(3);
					if (record.getR33_NO_OF_ACS() != null) {
						R33cell3.setCellValue(record.getR33_NO_OF_ACS().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

					// R33 Col E
					Cell R33cell4 = row.createCell(4);
					if (record.getR33_INTEREST_RATE() != null) {
						R33cell4.setCellValue(record.getR33_INTEREST_RATE().doubleValue());
						R33cell4.setCellStyle(numberStyle);
					} else {
						R33cell4.setCellValue("");
						R33cell4.setCellStyle(textStyle);
					}
					// R34 Col B
					row = sheet.getRow(33);
					Cell R34cell1 = row.createCell(1);
					if (record.getR34_ORIGINAL_AMT() != null) {
						R34cell1.setCellValue(record.getR34_ORIGINAL_AMT().doubleValue());
						R34cell1.setCellStyle(numberStyle);
					} else {
						R34cell1.setCellValue("");
						R34cell1.setCellStyle(textStyle);
					}

					// R34 Col C
					Cell R34cell2 = row.createCell(2);
					if (record.getR34_BALANCE_OUTSTANDING() != null) {
						R34cell2.setCellValue(record.getR34_BALANCE_OUTSTANDING().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}
					// R34 Col D
					Cell R34cell3 = row.createCell(3);
					if (record.getR34_NO_OF_ACS() != null) {
						R34cell3.setCellValue(record.getR34_NO_OF_ACS().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}

					// R34 Col E
					Cell R34cell4 = row.createCell(4);
					if (record.getR34_INTEREST_RATE() != null) {
						R34cell4.setCellValue(record.getR34_INTEREST_RATE().doubleValue());
						R34cell4.setCellStyle(numberStyle);
					} else {
						R34cell4.setCellValue("");
						R34cell4.setCellStyle(textStyle);
					}
					// R35 Col B
					row = sheet.getRow(34);

					Cell R35cell1 = row.createCell(1);
					if (record.getR35_ORIGINAL_AMT() != null) {
						R35cell1.setCellValue(record.getR35_ORIGINAL_AMT().doubleValue());
						R35cell1.setCellStyle(numberStyle);
					} else {
						R35cell1.setCellValue("");
						R35cell1.setCellStyle(textStyle);
					}

					// R35 Col C
					Cell R35cell2 = row.createCell(2);
					if (record.getR35_BALANCE_OUTSTANDING() != null) {
						R35cell2.setCellValue(record.getR35_BALANCE_OUTSTANDING().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}
					// R35 Col D
					Cell R35cell3 = row.createCell(3);
					if (record.getR35_NO_OF_ACS() != null) {
						R35cell3.setCellValue(record.getR35_NO_OF_ACS().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}

					// R35 Col E
					Cell R35cell4 = row.createCell(4);
					if (record.getR35_INTEREST_RATE() != null) {
						R35cell4.setCellValue(record.getR35_INTEREST_RATE().doubleValue());
						R35cell4.setCellStyle(numberStyle);
					} else {
						R35cell4.setCellValue("");
						R35cell4.setCellStyle(textStyle);
					}
					// R36 Col B
					row = sheet.getRow(35);
					Cell R36cell1 = row.createCell(1);
					if (record.getR36_ORIGINAL_AMT() != null) {
						R36cell1.setCellValue(record.getR36_ORIGINAL_AMT().doubleValue());
						R36cell1.setCellStyle(numberStyle);
					} else {
						R36cell1.setCellValue("");
						R36cell1.setCellStyle(textStyle);
					}

					// R36 Col C
					Cell R36cell2 = row.createCell(2);
					if (record.getR36_BALANCE_OUTSTANDING() != null) {
						R36cell2.setCellValue(record.getR36_BALANCE_OUTSTANDING().doubleValue());
						R36cell2.setCellStyle(numberStyle);
					} else {
						R36cell2.setCellValue("");
						R36cell2.setCellStyle(textStyle);
					}
					// R36 Col D
					Cell R36cell3 = row.createCell(3);
					if (record.getR36_NO_OF_ACS() != null) {
						R36cell3.setCellValue(record.getR36_NO_OF_ACS().doubleValue());
						R36cell3.setCellStyle(numberStyle);
					} else {
						R36cell3.setCellValue("");
						R36cell3.setCellStyle(textStyle);
					}

					// R36 Col E
					Cell R36cell4 = row.createCell(4);
					if (record.getR36_INTEREST_RATE() != null) {
						R36cell4.setCellValue(record.getR36_INTEREST_RATE().doubleValue());
						R36cell4.setCellStyle(numberStyle);
					} else {
						R36cell4.setCellValue("");
						R36cell4.setCellStyle(textStyle);
					}
					// R37 Col B
					row = sheet.getRow(36);
					Cell R37cell1 = row.createCell(1);
					if (record.getR37_ORIGINAL_AMT() != null) {
						R37cell1.setCellValue(record.getR37_ORIGINAL_AMT().doubleValue());
						R37cell1.setCellStyle(numberStyle);
					} else {
						R37cell1.setCellValue("");
						R37cell1.setCellStyle(textStyle);
					}

					// R37 Col C
					Cell R37cell2 = row.createCell(2);
					if (record.getR37_BALANCE_OUTSTANDING() != null) {
						R37cell2.setCellValue(record.getR37_BALANCE_OUTSTANDING().doubleValue());
						R37cell2.setCellStyle(numberStyle);
					} else {
						R37cell2.setCellValue("");
						R37cell2.setCellStyle(textStyle);
					}
					// R37 Col D
					Cell R37cell3 = row.createCell(3);
					if (record.getR37_NO_OF_ACS() != null) {
						R37cell3.setCellValue(record.getR37_NO_OF_ACS().doubleValue());
						R37cell3.setCellStyle(numberStyle);
					} else {
						R37cell3.setCellValue("");
						R37cell3.setCellStyle(textStyle);
					}

					// R37 Col E
					Cell R37cell4 = row.createCell(4);
					if (record.getR37_INTEREST_RATE() != null) {
						R37cell4.setCellValue(record.getR37_INTEREST_RATE().doubleValue());
						R37cell4.setCellStyle(numberStyle);
					} else {
						R37cell4.setCellValue("");
						R37cell4.setCellStyle(textStyle);
					}
					row = sheet.getRow(37);
					// R37 Col E
					Cell R38cell4 = row.createCell(4);
					if (record.getR38_INTEREST_RATE() != null) {
						R38cell4.setCellValue(record.getR38_INTEREST_RATE().doubleValue());
						R38cell4.setCellStyle(numberStyle);
					} else {
						R38cell4.setCellValue("");
						R38cell4.setCellStyle(textStyle);
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

//Resub Email Format
	// Archival download for email
	public byte[] BRRS_Q_STAFFEmailResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		Date reportDate = dateformat.parse(todate);
		if (type.equals("RESUB") & version != null) {

		}
		List<Q_STAFF_Resub_Summary_Entity> dataList = Q_STAFF_Resub_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found forQ_STAFF report. Returning empty result.");
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
//EMAIL EXCEL
			// --- End of Style Definitions ---

			int startRow = 8;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					Q_STAFF_Resub_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R9 Col B
					Cell R9cell1 = row.createCell(3);
					if (record.getR9_LOCAL() != null) {
						R9cell1.setCellValue(record.getR9_LOCAL().doubleValue());
						R9cell1.setCellStyle(numberStyle);
					} else {
						R9cell1.setCellValue("");
						R9cell1.setCellStyle(textStyle);
					}

					// R9 Col C
					Cell R9cell2 = row.createCell(4);
					if (record.getR9_EXPARIATES() != null) {
						R9cell2.setCellValue(record.getR9_EXPARIATES().doubleValue());
						R9cell2.setCellStyle(numberStyle);
					} else {
						R9cell2.setCellValue("");
						R9cell2.setCellStyle(textStyle);
					}

					// R9 Col C
					Cell R9cell3 = row.createCell(5);
					if (record.getR9_TOTAL() != null) {
						R9cell3.setCellValue(record.getR9_TOTAL().doubleValue());
						R9cell3.setCellStyle(numberStyle);
					} else {
						R9cell3.setCellValue("");
						R9cell3.setCellStyle(textStyle);
					}
					// R10 Col B
					row = sheet.getRow(9);
					// R10 Col B
					Cell R10cell1 = row.createCell(3);
					if (record.getR10_LOCAL() != null) {
						R10cell1.setCellValue(record.getR10_LOCAL().doubleValue());
						R10cell1.setCellStyle(numberStyle);
					} else {
						R10cell1.setCellValue("");
						R10cell1.setCellStyle(textStyle);
					}

					// R10 Col C
					Cell R10cell2 = row.createCell(4);
					if (record.getR10_EXPARIATES() != null) {
						R10cell2.setCellValue(record.getR10_EXPARIATES().doubleValue());
						R10cell2.setCellStyle(numberStyle);
					} else {
						R10cell2.setCellValue("");
						R10cell2.setCellStyle(textStyle);
					}
					Cell R10cell3 = row.createCell(5);
					if (record.getR10_TOTAL() != null) {
						R10cell3.setCellValue(record.getR10_TOTAL().doubleValue());
						R10cell3.setCellStyle(numberStyle);
					} else {
						R10cell3.setCellValue("");
						R10cell3.setCellStyle(textStyle);
					}
					// R11 Col B
					row = sheet.getRow(10);

					// R11 Col B
					Cell R11cell1 = row.createCell(3);
					if (record.getR11_LOCAL() != null) {
						R11cell1.setCellValue(record.getR11_LOCAL().doubleValue());
						R11cell1.setCellStyle(numberStyle);
					} else {
						R11cell1.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}

					// R11 Col C
					Cell R11cell2 = row.createCell(4);
					if (record.getR11_EXPARIATES() != null) {
						R11cell2.setCellValue(record.getR11_EXPARIATES().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}

					// R11 TOTAL
					Cell R11cell3 = row.createCell(5);
					if (record.getR11_TOTAL() != null) {
						R11cell3.setCellValue(record.getR11_TOTAL().doubleValue());
						R11cell3.setCellStyle(numberStyle);
					} else {
						R11cell3.setCellValue("");
						R11cell3.setCellStyle(textStyle);
					}
					// R12 Col B
					row = sheet.getRow(11);

					// R12 Col B
					Cell R12cell1 = row.createCell(3);
					if (record.getR12_LOCAL() != null) {
						R12cell1.setCellValue(record.getR12_LOCAL().doubleValue());
						R12cell1.setCellStyle(numberStyle);
					} else {
						R12cell1.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

					// R12 Col C
					Cell R12cell2 = row.createCell(4);
					if (record.getR12_EXPARIATES() != null) {
						R12cell2.setCellValue(record.getR12_EXPARIATES().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}

					// R12 TOTAL
					Cell R12cell3 = row.createCell(5);
					if (record.getR12_TOTAL() != null) {
						R12cell3.setCellValue(record.getR12_TOTAL().doubleValue());
						R12cell3.setCellStyle(numberStyle);
					} else {
						R12cell3.setCellValue("");
						R12cell3.setCellStyle(textStyle);
					}

					// R13 Col B
					row = sheet.getRow(12);

					// R13 Col B
					Cell R13cell1 = row.createCell(3);
					if (record.getR13_LOCAL() != null) {
						R13cell1.setCellValue(record.getR13_LOCAL().doubleValue());
						R13cell1.setCellStyle(numberStyle);
					} else {
						R13cell1.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

					// R13 Col C
					Cell R13cell2 = row.createCell(4);
					if (record.getR13_EXPARIATES() != null) {
						R13cell2.setCellValue(record.getR13_EXPARIATES().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}

					// R13 TOTAL
					Cell R13cell3 = row.createCell(5);
					if (record.getR13_TOTAL() != null) {
						R13cell3.setCellValue(record.getR13_TOTAL().doubleValue());
						R13cell3.setCellStyle(numberStyle);
					} else {
						R13cell3.setCellValue("");
						R13cell3.setCellStyle(textStyle);
					}

					// R14 Col B
					row = sheet.getRow(13);

					// R14 Col B
					Cell R14cell1 = row.createCell(3);
					if (record.getR14_LOCAL() != null) {
						R14cell1.setCellValue(record.getR14_LOCAL().doubleValue());
						R14cell1.setCellStyle(numberStyle);
					} else {
						R14cell1.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

					// R14 Col C
					Cell R14cell2 = row.createCell(4);
					if (record.getR14_EXPARIATES() != null) {
						R14cell2.setCellValue(record.getR14_EXPARIATES().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}

					// R14 TOTAL
					Cell R14cell3 = row.createCell(5);
					if (record.getR14_TOTAL() != null) {
						R14cell3.setCellValue(record.getR14_TOTAL().doubleValue());
						R14cell3.setCellStyle(numberStyle);
					} else {
						R14cell3.setCellValue("");
						R14cell3.setCellStyle(textStyle);
					}

					// ================= R21 =================
					row = sheet.getRow(20);

					Cell R21cell1 = row.createCell(3);
					if (record.getR21_LOCAL() != null) {
						R21cell1.setCellValue(record.getR21_LOCAL().doubleValue());
						R21cell1.setCellStyle(numberStyle);
					} else {
						R21cell1.setCellValue("");
						R21cell1.setCellStyle(textStyle);
					}

					Cell R21cell2 = row.createCell(4);
					if (record.getR21_EXPARIATES() != null) {
						R21cell2.setCellValue(record.getR21_EXPARIATES().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);
					}

					Cell R21cell3 = row.createCell(5);
					if (record.getR21_TOTAL() != null) {
						R21cell3.setCellValue(record.getR21_TOTAL().doubleValue());
						R21cell3.setCellStyle(numberStyle);
					} else {
						R21cell3.setCellValue("");
						R21cell3.setCellStyle(textStyle);
					}

					// ================= R22 =================
					row = sheet.getRow(21);

					Cell R22cell1 = row.createCell(3);
					if (record.getR22_LOCAL() != null) {
						R22cell1.setCellValue(record.getR22_LOCAL().doubleValue());
						R22cell1.setCellStyle(numberStyle);
					} else {
						R22cell1.setCellValue("");
						R22cell1.setCellStyle(textStyle);
					}

					Cell R22cell2 = row.createCell(4);
					if (record.getR22_EXPARIATES() != null) {
						R22cell2.setCellValue(record.getR22_EXPARIATES().doubleValue());
						R22cell2.setCellStyle(numberStyle);
					} else {
						R22cell2.setCellValue("");
						R22cell2.setCellStyle(textStyle);
					}

					Cell R22cell3 = row.createCell(5);
					if (record.getR22_TOTAL() != null) {
						R22cell3.setCellValue(record.getR22_TOTAL().doubleValue());
						R22cell3.setCellStyle(numberStyle);
					} else {
						R22cell3.setCellValue("");
						R22cell3.setCellStyle(textStyle);
					}

					// ================= R23 =================
					row = sheet.getRow(22);

					Cell R23cell1 = row.createCell(3);
					if (record.getR23_LOCAL() != null) {
						R23cell1.setCellValue(record.getR23_LOCAL().doubleValue());
						R23cell1.setCellStyle(numberStyle);
					} else {
						R23cell1.setCellValue("");
						R23cell1.setCellStyle(textStyle);
					}

					Cell R23cell2 = row.createCell(4);
					if (record.getR23_EXPARIATES() != null) {
						R23cell2.setCellValue(record.getR23_EXPARIATES().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}

					Cell R23cell3 = row.createCell(5);
					if (record.getR23_TOTAL() != null) {
						R23cell3.setCellValue(record.getR23_TOTAL().doubleValue());
						R23cell3.setCellStyle(numberStyle);
					} else {
						R23cell3.setCellValue("");
						R23cell3.setCellStyle(textStyle);
					}

					// ================= R24 =================
					row = sheet.getRow(23);

					Cell R24cell1 = row.createCell(3);
					if (record.getR24_LOCAL() != null) {
						R24cell1.setCellValue(record.getR24_LOCAL().doubleValue());
						R24cell1.setCellStyle(numberStyle);
					} else {
						R24cell1.setCellValue("");
						R24cell1.setCellStyle(textStyle);
					}

					Cell R24cell2 = row.createCell(4);
					if (record.getR24_EXPARIATES() != null) {
						R24cell2.setCellValue(record.getR24_EXPARIATES().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}

					Cell R24cell3 = row.createCell(5);
					if (record.getR24_TOTAL() != null) {
						R24cell3.setCellValue(record.getR24_TOTAL().doubleValue());
						R24cell3.setCellStyle(numberStyle);
					} else {
						R24cell3.setCellValue("");
						R24cell3.setCellStyle(textStyle);
					}

					// ================= R25 =================
					row = sheet.getRow(24);

					Cell R25cell1 = row.createCell(3);
					if (record.getR25_LOCAL() != null) {
						R25cell1.setCellValue(record.getR25_LOCAL().doubleValue());
						R25cell1.setCellStyle(numberStyle);
					} else {
						R25cell1.setCellValue("");
						R25cell1.setCellStyle(textStyle);
					}

					Cell R25cell2 = row.createCell(4);
					if (record.getR25_EXPARIATES() != null) {
						R25cell2.setCellValue(record.getR25_EXPARIATES().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					Cell R25cell3 = row.createCell(5);
					if (record.getR25_TOTAL() != null) {
						R25cell3.setCellValue(record.getR25_TOTAL().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}

					// ================= R26 =================
					row = sheet.getRow(25);

					Cell R26cell1 = row.createCell(3);
					if (record.getR26_LOCAL() != null) {
						R26cell1.setCellValue(record.getR26_LOCAL().doubleValue());
						R26cell1.setCellStyle(numberStyle);
					} else {
						R26cell1.setCellValue("");
						R26cell1.setCellStyle(textStyle);
					}

					Cell R26cell2 = row.createCell(4);
					if (record.getR26_EXPARIATES() != null) {
						R26cell2.setCellValue(record.getR26_EXPARIATES().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					Cell R26cell3 = row.createCell(5);
					if (record.getR26_TOTAL() != null) {
						R26cell3.setCellValue(record.getR26_TOTAL().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}

					// ================= R27 =================
					row = sheet.getRow(26);

					Cell R27cell1 = row.createCell(3);
					if (record.getR27_LOCAL() != null) {
						R27cell1.setCellValue(record.getR27_LOCAL().doubleValue());
						R27cell1.setCellStyle(numberStyle);
					} else {
						R27cell1.setCellValue("");
						R27cell1.setCellStyle(textStyle);
					}

					Cell R27cell2 = row.createCell(4);
					if (record.getR27_EXPARIATES() != null) {
						R27cell2.setCellValue(record.getR27_EXPARIATES().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}

					Cell R27cell3 = row.createCell(5);
					if (record.getR27_TOTAL() != null) {
						R27cell3.setCellValue(record.getR27_TOTAL().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);
					}

					// TABLE 3
					// R33 Col B
					row = sheet.getRow(32);
					Cell R33cell1 = row.createCell(3);
					if (record.getR33_ORIGINAL_AMT() != null) {
						R33cell1.setCellValue(record.getR33_ORIGINAL_AMT().doubleValue());
						R33cell1.setCellStyle(numberStyle);
					} else {
						R33cell1.setCellValue("");
						R33cell1.setCellStyle(textStyle);
					}

					// R33 Col C
					Cell R33cell2 = row.createCell(4);
					if (record.getR33_BALANCE_OUTSTANDING() != null) {
						R33cell2.setCellValue(record.getR33_BALANCE_OUTSTANDING().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}
					// R33 Col D
					Cell R33cell3 = row.createCell(5);
					if (record.getR33_NO_OF_ACS() != null) {
						R33cell3.setCellValue(record.getR33_NO_OF_ACS().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

					// R33 Col E
					Cell R33cell4 = row.createCell(6);
					if (record.getR33_INTEREST_RATE() != null) {
						R33cell4.setCellValue(record.getR33_INTEREST_RATE().doubleValue());
						R33cell4.setCellStyle(numberStyle);
					} else {
						R33cell4.setCellValue("");
						R33cell4.setCellStyle(textStyle);
					}
					// R34 Col B
					row = sheet.getRow(33);
					Cell R34cell1 = row.createCell(3);
					if (record.getR34_ORIGINAL_AMT() != null) {
						R34cell1.setCellValue(record.getR34_ORIGINAL_AMT().doubleValue());
						R34cell1.setCellStyle(numberStyle);
					} else {
						R34cell1.setCellValue("");
						R34cell1.setCellStyle(textStyle);
					}

					// R34 Col C
					Cell R34cell2 = row.createCell(4);
					if (record.getR34_BALANCE_OUTSTANDING() != null) {
						R34cell2.setCellValue(record.getR34_BALANCE_OUTSTANDING().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}
					// R34 Col D
					Cell R34cell3 = row.createCell(5);
					if (record.getR34_NO_OF_ACS() != null) {
						R34cell3.setCellValue(record.getR34_NO_OF_ACS().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}

					// R34 Col E
					Cell R34cell4 = row.createCell(6);
					if (record.getR34_INTEREST_RATE() != null) {
						R34cell4.setCellValue(record.getR34_INTEREST_RATE().doubleValue());
						R34cell4.setCellStyle(numberStyle);
					} else {
						R34cell4.setCellValue("");
						R34cell4.setCellStyle(textStyle);
					}
					// R35 Col B
					row = sheet.getRow(34);

					Cell R35cell1 = row.createCell(3);
					if (record.getR35_ORIGINAL_AMT() != null) {
						R35cell1.setCellValue(record.getR35_ORIGINAL_AMT().doubleValue());
						R35cell1.setCellStyle(numberStyle);
					} else {
						R35cell1.setCellValue("");
						R35cell1.setCellStyle(textStyle);
					}

					// R35 Col C
					Cell R35cell2 = row.createCell(4);
					if (record.getR35_BALANCE_OUTSTANDING() != null) {
						R35cell2.setCellValue(record.getR35_BALANCE_OUTSTANDING().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}
					// R35 Col D
					Cell R35cell3 = row.createCell(5);
					if (record.getR35_NO_OF_ACS() != null) {
						R35cell3.setCellValue(record.getR35_NO_OF_ACS().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}

					// R35 Col E
					Cell R35cell4 = row.createCell(6);
					if (record.getR35_INTEREST_RATE() != null) {
						R35cell4.setCellValue(record.getR35_INTEREST_RATE().doubleValue());
						R35cell4.setCellStyle(numberStyle);
					} else {
						R35cell4.setCellValue("");
						R35cell4.setCellStyle(textStyle);
					}
					// R36 Col B
					row = sheet.getRow(35);
					Cell R36cell1 = row.createCell(3);
					if (record.getR36_ORIGINAL_AMT() != null) {
						R36cell1.setCellValue(record.getR36_ORIGINAL_AMT().doubleValue());
						R36cell1.setCellStyle(numberStyle);
					} else {
						R36cell1.setCellValue("");
						R36cell1.setCellStyle(textStyle);
					}

					// R36 Col C
					Cell R36cell2 = row.createCell(4);
					if (record.getR36_BALANCE_OUTSTANDING() != null) {
						R36cell2.setCellValue(record.getR36_BALANCE_OUTSTANDING().doubleValue());
						R36cell2.setCellStyle(numberStyle);
					} else {
						R36cell2.setCellValue("");
						R36cell2.setCellStyle(textStyle);
					}
					// R36 Col D
					Cell R36cell3 = row.createCell(5);
					if (record.getR36_NO_OF_ACS() != null) {
						R36cell3.setCellValue(record.getR36_NO_OF_ACS().doubleValue());
						R36cell3.setCellStyle(numberStyle);
					} else {
						R36cell3.setCellValue("");
						R36cell3.setCellStyle(textStyle);
					}

					// R36 Col E
					Cell R36cell4 = row.createCell(6);
					if (record.getR36_INTEREST_RATE() != null) {
						R36cell4.setCellValue(record.getR36_INTEREST_RATE().doubleValue());
						R36cell4.setCellStyle(numberStyle);
					} else {
						R36cell4.setCellValue("");
						R36cell4.setCellStyle(textStyle);
					}
					// R37 Col B
					row = sheet.getRow(36);
					Cell R37cell1 = row.createCell(3);
					if (record.getR37_ORIGINAL_AMT() != null) {
						R37cell1.setCellValue(record.getR37_ORIGINAL_AMT().doubleValue());
						R37cell1.setCellStyle(numberStyle);
					} else {
						R37cell1.setCellValue("");
						R37cell1.setCellStyle(textStyle);
					}

					// R37 Col C
					Cell R37cell2 = row.createCell(4);
					if (record.getR37_BALANCE_OUTSTANDING() != null) {
						R37cell2.setCellValue(record.getR37_BALANCE_OUTSTANDING().doubleValue());
						R37cell2.setCellStyle(numberStyle);
					} else {
						R37cell2.setCellValue("");
						R37cell2.setCellStyle(textStyle);
					}
					// R37 Col D
					Cell R37cell3 = row.createCell(5);
					if (record.getR37_NO_OF_ACS() != null) {
						R37cell3.setCellValue(record.getR37_NO_OF_ACS().doubleValue());
						R37cell3.setCellStyle(numberStyle);
					} else {
						R37cell3.setCellValue("");
						R37cell3.setCellStyle(textStyle);
					}

					// R37 Col E
					Cell R37cell4 = row.createCell(6);
					if (record.getR37_INTEREST_RATE() != null) {
						R37cell4.setCellValue(record.getR37_INTEREST_RATE().doubleValue());
						R37cell4.setCellStyle(numberStyle);
					} else {
						R37cell4.setCellValue("");
						R37cell4.setCellStyle(textStyle);
					}
					row = sheet.getRow(37);
					// R37 Col E
					Cell R38cell4 = row.createCell(6);
					if (record.getR38_INTEREST_RATE() != null) {
						R38cell4.setCellValue(record.getR38_INTEREST_RATE().doubleValue());
						R38cell4.setCellStyle(numberStyle);
					} else {
						R38cell4.setCellValue("");
						R38cell4.setCellStyle(textStyle);
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
}
