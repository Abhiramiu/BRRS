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

import com.bornfire.brrs.entities.BRRS_M_SRWA_12G_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12G_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12G_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12G_Resub_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12G_Resub_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12G_Summary_Repo;
import com.bornfire.brrs.entities.M_SRWA_12G_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_SRWA_12G_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12G_Detail_Entity;
import com.bornfire.brrs.entities.M_SRWA_12G_Resub_Detail_Entity;
import com.bornfire.brrs.entities.M_SRWA_12G_Resub_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12G_Summary_Entity;

@Component
@Service

public class BRRS_M_SRWA_12G_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SRWA_12G_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_SRWA_12G_Summary_Repo brrs_M_SRWA_12G_summary_repo;

	@Autowired
	BRRS_M_SRWA_12G_Detail_Repo brrs_M_SRWA_12G_detail_repo;

	@Autowired
	BRRS_M_SRWA_12G_Archival_Summary_Repo M_SRWA_12G_Archival_Summary_Repo;

	@Autowired
	BRRS_M_SRWA_12G_Archival_Detail_Repo BRRS_M_SRWA_12G_Archival_Detail_Repo;

	@Autowired
	BRRS_M_SRWA_12G_Resub_Summary_Repo M_SRWA_12G_Resub_Summary_Repo;

	@Autowired
	BRRS_M_SRWA_12G_Resub_Detail_Repo M_SRWA_12G_Resub_Detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRRS_M_SRWA_12GView(String reportId, String fromdate, String todate, String currency,
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
				List<M_SRWA_12G_Archival_Summary_Entity> T1Master = M_SRWA_12G_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);
				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_SRWA_12G_Resub_Summary_Entity> T1Master = M_SRWA_12G_Resub_Summary_Repo
						.getdatabydateListarchival(d1, version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<M_SRWA_12G_Summary_Entity> T1Master = brrs_M_SRWA_12G_summary_repo
						.getdatabydateList(dateformat.parse(todate));
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_SRWA_12G_Archival_Detail_Entity> T1Master = BRRS_M_SRWA_12G_Archival_Detail_Repo
							.getdatabydateListarchival(d1, version);
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_SRWA_12G_Resub_Detail_Entity> T1Master = M_SRWA_12G_Resub_Detail_Repo
							.getdatabydateListarchival(d1, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_SRWA_12G_Detail_Entity> T1Master = brrs_M_SRWA_12G_detail_repo
							.getdatabydateList(dateformat.parse(todate));
					System.out.println("Details......T1Master Size " + T1Master.size());
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_SRWA_12G");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	public void updateReport(M_SRWA_12G_Summary_Entity updatedEntity) {

		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		// 1️⃣ Fetch existing SUMMARY
		M_SRWA_12G_Summary_Entity existingSummary = brrs_M_SRWA_12G_summary_repo
				.findById(updatedEntity.getReport_date()).orElseThrow(() -> new RuntimeException(
						"Summary record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

		// 2️⃣ Fetch or create DETAIL
		M_SRWA_12G_Detail_Entity existingDetail = brrs_M_SRWA_12G_detail_repo.findById(updatedEntity.getReport_date())
				.orElseGet(() -> {
					M_SRWA_12G_Detail_Entity d = new M_SRWA_12G_Detail_Entity();
					d.setReport_date(updatedEntity.getReport_date());
					return d;
				});

		try {

			// 🔁 Loop R11 → R60 (normal fields)
			for (int i = 11; i <= 60; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "credit_rating", "security_firm", "exposure_amount", "risk_weight",
						"rating_agency" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_SRWA_12G_Summary_Entity.class.getMethod(getterName);

						Method summarySetter = M_SRWA_12G_Summary_Entity.class.getMethod(setterName,
								getter.getReturnType());

						Method detailSetter = M_SRWA_12G_Detail_Entity.class.getMethod(setterName,
								getter.getReturnType());

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

			// 🔁 Loop R11 → R60 (formula column)
			for (int i = 11; i <= 60; i++) {
				String prefix = "R" + i + "_";
				String field = "risk_weighted_amount";

				String getterName = "get" + prefix + field;
				String setterName = "set" + prefix + field;

				try {
					Method getter = M_SRWA_12G_Summary_Entity.class.getMethod(getterName);

					Method summarySetter = M_SRWA_12G_Summary_Entity.class.getMethod(setterName,
							getter.getReturnType());

					Method detailSetter = M_SRWA_12G_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

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

			// 🔁 Handle R61 totals
			String[] totalFields = { "exposure_amount", "risk_weighted_amount" };

			for (String field : totalFields) {
				String getterName = "getR61_" + field;
				String setterName = "setR61_" + field;

				try {
					Method getter = M_SRWA_12G_Summary_Entity.class.getMethod(getterName);

					Method summarySetter = M_SRWA_12G_Summary_Entity.class.getMethod(setterName,
							getter.getReturnType());

					Method detailSetter = M_SRWA_12G_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

					Object newValue = getter.invoke(updatedEntity);

					// ✅ set into SUMMARY
					summarySetter.invoke(existingSummary, newValue);

					// ✅ set into DETAIL
					detailSetter.invoke(existingDetail, newValue);

				} catch (NoSuchMethodException e) {
					// skip if not present
					continue;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 3️⃣ Save BOTH (same transaction)
		brrs_M_SRWA_12G_summary_repo.save(existingSummary);
		brrs_M_SRWA_12G_detail_repo.save(existingDetail);
	}

	public void updateResubReport(M_SRWA_12G_Resub_Summary_Entity updatedEntity) {

		Date reportDate = updatedEntity.getReportDate();

		// ----------------------------------------------------
		// 1️⃣ GET CURRENT VERSION FROM RESUB TABLE
		// ----------------------------------------------------

		BigDecimal maxResubVer = M_SRWA_12G_Resub_Summary_Repo.findMaxVersion(reportDate);

		if (maxResubVer == null)
			throw new RuntimeException("No record for: " + reportDate);

		BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

		Date now = new Date();

		// ====================================================
		// 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
		// ====================================================

		M_SRWA_12G_Resub_Summary_Entity resubSummary = new M_SRWA_12G_Resub_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "reportResubDate");

		resubSummary.setReportDate(reportDate);
		resubSummary.setReportVersion(newVersion);
		resubSummary.setReportResubDate(now);

		// ====================================================
		// 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
		// ====================================================

		M_SRWA_12G_Resub_Detail_Entity resubDetail = new M_SRWA_12G_Resub_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "reportResubDate");

		resubDetail.setReportDate(reportDate);
		resubDetail.setReportVersion(newVersion);
		resubDetail.setREPORT_RESUBDATE(now);

		// ====================================================
		// 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
		// ====================================================

		M_SRWA_12G_Archival_Summary_Entity archSummary = new M_SRWA_12G_Archival_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "reportResubDate");

		archSummary.setReportDate(reportDate);
		archSummary.setReportVersion(newVersion); // SAME VERSION
		archSummary.setReportResubDate(now);

		// ====================================================
		// 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
		// ====================================================

		M_SRWA_12G_Archival_Detail_Entity archDetail = new M_SRWA_12G_Archival_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, archDetail, "reportDate", "reportVersion", "reportResubDate");

		archDetail.setReportDate(reportDate);
		archDetail.setReportVersion(newVersion); // SAME VERSION
		archDetail.setReportResubDate(now);

		// ====================================================
		// 6️⃣ SAVE ALL WITH SAME DATA
		// ====================================================

		M_SRWA_12G_Resub_Summary_Repo.save(resubSummary);
		M_SRWA_12G_Resub_Detail_Repo.save(resubDetail);

		M_SRWA_12G_Archival_Summary_Repo.save(archSummary);
		BRRS_M_SRWA_12G_Archival_Detail_Repo.save(archDetail);
	}

	public List<Object[]> getM_SRWA_12GResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_SRWA_12G_Archival_Summary_Entity> latestArchivalList = M_SRWA_12G_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_SRWA_12G_Archival_Summary_Entity entity : latestArchivalList) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					resubList.add(row);
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}
		} catch (Exception e) {
			System.err.println("Error fetching M_SRWA_12G Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	public List<Object[]> getM_SRWA_12GArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_SRWA_12G_Archival_Summary_Entity> repoData = M_SRWA_12G_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_SRWA_12G_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),entity.getReportResubDate()};
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_SRWA_12G_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_SRWA_12G Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Normal format Excel

	public byte[] getBRRS_M_SRWA_12GExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {
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
				return getExcelM_SRWA_12GARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SRWA_12GResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_SRWA_12GEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} else {

				// Fetch data

				List<M_SRWA_12G_Summary_Entity> dataList = brrs_M_SRWA_12G_summary_repo
						.getdatabydateList(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_SRWA_12G report. Returning empty result.");
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

					int startRow = 10;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_SRWA_12G_Summary_Entity record1 = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

							Cell cellB, cellC, cellD, cellE, cellF;
							CellStyle originalStyle;

							// ===== Row 11 / Col B =====
							row = sheet.getRow(10);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value
							if (record1.getR11_security_firm() != null)
								cellB.setCellValue(record1.getR11_security_firm()); // String directly
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R11 / Col C =====

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR11_credit_rating() != null)
								cellC.setCellValue(record1.getR11_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R11 / Col D =====

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							// ✅ Handle String value
							if (record1.getR11_rating_agency() != null)
								cellD.setCellValue(record1.getR11_rating_agency()); // String directly
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R11 / Col E =====

							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR11_exposure_amount() != null)
								cellE.setCellValue(record1.getR11_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R11 / Col F =====

							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR11_risk_weight() != null)
								cellF.setCellValue(record1.getR11_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 12 / Col B =====
							row = sheet.getRow(11);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR12_security_firm() != null)
								cellB.setCellValue(record1.getR12_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R12 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR12_credit_rating() != null)
								cellC.setCellValue(record1.getR12_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R12 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR12_rating_agency() != null)
								cellD.setCellValue(record1.getR12_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R12 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR12_exposure_amount() != null)
								cellE.setCellValue(record1.getR12_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R12 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR12_risk_weight() != null)
								cellF.setCellValue(record1.getR12_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 13 / Col B =====
							row = sheet.getRow(12);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR13_security_firm() != null)
								cellB.setCellValue(record1.getR13_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R13 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR13_credit_rating() != null)
								cellC.setCellValue(record1.getR13_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R13 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR13_rating_agency() != null)
								cellD.setCellValue(record1.getR13_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R13 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR13_exposure_amount() != null)
								cellE.setCellValue(record1.getR13_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R13 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR13_risk_weight() != null)
								cellF.setCellValue(record1.getR13_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 14 / Col B =====
							row = sheet.getRow(13);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR14_security_firm() != null)
								cellB.setCellValue(record1.getR14_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R14 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR14_credit_rating() != null)
								cellC.setCellValue(record1.getR14_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R14 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR14_rating_agency() != null)
								cellD.setCellValue(record1.getR14_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R14 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR14_exposure_amount() != null)
								cellE.setCellValue(record1.getR14_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R14 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR14_risk_weight() != null)
								cellF.setCellValue(record1.getR14_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 15 / Col B =====
							row = sheet.getRow(14);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR15_security_firm() != null)
								cellB.setCellValue(record1.getR15_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R15 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR15_credit_rating() != null)
								cellC.setCellValue(record1.getR15_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R15 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR15_rating_agency() != null)
								cellD.setCellValue(record1.getR15_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R15 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR15_exposure_amount() != null)
								cellE.setCellValue(record1.getR15_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R15 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR15_risk_weight() != null)
								cellF.setCellValue(record1.getR15_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 16 / Col B =====
							row = sheet.getRow(15);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR16_security_firm() != null)
								cellB.setCellValue(record1.getR16_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R16 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR16_credit_rating() != null)
								cellC.setCellValue(record1.getR16_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R16 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR16_rating_agency() != null)
								cellD.setCellValue(record1.getR16_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R16 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR16_exposure_amount() != null)
								cellE.setCellValue(record1.getR16_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R16 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR16_risk_weight() != null)
								cellF.setCellValue(record1.getR16_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 17 / Col B =====
							row = sheet.getRow(16);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR17_security_firm() != null)
								cellB.setCellValue(record1.getR17_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R17 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR17_credit_rating() != null)
								cellC.setCellValue(record1.getR17_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R17 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR17_rating_agency() != null)
								cellD.setCellValue(record1.getR17_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R17 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR17_exposure_amount() != null)
								cellE.setCellValue(record1.getR17_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R17 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR17_risk_weight() != null)
								cellF.setCellValue(record1.getR17_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 18 / Col B =====
							row = sheet.getRow(17);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR18_security_firm() != null)
								cellB.setCellValue(record1.getR18_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R18 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR18_credit_rating() != null)
								cellC.setCellValue(record1.getR18_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R18 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR18_rating_agency() != null)
								cellD.setCellValue(record1.getR18_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R18 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR18_exposure_amount() != null)
								cellE.setCellValue(record1.getR18_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R18 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR18_risk_weight() != null)
								cellF.setCellValue(record1.getR18_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 19 / Col B =====
							row = sheet.getRow(18);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR19_security_firm() != null)
								cellB.setCellValue(record1.getR19_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R19 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR19_credit_rating() != null)
								cellC.setCellValue(record1.getR19_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R19 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR19_rating_agency() != null)
								cellD.setCellValue(record1.getR19_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R19 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR19_exposure_amount() != null)
								cellE.setCellValue(record1.getR19_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R19 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR19_risk_weight() != null)
								cellF.setCellValue(record1.getR19_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 20 / Col B =====
							row = sheet.getRow(19);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR20_security_firm() != null)
								cellB.setCellValue(record1.getR20_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R20 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR20_credit_rating() != null)
								cellC.setCellValue(record1.getR20_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R20 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR20_rating_agency() != null)
								cellD.setCellValue(record1.getR20_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R20 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR20_exposure_amount() != null)
								cellE.setCellValue(record1.getR20_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R20 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR20_risk_weight() != null)
								cellF.setCellValue(record1.getR20_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 21 / Col B =====
							row = sheet.getRow(20);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR21_security_firm() != null)
								cellB.setCellValue(record1.getR21_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R21 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR21_credit_rating() != null)
								cellC.setCellValue(record1.getR21_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R21 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR21_rating_agency() != null)
								cellD.setCellValue(record1.getR21_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R21 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR21_exposure_amount() != null)
								cellE.setCellValue(record1.getR21_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R21 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR21_risk_weight() != null)
								cellF.setCellValue(record1.getR21_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 22 / Col B =====
							row = sheet.getRow(21);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR22_security_firm() != null)
								cellB.setCellValue(record1.getR22_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R22 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR22_credit_rating() != null)
								cellC.setCellValue(record1.getR22_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R22 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR22_rating_agency() != null)
								cellD.setCellValue(record1.getR22_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R22 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR22_exposure_amount() != null)
								cellE.setCellValue(record1.getR22_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R22 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR22_risk_weight() != null)
								cellF.setCellValue(record1.getR22_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 23 / Col B =====
							row = sheet.getRow(22);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR23_security_firm() != null)
								cellB.setCellValue(record1.getR23_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R23 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR23_credit_rating() != null)
								cellC.setCellValue(record1.getR23_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R23 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR23_rating_agency() != null)
								cellD.setCellValue(record1.getR23_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R23 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR23_exposure_amount() != null)
								cellE.setCellValue(record1.getR23_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R23 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR23_risk_weight() != null)
								cellF.setCellValue(record1.getR23_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 24 / Col B =====
							row = sheet.getRow(23);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR24_security_firm() != null)
								cellB.setCellValue(record1.getR24_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R24 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR24_credit_rating() != null)
								cellC.setCellValue(record1.getR24_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R24 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR24_rating_agency() != null)
								cellD.setCellValue(record1.getR24_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R24 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR24_exposure_amount() != null)
								cellE.setCellValue(record1.getR24_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R24 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR24_risk_weight() != null)
								cellF.setCellValue(record1.getR24_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 25 / Col B =====
							row = sheet.getRow(24);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR25_security_firm() != null)
								cellB.setCellValue(record1.getR25_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R25 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR25_credit_rating() != null)
								cellC.setCellValue(record1.getR25_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R25 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR25_rating_agency() != null)
								cellD.setCellValue(record1.getR25_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R25 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR25_exposure_amount() != null)
								cellE.setCellValue(record1.getR25_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R25 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR25_risk_weight() != null)
								cellF.setCellValue(record1.getR25_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 26 / Col B =====
							row = sheet.getRow(25);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR26_security_firm() != null)
								cellB.setCellValue(record1.getR26_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R26 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR26_credit_rating() != null)
								cellC.setCellValue(record1.getR26_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R26 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR26_rating_agency() != null)
								cellD.setCellValue(record1.getR26_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R26 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR26_exposure_amount() != null)
								cellE.setCellValue(record1.getR26_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R26 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR26_risk_weight() != null)
								cellF.setCellValue(record1.getR26_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 27 / Col B =====
							row = sheet.getRow(26);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR27_security_firm() != null)
								cellB.setCellValue(record1.getR27_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R27 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR27_credit_rating() != null)
								cellC.setCellValue(record1.getR27_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R27 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR27_rating_agency() != null)
								cellD.setCellValue(record1.getR27_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R27 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR27_exposure_amount() != null)
								cellE.setCellValue(record1.getR27_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R27 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR27_risk_weight() != null)
								cellF.setCellValue(record1.getR27_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 28 / Col B =====
							row = sheet.getRow(27);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR28_security_firm() != null)
								cellB.setCellValue(record1.getR28_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R28 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR28_credit_rating() != null)
								cellC.setCellValue(record1.getR28_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R28 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR28_rating_agency() != null)
								cellD.setCellValue(record1.getR28_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R28 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR28_exposure_amount() != null)
								cellE.setCellValue(record1.getR28_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R28 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR28_risk_weight() != null)
								cellF.setCellValue(record1.getR28_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 29 / Col B =====
							row = sheet.getRow(28);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR29_security_firm() != null)
								cellB.setCellValue(record1.getR29_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R29 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR29_credit_rating() != null)
								cellC.setCellValue(record1.getR29_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R29 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR29_rating_agency() != null)
								cellD.setCellValue(record1.getR29_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R29 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR29_exposure_amount() != null)
								cellE.setCellValue(record1.getR29_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R29 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR29_risk_weight() != null)
								cellF.setCellValue(record1.getR29_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 30 / Col B =====
							row = sheet.getRow(29);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR30_security_firm() != null)
								cellB.setCellValue(record1.getR30_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R30 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR30_credit_rating() != null)
								cellC.setCellValue(record1.getR30_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R30 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR30_rating_agency() != null)
								cellD.setCellValue(record1.getR30_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R30 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR30_exposure_amount() != null)
								cellE.setCellValue(record1.getR30_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R30 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR30_risk_weight() != null)
								cellF.setCellValue(record1.getR30_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 31 / Col B =====
							row = sheet.getRow(30);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR31_security_firm() != null)
								cellB.setCellValue(record1.getR31_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R31 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR31_credit_rating() != null)
								cellC.setCellValue(record1.getR31_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R31 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR31_rating_agency() != null)
								cellD.setCellValue(record1.getR31_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R31 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR31_exposure_amount() != null)
								cellE.setCellValue(record1.getR31_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R31 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR31_risk_weight() != null)
								cellF.setCellValue(record1.getR31_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 32 / Col B =====
							row = sheet.getRow(31);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR32_security_firm() != null)
								cellB.setCellValue(record1.getR32_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R32 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR32_credit_rating() != null)
								cellC.setCellValue(record1.getR32_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R32 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR32_rating_agency() != null)
								cellD.setCellValue(record1.getR32_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R32 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR32_exposure_amount() != null)
								cellE.setCellValue(record1.getR32_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R32 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR32_risk_weight() != null)
								cellF.setCellValue(record1.getR32_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 33 / Col B =====
							row = sheet.getRow(32);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR33_security_firm() != null)
								cellB.setCellValue(record1.getR33_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R33 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR33_credit_rating() != null)
								cellC.setCellValue(record1.getR33_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R33 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR33_rating_agency() != null)
								cellD.setCellValue(record1.getR33_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R33 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR33_exposure_amount() != null)
								cellE.setCellValue(record1.getR33_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R33 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR33_risk_weight() != null)
								cellF.setCellValue(record1.getR33_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 34 / Col B =====
							row = sheet.getRow(33);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR34_security_firm() != null)
								cellB.setCellValue(record1.getR34_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R34 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR34_credit_rating() != null)
								cellC.setCellValue(record1.getR34_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R34 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR34_rating_agency() != null)
								cellD.setCellValue(record1.getR34_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R34 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR34_exposure_amount() != null)
								cellE.setCellValue(record1.getR34_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R34 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR34_risk_weight() != null)
								cellF.setCellValue(record1.getR34_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 35 / Col B =====
							row = sheet.getRow(34);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR35_security_firm() != null)
								cellB.setCellValue(record1.getR35_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R35 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR35_credit_rating() != null)
								cellC.setCellValue(record1.getR35_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R35 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR35_rating_agency() != null)
								cellD.setCellValue(record1.getR35_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R35 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR35_exposure_amount() != null)
								cellE.setCellValue(record1.getR35_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R35 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR35_risk_weight() != null)
								cellF.setCellValue(record1.getR35_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 36 / Col B =====
							row = sheet.getRow(35);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR36_security_firm() != null)
								cellB.setCellValue(record1.getR36_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R36 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR36_credit_rating() != null)
								cellC.setCellValue(record1.getR36_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R36 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR36_rating_agency() != null)
								cellD.setCellValue(record1.getR36_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R36 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR36_exposure_amount() != null)
								cellE.setCellValue(record1.getR36_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R36 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR36_risk_weight() != null)
								cellF.setCellValue(record1.getR36_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 37 / Col B =====
							row = sheet.getRow(36);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR37_security_firm() != null)
								cellB.setCellValue(record1.getR37_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R37 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR37_credit_rating() != null)
								cellC.setCellValue(record1.getR37_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R37 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR37_rating_agency() != null)
								cellD.setCellValue(record1.getR37_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R37 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR37_exposure_amount() != null)
								cellE.setCellValue(record1.getR37_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R37 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR37_risk_weight() != null)
								cellF.setCellValue(record1.getR37_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 38 / Col B =====
							row = sheet.getRow(37);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR38_security_firm() != null)
								cellB.setCellValue(record1.getR38_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R38 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR38_credit_rating() != null)
								cellC.setCellValue(record1.getR38_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R38 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR38_rating_agency() != null)
								cellD.setCellValue(record1.getR38_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R38 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR38_exposure_amount() != null)
								cellE.setCellValue(record1.getR38_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R38 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR38_risk_weight() != null)
								cellF.setCellValue(record1.getR38_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 39 / Col B =====
							row = sheet.getRow(38);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR39_security_firm() != null)
								cellB.setCellValue(record1.getR39_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R39 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR39_credit_rating() != null)
								cellC.setCellValue(record1.getR39_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R39 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR39_rating_agency() != null)
								cellD.setCellValue(record1.getR39_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R39 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR39_exposure_amount() != null)
								cellE.setCellValue(record1.getR39_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R39 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR39_risk_weight() != null)
								cellF.setCellValue(record1.getR39_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 40 / Col B =====
							row = sheet.getRow(39);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR40_security_firm() != null)
								cellB.setCellValue(record1.getR40_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R40 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR40_credit_rating() != null)
								cellC.setCellValue(record1.getR40_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R40 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR40_rating_agency() != null)
								cellD.setCellValue(record1.getR40_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R40 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR40_exposure_amount() != null)
								cellE.setCellValue(record1.getR40_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R40 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR40_risk_weight() != null)
								cellF.setCellValue(record1.getR40_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 41 / Col B =====
							row = sheet.getRow(40);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR41_security_firm() != null)
								cellB.setCellValue(record1.getR41_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R41 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR41_credit_rating() != null)
								cellC.setCellValue(record1.getR41_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R41 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR41_rating_agency() != null)
								cellD.setCellValue(record1.getR41_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R41 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR41_exposure_amount() != null)
								cellE.setCellValue(record1.getR41_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R41 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR41_risk_weight() != null)
								cellF.setCellValue(record1.getR41_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 42 / Col B =====
							row = sheet.getRow(41);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR42_security_firm() != null)
								cellB.setCellValue(record1.getR42_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R42 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR42_credit_rating() != null)
								cellC.setCellValue(record1.getR42_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R42 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR42_rating_agency() != null)
								cellD.setCellValue(record1.getR42_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R42 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR42_exposure_amount() != null)
								cellE.setCellValue(record1.getR42_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R42 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR42_risk_weight() != null)
								cellF.setCellValue(record1.getR42_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 43 / Col B =====
							row = sheet.getRow(42);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR43_security_firm() != null)
								cellB.setCellValue(record1.getR43_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R43 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR43_credit_rating() != null)
								cellC.setCellValue(record1.getR43_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R43 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR43_rating_agency() != null)
								cellD.setCellValue(record1.getR43_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R43 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR43_exposure_amount() != null)
								cellE.setCellValue(record1.getR43_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R43 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR43_risk_weight() != null)
								cellF.setCellValue(record1.getR43_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 44 / Col B =====
							row = sheet.getRow(43);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR44_security_firm() != null)
								cellB.setCellValue(record1.getR44_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R44 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR44_credit_rating() != null)
								cellC.setCellValue(record1.getR44_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R44 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR44_rating_agency() != null)
								cellD.setCellValue(record1.getR44_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R44 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR44_exposure_amount() != null)
								cellE.setCellValue(record1.getR44_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R44 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR44_risk_weight() != null)
								cellF.setCellValue(record1.getR44_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 45 / Col B =====
							row = sheet.getRow(44);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR45_security_firm() != null)
								cellB.setCellValue(record1.getR45_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R45 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR45_credit_rating() != null)
								cellC.setCellValue(record1.getR45_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R45 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR45_rating_agency() != null)
								cellD.setCellValue(record1.getR45_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R45 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR45_exposure_amount() != null)
								cellE.setCellValue(record1.getR45_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R45 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR45_risk_weight() != null)
								cellF.setCellValue(record1.getR45_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 46 / Col B =====
							row = sheet.getRow(45);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR46_security_firm() != null)
								cellB.setCellValue(record1.getR46_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R46 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR46_credit_rating() != null)
								cellC.setCellValue(record1.getR46_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R46 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR46_rating_agency() != null)
								cellD.setCellValue(record1.getR46_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R46 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR46_exposure_amount() != null)
								cellE.setCellValue(record1.getR46_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R46 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR46_risk_weight() != null)
								cellF.setCellValue(record1.getR46_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 47 / Col B =====
							row = sheet.getRow(46);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR47_security_firm() != null)
								cellB.setCellValue(record1.getR47_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R47 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR47_credit_rating() != null)
								cellC.setCellValue(record1.getR47_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R47 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR47_rating_agency() != null)
								cellD.setCellValue(record1.getR47_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R47 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR47_exposure_amount() != null)
								cellE.setCellValue(record1.getR47_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R47 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR47_risk_weight() != null)
								cellF.setCellValue(record1.getR47_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 48 / Col B =====
							row = sheet.getRow(47);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR48_security_firm() != null)
								cellB.setCellValue(record1.getR48_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R48 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR48_credit_rating() != null)
								cellC.setCellValue(record1.getR48_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R48 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR48_rating_agency() != null)
								cellD.setCellValue(record1.getR48_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R48 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR48_exposure_amount() != null)
								cellE.setCellValue(record1.getR48_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R48 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR48_risk_weight() != null)
								cellF.setCellValue(record1.getR48_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 49 / Col B =====
							row = sheet.getRow(48);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR49_security_firm() != null)
								cellB.setCellValue(record1.getR49_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R49 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR49_credit_rating() != null)
								cellC.setCellValue(record1.getR49_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R49 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR49_rating_agency() != null)
								cellD.setCellValue(record1.getR49_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R49 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR49_exposure_amount() != null)
								cellE.setCellValue(record1.getR49_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R49 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR49_risk_weight() != null)
								cellF.setCellValue(record1.getR49_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 50 / Col B =====
							row = sheet.getRow(49);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR50_security_firm() != null)
								cellB.setCellValue(record1.getR50_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R50 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR50_credit_rating() != null)
								cellC.setCellValue(record1.getR50_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R50 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR50_rating_agency() != null)
								cellD.setCellValue(record1.getR50_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R50 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR50_exposure_amount() != null)
								cellE.setCellValue(record1.getR50_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R50 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR50_risk_weight() != null)
								cellF.setCellValue(record1.getR50_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 51 / Col B =====
							row = sheet.getRow(50);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR51_security_firm() != null)
								cellB.setCellValue(record1.getR51_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R51 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR51_credit_rating() != null)
								cellC.setCellValue(record1.getR51_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R51 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR51_rating_agency() != null)
								cellD.setCellValue(record1.getR51_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R51 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR51_exposure_amount() != null)
								cellE.setCellValue(record1.getR51_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R51 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR51_risk_weight() != null)
								cellF.setCellValue(record1.getR51_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 52 / Col B =====
							row = sheet.getRow(51);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR52_security_firm() != null)
								cellB.setCellValue(record1.getR52_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R52 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR52_credit_rating() != null)
								cellC.setCellValue(record1.getR52_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R52 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR52_rating_agency() != null)
								cellD.setCellValue(record1.getR52_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R52 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR52_exposure_amount() != null)
								cellE.setCellValue(record1.getR52_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R52 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR52_risk_weight() != null)
								cellF.setCellValue(record1.getR52_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 53 / Col B =====
							row = sheet.getRow(52);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR53_security_firm() != null)
								cellB.setCellValue(record1.getR53_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R53 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR53_credit_rating() != null)
								cellC.setCellValue(record1.getR53_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R53 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR53_rating_agency() != null)
								cellD.setCellValue(record1.getR53_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R53 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR53_exposure_amount() != null)
								cellE.setCellValue(record1.getR53_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R53 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR53_risk_weight() != null)
								cellF.setCellValue(record1.getR53_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 54 / Col B =====
							row = sheet.getRow(53);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR54_security_firm() != null)
								cellB.setCellValue(record1.getR54_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R54 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR54_credit_rating() != null)
								cellC.setCellValue(record1.getR54_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R54 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR54_rating_agency() != null)
								cellD.setCellValue(record1.getR54_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R54 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR54_exposure_amount() != null)
								cellE.setCellValue(record1.getR54_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R54 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR54_risk_weight() != null)
								cellF.setCellValue(record1.getR54_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 55 / Col B =====
							row = sheet.getRow(54);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR55_security_firm() != null)
								cellB.setCellValue(record1.getR55_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R55 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR55_credit_rating() != null)
								cellC.setCellValue(record1.getR55_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R55 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR55_rating_agency() != null)
								cellD.setCellValue(record1.getR55_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R55 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR55_exposure_amount() != null)
								cellE.setCellValue(record1.getR55_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R55 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR55_risk_weight() != null)
								cellF.setCellValue(record1.getR55_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 56 / Col B =====
							row = sheet.getRow(55);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR56_security_firm() != null)
								cellB.setCellValue(record1.getR56_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R56 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR56_credit_rating() != null)
								cellC.setCellValue(record1.getR56_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R56 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR56_rating_agency() != null)
								cellD.setCellValue(record1.getR56_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R56 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR56_exposure_amount() != null)
								cellE.setCellValue(record1.getR56_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R56 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR56_risk_weight() != null)
								cellF.setCellValue(record1.getR56_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 57 / Col B =====
							row = sheet.getRow(56);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR57_security_firm() != null)
								cellB.setCellValue(record1.getR57_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R57 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR57_credit_rating() != null)
								cellC.setCellValue(record1.getR57_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R57 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR57_rating_agency() != null)
								cellD.setCellValue(record1.getR57_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R57 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR57_exposure_amount() != null)
								cellE.setCellValue(record1.getR57_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R57 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR57_risk_weight() != null)
								cellF.setCellValue(record1.getR57_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 58 / Col B =====
							row = sheet.getRow(57);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR58_security_firm() != null)
								cellB.setCellValue(record1.getR58_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R58 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR58_credit_rating() != null)
								cellC.setCellValue(record1.getR58_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R58 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR58_rating_agency() != null)
								cellD.setCellValue(record1.getR58_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R58 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR58_exposure_amount() != null)
								cellE.setCellValue(record1.getR58_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R58 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR58_risk_weight() != null)
								cellF.setCellValue(record1.getR58_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 59 / Col B =====
							row = sheet.getRow(58);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR59_security_firm() != null)
								cellB.setCellValue(record1.getR59_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R59 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR59_credit_rating() != null)
								cellC.setCellValue(record1.getR59_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R59 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR59_rating_agency() != null)
								cellD.setCellValue(record1.getR59_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R59 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR59_exposure_amount() != null)
								cellE.setCellValue(record1.getR59_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R59 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR59_risk_weight() != null)
								cellF.setCellValue(record1.getR59_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

							// ===== Row 60 / Col B =====
							row = sheet.getRow(59);
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR60_security_firm() != null)
								cellB.setCellValue(record1.getR60_security_firm());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R60 / Col C =====
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR60_credit_rating() != null)
								cellC.setCellValue(record1.getR60_credit_rating().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R60 / Col D =====
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR60_rating_agency() != null)
								cellD.setCellValue(record1.getR60_rating_agency());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);

							// ===== R60 / Col E =====
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							originalStyle = cellE.getCellStyle();
							if (record1.getR60_exposure_amount() != null)
								cellE.setCellValue(record1.getR60_exposure_amount().doubleValue());
							else
								cellE.setCellValue("");
							cellE.setCellStyle(originalStyle);

							// ===== R60 / Col F =====
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							originalStyle = cellF.getCellStyle();
							if (record1.getR60_risk_weight() != null)
								cellF.setCellValue(record1.getR60_risk_weight().doubleValue());
							else
								cellF.setCellValue("");
							cellF.setCellStyle(originalStyle);

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
	public byte[] BRRS_M_SRWA_12GEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_SRWA_12GArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SRWA_12GResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			List<M_SRWA_12G_Summary_Entity> dataList = brrs_M_SRWA_12G_summary_repo
					.getdatabydateList(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_SRWA_12G report. Returning empty result.");
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

				int startRow = 10;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_SRWA_12G_Summary_Entity record1 = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						Cell cellB, cellC, cellD, cellE, cellF;
						CellStyle originalStyle;

						// ===== Row 11 / Col B =====
						row = sheet.getRow(10);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR11_security_firm() != null)
							cellB.setCellValue(record1.getR11_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R11 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR11_credit_rating() != null)
							cellC.setCellValue(record1.getR11_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R11 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR11_rating_agency() != null)
							cellD.setCellValue(record1.getR11_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R11 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR11_exposure_amount() != null)
							cellE.setCellValue(record1.getR11_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R11 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR11_risk_weight() != null)
							cellF.setCellValue(record1.getR11_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R11 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR11_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR11_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 12 / Col B =====
						row = sheet.getRow(11);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR12_security_firm() != null)
							cellB.setCellValue(record1.getR12_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R12 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR12_credit_rating() != null)
							cellC.setCellValue(record1.getR12_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R12 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR12_rating_agency() != null)
							cellD.setCellValue(record1.getR12_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R12 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR12_exposure_amount() != null)
							cellE.setCellValue(record1.getR12_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R12 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR12_risk_weight() != null)
							cellF.setCellValue(record1.getR12_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R12 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR12_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR12_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 13 / Col B =====
						row = sheet.getRow(12);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR13_security_firm() != null)
							cellB.setCellValue(record1.getR13_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R13 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR13_credit_rating() != null)
							cellC.setCellValue(record1.getR13_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R13 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR13_rating_agency() != null)
							cellD.setCellValue(record1.getR13_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R13 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR13_exposure_amount() != null)
							cellE.setCellValue(record1.getR13_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R13 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR13_risk_weight() != null)
							cellF.setCellValue(record1.getR13_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R13 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR13_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR13_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 14 / Col B =====
						row = sheet.getRow(13);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR14_security_firm() != null)
							cellB.setCellValue(record1.getR14_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R14 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR14_credit_rating() != null)
							cellC.setCellValue(record1.getR14_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R14 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR14_rating_agency() != null)
							cellD.setCellValue(record1.getR14_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R14 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR14_exposure_amount() != null)
							cellE.setCellValue(record1.getR14_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R14 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR14_risk_weight() != null)
							cellF.setCellValue(record1.getR14_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R14 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR14_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR14_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 15 / Col B =====
						row = sheet.getRow(14);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR15_security_firm() != null)
							cellB.setCellValue(record1.getR15_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R15 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR15_credit_rating() != null)
							cellC.setCellValue(record1.getR15_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R15 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR15_rating_agency() != null)
							cellD.setCellValue(record1.getR15_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R15 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR15_exposure_amount() != null)
							cellE.setCellValue(record1.getR15_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R15 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR15_risk_weight() != null)
							cellF.setCellValue(record1.getR15_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R15 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR15_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR15_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);
						// ===== Row 16 / Col B =====
						row = sheet.getRow(15);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR16_security_firm() != null)
							cellB.setCellValue(record1.getR16_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R16 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR16_credit_rating() != null)
							cellC.setCellValue(record1.getR16_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R16 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR16_rating_agency() != null)
							cellD.setCellValue(record1.getR16_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R16 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR16_exposure_amount() != null)
							cellE.setCellValue(record1.getR16_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R16 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR16_risk_weight() != null)
							cellF.setCellValue(record1.getR16_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R16 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR16_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR16_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 17 / Col B =====
						row = sheet.getRow(16);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR17_security_firm() != null)
							cellB.setCellValue(record1.getR17_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R17 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR17_credit_rating() != null)
							cellC.setCellValue(record1.getR17_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R17 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR17_rating_agency() != null)
							cellD.setCellValue(record1.getR17_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R17 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR17_exposure_amount() != null)
							cellE.setCellValue(record1.getR17_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R17 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR17_risk_weight() != null)
							cellF.setCellValue(record1.getR17_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R17 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR17_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR17_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 18 / Col B =====
						row = sheet.getRow(17);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR18_security_firm() != null)
							cellB.setCellValue(record1.getR18_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R18 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR18_credit_rating() != null)
							cellC.setCellValue(record1.getR18_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R18 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR18_rating_agency() != null)
							cellD.setCellValue(record1.getR18_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R18 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR18_exposure_amount() != null)
							cellE.setCellValue(record1.getR18_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R18 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR18_risk_weight() != null)
							cellF.setCellValue(record1.getR18_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R18 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR18_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR18_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 19 / Col B =====
						row = sheet.getRow(18);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR19_security_firm() != null)
							cellB.setCellValue(record1.getR19_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R19 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR19_credit_rating() != null)
							cellC.setCellValue(record1.getR19_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R19 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR19_rating_agency() != null)
							cellD.setCellValue(record1.getR19_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R19 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR19_exposure_amount() != null)
							cellE.setCellValue(record1.getR19_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R19 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR19_risk_weight() != null)
							cellF.setCellValue(record1.getR19_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R19 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR19_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR19_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 20 / Col B =====
						row = sheet.getRow(19);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR20_security_firm() != null)
							cellB.setCellValue(record1.getR20_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R20 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR20_credit_rating() != null)
							cellC.setCellValue(record1.getR20_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R20 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR20_rating_agency() != null)
							cellD.setCellValue(record1.getR20_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R20 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR20_exposure_amount() != null)
							cellE.setCellValue(record1.getR20_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R20 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR20_risk_weight() != null)
							cellF.setCellValue(record1.getR20_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R20 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR20_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR20_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 21 / Col B =====
						row = sheet.getRow(20);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR21_security_firm() != null)
							cellB.setCellValue(record1.getR21_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R21 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR21_credit_rating() != null)
							cellC.setCellValue(record1.getR21_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R21 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR21_rating_agency() != null)
							cellD.setCellValue(record1.getR21_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R21 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR21_exposure_amount() != null)
							cellE.setCellValue(record1.getR21_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R21 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR21_risk_weight() != null)
							cellF.setCellValue(record1.getR21_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R21 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR21_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR21_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 22 / Col B =====
						row = sheet.getRow(21);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR22_security_firm() != null)
							cellB.setCellValue(record1.getR22_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R22 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR22_credit_rating() != null)
							cellC.setCellValue(record1.getR22_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R22 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR22_rating_agency() != null)
							cellD.setCellValue(record1.getR22_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R22 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR22_exposure_amount() != null)
							cellE.setCellValue(record1.getR22_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R22 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR22_risk_weight() != null)
							cellF.setCellValue(record1.getR22_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R22 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR22_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR22_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 23 / Col B =====
						row = sheet.getRow(22);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR23_security_firm() != null)
							cellB.setCellValue(record1.getR23_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R23 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR23_credit_rating() != null)
							cellC.setCellValue(record1.getR23_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R23 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR23_rating_agency() != null)
							cellD.setCellValue(record1.getR23_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R23 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR23_exposure_amount() != null)
							cellE.setCellValue(record1.getR23_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R23 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR23_risk_weight() != null)
							cellF.setCellValue(record1.getR23_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R23 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR23_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR23_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 24 / Col B =====
						row = sheet.getRow(23);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR24_security_firm() != null)
							cellB.setCellValue(record1.getR24_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R24 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR24_credit_rating() != null)
							cellC.setCellValue(record1.getR24_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R24 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR24_rating_agency() != null)
							cellD.setCellValue(record1.getR24_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R24 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR24_exposure_amount() != null)
							cellE.setCellValue(record1.getR24_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R24 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR24_risk_weight() != null)
							cellF.setCellValue(record1.getR24_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R24 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR24_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR24_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 25 / Col B =====
						row = sheet.getRow(24);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR25_security_firm() != null)
							cellB.setCellValue(record1.getR25_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R25 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR25_credit_rating() != null)
							cellC.setCellValue(record1.getR25_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R25 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR25_rating_agency() != null)
							cellD.setCellValue(record1.getR25_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R25 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR25_exposure_amount() != null)
							cellE.setCellValue(record1.getR25_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R25 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR25_risk_weight() != null)
							cellF.setCellValue(record1.getR25_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R25 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR25_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR25_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 26 / Col B =====
						row = sheet.getRow(25);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR26_security_firm() != null)
							cellB.setCellValue(record1.getR26_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R26 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR26_credit_rating() != null)
							cellC.setCellValue(record1.getR26_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R26 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR26_rating_agency() != null)
							cellD.setCellValue(record1.getR26_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R26 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR26_exposure_amount() != null)
							cellE.setCellValue(record1.getR26_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R26 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR26_risk_weight() != null)
							cellF.setCellValue(record1.getR26_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R26 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR26_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR26_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 27 / Col B =====
						row = sheet.getRow(26);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR27_security_firm() != null)
							cellB.setCellValue(record1.getR27_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R27 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR27_credit_rating() != null)
							cellC.setCellValue(record1.getR27_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R27 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR27_rating_agency() != null)
							cellD.setCellValue(record1.getR27_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R27 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR27_exposure_amount() != null)
							cellE.setCellValue(record1.getR27_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R27 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR27_risk_weight() != null)
							cellF.setCellValue(record1.getR27_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R27 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR27_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR27_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 28 / Col B =====
						row = sheet.getRow(27);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR28_security_firm() != null)
							cellB.setCellValue(record1.getR28_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R28 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR28_credit_rating() != null)
							cellC.setCellValue(record1.getR28_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R28 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR28_rating_agency() != null)
							cellD.setCellValue(record1.getR28_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R28 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR28_exposure_amount() != null)
							cellE.setCellValue(record1.getR28_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R28 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR28_risk_weight() != null)
							cellF.setCellValue(record1.getR28_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R28 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR28_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR28_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 29 / Col B =====
						row = sheet.getRow(28);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR29_security_firm() != null)
							cellB.setCellValue(record1.getR29_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R29 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR29_credit_rating() != null)
							cellC.setCellValue(record1.getR29_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R29 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR29_rating_agency() != null)
							cellD.setCellValue(record1.getR29_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R29 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR29_exposure_amount() != null)
							cellE.setCellValue(record1.getR29_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R29 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR29_risk_weight() != null)
							cellF.setCellValue(record1.getR29_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R29 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR29_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR29_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 30 / Col B =====
						row = sheet.getRow(29);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR30_security_firm() != null)
							cellB.setCellValue(record1.getR30_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R30 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR30_credit_rating() != null)
							cellC.setCellValue(record1.getR30_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R30 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR30_rating_agency() != null)
							cellD.setCellValue(record1.getR30_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R30 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR30_exposure_amount() != null)
							cellE.setCellValue(record1.getR30_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R30 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR30_risk_weight() != null)
							cellF.setCellValue(record1.getR30_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R30 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR30_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR30_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 31 / Col B =====
						row = sheet.getRow(30);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR31_security_firm() != null)
							cellB.setCellValue(record1.getR31_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R31 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR31_credit_rating() != null)
							cellC.setCellValue(record1.getR31_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R31 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR31_rating_agency() != null)
							cellD.setCellValue(record1.getR31_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R31 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR31_exposure_amount() != null)
							cellE.setCellValue(record1.getR31_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R31 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR31_risk_weight() != null)
							cellF.setCellValue(record1.getR31_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R31 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR31_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR31_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 32 / Col B =====
						row = sheet.getRow(31);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR32_security_firm() != null)
							cellB.setCellValue(record1.getR32_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R32 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR32_credit_rating() != null)
							cellC.setCellValue(record1.getR32_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R32 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR32_rating_agency() != null)
							cellD.setCellValue(record1.getR32_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R32 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR32_exposure_amount() != null)
							cellE.setCellValue(record1.getR32_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R32 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR32_risk_weight() != null)
							cellF.setCellValue(record1.getR32_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R32 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR32_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR32_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 33 / Col B =====
						row = sheet.getRow(32);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR33_security_firm() != null)
							cellB.setCellValue(record1.getR33_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R33 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR33_credit_rating() != null)
							cellC.setCellValue(record1.getR33_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R33 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR33_rating_agency() != null)
							cellD.setCellValue(record1.getR33_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R33 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR33_exposure_amount() != null)
							cellE.setCellValue(record1.getR33_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R33 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR33_risk_weight() != null)
							cellF.setCellValue(record1.getR33_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R33 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR33_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR33_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 34 / Col B =====
						row = sheet.getRow(33);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR34_security_firm() != null)
							cellB.setCellValue(record1.getR34_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R34 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR34_credit_rating() != null)
							cellC.setCellValue(record1.getR34_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R34 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR34_rating_agency() != null)
							cellD.setCellValue(record1.getR34_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R34 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR34_exposure_amount() != null)
							cellE.setCellValue(record1.getR34_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R34 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR34_risk_weight() != null)
							cellF.setCellValue(record1.getR34_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R34 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR34_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR34_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 35 / Col B =====
						row = sheet.getRow(34);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR35_security_firm() != null)
							cellB.setCellValue(record1.getR35_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R35 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR35_credit_rating() != null)
							cellC.setCellValue(record1.getR35_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R35 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR35_rating_agency() != null)
							cellD.setCellValue(record1.getR35_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R35 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR35_exposure_amount() != null)
							cellE.setCellValue(record1.getR35_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R35 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR35_risk_weight() != null)
							cellF.setCellValue(record1.getR35_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R35 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR35_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR35_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 36 / Col B =====
						row = sheet.getRow(35);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR36_security_firm() != null)
							cellB.setCellValue(record1.getR36_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R36 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR36_credit_rating() != null)
							cellC.setCellValue(record1.getR36_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R36 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR36_rating_agency() != null)
							cellD.setCellValue(record1.getR36_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R36 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR36_exposure_amount() != null)
							cellE.setCellValue(record1.getR36_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R36 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR36_risk_weight() != null)
							cellF.setCellValue(record1.getR36_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R36 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR36_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR36_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== Row 37 / Col B =====
						row = sheet.getRow(36);
						cellB = row.getCell(0);
						if (cellB == null)
							cellB = row.createCell(0);
						originalStyle = cellB.getCellStyle();
						// ✅ Handle String value
						if (record1.getR37_security_firm() != null)
							cellB.setCellValue(record1.getR37_security_firm()); // String directly
						else
							cellB.setCellValue("");
						cellB.setCellStyle(originalStyle);

						// ===== R37 / Col C =====

						cellC = row.getCell(1);
						if (cellC == null)
							cellC = row.createCell(1);
						originalStyle = cellC.getCellStyle();
						if (record1.getR37_credit_rating() != null)
							cellC.setCellValue(record1.getR37_credit_rating().doubleValue());
						else
							cellC.setCellValue("");
						cellC.setCellStyle(originalStyle);

						// ===== R37 / Col D =====

						cellD = row.getCell(3);
						if (cellD == null)
							cellD = row.createCell(3);
						originalStyle = cellD.getCellStyle();
						// ✅ Handle String value
						if (record1.getR37_rating_agency() != null)
							cellD.setCellValue(record1.getR37_rating_agency()); // String directly
						else
							cellD.setCellValue("");
						cellD.setCellStyle(originalStyle);

						// ===== R37 / Col E =====

						cellE = row.getCell(5);
						if (cellE == null)
							cellE = row.createCell(5);
						originalStyle = cellE.getCellStyle();
						if (record1.getR37_exposure_amount() != null)
							cellE.setCellValue(record1.getR37_exposure_amount().doubleValue());
						else
							cellE.setCellValue("");
						cellE.setCellStyle(originalStyle);

						// ===== R37 / Col F =====

						cellF = row.getCell(6);
						if (cellF == null)
							cellF = row.createCell(6);
						originalStyle = cellF.getCellStyle();
						if (record1.getR37_risk_weight() != null)
							cellF.setCellValue(record1.getR37_risk_weight().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);

						// ===== R37 / Col G =====
						cellF = row.getCell(7);
						if (cellF == null)
							cellF = row.createCell(7);
						originalStyle = cellF.getCellStyle();
						if (record1.getR37_risk_weighted_amount() != null)
							cellF.setCellValue(record1.getR37_risk_weighted_amount().doubleValue());
						else
							cellF.setCellValue("");
						cellF.setCellStyle(originalStyle);
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
	public byte[] getExcelM_SRWA_12GARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_SRWA_12GArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_SRWA_12G_Archival_Summary_Entity> dataList = M_SRWA_12G_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SRWA_12G report. Returning empty result.");
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
					M_SRWA_12G_Archival_Summary_Entity record1 = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cellB, cellC, cellD, cellE, cellF;
					CellStyle originalStyle;

					// ===== Row 11 / Col B =====
					row = sheet.getRow(10);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_security_firm() != null)
						cellB.setCellValue(record1.getR11_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R11 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR11_credit_rating() != null)
						cellC.setCellValue(record1.getR11_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R11 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_rating_agency() != null)
						cellD.setCellValue(record1.getR11_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R11 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR11_exposure_amount() != null)
						cellE.setCellValue(record1.getR11_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R11 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR11_risk_weight() != null)
						cellF.setCellValue(record1.getR11_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 12 / Col B =====
					row = sheet.getRow(11);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR12_security_firm() != null)
						cellB.setCellValue(record1.getR12_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R12 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR12_credit_rating() != null)
						cellC.setCellValue(record1.getR12_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R12 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR12_rating_agency() != null)
						cellD.setCellValue(record1.getR12_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R12 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR12_exposure_amount() != null)
						cellE.setCellValue(record1.getR12_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R12 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR12_risk_weight() != null)
						cellF.setCellValue(record1.getR12_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 13 / Col B =====
					row = sheet.getRow(12);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR13_security_firm() != null)
						cellB.setCellValue(record1.getR13_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R13 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR13_credit_rating() != null)
						cellC.setCellValue(record1.getR13_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R13 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR13_rating_agency() != null)
						cellD.setCellValue(record1.getR13_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R13 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR13_exposure_amount() != null)
						cellE.setCellValue(record1.getR13_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R13 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR13_risk_weight() != null)
						cellF.setCellValue(record1.getR13_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 14 / Col B =====
					row = sheet.getRow(13);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR14_security_firm() != null)
						cellB.setCellValue(record1.getR14_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R14 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR14_credit_rating() != null)
						cellC.setCellValue(record1.getR14_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R14 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR14_rating_agency() != null)
						cellD.setCellValue(record1.getR14_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R14 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR14_exposure_amount() != null)
						cellE.setCellValue(record1.getR14_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R14 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR14_risk_weight() != null)
						cellF.setCellValue(record1.getR14_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 15 / Col B =====
					row = sheet.getRow(14);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR15_security_firm() != null)
						cellB.setCellValue(record1.getR15_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R15 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR15_credit_rating() != null)
						cellC.setCellValue(record1.getR15_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R15 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR15_rating_agency() != null)
						cellD.setCellValue(record1.getR15_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R15 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR15_exposure_amount() != null)
						cellE.setCellValue(record1.getR15_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R15 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR15_risk_weight() != null)
						cellF.setCellValue(record1.getR15_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 16 / Col B =====
					row = sheet.getRow(15);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR16_security_firm() != null)
						cellB.setCellValue(record1.getR16_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R16 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR16_credit_rating() != null)
						cellC.setCellValue(record1.getR16_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R16 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR16_rating_agency() != null)
						cellD.setCellValue(record1.getR16_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R16 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR16_exposure_amount() != null)
						cellE.setCellValue(record1.getR16_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R16 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR16_risk_weight() != null)
						cellF.setCellValue(record1.getR16_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 17 / Col B =====
					row = sheet.getRow(16);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR17_security_firm() != null)
						cellB.setCellValue(record1.getR17_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R17 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR17_credit_rating() != null)
						cellC.setCellValue(record1.getR17_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R17 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR17_rating_agency() != null)
						cellD.setCellValue(record1.getR17_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R17 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR17_exposure_amount() != null)
						cellE.setCellValue(record1.getR17_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R17 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR17_risk_weight() != null)
						cellF.setCellValue(record1.getR17_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 18 / Col B =====
					row = sheet.getRow(17);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR18_security_firm() != null)
						cellB.setCellValue(record1.getR18_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R18 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR18_credit_rating() != null)
						cellC.setCellValue(record1.getR18_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R18 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR18_rating_agency() != null)
						cellD.setCellValue(record1.getR18_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R18 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR18_exposure_amount() != null)
						cellE.setCellValue(record1.getR18_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R18 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR18_risk_weight() != null)
						cellF.setCellValue(record1.getR18_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 19 / Col B =====
					row = sheet.getRow(18);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR19_security_firm() != null)
						cellB.setCellValue(record1.getR19_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R19 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR19_credit_rating() != null)
						cellC.setCellValue(record1.getR19_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R19 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR19_rating_agency() != null)
						cellD.setCellValue(record1.getR19_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R19 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR19_exposure_amount() != null)
						cellE.setCellValue(record1.getR19_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R19 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR19_risk_weight() != null)
						cellF.setCellValue(record1.getR19_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 20 / Col B =====
					row = sheet.getRow(19);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR20_security_firm() != null)
						cellB.setCellValue(record1.getR20_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R20 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR20_credit_rating() != null)
						cellC.setCellValue(record1.getR20_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R20 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR20_rating_agency() != null)
						cellD.setCellValue(record1.getR20_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R20 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR20_exposure_amount() != null)
						cellE.setCellValue(record1.getR20_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R20 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR20_risk_weight() != null)
						cellF.setCellValue(record1.getR20_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 21 / Col B =====
					row = sheet.getRow(20);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR21_security_firm() != null)
						cellB.setCellValue(record1.getR21_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R21 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR21_credit_rating() != null)
						cellC.setCellValue(record1.getR21_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R21 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR21_rating_agency() != null)
						cellD.setCellValue(record1.getR21_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R21 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR21_exposure_amount() != null)
						cellE.setCellValue(record1.getR21_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R21 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR21_risk_weight() != null)
						cellF.setCellValue(record1.getR21_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 22 / Col B =====
					row = sheet.getRow(21);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR22_security_firm() != null)
						cellB.setCellValue(record1.getR22_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R22 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR22_credit_rating() != null)
						cellC.setCellValue(record1.getR22_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R22 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR22_rating_agency() != null)
						cellD.setCellValue(record1.getR22_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R22 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR22_exposure_amount() != null)
						cellE.setCellValue(record1.getR22_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R22 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR22_risk_weight() != null)
						cellF.setCellValue(record1.getR22_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 23 / Col B =====
					row = sheet.getRow(22);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR23_security_firm() != null)
						cellB.setCellValue(record1.getR23_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R23 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR23_credit_rating() != null)
						cellC.setCellValue(record1.getR23_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R23 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR23_rating_agency() != null)
						cellD.setCellValue(record1.getR23_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R23 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR23_exposure_amount() != null)
						cellE.setCellValue(record1.getR23_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R23 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR23_risk_weight() != null)
						cellF.setCellValue(record1.getR23_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 24 / Col B =====
					row = sheet.getRow(23);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR24_security_firm() != null)
						cellB.setCellValue(record1.getR24_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R24 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR24_credit_rating() != null)
						cellC.setCellValue(record1.getR24_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R24 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR24_rating_agency() != null)
						cellD.setCellValue(record1.getR24_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R24 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR24_exposure_amount() != null)
						cellE.setCellValue(record1.getR24_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R24 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR24_risk_weight() != null)
						cellF.setCellValue(record1.getR24_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 25 / Col B =====
					row = sheet.getRow(24);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR25_security_firm() != null)
						cellB.setCellValue(record1.getR25_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R25 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR25_credit_rating() != null)
						cellC.setCellValue(record1.getR25_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R25 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR25_rating_agency() != null)
						cellD.setCellValue(record1.getR25_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R25 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR25_exposure_amount() != null)
						cellE.setCellValue(record1.getR25_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R25 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR25_risk_weight() != null)
						cellF.setCellValue(record1.getR25_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 26 / Col B =====
					row = sheet.getRow(25);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR26_security_firm() != null)
						cellB.setCellValue(record1.getR26_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R26 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR26_credit_rating() != null)
						cellC.setCellValue(record1.getR26_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R26 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR26_rating_agency() != null)
						cellD.setCellValue(record1.getR26_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R26 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR26_exposure_amount() != null)
						cellE.setCellValue(record1.getR26_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R26 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR26_risk_weight() != null)
						cellF.setCellValue(record1.getR26_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 27 / Col B =====
					row = sheet.getRow(26);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR27_security_firm() != null)
						cellB.setCellValue(record1.getR27_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R27 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR27_credit_rating() != null)
						cellC.setCellValue(record1.getR27_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R27 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR27_rating_agency() != null)
						cellD.setCellValue(record1.getR27_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R27 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR27_exposure_amount() != null)
						cellE.setCellValue(record1.getR27_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R27 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR27_risk_weight() != null)
						cellF.setCellValue(record1.getR27_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 28 / Col B =====
					row = sheet.getRow(27);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR28_security_firm() != null)
						cellB.setCellValue(record1.getR28_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R28 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR28_credit_rating() != null)
						cellC.setCellValue(record1.getR28_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R28 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR28_rating_agency() != null)
						cellD.setCellValue(record1.getR28_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R28 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR28_exposure_amount() != null)
						cellE.setCellValue(record1.getR28_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R28 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR28_risk_weight() != null)
						cellF.setCellValue(record1.getR28_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 29 / Col B =====
					row = sheet.getRow(28);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR29_security_firm() != null)
						cellB.setCellValue(record1.getR29_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R29 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR29_credit_rating() != null)
						cellC.setCellValue(record1.getR29_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R29 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR29_rating_agency() != null)
						cellD.setCellValue(record1.getR29_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R29 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR29_exposure_amount() != null)
						cellE.setCellValue(record1.getR29_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R29 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR29_risk_weight() != null)
						cellF.setCellValue(record1.getR29_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 30 / Col B =====
					row = sheet.getRow(29);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR30_security_firm() != null)
						cellB.setCellValue(record1.getR30_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R30 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR30_credit_rating() != null)
						cellC.setCellValue(record1.getR30_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R30 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR30_rating_agency() != null)
						cellD.setCellValue(record1.getR30_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R30 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR30_exposure_amount() != null)
						cellE.setCellValue(record1.getR30_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R30 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR30_risk_weight() != null)
						cellF.setCellValue(record1.getR30_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 31 / Col B =====
					row = sheet.getRow(30);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR31_security_firm() != null)
						cellB.setCellValue(record1.getR31_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R31 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR31_credit_rating() != null)
						cellC.setCellValue(record1.getR31_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R31 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR31_rating_agency() != null)
						cellD.setCellValue(record1.getR31_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R31 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR31_exposure_amount() != null)
						cellE.setCellValue(record1.getR31_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R31 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR31_risk_weight() != null)
						cellF.setCellValue(record1.getR31_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 32 / Col B =====
					row = sheet.getRow(31);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR32_security_firm() != null)
						cellB.setCellValue(record1.getR32_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R32 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR32_credit_rating() != null)
						cellC.setCellValue(record1.getR32_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R32 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR32_rating_agency() != null)
						cellD.setCellValue(record1.getR32_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R32 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR32_exposure_amount() != null)
						cellE.setCellValue(record1.getR32_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R32 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR32_risk_weight() != null)
						cellF.setCellValue(record1.getR32_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 33 / Col B =====
					row = sheet.getRow(32);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR33_security_firm() != null)
						cellB.setCellValue(record1.getR33_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R33 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR33_credit_rating() != null)
						cellC.setCellValue(record1.getR33_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R33 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR33_rating_agency() != null)
						cellD.setCellValue(record1.getR33_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R33 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR33_exposure_amount() != null)
						cellE.setCellValue(record1.getR33_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R33 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR33_risk_weight() != null)
						cellF.setCellValue(record1.getR33_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 34 / Col B =====
					row = sheet.getRow(33);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR34_security_firm() != null)
						cellB.setCellValue(record1.getR34_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R34 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR34_credit_rating() != null)
						cellC.setCellValue(record1.getR34_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R34 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR34_rating_agency() != null)
						cellD.setCellValue(record1.getR34_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R34 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR34_exposure_amount() != null)
						cellE.setCellValue(record1.getR34_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R34 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR34_risk_weight() != null)
						cellF.setCellValue(record1.getR34_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 35 / Col B =====
					row = sheet.getRow(34);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR35_security_firm() != null)
						cellB.setCellValue(record1.getR35_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R35 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR35_credit_rating() != null)
						cellC.setCellValue(record1.getR35_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R35 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR35_rating_agency() != null)
						cellD.setCellValue(record1.getR35_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R35 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR35_exposure_amount() != null)
						cellE.setCellValue(record1.getR35_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R35 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR35_risk_weight() != null)
						cellF.setCellValue(record1.getR35_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 36 / Col B =====
					row = sheet.getRow(35);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR36_security_firm() != null)
						cellB.setCellValue(record1.getR36_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R36 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR36_credit_rating() != null)
						cellC.setCellValue(record1.getR36_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R36 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR36_rating_agency() != null)
						cellD.setCellValue(record1.getR36_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R36 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR36_exposure_amount() != null)
						cellE.setCellValue(record1.getR36_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R36 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR36_risk_weight() != null)
						cellF.setCellValue(record1.getR36_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 37 / Col B =====
					row = sheet.getRow(36);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR37_security_firm() != null)
						cellB.setCellValue(record1.getR37_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R37 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR37_credit_rating() != null)
						cellC.setCellValue(record1.getR37_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R37 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR37_rating_agency() != null)
						cellD.setCellValue(record1.getR37_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R37 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR37_exposure_amount() != null)
						cellE.setCellValue(record1.getR37_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R37 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR37_risk_weight() != null)
						cellF.setCellValue(record1.getR37_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 38 / Col B =====
					row = sheet.getRow(37);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR38_security_firm() != null)
						cellB.setCellValue(record1.getR38_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R38 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR38_credit_rating() != null)
						cellC.setCellValue(record1.getR38_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R38 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR38_rating_agency() != null)
						cellD.setCellValue(record1.getR38_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R38 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR38_exposure_amount() != null)
						cellE.setCellValue(record1.getR38_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R38 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR38_risk_weight() != null)
						cellF.setCellValue(record1.getR38_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 39 / Col B =====
					row = sheet.getRow(38);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR39_security_firm() != null)
						cellB.setCellValue(record1.getR39_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R39 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR39_credit_rating() != null)
						cellC.setCellValue(record1.getR39_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R39 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR39_rating_agency() != null)
						cellD.setCellValue(record1.getR39_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R39 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR39_exposure_amount() != null)
						cellE.setCellValue(record1.getR39_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R39 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR39_risk_weight() != null)
						cellF.setCellValue(record1.getR39_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 40 / Col B =====
					row = sheet.getRow(39);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR40_security_firm() != null)
						cellB.setCellValue(record1.getR40_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R40 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR40_credit_rating() != null)
						cellC.setCellValue(record1.getR40_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R40 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR40_rating_agency() != null)
						cellD.setCellValue(record1.getR40_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R40 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR40_exposure_amount() != null)
						cellE.setCellValue(record1.getR40_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R40 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR40_risk_weight() != null)
						cellF.setCellValue(record1.getR40_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 41 / Col B =====
					row = sheet.getRow(40);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR41_security_firm() != null)
						cellB.setCellValue(record1.getR41_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R41 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR41_credit_rating() != null)
						cellC.setCellValue(record1.getR41_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R41 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR41_rating_agency() != null)
						cellD.setCellValue(record1.getR41_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R41 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR41_exposure_amount() != null)
						cellE.setCellValue(record1.getR41_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R41 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR41_risk_weight() != null)
						cellF.setCellValue(record1.getR41_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 42 / Col B =====
					row = sheet.getRow(41);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR42_security_firm() != null)
						cellB.setCellValue(record1.getR42_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R42 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR42_credit_rating() != null)
						cellC.setCellValue(record1.getR42_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R42 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR42_rating_agency() != null)
						cellD.setCellValue(record1.getR42_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R42 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR42_exposure_amount() != null)
						cellE.setCellValue(record1.getR42_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R42 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR42_risk_weight() != null)
						cellF.setCellValue(record1.getR42_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 43 / Col B =====
					row = sheet.getRow(42);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR43_security_firm() != null)
						cellB.setCellValue(record1.getR43_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R43 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR43_credit_rating() != null)
						cellC.setCellValue(record1.getR43_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R43 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR43_rating_agency() != null)
						cellD.setCellValue(record1.getR43_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R43 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR43_exposure_amount() != null)
						cellE.setCellValue(record1.getR43_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R43 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR43_risk_weight() != null)
						cellF.setCellValue(record1.getR43_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 44 / Col B =====
					row = sheet.getRow(43);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR44_security_firm() != null)
						cellB.setCellValue(record1.getR44_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R44 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR44_credit_rating() != null)
						cellC.setCellValue(record1.getR44_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R44 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR44_rating_agency() != null)
						cellD.setCellValue(record1.getR44_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R44 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR44_exposure_amount() != null)
						cellE.setCellValue(record1.getR44_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R44 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR44_risk_weight() != null)
						cellF.setCellValue(record1.getR44_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 45 / Col B =====
					row = sheet.getRow(44);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR45_security_firm() != null)
						cellB.setCellValue(record1.getR45_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R45 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR45_credit_rating() != null)
						cellC.setCellValue(record1.getR45_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R45 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR45_rating_agency() != null)
						cellD.setCellValue(record1.getR45_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R45 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR45_exposure_amount() != null)
						cellE.setCellValue(record1.getR45_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R45 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR45_risk_weight() != null)
						cellF.setCellValue(record1.getR45_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 46 / Col B =====
					row = sheet.getRow(45);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR46_security_firm() != null)
						cellB.setCellValue(record1.getR46_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R46 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR46_credit_rating() != null)
						cellC.setCellValue(record1.getR46_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R46 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR46_rating_agency() != null)
						cellD.setCellValue(record1.getR46_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R46 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR46_exposure_amount() != null)
						cellE.setCellValue(record1.getR46_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R46 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR46_risk_weight() != null)
						cellF.setCellValue(record1.getR46_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 47 / Col B =====
					row = sheet.getRow(46);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR47_security_firm() != null)
						cellB.setCellValue(record1.getR47_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R47 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR47_credit_rating() != null)
						cellC.setCellValue(record1.getR47_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R47 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR47_rating_agency() != null)
						cellD.setCellValue(record1.getR47_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R47 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR47_exposure_amount() != null)
						cellE.setCellValue(record1.getR47_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R47 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR47_risk_weight() != null)
						cellF.setCellValue(record1.getR47_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 48 / Col B =====
					row = sheet.getRow(47);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR48_security_firm() != null)
						cellB.setCellValue(record1.getR48_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R48 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR48_credit_rating() != null)
						cellC.setCellValue(record1.getR48_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R48 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR48_rating_agency() != null)
						cellD.setCellValue(record1.getR48_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R48 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR48_exposure_amount() != null)
						cellE.setCellValue(record1.getR48_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R48 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR48_risk_weight() != null)
						cellF.setCellValue(record1.getR48_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 49 / Col B =====
					row = sheet.getRow(48);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR49_security_firm() != null)
						cellB.setCellValue(record1.getR49_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R49 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR49_credit_rating() != null)
						cellC.setCellValue(record1.getR49_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R49 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR49_rating_agency() != null)
						cellD.setCellValue(record1.getR49_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R49 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR49_exposure_amount() != null)
						cellE.setCellValue(record1.getR49_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R49 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR49_risk_weight() != null)
						cellF.setCellValue(record1.getR49_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 50 / Col B =====
					row = sheet.getRow(49);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR50_security_firm() != null)
						cellB.setCellValue(record1.getR50_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R50 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR50_credit_rating() != null)
						cellC.setCellValue(record1.getR50_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R50 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR50_rating_agency() != null)
						cellD.setCellValue(record1.getR50_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R50 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR50_exposure_amount() != null)
						cellE.setCellValue(record1.getR50_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R50 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR50_risk_weight() != null)
						cellF.setCellValue(record1.getR50_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 51 / Col B =====
					row = sheet.getRow(50);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR51_security_firm() != null)
						cellB.setCellValue(record1.getR51_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R51 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR51_credit_rating() != null)
						cellC.setCellValue(record1.getR51_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R51 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR51_rating_agency() != null)
						cellD.setCellValue(record1.getR51_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R51 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR51_exposure_amount() != null)
						cellE.setCellValue(record1.getR51_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R51 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR51_risk_weight() != null)
						cellF.setCellValue(record1.getR51_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 52 / Col B =====
					row = sheet.getRow(51);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR52_security_firm() != null)
						cellB.setCellValue(record1.getR52_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R52 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR52_credit_rating() != null)
						cellC.setCellValue(record1.getR52_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R52 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR52_rating_agency() != null)
						cellD.setCellValue(record1.getR52_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R52 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR52_exposure_amount() != null)
						cellE.setCellValue(record1.getR52_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R52 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR52_risk_weight() != null)
						cellF.setCellValue(record1.getR52_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 53 / Col B =====
					row = sheet.getRow(52);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR53_security_firm() != null)
						cellB.setCellValue(record1.getR53_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R53 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR53_credit_rating() != null)
						cellC.setCellValue(record1.getR53_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R53 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR53_rating_agency() != null)
						cellD.setCellValue(record1.getR53_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R53 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR53_exposure_amount() != null)
						cellE.setCellValue(record1.getR53_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R53 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR53_risk_weight() != null)
						cellF.setCellValue(record1.getR53_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 54 / Col B =====
					row = sheet.getRow(53);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR54_security_firm() != null)
						cellB.setCellValue(record1.getR54_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R54 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR54_credit_rating() != null)
						cellC.setCellValue(record1.getR54_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R54 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR54_rating_agency() != null)
						cellD.setCellValue(record1.getR54_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R54 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR54_exposure_amount() != null)
						cellE.setCellValue(record1.getR54_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R54 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR54_risk_weight() != null)
						cellF.setCellValue(record1.getR54_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 55 / Col B =====
					row = sheet.getRow(54);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR55_security_firm() != null)
						cellB.setCellValue(record1.getR55_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R55 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR55_credit_rating() != null)
						cellC.setCellValue(record1.getR55_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R55 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR55_rating_agency() != null)
						cellD.setCellValue(record1.getR55_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R55 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR55_exposure_amount() != null)
						cellE.setCellValue(record1.getR55_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R55 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR55_risk_weight() != null)
						cellF.setCellValue(record1.getR55_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 56 / Col B =====
					row = sheet.getRow(55);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR56_security_firm() != null)
						cellB.setCellValue(record1.getR56_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R56 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR56_credit_rating() != null)
						cellC.setCellValue(record1.getR56_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R56 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR56_rating_agency() != null)
						cellD.setCellValue(record1.getR56_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R56 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR56_exposure_amount() != null)
						cellE.setCellValue(record1.getR56_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R56 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR56_risk_weight() != null)
						cellF.setCellValue(record1.getR56_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 57 / Col B =====
					row = sheet.getRow(56);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR57_security_firm() != null)
						cellB.setCellValue(record1.getR57_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R57 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR57_credit_rating() != null)
						cellC.setCellValue(record1.getR57_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R57 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR57_rating_agency() != null)
						cellD.setCellValue(record1.getR57_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R57 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR57_exposure_amount() != null)
						cellE.setCellValue(record1.getR57_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R57 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR57_risk_weight() != null)
						cellF.setCellValue(record1.getR57_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 58 / Col B =====
					row = sheet.getRow(57);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR58_security_firm() != null)
						cellB.setCellValue(record1.getR58_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R58 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR58_credit_rating() != null)
						cellC.setCellValue(record1.getR58_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R58 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR58_rating_agency() != null)
						cellD.setCellValue(record1.getR58_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R58 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR58_exposure_amount() != null)
						cellE.setCellValue(record1.getR58_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R58 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR58_risk_weight() != null)
						cellF.setCellValue(record1.getR58_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 59 / Col B =====
					row = sheet.getRow(58);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR59_security_firm() != null)
						cellB.setCellValue(record1.getR59_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R59 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR59_credit_rating() != null)
						cellC.setCellValue(record1.getR59_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R59 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR59_rating_agency() != null)
						cellD.setCellValue(record1.getR59_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R59 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR59_exposure_amount() != null)
						cellE.setCellValue(record1.getR59_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R59 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR59_risk_weight() != null)
						cellF.setCellValue(record1.getR59_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 60 / Col B =====
					row = sheet.getRow(59);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR60_security_firm() != null)
						cellB.setCellValue(record1.getR60_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R60 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR60_credit_rating() != null)
						cellC.setCellValue(record1.getR60_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R60 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR60_rating_agency() != null)
						cellD.setCellValue(record1.getR60_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R60 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR60_exposure_amount() != null)
						cellE.setCellValue(record1.getR60_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R60 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR60_risk_weight() != null)
						cellF.setCellValue(record1.getR60_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

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
	public byte[] BRRS_M_SRWA_12GArchivalEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_SRWA_12G_Archival_Summary_Entity> dataList = M_SRWA_12G_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_SRWA_12G report. Returning empty result.");
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
					M_SRWA_12G_Archival_Summary_Entity record1 = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cellB, cellC, cellD, cellE, cellF;
					CellStyle originalStyle;

					// ===== Row 11 / Col B =====
					row = sheet.getRow(10);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_security_firm() != null)
						cellB.setCellValue(record1.getR11_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R11 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR11_credit_rating() != null)
						cellC.setCellValue(record1.getR11_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R11 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_rating_agency() != null)
						cellD.setCellValue(record1.getR11_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R11 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR11_exposure_amount() != null)
						cellE.setCellValue(record1.getR11_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R11 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR11_risk_weight() != null)
						cellF.setCellValue(record1.getR11_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R11 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR11_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR11_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 12 / Col B =====
					row = sheet.getRow(11);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_security_firm() != null)
						cellB.setCellValue(record1.getR12_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R12 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR12_credit_rating() != null)
						cellC.setCellValue(record1.getR12_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R12 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_rating_agency() != null)
						cellD.setCellValue(record1.getR12_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R12 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR12_exposure_amount() != null)
						cellE.setCellValue(record1.getR12_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R12 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR12_risk_weight() != null)
						cellF.setCellValue(record1.getR12_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R12 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR12_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR12_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 13 / Col B =====
					row = sheet.getRow(12);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR13_security_firm() != null)
						cellB.setCellValue(record1.getR13_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R13 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR13_credit_rating() != null)
						cellC.setCellValue(record1.getR13_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R13 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR13_rating_agency() != null)
						cellD.setCellValue(record1.getR13_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R13 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR13_exposure_amount() != null)
						cellE.setCellValue(record1.getR13_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R13 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR13_risk_weight() != null)
						cellF.setCellValue(record1.getR13_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R13 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR13_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR13_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 14 / Col B =====
					row = sheet.getRow(13);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR14_security_firm() != null)
						cellB.setCellValue(record1.getR14_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R14 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR14_credit_rating() != null)
						cellC.setCellValue(record1.getR14_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R14 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR14_rating_agency() != null)
						cellD.setCellValue(record1.getR14_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R14 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR14_exposure_amount() != null)
						cellE.setCellValue(record1.getR14_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R14 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR14_risk_weight() != null)
						cellF.setCellValue(record1.getR14_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R14 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR14_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR14_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 15 / Col B =====
					row = sheet.getRow(14);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR15_security_firm() != null)
						cellB.setCellValue(record1.getR15_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R15 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR15_credit_rating() != null)
						cellC.setCellValue(record1.getR15_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R15 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR15_rating_agency() != null)
						cellD.setCellValue(record1.getR15_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R15 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR15_exposure_amount() != null)
						cellE.setCellValue(record1.getR15_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R15 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR15_risk_weight() != null)
						cellF.setCellValue(record1.getR15_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R15 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR15_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR15_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);
					// ===== Row 16 / Col B =====
					row = sheet.getRow(15);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR16_security_firm() != null)
						cellB.setCellValue(record1.getR16_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R16 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR16_credit_rating() != null)
						cellC.setCellValue(record1.getR16_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R16 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR16_rating_agency() != null)
						cellD.setCellValue(record1.getR16_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R16 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR16_exposure_amount() != null)
						cellE.setCellValue(record1.getR16_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R16 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR16_risk_weight() != null)
						cellF.setCellValue(record1.getR16_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R16 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR16_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR16_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 17 / Col B =====
					row = sheet.getRow(16);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR17_security_firm() != null)
						cellB.setCellValue(record1.getR17_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R17 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR17_credit_rating() != null)
						cellC.setCellValue(record1.getR17_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R17 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR17_rating_agency() != null)
						cellD.setCellValue(record1.getR17_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R17 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR17_exposure_amount() != null)
						cellE.setCellValue(record1.getR17_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R17 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR17_risk_weight() != null)
						cellF.setCellValue(record1.getR17_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R17 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR17_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR17_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 18 / Col B =====
					row = sheet.getRow(17);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR18_security_firm() != null)
						cellB.setCellValue(record1.getR18_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R18 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR18_credit_rating() != null)
						cellC.setCellValue(record1.getR18_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R18 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR18_rating_agency() != null)
						cellD.setCellValue(record1.getR18_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R18 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR18_exposure_amount() != null)
						cellE.setCellValue(record1.getR18_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R18 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR18_risk_weight() != null)
						cellF.setCellValue(record1.getR18_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R18 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR18_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR18_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 19 / Col B =====
					row = sheet.getRow(18);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR19_security_firm() != null)
						cellB.setCellValue(record1.getR19_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R19 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR19_credit_rating() != null)
						cellC.setCellValue(record1.getR19_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R19 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR19_rating_agency() != null)
						cellD.setCellValue(record1.getR19_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R19 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR19_exposure_amount() != null)
						cellE.setCellValue(record1.getR19_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R19 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR19_risk_weight() != null)
						cellF.setCellValue(record1.getR19_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R19 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR19_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR19_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 20 / Col B =====
					row = sheet.getRow(19);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR20_security_firm() != null)
						cellB.setCellValue(record1.getR20_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R20 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR20_credit_rating() != null)
						cellC.setCellValue(record1.getR20_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R20 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR20_rating_agency() != null)
						cellD.setCellValue(record1.getR20_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R20 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR20_exposure_amount() != null)
						cellE.setCellValue(record1.getR20_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R20 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR20_risk_weight() != null)
						cellF.setCellValue(record1.getR20_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R20 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR20_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR20_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 21 / Col B =====
					row = sheet.getRow(20);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR21_security_firm() != null)
						cellB.setCellValue(record1.getR21_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R21 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR21_credit_rating() != null)
						cellC.setCellValue(record1.getR21_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R21 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR21_rating_agency() != null)
						cellD.setCellValue(record1.getR21_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R21 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR21_exposure_amount() != null)
						cellE.setCellValue(record1.getR21_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R21 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR21_risk_weight() != null)
						cellF.setCellValue(record1.getR21_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R21 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR21_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR21_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 22 / Col B =====
					row = sheet.getRow(21);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR22_security_firm() != null)
						cellB.setCellValue(record1.getR22_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R22 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR22_credit_rating() != null)
						cellC.setCellValue(record1.getR22_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R22 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR22_rating_agency() != null)
						cellD.setCellValue(record1.getR22_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R22 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR22_exposure_amount() != null)
						cellE.setCellValue(record1.getR22_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R22 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR22_risk_weight() != null)
						cellF.setCellValue(record1.getR22_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R22 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR22_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR22_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 23 / Col B =====
					row = sheet.getRow(22);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR23_security_firm() != null)
						cellB.setCellValue(record1.getR23_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R23 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR23_credit_rating() != null)
						cellC.setCellValue(record1.getR23_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R23 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR23_rating_agency() != null)
						cellD.setCellValue(record1.getR23_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R23 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR23_exposure_amount() != null)
						cellE.setCellValue(record1.getR23_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R23 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR23_risk_weight() != null)
						cellF.setCellValue(record1.getR23_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R23 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR23_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR23_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 24 / Col B =====
					row = sheet.getRow(23);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR24_security_firm() != null)
						cellB.setCellValue(record1.getR24_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R24 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR24_credit_rating() != null)
						cellC.setCellValue(record1.getR24_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R24 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR24_rating_agency() != null)
						cellD.setCellValue(record1.getR24_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R24 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR24_exposure_amount() != null)
						cellE.setCellValue(record1.getR24_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R24 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR24_risk_weight() != null)
						cellF.setCellValue(record1.getR24_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R24 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR24_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR24_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 25 / Col B =====
					row = sheet.getRow(24);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR25_security_firm() != null)
						cellB.setCellValue(record1.getR25_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R25 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR25_credit_rating() != null)
						cellC.setCellValue(record1.getR25_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R25 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR25_rating_agency() != null)
						cellD.setCellValue(record1.getR25_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R25 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR25_exposure_amount() != null)
						cellE.setCellValue(record1.getR25_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R25 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR25_risk_weight() != null)
						cellF.setCellValue(record1.getR25_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R25 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR25_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR25_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 26 / Col B =====
					row = sheet.getRow(25);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR26_security_firm() != null)
						cellB.setCellValue(record1.getR26_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R26 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR26_credit_rating() != null)
						cellC.setCellValue(record1.getR26_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R26 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR26_rating_agency() != null)
						cellD.setCellValue(record1.getR26_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R26 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR26_exposure_amount() != null)
						cellE.setCellValue(record1.getR26_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R26 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR26_risk_weight() != null)
						cellF.setCellValue(record1.getR26_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R26 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR26_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR26_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 27 / Col B =====
					row = sheet.getRow(26);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR27_security_firm() != null)
						cellB.setCellValue(record1.getR27_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R27 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR27_credit_rating() != null)
						cellC.setCellValue(record1.getR27_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R27 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR27_rating_agency() != null)
						cellD.setCellValue(record1.getR27_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R27 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR27_exposure_amount() != null)
						cellE.setCellValue(record1.getR27_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R27 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR27_risk_weight() != null)
						cellF.setCellValue(record1.getR27_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R27 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR27_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR27_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 28 / Col B =====
					row = sheet.getRow(27);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR28_security_firm() != null)
						cellB.setCellValue(record1.getR28_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R28 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR28_credit_rating() != null)
						cellC.setCellValue(record1.getR28_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R28 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR28_rating_agency() != null)
						cellD.setCellValue(record1.getR28_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R28 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR28_exposure_amount() != null)
						cellE.setCellValue(record1.getR28_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R28 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR28_risk_weight() != null)
						cellF.setCellValue(record1.getR28_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R28 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR28_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR28_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 29 / Col B =====
					row = sheet.getRow(28);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR29_security_firm() != null)
						cellB.setCellValue(record1.getR29_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R29 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR29_credit_rating() != null)
						cellC.setCellValue(record1.getR29_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R29 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR29_rating_agency() != null)
						cellD.setCellValue(record1.getR29_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R29 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR29_exposure_amount() != null)
						cellE.setCellValue(record1.getR29_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R29 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR29_risk_weight() != null)
						cellF.setCellValue(record1.getR29_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R29 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR29_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR29_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 30 / Col B =====
					row = sheet.getRow(29);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR30_security_firm() != null)
						cellB.setCellValue(record1.getR30_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R30 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR30_credit_rating() != null)
						cellC.setCellValue(record1.getR30_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R30 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR30_rating_agency() != null)
						cellD.setCellValue(record1.getR30_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R30 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR30_exposure_amount() != null)
						cellE.setCellValue(record1.getR30_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R30 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR30_risk_weight() != null)
						cellF.setCellValue(record1.getR30_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R30 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR30_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR30_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 31 / Col B =====
					row = sheet.getRow(30);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR31_security_firm() != null)
						cellB.setCellValue(record1.getR31_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R31 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR31_credit_rating() != null)
						cellC.setCellValue(record1.getR31_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R31 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR31_rating_agency() != null)
						cellD.setCellValue(record1.getR31_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R31 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR31_exposure_amount() != null)
						cellE.setCellValue(record1.getR31_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R31 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR31_risk_weight() != null)
						cellF.setCellValue(record1.getR31_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R31 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR31_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR31_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 32 / Col B =====
					row = sheet.getRow(31);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR32_security_firm() != null)
						cellB.setCellValue(record1.getR32_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R32 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR32_credit_rating() != null)
						cellC.setCellValue(record1.getR32_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R32 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR32_rating_agency() != null)
						cellD.setCellValue(record1.getR32_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R32 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR32_exposure_amount() != null)
						cellE.setCellValue(record1.getR32_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R32 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR32_risk_weight() != null)
						cellF.setCellValue(record1.getR32_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R32 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR32_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR32_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 33 / Col B =====
					row = sheet.getRow(32);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR33_security_firm() != null)
						cellB.setCellValue(record1.getR33_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R33 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR33_credit_rating() != null)
						cellC.setCellValue(record1.getR33_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R33 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR33_rating_agency() != null)
						cellD.setCellValue(record1.getR33_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R33 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR33_exposure_amount() != null)
						cellE.setCellValue(record1.getR33_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R33 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR33_risk_weight() != null)
						cellF.setCellValue(record1.getR33_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R33 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR33_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR33_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 34 / Col B =====
					row = sheet.getRow(33);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR34_security_firm() != null)
						cellB.setCellValue(record1.getR34_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R34 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR34_credit_rating() != null)
						cellC.setCellValue(record1.getR34_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R34 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR34_rating_agency() != null)
						cellD.setCellValue(record1.getR34_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R34 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR34_exposure_amount() != null)
						cellE.setCellValue(record1.getR34_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R34 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR34_risk_weight() != null)
						cellF.setCellValue(record1.getR34_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R34 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR34_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR34_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 35 / Col B =====
					row = sheet.getRow(34);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR35_security_firm() != null)
						cellB.setCellValue(record1.getR35_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R35 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR35_credit_rating() != null)
						cellC.setCellValue(record1.getR35_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R35 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR35_rating_agency() != null)
						cellD.setCellValue(record1.getR35_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R35 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR35_exposure_amount() != null)
						cellE.setCellValue(record1.getR35_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R35 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR35_risk_weight() != null)
						cellF.setCellValue(record1.getR35_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R35 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR35_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR35_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 36 / Col B =====
					row = sheet.getRow(35);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR36_security_firm() != null)
						cellB.setCellValue(record1.getR36_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R36 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR36_credit_rating() != null)
						cellC.setCellValue(record1.getR36_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R36 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR36_rating_agency() != null)
						cellD.setCellValue(record1.getR36_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R36 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR36_exposure_amount() != null)
						cellE.setCellValue(record1.getR36_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R36 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR36_risk_weight() != null)
						cellF.setCellValue(record1.getR36_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R36 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR36_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR36_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 37 / Col B =====
					row = sheet.getRow(36);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR37_security_firm() != null)
						cellB.setCellValue(record1.getR37_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R37 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR37_credit_rating() != null)
						cellC.setCellValue(record1.getR37_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R37 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR37_rating_agency() != null)
						cellD.setCellValue(record1.getR37_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R37 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR37_exposure_amount() != null)
						cellE.setCellValue(record1.getR37_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R37 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR37_risk_weight() != null)
						cellF.setCellValue(record1.getR37_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R37 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR37_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR37_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);
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
	public byte[] BRRS_M_SRWA_12GResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SRWA_12GResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_SRWA_12G_Resub_Summary_Entity> dataList = M_SRWA_12G_Resub_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SRWA_12G report. Returning empty result.");
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

					M_SRWA_12G_Resub_Summary_Entity record1 = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cellB, cellC, cellD, cellE, cellF;
					CellStyle originalStyle;

					// ===== Row 11 / Col B =====
					row = sheet.getRow(10);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_security_firm() != null)
						cellB.setCellValue(record1.getR11_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R11 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR11_credit_rating() != null)
						cellC.setCellValue(record1.getR11_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R11 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_rating_agency() != null)
						cellD.setCellValue(record1.getR11_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R11 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR11_exposure_amount() != null)
						cellE.setCellValue(record1.getR11_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R11 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR11_risk_weight() != null)
						cellF.setCellValue(record1.getR11_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 12 / Col B =====
					row = sheet.getRow(11);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR12_security_firm() != null)
						cellB.setCellValue(record1.getR12_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R12 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR12_credit_rating() != null)
						cellC.setCellValue(record1.getR12_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R12 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR12_rating_agency() != null)
						cellD.setCellValue(record1.getR12_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R12 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR12_exposure_amount() != null)
						cellE.setCellValue(record1.getR12_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R12 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR12_risk_weight() != null)
						cellF.setCellValue(record1.getR12_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 13 / Col B =====
					row = sheet.getRow(12);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR13_security_firm() != null)
						cellB.setCellValue(record1.getR13_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R13 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR13_credit_rating() != null)
						cellC.setCellValue(record1.getR13_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R13 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR13_rating_agency() != null)
						cellD.setCellValue(record1.getR13_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R13 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR13_exposure_amount() != null)
						cellE.setCellValue(record1.getR13_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R13 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR13_risk_weight() != null)
						cellF.setCellValue(record1.getR13_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 14 / Col B =====
					row = sheet.getRow(13);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR14_security_firm() != null)
						cellB.setCellValue(record1.getR14_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R14 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR14_credit_rating() != null)
						cellC.setCellValue(record1.getR14_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R14 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR14_rating_agency() != null)
						cellD.setCellValue(record1.getR14_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R14 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR14_exposure_amount() != null)
						cellE.setCellValue(record1.getR14_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R14 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR14_risk_weight() != null)
						cellF.setCellValue(record1.getR14_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 15 / Col B =====
					row = sheet.getRow(14);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR15_security_firm() != null)
						cellB.setCellValue(record1.getR15_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R15 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR15_credit_rating() != null)
						cellC.setCellValue(record1.getR15_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R15 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR15_rating_agency() != null)
						cellD.setCellValue(record1.getR15_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R15 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR15_exposure_amount() != null)
						cellE.setCellValue(record1.getR15_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R15 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR15_risk_weight() != null)
						cellF.setCellValue(record1.getR15_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 16 / Col B =====
					row = sheet.getRow(15);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR16_security_firm() != null)
						cellB.setCellValue(record1.getR16_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R16 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR16_credit_rating() != null)
						cellC.setCellValue(record1.getR16_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R16 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR16_rating_agency() != null)
						cellD.setCellValue(record1.getR16_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R16 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR16_exposure_amount() != null)
						cellE.setCellValue(record1.getR16_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R16 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR16_risk_weight() != null)
						cellF.setCellValue(record1.getR16_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 17 / Col B =====
					row = sheet.getRow(16);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR17_security_firm() != null)
						cellB.setCellValue(record1.getR17_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R17 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR17_credit_rating() != null)
						cellC.setCellValue(record1.getR17_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R17 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR17_rating_agency() != null)
						cellD.setCellValue(record1.getR17_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R17 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR17_exposure_amount() != null)
						cellE.setCellValue(record1.getR17_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R17 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR17_risk_weight() != null)
						cellF.setCellValue(record1.getR17_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 18 / Col B =====
					row = sheet.getRow(17);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR18_security_firm() != null)
						cellB.setCellValue(record1.getR18_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R18 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR18_credit_rating() != null)
						cellC.setCellValue(record1.getR18_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R18 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR18_rating_agency() != null)
						cellD.setCellValue(record1.getR18_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R18 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR18_exposure_amount() != null)
						cellE.setCellValue(record1.getR18_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R18 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR18_risk_weight() != null)
						cellF.setCellValue(record1.getR18_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 19 / Col B =====
					row = sheet.getRow(18);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR19_security_firm() != null)
						cellB.setCellValue(record1.getR19_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R19 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR19_credit_rating() != null)
						cellC.setCellValue(record1.getR19_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R19 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR19_rating_agency() != null)
						cellD.setCellValue(record1.getR19_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R19 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR19_exposure_amount() != null)
						cellE.setCellValue(record1.getR19_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R19 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR19_risk_weight() != null)
						cellF.setCellValue(record1.getR19_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 20 / Col B =====
					row = sheet.getRow(19);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR20_security_firm() != null)
						cellB.setCellValue(record1.getR20_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R20 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR20_credit_rating() != null)
						cellC.setCellValue(record1.getR20_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R20 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR20_rating_agency() != null)
						cellD.setCellValue(record1.getR20_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R20 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR20_exposure_amount() != null)
						cellE.setCellValue(record1.getR20_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R20 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR20_risk_weight() != null)
						cellF.setCellValue(record1.getR20_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 21 / Col B =====
					row = sheet.getRow(20);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR21_security_firm() != null)
						cellB.setCellValue(record1.getR21_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R21 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR21_credit_rating() != null)
						cellC.setCellValue(record1.getR21_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R21 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR21_rating_agency() != null)
						cellD.setCellValue(record1.getR21_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R21 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR21_exposure_amount() != null)
						cellE.setCellValue(record1.getR21_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R21 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR21_risk_weight() != null)
						cellF.setCellValue(record1.getR21_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 22 / Col B =====
					row = sheet.getRow(21);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR22_security_firm() != null)
						cellB.setCellValue(record1.getR22_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R22 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR22_credit_rating() != null)
						cellC.setCellValue(record1.getR22_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R22 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR22_rating_agency() != null)
						cellD.setCellValue(record1.getR22_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R22 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR22_exposure_amount() != null)
						cellE.setCellValue(record1.getR22_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R22 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR22_risk_weight() != null)
						cellF.setCellValue(record1.getR22_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 23 / Col B =====
					row = sheet.getRow(22);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR23_security_firm() != null)
						cellB.setCellValue(record1.getR23_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R23 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR23_credit_rating() != null)
						cellC.setCellValue(record1.getR23_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R23 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR23_rating_agency() != null)
						cellD.setCellValue(record1.getR23_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R23 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR23_exposure_amount() != null)
						cellE.setCellValue(record1.getR23_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R23 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR23_risk_weight() != null)
						cellF.setCellValue(record1.getR23_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 24 / Col B =====
					row = sheet.getRow(23);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR24_security_firm() != null)
						cellB.setCellValue(record1.getR24_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R24 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR24_credit_rating() != null)
						cellC.setCellValue(record1.getR24_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R24 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR24_rating_agency() != null)
						cellD.setCellValue(record1.getR24_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R24 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR24_exposure_amount() != null)
						cellE.setCellValue(record1.getR24_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R24 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR24_risk_weight() != null)
						cellF.setCellValue(record1.getR24_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 25 / Col B =====
					row = sheet.getRow(24);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR25_security_firm() != null)
						cellB.setCellValue(record1.getR25_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R25 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR25_credit_rating() != null)
						cellC.setCellValue(record1.getR25_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R25 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR25_rating_agency() != null)
						cellD.setCellValue(record1.getR25_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R25 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR25_exposure_amount() != null)
						cellE.setCellValue(record1.getR25_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R25 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR25_risk_weight() != null)
						cellF.setCellValue(record1.getR25_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 26 / Col B =====
					row = sheet.getRow(25);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR26_security_firm() != null)
						cellB.setCellValue(record1.getR26_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R26 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR26_credit_rating() != null)
						cellC.setCellValue(record1.getR26_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R26 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR26_rating_agency() != null)
						cellD.setCellValue(record1.getR26_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R26 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR26_exposure_amount() != null)
						cellE.setCellValue(record1.getR26_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R26 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR26_risk_weight() != null)
						cellF.setCellValue(record1.getR26_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 27 / Col B =====
					row = sheet.getRow(26);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR27_security_firm() != null)
						cellB.setCellValue(record1.getR27_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R27 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR27_credit_rating() != null)
						cellC.setCellValue(record1.getR27_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R27 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR27_rating_agency() != null)
						cellD.setCellValue(record1.getR27_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R27 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR27_exposure_amount() != null)
						cellE.setCellValue(record1.getR27_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R27 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR27_risk_weight() != null)
						cellF.setCellValue(record1.getR27_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 28 / Col B =====
					row = sheet.getRow(27);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR28_security_firm() != null)
						cellB.setCellValue(record1.getR28_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R28 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR28_credit_rating() != null)
						cellC.setCellValue(record1.getR28_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R28 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR28_rating_agency() != null)
						cellD.setCellValue(record1.getR28_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R28 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR28_exposure_amount() != null)
						cellE.setCellValue(record1.getR28_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R28 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR28_risk_weight() != null)
						cellF.setCellValue(record1.getR28_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 29 / Col B =====
					row = sheet.getRow(28);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR29_security_firm() != null)
						cellB.setCellValue(record1.getR29_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R29 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR29_credit_rating() != null)
						cellC.setCellValue(record1.getR29_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R29 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR29_rating_agency() != null)
						cellD.setCellValue(record1.getR29_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R29 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR29_exposure_amount() != null)
						cellE.setCellValue(record1.getR29_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R29 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR29_risk_weight() != null)
						cellF.setCellValue(record1.getR29_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 30 / Col B =====
					row = sheet.getRow(29);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR30_security_firm() != null)
						cellB.setCellValue(record1.getR30_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R30 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR30_credit_rating() != null)
						cellC.setCellValue(record1.getR30_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R30 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR30_rating_agency() != null)
						cellD.setCellValue(record1.getR30_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R30 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR30_exposure_amount() != null)
						cellE.setCellValue(record1.getR30_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R30 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR30_risk_weight() != null)
						cellF.setCellValue(record1.getR30_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 31 / Col B =====
					row = sheet.getRow(30);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR31_security_firm() != null)
						cellB.setCellValue(record1.getR31_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R31 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR31_credit_rating() != null)
						cellC.setCellValue(record1.getR31_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R31 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR31_rating_agency() != null)
						cellD.setCellValue(record1.getR31_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R31 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR31_exposure_amount() != null)
						cellE.setCellValue(record1.getR31_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R31 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR31_risk_weight() != null)
						cellF.setCellValue(record1.getR31_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 32 / Col B =====
					row = sheet.getRow(31);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR32_security_firm() != null)
						cellB.setCellValue(record1.getR32_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R32 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR32_credit_rating() != null)
						cellC.setCellValue(record1.getR32_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R32 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR32_rating_agency() != null)
						cellD.setCellValue(record1.getR32_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R32 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR32_exposure_amount() != null)
						cellE.setCellValue(record1.getR32_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R32 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR32_risk_weight() != null)
						cellF.setCellValue(record1.getR32_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 33 / Col B =====
					row = sheet.getRow(32);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR33_security_firm() != null)
						cellB.setCellValue(record1.getR33_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R33 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR33_credit_rating() != null)
						cellC.setCellValue(record1.getR33_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R33 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR33_rating_agency() != null)
						cellD.setCellValue(record1.getR33_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R33 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR33_exposure_amount() != null)
						cellE.setCellValue(record1.getR33_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R33 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR33_risk_weight() != null)
						cellF.setCellValue(record1.getR33_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 34 / Col B =====
					row = sheet.getRow(33);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR34_security_firm() != null)
						cellB.setCellValue(record1.getR34_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R34 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR34_credit_rating() != null)
						cellC.setCellValue(record1.getR34_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R34 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR34_rating_agency() != null)
						cellD.setCellValue(record1.getR34_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R34 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR34_exposure_amount() != null)
						cellE.setCellValue(record1.getR34_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R34 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR34_risk_weight() != null)
						cellF.setCellValue(record1.getR34_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 35 / Col B =====
					row = sheet.getRow(34);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR35_security_firm() != null)
						cellB.setCellValue(record1.getR35_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R35 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR35_credit_rating() != null)
						cellC.setCellValue(record1.getR35_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R35 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR35_rating_agency() != null)
						cellD.setCellValue(record1.getR35_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R35 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR35_exposure_amount() != null)
						cellE.setCellValue(record1.getR35_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R35 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR35_risk_weight() != null)
						cellF.setCellValue(record1.getR35_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 36 / Col B =====
					row = sheet.getRow(35);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR36_security_firm() != null)
						cellB.setCellValue(record1.getR36_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R36 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR36_credit_rating() != null)
						cellC.setCellValue(record1.getR36_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R36 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR36_rating_agency() != null)
						cellD.setCellValue(record1.getR36_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R36 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR36_exposure_amount() != null)
						cellE.setCellValue(record1.getR36_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R36 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR36_risk_weight() != null)
						cellF.setCellValue(record1.getR36_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 37 / Col B =====
					row = sheet.getRow(36);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR37_security_firm() != null)
						cellB.setCellValue(record1.getR37_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R37 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR37_credit_rating() != null)
						cellC.setCellValue(record1.getR37_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R37 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR37_rating_agency() != null)
						cellD.setCellValue(record1.getR37_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R37 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR37_exposure_amount() != null)
						cellE.setCellValue(record1.getR37_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R37 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR37_risk_weight() != null)
						cellF.setCellValue(record1.getR37_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 38 / Col B =====
					row = sheet.getRow(37);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR38_security_firm() != null)
						cellB.setCellValue(record1.getR38_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R38 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR38_credit_rating() != null)
						cellC.setCellValue(record1.getR38_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R38 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR38_rating_agency() != null)
						cellD.setCellValue(record1.getR38_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R38 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR38_exposure_amount() != null)
						cellE.setCellValue(record1.getR38_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R38 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR38_risk_weight() != null)
						cellF.setCellValue(record1.getR38_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 39 / Col B =====
					row = sheet.getRow(38);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR39_security_firm() != null)
						cellB.setCellValue(record1.getR39_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R39 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR39_credit_rating() != null)
						cellC.setCellValue(record1.getR39_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R39 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR39_rating_agency() != null)
						cellD.setCellValue(record1.getR39_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R39 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR39_exposure_amount() != null)
						cellE.setCellValue(record1.getR39_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R39 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR39_risk_weight() != null)
						cellF.setCellValue(record1.getR39_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 40 / Col B =====
					row = sheet.getRow(39);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR40_security_firm() != null)
						cellB.setCellValue(record1.getR40_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R40 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR40_credit_rating() != null)
						cellC.setCellValue(record1.getR40_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R40 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR40_rating_agency() != null)
						cellD.setCellValue(record1.getR40_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R40 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR40_exposure_amount() != null)
						cellE.setCellValue(record1.getR40_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R40 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR40_risk_weight() != null)
						cellF.setCellValue(record1.getR40_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 41 / Col B =====
					row = sheet.getRow(40);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR41_security_firm() != null)
						cellB.setCellValue(record1.getR41_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R41 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR41_credit_rating() != null)
						cellC.setCellValue(record1.getR41_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R41 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR41_rating_agency() != null)
						cellD.setCellValue(record1.getR41_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R41 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR41_exposure_amount() != null)
						cellE.setCellValue(record1.getR41_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R41 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR41_risk_weight() != null)
						cellF.setCellValue(record1.getR41_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 42 / Col B =====
					row = sheet.getRow(41);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR42_security_firm() != null)
						cellB.setCellValue(record1.getR42_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R42 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR42_credit_rating() != null)
						cellC.setCellValue(record1.getR42_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R42 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR42_rating_agency() != null)
						cellD.setCellValue(record1.getR42_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R42 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR42_exposure_amount() != null)
						cellE.setCellValue(record1.getR42_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R42 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR42_risk_weight() != null)
						cellF.setCellValue(record1.getR42_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 43 / Col B =====
					row = sheet.getRow(42);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR43_security_firm() != null)
						cellB.setCellValue(record1.getR43_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R43 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR43_credit_rating() != null)
						cellC.setCellValue(record1.getR43_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R43 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR43_rating_agency() != null)
						cellD.setCellValue(record1.getR43_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R43 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR43_exposure_amount() != null)
						cellE.setCellValue(record1.getR43_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R43 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR43_risk_weight() != null)
						cellF.setCellValue(record1.getR43_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 44 / Col B =====
					row = sheet.getRow(43);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR44_security_firm() != null)
						cellB.setCellValue(record1.getR44_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R44 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR44_credit_rating() != null)
						cellC.setCellValue(record1.getR44_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R44 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR44_rating_agency() != null)
						cellD.setCellValue(record1.getR44_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R44 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR44_exposure_amount() != null)
						cellE.setCellValue(record1.getR44_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R44 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR44_risk_weight() != null)
						cellF.setCellValue(record1.getR44_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 45 / Col B =====
					row = sheet.getRow(44);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR45_security_firm() != null)
						cellB.setCellValue(record1.getR45_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R45 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR45_credit_rating() != null)
						cellC.setCellValue(record1.getR45_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R45 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR45_rating_agency() != null)
						cellD.setCellValue(record1.getR45_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R45 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR45_exposure_amount() != null)
						cellE.setCellValue(record1.getR45_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R45 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR45_risk_weight() != null)
						cellF.setCellValue(record1.getR45_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 46 / Col B =====
					row = sheet.getRow(45);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR46_security_firm() != null)
						cellB.setCellValue(record1.getR46_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R46 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR46_credit_rating() != null)
						cellC.setCellValue(record1.getR46_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R46 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR46_rating_agency() != null)
						cellD.setCellValue(record1.getR46_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R46 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR46_exposure_amount() != null)
						cellE.setCellValue(record1.getR46_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R46 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR46_risk_weight() != null)
						cellF.setCellValue(record1.getR46_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 47 / Col B =====
					row = sheet.getRow(46);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR47_security_firm() != null)
						cellB.setCellValue(record1.getR47_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R47 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR47_credit_rating() != null)
						cellC.setCellValue(record1.getR47_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R47 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR47_rating_agency() != null)
						cellD.setCellValue(record1.getR47_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R47 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR47_exposure_amount() != null)
						cellE.setCellValue(record1.getR47_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R47 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR47_risk_weight() != null)
						cellF.setCellValue(record1.getR47_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 48 / Col B =====
					row = sheet.getRow(47);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR48_security_firm() != null)
						cellB.setCellValue(record1.getR48_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R48 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR48_credit_rating() != null)
						cellC.setCellValue(record1.getR48_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R48 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR48_rating_agency() != null)
						cellD.setCellValue(record1.getR48_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R48 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR48_exposure_amount() != null)
						cellE.setCellValue(record1.getR48_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R48 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR48_risk_weight() != null)
						cellF.setCellValue(record1.getR48_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 49 / Col B =====
					row = sheet.getRow(48);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR49_security_firm() != null)
						cellB.setCellValue(record1.getR49_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R49 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR49_credit_rating() != null)
						cellC.setCellValue(record1.getR49_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R49 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR49_rating_agency() != null)
						cellD.setCellValue(record1.getR49_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R49 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR49_exposure_amount() != null)
						cellE.setCellValue(record1.getR49_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R49 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR49_risk_weight() != null)
						cellF.setCellValue(record1.getR49_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 50 / Col B =====
					row = sheet.getRow(49);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR50_security_firm() != null)
						cellB.setCellValue(record1.getR50_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R50 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR50_credit_rating() != null)
						cellC.setCellValue(record1.getR50_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R50 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR50_rating_agency() != null)
						cellD.setCellValue(record1.getR50_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R50 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR50_exposure_amount() != null)
						cellE.setCellValue(record1.getR50_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R50 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR50_risk_weight() != null)
						cellF.setCellValue(record1.getR50_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 51 / Col B =====
					row = sheet.getRow(50);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR51_security_firm() != null)
						cellB.setCellValue(record1.getR51_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R51 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR51_credit_rating() != null)
						cellC.setCellValue(record1.getR51_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R51 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR51_rating_agency() != null)
						cellD.setCellValue(record1.getR51_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R51 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR51_exposure_amount() != null)
						cellE.setCellValue(record1.getR51_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R51 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR51_risk_weight() != null)
						cellF.setCellValue(record1.getR51_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 52 / Col B =====
					row = sheet.getRow(51);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR52_security_firm() != null)
						cellB.setCellValue(record1.getR52_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R52 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR52_credit_rating() != null)
						cellC.setCellValue(record1.getR52_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R52 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR52_rating_agency() != null)
						cellD.setCellValue(record1.getR52_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R52 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR52_exposure_amount() != null)
						cellE.setCellValue(record1.getR52_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R52 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR52_risk_weight() != null)
						cellF.setCellValue(record1.getR52_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 53 / Col B =====
					row = sheet.getRow(52);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR53_security_firm() != null)
						cellB.setCellValue(record1.getR53_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R53 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR53_credit_rating() != null)
						cellC.setCellValue(record1.getR53_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R53 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR53_rating_agency() != null)
						cellD.setCellValue(record1.getR53_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R53 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR53_exposure_amount() != null)
						cellE.setCellValue(record1.getR53_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R53 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR53_risk_weight() != null)
						cellF.setCellValue(record1.getR53_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 54 / Col B =====
					row = sheet.getRow(53);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR54_security_firm() != null)
						cellB.setCellValue(record1.getR54_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R54 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR54_credit_rating() != null)
						cellC.setCellValue(record1.getR54_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R54 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR54_rating_agency() != null)
						cellD.setCellValue(record1.getR54_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R54 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR54_exposure_amount() != null)
						cellE.setCellValue(record1.getR54_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R54 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR54_risk_weight() != null)
						cellF.setCellValue(record1.getR54_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 55 / Col B =====
					row = sheet.getRow(54);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR55_security_firm() != null)
						cellB.setCellValue(record1.getR55_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R55 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR55_credit_rating() != null)
						cellC.setCellValue(record1.getR55_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R55 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR55_rating_agency() != null)
						cellD.setCellValue(record1.getR55_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R55 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR55_exposure_amount() != null)
						cellE.setCellValue(record1.getR55_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R55 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR55_risk_weight() != null)
						cellF.setCellValue(record1.getR55_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 56 / Col B =====
					row = sheet.getRow(55);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR56_security_firm() != null)
						cellB.setCellValue(record1.getR56_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R56 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR56_credit_rating() != null)
						cellC.setCellValue(record1.getR56_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R56 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR56_rating_agency() != null)
						cellD.setCellValue(record1.getR56_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R56 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR56_exposure_amount() != null)
						cellE.setCellValue(record1.getR56_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R56 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR56_risk_weight() != null)
						cellF.setCellValue(record1.getR56_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 57 / Col B =====
					row = sheet.getRow(56);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR57_security_firm() != null)
						cellB.setCellValue(record1.getR57_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R57 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR57_credit_rating() != null)
						cellC.setCellValue(record1.getR57_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R57 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR57_rating_agency() != null)
						cellD.setCellValue(record1.getR57_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R57 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR57_exposure_amount() != null)
						cellE.setCellValue(record1.getR57_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R57 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR57_risk_weight() != null)
						cellF.setCellValue(record1.getR57_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 58 / Col B =====
					row = sheet.getRow(57);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR58_security_firm() != null)
						cellB.setCellValue(record1.getR58_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R58 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR58_credit_rating() != null)
						cellC.setCellValue(record1.getR58_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R58 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR58_rating_agency() != null)
						cellD.setCellValue(record1.getR58_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R58 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR58_exposure_amount() != null)
						cellE.setCellValue(record1.getR58_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R58 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR58_risk_weight() != null)
						cellF.setCellValue(record1.getR58_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 59 / Col B =====
					row = sheet.getRow(58);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR59_security_firm() != null)
						cellB.setCellValue(record1.getR59_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R59 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR59_credit_rating() != null)
						cellC.setCellValue(record1.getR59_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R59 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR59_rating_agency() != null)
						cellD.setCellValue(record1.getR59_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R59 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR59_exposure_amount() != null)
						cellE.setCellValue(record1.getR59_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R59 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR59_risk_weight() != null)
						cellF.setCellValue(record1.getR59_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 60 / Col B =====
					row = sheet.getRow(59);
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR60_security_firm() != null)
						cellB.setCellValue(record1.getR60_security_firm());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R60 / Col C =====
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR60_credit_rating() != null)
						cellC.setCellValue(record1.getR60_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R60 / Col D =====
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR60_rating_agency() != null)
						cellD.setCellValue(record1.getR60_rating_agency());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R60 / Col E =====
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					if (record1.getR60_exposure_amount() != null)
						cellE.setCellValue(record1.getR60_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R60 / Col F =====
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					if (record1.getR60_risk_weight() != null)
						cellF.setCellValue(record1.getR60_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

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
	public byte[] BRRS_M_SRWA_12GResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_SRWA_12G_Resub_Summary_Entity> dataList = M_SRWA_12G_Resub_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_SRWA_12G report. Returning empty result.");
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
					M_SRWA_12G_Resub_Summary_Entity record1 = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell cellB, cellC, cellD, cellE, cellF;
					CellStyle originalStyle;

					// ===== Row 11 / Col B =====
					row = sheet.getRow(10);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_security_firm() != null)
						cellB.setCellValue(record1.getR11_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R11 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR11_credit_rating() != null)
						cellC.setCellValue(record1.getR11_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R11 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_rating_agency() != null)
						cellD.setCellValue(record1.getR11_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R11 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR11_exposure_amount() != null)
						cellE.setCellValue(record1.getR11_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R11 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR11_risk_weight() != null)
						cellF.setCellValue(record1.getR11_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R11 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR11_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR11_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 12 / Col B =====
					row = sheet.getRow(11);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_security_firm() != null)
						cellB.setCellValue(record1.getR12_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R12 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR12_credit_rating() != null)
						cellC.setCellValue(record1.getR12_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R12 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_rating_agency() != null)
						cellD.setCellValue(record1.getR12_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R12 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR12_exposure_amount() != null)
						cellE.setCellValue(record1.getR12_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R12 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR12_risk_weight() != null)
						cellF.setCellValue(record1.getR12_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R12 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR12_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR12_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 13 / Col B =====
					row = sheet.getRow(12);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR13_security_firm() != null)
						cellB.setCellValue(record1.getR13_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R13 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR13_credit_rating() != null)
						cellC.setCellValue(record1.getR13_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R13 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR13_rating_agency() != null)
						cellD.setCellValue(record1.getR13_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R13 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR13_exposure_amount() != null)
						cellE.setCellValue(record1.getR13_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R13 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR13_risk_weight() != null)
						cellF.setCellValue(record1.getR13_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R13 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR13_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR13_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 14 / Col B =====
					row = sheet.getRow(13);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR14_security_firm() != null)
						cellB.setCellValue(record1.getR14_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R14 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR14_credit_rating() != null)
						cellC.setCellValue(record1.getR14_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R14 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR14_rating_agency() != null)
						cellD.setCellValue(record1.getR14_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R14 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR14_exposure_amount() != null)
						cellE.setCellValue(record1.getR14_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R14 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR14_risk_weight() != null)
						cellF.setCellValue(record1.getR14_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R14 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR14_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR14_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 15 / Col B =====
					row = sheet.getRow(14);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR15_security_firm() != null)
						cellB.setCellValue(record1.getR15_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R15 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR15_credit_rating() != null)
						cellC.setCellValue(record1.getR15_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R15 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR15_rating_agency() != null)
						cellD.setCellValue(record1.getR15_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R15 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR15_exposure_amount() != null)
						cellE.setCellValue(record1.getR15_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R15 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR15_risk_weight() != null)
						cellF.setCellValue(record1.getR15_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R15 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR15_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR15_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);
					// ===== Row 16 / Col B =====
					row = sheet.getRow(15);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR16_security_firm() != null)
						cellB.setCellValue(record1.getR16_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R16 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR16_credit_rating() != null)
						cellC.setCellValue(record1.getR16_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R16 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR16_rating_agency() != null)
						cellD.setCellValue(record1.getR16_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R16 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR16_exposure_amount() != null)
						cellE.setCellValue(record1.getR16_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R16 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR16_risk_weight() != null)
						cellF.setCellValue(record1.getR16_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R16 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR16_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR16_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 17 / Col B =====
					row = sheet.getRow(16);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR17_security_firm() != null)
						cellB.setCellValue(record1.getR17_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R17 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR17_credit_rating() != null)
						cellC.setCellValue(record1.getR17_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R17 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR17_rating_agency() != null)
						cellD.setCellValue(record1.getR17_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R17 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR17_exposure_amount() != null)
						cellE.setCellValue(record1.getR17_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R17 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR17_risk_weight() != null)
						cellF.setCellValue(record1.getR17_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R17 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR17_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR17_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 18 / Col B =====
					row = sheet.getRow(17);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR18_security_firm() != null)
						cellB.setCellValue(record1.getR18_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R18 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR18_credit_rating() != null)
						cellC.setCellValue(record1.getR18_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R18 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR18_rating_agency() != null)
						cellD.setCellValue(record1.getR18_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R18 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR18_exposure_amount() != null)
						cellE.setCellValue(record1.getR18_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R18 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR18_risk_weight() != null)
						cellF.setCellValue(record1.getR18_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R18 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR18_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR18_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 19 / Col B =====
					row = sheet.getRow(18);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR19_security_firm() != null)
						cellB.setCellValue(record1.getR19_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R19 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR19_credit_rating() != null)
						cellC.setCellValue(record1.getR19_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R19 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR19_rating_agency() != null)
						cellD.setCellValue(record1.getR19_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R19 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR19_exposure_amount() != null)
						cellE.setCellValue(record1.getR19_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R19 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR19_risk_weight() != null)
						cellF.setCellValue(record1.getR19_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R19 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR19_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR19_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 20 / Col B =====
					row = sheet.getRow(19);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR20_security_firm() != null)
						cellB.setCellValue(record1.getR20_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R20 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR20_credit_rating() != null)
						cellC.setCellValue(record1.getR20_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R20 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR20_rating_agency() != null)
						cellD.setCellValue(record1.getR20_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R20 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR20_exposure_amount() != null)
						cellE.setCellValue(record1.getR20_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R20 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR20_risk_weight() != null)
						cellF.setCellValue(record1.getR20_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R20 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR20_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR20_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 21 / Col B =====
					row = sheet.getRow(20);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR21_security_firm() != null)
						cellB.setCellValue(record1.getR21_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R21 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR21_credit_rating() != null)
						cellC.setCellValue(record1.getR21_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R21 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR21_rating_agency() != null)
						cellD.setCellValue(record1.getR21_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R21 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR21_exposure_amount() != null)
						cellE.setCellValue(record1.getR21_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R21 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR21_risk_weight() != null)
						cellF.setCellValue(record1.getR21_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R21 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR21_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR21_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 22 / Col B =====
					row = sheet.getRow(21);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR22_security_firm() != null)
						cellB.setCellValue(record1.getR22_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R22 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR22_credit_rating() != null)
						cellC.setCellValue(record1.getR22_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R22 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR22_rating_agency() != null)
						cellD.setCellValue(record1.getR22_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R22 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR22_exposure_amount() != null)
						cellE.setCellValue(record1.getR22_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R22 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR22_risk_weight() != null)
						cellF.setCellValue(record1.getR22_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R22 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR22_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR22_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 23 / Col B =====
					row = sheet.getRow(22);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR23_security_firm() != null)
						cellB.setCellValue(record1.getR23_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R23 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR23_credit_rating() != null)
						cellC.setCellValue(record1.getR23_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R23 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR23_rating_agency() != null)
						cellD.setCellValue(record1.getR23_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R23 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR23_exposure_amount() != null)
						cellE.setCellValue(record1.getR23_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R23 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR23_risk_weight() != null)
						cellF.setCellValue(record1.getR23_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R23 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR23_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR23_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 24 / Col B =====
					row = sheet.getRow(23);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR24_security_firm() != null)
						cellB.setCellValue(record1.getR24_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R24 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR24_credit_rating() != null)
						cellC.setCellValue(record1.getR24_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R24 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR24_rating_agency() != null)
						cellD.setCellValue(record1.getR24_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R24 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR24_exposure_amount() != null)
						cellE.setCellValue(record1.getR24_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R24 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR24_risk_weight() != null)
						cellF.setCellValue(record1.getR24_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R24 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR24_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR24_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 25 / Col B =====
					row = sheet.getRow(24);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR25_security_firm() != null)
						cellB.setCellValue(record1.getR25_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R25 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR25_credit_rating() != null)
						cellC.setCellValue(record1.getR25_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R25 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR25_rating_agency() != null)
						cellD.setCellValue(record1.getR25_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R25 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR25_exposure_amount() != null)
						cellE.setCellValue(record1.getR25_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R25 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR25_risk_weight() != null)
						cellF.setCellValue(record1.getR25_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R25 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR25_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR25_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 26 / Col B =====
					row = sheet.getRow(25);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR26_security_firm() != null)
						cellB.setCellValue(record1.getR26_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R26 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR26_credit_rating() != null)
						cellC.setCellValue(record1.getR26_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R26 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR26_rating_agency() != null)
						cellD.setCellValue(record1.getR26_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R26 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR26_exposure_amount() != null)
						cellE.setCellValue(record1.getR26_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R26 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR26_risk_weight() != null)
						cellF.setCellValue(record1.getR26_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R26 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR26_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR26_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 27 / Col B =====
					row = sheet.getRow(26);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR27_security_firm() != null)
						cellB.setCellValue(record1.getR27_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R27 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR27_credit_rating() != null)
						cellC.setCellValue(record1.getR27_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R27 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR27_rating_agency() != null)
						cellD.setCellValue(record1.getR27_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R27 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR27_exposure_amount() != null)
						cellE.setCellValue(record1.getR27_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R27 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR27_risk_weight() != null)
						cellF.setCellValue(record1.getR27_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R27 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR27_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR27_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 28 / Col B =====
					row = sheet.getRow(27);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR28_security_firm() != null)
						cellB.setCellValue(record1.getR28_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R28 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR28_credit_rating() != null)
						cellC.setCellValue(record1.getR28_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R28 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR28_rating_agency() != null)
						cellD.setCellValue(record1.getR28_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R28 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR28_exposure_amount() != null)
						cellE.setCellValue(record1.getR28_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R28 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR28_risk_weight() != null)
						cellF.setCellValue(record1.getR28_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R28 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR28_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR28_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 29 / Col B =====
					row = sheet.getRow(28);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR29_security_firm() != null)
						cellB.setCellValue(record1.getR29_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R29 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR29_credit_rating() != null)
						cellC.setCellValue(record1.getR29_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R29 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR29_rating_agency() != null)
						cellD.setCellValue(record1.getR29_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R29 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR29_exposure_amount() != null)
						cellE.setCellValue(record1.getR29_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R29 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR29_risk_weight() != null)
						cellF.setCellValue(record1.getR29_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R29 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR29_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR29_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 30 / Col B =====
					row = sheet.getRow(29);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR30_security_firm() != null)
						cellB.setCellValue(record1.getR30_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R30 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR30_credit_rating() != null)
						cellC.setCellValue(record1.getR30_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R30 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR30_rating_agency() != null)
						cellD.setCellValue(record1.getR30_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R30 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR30_exposure_amount() != null)
						cellE.setCellValue(record1.getR30_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R30 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR30_risk_weight() != null)
						cellF.setCellValue(record1.getR30_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R30 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR30_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR30_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 31 / Col B =====
					row = sheet.getRow(30);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR31_security_firm() != null)
						cellB.setCellValue(record1.getR31_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R31 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR31_credit_rating() != null)
						cellC.setCellValue(record1.getR31_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R31 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR31_rating_agency() != null)
						cellD.setCellValue(record1.getR31_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R31 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR31_exposure_amount() != null)
						cellE.setCellValue(record1.getR31_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R31 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR31_risk_weight() != null)
						cellF.setCellValue(record1.getR31_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R31 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR31_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR31_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 32 / Col B =====
					row = sheet.getRow(31);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR32_security_firm() != null)
						cellB.setCellValue(record1.getR32_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R32 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR32_credit_rating() != null)
						cellC.setCellValue(record1.getR32_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R32 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR32_rating_agency() != null)
						cellD.setCellValue(record1.getR32_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R32 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR32_exposure_amount() != null)
						cellE.setCellValue(record1.getR32_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R32 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR32_risk_weight() != null)
						cellF.setCellValue(record1.getR32_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R32 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR32_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR32_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 33 / Col B =====
					row = sheet.getRow(32);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR33_security_firm() != null)
						cellB.setCellValue(record1.getR33_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R33 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR33_credit_rating() != null)
						cellC.setCellValue(record1.getR33_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R33 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR33_rating_agency() != null)
						cellD.setCellValue(record1.getR33_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R33 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR33_exposure_amount() != null)
						cellE.setCellValue(record1.getR33_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R33 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR33_risk_weight() != null)
						cellF.setCellValue(record1.getR33_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R33 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR33_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR33_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 34 / Col B =====
					row = sheet.getRow(33);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR34_security_firm() != null)
						cellB.setCellValue(record1.getR34_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R34 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR34_credit_rating() != null)
						cellC.setCellValue(record1.getR34_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R34 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR34_rating_agency() != null)
						cellD.setCellValue(record1.getR34_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R34 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR34_exposure_amount() != null)
						cellE.setCellValue(record1.getR34_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R34 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR34_risk_weight() != null)
						cellF.setCellValue(record1.getR34_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R34 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR34_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR34_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 35 / Col B =====
					row = sheet.getRow(34);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR35_security_firm() != null)
						cellB.setCellValue(record1.getR35_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R35 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR35_credit_rating() != null)
						cellC.setCellValue(record1.getR35_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R35 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR35_rating_agency() != null)
						cellD.setCellValue(record1.getR35_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R35 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR35_exposure_amount() != null)
						cellE.setCellValue(record1.getR35_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R35 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR35_risk_weight() != null)
						cellF.setCellValue(record1.getR35_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R35 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR35_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR35_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 36 / Col B =====
					row = sheet.getRow(35);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR36_security_firm() != null)
						cellB.setCellValue(record1.getR36_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R36 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR36_credit_rating() != null)
						cellC.setCellValue(record1.getR36_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R36 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR36_rating_agency() != null)
						cellD.setCellValue(record1.getR36_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R36 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR36_exposure_amount() != null)
						cellE.setCellValue(record1.getR36_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R36 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR36_risk_weight() != null)
						cellF.setCellValue(record1.getR36_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R36 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR36_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR36_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== Row 37 / Col B =====
					row = sheet.getRow(36);
					cellB = row.getCell(0);
					if (cellB == null)
						cellB = row.createCell(0);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR37_security_firm() != null)
						cellB.setCellValue(record1.getR37_security_firm()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R37 / Col C =====

					cellC = row.getCell(1);
					if (cellC == null)
						cellC = row.createCell(1);
					originalStyle = cellC.getCellStyle();
					if (record1.getR37_credit_rating() != null)
						cellC.setCellValue(record1.getR37_credit_rating().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R37 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR37_rating_agency() != null)
						cellD.setCellValue(record1.getR37_rating_agency()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R37 / Col E =====

					cellE = row.getCell(5);
					if (cellE == null)
						cellE = row.createCell(5);
					originalStyle = cellE.getCellStyle();
					if (record1.getR37_exposure_amount() != null)
						cellE.setCellValue(record1.getR37_exposure_amount().doubleValue());
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R37 / Col F =====

					cellF = row.getCell(6);
					if (cellF == null)
						cellF = row.createCell(6);
					originalStyle = cellF.getCellStyle();
					if (record1.getR37_risk_weight() != null)
						cellF.setCellValue(record1.getR37_risk_weight().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R37 / Col G =====
					cellF = row.getCell(7);
					if (cellF == null)
						cellF = row.createCell(7);
					originalStyle = cellF.getCellStyle();
					if (record1.getR37_risk_weighted_amount() != null)
						cellF.setCellValue(record1.getR37_risk_weighted_amount().doubleValue());
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);
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